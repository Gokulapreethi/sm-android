/**
 * 
 */
package com.util;

import java.util.LinkedList;


@SuppressWarnings("rawtypes")
public class Queue {
	  /**
     * LinkedList object used for storing the elements.
     */

	public LinkedList queue;

    /**
     * Creates a new instance of Queue.
     */
    
	public Queue() {
        queue = new LinkedList();
    }

    /**
     * Adds the object to the queue. Method is synchronized.
     *
     * @param obj instance of the element that has been put in the queue.
     */
    @SuppressWarnings("unchecked")
	public synchronized void addObject(Object obj) {
        queue.add(obj);
        notify();
    }

    /**
     * Retrieves the first value from the queue; blocks if the queue is empty.
     * Method is synchronized.
     *
     * @return The first object(element).
     * @throws java.lang.InterruptedException
     */
    public synchronized Object getObject() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        return queue.removeFirst();
    }

    /**
     * Retrieves the size of the queue. Method is synchronized.
     *
     * @return queue size.
     */
    public synchronized int getSize() {
        return queue.size();
    }
}
