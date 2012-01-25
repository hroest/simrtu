/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lib.interfaces.queue;

// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 23.03.2009 14:34:30
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)

public interface QueueIface
{
    public abstract boolean isEmpty();

    public abstract boolean isFull();

    public abstract int enqueue(Object obj);

    public abstract Object dequeue() throws QueueException;

    public abstract Object blockingDequeue();

    public abstract int getElementCount();
}