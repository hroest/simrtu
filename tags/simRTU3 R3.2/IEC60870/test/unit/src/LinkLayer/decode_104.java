/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LinkLayer;

/**
 *
 * @author Michael
 */
public class decode_104 {

    public static void main(String args[])
    {

        byte[] data = {(byte)104, 4, (byte)131, 0, 0, 0};

        int length = data.length;

        String msg = "Data length: " + Integer.toString(length) + " Package: ";

        try {

            //for debugging purposes write all package date to file

            for (int ii = 0; ii < length; ii++) {
                msg = msg + "[" + Integer.toString(data[ii] & 0xFF) + "] ";
            }

            msg = msg + "!";

            System.out.println("Empfangenes 104er Paket: " + msg);

            //divide packages into atomar ones

            for (int ii = 0; ii < length; ii++) {
                //starting point equals 104
                if (ii >= length) {
                    return;
                }

                if ((data[ii] & 0xFF) == 104) {
                    //length of one package is coded
                    byte[] rxd = new byte[(data[ii + 1] & 0xFF) + 2];
                    for (int j = 0; j < rxd.length; j++) {
                        if (ii >= length) {
                            System.out.println("Fehlerhafte Paketlänge, verwerfe Paket der Länge :" + j + ":" + msg);
                            return;
                        }
                        rxd[j] = data[ii];
                        if (j < rxd.length) {
                            ii++;
                        }
                    }
                    //send(rxd);
                    //dec ii, because for loop increases...
                    //ii--;
                    //next package
                }
            }
        } catch (Exception e) {
            //if an error occurs
            System.out.println("Fehler im Linklayer (Paket decodieren): " + e + " " + msg);
        }
    }
}
