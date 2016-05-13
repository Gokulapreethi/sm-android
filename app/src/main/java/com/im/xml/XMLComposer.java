package com.im.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import android.util.Log;

import com.chat.ChatBean;

public class XMLComposer {

	private DocumentBuilderFactory documentBuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document document;
	private Element rootElement;

	private String[] strArTextElements = { "user", "msg", "buddy", "time",
			"size", "color", "face", "style", "file", "oldFile", "isrobo",
			"isimported", "id", "flag", "signalid" };
	private String[] strArOtherElements = { "user", "file", "buddy", "time",
			"oldFile", "isrobo", "isimported", "id", "flag", "signalid" };
	private String[] strArMAPElements = { "user", "file", "buddy", "time",
			"latitude", "longitude", "address", "id" };

	private String fileName;

	public XMLComposer(String filename) {

		fileName = filename;
		File f = new File(filename);

		if (!f.exists()) {
			System.out.println("File is not exists");
			try {
				createXMLFile(filename);
			} catch (ParserConfigurationException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			} catch (TransformerException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
			System.out.println("File created");

		} else {
			System.out.println("File exists");
			try {
				makeDocumentObject(filename);
			} catch (ParserConfigurationException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			} catch (SAXException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initialize the Document object
	 * 
	 * @param strFileName
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void makeDocumentObject(String strFileName)
			throws ParserConfigurationException, SAXException, IOException {

		File fXmlFile = new File(strFileName);
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		document = (Document) documentBuilder.parse(fXmlFile);
		rootElement = document.getDocumentElement();
		System.out.println("Root Element ******:" + rootElement.getNodeName());
	}

	/**
	 * If File is not exist create the file with root element
	 * 
	 * @param strFileName
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	void createXMLFile(String strFileName) throws ParserConfigurationException,
			TransformerException {

		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		document = documentBuilder.newDocument();
		rootElement = document.createElement("mm");
		document.appendChild(rootElement);
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File(strFileName));
		transformer.transform(source, result);

	}

	/**
	 * Add child nodes.
	 * 
	 * @param noteType
	 *            - txt (or) img (or) audio (or) video
	 * @param buddyStatus
	 *            - sender (or) receiver
	 * @param data
	 *            - For the txt array length will be 8 others array length will
	 *            be 4
	 * 
	 */
	public void addChildNode(String noteType, String buddyStatus, String[] data)
			throws Exception {
		Log.d("XML", "#######################add child node called");
		if (document == null) {
			try {
				makeDocumentObject(fileName);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("X", " document is null :" + fileName);
		} else {
			Log.e("X", "document is not null");
		}

		Element element = document.createElement(noteType);
		element.setAttribute("category", buddyStatus);

		if (noteType.equalsIgnoreCase("txt")) {

			Log.d("XML", "Came to else part");
			Log.d("XML", "Came to else part" + data.length);
			for (int i = 0; i < data.length; i++) {
				Node nodeTitle = document.createElement(strArTextElements[i]);
				Log.e("X", "strArMAPElements:" + strArTextElements[i]);
				if (data[i] != null) {
					Log.e("X", "strArTextElements:" + data[i]);
					Text textTitle = document.createTextNode(data[i]);
					nodeTitle.appendChild(textTitle);
				}

				element.appendChild(nodeTitle);

			}

		} else if (noteType.equalsIgnoreCase("location")) {
			Log.e("X", "Write map Data ");
			for (int i = 0; i < data.length; i++) {
				Node nodeTitle = document.createElement(strArMAPElements[i]);
				Log.e("X", "strArMAPElements:" + strArMAPElements[i]);
				Text textTitle = document.createTextNode(data[i]);
				Log.i("X", "data:" + data[i]);
				nodeTitle.appendChild(textTitle);
				element.appendChild(nodeTitle);
			}
			Log.e("X", "Write map Data finished");
		} else {

			for (int i = 0; i < data.length; i++) {
				Node nodeTitle = document.createElement(strArOtherElements[i]);
				Log.e("X", "strArMAPElements:" + strArOtherElements[i]);
				Log.e("X", "strArMAPElements:" + data[i]);
				Text textTitle = document.createTextNode(data[i]);
				Log.i("X", "data:" + data[i]);
				nodeTitle.appendChild(textTitle);
				element.appendChild(nodeTitle);

			}

		}

		/*
		 * if(noteType.equalsIgnoreCase("")){ for (int i = 0; i < data.length;
		 * i++) { Node nodeTitle =
		 * document.createElement(strArOtherElements[i]); Log.e("X",
		 * "strArMAPElements:"+strArOtherElements[i]); Text textTitle =
		 * document.createTextNode(data[i]); Log.i("X", "data:"+data[i]);
		 * nodeTitle.appendChild(textTitle); element.appendChild(nodeTitle);
		 * 
		 * } }else if(noteType.equals("")){ Log.e("X", "Write map Data "); for
		 * (int i = 0; i < data.length; i++) { Node nodeTitle =
		 * document.createElement(strArMAPElements[i]); Log.e("X",
		 * "strArMAPElements:"+strArMAPElements[i]); Text textTitle =
		 * document.createTextNode(data[i]); Log.i("X", "data:"+data[i]);
		 * nodeTitle.appendChild(textTitle); element.appendChild(nodeTitle); }
		 * Log.e("X", "Write map Data finished"); } else{
		 * Log.d("XML","Came to else part");
		 * Log.d("XML","Came to else part"+data.length); for (int i = 0; i <
		 * data.length; i++) { Node nodeTitle =
		 * document.createElement(strArTextElements[i]); Log.e("X",
		 * "strArMAPElements:"+strArTextElements[i]); if(data[i]!=null){
		 * Log.e("X", "strArTextElements:"+data[i]); Text textTitle =
		 * document.createTextNode(data[i]); nodeTitle.appendChild(textTitle); }
		 * element.appendChild(nodeTitle);
		 * 
		 * } }
		 */

		rootElement.appendChild(element);

		try {
			writeIntoFile(document, fileName);
		} catch (TransformerException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

		// System.out.println(noteType +" data added in to xml");
	}

	public void addChildNode(Object bean) throws Exception {
		System.out.println("addchild called___________________");

		int id = 0;

		Node no = document.getLastChild();
		Log.e("XX", "XXXXXXXXXXXXcondition satisfied" + no);
		if (no.getNodeType() == Node.ELEMENT_NODE) {
			Node ele = no.getLastChild();
			if (ele != null) {
				Log.e("XX",
						"XXXXXXXXXXXXcondition satisfied" + no.getNodeName());
				Log.e("XX",
						"XXXXXXXXXXXXcondition satisfied" + ele.getNodeName());

				NodeList list = ele.getChildNodes();
				Log.e("XX",
						"XXXXXXXXXXXXcondition satisfied" + list.getLength());
				for (int i = 0; i < list.getLength(); i++) {
					Node nd = list.item(i);
					if (nd.getNodeName().equalsIgnoreCase("id")) {
						Log.e("XX",
								"XXXXXXXXXXXXcondition satisfied"
										+ nd.getTextContent());
						String val = nd.getTextContent();
						if (val != null && val.trim().length() != 0) {
							int idval = Integer.parseInt(nd.getTextContent());
							id = idval + 1;
						}
					}
				}
			} else {
				id = 0;
			}
		}

		if (bean instanceof TextChatBean) {
			System.out.println("Text bean__________________");
			TextChatBean tcb = (TextChatBean) bean;

			String[] data = new String[15];
			String buddyStatus = null;
			String noteType = "txt";
			Log.d("Type", "%%%%%%%%%%%%%%%%%%%%%%%" + tcb.getType());
			if (tcb.getType() == 1) {
				buddyStatus = "sender";
			} else if (tcb.getType() == 2) {

				buddyStatus = "receiver";
				Log.d("Type", "%%%%%%%%%%%%%%%%%%%%%%%" + buddyStatus);
			}

			data[0] = tcb.getUserName();
			String myMessageToSend = tcb.getMessage();
			if (myMessageToSend.contains("&")) {
				myMessageToSend = myMessageToSend.replace("&", "&amp;");
			}
			if (myMessageToSend.contains("<")) {
				myMessageToSend = myMessageToSend.replace("<", "&lt;");
			}
			if (myMessageToSend.contains(">")) {
				myMessageToSend = myMessageToSend.replace(">", "&gt;");
			}
			if (myMessageToSend.contains("'")) {
				myMessageToSend = myMessageToSend.replace("'", "&apos;");
			}
			if (myMessageToSend.contains("\"")) {
				// String d = "\"";
				myMessageToSend = myMessageToSend.replace("\"", "&quot;");
			}
			tcb.setMessage(myMessageToSend);
			data[1] = tcb.getMessage();
			data[2] = tcb.getBuddyName();
			data[3] = tcb.getChatTime();
			data[4] = tcb.getSize();
			data[5] = tcb.getColor();
			data[6] = tcb.getFace();
			data[7] = tcb.getStyle();

			if (tcb.getFileName().trim().length() != 0
					&& !tcb.getFileName().equalsIgnoreCase("empty")) {
				data[8] = tcb.getFileName();
			} else {
				data[8] = "empty";
			}

			if (tcb.getOldFileName().trim().length() != 0
					&& !tcb.getOldFileName().equalsIgnoreCase("empty")) {
				data[9] = tcb.getOldFileName();
			} else {
				data[9] = "empty";
			}

			if (tcb.getIsRobo()) {
				data[10] = "1";
			} else {
				data[10] = "0";
			}

			if (tcb.getisImported()) {
				data[11] = "1";
			} else {
				data[11] = "0";
			}
			data[12] = Integer.toString(id);
			data[13] = Integer.toString(tcb.getFlag());
			data[14] = tcb.getSignalId();

			Log.e("X", "noteType:" + noteType);
			Log.e("X", "buddyStatus:" + buddyStatus);
			Log.e("X", "data size:" + data.length);
			Log.e("X", "Type" + tcb.getType());
			addChildNode(noteType, buddyStatus, data);

		} else if (bean instanceof ImageChatBean) {
			ImageChatBean icb = (ImageChatBean) bean;
			String[] data = new String[10];
			String buddyStatus = null;
			String noteType = "img";
			if (icb.getType() == 1) {
				buddyStatus = "sender";
			} else if (icb.getType() == 2) {
				buddyStatus = "receiver";
			}
			data[0] = icb.getUserName();
			data[1] = icb.getFilePath();
			data[2] = icb.getBuddyName();
			data[3] = icb.getChatTime();

			if (icb.getOldfileName().trim().length() != 0
					&& !icb.getOldfileName().equalsIgnoreCase("empty")) {
				data[4] = icb.getOldfileName();
			} else {
				data[4] = "empty";
			}

			if (icb.getIsRobo()) {
				data[5] = "1";
			} else {
				data[5] = "0";
			}

			if (icb.getisImported()) {
				data[6] = "1";
			} else {
				data[6] = "0";
			}
			data[7] = Integer.toString(id);
			data[8] = Integer.toString(icb.getFlag());
			data[9] = icb.getSignalId();

			addChildNode(noteType, buddyStatus, data);

		} else if (bean instanceof AudioChatBean) {
			AudioChatBean acb = (AudioChatBean) bean;
			String[] data = new String[10];
			String buddyStatus = null;
			String noteType = "audio";
			if (acb.getType() == 1) {
				buddyStatus = "sender";
			} else if (acb.getType() == 2) {
				buddyStatus = "receiver";
			}
			data[0] = acb.getUserName();
			data[1] = acb.getFilePath();
			data[2] = acb.getBuddyName();
			data[3] = acb.getChatTime();
			if (acb.getOldFileName().trim().length() != 0
					&& !acb.getOldFileName().equalsIgnoreCase("empty")) {
				data[4] = acb.getOldFileName();
			} else {
				data[4] = "empty";
			}

			if (acb.getIsRobo()) {
				data[5] = "1";
			} else {
				data[5] = "0";
			}

			if (acb.getisImported()) {
				data[6] = "1";
			} else {
				data[6] = "0";
			}

			data[7] = Integer.toString(id);
			data[8] = Integer.toString(acb.getFlag());
			data[9] = acb.getSignalId();

			addChildNode(noteType, buddyStatus, data);
		} else if (bean instanceof VideoChatBean) {
			VideoChatBean vcb = (VideoChatBean) bean;
			String[] data = new String[10];
			String buddyStatus = null;
			String noteType = "video";
			if (vcb.getType() == 1) {
				buddyStatus = "sender";
			} else if (vcb.getType() == 2) {
				buddyStatus = "receiver";
			}
			data[0] = vcb.getUserName();
			data[1] = vcb.getFilePath();
			data[2] = vcb.getBuddyName();
			data[3] = vcb.getChatTime();

			if (vcb.getoldFileName().trim().length() != 0
					&& !vcb.getoldFileName().equalsIgnoreCase("empty")) {
				data[4] = vcb.getoldFileName();
			} else {
				data[4] = "empty";
			}

			if (vcb.getIsRobo()) {
				data[5] = "1";
			} else {
				data[5] = "0";
			}

			if (vcb.getisImported()) {
				data[6] = "1";
			} else {
				data[6] = "0";
			}
			data[7] = Integer.toString(id);
			data[8] = Integer.toString(vcb.getFlag());
			data[9] = vcb.getSignalId();

			addChildNode(noteType, buddyStatus, data);
		} else if (bean instanceof LocationChatBean) {
			LocationChatBean icb = (LocationChatBean) bean;
			String[] data = new String[8];
			String buddyStatus = null;
			String noteType = "location";
			if (icb.getType() == 1) {
				buddyStatus = "sender";
			} else if (icb.getType() == 2) {
				buddyStatus = "receiver";
			}
			data[0] = icb.getUserName();
			data[1] = icb.getFilePath();
			data[2] = icb.getBuddyName();
			data[3] = icb.getChatTime();

			data[4] = icb.getLatitude();
			data[5] = icb.getLongitude();
			data[6] = icb.getAddress();

			Log.e("X", "noteType:" + noteType);
			Log.e("X", "buddyStatus:" + buddyStatus);
			Log.e("X", "data size:" + data.length);

			data[7] = Integer.toString(id);
			addChildNode(noteType, buddyStatus, data);

		}

	}

	void writeIntoFile(Document doc, String fname) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		StreamResult result = new StreamResult(new File(fileName));
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		Log.e("lgg", "Modified finished  file");

	}

	private void sfunction(String M_type, String signalid) {

		NodeList nodelist = document.getElementsByTagName("mm");
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node no = nodelist.item(i);
			Log.e("XX", "XXXXXXXXXXXXcondition satisfied1" + no.getNodeName());
			NodeList childs = no.getChildNodes();
			Log.e("XX", "XXXXXXXXXXXXcondition satisfied2" + childs.getLength());
			for (int idx = 0; idx < childs.getLength(); idx++) {
				Node childnode = childs.item(idx);
				Log.e("XX",
						"XXXXXXXXXXXXcondition satisfied2"
								+ childnode.getNodeName());
				if (childnode.getNodeName().equalsIgnoreCase(M_type)) {
					NodeList attributes = childnode.getChildNodes();
					for (int att = 0; att < attributes.getLength(); att++) {
						Node nvalues = attributes.item(att);
						if (nvalues.getNodeName().equalsIgnoreCase("signalid")) {
							String id = nvalues.getTextContent();

							if (M_type.equals("txt")) {
								if (id.equals(signalid)) {
									for (int j = 0; j < attributes.getLength(); j++) {
										Node values = attributes.item(j);
										Log.e("XX",
												"XXXXXXXXXXXXcondition satisfied2"
														+ values);
										Log.e("XX",
												"XXXXXXXXXXXXcondition satisfied2"
														+ values.getNodeName());
										if (values.getNodeName()
												.equalsIgnoreCase("flag")) {
											Log.i("flg",
													"################## condition satisfied"
															+ values.getTextContent());
											/*
											 * values.getFirstChild().setNodeValue
											 * (flag);
											 */
											break;
											/* values.setNodeValue("ddsgdsg"); */
										}

									}
								}
							} else if (M_type.equals("img")) {
								if (id.equals(signalid)) {

									for (int j = 0; j < attributes.getLength(); j++) {
										Node values = attributes.item(j);
										Log.e("XX",
												"XXXXXXXXXXXXcondition satisfied2"
														+ values);
										Log.e("XX",
												"XXXXXXXXXXXXcondition satisfied2"
														+ values.getNodeName());
										if (values.getNodeName()
												.equalsIgnoreCase("flag")) {
											Log.i("flg",
													"################## condition satisfied"
															+ values.getTextContent());
											/*
											 * values.getFirstChild().setNodeValue
											 * (flag);
											 */

											break;
											/* values.setNodeValue("ddsgdsg"); */
										}

									}
								}

							} else if (M_type.equals("audio")) {
								if (id.equals(signalid)) {

									for (int j = 0; j < attributes.getLength(); j++) {
										Node values = attributes.item(j);
										Log.e("XX",
												"XXXXXXXXXXXXcondition satisfied2"
														+ values);
										Log.e("XX",
												"XXXXXXXXXXXXcondition satisfied2"
														+ values.getNodeName());
										if (values.getNodeName()
												.equalsIgnoreCase("flag")) {
											Log.i("flg",
													"################## condition satisfied"
															+ values.getTextContent());
											/*
											 * values.getFirstChild().setNodeValue
											 * (flag);
											 */
											break;
											/* values.setNodeValue("ddsgdsg"); */
										}

									}
								}

							} else if (M_type.equals("video")) {
								if (id.equals(signalid)) {

									for (int j = 0; j < attributes.getLength(); j++) {
										Node values = attributes.item(j);
										Log.e("XX",
												"XXXXXXXXXXXXcondition satisfied2"
														+ values);
										Log.e("XX",
												"XXXXXXXXXXXXcondition satisfied2"
														+ values.getNodeName());
										if (values.getNodeName()
												.equalsIgnoreCase("flag")) {
											Log.i("flg",
													"################## condition satisfied"
															+ values.getTextContent());
											/*
											 * values.getFirstChild().setNodeValue
											 * (flag);
											 */
											break;
											/* values.setNodeValue("ddsgdsg"); */
										}

									}
								}
							}

						}
					}
				}

			}

		}
	}

	public void updateXMLContent(Object bean) {
		try {
			Log.e("lgg", "came to update xml");
			String type = "";
			TextChatBean tbean = null;
			AudioChatBean abean = null;
			VideoChatBean vbean = null;
			ImageChatBean ibean = null;
			if (bean instanceof TextChatBean) {
				tbean = (TextChatBean) bean;
				type = "txt";
			} else if (bean instanceof AudioChatBean) {
				abean = (AudioChatBean) bean;
				type = "audio";
			} else if (bean instanceof ImageChatBean) {
				ibean = (ImageChatBean) bean;
				type = "img";
			} else if (bean instanceof VideoChatBean) {
				vbean = (VideoChatBean) bean;
				type = "video";
			}
			Log.e("lgg", "came to update xml" + type);
			/*
			 * NodeList childNodes = nodeGettingChanged.getChildNodes(); for
			 * (int i = 0; i != childNodes.getLength(); ++i) { Node child =
			 * childNodes.item(i); if (!(child instanceof Element)) continue;
			 * 
			 * if (child.getNodeName().equals("name"))
			 * child.getFirstChild().setNodeValue("SomethingElse") ; else if
			 * (child.getNodeName().equals("ip"))
			 * child.getFirstChild().setNodeValue("localHost") ; else if
			 * (child.getNodeName().equals("port"))
			 * child.getFirstChild().setNodeValue("4447") ; }
			 */

			NodeList nodelist = document.getElementsByTagName("mm");
			for (int i = 0; i < nodelist.getLength(); i++) {
				Node no = nodelist.item(i);
				Log.e("XX",
						"XXXXXXXXXXXXcondition satisfied1" + no.getNodeName());
				NodeList childs = no.getChildNodes();
				Log.e("XX",
						"XXXXXXXXXXXXcondition satisfied2" + childs.getLength());
				for (int idx = 0; idx < childs.getLength(); idx++) {
					Node childnode = childs.item(idx);
					Log.e("XX",
							"XXXXXXXXXXXXcondition satisfied2"
									+ childnode.getNodeName());
					if (childnode.getNodeName().equalsIgnoreCase(type)) {
						NodeList attributes = childnode.getChildNodes();
						for (int att = 0; att < attributes.getLength(); att++) {
							Node nvalues = attributes.item(att);
							if (nvalues.getNodeName().equalsIgnoreCase("id")) {
								int id = Integer.parseInt(nvalues
										.getTextContent());

								if (type.equals("txt")) {
									if (id == tbean.getId()) {
										for (int j = 0; j < attributes
												.getLength(); j++) {
											Node values = attributes.item(j);
											Log.e("XX",
													"XXXXXXXXXXXXcondition satisfied2"
															+ values);
											Log.e("XX",
													"XXXXXXXXXXXXcondition satisfied2"
															+ values.getNodeName());
											if (values
													.getNodeName()
													.equalsIgnoreCase("oldFile")) {
												if ((!tbean.getOldFileName()
														.equalsIgnoreCase(
																"empty"))
														&& (!tbean
																.getOldFileName()
																.equals(""))) {
													values.setTextContent(tbean
															.getOldFileName());
												}
												/*
												 * values.setNodeValue("ddsgdsg")
												 * ;
												 */
											}
											if ((values.getNodeName()
													.equalsIgnoreCase("file"))
													&& (!tbean.getFileName()
															.equals(""))) {
												Log.e("XX",
														"XXXXXXXXXXXXcondition satisfied33"
																+ values.getFirstChild());
												if (!tbean.getFileName()
														.equalsIgnoreCase(
																"empty")) {
													values.setTextContent(tbean
															.getFileName());
												}
												/*
												 * values.setNodeValue("ddsgdsg")
												 * ;
												 */
											}

										}
									}
								} else if (type.equals("img")) {
									if (id == ibean.getId()) {

										for (int j = 0; j < attributes
												.getLength(); j++) {
											Node values = attributes.item(j);
											if (values
													.getNodeName()
													.equalsIgnoreCase("oldFile")) {
												if ((!ibean.getOldfileName()
														.equalsIgnoreCase(
																"empty"))
														&& (!ibean
																.getOldfileName()
																.equals(""))) {
													values.setTextContent(ibean
															.getOldfileName());
												}
											}
											if (values.getNodeName()
													.equalsIgnoreCase("file")) {
												if ((!ibean.getFilePath()
														.equalsIgnoreCase(
																"empty"))
														&& (!ibean
																.getFilePath()
																.equals(""))) {
													values.setTextContent(ibean
															.getFilePath());
												}
											}

										}
									}

								} else if (type.equals("audio")) {
									if (id == abean.getId()) {

										for (int j = 0; j < attributes
												.getLength(); j++) {
											Node values = attributes.item(j);
											if (values
													.getNodeName()
													.equalsIgnoreCase("oldFile")) {
												if ((!abean.getOldFileName()
														.equalsIgnoreCase(
																"empty"))
														&& (!abean
																.getOldFileName()
																.equals(""))) {
													values.setTextContent(abean
															.getOldFileName());
												}
											}
											if (values.getNodeName()
													.equalsIgnoreCase("file")) {
												if ((!abean.getFilePath()
														.equalsIgnoreCase(
																"empty"))
														&& (!abean
																.getFilePath()
																.equals(""))) {
													values.setTextContent(abean
															.getFilePath());
												}
											}

										}
									}

								} else if (type.equals("video")) {
									if (id == vbean.getId()) {

										for (int j = 0; j < attributes
												.getLength(); j++) {
											Node values = attributes.item(j);
											if (values
													.getNodeName()
													.equalsIgnoreCase("oldFile")) {
												if ((!vbean.getoldFileName()
														.equalsIgnoreCase(
																"empty"))
														&& (!vbean
																.getoldFileName()
																.equals(""))) {
													values.setTextContent(vbean
															.getoldFileName());
												}
											}
											if (values.getNodeName()
													.equalsIgnoreCase("file")) {
												if ((!vbean.getFilePath()
														.equalsIgnoreCase(
																"empty"))
														&& (!vbean
																.getFilePath()
																.equals(""))) {
													values.setTextContent(vbean
															.getFilePath());
												}
											}

										}

									}
								}

							}
						}
					}

				}
			}
			try {
				writeIntoFile(document, fileName);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * Node no=document.getLastChild(); Log.e("XX",
			 * "XXXXXXXXXXXXcondition satisfied"+no);
			 * if(no.getNodeType()==Node.ELEMENT_NODE) { Node
			 * ele=no.getLastChild();
			 * 
			 * Log.e("XX", "XXXXXXXXXXXXcondition satisfied"+no.getNodeName());
			 * Log.e("XX", "XXXXXXXXXXXXcondition satisfied"+ele.getNodeName());
			 * 
			 * NodeList list=ele.getChildNodes(); Log.e("XX",
			 * "XXXXXXXXXXXXcondition satisfied"+list.getLength()); for(int
			 * i=0;i<list.getLength();i++) { Node nd=list.item(i);
			 * if(nd.getNodeName().equalsIgnoreCase("msg")) { Log.e("XX",
			 * "XXXXXXXXXXXXcondition satisfied"+nd.getTextContent()); } } }
			 */
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void updateFlag(String signalid, String flag, String M_type) {

		try {
			Log.e("lgg",
					"updateFlag(String signalid,String flag,String M_type)");

			Log.e("lgg", "came to update xml" + M_type);
			Log.e("lgg", "came to update xml" + signalid);
			Log.e("lgg", "came to update xml" + flag);
			/*
			 * NodeList childNodes = nodeGettingChanged.getChildNodes(); for
			 * (int i = 0; i != childNodes.getLength(); ++i) { Node child =
			 * childNodes.item(i); if (!(child instanceof Element)) continue;
			 * 
			 * if (child.getNodeName().equals("name"))
			 * child.getFirstChild().setNodeValue("SomethingElse") ; else if
			 * (child.getNodeName().equals("ip"))
			 * child.getFirstChild().setNodeValue("localHost") ; else if
			 * (child.getNodeName().equals("port"))
			 * child.getFirstChild().setNodeValue("4447") ; }
			 */

			NodeList nodelist = document.getElementsByTagName("mm");
			for (int i = 0; i < nodelist.getLength(); i++) {
				Node no = nodelist.item(i);
				Log.e("XX",
						"XXXXXXXXXXXXcondition satisfied1" + no.getNodeName());
				NodeList childs = no.getChildNodes();
				Log.e("XX",
						"XXXXXXXXXXXXcondition satisfied2" + childs.getLength());
				for (int idx = 0; idx < childs.getLength(); idx++) {
					Node childnode = childs.item(idx);
					Log.e("XX",
							"XXXXXXXXXXXXcondition satisfied2"
									+ childnode.getNodeName());
					if (childnode.getNodeName().equalsIgnoreCase(M_type)) {
						NodeList attributes = childnode.getChildNodes();
						for (int att = 0; att < attributes.getLength(); att++) {
							Node nvalues = attributes.item(att);
							if (nvalues.getNodeName().equalsIgnoreCase(
									"signalid")) {
								String id = nvalues.getTextContent();

								if (M_type.equals("txt")) {
									if (id.equals(signalid)) {
										for (int j = 0; j < attributes
												.getLength(); j++) {
											Node values = attributes.item(j);
											Log.e("XX",
													"XXXXXXXXXXXXcondition satisfied2"
															+ values);
											Log.e("XX",
													"XXXXXXXXXXXXcondition satisfied2"
															+ values.getNodeName());
											if (values.getNodeName()
													.equalsIgnoreCase("flag")) {
												Log.i("XXX",
														"################## condition satisfied");
												/*
												 * values.getFirstChild().
												 * setNodeValue(flag);
												 */
												values.setTextContent(flag);

												break;
												/*
												 * values.setNodeValue("ddsgdsg")
												 * ;
												 */
											}

										}
									}
								} else if (M_type.equals("img")) {
									if (id.equals(signalid)) {

										for (int j = 0; j < attributes
												.getLength(); j++) {
											Node values = attributes.item(j);
											Log.e("XX",
													"XXXXXXXXXXXXcondition satisfied2"
															+ values);
											Log.e("XX",
													"XXXXXXXXXXXXcondition satisfied2"
															+ values.getNodeName());
											if (values.getNodeName()
													.equalsIgnoreCase("flag")) {
												Log.i("XXX",
														"################## condition satisfied");
												/*
												 * values.getFirstChild().
												 * setNodeValue(flag);
												 */
												values.setTextContent(flag);

												break;
												/*
												 * values.setNodeValue("ddsgdsg")
												 * ;
												 */
											}

										}
									}

								} else if (M_type.equals("audio")) {
									if (id.equals(signalid)) {

										for (int j = 0; j < attributes
												.getLength(); j++) {
											Node values = attributes.item(j);
											Log.e("XX",
													"XXXXXXXXXXXXcondition satisfied2"
															+ values);
											Log.e("XX",
													"XXXXXXXXXXXXcondition satisfied2"
															+ values.getNodeName());
											if (values.getNodeName()
													.equalsIgnoreCase("flag")) {
												Log.i("XXX",
														"################## condition satisfied");
												/*
												 * values.getFirstChild().
												 * setNodeValue(flag);
												 */
												values.setTextContent(flag);
												break;
												/*
												 * values.setNodeValue("ddsgdsg")
												 * ;
												 */
											}

										}
									}

								} else if (M_type.equals("video")) {
									if (id.equals(signalid)) {
										for (int j = 0; j < attributes
												.getLength(); j++) {
											Node values = attributes.item(j);
											Log.e("XX",
													"XXXXXXXXXXXXcondition satisfied2"
															+ values);
											Log.e("XX",
													"XXXXXXXXXXXXcondition satisfied2"
															+ values.getNodeName());
											if (values.getNodeName()
													.equalsIgnoreCase("flag")) {
												Log.i("XXX",
														"################## condition satisfied");
												/*
												 * values.getFirstChild().
												 * setNodeValue(flag);
												 */
												values.setTextContent(flag);
												break;
												/*
												 * values.setNodeValue("ddsgdsg")
												 * ;
												 */
											}

										}
									}
								}

							}
						}
					}

				}
			}
			try {

				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(new File(fileName));
				transformer.transform(source, result);

				/*
				 * sfunction(M_type, signalid);
				 * writeIntoFile(document,fileName);
				 */
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * Node no=document.getLastChild(); Log.e("XX",
			 * "XXXXXXXXXXXXcondition satisfied"+no);
			 * if(no.getNodeType()==Node.ELEMENT_NODE) { Node
			 * ele=no.getLastChild();
			 * 
			 * Log.e("XX", "XXXXXXXXXXXXcondition satisfied"+no.getNodeName());
			 * Log.e("XX", "XXXXXXXXXXXXcondition satisfied"+ele.getNodeName());
			 * 
			 * NodeList list=ele.getChildNodes(); Log.e("XX",
			 * "XXXXXXXXXXXXcondition satisfied"+list.getLength()); for(int
			 * i=0;i<list.getLength();i++) { Node nd=list.item(i);
			 * if(nd.getNodeName().equalsIgnoreCase("msg")) { Log.e("XX",
			 * "XXXXXXXXXXXXcondition satisfied"+nd.getTextContent()); } } }
			 */
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public String composeChatHistoryXml(ChatBean chatBean) {
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
			e.printStackTrace();
		}
		return null;

	}

}
