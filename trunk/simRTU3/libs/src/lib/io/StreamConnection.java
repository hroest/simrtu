/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.io;

/*
 * http://www.docjar.org/download.jsp?q=javax.microedition.io.Connection
 * @author mikra
 */
public interface StreamConnection extends InputConnection, OutputConnection
{
        public String get_foreign_uri();
        public String get_own_uri();
}