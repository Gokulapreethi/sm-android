package com.cg.commongui;
//
//import java.io.IOException;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.graphics.Color;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.media.MediaPlayer.OnCompletionListener;
//import android.media.MediaPlayer.OnPreparedListener;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.MediaController;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cg.account.R;
//import com.cg.commonclass.WebServiceReferences;
//
///**
// * Full screen view of the video player. Video files are which is recorded by
// * our application
// * 
// * @author
// * 
// */
//public class VideoPlayer extends Activity implements OnClickListener,
//		SurfaceHolder.Callback {
//
//	private LinearLayout llayVideoControls;
//
//	private SurfaceView surfaceview;
//
//	private SurfaceHolder holder;
//
//	private MediaPlayer player;
//
//	private Button btnClose;
//
//	private MediaController mediaController;
//
//	private String filepath;
//
//	private int time;
//
//	private int noScrHeight;
//
//	private int noScrWidth;
//
//	private LinearLayout llayVolumeControl;
//
//	private SeekBar seekVolume;
//
//	private TextView tvVolume;
//
//	private ImageView ivSpeaker;
//
//	private AudioManager audioManager;
//
//	private int maxVolume;
//
//	private int curVolume;
//
//	public ProgressDialog progressDialog;
//
//	private SeekBar videoSeekBar = null;
//
//	private Handler playerHandler = new Handler();;
//
//	/** Called when the activity is first created. */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.video_player_new);
//
//		getWindow().clearFlags(
//				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//		WebServiceReferences.contextTable.put("videoplayer", this);
//		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//		curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//
//		/** GetDisply Screen Height and Width */
//		DisplayMetrics displaymetrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//		noScrHeight = displaymetrics.heightPixels;
//		noScrWidth = displaymetrics.widthPixels;
//
//		llayVideoControls = (LinearLayout) findViewById(R.id.video_control_layout);
//		llayVolumeControl = (LinearLayout) findViewById(R.id.llay_vp_volume_control);
//		seekVolume = (SeekBar) findViewById(R.id.vp_volume_progress);
//		videoSeekBar = (SeekBar) findViewById(R.id.vp_video_progress);
//		videoSeekBar.setVisibility(View.GONE);
//		tvVolume = (TextView) findViewById(R.id.vp_volume);
//		tvVolume.setTextColor(Color.BLACK);
//		tvVolume.setText(Integer.toString(curVolume));
//		ivSpeaker = (ImageView) findViewById(R.id.img_speaker);
//		VolumeImageDisplay();
//		seekVolume.setMax(maxVolume);
//		seekVolume.setProgress(curVolume);
//		seekVolume
//				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//					@Override
//					public void onStopTrackingTouch(SeekBar arg0) {
//
//					}
//
//					@Override
//					public void onStartTrackingTouch(SeekBar arg0) {
//
//					}
//
//					@Override
//					public void onProgressChanged(SeekBar arg0, int arg1,
//							boolean arg2) {
//						audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
//								arg1, 0);
//						curVolume = arg1;
//						tvVolume.setText(Integer.toString(arg1));
//						VolumeImageDisplay();
//					}
//				});
//
//		btnClose = (Button) findViewById(R.id.btn_vd_close);
//		btnClose.setOnClickListener(this);
//		btnClose.setText("Exit");
//
//		mediaController = new MediaController(this);
//
//		player = new MediaPlayer();
//
//		surfaceview = (SurfaceView) findViewById(R.id.surfaceViewFrame);
//
//		progressDialog = ProgressDialog.show(VideoPlayer.this, "",
//				"Please Wait...", true);
//		progressDialog.setCancelable(true);
//
//		this.holder = surfaceview.getHolder();
//		holder.addCallback(this);
//
//		/*
//		 * vv.setLayoutParams(new FrameLayout.LayoutParams(noScrWidth,
//		 * noScrHeight));
//		 */
//
//		mediaController.setAnchorView(surfaceview);
//		// filepath = "/sdcard/test.3gp";
//		filepath = getIntent().getStringExtra("File_Path");
//		time = getIntent().getIntExtra("time", 0);
//
//		Log.e("TAGGGGGG", "mediacontroller" + "***********" + time + filepath);
//
//		try {
//			player.setDataSource(filepath);
//			player.prepare();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		if (time != 0) {
//			player.seekTo(time);
//		}
//
//		int topContainerId = getResources().getIdentifier(
//				"mediacontroller_progress", "id", "android");
//
//		player.setOnPreparedListener(new OnPreparedListener() {
//
//			@Override
//			public void onPrepared(MediaPlayer arg0) {
//
//				int videoWidth = player.getVideoWidth();
//				int videoHeight = player.getVideoHeight();
//				float videoProportion = (float) videoWidth
//						/ (float) videoHeight;
//				int screenWidth = getWindowManager().getDefaultDisplay()
//						.getWidth();
//				int screenHeight = getWindowManager().getDefaultDisplay()
//						.getHeight();
//				float screenProportion = (float) screenWidth
//						/ (float) screenHeight;
//				android.view.ViewGroup.LayoutParams lp = surfaceview
//						.getLayoutParams();
//
//				if (videoProportion > screenProportion) {
//					lp.width = screenWidth;
//					lp.height = (int) ((float) screenWidth / videoProportion);
//				} else {
//					lp.width = (int) (videoProportion * (float) screenHeight);
//					lp.height = screenHeight;
//				}
//				surfaceview.setLayoutParams(lp);
//
//				progressDialog.dismiss();
//
//				if (!player.isPlaying()) {
//					player.start();
//				}
//				Log.i("video",
//						"=== > total duration : : " + player.getDuration());
//				Log.i("video",
//						"=== > current pos :: " + player.getCurrentPosition());
//				videoSeekBar.setProgress(0);
//				videoSeekBar.setMax(player.getDuration());
//				updateProgressBar();
//				llayVideoControls.setVisibility(View.VISIBLE);
//				llayVolumeControl.setVisibility(View.VISIBLE);
//				llayVideoControls.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						llayVideoControls.setVisibility(View.INVISIBLE);
//					}
//				}, 7000);
//				llayVolumeControl.postDelayed(new Runnable() {
//
//					@Override
//					public void run() {
//						llayVolumeControl.setVisibility(View.INVISIBLE);
//					}
//				}, 7000);
//
//				videoSeekBar
//						.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//
//							@Override
//							public void onStopTrackingTouch(SeekBar seekBar) {
//								playerHandler.removeCallbacks(updateProg);
//								int currentPosition = seekBar.getProgress();
//								player.seekTo(currentPosition);
//								updateProgressBar();
//							}
//
//							@Override
//							public void onStartTrackingTouch(SeekBar seekBar) {
//								// TODO Auto-generated method stub
//								playerHandler.removeCallbacks(updateProg);
//							}
//
//							@Override
//							public void onProgressChanged(SeekBar seekBar,
//									int progress, boolean fromUser) {
//							}
//						});
//
//			}
//
//			public void updateProgressBar() {
//				playerHandler.postDelayed(updateProg, 100);
//			}
//
//			private Runnable updateProg = new Runnable() {
//				public void run() {
//					try {
//						if (player != null) {
//							videoSeekBar.setProgress(player
//									.getCurrentPosition());
//						}
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			};
//
//		});
//
//		surfaceview.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				llayVideoControls.setVisibility(View.VISIBLE);
//				llayVolumeControl.setVisibility(View.VISIBLE);
//				llayVideoControls.postDelayed(new Runnable() {
//
//					@Override
//					public void run() {
//						llayVideoControls.setVisibility(View.INVISIBLE);
//
//					}
//				}, 7000);
//				llayVolumeControl.postDelayed(new Runnable() {
//
//					@Override
//					public void run() {
//						llayVolumeControl.setVisibility(View.INVISIBLE);
//
//					}
//				}, 7000);
//
//				return false;
//			}
//		});
//
//		player.setOnCompletionListener(new OnCompletionListener() {
//
//			@Override
//			public void onCompletion(MediaPlayer mp) {
//				if (player != null)
//					player.stop();
//				Log.d("log", ".................................... close");
//				VideoPlayer.this.finish();
//			}
//		});
//
//	}
//
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		WebServiceReferences.contextTable.remove("videoplayer");
//		if (player != null) {
//			player.stop();
//			player = null;
//		}
//
//		super.onDestroy();
//	}
//
//	/**
//	 * Change the speaker image respective to the volume level
//	 */
//	void VolumeImageDisplay() {
//
//		int state = maxVolume / 2;
//
//		if (curVolume > state) {
//			ivSpeaker.setBackgroundResource(R.drawable.vol_high);
//			tvVolume.setTextColor(Color.RED);
//		} else if (curVolume == 0) {
//			ivSpeaker.setBackgroundResource(R.drawable.vol_muted);
//			tvVolume.setTextColor(Color.BLACK);
//		} else {
//			tvVolume.setTextColor(Color.GREEN);
//			ivSpeaker.setBackgroundResource(R.drawable.vol_medium);
//		}
//	}
//
//	public void stopPlayback() {
//		if (player.isPlaying()) {
//			player.stop();
//			player.seekTo(player.getDuration());
//		}
//
//	}
//
//	/**
//	 * Handle the button click events
//	 */
//	@Override
//	public void onClick(View v) {
//
//		switch (v.getId()) {
//
//		case R.id.btn_vd_close:
//			player.stop();
//			Log.d("log", ".................................... close");
//			VideoPlayer.this.finish();
//			break;
//
//		}
//
//	}
//
//	/**
//	 * To show the toast messages
//	 * 
//	 * @param msg
//	 */
//	void ShowToast(String msg) {
//		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//	}
//
//	/**
//	 * When clicked the back button finish the current running activity
//	 */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//			if (player != null)
//				player.stop();
//			finish();
//		}
//
//		return super.onKeyDown(keyCode, event);
//	}
//
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		// TODO Auto-generated method stub
//
//		if (outState == null) {
//			outState = new Bundle();
//		}
//		outState.putString("File_Path", filepath);
//		super.onSaveInstanceState(outState);
//	}
//
//	public void Close() {
//		this.finish();
//	}
//
//	@Override
//	public void surfaceChanged(SurfaceHolder holder, int format, int width,
//			int height) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void surfaceCreated(SurfaceHolder holder) {
//		// TODO Auto-generated method stub
//		this.holder = holder;
//		player.setDisplay(holder);
//	}
//
//	@Override
//	public void surfaceDestroyed(SurfaceHolder holder) {
//		// TODO Auto-generated method stub
//		synchronized (this) {
//			this.notifyAll();
//		}
//	}
//
//	public void notifyGSMCallAccepted() {
//		if (player != null && player.isPlaying())
//			stopPlayback();
//	}
//
//}
