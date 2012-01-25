/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.io;

/**
 *
 * @author Micha
 */
import java.io.IOException;


public interface Connection
{

	void close()
			throws IOException;

    boolean isOpen();

}