package org.lib.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.lib.PatientDetailsBean;
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
import org.lib.model.SubscribeBean;
import org.lib.model.TaskDetailsBean;
import org.lib.model.UtilityBean;
import org.lib.xml.XmlComposer;

import android.content.Context;
import android.util.Log;

import com.bean.DefaultPermission;
import com.bean.EditFormBean;
import com.bean.GroupChatPermissionBean;
import com.bean.ProfileBean;
import com.bean.SurveyApplicantFormBean;
import com.cg.commonclass.CallDispatcher;
import com.util.SingleInstance;

/**
 * This class is used for adding the Webservice Request message to the Queue.
 * Then the request messages in the Queue are processed
 * by(WebServiceClientProcess) aThread. Request Messages like SIGNIN
 * Request,Subscribe Request etc...
 * 
 * 
 */
public class WebServiceClient {

	/**
	 * Set the URL of the Server as the input String.
	 */
	private String mWsdlURL = null;
	/**
	 * NameSpace of the Server.
	 */
	private String mNamespace = null;

	/**
	 * used to compose the XML.
	 */
	private XmlComposer xmlComposer = new XmlComposer();

	private WSNotifier wsNotifier = null;

	/**
	 * This interface is used to notify the WebService Response message like
	 * SIGNIN,SUBSCRIBE etc. This is also used to Notify Error(failure).
	 */
	private WebServiceCallback webServiceCallback = null;

	public WebServiceCallback getWebServiceCallback() {
		return webServiceCallback;
	}

	/**
	 * Register the implemented class which is used to notify Results.
	 * 
	 * @param webServiceCallback
	 *            implemented class
	 */
	public void setWebServiceCallback(WebServiceCallback webServiceCallback) {
		this.webServiceCallback = webServiceCallback;
		if (wsNotifier != null) {
			wsNotifier.setSrviceCallback(webServiceCallback);
		}
	}

	/**
	 * Get the URL which the Server is running.
	 * 
	 * @return The URL of Server
	 */
	public String getmWsdlURL() {
		return mWsdlURL;
	}

	/**
	 * Set the URL of the Server as the input String.
	 * 
	 * @param mWsdlURL
	 *            The URL of Server.
	 */
	public void setmWsdlURL(String mWsdlURL) {
		this.mWsdlURL = mWsdlURL;
	}

	/**
	 * Get the NameSpace of the server.
	 * 
	 * @return The NameSpace.
	 */
	public String getmNamespace() {
		return mNamespace;
	}

	/**
	 * Set the NameSpace of the Server as the input String.
	 * 
	 * @param mNamespace
	 *            The NameSpace of Server
	 */
	public void setmNamespace(String mNamespace) {
		this.mNamespace = mNamespace;
	}

	public void addToWSQueue(Servicebean servicebean) {
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
		else
			Log.d("WSRUNNER", "wsNotifier is null");
	}

	/**
	 * Used to add the SignOut Request to the Queue.
	 * 
	 * @param username
	 *            User name
	 */
	public void siginout(String username) {

		Servicebean servicebean = new Servicebean();
		servicebean.setWsmethodname("Signout");
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("uname", username);
		property_map.put("key", "0");
		servicebean.setProperty_map(property_map);
		servicebean.setServiceMethods(EnumWebServiceMethods.SIGNOUT);
		if (wsNotifier != null) {
			wsNotifier.clearBGTask();
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}

	public void getChatTemplate(String userid,String datetime) {

		Log.i("chattemplate","webserviceClient getchatTemplate date time--->"+datetime);
		Servicebean servicebean = new Servicebean();
		servicebean.setWsmethodname("GetChatTemplates");
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("userid", userid);
		property_map.put("date", datetime);
		servicebean.setProperty_map(property_map);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETCHATTEMPLATE);
		if (wsNotifier != null) {
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}


	/**
	 * used to add the Accept or Reject message to the Queue.
	 * 
	 * @param uname
	 *            user name.
	 * @param bname
	 *            Buddy name(Requested Buddy).
	 * @param value
	 *            Accept or Reject status.
	 */
	public void acceptRejectPeople(String uname, String bname, String value,String report,
			Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("uname", uname);
		property_map.put("bname", bname);
		property_map.put("response", value);
		property_map.put("report", report);
		property_map.put("key", "0");
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("AcceptReject");
		servicebean.setObj(bname);
		servicebean.setCallBack(callBack);
		if (value.equals("0")) {
			servicebean.setServiceMethods(EnumWebServiceMethods.REJECT);
		} else if (value.equals("1")) {
			servicebean.setServiceMethods(EnumWebServiceMethods.ACCEPT);
		}
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	/**
	 * This method is used to add the find people message to the Queue. This
	 * message is further Processed by WebServiceClientProcess Thread to call
	 * WebService.
	 * 
	 * @param findPeople
	 *            Buddy name for Search
	 */
	public void findPeople(String findPeople, Object callBack) {
		Servicebean servicebean = new Servicebean();
		// servicebean.setObj(findPeople);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("searchStr", findPeople);
		property_map.put("key", "0");
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("FindPeople");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.FINDPEOPLE);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	/**
	 * This method is used to add the KeepAlive message to the Queue. This
	 * message is further Processed by WebServiceClientProcess Thread to call
	 * WebService. The keepalive bean holding my information like private
	 * IpAddress,Public IpAddress etc...
	 * 
	 * @param aliveBean
	 *            bean holding my own information
	 */
	public void heartBeat(KeepAliveBean aliveBean) {
		String keepalivexml = xmlComposer.composeKeepAliveXml(aliveBean);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("keepAliveXML", keepalivexml);
		property_map.put("key", aliveBean.getKey());
		servicebean.setProperty_map(property_map);
		servicebean.setKey(aliveBean.getKey());
		servicebean.setWsmethodname("HeartBeat");
		servicebean.setServiceMethods(EnumWebServiceMethods.HEARTBEAT);
		if (wsNotifier != null) {
			SingleInstance.mainContext.setLogWriterForCall("SEND HeartBeat 1: Notifier=available");
			SingleInstance.printLog(null, "SEND HeartBeat 1: Notifier=available", "INFO", null);
			wsNotifier.addTasktoExecutor(servicebean);
		} else
			SingleInstance.mainContext.setLogWriterForCall("SEND HeartBeat 1: Notifier=null");
		SingleInstance.printLog(null, "SEND HeartBeat 1: Notifier=null" + ",Key=1", "INFO", null);


	}

	/**
	 * This method is used to add the deletePeople Request message to the Queue.
	 * This message is further Processed by WebServiceClientProcess Thread to
	 * call WebService.
	 * 
	 * @param username
	 *            User name
	 * @param peopleName
	 *            Buddy name to delete from my BuddyList.
	 */
	public void deletePeople(String username, String peopleName, Object object) {

		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("uname", username);
		property_map.put("bname", peopleName);
		property_map.put("key", "0");
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("DeletePeople");
		servicebean.setCallBack(object);
		servicebean.setServiceMethods(EnumWebServiceMethods.DELETEPEOPLE);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	/**
	 * This method is used to add the addPeople Request message to the Queue.
	 * This message is further Processed by WebServiceClientProcess Thread to
	 * call WebService.
	 * 
	 * @param username
	 *            User name
	 * @param peopleName
	 *            Buddy name for Buddy request.
	 */

	public void addPeople(String username, String peopleName, Object obj) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("uname", username);
		property_map.put("bname", peopleName);
		property_map.put("key", "0");
		servicebean.setProperty_map(property_map);
		servicebean.setCallBack(obj);
		servicebean.setWsmethodname("AddPeople");
		servicebean.setServiceMethods(EnumWebServiceMethods.ADDPEOPLE);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	/**
	 * This method is used to add the SignIn Request message to the Queue. This
	 * message is further Processed by WebServiceClientProcess Thread to call
	 * WebService.
	 * 
	 * @param sb
	 *            bean holding the information to signIn like UserName,Password.
	 */
	public void sigin(SiginBean sb) {
		String siginxml = xmlComposer.composeSiginXml(sb);
		Servicebean servicebean = new Servicebean();
		servicebean.setCallBack(sb.getCallBack());
		servicebean.setWsmethodname("Signin");
		HashMap<String, String> ws_argmap = new HashMap<String, String>();
		ws_argmap.put("loginxml", siginxml);
		ws_argmap.put("key", "0");
		servicebean.setProperty_map(ws_argmap);
		servicebean.setServiceMethods(EnumWebServiceMethods.SIGNIN);
		if (wsNotifier != null) {
			wsNotifier.clearBGTask();
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}

	// public void getProfileTemplate(String modifiedDate) {
	// Servicebean servicebean = new Servicebean();
	// HashMap<String, String> property_map = new HashMap<String, String>();
	// if (modifiedDate != null || modifiedDate != "")
	// property_map.put("profileTemplateModifiedTime", modifiedDate);
	// else
	// property_map.put("profileTemplateModifiedTime", "");
	//
	// servicebean.setWsmethodname("GetStandardProfileTemplate");
	// servicebean.setProperty_map(property_map);
	// servicebean.setServiceMethods(EnumWebServiceMethods.GETPROFILETEMPLATE);
	// if (wsNotifier != null)
	// wsNotifier.addTasktoExecutor(servicebean);
	// }

	public void getProfessions(String user, String password, Object obj) {
		String getProfxml = xmlComposer.composeGetProfessionsXml(user, password);
		Servicebean servicebean = new Servicebean();
		servicebean.setWsmethodname("GetProfessions");
		HashMap<String, String> ws_argmap = new HashMap<String, String>();
		ws_argmap.put("reqxml", getProfxml);
		servicebean.setProperty_map(ws_argmap);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETPROFESSIONS);
		servicebean.setCallBack(obj);
		if (wsNotifier != null) {
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}
	public void setProfessionPermission(String user, String password, String value,Object obj) {
		String setProfxml = xmlComposer.composesetProfessionPermissionXml(user, password, value);
		Servicebean servicebean = new Servicebean();
		servicebean.setWsmethodname("SetProfessionPermission");
		HashMap<String, String> ws_argmap = new HashMap<String, String>();
		ws_argmap.put("reqxml", setProfxml);
		servicebean.setProperty_map(ws_argmap);
		servicebean.setServiceMethods(EnumWebServiceMethods.SETPROFESSIONPERMISSION);
		servicebean.setCallBack(obj);
		if (wsNotifier != null) {
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}
	public void setStandardProfileFieldValues(String[] params,
			Vector<FieldTemplateBean> fields) {
		Servicebean servicebean = new Servicebean();
		String setStdProfileFieldValuesXml = xmlComposer
				.composeSetStandardProfileFieldValuesXML(params, fields);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("profileXml", setStdProfileFieldValuesXml);
		servicebean.setWsmethodname("SetStandardProfileFieldValues");
		servicebean.setProperty_map(property_map);
		servicebean
				.setServiceMethods(EnumWebServiceMethods.SETSTANDARDPROFILEFIELDVALUES);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void updateProfileFieldValueForMultimedia(String[] params,
			FieldTemplateBean fields) {
		Servicebean servicebean = new Servicebean();
		String setStdProfileFieldValuesXml = xmlComposer
				.composeProfileFieldValuesXMLForMultimedia(params, fields);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("profileXml", setStdProfileFieldValuesXml);
		servicebean.setWsmethodname("SetStandardProfileFieldValues");
		servicebean.setProperty_map(property_map);
		servicebean
				.setServiceMethods(EnumWebServiceMethods.SETSTANDARDPROFILEFIELDVALUES);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void getStandardProfilefieldvalues(String[] params) {
		Servicebean servicebean = new Servicebean();
		String getStdProfileFieldValuesXml = xmlComposer
				.composeGetStandardProfileFieldValuesXML(params);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("profileXml", getStdProfileFieldValuesXml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetStandardProfileFieldValues");
		servicebean
				.setServiceMethods(EnumWebServiceMethods.GETPROFILEFIELDVALUES);
		servicebean.setCallBack(SingleInstance.mainContext);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void DeleteAllShares(String user_id, String buddy_id) {

		Servicebean servicebean = new Servicebean();
		String frnXML = xmlComposer.composeWithDrawnPermissionXML(user_id,
				buddy_id);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("deleteXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("DeleteAllShares");
		servicebean.setServiceMethods(EnumWebServiceMethods.DELETEALLSHARES);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void changePassword(String[] param, Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("userName", param[0]);
		property_map.put("oldPassword", param[1]);
		property_map.put("newPassword", param[2]);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("ChangePassword");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.CHANGEPASSWORD);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

    public void ResetPassword(String[] param, Object callBack) {
        Servicebean servicebean = new Servicebean();
        HashMap<String, String> property_map = new HashMap<String, String>();
        property_map.put("userName", param[0]);
        property_map.put("newPassword", param[1]);
        servicebean.setProperty_map(property_map);
        servicebean.setWsmethodname("ResetPassword");
        servicebean.setCallBack(callBack);
        servicebean.setServiceMethods(EnumWebServiceMethods.RESETPASSWORD);
        if (wsNotifier != null)
            wsNotifier.addTasktoExecutor(servicebean);

    }
	public void ResetPin(String[] param, Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("userName", param[0]);
		property_map.put("oldPin", param[1]);
		property_map.put("newPin", param[2]);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("ResetPin");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.RESETPIN);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	public void DeleteProfile(String userid, String profileid) {

		Servicebean servicebean = new Servicebean();
		String frnXML = xmlComposer.ComposeDeleteProfileXML(userid, profileid);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("ProfileXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("DeleteProfile");
		servicebean.setServiceMethods(EnumWebServiceMethods.DELETEPROFILE);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void SharebyProfile(String dadas, ArrayList<String[]> details) {

		Servicebean servicebean = new Servicebean();
		String frnXML = xmlComposer.ComposeCreateShareXML(dadas, details);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("searchxml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SearchProfileByFilter");
		servicebean.setServiceMethods(EnumWebServiceMethods.SHAREBYPROFILE);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void ShareFiles(ShareReminder shareReminder) {
		Servicebean servicebean = new Servicebean();
		String fileSharing = xmlComposer.composeShareReminderXml(shareReminder);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("sharereminderXml", fileSharing);
		servicebean.setWsmethodname("ShareReminder");
		servicebean.setProperty_map(property_map);
		servicebean.setCallBack(shareReminder);
		servicebean.setServiceMethods(EnumWebServiceMethods.SHARE_REMAINDER);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	
	public void FileUpload(String[] parm,Object obj) {
        Log.i("FileUpload", "Inside FileUpload WsClient--->" );

        Servicebean servicebean = new Servicebean();
		String fuploadxml = xmlComposer.fileUploadXml(parm);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("uploadxml", fuploadxml);
		servicebean.setWsmethodname("FileUpload");
		servicebean.setProperty_map(property_map);
		servicebean.setCallBack(obj);
		servicebean.setServiceMethods(EnumWebServiceMethods.UPLOAD);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void FileDownload(String[] parm) {
		Servicebean servicebean = new Servicebean();
		String fdloadxml = xmlComposer.fileDownloadXml(parm);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("downloadxml", fdloadxml);
		servicebean.setWsmethodname("FileDownload");
		servicebean.setProperty_map(property_map);
		servicebean.setServiceMethods(EnumWebServiceMethods.DOWNLOAD);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void forgetPassword(String[] parm) {
		// TODO Auto-generated method stub
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("username", parm[0]);
		property_map.put("emailid", parm[1]);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("ForgetPassword");
		servicebean.setServiceMethods(EnumWebServiceMethods.FORGOTPASSWORD);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	public void getSecurityQuestions(Object obj,String key,String date) {
		// TODO Auto-generated method stub
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("key", "");
		property_map.put("date", date);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("Getsecurityquestions");
        servicebean.setCallBack(obj);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETSECURITYQUESTIONS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

    public void forgetPasswordSnazMed(String[] parm,Object obj) {
        Servicebean servicebean = new Servicebean();
        String reqXml = xmlComposer.forgotpasswordSnazMed(parm);
        HashMap<String, String> property_map = new HashMap<String, String>();
        property_map.put("reqXml", reqXml);
        servicebean.setWsmethodname("ForgotPassword");
        servicebean.setProperty_map(property_map);
        servicebean.setServiceMethods(EnumWebServiceMethods.FORGOTPASSWORDNEW);
		servicebean.setCallBack(obj);
        if (wsNotifier != null)
            wsNotifier.addTasktoExecutor(servicebean);

	}

	public void SecretAnswer(String[] parm) {
		// TODO Auto-generated method stub
		String saxml = xmlComposer.secretAnswerXml(parm);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("secretXml", saxml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SecretAnswer");
		servicebean.setServiceMethods(EnumWebServiceMethods.SECRETANSWER);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void getsetUtility(UtilityBean bean, Object obj) {
		String getsetutiltiyxml = xmlComposer
				.composeSetandGetutilityItems(bean);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("utilityXml", getsetutiltiyxml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetAndGetUtilityItems");
		servicebean.setCallBack(obj);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETSETUTILITY);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void setUtilityItems(UtilityBean bean, Object obj) {
		String getsetutiltiyxml = xmlComposer
				.composeSetAndGetUtilityItemsXML(bean);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("utilityXml", getsetutiltiyxml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetAndGetUtilityItems");
		servicebean.setCallBack(obj);
		servicebean
				.setServiceMethods(EnumWebServiceMethods.SETANDGETUTILITYITEMS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void blockUnblock(String[] param) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("userId", param[0]);
		property_map.put("buddyId", param[1]);
		property_map.put("key", param[2]);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("BlockUnBlockBuddy");
		servicebean.setServiceMethods(EnumWebServiceMethods.BLOCKUNBLOCKBUDDY);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void setUtilityServices(Vector<UtilityBean> utilityList, Object obj) {
		String getsetutiltiyxml = xmlComposer
				.composeSetUtilityServices(utilityList);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("utilityXml", getsetutiltiyxml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetUtilityServices");
		servicebean.setCallBack(obj);
		servicebean.setServiceMethods(EnumWebServiceMethods.SETUTILITYSERVICES);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void Getcontent(String dadas, String fields, String filterdate,
			Object context) {

		if (filterdate == null) {
			filterdate = "";
		}
		String frnXML = xmlComposer.ComposegetFormDataXML(dadas, fields,
				filterdate);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("formId", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetFormContent");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETFORMRECORDS);
		servicebean.setCallBack(context);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void CreateForm(String[] dadas, String[] fields, String[] types) {

		String frnXML = xmlComposer.ComposeCreateFormXML(dadas, fields, types);

		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("formXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("CreateForm");
		servicebean.setServiceMethods(EnumWebServiceMethods.CREATEFORM);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void CreateFormAttribute(String[] dadas, String[] fields,
			String[] types, ArrayList<String[]> details, Context context) {
		String frnXML = xmlComposer.ComposeCreateFormAttributeXML(dadas,
				fields, types, details);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("formXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("CreateForm");
		servicebean.setServiceMethods(EnumWebServiceMethods.CREATEFORM);
		servicebean.setCallBack(context);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void SaveAccessForm(String userid, String formid, String buddy,
			String permissionid, String syncid, String syncquery, String fsid,
			String type, Context context, String[] formDetails) {

		Servicebean servicebean = new Servicebean();
		String frnXML = xmlComposer.ComposeaccessFormXML(userid, formid, buddy,
				permissionid, syncid, syncquery, fsid, type);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("formsettingsXml", frnXML);
		servicebean.setExtraDatas("no");
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetFormSettings");
		servicebean.setServiceMethods(EnumWebServiceMethods.ACCESSFORM);
		servicebean.setOption(formDetails);
		servicebean.setCallBack(context);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void SaveAccessFormForMultipleEntry(Context context,
			ArrayList<String[]> formList, String[] formDetails,
			String addNewRecords) {

		Servicebean servicebean = new Servicebean();
		String frnXML = xmlComposer.composeMultipleAccessShareXML(formList);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("formsettingsXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setExtraDatas(addNewRecords);
		servicebean.setWsmethodname("SetFormSettings");
		servicebean.setServiceMethods(EnumWebServiceMethods.ACCESSMUTIPLEFORM);
		servicebean.setCallBack(context);
		servicebean.setOption(formDetails);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void Getformsettings(String fsid, String username, Context context) {

		String frnXML = xmlComposer.ComposegetFormSettingsXML(fsid, username);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("formsettingsXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetFormSettings");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETFORMSETTINGS);
		servicebean.setCallBack(context);

		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	public void addFormRecords(String username, String id, String[] fields,
			String[] values, Context context) {
		String frnXML = xmlComposer.ComposeAddFormDataXML(username, id, fields,
				values);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("formdataXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("AddFormData");
		servicebean.setServiceMethods(EnumWebServiceMethods.ADDFORMRECORDS);
		servicebean.setCallBack(context);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	public void updateFormRecords(String formid, String user, String tableid,
			String[] fields, String[] values, Context context) {

		String frnXML = xmlComposer.ComposeUpdateFormDataXML(formid, user,
				tableid, fields, values);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("updateXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("UpdateForm");
		servicebean.setServiceMethods(EnumWebServiceMethods.UPDATEFORMRECORDS);
		servicebean.setCallBack(context);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	public void deleteForm(String[] params, Object context) {

		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("userid", params[0]);
		property_map.put("formid", params[1]);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("DeleteForm");
		servicebean.setServiceMethods(EnumWebServiceMethods.DELETEFORM);
		servicebean.setCallBack(context);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	public void getFormTemplate(String[] params, Context context) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("userId", params[0]);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetFormsTemplate");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETFORMSTEMPLATE);
		servicebean.setCallBack(context);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void deleteFormRecords(String[] params, Context context) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("userid", params[0]);
		property_map.put("formid", params[1]);
		property_map.put("tableid", params[2]);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("DeleteRowFromForm");
		servicebean.setServiceMethods(EnumWebServiceMethods.DELETEFORMRECORD);
		servicebean.setCallBack(context);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void DeleteAccessForm(String userid, String formid, String buddy,
			String permissionid, String syncid, String syncquery, String fsid,
			String type, Context context) {
		String frnXML = xmlComposer.ComposeaccessFormXML(userid, formid, buddy,
				permissionid, syncid, syncquery, fsid, type);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("formsettingsXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetFormSettings");
		servicebean.setServiceMethods(EnumWebServiceMethods.DELETEACCESSFORM);
		servicebean.setCallBack(context);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	public void GetAttributecontent(String form_owner, String formId,
			Context context, String isEditFormField) {
		Servicebean servicebean = new Servicebean();
		String frnXML = xmlComposer.ComposegetFormAttributeDataXML(form_owner,
				formId);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("attributeXml", frnXML);
		property_map.put("isEditFormField", isEditFormField);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetFormAttributes");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETFORMATTRIBUTE);
		servicebean.setCallBack(context);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	public void offlineconfiguration(String user_id,
			ArrayList<OfflineRequestConfigBean> config_list,
			ArrayList<OfflineRequestConfigBean> buddyconfig_list, Object obj) {

		Servicebean servicebean = new Servicebean();
		String frnXML = xmlComposer.composeForCallConfiurationInfo(user_id,
				config_list, buddyconfig_list);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("configurationXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("offlineconfiguration");
		servicebean.setCallBack(obj);
		servicebean.setObj(frnXML);
		servicebean.setServiceMethods(EnumWebServiceMethods.OFFLINECONFIGURATION);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}


	public void OfflineCallResponse(String[] details) {
		Log.d("Avataar", " --->OfflineCallResponse in API<---" + details[0]
				+ "------->" + details[1]);
		if (!details[0].equalsIgnoreCase(details[1])) {
			Servicebean servicebean = new Servicebean();
			String xml = xmlComposer.composeOfflineCallResponsexml(details);
			HashMap<String, String> property_map = new HashMap<String, String>();
			property_map.put("responseXml", xml);
			servicebean.setProperty_map(property_map);
			servicebean.setWsmethodname("OfflineCallResponse");
			servicebean.setObj(xml);
			servicebean.setServiceMethods(EnumWebServiceMethods.OFFLINECALLRESPONSE);
			if (wsNotifier != null)
				wsNotifier.addTasktoExecutor(servicebean);
		}
	}

	public void ResponseForCallConfiguration(OfflineRequestConfigBean bean) {
		Servicebean servicebean = new Servicebean();
		String frnXML = xmlComposer
				.composeResponseForCallConfigurationxml(bean);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("configurationXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("ResponseForCallConfiguration");
		servicebean.setObj(frnXML);
		servicebean.setServiceMethods(EnumWebServiceMethods.RESPONSEFORCALLCONFIGURATION);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void GetConfigurationResponDetails(String[] params) {
		Servicebean servicebean = new Servicebean();
		String frnXML = xmlComposer
				.composeGetConfigurationResponDetails(params);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("configurationXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetConfigurationResponDetails");
		servicebean.setObj(frnXML);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETCONFIGURATIONRESPONSEDETAILS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void DeleteMyResponseDetail(String[] params) {
		Servicebean servicebean = new Servicebean();
		String frnXML = xmlComposer.composeDeleteMyResponseDetail(params);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("responseXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("DeleteMyResponseDetail");
		servicebean.setObj(frnXML);
		servicebean.setServiceMethods(EnumWebServiceMethods.DELETEMYRESPONSEDETAILS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void setPermission(PermissionBean bean) {
		Servicebean servicebean = new Servicebean();
		String permissionxml = xmlComposer.composeSetPermissionXML(bean);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("permissionXml", permissionxml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetPermission");
		servicebean.setServiceMethods(EnumWebServiceMethods.SETPERMISSION);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	public void getAllPermission(String[] values) {
		Servicebean servicebean = new Servicebean();
		String getallpermissionxml = xmlComposer
				.composeGetAllPermissionXML(values);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("permissionXml", getallpermissionxml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetAllPermission");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETALLPERMISSION);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void saveMyLocation(String[] location) {
		String locationXml = xmlComposer.composeLocationXml(location);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("locationXml", locationXml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SaveMyLocation");
		servicebean.setObj(locationXml);
		servicebean.setServiceMethods(EnumWebServiceMethods.SAVEMYLOCATION);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	/*
	 * Group Management Services
	 */

	public void createGroup(GroupBean bean, Object callBack) {
		Servicebean servicebean = new Servicebean();
		String CreateGroup = xmlComposer.composeCreateGroup(bean);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("groupXml", CreateGroup.trim());
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("CreateorModifygroup");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.CREATEGROUP);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void deleteGroup(String ownerName, String groupId) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("ownerName", ownerName);
		property_map.put("groupId", groupId);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("DeleteGroup");
		servicebean.setServiceMethods(EnumWebServiceMethods.DELETEGROUP);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void getGroupAndMembers(String ownerName, String groupModifiedDate,
			Object obj) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("ownerName", ownerName);
		property_map.put("groupModifiedDate", groupModifiedDate);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetGroupAndMembers");
		servicebean.setCallBack(obj);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETGROUPANDMEMBERS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void deleteGroupMembers(GroupBean bean) {
		Servicebean servicebean = new Servicebean();
		String groupXml = xmlComposer.composeGroupMembersDelete(bean);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("groupXml", groupXml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GroupMembersDelete");
		servicebean.setServiceMethods(EnumWebServiceMethods.GROUPMEMBERSDELETE);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	public void getParticipateGroups(String userId, String groupModifiedDate,
			Object obj) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("userId", userId);
		property_map.put("groupModifiedDate", groupModifiedDate);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetParticipateGroups");
		servicebean.setCallBack(obj);
		servicebean
				.setServiceMethods(EnumWebServiceMethods.GETPARTICIPATEGROUPS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void leaveGroup(String groupId, String groupMemberName) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("groupId", groupId);
		property_map.put("groupMemberName", groupMemberName);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("LeaveGroup");
		servicebean.setServiceMethods(EnumWebServiceMethods.LEAVEGROUP);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void modifyGroup(GroupBean gBean) {
		Servicebean servicebean = new Servicebean();
		String groupXml = xmlComposer.composeGroupModified(gBean);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("groupXml", groupXml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("ModifyGroup");
		servicebean.setCallBack(gBean.getCallback());
		servicebean.setServiceMethods(EnumWebServiceMethods.MODIFYGROUP);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void setGroupChatSettings(GroupBean gBean,
			Vector<GroupChatPermissionBean> gcpList) {

		Servicebean servicebean = new Servicebean();
		String groupXml = xmlComposer.composeGroupChatSettings(gBean, gcpList);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("groupXml", groupXml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetGroupSettings");
		servicebean.setCallBack(gBean.getCallback());
		servicebean
				.setServiceMethods(EnumWebServiceMethods.SETGROUPCHATSETTING);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	public void getGroupChatSettings(String[] groupChatSettings) {

		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("ownerName", groupChatSettings[0]);
		if (groupChatSettings[1] != null) {
			property_map.put("memberName", groupChatSettings[1]);
		}
		if (groupChatSettings[2] != null) {
			property_map.put("groupId", groupChatSettings[2]);
		}
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetGroupSettings");
		servicebean.setCallBack(SingleInstance.mainContext);
		servicebean
				.setServiceMethods(EnumWebServiceMethods.GETGROUPCHATSETTING);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	public void setorDeleteFormFieldSettings(
			FormFieldSettingsBean settingsBean,
			Vector<DefaultPermission> attributeBeanList, Object obj) {
		Servicebean servicebean = new Servicebean();
		String fieldSettingsXml = xmlComposer
				.composeSetorDeleteFormsFieldSettings(settingsBean,
						attributeBeanList);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("fieldSettingsXml", fieldSettingsXml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetFormsFieldSettings");
		servicebean.setCallBack(obj);
		servicebean
				.setServiceMethods(EnumWebServiceMethods.SETORDELETEFORMFIELDSETTINGS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}

	
	public void UsersignIn(SiginBean sb) {
		String siginxml = xmlComposer.composeUserSiginXml(sb);
		Servicebean servicebean = new Servicebean();
		servicebean.setCallBack(sb.getCallBack());
		servicebean.setWsmethodname("Signin");
		HashMap<String, String> ws_argmap = new HashMap<String, String>();
		ws_argmap.put("loginxml", siginxml);
		ws_argmap.put("key", "0");
		servicebean.setProperty_map(ws_argmap);
		servicebean.setServiceMethods(EnumWebServiceMethods.USERSIGNIN);
		if (wsNotifier != null) {
			wsNotifier.clearBGTask();
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}



	/**
	 * Edit Form Fields with New Column Added
	 */

	public void editNewFormFields(Context context, String[] params,
			String[] field_name, String[] field_type,
			ArrayList<String[]> attributes) {
		String frnXML = xmlComposer.composeEditFormForNewMode(params,
				field_name, field_type, attributes);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("editFormXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("EditFormFields");
		servicebean
				.setServiceMethods(EnumWebServiceMethods.EDITFORMFORNEWFIELDADD);
		servicebean.setCallBack(context);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	/**
	 * Edit Existing Form Field
	 */
	public void updateExistingFormFields(Context context, String[] params,
			ArrayList<EditFormBean> eList) {
		String frnXML = xmlComposer.composeEditFormFieldForUpdateMode(params,
				eList);
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("editFormXml", frnXML);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("EditFormFields");
		servicebean.setServiceMethods(EnumWebServiceMethods.EDITFORM);
		servicebean.setCallBack(context);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	/**
	 * Start the WebServiceClientProcess Thread.Set the NameSpace,Url and Queue
	 * to WebServiceClientProcess.
	 */
	public void start(String ipaddress, int port) {
		wsNotifier = new WSNotifier(ipaddress, port, getmNamespace(),
				getmWsdlURL(), webServiceCallback);
	}

	/**
	 * Stop the WebServiceClientProcess Thread.
	 */
	public void stop() {
		if (wsNotifier != null) {
			wsNotifier.shutDowntaskmanager();
		}
	}

	public void NewVerification(String[] param, Object obj) {
		String requestxml = xmlComposer.composeNewVerificationXML(param);
		Servicebean servicebean = new Servicebean();
		servicebean.setWsmethodname("Newuserverification");
		HashMap<String, String> ws_argmap = new HashMap<String, String>();
		ws_argmap.put("requestxml", requestxml);
		servicebean.setProperty_map(ws_argmap);
		servicebean.setServiceMethods(EnumWebServiceMethods.NEWVERIFICATION);
		servicebean.setCallBack(obj);
		if (wsNotifier != null) {
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}
	public void Subscribe(SubscribeBean sb,Object obj) {
		Log.i("AAAA","WEBSERVICE CLIENT Subscribe");
		String regInputXml = xmlComposer.subscribeXML(sb);
		Servicebean servicebean = new Servicebean();
		servicebean.setWsmethodname("Subscribe");
		HashMap<String, String> ws_argmap = new HashMap<String, String>();
		ws_argmap.put("regInputXml", regInputXml);
		servicebean.setProperty_map(ws_argmap);
		servicebean.setServiceMethods(EnumWebServiceMethods.SUBSCRIBENEW);
		servicebean.setCallBack(obj);
		if (wsNotifier != null) {
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}
	public void SetMyAccount(ProfileBean pb,Object obj) {
		Log.i("AAAA","WEBSERVICE CLIENT SetMyAccount");
		String requestxml = xmlComposer.composeSetMyAccountXML(pb);
		Servicebean servicebean = new Servicebean();
		servicebean.setWsmethodname("Setmyaccount");
		HashMap<String, String> ws_argmap = new HashMap<String, String>();
		ws_argmap.put("requestxml", requestxml);
		servicebean.setProperty_map(ws_argmap);
		servicebean.setServiceMethods(EnumWebServiceMethods.SETMYACCOUNT);
		servicebean.setCallBack(obj);
		if (wsNotifier != null) {
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}
	public void GetMyAccount(String uname ,Object obj) {
		Log.i("AAAA", "WEBSERVICE CLIENT SetMyAccount");
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("username", uname);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("Getmyaccount");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETMYACCOUNT);
		servicebean.setCallBack(obj);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void UpdateSecretQuestion(String[] param ,Object obj) {
		String requestxml = xmlComposer.composeUpdateSecretAnswerXML(param);
		Servicebean servicebean = new Servicebean();
		servicebean.setWsmethodname("UpdateSecretquestionanswer");
		HashMap<String, String> ws_argmap = new HashMap<String, String>();
		ws_argmap.put("requestXml", requestxml);
		servicebean.setProperty_map(ws_argmap);
		servicebean.setServiceMethods(EnumWebServiceMethods.UPDATESECRETANSWER);
		servicebean.setCallBack(obj);
		if (wsNotifier != null) {
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}
	public void ResetAccount(String uname ,Object obj) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("userId", uname);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("ResetAccount");
		servicebean.setServiceMethods(EnumWebServiceMethods.RESETACCOUNT);
		servicebean.setCallBack(obj);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}
	public void GetMypin(String username) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("username", username);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("Getmypin");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETMYPIN);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}
	public void SearchPeopleByAccount(String[] param ,Object obj) {
		String accountXml = xmlComposer.composeSearchPeopleXML(param);
		Servicebean servicebean = new Servicebean();
		servicebean.setWsmethodname("SearchPeopleByAccount");
		HashMap<String, String> ws_argmap = new HashMap<String, String>();
		ws_argmap.put("accountXml", accountXml);
		servicebean.setProperty_map(ws_argmap);
		servicebean.setServiceMethods(EnumWebServiceMethods.SEARCHPEOPLEBYACCOUNT);
		servicebean.setCallBack(obj);
		if (wsNotifier != null) {
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}

	public void GetAllProfile(String username, String buddyname ,Object obj) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("username", username);
		property_map.put("buddyname", buddyname);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("Getallprofile");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETALLPROFILE);
		servicebean.setCallBack(obj);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void GetMySecretQuestion(String username,Object obj) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("username", username);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("Getmysecretquestion");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETMYSECRETQUESTION);
		servicebean.setCallBack(obj);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);

	}
	public void GetStates(String date ,Object obj) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("date", date);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetStates");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETSTATES);
		servicebean.setCallBack(obj);
		if (wsNotifier != null) {
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}
	public void GetCities(String date ,Object obj){
		Servicebean servicebean = new Servicebean();
		HashMap<String,String> propert_map = new HashMap<String,String>();
		propert_map.put("date",date);
		servicebean.setProperty_map(propert_map);
		servicebean.setWsmethodname("GetCities");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETCITIES);
		servicebean.setCallBack(obj);
		if ((wsNotifier !=null)){
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}
	public void GetHospitalDetails(String date,Object obj) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("date", date);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("Gethospitaldetails");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETHOSPITALDETAILS);
		servicebean.setCallBack(obj);
		if (wsNotifier != null) {
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}
	public void GetMedicalSocieties(String medicalsocietie,Object obj) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("medicalsocietie", medicalsocietie);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("Getmedicalsocieties");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETMEDICALSOCIETY);
		servicebean.setCallBack(obj);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void GetSpecialities(String date,Object obj) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("date", date);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetSpecialities");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETSPECIALTY);
		servicebean.setCallBack(obj);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void GetMedicalSchools(String date,Object obj) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("date", date);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetMedicalSchools");
		servicebean.setServiceMethods(EnumWebServiceMethods.MEDICALSCHOOLS);
		servicebean.setCallBack(obj);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void FileUpdates(String username,String filename) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("fileowner", username);
		property_map.put("filename", filename);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("fileUpdates");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETMEDICALSOCIETY);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void AcceptRejectGroupmember(String groupid,String member,String status,Object obj) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("groupId", groupid);
		property_map.put("groupMemberName", member);
		property_map.put("status", status);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("AcceptRejectGroupmember");
		servicebean.setServiceMethods(EnumWebServiceMethods.ACCEPTREJECTGROUP);
		servicebean.setCallBack(obj);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void GetGroupDetails(String username,String groupid,Object obj) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("userName", username);
		property_map.put("groupId", groupid);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetGroupDetails");
		servicebean.setServiceMethods(EnumWebServiceMethods.GETGROUPDETAILS);
		servicebean.setCallBack(obj);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void createRoundingGroup(GroupBean bean, Object callBack) {
		Servicebean servicebean = new Servicebean();
		String CreateGroup = xmlComposer.composeCreateGroup(bean);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("groupXml", CreateGroup.trim());
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("CreateorModifyRounding");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.CREATEORMODIFYROUNDING);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void getRoundingGroupAndMembers(String ownerName, String groupModifiedDate,Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("ownerName", ownerName);
		property_map.put("groupModifiedDate", groupModifiedDate);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetRoundingGroupAndMembers");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETROUNDINGGROUPS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void SetMemberRights(String username, String groupid,String status,String role,Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("username", username);
		property_map.put("groupid", groupid);
		property_map.put("status", status);
		property_map.put("role", role);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetMemberRights");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.SETMEMBERRIGHTS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void SetPatientRecord(PatientDetailsBean pbean,Object callBack) {
		Servicebean servicebean = new Servicebean();
		String patientrecordxml = xmlComposer.composePatientRecordXML(pbean);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("patientrecordxml", patientrecordxml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetPatientRecord");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.SETPATIENTRECORD);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void SetTaskRecord(TaskDetailsBean tBean,Object callBack) {
		Servicebean servicebean = new Servicebean();
		String taskrecordxml = xmlComposer.composeCreateTaskXML(tBean);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("taskrecordxml", taskrecordxml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetTaskRecord");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.CREATETASK);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void SetOrEditRoleAccess(RolePatientManagementBean pbean,RoleEditRndFormBean eBean,RoleTaskMgtBean tBean,RoleCommentsViewBean cBean,Object callBack) {
		Servicebean servicebean = new Servicebean();
		String requestxml = xmlComposer.composeSetorEditRoleAccessXML(pbean, eBean, tBean, cBean);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("requestxml", requestxml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetorEditRoleAccess");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.SETOREDITROLEACCESS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void GetPatientRecords(String patientid, String groupid,Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("patientid", patientid);
		property_map.put("groupid", groupid);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetPatientRecord");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETPATIENTRECORDS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void GetTaskInfo(String taskid, String groupid,Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("taskid", taskid);
		property_map.put("groupid", groupid);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetTaskRecord");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETTASKINFO);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void SetPatientComments(PatientCommentsBean cBean,Object callBack) {
		Servicebean servicebean = new Servicebean();
		String requestxml = xmlComposer.composeSetPatientCommentsXML(cBean);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("requestxml", requestxml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetPatientComments");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.SETPATIENTCOMMENTS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void SetPatientDescription(PatientDescriptionBean cBean,Object callBack) {
		Servicebean servicebean = new Servicebean();
		String patientdescriptionxml = xmlComposer.composeSetPatientDescriptionXML(cBean);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("patientdescriptionxml", patientdescriptionxml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("SetPatientDescription");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.SETPATIENTDESCRIPTION);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void GetPatientDescription(String patientid,String reportid,Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("patientid", patientid);
		property_map.put("reportid", reportid);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetPatientDescription");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETPATIENTDESCRIPTION);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void GetRoleAccess(String userid, String groupid,String roleid,Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("userid", userid);
		property_map.put("groupid", groupid);
		property_map.put("rid", roleid);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetRoleAccess");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETROLEACCESS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void GetPatientComments(String commentid, String groupid,String patientid,Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("commentid", commentid);
		property_map.put("groupid", groupid);
		property_map.put("patientid", patientid);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetPatientComments");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETPATIENTCOMMENTS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void dischargePatient(String username, String groupId,String patientid,Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("username", username);
		property_map.put("groupId", groupId);
		property_map.put("patientid", patientid);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("DischargePatient");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.PATIENTDISCHARGE);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void GetMemberRights(String username, String groupid,Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("username", username);
		property_map.put("groupid", groupid);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetMemberRights");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETMEMBERRIGHTS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void DeleteTask(String username, String taskId,String groupId,Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("username", username);
		property_map.put("taskId", taskId);
		property_map.put("groupId", groupId);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("DeleteTask");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.DELETETASK);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}
	public void GetFileDetails(String username, String branchtype,Object callBack) {
		Servicebean servicebean = new Servicebean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("username", username);
		property_map.put("branchtype", branchtype);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("GetFilesDetail");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.GETFILEDETAILS);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

	public void FeedBack(String feedback) {
		String feedbackxml = xmlComposer.composeFeedBackXML(feedback);
		Servicebean servicebean = new Servicebean();
		servicebean.setWsmethodname("SetFeedback");
		HashMap<String, String> ws_argmap = new HashMap<String, String>();
		ws_argmap.put("feedbackxml", feedbackxml);
		servicebean.setProperty_map(ws_argmap);
		servicebean.setServiceMethods(EnumWebServiceMethods.SETFEEDBACK);
		if (wsNotifier != null) {
			wsNotifier.addTasktoExecutor(servicebean);
		}
	}
	public void UpdateChatTemplate(String[] param,Object callBack) {
		Servicebean servicebean = new Servicebean();
		String templatexml = xmlComposer.composeUpdateChatTemplateXML(param);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("templatexml", templatexml);
		servicebean.setProperty_map(property_map);
		servicebean.setWsmethodname("UpdateChatTemplate");
		servicebean.setCallBack(callBack);
		servicebean.setServiceMethods(EnumWebServiceMethods.UPDATECHATTEMPLATE);
		if (wsNotifier != null)
			wsNotifier.addTasktoExecutor(servicebean);
	}

}
