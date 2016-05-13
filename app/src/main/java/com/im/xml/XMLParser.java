package com.im.xml;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

public class XMLParser {

	ArrayList<Object> parseList = new ArrayList<Object>();

	public XMLParser(String strFileName) {

		try {

			File fXmlFile = new File(strFileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("mm");
			Log.d("XML", "QQQQQQQQQQQQQQQQQQQQQQQ" + nList.getLength());

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				NodeList nl = nNode.getChildNodes();

				// System.out.println("~~~~" + nl.getLength());

				for (int j = 0; j < nl.getLength(); j++) {
					Node chatElementNode = nl.item(j);

					// System.out.println(nNodec.getNodeType()+"----" +
					// nNodec.getNodeName());

					NamedNodeMap attrs = chatElementNode.getAttributes();
					if (attrs != null) {

						System.out
								.println("****************************************************");

						for (int idx = 0; idx < attrs.getLength(); idx++) {
							System.out.println(attrs.item(idx).getNodeName()
									+ " :" + attrs.item(idx).getNodeValue());
						}
						System.out
								.println("****************************************************");

						if (chatElementNode.getNodeType() == Node.ELEMENT_NODE) {
							if (chatElementNode.getNodeName().equals("txt")) {
								parseList.add(getTextChatBean(chatElementNode,
										attrs.item(0).getNodeValue()));
							} else if (chatElementNode.getNodeName().equals(
									"audio")) {
								parseList.add(getAudioChatBean(chatElementNode,
										attrs.item(0).getNodeValue()));

							} else if (chatElementNode.getNodeName().equals(
									"video")) {
								parseList.add(getVideoChatBean(chatElementNode,
										attrs.item(0).getNodeValue()));

							} else if (chatElementNode.getNodeName().equals(
									"img")) {
								parseList.add(getImgtChatBean(chatElementNode,
										attrs.item(0).getNodeValue()));

							} else if (chatElementNode.getNodeName().equals(
									"location")) {
								parseList.add(getLocationChatBeab(
										chatElementNode, attrs.item(0)
												.getNodeValue()));
							}

						}

					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	TextChatBean getTextChatBean(Node chatElementNode, String type) {

		TextChatBean obj = new TextChatBean();

		NodeList childListofChatElement = chatElementNode.getChildNodes();

		for (int i = 0; i < childListofChatElement.getLength(); i++) {
			Node no = childListofChatElement.item(i);

			if (no.getNodeType() == Node.ELEMENT_NODE) {
				// System.out.println("\t" + no.getNodeName() + "==>"
				// + no.getTextContent());

				if (no.getNodeName().equals("user")) {
					obj.setUserName(no.getTextContent());
				} else if (no.getNodeName().equals("msg")) {
					String myMessageToSend = no.getTextContent();
					if (myMessageToSend.contains("&amp;")) {
						myMessageToSend = myMessageToSend.replace("&amp;", "");
					}
					if (myMessageToSend.contains("&lt;")) {
						myMessageToSend = myMessageToSend.replace("&lt;", "<");
					}
					if (myMessageToSend.contains("&gt;")) {
						myMessageToSend = myMessageToSend.replace("&gt;", ">");
					}
					if (myMessageToSend.contains("&apos;")) {
						myMessageToSend = myMessageToSend
								.replace("&apos;", "'");
					}
					if (myMessageToSend.contains("&quot;")) {
						// String d = "\"";
						myMessageToSend = myMessageToSend.replace("&quot;",
								"\"");
					}

					obj.setMessage(myMessageToSend);

				} else if (no.getNodeName().equals("buddy")) {
					obj.setBuddyName(no.getTextContent());
				} else if (no.getNodeName().equals("time")) {
					obj.setChatTime(no.getTextContent());
				} else if (no.getNodeName().equals("size")) {
					obj.setSize(no.getTextContent());
				} else if (no.getNodeName().equals("color")) {
					obj.setColor(no.getTextContent());
				} else if (no.getNodeName().equals("face")) {
					obj.setFace(no.getTextContent());
				} else if (no.getNodeName().equals("style")) {
					obj.setStyle(no.getTextContent());
				} else if (no.getNodeName().equalsIgnoreCase("oldFile")) {
					obj.setOldFileName(no.getTextContent());
				} else if (no.getNodeName().equalsIgnoreCase("file")) {
					obj.setFileName(no.getTextContent());
				} else if (no.getNodeName().equalsIgnoreCase("isimported")) {
					if (no.getTextContent().equals("1")) {
						obj.setisImported(true);
					} else {
						obj.setisImported(false);
					}
				} else if (no.getNodeName().equalsIgnoreCase("isrobo")) {
					if (no.getTextContent().equals("1")) {
						obj.setIsRobo(true);
					} else {
						obj.setIsRobo(false);
					}
				} else if (no.getNodeName().equalsIgnoreCase("id")) {
					obj.setId(Integer.parseInt(no.getTextContent()));
				} else if (no.getNodeName().equalsIgnoreCase("flag")) {
					obj.setFlag(Integer.parseInt(no.getTextContent()));
				} else if (no.getNodeName().equalsIgnoreCase("signalid")) {
					obj.setSignalId(no.getTextContent());
				}

			}

		}
		if (type.equals("sender")) {
			obj.setType(1);
		} else {
			obj.setType(2);
		}
		return obj;
	}

	ImageChatBean getImgtChatBean(Node chatElementNode, String type) {

		ImageChatBean obj = new ImageChatBean();

		NodeList childListofChatElement = chatElementNode.getChildNodes();

		for (int i = 0; i < childListofChatElement.getLength(); i++) {
			Node no = childListofChatElement.item(i);

			if (no.getNodeType() == Node.ELEMENT_NODE) {
				// System.out.println("\t" + no.getNodeName() + "==>"
				// + no.getTextContent());

				if (no.getNodeName().equals("user")) {
					obj.setUserName(no.getTextContent());
				} else if (no.getNodeName().equals("file")) {
					obj.setFilePath(no.getTextContent());
				} else if (no.getNodeName().equals("buddy")) {
					obj.setBuddyName(no.getTextContent());
				} else if (no.getNodeName().equals("time")) {
					obj.setChatTime(no.getTextContent());
				} else if (no.getNodeName().equalsIgnoreCase("oldFile")) {
					obj.setOldFileName(no.getTextContent());
				} else if (no.getNodeName().equalsIgnoreCase("isimported")) {
					if (no.getTextContent().equals("1")) {
						obj.setisImported(true);
					} else {
						obj.setisImported(false);
					}
				} else if (no.getNodeName().equalsIgnoreCase("isrobo")) {
					if (no.getTextContent().equals("1")) {
						obj.setIsRobo(true);
					} else {
						obj.setIsRobo(false);
					}
				} else if (no.getNodeName().equalsIgnoreCase("id")) {
					obj.setId(Integer.parseInt(no.getTextContent()));
				} else if (no.getNodeName().equalsIgnoreCase("flag")) {
					obj.setFlag(Integer.parseInt(no.getTextContent()));
				} else if (no.getNodeName().equalsIgnoreCase("signalid")) {
					obj.setSignalId(no.getTextContent());
				}

			}

		}
		if (type.equals("sender")) {
			obj.setType(1);
		} else {
			obj.setType(2);
		}
		return obj;
	}

	LocationChatBean getLocationChatBeab(Node chatElementNode, String type) {

		LocationChatBean obj = new LocationChatBean();

		NodeList childListofChatElement = chatElementNode.getChildNodes();

		for (int i = 0; i < childListofChatElement.getLength(); i++) {
			Node no = childListofChatElement.item(i);

			if (no.getNodeType() == Node.ELEMENT_NODE) {
				// System.out.println("\t" + no.getNodeName() + "==>"
				// + no.getTextContent());

				if (no.getNodeName().equals("user")) {
					obj.setUserName(no.getTextContent());
				} else if (no.getNodeName().equals("file")) {
					obj.setFilePath(no.getTextContent());
				} else if (no.getNodeName().equals("buddy")) {
					obj.setBuddyName(no.getTextContent());
				} else if (no.getNodeName().equals("time")) {
					obj.setChatTime(no.getTextContent());
				} else if (no.getNodeName().equals("latitude")) {
					obj.setLatitude(no.getTextContent());
				} else if (no.getNodeName().equals("longitude")) {
					obj.setLongitude(no.getTextContent());
				} else if (no.getNodeName().equals("address")) {
					obj.setAddress(no.getTextContent());
				}

			}

		}
		if (type.equals("sender")) {
			obj.setType(1);
		} else {
			obj.setType(2);
		}
		return obj;

	}

	AudioChatBean getAudioChatBean(Node chatElementNode, String type) {

		AudioChatBean obj = new AudioChatBean();

		NodeList childListofChatElement = chatElementNode.getChildNodes();

		for (int i = 0; i < childListofChatElement.getLength(); i++) {
			Node no = childListofChatElement.item(i);

			if (no.getNodeType() == Node.ELEMENT_NODE) {
				// System.out.println("\t" + no.getNodeName() + "==>"
				// + no.getTextContent());
				if (no.getNodeName().equals("user")) {
					obj.setUserName(no.getTextContent());
				} else if (no.getNodeName().equals("file")) {
					obj.setFilePath(no.getTextContent());
				} else if (no.getNodeName().equals("buddy")) {
					obj.setBuddyName(no.getTextContent());
				} else if (no.getNodeName().equals("time")) {
					obj.setChatTime(no.getTextContent());
				} else if (no.getNodeName().equalsIgnoreCase("oldFile")) {
					obj.setOldFileName(no.getTextContent());
				} else if (no.getNodeName().equalsIgnoreCase("isimported")) {
					if (no.getTextContent().equals("1")) {
						obj.setisImported(true);
					} else {
						obj.setisImported(false);
					}
				} else if (no.getNodeName().equalsIgnoreCase("isrobo")) {
					if (no.getTextContent().equals("1")) {
						obj.setIsRobo(true);
					} else {
						obj.setIsRobo(false);
					}
				} else if (no.getNodeName().equalsIgnoreCase("id")) {
					obj.setId(Integer.parseInt(no.getTextContent()));
				} else if (no.getNodeName().equalsIgnoreCase("flag")) {
					Log.i("thread", "################## condition satisfied"
							+ no.getTextContent());
					obj.setFlag(Integer.parseInt(no.getTextContent()));
				} else if (no.getNodeName().equalsIgnoreCase("signalid")) {
					obj.setSignalId(no.getTextContent());
				}
			}

		}
		if (type.equals("sender")) {
			obj.setType(1);
		} else {
			obj.setType(2);
		}
		return obj;
	}

	VideoChatBean getVideoChatBean(Node chatElementNode, String type) {

		VideoChatBean obj = new VideoChatBean();

		NodeList childListofChatElement = chatElementNode.getChildNodes();

		for (int i = 0; i < childListofChatElement.getLength(); i++) {
			Node no = childListofChatElement.item(i);

			if (no.getNodeType() == Node.ELEMENT_NODE) {
				// System.out.println("\t" + no.getNodeName() + "==>"
				// + no.getTextContent());

				if (no.getNodeName().equals("user")) {
					obj.setUserName(no.getTextContent());
				} else if (no.getNodeName().equals("file")) {
					obj.setFilePath(no.getTextContent());
				} else if (no.getNodeName().equals("buddy")) {
					obj.setBuddyName(no.getTextContent());
				} else if (no.getNodeName().equals("time")) {
					obj.setChatTime(no.getTextContent());
				} else if (no.getNodeName().equalsIgnoreCase("oldFile")) {
					obj.setOldfileName(no.getTextContent());
				} else if (no.getNodeName().equalsIgnoreCase("isimported")) {
					if (no.getTextContent().equals("1")) {
						obj.setisImported(true);
					} else {
						obj.setisImported(false);
					}
				} else if (no.getNodeName().equalsIgnoreCase("isrobo")) {
					if (no.getTextContent().equals("1")) {
						obj.setIsRobo(true);
					} else {
						obj.setIsRobo(false);
					}
				} else if (no.getNodeName().equalsIgnoreCase("id")) {
					obj.setId(Integer.parseInt(no.getTextContent()));
				} else if (no.getNodeName().equalsIgnoreCase("flag")) {
					obj.setFlag(Integer.parseInt(no.getTextContent()));
				} else if (no.getNodeName().equalsIgnoreCase("signalid")) {
					obj.setSignalId(no.getTextContent());
				}

			}

		}
		if (type.equals("sender")) {
			obj.setType(1);
		} else {
			obj.setType(2);
		}
		return obj;
	}

	public ArrayList<Object> getParseList() {
		return parseList;
	}

}
