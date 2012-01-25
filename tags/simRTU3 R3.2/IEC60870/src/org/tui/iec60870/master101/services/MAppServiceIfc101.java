/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.master101.services;

/**
 * <p>
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 19.II.03
 */
public interface MAppServiceIfc101 {

    /**
     * initial procedure: ResetCU + Synchro + General interrogation.
     */
    public static final byte S0_INITIAL = 0x00;
    /**
     * application service for station initialization by reseting communication unit.
     */
    public static final byte S1_RESET_CU = 0x01;
    public static final byte S13_REQ_STATUS = 0x0D;
    /**
     * application service for delay acquisition.
     */
    public static final byte S2_DELAY_ACQ = 0x02;
    /**
     * application service for station time synchronization
     */
    public static final byte S3_TIME_SYNCHRONIZATION = 0x03;
    /**
     * application service for station general interrogation
     */
    public static final byte S4_GENERAL_INTERROGATION = 0x04;
    /**
     * application service for counter interrogation
     */
    public static final byte S5_GENERAL_COUNTER_INTERROGATION = 0x05;
    /**
     * application service for read command
     */
    public static final byte S6_READ_COMMAND = 0x06;
    /**
     * application service for test command
     */
    public static final byte S7_TEST_COMMAND = 0x07;
    /**
     * application service for reset process command
     */
    public static final byte S8_RESET_PROCESS_COMMAND = 0x08;
    /**
     * application service for single, double, regulating step, set point
     * and bitstring commands.
     */
    public static final byte S9_COMMAND = 0x09;
    /**
     * application service for parameter.
     */
    public static final byte S10_PARAMETER = 0x0A;
    /**
     * application service for parameter activation.
     */
    public static final byte S11_PARAM_ACT = 0x0B;
    /**
     * user data class 2 polling
     */
    public static final byte S12_POLLING = 0x0C;
    public static final int S0_PRIORITY = 0;
    public static final int S1_PRIORITY = 13;
    public static final int S2_PRIORITY = 10;
    public static final int S3_PRIORITY = 9;
    public static final int S4_PRIORITY = 8;
    public static final int S5_PRIORITY = 7;
    public static final int S6_PRIORITY = 6;
    public static final int S7_PRIORITY = 11;
    public static final int S8_PRIORITY = 5;
    public static final int S9_PRIORITY = 4;
    public static final int S10_PRIORITY = 3;
    public static final int S11_PRIORITY = 2;
    public static final int S12_PRIORITY = 1;
    public static final int S13_PRIORITY = 12;
}
