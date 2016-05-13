package com.cg.ftpprocessor;

import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.lib.model.FieldTemplateBean;
import org.lib.model.KeepAliveBean;
import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.ShareReminder;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.bean.GroupChatBean;
import com.cg.DB.DBAccess;
import com.cg.avatar.AnsweringMachineActivity;
import com.cg.avatar.AvatarActivity;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.CompleteListView;
import com.cg.forms.FormDescription;
import com.cg.forms.FormPermissionViewer;
import com.cg.forms.FormRecordsCreators;
import com.cg.forms.FormViewer;
import com.cg.quickaction.ContactLogicbean;
import com.cg.quickaction.QuickActionSettingcalls;
import com.cg.quickaction.QuickActionTitlecalls;
import com.cg.settings.MenuPage;
import com.chat.ChatActivity;
import com.group.chat.GroupChatActivity;
import com.main.AppMainActivity;
import com.main.FormsFragment;
import com.util.SingleInstance;

public class FTPNotifier implements FtpListener {

	private Threadpoolexecutor taskmanager;

	public static FTPQueue ftpQueue = new FTPQueue();

	private CallDispatcher calldisp = null;

	private Context context = null;

	public FTPNotifier() {
		taskmanager = new Threadpoolexecutor(1, 1, 5000, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(100));
		taskmanager.prestartCoreThread();

		if (taskmanager != null) {
			taskmanager
					.setRejectedExecutionHandler(new RejectedExecutionHandler() {

						@Override
						public void rejectedExecution(Runnable task,
								ThreadPoolExecutor executor) {
							// TODO Auto-generated method stub
							TaskRunner taskRunner = (TaskRunner) task;
							if (taskRunner.getBean() != null)
								ftpQueue.addMsg(taskRunner.getBean());
						}
					});
		}

		context = ((AppMainActivity) SingleInstance.contextTable.get("MAIN"));
		calldisp = CallDispatcher.getCallDispatcher(context);

	}

	public void shutDowntaskmanager() {
		if (taskmanager != null)
			taskmanager.shutdown();
		taskmanager = null;
	}

	public void addTasktoExecutor(FTPBean bean) {
		ftpQueue.addMsg(bean);
		if (taskmanager != null) {
			TaskRunner runnable_task = new TaskRunner();
			runnable_task.setCallback(this);
			taskmanager.execute(runnable_task);
		}
	}

	@Override
	public void FTPUploaded(FTPBean bean) {

		Log.d("chat123", "uploaded");
		try {
			// TODO Auto-generated method stub
			if (bean.getRequest_from() != null) {
				if (bean.getRequest_from().equals("call_settings")) {
					if (WebServiceReferences.contextTable
							.containsKey("avatarset")) {
						calldisp.showToast(
								SipNotificationListener.getCurrentContext(),
								"File uploaded");
						// WebServiceReferences.webServiceClient
						// .offlineconfiguration(CallDispatcher.LoginUser,
						// bean.getDefalut_lis(),
						// bean.getBuddy_list());

						((AppMainActivity) SingleInstance.contextTable
								.get("MAIN")).notifyUploadStatus(
								bean.getFilename(), bean.getReq_object(), true);
					}
				} else if (bean.getRequest_from().equals("answering machine")) {
					if (WebServiceReferences.contextTable
							.containsKey("answeringmachine"))
						((AnsweringMachineActivity) WebServiceReferences.contextTable
								.get("answeringmachine")).notifyUploadStatus(
								(Integer) bean.getReq_object(),
								bean.getFilename(), true);
				} else if (bean.getRequest_from().equals("pending clone")) {
					if (WebServiceReferences.contextTable
							.containsKey("avatarset")) {
						// (AvatarFragment.newInstance(context))
						// .notifyUploadStatus(
						// (OfflineRequestConfigBean) bean
						// .getReq_object(), bean
						// .getFilename(), true);
						(AvatarActivity.newInstance(context))
								.notifyUploadStatus(
										(OfflineRequestConfigBean) bean
												.getReq_object(), bean
												.getFilename(), true);

						// ((AppMainActivity) SingleInstance.contextTable
						// .get("MAIN")).notifyUploadStatus(
						// bean.getFilename(), bean.getReq_object(), true);
					}
				} else if (bean.getRequest_from().equalsIgnoreCase(
						"profile uploading")) {
					((AppMainActivity) SingleInstance.contextTable.get("MAIN"))
							.notifyUploadStatus(bean.getFilename(),
									bean.getReq_object(), true);
				}
				if (bean.getRequest_from().equalsIgnoreCase("filessharing")) {
					for (String buddyname : bean.getBuddiesList()) {
						calldisp.sendReminder(bean.getFilename(), buddyname,
								bean.getDatas().get(0), bean.getDatas().get(1),
								bean.getDatas().get(2), bean.getDatas().get(3),
								bean.getDatas().get(4), bean.getFile_path(),
								null, true, bean.isStream_enabled(),
								bean.getComment(), bean.getVanish_mode(),
								bean.getVanish_value(),bean.getcBean());
					}
					if (bean.getBuddiesList().size() == 1) {
						if (!SingleInstance.orderToastShow) {
							calldisp.showToast(
									SipNotificationListener.getCurrentContext(),
									bean.getDatas().get(0)
											+ " sent successfully");

						}
						}
				} else if (bean.getRequest_from().equalsIgnoreCase("MPP")
						|| bean.getRequest_from().equalsIgnoreCase("MAP")
						|| bean.getRequest_from().equalsIgnoreCase("MVP")
						|| bean.getRequest_from().equalsIgnoreCase("MHP")) {

					if (bean.getReq_object() != null) {

						ChatActivity chatActivity = (ChatActivity) SingleInstance.contextTable
								.get("chatactivity");
						if (chatActivity != null) {
							chatActivity.uploadCompleted(bean);
						}

						// InstantMessageScreen imsc = (InstantMessageScreen)
						// WebServiceReferences.instantMessageScreen
						// .get(bean.getComment());
						// if (imsc != null) {
						// if (bean.getRequest_from().equals("MPP")) {
						// imsc.notifyImageUploaded(true, bean);
						// } else if (bean.getRequest_from().equals("MVP")) {
						// imsc.notifyVideoUploaded(true, bean);
						// } else if (bean.getRequest_from().equals("MAP")) {
						// imsc.notifyAudioUploaded(true, bean);
						// } else if (bean.getRequest_from().equals("MHP")) {
						// Log.i("log", "mhp for handsketch");
						// imsc.notifyFPImageUploaded(true, bean);
						// }
						// }
					}

				}

				else if (bean.getRequest_from().equals("forms")) {
					if (WebServiceReferences.contextTable
							.containsKey("frmreccreator")) {
						FormRecordsCreators frmRecCreator = (FormRecordsCreators) WebServiceReferences.contextTable
								.get("frmreccreator");
						frmRecCreator.isUpload = true;
						frmRecCreator.UploadingCompleted();

					}
					if (WebServiceReferences.contextTable
							.containsKey("formdesc")) {

						if (bean.getFile_path().startsWith("FormIcon_")) {

							FormDescription fDesc = (FormDescription) WebServiceReferences.contextTable
									.get("formdesc");
							fDesc.isUpload = true;
							fDesc.UploadingCompleted();
							fDesc.notifyUploadIcon();

						}

					}

				} else if (bean.getRequest_from().equalsIgnoreCase(
						"quickaction")) {
					if (bean.getReq_object() != null) {
						ContactLogicbean bLogic = (ContactLogicbean) bean
								.getReq_object();
						if (WebServiceReferences.contextTable
								.containsKey("QuickActionTitlecalls")) {
							QuickActionTitlecalls QTitleCalls = (QuickActionTitlecalls) WebServiceReferences.contextTable
									.get("QuickActionTitlecalls");
							Log.i("QAA", "filename :: " + bean.getFilename());
							Log.i("QAA", "runNow :: " + bLogic.isRunNow());
							Log.i("QAA", "save :: " + bLogic.isIssave());
							if (!bLogic.isNew() && bLogic.isEdit()
									&& bLogic.isIssave()) {
								QTitleCalls.saveAndExceuteQuickAction(
										bean.getFilename(), bLogic.isRunNow());
							}

						}
						if (WebServiceReferences.contextTable
								.containsKey("QuickActionSettingcalls")) {
							QuickActionSettingcalls QSettingCalls = (QuickActionSettingcalls) WebServiceReferences.contextTable
									.get("QuickActionSettingcalls");
							Log.i("QAA", "filename :: " + bean.getFilename());
							Log.i("QAA", "runNow :: " + bLogic.isRunNow());
							Log.i("QAA", "save :: " + bLogic.isIssave());
							QSettingCalls.saveQA(bLogic.isNew(),
									bLogic.isIssave(), bean.getFilename(),
									bLogic);

						}
					}
				} else if (bean.getRequest_from().equals("menupage")) {
					// if (WebServiceReferences.contextTable
					// .containsKey("menupage"))
					// ((MenuPage) WebServiceReferences.contextTable
					// .get("menupage")).notifyUpload(bean
					// .getFilename());
					FieldTemplateBean fieldTemplateBean = new FieldTemplateBean();
					fieldTemplateBean
							.setFieldId(String.valueOf(calldisp
									.getdbHeler(
											SipNotificationListener
													.getCurrentContext())
									.getProfilePicFieldId("Picture")));
					fieldTemplateBean.setFiledvalue(bean.getFilename());
					Vector<FieldTemplateBean> fields_list1 = new Vector<FieldTemplateBean>();
					fields_list1.add(fieldTemplateBean);
					String[] params = new String[2];
					params[0] = CallDispatcher.LoginUser;
					params[1] = "5";

					WebServiceReferences.webServiceClient
							.setStandardProfileFieldValues(params, fields_list1);
				} else if (bean.getRequest_from().equals("100")) {
					if (bean.getReq_object() != null) {

						GroupChatActivity groupChatActivity = (GroupChatActivity) SingleInstance.contextTable
								.get("groupchat");
						if (groupChatActivity != null) {
							groupChatActivity.uploadCompleted(bean, true);
						}
					}
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void FTPUploaingFailed(FTPBean bean, String reason) {
		// TODO Auto-generated method stub
		if (bean.getRequest_from() != null) {
			if (bean.getRequest_from().equals("call_settings")) {
				if (WebServiceReferences.contextTable.containsKey("avatarset")) {
					calldisp.showToast(
							SipNotificationListener.getCurrentContext(),
							"File upload failed");
					if (bean.getMode().equalsIgnoreCase("edit"))
						calldisp.showToast(
								SipNotificationListener.getCurrentContext(),
								"Sorry, avatar was not updated");
					else
						calldisp.showToast(
								SipNotificationListener.getCurrentContext(),
								"Sorry, avatar was not added");

					calldisp.cancelDialog();

				}
			} else if (bean.getRequest_from().equals("pending clone")) {
				if (WebServiceReferences.contextTable.containsKey("avatarset")) {
					calldisp.showToast(
							SipNotificationListener.getCurrentContext(),
							"File upload failed");
					calldisp.showToast(
							SipNotificationListener.getCurrentContext(),
							"Sorry, please try again to respond");
					calldisp.cancelDialog();
				}

			} else if (bean.getRequest_from().equals("answering machine")) {
				if (WebServiceReferences.contextTable
						.containsKey("answeringmachine")) {
					calldisp.showToast(
							SipNotificationListener.getCurrentContext(),
							"File upload failed");
					calldisp.showToast(
							SipNotificationListener.getCurrentContext(),
							"Sorry, please try again to respond");
					calldisp.cancelDialog();
				}
			}

			else if (bean.getRequest_from().equalsIgnoreCase(
					"profile uploading")) {

				((AppMainActivity) SingleInstance.contextTable.get("MAIN"))
						.notifyUploadStatus(bean.getFile_path(),
								bean.getReq_object(), false);
			}

			if (bean.getRequest_from().equalsIgnoreCase("MPP")
					|| bean.getRequest_from().equalsIgnoreCase("MAP")
					|| bean.getRequest_from().equalsIgnoreCase("MVP")
					|| bean.getRequest_from().equalsIgnoreCase("MHP")) {

				if (bean.getComment() != null) {
					// InstantMessageScreen imsc = (InstantMessageScreen)
					// WebServiceReferences.instantMessageScreen
					// .get(bean.getComment());
					// if (imsc != null) {
					// if (bean.getRequest_from().equals("MPP")) {
					// imsc.notifyImageUploaded(false, bean);
					// } else if (bean.getRequest_from().equals("MVP")) {
					// imsc.notifyVideoUploaded(false, bean);
					// } else if (bean.getRequest_from().equals("MAP")) {
					// imsc.notifyAudioUploaded(false, bean);
					// } else if (bean.getRequest_from().equals("MHP")) {
					// imsc.notifyFPImageUploaded(false, bean);
					// }
					// }
				}

			}

			if (bean.getRequest_from().equals("forms")) {
				if (WebServiceReferences.contextTable
						.containsKey("frmreccreator")) {
					FormRecordsCreators frmRecCreator = (FormRecordsCreators) WebServiceReferences.contextTable
							.get("frmreccreator");
					frmRecCreator.isUpload = false;
					frmRecCreator.notifyFileUploadError();

				}
				if (WebServiceReferences.contextTable.containsKey("formdesc")) {
					FormDescription fDesc = (FormDescription) WebServiceReferences.contextTable
							.get("formdesc");
					fDesc.isUpload = true;
					fDesc.notifyFileUploadError();
				}

			}

			if (bean.getRequest_from().equals("menupage")) {
				if (WebServiceReferences.contextTable.containsKey("menupage"))
					((MenuPage) WebServiceReferences.contextTable
							.get("menupage")).notifyUploadFailed();
			}
			// else if (bean.getRequest_from().equals("100")) {
			// if (bean.getReq_object() != null) {
			//
			// GroupChatActivity groupChatActivity = (GroupChatActivity)
			// SingleInstance.contextTable
			// .get("groupchat");
			// if (groupChatActivity != null) {
			// groupChatActivity.uploadCompleted(bean, false);
			// }
			// }
			// }

		}
	}

	@Override
	public void Downloaded(FTPBean bean) {
		// TODO Auto-generated method stub
		Log.i("AAAA", "Downloaded in ftpnotifier :"+bean.getRequest_from());
		if (bean.getRequest_from() != null) {
			if (bean.getRequest_from().equals("answering machine")) {
				if (WebServiceReferences.contextTable
						.containsKey("answeringmachine")) {
					Log.i("avatar123", "inside avatar download 3");
					((AnsweringMachineActivity) WebServiceReferences.contextTable
							.get("answeringmachine")).notifyDownloadStatus(
							(String) bean.getReq_object(), bean.getFilename(),
							true);
				} else if (WebServiceReferences.contextTable
						.containsKey("avatarset")) {
					// ((CloneActivity) WebServiceReferences.contextTable
					// .get("clone")).notifyDownloadStatus(
					// (String) bean.getReq_object(), bean.getFilename(),
					// true, bean.getRequest_from());
					Log.i("avatar098", "avatarset" + "Downloaded");

					// AvatarFragment avatarFragment = AvatarFragment
					// .newInstance(context);
					AvatarActivity avatarActivity = AvatarActivity
							.newInstance(context);
					avatarActivity.notifyDownloadStatus(
							(String) bean.getReq_object(), bean.getFilename(),
							true, bean.getRequest_from());
				} else {
					ContentValues cv = new ContentValues();
					cv.put("status", 2);
					DBAccess.getdbHeler(context)
							.updateOfflineCallPendingClones(cv,
									"id=" + (String) bean.getReq_object());
					cv = null;
				}
			} else if (bean.getRequest_from().equals("offline response")) {
				ContentValues cv = new ContentValues();
				cv.put("status", 2);
				DBAccess.getdbHeler(SipNotificationListener.getCurrentContext())
						.updateOfflineCallResponseDetails(cv,
								"id=" + (String) bean.getReq_object());
				cv = null;

				// if (WebServiceReferences.contextTable.containsKey("clone"))
				// AvatarFragment.newInstance(context).notifyDownloadStatus(
				// (String) bean.getReq_object(), bean.getFilename(),
				// true, bean.getRequest_from());
				if (WebServiceReferences.contextTable.containsKey("avatarset")) {
					AvatarActivity.newInstance(context).notifyDownloadStatus(
							(String) bean.getReq_object(), bean.getFilename(),
							true, bean.getRequest_from());
				}
			} else if (bean.getRequest_from().equals("offline settings")) {
				Log.d("FTP", "Came to offline settings");
				ContentValues cv = new ContentValues();
				cv.put("status", 2);
				DBAccess.getdbHeler(SipNotificationListener.getCurrentContext())
						.updateOfflineCallSettingsDetails(cv,
								"Id=" + (String) bean.getReq_object());
				Log.d("FTP",
						"contains clone activity"
								+ WebServiceReferences.contextTable
										.containsKey("avatarset"));
				// if (WebServiceReferences.contextTable.containsKey("clone"))
				// AvatarFragment.newInstance(context).notifyDownloadStatus(
				// (String) bean.getReq_object(), bean.getFilename(),
				// true, bean.getRequest_from());
				if (WebServiceReferences.contextTable.containsKey("avatarset"))
					AvatarActivity.newInstance(context).notifyDownloadStatus(
							(String) bean.getReq_object(), bean.getFilename(),
							true, bean.getRequest_from());

			} else if (bean.getRequest_from().equals("profile field")) {
				if (bean.getReq_object() != null) {
					String[] info = ((String) bean.getReq_object()).split(",");
					if (info[2] != null && info[2].equals("true")) {
						if (WebServiceReferences.callDispatch
								.containsKey("calldisp"))
							((CallDispatcher) WebServiceReferences.callDispatch
									.get("calldisp"))
									.profilePictureDownloaded();
					}
				}
				if (WebServiceReferences.callDispatch.containsKey("calldisp"))
					((CallDispatcher) WebServiceReferences.callDispatch
							.get("calldisp")).notifyFileDownloaded(
							(String) bean.getReq_object(),
							bean.getRequest_from(), true, bean.getFilename());
			}

			else if (bean.getRequest_from().equals("files")) {
				if (bean.getProgress() == 100) {

					if (bean.getReq_object() != null) {

						if (bean.getReq_object() instanceof ShareReminder) {
							final ShareReminder sharer = (ShareReminder) bean
									.getReq_object();

							KeepAliveBean aliveBean = calldisp
									.getKeepAliveBean(sharer.getId(),
											"accepted");
							aliveBean.setKey("0");
							WebServiceReferences.webServiceClient
									.heartBeat(aliveBean);
							Log.d("thread", "Accept Send completed !!!!");

						}

						// ((CompleteListView) WebServiceReferences.contextTable
						// .get("MAIN"))
						// .notifyFileDownloaded("true", bean);
						((AppMainActivity) SingleInstance.contextTable
								.get("MAIN"))
								.notifyFileDownloaded("true", bean);
					}
				}

			} else if (bean.getRequest_from().equalsIgnoreCase("forms")) {

				if (bean.getFile_path().contains("FormIcon_")) {

					FormsFragment.newInstance(context).populateLists();
				}
				if (bean.getFile_path().startsWith("Instruction_")) {

					if (WebServiceReferences.contextTable
							.containsKey("frmreccreator")) {
						FormRecordsCreators frmRecCreator = (FormRecordsCreators) WebServiceReferences.contextTable
								.get("frmreccreator");
						frmRecCreator.isUpload = true;
						frmRecCreator.downloadinCompleted();
					}

				}
				if (WebServiceReferences.contextTable.containsKey("frmviewer")) {
					FormViewer frmviewer = (FormViewer) WebServiceReferences.contextTable
							.get("frmviewer");
					frmviewer.refreshList();

				}
				if (WebServiceReferences.contextTable
						.containsKey("formpermissionviewer")) {
					FormPermissionViewer frmviewer = (FormPermissionViewer) WebServiceReferences.contextTable
							.get("formpermissionviewer");
					frmviewer.refreshList();

				}
			}
			if (bean.getRequest_from().equalsIgnoreCase("MPP")
					|| bean.getRequest_from().equalsIgnoreCase("MAP")
					|| bean.getRequest_from().equalsIgnoreCase("MVP")
					|| bean.getRequest_from().equalsIgnoreCase("MHP"))
				calldisp.notifyDownloadtoIMScreen(bean, true);

			if (bean.getRequest_from().equals("100")) {
				GroupChatBean groupChatBean = (GroupChatBean) bean
						.getReq_object();
				AppMainActivity appMain = ((AppMainActivity) SingleInstance.contextTable
						.get("MAIN"));
				if (appMain != null) {
					appMain.processGroupChatChanges(groupChatBean);
				}

			}
			if (bean.getRequest_from().equalsIgnoreCase("viewmenu")) {
				
			}

		}
	}

	@Override
	public void DownloadFailure(FTPBean bean, String reason) {
		// TODO Auto-generated method stub
		if (bean.getRequest_from().equals("profile field")) {
			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				((CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp")).notifyFileDownloaded(
						(String) bean.getReq_object(), bean.getRequest_from(),
						false, bean.getFilename());
		} else if (bean.getRequest_from().equals("files")) {
			if (bean.getReq_object() instanceof ShareReminder) {
				final ShareReminder sharer = (ShareReminder) bean
						.getReq_object();

				KeepAliveBean aliveBean = calldisp.getKeepAliveBean(
						sharer.getId(), "rejected");
				aliveBean.setKey("0");
				WebServiceReferences.webServiceClient.heartBeat(aliveBean);
				Log.d("thread", "Accept Send completed !!!!");

			}
			if (WebServiceReferences.contextTable.containsKey("MAIN")) {
				if (bean.getReq_object() != null) {
					((CompleteListView) WebServiceReferences.contextTable
							.get("MAIN")).notifyFileDownloaded("false", bean);
				}
			}
		}
		if (bean.getRequest_from().equalsIgnoreCase("MPP")
				|| bean.getRequest_from().equalsIgnoreCase("MAP")
				|| bean.getRequest_from().equalsIgnoreCase("MVP")
				|| bean.getRequest_from().equalsIgnoreCase("MHP"))
			calldisp.notifyDownloadtoIMScreen(bean, true);

		// if (bean.getRequest_from().equals("100")) {
		//
		// GroupChatBean groupChatBean = (GroupChatBean) bean.getReq_object();
		// groupChatBean.setMediaName(null);
		// CompleteListView comp = (CompleteListView)
		// WebServiceReferences.contextTable
		// .get("MAIN");
		// if (comp != null) {
		// comp.processGroupChatChanges(groupChatBean);
		// }
		//
		// }
	}

}
