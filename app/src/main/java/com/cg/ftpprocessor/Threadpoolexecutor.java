package com.cg.ftpprocessor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Threadpoolexecutor extends ThreadPoolExecutor {

	public Threadpoolexecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {

		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);

		// TODO Auto-generated constructor stub
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		// TODO Auto-generated method stub
		super.beforeExecute(t, r);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		// TODO Auto-generated method stub
		super.afterExecute(r, t);
	}

	@Override
	public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {
		// TODO Auto-generated method stub
		super.setRejectedExecutionHandler(handler);
	};

}
