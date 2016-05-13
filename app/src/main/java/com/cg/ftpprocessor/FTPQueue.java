package com.cg.ftpprocessor;

import java.util.LinkedList;

public class FTPQueue {
	public LinkedList queue;

	public FTPQueue() {
		queue = new LinkedList();
	}

	public synchronized void addMsg(Object obj) {
		queue.add(obj);
		notify();
	}

	@SuppressWarnings("unchecked")
	public synchronized void addMsgFirst(Object obj) {
		queue.addFirst(obj);
		notify();
	}

	public synchronized Object getMsg() throws InterruptedException {
		while (queue.isEmpty()) {
			wait();
		}
		return queue.removeFirst();
	}

	public synchronized int getSize() {
		return queue.size();
	}
    public void makeEmptyQueue()
    {
        queue.clear();
    }
}
