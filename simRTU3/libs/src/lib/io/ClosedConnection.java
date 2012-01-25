/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.io;

import java.io.IOException;

public interface ClosedConnection
{

  Connection open(String name) throws IOException;

}

