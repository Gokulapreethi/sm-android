package org.lib.webservice;

import android.content.Context;
import android.util.Log;

import com.bean.EditForm;
import com.bean.GroupChatBean;
import com.bean.GroupChatPermissionBean;
import com.bean.ProfileBean;
import com.cg.account.ChangePassword;
import com.cg.account.FindPeople;
import com.cg.account.MyAccountActivity;
import com.cg.account.NewUser;
import com.cg.account.PinAndTouchId;
import com.cg.account.ResetPassword;
import com.cg.account.Searchpeople;
import com.cg.account.SecurityQuestions;
import com.cg.account.forgotPassword;
import com.cg.avatar.AvatarActivity;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.FileInfoFragment;
import com.cg.files.sendershare;
import com.cg.forms.AccessAndSync;
import com.cg.forms.AddNewForm;
import com.cg.forms.AppsView;
import com.cg.forms.EditFormFields;
import com.cg.forms.FormDescription;
import com.cg.forms.FormFieldAccessActivity;
import com.cg.forms.FormPermissionViewer;
import com.cg.forms.FormRecordsCreators;
import com.cg.forms.FormViewer;
import com.cg.rounding.AssignPatientActivity;
import com.cg.rounding.AttendingRightsActivity;
import com.cg.rounding.OwnershipActivity;
import com.cg.rounding.PatientRoundingFragment;
import com.cg.rounding.RoundNewPatientActivity;
import com.cg.rounding.RoundingEditActivity;
import com.cg.rounding.RoundingGroupActivity;
import com.cg.rounding.TaskCreationActivity;
import com.cg.utilities.UtilityBuyer;
import com.cg.utilities.UtilityBuyerNew;
import com.cg.utilities.UtilitySeller;
import com.cg.utilities.UtilityServiceNeeder;
import com.cg.utilities.UtilityServiceProvider;
import com.group.GroupActivity;
import com.group.chat.ChatTemplateActivity;
import com.group.chat.GroupChatActivity;
import com.group.chat.GroupChatSettings;
import com.group.chat.ProfessionList;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.main.FeedbackFragment;
import com.main.FormsFragment;
import com.main.LoginPageFragment;
import com.main.MyAccountFragment;
import com.main.QuickActionFragment;
import com.main.Registration;
import com.main.SearchPeopleFragment;
import com.main.SettingsFragment;
import com.util.SingleInstance;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.lib.PatientDetailsBean;
import org.lib.model.BuddyInformationBean;
import org.lib.model.FileDetailsBean;
import org.lib.model.FindPeopleBean;
import org.lib.model.FormAttributeBean;
import org.lib.model.FormsBean;
import org.lib.model.Formsinfocontainer;
import org.lib.model.GroupBean;
import org.lib.model.GroupMemberBean;
import org.lib.model.OfflineConfigurationBean;
import org.lib.model.PatientCommentsBean;
import org.lib.model.PatientDescriptionBean;
import org.lib.model.PermissionBean;
import org.lib.model.RoleAccessBean;
import org.lib.model.ShareReminder;
import org.lib.model.ShareSendBean;
import org.lib.model.TaskDetailsBean;
import org.lib.model.UtilityResponse;
import org.lib.model.WebServiceBean;
import org.lib.xml.XmlParser;
import org.net.AndroidInsecureKeepAliveHttpsTransportSE;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

//import com.main.AvatarFragment;

public class WSRunner implements Runnable {

	public Servicebean mServicebean;

	public String mServer_ip;

	public int mPort;

	public String mWsdl_link;

	public String mNamespace;

	private WebServiceCallback mService_callback;

	private AndroidInsecureKeepAliveHttpsTransportSE androidHttpTransport = null;

	private SoapSerializationEnvelope mEnvelope = null;

	private SoapObject mRequest = null;

	private SoapPrimitive mSp = null;

	public String quotes = "\"";

	private XmlParser mParser = new XmlParser();

	private boolean mChk = false;

	private String parse = "";

	private WebServiceBean webServiceBean = null;

	private String TAG = "Web_Service";

	@Override
	public void run() {
		try {

			mServicebean = (Servicebean) WSNotifier.wsQueue.getMsg();
			if (mServicebean != null) {

				try {
					parse = mWsdl_link.substring(mWsdl_link.indexOf("://") + 3);

					parse = parse.substring(parse.indexOf(":") + 1);

					parse = parse.substring(parse.indexOf("/"),
							parse.indexOf("?"));
				} catch (Exception e) {
					e.printStackTrace();

				}
				androidHttpTransport = new AndroidInsecureKeepAliveHttpsTransportSE(
						mServer_ip, mPort, parse, 30000);

				mRequest = new SoapObject(mNamespace, mServicebean
						.getWsmethodname().trim());

				if (mServicebean.getProperty_map() != null) {
					for (Entry<String, String> set : mServicebean
							.getProperty_map().entrySet()) {
						mRequest.addProperty(set.getKey().trim(), set
								.getValue().trim());
					}
					HashMap<String, String> map = mServicebean
							.getProperty_map();
					map = null;
				}

				Log.d("webservice", "My Server Requset  :" + mRequest);

				mEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				mEnvelope.setOutputSoapObject(mRequest);

				androidHttpTransport.call(
						quotes + mNamespace + mServicebean.getWsmethodname()
								+ quotes, mEnvelope);
				mSp = (SoapPrimitive) mEnvelope.getResponse();

				Log.d("webservice", "My Server response  :" + mSp.toString());

				switch (mServicebean.getServiceMethods()) {
				case SUBSCRIBE:
					Log.i("register", "gfkjgsfkgwfvhj");
					webServiceBean = mParser.parseSubscribe(mSp.toString());
					mServicebean.setObj(webServiceBean);
					if (mServicebean.getCallBack() != null) {
						if (mService_callback != null)
							mService_callback
									.notifyWebServiceResponse(mServicebean);
					}

					break;

				case SIGNOUT:
					webServiceBean = mParser.parseSubscribe(mSp.toString());
					mServicebean.setObj(webServiceBean);
					Log.i("result", "---Signout-----");
					if (mService_callback != null) {
						Log.i("result", "---response-----");
						((AppMainActivity) SingleInstance.contextTable
								.get("MAIN"))
								.notifyWebServiceResponse(mServicebean);
						Log.i("result", "---response send-----");
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}
					break;

				case HEARTBEAT:
					mChk = mParser.getResult(mSp.toString());
					if (mChk) {
						ArrayList<BuddyInformationBean> buddyList = mParser
								.parseKeepAlive(mSp.toString());
						mServicebean.setObj(buddyList);

					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					if (mService_callback != null)
						mService_callback
								.notifyWebServiceResponse(mServicebean);

					break;

				case FINDPEOPLE:
					mChk = mParser.getResult(mSp.toString());
					if (mChk) {
						ArrayList<FindPeopleBean> peopleList = mParser
								.parseFindPeople(mSp.toString());
						mServicebean.setObj(peopleList);

					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					if (mService_callback != null)
						mService_callback
								.notifyWebServiceResponse(mServicebean);

					break;

                case SEARCHPEOPLEBYACCOUNT:
                    mChk = mParser.getResult(mSp.toString());
                    if (mChk) {
                        ArrayList<BuddyInformationBean> peopleList = mParser.parseSearchPeopleByAccount(mSp.toString());
                        mServicebean.setObj(peopleList);
                    } else {
                        webServiceBean = mParser.parseResultFromXml(mSp.toString());
                        mServicebean.setObj(webServiceBean);
                    }
                    if (mServicebean.getCallBack() != null) {
                        ((SearchPeopleFragment) mServicebean.getCallBack()).notifySearchPeople(mServicebean.getObj());
                    } else {
                        SingleInstance.printLog(TAG, "Login, Callback is NULL",null, null);
                    }
                    break;

				case ADDPEOPLE:
					mChk = mParser.getResult(mSp.toString());
					if (mChk) {
						ArrayList<BuddyInformationBean> peopleList = mParser
								.parseAddPeople(mSp.toString());
						mServicebean.setObj(peopleList);
						Log.d("thread", "Addpeople response 111111"
								+ mServicebean.getObj());

					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					// if (mService_callback != null)
					// mService_callback
					// .notifyWebServiceResponse(mServicebean);
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof FindPeople) {
							SingleInstance.mainContext
									.notifyBuddyRequestSent(mServicebean);
						}
					}

					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof UtilityBuyer) {
							SingleInstance.mainContext
									.notifyBuddyRequestSent(mServicebean);
						}
					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof UtilitySeller) {
							SingleInstance.mainContext
									.notifyBuddyRequestSent(mServicebean);
						}
					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof UtilityServiceNeeder) {
							SingleInstance.mainContext
									.notifyBuddyRequestSent(mServicebean);
						}
					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof UtilityServiceProvider) {
							SingleInstance.mainContext
									.notifyBuddyRequestSent(mServicebean);
						}
					}

					break;

				case DELETEPEOPLE:

					webServiceBean = mParser.parseResultFromXml(mSp.toString());
					mServicebean.setObj(webServiceBean);
					// if (mService_callback != null)
					// mService_callback
					// .notifyWebServiceResponse(mServicebean);
					if (mServicebean.getCallBack() != null) {
						((ContactsFragment) mServicebean.getCallBack())
								.notifyBuddyDeleted(((WebServiceBean) mServicebean
										.getObj()).getText());
					}

					break;

				case ACCEPT:

					mChk = mParser.getResult(mSp.toString());
					if (mChk) {
						ArrayList<BuddyInformationBean> buddyList = mParser
								.parseAcceptResponse(mSp.toString());
						mServicebean.setObj(buddyList);
					} else {
						webServiceBean = new WebServiceBean();
						mServicebean.setObj(null);
					}
					// if (mService_callback != null)
					// mService_callback
					// .notifyWebServiceResponse(mServicebean);
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof ContactsFragment) {
							ContactsFragment contactsFragment = (ContactsFragment) mServicebean
									.getCallBack();
							contactsFragment.notifyAcceptRejectRequest(
									mServicebean, true);
						}
					
//						if (mServicebean.getCallBack() instanceof FindPeople) {
//							FindPeople findPeople = (FindPeople) mServicebean
//									.getCallBack();
//							findPeople.notifyAcceptRejectRequest(mServicebean,
//									true);
//						}
					}

					break;

				case REJECT:

					mChk = mParser.getResult(mSp.toString());
					if (mChk) {
						webServiceBean = new WebServiceBean();
						webServiceBean.setResult("1");
						webServiceBean.setText((String) mServicebean.getObj());
						mServicebean.setObj(webServiceBean);
					} else {
						webServiceBean = new WebServiceBean();
						mServicebean.setObj(webServiceBean);
					}
					// if (mService_callback != null)
					// mService_callback
					// .notifyWebServiceResponse(mServicebean);
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof ContactsFragment) {
							ContactsFragment contactsFragment = (ContactsFragment) mServicebean
									.getCallBack();
							contactsFragment.notifyAcceptRejectRequest(
									mServicebean, false);
						}

				
//						if (mServicebean.getCallBack() instanceof FindPeople) {
//							FindPeople findPeople = (FindPeople) mServicebean
//									.getCallBack();
//							findPeople.notifyAcceptRejectRequest(mServicebean,
//									true);
//						}
					}
					break;

				case GETPROFILEFIELDVALUES:
					if (mParser.getResult(mSp.toString())) {
						ArrayList<Object> profileResponseList = mParser
								.parseGetStandaradfieldValues(mSp.toString());
						mServicebean.setObj(profileResponseList);

					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					// mService_callback.notifyWebServiceResponse(mServicebean);
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.notifyViewProfile(mServicebean);
						}
					}
					break;

				case GETPROFILETEMPLATE:
					// if (mParser.getResult(mSp.toString())) {
					// ArrayList<Object> getProfileTemplateList = mParser
					// .parseGetProfileTemplateResponse(mSp.toString());
					// mServicebean.setObj(getProfileTemplateList);
					//
					// } else {
					// webServiceBean = mParser.parseResultFromXml(mSp
					// .toString());
					// mServicebean.setObj(webServiceBean);
					// }
					//
					// mService_callback.notifyWebServiceResponse(mServicebean);
					break;

				case SETSTANDARDPROFILEFIELDVALUES:
					if (mParser.getResult(mSp.toString())) {
						ArrayList<Object> profileResponseList = mParser
								.parseSetProfileResponse(mSp.toString());
						mServicebean.setObj(profileResponseList);

					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					mService_callback.notifyWebServiceResponse(mServicebean);
					break;

				case DELETEPROFILE:

					if (mParser.getResult(mSp.toString())) {

						String[] result = mParser.parseDeleteProfile(mSp
								.toString());
						mServicebean.setObj(result);
					} else {

						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);

					}
					mService_callback.notifyWebServiceResponse(mServicebean);

					break;

				case SHAREBYPROFILE:
					if (mParser.getResult(mSp.toString())) {
						ArrayList<String> result = mParser
								.parseSharebyprofileSearchXml(mSp.toString());
						mServicebean.setObj(result);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					mService_callback.notifyWebServiceResponse(mServicebean);
					break;
				case SHARE_REMAINDER:
					mChk = mParser.getResult(mSp.toString());
					Log.d("XP WSD","file"+mChk);
					if (mChk) {
						Log.d("XP WSD", "file for xml");
						String[] file=mParser.parsedownloadxml(mSp.toString());
						SingleInstance.mainContext.notifyFiledetails(file);
					}
					if (mParser.getResult(mSp.toString())) {
						ShareSendBean sbean = mParser.getShareSendResult(mSp
								.toString());
						mServicebean.setObj(sbean);
					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof ShareReminder) {
							ShareReminder shareReminder = (ShareReminder) mServicebean
									.getCallBack();
							if (shareReminder.getParent_id() != null
									&& shareReminder.getParent_id().length() > 0) {}
						}
					}

//					mService_callback.notifyWebServiceResponse(mServicebean);
					break;
					case UPLOAD:
						mChk = mParser.getResult(mSp.toString());
						String upload_result = mParser.parseResultXml(mSp.toString());
						if(mChk) {
							Log.d("XP WSD", "file" + mChk);
							SingleInstance.mainContext.showToast("File upload successfully");
							if (mServicebean.getCallBack() != null) {
								if (mServicebean.getCallBack() instanceof sendershare) {
									Log.i("AAAA","wsrunner send share");
									((sendershare) mServicebean.getCallBack())
											.sendFile();
								} else if (mServicebean.getCallBack()instanceof FileInfoFragment) {
									Log.i("AAAA","wsrunner send file info fragment");
									((FileInfoFragment) mServicebean.getCallBack())
											.sendFile();
								}
							}
						}else
							SingleInstance.mainContext.showToast(upload_result);
						SingleInstance.fileDetailsBean=null;


						break;

					case SYNC:
						mChk = mParser.getResult(mSp.toString());
						ArrayList<ShareReminder> syncBean=mParser
								.parseSyncFileDetails(mSp.toString());
						mServicebean.setObj(syncBean);
						Log.d("SYNC", "--->SYNC");
						if (mServicebean.getCallBack() instanceof AppMainActivity) {
							Log.d("SYNC","--->SYNC if");
							((AppMainActivity) mServicebean.getCallBack())
									.loginResponse(mServicebean.getObj());
							SingleInstance.mainContext.syncFinished();
						}
						break;
					case SYNCCHAT:
						mChk = mParser.getResult(mSp.toString());
						ArrayList<GroupChatBean> chatBean=mParser
								.parseSyncChatDetails(mSp.toString());
						mServicebean.setObj(chatBean);
						Log.d("CHATSYNC", "--->SYNC CHAT");
						if (mServicebean.getCallBack() instanceof AppMainActivity) {
							Log.d("CHATSYNC","--->SYNC CHAT if");
							((AppMainActivity) mServicebean.getCallBack())
									.notifySyncChat(mServicebean.getObj());
						}
						break;
					case GETSETUTILITY:

					if (mParser.getResult(mSp.toString())) {
						UtilityResponse bean = mParser.parseUtilityresponse(mSp
								.toString());
						mServicebean.setObj(bean);
					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof UtilityBuyerNew) {
							((UtilityBuyerNew) mServicebean.getCallBack())
									.notifywebserviceReponse(mServicebean
											.getObj());
						}
					} else {
						mService_callback
								.notifyWebServiceResponse(mServicebean);
					}
					break;
				case SETANDGETUTILITYITEMS:

					if (mParser.getResult(mSp.toString())) {
						UtilityResponse bean = mParser
								.parseUtilityItemresponse(mSp.toString());
						mServicebean.setObj(bean);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}

					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof ContactsFragment) {
//							((ContactsFragment) mServicebean.getCallBack())
//									.notifywebserviceReponseForUtilityItemResponse(mServicebean
//											.getObj());
						}
						
					} else {
						mService_callback
								.notifyWebServiceResponse(mServicebean);
					}
					break;

				case BLOCKUNBLOCKBUDDY:

					if (mParser.getResult(mSp.toString())) {
						String[] blockUnblock = mParser
								.parseBlockUnblockresult(mSp.toString());
						mServicebean.setObj(blockUnblock);
					}
					mService_callback.notifyWebServiceResponse(mServicebean);
					break;

				case GETBLOCKBUDDYLIST:

					if (mParser.getResult(mSp.toString())) {
						String[] blockUnblock = mParser
								.parseBlockUnblockresult(mSp.toString());
						mServicebean.setObj(blockUnblock);
					}
					mService_callback.notifyWebServiceResponse(mServicebean);
					break;
				case SETUTILITYSERVICES:
					if (mParser.getResult(mSp.toString())) {
						Vector<UtilityResponse> responseList = mParser
								.parseSetUtilityServices(mSp.toString());
						mServicebean.setObj(responseList);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}

					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof UtilitySeller) {
							((UtilitySeller) mServicebean.getCallBack())
									.notifySaveAllUtilityItems(mServicebean
											.getObj());
						}
						if (mServicebean.getCallBack() instanceof UtilityBuyer) {
							((UtilityBuyer) mServicebean.getCallBack())
									.notifySaveAllUtilityItems(mServicebean
											.getObj());
						}
						if (mServicebean.getCallBack() instanceof UtilityServiceNeeder) {
							((UtilityServiceNeeder) mServicebean.getCallBack())
									.notifySaveAllUtilityItems(mServicebean
											.getObj());
						}
						if (mServicebean.getCallBack() instanceof UtilityServiceProvider) {
							((UtilityServiceProvider) mServicebean
									.getCallBack())
									.notifySaveAllUtilityItems(mServicebean
											.getObj());
						}
					}
					break;
				// case SYNCUTILITYITEMS:
				//
				// if (mParser.getResult(mSp.toString())) {
				// Syncutilitybean bean = mParser.parseGetUtilityItems(mSp
				// .toString());
				// mServicebean.setObj(bean);
				// }
				// mService_callback.notifyWebServiceResponse(mServicebean);
				// break;

				case FORGOTPASSWORD:
					webServiceBean = mParser
							.parseForgetPasswordResultFromXml(mSp.toString());
					mServicebean.setObj(webServiceBean);
					mService_callback.notifyWebServiceResponse(mServicebean);
					break;

				case SECRETANSWER:
					webServiceBean = mParser.parseUserSubscribe(mSp
							.toString());
					mServicebean.setObj(webServiceBean);
					mService_callback.notifyWebServiceResponse(mServicebean);

					break;

				case CREATEFORM:
					Log.d("log", "Came to createforms");

					if (mParser.getResult(mSp.toString())) {
						Log.d("zxz", "Call configuration if ");
						ArrayList<String[]> result = mParser
								.parseCreateFormResult(mSp.toString());
						mServicebean.setObj(result);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
						Log.d("zxz", "Call on login else");

					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof FormDescription) {
							((FormDescription) mServicebean.getCallBack())
									.notifyWebServiceResponse(mServicebean
											.getObj());
						} else if (mServicebean.getCallBack() instanceof QuickActionFragment) {
							Log.i("register", "---response-----");
							((QuickActionFragment) mServicebean.getCallBack())
									.notifyWebServiceResponse(mServicebean
											.getObj());
							Log.i("formdirect",
									"ACCESSFORM:Inside form AppsView");

						} else if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.notifyQuickAction(mServicebean.getObj());
						}

					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}

					break;

				case ACCESSFORM:

					if (mParser.getResult(mSp.toString())) {
						Log.d("zxz", "Call configuration if ");
						String[] result = mParser.parseAccessFormResult(mSp
								.toString());
						mServicebean.setObj(result);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
						Log.d("zxz", "Call on login else");

					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof AccessAndSync) {
							((AccessAndSync) mServicebean.getCallBack())
									.notifyWebServiceResponse(mServicebean
											.getObj());

							Log.i("formdirect",
									"ACCESSFORM:Inside form recordscreators");
						} else if (mServicebean.getCallBack() instanceof FormPermissionViewer) {
							((FormPermissionViewer) mServicebean.getCallBack())
									.notifyWebServiceResponseupdate(mServicebean
											.getObj());
							

						} else if (mServicebean.getCallBack() instanceof QuickActionFragment) {
							Log.i("register", "---response-----");
							((QuickActionFragment) mServicebean.getCallBack())
									.notifyWebServiceResponseAccess(mServicebean
											.getObj());
							Log.i("formdirect",
									"ACCESSFORM:Inside form AppsView");

						} else if (mServicebean.getCallBack() instanceof GroupChatActivity) {
							Log.i("register", "---response-----");
							String multipleAccess = "";
							if (mServicebean.getExtraDatas() instanceof String) {
								multipleAccess = (String) mServicebean
										.getExtraDatas();
							}
							((GroupChatActivity) mServicebean.getCallBack())
									.notifyWebserviceForAccessForm(
											mServicebean.getObj(),
											mServicebean.getOption(),
											multipleAccess);
							Log.i("formdirect",
									"ACCESSFORM:Inside form AppsView");

						}

					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}

					break;
				case ACCESSMUTIPLEFORM:

					if (mParser.getResult(mSp.toString())) {
						Log.d("zxz", "Call configuration if ");
						ArrayList<String[]> result = mParser
								.parseMultipleAccessForm(mSp.toString());
						mServicebean.setObj(result);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
						Log.d("zxz", "Call on login else");

					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof GroupChatActivity) {
							Log.i("register", "---response-----");
							String multipleAccess = "";
							if (mServicebean.getExtraDatas() instanceof String) {
								multipleAccess = (String) mServicebean
										.getExtraDatas();
							}
							((GroupChatActivity) mServicebean.getCallBack())
									.notifyWebserviceForAccessForm(
											mServicebean.getObj(),
											mServicebean.getOption(),
											multipleAccess);
							Log.i("formdirect",
									"ACCESSFORM:Inside form AppsView");

						}

					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}

					break;

				case DELETEACCESSFORM:
					Log.d("log", "Came to createforms");

					if (mParser.getResult(mSp.toString())) {
						Log.d("zxz", "Call configuration if ");
						String[] result = mParser
								.parseDeleteAccessFormResult(mSp.toString());
						mServicebean.setObj(result);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
						Log.d("zxz", "Call on login else");

					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof FormPermissionViewer) {
							((FormPermissionViewer) mServicebean.getCallBack())
									.notifyDeleteaccessWebServiceResponse(mServicebean
											.getObj());

							Log.i("formdirect",
									"DELETEACCESSFORM:Inside form recordscreators");
						}

					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}

					break;

				case GETFORMSETTINGS:
					Log.d("log", "Came to getsetting");

					if (mParser.getResult(mSp.toString())) {
						Log.d("zxz", "Call configuration if ");
						Vector<Object> formSettingsList = mParser
								.parsesettingAccessFormResult(mSp.toString());
						mServicebean.setObj(formSettingsList);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
						Log.d("zxz", "Call on login else");

					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof FormPermissionViewer) {
							((FormPermissionViewer) mServicebean.getCallBack())
									.notifyWebServiceResponse(mServicebean
											.getObj());

							Log.i("formdirect",
									"DELETEACCESSFORM:Inside form recordscreators");
						} else if (mServicebean.getCallBack() instanceof QuickActionFragment) {
							Log.i("register", "---response-----");
							((QuickActionFragment) mServicebean.getCallBack())
									.notifyWebServiceResponseUDP(mServicebean
											.getObj());
							Log.i("formdirect",
									"ACCESSFORM:Inside form AppsView");

						}
						if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.handleGetDataFormsSettings(mServicebean
											.getObj());

							Log.i("formdirect",
									"DELETEACCESSFORM:Inside form recordscreators");
						}

					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}

					break;
				case GETFORMSTEMPLATE:
					Log.d("zxz", "heartbeat request");

					if (mParser.getResult(mSp.toString())) {
						ArrayList<Object> buddyList = mParser
								.parseFormTemplate(mSp.toString());
						mServicebean.setObj(buddyList);

					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}

					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.handleFormsTemplate(mServicebean.getObj());

							Log.i("formdirect",
									"GETFORMSTEMPLATE:Inside form recordscreators");
						}

					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}

					// mService_callback.notifyWebServiceResponse(mServicebean);

					break;

				case GETFORMATTRIBUTE:

					if (mParser.getResult(mSp.toString())) {
						Log.d("zxz", "Call configuration if ");
						ArrayList<FormAttributeBean> result = mParser
								.parseGetAttribute(mSp.toString());
						mServicebean.setObj(result);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
						Log.d("zxz", "Call on login else");

					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof FormDescription) {
							((FormDescription) mServicebean.getCallBack())
									.notifyWebServiceGetAttributeResponse(
											mServicebean.getObj(),
											mServicebean.getProperty_map().get(
													"isEditFormField"));

							Log.i("formdirect",
									"DELETEACCESSFORM:Inside form recordscreators");
						} else if (mServicebean.getCallBack() instanceof FormsFragment) {
							((FormsFragment) mServicebean.getCallBack())
									.populateLists();

							Log.i("formdirect",
									"ACCESSFORM:Inside form AppsView");

						} else if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.handleGetDataAttributeForms(
											mServicebean.getObj(),
											mServicebean.getProperty_map().get(
													"isEditFormField"));

							Log.i("formdirect",
									"ACCESSFORM:Inside form AppsView");

						}

					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}

					break;

				case ADDFORMRECORDS:

					if (mParser.getResult(mSp.toString())) {
						Log.d("zxz", "Call configuration if ");
						String[] result = mParser.parseAddFormXml(mSp
								.toString());
						mServicebean.setObj(result);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
						Log.d("zxz", "Call on login else");

					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof FormRecordsCreators) {
							((FormRecordsCreators) mServicebean.getCallBack())
									.notifyWebServiceResponse(mServicebean
											.getObj());

							Log.i("formdirect",
									"ADDFORMRECORDS:Inside form recordscreators");
						} else if (mServicebean.getCallBack() instanceof QuickActionFragment) {
							Log.i("register", "---response-----");
							((QuickActionFragment) mServicebean.getCallBack())
									.notifyWebServiceResponse1(mServicebean
											.getObj());
							Log.i("formdirect",
									"ACCESSFORM:Inside form AppsView");

						} else if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.notifyQuickActiondata(mServicebean
											.getObj());
						} else if (SingleInstance.contextTable
								.containsKey("forms")) {
							FormsFragment quickActionFragment = FormsFragment
									.newInstance((Context) mServicebean
											.getCallBack());

							quickActionFragment.populateLists();
							Log.i("formdirect",
									"ADDFORMRECORDS:Inside form forms");

						} else if (mServicebean.getCallBack() instanceof AppsView) {
							((AppsView) mServicebean.getCallBack())
									.refreshIcon();
							Log.i("formdirect",
									"ADDFORMRECORDS:Inside form AppsView");

						}

					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}

					break;

				case GETFORMRECORDS:

					if (mParser.getResult(mSp.toString())) {
						Log.d("zxz", "Call configuration if ");
						Formsinfocontainer fbean = mParser
								.parseGetFormResult(mSp.toString());
						mServicebean.setObj(fbean);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
						Log.d("zxz", "Call on login else");
					}

					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof FormsFragment) {
							((FormsFragment) mServicebean.getCallBack())
									.notifyWebServiceResponse(mServicebean
											.getObj());
						} else if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.handleGetDataForms(mServicebean.getObj());
						}

					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}

					break;
				case UPDATEFORMRECORDS:

					if (mParser.getResult(mSp.toString())) {
						Log.d("zxz", "Call configuration if ");
						String[] result = mParser.parseEditFormXml(mSp
								.toString());
						mServicebean.setObj(result);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
						Log.d("zxz", "Call on login else");

					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof FormRecordsCreators) {
							((FormRecordsCreators) mServicebean.getCallBack())
									.notifyWebServiceResponse1(mServicebean
											.getObj());
						} else if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.handleDataForms(mServicebean.getObj());
						}

					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}

					break;

				case DELETEFORM:

					if (mParser.getResult(mSp.toString())) {
						Log.d("zxz", "Call configuration if ");
						FormsBean fbean = mParser.parseDeleteFormResult(mSp
								.toString());
						mServicebean.setObj(fbean);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
						Log.d("zxz", "Call on login else");
					}

					if (mServicebean.getCallBack() != null) {

						if (mServicebean.getCallBack() instanceof FormsFragment) {
							((FormsFragment) mServicebean.getCallBack())
									.notifyFormDeletionRespose(mServicebean
											.getObj());

						} else if (mServicebean.getCallBack() instanceof AppsView) {
							((AppsView) mServicebean.getCallBack())
									.refreshIcon();

						} else if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.handleDeleteForms(mServicebean.getObj());
						}

					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}

					break;

				case DELETEFORMRECORD:
					if (mParser.getResult(mSp.toString())) {
						Log.d("zxz", "Call configuration if ");
						FormsBean fbean = mParser
								.parseDeleteFormRecordResult(mSp.toString());
						mServicebean.setObj(fbean);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
						Log.d("zxz", "Call on login else");
					}

					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof FormViewer) {
							((FormViewer) mServicebean.getCallBack())
									.notifyWebServiceResponse(mServicebean
											.getObj());
						}

					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}

					break;
				case OFFLINECONFIGURATION:
					if (mParser.getResult(mSp.toString())) {
						OfflineConfigurationBean result1 = mParser
								.responseForCallConfigurationInfo(mSp
										.toString());
						mServicebean.setObj(result1);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					 mService_callback.notifyWebServiceResponse(mServicebean);
					if (mServicebean.getCallBack() != null) {
						Log.i("avatar_i", "OFFLINECONFIGURATION");
						if (mServicebean.getCallBack() instanceof AvatarActivity) {
							AvatarActivity avatarActivity = (AvatarActivity) mServicebean
									.getCallBack();
							avatarActivity.notifyWebresponse(
									mServicebean.getObj(),
									avatarActivity.viewPosition);
							Log.i("avatar_i", "OFFLINECONFIGURATION");
						}
					}
					break;

				case DELETEMYRESPONSEDETAILS:

					if (mParser.getResult(mSp.toString())) {

						String[] response = mParser.parseDeletemyResponse(mSp
								.toString());
						mServicebean.setObj(response);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					mService_callback.notifyWebServiceResponse(mServicebean);
					break;

				case GETCONFIGURATIONRESPONSEDETAILS:
					if (mParser.getResult(mSp.toString())) {

						ArrayList<Object> response_list = mParser
								.parseGetConfigurationResponDetails(mSp
										.toString());
						mServicebean.setObj(response_list);
					} else {

						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					mService_callback.notifyWebServiceResponse(mServicebean);
					break;

				// case GETMYCONFIGURATIONDETAILS:
				// if (mParser.getResult(mSp.toString())) {
				// ArrayList<Object> result1 = mParser
				// .parseGetMyConfigurationDetails(mSp.toString());
				//
				// mServicebean.setObj(result1);
				// } else {
				// webServiceBean = mParser.parseResultFromXml(mSp
				// .toString());
				// mServicebean.setObj(webServiceBean);
				//
				// }
				// mService_callback.notifyWebServiceResponse(mServicebean);
				// break;

				case OFFLINECALLRESPONSE:
					if (mParser.getResult(mSp.toString())) {
						ArrayList<Object> result1 = mParser
								.parseOfflineCallResponse(mSp.toString());
						mServicebean.setObj(result1);
					} else {

						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					mService_callback.notifyWebServiceResponse(mServicebean);
					break;

				case RESPONSEFORCALLCONFIGURATION:
					if (mParser.getResult(mSp.toString())) {
						String[] result1 = mParser
								.parseResponseForCallConfiguration(mSp
										.toString());

						mServicebean.setObj(result1);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					mService_callback.notifyWebServiceResponse(mServicebean);
					break;
				case CHANGEPASSWORD:
					webServiceBean = mParser.parseResultFromXml(mSp.toString());
					mServicebean.setObj(webServiceBean);

					// mService_callback.notifyWebServiceResponse(mServicebean);

					((ChangePassword) mServicebean.getCallBack())
							.notifyWebServiceResponse(mServicebean);

					break;
					case RESETPASSWORD:
						webServiceBean = mParser.parseResultFromXml(mSp.toString());
						mServicebean.setObj(webServiceBean);

						if (mServicebean.getCallBack() != null) {
							((ResetPassword) mServicebean.getCallBack())
									.notifyWebServiceResponse(mServicebean);
						}

						break;
					case RESETPIN:
						webServiceBean = mParser.parseResultFromXml(mSp.toString());
						mServicebean.setObj(webServiceBean);

						if (mServicebean.getCallBack() != null) {
							((PinAndTouchId) mServicebean.getCallBack())
									.notifyWebServiceResponse(mServicebean);
						}

						break;
					case UPDATESECRETANSWER:
						webServiceBean = mParser.parseResultFromXml(mSp.toString());
						mServicebean.setObj(webServiceBean);

						if (mServicebean.getCallBack() != null) {
							((SecurityQuestions) mServicebean.getCallBack())
									.notifyWebServiceResponse(mServicebean);
						}

						break;
					case GETSTATES:
						mChk = mParser.getResult(mSp.toString());
						if(mChk) {
							ArrayList<String[]> stateList = mParser.parseStates(mSp.toString());
							mServicebean.setObj(stateList);
						}else{
							webServiceBean = mParser.parseResultFromXml(mSp.toString());
							mServicebean.setObj(webServiceBean);
						}if (mServicebean.getCallBack()instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.notifyStatesWebServiceResponse(mServicebean.getObj());
						}

						break;
					case GETCITIES:
						Log.d("Cityvalue","x---->");
						mChk = mParser.getResult(mSp.toString());
						if(mChk){
							ArrayList<String>cityList = mParser.parsecities(mSp.toString());
							mServicebean.setObj(cityList);
						}else{
							webServiceBean = mParser.parseResultFromXml(mSp.toString());
							mServicebean.setObj(webServiceBean);
						}if (mServicebean.getCallBack()instanceof AppMainActivity){
						((AppMainActivity)mServicebean.getCallBack())
								.notifycityWebServiceResponse(mServicebean.getObj());
					}
						break;
					case GETHOSPITALDETAILS:
						mChk = mParser.getResult(mSp.toString());
						if(mChk) {
							ArrayList<String> hospitaldetailsList = mParser.parseHospitalDetails(mSp.toString());
							mServicebean.setObj(hospitaldetailsList);
						}else{
							webServiceBean = mParser.parseResultFromXml(mSp.toString());
							mServicebean.setObj(webServiceBean);
						}
						if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.notifyHospitalDetails(mServicebean.getObj());
						}

						break;
					case GETMEDICALSOCIETY:
						mChk = mParser.getResult(mSp.toString());
						if(mChk) {
							ArrayList<String[]> medicalList = mParser.parseMedicalSocieties(mSp.toString());
							mServicebean.setObj(medicalList);
						}else{
							webServiceBean = mParser.parseResultFromXml(mSp.toString());
							mServicebean.setObj(webServiceBean);
						}
						if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.notifyMedicalSocietiesWebServiceResponse(mServicebean.getObj());
						}

						break;
					case GETSPECIALTY:
						mChk = mParser.getResult(mSp.toString());
						if(mChk) {
							ArrayList<String[]> medicalList = mParser.parseSpecialities(mSp.toString());
							mServicebean.setObj(medicalList);
						}else{
							webServiceBean = mParser.parseResultFromXml(mSp.toString());
							mServicebean.setObj(webServiceBean);
						}
						if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.notifySpecialiesRresponse(mServicebean.getObj());
						}

						break;
					case MEDICALSCHOOLS:
						mChk = mParser.getResult(mSp.toString());
						if(mChk) {
							ArrayList<String> medicalSchools = mParser.parseMedicalSchools(mSp.toString());
							mServicebean.setObj(medicalSchools);
						}else{
							webServiceBean = mParser.parseResultFromXml(mSp.toString());
							mServicebean.setObj(webServiceBean);
						}
						if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.notifyMedicalSchools(mServicebean.getObj());
						}

						break;
					case RESETACCOUNT:
						webServiceBean = mParser.parseResultFromXml(mSp.toString());
						mServicebean.setObj(webServiceBean);

						if (mServicebean.getCallBack() instanceof MyAccountFragment) {
								Log.i("AAAA","WS RUNNER RESET ACCOUNT");
								((MyAccountFragment) mServicebean.getCallBack())
										.notifyDeleteMyaccount(mServicebean);
						}

						break;
					case GETMYPIN:
						if (mParser.getResult(mSp.toString())) {
							String pin = mParser.parseGetMypinXml(mSp
									.toString());
							AppMainActivity.pinNo=pin;
						} else {
							webServiceBean = mParser.parseResultFromXml(mSp.toString());
							mServicebean.setObj(webServiceBean);
						}
						break;
					case GETMYSECRETQUESTION:
						if (mParser.getResult(mSp.toString())) {
							Log.i("AAAA","notifyMySecretQuestion if ");
							String[] question = mParser.parseGetMySecretQuestionXml(mSp
									.toString());
							mServicebean.setObj(question);
						}
						if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.notifyMySecretQuestion(mServicebean.getObj());
						}else if (mServicebean.getCallBack() instanceof SettingsFragment) {
							((SettingsFragment) mServicebean.getCallBack())
									.notifyMySecretQuestion(mServicebean.getObj());
						}
						break;

				case DELETEALLSHARES:
					webServiceBean = mParser.parseResultFromXml(mSp.toString());
					mServicebean.setObj(webServiceBean);

					mService_callback.notifyWebServiceResponse(mServicebean);
					break;

				case SETPERMISSION:
					if (mParser.getResult(mSp.toString())) {
						String[] permission = mParser.parseSetPermissionXML(mSp
								.toString());
						mServicebean.setObj(permission);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
						Log.d("zxz", "Call on login else");
					}

					mService_callback.notifyWebServiceResponse(mServicebean);
					break;

				case GETALLPERMISSION:
					if (mParser.getResult(mSp.toString())) {
						ArrayList<PermissionBean> permissionList = mParser
								.parseGetallPermissionResult(mSp.toString());
						mServicebean.setObj(permissionList);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
						Log.d("zxz", "Call on login else");
					}

					mService_callback.notifyWebServiceResponse(mServicebean);
					break;
				case SAVEMYLOCATION:
					if (mParser.getResult(mSp.toString())) {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);

					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					mService_callback.notifyWebServiceResponse(mServicebean);
					break;
				/*
				 * Group Management
				 */
				case CREATEGROUP:
					if (mParser.getResult(mSp.toString())) {
						GroupBean bean = mParser.parseCreateGroup(mSp
								.toString());
						mServicebean.setObj(bean);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					// mService_callback.notifyWebServiceResponse(mServicebean);
					if (mServicebean.getCallBack() != null) {
						Log.i("register", "---response-----");
						((GroupActivity) mServicebean.getCallBack())
								.notifyCreateGroup(mServicebean.getObj());
						Log.i("register", "---response send-----");
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}
					break;
					case CREATEORMODIFYROUNDING:
						if (mParser.getResult(mSp.toString())) {
							GroupBean bean = mParser.parseCreateGroup(mSp.toString());
							mServicebean.setObj(bean);
						} else {
							webServiceBean = mParser.parseResultFromXml(mSp.toString());
							mServicebean.setObj(webServiceBean);
						}
						if (mServicebean.getCallBack() != null) {
							Log.i("register", "---response-----");
							((RoundingGroupActivity) mServicebean.getCallBack())
									.notifyCreateRoundingGroup(mServicebean.getObj());
						} else {
							SingleInstance.printLog(TAG, "Login, Callback is NULL",
									null, null);
						}
						break;
					case ACCEPTREJECTGROUP:
						String text[]=mParser.parseAcceptRejectGroup(mSp.toString());
						mServicebean.setObj(text);
						if (mServicebean.getCallBack() != null) {
							Log.i("AAAA","ACCEPTREJECTGROUP ");
							((AppMainActivity) mServicebean.getCallBack())
									.notifyAcceptRejectGroup(mServicebean.getObj());
						}
						break;
					case GETGROUPDETAILS:
						GroupBean gbean=mParser.parseGetGroupDetails(mSp.toString());
						mServicebean.setObj(gbean);
						if (mServicebean.getCallBack() != null) {
							Log.i("AAAA","GETGROUPDETAILS ");
							((AppMainActivity) mServicebean.getCallBack())
									.notifygroupDetails(mServicebean.getObj());
						}
						break;
				case DELETEGROUP:
					if (mParser.getResult(mSp.toString())) {
						GroupBean bean = mParser.parseDeleteGroup(mSp
								.toString());
						mServicebean.setObj(bean);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					ContactsFragment.getInstance(
							SingleInstance.mainContext)
							.notifyDeleteGroup(mServicebean.getObj());
					mService_callback.notifyWebServiceResponse(mServicebean);
					break;
				case GETGROUPANDMEMBERS:
					if (mParser.getResult(mSp.toString())) {
						Vector<GroupBean> bean = mParser
								.parseGetGroupAndMembers(mSp.toString());
						mServicebean.setObj(bean);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					if (mServicebean.getCallBack() != null) {
						Log.i("register", "---response-----");
						((AppMainActivity) mServicebean.getCallBack())
								.notifyMyGroupList(mServicebean.getObj());
						Log.i("register", "---response send-----");
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}
					// mService_callback.notifyWebServiceResponse(mServicebean);
					break;
					case GETROUNDINGGROUPS:
						if (mParser.getResult(mSp.toString())) {
							Vector<GroupBean> bean = mParser
									.parseGetRoundingGroupAndMembers(mSp.toString());
							mServicebean.setObj(bean);
						} else {
							webServiceBean = mParser.parseResultFromXml(mSp
									.toString());
							mServicebean.setObj(webServiceBean);
						}
						if (mServicebean.getCallBack() != null) {
							Log.i("register", "---response-----");
							((AppMainActivity) mServicebean.getCallBack())
									.notifyRoundingGroupList(mServicebean.getObj());
							Log.i("register", "---response send-----");
						} else {
							SingleInstance.printLog(TAG, "Login, Callback is NULL",
									null, null);
						}
						break;
				
				case GROUPMEMBERSDELETE:
					if (mParser.getResult(mSp.toString())) {
						GroupBean bean = mParser.parseGroupMembersDelete(mSp
								.toString());
						mServicebean.setObj(bean);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					mService_callback.notifyWebServiceResponse(mServicebean);
					break;
					case SETMEMBERRIGHTS:
						String aresult = mParser.parseResultXml(mSp.toString());
						mServicebean.setObj(aresult);

						if (mServicebean.getCallBack() instanceof RoundingEditActivity) {
							((RoundingEditActivity) mServicebean.getCallBack())
									.notifyOwnership(mServicebean.getObj());
						} else if (mServicebean.getCallBack()instanceof OwnershipActivity) {
							((OwnershipActivity) mServicebean.getCallBack())
									.notifyOwnership(mServicebean.getObj());
						}else {
							SingleInstance.printLog(TAG, "Login, Callback is NULL",
									null, null);
						}
						break;
					
				case GETPROFESSIONS:
					if (mParser.getResult(mSp.toString())) {
						String[] pList = mParser.parseGetProfessions(mSp
								.toString());
							
						mServicebean.setObj(pList);
					
					} 
					if (mServicebean.getCallBack() != null) {
						((AppMainActivity) mServicebean.getCallBack())
								.notifyGetProfessions(mServicebean.getObj());
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}
					break;
					case NEWVERIFICATION:
						if (mParser.getResult(mSp.toString())) {
							String[] temp = mParser.parseNewVerification(mSp
									.toString());
							mServicebean.setObj(temp);
						}else {
							webServiceBean = mParser.parseResultFromXml(mSp
									.toString());
							mServicebean.setObj(webServiceBean);
						}

							if (mServicebean.getCallBack() != null) {
								((NewUser) mServicebean.getCallBack())
										.notifyWebserviceResponse(mServicebean.getObj());
							} else {
								SingleInstance.printLog(TAG, "Login, Callback is NULL",
										null, null);
							}
						break;
					case SETPATIENTRECORD:
						if (mParser.getResult(mSp.toString())) {
							String[] temp = mParser.parseSetPatientRecord(mSp
									.toString());
							mServicebean.setObj(temp);
						} else {
							webServiceBean = mParser.parseResultFromXml(mSp
									.toString());
							mServicebean.setObj(webServiceBean);
						}
						if (mServicebean.getCallBack() instanceof RoundNewPatientActivity) {
							((RoundNewPatientActivity) mServicebean.getCallBack())
									.notifySetPatientRecord(mServicebean.getObj());
						} else if(mServicebean.getCallBack() instanceof AssignPatientActivity){
							((AssignPatientActivity) mServicebean.getCallBack())
									.notifySetPatientRecord(mServicebean.getObj());
					}else {
							SingleInstance.printLog(TAG, "Login, Callback is NULL",
									null, null);
						}

						break;
					case CREATETASK:
						if (mParser.getResult(mSp.toString())) {
							String[] temp = mParser.parseCreateTask(mSp
									.toString());
							mServicebean.setObj(temp);
						} else {
							webServiceBean = mParser.parseResultFromXml(mSp
									.toString());
							mServicebean.setObj(webServiceBean);
						}

							if (mServicebean.getCallBack() != null) {
								((TaskCreationActivity) mServicebean.getCallBack())
										.notifytaskcreated(mServicebean.getObj());
							}
						break;
					case SETOREDITROLEACCESS:
						if (mParser.getResult(mSp.toString())) {
							String[] temp = mParser.parseSetOrEditRole(mSp
									.toString());

							mServicebean.setObj(temp);
						}else {
							webServiceBean = mParser.parseResultFromXml(mSp
									.toString());
							mServicebean.setObj(webServiceBean);
						}
						if (mServicebean.getCallBack() != null) {
							Log.i("sss", "notifyRightsToUI of rights wsrunner ");
							((AttendingRightsActivity) mServicebean.getCallBack())
									.notifyRoleAccess(mServicebean.getObj());
						}
						break;
					case PATIENTDISCHARGE:
						if (mParser.getResult(mSp.toString())) {
							String[] temp = mParser.parseDischargePatient(mSp
									.toString());
							mServicebean.setObj(temp);
						}else {
							webServiceBean = mParser.parseResultFromXml(mSp
									.toString());
							mServicebean.setObj(webServiceBean);
						}
						if (mServicebean.getCallBack() != null) {
							Log.i("patientdetails","WS R PATIENTDISCHARGE");
							((PatientRoundingFragment) mServicebean.getCallBack())
									.notifyPatientDischarge(mServicebean.getObj());
						}
						break;
					case DELETETASK:
						if (mParser.getResult(mSp.toString())) {
							String[] temp = mParser.parseDeleteTask(mSp
									.toString());
							mServicebean.setObj(temp);
						}else {
							webServiceBean = mParser.parseResultFromXml(mSp
									.toString());
							mServicebean.setObj(webServiceBean);
						}
						if (mServicebean.getCallBack() != null) {
							if (mServicebean.getCallBack()instanceof PatientRoundingFragment) {
								((PatientRoundingFragment) mServicebean.getCallBack())
										.notifyDeletetask(mServicebean.getObj());
							}else if(mServicebean.getCallBack()instanceof GroupChatActivity)
							((GroupChatActivity) mServicebean.getCallBack())
									.notifyDeletetask(mServicebean.getObj());
						}
						break;
					case GETMEMBERRIGHTS:
						if (mParser.getResult(mSp.toString())) {
							Vector<GroupMemberBean> memberBeans = mParser.parseGetMemberRights(mSp
									.toString());
							mServicebean.setObj(memberBeans);
						}else {
							webServiceBean = mParser.parseResultFromXml(mSp
									.toString());
							mServicebean.setObj(webServiceBean);
						}
						if (mServicebean.getCallBack() != null) {
							((AppMainActivity) mServicebean.getCallBack())
									.notifyGetMemberRights(mServicebean.getObj());
						}
						break;
					case SETPATIENTCOMMENTS:
						if (mParser.getResult(mSp.toString())) {
							PatientCommentsBean cBean= mParser.parseSetPatientComments(mSp
									.toString());

							mServicebean.setObj(cBean);
						}
						Log.i("patientdetails", "notifyRightsToUI of rights wsrunner ");
						if (mServicebean.getCallBack() != null) {
							Log.i("patientdetails", "notifyRightsToUI of rights wsrunner ");
							((PatientRoundingFragment) mServicebean.getCallBack())
									.notifySetPatientComments(mServicebean.getObj());
						}
						break;
					case GETROLEACCESS:
						if (mParser.getResult(mSp.toString())) {
							Vector<Object> Bean= mParser.parseGetRoleAccess(mSp
									.toString());

							mServicebean.setObj(Bean);
						}
						Log.i("sss", "notifyroleAccess wsrunners");

						if (mServicebean.getCallBack() != null) {
							((AppMainActivity) mServicebean.getCallBack())
									.notifyroleAccess(mServicebean.getObj());
						} else {
							SingleInstance.printLog(TAG, "Login, Callback is NULL",
									null, null);
						}
						break;
					case SETPATIENTDESCRIPTION:
						if (mParser.getResult(mSp.toString())) {
							String[] pDesc= mParser.parseSetPatientDescription(mSp
									.toString());

							mServicebean.setObj(pDesc);
						}
						if (mServicebean.getCallBack() != null) {
						((PatientRoundingFragment) mServicebean.getCallBack())
								.notifySetPatientDescription(mServicebean.getObj());
					}
						break;
					case GETPATIENTRECORDS:
						if (mParser.getResult(mSp.toString())) {
							Vector<PatientDetailsBean> pBean = mParser.parseGetPatientDetails(mSp
									.toString());
							mServicebean.setObj(pBean);
						}else {
							webServiceBean = mParser.parseResultFromXml(mSp
									.toString());
							mServicebean.setObj(webServiceBean);
						}
						if (mServicebean.getCallBack() != null) {
						Log.i("patientdetails","GETPATIENTRECORDS");
						((AppMainActivity) mServicebean.getCallBack())
								.notifyGetPatientRecords(mServicebean.getObj());
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}
						break;
					case GETPATIENTCOMMENTS:
						if (mParser.getResult(mSp.toString())) {
							Vector<PatientCommentsBean> pBean = mParser.parseGetPatientComments(mSp
									.toString());
							mServicebean.setObj(pBean);
						}if (mServicebean.getCallBack() != null) {
						((AppMainActivity) mServicebean.getCallBack())
								.notifyGetPatientComments(mServicebean.getObj());
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}
						break;
					case GETTASKINFO:
						if (mParser.getResult(mSp.toString())) {
							Vector<TaskDetailsBean> pBean = mParser.parseGetTaskDetails(mSp
									.toString());

							mServicebean.setObj(pBean);
						}if (mServicebean.getCallBack() != null) {
						Log.i("patientdetails","GETTASKINFO");
								((AppMainActivity) mServicebean.getCallBack())
								.notifyGetTaskRecords(mServicebean.getObj());
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}
						break;
					case GETPATIENTDESCRIPTION:
						if (mParser.getResult(mSp.toString())) {
							Vector<PatientDescriptionBean> pBean = mParser.parseGetPatientDescription(mSp
									.toString());

							mServicebean.setObj(pBean);
						}if (mServicebean.getCallBack() != null) {
						Log.i("patientdetails","GETTASKINFO");
						((AppMainActivity) mServicebean.getCallBack())
								.notifyGetPatientDescription(mServicebean.getObj());
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}
						break;
					case GETFILEDETAILS:
						if (mParser.getResult(mSp.toString())) {
							Vector<FileDetailsBean>  pBean = mParser.parseGetFileDetails(mSp
									.toString());
							mServicebean.setObj(pBean);
						}
						if (mServicebean.getCallBack() != null) {
						((AppMainActivity) mServicebean.getCallBack())
								.notifyGetFileDetails(mServicebean.getObj());
					     } else {
						 SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					     }
						break;
					case UPDATECHATTEMPLATE:
						if (mParser.getResult(mSp.toString())) {
							String[] param = mParser.parseUpdateChatTemplate(mSp
									.toString());
							mServicebean.setObj(param);
						} else {
							webServiceBean = mParser.parseResultFromXml(mSp
									.toString());
							mServicebean.setObj(webServiceBean);
						}
						if (mServicebean.getCallBack() != null) {
							((ChatTemplateActivity) mServicebean.getCallBack())
									.notifyUpdateChatTemplate(mServicebean.getObj());
						} else {
							SingleInstance.printLog(TAG, "Login, Callback is NULL",
									null, null);
						}
						break;
					case SUBSCRIBENEW:
						String sresult = mParser.parseResultXml(mSp.toString());
						mServicebean.setObj(sresult);
							if (mServicebean.getCallBack() != null) {
								((Registration) mServicebean.getCallBack())
										.notifyRegistrationResponse(mServicebean.getObj());
								Log.i("AAAA", "WSRUNNER");
							} else {
								SingleInstance.printLog(TAG, "Login, Callback is NULL",
										null, null);
							}


						break;
					case FORGOTPASSWORDNEW:
						String result = mParser.parseResultXml(mSp.toString());
						mServicebean.setObj(result);
                        Log.i("AAAA", "FORGOT PASSWORD ____");
                        if (mServicebean.getCallBack() != null) {
                            Log.i("AAAA", "FORGOT PASSWORD ____");
                            ((forgotPassword) mServicebean.getCallBack())
                                    .getResponseFromServer(mServicebean.getObj());
                        } else {
                            SingleInstance.printLog(TAG, "Login, Callback is NULL",
                                    null, null);
                        }
						break;

                    case GETSECURITYQUESTIONS:
                        ArrayList<String[]> questionsList= mParser.parseSecurtiyQuestions(mSp.toString());
                        mServicebean.setObj(questionsList);
                        if (mServicebean.getCallBack() != null) {
                            ((AppMainActivity) mServicebean.getCallBack()).
									notifyGetSecurityQuestions(mServicebean.getObj());
                        } else {
                            SingleInstance.printLog(TAG, "GETSECURITYQUESTIONS, Callback is NULL",
                                    null, null);
                        }
                        break;

					case SETMYACCOUNT:
						webServiceBean = mParser.parseResultFromXml(mSp.toString());
						mServicebean.setObj(webServiceBean);
						Log.i("AAAA", "SETMYACCOUNT");
						if (mServicebean.getCallBack() instanceof MyAccountActivity) {
							((MyAccountActivity) mServicebean.getCallBack())
									.getResponseFromServer(mServicebean);
						} else {
							SingleInstance.printLog(TAG, "Login, Callback is NULL",
									null, null);
						}

						break;
				case GETPARTICIPATEGROUPS:
					if (mParser.getResult(mSp.toString())) {
						Vector<GroupBean> bean = mParser
								.parseGetParticipateGroups(mSp.toString());
						mServicebean.setObj(bean);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					if (mServicebean.getCallBack() != null) {
						Log.i("register", "---response-----");
						((AppMainActivity) mServicebean.getCallBack())
								.notifyBuddyGroupList(mServicebean.getObj());
						Log.i("register", "---response send-----");
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}
					// mService_callback.notifyWebServiceResponse(mServicebean);
					break;
				case LEAVEGROUP:
					if (mParser.getResult(mSp.toString())) {
						GroupBean bean = mParser
								.parseLeaveGroup(mSp.toString());
						mServicebean.setObj(bean);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					mService_callback.notifyWebServiceResponse(mServicebean);
					break;
				case MODIFYGROUP:
					if (mParser.getResult(mSp.toString())) {
						GroupBean bean = mParser.parseModifyGroup(mSp
								.toString());
						mServicebean.setObj(bean);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					if (mServicebean.getCallBack() != null) {
						Log.i("register", "---response-----");
						((GroupActivity) mServicebean.getCallBack())
								.notifyGroupModify(mServicebean.getObj());
						Log.i("register", "---response send-----");
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}
					break;
				case SETGROUPCHATSETTING:
					if (mParser.getResult(mSp.toString())) {
						String[] gresult = mParser.parseSetGroupChatSettings(mSp
								.toString());
						mServicebean.setObj(gresult);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					if (mServicebean.getCallBack() != null) {
						((GroupChatSettings) mServicebean.getCallBack())
								.notifyWebserviceResponse(mServicebean.getObj());
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}
					break;
				case SETPROFESSIONPERMISSION:	
					String presult = mParser.parseResultXml(mSp.toString());
					mServicebean.setObj(presult);
				
					if (mServicebean.getCallBack() != null) {
						((ProfessionList) mServicebean.getCallBack())
								.notifyWebserviceResponse(mServicebean.getObj());
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}
					break;
				case GETGROUPCHATSETTING:
					if (mParser.getResult(mSp.toString())) {
						Vector<GroupChatPermissionBean> gcpList = mParser
								.parseGetGroupChatSettings(mSp.toString());
						mServicebean.setObj(gcpList);
					} else {
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					if (mServicebean.getCallBack() != null) {
						((AppMainActivity) mServicebean.getCallBack())
								.notifyGetGroupChatSettings(mServicebean
										.getObj());
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}
					break;
					case GETMYACCOUNT:
						if (mParser.getResult(mSp.toString())) {
							ProfileBean gcpList = mParser
									.parseAllMyAccount(mSp.toString());
							mServicebean.setObj(gcpList);
						} else {
							webServiceBean = mParser.parseResultFromXml(mSp
									.toString());
							mServicebean.setObj(webServiceBean);
						}
						break;
					case GETALLPROFILE:
							Log.i("AAAA","WS RUNNER  ");
							Vector<ProfileBean> gcpList = mParser
									.parseGetAllProfile(mSp.toString());
							mServicebean.setObj(gcpList);
//						}
						if (mServicebean.getCallBack() instanceof AppMainActivity) {
							Log.i("AAAA","WS RUNNER getCallBack ");
							((AppMainActivity) mServicebean.getCallBack())
									.notifyGetAllProfile(mServicebean
											.getObj());
						}
						else if (mServicebean.getCallBack() instanceof ContactsFragment) {
							Log.i("AAAA","WS RUNNER getCallBack ");
							((ContactsFragment) mServicebean.getCallBack())
									.notifyGetAllProfile(mServicebean
											.getObj());
						}else {
							SingleInstance.printLog(TAG, "Login, Callback is NULL",
									null, null);
						}
						break;
					case DOWNLOAD:
						Log.d("XP WSD","File Download initiated...");

						mChk = mParser.getResult(mSp.toString());
						Log.d("XP WSD","file"+mChk);
						if (mChk) {
							Log.d("XP WSD", "file for xml");
							String[] file=mParser.parsedownloadxml(mSp.toString());
							SingleInstance.mainContext.notifyFiledetails(file);
						}
						break;
				case SETORDELETEFORMFIELDSETTINGS:
					webServiceBean = mParser.parseResultFromXml(mSp.toString());
					mServicebean.setObj(webServiceBean);
					if (mServicebean.getCallBack() instanceof FormFieldAccessActivity) {
						((FormFieldAccessActivity) mServicebean.getCallBack())
								.notifyFormFieldSettings(mServicebean.getObj());
					}
					break;
				
				
				
				
				
				case USERSIGNIN:
					mChk = mParser.getResult(mSp.toString());
					if (mChk) {
						ArrayList<BuddyInformationBean> buddyList = mParser
								.parseUserSignIn(mSp.toString());
						mServicebean.setObj(buddyList);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}
					if (mSp.toString().contains("<update type=")) {
						Log.d("zxz", "update tag available");
						mServicebean.setIsUpdateavailable(true);
						mServicebean.setUpdateObject(mParser.ParseUpdateTag(mSp
								.toString()));
					}

					if (mServicebean.getCallBack() != null) {
						Log.i("register", "---response-----");
						Log.i("login123",
								"Call Back " + mServicebean.getCallBack());
						if (mServicebean.getCallBack() instanceof LoginPageFragment) {
							((LoginPageFragment) mServicebean.getCallBack())
									.notifyLoginResponse(mServicebean.getObj());
						}
						Log.i("register", "---response send-----");
					} else {
						SingleInstance.printLog(TAG, "Login, Callback is NULL",
								null, null);
					}

					// if (mService_callback != null)
					// mService_callback
					// .notifyWebServiceResponse(mServicebean);

					break;

					case SETFEEDBACK:
						Log.i("feedback","reponse-->"+mSp.toString());
						mChk = mParser.getResult(mSp.toString());
						if(mChk){
							if(FeedbackFragment.feedbackFragment!=null){
								Log.i("feedback","positive response");
								FeedbackFragment.feedbackFragment.showwindow();
							}
						}
						break;
			
				case EDITFORM:
					mChk = mParser.getResult(mSp.toString());
					if (mChk) {
						EditForm editForm = mParser.parseEditForm(mSp
								.toString());
						mServicebean.setObj(editForm);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}

					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof EditFormFields) {
							EditFormFields eFields = (EditFormFields) mServicebean
									.getCallBack();
							eFields.notifyWebserviceResponse(mServicebean);

						}
					}
				case EDITFORMFORNEWFIELDADD:
					mChk = mParser.getResult(mSp.toString());
					if (mChk) {
						EditForm editForm = mParser
								.parseEditFormForAddNewField(mSp.toString());
						mServicebean.setObj(editForm);
					} else {
						Log.d("zxz", "Call on login else");
						webServiceBean = mParser.parseResultFromXml(mSp
								.toString());
						mServicebean.setObj(webServiceBean);
					}

					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof AddNewForm) {
							AddNewForm eFields = (AddNewForm) mServicebean
									.getCallBack();
							eFields.notifyWebserviceResponse(mServicebean);

						}
					}

					case GETCHATTEMPLATE:
						Log.i("chattemplate","getchatTemplate response--->"+mSp.toString());
//						mChk = mParser.getResult(mSp.toString());
//						if (mChk) {
							Log.i("chattemplate","parse value getchattemplate");
							ArrayList<Object> values=mParser.parseGetchattemplate(mSp.toString());
							if(SingleInstance.mainContext!=null){
								Log.i("chattemplate","mServicebean.getCallBack() != null");
									SingleInstance.mainContext.notifyChatTemplate(values);
							}
//						}else{
//							if(SingleInstance.mainContext!=null){
//								SingleInstance.mainContext.notifyChatTemplate(null);
//							}
//						}
						break;
				default:
					Log.d("webservice", "Came to default case");
					break;

				}

			}

		} catch (Exception e) {
			Log.d("zxz", "Exception response " + e.getMessage());
			String errorMsg = "Unable To Connect Server.Please try again Later";
			String exception = e.getMessage();
			e.printStackTrace();

			if(mServicebean.getServiceMethods()==EnumWebServiceMethods.HEARTBEAT) {
				try {
					SingleInstance.mainContext.setLogWriterForCall("SEND HeartBeat 2: "+e.getMessage());
					SingleInstance.printLog(null, "SEND HeartBeat 2: "+e.getMessage(), "INFO", null);
				} catch (Exception ex1) {
				}
			}

			if (exception != null) {
				if (exception
						.contains("expected: END_TAG {http://schemas.xmlsoap.org/soap/envelope/}")) {
					errorMsg = "Server Unavailable";
				} else if (exception.contains("The operation timed out")) {
					errorMsg = exception;
				} else if (exception.contains("Connection refused")) {
					errorMsg = "Connection refused";
				} else if (exception.contains("No route to host")) {
					errorMsg = "No route to host";
				} else if (exception.contains("Malformed ipv4 address")) {
					errorMsg = "Malformed ip address";
				} else if (exception.contains("Network is unreachable")) {
					errorMsg = "Network unreachable";
				}
			}
			if (mService_callback != null || mServicebean.getCallBack() != null) {
				switch (mServicebean.getServiceMethods()) {
				case SUBSCRIBE:
					((Registration) WebServiceReferences.contextTable
							.get("registration"))
							.notifyRegistrationResponse(errorMsg);

					break;
				case SIGNIN:
					((LoginPageFragment) mServicebean.getCallBack())
							.notifyLoginResponse(errorMsg);
					break;
				case SIGNOUT:
					mServicebean.setObj(errorMsg);
					((AppMainActivity) SingleInstance.mainContext)
							.notifyWebServiceResponse(errorMsg);
					break;
				case FINDPEOPLE:
					mServicebean.setObj(errorMsg);
					((AppMainActivity) SingleInstance.mainContext)
							.notifyWebServiceResponse(errorMsg);
					break;
				case CREATEFORM:
					mServicebean.setObj(errorMsg);
					if (mServicebean.getCallBack() instanceof FormDescription) {
						((FormDescription) mServicebean.getCallBack())
								.notifyWebServiceResponse(mServicebean.getObj());
					} else if (mServicebean.getCallBack() instanceof QuickActionFragment) {
						Log.i("register", "---response-----");
						((QuickActionFragment) mServicebean.getCallBack())
								.notifyWebServiceResponse(mServicebean.getObj());
						Log.i("formdirect", "ACCESSFORM:Inside form AppsView");

					}
					break;
				case SETGROUPCHATSETTING:
					mServicebean.setObj(errorMsg);
					if (mServicebean.getCallBack() instanceof GroupChatSettings) {
						((GroupChatSettings) mServicebean.getCallBack())
								.notifyWebserviceResponse(errorMsg);
					}
					break;
				case GETGROUPCHATSETTING:
					mServicebean.setObj(errorMsg);
					if (mServicebean.getCallBack() instanceof AppMainActivity) {
						((AppMainActivity) mServicebean.getCallBack())
								.ShowError(errorMsg);
					}
					break;
				case CREATEGROUP:
					mServicebean.setObj(errorMsg);
					if (mServicebean.getCallBack() instanceof GroupChatSettings) {
						((GroupActivity) mServicebean.getCallBack())
								.notifyCreateGroup(errorMsg);
					}
					break;
				case MODIFYGROUP:
					mServicebean.setObj(errorMsg);
					if (mServicebean.getCallBack() instanceof GroupChatSettings) {
						((GroupActivity) mServicebean.getCallBack())
								.notifyGroupModify(errorMsg);
					}
					break;
				case SETORDELETEFORMFIELDSETTINGS:
					mServicebean.setObj(errorMsg);
					if (mServicebean.getCallBack() instanceof FormFieldAccessActivity) {
						((FormFieldAccessActivity) mServicebean.getCallBack())
								.notifyFormFieldSettings(errorMsg);
					}
					break;
			
				case USERSIGNIN:
					((LoginPageFragment) mServicebean.getCallBack())
							.notifyLoginResponse(errorMsg);
					break;
				

				
				case SETANDGETUTILITYITEMS:
					mServicebean.setObj(errorMsg);
					if (mServicebean.getCallBack() instanceof ContactsFragment) {
						((ContactsFragment) mServicebean.getCallBack())
								.notifywebserviceReponseForUtilityItemResponse(mServicebean
										.getObj());
					}
				case SETUTILITYSERVICES:
					mServicebean.setObj(errorMsg);
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof UtilitySeller) {
							((UtilitySeller) mServicebean.getCallBack())
									.notifySaveAllUtilityItems(mServicebean
											.getObj());
						}
						if (mServicebean.getCallBack() instanceof UtilityBuyer) {
							((UtilityBuyer) mServicebean.getCallBack())
									.notifySaveAllUtilityItems(mServicebean
											.getObj());
						}
						if (mServicebean.getCallBack() instanceof UtilityServiceNeeder) {
							((UtilityServiceNeeder) mServicebean.getCallBack())
									.notifySaveAllUtilityItems(mServicebean
											.getObj());
						}
						if (mServicebean.getCallBack() instanceof UtilityServiceProvider) {
							((UtilityServiceProvider) mServicebean
									.getCallBack())
									.notifySaveAllUtilityItems(mServicebean
											.getObj());
						}
					}
					break;
				case ACCEPT:
					mServicebean.setObj(errorMsg);
					if (mServicebean.getCallBack() instanceof ContactsFragment) {
						ContactsFragment contactsFragment = (ContactsFragment) mServicebean
								.getCallBack();
						contactsFragment.notifyAcceptRejectRequest(
								mServicebean, true);
					}
//					if (mServicebean.getCallBack() instanceof FindPeople) {
//						FindPeople findPeople = (FindPeople) mServicebean
//								.getCallBack();
//						findPeople
//								.notifyAcceptRejectRequest(mServicebean, true);
//					}
					break;
				case REJECT:
					mServicebean.setObj(errorMsg);
					if (mServicebean.getCallBack() instanceof ContactsFragment) {
						ContactsFragment contactsFragment = (ContactsFragment) mServicebean
								.getCallBack();
						contactsFragment.notifyAcceptRejectRequest(
								mServicebean, false);
					}
//					if (mServicebean.getCallBack() instanceof FindPeople) {
//						FindPeople findPeople = (FindPeople) mServicebean
//								.getCallBack();
//						findPeople
//								.notifyAcceptRejectRequest(mServicebean, true);
//					}
					break;
				case EDITFORM:
					mServicebean.setObj(errorMsg);
					if (mServicebean.getCallBack() instanceof EditFormFields) {
						EditFormFields editFormFields = (EditFormFields) mServicebean
								.getCallBack();
						editFormFields.notifyWebserviceResponse(mServicebean);
					}
					break;
				case EDITFORMFORNEWFIELDADD:
					mServicebean.setObj(errorMsg);
					if (mServicebean.getCallBack() instanceof AddNewForm) {
						AddNewForm editFormFields = (AddNewForm) mServicebean
								.getCallBack();
						editFormFields.notifyWebserviceResponse(mServicebean);
					}
					break;
					case UPLOAD:
						mServicebean.setObj(errorMsg);
//						if(SingleInstance.fileDetailsBean!=null){
//							SingleInstance.pendingFiles.add(SingleInstance.fileDetailsBean);
//						}
//						if(SingleInstance.pendingFiles!=null){
//							for(FileDetailsBean fBean:SingleInstance.pendingFiles){
//								String[] param=new String[5];
//								param[0]=CallDispatcher.LoginUser;
//								param[1]=CallDispatcher.Password;
//								if(fBean.getServicetype().equalsIgnoreCase("upload")){
//									param[2]=fBean.getFiletype();
//									param[3]=fBean.getFilename();
//									param[4]=fBean.getFilecontent();
//									WebServiceReferences.webServiceClient.FileUpload(param, SingleInstance.mainContext);
//								}else {
//									param[2]=fBean.getFilename();
//									WebServiceReferences.webServiceClient.FileDownload(param);
//								}
//								SingleInstance.pendingFiles.remove(fBean);
//							}
//						}
						break;
				case GETFORMRECORDS:
					mServicebean.setObj(errorMsg);
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof FormsFragment) {
							((FormsFragment) mServicebean.getCallBack())
									.notifyWebServiceResponse(mServicebean
											.getObj());
						} else if (mServicebean.getCallBack() instanceof AppMainActivity) {
							((AppMainActivity) mServicebean.getCallBack())
									.handleGetDataForms(mServicebean.getObj());
						}
					}
					break;
				case FORGOTPASSWORD:
					mServicebean.setObj(errorMsg);
//					if (WebServiceReferences.contextTable
//							.containsKey("getnewpassword")) {
//						getnewpassword gPassword = ((getnewpassword) WebServiceReferences.contextTable
//								.get("getnewpassword"));
//						gPassword.notifyWebserviceResponse(mServicebean
//								.getObj());
//					}

				case ADDPEOPLE:
					mServicebean.setObj("The invite was not sent");
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof FindPeople) {
							SingleInstance.mainContext
									.notifyBuddyRequestSent(mServicebean);
						}
					}

					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof UtilityBuyer) {
							SingleInstance.mainContext
									.notifyBuddyRequestSent(mServicebean);
						}
					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof UtilitySeller) {
							SingleInstance.mainContext
									.notifyBuddyRequestSent(mServicebean);
						}
					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof UtilityServiceNeeder) {
							SingleInstance.mainContext
									.notifyBuddyRequestSent(mServicebean);
						}
					}
					if (mServicebean.getCallBack() != null) {
						if (mServicebean.getCallBack() instanceof UtilityServiceProvider) {
							SingleInstance.mainContext
									.notifyBuddyRequestSent(mServicebean);
						}
					}
					break;
					case GETCHATTEMPLATE:
						mServicebean.setObj(errorMsg);
						((AppMainActivity) SingleInstance.mainContext)
								.notifyWebServiceResponse(errorMsg);
						break;
			
				default:
					break;
				}

			}

			e.printStackTrace();
		} finally {
			parse = null;
			androidHttpTransport = null;
			mRequest = null;
			mEnvelope = null;
			mSp = null;
			mParser = null;
			quotes = null;
		}
	}

	public Servicebean getBean() {
		return this.mServicebean;
	}

	public void setserviceInfo(String ipAddress, int port, String wsdlLink,
			String namespace, WebServiceCallback call_back) {

		this.mServer_ip = ipAddress;
		this.mPort = port;
		this.mWsdl_link = wsdlLink;
		this.mNamespace = namespace;
		this.mService_callback = call_back;

	}

}
