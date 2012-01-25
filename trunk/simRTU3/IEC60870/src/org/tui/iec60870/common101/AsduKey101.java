/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common101;

import org.tui.iec60870.common.Asdu;
import org.tui.iec60870.common.AsduKey;


/**
 * <p>
 * This class defines specialized key asdu mapping for 101
 * both master & slave applications.
 * <p>
 * So, finally, such an object use same fields than iec asdu:
 * <p>
 * - type identification
 * <p>
 * - variable structure qualifier
 * <p>
 * - cause of transmission
 * <p>
 * - originator address if defined.
 * <p>
 * - common address.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 18.II.03
 */
public class AsduKey101 extends AsduKey
{
	/**
	 * Originator address
	 */
	public short oa;
	/**
	 * Object method equals redefinition
	 *
	 * @param obj	the object to compare
	 *
	 * @return equality indicator
	 */
    @Override
	public boolean equals(Object obj)
	{
	    if (obj instanceof AsduKey101)
		{
			AsduKey101 data = (AsduKey101)obj;
			if (id != data.id)
				return false;
			if(oa != data.oa)
				return false;
			if (caa != data.caa)
				return false;
			return true;
		}
		return false;
	}
	/**
	 * Hash code generator. MapKey is used in hashtable.
	 *
	 * @return the hashcode based on asdu type identification.
	 */
    @Override
	public int hashCode()
	{
		return (id);
	}
	/**
	 * Check asdu matching with this key.
	 *
	 * @param _asdu	The asdu to evaluate.
	 *
	 * @return true if matched, false else.
	 */
    @Override
	public boolean check(Asdu _asdu)
	{
		Asdu101 asdu = (Asdu101)_asdu;
		if (id!=-1 && id!=asdu.id)
			return false;
		if(cot!=-1 && cot!=asdu.cot)
			return false;
		if(oa!=-1 && oa!=asdu.oa)
			return false;
		if(caa!=-1 && caa!=asdu.caa)
			return false;
		return true;
	}
	/**
	 * <p>
	 * Allocates a key for handling asdus in 101 application.
	 * Specialization is based on field 'Originator Address', second octet
	 * of cause of transmission field.
	 *
	 * @param id		Asdu type identification.
	 * @param vsq		Asdu variable structure qualifier.
	 * @param cot		Asdu cause of transmission.
	 * @param oa		Asdu originator address.
	 * @param caa		Asdu common address.
	 */
	public AsduKey101(short id, short cot, int caa)
	{
		super(id, cot, caa);
	}
}
