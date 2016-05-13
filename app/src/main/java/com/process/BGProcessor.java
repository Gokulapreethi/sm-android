package com.process;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.Vector;

import org.lib.model.BuddyInformationBean;
import org.lib.model.FieldTemplateBean;
import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.ProfileTemplateBean;
import org.lib.model.ShareReminder;
import org.lib.model.Syncutilitybean;
import org.lib.model.UtilityBean;
import org.lib.model.WebServiceBean;
import org.lib.webservice.EnumWebServiceMethods;
import org.lib.xml.XmlComposer;

import android.content.ContentValues;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.cg.DB.DBAccess;
import com.cg.avatar.AnsweringMachineActivity;
import com.cg.avatar.AvatarActivity;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.ftpprocessor.FTPBean;
import com.cg.timer.FileDownloader;
import com.group.chat.GroupChatActivity;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.main.CreateProfileFragment;
import com.main.SettingsFragment;
import com.util.SingleInstance;
import com.util.Utils;
import com.ws.BackgroundWSProcessor;
import com.ws.WSBean;

public class BGProcessor {
	private static BGProcessor bgProcessor;

	private Timer download_timer;

	private FileDownloader fileDownloader;

	private AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.mainContext;

	// public ArrayList<String[]> profileDetails = new ArrayList<String[]>();

	public ArrayList<String> blocked_buddies = new ArrayList<String>();

	public HashMap<String, String> multimediaFieldsValues = new HashMap<String, String>();

	private Handler handlerForCall = new Handler();

	private CallDispatcher callDisp;

	private XmlComposer xmlComposer = new XmlComposer();

	public static Vector<BuddyInformationBean> ownerList = new Vector<BuddyInformationBean>();

	public static boolean ownerStatus = true;

	public static BGProcessor getInstance() {
		if (bgProcessor == null) {
			bgProcessor = new BGProcessor();

		}
		return bgProcessor;
	}

	// public BGProcessor(){
	// SingleInstance.backgroundContext=this;
	//
	// }
	public void getProfileTemplate() {
		Log.i("BackGround", "getProfileTemplate");
		String modifiedDate = DBAccess
				.getdbHeler(SingleInstance.mainContext)
				.getModifiedDate(
						"select profiletimestamp from profiletemplate where profileid=5");
		if (modifiedDate == null) {
			modifiedDate = "";
		} else if (modifiedDate.trim().length() == 0) {
			modifiedDate = "";
		}
		// WebServiceReferences.webServiceClient.getProfileTemplate(modifiedDate);
		GetProfileTemplate(modifiedDate, this);
		Log.i("BackGround", "getProfileTemplate" + this);

		scheduleFileDownloader();

	}

	private void scheduleFileDownloader() {
		if (fileDownloader == null && download_timer == null) {
			fileDownloader = new FileDownloader();
			download_timer = new Timer();
			download_timer.schedule(fileDownloader, 1000 * 60 * 5,
					1000 * 60 * 5);
		}
	}

	public void GetProfileTemplate(String modifiedDate, Object obj) {
		Log.i("BackGround", "getProfileTemplate with object");

		WSBean wsBean = new WSBean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		if (modifiedDate != null || modifiedDate != "")
			property_map.put("profileTemplateModifiedTime", modifiedDate);
		else
			property_map.put("profileTemplateModifiedTime", "");

		wsBean.setWsMethodName("GetStandardProfileTemplate");
		wsBean.setProperty_map(property_map);
		wsBean.setServiceMethods(EnumWebServiceMethods.GETPROFILETEMPLATE);
		wsBean.setCallBack(obj);
		BackgroundWSProcessor.getInstance().getQueue().addObject(wsBean);
		// if (wsNotifier != null)
		// wsNotifier.addTasktoExecute(wsBean);

	}

	// public void getStandardProfilefieldvalues(){
	// Servicebean servicebean = new Servicebean();
	// String getStdProfileFieldValuesXml = xmlComposer
	// .composeGetStandardProfileFieldValuesXML(params);
	// HashMap<String, String> property_map = new HashMap<String, String>();
	// property_map.put("profileXml", getStdProfileFieldValuesXml);
	// servicebean.setProperty_map(property_map);
	// servicebean.setWsmethodname("GetStandardProfileFieldValues");
	// servicebean
	// .setServiceMethods(EnumWebServiceMethods.GETPROFILEFIELDVALUES);
	// if (wsNotifier != null)
	// wsNotifier.addTasktoExecutor(servicebean);
	//
	// }

	public void notifyProfileTemplate(final WSBean wsBean) {
		// TODO Auto-generated method stub

		try {
			Log.i("BackGround", "notifyProfileTemplate before if");

			// TODO Auto-generated method stub
			if (wsBean.getResultObject() instanceof ArrayList) {

				Log.i("BackGround", "notifyProfileTemplate");

				ArrayList<Object> response = (ArrayList<Object>) wsBean
						.getResultObject();
				String profileId = null;
				for (Object object : response) {
					if (object instanceof String)
						Log.d("Profile", "GETPROFILETEMPLATE response ---->"
								+ object);

					if (object instanceof ArrayList) {
						ArrayList<ProfileTemplateBean> bean_list = (ArrayList<ProfileTemplateBean>) object;
						for (ProfileTemplateBean pBean : bean_list) {
							ContentValues cv = new ContentValues();
							cv.put("profileid", pBean.getProfileId());
							cv.put("profilename", pBean.getProfileName());
							cv.put("profiletimestamp", pBean.getModifiedDate());

							profileId = pBean.getProfileId();

							if (!DBAccess
									.getdbHeler(SingleInstance.mainContext)
									.isRecordExists(
											"select * from profiletemplate where profileid="
													+ pBean.getProfileId())) {
								int id = DBAccess.getdbHeler(
										SingleInstance.mainContext)
										.insertProfileTemplate(cv);
								Log.i("clone", "---->id" + id);
							} else {
								cv.remove("profileid");
								DBAccess.getdbHeler(SingleInstance.mainContext)
										.updateProfileTemplate(
												cv,
												"profileid="
														+ pBean.getProfileId());
							}

						}
					}
					if (object instanceof Vector) {
						Vector<FieldTemplateBean> field_list = (Vector<FieldTemplateBean>) object;
						for (FieldTemplateBean bean : field_list) {
							ContentValues cv = new ContentValues();
							if (profileId != null) {
								cv.put("profileid", profileId);
							}
							cv.put("groupname", bean.getGroupName());
							cv.put("fieldid", bean.getFieldId());
							cv.put("fieldname", bean.getFieldName());
							cv.put("fieldtype", bean.getFieldType());
							cv.put("createddate", bean.getCreatedDate());
							cv.put("modifieddate", bean.getModifiedDate());
							if (!DBAccess
									.getdbHeler(SingleInstance.mainContext)
									.isRecordExists(
											"select * from fieldtemplate where fieldid="
													+ bean.getFieldId())) {
								int id = DBAccess.getdbHeler(
										SingleInstance.mainContext)
										.insertFieldTemplate(cv);
								Log.i("clone", "---->id" + id);
							} else {
								cv.remove("fieldid");
								DBAccess.getdbHeler(SingleInstance.mainContext)
										.updateFieldTemplate(cv,
												"fieldid=" + bean.getFieldId());
							}
							ContentValues cv2 = new ContentValues();
							cv2.put("groupid", bean.getGroupId());
							cv2.put("groupname", bean.getGroupName());
							if (!DBAccess
									.getdbHeler(SingleInstance.mainContext)
									.isRecordExists(
											"select * from profilegroup where groupid="
													+ bean.getGroupId()
													+ " and groupname='"
													+ bean.getGroupName() + "'")) {
								int id = DBAccess.getdbHeler(
										SingleInstance.mainContext)
										.insertProfileGroup(cv2);
								Log.i("clone", "---->id" + id);
							}
						}
					}

				}
				// if (appMainActivity.profileDetails != null) {
				// for (String[] buddyprofile_info :
				// appMainActivity.profileDetails) {

				if (appMainActivity.profileDetails != null) {

					// Vector<BuddyInformationBean> templist = new
					// Vector<BuddyInformationBean>();
					// templist.addAll(ownerList);
					// String modified_Date = DB
					// .getdbHeler(
					// SingleInstance.mainContext)
					// .getModifiedDate(
					// "select max(modifieddate) from profilefieldvalues where userid='"
					// + CallDispatcher.LoginUser
					// + "'");
					// String[] profileDetail = new String[3];
					// profileDetail[0] = CallDispatcher.LoginUser;
					// profileDetail[1] = "5";
					// profileDetail[2] = modified_Date;
					// getStandardProfilefieldvalues(profileDetail);

					Log.i("BackGround", "appMainActivity.profileDetails"
							+ appMainActivity.profileDetails.size());

					for (String[] buddyprofile_info : appMainActivity.profileDetails) {
						if (buddyprofile_info != null) {
							BuddyInformationBean bib = null;
							Vector<BuddyInformationBean> tempList = new Vector<BuddyInformationBean>();
							tempList.addAll(ContactsFragment.getBuddyList());
							tempList.addAll(ownerList);
							for (BuddyInformationBean temp : tempList) {
								if (!temp.isTitle()) {
									if (temp.getName().equalsIgnoreCase(
											buddyprofile_info[0])) {
										bib = temp;
										break;
									}
								}
							}

							if (bib != null
									&& !bib.getStatus().equalsIgnoreCase(
											"pending")) {
								if (buddyprofile_info.length > 0) {
									if (buddyprofile_info[2].length() == 0) {
										Vector<String> multimediaFields = DBAccess
												.getdbHeler(
														SingleInstance.mainContext)
												.getMultimediaFields(
														"SELECT fieldvalue FROM profilefieldvalues WHERE fieldid IN (SELECT fieldid FROM fieldtemplate WHERE fieldtype = 'Audio' OR fieldtype = 'Video' OR fieldtype = 'Photo') and userid = '"
																+ buddyprofile_info[0]
																+ "'");
										if (multimediaFields != null
												&& multimediaFields.size() > 0) {
											for (String fieldName : multimediaFields) {
												File file = new File(
														Environment
																.getExternalStorageDirectory()
																.getAbsoluteFile()
																+ "/COMMedia/"
																+ fieldName);
												if (file.exists())
													file.delete();
											}
										}
										DBAccess.getdbHeler(
												SingleInstance.mainContext)
												.deleteFieldValues(
														"DELETE from profilefieldvalues WHERE userid="
																+ "\""
																+ buddyprofile_info[0]
																		.toString()
																		.trim()
																+ "\"");

									} else {
										String modified_date = DBAccess
												.getdbHeler(
														SingleInstance.mainContext)
												.getModifiedDate(
														"select max(modifieddate) from profilefieldvalues where userid='"
																+ buddyprofile_info[0]
																+ "'");
										if (modified_date == null)
											modified_date = "";
										else if (modified_date.trim().length() == 0)
											modified_date = "";

										if (buddyprofile_info[2] != null) {
											if (!buddyprofile_info[2]
													.equals(modified_date)) {
												String[] profileDetails = new String[3];
												profileDetails[0] = buddyprofile_info[0];
												profileDetails[1] = "5";
												profileDetails[2] = modified_date;
												// WebServiceReferences.webServiceClient
												// .getStandardProfilefieldvalues(profileDetails);
												getStandardProfilefieldvalues(profileDetails);
											}
										}
									}
								}
							}
						}
					}

					appMainActivity.profileDetails.clear();

				}

				if (SingleInstance.instanceTable.containsKey("createprofile")) {
					

					CreateProfileFragment pFragment = (CreateProfileFragment) SingleInstance.instanceTable
							.get("createprofile");
					pFragment.getProfileFields();
					if (pFragment.profileFieldList.size() > 0) {
						pFragment.clearAllViews();
						for (FieldTemplateBean fieldtemplate : pFragment.profileFieldList) {
							pFragment.inflateFields(0, fieldtemplate);
						}
					}

				}

			} else {
				if (wsBean.getResultObject() instanceof WebServiceBean) {
					if (((WebServiceBean) wsBean.getResultObject()).getText() != null
							&& ((WebServiceBean) wsBean.getResultObject())
									.getText().equalsIgnoreCase(
											"No updates in profile template")) {

						if (appMainActivity.profileDetails != null) {
							Log.i("BackGround",
									"appMainActivity.profileDetails"
											+ appMainActivity.profileDetails
													.size());
							String buddy=null;
							for (String[] buddyprofile_info : appMainActivity.profileDetails) {
								if (buddyprofile_info != null) {
									BuddyInformationBean bib = null;
									buddy=buddyprofile_info[0];
									for (BuddyInformationBean temp : ContactsFragment
											.getBuddyList()) {
										if (!temp.isTitle()) {
											if (temp.getName()
													.equalsIgnoreCase(
															buddyprofile_info[0])) {
												bib = temp;
												break;
											}
										}
									}
									if (bib != null
											&& !bib.getStatus()
													.equalsIgnoreCase("pending")) {
										if (buddyprofile_info.length > 0) {
											if (buddyprofile_info[2].length() == 0) {
												Vector<String> multimediaFields = DBAccess
														.getdbHeler(
																SingleInstance.mainContext)
														.getMultimediaFields(
																"SELECT fieldvalue FROM profilefieldvalues WHERE fieldid IN (SELECT fieldid FROM fieldtemplate WHERE fieldtype = 'Audio' OR fieldtype = 'Video' OR fieldtype = 'Photo') and userid = '"
																		+ buddyprofile_info[0]
																		+ "'");
												if (multimediaFields != null
														&& multimediaFields
																.size() > 0) {
													for (String fieldName : multimediaFields) {
														File file = new File(
																Environment
																		.getExternalStorageDirectory()
																		.getAbsoluteFile()
																		+ "/COMMedia/"
																		+ fieldName);
														if (file.exists())
															file.delete();
													}
												}
												DBAccess.getdbHeler(
														SingleInstance.mainContext)
														.deleteFieldValues(
																"DELETE from profilefieldvalues WHERE userid="
																		+ "\""
																		+ buddyprofile_info[0]
																				.toString()
																				.trim()
																		+ "\"");

											} else {
												String modified_date = DBAccess
														.getdbHeler(
																SingleInstance.mainContext)
														.getModifiedDate(
																"select max(modifieddate) from profilefieldvalues where userid='"
																		+ buddyprofile_info[0]
																		+ "'");
												if (modified_date == null)
													modified_date = "";
												else if (modified_date.trim()
														.length() == 0)
													modified_date = "";

												if (buddyprofile_info[2] != null) {
													if (!buddyprofile_info[2]
															.equals(modified_date)) {
														String[] profileDetails = new String[3];
														profileDetails[0] = buddyprofile_info[0];
														profileDetails[1] = "5";
														profileDetails[2] = modified_date;
														// WebServiceReferences.webServiceClient
														// .getStandardProfilefieldvalues(profileDetails);
														getStandardProfilefieldvalues(profileDetails);
													}
												}
											}
										}
									}
								}
							}

							appMainActivity.profileDetails.clear();
							if(buddy!=null)
							DBAccess.getdbHeler().deleteProfile(
									buddy);
						}

					}
				}
				Log.d("Profile", "GETPROFILETEMPLATE result--->"
						+ ((WebServiceBean) wsBean.getResultObject()).getText());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated method stub

	}

	public void getStandardProfilefieldvalues(String[] params) {
		// TODO Auto-generated method stub

		WSBean wsBean = new WSBean();
		Log.i("BackGround", "getStandardProfilefieldvalues");
		String getStdProfileFieldValuesXml = xmlComposer
				.composeGetStandardProfileFieldValuesXML(params);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("profileXml", getStdProfileFieldValuesXml);
		wsBean.setProperty_map(property_map);
		wsBean.setWsMethodName("GetStandardProfileFieldValues");
		wsBean.setServiceMethods(EnumWebServiceMethods.GETPROFILEFIELDVALUES);
		// if (wsNotifier != null)
		// wsNotifier.addTasktoExecutor(servicebean);
		wsBean.setCallBack(this);
		BackgroundWSProcessor.getInstance().getQueue().addObject(wsBean);
	}

	public void notifyProfileValues(WSBean wsBean) {
		// TODO Auto-generated method stub
		if (wsBean.getResultObject() instanceof ArrayList) {
			Log.i("BackGround", "notifyProfileValues");

			ArrayList<Object> resut = (ArrayList<Object>) wsBean
					.getResultObject();
			String[] profile_info = new String[4];
			profile_info = (String[]) resut.get(0);
			String profileOwner = profile_info[1];
			if (resut.size() > 1) {
				ArrayList<FieldTemplateBean> filed_values = (ArrayList<FieldTemplateBean>) resut
						.get(1);
				for (FieldTemplateBean fieldTemplateBean : filed_values) {

					ContentValues cv = new ContentValues();
					if (fieldTemplateBean.getFiledvalue() != null
							&& !fieldTemplateBean.getFiledvalue()
									.equalsIgnoreCase("null")
							&& fieldTemplateBean.getFieldId() != null) {

						cv.put("fieldid", fieldTemplateBean.getFieldId());
						cv.put("modifieddate",
								fieldTemplateBean.getField_modifieddate());
						cv.put("createddate",
								fieldTemplateBean.getField_createddate());
						cv.put("userid", profileOwner);

						cv.put("fieldvalue", fieldTemplateBean.getFiledvalue());

						if (multimediaFieldsValues.size() > 0)
							multimediaFieldsValues.clear();

						multimediaFieldsValues = DBAccess
								.getdbHeler(SingleInstance.mainContext)
								.getMultimediaFieldValues(
										"SELECT fieldid,fieldvalue FROM profilefieldvalues WHERE fieldid IN (SELECT fieldid FROM fieldtemplate WHERE fieldtype = 'Audio' OR fieldtype = 'Video' OR fieldtype = 'Photo') and userid = '"
												+ profileOwner + "'");
						Log.i("profile", "====> get profile multimedia");
						if (multimediaFieldsValues != null
								&& multimediaFieldsValues.size() > 0) {
							Log.i("profile",
									"====> inside get profile multimedia");
							if (multimediaFieldsValues
									.containsKey(fieldTemplateBean.getFieldId())) {
								String fieldValue = multimediaFieldsValues
										.get(fieldTemplateBean.getFieldId());
								if (!fieldValue
										.equalsIgnoreCase(fieldTemplateBean
												.getFiledvalue())) {
									Log.i("profile",
											"====> inside get profile multimedia file exists");
									File file = new File(Environment
											.getExternalStorageDirectory()
											.getAbsolutePath()
											+ "/COMMedia/" + fieldValue);
									if (file.exists()) {
										Log.i("profile",
												"====> inside get profile multimedia file delete");
										file.delete();
									}
								}
							}
						}

						String profile_multimedia = Environment
								.getExternalStorageDirectory()
								+ "/COMMedia/"
								+ fieldTemplateBean.getFiledvalue();
						File msg_file = new File(profile_multimedia);
						String f_type = DBAccess.getdbHeler(
								SingleInstance.mainContext).getFieldType(
								fieldTemplateBean.getFieldId());

						if (msg_file.exists())
							cv.put("status", 2);
						else {

							if (f_type != null
									&& f_type.equalsIgnoreCase("photo")
									|| f_type.equalsIgnoreCase("video")
									|| f_type.equalsIgnoreCase("audio")
									|| f_type.equalsIgnoreCase("multimedia"))
								cv.put("status", 0);
							else
								cv.put("status", 2);

						}
						boolean isUpdated = false;

						if (!DBAccess.getdbHeler(SingleInstance.mainContext)
								.isRecordExists(
										"select * from profilefieldvalues where fieldid="
												+ fieldTemplateBean
														.getFieldId()
												+ " and userid='"
												+ profileOwner + "'")) {

							DBAccess.getdbHeler(SingleInstance.mainContext)
									.insertProfileFieldValues(cv);

						} else {
							cv.remove("fieldid");
							DBAccess.getdbHeler(SingleInstance.mainContext)
									.updateProfileFieldValues(
											cv,
											"fieldid="
													+ fieldTemplateBean
															.getFieldId()
													+ " and userid='"
													+ profileOwner + "'");

						}

						if (fieldTemplateBean.getFieldId().equals("3"))
							isUpdated = true;

					

						if (!msg_file.exists()) {

							if (f_type != null) {
								if (f_type.equalsIgnoreCase("photo")
										|| f_type.equalsIgnoreCase("video")
										|| f_type.equalsIgnoreCase("audio")
										|| f_type
												.equalsIgnoreCase("multimedia")) {
									Log.d("profile", "Field value is :"
											+ fieldTemplateBean.getFiledvalue());
									if (fieldTemplateBean.getFieldId().equals(
											"3")) {

										for (BuddyInformationBean temp : ContactsFragment
												.getBuddyList()) {
											if (!temp.isTitle()) {
												if (temp.getName()
														.equalsIgnoreCase(
																fieldTemplateBean
																		.getUsername())) {

													temp.setProfile_picpath(fieldTemplateBean
															.getFiledvalue());
													break;
												}
											}
										}

									}
									downloadOfflineresponse(
											fieldTemplateBean.getFiledvalue(),
											fieldTemplateBean.getFieldId()
													+ "," + profileOwner + ","
													+ isUpdated,
											"profile field", null);
								}

							}
						} else {
							if (profileOwner
									.equalsIgnoreCase(CallDispatcher.LoginUser)) {
								SingleInstance.mainContext.setProfilePic();
								if (SingleInstance.contextTable
										.containsKey("settings")) {
									SettingsFragment settingContext = SettingsFragment
											.newInstance(SingleInstance.mainContext);
									settingContext
											.notifyProfilePictureDownloaded();
								}

									ContactsFragment contactsContext = ContactsFragment
											.getInstance(SingleInstance.mainContext);
									contactsContext
											.notifyProfilePictureDownloaded();

							}
							// comparewithStateanddistance();
							// profilePictureDownloaded();
						}

						msg_file = null;
					}

				}
			}

		} else if (wsBean.getResultObject() instanceof WebServiceBean) {
			Log.d("Profile",
					"Error message is --->"
							+ ((WebServiceBean) wsBean.getResultObject())
									.getText());
		}

		handlerForCall.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// cancelDialog();
				if (SingleInstance.contextTable.containsKey("groupchat")) {
					((GroupChatActivity) SingleInstance.contextTable
							.get("groupchat")).notifyViewProfile();
				} else if (WebServiceReferences.contextTable
						.containsKey("answeringmachine")) {
					((AnsweringMachineActivity) WebServiceReferences.contextTable
							.get("answeringmachine")).notifyViewProfile();
				} else if (WebServiceReferences.contextTable
						.containsKey("buddiesList")) {
					(ContactsFragment.getInstance(SingleInstance.mainContext))
							.notifyViewProfile(true);
				}
			}
		});

	}

	public void downloadOfflineresponse(String path, String id, String from,
			Object obj) {
		try {
			Log.d("FTP", "Came to downloadofflinecall Response----->" + id);
			AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
					.get("MAIN");
			FTPBean ftpBean = new FTPBean();
			if (CallDispatcher.LoginUser != null) {
				if (from.equalsIgnoreCase("profile field")) {
					if (id != null) {
						String[] details = id.split(",");
						if (details[0] != null && details[1] != null) {
							ContentValues cv = new ContentValues();
							cv.put("status", 1);
							DBAccess.getdbHeler(SingleInstance.mainContext)
									.updateProfileFieldValues(
											cv,
											"fieldid=" + details[0]
													+ " and userid='"
													+ details[1] + "'");

						}
					}
					ftpBean.setReq_object(id);
				} else if (from.equalsIgnoreCase("files")) {
					if (obj != null) {
						if (obj instanceof ShareReminder) {

							ftpBean.setShare((ShareReminder) obj);
							ftpBean.setReq_object((ShareReminder) obj);

						}
					}
				} else if (from.equalsIgnoreCase("answering machine")) {
					Log.i("avatar123", "inside avatar download 2");
					ContentValues cv = new ContentValues();
					cv.put("status", 1);
					DBAccess.getdbHeler(SingleInstance.mainContext)
							.updateOfflineCallPendingClones(cv, "id=" + id);
					cv = null;
					ftpBean.setReq_object(id);
				}

				if (path != null && path.trim().length() > 0
						&& !path.equalsIgnoreCase("null")) {
					ftpBean.setServer_ip(appMainActivity.cBean.getRouter()
							.split(":")[0]);
					ftpBean.setServer_port(40400);
					if (obj instanceof ShareReminder) {
						ftpBean.setFtp_username(((ShareReminder) obj).getFrom());
						ftpBean.setFtp_password(((ShareReminder) obj)
								.getFtpPassword());
					} else {
						ftpBean.setFtp_username("ftpadmin");
						ftpBean.setFtp_password("ftppassword");
					}
					Log.i("avatar123", "inside avatar download 2 filename : "
							+ path);
					ftpBean.setFile_path(path);
					ftpBean.setOperation_type(2);
					ftpBean.setRequest_from(from);
					appMainActivity.getFtpNotifier().addTasktoExecutor(ftpBean);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getMyGroupChatSettings(String userName) {
		String[] params = new String[3];
		params[0] = userName;
		params[1] = "";
		params[2] = "";
		WebServiceReferences.webServiceClient.getGroupChatSettings(params);
	}

	public void getSyncUtility() {
		// TODO Auto-generated method stub
		System.out.println("BGProcessor  getSyncUtility enter");
		Log.d("BGProcessor", "getSyncUtility");
		if (WebServiceReferences.running) {
			getBlockedBuddyList(CallDispatcher.LoginUser, this);
			Vector<UtilityBean> utility_list = DBAccess.getdbHeler(
					SingleInstance.mainContext).SelectUtilityRecords(
					"select * from utility where userid='"
							+ CallDispatcher.LoginUser + "'");

			String utilityName = "buysell";
			syncUtilityItems(CallDispatcher.LoginUser, utilityName,
					utility_list, this);
			utilityName = "serviceneededprovider";
			syncUtilityItems(CallDispatcher.LoginUser, utilityName,
					utility_list, this);

		}

	}

	public void syncUtilityItems(String userId, String utilityName,

	Vector<UtilityBean> utilityList, Object obj) {

		Log.d("BGProcessor", "getSyncUtilityItems enter request");
		// System.out.println("BGProcessor  getSyncUtilityItems enter");

		WSBean wsBean = new WSBean();

		String utilityItemsXml = xmlComposer.composeGetUtilitySyncItemsXML(
				userId, utilityName, utilityList);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("utilityXml", utilityItemsXml);
		wsBean.setProperty_map(property_map);
		wsBean.setWsMethodName("SyncUtilityItems");
		wsBean.setServiceMethods(EnumWebServiceMethods.SYNCUTILITYITEMS);
		wsBean.setCallBack(obj);
		BackgroundWSProcessor.getInstance().getQueue().addObject(wsBean);

		Log.d("BGProcessor", "getSyncUtilityItems end");
		// System.out.println("BGProcessor getSyncUtilityItems request end");

	}

	/*
	 * For New Utility Layout
	 */

	// public void notifyUtilityResponse(WSBean wsBean) {
	// System.out.println("BGProcessor notifyutility enter");
	// Log.d("BGProcessor", "notifySyncUtilityItems enter");
	//
	// // TODO Auto-generated method stub
	// if (wsBean.getResultObject() instanceof Syncutilitybean) {
	// Syncutilitybean sync_bean = (Syncutilitybean) wsBean
	// .getResultObject();
	// if (sync_bean.getMissing_utility() != null) {
	// for (UtilityBean bean : sync_bean.getMissing_utility()) {
	// ContentValues cv = new ContentValues();
	// cv.put("userid", bean.getUsername());
	// cv.put("org_name", bean.getNameororg());
	// cv.put("product_name", bean.getProduct_name());
	// cv.put("quantity", bean.getQty());
	// cv.put("price", bean.getPrice());
	// if (bean.getVideofilename() != null
	// && bean.getVideofilename().length() > 0) {
	// cv.put("video_file", Utils.getFilePathString(bean
	// .getVideofilename()));
	// } else {
	// cv.put("video_file", bean.getVideofilename());
	// }
	// if (bean.getImag_filename() != null
	// && bean.getImag_filename().length() > 0) {
	// cv.put("img_file", Utils.getFilePathString(bean
	// .getImag_filename()));
	// } else {
	// cv.put("img_file", bean.getImag_filename());
	// }
	// if (bean.getAudiofilename() != null
	// && bean.getAudiofilename().length() > 0) {
	// cv.put("voice", Utils.getFilePathString(bean
	// .getAudiofilename()));
	// } else {
	// cv.put("voice", bean.getAudiofilename());
	// }
	// cv.put("location", bean.getLocation());
	// cv.put("address", bean.getAddress());
	// cv.put("country", bean.getCountry());
	// cv.put("state", bean.getState());
	// cv.put("city", bean.getCityordist());
	// cv.put("pin", bean.getPin());
	// cv.put("email", bean.getEmail());
	// cv.put("c_no", bean.getC_no());
	// cv.put("entry_mode", bean.getMode());
	// cv.put("utility_name", bean.getUtility_name());
	// cv.put("posted_date", bean.getPosted_date());
	// cv.put("id", bean.getId());
	// if (!DBAccess.getdbHeler(SingleInstance.mainContext)
	// .isRecordExists(
	// "select * from utility where id="
	// + bean.getId()))
	// DBAccess.getdbHeler(SingleInstance.mainContext)
	// .insertUtility(cv);
	// else
	// DBAccess.getdbHeler(SingleInstance.mainContext)
	// .UpdateUtility(cv, bean.getPosted_date());
	// if (bean.getAudiofilename() != null) {
	// if (bean.getAudiofilename().trim().length() > 0) {
	// File fle = new File(
	// Environment.getExternalStorageDirectory()
	// + "/COMMedia/"
	// + bean.getAudiofilename());
	// if (!fle.exists())
	// downloadOfflineresponse(
	// bean.getAudiofilename(), null, null);
	//
	// }
	// }
	// if (bean.getVideofilename() != null) {
	// if (bean.getVideofilename().trim().length() > 0) {
	// File fle = new File(
	// Environment.getExternalStorageDirectory()
	// + "/COMMedia/"
	// + bean.getVideofilename());
	// if (!fle.exists())
	// downloadOfflineresponse(
	// bean.getVideofilename(), null, null);
	//
	// }
	// }
	// if (bean.getImag_filename() != null) {
	// if (bean.getImag_filename().trim().length() > 0) {
	// String[] file_names = bean.getImag_filename()
	// .split(",");
	// for (String names : file_names) {
	// File fle = new File(
	// Environment
	// .getExternalStorageDirectory()
	// + "/COMMedia/" + names);
	// if (!fle.exists())
	// downloadOfflineresponse(names, null, null);
	// }
	//
	// }
	// }
	//
	// }
	//
	// }
	// if (sync_bean.getUpdated_utility() != null) {
	// for (UtilityBean bean : sync_bean.getUpdated_utility()) {
	//
	// ContentValues cv = new ContentValues();
	// cv.put("userid", bean.getUsername());
	// cv.put("org_name", bean.getNameororg());
	// cv.put("product_name", bean.getProduct_name());
	// cv.put("quantity", bean.getQty());
	// cv.put("price", bean.getPrice());
	// cv.put("video_file", bean.getVideofilename());
	// cv.put("img_file", bean.getImag_filename());
	// cv.put("voice", bean.getAudiofilename());
	// cv.put("location", bean.getLocation());
	// cv.put("address", bean.getAddress());
	// cv.put("country", bean.getCountry());
	// cv.put("state", bean.getState());
	// cv.put("city", bean.getCityordist());
	// cv.put("pin", bean.getPin());
	// cv.put("email", bean.getEmail());
	// cv.put("c_no", bean.getC_no());
	// cv.put("entry_mode", bean.getMode());
	// cv.put("utility_name", bean.getUtility_name());
	// cv.put("posted_date", bean.getPosted_date());
	// cv.put("id", bean.getId());
	// if (!DBAccess.getdbHeler(SingleInstance.mainContext)
	// .isRecordExists(
	// "select * from utility where id="
	// + bean.getId()))
	// DBAccess.getdbHeler(SingleInstance.mainContext)
	// .insertUtility(cv);
	// else
	// DBAccess.getdbHeler(SingleInstance.mainContext)
	// .UpdatesyncUtility(cv, bean.getId());
	//
	// if (bean.getAudiofilename() != null) {
	// if (bean.getAudiofilename().trim().length() > 0) {
	// File fle = new File(
	// Environment.getExternalStorageDirectory()
	// + "/COMMedia/"
	// + bean.getAudiofilename());
	// if (!fle.exists())
	// callDisp.downloadOfflineresponse(
	// bean.getAudiofilename(), null,
	// "utilities", null);
	//
	// }
	// }
	// if (bean.getVideofilename() != null) {
	// if (bean.getVideofilename().trim().length() > 0) {
	// File fle = new File(
	// Environment.getExternalStorageDirectory()
	// + "/COMMedia/"
	// + bean.getVideofilename());
	// if (!fle.exists())
	// callDisp.downloadOfflineresponse(
	// bean.getVideofilename(), null,
	// "utilities", null);
	//
	// }
	// }
	// if (bean.getImag_filename() != null) {
	// if (bean.getImag_filename().trim().length() > 0) {
	// String[] file_names = bean.getImag_filename()
	// .split(",");
	// for (String names : file_names) {
	// File fle = new File(
	// Environment
	// .getExternalStorageDirectory()
	// + "/COMMedia/" + names);
	// if (!fle.exists())
	// callDisp.downloadOfflineresponse(names,
	// null, "utilities", null);
	//
	// }
	//
	// }
	// }
	//
	// }
	// }
	// if (sync_bean.getDeleted_utility() != null) {
	// for (UtilityBean utilityBean : sync_bean.getDeleted_utility()) {
	// DBAccess.getdbHeler(SingleInstance.mainContext)
	// .deleteUtility(
	// Integer.toString(utilityBean.getId()));
	// }
	// }
	// }
	//
	// Log.d("BGProcessor", "notifySyncUtilityItems end");
	//
	// }

	/*
	 * Ends here
	 */

	public void notifyUtilityResponse(WSBean wsBean) {
		System.out.println("BGProcessor notifyutility enter");
		Log.d("BGProcessor", "notifySyncUtilityItems enter");

		// TODO Auto-generated method stub
		if (wsBean.getResultObject() instanceof Syncutilitybean) {
			Syncutilitybean sync_bean = (Syncutilitybean) wsBean
					.getResultObject();
			if (sync_bean.getMissing_utility() != null) {
				for (UtilityBean bean : sync_bean.getMissing_utility()) {
					ContentValues cv = new ContentValues();
					cv.put("userid", bean.getUsername());
					cv.put("org_name", bean.getNameororg());
					cv.put("product_name", bean.getProduct_name());
					cv.put("quantity", bean.getQty());
					cv.put("price", bean.getPrice());
					cv.put("video_file", bean.getVideofilename());
					cv.put("img_file", bean.getImag_filename());
					cv.put("voice", bean.getAudiofilename());
					cv.put("location", bean.getLocation());
					cv.put("address", bean.getAddress());
					cv.put("country", bean.getCountry());
					cv.put("state", bean.getState());
					cv.put("city", bean.getCityordist());
					cv.put("pin", bean.getPin());
					cv.put("email", bean.getEmail());
					cv.put("c_no", bean.getC_no());
					cv.put("entry_mode", bean.getMode());
					cv.put("utility_name", bean.getUtility_name());
					cv.put("posted_date", bean.getPosted_date());
					cv.put("id", bean.getId());
					if (!DBAccess.getdbHeler(SingleInstance.mainContext)
							.isRecordExists(
									"select * from utility where id="
											+ bean.getId()))
						DBAccess.getdbHeler(SingleInstance.mainContext)
								.insertUtility(cv);
					else
						DBAccess.getdbHeler(SingleInstance.mainContext)
								.UpdateUtility(cv, bean.getPosted_date());
					if (bean.getAudiofilename() != null) {
						if (bean.getAudiofilename().trim().length() > 0) {
							File fle = new File(
									Environment.getExternalStorageDirectory()
											+ "/COMMedia/"
											+ bean.getAudiofilename());
							if (!fle.exists())
								downloadOfflineresponse(
										bean.getAudiofilename(), null,
										"utilities", null);

						}
					}
					if (bean.getVideofilename() != null) {
						if (bean.getVideofilename().trim().length() > 0) {
							File fle = new File(
									Environment.getExternalStorageDirectory()
											+ "/COMMedia/"
											+ bean.getVideofilename());
							if (!fle.exists())
								downloadOfflineresponse(
										bean.getVideofilename(), null,
										"utilities", null);

						}
					}
					if (bean.getImag_filename() != null) {
						if (bean.getImag_filename().trim().length() > 0) {
							String[] file_names = bean.getImag_filename()
									.split(",");
							for (String names : file_names) {
								File fle = new File(
										Environment
												.getExternalStorageDirectory()
												+ "/COMMedia/" + names);
								if (!fle.exists())
									downloadOfflineresponse(names, null,
											"utilities", null);
							}

						}
					}

				}

			}
			if (sync_bean.getUpdated_utility() != null) {
				for (UtilityBean bean : sync_bean.getUpdated_utility()) {

					ContentValues cv = new ContentValues();
					cv.put("userid", bean.getUsername());
					cv.put("org_name", bean.getNameororg());
					cv.put("product_name", bean.getProduct_name());
					cv.put("quantity", bean.getQty());
					cv.put("price", bean.getPrice());
					cv.put("video_file", bean.getVideofilename());
					cv.put("img_file", bean.getImag_filename());
					cv.put("voice", bean.getAudiofilename());
					cv.put("location", bean.getLocation());
					cv.put("address", bean.getAddress());
					cv.put("country", bean.getCountry());
					cv.put("state", bean.getState());
					cv.put("city", bean.getCityordist());
					cv.put("pin", bean.getPin());
					cv.put("email", bean.getEmail());
					cv.put("c_no", bean.getC_no());
					cv.put("entry_mode", bean.getMode());
					cv.put("utility_name", bean.getUtility_name());
					cv.put("posted_date", bean.getPosted_date());
					cv.put("id", bean.getId());
					if (!DBAccess.getdbHeler(SingleInstance.mainContext)
							.isRecordExists(
									"select * from utility where id="
											+ bean.getId()))
						DBAccess.getdbHeler(SingleInstance.mainContext)
								.insertUtility(cv);
					else
						DBAccess.getdbHeler(SingleInstance.mainContext)
								.UpdatesyncUtility(cv, bean.getId());

					if (bean.getAudiofilename() != null) {
						if (bean.getAudiofilename().trim().length() > 0) {
							File fle = new File(
									Environment.getExternalStorageDirectory()
											+ "/COMMedia/"
											+ bean.getAudiofilename());
							if (!fle.exists())
								downloadOfflineresponse(
										bean.getAudiofilename(), null,
										"utilities", null);

						}
					}
					if (bean.getVideofilename() != null) {
						if (bean.getVideofilename().trim().length() > 0) {
							File fle = new File(
									Environment.getExternalStorageDirectory()
											+ "/COMMedia/"
											+ bean.getVideofilename());
							if (!fle.exists())
								downloadOfflineresponse(
										bean.getVideofilename(), null,
										"utilities", null);

						}
					}
					if (bean.getImag_filename() != null) {
						if (bean.getImag_filename().trim().length() > 0) {
							String[] file_names = bean.getImag_filename()
									.split(",");
							for (String names : file_names) {
								File fle = new File(
										Environment
												.getExternalStorageDirectory()
												+ "/COMMedia/" + names);
								if (!fle.exists())
									downloadOfflineresponse(names, null,
											"utilities", null);

							}

						}
					}

				}
			}
			if (sync_bean.getDeleted_utility() != null) {
				for (UtilityBean utilityBean : sync_bean.getDeleted_utility()) {
					DBAccess.getdbHeler(SingleInstance.mainContext)
							.deleteUtility(
									Integer.toString(utilityBean.getId()));
				}
			}
		}

		Log.d("BGProcessor", "notifySyncUtilityItems end");

	}

	public void getBlockedBuddyList(String userId, Object obj) {
		// Servicebean servicebean = new Servicebean();
		WSBean wsBean = new WSBean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("userId", userId);
		wsBean.setProperty_map(property_map);
		wsBean.setWsMethodName("GetBlockedBuddyList");
		wsBean.setServiceMethods(EnumWebServiceMethods.GETBLOCKBUDDYLIST);
		wsBean.setCallBack(obj);
		BackgroundWSProcessor.getInstance().getQueue().addObject(wsBean);
	}

	public void notifyGETBLOCKBUDDYLIST(final WSBean wsBean) {
		// TODO Auto-generated method stub
		if (wsBean.getResultObject() instanceof ArrayList) {
			ArrayList<Object> blocked_list = (ArrayList<Object>) wsBean
					.getResultObject();
			blocked_buddies.clear();
			if (blocked_list != null) {
				for (Object object : blocked_list) {
					if (object instanceof String)
						blocked_buddies.add((String) object);
				}
			}

		}

	}

	public void getMyConfigurationDetails() {
		String dateTime = DBAccess.getdbHeler().getMaxDateofOfflineConfig(
				CallDispatcher.LoginUser);
		Log.i("BackGround", "getMyConfigurationDetails: ");

		if (dateTime == null) {
			dateTime = "\"\"";
		}

		String params[] = new String[2];
		params[0] = CallDispatcher.LoginUser;
		params[1] = dateTime;
		// if (WebServiceReferences.running) {
		GetMyConfigurationDetails(params, this);
		// }
	}

	private void GetMyConfigurationDetails(String[] params, Object obj) {
		// TODO Auto-generated method stub
		Log.i("BackGround", "GetMyConfigurationDetails ");

		WSBean wsBean = new WSBean();

		wsBean.setOption(params);
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("userId", params[0]);
		property_map.put("dateTime", params[1]);
		wsBean.setProperty_map(property_map);
		wsBean.setWsMethodName("GetMyConfigurationDetails");
		wsBean.setServiceMethods(EnumWebServiceMethods.GETMYCONFIGURATIONDETAILS);
		wsBean.setCallBack(obj);
		BackgroundWSProcessor.getInstance().getQueue().addObject(wsBean);

		// if (wsNotifier != null)
		// wsNotifier.addTasktoExecutor(servicebean);

	}

	public void notifyGetMyConfigurationDetails(final WSBean wsBean) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		if (wsBean.getResultObject() instanceof ArrayList) {
			Log.i("BackGround", "notifyGetMyConfigurationDetails: ");

			ArrayList<Object> getmyconfig_result = (ArrayList<Object>) wsBean
					.getResultObject();
			for (Object object : getmyconfig_result) {
				if (object instanceof String[]) {
					String[] config_info = (String[]) object;
					if (config_info[1] != null) {
						if (config_info[1].trim().length() > 0) {
							ArrayList<OfflineRequestConfigBean> config_list = DBAccess
									.getdbHeler().getOfflineSettingDetails(
											"where Id NOT IN(" + config_info[1]
													+ ")");
							if (config_list != null) {
								for (OfflineRequestConfigBean offlineRequestConfigBean : config_list) {
									if (offlineRequestConfigBean != null) {
										if (offlineRequestConfigBean
												.getMessagetype() != null) {
											if (!offlineRequestConfigBean
													.getMessagetype()
													.equalsIgnoreCase(
															"call forwarding")
													&& !offlineRequestConfigBean
															.getMessagetype()
															.equalsIgnoreCase(
																	"conference call")
													&& !offlineRequestConfigBean
															.getMessagetype()
															.equalsIgnoreCase(
																	"call forward to gsm")) {
												String path = offlineRequestConfigBean
														.getMessage();
												File config_path = new File(
														path);
												if (config_path.exists())
													config_path.delete();
											}
										}
										DBAccess.getdbHeler()
												.deleteOfflineCallSettingDetails(
														offlineRequestConfigBean
																.getId());
									}
								}
							}
						} else {
							if (getmyconfig_result.get(0) instanceof String) {
								String text = (String) getmyconfig_result
										.get(0);
								if (text.equalsIgnoreCase("No updates available")) {
									DBAccess.getdbHeler()
											.deleteOfflineCallSettingDetails(
													null);
								}
							}

						}
					}
				} else if (object instanceof ArrayList) {
					ArrayList<OfflineRequestConfigBean> bean_list = (ArrayList<OfflineRequestConfigBean>) object;
					for (OfflineRequestConfigBean result : bean_list) {
						ContentValues cv = new ContentValues();
						cv.put("Id", result.getId());
						cv.put("userid", CallDispatcher.LoginUser);
						if (result.getBuddyId()
								.equals(CallDispatcher.LoginUser))
							cv.put("buddyid", "default");
						else
							cv.put("buddyid", result.getBuddyId());

						cv.put("message_title", result.getMessageTitle());
						cv.put("message_type", result.getMessagetype());
						cv.put("message",
								Utils.getFilePathString(result.getMessage()));
						cv.put("when_action", result.getWhen());
						cv.put("status", 0);

						String path = Environment.getExternalStorageDirectory()
								+ "/COMMedia/" + result.getMessage();
						File fle = new File(path);

						if (!fle.exists())
							downloadOfflineresponse(result.getMessage(),
									result.getId(), "offline settings", null);

						cv.put("response_type", result.getResponseType());

						if (result.getUrl() == null)
							cv.put("url", "''");
						else
							cv.put("url", result.getUrl());

						if (result.getRecordTime() == null)
							cv.put("record_time", result.getRecordTime());
						else
							cv.put("record_time", result.getRecordTime());

						int row_id = DBAccess.getdbHeler()
								.insertOfflineCallSettingDetails(cv);
						Log.d("Avataar",
								"getmyconfigdetails insert response--->"
										+ row_id);
					}
				} else
					Log.d("Avataar",
							"Get My configuration Details response --->"
									+ object);

			}
		} else {
			Log.d("Avataar", "Get My configuration Details response --->"
					+ ((WebServiceBean) wsBean.getResultObject()).getText());
		}
		final ArrayList<OfflineRequestConfigBean> avatarList = DBAccess
				.getdbHeler().getOfflineSettingDetails(
						"where userid=" + "\"" + CallDispatcher.LoginUser
								+ "\"");
		AvatarActivity.BGProcessorProgress = true;

		if (WebServiceReferences.contextTable.containsKey("avatarset")) {
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					AvatarActivity avatarActivity = AvatarActivity
							.newInstance(SingleInstance.mainContext);
					if (WebServiceReferences.contextTable
							.containsKey("avatarset")) {

						avatarActivity.notifyUI(avatarList);
					}
				}
			});

		}

	}

	// public void updateOfflineFormEntry() {
	// Log.i("BackGround", "updateofflineentry");
	// ArrayList<OfflineFormRecordBean> totalList = new
	// ArrayList<OfflineFormRecordBean>();
	// ArrayList<org.lib.model.FormsListBean> list = DBAccess.getdbHeler()
	// .ownLookUpRecordss(CallDispatcher.LoginUser);
	// for (FormsListBean formsListBean : list) {
	// ArrayList<OfflineFormRecordBean> oList = DBAccess.getdbHeler()
	// .getOfflineRecordsofFormtbl(formsListBean.getForm_name());
	// totalList.addAll(oList);
	// }
	// for (OfflineFormRecordBean oBean : totalList) {
	// // addFormRecords(username, id, fields, values, this);
	// ArrayList<String> columnNameList = DBAccess.getdbHeler()
	// .getColumnNamesForOfflineRecord(oBean.getTableName());
	// if (columnNameList.size() > 0) {
	// String[] fields = columnNameList
	// .toArray(new String[columnNameList.size()]);
	// addFormRecords(oBean.getUuid(), oBean.getTableId(), fields,
	// oBean.get, this);
	// }
	// }
	//
	// scheduleFileDownloader();
	//
	// }

	private void addFormRecords(String username, String id, String[] fields,
			String[] values, Object obj) {
		String frnXML = xmlComposer.ComposeAddFormDataXML(username, id, fields,
				values);
		WSBean wsBean = new WSBean();
		HashMap<String, String> property_map = new HashMap<String, String>();
		property_map.put("formdataXml", frnXML);
		wsBean.setProperty_map(property_map);
		wsBean.setWsMethodName("AddFormData");
		wsBean.setServiceMethods(EnumWebServiceMethods.ADDFORMRECORDS);
		wsBean.setCallBack(obj);
		BackgroundWSProcessor.getInstance().getQueue().addObject(wsBean);

	}
}
