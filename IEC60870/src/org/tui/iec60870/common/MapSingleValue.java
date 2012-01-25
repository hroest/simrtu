/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

/**
 * <p>
 * This class represents standard mapping for an iec information recorded
 * in luciol database.<br>
 * <p>
 * To map a single value to a database, the first need is to isolate the value
 * from the asdu body. For this, this implementation has defined a value locator
 * that allows to locate whatever value that could be of any object type (Boolean,
 * String,Math32FP,...) hold by a standard asdu. This is possible according to the
 * implemented mechanism of standard Information Elements that hold array of datas
 * already decoded as object with a type (can match an instanceof) and of course
 * a value.
 * <p>
 * Once value is well located, application needs also to know where in the database
 * access (read/write) can be made to the expected value. To perform this, such a
 * mapped single value object defines a database locator.
 * <p>
 * Last, it may be very useful to translate the value between standard protocol
 * used value and database wanted value. For example, 103 implementation use <1>
 * for <off> and <2> stands for <on>. Other applications using database are not
 * awaited to understand 103 value meaning. So single value object incorporates
 * a translator to make the bridge to database. In this way, <1> can become false,
 * and <2> will be true.
 * <p>
 * NOTICE THAT THERE'S NOT ASDU FIELDS INFORMATIONS IN THIS CLASS.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */
public class MapSingleValue
{
	/**
	 * Database locator reference.
	 */
	public DbLocator db;
	/**
	 * Asdu locator reference.
	 */
	public int ioa;
	/**
	 * Translator reference.
	 */
	public Translator translator;

        // for read procedures
        public Object mapped;

        /**
	 * <p>
	 * Allocates a basic mapping single value between an iec application and the luciol
	 * database.
	 *
	 * @param db			Database locator reference.
	 * @param locator		Asdu single value locator reference.
	 * @param build			Build helper reference.
	 * @param translator	Translator reference.
	 */
	public MapSingleValue(DbLocator db, int ioa, Translator translator)
	{
		this.db			= db;
		this.ioa                = ioa;
		this.translator         = translator;
	}

}
