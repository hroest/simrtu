/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.io;

import java.io.IOException;


public class ConnectionNotFoundException extends IOException
{

	public ConnectionNotFoundException()
	{
		super();
	}


	public ConnectionNotFoundException(String s)
	{
		super(s);
	}

}