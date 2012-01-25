/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.common101;

/**
 * Interoperability101 defines interoperability for 101 protocol
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 18.II.03
 */
public interface Interoperability101 {

    /** periodic, cyclic */
    public static final byte COT_PERIODIC_CYCLIC = 0x01;
    /** background scan */
    public static final byte COT_BACKGROUND_SCAN = 0x02;
    /** spontaneous */
    public static final byte COT_SPONTANEOUS = 0x03;
    /** initialized */
    public static final byte COT_INITIALIZED = 0x04;
    /** request or requested */
    public static final byte COT_REQUEST = 0x05;
    /** activation */
    public static final byte COT_ACTIVATION = 0x06;
    /** activation confirmation */
    public static final byte COT_ACTIVATIONCONF = 0x07;
    /** deactivation */
    public static final byte COT_DEACTIVATION = 0x08;
    /** deactivation confirmation */
    public static final byte COT_DEACTIVATIONCONF = 0x09;
    /** activation termination */
    public static final byte COT_ACTIVATION_TERMINATION = 0x0A;
    /** return information caused by a remote command */
    public static final byte COT_RETURN_INFORMATION_REMOTE = 0x0B;
    /** return information caused by a local command */
    public static final byte COT_RETURN_INFORMATION_LOCAL = 0x0C;
    /** file transfer */
    public static final byte COT_FILE_TRANSFER = 0x0D;
    /** interrogated by general interrogation */
    public static final byte COT_INTERROGATION_GENERAL = 0x14;
    /** requested by general counter request */
    public static final byte COT_REQUEST_GENERAL_COUNTER = 0x25;
    /** addendum 2 */
    public static final byte COT_UNKNOWN_TYPE_IDENTIFICATION = 0x2C;
    public static final byte COT_UNKNOWN_CAUSE_TRANSMISSION = 0x2D;
    public static final byte COT_UNKNOWN_COMMON_ADDRESS_ASDU = 0x2E;
    public static final byte COT_UNKNOWN_INFO_OBJ_ADDRESS = 0x2F;
    /** process information in monitor direction */
    /** single point */
    public static final byte ID_M_SP_NA_1 = 0x01;
    public static final byte ID_M_SP_TA_1 = 0x02;
    public static final byte ID_M_SP_TB_1 = 0x1E;		/* amd 1 */

    /** double point */
    public static final byte ID_M_DP_NA_1 = 0x03;
    public static final byte ID_M_DP_TA_1 = 0x04;
    public static final byte ID_M_DP_TB_1 = 0x1F;		/* amd 1 */

    /** step position */
    public static final byte ID_M_ST_NA_1 = 0x05;
    public static final byte ID_M_ST_TA_1 = 0x06;
    public static final byte ID_M_ST_TB_1 = 0x20;		/* amd 1 */

    /** bitstring */
    public static final byte ID_M_BO_NA_1 = 0x07;
    public static final byte ID_M_BO_TA_1 = 0x08;
    public static final byte ID_M_BO_TB_1 = 0x21;		/* amd 1 */

    /** normalized measure */
    public static final byte ID_M_ME_NA_1 = 0x09;
    public static final byte ID_M_ME_TA_1 = 0x0A;
    public static final byte ID_M_ME_TD_1 = 0x22;		/* amd 1 */

    /** scaled measure */
    public static final byte ID_M_ME_NB_1 = 0x0B;
    public static final byte ID_M_ME_TB_1 = 0x0C;
    public static final byte ID_M_ME_TE_1 = 0x23;		/* amd 1 */

    /** floating point measure */
    public static final byte ID_M_ME_NC_1 = 0x0D;
    public static final byte ID_M_ME_TC_1 = 0x0E;
    public static final byte ID_M_ME_TF_1 = 0x24;		/* amd 1 */

    /** integrating totals */
    public static final byte ID_M_IT_NA_1 = 0x0F;
    public static final byte ID_M_IT_TA_1 = 0x10;
    public static final byte ID_M_IT_TB_1 = 0x25;		/* amd 1 */

    /** event protection equipment */
    public static final byte ID_M_EP_TA_1 = 0x11;
    public static final byte ID_M_EP_TD_1 = 0x26;		/* amd 1 */

    /** packed start events protection equipment */
    public static final byte ID_M_EP_TB_1 = 0x12;
    public static final byte ID_M_EP_TE_1 = 0x27;		/* amd 1 */

    /** packed output circuit protection equipment */
    public static final byte ID_M_EP_TC_1 = 0x13;
    public static final byte ID_M_EP_TF_1 = 0x28;		/* amd 1 */

    /** packed single point with status change detection */
    public static final byte ID_M_PS_NA_1 = 0x14;
    /** measured value, normalized, no quality descriptor */
    public static final byte ID_M_ME_ND_1 = 0x15;
    /** process information in control direction */
    /** single command */
    public static final byte ID_C_SC_NA_1 = 0x2D;		/* con */

    /**  double command */
    public static final byte ID_C_DC_NA_1 = 0x2E;		/* con */

    /** regulating step command */
    public static final byte ID_C_RC_NA_1 = 0x2F;		/* con */

    /** set point command, normalized value */
    public static final byte ID_C_SE_NA_1 = 0x30;		/* con */

    /** set point command, scaled value */
    public static final byte ID_C_SE_NB_1 = 0x31;		/* con */

    /** set point command, short floating point number */
    public static final byte ID_C_SE_NC_1 = 0x32;		/* con */

    /**  bitstring 32 bits */
    public static final byte ID_C_BO_NA_1 = 0x33;		/* con */

    /** system information in monitor direction */
    /** end of initialization */
    public static final byte ID_M_EI_NA_1 = 0x46;
    /** system information in control direction */
    /** interrogation command */
    public static final byte ID_C_IC_NA_1 = 0x64;	/* con */

    /** counter interrogation command */
    public static final byte ID_C_CI_NA_1 = 0x65;	/* con */

    /** read command */
    public static final byte ID_C_RD_NA_1 = 0x66;	/* con */

    /** clock synchronization command */
    public static final byte ID_C_CS_NA_1 = 0x67;	/* con */

    /** test command */
    public static final byte ID_C_TS_NA_1 = 0x68;	/* con */

    /** reset process command */
    public static final byte ID_C_RP_NA_1 = 0x69;	/* con */

    /** delay acquisition command */
    public static final byte ID_C_CD_NA_1 = 0x6A;	/* con */

    /** parameter in control direction */
    /** parameter of measured value, normalized value */
    public static final byte ID_P_ME_NA_1 = 0x6E;	/* con */

    /** parameter of measured value, scaled value */
    public static final byte ID_P_ME_NB_1 = 0x6F;	/* con */

    /** parameter of measured value, short floating point number */
    public static final byte ID_P_ME_NC_1 = 0x70;	/* con */

    /** parameter activation */
    public static final byte ID_P_AC_NA_1 = 0x71;	/* con */

    /** file transfer */
    /** file ready */
    public static final byte ID_F_FR_NA_1 = 0x78;
    /** section ready */
    public static final byte ID_F_SR_NA_1 = 0x79;
    /** call directory, select file, call file, call section */
    public static final byte ID_F_SC_NA_1 = 0x7A;
    /** last section, last segment */
    public static final byte ID_F_LS_NA_1 = 0x7B;
    /** ack file, ack section */
    public static final byte ID_F_AF_NA_1 = 0x7C;
    /** segment */
    public static final byte ID_F_SG_NA_1 = 0x7D;
    /** directory */
    public static final byte ID_F_DR_TA_1 = 0x7E;
    /**
     * QOC outstation
     */
    public int SHORT_PULSE = 0;
    public int LONG_PULSE = 0;
    // cause of transmission could be 1 or 2 sized in t101,104
    // cause of transmission is 1 sized in t103
    public byte COT_SIZE = 0;
    // common address could be 1 or 2 sized in t101,104 (if 2, second is originator address)
    // common address is 1 sized in t103
    public byte COMMON_ADDR_SIZE = 0;
    // info object address could be 1,2 or 3 sized in t101,t104
    // info object address is 1 sized in t103
    public byte INFO_OBJ_ADDR_SIZE = 0;
    // remote address could be 1 or 2 sized in t101,t104
    // remote address is 1 sized in t103
    public byte REMOTE_ADDR_SIZE = 0;
    public static final int HEADER_ASDU_SIZE = 6;
}
