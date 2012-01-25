/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.io;

/**
 * http://www.docjar.org/download.jsp?q=javax.microedition.io.Connection
 * @author mikra
 */
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public interface OutputConnection extends Connection
{

	OutputStream openOutputStream()
      throws IOException;

  DataOutputStream openDataOutputStream()
      throws IOException;

}
