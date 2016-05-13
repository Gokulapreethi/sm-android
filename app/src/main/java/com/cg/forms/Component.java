package com.cg.forms;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;
import com.cg.snazmed.R;
import com.cg.snazmed.R.drawable;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.TextNoteDatas;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.HandSketchActivity2;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.PhotoZoomActivity;
import com.cg.files.CompleteListBean;
import com.cg.files.CompleteListView;
import com.cg.hostedconf.AppReference;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.util.CustomVideoCamera;
import com.util.SingleInstance;
import com.util.VideoPlayer;

public class Component extends Activity implements OnClickListener {

	private Button btnaudio = null, btnVideo = null, btnImage = null;

	private String path = null;
	private LinearLayout contentLayout = null, footerlayout = null,
			titLayout = null;
	private int noScrHeight;
	private EditText edNotes = null;
	private Context context = null;
	private String ComponentPath = null;
	private boolean ON_CREATE, forms = false;
	private int volume = 50;
	private boolean isAudioRecordingStart = false;
	private Handler StopAudioRecordingHandler = null, handlerSeek = null,
			AudioPlayerHandler = null, StopVideoHandler = null;
	private boolean isAudioPlay = false, isVideoPlay = false;
	private int videoPause = 0;

	private StringBuffer instructionTest = new StringBuffer();
	private int FROM_GALERY = 2, FROM_CAMERA = 3, AUDIO = 4, CAPTURE_VIDEO = 5,
			HAND_SKETCH = 6, GET_RESOURCES, TEXT = 1,
			GALLERY_KITKAT_INTENT_CALLED = 7;
	private AlertDialog.Builder builder = null;
	private Button btnDone, saveadd = null;
	private CompleteListBean cbean = null;
	private CallDispatcher callDisp = null;
	private String thumpnail = "";
	private TextView call_view = null;
	private String datas = null;

	private Button btnIMRequest, btnBack, btntextsave;
	private TextView tvTitle;

	private Handler viewHandler = new Handler();
	private String noteType = null;
	private VideoView videoView = null;
	private MediaPlayer mpAudio = null;

	private Handler handler = new Handler();
	private ProgressDialog dialog = null;
	private int noScrWidth;
	private Button btn_notification;
	private boolean ishand_sketch = false;
	private ViewFlipper flipper = null;
	private MediaRecorder recorder = null;
	private Button btn_sketch;
	private Button editTextNotes;
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title2);

		btnIMRequest = (Button) findViewById(R.id.btn_im);
		btntextsave = (Button) findViewById(R.id.btn_save_note);
		btntextsave.setText("Save");
		btntextsave.setTextColor(Color.WHITE);
		btntextsave.setOnClickListener(this);
		// btntextsave.setVisibility(View.GONE);
		btn_notification = (Button) findViewById(R.id.notification);
		editTextNotes = (Button) findViewById(R.id.editnote);
		Button btnaccept = (Button) findViewById(R.id.btn_accept);
		Button btnreject = (Button) findViewById(R.id.btn_reject);
		btnaccept.setVisibility(View.GONE);
		btnreject.setVisibility(View.GONE);
		btnBack = (Button) findViewById(R.id.btn_back);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		btnBack.setLayoutParams(params);
		btnBack.setBackgroundResource(R.drawable.ic_action_back);
		tvTitle = (TextView) findViewById(R.id.tv_note_title);
		tvTitle.setTextColor(Color.WHITE);
		tvTitle.setText("Instructions");
		tvTitle.setSingleLine();

		btnIMRequest.setVisibility(View.GONE);
		btnIMRequest.setBackgroundResource(R.drawable.one);
		setContentView(R.layout.instructionmedia);
		WebServiceReferences.contextTable.put("cmpinstruction", this);
		WebServiceReferences.contextTable.put("a_play", this);
		Bundle bndl = getIntent().getExtras();
		ON_CREATE = bndl.getBoolean("action");
		noteType = bndl.getString("type");
		forms = bndl.getBoolean("forms");

		CompleteListView.checkDir();
		context = this;
		getVolume();
		btnDone = (Button) findViewById(R.id.btnsavemedia);
		btnaudio = (Button) findViewById(R.id.btnAudio);
		btnImage = (Button) findViewById(R.id.btnPhoto);
		btnVideo = (Button) findViewById(R.id.btnVideo);
		saveadd = (Button) findViewById(R.id.btnsaveandadd);
		btn_sketch = (Button) findViewById(R.id.btnsketch);

		flipper = (ViewFlipper) findViewById(R.id.lay_flipper);

		contentLayout = (LinearLayout) findViewById(R.id.footer);
		footerlayout = (LinearLayout) findViewById(R.id.footer2);
		titLayout = (LinearLayout) findViewById(R.id.footer1);

		contentLayout.setBackgroundColor(Color.GRAY);
		builder = new AlertDialog.Builder(context);

		handlerSeek = new Handler();

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		noScrHeight = displaymetrics.heightPixels;
		noScrWidth = displaymetrics.widthPixels;

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);

		callDisp.setNoScrHeight(noScrHeight);
		callDisp.setNoScrWidth(noScrWidth);
		displaymetrics = null;
		imageLoader = new ImageLoader(context);

		if (callDisp == null) {
			if (ValueHandler.hsActiveActivity.size() != 0
					&& ValueHandler.hsCallDispatcherObjs.size() != 0) {
				if (ValueHandler.hsActiveActivity.get(1).equals("tabscreen")) {
				} else if (ValueHandler.hsActiveActivity.get(1).equals(
						"todaylist")) {
				} else if (ValueHandler.hsActiveActivity.get(1).equals(
						"listall")) {
				}
			}
		}

		if (ON_CREATE) {
			editTextNotes.setVisibility(View.INVISIBLE);
			if (WebServiceReferences.contextTable.containsKey("notepicker")) {
				if (noteType != null) {
					showNoteView(noteType);
				}
			} else {

				showNoteView("note");
			}
		} else {
		}

		saveadd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.i("FORMSOPTMZ", "====>Inside saveAnd Add");

				if (isAudioRecordingStart) {
					ShowError(SingleInstance.mainContext.getResources()
							.getString(R.string.audio),
							SingleInstance.mainContext.getResources()
									.getString(R.string.audio_stop));
				} else if (isAudioPlay) {
					ShowError(SingleInstance.mainContext.getResources()
							.getString(R.string.audio),
							SingleInstance.mainContext.getResources()
									.getString(R.string.audio_stop));
				} else if (isVideoPlay) {
					ShowError(SingleInstance.mainContext.getResources()
							.getString(R.string.video),
							SingleInstance.mainContext.getResources()
									.getString(R.string.video_stop));
				} else {
					if (GET_RESOURCES == TEXT) {
						Log.i("FORMSOPTMZ", "====>" + GET_RESOURCES);

						if (edNotes.getText().toString().trim().length() != 0) {

							if (ON_CREATE) {
								ComponentPath = "/sdcard/COMMedia/"
										+ "Instruction_note"
										+ CompleteListView.getFileName()
										+ ".txt";
								path = "Instruction_note"
										+ CompleteListView.getFileName()
										+ ".txt";
							} else {
								ComponentPath = callDisp.cmp.getContentpath();
							}

							if (CompleteListView.textnotes == null)
								CompleteListView.textnotes = new TextNoteDatas();

							CompleteListView.textnotes
									.checkAndCreate(ComponentPath, edNotes
											.getText().toString());
							if (ON_CREATE) {

								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										edNotes.getWindowToken(), 0);
								getWindow()
										.setSoftInputMode(
												WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

								if (WebServiceReferences.contextTable
										.containsKey("notepicker")) {
									// NotePickerScreen picker =
									// (NotePickerScreen)
									// WebServiceReferences.contextTable
									// .get("notepicker");
									// picker.refreshList();
									finishActivity();
								} else {

									// footerlayout.setVisibility(View.VISIBLE);
									showToast(SingleInstance.mainContext.getResources().getString(R.string.text_note_saved_succesfully));

									ComponentPath = null;

									ON_CREATE = true;
									if (instructionTest.length() > 0) {
										instructionTest.append("," + path);

									} else {
										instructionTest.append(path);

									}

									showNoteView("note");

								}

							} else {
								datas = edNotes.getText().toString().trim();
							}
							btnDone.setEnabled(true);
						} else {
							showToast(SingleInstance.mainContext
									.getResources()
									.getString(
											R.string.empty_instruction_cannot_created));
						}

						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(edNotes.getWindowToken(), 0);
						getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

					} else {
						if (!isAudioRecordingStart) {

							if (ComponentPath != null) {
								if (GET_RESOURCES == CAPTURE_VIDEO) {
									File vfile = new File(ComponentPath
											+ ".mp4");
									File vfile1 = new File(ComponentPath
											+ ".jpg");

									if (vfile.exists() && vfile1.exists()) {

										if (WebServiceReferences.contextTable
												.containsKey("notepicker")) {
											// NotePickerScreen picker =
											// (NotePickerScreen)
											// WebServiceReferences.contextTable
											// .get("notepicker");
											// picker.refreshList();
											finish();
										} else {

											// footerlayout
											// .setVisibility(View.VISIBLE);
											showToast(SingleInstance.mainContext.getResources().getString(R.string.video_note_saved_succesfully));

											ComponentPath = null;

											btnDone.setEnabled(true);
											if (instructionTest.length() > 0) {
												instructionTest.append(","
														+ path);

											} else {
												instructionTest.append(path);

											}

											showNoteView("note");

										}
									} else {
										showToast(SingleInstance.mainContext
												.getResources()
												.getString(
														R.string.create_instruction));
									}

								} else {

									File fle = new File(ComponentPath);
									if (fle.exists()) {

										if (WebServiceReferences.contextTable
												.containsKey("notepicker")) {
											// NotePickerScreen picker =
											// (NotePickerScreen)
											// WebServiceReferences.contextTable
											// .get("notepicker");
											// picker.refreshList();
											finish();
										} else {

											// footerlayout
											// .setVisibility(View.VISIBLE);
											if (GET_RESOURCES == AUDIO) {
												showToast(SingleInstance.mainContext.getResources().getString(R.string.audio_note_saved_succesfully));
												if (instructionTest.length() > 0) {
													instructionTest.append(","
															+ path);

												} else {
													instructionTest
															.append(path);

												}

												showNoteView("note");

											} else {
												showToast(SingleInstance.mainContext.getResources().getString(R.string.image_note_saved_succesfully));
												if (instructionTest.length() > 0) {
													instructionTest.append(","
															+ path);

												} else {
													instructionTest
															.append(path);

												}
												showNoteView("note");

											}
											btnDone.setEnabled(true);
											ComponentPath = null;

										}
									} else {
										showToast(SingleInstance.mainContext
												.getResources()
												.getString(
														R.string.create_instruction));
									}
								}
							} else {
								showToast(SingleInstance.mainContext
										.getResources()
										.getString(
												R.string.empty_components_cannot_created));
							}

						} else {
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.stop_audio_save));
						}
					}
				}
			}
		});

		btnaudio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				showNoteView(SingleInstance.mainContext.getResources().getString(R.string.audio));
			}
		});
		btnImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				showNoteView(SingleInstance.mainContext.getResources().getString(R.string.photo));
			}
		});
		btnVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNoteView(SingleInstance.mainContext.getResources().getString(R.string.video));
			}
		});
		btn_sketch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNoteView(SingleInstance.mainContext.getResources().getString(R.string.handsketch));
			}
		});

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isAudioRecordingStart) {
					ShowError(SingleInstance.mainContext.getResources().getString(R.string.audio),
							SingleInstance.mainContext.getResources().getString(R.string.audio_stop));
				} else if (isAudioPlay) {
					ShowError(SingleInstance.mainContext.getResources().getString(R.string.audio),
							SingleInstance.mainContext.getResources().getString(R.string.audio_is_playing_kindly_stop_that_first));
				} else if (isVideoPlay) {
					ShowError(SingleInstance.mainContext.getResources().getString(R.string.video),
							SingleInstance.mainContext.getResources().getString(R.string.video_stop));
				} else {
					showDeleteAlert();
				}
			}
		});

		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveFile();
			}
		});

	}

	private void saveFile() {

		if (isAudioRecordingStart) {
			ShowError(
					SingleInstance.mainContext.getResources().getString(
							R.string.audio), SingleInstance.mainContext
							.getResources().getString(R.string.audio_stop));
		} else if (isAudioPlay) {
			ShowError(
					SingleInstance.mainContext.getResources().getString(
							R.string.audio),
					SingleInstance.mainContext.getResources().getString(
							R.string.audio_is_playing_kindly_stop_that_first));
		} else if (isVideoPlay) {
			ShowError(
					SingleInstance.mainContext.getResources().getString(
							R.string.video), SingleInstance.mainContext
							.getResources().getString(R.string.video_stop));
		} else {
			if (GET_RESOURCES == TEXT) {

				if (edNotes.getText().toString().trim().length() != 0) {

					if (ON_CREATE) {
						ComponentPath = "/sdcard/COMMedia/"
								+ "Instruction_note"
								+ CompleteListView.getFileName() + ".txt";
						path = "Instruction_note"
								+ CompleteListView.getFileName() + ".txt";
					} else {
						ComponentPath = path = "Instruction_note"
								+ CompleteListView.getFileName() + ".txt";
					}

					if (CompleteListView.textnotes == null)
						CompleteListView.textnotes = new TextNoteDatas();

					CompleteListView.textnotes.checkAndCreate(ComponentPath,
							edNotes.getText().toString());
					if (ON_CREATE) {

						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(edNotes.getWindowToken(), 0);
						getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

						if (WebServiceReferences.contextTable
								.containsKey("notepicker")) {

							finishActivity();
						} else {

							// footerlayout.setVisibility(View.VISIBLE);
							showToast(SingleInstance.mainContext
									.getResources()
									.getString(
											R.string.text_note_saved_succesfully));

							finishActivitys(true);

							ComponentPath = null;

							ON_CREATE = true;

						}

					} else {
						datas = edNotes.getText().toString().trim();
					}
					btnDone.setEnabled(true);
				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(
									R.string.empty_instruction_cannot_created));
				}

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edNotes.getWindowToken(), 0);
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

			} else {
				if (!isAudioRecordingStart) {

					if (ComponentPath != null) {
						if (GET_RESOURCES == CAPTURE_VIDEO) {
							File vfile = new File(ComponentPath + ".mp4");
							File vfile1 = new File(ComponentPath + ".jpg");

							if (vfile.exists() && vfile1.exists()) {

								if (WebServiceReferences.contextTable
										.containsKey("notepicker")) {

									finish();
								} else {

									// footerlayout.setVisibility(View.VISIBLE);
									showToast(SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.video_note_saved_succesfully));

									finishActivitys(true);

									ComponentPath = null;

									btnDone.setEnabled(true);

								}
							} else {
								showToast(SingleInstance.mainContext
										.getResources().getString(
												R.string.create_instruction));
							}

						} else {

							File fle = new File(ComponentPath);
							if (fle.exists()) {

								if (WebServiceReferences.contextTable
										.containsKey("notepicker")) {

									finish();
								} else {

									// footerlayout.setVisibility(View.VISIBLE);
									if (GET_RESOURCES == AUDIO) {
										showToast(SingleInstance.mainContext
												.getResources()
												.getString(
														R.string.audio_note_saved_succesfully));
										{

											finishActivitys(true);

										}

									} else {
										showToast(SingleInstance.mainContext
												.getResources()
												.getString(
														R.string.image_note_saved_succesfully));

										finishActivitys(true);
									}
									btnDone.setEnabled(true);
									ComponentPath = null;

								}
							} else {
								showToast(SingleInstance.mainContext
										.getResources().getString(
												R.string.create_instruction));
							}
						}
					} else {
						showToast(SingleInstance.mainContext
								.getResources()
								.getString(
										R.string.empty_components_cannot_created));
					}

				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.stop_audio_save));
				}
			}
		}

	}

	SharedPreferences preference;

	public void finishActivitys(boolean save) {
		if (save) {
			Log.i("forms123", "filepath finishactivitys : " + path);
			if (instructionTest.length() > 0) {
				instructionTest.append("," + path);

			} else {
				instructionTest.append(path);

			}
		}

		if (instructionTest.length() > 0) {

			String result = instructionTest.toString();

			Intent i = new Intent();
			Bundle bun = new Bundle();
			bun.putString("filepath", result);

			i.putExtra("share", bun);
			setResult(-10, i);
			finish();

		} else {

			finish();

		}

	}

	private void saveVolume() {
		try {
			if (preference == null)
				preference = PreferenceManager
						.getDefaultSharedPreferences(context);

			if (preference.contains("volume")) {
				SharedPreferences.Editor editor = preference.edit();
				editor.putInt("volume", volume);
				editor.commit();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void getVolume() {
		try {
			if (preference == null)
				preference = PreferenceManager
						.getDefaultSharedPreferences(context);

			if (preference.contains("volume")) {
				volume = preference.getInt("volume", 50);
			}
		} catch (Exception e) {

		}
	}

	public void showDefaultView() {
		GET_RESOURCES = TEXT;
		edNotes = new EditText(this);
		edNotes.setScroller(new Scroller(this));
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		edNotes.setGravity(Gravity.TOP);
		edNotes.setLayoutParams(lp);
		edNotes.setFocusable(true);
		edNotes.setHint(SingleInstance.mainContext.getResources().getString(R.string.tap_to_add));
		edNotes.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				try {
					if (thumpnail.trim().length() == 0) {
						if (edNotes.getText().toString().length() == 0) {
							tvTitle.setText("Note");
						} else {
							if (edNotes.getText().toString().length() >= 12) {

								tvTitle.setText(edNotes.getText().toString()
										.trim().substring(0, 11));

							} else if (edNotes.getText().toString().length() < 12) {

								tvTitle.setText(edNotes
										.getText()
										.toString()
										.substring(
												0,
												edNotes.getText().toString()
														.length()));

							}

						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				GET_RESOURCES = TEXT;
			}
		});

	}

	private void showCallView() {
		GET_RESOURCES = TEXT;
		call_view = new TextView(this);
		call_view.setScroller(new Scroller(this));
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		call_view.setGravity(Gravity.TOP);
		call_view.setLayoutParams(lp);
		call_view.setBackgroundColor(Color.WHITE);
	}

	static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {

			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return (availableBlocks * blockSize) / 1024;

		} else {
			return -1;
		}
	}

	public void showMenu() {
		final CharSequence[] items = {
				SingleInstance.mainContext.getResources().getString(
						R.string.from_gallery),
				SingleInstance.mainContext.getResources().getString(
						R.string.from_camera) };

		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {

				tvTitle.setText(SingleInstance.mainContext.getResources().getString(R.string.photo));
				if (item == 0) {
					GET_RESOURCES = FROM_GALERY;
					if (Build.VERSION.SDK_INT < 19) {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("image/*");
						startActivityForResult(intent, FROM_GALERY);
					} else {
						GET_RESOURCES = GALLERY_KITKAT_INTENT_CALLED;
						Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("image/*");
						startActivityForResult(intent,
								GALLERY_KITKAT_INTENT_CALLED);

					}
				} else if (item == 1) {
					GET_RESOURCES = FROM_CAMERA;
					Long free_size = getAvailableExternalMemorySize();
					if (free_size > 0 && free_size >= 5120) {
						ComponentPath = "Instruction_image"
								+ CompleteListView.getFileName() + ".jpg";
						path = "Instruction_image"
								+ CompleteListView.getFileName() + ".jpg";
						String strIPath = "/sdcard/COMMedia/" + ComponentPath;
						ComponentPath = strIPath;

						// Intent intent = new Intent(context,
						// MultimediaUtils.class);
						// intent.putExtra("filePath", strIPath);
						// intent.putExtra("requestCode", FROM_CAMERA);
						// intent.putExtra("action",
						// MediaStore.ACTION_IMAGE_CAPTURE);
						// intent.putExtra("createOrOpen", "create");
						// startActivity(intent);

						Intent intent = new Intent(context,
								CustomVideoCamera.class);
						intent.putExtra("filePath", strIPath);
						intent.putExtra("isPhoto", true);
						startActivityForResult(intent, FROM_CAMERA);

					} else {
						showToast(SingleInstance.mainContext.getResources().getString(R.string.insufficient_memory));
					}
				}

			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	public void showAudioView() {
		try {
			String strIPath = null;
			GET_RESOURCES = AUDIO;
			contentLayout.removeAllViews();

			strIPath = "/sdcard/COMMedia/" + "Instruction_audio"
					+ CompleteListView.getFileName() + ".mp4";
			path = "Instruction_audio" + CompleteListView.getFileName()
					+ ".mp4";
			ComponentPath = strIPath;

			// Audio

			Intent intent = new Intent(context, MultimediaUtils.class);
			intent.putExtra("filePath", strIPath);
			intent.putExtra("requestCode", AUDIO);
			intent.putExtra("action", "audio");
			intent.putExtra("createOrOpen", "create");
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private LinearLayout AudioNoteView(int state, final String AudioPath)
			throws Exception {

		try {
			if (ON_CREATE) {
				btnDone.setVisibility(View.VISIBLE);
				titLayout.setVisibility(View.GONE);
				editTextNotes.setVisibility(View.GONE);
				footerlayout.setVisibility(View.GONE);

			} else {
				btnDone.setVisibility(View.GONE);
				editTextNotes.setVisibility(View.VISIBLE);
				titLayout.setVisibility(View.VISIBLE);
				footerlayout.setVisibility(View.VISIBLE);
			}

			final int PRE_PLAY = 3;

			final LinearLayout llAudio = new LinearLayout(context);
			llAudio.setOrientation(LinearLayout.VERTICAL);
			llAudio.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			llAudio.setGravity(Gravity.CENTER);
			final Button btnAudio = new Button(context);

			btnAudio.setHint(Integer.toString(state));
			btnAudio.setGravity(Gravity.CENTER);
			btnAudio.setHintTextColor(Color.TRANSPARENT);
			btnAudio.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			if (state == PRE_PLAY) {
				btnAudio.setBackgroundResource(R.drawable.btn_play_new);
			}

			btnAudio.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(context, MultimediaUtils.class);
					intent.putExtra("filePath", AudioPath);
					intent.putExtra("requestCode", AUDIO);
					intent.putExtra("action", "audio");
					intent.putExtra("createOrOpen", "open");
					startActivity(intent);

				}

			});

			llAudio.addView(btnAudio);

			if (forms) {
				footerlayout.setVisibility(View.GONE);
			}
			return llAudio;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// private LinearLayout AudioNoteView(int state, final String AudioPath)
	// throws Exception {
	//
	// getVolume();
	//
	// final int PRE_RECORD = 1;
	// final int ON_RECORD = 2;
	// final int PRE_PLAY = 3;
	//
	// mpAudio = new MediaPlayer();
	//
	// recorder = new MediaRecorder();
	// recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	// recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	// recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
	//
	// final LinearLayout llAudio = new LinearLayout(context);
	// llAudio.setOrientation(LinearLayout.VERTICAL);
	// llAudio.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.MATCH_PARENT));
	// llAudio.setPadding(20, 20, 0, 0);
	//
	// final LinearLayout llayProgress = new LinearLayout(context);
	// llayProgress.setLayoutParams(new LayoutParams(
	// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	//
	// final SeekBar seekProgress = new SeekBar(context);
	// seekProgress.setLayoutParams(new LayoutParams(200,
	// LinearLayout.LayoutParams.WRAP_CONTENT));
	// llayProgress.setVisibility(View.INVISIBLE);
	// llayProgress.addView(seekProgress);
	// llayProgress.setGravity(Gravity.CENTER);
	//
	// final LinearLayout llaySoundControl = new LinearLayout(context);
	// llaySoundControl.setLayoutParams(new LayoutParams(
	// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	// llaySoundControl.setPadding(10, 10, 0, 10);
	// final TextView tvSound = new TextView(context);
	// tvSound.setText(" " + volume);
	// tvSound.setGravity(Gravity.CENTER);
	// tvSound.setTextColor(Color.BLACK);
	// tvSound.setWidth(40);
	// final Button ivSound = new Button(context);
	// ivSound.setGravity(Gravity.CENTER);
	// ivSound.setWidth(30);
	// ivSound.setHeight(30);
	// ivSound.setHint("sound");
	// ivSound.setHintTextColor(Color.TRANSPARENT);
	// ivSound.setBackgroundResource(R.drawable.vol_medium);
	// ivSound.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// String hint = ivSound.getHint().toString();
	// if (hint.equals("sound")) {
	// ivSound.setHint("mute");
	// mpAudio.setVolume(0, 0);
	// tvSound.setText(" 0");
	// ivSound.setBackgroundResource(R.drawable.vol_muted);
	// } else if (hint.equals("mute")) {
	// ivSound.setHint("sound");
	// mpAudio.setVolume(volume, volume);
	// tvSound.setText(" " + volume);
	// if (volume > 60)
	// ivSound.setBackgroundResource(R.drawable.vol_high);
	// else
	// ivSound.setBackgroundResource(R.drawable.vol_medium);
	// }
	//
	// }
	// });
	// final SeekBar seekSound = new SeekBar(context);
	// final AudioManager audioManager = (AudioManager)
	// getSystemService(Context.AUDIO_SERVICE);
	// seekSound.setMax(audioManager
	// .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
	// seekSound.setProgress(audioManager
	// .getStreamVolume(AudioManager.STREAM_MUSIC));
	// volume = seekSound.getProgress();
	// tvSound.setText(Integer.toString(seekSound.getProgress()));
	// seekSound.setDrawingCacheBackgroundColor(Color.BLUE);
	// seekSound.setPadding(10, 0, 10, 0);
	// seekSound.setLayoutParams(new LayoutParams(150, 30));
	// seekSound.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
	//
	// @Override
	// public void onStopTrackingTouch(SeekBar seekBar) {
	//
	// }
	//
	// @Override
	// public void onStartTrackingTouch(SeekBar seekBar) {
	//
	// }
	//
	// @Override
	// public void onProgressChanged(SeekBar seekBar, int progress,
	// boolean fromUser) {
	// volume = progress;
	// mpAudio.setVolume(volume, volume);
	//
	// audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
	// progress, 0);
	// tvSound.setText(" " + volume);
	// if (volume > 60)
	// ivSound.setBackgroundResource(R.drawable.vol_high);
	// else if (volume == 0)
	// ivSound.setBackgroundResource(R.drawable.vol_muted);
	// else
	// ivSound.setBackgroundResource(R.drawable.vol_medium);
	// saveVolume();
	//
	// }
	// });
	// llaySoundControl.addView(ivSound);
	// llaySoundControl.addView(seekSound);
	// llaySoundControl.addView(tvSound);
	// llaySoundControl.setVisibility(View.INVISIBLE);
	// llAudio.addView(llaySoundControl);
	//
	// final LinearLayout llayTimer = new LinearLayout(context);
	// llayTimer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT));
	// llayTimer.setGravity(Gravity.CENTER);
	// final TextView tvTimer = new TextView(context);
	// tvTimer.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
	// LayoutParams.WRAP_CONTENT));
	// tvTimer.setText("  00:00:00");
	// tvTimer.setTextColor(Color.BLACK);
	// tvTimer.setVisibility(View.GONE);
	//
	// final Chronometer stopWatch = new Chronometer(context);
	// stopWatch.setText("  00:00:00");
	//
	// stopWatch.setOnChronometerTickListener(new OnChronometerTickListener() {
	// @Override
	// public void onChronometerTick(Chronometer arg0) {
	// CharSequence text = stopWatch.getText();
	// if (text.length() == 5) {
	// stopWatch.setText("  00:" + text);
	// } else if (text.length() == 7) {
	// stopWatch.setText("  0" + text);
	// }
	// tvTimer.setText(stopWatch.getText().toString());
	// }
	// });
	//
	// stopWatch.setVisibility(View.GONE);
	// stopWatch.setGravity(Gravity.CENTER);
	//
	// llayTimer.addView(tvTimer);
	// llayTimer.addView(stopWatch);
	// llAudio.addView(llayTimer);
	//
	// final TableLayout tblControls = new TableLayout(context);
	// final TableRow trControls = new TableRow(context);
	// trControls.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT));
	// final Button btnPause = new Button(context);
	// btnPause.setBackgroundResource(R.drawable.v_pause);
	// btnPause.setHint("play");
	// btnPause.setHintTextColor(Color.TRANSPARENT);
	// trControls.addView(btnPause);
	// final Button btnStop = new Button(context);
	// btnStop.setBackgroundResource(R.drawable.v_stop);
	// trControls.addView(btnStop);
	//
	// final LinearLayout llayRecord = new LinearLayout(context);
	// llayRecord.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT));
	//
	// llayRecord.setGravity(Gravity.CENTER);
	// final Button btnAudio = new Button(context);
	// btnAudio.setHint(Integer.toString(state));
	// btnAudio.setHintTextColor(Color.TRANSPARENT);
	//
	// btnAudio.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
	// LayoutParams.WRAP_CONTENT));
	//
	// if (state == PRE_RECORD) {
	// btnAudio.setHint(Integer.toString(ON_RECORD));
	// tvTimer.setVisibility(View.VISIBLE);
	//
	// btnAudio.setBackgroundResource(R.drawable.v_stop);
	//
	// recorder.setOutputFile(AudioPath);
	// try {
	// recorder.prepare();
	// recorder.start();
	// ValueHandler.isRecordinginProgress = true;
	// stopWatch.setBase(SystemClock.elapsedRealtime());
	// stopWatch.start();
	// isAudioRecordingStart = true;
	//
	// StopAudioRecordingHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	//
	// if (msg.what == 430) {
	//
	// try {
	// if (isAudioRecordingStart) {
	// stopWatch.stop();
	// recorder.stop();
	// recorder.release();
	// ValueHandler.isRecordinginProgress = false;
	// isAudioRecordingStart = false;
	// btnAudio.setHint(Integer.toString(PRE_PLAY));
	// btnAudio.setBackgroundResource(R.drawable.v_play);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// super.handleMessage(msg);
	// }
	// };
	// } catch (IllegalStateException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// } else if (state == PRE_PLAY) {
	// tvTimer.setVisibility(View.VISIBLE);
	// mpAudio.reset();
	// try {
	//
	// mpAudio.setDataSource(AudioPath);
	// mpAudio.setLooping(false);
	// mpAudio.prepare();
	//
	// } catch (IllegalArgumentException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// } catch (IllegalStateException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// } catch (IOException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// btnAudio.setBackgroundResource(R.drawable.v_play);
	//
	// }
	//
	// mpAudio.setOnPreparedListener(new OnPreparedListener() {
	//
	// @Override
	// public void onPrepared(MediaPlayer mp) {
	// long milliseconds = mp.getDuration();
	// String seconds = ValueHandler.setLength2((int) (Math
	// .round((double) milliseconds / 1000) % 60));
	// String minutes = ValueHandler
	// .setLength2((int) ((milliseconds / (1000 * 60)) % 60));
	// String hours = ValueHandler
	// .setLength2((int) ((milliseconds / (1000 * 60 * 60)) % 24));
	// String asText = hours + ":" + minutes + ":" + seconds;
	// tvTimer.setText("  " + asText);
	// }
	// });
	//
	// btnAudio.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// switch (Integer.parseInt((String) btnAudio.getHint())) {
	// case PRE_RECORD:
	//
	// break;
	//
	// case ON_RECORD:
	// try {
	// stopWatch.stop();
	// recorder.stop();
	// recorder.release();
	// ValueHandler.isRecordinginProgress = false;
	// isAudioRecordingStart = false;
	// new File(AudioPath);
	// if (!WebServiceReferences.contextTable
	// .containsKey("notepicker")) {
	// // footerlayout.setVisibility(View.VISIBLE);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// btnAudio.setHint(Integer.toString(PRE_PLAY));
	// btnAudio.setBackgroundResource(R.drawable.v_play);
	// break;
	// case PRE_PLAY:
	// tvTimer.setVisibility(View.VISIBLE);
	//
	// try {
	// if (!mpAudio.isPlaying()) {
	//
	// mpAudio.reset();
	// mpAudio.setDataSource(AudioPath);
	// mpAudio.setLooping(false);
	// mpAudio.prepare();
	// mpAudio.start();
	// isAudioPlay = true;
	//
	// seekProgress.setMax(mpAudio.getDuration());
	// startPlayProgressUpdater();
	// } else {
	// mpAudio.reset();
	// mpAudio.start();
	// isAudioPlay = true;
	//
	// seekProgress.setMax(mpAudio.getDuration());
	// startPlayProgressUpdater();
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// try {
	// llAudio.removeView(llayRecord);
	// llAudio.addView(tblControls, 2);
	// // tblControls.setVisibility(View.VISIBLE);
	// llaySoundControl.setVisibility(View.VISIBLE);
	// llayProgress.setVisibility(View.INVISIBLE);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// break;
	//
	// default:
	// break;
	// }
	//
	// }
	//
	// private void startPlayProgressUpdater() {
	// seekProgress.setProgress(mpAudio.getCurrentPosition());
	// if (mpAudio.isPlaying()) {
	// Runnable notification = new Runnable() {
	// public void run() {
	// try {
	//
	// long milliseconds = mpAudio
	// .getCurrentPosition();
	// String seconds = ValueHandler.setLength2((int) (Math
	// .round((double) milliseconds / 1000) % 60));
	// String minutes = ValueHandler
	// .setLength2((int) ((milliseconds / (1000 * 60)) % 60));
	// String hours = ValueHandler
	// .setLength2((int) ((milliseconds / (1000 * 60 * 60)) % 24));
	//
	// String asText = hours + ":" + minutes + ":"
	// + seconds;
	// tvTimer.setText("  " + asText);
	//
	// startPlayProgressUpdater();
	// } catch (Exception e) {
	// // Log.e("Audio",
	// // "Exception:" + e.getMessage());
	// e.printStackTrace();
	// }
	// }
	// };
	// handlerSeek.postDelayed(notification, 500);
	// } else {
	// seekProgress.setProgress(mpAudio.getCurrentPosition());
	// mpAudio.pause();
	//
	// }
	// }
	// });
	//
	// AudioPlayerHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	//
	// if (msg.arg1 == 430) {
	// if (mpAudio.isPlaying()) {
	// mpAudio.stop();
	// isAudioPlay = false;
	// try {
	// mpAudio.reset();
	// mpAudio.setDataSource(AudioPath);
	// mpAudio.setLooping(false);
	// mpAudio.prepare();
	// } catch (IllegalStateException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// try {
	// btnPause.setHint("play");
	// btnPause.setBackgroundResource(R.drawable.v_pause);
	//
	// llAudio.removeView(tblControls);
	// llAudio.addView(llayRecord, 2);
	//
	// llaySoundControl.setVisibility(View.INVISIBLE);
	// llayProgress.setVisibility(View.INVISIBLE);
	// } catch (Exception e) {
	//
	// }
	// }
	//
	// super.handleMessage(msg);
	// }
	// };
	//
	// btnPause.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// if (btnPause.getHint().toString().equals("play")) {
	// mpAudio.pause();
	// btnPause.setHint("pause");
	// btnPause.setBackgroundResource(R.drawable.v_play);
	// }
	//
	// else if (btnPause.getHint().toString().equals("pause")) {
	// mpAudio.start();
	// isAudioPlay = true;
	// try {
	// startPlayProgressUpdater();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// btnPause.setHint("play");
	// btnPause.setBackgroundResource(R.drawable.v_pause);
	// }
	//
	// }
	//
	// private void startPlayProgressUpdater() throws Exception {
	// seekProgress.setProgress(mpAudio.getCurrentPosition());
	//
	// if (mpAudio.isPlaying()) {
	// Runnable notification = new Runnable() {
	// public void run() {
	// try {
	//
	// long milliseconds = mpAudio
	// .getCurrentPosition();
	// String seconds = ValueHandler.setLength2((int) (Math
	// .round((double) milliseconds / 1000) % 60));
	// String minutes = ValueHandler
	// .setLength2((int) ((milliseconds / (1000 * 60)) % 60));
	// String hours = ValueHandler
	// .setLength2((int) ((milliseconds / (1000 * 60 * 60)) % 24));
	//
	// String asText = hours + ":" + minutes + ":"
	// + seconds;
	// tvTimer.setText("  " + asText);
	//
	// startPlayProgressUpdater();
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// }
	// }
	// };
	// handlerSeek.postDelayed(notification, 500);
	// } else {
	// seekProgress.setProgress(mpAudio.getCurrentPosition());
	// mpAudio.pause();
	//
	// }
	// }
	// });
	//
	// btnStop.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// if (mpAudio.isPlaying()
	// || btnPause.getHint().toString().equals("pause")) {
	// mpAudio.stop();
	// isAudioPlay = false;
	// try {
	// mpAudio.reset();
	// mpAudio.setDataSource(AudioPath);
	// mpAudio.setLooping(false);
	// mpAudio.prepare();
	// } catch (IllegalStateException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// try {
	// btnPause.setHint("play");
	// btnPause.setBackgroundResource(R.drawable.v_pause);
	//
	// llAudio.removeView(tblControls);
	// llAudio.addView(llayRecord, 2);
	//
	// llaySoundControl.setVisibility(View.INVISIBLE);
	// llayProgress.setVisibility(View.INVISIBLE);
	// } catch (Exception e) {
	//
	// }
	// }
	// });
	//
	// mpAudio.setOnCompletionListener(new OnCompletionListener() {
	//
	// @Override
	// public void onCompletion(MediaPlayer mp) {
	// try {
	// mpAudio.reset();
	// mpAudio.setDataSource(AudioPath);
	// mpAudio.setLooping(false);
	// mpAudio.prepare();
	// isAudioPlay = false;
	// } catch (IllegalStateException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// try {
	// llAudio.removeView(tblControls);
	//
	// llAudio.addView(llayRecord, 2);
	//
	// llaySoundControl.setVisibility(View.INVISIBLE);
	// llayProgress.setVisibility(View.INVISIBLE);
	//
	// btnPause.setHint("play");
	// btnPause.setBackgroundResource(R.drawable.v_pause);
	// } catch (Exception e) {
	//
	// }
	// }
	// });
	//
	// trControls.setGravity(Gravity.CENTER);
	// trControls.setPadding(0, 10, 0, 20);
	// tblControls.addView(trControls);
	// llayRecord.addView(btnAudio);
	// llAudio.addView(llayRecord);
	//
	// llAudio.addView(llayProgress);
	//
	// return llAudio;
	//
	// }

	public String Copy(String paths) {
		// TODO Auto-generated method stub
		String destinationImagePath = null;
		try {
			File sd = Environment.getExternalStorageDirectory();
			if (sd.canWrite()) {
				String external = Environment.getExternalStorageDirectory()
						.toString();
				destinationImagePath = "/COMMedia/Instruction_image"
						+ CompleteListView.getFileName() + ".jpg";
				path = "Instruction_image" + CompleteListView.getFileName()
						+ ".jpg";

				String filepath = paths.replace(external, "");

				File source = new File(sd, filepath);
				File destination = new File(sd, destinationImagePath);
				if (source.exists()) {
					FileChannel src = new FileInputStream(source).getChannel();
					FileChannel dst = new FileOutputStream(destination)
							.getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();

				}
			}
		} catch (Exception e) {

		}

		return destinationImagePath;

	}

	private ImageView createImageView(final String Path, final String type) {
		ImageView iv = new ImageView(context);
		File CheckFile = new File(Path);
		if (CheckFile.exists()) {
			if (!WebServiceReferences.contextTable.containsKey("notepicker")) {
				// footerlayout.setVisibility(View.VISIBLE);
			}
			// showTitleView();
			// iv.setImageBitmap(image);
			imageLoader.DisplayImage(Path, iv, R.drawable.refresh);
			iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Intent in = new Intent(context, PhotoZoomActivity.class);
						in.putExtra("Photo_path", Path);
						if (type.equalsIgnoreCase("handsketch")) {
							in.putExtra("type", true);
						} else {
							in.putExtra("type", false);
						}
						in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(in);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			});
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT);
			params.gravity = Gravity.CENTER;
			iv.setLayoutParams(params);

		} else {
			iv.setBackgroundResource(R.drawable.broken);
		}
		return iv;

	}

	Bitmap img = null;

	public class bitmaploader extends AsyncTask<Uri, Void, Void> {

		@Override
		protected void onPostExecute(Void Result) {
			// TODO Auto-generated method stub
			super.onPostExecute(Result);
			try {
				cancelDialog();
				if (ComponentPath != null)
					img = callDisp.ResizeImage(ComponentPath);

				GET_RESOURCES = FROM_GALERY;
				if (img != null) {
					FileOutputStream fileOutputStream = new FileOutputStream(
							ComponentPath);
					img.compress(CompressFormat.JPEG, 75, fileOutputStream);
					contentLayout.removeAllViews();
					ishand_sketch = false;
					contentLayout.addView(createImageView(ComponentPath,
							"image"));
					btnaudio.setEnabled(false);
					btnVideo.setEnabled(false);
				} else {
					ComponentPath = null;
					btnDone.setVisibility(View.VISIBLE);
					showToast(SingleInstance.mainContext.getResources().getString(R.string.choose_image_files));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showprogress();
		}

		@Override
		protected Void doInBackground(Uri... params) {
			try {
				for (Uri uri : params) {
					ComponentPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + CompleteListView.getFileName()
							+ ".jpg";
					FileInputStream fin = (FileInputStream) getContentResolver()
							.openInputStream(uri);
					ByteArrayOutputStream straam = new ByteArrayOutputStream();
					byte[] content = new byte[1024];
					int bytesread;
					while ((bytesread = fin.read(content)) != -1) {
						straam.write(content, 0, bytesread);
					}
					byte[] bytes = straam.toByteArray();
					FileOutputStream fout = new FileOutputStream(ComponentPath);
					straam.flush();
					straam.close();
					straam = null;
					fin.close();
					fin = null;
					fout.write(bytes);
					fout.flush();
					fout.close();
					fout = null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);

			if (GET_RESOURCES == FROM_GALERY) {

				if (resultCode == Activity.RESULT_CANCELED) {
					ComponentPath = null;
					showToast(SingleInstance.mainContext.getResources().getString(R.string.choose_image_files));
				} else {

					// if (img != null) {
					// if (!img.isRecycled()) {
					// img.recycle();
					// }
					// }

					Uri selectedImageUri = data.getData();
					ComponentPath = callDisp
							.getRealPathFromURI(selectedImageUri);
					File selected_file = new File(ComponentPath);
					int length = (int) selected_file.length() / 1048576;
					if (length <= 2) {
						String[] image = Copy(ComponentPath).split("/");
						// img = callDisp.ResizeImage(ComponentPath);
						// if (img != null) {
						contentLayout.removeAllViews();
						ishand_sketch = false;
						contentLayout.addView(createImageView(ComponentPath,
								"image"));

						btnaudio.setEnabled(true);
						btnVideo.setEnabled(true);
						// } else {
						// ComponentPath = null;
						// showToast("Kindly choose image files");
						// }
					} else {
						showToast(SingleInstance.mainContext.getResources().getString(R.string.large_image));
					}

				}
			} else if (GET_RESOURCES == GALLERY_KITKAT_INTENT_CALLED) {

				if (resultCode == Activity.RESULT_CANCELED) {
					ComponentPath = null;
					btnDone.setVisibility(View.VISIBLE);
					showToast(SingleInstance.mainContext.getResources().getString(R.string.choose_image_files));
				} else {

					if (img != null) {
						if (!img.isRecycled()) {
							img.recycle();
						}
					}

					Uri selectedImageUri = data.getData();
					final int takeFlags = data.getFlags()
							& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
					getContentResolver().takePersistableUriPermission(
							selectedImageUri, takeFlags);
					ComponentPath = selectedImageUri.getPath();
					File selected_file = new File(ComponentPath);
					int length = (int) selected_file.length() / 1048576;

					if (length <= 2) {

						try {
							new bitmaploader().execute(selectedImageUri);

						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {

						showToast(SingleInstance.mainContext.getResources().getString(R.string.image_size_too_large));

					}

				}

			} else if (GET_RESOURCES == FROM_CAMERA) {
				if (resultCode == RESULT_OK) {
					// if (img != null) {
					// if (!img.isRecycled()) {
					// img.recycle();
					// }
					// }

					if (ComponentPath != null) {
						// img = callDisp.ResizeImage(ComponentPath);
						// if (img != null) {
						contentLayout.removeAllViews();
						ishand_sketch = false;
						contentLayout.addView(createImageView(ComponentPath,
								"image"));
						btnaudio.setEnabled(true);
						btnVideo.setEnabled(true);
						// } else {
						// }
					}
				}
			} else if (GET_RESOURCES == CAPTURE_VIDEO) {

				if (resultCode == Activity.RESULT_CANCELED) {
					tvTitle.setText("Note");
					showDefaultView();
					contentLayout.removeAllViews();
					contentLayout.addView(edNotes);
				} else if (resultCode == Activity.RESULT_OK) {
					Log.i("formscomp", "ok result");
					Log.i("XML", "===>Video From Component-->");
					Log.i("formscomp", "path :: " + ComponentPath);
					File fl = new File(ComponentPath + ".mp4");
					Log.i("formscomp", "path :: " + fl.getPath());
					if (fl.exists()) {
						if (fl.length() != 0) {
							CreateVideoThumbnail(ComponentPath);
							contentLayout.removeAllViews();

							contentLayout
									.addView(createVideoNote(ComponentPath));
							// footerlayout.setVisibility(View.VISIBLE);

							btnaudio.setEnabled(true);
							btnImage.setEnabled(true);
						} else {
							ComponentPath = null;
						}
					} else {
						ComponentPath = null;
					}
				}

			} else if (GET_RESOURCES == HAND_SKETCH) {

				if (resultCode == Activity.RESULT_CANCELED) {
					tvTitle.setText(SingleInstance.mainContext.getResources().getString(R.string.notes));
					showDefaultView();
					contentLayout.removeAllViews();
					contentLayout.addView(edNotes);
				} else if (resultCode == Activity.RESULT_OK) {
					tvTitle.setText(SingleInstance.mainContext.getResources().getString(R.string.hand_sketch));
					Bundle bun = data.getBundleExtra("sketch");
					ComponentPath = bun.getString("path");

					if (img != null) {
						if (!img.isRecycled()) {
							img.recycle();
						}
					}

					if (ComponentPath != null) {
						// img = callDisp.ResizeImage(ComponentPath);
						// if (img != null) {
						contentLayout.removeAllViews();
						ishand_sketch = true;
						contentLayout.addView(createImageView(ComponentPath,
								"sketch"));
						File file = new File(ComponentPath);
						path = file.getName();
						btnaudio.setEnabled(true);
						btnVideo.setEnabled(true);
						// } else {
						// }
					}
				}
			} else if (GET_RESOURCES == AUDIO) {
				try {
					contentLayout.addView(AudioNoteView(3, ComponentPath));
					// footerlayout.setVisibility(View.VISIBLE);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if (WebServiceReferences.contextTable
					.containsKey("multimediautils"))
				((MultimediaUtils) WebServiceReferences.contextTable
						.get("multimediautils")).finish();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	Bitmap bitmapThumb = null;

	private LinearLayout createVideoNote(String path) {
		try {
			final LinearLayout llayVideo = new LinearLayout(context);
			llayVideo.setOrientation(LinearLayout.VERTICAL);
			llayVideo.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			llayVideo.setGravity(Gravity.CENTER);

			llayVideo.setPadding(20, 20, 0, 0);

			if (bitmapThumb != null) {
				if (!bitmapThumb.isRecycled()) {
					bitmapThumb.recycle();
				}
			}

			if (!path.endsWith(".mp4")) {
				path = path + ".mp4";
			}

			String thumb = "";
			if (path.endsWith(".mp4")) {
				thumb = path.replace(".mp4", "");
			}
			final String filename = path;
			final File vfileCheck = new File(path);
			final File fileCheckV = new File(thumb + ".jpg");

			final ImageView ivThunb = new ImageView(context);
			ivThunb.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			if (!path.contains("ftp")) {
				if (fileCheckV.exists())
					bitmapThumb = callDisp.ResizeImage(thumb + ".jpg");

			} else {

			}

			if (vfileCheck.exists()) {
				final TableLayout tblControl = new TableLayout(context);

				ivThunb.setImageBitmap(bitmapThumb);
				ivThunb.setLayoutParams(new LinearLayout.LayoutParams(
						(int) (noScrWidth / 1.4), (int) (noScrHeight / 1.4)));

				videoView = new VideoView(context);
				ivThunb.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						isVideoPlay = true;
						tblControl.removeAllViews();
						llayVideo.addView(videoView, 0);
						llayVideo.removeView(ivThunb);
						// videoView
						// .setLayoutParams(new LinearLayout.LayoutParams(
						// (int) (noScrWidth / 1.4),
						// (int) (noScrHeight / 3.0)));
						Log.i("file", "filename :: " + filename);
						videoView.setVideoPath(filename);
						StopVideoHandler = new Handler() {
							@Override
							public void handleMessage(Message msg) {
								Log.e("handler", "Stop handler.....");
								if (msg.arg1 == 1) {
									videoPause = 0;
									if (videoView.isPlaying()) {
										videoPause = 1;
										videoView.pause();
										isVideoPlay = true;
									}
								}
								if (msg.arg1 == 2) {
									if (videoPause == 1) {
										videoView.start();
										isVideoPlay = true;
									}
								}

								super.handleMessage(msg);
							}
						};

						videoView.start();
						final Button btnPlay = new Button(context);
						btnPlay.setHint("play");
						btnPlay.setHintTextColor(Color.TRANSPARENT);
						btnPlay.setBackgroundResource(R.drawable.btn_pause_new);
						btnPlay.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Log.e("handler", "butto play.....");
								String hint = btnPlay.getHint().toString();
								if (hint.equals("play")) {
									btnPlay.setHint("pause");
									btnPlay.setBackgroundResource(R.drawable.btn_play_new);
									videoView.pause();
									isVideoPlay = true;
								} else if (hint.equals("pause")) {
									videoView.start();
									btnPlay.setBackgroundResource(R.drawable.btn_pause_new);
									btnPlay.setHint(SingleInstance.mainContext.getResources().getString(R.string.play));
									isVideoPlay = true;

								}
							}
						});

						videoView
								.setOnCompletionListener(new OnCompletionListener() {

									@Override
									public void onCompletion(MediaPlayer mp) {
										llayVideo.removeView(videoView);
										llayVideo.addView(ivThunb, 0);
										try {
											llayVideo.removeView(tblControl);
											isVideoPlay = false;
										} catch (Exception e) {
										}
									}
								});
						TableRow tr = new TableRow(context);
						tr.setGravity(Gravity.CENTER);
						Button btnStop = new Button(context);
						btnStop.setBackgroundResource(R.drawable.btn_stop_new);
						btnStop.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								videoView.stopPlayback();
								llayVideo.removeView(tblControl);
								llayVideo.removeView(videoView);
								llayVideo.addView(ivThunb, 0);
								isVideoPlay = false;
							}
						});

						Button btnFull = new Button(context);
						btnFull.setBackgroundResource(R.drawable.full_screen);
						btnFull.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								viewHandler.postDelayed(new Runnable() {

									@Override
									public void run() {
										try {
											isVideoPlay = false;
											llayVideo.removeView(tblControl);
											llayVideo.removeView(videoView);
											llayVideo.addView(ivThunb, 0);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}, 2000);
								videoView.pause();
								int pos = videoView.getCurrentPosition();
								videoView.stopPlayback();

								Intent intentVPlayer = new Intent(context,
										VideoPlayer.class);
								intentVPlayer.putExtra("File_Path", filename);
								intentVPlayer.putExtra("Player_Type",
										"Video Player");
								intentVPlayer.putExtra("time", pos);
								startActivity(intentVPlayer);
							}
						});
						tr.addView(btnPlay);
						tr.addView(btnStop);
						tr.addView(btnFull);
						tblControl.addView(tr);
						llayVideo.addView(tblControl, 1);

					}
				});

				llayVideo.addView(ivThunb);

			}

			return llayVideo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (StopVideoHandler != null) {
			if (isVideoPlay) {
				Message msg = new Message();
				msg.arg1 = 1;
				StopVideoHandler.sendMessage(msg);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;

		if (ValueHandler.isImageRotated) {
			if (contentLayout.getChildCount() != 0) {
				if (contentLayout.getChildAt(0) instanceof ImageView) {
					if (ValueHandler.imgPath.trim().length() != 0) {
						contentLayout.post(new Runnable() {

							@Override
							public void run() {
								// if (img != null) {
								// if (!img.isRecycled())
								// img.recycle();
								// }
								// img =
								// callDisp.ResizeImage(ValueHandler.imgPath
								// .trim());

								// if (img != null) {

								contentLayout.removeAllViews();
								if (ishand_sketch) {
									contentLayout.addView(createImageView(
											ValueHandler.imgPath.trim(),
											"sketch"));
								} else {
									contentLayout.addView(createImageView(
											ValueHandler.imgPath.trim(),
											"image"));
								}

								ValueHandler.imgPath = "";
							}
							// }
						});

					}

				}
			}
			ValueHandler.isImageRotated = false;
		}

		if (StopVideoHandler != null) {
			if (isVideoPlay) {
				Message msg = new Message();
				msg.arg1 = 2;
				StopVideoHandler.sendMessage(msg);
			}
		}
		super.onResume();
	}

	private boolean CreateVideoThumbnail(String strThumbPath) {
		try {
			System.out.println("create thumbnail");
			System.out.println("Thumb path" + strThumbPath);
			Log.i("formscomp", "===>1 path===>" + strThumbPath);
			MediaPlayer m = new MediaPlayer();
			m.setDataSource(strThumbPath + ".mp4");
			m.prepare();
			long milliseconds = (long) m.getDuration();
			String seconds = WebServiceReferences.setLength2((int) (Math
					.round((double) milliseconds / 1000) % 60));
			String minutes = WebServiceReferences
					.setLength2((int) ((milliseconds / (1000 * 60)) % 60));
			String hours = WebServiceReferences
					.setLength2((int) ((milliseconds / (1000 * 60 * 60)) % 24));

			String asText = hours + ":" + minutes + ":" + seconds;
			Log.e("myLog", "Duration :" + asText);

			m.reset();

			Bitmap thumbImage = ThumbnailUtils.createVideoThumbnail(
					strThumbPath + ".mp4",
					MediaStore.Images.Thumbnails.MINI_KIND);
			Log.e("lg", "?????????????????Thumb image" + thumbImage);
			if (thumbImage != null) {
				Log.i("formscomp", "===>2 path not null===>" + strThumbPath);

				thumbImage = ResizeVideoImage(thumbImage);

				Log.i("myLog", "Image Height :" + thumbImage.getHeight());
				Log.i("myLog", "Image Width  :" + thumbImage.getWidth());

				Bitmap thumb = combineImages(thumbImage, asText);

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				thumb.compress(CompressFormat.JPEG, 75, bos);
				FileOutputStream fos = new FileOutputStream(strThumbPath
						+ ".jpg");
				bos.writeTo(fos);
				bos.close();
				fos.close();
				thumbImage.recycle();
				thumb.recycle();
				return true;
			} else {
				Log.i("formscomp", "===>2 path===>Null" + strThumbPath);

				File fl = new File(strThumbPath + ".mp4");
				if (fl.exists()) {
					Log.i("formscomp", "===>3 path===>File Exists"
							+ strThumbPath);

					thumbImage = BitmapFactory.decodeResource(getResources(),
							drawable.broken);
					thumbImage = ResizeVideoImage(thumbImage);
					Log.i("myLog", "Image Height :" + thumbImage.getHeight());
					Log.i("myLog", "Image Width  :" + thumbImage.getWidth());
					thumbImage = combineImages(thumbImage, asText);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					thumbImage.compress(CompressFormat.JPEG, 75, bos);
					FileOutputStream fos = new FileOutputStream(strThumbPath
							+ ".jpg");
					bos.writeTo(fos);
					bos.close();
					fos.close();
					thumbImage.recycle();
					return true;
				} else {
					Log.i("formscomp", "===>4 path===>File Not Exists"
							+ strThumbPath);

					return false;
				}
			}

		} catch (Exception ex) {
			Log.i("formscomp", "===>5 path===>Exception" + ex.getMessage());

			Log.e("Exc", "FileNotFoundException : " + ex);
			return false;
		}

	}

	@SuppressWarnings("finally")
	private boolean createbrokenThump(String strThumbPath) {
		boolean status = false;
		try {

			File fl = new File(strThumbPath + ".mp4");
			if (fl.exists()) {

				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.broken);
				bmp = ResizeVideoImage(bmp);

				Bitmap thumb1 = combineImages(bmp, "00:00:00");
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				thumb1.compress(CompressFormat.JPEG, 75, bos);
				FileOutputStream fos = new FileOutputStream(strThumbPath
						+ ".jpg");
				bos.writeTo(fos);
				bos.close();
				fos.close();
				status = true;
				bmp.recycle();
				thumb1.recycle();
			} else {
				status = false;
			}
		} catch (FileNotFoundException ex) {
			Log.i("fromscomp", ex.getMessage());
			status = false;
		} catch (IOException ioe) {
			Log.i("fromscomp", ioe.getMessage());

			status = false;
		} catch (Exception e) {
			Log.i("fromscomp", e.getMessage());

			status = false;
		} finally {
			return (status);
		}
	}

	private Bitmap ResizeVideoImage(Bitmap bitmap) {

		int outWidth = bitmap.getWidth();
		int outHeight = bitmap.getHeight();

		if (outWidth == outHeight) {
			bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
		} else {

			if (outHeight == 200) {
				bitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight,
						true);
			} else {
				double ratio;
				// Log.i("IMG", "Demo ratio:" + ((double) 2 / (double) 3));
				if (outHeight < outWidth) {
					ratio = (double) outWidth / (double) outHeight;

					bitmap = Bitmap.createScaledBitmap(bitmap,
							(int) (200 * ratio), 200, true);
					// Log.e("IMG", "ratio:" + ratio);
				} else {
					// Log.i("IMG", "IMG Height:" + opts.outHeight);
					// Log.i("IMG", "IMG Width:" + opts.outWidth);
					ratio = (double) outWidth / (double) outHeight;

					bitmap = Bitmap.createScaledBitmap(bitmap,
							(int) Math.round(200 / ratio), 200, true);
				}

			}

		}
		return bitmap;
	}

	private Bitmap combineImages(Bitmap c, String time) {
		Bitmap cs = null;

		Bitmap bitmapPlay = BitmapFactory.decodeResource(getResources(),
				R.drawable.vplay1);
		int width, height = 0;
		width = c.getWidth();
		height = c.getHeight();
		cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas comboImage = new Canvas(cs);
		comboImage.drawBitmap(c, 0f, 0f, null);
		comboImage.drawBitmap(bitmapPlay,
				(c.getWidth() / 2 - bitmapPlay.getWidth() / 2),
				(c.getHeight() / 2 - bitmapPlay.getHeight() / 2), null);
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTypeface(Typeface.SERIF);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextSize(30);
		// comboImage.drawText(time,
		// (c.getWidth() / 2 - bitmapPlay.getWidth() / 2) - 30, (c
		// .getHeight() / 2 - bitmapPlay.getHeight() / 2), paint);

		comboImage.drawText(time, (c.getWidth() / 4), (c.getHeight() / 2),
				paint);

		return cs;
	}

	public void finishActivity() {
		try {

			if (GET_RESOURCES == TEXT) {
				if ((cbean == null)
						&& (edNotes.getText().toString().trim().length() != 0)) {
					ComponentPath = "/sdcard/COMMedia/"
							+ CompleteListView.getFileName() + ".txt";
					if (CompleteListView.textnotes == null)
						CompleteListView.textnotes = new TextNoteDatas();

					CompleteListView.textnotes.checkAndCreate(ComponentPath,
							edNotes.getText().toString().trim());
					if (WebServiceReferences.contextTable.containsKey("MAIN")) {
						if (WebServiceReferences.contextTable.get("MAIN") instanceof CompleteListView) {
							WebServiceReferences.contextTable.get("MAIN");
							if (CallDispatcher.LoginUser != null) {

							} else if (((CompleteListView) WebServiceReferences.contextTable
									.get("MAIN")).getOwner().trim().length() != 0) {

							}
						}
					}

					finish();
				} else {
					if (!ON_CREATE) {
						if (!cbean.getcomponentType().equalsIgnoreCase("call")) {

							if (!datas.trim().equals(
									edNotes.getText().toString().trim())) {

								if (CompleteListView.textnotes == null)
									CompleteListView.textnotes = new TextNoteDatas();

								CompleteListView.textnotes.checkAndCreate(
										cbean.getContentpath(), edNotes
												.getText().toString().trim());
								finish();

							} else {
								finish();
							}
						} else {
							finish();
						}
					} else {
						finish();
					}
				}

			} else if (GET_RESOURCES == CAPTURE_VIDEO) {
				if (ComponentPath != null) {
					if (!isVideoPlay) {
						File vfile = new File(ComponentPath + ".mp4");
						if (vfile.exists()) {
							// showAlert();

							if (WebServiceReferences.contextTable
									.containsKey("MAIN")) {
								if (WebServiceReferences.contextTable
										.get("MAIN") instanceof CompleteListView) {
									if (CallDispatcher.LoginUser != null) {
										WebServiceReferences.contextTable
												.get("MAIN");

										if (WebServiceReferences.contextTable
												.containsKey("notepicker")) {
											// NotePickerScreen picker =
											// (NotePickerScreen)
											// WebServiceReferences.contextTable
											// .get("notepicker");
											// picker.refreshList();
											finish();
										}
									} else if (((CompleteListView) WebServiceReferences.contextTable
											.get("MAIN")).getOwner().trim()
											.length() != 0) {

										WebServiceReferences.contextTable
												.get("MAIN");

										if (WebServiceReferences.contextTable
												.containsKey("notepicker")) {
											// NotePickerScreen picker =
											// (NotePickerScreen)
											// WebServiceReferences.contextTable
											// .get("notepicker");
											// picker.refreshList();
											finish();
										}

									}
								}
							}

							finish();
						}
					} else {
						showToast(SingleInstance.mainContext.getResources().getString(R.string.video_stop));
					}
				} else {
					if (!isVideoPlay) {

						finish();
					} else {
						showToast(SingleInstance.mainContext.getResources().getString(R.string.video_stop));
					}
				}

			} else {
				if (ComponentPath != null) {
					if (!isAudioRecordingStart) {
						if (!isAudioPlay) {
							if (!isVideoPlay) {

								File fl = new File(ComponentPath);
								if (fl.exists()) {

									if (WebServiceReferences.contextTable
											.containsKey("MAIN")) {
										if (WebServiceReferences.contextTable
												.get("MAIN") instanceof CompleteListView) {
											if (CallDispatcher.LoginUser != null) {
												WebServiceReferences.contextTable
														.get("MAIN");

												if (WebServiceReferences.contextTable
														.containsKey("notepicker")) {
													// NotePickerScreen picker =
													// (NotePickerScreen)
													// WebServiceReferences.contextTable
													// .get("notepicker");
													// picker.refreshList();
													finish();
												}
											} else if (((CompleteListView) WebServiceReferences.contextTable
													.get("MAIN")).getOwner()
													.trim().length() != 0) {
												WebServiceReferences.contextTable
														.get("MAIN");

												if (WebServiceReferences.contextTable
														.containsKey("notepicker")) {
													// NotePickerScreen picker =
													// (NotePickerScreen)
													// WebServiceReferences.contextTable
													// .get("notepicker");
													// picker.refreshList();
													finish();
												}

											}
										}
									}

									finish();
								} else {
									finish();
								}
							} else {
								showToast(SingleInstance.mainContext.getResources().getString(R.string.video_stop));
							}
						} else {
							showToast(SingleInstance.mainContext.getResources().getString(R.string.audio_is_playing_kindly_stop_that_first));
						}
					} else {
						showToast(SingleInstance.mainContext.getResources().getString(R.string.audio_stop));

					}
				} else {
					if (videoView != null) {
						if (videoView.isPlaying()) {
							videoView.stopPlayback();
						}
					}
					if (mpAudio != null) {
						if (mpAudio.isPlaying()) {
							mpAudio.stop();
						}
					}
					finish();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected static String getNoteCreateTime() {
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh:mm a");
		return sdf.format(curDate);
	}

	@Override
	protected void onDestroy() {
		if (WebServiceReferences.contextTable.containsKey("cmpinstruction")) {
			WebServiceReferences.contextTable.remove("cmpinstruction");
		}

		if (WebServiceReferences.contextTable.containsKey("a_play")) {
			WebServiceReferences.contextTable.remove("a_play");
		}

		if (callDisp != null) {
			callDisp.cmp = null;
		}

		super.onDestroy();
		if (img != null) {
			if (!img.isRecycled()) {
				img.recycle();
			}
		}
		if (bitmapThumb != null) {
			if (!bitmapThumb.isRecycled()) {
				bitmapThumb.recycle();
			}
		}

	}

	public void ShowError(String Title, String Message) {
		AlertDialog confirmation = new AlertDialog.Builder(this).create();
		confirmation.setTitle(Title);
		confirmation.setMessage(Message);
		confirmation.setCancelable(true);
		confirmation.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		confirmation.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (ComponentPath != null) {
				if (!isAudioRecordingStart) {
					if (!isAudioPlay) {
						if (!isVideoPlay) {
							finishActivitys(false);
						} else {
							ShowError(SingleInstance.mainContext.getResources().getString(R.string.video),
									SingleInstance.mainContext.getResources().getString(R.string.video_stop));
						}
					} else {
						ShowError(SingleInstance.mainContext.getResources().getString(R.string.audio),
								SingleInstance.mainContext.getResources().getString(R.string.audio_is_playing_kindly_stop_that_first));
					}
				} else {

					try {
						if (isAudioRecordingStart) {
							ShowError(SingleInstance.mainContext.getResources().getString(R.string.audio),
									SingleInstance.mainContext.getResources().getString(R.string.audio_is_playing_kindly_stop_that_first));

						} else {
							finishActivitys(false);

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				finishActivitys(false);
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	public static boolean CheckReminderIsValid(String strDate) {
		DateFormat formatter;

		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Calendar cal = Calendar.getInstance();
		String strD2 = formatter.format(cal.getTime());
		Date currentDateTime = null;
		try {
			currentDateTime = (Date) formatter.parse(strD2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date CheckDate = null;
		try {
			CheckDate = (Date) formatter.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (CheckDate.getTime() <= currentDateTime.getTime())
			return false;
		else
			return true;

	}

	private void showNoteView(String type) {
		if (type.equals("note")) {
			showDefaultView();
			contentLayout.removeAllViews();
			contentLayout.addView(edNotes);
		} else if (type.equals("audio")) {
			CompleteListView.checkDir();
			if (!isAudioRecordingStart) {
				if (!isAudioPlay) {
					if (!isVideoPlay) {
						if (ComponentPath != null) {
							File fle = new File(ComponentPath);
							if (fle.exists()) {
								fle.delete();
							}
							fle = null;
							ComponentPath = null;
						}
						if (edNotes != null) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									edNotes.getWindowToken(), 0);
							getWindow()
									.setSoftInputMode(
											WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
						}
						btnImage.setEnabled(true);
						btnVideo.setEnabled(true);
						// footerlayout.setVisibility(View.GONE);
						tvTitle.setText("Audio");
						showAudioView();
					} else {
						showToast(SingleInstance.mainContext.getResources().getString(R.string.video_stop));
					}
				} else {
					showToast(SingleInstance.mainContext.getResources().getString(R.string.audio_is_playing_kindly_stop_that_first));
				}
			} else {
				showToast(SingleInstance.mainContext.getResources().getString(R.string.audio_stop));
			}

		} else if (type.equals("video")) {

			CompleteListView.checkDir();
			if (!isAudioRecordingStart) {
				if (!isAudioPlay) {
					if (!isVideoPlay) {
						if (ComponentPath != null) {
							File fle = new File(ComponentPath);
							if (fle.exists()) {
								fle.delete();
							}
							fle = null;

						}
						if (edNotes != null) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									edNotes.getWindowToken(), 0);
							getWindow()
									.setSoftInputMode(
											WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
						}
						// footerlayout.setVisibility(View.VISIBLE);
						ComponentPath = null;
						tvTitle.setText("Video");
						String strIPath = "/sdcard/COMMedia/"
								+ "Instruction_video"
								+ CompleteListView.getFileName();
						ComponentPath = strIPath;
						path = "Instruction_video"
								+ CompleteListView.getFileName() + ".mp4";

						GET_RESOURCES = CAPTURE_VIDEO;
						Intent intent = new Intent(context,
								CustomVideoCamera.class);
						intent.putExtra("filePath", ComponentPath);
						// intent.putExtra("requestCode", CAPTURE_VIDEO);
						// intent.putExtra("action",
						// MediaStore.ACTION_VIDEO_CAPTURE);
						// intent.putExtra("createOrOpen", "create");
						startActivityForResult(intent, 5);

					} else {
						showToast(SingleInstance.mainContext.getResources().getString(R.string.video_stop));
					}
				} else {
					showToast(SingleInstance.mainContext.getResources().getString(R.string.audio_is_playing_kindly_stop_that_first));
				}
			} else {
				showToast(SingleInstance.mainContext.getResources().getString(R.string.audio_stop));
			}

		} else if (type.equals("photo")) {

			CompleteListView.checkDir();
			if (!isAudioRecordingStart) {
				if (!isAudioPlay) {
					if (!isVideoPlay) {
						if (ComponentPath != null) {
							File fle = new File(ComponentPath);
							if (fle.exists()) {
								fle.delete();
							}
							fle = null;
							ComponentPath = null;
						}
						if (edNotes != null) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									edNotes.getWindowToken(), 0);
							getWindow()
									.setSoftInputMode(
											WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
						}
						// footerlayout.setVisibility(View.VISIBLE);
						showMenu();
					} else {
						showToast(SingleInstance.mainContext.getResources().getString(R.string.video_stop));
					}
				} else {
					showToast(SingleInstance.mainContext.getResources().getString(R.string.audio_is_playing_kindly_stop_that_first));
				}
			} else {
				showToast(SingleInstance.mainContext.getResources().getString(R.string.audio_stop));
			}

		} else if (type.equalsIgnoreCase("handsketch")) {
			CompleteListView.checkDir();
			if (!isAudioRecordingStart) {
				if (!isAudioPlay) {
					if (!isVideoPlay) {
						if (ComponentPath != null) {
							File fle = new File(ComponentPath);
							if (fle.exists()) {
								fle.delete();
							}
							fle = null;
							ComponentPath = null;
						}
						if (edNotes != null) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									edNotes.getWindowToken(), 0);
							getWindow()
									.setSoftInputMode(
											WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
						}
						cbean = null;
						// footerlayout.setVisibility(View.VISIBLE);
						// btnDone.setEnabled(true);

						GET_RESOURCES = HAND_SKETCH;

						// Intent intent = new Intent(context,
						// HandSketchActivity.class);
						Intent intent = new Intent(context,
								HandSketchActivity2.class);
						startActivityForResult(intent, HAND_SKETCH);
					} else {
						showToast(SingleInstance.mainContext.getResources().getString(R.string.video_stop));
					}
				} else {
					showToast(SingleInstance.mainContext.getResources().getString(R.string.audio_is_playing_kindly_stop_that_first));
				}
			} else {
				showToast(SingleInstance.mainContext.getResources().getString(R.string.audio_stop));
			}

		}
	}

	public void showDeleteAlert() {
		try {
			AlertDialog.Builder buider = new AlertDialog.Builder(context);
			buider.setMessage(SingleInstance.mainContext.getResources().getString(R.string.save_and_goback))
					.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									saveFile();

								}
							})
					.setNegativeButton(SingleInstance.mainContext.getResources().getString(R.string.no),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									finish();
								}
							});
			AlertDialog alert = buider.create();
			alert.show();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void UploadingCompleted() {

		handler.post(new Runnable() {

			@Override
			public void run() {

				cancelDialog();

			}
		});

	}

	private void showprogress() {

		dialog = new ProgressDialog(context);
		dialog.setCancelable(true);
		dialog.setMessage("Progress ...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.show();

	}

	private void cancelDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	public void notifyResponse(String title, String Message) {
		cancelDialog();
		ShowError(title, Message);

	}

	public void incomingCallNotification() {

		try {
			if (isAudioRecordingStart)
				StopAudioRecordingHandler.sendEmptyMessage(430);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void savetextnote() {
		if (edNotes.getText().toString().trim().length() != 0) {
			if (ON_CREATE) {
				ComponentPath = "/sdcard/COMMedia/" + "Instruction_note"
						+ CompleteListView.getFileName() + ".txt";
				path = "Instruction_note" + CompleteListView.getFileName()
						+ ".txt";
			} else {
				ComponentPath = callDisp.cmp.getContentpath();
			}

			if (CompleteListView.textnotes == null)
				CompleteListView.textnotes = new TextNoteDatas();

			CompleteListView.textnotes.checkAndCreate(ComponentPath, edNotes
					.getText().toString());
			if (ON_CREATE) {
				if (WebServiceReferences.contextTable.containsKey("MAIN")) {
					if (WebServiceReferences.contextTable.get("MAIN") instanceof CompleteListView) {
						WebServiceReferences.contextTable.get("MAIN");
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(edNotes.getWindowToken(), 0);
						getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

						if (WebServiceReferences.contextTable
								.containsKey("notepicker")) {
							// NotePickerScreen picker = (NotePickerScreen)
							// WebServiceReferences.contextTable
							// .get("notepicker");
							// picker.refreshList();
							finishActivity();
						} else {

							// footerlayout.setVisibility(View.VISIBLE);
							showToast(SingleInstance.mainContext.getResources().getString(R.string.text_note_saved_succesfully)
									+ ComponentPath);
							finishActivitys(false);

							ComponentPath = null;

							// btnDone.setEnabled(true);
							ON_CREATE = false;

							// openNote();

						}
					}
				}
			} else {
				datas = edNotes.getText().toString().trim();
			}
			btnDone.setEnabled(true);
		} else {
			showToast(SingleInstance.mainContext.getResources().getString(R.string.empty_instruction_cannot_created));
		}

	}

	private boolean isReadyToPlay() {

		boolean result = false;
		if (WebServiceReferences.contextTable.containsKey("callscreen")) {
			result = false;
		} else if (ValueHandler.isRecordinginProgress) {
			result = false;
		} else {
			result = true;
		}

		return result;
	}

	public void stopPlayBck() {

		if (StopAudioRecordingHandler != null) {
			StopAudioRecordingHandler.sendEmptyMessage(430);
		}
		if (videoView != null) {
			if (videoView.isPlaying()) {
				videoView.stopPlayback();
			}
		}
		if (mpAudio != null) {
			if (mpAudio.isPlaying()) {
				mpAudio.stop();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_save_note:
			saveFile();
			break;

		default:
			break;
		}
	}

}
