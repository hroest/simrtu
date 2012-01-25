/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

/**
 * <p>
 * This class defines the way an iec application access the database to
 * effectively write or read a value.
 * <p>
 * Before going on, let's enumerate the different cases in which application
 * must be able to access the database:
 * <p>
 * - First case, application is decoding an asdu received from a remote station.
 * There's maybe some interesting data to record here whatever the master/slave
 * relationship occuring in such a transmission. We're in a WRITE ACCESS MODE.
 * <p>
 * - Second case, application is coding an asdu in order to send it to a remote
 * station. Application may need to drag data from db to correctly fill this asdu.
 * Same as before, it doesn't mind the master/slave mode of local application.
 * It's here a READ ACCESS MODE.
 * <p>
 * This 'Mapping Read Write Asdu' interests both master and slave applications.
 * Now the access modes are clearly identified, it's important to underline used
 * mechanisms to map a whole asdu or only part of it to database.
 * <p>
 * First, an asdu can effectively exchange several values with database. So the
 * need to define a set of 'mapping single values' in this class.
 * <p>
 * Second, application needs to sort out mapped asdus from non mapped. So
 * this class must use 'strategical' fields of an asdu in order to identify without
 * any doubts the nature of the information. To do this, we use mapping key.<br>
 * See Key for further information.
 * <p>
 * Last point, in order to build an asdu, we need to add an information elements
 * field.
 *
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 12.II.03
 */
public class MapAsdu
{
	/**
	 * A key to code/decode asdu.
	 */
	public AsduKey key;
	/**
	 * Set of single mapped values.
	 */
	public MapSingleValue[] value;
	/**
	 * Set of information elements.
	 */
	public InformationElement[] ie;

	/**
	 * <p>
	 * Allocates a basic mapping asdu between an iec application
	 * and luciol database. Such an object will be used to decode
	 * a received asdu from a remote, and to code an asdu to send.
	 *
	 * @param key		A key to map or build an asdu.
	 * @param value		An array of single mapped values.
	 * @param ie		An array of information elements.
	 */
	
    public MapAsdu(AsduKey key, MapSingleValue[] value, InformationElement[] ie)
	{
		this.key	= key;
		this.value	= value;
		this.ie		= ie;
	}

	public MapAsdu(AsduKey key, MapSingleValue[] value)
	{
		this.key	= key;
		this.value	= value;
	}

	public MapAsdu(AsduKey key)
	{
		this(key,null);
	}

        public MapAsdu(MapAsdu asdu)
        {
            this.key = asdu.key;
            this.value = asdu.value;
            this.ie = asdu.ie;
        }

};
