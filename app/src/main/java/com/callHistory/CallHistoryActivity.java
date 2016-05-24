package com.callHistory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.lib.model.RecordTransactionBean;

import android.app.Activity;
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
	private CallHistoryAdapter callHistoryAdapter;
	private ArrayList<RecordTransactionBean> mlist;
	public static boolean isEdit = false;

	public static String selectedBuddy;
	private String sessionId = "";
	private int position;
	private CallDispatcher callDisp;
	private Boolean isViewed=false;
	private MediaPlayer mPlayer = null;
	private SeekBar seekProgress;
	private TextView tvTimer;
	private ImageView btnPause;
	private String player = "";
	private Handler durationHandler = new Handler();

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

			delete_icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					text_recording.setVisibility(View.GONE);
					recoding_layout.setVisibility(View.GONE);


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

									File file = new File(Environment
											.getExternalStorageDirectory()
											+ "/COMMedia/CallRecording/"
											+ v.getTag().toString() + ".wav");
									Log.d("Stringpath", "mediapath--->"+file);
//									File path = new File(file);
//									mPlayer.setDataSource(file.getPath());
									int CountFiles = new File(Environment
											.getExternalStorageDirectory()
											+ "/COMMedia/CallRecording/").listFiles().length;
									Log.d("Test", "Length of the files@@@----->"
											+ CountFiles);







									if (file.exists()) {
										Intent intent = new Intent(context,
												MultimediaUtils.class);
										intent.putExtra("filePath", file.getPath());
										intent.putExtra("requestCode", 4);
										intent.putExtra("action", "audio");
										intent.putExtra("createOrOpen", "open");
										context.startActivity(intent);
									} else {
										// Toast.makeText(context,
										// "Sorry file not available",
										// Toast.LENGTH_LONG).show();
									}
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

//	private final MediaPlayer mPlayer = new MediaPlayer();
	private int mPlayingPosition = -1;
	private Handler mHandler = new Handler();

	private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();

	private class PlaybackUpdater implements Runnable {
		public SeekBar mBarToUpdate = null;
		public TextView tvToUpdate = null;

		@Override
		public void run() {
			if ((mPlayingPosition != -1) && (null != mBarToUpdate)) {
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
		mPlayingPosition = -1;
		mProgressUpdater.mBarToUpdate = null;
		mProgressUpdater.tvToUpdate = null;
		if (mPlayer != null && mPlayer.isPlaying())
			mPlayer.stop();
	}

	private void playAudio(String fname, int position) {
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

//	if (position == mPlayingPosition) {
		//pb.setVisibility(View.VISIBLE);
//		mProgressUpdater.mBarToUpdate = seekProgress;
//		mProgressUpdater.tvToUpdate = tvTimer;
//		mHandler.postDelayed(mProgressUpdater, 100);
//	} else {
		//pb.setVisibility(View.GONE);
//		if (gcBean.getMimetype().equals("audio")) {
//			try {
//				seekProgress.setProgress(0);
//				MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//				mmr.setDataSource();
//				String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//				mmr.release();
//				String min, sec;
//				min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration)));
//				sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(duration)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration))));
//				if (Integer.parseInt(min) < 10) {
//					min = 0 + String.valueOf(min);
//				}
//				if (Integer.parseInt(sec) < 10) {
//					sec = 0 + String.valueOf(sec);
//				}
//				tvTimer.setText(min + ":" + sec);
////                            audio_tv.setText(duration);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			seekProgress.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
//			seekProgress.setProgress(0);
//			if (mProgressUpdater.mBarToUpdate == seekProgress) {
//				//this progress would be updated, but this is the wrong position
//				mProgressUpdater.mBarToUpdate = null;
//			}
//		}
////	}
//
//	if(gcBean.isPlaying())
//	{
//		audio_play.setBackgroundResource(R.drawable.audiopause);
//	}else
//	{
//		audio_play.setBackgroundResource(R.drawable.play);
//	}
//	audio_play.setOnClickListener(new OnClickListener() {
//		@Override
//		public void onClick(View view) {
//			if(finalPlayBean == null)
//			{
//				.setBackgroundResource(R.drawable.audiopause);
//				playAudio(gcBean.getMediaName(), position);
//				gcBean.setPlaying(true);
//				finalPlayBean = gcBean;
//			}else if(finalPlayBean == gcBean)
//			{
//				if(mPlayer.isPlaying())
//				{
//					mPlayer.pause();
//					audio_play.setBackgroundResource(R.drawable.play);
//					gcBean.setPlaying(false);
//				}else
//				{
//					gcBean.setPlaying(true);
//					audio_play.setBackgroundResource(R.drawable.audiopause);
//					mPlayer.start();
//
//				}
//			}else
//			{
//				finalPlayBean.setPlaying(false);
//				finalPlayBean = gcBean;
//				finalPlayBean.setPlaying(true);
//				audio_play.setBackgroundResource(R.drawable.audiopause);
//				playAudio(gcBean.getMediaName(), position);
//
//			}
//	public void play(View view) {
//		mPlayer.start();
////		timeElapsed =
//		seekProgress.setProgress(mPlayer.getCurrentPosition());
//		durationHandler.postDelayed(updateSeekBarTime, 100);
//	}
//
//
//	private Runnable updateSeekBarTime = new Runnable() {
//		public void run() {
//			//get current position
////			timeElapsed = ;
//			//set seekbar progress
//			seekProgress.setProgress(mPlayer.getCurrentPosition());
//			//set time remaing
////			double timeRemaining = finalTime - timeElapsed;
////			tvTimer.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
//
//			//repeat yourself that again in 100 miliseconds
//			durationHandler.postDelayed(this, 100);
//		}
//	};



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
