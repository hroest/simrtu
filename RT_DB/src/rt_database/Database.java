/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rt_database;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Enumeration;
import java.util.Hashtable;
import lib.interfaces.application.Configurable;
import lib.xml.kXMLElement;

/**
 * Database class. A database contains DataElements.
 * <p>
 * At any time, there are always two databases :
 *
 * <ul>
 * <li>The {@link #ACTIVE} database is to be used by apps.
 * <li>The {@link #PASSIVE} database if to be used by configurator.
 * </ul>
 * <p>
 * A <b>commit</b> copies the passive database into the active database,
 * except for data records, that are thus lost.
 * <p>
 * A <b>roll back</b> copies the active database into the passive database.
 *
 * @author <a href="mailto:alexis.bietti@sysaware.com">Alexis BIETTI</a>
 * @version 1.0
 *
 * @see com.itlity.luciol.database.DataElement
 */
public class Database implements DatabaseFeatures, Configurable {
  //---------------------------------------------------------------------------
  // Constants
  //---------------------------------------------------------------------------

  /* XML names used in configuration */
  private static final String XML_DATABASE   = "Database";
  private static final String XML_SHORT_TIME = "shortTime";
  private static final String XML_LONG_TIME  = "longTime";

  /** Print debug info on <tt>System.out</tt> if true. */
  public static boolean DATABASE_DEBUG = true;

  public boolean init_ready = false;

  /** Option names. */
  private static final String[] OPTION_NAMES =
    new String[] {
      "DATABASE_DO_LONG_MAINTAIN_TIME",
      "DATABASE_DO_SHORT_MAINTAIN_TIME"
    };

  //---------------------------------------------------------------------------
  // Attributes
  //---------------------------------------------------------------------------

  /** Contains all data elements of this database. */
  private Hashtable dataElements = new Hashtable();

  /** Time stamp : the date when this database was created or commited. */
  private long timeStamp = System.currentTimeMillis();

  /** Long maintain time of digital outputs (in seconds) */
  private int longTime = 1;

  /** Short maintain time of digital outputs (in milliseconds) */
  private int shortTime = 20;

  // Meta information about the configuration file
  private String author  = "UNKNOWN";
  private String date    = "UNKNOWN";
  private String version = "UNKNOWN";
  private String fileId  = "UNKNOWN";


  //---------------------------------------------------------------------------
  // Constructor
  //---------------------------------------------------------------------------

  /**
   * Constructor.
   */
  public Database() {
  }



  //---------------------------------------------------------------------------
  // Methods
  //---------------------------------------------------------------------------

  /**
   * Get a DataElement specified by the give database ID.
   *
   * @param databaseID a database ID.
   *
   * @return a DataElement, or <tt> null </tt> if not found.
   */
  public DataElement getDataElement(int databaseID) {
    
      Object element = dataElements.get(new Integer(databaseID));
      
      if (element==null)
      {
          DatabaseApp.err("Kein Zugriff auf Objekt ID " + databaseID + ". Bitte Konfiguration prüfen.", "Database", 2);
      }
      
      return (DataElement) element;
  }

  /**
   * Return all the {@link DataElement}s as an {@link java.util.Enumeration}.
   *
   * @return all the {@link DataElement}s as an {@link java.util.Enumeration}.
   */
  public Enumeration elements() {
    return dataElements.elements();
  }

  /**
   * Add an element in this database with the specified ID. If the specified
   * ID is already assigned, the old element is discarded.
   *
   * @param element a DataElement.
   * @param ID a unique database identifier.
   */
  public void addElement(DataElement element, int ID) {
    element.databaseID = ID;
    addElement(element);
  }

  /**
   * Add an element in this database. The element contains it database ID.
   *
   * @param element a DataElement.
   */
  public void addElement(DataElement element) {
    dataElements.put(new Integer(element.databaseID), element);
  }

  /**
   * Return the digital output short time in millis.
   *
   * @return the digital output short time in millis.
   */
  public long shortTime() {
    return shortTime;
  }

  /**
   * Return the digital output long time in millis.
   *
   * @return the digital output long time in millis.
   */
  public long longTime() {
    return longTime * 1000;
  }

  //---------------------------------------------------------------------------
  // Interface XMLInterface
  //---------------------------------------------------------------------------

  /**
   * See the general contract of this method in the interface {@link
   * com.itlity.io.XMLInterface}
   */
   @Override
  public void inputFromXML(kXMLElement xml) throws IllegalArgumentException {
    try {
      // Parse attributes
      shortTime   = xml.getProperty(XML_SHORT_TIME, 20);
      longTime    = xml.getProperty(XML_LONG_TIME, 1);
      author      = xml.getProperty("author", "UNKNOWN");
      date        = xml.getProperty("date", "UNKNOWN");
      version     = xml.getProperty("version", "UNKNOWN");
      fileId      = xml.getProperty("fileId", "UNKNOWN");

      // Clear the current configuration
      dataElements.clear();

      for (
        Enumeration children = xml.enumerateChildren();
            children.hasMoreElements();) {
        kXMLElement child = (kXMLElement) children.nextElement();
        Class       clazz =
          (Class) DataElement.CLASSES.get(child.getTagName());

        if (clazz == null) {
          if (DEBUG) {
            DatabaseApp.log("XML: unbekanntes Element:" + child.getTagName().toString(), "Database");
          }

          // Unknown element, discard silently and pass to the next...
        } else {
          // Instanciate a new DataElement
          DataElement elem = (DataElement) clazz.newInstance(); 
          elem.inputFromXML(child);
          addElement(elem);
          Record record = elem.instanciateRecord();
          record.quality=Record.Q_INITIAL;
          
          float elm = child.getFloatProperty("initial", (float)0.0);

          record.setValue(elm);
          elem.writeNewRecord(record);
        }
      }
    } catch (Exception e) {
      // If there is any problem, let's discard the whole thing and warn the
      // upper level with an exception
      e.printStackTrace();
      throw new IllegalArgumentException(e.toString());
    }
    this.init_ready = true;
  }

  /**
   * Interface Configurable & XML
   */
    @Override
  public kXMLElement outputToXML() {
    kXMLElement root = new kXMLElement();
    int         size = dataElements.size();

    root.setTagName(XML_DATABASE);
    root.addProperty(XML_SHORT_TIME, shortTime);
    root.addProperty(XML_LONG_TIME, longTime);
    root.addProperty("author", author);
    root.addProperty("date", date);
    root.addProperty("version", version);
    root.addProperty("fileId", fileId);

    for (Enumeration keys = dataElements.keys(); keys.hasMoreElements();) {
      DataElement el =
        getDataElement(((Integer) keys.nextElement()).intValue());

      if (el == null) {
        continue;
      }

      root.addChild(el.outputToXML());
    }

    return root;
  }

  //---------------------------------------------------------------------------
  // Static helpers
  //---------------------------------------------------------------------------

  /**
   * Copy all DataElements of <tt>src</tt> into <tt>dest</tt>.
   *
   * @param src the source Database
   * @param dest the destination Database
   */
  protected static void copyInto(Database src, Database dest)
    throws DatabaseException {
    // Empty the notification queue
        try
        {
            while (DatabaseApp.toNotify.dequeue() != null) {}
        }
        catch(Exception e)
        {
            //@todo: find error
        }
    // Don't worry, be happy :)
    dest.inputFromXML(src.outputToXML());
  }
}
