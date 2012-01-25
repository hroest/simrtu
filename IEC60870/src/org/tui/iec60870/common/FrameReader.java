///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package org.tui.iec60870.common;
//
//import lib.util.FifoQueue;
//
///**
// * This class defines the way used to build a frame with
// * bytes from physical layer. We can notice that all the
// * format checking procedures have been deported to physical
// * layer (see Receiver) for reasons of performance.
// *
// * @author lionnel cauvy
// * @version 1.0
// * @date 01.II.03
// */
//
//
//public class FrameReader
//{
//
//    protected static Frame frame;
//
//	/**
//	 * Sets frame address field. Address field is here one-octet sized.
//	 * According to the standard it can also be two octets sized.
//	 *
//	 * @param octet	value of one octet sized address field.
//	 */
//	private static void setAddressField(short octet)
//	{
//		frame.address = octet;
//	}
//	/**
//	 * Sets frame address field. Address field is here two-octets sized.
//	 * According to the standard, it can also be one octet sized.
//	 *
//	 * @param octet1	lsb value of two octets sized address field.
//	 * @param octet2	msb value of two octets sized address field.
//	 */
//	private static void setAddressField(short octet1, short octet2)
//	{
//		frame.address = (octet2 << 8) | octet1;
//	}
//
//	/**
//	 * Sets a byte into user data field.
//	 *
//	 * @param octet	user data byte.
//	 */
//	private static void setUserData(Short octet)
//	{
//		frame.user.data.addElement(octet);
//	}
//
//	/**
//	 * Build method inherited from Factory. First step of construction is to set
//	 * the control field, second address field and last user data.<br>
//	 * Notice that 'header bytes' (ie start,length,checksum & stop bytes) are checked
//	 * at physical layer level. So a frame read from physical layer WILL NOT contain
//	 * any information about what is defined as header bytes.
//	 *
//	 * @param object	the object to turn into a frame. (com.ajile.util.FifoQueue)
//	 *
//	 * @return the built frame.
//	 *
//	 * @throw exception if no object to return the built object
//	 */
//	public static Frame build(Object object, int szCaa) throws IEC608705Exception
//	{
//		byte[] data = (byte[])object;
//        FifoQueue octets = new FifoQueue(255);
//
//
//        for (int ii=0; ii< data.length; ii++)
//        {
//            octets.enqueue((short)data[ii]);
//        }
//
//		// factory reinitialization.
//		frame = new Frame();
//
//		try
//		{
//			// address field step.
//			switch (szCaa)
//			{
//			case 1:
//				Short octet = (Short)octets.dequeue();
//				setAddressField(octet.shortValue());
//				break;
//			case 2:
//				Short octet1 = (Short)octets.dequeue();
//				Short octet2 = (Short)octets.dequeue();
//				setAddressField(octet1.shortValue(),octet2.shortValue());
//				break;
//			}
//
//			frame.user.address = frame.address;
//
//			// user data step.
//			while(!octets.isEmpty())
//			{
//				setUserData((Short)octets.dequeue());
//			}
//		}
//		catch(NullPointerException e)
//		{
//			while (!octets.isEmpty())
//				octets.dequeue();
//			// restart phy layer.
//			throw new IEC608705Exception("Frame not well formed:"+e);
//		}
//		/*catch(IEC608705Exception e)
//		{
//			while (!octets.isEmpty())
//				octets.dequeue();
//			reset();
//			throw new IEC608705Exception("Frame not well formed:"+e);
//		}*/
//
//		return frame;
//	}
//
//};
