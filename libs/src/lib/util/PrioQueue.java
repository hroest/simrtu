/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.util;

import lib.interfaces.queue.QueueException;
import lib.interfaces.queue.QueueIface;

// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 23.03.2009 14:33:41
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 

// Referenced classes of package com.ajile.util:
//            QueueIface

public class PrioQueue
    implements QueueIface
{

    public PrioQueue(int i)
    {
        elementCount = 0;
        maxElementCount = i;
        elementData = new Object[i];
        keyData = new int[i];
    }

    @Override
    public int enqueue(Object obj)
    {
        return -1;
    }

    public synchronized int enqueue(Object obj, int i)
    {
        if(elementCount >= maxElementCount)
            return -1;
        if(elementCount < 0)
            elementCount = 0;
        int j;

        for(j = elementCount - 1; j >= 0 && i > keyData[j]; j--);

        for(int k = elementCount - 1; k > j; k--)
        {
            keyData[k + 1] = keyData[k];
            elementData[k + 1] = elementData[k];
        }

        keyData[j + 1] = i;
        elementData[j + 1] = obj;
        elementCount++;
        notify();
        return maxElementCount - elementCount;
    }

    @Override
    public synchronized Object dequeue() throws QueueException
    {
        if(elementCount <= 0)
            return null;
        Object obj = elementData[0];
        for(int i = 1; i < elementCount; i++)
        {
            elementData[i - 1] = elementData[i];
            keyData[i - 1] = keyData[i];
        }

        elementCount--;
        return obj;
    }

    public synchronized Object blockingDequeue()
    {
        while(elementCount <= 0)
            try
            {
                wait();
            }
            catch(InterruptedException interruptedexception) { }
        Object obj = elementData[0];
        for(int i = 1; i < elementCount; i++)
        {
            elementData[i - 1] = elementData[i];
            keyData[i - 1] = keyData[i];
        }

        elementCount--;
        return obj;
    }

    public boolean isEmpty()
    {
        return elementCount == 0;
    }

    public boolean isFull()
    {
        return elementCount >= maxElementCount;
    }

    public int getElementCount()
    {
        return elementCount;
    }

    private int maxElementCount;
    private int elementCount;
    private Object elementData[];
    private int keyData[];
}
