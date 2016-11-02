package com.group.chat;

import android.util.Log;

import com.bean.GroupChatBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.hostedconf.AppReference;
import com.main.ContactsFragment;
import com.util.Queue;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChatHistoryWriter extends Thread {
	private boolean isRunning;
	private Queue queue;

	public GroupChatHistoryWriter(Queue queue) {
		this.queue = queue;
	}

	public GroupChatHistoryWriter() {
		queue = new Queue();
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	@Override
	public void run() {

		while (isRunning) {
			try {
				GroupChatBean groupChatBean = (GroupChatBean) queue.getObject();
				if (groupChatBean != null) {

					if (groupChatBean.getType().equals("100")) {
						groupChatBean.setUsername(CallDispatcher.LoginUser);

						if (groupChatBean.getFrom().equals(
								CallDispatcher.LoginUser)
								&& groupChatBean.getCategory()
										.equalsIgnoreCase("I")) {
							groupChatBean.setGroupId(groupChatBean.getTo());

						} else if (groupChatBean.getCategory()
								.equalsIgnoreCase("I")) {
							groupChatBean.setGroupId(groupChatBean.getFrom());
						}

						if (groupChatBean.getMediaName() != null
								&& groupChatBean.getMediaName().length() > 0) {
                            String[] path=groupChatBean.getMediaName().split(",");
                            if(path.length>0){
                                String fpath=null;
                                for(int i=0;i<path.length;i++)
                                {
                                    if(fpath==null)
                                    {
                                        fpath = CallDispatcher
                                                .containsLocalPath(path[i]);
                                    }else{
                                        fpath =fpath+","+CallDispatcher
                                                .containsLocalPath(path[i]);
                                    }

                                }
                                groupChatBean
                                        .setMediaName(fpath);
                            }else {
							String media = CallDispatcher
									.containsLocalPath(groupChatBean
											.getMediaName());
							groupChatBean.setMediaName(media);
                            }

						}

                        if(groupChatBean.getSubCategory()!=null&&groupChatBean.getSubCategory().equalsIgnoreCase("grb_r"))
                        {
                            int row = DBAccess.getdbHeler(
                                    SipNotificationListener.getCurrentContext())
                                    . updateChatReply(groupChatBean);
                            if(row>0) {
                                groupChatBean.setReply("grb_r");
                            }
                        }
                        else if(groupChatBean.getFrom().equals(CallDispatcher.LoginUser)&&groupChatBean.getSubCategory()!=null&&groupChatBean.getSubCategory().equalsIgnoreCase("grb")){
                            int row = DBAccess.getdbHeler(
                                    SipNotificationListener.getCurrentContext())
                                    . updateChatReplied(groupChatBean);
                            if(row>0)
                            groupChatBean.setReply("1");
                        }
                        if(groupChatBean.getSubCategory()!=null&&groupChatBean.getSubCategory().equalsIgnoreCase("gc_r"))
                        {
                            int row = DBAccess.getdbHeler(
                                    SipNotificationListener.getCurrentContext())
                                    . updateChatReply(groupChatBean);
                            if(row>0) {
                                groupChatBean.setReply("gc_r");
                            }
                        }
                        else if(groupChatBean.getFrom().equals(CallDispatcher.LoginUser)&&groupChatBean.getSubCategory()!=null&&groupChatBean.getSubCategory().equalsIgnoreCase("gc")){
                            int row = DBAccess.getdbHeler(
                                    SipNotificationListener.getCurrentContext())
                                    . updateChatReplied(groupChatBean);
                            if(row>0)
                                groupChatBean.setReply("1");
                        }
						int row = DBAccess.getdbHeler(
								SipNotificationListener.getCurrentContext())
								.insertGroupChat(groupChatBean);
						if (row > 0 && groupChatBean.getSubCategory() != null) {
							if (groupChatBean.getSubCategory()
									.equalsIgnoreCase("gs")) {
								DBAccess.getdbHeler(
										SipNotificationListener
												.getCurrentContext())
										.deleteScheduleMsg(
												groupChatBean.getSignalid());
							}
						}
						GroupBean groupBean = new GroupBean();
						groupBean.setGroupId(groupChatBean.getGroupId());
						groupBean.setModifiedDate(groupChatBean.getSenttime());
						DateFormat dateFormat = new SimpleDateFormat(
								"dd-MM-yyyy hh:mm:ss");
						String strDate = dateFormat.format(new Date());
						groupBean.setRecentDate(strDate);
						if (groupChatBean.getMimetype().equals("text")
								|| groupChatBean.getMimetype().equals(
										"location")) {
							groupBean.setLastMsg(groupChatBean.getFrom()
									+ " : " + groupChatBean.getMessage());
						} else {
							groupBean.setLastMsg(groupChatBean.getFrom() + ":"
									+ groupChatBean.getMimetype() );
						}
						if (groupChatBean.getCategory().equalsIgnoreCase("G")) {
//							DBAccess.getdbHeler().saveOrUpdateGroup(groupBean);
						} else if (groupChatBean.getCategory()
								.equalsIgnoreCase("I")) {
							groupChatBean.setSessionid(groupChatBean
									.getGroupId());
							// DB.getdbHeler().insertChat(groupChatBean);
							groupBean.setGroupName(groupChatBean.getSessionid()
									.replace(CallDispatcher.LoginUser, ""));
//							if(!groupChatBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)){
							groupBean.setLastMsg(groupChatBean.getMessage());
//							}
							groupBean.setUserName(CallDispatcher.LoginUser);
							groupBean.setCreatedDate(groupChatBean
									.getSenttime());
							if (groupChatBean.getMimetype().equals("text")
									|| groupChatBean.getMimetype().equals(
											"location")) {
								groupBean
										.setLastMsg(groupChatBean.getMessage());
								
							} else {

								if(groupChatBean.getFrom().equals(CallDispatcher.LoginUser))
								{
									Log.d("sent","sent"+groupBean.getUserName());
									Log.d("sent","sent"+CallDispatcher.LoginUser);

								groupBean.setLastMsg(groupChatBean.getMimetype() + " Sent");
							}else {
								groupBean.setLastMsg(groupChatBean.getMimetype() + " Received");
							}
							}

						}
						DBAccess.getdbHeler().insertorUpdateIndividualChat(
								groupBean);

						// if (CallDispatcher.message_map
						// .containsKey(groupChatBean.getSessionid()
						// .replace(CallDispatcher.LoginUser,
						// ""))) {
						// CallDispatcher.message_map.remove(groupChatBean
						// .getSessionid().replace(
						// CallDispatcher.LoginUser, ""));
						// }
						// if (groupChatBean.getMimetype().equals("text")
						// || groupChatBean.getMimetype().equals(
						// "location")) {
						// CallDispatcher.message_map.put(
						// groupChatBean.getSessionid().replace(
						// CallDispatcher.LoginUser, ""),
						// groupChatBean.getMessage());
						// } else {
						// CallDispatcher.message_map.put(
						// groupChatBean.getSessionid().replace(
						// CallDispatcher.LoginUser, ""),
						// groupChatBean.getMimetype());
						// }
						for (BuddyInformationBean bbBean : ContactsFragment
								.getBuddyList()) {
							if (!bbBean.isTitle()) {
								if (!bbBean.getStatus().equalsIgnoreCase(
										"pending")
										|| !bbBean.getStatus()
												.equalsIgnoreCase("new")
										&& (groupChatBean.getMessage() != null && !groupChatBean
												.getMessage().equalsIgnoreCase(
														"null"))) {

									String buddyName = groupChatBean
											.getSessionid()
											.replace(CallDispatcher.LoginUser,
													"").trim();
									Log.i("gchat123",
											"buddy : " + buddyName
													+ "list buddy : "
													+ bbBean.getName());
									if (bbBean.getName().equalsIgnoreCase(
											buddyName)) {
										Log.i("gchat123", "buddy : "
												+ buddyName + "last message : "
												+ groupBean.getLastMsg());
//										bbBean.setLastMessage(groupBean
//												.getLastMsg());
										bbBean.setLastMessage(null);
										
										break;
									}
								}
							}
						}
//						ExchangesFragment exchanges = ExchangesFragment
//								.newInstance(SipNotificationListener
//										.getCurrentContext());
//						if (exchanges != null)
//							exchanges.notifyGroupList();

						SingleInstance.mainContext.notifyMessageCount();

					} else if (groupChatBean.getType().equals("104")) {
                        DBAccess.getdbHeler().updateWithdraw(groupChatBean);
//						DBAccess.getdbHeler(
//								SipNotificationListener.getCurrentContext())
//								.deleteGroupChatEntry(
//										groupChatBean.getpSingnalId(),
//										CallDispatcher.LoginUser);
					}
				}
			} catch (Exception e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				else
					e.printStackTrace();
			}
		}
	}

	private void fileWriter(String xml, String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWritter = new FileWriter(file.getPath(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(xml);
			bufferWritter.flush();
			bufferWritter.close();
			bufferWritter = null;
			fileWritter.flush();
			fileWritter.close();
			fileWritter = null;

		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

}
