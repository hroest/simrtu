/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tui.iec60870.common;

import lib.math.Float;
import lib.math.Math32FP;

/**
 * Translation rule is linear equation 'ax+b'.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 */
public class LinearTranslator implements Translator
{
	/**
	 * Rate mutiplier a from linear equation 'ax+b'.
	 */
	public double rate;

	/**
	 * Offset from linear equation 'ax+b'.
	 */
	public double offset;

	/**
	 * Applies a linear equation 'ax+b' transformation
	 * on a data element from iec information element.
	 *
	 * @param obj	the object to transform
	 *
	 * @return a Boolean object
	 */
        @Override
	public Object apply(Object obj)
	{
		Object object = null;
		if (obj instanceof Integer)
		{
			int res = (int)((rate * ((Integer)obj).intValue()) + offset);
			object = new Integer(res);
		}
		else if (obj instanceof Byte)
		{
			int res = (int)((rate * ((Byte)obj).byteValue()) + offset);
			object = new Integer(res);
		}
		else if (obj instanceof Short)
		{
			int res = (int)((rate * ((Short)obj).shortValue()) + offset);
			object = new Integer(res);
		}
		else if (obj instanceof lib.math.Float)
		{
			float res = (float)((rate * ((Float)obj).floatValue()) + offset);
			object = new lib.math.Float(res);
		}
		else if (obj instanceof Math32FP)
		{
			float res = (float)((rate * ((Math32FP)obj).floatValue()) + offset);
			object = new Float(res);
		}
		else
			object = obj;

		return object;
	}

        @Override
	public Object inverseapply(Object obj)
	{
		Object object = null;
		if (obj instanceof Integer)
		{
			int res = (int)(((((Integer)obj).intValue()) - offset)/rate);
			object = new Integer(res);
		}
		else if (obj instanceof Byte)
		{
			int res = (int)(((((Byte)obj).byteValue()) - offset)/rate);
			object = new Integer(res);
		}
		else if (obj instanceof Short)
		{
			int res = (int)(((((Short)obj).shortValue()) - offset)/rate);
			object = new Integer(res);
		}
		else if (obj instanceof lib.math.Float)
		{
			float res = (float)(((((Float)obj).floatValue()) - offset)/rate);
			object = new lib.math.Float(res);
		}
		else if (obj instanceof Math32FP)
		{
			float res = (float)(((((Math32FP)obj).floatValue()) - offset)/rate);
			object = new Float(res);
		}
                else if (obj instanceof Float)
                {
                        float res = (float)(((((Float)obj).floatValue()) - offset)/rate);
			object = new lib.math.Float(res);
                }
		else
			object = obj;

		return object;
	}

	/**
	 * <p>
	 * Applies a linear translation to an Object value.
	 *
	 * @param rate		'a' from 'ax+b'
	 * @param offset	'b'
	 */
	public LinearTranslator(double rate, double offset)
	{
		this.rate = rate;
		this.offset = offset;
	}
}