///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package org.tui.iec60870.common;
//
///**
// * Frame is an object representation for data that is transmitted by protocol<br>
// * Frame is handled by link layer and is defined by format FT1.2 from iec 60870-5-1<br>
// * Frame can be:<br>
// * 	- single command (cf Interoperability.LinkLayer.singleByte)<br>
// * 	- fixed length (start byte _ control field _ address field _ user data _ checksum _ stop byte)<br>
// * 	- variable length (start byte _ length _ length _ start byte _ control field _ address field _ user data _ checksum _ stop byte)
// * Frame is always built by a frame factory object: frame reader for received data from physical layer,
// * and frame writer for data received from application layer.
// *
// * @author lionnel cauvy
// * @version 1.0
// * @date 01.II.03
// */
//
//public class Frame
//{
//	/* frame type */
//	public static final byte SINGLE				= 0x01;
//	public static final byte VARIABLE			= 0x02;
//	public static final byte FIXED				= 0x03;
//
//	/**
//	 * Frame type.<br>
//	 * To differenciate fixed length frame from variable length or also single byte commands.
//	 */
//	public byte type;
//
//	/**
//	 * Frame length.br>
//	 * Header bytes are not reported in the count.
//	 */
//	public short length;
//
//	/**
//	 * Start byte.<br>
//	 * Defined in Interoperability as 0x10 for fixed length frame, 0x68 for variable length frame
//	 * and 0x5A for single bytes command.<br>
//	 */
//	public short start;
//
//	/**
//	 * Checksum.<br>
//	 * Simple addition on control field, address field and user data field bytes.
//	 */
//	public short checksum;
//
//	/**
//	 * Stop byte.<br>
//	 * Defined in Interoperability as 0x16 value.
//	 */
//	public short stop;
//
//	/**
//	 * Address field.<br>
//	 * Address that indicates remote station address over the iec network
//	 * can be one or two octets sized.
//	 */
//	public int address;
//
//	/**
//	 * User data field.
//	 */
//	public UserData user;
//
//	/**
//	 * Instanciates a new frame.
//	 */
//	public Frame()
//	{
//		user = new UserData();
//		checksum = length = start = stop = type = 0;
//	}
//};
