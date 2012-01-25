/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.slave101;

import org.tui.iec60870.IEC60870App;
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
 * @author mikra
 */
public class SAsduReader101 implements AsduReader {

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
    /**
     * Asdu reference.
     */
    private Asdu101 asdu;

    /**
     * Fills cause of transmission field in 101 asdu.
     *
     * @param octet		cause of transmission value.
     *
     * @return true or false according to this information standard compliance.
     */
    protected boolean setCauseTransmission(short octet) {
        // there's a confirmation bit
        boolean valid = true;

        switch (octet) {
            case Interoperability101.COT_PERIODIC_CYCLIC:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ periodic, cyclic");
                break;
            case Interoperability101.COT_BACKGROUND_SCAN:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ background scan");
                break;
            case Interoperability101.COT_SPONTANEOUS:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ spontaneous");
                break;
            case Interoperability101.COT_INITIALIZED:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ initialized");
                break;
            case Interoperability101.COT_REQUEST:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ request or requested");
                break;
            case Interoperability101.COT_ACTIVATION:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ activation");
                break;
            case Interoperability101.COT_ACTIVATIONCONF:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ activation confirmation");
                break;
            case Interoperability101.COT_DEACTIVATION:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ deactivation");
                break;
            case Interoperability101.COT_DEACTIVATIONCONF:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ deactivation confirmation");
                break;
            case Interoperability101.COT_ACTIVATION_TERMINATION:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ activation termination");
                break;
            case Interoperability101.COT_RETURN_INFORMATION_REMOTE:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ return information caused by a remote command");
                break;
            case Interoperability101.COT_RETURN_INFORMATION_LOCAL:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ return information caused by a local command");
                break;
            case Interoperability101.COT_FILE_TRANSFER:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ file transfer");
                break;
            case Interoperability101.COT_INTERROGATION_GENERAL:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ interrogated by general interrogation");
                break;

            case Interoperability101.COT_REQUEST_GENERAL_COUNTER:
//			CodeProperties.applog("[SAsduReader101] cause of transmission _ requested by general counter request");
                break;
        }

        if (valid) {
            asdu.cot = octet;
        } else {
            reset();
        }

        return valid;
    }

    /**
     * Fills type identification field in 101 asdu. Private asdus
     * can be detected here.
     *
     * @param octet		type identification value.
     *
     * @return true or false according to this information standard compliance.
     */
    protected boolean setTypeIdentification(short octet) {
        boolean valid = true;
        switch (octet) {
            /** process information monitor direction */
            case Interoperability101.ID_C_DC_NA_1:
                break;
            case Interoperability101.ID_C_IC_NA_1:
                break;
            case Interoperability101.ID_C_CS_NA_1:
                break;
            case Interoperability101.ID_C_SC_NA_1:
                break;
            default:
                IEC60870App.err("Nicht implementierter Typ." + octet, "SAsduReader101", 1);
                valid=false;
            }
        asdu.id = octet;
        return valid;
    }

    /**
     * Build main method inherited from asdu factory. Here, this
     * completes a 101 asdu.
     *
     * @param object	short array to turn into an asdu.
     * @param prange	The private range of related remote that sent this asdu.
     *
     * @return the built asdu.
     */
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
            //setInformationObjectStructure((byte)1,number);
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
                    //100 interrogation
                    case Interoperability101.ID_C_IC_NA_1:
                        for (byte j = 0; j < numelt; j++) {
                            short qoi = ((Short) data.nextElement()).shortValue();
                            info.setElement(new QOI(qoi));
                        }
                        break;
                    case Interoperability101.ID_C_SC_NA_1:
                        for (byte j=0; j < numelt; j++){
                            short sco = ((Short) data.nextElement()).shortValue();
                            info.setElement(new SCO(sco));
                        }
                        break;
                    //46 double command
                    case Interoperability101.ID_C_DC_NA_1:
                        for (byte j=0; j < numelt; j++){
                            short dco = ((Short) data.nextElement()).shortValue();
                            info.setElement(new DCO(dco));
                        }	
                    break;
                }
                asdu.add(info);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IEC608705Exception("IEC Exception: bad asdu format");
        }

        return asdu;

    }

    /**
     * Reset factory
     */
    protected void reset() {
        asdu = null;

    }

    /**
     * Constructs a reader factory specialized for 101 master station.
     *
     * @param szCot	Cause of transmission length (1-2).
     * @param szCaa	Common address of asdus length (1-2).
     * @param szIoa	Information object address length (1-2-3).
     * @param szLnk	Link address size (1-2).
     */
    public SAsduReader101(byte szCot, byte szCaa, byte szIoa) {
        this.szCot = szCot;
        this.szCaa = szCaa;
        this.szIoa = szIoa;
    }
}