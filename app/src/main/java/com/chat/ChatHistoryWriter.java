package com.chat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.cg.hostedconf.AppReference;
import com.util.Queue;

public class ChatHistoryWriter extends Thread {
	private boolean isRunning;
	private Queue queue;

	public ChatHistoryWriter(Queue queue) {
		this.queue = queue;
	}

	public ChatHistoryWriter() {
		queue = new Queue();
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	@Override
	public void run() {

		while (isRunning) {
			try {
				ChatBean chatBean = (ChatBean) queue.getObject();
				if (chatBean != null) {

					// Log.d("debug123", "ChatBean : "+chatBean.toString());
					// XMLComposer xmlComposer = new XMLComposer();
					// String chatXML = xmlComposer
					// .composeChatHistoryXML(chatBean);
					//
					// String fileName = null;
					// if (chatBean.isSender())
					// fileName = chatBean.getFromUser()
					// + chatBean.getToUser();
					// else
					// fileName = chatBean.getToUser()
					// + chatBean.getFromUser();
					// String path = Environment.getExternalStorageDirectory()
					// + "/COMMedia/chat/" + fileName + ".xml";
					//
					// fileWriter(chatXML, path);
//					DB.getdbHeler(
//							SipNotificationListener.getCurrentContext())
//							.insertChat(chatBean);

				}
			} catch (Exception e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				else
					e.printStackTrace();
			}
		}
	}

	private void fileWriter(String xml, String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWritter = new FileWriter(file.getPath(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(xml);
			bufferWritter.flush();
			bufferWritter.close();
			bufferWritter = null;
			fileWritter.flush();
			fileWritter.close();
			fileWritter = null;

		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

}
