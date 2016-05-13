package com.cg.instancemessage;

import java.io.File;
import java.io.IOException;
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
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.PhotoZoomActivity;
import com.cg.files.ComponentCreator;
import com.cg.files.Components;
import com.main.AppMainActivity;
import com.util.SingleInstance;
import com.util.VideoPlayer;

public class NotePickerScreen extends Activity implements OnClickListener {

	private Context context;

	private String NOTE_TYPE;

	private String form;

    public static boolean isOld = false;

	private String strQuery = "select componentid,componentname from component where owner='"
			+ CallDispatcher.LoginUser + "'";

	private HashMap<String, String> hsNotes;

	private ScrollView scrl;

	private Button btnBack, btnDone, btnDeselect;

	private LinearLayout llayNoteList;

	private RadioButton[] rb;

	private AutoCompleteTextView acSearch;

	private Button btnSearch;

	private MediaPlayer chatplayer;

	private boolean isaudioPlaying = false;

	private CallDispatcher callDispatcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isOld = true;
		context = this;
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.note_picker_screen);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		RelativeLayout rlScren = (RelativeLayout) findViewById(R.id.note_picker_screen);
		// rlScren.setBackgroundColor(Color.rgb(87, 106, 238));
		rlScren.setBackgroundColor(Color.BLACK);
		NOTE_TYPE = getIntent().getStringExtra("note");
		form = getIntent().getStringExtra("forms");

		if (NOTE_TYPE.equalsIgnoreCase("handsketch")) {
			strQuery = strQuery + " and componenttype='sketch'";
		} else {
			strQuery = strQuery + " and componenttype='" + NOTE_TYPE + "'";
		}

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int noScrHeight = displaymetrics.heightPixels;
		int noScrWidth = displaymetrics.widthPixels;

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDispatcher = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDispatcher = new CallDispatcher(context);

		callDispatcher.setNoScrHeight(noScrHeight);
		callDispatcher.setNoScrWidth(noScrWidth);
		displaymetrics = null;

		hsNotes = callDispatcher.getdbHeler(context).getNoteListForPicker(
				strQuery);
		Log.i("log", "size" + hsNotes.size());

		btnBack = (Button) findViewById(R.id.btn_note_back);

		btnDone = (Button) findViewById(R.id.btn_note_done);

		btnDeselect = (Button) findViewById(R.id.btn_note_deselect);
		btnDeselect.setVisibility(View.INVISIBLE);

		acSearch = (AutoCompleteTextView) findViewById(R.id.autoComplete_search_note);

		btnSearch = (Button) findViewById(R.id.btn_note_search);
		btnSearch.setOnClickListener(this);
		if (WebServiceReferences.contextTable.containsKey("notepicker")) {
			WebServiceReferences.contextTable.remove("notepicker");
		}
		WebServiceReferences.contextTable.put("notepicker", this);
		 TextView title = (TextView) findViewById(R.id.title_note);

//		if (NOTE_TYPE.equals("note")) {
//			setTitle("Text Note Picker Screen ");
//		} else if (NOTE_TYPE.equals("photo")) {
//			setTitle("Photo Note Picker Screen");
//		} else if (NOTE_TYPE.equals("audio")) {
//			setTitle(SingleInstance.mainContext.getResources().getString(R.string.audio_note_picker_screen1));
//		} else if (NOTE_TYPE.equals("video")) {
//			setTitle(SingleInstance.mainContext.getResources().getString(R.string.video_note_picker_screen1));
//		} else if (NOTE_TYPE.equals("sketch")) {
//			setTitle("Hand Sketch Note Picker Screen ");
//		}
		if (NOTE_TYPE.equals("note")) {
			title.setText("Text Note Picker Screen ");
		} else if (NOTE_TYPE.equals("photo")) {
			title.setText("Photo Note Picker Screen");
		} else if (NOTE_TYPE.equals("audio")) {
			title.setText(SingleInstance.mainContext.getResources().getString(R.string.audio_note_picker_screen1));
		} else if (NOTE_TYPE.equals("video")) {
			title.setText(SingleInstance.mainContext.getResources().getString(R.string.video_note_picker_screen1));
		} else if (NOTE_TYPE.equals("sketch")) {
			title.setText("Hand Sketch Note Picker Screen ");
		}
		llayNoteList = new LinearLayout(context);
		llayNoteList.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		scrl = (ScrollView) findViewById(R.id.scrl_note);
		scrl.setPadding(10, 10, 10, 10);

		if (hsNotes.size() == 0) {
			TextView tvMessage = new TextView(context);
			tvMessage.setText(SingleInstance.mainContext.getResources().getString(R.string.no_details_to_select));
			llayNoteList.addView(tvMessage);
		} else {

			Object[] strArHintsTemp = hsNotes.values().toArray();
			Set hashSet = new HashSet(Arrays.asList(strArHintsTemp));

			String[] strARHintItems = (String[]) hashSet
					.toArray(new String[hashSet.size()]);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.search_list_items, strARHintItems);

			acSearch.setAdapter(adapter);

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
						Log.e("message", "" + hsNotes.size());
						btnDeselect.setVisibility(View.VISIBLE);
					}
				}
			}
		}

		btnBack.setOnClickListener(this);
		btnDone.setOnClickListener(this);
		btnDeselect.setOnClickListener(this);

	}
@Override
 protected void onResume() {
  super.onResume();
  AppMainActivity.inActivity = this;
 }

	public void refreshList() {
		hsNotes.clear();
		hsNotes = callDispatcher.getdbHeler(context).getNoteListForPicker(
				strQuery);

		if (hsNotes.size() == 0) {
			TextView tvMessage = new TextView(context);
			tvMessage.setText(SingleInstance.mainContext.getResources().getString(R.string.no_details_to_select));
			llayNoteList.addView(tvMessage);
		} else {

			Object[] strArHintsTemp = hsNotes.values().toArray();
			Set hashSet = new HashSet(Arrays.asList(strArHintsTemp));

			String[] strARHintItems = (String[]) hashSet
					.toArray(new String[hashSet.size()]);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.search_list_items, strARHintItems);

			acSearch.setAdapter(adapter);

			loadNoteList();
		}

	}

	private void loadNoteList() {
		llayNoteList.removeAllViews();
		rb = new RadioButton[hsNotes.size()];
		int idx = 0;
		final RadioGroup rgNote = new RadioGroup(context);
		rgNote.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		rgNote.setPadding(10, 10, 10, 10);
		Set s = hsNotes.entrySet();
		Iterator i = s.iterator();
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			Log.i("notepicker", "key =====> " + me.getKey().toString());
			Log.i("notepicker", "value =====> " + me.getValue().toString());
			RelativeLayout layout = new RelativeLayout(context);
			layout.setLayoutParams(new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			rb[idx] = new RadioButton(context);
			rb[idx].setText(me.getValue().toString());
			rb[idx].setTextColor(getResources().getColor(R.color.black));
			rb[idx].setId(Integer.parseInt(me.getKey().toString()));

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
					btnDeselect.setVisibility(View.VISIBLE);
					for (int i = 0; i < rb.length; i++) {
						if (rb[i].getId() != v.getId()) {
							rb[i].setChecked(false);
							Log.e("message", "" + rb.length);
							// btnDeselect.setVisibility(View.VISIBLE);
						}
					}
				}
			});

			final Components comp = callDispatcher.getdbHeler(context)
					.getComponent(
							"select * from component where componentid="
									+ me.getKey().toString());

			if (comp.getcomponentType().equals("note")) {
				Button btn = new Button(context);
				btn.setText("View");

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
				final Components cbean = callDispatcher
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
					Bitmap bitMapImage = callDispatcher.ResizeImage(cbean
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
				final Components cbean = callDispatcher
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

					Bitmap bitMapImage = callDispatcher.ResizeImage(cbean
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
				// final Components cbean = SplashScreen
				// .getDB(context)
				// .getComponent(
				// ("select * from component where componentid=" + rb[idx]
				// .getId()));
				String uri = comp.getContentPath();
				if (form != null) {
					if (uri.contains("MAD_")) {
						final Button btn = new Button(context);
						btn.setText("Play");
						btn.setHint("play");
						btn.setHint(me.getKey().toString());
						btn.setTag(comp.getContentPath());
						Log.i("log", "Audio component" + comp.getContentPath());
						Log.i("log", "Audio component" + me.getKey().toString());
						RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.WRAP_CONTENT,
								RelativeLayout.LayoutParams.WRAP_CONTENT);
						layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

						layout.addView(btn, layRight);

						chatplayer = new MediaPlayer();
						chatplayer.setLooping(false);

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
											btn.setText("Play");
											btn.setHint("play");
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
								Log.d("log",
										"................................button clicked");
								Log.d("log",
										"................................button clicked"
												+ btn.getHint().toString());
								Log.i("log", "=============> "
										+ v.getTag().toString());

								if (btn.getText().toString().trim()
										.equalsIgnoreCase("play")) {
									if (!isaudioPlaying) {
										try {
											chatplayer.setDataSource(v.getTag()
													.toString());
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
										chatplayer.start();
										btn.setText("Stop");
										btn.setHint("stop");
										isaudioPlaying = true;
									}

									else {
										showToast("Audio is Playing .Kindly Stop that first");
									}
								} else if (btn.getText().toString().trim()
										.equalsIgnoreCase("stop")) {
									chatplayer.stop();
									try {
										chatplayer.release();
										chatplayer.prepare();
									} catch (IllegalStateException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									btn.setText("Play");
									btn.setHint("play");
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
					btn.setText("Play");
					btn.setHint("play");
					btn.setHint(me.getKey().toString());
					btn.setTag(comp.getContentPath());
					Log.i("notepicker",
							"Audio component" + comp.getContentPath());
					Log.i("notepicker", "Audio component"
							+ me.getKey().toString());
					RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

					layout.addView(btn, layRight);

					chatplayer = new MediaPlayer();
					chatplayer.setLooping(false);
					chatplayer
							.setOnCompletionListener(new OnCompletionListener() {

								@Override
								public void onCompletion(MediaPlayer mp) {
									// TODO Auto-generated method stub
									Log.i("log", "on completion listener start");
									mp.reset();
									// mp.release();
									try {
										// mp.setDataSource(btn.getTag()
										// .toString());
										// mp.setLooping(false);
										// mp.prepare();

										btn.setText("Play");
										btn.setHint("play");
										isaudioPlaying = false;
									} catch (IllegalArgumentException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IllegalStateException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							});

					btn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Log.d("notepicker",
									"................................button clicked");
							Log.d("notepicker",
									"................................button clicked"
											+ btn.getHint().toString());
							Log.i("notepicker",
									"=============> button click path "
											+ v.getTag().toString());
							if (btn.getText().toString().trim()
									.equalsIgnoreCase("play")) {
								if (!isaudioPlaying) {
									try {
										chatplayer.setDataSource(v.getTag()
												.toString());
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
									chatplayer.start();
									btn.setText("Stop");
									btn.setHint("stop");
									isaudioPlaying = true;
								}

								else {
									showToast("Audio is Playing .Kindly Stop that first");
								}
							} else if (btn.getText().toString().trim()
									.equalsIgnoreCase("stop")) {
								chatplayer.stop();
								try {
									if (chatplayer != null) {
										chatplayer.reset();
									}
									chatplayer.prepare();
								} catch (IllegalStateException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								btn.setText("Play");
								btn.setHint("play");
								isaudioPlaying = false;
							}
						}

					});
					rgNote.addView(layout);

				}

			} else if (comp.getcomponentType().equals("video")) {

				final Components cbean = callDispatcher
						.getdbHeler(context)
						.getComponent(
								("select * from component where componentid=" + rb[idx]
										.getId()));
				String uri = cbean.getContentPath();

				if (!uri.startsWith("http")) {
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
								// bitmapThumb = ResizeImage(component
								// .getContentThumbnail());
								bitmapThumb = callDispatcher.ResizeImage(uri1
										+ ".jpg");
							Log.e("list", ">>>>>>>>>>>>>>>" + uri1 + ".jpg");
							Log.e("list", ">>>>>>>>>>>>>>>" + bitmapThumb);
							Log.e("list",
									">>>>>>>>>>>>>>>" + vfileCheck.exists());
							if (vfileCheck.exists()) {

								ivThunb.setImageBitmap(bitmapThumb);
								ivThunb.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Intent intentVPlayer = new Intent(
												context, VideoPlayer.class);
//										intentVPlayer.putExtra("File_Path",
//												uri1 + ".mp4");
//										intentVPlayer.putExtra("Player_Type",
//												"Video Player");
										intentVPlayer.putExtra("video", uri1 + ".mp4");

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
							// bitmapThumb = ResizeImage(component
							// .getContentThumbnail());
							bitmapThumb = callDispatcher.ResizeImage(uri1
									+ ".jpg");
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
//									intentVPlayer.putExtra("File_Path", uri1
//											+ ".mp4");
//									intentVPlayer.putExtra("Player_Type",
//											"Video Player");
									intentVPlayer.putExtra("video", uri1+ ".mp4");
									startActivity(intentVPlayer);
								}
							});
						}
						rgNote.addView(layout);
					}

				} else {
					rgNote.removeView(layout);
				}

			}

			idx++;
		}
		llayNoteList.addView(rgNote);
	}

	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public void stopAudio() {
		if (chatplayer != null) {
			if (isaudioPlaying) {
				chatplayer.stop();
				finish();
			}
		}
	}

	@Override
	public void onClick(View v) {

		Log.e("click", "XXXXXXXXXXXXXXXXXXXXXXXXXX onclick event" + v);
		if (v == btnBack) {
			// setActivityResult();
			if (chatplayer != null) {
				if (isaudioPlaying) {
					chatplayer.stop();
					finish();
				}

				finish();

			} else {
				finish();
			}

		} else if (v == btnSearch) {
			SingleInstance.notePicker=true;
			if (!isaudioPlaying) {

				Log.e("click", "XXXXXXXXXXXXXXXXXXXXXXXXXX onclick event");
				Intent intentComponent = new Intent(context,
						ComponentCreator.class);
				Bundle bndl = new Bundle();
				bndl.putString("type", NOTE_TYPE);
				bndl.putBoolean("action", true);
				bndl.putBoolean("forms", true);
				intentComponent.putExtras(bndl);
				startActivity(intentComponent);
			} else {
				Toast.makeText(context,
						"Please stop playing audio before adding new audio",
						Toast.LENGTH_LONG).show();
			}
		} else if (v == btnDone) {
			if (isaudioPlaying) {
				Toast.makeText(getApplicationContext(),
						"Due to interruption audio player stoped.",
						Toast.LENGTH_LONG).show();
				chatplayer.stop();
				isaudioPlaying = false;
				setActivityResult();

				// Message msgStopPlayer = new Message();
				// msgStopPlayer.arg1 = 430;
				// AudioPlayerHandler.sendMessage(msgStopPlayer);
			}
			if (!isaudioPlaying) {
				setActivityResult();
			}
		} else if (v == btnDeselect) {
			if (rb.length != 0) {
				for (int i = 0; i < rb.length; i++)

				{
					Log.e("tagg", "###" + rb[i] + i);
					if (rb[i].isChecked()) {
						rb[i].setChecked(false);
						btnDeselect.setVisibility(View.INVISIBLE);
					} else {
						Log.e("taggg", "notchecked");
					}
				}
			}

		} else {
			Log.e("taggggggg", "message");
		}

	}

	private void setActivityResult() {

		Components cbean = null;
		if (rb != null) {
			for (int i = 0; i < rb.length; i++) {
				if (rb[i].isChecked()) {
					cbean = callDispatcher
							.getdbHeler(context)
							.getComponent(
									("select * from component where componentid=" + rb[i]
											.getId()));
					break;
				}
			}

			if (cbean != null) {
				Intent i = new Intent();
				Bundle bun = new Bundle();
				bun.putString("filepath", cbean.getContentPath());
				bun.putString("type", NOTE_TYPE);
				bun.putString("id", Integer.toString(cbean.getComponentId()));
				i.putExtra("share", bun);
				setResult(RESULT_OK, i);
				finish();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Select a Note").setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();

			}
		} else {
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		Menu m_menu = menu;
		// if(rb.length!=0)
		// {
		// m_menu.add(Menu.NONE, 1, 0, "Deselct All");
		// }
		// else
		// {
		// Log.e("messssage", "taggggggg");
		// m_menu.clear();
		// }

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
		// if(rb.length!=0)
		// {
		// m_menu.add(Menu.NONE, 1, 0, "Deselect All");
		// }
		// else {
		// Log.e("messssage", "taggggggg");
		// }

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
			// setActivityResult();
			ShowError("Note", "Are you sure,You want to go back ?");
		}
		return super.onKeyDown(keyCode, event);

	}

	public void ShowError(String Title, String Message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Message)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								finish();

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
		if (WebServiceReferences.contextTable.containsKey("notepicker")) {
			WebServiceReferences.contextTable.remove("notepicker");
		}
		super.onDestroy();
	}

}
