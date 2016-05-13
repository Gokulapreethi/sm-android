
/*
 * Queue.java
 *
 * 
 *
 */
package org.core;

import java.util.LinkedList;


/**
 * Queue is used to insert and Retrieve  Object. It is used as FIFO(First-In-First-Out) logic.
 * java.util.LinkedList is used here for insert and Retrieve operations.
 * 
 *
 */
public class Queue {
    
    /** LinkedList object used for storing the elements. */
    public LinkedList queue;/**< LinkedList object used for storing the elements. */
    
    /** Creates a new instance of Queue */
    public Queue() {
        queue = new LinkedList();
    }
    
    /** Adds the object to the queue. This method is synchronized.
     *
     *@param obj instance of the element that has been put in the queue.
     */
    public synchronized void addMsg(Object obj) {
        queue.add(obj);
        notify();
    }
    
    /** Retrieves the first value from the queue; blocks if the queue is empty.
     * Method is synchronized.
     * @return The first object(element).
     */
    public synchronized Object getMsg() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        return queue.removeFirst();
    }
    /** Retrieves the size of the queue.
     * Method is synchronized.
     * @return queue size.
     */
    public synchronized int getSize() {
        
    	return queue.size();
    }
}

