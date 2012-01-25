/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;

import java.util.Enumeration;
import java.util.Vector;

import lib.util.Properties;

/**
 * This software is part of NanoXML. Copyright (C) 2000 Marc De Scheemaecker,
 * All Rights Reserved.
 *
 * <p>
 * kXMLElement is a representation of an XML object. The object is able to
 * parse XML code.
 * </p>
 *
 * <p>
 * Note that NanoXML is not 100% XML 1.0 compliant:
 *
 * <ul>
 * <li>
 * The parser is non-validating.
 * </li>
 * <li>
 * The DTD is fully ignored, including <CODE>&lt;!ENTITY...&gt;</CODE>.
 * </li>
 * <li>
 * There is no support for mixed content (elements containing both subelements
 * and CDATA elements)
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Revision:</b> 1.1<br><b>Date</b>: 2002/09/27 13:07:34
 * </p>
 *
 * @author <a href="mailto:Marc.DeScheemaecker@advalvas.be">Marc De
 *         Scheemaecker</a>
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 * @version 1.6.1
 *
 * @see com.itlity.io.XMLParseException
 */
public class kXMLElement {
  static final boolean DEBUG = false;

  /** Major version of NanoXML. */
  public static final int NANOXML_MAJOR_VERSION = 1;

  /** Minor version of NanoXML. */
  public static final int NANOXML_MINOR_VERSION = 6;

  /** Whether indentation is active. */
  public static final boolean INDENT = true;

  /**
   * Basic conversion table shared among all elements that are not initialized
   * with a specified conversion table. Thus, we avoid the creation of a new
   * conversion table every time we create a new kXMLElement.
   */
  static final Properties BASIC_CONVERSION_TABLE = new Properties();

  /**
   * Static initializer for the basic conversion table.
   */
  static {
    BASIC_CONVERSION_TABLE.put("lt", "<");
    BASIC_CONVERSION_TABLE.put("gt", ">");
    BASIC_CONVERSION_TABLE.put("quot", "\"");
    BASIC_CONVERSION_TABLE.put("apos", "'");
    BASIC_CONVERSION_TABLE.put("amp", "&");
  }

  /** The attributes given to the object. */
  private Properties attributes = null;

  /**
   * Subobjects of the object. The subobjects are of class kXMLElement
   * themselves.
   */
  private Vector children = null;

  /** The class of the object (the name indicated in the tag). */
  private String tagName = null;

  /** Conversion table for &amp;...; tags. */
  private Properties conversionTable = null;

  /** Whether to skip leading whitespace in CDATA. */
  private boolean skipLeadingWhitespace = false;

  /** The line number where the element starts. */
  private int lineNr = 0;

  /**
   * Creates a new XML element. The following settings are used:
   *
   * <DL>
   * <dt>
   * Conversion table
   * </dt>
   * <dd>
   * Minimal XML conversions: <CODE>&amp;amp; &amp;lt; &amp;gt; &amp;apos;
   * &amp;quot;</CODE>
   * </dd>
   * <dt>
   * Skip whitespace in contents
   * </dt>
   * <dd>
   * <CODE>false</CODE>
   * </dd>
   * </dl>
   */
  public kXMLElement() throws XMLParseException {
    this(BASIC_CONVERSION_TABLE, false, false);
  }

  /**
   * Creates a new XML element. The following settings are used:
   *
   * <DL>
   * <dt>
   * Conversion table
   * </dt>
   * <dd>
   * <I>conversionTable</I> combined with the minimal XML conversions:
   * <CODE>&amp;amp; &amp;lt; &amp;gt; &amp;apos; &amp;quot;</CODE>
   * </dd>
   * <dt>
   * Skip whitespace in contents
   * </dt>
   * <dd>
   * <CODE>false</CODE>
   * </dd>
   * </dl>
   */
  public kXMLElement(Properties conversionTable) {
    this(conversionTable, false, true);
  }

  /**
   * Creates a new XML element. The following settings are used:
   *
   * <DL>
   * <dt>
   * Conversion table
   * </dt>
   * <dd>
   * Minimal XML conversions: <CODE>&amp;amp; &amp;lt; &amp;gt; &amp;apos;
   * &amp;quot;</CODE>
   * </dd>
   * <dt>
   * Skip whitespace in contents
   * </dt>
   * <dd>
   * <I>skipLeadingWhitespace</I>
   * </dd>
   * </dl>
   */
  public kXMLElement(boolean skipLeadingWhitespace) {
    this(BASIC_CONVERSION_TABLE, skipLeadingWhitespace, false);
  }

  /**
   * Creates a new XML element. The following settings are used:
   *
   * <DL>
   * <dt>
   * Conversion table
   * </dt>
   * <dd>
   * <I>conversionTable</I> combined with the minimal XML conversions:
   * <CODE>&amp;amp; &amp;lt; &amp;gt; &amp;apos; &amp;quot;</CODE>
   * </dd>
   * <dt>
   * Skip whitespace in contents
   * </dt>
   * <dd>
   * <I>skipLeadingWhitespace</I>
   * </dd>
   * <dt>
   * Ignore Case
   * </dt>
   * <dd>
   * <CODE>true</CODE>
   * </dd>
   * </dl>
   */
  public kXMLElement(
    Properties conversionTable, boolean skipLeadingWhitespace) {
    this(conversionTable, skipLeadingWhitespace, true);
  }

  /**
   * Creates a new XML element. The following settings are used:
   *
   * <DL>
   * <dt>
   * Conversion table
   * </dt>
   * <dd>
   * <I>conversionTable</I>, eventually combined with the minimal XML
   * conversions: <CODE>&amp;amp; &amp;lt; &amp;gt; &amp;apos;
   * &amp;quot;</CODE> (depending on <I>fillBasicConversionTable</I>)
   * </dd>
   * <dt>
   * Skip whitespace in contents
   * </dt>
   * <dd>
   * <I>skipLeadingWhitespace</I>
   * </dd>
   * <dt>
   * Ignore Case
   * </dt>
   * <dd>
   * <I>ignoreCase</I>
   * </dd>
   * </dl>
   *
   * <P>
   * This constructor should <I>only</I> be called from kXMLElement itself to
   * create child elements.
   * </p>
   */
  protected kXMLElement(
    Properties conversionTable, boolean skipLeadingWhitespace,
    boolean fillBasicConversionTable) {
    this.skipLeadingWhitespace   = skipLeadingWhitespace;
    this.tagName                 = null;
    this.attributes              = new Properties();
    this.children                = new Vector();
    this.conversionTable         = conversionTable;
    this.lineNr                  = 0;

    if (fillBasicConversionTable) {
      conversionTable.put("lt", "<");
      conversionTable.put("gt", ">");
      conversionTable.put("quot", "\"");
      conversionTable.put("apos", "'");
      conversionTable.put("amp", "&");
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param args DOCUMENT ME!
   */
  public static void main(String[] args) {
    kXMLElement xml   = new kXMLElement();
    kXMLElement child = new kXMLElement();

    xml.setTagName("Person");
    xml.addProperty("firstName", "Alexis");
    xml.addProperty("name", "Bietti");
    xml.addProperty("age", "24");

    child.setTagName("Address");
    child.addProperty("street", "rue de l'hotel de ville");
    child.addProperty("city", "Nimes");
    child.addProperty("number", "6");
    child.addProperty("zip", "30000");
    child.addProperty("country", "France");

    xml.addChild(child);

    System.out.println(xml);
  }

  /**
   * Adds a subobject.
   */
  public void addChild(kXMLElement child) {
    this.children.addElement(child);
  }

  /**
   * <p>
   * Adds a property. If the element is case insensitive, the property name is
   * capitalized.
   * </p>
   *
   * <p>
   * Implementation note 1 : the implementation of insensitive case was
   * removed <br>
   * Implementation note 2 : if the value is <tt>null</tt>, the value "" is
   * substituted.
   * </p>
   */
  public void addProperty(String key, Object value) {
    if (value == null) {
      value = "";
    }

    this.attributes.put(key, value.toString());
  }

  /**
   * Adds a property. If the element is case insensitive, the property name is
   * capitalized.
   */
  public void addProperty(String key, int value) {
    this.attributes.put(key, Integer.toString(value));
  }

  /**
   * Adds a property. If the element is case insensitive, the property name is
   * capitalized.
   */
  public void addProperty(String key, long value) {
    this.attributes.put(key, Long.toString(value));
  }

  /**
   * Adds a property
   */
  public void addProperty(String key, boolean value) {
    this.attributes.put(key, value ? "1" : "0");
  }

  /**
   * Returns the number of subobjects of the object.
   */
  public int countChildren() {
    return this.children.size();
  }

  /**
   * Returns the child element at the specified index.
   *
   * @param index
   *
   * @return kXMLElement
   *
   * @throws ArrayIndexOutOfBoundsException
   */
  public kXMLElement getChild(int index) throws ArrayIndexOutOfBoundsException {
    return (kXMLElement) this.children.elementAt(index);
  }

  /**
   * Enumerates the attribute names.
   */
  public Enumeration enumeratePropertyNames() {
    return this.attributes.keys();
  }

  /**
   * Enumerates the subobjects of the object.
   */
  public Enumeration enumerateChildren() {
    return this.children.elements();
  }

  /**
   * Returns the subobjects of the object.
   */
  public Vector getChildren() {
    return this.children;
  }

  /**
   * Returns the line nr on which the element is found.
   */
  public int getLineNr() {
    return this.lineNr;
  }

  /**
   * Returns a property of the object. If there is no such property, this
   * method returns <CODE>null</CODE>.
   */
  public String getProperty(String key) {
    return this.attributes.getProperty(key);
  }

  /**
   * Returns a property of the object. If the property doesn't exist,
   * <I>defaultValue</I> is returned.
   */
  public String getProperty(String key, String defaultValue) {
    return this.attributes.getProperty(key, defaultValue);
  }

  /**
   * <p>
   * Returns an integer property of the object. If the property doesn't exist,
   * <I>defaultValue</I> is returned.
   * </p>
   *
   * <p>
   * <b> Implementation note: </b> changed the behaviour when the value exists
   * but is not a well formed integer, the default is returned instead of
   * throwing an exception.
   * </p>
   */
  public int getProperty(String key, int defaultValue) {
    String val = this.attributes.getProperty(key);

    if (val == null) {
      return defaultValue;
    } else {
      try {
        return Integer.parseInt(val);
      } catch (NumberFormatException e) {
        // throw this.invalidValue(key, val, this.lineNr);
        return defaultValue;
      }
    }
  }

  /**
   * <p>
   * Return an integer property of the object.
   * </p>
   *
   * <p>
   * This method does NOT perform any check and may throw exceptions.
   * </p>
   *
   * <p>
   * <i> Added by Alexis BIETTI, the 15-XI-2002 </i>
   * </p>
   *
   * @throws NullPointerException if the property does not exist
   * @throws NumberFormatException if the property is not a number
   */
  public int getIntProperty(String key)
    throws NullPointerException, NumberFormatException {
    return Integer.parseInt(this.attributes.getProperty(key));
  }

  /**
   * <p>
   * Return a float property of the object.
   * </p>
   *
   * <p>
   * This method does NOT perform any check and may throw exceptions.
   * </p>
   *
   * <p>
   * <i> Added by Alexis BIETTI, the 15-XI-2002 </i>
   * </p>
   *
   * @throws NullPointerException if the property does not exist
   * @throws NumberFormatException if the property is not a number
   */
  public float getFloatProperty(String key, float defaultValue) {
    try {
      return lib.math.Float.parseFloat(
        this.attributes.getProperty(key));
    } catch (Exception nfe) {
      return defaultValue;
    }
  }

  /**
   * <p>
   * Return a boolean property of the object.
   * </p>
   *
   * <p>
   * Legal boolean values of XML Schema are supported : '0', '1', 'false' and
   * 'true'.
   * </p>
   *
   * <p>
   * <i> Added by Alexis BIETTI, the 15-XI-2002 </i>
   * </p>
   *
   * @param key the name of the XML attribute
   * @param defaultValue the boolean value returned if the proterty does not
   *        exist or if it does not represent a legal boolean value.
   */
  public boolean getBooleanProperty(String key, boolean defaultValue) {
    String value = this.attributes.getProperty(key);

    if (value == null) {
      return defaultValue;
    } else if (value.equals("1") || value.equals("true")) {
      return true;
    } else if (value.equals("0") || value.equals("false")) {
      return false;
    } else {
      return defaultValue;
    }
  }

  /**
   * Returns a boolean property of the object. If the property is missing,
   * <I>defaultValue</I> is returned.
   */
  public boolean getProperty(
    String key, String trueValue, String falseValue, boolean defaultValue) {
    String val = this.attributes.getProperty(key);

    if (val == null) {
      return defaultValue;
    } else if (val.equals(trueValue)) {
      return true;
    } else if (val.equals(falseValue)) {
      return false;
    } else {
      throw this.invalidValue(key, val, this.lineNr);
    }
  }

  /**
   * Returns a property by looking up a key in the hashtable <I>valueSet</I>
   * If the property doesn't exist, the value corresponding to
   * <I>defaultValue</I>  is returned.
   */
  public Object getProperty(
    String key, Properties valueSet, String defaultValue) {
    String val = this.attributes.getProperty(key);

    if (val == null) {
      val = defaultValue;
    }

    Object result = valueSet.get(val);

    if (result == null) {
      throw this.invalidValue(key, val, this.lineNr);
    }

    return result;
  }

  /**
   * Returns a property by looking up a key in the hashtable <I>valueSet</I>.
   * If the property doesn't exist, the value corresponding to
   * <I>defaultValue</I>  is returned.
   */
  public String getStringProperty(
    String key, Properties valueSet, String defaultValue) {
    String val    = this.attributes.getProperty(key);
    String result;

    if (val == null) {
      val = defaultValue;
    }

    try {
      result = (String) (valueSet.get(val));
    } catch (ClassCastException e) {
      throw this.invalidValueSet(key);
    }

    if (result == null) {
      throw this.invalidValue(key, val, this.lineNr);
    }

    return result;
  }

  /**
   * Returns the class (i.e. the name indicated in the tag) of the object.
   */
  public String getTagName() {
    return this.tagName;
  }

  /**
   * Checks whether a character may be part of an identifier.
   *
   * <p>
   * Indentifier chars match <code>[A-Za-z0-9.\-_:]</code>
   * </p>
   */
  private static boolean isIdentifierChar(char ch) {
    return (((ch >= 'A') && (ch <= 'Z')) || ((ch >= 'a') && (ch <= 'z'))
    || ((ch >= '0') && (ch <= '9')) || (".-_:".indexOf(ch) >= 0));
  }

  /**
   * Reads an XML definition from a java.io.Reader and parses it.
   *
   * @exception java.io.IOException if an error occured while reading the
   *            input
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the read data
   */
  public void parseFromReader(Reader reader)
    throws IOException, XMLParseException {
    this.parseFromReader(reader, 1);
  }

  /**
   * Reads an XML definition from a java.io.Reader and parses it.
   *
   * @exception java.io.IOException if an error occured while reading the
   *            input
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the read data
   */
  public void parseFromReader(Reader reader, int startingLineNr)
    throws IOException, XMLParseException {
    int    blockSize = 4096;
    char[] input = null;
    int    size  = 0;

    for (;;) {
      if (input == null) {
        input = new char[blockSize];
      } else {
        char[] oldInput = input;
        input = new char[input.length + blockSize];
        System.arraycopy(oldInput, 0, input, 0, oldInput.length);
      }

      int charsRead = reader.read(input, size, blockSize);

      if (charsRead < 0) {
        break;
      }

      size += charsRead;
    }

    this.parseCharArray(input, 0, size, startingLineNr);
  }

  /**
   * Parses an XML definition.
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the string
   */
  public void parseString(String string) throws XMLParseException {
    this.parseCharArray(string.toCharArray(), 0, string.length(), 1);
  }

  /**
   * Parses an XML definition starting at <I>offset</I>.
   *
   * @return the offset of the string following the XML data
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the string
   */
  public int parseString(String string, int offset) throws XMLParseException {
    return this.parseCharArray(
      string.toCharArray(), offset, string.length(), 1);
  }

  /**
   * Parses an XML definition starting at <I>offset</I>.
   *
   * @return the offset of the string following the XML data (&lt;= end)
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the string
   */
  public int parseString(String string, int offset, int end)
    throws XMLParseException {
    return this.parseCharArray(string.toCharArray(), offset, end, 1);
  }

  /**
   * Parses an XML definition starting at <I>offset</I>.
   *
   * @return the offset of the string following the XML data (&lt;= end)
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the string
   */
  public int parseString(
    String string, int offset, int end, int startingLineNr)
    throws XMLParseException {
    return this.parseCharArray(
      string.toCharArray(), offset, end, startingLineNr);
  }

  /**
   * Parses an XML definition starting at <I>offset</I>.
   *
   * @return the offset of the array following the XML data (&lt;= end)
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the array
   */
  public int parseCharArray(char[] input, int offset, int end)
    throws XMLParseException {
    return this.parseCharArray(input, offset, end, 1);
  }

  /**
   * Parses an XML definition starting at <I>offset</I>.
   *
   * @return the offset of the array following the XML data (&lt;= end)
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the array
   */
  public int parseCharArray(
    char[] input, int offset, int end, int startingLineNr)
    throws XMLParseException {
    int[] lineNr = new int[1];
    lineNr[0] = startingLineNr;

    return this.parseCharArray(input, offset, end, lineNr);
  }

  /**
   * Parses an XML definition starting at <I>offset</I>.
   *
   * @return the offset of the array following the XML data (&lt;= end)
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the array
   */
  private int parseCharArray(
    char[] input, int offset, int end, int[] currentLineNr)
    throws XMLParseException {
    this.lineNr       = currentLineNr[0];
    this.tagName      = null;
    this.attributes   = new Properties();
    this.children     = new Vector();

    try {
      offset = this.skipWhitespace(input, offset, end, currentLineNr);
    } catch (XMLParseException e) {
      return offset;
    }

    offset        = this.skipPreamble(input, offset, end, currentLineNr);
    offset        = this.scanTagName(input, offset, end, currentLineNr);
    this.lineNr   = currentLineNr[0];
    offset        = this.scanAttributes(input, offset, end, currentLineNr);

    int[] contentOffset = new int[1];
    int[] contentSize   = new int[1];
    int   contentLineNr = currentLineNr[0];
    offset =
      this.scanContent(
        input, offset, end, contentOffset, contentSize, currentLineNr);

    if (contentSize[0] > 0) {
      this.scanChildren(
        input, contentOffset[0], contentSize[0], contentLineNr);

      if (this.children.size() > 0) {
      } else {
        this.processContents(
          input, contentOffset[0], contentSize[0], contentLineNr);
      }
    }

    return offset;
  }

  /**
   * Decodes the entities in the contents and, if skipLeadingWhitespace is
   * <CODE>true</CODE>, removes extraneous whitespaces after newlines and
   * convert those newlines into spaces.
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the array
   */
  private void processContents(
    char[] input, int contentOffset, int contentSize, int contentLineNr)
    throws XMLParseException {
    int[] lineNr = new int[1];
    lineNr[0] = contentLineNr;

    if (!this.skipLeadingWhitespace) {
      String str = new String(input, contentOffset, contentSize);

      // this.contents = this.decodeString(str, lineNr[0]);
      this.decodeString(str, lineNr[0]);

      return;
    }

    StringBuffer result = new StringBuffer(contentSize);
    int          end = contentSize + contentOffset;

    for (int i = contentOffset; i < end; i++) {
      char ch = input[i];

      // The end of the contents is always a < character, so there's
      // no danger for bounds violation
      while ((ch == '\r') || (ch == '\n')) {
        lineNr[0]++;
        result.append(ch);

        i++;
        ch = input[i];

        if (ch != '\n') {
          result.append(ch);
        }

        do {
          i++;
          ch = input[i];
        } while ((ch == ' ') || (ch == '\t'));
      }

      if (i < end) {
        result.append(ch);
      }
    }

    // this.contents = this.decodeString(result.toString(), lineNr[0]);
    this.decodeString(result.toString(), lineNr[0]);
  }

  /**
   * Removes a child object. If the object is not a child, nothing happens.
   */
  public void removeChild(kXMLElement child) {
    this.children.removeElement(child);
  }

  /**
   * Removes an attribute.
   */
  public void removeChild(String key) {
    this.attributes.remove(key);
  }

  /**
   * Scans the attributes of the object.
   *
   * @return the offset in the string following the attributes, so that
   *         input[offset] in { '/', '>' }
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the array
   */
  private int scanAttributes(char[] input, int offset, int end, int[] lineNr)
    throws XMLParseException {
    String key;
    String value;

    for (;;) {
      offset = this.skipWhitespace(input, offset, end, lineNr);

      char ch = input[offset];

      if ((ch == '/') || (ch == '>')) {
        break;
      }

      offset = this.scanOneAttribute(input, offset, end, lineNr);
    }

    return offset;
  }

  /**
   * !!! Searches the content for child objects. If such objects exist, the
   * content is reduced to <CODE>null</CODE>.
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the array
   */
  protected void scanChildren(
    char[] input, int contentOffset, int contentSize, int contentLineNr)
    throws XMLParseException {
    int   end    = contentOffset + contentSize;
    int   offset = contentOffset;
    int[] lineNr = new int[1];
    lineNr[0] = contentLineNr;

    while (offset < end) {
      try {
        offset = this.skipWhitespace(input, offset, end, lineNr);
      } catch (XMLParseException e) {
        return;
      }

      if (
        (input[offset] != '<')
            || ((input[offset + 1] == '!') && (input[offset + 2] == '['))) {
        return;
      }

      kXMLElement child = this.createAnotherElement();
      offset = child.parseCharArray(input, offset, end, lineNr);
      this.children.addElement(child);
    }
  }

  /**
   * Creates a new XML element.
   */
  protected kXMLElement createAnotherElement() {
    return new kXMLElement(
      this.conversionTable, this.skipLeadingWhitespace, false);
  }

  /**
   * Scans the content of the object.
   *
   * @return the offset after the XML element; contentOffset points to the
   *         start of the content section; contentSize is the size of the
   *         content section
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the array
   */
  private int scanContent(
    char[] input, int offset, int end, int[] contentOffset, int[] contentSize,
    int[] lineNr) throws XMLParseException {
    if (input[offset] == '/') {
      contentSize[0] = 0;

      if (input[offset + 1] != '>') {
        throw this.expectedInput("'>'", lineNr[0]);
      }

      return offset + 2;
    }

    if (input[offset] != '>') {
      throw this.expectedInput("'>'", lineNr[0]);
    }

    if (this.skipLeadingWhitespace) {
      offset = this.skipWhitespace(input, offset + 1, end, lineNr);
    } else {
      offset++;
    }

    int begin = offset;
    contentOffset[0] = offset;

    int    level = 0;
    char[] tag = this.tagName.toCharArray();
    end -= (tag.length + 2);

    while ((offset < end) && (level >= 0)) {
      if (input[offset] == '<') {
        boolean ok = true;

        if ((offset < (end - 1)) && (input[offset + 1] == '!')) {
          offset++;

          continue;
        }

        for (int i = 0; ok && (i < tag.length); i++) {
          ok &= (input[offset + (i + 1)] == tag[i]);
        }

        ok &= !isIdentifierChar(input[offset + tag.length + 1]);

        if (ok) {
          while ((offset < end) && (input[offset] != '>')) {
            offset++;
          }

          if (input[offset - 1] != '/') {
            level++;
          }

          continue;
        } else if (input[offset + 1] == '/') {
          ok = true;

          for (int i = 0; ok && (i < tag.length); i++) {
            ok &= (input[offset + (i + 2)] == tag[i]);
          }

          if (ok) {
            contentSize[0] = offset - contentOffset[0];
            offset += (tag.length + 2);
            offset = this.skipWhitespace(input, offset, end, lineNr);

            if (input[offset] == '>') {
              level--;
              offset++;
            }

            continue;
          }
        }
      }

      if (input[offset] == '\r') {
        lineNr[0]++;

        if ((offset != end) && (input[offset + 1] == '\n')) {
          offset++;
        }
      } else if (input[offset] == '\n') {
        lineNr[0]++;
      }

      offset++;
    }

    if (level >= 0) {
      throw this.unexpectedEndOfData(lineNr[0]);
    }

    if (this.skipLeadingWhitespace) {
      int i = (contentOffset[0] + contentSize[0]) - 1;

      while ((contentSize[0] >= 0) && (input[i] <= ' ')) {
        i--;
        contentSize[0]--;
      }
    }

    return offset;
  }

  /**
   * Scans an identifier.
   *
   * @return the identifier, or <CODE>null</CODE> if offset doesn't point to
   *         an identifier
   */
  private String scanIdentifier(char[] input, int offset, int end) {
    int begin = offset;

    while ((offset < end) && (isIdentifierChar(input[offset]))) {
      offset++;
    }

    if ((offset == end) || (offset == begin)) {
      return null;
    } else {
      return new String(input, begin, offset - begin);
    }
  }

  /**
   * Scans one attribute of an object.
   *
   * @return the offset after the attribute
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the array
   */
  private int scanOneAttribute(
    char[] input, int offset, int end, int[] lineNr) throws XMLParseException {
    String key;
    String value;

    key = this.scanIdentifier(input, offset, end);

    if (key == null) {
      throw this.syntaxError("an attribute key", lineNr[0]);
    }

    offset = this.skipWhitespace(input, offset + key.length(), end, lineNr);

    if (input[offset] != '=') {
      throw this.valueMissingForAttribute(key, lineNr[0]);
    }

    offset   = this.skipWhitespace(input, offset + 1, end, lineNr);

    value = this.scanString(input, offset, end, lineNr);

    if (value == null) {
      throw this.syntaxError("an attribute value", lineNr[0]);
    }

    if ((value.charAt(0) == '"') || (value.charAt(0) == '\'')) {
      value = value.substring(1, (value.length() - 1));
      offset += 2;
    }

    this.attributes.put(key, this.decodeString(value, lineNr[0]));

    return offset + value.length();
  }

  /**
   * Scans a string. Strings are either identifiers, or text delimited by
   * double quotes.
   *
   * @return the string found, without delimiting double quotes; or null if
   *         offset didn't point to a valid string
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the array
   */
  private String scanString(char[] input, int offset, int end, int[] lineNr)
    throws XMLParseException {
    char delim = input[offset];

    if ((delim == '"') || (delim == '\'')) {
      int begin = offset;
      offset++;

      while ((offset < end) && (input[offset] != delim)) {
        if (input[offset] == '\r') {
          lineNr[0]++;

          if ((offset != end) && (input[offset + 1] == '\n')) {
            offset++;
          }
        } else if (input[offset] == '\n') {
          lineNr[0]++;
        }

        offset++;
      }

      if (offset == end) {
        return null;
      } else {
        return new String(input, begin, offset - begin + 1);
      }
    } else {
      return this.scanIdentifier(input, offset, end);
    }
  }

  /**
   * Scans the class (tag) name of the object.
   *
   * @return the position after the tag name
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the array
   */
  private int scanTagName(char[] input, int offset, int end, int[] lineNr)
    throws XMLParseException {
    this.tagName = this.scanIdentifier(input, offset, end);

    if (this.tagName == null) {
      throw this.syntaxError("a tag name", lineNr[0]);
    }

    return offset + this.tagName.length();
  }

  /**
   * Changes the tag name.
   *
   * @param tagName The new tag name.
   */
  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  /**
   * Skips a tag that don't contain any useful data: &lt;?...?&gt;,
   * &lt;!...&gt; and comments.
   *
   * @return the position after the tag
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the array
   */
  protected int skipBogusTag(char[] input, int offset, int end, int[] lineNr) {
    if ((input[offset + 1] == '-') && (input[offset + 2] == '-')) {
      while (
        (offset < end)
            && ((input[offset] != '-') || (input[offset + 1] != '-')
            || (input[offset + 2] != '>'))) {
        if (input[offset] == '\r') {
          lineNr[0]++;

          if ((offset != end) && (input[offset + 1] == '\n')) {
            offset++;
          }
        } else if (input[offset] == '\n') {
          lineNr[0]++;
        }

        offset++;
      }

      if (offset == end) {
        throw unexpectedEndOfData(lineNr[0]);
      } else {
        return offset + 3;
      }
    }

    int level = 1;

    while (offset < end) {
      char ch = input[offset++];

      switch (ch) {
        case '\r':

          if ((offset < end) && (input[offset] == '\n')) {
            offset++;
          }

          lineNr[0]++;

          break;

        case '\n':
          lineNr[0]++;

          break;

        case '<':
          level++;

          break;

        case '>':
          level--;

          if (level == 0) {
            return offset;
          }

          break;

        default:}
    }

    throw this.unexpectedEndOfData(lineNr[0]);
  }

  /**
   * Skips a tag that don't contain any useful data: &lt;?...?&gt;,
   * &lt;!...&gt; and comments.
   *
   * @param input
   * @param offset
   * @param end
   * @param lineNr
   *
   * @return the position after the tag
   *
   * @exception nanoxml.XMLParseException if an error occured while parsing
   *            the array
   */
  private int skipPreamble(char[] input, int offset, int end, int[] lineNr)
    throws XMLParseException {
    char ch;

    do {
      offset = this.skipWhitespace(input, offset, end, lineNr);

      if (input[offset] != '<') {
        this.expectedInput("'<'", lineNr[0]);
      }

      offset++;

      if (offset >= end) {
        throw this.unexpectedEndOfData(lineNr[0]);
      }

      ch = input[offset];

      if ((ch == '!') || (ch == '?')) {
        offset = this.skipBogusTag(input, offset, end, lineNr);
      }
    } while (!isIdentifierChar(ch));

    return offset;
  }

  /**
   * Skips whitespace characters.
   *
   * @param input
   * @param offset
   * @param end
   * @param lineNr
   *
   * @return the position after the whitespace
   *
   * @exception XMLParseException if an error occured while parsing the array
   */
  private int skipWhitespace(char[] input, int offset, int end, int[] lineNr) {
    while ((offset < end) && (input[offset] <= ' ')) {
      if (input[offset] == '\r') {
        lineNr[0]++;

        if ((offset != end) && (input[offset + 1] == '\n')) {
          offset++;
        }
      } else if (input[offset] == '\n') {
        lineNr[0]++;
      }

      offset++;
    }

    if (offset == end) {
      throw this.unexpectedEndOfData(lineNr[0]);
    }

    return offset;
  }

  /**
   * Converts &amp;...; sequences to "normal" chars.
   *
   * @param s a {@link java.lang.String} to decode
   * @param lineNr the current line number of the XML file.
   */
  protected String decodeString(String s, int lineNr) {
    StringBuffer result = new StringBuffer(s.length());
    int          index = 0;

    while (index < s.length()) {
      int index2 = (s + '&').indexOf('&', index);
      int index3 = (s + "<![CDATA[").indexOf("<![CDATA[", index);

      if (index2 <= index3) {
        result.append(s.substring(index, index2));

        if (index2 == s.length()) {
          break;
        }

        index = s.indexOf(';', index2);

        if (index < 0) {
          result.append(s.substring(index2));

          break;
        }

        String key = s.substring(index2 + 1, index);

        if (key.charAt(0) == '#') {
          if (key.charAt(1) == 'x') {
            result.append((char) (Integer.parseInt(key.substring(2), 16)));
          } else {
            result.append((char) (Integer.parseInt(key.substring(1), 10)));
          }
        } else {
          result.append(
            this.conversionTable.getProperty(key, "&" + key + ';'));
        }
      } else {
        int index4 = (s + "]]>").indexOf("]]>", index3 + 9);
        result.append(s.substring(index, index3));
        result.append(s.substring(index3 + 9, index4));
        index = index4 + 2;
      }

      index++;
    }

    return result.toString();
  }

  /**
   * Writes the XML element to a {@link java.lang.String}.
   *
   * <p>
   * Attention: this {@link java.lang.String} may be extremely long so this
   * method should not be used.
   * </p>
   *
   * <p>
   * Use {@link #output(OutputStream)} or {@link #output(PrintStream)}
   * instead.
   * </p>
   *
   * @return a rather long {@link java.lang.String} representation of this XML
   *         element.
   */
  public String toString() {
    ByteArrayBuffer buf = new ByteArrayBuffer();
    this.output(buf);

    return buf.toString();
  }

  /**
   * Writes the XML element to a {@link java.io.OutputStream}
   *
   * @param out
   */
  public void output(OutputStream out) {
    this.output(new PrintStream(out), 0);
  }

  /**
   * Writes the XML element to a {@link java.io.PrintStream}
   *
   * @param out
   */
  public void output(PrintStream out) {
    this.output(out, 0);
  }

  /**
   * Writes the XML element to a {@link java.io.PrintStream} with some
   * indentation.
   */
  void output(PrintStream out, int indent) {
    for (int i = 0; i < indent; i++) {
      out.write('\t');
    }

    if (this.tagName == null) {
      // this.writeEncoded(out, this.contents);
      return;
    }

    out.write('<');
    out.print(this.tagName);

    if (!this.attributes.isEmpty()) {
      Enumeration enumm = this.attributes.keys();

      while (enumm.hasMoreElements()) {
        out.write(' ');

        String key = (String) (enumm.nextElement());
        out.print(key);
        out.print("=\"");
        this.writeEncoded(out, (String) (this.attributes.get(key)));
        out.write('"');
      }
    }

    if (this.children.isEmpty()) {
      out.println("/>");
    } else {
      out.println('>');

      Enumeration enumm      = this.enumerateChildren();
      int         newIndent = 0;

      if (INDENT) {
        newIndent = indent + 1;
      }

      while (enumm.hasMoreElements()) {
        kXMLElement child = (kXMLElement) (enumm.nextElement());

        if (INDENT) {
          child.output(out, newIndent);
        } else {
          child.output(out, 0);
        }
      }

      if (INDENT) {
        for (int i = 0; i < indent; i++) {
          out.write('\t');
        }
      }

      out.print("</");
      out.print(this.tagName);
      out.println('>');
    }
  }

  /**
   * Writes a string encoded to a writer.
   */
  protected void writeEncoded(PrintStream out, String str) {
    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);

      switch (ch) {
        case '<':
          out.print("&lt;");

          break;

        case '>':
          out.print("&gt;");

          break;

        case '&':
          out.print("&amp;");

          break;

        case '"':
          out.print("&quot;");

          break;

        case '\'':
          out.print("&amp;");

          break;

        case '\r':
        case '\n':
          out.write(ch);

          break;

        default:

          if ((int) ch < 16) {
            out.print("&#x0");
            out.print(Integer.toString((int) ch, 16));
            out.write(';');
          } else if (((int) ch < 32) || (((int) ch > 126) && ((int) ch < 256))) {
            out.print("&#x");
            out.print(Integer.toString((int) ch, 16));
            out.write(';');
          } else {
            out.write(ch);
          }
      }
    }
  }

  /**
   * Creates a parse exception for when an invalid valueset is given to a
   * method.
   */
  private XMLParseException invalidValueSet(String key) {
    String msg = "Invalid value set (key = \"" + key + "\")";

    return new XMLParseException(this.getTagName(), msg);
  }

  /**
   * Creates a parse exception for when an invalid value is given to a method.
   */
  private XMLParseException invalidValue(String key, String value, int lineNr) {
    String msg =
      "Attribute \"" + key + "\" does not contain a valid " + "value (\""
      + value + "\")";

    return new XMLParseException(this.getTagName(), lineNr, msg);
  }

  /**
   * The end of the data input has been reached.
   */
  private XMLParseException unexpectedEndOfData(int lineNr) {
    String msg = "Unexpected end of data reached";

    return new XMLParseException(this.getTagName(), lineNr, msg);
  }

  /**
   * A syntax error occured.
   */
  private XMLParseException syntaxError(String context, int lineNr) {
    String msg = "Syntax error while parsing " + context;

    return new XMLParseException(this.getTagName(), lineNr, msg);
  }

  /**
   * A character has been expected.
   */
  private XMLParseException expectedInput(String charSet, int lineNr) {
    String msg = "Expected: " + charSet;

    return new XMLParseException(this.getTagName(), lineNr, msg);
  }

  /**
   * A value is missing for an attribute.
   */
  private XMLParseException valueMissingForAttribute(String key, int lineNr) {
    String msg = "Value missing for attribute with key \"" + key + "\"";

    return new XMLParseException(this.getTagName(), lineNr, msg);
  }

  //------------------------------------------------------------------
}
