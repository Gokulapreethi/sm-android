package com.thread;

import java.util.LinkedList;

import android.util.Log;

public class SipQueue {

    /**
     * LinkedList object used for storing the elements.
     */
    public LinkedList<Object> sip_queue;

    /**
     * Creates a new instance of Queue
     */
    public SipQueue() {
        sip_queue = new LinkedList<Object>();
    }

    /**
     * Adds the object to the queue. This method is synchronized.
     *
     * @param obj instance of the element that has been put in the queue.
     */
    public synchronized void addMsg(Object obj) {
        Log.i("droid123", "message giong to be added");
        sip_queue.add(obj);
        Log.i("droid123", "before notify queue size" + sip_queue.size());
        notify();
        Log.i("droid123", "after notify queue size" + sip_queue.size());
    }

    /**
     * Retrieves the first value from the queue; blocks if the queue is empty.
     * Method is synchronized.
     *
     * @return The first object(element).
     */
    public synchronized Object getMsg() throws InterruptedException {
        while (sip_queue.isEmpty()) {
            wait();
        }
        return sip_queue.removeFirst();
    }

    /**
     * Retrieves the size of the queue.
     * Method is synchronized.
     *
     * @return queue size.
     */
    public synchronized int getSize() {

        return sip_queue.size();
    }

    public void clearQueue() {
        sip_queue.clear();

    }

    public void insertFirst(Object obj) {
        sip_queue.addFirst(obj);
    }
}
