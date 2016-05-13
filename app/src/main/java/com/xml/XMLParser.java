package com.xml;

import java.io.StringReader;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.WebServiceReferences;
import com.chat.ChatBean;

public class XMLParser {

	public Vector<ChatBean> parseChatHistory(String xml) {
		Vector<ChatBean> vector = null;
		ChatBean chatBean = null;
		DocumentBuilderFactory builderFactory = null;
		DocumentBuilder documentBuilder = null;
		InputSource inputSource = null;
		Document document = null;
		Element element = null;
		CallDispatcher callDisp = null;
		try {
			vector = new Vector<ChatBean>();
			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(
						SipNotificationListener.getCurrentContext());
			builderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = builderFactory.newDocumentBuilder();
			inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(xml));
			document = documentBuilder.parse(inputSource);
			NodeList nList = document.getElementsByTagName("chat");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					element = (Element) nNode;
					chatBean = new ChatBean();
					chatBean.setFromUser(element.getAttribute("from"));
					chatBean.setToUser(element.getAttribute("to"));
					if (element.getAttribute("issender").equals("true")) {
						chatBean.setSender(true);
					} else {
						chatBean.setSender(false);
					}
					chatBean.setSessionId(element.getAttribute("sessionid"));
					chatBean.setSignalId(element.getAttribute("signalid"));
					chatBean.setStatus(element.getAttribute("status"));
					chatBean.setMessageType(element.getAttribute("type"));
					chatBean.setMessage(element.getAttribute("message"));
//					if (chatBean.getMessageType().equals("MPP")
//							|| chatBean.getMessageType().equals("MHP")) {
//						Bitmap bitMap = callDisp.ResizeImage(chatBean
//								.getMessage());
//						bitMap = Bitmap.createScaledBitmap(bitMap, 200, 150,
//								false);
//						chatBean.setProfilePic(bitMap);
//					}
					chatBean.setTimeDate(element.getAttribute("datetime"));
					vector.add(chatBean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vector;
	}
}
