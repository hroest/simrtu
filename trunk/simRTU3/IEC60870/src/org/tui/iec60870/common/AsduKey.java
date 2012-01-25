/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tui.iec60870.common;

/**
 * This class defines the way to code/decode asdu for an iec application.
 * <p>
 * General contract for this class is to provide all required informations
 * to to be able to code an asdu 'header' depending on iec implementation
 * in use.
 * <p>
 * So, finally, such an object use same fields than iec asdu:
 * <p>
 * - type identification
 * <p>
 * - variable structure qualifier
 * <p>
 * - cause of transmission
 * <p>
 * - common address.
 * <p>
 * See MapAsdu class for further informations about the use of such objects.
 *
 * @author lionnel cauvy
 * @version 1.0
 * @date 01.II.03
 *
 * @see MapAsdu
 */
public abstract class AsduKey {

    /**
     * Final static identifier to sort out not used fields
     * from an instance of such a key.
     * <p>
     * For example, when writing a received asdu according to its
     * mapping, a master application MUST NOT take into account
     * cause of transmission field because same information can be
     * raised with different causes of transmissions (General
     * interrogation, spontaneous event, ...). Whereas in same case,
     * a slave application MUST also validate cause of transmission
     * field.
     * <p>
     * When reading asdu to build from database, all fields are normally
     * needed but we can use default values for Common Address or Variable
     * Structure Qualifier fields....
     */
    public static final short NOTUSED = -1;
    /**
     * Asdu type identification.
     */
    public short id;
  
    /**
     * Asdu cause of transmission.
     */
    public short cot;
    /**
     * Asdu common address. Notice that field can be 1 or 2 octets
     * length according to standard implementation.
     */
    public int caa;

    /**
     * Abstract method to implement according to 10X standard
     * to check matching between real asdu on one hand and an
     * instance of key on the other hand.
     *
     * @param asdu	The real received asdu to check.
     *
     * @return true if matched, false else.
     */
    public abstract boolean check(Asdu asdu);

    /**
     * Hash code overriden method. To implement according
     * to iec implementation in use.
     *
     * @return an integer hashcode.
     */
    @Override
    public abstract int hashCode();

    /**
     * Equlity overriden method. To implement according
     * to iec implementation in use.
     *
     * @return an equlity indicator flag.
     */
    @Override
    public abstract boolean equals(Object object);

    /**
     * <p>
     * Allocates a key for asdus mapping.
     *
     * @param id		Asdu type identification.
     * @param vsq		Asdu variable structure qualifier.
     * @param cot		Asdu cause of transmission.
     * @param caa		Asdu common address.
     */

    protected AsduKey(short id, short cot, int caa) {
        this.id = id;
        this.caa = caa;
        this.cot = cot;
    }
}
