/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Connector {

    public static final int READ = 1;
    public static final int WRITE = 2;
    public static final int READ_WRITE = 3;

    public static Connection open(String name)
            throws IOException {
        return open(name, READ_WRITE, false);
    }

    public static Connection open(String name, int mode)
            throws IOException {
        return open(name, mode, false);
    }

    public static Connection open(String name, int mode, boolean timeouts)
            throws IOException {
        ClosedConnection cn;
        try {
            Class cl = Class.forName("lib.io." + name.substring(0, name.indexOf(':')) + ".Connection");
            cn = (ClosedConnection) cl.newInstance();
        } catch (ClassNotFoundException ex) {
            System.err.println(ex);
            throw new ConnectionNotFoundException();
        } catch (InstantiationException ex) {
            System.err.println(ex);
            throw new ConnectionNotFoundException();
        } catch (IllegalAccessException ex) {
            System.err.println(ex);
            throw new ConnectionNotFoundException();
        }
        return cn.open(name);
    }

    public static DataInputStream openDataInputStream(String name)
            throws IOException {
        InputConnection cn = (InputConnection) open(name);

        return cn.openDataInputStream();
    }

    public static DataOutputStream openDataOutputStream(String name)
            throws IOException {
        OutputConnection cn = (OutputConnection) open(name);

        return cn.openDataOutputStream();
    }

    public static InputStream openInputStream(String name)
            throws IOException {
        InputConnection cn = (InputConnection) open(name);

        return cn.openInputStream();
    }

    public static OutputStream openOutputStream(String name)
            throws IOException {
        OutputConnection cn = (OutputConnection) open(name);

        return cn.openOutputStream();
    }
}
