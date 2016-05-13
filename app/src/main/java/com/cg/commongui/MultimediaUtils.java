package com.cg.commongui;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.ComponentCreator;
import com.cg.instancemessage.NotePickerScreen;
import com.crypto.AESFileCrypto;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class MultimediaUtils extends Activity {

	private String filePath = null;

	private int requestCode = 0;

	private String action = null;

	private String createOrOpen = null;

	private Context cntx = null;

	private Context context = null;

	private MediaRecorder mRecorder = null;

	private MediaPlayer mPlayer = null;

	private LinearLayout audioLayout = null;

	private Button recordAudio = null;
	private Button recordback=null;

	private Handler handler = null;

	private Button saveAudio = null;

	private boolean mStartRecording = true;

	private boolean mStartPlaying = false;

	private Handler handlerSeek = null;

	private LinearLayout micLayout = null;

	private SeekBar seekProgress = null;

	private TextView fileName = null;

	private Runnable notification = null;

	private boolean isAudioRecording = false;

	private LinearLayout linear = null;

	private Chronometer chronometer = null;
	CountDownTimer timer ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			context = this;
			if(SingleInstance.mainContext.getResources()
					.getString(R.string.screenshot).equalsIgnoreCase(SingleInstance.mainContext.getResources()
							.getString(R.string.yes))){
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
			}
			this.setFinishOnTouchOutside(false);
			filePath = getIntent().getStringExtra("filePath");
			requestCode = getIntent().getIntExtra("requestCode", 0);
			action = getIntent().getStringExtra("action");
			boolean isAudio = getIntent().getBooleanExtra("isAudio", false);
			createOrOpen = getIntent().getStringExtra("createOrOpen");
			WebServiceReferences.contextTable.put("multimediautils", context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.multimediautils);
			audioLayout = (LinearLayout) findViewById(R.id.audio_layout);
			recordAudio = (Button) findViewById(R.id.record_audio);
			recordback=(Button) findViewById(R.id.record_back);
			saveAudio = (Button) findViewById(R.id.save_audio);
			micLayout = (LinearLayout) findViewById(R.id.mic);
			seekProgress = (SeekBar) findViewById(R.id.seekBar1);
			fileName = (TextView) findViewById(R.id.filename);
			chronometer = (Chronometer) findViewById(R.id.chronometer1);
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			amanager = (AudioManager) this
					.getSystemService(Context.AUDIO_SERVICE);
			cntx = getCurrentContext();
			if (createOrOpen.equalsIgnoreCase("create")) {
				if (!action.equalsIgnoreCase("audio") || isAudio) {
					Log.i("result", "--request code--" + requestCode);
					capturePhotoVideo(filePath, cntx, requestCode, action);
				} else {
					audioLayout.setVisibility(View.VISIBLE);
				}

			} else if (createOrOpen.equalsIgnoreCase("open")) {
				if (action.equalsIgnoreCase("audio") || isAudio) {
					linear = AudioNoteView(3, filePath);

				}

			}

			recordAudio.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						onRecord(mStartRecording, filePath);
						// if (mStartRecording) {
						// recordAudio.setText("Stop");
						// } else {
						if (mStartRecording) {
							recordAudio.setText("Start");
							recordAudio.setVisibility(View.GONE);
							saveAudio.setVisibility(View.VISIBLE);

						}
						mStartRecording = false;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			});
			recordback.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						if (recordAudio.getText().toString().equalsIgnoreCase("stop")
								|| saveAudio.getVisibility() == View.VISIBLE) {
							stopRecording();
							showSaveAlert();
						} else if (linear != null) {
							if (mPlayer != null && mPlayer.isPlaying()) {
								stopPlaying();
							}
							finish();
						} else if (recordAudio.getText().toString()
								.equalsIgnoreCase("record")) {
							if (mPlayer != null && mPlayer.isPlaying()) {
								stopPlaying();
							}

							if (WebServiceReferences.contextTable
									.containsKey("Component")) {
								ComponentCreator comp = (ComponentCreator) WebServiceReferences.contextTable
										.get("Component");
								CallDispatcher.fromMultimediaUtils = true;
								if (comp.send) {
									comp.finish();
								}

							}

							finish();

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			});
			saveAudio.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (!mStartPlaying && !mStartRecording) {
							timer.cancel ();
							capturePhotoVideo(filePath, cntx, requestCode,
									action);
						}
						finish();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void showElapsedTime() {
		
	    long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();      
	    Log.d("Test","Time###########"+elapsedMillis);
	    if(elapsedMillis>60000){
	    	chronometer.stop();
	    }
//	    Toast.makeText(ChronoExample.this, "Elapsed milliseconds: " + elapsedMillis, 
//	            Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (WebServiceReferences.contextTable.containsKey("multimediautils"))
			WebServiceReferences.contextTable.remove("multimediautils");
		super.onDestroy();
	}

	public void capturePhotoVideo(String filePath, Context con,
			int requestCode, String action) {

		try {
			Log.i("result", "capture mahtod" + requestCode + action);
			Intent i = new Intent(action);
			if (!action.equalsIgnoreCase("audio")) {
				Log.i("result", "------if------" + filePath + "---" + con
						+ "----" + requestCode + "---" + action);
				if (action.equalsIgnoreCase(MediaStore.ACTION_VIDEO_CAPTURE)) {
					if (!filePath.endsWith(".mp4"))
						filePath = filePath + ".mp4";
					/*
					 * Starts from API level 8
					 */
					i.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
					i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,
							CamcorderProfile.QUALITY_LOW);
				} else if (action
						.equalsIgnoreCase(MediaStore.ACTION_IMAGE_CAPTURE)) {
					Log.i("result", "-----else if------");
				}
			} else {
				Log.i("result", "------else------");
				stopRecording();
			}

			Uri imageUri = Uri.fromFile(new File(filePath));
			Log.i("group123", "filepath : " + filePath);
			Log.i("group123", "from imageuri" + imageUri.getPath());
			i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			i.putExtra("file", filePath);
			((Activity) con).startActivityForResult(i, requestCode);
			Log.i("result", "*******************" + requestCode);
			finish();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Context getCurrentContext() {

		try {
			if (WebServiceReferences.contextTable.containsKey("auto_play"))
				cntx = WebServiceReferences.contextTable.get("auto_play");
			if (WebServiceReferences.contextTable.containsKey("Component"))
				cntx = WebServiceReferences.contextTable.get("Component");
			else if (WebServiceReferences.contextTable
					.containsKey("buscard"))
				cntx = WebServiceReferences.contextTable.get("buscard");
			else if (WebServiceReferences.contextTable
					.containsKey("utilitybuyer"))
				cntx = WebServiceReferences.contextTable.get("utilitybuyer");
			else if (WebServiceReferences.contextTable
					.containsKey("utilityseller"))
				cntx = WebServiceReferences.contextTable.get("utilityseller");
			else if (WebServiceReferences.contextTable
					.containsKey("utilityneeder"))
				cntx = WebServiceReferences.contextTable.get("utilityneeder");
			else if (WebServiceReferences.contextTable
					.containsKey("utilityprovider"))
				cntx = WebServiceReferences.contextTable.get("utilityprovider");
			else if (WebServiceReferences.contextTable.containsKey("clone"))
				cntx = WebServiceReferences.contextTable.get("clone");
			else if (WebServiceReferences.contextTable
					.containsKey("answeringmachine"))
				cntx = WebServiceReferences.contextTable
						.get("answeringmachine");
			else if (WebServiceReferences.contextTable.containsKey("formdesc"))
				cntx = WebServiceReferences.contextTable.get("formdesc");
			else if (WebServiceReferences.contextTable
					.containsKey("cmpinstruction"))
				cntx = WebServiceReferences.contextTable.get("cmpinstruction");
			else if (WebServiceReferences.contextTable
					.containsKey("frmreccreator"))
				cntx = WebServiceReferences.contextTable.get("frmreccreator");
			else if (WebServiceReferences.contextTable.containsKey("imscreen"))
				cntx = WebServiceReferences.contextTable.get("imscreen");
			else if (WebServiceReferences.contextTable.containsKey("menupage"))
				cntx = WebServiceReferences.contextTable.get("menupage");
			else if (WebServiceReferences.contextTable.containsKey("avatarset"))
				cntx = WebServiceReferences.contextTable.get("avatarset");
			else if (SingleInstance.contextTable.containsKey("chatactivity"))
				cntx = SingleInstance.contextTable.get("chatactivity");
			else if (SingleInstance.contextTable.containsKey("groupchat"))
				cntx = SingleInstance.contextTable.get("groupchat");
			else if (SingleInstance.contextTable.containsKey("profile"))
				cntx = SingleInstance.contextTable.get("profile");
			else if (SingleInstance.contextTable.containsKey("settings"))
				cntx = SingleInstance.contextTable.get("settings");
			else if (SingleInstance.contextTable.containsKey("MAIN"))
				cntx = SingleInstance.contextTable.get("MAIN");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cntx;
	}

	private void onRecord(boolean start, String fileName) {
		try {
			if (start) {
				startRecording(fileName);
				showElapsedTime();
			} else {
				stopRecording();
				showElapsedTime();
				chronometer.stop();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void onPlay(boolean start, String fileName) {

		try {
			if (start) {
				startPlaying(fileName);
			} else {
				stopPlaying();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startPlaying(String mFileName) {
		try {
			mPlayer = new MediaPlayer();
			try {
				mPlayer.setDataSource(mFileName);
				mPlayer.prepare();
				mPlayer.start();
			} catch (IOException e) {
				Log.e("multimedia", "prepare() failed");
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void stopPlaying() {
		try {
			if (mPlayer != null) {
				mPlayer.release();
				mPlayer = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startRecording(String mFileName) {
		chronometer.setBase(SystemClock.elapsedRealtime());
		chronometer.start();
		isAudioRecording = true;
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setMaxDuration(60000);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

		try {
			mRecorder.prepare();
			timer= new CountDownTimer(60000, 1000) {

			     public void onTick(long millisUntilFinished) {
//			         mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
			     }

			     public void onFinish() {
						stopRecording();
						if (!mStartPlaying && !mStartRecording) {
							capturePhotoVideo(filePath, cntx, requestCode,
									action);
						}
						finish();


//			         mTextField.setText("done!");
			     }
			  };
			timer.start();
		} catch (IOException e) {
			isAudioRecording = false;
		}

		mRecorder.start();
	}

	private void stopRecording() {
		try {
			showElapsedTime();
			isAudioRecording = false;
			if (mRecorder != null) {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (mRecorder != null) {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
			}
		}
	}

	Button btnPause = null;

	Button btnStop = null;

	LinearLayout llAudio = null;

	LinearLayout llaySoundControl = null;

	private AudioManager amanager = null;

	private LinearLayout AudioNoteView(int state, String filePath)
			throws Exception {

  // start 07-10-15 changes

		final String AudioPath = AESFileCrypto.decryptFile(context,filePath);

		// ended 07-10-15 changes

		handlerSeek = new Handler();
		llAudio = new LinearLayout(context);
		try {
			final int PRE_PLAY = 3;
			mPlayer = new MediaPlayer();
			llAudio.setOrientation(LinearLayout.VERTICAL);
			llAudio.setBackgroundResource(R.color.grey_light_bg);
			llAudio.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			seekProgress.setVisibility(View.INVISIBLE);
			llaySoundControl = new LinearLayout(context);
			llaySoundControl.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			llaySoundControl.setPadding(10, 10, 0, 10);
			llaySoundControl.setVisibility(View.INVISIBLE);
			llAudio.addView(llaySoundControl);
			final LinearLayout llayTimer = new LinearLayout(context);
			llayTimer.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			final TextView tvTimer = new TextView(context);
			tvTimer.setWidth(200);
			tvTimer.setText("  00:00:00");
			tvTimer.setTextColor(Color.BLACK);
			tvTimer.setVisibility(View.INVISIBLE);
			tvTimer.setGravity(Gravity.CENTER_HORIZONTAL);
			final Button btnAudio = new Button(context);
			final Chronometer stopWatch = new Chronometer(context);
			stopWatch.setText("  00:00:00");
			stopWatch
					.setOnChronometerTickListener(new OnChronometerTickListener() {
						@Override
						public void onChronometerTick(Chronometer arg0) {
							Log.d("lg", "Stop watch.....");
							CharSequence text = stopWatch.getText();
							if (text.length() == 5) {
								stopWatch.setText("  00:" + text);
							} else if (text.length() == 7) {
								stopWatch.setText("  0" + text);
							}
							tvTimer.setText(stopWatch.getText().toString());
							Log.i("time", "===> "
									+ stopWatch.getText().toString());
							 long elapsedMillis = SystemClock.elapsedRealtime() - stopWatch.getBase();
							 Log.i("time","elaspedTime"+elapsedMillis);
							if (stopWatch.getText().toString()
									.equalsIgnoreCase("00:01:00")
									|| stopWatch.getText().toString()
											.equalsIgnoreCase("1:00")|| stopWatch.getText().toString()
											.equalsIgnoreCase("01:00")) {
								stopWatch.stop();
								WebServiceReferences.isRecordinginProgress = false;
								btnAudio.setHint(Integer.toString(PRE_PLAY));
								btnAudio.setBackgroundResource(R.drawable.btn_play_new);
							}
						}
					});
			stopWatch.setVisibility(View.GONE);
			llayTimer.addView(tvTimer);
			// llayTimer.setPadding(20, 20, 0, 20);
			llayTimer.addView(stopWatch);
			llayTimer.setGravity(Gravity.CENTER);
			llAudio.addView(llayTimer);

			final TableLayout tblControls = new TableLayout(context);
			final TableRow trControls = new TableRow(context);
			trControls.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			btnPause = new Button(context);
			btnPause.setBackgroundResource(R.drawable.btn_pause_new);
			btnPause.setHint("play");
			btnPause.setHintTextColor(Color.TRANSPARENT);
			trControls.addView(btnPause);
			btnStop = new Button(context);
			btnStop.setBackgroundResource(R.drawable.btn_stop_new);
			trControls.addView(btnStop);
			final LinearLayout llayRecord = new LinearLayout(context);
			llayRecord.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			llayRecord.setGravity(Gravity.CENTER);

			btnAudio.setHint(Integer.toString(state));
			btnAudio.setGravity(Gravity.CENTER);
			btnAudio.setHintTextColor(Color.TRANSPARENT);
			btnAudio.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			if (state == PRE_PLAY) {
				tvTimer.setVisibility(View.VISIBLE);
				mPlayer.reset();
				try {
					Log.i("multi123", "audiopath : " + AudioPath);
					mPlayer.setDataSource(AudioPath);
					mPlayer.setLooping(false);
					mPlayer.prepare();
					play(tvTimer, AudioPath, llAudio, llayRecord,
							llaySoundControl, tblControls);
					btnAudio.setVisibility(View.GONE);

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
				btnAudio.setBackgroundResource(R.drawable.btn_play_new);

			}

			mPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					try {
						Log.e("player", "mplayer prepared");
						long milliseconds = mp.getDuration();

						String seconds = WebServiceReferences.setLength2((int) (Math
								.round((double) milliseconds / 1000) % 60));
						Log.e("player", "mplayer prepared" + seconds);
						String minutes = WebServiceReferences
								.setLength2((int) ((milliseconds / (1000 * 60)) % 60));
						Log.e("player", "mplayer prepared" + minutes);
						String hours = WebServiceReferences
								.setLength2((int) ((milliseconds / (1000 * 60 * 60)) % 24));
						Log.e("player", "mplayer prepared" + hours);
						String asText = hours + ":" + minutes + ":" + seconds;
						tvTimer.setText("  " + asText);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			btnAudio.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						Log.d("lg", "button hint" + btnAudio.getHint());
						switch (Integer.parseInt((String) btnAudio.getHint())) {
						case PRE_PLAY:
							play(tvTimer, AudioPath, llAudio, llayRecord,
									llaySoundControl, tblControls);
							break;
						default:
							break;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			});

			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					Log.d("zxz", "audioplayerha");

					if (msg.arg1 == 430) {
						Log.d("zxz", "420");
						if (mPlayer.isPlaying()) {
							Log.d("zxz", "420 if");
							mPlayer.stop();
							try {
								mPlayer.reset();
								mPlayer.setDataSource(AudioPath);
								mPlayer.setLooping(false);
								mPlayer.prepare();
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						try {
							btnPause.setHint("play");
							btnPause.setBackgroundResource(R.drawable.btn_pause_new);

							llAudio.removeView(tblControls);
							llAudio.addView(llayRecord, 2);

							llaySoundControl.setVisibility(View.INVISIBLE);
							seekProgress.setVisibility(View.INVISIBLE);
						} catch (Exception e) {

						}
					}

					super.handleMessage(msg);
				}
			};

			btnPause.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (btnPause.getHint().toString().equals("play")) {
						mPlayer.pause();
						btnPause.setHint("pause");
						btnPause.setBackgroundResource(R.drawable.btn_play_new);
					} else if (btnPause.getHint().toString().equals("pause")) {
						mPlayer.start();
						try {
							startPlayProgressUpdater();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						btnPause.setHint("play");
						btnPause.setBackgroundResource(R.drawable.btn_pause_new);
					}

				}

				private void startPlayProgressUpdater() throws Exception {
					seekProgress.setProgress(mPlayer.getCurrentPosition());

					if (mPlayer.isPlaying()) {


						Runnable notification = new Runnable() {
							public void run() {
								try {

									Log.d("lg", "play progress().....");
									long milliseconds = mPlayer
											.getCurrentPosition();
									String seconds = WebServiceReferences.setLength2((int) (Math
											.round((double) milliseconds / 1000) % 60));
									String minutes = WebServiceReferences
											.setLength2((int) ((milliseconds / (1000 * 60)) % 60));
									String hours = WebServiceReferences
											.setLength2((int) ((milliseconds / (1000 * 60 * 60)) % 24));

									String asText = hours + ":" + minutes + ":"
											+ seconds;
									tvTimer.setText("  " + asText);

									startPlayProgressUpdater();
								} catch (Exception e) {
									// Log.e("Audio",
									// "Exception:" + e.getMessage());
									e.printStackTrace();
								}
							}
						};
						handlerSeek.postDelayed(notification, 500);
					} else {
						seekProgress.setProgress(mPlayer.getCurrentPosition());
						mPlayer.pause();

					}

				}
			});

			btnStop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						if (mPlayer != null
								&& mPlayer.isPlaying()
								|| btnPause.getHint().toString()
										.equals("pause")) {
							mPlayer.stop();
							try {
								mPlayer.reset();
								mPlayer.setDataSource(AudioPath);
								mPlayer.setLooping(false);
								mPlayer.prepare();
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						btnPause.setHint("play");
						btnPause.setBackgroundResource(R.drawable.btn_pause_new);
						llAudio.removeView(tblControls);
						llAudio.addView(llayRecord, 2);
						llaySoundControl.setVisibility(View.INVISIBLE);
						seekProgress.setVisibility(View.INVISIBLE);
					} catch (Exception e) {

					}
				}
			});

			mPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					try {
						mPlayer.reset();
						mPlayer.setDataSource(AudioPath);
						mPlayer.setLooping(false);
						mPlayer.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
						btnAudio.setVisibility(View.VISIBLE);
						llAudio.removeView(tblControls);

						llAudio.addView(llayRecord, 2);

						llaySoundControl.setVisibility(View.INVISIBLE);
						seekProgress.setVisibility(View.INVISIBLE);
						btnPause.setHint("play");
						btnPause.setBackgroundResource(R.drawable.btn_pause_new);

					} catch (Exception e) {

					}
				}

			});

			trControls.setGravity(Gravity.CENTER);
			trControls.setPadding(0, 10, 0, 20);
			tblControls.addView(trControls);
			llayRecord.addView(btnAudio);
			llAudio.addView(llayRecord);
			audioLayout.removeAllViews();
			audioLayout.addView(llAudio);
			audioLayout.setVisibility(View.VISIBLE);
			micLayout.setVisibility(View.GONE);
			// fileName.setVisibility(View.VISIBLE);
			String file = "";
			if (AudioPath.contains("/sdcard/COMMedia/"))
				file = AudioPath.replace("/sdcard/COMMedia/", "");
			else
				file = AudioPath;

			// fileName.setText("File Name : " + file);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return llAudio;
	}
	public void StopAudioPlay()
	{
		if (mPlayer.isPlaying())
		{
			mPlayer.stop();
		}
	}

	private void play(TextView tvTimer, String AudioPath, LinearLayout llAudio,
			LinearLayout llayRecord, LinearLayout llaySoundControl,
			TableLayout tblControls) {

		tvTimer.setVisibility(View.VISIBLE);

		try {
			if (!mPlayer.isPlaying()) {
				Log.e("player", "not playing");
				mPlayer.reset();
				mPlayer.setDataSource(AudioPath);
				mPlayer.setLooping(false);
				mPlayer.prepare();
				mPlayer.start();
				Log.e("player", "not playing" + mPlayer.getDuration());
				seekProgress.setMax(mPlayer.getDuration());
				startPlayProgressUpdater(tvTimer);
			} else {
				mPlayer.reset();
				mPlayer.start();
				Log.d("lg", "pre play....");
				Log.d("lg", "pre play...." + mPlayer.getDuration());
				seekProgress.setMax(mPlayer.getDuration());
				startPlayProgressUpdater(tvTimer);
			}

			Log.d("Audio", "Audio Time:" + mPlayer.getDuration());

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			llAudio.removeView(llayRecord);
			llAudio.addView(tblControls, 2);
			llaySoundControl.setVisibility(View.VISIBLE);
			seekProgress.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startPlayProgressUpdater(final TextView tvTimer) {
		try {
			seekProgress.setProgress(mPlayer.getCurrentPosition());
			Log.d("player", "?????????????????????" + mPlayer.isPlaying());
			if (mPlayer.isPlaying()) {
				notification = new Runnable() {
					public void run() {
						try {

							long milliseconds = mPlayer.getCurrentPosition();
							String seconds = WebServiceReferences
									.setLength2((int) (Math
											.round((double) milliseconds / 1000) % 60));
							String minutes = WebServiceReferences
									.setLength2((int) ((milliseconds / (1000 * 60)) % 60));
							String hours = WebServiceReferences
									.setLength2((int) ((milliseconds / (1000 * 60 * 60)) % 24));

							String asText = hours + ":" + minutes + ":"
									+ seconds;
							tvTimer.setText("  " + asText);

							startPlayProgressUpdater(tvTimer);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				handlerSeek.postDelayed(notification, 500);
			} else {
				seekProgress.setProgress(mPlayer.getCurrentPosition());
				mPlayer.pause();

			}
			seekProgress
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub
							handlerSeek.removeCallbacks(notification);
							int currentPosition = (seekBar.getProgress());

							// forward or backward to certain seconds
							mPlayer.seekTo(currentPosition);

							// update timer progress again
							startPlayProgressUpdater(tvTimer);
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub
							handlerSeek.removeCallbacks(notification);
						}

						@Override
						public void onProgressChanged(SeekBar seekBar,
								int progress, boolean fromUser) {
							// TODO Auto-generated method stub

						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int progressToTimer(int progress, int totalDuration) {
		try {
			int currentDuration = 0;
			totalDuration = (int) (totalDuration / 1000);
			currentDuration = (int) ((((double) progress) / 100) * totalDuration);

			// return current duration in milliseconds
			return currentDuration * 1000;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			// TODO Auto-generated method stub
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				if (recordAudio.getText().toString().equalsIgnoreCase("stop")
						|| saveAudio.getVisibility() == View.VISIBLE) {
					stopRecording();
					showSaveAlert();
				} else if (linear != null) {
					if (mPlayer != null && mPlayer.isPlaying()) {
						stopPlaying();
					}
					finish();
				} else if (recordAudio.getText().toString()
						.equalsIgnoreCase("record")) {
					if (mPlayer != null && mPlayer.isPlaying()) {
						stopPlaying();
					}

					if (WebServiceReferences.contextTable
							.containsKey("Component")) {
						ComponentCreator comp = (ComponentCreator) WebServiceReferences.contextTable
								.get("Component");
						CallDispatcher.fromMultimediaUtils = true;
						if (comp.send) {
							comp.finish();
						}

					}

					finish();

				}
			}
			if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
				// Do something
				Log.i("volume", "decrease.");
				amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
				// return false;
				Log.i("volume", "increase.");
				amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showToast(String message) {
		try {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showSaveAlert() {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("Are you sure want to Save and Go Back?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									if (!mStartPlaying && !mStartRecording) {
										capturePhotoVideo(filePath, cntx,
												requestCode, action);
									}
									finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									if (WebServiceReferences.contextTable
											.containsKey("Component")) {
										ComponentCreator componentCreator = (ComponentCreator) WebServiceReferences.contextTable
												.get("Component");
										boolean componentScreenExists = false;
										if (WebServiceReferences.contextTable
												.containsKey("sharenotepicker")) {

											componentScreenExists = true;
										}
										if (WebServiceReferences.contextTable
												.containsKey("notepicker")) {
											((NotePickerScreen) WebServiceReferences.contextTable
													.get("notepicker"))
													.refreshList();
											componentScreenExists = true;
										}
										if (componentCreator.send
												|| componentScreenExists) {
											componentCreator.finish();
										}
									}
									finish();

								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		} catch (Exception e) {

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#dispatchKeyEvent(android.view.KeyEvent)
	 */
	// @Override
	// public boolean dispatchKeyEvent(KeyEvent event) {
	// try {
	// // TODO Auto-generated method stub
	// int action = event.getAction();
	// int keyCode = event.getKeyCode();
	// switch (keyCode) {
	// case KeyEvent.KEYCODE_VOLUME_UP:
	// if (action == KeyEvent.ACTION_DOWN) {
	//
	// }
	// return true;
	// case KeyEvent.KEYCODE_VOLUME_DOWN:
	// if (action == KeyEvent.ACTION_DOWN) {
	// // TODO
	// }
	// return true;
	// default:
	// }
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	//
	// }
	// return super.dispatchKeyEvent(event);
	// }

	public void notifyGSMCallAccepted() {
		if (mRecorder != null) {
			if (isAudioRecording) {
				recordAudio.setText("Start");
				stopRecording();
			}
		}
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.pause();
			btnPause.setHint("pause");
			btnPause.setBackgroundResource(R.drawable.btn_play_new);
		}
	}
}
