/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.io;

/**
 * http://www.docjar.org/download.jsp?q=javax.microedition.io.Connection
 * @author mikra
 */
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;


public interface InputConnection extends Connection
{

	InputStream openInputStream()
      throws IOException;

    DataInputStream openDataInputStream()
      throws IOException;

}

