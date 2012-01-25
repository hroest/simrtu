/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.util;

import lib.interfaces.queue.QueueException;
import lib.interfaces.queue.QueueIface;

/**
 *
 * @author Micha
 */
public class FifoQueue
        implements QueueIface {

    private int maxElementCount;
    private int head = 0;
    private int tail = 0;
    private int elementCount = 0;
    private Object[] elementData;

    public FifoQueue(int paramInt) {
        this.maxElementCount = paramInt;
        this.elementData = new Object[paramInt];
    }

    @Override
    public synchronized int getElementCount() {
        return this.elementCount;
    }

    public synchronized void emptyQueue()
    {
       this.head=0;
       this.tail=0;
       this.elementCount=0;
       this.elementData = new Object[this.maxElementCount];
    }

    @Override
    public boolean isEmpty() {
        return (this.elementCount == 0);
    }

    @Override
    public boolean isFull() {
        return (this.elementCount >= this.maxElementCount - 1);
    }

    @Override
    public synchronized int enqueue(Object paramObject) {
        if (this.elementCount >= this.maxElementCount) {
            return -1;
        }
        this.elementData[this.tail] = paramObject;
        this.tail = ((this.tail + 1) % this.maxElementCount);
        this.elementCount += 1;
        super.notifyAll();
        return (this.maxElementCount - this.elementCount);
    }

    @Override
    public synchronized Object dequeue() throws QueueException {
        Object localObject = null;
        try {
            if (this.elementCount <= 0) {
                return null;
            }
            localObject = this.elementData[this.head];
            this.head = ((this.head + 1) % this.maxElementCount);
            this.elementCount -= 1;
            super.notifyAll();

        } catch (Exception e) {
            throw new QueueException(e);
        }
        return localObject;
    }

    @Override
    public synchronized Object blockingDequeue() {
        return blockingDequeue(0);
    }

    public synchronized Object blockingDequeue(int paramInt) {
        while (this.elementCount <= 0) {
            try {
                super.wait(paramInt);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((paramInt > 0) && (this.elementCount <= 0)) {
                return null;
            }
        }
        Object localObject = this.elementData[this.head];
        this.head = ((this.head + 1) % this.maxElementCount);
        this.elementCount -= 1;
        super.notify();
        return localObject;
    }
}
