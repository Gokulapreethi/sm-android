package com.chat;

import org.lib.model.BuddyInformationBean;
import org.lib.model.SignalingBean;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.main.AppMainActivity;
import com.util.Queue;
import com.util.SingleInstance;

public class ChatProcesser extends Thread {

	private boolean isRunning;
	private Queue queue;

	public ChatProcesser(Queue queue) {
		this.queue = queue;
	}

	public ChatProcesser() {
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
		try {
			while (isRunning) {
				ChatBean chatBean = (ChatBean) queue.getObject();

				BuddyInformationBean bib = null;
//						WebServiceReferences.buddyList.get(chatBean.getToUser());

				SignalingBean sb = new SignalingBean();
				sb.setFrom(chatBean.getFromUser());
				sb.setTo(chatBean.getToUser());
				sb.setType("11");
				sb.setSessionid(chatBean.getSessionId());
				sb.setSignalid("" + Math.random() * 100000000);
				sb.setResult("0");
				sb.setisRobo("");
				sb.setTolocalip(bib.getLocalipaddress());
				sb.setTopublicip(bib.getExternalipaddress());
				sb.setToSignalPort(bib.getSignalingPort());
				sb.setCallType(chatBean.getMessageType());

				if (chatBean.getMessageType().equalsIgnoreCase("MTP")) {
					String message = convertSpecialCharacters(chatBean
							.getMessage());
					sb.setMessage(message);
					CallDispatcher.message_map.put(chatBean.getToUser(),
							chatBean.getMessage());
				} else {
					if (chatBean.getMessageType().equalsIgnoreCase("MAP"))
						CallDispatcher.message_map.put(chatBean.getToUser(),
								"Audio Message");
					else if (chatBean.getMessageType().equalsIgnoreCase("MVP"))
						CallDispatcher.message_map.put(chatBean.getToUser(),
								"Video Message");
					else if (chatBean.getMessageType().equalsIgnoreCase("MHP"))
						CallDispatcher.message_map.put(chatBean.getToUser(),
								"Handsketch Message");
					sb.setFileId("007");
					sb.setFilePath(chatBean.getMessage());
					sb.setFtpUser(CallDispatcher.LoginUser);
					sb.setFtppassword(CallDispatcher.Password);
					Log.i("chat", "chat123 filepath :: " + sb.getFilePath());
				}
				if (AppMainActivity.commEngine != null)
					AppMainActivity.commEngine.makeIM(sb);
				else
					Toast.makeText(SipNotificationListener.getCurrentContext(),
							"communication engine is null", Toast.LENGTH_LONG)
							.show();

				chatBean.setSessionId(sb.getSessionid());
				chatBean.setSignalId(sb.getSignalid());
				ChatBean bean = chatBean.clone();
				if (!chatBean.getMessageType().equalsIgnoreCase("MTP"))
					bean.setMessage(Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + chatBean.getMessage());
				SingleInstance.getChatHistoryWriter().getQueue()
						.addObject(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String convertSpecialCharacters(String message) {
		// used to Convert special character

		message = message.replace("&", "&amp;");

		message = message.replace("<", "&lt;");

		message = message.replace(">", "&gt;");

		message = message.replace("'", "&apos;");

		// String d = "\"";
		message = message.replace("\"", "&quot;");

		return message;
	}

}
