package org.lib.xml;

import java.util.ArrayList;
import java.util.Vector;

import org.lib.PatientDetailsBean;
import org.lib.model.CallHistoryBean;
import org.lib.model.FieldTemplateBean;
import org.lib.model.FormFieldSettingsBean;
import org.lib.model.GroupBean;
import org.lib.model.KeepAliveBean;
import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.PatientCommentsBean;
import org.lib.model.PatientDescriptionBean;
import org.lib.model.PermissionBean;
import org.lib.model.RoleAccessBean;
import org.lib.model.RoleCommentsViewBean;
import org.lib.model.RoleEditRndFormBean;
import org.lib.model.RolePatientManagementBean;
import org.lib.model.RoleTaskMgtBean;
import org.lib.model.ShareReminder;
import org.lib.model.SiginBean;
import org.lib.model.SignalingBean;
import org.lib.model.SubscribeBean;
import org.lib.model.TaskDetailsBean;
import org.lib.model.UdpMessageBean;
import org.lib.model.UtilityBean;
import org.lib.webservice.Servicebean;

import android.util.Base64;
import android.util.Log;

import com.bean.BuddyPermission;
import com.bean.DefaultPermission;
import com.bean.EditFormBean;
import com.bean.GroupChatBean;
import com.bean.GroupChatPermissionBean;
import com.bean.ProfileBean;
//import com.bean.SurveyApplicantFormBean;
import com.cg.commonclass.CallDispatcher;
import com.cg.hostedconf.AppReference;

/**
 * This class is used to Compose the XML.
 * 
 * 
 */
public class XmlComposer {

	private static String quotes = "\"";

	/**
	 * Convert and Return SubscribeBean in to XML format String for Signaling.
	 * 
	 * @param sb
	 *            SubscribeBean
	 * @return Composed String
	 */
	public String composeSubscribeXml(SubscribeBean sb) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<Registration ");
		if (sb.getName() != null) {
			buffer.append("name=" + quotes + sb.getName() + quotes);
		}
		if (sb.getPassword() != null) {
			buffer.append(" password=" + quotes + sb.getPassword() + quotes);
		}

		if (sb.getEmailId() != null) {
			buffer.append(" email=" + quotes + sb.getEmailId() + quotes);
		}
		if (sb.getProfession() != null) {
			buffer.append(" profession=" + quotes + sb.getProfession() + quotes);
		}
		if (sb.getMobileNo() != null) {
			buffer.append(" mobileno=" + quotes + sb.getMobileNo() + quotes);
		}
		if (sb.getPhoto() != null) {
			buffer.append(" photo=" + quotes + sb.getPhoto() + quotes);
		}
		if (sb.getSecretQuestion() != null) {
			buffer.append(" secquestion=" + quotes + sb.getSecretQuestion()
					+ quotes);
		}
		if (sb.getSecretAnswer() != null) {
			buffer.append(" secanswer=" + quotes + sb.getSecretAnswer()
					+ quotes);
		}
		buffer.append(" />");
		return getcreateReqResMsg(buffer.toString());
	}

	public String ComposeLinkXML(String[] params) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><FbLink");
			if (params[0] != null) {
				buffer.append(" userid=" + quotes + params[0] + quotes);
			}
			if (params[1] != null) {
				buffer.append(" fbaccid=" + quotes + params[1] + quotes);
			}
			if (params[2] != null) {
				buffer.append(" fbdisname=" + quotes + params[2] + quotes);
			}
			buffer.append(" />");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String ComposeaccessFormXML(String userid, String formid,
			String buddy, String permissionid, String syncid, String syncquery,
			String fsid, String type) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><FormSettings");
			if (userid != null) {
				buffer.append(" userid=" + quotes + userid + quotes);
			}
			if (formid != null) {
				buffer.append(" formid=" + quotes + formid + quotes);
			}
			if (buddy != null) {
				buffer.append(" buddy=" + quotes + buddy + quotes);
			}
			if (permissionid != null) {
				buffer.append(" permissionid=" + quotes + permissionid + quotes);
			}
			if (syncid != null) {
				buffer.append(" syncid=" + quotes + syncid + quotes);
			}
			if (syncquery != null) {
				buffer.append(" syncquery=" + quotes + syncquery + quotes);
			}
			if (fsid != null) {
				buffer.append(" fsid=" + quotes + fsid + quotes);
			}
			if (type != null) {
				buffer.append(" mode=" + quotes + type + quotes);
			}
			buffer.append(">");

			buffer.append("</FormSettings>");
			buffer.append("</com>");
			Log.d("xml", "composed xml for sync" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String composeMultipleAccessShareXML(ArrayList<String[]> accessList) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com>");
			for (String[] formAccess : accessList) {
				buffer.append("<FormSettings");
				if (formAccess[0] != null) {
					buffer.append(" userid=" + quotes + formAccess[0] + quotes);
				}
				if (formAccess[1] != null) {
					buffer.append(" formid=" + quotes + formAccess[1] + quotes);
				}
				if (formAccess[2] != null) {
					buffer.append(" buddy=" + quotes + formAccess[2] + quotes);
				}
				if (formAccess[3] != null) {
					buffer.append(" permissionid=" + quotes + formAccess[3]
							+ quotes);
				}
				if (formAccess[4] != null) {
					buffer.append(" syncid=" + quotes + formAccess[4] + quotes);
				}
				if (formAccess[5] != null) {
					buffer.append(" syncquery=" + quotes + formAccess[5]
							+ quotes);
				}
				if (formAccess[6] != null) {
					buffer.append(" fsid=" + quotes + formAccess[6] + quotes);
				}
				if (formAccess[7] != null) {
					buffer.append(" mode=" + quotes + formAccess[7] + quotes);
				}
				buffer.append(">");

				buffer.append("</FormSettings>");
			}
			buffer.append("</com>");
			Log.d("xml", "composed xml for sync" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String ComposeCreateShareXML(String params,
			ArrayList<String[]> member) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><Search");
			if (params != null) {
				buffer.append(" userid=" + quotes + params + quotes);
			}

			buffer.append(">");
			for (int i = 0; i < member.size(); i++) {
				String[] field_name = member.get(i);
				buffer.append("<fields fieldid=" + quotes + field_name[0]
						+ quotes + " fieldname=" + quotes + field_name[1]
						+ quotes + " fieldvalue=" + quotes + field_name[2]
						+ quotes + " >");

				buffer.append("</fields>");

			}

			buffer.append("</Search>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String ComposeCreateFormAttributeXML(String[] params,
			String[] field_name, String[] field_type, ArrayList<String[]> member) {
		String datasource = "";
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><UserForm");
			if (params[0] != null) {
				buffer.append(" username=" + quotes + params[0] + quotes);
			}
			if (params[1] != null) {
				buffer.append(" formname=" + quotes + params[1] + quotes);
			}
			if (params[2] != null) {
				buffer.append(" formicon=" + quotes + params[2] + quotes);
			}
			if (params[3] != null) {
				buffer.append(" formdescription=" + quotes + params[3] + quotes);
			}
			buffer.append(">");
			for (int i = 0; i < field_name.length - 3; i++) {
				buffer.append("<Column fieldname=" + quotes + field_name[i]
						+ quotes + " fieldtype=" + quotes + field_type[i]
						+ quotes + " >");
				// <Attribute dataentrymode="a11" datasource="a22"
				// datavalidation="a33" defaultvalue="a44" instruction="a55"
				// errortip="a66" />
				String[] memb = member.get(i);

				buffer.append("<Attribute dataentrymode=" + quotes + memb[1]
						+ quotes + " datasource=" + quotes + datasource
						+ quotes + " datavalidation=" + quotes + memb[2]
						+ quotes + " defaultvalue=" + quotes + memb[3] + quotes
						+ " instruction=" + quotes + memb[4] + quotes
						+ " errortip=" + quotes + memb[5] + quotes + " />");
				buffer.append("</Column>");

			}
			for (int i = field_name.length - 3; i < field_name.length; i++) {

				buffer.append("<Column fieldname=" + quotes + field_name[i]
						+ quotes + " fieldtype=" + quotes + field_type[i]
						+ quotes + " />");

			}

			buffer.append("</UserForm>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String ComposeCreateFormXML(String[] params, String[] field_name,
			String[] field_type) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><UserForm");
			if (params[0] != null) {
				buffer.append(" username=" + quotes + params[0] + quotes);
			}
			if (params[1] != null) {
				buffer.append(" formname=" + quotes + params[1] + quotes);
			}
			buffer.append(">");
			for (int i = 0; i < field_name.length; i++) {
				buffer.append("<Column fieldname=" + quotes + field_name[i]
						+ quotes + " fieldtype=" + quotes + field_type[i]
						+ quotes + " />");
			}
			buffer.append("</UserForm>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String ComposeAddFormDataXML(String username, String form_id,
			String[] params, String[] records) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com>");
			buffer.append("<UserDataForm formid=" + quotes + form_id + quotes
					+ " userid=" + quotes + username + quotes + ">");
			for (int i = 0; i < records.length; i++) {
				buffer.append("<Data field=" + quotes + params[i] + quotes
						+ " value=" + quotes + records[i] + quotes + " />");
			}
			buffer.append("</UserDataForm>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String ComposeUpdateFormDataXML(String formid, String userid,
			String tableid, String[] params, String[] records) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com>");
			buffer.append("<UserEditForm formid=" + quotes + formid + quotes
					+ " userid=" + quotes + userid + quotes + " tableid="
					+ quotes + tableid + quotes + ">");
			for (int i = 0; i < records.length; i++) {
				buffer.append("<Data field=" + quotes + params[i] + quotes
						+ " value=" + quotes + records[i] + quotes + " />");
			}
			buffer.append("</UserEditForm>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String ComposegetFormDataXML(String username, String form_id,
			String filterdate) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com>");
			buffer.append("<UserFormData userid=" + quotes + username + quotes
					+ " formid=" + quotes + form_id + quotes + " filterdate="
					+ quotes + filterdate + quotes + ">");

			buffer.append("</UserFormData>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String ComposegetFormSettingsXML(String fsid, String username) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com>");
			buffer.append("<FormSettings  userid=" + quotes + username + quotes
					+ " fsid=" + quotes + fsid + quotes + " >");

			buffer.append("</FormSettings >");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String ComposeCallHistory(ArrayList<CallHistoryBean> callHistorydata) {
		// if(callHistory.getAutoCall()==null)
		// {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\"?>" + "<com>");
		for (int i = 0; i < callHistorydata.size(); i++) {
			CallHistoryBean callHistory = callHistorydata.get(i);

			buffer.append("<transaction ");

			if (callHistory.getFrom() != null) {
				buffer.append("from=" + quotes + callHistory.getFrom() + quotes);
			}
			if (callHistory.getTo() != null) {
				buffer.append(" to=" + quotes + callHistory.getTo() + quotes);
			}

			if (callHistory.getSessionId() != null) {
				buffer.append(" sessid=" + quotes + callHistory.getSessionId()
						+ quotes);
			}

			if (callHistory.getType() != null) {
				buffer.append(" type=" + quotes + callHistory.getType()
						+ quotes);
			}

			if (callHistory.getStime() != null) {
				buffer.append(" stime=" + quotes + callHistory.getStime()
						+ quotes);
			}

			if (callHistory.getEtime() != null) {
				buffer.append(" etime=" + quotes + callHistory.getEtime()
						+ quotes);
			}

			if (callHistory.getLoginUserName() != null) {
				buffer.append(" userid=" + quotes
						+ callHistory.getLoginUserName() + quotes);
			}

			buffer.append(" deviceos=" + quotes + "Android" + quotes);

			if (callHistory.getAutoCall() != null) {
				buffer.append(" ac=" + quotes + callHistory.getAutoCall()
						+ quotes);
			}

			if (callHistory.getNetworkState() != null) {
				buffer.append(" network=" + quotes
						+ callHistory.getNetworkState() + quotes);
			}
			buffer.append(" />");

		}

		buffer.append("</com>");

		// Log.d("xml","newwwwwwwwww "+buffer);

		return buffer.toString();

	}

	/**
	 * Convert and Return SignalingBean in to XML format String for Signaling.
	 * 
	 * @param sb
	 *            SignalingBean
	 * @return composed String
	 */
	public String composeInviteXML(SignalingBean sb) {
		StringBuffer buffer = new StringBuffer();
		String result = null;
		buffer.append("<tl ");
		buffer.append("from=" + quotes + sb.getFrom() + quotes);
		buffer.append(" to=" + quotes + sb.getTo() + quotes);
		buffer.append(" localip=" + quotes + sb.getLocalip() + quotes);
		buffer.append(" publicip=" + quotes + sb.getPublicip() + quotes);
		buffer.append(" calltype=" + quotes + sb.getCallType() + quotes);

		buffer.append(" sessionid=" + quotes + sb.getSessionid() + quotes);

		buffer.append(" signalid=" + quotes + sb.getSignalid() + quotes);
		buffer.append(" signalport=" + quotes + sb.getSignalport() + quotes);

		if (sb.getCallSubType() != null) {
			buffer.append(" callsubtype=" + quotes + sb.getCallSubType()
					+ quotes);
		}

		if(sb.getChatid() != null){
			buffer.append(" callChatId=" + quotes + sb.getChatid()
					+ quotes);
		}
		if (sb.getGmember() != null) {
			buffer.append(" gmember=" + quotes + sb.getGmember() + quotes);
		}

		if (sb.getConferencemember() != null) {
			buffer.append(" conferencemember=" + quotes
					+ sb.getConferencemember() + quotes);
		}

		if(sb.getType() != null && sb.getType().equals("0") && sb.getHost() != null
				&& sb.getHost().length() > 0 && sb.getParticipants() != null && sb.getParticipants().length()>0){
			buffer.append(" host=" + quotes
					+ sb.getHost() + quotes);
			buffer.append(" participants=" + quotes
					+ sb.getParticipants() + quotes);
			if(sb.getJoincall() != null && sb.getJoincall().equalsIgnoreCase("yes")) {
				buffer.append(" joincall=" + quotes
						+ "yes" + quotes);
			} else {
				buffer.append(" joincall=" + quotes
						+ "no" + quotes);
			}
		}

		if(sb.getVideopromote() != null && sb.getVideopromote().equalsIgnoreCase("yes")){
			buffer.append(" videoPromotion=" + quotes
					+ "yes" + quotes);
		}

		if(sb.getVideoStoped() != null){
			buffer.append(" myVideoStoped=" + quotes
					+ sb.getVideoStoped() + quotes);
		}

		if (sb.getCallType().equals("MTP")) {
			Log.d("log", "################# condition satisfied");
			buffer.append(" message=" + quotes + sb.getMessage() + quotes);
			buffer.append(" fontname=" + quotes + "Helvetica" + quotes);
			buffer.append(" fontsize=" + quotes + "12.000000" + quotes);
			buffer.append(" fontcolor=" + quotes
					+ "0.000000,1.000000,0.000000,0.000000" + quotes);
			buffer.append(" fontstyle=" + quotes + "" + quotes);
		} else {
			buffer.append(" message=" + quotes + quotes);
		}

		if (sb.getRdpUsername() != null && sb.getRdpPassword() != null
				&& sb.getRdpPort() != null) {
			buffer.append(" rdpusername=" + quotes + sb.getRdpUsername()
					+ quotes);
			buffer.append(" rdppassword=" + quotes + sb.getRdpPassword()
					+ quotes);
			buffer.append(" rdpport=" + quotes + sb.getRdpPort() + quotes);

		}
		if (sb.getSsrc() != null) {
			buffer.append(" ssrc=" + quotes + sb.getSsrc() + quotes);
		}
		if (sb.getAudiossrc() != null) {
			buffer.append(" audioSSRC=" + quotes + sb.getAudiossrc() + quotes);

		}
		if (sb.getVideossrc() != null) {
			buffer.append(" videoSSRC=" + quotes + sb.getVideossrc() + quotes);
		}
		if (sb.getSeqNo() != null) {
			buffer.append(" seqno=" + quotes + sb.getSeqNo() + quotes);
		}

		if (sb.getFilePath() != null) {
			buffer.append(" filename=" + quotes + sb.getFilePath() + quotes);
		}
		if (!sb.getType().equals("12")) {

			if (sb.getFtpUser() != null && sb.getFtppassword() != null) {
				buffer.append(" ftpusername=" + quotes + sb.getFtpUser()
						+ quotes);
				buffer.append(" ftppassword=" + quotes + sb.getFtppassword()
						+ quotes);
			}
			buffer.append(" fileid=" + quotes + quotes);
		}

		if (sb.getisRobo() != null) {
			buffer.append(" robo=" + quotes + sb.getisRobo() + quotes);
		}
		if (sb.getisRoboKey() != null) {
			buffer.append(" robokey=" + quotes + sb.getisRoboKey() + quotes);
		}

		if (sb.getBs_calltype() != null) {
			buffer.append(" bs_calltype=" + quotes + sb.getBs_calltype()
					+ quotes);
		}
		if (sb.getBs_callCategory() != null) {
			buffer.append(" bs_callCategory=" + quotes
					+ sb.getBs_callCategory() + quotes);
		}
		if (sb.getBs_callstatus() != null) {
			buffer.append(" bs_callstatus=" + quotes + sb.getBs_callstatus()
					+ quotes);
		}
		if (sb.getBs_parentid() != null) {
			buffer.append(" bs_parentid=" + quotes + sb.getBs_parentid()
					+ quotes);
		}

		buffer.append("/>");

		return buffer.toString();
	}

	public String composeType13Xml(SignalingBean sb) {
		StringBuffer buffer = new StringBuffer();
		// String result=null;
		buffer.append("<tl ");
		buffer.append("from=" + quotes + sb.getFrom() + quotes);
		buffer.append(" to=" + quotes + sb.getTo() + quotes);

		buffer.append(" sessionid=" + quotes + sb.getSessionid() + quotes);
		buffer.append(" signalid=" + quotes + sb.getSignalid() + quotes);

		buffer.append("/>");

		return buffer.toString();

	}

	/**
	 * To convert the Input Text to XML for Signaling.
	 * 
	 * @param text
	 *            Input text
	 * @return composed String
	 */
	private String getInputRequestMessage(String text) {
		String ret = "";
		ret = "<?xml version=\"1.0\"?><tl result=0 type=0>";
		ret = ret + text + "</tl>";
		return ret;
	}

	/**
	 * To convert the Input Text to XML for Accept or Reject Buddy request.
	 * 
	 * @param text
	 *            Input message.
	 * @return composed String
	 */
	private String getAcceptRequestMessage(String text) {
		String ret = "";
		ret = "<?xml version=\"1.0\"?><tl result=0 type=0>";
		ret = ret + text + "</tl>";
		return ret;
	}

	/**
	 * This method can be called to get a complete well formed xml including
	 * root element
	 * 
	 * @param text
	 *            xml content
	 * @param result
	 *            result value
	 * @param type
	 *            type of xml content
	 * @return returns a well formed xml
	 */
	public String getMessagewithRoot(String text, String result, String type) {
		String ret = "";
		ret = "<?xml version=\"1.0\"?><com result=" + quotes + result + quotes
				+ " type=" + quotes + type + quotes + ">";
		ret = ret + text + "</com>";
		return ret;
	}

	/**
	 * This method can be called to get a complete well formed XML for
	 * AcceptMessage
	 * 
	 * @param text
	 *            XML content
	 * @return composed String
	 */
	public String getInputResponseAcceptMessage(String text) {
		String ret = "";
		ret = "<?xml version=\"1.0\"?><tl result=0 type=1>";
		ret = ret + text + "</tl>";
		return ret;
	}

	/**
	 * This method can be called to get a complete well formed XML for
	 * RejectMessage
	 * 
	 * @param text
	 *            XML content
	 * @return composed String
	 */
	public String getInputResponseRejectMessage(String text) {
		String ret = "";
		ret = "<?xml version=\"1.0\"?><tl result=1 type=0>";
		ret = ret + text + "</tl>";
		return ret;
	}

	/**
	 * Used to compose Signaling XML from the SignalingBean.
	 * 
	 * @param sb
	 *            signaling bean contains signaling informations.
	 * @return composed String
	 */
	public String composeSignalXML(SignalingBean sb) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<tl ");

		buffer.append("from=" + quotes + sb.getFrom() + quotes);
		buffer.append(" to=" + quotes + sb.getTo() + quotes);
		buffer.append(" localip=" + quotes + sb.getLocalip() + quotes);
		buffer.append(" publicip=" + quotes + sb.getPublicip() + quotes);
		buffer.append(" connectport=" + quotes + sb.getBuddyConnectport()
				+ quotes);
		buffer.append(" calltype=" + quotes + sb.getCallType() + quotes);

		buffer.append(" audioSSRC=" + quotes + sb.getAudiossrc() + quotes);
		buffer.append(" videoSSRC=" + quotes + sb.getVideossrc() + quotes);

		buffer.append(" sessionid=" + quotes + sb.getSessionid() + quotes);
		buffer.append(" conferencemember=" + quotes + sb.getConferencemember()
				+ quotes);
		buffer.append(" punchingmode=" + quotes + sb.getPunchingmode() + quotes);
		buffer.append(" connectip=" + quotes + sb.getBuddyConnectip() + quotes);
		buffer.append(" signalid=" + quotes + sb.getSignalid() + quotes);
		buffer.append(" signalport=" + quotes + sb.getSignalport() + quotes);

		if (sb.getUrl() != null || sb.getForwardingDetails() != null) {
			String composeexecuteXml = executeXml(sb);
			buffer.append(" action=" + quotes + composeexecuteXml + quotes);
		}
		if (sb.getCallSubType() != null) {
			buffer.append(" callsubtype=" + quotes + sb.getCallSubType()
					+ quotes);
		}

		buffer.append(" message=" + quotes + sb.getMessage() + quotes);
		buffer.append(" />");
		// if (AppReference.isWriteInFile)
		// AppReference.getLogger().debug("CALL123 : " + buffer.toString());
		return buffer.toString();

	}

	public String executeXml(SignalingBean aliveBean) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<execute>");
		buffer.append("<action ");
		if (aliveBean.getUrl() != null) {
			buffer.append("type=" + quotes + "1" + quotes);

			buffer.append(" command=" + quotes + aliveBean.getUrl() + quotes);
		} else if (aliveBean.getForwardingDetails() != null) {
			buffer.append("type=" + quotes + "2" + quotes);
			String[] ssdata = aliveBean.getForwardingDetails();
			String forwarddetails = ssdata[0] + "," + ssdata[1] + ","
					+ ssdata[2] + "," + ssdata[3];

			buffer.append(" command=" + quotes + forwarddetails + quotes);

		}

		buffer.append("/>");
		buffer.append(" </execute>");

		return EscapeXML("<?xml version=\"1.0\"?>" + buffer.toString());

		// return buffer.toString();
		// return "";

	}

	// String xmlEscapeText(String t) {
	// Log.d("SIGNAL", "dd "+t);
	// StringBuilder sb = new StringBuilder();
	// for(int i = 0; i < t.length(); i++){
	// char c = t.charAt(i);
	// switch(c){
	// case '<': sb.append("&lt;"); break;
	// case '>': sb.append("&gt;"); break;
	// case '\"': sb.append("&guot;"); break;
	// case '&': sb.append("&amp;"); break;
	// case '\'': sb.append("&apos;"); break;
	// default:
	// if(c>0x7e) {
	// sb.append("&#"+((int)c)+";");
	// }else
	// sb.append(c);
	// }
	// }
	// return sb.toString();
	// }

	public static String EscapeXML(String s)

	{

		// if (String.IsNullOrEmpty(s)) return s;

		String returnString = s;

		returnString = returnString.replace("'", "&apos;");
		returnString = returnString.replace("\"", "&quot;");

		returnString = returnString.replace(">", "&gt;");

		returnString = returnString.replace("<", "&lt;");

		returnString = returnString.replace("&", "&amp;");

		return returnString;

	}

	/**
	 * Used to compose SignIn XML from the SiginBean.
	 * 
	 * @param sb
	 *            SiginBean contains SignIn details.
	 * @return composed String
	 */

	public String composeSiginXml(SiginBean sb) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<UserName ");
		if (sb.getName() != null) {
			buffer.append("name=" + quotes + sb.getName() + quotes);
		}
		if (sb.getPassword() != null) {
			buffer.append(" password=" + quotes + sb.getPassword() + quotes);
		}

		if (sb.getLocalladdress() != null) {
			buffer.append(" localaddress=" + quotes + sb.getLocalladdress()
					+ quotes);
		}
		if (sb.getExternalipaddress() != null) {
			buffer.append(" externalipaddress=" + quotes
					+ sb.getExternalipaddress() + quotes);
		}
		if (sb.getMac() != null) {
			buffer.append(" mac=" + quotes + sb.getMac() + quotes);
		}

		if (sb.getPstatus() != null) {
			buffer.append(" Pstatus=" + quotes + sb.getPstatus() + quotes);
		}
		if (sb.getPstatus() != null) {
			buffer.append(" signalingport=" + quotes + sb.getSignalingPort()
					+ quotes);
		}

		if (sb.getLatitude() != null) {
			buffer.append(" lat=" + quotes + sb.getLatitude() + quotes);
		}
		if (sb.getLongitude() != null) {
			buffer.append(" long=" + quotes + sb.getLongitude() + quotes);
		}

		if (sb.getDtype() != null) {
			buffer.append(" dtype=" + quotes + sb.getDtype() + quotes);
		}
		if (sb.getDos() != null) {
			buffer.append(" dos=" + quotes + sb.getDos() + quotes);
		}
		if (sb.getAver() != null) {
			buffer.append(" aver=" + quotes + sb.getAver() + quotes);
		}

		

		if (sb.getReleaseVersion() != null) {
			buffer.append(" avdate=" + quotes + sb.getReleaseVersion() + quotes);
		}

		if (sb.getdevcetype() != null) {
			buffer.append(" devicetype=" + quotes + sb.getdevcetype() + quotes);
		}

		buffer.append(" />");
		Log.i("welcome",
				"******************webserviceclient.java buffer.toString()****************"
						+ buffer.toString());

		return getcreateReqResMsg(buffer.toString());
	}

	public String composeLocationXml(String[] locationDetails) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<com>");
		buffer.append("<Location ");
		if (locationDetails[0] != null) {
			buffer.append("name=" + quotes + locationDetails[0] + quotes);

		}
		if (locationDetails[1] != null) {
			buffer.append(" lat=" + quotes + locationDetails[1] + quotes);
		}

		if (locationDetails[2] != null) {
			buffer.append(" long=" + quotes + locationDetails[2] + quotes);
		}

		if (locationDetails[3] != null) {
			buffer.append(" virtualuserid=" + quotes + locationDetails[3]
					+ quotes);
		}

		if (locationDetails[4] != null) {
			buffer.append(" forvirtualfriend=" + quotes + locationDetails[4]
					+ quotes);
		}

		buffer.append("/>");
		buffer.append(" </com>");

		return "<?xml version=\"1.0\"?>" + buffer.toString();

	}

	public String composeCreateVbuddy(String[] vbuddy) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<protostart>");
		buffer.append("<VirtualUser ");
		if (vbuddy[0] != null) {
			buffer.append("username=" + quotes + vbuddy[0] + quotes);

		}
		if (vbuddy[1] != null) {
			buffer.append(" virtualusername=" + quotes + vbuddy[1] + quotes);

		}
		if (vbuddy[2] != null) {
			buffer.append(" serviceavailable=" + quotes + vbuddy[2] + quotes);
		}

		if (vbuddy[3] != null) {
			buffer.append(" action=" + quotes + vbuddy[3] + quotes);
		}

		buffer.append("/>");
		buffer.append(" </protostart>");

		return "<?xml version=\"1.0\"?>" + buffer.toString();

	}

	/**
	 * Used to compose KeepAliveXml from the KeepAliveBean.
	 * 
	 * @param aliveBean
	 *            KeepAliveBean contains keepAlive details
	 * @return composed String
	 */
	public String composeKeepAliveXml(KeepAliveBean aliveBean) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<keepalive ");
		if (aliveBean.getName() != null) {
			buffer.append("name=" + quotes + aliveBean.getName() + quotes);
		}
		if (aliveBean.getPassword() != null) {
			buffer.append(" password=" + quotes + aliveBean.getPassword()
					+ quotes);
		}

		if (aliveBean.getLocalipaddress() != null) {
			buffer.append(" localipaddress=" + quotes
					+ aliveBean.getLocalipaddress() + quotes);
		}
		if (aliveBean.getExternalipaddress() != null) {
			buffer.append(" externalipaddress=" + quotes
					+ aliveBean.getExternalipaddress() + quotes);
		}

		if (aliveBean.getStatus() != null) {
			buffer.append(" status=" + quotes + aliveBean.getStatus() + quotes);
		}

		if (aliveBean.getDtype() != null)
			buffer.append(" dtype=" + quotes + aliveBean.getDtype() + quotes);

		if (aliveBean.getavdate() != null)
			buffer.append(" avdate=" + quotes + aliveBean.getavdate() + quotes);

		if (aliveBean.getAver() != null)
			buffer.append(" aver=" + quotes + aliveBean.getAver() + quotes);

		if (aliveBean.getLat() != null) {
			buffer.append(" lat=" + quotes + aliveBean.getLat() + quotes);
		}

		if (aliveBean.getLon() != null) {
			buffer.append(" long=" + quotes + aliveBean.getLon() + quotes);
		}
		if (CallDispatcher.getDeviceId() != null) {
			buffer.append(" macid=" + quotes + CallDispatcher.getDeviceId() + quotes);
		}

		buffer.append(" />");

		if (aliveBean.getId() != null
				&& aliveBean.getShareReminderAction() != null) {
			buffer.append("<sharereminder id=" + quotes + aliveBean.getId()
					+ quotes + " status=" + quotes
					+ aliveBean.getShareReminderAction() + quotes + " />");
		}
		return getcreateReqResMsg(buffer.toString());
	}

	/**
	 * Used to compose ShareReminderXml from the ShareReminder bean.
	 * 
	 * @param reminder
	 *            ShareReminder contains ShareRemainder details.
	 * @return composed String
	 */
	public String composeShareReminderXml(ShareReminder reminder) {

		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("<sharereminder ");
			if (reminder.getFrom() != null) {
				buffer.append("from=" + quotes + reminder.getFrom() + quotes);
			}
			if (reminder.getTo() != null) {
				buffer.append(" to=" + quotes + reminder.getTo() + quotes);
			}

			if (reminder.getType() != null) {
				buffer.append(" type=" + quotes + reminder.getType() + quotes);
			}

			if (reminder.getReceiverStatus() != null) {
				buffer.append(" receiverstatus=" + quotes
						+ reminder.getReceiverStatus() + quotes);
			}

			if (reminder.getReminderStatus() != null) {
				buffer.append(" reminderstatus=" + quotes
						+ reminder.getReminderStatus() + quotes);
			}

			if (reminder.getFileLocation() != null) {
				buffer.append(" filelocation=" + quotes
						+ reminder.getFileLocation() + quotes);
			}
			if (reminder.getReminderdatetime() != null) {
				buffer.append(" reminderdatetime=" + quotes
						+ reminder.getReminderdatetime() + quotes);
			}
			if (reminder.getRemindertz() != null) {
				buffer.append(" remindertz=" + quotes
						+ reminder.getRemindertz() + quotes);
			}

			if (reminder.getFileid() != null) {
				buffer.append(" fileid=" + quotes + reminder.getFileid()
						+ quotes);
			}
			if (reminder.getReminderResponseType() != null) {
				buffer.append(" responsetype=" + quotes
						+ reminder.getReminderResponseType() + quotes);
			}
			if (reminder.getisbusyResponse() != null) {
				buffer.append(" busynote=" + quotes
						+ reminder.getisbusyResponse() + quotes);
			}
			if (reminder.getDownloadType() != null) {
				buffer.append(" downloadtype=" + quotes
						+ reminder.getDownloadType() + quotes);
			}
			if (reminder.getShareComment() != null) {
				buffer.append(" comment=" + quotes + reminder.getShareComment()
						+ quotes);
			}
			if (reminder.getVanishMode() != null) {
				buffer.append(" vanishmode=" + quotes
						+ reminder.getVanishMode() + quotes);
			}
			if (reminder.getVanishValue() != null) {
				buffer.append(" vanishvalue=" + quotes
						+ reminder.getVanishValue() + quotes);
			}
			if (reminder.getBstype() != null) {
				buffer.append(" bscategory=" + quotes + reminder.getBstype()
						+ quotes);
			}
			if (reminder.getBsstatus() != null) {
				buffer.append(" bsstatus=" + quotes + reminder.getBsstatus()
						+ quotes);
			}
			if (reminder.getDirection() != null) {
				buffer.append(" bsdirection=" + quotes
						+ reminder.getDirection() + quotes);
			}
			if (reminder.getParent_id() != null) {
				buffer.append(" parentid=" + quotes + reminder.getParent_id()
						+ quotes);
			}
			buffer.append(" />");

			Log.d("xml", "" + buffer);

			return getcreateReqResMsg(buffer.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

    public String forgotpasswordSnazMed(String[] parm) {

		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("<Forgotpassword ");
			if (CallDispatcher.LoginUser != null) {
				buffer.append("username=" + quotes + CallDispatcher.LoginUser + quotes);
			}
			if (parm[0] != null) {
				buffer.append(" secquestion1=" + quotes + parm[0] + quotes);
				buffer.append(" secanswer1=" + quotes + parm[1] + quotes);
			}

            if (parm[0] != null) {
                buffer.append(" secquestion2=" + quotes + parm[2] + quotes);
                buffer.append(" secanswer2=" + quotes + parm[3] + quotes);
            }

            if (parm[0] != null) {
                buffer.append(" secquestion3=" + quotes + parm[4] + quotes);
                buffer.append(" secanswer3=" + quotes + parm[5] + quotes);
            }
			buffer.append(" />");

			Log.d("xml", "" + buffer);

			return "<?xml version=\"1.0\"?>" + buffer.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Used to compose Request or Reject Message.
	 * 
	 * @param text
	 *            user Input
	 * @return composed String
	 */
	public String getcreateReqResMsg(String text) {
		String ret = "";
		try {
			String message = "";
			int len = 0, emplen = 0;
			message = "<?xml version=\"1.0\"?>"
					+ "<protostart size=\"\"></protostart>";
			message = message + text;
			len = message.length();
			Integer i = new Integer(len);
			emplen = i.toString().length();
			len = len + emplen;
			ret = "<?xml version=\"1.0\"?>" + "<protostart size=\"" + len
					+ "\">";
			ret = ret + text + "</protostart>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public String secretAnswerXml(String[] secQuestion) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<secret ");
		buffer.append(" name=" + quotes + secQuestion[0] + quotes);

		buffer.append(" secquestion=" + quotes + secQuestion[1] + quotes);
		buffer.append(" secanswer=" + quotes + secQuestion[2] + quotes);
		buffer.append(" newpwd=" + quotes + secQuestion[3] + quotes);

		buffer.append(" />");
		return getcreateSecretAnswerXML(buffer.toString());
	}

	public String getcreateSecretAnswerXML(String text) {

		String ret = "";
		try {

			ret = "<?xml version=\"1.0\"?>" + "<com size=\"" + 1 + "\">";
			ret = ret + text + "</com>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	public String composeGetProfessionsXml(String user, String password) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<profession ");
		
		buffer.append(" userid=" + quotes + user + quotes);
		buffer.append(" password=" + quotes + password + quotes);

		buffer.append(" />");
		return "<?xml version=\"1.0\"?>" + buffer.toString();
	}
	public String composesetProfessionPermissionXml(String user, String password, String value) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<profession ");
		
		buffer.append(" userid=" + quotes + user + quotes);
		buffer.append(" password=" + quotes + password + quotes);
		buffer.append(" searchby=" + quotes + value + quotes);

		buffer.append(" />");
		return "<?xml version=\"1.0\"?>" + buffer.toString();
	}

	public String composeUdpAcknowledgement(UdpMessageBean UMB) {
		String ret = "";
		ret = "<?xml version=\"1.0\"?><com type=" + quotes + UMB.getType()
				+ quotes + " sid=" + quotes + UMB.getSid() + quotes + ">";
		ret = ret + "</com>";
		Log.e("lg", "xml" + ret);
		return ret;

	}

	public String composeForCallConfiurationInfo(String userid,
			ArrayList<OfflineRequestConfigBean> configList,
			ArrayList<OfflineRequestConfigBean> buddyConfigList) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><offlineconfig");
			if (userid != null) {
				buffer.append(" userid=" + quotes + userid + quotes);

			}
			buffer.append(">");

			for (int i = 0; i < configList.size(); i++) {
				OfflineRequestConfigBean bean = configList.get(i);

				buffer.append("<defaultconfig");

				if (bean.getBuddyId() != null)
					buffer.append(" buddyid=" + quotes + bean.getBuddyId()
							+ quotes);

				if (bean.getMessageTitle() != null)
					buffer.append(" messagetitle=" + quotes
							+ bean.getMessageTitle() + quotes);

				if (bean.getMessagetype() != null)
					buffer.append(" messagetype=" + quotes
							+ bean.getMessagetype() + quotes);

				if (bean.getMessage() != null)
					buffer.append(" message=" + quotes + bean.getMessage()
							+ quotes);

				if (bean.getResponseType() != null)
					buffer.append(" responsetype=" + quotes
							+ bean.getResponseType() + quotes);

				if (bean.getId() != null)
					buffer.append(" id=" + quotes + bean.getId() + quotes);

				if (bean.getMode() != null)
					buffer.append(" mode=" + quotes + bean.getMode() + quotes);

				if (bean.getUrl() != null)
					buffer.append(" url=" + quotes + bean.getUrl() + quotes);

				if (bean.getWhen() != null)
					buffer.append(" when=" + quotes + bean.getWhen() + quotes);

				buffer.append("/>");
			}
			if (buddyConfigList != null) {
				for (int i = 0; i < buddyConfigList.size(); i++) {
					OfflineRequestConfigBean bean = buddyConfigList.get(i);

					buffer.append("<buddyconfig");

					if (bean.getBuddyId() != null)
						buffer.append(" buddyid=" + quotes + bean.getBuddyId()
								+ quotes);

					if (bean.getMessageTitle() != null)
						buffer.append(" messagetitle=" + quotes
								+ bean.getMessageTitle() + quotes);

					if (bean.getMessagetype() != null)
						buffer.append(" messagetype=" + quotes
								+ bean.getMessagetype() + quotes);

					if (bean.getMessage() != null)
						buffer.append(" message=" + quotes + bean.getMessage()
								+ quotes);

					if (bean.getResponseType() != null)
						buffer.append(" responsetype=" + quotes
								+ bean.getResponseType() + quotes);

					if (bean.getId() != null)
						buffer.append(" id=" + quotes + bean.getId() + quotes);

					if (bean.getMode() != null)
						buffer.append(" mode=" + quotes + bean.getMode()
								+ quotes);

					if (bean.getUrl() != null)
						buffer.append(" url=" + quotes + bean.getUrl() + quotes);

					if (bean.getWhen() != null)
						buffer.append(" when=" + quotes + bean.getWhen()
								+ quotes);

					buffer.append("/>");
				}
			}
			buffer.append("</offlineconfig>");

			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	@SuppressWarnings("finally")
	public String composeGetConfigurationResponDetails(String[] params) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?><com><ConfigResponses");
			if (params[0] != null)
				buffer.append(" userid=" + quotes + params[0] + quotes);
			if (params[1] != null)
				buffer.append(" buddyid=" + quotes + params[1] + quotes);
			else
				buffer.append(" buddyid=" + quotes + quotes);
			if (params[2] != null)
				buffer.append(" configid=" + quotes + params[2] + quotes);
			buffer.append("/>");
			buffer.append("</com>");

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			return buffer.toString();
		}
	}

	@SuppressWarnings("finally")
	public String composeDeleteMyResponseDetail(String[] params) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?><com><DeleteResponses");
			if (params[0] != null)
				buffer.append(" userid=" + quotes + params[0] + quotes);
			if (params[1] != null)
				buffer.append(" buddyid=" + quotes + params[1] + quotes);

			if (params[2] != null)
				buffer.append(" configid=" + quotes + params[2] + quotes);
			buffer.append("/>");
			buffer.append("</com>");
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			return buffer.toString();
		}
	}

	@SuppressWarnings("finally")
	public String composeOfflineCallResponsexml(String[] details) {
		StringBuffer buffer = new StringBuffer();
		try {

			buffer.append("<?xml version=\"1.0\"?><com><offlineCallResponse");

			if (details[0] != null)
				buffer.append(" userid=" + quotes + details[0] + quotes);

			if (details[1] != null)
				buffer.append(" buddyid=" + quotes + details[1] + quotes);

			if (details[2] != null)
				buffer.append(" when=" + quotes + details[2] + quotes);

			buffer.append(">");

			buffer.append("</offlineCallResponse></com>");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return buffer.toString();
		}

	}

	public String composeResponseForCallConfigurationxml(
			OfflineRequestConfigBean bean) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>"
					+ "<com><ConfigurationResponse");
			if (bean.getUserId() != null)
				buffer.append(" userid=" + quotes + bean.getUserId() + quotes
						+ ">");

			buffer.append("<responsedetail");

			if (bean.getBuddyId() != null)
				buffer.append(" buddyid=" + quotes + bean.getBuddyId() + quotes);

			buffer.append(" configid=" + quotes + bean.getConfig_id() + quotes);

			if (bean.getMessageTitle() != null)
				buffer.append(" messagetitle=" + quotes
						+ bean.getMessageTitle() + quotes);

			if (bean.getResponseType() != null)
				buffer.append(" responsetype=" + quotes
						+ bean.getResponseType() + quotes);

			if (bean.getResponse() != null)
				buffer.append(" response=" + quotes + bean.getResponse()
						+ quotes);

			buffer.append("/>");
			buffer.append("</ConfigurationResponse></com>");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public String composeWithDrawnPermissionXML(String userid, String buddyid) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><DeleteAllShares");
			if (userid != null) {
				buffer.append(" userid=" + quotes + userid + quotes);
			}
			if (buddyid != null) {
				buffer.append(" buddyid=" + quotes + buddyid + quotes);
			}
			buffer.append(">");

			buffer.append("</DeleteAllShares>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	@SuppressWarnings("finally")
	public String composeSetPermissionXML(PermissionBean bean) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><Setpermission");
			if (bean.getUserId() != null)
				buffer.append(" userid=" + quotes + bean.getUserId() + quotes);
			if (bean.getBuddyId() != null)
				buffer.append(" buddyid=" + quotes + bean.getBuddyId() + quotes);
			if (bean.getAudio_call() != null)
				buffer.append(" audiocall=" + quotes + bean.getAudio_call()
						+ quotes);
			if (bean.getVideo_call() != null)
				buffer.append(" videocall=" + quotes + bean.getVideo_call()
						+ quotes);
			if (bean.getABC() != null)
				buffer.append(" audiobroadcast=" + quotes + bean.getABC()
						+ quotes);
			if (bean.getVBC() != null)
				buffer.append(" videobroadcast=" + quotes + bean.getVBC()
						+ quotes);
			if (bean.getAUC() != null)
				buffer.append(" audiounicast=" + quotes + bean.getAUC()
						+ quotes);
			if (bean.getVUC() != null)
				buffer.append(" videounicast=" + quotes + bean.getVUC()
						+ quotes);
			if (bean.getMMchat() != null)
				buffer.append(" mmchat=" + quotes + bean.getMMchat() + quotes);
			if (bean.getTextMessage() != null)
				buffer.append(" textmessage=" + quotes + bean.getTextMessage()
						+ quotes);
			if (bean.getPhotoMessage() != null)
				buffer.append(" photomessage=" + quotes
						+ bean.getPhotoMessage() + quotes);
			if (bean.getAudioMessage() != null)
				buffer.append(" audiomessage=" + quotes
						+ bean.getAudioMessage() + quotes);
			if (bean.getVideoMessage() != null)
				buffer.append(" videomessage=" + quotes
						+ bean.getVideoMessage() + quotes);
			if (bean.getSketchMessage() != null)
				buffer.append(" sketchmessage=" + quotes
						+ bean.getSketchMessage() + quotes);
			if (bean.getShareProfile() != null)
				buffer.append(" shareprofile=" + quotes
						+ bean.getShareProfile() + quotes);
			if (bean.getViewProfile() != null)
				buffer.append(" viewprofile=" + quotes + bean.getViewProfile()
						+ quotes);
			if (bean.getAvtaar() != null)
				buffer.append(" answeringmachine=" + quotes + bean.getAvtaar()
						+ quotes);
			if (bean.getFormshare() != null)
				buffer.append(" formshare=" + quotes + bean.getFormshare()
						+ quotes);
			if (bean.getHostedconf() != null)
				buffer.append(" hostedconference=" + quotes
						+ bean.getHostedconf() + quotes);
			if (bean.getRecordTime() != null)
				buffer.append(" recordtime=" + quotes + bean.getRecordTime()
						+ quotes);
			buffer.append("/>");
			buffer.append("</com>");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return buffer.toString();
		}
	}

	@SuppressWarnings("finally")
	public String composeGetAllPermissionXML(String[] values) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?><com><GetPermission");
			if (values[0] != null)
				buffer.append(" userid=" + quotes + values[0] + quotes);
			if (values[1] != null)
				buffer.append(" buddyid=" + quotes + values[1] + quotes);
			if (values[2] != null)
				buffer.append(" datetime=" + quotes + values[2] + quotes);

			buffer.append("/>");
			buffer.append("</com>");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return buffer.toString();
		}
	}

	@SuppressWarnings("finally")
	public String composeSetandGetutilityItems(UtilityBean utilityBean) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?><com><FormDetails");
			if (utilityBean.getUsername() != null)
				buffer.append(" userid=" + quotes + utilityBean.getUsername()
						+ quotes);
			if (utilityBean.getType() != null)
				buffer.append(" type=" + quotes + utilityBean.getType()
						+ quotes);
			buffer.append(" utilityid=" + quotes + utilityBean.getId() + quotes);

			if (utilityBean.getResult() != null)
				buffer.append(" result=" + quotes + utilityBean.getResult()
						+ quotes);

			if (utilityBean.getUtility_name() != null)
				buffer.append(" utilityname=" + quotes
						+ utilityBean.getUtility_name() + quotes);
			if (utilityBean.getProduct_name() != null)
				if (utilityBean.getUtility_name().equalsIgnoreCase("buy")
						|| utilityBean.getUtility_name().equalsIgnoreCase(
								"sell")) {
					buffer.append(" productname=" + quotes
							+ utilityBean.getProduct_name() + quotes);
				} else {
					buffer.append(" servicename=" + quotes
							+ utilityBean.getProduct_name() + quotes);
				}
			if (utilityBean.getPrice() != null)
				buffer.append(" price=" + quotes + utilityBean.getPrice()
						+ quotes);
			if (utilityBean.getQty() != null)
				buffer.append(" quantity=" + quotes + utilityBean.getQty()
						+ quotes);
			if (utilityBean.getNameororg() != null)
				if (utilityBean.getUtility_name().equalsIgnoreCase("buy")
						|| utilityBean.getUtility_name().equalsIgnoreCase(
								"sell")) {
					buffer.append(" orgname=" + quotes
							+ utilityBean.getNameororg() + quotes);
				} else {
					buffer.append(" name=" + quotes
							+ utilityBean.getNameororg() + quotes);
				}
			if (utilityBean.getAddress() != null)
				buffer.append(" address=" + quotes + utilityBean.getAddress()
						+ quotes);
			if (utilityBean.getLocation() != null)
				buffer.append(" location=" + quotes + utilityBean.getLocation()
						+ quotes);
			if (utilityBean.getCityordist() != null)
				buffer.append(" city=" + quotes + utilityBean.getCityordist()
						+ quotes);
			if (utilityBean.getState() != null)
				buffer.append(" state=" + quotes + utilityBean.getState()
						+ quotes);
			if (utilityBean.getCountry() != null)
				buffer.append(" country=" + quotes + utilityBean.getCountry()
						+ quotes);
			if (utilityBean.getPin() != null)
				buffer.append(" pin=" + quotes + utilityBean.getPin() + quotes);
			if (utilityBean.getImag_filename() != null)
				buffer.append(" photo=" + quotes
						+ utilityBean.getImag_filename() + quotes);
			if (utilityBean.getAudiofilename() != null)
				buffer.append(" audio=" + quotes
						+ utilityBean.getAudiofilename() + quotes);
			if (utilityBean.getVideofilename() != null)
				buffer.append(" video=" + quotes
						+ utilityBean.getVideofilename() + quotes);
			if (utilityBean.getC_no() != null)
				buffer.append(" phone=" + quotes + utilityBean.getC_no()
						+ quotes);
			if (utilityBean.getPosted_date() != null)
				buffer.append(" posteddate=" + quotes
						+ utilityBean.getPosted_date() + quotes);
			buffer.append(" mode=" + quotes + utilityBean.getMode() + quotes);
			buffer.append("/>");
			buffer.append("</com>");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return buffer.toString();
		}
	}

	public String composeSetStandardProfileFieldValuesXML(String[] params,
			Vector<FieldTemplateBean> fields_list) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><StandardProfile");
			if (params != null) {
				buffer.append(" userid=" + quotes + params[0] + quotes
						+ " profileid=" + quotes + params[1] + quotes);
			}

			buffer.append(">");
			for (FieldTemplateBean fBean : fields_list) {
				if (fBean.getFiledvalue() != null
						&& fBean.getFiledvalue().length() > 0) {
					buffer.append("<Fieldvalue fieldid=" + quotes
							+ fBean.getFieldId() + quotes + " fieldvalue="
							+ quotes + fBean.getFiledvalue() + quotes + " >");
					buffer.append("</Fieldvalue>");
				}

			}

			buffer.append("</StandardProfile>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String composeProfileFieldValuesXMLForMultimedia(String[] params,
			FieldTemplateBean fBean) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><StandardProfile");
			if (params != null) {
				buffer.append(" userid=" + quotes + params[0] + quotes
						+ " profileid=" + quotes + params[1] + quotes);
			}

			buffer.append(">");

			if (fBean.getFiledvalue() != null
					&& fBean.getFiledvalue().length() > 0) {
				buffer.append("<Fieldvalue fieldid=" + quotes
						+ fBean.getFieldId() + quotes + " fieldvalue=" + quotes
						+ fBean.getFiledvalue() + quotes + " >");
				buffer.append("</Fieldvalue>");
			}

			buffer.append("</StandardProfile>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String composeGetStandardProfileFieldValuesXML(String[] params) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>"
					+ "<com><StandardProfileDetails");
			if (params != null) {
				buffer.append(" userid=" + quotes + params[0] + quotes
						+ " profileid=" + quotes + params[1] + quotes
						+ " profilemodifiedtime=" + quotes + params[2] + quotes);
			}

			buffer.append(">");
			buffer.append("</StandardProfileDetails>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String ComposeDeleteProfileXML(String userid, String profileid) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><deleteprofile");
			if (userid != null) {
				buffer.append(" userid=" + quotes + userid + quotes);
			}
			if (profileid != null) {
				buffer.append(" profileid=" + quotes + profileid + quotes);
			}
			buffer.append(">");

			buffer.append("</deleteprofile>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String composeGetUtilitySyncItemsXML(String userId,
			String utilityName, Vector<UtilityBean> utilityList) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><utilityDetails");
			if (userId != null) {
				buffer.append(" userId=" + quotes + userId + quotes
						+ " utilityName=" + quotes + utilityName + quotes);
			}
			buffer.append(">");
			for (UtilityBean bean : utilityList) {
				buffer.append("<utilityItem utilityId=" + quotes + bean.getId()
						+ quotes + " postedDate=" + quotes
						+ bean.getPosted_date() + quotes + " >");
				buffer.append("</utilityItem>");
			}
			buffer.append("</utilityDetails>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String composeCreateGroup(GroupBean bean) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><CreateorModifyGroup");
			if (bean != null) {
				if (bean.getOwnerName() != null)
					buffer.append(" ownerName=" + quotes + bean.getOwnerName()
							+ quotes);
				if (bean.getGroupId() != null)
					buffer.append(" groupId=" + quotes + bean.getGroupId()
							+ quotes);
				if (bean.getGroupdescription() != null)
					buffer.append(" groupDescription=" + quotes + bean.getGroupdescription() + quotes);
				if (bean.getGroupIcon() != null)
					buffer.append(" groupIcon=" + quotes + bean.getGroupIcon() + quotes);
				if (bean.getGroupName() != null)
					buffer.append(" groupName=" + quotes + bean.getGroupName()
							+ quotes);
				if (bean.getGroupMembers() != null)
					buffer.append(" addGroupMembers=" + quotes
							+ bean.getGroupMembers() + quotes);
				if (bean.getDeleteGroupMembers() != null)
					buffer.append(" deleteGroupMembers=" + quotes
							+ bean.getDeleteGroupMembers() + quotes);
			}
			buffer.append(">");

			buffer.append("</CreateorModifyGroup>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String composeGroupMembersAdd(GroupBean bean) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><GroupMembersAdd");
			if (bean != null) {
				if (bean.getOwnerName() != null)
					buffer.append(" ownerName=" + quotes + bean.getOwnerName()
							+ quotes);
				if (bean.getGroupId() != null)
					buffer.append(" groupId=" + quotes + bean.getGroupId()
							+ quotes);
				if (bean.getGroupMembers() != null)
					buffer.append(" groupMembers=" + quotes
							+ bean.getGroupMembers() + quotes);
			}
			buffer.append(">");

			buffer.append("</GroupMembersAdd>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String composeGroupMembersDelete(GroupBean bean) {

		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>"
					+ "<com><GroupMembersDelete");
			if (bean != null) {
				if (bean.getOwnerName() != null)
					buffer.append(" ownerName=" + quotes + bean.getOwnerName()
							+ quotes);
				if (bean.getGroupId() != null)
					buffer.append(" groupId=" + quotes + bean.getGroupId()
							+ quotes);
				if (bean.getGroupMembers() != null)
					buffer.append(" groupMembers=" + quotes
							+ bean.getGroupMembers() + quotes);
			}
			buffer.append(">");

			buffer.append("</GroupMembersDelete>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();

	}

	public String composeGroupModified(GroupBean gBean) {

		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><ModifyGroup");
			if (gBean != null) {
				if (gBean.getOwnerName() != null)
					buffer.append(" ownerName=" + quotes + gBean.getOwnerName()
							+ quotes);
				if (gBean.getGroupId() != null)
					buffer.append(" groupId=" + quotes + gBean.getGroupId()
							+ quotes);
				if (gBean.getGroupName() != null)
					buffer.append(" groupName=" + quotes + gBean.getGroupName()
							+ quotes);
				if (gBean.getGroupMembers() != null)
					buffer.append(" addGroupMembers=" + quotes
							+ gBean.getGroupMembers() + quotes);
				if (gBean.getDeleteGroupMembers() != null)
					buffer.append(" deleteGroupMembers=" + quotes
							+ gBean.getDeleteGroupMembers() + quotes);
			}
			buffer.append(">");

			buffer.append("</ModifyGroup>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();

	}

	public String composeGroupChatSettings(GroupBean gBean,
			Vector<GroupChatPermissionBean> gcpList) {

		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><GroupSettings");
			if (gBean != null) {
				if (gBean.getOwnerName() != null)
					buffer.append(" ownerName=" + quotes + gBean.getOwnerName()
							+ quotes);
				if (gBean.getGroupId() != null)
					buffer.append(" groupId=" + quotes + gBean.getGroupId()
							+ quotes);
				if (gBean.getType() != null)
					buffer.append(" type=" + quotes + gBean.getType() + quotes);
				if (gBean.getGroupMembers() != null)
					buffer.append(" groupMember=" + quotes
							+ gBean.getGroupMembers() + quotes);
				buffer.append(">");
				for (GroupChatPermissionBean gpBean : gcpList) {
					buffer.append("<settings");
					if (gpBean.getGroupMember() != null)
						buffer.append(" groupMember=" + quotes
								+ gpBean.getGroupMember() + quotes);
					if (gpBean.getAudioConference() != null)
						buffer.append(" audioConference=" + quotes
								+ gpBean.getAudioConference() + quotes);
					if (gpBean.getVideoConference() != null)
						buffer.append(" videoConference=" + quotes
								+ gpBean.getVideoConference() + quotes);
					if (gpBean.getAudioBroadcast() != null)
						buffer.append(" audioBroadcast=" + quotes
								+ gpBean.getAudioBroadcast() + quotes);
					if (gpBean.getVideoBroadcast() != null)
						buffer.append(" videoBroadcast=" + quotes
								+ gpBean.getVideoBroadcast() + quotes);
					if (gpBean.getTextMessage() != null)
						buffer.append(" textMessage=" + quotes
								+ gpBean.getTextMessage() + quotes);
					if (gpBean.getAudioMessage() != null)
						buffer.append(" audioMessage=" + quotes
								+ gpBean.getAudioMessage() + quotes);
					if (gpBean.getVideoMessage() != null)
						buffer.append(" videoMessage=" + quotes
								+ gpBean.getVideoMessage() + quotes);
					if (gpBean.getPhotoMessage() != null)
						buffer.append(" photoMessage=" + quotes
								+ gpBean.getPhotoMessage() + quotes);
					if (gpBean.getPrivateMessage() != null)
						buffer.append(" privateMessage=" + quotes
								+ gpBean.getPrivateMessage() + quotes);
					if (gpBean.getReplyBackMessage() != null)
						buffer.append(" reply=" + quotes
								+ gpBean.getReplyBackMessage() + quotes);
					if (gpBean.getScheduleMessage() != null)
						buffer.append(" schedule=" + quotes
								+ gpBean.getScheduleMessage() + quotes);
					if (gpBean.getDeadLineMessage() != null)
						buffer.append(" deadLine=" + quotes
								+ gpBean.getDeadLineMessage() + quotes);
					if (gpBean.getWithDrawn() != null)
						buffer.append(" withDraw=" + quotes
								+ gpBean.getWithDrawn() + quotes);
					if (gpBean.getChat() != null)
						buffer.append(" chat=" + quotes
								+ gpBean.getChat() + quotes);
					buffer.append("/>");
				}

			}
			buffer.append("</GroupSettings>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();

	}

	public String getWellFormedXml(String text, String userid, String type,
			String key) {
		String ret = "";
		ret = "<?xml version=\"1.0\"?><com type=" + quotes + type + quotes
				+ " userid=" + quotes + userid + quotes + " key=" + quotes
				+ key + quotes + ">";
		ret = ret + "</com>";
		return ret;
	}

	public String ComposegetFormAttributeDataXML(String form_owner,
			String formId) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com>");
			buffer.append("<FormAttributes userid=" + quotes + form_owner
					+ quotes + " formid=" + quotes + formId + quotes + " />");

			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String composeGroupChatXML(GroupChatBean bean) {
		StringBuilder builder = null;
		try {
            String temp = null;
            try{
                if(!bean.getMessage().equals(null)) {
                    byte[] data = bean.getMessage().getBytes("UTF-8");
                    String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                    temp = base64;
                }else{
                    temp = bean.getMessage();
                }
            }catch (Exception e){
                Log.d("AAA","XC error => "+e.toString());
                temp = bean.getMessage();
            }
			builder = new StringBuilder();
			builder.append("<?xml version=\"1.0\"?>");
			builder.append("<com result=\"");
			builder.append("0");
			builder.append("\" type=\"");
			builder.append(bean.getType());
			builder.append("\"><tl category=\"");
			builder.append(bean.getCategory());
			builder.append("\" subcategory=\"");
			builder.append(bean.getSubCategory());
			builder.append("\" mimetype=\"");
			if(bean.getMessage()!=null &&bean.getMessage().length()>0 && bean.getMediaName()!=null)
				builder.append("mixedfile");
			else
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
			builder.append("\" senttimez=\"Asia/Kolkata");
//			builder.append(bean.getSenttimez());
			builder.append("\" privatemembers=\"");
			builder.append(bean.getPrivateMembers());
			builder.append("\" remindertime=\"");
			builder.append(bean.getReminderTime());
			builder.append("\" parentid=\"");
			builder.append(bean.getParentId());
			builder.append("\"/><message>");
			builder.append("<![CDATA[");
			builder.append(temp);
			builder.append("]]>");
			builder.append("</message>");
			builder.append("<media filename=\"");
			builder.append(bean.getMediaName());
			builder.append("\" comment=\"");
			builder.append(bean.getComment());
			builder.append("\" filetitle=\"");
			builder.append(bean.getFiletitle());
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

	public String composeGroupChat102XML(GroupChatBean bean) {

		StringBuilder builder = null;
		try {
			builder = new StringBuilder();
			builder.append("<?xml version=\"1.0\"?>");
			builder.append("<com result=\"");
			builder.append(bean.getResult());
			builder.append("\" type=\"");
			builder.append("102");
			builder.append("\" sid=\"");
			builder.append(bean.getSid());
			builder.append("\"><tl chatid=\"");
			builder.append(bean.getSessionid());
			builder.append("\" from=\"");
			builder.append(CallDispatcher.LoginUser);
			builder.append("\" to=\"");
			builder.append(bean.getTo());
			builder.append("\" signalid=\"");
			builder.append(bean.getSignalid());
			builder.append("\"/>");
			builder.append("</com>");
			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    public String composeGroupChat101XML(GroupChatBean bean) {

        StringBuilder builder = null;
        try {
            builder = new StringBuilder();
            builder.append("<?xml version=\"1.0\"?>");
            builder.append("<com result=\"");
            builder.append(bean.getResult());
            builder.append("\" type=\"");
            builder.append("101");
//            builder.append("\" sid=\"");
//            builder.append(bean.getSid());
            builder.append("\"><tl sessionid=\"");
            builder.append(bean.getSessionid());
            builder.append("\" from=\"");
            builder.append(CallDispatcher.LoginUser);
            builder.append("\" to=\"");
            builder.append(bean.getFrom());
            builder.append("\" signalid=\"");
            builder.append(bean.getSignalid());
            builder.append("\" datetimewithzone=\"");
            builder.append(bean.getSenttime());
            builder.append("\"/>");
            builder.append("</com>");
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String composeGroupChat105XML(GroupChatBean bean) {

        StringBuilder builder = null;
        try {
            builder = new StringBuilder();
            builder.append("<?xml version=\"1.0\"?>");
            builder.append("<com result=\"");
            builder.append(bean.getResult());
            builder.append("\" type=\"");
            builder.append("105");
//            builder.append("\" sid=\"");
//            builder.append(bean.getSid());
            builder.append("\"><tl sessionid=\"");
            builder.append(bean.getSessionid());
            builder.append("\" from=\"");
            builder.append(CallDispatcher.LoginUser);
            builder.append("\" to=\"");
            builder.append(bean.getTo());
            builder.append("\" category=\"");
            builder.append(bean.getCategory());
            builder.append("\" dateandtime=\"");
            builder.append(bean.getSenttime());
            builder.append("\"/>");
            builder.append("</com>");
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	public String composeGroupChatDeleteMsgXML(GroupChatBean bean) {

		StringBuilder builder = null;
		try {
			builder = new StringBuilder();
			builder.append("<?xml version=\"1.0\"?>");
			builder.append("<com result=\"");
			builder.append("0");
			builder.append("\" type=\"");
			builder.append(bean.getType());
			builder.append("\"><tl category=\"");
			builder.append(bean.getCategory());
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
			builder.append("\" psignalid=\"");
			builder.append(bean.getpSingnalId());
			builder.append("\" dateandtime=\"");
			builder.append(bean.getSenttime());
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

	public String composeSetorDeleteFormsFieldSettings(
			FormFieldSettingsBean settingsBean,
			Vector<DefaultPermission> dPermissionList) {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com>");
			buffer.append("<FormsFieldSettings");
			if (settingsBean.getFormId() != null) {
				buffer.append(" formid=" + quotes + settingsBean.getFormId()
						+ quotes);
			}
			if (settingsBean.getUserId() != null) {
				buffer.append(" userid=" + quotes + settingsBean.getUserId()
						+ quotes);
			}
			if (settingsBean.getMode() != null) {
				buffer.append(" mode=" + quotes + settingsBean.getMode()
						+ quotes);
			}
			buffer.append(">");
			for (DefaultPermission dPermission : dPermissionList) {
				buffer.append("<Attribute");
				if (dPermission.getAttributeId() != null) {
					buffer.append(" attributeid=" + quotes
							+ dPermission.getAttributeId() + quotes);
				}
				if (getPermissionId(dPermission.getDefaultPermission()) != null) {
					buffer.append(" defaultpermission="
							+ quotes
							+ getPermissionId(dPermission
									.getDefaultPermission()) + quotes);
				}
				buffer.append(">");
				if (dPermission.getBuddyPermissionList() != null) {
					Vector<BuddyPermission> bPermissionList = dPermission
							.getBuddyPermissionList();
					for (BuddyPermission bPermission : bPermissionList) {
						buffer.append("<FieldAccess");
						if (bPermission.getBuddyName() != null) {
							buffer.append(" accessiblebuddy=" + quotes
									+ bPermission.getBuddyName() + quotes);
						}
						if (getPermissionId(bPermission.getBuddyAccess()) != null) {
							buffer.append(" permissionid="
									+ quotes
									+ getPermissionId(bPermission
											.getBuddyAccess()) + quotes);
						}
						buffer.append("/>");
					}
				}
				buffer.append("</Attribute>");
			}
			buffer.append("</FormsFieldSettings>");

			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

//	public String composeSaveSurveyApplicationForm(
//			SurveyApplicantFormBean saveFormBean) {
//		// TODO Auto-generated method stub
//		StringBuffer buffer = new StringBuffer();
//		try {
//			buffer.append("<?xml version=\"1.0\"?>" + "<com>");
//			buffer.append("<SurveyForm");
//			if (saveFormBean.getApplicationnumber() != null) {
//				buffer.append(" applicationNumber=" + quotes
//						+ saveFormBean.getApplicationnumber() + quotes);
//			}
//			if (saveFormBean.getCsocietyname() != null) {
//				buffer.append(" cSocietyName=" + quotes
//						+ saveFormBean.getCsocietyname() + quotes);
//			}
//			if (saveFormBean.getType() != null) {
//				buffer.append(" type=" + quotes + saveFormBean.getType()
//						+ quotes);
//			}
//			if (saveFormBean.getThana() != null) {
//				buffer.append(" thana=" + quotes + saveFormBean.getThana()
//						+ quotes);
//			}
//			if (saveFormBean.getDistrict() != null) {
//				buffer.append(" district=" + quotes
//						+ saveFormBean.getDistrict() + quotes);
//			}
//			if (saveFormBean.getApplicationName() != null) {
//				buffer.append(" applicationName=" + quotes
//						+ saveFormBean.getApplicationName() + quotes);
//			}
//			if (saveFormBean.getApplicantage() > 0) {
//				buffer.append(" applicantAge=" + quotes
//						+ saveFormBean.getApplicantage() + quotes);
//			}
//			if (saveFormBean.getApplicantQualification() != null) {
//				buffer.append(" qualification=" + quotes
//						+ saveFormBean.getApplicantQualification() + quotes);
//			}
//			if (saveFormBean.getNid() != null) {
//				buffer.append(" nId=" + quotes + saveFormBean.getNid() + quotes);
//			}
//			if (saveFormBean.getpOccupation() != null) {
//				buffer.append(" pOccupation=" + quotes
//						+ saveFormBean.getpOccupation() + quotes);
//			}
//
//			if (saveFormBean.getDiseases() != null) {
//				buffer.append(" diseases=" + quotes
//						+ saveFormBean.getDiseases() + quotes);
//			}
//			if (saveFormBean.getMp() != null) {
//				buffer.append(" mp=" + quotes + saveFormBean.getMp() + quotes);
//			}
//			if (saveFormBean.getLeaveplace() != null) {
//				buffer.append(" leavePlace=" + quotes
//						+ saveFormBean.getLeaveplace() + quotes);
//			}
//			if (saveFormBean.getGaurdiandetails() != null) {
//				buffer.append(" gaurdianDetails=" + quotes
//						+ saveFormBean.getGaurdiandetails() + quotes);
//			}
//			if (saveFormBean.getGname() != null) {
//				buffer.append(" gName=" + quotes + saveFormBean.getGname()
//						+ quotes);
//			}
//			if (saveFormBean.getpOccupation() != null) {
//				buffer.append(" pOccupation=" + quotes
//						+ saveFormBean.getpOccupation() + quotes);
//			}
//
//			if (saveFormBean.getDiseases() != null) {
//				buffer.append(" diseases=" + quotes
//						+ saveFormBean.getDiseases() + quotes);
//			}
//			if (saveFormBean.getMp() != null) {
//				buffer.append(" mp=" + quotes + saveFormBean.getMp() + quotes);
//			}
//			if (saveFormBean.getLeaveplace() != null) {
//				buffer.append(" leavePlace=" + quotes
//						+ saveFormBean.getLeaveplace() + quotes);
//			}
//			if (saveFormBean.getGaurdiandetails() != null) {
//				buffer.append(" gaurdianDetails=" + quotes
//						+ saveFormBean.getGaurdiandetails() + quotes);
//			}
//			if (saveFormBean.getGname() != null) {
//				buffer.append(" gName=" + quotes + saveFormBean.getGname()
//						+ quotes);
//			}
//			if (saveFormBean.getGage() > 0) {
//				buffer.append(" gAge=" + quotes + saveFormBean.getGage()
//						+ quotes);
//			}
//			if (saveFormBean.getGqualification() != null) {
//				buffer.append(" gQualification=" + quotes
//						+ saveFormBean.getGqualification() + quotes);
//			}
//			if (saveFormBean.getGoccupation() != null) {
//				buffer.append(" gOccupation=" + quotes
//						+ saveFormBean.getGoccupation() + quotes);
//			}
//			if (saveFormBean.getMname() != null) {
//				buffer.append(" mName=" + quotes + saveFormBean.getMname()
//						+ quotes);
//			}
//			if (saveFormBean.getMage() > 0) {
//				buffer.append(" mAge=" + quotes + saveFormBean.getMage()
//						+ quotes);
//			}
//			if (saveFormBean.getMqualification() != null) {
//				buffer.append(" mQualification=" + quotes
//						+ saveFormBean.getMqualification() + quotes);
//			}
//			if (saveFormBean.getMoccpation() != null) {
//				buffer.append(" mOccpation=" + quotes
//						+ saveFormBean.getMoccpation() + quotes);
//			}
//			if (saveFormBean.getPraddress() != null) {
//				buffer.append(" prAddress=" + quotes
//						+ saveFormBean.getPraddress() + quotes);
//			}
//
//			if (saveFormBean.getPrvillage() != null) {
//				buffer.append(" prVillage=" + quotes
//						+ saveFormBean.getPrvillage() + quotes);
//			}
//			if (saveFormBean.getPrunionorward() != null) {
//				buffer.append(" prUnionOrWard=" + quotes
//						+ saveFormBean.getPrunionorward() + quotes);
//			}
//			if (saveFormBean.getPrthana() != null) {
//				buffer.append(" prThana=" + quotes + saveFormBean.getPrthana()
//						+ quotes);
//			}
//			if (saveFormBean.getPrdistrict() != null) {
//				buffer.append(" prDistrict=" + quotes
//						+ saveFormBean.getPrdistrict() + quotes);
//			}
//			if (saveFormBean.getPeaddress() != null) {
//				buffer.append(" peAddress=" + quotes
//						+ saveFormBean.getPeaddress() + quotes);
//			}
//			if (saveFormBean.getPeVillage() != null) {
//				buffer.append(" peVillage=" + quotes
//						+ saveFormBean.getPeVillage() + quotes);
//			}
//
//			if (saveFormBean.getPeunionorward() != null) {
//				buffer.append(" peUnionorWard=" + quotes
//						+ saveFormBean.getPeunionorward() + quotes);
//			}
//			if (saveFormBean.getPethana() != null) {
//				buffer.append(" peThana=" + quotes + saveFormBean.getPethana()
//						+ quotes);
//			}
//			if (saveFormBean.getPedistrict() != null) {
//				buffer.append(" peDistrict=" + quotes
//						+ saveFormBean.getPedistrict() + quotes);
//			}
//			if (saveFormBean.getTharea() != null) {
//				buffer.append(" thArea=" + quotes + saveFormBean.getTharea()
//						+ quotes);
//			}
//			if (saveFormBean.getThouse() != null) {
//				buffer.append(" tHouse=" + quotes + saveFormBean.getThouse()
//						+ quotes);
//			}
//			if (saveFormBean.getOwnertotalarea() != null) {
//				buffer.append(" ownerTotalArea=" + quotes
//						+ saveFormBean.getOwnertotalarea() + quotes);
//			}
//			if (saveFormBean.getLeasetotalarea() != null) {
//				buffer.append(" leaseTotalArea=" + quotes
//						+ saveFormBean.getLeasetotalarea() + quotes);
//			}
//			if (saveFormBean.getPedistrict() != null) {
//				buffer.append(" totalProptyValue=" + quotes
//						+ saveFormBean.getTotalproptyvalue() + quotes);
//			}
//			if (saveFormBean.getTfmembers() > 0) {
//				buffer.append(" tfMembers=" + quotes
//						+ saveFormBean.getTfmembers() + quotes);
//			}
//			if (saveFormBean.getTotalboys() > 0) {
//				buffer.append(" totalBoys=" + quotes + saveFormBean.getThouse()
//						+ quotes);
//			}
//			if (saveFormBean.getTotalgirls() > 0) {
//				buffer.append(" totalGirls=" + quotes
//						+ saveFormBean.getTotalgirls() + quotes);
//			}
//			if (saveFormBean.getFamilyheadname() != null) {
//				buffer.append(" familyHeadName=" + quotes
//						+ saveFormBean.getFamilyheadname() + quotes);
//			}
//			if (saveFormBean.getDistance() > 0) {
//				buffer.append(" distance=" + quotes
//						+ saveFormBean.getDistance() + quotes);
//			}
//			if (saveFormBean.getOthersocietyname() != null) {
//				buffer.append(" otherSocietyName=" + quotes
//						+ saveFormBean.getOthersocietyname() + quotes);
//			}
//			if (saveFormBean.getAgreename() != null) {
//				buffer.append(" agreeName=" + quotes
//						+ saveFormBean.getAgreename() + quotes);
//			}
//			if (saveFormBean.getAgreedate() != null) {
//				buffer.append(" agreeDate=" + quotes
//						+ saveFormBean.getAgreedate() + quotes);
//			}
//			if (saveFormBean.getProposernamehog() != null) {
//				buffer.append(" proposerNamehog=" + quotes
//						+ saveFormBean.getProposernamehog() + quotes);
//			}
//			if (saveFormBean.getProposerdate() != null) {
//				buffer.append(" proposerDate=" + quotes
//						+ saveFormBean.getProposerdate() + quotes);
//			}
//			if (saveFormBean.getSurveyorname() != null) {
//				buffer.append(" surveyName=" + quotes
//						+ saveFormBean.getSurveyorname() + quotes);
//			}
//			if (saveFormBean.getSurveyordesignation() != null) {
//				buffer.append(" surveyorDesignation=" + quotes
//						+ saveFormBean.getSurveyordesignation() + quotes);
//			}
//			if (saveFormBean.getInspectornameorassistant() != null) {
//				buffer.append(" inspectorOrAssistantName=" + quotes
//						+ saveFormBean.getInspectornameorassistant() + quotes);
//			}
//			if (saveFormBean.getCreatedby() != null) {
//				buffer.append(" createdby=" + quotes
//						+ saveFormBean.getCreatedby() + quotes);
//			}
//			buffer.append("</SurveyForm>");
//
//			buffer.append("</com>");
//			Log.d("xml", "composed xml" + buffer.toString());
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return buffer.toString();
//	}



	public String composeSubscribeUser(SubscribeBean subscribeBean) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<Registration ");
		if (subscribeBean.getName() != null) {
			buffer.append("name=" + quotes + subscribeBean.getName() + quotes);
		}
		if (subscribeBean.getPassword() != null) {
			buffer.append(" password=" + quotes + subscribeBean.getPassword()
					+ quotes);
		}

		if (subscribeBean.getEmailId() != null) {
			buffer.append(" email=" + quotes + subscribeBean.getEmailId()
					+ quotes);
		}
		// if (subscribeBean.getProfession() != null) {
		// buffer.append(" profession=" + quotes + subscribeBean.getProfession()
		// + quotes);
		// }
		if (subscribeBean.getMobileNo() != null) {
			buffer.append(" mobileno=" + quotes + subscribeBean.getMobileNo()
					+ quotes);
		}
		if (subscribeBean.getPhoto() != null) {
			buffer.append(" photo=" + quotes + subscribeBean.getPhoto()
					+ quotes);
		}
		if (subscribeBean.getSecretQuestion() != null) {
			buffer.append(" secquestion=" + quotes
					+ subscribeBean.getSecretQuestion() + quotes);
		}
		if (subscribeBean.getSecretAnswer() != null) {
			buffer.append(" secanswer=" + quotes
					+ subscribeBean.getSecretAnswer() + quotes);
		}
		if (subscribeBean.getOccupation() != null) {
			buffer.append(" occupation=" + quotes
					+ subscribeBean.getOccupation() + quotes);
		}
		buffer.append(" />");
		return getcreateReqResMsg(buffer.toString());
	}

	/**
	 * Used to compose SignIn XML from the SiginBean.
	 * 
	 * @param sb
	 *            SiginBean contains SignIn details.
	 * @return composed String
	 */

	public String composeUserSiginXml(SiginBean sb) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<UserName ");
		if (sb.getName() != null) {
			buffer.append("name=" + quotes + sb.getName() + quotes);
		}
		if (sb.getPassword() != null) {
			buffer.append(" password=" + quotes + sb.getPassword() + quotes);
		}

		if (sb.getLocalladdress() != null) {
			buffer.append(" localaddress=" + quotes + sb.getLocalladdress()
					+ quotes);
		}
		if (sb.getExternalipaddress() != null) {
			buffer.append(" externalipaddress=" + quotes
					+ sb.getExternalipaddress() + quotes);
		}
		if (sb.getMac() != null) {
			buffer.append(" mac=" + quotes + sb.getMac() + quotes);
		}

		if (sb.getPstatus() != null) {
			buffer.append(" Pstatus=" + quotes + sb.getPstatus() + quotes);
		}
		if (sb.getPstatus() != null) {
			buffer.append(" signalingport=" + quotes + sb.getSignalingPort()
					+ quotes);
		}

		if (sb.getLatitude() != null) {
			buffer.append(" lat=" + quotes + sb.getLatitude() + quotes);
		}
		if (sb.getLongitude() != null) {
			buffer.append(" long=" + quotes + sb.getLongitude() + quotes);
		}

		if (sb.getDtype() != null) {
			buffer.append(" dtype=" + quotes + sb.getDtype() + quotes);
		}
		if (sb.getDos() != null) {
			buffer.append(" dos=" + quotes + sb.getDos() + quotes);
		}
		if (sb.getAver() != null) {
			buffer.append(" aver=" + quotes + sb.getAver() + quotes);
		}

	

		if (sb.getReleaseVersion() != null) {
			buffer.append(" avdate=" + quotes + sb.getReleaseVersion() + quotes);
		}

		if (sb.getdevcetype() != null) {
			buffer.append(" devicetype=" + quotes + sb.getdevcetype() + quotes);
		}

		buffer.append(" />");
		Log.i("welcome",
				"******************webserviceclient.java buffer.toString()****************"
						+ buffer.toString());

		return getcreateReqResMsg(buffer.toString());
	}



	private String getPermissionId(String permission) {
		if (permission.equalsIgnoreCase("none")) {
			return "F1";
		} else if (permission.equalsIgnoreCase("view")) {
			return "F2";
		} else if (permission.equalsIgnoreCase("modify")) {
			return "F3";
		} else {
			return null;
		}
	}

	public String composeSetAndGetUtilityItemsXML(UtilityBean utilityList) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><FormDetails");
			// if (userId != null) {
			buffer.append(" type=" + quotes + utilityList.getType() + quotes
					+ " userid=" + quotes + utilityList.getOwnerName() + quotes
					+ " buddyname=" + quotes + utilityList.getUsername()
					+ quotes + " utilityname=" + quotes
					+ utilityList.getUtility_name() + quotes);
			// }
			buffer.append("/>");

			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	@SuppressWarnings("finally")
	public String composeSetUtilityServices(Vector<UtilityBean> utilityList) {
		StringBuffer buffer = new StringBuffer();
		try {
			if (utilityList != null && utilityList.size() > 0) {
				buffer.append("<?xml version=\"1.0\"?><com>");
				for (UtilityBean utilityBean : utilityList) {
					buffer.append("<FormDetails");
					if (utilityBean.getUsername() != null)
						buffer.append(" userid=" + quotes
								+ utilityBean.getUsername() + quotes);
					if (utilityBean.getType() != null)
						buffer.append(" type=" + quotes + utilityBean.getType()
								+ quotes);
					buffer.append(" utilityid=" + quotes + utilityBean.getId()
							+ quotes);

					if (utilityBean.getResult() != null)
						buffer.append(" result=" + quotes
								+ utilityBean.getResult() + quotes);

					if (utilityBean.getUtility_name() != null)
						buffer.append(" utilityname=" + quotes
								+ utilityBean.getUtility_name() + quotes);
					if (utilityBean.getProduct_name() != null)
						if (utilityBean.getUtility_name().equalsIgnoreCase(
								"buy")
								|| utilityBean.getUtility_name()
										.equalsIgnoreCase("sell")) {
							buffer.append(" productname=" + quotes
									+ utilityBean.getProduct_name() + quotes);
						} else {
							buffer.append(" servicename=" + quotes
									+ utilityBean.getProduct_name() + quotes);
						}
					if (utilityBean.getPrice() != null)
						buffer.append(" price=" + quotes
								+ utilityBean.getPrice() + quotes);
					if (utilityBean.getQty() != null)
						buffer.append(" quantity=" + quotes
								+ utilityBean.getQty() + quotes);
					if (utilityBean.getNameororg() != null)
						if (utilityBean.getUtility_name().equalsIgnoreCase(
								"buy")
								|| utilityBean.getUtility_name()
										.equalsIgnoreCase("sell")) {
							buffer.append(" orgname=" + quotes
									+ utilityBean.getNameororg() + quotes);
						} else {
							buffer.append(" name=" + quotes
									+ utilityBean.getNameororg() + quotes);
						}
					if (utilityBean.getAddress() != null)
						buffer.append(" address=" + quotes
								+ utilityBean.getAddress() + quotes);
					if (utilityBean.getLocation() != null)
						buffer.append(" location=" + quotes
								+ utilityBean.getLocation() + quotes);
					if (utilityBean.getCityordist() != null)
						buffer.append(" city=" + quotes
								+ utilityBean.getCityordist() + quotes);
					if (utilityBean.getState() != null)
						buffer.append(" state=" + quotes
								+ utilityBean.getState() + quotes);
					if (utilityBean.getCountry() != null)
						buffer.append(" country=" + quotes
								+ utilityBean.getCountry() + quotes);
					if (utilityBean.getPin() != null)
						buffer.append(" pin=" + quotes + utilityBean.getPin()
								+ quotes);
					if (utilityBean.getImag_filename() != null)
						buffer.append(" photo=" + quotes
								+ utilityBean.getImag_filename() + quotes);
					if (utilityBean.getAudiofilename() != null)
						buffer.append(" audio=" + quotes
								+ utilityBean.getAudiofilename() + quotes);
					if (utilityBean.getVideofilename() != null)
						buffer.append(" video=" + quotes
								+ utilityBean.getVideofilename() + quotes);
					if (utilityBean.getC_no() != null)
						buffer.append(" phone=" + quotes
								+ utilityBean.getC_no() + quotes);
					if (utilityBean.getPosted_date() != null)
						buffer.append(" posteddate=" + quotes
								+ utilityBean.getPosted_date() + quotes);
					buffer.append(" mode=" + quotes + utilityBean.getMode()
							+ quotes);
					buffer.append("/>");
				}
				buffer.append("</com>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return buffer.toString();
		}
	}

	public String composeEditFormForNewMode(String[] params,
			String[] field_name, String[] field_type,
			ArrayList<String[]> attributes) {
		String datasource = "";
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><EditForm");
			if (params[0] != null) {
				buffer.append(" userid=" + quotes + params[0] + quotes);
			}
			if (params[1] != null) {
				buffer.append(" formid=" + quotes + params[1] + quotes);
			}
			if (params[2] != null) {
				buffer.append(" mode=" + quotes + params[2] + quotes);
			}
			buffer.append(">");
			for (int i = 0; i < field_name.length; i++) {
				buffer.append("<Column fieldname=" + quotes + field_name[i]
						+ quotes + " fieldtype=" + quotes + field_type[i]
						+ quotes + " >");
				String[] memb = attributes.get(i);

				buffer.append("<Attribute dataentrymode=" + quotes + memb[1]
						+ quotes + " datasource=" + quotes + datasource
						+ quotes + " datavalidation=" + quotes + memb[2]
						+ quotes + " defaultvalue=" + quotes + memb[3] + quotes
						+ " instruction=" + quotes + memb[4] + quotes
						+ " errortip=" + quotes + memb[5] + quotes + " />");
				buffer.append("</Column>");

			}
			buffer.append("</EditForm>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String composeEditFormFieldForUpdateMode(String[] params,
			ArrayList<EditFormBean> eList) {

		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><EditForm");
			if (params[0] != null) {
				buffer.append(" userid=" + quotes + params[0] + quotes);
			}
			if (params[1] != null) {
				buffer.append(" formid=" + quotes + params[1] + quotes);
			}
			if (params[2] != null) {
				buffer.append(" mode=" + quotes + params[2] + quotes);
			}
			buffer.append(">");
			for (EditFormBean eBean : eList) {
				buffer.append("<Column fatid=" + quotes
						+ eBean.getAttributeid() + quotes + " newfieldname="
						+ quotes + eBean.getColumnname() + quotes + " />");

			}
			buffer.append("</EditForm>");
			buffer.append("</com>");
			Log.d("xml", "composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();

	}

	public String composeNewVerificationXML(String[] params) {
		StringBuffer buffer = new StringBuffer();
		try {
			 buffer = new StringBuffer();
			buffer.append("<Newuserverification ");
			buffer.append(" role=" + quotes + params[0] + quotes);
			buffer.append(" firstname=" + quotes + params[1] + quotes);
			buffer.append(" lastname=" + quotes + params[2] + quotes);
			buffer.append(" dob=" + quotes + params[3] + quotes);
			buffer.append(" ssn=" + quotes + params[4] + quotes);
			buffer.append(" state=" + quotes + params[5] + quotes);
			buffer.append(" houseno=" + quotes + params[6] + quotes);
			buffer.append(" zip=" + quotes + params[7] + quotes);

			buffer.append(" />");
			return "<?xml version=\"1.0\"?>" + buffer.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}

	public String composeSetMyAccountXML(ProfileBean pb) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer = new StringBuffer();
			buffer.append("<Setmyaccount");
			buffer.append(" username=" + quotes + pb.getUsername() + quotes);
			buffer.append(" nickname=" + quotes + pb.getNickname() + quotes);
			buffer.append(" photo=" + quotes + pb.getPhoto() + quotes);
			buffer.append(" title=" + quotes + pb.getTitle() + quotes);
			buffer.append(" firstname=" + quotes + pb.getFirstname() + quotes);
			buffer.append(" lastname=" + quotes + pb.getLastname() + quotes);
			buffer.append(" sex=" + quotes + pb.getSex() + quotes);
			buffer.append(" usertype=" + quotes + pb.getUsertype() + quotes);
			buffer.append(" state=" + quotes + pb.getState() + quotes);
			buffer.append(" profession=" + quotes + pb.getProfession() + quotes);
			buffer.append(" speciality=" + quotes + pb.getSpeciality() + quotes);
			buffer.append(" medicalschool=" + quotes + pb.getMedicalschool() + quotes);
			buffer.append(" residencyprogram=" + quotes + pb.getResidencyprogram() + quotes);
			buffer.append(" fellowshipprogram=" + quotes + pb.getFellowshipprogram() + quotes);
			buffer.append(" officeaddress=" + quotes + pb.getOfficeaddress() + quotes);
			buffer.append(" hospitalaffiliation=" + quotes + pb.getHospitalaffiliation() + quotes);
			buffer.append(" citationpublications=" + quotes + pb.getCitationpublications() + quotes);
			buffer.append(" organizationmembership=" + quotes + pb.getOrganizationmembership() + quotes);
			buffer.append(" tos=" + quotes + pb.getTos() + quotes);
			buffer.append(" baa=" + quotes + pb.getBaa() + quotes);

			buffer.append(" />");
			return "<?xml version=\"1.0\"?>" + buffer.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}
	public String subscribeXML(SubscribeBean sb) {
		StringBuffer buffer = new StringBuffer();
		try {
			Log.i("AAAA","XML COMPOSER ____");
		buffer.append("<Registration ");
		buffer.append(" photo=" + quotes + sb.getPhoto() + quotes);
		buffer.append(" name=" + quotes + sb.getEmailId() + quotes);
		buffer.append(" mobileno=" + quotes + sb.getMobileNo() + quotes);
		buffer.append(" nickname=" + quotes + sb.getName() + quotes);
		buffer.append(" title=" + quotes + sb.getTitle() + quotes);
		buffer.append(" firstname=" + quotes + sb.getFname() + quotes);
		buffer.append(" middlename=" + quotes + sb.getMname() + quotes);
		buffer.append(" lastname=" + quotes + sb.getLname() + quotes);
		buffer.append(" usertype=" + quotes + sb.getOccupation() + quotes);
		buffer.append(" officeaddress=" + quotes + sb.getOffcAddr() + quotes);
		buffer.append(" state=" + quotes + sb.getState() + quotes);
		buffer.append(" hospitalaffiliation=" + quotes + sb.getHospitalaff() + quotes);
		buffer.append(" password=" + quotes + sb.getPassword() + quotes);
		buffer.append(" pin=" + quotes + sb.getPin() + quotes);
		buffer.append(" secquestion1=" + quotes + sb.getSecques1() + quotes);
		buffer.append(" secanswer1=" + quotes + sb.getSecans1() + quotes);
		buffer.append(" secquestion2=" + quotes + sb.getSecques2() + quotes);
		buffer.append(" secanswer2=" + quotes +sb.getSecans2() + quotes);
		buffer.append(" secquestion3=" + quotes +sb.getSecques3() + quotes);
		buffer.append(" secanswer3=" + quotes + sb.getSecans3() + quotes);
		buffer.append(" externalip=" + quotes + sb.getExternalip() + quotes);
		buffer.append(" devicetype=" + quotes + sb.getDevicetype() + quotes);
		buffer.append(" dos=" + quotes + sb.getDos() + quotes);
		buffer.append(" aver=" + quotes + sb.getAver() + quotes);
		buffer.append(" reserved=" + quotes + sb.getReserved() + quotes);
		buffer.append(" tos=" + quotes + sb.getTos() + quotes);
		buffer.append(" baa=" + quotes + sb.getBaa() + quotes);
			buffer.append(" dob=" + quotes + sb.getDob() + quotes);
			buffer.append(" ssn=" + quotes + sb.getSsn() + quotes);
			buffer.append(" houseno=" + quotes + sb.getHouseno() + quotes);
			buffer.append(" zipcode=" + quotes + sb.getZipcode() + quotes);
			buffer.append(" profession=" + quotes + sb.getProfession() + quotes);
			buffer.append(" speciality=" + quotes + sb.getSpeciality() + quotes);
			buffer.append(" medicalschool=" + quotes + sb.getMedicalschool() + quotes);
			buffer.append(" residencyprogram=" + quotes + sb.getRes_pgm() + quotes);
			buffer.append(" fellowshipprogram=" + quotes + sb.getFellow_pgm() + quotes);
			buffer.append(" citationpublications=" + quotes + sb.getCitations() + quotes);
			buffer.append(" sex=" + quotes + sb.getSex() + quotes);
			buffer.append(" associationmembership=" + quotes + sb.getAssociation() + quotes);

		buffer.append(" />");

		} catch (Exception e) {
			// TODO: handle exception
		}
		return "<?xml version=\"1.0\"?>" + buffer.toString();
	}
	public String composeUpdateSecretAnswerXML(String[] parm) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("<Update ");

				buffer.append(" username=" + quotes + parm[0] + quotes);
				buffer.append(" secquestion1=" + quotes + parm[1] + quotes);
				buffer.append(" secanswer1=" + quotes + parm[2] + quotes);
				buffer.append(" secquestion2=" + quotes+parm[3]+ quotes);
				buffer.append(" secanswer2=" + quotes + parm[4] + quotes);
				buffer.append(" secquestion3=" + quotes + parm[5]+ quotes);
				buffer.append(" secanswer3=" + quotes + parm[6] + quotes);

			buffer.append(" />");

			Log.d("xml", "" + buffer);
			return "<?xml version=\"1.0\"?>" + buffer.toString();
	}
	public String composeSearchPeopleXML(String[] param) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<Searchaccount ");
			buffer.append(" username=" + quotes + param[0] + quotes);
			buffer.append(" buddyname=" + quotes + param[1] + quotes);
			buffer.append(" firstname=" + quotes + param[2] + quotes);
			buffer.append(" lastname=" + quotes + param[3] + quotes);
			buffer.append(" sex=" + quotes + param[4] + quotes);
			buffer.append(" usertype=" + quotes + param[5] + quotes);
			buffer.append(" state=" + quotes + param[6] + quotes);
			buffer.append(" speciality=" + quotes + param[7] + quotes);
			buffer.append(" medicalschool=" + quotes + param[8] + quotes);
			buffer.append(" residencyprogram=" + quotes + param[9] + quotes);
			buffer.append(" fellowshipprogram=" + quotes + param[10] + quotes);
			buffer.append(" officeaddress=" + quotes + param[11] + quotes);
			buffer.append(" hospitalaffiliation=" + quotes + param[12] + quotes);
			buffer.append(" mobileno=" + quotes +param[13] + quotes);
			buffer.append(" nickname=" + quotes +param[14] + quotes);
			buffer.append(" />");

		} catch (Exception e) {
			// TODO: handle exception
		}
		return "<?xml version=\"1.0\"?>" + buffer.toString();
	}
		public String fileUploadXml(String[] param) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<fileupload ");
		
		buffer.append(" username=" + quotes + param[0] + quotes);
		buffer.append(" password=" + quotes + param[1] + quotes);
		buffer.append(" mimetype=" + quotes + param[2] + quotes);
		buffer.append(" filename=" + quotes + param[3] + quotes);
		buffer.append(" content=" + quotes + param[4] + quotes);
			buffer.append(" branchtype=" + quotes + param[5] + quotes);
			buffer.append(" filesize=" + quotes + param[6] + quotes);

		buffer.append(" />");
		Log.i("FileUpload","FILEUPLOAD>>>>>"+ buffer.toString());
		return "<?xml version=\"1.0\"?>" + buffer.toString();
	}
	public String fileDownloadXml(String[] param) {
		Log.i("STringvalue", "filepath"+param);
		StringBuffer buffer = new StringBuffer();
		buffer.append("<filedownload ");
		
		buffer.append(" userid=" + quotes + param[0] + quotes);
		buffer.append(" password=" + quotes + param[1] + quotes);
		buffer.append(" filename=" + quotes + param[2] + quotes);

		buffer.append(" />");
		return "<?xml version=\"1.0\"?>" + buffer.toString();
	}
	public String composePatientRecordXML(PatientDetailsBean pBean) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<setpatientrecord ");

		buffer.append(" groupid=" + quotes + pBean.getGroupid() + quotes);
		buffer.append(" patientid=" + quotes + pBean.getPatientid() + quotes);
		buffer.append(" creatorName=" + quotes + pBean.getCreatorname() + quotes);
		buffer.append(" firstname=" + quotes + pBean.getFirstname() + quotes);
		buffer.append(" middlename=" + quotes + pBean.getMiddlename() + quotes);
		buffer.append(" lastname=" + quotes + pBean.getLastname() + quotes);
		buffer.append(" dob=" + quotes + pBean.getDob() + quotes);
		buffer.append(" sex=" + quotes + pBean.getSex() + quotes);
		buffer.append(" hospital=" + quotes + pBean.getHospital() + quotes);
		buffer.append(" mrn=" + quotes + pBean.getMrn() + quotes);
		buffer.append(" location=" + quotes + "" + quotes);
		buffer.append(" floor=" + quotes + pBean.getFloor() + quotes);
		buffer.append(" ward=" + quotes + pBean.getWard() + quotes);
		buffer.append(" room=" + quotes + pBean.getRoom() + quotes);
		buffer.append(" bed=" + quotes + pBean.getBed() + quotes);
		buffer.append(" admissionDate=" + quotes + pBean.getAdmissiondate() + quotes);
		buffer.append(" assignedMembers=" + quotes + pBean.getAssignedmembers() + quotes);

		buffer.append(" />");
		return "<?xml version=\"1.0\"?>" + buffer.toString();
	}
	public String composeCreateTaskXML(TaskDetailsBean tBean) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<settaskrecord ");

		buffer.append(" groupid=" + quotes + tBean.getGroupid() + quotes);
		buffer.append(" taskid=" + quotes + tBean.getTaskId() + quotes);
		buffer.append(" creatorname=" + quotes + tBean.getCreatorName() + quotes);
		buffer.append(" taskdesc=" + quotes + tBean.getTaskdesc() + quotes);
		buffer.append(" callpatient=" + quotes + tBean.getPatientname() + quotes);
		if(tBean.getAssignedMembers()!=null)
		buffer.append(" assignmembers=" + quotes + tBean.getAssignedMembers() + quotes);
		buffer.append(" patientid=" + quotes + tBean.getPatientid() + quotes);
		buffer.append(" duedate=" + quotes + tBean.getDuedate() + quotes);
		buffer.append(" duetime=" + quotes + tBean.getDuetime() + quotes);
		buffer.append(" setreminder=" + quotes + tBean.getSetreminder() + quotes);
		buffer.append(" timetoremind=" + quotes + tBean.getTimetoremind() + quotes);
		buffer.append(" taskstatus=" + quotes + tBean.getTaskstatus() + quotes);

		buffer.append(" />");
		return "<?xml version=\"1.0\"?>" + buffer.toString();
	}
	public String composeSetorEditRoleAccessXML(RolePatientManagementBean pBean,RoleEditRndFormBean eBean,RoleTaskMgtBean tBean,RoleCommentsViewBean cBean) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<Setrole  ");

		buffer.append(" groupid=" + quotes + pBean.getGroupid() + quotes);
		buffer.append(" groupowner=" + quotes + pBean.getGroupowner() + quotes);
		buffer.append(" groupmember=" + quotes + pBean.getGroupmember() + quotes);
		buffer.append(" roleid=" + quotes + pBean.getRoleid()+ quotes);
		buffer.append(" role=" + quotes + pBean.getRole() + quotes);

		buffer.append(" add=" + quotes + pBean.getAdd() + quotes);
		buffer.append(" modify=" + quotes + pBean.getModify() + quotes);
		buffer.append(" delete=" + quotes + pBean.getDelete() + quotes);
		buffer.append(" discharge=" + quotes + pBean.getDischarge() + quotes);
		buffer.append(" status=" + quotes + eBean.getStatus() + quotes);
		buffer.append(" diagnosis=" + quotes + eBean.getDiagnosis() + quotes);
		buffer.append(" testandvitals=" + quotes + eBean.getTestandvitals() + quotes);
		buffer.append(" hospitalcourse=" + quotes + eBean.getHospitalcourse() + quotes);
		buffer.append(" notes=" + quotes + eBean.getNotes() + quotes);
		buffer.append(" consults=" + quotes + eBean.getConsults() + quotes);
		buffer.append(" tattending=" + quotes + tBean.getTattending() + quotes);
		buffer.append(" tfellow=" + quotes + tBean.getTfellow() + quotes);
		buffer.append(" tchiefresident=" + quotes + tBean.getTchiefresident() + quotes);
		buffer.append(" tresident=" + quotes + tBean.getTresident() + quotes);
		buffer.append(" tmedstudent=" + quotes + tBean.getTmedstudent() + quotes);
		buffer.append(" cattending=" + quotes + cBean.getCattending() + quotes);
		buffer.append(" cfellow=" + quotes + cBean.getCfellow() + quotes);
		buffer.append(" cchiefresident=" + quotes + cBean.getCchiefresident() + quotes);
		buffer.append(" cresident=" + quotes + cBean.getCresident() + quotes);
		buffer.append(" cmedstudent=" + quotes + cBean.getCmedstudent() + quotes);
		buffer.append(" />");
		return "<?xml version=\"1.0\"?>" + buffer.toString();
	}
	public String composeSetPatientCommentsXML(PatientCommentsBean cBean) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<setpatientcomments ");

		buffer.append(" groupid=" + quotes + cBean.getGroupid() + quotes);
		buffer.append(" commentid=" + quotes + cBean.getCommentid() + quotes);
		buffer.append(" patientid=" + quotes + cBean.getPatientid() + quotes);
		buffer.append(" groupowner=" + quotes + cBean.getGroupowner() + quotes);
		buffer.append(" groupmember=" + quotes + cBean.getGroupmember() + quotes);
		buffer.append(" comments=" + quotes + cBean.getComments() + quotes);
		buffer.append(" dateandtime=" + quotes + cBean.getDateandtime() + quotes);
		buffer.append(" />");
		return "<?xml version=\"1.0\"?>" + buffer.toString();
	}
	public String composeSetPatientDescriptionXML(PatientDescriptionBean bean) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<setpatientdesc ");

		buffer.append(" patientid=" + quotes + bean.getPatientid() + quotes);
		buffer.append(" groupid=" + quotes + bean.getGroupid() + quotes);
		buffer.append(" reportid=" + quotes + bean.getReportid() + quotes);
		buffer.append(" reportcreator=" + quotes + bean.getReportcreator() + quotes);
		buffer.append(" currentstatus=" + quotes + bean.getCurrentstatus() + quotes);
		buffer.append(" diagnosis=" + quotes + bean.getDiagnosis() + quotes);
		buffer.append(" medications=" + quotes + bean.getMedications() + quotes);
		buffer.append(" testandvitals=" + quotes + bean.getTestandvitals() + quotes);
		buffer.append(" hospitalcourse=" + quotes + bean.getHospitalcourse() + quotes);
		buffer.append(" consults=" + quotes + bean.getConsults() + quotes);
		buffer.append(" />");
		return "<?xml version=\"1.0\"?>" + buffer.toString();
	}

	public String composeFeedBackXML(String feedback) {
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append("<?xml version=\"1.0\"?>" + "<com><setfeedback");
			buffer.append(" userid=" + quotes + CallDispatcher.LoginUser + quotes
					+ " feedbackmode=" + quotes + 0 + quotes
					+ " feedback=" + quotes + feedback + quotes);
						buffer.append("/>");
			buffer.append("</com>");
			Log.d("feedback", "feedback composed xml" + buffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer.toString();
	}
	public String composeUpdateChatTemplateXML(String[] param) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<UpdateChatTemplate  ");

		buffer.append(" userid=" + quotes + param[0] + quotes);
		buffer.append(" requestid=" + quotes + param[1] + quotes);
		buffer.append(" editmode="+ quotes+ param[2] + quotes);
		buffer.append(" type=" + quotes + param[3] + quotes);
		buffer.append(" templateid=" + quotes + param[4] + quotes);
		buffer.append(" template=" + quotes + param[5] + quotes);
		buffer.append(" deletetemplates=" + quotes + param[6] + quotes);
		buffer.append(" />");
		return "<?xml version=\"1.0\"?>" + buffer.toString();
	}
}
