package com.callHistory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
			isDelete=getIntent().getBooleanExtra("isDelete",false);

			TextView callstate = (TextView) findViewById(R.id.cstate_title);
			final ImageView preview = (ImageView) findViewById(R.id.play_button);
			seekProgress = (SeekBar) findViewById(R.id.seekBar1);
			seekProgress.setClickable(false);
			Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer1);
			tvTimer = (TextView) findViewById(R.id.txt_time);
			final ImageView delete_icon = (ImageView) findViewById(R.id.delete_icon);
			final TextView text_recording = (TextView)findViewById(R.id.text_recording);
			btnPause = (ImageView)findViewById(R.id.btn_pause);
			final RelativeLayout recoding_layout = (RelativeLayout)findViewById(R.id.recoding_layout);
			if(isDelete){
				text_recording.setVisibility(View.GONE);
				recoding_layout.setVisibility(View.GONE);
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
							dialog.dismiss();
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

					try {
						String query = "";

						query = "select * from recordtransactiondetails where sessionid='"
								+ sessionId + "'";

						Log.i("CH", "Query" + query);
						mlist = DBAccess.getdbHeler().getcallhistorydetails(query);
						RecordTransactionBean recordTransactionBean = mlist
								.get(position);

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
						if (recordTransactionBean.getFromName() != null) {
							from.setText(recordTransactionBean.getFromName());
						}
						if (recordTransactionBean.getToName() != null) {
							to.setText(recordTransactionBean.getToName());
						}
						if (recordTransactionBean.getStartTime() != null) {
							date.setText(recordTransactionBean.getStartTime());
						}
						if (recordTransactionBean.getCallDuration() != null) {
							duration.setText(recordTransactionBean
									.getCallDuration());
							tvTimer.setText(recordTransactionBean
									.getCallDuration());


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
//									preview.setBackgroundResource(R.drawable.audiopause);
//									preview.setVisibility(View.GONE);
//									btnPause.setVisibility(View.VISIBLE);

									String file = Environment
											.getExternalStorageDirectory()
											+ "/COMMedia/CallRecording/"
											+ v.getTag().toString() + ".wav";


									Log.d("Stringpath", "mediapath--->"+file);
//									File path = new File(file);
//									mPlayer.setDataSource(file.getPath());
									int CountFiles = new File(Environment
											.getExternalStorageDirectory()
											+ "/COMMedia/CallRecording/").listFiles().length;
									Log.d("Test", "Length of the files@@@----->");

									playAudio(file, 0);








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
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
				}

				catch(
				Exception e
				)

				{
					e.printStackTrace();
				}

			}


	private int mPlayingPosition = 0;
	private Handler mHandler = new Handler();

	private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();

	private class PlaybackUpdater implements Runnable {
		public SeekBar mBarToUpdate = null;
		public TextView tvToUpdate = null;

		@Override
		public void run() {
			if ((mPlayingPosition != 0) && (null != mBarToUpdate)) {
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

			//trigger list refresh, this will make progressbar start updating if visible
//			adapter.notifyDataSetChanged();
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
		super.onDestroy();
		}

}
