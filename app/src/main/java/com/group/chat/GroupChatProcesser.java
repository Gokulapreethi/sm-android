package com.group.chat;

import org.lib.model.SignalingBean;

import android.os.Environment;
import android.util.Log;

import com.bean.GroupChatBean;
import com.cg.hostedconf.AppReference;
import com.main.AppMainActivity;
import com.util.Queue;
import com.util.SingleInstance;

public class GroupChatProcesser extends Thread {

	private Queue queue;
	private boolean isRunning;

	public GroupChatProcesser() {
		queue = new Queue();
	}

	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public void run() {

		Log.d("GROUP_CHAT", "Group Processer Started.");

		while (isRunning) {
			try {
				Object object = queue.getObject();
				Log.d("GROUP_CHAT", "Got Object : " + object);
				if (object != null) {
					if (object instanceof GroupChatBean) {
						GroupChatBean gcBean = (GroupChatBean) object;

						SignalingBean sb = new SignalingBean();
						sb.setSignalid(gcBean.getSignalid());
						sb.setSessionid(gcBean.getSessionid());
						sb.setTo(gcBean.getTo());
						sb.setType(gcBean.getType());
						sb.setTolocalip(null);
						sb.setTopublicip(null);
						sb.setRequestSource(gcBean);

						Log.d("GROUP_CHAT", "Going to send : " + gcBean.getTo());

						AppMainActivity.commEngine.makeIM(sb);

						gcBean.setSessionid(sb.getSessionid());
						GroupChatBean bean = gcBean.clone();
						if ((!bean.getMimetype().equalsIgnoreCase("text") && !bean
								.getMimetype().equalsIgnoreCase("location") && !bean.getMimetype().equalsIgnoreCase("link"))
								&& !bean.getMediaName().contains(
										Environment
												.getExternalStorageDirectory()
												+ "/COMMedia/"))
                        {
                            String[] path=bean.getMediaName().split(",");
                            if(path.length>0)
                            {
                                String sp=null;
                                for(int i=0;i<path.length;i++)
                                {
                                   if(sp==null)
                                   {
                                      sp= Environment
                                              .getExternalStorageDirectory()
                                              + "/COMMedia/"+path[i];
                                   }else{
                                       sp= sp+","+Environment
                                               .getExternalStorageDirectory()
                                               + "/COMMedia/"+path[i];
                                   }
                                }
                                if(sp!=null) {
                                    bean.setMediaName(sp);
                                }else{
                                    bean.setMediaName(Environment
                                            .getExternalStorageDirectory()
                                            + "/COMMedia/" + bean.getMediaName());
                                }
                            }else {
                                bean.setMediaName(Environment
                                        .getExternalStorageDirectory()
                                        + "/COMMedia/" + bean.getMediaName());
                            }
                        }
						if(bean.getMimetype().equalsIgnoreCase("text") || bean.getMimetype().equalsIgnoreCase("location")
								||bean.getMimetype().equalsIgnoreCase("link") ) {
							SingleInstance.getGroupChatHistoryWriter().getQueue()
									.addObject(bean);
						}
					}
				} else {
					break;
				}
			} catch (Exception e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				else
					e.printStackTrace();
			}
		}
		Log.d("GROUP_CHAT", "Group Processer Stopped.");
	}

}
