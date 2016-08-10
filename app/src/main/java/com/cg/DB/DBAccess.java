package com.cg.DB;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.bean.BuddyPermission;
import com.bean.DefaultPermission;
import com.bean.EditFormBean;
import com.bean.FormFieldBean;
import com.bean.FormInfoBean;
import com.bean.GroupChatBean;
import com.bean.GroupChatPermissionBean;
import com.bean.NotifyListBean;
import com.bean.OfflineFormRecordBean;
import com.bean.ProfileBean;
import com.bean.UploadDownloadStatusBean;
import com.bean.UserBean;
import com.cg.Calendar.ScheduleBean;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.CompleteListBean;
import com.cg.files.Components;
import com.cg.forms.Alert;
import com.cg.forms.InputsFields;
import com.cg.forms.formItem;
import com.cg.hostedconf.AppReference;
import com.cg.quickaction.ContactLogicbean;
import com.cg.quickaction.QuickActionBroadCastReceiver;
import com.cg.quickaction.QuickActionBuilder;
import com.cg.quickaction.TableColumnsBean;
import com.cg.settings.UserSettingsBean;
import com.cg.snazmed.R;
import com.cg.timer.ReminderDetail;
import com.cg.timer.ReminderRetrieve;
import com.chat.ChatBean;
import com.group.chat.ChatInfoBean;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.PatientDetailsBean;
import org.lib.model.BuddyInformationBean;
import org.lib.model.ChattemplateModifieddate;
import org.lib.model.FieldTemplateBean;
import org.lib.model.FormAttributeBean;
import org.lib.model.GroupBean;
import org.lib.model.GroupMemberBean;
import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.PatientCommentsBean;
import org.lib.model.PatientDescriptionBean;
import org.lib.model.PermissionBean;
import org.lib.model.RecordTransactionBean;
import org.lib.model.RoleAccessBean;
import org.lib.model.RoleCommentsViewBean;
import org.lib.model.RoleEditRndFormBean;
import org.lib.model.RolePatientManagementBean;
import org.lib.model.RoleTaskMgtBean;
import org.lib.model.SignalingBean;
import org.lib.model.TaskDetailsBean;
import org.lib.model.UtilityBean;
import org.lib.model.chattemplatebean;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class DBAccess extends SQLiteOpenHelper {

	private static DBAccess dbHelper;

	public static final int DATABASE_VERSION = 1;
	/**
	 * DB Inside COMMedia Folder //
	 */

	public static final String DATABASE_NAME = Environment
			.getExternalStorageDirectory()
			+ "/COMMedia/"
			+ SingleInstance.mainContext.getResources().getString(
					R.string.app_name) + ".db";

	/**
	 * DB inside application
	 */
//	 public static final String DATABASE_NAME = SingleInstance.mainContext
//	 .getResources().getString(R.string.app_name) + ".db";

	private SQLiteDatabase db = null;

	String profileTemplate = "CREATE TABLE if not exists `profiletemplate` (`profileid` int(100)  NOT NULL,`profilename` varchar(255) NOT NULL,`profiletimestamp` varchar(45),PRIMARY KEY (`profileid`))";
	String fieldTemplate = "CREATE TABLE if not exists `fieldtemplate` (`profileid` int(100) NOT NULL,`groupname` varchar(255) NOT NULL ,`fieldid` int(10) NOT NULL,`fieldname` varchar(255) NOT NULL,`fieldtype` varchar(45) NOT NULL,`createddate` varchar(45),`modifieddate` varchar(45),PRIMARY KEY (`fieldid`))";
	String profileFieldValues = "CREATE TABLE if not exists `profilefieldvalues` (`fieldid` int(100) NOT NULL,`userid` varchar(255) NOT NULL,`fieldvalue` varchar(512) NOT NULL,`createddate` varchar(45),`modifieddate` varchar(45),`flag` tinyint(1),`status` tinyint(1),PRIMARY KEY (`fieldid`,`userid`))";
	String profileGroup = "CREATE TABLE if not exists `profilegroup` (`groupid` int(10) NOT NULL,`groupname` varchar(255) NOT NULL,PRIMARY KEY (`groupid`,`groupname`))";
	String component = "create table if not exists component(componentid INTEGER PRIMARY KEY AUTOINCREMENT, componenttype nvarchar(20), componentpath nvarchar(250), ftppath nvarchar(250), componentname nvarchar(250), fromuser nvarchar(25),comment nvarchar(250), owner nvarchar(50), vanishmode nvarchar(20),vanishvalue nvarchar(20),receiveddateandtime nvarchar(100),reminderdateandtime nvarchar(100), reminderstatus integer, viewmode integer, reminderzone nvarchar(100), reminderresponsetype nvarchar(50),bscategory nvarchar(50),bsstatus nvarchar(50),bsdirection nvarchar(50),parentid nvarchar(100),username nvarchar(100),touser nvarchar(100))";
	String shareComponent = "create table if not exists sharecomponent(componentid integer primary key, componentpath nvarchar(250), componenttype nvarchar(25), fromuser nvarchar(50), touser nvarchar(50), sharestatus integer, ftppath nvarchar(250))";
	String utility_table = "create table if not exists utility(id INTEGER PRIMARY KEY,userid nvarchar(20),org_name nvarchar(40),product_name nvarchar(40),quantity nvarchar(20),price nvarchar(20),video_file nvarchar(30),img_file nvarchar(120),voice nvarchar(30),location nvarchar(60),address nvarchar(200),country nvarchar(20),state nvarchar(20),city nvarchar(20),pin nvarchar(20),email nvarchar(30),c_no nvarchar(20),entry_mode tinyint(1),utility_name nvarchar(30),posted_date nvarchar(20))";
	String look_up = "create table if not exists formslookup(Id INTEGER PRIMARY KEY AUTOINCREMENT,owner nvarchar(30),tablename nvarchar(30),tableid nvarchar(20),rowcount nvarchar(20),formtime nvarchar(20),status nvarchar(20),Formiconname nvarchar(20),Formdescription nvarchar(20))";
	String form_settings = "create table if not exists formsettings(settingid nvarchar(30) PRIMARY KEY,formowenerid nvarchar(30),formid nvarchar(30),buddyid nvarchar(20),accesscode nvarchar(20),synccode nvarchar(20),syncquery nvarchar(20),datecreated nvarchar(20),datemodified nvarchar(20))";
	String form_info = "create table if not exists forminfo(Id INTEGER PRIMARY KEY,tablename nvarchar(20),column nvarchar(20),entrymode nvarchar(20),validdata nvarchar(20),defaultvalue nvarchar(20),instruction nvarchar(20),errortip nvarchar(20),attributeid nvarchar(20))";
	String offlinecallsettingdetails = "create table if not exists offlinecallsettingdetails(Id INTEGER PRIMARY KEY AUTOINCREMENT,userid nvarchar(250),buddyid nvarchar(250),message_title nvarchar(250),message_type nvarchar(250),message nvarchar(250),response_type nvarchar(50),url nvarchar(20),when_action nvarchar(20),record_time nvarchar(50),status tinyint(1))";
	String offlinecallresponsedetails = "create table if not exists offlinecallresponsedetails (id INTEGER PRIMARY KEY AUTOINCREMENT,userid nvarchar(255),buddyid nvarchar(255),configid Integer,messagetitle nvarchar(255),responsetype nvarchar(45),response nvarchar(255),receivedtime nvarchar(45),status tinyint(1))";
	String offlinecallpendingclones = "create table if not exists offlinecallpendingclones (id INTEGER PRIMARY KEY AUTOINCREMENT,config_id nvarchar(25) ,fromuser nvarchar(255),messagetitle nvarchar(255),messagetype nvarchar(255),message nvarchar(255),responsetype nvarchar(255),response nvarchar(255),url varchar(500),receivedtime nvarchar(45),sendstatus nvarchar(45),username nvarchar(20),when_action nvarchar(20),status tinyint(1))";
	String clonemaster = "create table if not exists clonemaster(id INTEGER PRIMARY KEY,cid nvarchar(25),cdescription nvarchar(25))";
	String quickaction = "create table if not exists CustomAction(Id INTEGER PRIMARY KEY AUTOINCREMENT,label nvarchar(30),condition nvarchar(50),description nvarchar(50),time nvarchar(30),status integer,ftppath nvarchar(30),actioncode nvarchar(30),touser nvarchar(50),username nvarchar(20),mode nvarchar(20),freq nvarchar(20),runmode varchar(20))";
	String permission_table = "create table if not exists setpermission(id INTEGER PRIMARY KEY,userid nvarchar(25),buddyid nvarchar(25),audiocall tinyint(1),videocall tinyint(1),audiobroadcast tinyint(1),videobroadcast tinyint(1),audiounicast tinyint(1),videounicast tinyint(1),mmchat tinyint(1),textmessage tinyint(1),videomessage tinyint(1),photomessage tinyint(1),audiomessage tinyint(1),sketchmessage tinyint(1),shareprofile tinyint(1),viewprofile tinyint(1),answeringmachine tinyint(1),formshare tinyint(1),hostedconf tinyint(1),record_time nvarchar(50))";
	String MMChat = "create table if not exists MMChat(sessionid nvarchar(30),filepath nvarchar(30),signalid nvarchar(20),ftppath nvarchar(20))";
	String User_Settings = "create table if not exists Settings(Id INTEGER PRIMARY KEY AUTOINCREMENT,username nvarchar(30),settings nvarchar(30),service nvarchar(30),value1 nvarchar(30),value2 nvarchar(30),remoteaddress nvarchar(30))";
	String user_services = "create table if not exists UserServices(Id INTEGER PRIMARY KEY AUTOINCREMENT,servicename nvarchar(30),serviceid nvarchar(30))";
	String create_group = "create table if not exists grouplist(id INTEGER PRIMARY KEY AUTOINCREMENT, groupid int(100), groupname varchar(225),groupowner varchar(100), createddate varchar(45), modifieddate varchar(45),username varchar(45),lastmsg varchar(100),category varchar(25),recentdate varchar(100),groupdescription varchar(100),groupicon varchar(200),grouptype varchar(45),adminmembers varchar(255))";
	String create_group_members = "create table if not exists groupdetails(id INTEGER PRIMARY KEY AUTOINCREMENT, groupid int(100), active_members varchar(255), inactive_members varchar(255), createddate varchar(45) NOT NULL, modifieddate varchar(45) NOT NULL,groupowner varchar(100) NOT NULL,username varchar(45) NOT NULL,groupdescription varchar(100),groupicon varchar(200),invitemembers varchar(100),grouptype varchar(45), adminmembers varchar(255))";
	String chat = "create table if not exists chat(id INTEGER PRIMARY KEY AUTOINCREMENT, category varchar(20), subcategory varchar(45), groupid int(100), username varchar(100), mimetype varchar(45), fromuser varchar(100), touser varchar(100), message varchar(200), media varchar(200), ftpusername varchar(100), ftppassword varchar(200), sessionid varchar(200), signalid varchar(200) unique, senttime varchar(200), senttimezone varchar(200),privatemembers varchar(200),parentid varchar(200),remindertime varchar(200),status tinyint(1),unreadstatus tinyint(1), thumb tinyint(1) DEFAULT 0,reply varchar(20),replied varchar(20),confirm varchar(20),urgent varchar(20),unview varchar(20),withdrawn varchar(20),senderwithdraw varchar(20) ,dateandtime varchar(100))";
	String imchat = "create table if not exists imchat(id INTEGER PRIMARY KEY AUTOINCREMENT, groupid int(100), groupname varchar(225),groupowner varchar(100), createddate varchar(45), modifieddate varchar(45),username varchar(45),lastmsg varchar(100),category varchar(25),recentdate varchar(100))";
	String schedulemsg = "create table if not exists schedulemsg(id INTEGER PRIMARY KEY AUTOINCREMENT, category varchar(20), subcategory varchar(45), groupid int(100), username varchar(100), mimetype varchar(45), fromuser varchar(100), touser varchar(100), message varchar(200), media varchar(200), ftpusername varchar(100), ftppassword varchar(100), sessionid varchar(200), signalid varchar(200), senttime varchar(200), senttimezone varchar(200),privatemembers varchar(200))";
	String uploaddownload = "create table if not exists uploaddownload(id INTEGER PRIMARY KEY AUTOINCREMENT, username varchar(100), media varchar(200), ftpusername varchar(100), ftppassword varchar(100), status tinyint(2), operations varchar(50),modules varchar(50),othervalues varchar(100))";
	String group_permission_table = "create table if not exists setgrouppermission(id INTEGER PRIMARY KEY,userid nvarchar(25),groupid nvarchar(25),groupowner nvarchar(25),groupmember nvarchar(25),audioconf tinyint(1),videoconf tinyint(1),audiobroadcast tinyint(1),videobroadcast tinyint(1),textmessage tinyint(1),videomessage tinyint(1),photomessage tinyint(1),audiomessage tinyint(1),privatemessage tinyint(1),replybackmessage tinyint(1),schedulemessage tinyint(1),deadlinemessage tinyint(1),withdrawn tinyint(1),chat tinyint(1))";
	String formfieldbuddyaccess = "create table if not exists formfieldbuddyaccess(id INTEGER PRIMARY KEY, formid nvarchar(25), attributeid nvarchar(25), accessiblebuddy nvarchar(100), buddyaccess nvarchar(25))";
	String formfielddefaultaccess = "create table if not exists formfielddefaultaccess(id INTEGER PRIMARY KEY, formid nvarchar(25), attributeid nvarchar(25), defaultaccess nvarchar(25))";
	String autoacceptcalls = "create table if not exists autoacceptcalls(owner nvarchar(255), username nvarchar(255),flag nvarchar(25))";
	String scheduleevent = "create table if not exists scheduleevent(owner nvarchar(255), title nvarchar(255), starttime nvarchar(225), endtime nvarchar(255), eventdate nvarchar(255))";
	String securityquestions = "create table if not exists securityquestions(id tinyint(4), questions nvarchar(225), createddate nvarchar(255))";
    String recordtransactiondetails = "create table if not exists recordtransactiondetails(id INTEGER PRIMARY KEY AUTOINCREMENT, fromname nvarchar(100), toname nvarchar(100),parentid nvarchar(250), sessionid nvarchar(250), type nvarchar(100), starttime nvarchar(100), endtime nvarchar(100), calltime nvarchar(100), userid nvarchar(100), network nvarchar(50), deviceos nvarchar(50),recordedfile nvarchar(100), calltype nvarchar(100),bs_calltype tinyint(4),bs_callstatus tinyint(4),bs_callcategory tinyint(4),sortdate nvarchar(100),status tinyint(4),activecallstatus nvarchar(100),chatid nvarchar(100),host varchar(100), participants varchar(256),hostname varchar(50),participantname varchar(250))";
	String chattemplate = "create table if not exists chattemplate(id varchar(10), message nvarchar(225),userid nvarchar(225),editmodevalue nvarchar(50))";
	String serverhelp = "create table if not exists serverhelp(method varchar(100), lastmodified nvarchar(225))";
	String roundingmemberdetails = "create table if not exists roundingmemberdetails(groupid nvarchar(25),membername nvarchar(25),role nvarchar(25),admin nvarchar(25))";
	String patientdetails = "create table if not exists patientdetails(groupid nvarchar(25), patientid nvarchar(25), creatorname nvarchar(25), firstname nvarchar(25), middlename nvarchar(25), lastname nvarchar(25), dob nvarchar(25), sex nvarchar(25),hospital nvarchar(25), mrn nvarchar(25), floor nvarchar(25), ward nvarchar(25), room nvarchar(25), bed nvarchar(25),admissiondate nvarchar(25), assignedmembers nvarchar(255),status nvarchar(25))";
	String taskdetails ="create table if not exists taskdetails(groupid nvarchar(25), taskid nvarchar(25), patientid nvarchar(25),creatorname nvarchar(25), taskdesc nvarchar(25), callpatient nvarchar(25), duedate nvarchar(25), duetime nvarchar(25), setreminder nvarchar(25),timetoremind nvarchar(25),assignmembers nvarchar(25),taskstatus nvarchar(25))";
	String patientcomments = "create table if not exists patientcomments(groupid nvarchar(25), commentid nvarchar(25),patientid nvarchar(25),groupowner nvarchar(25), groupmember nvarchar(25), dateandtime nvarchar(25), comments nvarchar(225))";
	String patientdescription = "create table if not exists patientdescription(patientid nvarchar(25),groupid nvarchar(25), reportid nvarchar(25),creatorname nvarchar(25),currentstatus nvarchar(25), diagnosis nvarchar(200), medications nvarchar(255),testandvitals nvarchar(255),hospitalcourse nvarchar(100),consults nvarchar(255),reportmodifier nvarchar(100))";
	String roleaccess ="create table if not exists roleaccess(groupid nvarchar(25), groupOwner nvarchar(25), groupmember nvarchar(25), roleid nvarchar(25), role nvarchar(25), patientmanagement tinyint(1), taskmanagement tinyint(1), editroundingform tinyint(1), commentsview tinyint(1))";
	String rolepatientmgt ="create table if not exists rolepatientmgt(groupid nvarchar(25), groupOwner nvarchar(25), groupmember nvarchar(25), roleid nvarchar(25), role nvarchar(25), padd tinyint(1), pmodify tinyint(1), pdelete tinyint(1), discharge tinyint(1))";
	String roleeditroundignform ="create table if not exists roleeditroundignform(groupid nvarchar(25), groupOwner nvarchar(25), groupmember nvarchar(25), roleid nvarchar(25), role nvarchar(25), status tinyint(1), diagnosis tinyint(1), testandvitals tinyint(1), hospitalcourse tinyint(1), notes tinyint(1), consults tinyint(1))";
	String roletransctionmgt ="create table if not exists roletransctionmgt(groupid nvarchar(25), groupOwner nvarchar(25), groupmember nvarchar(25), roleid nvarchar(25), role nvarchar(25), tattending tinyint(1), tfellow tinyint(1), tchiefresident tinyint(1), tresident tinyint(1), tmedstudent tinyint(1))";
	String rolecommentsview ="create table if not exists rolecommentsview(groupid nvarchar(25), groupOwner nvarchar(25), groupmember nvarchar(25), roleid nvarchar(25), role nvarchar(25), cattending tinyint(1), cfellow tinyint(1), cchiefresident tinyint(1), cresident tinyint(1), cmedstudent tinyint(1))";
	String profiledetails ="create table if not exists profiledetails(username nvarchar(100), nickname nvarchar(100), photo nvarchar(100), title nvarchar(100), firstname nvarchar(100), lastname nvarchar(100), sex nvarchar(100), usertype nvarchar(100), state nvarchar(100), profession nvarchar(100), speciality nvarchar(100),medicalschool nvarchar(100), residency nvarchar(100), fellowship nvarchar(100), officeaddress nvarchar(100), hospitalaffiliation nvarchar(100), citations nvarchar(100), organization nvarchar(100), tos tinyint(1), baa tinyint(1), status nvarchar(100), mobileno nvarchar(100), homeaddress nvarchar(100), dob nvarchar(100),ssn nvarchar(25),race nvarchar(100), ethinicity nvarchar(100), maritalstatus nvarchar(100))";
	String chatinfo ="create table if not exists chatinfo(sid nvarchar(25), members nvarchar(125),status nvarchar(25),datetime nvarchar(100))";
	String statedetails ="create table if not exists statedetails(statename nvarchar(125),statecode nvarchar(25))";
	String medicalschools ="create table if not exists medicalschools(medicalschool nvarchar(200))";
	String specialitydetails ="create table if not exists specialitydetails(speciality nvarchar(200),code nvarchar(25))";
	String hospitaldetails ="create table if not exists hospitaldetails(hospitalname nvarchar(200))";
	String medicalsocieties ="create table if not exists medicalsocieties(id nvarchar(25),medicalsociety nvarchar(200))";
	String seeallpatientdetails = "create table if not exists seeallpatientdetails(groupid nvarchar(25), patientid nvarchar(100), diagnosis nvarchar(100), active nvarchar(100), commentdate nvarchar(100))";
	private CallDispatcher callDisp;

	public DBAccess(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = CallDispatcher
					.getCallDispatcher(SingleInstance.mainContext);
	}

	public static DBAccess getdbHeler(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBAccess(context);
			dbHelper.openDatabase();
			dbHelper.insertCloneMasterDatas();
			dbHelper.insertRecordsonMasterTable();
		}

		return dbHelper;
	}

	public static DBAccess getdbHeler() {
		if (dbHelper == null) {
			dbHelper = new DBAccess(SingleInstance.mainContext);
			dbHelper.openDatabase();
			dbHelper.insertCloneMasterDatas();
			dbHelper.insertRecordsonMasterTable();
		}

		return dbHelper;
	}

	/**
	 * Open writable database when database is in null.
	 */
	public void openDatabase() {
		if (db == null) {

			try {
				db = getWritableDatabase();
			} catch (Exception e) {
				e.printStackTrace();

			}
		}

	}

	/**
	 * Open Readable database when database is in null.
	 */

	public void openReadableDatabase() {
		try {
			if (db == null)
				db = getReadableDatabase();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();
		db.close();
		db = null;
	}

	/**
	 * Create all Tables in database.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(fieldTemplate);
		db.execSQL(profileFieldValues);
		db.execSQL(profileGroup);
		db.execSQL(profileTemplate);
		db.execSQL(component);
		db.execSQL(shareComponent);
		db.execSQL(utility_table);
		db.execSQL(look_up);
		db.execSQL(form_settings);
		db.execSQL(form_info);
		db.execSQL(clonemaster);
		db.execSQL(offlinecallpendingclones);
		db.execSQL(offlinecallresponsedetails);
		db.execSQL(offlinecallsettingdetails);
		db.execSQL(quickaction);
		db.execSQL(permission_table);
		db.execSQL(MMChat);
		db.execSQL(User_Settings);
		db.execSQL(user_services);
		db.execSQL(create_group);
		db.execSQL(create_group_members);
		db.execSQL(chat);
		db.execSQL(imchat);
		db.execSQL(schedulemsg);
		db.execSQL(uploaddownload);
		db.execSQL(group_permission_table);
		db.execSQL(formfieldbuddyaccess);
		db.execSQL(formfielddefaultaccess);
		db.execSQL(autoacceptcalls);
		db.execSQL(scheduleevent);
		db.execSQL(securityquestions);
		db.execSQL(recordtransactiondetails);
		db.execSQL(chattemplate);
		db.execSQL(serverhelp);
		db.execSQL(roundingmemberdetails);
		db.execSQL(patientdetails);
		db.execSQL(taskdetails);
		db.execSQL(patientcomments);
		db.execSQL(patientdescription);
		db.execSQL(rolepatientmgt);
		db.execSQL(roleeditroundignform);
		db.execSQL(roletransctionmgt);
		db.execSQL(rolecommentsview);
		db.execSQL(roleaccess);
		db.execSQL(profiledetails);
		db.execSQL(chatinfo);
		db.execSQL(statedetails);
		db.execSQL(medicalschools);
		db.execSQL(specialitydetails);
		db.execSQL(hospitaldetails);
		db.execSQL(medicalsocieties);
		db.execSQL(seeallpatientdetails);
	}

    public void updateThumbs(String signalID){
        try {
            String s = "update chat set thumb=1 where signalid='" + signalID + "'";
            Log.d("abcdef","SQL => "+s);
            db.execSQL(s);
        }catch(Exception e){
            Log.d("abcdef","novalue in DB for "+signalID+" returns with error "+e.toString());
        }
    }
    public void updateWithdraw(GroupChatBean groupChatBean){
        try {
            String s = "update chat set withdrawn='1' and mimetype='text' where signalid='" + groupChatBean.getpSingnalId() + "'";
            Log.d("abcdef","SQL => "+s);
                Log.i("withdraw","DbAccess updatewothdraw mathod");
			//For withdraw message
			//start
			    ContentValues contentValues =new ContentValues();
//			    contentValues.put("subcategory","null");
			if(groupChatBean.getFrom()!=null && !groupChatBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)){
				contentValues.put("mimetype","text");
				contentValues.put("message","Message withdrawn @"+getCurrentTime());
				contentValues.put("media","null");
			}else{
//				if(groupChatBean.getMimetype()!=null && groupChatBean.getMimetype().equalsIgnoreCase("text")) {
//					contentValues.put("message",groupChatBean.getMessage()+" @"+getCurrentTime());
//				}else{
//					contentValues.put("message","@"+getCurrentTime());
//				}
				contentValues.put("senderwithdraw","Message withdrawn @"+getCurrentTime());
			}
			contentValues.put("withdrawn","1");
//			contentValues.put("withdrawtime",getCurrentTime());
				int row_id = updateChatWithdraw_row(contentValues, "signalid='" + groupChatBean.getpSingnalId()
						+ "'");

          //end

//            db.execSQL(s);
        }catch(Exception e){
            Log.d("abcdef","novalue in DB for "+groupChatBean.getpSingnalId()+" returns with error "+e.toString());
        }
    }
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@SuppressWarnings("finally")
	public int insertProfileTemplate(ContentValues cv) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.insert("profiletemplate", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	@SuppressWarnings("finally")
	public int updateProfileTemplate(ContentValues cv, String condition) {

		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("profiletemplate", cv, condition, null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	public Vector<FieldTemplateBean> getProfileFieldsValues(String userId) {

		Vector<FieldTemplateBean> fieldTemplateList = null;
		FieldTemplateBean fBean = null;
		try {

			if (!db.isOpen())
				openDatabase();

			String querry = "select groupname from profilegroup";
			Cursor grp_cursor = db.rawQuery(querry, null);
			grp_cursor.moveToFirst();
			fieldTemplateList = new Vector<FieldTemplateBean>();
			while (grp_cursor.isAfterLast() == false) {
				String group_name = grp_cursor.getString(0);
				StringBuilder sb = new StringBuilder();
				sb.append("Select DISTINCT `profiletemplate`.`profileid`,`profiletemplate`.`profilename`,`fieldtemplate`.`groupname` ,`fieldtemplate`.`fieldid` ,`fieldtemplate`.`fieldname`,`fieldtemplate`.`fieldtype`,(Select `fieldvalue` from `profilefieldvalues` where `userid`='"
						+ userId
						+ "' and `fieldid`=`fieldtemplate`.`fieldid`)`fieldvalue` from `fieldtemplate`,`profiletemplate` where  `profiletemplate`.`profileid`=`fieldtemplate`.`profileid` and `fieldtemplate`.`groupname`='"
						+ group_name + "'");

				Cursor c = db.rawQuery(sb.toString(), null);
				c.moveToFirst();
				while (c.isAfterLast() == false) {
					Log.i("forms", "inside while");
					fBean = new FieldTemplateBean();
					fBean.setGroupName(c.getString(c
							.getColumnIndex("groupname")));
					fBean.setFieldId(c.getString(c.getColumnIndex("fieldid")));
					fBean.setFieldName(c.getString(c
							.getColumnIndex("fieldname")));
					fBean.setFieldType(c.getString(c
							.getColumnIndex("fieldtype")));
					fBean.setFiledvalue(c.getString(c
							.getColumnIndex("fieldvalue")));
					fieldTemplateList.add(fBean);

					c.moveToNext();
				}
				c.close();
				grp_cursor.moveToNext();
			}
			grp_cursor.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldTemplateList;
	}

	public Vector<FieldTemplateBean> getfieldstoDownload() {

		Vector<FieldTemplateBean> fieldTemplateList = null;
		FieldTemplateBean fBean = null;
		try {

			if (!db.isOpen())
				openDatabase();

			fieldTemplateList = new Vector<FieldTemplateBean>();

			StringBuilder sb = new StringBuilder();
			sb.append("select * from profilefieldvalues where status=0");
			Cursor c = db.rawQuery(sb.toString(), null);
			c.moveToFirst();
			while (c.isAfterLast() == false) {

				fBean = new FieldTemplateBean();
				fBean.setFieldId(c.getString(0));
				fBean.setFiledvalue(c.getString(1));
				fBean.setUsername(c.getString(2));
				fBean.setStatus(c.getString(6));
				fieldTemplateList.add(fBean);
				c.moveToNext();
			}
			c.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldTemplateList;
	}

	public Vector<FieldTemplateBean> getProfileFields() {

		Vector<FieldTemplateBean> fieldTemplateList = null;
		FieldTemplateBean fBean = null;
		try {
			if (!db.isOpen())
				openDatabase();

			StringBuilder sb = new StringBuilder();
			Log.i("profile", "---------------" + CallDispatcher.LoginUser);
			sb.append("Select DISTINCT `profiletemplate`.`profileid`,`profiletemplate`.`profilename`,`fieldtemplate`.`groupname` ,`fieldtemplate`.`fieldid` ,`fieldtemplate`.`fieldname`,`fieldtemplate`.`fieldtype`,(Select `fieldvalue` from `profilefieldvalues` where `userid`='"
					+ CallDispatcher.LoginUser
					+ "' and `fieldid`=`fieldtemplate`.`fieldid`)`fieldvalue` from `fieldtemplate`,`profiletemplate` where  `profiletemplate`.`profileid`=`fieldtemplate`.`profileid`");
			Log.i("profile", "QUERYYYYYYYYYYYYYY" + sb.toString());
			Cursor c = db.rawQuery(sb.toString(), null);
			c.moveToFirst();
			fieldTemplateList = new Vector<FieldTemplateBean>();
			while (c.isAfterLast() == false) {
				Log.i("profile", "inside while");
				fBean = new FieldTemplateBean();
				fBean.setGroupName(c.getString(c.getColumnIndex("groupname")));
				fBean.setFieldId(c.getString(c.getColumnIndex("fieldid")));
				fBean.setFieldName(c.getString(c.getColumnIndex("fieldname")));
				fBean.setFieldType(c.getString(c.getColumnIndex("fieldtype")));
				fBean.setFiledvalue(c.getString(c.getColumnIndex("fieldvalue")));
				fieldTemplateList.add(fBean);
				Log.i("profile", "get field values" + fBean.getFiledvalue());
				c.moveToNext();
			}
			c.close();

		} catch (Exception e) {
			Log.i("off", "INSIDE OFFLINE error message" + e.getMessage());
			e.printStackTrace();

		}
		Log.i("profile", "==========>bean size " + fieldTemplateList.size());
		return fieldTemplateList;

	}

	@SuppressWarnings("finally")
	public int insertProfileGroup(ContentValues cv) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.insert("profilegroup", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i("thread", "came to insertProfileGroup" + e.toString());
		} finally {
			return row_id;
		}
	}

	@SuppressWarnings("finally")
	public int insertFieldTemplate(ContentValues cv) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.insert("fieldtemplate", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			return row_id;
		}
	}

	public Vector<FieldTemplateBean> getSearchByProfileFields() {
		Cursor cur;
		Vector<FieldTemplateBean> fieldList = new Vector<FieldTemplateBean>();
		try {
			if (!db.isOpen())
				openDatabase();

			String query = "Select `fieldtemplate`.`fieldid`,`fieldtemplate`.`fieldname`,`fieldtemplate`.`fieldtype`,(Select `fieldvalue` from `profilefieldvalues` where `userid`='')`fieldvalue` from `fieldtemplate` where `fieldtemplate`.`fieldtype` !='Multimedia' AND `fieldtemplate`.`fieldtype` !='Photo' AND `fieldtemplate`.`fieldtype`!='Audio' AND `fieldtemplate`.`fieldtype` !='Video' ORDER BY `fieldtemplate`.`fieldid`";
			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				FieldTemplateBean bean = new FieldTemplateBean();
				bean.setFieldId(cur.getString(0));
				bean.setFieldName(cur.getString(1));
				bean.setFieldType(cur.getString(2));
				fieldList.add(bean);
				cur.moveToNext();
			}
			cur.close();
			return fieldList;
		} catch (Exception e) {
			return null;

		}

	}

	@SuppressWarnings("finally")
	public int updateFieldTemplate(ContentValues cv, String condition) {

		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("fieldtemplate", cv, condition, null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	@SuppressWarnings("finally")
	public String getFieldType(String fieldid) {
		String f_type = null;
		Cursor cur = null;
		try {
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery(
					"select fieldtype from fieldtemplate where fieldid="
							+ fieldid, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				f_type = cur.getString(0);
				cur.moveToNext();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cur != null)
				cur.close();
			return f_type;
		}
	}

	public int getProfilePicFieldId(String fieldname) {
		Cursor cur = null;
		int fieldId = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery(
					"select fieldid from fieldtemplate where fieldname='"
							+ fieldname + "'", null);
			if (cur.moveToFirst()) {
				fieldId = cur.getInt(0);
			}
			cur.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return fieldId;
	}

	@SuppressWarnings("finally")
	public int insertProfileFieldValues(ContentValues cv) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			Log.i("profile123", "field id : " + cv.get("fieldid")
					+ "fieldvalue : " + cv.get("fieldvalue"));
			row_id = (int) db.insert("profilefieldvalues", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i("thread", "came to insertProfileFieldValues" + e.toString());
		} finally {
			return row_id;
		}
	}

	public boolean deleteProfile(String username) {
		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = null;
			if (username != null)
				strQuery = "DELETE from profilefieldvalues WHERE userid='"
						+ username + "'";
			db.execSQL(strQuery);
			delete = true;
		} catch (Exception e) {
			delete = false;
		}
		return delete;
	}

	@SuppressWarnings("finally")
	public ArrayList<String> getProfile(String userName) {
		ArrayList<String> column_names = new ArrayList<String>();
		Cursor ti = null;
		try {
			if (!db.isOpen())
				openDatabase();

			ti = db.rawQuery("SELECT * FROM profilefieldvalues where userid='"
					+ userName + "'", null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {

					column_names.add(ti.getString(1));

					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	@SuppressWarnings("finally")
	public int updateProfileFieldValues(ContentValues cv, String condition) {

		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("profilefieldvalues", cv, condition, null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i("thread", "came to updateprofilefieldvalues" + e.toString());
		} finally {
			return row_id;
		}
	}


	public String getModifiedDate(String query) {
		Cursor cur = null;
		String modifiedDate = "";
		try {
			if (db.isOpen())
				openDatabase();

			cur = db.rawQuery(query, null);
			cur.moveToFirst();

			while (!cur.isAfterLast()) {
				modifiedDate = cur.getString(0);
				cur.moveToNext();
			}

		} catch (Exception e) {
			Log.e("banudebug",
					this.getClass().getName() + " BLDatas " + e.getMessage());
			e.printStackTrace();

		}
		return modifiedDate;
	}

	public String getProfilePic(String userName) {
		String profilePic = "";
			try {
				if (db.isOpen())
					openDatabase();

				Cursor cur = null;
				try {
					String qry = "select photo from profiledetails where username='"+userName+"'";
					Log.i("sss","profile pic "+qry);
					cur = db.rawQuery(qry, null);
					cur.moveToFirst();

					while (cur.isAfterLast() == false) {
						profilePic = cur.getString(0);
						cur.moveToNext();
					}
					Log.i("sss","profile pic id "+profilePic);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return profilePic;

	}

	public ArrayList<String> getProfileUserName(Integer fieldId,
			String fieldName) {
		Cursor cur;
		ArrayList<String> profileUserNameList = new ArrayList<String>();
		try {
			if (!db.isOpen())
				openDatabase();

			String query = "Select userid from profilefieldvalues where fieldid="
					+ fieldId + " AND fieldvalue like '" + fieldName + "%'";
			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {

				profileUserNameList.add(cur.getString(0));
				cur.moveToNext();
			}
			cur.close();
			return profileUserNameList;
		} catch (Exception e) {

		}
		return null;
	}

	public HashMap<String, String> getMultimediaFieldValues(String query) {
		Cursor cur;
		HashMap<String, String> fieldValueMap = new HashMap<String, String>();
		try {
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				if (cur.getString(0) != null && cur.getString(0).length() > 0
						&& !cur.getString(0).equalsIgnoreCase("null")) {
					fieldValueMap.put(cur.getString(0), cur.getString(1));
				}

				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			Log.e("profile", "===> " + e.getMessage());
			e.printStackTrace();
		} finally {
			return fieldValueMap;
		}
	}

	@SuppressWarnings("finally")
	public boolean isRecordExists(String Querry) {

		Cursor cur = null;
		boolean status = false;
		try {
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery(Querry, null);
			cur.moveToFirst();

			if (cur.getCount() > 0)
				status = true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		finally {

			if (cur != null)
				cur.close();

			return status;
		}
	}

	public Vector<String> getMultimediaFields(String query) {
		Cursor cur;
		Vector<String> fieldValueList = new Vector<String>();
		try {
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				if (cur.getString(0) != null && cur.getString(0).length() > 0
						&& !cur.getString(0).equalsIgnoreCase("null")) {
					fieldValueList.add(cur.getString(0));
				}

				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			Log.e("profile", "===> " + e.getMessage());
			e.printStackTrace();
		} finally {
			return fieldValueList;
		}
	}

	public void deleteFieldValues(String qry) {
		try {
			if (db.isOpen())
				openDatabase();

			db.execSQL(qry);
		} catch (Exception e) {
			Log.e("clone", " deleteFieldValues== >" + e.getMessage());
		}
	}

	@SuppressWarnings("finally")
	public Vector<CompleteListBean> getCompleteListProperties(String strQuery) {
		Vector<CompleteListBean> alCompleteList = new Vector<CompleteListBean>();
		try {
			Log.d("thread", "..... came to getcompletelist properties"
					+ strQuery);

			Cursor cur = null;
			try {
				Log.d("cur",
						"database.............................." + db.isOpen());
				if (!db.isOpen())
					openDatabase();

				cur = db.rawQuery(strQuery, null);
				Log.d("cur",
						"cursor.............................." + cur.getCount());
				cur.moveToFirst();
				while (cur.isAfterLast() == false) {
					CompleteListBean clPro = new CompleteListBean();
					clPro.setComponentId(cur.getInt(0));
					clPro.setcomponentType(cur.getString(1));
					clPro.setContentPath(cur.getString(2));
					clPro.setFtpPath(cur.getString(3));
					clPro.setContentName(cur.getString(4));
					if (cur.getString(5) != null)
						clPro.setFromUser(cur.getString(5));

					clPro.setContent(cur.getString(6));
					clPro.setOwner(cur.getString(8));
					if (cur.getString(9) != null)
						clPro.setVanishMode(cur.getString(9));

					clPro.setVanishValue(cur.getString(10));
					clPro.setDateAndTime(cur.getString(11));
					if (cur.getString(12) != null)
						clPro.setReminderTime(cur.getString(12));
					clPro.setViewMode(cur.getInt(13));
					clPro.setReminderZone(cur.getString(11));
					clPro.setResponseType(cur.getString(15));
					if (clPro.getcomponentType().equals("note")) {
						if (clPro.getContentName().trim().length() >= 12) {
							clPro.setContentName(clPro.getContentName()
									.substring(0, 9));
						} else {
							clPro.setContentName(clPro.getContentName()
									.substring(
											0,
											clPro.getContentName().trim()
													.length()));
						}
					}
					clPro.setBstype(cur.getString(16));
					clPro.setBsstatus(cur.getString(17));
					clPro.setDirection(cur.getString(18));
					clPro.setParent_id(cur.getString(19));

					alCompleteList.add(clPro);
					cur.moveToNext();
				}
				// Collections.reverse(alCompleteList);
			} catch (Exception e) {
				Log.e("thread", "getVTabProperties:" + e.getMessage());
				e.printStackTrace();
			} finally {
				if (cur != null) {
					cur.close();
				}

				return alCompleteList;

			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("files", "Exception :: " + e.getMessage());
			e.printStackTrace();

		} finally {
			return alCompleteList;
		}

	}

	public boolean ExecuteQuery(String strQuery) {
		boolean checkExceute = false;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			db.execSQL(strQuery);

			checkExceute = true;

		} catch (Exception e) {
			checkExceute = false;
			e.printStackTrace();

		} finally {

		}
		return checkExceute;
	}

	public Vector<Components> getshareComponent(String query) {
		Cursor cur = null;
		Vector<Components> shareCompList = new Vector<Components>();
		try {

			if (db.isOpen()) {

			} else {

				openDatabase();
			}

			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				Components cmptPro = new Components();
				cmptPro.setComponentId(cur.getInt(0));
				cmptPro.setContentPath(cur.getString(1));
				cmptPro.setcomponentType(cur.getString(2));
				cmptPro.setfromuser(cur.getString(3));
				cmptPro.settoUser(cur.getString(4));
				cmptPro.setSharestatus(cur.getString(5));
				cmptPro.setFtpPath(cur.getString(6));
				shareCompList.add(cmptPro);
				cur.moveToNext();
			}
			return shareCompList;
		} catch (Exception e) {
			Log.e("db", "getComponent:" + e.getMessage());
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}

		}

	}

	@SuppressWarnings("finally")
	public int insertComponent(ContentValues cv) {
		int row_id = 0;
		cv.put("username", CallDispatcher.LoginUser);
		try {
			Log.i("clone", "came to insertRecords insertComponent.......");

			if (!db.isOpen()) {
				openDatabase();
			}
			row_id = (int) db.insert("component", null, cv);
			Log.d("component", "Row inserted id--->" + row_id);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i("clone", "came to exception in insertRecords insertComponent"
					+ e.toString());
		} finally {
			return row_id;
		}
	}

	@SuppressWarnings("finally")
	public int insertShareComponent(ContentValues cv) {
		int row_id = 0;
		try {
			Log.i("clone", "came to insertRecords insertShareComponent.......");

			if (!db.isOpen()) {
				openDatabase();
			}
			row_id = (int) db.insert("sharecomponent", null, cv);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			return row_id;
		}
	}

	public int getMaxIdToSet() {

		int noSetId = 0;

		Cursor cur = null;

		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			cur = db.rawQuery("SELECT MAX(componentId) FROM component ", null);
			cur.moveToFirst();
			noSetId = cur.getInt(0);

		} catch (Exception e) {
			Log.e("thread",
					"getValueFromQuery maxxxx: exception" + e.getMessage());
		} finally {
			if (cur != null) {
				cur.close();
			}

		}
		return noSetId;

	}

	public Components getComponent(String strQuery) {
		Components cmptPro = null;
		Cursor cur = null;
		try {
			if (db.isOpen()) {
			} else {
				openDatabase();
			}
			cur = db.rawQuery(strQuery, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				cmptPro = new Components();
				cmptPro.setComponentId(cur.getInt(0));
				cmptPro.setcomponentType(cur.getString(1));
				cmptPro.setContentPath(cur.getString(2));
				cmptPro.setContentName(cur.getString(4));
				cmptPro.setVanishMode(cur.getString(8));
				cmptPro.setVanishValue(cur.getString(9));
				cmptPro.setDateAndTime(cur.getString(10));
				cmptPro.setRemDateAndTime(cur.getString(11));
				cmptPro.setViewMode(cur.getInt(13));
				cmptPro.setfromuser(cur.getString(5));
				cmptPro.setComment(cur.getString(6));
				cur.moveToNext();
			}
			return cmptPro;
		} catch (Exception e) {
			Log.e("db", "getComponent:" + e.getMessage());
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}

		}

	}

	public Vector<ReminderDetail> getTTL() {
		Vector<ReminderDetail> ttlList = new Vector<ReminderDetail>();
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String strGetQry = "select componentid, vanishmode,vanishvalue from component";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("name", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				ReminderDetail response = new ReminderDetail();
				response.setCompId(cur.getInt(0));
				response.setVanishMode(cur.getString(1));
				response.setVanishValue(cur.getString(2));
				ttlList.add(response);
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ttlList;

	}

	public ReminderRetrieve getReminderDetail(String strTime) {

		try {
			ReminderRetrieve remData = new ReminderRetrieve();

			ArrayList<Integer> ALReminderDetail = new ArrayList<Integer>();

			Cursor cur = null;
			try {

				if (db.isOpen()) {

				} else {
					openDatabase();
				}

				boolean isResponseType = false;
				String strGetQry = "select * from component where reminderdateandtime ='"
						+ strTime + "' and reminderstatus=1";

				cur = db.rawQuery(strGetQry, null);
				cur.moveToFirst();
				while (cur.isAfterLast() == false) {
					ReminderDetail response = new ReminderDetail();
					int id = cur.getInt(0);
					response.setComponentID(id);
					response.setRemType(cur.getString(1));
					response.setRemContent(cur.getString(4));
					response.setRemPath(cur.getString(2));

					String str = cur.getString(15);

					if (str != null) {
						if (str.trim().length() != 0) {
							isResponseType = true;
						}
					}

					if (response.getRemType().equalsIgnoreCase("video")) {
						if (response.getRemPath().startsWith("ftp")) {
							ALReminderDetail.add(id);
						} else {
							String filename = response.getRemPath();
							if (!filename.contains(".mp4"))
								filename = filename + ".mp4";
							File vfile = new File(filename);
							if (vfile.exists()) {
								if (vfile.length() != 0) {
									ALReminderDetail.add(id);
								}
							}
						}
					} else {
						File vfile = new File(response.getRemPath());
						if (vfile.exists()) {
							if (vfile.length() != 0) {
								ALReminderDetail.add(id);
							}
						}
					}

					cur.moveToNext();
				}

				remData.setResponseType(isResponseType);
				remData.setReminderList(ALReminderDetail);

				if (ALReminderDetail.size() != 0) {

					for (int i = 0; i < ALReminderDetail.size(); i++) {
						String qryy = "update component set reminderstatus=2 where componentid ="
								+ ALReminderDetail.get(i);
						ExecuteQuery(qryy);
					}

				}
			} catch (Exception e) {
				Log.e("db", "get Reminder Data Exception " + e.getMessage());
			} finally {
				if (cur != null) {
					cur.close();
				}

			}

			return remData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@SuppressWarnings("finally")
	public int updateComponent(ContentValues cv, String condition) {

		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("component", cv, condition, null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i("thread", "came to update component" + e.toString());
		} finally {
			return row_id;
		}
	}

	@SuppressWarnings("finally")
	public long insertUtility(ContentValues cv) {
		long row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			row_id = db.insert("utility", null, cv);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return row_id;
		}
	}

	@SuppressWarnings("finally")
	public long UpdateUtility(ContentValues cv, String condition) {
		long row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			row_id = db.update("utility", cv, condition, null);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return row_id;
		}
	}

	@SuppressWarnings("finally")
	public long UpdatesyncUtility(ContentValues cv, int id) {
		long row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			row_id = db.update("utility", cv, "id='" + id + "'", null);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return row_id;
		}
	}

	@SuppressWarnings("finally")
	public long deleteUtility(String rec_id) {
		long row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			row_id = db.delete("utility", "id=" + rec_id, null);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return row_id;
		}
	}

	@SuppressWarnings("finally")
	public Vector<UserBean> Selectautpacceptcalls(String uname) {
		Cursor cur;
		Vector<UserBean> usersList = new Vector<UserBean>();
		try {
			if (!db.isOpen())
				openDatabase();
			String query = "select * from autoacceptcalls where owner='"
					+ uname + "'";

			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				String buddyName = cur.getString(1);
				for (BuddyInformationBean bBean : ContactsFragment
						.getBuddyList()) {
					if (!bBean.isTitle()) {
						if (!bBean.getStatus().equalsIgnoreCase("pending")
								&& !bBean.getStatus().equalsIgnoreCase("new")) {
							if (bBean.getName().equalsIgnoreCase(buddyName)) {
								UserBean bean = new UserBean();
								bean.setBuddyName(cur.getString(1));
								bean.setOwnerName(cur.getString(0));
								bean.setFlag(cur.getString(2));
								usersList.add(bean);
							}
						}
					}
				}
				cur.moveToNext();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return usersList;
		}
	}

	@SuppressWarnings("finally")
	public Vector<UtilityBean> SelectUtilityRecords(String query) {
		Cursor cur;
		Vector<UtilityBean> utility_list = new Vector<UtilityBean>();
		try {
			if (!db.isOpen())
				openDatabase();
			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				UtilityBean bean = new UtilityBean();
				bean.setId(cur.getInt(0));
				bean.setUsername(cur.getString(1));
				bean.setNameororg(cur.getString(2));
				bean.setProduct_name(cur.getString(3));
				bean.setQty(cur.getString(4));
				bean.setPrice(cur.getString(5));
				bean.setVideofilename(cur.getString(6));
				bean.setImag_filename(cur.getString(7));
				bean.setAudiofilename(cur.getString(8));
				bean.setLocation(cur.getString(9));
				bean.setAddress(cur.getString(10));
				bean.setCountry(cur.getString(11));
				bean.setState(cur.getString(12));
				bean.setCityordist(cur.getString(13));
				bean.setPin(cur.getString(14));
				bean.setEmail(cur.getString(15));
				bean.setC_no(cur.getString(16));
				bean.setMode(cur.getInt(17));
				bean.setUtility_name(cur.getString(18));
				bean.setPosted_date(cur.getString(19));
				bean.setExistingPostedDate(cur.getString(19));
				utility_list.add(bean);
				cur.moveToNext();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return utility_list;
		}
	}

	@SuppressWarnings("finally")
	public PermissionBean selectPermissions(String select_qry, String user_id,
			String buddy_id) {
		PermissionBean permissionBean = new PermissionBean();
		Cursor cur;
		try {
			if (!db.isOpen())
				openDatabase();
			cur = db.rawQuery(select_qry, null);
			if (cur.getCount() > 0) {
				cur.moveToFirst();
				while (!cur.isAfterLast()) {
					permissionBean.setUserId(cur.getString(1));
					permissionBean.setBuddyId(cur.getString(2));
					permissionBean
							.setAudio_call(Integer.toString(cur.getInt(3)));
					permissionBean
							.setVideo_call(Integer.toString(cur.getInt(4)));
					permissionBean.setABC(Integer.toString(cur.getInt(5)));
					permissionBean.setVBC(Integer.toString(cur.getInt(6)));
					permissionBean.setAUC(Integer.toString(cur.getInt(7)));
					permissionBean.setVUC(Integer.toString(cur.getInt(8)));
					permissionBean.setMMchat(Integer.toString(cur.getInt(9)));
					permissionBean.setTextMessage(Integer.toString(cur
							.getInt(10)));
					permissionBean.setVideoMessage(Integer.toString(cur
							.getInt(11)));
					permissionBean.setPhotoMessage(Integer.toString(cur
							.getInt(12)));
					permissionBean.setAudioMessage(Integer.toString(cur
							.getInt(13)));
					permissionBean.setSketchMessage(Integer.toString(cur
							.getInt(14)));
					permissionBean.setShareProfile(Integer.toString(cur
							.getInt(15)));
					permissionBean.setViewProfile(Integer.toString(cur
							.getInt(16)));
					permissionBean.setAvtaar(Integer.toString(cur.getInt(17)));
					permissionBean
							.setFormshare(Integer.toString(cur.getInt(18)));
					permissionBean.setHostedconf(Integer.toString(cur
							.getInt(19)));
					cur.moveToNext();
				}
			} else {
				permissionBean.setUserId(user_id);
				permissionBean.setBuddyId(buddy_id);
				permissionBean.setAudio_call("1");
				permissionBean.setVideo_call("1");
				permissionBean.setABC("1");
				permissionBean.setVBC("1");
				permissionBean.setAUC("1");
				permissionBean.setVUC("1");
				permissionBean.setMMchat("1");
				permissionBean.setTextMessage("1");
				permissionBean.setVideoMessage("1");
				permissionBean.setPhotoMessage("1");
				permissionBean.setAudioMessage("1");
				permissionBean.setSketchMessage("1");
				permissionBean.setShareProfile("1");
				permissionBean.setViewProfile("1");
				permissionBean.setAvtaar("1");
				permissionBean.setFormshare("1");
				permissionBean.setHostedconf("1");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return permissionBean;
		}
	}

	public GroupChatPermissionBean selectPermissionsForGroup(String select_qry,
			String userId, String groupId, String owner, String member) {
		GroupChatPermissionBean gPermissionBean = new GroupChatPermissionBean();
		Cursor cur;
		try {
			if (!db.isOpen())
				openDatabase();
			cur = db.rawQuery(select_qry, null);
			if (cur.getCount() > 0) {
				cur.moveToFirst();
				while (!cur.isAfterLast()) {
					gPermissionBean.setUserId(cur.getString(1));
					gPermissionBean.setGroupId(cur.getString(2));
					gPermissionBean.setGroupOwner(cur.getString(3));
					gPermissionBean.setGroupMember(cur.getString(4));
					gPermissionBean.setAudioConference(Integer.toString(cur
							.getInt(5)));
					gPermissionBean.setVideoConference(Integer.toString(cur
							.getInt(6)));
					gPermissionBean.setAudioBroadcast(Integer.toString(cur
							.getInt(7)));
					gPermissionBean.setVideoBroadcast(Integer.toString(cur
							.getInt(8)));
					gPermissionBean.setTextMessage(Integer.toString(cur
							.getInt(9)));
					gPermissionBean.setPhotoMessage(Integer.toString(cur
							.getInt(11)));
					gPermissionBean.setAudioMessage(Integer.toString(cur
							.getInt(12)));
					gPermissionBean.setVideoMessage(Integer.toString(cur
							.getInt(10)));
					gPermissionBean.setPrivateMessage(Integer.toString(cur
							.getInt(13)));
					gPermissionBean.setReplyBackMessage(Integer.toString(cur
							.getInt(14)));
					gPermissionBean.setScheduleMessage(Integer.toString(cur
							.getInt(15)));
					gPermissionBean.setDeadLineMessage(Integer.toString(cur
							.getInt(16)));
					gPermissionBean.setWithDrawn(Integer.toString(cur
							.getInt(17)));
					gPermissionBean.setChat(Integer.toString(cur
							.getInt(18)));
					cur.moveToNext();
				}
			} else {
				gPermissionBean.setUserId(userId);
				gPermissionBean.setGroupId(groupId);
				gPermissionBean.setGroupOwner(owner);
				gPermissionBean.setGroupMember(member);
				gPermissionBean.setAudioConference("1");
				gPermissionBean.setVideoConference("1");
				gPermissionBean.setAudioBroadcast("1");
				gPermissionBean.setVideoBroadcast("1");
				gPermissionBean.setTextMessage("1");
				gPermissionBean.setPhotoMessage("1");
				gPermissionBean.setAudioMessage("1");
				gPermissionBean.setVideoMessage("1");
				gPermissionBean.setPrivateMessage("1");
				gPermissionBean.setReplyBackMessage("1");
				gPermissionBean.setScheduleMessage("1");
				gPermissionBean.setDeadLineMessage("1");
				gPermissionBean.setWithDrawn("1");
				gPermissionBean.setChat("1");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return gPermissionBean;
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<String[]> getRecordsofSettingtbl(String tblname) {
		ArrayList<String[]> column_recs = new ArrayList<String[]>();
		Cursor ti = null;
		try {
			if (!db.isOpen()) {
				openDatabase();
			}
			ti = db.rawQuery("SELECT * FROM formsettings where formid="
					+ tblname, null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					String[] recs = new String[ti.getColumnCount()];
					for (int i = 0; i < ti.getColumnCount(); i++) {

						recs[i] = ti.getString(i);

					}
					System.out.println("col  table id====: " + ti.getString(1));
					column_recs.add(recs);
					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_recs;
		}

	}

	@SuppressWarnings("finally")
	public ArrayList<EditFormBean> getFormInfoDetails(String tblname,
			String columnName) {
		ArrayList<EditFormBean> editFormList = new ArrayList<EditFormBean>();
		Cursor cur = null;
		try {
			if (!db.isOpen()) {
				openDatabase();
			}

			cur = db.rawQuery("SELECT * FROM forminfo WHERE tablename='" + "["
					+ tblname + "]' and column='" + "[" + columnName + "]'",
					null);
			if (cur.moveToFirst()) {
				while (!cur.isAfterLast()) {
					EditFormBean editFormBean = new EditFormBean();
					editFormBean.setId(cur.getString(0));
					editFormBean.setTablename(cur.getString(1));
					String col = cur.getString(2).replace("[", "")
							.replace("]", "").trim().toLowerCase();
					if (col.contains("blob_")) {
						col.toLowerCase().replace("blob_", "").trim();
					}
					editFormBean.setColumnname(col);
					editFormBean.setEntrymode(cur.getString(3));
					editFormBean.setValidata(cur.getString(4));
					editFormBean.setDefaultvalue(cur.getString(5));
					editFormBean.setInstruction(cur.getString(6));
					editFormBean.setErrortip(cur.getString(7));
					editFormBean.setAttributeid(cur.getString(8));
					editFormList.add(editFormBean);
					cur.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			return editFormList;
		}

	}

	public EditFormBean getFormInfoDetailsById(String tblname,
			String attributeid) {
		EditFormBean editFormBean = new EditFormBean();
		Cursor cur = null;
		try {
			if (!db.isOpen()) {
				openDatabase();
			}

			cur = db.rawQuery(
					"SELECT * FROM forminfo WHERE tablename='" + "[" + tblname
							+ "]" + "' and attributeid='" + attributeid + "'",
					null);
			if (cur.moveToFirst()) {
				while (!cur.isAfterLast()) {
					editFormBean.setId(cur.getString(0));
					editFormBean.setTablename(cur.getString(1));
					editFormBean.setColumnname(cur.getString(2)
							.replace("[", "").replace("]", "").trim());
					editFormBean.setEntrymode(cur.getString(3));
					editFormBean.setValidata(cur.getString(4));
					editFormBean.setDefaultvalue(cur.getString(5));
					editFormBean.setInstruction(cur.getString(6));
					editFormBean.setErrortip(cur.getString(7));
					editFormBean.setAttributeid(cur.getString(8));
					cur.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			return editFormBean;
		}

	}

	public boolean updates(String formId, ContentValues cv, String buddy) {
		boolean result = false;
		try {
			if (!db.isOpen())
				openDatabase();
			long i = db.update("formsettings", cv, "settingid=" + formId, null);

			Log.i("thread", "Row id......." + i);
			if (i != -1)
				result = true;
			else
				result = false;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Log.i("settings", "result" + result);
		return result;
	}

	@SuppressWarnings("finally")
	public HashMap<String, String> getColumnTypes(String tblname) {
		HashMap<String, String> column_names = new HashMap<String, String>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("PRAGMA table_info(" + "[" + tblname + "]" + ")",
					null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					Log.i("thread ", "Col :" + ti.getString(1));
					Log.i("thread ", "Col type:" + ti.getString(2));

					if ((!ti.getString(1).equalsIgnoreCase("id"))
							&& (!ti.getString(1).equalsIgnoreCase("tableid"))) {
						Log.i("test ", "Col :" + ti.getString(1));
						Log.i("test ", "Col type:" + ti.getString(2));
						column_names.put(ti.getString(1), ti.getString(2));

					}
					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	@SuppressWarnings({ "finally" })
	public String getFormName(String id) {

		Log.i("thread", "tbl name............." + id);
		Cursor ti = null;
		String name = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery(
					"SELECT tablename FROM formslookup WHERE tableid='" + id
							+ "'", null);
			ti.moveToFirst();
			Log.i("thread", "@@@@@@@@@@@ table exists count" + ti.getCount());
			if (ti.getCount() > 0) {
				Log.i("thread", "@@@@@@@@@@@ table exists");
				name = ti.getString(0);
				Log.i("thread", "@@@@@@@@@@@ table exists" + name);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			if (ti != null) {
				ti.close();
			}
			return name;
		}

	}

	@SuppressWarnings("finally")
	public ArrayList<formItem> LookUpRecords(Bitmap image, String username) {
		ArrayList<formItem> own_records = new ArrayList<formItem>();
		Cursor cur = null;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			cur = db.rawQuery(
					"SELECT tablename,owner,tableid,rowcount FROM formslookup where owner='"
							+ username + "'", null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				formItem flist_bean = new formItem();

				flist_bean.setTitle(cur.getString(0));
				flist_bean.setId(cur.getString(2));
				flist_bean.setAction(cur.getString(3));
				flist_bean.setImage(image);
				flist_bean.setOwner(cur.getString(1));

				own_records.add(flist_bean);
				cur.moveToNext();

			}
			cur.close();

			ArrayList<String> buddies = new ArrayList<String>();

			Vector<BuddyInformationBean> data = ContactsFragment.getBuddyList();

			for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
				if (!bib.isTitle()) {
					if (!bib.getStatus().equalsIgnoreCase("Pending")) {
						if (!bib.getStatus().equalsIgnoreCase("New")) {
							buddies.add(bib.getName());

						}
					}
				}
			}

			for (String formsListBean : buddies) {

				// for (String formsListBean :
				// WebServiceReferences.buddies_forms) {

				cur = db.rawQuery(
						"SELECT tablename,owner,tableid,rowcount FROM formslookup where owner='"
								+ formsListBean + "'", null);
				cur.moveToFirst();
				while (cur.isAfterLast() == false) {
					formItem flist_bean = new formItem();

					flist_bean.setTitle(cur.getString(0));
					flist_bean.setId(cur.getString(2));
					flist_bean.setAction(cur.getString(3));
					flist_bean.setImage(image);
					flist_bean.setOwner(cur.getString(1));
					own_records.add(flist_bean);
					cur.moveToNext();
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			return own_records;
		}
	}

	@SuppressWarnings("finally")
	public String[] getAccessRights(String formId, String buddyId) {

		Cursor ti = null;
		String[] recs = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery(
					"SELECT accesscode,synccode,formowenerid FROM formsettings where formid='"
							+ formId + "' and buddyid='" + buddyId + "'", null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					recs = new String[ti.getColumnCount()];
					for (int i = 0; i < ti.getColumnCount(); i++) {

						recs[i] = ti.getString(i);

						Log.i("settings", "**********" + recs[i]);
					}
					System.out.println("col  table id====: " + ti.getString(1));

					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}

			return recs;
		}

	}

	@SuppressWarnings("finally")
	public boolean createFormTable(ArrayList<String> fields, String tbl_name,
			HashMap<String, String> dtype) {
		Log.i("new", "values---->" + dtype.values());
		Log.i("new", "values---->" + dtype.keySet().toString());
		for (int i = 0; i < fields.size(); i++) {
			Log.i("new", "fielsss  values---->" + fields.get(i));

		}
		boolean result = false;
		try {

			String qrystring = "Id INTEGER PRIMARY KEY AUTOINCREMENT,[tableid]";
			for (int i = 0; i < fields.size(); i++) {
				Log.i("new", "Tbl field......." + fields.get(i));
				Log.i("thread", "Tbl field......." + dtype.get(fields.get(i)));
				String columnane = fields.get(i).replace("[", "")
						.replace("]", "");

				qrystring = qrystring + "," + fields.get(i) + " "
						+ dtype.get(columnane).trim();

			}
			String qry = "create table if not exists " + tbl_name + "("
					+ qrystring + ")";

			if (ExecuteQuery(qry)) {
				result = true;

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return result;
		}

	}
//public boolean getBoolean()
//{
//	return true;
//}
	public long insertAttribute(FormInfoBean fIBean) {
		long i = -1;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("tablename", fIBean.getFormName());
			cv.put("column", fIBean.getColumn());
			cv.put("entrymode", fIBean.getEntryMode());
			cv.put("validdata", fIBean.getValidData());
			cv.put("defaultvalue", fIBean.getDefaultValue());
			cv.put("instruction", fIBean.getInstruction());
			cv.put("errortip", fIBean.getErrorTip());
			cv.put("attributeid", fIBean.getAttributeId());
			i = db.insert("forminfo", null, cv);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	public boolean updatesAttribute(FormInfoBean fIBean) {

		boolean result = false;
		try {

			String qry = "update forminfo set attributeid='"
					+ fIBean.getAttributeId() + "' where column='"
					+ fIBean.getColumn() + "'";
			Log.i("formfield123", "Query : " + qry);
			if (ExecuteQuery(qry)) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("settings", "result" + result);
		return result;
	}

	@SuppressWarnings("finally")
	public ArrayList<String[]> getColumnofSettingtbl(String tblname) {
		ArrayList<String[]> column_recs = new ArrayList<String[]>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("SELECT * FROM formsettings where formid="
					+ tblname, null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					String[] recs = new String[ti.getColumnCount()];
					for (int i = 0; i < ti.getColumnCount(); i++) {

						recs[i] = ti.getColumnName(i);

						Log.i("settings", "**********" + recs[i]);
					}
					System.out.println("col  table id====: " + ti.getString(1));
					column_recs.add(recs);
					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_recs;
		}

	}

	public int getreocrdcountUDP(String formid, String fsid, String owner,
			String buddy) {
		String[] result = null;
		int count = 0;

		String id = formid;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT Count(*) FROM formsettings where formid=" + id
					+ " and settingid=" + fsid);
			count = getContentCount("SELECT Count(*) FROM formsettings where formid="
					+ id + " and settingid=" + fsid);

			Cursor c = db.rawQuery(sb.toString(), null);
			c.moveToFirst();

			result = new String[c.getCount()];
			int i = 0;
			while (c.moveToNext()) {
				Log.i("settings", "inside while");

				result[i] = c.getString(c.getColumnIndex("formid"));

				i++;
			}
			c.close();
		} catch (Exception e) {
		}
		return count;
	}

	private int getContentCount(String strQuery) {
		int count = 0;
		Cursor cur = null;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			cur = db.rawQuery(strQuery, null);

			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				count = cur.getInt(0);
				cur.moveToNext();
			}

		} catch (Exception e) {
			Log.e("db", "getCurrentMonthDetails " + e.getMessage());
		} finally {
			if (cur != null) {
				cur.close();
			}

		}
		return count;
	}

	@SuppressWarnings("finally")
	public HashMap<String, String> getColumnTypesforminfo(String tblname) {
		Log.i("IMP", "	inside column types from info FOR LOOP");

		HashMap<String, String> column_names = new HashMap<String, String>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("PRAGMA table_info(" + "forminfo" + ")", null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					Log.i("thread ", "Col :" + ti.getString(1));
					Log.i("thread ", "Col type:" + ti.getString(2));
					Log.i("IMP", "	inside cursorrrrrrrr");

					Log.i("test ", "Col :" + ti.getString(1));
					Log.i("IMP", "	columnnnnn:" + ti.getString(1));

					Log.i("test ", "Col type:" + ti.getString(2));
					Log.i("IMP", "	columnnnnn typeee:" + ti.getString(2));

					column_names.put(ti.getString(1), ti.getString(2));

					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("IMP", "	THIS IS ERROR--->" + e.getMessage());

			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	@SuppressWarnings("finally")
	public String[] getRecordsofforminfotable(String tablename, String colname,
			HashMap<String, String> types) {
		Log.i("IMP", "INSIDE FORMINFO RECORDS GET METHOD IN DB ACCESS "
				+ colname + tablename);
		String[] recs = null;
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String tblName = "[" + tablename + "]";
			String colName = "[" + colname + "]";
			ti = db.rawQuery("SELECT * FROM forminfo WHERE tablename='"
					+ tblName + "' and column='" + colName + "'", null);

			Log.i("forms", "====> " + ti.getCount());
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					recs = new String[ti.getColumnCount()];
					for (int i = 0; i < ti.getColumnCount(); i++) {
						System.out.println("col: " + ti.getColumnName(i));
						String tp = types.get(ti.getColumnName(i));
						if (tp.contains("2"))
							recs[i] = ti.getString(i);
						else if (tp.equalsIgnoreCase("INTEGER"))

							recs[i] = Integer.toString(ti.getInt(i));
						else if (tp.equalsIgnoreCase("BLOB"))
							recs[i] = Base64.encodeToString(ti.getBlob(i),
									Base64.DEFAULT);

						Log.i("IMP", "col  table id====0000: " + tp);
						Log.i("IMP",
								"col  table id====1111: " + ti.getString(i));
						Log.i("IMP", "col  table id====222: " + recs[i]);

					}

					System.out.println("col  table id====: " + ti.getString(1));

					Log.i("info", "col  table id====: " + ti.getString(1));

					for (int i = 0; i < recs.length; i++) {
						Log.i("info", "col  table id====: " + recs[i]);

					}

					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i("IMP", "ERRORRRRRRR----> " + e.getMessage());

		} finally {
			if (ti != null) {
				ti.close();
			}
			return recs;
		}

	}

	@SuppressWarnings("finally")
	public ArrayList<String[]> isQueryContainResults(String qry, String owner) {
		ArrayList<String[]> column_names = new ArrayList<String[]>();

		Cursor ti = null;
		try {
			Log.i("name", "query" + qry);
			String[] split_qry = qry.split("\\s+");
			String[] val = null;

			for (int i = 0; i < split_qry.length; i++) {
				Log.i("QB",
						"Splitted table name isQueryContainResultss ......."
								+ split_qry[i]);

			}
			if (split_qry.length >= 4) {
				Log.i("thread",
						" Inside isquery contain results Splitted table name isQueryContainResultss ......."
								+ split_qry[3]);

				String result_qry = "";
				for (int i = 0; i < split_qry.length; i++) {
					if (result_qry.trim().length() == 0)
						result_qry = split_qry[i];
					else
						result_qry = result_qry + " " + split_qry[i];
				}
				Log.i("thread",
						"Splitted table name isQueryContainResultss ......."
								+ qry);

				if (!db.isOpen())
					openDatabase();
				ti = db.rawQuery(qry, null);

				ti.moveToFirst();
				Log.i("thread",
						"@@@@@@@@@@@ table exists count" + ti.getCount());

				if (ti.getCount() > 0) {
					if (ti.moveToFirst()) {

						Log.i("ne",
								"@@@@@@@@@@@ column exists count"
										+ ti.getColumnCount());

						while (ti.isAfterLast() == false) {
							val = new String[ti.getColumnCount()];

							for (int i = 0; i < ti.getColumnCount(); i++) {
								Log.i("thread",
										"@@@@@@@@@@@ table exists)))))))))))))");
								Log.i("ne",
										"@@@@@@@@@@@ table exists)))))))))))))");
								if (ti.isNull(i)) {
									val[i] = "";

								} else {
									val[i] = ti.getString(i);

								}
								Log.i("thread", "@@@@@@@@@@@ values" + val[i]);
								Log.i("ne", "@@@@@@@@@@@ values" + val[i]);

							}

							column_names.add(val);
							ti.moveToNext();

						}

					}

				}

			} else {

				Log.i("thread", " else looooooop");

			}
		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}

	}

	public boolean insertForRecords(String formId, ContentValues cv) {

		Log.i("new", "aaaaaaaaaaaaaa" + cv.toString());
		String formName = "[" + formId + "]";
		boolean result = false;
		try {
			if (!db.isOpen())
				openDatabase();

			long i = db.insert(formName, null, cv);
			Log.i("test", "Row id......." + i);
			if (i != -1)
				result = true;
			else
				result = false;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	public boolean update(String formId, ContentValues cv, String recordid) {
		boolean result = false;
		try {
			if (!db.isOpen())
				openDatabase();

			Log.i("formbug"," cv.get([status]) "+cv.get("[status]")+" cv.get([text])"+cv.get("[text]"));
			ContentValues cv1=new ContentValues();
//			cv1.put("text", "apple");
			long i = db.update(formId, cv, "tableid='" +recordid+"'", null);
//			long i = db.update(formId, cv, "Id=" + recordid, null);
			Log.i("thread", "Row id......." + i);
			Log.i("formbug", "update db ==> formId"+formId+" recordid :"+recordid+" Row id. :" + i);

			if (i != -1)
				result = true;
			else
				result = false;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("finally")
	public String getRecordCount(String tbl_name) {
		long count = 0;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String Query = "SELECT COUNT(*) FROM " + tbl_name;
			SQLiteStatement statement = db.compileStatement(Query);
			count = statement.simpleQueryForLong();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			return Long.toString(count);
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<org.lib.model.FormsListBean> ownLookUpRecordsForSearch(
			String username, String searchtext) {

		searchtext = searchtext.toLowerCase();
		ArrayList<org.lib.model.FormsListBean> own_records = new ArrayList<org.lib.model.FormsListBean>();
		Cursor cur = null;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			cur = db.rawQuery(
					"SELECT tablename,owner,tableid,rowcount,Formiconname FROM formslookup where owner='"
							+ username + "' order by tablename", null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				org.lib.model.FormsListBean flist_bean = new org.lib.model.FormsListBean();
				flist_bean.setForm_name(cur.getString(0));
				flist_bean.setForm_ownser(cur.getString(1));
				flist_bean.setForm_id(cur.getString(2));
				if (cur.getString(3).trim().length() == 0) {
					flist_bean.setnumberof_rows(cur.getString(3));
				} else {
					flist_bean.setnumberof_rows(cur.getString(3));
				}

				if (cur.isNull(cur.getColumnIndex("Formiconname"))) {
					flist_bean.setFormicon("");

				}

				else {
					flist_bean.setFormicon(cur.getString(4));

				}

				if (flist_bean.getForm_name().toLowerCase()
						.startsWith(searchtext)
						|| flist_bean.getForm_name().toLowerCase()
								.equalsIgnoreCase(searchtext)) {
					own_records.add(flist_bean);

				}

				cur.moveToNext();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			return own_records;
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<org.lib.model.FormsListBean> BuddiesLookUpRecordsForSearch(
			String searchtext) {

		searchtext = searchtext.toLowerCase();
		ArrayList<org.lib.model.FormsListBean> buddy_records = new ArrayList<org.lib.model.FormsListBean>();
		Cursor cur = null;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			for (String formsListBean : WebServiceReferences.buddies_forms) {

				cur = db.rawQuery(
						"SELECT tablename,owner,tableid,rowcount,Formiconname FROM formslookup where owner='"
								+ formsListBean + "' order by tablename", null);
				cur.moveToFirst();
				while (cur.isAfterLast() == false) {

					org.lib.model.FormsListBean flist_bean = new org.lib.model.FormsListBean();

					flist_bean.setForm_name(cur.getString(0));
					flist_bean.setForm_ownser(cur.getString(1));
					flist_bean.setForm_id(cur.getString(2));

					if (cur.getString(3).trim().length() == 0) {
						flist_bean.setnumberof_rows(cur.getString(3));
					} else {
						flist_bean.setnumberof_rows(cur.getString(3));
					}
					if (cur.isNull(cur.getColumnIndex("Formiconname"))) {
						flist_bean.setFormicon("");

					} else {

						flist_bean.setFormicon(cur.getString(4));

					}

					if (flist_bean.getForm_name().toLowerCase()
							.startsWith(searchtext)
							|| flist_bean.getForm_name().toLowerCase()
									.equalsIgnoreCase(searchtext)) {
						buddy_records.add(flist_bean);

					}
					cur.moveToNext();

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			return buddy_records;
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<org.lib.model.FormsListBean> ownLookUpRecordss(
			String username) {
		ArrayList<org.lib.model.FormsListBean> own_records = new ArrayList<org.lib.model.FormsListBean>();
		Cursor cur = null;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			cur = db.rawQuery(
					"SELECT tablename,owner,tableid,rowcount,Formiconname FROM formslookup where owner='"
							+ username + "' order by tablename", null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				org.lib.model.FormsListBean flist_bean = new org.lib.model.FormsListBean();
				flist_bean.setForm_name(cur.getString(0));
				flist_bean.setForm_ownser(cur.getString(1));
				flist_bean.setForm_id(cur.getString(2));
				if (cur.getString(3).trim().length() == 0) {
					flist_bean.setnumberof_rows(cur.getString(3));
				} else {
					flist_bean.setnumberof_rows(cur.getString(3));
				}

				if (cur.isNull(cur.getColumnIndex("Formiconname"))) {
					flist_bean.setFormicon("");

				}

				else {
					flist_bean.setFormicon(cur.getString(4));

				}
				own_records.add(flist_bean);
				cur.moveToNext();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			return own_records;
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<org.lib.model.FormsListBean> BuddiesLookUpRecords() {

		ArrayList<org.lib.model.FormsListBean> buddy_records = new ArrayList<org.lib.model.FormsListBean>();
		Cursor cur = null;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ArrayList<String> buddies = new ArrayList<String>();

			Vector<BuddyInformationBean> data = ContactsFragment.getBuddyList();

			for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
				if (!bib.isTitle()) {
					if (!bib.getStatus().equalsIgnoreCase("Pending")) {
						if (!bib.getStatus().equalsIgnoreCase("New")) {
							buddies.add(bib.getName());

						}
					}
				}
			}

			for (String formsListBean : buddies) {
				// for (String formsListBean :
				// WebServiceReferences.buddies_forms) {

				cur = db.rawQuery(
						"SELECT tablename,owner,tableid,rowcount,Formiconname FROM formslookup where owner='"
								+ formsListBean + "' order by tablename", null);
				cur.moveToFirst();
				while (cur.isAfterLast() == false) {

					org.lib.model.FormsListBean flist_bean = new org.lib.model.FormsListBean();

					flist_bean.setForm_name(cur.getString(0));
					flist_bean.setForm_ownser(cur.getString(1));
					flist_bean.setForm_id(cur.getString(2));

					if (cur.getString(3).trim().length() == 0) {
						flist_bean.setnumberof_rows(cur.getString(3));
					} else {
						flist_bean.setnumberof_rows(cur.getString(3));
					}
					if (cur.isNull(cur.getColumnIndex("Formiconname"))) {
						flist_bean.setFormicon("");

					} else {

						flist_bean.setFormicon(cur.getString(4));

					}
					buddy_records.add(flist_bean);
					cur.moveToNext();

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			return buddy_records;
		}
	}

	public boolean isFormtableExists(String table_name) {
		boolean isexists = false;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			if (table_name != null) {
				Cursor cur = db.rawQuery(
						"select name from sqlite_master where type = 'table'",
						null);
				cur.moveToFirst();
				ArrayList<String> tableName = new ArrayList<String>();
				while (cur.isAfterLast() == false) {
					Log.i("tbl_name", "===>" + cur.getString(0));
					if (table_name.equalsIgnoreCase(cur.getString(0))) {
						tableName.add(table_name);
					}
					cur.moveToNext();
				}
				if (tableName.size() > 0) {
					isexists = true;
				}
			}
		} catch (Exception e) {
			isexists = false;
			e.printStackTrace();
		}
		return isexists;
	}

	@SuppressWarnings("finally")
	public boolean record(String frm_name, String tableid) {
		Cursor ti = null;
		boolean isexists = false;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			String formName = "[" + frm_name + "]";

			Log.i("PROFILELOG", "SELECT * FROM " + formName
					+ " WHERE tableid='" + tableid + "'");

			ti = db.rawQuery("SELECT * FROM " + formName + " WHERE tableid='"
					+ tableid + "'", null);
			if (ti.moveToFirst()) {
				Log.i("forms", "@@@@@@@@@@@ recordddddddd exists");
				isexists = true;

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			isexists = false;
		} finally {
			if (ti != null) {
				ti.close();
			}
			return isexists;
		}

	}

	@SuppressWarnings("finally")
	public boolean isFormExists(String frm_name, String owner_name) {
		Cursor ti = null;
		boolean isexists = false;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("SELECT * FROM formslookup WHERE  tableid='"
					+ frm_name + "' and owner='" + owner_name + "'", null);
			if (ti.moveToFirst()) {
				Log.i("thread", "@@@@@@@@@@@ table exists");
				isexists = true;

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return isexists;
		}

	}

	@SuppressWarnings("finally")
	public String getColumnofSyncoffline(String formownerid, String formid) {
		String value = null;
		Log.i("a", "inside DB For ACCESS**********" + formownerid + formid);

		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				Log.i("a", "inside DB For ACCESS********** getconnections");

				openDatabase();
			}
			ti = db.rawQuery(
					"SELECT synccode  FROM formsettings WHERE buddyid='"
							+ formownerid + "' and formid='" + formid + "'",
					null);

			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					Log.i("a", "inside DB For ACCESS whileee**********");

					String[] recs = new String[ti.getColumnCount()];
					for (int i = 0; i < ti.getColumnCount(); i++) {

						recs[i] = ti.getString(i);
						value = ti.getString(i);

						Log.i("a", "db valuesss**********" + recs[i]);
					}
					System.out.println("col  table id====: " + ti.getString(1));
					// column_recs.add(recs);
					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return value;
		}

	}

	@SuppressWarnings("finally")
	public String getColumnofAcessoffline(String formownerid, String formid) {
		String value = null;
		Log.i("a", "inside DB For ACCESS**********" + formownerid + formid);

		ArrayList<String[]> column_recs = new ArrayList<String[]>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				Log.i("a", "inside DB For ACCESS********** getconnections");

				openDatabase();
			}
			ti = db.rawQuery(
					"SELECT accesscode FROM formsettings WHERE buddyid='"
							+ formownerid + "' and formid='" + formid + "'",
					null);

			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					Log.i("a", "inside DB For ACCESS whileee**********");

					String[] recs = new String[ti.getColumnCount()];
					for (int i = 0; i < ti.getColumnCount(); i++) {

						recs[i] = ti.getString(i);
						value = ti.getString(i);

						Log.i("a", "db valuesss**********" + recs[i]);
					}
					System.out.println("col  table id====: " + ti.getString(1));
					column_recs.add(recs);
					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return value;
		}

	}

	@SuppressWarnings("finally")
	public ArrayList<String> getColumnNames(String tblname) {
		ArrayList<String> column_names = new ArrayList<String>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("PRAGMA table_info(" + "[" + tblname + "]" + ")",
					null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					Log.i("thread ", "Col :" + ti.getString(1));
					Log.i("thread ", "Col type:" + ti.getString(2));
					Log.i("test ", "Col :" + ti.getString(1));
					Log.i("test ", "Col type:" + ti.getString(2));
					if ((!ti.getString(1).equalsIgnoreCase("id"))
							&& (!ti.getString(1).equalsIgnoreCase("tableid"))) {
						column_names.add(ti.getString(1));
					}
					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	@SuppressWarnings("finally")
	public String getColumnofAcess(String formownerid, String formid) {
		String value = null;
		Log.i("a", "inside DB For ACCESS**********" + formownerid + formid);

		ArrayList<String[]> column_recs = new ArrayList<String[]>();
		Cursor ti = null;
		try {
			if (!db.isOpen())
				openDatabase();

			ti = db.rawQuery(
					"SELECT accesscode FROM formsettings WHERE buddyid='"
							+ CallDispatcher.LoginUser + "' and formid='"
							+ formid + "'", null);

			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {

					String[] recs = new String[ti.getColumnCount()];
					for (int i = 0; i < ti.getColumnCount(); i++) {

						recs[i] = ti.getString(i);
						value = ti.getString(i);

						Log.i("a", "db valuesss**********" + recs[i]);
					}

					column_recs.add(recs);
					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return value;
		}

	}

	public int getreocrdcount(String formid) {
		String[] result = null;
		int count = 0;

		String id = formid;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT Count(*) FROM formsettings where formid=" + id);
			count = getContentCount("SELECT Count(*) FROM formsettings where formid="
					+ id);

			Cursor c = db.rawQuery(sb.toString(), null);
			c.moveToFirst();

			result = new String[c.getCount()];
			int i = 0;
			while (c.moveToNext()) {
				Log.i("settings", "inside while");

				result[i] = c.getString(c.getColumnIndex("formid"));

				i++;
			}
			c.close();
		} catch (Exception e) {
		}
		return count;
	}

	@SuppressWarnings("finally")
	public String[] getFormDetailsRecords(String id) {
		String[] form_values = null;
		Cursor cur = null;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			cur = db.rawQuery(
					"SELECT Formiconname,Formdescription FROM formslookup where tableid='"
							+ id + "'", null);
			cur.moveToFirst();
			form_values = new String[2];
			while (cur.isAfterLast() == false) {

				if (cur.isNull(cur.getColumnIndex("Formiconname"))) {
					form_values[0] = "";

				} else {
					form_values[0] = cur.getString(cur
							.getColumnIndex("Formiconname"));

				}

				if (cur.isNull(cur.getColumnIndex("Formdescription"))) {
					form_values[1] = "";

				} else {
					form_values[1] = cur.getString(cur
							.getColumnIndex("Formdescription"));

				}

				cur.moveToNext();

			}
			cur.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			return form_values;
		}
	}

	@SuppressWarnings("finally")
	public String[] getColumnofSync(String formid) {
		String value[] = new String[3];

		Cursor cur = null;
		try {
			if (db.isOpen()) {

			} else {

				openDatabase();
			}
			cur = db.rawQuery(
					"SELECT settingid,synccode,formowenerid  FROM formsettings WHERE buddyid='"
							+ CallDispatcher.LoginUser + "' and formid='"
							+ formid + "'", null);

			if (cur.moveToFirst()) {
				while (cur.isAfterLast() == false) {
					value[0] = cur.getString(0);
					value[1] = cur.getString(1);
					value[2] = cur.getString(2);
					cur.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			return value;
		}

	}

	@SuppressWarnings("finally")
	public ArrayList<String[]> getRecordsofreadAndAddOwn(String tblname,
			HashMap<String, String> types) {
		String[] recs = null;
		ArrayList<String[]> column_recs = new ArrayList<String[]>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String tbl = tblname;
			if (!tbl.contains("[") && tbl.contains("]")) {
				tbl = "[" + tblname + "]";

			}

			ti = db.rawQuery("SELECT * FROM " + tbl + " WHERE uuid='"
					+ CallDispatcher.LoginUser + "'", null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					recs = new String[ti.getColumnCount() - 1];
					for (int i = 2; i < ti.getColumnCount(); i++) {
						System.out.println("col: " + ti.getColumnName(i));
						String tp = types.get(ti.getColumnName(i));
						if (tp.contains("2")) {
							recs[i - 2] = ti.getString(i);
						} else if (tp.equalsIgnoreCase("INTEGER")) {
							recs[i - 2] = Integer.toString(ti.getInt(i));
						} else if (tp.equalsIgnoreCase("BLOB")) {
							recs[i - 2] = Base64.encodeToString(ti.getBlob(i),
									Base64.DEFAULT);
						}

						Log.i("records", "col  table id====0000: " + tp);
						Log.i("records",
								"col  table id====1111: " + ti.getString(i));
						Log.i("records", "col  table id====222: " + recs[i - 2]);

					}

					recs[recs.length - 1] = ti.getString(1);

					for (int i = 0; i < recs.length; i++) {
						Log.i("ne", "col  table id====: " + recs[i]);

					}
					column_recs.add(recs);
					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_recs;
		}

	}

	@SuppressWarnings("finally")
	public ArrayList<String[]> getRecordsofFormtbl(String tblname,
			HashMap<String, String> types) {
		String[] recs = null;
		ArrayList<String[]> column_recs = new ArrayList<String[]>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String tbl = tblname;
			if (!tbl.contains("[") && !tbl.contains("]")) {
				tbl = "[" + tblname + "]";

			}

			ti = db.rawQuery("SELECT * FROM " + tbl
					+ " WHERE status='0' OR status='1' OR status='2'", null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					recs = new String[ti.getColumnCount() - 1];
					for (int i = 2; i < ti.getColumnCount(); i++) {
						System.out.println("col: " + ti.getColumnName(i));
						String tp = types.get(ti.getColumnName(i));
						if (tp != null) {
							if (tp.contains("2")) {
								recs[i - 2] = ti.getString(i);
							} else if (tp.equalsIgnoreCase("INTEGER")) {
								recs[i - 2] = Integer.toString(ti.getInt(i));
							} else if (tp.equalsIgnoreCase("BLOB")) {
								recs[i - 2] = Base64.encodeToString(
										ti.getBlob(i), Base64.DEFAULT);
							}

							Log.i("records", "col  table id====0000: " + tp);
							Log.i("records",
									"col  table id====1111: " + ti.getString(i));
							Log.i("records", "col  table id====222: "
									+ recs[i - 2]);

						} else {
							Log.i("newform123",
									"data type with null" + ti.getColumnName(i));

						}
					}
					recs[recs.length - 1] = ti.getString(1);

					for (int i = 0; i < recs.length; i++) {
						Log.i("ne", "col  table id====: " + recs[i]);

					}
					column_recs.add(recs);

					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_recs;
		}

	}

	public ArrayList<OfflineFormRecordBean> getOfflineRecordsofFormtbl(
			String tblname) {
		ArrayList<OfflineFormRecordBean> offlineFormRecordList = new ArrayList<OfflineFormRecordBean>();
		Cursor cur = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String tbl = tblname;
			if (!tbl.contains("[") && !tbl.contains("]")) {
				tbl = "[" + tblname + "]";

			}

			cur = db.rawQuery("SELECT * FROM " + tbl + " WHERE status='0'",
					null);
			if (cur.moveToFirst()) {
				while (cur.isAfterLast() == false) {
					OfflineFormRecordBean oBean = new OfflineFormRecordBean();
					oBean.setId(cur.getInt(0));
					oBean.setTableId(cur.getString(1));
					oBean.setTableName(cur.getString(2));
					oBean.setUuid(cur.getString(3));
					oBean.setEuuid(cur.getString(4));
					oBean.setUudate(cur.getString(5));
					oBean.setRecordDate(cur.getString(6));
					oBean.setStatus(cur.getString(7));
					offlineFormRecordList.add(oBean);

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			return offlineFormRecordList;
		}

	}

	public ArrayList<String> getDBNames() {
		ArrayList<String> result = new ArrayList<String>();
		Cursor c = null;

		try {

			if (!db.isOpen())
				openDatabase();
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM formslookup ");

			c = db.rawQuery(sb.toString(), null);

			Log.i("welcome", "countttt" + c.getCount());

			if (c.moveToFirst()) {
				while (c.isAfterLast() == false) {

					String id = c.getString(c.getColumnIndex("tableid"));
					String COUNT = c.getString(c.getColumnIndex("rowcount"));

					Log.i("welcome",
							"countttt"
									+ c.getString(c.getColumnIndex("tablename"))
									+ "_" + id);
					Log.i("welcome", "iddddd first" + id);

					if (id.length() != 0 && COUNT.length() != 0) {
						result.add("["
								+ c.getString(c.getColumnIndex("tablename"))
								+ "_" + id + "]");

						Log.i("welcome", "iddddd inside" + id);

						Log.i("welcome",
								"insidecountttt"
										+ c.getString(c
												.getColumnIndex("tablename"))
										+ "_" + id);

					}

					c.moveToNext();

				}

			}

		} catch (Exception e) {
		}
		return result;
	}

	@SuppressWarnings("finally")
	public ArrayList<String> getColumnSpclQA(String tblname) {

		String[] tablenames = tblname.split(",");

		ArrayList<String> column_names = new ArrayList<String>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			if (tablenames.length > 0) {
				for (int i = 0; i < tablenames.length; i++) {
					String column_name = tablenames[i];
					ti = db.rawQuery("PRAGMA table_info(" + column_name + ")",
							null);
					if (ti.moveToFirst()) {
						while (ti.isAfterLast() == false) {
							Log.i("thread ", "Col :" + ti.getString(1));
							Log.i("thread ", "Col type:" + ti.getString(2));
							Log.i("test ", "Col :" + ti.getString(1));
							Log.i("test ", "Col type:" + ti.getString(2));

							if (!ti.getString(1).equals("Id")
									&& !ti.getString(1).equals("tableid")) {
								column_names.add(ti.getString(2));

							}

							ti.moveToNext();

						}

					}

				}
			}

			else {

				ti = db.rawQuery("PRAGMA table_info([" + tblname + "])", null);
				if (ti.moveToFirst()) {
					while (ti.isAfterLast() == false) {
						Log.i("thread ", "Col :" + ti.getString(1));
						Log.i("thread ", "Col type:" + ti.getString(2));
						Log.i("test ", "Col :" + ti.getString(1));
						Log.i("test ", "Col type:" + ti.getString(2));

						column_names.add(ti.getString(2));

						ti.moveToNext();

					}

				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<String> getColumnTypesTblQuery(String tblname) {

		String[] tablenames = tblname.split(",");

		ArrayList<String> column_names = new ArrayList<String>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			if (tablenames.length > 0) {
				for (int i = 0; i < tablenames.length; i++) {
					String column_name = "[" + tablenames[i] + "]";
					ti = db.rawQuery("PRAGMA table_info(" + column_name + ")",
							null);
					if (ti.moveToFirst()) {
						while (ti.isAfterLast() == false) {
							Log.i("thread ", "Col :" + ti.getString(1));
							Log.i("thread ", "Col type:" + ti.getString(2));
							Log.i("test ", "Col :" + ti.getString(1));
							Log.i("test ", "Col type:" + ti.getString(2));

							if (!ti.getString(1).equals("Id")
									&& !ti.getString(1).equals("tableid")) {
								column_names.add(ti.getString(2));

							}

							ti.moveToNext();

						}

					}

				}
			}

			else {

				ti = db.rawQuery("PRAGMA table_info([" + tblname + "])", null);
				if (ti.moveToFirst()) {
					while (ti.isAfterLast() == false) {
						Log.i("thread ", "Col :" + ti.getString(1));
						Log.i("thread ", "Col type:" + ti.getString(2));
						Log.i("test ", "Col :" + ti.getString(1));
						Log.i("test ", "Col type:" + ti.getString(2));

						column_names.add(ti.getString(2));

						ti.moveToNext();

					}

				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<String> getColumnNamesQuery(String tblname) {

		String[] tablenames = tblname.split(",");

		ArrayList<String> column_names = new ArrayList<String>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			if (tablenames.length > 0) {
				for (int i = 0; i < tablenames.length; i++) {
					ti = db.rawQuery(
							"PRAGMA table_info(" + tablenames[i] + ")", null);
					if (ti.moveToFirst()) {
						while (ti.isAfterLast() == false) {
							Log.i("thread ", "Col :" + ti.getString(1));
							Log.i("thread ", "Col type:" + ti.getString(2));
							Log.i("test ", "Col :" + ti.getString(1));
							Log.i("test ", "Col type :" + ti.getString(2));

							Log.i("welcome", "Col :" + ti.getString(1));
							Log.i("welcome", "Col type :" + ti.getString(2));
							Log.i("welcome", "Col :" + ti.getString(1));
							Log.i("welcome", "Col type :" + ti.getString(2));

							Log.i("welcome", "Table name in Db access class-->"
									+ tablenames[i]);

							if (ti.getString(1) != null) {
								if (!ti.getString(1).equals("Id")
										&& !ti.getString(1).equals("tableid")
										&& !ti.getString(1)
												.equals("recorddate")
										&& !ti.getString(1).equals("status"))

									column_names.add(tablenames[i] + "." + "["
											+ ti.getString(1) + "]");

							}

							ti.moveToNext();

						}

					}

				}
			}

			else {

				ti = db.rawQuery("PRAGMA table_info(" + tblname + ")", null);
				if (ti.moveToFirst()) {
					while (ti.isAfterLast() == false) {
						Log.i("thread ", "Col :" + ti.getString(1));
						Log.i("thread ", "Col type:" + ti.getString(2));
						Log.i("test ", "Col :" + ti.getString(1));
						Log.i("test ", "Col type:" + ti.getString(2));

						column_names.add(tblname + "." + "[" + ti.getString(1)
								+ "]");

						ti.moveToNext();

					}

				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<String> getColumnQueryForQuery(String tblname) {
		ArrayList<String> column_names = new ArrayList<String>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			ti = db.rawQuery("PRAGMA table_info(" + tblname + ")", null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					Log.i("thread ", "Col :" + ti.getString(1));
					Log.i("thread ", "Col type:" + ti.getString(2));
					Log.i("test ", "Col :" + ti.getString(1));
					Log.i("test ", "Col type:" + ti.getString(2));
					if ((!ti.getString(1).equalsIgnoreCase("id"))
							&& (!ti.getString(1).equalsIgnoreCase("recorddate"))
							&& (!ti.getString(1).equalsIgnoreCase("uuid"))
							&& (!ti.getString(1).equalsIgnoreCase("euuid"))
							&& (!ti.getString(1).equalsIgnoreCase("uudate"))) {
						column_names.add(tblname + "." + "[" + ti.getString(1)
								+ "]");
					}

					ti.moveToNext();

				}

			}

			else {

				ti = db.rawQuery("PRAGMA table_info(" + tblname + ")", null);
				if (ti.moveToFirst()) {
					while (ti.isAfterLast() == false) {
						Log.i("thread ", "Col :" + ti.getString(1));
						Log.i("thread ", "Col type:" + ti.getString(2));
						Log.i("test ", "Col :" + ti.getString(1));
						Log.i("test ", "Col type:" + ti.getString(2));

						column_names.add(tblname + "." + "[" + ti.getString(1)
								+ "]");
						ti.moveToNext();

					}

				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	@SuppressWarnings("finally")
	public HashMap<String, String> getColumnNamesQueryMap(String tblname) {

		String[] tablenames = tblname.split(",");
		HashMap<String, String> hsNotes = new HashMap<String, String>();

		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			if (tablenames.length > 0) {
				for (int i = 0; i < tablenames.length; i++) {
					// String tblName = tablenames[i];
					String tblName = "[" + tablenames[i] + "]";

					ti = db.rawQuery("PRAGMA table_info(" + tblName + ")", null);
					if (ti.moveToFirst()) {
						while (ti.isAfterLast() == false) {
							Log.i("thread ", "Col :" + ti.getString(1));
							Log.i("thread ", "Col type:" + ti.getString(2));
							Log.i("test ", "Col :" + ti.getString(1));
							Log.i("test ", "Col type :" + ti.getString(2));

							Log.i("welcome", "Col :" + ti.getString(1));
							Log.i("welcome", "Col type :" + ti.getString(2));
							Log.i("welcome", "Col :" + ti.getString(1));
							Log.i("welcome", "Col type :" + ti.getString(2));

							Log.i("welcome", "Table name in Db access class-->"
									+ tablenames[i]);

							if (ti.getString(1) != null) {
								if (!ti.getString(1).equals("Id")
										&& !ti.getString(1).equals("tableid")
										&& !ti.getString(1)
												.equals("recorddate")
										&& !ti.getString(1).equals("status"))

									hsNotes.put(
											tablenames[i] + "." + "["
													+ ti.getString(1) + "]",
											ti.getString(2));

							}

							ti.moveToNext();

						}

					}

				}
			}

			else {
				String tblName = "[" + tblname + "]";
				ti = db.rawQuery("PRAGMA table_info(" + tblName + ")", null);
				if (ti.moveToFirst()) {
					while (ti.isAfterLast() == false) {
						Log.i("thread ", "Col :" + ti.getString(1));
						Log.i("thread ", "Col type:" + ti.getString(2));
						Log.i("test ", "Col :" + ti.getString(1));
						Log.i("test ", "Col type:" + ti.getString(2));

						if (ti.getString(1) != null) {
							if (!ti.getString(1).equals("Id")
									&& !ti.getString(1).equals("tableid")
									&& !ti.getString(1).equals("recorddate")
									&& !ti.getString(1).equals("status"))

								hsNotes.put(
										tblname + "." + "[" + ti.getString(1)
												+ "]", ti.getString(2));

						}

						ti.moveToNext();

					}

				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return hsNotes;
		}
	}

	public boolean isValidQuery(String qry, String owner) {
		boolean result = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String[] split_qry = qry.split("\\s+");
			Log.i("thread", "querryyyy  isvalidquery......." + qry);

			if (split_qry.length >= 4) {
				Log.i("thread", "Splitted table name isvalidquery ......."
						+ split_qry[3]);
				String id = getFormId(split_qry[3]);
				split_qry[3] = split_qry[3] + "_" + id;
				String result_qry = "";
				for (int i = 0; i < split_qry.length; i++) {
					if (result_qry.trim().length() == 0)
						result_qry = split_qry[i];
					else
						result_qry = result_qry + " " + split_qry[i];
				}
				Log.i("thread", "Splitted table name isvalidquery ......."
						+ qry);

				db.rawQuery(qry, null);
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return result;
	}

	@SuppressWarnings("finally")
	public String getFormId(String name) {

		Log.i("thread", "tbl name............." + name);
		Cursor ti = null;
		String id = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery(
					"SELECT tableid FROM formslookup WHERE tablename='" + "["
							+ name + "]" + "'", null);
			ti.moveToFirst();
			Log.i("thread", "@@@@@@@@@@@ table exists count" + ti.getCount());
			if (ti.getCount() > 0) {
				Log.i("thread", "@@@@@@@@@@@ table exists");
				id = ti.getString(0);
				Log.i("thread", "@@@@@@@@@@@ table exists" + id);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			if (ti != null) {
				ti.close();
			}
			return id;
		}

	}

	@SuppressWarnings("finally")
	public ArrayList<formItem> BLDatasOverall() {
		Cursor cur = null;
		ArrayList<formItem> totalDatas = new ArrayList<formItem>();

		try {

			String qry = "select * from CustomAction where username='"
					+ CallDispatcher.LoginUser + "'";
			if (!db.isOpen()) {
				openDatabase();
			}
			cur = db.rawQuery(qry, null);
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {

				formItem blBean = new formItem();
				blBean.setTitle(cur.getString(1));
				blBean.setId(String.valueOf(cur.getInt(0)));
				blBean.setAction(cur.getString(7));

				totalDatas.add(blBean);

				cur.moveToNext();

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		finally {
			if (cur != null) {
				cur.close();
			}

			return totalDatas;
		}

	}

	@SuppressWarnings("finally")
	public String getRecordtime(String tbl_id) {

		Log.i("forms", "inside getrecordtime name............." + tbl_id);
		Cursor ti = null;
		String id = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("SELECT formtime FROM formslookup WHERE tableid='"
					+ tbl_id + "'", null);
			ti.moveToFirst();
			Log.i("thread", "@@@@@@@@@@@ table exists count" + ti.getCount());
			if (ti.getCount() > 0) {
				Log.i("thread", "@@@@@@@@@@@ table exists");
				id = ti.getString(0);
				Log.i("forms", "@@@@@@@@@@@ this is formtime exists" + id);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			if (ti != null) {
				ti.close();
			}
			return id;
		}

	}

	@SuppressWarnings("finally")
	public String getSettingsModifiedtime(String tbl_id) {

		Log.i("forms", "inside getrecordtime name............." + tbl_id);
		Cursor ti = null;
		String id = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery(
					"SELECT datemodified FROM formsettings WHERE formid='"
							+ tbl_id + "'", null);
			ti.moveToFirst();
			Log.i("thread", "@@@@@@@@@@@ table exists count" + ti.getCount());
			if (ti.getCount() > 0) {
				Log.i("thread", "@@@@@@@@@@@ table exists");
				id = ti.getString(0);
				Log.i("forms", "@@@@@@@@@@@ this is formtime exists" + id);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			if (ti != null) {
				ti.close();
			}
			return id;
		}

	}

	@SuppressWarnings("finally")
	public boolean isAttributeFormExists(String tablename, String owner_name) {
		Cursor ti = null;
		boolean isexists = false;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("SELECT * FROM forminfo WHERE  tablename='" + "["
					+ tablename + "]" + "' and column='" + "[" + owner_name
					+ "]" + "'", null);
			if (ti.moveToFirst()) {
				Log.i("thread", "@@@@@@@@@@@ table exists");
				isexists = true;

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return isexists;
		}

	}

	@SuppressWarnings("finally")
	public Vector<InputsFields> getFormFields(String tablename) {
		Cursor cur = null;
		Vector<InputsFields> fieldList = new Vector<InputsFields>();

		try {
			if (!db.isOpen()) {
				openDatabase();
			}

			cur = db.rawQuery("SELECT * FROM forminfo WHERE  tablename='" + "["
					+ tablename + "]" + "'", null);
			if (cur.moveToFirst()) {
				while (cur.isAfterLast() == false) {
					InputsFields iFields = new InputsFields();
					iFields.setId(cur.getInt(0));
					iFields.setFieldName(cur.getString(2).replace("[", "")
							.replace("]", ""));
					iFields.setFieldType(cur.getString(3));
					iFields.setValidData(cur.getString(4));
					iFields.setDefaultValue(cur.getString(5));
					iFields.setInstructions(cur.getString(6));
					iFields.setErrorMsg(cur.getString(7));
					iFields.setAttributeId(cur.getString(8));
					fieldList.add(iFields);
					cur.moveToNext();
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			return fieldList;
		}

	}

	@SuppressWarnings("finally")
	public ArrayList<String> LocalTableName(String ids) {
		ArrayList<String> own_records = new ArrayList<String>();
		Cursor cur = null;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			cur = db.rawQuery(
					"SELECT tablename,tableid FROM formslookup where tableid not in("
							+ ids + ")", null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				if (cur.getString(0) != null && cur.getString(1) != null) {
					String table_name = cur.getString(0) + "_"
							+ cur.getString(1);

					Log.i("FORMOPT", "table name from db===>" + table_name);
					own_records.add(table_name);
					cur.moveToNext();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			return own_records;
		}
	}

	public boolean isSettingFormExists(String settingid) {
		Log.i("FORMSETT", "inside isSettingFormExists===>" + settingid);

		Cursor ti = null;
		boolean isexists = false;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("SELECT * FROM formsettings WHERE settingid='"
					+ settingid + "'", null);
			ti.moveToFirst();
			if (ti.getCount() > 0) {
				isexists = true;

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
		}

		Log.i("FORMSETT", "inside isexists===>" + isexists);

		return isexists;

	}

	@SuppressWarnings("finally")
	public ArrayList<String> LocalTableNameForNewUser() {
		ArrayList<String> own_records = new ArrayList<String>();
		Cursor cur = null;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			cur = db.rawQuery("SELECT tablename,tableid FROM formslookup", null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				if (cur.getString(0) != null && cur.getString(1) != null) {
					String table_name = cur.getString(0) + "_"
							+ cur.getString(1);

					Log.i("FORMOPT", "table name from db===>" + table_name);
					own_records.add(table_name);
					cur.moveToNext();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			return own_records;
		}
	}

	public String[] getDBNamesOFFline() {
		Log.i("off", "INSIDE OFFLINE getDBNamesOFFline");

		String[] result = null;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM formslookup WHERE status='" + "0" + "'");

			Cursor c = db.rawQuery(sb.toString(), null);
			Log.i("off",
					"INSIDE OFFLINE getDBNamesOFFline counttttttt"
							+ c.getCount());

			c.moveToFirst();

			result = new String[c.getCount()];
			int i = 0;
			while (c.isAfterLast() == false) {
				Log.i("forms", "inside while");

				String id = c.getString(c.getColumnIndex("tableid"));

				result[i] = c.getString(c.getColumnIndex("tablename")) + "_"
						+ id;
				Log.i("off", "INSIDE OFFLINE getDBNamesOFFline****************"
						+ result[i]);

				c.moveToNext();
				i++;
			}
			c.close();
		} catch (Exception e) {

			Log.i("off", "INSIDE OFFLINE error message" + e.getMessage());

		}
		return result;
	}

	public String[] getDeleteDBNamesOFFline() {
		Log.i("off", "INSIDE OFFLINE getDBNamesOFFline");

		String[] result = null;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM formslookup WHERE status='" + "3" + "'");

			Cursor c = db.rawQuery(sb.toString(), null);
			Log.i("off",
					"INSIDE OFFLINE getDBNamesOFFline counttttttt"
							+ c.getCount());

			c.moveToFirst();

			result = new String[c.getCount()];
			int i = 0;
			while (c.isAfterLast() == false) {
				Log.i("forms", "inside while");

				String id = c.getString(c.getColumnIndex("tableid"));

				result[i] = c.getString(c.getColumnIndex("tablename")) + "_"
						+ id;
				Log.i("off", "INSIDE OFFLINE getDBNamesOFFline****************"
						+ result[i]);

				c.moveToNext();
				i++;
			}
			c.close();
		} catch (Exception e) {

			Log.i("off", "INSIDE OFFLINE error message" + e.getMessage());

		}
		return result;
	}

	@SuppressWarnings("finally")
	public ArrayList<String> getColumnNamesoffline(String tblname) {
		ArrayList<String> column_names = new ArrayList<String>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("PRAGMA table_info(" + tblname + ")", null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					Log.i("thread ", "Col :" + ti.getString(1));
					Log.i("thread ", "Col type:" + ti.getString(2));
					Log.i("off ", "this is offline Col :" + ti.getString(1));
					Log.i("off ", "Col type:" + ti.getString(2));
					if ((!ti.getString(1).equalsIgnoreCase("id"))
							&& (!ti.getString(1).equalsIgnoreCase("tableid"))
							&& (!ti.getString(1).equalsIgnoreCase("recorddate"))
							&& (!ti.getString(1).equalsIgnoreCase("status"))) {
						column_names.add(ti.getString(1));
					}
					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<String[]> getRecordsoflineinsert(String tblname,
			String status) {
		ArrayList<String[]> column_recs = new ArrayList<String[]>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("SELECT * FROM " + tblname + " where status="
					+ status, null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					String[] recs = new String[ti.getColumnCount() - 4];
					for (int i = 2; i < ti.getColumnCount() - 4; i++) {

						recs[i - 2] = ti.getString(i);

						Log.i("off", "this is status o**********" + recs[i - 2]);
					}
					System.out.println("col  table id====: " + ti.getString(1));
					column_recs.add(recs);
					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_recs;
		}

	}

	@SuppressWarnings("finally")
	public ArrayList<String[]> getRecordsoflineupdate(String tblname) {
		ArrayList<String[]> column_recs = new ArrayList<String[]>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("SELECT * FROM " + tblname + " where status="
					+ "2", null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					String[] recs = new String[ti.getColumnCount() - 3];
					for (int i = 1; i < ti.getColumnCount() - 3; i++) {

						recs[i - 1] = ti.getString(i);

						Log.i("settings", "**********" + recs[i - 1]);
					}
					System.out.println("col  table id====: " + ti.getString(1));
					column_recs.add(recs);
					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_recs;
		}

	}

	@SuppressWarnings("finally")
	public ArrayList<String[]> getRecordsoflinedelete(String tblname) {
		Log.i("off", "INSIDE DELETEEEEEE");

		ArrayList<String[]> column_recs = new ArrayList<String[]>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("SELECT Id,tableid FROM " + tblname
					+ " where status=3", null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					String[] recs = new String[ti.getColumnCount()];

					for (int i = 0; i < ti.getColumnCount(); i++) {

						recs[i] = ti.getString(i);

						Log.i("settings", "**********" + recs[i]);
					}
					System.out.println("col  table id====: " + ti.getString(1));
					column_recs.add(recs);
					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_recs;
		}

	}

	public ArrayList<OfflineRequestConfigBean> getOfflineCallResponseDetails(
			String condition) {
		Cursor cur = null;
		ArrayList<OfflineRequestConfigBean> offlineCallResponseDetailsList = new ArrayList<OfflineRequestConfigBean>();
		try {
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery("select * from offlinecallresponsedetails "
					+ condition, null);
			while (cur.moveToNext()) {
				OfflineRequestConfigBean bean = new OfflineRequestConfigBean();
				bean.setId(Integer.toString(cur.getInt(0)));
				bean.setUserId(cur.getString(1));
				bean.setBuddyId(cur.getString(2));
				bean.setConfigId(cur.getString(3));
				bean.setMessageTitle(cur.getString(4));
				bean.setResponseType(cur.getString(5));
				bean.setResponse(cur.getString(6));
				bean.setReceivedTime(cur.getString(7));
				bean.setStatus(cur.getInt(8));
				offlineCallResponseDetailsList.add(bean);
			}

		} catch (Exception e) {
			Log.e("banudebug",
					this.getClass().getName() + " BLDatas " + e.getMessage());
			e.printStackTrace();

		}
		return offlineCallResponseDetailsList;
	}

	public ArrayList<OfflineRequestConfigBean> getOfflineSettingDetails(
			String condition) {
		Cursor cur = null;
		ArrayList<OfflineRequestConfigBean> settingsDetailsList = new ArrayList<OfflineRequestConfigBean>();
		try {
			if (db.isOpen())
				openDatabase();

			String query = "select * from offlinecallsettingdetails "
					+ condition;
			Log.d("Avataar", "My Select querry is--->" + query);
			cur = db.rawQuery(query, null);
			cur.moveToFirst();

			while (!cur.isAfterLast()) {
				OfflineRequestConfigBean bean = new OfflineRequestConfigBean();
				bean.setId(Integer.toString(cur.getInt(0)));
				bean.setUserId(cur.getString(1));
				bean.setBuddyId(cur.getString(2));
				bean.setMessageTitle(cur.getString(3));
				bean.setMessagetype(cur.getString(4));
				bean.setMessage(cur.getString(5));
				bean.setResponseType(cur.getString(6));
				bean.setUrl(cur.getString(7));
				bean.setWhen(cur.getString(8));
				bean.setStatus(cur.getInt(9));
				settingsDetailsList.add(bean);
				cur.moveToNext();
			}

		} catch (Exception e) {
			Log.e("banudebug",
					this.getClass().getName() + " BLDatas " + e.getMessage());
			e.printStackTrace();

		}
		return settingsDetailsList;
	}

	public ArrayList<OfflineRequestConfigBean> getOfflineCallSettingsDetailsForDelete() {
		Cursor cur = null;
		ArrayList<OfflineRequestConfigBean> settingsDetailsList = new ArrayList<OfflineRequestConfigBean>();
		try {
			if (db.isOpen())
				openDatabase();

			cur = db.rawQuery(
					"select * from offlinecallsettingdetails where userid="
							+ "\"" + CallDispatcher.LoginUser + "\"", null);
			cur.moveToFirst();

			while (!cur.isAfterLast()) {
				OfflineRequestConfigBean bean = new OfflineRequestConfigBean();
				bean.setId(Integer.toString(cur.getInt(0)));
				bean.setUserId(cur.getString(1));
				bean.setBuddyId(cur.getString(2));
				bean.setMessageTitle(cur.getString(3));
				bean.setMessagetype(cur.getString(4));
				bean.setMessage(cur.getString(5));
				bean.setResponseType(cur.getString(6));
				bean.setUrl(cur.getString(7));
				bean.setWhen(cur.getString(8));
				bean.setMode("delete");
				settingsDetailsList.add(bean);
				cur.moveToNext();
			}

		} catch (Exception e) {
			Log.e("banudebug",
					this.getClass().getName() + " BLDatas " + e.getMessage());
			e.printStackTrace();

		}
		return settingsDetailsList;
	}

	public void deleteOfflineCallSettingDetails(String id) {

		try {
			if (db.isOpen())
				openDatabase();

			String strQuery = null;
			if (id != null)
				strQuery = "DELETE from offlinecallsettingdetails WHERE Id="
						+ id;
			else
				strQuery = "DELETE from offlinecallsettingdetails";

			db.execSQL(strQuery);
		} catch (Exception e) {
			Log.e("Avataar", " deleteOfflineCallResponseDetails message== >"
					+ e.getMessage());
		}
	}

	public String getMaxDateofOfflineConfig(String userId) {
		String dateTime = null;
		try {
			if (db.isOpen())
				openDatabase();

			SQLiteStatement stmt = db
					.compileStatement("SELECT MAX(record_time) FROM offlinecallsettingdetails where userid='"
							+ userId + "'");

			dateTime = (String) stmt.simpleQueryForString();

		} catch (Exception e) {
			Log.e("clone",
					" getMaxDateofOfflineConfig message== >" + e.getMessage());
		}
		return dateTime;

	}

	@SuppressWarnings("finally")
	public String getwheninfo(String qry) {
		String value = null;
		Cursor cur;
		try {
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery(qry, null);
			cur.moveToFirst();
			Log.d("clone", "------>" + cur.getCount());
			while (!cur.isAfterLast()) {

				value = cur.getString(0);

				Log.d("clone", "------> value is" + value);

				cur.moveToNext();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return value;
		}
	}

	@SuppressWarnings("finally")
	public int updateOfflineCallSettingsDetails(ContentValues cv,
			String condition) {
		int row_id = 0;
		try {
			Log.i("thread", "came to updateOfflineCallResponseDetails.......");

			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("offlinecallsettingdetails", cv,
					condition, null);
			Log.d("NOTES", "Row row id--->" + row_id);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i("thread",
					"came to updateOfflineCallSettingsDetails" + e.toString());
		} finally {
			return row_id;
		}
	}

	@SuppressWarnings("finally")
	public int insertOfflineCallSettingDetails(ContentValues cv) {
		int row_id = 0;
		try {
			Log.i("clone",
					"came to insertRecords insertOfflineCallSettingDetails.......");

			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.insert("offlinecallsettingdetails", null, cv);
			Log.d("clone", "Row inserted id--->" + row_id);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i("clone",
					"came to insertRecords pending clones" + e.toString());
		} finally {
			return row_id;
		}
	}

	public void deleteOfflineCallResponseDetails(String config_id) {
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = "DELETE from offlinecallresponsedetails WHERE id="
					+ "\"" + config_id + "\"";

			db.execSQL(strQuery);
			Log.i("clone", "delete offlinecall===> " + config_id);
		} catch (Exception e) {
			Log.e("clone",
					"deleteOfflineCallResponseDetails message== >"
							+ e.getMessage());
		}
	}

	@SuppressWarnings("finally")
	public int updateOfflineCallResponseDetails(ContentValues cv,
			String condition) {

		int row_id = 0;
		try {
			Log.i("thread", "came to updateOfflineCallResponseDetails.......");

			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("offlinecallresponsedetails", cv,
					condition, null);
			Log.d("NOTES", "Row row id--->" + row_id);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i("thread",
					"came to updateOfflineCallResponseDetails" + e.toString());
		} finally {
			return row_id;
		}
	}

	@SuppressWarnings("finally")
	public int updateOfflineCallPendingClones(ContentValues cv, String condition) {
		int row_id = 0;
		try {
			Log.i("thread", "came to updateOfflineCallResponseDetails.......");

			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("offlinecallpendingclones", cv, condition,
					null);
			Log.d("NOTES", "Row row id--->" + row_id);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i("thread",
					"came to updateOfflineCallPendingClones" + e.toString());
		} finally {
			return row_id;
		}
	}

	public boolean checkcloneMasterEntryexists() {
		boolean exists = false;
		if (!db.isOpen())
			openDatabase();

		Cursor cur = db.rawQuery("select * from clonemaster", null);
		if (cur.getCount() > 0)
			exists = true;
		return exists;
	}

	public void insertCloneMasterdata(ContentValues cv) {
		if (!db.isOpen())
			openDatabase();

		db.insert("clonemaster", null, cv);
	}

	public ArrayList<OfflineRequestConfigBean> getOfflinePendingClones(
			String condition) {
		Cursor cur = null;
		ArrayList<OfflineRequestConfigBean> offlinePendingCloneList = new ArrayList<OfflineRequestConfigBean>();
		try {
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery("select * from offlinecallpendingclones "
					+ condition, null);
			while (cur.moveToNext()) {
				OfflineRequestConfigBean bean = new OfflineRequestConfigBean();
				bean.setId(Integer.toString(cur.getInt(0)));
				bean.setConfig_id(cur.getString(1));
				bean.setFrom(cur.getString(2));
				bean.setMessageTitle(cur.getString(3));
				bean.setMessagetype(cur.getString(4));
				bean.setMessage(cur.getString(5));
				bean.setResponseType(cur.getString(6));
				bean.setResponse(cur.getString(7));
				bean.setUrl(cur.getString(8));
				bean.setReceivedTime(cur.getString(9));
				bean.setSendStatus(cur.getString(10));
				bean.setUserId(cur.getString(11));
				bean.setStatus(cur.getInt(12));
				offlinePendingCloneList.add(bean);
			}

		} catch (Exception e) {
			Log.e("banudebug",
					this.getClass().getName() + " BLDatas " + e.getMessage());
			e.printStackTrace();

		}
		return offlinePendingCloneList;
	}

	@SuppressWarnings({ "finally" })
	public boolean isfileisinuse(String path) {
		Cursor cur = null;
		boolean status = false;
		try {
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery(
					"select * from offlinecallpendingclones where message='"
							+ path + "' or response='" + path + "'", null);
			if (cur.getCount() > 0)
				status = true;

			if (!status) {
				cur = db.rawQuery(
						"select * from offlinecallresponsedetails where response='"
								+ path + "'", null);

				if (cur.getCount() > 0)
					status = true;
				if (!status) {

					cur = db.rawQuery(
							"select * from offlinecallsettingdetails where message='"
									+ path + "'", null);

					if (cur.getCount() > 0)
						status = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cur != null)
				cur.close();

			return status;
		}
	}

	public void deleteOfflinePendingClones(String id) {
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = "DELETE from offlinecallpendingclones WHERE id="
					+ id;
			db.execSQL(strQuery);
			Log.i("clone", "===> delte pending clones success");
		} catch (Exception e) {
			Log.e("clone",
					" deleteOfflinePendingClones message== >" + e.getMessage());
		}
	}

	@SuppressWarnings("finally")
	public int insertOfflinePendingClones(ContentValues cv) {
		int row_id = 0;
		try {
			Log.i("clone",
					"came to insertRecords insertOfflinePendingClones.......");

			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.insert("offlinecallpendingclones", null, cv);

			Log.d("clone", "insertOfflinePendingClones Row inserted id--->"
					+ row_id);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i("clone", "came to insertOfflinePendingClones" + e.toString());
		} finally {
			return row_id;
		}
	}

	@SuppressWarnings("finally")
	public int insertOfflineCallResponseDetails(ContentValues cv) {
		int row_id = 0;
		try {
			Log.i("thread",
					"came to insertRecords insertOfflineCallResponseDetails.......");

			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.insert("offlinecallresponsedetails", null, cv);
			Log.d("NOTES", "Row inserted id--->" + row_id);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i("thread",
					"came to insertOfflineCallResponseDetails" + e.toString());
		} finally {
			return row_id;
		}
	}

	public void insertCloneMasterDatas() {
		if (!checkcloneMasterEntryexists()) {
			ContentValues cv;
			cv = new ContentValues();
			cv.put("cid", "1");
			cv.put("cdescription", "No Answer");
			insertCloneMasterdata(cv);
			cv = null;
			cv = new ContentValues();
			cv.put("cid", "2");
			cv.put("cdescription", "Unable To Connect");
			insertCloneMasterdata(cv);
			cv = null;
			cv = new ContentValues();
			cv.put("cid", "3");
			cv.put("cdescription", "Call Dropped");
			insertCloneMasterdata(cv);
			cv = null;
			cv = new ContentValues();
			cv.put("cid", "4");
			cv.put("cdescription", "Call Disconnected");
			insertCloneMasterdata(cv);
			cv = null;
			cv = new ContentValues();
			cv.put("cid", "5");
			cv.put("cdescription", "Offline");
			insertCloneMasterdata(cv);
			cv = null;
			cv = new ContentValues();
			cv.put("cid", "6");
			cv.put("cdescription", "Call Rejected");
			insertCloneMasterdata(cv);
			cv = null;
			cv = new ContentValues();
			cv.put("cid", "7");
			cv.put("cdescription", "Missed Call");
			insertCloneMasterdata(cv);
			cv = null;
		}
	}

	public ContactLogicbean getQucikActionById(String id) {
		Cursor cur = null;
		String user = CallDispatcher.LoginUser;
		ContactLogicbean blBean = null;
		try {
			String SQL_GETQACTIONBYID = "select id,label,condition,time,status,ftppath,actioncode,touser,username,mode,freq,runmode,description from CustomAction where id=?";
			cur = db.rawQuery(SQL_GETQACTIONBYID, new String[] { id });
			while (cur.moveToNext()) {

				blBean = QuickActionBuilder.getSecLogicbean(user,
						cur.getString(6), cur.getInt(0));
				Log.i("QAA",
						"QAA" + "id :: " + cur.getString(0) + "iseditable :: "
								+ cur.getString(1) + "condition :: "
								+ cur.getString(2) + "edit time :: "
								+ cur.getString(3) + "status :: "
								+ cur.getString(4) + "ftpPath :: "
								+ cur.getString(5) + "action :: "
								+ cur.getString(6) + "edittoUser :: "
								+ cur.getString(7) + "fromUsre :: "
								+ cur.getString(8) + "timeMod :: "
								+ cur.getString(9) + "freq :: "
								+ cur.getString(10) + "run mode :: "
								+ cur.getString(11) + "freq mode :: "
								+ cur.getShort(9) + "mode time :: "
								+ cur.getString(12) + "description ::"
								+ cur.getString(10));

				blBean.setId(Integer.parseInt(cur.getString(0)));
				blBean.setEditlable(cur.getString(1));
				blBean.setEditconditon(cur.getString(2));
				blBean.setEdittime(cur.getString(3));
				blBean.setStatus(cur.getString(4));
				blBean.setFtpPath(cur.getString(5));
				blBean.setAction(cur.getString(6));
				blBean.setEditToUser(cur.getString(7));
				blBean.setFromuser(cur.getString(8));
				blBean.setTimemode(cur.getString(9));
				blBean.setFrequncy(cur.getString(10));
				blBean.setRunMode(cur.getString(11));
				blBean.setFreqMode(cur.getString(10));
				blBean.setModeTime(cur.getString(9));
				blBean.setDesc(cur.getString(12));
				blBean.setNew(false);
				blBean.setDirty(false);
			}

		} catch (Exception e) {
			Log.e("banudebug",
					this.getClass().getName() + " BLDatas " + e.getMessage());
			e.printStackTrace();

		}
		return blBean;

	}

	public String getFtpPath(String QAName) {
		Cursor cur = null;

		String filePath = "";
		try {
			cur = db.rawQuery("select ftpPath from CustomAction WHERE label=?",
					new String[] { QAName });
			while (cur.moveToNext()) {
				filePath = cur.getString(0);
				Log.i("DBB", "Filepath::===>" + filePath);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return filePath;

	}

	@SuppressWarnings("finally")
	public ArrayList<String[]> isQueryContainResultss(String qry, String owner) {
		ArrayList<String[]> column_names = new ArrayList<String[]>();

		Cursor ti = null;
		try {
			Log.i("name", "query" + qry);
			String[] split_qry = qry.split("\\s+");
			String[] val = null;

			if (split_qry.length >= 4) {
				String result_qry = "";
				for (int i = 0; i < split_qry.length; i++) {
					if (result_qry.trim().length() == 0)
						result_qry = split_qry[i];
					else
						result_qry = result_qry + " " + split_qry[i];
				}
				if (!db.isOpen())
					openDatabase();
				ti = db.rawQuery(qry, null);

				ti.moveToFirst();
				if (ti.getCount() > 0) {
					if (ti.moveToFirst()) {
						while (ti.isAfterLast() == false) {
							val = new String[ti.getColumnCount()];

							for (int i = 0; i < ti.getColumnCount(); i++) {
								if (ti.isNull(i)) {
									val[i] = "";

								} else {
									val[i] = ti.getString(i);

								}
							}

							column_names.add(val);
							ti.moveToNext();

						}

					}

				}

			}
		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}

	}

	@SuppressWarnings("finally")
	public String isQueryContainResult(String qry, String owner) {
		String result = "";
		Cursor ti = null;
		try {
			Log.i("name", "query" + qry);
			if (qry.startsWith("No trigger")) {
				result = "";
			} else {
				String[] split_qry = qry.split("\\s+");
				if (split_qry.length >= 4) {
					Log.i("thread", "Splitted table name ......."
							+ split_qry[3]);

					String result_qry = "";
					for (int i = 0; i < split_qry.length; i++) {
						if (result_qry.trim().length() == 0)
							result_qry = split_qry[i];
						else
							result_qry = result_qry + " " + split_qry[i];
					}

					if (!db.isOpen())
						openDatabase();
					ti = db.rawQuery(qry, null);

					ti.moveToFirst();

					if (ti.getCount() > 0) {
						result = "true";
					} else {
						result = "false";
					}

				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			result = "exception";
			return result;
		} finally {
			if (ti != null) {
				ti.close();
			}
			return result;
		}

	}

	@SuppressWarnings("finally")
	public String[] getRecordsofforminfotable1(String tblname,
			HashMap<String, String> types) {
		Log.i("IMP", "INSIDE FORMINFO RECORDS GET METHOD IN DB ACCESS "
				+ tblname);

		String[] recs = null;
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("SELECT * FROM forminfo WHERE column='" + "["
					+ tblname + "]" + "'", null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					recs = new String[ti.getColumnCount() - 2];
					Log.i("IMP",
							"INSIDE FORMINFO RECORDS GET METHOD IN DB countttt...... "
									+ ti.getColumnCount());

					for (int i = 2; i < ti.getColumnCount(); i++) {

						Log.i("IMP",
								"INSIDE FORMINFO RECORDS GET METHOD IN DB ACCESS...... "
										+ ti.getColumnName(i) + i);

						System.out.println("col: " + ti.getColumnName(i));
						String tp = types.get(ti.getColumnName(i));
						if (tp.contains("2")) {
							if (ti.getString(i) != null) {

								if (ti.getString(i) != null
										|| ti.getString(i).length() != 0) {
									recs[i - 2] = ti.getString(i);

								} else {
									recs[i - 2] = "";

								}
							} else {
								recs[i - 2] = "";

							}

						} else if (tp.equalsIgnoreCase("INTEGER")) {
							if (Integer.toString(ti.getInt(i)) != null) {
								recs[i - 2] = Integer.toString(ti.getInt(i));
							}

							else {
								recs[i - 2] = "";

							}
						} else if (tp.equalsIgnoreCase("BLOB")) {
							recs[i - 2] = Base64.encodeToString(ti.getBlob(i),
									Base64.DEFAULT);
						}

					}

					for (int i = 0; i < recs.length; i++) {
						Log.i("IMP", "col  table id====: " + recs[i]);

					}

					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return recs;
		}

	}

	public String getquickactionid() {
		Cursor cur = null;
		try {
			String id = null;

			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			cur = db.rawQuery("SELECT MAX(id) FROM CUSTOMACTION ", null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				id = Integer.toString(cur.getInt(0));
				cur.moveToNext();
			}
			cur.close();
			return id;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (cur != null)
				cur.close();

			return null;
		}
	}

	public void updateSecLogicbean(ContactLogicbean obj, String filepath,
			int id) {
		if (obj.isMarkDelete()) {
			db.delete("CUSTOMACTION", "ID", new String[] { obj.getId() + "" });

		}
		int alarmId = 0;
		if (obj.getId() > 0) {
			alarmId = obj.getId();
		} else {
			alarmId = id;
		}
		Log.i("QAA123", "alaramid : " + alarmId);

		if (CallDispatcher.timeMode.equalsIgnoreCase("manually")) {
			obj.setRunMode("RM");
			obj.setModeTime("Once");
			obj.setFreqMode("0");
		} else if (CallDispatcher.timeMode.equalsIgnoreCase("specific")) {
			obj.setRunMode("RS");
			obj.setModeTime("Once");
			obj.setFreqMode("0");
		} else if (CallDispatcher.timeMode.equalsIgnoreCase("Repeat")) {
			obj.setRunMode("RP");
		}
		if (obj.getSchedule().equalsIgnoreCase("This task will run manually")) {
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int min = c.get(Calendar.MINUTE);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int date = c.get(Calendar.DAY_OF_MONTH);
			String CurrentDate = year + "-" + month + "-" + date + " " + hour
					+ ":" + min;
			obj.setSchedule(CurrentDate);
		}

		if (!obj.isNew()) {
			if (alarmId > 0) {
				Intent intent = new Intent(SingleInstance.mainContext,
						QuickActionBroadCastReceiver.class);
				PendingIntent alarmIntent = PendingIntent.getBroadcast(
						SingleInstance.mainContext, alarmId, intent, 0);
				AlarmManager alarmMgr = (AlarmManager) SingleInstance.mainContext
						.getSystemService(Context.ALARM_SERVICE);
				alarmMgr.cancel(alarmIntent);
			}
			db.execSQL(
					"update CustomAction set label = ?, condition = ?,description=?, time = ?, status = ?, ftppath =?, actioncode=?, touser=?, username = ?, runmode = ?, mode = ?, freq = ? where id =?",
					new String[] { obj.getEditlable(), obj.getEditconditon(),
							obj.getDesc(), obj.getSchedule(), obj.getStatus(),
							filepath, obj.getAction(), obj.getEditToUser(),
							CallDispatcher.LoginUser, obj.getRunMode(),
							obj.getModeTime(), obj.getFreqMode(), id + "" });
			addAlarm(SingleInstance.mainContext, filepath, obj, alarmId);
		} else {
			db.execSQL(
					"INSERT  into CustomAction(label,condition,description,time,status,ftppath,actioncode,touser,username,mode,freq,runmode)"
							+ " values (?,?,?,?,?,?,?,?,?,?,?,?)",
					new String[] { obj.getEditlable(), obj.getEditconditon(),
							obj.getDesc(), obj.getSchedule(), obj.getStatus(),
							filepath, obj.getAction(), obj.getEditToUser(),
							CallDispatcher.LoginUser, obj.getModeTime(),
							obj.getFreqMode(), obj.getRunMode() });

		}

	}

	private void addAlarm(Context context, String filepath,
			ContactLogicbean obj, int id) {

		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, QuickActionBroadCastReceiver.class);
		intent.putExtra("ftppath", filepath);
		intent.putExtra("fromuser", CallDispatcher.LoginUser);
		intent.putExtra("touser", obj.getEditToUser());
		intent.putExtra("content", obj.getEditlable());
		intent.putExtra("type", obj.getAction());
		intent.putExtra("query", obj.getCondition());
		intent.putExtra("id", id);
		if (obj.getRunMode().equalsIgnoreCase("RP")) {
			int frequency = Integer.parseInt(obj.getFreqMode());
			if (obj.getModeTime().equalsIgnoreCase("minutes")) {
				String[] dateTime = obj.getSchedule().split(" ");
				String hourMinute[] = dateTime[1].split(":");
				int hrs = Integer.parseInt(hourMinute[0]);
				int mins = Integer.parseInt(hourMinute[1]);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY, hrs);
				calendar.set(Calendar.MINUTE, mins);
				int hr = calendar.get(Calendar.HOUR_OF_DAY);
				int min = calendar.get(Calendar.MINUTE);
				Log.i("QAA", "==> hrs " + hr);
				Log.i("QAA", "===> mins " + min);
				PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
						id, intent, 0);
				Log.i("QAA", "id to be put inside map : " + id);
				alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), 1000 * 60 * frequency,
						alarmIntent);
			} else if (obj.getModeTime().equalsIgnoreCase("hour")) {
				String[] dateTime = obj.getSchedule().split(" ");
				String hourMinute[] = dateTime[1].split(":");
				int hrs = Integer.parseInt(hourMinute[0]);
				int mins = Integer.parseInt(hourMinute[1]);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY, hrs);
				calendar.set(Calendar.MINUTE, mins);
				int hr = calendar.get(Calendar.HOUR_OF_DAY);
				int min = calendar.get(Calendar.MINUTE);
				Log.i("QAA", "==> hrs " + hr);
				Log.i("QAA", "===> mins " + min);
				PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
						id, intent, 0);
				Log.i("QAA", "id to be put inside map : " + id);
				alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), 1000 * 60 * 60 * frequency,
						alarmIntent);
			} else if (obj.getModeTime().equalsIgnoreCase("day")) {
				String[] dateTime = obj.getSchedule().split(" ");
				String hourMinute[] = dateTime[1].split(":");
				int hrs = Integer.parseInt(hourMinute[0]);
				int mins = Integer.parseInt(hourMinute[1]);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY, hrs);
				calendar.set(Calendar.MINUTE, mins);
				int hr = calendar.get(Calendar.HOUR_OF_DAY);
				int min = calendar.get(Calendar.MINUTE);
				Log.i("QAA", "==> hrs " + hr);
				Log.i("QAA", "===> mins " + min);
				PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
						id, intent, 0);
				alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY
								* frequency, alarmIntent);
			} else if (obj.getModeTime().equalsIgnoreCase("months")) {
				String[] dateTime = obj.getSchedule().split(" ");
				String hourMinute[] = dateTime[1].split(":");
				int hrs = Integer.parseInt(hourMinute[0]);
				int mins = Integer.parseInt(hourMinute[1]);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY, hrs);
				calendar.set(Calendar.MINUTE, mins);
				int hr = calendar.get(Calendar.HOUR_OF_DAY);
				int min = calendar.get(Calendar.MINUTE);
				Log.i("QAA", "==> hrs " + hr);
				Log.i("QAA", "===> mins " + min);
				PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
						id, intent, 0);
				alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(),
						30L * 1440L * 60000L * frequency, alarmIntent);
			}

			else if (obj.getModeTime().equalsIgnoreCase("weeks")) {
				String[] dateTime = obj.getSchedule().split(" ");
				String hourMinute[] = dateTime[1].split(":");
				int hrs = Integer.parseInt(hourMinute[0]);
				int mins = Integer.parseInt(hourMinute[1]);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY, hrs);
				calendar.set(Calendar.MINUTE, mins);
				int hr = calendar.get(Calendar.HOUR_OF_DAY);
				int min = calendar.get(Calendar.MINUTE);
				Log.i("QAA", "==> hrs " + hr);
				Log.i("QAA", "===> mins " + min);
				PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
						id, intent, 0);
				CallDispatcher.quickActionMap.put(id, alarmIntent);
				alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY
								* 7 * frequency, alarmIntent);
			}
		} else if (obj.getRunMode().equalsIgnoreCase("RS")) {
			Alert.writeString(context, Alert.ftppath, filepath);
			Alert.writeString(context, Alert.fromuser, CallDispatcher.LoginUser);
			Alert.writeString(context, Alert.touser, obj.getEditToUser());
			Alert.writeString(context, Alert.content, obj.getEditlable());
			Alert.writeString(context, Alert.type, obj.getAction());
			Alert.writeString(context, Alert.query, obj.getEditconditon());
			Log.i("name", "same");
		}

	}

	@SuppressWarnings("finally")
	public ArrayList<TableColumnsBean> getColumnTypesTbl(String tblname) {

		String[] tablenames = tblname.split(",");

		ArrayList<TableColumnsBean> column_names = new ArrayList<TableColumnsBean>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			if (tablenames.length > 0) {
				for (int i = 0; i < tablenames.length; i++) {
					String tblName = "[" + tablenames[i] + "]";
					ti = db.rawQuery("PRAGMA table_info(" + tblName + ")", null);
					if (ti.moveToFirst()) {
						while (ti.isAfterLast() == false) {
							Log.i("thread ", "Col :" + ti.getString(1));
							Log.i("thread ", "Col type:" + ti.getString(2));
							Log.i("test ", "Col :" + ti.getString(1));
							Log.i("test ", "Col type:" + ti.getString(2));
							TableColumnsBean tBean = new TableColumnsBean();
							if ((ti.getString(1).equalsIgnoreCase("id"))
									|| (ti.getString(1)
											.equalsIgnoreCase("tableid"))
									|| (ti.getString(1)
											.equalsIgnoreCase("recorddate"))
									|| (ti.getString(1)
											.equalsIgnoreCase("status"))) {
								Log.i("Column",
										"Excluded :: " + ti.getString(1));
							} else {
								tBean.setColumnName(ti.getString(1));
								tBean.setColumnType(ti.getString(2));
								column_names.add(tBean);
							}

							ti.moveToNext();

						}

					}

				}
			}

			else {
				String tblName = "[" + tblname + "]";
				ti = db.rawQuery("PRAGMA table_info(" + tblName + ")", null);
				if (ti.moveToFirst()) {
					while (ti.isAfterLast() == false) {
						Log.i("thread ", "Col :" + ti.getString(1));
						Log.i("thread ", "Col type:" + ti.getString(2));
						Log.i("test ", "Col :" + ti.getString(1));
						Log.i("test ", "Col type:" + ti.getString(2));

						TableColumnsBean tBean = new TableColumnsBean();
						tBean.setColumnName(ti.getString(1));
						tBean.setColumnType(ti.getString(2));
						column_names.add(tBean);

						ti.moveToNext();

					}

				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<String> getablename() {
		ArrayList<String> column_names = new ArrayList<String>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("SELECT tablename FROM formslookup", null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {

					for (int i = 0; i < ti.getColumnCount(); i++) {
						column_names.add(ti.getString(i));
					}

					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}

	}

	public ArrayList<ContactLogicbean> getQucikActionList() {
		Cursor cur = null;
		String user = CallDispatcher.LoginUser;
		ArrayList<ContactLogicbean> quickActionList = new ArrayList<ContactLogicbean>();
		Log.e("banudebug", this.getClass().getName() + " Login User " + user);
		try {
			cur = db.rawQuery(
					"select id,label,condition,time,status,ftppath,actioncode,touser,username,mode,freq,runmode,description from CustomAction where username=?",
					new String[] { user });
			while (cur.moveToNext()) {

				ContactLogicbean blBean = QuickActionBuilder
						.getSecLogicbean(user, cur.getString(6),
								cur.getInt(0));

				blBean.setLabel(cur.getString(1));
				blBean.setCondition(cur.getString(2));
				blBean.setStatus(cur.getString(4));
				blBean.setFtpPath(cur.getString(5));
				blBean.setTo(cur.getString(7));
				blBean.setFromuser(cur.getString(8));
				blBean.setTimemode(cur.getString(9));
				blBean.setFrequncy(cur.getString(10));
				blBean.setDesc(cur.getString(12));
				blBean.setNew(false);
				blBean.setDirty(false);
				quickActionList.add(blBean);
			}

		} catch (Exception e) {
			Log.e("banudebug",
					this.getClass().getName() + " BLDatas " + e.getMessage());
			e.printStackTrace();

		}
		return quickActionList;

	}

	public String getRunMode(int id) {
		Cursor cur = null;
		String runMod = "";
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			cur = db.rawQuery("SELECT runmode FROM CustomAction where Id = '"
					+ id + "'", null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				runMod = cur.getString(0);
				cur.moveToNext();
			}
			cur.close();
			return runMod;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		} finally {
			if (cur != null)
				cur.close();

		}
	}

	@SuppressWarnings("finally")
	public boolean createFormTableQA(ArrayList<String> fields, String tbl_name,
			ArrayList<String> type) {
		boolean result = false;
		try {
			String qrystring = "Id INTEGER PRIMARY KEY AUTOINCREMENT,tableid nvarchar(20)";
			for (int i = 0; i < fields.size(); i++) {
				qrystring = qrystring + "," + fields.get(i) + " "
						+ type.get(i).trim();
			}
			String table = "[" + tbl_name + "]";
			String qry = "create table if not exists " + table + "("
					+ qrystring + ")";

			if (ExecuteQuery(qry)) {
				result = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			return result;
		}

	}

	public HashMap<String, String> getNoteListForPicker(String strQuery) {

		HashMap<String, String> hsNotes = new HashMap<String, String>();

		Cursor cur = null;
		try {

			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			cur = db.rawQuery(strQuery, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {

				hsNotes.put("" + cur.getInt(0), cur.getString(1));

				cur.moveToNext();
			}

		} catch (Exception e) {
		} finally {
			if (cur != null) {
				cur.close();
			}

		}

		return hsNotes;
	}

	public Boolean gettime(String time, Context context) {
		Cursor cur = null;
		Boolean success = false;
		String label = null;
		String timemode = null;
		String times = null;
		int frequency = 0;
		String strGetQry = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			strGetQry = "select * from CustomAction where ( (strftime('%Y-%m-%d %H:%M','"
					+ time
					+ "') > strftime('%Y-%m-%d %H:%M',time)) or (strftime('%Y-%m-%d %H:%M','"
					+ time + "') = strftime('%Y-%m-%d %H:%M',time))) ";

			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("name", "length" + String.valueOf(len));
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				int id = cur.getInt(0);
				label = cur.getString(0);
				timemode = cur.getString(10);
				if (cur.getString(11) != null)
					frequency = Integer.parseInt(cur.getString(11));
				Log.i("QAA", "==> " + timemode);
				Log.i("QAA", "===> " + frequency);
				ReminderDetail response = new ReminderDetail();
				response.setLabelID(id);
				times = cur.getString(4).trim();
				if (time.equals(times) && id > 0) {
					if (cur.getString(12).toString().equalsIgnoreCase("RP")) {
						AlarmManager alarmMgr = (AlarmManager) SingleInstance.mainContext
								.getSystemService(Context.ALARM_SERVICE);
						Intent intent = new Intent(SingleInstance.mainContext,
								QuickActionBroadCastReceiver.class);
						intent.putExtra("ftppath", cur.getString(6));
						intent.putExtra("fromuser", cur.getString(9));
						intent.putExtra("touser", cur.getString(8));
						intent.putExtra("content", cur.getString(1));
						intent.putExtra("type", cur.getString(7));
						intent.putExtra("query", cur.getString(2));
						intent.putExtra("id", id);
						if (timemode.equalsIgnoreCase("minutes")) {
							String[] dateTime = times.split(" ");
							String hourMinute[] = dateTime[1].split(":");
							int hrs = Integer.parseInt(hourMinute[0]);
							int mins = Integer.parseInt(hourMinute[1]);
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(System.currentTimeMillis());
							calendar.set(Calendar.HOUR_OF_DAY, hrs);
							calendar.set(Calendar.MINUTE, mins);
							int hr = calendar.get(Calendar.HOUR_OF_DAY);
							int min = calendar.get(Calendar.MINUTE);
							Log.i("QAA", "==> hrs " + hr);
							Log.i("QAA", "===> mins " + min);
							PendingIntent alarmIntent = PendingIntent
									.getBroadcast(SingleInstance.mainContext,
											id, intent, 0);
							Log.i("QAA", "id to be put inside map : " + id);
							alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
									calendar.getTimeInMillis(),
									1000 * 60 * frequency, alarmIntent);
						} else if (timemode.equalsIgnoreCase("hour")) {
							String[] dateTime = times.split(" ");
							String hourMinute[] = dateTime[1].split(":");
							int hrs = Integer.parseInt(hourMinute[0]);
							int mins = Integer.parseInt(hourMinute[1]);
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(System.currentTimeMillis());
							calendar.set(Calendar.HOUR_OF_DAY, hrs);
							calendar.set(Calendar.MINUTE, mins);
							int hr = calendar.get(Calendar.HOUR_OF_DAY);
							int min = calendar.get(Calendar.MINUTE);
							Log.i("QAA", "==> hrs " + hr);
							Log.i("QAA", "===> mins " + min);
							PendingIntent alarmIntent = PendingIntent
									.getBroadcast(SingleInstance.mainContext,
											id, intent, 0);
							Log.i("QAA", "id to be put inside map : " + id);
							alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
									calendar.getTimeInMillis(),
									1000 * 60 * 60 * frequency, alarmIntent);
						} else if (timemode.equalsIgnoreCase("day")) {
							String[] dateTime = times.split(" ");
							String hourMinute[] = dateTime[1].split(":");
							int hrs = Integer.parseInt(hourMinute[0]);
							int mins = Integer.parseInt(hourMinute[1]);
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(System.currentTimeMillis());
							calendar.set(Calendar.HOUR_OF_DAY, hrs);
							calendar.set(Calendar.MINUTE, mins);
							int hr = calendar.get(Calendar.HOUR_OF_DAY);
							int min = calendar.get(Calendar.MINUTE);
							Log.i("QAA", "==> hrs " + hr);
							Log.i("QAA", "===> mins " + min);
							PendingIntent alarmIntent = PendingIntent
									.getBroadcast(SingleInstance.mainContext,
											id, intent, 0);
							alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
									calendar.getTimeInMillis(),
									AlarmManager.INTERVAL_DAY * frequency,
									alarmIntent);
						} else if (timemode.equalsIgnoreCase("months")) {
							String[] dateTime = times.split(" ");
							String hourMinute[] = dateTime[1].split(":");
							int hrs = Integer.parseInt(hourMinute[0]);
							int mins = Integer.parseInt(hourMinute[1]);
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(System.currentTimeMillis());
							calendar.set(Calendar.HOUR_OF_DAY, hrs);
							calendar.set(Calendar.MINUTE, mins);
							int hr = calendar.get(Calendar.HOUR_OF_DAY);
							int min = calendar.get(Calendar.MINUTE);
							Log.i("QAA", "==> hrs " + hr);
							Log.i("QAA", "===> mins " + min);
							PendingIntent alarmIntent = PendingIntent
									.getBroadcast(SingleInstance.mainContext,
											id, intent, 0);
							alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
									calendar.getTimeInMillis(),
									30L * 1440L * 60000L * frequency,
									alarmIntent);
						}

						else if (timemode.equalsIgnoreCase("weeks")) {
							String[] dateTime = times.split(" ");
							String hourMinute[] = dateTime[1].split(":");
							int hrs = Integer.parseInt(hourMinute[0]);
							int mins = Integer.parseInt(hourMinute[1]);
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(System.currentTimeMillis());
							calendar.set(Calendar.HOUR_OF_DAY, hrs);
							calendar.set(Calendar.MINUTE, mins);
							int hr = calendar.get(Calendar.HOUR_OF_DAY);
							int min = calendar.get(Calendar.MINUTE);
							Log.i("QAA", "==> hrs " + hr);
							Log.i("QAA", "===> mins " + min);
							PendingIntent alarmIntent = PendingIntent
									.getBroadcast(SingleInstance.mainContext,
											id, intent, 0);
							CallDispatcher.quickActionMap.put(id, alarmIntent);
							alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
									calendar.getTimeInMillis(),
									AlarmManager.INTERVAL_DAY * 7 * frequency,
									alarmIntent);
						}

					} else if (cur.getString(12).toString()
							.equalsIgnoreCase("RS")) {
						Log.i("name", "bean set" + cur.getString(5));
						Alert.writeString(context, Alert.ftppath,
								cur.getString(6));
						Alert.writeString(context, Alert.fromuser,
								cur.getString(9));
						Alert.writeString(context, Alert.touser,
								cur.getString(8));
						Alert.writeString(context, Alert.content,
								cur.getString(1));
						Alert.writeString(context, Alert.type, cur.getString(7));
						Alert.writeString(context, Alert.query,
								cur.getString(2));
						Log.i("QAA", "times equals" + times);
						Log.i("name", "same");
						success = true;
					}
				}
				cur.moveToNext();

				// update(label, timemode, times, frequency);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("QAA", "RETURN error" + e.getMessage());
			success = false;
		} finally {
			if (cur != null)
				cur.close();
		}
		return success;

	}

	public CompleteListBean putDBEntry(int option, String ComponentPath,
			String time, String title, CompleteListBean cmpBean,String fileDesc) {
		try {

			String owner = null;
			if (CallDispatcher.LoginUser == null) {
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(SingleInstance.mainContext);
				owner = sharedPreferences.getString("uname", null);
			} else {
				owner = CallDispatcher.LoginUser;
			}
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(SingleInstance.mainContext);
			String bsstatus = null;
			String bstype = null;
			String bsdirection = null;
			String bsid = null;
			String comment=fileDesc;
			// bsstatus = sharedPreferences.getString("bsstatus", null);
			// bstype = sharedPreferences.getString("bstype", null);
			// bsdirection = sharedPreferences.getString("bsdirection", null);
			// bsid = sharedPreferences.getString("bsid", null);
			if (cmpBean != null && cmpBean.getParent_id() != null) {
				bsstatus = "Received";
				bstype = "Order";
				bsdirection = "In";
				bsid = cmpBean.getParent_id();
			}
			String heading = title;
			if (heading.contains("'")) {
				heading = heading.replace("'", "''");
			}
			ContentValues cv = new ContentValues();
			CompleteListBean clBean = new CompleteListBean();
			String componentType = "";
			String caption = title;

			if (option == 1) {
				componentType = "note";
				caption = heading;
			} else if (option == 2 || option == 3)
				componentType = "photo";
			else if (option == 4)
				componentType = "audio";
			else if (option == 5)
				componentType = "video";
			else if (option == 6)
				componentType = "sketch";
			else if (option == 9)
				componentType = "document";

			cv.put("componenttype", componentType);
			cv.put("componentpath", ComponentPath);
			cv.put("ftppath", "");
			cv.put("componentname", caption);
			cv.put("fromuser", "");
			cv.put("comment", comment);
			cv.put("reminderstatus", 0);
			cv.put("owner", owner);
			cv.put("vanishmode", "");
			cv.put("vanishvalue", "");
			cv.put("receiveddateandtime",
					WebServiceReferences.getNoteCreateTimeForFiles());
			cv.put("reminderdateandtime", "");
			cv.put("viewmode", 1);
			cv.put("reminderzone", "");
			cv.put("reminderresponsetype", "");
			cv.put("bscategory", bstype);
			cv.put("bsstatus", bsstatus);
			cv.put("bsdirection", bsdirection);
			cv.put("parentid", bsid);
			cv.put("username", CallDispatcher.LoginUser);
			int id = insertComponent(cv);
			clBean.setComponentId(id);
			clBean.setcomponentType(componentType);
			clBean.setContentPath(ComponentPath);
			clBean.setContentName(title);
			clBean.setDateAndTime(time);
			clBean.setIsresponsed("0");
			callDisp.cmp = clBean;
			return clBean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public int insertPermission(ContentValues cv) {
		if (!db.isOpen())
			openDatabase();
		int row_id = (int) db.insert("setpermission", null, cv);
		return row_id;
	}

	public int updatePermission(ContentValues cv, String where) {
		if (!db.isOpen())
			openDatabase();
		int row_id = (int) db.update("setpermission", cv, where, null);
		return row_id;
	}

	public String getMaxDateofPermission(String userId) {
		String dateTime = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			SQLiteStatement stmt = db
					.compileStatement("SELECT MAX(record_time) FROM setpermission");

			dateTime = (String) stmt.simpleQueryForString();
		} catch (Exception e) {
			Log.e("clone",
					" getMaxDateofPermission message== >" + e.getMessage());
		}
		return dateTime;

	}

	public boolean userChatting(String buddy) {
		Cursor cur = null;
		try {
			if (!db.isOpen())
				openDatabase();

			String Query = "select componentpath from component where comment='"
					+ buddy + "' and Reminderstatus='0' and componentType='IM'";
			cur = db.rawQuery(Query, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				return true;

			}
		} catch (Exception e) {
			Log.e("db", "getVTabProperties:" + e.getMessage());
		} finally {
			if (cur != null) {
				cur.close();
			}

		}

		return false;
	}

	public String getValueFromQuery(String Query) {

		String strResponse = "NoData";
		Cursor cur = null;
		try {

			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery(Query, null);
			cur.moveToFirst();
			strResponse = cur.getString(0);
			Log.e("user", "Query:" + Query);

		} catch (Exception e) {
			Log.e("Exc", "getValueFromQuery:" + e.getMessage());
			Log.e("user", "Query:" + Query);
		} finally {
			if (cur != null) {
				cur.close();
			}

		}

		return strResponse;
	}

	@SuppressWarnings("finally")
	public String getFtppath(String signalid, String sessionid) {
		String ftppath = null;
		Cursor cur = null;
		try {

			String qry = "select * from MMChat where sessionid = '" + sessionid
					+ "' and signalid='" + signalid + "'";
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery(qry, null);
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				if (cur.getString(3).trim().length() != 0) {
					ftppath = cur.getString(3);
				}
				cur.moveToNext();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		finally {
			if (cur != null) {
				cur.close();
			}

			return ftppath;
		}
	}

	@SuppressWarnings("finally")
	public UserSettingsBean getUserSettings() {
		UserSettingsBean u_sett = null;
		Cursor cur = null;
		try {
			String qry = "select settings,service,value1,value2,remoteaddress from Settings where username='"
					+ CallDispatcher.LoginUser + "'";
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery(qry, null);
			cur.moveToFirst();
			if (cur.getCount() > 0) {
				u_sett = new UserSettingsBean();
			}
			while (cur.isAfterLast() == false) {
				if (cur.getString(0).equalsIgnoreCase("autoplay")) {
					u_sett.setAutoplayService(cur.getString(1));
				} else if (cur.getString(0).equalsIgnoreCase("sharetone")) {
					u_sett.setSharetoneService(cur.getString(1));
					u_sett.setShareToneValue1(cur.getString(2));
				} else if (cur.getString(0).equalsIgnoreCase("locationset")) {
					u_sett.setLocationservEnabled(cur.getString(1));
				} else if (cur.getString(0).equalsIgnoreCase("locationservice")) {
					u_sett.setLocationService(cur.getString(1));
					u_sett.setLcationValue1(cur.getString(2));
					u_sett.setLocationValue2(cur.getString(3));
					u_sett.setgeoRemoteAddree(cur.getString(4));
				}
				cur.moveToNext();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			/*
			 * if(db.isOpen()) db.close();
			 */
			return u_sett;
		}
	}

	@SuppressWarnings("finally")
	public String getServiceId(String ServiceName) {
		String service_id = null;
		Cursor cur = null;
		try {
			String qry = "select serviceid from UserServices where servicename='"
					+ ServiceName + "'";
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery(qry, null);
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				service_id = cur.getString(0);
				cur.moveToNext();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			/*
			 * if(db.isOpen()) db.close();
			 */
			return service_id;
		}
	}

	@SuppressWarnings("finally")
	public String getServiceName(String service_id) {

		String service_name = null;
		Cursor cur = null;
		try {
			String qry = "select servicename from UserServices where serviceid='"
					+ service_id + "'";
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery(qry, null);
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				service_name = cur.getString(0);
				cur.moveToNext();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
			/*
			 * if(db.isOpen()) db.close();
			 */
			return service_name;
		}

	}

	public void insertRecordsonMasterTable() {
		try {
			Log.i("thread", "came to insertRecords.......");

			String querry = null;
			if (!db.isOpen())
				openDatabase();

			Cursor cur5 = db.rawQuery("SELECT * FROM UserServices", null);
			if (!cur5.moveToNext()) {
				Log.i("thread",
						"came to insertRecords.......+user_services master");
				querry = "insert into UserServices(servicename,serviceid)values('Audio Call','101')";
				db.execSQL(querry);
				querry = "insert into UserServices(servicename,serviceid)values('Video Call','102')";
				db.execSQL(querry);
				querry = "insert into UserServices(servicename,serviceid)values('Audio Unicast','103')";
				db.execSQL(querry);
				querry = "insert into UserServices(servicename,serviceid)values('Video Unicast','104')";
				db.execSQL(querry);
				querry = "insert into UserServices(servicename,serviceid)values('Share Text Note','105')";
				db.execSQL(querry);
				querry = "insert into UserServices(servicename,serviceid)values('Share Audio Note','106')";
				db.execSQL(querry);
				querry = "insert into UserServices(servicename,serviceid)values('Share Video Note','107')";
				db.execSQL(querry);
				querry = "insert into UserServices(servicename,serviceid)values('Share Photo Note','108')";
				db.execSQL(querry);
				querry = "insert into UserServices(servicename,serviceid)values('Forward','109')";
				db.execSQL(querry);
				querry = "insert into UserServices(servicename,serviceid)values('Redirect to url','110')";
				db.execSQL(querry);
				querry = "insert into UserServices(servicename,serviceid)values('note','12')";
				db.execSQL(querry);
				querry = "insert into UserServices(servicename,serviceid)values('audio','10')";
				db.execSQL(querry);
				querry = "insert into UserServices(servicename,serviceid)values('video','11')";
				db.execSQL(querry);
				querry = "insert into UserServices(servicename,serviceid)values('image','13')";
				db.execSQL(querry);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.i("thread",
					"came to insertRecords.......+conditio master"
							+ e.toString());
		} finally {

		}
	}

	public int getUnreadorderSize(String username) {
		int size = 0;
		Cursor cur = null;
		try {
			if (!db.isOpen())
				openDatabase();

			if (username != null)
				cur = db.rawQuery(
						"SELECT * FROM component where parentid IS not NULL and viewmode='0' and owner='"
								+ username + "'", null);
			else {
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(SingleInstance.mainContext);
				username = sharedPreferences.getString("uname", null);
				if (username != null)
					cur = db.rawQuery(
							"SELECT * FROM component where parentid IS not NULL viewmode='0' and owner='"
									+ username + "'", null);
				else
					cur = db.rawQuery(
							"SELECT * FROM component where parentid IS not NULL viewmode='0'",
							null);

			}
			if (cur != null) {
				size = cur.getCount();
				cur.moveToFirst();
				while (cur.isAfterLast() == false) {
					CompleteListBean clPro = new CompleteListBean();
					clPro.setComponentId(cur.getInt(0));
					clPro.setcomponentType(cur.getString(1));
					clPro.setContentPath(cur.getString(2));
					clPro.setFtpPath(cur.getString(3));
					clPro.setContentName(cur.getString(4));
					if (cur.getString(5) != null)
						clPro.setFromUser(cur.getString(5));

					clPro.setContent(cur.getString(6));
					clPro.setOwner(cur.getString(8));
					if (cur.getString(9) != null)
						clPro.setVanishMode(cur.getString(9));

					clPro.setVanishValue(cur.getString(10));
					clPro.setDateAndTime(cur.getString(11));
					if (cur.getString(12) != null)
						clPro.setReminderTime(cur.getString(12));
					clPro.setViewMode(cur.getInt(13));
					clPro.setReminderZone(cur.getString(11));
					clPro.setResponseType(cur.getString(15));
					if (clPro.getcomponentType().equals("note")) {
						if (clPro.getContentName().trim().length() >= 12) {
							clPro.setContentName(clPro.getContentName()
									.substring(0, 9));
						} else {
							clPro.setContentName(clPro.getContentName()
									.substring(
											0,
											clPro.getContentName().trim()
													.length()));
						}
					}
					cur.moveToNext();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (cur != null)
				cur.close();

			return size;
		}
	}

	public int getUnreadnotesSize(String username) {
		int size = 0;
		Cursor cur = null;
		try {
			if (!db.isOpen())
				openDatabase();

			if (username != null)
				cur = db.rawQuery(
						"SELECT * FROM component where (parentid IS NULL or parentid='') and viewmode='0' and owner='"
								+ username + "'", null);
			else {
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(SingleInstance.mainContext);
				username = sharedPreferences.getString("uname", null);
				if (username != null)
					cur = db.rawQuery(
							"SELECT * FROM component where (parentid IS NULL or parentid='') and viewmode='0' and owner='"
									+ username + "'", null);
				else
					cur = db.rawQuery(
							"SELECT * FROM component where (parentid IS NULL or parentid='') and viewmode='0'",
							null);

			}
			if (cur != null) {
				size = cur.getCount();
				cur.moveToFirst();
				while (cur.isAfterLast() == false) {
					CompleteListBean clPro = new CompleteListBean();
					clPro.setComponentId(cur.getInt(0));
					clPro.setcomponentType(cur.getString(1));
					clPro.setContentPath(cur.getString(2));
					clPro.setFtpPath(cur.getString(3));
					clPro.setContentName(cur.getString(4));
					if (cur.getString(5) != null)
						clPro.setFromUser(cur.getString(5));

					clPro.setContent(cur.getString(6));
					clPro.setOwner(cur.getString(8));
					if (cur.getString(9) != null)
						clPro.setVanishMode(cur.getString(9));

					clPro.setVanishValue(cur.getString(10));
					clPro.setDateAndTime(cur.getString(11));
					if (cur.getString(12) != null)
						clPro.setReminderTime(cur.getString(12));
					clPro.setViewMode(cur.getInt(13));
					clPro.setReminderZone(cur.getString(11));
					clPro.setResponseType(cur.getString(15));
					if (clPro.getcomponentType().equals("note")) {
						if (clPro.getContentName().trim().length() >= 12) {
							clPro.setContentName(clPro.getContentName()
									.substring(0, 9));
						} else {
							clPro.setContentName(clPro.getContentName()
									.substring(
											0,
											clPro.getContentName().trim()
													.length()));
						}
					}
					cur.moveToNext();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (cur != null)
				cur.close();

			return size;
		}
	}

	public void deleteAllshares(String qry) {
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			db.execSQL(qry);
		} catch (Exception e) {
			Log.e("clone", " deleteall shares message== >" + e.getMessage());
		}
	}

	public int getDownloadStatus(String id) {
		int status = 5;
		Cursor cur = null;
		try {
			if (!db.isOpen())
				openDatabase();
			cur = db.rawQuery(
					"SELECT status FROM offlinecallpendingclones where id='"
							+ id + "'", null);
			if (cur != null) {
				cur.moveToFirst();
				while (cur.isAfterLast() == false) {
					status = cur.getInt(0);
					cur.moveToNext();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	public Set<String> getSessionId(String userName) {
		Cursor cur = null;
		Set<String> sessionIdList = new HashSet<String>();
		try {
			if (!db.isOpen())
				openDatabase();
			cur = db.rawQuery(
					"select componentpath from component where fromuser='"
							+ userName + "' and componenttype='IM'", null);
			if (cur != null) {
				cur.moveToFirst();
				while (cur.isAfterLast() == false) {
					if (cur.getString(0) != null
							&& cur.getString(0).length() > 0) {
						sessionIdList.add(cur.getString(0));
					}
					cur.moveToNext();
				}
			}
		} catch (Exception e) {

		}
		return sessionIdList;
	}

	public int updateFormsRecordCount(ContentValues cv, String condition) {

		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("formslookup", cv, condition, null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}

	}

	public void saveOrUpdateProfileField(FieldTemplateBean fieldTemplate) {
		ContentValues cv = new ContentValues();
		if (fieldTemplate.getFieldId() != null
				&& fieldTemplate.getFieldId().length() > 0)
			cv.put("fieldid", fieldTemplate.getFieldId());
		cv.put("userid", CallDispatcher.LoginUser);
		if (fieldTemplate.getFiledvalue() != null
				&& fieldTemplate.getFiledvalue().length() > 0)
			cv.put("fieldvalue", fieldTemplate.getFiledvalue());
		cv.put("createddate", fieldTemplate.getCreatedDate());
		cv.put("modifieddate", fieldTemplate.getModifiedDate());
		cv.put("flag", fieldTemplate.getStatus());

		if (isRecordExists("select * from profilefieldvalues where fieldid="
				+ fieldTemplate.getFieldId() + " and userid='"
				+ CallDispatcher.LoginUser + "'")) {
			updateProfileFieldValues(cv,
					"fieldid=" + fieldTemplate.getFieldId() + " and userid='"
							+ CallDispatcher.LoginUser + "'");
		} else {
			insertProfileFieldValues(cv);
		}
	}

	public int saveOrUpdateGroup(GroupBean groupBean) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			ContentValues cv = new ContentValues();
			if (groupBean.getGroupId() != null)
				cv.put("groupid", groupBean.getGroupId());
			if (groupBean.getGroupName() != null)
				cv.put("groupname", groupBean.getGroupName());
			if (groupBean.getOwnerName() != null)
				cv.put("groupowner", groupBean.getOwnerName());
			if (groupBean.getCreatedDate() != null)
				cv.put("createddate", groupBean.getCreatedDate());
			if (groupBean.getModifiedDate() != null)
				cv.put("modifieddate", groupBean.getModifiedDate());
			if (groupBean.getUserName() != null)
				cv.put("username", groupBean.getUserName());
			if (groupBean.getLastMsg() != null)
				cv.put("lastmsg", groupBean.getLastMsg());
			if (groupBean.getGroupdescription() != null)
				cv.put("groupdescription", groupBean.getGroupdescription());
			if (groupBean.getGroupIcon() != null)
				cv.put("groupicon", groupBean.getGroupIcon());
			if (groupBean.getGrouptype() != null)
				cv.put("grouptype", groupBean.getGrouptype());
			else
				cv.put("grouptype", "group");
			if (groupBean.getAdminMember() != null)
				cv.put("adminmembers", groupBean.getAdminMember());
			if (groupBean.getRecentDate() != null
					&& groupBean.getRecentDate().length() > 0
					&& !groupBean.getRecentDate().equalsIgnoreCase("null")) {
				cv.put("recentdate", groupBean.getRecentDate());
			} else {
				DateFormat dateFormat = new SimpleDateFormat(
						"dd-MM-yyyy hh:mm:ss");
				String strDate = dateFormat.format(new Date());
				cv.put("recentdate", strDate);
			}
			cv.put("category", "G");
			if (isRecordExists("select * from grouplist where groupid='"
					+ groupBean.getGroupId() + "'"))
				row_id = updateGroup(cv, "groupid='" + groupBean.getGroupId()
						+ "'");
			else
				row_id = (int) db.insert("grouplist", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	public Vector<GroupBean> getAllGroups(String loginUser,String type) {
		Vector<GroupBean> groupList = new Vector<GroupBean>();
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			Log.i("AAA", "DB list  " );
			String strGetQry = null;
				strGetQry = "select * from grouplist where username='"
						+ loginUser + "'and grouptype='" + type + "'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				GroupBean groupBean = new GroupBean();
				groupBean.setId(cur.getString(0));
				groupBean.setGroupId(cur.getString(1));
				groupBean.setGroupName(cur.getString(2));
				groupBean.setOwnerName(cur.getString(3));
				groupBean.setCreatedDate(cur.getString(4));
				groupBean.setModifiedDate(cur.getString(5));
				groupBean.setUserName(cur.getString(6));
				groupBean.setLastMsg(cur.getString(7));
				groupBean.setCategory(cur.getString(8));
				groupBean.setRecentDate(cur.getString(9));
				groupBean.setGroupdescription(cur.getString(10));
				groupBean.setGroupIcon(cur.getString(11));
				groupBean.setGrouptype(cur.getString(12));
				groupList.add(groupBean);
				Log.i("AAA", "DB list 1" + groupList);
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return groupList;

	}

	public int updateGroup(ContentValues cv, String condition) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("grouplist", cv, condition, null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	public int updateIndividualChat(ContentValues cv, String condition) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("imchat", cv, condition, null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public int deleteOldGroups() {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.delete("grouplist", null, null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}

	}

	public int deleteDuplicateTableName(String tableName) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.delete("[" + tableName, null, null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}

	}

	public void insertautoacceptcalls(UserBean uBean, String flag) {

		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("username", uBean.getBuddyName());
			cv.put("owner", CallDispatcher.LoginUser);
			cv.put("flag", flag);

			db.insert("autoacceptcalls", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

	}

	//

	public void Updateautoacceptcalls(UserBean uBean, String flag) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();

			cv.put("flag", flag);

			String insertQuery = "update autoacceptcalls set flag='" + flag
					+ "' where owner='" + CallDispatcher.LoginUser
					+ "'and username='" + uBean.getBuddyName() + "'";

			ExecuteQuery(insertQuery);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

	}

	public void UpdateComponent(int id, String status) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();

			cv.put("bsstatus", status);

			String insertQuery = "update component set bsstatus='" + status
					+ "' where componentid='" + id + "'";

			ExecuteQuery(insertQuery);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

	}
	public int insertorUpdatTaskDetails(TaskDetailsBean taskBean) {
		Log.i("patientdetails", "insertorUpdatTaskDetails updated String");

		int row = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("groupid", taskBean.getGroupid());
			cv.put("taskid", taskBean.getTaskId());
			cv.put("creatorname", taskBean.getCreatorName());
			cv.put("taskdesc", taskBean.getTaskdesc());
			cv.put("callpatient", taskBean.getPatientname());
			cv.put("duedate", taskBean.getDuedate());
			cv.put("duetime", taskBean.getDuetime());
			cv.put("setreminder", taskBean.getSetreminder());
			cv.put("timetoremind", taskBean.getTimetoremind());
			cv.put("assignmembers", taskBean.getAssignedMembers());
			cv.put("patientid", taskBean.getPatientid());
			cv.put("taskstatus", taskBean.getTaskstatus());


			if (isRecordExists("select * from taskdetails where groupid='"
					+ taskBean.getGroupid() + "'and taskid='" + taskBean.getTaskId() + "'")) {
				Log.i("sss", "UpdateDetails of task ");
				row = updateTaskDetails(cv,
						"groupid='" + taskBean.getGroupid() + "'and taskid='" + taskBean.getTaskId() + "'");
			}
			else {
				Log.i("patientdetails", "inserttaskDetails new");
				row = (int) db.insert("taskdetails", null, cv);
			}
			return row;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return 0;
		}
	}

	public int insertorUpdateGroupMembers(GroupBean groupBean) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("groupid", groupBean.getGroupId());
			cv.put("groupowner", groupBean.getOwnerName());
			cv.put("username", groupBean.getUserName());
			cv.put("active_members", groupBean.getActiveGroupMembers());
			cv.put("inactive_members", groupBean.getInActiveGroupMembers());
			cv.put("createddate", groupBean.getCreatedDate());
			cv.put("modifieddate", groupBean.getModifiedDate());
			cv.put("invitemembers", groupBean.getInviteMembers());
			cv.put("grouptype", groupBean.getGrouptype());

			if (groupBean.getGroupdescription() != null)
				cv.put("groupdescription", groupBean.getGroupdescription());
			if (groupBean.getGroupIcon() != null)
				cv.put("groupicon", groupBean.getGroupIcon());
			if (groupBean.getGrouptype() != null)
				cv.put("grouptype", groupBean.getGrouptype());
			if (groupBean.getAdminMember() != null)
				cv.put("adminmembers", groupBean.getAdminMember());

			if (isRecordExists("select * from groupdetails where groupid="
					+ groupBean.getGroupId()))
				row_id = updateGroupMembers(cv,
						"groupid=" + groupBean.getGroupId());
			else
				row_id = (int) db.insert("groupdetails", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public int updateTaskDetails(ContentValues cv, String condition) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("taskdetails", cv, condition, null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public int updateGroupMembers(ContentValues cv, String condition) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("groupdetails", cv, condition, null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	public GroupBean getGroup(String query) {
		GroupBean groupBean = null;
		Cursor cur = null;
		try {
			if (db.isOpen()) {
			} else {
				openDatabase();
			}
			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				groupBean = new GroupBean();
				groupBean.setId(cur.getString(0));
				groupBean.setGroupId(cur.getString(1));
				groupBean.setGroupName(cur.getString(2));
				groupBean.setOwnerName(cur.getString(3));
				groupBean.setCreatedDate(cur.getString(4));
				groupBean.setModifiedDate(cur.getString(5));
				groupBean.setUserName(cur.getString(6));
				groupBean.setGroupdescription(cur.getString(10));
				groupBean.setGroupIcon(cur.getString(11));
				cur.moveToNext();
			}
			return groupBean;
		} catch (Exception e) {
			Log.e("db", "getGroup:" + e.getMessage());
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}

		}

	}

	public GroupBean getGroupAndMembers(String query) {

		Cursor cur = null;
		GroupBean groupBean = null;
		try {
			if (db.isOpen()) {
			} else {
				openDatabase();
			}
			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				groupBean = new GroupBean();
				groupBean.setId(cur.getString(0));
				groupBean.setGroupId(cur.getString(1));
				groupBean.setActiveGroupMembers(cur.getString(2));
				groupBean.setInActiveGroupMembers(cur.getString(3));
				groupBean.setCreatedDate(cur.getString(4));
				groupBean.setModifiedDate(cur.getString(5));
				groupBean.setOwnerName(cur.getString(6));
				groupBean.setUserName(cur.getString(7));
				groupBean.setGroupdescription(cur.getString(8));
				groupBean.setGroupIcon(cur.getString(9));
				groupBean.setInviteMembers(cur.getString(10));
				groupBean.setGrouptype(cur.getString(11));
				groupBean.setAdminMember(cur.getString(12));
				cur.moveToNext();
			}
			return groupBean;
		} catch (Exception e) {
			Log.e("db", "getGroup:" + e.getMessage());
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}

		}

	}

	public String getGroupMembers(String query) {

		Cursor cur = null;
		String groupMembers = null;
		try {
			if (db.isOpen()) {
			} else {
				openDatabase();
			}
			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				String active_members = cur.getString(2);
				String inActive_members = cur.getString(3);
				groupMembers = cur.getString(2);
				cur.moveToNext();
			}
			return groupMembers;
		} catch (Exception e) {
			Log.e("db", "getGroup:" + e.getMessage());
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}

		}

	}

	public boolean deleteGroupAndMembers(String groupId) {

		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = null;
			if (groupId != null)
				strQuery = "DELETE from grouplist WHERE groupid='" + groupId
						+ "'";
			db.execSQL(strQuery);
			delete = true;
			delete = deleteMembers(groupId);
			deleteMemberDetails(groupId);
			deletePatientDetails(groupId);
			deleteRoleAccessDetails(groupId);
		} catch (Exception e) {
			e.printStackTrace();
			delete = false;
		}
		return delete;

	}

	public boolean deleteMembers(String groupId) {

		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = null;
			if (groupId != null)
				strQuery = "DELETE from groupdetails WHERE groupid='" + groupId
						+ "'";
			db.execSQL(strQuery);
			delete = true;
		} catch (Exception e) {
			e.printStackTrace();
			delete = false;
		}
		return delete;

	}
	public boolean deletePatientDetails(String groupId) {

		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery1 = null,strQuery2=null,strQuery3 = null;
			if (groupId != null) {
				strQuery1 = "DELETE  from patientdetails WHERE groupid='" + groupId + "'";
				strQuery2 = "DELETE  from patientdescription WHERE groupid='" + groupId + "'";
				strQuery3 = "DELETE  from patientcomments WHERE groupid='" + groupId + "'";
			}
			db.execSQL(strQuery1);
			db.execSQL(strQuery2);
			db.execSQL(strQuery3);
			delete = true;
		} catch (Exception e) {
			e.printStackTrace();
			delete = false;
		}
		return delete;

	}
	public boolean deleteRoleAccessDetails(String groupId) {

		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();
			String strQuery1 = null,strQuery2=null,strQuery3 = null;
			String strQuery4 = null,strQuery5 = null;
			if (groupId != null) {
				strQuery1 = "DELETE  from rolecommentsview WHERE groupid='" + groupId + "'";
				strQuery2 = "DELETE  from roletransctionmgt WHERE groupid='" + groupId + "'";
				strQuery3 = "DELETE  from roleeditroundignform WHERE groupid='" + groupId + "'";
				strQuery4 = "DELETE  from rolepatientmgt WHERE groupid='" + groupId + "'";
				strQuery5 = "DELETE  from roleaccess WHERE groupid='" + groupId + "'";
			}
			db.execSQL(strQuery1);
			db.execSQL(strQuery2);
			db.execSQL(strQuery3);
			db.execSQL(strQuery4);
			db.execSQL(strQuery5);
			delete = true;
		} catch (Exception e) {
			e.printStackTrace();
			delete = false;
		}
		return delete;

	}

	public void deleteGroupMembers(GroupBean groupBean) {

		String query = "select * from groupdetails where groupid="
				+ groupBean.getGroupId();
		try {
			if (!db.isOpen())
				openDatabase();

			Cursor cur = db.rawQuery(query, null);
			if (cur != null) {
				GroupBean gBean = null;
				cur.moveToFirst();
				while (cur.isAfterLast() == false) {
					gBean = new GroupBean();
					gBean.setId(cur.getString(0));
					gBean.setGroupId(cur.getString(1));
					gBean.setActiveGroupMembers(cur.getString(2));
					gBean.setInActiveGroupMembers(cur.getString(3));
					gBean.setCreatedDate(cur.getString(4));
					gBean.setModifiedDate(cur.getString(5));
					gBean.setOwnerName(cur.getString(6));
					gBean.setUserName(cur.getString(7));
					cur.moveToNext();
				}

				if (gBean != null) {
					String deleteMembers = groupBean.getDeleteGroupMembers();
					if (gBean.getActiveGroupMembers() != null
							&& gBean.getActiveGroupMembers().length() != 0) {
						String[] activeMembers = gBean.getActiveGroupMembers()
								.split(",");
						String tmpMembers = "";
						for (String tmp : activeMembers) {
							if (!deleteMembers.contains(tmp)) {
								tmpMembers = tmpMembers + tmp + ",";
							}
						}
						if (tmpMembers.length() > 0)
							tmpMembers = tmpMembers.substring(0,
									tmpMembers.length() - 1);
						else
							tmpMembers = null;

						gBean.setActiveGroupMembers(tmpMembers);
					}

					if (gBean.getInActiveGroupMembers() != null
							&& gBean.getInActiveGroupMembers().length() != 0) {
						String[] activeMembers = gBean
								.getInActiveGroupMembers().split(",");
						String tmpMembers = "";
						for (String tmp : activeMembers) {
							if (!deleteMembers.contains(tmp)) {
								tmpMembers = tmpMembers + tmp + ",";
							}
						}
						if (tmpMembers.length() > 0)
							tmpMembers = tmpMembers.substring(0,
									tmpMembers.length() - 1);
						else
							tmpMembers = null;

						gBean.setInActiveGroupMembers(tmpMembers);
					}

					insertorUpdateGroupMembers(gBean);
				}
				cur.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public int updateChatReply(GroupChatBean groupChatBean) {
        int row = 0;
        try {
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("reply", groupChatBean.getSubCategory());
            if (isRecordExists("select * from chat where parentid='"
                    + groupChatBean.getParentId() + "'")) {
                row = (int) db.update("chat", cv,
                        "parentid='" + groupChatBean.getParentId() + "'", null);
            }
            return row;
        } catch (Exception e) {
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return 0;
        }

    }
    public int updategroupChatPrivate(GroupChatBean groupChatBean) {
        int row = 0;
        try {
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("reply", "private");
            if (isRecordExists("select * from chat where signalid='"
                    + groupChatBean.getSignalid() + "'")) {
                row = (int) db.update("chat", cv,
                        "signalid='" + groupChatBean.getSignalid() + "'", null);
            }
            return row;
        } catch (Exception e) {
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return 0;
        }

    }
    public int updateChatReplied(GroupChatBean groupChatBean) {
        int row = 0;
        try {
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("replied", "reply");
            if (isRecordExists("select * from chat where parentid='"
                    + groupChatBean.getParentId() + "'")) {
                row = (int) db.update("chat", cv,
                        "parentid='" + groupChatBean.getParentId() + "'", null);
            }
            return row;
        } catch (Exception e) {
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return 0;
        }

    }


	public int updatePrivateReply(GroupChatBean groupChatBean) {
		int row = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("reply", "GPRB_R");
			if (isRecordExists("select * from chat where signalid='"
					+ groupChatBean.getSignalid() + "'")) {
				row = (int) db.update("chat", cv,
						"signalid='" + groupChatBean.getSignalid() + "'", null);
			}
			return row;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return 0;
		}

	}


    public int pidChatReply(GroupChatBean groupChatBean) {
        int row = 0;
        try {
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("reply", "1");
            if (isRecordExists("select * from chat where parentid='"
                    + groupChatBean.getParentId() + "'")) {
                row = 1;
            }
            return row;
        } catch (Exception e) {
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return 0;
        }

    }
	public int insertGroupChat(GroupChatBean groupChatBean) {
		int row = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("category", groupChatBean.getCategory());
			cv.put("subcategory", groupChatBean.getSubCategory());
			cv.put("groupid", groupChatBean.getGroupId());
			cv.put("mimetype", groupChatBean.getMimetype());
			cv.put("fromuser", groupChatBean.getFrom());
			cv.put("touser", groupChatBean.getTo());
			cv.put("message", groupChatBean.getMessage());
			cv.put("privatemembers", groupChatBean.getPrivateMembers());
			cv.put("media", groupChatBean.getMediaName());
			cv.put("ftpusername", groupChatBean.getFtpUsername());
			cv.put("ftppassword", groupChatBean.getFtpPassword());
			cv.put("sessionid", groupChatBean.getSessionid());
			cv.put("signalid", groupChatBean.getSignalid());
			cv.put("senttime", groupChatBean.getSenttime());
			cv.put("senttimezone", groupChatBean.getSenttimez());
			cv.put("username", groupChatBean.getUsername());
			cv.put("parentid", groupChatBean.getParentId());
			cv.put("remindertime", groupChatBean.getReminderTime());
			cv.put("status", groupChatBean.getStatus());
            cv.put("unreadstatus", groupChatBean.getUnreadStatus());
            cv.put("thumb", groupChatBean.getThumb());
            cv.put("unview", groupChatBean.getUnview());
            cv.put("reply", groupChatBean.getReply());
			cv.put("dateandtime",groupChatBean.getDateandtime());
			if (isRecordExists("select * from chat where signalid='"
					+ groupChatBean.getSignalid() + "'")) {
				row = (int) db.update("chat", cv,
						"signalid='" + groupChatBean.getSignalid() + "'", null);
			} else {
				row = (int) db.insert("chat", null, cv);
			}
			return row;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return 0;
		}

	}

	public int insertorUpdateIndividualChat(GroupBean groupBean) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			ContentValues cv = new ContentValues();
			if (groupBean.getGroupId() != null)
				cv.put("groupid", groupBean.getGroupId());
			if (groupBean.getGroupName() != null)
				cv.put("groupname", groupBean.getGroupName());
			if (groupBean.getOwnerName() != null)
				cv.put("groupowner", groupBean.getOwnerName());
			if (groupBean.getCreatedDate() != null)
				cv.put("createddate", groupBean.getCreatedDate());
			if (groupBean.getModifiedDate() != null)
				cv.put("modifieddate", groupBean.getModifiedDate());
			if (groupBean.getUserName() != null)
				cv.put("username", groupBean.getUserName());
			if (groupBean.getLastMsg() != null)
				cv.put("lastmsg", groupBean.getLastMsg());
			if (groupBean.getRecentDate() != null
					&& groupBean.getRecentDate().length() > 0
					&& !groupBean.getRecentDate().equalsIgnoreCase("null")) {
				cv.put("recentdate", groupBean.getRecentDate());
			} else {
				DateFormat dateFormat = new SimpleDateFormat(
						"dd-MM-yyyy hh:mm:ss");
				String strDate = dateFormat.format(new Date());
				cv.put("recentdate", strDate);
			}
			cv.put("category", "I");
			if (isRecordExists("select * from imchat where groupid='"
					+ groupBean.getGroupId() + "' and username='"
					+ CallDispatcher.LoginUser + "'"))
				row_id = updateIndividualChat(cv,
						"groupid='" + groupBean.getGroupId()
								+ "' and username='" + CallDispatcher.LoginUser
								+ "'");
			else
				row_id = (int) db.insert("imchat", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	public Vector<GroupChatBean> getChatHistory(String username) {
		Vector<GroupChatBean> chatList = new Vector<GroupChatBean>();
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String strGetQry = "";
			strGetQry = "select * from imchat where username='" + username
					+ "'";
			cur = db.rawQuery(strGetQry, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				GroupChatBean chatBean = new GroupChatBean();
				chatBean.setId(cur.getString(0));
				chatBean.setMessage(cur.getString(1));
				chatBean.setMimetype(cur.getString(2));
				// if (chatBean.getMessageType().equalsIgnoreCase("MPP")
				// || chatBean.getMessageType().equalsIgnoreCase("MHP")) {
				// Bitmap bitMap = callDisp.ResizeImage(chatBean.getMessage());
				// bitMap = Bitmap.createScaledBitmap(bitMap, 200, 150, false);
				// chatBean.setProfilePic(bitMap);
				// }
				chatBean.setSenttime(cur.getString(3));
				chatBean.setFrom(cur.getString(4));
				chatBean.setTo(cur.getString(5));
				chatBean.setUsername(cur.getString(6));
				chatBean.setSessionid(cur.getString(8));
				chatBean.setSignalid(cur.getString(9));
				chatList.add(chatBean);
				cur.moveToNext();
			}
			cur.close();

		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return chatList;
	}

	public Vector<ChatBean> getLoadChatHistoryForExchanges() {
		Vector<ChatBean> chatList = new Vector<ChatBean>();
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String strGetQry = "";
			strGetQry = "select distinct groupid from chat where category='I' and username='"
					+ CallDispatcher.LoginUser + "'";
			cur = db.rawQuery(strGetQry, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				String user = cur.getString(0).replace(
						CallDispatcher.LoginUser, "");
				ChatBean chatBean = new ChatBean();
				chatBean.setFromUser(user);
				String path = Environment.getExternalStorageDirectory()
						+ "/COMMedia/profile/" + user + ".png";
				chatBean.setProfilePath(path);
				chatList.add(chatBean);
				cur.moveToNext();
			}
			cur.close();

		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return chatList;
	}

	public Vector<GroupChatBean> getGroupChatHistory(String groupid,
			boolean group) {
		Vector<GroupChatBean> groupChatList = new Vector<GroupChatBean>();
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String query = "";
			if (group)
				query = "select * from chat where groupid='" + groupid
						+ "' and username ='" + CallDispatcher.LoginUser + "'";
			Log.i("group123", "query : " + query);
			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				GroupChatBean groupChatBean = new GroupChatBean();
				groupChatBean.setId(cur.getString(0));
				groupChatBean.setCategory(cur.getString(1));
				groupChatBean.setSubCategory(cur.getString(2));
				groupChatBean.setGroupId(cur.getString(3));
				groupChatBean.setUsername(cur.getString(4));
				groupChatBean.setMimetype(cur.getString(5));
				groupChatBean.setFrom(cur.getString(6));
				groupChatBean.setTo(cur.getString(7));
				groupChatBean.setMessage(cur.getString(8));
				groupChatBean.setMediaName(cur.getString(9));
				groupChatBean.setFtpUsername(cur.getString(10));
				groupChatBean.setFtpPassword(cur.getString(11));
				groupChatBean.setSessionid(cur.getString(12));
				groupChatBean.setSignalid(cur.getString(13));
				groupChatBean.setSenttime(cur.getString(14));
				groupChatBean.setSenttimez(cur.getString(15));
				groupChatBean.setPrivateMembers(cur.getString(16));
				groupChatBean.setParentId(cur.getString(17));
				groupChatBean.setReminderTime(cur.getString(18));
				groupChatBean.setStatus(cur.getInt(19));
                groupChatBean.setUnreadStatus(cur.getInt(20));
                groupChatBean.setThumb(cur.getInt(21));
                groupChatBean.setReply(cur.getString(22));
                groupChatBean.setReplied(cur.getString(23));
				if(cur.getString(26) != null && cur.getString(26).equalsIgnoreCase("join")) {
					groupChatBean.setIsJoin(true);
				} else {
					groupChatBean.setIsJoin(false);
				}
                groupChatBean.setWithdrawn(cur.getString(27));
				if(cur.getString(28)!=null)
				groupChatBean.setSenderWithdraw(cur.getString(28));
				groupChatBean.setDateandtime(cur.getString(29));

				groupChatList.add(groupChatBean);
				cur.moveToNext();
				Log.i("group123", "chat list size in db: " + groupChatBean.getMessage());
			}
			Log.i("group123", "chat list size in db: " + groupChatList.size());
			cur.close();
		} catch (Exception e) {
			e.printStackTrace();
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return groupChatList;
	}

	// public Vector<GroupChatBean> loadHistoryGC(Vector<GroupChatBean> gcList)
	// {
	// Vector<GroupChatBean> groupChatList = new Vector<GroupChatBean>();
	// for (GroupChatBean gcBean : gcList) {
	// if (gcBean.getParentId() == null
	// || gcBean.getParentId().length() == 0
	// || gcBean.getParentId().equalsIgnoreCase("null")) {
	// groupChatList.add(gcBean);
	// } else{
	// Vector<GroupChatBean> childList = getDeadLineGroupChatBean(gcBean
	// .getSignalid());
	// if (childList != null && childList.size() > 0) {
	// groupChatList.addAll(childList);
	// }
	//
	// }
	//
	// }
	// return groupChatList;
	//
	// }

	public Vector<GroupChatBean> loadHistoryGC(Vector<GroupChatBean> gcList) {

		HashMap<String, Vector<GroupChatBean>> groupChatListMap = new HashMap<String, Vector<GroupChatBean>>();
		HashMap<String, GroupChatBean> deadLineGroupChatMap = new HashMap<String, GroupChatBean>();

		for (int i = 0; i < gcList.size(); i++) {
			GroupChatBean gcBean = gcList.get(i);
			if (gcBean.getParentId() == null
					|| gcBean.getParentId().length() == 0
					|| gcBean.getParentId().equalsIgnoreCase("null")) {
			} else {
				//
				if (gcBean.getSubCategory().equalsIgnoreCase("GRB")||gcBean.getSubCategory().equalsIgnoreCase("grb_r")) {
					Vector<GroupChatBean> tempList = groupChatListMap
							.get(gcBean.getParentId());
					if (tempList == null) {
						Vector<GroupChatBean> tempGCList = new Vector<GroupChatBean>();
						tempGCList.add(gcBean);
						groupChatListMap.put(gcBean.getParentId(), tempGCList);
					} else {
						tempList.add(gcBean);
					}
				} else if (gcBean.getSubCategory().equalsIgnoreCase("GD")
						|| gcBean.getSubCategory().equalsIgnoreCase("GDI")) {
					GroupChatBean groupChatBean = deadLineGroupChatMap
							.get(gcBean.getParentId());
					if (groupChatBean == null) {
						deadLineGroupChatMap.put(gcBean.getParentId(), gcBean);
					} else {
						String msg = groupChatBean.getMessage() + "\nStatus : "
								+ gcBean.getMessage();
						groupChatBean.setMessage(msg);
					}
				}
			}
		}

		final Vector<GroupChatBean> finalGCList = new Vector<GroupChatBean>();
		HashSet<String> groupChatSet = new HashSet<String>();
		HashSet<String> deadLinegroupChatSet = new HashSet<String>();
		for (GroupChatBean gcBean : gcList) {
			if (gcBean.getParentId() == null
					|| gcBean.getParentId().length() == 0
					|| gcBean.getParentId().equalsIgnoreCase("null")) {
				finalGCList.add(gcBean);
			} else {
				if (gcBean.getSubCategory().equalsIgnoreCase("GRB")||gcBean.getSubCategory().equalsIgnoreCase("grb_r")) {
					//For Reply message add list below
					//start
//					if (!groupChatSet.contains(gcBean.getParentId())) {
//						Vector<GroupChatBean> tempList = groupChatListMap
//								.get(gcBean.getParentId());
//						finalGCList.addAll(tempList);
//						groupChatSet.add(gcBean.getParentId());
//					}
					//End
					//New Code start
					finalGCList.add(gcBean);
					//End
				}else if (gcBean.getSubCategory().equalsIgnoreCase("Gc")) {
                    finalGCList.add(gcBean);
                }else if (gcBean.getSubCategory().equalsIgnoreCase("Gu")) {
                    finalGCList.add(gcBean);
                }else if(gcBean.getSubCategory().equalsIgnoreCase("gp") || gcBean.getSubCategory().equalsIgnoreCase("gp_r")){
					finalGCList.add(gcBean);
				}
				else
                if (gcBean.getSubCategory().equalsIgnoreCase("GD")
						|| gcBean.getSubCategory().equalsIgnoreCase("GDI")) {

					if (!deadLinegroupChatSet.contains(gcBean.getParentId())) {
						GroupChatBean bean = deadLineGroupChatMap.get(gcBean
								.getParentId());
						finalGCList.add(bean);
						deadLinegroupChatSet.add(gcBean.getParentId());
					}

				}
			}
		}
		return finalGCList;
	}

	public Vector<GroupChatBean> getDeadLineGroupChatBean(String signalId) {
		Vector<GroupChatBean> groupChatList = new Vector<GroupChatBean>();
		try {
			if (signalId != null && !signalId.equalsIgnoreCase("null")
					&& signalId.length() > 0) {
				Cursor cur = null;
				if (!db.isOpen())
					openDatabase();
				String query = "select * from chat where parentid='" + signalId
						+ "'";
				cur = db.rawQuery(query, null);
				cur.moveToFirst();
				while (cur.isAfterLast() == false) {
					GroupChatBean groupChatBean = new GroupChatBean();
					groupChatBean.setId(cur.getString(0));
					groupChatBean.setCategory(cur.getString(1));
					groupChatBean.setSubCategory(cur.getString(2));
					groupChatBean.setGroupId(cur.getString(3));
					groupChatBean.setUsername(cur.getString(4));
					groupChatBean.setMimetype(cur.getString(5));
					groupChatBean.setFrom(cur.getString(6));
					groupChatBean.setTo(cur.getString(7));
					groupChatBean.setMessage(cur.getString(8));
					groupChatBean.setMediaName(cur.getString(9));
					groupChatBean.setFtpUsername(cur.getString(10));
					groupChatBean.setFtpPassword(cur.getString(11));
					groupChatBean.setSessionid(cur.getString(12));
					groupChatBean.setSignalid(cur.getString(13));
					groupChatBean.setSenttime(cur.getString(14));
					groupChatBean.setSenttimez(cur.getString(15));
					groupChatBean.setPrivateMembers(cur.getString(16));
					groupChatBean.setParentId(cur.getString(17));
					groupChatList.add(groupChatBean);
					cur.moveToNext();
				}
				cur.close();
			}

		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return groupChatList;

	}

	public Vector<String> getSignalIdList(String groupId) {

		Vector<String> signalIdList = new Vector<String>();
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String query = "select signalid from chat where groupid='"
					+ groupId + "'";
			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				signalIdList.add(cur.getString(0));
				cur.moveToNext();

			}
			cur.close();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return signalIdList;

	}

	public int updateChat(ChatBean chatBean, String signalId) {

		int row = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("message", chatBean.getMessage());
			cv.put("messagetype", chatBean.getMessageType());
			cv.put("timedate", chatBean.getTimeDate());
			cv.put("fromuser", chatBean.getFromUser());
			cv.put("touser", chatBean.getToUser());
            cv.put("username", chatBean.getUsername());
			if (chatBean.isSender())
				cv.put("issender", 1);
			else
				cv.put("issender", 0);
			cv.put("sessionid", chatBean.getSessionId());
			cv.put("signalid", chatBean.getSignalId());
			cv.put("status", chatBean.getStatus());
			row = (int) db.update("imchat", cv,
					"signalid='" + chatBean.getSignalId() + "'", null);
			return row;

		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return 0;

	}

	public int updateGroupChat(int status, String signalId) {
		int row = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("status", status);
			row = (int) db.update("chat", cv, "signalid='" + signalId + "'",
					null);
			return row;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return 0;
		}

	}

	public int updateUnreadMsgForGroupChat(int status, String groupId,
			String username) {
		int row = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("unreadstatus", status);
			row = (int) db.update("chat", cv, "groupid='" + groupId
					+ "' and username='" + username + "'", null);
			return row;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return 0;
		}

	}

	public int getUnreadMsgCount(String username) {

		int count = 0;
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String query = "select * from chat where unreadstatus='0'  and username='" + username + "'";
//			String query = "select distinct groupid from chat where unreadstatus='0' and username='"
//					+ username + "'";
			cur = db.rawQuery(query, null);
			count = cur.getCount();
			cur.close();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return count;

	}

	public int getUnreadMsgCountById(String groupId, String username) {

		int count = 0;
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String query = "select * from chat where unreadstatus='0' and  groupid='"
					+ groupId + "' and username='" + username + "'";
			cur = db.rawQuery(query, null);
			count = cur.getCount();
			cur.close();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return count;

	}

	public int getUnreadMsgCountIndividualChat(String username) {
		int count = 0;
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String query = "select distinct groupid from chat where unreadstatus='0' and  category='I' and username='"
					+ username + "'";
			cur = db.rawQuery(query, null);
			count = cur.getCount();
			cur.close();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return count;

	}

	public boolean deleteGroupChatEntry(String signalId, String username) {

		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = null;
			if (signalId != null)
				strQuery = "DELETE from chat WHERE signalid='" + signalId
						+ "' and username='" + username + "'";
			db.execSQL(strQuery);
			delete = true;
		} catch (Exception e) {
			delete = false;
		}
		return delete;

	}

	public boolean deleteGroupChatEntryLocally(String groupId, String username) {

		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = null;
			if (groupId != null && username != null)
				strQuery = "DELETE from chat WHERE groupid='" + groupId
						+ "' and username='" + username + "'";
			db.execSQL(strQuery);
			delete = true;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			delete = false;
		}
		return delete;

	}

	public boolean deleteGroupChatEntryLeaveGroup(String groupId,
			String username, String fromUser) {

		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = null;
			if (groupId != null && username != null)
				strQuery = "DELETE from chat WHERE groupid='" + groupId
						+ "' and username='" + username + "' and fromuser='"
						+ fromUser + "'";
			db.execSQL(strQuery);
			delete = true;
		} catch (Exception e) {
			delete = false;
		}
		return delete;

	}

	public int insertorUpdateScheduleMsg(GroupChatBean gcBean) {

		int row = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("category", gcBean.getCategory());
			cv.put("subcategory", gcBean.getSubCategory());
			cv.put("groupid", gcBean.getGroupId());
			cv.put("mimetype", gcBean.getMimetype());
			cv.put("fromuser", gcBean.getFrom());
			cv.put("touser", gcBean.getTo());
			cv.put("message", gcBean.getMessage());
			cv.put("privatemembers", gcBean.getPrivateMembers());
			cv.put("media", gcBean.getMediaName());
			cv.put("ftpusername", gcBean.getFtpUsername());
			cv.put("ftppassword", gcBean.getFtpPassword());
			cv.put("sessionid", gcBean.getSessionid());
			cv.put("signalid", gcBean.getSignalid());
			cv.put("senttime", gcBean.getSenttime());
			cv.put("senttimezone", gcBean.getSenttimez());
			cv.put("username", gcBean.getUsername());
			if (isRecordExists("select * from schedulemsg where signalid='"
					+ gcBean.getSignalid() + "'")) {
				row = db.update("schedulemsg", cv,
						"signalid='" + gcBean.getSignalid() + "'", null);
			} else
				row = (int) db.insert("schedulemsg", null, cv);
			return row;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return 0;
		}
	}

	// public GroupChatBean getScheduleMsg(String signalId) {
	// GroupChatBean groupChatBean = null;
	// try {
	// Cursor cur = null;
	// if (!db.isOpen())
	// openDatabase();
	// String query = "select * from schedulemsg where signalid='"
	// + signalId + "'";
	// cur = db.rawQuery(query, null);
	// cur.moveToFirst();
	// while (cur.isAfterLast() == false) {
	// groupChatBean = new GroupChatBean();
	// groupChatBean.setId(cur.getString(0));
	// groupChatBean.setCategory(cur.getString(1));
	// groupChatBean.setSubCategory(cur.getString(2));
	// groupChatBean.setGroupId(cur.getString(3));
	// groupChatBean.setUsername(cur.getString(4));
	// groupChatBean.setMimetype(cur.getString(5));
	// groupChatBean.setFrom(cur.getString(6));
	// groupChatBean.setTo(cur.getString(7));
	// groupChatBean.setMessage(cur.getString(8));
	// groupChatBean.setMediaName(cur.getString(9));
	// groupChatBean.setFtpUsername(cur.getString(10));
	// groupChatBean.setFtpPassword(cur.getString(11));
	// groupChatBean.setSessionid(cur.getString(12));
	// groupChatBean.setSignalid(cur.getString(13));
	// groupChatBean.setSenttime(callDisp.getCurrentDateandTime());
	// groupChatBean.setSenttimez(cur.getString(15));
	// groupChatBean.setPrivateMembers(cur.getString(16));
	// cur.moveToNext();
	// }
	// cur.close();
	// } catch (Exception e) {
	// if (AppReference.isWriteInFile)
	// AppReference.logger.error(e.getMessage(), e);
	// else
	// e.printStackTrace();
	// }
	// return groupChatBean;
	//
	// }

	public boolean deleteScheduleMsg(String signalId) {
		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			if (signalId != null) {
				if (isRecordExists("select * from schedulemsg WHERE signalid='"
						+ signalId + "'")) {
					String strQuery = "DELETE from schedulemsg WHERE signalid='"
							+ signalId + "'";
					db.execSQL(strQuery);
					delete = true;
				}
			}
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);

			delete = false;
		}
		return delete;
	}

	public int insertOrUpdateUploadDownloadStatus(
			UploadDownloadStatusBean uploadDownloadStatusBean) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("username", uploadDownloadStatusBean.getUsername());
			cv.put("media", uploadDownloadStatusBean.getFilename());
			cv.put("ftpusername", uploadDownloadStatusBean.getFtpusername());
			cv.put("ftppassword", uploadDownloadStatusBean.getFtppassword());
			cv.put("status", uploadDownloadStatusBean.getStatus());
			cv.put("operations", uploadDownloadStatusBean.getOperation());
			cv.put("modules", uploadDownloadStatusBean.getModules());
			cv.put("othervalues", uploadDownloadStatusBean.getOthers());

			if (isRecordExists("select * from uploaddownload where media='"
					+ uploadDownloadStatusBean.getFilename() + "'"))
				row_id = (int) db.update("uploaddownload", cv, "media='"
						+ uploadDownloadStatusBean.getFilename() + "'", null);
			else
				row_id = (int) db.insert("uploaddownload", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	public boolean deleteUploadDownloadStatus(String filename) {
		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			if (filename != null) {
				if (isRecordExists("select * from uploaddownload WHERE media='"
						+ filename + "' and status='0'")) {
					String strQuery = "DELETE from uploaddownload WHERE media='"
							+ filename + "' and status='0'";
					db.execSQL(strQuery);
					delete = true;
				}
			}
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);

			delete = false;
		}
		return delete;
	}

	public Vector<UploadDownloadStatusBean> getUploadDownloadList(
			String username) {
		Cursor cur;
		Vector<UploadDownloadStatusBean> uploadDownloadList = new Vector<UploadDownloadStatusBean>();
		try {
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery("select * from uploaddownload where username='"
					+ username + "'", null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				UploadDownloadStatusBean uploadDownloadStatusBean = new UploadDownloadStatusBean();
				uploadDownloadStatusBean.setId(cur.getString(0));
				uploadDownloadStatusBean.setUsername(cur.getString(1));
				uploadDownloadStatusBean.setFilename(cur.getString(2));
				uploadDownloadStatusBean.setFtpusername(cur.getString(3));
				uploadDownloadStatusBean.setFtppassword(cur.getString(4));
				uploadDownloadStatusBean.setStatus(cur.getString(5));
				uploadDownloadStatusBean.setOperation(cur.getString(6));
				uploadDownloadStatusBean.setModules(cur.getString(7));
				uploadDownloadStatusBean.setOthers(cur.getString(8));
				uploadDownloadList.add(uploadDownloadStatusBean);
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return uploadDownloadList;
		}
	}

	public GroupChatBean getGroupChatBean(String signalId) {
		Cursor cur;
		GroupChatBean groupChatBean = null;
		try {
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery("select * from chat where signalid='" + signalId
					+ "'", null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				groupChatBean = new GroupChatBean();
				groupChatBean.setId(cur.getString(0));
				groupChatBean.setCategory(cur.getString(1));
				groupChatBean.setSubCategory(cur.getString(2));
				groupChatBean.setGroupId(cur.getString(3));
				groupChatBean.setUsername(cur.getString(4));
				groupChatBean.setMimetype(cur.getString(5));
				groupChatBean.setFrom(cur.getString(6));
				groupChatBean.setTo(cur.getString(7));
				groupChatBean.setMessage(cur.getString(8));
				groupChatBean.setMediaName(cur.getString(9));
				groupChatBean.setFtpUsername(cur.getString(10));
				groupChatBean.setFtpPassword(cur.getString(11));
				groupChatBean.setSessionid(cur.getString(12));
				groupChatBean.setSignalid(cur.getString(13));
				groupChatBean.setSenttime(cur.getString(14));
				groupChatBean.setSenttimez(cur.getString(15));
				groupChatBean.setPrivateMembers(cur.getString(16));
				groupChatBean.setParentId(cur.getString(17));
				groupChatBean.setReminderTime(cur.getString(18));
				cur.moveToNext();

			}
			cur.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return groupChatBean;
		}
	}

	public Vector<GroupBean> getAllIndividualChat(String username) {
		Vector<GroupBean> chatList = new Vector<GroupBean>();
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String strGetQry = "select * from imchat where username='"
					+ username + "'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("name", "length" + String.valueOf(len));
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				GroupBean groupBean = new GroupBean();
				groupBean.setId(cur.getString(0));
				groupBean.setGroupId(cur.getString(1));
				groupBean.setGroupName(cur.getString(2));
				String path = Environment.getExternalStorageDirectory()
						+ "/COMMedia/profile/"
						+ groupBean.getGroupName().trim() + ".png";
				groupBean.setGroupProfilePic(path);
				groupBean.setOwnerName(cur.getString(3));
				groupBean.setCreatedDate(cur.getString(4));
				groupBean.setModifiedDate(cur.getString(5));
				groupBean.setUserName(cur.getString(6));
				groupBean.setLastMsg(cur.getString(7));
				groupBean.setCategory(cur.getString(8));
				groupBean.setRecentDate(cur.getString(9));
				chatList.add(groupBean);
				cur.moveToNext();
			}
			cur.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chatList;

	}

	public GroupBean getAllIndividualChatByBuddyName(String username,
			String buddyName) {
		GroupBean groupBean = new GroupBean();
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String strGetQry = "select * from imchat where username='"
					+ username + "' and groupid='" + buddyName + "'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("name", "length" + String.valueOf(len));
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				groupBean.setId(cur.getString(0));
				groupBean.setGroupId(cur.getString(1));
				groupBean.setGroupName(cur.getString(2));
				Log.i("gctest",
						"groupBean.getGroupName() :" + groupBean.getGroupName());
				if (groupBean.getGroupName() != null) {
					String path = Environment.getExternalStorageDirectory()
							+ "/COMMedia/profile/"
							+ groupBean.getGroupName().trim() + ".png";
					groupBean.setGroupProfilePic(path);
				}
				groupBean.setOwnerName(cur.getString(3));
				groupBean.setCreatedDate(cur.getString(4));
				groupBean.setModifiedDate(cur.getString(5));
				groupBean.setUserName(cur.getString(6));
				groupBean.setLastMsg(cur.getString(7));
				groupBean.setCategory(cur.getString(8));
				groupBean.setRecentDate(cur.getString(9));
				cur.moveToNext();
			}
			cur.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return groupBean;

	}

	public boolean deleteIndividualChat(String buddyName) {
		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = null;
			if (buddyName != null)
				strQuery = "DELETE from imchat WHERE groupid='" + buddyName
						+ "'";
			db.execSQL(strQuery);
			delete = true;
		} catch (Exception e) {
			delete = false;
		}
		return delete;

	}

	public int insertorUpdateGroupChatSettings(GroupChatPermissionBean gcpBean) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("userid", gcpBean.getUserId());
			cv.put("groupid", gcpBean.getGroupId());
			cv.put("groupowner", gcpBean.getGroupOwner());
			cv.put("groupmember", gcpBean.getGroupMember());
			cv.put("audioconf", gcpBean.getAudioConference());
			cv.put("videoconf", gcpBean.getVideoConference());
			cv.put("audiobroadcast", gcpBean.getAudioBroadcast());
			cv.put("videobroadcast", gcpBean.getVideoBroadcast());
			cv.put("textmessage", gcpBean.getTextMessage());
			cv.put("videomessage", gcpBean.getVideoMessage());
			cv.put("photomessage", gcpBean.getPhotoMessage());
			cv.put("audiomessage", gcpBean.getAudioMessage());
			cv.put("privatemessage", gcpBean.getPrivateMessage());
			cv.put("replybackmessage", gcpBean.getReplyBackMessage());
			cv.put("schedulemessage", gcpBean.getScheduleMessage());
			cv.put("deadlinemessage", gcpBean.getDeadLineMessage());
			cv.put("withdrawn", gcpBean.getWithDrawn());
			cv.put("chat", gcpBean.getChat());
			Log.i("Test", "DBENTRY??????????????"+gcpBean.getChat());
			if (isRecordExists("select * from setgrouppermission where groupmember='"
					+ gcpBean.getGroupMember()
					+ "' and groupid='"
					+ gcpBean.getGroupId()
					+ "'and userid='"
					+ CallDispatcher.LoginUser + "'"))
				row_id = (int) db.update("setgrouppermission", cv,
						"groupmember='" + gcpBean.getGroupMember()
								+ "' and groupid='" + gcpBean.getGroupId()
								+ "' and userid='" + CallDispatcher.LoginUser
								+ "'", null);
			else
				row_id = (int) db.insert("setgrouppermission", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	public boolean isPermittedinGroupChat(String messageType, String groupid,
			String username) {
		Cursor ti = null;
		boolean isexists = true;
		try {
			if (!db.isOpen())
				openDatabase();
			String query = "SELECT * FROM setgrouppermission WHERE "
					+ messageType + "='0' and groupid='" + groupid
					+ "' and groupmember='" + username + "'";
			Log.i("gpermission123", "query : " + query);
			ti = db.rawQuery(query, null);

			if (ti.moveToFirst())
				isexists = false;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return isexists;
		}

	}

	public String getFormFieldBuddyAccessSettings(String formId,
			String attributeId) {
		String permission = null;
		try {
			if (!db.isOpen())
				openDatabase();
			String query = "SELECT buddyaccess FROM formfieldbuddyaccess WHERE formid='"
					+ formId + "' and attributeid='" + attributeId + "'";
			Log.i("gpermission123", "query : " + query);
			Cursor cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				permission = cur.getString(0);
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return permission;

	}

	public String getFormFieldDefaultAccessSettings(String formId,
			String attributeId) {
		String permission = null;
		try {
			if (!db.isOpen())
				openDatabase();
			String query = "SELECT defaultaccess FROM formfielddefaultaccess WHERE formid='"
					+ formId + "' and attributeid='" + attributeId + "'";
			Log.i("gpermission123", "query : " + query);
			Cursor cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				permission = cur.getString(0);
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return permission;

	}

	public Vector<BuddyPermission> getAccesssibleBuddies(String formId,
			String attributeId) {

		Cursor cur;
		Vector<BuddyPermission> buddyAccessList = new Vector<BuddyPermission>();
		try {
			if (!db.isOpen())
				openDatabase();
			String query = "select accessiblebuddy,buddyaccess from formfieldbuddyaccess where formid='"
					+ formId + "' and attributeid='" + attributeId + "'";
			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				BuddyPermission fABean = new BuddyPermission();
				String buddyName = cur.getString(0);
				if (buddyName != null
						&& !buddyName
								.equalsIgnoreCase(CallDispatcher.LoginUser)) {
					fABean.setBuddyName(buddyName);
					String buddyAccess = cur.getString(1);
					if (buddyAccess.equalsIgnoreCase("F1")) {
						fABean.setBuddyAccess("None");
					} else if (buddyAccess.equalsIgnoreCase("F2")) {
						fABean.setBuddyAccess("View");
					} else if (buddyAccess.equalsIgnoreCase("F3")) {
						fABean.setBuddyAccess("Modify");
					}
					buddyAccessList.add(fABean);
					Log.i("formfield123", "buddyaccess list size : "
							+ buddyAccessList.size());
				}
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return buddyAccessList;
		}

	}

	@SuppressWarnings("finally")
	public Vector<FormFieldBean> getFormInfoList(String tblname, String formId) {
		Vector<FormFieldBean> formFieldBeanList = new Vector<FormFieldBean>();
		try {
			if (!db.isOpen())
				openDatabase();
			String formName = tblname + "_" + formId;
			String query = "SELECT column,attributeid FROM forminfo WHERE tablename='"
					+ "[" + formName + "]" + "'";
			Cursor cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				FormFieldBean fieldBean = new FormFieldBean();
				fieldBean.setFieldName(cur.getString(0));
				fieldBean.setAttributeId(cur.getString(1));
				fieldBean.setFormId(formId);
				fieldBean.setDefaultPermissionList(getDefaultFieldAccess(
						formId, fieldBean.getAttributeId()));
				formFieldBeanList.add(fieldBean);
				cur.moveToNext();
			}
			cur.close();

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
		return formFieldBeanList;

	}

	@SuppressWarnings("finally")
	public Vector<DefaultPermission> getDefaultFieldAccess(String formId,
			String attributeId) {
		Vector<DefaultPermission> dList = new Vector<DefaultPermission>();
		try {
			if (!db.isOpen())
				openDatabase();

			String query = "SELECT defaultaccess FROM formfielddefaultaccess WHERE formid='"
					+ formId + "' and attributeid='" + attributeId + "'";
			Cursor cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				DefaultPermission dPermission = new DefaultPermission();
				dPermission.setAttributeId(attributeId);
				String getAccess = cur.getString(0);
				if (getAccess.equalsIgnoreCase("F1")) {
					dPermission.setDefaultPermission("None");
				} else if (getAccess.equalsIgnoreCase("F2")) {
					dPermission.setDefaultPermission("View");
				} else if (getAccess.equalsIgnoreCase("F3")) {
					dPermission.setDefaultPermission("Modify");
				}
				Vector<BuddyPermission> bList = getAccesssibleBuddies(formId,
						attributeId);
				if (bList != null) {
					dPermission.setBuddyPermissionList(bList);
				}
				dList.add(dPermission);
				cur.moveToNext();
			}
			cur.close();

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
		return dList;

	}

	public boolean deleteFormFieldBuddyAccess(String formId,
			String attributeId, String buddyName) {
		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = "DELETE from formfieldbuddyaccess WHERE formid='"
					+ formId + "' and attributeid='" + attributeId
					+ "' and accessiblebuddy='" + buddyName + "'";
			db.execSQL(strQuery);
			delete = true;
		} catch (Exception e) {
			delete = false;
		}
		return delete;
	}

	public int insertBuddyFormFieldAccess(ContentValues cv) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.insert("formfieldbuddyaccess", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			return row_id;
		}
	}

	public int updateBuddyFormFieldAccess(ContentValues cv, String condition) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("formfieldbuddyaccess", cv, condition,
					null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			return row_id;
		}
	}

	public int insertOwnFormFieldAccess(ContentValues cv) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.insert("formfielddefaultaccess", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			return row_id;
		}
	}

	public int updateOwnFormFieldAccess(ContentValues cv, String condition) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("formfielddefaultaccess", cv, condition,
					null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			return row_id;
		}
	}

	public int saveOrUpdateOwnerFormField(String formId, String attributeId,
			String defaultAccess) {
		int row_id = 0;
		ContentValues cv = new ContentValues();
		if (formId != null) {
			cv.put("formid", formId);
		}
		if (attributeId != null) {
			cv.put("attributeid", attributeId);
		}
		if (defaultAccess != null) {
			cv.put("defaultaccess", defaultAccess);
		}
		if (isRecordExists("select * from formfielddefaultaccess where formid='"
				+ formId + "' and attributeid='" + attributeId + "'")) {
			row_id = updateOwnFormFieldAccess(cv, "formid='" + formId
					+ "' and attributeid='" + attributeId + "'");
		} else {
			row_id = insertOwnFormFieldAccess(cv);
		}
		return row_id;
	}

	public int saveOrUpdateIndividualFormField(String formId,
			String attributeId, String buddyName, String buddyAccess) {
		int row_id = 0;
		ContentValues cv = new ContentValues();
		if (formId != null) {
			cv.put("formid", formId);
		}
		if (attributeId != null) {
			cv.put("attributeid", attributeId);
		}
		if (buddyName != null) {
			cv.put("accessiblebuddy", buddyName);
		}
		if (buddyAccess != null) {
			cv.put("buddyaccess", buddyAccess);
		}

		if (isRecordExists("select * from formfieldbuddyaccess where formid='"
				+ formId + "' and attributeid='" + attributeId
				+ "' and accessiblebuddy='" + buddyName + "'")) {
			row_id = updateBuddyFormFieldAccess(cv, "formid='" + formId
					+ "' and attributeid='" + attributeId
					+ "' and accessiblebuddy='" + buddyName + "'");
		} else {
			row_id = insertBuddyFormFieldAccess(cv);
		}

		return row_id;
	}

	public ArrayList<String> getBuddyNamesFormFieldSettings(String userName,
			String buddyName, String formId) {
		ArrayList<String> bNamesList = new ArrayList<String>();
		try {
			if (!db.isOpen())
				openDatabase();

			String query = "SELECT buddyid FROM formsettings WHERE formid='"
					+ formId + "'";
			Cursor cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				String buddy = cur.getString(0);
				if (!buddy.equalsIgnoreCase(userName)
						&& !buddy.equalsIgnoreCase(buddyName)) {
					bNamesList.add(buddy);
				}
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return bNamesList;

	}

	public int saveOrUpdateFormInfo(String tableName, String columnName,
			FormAttributeBean faBean) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			ContentValues cv = new ContentValues();
			if (tableName != null)
				cv.put("tablename", tableName);
			if (columnName != null)
				cv.put("column", columnName);
			if (faBean.getEntry() != null)
				cv.put("entrymode", faBean.getEntry());
			if (faBean.getDatavalidation() != null)
				cv.put("validdata", faBean.getDatavalidation());
			if (faBean.getDefaultvalue() != null)
				cv.put("defaultvalue", faBean.getDefaultvalue());
			if (faBean.getInstruction() != null)
				cv.put("instruction", faBean.getInstruction());
			if (faBean.getErrortip() != null)
				cv.put("errortip", faBean.getErrortip());
			if (faBean.getAttributeid() != null) {
				cv.put("attributeid", faBean.getAttributeid());
			}

			if (isRecordExists("select * from forminfo where tablename='"
					+ tableName + "' and attributeid='"
					+ faBean.getAttributeid() + "'"))
				row_id = updateFormAttribute(cv, "tablename='" + tableName
						+ "' and attributeid='" + faBean.getAttributeid() + "'");
			else
				row_id = (int) db.insert("forminfo", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	public int updateFormAttribute(ContentValues cv, String condition) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("forminfo", cv, condition, null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	public boolean deleteBuddyFromAutomacticCall(String username,
			String ownerName) {
		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = null;
			if (username != null)
				strQuery = "DELETE from autoacceptcalls WHERE username='"
						+ username + "' and owner='" + ownerName + "'";
			db.execSQL(strQuery);
			delete = true;
		} catch (Exception e) {
			delete = false;
		}
		return delete;
	}


	

	public String TimeStampConverter(String inputDate) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss:SSS");
			Date parsedTimeStamp = dateFormat.parse(inputDate);
			Timestamp timestamp = new Timestamp(parsedTimeStamp.getTime());
			if (timestamp == null) {
				return null;
			} else {
				SimpleDateFormat monthDayYearformatter = new SimpleDateFormat(
						"dd-MM-yyyy");
				return monthDayYearformatter.format((Date) timestamp);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	
	private String dateToStringFormat(String date) {
		String result = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.S");
			Date tempDate = simpleDateFormat.parse(date);
			SimpleDateFormat outputDateFormat = new SimpleDateFormat(
					"dd-MM-yyyy");
			result = outputDateFormat.format(tempDate);
		} catch (Exception ex) {
			System.out.println("Parse Exception");
		}
		return result;
	}


	
	
	public int saveOrUpdateRecordtransactiondetails(SignalingBean sb) {
		int row_id = 0;
		try {
			
			
			Log.d("CallHistory123", "Start Time : "+sb.getStartTime());
			Log.d("CallHistory123", "End Time : "+sb.getEndTime());
			
			if (!db.isOpen())
				openDatabase();

			ContentValues cv = new ContentValues();
			if (sb.getFrom() != null) {
				cv.put("fromname", sb.getFrom());
			}
			if (sb.getTo() != null) {
				cv.put("toname", sb.getTo());
			}
			if (sb.getBs_parentid() != null) {
				cv.put("parentid", sb.getBs_parentid());
			} else {
				cv.put("parentid", "");
			}
			if (sb.getType() != null) {
				cv.put("type", sb.getType());
			}
			if (sb.getCallType() != null) {
				cv.put("calltype", sb.getCallType());
			}
			if (sb.getCallType().equalsIgnoreCase("ABC")
					|| sb.getCallType().equalsIgnoreCase("VBC")
					|| sb.getCallType().equalsIgnoreCase("AP")
					|| sb.getCallType().equalsIgnoreCase("VP")) {
				if (sb.getSignalid() != null) {
					cv.put("sessionid", sb.getSignalid());
				}
			} else {
				if (sb.getSessionid() != null) {
					cv.put("sessionid", sb.getSessionid());
				}
			}
			if (sb.getStartTime() != null) {
				cv.put("starttime", sb.getStartTime());
			}
			if (sb.getEndTime() != null) {
				cv.put("endtime", sb.getEndTime());
			}
			if (sb.getUserId() != null) {
				cv.put("userid", sb.getUserId());
			} else {
				cv.put("userid", CallDispatcher.LoginUser);
			}
			if (sb.getNetWork() != null) {
				cv.put("network", sb.getNetWork());
			}
			if (sb.getDeviceOs() != null) {
				cv.put("deviceos", sb.getDeviceOs());
			}
			if (sb.getCallDuration() != null) {
				cv.put("calltime", sb.getCallDuration());
			}
			if(sb.getChatid() !=null){
				cv.put("chatid",sb.getChatid());
			}
			if(sb.getCallstatus() !=null){
				cv.put("activecallstatus",sb.getCallstatus());
			}
			if (sb.getBs_calltype() != null) {
				cv.put("bs_calltype", sb.getBs_calltype());
			}
			if (sb.getBs_callstatus() != null) {
				cv.put("bs_callstatus", sb.getBs_callstatus());
			}
			if (sb.getBs_callCategory() != null) {
				cv.put("bs_callcategory", sb.getBs_callCategory());
			}
			if (sb.getEndTime() != null) {
				cv.put("sortdate", sb.getEndTime());
			}
			if(sb.getCallstatus().equalsIgnoreCase("missedcall"))
				cv.put("status", "0");
			else
				cv.put("status", "1");
            if(sb.getHost() != null){
				cv.put("host", sb.getHost());
			}

			if(sb.getParticipants() != null){
				cv.put("participants",sb.getParticipants());
			}

			if(sb.getHost_name()!=null){
				cv.put("hostname",sb.getHost_name());
			}

			if(sb.getParticipant_name()!=null){
				cv.put("participantname",sb.getParticipant_name());
			}

			if (isRecordExists("select * from recordtransactiondetails where parentid = '"
					+ sb.getBs_parentid()
					+ "' and starttime = '"
					+ sb.getStartTime() + "'")) {
				row_id = UpdateRecordtransactionEndtime(sb.getEndTime(),
						"parentid = '" + sb.getBs_parentid()
								+ "' and starttime = '" + sb.getStartTime()
								+ "'");
			} else
				row_id = (int) db.insert("recordtransactiondetails", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	private int UpdateRecordtransactionEndtime(String string, String condition) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			if (string != null) {
				cv.put("endtime", string);
			}
			row_id = (int) db.update("recordtransactiondetails", cv, condition,
					null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	public void updaterecordtransaction(String sessionID){
		try {
			String s = "update recordtransactiondetails set recordedfile=1 where sessionid='" + sessionID + "'";
			Log.d("record","callhistory--> "+s);
			db.execSQL(s);
		}catch(Exception e){
			Log.d("record","novalue in DB for "+sessionID+" returns with error "+e.toString());
		}
	}
	public int countrecordtransactiondetails() {
		int cnt = 0;
		String countQuery = "SELECT  * FROM recordtransactiondetails";
		try {
			if (!db.isOpen())
				openDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			cnt = cursor.getCount();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return cnt;
		}
	}

	public int countcomponent() {
		int cnt = 0;
		String countQuery = "SELECT  * FROM component";
		try {
			if (!db.isOpen())
				openDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			cnt = cursor.getCount();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return cnt;
		}
	}

	public Vector<CompleteListBean> gethistorydetails(String query) {
		CompleteListBean hDetails = null;
		Vector<CompleteListBean> list = null;
		Cursor cur = null;
		try {
			if (db.isOpen()) {
			} else {
				openDatabase();
			}
			list = new Vector<CompleteListBean>();
			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			/*
			 * String recordtransactiondetails = "create table if not exists
			 * recordtransactiondetails(id INTEGER PRIMARY KEY AUTOINCREMENT,
			 * fromname nvarchar(100), toname nvarchar(100),parentid
			 * nvarchar(250), sessionid nvarchar(250), type nvarchar(100),
			 * starttime nvarchar(100), endtime nvarchar(100), userid
			 * nvarchar(100), network nvarchar(50), deviceos nvarchar(50),ac
			 * nvarchar(50))";
			 */

			while (cur.isAfterLast() == false) {
				hDetails = new CompleteListBean();
				hDetails.setFromName(cur.getString(1));
				Log.i("CH", "From :" + cur.getString(1));
				hDetails.setToName(cur.getString(2));
				Log.i("CH", "To :" + cur.getString(2));
				hDetails.setParent_id(cur.getString(3));
				Log.i("CH", "Parent ID :" + cur.getString(3));
				hDetails.setStartTime(cur.getString(6));
				Log.i("CH", "Start Time :" + cur.getString(6));
				hDetails.setEndTime(cur.getString(7));
				Log.i("CH", "End Time :" + cur.getString(7));
				hDetails.setCalltype(cur.getString(12));
				Log.i("CH", "CallType :" + cur.getString(12));

				list.add(hDetails);
				cur.moveToNext();
			}
			return list;
		} catch (Exception e) {
			Log.e("db",
					"get all getcallhistorydetails details:" + e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}

		}
	}

	public boolean deleteFromCallHistory(String userid) {
		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = null;
			if (userid != null)
				strQuery = "DELETE from recordtransactiondetails";
//				WHERE userid='"
//						+ userid  + "'";
			db.execSQL(strQuery);
			delete = true;
			Log.i("Test", "Call History===> " + userid);
		} catch (Exception e) {
			delete = false;
			Log.e("clone",
					"Call History message== >"
							+ e.getMessage());
		}
		return delete;
	}

	public Vector<RecordTransactionBean> getCallHistoryDetails(String query) {
		Vector<RecordTransactionBean> rList = new Vector<RecordTransactionBean>();
		Cursor cur = null;
		try {
			if (db.isOpen()) {
			} else {
				openDatabase();
			}
			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				RecordTransactionBean recordTransactionBean = new RecordTransactionBean();
				recordTransactionBean.setParentID(cur.getString(0));
				recordTransactionBean.setFromName(cur.getString(1));
				recordTransactionBean.setToName(cur.getString(2));
				recordTransactionBean.setCalltype(cur.getString(3));
				recordTransactionBean.setStartTime(cur.getString(4));
				if (recordTransactionBean.getStartTime() != null) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"dd-MM-yyyy HH:mm:ss ");
					Date tempDate = simpleDateFormat
							.parse(recordTransactionBean.getStartTime());
					SimpleDateFormat outputDateFormat = new SimpleDateFormat(
							CallDispatcher.dateFormat + " hh:mm aaa");
					String createdDate = outputDateFormat.format(tempDate);
					recordTransactionBean.setCreatedDate(createdDate);
				}
				recordTransactionBean.setCallDuration(cur.getString(5));
				recordTransactionBean.setBsStatus(cur.getString(6));
				rList.add(recordTransactionBean);
				cur.moveToNext();
			}
			return rList;
		} catch (Exception e) {
			Log.e("db",
					"get all getcallhistorydetails details:" + e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}

		}
	}

	public ArrayList<RecordTransactionBean> getcallhistorydetails(String query) {
		RecordTransactionBean hDetails = null;
		ArrayList<RecordTransactionBean> list = null;
		Cursor cur = null;
		try {
			if (db.isOpen()) {
			} else {
				openDatabase();
			}
			list = new ArrayList<RecordTransactionBean>();
			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			/*
			 * String recordtransactiondetails = "create table if not exists
			 * recordtransactiondetails(id INTEGER PRIMARY KEY AUTOINCREMENT,
			 * fromname nvarchar(100), toname nvarchar(100),parentid
			 * nvarchar(250), sessionid nvarchar(250), type nvarchar(100),
			 * starttime nvarchar(100), endtime nvarchar(100), userid
			 * nvarchar(100), network nvarchar(50), deviceos nvarchar(50),ac
			 * nvarchar(50))";
			 * 
			 * String recordtransactiondetails = "create table if not exists
			 * recordtransactiondetails (id INTEGER PRIMARY KEY AUTOINCREMENT,
			 * fromname nvarchar(100), toname nvarchar(100),parentid
			 * nvarchar(250), sessionid nvarchar(250), type nvarchar(100),
			 * starttime nvarchar(100), endtime nvarchar(100), calltime
			 * nvarchar(100), userid nvarchar(100), network nvarchar(50),
			 * deviceos nvarchar(50),recordedfile nvarchar(100), calltype
			 * nvarchar(100),bs_calltype tinyint(4),bs_callstatus tinyint(4),
			 * bs_callcategory tinyint(4))";
			 */

			while (cur.isAfterLast() == false) {
				hDetails = new RecordTransactionBean();
				hDetails.setFromName(cur.getString(1));
				Log.i("CH", "From :" + cur.getString(1));
				hDetails.setToName(cur.getString(2));
				Log.i("CH", "To :" + cur.getString(2));
				hDetails.setParentID(cur.getString(3));
				Log.i("CH", "Parent ID :" + cur.getString(3));
				hDetails.setStartTime(cur.getString(6));
				Log.i("CH", "Start Time :" + cur.getString(6));
				hDetails.setEndTime(cur.getString(7));
				Log.i("CH", "End Time :" + cur.getString(7));
				hDetails.setCallDuration(cur.getString(8));
				Log.i("CH", "Duration :" + cur.getString(8));
				hDetails.setRecordedfile(cur.getString(12));
				hDetails.setCalltype(cur.getString(13));
				Log.i("CH", "CallType :" + cur.getString(13));
				hDetails.setSessionid(cur.getString(4));
				if(cur.getString(21) != null) {
					hDetails.setHost(cur.getString(21));
				}
				if(cur.getString(22) != null) {
					hDetails.setParticipants(cur.getString(22));
				}
				if(cur.getString(23)!=null){
					hDetails.setHost_emailid(cur.getString(23));
				}
				if(cur.getString(24)!=null){
					hDetails.setTot_participant(cur.getString(24));
				}
				if(cur.getString(19)!=null){
                   hDetails.setCall_state(cur.getString(19));
				}

				list.add(hDetails);
				cur.moveToNext();
			}
			return list;
		} catch (Exception e) {
			Log.e("db",
					"get all getcallhistorydetails details:" + e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}

		}
	}

	@SuppressWarnings("finally")
	public Vector<String> getrecordtransactiondetailsParentId() {
		Vector<String> column_names = new Vector<String>();
		Cursor ti = null;
		try {
			if (!db.isOpen())
				openDatabase();

			ti = db.rawQuery(
					"SELECT DISTINCT parentid FROM recordtransactiondetails where parentid IS NOT NULL ",
					null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {

					column_names.add(ti.getString(0));

					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	public Vector<String> getcomponentParentId() {
		Vector<String> column_names = new Vector<String>();
		Cursor ti = null;
		try {
			if (!db.isOpen())
				openDatabase();

			ti = db.rawQuery(
					"SELECT DISTINCT parentid FROM component where parentid IS NOT NULL ",
					null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {

					column_names.add(ti.getString(0));

					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	public boolean updateTableColumn(String tableName,
			ArrayList<InputsFields> fieldList, ArrayList<InputsFields> oldList) {
		boolean result = false;
		try {
			if (!db.isOpen()) {
				openDatabase();
			}
			String[] tbl = tableName.split("_");
			String tableId = tbl[1];
			// if (isFormtableExists(tableName + "copy")) {
			// deleteDuplicateTableName(tableName + "copy");
			// }
			HashMap<String, String> tableDatatypeInfo = getColumnTypesForDuplication(tableName);
			String query = "ALTER TABLE " + "[" + tableName + "]"
					+ " RENAME TO [" + tableName + "copy]";
			Log.i("newform123", "query1 : " + query);
			String createNewTable = "";
			if (ExecuteQuery(query)) {
				createNewTable = "Id INTEGER PRIMARY KEY AUTOINCREMENT,[tableid] nvarchar(20)";

				for (int i = 0; i < fieldList.size(); i++) {
					Log.i("new", "Tbl field......." + fieldList.get(i));
					Log.i("thread",
							"Tbl field......."
									+ (fieldList.get(i).getFieldName()));
					String columnane = fieldList.get(i).getFieldName()
							.replace("[", "").replace("]", "");
					if (columnane != null) {
						if (tableDatatypeInfo.containsKey(columnane)) {
							createNewTable = createNewTable + "," + "["
									+ fieldList.get(i).getFieldName() + "]"
									+ " "
									+ tableDatatypeInfo.get(columnane).trim();
						} else if (columnane.length() > 0) {
							Log.i("newform123", "default columns : "
									+ columnane);
							createNewTable = createNewTable + "," + "["
									+ fieldList.get(i).getFieldName() + "]"
									+ " " + "nvarchar(20)";
						}
					}

				}
				createNewTable = createNewTable + ",uuid "
						+ tableDatatypeInfo.get("uuid").trim() + ",euuid "
						+ tableDatatypeInfo.get("euuid").trim() + ",uudate "
						+ tableDatatypeInfo.get("uudate").trim()
						+ ",recorddate "
						+ tableDatatypeInfo.get("recorddate").trim()
						+ ",status " + tableDatatypeInfo.get("uudate").trim();
			}
			String qry = "create table if not exists [" + tableName + "] ("
					+ createNewTable + ")";
			Log.i("newform123", "query 2 : " + qry);
			if (ExecuteQuery(qry)) {
				String insertValuesQuery = "INSERT INTO [" + tableName + "] (";
				insertValuesQuery = insertValuesQuery + "[tableid],";
				for (int j = 0; j < fieldList.size(); j++) {
					if (j > 0) {
						insertValuesQuery = insertValuesQuery + ",";
					}
					insertValuesQuery = insertValuesQuery + "["
							+ fieldList.get(j).getFieldName() + "]";
				}
				insertValuesQuery = insertValuesQuery
						+ ",uuid,euuid,uudate,recorddate,status";
				insertValuesQuery = insertValuesQuery + ")";
				if (insertValuesQuery.endsWith(",")) {
					insertValuesQuery = insertValuesQuery.substring(0,
							insertValuesQuery.length() - 1);
				}

				insertValuesQuery = insertValuesQuery + " SELECT ";
				insertValuesQuery = insertValuesQuery + "[tableid],";
				for (int k = 0; k < oldList.size(); k++) {
					String columnane = oldList.get(k).getFieldName()
							.replace("[", "").replace("]", "");

					if (k > 0) {
						insertValuesQuery = insertValuesQuery + ",";
					}
					if (tableDatatypeInfo.containsKey(columnane)) {
						insertValuesQuery = insertValuesQuery + "["
								+ oldList.get(k).getFieldName() + "]";
					} else {
						EditFormBean editFormBean = getFormInfoDetailsById(
								tableName, oldList.get(k).getAttributeId());
						if (editFormBean != null) {
							insertValuesQuery = insertValuesQuery + "["
									+ editFormBean.getColumnname() + "]";
						}
					}

				}
				insertValuesQuery = insertValuesQuery
						+ ",uuid,euuid,uudate,recorddate,status";

				if (insertValuesQuery.endsWith(",")) {
					insertValuesQuery = insertValuesQuery.substring(0,
							insertValuesQuery.length() - 1);
				}
				insertValuesQuery = insertValuesQuery + " FROM [" + tableName
						+ "copy]";
				Log.i("newform123", "query3 : " + insertValuesQuery);
				if (ExecuteQuery(insertValuesQuery)) {
					String drop = "DROP TABLE  [" + tableName + "copy]";
					if (ExecuteQuery(drop)) {
						result = true;
					}
				}
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (isFormtableExists(tableName + "copy")) {
					deleteDuplicateTableName(tableName + "copy");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean addNewColumnInExistingTable(String tableName,
			ArrayList<EditFormBean> eList) {
		boolean result = false;
		try {
			if (!db.isOpen()) {
				openDatabase();
			}
			for (EditFormBean editFormBean : eList) {

				String query = "ALTER TABLE " + tableName + " ADD COLUMN "
						+ editFormBean.getColumnname() + " "
						+ editFormBean.getDatatype() + ";";
				Log.i("newform123", "query1 : " + query);
				if (ExecuteQuery(query)) {
					Log.i("newform123", "query1 success: " + query);
					result = true;
				} else {
					return false;
				}
			}

			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("finally")
	public HashMap<String, String> getColumnTypesForDuplication(String tblname) {
		HashMap<String, String> column_names = new HashMap<String, String>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("PRAGMA table_info(" + "[" + tblname + "]" + ")",
					null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					Log.i("thread ", "Col :" + ti.getString(1));
					Log.i("thread ", "Col type:" + ti.getString(2));

					Log.i("test ", "Col :" + ti.getString(1));
					Log.i("test ", "Col type:" + ti.getString(2));
					column_names.put(ti.getString(1), ti.getString(2));

					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<String> getColumnNamesForOfflineRecord(String tblname) {
		ArrayList<String> column_names = new ArrayList<String>();
		Cursor ti = null;
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}

			ti = db.rawQuery("PRAGMA table_info(" + "[" + tblname + "]" + ")",
					null);
			if (ti.moveToFirst()) {
				while (ti.isAfterLast() == false) {
					Log.i("thread ", "Col :" + ti.getString(1));
					Log.i("thread ", "Col type:" + ti.getString(2));

					Log.i("test ", "Col :" + ti.getString(1));
					Log.i("test ", "Col type:" + ti.getString(2));
					column_names.add(ti.getString(1));

					ti.moveToNext();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (ti != null) {
				ti.close();
			}
			return column_names;
		}
	}

	public Vector<CompleteListBean> getFilesList() {
		Vector<CompleteListBean> alCompleteList = new Vector<CompleteListBean>();
		try {
			Cursor cur = null;
			String strQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype,bscategory,bsstatus,bsdirection,parentid from component where (parentid IS NULL or parentid = '') and componenttype!='IM' and componenttype!='call' and owner='";
			try {
				Log.d("cur",
						"database.............................." + db.isOpen());
				if (!db.isOpen())
					openDatabase();

				cur = db.rawQuery(strQuery, null);
				Log.d("cur",
						"cursor.............................." + cur.getCount());
				cur.moveToFirst();
				while (cur.isAfterLast() == false) {
					CompleteListBean clPro = new CompleteListBean();
					clPro.setComponentId(cur.getInt(0));
					clPro.setcomponentType(cur.getString(1));
					clPro.setContentPath(cur.getString(2));
					clPro.setFtpPath(cur.getString(3));
					clPro.setContentName(cur.getString(4));
					if (cur.getString(5) != null)
						clPro.setFromUser(cur.getString(5));

					clPro.setContent(cur.getString(6));
					clPro.setOwner(cur.getString(8));
					if (cur.getString(9) != null)
						clPro.setVanishMode(cur.getString(9));

					clPro.setVanishValue(cur.getString(10));
					clPro.setDateAndTime(cur.getString(11));
					if (cur.getString(12) != null)
						clPro.setReminderTime(cur.getString(12));
					clPro.setViewMode(cur.getInt(13));
					clPro.setReminderZone(cur.getString(11));
					clPro.setResponseType(cur.getString(15));
					if (clPro.getcomponentType().equals("note")) {
						if (clPro.getContentName().trim().length() >= 12) {
							clPro.setContentName(clPro.getContentName()
									.substring(0, 9));
						} else {
							clPro.setContentName(clPro.getContentName()
									.substring(
											0,
											clPro.getContentName().trim()
													.length()));
						}
					}
					clPro.setBstype(cur.getString(16));
					clPro.setBsstatus(cur.getString(17));
					clPro.setDirection(cur.getString(18));
					clPro.setParent_id(cur.getString(19));
					// clPro.setToName(cur.getString(20));
					//
					// clPro.setFromName(cur.getString(20));
					// clPro.setToName(cur.getString(21));
					//
					// clPro.setParent_idcall(cur.getString(22));
					// clPro.setStartTime(cur.getString(25));
					//
					// clPro.setEndTime(cur.getString(26));
					//

					alCompleteList.add(clPro);
					cur.moveToNext();
				}
				// Collections.reverse(alCompleteList);
			} catch (Exception e) {
				Log.e("thread", "getVTabProperties:" + e.getMessage());
				e.printStackTrace();
			} finally {
				if (cur != null) {
					cur.close();
				}

				return alCompleteList;

			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("files", "Exception :: " + e.getMessage());
			e.printStackTrace();

		} finally {
			return alCompleteList;
		}
	}
	public void deleteAllIndividualChat(String groupId) {
		try {
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String strDelQry = "DELETE from imchat where groupid='"
					+ groupId + "'";
			Log.d("AAAA","Sql passed is => "+strDelQry);
			db.execSQL(strDelQry);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public Vector<NotifyListBean> getNotifyFilesList(String username) {
		Cursor cur = null;
		Vector<NotifyListBean> filesList = new Vector<NotifyListBean>();
		try {
			String strquery="SELECT fromuser as fromuser, touser as touser, owner as owner, componenttype as type, ftppath as content, reminderdateandtime as media, receiveddateandtime as sortdate, coalesce(null, 'F') as notifytype, viewmode as viewed, componentid as ID,comment as category FROM component WHERE owner='"+username+"'\n" +
					"UNION ALL\n" +
					"SELECT fromname as fromuser, toname as touser, userid as owner, calltype as type, calltime as content, recordedfile as media, sortdate as sortdate, coalesce(null, 'C') as notifytype, status as viewed, sessionid as ID,chatid as category FROM recordtransactiondetails WHERE userid='"+username+"'\n" +
					"UNION ALL\n" +
					"SELECT fromuser as fromuser, touser as touser, username as owner, mimetype as type, message as content, media as media, senttime as sortdate, coalesce(null, 'I') as notifytype, unreadstatus as viewed, groupid as ID,category as category FROM chat WHERE username='"+username+"';";

			if (!db.isOpen()) {
				openDatabase();
			}

			cur = db.rawQuery(strquery, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				NotifyListBean nBean = new NotifyListBean();
				nBean.setFrom(cur.getString(0));
				nBean.setTo(cur.getString(1));
				nBean.setOwner(cur.getString(2));
				nBean.setType(cur.getString(3));
				nBean.setContent(cur.getString(4));
				nBean.setMedia(cur.getString(5));
				nBean.setSortdate(cur.getString(6));
				nBean.setNotifttype(cur.getString(7));
				nBean.setViewed(cur.getInt(8));
				nBean.setFileid(cur.getString(9));
				nBean.setCategory(cur.getString(10));

				cur.moveToNext();
				filesList.add(nBean);
			}
			return filesList;
		} catch (Exception e) {
			Log.e("db", "getNotifyFiles:" + e.getMessage());
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}

		}

	}
    public void setReadAllCount(String username,String type){
        try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String query=null;
			if (type.equalsIgnoreCase("file")) {
				query = "update component set viewmode='1' where owner='" + username + "'";
				db.execSQL(query);
			} else if (type.equalsIgnoreCase("call")){
				query = "update recordtransactiondetails set status='1' where userid='" + username + "'";
			    db.execSQL(query);
		    } else{
				query = "update chat set unreadstatus='1' where username='" + username + "'";
			    db.execSQL(query);
		    }
        } catch (Exception e) {
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
        }
    }
	public int getUnreadFileCount(String username) {

		int count = 0;
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String query = "select distinct componentid from component where viewmode='0' and owner='"
					+ username + "'";
			cur = db.rawQuery(query, null);
			count = cur.getCount();
			cur.close();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return count;

	}
	public int getUnreadCallCount(String username) {

		int count = 0;
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String query = "select distinct sessionid from recordtransactiondetails where status='0' and userid='"
					+ username + "'";
			cur = db.rawQuery(query, null);
			count = cur.getCount();
			cur.close();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return count;

	}
	public int insertorUpdateScheduleEvents(ScheduleBean scheduleBean) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("owner", scheduleBean.getOwner());
			cv.put("title", scheduleBean.getTitle());
			cv.put("starttime", scheduleBean.getStartTime());
			cv.put("endtime", scheduleBean.getEndTime());
			cv.put("eventdate", scheduleBean.getSceduleDate());
			if (isRecordExists("select * from scheduleevent where owner='"
					+ scheduleBean.getOwner() + "' and eventdate='"
					+ scheduleBean.getSceduleDate() + "'"))
				row_id = db.update("scheduleevent", cv, "owner='" + scheduleBean.getOwner()+"' and eventdate='"
						+scheduleBean.getSceduleDate() + "'", null);
			else
				row_id = (int) db.insert("scheduleevent", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public Vector<ScheduleBean> getEvents(String loginUser,String date) {
		Vector<ScheduleBean> eventList = new Vector<ScheduleBean>();
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String strGetQry = "select * from scheduleevent where owner='"
					+ loginUser + "' and eventdate='" + date +"'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("name", "length" + String.valueOf(len));
			cur.moveToFirst();
			ScheduleBean sBean = new ScheduleBean();
			while (cur.isAfterLast() == false) {
				sBean.setOwner(cur.getString(0));
				sBean.setTitle(cur.getString(1));
				sBean.setStartTime(cur.getString(2));
				sBean.setEndTime(cur.getString(3));
				sBean.setSceduleDate(cur.getString(4));

				cur.moveToNext();
				Log.i("AAAA", "getEvents " + sBean.toString());
				eventList.add(sBean);
			}
			cur.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return eventList;

	}
	public boolean deleteSelectedEvent(String selecteddate, String username) {

		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = null;
			if (username != null && selecteddate != null)
				strQuery = "DELETE from scheduleevent WHERE owner='" + username
						+ "' and eventdate='" + selecteddate + "'";
			db.execSQL(strQuery);
			delete = true;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			delete = false;
		}
		return delete;

	}
	public int insertorUpdateSecurityQuestions(String[] questions) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("id", questions[0]);
			cv.put("questions", questions[1]);
			cv.put("createddate", questions[2]);
			if (isRecordExists("select * from securityquestions where id="
					+ questions[0]))
				row_id = db.update("securityquestions", cv, "id='" + questions[0]
						+ "'", null);
			else
				row_id = (int) db.insert("securityquestions", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public int countEntryDetails(String query) {
		int cnt = 0;
		String countQuery = query;
		Log.i("AAAA","count "+query);
		try {
			if (!db.isOpen())
				openDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			cnt = cursor.getCount();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return cnt;
		}
	}
	public ArrayList<String> getSecurityQuestions() {
		ArrayList<String> questionList = new ArrayList<String>();
		String question;
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String strGetQry = "select questions from securityquestions";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			cur.moveToFirst();
			question=new String();
			while (cur.isAfterLast() == false) {
				question=cur.getString(0);
				cur.moveToNext();
				questionList.add(question);
			}
			cur.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return questionList;

	}
	public ArrayList<String> getSecurityQuestionsIds() {
		ArrayList<String> questionList = new ArrayList<String>();
		String question;
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String strGetQry = "select id from securityquestions";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			cur.moveToFirst();
			question=new String();
			while (cur.isAfterLast() == false) {
				question=cur.getString(0);
				cur.moveToNext();
				questionList.add(question);
			}
			cur.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return questionList;

	}



	public int insertChatTemplate(chattemplatebean bean) {
		int row = 0;
		try {
				if (!db.isOpen())
					openDatabase();
				ContentValues cv = new ContentValues();
				cv.put("id", bean.getTempletid());
				cv.put("message", bean.getTempletmessage());
				cv.put("userid", CallDispatcher.LoginUser);
			    cv.put("editmodevalue",bean.getEditvalue());
			if (isRecordExists("select * from chattemplate where userid='"
					+ CallDispatcher.LoginUser + "'and id='" + bean.getTempletid() + "'"))
				row = (int) db.update("chattemplate", cv, "userid='" + CallDispatcher.LoginUser + "'and id='" + bean.getTempletid() + "'",null);
			else
				row = (int) db.insert("chattemplate", null, cv);
			return row;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return 0;
		}

	}

	public int insertChatTemplateModifieddate(ChattemplateModifieddate modifieddate) {
		int row = 0;
		try {
				if (!db.isOpen())
					openDatabase();
				ContentValues cv = new ContentValues();
				cv.put("method", "chattemplate");
				cv.put("lastmodified", modifieddate.getModifieddatetime());
				row = (int) db.insert("serverhelp", null, cv);
			return row;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return 0;
		}

	}

	public String getChatTemplateModifieddatetime(){
		String datetime=null;
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String strGetQry = "select lastmodified from serverhelp";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				datetime=cur.getString(0);
				cur.moveToNext();
			}
			cur.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datetime;
	}

	public boolean chatTemplateModifiedDateDelete(){
		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = null;
				strQuery = "DELETE * from serverhelp";
			db.execSQL(strQuery);
			delete = true;
		} catch (Exception e) {
			delete = false;
			Log.e("clone",
					"Call History message== >"
							+ e.getMessage());
		}
		return delete;
	}

	public boolean deleteChatTemplate(String templateId){
		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();
			String strQuery = null;
			strQuery = "DELETE from chattemplate WHERE id='" + templateId
					+ "' and userid='" + CallDispatcher.LoginUser + "'";
			db.execSQL(strQuery);
			delete = true;
		} catch (Exception e) {
			delete = false;
			Log.e("clone", "Call History message== >" + e.getMessage());
		}
		return delete;
	}

	public Vector<chattemplatebean> getChatTemplates(){
          Vector<chattemplatebean> vc_chaChattemplatebeans=new Vector<>();
		  try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			String strGetQry = "select id,message,editmodevalue from chattemplate WHERE userid='" + CallDispatcher.LoginUser+ "'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				chattemplatebean chattemplatebean=new chattemplatebean();
				chattemplatebean.setTempletid(cur.getString(0));
				chattemplatebean.setTempletmessage(cur.getString(1));
				chattemplatebean.setEditvalue(cur.getString(2));
				cur.moveToNext();
				vc_chaChattemplatebeans.add(chattemplatebean);
			}
			cur.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vc_chaChattemplatebeans;
	}
	public int insertorUpdateMemberDetails(GroupMemberBean memberBean) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("groupid", memberBean.getGroupid());
			cv.put("membername", memberBean.getMembername());
			cv.put("role", memberBean.getRole());
			cv.put("admin", memberBean.getAdmin());

			if (isRecordExists("select * from roundingmemberdetails where groupid='"
					+ memberBean.getGroupid() + "'and membername='" + memberBean.getMembername() + "'"))
				row_id = updateMemberDetails(cv,
						"groupid='" + memberBean.getGroupid() + "'and membername='" + memberBean.getMembername() + "'");
			else
				row_id = (int) db.insert("roundingmemberdetails", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public boolean deleteMemberDetails(String groupId) {

		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery1 = null,strQuery2 = null;
			if (groupId != null) {
				strQuery1 = "DELETE from roundingmemberdetails WHERE groupid='" + groupId + "'";
				strQuery2 = "DELETE from taskdetails WHERE groupid='" + groupId + "'";
			}
			db.execSQL(strQuery1);
			db.execSQL(strQuery2);
			delete = true;
		} catch (Exception e) {
			e.printStackTrace();
			delete = false;
		}
		return delete;

	}
	public GroupMemberBean getMemberDetails(String groupid,String membername) {

		GroupMemberBean groupBean = new GroupMemberBean();
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			Log.i("AAA", "DB list  " );
			String strGetQry = null;
				strGetQry = "select * from roundingmemberdetails where groupid='"
						+ groupid + "'and membername='" + membername + "'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				groupBean.setGroupid(cur.getString(0));
				groupBean.setMembername(cur.getString(1));
				groupBean.setRole(cur.getString(2));
				groupBean.setAdmin(cur.getString(3));
				Log.i("AAA", "DB list 1" );
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return groupBean;

	}

	public int updateMemberDetails(ContentValues cv, String condition) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("roundingmemberdetails", cv, condition, null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public int insertorUpdatePatientDetails(PatientDetailsBean pBean) {
		int row_id = 0;
		Log.i("patientdetails", "insertorUpdatePatientDetails ");
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("groupid", pBean.getGroupid());
			cv.put("creatorname", pBean.getCreatorname());
			cv.put("patientid", pBean.getPatientid());
			cv.put("firstname", pBean.getFirstname());
			cv.put("middlename", pBean.getMiddlename());
			cv.put("lastname", pBean.getLastname());
			cv.put("dob", pBean.getDob());
			cv.put("sex", pBean.getSex());
			cv.put("hospital", pBean.getHospital());
			cv.put("mrn", pBean.getMrn());
			cv.put("floor", pBean.getFloor());
			cv.put("ward", pBean.getWard());
			cv.put("room", pBean.getRoom());
			cv.put("bed", pBean.getBed());
			cv.put("admissiondate", pBean.getAdmissiondate());
			cv.put("assignedmembers", pBean.getAssignedmembers());

			if (isRecordExists("select * from patientdetails where groupid='"
					+ pBean.getGroupid() + "'and patientid='" + pBean.getPatientid() + "'")) {
				Log.i("patientdetails", "insertorUpdatePatientDetails ");
				row_id = (int) db.update("patientdetails", cv, "groupid='" + pBean.getGroupid() + "'and patientid='" + pBean.getPatientid() + "'", null);
			}
			else {
				Log.i("patientdetails", "insertorUpdatePatientDetails new");
				row_id = (int) db.insert("patientdetails", null, cv);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public Vector<PatientDetailsBean> getAllPatientDetails(String strGetQry) {
		Vector<PatientDetailsBean> patientList = new Vector<PatientDetailsBean>();
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			Log.i("AAA", "DB list  " );
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				PatientDetailsBean groupBean = new PatientDetailsBean();
				groupBean.setGroupid(cur.getString(0));
				groupBean.setPatientid(cur.getString(1));
				groupBean.setCreatorname(cur.getString(2));
				groupBean.setFirstname(cur.getString(3));
				groupBean.setMiddlename(cur.getString(4));
				groupBean.setLastname(cur.getString(5));
				groupBean.setDob(cur.getString(6));
				groupBean.setSex(cur.getString(7));
				groupBean.setHospital(cur.getString(8));
				groupBean.setMrn(cur.getString(9));
				groupBean.setFloor(cur.getString(10));
				groupBean.setWard(cur.getString(11));
				groupBean.setRoom(cur.getString(12));
				groupBean.setBed(cur.getString(13));
				groupBean.setAdmissiondate(cur.getString(14));
				groupBean.setAssignedmembers(cur.getString(15));
				groupBean.setStatus(cur.getString(16));
				patientList.add(groupBean);
				Log.i("AAA", "DB list 1" + patientList);
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return patientList;

	}
	public Vector<TaskDetailsBean> getAllTaskDetails(String strGetQry) {
		Vector<TaskDetailsBean> taskList = new Vector<TaskDetailsBean>();
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			Log.i("AAA", "DB list  " );
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				TaskDetailsBean taskBean = new TaskDetailsBean();
				taskBean.setGroupid(cur.getString(0));
				taskBean.setTaskId(cur.getString(1));
				taskBean.setPatientid(cur.getString(2));
				taskBean.setCreatorName(cur.getString(3));
				taskBean.setTaskdesc(cur.getString(4));
				taskBean.setPatientname(cur.getString(5));
				taskBean.setDuedate(cur.getString(6));
				taskBean.setDuetime(cur.getString(7));
				taskBean.setSetreminder(cur.getString(8));
				taskBean.setTimetoremind(cur.getString(9));
				taskBean.setAssignedMembers(cur.getString(10));
				taskBean.setTaskstatus(cur.getString(11));
				taskList.add(taskBean);
				Log.i("AAA", "DB list 1" + taskList);
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taskList;

	}
	public TaskDetailsBean getPatientTaskDetails(String strGetQry) {
		TaskDetailsBean taskBean = new TaskDetailsBean();
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			Log.i("AAA", "DB list  " );

			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				taskBean.setGroupid(cur.getString(0));
				taskBean.setTaskId(cur.getString(1));
				taskBean.setPatientid(cur.getString(2));
				taskBean.setCreatorName(cur.getString(3));
				taskBean.setTaskdesc(cur.getString(4));
				taskBean.setPatientname(cur.getString(5));
				taskBean.setDuedate(cur.getString(6));
				taskBean.setDuetime(cur.getString(7));
				taskBean.setSetreminder(cur.getString(8));
				taskBean.setTimetoremind(cur.getString(9));
				taskBean.setAssignedMembers(cur.getString(10));
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taskBean;

	}

	public int insertorUpdatePatientComments(PatientCommentsBean pBean) {
		int row_id = 0;
		Log.i("patientdetails", "insertorUpdatePatientDetails ");
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("groupid", pBean.getGroupid());
			cv.put("groupowner", pBean.getGroupowner());
			cv.put("groupmember", pBean.getGroupmember());
			cv.put("dateandtime", pBean.getDateandtime());
			cv.put("comments", pBean.getComments());
			cv.put("commentid", pBean.getCommentid());
			cv.put("patientid", pBean.getPatientid());

			if (isRecordExists("select * from patientcomments where groupid='"
					+ pBean.getGroupid() + "'and commentid='" + pBean.getCommentid()
					+ "'and patientid='" + pBean.getPatientid()+"'"))
				row_id = (int) db.update("patientcomments", cv, "groupid='" + pBean.getGroupid() +"'and commentid='" + pBean.getCommentid() + "'and patientid='" + pBean.getPatientid()+"'", null);
			else {
				row_id = (int) db.insert("patientcomments", null, cv);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}

	public int insertorUpdatePatientDescriptions(PatientDescriptionBean pcBean) {
		int row_id = 0;
		Log.i("patientdetails", "insertorUpdatePatientDetails ");
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("patientid", pcBean.getPatientid());
			cv.put("groupid", pcBean.getGroupid());
			cv.put("reportid", pcBean.getReportid());
			cv.put("creatorname", pcBean.getReportcreator());
			cv.put("currentstatus", pcBean.getCurrentstatus());
			cv.put("diagnosis", pcBean.getDiagnosis());
			cv.put("medications", pcBean.getMedications());
			cv.put("testandvitals", pcBean.getTestandvitals());
			cv.put("hospitalcourse",pcBean.getHospitalcourse());
			cv.put("consults", pcBean.getConsults());

			if (isRecordExists("select * from patientdescription where patientid='"
					+ pcBean.getPatientid() + "'and reportid='" + pcBean.getReportid() + "'"))
				row_id = (int) db.update("patientdescription", cv, "patientid='" + pcBean.getPatientid()
						+ "'and reportid='" + pcBean.getReportid() +"'", null);
			else {
				row_id = (int) db.insert("patientdescription", null, cv);
			}
			UpdatePatientStatus(pcBean.getCurrentstatus(),pcBean.getPatientid(),pcBean.getGroupid());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public int UpdatePatientStatus(String status,String patientid,String groupid) {
		int row_id = 0;
		Log.i("patientdetails", "insertorUpdatePatientDetails ");
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("status", status);
			row_id = (int) db.update("patientdetails", cv, "groupid='" + groupid + "'and patientid='" + patientid + "'", null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public PatientDescriptionBean getPatientDescriptionDetails(String patientid) {
		PatientDescriptionBean groupBean = new PatientDescriptionBean();
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			Log.i("AAA", "DB list  ");
			String strGetQry = null;
			strGetQry = "select * from patientdescription where patientid='"
					+ patientid + "'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				groupBean.setPatientid(cur.getString(0));
				groupBean.setGroupid(cur.getString(1));
				groupBean.setReportid(cur.getString(2));
				groupBean.setReportcreator(cur.getString(3));
				groupBean.setCurrentstatus(cur.getString(4));
				groupBean.setDiagnosis(cur.getString(5));
				groupBean.setMedications(cur.getString(6));
				groupBean.setTestandvitals(cur.getString(7));
				groupBean.setHospitalcourse(cur.getString(8));
				groupBean.setConsults(cur.getString(9));
				groupBean.setReportmodifier(cur.getString(10));
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return groupBean;

	}
	public int insertorupdateRolepatientAccess(RolePatientManagementBean rBean) {
		Log.i("sss", "DBmembershipRights updated String");

		int row = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("groupid", rBean.getGroupid());
			cv.put("groupowner", rBean.getGroupowner());
			cv.put("groupmember", rBean.getGroupmember());
			cv.put("roleid", rBean.getRoleid());
			cv.put("role", rBean.getRole());
			cv.put("padd", rBean.getAdd());
			cv.put("pmodify", rBean.getModify());
			cv.put("pdelete", rBean.getDelete());
			cv.put("discharge", rBean.getDischarge());


			if (isRecordExists("select * from rolepatientmgt where groupid='"
					+ rBean.getGroupid() + "'and roleid='" + rBean.getRoleid() + "'")) {
				Log.i("sss", "isRecordExists rightss of task=============== ");
				row = (int) db.update("rolepatientmgt", cv, "groupid='" + rBean.getGroupid()
						+ "'and roleid='" + rBean.getRoleid() + "'", null);
			}
			else {
				Log.i("sss", "insert rights new");
				row = (int) db.insert("rolepatientmgt", null, cv);
			}
			return row;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return 0;
		}
	}
    public int insertorupdateRoleEditRoundingFormAccess(RoleEditRndFormBean eBean) {
        Log.i("sss", "DBmembershipRights updated String");

        int row = 0;
        try {
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("groupid", eBean.getGroupid());
            cv.put("groupowner", eBean.getGroupowner());
            cv.put("groupmember", eBean.getGroupmember());
            cv.put("roleid", eBean.getRoleid());
            cv.put("role", eBean.getRole());
            cv.put("status", eBean.getStatus());
            cv.put("diagnosis", eBean.getDiagnosis());
            cv.put("testandvitals", eBean.getTestandvitals());
            cv.put("hospitalcourse", eBean.getHospitalcourse());
            cv.put("notes", eBean.getNotes());
            cv.put("consults", eBean.getConsults());

            if (isRecordExists("select * from roleeditroundignform where groupid='"
                    + eBean.getGroupid() + "'and roleid='" + eBean.getRoleid() + "'")) {
                Log.i("sss", "isRecordExists rightss of task=============== ");
				row = (int) db.update("roleeditroundignform", cv, "groupid='" + eBean.getGroupid()
						+ "'and roleid='" + eBean.getRoleid() + "'", null);
            }
            else {
                Log.i("sss", "insert rights new");
                row = (int) db.insert("roleeditroundignform", null, cv);
            }
            return row;
        } catch (Exception e) {
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return 0;
        }
    }

    public int insertorupdateRoleTransactionAccess(RoleTaskMgtBean tBean) {
        Log.i("sss", "DBmembershipRights updated String");

        int row = 0;
        try {
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("groupid", tBean.getGroupid());
            cv.put("groupowner", tBean.getGroupowner());
            cv.put("groupmember", tBean.getGroupmember());
            cv.put("roleid", tBean.getRoleid());
            cv.put("role", tBean.getRole());
            cv.put("tattending", tBean.getTattending());
            cv.put("tfellow", tBean.getTfellow());
            cv.put("tchiefresident", tBean.getTchiefresident());
            cv.put("tresident", tBean.getTresident());
            cv.put("tmedstudent", tBean.getTmedstudent());


            if (isRecordExists("select * from roletransctionmgt where groupid='"
                    + tBean.getGroupid() + "'and roleid='" + tBean.getRoleid() + "'")) {
                Log.i("sss", "isRecordExists rightss of task=============== ");
				row = (int) db.update("roletransctionmgt", cv, "groupid='" + tBean.getGroupid()
						+ "'and roleid='" + tBean.getRoleid() + "'", null);
            }
            else {
                Log.i("sss", "insert rights new");
                row = (int) db.insert("roletransctionmgt", null, cv);
            }
            return row;
        } catch (Exception e) {
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return 0;
        }
    }

    public int insertorupdateRoleCommentsViewAccess(RoleCommentsViewBean cBean) {
        Log.i("sss", "DBmembershipRights updated String");

        int row = 0;
        try {
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("groupid", cBean.getGroupid());
            cv.put("groupowner", cBean.getGroupowner());
            cv.put("groupmember", cBean.getGroupmember());
            cv.put("roleid", cBean.getRoleid());
            cv.put("role", cBean.getRole());
            cv.put("cattending", cBean.getCattending());
            cv.put("cfellow", cBean.getCfellow());
            cv.put("cchiefresident", cBean.getCchiefresident());
            cv.put("cresident", cBean.getCresident());
            cv.put("cmedstudent", cBean.getCmedstudent());

            if (isRecordExists("select * from rolecommentsview where groupid='"
                    + cBean.getGroupid() + "'and roleid='" + cBean.getRoleid() + "'")) {
                Log.i("sss", "isRecordExists rightss of task=============== ");
				row = (int) db.update("rolecommentsview", cv, "groupid='" + cBean.getGroupid()
						+ "'and roleid='" + cBean.getRoleid() + "'", null);
            }
            else {
                Log.i("sss", "insert rights new");
                row = (int) db.insert("rolecommentsview", null, cv);
            }
            return row;
        } catch (Exception e) {
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return 0;
        }
    }
	public int insertorupdateRoleAccess(RoleAccessBean rBean) {
		Log.i("sss", "DBmembershipRights updated String");

		int row = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("groupid", rBean.getGroupid());
			cv.put("groupowner", rBean.getGroupowner());
			cv.put("groupmember", rBean.getGroupmember());
			cv.put("roleid", rBean.getRoleid());
			cv.put("role", rBean.getRole());
			cv.put("patientmanagement", rBean.getPatientmanagement());
			cv.put("taskmanagement", rBean.getTaskmanagement());
			cv.put("editroundingform", rBean.getEditroundingform());
			cv.put("commentsview", rBean.getCommentsview());


			if (isRecordExists("select * from roleaccess where groupid='"
					+ rBean.getGroupid() + "'and roleid='" + rBean.getRoleid() + "'")) {
				Log.i("sss", "isRecordExists rightss of task=============== ");
				row = (int) db.update("roleaccess", cv, "groupid='" + rBean.getGroupid()
						+ "'and roleid='" + rBean.getRoleid() + "'", null);
			}
			else {
				Log.i("sss", "insert rights new");
				row = (int) db.insert("roleaccess", null, cv);
			}
			return row;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return 0;
		}
	}
	public Vector<PatientCommentsBean> getPatientComments(String groupid,String patientid,String ownername,String type) {
		Vector<PatientCommentsBean> commentsList = new Vector<PatientCommentsBean>();
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			Log.i("AAA", "DB list  ");
			String strGetQry = null;
			if(type.equalsIgnoreCase("mine"))
			strGetQry = "select * from patientcomments where groupid='"
					+ groupid +  "'and patientid='" + patientid + "'and groupmember='" + ownername +"'";
			else if(type.equalsIgnoreCase("team"))
				strGetQry = "select * from patientcomments where groupid='"
						+ groupid + "'and patientid='" + patientid + "'and groupmember!='" + ownername +"'";
			else
				strGetQry = "select * from patientcomments where groupid='"
						+ groupid + "'and patientid='" + patientid + "'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				PatientCommentsBean groupBean = new PatientCommentsBean();
				groupBean.setGroupid(cur.getString(0));
				groupBean.setCommentid(cur.getString(1));
				groupBean.setPatientid(cur.getString(2));
				groupBean.setGroupowner(cur.getString(3));
				groupBean.setGroupmember(cur.getString(4));
				groupBean.setDateandtime(cur.getString(5));
				groupBean.setComments(cur.getString(6));
				commentsList.add(groupBean);
				Log.i("AAA", "DB list 1" + commentsList);
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return commentsList;

	}
	public Vector<PatientCommentsBean> getParticularPatientComments(String groupid,String patientid,String membername) {
		Vector<PatientCommentsBean> commentsList = new Vector<PatientCommentsBean>();
		try {
			Cursor cur = null;
			if (db.isOpen()) {

			} else {
				openDatabase();
			}
			Log.i("AAA", "DB list  ");
			String strGetQry = null;
				strGetQry = "select * from patientcomments where groupid='"
						+ groupid +  "'and patientid='" + patientid + "'and groupmember='" + membername +"'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				PatientCommentsBean groupBean = new PatientCommentsBean();
				groupBean.setGroupid(cur.getString(0));
				groupBean.setCommentid(cur.getString(1));
				groupBean.setPatientid(cur.getString(2));
				groupBean.setGroupowner(cur.getString(3));
				groupBean.setGroupmember(cur.getString(4));
				groupBean.setDateandtime(cur.getString(5));
				groupBean.setComments(cur.getString(6));
				commentsList.add(groupBean);
				Log.i("AAA", "DB list 1" + commentsList);
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return commentsList;

	}
	public boolean deletePatientRelatedDetails(String groupId,String patientid) {

		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery1 = null,strQuery2=null,strQuery3 = null;
			String strQuery4 = null;
			if (groupId != null) {
				strQuery1 = "DELETE  from patientdetails WHERE groupid='" + groupId +  "'and patientid='" + patientid +"'";
				strQuery2 = "DELETE  from patientdescription WHERE groupid='" + groupId + "'and patientid='" + patientid + "'";
				strQuery3 = "DELETE  from patientcomments WHERE groupid='" + groupId + "'and patientid='" + patientid +"'";
				strQuery4 = "DELETE from taskdetails WHERE groupid='" + groupId +  "'and patientid='" + patientid +"'";
			}
			db.execSQL(strQuery1);
			db.execSQL(strQuery2);
			db.execSQL(strQuery3);
			db.execSQL(strQuery4);
			delete = true;
		} catch (Exception e) {
			e.printStackTrace();
			delete = false;
		}
		return delete;
	}
	public RolePatientManagementBean getRolePatientManagement(String groupid,String role) {
		RolePatientManagementBean pmBean = new RolePatientManagementBean();
		try {
			Cursor cur = null;
			if (db.isOpen()) {
			} else
				openDatabase();
			String strGetQry = null;
			strGetQry = "select * from rolepatientmgt where groupid='" + groupid +  "'and role='" + role +"'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				pmBean.setGroupid(cur.getString(0));
				pmBean.setGroupowner(cur.getString(1));
				pmBean.setGroupmember(cur.getString(2));
				pmBean.setRoleid(cur.getString(3));
				pmBean.setRole(cur.getString(4));
				pmBean.setAdd(cur.getString(5));
				pmBean.setModify(cur.getString(6));
				pmBean.setDelete(cur.getString(7));
				pmBean.setDischarge(cur.getString(8));
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pmBean;

	}
	public RoleTaskMgtBean getRoleTaskManagement(String groupid,String role) {
		RoleTaskMgtBean pmBean = new RoleTaskMgtBean();
		try {
			Cursor cur = null;
			if (db.isOpen()) {
			} else
				openDatabase();
			String strGetQry = null;
			strGetQry = "select * from roletransctionmgt where groupid='" + groupid +  "'and role='" + role +"'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				pmBean.setGroupid(cur.getString(0));
				pmBean.setGroupowner(cur.getString(1));
				pmBean.setGroupmember(cur.getString(2));
				pmBean.setRoleid(cur.getString(3));
				pmBean.setRole(cur.getString(4));
				pmBean.setTattending(cur.getString(5));
				pmBean.setTfellow(cur.getString(6));
				pmBean.setTchiefresident(cur.getString(7));
				pmBean.setTresident(cur.getString(8));
				pmBean.setTmedstudent(cur.getString(9));
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pmBean;
	}
	public RoleCommentsViewBean getRoleCommentsView(String groupid,String role) {
		RoleCommentsViewBean pmBean = new RoleCommentsViewBean();
		try {
			Cursor cur = null;
			if (db.isOpen()) {
			} else
				openDatabase();
			String strGetQry = null;
			strGetQry = "select * from rolecommentsview where groupid='" + groupid +  "'and role='" + role +"'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				pmBean.setGroupid(cur.getString(0));
				pmBean.setGroupowner(cur.getString(1));
				pmBean.setGroupmember(cur.getString(2));
				pmBean.setRoleid(cur.getString(3));
				pmBean.setRole(cur.getString(4));
				pmBean.setCattending(cur.getString(5));
				pmBean.setCfellow(cur.getString(6));
				pmBean.setCchiefresident(cur.getString(7));
				pmBean.setCresident(cur.getString(8));
				pmBean.setCmedstudent(cur.getString(9));
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pmBean;
	}
	public RoleEditRndFormBean getRoleEditRoundingDetails(String groupid,String role) {
		RoleEditRndFormBean pmBean = new RoleEditRndFormBean();
		try {
			Cursor cur = null;
			if (db.isOpen()) {
			} else
				openDatabase();
			String strGetQry = null;
			strGetQry = "select * from roleeditroundignform where groupid='" + groupid +  "'and role='" + role +"'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				pmBean.setGroupid(cur.getString(0));
				pmBean.setGroupowner(cur.getString(1));
				pmBean.setGroupmember(cur.getString(2));
				pmBean.setRoleid(cur.getString(3));
				pmBean.setRole(cur.getString(4));
				pmBean.setStatus(cur.getString(5));
				pmBean.setDiagnosis(cur.getString(6));
				pmBean.setTestandvitals(cur.getString(7));
				pmBean.setHospitalcourse(cur.getString(8));
				pmBean.setNotes(cur.getString(9));
				pmBean.setConsults(cur.getString(10));
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pmBean;
	}
	public RoleAccessBean getRoleAccessDetails(String groupid,String role) {
		RoleAccessBean pmBean = new RoleAccessBean();
		try {
			Cursor cur = null;
			if (db.isOpen()) {
			} else
				openDatabase();
			String strGetQry = null;
			strGetQry = "select * from roleaccess where groupid='" + groupid +  "'and role='" + role +"'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				pmBean.setGroupid(cur.getString(0));
				pmBean.setGroupowner(cur.getString(1));
				pmBean.setGroupmember(cur.getString(2));
				pmBean.setRoleid(cur.getString(3));
				pmBean.setRole(cur.getString(4));
				pmBean.setPatientmanagement(cur.getString(5));
				pmBean.setTaskmanagement(cur.getString(6));
				pmBean.setEditroundingform(cur.getString(7));
				pmBean.setCommentsview(cur.getString(8));
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pmBean;
	}
	public int insertorupdateProfileDetails(ProfileBean pBean) {
		Log.i("sss", "DBmembershipRights updated String");

		int row = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("username", pBean.getUsername());
			cv.put("nickname", pBean.getNickname());
			cv.put("photo", pBean.getPhoto());
			cv.put("title", pBean.getTitle());
			cv.put("firstname", pBean.getFirstname());
			cv.put("lastname", pBean.getLastname());
			cv.put("sex", pBean.getSex());
			cv.put("usertype", pBean.getUsertype());
			cv.put("state", pBean.getState());
			cv.put("profession", pBean.getProfession());
			cv.put("speciality", pBean.getSpeciality());
			cv.put("medicalschool", pBean.getMedicalschool());
			cv.put("residency", pBean.getResidencyprogram());
			cv.put("fellowship", pBean.getFellowshipprogram());
			cv.put("officeaddress", pBean.getOfficeaddress());
			cv.put("hospitalaffiliation", pBean.getHospitalaffiliation());
			cv.put("citations", pBean.getCitationpublications());
			cv.put("organization", pBean.getOrganizationmembership());
			cv.put("tos", pBean.getTos());
			cv.put("baa", pBean.getBaa());
			cv.put("status", pBean.getStatus());
			cv.put("mobileno", pBean.getMobileno());
			cv.put("homeaddress", pBean.getAddress());
			cv.put("dob", pBean.getDob());
			cv.put("ssn", pBean.getSsn());
			cv.put("race", pBean.getRace());
			cv.put("ethinicity", pBean.getEthinicity());
			cv.put("maritalstatus", pBean.getMaritalstatus());

			if (isRecordExists("select * from profiledetails where username='"
					+ pBean.getUsername() + "'")) {
				row = (int) db.update("profiledetails", cv, "username='" + pBean.getUsername()
						+  "'", null);
			}
			else {
				row = (int) db.insert("profiledetails", null, cv);
			}
			return row;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return 0;
		}
	}
	public ProfileBean getProfileDetails(String username) {
		ProfileBean pmBean = new ProfileBean();
		try {
			Cursor cur = null;
			if (db.isOpen()) {
			} else
				openDatabase();
			String strGetQry = null;
			strGetQry = "select * from profiledetails where username='" + username +  "'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				pmBean.setUsername(cur.getString(0));
				pmBean.setNickname(cur.getString(1));
				pmBean.setPhoto(cur.getString(2));
				pmBean.setTitle(cur.getString(3));
				pmBean.setFirstname(cur.getString(4));
				pmBean.setLastname(cur.getString(5));
				pmBean.setSex(cur.getString(6));
				pmBean.setUsertype(cur.getString(7));
				pmBean.setState(cur.getString(8));
				pmBean.setProfession(cur.getString(9));
				pmBean.setSpeciality(cur.getString(10));
				pmBean.setMedicalschool(cur.getString(11));
				pmBean.setResidencyprogram(cur.getString(12));
				pmBean.setFellowshipprogram(cur.getString(13));
				pmBean.setOfficeaddress(cur.getString(14));
				pmBean.setHospitalaffiliation(cur.getString(15));
				pmBean.setCitationpublications(cur.getString(16));
				pmBean.setOrganizationmembership(cur.getString(17));
				pmBean.setTos(cur.getString(18));
				pmBean.setBaa(cur.getString(19));
				pmBean.setStatus(cur.getString(20));
				pmBean.setMobileno(cur.getString(21));
				pmBean.setAddress(cur.getString(22));
				pmBean.setDob(cur.getString(23));
				pmBean.setSsn(cur.getString(24));
				pmBean.setRace(cur.getString(25));
				pmBean.setEthinicity(cur.getString(26));
				pmBean.setMaritalstatus(cur.getString(27));
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pmBean;
	}
	public boolean deleteBuddyProfile(String username) {
		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();

			String strQuery = null;
			if (username != null)
				strQuery = "DELETE from profiledetails WHERE username='" + username + "'";
			db.execSQL(strQuery);
			delete = true;
		} catch (Exception e) {
			delete = false;
		}
		return delete;
	}
	public boolean deleteTaskDetails(String strQuery) {

		boolean delete = false;
		try {
			if (!db.isOpen())
				openDatabase();
			db.execSQL(strQuery);
			delete = true;
		} catch (Exception e) {
			e.printStackTrace();
			delete = false;
		}
		return delete;

	}

	public int ChatInfo(String members,String sid,String status,String date) {
		int row_id = 0;
		String val="3";
		try {
			ContentValues cv = new ContentValues();

			if (isRecordExists("select * from chatinfo where sid='" + sid + "' and members='" + members + "'")){
				cv.put("members", members);
				cv.put("status", status);
				cv.put("datetime", date);
				row_id = (int) db.update("chatinfo", cv, "sid='"+ sid + "' and members='"+ members + "'", null);
				}
			else {
				cv.put("sid", sid);
				cv.put("members", members);
				cv.put("status", status);
				cv.put("datetime", date);
				if (isRecordExists("select * from chatinfo where sid='"+ sid + "' and members='"+ members + "' and status='"+ val + "'"))
				{}else {
					row_id = (int) db.insert("chatinfo", null, cv);
				}
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public ArrayList< ChatInfoBean> ChatInfoMembers(String sid) {
		ArrayList< ChatInfoBean> ChatInfoMap = new ArrayList< ChatInfoBean>();
		int row_id = 0;
		String mem=null;
		try {
			Cursor cur = null;
			if (db.isOpen()) {
			} else
				openDatabase();
			String strGetQry = null;
			strGetQry = "select * from chatinfo where sid='"
					+ sid + "'";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				ChatInfoBean b=new ChatInfoBean();
				b.setDate(cur.getString(3));
				b.setName(cur.getString(1));
				b.setSid(cur.getString(0));
				b.setStatus(cur.getString(2));
				ChatInfoMap.add(b);
				cur.moveToNext();
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ChatInfoMap;
	}
	public int countPatientComments(String groupid,String patientid,String membername) {
		int cnt = 0;
		String strGetQry = null;
		strGetQry = "select * from patientcomments where groupid='"
				+ groupid +  "'and patientid='" + patientid + "'and groupmember='" + membername +"'";
		try {
			if (!db.isOpen())
				openDatabase();
			Cursor cursor = db.rawQuery(strGetQry, null);
			cnt = cursor.getCount();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return cnt;
		}
	}
	public int insertorUpdateStateDetails(String statename,String code) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("statename", statename);
			cv.put("statecode", code);
				row_id = (int) db.insert("statedetails", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public int insertseallcomments(PatientDescriptionBean pbean){
		int comment_id = 0;
		try{
			if(!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("groupid", pbean.getGroupid());
			cv.put("patientid", pbean.getPatientid());
			cv.put("active",pbean.getActiveID());
			if(pbean.getDiagnosis()!=null)
			cv.put("diagnosis", pbean.getDiagnosis());
			if(pbean.getDate()!=null)
				cv.put("commentdate",pbean.getDate());
			comment_id = (int)db.insert("seeallpatientdetails", null, cv);
		}catch(Exception e){
			e.printStackTrace();
		}
		return comment_id;
	}

	public void updateseallcomments(String value, String commentdate,String patientID){
		try{
			Log.d("updateseeall", "incoming");
			Log.d("updateseeall", "incoming1"+value+commentdate+patientID);
			String s = "update seeallpatientdetails set active=1 where diagnosis='"+value+ "' and commentdate='"+commentdate+"' and patientid='"+patientID+"'";
			String s1 = "update seeallpatientdetails set active=0 where diagnosis!='"+value+ "' and commentdate!='"+commentdate+"' and patientid='"+patientID+"'";
			db.execSQL(s);
			db.execSQL(s1);
			Log.d("updateseeall", "SQL => " + s);
		}catch(Exception e){
			Log.d("updateseeall","novalue in DB for "+value+ commentdate+patientID+" returns with error "+e.toString());
		}

	}

	public Vector<PatientDescriptionBean> getseallcomments(String strGetQry){
		Vector<PatientDescriptionBean> seeallcomment = new Vector<PatientDescriptionBean>();
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();

			cur = db.rawQuery(strGetQry, null);
			int length = cur.getCount();
			Log.d("Stringvalue", "dbvalue0" + strGetQry);
			cur.moveToFirst();
			Log.d("Stringvalue", "dbvalue1");
			while (cur.isAfterLast() == false) {
				Log.d("Stringvalue", "dbvalue2");
				PatientDescriptionBean pBean = new PatientDescriptionBean();
				pBean.setGroupid(cur.getString(0));
				pBean.setDiagnosis(cur.getString(2));
				pBean.setActiveID(cur.getString(3));
				pBean.setDate(cur.getString(4));
				pBean.setPatientid(cur.getString(1));
				cur.moveToNext();
				seeallcomment.add(pBean);
				Log.d("Stringvalue","dbvalue"+pBean.getDiagnosis());
			}
			cur.close();

		}catch (Exception e){
			e.printStackTrace();
		}
		return seeallcomment;
	}
	public ArrayList<String> getStateDetails() {
		ArrayList<String> stateList = new ArrayList<String>();
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String strGetQry = null;
			strGetQry = "select * from statedetails ";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				String state=new String();
				state=cur.getString(0);
				cur.moveToNext();
				stateList.add(state);
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stateList;
	}
	public int insertorUpdateMedicalSchools(String schoolname) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("medicalschool", schoolname);
			row_id = (int) db.insert("medicalschools", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public ArrayList<String> getMedicalSchoolDetails() {
		ArrayList<String> schoolList = new ArrayList<String>();
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String strGetQry = null;
			strGetQry = "select * from medicalschools ";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				String medicalschol=new String();
				medicalschol=cur.getString(0);
				cur.moveToNext();
				schoolList.add(medicalschol);
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return schoolList;
	}
	public int insertorUpdateSpecialityDetails(String schoolname,String code) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("speciality", schoolname);
			cv.put("code", code);
			row_id = (int) db.insert("specialitydetails", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public ArrayList<String> getSpecialityDetails() {
		ArrayList<String> specialityList = new ArrayList<String>();
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String strGetQry = null;
			strGetQry = "select * from specialitydetails ";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				String speciality=new String();
				speciality=cur.getString(0);
				cur.moveToNext();
				specialityList.add(speciality);
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return specialityList;
	}
	public int insertorUpdateHospitalDetails(String hospitalname) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("hospitalname", hospitalname);
			row_id = (int) db.insert("hospitaldetails", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public ArrayList<String> getHospitalDetails() {
		ArrayList<String> hospitalList = new ArrayList<String>();
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String strGetQry = null;
			strGetQry = "select * from hospitaldetails ";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				String hospital=new String();
				hospital=cur.getString(0);
				cur.moveToNext();
				hospitalList.add(hospital);
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hospitalList;
	}
	public int insertorUpdateMedicalSocieties(String medicalsociety,String id) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("medicalsociety", medicalsociety);
			cv.put("id", id);
			row_id = (int) db.insert("medicalsocieties", null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public ArrayList<String> getMedicalSocietiesDetails() {
		ArrayList<String> medicalList = new ArrayList<String>();
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String strGetQry = null;
			strGetQry = "select * from medicalsocieties ";
			cur = db.rawQuery(strGetQry, null);
			int len = cur.getCount();
			Log.i("AAA", "length" + String.valueOf(len));
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				String medicalsociety=new String();
				medicalsociety=cur.getString(1);
				cur.moveToNext();
				medicalList.add(medicalsociety);
			}
			cur.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return medicalList;
	}

	public Vector<GroupChatBean> getChatLinksHistory(String groupid,
													 boolean group) {
		Vector<GroupChatBean> groupChatList = new Vector<GroupChatBean>();
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String query = "";
			if (group)
				query = "select fromuser,message,senttime from chat where unview!='1' and groupid='" + groupid
						+ "' and username ='" + CallDispatcher.LoginUser + "' and mimetype ='link'";
			Log.i("group123", "query : " + query);
			cur = db.rawQuery(query, null);
			cur.moveToFirst();
			while (cur.isAfterLast() == false) {
				GroupChatBean groupChatBean = new GroupChatBean();
				groupChatBean.setFrom(cur.getString(0));
				groupChatBean.setMessage(cur.getString(1));
				groupChatBean.setSenttime(cur.getString(2));
				groupChatList.add(groupChatBean);
				cur.moveToNext();
				Log.i("group123", "chat list size in db: " + groupChatBean.getMessage());
			}
			Log.i("group123", "chat list size in db: " + groupChatList.size());
			cur.close();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return groupChatList;
	}
	public int insertGroupCallChat(SignalingBean groupChatBean) {
		int row = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("category", "call");
			if(groupChatBean.getChatid().equalsIgnoreCase(CallDispatcher.LoginUser)) {
				if(!groupChatBean.getTo().equalsIgnoreCase(CallDispatcher.LoginUser)){
					cv.put("groupid", groupChatBean.getTo());
				} else if(!groupChatBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)){
					cv.put("groupid", groupChatBean.getFrom());
				}
			} else if(groupChatBean.getChatid().contains(CallDispatcher.LoginUser)) {
				cv.put("groupid", groupChatBean.getChatid());
			} else {

				cv.put("groupid", groupChatBean.getChatid());
			}

			cv.put("fromuser",groupChatBean.getHost());
			if(groupChatBean.getHost().equalsIgnoreCase(groupChatBean.getTo())) {
				cv.put("touser", groupChatBean.getFrom());
			} else if(groupChatBean.getHost().equalsIgnoreCase(groupChatBean.getFrom())){
				cv.put("touser", groupChatBean.getTo());
			}
//			cv.put("fromuser", groupChatBean.getFrom());
//			cv.put("touser", groupChatBean.getTo());
			cv.put("message", groupChatBean.getCallType());
			cv.put("ftpusername", groupChatBean.getHost());
			cv.put("sessionid", groupChatBean.getSessionid());
			cv.put("senttime", groupChatBean.getEndTime());
			cv.put("username", CallDispatcher.LoginUser);
			cv.put("ftppassword", groupChatBean.getParticipants());
			cv.put("remindertime", groupChatBean.getCallDuration());
			cv.put("subcategory", groupChatBean.getCallstatus());
			cv.put("dateandtime", groupChatBean.getEndTime());

			if(groupChatBean.getCallstatus() != null && groupChatBean.getCallstatus().equalsIgnoreCase("missedcall")) {
				cv.put("unview","join");
			} else {
				cv.put("unview","normal");
			}

			if (isRecordExists("select * from chat where signalid='"
					+ groupChatBean.getSessionid() + "'")) {
				row = (int) db.update("chat", cv,
						"signalid='" + groupChatBean.getSessionid() + "'", null);
			} else {
				row = (int) db.insert("chat", null, cv);
			}
			return row;
		} catch (Exception e) {
			e.printStackTrace();
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
//			else
//				e.printStackTrace();
			return 0;
		}

	}

	public int updateGroupCallChatEntry(GroupChatBean groupChatBean){
		int row = 0;
		try {
			if (!db.isOpen())
				openDatabase();
			ContentValues cv = new ContentValues();
			cv.put("unview","normal");


			if (isRecordExists("select * from chat where sessionid='"
					+ groupChatBean.getSessionid() + "'")) {
				Log.i("AudioCall", "chat hisory Available=============== ");
				row = (int) db.update("chat", cv, "sessionid='" + groupChatBean.getSessionid() + "'", null);
			} else {
				Log.i("AudioCall","Chat history not available");
			}

			return row;
		} catch (Exception e) {
			return row;
		}
	}


	public int updateChatWithdraw_row(ContentValues cv, String condition) {
		int row_id = 0;
		try {
			if (!db.isOpen())
				openDatabase();

			row_id = (int) db.update("chat", cv, condition, null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		} finally {
			return row_id;
		}
	}
	public String getCurrentTime() {
		try {
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(
					"hh:mm a");
			return sdf.format(curDate).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String getminDateandTime() {
		String dateandtime="";
		try {
			Cursor cur = null;
			if (!db.isOpen())
				openDatabase();
			String strGetQry = "select min(dateandtime) from chat where username='"
					+ CallDispatcher.LoginUser + "'";
			cur = db.rawQuery(strGetQry, null);
			cur.moveToFirst();

			while (cur.isAfterLast() == false) {
				dateandtime = cur.getString(0);
				cur.moveToNext();
			}
			cur.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateandtime;
	}



}