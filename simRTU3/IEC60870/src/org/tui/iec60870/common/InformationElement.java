/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

/**
 * Element is super class for all iec608705 information elements
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */

public abstract class InformationElement
{
	/**
	 * 'physical' data octet per octet array
	 */
	public short octetset[];

	/**
	 * 'sub information' data array
	 */
	public Object dataset[];

	/**
	 * information element invalidity
	 */
	public short invalidity;

	/**
	 * abstract method for decoding sub information data from physical data
	 */
	protected abstract void decode();

	/**
	 * abstract method for coding physical data from sub information data
	 */
	protected abstract void code();

        /**
         * abstract method for decoding information elements
         */

        //@todo generic values
        //protected abstract Object get_value();

        public int getElementSize()
        {
            return octetset.length;
        }

	/**
	 * set value in octet array at desired index
	 *
	 * @param i		index to set
	 * @param val	value to set
	 */
	public void setOctet(short i, short val)
	{
		octetset[i] = val;
		if(i == octetset.length-1)
			decode();
	}

	/**
	 * set sub information in data array at desired index
	 *
	 * @param i	index to set
	 * @param val	data to set
	 */
	public void setData(short i, Object val)
	{
		dataset[i] = val;
		// refresh if all data known
		if(refresh())
			code();
	}
	public void setData(Object val)
	{
		setData((short)0,val);
	}

	/**
	 * refresh indicator
	 *
	 * @return flag indicating dataset is complete
	 */
	private boolean refresh()
	{
		int k;
		boolean valid = true;
		for(k =0; k<dataset.length;k++)
		{
			if(dataset[k]==null)
				valid = false;
		}
		return valid;
	}

	/**
	 * instanciates an Element object
	 *
	 * @param i number of 'physical' octets
	 * @param j number of different informations in this information element
	 */
	public InformationElement(int i, int j)
	{
		int k;
		invalidity = 0;
		octetset = new short[i];
		dataset = new Object[j];
		// init
		for(k = 0; k<i; k++)
		{
			octetset[k] = (short)0x00;
		}
		for(k = 0; k<j; k++)
		{
			dataset[k] = null;
		}
	}

}
