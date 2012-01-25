/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.slave101;

import org.tui.iec60870.IEC60870App;
import org.tui.iec60870.common.AsduWriter;
import org.tui.iec60870.common.IEC608705Exception;
import org.tui.iec60870.common101.Interoperability101;
import org.tui.iec60870.common101.information.*;
import org.tui.iec60870.common.InformationObject;
import org.tui.iec60870.common.InformationElement;
import org.tui.iec60870.common101.AsduKey101;
import org.tui.iec60870.common.AppService;
import org.tui.iec60870.common.MapAsdu;
import org.tui.iec60870.common.MapSingleValue;
import org.tui.iec60870.common.Translator;

import org.tui.iec60870.slave.SDataClass1AppSvc;
import org.tui.iec60870.slave101.services.SEIAppSvc101;
import org.tui.iec60870.slave101.services.SEvtAppSvc101;
import org.tui.iec60870.slave101.services.SGIAppSvc101;
import org.tui.iec60870.slave101.services.SDCAppSvc101;
import org.tui.iec60870.slave101.services.SSCAppSvc101;

import rt_database.Record;

import java.util.Vector;

/**
 *
 * @author mikra
 */
public class SAsduWriter101 implements AsduWriter {

    /**
     * Cause of transmission field length.
     */
    private byte szCot = 2;
    /**
     * Common Address of Asdus field length.
     */
    private byte szCaa = 2;
    /**
     * Information Object Address field length.
     */
    private byte szIoa = 3;
    private int max_asdu_length = 240;

    @Override
    public Object build(AppService svc) throws IEC608705Exception {

        if (svc == null) {
            return null;
        }
        try {
            if (svc instanceof SDataClass1AppSvc) {
                return SDataClass1AppSvcAsdu((SDataClass1AppSvc) svc);
            } else if (svc instanceof SDataClass1AppSvc) {
                return SDataClass2AppSvcAsdu((SDataClass1AppSvc) svc);
            } else if ((svc instanceof AppService) || (svc instanceof SEIAppSvc101) || (svc instanceof SEvtAppSvc101) || (svc instanceof SGIAppSvc101)) {
                return AppServicesAsdu(svc);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new IEC608705Exception(e, "Proc. build/SAsduWriter101");
        }

    }

    private Object SDataClass1AppSvcAsdu(SDataClass1AppSvc proc) throws Exception {
        //returns Vector of Objects if not skalar or Object if skalar
        try
        {
        InformationObject[] io = getIO_multi(proc);

        if (io.length == 0) {
            return null;
        } else if (io.length == 1) {
            return scalar_ElementSet(io[0], proc);
        } else {
            Vector ret = vector_ElementSet(io, proc);
            return ret;
        }
        }
        catch(Exception e)
        {
            throw new IEC608705Exception(e, "Proc. SDataClass1AppSvcAsdu/SAsduWriter101");
        }
    }

    private Vector SDataClass2AppSvcAsdu(SDataClass1AppSvc proc) {
        return null;
    }

    private Object AppServicesAsdu(AppService proc) throws Exception {
        InformationObject io = null;
        io = getIO_single(proc);
        return scalar_ElementSet(io, proc);
    }

    private InformationObject getIO_single(AppService proc) throws Exception {
        return getIO_single(proc, 0);
    }

    private InformationObject getIO_single(AppService proc, int index) throws Exception {

        try
        {
        InformationObject io = null;
        MapAsdu asdu = proc.asdu;
        MapSingleValue[] value = asdu.value;
        InformationElement element = null;
        Record rec = null;

        switch (asdu.key.id) {

            /*
             * Supported Types of Slave Outputs
             */

            // Typ1 Einzelmeldung ohne Zeitmarke
            case Interoperability101.ID_M_SP_NA_1:
                try {
                    io = new InformationObject();
                    // we must check mapping compatibility...
                    if (value.length < index + 1) {
                        return null;
                    }
                    // mapping is ok, just go on...
                    element = new SIQ();


                    rec = (Record) value[index].mapped;

                    if (rec.booleanValue()) {
                        element.setData((short) 0, (byte) 1);
                    } else {
                        element.setData((short) 0, (byte) 0);
                    }
                    //non blocked
                    element.setData((short) 1, (byte) 0);
                    //reserved
                    element.setData((short) 2, false);
                    //not substituted
                    element.setData((short) 3, false);
                    //is actual
                    element.setData((short) 4, false);
                    //valid
                    element.setData((short) 5, false);

                    io.setElement(element);
                    io.setAddress(value[index].ioa);
                    
                    this.set_cot(rec, proc);

                } catch (Exception e) {
                    IEC60870App.err("Slave101/SAsduWriter101: Fehler beim Lesen/Mapping aus der Datenbank. Kann Asdu Element nicht bilden Index: " + index + " Typ: " + Interoperability101.ID_M_SP_TB_1 + " : " + e, "-",1);
                    return null;
                }
                break;


            // Typ3 Doppelmeldung ohne Zeitmarke
            case Interoperability101.ID_M_DP_NA_1:
                try {
                    io = new InformationObject();
                    // we must check mapping compatibility...
                    if (value.length < index + 1) {
                        return null;
                    }
                    // mapping is ok, just go on...
                    element = new DIQ();

                    //special handling diq
                    rec = (Record) value[index].mapped;

                    element.setData((short) 0, (byte) rec.intValue());
                    //non blocked
                    element.setData((short) 1, (byte) 0);
                    //reserved
                    element.setData((short) 2, false);
                    //not substituted
                    element.setData((short) 3, false);
                    //is actual
                    element.setData((short) 4, false);
                    //valid
                    element.setData((short) 5, false);

                    io.setElement(element);

                    io.setAddress(value[index].ioa);

                    this.set_cot(rec, proc);


                } catch (Exception e) {
                    IEC60870App.err("Slave101/SAsduWriter101: Fehler beim Lesen/Mapping aus der Datenbank. Kann Asdu Element nicht bilden Index: " + index + " Typ: " + Interoperability101.ID_M_DP_NA_1 + " : " + e, "-", 1);
                    return null;
                }
                break;
            
                
            

            // Typ30 Einzelmeldung mit Zeitmarke CP56Time2a
            case Interoperability101.ID_M_SP_TB_1:
                try {
                    io = new InformationObject();
                    // we must check mapping compatibility...
                    if (value.length < index + 1) {
                        return null;
                    }
                    // mapping is ok, just go on...
                    element = new SIQ();

                    rec = (Record) value[index].mapped;

                    if (rec.booleanValue()) {
                        element.setData((short) 0, (byte) 1);
                    } else {
                        element.setData((short) 0, (byte) 0);
                    }
                    //non blocked
                    element.setData((short) 1, (byte) 0);
                    //reserved
                    element.setData((short) 2, false);
                    //not substituted
                    element.setData((short) 3, false);
                    //is actual
                    element.setData((short) 4, false);
                    //valid
                    element.setData((short) 5, false);

                    io.setElement(element);
                    io.setElement(new CP56Time2a());
                    io.setAddress(value[index].ioa);

                    this.set_cot(rec, proc);

                } catch (Exception e) {
                    IEC60870App.err("Slave101/SAsduWriter101: Fehler beim Lesen/Mapping aus der Datenbank. Kann Asdu Element nicht bilden Index: " + index + " Typ: " + Interoperability101.ID_M_SP_TB_1 + " : " + e, "-",1);
                    return null;
                }
                break;

            // Typ31 Doppelmeldung mit Zeitmarke CP56Time2a
            case Interoperability101.ID_M_DP_TB_1:
                try {
                    //special handling diq
                    rec = (Record) value[index].mapped;

                    io = new InformationObject();
                    // we must check mapping compatibility...
                    if (value.length < index + 1) {
                        return null;
                    }
                    // mapping is ok, just go on...
                    element = new DIQ();

                    element.setData((short) 0, (byte) rec.intValue());
                    //non blocked
                    element.setData((short) 1, (byte) 0);
                    //reserved
                    element.setData((short) 2, false);
                    //not substituted
                    element.setData((short) 3, false);
                    //is actual
                    element.setData((short) 4, false);
                    //valid
                    element.setData((short) 5, false);


                    io.setElement(element);

                    io.setElement(new CP56Time2a());
                    io.setAddress(value[index].ioa);

                    this.set_cot(rec, proc);

                } catch (Exception e) {
                    IEC60870App.err("Slave101/SAsduWriter101: Fehler beim Lesen/Mapping aus der Datenbank. Kann Asdu Element nicht bilden Index: " + index + " Typ: " + Interoperability101.ID_M_SP_TB_1 + " : " + e, "-", 1);
                    return null;
                }
                break;
                // Typ35 Messwert skaliert mit CP56Time2a
                case Interoperability101.ID_M_ME_TE_1:
                try {
                    rec = ((Record) value[index].mapped);
                    Translator trs = (Translator) value[index].translator;
                    io = new InformationObject();
                    // we must check mapping compatibility...
                    if (value.length < index + 1) {
                        return null;
                    }
                    // mapping is ok, just go on...
                    element = new SVA();

                    float fl_val = rec.floatValue();
                    if (trs !=null)
                    {
                      fl_val = (Float)trs.inverseapply(fl_val);
                    }
                    short sh_val = (short)((Math.round(fl_val)) & 0xFFFF);

                    element.setData((short) 0, sh_val);
                    io.setElement(element);
                    io.setElement(new QDS((short) 0));
                    io.setElement(new CP56Time2a());
                    io.setAddress(value[index].ioa);

                    this.set_cot(rec, proc);

                } catch (Exception e) {
                    IEC60870App.err("Slave101/SAsduWriter101: Fehler beim Lesen/Mapping aus der Datenbank. Kann Asdu Element nicht bilden Index: " + index + " Typ: " + Interoperability101.ID_M_ME_TF_1 + " : " + e,"-",1);
                    return null;
                }
                break;
                


                // Typ36 Messwert verkürzt Gleitkommazahl mit CP56Time2a
            case Interoperability101.ID_M_ME_TF_1:
                try {
                    rec = ((Record) value[index].mapped);
                    io = new InformationObject();
                    // we must check mapping compatibility...
                    if (value.length < index + 1) {
                        return null;
                    }
                    // mapping is ok, just go on...
                    element = new R32_23();
                    element.setData((short) 0, rec.objectValue());
                    io.setElement(element);
                    io.setElement(new QDS((short) 0));
                    io.setElement(new CP56Time2a());
                    io.setAddress(value[index].ioa);

                    this.set_cot(rec, proc);
                   
                } catch (Exception e) {
                    IEC60870App.err("Slave101/SAsduWriter101: Fehler beim Lesen/Mapping aus der Datenbank. Kann Asdu Element nicht bilden Index: " + index + " Typ: " + Interoperability101.ID_M_ME_TF_1 + " : " + e,"-",1);
                    return null;
                }
                break;

            case Interoperability101.ID_C_SC_NA_1:
                SSCAppSvc101 svc_sc = (SSCAppSvc101) proc;
                io = (InformationObject) svc_sc.raw_asdu.io.get(0);
                break;
            // Typ46 Double Command
            case Interoperability101.ID_C_DC_NA_1:
                SDCAppSvc101 svc_dc = (SDCAppSvc101) proc;
                io = (InformationObject) svc_dc.raw_asdu.io.get(0);
                break;
            // Typ70 Ende der Initialisierung
            case Interoperability101.ID_M_EI_NA_1:
                SEIAppSvc101 svc_ei = (SEIAppSvc101) proc;
                io = new InformationObject();
                io.setElement(new COI(svc_ei.coi));
                break;

            // Typ100 Generalabfragebefehl
            case Interoperability101.ID_C_IC_NA_1:
                io = new InformationObject();
                io.setElement(new QOI(proc.cot));
                break;

            // Typ103 Uhrzeitsynchronisation
            case Interoperability101.ID_C_CS_NA_1:
                io = new InformationObject();
                io.setElement(new CP56Time2a());
                break;

            default:
                IEC60870App.log("Slave101/SAsduWriter101 nicht implementierter Typ: " + asdu.key.id,"-");
                IEC60870App.err("Slave101/SAsduWriter101 nicht implementierter Typ: " + asdu.key.id,"-",2);
                return null;
        }

        return io;
        }
        catch(Exception e)
        {
            throw new IEC608705Exception(e, "Proc. getIO_single/SAsduWriter101");
        }

        
    }

    private InformationObject[] getIO_multi(AppService proc) throws Exception {
        try
        {
        MapAsdu asdu = proc.asdu;
        MapSingleValue[] value = asdu.value;
        InformationObject[] io = new InformationObject[value.length];

        for (int ii = 0; ii < value.length; ii++) {
            io[ii] = getIO_single(proc, ii);
            if (io[ii]==null)
            {
                return null;
            }
        }
            return io;
        }
        catch (Exception e)
        {
            throw new IEC608705Exception(e, "Proc. getIO_multi/SAsduWriter101");
        }
    }

    private short[] scalar_ElementSet(InformationObject io, AppService proc) {
        short[] data = null;
        int size = 0;
        AsduKey101 key = (AsduKey101) proc.asdu.key;

        for (int i = 0; i < io.ie.size(); i++) {
            InformationElement ie = (InformationElement) io.ie.elementAt(i);
            size += ie.octetset.length;
        }
        data = new short[2 + szCot + szCaa + szIoa + size];
        int index = 0;
        data[index++] = key.id;

        //@todo vsq berechnen muss ermittelt werden aus der Anzahl der Objekt, Standard ist 1

        data[index++] = 1;

        short cot = proc.cot;

        switch (szCot) {
            

            case 1:
                data[index++] = cot;
                break;
            case 2:
                data[index++] = (short) ((cot & 0x00FF));
                data[index++] = (short) ((cot & 0xFF00) >> 8);
                break;
        }

        switch (szCaa) {
            case 1:
                data[index++] = (short) proc.casdu;
                break;
            case 2:
                data[index++] = (short) ((proc.casdu & 0x00FF));
                data[index++] = (short) ((proc.casdu & 0xFF00) >> 8);
                break;
        }

        int ioa = io.address;

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
        return data;
    }

    private Vector vector_ElementSet(InformationObject[] io, AppService proc) {
        //output Vector
        Vector output = new Vector();

        //temporary
        short[] tmp1 = null;
        Vector tmp2 = new Vector();

        //controls
        boolean newlist = true;

        //calculation of overhead
        int overhead = this.szCaa + this.szCot + 2;

        //reduced dataset for packaging
        int max_data_length = this.max_asdu_length - overhead;

        //modification and packaging
        for (int ii = 0; ii < io.length; ii++) {
            if (newlist) {
                newlist = false;
                //neue liste aufbauen -> erhalten der asduinfos

                if (io[ii] != null) {
                    tmp1 = scalar_ElementSet(io[ii], proc);
                    for (int jj = 0; jj < tmp1.length; jj++) {
                        tmp2.add(tmp1[jj]);
                    }
                }
            } else {

                //prüfung, ob das neue element die asdulänge überschreiten würde
                if (io[ii] != null) {
                    tmp1 = scalar_ElementSet(io[ii], proc);
                    if ((tmp2.size() + tmp1.length - overhead) > max_data_length) {
                        output.add(this.Obj2shortArray(tmp2.toArray()));
                        tmp2.clear();
                        ii--;
                        newlist = true;
                    } else {
                        //deleting overhead (zero index based means after overhead)
                        for (int jj = overhead; jj < tmp1.length; jj++) {
                            tmp2.add(tmp1[jj]);
                        }
                        //define object length
                        short tmp3 = (Short) tmp2.get(1);
                        tmp2.set(1, (short) (tmp3 + 1));
                    }
                }
            }
        }
        //fertig mit bearbeitung und add in den ausgabevektor
        output.add(this.Obj2shortArray(tmp2.toArray()));
        return output;
    }

    public SAsduWriter101(byte szCot, byte szCaa, byte szIoa) {
        this.szCot = szCot;
        this.szCaa = szCaa;
        this.szIoa = szIoa;
    }

    private short[] Obj2shortArray(Object[] objectlist) {
        try {
            short[] shortlist = new short[objectlist.length];
            for (int ii = 0; ii < objectlist.length; ii++) {
                shortlist[ii] = (Short) objectlist[ii];
            }
            return shortlist;
        } catch (Exception e) {
            IEC60870App.err("Error in procedure SAsduWriter101/Obj2shortArray" + e, "-",1);
            return null;
        }
    }

    private void set_cot(Record rec, AppService proc) {

        if ((proc.cot != (short)20) && (proc.cot != (short)5)) {
            if (rec.remote_event) {
                //remote caused event
                if (proc.to_central)
                {
                    proc.cot = 11;
                }

            } else if (rec.spontaneous) {
                //spontaneous event
                if (proc.to_central)
                {
                    proc.cot = 3;
                }
            } else if (rec.termination)
            {
                //termination
                if (!proc.to_central)
                {
                    proc.cot = 10;
                }
            } else if (rec.localevent) {
                //spontaneous event
                if (proc.to_central)
                {
                    proc.cot = 12;
                }
            }
        }
    }
}
