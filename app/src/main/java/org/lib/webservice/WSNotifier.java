package org.lib.webservice;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.cg.hostedconf.AppReference;

public class WSNotifier {

	private WSExecutor taskmanager;

	public static Queue wsQueue = new Queue();

	private String server_ip;

	private int connect_ort;

	private String namespace;

	private String wsdl_link;

	private WebServiceCallback service_callback;

	public WSNotifier(String ip, int port, String name_space, String url,
			WebServiceCallback callback) {

		this.server_ip = ip;
		this.connect_ort = port;
		this.namespace = name_space;
		this.wsdl_link = url;
		this.service_callback = callback;

		taskmanager = new WSExecutor(2, 2, 5000, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(100));
		taskmanager.prestartCoreThread();

		if (taskmanager != null) {
			taskmanager
					.setRejectedExecutionHandler(new RejectedExecutionHandler() {

						@Override
						public void rejectedExecution(Runnable task,
								ThreadPoolExecutor executor) {
							// TODO Auto-generated method stub
							WSRunner taskRunner = (WSRunner) task;
							if (taskRunner.getBean() != null)
								wsQueue.addMsg(taskRunner.getBean());
						}
					});
		}

	}

	public void shutDowntaskmanager() {
		if (taskmanager != null)
			taskmanager.shutdown();
		taskmanager = null;
	}

	public void addTasktoExecutor(Servicebean bean) {
		wsQueue.addMsg(bean);
		if (taskmanager != null) {
			WSRunner runnable_task = new WSRunner();
			runnable_task.setserviceInfo(server_ip, connect_ort, wsdl_link,
					namespace, service_callback);
			taskmanager.execute(runnable_task);
		}
	}

	public void setSrviceCallback(WebServiceCallback callback) {
		this.service_callback = callback;
	}

	public void clearBGTask() {
		Log.i("webservice123", "Current Queue size1" + wsQueue.getSize());
		wsQueue.queue.clear();
		Log.i("webservice123", "Current Queue size2" + wsQueue.getSize());

	}

}
