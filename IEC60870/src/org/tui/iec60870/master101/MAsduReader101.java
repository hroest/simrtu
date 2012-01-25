/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.master101;

import org.tui.iec60870.common.AsduReader;
import org.tui.iec60870.common.Asdu;
import org.tui.iec60870.common.UserData;
import org.tui.iec60870.common.IEC608705Exception;
import org.tui.iec60870.common101.Interoperability101;
import org.tui.iec60870.common101.information.*;
import org.tui.iec60870.common.InformationObject;
import org.tui.iec60870.common101.Asdu101;
import java.util.Enumeration;

/**
 *
 * @author Micha
 */
public class MAsduReader101 implements AsduReader {

    private Asdu101 asdu;
    /**
     * Cause of transmission field length.
     */
    private byte szCot = 1;
    /**
     * Common Address of Asdus field length.
     */
    private byte szCaa = 1;
    /**
     * Information Object Address field length.
     */
    private byte szIoa = 1;

    @Override
    public Asdu build(Object object) throws IEC608705Exception {
        reset();
        if (object == null) {
            return null;
        }
        UserData user = (UserData) object;
        asdu = new Asdu101();
        asdu.address = user.address;
        if (user.data == null) {
            return asdu;
        }
        if (user.data.isEmpty()) {
            return asdu;
        }
        short num = 0;
        byte numelt = 0;
        Enumeration data = user.data.elements();
        // DATA UNIT IDENTIFIER
        // TYPE IDENTIFICATION
        if (!setTypeIdentification(((Short) data.nextElement()).shortValue())) {
            reset();
            return null;
        }
        // VARIABLE STRUCTURE QUALIFIER
        short octet = ((Short) data.nextElement()).shortValue();
        asdu.vsq = octet;
        asdu.nio = (byte) (octet & MASK_NB);
        asdu.sq = (((short) (octet & MASK_SQ) >> 7) == 1) ? true : false;
        // CAUSE OF TRANSMISSION
        // 		(T101 COT SIZE UP TO 2 OCTETS)
        switch (szCot) {
            case 1:
                if (!setCauseTransmission(((Short) data.nextElement()).shortValue())) {
                    reset();
                    return null;
                }
                break;
            case 2:
                if (!setCauseTransmission(((Short) data.nextElement()).shortValue())) {
                    reset();
                    return null;
                }
                asdu.oa = ((Short) data.nextElement()).shortValue();
                break;
        }
        // COMMON ADDRESS
        // 		(T101 CAA SIZE UP TO 2 OCTETS)
        switch (szCaa) {
            case 1:
                asdu.caa = ((Short) data.nextElement()).shortValue();
                break;
            case 2:
                asdu.caa = (((Short) data.nextElement()).shortValue() & 0xFF) | ((((Short) data.nextElement()).shortValue() & 0xFF) << 8);
                break;
        }
        // INFORMATION OBJECT
        // sq from variable structure qualifier defines structure of info object set
        if (!asdu.sq) {
            // 1 information object, n information elements
            // number from variable structure qualifier defines number n of information elements
            // setInformationObjectStructure((byte)1,number);
            num = 1;
            numelt = (byte) asdu.nio;
        } else {
            // n information objects, 1 information element per object
            // number from variable structure qualifier defines number n of information objects
            //setInformationObjectStructure(number,(byte)1);
            num = asdu.nio;
            numelt = 1;
        }
        if (!asdu.sq & asdu.nio == 0) {
            num = 0x00;
        }
        try {
            // id from type identification defines which elements to use
            for (short i = 0; i < num; i++) {
                InformationObject info = new InformationObject();
                switch (szIoa) {
                    case 1:
                        info.address = ((Short) data.nextElement()).shortValue();
                        break;
                    case 2:
                        info.address = (((Short) data.nextElement()).shortValue() | (((Short) data.nextElement()).shortValue() << 8));
                        break;
                    case 3:
                        info.address = (((Short) data.nextElement()).shortValue() | (((Short) data.nextElement()).shortValue() << 8) | (((Short) data.nextElement()).shortValue() << 16));
                        break;
                    default:

                        reset();
                        return null;
                }

                switch (asdu.id) {
                    case Interoperability101.ID_P_ME_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new NVA(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QPM(((Short) data.nextElement()).shortValue()));
                        }
                        break;
                    case Interoperability101.ID_C_CS_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new CP56Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;
                    case Interoperability101.ID_C_IC_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new QOI(((Short) data.nextElement()).shortValue()));
                        }
                        break;
                    case Interoperability101.ID_M_SP_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new SIQ(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_SP_TA_1:
                        // SQ must be 0
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new SIQ(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP24Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_SP_TB_1:
                        // SQ must be 0
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new SIQ(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP56Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_DP_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new DIQ(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_DP_TA_1:
                        // SQ must be 0
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new DIQ(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP24Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_DP_TB_1:
                        // SQ must be 0
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new DIQ(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP56Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_ST_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new VTI(((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_ST_TA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new VTI(((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP24Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_ST_TB_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new VTI(((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP56Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_BO_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new BSI(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_BO_TA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new BSI(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP24Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_BO_TB_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new BSI(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP56Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_ME_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new NVA(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_ME_TA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new NVA(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP24Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_ME_TD_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new NVA(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP56Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_ME_NB_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new SVA(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_ME_TB_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new SVA(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP24Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_ME_TE_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new SVA(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP56Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_ME_NC_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new R32_23(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_ME_TC_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new R32_23(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP24Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_ME_TF_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new R32_23(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP56Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_IT_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new BCR(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_IT_TA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new BCR(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP24Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_IT_TB_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new BCR(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP56Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_EP_TA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new SEP(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP16Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP24Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_EP_TD_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new SEP(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP16Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP56Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_EP_TB_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new SPE(((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDP(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP16Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP24Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_EP_TE_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new SPE(((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDP(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP16Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP56Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_EP_TC_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new OCI(((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDP(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP16Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP24Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_EP_TF_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new OCI(((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDP(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP16Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP56Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_PS_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new SCD(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new QDS(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_ME_ND_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new NVA(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_M_EI_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new COI(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_F_FR_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new NOF(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new LOF(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new FRQ(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_F_SR_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new NOF(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new NOS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new LOF(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new SRQ(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_F_SC_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new NOF(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new NOS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new SCQ(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_F_LS_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new NOF(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new NOS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new LSQ(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CHS(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_F_AF_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new NOF(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new NOS(((Short) data.nextElement()).shortValue()));
                            info.setElement(new AFQ(((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    case Interoperability101.ID_F_SG_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            LOS los;
                            info.setElement(new NOF(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new NOS(((Short) data.nextElement()).shortValue()));
                            info.setElement(los = new LOS(((Short) data.nextElement()).shortValue()));
                            for (short k = 0; k < los.octetset[0]; k++) {
                                info.setElement(new OCTET(((Short) data.nextElement()).shortValue()));
                            }
                        }
                        break;

                    case Interoperability101.ID_F_DR_TA_1:
                        for (byte j = 0; j < numelt; j++) {
                            info.setElement(new NOF(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new LOF(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                            info.setElement(new SOF(((Short) data.nextElement()).shortValue()));
                            info.setElement(new CP56Time2a(((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue(), ((Short) data.nextElement()).shortValue()));
                        }
                        break;

                    default:
                        break;
                }
                asdu.add(info);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IEC608705Exception("IEC Exception: bad asdu format");
        }
        return asdu;
    }

    protected boolean setCauseTransmission(short octet) {
        // there's a confirmation bit
        boolean valid = true;

        switch (octet) {
            case Interoperability101.COT_PERIODIC_CYCLIC:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ periodic, cyclic");
                break;
            case Interoperability101.COT_BACKGROUND_SCAN:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ background scan");
                break;
            case Interoperability101.COT_SPONTANEOUS:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ spontaneous");
                break;
            case Interoperability101.COT_INITIALIZED:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ initialized");
                break;
            case Interoperability101.COT_REQUEST:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ request or requested");
                break;
            case Interoperability101.COT_ACTIVATION:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ activation");
                break;
            case Interoperability101.COT_ACTIVATIONCONF:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ activation confirmation");
                break;
            case Interoperability101.COT_DEACTIVATION:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ deactivation");
                break;
            case Interoperability101.COT_DEACTIVATIONCONF:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ deactivation confirmation");
                break;
            case Interoperability101.COT_ACTIVATION_TERMINATION:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ activation termination");
                break;
            case Interoperability101.COT_RETURN_INFORMATION_REMOTE:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ return information caused by a remote command");
                break;
            case Interoperability101.COT_RETURN_INFORMATION_LOCAL:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ return information caused by a local command");
                break;
            case Interoperability101.COT_FILE_TRANSFER:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ file transfer");
                break;
            case Interoperability101.COT_INTERROGATION_GENERAL:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ interrogated by general interrogation");
                break;

            case Interoperability101.COT_REQUEST_GENERAL_COUNTER:
//			CodeProperties.applog("[MAsduReader101] cause of transmission _ requested by general counter request");
                break;

            default:

                valid = false;
                break;
        }

        if (valid) {
            asdu.cot = octet;
        } else {
            reset();
        }

        return valid;
    }

    protected boolean setTypeIdentification(short octet) {
        boolean valid = true;
        switch (octet) {
            /** process information monitor direction */
            case Interoperability101.ID_M_SP_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ single point");
                break;
            case Interoperability101.ID_M_SP_TA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ single point");
                break;
            case Interoperability101.ID_M_SP_TB_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ single point");
                break;
            case Interoperability101.ID_M_DP_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ double point");
                break;
            case Interoperability101.ID_M_DP_TA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ double point");
                break;
            case Interoperability101.ID_M_DP_TB_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ double point");
                break;
            case Interoperability101.ID_M_ST_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ step position");
                break;
            case Interoperability101.ID_M_ST_TA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ step position");
                break;
            case Interoperability101.ID_M_ST_TB_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ step position");
                break;
            case Interoperability101.ID_M_BO_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ bitstring");
                break;
            case Interoperability101.ID_M_BO_TA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ bitstring");
                break;
            case Interoperability101.ID_M_BO_TB_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ bitstring");
                break;
            case Interoperability101.ID_M_ME_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ normalized measure");
                break;
            case Interoperability101.ID_M_ME_TA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ normalized measure");
                break;
            case Interoperability101.ID_M_ME_TD_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ normalized measure");
                break;
            case Interoperability101.ID_M_ME_NB_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ scaled measure");
                break;
            case Interoperability101.ID_M_ME_TB_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ scaled measure");
                break;
            case Interoperability101.ID_M_ME_TE_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ scaled measure");
                break;
            case Interoperability101.ID_M_ME_NC_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ floating point measure");
                break;
            case Interoperability101.ID_M_ME_TC_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ floating point measure");
                break;
            case Interoperability101.ID_M_ME_TF_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ floating point measure");
                break;
            case Interoperability101.ID_M_IT_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ integrating totals");
                break;
            case Interoperability101.ID_M_IT_TA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ integrating totals");
                break;
            case Interoperability101.ID_M_IT_TB_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ integrating totals");
                break;
            case Interoperability101.ID_M_EP_TA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ event protection equipment");
                break;
            case Interoperability101.ID_M_EP_TD_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ event protection equipment");
                break;
            case Interoperability101.ID_M_EP_TB_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ packed start events protection equipment");
                break;
            case Interoperability101.ID_M_EP_TE_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ packed start events protection equipment");
                break;
            case Interoperability101.ID_M_EP_TC_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ packed output circuit protection equipmentt");
                break;
            case Interoperability101.ID_M_EP_TF_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ packed output circuit protection equipment");
                break;
            case Interoperability101.ID_M_PS_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ packed single point with status change detection");
                break;
            case Interoperability101.ID_M_ME_ND_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ measured value, normalized, no quality descriptor");
                break;
            case Interoperability101.ID_M_EI_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ end of initialization");
                break;
            case Interoperability101.ID_F_FR_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ file ready");
                break;
            case Interoperability101.ID_F_SR_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ section ready");
                break;
            case Interoperability101.ID_F_SC_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ call directory, select file, call file, call section");
                break;
            case Interoperability101.ID_F_LS_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ last section, last segment");
                break;
            case Interoperability101.ID_F_AF_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ last section, last segmentack file, ack section");
                break;
            case Interoperability101.ID_F_SG_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ segment");
                break;
            case Interoperability101.ID_F_DR_TA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ directory");
                break;
            case Interoperability101.ID_C_SC_NA_1:
                break;
            case Interoperability101.ID_C_DC_NA_1:
                break;
            case Interoperability101.ID_C_RC_NA_1:
            case Interoperability101.ID_C_SE_NA_1:
            case Interoperability101.ID_C_SE_NB_1:
            case Interoperability101.ID_C_SE_NC_1:
            case Interoperability101.ID_C_BO_NA_1:
//		case Interoperability101.ID_C_IC_NA_1:
            case Interoperability101.ID_C_CI_NA_1:
            case Interoperability101.ID_C_RD_NA_1:
//		case Interoperability101.ID_C_CS_NA_1:
            case Interoperability101.ID_C_TS_NA_1:
            case Interoperability101.ID_C_RP_NA_1:
            case Interoperability101.ID_C_CD_NA_1:
//		case Interoperability101.ID_P_ME_NA_1:
            case Interoperability101.ID_P_ME_NB_1:
            case Interoperability101.ID_P_ME_NC_1:
            case Interoperability101.ID_P_AC_NA_1:

                valid = false;
                break;
            case Interoperability101.ID_P_ME_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ parameter of measured value, normalized value");
                break;
            case Interoperability101.ID_C_CS_NA_1:
//			CodeProperties.applog("[MAsduReader101] type identification _ clock synchronization command");
                break;
            case Interoperability101.ID_C_IC_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ interrogation command");
                break;
            default:
                valid = false;
                break;
        }
        asdu.id = octet;
        return valid;
    }

    protected void reset() {
        asdu = null;
    }

    public MAsduReader101(byte szCot, byte szCaa, byte szIoa) {
        this.szCot = szCot;
        this.szCaa = szCaa;
        this.szIoa = szIoa;
    }
}
