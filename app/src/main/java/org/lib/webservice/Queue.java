
/*
 * Queue.java
 *
 * 
 *
 */
package org.lib.webservice;

import java.util.LinkedList;

/**
 * java.util.LinkedList is used here for insert and Retrieve operations. 
 * LinkedList is Used to store and Retrieve the elements.
 *
 */
public class Queue {
    
    /** LinkedList object used for storing the elements. */
    public LinkedList queue;
    
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
    /**
     * Adds the object to the queue in First Place. This method is synchronized.
     * @param obj instance of the element that has been put in the queue.
     */
    public synchronized void addMsgFirst(Object obj) {
        queue.addFirst(obj);
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

