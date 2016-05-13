package com.cg.quickaction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.PhotoZoomActivity;
import com.cg.files.ComponentCreator;
import com.cg.files.Components;
import com.cg.instancemessage.NotePickerViewer;
import com.main.AppMainActivity;
import com.util.SingleInstance;
import com.util.VideoPlayer;

public class ShareNotePicker extends Activity implements OnClickListener {

	private Context context;

	/*
	 * Note type string in the database ==> TEXT_NOTE = "note", IMAGE_NOTE =
	 * "photo", AUDIO_NOTE = "audio", VIDEO_NOTE = "video";
	 */
	private String NOTE_TYPE;
	private String form;
	private String strQuery = "select componentid,componentname from component where owner='"
			+ CallDispatcher.LoginUser + "'";
	private HashMap<String, String> hsNotes;
	ScrollView scrl;
	Button btnSelect, btnDone, btn_cancel;
	LinearLayout llayNoteList;
	private CheckBox[] rb;
	Button btnShowAll;
	ImageView btn_add;
	private boolean isaudioPlaying = false;
	String selected, title, action, filepath, id;
	TextView tv_share;
	boolean editNotes = false;
	private ContactLogicbean beanObj;
	private CallDispatcher callDisp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);

			context = this;
			setContentView(R.layout.note_picker);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = CallDispatcher
						.getCallDispatcher(SingleInstance.mainContext);

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);
			displaymetrics = null;
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			beanObj = (ContactLogicbean) getIntent().getParcelableExtra(
					"qabean");
			tv_share = (TextView) findViewById(R.id.tv_share);
			btn_add = (ImageView) findViewById(R.id.iv_addnotes);
			NOTE_TYPE = getIntent().getStringExtra("note");
			editNotes = getIntent().getExtras().getBoolean("editFile");
			title = beanObj.getLabel();
			action = beanObj.getAction();
			tv_share.setText(title);
			if (NOTE_TYPE.equalsIgnoreCase("handsketch")) {
				strQuery = strQuery + " and componenttype='sketch'";
			} else {
				strQuery = strQuery + " and componenttype='" + NOTE_TYPE + "'";
			}

			hsNotes = callDisp.getdbHeler(context).getNoteListForPicker(
					strQuery);

			Log.i("log", "size" + hsNotes.size());
			
			
			
			if (WebServiceReferences.contextTable
					.containsKey("MultimediaUtils")) {
				((MultimediaUtils) WebServiceReferences.contextTable
						.get("MultimediaUtils")).finish();
			}

			btnDone = (Button) findViewById(R.id.btn_note_done);

			btn_cancel = (Button) findViewById(R.id.btn_cancel);
			btn_cancel.setOnClickListener(this);

			if (WebServiceReferences.contextTable
					.containsKey("sharenotepicker")) {
				WebServiceReferences.contextTable.remove("sharenotepicker");
			}
			
			

			WebServiceReferences.contextTable.put("sharenotepicker", this);

			llayNoteList = new LinearLayout(context);
			llayNoteList.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			scrl = (ScrollView) findViewById(R.id.scrl_note);
			scrl.setPadding(10, 10, 10, 10);

			if (hsNotes.size() == 0) {
				TextView tvMessage = new TextView(context);
				tvMessage.setTextColor(Color.BLACK);
				tvMessage.setText(SingleInstance.mainContext.getResources().getString(R.string.no_note_to_select));
				llayNoteList.addView(tvMessage);
			}

			else {

				Object[] strArHintsTemp = hsNotes.values().toArray();
				Set hashSet = new HashSet(Arrays.asList(strArHintsTemp));

				String[] strARHintItems = (String[]) hashSet
						.toArray(new String[hashSet.size()]);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						R.layout.search_list_items, strARHintItems);

				loadNoteList();
			}

			scrl.addView(llayNoteList);

			if (hsNotes.size() == 0) {
				Log.e("message", "**********" + hsNotes.size());
			} else {
				Log.e("message", "" + hsNotes.size());
				if (rb != null) {
					for (int i = 0; i < rb.length; i++) {
						if (rb[i].isChecked()) {
						}
					}
				}
			}

			btnDone.setOnClickListener(this);
			btn_add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("click", "XXXXXXXXXXXXXXXXXXXXXXXXXX onclick event");
					Intent intentComponent = new Intent(context,
							ComponentCreator.class);
					Bundle bndl = new Bundle();
					bndl.putString("type", NOTE_TYPE);
					bndl.putBoolean("action", true);
					bndl.putBoolean("forms", true);
					intentComponent.putExtras(bndl);
					startActivity(intentComponent);

				}
			});
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

    public void refreshList() {
		hsNotes.clear();
		hsNotes = callDisp.getdbHeler(context).getNoteListForPicker(strQuery);

		if (hsNotes.size() == 0) {
			TextView tvMessage = new TextView(context);
			tvMessage.setText(SingleInstance.mainContext.getResources().getString(R.string.no_details_to_select));
			llayNoteList.addView(tvMessage);
		} else

		{
			Object[] strArHintsTemp = hsNotes.values().toArray();
			Set hashSet = new HashSet(Arrays.asList(strArHintsTemp));

			String[] strARHintItems = (String[]) hashSet
					.toArray(new String[hashSet.size()]);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.search_list_items, strARHintItems);

			loadNoteList();
		}

	}

	private void loadNoteList() {
		llayNoteList.removeAllViews();
		rb = new CheckBox[hsNotes.size()];

		// rb = new RadioButton[hsNotes.size()];
		int idx = 0;
		final RadioGroup rgNote = new RadioGroup(context);
		rgNote.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		rgNote.setPadding(10, 10, 10, 10);
		Set s = hsNotes.entrySet();
		Iterator i = s.iterator();

		while (i.hasNext()) {

			Map.Entry me = (Map.Entry) i.next();
			RelativeLayout layout = new RelativeLayout(context);
			layout.setBackgroundColor(Color.WHITE);
			layout.setLayoutParams(new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

			rb[idx] = new CheckBox(context);
			rb[idx].setButtonDrawable(R.drawable.checkbok_selector);
			// rb[idx] = new RadioButton(context);
			rb[idx].setTextColor(Color.BLACK);
			rb[idx].setText(me.getValue().toString());
			rb[idx].setId(Integer.parseInt(me.getKey().toString()));
			selected = me.getValue().toString();
			RelativeLayout.LayoutParams layLeft = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			layLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

			layout.addView(rb[idx], layLeft);
			rb[idx].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("message", "" + rb.length);
					Log.e("message", "" + rb.length);
					// btnDeselect.setVisibility(View.VISIBLE);
					for (int i = 0; i < rb.length; i++) {
						if (rb[i].getId() != v.getId()) {
							rb[i].setChecked(false);
							Log.e("message", "" + rb.length);
						}
					}
				}
			});

			final Components comp = callDisp.getdbHeler(context).getComponent(
					"select * from component where componentid="
							+ me.getKey().toString());
			if (comp.getcomponentType().equals("note")) {
				Button btn = new Button(context);
				btn.setBackgroundResource(R.drawable.options);

				RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

				layout.addView(btn, layRight);
				btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context,
								NotePickerViewer.class);
						Bundle bndl = new Bundle();
						bndl.putString("path", comp.getContentPath());
						if (comp.getContentThumbnail().trim().length() != 0) {
							bndl.putString("title", comp.getContentThumbnail());
						} else {
							bndl.putString("title", comp.getContentName());
						}
						intent.putExtras(bndl);
						startActivity(intent);
					}
				});
				rgNote.addView(layout);
			} else if (comp.getcomponentType().equals("photo")) {
				ImageView iv = new ImageView(context);
				final Components cbean = callDisp
						.getdbHeler(context)
						.getComponent(
								("select * from component where componentid=" + rb[idx]
										.getId()));
				Log.d("image",
						".......................path" + cbean.getContentPath());
				File CheckFile = new File(cbean.getContentPath());
				Log.d("image",
						".......................path" + CheckFile.exists());
				RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
						100, 80);
				layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

				if (CheckFile.exists()) {
					Log.i("log", "inside phpto");

					Bitmap bitMapImage = callDisp.ResizeImage(cbean
							.getContentPath());

					iv.setImageBitmap(bitMapImage);

					iv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								Intent in = new Intent(context,
										PhotoZoomActivity.class);
								in.putExtra("Photo_path",
										cbean.getContentPath());
								in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(in);
							} catch (WindowManager.BadTokenException e) {
								// Log.e("Log", "Bad Tocken:" + e.toString());
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					});

					layout.addView(iv, layRight);
				} else {
					iv.setBackgroundResource(R.drawable.broken);
					layout.addView(iv, layRight);
				}
				rgNote.addView(layout);
			} else if (comp.getcomponentType().equals("sketch")) {

				Log.i("log", "inside sketch");
				ImageView iv = new ImageView(context);
				final Components cbean = callDisp
						.getdbHeler(context)
						.getComponent(
								("select * from component where componentid=" + rb[idx]
										.getId()));
				Log.d("image",
						".......................path" + cbean.getContentPath());
				File CheckFile = new File(cbean.getContentPath());
				Log.d("image",
						".......................path" + CheckFile.exists());
				RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
						100, 80);
				layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

				if (CheckFile.exists()) {

					Bitmap bitMapImage = callDisp.ResizeImage(cbean
							.getContentPath());

					iv.setImageBitmap(bitMapImage);

					iv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								Intent in = new Intent(context,
										PhotoZoomActivity.class);
								in.putExtra("Photo_path",
										cbean.getContentPath());
								in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(in);
							} catch (WindowManager.BadTokenException e) {
								// Log.e("Log", "Bad Tocken:" + e.toString());
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					});

					layout.addView(iv, layRight);
				} else {
					iv.setBackgroundResource(R.drawable.broken);
					layout.addView(iv, layRight);
				}
				rgNote.addView(layout);
			} else if (comp.getcomponentType().equals("audio")) {
				final Components cbean = callDisp
						.getdbHeler(context)
						.getComponent(
								("select * from component where componentid=" + rb[idx]
										.getId()));
				String uri = cbean.getContentPath();
				if (form != null) {
					if (uri.contains("MAD_")) {
						final Button btn = new Button(context);
						btn.setText(SingleInstance.mainContext.getResources().getString(R.string.play));
						btn.setHint(SingleInstance.mainContext.getResources().getString(R.string.play));
						btn.setHint(me.getKey().toString());
						btn.setTag(cbean.getContentPath());
						Log.i("log", "Audio component" + cbean.getContentPath());
						Log.i("log", "Audio component" + me.getKey().toString());
						RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.WRAP_CONTENT,
								RelativeLayout.LayoutParams.WRAP_CONTENT);
						layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

						layout.addView(btn, layRight);

						final MediaPlayer chatplayer = new MediaPlayer();
						chatplayer.setLooping(false);
						try {
							chatplayer.setDataSource(btn.getTag().toString());
							chatplayer.prepare();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						chatplayer
								.setOnCompletionListener(new OnCompletionListener() {

									@Override
									public void onCompletion(MediaPlayer mp) {
										// TODO Auto-generated method stub
										Log.i("log",
												"on completion listener start");
										mp.reset();
										try {
											mp.setDataSource(btn.getTag()
													.toString());
											mp.setLooping(false);
											mp.prepare();

											btn.setText(SingleInstance.mainContext.getResources().getString(R.string.play));
											btn.setHint(SingleInstance.mainContext.getResources().getString(R.string.play));
											isaudioPlaying = false;
										} catch (IllegalArgumentException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IllegalStateException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								});

						btn.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Log.d("player",
										"................................button clicked");
								Log.d("player",
										"................................button clicked"
												+ btn.getHint().toString());
								if (btn.getText().toString().trim()
										.equalsIgnoreCase("play")) {
									if (!isaudioPlaying) {

										chatplayer.start();
										btn.setText(SingleInstance.mainContext.getResources().getString(R.string.stop));
										btn.setHint(SingleInstance.mainContext.getResources().getString(R.string.stop));
										isaudioPlaying = true;
									}

									else {
										showToast(SingleInstance.mainContext.getResources().getString(R.string.audio_is_playing_kindly_stop_that_first));
									}
								} else if (btn.getText().toString().trim()
										.equalsIgnoreCase("stop")) {
									chatplayer.stop();
									try {
										chatplayer.prepare();
									} catch (IllegalStateException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									btn.setText(SingleInstance.mainContext.getResources().getString(R.string.play));
									btn.setHint(SingleInstance.mainContext.getResources().getString(R.string.play));
									isaudioPlaying = false;
								}
							}

						});
						rgNote.addView(layout);

					} else {
						rgNote.removeView(layout);
					}
				} else {
					final Button btn = new Button(context);
					btn.setText(SingleInstance.mainContext.getResources().getString(R.string.play));
					btn.setHint(SingleInstance.mainContext.getResources().getString(R.string.play));
					btn.setHint(me.getKey().toString());
					btn.setTag(cbean.getContentPath());
					Log.i("log", "Audio component" + cbean.getContentPath());
					Log.i("log", "Audio component" + me.getKey().toString());
					Log.i("sharenotepicker",
							"===> file name :: " + cbean.getContentPath());
					Log.i("sharenotepicker", "===> btn tag :: "
							+ btn.getTag().toString());
					RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

					layout.addView(btn, layRight);
					final MediaPlayer chatplayer = new MediaPlayer();
					chatplayer.setLooping(false);
					try {
						chatplayer.reset();
						chatplayer.setDataSource(btn.getTag().toString());
						chatplayer.prepare();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					chatplayer
							.setOnCompletionListener(new OnCompletionListener() {

								@Override
								public void onCompletion(MediaPlayer mp) {
									// TODO Auto-generated method stub
									Log.i("log", "on completion listener start");
									mp.reset();
									try {

										mp.setDataSource(btn.getTag()
												.toString());
										mp.setLooping(false);
										mp.prepare();

										btn.setText(SingleInstance.mainContext.getResources().getString(R.string.play));
										btn.setHint(SingleInstance.mainContext.getResources().getString(R.string.play));
										isaudioPlaying = false;
									} catch (IllegalArgumentException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IllegalStateException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							});

					btn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Log.d("player",
									"................................button clicked");
							Log.d("player",
									"................................button clicked"
											+ btn.getHint().toString());
							Log.i("sharenotepicker",
									"===> onclick file name :: "
											+ cbean.getContentPath());
							Log.i("sharenotepicker", "===> onclick btn tag :: "
									+ btn.getTag().toString());
							if (btn.getText().toString().trim()
									.equalsIgnoreCase("play")) {
								if (!isaudioPlaying) {
									try {
										chatplayer.setDataSource(btn.getTag()
												.toString());
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									chatplayer.start();
									btn.setText(SingleInstance.mainContext.getResources().getString(R.string.stop));
									btn.setHint(SingleInstance.mainContext.getResources().getString(R.string.stop));
									isaudioPlaying = true;
								}

								else {
									showToast(SingleInstance.mainContext.getResources().getString(R.string.audio_is_playing_kindly_stop_that_first));
								}
							} else if (btn.getText().toString().trim()
									.equalsIgnoreCase("stop")) {
								chatplayer.stop();
								try {
									chatplayer.prepare();
								} catch (IllegalStateException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								btn.setText(SingleInstance.mainContext.getResources().getString(R.string.play));
								btn.setHint(SingleInstance.mainContext.getResources().getString(R.string.play));
								isaudioPlaying = false;
							}
						}

					});
					rgNote.addView(layout);

				}

			} else if (comp.getcomponentType().equals("video")) {

				final Components cbean = callDisp
						.getdbHeler(context)
						.getComponent(
								("select * from component where componentid=" + rb[idx]
										.getId()));
				String uri = cbean.getContentPath();

				if (!uri.startsWith("ftp")) {
					if (form != null) {
						Log.i("BLOB", "is formmmmm");
						if (cbean.getContentPath().contains("MVD_")
								|| cbean.getContentPath().contains("MPD_")
								|| cbean.getContentPath().contains("MAD_")) {

							final String uri1 = cbean.getContentPath();

							final File vfileCheck = new File(uri1 + ".mp4");
							Log.i("log", "path" + cbean.getContentPath());
							Bitmap bitmapThumb = null;
							final File fileCheckV = new File(uri1 + ".jpg");

							final ImageView ivThunb = new ImageView(context);
							RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
									100, 80);
							layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

							layout.addView(ivThunb, layRight);

							if (fileCheckV.exists())
								bitmapThumb = callDisp.ResizeImage(uri1
										+ ".jpg");
							Log.e("list", ">>>>>>>>>>>>>>>" + uri1 + ".jpg");
							Log.e("list", ">>>>>>>>>>>>>>>" + bitmapThumb);
							Log.e("list",
									">>>>>>>>>>>>>>>" + vfileCheck.exists());
							if (vfileCheck.exists()) {

								ivThunb.setImageBitmap(bitmapThumb);
								ivThunb.setVisibility(View.VISIBLE);
								ivThunb.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Intent intentVPlayer = new Intent(
												context, VideoPlayer.class);
										// intentVPlayer.putExtra("File_Path",
										// uri1 + ".mp4");
										// intentVPlayer.putExtra("Player_Type",
										// "Video Player");
										intentVPlayer.putExtra("video", uri1
												+ ".mp4");

										startActivity(intentVPlayer);
									}
								});
							}
							rgNote.addView(layout);
						}
					} else {
						final String uri1 = cbean.getContentPath();
						final File vfileCheck = new File(uri1 + ".mp4");
						Log.i("log", "path" + cbean.getContentPath());
						Bitmap bitmapThumb = null;
						final File fileCheckV = new File(uri1 + ".jpg");

						final ImageView ivThunb = new ImageView(context);
						RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
								100, 80);

						layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

						layout.addView(ivThunb, layRight);

						if (fileCheckV.exists())
							bitmapThumb = callDisp.ResizeImage(uri1 + ".jpg");
						Log.e("list", ">>>>>>>>>>>>>>>" + uri1 + ".jpg");
						Log.e("list", ">>>>>>>>>>>>>>>" + bitmapThumb);
						Log.e("list", ">>>>>>>>>>>>>>>" + vfileCheck.exists());
						if (vfileCheck.exists()) {

							ivThunb.setImageBitmap(bitmapThumb);
							ivThunb.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intentVPlayer = new Intent(context,
											VideoPlayer.class);
									// intentVPlayer.putExtra("File_Path", uri1
									// + ".mp4");
									// intentVPlayer.putExtra("Player_Type",
									// "Video Player");
									intentVPlayer.putExtra("video", uri1
											+ ".mp4");
									startActivity(intentVPlayer);
								}
							});
						}
						rgNote.addView(layout);
					}

				} else {
					rgNote.removeView(layout);
				}

			} else {
				Log.d("Unhandled case  : ", comp.getcomponentType());
			}

			idx++;
		}
		llayNoteList.addView(rgNote);
	}

	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == btn_cancel.getId()) {
			SingleInstance.quickViewShow=true;
			if (WebServiceReferences.contextTable
					.containsKey("MultimediaUtils")) {
				((MultimediaUtils) WebServiceReferences.contextTable
						.get("MultimediaUtils")).finish();
			}
			
			ShowError(SingleInstance.mainContext.getResources().getString(R.string.warning),SingleInstance.mainContext.getResources().getString(R.string.are_you_sure_you_want_to_go_back));

		} else if (v == btnDone) {
			if (hsNotes.size() > 0) {
				if (isaudioPlaying) {
					Toast.makeText(getApplicationContext(),
							SingleInstance.mainContext.getResources().getString(R.string.please_stop_audio), Toast.LENGTH_LONG).show();
				} else {
					boolean checkFile = false;
					Components cbean = null;
					for (int i = 0; i < rb.length; i++) {
						if (rb[i].isChecked()) {
							cbean = callDisp
									.getdbHeler(context)
									.getComponent(
											("select * from component where componentid=" + rb[i]
													.getId()));
							checkFile = true;
						}
					}
					beanObj.setLabel(title);
					beanObj.setAction(action);

					if (editNotes) {
						if (checkFile) {
							Intent i = new Intent();
							Bundle bun = new Bundle();
							bun.putString("filepath", cbean.getContentPath());
							i.putExtra("ftp", bun);
							setResult(-13, i);
							finish();
						} else {
							Toast.makeText(context,
									SingleInstance.mainContext.getResources().getString(R.string.please_select_atleast_one_note),
									Toast.LENGTH_LONG).show();
						}
					} else {
						Intent intent = new Intent(this,
								QuickActionSelectcalls.class);
						intent.putExtra("filepath", filepath);
						intent.putExtra("type", NOTE_TYPE);
						intent.putExtra("id", id);
						intent.putExtra("qabean", beanObj);
						intent.putExtra("title", title);
						intent.putExtra("action", action);
						intent.putExtra("editUser", false);
					}
					if (rb != null) {
						for (int i = 0; i < rb.length; i++) {
							if (rb[i].isChecked()) {
								cbean = callDisp
										.getdbHeler(context)
										.getComponent(
												("select * from component where componentid=" + rb[i]
														.getId()));
								break;
							}
						}

						if (cbean != null) {
							beanObj.setLabel(title);
							beanObj.setAction(action);
							Log.i("Label", "label" + beanObj.getLabel());
							if (editNotes) {
								Intent i = new Intent();
								Bundle bun = new Bundle();
								bun.putString("filepath",
										cbean.getContentPath());
								i.putExtra("ftp", bun);
								setResult(-13, i);
								finish();
							} else {
								Intent i = new Intent(this,
										QuickActionSelectcalls.class);

								i.putExtra("filepath", cbean.getContentPath());
								i.putExtra("type", NOTE_TYPE);
								i.putExtra("id", Integer.toString(cbean
										.getComponentId()));
								i.putExtra("title", title);
								i.putExtra("qabean", beanObj);
								i.putExtra("action", action);
								i.putExtra("editUser", false);
								startActivity(i);

							}
						} else {
							showToast(SingleInstance.mainContext.getResources().getString(R.string.sorry_no_file_selected));
						}
					}
				}

			} else {
				showToast(SingleInstance.mainContext.getResources().getString(R.string.no_files));
			}
		}

	}

	public ArrayList<BuddyBean> getselected() {

		ArrayList<BuddyBean> array_buddybean = new ArrayList<BuddyBean>();

		BuddyBean buddyBean = new BuddyBean();
		buddyBean.setBuddyName(selected);
		array_buddybean.add(buddyBean);

		return array_buddybean;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		Menu m_menu = menu;

		if (hsNotes.size() == 0) {
			Log.e("messssage", "taggggggg");
		} else {
			if (rb != null) {
				for (int i = 0; i < rb.length; i++) {
					if (rb[i].isChecked()) {
						m_menu.add(Menu.NONE, 1, 0, "Deselect All");
					}
				}
			}
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Menu m_menu = menu;
		m_menu.clear();

		if (hsNotes.size() == 0) {

		} else {
			if (rb != null) {
				for (int i = 0; i < rb.length; i++) {
					if (rb[i].isChecked()) {
						m_menu.add(Menu.NONE, 1, 0, "Deselect All");
					}
				}
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			Log.e("taggg", "" + rb.length);

			int idx = 0;
			if (rb.length != 0) {
				for (int i = 0; i < rb.length; i++)

				{
					Log.e("tagg", "###" + rb[i] + i);
					if (rb[i].isChecked()) {
						rb[i].setChecked(false);
					} else {
						Log.e("taggg", "notchecked");
					}
				}
			}

		} else {
			Log.e("taggggggg", "message");
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ShowError(SingleInstance.mainContext.getResources().getString(R.string.warning),SingleInstance.mainContext.getResources().getString(R.string.are_you_sure_you_want_to_go_back));
		}
		return super.onKeyDown(keyCode, event);

	}

	public void ShowError(String Title, String Message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Message)
				.setCancelable(false)
				.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();

							}
						})
				.setNegativeButton(SingleInstance.mainContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (WebServiceReferences.contextTable.containsKey("sharenotepicker")) {
			WebServiceReferences.contextTable.remove("sharenotepicker");
		}
		
		if (WebServiceReferences.contextTable
				.containsKey("MultimediaUtils")) {
			((MultimediaUtils) WebServiceReferences.contextTable
					.get("MultimediaUtils")).finish();
		}
		super.onDestroy();
	}

}
