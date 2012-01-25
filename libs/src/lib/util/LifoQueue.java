/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.util;

import lib.interfaces.queue.QueueIface;
import lib.interfaces.queue.QueueException;

public class LifoQueue
    implements QueueIface
{

    public LifoQueue(int i)
    {
        elementCount = 0;
        maxElementCount = i;
        elementData = new Object[i];
    }

    public boolean isEmpty()
    {
        return elementCount == 0;
    }

    public boolean isFull()
    {
        return elementCount >= maxElementCount - 1;
    }

    public synchronized int enqueue(Object obj)
    {
        if(elementCount >= maxElementCount)
        {
            return -1;
        } else
        {
            elementData[elementCount] = obj;
            elementCount++;
            notify();
            return maxElementCount - elementCount;
        }
    }

    public synchronized Object dequeue() throws QueueException
    {
        if(elementCount <= 0)
        {
            return null;
        } else
        {
            elementCount--;
            return elementData[elementCount];
        }
    }

    public synchronized Object blockingDequeue()
    {
        return blockingDequeue(0);
    }

    public synchronized Object blockingDequeue(int i)
    {
        while(elementCount <= 0)
        {
            try
            {
                wait(i);
            }
            catch(InterruptedException interruptedexception) { }
            if(i > 0 && elementCount <= 0)
                return null;
        }
        elementCount--;
        return elementData[elementCount];
    }

    public int getElementCount()
    {
        return elementCount;
    }

    private int maxElementCount;
    private int elementCount;
    private Object elementData[];
}
