package com.cg.timer;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.TextNoteDatas;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.PhotoZoomActivity;
import com.cg.files.CompleteListView;
import com.cg.files.Components;
import com.main.AppMainActivity;
import com.util.SingleInstance;
import com.util.VideoPlayer;

public class ReminderManager extends Activity {

	ImageView btnBack, btnFward, btnRClose;
	TextView tvTitle;
	private LinearLayout llayContent;
	private Context context;
	Components component = null;
	private Handler audioPlayHandler = new Handler();
	private Handler videoPlayerHandler = new Handler();
	boolean text = false;
	private String[] m_type = { "Text", "Photo", "Audio", "Video" };
	private AlertDialog msg_dialog = null;
	private CallDispatcher callDisp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			KeyguardManager mKeyGuardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
			KeyguardLock mLock = mKeyGuardManager
					.newKeyguardLock("ReminderManager");
			mLock.disableKeyguard();
			((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
					PowerManager.SCREEN_DIM_WAKE_LOCK, "TAG").acquire();

			setContentView(R.layout.more_reminder);
			component = (Components) getIntent().getSerializableExtra("component");
			context = this;
			WebServiceReferences.contextTable.put("reminderactivity", context);
			llayContent = (LinearLayout) findViewById(R.id.more_rem_content);
			tvTitle = (TextView) findViewById(R.id.Rem_Title);
			btnBack = (ImageView) findViewById(R.id.back);
			btnBack.setVisibility(View.GONE);
			btnFward = (ImageView) findViewById(R.id.forward);
			btnFward.setVisibility(View.GONE);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(context);

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);
			displaymetrics = null;
			if (CompleteListView.textnotes == null)
				CompleteListView.textnotes = new TextNoteDatas();

			btnRClose = (ImageView) findViewById(R.id.rem_close);
			WebServiceReferences.isReminderOpened = true;

			btnRClose.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub

					audioPlayHandler.sendEmptyMessage(1);
					videoPlayerHandler.sendEmptyMessage(1);

					if ((component.getreminderresponsetype().trim().length() != 0)
							&& (component.getViewMode() == 1)) {
						if (WebServiceReferences.contextTable
								.containsKey("Component")) {
							// ComponentCreator creator = (ComponentCreator)
							// WebServiceReferences.contextTable
							// .get("Component");
							// creator.changeReminderStatus(component.getComponentId());
						}

						AlertDialog.Builder buider = new AlertDialog.Builder(
								context);
						buider.setMessage(
								"This note hHaving response type,Do you want to send respose now or later?")
								.setPositiveButton("Respond Now",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method stub
												// showResponseDialog(component);

											}
										})
								.setNegativeButton("Later",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method stub
												dialog.cancel();
												finish();
											}
										});
						AlertDialog alert = buider.create();
						alert.show();

					} else {
						if (WebServiceReferences.contextTable
								.containsKey("Component")) {
							// ComponentCreator creator = (ComponentCreator)
							// WebServiceReferences.contextTable
							// .get("Component");
							// creator.changeReminderStatus(component.getComponentId());
						}
						finish();
					}

				}
			});
			llayContent.addView(getNoteContent(component));
			tvTitle.setText(component.getContentName());
			if (component.getfromuser().trim().length() != 0) {
				tvTitle.setText(component.getContentName() + " "
						+ component.getfromuser());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    public RelativeLayout getNoteContent(final Components component) {

		try {
			RelativeLayout rlComponent = new RelativeLayout(context);
			rlComponent.setLayoutParams(new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT, 440));

			LinearLayout llayReminder = new LinearLayout(context);
			llayReminder.setId(1);
			llayReminder.setOrientation(LinearLayout.VERTICAL);
			llayReminder.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			if (component.getcomponentType().equals("note")) {
				ScrollView scrl = new ScrollView(context);
				scrl.setLayoutParams(new LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT, 250));
				TextView tv = new TextView(context);
				tv.setTextColor(Color.BLACK);
				 tv.setText(CompleteListView.textnotes.getInformation(component
				 .getContentPath()));
				scrl.addView(tv);
				llayReminder.addView(scrl);
			} else if (component.getcomponentType().equals("audio")) {
				llayReminder.setGravity(Gravity.CENTER);
				RelativeLayout Rlayout = new RelativeLayout(context);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
				Rlayout.setLayoutParams(params);
				ImageView iv = new ImageView(context);
				File CheckFile = new File(component.getContentPath());
				if (CheckFile.exists()) {
					iv.setBackgroundResource(R.drawable.btn_play_new);
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								Intent intent = new Intent(context,
										MultimediaUtils.class);
								intent.putExtra("filePath",
										component.getContentPath());
								intent.putExtra("requestCode", "2");
								intent.putExtra("action", "audio");
								intent.putExtra("createOrOpen", "open");
								startActivity(intent);

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
					});
				} else {
					iv.setBackgroundResource(R.drawable.broken);
				}
				Rlayout.addView(iv);
				llayReminder.addView(Rlayout);
			} else if (component.getcomponentType().equals("photo")) {
				TextView tvSpace = new TextView(context);
				tvSpace.setHeight(20);
				llayReminder.addView(tvSpace);
				ImageView iv = new ImageView(context);
				File CheckFile = new File(component.getContentPath());
				if (CheckFile.exists()) {
					Bitmap bitmap = callDisp.ResizeImage(
							component.getContentPath(), 300);
					iv.setImageBitmap(bitmap);
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								Intent in = new Intent(context,
										PhotoZoomActivity.class);
								in.putExtra("Photo_path",
										component.getContentPath());
								in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(in);
							} catch (WindowManager.BadTokenException e) {
								Log.e("Log", "Bad Tocken:" + e.toString());
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					});
				} else {
					iv.setBackgroundResource(R.drawable.broken);
				}

				llayReminder.addView(iv);

			} else if (component.getcomponentType().equals("video")) {

				TextView tvSpace = new TextView(context);
				tvSpace.setHeight(20);
				llayReminder.addView(tvSpace);

				final LinearLayout llayVideo = new LinearLayout(context);
				llayVideo.setOrientation(LinearLayout.VERTICAL);

				llayVideo.setGravity(Gravity.CENTER);

				String filename = component.getContentPath();
				if (!filename.endsWith(".mp4")) {
					filename = filename + ".mp4";
				}
				final File vfileCheck = new File(filename);
				Bitmap bitmapThumb = null;
				String thumb = component.getContentPath();
				if (thumb.endsWith(".mp4")) {
					thumb = thumb.replace(".mp4", "");
				}
				final File fileCheckV = new File(thumb + ".jpg");

				final ImageView ivThunb = new ImageView(context);
				ivThunb.setTag(filename);
				if (fileCheckV.exists())
					bitmapThumb = callDisp.ResizeImage(thumb + ".jpg", 300);

				final TableLayout tblControl = new TableLayout(context);
				tblControl.setVisibility(View.INVISIBLE);

				if (vfileCheck.exists()) {

					ivThunb.setImageBitmap(bitmapThumb);
					final VideoView videoView = new VideoView(context);

					videoPlayerHandler = new Handler();
					audioPlayHandler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							if (videoView.isPlaying()) {
								videoView.stopPlayback();
							}
							super.handleMessage(msg);
						}
					};
					ivThunb.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							tblControl.removeAllViews();
							tblControl.setVisibility(View.VISIBLE);

							llayVideo.addView(videoView, 0);
							llayVideo.removeView(ivThunb);
							videoView.setLayoutParams(new LinearLayout.LayoutParams(
									ivThunb.getWidth(), ivThunb.getHeight()));
							videoView.setVideoPath((String)v.getTag());
							videoView.setZOrderOnTop(true);
							videoView.start();

							videoView
									.setOnCompletionListener(new OnCompletionListener() {

										@Override
										public void onCompletion(MediaPlayer mp) {
											llayVideo.removeView(videoView);
											llayVideo.addView(ivThunb, 0);
											try {
												tblControl
														.setVisibility(View.INVISIBLE);
											} catch (Exception e) {
											}
										}
									});
							TableRow tr = new TableRow(context);
							tr.setGravity(Gravity.CENTER);

							final Button btnPlay = new Button(context);
							btnPlay.setTag("Play");
							btnPlay.setBackgroundResource(R.drawable.btn_pause_new);
							btnPlay.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									String hint = (String) btnPlay.getTag();
									if (hint.equals("play")) {
										videoView.pause();
										btnPlay.setTag("pause");
										btnPlay.setBackgroundResource(R.drawable.btn_play_new);
									} else {
										videoView.start();
										btnPlay.setBackgroundResource(R.drawable.btn_pause_new);
										btnPlay.setTag("play");
									}
								}
							});

							Button btnStopVideo = new Button(context);
							btnStopVideo
									.setBackgroundResource(R.drawable.btn_stop_new);
							btnStopVideo.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									btnPlay.setTag("play");
									videoView.stopPlayback();
									tblControl.setVisibility(View.INVISIBLE);
									llayVideo.removeView(videoView);
									llayVideo.addView(ivThunb, 0);

								}
							});

							Button btnFull = new Button(context);
							btnFull.setBackgroundResource(R.drawable.full_screen);
							btnFull.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									try {
										// llayVideo.removeView(tblControl);
										tblControl.setVisibility(View.INVISIBLE);
										llayVideo.removeView(videoView);
										llayVideo.addView(ivThunb, 0);
									} catch (Exception e) {
									}
									Intent intentVPlayer = new Intent(context,
											VideoPlayer.class);
//									intentVPlayer.putExtra("File_Path",
//											component.getContentPath() + ".mp4");
//									intentVPlayer.putExtra("Player_Type",
//											"Video Player");
									intentVPlayer.putExtra("video", component.getContentPath() + ".mp4");
									startActivity(intentVPlayer);
								}
							});
							tr.addView(btnPlay);
							tr.addView(btnStopVideo);
							tr.addView(btnFull);
							tblControl.addView(tr);
						}
					});

					llayVideo.addView(ivThunb);
					llayVideo.addView(tblControl, 1);
					llayReminder.addView(llayVideo);

				}

			} else if (component.getcomponentType().equalsIgnoreCase("sketch")) {

				TextView tvSpace = new TextView(context);
				tvSpace.setHeight(20);
				llayReminder.addView(tvSpace);
				ImageView iv = new ImageView(context);

				File CheckFile = new File(component.getContentPath());
				if (CheckFile.exists()) {
					Bitmap bitmap = callDisp.ResizeImage(
							component.getContentPath(), 300);
					iv.setImageBitmap(bitmap);
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								Intent in = new Intent(context,
										PhotoZoomActivity.class);
								in.putExtra("Photo_path",
										component.getContentPath());
								in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(in);
							} catch (WindowManager.BadTokenException e) {
								Log.e("Log", "Bad Tocken:" + e.toString());
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					});
				} else {
					iv.setBackgroundResource(R.drawable.broken);
				}

				llayReminder.addView(iv);

			}
			Log.i("Note", "Path: #############succesfull 1");

			LinearLayout llaySnooze = new LinearLayout(context);
			llaySnooze.setId(2);
			llaySnooze.setOrientation(LinearLayout.HORIZONTAL);

			llaySnooze.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			llaySnooze.setGravity(Gravity.CENTER);

			Button btnResponse = new Button(context);
			btnResponse.setText("Response");
			btnResponse.setVisibility(View.GONE);
			btnResponse.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (WebServiceReferences.contextTable.containsKey("Component")) {
						// ComponentCreator creator = (ComponentCreator)
						// WebServiceReferences.contextTable
						// .get("Component");
						// creator.changeReminderStatus(component.getComponentId());
					}

					if ((component.getreminderresponsetype().trim().length() != 0)
							&& (component.getViewMode() == 1)) {
						// showResponseDialog(component);
					}

					Toast.makeText(context, "Response clicked", Toast.LENGTH_LONG)
							.show();

				}
			});

			llaySnooze.addView(btnResponse);

			if ((component.getreminderresponsetype().trim().length() != 0)
					&& (component.getViewMode() == 1)) {
				btnResponse.setVisibility(View.VISIBLE);
			}

			Button btnSnooze = new Button(context);
			btnSnooze.setText(SingleInstance.mainContext.getResources().getString(R.string.snooze));
			final Spinner times = new Spinner(context);

			times.setMinimumWidth(60);

			btnSnooze.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					String mins = (String) times.getSelectedItem();

					if (!mins.equals("-select-")) {
						String strReminderTime = getSnoozeTime(Integer
								.parseInt(mins));
						String strQry = "update component set reminderdateandtime='"
								+ strReminderTime
								+ "',reminderstatus=1 where componentid="
								+ component.getComponentId();
						if (callDisp.getdbHeler(context).ExecuteQuery(strQry)) {
							Toast.makeText(context,
									SingleInstance.mainContext.getResources().getString(R.string.snoozed_for) + component.getcomponentType(),
									Toast.LENGTH_SHORT).show();
							finish();
						}
					} else {
						Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.please_select_valid_option),
								Toast.LENGTH_SHORT).show();
					}

				}
			});

			String[] strArray = { "-select-", "1", "2", "5", "10", "30" };
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_item, strArray);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			times.setAdapter(adapter);
			times.setSelection(0);
			llaySnooze.addView(btnSnooze);
			llaySnooze.addView(times);

			RelativeLayout.LayoutParams layBottom = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			layBottom.addRule(RelativeLayout.ALIGN_PARENT_TOP);

			rlComponent.addView(llaySnooze, layBottom);

			RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lay.addRule(RelativeLayout.BELOW, llaySnooze.getId());
			rlComponent.addView(llayReminder, lay);
			return rlComponent;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private String getSnoozeTime(int mins) {
		try {
			Calendar now = Calendar.getInstance();

			now.add(Calendar.MINUTE, mins);

			String strDate = now.get(Calendar.YEAR) + "-"
					+ checkLength(now.get(Calendar.MONTH) + 1) + "-"
					+ checkLength(now.get(Calendar.DATE));

			String strTime = checkLength(now.get(Calendar.HOUR_OF_DAY)) + ":"
					+ checkLength(now.get(Calendar.MINUTE));

			return strDate + " " + strTime;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private String checkLength(int no) {
		try {
			String strNo = Integer.toString(no);

			if (strNo.length() == 1) {
				strNo = "0" + strNo;
			}
			return strNo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			// TODO Auto-generated method stub
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				audioPlayHandler.sendEmptyMessage(1);
				videoPlayerHandler.sendEmptyMessage(1);

				if ((component.getreminderresponsetype().trim().length() != 0)
						&& (component.getViewMode() == 1)) {

					if (WebServiceReferences.contextTable.containsKey("Component")) {
						// ComponentCreator creator = (ComponentCreator)
						// WebServiceReferences.contextTable
						// .get("Component");
						// creator.changeReminderStatus(component.getComponentId());
					}

					AlertDialog.Builder buider = new AlertDialog.Builder(context);
					buider.setMessage(
							"This note having response type,Do you want to send response now or later")
							.setPositiveButton("Respond Now",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											// showResponseDialog(component);
										}
									})
							.setNegativeButton("Later",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.cancel();
											finish();
										}
									});
					AlertDialog alert = buider.create();
					alert.show();

				} else {
					if (WebServiceReferences.contextTable.containsKey("Component")) {
						// ComponentCreator creator = (ComponentCreator)
						// WebServiceReferences.contextTable
						// .get("Component");
						// creator.changeReminderStatus(component.getComponentId());
					}
					finish();
				}
			}

			return super.onKeyDown(keyCode, event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	@Override
	protected void onDestroy() {
		try {
			// TODO Auto-generated method stub
			super.onDestroy();
			WebServiceReferences.isReminderOpened = false;
			WebServiceReferences.contextTable.remove("reminderactivity");
			if (WebServiceReferences.reminderQueue.size() != 0) {
				if ((component.getreminderresponsetype().trim().length() != 0)
						&& (component.getViewMode() == 1)) {
				} else {
					Components comp = (Components) WebServiceReferences.reminderQueue
							.removeFirst();
					WebServiceReferences.reminderMap.remove(Integer.toString(comp
							.getComponentId()));
					Intent i = new Intent(this, ReminderManager.class);
					i.putExtra("component", comp);
					startActivity(i);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void stopPlayBack() {
		try {
			try {
				audioPlayHandler.sendEmptyMessage(1);
				videoPlayerHandler.sendEmptyMessage(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// private void showResponseDialog(final Components component) {
	// try {
	// AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
	// alert_builder.setTitle("Select Response Type");
	// alert_builder.setSingleChoiceItems(m_type, 0,
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int pos) {
	// // TODO Auto-generated method stub
	// if (pos == 0) {
	// Intent intentComponent = new Intent(context,
	// ComponentCreator.class);
	// Bundle bndl = new Bundle();
	// bndl.putString("type", "note");
	// bndl.putBoolean("action", true);
	// bndl.putBoolean("forms", false);
	// bndl.putString("buddyname",
	// component.getfromuser());
	// bndl.putBoolean("send", true);
	// intentComponent.putExtras(bndl);
	// startActivity(intentComponent);
	// dialog.dismiss();
	// finish();
	// } else if (pos == 1) {
	// Intent intentComponent = new Intent(context,
	// ComponentCreator.class);
	// Bundle bndl = new Bundle();
	// bndl.putString("type", "photo");
	// bndl.putBoolean("action", true);
	// bndl.putBoolean("forms", false);
	// bndl.putString("buddyname",
	// component.getfromuser());
	// bndl.putBoolean("send", true);
	// intentComponent.putExtras(bndl);
	// startActivity(intentComponent);
	// dialog.dismiss();
	// finish();
	// } else if (pos == 2) {
	// Intent intentComponent = new Intent(context,
	// ComponentCreator.class);
	// Bundle bndl = new Bundle();
	// bndl.putString("type", "audio");
	// bndl.putBoolean("action", true);
	// bndl.putBoolean("forms", false);
	// bndl.putString("buddyname",
	// component.getfromuser());
	// bndl.putBoolean("send", true);
	// intentComponent.putExtras(bndl);
	// startActivity(intentComponent);
	// dialog.dismiss();
	// finish();
	// } else if (pos == 3) {
	// Intent intentComponent = new Intent(context,
	// ComponentCreator.class);
	// Bundle bndl = new Bundle();
	// bndl.putString("type", "video");
	// bndl.putBoolean("action", true);
	// bndl.putBoolean("forms", false);
	// bndl.putString("buddyname",
	// component.getfromuser());
	// bndl.putBoolean("send", true);
	// intentComponent.putExtras(bndl);
	// startActivity(intentComponent);
	// dialog.dismiss();
	// finish();
	// }
	// }
	// });
	// msg_dialog = alert_builder.create();
	// msg_dialog.show();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}
