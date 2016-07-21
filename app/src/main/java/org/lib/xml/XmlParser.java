package org.lib.xml;

import android.telecom.Call;
import android.util.Base64;
import android.util.Log;

import com.bean.BuddyPermission;
import com.bean.DefaultPermission;
import com.bean.EditForm;
import com.bean.EditFormBean;
import com.bean.FormFieldBean;
import com.bean.FormFieldSettingDeleteBean;
import com.bean.GroupChatBean;
import com.bean.GroupChatPermissionBean;
import com.bean.IndividualPermission;
import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.PatientDetailsBean;
import org.lib.model.AppVersionUpdateBean;
import org.lib.model.BuddyInformationBean;
import org.lib.model.ChattemplateModifieddate;
import org.lib.model.ConnectionBrokerBean;
import org.lib.model.FieldTemplateBean;
import org.lib.model.FileDetailsBean;
import org.lib.model.FindPeopleBean;
import org.lib.model.FlightDetails;
import org.lib.model.FormAttributeBean;
import org.lib.model.FormRecordsbean;
import org.lib.model.FormSettingBean;
import org.lib.model.FormsBean;
import org.lib.model.FormsListBean;
import org.lib.model.Formsinfocontainer;
import org.lib.model.FtpDetails;
import org.lib.model.GroupBean;
import org.lib.model.GroupMemberBean;
import org.lib.model.OfflineConfigResponseBean;
import org.lib.model.OfflineConfigurationBean;
import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.PatientCommentsBean;
import org.lib.model.PatientDescriptionBean;
import org.lib.model.PermissionBean;
import org.lib.model.ProfileTemplateBean;
import org.lib.model.RoleAccessBean;
import org.lib.model.RoleCommentsViewBean;
import org.lib.model.RoleEditRndFormBean;
import org.lib.model.RolePatientManagementBean;
import org.lib.model.RoleTaskMgtBean;
import org.lib.model.ShareReminder;
import org.lib.model.ShareResponseBean;
import org.lib.model.ShareSendBean;
import org.lib.model.SignalingBean;
import org.lib.model.Syncutilitybean;
import org.lib.model.TaskDetailsBean;
import org.lib.model.UdpMessageBean;
import org.lib.model.UpdateInstructor;
import org.lib.model.UtilityBean;
import org.lib.model.UtilityResponse;
import org.lib.model.VirtualBuddyBean;
import org.lib.model.WebServiceBean;
import org.lib.model.chattemplatebean;
import org.lib.webservice.WebServiceClient;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * This class is used to parse the XML.
 * 
 * 
 */
public class XmlParser {

	private DocumentBuilderFactory dbf = null;
	private DocumentBuilder db = null;
	private InputSource is = null;
	private Document doc = null;
	private NodeList list = null;
	private Node node = null;
	private Element element = null;
	private NamedNodeMap nodeMap = null;

	/**
	 * Parse the XML and returns the Signaling bean
	 * 
	 * @param xml
	 *            XML format String
	 * @return The signaling bean.
	 */
	public SignalingBean parseSignal(String xml) {
		SignalingBean sb = new SignalingBean();
		try {
			Log.d("XML_PARSER", "SignalBean Parser : " + xml);
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("tl");
			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("sessionid") != null) {
				sb.setSessionid((nodeMap.getNamedItem("sessionid")
						.getNodeValue()));
				Log.d("BL", "Session....." + sb.getSessionid());
			}
			if (nodeMap.getNamedItem("from") != null) {
				sb.setFrom(nodeMap.getNamedItem("from").getNodeValue());
			}
			if (nodeMap.getNamedItem("to") != null) {
				sb.setTo(nodeMap.getNamedItem("to").getNodeValue());
			}
			if (nodeMap.getNamedItem("audioSSRC") != null) {
				sb.setAudiossrc(nodeMap.getNamedItem("audioSSRC")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("videoSSRC") != null) {
				sb.setVideossrc(nodeMap.getNamedItem("videoSSRC")
						.getNodeValue());
			}

			if (nodeMap.getNamedItem("localip") != null) {
				sb.setLocalip(nodeMap.getNamedItem("localip").getNodeValue());
			}
			if (nodeMap.getNamedItem("publicip") != null) {
				sb.setPublicip(nodeMap.getNamedItem("publicip").getNodeValue());
			}

			if (nodeMap.getNamedItem("tolocalip") != null) {
				sb.setTolocalip(nodeMap.getNamedItem("tolocalip")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("topublicip") != null) {
				sb.setTopublicip(nodeMap.getNamedItem("topublicip")
						.getNodeValue());
			}

			if (nodeMap.getNamedItem("calltype") != null) {
				sb.setCallType(nodeMap.getNamedItem("calltype").getNodeValue());
			}

			if (nodeMap.getNamedItem("ftpusername") != null) {
				Log.d("MM", ".........................Condition Satisfied");
				sb.setFtpUser(nodeMap.getNamedItem("ftpusername")
						.getNodeValue());
				Log.d("MM", "........................." + sb.getFtpUser());
			}
			if (nodeMap.getNamedItem("ftppassword") != null) {
				sb.setFtppassword(nodeMap.getNamedItem("ftppassword")
						.getNodeValue());
				Log.d("MM", "........................." + sb.getFtppassword());
			}

			if (nodeMap.getNamedItem("filename") != null) {
				Log.d("XML", "??????????????"
						+ nodeMap.getNamedItem("filename").getNodeValue());
				sb.setFilePath(nodeMap.getNamedItem("filename").getNodeValue());
				Log.d("MM", "........................." + sb.getFilePath());
			}

			if (nodeMap.getNamedItem("connectip") != null) {
				sb.setBuddyConnectip(nodeMap.getNamedItem("connectip")
						.getNodeValue());
			}

			if (nodeMap.getNamedItem("connectport") != null) {
				sb.setBuddyConnectport(nodeMap.getNamedItem("connectport")
						.getNodeValue());
			}

			if (nodeMap.getNamedItem("robo") != null) {
				sb.setisRobo(nodeMap.getNamedItem("robo").getNodeValue());
			}

			if (nodeMap.getNamedItem("punchingmode") != null) {
				sb.setPunchingmode(nodeMap.getNamedItem("punchingmode")
						.getNodeValue());
			}

			if (nodeMap.getNamedItem("signalid") != null) {
				sb.setSignalid(nodeMap.getNamedItem("signalid").getNodeValue());
			}

			if (nodeMap.getNamedItem("message") != null) {
				sb.setMessage(nodeMap.getNamedItem("message").getNodeValue());
			}

			Log.d("Conf", "##################going to parse confrerence member");
			if (nodeMap.getNamedItem("conferencemember") != null) {
				Log.d("Conf", "##################not null");
				sb.setConferencemember(nodeMap.getNamedItem("conferencemember")
						.getNodeValue());
				Log.d("Conf",
						"##################not null" + sb.getConferencemember());
			}

			if (nodeMap.getNamedItem("host") != null) {
				sb.setHost(nodeMap.getNamedItem("host")
						.getNodeValue());
			}

			if (nodeMap.getNamedItem("participants") != null) {
				sb.setParticipants(nodeMap.getNamedItem("participants")
						.getNodeValue());
			}

			if (nodeMap.getNamedItem("joincall") != null) {
				sb.setJoincall(nodeMap.getNamedItem("joincall")
						.getNodeValue());
			}

			if(nodeMap.getNamedItem("videoPromotion") != null) {
				sb.setVideopromote(nodeMap.getNamedItem("videoPromotion")
						.getNodeValue());
			}

			if(nodeMap.getNamedItem("myVideoStoped") != null) {
				sb.setVideoStoped(nodeMap.getNamedItem("myVideoStoped")
						.getNodeValue());
			}

			if (nodeMap.getNamedItem("signalport") != null) {
				sb.setSignalport(nodeMap.getNamedItem("signalport")
						.getNodeValue());
			}

			if (nodeMap.getNamedItem("gmember") != null) {
				sb.setGmember(nodeMap.getNamedItem("gmember").getNodeValue());
			}
			if (nodeMap.getNamedItem("rdpusername") != null) {
				sb.setRdpUsername(nodeMap.getNamedItem("rdpusername")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("rdppassword") != null) {
				sb.setRdpPassword(nodeMap.getNamedItem("rdppassword")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("rdpport") != null) {
				sb.setRdpPort(nodeMap.getNamedItem("rdpport").getNodeValue());
			}

			if (nodeMap.getNamedItem("action") != null) {
				parseAction(sb, nodeMap.getNamedItem("action").getNodeValue());

			}

			if (nodeMap.getNamedItem("seqno") != null) {
				sb.setSeqNo(nodeMap.getNamedItem("seqno").getNodeValue());
			}
			if (nodeMap.getNamedItem("ssrc") != null) {
				sb.setSsrc(nodeMap.getNamedItem("ssrc").getNodeValue());
			}
			if (nodeMap.getNamedItem("robokey") != null) {
				sb.setisrobokey(nodeMap.getNamedItem("robokey").getNodeValue());
			}

			if (nodeMap.getNamedItem("callsubtype") != null) {
				sb.setCallSubType(nodeMap.getNamedItem("callsubtype")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("callChatId") != null) {
				String chatid=nodeMap.getNamedItem("callChatId")
						.getNodeValue();
				if(chatid.contains(CallDispatcher.LoginUser)) {
					String temp = chatid.replace(CallDispatcher.LoginUser, "");
					sb.setChatid(temp);
				}else
					sb.setChatid(chatid);
			}
			/*
			 * 04-19 16:46:43.940: D/XML_PARSER(7285): SignalBean Parser : <?xml
			 * version="1.0"?><com result="0" type="0"><tl from="amuthanind"
			 * to="amuthanbus" localip="192.168.10.67" publicip="122.165.92.38"
			 * calltype="AC" sessionid="c3e9f568292c54d199edf2a6e00bfe35"
			 * signalid="26675990" signalport="53682" callsubtype="1"
			 * conferencemember="" message="" fileid="" bs_calltype="1"
			 * bs_callCategory="1" bs_callstatus=""
			 * bs_parentid="1429442179781"/></com>
			 */

			if (nodeMap.getNamedItem("bs_calltype") != null) {
				sb.setBs_calltype(nodeMap.getNamedItem("bs_calltype")
						.getNodeValue());
			}

			if (nodeMap.getNamedItem("bs_callCategory") != null) {
				sb.setBs_callCategory(nodeMap.getNamedItem("bs_callCategory")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("bs_callstatus") != null) {
				sb.setBs_callstatus(nodeMap.getNamedItem("bs_callstatus")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("bs_parentid") != null) {
				sb.setBs_parentid(nodeMap.getNamedItem("bs_parentid")
						.getNodeValue());
			}

			Log.d("SIGNAL", "666 get  " + sb.getUrl());

			String result[] = getTypeResult(xml);
			sb.setResult(result[0]);
			sb.setType(result[1]);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return sb;

	}

	void parseAction(SignalingBean sb, String text) {
		try {

			Log.d("SIGNAL", "222  " + text);
			text = UnescapeXML(text);
			Log.d("SIGNAL", "3333  " + text);
			// text="<?xml version="1.0"?><execute><action type="1" command="http://www.youtube.com/watch?v=LGoeujLAP34"/> </execute>"";
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(text));
			Document doc = (Document) db.parse(is);
			NodeList list = doc.getElementsByTagName("action");
			Node node = list.item(0);
			NamedNodeMap nodeMap = node.getAttributes();

			for (int i = 0; i < list.getLength(); i++) {
				Log.d("SIGNAL", "Insideeeeeeee");
			}

			if (nodeMap.getNamedItem("type") != null) {

				Log.d("SIGNAL", "22222222222 parseAction2 type "
						+ nodeMap.getNamedItem("command").getNodeValue());

				if (nodeMap.getNamedItem("type").getNodeValue().equals("1")) {
					if (nodeMap.getNamedItem("command") != null) {
						Log.d("SIGNAL",
								"555 set  "
										+ nodeMap.getNamedItem("command")
												.getNodeValue());
						sb.setUrl(nodeMap.getNamedItem("command")
								.getNodeValue());
					}
				}
				if (nodeMap.getNamedItem("type").getNodeValue().equals("2")) {
					if (nodeMap.getNamedItem("command") != null) {
						Log.d("SIGNAL",
								"555 set  "
										+ nodeMap.getNamedItem("command")
												.getNodeValue());
						// sb.setUrl(nodeMap.getNamedItem("command").getNodeValue());
						String[] datas = nodeMap.getNamedItem("command")
								.getNodeValue().split(",");
						sb.setForwardingDetails(datas);
					}

				}

			}
			// if(nodeMap.getNamedItem("command")!=null)
			// {
			// Log.d("SIGNAL", "22222222222 parseAction2 commannd");
			//
			//
			//
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// String decodeReceivedXml(String input)
	// {
	// // StringEscapeUtil.unescapeXml(xml)
	//
	// return "";
	// }
	// public static String unescapeXML( final String xml )
	// {
	// Pattern xmlEntityRegex = Pattern.compile( "&(#?)([^;]+);" );
	// //Unfortunately, Matcher requires a StringBuffer instead of a
	// StringBuilder
	// StringBuffer unescapedOutput = new StringBuffer( xml.length() );
	//
	// Matcher m = xmlEntityRegex.matcher( xml );
	// Map<String,String> builtinEntities = null;
	// String entity;
	// String hashmark;
	// String ent;
	// int code;
	// while ( m.find() ) {
	// ent = m.group(2);
	// hashmark = m.group(1);
	// if ( (hashmark != null) && (hashmark.length() > 0) ) {
	// code = Integer.parseInt( ent );
	// entity = Character.toString( (char) code );
	// } else {
	// //must be a non-numerical entity
	// if ( builtinEntities == null ) {
	// builtinEntities = buildBuiltinXMLEntityMap();
	// }
	// entity = builtinEntities.get( ent );
	// if ( entity == null ) {
	// //not a known entity - ignore it
	// entity = "&" + ent + ';';
	// }
	// }
	// m.appendReplacement( unescapedOutput, entity );
	// }
	// m.appendTail( unescapedOutput );
	//
	// return unescapedOutput.toString();
	// }

	public ArrayList parseCallhistoryResponse(String xml) {
		WebServiceBean serviceBean = new WebServiceBean();
		ArrayList<String> addBuddyList = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("transaction");
			addBuddyList = new ArrayList<String>();
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("sessid") != null) {
					addBuddyList.add(nodeMap.getNamedItem("sessid")
							.getNodeValue());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return addBuddyList;

	}

	public String UnescapeXML(String s)

	{

		String returnString = s;

		returnString = returnString.replace("&apos;", "'");

		returnString = returnString.replace("&quot;", "\"");

		returnString = returnString.replace("&gt;", ">");

		returnString = returnString.replace("&lt;", "<");

		returnString = returnString.replace("&amp;", "&");

		return returnString;

	}

	private static Map<String, String> buildBuiltinXMLEntityMap() {
		Map<String, String> entities = new HashMap<String, String>(10);
		entities.put("lt", "<");
		entities.put("gt", ">");
		entities.put("amp", "&");
		entities.put("apos", "'");
		entities.put("quot", "\"");
		return entities;
	}

	/**
	 * Parse the XML and returns the WebServiceBean bean
	 * 
	 * @param xml
	 *            XML format String
	 * @return The WebServiceBean bean.
	 */
	public WebServiceBean parseResultFromXml(String xml) {
		WebServiceBean serviceBean = new WebServiceBean();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("text");
			node = list.item(0);
			nodeMap = node.getAttributes();
			if(nodeMap.getNamedItem("text").getNodeValue()!=null) {
				Log.d("zxz", "received lo "
						+ nodeMap.getNamedItem("text").getNodeValue());
				Log.i("xml", "Response xml"
						+ nodeMap.getNamedItem("text").getNodeValue());

				serviceBean.setText(nodeMap.getNamedItem("text").getNodeValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("xml", "Response xml" + e.getMessage());

			Log.d("zxz", "received lo Exception .class..............");
		}
		return serviceBean;

	}
	public String parseResultXml(String xml) {
		String result=new String();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("text");
			node = list.item(0);
			nodeMap = node.getAttributes();
			Log.d("zxz", "received lo "
					+ nodeMap.getNamedItem("text").getNodeValue());
			Log.i("xml", "Response xml"
					+ nodeMap.getNamedItem("text").getNodeValue());

			if (nodeMap.getNamedItem("text") != null) 
				result = nodeMap.getNamedItem("text").getNodeValue();
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("xml", "Response xml" + e.getMessage());

			Log.d("zxz", "received lo Exception .class..............");
		}
		return result;

	}

	/**
	 * Parse the XML and returns the WebServiceBean bean
	 * 
	 * @param xml
	 *            XML format String
	 * @return The WebServiceBean bean.
	 */
	public WebServiceBean parseSubscribe(String xml) {

		Log.i("welcome", "coming to parsing xml");
		WebServiceBean serviceBean = new WebServiceBean();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("text");
			node = list.item(0);
			nodeMap = node.getAttributes();
			serviceBean.setText(nodeMap.getNamedItem("text").getNodeValue());
			if (getResult(xml)) {
				serviceBean.setResult("1");
			} else {
				serviceBean.setResult("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceBean;
	}

	public UpdateInstructor ParseUpdateTag(String xml) {
		UpdateInstructor instructor = new UpdateInstructor();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);

			list = doc.getElementsByTagName("update");
			for (int i = 0; i < list.getLength(); i++) {

				node = list.item(i);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("type") != null) {
					instructor.setType(nodeMap.getNamedItem("type")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("message") != null) {
					instructor.setMessage(nodeMap.getNamedItem("message")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("url") != null) {
					instructor.setUrl(nodeMap.getNamedItem("url")
							.getNodeValue());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instructor;
	}

	/**
	 * Parse the XML and returns the ArrayList.The ArrayList consist of signIn
	 * Response messages.
	 *
	 * @return ArrayList of Type Object
	 * 
	 */




	public FtpDetails ParseFtpDetails(NamedNodeMap nodeMap) {
		Log.i("forms", "inside signin parsing");
		FtpDetails frmrbean = new FtpDetails();
		try {

			if (nodeMap.getNamedItem("ftpusername") != null) {
				Log.i("forms",
						"inside signin parsing"
								+ nodeMap.getNamedItem("ftpusername")
										.getNodeValue());
				frmrbean.setFtpusername(nodeMap.getNamedItem("ftpusername")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("ftppassword") != null) {
				Log.i("forms",
						"inside signin parsing"
								+ nodeMap.getNamedItem("ftppassword")
										.getNodeValue());

				frmrbean.setftppassword(nodeMap.getNamedItem("ftppassword")
						.getNodeValue());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return frmrbean;

	}

	public FormsListBean ParseSigninForms(NamedNodeMap nodeMap) {
		Log.i("forms", "inside signin parsing");
		FormsListBean frmrbean = new FormsListBean();
		try {
			if (nodeMap.getNamedItem("userid") != null) {
				frmrbean.setForm_ownser(nodeMap.getNamedItem("userid")
						.getNodeValue());
				Log.i("b", "owner name"
						+ nodeMap.getNamedItem("userid").getNodeValue()
						.toString());
			}
			if (nodeMap.getNamedItem("formId") != null) {
				frmrbean.setForm_id(nodeMap.getNamedItem("formId")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("formname") != null) {
				frmrbean.setForm_name(nodeMap.getNamedItem("formname")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("rows") != null) {
				frmrbean.setnumberof_rows(nodeMap.getNamedItem("rows")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("formtime") != null) {
				Log.i("forms",
						"inside Owner parsing"
								+ nodeMap.getNamedItem("formtime")
										.getNodeValue());

				frmrbean.setForm_time(nodeMap.getNamedItem("formtime")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("formicon") != null) {
				Log.i("forms",
						"inside signin parsing"
								+ nodeMap.getNamedItem("formicon")
										.getNodeValue());
				frmrbean.setFormicon(nodeMap.getNamedItem("formicon")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("formdescription") != null) {
				Log.i("forms",
						"inside signin parsing"
								+ nodeMap.getNamedItem("formdescription")
										.getNodeValue());

				frmrbean.setFormdescription(nodeMap.getNamedItem(
						"formdescription").getNodeValue());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return frmrbean;

	}

	public FormSettingBean ParseSettingforms(NamedNodeMap nodeMap) {
		Log.i("settings", "inside settind parsing");
		FormSettingBean frmrbean = new FormSettingBean();
		try {
			if (nodeMap.getNamedItem("fsid") != null) {
				Log.i("settings", " fsid "
						+ nodeMap.getNamedItem("fsid").getNodeValue());

				frmrbean.setFsid(nodeMap.getNamedItem("fsid").getNodeValue());
			}
			if (nodeMap.getNamedItem("buddy") != null) {
				Log.i("settings", " buddy "
						+ nodeMap.getNamedItem("buddy").getNodeValue());

				frmrbean.setBuddy(nodeMap.getNamedItem("buddy").getNodeValue());
			}
			if (nodeMap.getNamedItem("permissionid") != null) {
				Log.i("settings",
						" permissionid "
								+ nodeMap.getNamedItem("permissionid")
										.getNodeValue());

				frmrbean.setPermissionid(nodeMap.getNamedItem("permissionid")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("syncid") != null) {
				Log.i("settings", " syncid "
						+ nodeMap.getNamedItem("syncid").getNodeValue());

				frmrbean.setsyncid(nodeMap.getNamedItem("syncid")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("syncquery") != null) {
				Log.i("settings",
						" syncquery "
								+ nodeMap.getNamedItem("syncquery")
										.getNodeValue());

				frmrbean.setSyncquery(nodeMap.getNamedItem("syncquery")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("createddate") != null) {
				Log.i("settings",
						" createddate "
								+ nodeMap.getNamedItem("createddate")
										.getNodeValue());

				frmrbean.setcreateddate(nodeMap.getNamedItem("createddate")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("modifieddate") != null) {
				Log.i("settings",
						" createddate "
								+ nodeMap.getNamedItem("modifieddate")
										.getNodeValue());

				frmrbean.setmodifieddate(nodeMap.getNamedItem("modifieddate")
						.getNodeValue());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return frmrbean;

	}

	public FormAttributeBean parseFormAttributes(NamedNodeMap nodemap,
			String tablename) {
		FormAttributeBean attributes = new FormAttributeBean();

		try {

			attributes.setTableName(tablename);

			if (nodemap.getNamedItem("formid") != null)
				attributes.setformid(nodemap.getNamedItem("formid")
						.getNodeValue().trim());

			if (nodemap.getNamedItem("fatid") != null)
				attributes.setAttributeid(nodemap.getNamedItem("fatid")
						.getNodeValue().trim());

			if (nodemap.getNamedItem("fieldname") != null)
				attributes.setfieldname(nodemap.getNamedItem("fieldname")
						.getNodeValue().trim());

			if (nodemap.getNamedItem("dataentrymode") != null)
				attributes.setDataEntry(nodemap.getNamedItem("dataentrymode")
						.getNodeValue().trim());

			if (nodemap.getNamedItem("datavalidation") != null)
				attributes.setDataValidation(nodemap
						.getNamedItem("datavalidation").getNodeValue().trim());

			if (nodemap.getNamedItem("defaultvalue") != null)
				attributes.setDefaultvalue(nodemap.getNamedItem("defaultvalue")
						.getNodeValue().trim());

			if (nodemap.getNamedItem("instruction") != null)
				attributes.setInstruction(nodemap.getNamedItem("instruction")
						.getNodeValue().trim());

			if (nodemap.getNamedItem("errortip") != null)
				attributes.setErrortip(nodemap.getNamedItem("errortip")
						.getNodeValue().trim());

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attributes;
	}

	public FormsListBean ParseOwninForms(NamedNodeMap nodeMap) {
		Log.i("forms", "inside signin parsing");
		FormsListBean frmrbean = new FormsListBean();
		try {
			frmrbean.setMode("yes");

			if (nodeMap.getNamedItem("userid") != null) {
				frmrbean.setForm_ownser(nodeMap.getNamedItem("userid")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("formId") != null) {
				frmrbean.setForm_id(nodeMap.getNamedItem("formId")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("formname") != null) {
				frmrbean.setForm_name(nodeMap.getNamedItem("formname")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("rows") != null) {
				frmrbean.setnumberof_rows(nodeMap.getNamedItem("rows")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("formtime") != null) {
				Log.i("forms",
						"inside Owner parsing"
								+ nodeMap.getNamedItem("formtime")
										.getNodeValue());

				frmrbean.setForm_time(nodeMap.getNamedItem("formtime")
						.getNodeValue());
			}

			if (nodeMap.getNamedItem("formicon") != null) {
				Log.i("forms",
						"inside signin parsing"
								+ nodeMap.getNamedItem("formicon")
										.getNodeValue());
				frmrbean.setFormicon(nodeMap.getNamedItem("formicon")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("formdescription") != null) {
				Log.i("forms",
						"inside signin parsing"
								+ nodeMap.getNamedItem("formdescription")
										.getNodeValue());

				frmrbean.setFormdescription(nodeMap.getNamedItem(
						"formdescription").getNodeValue());
			}
			if (nodeMap.getNamedItem("containsattributes") != null) {
				Log.i("forms",
						"inside Owner parsing"
								+ nodeMap.getNamedItem("containsattributes")
										.getNodeValue());

				frmrbean.setAttribute(nodeMap
						.getNamedItem("containsattributes").getNodeValue()
						.toString());
			} else {
				frmrbean.setAttribute("no");

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return frmrbean;

	}

	public FormsListBean ParseftpDetail(NamedNodeMap nodeMap) {
		Log.i("forms", "inside signin parsing");
		FormsListBean frmrbean = new FormsListBean();
		try {

			if (nodeMap.getNamedItem("ftpusername") != null) {
				Log.i("forms",
						"inside signin parsing"
								+ nodeMap.getNamedItem("ftpusername")
										.getNodeValue());
				frmrbean.setftpusername(nodeMap.getNamedItem("ftpusername")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("ftppassword") != null) {
				Log.i("forms",
						"inside signin parsing"
								+ nodeMap.getNamedItem("ftppassword")
										.getNodeValue());

				frmrbean.setftppassword(nodeMap.getNamedItem("ftppassword")
						.getNodeValue());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return frmrbean;

	}

	public ArrayList<String[]> parseCreateFormResult(String xml) {
		Log.i("xml", "Response xml" + xml);
		ArrayList<String[]> results = new ArrayList<String[]>();

		String[] result = null;

		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("UserForm");
			for (int i = 0; i < list.getLength(); i++) {

				node = list.item(0);
				nodeMap = node.getAttributes();
				result = new String[5];
				if (nodeMap.getNamedItem("formid") != null) {
					result[0] = nodeMap.getNamedItem("formid").getNodeValue();
				}
				if (nodeMap.getNamedItem("formname") != null) {
					result[1] = nodeMap.getNamedItem("formname").getNodeValue();
				}
				if (nodeMap.getNamedItem("text") != null) {
					result[2] = nodeMap.getNamedItem("text").getNodeValue();
				}
				if (nodeMap.getNamedItem("formtime") != null) {
					result[3] = nodeMap.getNamedItem("formtime").getNodeValue();
				}
				if (nodeMap.getNamedItem("fsid") != null) {
					result[4] = nodeMap.getNamedItem("fsid").getNodeValue();
				}
				results.add(result);
			}

			list = doc.getElementsByTagName("UserAttributeForm");
			for (int i = 0; i < list.getLength(); i++) {

				node = list.item(i);
				nodeMap = node.getAttributes();
				result = new String[3];

				if (nodeMap.getNamedItem("formid") != null) {
					result[0] = nodeMap.getNamedItem("formid").getNodeValue();
					Log.i("setting", result[0]);
				}
				// if (nodeMap.getNamedItem("formname") != null) {
				// result[1] = nodeMap.getNamedItem("formname").getNodeValue();
				// Log.i("setting", result[1]);
				//
				// }
				if (nodeMap.getNamedItem("columnname") != null) {
					result[1] = nodeMap.getNamedItem("columnname")
							.getNodeValue();
					Log.i("setting", result[1]);

				}
				if (nodeMap.getNamedItem("attributeid") != null) {
					result[2] = nodeMap.getNamedItem("attributeid")
							.getNodeValue();
					Log.i("setting", result[2]);

				}
				results.add(result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return results;
	}

	public String[] parsesettingAttributeFormResult(String xml) {
		Log.i("xml", "Response xml" + xml);
		String[] result = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("UserAttributeForm");

			node = list.item(0);
			nodeMap = node.getAttributes();

			result = new String[4];

			if (nodeMap.getNamedItem("formid") != null) {
				result[0] = nodeMap.getNamedItem("formid").getNodeValue();
				Log.i("setting", result[0]);
			}
			if (nodeMap.getNamedItem("formname") != null) {
				result[1] = nodeMap.getNamedItem("formname").getNodeValue();
				Log.i("setting", result[1]);

			}
			if (nodeMap.getNamedItem("columnname") != null) {
				result[2] = nodeMap.getNamedItem("columnname").getNodeValue();
				Log.i("setting", result[2]);

			}
			if (nodeMap.getNamedItem("attributeid") != null) {
				result[3] = nodeMap.getNamedItem("attributeid").getNodeValue();
				Log.i("setting", result[3]);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	public String[] parsegetAttributeFormResult(String xml) {
		Log.i("xml", "Response xml" + xml);
		String[] result = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("FormSettings");

			node = list.item(0);
			nodeMap = node.getAttributes();

			result = new String[9];

			if (nodeMap.getNamedItem("buddy") != null) {
				result[0] = nodeMap.getNamedItem("buddy").getNodeValue();
				Log.i("setting", result[0]);
			}
			if (nodeMap.getNamedItem("fsid") != null) {
				result[1] = nodeMap.getNamedItem("fsid").getNodeValue();
				Log.i("setting", result[1]);

			}
			if (nodeMap.getNamedItem("formid") != null) {
				result[2] = nodeMap.getNamedItem("formid").getNodeValue();
				Log.i("setting", result[2]);

			}
			if (nodeMap.getNamedItem("createddate") != null) {
				result[3] = nodeMap.getNamedItem("createddate").getNodeValue();
				Log.i("setting", result[3]);

			}
			if (nodeMap.getNamedItem("modifieddate") != null) {
				result[4] = nodeMap.getNamedItem("modifieddate").getNodeValue();
				Log.i("setting", result[4]);

			}
			if (nodeMap.getNamedItem("syncquery") != null) {
				result[5] = nodeMap.getNamedItem("syncquery").getNodeValue();
				Log.i("setting", result[5]);

			}
			if (nodeMap.getNamedItem("permissionid") != null) {
				result[6] = nodeMap.getNamedItem("permissionid").getNodeValue();
				Log.i("setting", result[6]);

			}
			if (nodeMap.getNamedItem("syncid") != null) {
				result[7] = nodeMap.getNamedItem("syncid").getNodeValue();
				Log.i("setting", result[7]);

			}
			if (nodeMap.getNamedItem("owener") != null) {
				result[8] = nodeMap.getNamedItem("owener").getNodeValue();
				Log.i("setting", "owner" + result[8]);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	public Vector<Object> parsesettingAccessFormResult(String xml) {
		Log.i("xml", "Response xml" + xml);
		Vector<Object> formSettingsList = new Vector<Object>();
		String[] result = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("FormSettings");

			node = list.item(0);
			nodeMap = node.getAttributes();

			result = new String[9];

			if (nodeMap.getNamedItem("buddy") != null) {
				result[0] = nodeMap.getNamedItem("buddy").getNodeValue();
				Log.i("setting", result[0]);
			}
			if (nodeMap.getNamedItem("fsid") != null) {
				result[1] = nodeMap.getNamedItem("fsid").getNodeValue();
				Log.i("setting", result[1]);

			}
			if (nodeMap.getNamedItem("formid") != null) {
				result[2] = nodeMap.getNamedItem("formid").getNodeValue();
				Log.i("setting", result[2]);

			}
			if (nodeMap.getNamedItem("createddate") != null) {
				result[3] = nodeMap.getNamedItem("createddate").getNodeValue();
				Log.i("setting", result[3]);

			}
			if (nodeMap.getNamedItem("modifieddate") != null) {
				result[4] = nodeMap.getNamedItem("modifieddate").getNodeValue();
				Log.i("setting", result[4]);

			}
			if (nodeMap.getNamedItem("syncquery") != null) {
				result[5] = nodeMap.getNamedItem("syncquery").getNodeValue();
				Log.i("setting", result[5]);

			}
			if (nodeMap.getNamedItem("permissionid") != null) {
				result[6] = nodeMap.getNamedItem("permissionid").getNodeValue();
				Log.i("setting", result[6]);

			}
			if (nodeMap.getNamedItem("syncid") != null) {
				result[7] = nodeMap.getNamedItem("syncid").getNodeValue();
				Log.i("setting", result[7]);

			}
			if (nodeMap.getNamedItem("owner") != null) {
				result[8] = nodeMap.getNamedItem("owner").getNodeValue();
				Log.i("setting", "owner" + result[8]);

			}
			formSettingsList.add(result);
			Vector<Vector<FormFieldBean>> fieldAccessList = parseFormFieldSettings(xml);
			if (fieldAccessList != null && fieldAccessList.size() > 0) {
				formSettingsList.add(fieldAccessList);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return formSettingsList;
	}

	public String[] parseAccessFormResult(String xml) {
		Log.i("xml", "Response xml" + xml);
		String[] result = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("FormSettings");

			node = list.item(0);
			nodeMap = node.getAttributes();

			result = new String[6];

			if (nodeMap.getNamedItem("buddy") != null) {
				result[0] = nodeMap.getNamedItem("buddy").getNodeValue();
			}
			if (nodeMap.getNamedItem("fsid") != null) {
				result[1] = nodeMap.getNamedItem("fsid").getNodeValue();
			}
			if (nodeMap.getNamedItem("formid") != null) {
				result[2] = nodeMap.getNamedItem("formid").getNodeValue();
			}
			if (nodeMap.getNamedItem("createdtime") != null) {
				result[3] = nodeMap.getNamedItem("createdtime").getNodeValue();
			}
			if (nodeMap.getNamedItem("editedtime") != null) {
				result[4] = nodeMap.getNamedItem("editedtime").getNodeValue();
			}
			if (nodeMap.getNamedItem("mode") != null) {
				result[5] = nodeMap.getNamedItem("mode").getNodeValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<String[]> parseMultipleAccessForm(String xml) {

		Log.i("xml", "Response xml" + xml);
		ArrayList<String[]> accessList = new ArrayList<String[]>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("FormSettings");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				String[] result = new String[6];

				if (nodeMap.getNamedItem("buddy") != null) {
					result[0] = nodeMap.getNamedItem("buddy").getNodeValue();
				}
				if (nodeMap.getNamedItem("fsid") != null) {
					result[1] = nodeMap.getNamedItem("fsid").getNodeValue();
				}
				if (nodeMap.getNamedItem("formid") != null) {
					result[2] = nodeMap.getNamedItem("formid").getNodeValue();
				}
				if (nodeMap.getNamedItem("createdtime") != null) {
					result[3] = nodeMap.getNamedItem("createdtime")
							.getNodeValue();
				}
				if (nodeMap.getNamedItem("editedtime") != null) {
					result[4] = nodeMap.getNamedItem("editedtime")
							.getNodeValue();
				}
				if (nodeMap.getNamedItem("mode") != null) {
					result[5] = nodeMap.getNamedItem("mode").getNodeValue();
				}
				accessList.add(result);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return accessList;

	}

	public String[] parseDeleteAccessFormResult(String xml) {
		Log.i("xml", "Response xml" + xml);
		String[] result = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("FormSettings");

			node = list.item(0);
			nodeMap = node.getAttributes();

			result = new String[1];

			if (nodeMap.getNamedItem("fsid") != null) {
				result[0] = nodeMap.getNamedItem("fsid").getNodeValue();

				Log.i("settings", "%%%%%%%%%%%%%%" + result[0]);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	public String[] parseAddFormXml(String xml) {

		Log.i("xml", "Response xml" + xml);
		String[] result = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("UserDataForm");
			node = list.item(0);
			nodeMap = node.getAttributes();
			result = new String[4];
			if (nodeMap.getNamedItem("tableid") != null) {
				result[0] = nodeMap.getNamedItem("tableid").getNodeValue();

				Log.i("records", "************" + result[0]);
			}
			if (nodeMap.getNamedItem("text") != null) {
				result[1] = nodeMap.getNamedItem("text").getNodeValue();
			}
			if (nodeMap.getNamedItem("formtime") != null) {
				result[2] = nodeMap.getNamedItem("formtime").getNodeValue();
			}
			if (nodeMap.getNamedItem("formid") != null) {
				result[3] = nodeMap.getNamedItem("formid").getNodeValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("zxz", "received lo Exception .class..............");
		}
		return result;

	}

	public String[] parseEditFormXml(String xml) {

		Log.i("xml", "Response xml" + xml);
		String result[] = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("FormRowEdited");
			node = list.item(0);
			nodeMap = node.getAttributes();
			result = new String[1];
			if (nodeMap.getNamedItem("tableid") != null) {
				result[0] = nodeMap.getNamedItem("tableid").getNodeValue();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("zxz", "received lo Exception .class..............");
		}
		return result;

	}

	public FormsBean parseDeleteFormRecordResult(String xml) {
		FormsBean fbean = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("FormRowDeleted");
			node = list.item(0);
			nodeMap = node.getAttributes();
			fbean = new FormsBean();

			if (nodeMap.getNamedItem("ownerid") != null) {
				fbean.setuserName(nodeMap.getNamedItem("ownerid")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("deleteuserid") != null) {
				fbean.setDeletedUserName(nodeMap.getNamedItem("deleteuserid")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("formid") != null) {
				fbean.setFormId(nodeMap.getNamedItem("formid").getNodeValue());
			}
			if (nodeMap.getNamedItem("formname") != null) {
				fbean.setFormName(nodeMap.getNamedItem("formname")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("tableid") != null) {
				fbean.setDeletedrecordId(nodeMap.getNamedItem("tableid")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("rows") != null) {
				fbean.setCount(nodeMap.getNamedItem("rows").getNodeValue());
			}

			fbean.setIsDeletedRecord(true);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return fbean;
	}

	@SuppressWarnings("finally")
	public Formsinfocontainer parseGetFormResult(String xml) {

		Formsinfocontainer formsinfocontainer = new Formsinfocontainer();
		ArrayList<FormRecordsbean> record_infolist = new ArrayList<FormRecordsbean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);

			list = doc.getElementsByTagName("UserDataForm");
			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("formId") != null)
				formsinfocontainer.setForm_id(nodeMap.getNamedItem("formId")
						.getNodeValue());

			if (nodeMap.getNamedItem("formName") != null)
				formsinfocontainer.setForm_name(nodeMap
						.getNamedItem("formName").getNodeValue());

			if (nodeMap.getNamedItem("fieldsName") != null)
				formsinfocontainer.setForm_fields(nodeMap.getNamedItem(
						"fieldsName").getNodeValue());

			formsinfocontainer.setForm_fields(formsinfocontainer
					.getForm_fields()
					+ ",recorddate:VARCHAR(45),tableid:VARCHAR(45)");

			list = null;
			node = null;
			nodeMap = null;

			list = doc.getElementsByTagName("Data");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				NodeList childs = node.getChildNodes();
				if (childs != null) {
					FormRecordsbean recordsbean = new FormRecordsbean();
					HashMap<String, String> field_valueset = new HashMap<String, String>();
					for (int j = 0; j < childs.getLength(); j++) {
						Node child_node = childs.item(j);
						if (child_node != null) {
							NamedNodeMap child_attributes = child_node
									.getAttributes();
							if (child_attributes != null) {
								if (child_attributes.getNamedItem("fieldname") != null
										&& child_attributes
												.getNamedItem("value") != null) {
									String field_name = child_attributes
											.getNamedItem("fieldname")
											.getNodeValue();
									String field_value = child_attributes
											.getNamedItem("value")
											.getNodeValue();

									if (field_name != null
											&& field_value != null) {
										if (!field_name
												.equalsIgnoreCase("flag"))
											field_valueset.put(field_name,
													field_value);
										else if (field_name
												.equalsIgnoreCase("flag")
												&& field_value.equals("1"))
											recordsbean
													.setIsDeleted_record(true);
									}

								}
							}
						}
						if (j == childs.getLength() - 1) {
							recordsbean.setRecords_info(field_valueset);
							record_infolist.add(recordsbean);
						}

					}
				}
			}
			formsinfocontainer.setRec_list(record_infolist);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return formsinfocontainer;
		}
	}

	public FormsBean parseDeleteFormResult(String xml) {
		FormsBean fbean = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("FormDeleted");
			node = list.item(0);
			nodeMap = node.getAttributes();
			fbean = new FormsBean();

			if (nodeMap.getNamedItem("ownerid") != null) {
				fbean.setuserName(nodeMap.getNamedItem("ownerid")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("deleteuserid") != null) {
				fbean.setDeletedUserName(nodeMap.getNamedItem("deleteuserid")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("formid") != null) {
				fbean.setFormId(nodeMap.getNamedItem("formid").getNodeValue());
			}
			if (nodeMap.getNamedItem("formname") != null) {
				fbean.setFormName(nodeMap.getNamedItem("formname")
						.getNodeValue());
			}
			fbean.setIsDeletedForm(true);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return fbean;
	}

	/**
	 * Parse the XML and returns the ArrayList.The ArrayList consist of
	 * HeartBeat Response messages.
	 *
	 * @param xml
	 *            XML format String
	 * @return ArrayList of Type Object
	 */
	public ArrayList parseKeepAlive(String xml) {
		Log.d("IMP", "Share accepte xml..........................." + xml);
		WebServiceBean serviceBean = new WebServiceBean();
		ArrayList<Object> buddyList = null;

		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("Buddy");
			buddyList = new ArrayList<Object>();

			for (int i = 0; i < list.getLength(); i++) {
				BuddyInformationBean bib = new BuddyInformationBean();
				node = list.item(i);
				nodeMap = node.getAttributes();
				bib.setName(nodeMap.getNamedItem("name").getNodeValue());
				bib.setLocalipaddress(nodeMap.getNamedItem("localipaddress")
						.getNodeValue());
				bib.setExternalipaddress(nodeMap.getNamedItem(
						"externalipaddress").getNodeValue());

				bib.setStatus(nodeMap.getNamedItem("status").getNodeValue());
				bib.setNickname(nodeMap.getNamedItem("nickname").getNodeValue());
				bib.setFirstname(nodeMap.getNamedItem("firstname").getNodeValue());
				bib.setLastname(nodeMap.getNamedItem("lastname").getNodeValue());
				// System.out.println(nodeMap.getNamedItem("type").getNodeValue());
				bib.setType(nodeMap.getNamedItem("type").getNodeValue());
				bib.setSignalingPort((nodeMap.getNamedItem("signalingport")
						.getNodeValue()));

				if (nodeMap.getNamedItem("lat") != null) {
					bib.setLatitude(nodeMap.getNamedItem("lat").getNodeValue());
				}
				if (nodeMap.getNamedItem("long") != null) {
					bib.setLongitude(nodeMap.getNamedItem("long")
							.getNodeValue());
				}

				if (nodeMap.getNamedItem("distance") != null) {
					String distance = nodeMap.getNamedItem("distance")
							.getNodeValue().trim();
					bib.setDistance(distance);
				} else
					bib.setDistance("nil");

				if (nodeMap.getNamedItem("fbaccid") != null) {
					bib.setFb_id(nodeMap.getNamedItem("fbaccid").getNodeValue());
					Log.d("parser", "......" + bib.getFbID());
				}
				if (nodeMap.getNamedItem("fbdisname") != null) {
					bib.setfbName(nodeMap.getNamedItem("fbdisname")
							.getNodeValue());
					Log.d("parser", "...." + bib.getFBname());
				}

				// System.out.println("bib.getType?"+bib.getType());
				buddyList.add(bib);
			}

			// add new virtual user details....

			list = doc.getElementsByTagName("VirtualUser");

			for (int i = 0; i < list.getLength(); i++) {
				VirtualBuddyBean bib = new VirtualBuddyBean();
				node = list.item(i);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("name") != null) {
					bib.setName(nodeMap.getNamedItem("name").getNodeValue());
					bib.setStatus("Virtual");
				}
				if (nodeMap.getNamedItem("owner") != null) {
					bib.setOwner(nodeMap.getNamedItem("owner").getNodeValue());
				}
				if (nodeMap.getNamedItem("serviceavailable") != null) {
					bib.setServiveAvailable(nodeMap.getNamedItem(
							"serviceavailable").getNodeValue());
				}
				if (nodeMap.getNamedItem("id") != null) {
					bib.setId((nodeMap.getNamedItem("id").getNodeValue()));
				}
				if (nodeMap.getNamedItem("action") != null) {
					bib.setAction((nodeMap.getNamedItem("action")
							.getNodeValue()));
				}

				buddyList.add(bib);

			}

			// used to parse virtualFriendsDetailssssssssss...
			list = doc.getElementsByTagName("VirtualFriendDetails");

			for (int i = 0; i < list.getLength(); i++) {
				VirtualBuddyBean bib = new VirtualBuddyBean();
				node = list.item(i);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("lat") != null) {
					bib.setLatitude(nodeMap.getNamedItem("lat").getNodeValue());

				}

				if (nodeMap.getNamedItem("lang") != null) {
					bib.setLangitude(nodeMap.getNamedItem("lang")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("virtualuserid") != null) {
					bib.setId((nodeMap.getNamedItem("virtualuserid")
							.getNodeValue()));
				}
				if (nodeMap.getNamedItem("friendname") != null) {
					bib.setFriendsName((nodeMap.getNamedItem("friendname")
							.getNodeValue()));
				}

				buddyList.add(bib);

			}

			list = doc.getElementsByTagName("sharereminder");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				buddyList.add(parseShareReminder(nodeMap));
			}
			list = doc.getElementsByTagName("shareresponse");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				buddyList.add(parseShareResponse(nodeMap));
			}

			list = doc.getElementsByTagName("OwnUserForm");
			Log.i("b", "node size===>" + list.getLength());

			for (int i = 0; i < list.getLength(); i++) {
				Log.i("BLOB", "inside own user form");
				node = list.item(i);
				nodeMap = node.getAttributes();
				FormsListBean bean = ParseSigninForms(nodeMap);
				buddyList.add(bean);
				Log.i("b", "owner name,===>" + bean.getForm_owner());

				NodeList nlist = node.getChildNodes();
				for (int j = 0; j < nlist.getLength(); j++) {
					Node node = nlist.item(j);
					if (node != null) {
						Log.i("Forms", "Node Name===>" + node.getNodeName());
						Log.i("Forms", "Form Name===>" + bean.getForm_name());
						if (node.getNodeName().equalsIgnoreCase(
								"UserFormSettings")) {
							NamedNodeMap map = node.getAttributes();
							buddyList.add(ParseSettingforms(map));
						} else if (node.getNodeName().equalsIgnoreCase(
								"FormAttributes")) {
							NamedNodeMap map = node.getAttributes();
							buddyList.add(parseFormAttributes(map,
									bean.getForm_name()));
						}
					}

				}

			}
			// list=doc.getElementsByTagName("UserFormSettings");
			// for(int i=0;i<list.getLength();i++)
			// {
			// Log.i("BLOB", "inside own user form");
			// node=list.item(i);
			// nodeMap=node.getAttributes();
			// buddyList.add(ParseSigninForms(nodeMap));
			// }

			list = doc.getElementsByTagName("UserForm");
			Log.i("b", "node size===>" + list.getLength());
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				FormsListBean bean = ParseSigninForms(nodeMap);
				buddyList.add(bean);
				Log.i("b", "owner name,===>" + bean.getForm_owner());
				NodeList nlist = node.getChildNodes();
				for (int j = 0; j < nlist.getLength(); j++) {
					Node node = nlist.item(j);
					if (node != null) {
						Log.i("Forms", "Node Name===>" + node.getNodeName());
						Log.i("Forms", "Form Name===>" + bean.getForm_name());
						if (node.getNodeName().equalsIgnoreCase(
								"UserFormSettings")) {
							NamedNodeMap map = node.getAttributes();
							buddyList.add(ParseSettingforms(map));
						} else if (node.getNodeName().equalsIgnoreCase(
								"FormAttributes")) {
							NamedNodeMap map = node.getAttributes();
							buddyList.add(parseFormAttributes(map,
									bean.getForm_name()));
						}
					}

				}

			}

			list = doc.getElementsByTagName("FormsFTPUsernamePassword");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				buddyList.add(ParseFtpDetails(nodeMap));
			}
			list = doc.getElementsByTagName("flightdetails");
			// System.out.println("##Length :"+list.getLength());
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				buddyList.add(parseFlightDetails(nodeMap));
			}

			list = doc.getElementsByTagName("ConfigurationResponses");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("status") != null)
					buddyList
							.add(nodeMap.getNamedItem("status").getNodeValue());
			}

			if (getResult(xml)) {
				serviceBean.setResult("1");
			} else {
				serviceBean.setResult("0");
			}
		} catch (Exception e) {
			Log.d("zxz", "Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return buddyList;
	}

	/**
	 * used to parse the FlightDetails Information.
	 * 
	 * @param nodeMap
	 *            NamedNodeMap
	 * @return FlightDetails
	 */
	public FlightDetails parseFlightDetails(NamedNodeMap nodeMap) {
		FlightDetails flightDetails = null;
		if (nodeMap != null) {
			flightDetails = new FlightDetails();
			flightDetails.setFlightNumber(nodeMap.getNamedItem("flightnumber")
					.getNodeValue());
			flightDetails.setSource(nodeMap.getNamedItem("source")
					.getNodeValue());
			flightDetails.setDestination(nodeMap.getNamedItem("destination")
					.getNodeValue());
			flightDetails.setDelayedBy(nodeMap.getNamedItem("delayedby")
					.getNodeValue());
			flightDetails.setStatus(nodeMap.getNamedItem("status")
					.getNodeValue());
		}
		return flightDetails;
	}

	@SuppressWarnings("finally")
	public ArrayList<Object> parseGetConfigurationResponDetails(String xml) {
		ArrayList<Object> response_list = new ArrayList<Object>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("MyResponseDetails");
			node = list.item(0);
			nodeMap = node.getAttributes();
			if (nodeMap.getNamedItem("userid") != null)
				response_list
						.add(nodeMap.getNamedItem("userid").getNodeValue());

			list = doc.getElementsByTagName("Mydetails");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				OfflineRequestConfigBean bean = new OfflineRequestConfigBean();

				if (nodeMap.getNamedItem("id") != null)
					bean.setId(nodeMap.getNamedItem("id").getNodeValue());

				if (nodeMap.getNamedItem("configid") != null)
					bean.setConfigId(nodeMap.getNamedItem("configid")
							.getNodeValue());

				if (nodeMap.getNamedItem("buddyid") != null)
					bean.setBuddyId(nodeMap.getNamedItem("buddyid")
							.getNodeValue());

				if (nodeMap.getNamedItem("messagetitle") != null)
					bean.setMessageTitle(nodeMap.getNamedItem("messagetitle")
							.getNodeValue());

				if (nodeMap.getNamedItem("responsetype") != null)
					bean.setResponseType(nodeMap.getNamedItem("responsetype")
							.getNodeValue());

				if (nodeMap.getNamedItem("response") != null)
					bean.setResponse(nodeMap.getNamedItem("response")
							.getNodeValue());
				response_list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return response_list;
		}
	}

	/**
	 * Parse the XML and Returns the Share remainder details.
	 *
	 * @param nodeMap
	 *            NamedNodeMap
	 * @return ShareRemainder
	 */
	@SuppressWarnings("finally")
	public ShareReminder parseShareReminder(NamedNodeMap nodeMap) {
		ShareReminder reminder = null;
		try {
			nodeMap = node.getAttributes();
			if (nodeMap != null) {

				reminder = new ShareReminder();
				if (nodeMap.getNamedItem("id") != null)
					reminder.setId(nodeMap.getNamedItem("id").getNodeValue());
				if (nodeMap.getNamedItem("from") != null)
					reminder.setFrom(nodeMap.getNamedItem("from")
							.getNodeValue());
				if (nodeMap.getNamedItem("ftppassword") != null)
					reminder.setFtpPassword(nodeMap.getNamedItem("ftppassword")
							.getNodeValue());
				if (nodeMap.getNamedItem("to") != null)
					reminder.setTo(nodeMap.getNamedItem("to").getNodeValue());
				if (nodeMap.getNamedItem("type") != null)
					reminder.setType(nodeMap.getNamedItem("type")
							.getNodeValue());
				if (nodeMap.getNamedItem("reminderstatus") != null)
					reminder.setReminderStatus(nodeMap.getNamedItem(
							"reminderstatus").getNodeValue());
				if (nodeMap.getNamedItem("filelocation") != null)
					reminder.setFileLocation(nodeMap.getNamedItem(
							"filelocation").getNodeValue());
				if (nodeMap.getNamedItem("remindertz") != null)
					reminder.setRemindertz(nodeMap.getNamedItem("remindertz")
							.getNodeValue());
				if (nodeMap.getNamedItem("status") != null)
					reminder.setStatus(nodeMap.getNamedItem("status")
							.getNodeValue());
				if (nodeMap.getNamedItem("receiverstatus") != null)
					reminder.setReceiverStatus(nodeMap.getNamedItem(
							"receiverstatus").getNodeValue());
				if (nodeMap.getNamedItem("reminderdatetime") != null)
					reminder.setReminderdatetime(nodeMap.getNamedItem(
							"reminderdatetime").getNodeValue());

				if (nodeMap.getNamedItem("responsetype") != null) {
					reminder.setReminderResponseType(nodeMap.getNamedItem(
							"responsetype").getNodeValue());
				}
				if (nodeMap.getNamedItem("busynote") != null) {
					reminder.setIsbusyResponse(nodeMap.getNamedItem("busynote")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("downloadtype") != null) {
					reminder.setDownloadType(nodeMap.getNamedItem(
							"downloadtype").getNodeValue());
				}
				if (nodeMap.getNamedItem("comment") != null)
					reminder.setComment(nodeMap.getNamedItem("comment")
							.getNodeValue());
				if (nodeMap.getNamedItem("vanishmode") != null) {
					reminder.setVanishMode(nodeMap.getNamedItem("vanishmode")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("vanishvalue") != null) {
					reminder.setVanishValue(nodeMap.getNamedItem("vanishvalue")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("bscategory") != null) {
					reminder.setBstype(nodeMap.getNamedItem("bscategory")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("bsstatus") != null) {
					reminder.setBsstatus(nodeMap.getNamedItem("bsstatus")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("bsdirection") != null) {
					reminder.setDirection(nodeMap.getNamedItem("bsdirection")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("parentid") != null) {
					reminder.setParent_id(nodeMap.getNamedItem("parentid")
							.getNodeValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return reminder;
		}

	}

	public ShareResponseBean parseShareResponse(NamedNodeMap nodeMap) {
		ShareResponseBean reminder = null;
		if (nodeMap != null) {
			reminder = new ShareResponseBean();
			reminder.setShareId(nodeMap.getNamedItem("sid").getNodeValue());
			reminder.setStatus(nodeMap.getNamedItem("status").getNodeValue());
		}

		return reminder;
	}

	/**
	 * Parse the input String and returns the ArrayList.The ArrayList consist of
	 * Find people bean.
	 * 
	 * @param xml
	 *            String
	 * @return ArrayList<FindPeopleBean>
	 */
	public ArrayList<FindPeopleBean> parseFindPeople(String xml) {

		WebServiceBean serviceBean = new WebServiceBean();
		ArrayList<FindPeopleBean> peopleList = null;

		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("searchusers");
			peopleList = new ArrayList<FindPeopleBean>();

			for (int i = 0; i < list.getLength(); i++) {
				FindPeopleBean bean = new FindPeopleBean();
				node = list.item(i);
				nodeMap = node.getAttributes();
				bean.setName(nodeMap.getNamedItem("name").getNodeValue());
				bean.setEmailId(nodeMap.getNamedItem("emailid").getNodeValue());
				bean.setRole(nodeMap.getNamedItem("occupation").getNodeValue());
				peopleList.add(bean);

			}

			list = doc.getElementsByTagName("virtualuser");
			for (int i = 0; i < list.getLength(); i++) {
				FindPeopleBean bean = new FindPeopleBean();
				node = list.item(i);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("name") != null) {
					bean.setName(nodeMap.getNamedItem("name").getNodeValue());
				}
				if (nodeMap.getNamedItem("id") != null) {
					bean.setId(nodeMap.getNamedItem("id").getNodeValue());
				}
				if (nodeMap.getNamedItem("owner") != null) {
					bean.setOwner(nodeMap.getNamedItem("owner").getNodeValue());
				}
				// if(nodeMap.getNamedItem("id")!=null)
				// {
				// bean.setId(nodeMap.getNamedItem("id").getNodeValue());
				// }
				peopleList.add(bean);

			}

			// Used to parse Virtual Buddies

			if (getResult(xml)) {
				serviceBean.setResult("1");
			} else {
				serviceBean.setResult("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return peopleList;
	}

	/**
	 * Parse the XML and returns the Result and Type.
	 * 
	 * @param xml
	 *            input String
	 * @return String[]
	 */
	public String[] getTypeResult(String xml) {

		String result[] = new String[2];

		try {

			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);

			list = doc.getElementsByTagName("com");
			node = list.item(0);
			nodeMap = node.getAttributes();
			String res = nodeMap.getNamedItem("result").getNodeValue();
			result[0] = res;
			res = nodeMap.getNamedItem("type").getNodeValue();
			result[1] = res;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	public String[] getType(String xml) {

		String result[] = new String[3];

		try {

			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);

			list = doc.getElementsByTagName("com");
			node = list.item(0);
			nodeMap = node.getAttributes();

			String res = nodeMap.getNamedItem("type").getNodeValue();
			result[0] = res;
			res = nodeMap.getNamedItem("sid").getNodeValue();
			result[1] = res;
			String rest = nodeMap.getNamedItem("eid").getNodeValue();
			res = nodeMap.getNamedItem("eid").getNodeValue();
			result[2] = res;

		} catch (Exception e) {
		}

		return result;

	}

	/**
	 * Used to get Result
	 * 
	 * @param xml
	 *            input String
	 * @return boolean
	 */

	public ArrayList<String> parseSharebyprofileSearchXml(String xml) {

		Log.i("xml", "Response xml" + xml);
		ArrayList<String> result = new ArrayList<String>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("buddyList");
			for (int i = 0; i < list.getLength(); i++) {

				node = list.item(0);
				nodeMap = node.getAttributes();

				NodeList nlist = node.getChildNodes();

				for (int j = 0; j < nlist.getLength(); j++) {

					Node node = nlist.item(j);
					if (node != null) {

						if (node.getNodeName().equalsIgnoreCase(
								"FoundPeopleByProfile")) {
							NamedNodeMap map = node.getAttributes();
							result.add(map.getNamedItem("buddyName")
									.getNodeValue());

						}
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("zxz", "received lo Exception .class..............");
			Log.d("IOS",
					"received lo Exception .class.............."
							+ e.getMessage());

		}
		return result;

	}

	public boolean getResult(String xml) {

		boolean result = false;

		try {

			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);

			list = doc.getElementsByTagName("com");
			node = list.item(0);
			nodeMap = node.getAttributes();
			String res = nodeMap.getNamedItem("result").getNodeValue();
			result = (res.equals("1") ? true : false);
			// System.out.println("Result=="+res);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	public ShareSendBean getShareSendResult(String xml) {

		ShareSendBean sbean = null;

		try {
			sbean = new ShareSendBean();
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);

			list = doc.getElementsByTagName("com");
			node = list.item(0);
			nodeMap = node.getAttributes();
			String res = nodeMap.getNamedItem("result").getNodeValue();

			list = doc.getElementsByTagName("sharereminder");
			node = list.item(0);
			nodeMap = node.getAttributes();
			String fileID = nodeMap.getNamedItem("fileid").getNodeValue();
			if (nodeMap.getNamedItem("text").getNodeValue() != null)
				sbean.setText(nodeMap.getNamedItem("text").getNodeValue());

			sbean.setStatus(res);
			sbean.setFileId(fileID);

			// System.out.println("Result=="+res);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sbean;

	}

	/**
	 * parse the XML and returns the ArrayList which contains BuddyInformations.
	 * 
	 * @param xml
	 *            input String
	 * @return ArrayList<BuddyInformationBean>
	 */
	public ArrayList<BuddyInformationBean> parseAddPeople(String xml) {

		WebServiceBean serviceBean = new WebServiceBean();
		ArrayList<BuddyInformationBean> addBuddyList = null;

		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("AddBuddy");
			addBuddyList = new ArrayList<BuddyInformationBean>();

			for (int i = 0; i < list.getLength(); i++) {
				BuddyInformationBean bean = new BuddyInformationBean();
				node = list.item(i);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("name") != null) {
					bean.setName(nodeMap.getNamedItem("name").getNodeValue());
				}
				if (nodeMap.getNamedItem("nickname") != null) {
					bean.setNickname(nodeMap.getNamedItem("nickname").getNodeValue());
				}
				if (nodeMap.getNamedItem("lastname") != null) {
					bean.setLastname(nodeMap.getNamedItem("lastname").getNodeValue());
				}
				if (nodeMap.getNamedItem("firstname") != null) {
					bean.setFirstname(nodeMap.getNamedItem("firstname").getNodeValue());
				}
				if (nodeMap.getNamedItem("emailid") != null) {
					bean.setEmailid(nodeMap.getNamedItem("emailid")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("localipaddress") != null) {
					bean.setLocalipaddress(nodeMap.getNamedItem(
							"localipaddress").getNodeValue());
				}
				if (nodeMap.getNamedItem("externalipaddress") != null) {
					bean.setExternalipaddress(nodeMap.getNamedItem(
							"externalipaddress").getNodeValue());
				}
				if (nodeMap.getNamedItem("photo") != null) {
					bean.setPhoto(nodeMap.getNamedItem("photo").getNodeValue()
							.getBytes());
				}
				if (nodeMap.getNamedItem("status") != null) {
					bean.setStatus(nodeMap.getNamedItem("status")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("occupation") != null) {
					bean.setOccupation(nodeMap.getNamedItem("occupation")
							.getNodeValue());
				}
				addBuddyList.add(bean);
			}

			Log.d("thread", "Addpeople response " + addBuddyList.size());

			if (getResult(xml)) {
				serviceBean.setResult("1");

			} else {
				serviceBean.setResult("0");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return addBuddyList;
	}

	public ArrayList<FormAttributeBean> parseGetAttribute(String xml) {

		WebServiceBean serviceBean = new WebServiceBean();
		ArrayList<FormAttributeBean> addBuddyList = null;
		Log.i("bug", "inside form attributebean--->");

		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("FormAttributes");
			addBuddyList = new ArrayList<FormAttributeBean>();
			Log.i("bug", "inside form attributebean--->");

			for (int i = 0; i < list.getLength(); i++) {
				FormAttributeBean bean = new FormAttributeBean();
				node = list.item(i);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("formid") != null) {
					bean.setformid(nodeMap.getNamedItem("formid")
							.getNodeValue());
					Log.i("bug", "formid--->"
							+ nodeMap.getNamedItem("formid").getNodeValue());

				}
				if (nodeMap.getNamedItem("attributeid") != null) {
					bean.setAttributeid(nodeMap.getNamedItem("attributeid")
							.getNodeValue());
					Log.i("ff123",
							"attributeid--->"
									+ nodeMap.getNamedItem("attributeid")
											.getNodeValue());

				}
				if (nodeMap.getNamedItem("fatid") != null) {
					bean.setAttributeid(nodeMap.getNamedItem("fatid")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("fieldname") != null) {
					bean.setfieldname(nodeMap.getNamedItem("fieldname")
							.getNodeValue());
					Log.i("bug",
							"fieldname--->"
									+ nodeMap.getNamedItem("fieldname")
											.getNodeValue());

				}
				if (nodeMap.getNamedItem("fieldtype") != null) {
					bean.setFieldtype(nodeMap.getNamedItem("fieldtype")
							.getNodeValue());

					Log.i("bug",
							"fieldtype--->"
									+ nodeMap.getNamedItem("fieldtype")
											.getNodeValue());
				}
				if (nodeMap.getNamedItem("dataentrymode") != null) {
					bean.setDataEntry(nodeMap.getNamedItem("dataentrymode")
							.getNodeValue());
					Log.i("bug",
							"dataentrymode--->"
									+ nodeMap.getNamedItem("dataentrymode")
											.getNodeValue());

				}
				if (nodeMap.getNamedItem("datasource") != null) {
					bean.setDataSource(nodeMap.getNamedItem("datasource")
							.getNodeValue());
					Log.i("bug",
							"datasource--->"
									+ nodeMap.getNamedItem("datasource")
											.getNodeValue());

				}
				if (nodeMap.getNamedItem("datavalidation") != null) {
					bean.setDataValidation(nodeMap.getNamedItem(
							"datavalidation").getNodeValue());
					Log.i("bug",
							"datavalidation--->"
									+ nodeMap.getNamedItem("datavalidation")
									.getNodeValue());

				}
				if (nodeMap.getNamedItem("defaultvalue") != null) {
					bean.setDefaultvalue(nodeMap.getNamedItem("defaultvalue")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("instruction") != null) {
					bean.setInstruction(nodeMap.getNamedItem("instruction")
							.getNodeValue());
					Log.i("bug",
							"instruction--->"
									+ nodeMap.getNamedItem("instruction")
									.getNodeValue());

				}
				if (nodeMap.getNamedItem("errortip") != null) {
					bean.setErrortip(nodeMap.getNamedItem("errortip")
							.getNodeValue());
					Log.i("bug",
							"errortip--->"
									+ nodeMap.getNamedItem("errortip")
									.getNodeValue());

				}

				addBuddyList.add(bean);
			}

			if (getResult(xml)) {
				serviceBean.setResult("1");
			} else {
				serviceBean.setResult("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("bug", "inside form attributebean errrrrrrrrrrrrrrrrrr--->"
					+ e.getMessage().toString());

		}
		return addBuddyList;
	}

	public ArrayList<VirtualBuddyBean> parseAddVirtualBuddy(String xml) {

		WebServiceBean serviceBean = new WebServiceBean();
		ArrayList<VirtualBuddyBean> addBuddyList = null;

		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("VirtualUser");
			addBuddyList = new ArrayList<VirtualBuddyBean>();

			for (int i = 0; i < list.getLength(); i++) {
				VirtualBuddyBean bean = new VirtualBuddyBean();
				node = list.item(i);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("name") != null) {
					bean.setName(nodeMap.getNamedItem("name").getNodeValue());
					bean.setStatus("Virtual");
				}
				if (nodeMap.getNamedItem("owner") != null) {
					bean.setOwner(nodeMap.getNamedItem("owner").getNodeValue());
				}
				if (nodeMap.getNamedItem("serviceavilable") != null) {
					bean.setServiveAvailable(nodeMap.getNamedItem(
							"serviceavilable").getNodeValue());
				}
				if (nodeMap.getNamedItem("action") != null) {
					bean.setAction(nodeMap.getNamedItem("action")
							.getNodeValue());
				}

				addBuddyList.add(bean);
			}

			if (getResult(xml)) {
				serviceBean.setResult("1");
			} else {
				serviceBean.setResult("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addBuddyList;
	}

	public UdpMessageBean parseUdpMessageBean(String xml) {

		Log.d("XML_PARSER", "UdpMessageBean Parser : " + xml);

		UdpMessageBean sb = new UdpMessageBean();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			if (!xml.contains("eid=\"17\"")) {
				list = doc.getElementsByTagName("Buddy");

				if (list.getLength() != 0) {
					node = list.item(0);
					nodeMap = node.getAttributes();

					BuddyInformationBean bib = new BuddyInformationBean();

					Log.d("XMLTAG", "parse xmlwewe " + xml);
					if (nodeMap.getNamedItem("name") != null) {
						Log.d("XMLTAG", "parse xml contains name");
						bib.setName((nodeMap.getNamedItem("name")
								.getNodeValue()));
					} else {
						Log.d("XMLTAG", "parse xml not    contains name");
					}
					if (nodeMap.getNamedItem("status") != null) {
						bib.setStatus((nodeMap.getNamedItem("status")
								.getNodeValue()));
					}

					if (nodeMap.getNamedItem("lat") != null) {
						bib.setLatitude((nodeMap.getNamedItem("lat")
								.getNodeValue()));
					}
					if (nodeMap.getNamedItem("long") != null) {
						bib.setLongitude((nodeMap.getNamedItem("long")
								.getNodeValue()));
					}

					if (nodeMap.getNamedItem("distance") != null) {
						String distance = nodeMap.getNamedItem("distance")
								.getNodeValue().trim();
						bib.setDistance(distance);
					} else
						bib.setDistance("nil");

					// if(sb.getEid().equals("3"))
					// {
					if (nodeMap.getNamedItem("localipaddress") != null) {
						bib.setLocalipaddress((nodeMap
								.getNamedItem("localipaddress").getNodeValue()));
					}

					if (nodeMap.getNamedItem("externalipaddress") != null) {
						bib.setExternalipaddress((nodeMap
								.getNamedItem("externalipaddress")
								.getNodeValue()));
					}
					if (nodeMap.getNamedItem("signalingport") != null) {
						bib.setSignalingPort((nodeMap
								.getNamedItem("signalingport").getNodeValue()));
					}

					if (nodeMap.getNamedItem("fbaccid") != null) {
						bib.setFb_id(nodeMap.getNamedItem("fbaccid")
								.getNodeValue());
					}
					if (nodeMap.getNamedItem("fbdisname") != null) {
						bib.setfbName(nodeMap.getNamedItem("fbdisname")
								.getNodeValue());
					}
					if (nodeMap.getNamedItem("occupation") != null) {
						bib.setOccupation(nodeMap.getNamedItem("occupation")
								.getNodeValue());
					}
					if (nodeMap.getNamedItem("firstname") != null) {
						bib.setFirstname((nodeMap.getNamedItem("firstname")
								.getNodeValue()));
					}
					if (nodeMap.getNamedItem("lastname") != null) {
						bib.setLastname((nodeMap.getNamedItem("lastname")
								.getNodeValue()));
					}
					if (nodeMap.getNamedItem("nickname") != null) {
						bib.setNickname((nodeMap.getNamedItem("nickname")
								.getNodeValue()));
					}

					// }
					sb.setBuddyInformation(bib);
					String result[] = getType(xml);
					sb.setType(result[0]);
					sb.setSid(result[1]);
					sb.setEid(result[2]);
					sb.setText(null);
				}
			}
			list = doc.getElementsByTagName("text");
			Log.d("lg", "......" + list.getLength());

			if ((list != null) && (list.getLength() != 0)) {
				System.out.println("OP1 :" + list.getLength());

				node = list.item(0);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("reason") != null) {
					sb.setText((nodeMap.getNamedItem("reason").getNodeValue()));
				}
				try {
					String result[] = getType(xml);
					sb.setType(result[0]);
					sb.setSid(result[1]);
					sb.setEid(result[2]);

				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			list = doc.getElementsByTagName("sharereminder");
			if ((list != null) && (list.getLength() != 0)) {
				ArrayList<Object> buddyList = new ArrayList<Object>();

				for (int i = 0; i < list.getLength(); i++) {
					node = list.item(i);
					nodeMap = node.getAttributes();
					buddyList.add(parseShareReminder(nodeMap));
					sb.setlist(buddyList);
				}
				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
			}
			// used to parse virtualFriendsDetailssssssssss...
			list = doc.getElementsByTagName("VirtualFriendDetails");

			if (list.getLength() != 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();

				VirtualBuddyBean bib = new VirtualBuddyBean();

				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("lat") != null) {
					bib.setLatitude(nodeMap.getNamedItem("lat").getNodeValue());

				}

				if (nodeMap.getNamedItem("long") != null) {
					bib.setLangitude(nodeMap.getNamedItem("long")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("virtualuserid") != null) {
					bib.setId((nodeMap.getNamedItem("virtualuserid")
							.getNodeValue()));
				}
				if (nodeMap.getNamedItem("friendname") != null) {
					bib.setFriendsName((nodeMap.getNamedItem("friendname")
							.getNodeValue()));
				}

				Log.d("UMBX", "on parsing VBddddd " + bib.getLatitude()
						+ " long " + bib.getLangitude());

				sb.setVirtualBuddyBean(bib);

				if (sb.getType() == null) {
					String result[] = getType(xml);
					sb.setType(result[0]);
					sb.setSid(result[1]);
					sb.setEid(result[2]);
					sb.setText(null);
				}
			}

			ArrayList<Object> forms_list = null;
			list = doc.getElementsByTagName("UserForm");
			if (list.getLength() > 0) {
				forms_list = new ArrayList<Object>();
				FormsListBean bean = new FormsListBean();
				for (int i = 0; i < list.getLength(); i++) {
					node = list.item(i);
					nodeMap = node.getAttributes();
					bean = ParseOwninForms(nodeMap);
					NodeList nlist = node.getChildNodes();
					if (nlist != null) {
						for (int j = 0; j < nlist.getLength(); j++) {
							Node node = nlist.item(j);
							NamedNodeMap map = node.getAttributes();

							FormSettingBean frmrbean = new FormSettingBean();
							try {
								if (map.getNamedItem("fsid") != null) {
									Log.i("settings", " fsid "
											+ map.getNamedItem("fsid")
													.getNodeValue());

									frmrbean.setFsid(map.getNamedItem("fsid")
											.getNodeValue());
								}
								if (map.getNamedItem("ownerid") != null) {
									Log.i("settings", " ownerid "
											+ map.getNamedItem("ownerid")
													.getNodeValue());

									frmrbean.setBuddy(map.getNamedItem(
											"ownerid").getNodeValue());
								}
								if (map.getNamedItem("mode") != null) {
									Log.i("settings", " mode "
											+ map.getNamedItem("mode")
													.getNodeValue());

									frmrbean.setmode(map.getNamedItem("mode")
											.getNodeValue());

									if (map.getNamedItem("mode").getNodeValue()
											.equalsIgnoreCase("new")) {
										bean.setMode("yes");
									} else {
										bean.setMode("no");
									}
								}
								if (map.getNamedItem("formid") != null) {
									Log.i("settings", " formid "
											+ map.getNamedItem("formid")
													.getNodeValue());

									frmrbean.setFormid(map.getNamedItem(
											"formid").getNodeValue());
								}
								if (map.getNamedItem("formsettingtime") != null) {
									Log.i("settings",
											" formsettingtime "
													+ map.getNamedItem(
															"formsettingtime")
															.getNodeValue());

									frmrbean.setsyncid(map.getNamedItem(
											"formsettingtime").getNodeValue());
								}

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							sb.setSettingList(frmrbean);

						}
					}

				}
				forms_list.add(bean);
				sb.setFormslist(forms_list);
				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
			}

			list = doc.getElementsByTagName("FormDeleted");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				FormsBean fdelbean = new FormsBean();

				if (nodeMap.getNamedItem("ownerid") != null) {
					fdelbean.setuserName(nodeMap.getNamedItem("ownerid")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("deleteuserid") != null) {
					fdelbean.setDeletedUserName(nodeMap.getNamedItem(
							"deleteuserid").getNodeValue());
				}
				if (nodeMap.getNamedItem("formid") != null) {
					fdelbean.setFormId(nodeMap.getNamedItem("formid")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("formname") != null) {
					fdelbean.setFormName(nodeMap.getNamedItem("formname")
							.getNodeValue());
				}
				fdelbean.setIsDeletedForm(true);
				sb.setDeleteObject(fdelbean);

				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
			}

			list = doc.getElementsByTagName("FormRowDeleted");
			Log.i("form", "^^^^^^^^^^^^^^^^^^^^" + list.toString() + "deleted");
			if (list.getLength() > 0) {

				node = list.item(0);
				nodeMap = node.getAttributes();
				FormsBean fdelrbean = new FormsBean();

				if (nodeMap.getNamedItem("ownerid") != null) {
					fdelrbean.setuserName(nodeMap.getNamedItem("ownerid")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("deleteuserid") != null) {
					fdelrbean.setDeletedUserName(nodeMap.getNamedItem(
							"deleteuserid").getNodeValue());
				}
				if (nodeMap.getNamedItem("formid") != null) {
					fdelrbean.setFormId(nodeMap.getNamedItem("formid")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("formname") != null) {
					fdelrbean.setFormName(nodeMap.getNamedItem("formname")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("tableid") != null) {
					fdelrbean.setDeletedrecordId(nodeMap
							.getNamedItem("tableid").getNodeValue());
				}
				if (nodeMap.getNamedItem("rows") != null) {
					fdelrbean.setCount(nodeMap.getNamedItem("rows")
							.getNodeValue());
				}

				fdelrbean.setIsDeletedRecord(true);
				sb.setDeleteObject(fdelrbean);

				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
			}
			list = doc.getElementsByTagName("FormRowEdited");
			Log.i("form", "^^^^^^^^^^^^^^^^^^^^" + list.toString() + "edited");

			if (list.getLength() > 0) {

				node = list.item(0);
				nodeMap = node.getAttributes();
				FormsBean fdelrbean = new FormsBean();

				if (nodeMap.getNamedItem("ownerid") != null) {
					fdelrbean.setuserName(nodeMap.getNamedItem("ownerid")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("edituserid") != null) {
					fdelrbean.setDeletedUserName(nodeMap.getNamedItem(
							"edituserid").getNodeValue());
				}
				if (nodeMap.getNamedItem("formid") != null) {
					fdelrbean.setFormId(nodeMap.getNamedItem("formid")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("formname") != null) {
					fdelrbean.setFormName(nodeMap.getNamedItem("formname")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("rowid") != null) {
					fdelrbean.setEditrecordId(nodeMap.getNamedItem("rowid")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("formtime") != null) {
					fdelrbean.setFormTime(nodeMap.getNamedItem("formtime")
							.getNodeValue());
				}
				fdelrbean.setIsEditRecord(true);
				sb.setDeleteObject(fdelrbean);

				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
			}

			list = doc.getElementsByTagName("FormsFieldSettings");
			String[] fieldSettingsDetails = null;
			if (list.getLength() > 0) {
				fieldSettingsDetails = new String[2];
				node = list.item(0);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("userid") != null) {
					fieldSettingsDetails[0] = nodeMap.getNamedItem("userid")
							.getNodeValue();
				}
				if (nodeMap.getNamedItem("formid") != null) {
					fieldSettingsDetails[1] = nodeMap.getNamedItem("formid")
							.getNodeValue();
				}
				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
				sb.setText(null);
				sb.setFormFieldSettings(fieldSettingsDetails);
			}

			FormFieldSettingDeleteBean fsdBean = null;
			list = doc.getElementsByTagName("DeleteFieldSettings");
			if (list.getLength() > 0) {
				fsdBean = new FormFieldSettingDeleteBean();
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("userid") != null) {
					fsdBean.setUserId(nodeMap.getNamedItem("userid")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("formid") != null) {
					fsdBean.setFormId(nodeMap.getNamedItem("formid")
							.getNodeValue());
				}
				NodeList nlist = node.getChildNodes();
				ArrayList<String[]> accessList = new ArrayList<String[]>();
				if (nlist != null) {
					for (int i = 0; i < nlist.getLength(); i++) {
						Node node = nlist.item(i);
						NamedNodeMap map = node.getAttributes();
						String access[] = new String[2];
						if (map.getNamedItem("attributeid").getNodeValue() != null) {
							access[0] = map.getNamedItem("attributeid")
									.getNodeValue();
						}
						if (map.getNamedItem("accessiblebuddy").getNodeValue() != null) {
							access[1] = map.getNamedItem("accessiblebuddy")
									.getNodeValue();
						}
						accessList.add(access);
					}

				}
				fsdBean.setBuddiesList(accessList);
				sb.setfSettingDeleteBean(fsdBean);
				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
			}

			list = doc.getElementsByTagName("DeleteAllShares");
			String[] configresponse_details = null;
			if (list.getLength() > 0) {
				configresponse_details = new String[2];
				node = list.item(0);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("buddyid") != null)
					configresponse_details[0] = nodeMap.getNamedItem("buddyid")
							.getNodeValue();

				if (nodeMap.getNamedItem("ownerid") != null)
					configresponse_details[1] = nodeMap.getNamedItem("ownerid")
							.getNodeValue();

				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
				sb.setText(null);
				sb.setConfig_responselist(configresponse_details);
			}

			list = doc.getElementsByTagName("ConfigResponse");
			if (list.getLength() > 0) {
				String[] result1 = new String[2];
				node = list.item(0);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("buddyid") != null)
					result1[0] = nodeMap.getNamedItem("buddyid").getNodeValue();

				if (nodeMap.getNamedItem("configid") != null)
					result1[1] = nodeMap.getNamedItem("configid")
							.getNodeValue();

				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
				sb.setText(null);
				sb.setCallrresponseupadte(result1);

			}

			list = doc.getElementsByTagName("Permission");
			if (list.getLength() > 0) {
				String[] permission_details = new String[2];
				node = list.item(0);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("ownerid") != null)
					permission_details[0] = nodeMap.getNamedItem("ownerid")
							.getNodeValue();

				if (nodeMap.getNamedItem("buddyid") != null)
					permission_details[1] = nodeMap.getNamedItem("buddyid")
							.getNodeValue();

				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
				sb.setText(null);
				sb.setPermission_details(permission_details);

			}

			list = doc.getElementsByTagName("StandardProfile");
			if (list.getLength() > 0) {
				String[] profile_details = new String[3];
				node = list.item(0);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("profileowner") != null)
					profile_details[0] = nodeMap.getNamedItem("profileowner")
							.getNodeValue();

				if (nodeMap.getNamedItem("profileid") != null)
					profile_details[1] = nodeMap.getNamedItem("profileid")
							.getNodeValue();

				if (nodeMap.getNamedItem("profilemodifiedtime") != null
						|| nodeMap.getNamedItem("profilemodifiedtime")
								.toString().length() == 0)
					profile_details[2] = nodeMap.getNamedItem(
							"profilemodifiedtime").getNodeValue();

				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
				sb.setText(null);
				sb.setProfile_details(profile_details);

			}

			list = doc.getElementsByTagName("ResetAccount");
			String[] accountdetails = null;
			if (list.getLength() > 0) {
				accountdetails = new String[2];
				node = list.item(0);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("accountowner") != null)
					accountdetails[0] = nodeMap.getNamedItem("accountowner")
							.getNodeValue();

				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
				sb.setText(null);
				sb.setResetProfile(true);
				sb.setReset_account(accountdetails);
			}

			list = doc.getElementsByTagName("ResetProfile");
			String[] profileDetails = null;
			if (list.getLength() > 0) {
				profileDetails = new String[2];
				node = list.item(0);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("profileowner") != null)
					profileDetails[0] = nodeMap.getNamedItem("profileowner")
							.getNodeValue();

				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
				sb.setText(null);
				sb.setResetProfile(true);
				sb.setProfile_details(profileDetails);
			}
			list = doc.getElementsByTagName("GroupSettings");
			String[] groupChatSettings = null;
			if (list.getLength() > 0) {
				groupChatSettings = new String[3];
				node = list.item(0);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("groupOwner") != null)
					groupChatSettings[0] = nodeMap.getNamedItem("groupOwner")
							.getNodeValue();
				if (nodeMap.getNamedItem("groupName") != null)
					groupChatSettings[1] = nodeMap.getNamedItem("groupName")
							.getNodeValue();
				if (nodeMap.getNamedItem("groupId") != null)
					groupChatSettings[2] = nodeMap.getNamedItem("groupId")
							.getNodeValue();

				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
				sb.setText(null);
				sb.setGroupChatSettings(groupChatSettings);
				SingleInstance.mainContext.showToast("your access has been modified in "+groupChatSettings[1]+" group");
			}

			list = doc.getElementsByTagName("GroupMemberAdd");
			GroupBean groupBean = null;
			if (list.getLength() > 0) {

				groupBean = parseGroupXml(xml);

			}
			list = doc.getElementsByTagName("GroupMembersDelete");

			if (list.getLength() > 0) {
				groupBean = parseGroupXml(xml);
			}
			list = doc.getElementsByTagName("EditGroup");

			if (list.getLength() > 0) {
				groupBean = parseGroupXml(xml);
			}
			list = doc.getElementsByTagName("GroupDelete");

			if (list.getLength() > 0) {
				groupBean = parseGroupXml(xml);
			}

			list = doc.getElementsByTagName("LeaveGroup");

			if (list.getLength() > 0) {
				groupBean = parseGroupXml(xml);
			}

			list = doc.getElementsByTagName("CreateorModifyGroup");

			if (list.getLength() > 0)
				groupBean = parseGroupXml(xml);

			if (groupBean != null) {
				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
				sb.setText(null);
				sb.setResponseObject(groupBean);
				SingleInstance.mainContext.EidforGroup=sb.getEid();
				Log.i("AAAA","xml parser EID = "+sb.getEid());
			}
			list = doc.getElementsByTagName("AcceptRejectGroupMember");
			if (list.getLength() > 0) {
				Log.i("AAAA","XML PARSER AcceptRejectGroupMember SIGNAL");
				String[] temp = new String[2];
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("groupId") != null)
					temp[0] = nodeMap.getNamedItem("groupId").getNodeValue();
				if (nodeMap.getNamedItem("status") != null)
					temp[1] = nodeMap.getNamedItem("status").getNodeValue();

				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
				sb.setText(null);
				sb.setResponseObject(temp);
			}
			list = doc.getElementsByTagName("Roundingcomments");
			if (list.getLength() > 0) {
				Log.i("patientdetails","XML PARSER Roundingcomments SIGNAL");
				String[] temp = new String[4];
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("ownername") != null)
					temp[0] = nodeMap.getNamedItem("ownername").getNodeValue();
				if (nodeMap.getNamedItem("groupid") != null)
					temp[1] = nodeMap.getNamedItem("groupid").getNodeValue();
				if (nodeMap.getNamedItem("patientid") != null)
					temp[2] = nodeMap.getNamedItem("patientid").getNodeValue();
				if (nodeMap.getNamedItem("commentid") != null)
					temp[3] = nodeMap.getNamedItem("commentid").getNodeValue();
				WebServiceReferences.webServiceClient.GetPatientComments(temp[3], temp[1], temp[2], SingleInstance.mainContext);
			}
			list = doc.getElementsByTagName("PatientRecord");
			if (list.getLength() > 0) {
				Log.i("patientdetails","XML PARSER PatientRecord SIGNAL");
				String[] temp = new String[3];
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("ownername") != null)
					temp[0] = nodeMap.getNamedItem("ownername").getNodeValue();
				if (nodeMap.getNamedItem("groupid") != null)
					temp[1] = nodeMap.getNamedItem("groupid").getNodeValue();
				if (nodeMap.getNamedItem("patientid") != null)
					temp[2] = nodeMap.getNamedItem("patientid").getNodeValue();
				WebServiceReferences.webServiceClient.GetPatientRecords(temp[2], temp[1], SingleInstance.mainContext);
			}
			list = doc.getElementsByTagName("TaskRecord");
			if (list.getLength() > 0) {
				Log.i("patientdetails","XML PARSER TaskRecord SIGNAL");
				String[] temp = new String[4];
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("ownername") != null)
					temp[0] = nodeMap.getNamedItem("ownername").getNodeValue();
				if (nodeMap.getNamedItem("groupid") != null)
					temp[1] = nodeMap.getNamedItem("groupid").getNodeValue();
				if (nodeMap.getNamedItem("taskid") != null)
					temp[2] = nodeMap.getNamedItem("taskid").getNodeValue();
				if (nodeMap.getNamedItem("taskstatus") != null)
					temp[3] = nodeMap.getNamedItem("taskstatus").getNodeValue();
				if(temp[3].equalsIgnoreCase("0"))
				WebServiceReferences.webServiceClient.GetTaskInfo(temp[2], temp[1], SingleInstance.mainContext);
				else {
					String strQuery="DELETE from taskdetails WHERE groupid='" + temp[1]
							+ "'and taskid='" + temp[2] + "'";
					DBAccess.getdbHeler().deleteTaskDetails(strQuery);
				}

			}
			list = doc.getElementsByTagName("PatientDescription");
			if (list.getLength() > 0) {
				Log.i("patientdetails","XML PARSER PatientDescription SIGNAL");
				String[] temp = new String[3];
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("ownername") != null)
					temp[0] = nodeMap.getNamedItem("ownername").getNodeValue();
				if (nodeMap.getNamedItem("patientid") != null)
					temp[1] = nodeMap.getNamedItem("patientid").getNodeValue();
				if (nodeMap.getNamedItem("reportid") != null)
					temp[2] = nodeMap.getNamedItem("reportid").getNodeValue();
				WebServiceReferences.webServiceClient.GetPatientDescription(temp[1], temp[2], SingleInstance.mainContext);
			}
			list = doc.getElementsByTagName("MemberRights");
			if (list.getLength() > 0) {
				Log.i("patientdetails","XML PARSER MemberRights SIGNAL");
				String[] temp = new String[4];
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("ownername") != null)
					temp[0] = nodeMap.getNamedItem("ownername").getNodeValue();
				if (nodeMap.getNamedItem("groupid") != null)
					temp[1] = nodeMap.getNamedItem("groupid").getNodeValue();
				if (nodeMap.getNamedItem("status") != null)
					temp[2] = nodeMap.getNamedItem("status").getNodeValue();
				if (nodeMap.getNamedItem("role") != null)
					temp[3] = nodeMap.getNamedItem("role").getNodeValue();
				WebServiceReferences.webServiceClient.GetMemberRights(CallDispatcher.LoginUser,temp[1],SingleInstance.mainContext);
			}
			list = doc.getElementsByTagName("RoleAccess");
			if (list.getLength() > 0) {
				Log.i("patientdetails","XML PARSER RoleAccess SIGNAL");
				String[] temp = new String[3];
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("ownername") != null)
					temp[0] = nodeMap.getNamedItem("ownername").getNodeValue();
				if (nodeMap.getNamedItem("groupid") != null)
					temp[1] = nodeMap.getNamedItem("groupid").getNodeValue();
				if (nodeMap.getNamedItem("roleid") != null)
					temp[2] = nodeMap.getNamedItem("roleid").getNodeValue();
				WebServiceReferences.webServiceClient.GetRoleAccess(CallDispatcher.LoginUser, temp[1], temp[2], SingleInstance.mainContext);
			}
			list = doc.getElementsByTagName("Patientdischarge");
			if (list.getLength() > 0) {
				Log.i("patientdetails","XML PARSER RoleAccess SIGNAL");
				String[] temp = new String[4];
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("groupOwner") != null)
					temp[0] = nodeMap.getNamedItem("groupOwner").getNodeValue();
				if (nodeMap.getNamedItem("groupId") != null)
					temp[1] = nodeMap.getNamedItem("groupId").getNodeValue();
				if (nodeMap.getNamedItem("groupName") != null)
					temp[2] = nodeMap.getNamedItem("groupName").getNodeValue();
				if (nodeMap.getNamedItem("patientid") != null)
					temp[3] = nodeMap.getNamedItem("patientid").getNodeValue();
				DBAccess.getdbHeler().deletePatientRelatedDetails(temp[1], temp[3]);
			}

			list = doc.getElementsByTagName("EditForm");
			String[] editForms = null;
			if (list.getLength() > 0) {
				editForms = new String[2];
				node = list.item(0);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("userid") != null)
					editForms[0] = nodeMap.getNamedItem("userid")
							.getNodeValue();
				if (nodeMap.getNamedItem("formid") != null)
					editForms[1] = nodeMap.getNamedItem("formid")
							.getNodeValue();

				String result[] = getType(xml);
				sb.setType(result[0]);
				sb.setSid(result[1]);
				sb.setEid(result[2]);
				sb.setText(null);
				sb.setEditForm(editForms);
			}
			list = doc.getElementsByTagName("tl");
			if (list.getLength() > 0) {
				GroupChatBean gChatBean = parseGroupChatXML(xml);
				sb.setResponseObject(gChatBean);
				sb.setText(null);
			}

			String result[] = getType(xml);
			sb.setType(result[0]);
			sb.setSid(result[1]);
			sb.setEid(result[2]);

		} catch (Exception e) {
			Log.e("XMLPARSER", "Error Parsing UDP Signal");
			e.printStackTrace();
		}

		return sb;
	}

	private GroupChatBean parseGroupChatXML(String xml) {
		DocumentBuilderFactory builderFactory = null;
		DocumentBuilder builder = null;
		InputSource inputSource = null;
		Document document = null;
		Element element = null;
		GroupChatBean bean = null;
		CallDispatcher callDisp = null;
		try {
			if (xml != null && !xml.equals("")) {
				builderFactory = DocumentBuilderFactory.newInstance();
				builder = builderFactory.newDocumentBuilder();
				inputSource = new InputSource();
				inputSource.setCharacterStream(new StringReader(xml));
				document = (Document) builder.parse(inputSource);
				element = (Element) document.getElementsByTagName("com")
						.item(0);
				bean = new GroupChatBean();
				if (WebServiceReferences.callDispatch.containsKey("calldisp"))
					callDisp = (CallDispatcher) WebServiceReferences.callDispatch
							.get("calldisp");
				else
					callDisp = new CallDispatcher(
							SipNotificationListener.getCurrentContext());
				if (element != null) {
					String result = element.getAttribute("result").trim();
					String type = element.getAttribute("type").trim();
					if (result != null && !result.trim().equals("")) {
						bean.setResult(result);
					}
					if (type != null && !type.trim().equals("")) {
						bean.setType(type);
					}
				}
				element = (Element) document.getElementsByTagName("tl").item(0);
				if (element != null) {

					String mimetype = element.getAttribute("mimetype").trim();
					String category = element.getAttribute("category").trim();
					String signalid = element.getAttribute("signalid").trim();
					String from = element.getAttribute("from").trim();
					String to = element.getAttribute("to").trim();
					String sessionid = element.getAttribute("sessionid").trim();
					String parentId = element.getAttribute("parentid").trim();

					if (mimetype != null && !mimetype.equals("")) {
						bean.setMimetype(mimetype);
					}
					if (category != null && !category.equals("")) {
						bean.setCategory(category);
					}
					if (parentId != null && !parentId.equals("")) {
						bean.setParentId(parentId);
					}
					if (from != null && !from.equals("")) {
						bean.setFrom(from);
					}
					if (to != null && !to.equals("")) {
						bean.setTo(to);
					}
					if (signalid != null && !signalid.equals("")) {
						bean.setSignalid(signalid);
					}
					if (sessionid != null && !sessionid.equals("")) {
						bean.setSessionid(sessionid);
						bean.setGroupId(sessionid);
					}
					if (bean.getType().equals("101")) {
						String senttimez = element.getAttribute("datetimewithzone")
								.trim();
						bean.setSenttimez(senttimez);
					}
					if (bean.getType().equals("100")) {

						String subcategory = element
								.getAttribute("subcategory").trim();

						String senttimez = element.getAttribute("senttimez")
								.trim();
						String senttime = element.getAttribute("senttime")
								.trim();

						String privatemembers = element
								.getAttribute("privatemembers");

						String reminderTime = element
								.getAttribute("remindertime");

						if (subcategory != null && !subcategory.equals("")) {
							bean.setSubCategory(subcategory);
						}
						if (privatemembers != null
								&& !privatemembers.equals("")) {
							bean.setPrivateMembers(privatemembers);
						}

						if (senttime != null && !senttime.equals("")) {
							bean.setSenttime(senttime);
						}
						if (senttimez != null && !senttimez.equals("")) {
							bean.setSenttimez(senttimez);
						}
						if (reminderTime != null && !reminderTime.equals("")) {
							bean.setReminderTime(reminderTime);
						}
						element = (Element) document.getElementsByTagName(
								"message").item(0);
						if (element != null) {
							String message = getCharacterDataFromElement(element);
							if (message != null && !message.trim().equals("")) {
								bean.setMessage(message);
							}
						}
						element = (Element) document.getElementsByTagName(
								"media").item(0);
						if (element != null) {
							String filename = element.getAttribute("filename")
									.trim();
							if (filename != null && !filename.equals("")) {
								bean.setMediaName(filename);
							}
							String filecomment = element.getAttribute("comment")
									.trim();
							if (filecomment != null && !filecomment.equals("")) {
								bean.setComment(filecomment);
							}
							String filetitle = element.getAttribute("filetitle")
									.trim();
							if (filetitle != null && !filetitle.equals("")) {
								bean.setFiletitle(filetitle);
							}

							String ftpusername = element.getAttribute(
									"ftpusername").trim();
							if (ftpusername != null && !ftpusername.equals("")) {
								bean.setFtpUsername(ftpusername);
							}
							String ftppassword = element.getAttribute(
									"ftppassword").trim();
							if (ftppassword != null && !ftppassword.equals("")) {
								bean.setFtpPassword(ftppassword);
							}
							bean.setThumb(2);
						}
					} else if (bean.getType().equals("104")) {

						String pSignalId = element.getAttribute("psignalid")
								.trim();
						String dateandtime = element
								.getAttribute("dateandtime").trim();

						if (pSignalId != null && !pSignalId.equals("")) {
							bean.setpSingnalId(pSignalId);
						}

						if (dateandtime != null && !dateandtime.equals("")) {
							bean.setSenttime(dateandtime);
						}
					}

				}
				return bean;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private java.lang.String getCharacterDataFromElement(Element element) {
        Node child = element.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            try {
                if(!cd.getData().trim().equals(null)) {
                    byte[] data = Base64.decode(cd.getData().trim(), Base64.DEFAULT);
                    String text = new String(data, "UTF-8");
                    return text;
                }
            }catch(Exception e){
                Log.d("Error","Error => "+e.toString());
            }
            return cd.getData().trim();
        }
        return null;
	}

	public String[] parseResponseForCallConfiguration(String xml) {
		String[] respons = new String[2];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("Response");
			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("userid") != null)
				respons[0] = nodeMap.getNamedItem("userid").getNodeValue();

			list = doc.getElementsByTagName("text");
			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("text") != null)
				respons[1] = nodeMap.getNamedItem("text").getNodeValue();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return respons;

	}

	public WebServiceBean parseForgetPasswordResultFromXml(String xml) {
		// TODO Auto-generated method stub
		WebServiceBean serviceBean = new WebServiceBean();
		if (getResult(xml)) {

			serviceBean.setResult("1");
			try {
				dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
				is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				doc = (Document) db.parse(is);
				list = doc.getElementsByTagName("text");
				node = list.item(0);
				nodeMap = node.getAttributes();
				Log.d("zxz", "received lo "
						+ nodeMap.getNamedItem("question").getNodeValue());
				serviceBean.setText(nodeMap.getNamedItem("question")
						.getNodeValue());
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("zxz", "received lo Exception .class..............");
			}

		} else {
			serviceBean.setResult("0");
			try {
				dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
				is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				doc = (Document) db.parse(is);
				list = doc.getElementsByTagName("text");
				node = list.item(0);
				nodeMap = node.getAttributes();
				Log.d("zxz", "received lo "
						+ nodeMap.getNamedItem("text").getNodeValue());
				serviceBean
						.setText(nodeMap.getNamedItem("text").getNodeValue());
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("zxz", "received lo Exception .class..............");
			}
		}

		return serviceBean;

	}

	public ArrayList<BuddyInformationBean> parseAcceptResponse(String xml) {

		ArrayList<BuddyInformationBean> result = new ArrayList<BuddyInformationBean>();

		//
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("AcceptBuddy");
			// buddyList = new ArrayList<Object>();

			// for (int i = 0; i < list.getLength(); i++) {
			BuddyInformationBean bib = new BuddyInformationBean();
			node = list.item(0);
			nodeMap = node.getAttributes();
			bib.setName(nodeMap.getNamedItem("name").getNodeValue());

			bib.setLocalipaddress(nodeMap.getNamedItem("localipaddress")
					.getNodeValue());

			bib.setExternalipaddress(nodeMap.getNamedItem("externalipaddress")
					.getNodeValue());

			bib.setStatus(nodeMap.getNamedItem("status").getNodeValue());

			// bib.setType(nodeMap.getNamedItem("type").getNodeValue());

			bib.setSignalingPort((nodeMap.getNamedItem("signalingport")
					.getNodeValue()));

			if (nodeMap.getNamedItem("lat") != null) {
				bib.setLatitude(nodeMap.getNamedItem("lat").getNodeValue());
			}
			if (nodeMap.getNamedItem("long") != null) {
				bib.setLongitude(nodeMap.getNamedItem("long").getNodeValue());
			}
			if (nodeMap.getNamedItem("occupation") != null) {
				bib.setOccupation(nodeMap.getNamedItem("occupation")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("firstname") != null) {
				bib.setFirstname(nodeMap.getNamedItem("firstname")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("lastname") != null) {
				bib.setLastname(nodeMap.getNamedItem("lastname")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("nickname") != null) {
				bib.setNickname(nodeMap.getNamedItem("nickname")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("distance") != null) {
				String distance = nodeMap.getNamedItem("distance")
						.getNodeValue().trim();
				bib.setDistance(distance);
			} else
				bib.setDistance("nil");
			// System.out.println("bib.getType?"+bib.getType());
			result.add(bib);
			WebServiceReferences.webServiceClient.GetAllProfile(CallDispatcher.LoginUser,bib.getName()
					,SingleInstance.mainContext);
			// }

			//

			// if (getResult(xml)) {
			// serviceBean.setResult("1");
			// } else {
			// serviceBean.setResult("0");
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		//
		return result;
	}

	public String[] parseWithDrawnPermissionXML(String xml) {
		String[] results = null;
		Log.i("xml", "====> inside witdrawn == > " + xml);
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("com");
			results = new String[3];
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(0);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("result") != null) {
					results[0] = (nodeMap.getNamedItem("result").getNodeValue());
				}
			}
			list = doc.getElementsByTagName("text");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(0);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("text") != null) {
					results[1] = (nodeMap.getNamedItem("text").getNodeValue());
				}
				if (nodeMap.getNamedItem("buddyid") != null) {
					results[1] = (nodeMap.getNamedItem("buddyid")
							.getNodeValue());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	public ArrayList<Object> parseGetMyConfigurationDetails(String xml) {
		ArrayList<Object> response_list = new ArrayList<Object>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);

			list = doc.getElementsByTagName("text");
			node = list.item(0);
			if (node != null) {
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("text") != null) {
					String result;
					result = nodeMap.getNamedItem("text").getNodeValue();
					response_list.add(result);

					list = doc.getElementsByTagName("ConfigurationDetails");
					node = list.item(0);
					nodeMap = node.getAttributes();

					String[] config_details = new String[2];
					if (nodeMap.getNamedItem("userid") != null)
						config_details[0] = nodeMap.getNamedItem("userid")
								.getNodeValue();

					if (nodeMap.getNamedItem("id") != null)
						config_details[1] = nodeMap.getNamedItem("id")
								.getNodeValue();

					response_list.add(config_details);

				}
			} else {
				list = doc.getElementsByTagName("ConfigurationDetails");
				node = list.item(0);
				nodeMap = node.getAttributes();

				String[] config_details = new String[2];
				if (nodeMap.getNamedItem("userid") != null)
					config_details[0] = nodeMap.getNamedItem("userid")
							.getNodeValue();

				if (nodeMap.getNamedItem("id") != null)
					config_details[1] = nodeMap.getNamedItem("id")
							.getNodeValue();

				response_list.add(config_details);

				ArrayList<OfflineRequestConfigBean> config_list = null;
				list = doc.getElementsByTagName("ConfigField");
				if (list.getLength() > 0) {
					config_list = new ArrayList<OfflineRequestConfigBean>();
					for (int i = 0; i < list.getLength(); i++) {
						node = list.item(i);
						nodeMap = node.getAttributes();
						OfflineRequestConfigBean bean = new OfflineRequestConfigBean();

						if (nodeMap.getNamedItem("id") != null)
							bean.setId(nodeMap.getNamedItem("id")
									.getNodeValue());

						if (nodeMap.getNamedItem("userid") != null)
							bean.setUserId(nodeMap.getNamedItem("userid")
									.getNodeValue());

						if (nodeMap.getNamedItem("buddyid") != null)
							bean.setBuddyId(nodeMap.getNamedItem("buddyid")
									.getNodeValue());

						if (nodeMap.getNamedItem("messagetitle") != null)
							bean.setMessageTitle(nodeMap.getNamedItem(
									"messagetitle").getNodeValue());

						if (nodeMap.getNamedItem("messagetype") != null)
							bean.setMessagetype(nodeMap.getNamedItem(
									"messagetype").getNodeValue());

						if (nodeMap.getNamedItem("message") != null)
							bean.setMessage(nodeMap.getNamedItem("message")
									.getNodeValue());

						if (nodeMap.getNamedItem("responsetype") != null)
							bean.setResponseType(nodeMap.getNamedItem(
									"responsetype").getNodeValue());

						if (nodeMap.getNamedItem("url") != null)
							bean.setUrl(nodeMap.getNamedItem("url")
									.getNodeValue());

						if (nodeMap.getNamedItem("when") != null)
							bean.setWhen(nodeMap.getNamedItem("when")
									.getNodeValue());
						if (nodeMap.getNamedItem("recordtime") != null)
							bean.setRecordTime(nodeMap.getNamedItem(
									"recordtime").getNodeValue());

						config_list.add(bean);

					}

				}
				response_list.add(config_list);

				ArrayList<OfflineRequestConfigBean> default_list = null;
				list = doc.getElementsByTagName("DefaultConfigField");
				if (list.getLength() > 0) {
					default_list = new ArrayList<OfflineRequestConfigBean>();
					for (int i = 0; i < list.getLength(); i++) {
						node = list.item(i);
						nodeMap = node.getAttributes();
						OfflineRequestConfigBean bean = new OfflineRequestConfigBean();

						if (nodeMap.getNamedItem("id") != null)
							bean.setId(nodeMap.getNamedItem("id")
									.getNodeValue());

						if (nodeMap.getNamedItem("userid") != null)
							bean.setUserId(nodeMap.getNamedItem("userid")
									.getNodeValue());

						if (nodeMap.getNamedItem("buddyid") != null)
							bean.setBuddyId(nodeMap.getNamedItem("buddyid")
									.getNodeValue());

						if (nodeMap.getNamedItem("messagetitle") != null)
							bean.setMessageTitle(nodeMap.getNamedItem(
									"messagetitle").getNodeValue());

						if (nodeMap.getNamedItem("messagetype") != null)
							bean.setMessagetype(nodeMap.getNamedItem(
									"messagetype").getNodeValue());

						if (nodeMap.getNamedItem("message") != null)
							bean.setMessage(nodeMap.getNamedItem("message")
									.getNodeValue());

						if (nodeMap.getNamedItem("responsetype") != null)
							bean.setResponseType(nodeMap.getNamedItem(
									"responsetype").getNodeValue());

						if (nodeMap.getNamedItem("url") != null)
							bean.setUrl(nodeMap.getNamedItem("url")
									.getNodeValue());

						if (nodeMap.getNamedItem("when") != null)
							bean.setWhen(nodeMap.getNamedItem("when")
									.getNodeValue());

						if (nodeMap.getNamedItem("recordtime") != null)
							bean.setRecordTime(nodeMap.getNamedItem(
									"recordtime").getNodeValue());
						default_list.add(bean);

					}
				}
				response_list.add(default_list);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response_list;
	}

	public ArrayList<Object> parseOfflineCallResponse(String xml) {
		ArrayList<Object> result_list = new ArrayList<Object>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("CallResponse");
			node = list.item(0);
			nodeMap = node.getAttributes();

			String user_id = null;
			if (nodeMap.getNamedItem("userid") != null)
				user_id = nodeMap.getNamedItem("userid").getNodeValue();
			result_list.add(user_id);

			String buddy_id = null;
			if (nodeMap.getNamedItem("buddyid") != null)
				buddy_id = nodeMap.getNamedItem("buddyid").getNodeValue();
			result_list.add(buddy_id);

			ArrayList<OfflineRequestConfigBean> default_list = null;
			list = doc.getElementsByTagName("ResponseDetails");
			if (list.getLength() > 0) {
				default_list = new ArrayList<OfflineRequestConfigBean>();
				for (int i = 0; i < list.getLength(); i++) {
					node = list.item(i);
					nodeMap = node.getAttributes();
					OfflineRequestConfigBean bean = new OfflineRequestConfigBean();

					if (nodeMap.getNamedItem("id") != null)
						bean.setConfig_id(nodeMap.getNamedItem("id")
								.getNodeValue());

					if (nodeMap.getNamedItem("userid") != null)
						bean.setUserId(nodeMap.getNamedItem("userid")
								.getNodeValue());

					if (nodeMap.getNamedItem("buddyid") != null)
						bean.setBuddyId(nodeMap.getNamedItem("buddyid")
								.getNodeValue());

					if (nodeMap.getNamedItem("messagetitle") != null)
						bean.setMessageTitle(nodeMap.getNamedItem(
								"messagetitle").getNodeValue());

					if (nodeMap.getNamedItem("messagetype") != null)
						bean.setMessagetype(nodeMap.getNamedItem("messagetype")
								.getNodeValue());

					if (nodeMap.getNamedItem("message") != null)
						bean.setMessage(nodeMap.getNamedItem("message")
								.getNodeValue());

					if (nodeMap.getNamedItem("responsetype") != null)
						bean.setResponseType(nodeMap.getNamedItem(
								"responsetype").getNodeValue());

					if (nodeMap.getNamedItem("url") != null)
						bean.setUrl(nodeMap.getNamedItem("url").getNodeValue());
					default_list.add(bean);
				}
			}
			result_list.add(default_list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result_list;
	}

	@SuppressWarnings("finally")
	public String[] parseSetPermissionXML(String xml) {
		String[] result = new String[3];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("text");
			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("text") != null)
				result[0] = nodeMap.getNamedItem("text").getNodeValue();
			if (nodeMap.getNamedItem("userid") != null)
				result[1] = nodeMap.getNamedItem("userid").getNodeValue();
			if (nodeMap.getNamedItem("buddyid") != null)
				result[2] = nodeMap.getNamedItem("buddyid").getNodeValue();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return result;
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<PermissionBean> parseGetallPermissionResult(String xml) {
		ArrayList<PermissionBean> permissionlist = new ArrayList<PermissionBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("PermissionDetails");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				PermissionBean bean = new PermissionBean();
				if (nodeMap.getNamedItem("userid") != null)
					bean.setUserId(nodeMap.getNamedItem("userid")
							.getNodeValue());
				if (nodeMap.getNamedItem("buddyid") != null)
					bean.setBuddyId(nodeMap.getNamedItem("buddyid")
							.getNodeValue());
				if (nodeMap.getNamedItem("audiocall") != null)
					bean.setAudio_call(nodeMap.getNamedItem("audiocall")
							.getNodeValue());
				if (nodeMap.getNamedItem("videocall") != null)
					bean.setVideo_call(nodeMap.getNamedItem("videocall")
							.getNodeValue());
				if (nodeMap.getNamedItem("audiobroadcast") != null)
					bean.setABC(nodeMap.getNamedItem("audiobroadcast")
							.getNodeValue());
				if (nodeMap.getNamedItem("videobroadcast") != null)
					bean.setVBC(nodeMap.getNamedItem("videobroadcast")
							.getNodeValue());
				if (nodeMap.getNamedItem("audiounicast") != null)
					bean.setAUC(nodeMap.getNamedItem("audiounicast")
							.getNodeValue());
				if (nodeMap.getNamedItem("videounicast") != null)
					bean.setVUC(nodeMap.getNamedItem("videounicast")
							.getNodeValue());
				if (nodeMap.getNamedItem("mmchat") != null)
					bean.setMMchat(nodeMap.getNamedItem("mmchat")
							.getNodeValue());
				if (nodeMap.getNamedItem("textmessage") != null)
					bean.setTextMessage(nodeMap.getNamedItem("textmessage")
							.getNodeValue());
				if (nodeMap.getNamedItem("photomessage") != null)
					bean.setPhotoMessage(nodeMap.getNamedItem("photomessage")
							.getNodeValue());
				if (nodeMap.getNamedItem("audiomessage") != null)
					bean.setAudioMessage(nodeMap.getNamedItem("audiomessage")
							.getNodeValue());
				if (nodeMap.getNamedItem("videomessage") != null)
					bean.setVideoMessage(nodeMap.getNamedItem("videomessage")
							.getNodeValue());
				if (nodeMap.getNamedItem("sketchmessage") != null)
					bean.setSketchMessage(nodeMap.getNamedItem("sketchmessage")
							.getNodeValue());
				if (nodeMap.getNamedItem("viewprofile") != null)
					bean.setViewProfile(nodeMap.getNamedItem("viewprofile")
							.getNodeValue());
				if (nodeMap.getNamedItem("shareprofile") != null)
					bean.setShareProfile(nodeMap.getNamedItem("shareprofile")
							.getNodeValue());
				if (nodeMap.getNamedItem("answeringmachine") != null)
					bean.setAvtaar(nodeMap.getNamedItem("answeringmachine")
							.getNodeValue());
				if (nodeMap.getNamedItem("formshare") != null)
					bean.setFormshare(nodeMap.getNamedItem("formshare")
							.getNodeValue());
				if (nodeMap.getNamedItem("hostedconference") != null)
					bean.setHostedconf(nodeMap.getNamedItem("hostedconference")
							.getNodeValue());
				if (nodeMap.getNamedItem("recordtime") != null)
					bean.setRecordTime(nodeMap.getNamedItem("recordtime")
							.getNodeValue());
				permissionlist.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return permissionlist;
		}
	}

	public String[] parseDeletemyResponse(String xml) {
		String[] delete_response = new String[3];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("ResponseDeleted");
			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("userid") != null)
				delete_response[0] = nodeMap.getNamedItem("userid")
						.getNodeValue();

			list = doc.getElementsByTagName("text");
			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("text") != null)
				delete_response[1] = nodeMap.getNamedItem("text")
						.getNodeValue();
			if (nodeMap.getNamedItem("id") != null)
				delete_response[2] = nodeMap.getNamedItem("id").getNodeValue();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return delete_response;
	}

	public OfflineConfigurationBean responseForCallConfigurationInfo(
			String input) throws Exception {

		OfflineConfigurationBean configurationBean = null;
		DocumentBuilderFactory dbf1 = null;
		DocumentBuilder db1 = null;
		InputSource is1 = null;
		Document doc1 = null;
		Element el = null;
		try {
			configurationBean = new OfflineConfigurationBean();
			dbf1 = DocumentBuilderFactory.newInstance();
			db1 = dbf1.newDocumentBuilder();
			is1 = new InputSource();
			is1.setCharacterStream(new StringReader(input));
			doc1 = (Document) db1.parse(is1);

			el = (Element) doc1.getElementsByTagName("offlineconfig").item(0);
			configurationBean.setUserId(el.getAttribute("userid").toLowerCase()
					.trim());

			NodeList nodeList = el.getElementsByTagName("text");

			ArrayList<OfflineConfigResponseBean> list = new ArrayList<OfflineConfigResponseBean>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				OfflineConfigResponseBean configBean = new OfflineConfigResponseBean();
				Node configNode = nodeList.item(i);
				Element element = (Element) configNode;
				configBean.setText(element.getAttribute("text").trim());
				configBean.setId(element.getAttribute("id"));
				configBean.setRecordTime(element.getAttribute("recordtime"));

				list.add(configBean);
			}
			configurationBean.setList(list);

		} catch (Exception e) {
			// e.printStackTrace();
			throw new Exception("Insufficient arguments");
		}
		return configurationBean;
	}

	public ArrayList<Object> parseGetProfileTemplateResponse(String xml) {
		ArrayList<Object> result_list = new ArrayList<Object>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			ArrayList<ProfileTemplateBean> profileTemplateList = null;

			list = doc.getElementsByTagName("text");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				String result = null;
				if (nodeMap.getNamedItem("text") != null)
					result = nodeMap.getNamedItem("text").getNodeValue();

				result_list.add(result);

			} else {
				list = doc.getElementsByTagName("Profiletemplate");
				if (list.getLength() > 0) {
					profileTemplateList = new ArrayList<ProfileTemplateBean>();
					for (int i = 0; i < list.getLength(); i++) {
						node = list.item(i);
						nodeMap = node.getAttributes();
						ProfileTemplateBean bean = new ProfileTemplateBean();
						if (nodeMap.getNamedItem("profileid") != null) {
							bean.setProfileId(nodeMap.getNamedItem("profileid")
									.getNodeValue());
						}
						if (nodeMap.getNamedItem("profilename") != null) {
							bean.setProfileName(nodeMap.getNamedItem(
									"profilename").getNodeValue());
						}
						if (nodeMap.getNamedItem("createddate") != null) {
							bean.setCreatedDate(nodeMap.getNamedItem(
									"createddate").getNodeValue());
						}

						if (nodeMap.getNamedItem("modifieddate") != null) {
							bean.setModifiedDate(nodeMap.getNamedItem(
									"modifieddate").getNodeValue());

						}
						profileTemplateList.add(bean);
					}
				}
				result_list.add(profileTemplateList);

				Vector<FieldTemplateBean> default_list = null;
				list = doc.getElementsByTagName("Fieldtemplate");
				if (list.getLength() > 0) {
					default_list = new Vector<FieldTemplateBean>();
					for (int i = 0; i < list.getLength(); i++) {
						node = list.item(i);
						nodeMap = node.getAttributes();
						FieldTemplateBean bean = new FieldTemplateBean();

						if (nodeMap.getNamedItem("groupname") != null)
							bean.setGroupName(nodeMap.getNamedItem("groupname")
									.getNodeValue());

						if (nodeMap.getNamedItem("groupid") != null)
							bean.setGroupId(nodeMap.getNamedItem("groupid")
									.getNodeValue());

						if (nodeMap.getNamedItem("fieldid") != null)
							bean.setFieldId(nodeMap.getNamedItem("fieldid")
									.getNodeValue());

						if (nodeMap.getNamedItem("fieldname") != null)
							bean.setFieldName(nodeMap.getNamedItem("fieldname")
									.getNodeValue());

						if (nodeMap.getNamedItem("fieldtype") != null)
							bean.setFieldType(nodeMap.getNamedItem("fieldtype")
									.getNodeValue());

						if (nodeMap.getNamedItem("createddate") != null)
							bean.setCreatedDate(nodeMap.getNamedItem(
									"createddate").getNodeValue());

						if (nodeMap.getNamedItem("modifieddate") != null)
							bean.setModifiedDate(nodeMap.getNamedItem(
									"modifieddate").getNodeValue());
						default_list.add(bean);
					}
				}
				result_list.add(default_list);
				Log.i("xml",
						"profile total size =======> " + result_list.size());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result_list;
	}

	public ArrayList<Object> parseSetProfileResponse(String xml) {
		ArrayList<Object> result_list = new ArrayList<Object>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			ArrayList<ProfileTemplateBean> profileTemplateList = null;
			list = doc.getElementsByTagName("Profiledetail");
			if (list.getLength() > 0) {
				profileTemplateList = new ArrayList<ProfileTemplateBean>();
				for (int i = 0; i < list.getLength(); i++) {
					node = list.item(i);
					nodeMap = node.getAttributes();
					ProfileTemplateBean bean = new ProfileTemplateBean();
					if (nodeMap.getNamedItem("profileid") != null) {
						bean.setProfileId(nodeMap.getNamedItem("profileid")
								.getNodeValue());
					}
					if (nodeMap.getNamedItem("createddate") != null) {
						bean.setCreatedDate(nodeMap.getNamedItem("createddate")
								.getNodeValue());
					}

					if (nodeMap.getNamedItem("modifieddate") != null) {
						bean.setModifiedDate(nodeMap.getNamedItem(
								"modifieddate").getNodeValue());

					}
					profileTemplateList.add(bean);
				}
			}
			result_list.add(profileTemplateList);

			Vector<FieldTemplateBean> default_list = null;
			list = doc.getElementsByTagName("Fielddetail");
			if (list.getLength() > 0) {
				default_list = new Vector<FieldTemplateBean>();
				for (int i = 0; i < list.getLength(); i++) {
					node = list.item(i);
					nodeMap = node.getAttributes();
					FieldTemplateBean bean = new FieldTemplateBean();

					if (nodeMap.getNamedItem("fieldid") != null)
						bean.setFieldId(nodeMap.getNamedItem("fieldid")
								.getNodeValue());

					if (nodeMap.getNamedItem("status") != null)
						bean.setStatus(nodeMap.getNamedItem("status")
								.getNodeValue());

					default_list.add(bean);
				}
			}
			result_list.add(default_list);
			Log.i("xml", "profile total size =======> " + result_list.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result_list;
	}

	@SuppressWarnings("finally")
	public UtilityResponse parseUtilityresponse(String xml) {
		UtilityResponse utilityResponse = new UtilityResponse();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("utilityList");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("utilityId") != null)
					utilityResponse.setUtility_id(nodeMap.getNamedItem(
							"utilityId").getNodeValue());
				if (nodeMap.getNamedItem("resultCount") != null)
					utilityResponse.setNoofresults(nodeMap.getNamedItem(
							"resultCount").getNodeValue());
				if (nodeMap.getNamedItem("postedDate") != null)
					utilityResponse.setPosted_date(nodeMap.getNamedItem(
							"postedDate").getNodeValue());
			}
			list = null;
			node = null;
			nodeMap = null;

			list = doc.getElementsByTagName("utilityEdit");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("utilityId") != null)
					utilityResponse.setEditedutility_id(nodeMap.getNamedItem(
							"utilityId").getNodeValue());
				if (nodeMap.getNamedItem("resultCount") != null)
					utilityResponse.setNoofresults(nodeMap.getNamedItem(
							"resultCount").getNodeValue());
				if (nodeMap.getNamedItem("postedDate") != null)
					utilityResponse.setPosted_date(nodeMap.getNamedItem(
							"postedDate").getNodeValue());
			}
			list = null;
			node = null;
			nodeMap = null;

			list = doc.getElementsByTagName("utilityDelete");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("utilityId") != null)
					utilityResponse.setDeletedutility_id(nodeMap.getNamedItem(
							"utilityId").getNodeValue());
				if (nodeMap.getNamedItem("text") != null)
					utilityResponse.setMessage(nodeMap.getNamedItem("text")
							.getNodeValue());
				if (nodeMap.getNamedItem("postedDate") != null)
					utilityResponse.setPosted_date(nodeMap.getNamedItem(
							"postedDate").getNodeValue());
			}
			list = null;
			node = null;
			nodeMap = null;

			ArrayList<UtilityBean> res_list = null;
			list = doc.getElementsByTagName("utilityItem");
			Log.d("utility", "Utility item length--->" + list.getLength());
			if (list.getLength() > 0)
				res_list = new ArrayList<UtilityBean>();
			for (int i = 0; i < list.getLength(); i++) {
				Log.d("utility", "Inside for loop i--->" + i);
				node = list.item(i);
				nodeMap = node.getAttributes();
				UtilityBean bean = new UtilityBean();
				if (nodeMap.getNamedItem("id") != null)
					bean.setId(Integer.parseInt(nodeMap.getNamedItem("id")
							.getNodeValue().trim()));
				if (nodeMap.getNamedItem("userid") != null)
					bean.setUsername(nodeMap.getNamedItem("userid")
							.getNodeValue());
				if (nodeMap.getNamedItem("productname") != null)
					bean.setProduct_name(nodeMap.getNamedItem("productname")
							.getNodeValue());
				if (nodeMap.getNamedItem("price") != null)
					bean.setPrice(nodeMap.getNamedItem("price").getNodeValue());
				if (nodeMap.getNamedItem("quantity") != null)
					bean.setQty(nodeMap.getNamedItem("quantity").getNodeValue());
				if (nodeMap.getNamedItem("address") != null)
					bean.setAddress(nodeMap.getNamedItem("address")
							.getNodeValue());
				if (nodeMap.getNamedItem("location") != null)
					bean.setLocation(nodeMap.getNamedItem("location")
							.getNodeValue());
				if (nodeMap.getNamedItem("orgname") != null)
					bean.setNameororg(nodeMap.getNamedItem("orgname")
							.getNodeValue());
				if (nodeMap.getNamedItem("name") != null
						&& nodeMap.getNamedItem("name").toString().length() > 0)
					bean.setNameororg(nodeMap.getNamedItem("name")
							.getNodeValue());
				if (nodeMap.getNamedItem("city") != null)
					bean.setCityordist(nodeMap.getNamedItem("city")
							.getNodeValue());
				if (nodeMap.getNamedItem("state") != null)
					bean.setState(nodeMap.getNamedItem("state").getNodeValue());
				if (nodeMap.getNamedItem("country") != null)
					bean.setCountry(nodeMap.getNamedItem("country")
							.getNodeValue());
				if (nodeMap.getNamedItem("pin") != null)
					bean.setPin(nodeMap.getNamedItem("pin").getNodeValue());
				if (nodeMap.getNamedItem("phone") != null)
					bean.setC_no(nodeMap.getNamedItem("phone").getNodeValue());
				if (nodeMap.getNamedItem("posteddate") != null)
					bean.setPosted_date(nodeMap.getNamedItem("posteddate")
							.getNodeValue());
				if (nodeMap.getNamedItem("audio") != null)
					bean.setAudiofilename(nodeMap.getNamedItem("audio")
							.getNodeValue());
				if (nodeMap.getNamedItem("video") != null)
					bean.setVideofilename(nodeMap.getNamedItem("video")
							.getNodeValue());
				if (nodeMap.getNamedItem("photo") != null)
					bean.setImag_filename(nodeMap.getNamedItem("photo")
							.getNodeValue());
				if (nodeMap.getNamedItem("mode") != null)
					bean.setMode(Integer.parseInt(nodeMap.getNamedItem("mode")
							.getNodeValue().trim()));
				if (nodeMap.getNamedItem("latlong") != null)
					bean.setLatlong(nodeMap.getNamedItem("latlong")
							.getNodeValue());
				if (nodeMap.getNamedItem("distance") != null)
					bean.setDistance(nodeMap.getNamedItem("distance")
							.getNodeValue());
				res_list.add(bean);
			}
			utilityResponse.setResult_list(res_list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return utilityResponse;
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<Object> parseGetStandaradfieldValues(String xml) {
		ArrayList<Object> field_list = new ArrayList<Object>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("Profiledetail");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();

				String[] profile_details = new String[4];
				if (nodeMap.getNamedItem("profileid") != null)
					profile_details[0] = nodeMap.getNamedItem("profileid")
							.getNodeValue();
				if (nodeMap.getNamedItem("profileowner") != null)
					profile_details[1] = nodeMap.getNamedItem("profileowner")
							.getNodeValue();
				if (nodeMap.getNamedItem("createddate") != null)
					profile_details[2] = nodeMap.getNamedItem("createddate")
							.getNodeValue();
				if (nodeMap.getNamedItem("modifieddate") != null)
					profile_details[3] = nodeMap.getNamedItem("modifieddate")
							.getNodeValue();
				field_list.add(profile_details);
			}
			list = doc.getElementsByTagName("Fielddetails");
			if (list.getLength() > 0) {
				ArrayList<FieldTemplateBean> values_list = new ArrayList<FieldTemplateBean>();
				for (int i = 0; i < list.getLength(); i++) {
					node = list.item(i);
					nodeMap = node.getAttributes();
					FieldTemplateBean bean = new FieldTemplateBean();
					if (nodeMap.getNamedItem("fieldid") != null)
						bean.setFieldId(nodeMap.getNamedItem("fieldid")
								.getNodeValue());
					if (nodeMap.getNamedItem("fieldvalue") != null)
						bean.setFiledvalue(nodeMap.getNamedItem("fieldvalue")
								.getNodeValue());
					if (nodeMap.getNamedItem("createddate") != null)
						bean.setField_createddate(nodeMap.getNamedItem(
								"createddate").getNodeValue());
					if (nodeMap.getNamedItem("modifieddate") != null)
						bean.setField_modifieddate(nodeMap.getNamedItem(
								"modifieddate").getNodeValue());
					values_list.add(bean);
				}
				field_list.add(values_list);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return field_list;
		}

	}

	public String[] parseDeleteProfile(String xml) {

		Log.i("xml", "Response xml" + xml);
		String[] values = new String[3];
		if (getResult(xml)) {

			values[0] = "1";
			try {
				dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
				is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				doc = (Document) db.parse(is);

				list = doc.getElementsByTagName("Profiledeleted");
				node = list.item(0);
				nodeMap = node.getAttributes();
				Log.d("profile", "received lo "
						+ nodeMap.getNamedItem("text").getNodeValue());
				values[1] = nodeMap.getNamedItem("text").getNodeValue();
				values[2] = nodeMap.getNamedItem("profileid").getNodeValue();
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("zxz", "received lo Exception .class..............");
			}

		} else {
			values[0] = "0";
			try {
				dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
				is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				doc = (Document) db.parse(is);
				list = doc.getElementsByTagName("text");
				node = list.item(0);
				nodeMap = node.getAttributes();
				Log.d("profile", "received lo "
						+ nodeMap.getNamedItem("text").getNodeValue());
				values[1] = nodeMap.getNamedItem("text").getNodeValue();
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("zxz", "received lo Exception .class..............");
			}
		}

		return values;
	}

	@SuppressWarnings("finally")
	public ArrayList<Object> parseGetBlockBuddyList(String xml) {

		Log.i("xml", "Response xml" + xml);
		ArrayList<Object> buddiesList = new ArrayList<Object>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("blockedBuddyList");
			node = list.item(0);
			nodeMap = node.getAttributes();
			if (nodeMap.getNamedItem("buddyList").getNodeValue() != null
					|| nodeMap.getNamedItem("buddyList").getNodeValue() != "") {
				String buddyName = nodeMap.getNamedItem("buddyList")
						.getNodeValue();
				if (buddyName.contains(",")) {
					String[] tousers = buddyName.split(",");
					for (int i = 0; i < tousers.length; i++) {
						buddiesList.add(tousers[i]);
					}

				} else {
					buddiesList.add(buddyName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("zxz", "received lo Exception .class..............");
		} finally {
			return buddiesList;
		}

	}

	@SuppressWarnings("finally")
	public Syncutilitybean parseGetUtilityItems(String xml) {

		Syncutilitybean syncbean = new Syncutilitybean();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("missingUtilityList");
			Log.d("utility",
					"my list count of missing utility--->" + list.getLength());
			if (list.getLength() > 0) {
				ArrayList<UtilityBean> values_list = new ArrayList<UtilityBean>();
				for (int i = 0; i < list.getLength(); i++) {
					node = list.item(i);
					NodeList nodeList = node.getChildNodes();
					Log.d("utility",
							"my list count of missing utility childnode count--->"
									+ nodeList.getLength());
					if (nodeList.getLength() > 0) {
						for (int j = 0; j < nodeList.getLength(); j++) {
							Log.d("utility", "node list node --->" + j);
							node = nodeList.item(j);
							nodeMap = node.getAttributes();
							UtilityBean bean = new UtilityBean();
							if (nodeMap.getNamedItem("id") != null)
								bean.setId(Integer.parseInt(nodeMap
										.getNamedItem("id").getNodeValue()));
							if (nodeMap.getNamedItem("userid") != null)
								bean.setUsername(nodeMap.getNamedItem("userid")
										.getNodeValue());
							if (nodeMap.getNamedItem("productname") != null)
								bean.setProduct_name(nodeMap.getNamedItem(
										"productname").getNodeValue());
							if (nodeMap.getNamedItem("utilityName") != null)
								bean.setUtility_name(nodeMap.getNamedItem(
										"utilityName").getNodeValue());
							if (nodeMap.getNamedItem("price") != null)
								bean.setPrice(nodeMap.getNamedItem("price")
										.getNodeValue());
							if (nodeMap.getNamedItem("quantity") != null)
								bean.setQty(nodeMap.getNamedItem("quantity")
										.getNodeValue());
							if (nodeMap.getNamedItem("address") != null)
								bean.setAddress(nodeMap.getNamedItem("address")
										.getNodeValue());
							if (nodeMap.getNamedItem("location") != null)
								bean.setLocation(nodeMap.getNamedItem(
										"location").getNodeValue());
							if (nodeMap.getNamedItem("orgname") != null)
								bean.setNameororg(nodeMap.getNamedItem(
										"orgname").getNodeValue());
							if (nodeMap.getNamedItem("city") != null)
								bean.setCityordist(nodeMap.getNamedItem("city")
										.getNodeValue());
							if (nodeMap.getNamedItem("state") != null)
								bean.setState(nodeMap.getNamedItem("state")
										.getNodeValue());
							if (nodeMap.getNamedItem("country") != null)
								bean.setCountry(nodeMap.getNamedItem("country")
										.getNodeValue());
							if (nodeMap.getNamedItem("pin") != null)
								bean.setPin(nodeMap.getNamedItem("pin")
										.getNodeValue());
							if (nodeMap.getNamedItem("phone") != null)
								bean.setC_no(nodeMap.getNamedItem("phone")
										.getNodeValue());
							if (nodeMap.getNamedItem("posteddate") != null)
								bean.setPosted_date(nodeMap.getNamedItem(
										"posteddate").getNodeValue());
							if (nodeMap.getNamedItem("audio") != null)
								bean.setAudiofilename(nodeMap.getNamedItem(
										"audio").getNodeValue());
							if (nodeMap.getNamedItem("video") != null)
								bean.setVideofilename(nodeMap.getNamedItem(
										"video").getNodeValue());
							if (nodeMap.getNamedItem("photo") != null)
								bean.setImag_filename(nodeMap.getNamedItem(
										"photo").getNodeValue());
							if (nodeMap.getNamedItem("mode") != null)
								bean.setMode(Integer.parseInt(nodeMap
										.getNamedItem("mode").getNodeValue()));
							values_list.add(bean);
						}

					}
				}
				if (values_list.size() > 0) {
					syncbean.setMissing_utility(values_list);
				}
			}
			list = doc.getElementsByTagName("updatedUtilityList");
			if (list.getLength() > 0) {
				ArrayList<UtilityBean> updatedList = new ArrayList<UtilityBean>();
				for (int i = 0; i < list.getLength(); i++) {
					node = list.item(i);
					NodeList nodeList = node.getChildNodes();
					if (nodeList.getLength() > 0) {
						for (int j = 0; j < nodeList.getLength(); j++) {
							node = nodeList.item(j);
							nodeMap = node.getAttributes();
							UtilityBean bean = new UtilityBean();
							if (nodeMap.getNamedItem("id") != null)
								bean.setId(Integer.parseInt(nodeMap
										.getNamedItem("id").getNodeValue()));
							if (nodeMap.getNamedItem("userid") != null)
								bean.setUsername(nodeMap.getNamedItem("userid")
										.getNodeValue());
							if (nodeMap.getNamedItem("productname") != null)
								bean.setProduct_name(nodeMap.getNamedItem(
										"productname").getNodeValue());
							if (nodeMap.getNamedItem("utilityName") != null)
								bean.setUtility_name(nodeMap.getNamedItem(
										"utilityName").getNodeValue());
							if (nodeMap.getNamedItem("price") != null)
								bean.setPrice(nodeMap.getNamedItem("price")
										.getNodeValue());
							if (nodeMap.getNamedItem("quantity") != null)
								bean.setQty(nodeMap.getNamedItem("quantity")
										.getNodeValue());
							if (nodeMap.getNamedItem("address") != null)
								bean.setAddress(nodeMap.getNamedItem("address")
										.getNodeValue());
							if (nodeMap.getNamedItem("location") != null)
								bean.setLocation(nodeMap.getNamedItem(
										"location").getNodeValue());
							if (nodeMap.getNamedItem("orgname") != null)
								bean.setNameororg(nodeMap.getNamedItem(
										"orgname").getNodeValue());
							if (nodeMap.getNamedItem("city") != null)
								bean.setCityordist(nodeMap.getNamedItem("city")
										.getNodeValue());
							if (nodeMap.getNamedItem("state") != null)
								bean.setState(nodeMap.getNamedItem("state")
										.getNodeValue());
							if (nodeMap.getNamedItem("country") != null)
								bean.setCountry(nodeMap.getNamedItem("country")
										.getNodeValue());
							if (nodeMap.getNamedItem("pin") != null)
								bean.setPin(nodeMap.getNamedItem("pin")
										.getNodeValue());
							if (nodeMap.getNamedItem("phone") != null)
								bean.setC_no(nodeMap.getNamedItem("phone")
										.getNodeValue());
							if (nodeMap.getNamedItem("posteddate") != null)
								bean.setPosted_date(nodeMap.getNamedItem(
										"posteddate").getNodeValue());
							if (nodeMap.getNamedItem("audio") != null)
								bean.setAudiofilename(nodeMap.getNamedItem(
										"audio").getNodeValue());
							if (nodeMap.getNamedItem("video") != null)
								bean.setVideofilename(nodeMap.getNamedItem(
										"video").getNodeValue());
							if (nodeMap.getNamedItem("photo") != null)
								bean.setImag_filename(nodeMap.getNamedItem(
										"photo").getNodeValue());
							if (nodeMap.getNamedItem("mode") != null)
								bean.setMode(Integer.parseInt(nodeMap
										.getNamedItem("mode").getNodeValue()));
							updatedList.add(bean);
						}
					}
				}
				syncbean.setUpdated_utility(updatedList);
			}
			list = doc.getElementsByTagName("deletedItem");
			if (list.getLength() > 0) {
				ArrayList<UtilityBean> deletedList = new ArrayList<UtilityBean>();
				UtilityBean[] deleteArray = null;
				for (int i = 0; i < list.getLength(); i++) {
					node = list.item(i);
					NodeList nodeList = node.getChildNodes();
					if (nodeList.getLength() > 0) {
						for (int j = 0; j < nodeList.getLength(); j++) {
							node = nodeList.item(j);
							nodeMap = node.getAttributes();
							UtilityBean bean = new UtilityBean();
							if (nodeMap.getNamedItem("id") != null)
								bean.setId(Integer.parseInt(nodeMap
										.getNamedItem("id").getNodeValue()));
							deletedList.add(bean);
							deleteArray = deletedList
									.toArray(new UtilityBean[deletedList.size()]);
						}
					}
				}
				if (deletedList.size() > 0) {
					syncbean.setDeleted_utility(deletedList);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return syncbean;
		}

	}

	@SuppressWarnings("finally")
	public String[] parseBlockUnblockresult(String xml) {
		String[] result = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("blockBuddy");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				result = new String[4];

				if (nodeMap.getNamedItem("userId") != null)
					result[0] = nodeMap.getNamedItem("userId").getNodeValue();

				if (nodeMap.getNamedItem("buddyId") != null)
					result[1] = nodeMap.getNamedItem("buddyId").getNodeValue();

				if (nodeMap.getNamedItem("key") != null)
					result[2] = nodeMap.getNamedItem("key").getNodeValue();

				if (nodeMap.getNamedItem("text") != null)
					result[3] = nodeMap.getNamedItem("text").getNodeValue();

			}

		} catch (Exception e) {

		} finally {
			return result;
		}
	}

	@SuppressWarnings("finally")
	public String[] parseshowhidelocResult(String xml) {
		String[] result = new String[2];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("ShowOrHideMyDistance");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				result[0] = "1";
				if (nodeMap.getNamedItem("text") != null)
					result[1] = nodeMap.getNamedItem("text").getNodeValue();
			} else {
				list = doc.getElementsByTagName("text");
				if (list.getLength() > 0) {
					node = list.item(0);
					nodeMap = node.getAttributes();
					result[0] = "0";
					if (nodeMap.getNamedItem("text") != null)
						result[1] = nodeMap.getNamedItem("text").getNodeValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return result;
		}
	}

	public ArrayList parseFormTemplate(String xml) {
		Log.i("xml", "Response xml" + xml);
		WebServiceBean serviceBean = new WebServiceBean();
		ArrayList<Object> buddyList = null;
		Log.i("SET", "parseFormTemplate" + xml);

		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);

			list = doc.getElementsByTagName("OwnUserForm");
			buddyList = new ArrayList<Object>();

			Log.i("b", "node size===>" + list.getLength());

			for (int i = 0; i < list.getLength(); i++) {
				Log.i("BLOB", "inside own user form");
				node = list.item(i);
				nodeMap = node.getAttributes();
				FormsListBean bean = ParseSigninForms(nodeMap);
				buddyList.add(bean);
				Log.i("b", "owner name,===>" + bean.getForm_owner());

				NodeList nlist = node.getChildNodes();
				for (int j = 0; j < nlist.getLength(); j++) {
					Node node = nlist.item(j);
					if (node != null) {
						Log.i("Forms", "Node Name===>" + node.getNodeName());
						Log.i("Forms", "Form Name===>" + bean.getForm_name());
						if (node.getNodeName().equalsIgnoreCase(
								"UserFormSettings")) {
							NamedNodeMap map = node.getAttributes();
							buddyList.add(ParseSettingforms(map));
						} else if (node.getNodeName().equalsIgnoreCase(
								"FormAttributes")) {
							NamedNodeMap map = node.getAttributes();
							buddyList.add(parseFormAttributes(map,
									bean.getForm_name()));
						}
					}

				}

			}
			list = doc.getElementsByTagName("UserForm");
			Log.i("b", "node size===>" + list.getLength());
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				FormsListBean bean = ParseSigninForms(nodeMap);
				buddyList.add(bean);
				Log.i("b", "owner name,===>" + bean.getForm_owner());
				NodeList nlist = node.getChildNodes();
				for (int j = 0; j < nlist.getLength(); j++) {
					Node node = nlist.item(j);
					if (node != null) {
						Log.i("Forms", "Node Name===>" + node.getNodeName());
						Log.i("Forms", "Form Name===>" + bean.getForm_name());
						if (node.getNodeName().equalsIgnoreCase(
								"UserFormSettings")) {
							NamedNodeMap map = node.getAttributes();
							buddyList.add(ParseSettingforms(map));
						} else if (node.getNodeName().equalsIgnoreCase(
								"FormAttributes")) {
							NamedNodeMap map = node.getAttributes();
							buddyList.add(parseFormAttributes(map,
									bean.getForm_name()));
						}
					}

				}

			}

			if (getResult(xml)) {
				serviceBean.setResult("1");
			} else {
				serviceBean.setResult("0");
			}
		} catch (Exception e) {
			Log.d("zxz", "Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return buddyList;
	}

	public GroupBean parseCreateGroup(String xml) {
		GroupBean bean = new GroupBean();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("CreateorModifyGroup");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("groupName") != null)
					bean.setGroupName(nodeMap.getNamedItem("groupName")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupId") != null)
					bean.setGroupId(nodeMap.getNamedItem("groupId")
							.getNodeValue());
				if (nodeMap.getNamedItem("createdDate") != null)
					bean.setCreatedDate(nodeMap.getNamedItem("createdDate")
							.getNodeValue());
				if (nodeMap.getNamedItem("modifiedDate") != null)
					bean.setModifiedDate(nodeMap.getNamedItem("modifiedDate")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupDescription") != null)
					bean.setGroupdescription(nodeMap.getNamedItem("groupDescription")
							.getNodeValue());
				if (nodeMap.getNamedItem("inActiveGroupMembers") != null)
					bean.setInActiveGroupMembers(nodeMap.getNamedItem("inActiveGroupMembers")
							.getNodeValue());
				if (nodeMap.getNamedItem("activeGroupMembers") != null)
					bean.setActiveGroupMembers(nodeMap.getNamedItem("activeGroupMembers")
							.getNodeValue());
				if (nodeMap.getNamedItem("inviteActiveMembers") != null)
					bean.setInviteMembers(nodeMap.getNamedItem("inviteActiveMembers")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupIcon") != null)
					bean.setGroupIcon(nodeMap.getNamedItem("groupIcon")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupType") != null)
					bean.setGrouptype(nodeMap.getNamedItem("groupType")
							.getNodeValue());
				if (nodeMap.getNamedItem("adminMember") != null)
					bean.setAdminMember(nodeMap.getNamedItem("adminMember")
							.getNodeValue());

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return bean;
		}
	}

	public GroupBean parseDeleteGroup(String xml) {
		GroupBean bean = new GroupBean();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("GroupDelete");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("text") != null)
					bean.setResult(nodeMap.getNamedItem("text").getNodeValue());
				if (nodeMap.getNamedItem("groupId") != null)
					bean.setGroupId(nodeMap.getNamedItem("groupId")
							.getNodeValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return bean;
		}
	}

	public GroupBean parseGroupMembersDelete(String xml) {
		GroupBean bean = new GroupBean();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("GroupMembersDelete");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("text") != null)
					bean.setResult(nodeMap.getNamedItem("text").getNodeValue());
				if (nodeMap.getNamedItem("groupId") != null)
					bean.setGroupId(nodeMap.getNamedItem("groupId")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupName") != null)
					bean.setGroupName(nodeMap.getNamedItem("groupName")
							.getNodeValue());
				if (nodeMap.getNamedItem("deletedGroupMembers") != null)
					bean.setDeleteGroupMembers(nodeMap.getNamedItem(
							"deletedGroupMembers").getNodeValue());
				if (nodeMap.getNamedItem("nonGroupMembers") != null)
					bean.setNonGroupMembers(nodeMap.getNamedItem(
							"nonGroupMembers").getNodeValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return bean;
		}
	}

	public Vector<GroupBean> parseGetGroupAndMembers(String xml) {
		Vector<GroupBean> response_list = new Vector<GroupBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("OwnGroupDetails");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				GroupBean bean = new GroupBean();
				bean.setOwnerName(CallDispatcher.LoginUser);

				if (nodeMap.getNamedItem("groupId") != null)
					bean.setGroupId(nodeMap.getNamedItem("groupId")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupName") != null)
					bean.setGroupName(nodeMap.getNamedItem("groupName")
							.getNodeValue());

				if (nodeMap.getNamedItem("activeGroupMembers") != null)
					bean.setActiveGroupMembers(nodeMap.getNamedItem(
							"activeGroupMembers").getNodeValue());

				if (nodeMap.getNamedItem("inActiveGroupMembers") != null)
					bean.setInActiveGroupMembers(nodeMap.getNamedItem(
							"inActiveGroupMembers").getNodeValue());

				if (nodeMap.getNamedItem("createdDate") != null)
					bean.setCreatedDate(nodeMap.getNamedItem("createdDate")
							.getNodeValue());

				if (nodeMap.getNamedItem("modifiedDate") != null)
					bean.setModifiedDate(nodeMap.getNamedItem("modifiedDate")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupDepiction") != null)
					bean.setGroupdescription(nodeMap.getNamedItem("groupDepiction")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupIcon") != null)
					bean.setGroupIcon(nodeMap.getNamedItem("groupIcon")
							.getNodeValue());
				if (nodeMap.getNamedItem("inviteActiveMembers") != null)
					bean.setInviteMembers(nodeMap.getNamedItem("inviteActiveMembers")
							.getNodeValue());
				response_list.add(bean);
			}
			list = doc.getElementsByTagName("ParticepatedGroupsDetails");
			String[] activebuddies=null;
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				GroupBean bean = new GroupBean();

				if (nodeMap.getNamedItem("groupId") != null)
					bean.setGroupId(nodeMap.getNamedItem("groupId")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupName") != null)
					bean.setGroupName(nodeMap.getNamedItem("groupName")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupOwner") != null)
					bean.setOwnerName(nodeMap.getNamedItem("groupOwner")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupMembers") != null) {
					bean.setActiveGroupMembers(nodeMap.getNamedItem("groupMembers")
							.getNodeValue());
					activebuddies = bean.getActiveGroupMembers().split(",");
				}

				if (nodeMap.getNamedItem("createdDate") != null)
					bean.setCreatedDate(nodeMap.getNamedItem("createdDate")
							.getNodeValue());

				if (nodeMap.getNamedItem("modifiedDate") != null)
					bean.setModifiedDate(nodeMap.getNamedItem("modifiedDate")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupDepiction") != null)
					bean.setGroupdescription(nodeMap.getNamedItem("groupDepiction")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupIcon") != null)
					bean.setGroupIcon(nodeMap.getNamedItem("groupIcon")
							.getNodeValue());
				if (nodeMap.getNamedItem("inviteActiveMembers") != null)
					bean.setInviteMembers(nodeMap.getNamedItem("inviteActiveMembers")
							.getNodeValue());
				response_list.add(bean);
			}
			if(activebuddies!=null) {
				activebuddies = new HashSet<String>(Arrays.asList(activebuddies)).toArray(new String[0]);
				ArrayList<String> buddies = new ArrayList<String>();
				for (BuddyInformationBean bean : ContactsFragment.getBuddyList()) {
					if (bean.getName() != null)
						buddies.add(bean.getName());
				}
				ArrayList<String> nonbuddies = new ArrayList(Arrays.asList(activebuddies));
				nonbuddies.removeAll(buddies);
				for (String tmp : nonbuddies) {
					Log.i("AAAA", "xml duplicate " + tmp);
					if (!tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
						WebServiceReferences.webServiceClient.GetAllProfile(
								CallDispatcher.LoginUser, tmp, SingleInstance.mainContext);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return response_list;
		}
	}

	public GroupBean parseLeaveGroup(String xml) {
		GroupBean bean = new GroupBean();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("LeaveGroup");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("text") != null)
					bean.setResult(nodeMap.getNamedItem("text").getNodeValue());
				if (nodeMap.getNamedItem("groupId") != null)
					bean.setGroupId(nodeMap.getNamedItem("groupId")
							.getNodeValue());
				if (nodeMap.getNamedItem("deletedGroupMembers") != null)
					bean.setDeleteGroupMembers(nodeMap.getNamedItem(
							"deletedGroupMembers").getNodeValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return bean;
		}
	}

	public Vector<GroupBean> parseGetParticipateGroups(String xml) {
		Vector<GroupBean> response_list = new Vector<GroupBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("GroupDetails");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				GroupBean bean = new GroupBean();

				if (nodeMap.getNamedItem("groupId") != null)
					bean.setGroupId(nodeMap.getNamedItem("groupId")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupName") != null)
					bean.setGroupName(nodeMap.getNamedItem("groupName")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupOwner") != null)
					bean.setOwnerName(nodeMap.getNamedItem("groupOwner")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupMembers") != null)
					bean.setGroupMembers(nodeMap.getNamedItem("groupMembers")
							.getNodeValue());

				if (nodeMap.getNamedItem("createdDate") != null)
					bean.setCreatedDate(nodeMap.getNamedItem("createdDate")
							.getNodeValue());

				if (nodeMap.getNamedItem("modifiedDate") != null)
					bean.setModifiedDate(nodeMap.getNamedItem("modifiedDate")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupDepiction") != null)
					bean.setGroupdescription(nodeMap.getNamedItem("groupDepiction")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupIcon") != null)
					bean.setGroupIcon(nodeMap.getNamedItem("groupIcon")
							.getNodeValue());
				response_list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return response_list;
		}
	}

	public GroupBean parseModifyGroup(String xml) {
		GroupBean bean = new GroupBean();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("ModifyGroup");
			if (list.getLength() > 0) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("text") != null)
					bean.setResult(nodeMap.getNamedItem("text").getNodeValue());
				if (nodeMap.getNamedItem("groupId") != null)
					bean.setGroupId(nodeMap.getNamedItem("groupId")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupName") != null)
					bean.setGroupName(nodeMap.getNamedItem("groupName")
							.getNodeValue());
				if (nodeMap.getNamedItem("activeGroupMembers") != null)
					bean.setActiveGroupMembers(nodeMap.getNamedItem(
							"activeGroupMembers").getNodeValue());
				if (nodeMap.getNamedItem("inActiveGroupMembers") != null)
					bean.setInActiveGroupMembers(nodeMap.getNamedItem(
							"inActiveGroupMembers").getNodeValue());
				if (nodeMap.getNamedItem("createdDate") != null)
					bean.setCreatedDate(nodeMap.getNamedItem("createdDate")
							.getNodeValue());
				if (nodeMap.getNamedItem("modifiedDate") != null)
					bean.setModifiedDate(nodeMap.getNamedItem("modifiedDate")
							.getNodeValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return bean;
		}
	}

	public GroupBean parseGroupXml(String xml) {
		DocumentBuilderFactory builderFactory = null;
		DocumentBuilder builder = null;
		InputSource inputSource = null;
		Document document = null;
		Element element = null;
		try {
			if (xml != null && !xml.equals("")) {
				builderFactory = DocumentBuilderFactory.newInstance();
				builder = builderFactory.newDocumentBuilder();
				inputSource = new InputSource();
				inputSource.setCharacterStream(new StringReader(xml));
				document = (Document) builder.parse(inputSource);
				GroupBean bean = null;
				element = (Element) document.getElementsByTagName(
						"GroupMemberAdd").item(0);
				if (element != null) {
					bean = new GroupBean();
					String groupOwner = element.getAttribute("groupOwner")
							.trim();
					String groupId = element.getAttribute("groupId").trim();
					String groupName = element.getAttribute("groupName").trim();
					String groupMembers = element.getAttribute("groupMembers")
							.trim();
					String newGroupMembers = element.getAttribute(
							"newGroupMembers").trim();
					bean.setMethod("GroupMemberAdd");
					if (groupOwner != null && !groupOwner.equals("")) {
						bean.setOwnerName(groupOwner);
					}
					if (groupId != null && !groupId.equals("")) {
						bean.setGroupId(groupId);
					}
					if (groupName != null && !groupName.equals("")) {
						bean.setGroupName(groupName);
					}
					if (groupMembers != null && !groupMembers.equals("")) {
						bean.setGroupMembers(groupMembers);
					}
					if (newGroupMembers != null && !newGroupMembers.equals("")) {
						bean.setNewGroupMembers(newGroupMembers);
					}
					return bean;
				}

				element = (Element) document.getElementsByTagName("EditGroup")
						.item(0);
				if (element != null) {
					bean = new GroupBean();
					String groupOwner = element.getAttribute("groupOwner")
							.trim();
					String groupId = element.getAttribute("groupId").trim();
					String groupName = element.getAttribute("groupName").trim();
					bean.setMethod("EditGroup");
					if (groupOwner != null && !groupOwner.equals("")) {
						bean.setOwnerName(groupOwner);
					}
					if (groupId != null && !groupId.equals("")) {
						bean.setGroupId(groupId);
					}
					if (groupName != null && !groupName.equals("")) {
						bean.setGroupName(groupName);
					}
					return bean;
				}
				element = (Element) document.getElementsByTagName(
						"GroupMembersDelete").item(0);
				if (element != null) {
					bean = new GroupBean();
					String groupOwner = element.getAttribute("groupOwner")
							.trim();
					String groupId = element.getAttribute("groupId").trim();
					String groupName = element.getAttribute("groupName").trim();
					String deletedGroupMembers = element.getAttribute(
							"deletedGroupMembers").trim();
					bean.setMethod("GroupMembersDelete");
					if (groupOwner != null && !groupOwner.equals("")) {
						bean.setOwnerName(groupOwner);
					}
					if (groupId != null && !groupId.equals("")) {
						bean.setGroupId(groupId);
					}
					if (groupName != null && !groupName.equals("")) {
						bean.setGroupName(groupName);
					}
					if (deletedGroupMembers != null
							&& !deletedGroupMembers.equals("")) {
						bean.setDeleteGroupMembers(deletedGroupMembers);
					}
					return bean;
				}
				element = (Element) document
						.getElementsByTagName("GroupDelete").item(0);
				if (element != null) {
					bean = new GroupBean();
					String groupOwner = element.getAttribute("groupOwner")
							.trim();
					String groupId = element.getAttribute("groupId").trim();
					String groupName = element.getAttribute("groupName").trim();
					bean.setMethod("GroupDelete");
					if (groupOwner != null && !groupOwner.equals("")) {
						bean.setOwnerName(groupOwner);
					}
					if (groupId != null && !groupId.equals("")) {
						bean.setGroupId(groupId);
					}
					if (groupName != null && !groupName.equals("")) {
						bean.setGroupName(groupName);
					}
					return bean;
				}
				element = (Element) document.getElementsByTagName("LeaveGroup")
						.item(0);
				if (element != null) {
					bean = new GroupBean();
					String groupOwner = element.getAttribute("groupOwner")
							.trim();
					String groupId = element.getAttribute("groupId").trim();
					String groupName = element.getAttribute("groupName").trim();
					String deletedGroupMembers = element.getAttribute(
							"deletedGroupMembers").trim();
					bean.setMethod("LeaveGroup");
					if (groupOwner != null && !groupOwner.equals("")) {
						bean.setOwnerName(groupOwner);
					}
					if (groupId != null && !groupId.equals("")) {
						bean.setGroupId(groupId);
					}
					if (groupName != null && !groupName.equals("")) {
						bean.setGroupName(groupName);
					}
					if (deletedGroupMembers != null
							&& !deletedGroupMembers.equals("")) {
						bean.setDeleteGroupMembers(deletedGroupMembers);
					}
					return bean;
				}
				element = (Element) document
						.getElementsByTagName("CreateorModifyGroup").item(0);
				if (element != null) {
					bean = new GroupBean();
					String groupOwner = element.getAttribute("groupOwner")
							.trim();
					String groupId = element.getAttribute("groupId").trim();
					String groupName = element.getAttribute("groupName").trim();
					String groupMembers = element.getAttribute("groupMembers")
							.trim();
					String addGroupMembers = element.getAttribute(
							"addGroupMembers").trim();
					String deleteGroupMembers = element.getAttribute(
							"deleteGroupMembers").trim();
					String groupIcon = element.getAttribute("groupIcon").trim();
					String groupDescription = element.getAttribute("groupDescription").trim();
					bean.setMethod("ModifyGroup");
					if (groupOwner != null && !groupOwner.equals("")) {
						bean.setOwnerName(groupOwner);
					}
					if (groupId != null && !groupId.equals("")) {
						bean.setGroupId(groupId);
					}
					if (groupName != null && !groupName.equals("")) {
						bean.setGroupName(groupName);
					}
					if (groupMembers != null && !groupMembers.equals("")) {
						bean.setGroupMembers(groupMembers);
					}
					if (groupDescription != null && !groupDescription.equals("")) {
						bean.setGroupdescription(groupDescription);
					}
					if (groupIcon != null && !groupIcon.equals("")) {
						bean.setGroupIcon(groupIcon);
					}
					if (addGroupMembers != null && !addGroupMembers.equals("")) {
						bean.setActiveGroupMembers(addGroupMembers);
					}
					if (deleteGroupMembers != null
							&& !deleteGroupMembers.equals("")) {
						bean.setDeleteGroupMembers(deleteGroupMembers);
					}
					return bean;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String[] parseSetGroupChatSettings(String xml) {
		String[] result = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("GroupSettings");

			node = list.item(0);
			nodeMap = node.getAttributes();

			result = new String[4];

			if (nodeMap.getNamedItem("text") != null) {
				result[0] = nodeMap.getNamedItem("text").getNodeValue();
			}
			if (nodeMap.getNamedItem("groupId") != null) {
				result[1] = nodeMap.getNamedItem("groupId").getNodeValue();

			}
			if (nodeMap.getNamedItem("updatedGroupMembers") != null) {
				result[2] = nodeMap.getNamedItem("updatedGroupMembers")
						.getNodeValue();

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	public String[] parseGetProfessions(String xml) {
		String result = null;
		String Split[]=null;
		
		try {
			Log.i("Test", "GET PROFESSION PARSING"+xml);
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("profession");

			node = list.item(0);
			nodeMap = node.getAttributes();
           
			if (nodeMap.getNamedItem("values") != null) {
				result = nodeMap.getNamedItem("values").getNodeValue();
			}
			Log.i("Test", "GET PROFESSION PARSING RESULT Array "+result);

			String delimiter = ",";
			 Split=result.split(delimiter);

			  /* print substrings */
			  for(int i =0; i < Split.length ; i++){
					Log.i("Test", "PROFESSION "+Split[i]);				

			  }
			  					
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Split;
	}
	public String[] parseNewVerification(String xml) {
		String[] result = null;
		result=new String[2];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("text");

			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("text") != null) {
				result[0] = nodeMap.getNamedItem("text").getNodeValue();
			}
			if (nodeMap.getNamedItem("role") != null) {
				result[1] = nodeMap.getNamedItem("role").getNodeValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	public Vector<GroupChatPermissionBean> parseGetGroupChatSettings(String xml) {
		Vector<GroupChatPermissionBean> gcpList = new Vector<GroupChatPermissionBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("settings");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				GroupChatPermissionBean bean = new GroupChatPermissionBean();

				if (nodeMap.getNamedItem("groupId") != null)
					bean.setGroupId(nodeMap.getNamedItem("groupId")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupOwner") != null)
					bean.setGroupOwner(nodeMap.getNamedItem("groupOwner")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupMember") != null)
					bean.setGroupMember(nodeMap.getNamedItem("groupMember")
							.getNodeValue());

				if (nodeMap.getNamedItem("audioConference") != null)
					bean.setAudioConference(nodeMap.getNamedItem(
							"audioConference").getNodeValue());

				if (nodeMap.getNamedItem("videoConference") != null)
					bean.setVideoConference(nodeMap.getNamedItem(
							"videoConference").getNodeValue());

				if (nodeMap.getNamedItem("audioBroadcast") != null)
					bean.setAudioBroadcast(nodeMap.getNamedItem(
							"audioBroadcast").getNodeValue());

				if (nodeMap.getNamedItem("videoBroadcast") != null)
					bean.setVideoBroadcast(nodeMap.getNamedItem(
							"videoBroadcast").getNodeValue());
				if (nodeMap.getNamedItem("textMessage") != null)
					bean.setTextMessage(nodeMap.getNamedItem("textMessage")
							.getNodeValue());
				if (nodeMap.getNamedItem("audioMessage") != null)
					bean.setAudioMessage(nodeMap.getNamedItem("audioMessage")
							.getNodeValue());
				if (nodeMap.getNamedItem("videoMessage") != null)
					bean.setVideoMessage(nodeMap.getNamedItem("videoMessage")
							.getNodeValue());
				if (nodeMap.getNamedItem("photoMessage") != null)
					bean.setPhotoMessage(nodeMap.getNamedItem("photoMessage")
							.getNodeValue());
				if (nodeMap.getNamedItem("privateMessage") != null)
					bean.setPrivateMessage(nodeMap.getNamedItem(
							"privateMessage").getNodeValue());
				if (nodeMap.getNamedItem("reply") != null)
					bean.setReplyBackMessage(nodeMap.getNamedItem("reply")
							.getNodeValue());
				if (nodeMap.getNamedItem("schedule") != null)
					bean.setScheduleMessage(nodeMap.getNamedItem("schedule")
							.getNodeValue());
				if (nodeMap.getNamedItem("deadLine") != null)
					bean.setDeadLineMessage(nodeMap.getNamedItem("deadLine")
							.getNodeValue());
				if (nodeMap.getNamedItem("withDraw") != null)
					bean.setWithDrawn(nodeMap.getNamedItem("withDraw")
							.getNodeValue());
				if (nodeMap.getNamedItem("chat") != null)
					bean.setChat(nodeMap.getNamedItem("chat")
							.getNodeValue());
				gcpList.add(bean);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return gcpList;
	}

	private Vector<Vector<FormFieldBean>> parseFormFieldSettings(String xml) {
		DocumentBuilderFactory dbf = null;
		DocumentBuilder db = null;
		InputSource is = null;
		Document doc = null;
		Node node = null;
		Element element;
		Vector<Vector<FormFieldBean>> totalList = new Vector<Vector<FormFieldBean>>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			element = (Element) doc.getElementsByTagName("OwnerFieldSettings")
					.item(0);
			Log.i("formfield345", "parse started");
			Vector<FormFieldBean> ownerList = new Vector<FormFieldBean>();
			if (element != null) {
				NodeList fieldSettingsNode = element
						.getElementsByTagName("FormsFieldSettings");
				if (fieldSettingsNode != null) {

					for (int j = 0; j < fieldSettingsNode.getLength(); j++) {
						FormFieldBean fBean = new FormFieldBean();
						element = (Element) fieldSettingsNode.item(j);
						String formId = element.getAttribute("formid").trim();
						Log.i("formfield345", "owner formid : " + formId);
						fBean.setFormId(formId);
						NodeList elementsByTagName = element
								.getElementsByTagName("Attribute");
						Vector<DefaultPermission> dList = new Vector<DefaultPermission>();
						for (int k = 0; k < elementsByTagName.getLength(); k++) {
							element = (Element) elementsByTagName.item(k);
							String attributeid = element.getAttribute(
									"attributeid").trim();
							String defaultpermission = element.getAttribute(
									"defaultpermission").trim();
							Log.i("formfield345", "owner aid : " + attributeid);
							Log.i("formfield345", "owner daccess : "
									+ defaultpermission);
							DefaultPermission dPermission = new DefaultPermission();
							dPermission.setAttributeId(attributeid);
							dPermission.setDefaultPermission(defaultpermission);
							NodeList fieldAccess = element
									.getElementsByTagName("FieldAccess");
							Vector<BuddyPermission> bList = new Vector<BuddyPermission>();
							for (int m = 0; m < fieldAccess.getLength(); m++) {
								element = (Element) fieldAccess.item(m);
								String accessiblebuddy = element.getAttribute(
										"accessiblebuddy").trim();
								String permissionid = element.getAttribute(
										"permissionid").trim();
								Log.i("formfield345", "owner buddyname : "
										+ accessiblebuddy);
								Log.i("formfield345",
										"owner buddypermission : "
												+ permissionid);
								BuddyPermission bPermission = new BuddyPermission();
								bPermission.setBuddyName(accessiblebuddy);
								bPermission.setBuddyAccess(permissionid);
								bList.add(bPermission);
							}
							dPermission.setBuddyPermissionList(bList);
							dList.add(dPermission);
						}
						fBean.setDefaultPermissionList(dList);
						ownerList.add(fBean);
					}
				}
			}
			totalList.add(ownerList);
			Vector<FormFieldBean> individualFormSettingList = new Vector<FormFieldBean>();
			element = (Element) doc.getElementsByTagName(
					"IndividualFieldSettings").item(0);
			if (element != null) {
				NodeList nodeList = element
						.getElementsByTagName("FormsFieldSettings");
				if (nodeList != null) {
					for (int i = 0; i < nodeList.getLength(); i++) {
						FormFieldBean fBean = new FormFieldBean();
						Vector<IndividualPermission> individualList = new Vector<IndividualPermission>();
						element = (Element) nodeList.item(0);
						String formId = element.getAttribute("formid").trim();
						fBean.setFormId(formId);
						String[] getFormSettings = DBAccess.getdbHeler()
								.getColumnofSync(formId);

						String userName = "";
						if (getFormSettings != null) {
							userName = getFormSettings[2];
						}
						Log.i("formfield345", "individual formid : " + formId);
						NodeList elementsByTagName = element
								.getElementsByTagName("IndividualFieldAccess");
						for (int k = 0; k < elementsByTagName.getLength(); k++) {
							element = (Element) elementsByTagName.item(k);
							String attributeid = element.getAttribute(
									"attributeid").trim();
							String permissionid = element.getAttribute(
									"permissionid").trim();
							Log.i("formfield345", "individual aid : "
									+ attributeid);
							Log.i("formfield345", "individual permission : "
									+ permissionid);
							IndividualPermission iBean = new IndividualPermission();
							iBean.setFormId(formId);
							iBean.setUserName(CallDispatcher.LoginUser);
							iBean.setAttributeId(attributeid);
							iBean.setPermission(permissionid);
							individualList.add(iBean);
						}
						fBean.setIndividualPermissionList(individualList);
						individualFormSettingList.add(fBean);
					}
				}
			}
			totalList.add(individualFormSettingList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return totalList;
	}

	
	public WebServiceBean parseUserSubscribe(String xml) {

		Log.i("welcome", "coming to parsing xml");
		WebServiceBean serviceBean = new WebServiceBean();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("text");
			node = list.item(0);
			nodeMap = node.getAttributes();
			serviceBean.setText(nodeMap.getNamedItem("text").getNodeValue());
			if (getResult(xml)) {
				serviceBean.setResult("1");
			} else {
				serviceBean.setResult("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceBean;
	}

	public ArrayList parseUserSignIn(String xml) {
		Log.i("xml", "responsed xml" + xml);
		Log.i("welcome", "responsed xml" + xml);

		WebServiceBean serviceBean = new WebServiceBean();
		ArrayList<Object> buddyList = null;

		try {

			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);

			list = doc.getElementsByTagName("com");
			for (int i = 0; i < list.getLength(); i++) {

				node = list.item(0);
				nodeMap = node.getAttributes();
				String router;
				Log.i("AAAA","parseusersignin ");
				if (nodeMap.getNamedItem("router") != null) {
					router = nodeMap.getNamedItem("router").getNodeValue();
					AppMainActivity.ip = router.split(":")[0];
					AppMainActivity.port= Integer.parseInt(router.split(":")[1]);
					Log.i("AAAA","parseusersignin "+AppMainActivity.ip+" "+AppMainActivity.port);
				}
			}

			list = doc.getElementsByTagName("UserDetails");
			buddyList = new ArrayList();

			Log.d("zxz", "Users " + list.getLength());

			for (int i = 0; i < list.getLength(); i++) {
				BuddyInformationBean bib = new BuddyInformationBean();
				node = list.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					element = (Element) node;
					if (element != null) {
                        String name = element.getAttribute("name").trim();
                        if (!(name.equals(null) && name.equals(""))) {
                            bib.setName(name);
                            bib.setEmailid(name);
                        }
                        String nickName = element.getAttribute("nickname").trim();
                        if (!(nickName.equals(null) || nickName.equals(""))) {
                            bib.setNickname(nickName);
                        }
						String status = element.getAttribute("status").trim();
                        if(!(status.equals(null) || status.equals(""))){
                            if (status.equals("0")) {
                                bib.setStatus("Offline");
                            } else if (status.equals("1")) {
                                bib.setStatus("Online");
                            } else if (status.equals("2")) {
                                bib.setStatus("Airport");
                            } else if (status.equals("3")) {
                                bib.setStatus("Away");
                            } else if (status.equals("4")) {
                                bib.setStatus("Stealth");
                            }else {
                                bib.setStatus(status);
                            }
                        }
                        String localipaddress = element.getAttribute("localipaddress").trim();
                        if(!(localipaddress.equals(null) || localipaddress.equals(""))){
                            bib.setLocalipaddress(localipaddress);
                        }
                        String externalipaddress = element.getAttribute("externalipaddress").trim();
                        if(!(externalipaddress.equals(null) || externalipaddress.equals(""))){
                            bib.setExternalipaddress(externalipaddress);
                        }
                        String photo = element.getAttribute("photo").trim();
                        if (!(photo.equals(null) || photo.equals(""))) {
                            bib.setProfile_picpath(photo);
                        }
                        String signalingport = element.getAttribute("signalingport").trim();
                        if (!(signalingport.equals(null) || signalingport.equals(""))) {
                            bib.setSignalingPort(signalingport);
                        }
                        String mode = element.getAttribute("mode").trim();
                        if (!(mode.equals(null) || mode.equals(""))) {
                            bib.setMode(mode);
                        }
                        String lat = element.getAttribute("lat").trim();
                        if (!(lat.equals(null) || lat.equals(""))) {
                            bib.setLatitude(lat);
                        }
                        String lon = element.getAttribute("long").trim();
                        if (!(lon.equals(null) || lon.equals(""))) {
                            bib.setLongitude(lon);
                        }
                        String distance = element.getAttribute("distance").trim();
                        if (!(distance.equals(null) || distance.equals(""))) {
                            bib.setDistance(distance);
                        } else {
                            bib.setDistance("nil");
                        }
                        String occupation = element.getAttribute("occupation").trim();
                        if (!(occupation.equals(null) || occupation.equals(""))) {
                            bib.setOccupation(occupation);
                        }
                        String firstname = element.getAttribute("firstname").trim();
                        if (!(firstname.equals(null) || firstname.equals(""))) {
                            bib.setFirstname(firstname);
                        }
                        String lastname = element.getAttribute("lastname").trim();
                        if (!(lastname.equals(null) || lastname.equals(""))) {
                            bib.setLastname(lastname);
                        }
                        buddyList.add(bib);
                    }
                }
			}
			list = doc.getElementsByTagName("update");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				AppVersionUpdateBean bean = new AppVersionUpdateBean();
				if (nodeMap.getNamedItem("type") != null)
					bean.setType(Integer.parseInt(nodeMap.getNamedItem("type")
							.getNodeValue()));
				if (nodeMap.getNamedItem("message") != null)
					bean.setMessage(nodeMap.getNamedItem("message")
							.getNodeValue());
				if (nodeMap.getNamedItem("url") != null)
					bean.setUrl(nodeMap.getNamedItem("url").getNodeValue());
				buddyList.add(bean);

			}
			list = doc.getElementsByTagName("ProfileModifiedTime");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				String profileModifiedDate[] = new String[3];
				if (nodeMap.getNamedItem("profileowner") != null) {
					profileModifiedDate[0] = nodeMap.getNamedItem(
							"profileowner").getNodeValue();
				}
				if (nodeMap.getNamedItem("profileid") != null) {
					profileModifiedDate[1] = nodeMap.getNamedItem("profileid")
							.getNodeValue();
				}
				if (nodeMap.getNamedItem("modifieddate") != null) {
					profileModifiedDate[2] = nodeMap.getNamedItem(
							"modifieddate").getNodeValue();
				}
				buddyList.add(profileModifiedDate);
			}

			list = doc.getElementsByTagName("VirtualUser");

			for (int i = 0; i < list.getLength(); i++) {
				VirtualBuddyBean bib = new VirtualBuddyBean();
				node = list.item(i);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("name") != null) {
					bib.setName(nodeMap.getNamedItem("name").getNodeValue());
					bib.setStatus("Virtual");
				}
				if (nodeMap.getNamedItem("owner") != null) {
					bib.setOwner(nodeMap.getNamedItem("owner").getNodeValue());
				}
				if (nodeMap.getNamedItem("serviceavailable") != null) {
					bib.setServiveAvailable(nodeMap.getNamedItem(
							"serviceavailable").getNodeValue());
				}
				if (nodeMap.getNamedItem("id") != null) {
					bib.setId((nodeMap.getNamedItem("id").getNodeValue()));
				}
				if (nodeMap.getNamedItem("action") != null) {
					bib.setAction((nodeMap.getNamedItem("action")
							.getNodeValue()));
				}

				buddyList.add(bib);

			}

			// Used tom parse own virtual uasers...
			list = doc.getElementsByTagName("OwnVirtualUser");

			for (int i = 0; i < list.getLength(); i++) {
				VirtualBuddyBean bib = new VirtualBuddyBean();
				node = list.item(i);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("name") != null) {
					bib.setName(nodeMap.getNamedItem("name").getNodeValue());

				}

				if (nodeMap.getNamedItem("serviceavailable") != null) {
					bib.setServiveAvailable(nodeMap.getNamedItem(
							"serviceavailable").getNodeValue());
				}
				if (nodeMap.getNamedItem("id") != null) {
					bib.setId((nodeMap.getNamedItem("id").getNodeValue()));
				}

				buddyList.add(bib);

			}


			// used to parse virtualFriendsDetailssssssssss...
			list = doc.getElementsByTagName("VirtualFriendDetails");

			for (int i = 0; i < list.getLength(); i++) {
				VirtualBuddyBean bib = new VirtualBuddyBean();
				node = list.item(i);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("lat") != null) {
					bib.setLatitude(nodeMap.getNamedItem("lat").getNodeValue());
				}

				if (nodeMap.getNamedItem("lang") != null) {
					bib.setLangitude(nodeMap.getNamedItem("lang")
							.getNodeValue());
				}
				if (nodeMap.getNamedItem("virtualuserid") != null) {
					bib.setId((nodeMap.getNamedItem("virtualuserid")
							.getNodeValue()));
				}
				if (nodeMap.getNamedItem("friendname") != null) {
					bib.setFriendsName((nodeMap.getNamedItem("friendname")
							.getNodeValue()));
				}

				buddyList.add(bib);

			}
			list = doc.getElementsByTagName("sipserverdetails");

			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("realm") != null)
					AppMainActivity.relam=nodeMap.getNamedItem("realm")
							.getNodeValue().trim();
				if (nodeMap.getNamedItem("registrar") != null)
					AppMainActivity.registrar=nodeMap.getNamedItem("registrar")
							.getNodeValue().trim();
				if (nodeMap.getNamedItem("proxy") != null)
					AppMainActivity.proxy=nodeMap.getNamedItem("proxy")
							.getNodeValue().trim();
			}

			list = doc.getElementsByTagName("com");
			node = list.item(0);
			nodeMap = node.getAttributes();
			ConnectionBrokerBean brokerBean = new ConnectionBrokerBean();
			brokerBean.setCbserver1(nodeMap.getNamedItem("cbserver1")
					.getNodeValue());
			brokerBean.setCbserver2(nodeMap.getNamedItem("cbserver2")
					.getNodeValue());
			brokerBean.setRouter(nodeMap.getNamedItem("router").getNodeValue());
			brokerBean.setRelayServer(nodeMap.getNamedItem("relayserver")
					.getNodeValue());
			if (nodeMap.getNamedItem("aa1port") != null) {
				brokerBean.setaa1Port(nodeMap.getNamedItem("aa1port")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("aa2port") != null) {
				brokerBean.setaa2Port(nodeMap.getNamedItem("aa2port")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("freeswitchserver") != null) {
				brokerBean.setFS(nodeMap.getNamedItem("freeswitchserver")
						.getNodeValue());
			}
			buddyList.add(brokerBean);

			list = doc.getElementsByTagName("sharereminder");
			// System.out.println("##Length :"+list.getLength());
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				buddyList.add(parseShareReminder(nodeMap));
			}

			// list=doc.getElementsByTagName("UserForm");
			// for(int i=0;i<list.getLength();i++)
			// {
			// node=list.item(i);
			// nodeMap=node.getAttributes();
			// buddyList.add(ParseSigninForms(nodeMap));
			// }

			list = doc.getElementsByTagName("flightdetails");
			// System.out.println("##Length :"+list.getLength());
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				buddyList.add(parseFlightDetails(nodeMap));
			}

			
			
			if (getResult(xml)) {
				serviceBean.setResult("1");
			} else {
				serviceBean.setResult("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buddyList;
	}

	
	@SuppressWarnings("finally")
	public UtilityResponse parseUtilityItemresponse(String xml) {
		UtilityResponse utilityResponse = new UtilityResponse();
		try {

			ArrayList<UtilityBean> res_list = null;

			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			Log.i("formfield345", "parse started");
			Vector<FormFieldBean> ownerList = new Vector<FormFieldBean>();
			NodeList fieldSettingsNode = doc
					.getElementsByTagName("utilityList");
			if (fieldSettingsNode != null) {

				for (int j = 0; j < fieldSettingsNode.getLength(); j++) {

					element = (Element) fieldSettingsNode.item(j);
					String Id = element.getAttribute("utilityId").trim();
					String count = element.getAttribute("resultCount").trim();
					String posted = element.getAttribute("postedDate").trim();
					String latitude = element.getAttribute("latLong").trim();
					// fBean.setFormId(Id);
					utilityResponse.setUtility_id(Id);
					utilityResponse.setNoofresults(count);
					utilityResponse.setPosted_date(posted);
					NodeList elementsByTagName = element
							.getElementsByTagName("utilityItem");
					res_list = new ArrayList<UtilityBean>();
					Vector<UtilityBean> dList = new Vector<UtilityBean>();
					for (int k = 0; k < elementsByTagName.getLength(); k++) {
						element = (Element) elementsByTagName.item(k);

						UtilityBean bean = new UtilityBean();
						String itemId = element.getAttribute("id").trim();
						String userid = element.getAttribute("userid").trim();
						String pname = element.getAttribute("productname")
								.trim();
						bean.setProduct_name(pname);
						String price = element.getAttribute("price").trim();
						bean.setPrice(price);
						String quantity = element.getAttribute("quantity")
								.trim();
						bean.setQty(quantity);
						String address = element.getAttribute("address").trim();
						bean.setAddress(address);
						String location = element.getAttribute("location")
								.trim();
						bean.setLocation(location);
						String orgname = element.getAttribute("orgname").trim();
						bean.setNameororg(orgname);
						String city = element.getAttribute("city").trim();
						bean.setCityordist(city);
						String state = element.getAttribute("state").trim();
						bean.setState(state);
						String country = element.getAttribute("country").trim();
						bean.setCountry(country);
						String pin = element.getAttribute("pin").trim();
						bean.setPin(pin);
						String phone = element.getAttribute("phone").trim();
						bean.setC_no(phone);
						String posteddate = element.getAttribute("posteddate")
								.trim();
						bean.setPosted_date(posteddate);
						String audio = element.getAttribute("audio").trim();
						String video = element.getAttribute("video").trim();
						String photo = element.getAttribute("photo").trim();
						bean.setImag_filename(photo);
						String mode = element.getAttribute("mode").trim();
						String latlong = element.getAttribute("latlong").trim();
						String distance = element.getAttribute("distanceS")
								.trim();

						bean.setId(Integer.parseInt(itemId));
						bean.setUsername(userid);
						res_list.add(bean);

					}
					utilityResponse.setResult_list(res_list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return utilityResponse;
		}

	}

	public Vector<UtilityResponse> parseSetUtilityServices(String xml) {
		Vector<UtilityResponse> res_list = new Vector<UtilityResponse>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			Log.i("formfield345", "parse started");
			NodeList UtilityResponseNode = doc
					.getElementsByTagName("utilityid");
			if (UtilityResponseNode != null) {
				for (int j = 0; j < UtilityResponseNode.getLength(); j++) {
					UtilityResponse utilityResponse = new UtilityResponse();
					element = (Element) UtilityResponseNode.item(j);
					String Id = element.getAttribute("id").trim();
					String productName = element.getAttribute("productname")
							.trim();
					String posted = element.getAttribute("postedDate").trim();
					utilityResponse.setUtility_id(Id);
					utilityResponse.setProductName(productName);
					utilityResponse.setPosted_date(posted);
					res_list.add(utilityResponse);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return res_list;
		}

	}

	public EditForm parseEditForm(String xml) {
		EditForm editForm = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			Log.i("formfield345", "parse started");
			element = (Element) doc.getElementsByTagName("EditForm").item(0);
			if (element != null) {
				editForm = new EditForm();
				editForm.setMode(element.getAttribute("mode").trim());
				list = doc.getElementsByTagName("text");
				node = list.item(0);
				nodeMap = node.getAttributes();
				editForm.setText(nodeMap.getNamedItem("text").getNodeValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return editForm;
		}

	}

	public EditForm parseEditFormForAddNewField(String xml) {
		EditForm editForm = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			Log.i("formfield345", "parse started");
			element = (Element) doc.getElementsByTagName("EditForm").item(0);
			if (element != null) {
				editForm = new EditForm();
				editForm.setMode(element.getAttribute("mode").trim());
				list = doc.getElementsByTagName("UserAttributeForm");
				HashMap<String, EditFormBean> eMap = new HashMap<String, EditFormBean>();
				for (int i = 0; i < list.getLength(); i++) {
					EditFormBean eBean = new EditFormBean();
					node = list.item(i);
					nodeMap = node.getAttributes();
					if (nodeMap.getNamedItem("formid") != null) {
						eBean.setFormid(nodeMap.getNamedItem("formid")
								.getNodeValue());
					}

					if (nodeMap.getNamedItem("columnname") != null) {
						eBean.setColumnname(nodeMap.getNamedItem("columnname")
								.getNodeValue());

					}
					if (nodeMap.getNamedItem("attributeid") != null) {
						eBean.setAttributeid(nodeMap
								.getNamedItem("attributeid").getNodeValue());
					}
					eMap.put(eBean.getColumnname(), eBean);
				}
				editForm.seteList(eMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return editForm;
		}

	}

	public ProfileBean parseAllMyAccount(String xml) {
        ProfileBean bean = new ProfileBean();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("Getmyaccount");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("username") != null)
					bean.setUsername(nodeMap.getNamedItem("username")
							.getNodeValue());
				if (nodeMap.getNamedItem("photo") != null)
					bean.setPhoto(nodeMap.getNamedItem("photo")
							.getNodeValue());

				if (nodeMap.getNamedItem("title") != null)
					bean.setTitle(nodeMap.getNamedItem("title")
							.getNodeValue());

				if (nodeMap.getNamedItem("firstname") != null)
					bean.setFirstname(nodeMap.getNamedItem(
							"firstname").getNodeValue());
				if (nodeMap.getNamedItem("lastname") != null)
					bean.setLastname(nodeMap.getNamedItem(
							"lastname").getNodeValue());
				if (nodeMap.getNamedItem("sex") != null)
					bean.setSex(nodeMap.getNamedItem(
							"sex").getNodeValue());
				if (nodeMap.getNamedItem("usertype") != null)
					bean.setUsertype(nodeMap.getNamedItem(
							"usertype").getNodeValue());
				if (nodeMap.getNamedItem("state") != null)
					bean.setState(nodeMap.getNamedItem("state")
							.getNodeValue());
				if (nodeMap.getNamedItem("profession") != null)
					bean.setProfession(nodeMap.getNamedItem("profession")
							.getNodeValue());
				if (nodeMap.getNamedItem("speciality") != null)
					bean.setSpeciality(nodeMap.getNamedItem("speciality")
							.getNodeValue());
				if (nodeMap.getNamedItem("medicalschool") != null)
					bean.setMedicalschool(nodeMap.getNamedItem("medicalschool")
							.getNodeValue());
				if (nodeMap.getNamedItem("residencyprogram") != null)
					bean.setResidencyprogram(nodeMap.getNamedItem(
							"residencyprogram").getNodeValue());
				if (nodeMap.getNamedItem("fellowshipprogram") != null)
					bean.setFellowshipprogram(nodeMap.getNamedItem("fellowshipprogram")
							.getNodeValue());
				if (nodeMap.getNamedItem("officeaddress") != null)
					bean.setOfficeaddress(nodeMap.getNamedItem("officeaddress")
							.getNodeValue());
				if (nodeMap.getNamedItem("hospitalaffiliation") != null)
					bean.setHospitalaffiliation(nodeMap.getNamedItem("hospitalaffiliation")
							.getNodeValue());
				if (nodeMap.getNamedItem("citationpublications") != null)
					bean.setCitationpublications(nodeMap.getNamedItem("citationpublications")
							.getNodeValue());
				if (nodeMap.getNamedItem("organizationmembership") != null)
					bean.setOrganizationmembership(nodeMap.getNamedItem("organizationmembership")
							.getNodeValue());
				if (nodeMap.getNamedItem("tos") != null)
					bean.setTos(nodeMap.getNamedItem("tos")
							.getNodeValue());
				if (nodeMap.getNamedItem(" baa") != null)
					bean.setBaa(nodeMap.getNamedItem("baa")
                            .getNodeValue());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bean;
	}
	public ArrayList<BuddyInformationBean> parseSearchPeopleByAccount(String xml) {

		Log.i("xml", "Response xml" + xml);
		ArrayList<BuddyInformationBean> result = new ArrayList<BuddyInformationBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("buddyList");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(0);
				nodeMap = node.getAttributes();
				NodeList nlist = node.getChildNodes();
				for (int j = 0; j < nlist.getLength(); j++) {
					Node node = nlist.item(j);
                    BuddyInformationBean bib = new BuddyInformationBean();
					if (node != null) {
						if (node.getNodeName().equalsIgnoreCase("FoundPeopleByAccount")) {
							NamedNodeMap map = node.getAttributes();
							Log.i("AAAA", "XP Search people "+result);
                            if(map.getNamedItem("buddyName").getNodeValue()!=null){
                                bib.setEmailid(map.getNamedItem("buddyName").getNodeValue());
                            }
                            if(map.getNamedItem("Profession").getNodeValue()!=null){
                                bib.setOccupation(map.getNamedItem("Profession").getNodeValue());
                            }
                            if(map.getNamedItem("nickName").getNodeValue()!=null){
                                bib.setNickname(map.getNamedItem("nickName").getNodeValue());
                            }
                            if(map.getNamedItem("photo").getNodeValue()!=null && map.getNamedItem("photo").getNodeValue()!="null"){
                                bib.setProfile_picpath(map.getNamedItem("photo").getNodeValue());
//                                if(!(bib.getProfile_picpath().equals(null) || bib.getProfile_picpath().equals(""))) {
//                                    String directory_path = Environment
//                                            .getExternalStorageDirectory()
//                                            .getAbsolutePath()
//                                            + "/COMMedia/"+bib.getProfile_picpath();
//                                    File picPath = new File(directory_path);
//                                    if(!picPath.exists()) {
//                                        String[] param = new String[3];
//                                        param[0] = CallDispatcher.LoginUser;
//                                        param[1] = CallDispatcher.Password;
//                                        param[2] = bib.getProfile_picpath();
//                                        WebServiceReferences.webServiceClient.FileDownload(param);
//                                    }
//                                }
                            }
                            if(map.getNamedItem("status").getNodeValue()!=null){
                                bib.setStatus(map.getNamedItem("status").getNodeValue());
                            }
							if(map.getNamedItem("firstname").getNodeValue()!=null){
								bib.setFirstname(map.getNamedItem("firstname").getNodeValue());
							}
							if(map.getNamedItem("lastname").getNodeValue()!=null){
								bib.setLastname(map.getNamedItem("lastname").getNodeValue());
							}
						}
                        result.add(bib);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("zxz", "received lo Exception .class..............");
            Log.d("IOS", "received lo Exception .class.............." + e.getMessage());
		}
		return result;

	}
	public String[] parsedownloadxml(String xml) {

        String[] filedetails=new String[2];
		Log.d("XP WSD","Inside XmlParser to download file");
		try {

			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);

			list = doc.getElementsByTagName("filedownload");
			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("filename") != null)
				filedetails[0]=nodeMap.getNamedItem("filename")
						.getNodeValue();
			list = doc.getElementsByTagName("content");
				node = list.item(0);
				NodeList childList11 = node.getChildNodes();
				Node childNode11 = childList11.item(0);

				filedetails[1]=childNode11.getNodeValue();
			Log.d("filename","filenmae :"+filedetails[0]);
			Log.i("file","fileContent-->"+filedetails[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filedetails;
	}
	public String parseGetMypinXml(String xml) {
		String result=null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("Getmypin");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("pin") != null)
					result = nodeMap.getNamedItem("pin").getNodeValue();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	public String[] parseGetMySecretQuestionXml(String xml) {
		String[] result=new String[6];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("Getmysecquestion");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();

				if (nodeMap.getNamedItem("secquestion1") != null)
					result[0] = nodeMap.getNamedItem("secquestion1").getNodeValue();
				if (nodeMap.getNamedItem("secquestion2") != null)
					result[1] = nodeMap.getNamedItem("secquestion2").getNodeValue();
				if (nodeMap.getNamedItem("secquestion3") != null)
					result[2] = nodeMap.getNamedItem("secquestion3").getNodeValue();
				if (nodeMap.getNamedItem("secanswer1") != null)
					result[3] = nodeMap.getNamedItem("secanswer1").getNodeValue();
				if (nodeMap.getNamedItem("secanswer2") != null)
					result[4] = nodeMap.getNamedItem("secanswer2").getNodeValue();
				if (nodeMap.getNamedItem("secanswer3") != null)
					result[5] = nodeMap.getNamedItem("secanswer3").getNodeValue();

			}
			Log.i("AAAA","notifyMySecretQuestion result "+result.length);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	public Vector<FindPeopleBean> parseSearchPeople(String xml) {

		Log.i("xml", "Response xml" + xml);
		Vector<FindPeopleBean> fBean = new Vector<FindPeopleBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("buddyList");
			for (int i = 0; i < list.getLength(); i++) {

				node = list.item(0);
				nodeMap = node.getAttributes();
				FindPeopleBean bean = new FindPeopleBean();
//				NodeList nlist = node.getChildNodes();
//				for (int j = 0; j < nlist.getLength(); j++) {
//					Node node = nlist.item(j);
//					if (node != null) {

						if (node.getNodeName().equalsIgnoreCase(
								"FoundPeopleByAccount")) {
							NamedNodeMap map = node.getAttributes();
							bean.setEmailId(map.getNamedItem("buddyName")
									.getNodeValue());
							bean.setName(map.getNamedItem("nickName")
									.getNodeValue());
							bean.setRole(map.getNamedItem("Profession")
									.getNodeValue());
							bean.setPhoto(map.getNamedItem("photo")
									.getNodeValue());
							bean.setStatus(map.getNamedItem("status")
									.getNodeValue());
						}
//					}
//				}
				fBean.add(bean);

			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("zxz", "received lo Exception .class..............");
			Log.d("IOS",
					"received lo Exception .class.............."
							+ e.getMessage());

		}
		return fBean;

	}
	public Vector<ProfileBean> parseGetAllProfile(String xml) {
		Log.i("AAAA","XP parseGetAllProfile  ");
		Vector<ProfileBean> profileList = new Vector<ProfileBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);

			list = doc.getElementsByTagName("Getmyprofile");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				ProfileBean bean=new ProfileBean();
				if (nodeMap.getNamedItem("username") != null)
					bean.setUsername(nodeMap.getNamedItem("username")
							.getNodeValue());
				if (nodeMap.getNamedItem("status") != null)
					bean.setStatus(nodeMap.getNamedItem("status")
							.getNodeValue());
				if (nodeMap.getNamedItem("photo") != null)
					bean.setPhoto(nodeMap.getNamedItem("photo")
							.getNodeValue());
				if (nodeMap.getNamedItem("nickname") != null)
					bean.setNickname(nodeMap.getNamedItem("nickname")
							.getNodeValue());
				if (nodeMap.getNamedItem("mobile") != null)
					bean.setMobileno(nodeMap.getNamedItem("mobile")
							.getNodeValue());
				if (nodeMap.getNamedItem("email") != null)
					bean.setUsername(nodeMap.getNamedItem("email")
							.getNodeValue());
				if (nodeMap.getNamedItem("address") != null)
					bean.setAddress(nodeMap.getNamedItem("address")
							.getNodeValue());
				if (nodeMap.getNamedItem("title") != null)
					bean.setTitle(nodeMap.getNamedItem("title")
							.getNodeValue());
				if (nodeMap.getNamedItem("firstname") != null)
					bean.setFirstname(nodeMap.getNamedItem(
							"firstname").getNodeValue());
				if (nodeMap.getNamedItem("lastname") != null)
					bean.setLastname(nodeMap.getNamedItem(
							"lastname").getNodeValue());
				if (nodeMap.getNamedItem("dob") != null)
					bean.setDob(nodeMap.getNamedItem(
							"dob").getNodeValue());
				if (nodeMap.getNamedItem("ssn") != null)
					bean.setSsn(nodeMap.getNamedItem(
							"ssn").getNodeValue());
				if (nodeMap.getNamedItem("sex") != null)
					bean.setSex(nodeMap.getNamedItem(
							"sex").getNodeValue());
				if (nodeMap.getNamedItem("race") != null)
					bean.setRace(nodeMap.getNamedItem(
							"race").getNodeValue());
				if (nodeMap.getNamedItem("ethnicity") != null)
					bean.setEthinicity(nodeMap.getNamedItem(
							"ethnicity").getNodeValue());
				if (nodeMap.getNamedItem("maritalstatus") != null)
					bean.setMaritalstatus(nodeMap.getNamedItem(
							"maritalstatus").getNodeValue());
				if (nodeMap.getNamedItem("usertype") != null)
					bean.setUsertype(nodeMap.getNamedItem(
							"usertype").getNodeValue());
				if (nodeMap.getNamedItem("state") != null)
					bean.setState(nodeMap.getNamedItem("state")
							.getNodeValue());
				if (nodeMap.getNamedItem("profession") != null)
					bean.setProfession(nodeMap.getNamedItem("profession")
							.getNodeValue());
				if (nodeMap.getNamedItem("speciality") != null)
					bean.setSpeciality(nodeMap.getNamedItem("speciality")
							.getNodeValue());
				if (nodeMap.getNamedItem("medicalschool") != null)
					bean.setMedicalschool(nodeMap.getNamedItem("medicalschool")
							.getNodeValue());
				if (nodeMap.getNamedItem("residencyprogram") != null)
					bean.setResidencyprogram(nodeMap.getNamedItem(
							"residencyprogram").getNodeValue());
				if (nodeMap.getNamedItem("fellowshipprogram") != null)
					bean.setFellowshipprogram(nodeMap.getNamedItem("fellowshipprogram")
							.getNodeValue());
				if (nodeMap.getNamedItem("officeaddress") != null)
					bean.setOfficeaddress(nodeMap.getNamedItem("officeaddress")
							.getNodeValue());
				if (nodeMap.getNamedItem("hospitalaffiliation") != null)
					bean.setHospitalaffiliation(nodeMap.getNamedItem("hospitalaffiliation")
							.getNodeValue());
				if (nodeMap.getNamedItem("citationpublications") != null)
					bean.setCitationpublications(nodeMap.getNamedItem("citationpublications")
							.getNodeValue());
				if (nodeMap.getNamedItem("organizationmembership") != null)
					bean.setOrganizationmembership(nodeMap.getNamedItem("organizationmembership")
							.getNodeValue());
				if (nodeMap.getNamedItem("tos") != null)
					bean.setTos(nodeMap.getNamedItem("tos")
							.getNodeValue());
				if (nodeMap.getNamedItem("baa") != null)
					bean.setBaa(nodeMap.getNamedItem("baa")
							.getNodeValue());
				profileList.add(bean);
			}

			list = doc.getElementsByTagName("BuddyProfileDetails");
			Log.i("AAAA","XP BuddyProfileDetails  "+list.getLength());
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				ProfileBean bean=new ProfileBean();
				if (nodeMap.getNamedItem("username") != null)
					bean.setUsername(nodeMap.getNamedItem("username")
							.getNodeValue());
				if (nodeMap.getNamedItem("status") != null)
					bean.setStatus(nodeMap.getNamedItem("status")
							.getNodeValue());
				if (nodeMap.getNamedItem("photo") != null)
					bean.setPhoto(nodeMap.getNamedItem("photo")
							.getNodeValue());
				if (nodeMap.getNamedItem("nickname") != null)
					bean.setNickname(nodeMap.getNamedItem("nickname")
							.getNodeValue());
				if (nodeMap.getNamedItem("mobile") != null)
					bean.setMobileno(nodeMap.getNamedItem("mobile")
							.getNodeValue());
				if (nodeMap.getNamedItem("email") != null)
					bean.setUsername(nodeMap.getNamedItem("email")
							.getNodeValue());
				if (nodeMap.getNamedItem("address") != null)
					bean.setAddress(nodeMap.getNamedItem("address")
							.getNodeValue());
				if (nodeMap.getNamedItem("title") != null)
					bean.setTitle(nodeMap.getNamedItem("title")
							.getNodeValue());
				if (nodeMap.getNamedItem("firstname") != null)
					bean.setFirstname(nodeMap.getNamedItem(
							"firstname").getNodeValue());
				if (nodeMap.getNamedItem("lastname") != null)
					bean.setLastname(nodeMap.getNamedItem(
							"lastname").getNodeValue());
				if (nodeMap.getNamedItem("dob") != null)
					bean.setDob(nodeMap.getNamedItem(
							"dob").getNodeValue());
				if (nodeMap.getNamedItem("ssn") != null)
					bean.setSsn(nodeMap.getNamedItem(
							"ssn").getNodeValue());
				if (nodeMap.getNamedItem("sex") != null)
					bean.setSex(nodeMap.getNamedItem(
							"sex").getNodeValue());
				if (nodeMap.getNamedItem("race") != null)
					bean.setRace(nodeMap.getNamedItem(
							"race").getNodeValue());
				if (nodeMap.getNamedItem("ethnicity") != null)
					bean.setEthinicity(nodeMap.getNamedItem(
							"ethnicity").getNodeValue());
				if (nodeMap.getNamedItem("maritalstatus") != null)
					bean.setMaritalstatus(nodeMap.getNamedItem(
							"maritalstatus").getNodeValue());
				if (nodeMap.getNamedItem("usertype") != null)
					bean.setUsertype(nodeMap.getNamedItem(
							"usertype").getNodeValue());
				if (nodeMap.getNamedItem("state") != null)
					bean.setState(nodeMap.getNamedItem("state")
							.getNodeValue());
				if (nodeMap.getNamedItem("profession") != null)
					bean.setProfession(nodeMap.getNamedItem("profession")
							.getNodeValue());
				if (nodeMap.getNamedItem("speciality") != null)
					bean.setSpeciality(nodeMap.getNamedItem("speciality")
							.getNodeValue());
				if (nodeMap.getNamedItem("medicalschool") != null)
					bean.setMedicalschool(nodeMap.getNamedItem("medicalschool")
							.getNodeValue());
				if (nodeMap.getNamedItem("residencyprogram") != null)
					bean.setResidencyprogram(nodeMap.getNamedItem(
							"residencyprogram").getNodeValue());
				if (nodeMap.getNamedItem("fellowshipprogram") != null)
					bean.setFellowshipprogram(nodeMap.getNamedItem("fellowshipprogram")
							.getNodeValue());
				if (nodeMap.getNamedItem("officeaddress") != null)
					bean.setOfficeaddress(nodeMap.getNamedItem("officeaddress")
							.getNodeValue());
				if (nodeMap.getNamedItem("hospitalaffiliation") != null)
					bean.setHospitalaffiliation(nodeMap.getNamedItem("hospitalaffiliation")
							.getNodeValue());
				if (nodeMap.getNamedItem("citationpublications") != null)
					bean.setCitationpublications(nodeMap.getNamedItem("citationpublications")
							.getNodeValue());
				if (nodeMap.getNamedItem("organizationmembership") != null)
					bean.setOrganizationmembership(nodeMap.getNamedItem("organizationmembership")
							.getNodeValue());
				if (nodeMap.getNamedItem("tos") != null)
					bean.setTos(nodeMap.getNamedItem("tos")
							.getNodeValue());
				if (nodeMap.getNamedItem("baa") != null)
					bean.setBaa(nodeMap.getNamedItem("baa")
							.getNodeValue());
				profileList.add(bean);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return profileList;
	}

    public ArrayList<String[]> parseSecurtiyQuestions(String xml){
        ArrayList<String[]> questionsList = new ArrayList<String[]>();
        String[] questions=null;
        String date=null;
        if (getResult(xml)) {
            try {
                dbf = DocumentBuilderFactory.newInstance();
                db = dbf.newDocumentBuilder();
                is = new InputSource();
                is.setCharacterStream(new StringReader(xml));
                doc = (Document) db.parse(is);
                list = doc.getElementsByTagName("Securityquestions");
                node = list.item(0);
                nodeMap = node.getAttributes();
                if(nodeMap.getNamedItem("createddate")!=null){
                    date=nodeMap.getNamedItem("createddate").getNodeValue();
                }
                SingleInstance.mainContext.seccreatedDate=date;
                list = doc.getElementsByTagName("Securityquestion");
                if(list.getLength()>0) {
                    for(int i=0;i<list.getLength();i++) {
                        questions=new String[list.getLength()];
                        node = list.item(i);
                        nodeMap = node.getAttributes();
                        if(nodeMap.getNamedItem("id")!=null){
                            questions[0]=nodeMap.getNamedItem("id").getNodeValue();
                        }
                        if(nodeMap.getNamedItem("securityquestion")!=null){
                            questions[1]=nodeMap.getNamedItem("securityquestion").getNodeValue();
                        }
                        questions[2]=date;
                        questionsList.add(questions);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return questionsList;
    }
	public ArrayList<String[]> parseStates(String xml){
		ArrayList<String[]> stateList = new ArrayList<String[]>();
		String[] states=null;
		if (getResult(xml)) {
			try {
				dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
				is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				doc = (Document) db.parse(is);
				list = doc.getElementsByTagName("states");
				if(list.getLength()>0) {
					for(int i=0;i<list.getLength();i++) {
						states=new String[3];
						node = list.item(i);
						nodeMap = node.getAttributes();
						if(nodeMap.getNamedItem("statename")!=null){
							states[0]=nodeMap.getNamedItem("statename").getNodeValue();
						}
						if(nodeMap.getNamedItem("statecode")!=null){
							states[1]=nodeMap.getNamedItem("statecode").getNodeValue();
						}
						stateList.add(states);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return stateList;
	}
	public ArrayList<String> parsecities(String xml){
		ArrayList<String> cityList = new ArrayList<String>();
		String city=null;
		if(getResult(xml)){
			try{
				dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
				is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				doc = (Document)db.parse(is);
				list = doc.getElementsByTagName("City");
				if(list.getLength()>0){
					for(int i=0;i<list.getLength();i++){
						city = new String();
						node = list.item(i);
						nodeMap = node.getAttributes();
						if(nodeMap.getNamedItem("cityName")!=null){
							city=nodeMap.getNamedItem("cityName").getNodeValue();
						}
						cityList.add(city);
						Log.d("listvalue", "parsecities" + cityList.size());
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}


		return cityList;
	}
	public ArrayList<String> parseHospitalDetails(String xml){
		ArrayList<String> hospitaldetailsList = new ArrayList<String>();
		String hospitaldetails=null;
		if (getResult(xml)) {
			try {
				dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
				is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				doc = (Document) db.parse(is);
				list = doc.getElementsByTagName("hospitals");
				if(list.getLength()>0) {
					for(int i=0;i<list.getLength();i++) {
						hospitaldetails=new String();
						node = list.item(i);
						nodeMap = node.getAttributes();
						if(nodeMap.getNamedItem("bizname")!=null){
							hospitaldetails=nodeMap.getNamedItem("bizname").getNodeValue();
						}
						hospitaldetailsList.add(hospitaldetails);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hospitaldetailsList;
	}
	public ArrayList<String[]> parseMedicalSocieties(String xml){
		ArrayList<String[]> medicalList = new ArrayList<String[]>();
		String[] medicaldetails=null;
		if (getResult(xml)) {
			try {
				dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
				is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				doc = (Document) db.parse(is);
				list = doc.getElementsByTagName("medicalsocietie");
				if(list.getLength()>0) {
					for(int i=0;i<list.getLength();i++) {
						medicaldetails=new String[3];
						node = list.item(i);
						nodeMap = node.getAttributes();
						if(nodeMap.getNamedItem("id")!=null){
							medicaldetails[0]=nodeMap.getNamedItem("id").getNodeValue();
						}
						if(nodeMap.getNamedItem("medicalsocietiename")!=null){
							medicaldetails[1]=nodeMap.getNamedItem("medicalsocietiename").getNodeValue();
						}
						medicalList.add(medicaldetails);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return medicalList;
	}
	public ArrayList<String[]> parseSpecialities(String xml){
		ArrayList<String[]> specialities = new ArrayList<String[]>();
		String[] medicaldetails=null;
		if (getResult(xml)) {
			try {
				dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
				is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				doc = (Document) db.parse(is);
				list = doc.getElementsByTagName("specialities");
				if(list.getLength()>0) {
					for(int i=0;i<list.getLength();i++) {
						medicaldetails=new String[3];
						node = list.item(i);
						nodeMap = node.getAttributes();
						if(nodeMap.getNamedItem("speciality")!=null){
							medicaldetails[0]=nodeMap.getNamedItem("speciality").getNodeValue();
						}
						if(nodeMap.getNamedItem("code")!=null){
							medicaldetails[1]=nodeMap.getNamedItem("code").getNodeValue();
						}
						specialities.add(medicaldetails);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return specialities;
	}
	public ArrayList<String> parseMedicalSchools(String xml){
		ArrayList<String> medicalSchools = new ArrayList<String>();
		String medicaldetails=null;
		if (getResult(xml)) {
			try {
				dbf = DocumentBuilderFactory.newInstance();
				db = dbf.newDocumentBuilder();
				is = new InputSource();
				is.setCharacterStream(new StringReader(xml));
				doc = (Document) db.parse(is);
				list = doc.getElementsByTagName("medicalschools");
				if(list.getLength()>0) {
					for(int i=0;i<list.getLength();i++) {
						medicaldetails=new String();
						node = list.item(i);
						nodeMap = node.getAttributes();
						if(nodeMap.getNamedItem("schoolname")!=null){
							medicaldetails=nodeMap.getNamedItem("schoolname").getNodeValue();
						}
						medicalSchools.add(medicaldetails);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return medicalSchools;
	}
	public String[] parseAcceptRejectGroup(String xml) {
		String result[] = new String[3];

		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("acceptrejectGroupMember");

			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("text") != null) {
				result[0] = nodeMap.getNamedItem("text").getNodeValue();
			}
			if (nodeMap.getNamedItem("groupId") != null) {
				result[1] = nodeMap.getNamedItem("groupId").getNodeValue();
			}
			if (nodeMap.getNamedItem("status") != null) {
				result[2] = nodeMap.getNamedItem("status").getNodeValue();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	public GroupBean parseGetGroupDetails(String xml) {
		GroupBean bean = new GroupBean();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("Groupdetail");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				bean.setOwnerName(CallDispatcher.LoginUser);

				if (nodeMap.getNamedItem("groupId") != null)
					bean.setGroupId(nodeMap.getNamedItem("groupId")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupName") != null)
					bean.setGroupName(nodeMap.getNamedItem("groupName")
							.getNodeValue());

				if (nodeMap.getNamedItem("activeGroupMembers") != null)
					bean.setActiveGroupMembers(nodeMap.getNamedItem(
							"activeGroupMembers").getNodeValue());

				if (nodeMap.getNamedItem("inActiveGroupMembers") != null)
					bean.setInActiveGroupMembers(nodeMap.getNamedItem(
							"inActiveGroupMembers").getNodeValue());

				if (nodeMap.getNamedItem("createdDate") != null)
					bean.setCreatedDate(nodeMap.getNamedItem("createdDate")
							.getNodeValue());

				if (nodeMap.getNamedItem("modifiedDate") != null)
					bean.setModifiedDate(nodeMap.getNamedItem("modifiedDate")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupDepiction") != null)
					bean.setGroupdescription(nodeMap.getNamedItem("groupDepiction")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupIcon") != null)
					bean.setGroupIcon(nodeMap.getNamedItem("groupIcon")
							.getNodeValue());
				if (nodeMap.getNamedItem("inviteActiveMembers") != null)
					bean.setInviteMembers(nodeMap.getNamedItem("inviteActiveMembers")
							.getNodeValue());
			}
		} catch (Exception e) {

		}
		return bean;
	}
	public Vector<GroupBean> parseGetRoundingGroupAndMembers(String xml) {
		Vector<GroupBean> response_list = new Vector<GroupBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("OwnRoundingDetails");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				GroupBean bean = new GroupBean();
				bean.setOwnerName(CallDispatcher.LoginUser);

				if (nodeMap.getNamedItem("groupType") != null)
					bean.setGrouptype(nodeMap.getNamedItem("groupType")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupId") != null)
					bean.setGroupId(nodeMap.getNamedItem("groupId")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupName") != null)
					bean.setGroupName(nodeMap.getNamedItem("groupName")
							.getNodeValue());

				if (nodeMap.getNamedItem("activeGroupMembers") != null)
					bean.setActiveGroupMembers(nodeMap.getNamedItem(
							"activeGroupMembers").getNodeValue());

				if (nodeMap.getNamedItem("inActiveGroupMembers") != null)
					bean.setInActiveGroupMembers(nodeMap.getNamedItem(
							"inActiveGroupMembers").getNodeValue());

				if (nodeMap.getNamedItem("createdDate") != null)
					bean.setCreatedDate(nodeMap.getNamedItem("createdDate")
							.getNodeValue());

				if (nodeMap.getNamedItem("modifiedDate") != null)
					bean.setModifiedDate(nodeMap.getNamedItem("modifiedDate")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupDepiction") != null)
					bean.setGroupdescription(nodeMap.getNamedItem("groupDepiction")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupIcon") != null)
					bean.setGroupIcon(nodeMap.getNamedItem("groupIcon")
							.getNodeValue());
				if (nodeMap.getNamedItem("inviteActiveMembers") != null)
					bean.setInviteMembers(nodeMap.getNamedItem("inviteActiveMembers")
							.getNodeValue());
				if (nodeMap.getNamedItem("adminMember") != null)
					bean.setAdminMember(nodeMap.getNamedItem("adminMember")
							.getNodeValue());
				response_list.add(bean);
			}
			list = doc.getElementsByTagName("ParticepatedRoundingDetails");
			String[] activebuddies =null;
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				GroupBean bean = new GroupBean();

				if (nodeMap.getNamedItem("groupType") != null)
					bean.setGrouptype(nodeMap.getNamedItem("groupType")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupId") != null)
					bean.setGroupId(nodeMap.getNamedItem("groupId")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupName") != null)
					bean.setGroupName(nodeMap.getNamedItem("groupName")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupOwner") != null)
					bean.setOwnerName(nodeMap.getNamedItem("groupOwner")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupMembers") != null) {
					bean.setActiveGroupMembers(nodeMap.getNamedItem("groupMembers")
							.getNodeValue());
					activebuddies = bean.getActiveGroupMembers().split(",");
				}
				if (nodeMap.getNamedItem("createdDate") != null)
					bean.setCreatedDate(nodeMap.getNamedItem("createdDate")
							.getNodeValue());

				if (nodeMap.getNamedItem("modifiedDate") != null)
					bean.setModifiedDate(nodeMap.getNamedItem("modifiedDate")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupDepiction") != null)
					bean.setGroupdescription(nodeMap.getNamedItem("groupDepiction")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupIcon") != null)
					bean.setGroupIcon(nodeMap.getNamedItem("groupIcon")
							.getNodeValue());
				if (nodeMap.getNamedItem("inviteActiveMembers") != null)
					bean.setInviteMembers(nodeMap.getNamedItem("inviteActiveMembers")
							.getNodeValue());
				if (nodeMap.getNamedItem("adminMember") != null)
					bean.setAdminMember(nodeMap.getNamedItem("adminMember")
							.getNodeValue());
				response_list.add(bean);
			}
			if(activebuddies!=null) {
				activebuddies = new HashSet<String>(Arrays.asList(activebuddies)).toArray(new String[0]);
				ArrayList<String> buddies = new ArrayList<String>();
				for (BuddyInformationBean bean : ContactsFragment.getBuddyList()) {
					if (bean.getName() != null)
						buddies.add(bean.getName());
				}
				ArrayList<String> nonbuddies = new ArrayList(Arrays.asList(activebuddies));
				nonbuddies.removeAll(buddies);
				for (String tmp : nonbuddies) {
					Log.i("AAAA", "xml duplicate " + tmp);
					if (!tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
						WebServiceReferences.webServiceClient.GetAllProfile(
								CallDispatcher.LoginUser, tmp, SingleInstance.mainContext);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return response_list;
		}
	}
	public String[] parseSetPatientRecord(String xml) {
		String[] result = null;
		result=new String[3];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("patientrecord");

			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("patientid") != null) {
				result[0] = nodeMap.getNamedItem("patientid").getNodeValue();
			}
			if (nodeMap.getNamedItem("groupid") != null) {
				result[1] = nodeMap.getNamedItem("groupid").getNodeValue();
			}
			if (nodeMap.getNamedItem("creatorname") != null) {
				result[2] = nodeMap.getNamedItem("creatorname").getNodeValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	public String[] parseCreateTask(String xml) {
		String[] result = null;
		result=new String[3];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("settaskrecord");

			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("taskid") != null) {
				result[0] = nodeMap.getNamedItem("taskid").getNodeValue();
			}
			if (nodeMap.getNamedItem("groupid") != null) {
				result[1] = nodeMap.getNamedItem("groupid").getNodeValue();
			}
			if (nodeMap.getNamedItem("creatorname") != null) {
				result[2] = nodeMap.getNamedItem("creatorname").getNodeValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	public String[] parseSetOrEditRole(String xml) {
		String[] result = null;
		result=new String[3];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("Role");

			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("roleid") != null) {
				result[0] = nodeMap.getNamedItem("roleid").getNodeValue();
			}
			if (nodeMap.getNamedItem("groupid") != null) {
				result[1] = nodeMap.getNamedItem("groupid").getNodeValue();
			}
			if (nodeMap.getNamedItem("groupmember") != null) {
				result[2] = nodeMap.getNamedItem("groupmember").getNodeValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	public ArrayList<Object> parseGetchattemplate(String xml){
		ArrayList<Object> chattemplate=new ArrayList<>();

		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("ChatTemplates");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					element = (Element) node;
					if (element != null) {
						String datetime = element.getAttribute("modifieddate").trim();
						if (!(datetime.equals(null) && datetime.equals(""))) {
							ChattemplateModifieddate bean=new ChattemplateModifieddate();
							bean.setModifieddatetime(datetime);
							chattemplate.add(bean);

						}
					}
				}
			}
			list = doc.getElementsByTagName("item");
			for (int i = 0; i < list.getLength(); i++) {
				chattemplatebean bean = new chattemplatebean();
				node = list.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					element = (Element) node;
					if (element != null) {
						String id = element.getAttribute("id").trim();
						if (!(id.equals(null) || id.equals(""))) {
							bean.setTempletid(id);
						}
						String message = element.getAttribute("template").trim();
						if (!(message.equals(null) || message.equals(""))) {
							bean.setTempletmessage(message);
						}
						chattemplate.add(bean);
					}
				}
			}
		} catch (Exception e) {
             e.printStackTrace();
		}
		return chattemplate;

	}
	public Vector<PatientDetailsBean> parseGetPatientDetails(String xml) {
		Vector<PatientDetailsBean> response_list = new Vector<PatientDetailsBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("GetPatientRecord");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				PatientDetailsBean bean = new PatientDetailsBean();

				if (nodeMap.getNamedItem("patientid") != null)
					bean.setPatientid(nodeMap.getNamedItem("patientid")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupid") != null)
					bean.setGroupid(nodeMap.getNamedItem("groupid")
							.getNodeValue());

				if (nodeMap.getNamedItem("creatorname") != null)
					bean.setCreatorname(nodeMap.getNamedItem("creatorname")
							.getNodeValue());

				if (nodeMap.getNamedItem("firstname") != null)
					bean.setFirstname(nodeMap.getNamedItem(
							"firstname").getNodeValue());

				if (nodeMap.getNamedItem("middlename") != null)
					bean.setMiddlename(nodeMap.getNamedItem(
							"middlename").getNodeValue());

				if (nodeMap.getNamedItem("lastname") != null)
					bean.setLastname(nodeMap.getNamedItem("lastname")
							.getNodeValue());

				if (nodeMap.getNamedItem("dob") != null)
					bean.setDob(nodeMap.getNamedItem("dob")
							.getNodeValue());
				if (nodeMap.getNamedItem("sex") != null)
					bean.setSex(nodeMap.getNamedItem("sex")
							.getNodeValue());
				if (nodeMap.getNamedItem("hospital") != null)
					bean.setHospital(nodeMap.getNamedItem("hospital")
							.getNodeValue());
				if (nodeMap.getNamedItem("mrn") != null)
					bean.setMrn(nodeMap.getNamedItem("mrn")
							.getNodeValue());
				if (nodeMap.getNamedItem("floor") != null)
					bean.setFloor(nodeMap.getNamedItem("floor")
							.getNodeValue());
				if (nodeMap.getNamedItem("ward") != null)
					bean.setWard(nodeMap.getNamedItem("ward")
							.getNodeValue());
				if (nodeMap.getNamedItem("room") != null)
					bean.setRoom(nodeMap.getNamedItem("room")
							.getNodeValue());
				if (nodeMap.getNamedItem("bed") != null)
					bean.setBed(nodeMap.getNamedItem("bed")
							.getNodeValue());
				if (nodeMap.getNamedItem("admissiondate") != null)
					bean.setAdmissiondate(nodeMap.getNamedItem("admissiondate")
							.getNodeValue());
				if (nodeMap.getNamedItem("assignedmembers") != null)
					bean.setAssignedmembers(nodeMap.getNamedItem("assignedmembers")
							.getNodeValue());
				response_list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return response_list;
		}
	}
	public Vector<TaskDetailsBean> parseGetTaskDetails(String xml) {
		Vector<TaskDetailsBean> response_list = new Vector<TaskDetailsBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("gettaskrecord");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				TaskDetailsBean bean = new TaskDetailsBean();

				if (nodeMap.getNamedItem("taskid") != null)
					bean.setTaskId(nodeMap.getNamedItem("taskid")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupid") != null)
					bean.setGroupid(nodeMap.getNamedItem("groupid")
							.getNodeValue());

				if (nodeMap.getNamedItem("creatorname") != null)
					bean.setCreatorName(nodeMap.getNamedItem("creatorname")
							.getNodeValue());

				if (nodeMap.getNamedItem("taskdesc") != null)
					bean.setTaskdesc(nodeMap.getNamedItem(
							"taskdesc").getNodeValue());

				if (nodeMap.getNamedItem("callpatient") != null)
					bean.setPatientname(nodeMap.getNamedItem(
							"callpatient").getNodeValue());

				if (nodeMap.getNamedItem("assignmembers") != null)
					bean.setAssignedMembers(nodeMap.getNamedItem("assignmembers")
							.getNodeValue());

				if (nodeMap.getNamedItem("duedate") != null)
					bean.setDuedate(nodeMap.getNamedItem("duedate")
							.getNodeValue());
				if (nodeMap.getNamedItem("duetime") != null)
					bean.setDuetime(nodeMap.getNamedItem("duetime")
							.getNodeValue());
				if (nodeMap.getNamedItem("setreminder") != null)
					bean.setSetreminder(nodeMap.getNamedItem("setreminder")
							.getNodeValue());
				if (nodeMap.getNamedItem("timetoremind") != null)
					bean.setTimetoremind(nodeMap.getNamedItem("timetoremind")
							.getNodeValue());
				if (nodeMap.getNamedItem("patientid") != null)
					bean.setPatientid(nodeMap.getNamedItem("patientid")
							.getNodeValue());
				if (nodeMap.getNamedItem("taskstatus") != null)
					bean.setTaskstatus(nodeMap.getNamedItem("taskstatus")
							.getNodeValue());
				response_list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return response_list;
		}
	}
	public PatientCommentsBean parseSetPatientComments(String xml) {

		PatientCommentsBean bean=new PatientCommentsBean();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("Patientcomments");

			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("commentid") != null) {
				bean.setCommentid(nodeMap.getNamedItem("commentid")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("patientid") != null) {
				bean.setPatientid(nodeMap.getNamedItem("patientid")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("groupid") != null) {
				bean.setGroupid(nodeMap.getNamedItem("groupid")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("groupowner") != null) {
				bean.setGroupowner(nodeMap.getNamedItem("groupowner")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("groupmember") != null) {
				bean.setGroupmember(nodeMap.getNamedItem("groupmember")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("comment") != null) {
				bean.setComments(nodeMap.getNamedItem("comment")
						.getNodeValue());
			}
			if (nodeMap.getNamedItem("dateandtime") != null) {
				bean.setDateandtime(nodeMap.getNamedItem("dateandtime")
						.getNodeValue());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bean;
	}

	public String[] parseSetPatientDescription(String xml) {
		String[] result = null;
		result=new String[3];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("patientdesc");

			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("patientid") != null) {
				result[0] = nodeMap.getNamedItem("patientid").getNodeValue();
			}
			if (nodeMap.getNamedItem("reportid") != null) {
				result[1] = nodeMap.getNamedItem("reportid").getNodeValue();
			}
			if (nodeMap.getNamedItem("creatorname") != null) {
				result[2] = nodeMap.getNamedItem("creatorname").getNodeValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	public Vector<Object> parseGetRoleAccess(String xml) {
		Vector<Object> roles = new Vector<Object>();
		String[] result = null;
		result = new String[3];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("Roundingrights");
			String roleid = null;
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				RoleAccessBean bean = new RoleAccessBean();

				if (nodeMap.getNamedItem("roleid") != null)
					bean.setRoleid(nodeMap.getNamedItem("roleid")
							.getNodeValue());
				roleid=bean.getRoleid();

				if (nodeMap.getNamedItem("groupid") != null)
					bean.setGroupid(nodeMap.getNamedItem("groupid")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupowner") != null)
					bean.setGroupowner(nodeMap.getNamedItem("groupowner")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupmember") != null)
					bean.setGroupmember(nodeMap.getNamedItem("groupmember").getNodeValue());

				if (nodeMap.getNamedItem("role") != null)
					bean.setRole(nodeMap.getNamedItem(
							"role").getNodeValue());

				if (nodeMap.getNamedItem("patientmanagement") != null)
					bean.setPatientmanagement(nodeMap.getNamedItem("patientmanagement")
							.getNodeValue());

				if (nodeMap.getNamedItem("taskmanagement") != null)
					bean.setTaskmanagement(nodeMap.getNamedItem("taskmanagement")
							.getNodeValue());
				if (nodeMap.getNamedItem("editroundingform") != null)
					bean.setEditroundingform(nodeMap.getNamedItem("editroundingform")
							.getNodeValue());
				if (nodeMap.getNamedItem("commentsview") != null)
					bean.setCommentsview(nodeMap.getNamedItem("commentsview")
							.getNodeValue());
				roles.add(bean);
			}
			list = doc.getElementsByTagName("Patientmanagement");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				RolePatientManagementBean pbean = new RolePatientManagementBean();

				if (nodeMap.getNamedItem("groupid") != null)
					pbean.setGroupid(nodeMap.getNamedItem("groupid")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupowner") != null)
					pbean.setGroupowner(nodeMap.getNamedItem("groupowner")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupmember") != null)
					pbean.setGroupmember(nodeMap.getNamedItem("groupmember").getNodeValue());
				pbean.setRoleid(roleid);

				if (nodeMap.getNamedItem("role") != null)
					pbean.setRole(nodeMap.getNamedItem(
							"role").getNodeValue());

				if (nodeMap.getNamedItem("add") != null)
					pbean.setAdd(nodeMap.getNamedItem("add")
							.getNodeValue());

				if (nodeMap.getNamedItem("modify") != null)
					pbean.setModify(nodeMap.getNamedItem("modify")
							.getNodeValue());
				if (nodeMap.getNamedItem("delete") != null)
					pbean.setDelete(nodeMap.getNamedItem("delete")
							.getNodeValue());
				if (nodeMap.getNamedItem("discharge") != null)
					pbean.setDischarge(nodeMap.getNamedItem("discharge")
							.getNodeValue());
				roles.add(pbean);
			}
			list = doc.getElementsByTagName("Editroundingform");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				RoleEditRndFormBean eBean = new RoleEditRndFormBean();

				if (nodeMap.getNamedItem("groupid") != null)
					eBean.setGroupid(nodeMap.getNamedItem("groupid")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupowner") != null)
					eBean.setGroupowner(nodeMap.getNamedItem("groupowner")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupmember") != null)
					eBean.setGroupmember(nodeMap.getNamedItem("groupmember").getNodeValue());

				eBean.setRoleid(roleid);

				if (nodeMap.getNamedItem("role") != null)
					eBean.setRole(nodeMap.getNamedItem(
							"role").getNodeValue());

				if (nodeMap.getNamedItem("status") != null)
					eBean.setStatus(nodeMap.getNamedItem("status")
							.getNodeValue());

				if (nodeMap.getNamedItem("diagnosis") != null)
					eBean.setDiagnosis(nodeMap.getNamedItem("diagnosis")
							.getNodeValue());
				if (nodeMap.getNamedItem("testandvitals") != null)
					eBean.setTestandvitals(nodeMap.getNamedItem("testandvitals")
							.getNodeValue());
				if (nodeMap.getNamedItem("hospitalcourse") != null)
					eBean.setHospitalcourse(nodeMap.getNamedItem("hospitalcourse")
							.getNodeValue());
				if (nodeMap.getNamedItem("notes") != null)
					eBean.setNotes(nodeMap.getNamedItem("notes")
							.getNodeValue());
				if (nodeMap.getNamedItem("consults") != null)
					eBean.setConsults(nodeMap.getNamedItem("consults")
							.getNodeValue());
				roles.add(eBean);
			}
			list = doc.getElementsByTagName("Taskmanagement");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				RoleTaskMgtBean tBean = new RoleTaskMgtBean();

				if (nodeMap.getNamedItem("groupid") != null)
					tBean.setGroupid(nodeMap.getNamedItem("groupid")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupowner") != null)
					tBean.setGroupowner(nodeMap.getNamedItem("groupowner")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupmember") != null)
					tBean.setGroupmember(nodeMap.getNamedItem("groupmember").getNodeValue());

				tBean.setRoleid(roleid);

				if (nodeMap.getNamedItem("role") != null)
					tBean.setRole(nodeMap.getNamedItem(
							"role").getNodeValue());

				if (nodeMap.getNamedItem("attending") != null)
					tBean.setTattending(nodeMap.getNamedItem("attending")
							.getNodeValue());

				if (nodeMap.getNamedItem("fellow") != null)
					tBean.setTfellow(nodeMap.getNamedItem("fellow")
							.getNodeValue());
				if (nodeMap.getNamedItem("chiefresident") != null)
					tBean.setTchiefresident(nodeMap.getNamedItem("chiefresident")
							.getNodeValue());
				if (nodeMap.getNamedItem("resident") != null)
					tBean.setTresident(nodeMap.getNamedItem("resident")
							.getNodeValue());
				if (nodeMap.getNamedItem("medstudent") != null)
					tBean.setTmedstudent(nodeMap.getNamedItem("medstudent")
							.getNodeValue());

				roles.add(tBean);
			}
			list = doc.getElementsByTagName("Commentsview");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				RoleCommentsViewBean cBean = new RoleCommentsViewBean();

				if (nodeMap.getNamedItem("groupid") != null)
					cBean.setGroupid(nodeMap.getNamedItem("groupid")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupowner") != null)
					cBean.setGroupowner(nodeMap.getNamedItem("groupowner")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupmember") != null)
					cBean.setGroupmember(nodeMap.getNamedItem("groupmember").getNodeValue());
				cBean.setRoleid(roleid);

				if (nodeMap.getNamedItem("role") != null)
					cBean.setRole(nodeMap.getNamedItem(
							"role").getNodeValue());

				if (nodeMap.getNamedItem("attending") != null)
					cBean.setCattending(nodeMap.getNamedItem("attending")
							.getNodeValue());

				if (nodeMap.getNamedItem("fellow") != null)
					cBean.setCfellow(nodeMap.getNamedItem("fellow")
							.getNodeValue());
				if (nodeMap.getNamedItem("chiefresident") != null)
					cBean.setCchiefresident(nodeMap.getNamedItem("chiefresident")
							.getNodeValue());
				if (nodeMap.getNamedItem("resident") != null)
					cBean.setCresident(nodeMap.getNamedItem("resident")
							.getNodeValue());
				if (nodeMap.getNamedItem("medstudent") != null)
					cBean.setCmedstudent(nodeMap.getNamedItem("medstudent")
							.getNodeValue());

				roles.add(cBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roles;
	}
	public Vector<PatientDescriptionBean> parseGetPatientDescription(String xml) {
		Vector<PatientDescriptionBean> response_list = new Vector<PatientDescriptionBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("Reportdetail");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				PatientDescriptionBean bean = new PatientDescriptionBean();

				if (nodeMap.getNamedItem("patientid") != null)
					bean.setPatientid(nodeMap.getNamedItem("patientid")
							.getNodeValue());
				if (nodeMap.getNamedItem("groupid") != null)
					bean.setGroupid(nodeMap.getNamedItem("groupid")
							.getNodeValue());

				if (nodeMap.getNamedItem("reportid") != null)
					bean.setReportid(nodeMap.getNamedItem("reportid")
							.getNodeValue());

				if (nodeMap.getNamedItem("reportcreatorname") != null)
					bean.setReportcreator(nodeMap.getNamedItem("reportcreatorname")
							.getNodeValue());

				if (nodeMap.getNamedItem("reportmodifiername") != null)
					bean.setReportmodifier(nodeMap.getNamedItem(
							"reportmodifiername").getNodeValue());

				if (nodeMap.getNamedItem("currentstatus") != null)
					bean.setCurrentstatus(nodeMap.getNamedItem(
							"currentstatus").getNodeValue());

				if (nodeMap.getNamedItem("diagnosis") != null)
					bean.setDiagnosis(nodeMap.getNamedItem("diagnosis")
							.getNodeValue());

				if (nodeMap.getNamedItem("medications") != null)
					bean.setMedications(nodeMap.getNamedItem("medications")
							.getNodeValue());
				if (nodeMap.getNamedItem("testandvitals") != null)
					bean.setTestandvitals(nodeMap.getNamedItem("testandvitals")
							.getNodeValue());
				if (nodeMap.getNamedItem("hospitalcourse") != null)
					bean.setHospitalcourse(nodeMap.getNamedItem("hospitalcourse")
							.getNodeValue());
				if (nodeMap.getNamedItem("consults") != null)
					bean.setConsults(nodeMap.getNamedItem("consults")
							.getNodeValue());
				response_list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return response_list;
		}
	}
	public Vector<PatientCommentsBean> parseGetPatientComments(String xml) {
		Vector<PatientCommentsBean> response_list = new Vector<PatientCommentsBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("PatientComment");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				PatientCommentsBean bean = new PatientCommentsBean();

				if (nodeMap.getNamedItem("commentid") != null)
					bean.setCommentid(nodeMap.getNamedItem("commentid")
							.getNodeValue());
				if (nodeMap.getNamedItem("patientid") != null)
					bean.setPatientid(nodeMap.getNamedItem("patientid")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupid") != null)
					bean.setGroupid(nodeMap.getNamedItem("groupid")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupowner") != null)
					bean.setGroupowner(nodeMap.getNamedItem("groupowner")
							.getNodeValue());

				if (nodeMap.getNamedItem("groupmember") != null)
					bean.setGroupmember(nodeMap.getNamedItem(
							"groupmember").getNodeValue());

				if (nodeMap.getNamedItem("comment") != null)
					bean.setComments(nodeMap.getNamedItem(
							"comment").getNodeValue());

				if (nodeMap.getNamedItem("dateandtime") != null)
					bean.setDateandtime(nodeMap.getNamedItem("dateandtime")
							.getNodeValue());
				response_list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return response_list;
		}
	}
	public String[] parseDischargePatient(String xml) {
		String[] result = null;
		result=new String[3];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("Dischargepatient");

			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("text") != null) {
				result[0] = nodeMap.getNamedItem("text").getNodeValue();
			}
			if (nodeMap.getNamedItem("groupId") != null) {
				result[1] = nodeMap.getNamedItem("groupId").getNodeValue();
			}
			if (nodeMap.getNamedItem("patinetid") != null) {
				result[2] = nodeMap.getNamedItem("patinetid").getNodeValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	public Vector<GroupMemberBean> parseGetMemberRights(String xml) {
		Vector<GroupMemberBean> response_list = new Vector<GroupMemberBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("MemberDetail");
			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				GroupMemberBean bean = new GroupMemberBean();

				if (nodeMap.getNamedItem("groupid") != null)
					bean.setGroupid(nodeMap.getNamedItem("groupid")
							.getNodeValue());
				if (nodeMap.getNamedItem("membername") != null)
					bean.setMembername(nodeMap.getNamedItem("membername")
							.getNodeValue());

				if (nodeMap.getNamedItem("adminrights") != null)
					bean.setAdmin(nodeMap.getNamedItem("adminrights")
							.getNodeValue());

				if (nodeMap.getNamedItem("memberrights") != null)
					bean.setRole(nodeMap.getNamedItem("memberrights")
							.getNodeValue());
				response_list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return response_list;
		}
	}
	public ArrayList<ShareReminder> parseSyncFileDetails(String xml)
	{
		ArrayList<ShareReminder>sync=new ArrayList<ShareReminder>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			Log.i("formfield345", "parse started");
			list = doc.getElementsByTagName("sharereminder");

			for (int i = 0; i < list.getLength(); i++) {
				node = list.item(i);
				nodeMap = node.getAttributes();
				sync.add(parseShareReminder(nodeMap));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return sync;
		}
	}
	public String[] parseDeleteTask(String xml) {
		String[] result = null;
		result=new String[3];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("DeleteTask");

			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("text") != null) {
				result[0] = nodeMap.getNamedItem("text").getNodeValue();
			}
			if (nodeMap.getNamedItem("groupId") != null) {
				result[1] = nodeMap.getNamedItem("groupId").getNodeValue();
			}
			if (nodeMap.getNamedItem("taskId") != null) {
				result[2] = nodeMap.getNamedItem("taskId").getNodeValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	public Vector<FileDetailsBean> parseGetFileDetails(String xml) {
		Vector<FileDetailsBean> fileslist=new Vector<FileDetailsBean>();
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("FileDetails");
			for (int i = 0; i < list.getLength(); i++) {
				FileDetailsBean bean = new FileDetailsBean();
				node = list.item(i);
				nodeMap = node.getAttributes();
				if (nodeMap.getNamedItem("username") != null)
					bean.setUsername(nodeMap.getNamedItem("username").getNodeValue());
				if (nodeMap.getNamedItem("branchtype") != null)
					bean.setBranchtype(nodeMap.getNamedItem("branchtype").getNodeValue());
				if (nodeMap.getNamedItem("textfiles") != null)
					bean.setTetxfiles(nodeMap.getNamedItem("textfiles").getNodeValue());
				if (nodeMap.getNamedItem("imagefiles") != null)
					bean.setImagefiles(nodeMap.getNamedItem("imagefiles").getNodeValue());
				if (nodeMap.getNamedItem("audiofiles") != null)
					bean.setAudiofiles(nodeMap.getNamedItem("audiofiles").getNodeValue());
				if (nodeMap.getNamedItem("videofiles") != null)
					bean.setVideofiles(nodeMap.getNamedItem("videofiles").getNodeValue());
				if (nodeMap.getNamedItem("otherfiles") != null)
					bean.setOtherfiles(nodeMap.getNamedItem("otherfiles").getNodeValue());
				if (nodeMap.getNamedItem("totalsize") != null)
					bean.setTotalsize(nodeMap.getNamedItem("totalsize").getNodeValue());
				fileslist.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return fileslist;
		}
	}
	public String[] parseUpdateChatTemplate(String xml) {
		String[] result = null;
		result=new String[4];
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = (Document) db.parse(is);
			list = doc.getElementsByTagName("UpdateChatTemplate");

			node = list.item(0);
			nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("modifieddate") != null) {
				result[0] = nodeMap.getNamedItem("modifieddate").getNodeValue();
			}
			if (nodeMap.getNamedItem("type") != null) {
				result[1] = nodeMap.getNamedItem("type").getNodeValue();
			}
			if (nodeMap.getNamedItem("templateid") != null) {
				result[2] = nodeMap.getNamedItem("templateid").getNodeValue();
			}
			if (nodeMap.getNamedItem("message") != null) {
				result[3] = nodeMap.getNamedItem("message").getNodeValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
}
