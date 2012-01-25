/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.io;

/**
 *
 * @author Micha
 */
/**
 * <p>
 * This class validates the format of net related strings : e-mail addresses,
 * domain names, IP addresses.
 * </p>
 *
 * <p>
 * The implementation of this class is derived from the
 * <tt>org.apache.mailet.MailAddress</tt> class, by:
 *
 * <ul>
 * <li>
 * Roberto Lo Giacco &lt;rlogiacco_at_mail.com&gt;,
 * </li>
 * <li>
 * Serge Knystautas &lt;sergek_at_lokitech.com&gt;,
 * </li>
 * <li>
 * Gabriel Bucher &lt;gabriel.bucher_at_razor.ch&gt;
 * </li>
 * </ul>
 * </p>
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com"> Alexis BIETTI </a>
 */
public class Validator {
  //---------------------------------------------------------------------------
  // Attributes and constants
  //---------------------------------------------------------------------------

  /** Some special characters. */
  private static final char[] SPECIAL =
  { '<', '>', '(', ')', '[', ']', '\\', '.', ',', ';', ':', '@', '\"' };

  /** Current position, used for parsing */
  private int pos = 0;

  /** The string we validate */
  private String string;

  //---------------------------------------------------------------------------
  // Constructors
  //---------------------------------------------------------------------------

  /**
   * Create a new Validator, with no initialization.
   */
  public Validator() {
    super();
  }

  /**
   * Initialize a validator with the String <tt>string</tt>
   *
   * @param address a String
   */
  public Validator(String string) {
    super();
    this.string = string;
  }

  //---------------------------------------------------------------------------
  // Testing
  //---------------------------------------------------------------------------

  /**
   * Test
   */
  public static void main(String[] args) {
    String[] d =
      new String[] {
        "127.0.0.1", "192.168.165.131", "....", "151, 48", "1531.15.26.2",
        "127.0.0.-0", "127.0000.13.0"
      };

    Validator v = new Validator(null);

    for (int i = 0; i < d.length; ++i) {
      v.setString(d[i]);

      try {
        String ip = Integer.toHexString(v.string2IP());
        System.out.println(d[i] + " -> " + Integer.toHexString(v.string2IP()));
      } catch (IllegalArgumentException iae) {
      }
    }
  }

  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------

  /**
   * Initialize this validator to validate a new address.
   *
   * @param address a String
   */
  public void setString(String string) {
    this.string = string;
  }

  /**
   * Convert an IP address into a String.
   *
   * @param ip an IP address
   *
   * @return a String representation of this IP address.
   */
  public static String ip2String(int ip) {
    StringBuffer buf   = new StringBuffer();
    int          octet;

    for (int i = 0; i < 32; i += 8) {
      octet = (ip >> i) & 0xff;
      buf.append(octet);
      buf.append('.');
    }

    buf.deleteCharAt(buf.length() - 1);

    return buf.toString();
  }

  /**
   * Convert the current string into an IP address, raw formatted.
   *
   * @return a raw formatted IP address.
   *
   * @throws IllegalArgumentException if the string is not a well formed IP
   *         address.
   */
  public int string2IP() throws IllegalArgumentException {
    pos = 0;

    int  ip     = 0; // IP address
    char c; // current char
    int  octets = 0; // number of bytes read
    int  len    = string.length(); // length we must not go over

    while (pos < len) // loop octets
     {
      StringBuffer buf = new StringBuffer();
      int          val;

      while (pos < len) // loop digits
       {
        c = string.charAt(pos);

        if (c == '.') {
          ++pos;

          break;
        }

        if ((c < '0') || (c > '9')) {
          // Not a digit
          throw new IllegalArgumentException();
        }

        buf.append(c);
        ++pos;
      }

      if ((buf.length() == 0) || (buf.length() > 3)) {
        // The current number is either null or > 300
        throw new IllegalArgumentException();
      }

      val = Integer.parseInt(buf.toString());

      if ((val < 0) || (val > 255)) {
        // The current number is not in the range 0-255
        throw new IllegalArgumentException();
      }

      ip |= ((val & 0xff) << (octets << 3));
      ++octets;

      if (octets > 4) {
        // Too many octets in this IP address
        throw new IllegalArgumentException();
      }
    }

    return ip;
  }

  /**
   * Test if the string used to initialize this validator is a well formed
   * domain name, including names, IP, and numbers
   *
   * @return <tt>true</tt> if it is a well formed domain name.
   */
  public boolean validateDomainName() {
    pos = 0;

    String d = string.trim();

    return parseDomainPart(d);
  }

  /**
   * Test if the string used to initialize this validator is a well formed
   * mail address.
   *
   * @return <tt>true</tt> if it is a well formed mail address.
   */
  public boolean validateMailAddress() {
    pos = 0;

    String       address = string.trim();
    StringBuffer userSB = new StringBuffer();

    //Begin parsing
    //<mailbox> ::= <local-part> "@" <domain>
    try {
      //parse local-part
      //<local-part> ::= <dot-string> | <quoted-string>
      // Quoted strings not supported !
      if (address.charAt(pos) == '\"') {
        return false;
      } else {
        userSB.append(parseUnquotedLocalPart(address));
      }

      if (userSB.toString().length() == 0) {
        return false;
      }

      //find @
      if (address.charAt(pos) != '@') {
        return false;
      }

      pos++;

      parseDomainPart(address);
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  //---------------------------------------------------------------------------
  // Utility methods
  //---------------------------------------------------------------------------

  /**
   * Test if the given string is a valid domain name.
   *
   * <p>
   * A valid domain name is compliant with the following definition:
   * </p>
   *
   * <p>
   * <code>  &lt;domain&gt; ::=  &lt;element&gt; | &lt;element&gt;"."
   * &lt;domain&gt; <br>
   * &lt;element&gt; ::= &lt;name&gt; | "#" &lt;number&gt; | "["
   * &lt;dotnum&gt; "]" </code>
   * </p>
   *
   * @param address a domain name
   *
   * @return <tt> true </tt> if the string <tt> address </tt> is a valid
   *         domain name.
   */
  private boolean parseDomainPart(String address) {
    StringBuffer hostSB = new StringBuffer();

    //parse domain
    //<domain> ::=  <element> | <element> "." <domain>
    //<element> ::= <name> | "#" <number> | "[" <dotnum> "]"
    while (pos < address.length()) {
      if (address.charAt(pos) == '#') {
        hostSB.append(parseNumber(address));
      } else if (address.charAt(pos) == '[') {
        hostSB.append(parseDotNum(address));
      } else {
        hostSB.append(parseDomainName(address));
      }

      if (pos >= address.length()) {
        break;
      }

      if (address.charAt(pos) == '.') {
        hostSB.append('.');
        pos++;

        continue;
      }

      break;
    }

    if (hostSB.toString().length() == 0) {
      return false;
    }

    return true;
  }

  /**
   * Test if the given string is a valid local part of an e-mail address.
   *
   * <p>
   * The local part is all that is before the 'at' character.
   * </p>
   *
   * @param address the local part of an e-mail address
   */
  private String parseUnquotedLocalPart(String address)
    throws IllegalArgumentException {
    StringBuffer resultSB = new StringBuffer();

    //<dot-string> ::= <string> | <string> "." <dot-string>
    boolean lastCharDot = false;

    while (true) {
      //<string> ::= <char> | <char> <string>
      //<char> ::= <c> | "\" <x>
      if (address.charAt(pos) == '\\') {
        resultSB.append('\\');
        pos++;

        //<x> ::= any one of the 128 ASCII characters (no exceptions)
        char x = address.charAt(pos);

        if ((x < 0) || (x > 128)) {
          throw new IllegalArgumentException();
        }

        resultSB.append(x);
        pos++;
        lastCharDot = false;
      } else if (address.charAt(pos) == '.') {
        resultSB.append('.');
        pos++;
        lastCharDot = true;
      } else if (address.charAt(pos) == '@') {
        //End of local-part
        break;
      } else {
        //<c> ::= any one of the 128 ASCII characters, but not any
        //    <special> or <SP>
        //<special> ::= "<" | ">" | "(" | ")" | "[" | "]" | "\" | "."
        //    | "," | ";" | ":" | "@"  """ | the control
        //    characters (ASCII codes 0 through 31 inclusive and
        //    127)
        //<SP> ::= the space character (ASCII code 32)
        char c = address.charAt(pos);

        if ((c <= 31) || (c == 127) || (c == ' ')) {
          throw new IllegalArgumentException();
        }

        for (int i = 0; i < SPECIAL.length; i++) {
          if (c == SPECIAL[i]) {
            throw new IllegalArgumentException();
          }
        }

        resultSB.append(c);
        pos++;
        lastCharDot = false;
      }
    }

    if (lastCharDot) {
      throw new IllegalArgumentException();
    }

    return resultSB.toString();
  }

  /**
   * DOCUMENT ME!
   *
   * @param address DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   *
   * @throws IllegalArgumentException DOCUMENT ME!
   */
  private String parseNumber(String address) throws IllegalArgumentException {
    //<number> ::= <d> | <d> <number>
    StringBuffer resultSB = new StringBuffer();

    //We keep the position from the class level pos field
    while (true) {
      if (pos >= address.length()) {
        break;
      }

      //<d> ::= any one of the ten digits 0 through 9
      char d = address.charAt(pos);

      if (d == '.') {
        break;
      }

      if ((d < '0') || (d > '9')) {
        throw new IllegalArgumentException();
      }

      resultSB.append(d);
      pos++;
    }

    return resultSB.toString();
  }

  /**
   * Parse a
   */
  private String parseDotNum(String address) throws IllegalArgumentException {
    //throw away all irrelevant '\' they're not necessary for escaping of '.'
    // or digits, and are illegal as part of the domain-literal
    while (address.indexOf("\\") > -1) {
      address =
        address.substring(0, address.indexOf("\\"))
        + address.substring(address.indexOf("\\") + 1);
    }

    StringBuffer resultSB = new StringBuffer();

    //we were passed the string with pos pointing the the [ char.
    // take the first char ([), put it in the result buffer and increment pos
    resultSB.append(address.charAt(pos));
    pos++;

    //<dotnum> ::= <snum> "." <snum> "." <snum> "." <snum>
    for (int octet = 0; octet < 4; octet++) {
      //<snum> ::= one, two, or three digits representing a decimal
      //                      integer value in the range 0 through 255
      //<d> ::= any one of the ten digits 0 through 9
      StringBuffer snumSB = new StringBuffer();

      for (int digits = 0; digits < 3; digits++) {
        char d = address.charAt(pos);

        if (d == '.') {
          break;
        }

        if (d == ']') {
          break;
        }

        if ((d < '0') || (d > '9')) {
          throw new IllegalArgumentException();
        }

        snumSB.append(d);
        ++pos;
      }

      if (snumSB.toString().length() == 0) {
        throw new IllegalArgumentException();
      }

      try {
        int snum = Integer.parseInt(snumSB.toString());

        if (snum > 255) {
          throw new IllegalArgumentException();
        }
      } catch (NumberFormatException nfe) {
        throw new IllegalArgumentException();
      }

      resultSB.append(snumSB.toString());

      if (address.charAt(pos) == ']') {
        if (octet < 3) {
          throw new IllegalArgumentException();
        } else {
          break;
        }
      }

      if (address.charAt(pos) == '.') {
        resultSB.append('.');
        ++pos;
      }
    }

    if (address.charAt(pos) != ']') {
      throw new IllegalArgumentException();
    }

    resultSB.append(']');
    ++pos;

    return resultSB.toString();
  }

  /**
   * Parse the given domain name.
   *
   * @param address a domain name.
   */
  private String parseDomainName(String address)
    throws IllegalArgumentException {
    StringBuffer resultSB = new StringBuffer();

    //<name> ::= <a> <ldh-str> <let-dig>
    //<ldh-str> ::= <let-dig-hyp> | <let-dig-hyp> <ldh-str>
    //<let-dig> ::= <a> | <d>
    //<let-dig-hyp> ::= <a> | <d> | "-"
    //<a> ::= any one of the 52 alphabetic characters A through Z
    //  in upper case and a through z in lower case
    //<d> ::= any one of the ten digits 0 through 9
    //basically, this is a series of letters, digits, and hyphens,
    // but it can't start with a digit or hypthen
    // and can't end with a hyphen
    //by practice though, we should relax this as domain names can start
    // with digits as well as letters.  So only check that doesn't start
    // or end with hyphen.
    while (true) {
      if (pos >= address.length()) {
        break;
      }

      char ch = address.charAt(pos);

      if (
        ((ch >= '0') && (ch <= '9')) || ((ch >= 'a') && (ch <= 'z'))
            || ((ch >= 'A') && (ch <= 'Z')) || (ch == '-')) {
        resultSB.append(ch + "");
        pos++;

        continue;
      }

      if (ch == '.') {
        break;
      }

      throw new IllegalArgumentException();
    }

    String result = resultSB.toString();

    if (result.startsWith("-") || result.endsWith("-")) {
      throw new IllegalArgumentException();
    }

    return result;
  }
}