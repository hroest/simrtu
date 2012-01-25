/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.master101;

import org.tui.iec60870.master101.services.MCmdAppSvc101;
import org.tui.iec60870.master101.services.MGIAppSvc101;
import org.tui.iec60870.common.AsduWriter;
import org.tui.iec60870.common.AppService;
import org.tui.iec60870.common.IEC608705Exception;
import org.tui.iec60870.common101.Interoperability101;
import org.tui.iec60870.common101.information.*;
import org.tui.iec60870.common.InformationObject;
import org.tui.iec60870.common.InformationElement;
import org.tui.iec60870.common101.AsduKey101;
import org.tui.iec60870.master.MAppService;

/**
 *
 * @author Micha
 */
public class MAsduWriter101 implements AsduWriter {

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
    public Object build(AppService svc) throws IEC608705Exception {
        short[] data = null;
        int size = 0;
        MAppService app = (MAppService) svc;
        if (app.asdu != null) {
            AsduKey101 key = (AsduKey101) app.asdu.key;
            InformationObject io = getIo(app);
            for (int i = 0; i < io.ie.size(); i++) {
                InformationElement ie = (InformationElement) io.ie.elementAt(i);
                size += ie.octetset.length;
            }
            data = new short[2 + szCot + szCaa + szIoa + size];
            int index = 0;
            data[index++] = key.id;

            //@todo vsq berechnen muss ermittelt werden aus der Anzahl der Objekt, Standard ist 1

            data[index++] = 1;

            switch (szCot) {
                case 1:
                    data[index++] = key.cot;
                    break;
                case 2:
                    data[index++] = (short) ((key.cot & 0x00FF));
                    data[index++] = (short) ((key.cot & 0xFF00) >> 8);
                    break;
            }



            switch (szCaa) {
                case 1:
                    data[index++] = (short) key.caa;
                    break;
                case 2:
                    data[index++] = (short) ((key.caa & 0x00FF));
                    data[index++] = (short) ((key.caa & 0xFF00) >> 8);
                    break;
            }

            int ioa = 0;
            if (svc instanceof MCmdAppSvc101) {
                ioa = ((MCmdAppSvc101) svc).ioa;
            }

            switch (szIoa) {
                case 1:
                    data[index++] = (short) ioa;
                    break;
                case 2:
                    data[index++] = (short) ((ioa & 0xFF));
                    data[index++] = (short) ((ioa & 0xFF00) >> 8);
                    break;
                case 3:
                    data[index++] = (short) ((ioa & 0x0000FF));
                    data[index++] = (short) ((ioa & 0x00FF00) >> 8);
                    data[index++] = (short) ((ioa & 0xFF0000) >> 16);
                    break;
            }


            for (int j = 0; j < io.ie.size(); j++) {
                InformationElement ie = (InformationElement) io.ie.elementAt(j);
                for (int k = 0; k < ie.octetset.length; k++) {
                    data[index++] = ie.octetset[k];
                }
            }
        }
        return data;
    }

    private InformationObject getIo(AppService proc) {
        InformationObject io = new InformationObject();

        // information object address & numelt????

        //MContext101 context = (MContext101) proc.context;

        switch (proc.asdu.key.id) {
            case Interoperability101.ID_C_SC_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ single command");
                io.setElement(new SCO(proc));
                break;
            case Interoperability101.ID_C_DC_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ double command");
                io.setElement(new DCO(proc));
                break;
            case Interoperability101.ID_C_RC_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ regulating step command");
                io.setElement(new RCO());
                break;
            case Interoperability101.ID_C_SE_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ set point command, normalized value");
                io.setElement(new NVA());
                io.setElement(new QOS());
                break;
            case Interoperability101.ID_C_SE_NB_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ set point command, scaled value");
                io.setElement(new SVA());
                io.setElement(new QOS());
                break;
            case Interoperability101.ID_C_SE_NC_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ set point command, short floating point number");
                io.setElement(new R32_23());
                io.setElement(new QOS());
                break;
            case Interoperability101.ID_C_BO_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ bitsrting 32 bits");
                io.setElement(new BSI());//map
                break;
            case Interoperability101.ID_C_IC_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ interrogation command");
                MGIAppSvc101 svc = (MGIAppSvc101) proc;
                io = new InformationObject();
                io.setElement(new QOI(svc.qoi));
                break;
            case Interoperability101.ID_C_CI_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ counter interrogation command");
                io.setElement(new QCC());
                break;
            case Interoperability101.ID_C_RD_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ read command");
                // nada
                break;
            case Interoperability101.ID_C_CS_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ clock synchronization command");
                io = new InformationObject();
                io.setElement(new CP56Time2a());
                break;
            case Interoperability101.ID_C_TS_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ test command");
                io = new InformationObject();
                io.setElement(new FBP());
                break;
            case Interoperability101.ID_C_RP_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ reset process command");
                io.setElement(new QRP());
                break;
            case Interoperability101.ID_C_CD_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ delay acquisition command");
                io = new InformationObject();
                io.setElement(new CP16Time2a());
                break;
            case Interoperability101.ID_P_ME_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ parameter of measured value, normalized value");
                io.setElement(new NVA());
                io.setElement(new QPM());
                break;
            case Interoperability101.ID_P_ME_NB_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ parameter of measured value, scaled value");
                io.setElement(new SVA());
                io.setElement(new QPM());
                break;
            case Interoperability101.ID_P_ME_NC_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ parameter of measured value, short floating point number");
                io.setElement(new R32_23()); //map
                io.setElement(new QPM()); //proc
                break;
            case Interoperability101.ID_P_AC_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ parameter activation");
                io.setElement(new QPA());
                break;
            case Interoperability101.ID_F_FR_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ file ready");
                io.setElement(new NOF());
                io.setElement(new LOF());
                io.setElement(new FRQ());
                break;
            case Interoperability101.ID_F_SR_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ section ready");
                io.setElement(new NOF());
                io.setElement(new NOS());
                io.setElement(new LOF());
                io.setElement(new SRQ());
                break;
            case Interoperability101.ID_F_SC_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ call directory, select file, call file, call section");
                io.setElement(new NOF());
                io.setElement(new NOS());
                io.setElement(new SCQ());
                break;
            case Interoperability101.ID_F_LS_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ last section, last segment");
                io.setElement(new NOF());
                io.setElement(new NOS());
                io.setElement(new LSQ());
                io.setElement(new CHS());
                break;
            case Interoperability101.ID_F_AF_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ last section, last segmentack file, ack section");
                io.setElement(new NOF());
                io.setElement(new NOS());
                io.setElement(new AFQ());
                break;
            case Interoperability101.ID_F_SG_NA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ segment");
                LOS los;
                io.setElement(new NOF());
                io.setElement(new NOS());
                io.setElement(los = new LOS());
                for (short j = 0; j < los.octetset[0]; j++) {
                    io.setElement(new OCTET());
                }
                break;
            case Interoperability101.ID_F_DR_TA_1:
//			CodeProperties.applog("[MAsduWriter101] type identification _ directory");
                // VSQ gives number of elements !!!!
                io.setElement(new NOF());
                io.setElement(new LOF());
                io.setElement(new SOF());
                io.setElement(new CP56Time2a());
                break;
            default:

                break;
        }

        return io;
    }

    public MAsduWriter101(byte szCot, byte szCaa, byte szIoa) {
        this.szCot = szCot;
        this.szCaa = szCaa;
        this.szIoa = szIoa;
    }
}
