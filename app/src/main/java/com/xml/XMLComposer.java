package com.xml;

import com.bean.GroupChatBean;
import com.cg.hostedconf.AppReference;
import com.chat.ChatBean;

public class XMLComposer {

	public String composeChatHistoryXML(ChatBean chatBean) {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("<chat from=\"");
			builder.append(chatBean.getFromUser());
			builder.append("\" to=\"");
			builder.append(chatBean.getToUser());
			builder.append("\" issender=\"");
			builder.append(chatBean.isSender());
			builder.append("\" sessionid=\"");
			builder.append(chatBean.getSessionId());
			builder.append("\" signalid=\"");
			builder.append(chatBean.getSignalId());
			builder.append("\" status=\"");
			builder.append(chatBean.getStatus());
			builder.append("\" datetime=\"");
			builder.append(chatBean.getTimeDate());
			builder.append("\" message=\"");
			builder.append(chatBean.getMessage());
			builder.append("\" type=\"");
			builder.append(chatBean.getMessageType());
			builder.append("\"/>");
			return builder.toString();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return null;
	}

	public String groupChatComposer(GroupChatBean bean) {
		StringBuilder builder = null;
		try {
			builder = new StringBuilder();
			builder.append("<?xml version=\"1.0\"?>");
			builder.append("<com result=\"");
			builder.append(bean.getResult());
			builder.append("\" type=\"");
			builder.append(bean.getType());
			builder.append("\"><tl category=\"");
			builder.append(bean.getCategory());
			builder.append("\" subcategory=\"");
			builder.append(bean.getSubCategory());
			builder.append("\" mimetype=\"");
			builder.append(bean.getMimetype());
			builder.append("\" sessionid=\"");
			builder.append(bean.getSessionid());
			builder.append("\" from=\"");
			builder.append(bean.getFrom());
			builder.append("\" to=\"");
			builder.append(bean.getTo());
			builder.append("\" signalid=\"");
			builder.append(bean.getSignalid());
			builder.append("\" senttime=\"");
			builder.append(bean.getSenttime());
			builder.append("\" senttimez=\"");
			builder.append(bean.getSenttimez());
			builder.append("\"/><message>");
			builder.append("<![CDATA[");
			builder.append(bean.getMessage());
			builder.append("]]>");
			builder.append("</message>");
			builder.append("<media");
			builder.append("\" filename=\"");
			builder.append(bean.getMediaName());
			builder.append("\" ftpusername=\"");
			builder.append(bean.getFtpUsername());
			builder.append("\" ftppassword=\"");
			builder.append(bean.getFtpPassword());
			builder.append("\"/>");
			builder.append("</com>");
			return builder.toString();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return null;
	}
}
