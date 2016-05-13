package com.cg.files;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.main.AppMainActivity;
import com.util.VideoPlayer;

public class AutoPlayerScreen extends Activity {

	private Context context;
	private Handler audioPlayHandler;
	ImageView ivThunb;
	private int volume = 50;
	private MediaPlayer mp = null;
	private VideoView videoView = null;
	private CallDispatcher callDisp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		WebServiceReferences.contextTable.put("auto_play", context);
		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);
		initiatePlayer();
	}

	private void initiatePlayer() {

		try {
			Components cmpts = WebServiceReferences.llAutoPlayContent
					.removeFirst();
			Log.i("thread", "############# size" + cmpts);
			if (cmpts != null) {
				if (cmpts.getcomponentType().equals("audio")) {
					// startAudioPlayer(cmpts);
					Intent intent = new Intent(context, MultimediaUtils.class);
					intent.putExtra("filePath", cmpts.getContentPath());
					intent.putExtra("requestCode", "6");
					intent.putExtra("action", "audio");
					intent.putExtra("createOrOpen", "open");
					startActivity(intent);
				} else if (cmpts.getcomponentType().equals("video")) {
					startVideoPlayer(cmpts);
				}
			} else {
				finish();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			finish();
		}
	}

	private void startAudioPlayer(final Components component) {

		LinearLayout llayContent = new LinearLayout(context);
		llayContent.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		LinearLayout.LayoutParams lprms = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lprms.gravity = Gravity.CENTER;

		LinearLayout llayAudioPlayer = new LinearLayout(context);
		llayAudioPlayer.setId(1);
		llayAudioPlayer.setOrientation(LinearLayout.VERTICAL);
		llayAudioPlayer.setLayoutParams(lprms);

		// llayAudioPlayer.setBackgroundResource(R.layout.rounded_corner);

		mp = new MediaPlayer();

		final LinearLayout llaySoundControl = new LinearLayout(context);
		llaySoundControl.setPadding(10, 10, 0, 10);
		final TextView tvSound = new TextView(context);
		tvSound.setText(" " + volume);
		tvSound.setGravity(Gravity.CENTER);
		final Button ivSound = new Button(context);
		ivSound.setGravity(Gravity.CENTER);
		ivSound.setWidth(30);
		ivSound.setHeight(30);
		ivSound.setHint("sound");
		ivSound.setHintTextColor(Color.TRANSPARENT);
		ivSound.setBackgroundResource(R.drawable.vol_medium);
		ivSound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String hint = ivSound.getHint().toString();
				if (hint.equals("sound")) {
					ivSound.setHint("mute");
					mp.setVolume(0, 0);
					tvSound.setText(" 0");
					ivSound.setBackgroundResource(R.drawable.vol_muted);
				} else {
					ivSound.setHint("mute");
					mp.setVolume(volume, volume);
					tvSound.setText(" " + volume);
					if (volume > 60)
						ivSound.setBackgroundResource(R.drawable.vol_high);
					else
						ivSound.setBackgroundResource(R.drawable.vol_medium);
				}

			}
		});
		final SeekBar seekSound = new SeekBar(context);
		seekSound.setProgress(volume);
		seekSound.setDrawingCacheBackgroundColor(Color.BLUE);
		seekSound.setPadding(10, 0, 10, 0);
		seekSound.setLayoutParams(new LayoutParams(200, 30));
		seekSound.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				volume = progress;
				Log.i("volume", "volume:" + progress);
				mp.setVolume(progress, progress);
				tvSound.setText(" " + progress);
				if (progress > 60)
					ivSound.setBackgroundResource(R.drawable.vol_high);
				else if (progress == 0)
					ivSound.setBackgroundResource(R.drawable.vol_muted);
				else
					ivSound.setBackgroundResource(R.drawable.vol_medium);
			}
		});
		llaySoundControl.addView(ivSound);
		llaySoundControl.addView(seekSound);
		llaySoundControl.addView(tvSound);
		llaySoundControl.setVisibility(View.INVISIBLE);

		final SeekBar seek = new SeekBar(context);

		final TableLayout tblAudio = new TableLayout(context);

		final LinearLayout llayProgress = new LinearLayout(context);
		ProgressBar progBar = new ProgressBar(context);
		progBar.setLayoutParams(new LayoutParams(50, 50));

		llayProgress.addView(progBar);

		TableRow trControls = new TableRow(context);
		trControls.setGravity(Gravity.CENTER);
		final Button btnAudioPlay = new Button(context);
		btnAudioPlay.setWidth(120);

		final Button btnAudioStop = new Button(context);
		btnAudioStop.setWidth(120);

		seek.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mp.isPlaying()) {
					SeekBar sb = (SeekBar) v;
					mp.seekTo(sb.getProgress());
				}
				return false;
			}
		});
		seek.setVisibility(View.INVISIBLE);

		btnAudioPlay.setText("Play");
		btnAudioStop.setText("  >>  ");

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {

				try {
					Log.i("thread", "@@@@@@@@@@@ on completedd");

					initiatePlayer();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		mp.reset();
		try {
			mp.setDataSource(component.getContentPath());
			mp.setLooping(false); // Set looping
			mp.prepare();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		btnAudioPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				File CheckFile = new File(component.getContentPath());
				if (CheckFile.exists()) {

					String strPlayStatus = btnAudioPlay.getText().toString();

					if (strPlayStatus.equalsIgnoreCase("play")) {
						btnAudioPlay.setText("pause");
						llaySoundControl.setVisibility(View.VISIBLE);
						seek.setVisibility(View.VISIBLE);

						try {
							mp.start();
							seek.setMax(mp.getDuration());
							startPlayProgressUpdater();
						} catch (Exception e) {

						}
					} else if (strPlayStatus.equalsIgnoreCase("pause")) {
						btnAudioPlay.setText("play");
						mp.pause();
					}

				} else {
					Toast.makeText(context, "Audio File is not available",
							Toast.LENGTH_SHORT).show();
				}

			}

			private void startPlayProgressUpdater() {
				if (mp != null) {
					seek.setProgress(mp.getCurrentPosition());

					if (mp.isPlaying()) {
						Runnable notification = new Runnable() {
							public void run() {
								startPlayProgressUpdater();
							}
						};
						audioPlayHandler.postDelayed(notification, 500);
					} else {
						mp.pause();
						// seek.setProgress(0);
					}
				}
			}
		});

		btnAudioStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// if (btnAudioStop.getText().equals("Stop")) {
				// try {
				//
				// tblAudio.removeView(llayProgress);
				// btnAudioPlay.setText("Play");
				// btnAudioStop.setText(" Stop");
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// } else if (btnAudioStop.getText().equals(" Stop")) {
				// try {
				// mp.stop();
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				//
				// }

				try {

					if (mp.isPlaying()) {
						mp.stop();

					}
					Log.i("thread", "@@@@@@@@@@@ next clicked");

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		trControls.addView(btnAudioPlay);
		trControls.addView(btnAudioStop);
		tblAudio.addView(trControls);
		llayAudioPlayer.addView(llaySoundControl);
		llayAudioPlayer.addView(tblAudio);
		llayAudioPlayer.addView(seek);

		audioPlayHandler = new Handler() {/*
										 * @Override public void
										 * handleMessage(Message msg) {
										 * super.handleMessage(msg);
										 * 
										 * if (msg.what == 1) { // process to
										 * pause btnAudioPlay.setText("play");
										 * if(mp!=null) { if(mp.isPlaying()) {
										 * mp.pause(); } }
										 * 
										 * } else if (msg.what == 2) { //
										 * process to resume
										 * 
										 * String strPlayStatus =
										 * btnAudioPlay.getText().toString();
										 * 
										 * if
										 * (strPlayStatus.equalsIgnoreCase("play"
										 * )) { btnAudioPlay.setText("pause");
										 * llaySoundControl
										 * .setVisibility(View.VISIBLE);
										 * seek.setVisibility(View.VISIBLE);
										 * 
										 * try { mp.start();
										 * seek.setMax(mp.getDuration());
										 * startPlayProgressUpdater(); } catch
										 * (Exception e) {
										 * 
										 * } } else if
										 * (strPlayStatus.equalsIgnoreCase
										 * ("pause")) {
										 * btnAudioPlay.setText("play");
										 * mp.pause(); }
										 * 
										 * }
										 * 
										 * startPlayProgressUpdater();
										 * 
										 * }
										 * 
										 * private void
										 * startPlayProgressUpdater() {
										 * seek.setProgress
										 * (mp.getCurrentPosition());
										 * 
										 * if (mp.isPlaying()) { Runnable
										 * notification = new Runnable() {
										 * public void run() {
										 * startPlayProgressUpdater(); } };
										 * audioPlayHandler
										 * .postDelayed(notification, 500); }
										 * else { mp.pause();
										 * seek.setProgress(0); } }
										 */
		};
		setContentView(llayAudioPlayer);
		File CheckFile = new File(component.getContentPath());
		if (CheckFile.exists()) {
			btnAudioPlay.setText("play");
			final String strPlayStatus = btnAudioPlay.getText().toString();

			audioPlayHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					if (strPlayStatus.equalsIgnoreCase("play")) {
						btnAudioPlay.setText("pause");
						llaySoundControl.setVisibility(View.VISIBLE);
						seek.setVisibility(View.VISIBLE);

						try {

							mp.start();
							seek.setMax(mp.getDuration());

							audioPlayHandler.post(new Runnable() {

								@Override
								public void run() {
									startPlayProgressUpdater();

								}

								private void startPlayProgressUpdater() {
									if (mp != null) {
										seek.setProgress(mp
												.getCurrentPosition());

										if (mp.isPlaying()) {
											Runnable notification = new Runnable() {
												public void run() {
													startPlayProgressUpdater();
												}
											};
											audioPlayHandler.postDelayed(
													notification, 500);
										} else {
											mp.pause();
											seek.setProgress(0);
										}
									}
								}
							});

						} catch (Exception e) {

						}
					} else if (strPlayStatus.equalsIgnoreCase("pause")) {
						btnAudioPlay.setText("play");
						mp.pause();
					}
				}
			}, 1000);

		} else {
			Toast.makeText(context, "Audio File is not available",
					Toast.LENGTH_SHORT).show();
		}

	}

	private LinearLayout.LayoutParams paramsNotFullscreen; // if you're using
															// RelativeLatout

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) // To
																			// fullscreen
		{
			paramsNotFullscreen = (LinearLayout.LayoutParams) videoView
					.getLayoutParams();
			RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) new LayoutParams(
					paramsNotFullscreen);
			params.setMargins(0, 0, 0, 0);
			params.height = ViewGroup.LayoutParams.MATCH_PARENT;
			params.width = ViewGroup.LayoutParams.MATCH_PARENT;
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			videoView.setLayoutParams(params);

		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			videoView.setLayoutParams(paramsNotFullscreen);
		}
	}

	private void startVideoPlayer(final Components component) {
		LinearLayout llayVideoPlayer = new LinearLayout(context);
		llayVideoPlayer.setId(1);
		llayVideoPlayer.setOrientation(LinearLayout.VERTICAL);
		llayVideoPlayer.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		TextView tvSpace = new TextView(context);
		tvSpace.setHeight(20);
		llayVideoPlayer.addView(tvSpace);

		final LinearLayout llayVideo = new LinearLayout(context);
		llayVideo.setOrientation(LinearLayout.VERTICAL);

		llayVideo.setGravity(Gravity.CENTER);

		final File vfileCheck = new File(component.getContentPath());
		Bitmap bitmapThumb = null;
		String bitmapImage = component.getContentPath();
		bitmapImage = bitmapImage.replace("mp4", "jpg");
		final File fileCheckV = new File(bitmapImage);

		// final ImageView ivThunb = new ImageView(context);
		// if (fileCheckV.exists())
		// bitmapThumb = TodayList2.ResizeImage(component.getContentPath()
		// + ".jpg", 300);

		final TableLayout tblControl = new TableLayout(context);
		tblControl.setVisibility(View.INVISIBLE);

		if (vfileCheck.exists()) {

			// ivThunb.setImageBitmap(bitmapThumb);
			videoView = new VideoView(context);

			audioPlayHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (videoView.isPlaying()) {
						videoView.stopPlayback();
					}
					super.handleMessage(msg);
				}
			};

			// tblControl.removeAllViews();
			tblControl.setVisibility(View.VISIBLE);

			llayVideo.addView(videoView, 0);
			// llayVideo.removeView(ivThunb);
			videoView.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));

			// videoView.setMinimumHeight(300);
			// videoView.setMinimumWidth(300);
			// getWindow().clearFlags(WindowManager
			// .LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			// WindowManager.LayoutParams.FLAG_FULLSCREEN);
			videoView.setVideoPath(component.getContentPath());
			videoView.setZOrderOnTop(true);
			videoView.requestFocus();
			videoView.start();

			videoView.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					try {
						/* ValueHandler.llAutoPlayContent.removeFirst(); */

						initiatePlayer();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
			TableRow tr = new TableRow(context);
			tr.setGravity(Gravity.CENTER);

			final Button btnPlay = new Button(context);
			// btnPlay.setHint("Play");
			btnPlay.setBackgroundResource(R.drawable.v_pause);
			btnPlay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String hint = btnPlay.getHint().toString();
					if (hint.equals("play")) {
						videoView.pause();
						btnPlay.setHint("pause");
						btnPlay.setBackgroundResource(R.drawable.v_play);
					} else {
						videoView.start();
						btnPlay.setBackgroundResource(R.drawable.v_pause);
						btnPlay.setHint("play");
					}
				}
			});

			Button btnStopVideo = new Button(context);
			btnStopVideo.setBackgroundResource(R.drawable.stop_select);
			btnStopVideo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					videoView.stopPlayback();
					try {
						/* ValueHandler.llAutoPlayContent.removeFirst(); */
						if (videoView.isPlaying()) {
							videoView.stopPlayback();

						}
						initiatePlayer();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			ivThunb = new ImageView(context);

			ivThunb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					try {
						// llayVideo.removeView(tblControl);
						tblControl.setVisibility(View.VISIBLE);
						llayVideo.removeView(ivThunb);
						llayVideo.addView(videoView, 0);
					} catch (Exception e) {
					}

				}
			});
			Button btnFull = new Button(context);
			btnFull.setBackgroundResource(R.drawable.full_screen);
			btnFull.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intentVPlayer = new Intent(context,
							VideoPlayer.class);
//					intentVPlayer.putExtra("File_Path",
//							component.getContentPath() + ".mp4");
//					intentVPlayer.putExtra("Player_Type", "Video Player");
					intentVPlayer.putExtra("video", component.getContentPath() + ".mp4");
					startActivity(intentVPlayer);

					Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(
							component.getContentPath() + ".mp4",
							MediaStore.Images.Thumbnails.MINI_KIND);
					BitmapDrawable bitmapDrawable = new BitmapDrawable(
							thumbnail);

					videoView.setBackgroundDrawable(bitmapDrawable);
					Bitmap bitmapThumb = null;

					ivThunb.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					bitmapThumb = callDisp.ResizeImage(component.getContentPath() + ".jpg");
					ivThunb.setImageBitmap(bitmapThumb);

					try {
						// llayVideo.removeView(tblControl);
						tblControl.setVisibility(View.INVISIBLE);
						llayVideo.removeView(videoView);
						llayVideo.addView(ivThunb, 0);
					} catch (Exception e) {
					}
				}
			});
			tr.addView(btnPlay);
			tr.addView(btnStopVideo);
			tr.addView(btnFull);
			tblControl.addView(tr);
			// tblControl.setVisibility(View.INVISIBLE);
			// llayVideo.addView(tblControl, 1);

			// llayVideo.addView(ivThunb);
			llayVideo.addView(tblControl, 1);
			llayVideoPlayer.addView(llayVideo);
			Log.i("Note", "Path:" + component.getContentPath() + ".mp4");
			setContentView(llayVideoPlayer);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		Log.i("thread", ".................came to onpause method");
		/*
		 * if (audioPlayHandler != null) { audioPlayHandler.sendEmptyMessage(1);
		 * }
		 */

		if (mp != null) {
			if (mp.isPlaying()) {
				mp.pause();

			}

		}
		if (videoView != null) {
			if (videoView.isPlaying()) {
				videoView.stopPlayback();
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;
		if (audioPlayHandler != null) {
			audioPlayHandler.sendEmptyMessage(2);
		}
	}

	public void stopPlayback() {
		Log.i("thread", ".................came to destroy method");
		if (mp != null) {

			mp.stop();
			mp.release();
			mp = null;

		}
		if (videoView != null) {
			if (videoView.isPlaying()) {
				videoView.stopPlayback();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.i("thread", ".................came to destroy method");
		if (mp != null) {

			mp.stop();
			mp.release();
			mp = null;

		}
		if (videoView != null) {
			if (videoView.isPlaying()) {
				videoView.stopPlayback();
			}
		}

		if (WebServiceReferences.contextTable.containsKey("auto_play")) {
			WebServiceReferences.contextTable.remove("auto_play");
		}

	}

}
