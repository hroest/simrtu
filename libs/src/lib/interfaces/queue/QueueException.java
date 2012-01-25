/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.interfaces.queue;

/**
 *
 * @author autologon
 */
public class QueueException extends Exception
{
	public QueueException()
	{
		super();
	}

	public QueueException(String msg)
	{
		super(msg);
	}

        public QueueException(Exception e)
        {
            super(e);
        }

        public QueueException(Exception e, String msg)
        {
            this(e.toString() + "Ursache:" + msg + " // ");
        }

}
