package com.callHistory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.lib.model.BuddyInformationBean;
import org.lib.model.RecordTransactionBean;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;

public class CallHistoryActivity extends Activity {
	private Context context;

	Handler handler = new Handler();
	private String centername = null;
	String product = null;
	private double timeElapsed = 0;
	private CallHistoryAdapter callHistoryAdapter;
	private ArrayList<RecordTransactionBean> mlist;
	public static boolean isEdit = false;
	Handler history_handler;
	public static String selectedBuddy;
	private String sessionId = "";
	private int position;
	private CallDispatcher callDisp;
	private Boolean isViewed=false;
	private MediaPlayer mPlayer = new MediaPlayer();
	private SeekBar seekProgress;
	private TextView tvTimer;
	private ImageView btnPause;
	int finalTime, startTime;
	private String player = "";
	private Handler durationHandler = new Handler();
	private Boolean isDelete=false;
	private int mPlayingPosition = 0;
	private Handler mHandler = new Handler();
	private boolean mediaplay = false;
	private boolean isfromaudio = true;
	private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {

			Log.i("Test", "CallHistoryActivity>>>>>>>>>>");
			context = this;
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.call_history_row);
			if (SingleInstance.mainContext
					.getResources()
					.getString(R.string.screenshot)
					.equalsIgnoreCase(
							SingleInstance.mainContext.getResources()
									.getString(R.string.yes))) {
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
						WindowManager.LayoutParams.FLAG_SECURE);
			}
			WebServiceReferences.contextTable.put("ordermenuactivity", this);
			mlist = new ArrayList<RecordTransactionBean>();
			ImageView back = (ImageView) findViewById(R.id.back_button);
			TextView calltype = (TextView) findViewById(R.id.ctype_title);

			TextView callcontent = (TextView) findViewById(R.id.ccontent_title);

			TextView from = (TextView) findViewById(R.id.from_title);
			TextView to = (TextView) findViewById(R.id.touser_title);
			TextView date = (TextView) findViewById(R.id.date_title);
			TextView duration = (TextView) findViewById(R.id.duration_title);
			isDelete = getIntent().getBooleanExtra("isDelete", false);
			isfromaudio = getIntent().getBooleanExtra("audiocall", true);

			TextView callstate = (TextView) findViewById(R.id.cstate_title);
			final ImageView preview = (ImageView) findViewById(R.id.play_button);
			seekProgress = (SeekBar) findViewById(R.id.seekBar1);
			seekProgress.setClickable(false);
			tvTimer = (TextView) findViewById(R.id.txt_time);
			final ImageView delete_icon = (ImageView) findViewById(R.id.delete_icon);
			final TextView text_recording = (TextView) findViewById(R.id.text_recording);
			btnPause = (ImageView) findViewById(R.id.btn_pause);
			ImageView owner_img=(ImageView)findViewById(R.id.own_image);
			final RelativeLayout recoding_layout = (RelativeLayout) findViewById(R.id.recoding_layout);
			if (!isfromaudio) {
				owner_img.setVisibility(View.VISIBLE);
			} else {
				owner_img.setVisibility(View.GONE);
			}
			if (isDelete) {
					text_recording.setVisibility(View.GONE);
					recoding_layout.setVisibility(View.GONE);
					owner_img.setVisibility(View.GONE);
			}


			delete_icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.callrecord_delete_dialog);
					dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
					dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
					dialog.show();
					Button cancel = (Button) dialog.findViewById(R.id.save);
					Button delete = (Button) dialog.findViewById(R.id.delete);
					cancel.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					delete.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							text_recording.setVisibility(View.GONE);
							recoding_layout.setVisibility(View.GONE);
							DBAccess.getdbHeler().updaterecordtransaction(sessionId);
							delete_icon.setVisibility(View.GONE);
							dialog.dismiss();
							mPlayer.pause();
						}
					});


				}
			});


			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					SingleInstance.orderToastShow = false;
					// TODO Auto-generated method stub
					finish();
				}
			});

			sessionId = getIntent().getStringExtra("sessionid");
			isViewed = getIntent().getBooleanExtra("isviewed", false);

			position = 0;

			String query = "";

			query = "select * from recordtransactiondetails where sessionid='"
					+ sessionId + "'";

			int pos = 0;
			Log.i("CH", "Query" + query);
			mlist = DBAccess.getdbHeler().getcallhistorydetails(query);
			RecordTransactionBean recordTransactionBean = mlist
					.get(pos);
			if(recordTransactionBean.getRecordedfile() != null){
				text_recording.setVisibility(View.GONE);
				recoding_layout.setVisibility(View.GONE);
				owner_img.setVisibility(View.GONE);

			}

			if (recordTransactionBean.getCalltype() != null) {
				String callType = "";
				if (recordTransactionBean.getCalltype().equalsIgnoreCase(
						"AC")) {
					callType = "Audio Call";
				} else if (recordTransactionBean.getCalltype()
						.equalsIgnoreCase("VC")) {
					callType = "Video Call";
				} else if (recordTransactionBean.getCalltype()
						.equalsIgnoreCase("ABC")) {
					callType = "Audio Broadcast";
				} else if (recordTransactionBean.getCalltype()
						.equalsIgnoreCase("VBC")) {
					callType = "Video Broadcast";
				} else if (recordTransactionBean.getCalltype()
						.equalsIgnoreCase("AP")) {
					callType = "Audio Unicast";
				} else if (recordTransactionBean.getCalltype()
						.equalsIgnoreCase("VP")) {
					callType = "Video Unicast";
				}
				calltype.setText(callType);
			}
			if (recordTransactionBean.getCalltype() != null) {
				callcontent.setText(recordTransactionBean.getCalltype());
			}

			//For this set From and To name
			//start

			if (recordTransactionBean.getFromName() != null) {
				Log.i("callhistory","fromname-->"+recordTransactionBean.getFromName());
				Log.i("callhistory","LoginUser-->"+CallDispatcher.LoginUser);
				if(ContactsFragment.buddyList!=null && ContactsFragment.buddyList.size()>0){
					for(BuddyInformationBean bean:ContactsFragment.buddyList){
						if(bean.getEmailid()!=null) {
							if (bean.getEmailid().equalsIgnoreCase(recordTransactionBean.getFromName())) {
								if (bean.getFirstname() != null && bean.getLastname() != null) {
									from.setText(bean.getFirstname() + " " + bean.getLastname());
									break;
								} else if (bean.getFirstname() != null) {
									from.setText(bean.getFirstname());
									break;
								}
							}
						}

					}

					if(recordTransactionBean.getFromName().trim().equalsIgnoreCase(CallDispatcher.LoginUser.trim())){
						from.setText("Me");
					}
				}else {
					from.setText(recordTransactionBean.getFromName());
				}
			}

			if(recordTransactionBean.getHost_emailid()!=null){
				Log.i("callhistory","fromname-->"+recordTransactionBean.getHost_emailid());
				Log.i("callhistory","LoginUser-->"+CallDispatcher.LoginUser);
				if(ContactsFragment.buddyList!=null && ContactsFragment.buddyList.size()>0){
					for(BuddyInformationBean bean:ContactsFragment.buddyList){
						if(bean.getEmailid()!=null) {
							if (bean.getEmailid().equalsIgnoreCase(recordTransactionBean.getHost_emailid())) {
								if (bean.getFirstname() != null && bean.getLastname() != null) {
									from.setText(bean.getFirstname() + " " + bean.getLastname());
									break;
								} else if (bean.getFirstname() != null) {
									from.setText(bean.getFirstname());
									break;
								}
							}
						}

					}

					if(recordTransactionBean.getHost_emailid().trim().equalsIgnoreCase(CallDispatcher.LoginUser.trim())){
						from.setText("Me");
					}
				}else {
					from.setText(recordTransactionBean.getHost_emailid());
				}
			}

			if (recordTransactionBean.getToName() != null) {
				if(ContactsFragment.buddyList!=null && ContactsFragment.buddyList.size()>0){
					for(BuddyInformationBean bean:ContactsFragment.buddyList){
						if(bean.getEmailid()!=null) {
							if (bean.getEmailid().equalsIgnoreCase(recordTransactionBean.getToName())) {
								if (bean.getFirstname() != null && bean.getLastname() != null) {
									to.setText(bean.getFirstname() + " " + bean.getLastname());
									break;
								} else if (bean.getFirstname() != null) {
									to.setText(bean.getFirstname());
									break;
								}
							}
						}

					}
					if(recordTransactionBean.getHost_emailid()!=null) {
						if (recordTransactionBean.getToName().equalsIgnoreCase(recordTransactionBean.getHost_emailid())) {
							to.setText("Me");
						}
					}
				}else {
					to.setText(recordTransactionBean.getToName());
				}
			}

			if (recordTransactionBean.getTot_participant() != null) {
				if(ContactsFragment.buddyList!=null && ContactsFragment.buddyList.size()>0){
					String buddies=null;
					if(recordTransactionBean.getToName().equalsIgnoreCase(CallDispatcher.LoginUser)){
						to.setText("Me");
					}

					if(recordTransactionBean.getTot_participant().contains(",")) {
						String s[] = recordTransactionBean.getTot_participant().split(",");
						for (int i=0;i<s.length;i++) {
							for (BuddyInformationBean bean : ContactsFragment.buddyList) {
								if (bean.getEmailid() != null) {
									if (bean.getEmailid().equalsIgnoreCase(s[i])) {
										if (bean.getFirstname() != null && bean.getLastname() != null) {
											if (buddies == null) {
												buddies = bean.getFirstname() + " " + bean.getLastname();
											} else {
												buddies = buddies + "," + bean.getFirstname() + " " + bean.getLastname();
											}

										} else if (bean.getFirstname() != null) {
											if (buddies == null) {
												buddies = bean.getFirstname();
											} else {
												buddies = buddies + "," + bean.getFirstname();
											}
										}
									}
								}

							}
						}
					}else{
						for (BuddyInformationBean bean : ContactsFragment.buddyList) {
							if (bean.getEmailid() != null) {
								if (bean.getEmailid().equalsIgnoreCase(recordTransactionBean.getTot_participant())) {
									if (bean.getFirstname() != null && bean.getLastname() != null) {
										if (buddies == null) {
											buddies = bean.getFirstname() + " " + bean.getLastname();
										} else {
											buddies = buddies + "," + bean.getFirstname() + " " + bean.getLastname();
										}


									} else if (bean.getFirstname() != null) {
										if (buddies == null) {
											buddies = bean.getFirstname();
										} else {
											buddies = buddies + "," + bean.getFirstname();
										}
									}
								}
							}

						}
					}
					if(buddies!=null){
						to.setText(buddies);
					}

				}else {
					to.setText(recordTransactionBean.getToName());
				}
			}

			if(recordTransactionBean.getCall_state()!=null){
				if(recordTransactionBean.getCall_state().equalsIgnoreCase("missedcall")){
					if(recordTransactionBean.getHost()!=null){
						if(ContactsFragment.buddyList!=null && ContactsFragment.buddyList.size()>0){
							for(BuddyInformationBean bean:ContactsFragment.buddyList){
								if(bean.getEmailid()!=null) {
									if (bean.getEmailid().equalsIgnoreCase(recordTransactionBean.getHost())) {
										if (bean.getFirstname() != null && bean.getLastname() != null) {
											from.setText(bean.getFirstname() + " " + bean.getLastname());
											break;
										} else if (bean.getFirstname() != null) {
											from.setText(bean.getFirstname());
											break;
										}
									}
								}

							}
							to.setText("Me");
						}

					}
				}
			}
			//End
			if (recordTransactionBean.getStartTime() != null) {
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
				Date d1 = dateformat.parse(recordTransactionBean.getStartTime());
				String newdate = sdf.format(d1);
				date.setText(newdate);
			}
			if (recordTransactionBean.getCallDuration() != null) {
				if(recordTransactionBean.getCallDuration().contains(":")){
					String hour=recordTransactionBean.getCallDuration().split(":")[0];
					String min=recordTransactionBean.getCallDuration().split(":")[1];
					String sec=recordTransactionBean.getCallDuration().split(":")[2];
					if(hour.length()==1){
						hour="0"+hour;
					}
					if(min.length()==1){
						min="0"+min;
					}
					if(sec.length()==1){
						sec="0"+sec;
					}
					String totTime=hour+":"+min+":"+sec;
					duration.setText(totTime);
					tvTimer.setText(hour+":"+min+":"+sec);
				}else {
					duration.setText(recordTransactionBean
							.getCallDuration());
				}
//							tvTimer.setText(recordTransactionBean
//									.getCallDuration());


			}else{
				duration.setText("00:00:00");
			}

			if (recordTransactionBean.getSessionid() != null) {
				preview.setTag(recordTransactionBean.getSessionid());
			}

			if (isViewed) {
				String strUpdateNote = "update recordtransactiondetails set status='" + 1
						+ "' where sessionid='" + sessionId
						+ "'";
				if (DBAccess.getdbHeler().ExecuteQuery(strUpdateNote)) {
					recordTransactionBean.setStatus(1);
				}
			}


			preview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {


						String file = Environment
								.getExternalStorageDirectory()
								+ "/COMMedia/CallRecording/"
								+ v.getTag().toString() + ".wav";
						Log.d("Stringpath", "mediapath--->" + file);


//									File path = new File(file);
//									mPlayer.setDataSource(file.getPath());
						int CountFiles = new File(Environment
								.getExternalStorageDirectory()
								+ "/COMMedia/CallRecording/").listFiles().length;
						Log.d("Test", "Length of the files@@@----->");
						File newfile=new File(file);

						if (mPlayer.isPlaying()) {
							mPlayer.pause();
							preview.setBackgroundResource(R.drawable.play);
						} else {
							preview.setBackgroundResource(R.drawable.audiopause);
							if(newfile.exists())
							playAudio(file, 0);

						}
						if(newfile.exists()) {

							if (position == mPlayingPosition) {
								mProgressUpdater.mBarToUpdate = seekProgress;
								mProgressUpdater.tvToUpdate = tvTimer;
								mHandler.postDelayed(mProgressUpdater, 100);
							} else {

								try {
									Log.d("Stringpath", "mediapath--->");
									seekProgress.setProgress(0);
									MediaMetadataRetriever mmr = new MediaMetadataRetriever();
									mmr.setDataSource(file);
									String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
									mmr.release();
									String min, sec;
									min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration)));
									sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(duration)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration))));
									if (Integer.parseInt(min) < 10) {
										min = 0 + String.valueOf(min);
									}
									if (Integer.parseInt(sec) < 10) {
										sec = 0 + String.valueOf(sec);
									}
									tvTimer.setText(min + ":" + sec);
//                            audio_tv.setText(duration);
								} catch (Exception e) {
									e.printStackTrace();
								}

								seekProgress.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
								seekProgress.setProgress(0);
								if (mProgressUpdater.mBarToUpdate == seekProgress) {
									//this progress would be updated, but this is the wrong position
									mProgressUpdater.mBarToUpdate = null;
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}


				}
			});

		}catch (Exception e){
			e.printStackTrace();
		}




					}





//									if (file.exists()) {
//										Intent intent = new Intent(context,
//												MultimediaUtils.class);
//										intent.putExtra("filePath", file.getPath());
//										intent.putExtra("requestCode", 4);
//										intent.putExtra("action", "audio");
//										intent.putExtra("createOrOpen", "open");
//										context.startActivity(intent);
//									} else {
//										// Toast.makeText(context,
//										// "Sorry file not available",
//										// Toast.LENGTH_LONG).show();
//									}






	private class PlaybackUpdater implements Runnable {
		public SeekBar mBarToUpdate = null;
		public TextView tvToUpdate = null;

		@Override
		public void run() {
			if ((mPlayingPosition != -1) && (null != mBarToUpdate)) {
				Log.d("Mposition","seekbar---->");
				double tElapsed = mPlayer.getCurrentPosition();
				int fTime = mPlayer.getDuration();
				double timeRemaining = fTime - tElapsed;
				double sTime = mPlayer.getCurrentPosition();

				String min, sec;
				//for decreasing
//                min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining));
//                sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining)));

				//for increasing
				min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) sTime));
				sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) sTime)));
				if (Integer.parseInt(min) < 10) {
					min = 0 + String.valueOf(min);
				}
				if (Integer.parseInt(sec) < 10) {
					sec = 0 + String.valueOf(sec);
				}
				tvToUpdate.setText(min + ":" + sec);
				mBarToUpdate.setProgress((100 * mPlayer.getCurrentPosition() / mPlayer.getDuration()));
//                tvToUpdate.setText(String.format("%d:%d ",TimeUnit.MILLISECONDS.toMinutes((long) fTime),TimeUnit.MILLISECONDS.toSeconds((long) fTime) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) fTime))));
				mHandler.postDelayed(this, 500);

			} else {
				//not playing so stop updating
			}
		}
	}

	private void stopPlayback() {
		mPlayingPosition = 0;
		mProgressUpdater.mBarToUpdate = null;
		mProgressUpdater.tvToUpdate = null;
		if (mPlayer != null && mPlayer.isPlaying())
			mPlayer.stop();
	}

	public void playAudio(String fname,  int position) {
		try {
			mPlayer.reset();
			mPlayer.setDataSource(fname);
			mPlayer.prepare();
			mPlayer.start();
			mPlayingPosition = position;

			mHandler.postDelayed(mProgressUpdater, 500);

		} catch (IOException e) {

			e.printStackTrace();
			stopPlayback();
		}
	}



private Runnable UpdateSongTime = new Runnable() {
	public void run() {
		startTime = mPlayer.getCurrentPosition();
//            seekBar.setProgress((int) startTime);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				startPlayProgressUpdater();

			}
		}, 100);

		history_handler.postDelayed(this, 100);
	}
};


	private void startPlayProgressUpdater() {
		if (mPlayer != null) {
			if (mPlayer.isPlaying()) {
				Log.d("lg", "play progress().....");
				long milliseconds = mPlayer.getCurrentPosition();
				timeElapsed = mPlayer.getCurrentPosition();

				double timeRemaining = finalTime - timeElapsed;
				String min, sec;
				min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining));
				sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining)));

				if (Integer.parseInt(min) < 10) {
					min = 0 + String.valueOf(min);
				}
				if (Integer.parseInt(sec) < 10) {
					sec = 0 + String.valueOf(sec);
				}
//                txt_time.setText(min + ":" + sec);
			}
		}
	}



			@Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    public int getCount() {
		return mlist.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		RecordTransactionBean bean = (RecordTransactionBean) mlist.get(arg0);
		return bean;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	protected void onDestroy() {

		if (WebServiceReferences.contextTable.containsKey("ordermenuactivity")) {
			WebServiceReferences.contextTable.remove("ordermenuactivity");
		}
		mPlayer.stop();
		super.onDestroy();
		}

}
