package com.cg.forms;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import bsh.EvalError;
import bsh.Interpreter;

import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.HandSketchActivity2;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.PhotoZoomActivity;
import com.cg.files.CompleteListView;
import com.cg.hostedconf.AppReference;
import com.cg.timer.ReminderService;
import com.group.chat.GroupChatActivity;
import com.main.AppMainActivity;
import com.main.FormsFragment;
import com.util.CustomVideoCamera;
import com.util.SingleInstance;
import com.util.VideoPlayer;

public class FormRecordsCreators extends Activity implements OnClickListener {
	private Button btn_save = null;
	private Button btn_svcreate = null;
	private Button btn_cancl = null;
	private Button btn_quickaction = null;
	private LinearLayout records_container = null;
	private TextView rec_tite = null;
	private String[] record = null;
	private String getFormId;
	private ArrayList<String> field_list = null;
	private ArrayList<String> field_values = null;
	private HashMap<String, String> fieldvalues = null;
	private ArrayList<String[]> rec_list = null;
	private StringBuffer values;
	private AudioManager audioManager = null;
	public ImageView blob_image;
	private Handler handler = new Handler();
	boolean isreject = false;
	private Context context = null;
	private String title = null;
	private Boolean update = false;
	private Boolean issipcall = false;
	private TextView hint = null;
	public Bitmap bitmap = null;
	public boolean isUpload = true;
	private String name = null;
	private String instructions = null;
	private File vfileCheck = null;
	private int i1;
	private static final int DATE_DIALOG_ID = 0;
	private int pHour;
	private int pMinute;
	// private EditText ed_fld = null;
	private String ComponentPath = "";
	private String timeD;
	static final int TIME_DIALOG_ID = 1;
	private int mYear;
	private int mMonth;
	private int mDay;
	private MediaPlayer chatplayer = null;
	private String[] value = { "" };
	private base64Decoder decode_task = null;
	private SharedPreferences p;
	private CallDispatcher callDisp = null;
	private Handler wservice_handler = null;
	private ProgressDialog dialog = null;
	private boolean hastocreate_another = false;
	private String table_id = null;
	private String updateformid = null;
	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();
	private String updaterecordid = null;
	boolean isaudioPlaying = false;
	private HashMap<String, String> dtypes = null;
	private int selected_rowid;
	private AlertDialog alert = null;
	private String blob_path = null;
	private HashMap<String, Bitmap> bmp_container = null;
	private String[] info_lists = null;
	private String[] validate_lists = null;
	private Button btn_rejcall = null;
	private String instruction = null;
	private String errortip = null;
	private String validdata = null;
	private String entrymode = null;
	private String defaultvalue = null;
	private String entrymodefield = null;
	private String formula = null;
	private static String normaldate;
	private static String normaltime;
	private static String pickgdate;
	private static String pickldate;
	private static String pickbdate;
	private Button IMRequest;
	// public ArrayList<CallerBean> callerBean_Array = null;
	private String symbol = "";
	private String locaLpath = Environment.getExternalStorageDirectory()
			.getPath() + "/COMMedia/";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			if (savedInstanceState != null) {
				ComponentPath = (String) savedInstanceState
						.get("componentpath");

			}
			setContentView(R.layout.form_records);
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			context = this;
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

			CallDispatcher.pdialog = new ProgressDialog(context);

			WebServiceReferences.contextTable.put("frmreccreator", context);

			IMRequest = (Button) findViewById(R.id.recim);

			IMRequest.setVisibility(View.GONE);
			IMRequest.setBackgroundResource(R.drawable.one);
			btn_save = (Button) findViewById(R.id.btn_recsave);
			btn_quickaction = (Button) findViewById(R.id.btn_savequickaction);
			btn_quickaction.setOnClickListener(this);

			btn_save.setOnClickListener(this);
			btn_svcreate = (Button) findViewById(R.id.btn_recsaveandcreate);
			btn_svcreate.setOnClickListener(this);
			btn_cancl = (Button) findViewById(R.id.btn_recback);
			btn_cancl.setOnClickListener(this);

			btn_rejcall = (Button) findViewById(R.id.btn_rejcall);
			btn_rejcall.setOnClickListener(this);
			btn_rejcall.setVisibility(View.GONE);
			records_container = (LinearLayout) findViewById(R.id.llayout_holder);
			rec_tite = (TextView) findViewById(R.id.tv_rectilte);
			field_list = getIntent().getStringArrayListExtra("list");
			record = getIntent().getStringArrayExtra("recordlist");
			title = getIntent().getStringExtra("title");
			table_id = getIntent().getStringExtra("id");
			audioManager = (AudioManager) this
					.getSystemService(Context.AUDIO_SERVICE);

			if (audioManager != null)
				audioManager.setSpeakerphoneOn(false);
			updateformid = getIntent().getStringExtra("updateformid");
			updaterecordid = getIntent().getStringExtra("updatetableid");

			dtypes = (HashMap<String, String>) getIntent()
					.getSerializableExtra("types");
			rec_tite.setText(title);

			bmp_container = new HashMap<String, Bitmap>();

			btn_save.setWidth(noScrWidth - 5);
			btn_svcreate.setWidth(noScrWidth - 5);
			field_values = new ArrayList<String>();
			fieldvalues = new HashMap<String, String>();
			wservice_handler = new Handler();
			update = getIntent().getBooleanExtra("update", false);
			issipcall = getIntent().getBooleanExtra("addwithsipcall", true);
			btn_quickaction.setVisibility(View.GONE);

			if (update) {
				Log.i("HEART", "Is Update Call");
				loadformrecordsFields();
				btn_save.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.update));
				btn_svcreate.setVisibility(View.GONE);
				btn_quickaction.setVisibility(View.GONE);

			} else {
				Log.i("HEART", "Is Create Call");

				loadformFields();

				btn_save.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.save));
				btn_svcreate.setVisibility(View.VISIBLE);

			}

			if (issipcall) {
				// callDisp.SIPCallisAccepted = true;
				// callerBean_Array = (ArrayList<CallerBean>) getIntent()
				// .getSerializableExtra("call_list");

				btn_rejcall.setVisibility(View.GONE);
				// callDisp.SIPCallisAccepted = true;
				btn_rejcall.setBackgroundResource(R.drawable.hangupx);
				btn_rejcall.setTag("0");

				String buddyNames = getIntent().getStringExtra("buddyname");

				/**
				 * Need confirmation regarding call type
				 */
				// callDisp.MakeCall(1, buddyNames, context);

				// AppReference.addWithCall = true;
				// for (CallerBean calBean : callerBean_Array) {
				//
				// CommunicationBean bean = new CommunicationBean();
				// bean.setOperationType(sip_operation_types.MAKE_CALL);
				// bean.setAcc_id(AppReference.sip_accountID);
				// bean.setRealm(callDisp.getFS());
				// bean.setTonumber(calBean.getToNnumber());
				// AppReference.sipQueue.addMsg(bean);
				//
				// }

			} else {
				btn_quickaction.setVisibility(View.VISIBLE);

				callDisp.SIPCallisAccepted = false;

				btn_rejcall.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loadformrecordsFields() {
		Log.i("hs", "loadformrecordsFields");

		String text = "";
		HashMap<String, String> dtype = new HashMap<String, String>();
		dtype = callDisp.getdbHeler(context).getColumnTypesforminfo("");
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < record.length - 6; i++) {

			try {
				if (!field_list.get(i).contains("blob_")) {

					String colName = "";

					if (field_list.get(i).contains("-")) {
						field_list.get(i).split("-");
						colName = field_list.get(i);

					} else {
						colName = field_list.get(i);

					}

					info_lists = callDisp.getdbHeler(context)
							.getRecordsofforminfotable(
									title + "_" + updateformid, colName, dtype);

					Log.i("hs",
							"info_lists[3].toString() :"
									+ info_lists[3].toString());

					String dropdown = info_lists[3].toString();
					String attId = info_lists[8];
					String permission = null;
					String defaultPermission = null;
					if (attId != null) {
						permission = DBAccess.getdbHeler()
								.getFormFieldBuddyAccessSettings(table_id,
										attId);
						defaultPermission = DBAccess.getdbHeler()
								.getFormFieldDefaultAccessSettings(table_id,
										attId);
					}
					if (dropdown.equalsIgnoreCase("drop down")) {
						entrymode = info_lists[3];
						validdata = info_lists[4];
						defaultvalue = info_lists[5];
						instruction = info_lists[6] + ",";
						errortip = info_lists[7];

						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.formrecord_fields, null);
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F1")) {
							layout.setVisibility(View.GONE);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							} else if (permission.equalsIgnoreCase("F2")) {
								layout.setVisibility(View.VISIBLE);
							} else {
								layout.setVisibility(View.VISIBLE);
							}
						}
						layout.findViewById(R.id.tview_clm);
						LinearLayout layoutt = (LinearLayout) layout
								.findViewById(R.id.tview_lay);
						TextView tv1 = (TextView) layout
								.findViewById(R.id.tview_clms);
						ProgressBar progressBar = (ProgressBar) layout
								.findViewById(R.id.down_progressBar);
						ProgressBar intermediateProgress = (ProgressBar) layout
								.findViewById(R.id.progressBar2);
						intermediateProgress.setVisibility(View.GONE);
						tv1.setText(field_list.get(i));
						final String[] inst = instruction.split(",");
						final ImageView[] iv_album = new ImageView[inst.length];
						final TextView[] txt_view = new TextView[inst.length];

						StringBuffer sb = new StringBuffer();

						for (i1 = 0; i1 < inst.length; i1++) {
							instructions = inst[i1];
							if (inst[i1].contains("note")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								txt_view[i1] = new TextView(context);
								txt_view[i1].setPadding(30, 10, 30, 0);
								txt_view[i1].setText(text);

								layoutt.addView(txt_view[i1], params);

								try {
									File myFile = new File("/sdcard/COMMedia/"
											+ instructions);
									FileInputStream fIn = new FileInputStream(
											myFile);
									BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null) {
										aBuffer += aDataRow + "\n";
									}
									text = aBuffer;
									if (inst.length == 1) {
										sb.append(text);
									} else {
										sb.append(text + "\n");
									}
									myReader.close();

								} catch (Exception e) {
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);
									e.printStackTrace();
								}

								txt_view[i1].setText(text);

							}

							else if (inst[i1].contains("image")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.rsz_broken));

								iv_album[i1]
										.setContentDescription(instructions);

								iv_album[i1].setId(i1 + 1);
								final ImageView imgview = iv_album[i1];
								vfileCheck = new File(imgview
										.getContentDescription().toString());
								if (vfileCheck.exists())
									iv_album[i1].setImageBitmap(callDisp
											.ResizeImage(instructions));
								layoutt.addView(iv_album[i1], params);

								ArrayList<Object> list = new ArrayList<Object>();
								list.add(inst[i1]);
								list.add(iv_album[i1]);
								decode_task = new base64Decoder();
								decode_task.execute(list);

								imgview.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists()) {

											Intent in = new Intent(context,
													PhotoZoomActivity.class);
											in.putExtra(
													"Photo_path",

													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											in.putExtra("type", false);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivityForResult(in, 10);
										} else {

										}

									}
								});

							}

							else if (inst[i1].contains("audio")) {
								if (SingleInstance.formMultimediaRecords
										.containsKey(instructions)) {
									if (SingleInstance.formMultimediaRecords
											.get(instructions) == 1) {
										intermediateProgress
												.setVisibility(View.VISIBLE);
									} else {
										intermediateProgress
												.setVisibility(View.GONE);
									}
								} else {
									RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
											RelativeLayout.LayoutParams.WRAP_CONTENT,
											RelativeLayout.LayoutParams.WRAP_CONTENT);
									params.addRule(
											RelativeLayout.ALIGN_PARENT_TOP,
											RelativeLayout.ABOVE);
									iv_album[i1] = new ImageView(context);
									iv_album[i1].setPadding(30, 10, 30, 0);
									iv_album[i1].setImageDrawable(this
											.getResources().getDrawable(
													R.drawable.v_play));
									iv_album[i1]
											.setContentDescription(instructions);
									iv_album[i1].setId(i1 + 1);

									final ImageView img = iv_album[i1];

									layoutt.addView(iv_album[i1], params);

									chatplayer = new MediaPlayer();
									img.setTag("0");
									img.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											vfileCheck = new File(
													locaLpath
															+ img.getContentDescription()
																	.toString());

											if (img.getTag().toString()
													.equals("1")) {
												img.setTag("0");
												bitmap = BitmapFactory
														.decodeResource(
																getResources(),
																R.drawable.v_play);
												img.setImageBitmap(bitmap);
												chatplayer.stop();
												chatplayer.reset();
												isaudioPlaying = false;
											} else {
												if (!isaudioPlaying) {

													if (vfileCheck.exists()) {

														try {
															chatplayer
																	.setDataSource(locaLpath
																			+ img.getContentDescription()
																					.toString());
															chatplayer
																	.setLooping(false);

															chatplayer
																	.prepare();
															chatplayer.start();
															img.setTag("1");

															bitmap = BitmapFactory
																	.decodeResource(
																			getResources(),
																			R.drawable.v_stop);
															img.setImageBitmap(bitmap);
															isaudioPlaying = true;

														} catch (IllegalArgumentException e) {
															if (AppReference.isWriteInFile)
																AppReference.logger
																		.error(e.getMessage(),
																				e);
															e.printStackTrace();
															e.printStackTrace();
														} catch (IllegalStateException e) {
															if (AppReference.isWriteInFile)
																AppReference.logger
																		.error(e.getMessage(),
																				e);
															e.printStackTrace();
															e.printStackTrace();
														} catch (IOException e) {
															if (AppReference.isWriteInFile)
																AppReference.logger
																		.error(e.getMessage(),
																				e);
															e.printStackTrace();
															e.printStackTrace();
														}

														chatplayer
																.setOnCompletionListener(new OnCompletionListener() {

																	@Override
																	public void onCompletion(
																			MediaPlayer mp) {

																		try {
																			chatplayer
																					.reset();
																			if (bitmap != null) {
																				if (!bitmap
																						.isRecycled()) {
																					bitmap.recycle();
																				}
																			}
																			isaudioPlaying = false;
																			bitmap = BitmapFactory
																					.decodeResource(
																							getResources(),
																							R.drawable.v_play);
																			img.setImageBitmap(bitmap);
																			img.setTag("0");

																		} catch (IllegalArgumentException e) {
																			if (AppReference.isWriteInFile)
																				AppReference.logger
																						.error(e.getMessage(),
																								e);
																			e.printStackTrace();
																			e.printStackTrace();
																		} catch (IllegalStateException e) {
																			if (AppReference.isWriteInFile)
																				AppReference.logger
																						.error(e.getMessage(),
																								e);
																			e.printStackTrace();
																			e.printStackTrace();
																		}

																	}
																});

													} else {
														Toast.makeText(
																context,
																"File Downloading",
																1).show();
														downloadConfiguredNote(instructions);

													}
												} else {

													showToast(SingleInstance.mainContext
															.getResources()
															.getString(
																	R.string.please_stop_audio));
												}
											}

										}
									});
								}
							}

							else if (inst[i1].contains("video")) {
								if (SingleInstance.formMultimediaRecords
										.containsKey(instructions)) {
									if (SingleInstance.formMultimediaRecords
											.get(instructions) == 1) {
										intermediateProgress
												.setVisibility(View.VISIBLE);
									} else {
										intermediateProgress
												.setVisibility(View.GONE);
									}
								} else {
									RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
											RelativeLayout.LayoutParams.WRAP_CONTENT,
											RelativeLayout.LayoutParams.WRAP_CONTENT);
									params.addRule(
											RelativeLayout.ALIGN_PARENT_TOP,
											RelativeLayout.ABOVE);
									iv_album[i1] = new ImageView(context);
									iv_album[i1].setPadding(30, 10, 30, 0);
									iv_album[i1].setImageDrawable(this
											.getResources().getDrawable(
													R.drawable.v_play));
									iv_album[i1]
											.setContentDescription(instructions);
									iv_album[i1].setId(i1 + 1);

									final ImageView im = iv_album[i1];

									layoutt.addView(iv_album[i1], params);

									im.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											vfileCheck = new File(
													locaLpath
															+ im.getContentDescription()
																	.toString());

											if (vfileCheck.exists()) {

												if (!isaudioPlaying) {

													Intent intentVPlayer = new Intent(
															context,
															VideoPlayer.class);
													intentVPlayer
															.putExtra(
																	"File_Path",

																	locaLpath
																			+ im.getContentDescription()
																					.toString());
													intentVPlayer.putExtra(
															"Player_Type",
															"Video Player");
													startActivity(intentVPlayer);
												}

												else {
													showToast(SingleInstance.mainContext
															.getResources()
															.getString(
																	R.string.please_stop_audio));

												}

											} else {

												Toast.makeText(context,
														"File Downloading", 1)
														.show();
												downloadConfiguredNote(instructions);

											}

										}
									});
								}
							}

						}

						final ImageView im = (ImageView) layout
								.findViewById(R.id.iview_dtype);
						im.setContentDescription(validdata);
						EditText ed_fld = (EditText) layout
								.findViewById(R.id.edtxt_frmfield);
						ed_fld.setText(record[i]);

						ed_fld.setHint("Select a value from drop down");

						ed_fld.setFocusableInTouchMode(false);
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F2")) {
							im.setEnabled(false);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F2")) {
								im.setEnabled(false);
							} else if (permission.equalsIgnoreCase("F3")) {
								im.setEnabled(true);
							}
						}
						im.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								addView(v, im.getContentDescription()
										.toString());
							}
						});
						if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("INTEGER")) {
							ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);
						} else if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("nvarchar(20)")) {
							ed_fld.setInputType(InputType.TYPE_CLASS_TEXT);
						}

						layout.setId(records_container.getChildCount());
						layout.setContentDescription(entrymode);

						records_container.addView(layout);
					} else if (dropdown.equalsIgnoreCase("time")) {

						entrymode = info_lists[3];
						validdata = info_lists[4];
						defaultvalue = info_lists[5];
						instruction = info_lists[6] + ",";
						errortip = info_lists[7];

						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.formrecord_fields, null);
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F1")) {
							layout.setVisibility(View.GONE);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							} else if (permission.equalsIgnoreCase("F2")) {
								layout.setVisibility(View.VISIBLE);
							} else {
								layout.setVisibility(View.VISIBLE);
							}
						}
						layout.findViewById(R.id.tview_clm);
						LinearLayout layoutt = (LinearLayout) layout
								.findViewById(R.id.tview_lay);
						TextView tv1 = (TextView) layout
								.findViewById(R.id.tview_clms);

						tv1.setText(field_list.get(i));

						final String[] inst = instruction.split(",");
						final ImageView[] iv_album = new ImageView[inst.length];
						final TextView[] txt_view = new TextView[inst.length];

						StringBuffer sb = new StringBuffer();

						for (i1 = 0; i1 < inst.length; i1++) {
							instructions = inst[i1];
							if (inst[i1].contains("note")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								txt_view[i1] = new TextView(context);
								txt_view[i1].setPadding(30, 10, 30, 0);
								txt_view[i1].setText(text);

								layoutt.addView(txt_view[i1], params);

								try {
									File myFile = new File("/sdcard/COMMedia/"
											+ instructions);
									FileInputStream fIn = new FileInputStream(
											myFile);
									BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null) {
										aBuffer += aDataRow + "\n";
									}
									text = aBuffer;
									if (inst.length == 1) {
										sb.append(text);
									} else {
										sb.append(text + "\n");
									}
									myReader.close();

								} catch (Exception e) {
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);
									e.printStackTrace();
								}

								txt_view[i1].setText(text);

							}

							else if (inst[i1].contains("image")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.rsz_broken));

								iv_album[i1]
										.setContentDescription(instructions);

								iv_album[i1].setId(i1 + 1);
								final ImageView imgview = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								ArrayList<Object> list = new ArrayList<Object>();
								list.add(inst[i1]);
								list.add(iv_album[i1]);
								decode_task = new base64Decoder();
								decode_task.execute(list);

								vfileCheck = new File(imgview
										.getContentDescription().toString());
								imgview.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists()) {

											Intent in = new Intent(context,
													PhotoZoomActivity.class);
											in.putExtra(
													"Photo_path",

													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											in.putExtra("type", false);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivityForResult(in, 10);
										} else {

										}

									}
								});

							}

							else if (inst[i1].contains("audio")) {
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView img = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								chatplayer = new MediaPlayer();
								img.setTag("0");
								img.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ img.getContentDescription()
														.toString());

										if (img.getTag().toString().equals("1")) {
											img.setTag("0");
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_play);
											img.setImageBitmap(bitmap);
											chatplayer.stop();
											chatplayer.reset();
											isaudioPlaying = false;
										} else {
											if (!isaudioPlaying) {

												if (vfileCheck.exists()) {

													try {
														chatplayer
																.setDataSource(locaLpath
																		+ img.getContentDescription()
																				.toString());
														chatplayer
																.setLooping(false);

														chatplayer.prepare();
														chatplayer.start();
														img.setTag("1");

														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_stop);
														img.setImageBitmap(bitmap);
														isaudioPlaying = true;

													} catch (IllegalArgumentException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
														e.printStackTrace();
													} catch (IllegalStateException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
														e.printStackTrace();
													} catch (IOException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
														e.printStackTrace();
													}

													chatplayer
															.setOnCompletionListener(new OnCompletionListener() {

																@Override
																public void onCompletion(
																		MediaPlayer mp) {

																	try {
																		chatplayer
																				.reset();
																		if (bitmap != null) {
																			if (!bitmap
																					.isRecycled()) {
																				bitmap.recycle();
																			}
																		}
																		isaudioPlaying = false;
																		bitmap = BitmapFactory
																				.decodeResource(
																						getResources(),
																						R.drawable.v_play);
																		img.setImageBitmap(bitmap);
																		img.setTag("0");

																	} catch (IllegalArgumentException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																		e.printStackTrace();
																	} catch (IllegalStateException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																		e.printStackTrace();
																	}

																}
															});

												} else {
													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}
											} else {

												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));
											}
										}

									}
								});
							}

							else if (inst[i1].contains("video")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView im = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ im.getContentDescription()
														.toString());

										if (vfileCheck.exists()) {

											if (!isaudioPlaying) {

												Intent intentVPlayer = new Intent(
														context,
														VideoPlayer.class);
												intentVPlayer
														.putExtra(
																"File_Path",

																locaLpath
																		+ im.getContentDescription()
																				.toString());
												intentVPlayer.putExtra(
														"Player_Type",
														"Video Player");
												startActivity(intentVPlayer);
											}

											else {
												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));

											}

										} else {

											Toast.makeText(context,
													"File Downloading", 1)
													.show();
											downloadConfiguredNote(instructions);

										}

									}
								});

							}

						}

						final ImageView im = (ImageView) layout
								.findViewById(R.id.iview_dtype);
						im.setContentDescription(validdata);
						final EditText ed_fld = (EditText) layout
								.findViewById(R.id.edtxt_frmfield);
						ed_fld.setText(record[i]);
						ed_fld.setHint("Select a time");

						ed_fld.setFocusableInTouchMode(false);
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F2")) {
							im.setEnabled(false);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F2")) {
								im.setEnabled(false);
							} else {
								im.setEnabled(true);
							}
						}
						im.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								final Calendar cal = Calendar.getInstance();
								pHour = cal.get(Calendar.HOUR_OF_DAY);
								pMinute = cal.get(Calendar.MINUTE);

								Button btnSet;
								final TimePicker tp;
								final AlertDialog alertReminder = new AlertDialog.Builder(
										context).create();

								ScrollView tblDTPicker = (ScrollView) View
										.inflate(context,
												R.layout.timepickerdialog, null);

								btnSet = (Button) tblDTPicker
										.findViewById(R.id.btnSetDateTime);

								tp = (TimePicker) tblDTPicker
										.findViewById(R.id.timePicker);
								tp.setIs24HourView(true);
								;

								btnSet.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {

										normaltime = WebServiceReferences
												.setLength2(tp.getCurrentHour())
												+ ":"
												+ WebServiceReferences.setLength2(tp
														.getCurrentMinute());
										ed_fld.setText(normaltime);

										alertReminder.dismiss();

									}
								});
								alertReminder.setTitle("Time");
								alertReminder.setView(tblDTPicker);
								alertReminder.show();

							}
						});

						if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("INTEGER")) {
							ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);
						} else if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("nvarchar(20)")) {
							ed_fld.setInputType(InputType.TYPE_CLASS_TEXT);
						}

						layout.setId(records_container.getChildCount());
						layout.setContentDescription(entrymode);

						records_container.addView(layout);

					} else if (dropdown.equalsIgnoreCase("date")) {

						entrymode = info_lists[3];
						validdata = info_lists[4];
						defaultvalue = info_lists[5];
						instruction = info_lists[6] + ",";
						errortip = info_lists[7];

						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.formrecord_fields, null);
						layout.findViewById(R.id.tview_clm);
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F1")) {
							layout.setVisibility(View.GONE);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							} else if (permission.equalsIgnoreCase("F2")) {
								layout.setVisibility(View.VISIBLE);
							} else {
								layout.setVisibility(View.VISIBLE);
							}
						}
						LinearLayout layoutt = (LinearLayout) layout
								.findViewById(R.id.tview_lay);
						TextView tv1 = (TextView) layout
								.findViewById(R.id.tview_clms);
						tv1.setText(field_list.get(i));
						StringBuffer sb = new StringBuffer();

						final String[] inst = instruction.split(",");
						final ImageView[] iv_album = new ImageView[inst.length];
						final TextView[] txt_view = new TextView[inst.length];

						for (i1 = 0; i1 < inst.length; i1++) {
							instructions = inst[i1];
							if (inst[i1].contains("note")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								txt_view[i1] = new TextView(context);
								txt_view[i1].setPadding(30, 10, 30, 0);
								txt_view[i1].setText(text);

								layoutt.addView(txt_view[i1], params);

								try {
									File myFile = new File("/sdcard/COMMedia/"
											+ instructions);
									FileInputStream fIn = new FileInputStream(
											myFile);
									BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null) {
										aBuffer += aDataRow + "\n";
									}
									text = aBuffer;
									if (inst.length == 1) {
										sb.append(text);
									} else {
										sb.append(text + "\n");
									}
									myReader.close();

								} catch (Exception e) {
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);
									e.printStackTrace();
								}

								txt_view[i1].setText(text);

							}

							else if (inst[i1].contains("image")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.rsz_broken));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);
								final ImageView imgview = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								ArrayList<Object> list = new ArrayList<Object>();
								list.add(inst[i1]);
								list.add(iv_album[i1]);
								decode_task = new base64Decoder();
								decode_task.execute(list);

								vfileCheck = new File(imgview
										.getContentDescription().toString());
								imgview.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists()) {

											Intent in = new Intent(context,
													PhotoZoomActivity.class);
											in.putExtra(
													"Photo_path",

													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											in.putExtra("type", false);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivityForResult(in, 10);
										} else {

										}

									}
								});

							}

							else if (inst[i1].contains("audio")) {
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView img = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								chatplayer = new MediaPlayer();
								img.setTag("0");
								img.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ img.getContentDescription()
														.toString());

										if (img.getTag().toString().equals("1")) {
											img.setTag("0");
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_play);
											img.setImageBitmap(bitmap);
											chatplayer.stop();
											chatplayer.reset();
											isaudioPlaying = false;
										} else {
											if (!isaudioPlaying) {

												if (vfileCheck.exists()) {

													try {
														chatplayer
																.setDataSource(locaLpath
																		+ img.getContentDescription()
																				.toString());
														chatplayer
																.setLooping(false);

														chatplayer.prepare();
														chatplayer.start();
														img.setTag("1");

														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_stop);
														img.setImageBitmap(bitmap);
														isaudioPlaying = true;

													} catch (IllegalArgumentException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
														e.printStackTrace();
													} catch (IllegalStateException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
														e.printStackTrace();
													} catch (IOException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
														e.printStackTrace();
													}

													chatplayer
															.setOnCompletionListener(new OnCompletionListener() {

																@Override
																public void onCompletion(
																		MediaPlayer mp) {

																	try {
																		chatplayer
																				.reset();
																		if (bitmap != null) {
																			if (!bitmap
																					.isRecycled()) {
																				bitmap.recycle();
																			}
																		}
																		isaudioPlaying = false;
																		bitmap = BitmapFactory
																				.decodeResource(
																						getResources(),
																						R.drawable.v_play);
																		img.setImageBitmap(bitmap);
																		img.setTag("0");

																	} catch (IllegalArgumentException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	} catch (IllegalStateException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	}

																}
															});

												} else {
													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}
											} else {

												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));
											}
										}

									}
								});
							}

							else if (inst[i1].contains("video")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView im = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ im.getContentDescription()
														.toString());

										if (vfileCheck.exists()) {

											if (!isaudioPlaying) {

												Intent intentVPlayer = new Intent(
														context,
														VideoPlayer.class);
												intentVPlayer
														.putExtra(
																"File_Path",

																locaLpath
																		+ im.getContentDescription()
																				.toString());

												intentVPlayer.putExtra(
														"Player_Type",
														"Video Player");
												startActivity(intentVPlayer);
											}

											else {
												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));

											}

										} else {

											Toast.makeText(context,
													"File Downloading", 1)
													.show();
											downloadConfiguredNote(instructions);

										}

									}
								});

							}

						}

						final ImageView im = (ImageView) layout
								.findViewById(R.id.iview_dtype);
						im.setContentDescription(validdata);
						final EditText ed_fld = (EditText) layout
								.findViewById(R.id.edtxt_frmfield);
						ed_fld.setText(record[i]);
						ed_fld.setHint("Select a date");

						ed_fld.setFocusableInTouchMode(false);
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F2")) {
							im.setEnabled(false);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F2")) {
								im.setEnabled(false);
							} else {
								im.setEnabled(true);
							}
						}
						// if (permission != null
						// && permission.equalsIgnoreCase("F2")) {
						// im.setEnabled(false);
						// }
						im.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								final Calendar c = Calendar.getInstance();
								mYear = c.get(Calendar.YEAR);
								mMonth = c.get(Calendar.MONTH);
								mDay = c.get(Calendar.DAY_OF_MONTH);
								Button btnSet;
								final DatePicker dp;
								final AlertDialog alertReminder = new AlertDialog.Builder(
										context).create();

								ScrollView tblDTPicker = (ScrollView) View
										.inflate(context, R.layout.datepicker,
												null);

								btnSet = (Button) tblDTPicker
										.findViewById(R.id.btnSetDateTime);
								dp = (DatePicker) tblDTPicker
										.findViewById(R.id.datePicker);

								btnSet.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										normaldate = +dp.getYear()
												+ "-"
												+ WebServiceReferences.setLength2((dp
														.getMonth() + 1))
												+ "-"
												+ WebServiceReferences.setLength2(dp
														.getDayOfMonth());

										ed_fld.setText(normaldate);
										alertReminder.dismiss();

									}
								});
								alertReminder.setTitle("Date");
								alertReminder.setView(tblDTPicker);
								alertReminder.show();

							}
						});

						if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("INTEGER")) {
							ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);
						} else if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("nvarchar(20)")) {
							ed_fld.setInputType(InputType.TYPE_CLASS_TEXT);
						}

						layout.setId(records_container.getChildCount());
						layout.setContentDescription(entrymode);

						records_container.addView(layout);

					} else if (dropdown.equalsIgnoreCase("radio button")) {

						entrymode = info_lists[3];
						validdata = info_lists[4];
						defaultvalue = info_lists[5];
						instruction = info_lists[6] + ",";
						errortip = info_lists[7];
						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.formrecord_fields, null);
						// if (permission != null
						// && permission.equalsIgnoreCase("F1")) {
						// layout.setVisibility(View.GONE);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F1")) {
							layout.setVisibility(View.GONE);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							} else if (permission.equalsIgnoreCase("F2")) {
								layout.setVisibility(View.VISIBLE);
							} else {
								layout.setVisibility(View.VISIBLE);
							}
						}
						layout.findViewById(R.id.tview_clm);
						LinearLayout layoutt = (LinearLayout) layout
								.findViewById(R.id.tview_lay);
						TextView tv1 = (TextView) layout
								.findViewById(R.id.tview_clms);
						tv1.setText(field_list.get(i));

						StringBuffer sb = new StringBuffer();

						final String[] inst = instruction.split(",");
						final ImageView[] iv_album = new ImageView[inst.length];
						final TextView[] txt_view = new TextView[inst.length];

						for (i1 = 0; i1 < inst.length; i1++) {
							instructions = inst[i1];
							if (inst[i1].contains("note")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								txt_view[i1] = new TextView(context);
								txt_view[i1].setPadding(30, 10, 30, 0);
								txt_view[i1].setText(text);

								layoutt.addView(txt_view[i1], params);

								try {
									File myFile = new File("/sdcard/COMMedia/"
											+ instructions);
									FileInputStream fIn = new FileInputStream(
											myFile);
									BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null) {
										aBuffer += aDataRow + "\n";
									}
									text = aBuffer;
									if (inst.length == 1) {
										sb.append(text);
									} else {
										sb.append(text + "\n");
									}
									myReader.close();

								} catch (Exception e) {
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);
								}

								txt_view[i1].setText(text);

							}

							else if (inst[i1].contains("image")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.rsz_broken));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);
								final ImageView imgview = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								ArrayList<Object> list = new ArrayList<Object>();
								list.add(inst[i1]);
								list.add(iv_album[i1]);
								decode_task = new base64Decoder();
								decode_task.execute(list);

								vfileCheck = new File(imgview
										.getContentDescription().toString());
								imgview.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists()) {

											Intent in = new Intent(context,
													PhotoZoomActivity.class);
											in.putExtra(
													"Photo_path",

													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											in.putExtra("type", false);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivityForResult(in, 10);
										} else {

										}

									}
								});

							}

							else if (inst[i1].contains("audio")) {
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView img = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								chatplayer = new MediaPlayer();
								img.setTag("0");
								img.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ img.getContentDescription()
														.toString());

										if (img.getTag().toString().equals("1")) {
											img.setTag("0");
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_play);
											img.setImageBitmap(bitmap);
											chatplayer.stop();
											chatplayer.reset();
											isaudioPlaying = false;
										} else {
											if (!isaudioPlaying) {

												if (vfileCheck.exists()) {

													try {
														chatplayer.setDataSource(locaLpath
																+ img.getContentDescription());
														chatplayer
																.setLooping(false);

														chatplayer.prepare();
														chatplayer.start();
														img.setTag("1");

														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_stop);
														img.setImageBitmap(bitmap);
														isaudioPlaying = true;

													} catch (IllegalArgumentException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IllegalStateException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IOException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													}

													chatplayer
															.setOnCompletionListener(new OnCompletionListener() {

																@Override
																public void onCompletion(
																		MediaPlayer mp) {

																	try {
																		chatplayer
																				.reset();
																		if (bitmap != null) {
																			if (!bitmap
																					.isRecycled()) {
																				bitmap.recycle();
																			}
																		}
																		isaudioPlaying = false;
																		bitmap = BitmapFactory
																				.decodeResource(
																						getResources(),
																						R.drawable.v_play);
																		img.setImageBitmap(bitmap);
																		img.setTag("0");

																	} catch (IllegalArgumentException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	} catch (IllegalStateException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	}

																}
															});

												} else {
													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}
											} else {

												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));
											}
										}

									}
								});
							}

							else if (inst[i1].contains("video")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView im = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ im.getContentDescription()
														.toString());

										if (vfileCheck.exists()) {

											if (!isaudioPlaying) {

												Intent intentVPlayer = new Intent(
														context,
														VideoPlayer.class);
												intentVPlayer
														.putExtra(
																"File_Path",

																locaLpath
																		+ im.getContentDescription()
																				.toString());

												intentVPlayer.putExtra(
														"Player_Type",
														"Video Player");
												startActivity(intentVPlayer);
											}

											else {
												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));

											}

										} else {

											Toast.makeText(context,
													"File Downloading", 1)
													.show();
											downloadConfiguredNote(instructions);

										}

									}
								});

							}

						}

						final ImageView im = (ImageView) layout
								.findViewById(R.id.iview_dtype);
						im.setContentDescription(validdata);
						EditText ed_fld = (EditText) layout
								.findViewById(R.id.edtxt_frmfield);
						ed_fld.setFilters(getFilter());
						ed_fld.setText(record[i]);
						ed_fld.setHint("Select a value from drop down");

						ed_fld.setFocusableInTouchMode(false);
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F2")) {
							im.setEnabled(false);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F2")) {
								im.setEnabled(false);
							} else {
								im.setEnabled(true);
							}
						}
						// if (permission != null
						// && permission.equalsIgnoreCase("F2")) {
						// im.setEnabled(false);
						// }
						im.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								addView(v, im.getContentDescription()
										.toString());
							}
						});

						if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("INTEGER")) {
							ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);
						} else if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("nvarchar(20)")) {
							ed_fld.setInputType(InputType.TYPE_CLASS_TEXT);
						}

						layout.setId(records_container.getChildCount());
						layout.setContentDescription(entrymode);

						records_container.addView(layout);
					} else if (dropdown.equalsIgnoreCase("date and time")) {

						entrymode = info_lists[3];
						validdata = info_lists[4];
						defaultvalue = info_lists[5];
						instruction = info_lists[6] + ",";
						errortip = info_lists[7];

						if (validdata.length() == 0) {
							validdata = "Date and Time";

						}

						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.formrecord_fields, null);
						// if (permission != null
						// && permission.equalsIgnoreCase("F1")) {
						// layout.setVisibility(View.GONE);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F1")) {
							layout.setVisibility(View.GONE);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							} else if (permission.equalsIgnoreCase("F2")) {
								layout.setVisibility(View.VISIBLE);
							} else {
								layout.setVisibility(View.VISIBLE);
							}
						}
						layout.findViewById(R.id.tview_clm);
						LinearLayout layoutt = (LinearLayout) layout
								.findViewById(R.id.tview_lay);
						TextView tv1 = (TextView) layout
								.findViewById(R.id.tview_clms);
						tv1.setText(field_list.get(i));

						StringBuffer sb = new StringBuffer();

						final String[] inst = instruction.split(",");
						final ImageView[] iv_album = new ImageView[inst.length];
						final TextView[] txt_view = new TextView[inst.length];

						for (i1 = 0; i1 < inst.length; i1++) {
							instructions = inst[i1];
							if (inst[i1].contains("note")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								txt_view[i1] = new TextView(context);
								txt_view[i1].setPadding(30, 10, 30, 0);
								txt_view[i1].setText(text);

								layoutt.addView(txt_view[i1], params);

								try {
									File myFile = new File("/sdcard/COMMedia/"
											+ instructions);
									FileInputStream fIn = new FileInputStream(
											myFile);
									BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null) {
										aBuffer += aDataRow + "\n";
									}
									text = aBuffer;
									if (inst.length == 1) {
										sb.append(text);
									} else {
										sb.append(text + "\n");
									}
									myReader.close();

								} catch (Exception e) {
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);
								}

								txt_view[i1].setText(text);

							}

							else if (inst[i1].contains("image")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.rsz_broken));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);
								final ImageView imgview = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								ArrayList<Object> list = new ArrayList<Object>();
								list.add(inst[i1]);
								list.add(iv_album[i1]);
								decode_task = new base64Decoder();
								decode_task.execute(list);

								vfileCheck = new File(imgview
										.getContentDescription().toString());
								imgview.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists()) {

											Intent in = new Intent(context,
													PhotoZoomActivity.class);
											in.putExtra(
													"Photo_path",

													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											in.putExtra("type", false);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivityForResult(in, 10);
										} else {

										}

									}
								});

							}

							else if (inst[i1].contains("audio")) {
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView img = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								chatplayer = new MediaPlayer();
								img.setTag("0");
								img.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ img.getContentDescription()
														.toString());

										if (img.getTag().toString().equals("1")) {
											img.setTag("0");
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_play);
											img.setImageBitmap(bitmap);
											chatplayer.stop();
											chatplayer.reset();
											isaudioPlaying = false;
										} else {
											if (!isaudioPlaying) {

												if (vfileCheck.exists()) {

													try {
														chatplayer
																.setDataSource(locaLpath
																		+ img.getContentDescription()
																				.toString());
														chatplayer
																.setLooping(false);

														chatplayer.prepare();
														chatplayer.start();
														img.setTag("1");

														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_stop);
														img.setImageBitmap(bitmap);
														isaudioPlaying = true;

													} catch (IllegalArgumentException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IllegalStateException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IOException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													}

													chatplayer
															.setOnCompletionListener(new OnCompletionListener() {

																@Override
																public void onCompletion(
																		MediaPlayer mp) {

																	try {
																		chatplayer
																				.reset();
																		if (bitmap != null) {
																			if (!bitmap
																					.isRecycled()) {
																				bitmap.recycle();
																			}
																		}
																		isaudioPlaying = false;
																		bitmap = BitmapFactory
																				.decodeResource(
																						getResources(),
																						R.drawable.v_play);
																		img.setImageBitmap(bitmap);
																		img.setTag("0");

																	} catch (IllegalArgumentException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	} catch (IllegalStateException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	}

																}
															});

												} else {
													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}
											} else {

												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));
											}
										}

									}
								});
							}

							else if (inst[i1].contains("video")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView im = iv_album[i1];

								layoutt.addView(iv_album[i1], params);
								vfileCheck = new File(im
										.getContentDescription().toString());
								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ im.getContentDescription()
														.toString());

										if (vfileCheck.exists()) {

											if (!isaudioPlaying) {

												Intent intentVPlayer = new Intent(
														context,
														VideoPlayer.class);
												intentVPlayer
														.putExtra(
																"File_Path",
																locaLpath
																		+ im.getContentDescription()
																				.toString());

												intentVPlayer.putExtra(
														"Player_Type",
														"Video Player");
												startActivity(intentVPlayer);
											}

											else {
												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));

											}

										} else {

											Toast.makeText(context,
													"File Downloading", 1)
													.show();
											downloadConfiguredNote(instructions);

										}

									}
								});

							}

						}

						final ImageView im = (ImageView) layout
								.findViewById(R.id.iview_dtype);
						im.setContentDescription(validdata);
						final EditText ed_fld = (EditText) layout
								.findViewById(R.id.edtxt_frmfield);
						ed_fld.setText(record[i]);
						ed_fld.setHint("Select a Date and Time");

						ed_fld.setFocusableInTouchMode(false);
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F2")) {
							im.setEnabled(false);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F2")) {
								im.setEnabled(false);
							} else {
								im.setEnabled(true);
							}
						}
						// if (permission != null
						// && permission.equalsIgnoreCase("F2")) {
						// im.setEnabled(false);
						// }
						im.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								final String vaString = im
										.getContentDescription().toString();
								if (vaString.length() > 0) {
									value = vaString.replace("GT", "GT ")
											.replace("LT", "LT ")
											.replace("BW", "BW ")
											.replace("GT ", "GT ")
											.replace("LT ", "LT ")
											.replace("BW ", "BW ").split(" ");

									if (value.length == 0) {
										value = vaString.replace("GT", "GT ")
												.replace("LT", "LT ")
												.replace("BW", "BW ")
												.split(" ");

									}

								}
								if (vaString.length() == 0
										|| vaString
												.equalsIgnoreCase("date and time")) {

									Button btnSet;
									final DatePicker dp;
									final TimePicker tp;
									final AlertDialog alertReminder = new AlertDialog.Builder(
											context).create();

									ScrollView tblDTPicker = (ScrollView) View
											.inflate(context,
													R.layout.date_time_picker,
													null);

									btnSet = (Button) tblDTPicker
											.findViewById(R.id.btnSetDateTime);
									dp = (DatePicker) tblDTPicker
											.findViewById(R.id.datePicker);
									tp = (TimePicker) tblDTPicker
											.findViewById(R.id.timePicker);
									tp.setIs24HourView(true);
									;
									btnSet.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											pickgdate = +dp.getYear()
													+ "-"
													+ WebServiceReferences.setLength2((dp
															.getMonth() + 1))
													+ "-"
													+ WebServiceReferences.setLength2(dp
															.getDayOfMonth())
													+ " "
													+ WebServiceReferences.setLength2(tp
															.getCurrentHour())
													+ ":"
													+ WebServiceReferences.setLength2(tp
															.getCurrentMinute());
											ed_fld.setText(pickgdate);

										}
									});
									alertReminder.setTitle("Date and Time");
									alertReminder.setView(tblDTPicker);
									alertReminder.show();

								} else if (value[0].equals("GT")) {
									Button btnSet;
									final DatePicker dp;
									final TimePicker tp;
									final AlertDialog alertReminder = new AlertDialog.Builder(
											context).create();

									ScrollView tblDTPicker = (ScrollView) View
											.inflate(context,
													R.layout.date_time_picker,
													null);

									btnSet = (Button) tblDTPicker
											.findViewById(R.id.btnSetDateTime);
									dp = (DatePicker) tblDTPicker
											.findViewById(R.id.datePicker);
									tp = (TimePicker) tblDTPicker
											.findViewById(R.id.timePicker);
									tp.setIs24HourView(true);

									btnSet.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											pickgdate = +dp.getYear()
													+ "-"
													+ WebServiceReferences.setLength2((dp
															.getMonth() + 1))
													+ "-"
													+ WebServiceReferences.setLength2(dp
															.getDayOfMonth())
													+ " "
													+ WebServiceReferences.setLength2(tp
															.getCurrentHour())
													+ ":"
													+ WebServiceReferences.setLength2(tp
															.getCurrentMinute());

											if (callDisp
													.CheckGreaterDateandTime(
															pickgdate,
															vaString.replace(
																	"GT", "")
																	.trim())) {

												alertReminder.dismiss();
												Intent reminderIntent = new Intent(
														context,
														ReminderService.class);
												startService(reminderIntent);

												ed_fld.setText(pickgdate);
											} else {
												showToast("Kindly select Greater than date from "
														+ value[1]
														+ " "
														+ value[2]);

											}

										}
									});
									alertReminder.setTitle("Date and Time");
									alertReminder.setView(tblDTPicker);
									alertReminder.show();

								}
								// else if (validdata.equals("LT" + lesstime)) {
								else if (value[0].equals("LT")) {
									Button btnSet;
									final DatePicker dp;
									final TimePicker tp;
									final AlertDialog alertReminder = new AlertDialog.Builder(
											context).create();

									ScrollView tblDTPicker = (ScrollView) View
											.inflate(context,
													R.layout.date_time_picker,
													null);

									btnSet = (Button) tblDTPicker
											.findViewById(R.id.btnSetDateTime);
									dp = (DatePicker) tblDTPicker
											.findViewById(R.id.datePicker);
									tp = (TimePicker) tblDTPicker
											.findViewById(R.id.timePicker);
									tp.setIs24HourView(true);

									btnSet.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											pickldate = +dp.getYear()
													+ "-"
													+ WebServiceReferences.setLength2((dp
															.getMonth() + 1))
													+ "-"
													+ WebServiceReferences.setLength2(dp
															.getDayOfMonth())
													+ " "
													+ WebServiceReferences.setLength2(tp
															.getCurrentHour())
													+ ":"
													+ WebServiceReferences.setLength2(tp
															.getCurrentMinute());

											if (callDisp.CheckLessDateandTime(
													pickldate, vaString
															.replace("LT", "")
															.trim())) {
												alertReminder.dismiss();
												Intent reminderIntent = new Intent(
														context,
														ReminderService.class);
												startService(reminderIntent);

												ed_fld.setText(pickldate);
											} else {
												showToast("Kindly select Less than date from  "
														+ value[1]
														+ " "
														+ value[2]);

											}
										}
									});
									alertReminder.setTitle("Date and Time");
									alertReminder.setView(tblDTPicker);
									alertReminder.show();
								} else if (value[0].equals("BW")) {
									final String[] date = validdata
											.replace("and", ",")
											.replace("BW", "").trim()
											.split(",");

									Button btnSet;
									final DatePicker dp;
									final TimePicker tp;
									final AlertDialog alertReminder = new AlertDialog.Builder(
											context).create();

									ScrollView tblDTPicker = (ScrollView) View
											.inflate(context,
													R.layout.date_time_picker,
													null);

									btnSet = (Button) tblDTPicker
											.findViewById(R.id.btnSetDateTime);
									dp = (DatePicker) tblDTPicker
											.findViewById(R.id.datePicker);
									tp = (TimePicker) tblDTPicker
											.findViewById(R.id.timePicker);
									tp.setIs24HourView(true);
									;

									btnSet.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											pickbdate = +dp.getYear()
													+ "-"
													+ WebServiceReferences.setLength2((dp
															.getMonth() + 1))
													+ "-"
													+ WebServiceReferences.setLength2(dp
															.getDayOfMonth())
													+ " "
													+ WebServiceReferences.setLength2(tp
															.getCurrentHour())
													+ ":"
													+ WebServiceReferences.setLength2(tp
															.getCurrentMinute());

											if (callDisp
													.Checkbetweendateandtime(
															pickbdate, date[0],
															date[1])) {
												alertReminder.dismiss();
												Intent reminderIntent = new Intent(
														context,
														ReminderService.class);
												startService(reminderIntent);

												ed_fld.setText(pickbdate);
											}

											else {
												showToast("Kindly select in between date from  "
														+ value[1]
														+ " "
														+ value[2]
														+ " to "
														+ value[4]
														+ " "
														+ value[5]);

											}
										}
									});
									alertReminder.setTitle("Date and Time");
									alertReminder.setView(tblDTPicker);
									alertReminder.show();
								}

							}
						});

						if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("INTEGER")) {
							ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);
						} else if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("nvarchar(20)")) {
							ed_fld.setInputType(InputType.TYPE_CLASS_TEXT);
						}

						layout.setId(records_container.getChildCount());
						layout.setContentDescription(entrymode);

						records_container.addView(layout);

					} else if (dropdown.equalsIgnoreCase("numeric")) {

						entrymode = info_lists[3];
						validdata = info_lists[4];
						defaultvalue = info_lists[5];
						instruction = info_lists[6] + "," + ",";
						errortip = info_lists[7];

						value = validdata.split(" ");

						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.formrecord_fields, null);
						// if (permission != null
						// && permission.equalsIgnoreCase("F1")) {
						// layout.setVisibility(View.GONE);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F1")) {
							layout.setVisibility(View.GONE);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							} else if (permission.equalsIgnoreCase("F2")) {
								layout.setVisibility(View.VISIBLE);
							} else {
								layout.setVisibility(View.VISIBLE);
							}
						}
						layout.findViewById(R.id.tview_clm);
						LinearLayout layoutt = (LinearLayout) layout
								.findViewById(R.id.tview_lay);
						TextView tv1 = (TextView) layout
								.findViewById(R.id.tview_clms);
						tv1.setText(field_list.get(i));

						final String[] inst = instruction.split(",");
						final ImageView[] iv_album = new ImageView[inst.length];
						final TextView[] txt_view = new TextView[inst.length];

						StringBuffer sb = new StringBuffer();

						for (i1 = 0; i1 < inst.length; i1++) {
							instructions = inst[i1];
							if (inst[i1].contains("note")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								txt_view[i1] = new TextView(context);
								txt_view[i1].setPadding(30, 10, 30, 0);
								txt_view[i1].setText(text);

								layoutt.addView(txt_view[i1], params);

								try {
									File myFile = new File("/sdcard/COMMedia/"
											+ instructions);
									FileInputStream fIn = new FileInputStream(
											myFile);
									BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null) {
										aBuffer += aDataRow + "\n";
									}
									text = aBuffer;
									if (inst.length == 1) {
										sb.append(text);
									} else {
										sb.append(text + "\n");
									}
									myReader.close();

								} catch (Exception e) {
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);
								}

								txt_view[i1].setText(text);

							}

							else if (inst[i1].contains("image")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.rsz_broken));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView imgview = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								ArrayList<Object> list = new ArrayList<Object>();
								list.add(inst[i1]);
								list.add(iv_album[i1]);
								decode_task = new base64Decoder();
								decode_task.execute(list);

								vfileCheck = new File(imgview
										.getContentDescription().toString());
								imgview.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists()) {

											Intent in = new Intent(context,
													PhotoZoomActivity.class);
											in.putExtra(
													"Photo_path",

													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											in.putExtra("type", false);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivityForResult(in, 10);
										} else {

										}

									}
								});

							}

							else if (inst[i1].contains("audio")) {
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView img = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								chatplayer = new MediaPlayer();
								img.setTag("0");
								img.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ img.getContentDescription()
														.toString());

										if (img.getTag().toString().equals("1")) {
											img.setTag("0");
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_play);
											img.setImageBitmap(bitmap);
											chatplayer.stop();
											chatplayer.reset();
											isaudioPlaying = false;
										} else {
											if (!isaudioPlaying) {

												if (vfileCheck.exists()) {

													try {
														chatplayer
																.setDataSource(locaLpath
																		+ img.getContentDescription()
																				.toString());
														chatplayer
																.setLooping(false);

														chatplayer.prepare();
														chatplayer.start();
														img.setTag("1");

														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_stop);
														img.setImageBitmap(bitmap);
														isaudioPlaying = true;

													} catch (IllegalArgumentException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IllegalStateException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IOException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													}

													chatplayer
															.setOnCompletionListener(new OnCompletionListener() {

																@Override
																public void onCompletion(
																		MediaPlayer mp) {

																	try {
																		chatplayer
																				.reset();
																		if (bitmap != null) {
																			if (!bitmap
																					.isRecycled()) {
																				bitmap.recycle();
																			}
																		}
																		isaudioPlaying = false;
																		bitmap = BitmapFactory
																				.decodeResource(
																						getResources(),
																						R.drawable.v_play);
																		img.setImageBitmap(bitmap);
																		img.setTag("0");

																	} catch (IllegalArgumentException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	} catch (IllegalStateException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	}

																}
															});

												} else {
													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}
											} else {

												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));
											}
										}

									}
								});
							}

							else if (inst[i1].contains("video")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView im = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ im.getContentDescription()
														.toString());

										if (vfileCheck.exists()) {

											if (!isaudioPlaying) {

												Intent intentVPlayer = new Intent(
														context,
														VideoPlayer.class);
												intentVPlayer
														.putExtra(
																"File_Path",

																locaLpath
																		+ im.getContentDescription()
																				.toString());

												intentVPlayer.putExtra(
														"Player_Type",
														"Video Player");
												startActivity(intentVPlayer);
											}

											else {
												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));

											}

										} else {

											Toast.makeText(context,
													"File Downloading", 1)
													.show();
											downloadConfiguredNote(instructions);

										}

									}
								});

							}

						}

						final ImageView im = (ImageView) layout
								.findViewById(R.id.iview_dtype);
						im.setContentDescription(validdata);
						im.setVisibility(View.GONE);

						EditText ed_fld = (EditText) layout
								.findViewById(R.id.edtxt_frmfield);
						ed_fld.setText(record[i]);
						ed_fld.setHint(SingleInstance.mainContext
								.getResources().getString(
										R.string.please_enter_number_here));
						ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);
						InputFilter[] FilterArray = new InputFilter[1];
						FilterArray[0] = new InputFilter.LengthFilter(10);
						ed_fld.setFilters(FilterArray);
						ed_fld.setFocusableInTouchMode(true);

						layout.setId(records_container.getChildCount());
						layout.setContentDescription(entrymode);

						records_container.addView(layout);

					} else if (dropdown.equalsIgnoreCase("current location")) {

						entrymode = info_lists[3];
						validdata = info_lists[4];
						defaultvalue = info_lists[5];
						instruction = info_lists[6] + ",";
						errortip = info_lists[7];

						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.formrecord_fields, null);
						// if (permission != null
						// && permission.equalsIgnoreCase("F1")) {
						// layout.setVisibility(View.GONE);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F1")) {
							layout.setVisibility(View.GONE);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							} else if (permission.equalsIgnoreCase("F2")) {
								layout.setVisibility(View.VISIBLE);
							} else {
								layout.setVisibility(View.VISIBLE);
							}
						}
						layout.findViewById(R.id.tview_clm);
						LinearLayout layoutt = (LinearLayout) layout
								.findViewById(R.id.tview_lay);
						TextView tv1 = (TextView) layout
								.findViewById(R.id.tview_clms);
						tv1.setText(field_list.get(i));

						StringBuffer sb = new StringBuffer();

						final String[] inst = instruction.split(",");
						final ImageView[] iv_album = new ImageView[inst.length];
						final TextView[] txt_view = new TextView[inst.length];

						for (i1 = 0; i1 < inst.length; i1++) {
							instructions = inst[i1];
							if (inst[i1].contains("note")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								txt_view[i1] = new TextView(context);
								txt_view[i1].setPadding(30, 10, 30, 0);
								txt_view[i1].setText(text);

								layoutt.addView(txt_view[i1], params);

								try {
									File myFile = new File("/sdcard/COMMedia/"
											+ instructions);
									FileInputStream fIn = new FileInputStream(
											myFile);
									BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null) {
										aBuffer += aDataRow + "\n";
									}
									text = aBuffer;
									if (inst.length == 1) {
										sb.append(text);
									} else {
										sb.append(text + "\n");
									}
									myReader.close();

								} catch (Exception e) {
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);
								}

								txt_view[i1].setText(text);

							}

							else if (inst[i1].contains("image")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.rsz_broken));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);
								final ImageView imgview = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								ArrayList<Object> list = new ArrayList<Object>();
								list.add(inst[i1]);
								list.add(iv_album[i1]);
								decode_task = new base64Decoder();
								decode_task.execute(list);

								vfileCheck = new File(imgview
										.getContentDescription().toString());
								imgview.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists()) {

											Intent in = new Intent(context,
													PhotoZoomActivity.class);
											in.putExtra(
													"Photo_path",

													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											in.putExtra("type", false);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivityForResult(in, 10);
										} else {

										}

									}
								});

							}

							else if (inst[i1].contains("audio")) {
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView img = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								chatplayer = new MediaPlayer();
								img.setTag("0");
								img.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ img.getContentDescription()
														.toString());

										if (img.getTag().toString().equals("1")) {
											img.setTag("0");
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_play);
											img.setImageBitmap(bitmap);
											chatplayer.stop();
											chatplayer.reset();
											isaudioPlaying = false;
										} else {
											if (!isaudioPlaying) {

												if (vfileCheck.exists()) {

													try {
														chatplayer
																.setDataSource(locaLpath
																		+ img.getContentDescription()
																				.toString());
														chatplayer
																.setLooping(false);

														chatplayer.prepare();
														chatplayer.start();
														img.setTag("1");

														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_stop);
														img.setImageBitmap(bitmap);
														isaudioPlaying = true;

													} catch (IllegalArgumentException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IllegalStateException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IOException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													}

													chatplayer
															.setOnCompletionListener(new OnCompletionListener() {

																@Override
																public void onCompletion(
																		MediaPlayer mp) {

																	try {
																		chatplayer
																				.reset();
																		if (bitmap != null) {
																			if (!bitmap
																					.isRecycled()) {
																				bitmap.recycle();
																			}
																		}
																		isaudioPlaying = false;
																		bitmap = BitmapFactory
																				.decodeResource(
																						getResources(),
																						R.drawable.v_play);
																		img.setImageBitmap(bitmap);
																		img.setTag("0");

																	} catch (IllegalArgumentException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	} catch (IllegalStateException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	}

																}
															});

												} else {
													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}
											} else {

												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));
											}
										}

									}
								});
							}

							else if (inst[i1].contains("video")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView im = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ im.getContentDescription()
														.toString());

										if (vfileCheck.exists()) {

											if (!isaudioPlaying) {

												Intent intentVPlayer = new Intent(
														context,
														VideoPlayer.class);
												intentVPlayer
														.putExtra(
																"File_Path",

																locaLpath
																		+ im.getContentDescription()
																				.toString());

												intentVPlayer.putExtra(
														"Player_Type",
														"Video Player");
												startActivity(intentVPlayer);
											}

											else {
												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));

											}

										} else {

											Toast.makeText(context,
													"File Downloading", 1)
													.show();
											downloadConfiguredNote(instructions);

										}

									}
								});

							}

						}

						final ImageView im = (ImageView) layout
								.findViewById(R.id.iview_dtype);
						im.setContentDescription(validdata);
						im.setVisibility(View.GONE);

						final EditText ed_fld = (EditText) layout
								.findViewById(R.id.edtxt_frmfield);
						ed_fld.setText(record[i]);
						ed_fld.setHint(SingleInstance.mainContext
								.getResources().getString(
										R.string.tap_to_get_current_location));
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F2")) {
							ed_fld.setEnabled(false);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F2")) {
								ed_fld.setEnabled(false);
							} else {
								ed_fld.setEnabled(true);
							}
						}
						// if (permission != null
						// && permission.equalsIgnoreCase("F2")) {
						// ed_fld.setEnabled(false);
						// }
						ed_fld.setFocusableInTouchMode(false);
						ed_fld.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (CallDispatcher.latitude == 0.0
										&& CallDispatcher.longitude == 0.0) {
									showToast("Sorry! Turn On Location Service ");
								} else {
									String values = CallDispatcher.latitude
											+ " and "
											+ CallDispatcher.longitude;
									ed_fld.setText(values);
								}

							}
						});

						layout.setId(records_container.getChildCount());
						layout.setContentDescription(entrymode);

						records_container.addView(layout);

					} else if (dropdown.equalsIgnoreCase("current date")) {

						entrymode = info_lists[3];
						validdata = info_lists[4];
						defaultvalue = info_lists[5];
						instruction = info_lists[6] + ",";
						errortip = info_lists[7];

						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.formrecord_fields, null);
						// if (permission != null
						// && permission.equalsIgnoreCase("F1")) {
						// layout.setVisibility(View.GONE);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F1")) {
							layout.setVisibility(View.GONE);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							} else if (permission.equalsIgnoreCase("F2")) {
								layout.setVisibility(View.VISIBLE);
							} else {
								layout.setVisibility(View.VISIBLE);
							}
						}
						layout.findViewById(R.id.tview_clm);
						LinearLayout layoutt = (LinearLayout) layout
								.findViewById(R.id.tview_lay);
						TextView tv1 = (TextView) layout
								.findViewById(R.id.tview_clms);
						tv1.setText(field_list.get(i));

						StringBuffer sb = new StringBuffer();

						final String[] inst = instruction.split(",");
						final ImageView[] iv_album = new ImageView[inst.length];
						final TextView[] txt_view = new TextView[inst.length];

						for (i1 = 0; i1 < inst.length; i1++) {
							instructions = inst[i1];
							if (inst[i1].contains("note")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								txt_view[i1] = new TextView(context);
								txt_view[i1].setPadding(30, 10, 30, 0);
								txt_view[i1].setText(text);

								layoutt.addView(txt_view[i1], params);

								try {
									File myFile = new File("/sdcard/COMMedia/"
											+ instructions);
									FileInputStream fIn = new FileInputStream(
											myFile);
									BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null) {
										aBuffer += aDataRow + "\n";
									}
									text = aBuffer;
									if (inst.length == 1) {
										sb.append(text);
									} else {
										sb.append(text + "\n");
									}
									myReader.close();

								} catch (Exception e) {
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);
								}

								txt_view[i1].setText(text);

							}

							else if (inst[i1].contains("image")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.rsz_broken));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);
								final ImageView imgview = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								ArrayList<Object> list = new ArrayList<Object>();
								list.add(inst[i1]);
								list.add(iv_album[i1]);
								decode_task = new base64Decoder();
								decode_task.execute(list);

								vfileCheck = new File(imgview
										.getContentDescription().toString());
								imgview.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists()) {

											Intent in = new Intent(context,
													PhotoZoomActivity.class);
											in.putExtra(
													"Photo_path",

													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											in.putExtra("type", false);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivityForResult(in, 10);
										} else {

										}

									}
								});

							}

							else if (inst[i1].contains("audio")) {
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView img = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								chatplayer = new MediaPlayer();
								img.setTag("0");
								img.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ img.getContentDescription()
														.toString());

										if (img.getTag().toString().equals("1")) {
											img.setTag("0");
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_play);
											img.setImageBitmap(bitmap);
											chatplayer.stop();
											chatplayer.reset();
											isaudioPlaying = false;
										} else {
											if (!isaudioPlaying) {

												if (vfileCheck.exists()) {

													try {
														chatplayer
																.setDataSource(locaLpath
																		+ img.getContentDescription()
																				.toString());
														chatplayer
																.setLooping(false);

														chatplayer.prepare();
														chatplayer.start();
														img.setTag("1");

														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_stop);
														img.setImageBitmap(bitmap);
														isaudioPlaying = true;

													} catch (IllegalArgumentException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IllegalStateException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IOException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													}

													chatplayer
															.setOnCompletionListener(new OnCompletionListener() {

																@Override
																public void onCompletion(
																		MediaPlayer mp) {

																	try {
																		chatplayer
																				.reset();
																		if (bitmap != null) {
																			if (!bitmap
																					.isRecycled()) {
																				bitmap.recycle();
																			}
																		}
																		isaudioPlaying = false;
																		bitmap = BitmapFactory
																				.decodeResource(
																						getResources(),
																						R.drawable.v_play);
																		img.setImageBitmap(bitmap);
																		img.setTag("0");

																	} catch (IllegalArgumentException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	} catch (IllegalStateException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	}

																}
															});

												} else {
													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}
											} else {

												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));
											}
										}

									}
								});
							}

							else if (inst[i1].contains("video")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView im = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ im.getContentDescription()
														.toString());

										if (vfileCheck.exists()) {

											if (!isaudioPlaying) {

												Intent intentVPlayer = new Intent(
														context,
														VideoPlayer.class);
												intentVPlayer
														.putExtra(
																"File_Path",

																locaLpath
																		+ im.getContentDescription()
																				.toString());

												intentVPlayer.putExtra(
														"Player_Type",
														"Video Player");
												startActivity(intentVPlayer);
											}

											else {
												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));

											}

										} else {

											Toast.makeText(context,
													"File Downloading", 1)
													.show();
											downloadConfiguredNote(instructions);

										}

									}
								});

							}

						}

						final ImageView im = (ImageView) layout
								.findViewById(R.id.iview_dtype);
						im.setContentDescription(validdata);
						im.setVisibility(View.GONE);

						final EditText ed_fld = (EditText) layout
								.findViewById(R.id.edtxt_frmfield);
						ed_fld.setText(record[i]);
						ed_fld.setHint("Tap to get current date");
						// if (permission != null
						// && permission.equalsIgnoreCase("F2")) {
						// ed_fld.setEnabled(false);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F2")) {
							ed_fld.setEnabled(false);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F2")) {
								ed_fld.setEnabled(false);
							} else {
								ed_fld.setEnabled(true);
							}
						}
						ed_fld.setFocusableInTouchMode(false);
						ed_fld.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								ed_fld.setText(getCurrentDate());

							}
						});

						layout.setId(records_container.getChildCount());
						layout.setContentDescription(entrymode);

						records_container.addView(layout);

					}

					else if (dropdown.equalsIgnoreCase("current time")) {

						entrymode = info_lists[3];
						validdata = info_lists[4];
						defaultvalue = info_lists[5];
						instruction = info_lists[6] + ",";
						errortip = info_lists[7];

						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.formrecord_fields, null);
						// if (permission != null
						// && permission.equalsIgnoreCase("F1")) {
						// layout.setVisibility(View.GONE);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F1")) {
							layout.setVisibility(View.GONE);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							} else if (permission.equalsIgnoreCase("F2")) {
								layout.setVisibility(View.VISIBLE);
							} else {
								layout.setVisibility(View.VISIBLE);
							}
						}
						layout.findViewById(R.id.tview_clm);
						LinearLayout layoutt = (LinearLayout) layout
								.findViewById(R.id.tview_lay);
						TextView tv1 = (TextView) layout
								.findViewById(R.id.tview_clms);
						tv1.setText(field_list.get(i));

						StringBuffer sb = new StringBuffer();

						final String[] inst = instruction.split(",");
						final ImageView[] iv_album = new ImageView[inst.length];
						final TextView[] txt_view = new TextView[inst.length];

						for (i1 = 0; i1 < inst.length; i1++) {
							instructions = inst[i1];
							if (inst[i1].contains("note")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								txt_view[i1] = new TextView(context);
								txt_view[i1].setPadding(30, 10, 30, 0);
								txt_view[i1].setText(text);

								layoutt.addView(txt_view[i1], params);

								try {
									File myFile = new File("/sdcard/COMMedia/"
											+ instructions);
									FileInputStream fIn = new FileInputStream(
											myFile);
									BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null) {
										aBuffer += aDataRow + "\n";
									}
									text = aBuffer;
									if (inst.length == 1) {
										sb.append(text);
									} else {
										sb.append(text + "\n");
									}
									myReader.close();

								} catch (Exception e) {
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);
								}

								txt_view[i1].setText(text);

							}

							else if (inst[i1].contains("image")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.rsz_broken));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);
								final ImageView imgview = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								ArrayList<Object> list = new ArrayList<Object>();
								list.add(inst[i1]);
								list.add(iv_album[i1]);
								decode_task = new base64Decoder();
								decode_task.execute(list);

								vfileCheck = new File(imgview
										.getContentDescription().toString());
								imgview.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists()) {

											Intent in = new Intent(context,
													PhotoZoomActivity.class);
											in.putExtra(
													"Photo_path",

													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											in.putExtra("type", false);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivityForResult(in, 10);
										} else {

										}

									}
								});

							}

							else if (inst[i1].contains("audio")) {
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView img = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								chatplayer = new MediaPlayer();
								img.setTag("0");
								img.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ img.getContentDescription()
														.toString());

										if (img.getTag().toString().equals("1")) {
											img.setTag("0");
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_play);
											img.setImageBitmap(bitmap);
											chatplayer.stop();
											chatplayer.reset();
											isaudioPlaying = false;
										} else {
											if (!isaudioPlaying) {

												if (vfileCheck.exists()) {

													try {
														chatplayer
																.setDataSource(locaLpath
																		+ img.getContentDescription()
																				.toString());
														chatplayer
																.setLooping(false);

														chatplayer.prepare();
														chatplayer.start();
														img.setTag("1");

														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_stop);
														img.setImageBitmap(bitmap);
														isaudioPlaying = true;

													} catch (IllegalArgumentException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IllegalStateException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IOException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													}

													chatplayer
															.setOnCompletionListener(new OnCompletionListener() {

																@Override
																public void onCompletion(
																		MediaPlayer mp) {

																	try {
																		chatplayer
																				.reset();
																		if (bitmap != null) {
																			if (!bitmap
																					.isRecycled()) {
																				bitmap.recycle();
																			}
																		}
																		isaudioPlaying = false;
																		bitmap = BitmapFactory
																				.decodeResource(
																						getResources(),
																						R.drawable.v_play);
																		img.setImageBitmap(bitmap);
																		img.setTag("0");

																	} catch (IllegalArgumentException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	} catch (IllegalStateException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	}

																}
															});

												} else {
													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}
											} else {

												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));
											}
										}

									}
								});
							}

							else if (inst[i1].contains("video")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView im = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ im.getContentDescription()
														.toString());

										if (vfileCheck.exists()) {

											if (!isaudioPlaying) {

												Intent intentVPlayer = new Intent(
														context,
														VideoPlayer.class);
												intentVPlayer
														.putExtra(
																"File_Path",

																locaLpath
																		+ im.getContentDescription()
																				.toString());
												intentVPlayer.putExtra(
														"Player_Type",
														"Video Player");
												startActivity(intentVPlayer);
											}

											else {
												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));

											}

										} else {

											Toast.makeText(context,
													"File Downloading", 1)
													.show();
											downloadConfiguredNote(instructions);

										}

									}
								});

							}

						}

						final ImageView im = (ImageView) layout
								.findViewById(R.id.iview_dtype);
						im.setContentDescription(validdata);
						im.setVisibility(View.GONE);

						final EditText ed_fld = (EditText) layout
								.findViewById(R.id.edtxt_frmfield);
						ed_fld.setText(record[i]);

						ed_fld.setHint("Tap to get current time");

						ed_fld.setFocusableInTouchMode(false);
						// if (permission != null
						// && permission.equalsIgnoreCase("F2")) {
						// ed_fld.setEnabled(false);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F2")) {
							ed_fld.setEnabled(false);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F2")) {
								ed_fld.setEnabled(false);
							} else {
								ed_fld.setEnabled(true);
							}
						}
						ed_fld.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								ed_fld.setText(getCurrentTime());

							}
						});

						layout.setId(records_container.getChildCount());
						layout.setContentDescription(entrymode);

						records_container.addView(layout);

					} else if (dropdown.equalsIgnoreCase("compute")) {

						entrymode = info_lists[3];
						validdata = info_lists[4];
						defaultvalue = info_lists[5];
						instruction = info_lists[6] + ",";
						errortip = info_lists[7];

						value = validdata.split(" ");
						Log.i("forms123", "value compute value : " + value);
						for (int j = 0; j < value.length; j++) {

							if (value[0].equalsIgnoreCase("NM")) {
								entrymodefield = "numeric";
								formula = validdata.replace("NM", "");

							} else if (value[0].equalsIgnoreCase("FT")) {
								entrymodefield = "free text";
								formula = validdata.replace("FT", "");

							} else if (value[0].equalsIgnoreCase("DT")) {
								entrymodefield = "date";
								formula = validdata.replace("DT", "");

							}

						}
						Log.i("forms123", "compute formula : " + formula);
						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.formrecord_fields, null);
						// if (permission != null
						// && permission.equalsIgnoreCase("F1")) {
						// layout.setVisibility(View.GONE);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F1")) {
							layout.setVisibility(View.GONE);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							} else if (permission.equalsIgnoreCase("F2")) {
								layout.setVisibility(View.VISIBLE);
							} else {
								layout.setVisibility(View.VISIBLE);
							}
						}
						layout.findViewById(R.id.tview_clm);
						LinearLayout layoutt = (LinearLayout) layout
								.findViewById(R.id.tview_lay);
						TextView tv1 = (TextView) layout
								.findViewById(R.id.tview_clms);
						tv1.setText(field_list.get(i));

						final EditText ed_fld1 = (EditText) layout
								.findViewById(R.id.edtxt_frmfield);
						ed_fld1.setFocusableInTouchMode(false);
						ed_fld1.setContentDescription(formula);
						ed_fld1.setTag(entrymodefield);
						ed_fld1.setHint(SingleInstance.mainContext
								.getResources().getString(
										R.string.tap_to_get_value_computed));
						// if (permission != null
						// && permission.equalsIgnoreCase("F2")) {
						// ed_fld1.setEnabled(false);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F2")) {
							ed_fld1.setEnabled(false);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F2")) {
								ed_fld1.setEnabled(false);
							} else {
								ed_fld1.setEnabled(true);
							}
						}
						ed_fld1.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								String entrymodefields = ed_fld1.getTag()
										.toString();
								if (entrymodefields.equalsIgnoreCase("Numeric")) {
									if (ed_fld1.getContentDescription()
											.toString() != null
											&& !ed_fld1.getContentDescription()
													.toString()
													.equalsIgnoreCase("null")) {
										getDisplay(entrymodefields, ed_fld1
												.getContentDescription()
												.toString());

										try {

											Interpreter interpreter = new Interpreter();
											interpreter
													.eval("result=" + symbol);
											ed_fld1.setText(""
													+ interpreter.get("result"));

										}

										catch (EvalError e) {
											if (AppReference.isWriteInFile)
												AppReference.logger.error(
														e.getMessage(), e);
											e.printStackTrace();
										}
									}
								} else if (entrymodefields
										.equalsIgnoreCase("Free Text")) {
									if (ed_fld1.getContentDescription()
											.toString() != null
											&& !ed_fld1.getContentDescription()
													.toString()
													.equalsIgnoreCase("null")) {
										getDisplay(entrymodefields, ed_fld1
												.getContentDescription()
												.toString());
										ed_fld1.setText(symbol);
									}
								}

								else if (entrymodefields
										.equalsIgnoreCase("Date")) {
									if (ed_fld1.getContentDescription()
											.toString() != null
											&& !ed_fld1.getContentDescription()
													.toString()
													.equalsIgnoreCase("null")) {
										getDisplay(entrymodefields, ed_fld1
												.getContentDescription()
												.toString());
										ed_fld1.setText(symbol);
									}
								}
							}
						});

						StringBuffer sb = new StringBuffer();

						final String[] inst = instruction.split(",");
						final ImageView[] iv_album = new ImageView[inst.length];
						final TextView[] txt_view = new TextView[inst.length];

						for (i1 = 0; i1 < inst.length; i1++) {
							instructions = inst[i1];
							if (inst[i1].contains("note")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								txt_view[i1] = new TextView(context);
								txt_view[i1].setPadding(30, 10, 30, 0);
								txt_view[i1].setText(text);

								layoutt.addView(txt_view[i1], params);

								try {
									File myFile = new File("/sdcard/COMMedia/"
											+ instructions);
									FileInputStream fIn = new FileInputStream(
											myFile);
									BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null) {
										aBuffer += aDataRow + "\n";
									}
									text = aBuffer;
									if (inst.length == 1) {
										sb.append(text);
									} else {
										sb.append(text + "\n");
									}
									myReader.close();

								} catch (Exception e) {
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);
								}

								txt_view[i1].setText(text);

							}

							else if (inst[i1].contains("image")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.rsz_broken));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);
								final ImageView imgview = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								ArrayList<Object> list = new ArrayList<Object>();
								list.add(inst[i1]);
								list.add(iv_album[i1]);
								decode_task = new base64Decoder();
								decode_task.execute(list);

								vfileCheck = new File(imgview
										.getContentDescription().toString());
								imgview.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists()) {

											Intent in = new Intent(context,
													PhotoZoomActivity.class);
											in.putExtra(
													"Photo_path",

													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											in.putExtra("type", false);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivityForResult(in, 10);
										} else {

										}

									}
								});

							}

							else if (inst[i1].contains("audio")) {
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView img = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								chatplayer = new MediaPlayer();
								img.setTag("0");
								img.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ img.getContentDescription()
														.toString());

										if (img.getTag().toString().equals("1")) {
											img.setTag("0");
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_play);
											img.setImageBitmap(bitmap);
											chatplayer.stop();
											chatplayer.reset();
											isaudioPlaying = false;
										} else {
											if (!isaudioPlaying) {

												if (vfileCheck.exists()) {

													try {
														chatplayer
																.setDataSource(locaLpath
																		+ img.getContentDescription()
																				.toString());
														chatplayer
																.setLooping(false);

														chatplayer.prepare();
														chatplayer.start();
														img.setTag("1");

														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_stop);
														img.setImageBitmap(bitmap);
														isaudioPlaying = true;

													} catch (IllegalArgumentException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IllegalStateException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IOException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													}

													chatplayer
															.setOnCompletionListener(new OnCompletionListener() {

																@Override
																public void onCompletion(
																		MediaPlayer mp) {

																	try {
																		chatplayer
																				.reset();
																		if (bitmap != null) {
																			if (!bitmap
																					.isRecycled()) {
																				bitmap.recycle();
																			}
																		}
																		isaudioPlaying = false;
																		bitmap = BitmapFactory
																				.decodeResource(
																						getResources(),
																						R.drawable.v_play);
																		img.setImageBitmap(bitmap);
																		img.setTag("0");

																	} catch (IllegalArgumentException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	} catch (IllegalStateException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	}

																}
															});

												} else {
													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}
											} else {

												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));
											}
										}

									}
								});
							}

							else if (inst[i1].contains("video")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView im = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ im.getContentDescription()
														.toString());

										if (vfileCheck.exists()) {

											if (!isaudioPlaying) {

												Intent intentVPlayer = new Intent(
														context,
														VideoPlayer.class);
												intentVPlayer
														.putExtra(
																"File_Path",

																locaLpath
																		+ im.getContentDescription()
																				.toString());
												intentVPlayer.putExtra(
														"Player_Type",
														"Video Player");
												startActivity(intentVPlayer);
											}

											else {
												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));

											}

										} else {

											Toast.makeText(context,
													"File Downloading", 1)
													.show();
											downloadConfiguredNote(instructions);

										}

									}
								});

							}

						}

						TextView hint = (TextView) layout
								.findViewById(R.id.tview_clm_hint);

						if (errortip.length() == 0) {
							hint.setVisibility(View.GONE);
						} else {
							hint.setVisibility(View.VISIBLE);
							hint.setText("Error Hint:" + errortip);
							hint.setTextColor(getResources().getColor(
									R.color.orange));
						}

						final ImageView im = (ImageView) layout
								.findViewById(R.id.iview_dtype);
						im.setContentDescription(validdata);
						im.setVisibility(View.GONE);

						if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("INTEGER")) {
							ed_fld1.setInputType(InputType.TYPE_CLASS_NUMBER);
						} else if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("nvarchar(20)")) {
							ed_fld1.setInputType(InputType.TYPE_CLASS_TEXT);
						}

						layout.setId(records_container.getChildCount());
						layout.setContentDescription(entrymode);

						records_container.addView(layout);

					} else {

						entrymode = info_lists[3];
						validdata = info_lists[4];
						defaultvalue = info_lists[5];
						instruction = info_lists[6] + ",";
						errortip = info_lists[7];

						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.formrecord_fields, null);
						// if (permission != null
						// && permission.equalsIgnoreCase("F1")) {
						// layout.setVisibility(View.GONE);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F1")) {
							layout.setVisibility(View.GONE);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							} else if (permission.equalsIgnoreCase("F2")) {
								layout.setVisibility(View.VISIBLE);
							} else {
								layout.setVisibility(View.VISIBLE);
							}
						}
						layout.findViewById(R.id.tview_clm);
						LinearLayout layoutt = (LinearLayout) layout
								.findViewById(R.id.tview_lay);
						TextView tv1 = (TextView) layout
								.findViewById(R.id.tview_clms);
						tv1.setText(field_list.get(i));
						StringBuffer sb = new StringBuffer();
						// if (permission != null
						// && permission.equalsIgnoreCase("F2")) {
						// tv1.setEnabled(false);
						// tv1.setFocusable(false);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F2")) {
							tv1.setEnabled(false);
							tv1.setFocusable(false);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F2")) {
								tv1.setEnabled(false);
								tv1.setFocusable(false);
							} else {
								tv1.setEnabled(true);
								tv1.setFocusable(false);
							}
						}
						Log.i("hs", "instruction :" + instruction);

						final String[] inst = instruction.split(",");
						final ImageView[] iv_album = new ImageView[inst.length];
						final TextView[] txt_view = new TextView[inst.length];
						Log.i("hs", "inst :" + inst);

						for (i1 = 0; i1 < inst.length; i1++) {
							instructions = inst[i1];
							if (inst[i1].contains("note")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								txt_view[i1] = new TextView(context);
								txt_view[i1].setPadding(30, 10, 30, 0);
								txt_view[i1].setText(text);

								layoutt.addView(txt_view[i1], params);

								try {
									File myFile = new File("/sdcard/COMMedia/"
											+ instructions);
									FileInputStream fIn = new FileInputStream(
											myFile);
									BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null) {
										aBuffer += aDataRow + "\n";
									}
									text = aBuffer;
									if (inst.length == 1) {
										sb.append(text);
									} else {
										sb.append(text + "\n");
									}
									myReader.close();

								} catch (Exception e) {
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);
								}

								txt_view[i1].setText(text);

							}

							else if (inst[i1].contains("image")) {
								Log.i("hs", "else : image");
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1]
										.setBackgroundResource(R.drawable.rsz_broken);
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);
								final ImageView imgview = iv_album[i1];
								vfileCheck = new File(imgview
										.getContentDescription().toString());
								if (vfileCheck.exists())
									iv_album[i1].setImageBitmap(callDisp
											.ResizeImage(imgview
													.getContentDescription()
													.toString()));

								layoutt.addView(iv_album[i1], params);

								ArrayList<Object> list = new ArrayList<Object>();
								list.add(inst[i1]);
								list.add(iv_album[i1]);
								decode_task = new base64Decoder();
								decode_task.execute(list);

								imgview.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists()) {

											Intent in = new Intent(context,
													PhotoZoomActivity.class);
											in.putExtra(
													"Photo_path",

													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											in.putExtra("type", false);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivityForResult(in, 10);
										} else {

										}

									}
								});

							}

							else if (inst[i1].contains("audio")) {
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView img = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								chatplayer = new MediaPlayer();
								img.setTag("0");
								img.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ img.getContentDescription()
														.toString());

										if (img.getTag().toString().equals("1")) {
											img.setTag("0");
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_play);
											img.setImageBitmap(bitmap);
											chatplayer.stop();
											chatplayer.reset();
											isaudioPlaying = false;
										} else {
											if (!isaudioPlaying) {

												if (vfileCheck.exists()) {

													try {
														chatplayer
																.setDataSource(locaLpath
																		+ img.getContentDescription()
																				.toString());
														chatplayer
																.setLooping(false);

														chatplayer.prepare();
														chatplayer.start();
														img.setTag("1");

														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_stop);
														img.setImageBitmap(bitmap);
														isaudioPlaying = true;

													} catch (IllegalArgumentException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IllegalStateException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IOException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													}

													chatplayer
															.setOnCompletionListener(new OnCompletionListener() {

																@Override
																public void onCompletion(
																		MediaPlayer mp) {

																	try {
																		chatplayer
																				.reset();
																		if (bitmap != null) {
																			if (!bitmap
																					.isRecycled()) {
																				bitmap.recycle();
																			}
																		}
																		isaudioPlaying = false;
																		bitmap = BitmapFactory
																				.decodeResource(
																						getResources(),
																						R.drawable.v_play);
																		img.setImageBitmap(bitmap);
																		img.setTag("0");

																	} catch (IllegalArgumentException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	} catch (IllegalStateException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	}

																}
															});

												} else {
													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}
											} else {

												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));
											}
										}

									}
								});
							}

							else if (inst[i1].contains("video")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView im = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ im.getContentDescription()
														.toString());

										if (vfileCheck.exists()) {

											if (!isaudioPlaying) {

												Intent intentVPlayer = new Intent(
														context,
														VideoPlayer.class);
												intentVPlayer
														.putExtra(
																"File_Path",

																locaLpath
																		+ im.getContentDescription()
																				.toString());

												intentVPlayer.putExtra(
														"Player_Type",
														"Video Player");
												startActivity(intentVPlayer);
											}

											else {
												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));

											}

										} else {

											Toast.makeText(context,
													"File Downloading", 1)
													.show();
											downloadConfiguredNote(instructions);

										}

									}
								});

							}

						}

						final TextView hint = (TextView) layout
								.findViewById(R.id.tview_clm_hint);
						hint.setTextColor(getResources().getColor(
								R.color.orange));
						hint.setVisibility(View.GONE);
						// if (permission != null
						// && permission.equalsIgnoreCase("F1")) {
						// layout.setVisibility(View.GONE);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F1")) {
							layout.setVisibility(View.GONE);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							} else if (permission.equalsIgnoreCase("F2")) {
								layout.setVisibility(View.VISIBLE);
							} else {
								layout.setVisibility(View.VISIBLE);
							}
						}
						final ImageView im = (ImageView) layout
								.findViewById(R.id.iview_dtype);
						im.setContentDescription(validdata);
						im.setVisibility(View.GONE);

						EditText ed_fld = (EditText) layout
								.findViewById(R.id.edtxt_frmfield);
						ed_fld.setText(record[i]);
						ed_fld.setHint("Enter Text ");
						// if (permission != null
						// && permission.equalsIgnoreCase("F2")) {
						// ed_fld.setEnabled(false);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F2")) {
							ed_fld.setEnabled(false);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F2")) {
								ed_fld.setEnabled(false);
							} else {
								ed_fld.setEnabled(true);
							}
						}
						ed_fld.setFocusableInTouchMode(true);
						ed_fld.addTextChangedListener(new TextWatcher() {

							@Override
							public void onTextChanged(CharSequence s,
									int start, int before, int count) {
								if (s.length() == 0) {
									hint.setVisibility(View.GONE);
								}
							}

							@Override
							public void beforeTextChanged(CharSequence s,
									int start, int count, int after) {

							}

							@Override
							public void afterTextChanged(Editable s) {

							}
						});
						if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("INTEGER")) {
							ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);
						} else if (validdata.equalsIgnoreCase("email")) {
							ed_fld.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
						}

						else if (validdata.equalsIgnoreCase("NO SPECIAL CHAR")) {
							ed_fld.setFilters(getNoSpclchar());
						} else if (dtypes.get(field_list.get(i)).trim()
								.equalsIgnoreCase("nvarchar(20)")) {
							ed_fld.setInputType(InputType.TYPE_CLASS_TEXT);
						}

						layout.setId(records_container.getChildCount());
						layout.setContentDescription(entrymode);

						records_container.addView(layout);

					}

				} else {

					info_lists = callDisp.getdbHeler(context)
							.getRecordsofforminfotable(
									title + "_" + updateformid,
									field_list.get(i), dtype);
					if (info_lists != null) {
						instruction = info_lists[6] + ",";
						String attId = info_lists[8];
						String permission = null;
						String defaultPermission = null;

						if (attId != null) {
							permission = DBAccess.getdbHeler()
									.getFormFieldBuddyAccessSettings(table_id,
											attId);
							defaultPermission = DBAccess.getdbHeler()
									.getFormFieldDefaultAccessSettings(
											table_id, attId);
						}
						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.blob_row, null);
						// if (permission != null
						// && permission.equalsIgnoreCase("F1")) {
						// layout.setVisibility(View.GONE);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F1")) {
							layout.setVisibility(View.GONE);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							} else if (permission.equalsIgnoreCase("F2")) {
								layout.setVisibility(View.VISIBLE);
							} else {
								layout.setVisibility(View.VISIBLE);
							}
						}
						LayoutParams paramss = new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT);
						paramss.setMargins(0, 10, 0, 0);
						TextView tv = (TextView) layout
								.findViewById(R.id.tview_clmblob);
						tv.setText(field_list.get(i).replace("blob_", ""));

						View view = (View) layout.findViewById(R.id.view_inst);

						ImageView option_selector = (ImageView) layout
								.findViewById(R.id.iview_dtype);
						LinearLayout blob_layout = (LinearLayout) layout
								.findViewById(R.id.blob_layout);
						blob_layout.setVisibility(View.VISIBLE);
						ImageView blob_image = (ImageView) layout
								.findViewById(R.id.iview_blobimage);
						option_selector.setContentDescription("multimedia");
						String blob_path = record[i];
						blob_image.setContentDescription(record[i]);

						Log.i("hs", "blob_path :" + blob_path);

						// bitmap = callDisp.ResizeImage(blob_path);
						try {
							if (blob_path.contains(".jpg")) {
								// bitmap = callDisp.ResizeImage(blob_path);
								// blob_image.setImageBitmap(bitmap);
								File imgFile = new File(locaLpath + blob_path);

								if (imgFile.exists()) {

									bitmap = BitmapFactory.decodeFile(imgFile
											.getAbsolutePath());

									// ImageView myImage = (ImageView)
									// findViewById(R.id.imageviewTest);

									blob_image.setImageBitmap(bitmap);

								}

								// File filehs = new File(blob_path);

								// if (filehs.exists()) {
								//
								// Uri uri = Uri.fromFile(filehs);
								// blob_image.setImageURI(uri);
								// layout.setLayoutParams(paramss);
								// // bitmap = callDisp.ResizeImage(blob_path);
								// //
								// callDisp.changemyPictureOrientation(bitmap,
								// blob_path);
								// // blob_image.setImageBitmap(bitmap);
								//
								// // if (bitmap != null && bitmap.isRecycled())
								// // bitmap.recycle();
								// // bitmap = null;
								// // uploadConfiguredNote(blob_path);
								// // LinearLayout blob_row = (LinearLayout)
								// records_container
								// // .getChildAt(selected_rowid);
								// // LinearLayout blob_layout = (LinearLayout)
								// blob_row
								// // .findViewById(R.id.blob_layout);
								// // ImageView blob_image = (ImageView)
								// blob_row
								// // .findViewById(R.id.iview_blobimage);
								// // setBlobImage(blob_image,
								// Integer.toString(layout.getId()));
								// // blob_layout.setVisibility(View.VISIBLE);
								//
								// }

							} else if (blob_path.contains("MAD_")
									|| blob_path.contains("MVD_")) {

								bitmap = BitmapFactory.decodeResource(
										getResources(), R.drawable.v_play);
								blob_image.setImageBitmap(bitmap);

							} else {

								bitmap = BitmapFactory.decodeResource(
										getResources(), R.drawable.v_play);
								blob_image.setVisibility(View.GONE);
							}
							// if (bitmap == null) {
							// downloadConfiguredNote(record[i]);
							// if (record[i].contains("MVD_")) {
							// downloadConfiguredNote(record[i] + ".mp4");
							// }
							//
							// }
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						LinearLayout layoutt = (LinearLayout) layout
								.findViewById(R.id.tview_lay);
						layoutt.findViewById(R.id.tview_clms);
						StringBuffer sb = new StringBuffer();

						final String[] inst = instruction.split(",");
						final ImageView[] iv_album = new ImageView[inst.length];
						final TextView[] txt_view = new TextView[inst.length];

						if (inst.length > 0) {
							view.setVisibility(View.VISIBLE);
						} else {
							view.setVisibility(View.GONE);

						}
						for (i1 = 0; i1 < inst.length; i1++) {
							instructions = inst[i1];
							if (inst[i1].contains("note")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								txt_view[i1] = new TextView(context);
								txt_view[i1].setPadding(30, 10, 30, 0);
								txt_view[i1].setText(text);

								layoutt.addView(txt_view[i1], params);

								try {
									File myFile = new File("/sdcard/COMMedia/"
											+ instructions);
									FileInputStream fIn = new FileInputStream(
											myFile);
									BufferedReader myReader = new BufferedReader(
											new InputStreamReader(fIn));
									String aDataRow = "";
									String aBuffer = "";
									while ((aDataRow = myReader.readLine()) != null) {
										aBuffer += aDataRow + "\n";
									}
									text = aBuffer;
									if (inst.length == 1) {
										sb.append(text);
									} else {
										sb.append(text + "\n");
									}
									myReader.close();

								} catch (Exception e) {
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);
								}

								txt_view[i1].setText(text);

							}

							else if (inst[i1].contains("image")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.rsz_broken));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);
								final ImageView imgview = iv_album[i1];
								vfileCheck = new File(imgview
										.getContentDescription().toString());
								if (vfileCheck.exists())
									iv_album[i1].setImageBitmap(callDisp
											.ResizeImage(imgview
													.getContentDescription()
													.toString()));
								layoutt.addView(iv_album[i1], params);

								ArrayList<Object> list = new ArrayList<Object>();
								list.add(inst[i1]);
								list.add(iv_album[i1]);
								decode_task = new base64Decoder();
								decode_task.execute(list);

								imgview.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists()) {

											Intent in = new Intent(context,
													PhotoZoomActivity.class);
											in.putExtra(
													"Photo_path",

													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											in.putExtra("type", false);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivityForResult(in, 10);
										} else {

										}

									}
								});

							}

							else if (inst[i1].contains("audio")) {
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));
								iv_album[i1]
										.setContentDescription(instructions);
								iv_album[i1].setId(i1 + 1);

								final ImageView img = iv_album[i1];

								layoutt.addView(iv_album[i1], params);

								chatplayer = new MediaPlayer();
								img.setTag("0");
								img.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										vfileCheck = new File(locaLpath
												+ img.getContentDescription()
														.toString());

										if (img.getTag().toString().equals("1")) {
											img.setTag("0");
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_play);
											img.setImageBitmap(bitmap);
											chatplayer.stop();
											chatplayer.reset();
											isaudioPlaying = false;
										} else {
											if (!isaudioPlaying) {

												if (vfileCheck.exists()) {

													try {
														chatplayer
																.setDataSource(locaLpath
																		+ img.getContentDescription()
																				.toString());
														chatplayer
																.setLooping(false);

														chatplayer.prepare();
														chatplayer.start();
														img.setTag("1");

														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_stop);
														img.setImageBitmap(bitmap);
														isaudioPlaying = true;

													} catch (IllegalArgumentException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IllegalStateException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													} catch (IOException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													}

													chatplayer
															.setOnCompletionListener(new OnCompletionListener() {

																@Override
																public void onCompletion(
																		MediaPlayer mp) {

																	try {
																		chatplayer
																				.reset();
																		if (bitmap != null) {
																			if (!bitmap
																					.isRecycled()) {
																				bitmap.recycle();
																			}
																		}
																		isaudioPlaying = false;
																		bitmap = BitmapFactory
																				.decodeResource(
																						getResources(),
																						R.drawable.v_play);
																		img.setImageBitmap(bitmap);
																		img.setTag("0");

																	} catch (IllegalArgumentException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	} catch (IllegalStateException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	}

																}
															});

												} else {
													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}
											} else {

												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.please_stop_audio));
											}
										}
									}
								});
							}

							else if (inst[i1].contains("video")) {

								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
										RelativeLayout.LayoutParams.WRAP_CONTENT,
										RelativeLayout.LayoutParams.WRAP_CONTENT);
								params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
										RelativeLayout.ABOVE);
								iv_album[i1] = new ImageView(context);
								iv_album[i1].setPadding(30, 10, 30, 0);
								iv_album[i1].setImageDrawable(this
										.getResources().getDrawable(
												R.drawable.v_play));

								iv_album[i1].setId(i1 + 1);

								layoutt.addView(iv_album[i1], params);

								iv_album[i1]
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												vfileCheck = new File(
														locaLpath
																+ iv_album[i1]
																		.getContentDescription()
																		.toString());

												if (vfileCheck.exists()) {

													if (!isaudioPlaying) {
														Intent intentVPlayer = new Intent(
																context,
																VideoPlayer.class);
														intentVPlayer
																.putExtra(
																		"File_Path",

																		locaLpath
																				+ iv_album[i1]
																						.getContentDescription()
																						.toString());
														intentVPlayer.putExtra(
																"Player_Type",
																"Video Player");
														startActivity(intentVPlayer);
													} else {

														showToast("Kindly stop playing audio ");
													}

												} else {

													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}

											}
										});

							}

						}
						// if (permission != null
						// && permission.equalsIgnoreCase("F2")) {
						// option_selector.setEnabled(false);
						// }
						if (defaultPermission != null
								&& defaultPermission.equalsIgnoreCase("F2")) {
							option_selector.setEnabled(false);
						}
						if (permission != null) {
							if (permission.equalsIgnoreCase("F2")) {
								option_selector.setEnabled(false);
							} else {
								option_selector.setEnabled(true);
							}
						}
						option_selector.setContentDescription("multimedia");

						option_selector
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										RelativeLayout r_parent = (RelativeLayout) v
												.getParent();
										LinearLayout l_parent = (LinearLayout) r_parent
												.getParent();
										selected_rowid = l_parent.getId();
										showBlobSelector();
									}
								});
						layout.setId(records_container.getChildCount());
						layout.setLayoutParams(paramss);
						layout.setContentDescription(entrymode);

						records_container.addView(layout);

					}
				}
			} catch (Exception e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
	}

	String getRecords(String query) {

		rec_list = callDisp.getdbHeler(context)
				.isQueryContainResults(query, "");
		values = new StringBuffer();

		String val = null;
		for (int i = 0; i < rec_list.size(); i++) {

			String[] rec = rec_list.get(i);
			for (int j = 0; j < rec.length; j++) {
				if (rec[j].length() > 0) {
					values.append(rec[j]);
					values.append(",");
				}

			}
			val = values.toString()
					.substring(0, values.toString().length() - 1);

		}

		if (val == null) {
			val = "None,";
		}
		return val;
	}

	private void addView(final View v, String vaiddatas) {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				FormRecordsCreators.this);
		builder.create();
		builder.setTitle("Valid Data");
		final String[] Values;

		if (vaiddatas.startsWith("H")) {
			String validdataValues = vaiddatas.replace("H", "").trim();

			Values = validdataValues.split(",");

		} else if (vaiddatas.startsWith("R")) {
			String validdataValues = vaiddatas.replace("R", "").trim();
			Values = validdataValues.split(",");
		} else {
			String query = vaiddatas.substring(1).trim();
			Values = getRecords(query).split(",");

		}

		List<String> list = Arrays.asList(Values);
		Set<String> set = new HashSet<String>(list);

		final String[] result = new String[set.size()];
		set.toArray(result);
		Arrays.sort(result);
		int selected = -1;

		builder.setSingleChoiceItems(result, selected,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						changeFieldType(result[which].toString(), v);
						alert.cancel();
					}
				});
		alert = builder.create();
		alert.show();

	}

	private void changeFieldType(String type, View v) {
		RelativeLayout layout_container = (RelativeLayout) v.getParent();
		EditText ed_fld = (EditText) layout_container
				.findViewById(R.id.edtxt_frmfield);
		ed_fld.setText(type);
	}

	private void loadformFields() {
		try {
			Log.i("hs", "loadformFields");

			records_container.removeAllViews();
			new LayoutParams(96, 96);

			LayoutInflater layoutInflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			HashMap<String, String> dtype = new HashMap<String, String>();
			dtype = callDisp.getdbHeler(context).getColumnTypesforminfo("");

			String text = "";
			for (String recs : field_list) {

				try {
					if (!recs.contains("blob_")) {
						String colName = "";
						if (recs.contains("-")) {
							recs.split("-");
							colName = recs;

						} else {
							colName = recs;

						}
						Log.i("newform123", "loadformfields1 table name : "
								+ title + "_" + table_id + "column : "
								+ colName + " datatype" + dtype);
						info_lists = callDisp.getdbHeler(context)
								.getRecordsofforminfotable(
										title + "_" + table_id, colName, dtype);
						if (info_lists != null) {
							Log.i("newform123",
									"loadformfields1 info_list not null");
							String dropdown = info_lists[3].toString();
							String attId = info_lists[8];
							String permission = DBAccess.getdbHeler()
									.getFormFieldBuddyAccessSettings(table_id,
											attId);
							Log.i("newform123", "bPermission : " + permission);
							String defaultPermission = DBAccess.getdbHeler()
									.getFormFieldDefaultAccessSettings(
											table_id, attId);
							Log.i("newform123", "dPermission : "
									+ defaultPermission);
							if (dropdown.equalsIgnoreCase("drop down")) {

								entrymode = info_lists[3];
								validdata = info_lists[4];
								defaultvalue = info_lists[5];
								instruction = info_lists[6] + ",";
								errortip = info_lists[7];

								LinearLayout layout = (LinearLayout) layoutInflater
										.inflate(R.layout.formrecord_fields,
												null);
								layout.findViewById(R.id.tview_clm);
								RelativeLayout relativeLayout = (RelativeLayout) layout
										.findViewById(R.id.rlayout_grp);

								TextView tv1 = (TextView) layout
										.findViewById(R.id.tview_clms);
								tv1.setText(recs);
								Log.i("ff123", "permission : " + permission);
								LinearLayout layoutt = (LinearLayout) layout
										.findViewById(R.id.tview_lay);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// layout.setVisibility(View.GONE);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F1")) {
									layout.setVisibility(View.GONE);
								} else {
									layout.setVisibility(View.VISIBLE);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F1")) {
										layout.setVisibility(View.GONE);
									} else if (permission
											.equalsIgnoreCase("F2")) {
										layout.setVisibility(View.VISIBLE);
									} else {
										layout.setVisibility(View.VISIBLE);
									}
								}
								final String[] inst = instruction.split(",");
								final ImageView[] iv_album = new ImageView[inst.length];
								final TextView[] txt_view = new TextView[inst.length];

								StringBuffer sb = new StringBuffer();

								for (i1 = 0; i1 < inst.length; i1++) {
									instructions = inst[i1];
									if (inst[i1].contains("note")) {
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										txt_view[i1] = new TextView(context);
										txt_view[i1].setPadding(30, 10, 30, 0);
										txt_view[i1].setText(text);

										layoutt.addView(txt_view[i1], params);

										try {
											File myFile = new File(
													"/sdcard/COMMedia/"
															+ instructions);
											FileInputStream fIn = new FileInputStream(
													myFile);
											BufferedReader myReader = new BufferedReader(
													new InputStreamReader(fIn));
											String aDataRow = "";
											String aBuffer = "";
											while ((aDataRow = myReader
													.readLine()) != null) {
												aBuffer += aDataRow + "\n";
											}
											text = aBuffer;
											if (inst.length == 1) {
												sb.append(text);
											} else {
												sb.append(text + "\n");
											}
											myReader.close();

										} catch (Exception e) {
											if (AppReference.isWriteInFile)
												AppReference.logger.error(
														e.getMessage(), e);
										}

										txt_view[i1].setText(text);

									}

									else if (inst[i1].contains("image")) {
										Log.i("hs", "image :");
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.rsz_broken));
										iv_album[i1]
												.setContentDescription(instructions);

										iv_album[i1].setId(i1 + 1);

										final ImageView imgview = iv_album[i1];
										vfileCheck = new File(imgview
												.getContentDescription()
												.toString());
										if (vfileCheck.exists())
											iv_album[i1]
													.setImageBitmap(callDisp
															.ResizeImage(imgview
																	.getContentDescription()
																	.toString()));
										layoutt.addView(iv_album[i1], params);

										ArrayList<Object> list = new ArrayList<Object>();
										list.add(inst[i1]);
										list.add(iv_album[i1]);
										decode_task = new base64Decoder();
										decode_task.execute(list);

										imgview.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												vfileCheck = new File(
														locaLpath
																+ imgview
																		.getContentDescription()
																		.toString());
												if (vfileCheck.exists()) {

													Intent in = new Intent(
															context,
															PhotoZoomActivity.class);
													in.putExtra(
															"Photo_path",

															locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString());
													in.putExtra("type", false);
													in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
													startActivityForResult(in,
															10);
												} else {

												}

											}
										});

									}

									else if (inst[i1].contains("audio")) {
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.v_play));
										iv_album[i1]
												.setContentDescription(instructions);
										iv_album[i1].setId(i1 + 1);

										final ImageView img = iv_album[i1];

										layoutt.addView(iv_album[i1], params);

										chatplayer = new MediaPlayer();
										img.setTag("0");

										img.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												vfileCheck = new File(
														locaLpath
																+ img.getContentDescription()
																		.toString());

												if (img.getTag().toString()
														.equals("1")) {
													img.setTag("0");
													bitmap = BitmapFactory
															.decodeResource(
																	getResources(),
																	R.drawable.v_play);
													img.setImageBitmap(bitmap);
													isaudioPlaying = false;

													chatplayer.stop();
													chatplayer.reset();
												} else {
													if (!isaudioPlaying) {

														if (vfileCheck.exists()) {

															try {
																chatplayer
																		.setDataSource(locaLpath
																				+ img.getContentDescription()
																						.toString());
																chatplayer
																		.setLooping(false);

																chatplayer
																		.prepare();
																chatplayer
																		.start();
																bitmap = BitmapFactory
																		.decodeResource(
																				getResources(),
																				R.drawable.v_stop);
																img.setImageBitmap(bitmap);
																img.setTag("1");
																isaudioPlaying = true;

															} catch (IllegalArgumentException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															} catch (IllegalStateException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															} catch (IOException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															}

															chatplayer
																	.setOnCompletionListener(new OnCompletionListener() {

																		@Override
																		public void onCompletion(
																				MediaPlayer mp) {

																			try {
																				chatplayer
																						.reset();
																				isaudioPlaying = false;

																				bitmap = BitmapFactory
																						.decodeResource(
																								getResources(),
																								R.drawable.v_play);
																				img.setImageBitmap(bitmap);

																				img.setTag("0");

																			} catch (IllegalArgumentException e) {
																				if (AppReference.isWriteInFile)
																					AppReference.logger
																							.error(e.getMessage(),
																									e);
																				e.printStackTrace();
																			} catch (IllegalStateException e) {
																				if (AppReference.isWriteInFile)
																					AppReference.logger
																							.error(e.getMessage(),
																									e);
																				e.printStackTrace();
																			}

																		}
																	});

														} else {
															Toast.makeText(
																	context,
																	"File Downloading",
																	1).show();
															downloadConfiguredNote(instructions);

														}
													} else {

														showToast(SingleInstance.mainContext
																.getResources()
																.getString(
																		R.string.please_stop_audio));
													}

												}

											}
										});

									}

									else if (inst[i1].contains("video")) {

										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.v_play));
										iv_album[i1]
												.setContentDescription(instructions);

										iv_album[i1].setId(i1 + 1);

										final ImageView img = iv_album[i1];

										layoutt.addView(iv_album[i1], params);

										img.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												vfileCheck = new File(
														locaLpath
																+ img.getContentDescription()
																		.toString());

												if (vfileCheck.exists()) {
													if (!isaudioPlaying) {

														Intent intentVPlayer = new Intent(
																context,
																VideoPlayer.class);
														intentVPlayer
																.putExtra(
																		"File_Path",

																		locaLpath
																				+ img.getContentDescription()
																						.toString());
														intentVPlayer.putExtra(
																"Player_Type",
																"Video Player");
														startActivity(intentVPlayer);
													}

													else {
														showToast(SingleInstance.mainContext
																.getResources()
																.getString(
																		R.string.please_stop_audio));

													}
												} else {

													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}

											}
										});

									}

								}

								final ImageView im = (ImageView) layout
										.findViewById(R.id.iview_dtype);
								im.setContentDescription(validdata);
								EditText ed_fld = (EditText) layout
										.findViewById(R.id.edtxt_frmfield);
								ed_fld.setText(defaultvalue);
								ed_fld.setContentDescription(defaultvalue);
								ed_fld.setHint("Select a value from drop down");
								// if (permission != null
								// && permission.equalsIgnoreCase("F2")) {
								// im.setEnabled(false);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F2")) {
									im.setEnabled(false);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F2")) {
										im.setEnabled(false);
//									} else {
//										im.setEnabled(false);
//									}
									}else if (permission.equalsIgnoreCase("F3")) {
										im.setEnabled(true);
									}
								}
								ed_fld.setFocusableInTouchMode(false);
								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										addView(v, im.getContentDescription()
												.toString());
									}
								});

								if (dtypes.get(recs).trim()
										.equalsIgnoreCase("INTEGER")) {
									ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);
								} else if (dtypes.get(recs).trim()
										.equalsIgnoreCase("nvarchar(20)")) {
									ed_fld.setInputType(InputType.TYPE_CLASS_TEXT);
								}

								layout.setId(records_container.getChildCount());
								layout.setContentDescription(entrymode);
								records_container.addView(layout);
							}

							else if (dropdown.equalsIgnoreCase("date")) {

								entrymode = info_lists[3];
								validdata = info_lists[4];
								defaultvalue = info_lists[5];
								instruction = info_lists[6] + ",";
								errortip = info_lists[7];

								LinearLayout layout = (LinearLayout) layoutInflater
										.inflate(R.layout.formrecord_fields,
												null);
								layout.findViewById(R.id.tview_clm);
								TextView tv1 = (TextView) layout
										.findViewById(R.id.tview_clms);
								tv1.setText(recs);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// layout.setVisibility(View.GONE);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F1")) {
									layout.setVisibility(View.GONE);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F1")) {
										layout.setVisibility(View.GONE);
									} else if (permission
											.equalsIgnoreCase("F2")) {
										layout.setVisibility(View.VISIBLE);
									} else {
										layout.setVisibility(View.VISIBLE);
									}
								}
								LinearLayout layoutt = (LinearLayout) layout
										.findViewById(R.id.tview_lay);

								final String[] inst = instruction.split(",");
								final ImageView[] iv_album = new ImageView[inst.length];
								final TextView[] txt_view = new TextView[inst.length];

								StringBuffer sb = new StringBuffer();

								for (i1 = 0; i1 < inst.length; i1++) {
									instructions = inst[i1];
									if (inst[i1].contains("note")) {
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										txt_view[i1] = new TextView(context);
										txt_view[i1].setPadding(30, 10, 30, 0);
										txt_view[i1].setText(text);

										layoutt.addView(txt_view[i1], params);

										try {
											File myFile = new File(
													"/sdcard/COMMedia/"
															+ instructions);
											FileInputStream fIn = new FileInputStream(
													myFile);
											BufferedReader myReader = new BufferedReader(
													new InputStreamReader(fIn));
											String aDataRow = "";
											String aBuffer = "";
											while ((aDataRow = myReader
													.readLine()) != null) {
												aBuffer += aDataRow + "\n";
											}
											text = aBuffer;
											if (inst.length == 1) {
												sb.append(text);
											} else {
												sb.append(text + "\n");
											}
											myReader.close();

										} catch (Exception e) {
											if (AppReference.isWriteInFile)
												AppReference.logger.error(
														e.getMessage(), e);
										}

										txt_view[i1].setText(text);

									}

									else if (inst[i1].contains("image")) {

										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.rsz_broken));
										iv_album[i1]
												.setContentDescription(instructions);

										iv_album[i1].setId(i1 + 1);

										final ImageView imgview = iv_album[i1];
										vfileCheck = new File(imgview
												.getContentDescription()
												.toString());
										if (vfileCheck.exists())
											iv_album[i1]
													.setImageBitmap(callDisp
															.ResizeImage(imgview
																	.getContentDescription()
																	.toString()));
										layoutt.addView(iv_album[i1], params);

										ArrayList<Object> list = new ArrayList<Object>();
										list.add(inst[i1]);
										list.add(iv_album[i1]);
										decode_task = new base64Decoder();
										decode_task.execute(list);

										imgview.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												vfileCheck = new File(
														locaLpath
																+ imgview
																		.getContentDescription()
																		.toString());
												if (vfileCheck.exists()) {

													Intent in = new Intent(
															context,
															PhotoZoomActivity.class);
													in.putExtra(
															"Photo_path",

															locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString());
													in.putExtra("type", false);
													in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
													startActivityForResult(in,
															10);
												} else {

												}

											}
										});

									}

									else if (inst[i1].contains("audio")) {
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.v_play));
										iv_album[i1]
												.setContentDescription(instructions);
										iv_album[i1].setId(i1 + 1);

										final ImageView img = iv_album[i1];

										layoutt.addView(iv_album[i1], params);

										chatplayer = new MediaPlayer();

										img.setTag("0");

										img.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												vfileCheck = new File(
														locaLpath
																+ img.getContentDescription()
																		.toString());

												if (img.getTag().toString()
														.equals("1")) {
													img.setTag("0");
													bitmap = BitmapFactory
															.decodeResource(
																	getResources(),
																	R.drawable.v_play);
													img.setImageBitmap(bitmap);
													isaudioPlaying = false;

													chatplayer.stop();
													chatplayer.reset();
												} else {
													if (!isaudioPlaying) {

														if (vfileCheck.exists()) {

															try {
																chatplayer
																		.setDataSource(locaLpath
																				+ img.getContentDescription()
																						.toString());
																chatplayer
																		.setLooping(false);

																chatplayer
																		.prepare();
																chatplayer
																		.start();
																bitmap = BitmapFactory
																		.decodeResource(
																				getResources(),
																				R.drawable.v_stop);
																img.setImageBitmap(bitmap);
																img.setTag("1");
																isaudioPlaying = true;

															} catch (IllegalArgumentException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															} catch (IllegalStateException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															} catch (IOException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															}

															chatplayer
																	.setOnCompletionListener(new OnCompletionListener() {

																		@Override
																		public void onCompletion(
																				MediaPlayer mp) {

																			try {
																				chatplayer
																						.reset();
																				isaudioPlaying = false;

																				bitmap = BitmapFactory
																						.decodeResource(
																								getResources(),
																								R.drawable.v_play);
																				img.setImageBitmap(bitmap);

																				img.setTag("0");

																			} catch (IllegalArgumentException e) {
																				if (AppReference.isWriteInFile)
																					AppReference.logger
																							.error(e.getMessage(),
																									e);
																				e.printStackTrace();
																			} catch (IllegalStateException e) {
																				if (AppReference.isWriteInFile)
																					AppReference.logger
																							.error(e.getMessage(),
																									e);
																				e.printStackTrace();
																			}

																		}
																	});

														} else {
															Toast.makeText(
																	context,
																	"File Downloading",
																	1).show();
															downloadConfiguredNote(instructions);

														}
													} else {

														showToast(SingleInstance.mainContext
																.getResources()
																.getString(
																		R.string.please_stop_audio));
													}

												}

											}
										});
									}

									else if (inst[i1].contains("video")) {

										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.v_play));
										iv_album[i1]
												.setContentDescription(instructions);

										iv_album[i1].setId(i1 + 1);

										final ImageView img = iv_album[i1];

										layoutt.addView(iv_album[i1], params);

										img.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stub
												vfileCheck = new File(
														locaLpath
																+ img.getContentDescription()
																		.toString());

												if (vfileCheck.exists()) {
													if (!isaudioPlaying) {

														Intent intentVPlayer = new Intent(
																context,
																VideoPlayer.class);
														intentVPlayer
																.putExtra(
																		"File_Path",

																		locaLpath
																				+ img.getContentDescription()
																						.toString());
														intentVPlayer.putExtra(
																"Player_Type",
																"Video Player");
														startActivity(intentVPlayer);
													}

													else {
														showToast("Kindly stop audio playing");

													}
												} else {

													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}

											}
										});

									}

								}

								final ImageView im = (ImageView) layout
										.findViewById(R.id.iview_dtype);
								im.setContentDescription(validdata);
								final EditText ed_fld = (EditText) layout
										.findViewById(R.id.edtxt_frmfield);
								ed_fld.setText(defaultvalue);
								ed_fld.setContentDescription(defaultvalue);

								ed_fld.setHint("Select a date");

								ed_fld.setFocusableInTouchMode(false);
								// if (permission != null
								// && permission.equalsIgnoreCase("F2")) {
								// im.setEnabled(false);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F2")) {
									im.setEnabled(false);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F2")) {
										im.setEnabled(false);
									} else {
										im.setEnabled(true);
									}
								}
								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										final Calendar c = Calendar
												.getInstance();
										mYear = c.get(Calendar.YEAR);
										mMonth = c.get(Calendar.MONTH);
										mDay = c.get(Calendar.DAY_OF_MONTH);

										Button btnSet;
										final DatePicker dp;
										final AlertDialog alertReminder = new AlertDialog.Builder(
												context).create();

										ScrollView tblDTPicker = (ScrollView) View
												.inflate(context,
														R.layout.datepicker,
														null);

										btnSet = (Button) tblDTPicker
												.findViewById(R.id.btnSetDateTime);
										dp = (DatePicker) tblDTPicker
												.findViewById(R.id.datePicker);

										btnSet.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												normaldate = +dp.getYear()
														+ "-"
														+ WebServiceReferences.setLength2((dp
																.getMonth() + 1))
														+ "-"
														+ WebServiceReferences.setLength2(dp
																.getDayOfMonth());

												ed_fld.setText(normaldate);
												alertReminder.dismiss();

											}
										});
										alertReminder.setTitle("Date");
										alertReminder.setView(tblDTPicker);
										alertReminder.show();

									}
								});

								if (dtypes.get(recs).trim()
										.equalsIgnoreCase("INTEGER")) {
									ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);
								} else if (dtypes.get(recs).trim()
										.equalsIgnoreCase("nvarchar(20)")) {
									ed_fld.setInputType(InputType.TYPE_CLASS_TEXT);
								}

								layout.setId(records_container.getChildCount());
								layout.setContentDescription(entrymode);

								records_container.addView(layout);

							} else if (dropdown.equalsIgnoreCase("time")) {

								entrymode = info_lists[3];
								validdata = info_lists[4];
								defaultvalue = info_lists[5];
								instruction = info_lists[6] + ",";
								errortip = info_lists[7];

								LinearLayout layout = (LinearLayout) layoutInflater
										.inflate(R.layout.formrecord_fields,
												null);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// layout.setVisibility(View.GONE);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F1")) {
									layout.setVisibility(View.GONE);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F1")) {
										layout.setVisibility(View.GONE);
									} else if (permission
											.equalsIgnoreCase("F2")) {
										layout.setVisibility(View.VISIBLE);
									} else {
										layout.setVisibility(View.VISIBLE);
									}
								}
								layout.findViewById(R.id.tview_clm);
								TextView tv1 = (TextView) layout
										.findViewById(R.id.tview_clms);
								tv1.setText(recs);

								LinearLayout layoutt = (LinearLayout) layout
										.findViewById(R.id.tview_lay);

								final String[] inst = instruction.split(",");
								final ImageView[] iv_album = new ImageView[inst.length];
								final TextView[] txt_view = new TextView[inst.length];

								StringBuffer sb = new StringBuffer();

								for (i1 = 0; i1 < inst.length; i1++) {
									instructions = inst[i1];
									if (inst[i1].contains("note")) {
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										txt_view[i1] = new TextView(context);
										txt_view[i1].setPadding(30, 10, 30, 0);
										txt_view[i1].setText(text);

										layoutt.addView(txt_view[i1], params);

										try {
											File myFile = new File(
													"/sdcard/COMMedia/"
															+ instructions);
											FileInputStream fIn = new FileInputStream(
													myFile);
											BufferedReader myReader = new BufferedReader(
													new InputStreamReader(fIn));
											String aDataRow = "";
											String aBuffer = "";
											while ((aDataRow = myReader
													.readLine()) != null) {
												aBuffer += aDataRow + "\n";
											}
											text = aBuffer;
											if (inst.length == 1) {
												sb.append(text);
											} else {
												sb.append(text + "\n");
											}
											myReader.close();

										} catch (Exception e) {
											if (AppReference.isWriteInFile)
												AppReference.logger.error(
														e.getMessage(), e);
										}

										txt_view[i1].setText(text);

									}

									else if (inst[i1].contains("image")) {

										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.v_play));
										iv_album[i1]
												.setContentDescription(instructions);

										iv_album[i1].setId(i1 + 1);

										final ImageView imgview = iv_album[i1];
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists())
											iv_album[i1]
													.setImageBitmap(callDisp
															.ResizeImage(locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString()));
										layoutt.addView(iv_album[i1], params);

										ArrayList<Object> list = new ArrayList<Object>();
										list.add(inst[i1]);
										list.add(iv_album[i1]);
										decode_task = new base64Decoder();
										decode_task.execute(list);

										imgview.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												vfileCheck = new File(
														locaLpath
																+ imgview
																		.getContentDescription()
																		.toString());
												if (vfileCheck.exists()) {

													Intent in = new Intent(
															context,
															PhotoZoomActivity.class);
													in.putExtra(
															"Photo_path",

															locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString());
													in.putExtra("type", false);
													in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
													startActivityForResult(in,
															10);
												} else {

												}

											}
										});

									}

									else if (inst[i1].contains("audio")) {
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.v_play));
										iv_album[i1]
												.setContentDescription(instructions);
										iv_album[i1].setId(i1 + 1);

										final ImageView img = iv_album[i1];

										layoutt.addView(iv_album[i1], params);

										chatplayer = new MediaPlayer();

										img.setTag("0");

										img.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												vfileCheck = new File(
														locaLpath
																+ img.getContentDescription()
																		.toString());

												if (img.getTag().toString()
														.equals("1")) {
													img.setTag("0");
													bitmap = BitmapFactory
															.decodeResource(
																	getResources(),
																	R.drawable.v_play);
													img.setImageBitmap(bitmap);
													isaudioPlaying = false;

													chatplayer.stop();
													chatplayer.reset();
												} else {
													if (!isaudioPlaying) {

														if (vfileCheck.exists()) {

															try {
																chatplayer
																		.setDataSource(locaLpath
																				+ img.getContentDescription()
																						.toString());
																chatplayer
																		.setLooping(false);

																chatplayer
																		.prepare();
																chatplayer
																		.start();
																bitmap = BitmapFactory
																		.decodeResource(
																				getResources(),
																				R.drawable.v_stop);
																img.setImageBitmap(bitmap);
																img.setTag("1");
																isaudioPlaying = true;

															} catch (IllegalArgumentException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															} catch (IllegalStateException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															} catch (IOException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															}

															chatplayer
																	.setOnCompletionListener(new OnCompletionListener() {

																		@Override
																		public void onCompletion(
																				MediaPlayer mp) {

																			try {
																				chatplayer
																						.reset();
																				isaudioPlaying = false;

																				bitmap = BitmapFactory
																						.decodeResource(
																								getResources(),
																								R.drawable.v_play);
																				img.setImageBitmap(bitmap);

																				img.setTag("0");

																			} catch (IllegalArgumentException e) {
																				if (AppReference.isWriteInFile)
																					AppReference.logger
																							.error(e.getMessage(),
																									e);
																				e.printStackTrace();
																			} catch (IllegalStateException e) {
																				if (AppReference.isWriteInFile)
																					AppReference.logger
																							.error(e.getMessage(),
																									e);
																				e.printStackTrace();
																			}

																		}
																	});

														} else {
															Toast.makeText(
																	context,
																	"File Downloading",
																	1).show();
															downloadConfiguredNote(instructions);

														}
													} else {

														showToast(SingleInstance.mainContext
																.getResources()
																.getString(
																		R.string.please_stop_audio));
													}

												}

											}
										});

									}

									else if (inst[i1].contains("video")) {

										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.v_play));
										iv_album[i1]
												.setContentDescription(instructions);

										iv_album[i1].setId(i1 + 1);

										final ImageView img = iv_album[i1];

										layoutt.addView(iv_album[i1], params);

										img.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												vfileCheck = new File(
														locaLpath
																+ img.getContentDescription()
																		.toString());

												if (vfileCheck.exists()) {
													if (!isaudioPlaying) {

														Intent intentVPlayer = new Intent(
																context,
																VideoPlayer.class);
														intentVPlayer
																.putExtra(
																		"File_Path",

																		locaLpath
																				+ img.getContentDescription()
																						.toString());
														intentVPlayer.putExtra(
																"Player_Type",
																"Video Player");
														startActivity(intentVPlayer);
													}

													else {
														showToast(SingleInstance.mainContext
																.getResources()
																.getString(
																		R.string.please_stop_audio));

													}
												} else {

													Toast.makeText(context,
															"File Downloading",
															1).show();
													downloadConfiguredNote(instructions);

												}

											}
										});

									}

								}

								final ImageView im = (ImageView) layout
										.findViewById(R.id.iview_dtype);
								im.setContentDescription(validdata);

								final EditText ed_fld = (EditText) layout
										.findViewById(R.id.edtxt_frmfield);
								ed_fld.setContentDescription(defaultvalue);

								ed_fld.setHint("Select a time");
								ed_fld.setFocusableInTouchMode(false);
								// if (permission != null
								// && permission.equalsIgnoreCase("F2")) {
								// im.setEnabled(false);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F2")) {
									im.setEnabled(false);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F2")) {
										im.setEnabled(false);
									} else {
										im.setEnabled(true);
									}
								}
								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										final Calendar cal = Calendar
												.getInstance();
										pHour = cal.get(Calendar.HOUR_OF_DAY);
										pMinute = cal.get(Calendar.MINUTE);
										Button btnSet;
										final TimePicker tp;
										final AlertDialog alertReminder = new AlertDialog.Builder(
												context).create();

										ScrollView tblDTPicker = (ScrollView) View
												.inflate(
														context,
														R.layout.timepickerdialog,
														null);

										btnSet = (Button) tblDTPicker
												.findViewById(R.id.btnSetDateTime);

										tp = (TimePicker) tblDTPicker
												.findViewById(R.id.timePicker);
										tp.setIs24HourView(true);

										btnSet.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {

												normaltime = WebServiceReferences.setLength2(tp
														.getCurrentHour())
														+ ":"
														+ WebServiceReferences.setLength2(tp
																.getCurrentMinute());
												ed_fld.setText(normaltime);

												alertReminder.dismiss();

											}
										});
										alertReminder.setTitle("Time");
										alertReminder.setView(tblDTPicker);
										alertReminder.show();
									}
								});

								layout.setId(records_container.getChildCount());
								layout.setContentDescription(entrymode);

								records_container.addView(layout);

							} else if (dropdown
									.equalsIgnoreCase("current location")) {

								entrymode = info_lists[3];
								validdata = info_lists[4];
								defaultvalue = info_lists[5];
								instruction = info_lists[6] + ",";
								errortip = info_lists[7];

								LinearLayout layout = (LinearLayout) layoutInflater
										.inflate(R.layout.formrecord_fields,
												null);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// layout.setVisibility(View.GONE);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F1")) {
									layout.setVisibility(View.GONE);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F1")) {
										layout.setVisibility(View.GONE);
									} else if (permission
											.equalsIgnoreCase("F2")) {
										layout.setVisibility(View.VISIBLE);
									} else {
										layout.setVisibility(View.VISIBLE);
									}
								}
								TextView tv = (TextView) layout
										.findViewById(R.id.tview_clm);
								TextView tv1 = (TextView) layout
										.findViewById(R.id.tview_clms);
								tv1.setText(recs);
								LinearLayout layoutt = (LinearLayout) layout
										.findViewById(R.id.tview_lay);

								if (instruction.equals(null)
										|| instruction.equals("")) {
									tv.setVisibility(View.GONE);
								} else {
									String[] inst = instruction.split(",");
									final ImageView[] iv_album = new ImageView[inst.length];
									final TextView[] txt_view = new TextView[inst.length];

									StringBuffer sb = new StringBuffer();

									for (i1 = 0; i1 < inst.length; i1++) {
										instructions = inst[i1];
										if (inst[i1].contains("note")) {
											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											txt_view[i1] = new TextView(context);
											txt_view[i1].setPadding(30, 10, 30,
													0);
											txt_view[i1].setText(text);

											layoutt.addView(txt_view[i1],
													params);

											try {
												File myFile = new File(
														"/sdcard/COMMedia/"
																+ instructions);
												FileInputStream fIn = new FileInputStream(
														myFile);
												BufferedReader myReader = new BufferedReader(
														new InputStreamReader(
																fIn));
												String aDataRow = "";
												String aBuffer = "";
												while ((aDataRow = myReader
														.readLine()) != null) {
													aBuffer += aDataRow + "\n";
												}
												text = aBuffer;
												if (inst.length == 1) {
													sb.append(text);
												} else {
													sb.append(text + "\n");
												}
												myReader.close();

											} catch (Exception e) {
												if (AppReference.isWriteInFile)
													AppReference.logger.error(
															e.getMessage(), e);
											}

											txt_view[i1].setText(text);

										}

										else if (inst[i1].contains("image")) {

											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1]
													.setImageDrawable(this
															.getResources()
															.getDrawable(
																	R.drawable.rsz_broken));
											iv_album[i1]
													.setContentDescription(instructions);
											iv_album[i1].setId(i1 + 1);
											final ImageView imgview = iv_album[i1];
											vfileCheck = new File(
													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											if (vfileCheck.exists())
												iv_album[i1]
														.setImageBitmap(callDisp
																.ResizeImage(locaLpath
																		+ imgview
																				.getContentDescription()
																				.toString()));
											layoutt.addView(iv_album[i1],
													params);

											ArrayList<Object> list = new ArrayList<Object>();
											list.add(inst[i1]);
											list.add(iv_album[i1]);
											decode_task = new base64Decoder();
											decode_task.execute(list);

											imgview.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													vfileCheck = new File(
															locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString());
													if (vfileCheck.exists()) {

														Intent in = new Intent(
																context,
																PhotoZoomActivity.class);
														in.putExtra(
																"Photo_path",

																locaLpath
																		+ imgview
																				.getContentDescription()
																				.toString());
														in.putExtra("type",
																false);
														in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
														startActivityForResult(
																in, 10);
													} else {

													}

												}
											});

										}

										else if (inst[i1].contains("audio")) {
											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1]
													.setContentDescription(instructions);

											iv_album[i1].setImageDrawable(this
													.getResources()
													.getDrawable(
															R.drawable.v_play));

											iv_album[i1].setId(i1 + 1);

											final ImageView img = iv_album[i1];

											layoutt.addView(iv_album[i1],
													params);

											chatplayer = new MediaPlayer();
											img.setTag("0");

											img.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													vfileCheck = new File(
															locaLpath
																	+ img.getContentDescription()
																			.toString());

													if (img.getTag().toString()
															.equals("1")) {
														img.setTag("0");
														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_play);
														img.setImageBitmap(bitmap);
														isaudioPlaying = false;

														chatplayer.stop();
														chatplayer.reset();
													} else {
														if (!isaudioPlaying) {

															if (vfileCheck
																	.exists()) {

																try {
																	chatplayer
																			.setDataSource(locaLpath
																					+ img.getContentDescription()
																							.toString());
																	chatplayer
																			.setLooping(false);

																	chatplayer
																			.prepare();
																	chatplayer
																			.start();
																	bitmap = BitmapFactory
																			.decodeResource(
																					getResources(),
																					R.drawable.v_stop);
																	img.setImageBitmap(bitmap);
																	img.setTag("1");
																	isaudioPlaying = true;

																} catch (IllegalArgumentException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																} catch (IllegalStateException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																} catch (IOException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																}

																chatplayer
																		.setOnCompletionListener(new OnCompletionListener() {

																			@Override
																			public void onCompletion(
																					MediaPlayer mp) {

																				try {
																					chatplayer
																							.reset();
																					isaudioPlaying = false;

																					bitmap = BitmapFactory
																							.decodeResource(
																									getResources(),
																									R.drawable.v_play);
																					img.setImageBitmap(bitmap);

																					img.setTag("0");

																				} catch (IllegalArgumentException e) {
																					if (AppReference.isWriteInFile)
																						AppReference.logger
																								.error(e.getMessage(),
																										e);
																					e.printStackTrace();
																				} catch (IllegalStateException e) {
																					if (AppReference.isWriteInFile)
																						AppReference.logger
																								.error(e.getMessage(),
																										e);
																					e.printStackTrace();
																				}

																			}
																		});

															} else {
																Toast.makeText(
																		context,
																		"File Downloading",
																		1)
																		.show();
																downloadConfiguredNote(instructions);

															}
														} else {

															showToast(SingleInstance.mainContext
																	.getResources()
																	.getString(
																			R.string.please_stop_audio));
														}

													}

												}
											});
										}

										else if (inst[i1].contains("video")) {
											vfileCheck = new File(instructions);

											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1].setImageDrawable(this
													.getResources()
													.getDrawable(
															R.drawable.v_play));
											iv_album[i1]
													.setContentDescription(instructions);

											iv_album[i1].setId(i1 + 1);

											final ImageView img = iv_album[i1];

											layoutt.addView(iv_album[i1],
													params);

											iv_album[i1]
													.setOnClickListener(new OnClickListener() {

														@Override
														public void onClick(
																View v) {
															vfileCheck = new File(
																	locaLpath
																			+ img.getContentDescription()
																					.toString());

															if (vfileCheck
																	.exists()) {
																if (!isaudioPlaying) {

																	Intent intentVPlayer = new Intent(
																			context,
																			VideoPlayer.class);
																	intentVPlayer
																			.putExtra(
																					"File_Path",

																					locaLpath
																							+ img.getContentDescription());
																	intentVPlayer
																			.putExtra(
																					"Player_Type",
																					"Video Player");
																	startActivity(intentVPlayer);
																}

																else {
																	showToast("Kindly stop audio playing");

																}
															} else {

																Toast.makeText(
																		context,
																		"File Downloading",
																		1)
																		.show();
																downloadConfiguredNote(instructions);

															}

														}
													});

										}
									}

									tv.setText(sb.toString());

								}

								final ImageView im = (ImageView) layout
										.findViewById(R.id.iview_dtype);
								im.setContentDescription("");
								im.setVisibility(View.GONE);
								final EditText ed_fld = (EditText) layout
										.findViewById(R.id.edtxt_frmfield);
								ed_fld.setHint("Tap to get current Location");
								ed_fld.setFocusableInTouchMode(false);
								ed_fld.setContentDescription(defaultvalue);
								// if (permission != null
								// && permission.equalsIgnoreCase("F2")) {
								// ed_fld.setEnabled(false);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F2")) {
									ed_fld.setEnabled(false);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F2")) {
										ed_fld.setEnabled(false);
									} else {
										ed_fld.setEnabled(true);
									}
								}
								ed_fld.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {

										if (CallDispatcher.latitude == 0.0
												&& CallDispatcher.longitude == 0.0) {
											showToast("Sorry! Turn On Location Service ");
										} else {
											String values = CallDispatcher.latitude
													+ " and "
													+ CallDispatcher.longitude;
											ed_fld.setText(values);
										}
									}
								});
								layout.setId(records_container.getChildCount());
								layout.setContentDescription(entrymode);

								records_container.addView(layout);

							} else if (dropdown
									.equalsIgnoreCase("radio button")) {

								entrymode = info_lists[3];
								validdata = info_lists[4];
								defaultvalue = info_lists[5];
								instruction = info_lists[6] + ",";
								errortip = info_lists[7];

								LinearLayout layout = (LinearLayout) layoutInflater
										.inflate(R.layout.formrecord_fields,
												null);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// layout.setVisibility(View.GONE);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F1")) {
									layout.setVisibility(View.GONE);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F1")) {
										layout.setVisibility(View.GONE);
									} else if (permission
											.equalsIgnoreCase("F2")) {
										layout.setVisibility(View.VISIBLE);
									} else {
										layout.setVisibility(View.VISIBLE);
									}
								}
								layout.findViewById(R.id.tview_clm);
								TextView tv1 = (TextView) layout
										.findViewById(R.id.tview_clms);
								tv1.setText(recs);
								LinearLayout layoutt = (LinearLayout) layout
										.findViewById(R.id.tview_lay);
								StringBuffer sb = new StringBuffer();

								final String[] inst = instruction.split(",");
								final ImageView[] iv_album = new ImageView[inst.length];
								final TextView[] txt_view = new TextView[inst.length];

								for (i1 = 0; i1 < inst.length; i1++) {
									instructions = inst[i1];
									if (inst[i1].contains("note")) {
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										txt_view[i1] = new TextView(context);
										txt_view[i1].setPadding(30, 10, 30, 0);
										txt_view[i1].setText(text);

										layoutt.addView(txt_view[i1], params);

										try {
											File myFile = new File(
													"/sdcard/COMMedia/"
															+ instructions);
											FileInputStream fIn = new FileInputStream(
													myFile);
											BufferedReader myReader = new BufferedReader(
													new InputStreamReader(fIn));
											String aDataRow = "";
											String aBuffer = "";
											while ((aDataRow = myReader
													.readLine()) != null) {
												aBuffer += aDataRow + "\n";
											}
											text = aBuffer;
											if (inst.length == 1) {
												sb.append(text);
											} else {
												sb.append(text + "\n");
											}
											myReader.close();

										} catch (Exception e) {
											if (AppReference.isWriteInFile)
												AppReference.logger.error(
														e.getMessage(), e);
										}

										txt_view[i1].setText(text);

									}

									else if (inst[i1].contains("image")) {

										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.rsz_broken));
										iv_album[i1]
												.setContentDescription(instructions);
										iv_album[i1].setId(i1 + 1);
										final ImageView imgview = iv_album[i1];
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists())
											iv_album[i1]
													.setImageBitmap(callDisp
															.ResizeImage(locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString()));
										layoutt.addView(iv_album[i1], params);

										ArrayList<Object> list = new ArrayList<Object>();
										list.add(inst[i1]);
										list.add(iv_album[i1]);
										decode_task = new base64Decoder();
										decode_task.execute(list);

										imgview.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												vfileCheck = new File(
														locaLpath
																+ imgview
																		.getContentDescription()
																		.toString());
												if (vfileCheck.exists()) {

													Intent in = new Intent(
															context,
															PhotoZoomActivity.class);
													in.putExtra(
															"Photo_path",

															locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString());
													in.putExtra("type", false);
													in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
													startActivityForResult(in,
															10);
												} else {

												}

											}
										});

									}

									else if (inst[i1].contains("audio")) {
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1]
												.setContentDescription(instructions);

										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.v_play));

										iv_album[i1].setId(i1 + 1);

										final ImageView img = iv_album[i1];

										layoutt.addView(iv_album[i1], params);

										chatplayer = new MediaPlayer();

										img.setTag("0");

										img.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												vfileCheck = new File(
														locaLpath
																+ img.getContentDescription()
																		.toString());

												if (img.getTag().toString()
														.equals("1")) {
													img.setTag("0");
													bitmap = BitmapFactory
															.decodeResource(
																	getResources(),
																	R.drawable.v_play);
													img.setImageBitmap(bitmap);
													isaudioPlaying = false;

													chatplayer.stop();
													chatplayer.reset();
												} else {
													if (!isaudioPlaying) {

														if (vfileCheck.exists()) {

															try {
																chatplayer
																		.setDataSource(locaLpath
																				+ img.getContentDescription()
																						.toString());
																chatplayer
																		.setLooping(false);

																chatplayer
																		.prepare();
																chatplayer
																		.start();
																bitmap = BitmapFactory
																		.decodeResource(
																				getResources(),
																				R.drawable.v_stop);
																img.setImageBitmap(bitmap);
																img.setTag("1");
																isaudioPlaying = true;

															} catch (IllegalArgumentException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															} catch (IllegalStateException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															} catch (IOException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															}

															chatplayer
																	.setOnCompletionListener(new OnCompletionListener() {

																		@Override
																		public void onCompletion(
																				MediaPlayer mp) {

																			try {
																				chatplayer
																						.reset();
																				isaudioPlaying = false;

																				bitmap = BitmapFactory
																						.decodeResource(
																								getResources(),
																								R.drawable.v_play);
																				img.setImageBitmap(bitmap);

																				img.setTag("0");

																			} catch (IllegalArgumentException e) {
																				if (AppReference.isWriteInFile)
																					AppReference.logger
																							.error(e.getMessage(),
																									e);
																				e.printStackTrace();
																			} catch (IllegalStateException e) {
																				if (AppReference.isWriteInFile)
																					AppReference.logger
																							.error(e.getMessage(),
																									e);
																				e.printStackTrace();
																			}

																		}
																	});

														} else {
															Toast.makeText(
																	context,
																	"File Downloading",
																	1).show();
															downloadConfiguredNote(instructions);

														}
													} else {

														showToast(SingleInstance.mainContext
																.getResources()
																.getString(
																		R.string.please_stop_audio));
													}

												}

											}
										});
									}

									else if (inst[i1].contains("video")) {

										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.v_play));
										iv_album[i1]
												.setContentDescription(instructions);

										iv_album[i1].setId(i1 + 1);

										final ImageView img = iv_album[i1];

										layoutt.addView(iv_album[i1], params);

										iv_album[i1]
												.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
														vfileCheck = new File(
																locaLpath
																		+ img.getContentDescription()
																				.toString());

														if (vfileCheck.exists()) {
															if (!isaudioPlaying) {

																Intent intentVPlayer = new Intent(
																		context,
																		VideoPlayer.class);
																intentVPlayer
																		.putExtra(
																				"File_Path",

																				locaLpath+img.getContentDescription());
																intentVPlayer
																		.putExtra(
																				"Player_Type",
																				"Video Player");
																startActivity(intentVPlayer);
															}

															else {
																showToast(SingleInstance.mainContext
																		.getResources()
																		.getString(
																				R.string.please_stop_audio));

															}
														} else {

															Toast.makeText(
																	context,
																	"File Downloading",
																	1).show();
															downloadConfiguredNote(instructions);

														}

													}
												});

									}

								}

								final ImageView im = (ImageView) layout
										.findViewById(R.id.iview_dtype);
								im.setContentDescription(validdata);
								EditText ed_fld = (EditText) layout
										.findViewById(R.id.edtxt_frmfield);
								ed_fld.setFilters(getFilter());
								ed_fld.setText(defaultvalue);
								ed_fld.setContentDescription(defaultvalue);
								ed_fld.setHint("Select a value from drop down");

								ed_fld.setFocusableInTouchMode(false);
								// if (permission != null
								// && permission.equalsIgnoreCase("F2")) {
								// im.setEnabled(false);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F2")) {
									im.setEnabled(false);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F2")) {
										im.setEnabled(false);
									} else {
										im.setEnabled(true);
									}
								}
								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										addView(v, im.getContentDescription()
												.toString());
									}
								});

								if (dtypes.get(recs).trim()
										.equalsIgnoreCase("INTEGER")) {
									ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);
								} else if (dtypes.get(recs).trim()
										.equalsIgnoreCase("nvarchar(20)")) {
									ed_fld.setInputType(InputType.TYPE_CLASS_TEXT);
								}

								layout.setId(records_container.getChildCount());
								layout.setContentDescription(entrymode);

								records_container.addView(layout);
							} else if (dropdown
									.equalsIgnoreCase("date and time")) {

								entrymode = info_lists[3];
								validdata = info_lists[4];
								defaultvalue = info_lists[5];
								instruction = info_lists[6] + ",";
								errortip = info_lists[7];

								if (validdata.length() == 0) {

									validdata = "Date and Time";
								}

								LinearLayout layout = (LinearLayout) layoutInflater
										.inflate(R.layout.formrecord_fields,
												null);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// layout.setVisibility(View.GONE);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F1")) {
									layout.setVisibility(View.GONE);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F1")) {
										layout.setVisibility(View.GONE);
									} else if (permission
											.equalsIgnoreCase("F2")) {
										layout.setVisibility(View.VISIBLE);
									} else {
										layout.setVisibility(View.VISIBLE);
									}
								}
								layout.findViewById(R.id.tview_clm);
								LinearLayout layoutt = (LinearLayout) layout
										.findViewById(R.id.tview_lay);
								TextView tv1 = (TextView) layout
										.findViewById(R.id.tview_clms);
								tv1.setText(recs);

								final String[] inst = instruction.split(",");
								final ImageView[] iv_album = new ImageView[inst.length];
								final TextView[] txt_view = new TextView[inst.length];

								StringBuffer sb = new StringBuffer();

								for (i1 = 0; i1 < inst.length; i1++) {
									instructions = inst[i1];
									if (inst[i1].contains("note")) {
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										txt_view[i1] = new TextView(context);
										txt_view[i1].setPadding(30, 10, 30, 0);
										txt_view[i1].setText(text);

										layoutt.addView(txt_view[i1], params);

										try {
											File myFile = new File(
													"/sdcard/COMMedia/"
															+ instructions);
											FileInputStream fIn = new FileInputStream(
													myFile);
											BufferedReader myReader = new BufferedReader(
													new InputStreamReader(fIn));
											String aDataRow = "";
											String aBuffer = "";
											while ((aDataRow = myReader
													.readLine()) != null) {
												aBuffer += aDataRow + "\n";
											}
											text = aBuffer;
											if (inst.length == 1) {
												sb.append(text);
											} else {
												sb.append(text + "\n");
											}
											myReader.close();

										} catch (Exception e) {
											if (AppReference.isWriteInFile)
												AppReference.logger.error(
														e.getMessage(), e);
										}

										txt_view[i1].setText(text);

									}

									else if (inst[i1].contains("image")) {

										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.rsz_broken));
										iv_album[i1]
												.setContentDescription(instructions);

										iv_album[i1].setId(i1 + 1);
										final ImageView imgview = iv_album[i1];
										vfileCheck = new File(
												locaLpath
														+ imgview
																.getContentDescription()
																.toString());
										if (vfileCheck.exists())
											iv_album[i1]
													.setImageBitmap(callDisp
															.ResizeImage(locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString()));
										layoutt.addView(iv_album[i1], params);

										ArrayList<Object> list = new ArrayList<Object>();
										list.add(inst[i1]);
										list.add(iv_album[i1]);
										decode_task = new base64Decoder();
										decode_task.execute(list);

										imgview.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												vfileCheck = new File(
														locaLpath
																+ imgview
																		.getContentDescription()
																		.toString());
												if (vfileCheck.exists()) {

													Intent in = new Intent(
															context,
															PhotoZoomActivity.class);
													in.putExtra(
															"Photo_path",

															locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString());
													in.putExtra("type", false);
													in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
													startActivityForResult(in,
															10);
												} else {

												}

											}
										});

									}

									else if (inst[i1].contains("audio")) {
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1]
												.setContentDescription(instructions);

										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.v_play));

										iv_album[i1].setId(i1 + 1);

										final ImageView img = iv_album[i1];

										layoutt.addView(iv_album[i1], params);

										chatplayer = new MediaPlayer();

										img.setTag("0");

										img.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												vfileCheck = new File(
														locaLpath
																+ img.getContentDescription()
																		.toString());

												if (img.getTag().toString()
														.equals("1")) {
													img.setTag("0");
													bitmap = BitmapFactory
															.decodeResource(
																	getResources(),
																	R.drawable.v_play);
													img.setImageBitmap(bitmap);
													isaudioPlaying = false;

													chatplayer.stop();
													chatplayer.reset();
												} else {
													if (!isaudioPlaying) {

														if (vfileCheck.exists()) {

															try {
																chatplayer
																		.setDataSource(locaLpath
																				+ img.getContentDescription()
																						.toString());
																chatplayer
																		.setLooping(false);

																chatplayer
																		.prepare();
																chatplayer
																		.start();
																bitmap = BitmapFactory
																		.decodeResource(
																				getResources(),
																				R.drawable.v_stop);
																img.setImageBitmap(bitmap);
																img.setTag("1");
																isaudioPlaying = true;

															} catch (IllegalArgumentException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															} catch (IllegalStateException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															} catch (IOException e) {
																if (AppReference.isWriteInFile)
																	AppReference.logger
																			.error(e.getMessage(),
																					e);
																e.printStackTrace();
															}

															chatplayer
																	.setOnCompletionListener(new OnCompletionListener() {

																		@Override
																		public void onCompletion(
																				MediaPlayer mp) {

																			try {
																				chatplayer
																						.reset();
																				isaudioPlaying = false;

																				bitmap = BitmapFactory
																						.decodeResource(
																								getResources(),
																								R.drawable.v_play);
																				img.setImageBitmap(bitmap);

																				img.setTag("0");

																			} catch (IllegalArgumentException e) {
																				if (AppReference.isWriteInFile)
																					AppReference.logger
																							.error(e.getMessage(),
																									e);
																				e.printStackTrace();
																			} catch (IllegalStateException e) {
																				if (AppReference.isWriteInFile)
																					AppReference.logger
																							.error(e.getMessage(),
																									e);
																				e.printStackTrace();
																			}

																		}
																	});

														} else {
															Toast.makeText(
																	context,
																	"File Downloading",
																	1).show();
															downloadConfiguredNote(instructions);

														}
													} else {

														showToast(SingleInstance.mainContext
																.getResources()
																.getString(
																		R.string.please_stop_audio));
													}

												}

											}
										});

									}

									else if (inst[i1].contains("video")) {
										vfileCheck = new File(instructions);

										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												RelativeLayout.LayoutParams.WRAP_CONTENT,
												RelativeLayout.LayoutParams.WRAP_CONTENT);
										params.addRule(
												RelativeLayout.ALIGN_PARENT_TOP,
												RelativeLayout.ABOVE);
										iv_album[i1] = new ImageView(context);
										iv_album[i1].setPadding(30, 10, 30, 0);
										iv_album[i1].setImageDrawable(this
												.getResources().getDrawable(
														R.drawable.v_play));
										iv_album[i1]
												.setContentDescription(instructions);

										iv_album[i1].setId(i1 + 1);

										final ImageView img = iv_album[i1];

										layoutt.addView(iv_album[i1], params);

										iv_album[i1]
												.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
														vfileCheck = new File(
																locaLpath
																		+ img.getContentDescription()
																				.toString());

														if (vfileCheck.exists()) {
															if (!isaudioPlaying) {

																Intent intentVPlayer = new Intent(
																		context,
																		VideoPlayer.class);
																intentVPlayer
																		.putExtra(
																				"File_Path",

																				locaLpath
																						+ img.getContentDescription());
																intentVPlayer
																		.putExtra(
																				"Player_Type",
																				"Video Player");
																startActivity(intentVPlayer);
															}

															else {
																showToast(SingleInstance.mainContext
																		.getResources()
																		.getString(
																				R.string.please_stop_audio));

															}
														} else {

															Toast.makeText(
																	context,
																	"File Downloading",
																	1).show();
															downloadConfiguredNote(instructions);

														}

													}
												});

									}

								}

								TextView hint = (TextView) layout
										.findViewById(R.id.tview_clm_hint);

								if (errortip.length() == 0) {
									hint.setVisibility(View.GONE);
								} else {
									hint.setVisibility(View.VISIBLE);
									hint.setText("Error Hint:" + errortip);
									hint.setTextColor(getResources().getColor(
											R.color.orange));
								}

								final ImageView im = (ImageView) layout
										.findViewById(R.id.iview_dtype);
								im.setContentDescription(validdata);

								final EditText ed_fld = (EditText) layout
										.findViewById(R.id.edtxt_frmfield);

								ed_fld.setContentDescription(defaultvalue);

								ed_fld.setText(defaultvalue);
								ed_fld.setHint("Select a Date and Time");

								ed_fld.setFocusableInTouchMode(false);
								// if (permission != null
								// && permission.equalsIgnoreCase("F2")) {
								// im.setEnabled(false);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F2")) {
									im.setEnabled(false);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F2")) {
										im.setEnabled(false);
									} else {
										im.setEnabled(true);
									}
								}
								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {

										final String valString = im
												.getContentDescription()
												.toString();
										if (valString.length() > 0) {
											value = valString
													.replace("GT", "GT ")
													.replace("LT", "LT ")
													.replace("BW", "BW ")
													.replace("GT ", "GT ")
													.replace("LT ", "LT ")
													.replace("BW ", "BW ")
													.split(" ");

											if (value.length == 0) {
												value = valString
														.replace("GT", "GT ")
														.replace("LT", "LT ")
														.replace("BW", "BW ")
														.split(" ");

											}

										}

										if (valString.length() == 0
												|| valString
														.equalsIgnoreCase("date and time")
												|| valString == null) {

											Button btnSet;
											final DatePicker dp;
											final TimePicker tp;
											final AlertDialog alertReminder = new AlertDialog.Builder(
													context).create();

											ScrollView tblDTPicker = (ScrollView) View
													.inflate(
															context,
															R.layout.date_time_picker,
															null);

											btnSet = (Button) tblDTPicker
													.findViewById(R.id.btnSetDateTime);
											dp = (DatePicker) tblDTPicker
													.findViewById(R.id.datePicker);
											tp = (TimePicker) tblDTPicker
													.findViewById(R.id.timePicker);
											tp.setIs24HourView(true);
											;

											btnSet.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													pickgdate = +dp.getYear()
															+ "-"
															+ WebServiceReferences
																	.setLength2((dp
																			.getMonth() + 1))
															+ "-"
															+ WebServiceReferences
																	.setLength2(dp
																			.getDayOfMonth())
															+ " "
															+ WebServiceReferences
																	.setLength2(tp
																			.getCurrentHour())
															+ ":"
															+ WebServiceReferences
																	.setLength2(tp
																			.getCurrentMinute());
													ed_fld.setText(pickgdate);
													alertReminder.dismiss();

												}
											});
											alertReminder
													.setTitle("Date and Time");
											alertReminder.setView(tblDTPicker);
											alertReminder.show();

										}

										else if (value[0].startsWith("GT")) {

											Button btnSet;
											final DatePicker dp;
											final TimePicker tp;
											final AlertDialog alertReminder = new AlertDialog.Builder(
													context).create();

											ScrollView tblDTPicker = (ScrollView) View
													.inflate(
															context,
															R.layout.date_time_picker,
															null);

											btnSet = (Button) tblDTPicker
													.findViewById(R.id.btnSetDateTime);
											dp = (DatePicker) tblDTPicker
													.findViewById(R.id.datePicker);
											tp = (TimePicker) tblDTPicker
													.findViewById(R.id.timePicker);
											tp.setIs24HourView(true);

											btnSet.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													pickgdate = +dp.getYear()
															+ "-"
															+ WebServiceReferences
																	.setLength2((dp
																			.getMonth() + 1))
															+ "-"
															+ WebServiceReferences
																	.setLength2(dp
																			.getDayOfMonth())
															+ " "
															+ WebServiceReferences
																	.setLength2(tp
																			.getCurrentHour())
															+ ":"
															+ WebServiceReferences
																	.setLength2(tp
																			.getCurrentMinute());

													if (callDisp
															.CheckGreaterDateandTime(
																	pickgdate,
																	valString
																			.replace(
																					"GT",
																					"")
																			.trim())) {
														alertReminder.dismiss();
														Intent reminderIntent = new Intent(
																context,
																ReminderService.class);
														startService(reminderIntent);

														ed_fld.setText(pickgdate);
													} else {
														showToast("Kindly select Greater than date from  "
																+ value[1]
																+ " "
																+ value[2]);

													}

												}
											});
											alertReminder
													.setTitle("Date and Time");
											alertReminder.setView(tblDTPicker);
											alertReminder.show();

										} else if (value[0].startsWith("LT")) {

											Button btnSet;
											final DatePicker dp;
											final TimePicker tp;
											final AlertDialog alertReminder = new AlertDialog.Builder(
													context).create();

											ScrollView tblDTPicker = (ScrollView) View
													.inflate(
															context,
															R.layout.date_time_picker,
															null);

											btnSet = (Button) tblDTPicker
													.findViewById(R.id.btnSetDateTime);
											dp = (DatePicker) tblDTPicker
													.findViewById(R.id.datePicker);
											tp = (TimePicker) tblDTPicker
													.findViewById(R.id.timePicker);
											tp.setIs24HourView(true);
											;

											btnSet.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													pickldate = +dp.getYear()
															+ "-"
															+ WebServiceReferences
																	.setLength2((dp
																			.getMonth() + 1))
															+ "-"
															+ WebServiceReferences
																	.setLength2(dp
																			.getDayOfMonth())
															+ " "
															+ WebServiceReferences
																	.setLength2(tp
																			.getCurrentHour())
															+ ":"
															+ WebServiceReferences
																	.setLength2(tp
																			.getCurrentMinute());

													if (callDisp
															.CheckLessDateandTime(
																	pickldate,
																	valString
																			.replace(
																					"LT",
																					"")
																			.trim())) {
														alertReminder.dismiss();
														Intent reminderIntent = new Intent(
																context,
																ReminderService.class);
														startService(reminderIntent);

														ed_fld.setText(pickldate);
													} else {
														showToast("Kindly select Less than date from "
																+ value[1]
																+ " "
																+ value[2]);

													}
												}
											});
											alertReminder
													.setTitle("Date and Time");
											alertReminder.setView(tblDTPicker);
											alertReminder.show();
										} else if (value[0].startsWith("BW")) {

											final String[] date = valString
													.replace("and", ",")
													.replace("BW", "").trim()
													.split(",");

											Button btnSet;
											final DatePicker dp;
											final TimePicker tp;
											final AlertDialog alertReminder = new AlertDialog.Builder(
													context).create();

											ScrollView tblDTPicker = (ScrollView) View
													.inflate(
															context,
															R.layout.date_time_picker,
															null);

											btnSet = (Button) tblDTPicker
													.findViewById(R.id.btnSetDateTime);
											dp = (DatePicker) tblDTPicker
													.findViewById(R.id.datePicker);
											tp = (TimePicker) tblDTPicker
													.findViewById(R.id.timePicker);
											tp.setIs24HourView(true);

											btnSet.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													pickbdate = +dp.getYear()
															+ "-"
															+ WebServiceReferences
																	.setLength2((dp
																			.getMonth() + 1))
															+ "-"
															+ WebServiceReferences
																	.setLength2(dp
																			.getDayOfMonth())
															+ " "
															+ WebServiceReferences
																	.setLength2(tp
																			.getCurrentHour())
															+ ":"
															+ WebServiceReferences
																	.setLength2(tp
																			.getCurrentMinute());

													if (callDisp
															.Checkbetweendateandtime(
																	pickbdate,
																	date[0],
																	date[1])) {
														alertReminder.dismiss();
														Intent reminderIntent = new Intent(
																context,
																ReminderService.class);
														startService(reminderIntent);

														ed_fld.setText(pickbdate);
													}

													else {
														showToast("Kindly select in date between  "
																+ value[1]
																+ " "
																+ value[2]
																+ " to   "
																+ value[4]
																+ " "
																+ value[5]);

													}
												}
											});
											alertReminder
													.setTitle("Date and Time");
											alertReminder.setView(tblDTPicker);
											alertReminder.show();
										}

									}
								});

								if (dtypes.get(recs).trim()
										.equalsIgnoreCase("INTEGER")) {
									ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);
								} else if (dtypes.get(recs).trim()
										.equalsIgnoreCase("nvarchar(20)")) {
									ed_fld.setInputType(InputType.TYPE_CLASS_TEXT);
								}

								layout.setId(records_container.getChildCount());
								layout.setContentDescription(entrymode);

								records_container.addView(layout);

							} else if (dropdown.equalsIgnoreCase("numeric")) {

								entrymode = info_lists[3];
								validdata = info_lists[4];
								defaultvalue = info_lists[5];
								instruction = info_lists[6] + "," + ",";
								errortip = info_lists[7];

								value = validdata.split(" ");

								LinearLayout layout = (LinearLayout) layoutInflater
										.inflate(R.layout.formrecord_fields,
												null);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// layout.setVisibility(View.GONE);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F1")) {
									layout.setVisibility(View.GONE);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F1")) {
										layout.setVisibility(View.GONE);
									} else if (permission
											.equalsIgnoreCase("F2")) {
										layout.setVisibility(View.VISIBLE);
									} else {
										layout.setVisibility(View.VISIBLE);
									}
								}
								layout.findViewById(R.id.tview_clm);
								// if (permission != null
								// && permission.equalsIgnoreCase("F2")) {
								// layout.setEnabled(false);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F1")) {
									layout.setEnabled(false);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F2")) {
										layout.setEnabled(false);
									} else {
										layout.setEnabled(true);
									}
								}
								TextView tv = (TextView) layout
										.findViewById(R.id.tview_clm);
								TextView tv1 = (TextView) layout
										.findViewById(R.id.tview_clms);
								tv1.setText(recs);
								LinearLayout layoutt = (LinearLayout) layout
										.findViewById(R.id.tview_lay);

								if (instruction.equals(null)
										|| instruction.equals("")) {
									tv.setVisibility(View.GONE);
								} else {
									String[] inst = instruction.split(",");
									final ImageView[] iv_album = new ImageView[inst.length];
									final TextView[] txt_view = new TextView[inst.length];

									StringBuffer sb = new StringBuffer();

									for (i1 = 0; i1 < inst.length; i1++) {
										instructions = inst[i1];
										if (inst[i1].contains("note")) {
											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											txt_view[i1] = new TextView(context);
											txt_view[i1].setPadding(30, 10, 30,
													0);
											txt_view[i1].setText(text);

											layoutt.addView(txt_view[i1],
													params);

											try {
												File myFile = new File(
														"/sdcard/COMMedia/"
																+ instructions);
												FileInputStream fIn = new FileInputStream(
														myFile);
												BufferedReader myReader = new BufferedReader(
														new InputStreamReader(
																fIn));
												String aDataRow = "";
												String aBuffer = "";
												while ((aDataRow = myReader
														.readLine()) != null) {
													aBuffer += aDataRow + "\n";
												}
												text = aBuffer;
												if (inst.length == 1) {
													sb.append(text);
												} else {
													sb.append(text + "\n");
												}
												myReader.close();

											} catch (Exception e) {
												if (AppReference.isWriteInFile)
													AppReference.logger.error(
															e.getMessage(), e);
											}

											txt_view[i1].setText(text);

										}

										else if (inst[i1].contains("image")) {

											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1]
													.setImageDrawable(this
															.getResources()
															.getDrawable(
																	R.drawable.rsz_broken));
											iv_album[i1]
													.setContentDescription(instructions);

											iv_album[i1].setId(i1 + 1);

											final ImageView imgview = iv_album[i1];
											vfileCheck = new File(
													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											if (vfileCheck.exists())
												iv_album[i1]
														.setImageBitmap(callDisp
																.ResizeImage(locaLpath
																		+ imgview
																				.getContentDescription()
																				.toString()));
											layoutt.addView(iv_album[i1],
													params);

											ArrayList<Object> list = new ArrayList<Object>();
											list.add(inst[i1]);
											list.add(iv_album[i1]);
											decode_task = new base64Decoder();
											decode_task.execute(list);

											imgview.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													vfileCheck = new File(
															locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString());
													if (vfileCheck.exists()) {

														Intent in = new Intent(
																context,
																PhotoZoomActivity.class);
														in.putExtra(
																"Photo_path",

																locaLpath
																		+ imgview
																				.getContentDescription()
																				.toString());
														in.putExtra("type",
																false);
														in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
														startActivityForResult(
																in, 10);
													} else {

													}

												}
											});

										}

										else if (inst[i1].contains("audio")) {
											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1]
													.setContentDescription(instructions);

											iv_album[i1].setImageDrawable(this
													.getResources()
													.getDrawable(
															R.drawable.v_play));

											iv_album[i1].setId(i1 + 1);

											final ImageView img = iv_album[i1];

											layoutt.addView(iv_album[i1],
													params);

											vfileCheck = new File(locaLpath
													+ instructions);
											chatplayer = new MediaPlayer();

											img.setTag("0");

											img.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													vfileCheck = new File(
															locaLpath
																	+ img.getContentDescription()
																			.toString());

													if (img.getTag().toString()
															.equals("1")) {
														img.setTag("0");
														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_play);
														img.setImageBitmap(bitmap);
														isaudioPlaying = false;

														chatplayer.stop();
														chatplayer.reset();
													} else {
														if (!isaudioPlaying) {

															if (vfileCheck
																	.exists()) {

																try {
																	chatplayer
																			.setDataSource(locaLpath
																					+ img.getContentDescription()
																							.toString());
																	chatplayer
																			.setLooping(false);

																	chatplayer
																			.prepare();
																	chatplayer
																			.start();
																	bitmap = BitmapFactory
																			.decodeResource(
																					getResources(),
																					R.drawable.v_stop);
																	img.setImageBitmap(bitmap);
																	img.setTag("1");
																	isaudioPlaying = true;

																} catch (IllegalArgumentException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																} catch (IllegalStateException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																} catch (IOException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																}

																chatplayer
																		.setOnCompletionListener(new OnCompletionListener() {

																			@Override
																			public void onCompletion(
																					MediaPlayer mp) {

																				try {
																					chatplayer
																							.reset();
																					isaudioPlaying = false;

																					bitmap = BitmapFactory
																							.decodeResource(
																									getResources(),
																									R.drawable.v_play);
																					img.setImageBitmap(bitmap);

																					img.setTag("0");

																				} catch (IllegalArgumentException e) {
																					if (AppReference.isWriteInFile)
																						AppReference.logger
																								.error(e.getMessage(),
																										e);
																					e.printStackTrace();
																				} catch (IllegalStateException e) {
																					if (AppReference.isWriteInFile)
																						AppReference.logger
																								.error(e.getMessage(),
																										e);
																					e.printStackTrace();
																				}

																			}
																		});

															} else {
																Toast.makeText(
																		context,
																		"File Downloading",
																		1)
																		.show();
																downloadConfiguredNote(instructions);

															}
														} else {

															showToast(SingleInstance.mainContext
																	.getResources()
																	.getString(
																			R.string.please_stop_audio));
														}

													}

												}
											});

										}

										else if (inst[i1].contains("video")) {

											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1].setImageDrawable(this
													.getResources()
													.getDrawable(
															R.drawable.v_play));
											iv_album[i1]
													.setContentDescription(instructions);

											iv_album[i1].setId(i1 + 1);

											final ImageView img = iv_album[i1];

											layoutt.addView(iv_album[i1],
													params);

											iv_album[i1]
													.setOnClickListener(new OnClickListener() {

														@Override
														public void onClick(
																View v) {
															vfileCheck = new File(
																	locaLpath
																			+ img.getContentDescription()
																					.toString());

															if (vfileCheck
																	.exists()) {
																if (!isaudioPlaying) {

																	Intent intentVPlayer = new Intent(
																			context,
																			VideoPlayer.class);
																	intentVPlayer
																			.putExtra(
																					"File_Path",
																					locaLpath
																							+ img.getContentDescription());
																	intentVPlayer
																			.putExtra(
																					"Player_Type",
																					"Video Player");
																	startActivity(intentVPlayer);
																}

																else {
																	showToast("Kindly stop audio playing");

																}
															} else {

																Toast.makeText(
																		context,
																		"File Downloading",
																		1)
																		.show();
																downloadConfiguredNote(instructions);

															}

														}
													});

										}

									}

									tv.setText(sb.toString());

								}

								final ImageView im = (ImageView) layout
										.findViewById(R.id.iview_dtype);
								im.setContentDescription("");
								im.setVisibility(View.GONE);
								EditText ed_fld = (EditText) layout
										.findViewById(R.id.edtxt_frmfield);
								ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);

								if (defaultvalue.length() > 0) {
									ed_fld.setContentDescription(defaultvalue);

								} else {
									ed_fld.setContentDescription("");

								}
								InputFilter[] FilterArray = new InputFilter[1];
								FilterArray[0] = new InputFilter.LengthFilter(
										10);
								ed_fld.setFilters(FilterArray);
								ed_fld.setText(defaultvalue);
								ed_fld.setHint("Enter a Number");
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// im.setEnabled(false);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F2")) {
									im.setEnabled(false);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F2")) {
										im.setEnabled(false);
									} else {
										im.setEnabled(true);
									}
								}
								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										addView(v, im.getContentDescription()
												.toString());
									}
								});

								layout.setId(records_container.getChildCount());
								layout.setContentDescription(entrymode);

								records_container.addView(layout);

							} else if (dropdown
									.equalsIgnoreCase("current time")) {

								entrymode = info_lists[3];
								validdata = info_lists[4];
								defaultvalue = info_lists[5];
								instruction = info_lists[6] + ",";
								errortip = info_lists[7];

								LinearLayout layout = (LinearLayout) layoutInflater
										.inflate(R.layout.formrecord_fields,
												null);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// layout.setVisibility(View.GONE);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F1")) {
									layout.setVisibility(View.GONE);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F1")) {
										layout.setVisibility(View.GONE);
									} else if (permission
											.equalsIgnoreCase("F2")) {
										layout.setVisibility(View.VISIBLE);
									} else {
										layout.setVisibility(View.VISIBLE);
									}
								}
								TextView tv = (TextView) layout
										.findViewById(R.id.tview_clm);
								TextView tv1 = (TextView) layout
										.findViewById(R.id.tview_clms);
								tv1.setText(recs);
								LinearLayout layoutt = (LinearLayout) layout
										.findViewById(R.id.tview_lay);

								if (instruction.equals(null)
										|| instruction.equals("")) {
									tv.setVisibility(View.GONE);
								} else {
									String[] inst = instruction.split(",");
									final ImageView[] iv_album = new ImageView[inst.length];
									final TextView[] txt_view = new TextView[inst.length];

									StringBuffer sb = new StringBuffer();

									for (i1 = 0; i1 < inst.length; i1++) {
										instructions = inst[i1];
										if (inst[i1].contains("note")) {
											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											txt_view[i1] = new TextView(context);
											txt_view[i1].setPadding(30, 10, 30,
													0);
											txt_view[i1].setText(text);

											layoutt.addView(txt_view[i1],
													params);

											try {
												File myFile = new File(
														"/sdcard/COMMedia/"
																+ instructions);
												FileInputStream fIn = new FileInputStream(
														myFile);
												BufferedReader myReader = new BufferedReader(
														new InputStreamReader(
																fIn));
												String aDataRow = "";
												String aBuffer = "";
												while ((aDataRow = myReader
														.readLine()) != null) {
													aBuffer += aDataRow + "\n";
												}
												text = aBuffer;
												if (inst.length == 1) {
													sb.append(text);
												} else {
													sb.append(text + "\n");
												}
												myReader.close();

											} catch (Exception e) {
												if (AppReference.isWriteInFile)
													AppReference.logger.error(
															e.getMessage(), e);
											}

											txt_view[i1].setText(text);

										}

										else if (inst[i1].contains("image")) {

											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1]
													.setImageDrawable(this
															.getResources()
															.getDrawable(
																	R.drawable.rsz_broken));
											iv_album[i1]
													.setContentDescription(instructions);
											iv_album[i1].setId(i1 + 1);
											final ImageView imgview = iv_album[i1];
											vfileCheck = new File(
													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											if (vfileCheck.exists())
												iv_album[i1]
														.setImageBitmap(callDisp
																.ResizeImage(locaLpath
																		+ imgview
																				.getContentDescription()
																				.toString()));
											layoutt.addView(iv_album[i1],
													params);

											ArrayList<Object> list = new ArrayList<Object>();
											list.add(inst[i1]);
											list.add(iv_album[i1]);
											decode_task = new base64Decoder();
											decode_task.execute(list);

											imgview.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													vfileCheck = new File(
															locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString());
													if (vfileCheck.exists()) {

														Intent in = new Intent(
																context,
																PhotoZoomActivity.class);
														in.putExtra(
																"Photo_path",

																locaLpath
																		+ imgview
																				.getContentDescription()
																				.toString());
														in.putExtra("type",
																false);
														in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
														startActivityForResult(
																in, 10);
													} else {

													}

												}
											});

										}

										else if (inst[i1].contains("audio")) {
											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1]
													.setContentDescription(instructions);

											iv_album[i1].setImageDrawable(this
													.getResources()
													.getDrawable(
															R.drawable.v_play));

											iv_album[i1].setId(i1 + 1);

											final ImageView img = iv_album[i1];

											layoutt.addView(iv_album[i1],
													params);

											vfileCheck = new File(locaLpath
													+ instructions);
											chatplayer = new MediaPlayer();

											img.setTag("0");

											img.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {

													if (img.getTag().toString()
															.equals("1")) {
														img.setTag("0");
														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_play);
														img.setImageBitmap(bitmap);
														isaudioPlaying = false;

														chatplayer.stop();
														chatplayer.reset();
													} else {
														if (!isaudioPlaying) {

															if (vfileCheck
																	.exists()) {

																try {
																	chatplayer
																			.setDataSource(locaLpath
																					+ img.getContentDescription()
																							.toString());
																	chatplayer
																			.setLooping(false);

																	chatplayer
																			.prepare();
																	chatplayer
																			.start();
																	bitmap = BitmapFactory
																			.decodeResource(
																					getResources(),
																					R.drawable.v_stop);
																	img.setImageBitmap(bitmap);
																	img.setTag("1");
																	isaudioPlaying = true;

																} catch (IllegalArgumentException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																} catch (IllegalStateException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																} catch (IOException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																}

																chatplayer
																		.setOnCompletionListener(new OnCompletionListener() {

																			@Override
																			public void onCompletion(
																					MediaPlayer mp) {

																				try {
																					chatplayer
																							.reset();
																					isaudioPlaying = false;

																					bitmap = BitmapFactory
																							.decodeResource(
																									getResources(),
																									R.drawable.v_play);
																					img.setImageBitmap(bitmap);

																					img.setTag("0");

																				} catch (IllegalArgumentException e) {
																					if (AppReference.isWriteInFile)
																						AppReference.logger
																								.error(e.getMessage(),
																										e);
																					e.printStackTrace();
																				} catch (IllegalStateException e) {
																					if (AppReference.isWriteInFile)
																						AppReference.logger
																								.error(e.getMessage(),
																										e);
																					e.printStackTrace();
																				}

																			}
																		});

															} else {
																Toast.makeText(
																		context,
																		"File Downloading",
																		1)
																		.show();
																downloadConfiguredNote(instructions);

															}
														} else {

															showToast(SingleInstance.mainContext
																	.getResources()
																	.getString(
																			R.string.please_stop_audio));
														}

													}

												}
											});

										}

										else if (inst[i1].contains("video")) {

											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1].setImageDrawable(this
													.getResources()
													.getDrawable(
															R.drawable.v_play));
											iv_album[i1]
													.setContentDescription(instructions);

											iv_album[i1].setId(i1 + 1);

											final ImageView img = iv_album[i1];

											layoutt.addView(iv_album[i1],
													params);

											iv_album[i1]
													.setOnClickListener(new OnClickListener() {

														@Override
														public void onClick(
																View v) {
															vfileCheck = new File(
																	locaLpath
																			+ img.getContentDescription()
																					.toString());

															if (vfileCheck
																	.exists()) {

																if (!isaudioPlaying) {

																	Intent intentVPlayer = new Intent(
																			context,
																			VideoPlayer.class);
																	intentVPlayer
																			.putExtra(
																					"File_Path",

																					locaLpath
																							+ img.getContentDescription());
																	intentVPlayer
																			.putExtra(
																					"Player_Type",
																					"Video Player");
																	startActivity(intentVPlayer);
																}

																else {
																	showToast(SingleInstance.mainContext
																			.getResources()
																			.getString(
																					R.string.please_stop_audio));

																}
															} else {

																Toast.makeText(
																		context,
																		"File Downloading",
																		1)
																		.show();
																downloadConfiguredNote(instructions);

															}

														}
													});

										}

									}

									tv.setText(sb.toString());

								}

								final ImageView im = (ImageView) layout
										.findViewById(R.id.iview_dtype);
								im.setContentDescription("");
								im.setVisibility(View.GONE);
								final EditText ed_fld = (EditText) layout
										.findViewById(R.id.edtxt_frmfield);
								ed_fld.setContentDescription(defaultvalue);

								ed_fld.setText(getCurrentTime());

								ed_fld.setFocusableInTouchMode(false);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// ed_fld.setEnabled(false);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F2")) {
									ed_fld.setEnabled(false);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F2")) {
										ed_fld.setEnabled(false);
									} else {
										ed_fld.setEnabled(true);
									}
								}
								ed_fld.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										ed_fld.setText(getCurrentTime());

									}
								});

								layout.setId(records_container.getChildCount());
								layout.setContentDescription(entrymode);

								records_container.addView(layout);

							} else if (dropdown
									.equalsIgnoreCase("current date")) {

								entrymode = info_lists[3];
								validdata = info_lists[4];
								defaultvalue = info_lists[5];
								instruction = info_lists[6] + ",";
								errortip = info_lists[7];

								LinearLayout layout = (LinearLayout) layoutInflater
										.inflate(R.layout.formrecord_fields,
												null);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// layout.setVisibility(View.GONE);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F1")) {
									layout.setVisibility(View.GONE);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F1")) {
										layout.setVisibility(View.GONE);
									} else if (permission
											.equalsIgnoreCase("F2")) {
										layout.setVisibility(View.VISIBLE);
									} else {
										layout.setVisibility(View.VISIBLE);
									}
								}
								TextView tv = (TextView) layout
										.findViewById(R.id.tview_clm);
								TextView tv1 = (TextView) layout
										.findViewById(R.id.tview_clms);
								tv1.setText(recs);

								LinearLayout layoutt = (LinearLayout) layout
										.findViewById(R.id.tview_lay);

								if (instruction.equals(null)
										|| instruction.equals("")) {
									tv.setVisibility(View.GONE);
								} else {
									String[] inst = instruction.split(",");
									final ImageView[] iv_album = new ImageView[inst.length];
									final TextView[] txt_view = new TextView[inst.length];

									StringBuffer sb = new StringBuffer();

									for (i1 = 0; i1 < inst.length; i1++) {
										instructions = inst[i1];
										if (inst[i1].contains("note")) {
											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											txt_view[i1] = new TextView(context);
											txt_view[i1].setPadding(30, 10, 30,
													0);
											txt_view[i1].setText(text);

											layoutt.addView(txt_view[i1],
													params);

											try {
												File myFile = new File(
														"/sdcard/COMMedia/"
																+ instructions);
												FileInputStream fIn = new FileInputStream(
														myFile);
												BufferedReader myReader = new BufferedReader(
														new InputStreamReader(
																fIn));
												String aDataRow = "";
												String aBuffer = "";
												while ((aDataRow = myReader
														.readLine()) != null) {
													aBuffer += aDataRow + "\n";
												}
												text = aBuffer;
												if (inst.length == 1) {
													sb.append(text);
												} else {
													sb.append(text + "\n");
												}
												myReader.close();

											} catch (Exception e) {
												if (AppReference.isWriteInFile)
													AppReference.logger.error(
															e.getMessage(), e);
											}

											txt_view[i1].setText(text);

										}

										else if (inst[i1].contains("image")) {

											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1]
													.setImageDrawable(this
															.getResources()
															.getDrawable(
																	R.drawable.rsz_broken));
											iv_album[i1]
													.setContentDescription(instructions);
											iv_album[i1].setId(i1 + 1);
											final ImageView imgview = iv_album[i1];
											vfileCheck = new File(
													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											if (vfileCheck.exists())
												iv_album[i1]
														.setImageBitmap(callDisp
																.ResizeImage(locaLpath
																		+ imgview
																				.getContentDescription()
																				.toString()));
											layoutt.addView(iv_album[i1],
													params);

											ArrayList<Object> list = new ArrayList<Object>();
											list.add(inst[i1]);
											list.add(iv_album[i1]);
											decode_task = new base64Decoder();
											decode_task.execute(list);

											imgview.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													vfileCheck = new File(
															locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString());
													if (vfileCheck.exists()) {

														Intent in = new Intent(
																context,
																PhotoZoomActivity.class);
														in.putExtra(
																"Photo_path",

																locaLpath
																		+ imgview
																				.getContentDescription()
																				.toString());
														in.putExtra("type",
																false);
														in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
														startActivityForResult(
																in, 10);
													} else {

													}

												}
											});

										}

										else if (inst[i1].contains("audio")) {
											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1]
													.setContentDescription(instructions);

											iv_album[i1].setImageDrawable(this
													.getResources()
													.getDrawable(
															R.drawable.v_play));

											iv_album[i1].setId(i1 + 1);

											final ImageView img = iv_album[i1];

											layoutt.addView(iv_album[i1],
													params);

											vfileCheck = new File(locaLpath
													+ instructions);
											chatplayer = new MediaPlayer();

											img.setTag("0");

											img.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {

													if (img.getTag().toString()
															.equals("1")) {
														img.setTag("0");
														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_play);
														img.setImageBitmap(bitmap);
														isaudioPlaying = false;

														chatplayer.stop();
														chatplayer.reset();
													} else {
														if (!isaudioPlaying) {

															if (vfileCheck
																	.exists()) {

																try {
																	chatplayer
																			.setDataSource(locaLpath
																					+ img.getContentDescription()
																							.toString());
																	chatplayer
																			.setLooping(false);

																	chatplayer
																			.prepare();
																	chatplayer
																			.start();
																	bitmap = BitmapFactory
																			.decodeResource(
																					getResources(),
																					R.drawable.v_stop);
																	img.setImageBitmap(bitmap);
																	img.setTag("1");
																	isaudioPlaying = true;

																} catch (IllegalArgumentException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																} catch (IllegalStateException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																} catch (IOException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																}

																chatplayer
																		.setOnCompletionListener(new OnCompletionListener() {

																			@Override
																			public void onCompletion(
																					MediaPlayer mp) {

																				try {
																					chatplayer
																							.reset();
																					isaudioPlaying = false;

																					bitmap = BitmapFactory
																							.decodeResource(
																									getResources(),
																									R.drawable.v_play);
																					img.setImageBitmap(bitmap);

																					img.setTag("0");

																				} catch (IllegalArgumentException e) {
																					if (AppReference.isWriteInFile)
																						AppReference.logger
																								.error(e.getMessage(),
																										e);
																					e.printStackTrace();
																				} catch (IllegalStateException e) {
																					if (AppReference.isWriteInFile)
																						AppReference.logger
																								.error(e.getMessage(),
																										e);
																					e.printStackTrace();
																				}

																			}
																		});

															} else {
																Toast.makeText(
																		context,
																		"File Downloading",
																		1)
																		.show();
																downloadConfiguredNote(instructions);

															}
														} else {

															showToast(SingleInstance.mainContext
																	.getResources()
																	.getString(
																			R.string.please_stop_audio));
														}

													}

												}
											});
										}

										else if (inst[i1].contains("video")) {

											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1].setImageDrawable(this
													.getResources()
													.getDrawable(
															R.drawable.v_play));
											iv_album[i1]
													.setContentDescription(instructions);

											iv_album[i1].setId(i1 + 1);

											final ImageView img = iv_album[i1];

											layoutt.addView(iv_album[i1],
													params);

											iv_album[i1]
													.setOnClickListener(new OnClickListener() {

														@Override
														public void onClick(
																View v) {
															vfileCheck = new File(
																	locaLpath
																			+ img.getContentDescription()
																					.toString());

															if (vfileCheck
																	.exists()) {
																if (!isaudioPlaying) {

																	Intent intentVPlayer = new Intent(
																			context,
																			VideoPlayer.class);
																	intentVPlayer
																			.putExtra(
																					"File_Path",

																					locaLpath
																							+ img.getContentDescription());
																	intentVPlayer
																			.putExtra(
																					"Player_Type",
																					"Video Player");
																	startActivity(intentVPlayer);
																}

																else {
																	showToast("Kindly stop audio playing");

																}
															} else {

																Toast.makeText(
																		context,
																		"File Downloading",
																		1)
																		.show();
																downloadConfiguredNote(instructions);

															}

														}
													});

										}

									}

									tv.setText(sb.toString());

								}

								final ImageView im = (ImageView) layout
										.findViewById(R.id.iview_dtype);
								im.setContentDescription("");
								im.setVisibility(View.GONE);
								final EditText ed_fld = (EditText) layout
										.findViewById(R.id.edtxt_frmfield);
								ed_fld.setContentDescription(defaultvalue);

								ed_fld.setText(getCurrentDate());

								ed_fld.setFocusableInTouchMode(false);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// ed_fld.setEnabled(false);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F2")) {
									ed_fld.setEnabled(false);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F2")) {
										ed_fld.setEnabled(false);
									} else {
										ed_fld.setEnabled(true);
									}
								}
								ed_fld.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										ed_fld.setText(getCurrentDate());

									}
								});

								layout.setId(records_container.getChildCount());
								layout.setContentDescription(entrymode);

								records_container.addView(layout);

							} else if (dropdown.equalsIgnoreCase("compute")) {

								entrymode = info_lists[3];
								validdata = info_lists[4];
								defaultvalue = info_lists[5];
								instruction = info_lists[6] + ",";
								errortip = info_lists[7];

								value = validdata.split(" ");
								for (int i = 0; i < value.length; i++) {
									if (value[0].equalsIgnoreCase("NM")) {
										entrymodefield = "numeric";
										formula = validdata.replace("NM", "");

									} else if (value[0].equalsIgnoreCase("FT")) {
										entrymodefield = "free text";
										formula = validdata.replace("FT", "");

									} else if (value[0].equalsIgnoreCase("DT")) {
										entrymodefield = "date";
										formula = validdata.replace("DT", "");

									}

								}
								Log.i("forms123", "compute formula2 : "
										+ formula);
								LinearLayout layout = (LinearLayout) layoutInflater
										.inflate(R.layout.formrecord_fields,
												null);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// layout.setVisibility(View.GONE);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F1")) {
									layout.setVisibility(View.GONE);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F1")) {
										layout.setVisibility(View.GONE);
									} else if (permission
											.equalsIgnoreCase("F2")) {
										layout.setVisibility(View.VISIBLE);
									} else {
										layout.setVisibility(View.VISIBLE);
									}
								}
								layout.findViewById(R.id.tview_clm);
								TextView tv = (TextView) layout
										.findViewById(R.id.tview_clm);
								TextView tv1 = (TextView) layout
										.findViewById(R.id.tview_clms);
								tv1.setText(recs);

								final EditText edtxt = (EditText) layout
										.findViewById(R.id.edtxt_frmfield);
								edtxt.setFocusableInTouchMode(false);
								edtxt.setText(defaultvalue);
								edtxt.setHint(SingleInstance.mainContext
										.getResources()
										.getString(
												R.string.tap_to_get_value_computed));
								edtxt.setTag(entrymodefield);
								edtxt.setContentDescription(formula);
								edtxt.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										String entrymodefields = edtxt.getTag()
												.toString();
										if (entrymodefields
												.equalsIgnoreCase("Numeric")) {
											getDisplay(entrymodefields, edtxt
													.getContentDescription()
													.toString());

											try {

												Interpreter interpreter = new Interpreter();
												interpreter.eval("result="
														+ symbol);
												edtxt.setText(""
														+ interpreter
																.get("result"));
											} catch (EvalError e) {
												showToast(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.add_lessthan_ten_digit));
												if (AppReference.isWriteInFile)
													AppReference.logger.error(
															e.getMessage(), e);
												e.printStackTrace();
											}
										} else if (entrymodefields
												.equalsIgnoreCase("Free Text")) {
											getDisplay(entrymodefields, edtxt
													.getContentDescription()
													.toString());

											edtxt.setText(symbol);
										} else if (entrymodefields
												.equalsIgnoreCase("Date")) {
											getDisplay(entrymodefields, edtxt
													.getContentDescription()
													.toString());

											edtxt.setText(symbol);

										}

									}

								});

								LinearLayout layoutt = (LinearLayout) layout
										.findViewById(R.id.tview_lay);

								if (instruction.equals(null)
										|| instruction.equals("")) {
									tv.setVisibility(View.GONE);
								} else {
									String[] inst = instruction.split(",");
									final ImageView[] iv_album = new ImageView[inst.length];
									final TextView[] txt_view = new TextView[inst.length];

									StringBuffer sb = new StringBuffer();

									for (i1 = 0; i1 < inst.length; i1++) {
										instructions = inst[i1];
										if (inst[i1].contains("note")) {
											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											txt_view[i1] = new TextView(context);
											txt_view[i1].setPadding(30, 10, 30,
													0);
											txt_view[i1].setText(text);

											layoutt.addView(txt_view[i1],
													params);

											try {
												File myFile = new File(
														"/sdcard/COMMedia/"
																+ instructions);
												FileInputStream fIn = new FileInputStream(
														myFile);
												BufferedReader myReader = new BufferedReader(
														new InputStreamReader(
																fIn));
												String aDataRow = "";
												String aBuffer = "";
												while ((aDataRow = myReader
														.readLine()) != null) {
													aBuffer += aDataRow + "\n";
												}
												text = aBuffer;
												if (inst.length == 1) {
													sb.append(text);
												} else {
													sb.append(text + "\n");
												}
												myReader.close();

											} catch (Exception e) {
												if (AppReference.isWriteInFile)
													AppReference.logger.error(
															e.getMessage(), e);
											}

											txt_view[i1].setText(text);

										}

										else if (inst[i1].contains("image")) {

											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1]
													.setImageDrawable(this
															.getResources()
															.getDrawable(
																	R.drawable.rsz_broken));
											iv_album[i1]
													.setContentDescription(instructions);
											iv_album[i1].setId(i1 + 1);
											final ImageView imgview = iv_album[i1];
											vfileCheck = new File(
													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											if (vfileCheck.exists())
												iv_album[i1]
														.setImageBitmap(callDisp
																.ResizeImage(locaLpath
																		+ imgview
																				.getContentDescription()
																				.toString()));
											layoutt.addView(iv_album[i1],
													params);

											ArrayList<Object> list = new ArrayList<Object>();
											list.add(inst[i1]);
											list.add(iv_album[i1]);
											decode_task = new base64Decoder();
											decode_task.execute(list);

											imgview.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													vfileCheck = new File(
															locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString());
													if (vfileCheck.exists()) {

														Intent in = new Intent(
																context,
																PhotoZoomActivity.class);
														in.putExtra(
																"Photo_path",

																locaLpath
																		+ imgview
																				.getContentDescription()
																				.toString());
														in.putExtra("type",
																false);
														in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
														startActivityForResult(
																in, 10);
													} else {

													}

												}
											});

										}

										else if (inst[i1].contains("audio")) {
											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1]
													.setContentDescription(instructions);

											iv_album[i1].setImageDrawable(this
													.getResources()
													.getDrawable(
															R.drawable.v_play));

											iv_album[i1].setId(i1 + 1);

											final ImageView img = iv_album[i1];

											layoutt.addView(iv_album[i1],
													params);

											vfileCheck = new File(locaLpath
													+ instructions);
											chatplayer = new MediaPlayer();
											img.setTag("0");

											img.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {

													if (img.getTag().toString()
															.equals("1")) {
														img.setTag("0");
														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_play);
														img.setImageBitmap(bitmap);
														isaudioPlaying = false;

														chatplayer.stop();
														chatplayer.reset();
													} else {
														if (!isaudioPlaying) {

															if (vfileCheck
																	.exists()) {

																try {
																	chatplayer
																			.setDataSource(locaLpath
																					+ img.getContentDescription()
																							.toString());
																	chatplayer
																			.setLooping(false);

																	chatplayer
																			.prepare();
																	chatplayer
																			.start();
																	bitmap = BitmapFactory
																			.decodeResource(
																					getResources(),
																					R.drawable.v_stop);
																	img.setImageBitmap(bitmap);
																	img.setTag("1");
																	isaudioPlaying = true;

																} catch (IllegalArgumentException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																} catch (IllegalStateException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																} catch (IOException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																}

																chatplayer
																		.setOnCompletionListener(new OnCompletionListener() {

																			@Override
																			public void onCompletion(
																					MediaPlayer mp) {

																				try {
																					chatplayer
																							.reset();
																					isaudioPlaying = false;

																					bitmap = BitmapFactory
																							.decodeResource(
																									getResources(),
																									R.drawable.v_play);
																					img.setImageBitmap(bitmap);

																					img.setTag("0");

																				} catch (IllegalArgumentException e) {
																					if (AppReference.isWriteInFile)
																						AppReference.logger
																								.error(e.getMessage(),
																										e);
																					e.printStackTrace();
																				} catch (IllegalStateException e) {
																					if (AppReference.isWriteInFile)
																						AppReference.logger
																								.error(e.getMessage(),
																										e);
																					e.printStackTrace();
																				}

																			}
																		});

															} else {
																Toast.makeText(
																		context,
																		"File Downloading",
																		1)
																		.show();
																downloadConfiguredNote(instructions);

															}
														} else {

															showToast(SingleInstance.mainContext
																	.getResources()
																	.getString(
																			R.string.please_stop_audio));
														}

													}

												}
											});
										}

										else if (inst[i1].contains("video")) {

											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1].setImageDrawable(this
													.getResources()
													.getDrawable(
															R.drawable.v_play));
											iv_album[i1]
													.setContentDescription(instructions);

											iv_album[i1].setId(i1 + 1);

											final ImageView img = iv_album[i1];

											layoutt.addView(iv_album[i1],
													params);

											iv_album[i1]
													.setOnClickListener(new OnClickListener() {

														@Override
														public void onClick(
																View v) {
															vfileCheck = new File(
																	locaLpath
																			+ img.getContentDescription()
																					.toString());

															if (vfileCheck
																	.exists()) {
																if (!isaudioPlaying) {

																	Intent intentVPlayer = new Intent(
																			context,
																			VideoPlayer.class);
																	intentVPlayer
																			.putExtra(
																					"File_Path",

																					locaLpath
																							+ img.getContentDescription());
																	intentVPlayer
																			.putExtra(
																					"Player_Type",
																					"Video Player");
																	startActivity(intentVPlayer);
																}

																else {
																	showToast(SingleInstance.mainContext
																			.getResources()
																			.getString(
																					R.string.please_stop_audio));

																}
															} else {

																Toast.makeText(
																		context,
																		"File Downloading",
																		1)
																		.show();
																downloadConfiguredNote(instructions);

															}

														}
													});
										}

									}

									tv.setText(sb.toString());

								}

								final ImageView im = (ImageView) layout
										.findViewById(R.id.iview_dtype);
								im.setContentDescription("");
								im.setVisibility(View.GONE);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// im.setEnabled(false);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F2")) {
									layout.setVisibility(View.GONE);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F2")) {
										im.setEnabled(false);
									} else {
										im.setEnabled(false);
									}
								}
								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										addView(v, im.getContentDescription()
												.toString());
									}
								});

								layout.setId(records_container.getChildCount());
								layout.setContentDescription(entrymode);

								records_container.addView(layout);

							} else {

								entrymode = info_lists[3];
								validdata = info_lists[4];
								defaultvalue = info_lists[5];
								instruction = info_lists[6] + ",";
								errortip = info_lists[7];
								LinearLayout layout = (LinearLayout) layoutInflater
										.inflate(R.layout.formrecord_fields,
												null);
								layout.findViewById(R.id.tview_clm);
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// layout.setVisibility(View.GONE);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F1")) {
									layout.setVisibility(View.GONE);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F1")) {
										layout.setVisibility(View.GONE);
									} else if (permission
											.equalsIgnoreCase("F2")) {
										layout.setVisibility(View.VISIBLE);
									} else {
										layout.setVisibility(View.VISIBLE);
									}
								}
								TextView tv = (TextView) layout
										.findViewById(R.id.tview_clm);
								TextView tv1 = (TextView) layout
										.findViewById(R.id.tview_clms);
								tv1.setText(recs);

								LinearLayout layoutt = (LinearLayout) layout
										.findViewById(R.id.tview_lay);

								if (instruction.equals(null)
										|| instruction.equals("")) {
									tv.setVisibility(View.GONE);
								} else {
									StringBuffer sb = new StringBuffer();

									String[] inst = instruction.split(",");
									final ImageView[] iv_album = new ImageView[inst.length];
									final TextView[] txt_view = new TextView[inst.length];

									for (i1 = 0; i1 < inst.length; i1++) {
										instructions = inst[i1];
										if (inst[i1].contains("note")) {
											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											txt_view[i1] = new TextView(context);
											txt_view[i1].setPadding(30, 10, 30,
													0);

											txt_view[i1].setText(text);

											layoutt.addView(txt_view[i1],
													params);

											try {
												File myFile = new File(
														"/sdcard/COMMedia/"
																+ instructions);
												FileInputStream fIn = new FileInputStream(
														myFile);
												BufferedReader myReader = new BufferedReader(
														new InputStreamReader(
																fIn));
												String aDataRow = "";
												String aBuffer = "";
												while ((aDataRow = myReader
														.readLine()) != null) {
													aBuffer += aDataRow + "\n";
												}
												text = aBuffer;
												if (inst.length == 1) {
													sb.append(text);
												} else {
													sb.append(text + "\n");
												}
												myReader.close();

											} catch (Exception e) {
												if (AppReference.isWriteInFile)
													AppReference.logger.error(
															e.getMessage(), e);
											}

											txt_view[i1].setText(text);

										}

										if (inst[i1].contains("image")) {

											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1]
													.setImageDrawable(this
															.getResources()
															.getDrawable(
																	R.drawable.rsz_broken));
											iv_album[i1]
													.setContentDescription(instructions);
											iv_album[i1].setId(i1 + 1);
											final ImageView imgview = iv_album[i1];
											vfileCheck = new File(
													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											if (vfileCheck.exists())
												iv_album[i1]
														.setImageBitmap(callDisp
																.ResizeImage(locaLpath
																		+ imgview
																				.getContentDescription()
																				.toString()));
											layoutt.addView(iv_album[i1],
													params);

											ArrayList<Object> list = new ArrayList<Object>();
											list.add(inst[i1]);
											list.add(iv_album[i1]);
											decode_task = new base64Decoder();
											decode_task.execute(list);

											imgview.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													vfileCheck = new File(
															locaLpath
																	+ imgview
																			.getContentDescription()
																			.toString());
													if (vfileCheck.exists()) {

														Intent in = new Intent(
																context,
																PhotoZoomActivity.class);
														in.putExtra(
																"Photo_path",

																locaLpath
																		+ imgview
																				.getContentDescription()
																				.toString());
														in.putExtra("type",
																false);
														in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
														startActivityForResult(
																in, 10);
													} else {

													}

												}
											});

										}

										if (inst[i1].contains("audio")) {
											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1]
													.setContentDescription(instructions);

											iv_album[i1].setImageDrawable(this
													.getResources()
													.getDrawable(
															R.drawable.v_play));

											iv_album[i1].setId(i1 + 1);

											final ImageView img = iv_album[i1];

											layoutt.addView(iv_album[i1],
													params);

											chatplayer = new MediaPlayer();
											img.setTag("0");
											Log.i("FORMS",
													"===>"
															+ img.getContentDescription()
																	.toString());
											img.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													vfileCheck = new File(
															locaLpath
																	+ img.getContentDescription()
																			.toString());
													Log.i("FORMS",
															"===>"
																	+ locaLpath
																	+ img.getContentDescription()
																			.toString());

													if (img.getTag().toString()
															.equals("1")) {
														img.setTag("0");
														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_play);
														img.setImageBitmap(bitmap);
														isaudioPlaying = false;

														chatplayer.stop();
														chatplayer.reset();
													} else {
														if (!isaudioPlaying) {

															if (vfileCheck
																	.exists()) {
																Log.i("FORMS",
																		"===>EXIST");

																try {
																	chatplayer
																			.setDataSource(locaLpath
																					+ img.getContentDescription()
																							.toString());
																	chatplayer
																			.setLooping(false);

																	chatplayer
																			.prepare();
																	chatplayer
																			.start();
																	bitmap = BitmapFactory
																			.decodeResource(
																					getResources(),
																					R.drawable.v_stop);
																	img.setImageBitmap(bitmap);
																	img.setTag("1");
																	isaudioPlaying = true;

																} catch (IllegalArgumentException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																} catch (IllegalStateException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																} catch (IOException e) {
																	if (AppReference.isWriteInFile)
																		AppReference.logger
																				.error(e.getMessage(),
																						e);
																	e.printStackTrace();
																}

																chatplayer
																		.setOnCompletionListener(new OnCompletionListener() {

																			@Override
																			public void onCompletion(
																					MediaPlayer mp) {

																				try {
																					chatplayer
																							.reset();
																					isaudioPlaying = false;

																					bitmap = BitmapFactory
																							.decodeResource(
																									getResources(),
																									R.drawable.v_play);
																					img.setImageBitmap(bitmap);

																					img.setTag("0");

																				} catch (IllegalArgumentException e) {
																					if (AppReference.isWriteInFile)
																						AppReference.logger
																								.error(e.getMessage(),
																										e);
																					e.printStackTrace();
																				} catch (IllegalStateException e) {
																					if (AppReference.isWriteInFile)
																						AppReference.logger
																								.error(e.getMessage(),
																										e);
																					e.printStackTrace();
																				}

																			}
																		});

															} else {
																Toast.makeText(
																		context,
																		"File Downloading",
																		1)
																		.show();
																downloadConfiguredNote(instructions);

															}
														} else {

															showToast(SingleInstance.mainContext
																	.getResources()
																	.getString(
																			R.string.please_stop_audio));
														}

													}

												}
											});

										}

										if (inst[i1].contains("video")) {

											RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
													RelativeLayout.LayoutParams.WRAP_CONTENT,
													RelativeLayout.LayoutParams.WRAP_CONTENT);
											params.addRule(
													RelativeLayout.ALIGN_PARENT_TOP,
													RelativeLayout.ABOVE);
											iv_album[i1] = new ImageView(
													context);
											iv_album[i1].setPadding(30, 10, 30,
													0);
											iv_album[i1].setImageDrawable(this
													.getResources()
													.getDrawable(
															R.drawable.v_play));
											iv_album[i1]
													.setContentDescription(instructions);

											iv_album[i1].setId(i1 + 1);

											final ImageView img = iv_album[i1];

											layoutt.addView(iv_album[i1],
													params);

											iv_album[i1]
													.setOnClickListener(new OnClickListener() {

														@Override
														public void onClick(
																View v) {
															vfileCheck = new File(
																	locaLpath
																			+ img.getContentDescription()
																					.toString());

															if (vfileCheck
																	.exists()) {
																if (!isaudioPlaying) {

																	Intent intentVPlayer = new Intent(
																			context,
																			VideoPlayer.class);
																	intentVPlayer
																			.putExtra(
																					"File_Path",

																					locaLpath
																							+ img.getContentDescription());
																	intentVPlayer
																			.putExtra(
																					"Player_Type",
																					"Video Player");
																	startActivity(intentVPlayer);
																}

																else {
																	showToast("Kindly stop audio playing");

																}
															} else {

																Toast.makeText(
																		context,
																		"File Downloading",
																		1)
																		.show();
																downloadConfiguredNote(instructions);

															}

														}
													});

										}

									}

									tv.setText(sb.toString());

								}

								final TextView hint = (TextView) layout
										.findViewById(R.id.tview_clm_hint);

								final ImageView im = (ImageView) layout
										.findViewById(R.id.iview_dtype);
								im.setContentDescription("");
								im.setVisibility(View.GONE);

								EditText ed_fld = (EditText) layout
										.findViewById(R.id.edtxt_frmfield);
								// if (permission != null
								// && permission.equalsIgnoreCase("F2")) {
								// ed_fld.setEnabled(false);
								// ed_fld.setFocusable(false);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F2")) {
									ed_fld.setEnabled(false);
									ed_fld.setFocusable(false);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F2")) {
										ed_fld.setEnabled(false);
										ed_fld.setFocusable(false);
									} else {
										ed_fld.setEnabled(true);
										ed_fld.setFocusable(true);
									}
								}
								ed_fld.setText(defaultvalue);
								if (defaultvalue.length() > 0) {
									ed_fld.setContentDescription(defaultvalue);

								} else {
									ed_fld.setContentDescription("");

								}

								ed_fld.setHint("Enter Text ");
								ed_fld.addTextChangedListener(new TextWatcher() {

									@Override
									public void onTextChanged(CharSequence s,
											int start, int before, int count) {
										if (s.length() == 0) {
											hint.setVisibility(View.GONE);
										}
									}

									@Override
									public void beforeTextChanged(
											CharSequence s, int start,
											int count, int after) {

									}

									@Override
									public void afterTextChanged(Editable s) {

									}
								});
								// if (permission != null
								// && permission.equalsIgnoreCase("F1")) {
								// im.setEnabled(false);
								// }
								if (defaultPermission != null
										&& defaultPermission
												.equalsIgnoreCase("F2")) {
									im.setEnabled(false);
								}
								if (permission != null) {
									if (permission.equalsIgnoreCase("F2")) {
										im.setEnabled(false);
									} else {
										im.setEnabled(true);
									}
								}
								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										addView(v, im.getContentDescription()
												.toString());
									}
								});

								if (dtypes.get(recs).trim()
										.equalsIgnoreCase("INTEGER")) {
									ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);
								} else if (validdata.equalsIgnoreCase("email")) {
									ed_fld.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
								} else if (validdata
										.equalsIgnoreCase("NO SPECIAL CHAR")) {
									ed_fld.setFilters(getNoSpclchar());
								} else if (dtypes.get(recs).trim()
										.equalsIgnoreCase("nvarchar(20)")) {
									ed_fld.setInputType(InputType.TYPE_CLASS_TEXT);
								}

								layout.setId(records_container.getChildCount());
								layout.setContentDescription(entrymode);

								records_container.addView(layout);
							}
						}
					} else {
						info_lists = callDisp.getdbHeler(context)
								.getRecordsofforminfotable(
										title + "_" + table_id, recs, dtype);

						if (info_lists != null) {
							instruction = info_lists[6] + ",";
							String attId = info_lists[8];
							String permission = null;
							String defaultPermission = null;
							if (attId != null) {
								permission = DBAccess.getdbHeler()
										.getFormFieldBuddyAccessSettings(
												table_id, attId);
								defaultPermission = DBAccess.getdbHeler()
										.getFormFieldBuddyAccessSettings(
												table_id, attId);
							}
							LinearLayout layout = (LinearLayout) layoutInflater
									.inflate(R.layout.blob_row, null);
							LayoutParams paramss = new LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT);
							paramss.setMargins(0, 10, 0, 0);
							TextView tv = (TextView) layout
									.findViewById(R.id.tview_clmblob);

							tv.setText(recs.replace("blob_", ""));
							View view = (View) layout
									.findViewById(R.id.view_inst);

							ImageView option_selector = (ImageView) layout
									.findViewById(R.id.iview_dtype);
							LinearLayout blob_layout = (LinearLayout) layout
									.findViewById(R.id.blob_layout);
							blob_layout.setVisibility(View.GONE);
							LinearLayout layoutt = (LinearLayout) layout
									.findViewById(R.id.tview_lay);
							layoutt.findViewById(R.id.tview_clms);
							StringBuffer sb = new StringBuffer();

							final String[] inst = instruction.split(",");
							final ImageView[] iv_album = new ImageView[inst.length];
							final TextView[] txt_view = new TextView[inst.length];

							if (inst.length > 0) {
								view.setVisibility(View.VISIBLE);
							} else {
								view.setVisibility(View.GONE);

							}
							for (i1 = 0; i1 < inst.length; i1++) {
								instructions = inst[i1];
								if (inst[i1].contains("note")) {

									RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
											RelativeLayout.LayoutParams.WRAP_CONTENT,
											RelativeLayout.LayoutParams.WRAP_CONTENT);
									params.addRule(
											RelativeLayout.ALIGN_PARENT_TOP,
											RelativeLayout.ABOVE);
									txt_view[i1] = new TextView(context);
									txt_view[i1].setPadding(30, 10, 30, 0);

									layoutt.addView(txt_view[i1], params);

									try {
										File myFile = new File(
												"/sdcard/COMMedia/"
														+ instructions);
										FileInputStream fIn = new FileInputStream(
												myFile);
										BufferedReader myReader = new BufferedReader(
												new InputStreamReader(fIn));
										String aDataRow = "";
										String aBuffer = "";
										while ((aDataRow = myReader.readLine()) != null) {
											aBuffer += aDataRow + "\n";
										}
										text = aBuffer;
										if (inst.length == 1) {
											sb.append(text);
										} else {
											sb.append(text + "\n");
										}
										myReader.close();

									} catch (Exception e) {
										if (AppReference.isWriteInFile)
											AppReference.logger.error(
													e.getMessage(), e);
									}

									txt_view[i1].setText(text);

								}

								else if (inst[i1].contains("image")) {

									RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
											RelativeLayout.LayoutParams.WRAP_CONTENT,
											RelativeLayout.LayoutParams.WRAP_CONTENT);
									params.addRule(
											RelativeLayout.ALIGN_PARENT_TOP,
											RelativeLayout.ABOVE);
									iv_album[i1] = new ImageView(context);
									iv_album[i1].setPadding(30, 10, 30, 0);
									iv_album[i1].setImageDrawable(this
											.getResources().getDrawable(
													R.drawable.rsz_broken));
									iv_album[i1]
											.setContentDescription(instructions);
									iv_album[i1].setId(i1 + 1);
									final ImageView imgview = iv_album[i1];
									vfileCheck = new File(locaLpath
											+ imgview.getContentDescription()
													.toString());
									if (vfileCheck.exists())
										iv_album[i1]
												.setImageBitmap(callDisp
														.ResizeImage(locaLpath
																+ imgview
																		.getContentDescription()
																		.toString()));
									layoutt.addView(iv_album[i1], params);

									ArrayList<Object> list = new ArrayList<Object>();
									list.add(inst[i1]);
									list.add(iv_album[i1]);
									decode_task = new base64Decoder();
									decode_task.execute(list);

									imgview.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											vfileCheck = new File(
													locaLpath
															+ imgview
																	.getContentDescription()
																	.toString());
											if (vfileCheck.exists()) {

												Intent in = new Intent(context,
														PhotoZoomActivity.class);
												in.putExtra(
														"Photo_path",

														locaLpath
																+ imgview
																		.getContentDescription()
																		.toString());
												in.putExtra("type", false);
												in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												startActivityForResult(in, 10);
											} else {

											}

										}
									});

								}

								else if (inst[i1].contains("audio")) {
									RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
											RelativeLayout.LayoutParams.WRAP_CONTENT,
											RelativeLayout.LayoutParams.WRAP_CONTENT);
									params.addRule(
											RelativeLayout.ALIGN_PARENT_TOP,
											RelativeLayout.ABOVE);
									iv_album[i1] = new ImageView(context);
									iv_album[i1].setPadding(30, 10, 30, 0);
									iv_album[i1].setImageDrawable(this
											.getResources().getDrawable(
													R.drawable.v_play));
									iv_album[i1]
											.setContentDescription(instructions);
									iv_album[i1].setId(i1 + 1);

									final ImageView img = iv_album[i1];

									layoutt.addView(iv_album[i1], params);

									// vfileCheck = new File(img
									// .getContentDescription().toString());
									vfileCheck = new File(locaLpath
											+ instructions);
									chatplayer = new MediaPlayer();

									Log.i("FORMS", "Audio File===>"
											+ img.getContentDescription()
													.toString());
									img.setTag("0");
									img.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {

											if (img.getTag().toString()
													.equals("1")) {
												img.setTag("0");
												bitmap = BitmapFactory
														.decodeResource(
																getResources(),
																R.drawable.v_play);
												img.setImageBitmap(bitmap);
												isaudioPlaying = false;

												chatplayer.stop();
												chatplayer.reset();
											} else {
												if (!isaudioPlaying) {

													if (vfileCheck.exists()) {

														try {
															chatplayer
																	.setDataSource(locaLpath
																			+ img.getContentDescription()
																					.toString());
															chatplayer
																	.setLooping(false);

															chatplayer
																	.prepare();
															chatplayer.start();
															bitmap = BitmapFactory
																	.decodeResource(
																			getResources(),
																			R.drawable.v_stop);
															img.setImageBitmap(bitmap);
															img.setTag("1");
															isaudioPlaying = true;

														} catch (IllegalArgumentException e) {
															if (AppReference.isWriteInFile)
																AppReference.logger
																		.error(e.getMessage(),
																				e);
															e.printStackTrace();
														} catch (IllegalStateException e) {
															if (AppReference.isWriteInFile)
																AppReference.logger
																		.error(e.getMessage(),
																				e);
															e.printStackTrace();
														} catch (IOException e) {
															if (AppReference.isWriteInFile)
																AppReference.logger
																		.error(e.getMessage(),
																				e);
															e.printStackTrace();
														}

														chatplayer
																.setOnCompletionListener(new OnCompletionListener() {

																	@Override
																	public void onCompletion(
																			MediaPlayer mp) {

																		try {
																			chatplayer
																					.reset();
																			isaudioPlaying = false;

																			bitmap = BitmapFactory
																					.decodeResource(
																							getResources(),
																							R.drawable.v_play);
																			img.setImageBitmap(bitmap);

																			img.setTag("0");

																		} catch (IllegalArgumentException e) {
																			if (AppReference.isWriteInFile)
																				AppReference.logger
																						.error(e.getMessage(),
																								e);
																			e.printStackTrace();
																		} catch (IllegalStateException e) {
																			if (AppReference.isWriteInFile)
																				AppReference.logger
																						.error(e.getMessage(),
																								e);
																			e.printStackTrace();
																		}

																	}
																});

													} else {
														Toast.makeText(
																context,
																"File Downloading",
																1).show();
														downloadConfiguredNote(instructions);

													}
												} else {

													showToast(SingleInstance.mainContext
															.getResources()
															.getString(
																	R.string.please_stop_audio));
												}

											}

										}
									});
								}

								else if (inst[i1].contains("video")) {

									RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
											RelativeLayout.LayoutParams.WRAP_CONTENT,
											RelativeLayout.LayoutParams.WRAP_CONTENT);
									params.addRule(
											RelativeLayout.ALIGN_PARENT_TOP,
											RelativeLayout.ABOVE);
									iv_album[i1] = new ImageView(context);
									iv_album[i1].setPadding(30, 10, 30, 0);
									iv_album[i1].setImageDrawable(this
											.getResources().getDrawable(
													R.drawable.v_play));

									iv_album[i1].setId(i1 + 1);

									layoutt.addView(iv_album[i1], params);

									iv_album[i1]
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													Log.i("forms_test",
															"localpath :"
																	+ locaLpath);
													// Log.i("forms_test",
													// "  iv_album[i1] :"+iv_album[i1].getContentDescription().toString());
													vfileCheck = new File(
															locaLpath
																	+ instructions);
													// + iv_album[i1]
													// .getContentDescription()
													// .toString());

													if (vfileCheck.exists()) {
														if (!isaudioPlaying) {

															Intent intentVPlayer = new Intent(
																	context,
																	VideoPlayer.class);
															intentVPlayer
																	.putExtra(
																			"File_Path",

																			locaLpath
																					+ instructions);
															// + iv_album[i1]
															// .getContentDescription()
															// .toString());
															intentVPlayer
																	.putExtra(
																			"Player_Type",
																			"Video Player");
															startActivity(intentVPlayer);

														} else {

															showToast(SingleInstance.mainContext
																	.getResources()
																	.getString(
																			R.string.please_stop_audio));
														}

													} else {

														Toast.makeText(
																context,
																"File Downloading",
																1).show();
														downloadConfiguredNote(instructions);

													}

												}
											});

								}

							}

							option_selector.setContentDescription("multimedia");
							// if (permission != null
							// && permission.equalsIgnoreCase("F2")) {
							// option_selector.setEnabled(false);
							// }
							// if (permission != null
							// && permission.equalsIgnoreCase("F1")) {
							// layout.setVisibility(View.GONE);
							// }
							if (defaultPermission != null
									&& defaultPermission.equalsIgnoreCase("F1")) {
								layout.setVisibility(View.GONE);
							}
							if (permission != null) {
								if (permission.equalsIgnoreCase("F1")) {
									layout.setVisibility(View.GONE);
								} else if (permission.equalsIgnoreCase("F2")) {
									layout.setVisibility(View.VISIBLE);
									option_selector.setEnabled(false);
								} else {
									layout.setVisibility(View.VISIBLE);
									option_selector.setEnabled(true);
								}
							}

							option_selector
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											RelativeLayout r_parent = (RelativeLayout) v
													.getParent();
											LinearLayout l_parent = (LinearLayout) r_parent
													.getParent();
											selected_rowid = l_parent.getId();
											showBlobSelector();
										}
									});
							layout.setId(records_container.getChildCount());
							layout.setLayoutParams(paramss);
							layout.setContentDescription(entrymode);

							records_container.addView(layout);
						}
					}
				} catch (Exception e) {
					if (AppReference.isWriteInFile)
						AppReference.logger.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean validateEmail(String email) {

		if (email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
				&& email.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean nospecialchar(String nospec) {

		Pattern pt = Pattern.compile("[^a-zA-Z0-9\\s]]");
		Matcher match = pt.matcher(nospec);
		boolean isExists = true;
		while (match.find()) {
			isExists = false;
			break;
		}
		return isExists;

	}

	private boolean beginalpha(String val) {

		if (val.length() > 0) {
			char c = val.charAt(0);
			if (!Character.isLetter(c)) {

				return false;
			}
		}
		return true;
	}

	void recordinsert() {

		NewValidateForm(false);

		if (field_values.size() == field_list.size()) {

			try {
				showprogress();
				if (CallDispatcher.LoginUser != null) {
					field_list.add("uuid");
					field_values.add(CallDispatcher.LoginUser);
					field_list.add("euuid");
					field_values.add(CallDispatcher.LoginUser);
					field_list.add("uudate");
					field_values.add(getNoteCreateTime());
					if (WebServiceReferences.running) {
						WebServiceReferences.webServiceClient
								.addFormRecords(
										CallDispatcher.LoginUser,
										table_id,
										field_list
												.toArray(new String[field_list
														.size()]),
										field_values
												.toArray(new String[field_values
														.size()]), context);
					} else {
						callDisp.startWebService(
								getResources().getString(R.string.service_url),
								"80");
						WebServiceReferences.webServiceClient
								.addFormRecords(
										CallDispatcher.LoginUser,
										table_id,
										field_list
												.toArray(new String[field_list
														.size()]),
										field_values
												.toArray(new String[field_values
														.size()]), context);
					}
				} else {
					p = PreferenceManager.getDefaultSharedPreferences(context);

					final String username = p.getString("uname", null);
					field_list.add("uuid");
					field_values.add(username);
					field_list.add("euuid");
					field_values.add(username);
					field_list.add("uudate");
					field_values.add(getNoteCreateTime());
					String id = table_id;
					String formtime = getNoteCreateTime();
					String status = "0";

					ContentValues values = new ContentValues();
					values.put("tableid", id);

					for (int i = 0; i < field_list.size(); i++) {

						if (dtypes.get(field_list.get(i)).equalsIgnoreCase(
								"BLOB")) {
							values.put(field_list.get(i),
									convertbmptoByteArray(bmp_container
											.get(Integer.toString(i))));
						} else if (dtypes.get(field_list.get(i))
								.equalsIgnoreCase("INTEGER")) {
							values.put(field_list.get(i),
									Integer.parseInt(field_values.get(i)));
						} else {
							values.put(field_list.get(i), field_values.get(i));

						}
					}
					values.put("recorddate", formtime);
					values.put("status", status);

					if (callDisp.getdbHeler(context).insertForRecords(
							title + "_" + table_id, values)) {

						cancelDialog(true);
						String strQuery = "update formslookup set status='"
								+ "0" + "' where tableid=" + table_id;
						callDisp.getdbHeler(context).ExecuteQuery(strQuery);
						{
							finish();

						}

					}

				}
			} catch (NumberFormatException e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
			} catch (NotFoundException e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
			}
		}
		hastocreate_another = false;

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == btn_cancl.getId()) {
			// callDisp.SIPCallisAccepted = false;
			if (!isaudioPlaying) {

				if (issipcall) {
					isreject = true;
					// for (com.cg.hostedconf.CallerBean callerbean :
					// AppReference.process_members) {
					// if (callerbean.getCall_id() != -1) {
					// CommunicationBean cBean1 = new CommunicationBean();
					// cBean1.setOperationType(sip_operation_types.HANGUP_SINGLE_CALL);
					// cBean1.setCall_id(callerbean.getCall_id());
					// AppReference.sipQueue.addMsg(cBean1);
					// }
					// }

					// callerBean_Array.clear();

					finish();
				} else {
					finish();

				}
			} else {

				showToast("Kindly stop audio");
			}
		}

		else if (v.getId() == btn_rejcall.getId()) {
			if (btn_rejcall.getTag().equals("0")) {
				isreject = true;
				// callDisp.SIPCallisAccepted = false;
				btn_rejcall.setBackgroundResource(R.drawable.reconnect);
				btn_rejcall.setTag("1");

				// for (CallerBean callerbean : AppReference.process_members) {
				// if (callerbean.getCall_id() != -1) {
				// CommunicationBean cBean1 = new CommunicationBean();
				// cBean1.setOperationType(sip_operation_types.HANGUP_SINGLE_CALL);
				// cBean1.setCall_id(callerbean.getCall_id());
				// AppReference.sipQueue.addMsg(cBean1);
				// }
				// }
			} else if (btn_rejcall.getTag().equals("1")) {
				// callDisp.SIPCallisAccepted = true;
				btn_rejcall.setBackgroundResource(R.drawable.hangupx);
				btn_rejcall.setTag("0");
				// CommunicationBean bean1 = new CommunicationBean();
				// bean1.setOperationType(sip_operation_types.ENABLE_SRTP);
				// bean1.setAcc_id(AppReference.sip_accountID);
				// AppReference.sipQueue.addMsg(bean1);
				// for (CallerBean calBean : AppReference.process_members) {
				//
				// calBean.setCall_id(-1);
				// calBean.setPresense("Connecting");
				// calBean.setHold(0);
				// calBean.setMute(0);
				//
				// CommunicationBean bean = new CommunicationBean();
				// bean.setOperationType(sip_operation_types.MAKECALL_WITHMODE);
				// bean.setAcc_id(AppReference.sip_accountID);
				// bean.setRealm(callDisp.getFS());
				// bean.setSipEndpoint(AppReference.sip_registeredid);
				// bean.setMode("twoway");
				// bean.setTonumber(calBean.getToNnumber());
				// AppReference.sipQueue.addMsg(bean);
				//
				// }
			}

		}

		else if (v.getId() == btn_save.getId()) {
			try {
				if (isUpload) {

					if (!CallDispatcher.isWifiClosed) {
						if (!isaudioPlaying) {

							getFormId = callDisp.getdbHeler(context)
									.getFormName(table_id);
							if (getFormId != null || getFormId != "") {
								if (btn_save.getText().equals("Save")) {
									if (isaudioPlaying) {
										if (chatplayer.isPlaying()) {
											chatplayer.stop();
										}
									}

									NewValidateForm(false);

									if (field_values.size() == field_list
											.size()) {
										showprogress();
										if (CallDispatcher.LoginUser != null) {

											field_list.add("uuid");
											field_values
													.add(CallDispatcher.LoginUser);
											field_list.add("euuid");
											field_values
													.add(CallDispatcher.LoginUser);
											field_list.add("uudate");
											field_values
													.add(getNoteCreateTime());

											String strQuery = "update formslookup set status='"
													+ "0"
													+ "' where tableid="
													+ table_id;
											if (callDisp.getdbHeler(context)
													.ExecuteQuery(strQuery)) {

												if (WebServiceReferences.running) {
													WebServiceReferences.webServiceClient
															.addFormRecords(
																	CallDispatcher.LoginUser,
																	table_id,
																	field_list
																			.toArray(new String[field_list
																					.size()]),
																	field_values
																			.toArray(new String[field_values
																					.size()]),
																	context);
												} else {
													callDisp.startWebService(
															getResources()
																	.getString(
																			R.string.service_url),
															"80");
													WebServiceReferences.webServiceClient
															.addFormRecords(
																	CallDispatcher.LoginUser,
																	table_id,
																	field_list
																			.toArray(new String[field_list
																					.size()]),
																	field_values
																			.toArray(new String[field_values
																					.size()]),
																	context);
												}
											}
										} else {
											p = PreferenceManager
													.getDefaultSharedPreferences(context);

											final String username = p
													.getString("uname", null);
											field_list.add("uuid");
											field_values.add(username);
											field_list.add("euuid");
											field_values.add(username);
											field_list.add("uudate");
											field_values
													.add(getNoteCreateTime());
											String id = table_id;
											String formtime = getNoteCreateTime();
											String status = "0";

											ContentValues values = new ContentValues();
											values.put("tableid", id);

											for (int i = 0; i < field_list
													.size(); i++) {

												if (dtypes.get(
														field_list.get(i))
														.equalsIgnoreCase(
																"BLOB")) {

													values.put(
															field_list.get(i),
															convertbmptoByteArray(bmp_container
																	.get(Integer
																			.toString(i))));
												} else if (dtypes.get(
														field_list.get(i))
														.equalsIgnoreCase(
																"INTEGER")) {
													values.put(
															field_list.get(i),
															Integer.parseInt(field_values
																	.get(i)));
												} else {
													values.put(
															field_list.get(i),
															field_values.get(i));

												}
											}
											values.put("recorddate", formtime);
											values.put("status", status);

											if (callDisp.getdbHeler(context)
													.insertForRecords(
															title + "_"
																	+ table_id,
															values)) {

												cancelDialog(true);
												String strQuery = "update formslookup set status='"
														+ "0"
														+ "' where tableid="
														+ table_id;
												callDisp.getdbHeler(context)
														.ExecuteQuery(strQuery);
												{
													if (WebServiceReferences.contextTable
															.containsKey("frmviewer")) {
														FormViewer frm_creator = (FormViewer) WebServiceReferences.contextTable
																.get("frmviewer");
														if (frm_creator
																.isShowingCurrentForm(table_id)) {
															frm_creator
																	.refreshList();
														}
													}
													finish();

												}

											}

										}
									}
									hastocreate_another = false;
								}

								else {

									NewValidateForm(true);

									if (field_values.size() == field_list
											.size()) {

										showprogress();

										if (CallDispatcher.LoginUser != null) {
											field_list.add("uuid");
											field_values
													.add(CallDispatcher.LoginUser);
											field_list.add("euuid");
											field_values
													.add(CallDispatcher.LoginUser);
											field_list.add("uudate");
											field_values
													.add(getNoteCreateTime());
											String strQuery = "update formslookup set status='"
													+ "0"
													+ "' where tableid="
													+ table_id;
											if (callDisp.getdbHeler(context)
													.ExecuteQuery(strQuery)) {
												if (WebServiceReferences.running) {
													WebServiceReferences.webServiceClient
															.updateFormRecords(
																	updateformid,
																	CallDispatcher.LoginUser,
																	updaterecordid,
																	field_list
																			.toArray(new String[field_list
																					.size()]),
																	field_values
																			.toArray(new String[field_values
																					.size()]),
																	context);
												} else {
													callDisp.startWebService(
															getResources()
																	.getString(
																			R.string.service_url),
															"80");
													WebServiceReferences.webServiceClient
															.updateFormRecords(
																	updateformid,
																	CallDispatcher.LoginUser,
																	updaterecordid,
																	field_list
																			.toArray(new String[field_list
																					.size()]),
																	field_values
																			.toArray(new String[field_values
																					.size()]),
																	context);
												}
											}
										} else {

											p = PreferenceManager
													.getDefaultSharedPreferences(context);

											final String username = p
													.getString("uname", null);
											field_list.add("uuid");
											field_values.add(username);
											field_list.add("euuid");
											field_values.add(username);
											field_list.add("uudate");
											field_values
													.add(getNoteCreateTime());

											ContentValues values = new ContentValues();

											for (int i = 0; i < field_list
													.size(); i++) {

												if (dtypes.get(
														field_list.get(i))
														.equalsIgnoreCase(
																"BLOB")) {

													values.put(
															field_list.get(i),
															convertbmptoByteArray(bmp_container
																	.get(Integer
																			.toString(i))));
												} else if (dtypes.get(
														field_list.get(i))
														.equalsIgnoreCase(
																"INTEGER")) {
													values.put(
															field_list.get(i),
															Integer.parseInt(field_values
																	.get(i)));
												} else {
													values.put(
															field_list.get(i),
															field_values.get(i));
												}
											}
											values.put("status", "2");

											if (callDisp.getdbHeler(context)
													.update("[" + title + "_"
															+ updateformid
															+ "]", values,
															updaterecordid)) {

												cancelDialog(true);
												String strQuery = "update formslookup set status='"
														+ "0"
														+ "' where tableid="
														+ table_id;
												callDisp.getdbHeler(context)
														.ExecuteQuery(strQuery);
												{
													finish();

												}

											}

										}

										hastocreate_another = false;
									}

								}

							} else {

								Toast.makeText(
										context,
										SingleInstance.mainContext
												.getResources()
												.getString(
														R.string.buddy_delete_this_form),
										Toast.LENGTH_LONG).show();
								finish();
							}
						} else {
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.please_stop_audio));

						}
					} else {
						ShowError(
								SingleInstance.mainContext.getResources()
										.getString(R.string.network_err),
								SingleInstance.mainContext
										.getResources()
										.getString(R.string.network_unreachable));

					}
				} else {
					Toast.makeText(
							context,
							SingleInstance.mainContext
									.getResources()
									.getString(
											R.string.upload_failed_another_file),
							Toast.LENGTH_LONG).show();
				}
			} catch (NumberFormatException e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				e.printStackTrace();
			} catch (IllegalStateException e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		} else if (v.getId() == btn_svcreate.getId()) {
			if (isUpload) {
				if (!CallDispatcher.isWifiClosed) {
					if (!isaudioPlaying) {

						getFormId = callDisp.getdbHeler(context).getFormName(
								table_id);
						if (getFormId != null || getFormId != "") {
							NewValidateForm(false);

							if (field_values.size() == field_list.size()) {

								showprogress();

								field_list.add("uuid");
								field_values.add(CallDispatcher.LoginUser);
								field_list.add("euuid");
								field_values.add(CallDispatcher.LoginUser);
								field_list.add("uudate");
								field_values.add(getNoteCreateTime());
								if (WebServiceReferences.running) {
									WebServiceReferences.webServiceClient
											.addFormRecords(
													CallDispatcher.LoginUser,
													table_id,
													field_list
															.toArray(new String[field_list
																	.size()]),
													field_values
															.toArray(new String[field_values
																	.size()]),
													context);
								} else {
									callDisp.startWebService(getResources()
											.getString(R.string.service_url),
											"80");
									WebServiceReferences.webServiceClient
											.addFormRecords(
													CallDispatcher.LoginUser,
													table_id,
													field_list
															.toArray(new String[field_list
																	.size()]),
													field_values
															.toArray(new String[field_values
																	.size()]),
													context);
								}

								hastocreate_another = true;
							}
							InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							inputManager.hideSoftInputFromWindow(
									getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);

						} else {
							InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							inputManager.hideSoftInputFromWindow(
									getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
							Toast.makeText(
									context,
									SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.buddy_delete_this_form),
									Toast.LENGTH_LONG).show();
							finish();
						}
					} else {

						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.please_stop_audio));
					}

				} else {

					ShowError(SingleInstance.mainContext.getResources()
							.getString(R.string.network_err),
							SingleInstance.mainContext.getResources()
									.getString(R.string.network_unreachable));
				}
			} else {
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.upload_failed_another_file),
						Toast.LENGTH_LONG).show();
			}
		} else if (v.getId() == btn_quickaction.getId()) {
			if (isUpload) {
				if (!CallDispatcher.isWifiClosed) {
					if (!isaudioPlaying) {

						getFormId = callDisp.getdbHeler(context).getFormName(
								table_id);
						if (getFormId != null || getFormId != "") {
							Intent i = new Intent(context,
									Specialquickaction.class);
							startActivityForResult(i, 1);
						} else {
							Toast.makeText(
									context,
									SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.buddy_delete_this_form),
									Toast.LENGTH_LONG).show();
							finish();
						}
					} else {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.please_stop_audio));
					}

				} else {

					ShowError(SingleInstance.mainContext.getResources()
							.getString(R.string.network_err),
							SingleInstance.mainContext.getResources()
									.getString(R.string.network_unreachable));
				}
			} else {
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.upload_failed_another_file),
						Toast.LENGTH_LONG).show();
			}
		} else if (v.getId() == R.id.edtxt_frmfield) {

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

	private void NewValidateForm(boolean isupdate) {

		field_values.clear();
		for (int i = 0; i < records_container.getChildCount(); i++) {
			try {
				field_list.get(i).contains("blob_");
				if (field_list.get(i).contains("blob_")) {

					LinearLayout layout = (LinearLayout) records_container
							.getChildAt(i);
					LinearLayout blob_layout = (LinearLayout) layout
							.findViewById(R.id.blob_layout);
					ImageView blob_image = (ImageView) blob_layout
							.findViewById(R.id.iview_blobimage);
					if (isupdate) {
						if (blob_image.getContentDescription().toString()
								.length() > 0) {
							field_values.add(blob_image.getContentDescription()
									.toString());

						} else {

							field_values.add("");
						}
					} else {
						if (bmp_container.containsKey(Integer.toString(layout
								.getId()))) {

							field_values.add(blob_image.getContentDescription()
									.toString());
						} else {
							field_values.add("");

						}
					}

				} else {

					LinearLayout layout = (LinearLayout) records_container
							.getChildAt(i);
					final EditText fields = (EditText) layout
							.findViewById(R.id.edtxt_frmfield);
					HashMap<String, String> dtype = new HashMap<String, String>();

					dtype = callDisp.getdbHeler(context)
							.getColumnTypesforminfo("");
					hint = (TextView) layout.findViewById(R.id.tview_clm_hint);
					String colName = "";

					colName = field_list.get(i);

					if (table_id == null) {
						validate_lists = callDisp.getdbHeler(context)
								.getRecordsofforminfotable(
										title + "_" + updateformid, colName,
										dtype);

					} else {

						validate_lists = callDisp.getdbHeler(context)
								.getRecordsofforminfotable(
										title + "_" + table_id, colName, dtype);

					}

					entrymode = validate_lists[3];
					validdata = validate_lists[4];
					defaultvalue = validate_lists[5];
					instruction = validate_lists[6];
					errortip = validate_lists[7];
					if (fields.getText().toString().trim() == "") {
						Toast.makeText(
								getApplicationContext(),
								SingleInstance.mainContext.getResources()
										.getString(R.string.valid_text),
								Toast.LENGTH_LONG).show();
						btn_save.setEnabled(false);
						btn_svcreate.setEnabled(false);
						btn_quickaction.setEnabled(false);
					}
					if (entrymode.equalsIgnoreCase("drop down")) {

						if (fields.getText().toString().trim().length() == 0) {
							hint.setVisibility(View.GONE);
							field_values.add("");

						} else {
							hint.setVisibility(View.GONE);
							field_values
									.add(fields.getText().toString().trim());

						}

					} else if (entrymode.equalsIgnoreCase("radio button")) {

						if (fields.getText().toString().trim().length() == 0) {
							hint.setVisibility(View.GONE);
							field_values.add("");

						} else {
							hint.setVisibility(View.GONE);
							field_values
									.add(fields.getText().toString().trim());

						}
					} else if (entrymode.equalsIgnoreCase("current date")) {

						if (fields.getText().toString().trim().length() == 0) {
							hint.setVisibility(View.GONE);
							field_values.add("");

						} else {
							hint.setVisibility(View.GONE);
							field_values
									.add(fields.getText().toString().trim());

						}
					} else if (entrymode.equalsIgnoreCase("current time")) {

						if (fields.getText().toString().trim().length() == 0) {
							hint.setVisibility(View.GONE);
							field_values.add("");

						} else {
							hint.setVisibility(View.GONE);
							field_values
									.add(fields.getText().toString().trim());

						}
					} else if (entrymode.equalsIgnoreCase("current location")) {
						if (fields.getText().toString().trim().length() == 0) {
							hint.setVisibility(View.GONE);
							field_values.add("");

						} else {
							hint.setVisibility(View.GONE);
							field_values
									.add(fields.getText().toString().trim());

						}
					} else if (entrymode.equalsIgnoreCase("time")) {

						if (fields.getText().toString().trim().length() == 0) {
							hint.setVisibility(View.GONE);
							field_values.add("");

						} else {
							hint.setVisibility(View.GONE);
							field_values
									.add(fields.getText().toString().trim());

						}

					}

					else if (entrymode.equalsIgnoreCase("date")) {

						if (fields.getText().toString().trim().length() == 0) {
							hint.setVisibility(View.GONE);
							field_values.add("");

						} else {
							hint.setVisibility(View.GONE);
							field_values
									.add(fields.getText().toString().trim());

						}

					} else if (entrymode.equalsIgnoreCase("date and time")) {
						value = validdata.replace("GT", "GT ")
								.replace("LT", "LT ").replace("BW", "BW ")
								.replace("GT ", "GT ").replace("LT ", "LT ")
								.replace("BW ", "BW ").split(" ");
						if (value.length == 0) {
							value = validdata.replace("GT", "GT ")
									.replace("LT", "LT ").replace("BW", "BW ")
									.split(" ");

						}

						if (validdata.length() == 0) {
							if (fields.getText().toString().trim().length() == 0) {
								hint.setVisibility(View.GONE);
								field_values.add("");

							} else {
								hint.setVisibility(View.GONE);
								field_values.add(fields.getText().toString()
										.trim());

							}
						} else if (value[0].equals("GT")) {
							if (fields.getText().toString().trim().length() == 0) {
								hint.setVisibility(View.GONE);
								field_values.add("");

							} else {
								hint.setVisibility(View.GONE);
								field_values.add(fields.getText().toString()
										.trim());

							}
						} else if (value[0].equals("LT")) {
							if (fields.getText().toString().trim().length() == 0) {
								hint.setVisibility(View.GONE);
								field_values.add("");

							} else {
								hint.setVisibility(View.GONE);
								field_values.add(fields.getText().toString()
										.trim());

							}

						} else if (value[0].equals("BW")) {
							field_values
									.add(fields.getText().toString().trim());
						}

					} else if (entrymode.equalsIgnoreCase("free text")) {
						if (validdata.equals("EMAIL")) {
							if (fields.getText().toString().trim().length() == 0) {
								hint.setVisibility(View.GONE);
								field_values.add("");

							} else {

								if (!validateEmail(fields.getText().toString()
										.trim())) {

									if (errortip.length() == 0) {
										hint.setVisibility(View.VISIBLE);
										hint.setText("Please enter valid Email address");
										hint.setTextColor(getResources()
												.getColor(R.color.orange));
									} else {
										hint.setVisibility(View.VISIBLE);
										hint.setText(errortip);
										hint.setTextColor(getResources()
												.getColor(R.color.orange));
									}

									break;
								} else {
									hint.setVisibility(View.GONE);
									field_values.add(fields.getText()
											.toString().trim());

								}

							}

						}

						else if (validdata.startsWith("MINIMUM")) {

							String[] minValue = validdata.split(" ");
							int minCount = Integer.parseInt(minValue[1]);
							if (fields.getText().toString().trim().length() == 0) {
								hint.setVisibility(View.GONE);
								field_values.add("");

							} else {
								if (fields.getText().toString().trim().length() < minCount) {
									if (errortip.length() == 0) {
										hint.setVisibility(View.VISIBLE);
										hint.setText("Text must be minimum "
												+ minCount + " characters");
										hint.setTextColor(getResources()
												.getColor(R.color.orange));
									} else {
										hint.setVisibility(View.VISIBLE);
										hint.setText(errortip);
										hint.setTextColor(getResources()
												.getColor(R.color.orange));
									}

									break;
								} else {
									hint.setVisibility(View.GONE);
									field_values.add(fields.getText()
											.toString().trim());

								}
							}
						} else if (validdata.startsWith("MAXIMUM")) {
							String[] maxValue = validdata.split(" ");
							int maxCount = Integer.parseInt(maxValue[1]);
							if (fields.getText().toString().trim().length() == 0) {
								hint.setVisibility(View.GONE);
								field_values.add("");

							} else {
								if (fields.getText().toString().trim().length() > maxCount) {

									if (errortip.length() == 0) {
										hint.setVisibility(View.VISIBLE);
										hint.setText("Text allowed maximum "
												+ maxCount + " characters");
										hint.setTextColor(getResources()
												.getColor(R.color.orange));
									} else {
										hint.setVisibility(View.VISIBLE);
										hint.setText(errortip);
										hint.setTextColor(getResources()
												.getColor(R.color.orange));
									}

								} else {
									hint.setVisibility(View.GONE);
									field_values.add(fields.getText()
											.toString().trim());
								}
							}
						} else if (validdata.equals("NO SPECIAL CHAR")) {
							if (fields.getText().toString().trim().length() == 0) {
								hint.setVisibility(View.GONE);
								field_values.add("");

							} else {
								if (!nospecialchar(fields.getText().toString()
										.trim())) {

									if (errortip.length() == 0) {
										hint.setVisibility(View.VISIBLE);
										hint.setText("Special characters are not allowed");
										hint.setTextColor(getResources()
												.getColor(R.color.orange));
									} else {
										hint.setVisibility(View.VISIBLE);
										hint.setText(errortip);
										hint.setTextColor(getResources()
												.getColor(R.color.orange));
									}

								} else {
									hint.setVisibility(View.GONE);
									field_values.add(fields.getText()
											.toString().trim());

								}
							}
						} else if (validdata.equals("BEGINS WITH ALPHA")) {
							if (fields.getText().toString().trim().length() == 0) {
								hint.setVisibility(View.GONE);
								field_values.add("");

							} else {
								if (!beginalpha(fields.getText().toString()
										.trim())) {

									if (errortip.length() == 0) {
										hint.setVisibility(View.VISIBLE);
										hint.setText("Starting letter must begins with alpha");
										hint.setTextColor(getResources()
												.getColor(R.color.orange));
									} else {
										hint.setVisibility(View.VISIBLE);
										hint.setText(errortip);
										hint.setTextColor(getResources()
												.getColor(R.color.orange));
									}

								} else {
									hint.setVisibility(View.GONE);
									field_values.add(fields.getText()
											.toString().trim());
								}
							}
						} else {
							if (fields.getText().toString().trim().length() == 0) {
								hint.setVisibility(View.GONE);
								field_values.add("");

							} else {

								field_values.add(fields.getText().toString()
										.trim());
								hint.setVisibility(View.GONE);

							}
						}

					} else if (entrymode.equalsIgnoreCase("numeric")) {

						try {
							if (validdata.length() > 0) {
								value = validdata.split(" ");

								if (value[0].equals("LT")) {
									fields.setInputType(InputType.TYPE_CLASS_NUMBER);

									if (fields.getText().toString().trim()
											.length() == 0) {
										hint.setVisibility(View.GONE);
										field_values.add("");
									} else {

										if (!(Integer.parseInt(fields.getText()
												.toString().trim()) < Integer
												.parseInt(value[1])))

										{

											if (errortip.length() == 0) {
												hint.setVisibility(View.VISIBLE);
												hint.setText("Please enter the value less than  "
														+ value[1]);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											} else {
												hint.setVisibility(View.VISIBLE);
												hint.setText(errortip);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											}

										} else {
											hint.setVisibility(View.GONE);
											field_values.add(fields.getText()
													.toString().trim());

										}
									}
								}

								else if (value[0].equals("LE")) {

									fields.setInputType(InputType.TYPE_CLASS_NUMBER);
									if (fields.getText().toString().trim()
											.length() == 0) {
										hint.setVisibility(View.GONE);
										field_values.add("");
									} else {
										if (!(Integer.parseInt(fields.getText()
												.toString().trim()) <= Integer
												.parseInt(value[1]))) {

											if (errortip.length() == 0) {
												hint.setVisibility(View.VISIBLE);
												hint.setText("Please enter the value less than or equal  "
														+ value[1]);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											} else {
												hint.setVisibility(View.VISIBLE);
												hint.setText(errortip);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											}

										} else {
											hint.setVisibility(View.GONE);
											field_values.add(fields.getText()
													.toString().trim());
										}
									}
								} else if (value[0].equals("GT")) {

									fields.setInputType(InputType.TYPE_CLASS_NUMBER);

									if (fields.getText().toString().trim()
											.length() == 0) {

										hint.setVisibility(View.GONE);
										field_values.add("");
									} else {

										if (!(Long.parseLong(fields.getText()
												.toString().trim()) > Integer
												.parseInt(value[1]))) {

											if (errortip.length() == 0) {
												hint.setVisibility(View.VISIBLE);
												hint.setText("Please enter the value greater than "
														+ value[1]);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											} else {
												hint.setVisibility(View.VISIBLE);
												hint.setText(errortip);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											}

										} else {
											hint.setVisibility(View.GONE);
											field_values.add(fields.getText()
													.toString().trim());
										}
									}
								}

								else if (value[0].equals("GE")) {

									fields.setInputType(InputType.TYPE_CLASS_NUMBER);

									if (fields.getText().toString().trim()
											.length() == 0) {
										hint.setVisibility(View.GONE);
										field_values.add("");
									} else {
										if (!(Long.parseLong(fields.getText()
												.toString().trim()) >= Integer
												.parseInt(value[1]))) {

											if (errortip.length() == 0) {
												hint.setVisibility(View.VISIBLE);
												hint.setText("Please enter value the greater than or equal "
														+ value[1]);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											} else {
												hint.setVisibility(View.VISIBLE);
												hint.setText(errortip);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											}

										} else {
											hint.setVisibility(View.GONE);
											field_values.add(fields.getText()
													.toString().trim());
										}
									}

								}

								else if (value[0].equals("EQ")) {

									fields.setInputType(InputType.TYPE_CLASS_NUMBER);

									if (fields.getText().toString().trim()
											.length() == 0) {
										hint.setVisibility(View.GONE);
										field_values.add("");
									} else {
										if (!(Long.parseLong(fields.getText()
												.toString().trim()) == Integer
												.parseInt(value[1]))) {

											if (errortip.length() == 0) {
												hint.setVisibility(View.VISIBLE);
												hint.setText("Please enter the value equal to "
														+ value[1]);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											} else {
												hint.setVisibility(View.VISIBLE);
												hint.setText(errortip);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											}

										} else {
											hint.setVisibility(View.GONE);
											field_values.add(fields.getText()
													.toString().trim());
										}
									}

								} else if (value[0].equals("NEQ")) {

									fields.setInputType(InputType.TYPE_CLASS_NUMBER);

									if (fields.getText().toString().trim()
											.length() == 0) {
										hint.setVisibility(View.GONE);
										field_values.add("");
									} else {
										if (!(Long.parseLong(fields.getText()
												.toString().trim()) != Integer
												.parseInt(value[1]))) {

											if (errortip.length() == 0) {
												hint.setVisibility(View.VISIBLE);
												hint.setText("Please enter the value not equal to "
														+ value[1]);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											} else {
												hint.setVisibility(View.VISIBLE);
												hint.setText(errortip);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											}

										} else {
											hint.setVisibility(View.GONE);
											field_values.add(fields.getText()
													.toString().trim());
										}
									}

								} else if (value[0].equals("Accuracy")) {

									if (fields.getText().toString().trim()
											.length() == 0) {
										hint.setVisibility(View.GONE);
										field_values.add("");
									} else {

										if (fields.getText().toString().trim()
												.length() == Long
												.parseLong(value[1])) {
											hint.setVisibility(View.GONE);
											field_values.add(fields.getText()
													.toString().trim());
										} else {

											if (errortip.length() == 0) {
												hint.setVisibility(View.VISIBLE);
												hint.setText("Please enter the value accuracy digit "
														+ value[1]);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											} else {
												hint.setVisibility(View.VISIBLE);
												hint.setText(errortip);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											}
										}
									}

								} else if (value[0].equals("BT")) {

									fields.setInputType(InputType.TYPE_CLASS_NUMBER);
									if (fields.getText().toString().trim()
											.length() == 0) {
										hint.setVisibility(View.GONE);
										field_values.add("");
									} else {
										if (!(Long.parseLong(fields.getText()
												.toString().trim()) > Long
												.parseLong(value[1]) && Long
												.parseLong(fields.getText()
														.toString().trim()) < Long
												.parseLong(value[3]))) {

											if (errortip.length() == 0) {
												hint.setVisibility(View.VISIBLE);
												hint.setText("Please enter the value between "
														+ value[1]
														+ " and "
														+ value[3]);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											} else {
												hint.setVisibility(View.VISIBLE);
												hint.setText(errortip);
												hint.setTextColor(getResources()
														.getColor(
																R.color.orange));
											}

										} else {
											hint.setVisibility(View.GONE);
											field_values.add(fields.getText()
													.toString().trim());
										}
									}

								}

							}

							else {
								if (fields.getText().toString().trim().length() == 0) {

									hint.setVisibility(View.GONE);
									field_values.add("");
								} else {

									field_values.add(fields.getText()
											.toString().trim());
									hint.setVisibility(View.GONE);

								}

							}
						} catch (NumberFormatException e) {
							if (AppReference.isWriteInFile)
								AppReference.logger.error(e.getMessage(), e);
							e.printStackTrace();
						} catch (NotFoundException e) {
							if (AppReference.isWriteInFile)
								AppReference.logger.error(e.getMessage(), e);
							e.printStackTrace();
						}
					} else if (entrymode.equalsIgnoreCase("compute")) {
						if (fields.getText().toString().trim().length() != 0) {
							field_values
									.add(fields.getText().toString().trim());

							for (int j = 0; j < field_values.size(); j++) {

								field_values.get(j);
							}

							hint.setVisibility(View.GONE);

						} else {

							hint.setVisibility(View.GONE);
							field_values.add("");

						}
					}

				}
			} catch (NumberFormatException e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);

			} catch (NotFoundException e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
			}
		}
	}

	private void updateDisplayTime() {

		timeD = pad(pHour) + ":" + pad(pMinute);

	}

	private String pad(int a) {

		if (a >= 10) {
			return String.valueOf(a);

		} else {

			return "0" + String.valueOf(a);
		}

	}

	/** Callback received when the user "picks" a time in the dialog */
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			pHour = hourOfDay;
			pMinute = minute;
			updateDisplayTime();
		}
	};

	// updates the date in the TextView
	private void updateDisplay() {

	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, pHour, pMinute,
					true);
		}

		return null;
	}

	private void clearFields() {
		boolean request = true;
		for (int i = 0; i < records_container.getChildCount(); i++) {
			LinearLayout layouts = (LinearLayout) records_container
					.getChildAt(i);
			if (field_list.get(i).contains("blob_")) {
				Log.i("HEART", "Inside Blob=========>");

				LinearLayout layout = (LinearLayout) records_container
						.getChildAt(i);
				LinearLayout child_lay = (LinearLayout) layout
						.findViewById(R.id.blob_layout);
				child_lay.setVisibility(View.GONE);
			}

			else {
				String entryMode = layouts.getContentDescription().toString();
				Log.i("HEART", "EntryMode=========>" + entryMode);

				if (entryMode.equalsIgnoreCase("current time")) {
					LinearLayout layout = (LinearLayout) records_container
							.getChildAt(i);
					EditText fields = (EditText) layout
							.findViewById(R.id.edtxt_frmfield);
					fields.setText(getCurrentTime());
				} else if (entryMode.equalsIgnoreCase("current date")) {
					LinearLayout layout = (LinearLayout) records_container
							.getChildAt(i);
					EditText fields = (EditText) layout
							.findViewById(R.id.edtxt_frmfield);
					fields.setText(getCurrentDate());

				} else if (entryMode.equalsIgnoreCase("current location")) {
					LinearLayout layout = (LinearLayout) records_container
							.getChildAt(i);
					EditText fields = (EditText) layout
							.findViewById(R.id.edtxt_frmfield);
					fields.setText("");

				} else {

					LinearLayout layout = (LinearLayout) records_container
							.getChildAt(i);
					EditText fields = (EditText) layout
							.findViewById(R.id.edtxt_frmfield);
					fields.setText("");

				}

			}

		}
		Set<String> set = bmp_container.keySet();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			Bitmap bmp = (Bitmap) bmp_container.get(iterator.next());
			if (bmp != null) {
				if (!bmp.isRecycled()) {
					bmp.recycle();
					bmp = null;
				}
			}
		}

		if (bmp_container.size() > 0) {
			bmp_container.clear();
		}
	}

	public void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		Log.i("HEART", "OnDestroy CAll from  FormRecord");
		if (WebServiceReferences.contextTable.containsKey("frmviewer")) {
			FormViewer viewer = (FormViewer) WebServiceReferences.contextTable
					.get("frmviewer");
			viewer.refreshList();
		}

		if (SingleInstance.contextTable.containsKey("forms")) {
			FormsFragment quickActionFragment = FormsFragment
					.newInstance(context);

			quickActionFragment.populateLists();

		}

		Set<String> set = bmp_container.keySet();
		Iterator<String> iterator = set.iterator();

		while (iterator.hasNext()) {
			Bitmap bmp = (Bitmap) bmp_container.get(iterator.next());
			if (bmp != null) {
				if (!bmp.isRecycled()) {
					bmp.recycle();
					bmp = null;
				}
			}
		}

		if (issipcall) {
			// callDisp.SIPCallisAccepted = false;

			isreject = true;
			// for (com.cg.hostedconf.CallerBean callerbean :
			// AppReference.process_members) {
			//
			// if (callerbean.getCall_id() != -1) {
			//
			// CommunicationBean cBean1 = new CommunicationBean();
			// cBean1.setOperationType(sip_operation_types.HANGUP_SINGLE_CALL);
			// cBean1.setCall_id(callerbean.getCall_id());
			// AppReference.sipQueue.addMsg(cBean1);
			// }
			// }

			// AppReference.process_members.clear();

		}
		// if (callerBean_Array != null) {
		// callerBean_Array.clear();
		// }

		if (WebServiceReferences.contextTable.containsKey("frmreccreator")) {

			WebServiceReferences.contextTable.remove("frmreccreator");
		}

	}

	private InputFilter[] getNoSpclchar() {
		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (end > start) {

					char[] acceptedChars = new char[] { 'a', 'b', 'c', 'd',
							'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
							'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
							'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
							'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
							'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1',
							'2', '3', '4', '5', '6', '7', '8', '9', ' ' };

					for (int index = start; index < end; index++) {
						if (!new String(acceptedChars).contains(String
								.valueOf(source.charAt(index)))) {
							return "";
						}
					}
				}
				return null;
			}

		};
		return filters;
	}

	private InputFilter[] getFilter() {
		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (end > start) {

					char[] acceptedChars = new char[] { 'a', 'b', 'c', 'd',
							'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
							'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
							'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
							'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
							'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1',
							'2', '3', '4', '5', '6', '7', '8', '9', '_', '-',
							' ' };

					for (int index = start; index < end; index++) {
						if (!new String(acceptedChars).contains(String
								.valueOf(source.charAt(index)))) {
							return "";
						}
					}
				}
				return null;
			}

		};
		return filters;
	}

	public void notifyWebServiceResponse(final Object obj) {

		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				cancelDialog(true);
				if (obj instanceof String[]) {
					String[] response = (String[]) obj;
					if (hastocreate_another) {
						String id = response[0];
						String formtime = response[2];
						String status = "1";

						ContentValues values = new ContentValues();
						values.put("[tableid]", id);
						for (int i = 0; i < field_list.size(); i++) {
							String columnane = "[" + field_list.get(i) + "]";

							if (dtypes.get(field_list.get(i)).equalsIgnoreCase(
									"BLOB")) {
								values.put(columnane,
										convertbmptoByteArray(bmp_container
												.get(Integer.toString(i))));
							} else if (dtypes.get(field_list.get(i))
									.equalsIgnoreCase("INTEGER")) {
								try {
									values.put(columnane, Integer
											.parseInt(field_values.get(i)));
								} catch (NumberFormatException e) {
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);
									e.printStackTrace();
								}
							} else {
								values.put(columnane, field_values.get(i));

							}
						}
						values.put("[recorddate]", formtime);
						values.put("[status]", status);

						if (callDisp.getdbHeler(context).insertForRecords(
								title + "_" + table_id, values)) {

							String count = callDisp.getdbHeler(context)
									.getRecordCount(
											"[" + title + "_" + table_id + "]");

							String strQuery = "update formslookup set rowcount='"
									+ count
									+ "' ,formtime='"
									+ formtime
									+ "' ,status='"
									+ "1"
									+ "' where tableid="
									+ table_id;

							callDisp.getdbHeler(context).ExecuteQuery(strQuery);
							field_list.remove(field_list.size() - 1);
							field_list.remove(field_list.size() - 1);
							field_list.remove(field_list.size() - 1);

							field_values.remove(field_list.size() - 1);
							field_values.remove(field_list.size() - 1);
							field_values.remove(field_list.size() - 1);

							clearFields();

						}

					} else {
						String id = response[0];
						String formtime = response[2];
						String status = "1";

						ContentValues values = new ContentValues();
						values.put("[tableid]", id);
						Log.i("formdirect", "Response:before for loop"
								+ field_list.size());

						for (int i = 0; i < field_list.size(); i++) {
							String columnane = "[" + field_list.get(i) + "]";

							if (dtypes.get(field_list.get(i)).equalsIgnoreCase(
									"BLOB")) {
								values.put(columnane,
										convertbmptoByteArray(bmp_container
												.get(Integer.toString(i))));
							} else if (dtypes.get(field_list.get(i))
									.equalsIgnoreCase("INTEGER")) {
								if (field_values.get(i).length() > 0) {
									try {
										values.put(columnane, Integer
												.parseInt(field_values.get(i)));
									} catch (NumberFormatException e) {
										if (AppReference.isWriteInFile)
											AppReference.logger.error(
													e.getMessage(), e);
										e.printStackTrace();
									}
								}

							} else {

								values.put(columnane, field_values.get(i));
							}
						}

						values.put("[recorddate]", formtime);
						values.put("[status]", status);

						if (callDisp.getdbHeler(context).insertForRecords(
								title + "_" + table_id, values)) {

							String count = callDisp.getdbHeler(context)
									.getRecordCount(
											"[" + title + "_" + table_id + "]");
							String strQuery = "update formslookup set rowcount='"
									+ count
									+ "' ,formtime='"
									+ formtime
									+ "' ,status='"
									+ "1"
									+ "' where tableid="
									+ table_id;

							callDisp.getdbHeler(context).ExecuteQuery(strQuery);
							Log.i("formdirect",
									"Response:size of filed list is:"
											+ field_list.size());

							field_list.remove(field_list.size() - 1);
							field_list.remove(field_list.size() - 1);
							field_list.remove(field_list.size() - 1);

							field_values.remove(field_list.size() - 1);
							field_values.remove(field_list.size() - 1);
							field_values.remove(field_list.size() - 1);
							GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
									.get("groupchat");
							if (gChat != null) {
								String groupMembers = gChat
										.getGroupAndMembers();
								String[] members = groupMembers.split(",");
								ArrayList<String> membersList = new ArrayList<String>();
								for (int i = 0; i < members.length; i++) {
									if (!DBAccess.getdbHeler().isRecordExists(
											"select * from formsettings where formowenerid='"
													+ CallDispatcher.LoginUser
													+ "' and formid='"
													+ table_id
													+ "' and buddyid='"
													+ members[i] + "'")) {
										membersList.add(members[i]);

									}
								}
								if (membersList.size() > 0) {
									gChat.notifyShareFormBeforeAddRecords(
											title, table_id, membersList);
								} else {
									gChat.notifyFormEdited(title);
								}

							}
							finish();

						}

					}
				} else if (obj instanceof WebServiceBean) {
					WebServiceBean service_bean = (WebServiceBean) obj;
					showToast(service_bean.getText());
					finish();
				}
			}
		});
	}

	private void showprogress() {

		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setMessage("Progress ...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.show();

	}

	public void cancelDialog(boolean flag) {

		if (flag) {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;

			}
		} else {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.unable_to_connect_server),
						Toast.LENGTH_LONG).show();
			}

			field_list.remove("uuid");
			field_list.remove("euuid");
			field_list.remove("uudate");

		}
	}

	private void showBlobSelector() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				FormRecordsCreators.this);
		builder.create();
		builder.setTitle(SingleInstance.mainContext.getResources()
				.getString(R.string.multimedia_option));
		final CharSequence[] choiceList = { "From Gallery", "From Camera",
				"Audio", "Video", "Hand Sketch" };
		int selected = -1;
		builder.setSingleChoiceItems(choiceList, selected,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int index) {
						OpenBlobInterface(index);
						alert.cancel();
					}
				});
		alert = builder.create();
		alert.show();

	}

	private void OpenBlobInterface(int selected_option) {
		new Bundle();
		switch (selected_option) {
		case 0:
			if (Build.VERSION.SDK_INT < 19) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, 2);
			} else {
				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(intent, 19);
			}
			break;
		case 1:

			Long free_size = getAvailableExternalMemorySize();
			if (free_size > 0 && free_size >= 5120) {
				blob_path = "/sdcard/COMMedia/MPD_" + getFileName() + ".jpg";
				ComponentPath = blob_path;
				// Intent intent = new Intent(context, MultimediaUtils.class);
				//
				// intent.putExtra("filePath", blob_path);
				// intent.putExtra("requestCode", 0);
				// intent.putExtra("action", MediaStore.ACTION_IMAGE_CAPTURE);
				// intent.putExtra("createOrOpen", "create");
				// startActivity(intent);
				Intent intent = new Intent(context, CustomVideoCamera.class);
				intent.putExtra("filePath", blob_path);
				intent.putExtra("isPhoto", true);
				startActivityForResult(intent, 0);

			} else {
				showToast(SingleInstance.mainContext.getResources().getString(
						R.string.insufficient_memory));
			}

			break;

		case 2:
			blob_path = "/sdcard/COMMedia/" + "MAD_audio"
					+ CompleteListView.getFileName() + ".mp4";
			ComponentPath = blob_path;

			Intent intent = new Intent(context, MultimediaUtils.class);
			intent.putExtra("filePath", blob_path);
			intent.putExtra("requestCode", 100);
			intent.putExtra("action", "audio");
			intent.putExtra("createOrOpen", "create");
			startActivity(intent);

			break;
		case 3:

			blob_path = "/sdcard/COMMedia/" + "MVD_"
					+ CompleteListView.getFileName() + ".mp4";

			Log.i("HEART", "Selection is====>video 3" + "Blob ===>" + blob_path);
			ComponentPath = blob_path;
			Log.i("HEART", "Selection is====>video 3" + "Component ===>"
					+ ComponentPath);

			Intent intents = new Intent(context, CustomVideoCamera.class);
			intents.putExtra("filePath", blob_path);
			// intents.putExtra("requestCode", 100);
			// intents.putExtra("action", MediaStore.ACTION_VIDEO_CAPTURE);
			// intents.putExtra("createOrOpen", "create");
			startActivityForResult(intents, 100);

			break;
		case 4:
			Intent intent2 = new Intent(context, HandSketchActivity2.class);
			intent2.putExtra("filepath", blob_path);
			startActivityForResult(intent2, 101);

			break;
		default:
			break;
		}
	}

	private static String getFileName() {
		String strFilename;
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHH_mm_ss");
		Date date = new Date();
		strFilename = dateFormat.format(date);
		return strFilename;
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

	static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	private void setBlobImage(ImageView image, String id) {
		if (isUpload) {
			name.split("\\.");
			String filenames = name;
			if (name.contains(".jpg")) {
				bitmap = callDisp.ResizeImage(blob_path);
				if (bitmap != null)
					bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150, true);
				else
					bitmap = BitmapFactory.decodeResource(getResources(),
							R.drawable.v_play);

			} else if (blob_path.contains("MVD_"))

			{
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.v_play);

				if (name.contains(".mp4")) {
					filenames = name;
				} else {

					filenames = name + ".mp4";

				}

			} else

			{
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.v_play);

			}
			if (bitmap != null) {
				image.setImageBitmap(bitmap);
				image.setContentDescription(filenames);
				image.setVisibility(View.VISIBLE);
				bmp_container.put(id, bitmap);
			} else {
				image.setBackgroundResource(R.drawable.broken);
			}
		}
	}

	public class bitmaploader extends AsyncTask<Uri, Void, Void> {

		@Override
		protected void onPostExecute(Void Result) {
			super.onPostExecute(Result);
			try {
				cancelDialog(true);
				Bitmap result = null;
				if (blob_path != null)
					result = callDisp.ResizeImage(blob_path);
				if (result != null) {
					FileOutputStream fout = new FileOutputStream(blob_path);
					result.compress(CompressFormat.JPEG, 75, fout);
					fout.flush();
					fout.close();
					result.recycle();
					File selected_file = new File(blob_path);
					int length = (int) selected_file.length() / 1048576;
					if (length <= 2) {
						String[] image = Copy(blob_path).split("/");
						name = image[image.length - 1];
						uploadConfiguredNote(Copy(blob_path));
						LinearLayout blob_row = (LinearLayout) records_container
								.getChildAt(selected_rowid);
						LinearLayout blob_layout = (LinearLayout) blob_row
								.findViewById(R.id.blob_layout);
						blob_image = (ImageView) blob_row
								.findViewById(R.id.iview_blobimage);
						setBlobImage(blob_image,
								Integer.toString(blob_row.getId()));
						blob_layout.setVisibility(View.VISIBLE);
					} else {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.large_image));
					}
				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.cannot_read_image));
				}
			} catch (Exception e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
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
					blob_path = Environment.getExternalStorageDirectory()
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
					FileOutputStream fout = new FileOutputStream(blob_path);
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
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
			return null;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 2:
			if (requestCode == Activity.RESULT_CANCELED) {
				if (!bmp_container
						.containsKey(Integer.toString(selected_rowid))) {
					blob_path = null;
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.choose_image));
					LinearLayout blob_row = (LinearLayout) records_container
							.getChildAt(selected_rowid);
					LinearLayout blob_layout = (LinearLayout) blob_row
							.findViewById(R.id.blob_layout);
					blob_layout.setVisibility(View.GONE);
				}
			}

			else {
				if (data != null) {

					Uri selectedImageUri = data.getData();
					blob_path = callDisp.getRealPathFromURI(selectedImageUri);
					File selected_file = new File(blob_path);
					int length = (int) selected_file.length() / 1048576;
					if (length <= 2) {
						String[] image = Copy(blob_path).split("/");
						name = image[image.length - 1];
						uploadConfiguredNote(Copy(blob_path));
						LinearLayout blob_row = (LinearLayout) records_container
								.getChildAt(selected_rowid);
						LinearLayout blob_layout = (LinearLayout) blob_row
								.findViewById(R.id.blob_layout);
						blob_image = (ImageView) blob_row
								.findViewById(R.id.iview_blobimage);
						setBlobImage(blob_image,
								Integer.toString(blob_row.getId()));
						blob_layout.setVisibility(View.VISIBLE);
					} else {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.large_image));
					}

				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.choose_image));
				}
			}

			break;
		case 19:
			if (requestCode == Activity.RESULT_CANCELED) {
				if (!bmp_container
						.containsKey(Integer.toString(selected_rowid))) {
					blob_path = null;
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.choose_image));
					LinearLayout blob_row = (LinearLayout) records_container
							.getChildAt(selected_rowid);
					LinearLayout blob_layout = (LinearLayout) blob_row
							.findViewById(R.id.blob_layout);
					blob_layout.setVisibility(View.GONE);
				}
			}

			else {
				if (data != null) {

					Uri selectedImageUri = data.getData();
					new bitmaploader().execute(selectedImageUri);

				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.choose_image));
				}
			}
			break;

		case 0:
			String[] image = blob_path.split("/");
			name = image[image.length - 1];
			File file = new File(blob_path);
			if (file.exists()) {
				Bitmap bmp = callDisp.ResizeImage(blob_path);
				callDisp.changemyPictureOrientation(bmp, blob_path);
				if (bmp != null && bmp.isRecycled())
					bmp.recycle();
				bmp = null;
				uploadConfiguredNote(blob_path);
				LinearLayout blob_row = (LinearLayout) records_container
						.getChildAt(selected_rowid);
				LinearLayout blob_layout = (LinearLayout) blob_row
						.findViewById(R.id.blob_layout);
				ImageView blob_image = (ImageView) blob_row
						.findViewById(R.id.iview_blobimage);
				setBlobImage(blob_image, Integer.toString(blob_row.getId()));
				blob_layout.setVisibility(View.VISIBLE);

			} else {

				if (!bmp_container
						.containsKey(Integer.toString(selected_rowid))) {
					blob_path = null;
					showToast("Kindly Choose any one Image");
					LinearLayout blob_row = (LinearLayout) records_container
							.getChildAt(selected_rowid);
					LinearLayout blob_layout = (LinearLayout) blob_row
							.findViewById(R.id.blob_layout);
					blob_layout.setVisibility(View.GONE);
				}

			}

			break;
		case 1:

			if ((requestCode == 1) && (resultCode == -1)) {

				Bundle bun = data.getBundleExtra("share");
				if (bun != null) {
					String path = bun.getString("query");
					if (path.equalsIgnoreCase("success")) {
						recordinsert();
					}
				}

			}
			break;

		case 10:

			if ((requestCode == 10) && (resultCode == -1)) {

				Bundle bun = data.getBundleExtra("share");
				if (bun != null) {
					String path = bun.getString("changes");
					if (path.equalsIgnoreCase("yes")) {
						if (update) {
							loadformrecordsFields();

						} else {

							loadformFields();

						}
					}
				}

			}
			break;
		case 100:
			if (requestCode == Activity.RESULT_CANCELED) {
				if (!bmp_container
						.containsKey(Integer.toString(selected_rowid))) {
					blob_path = null;
					showToast("Kindly Choose any one Video");
					LinearLayout blob_row = (LinearLayout) records_container
							.getChildAt(selected_rowid);
					LinearLayout blob_layout = (LinearLayout) blob_row
							.findViewById(R.id.blob_layout);
					blob_layout.setVisibility(View.GONE);
				}
			}

			else {

				Log.i("HEART", "COmponent Path===>" + ComponentPath);

				if (ComponentPath.length() > 0) {
					String path = ComponentPath;

					if (path.contains("MVD") || path.contains("MAD")) {
						File myFile = new File(path);
						if (myFile.exists()) {
							if (path != null) {
								blob_path = path;
								uploadConfiguredNote(blob_path);

								Log.i("FORMSOPTMZ", "=====>Upload File"
										+ blob_path);

								String[] image1 = blob_path.split("/");
								name = image1[image1.length - 1];
								LinearLayout blob_row = (LinearLayout) records_container
										.getChildAt(selected_rowid);
								LinearLayout blob_layout = (LinearLayout) blob_row
										.findViewById(R.id.blob_layout);
								ImageView blob_image = (ImageView) blob_row
										.findViewById(R.id.iview_blobimage);
								LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
										LinearLayout.LayoutParams.WRAP_CONTENT,
										LinearLayout.LayoutParams.WRAP_CONTENT);
								params.setMargins(1, 5, 1, 1);
								params.gravity = Gravity.CENTER_VERTICAL;
								blob_image.setLayoutParams(params);
								setBlobImage(blob_image,
										Integer.toString(blob_row.getId()));
								blob_layout.setVisibility(View.VISIBLE);
							} else {
								if (!bmp_container.containsKey(Integer
										.toString(selected_rowid))) {
									blob_path = null;
									showToast(SingleInstance.mainContext
											.getResources().getString(
													R.string.choose_audio));
									LinearLayout blob_row = (LinearLayout) records_container
											.getChildAt(selected_rowid);
									LinearLayout blob_layout = (LinearLayout) blob_row
											.findViewById(R.id.blob_layout);
									blob_layout.setVisibility(View.GONE);
								}

							}
						} else {
							if (path.contains("MAD")) {
								showToast(SingleInstance.mainContext
										.getResources().getString(
												R.string.choose_audio));

							} else if (path.contains("MVD")) {
								showToast(SingleInstance.mainContext
										.getResources().getString(
												R.string.choose_video));

							}

						}

					}
				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.choose_video));

				}

			}

			break;
		case 101:

			if (resultCode == Activity.RESULT_CANCELED) {
				if (!bmp_container
						.containsKey(Integer.toString(selected_rowid))) {
					blob_path = null;
					showToast("Kindly Choose any one Image");
					LinearLayout blob_row = (LinearLayout) records_container
							.getChildAt(selected_rowid);
					LinearLayout blob_layout = (LinearLayout) blob_row
							.findViewById(R.id.blob_layout);
					blob_layout.setVisibility(View.GONE);
				}
			} else {
				if (data != null) {
					Bundle bun = data.getBundleExtra("sketch");
					String hspath = bun.getString("path");
					// Bundle hpath=data.getExtras();
					// String hspath=hpath.getString("path");
					Log.i("hs", "hspath" + hspath);
					blob_path = hspath;
					ComponentPath = blob_path;
					Log.i("hs", "blob_path" + blob_path);
					String[] hsimage = blob_path.split("/");
					name = hsimage[hsimage.length - 1];
				}
				File filehs = new File(blob_path);

				if (filehs.exists()) {
					Bitmap bmp = callDisp.ResizeImage(blob_path);
					callDisp.changemyPictureOrientation(bmp, blob_path);
					if (bmp != null && bmp.isRecycled())
						bmp.recycle();
					bmp = null;
					uploadConfiguredNote(blob_path);
					LinearLayout blob_row = (LinearLayout) records_container
							.getChildAt(selected_rowid);
					LinearLayout blob_layout = (LinearLayout) blob_row
							.findViewById(R.id.blob_layout);
					ImageView blob_image = (ImageView) blob_row
							.findViewById(R.id.iview_blobimage);
					setBlobImage(blob_image, Integer.toString(blob_row.getId()));
					blob_layout.setVisibility(View.VISIBLE);

				}

			}

			break;

		default:
			break;
		}
		if (WebServiceReferences.contextTable.containsKey("multimediautils"))
			((MultimediaUtils) WebServiceReferences.contextTable
					.get("multimediautils")).finish();

	}

	public String Copy(String path) {
		// TODO Auto-generated method stub
		String destinationImagePath = null;
		try {
			File sd = Environment.getExternalStorageDirectory();
			if (sd.canWrite()) {
				String external = Environment.getExternalStorageDirectory()
						.toString();

				destinationImagePath = "/COMMedia/MPD_" + getFileName()
						+ ".jpg";
				String filepath = path.replace(external, "");
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
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
		}

		return "/mnt/sdcard" + destinationImagePath;

	}

	private void uploadConfiguredNote(String path) {
		showprogress();

		callDisp.uploadofflineResponse(path, false, 1, "forms");
	}

	private void downloadConfiguredNote(String path) {

		if (path != null) {
			if (CallDispatcher.LoginUser != null) {
				callDisp.downloadOfflineresponse(path, "", "forms", "");
			}
		}
	}

	private String encodeTobase64(Bitmap image) {
		Bitmap immagex = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

		return imageEncoded;
	}

	private byte[] convertbmptoByteArray(Bitmap bmp) {
		Bitmap immagex = bmp;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
		byte[] b = baos.toByteArray();
		return b;
	}

	private Bitmap decodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory
				.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	public static String getNoteCreateTime() {
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
		return sdf.format(curDate);
	}

	private String getCurrentDate() {
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(curDate);
	}

	private String getCurrentTime() {
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		return sdf.format(curDate);

	}

	public void notifyWebServiceResponse1(final Object obj) {
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				cancelDialog(true);
				if (obj instanceof String[]) {
					String[] response = (String[]) obj;
					if (hastocreate_another) {
						String tableid = response[0];

						ContentValues values = new ContentValues();
						values.put("[tableid]", tableid);

						for (int i = 0; i < field_list.size(); i++) {
							String columnane = "[" + field_list.get(i) + "]";

							if (dtypes.get(field_list.get(i)).equalsIgnoreCase(
									"BLOB")) {
								values.put(columnane,
										convertbmptoByteArray(bmp_container
												.get(Integer.toString(i))));
							} else if (dtypes.get(field_list.get(i))
									.equalsIgnoreCase("INTEGER")) {
								if (field_values.get(i).length() > 0) {
									try {
										values.put(columnane, Integer
												.parseInt(field_values.get(i)));
									} catch (NumberFormatException e) {
										if (AppReference.isWriteInFile)
											AppReference.logger.error(
													e.getMessage(), e);
										e.printStackTrace();
									}
								}

							} else {
								values.put(columnane, field_values.get(i));
							}
						}
						values.put("[status]", "1");

						if (callDisp.getdbHeler(context).update(
								"[" + title + "_" + updateformid + "]", values,
								updaterecordid))
							;
						{

							field_list.remove(field_list.size() - 1);
							field_list.remove(field_list.size() - 1);
							field_values.remove(field_list.size() - 1);
							field_values.remove(field_list.size() - 1);

							clearFields();

						}

					} else {

						String tableid = response[0];

						ContentValues values = new ContentValues();
						values.put("[tableid]", tableid);

						for (int i = 0; i < field_list.size(); i++) {
							String columnane = "[" + field_list.get(i) + "]";

							if (dtypes.get(field_list.get(i)).equalsIgnoreCase(
									"BLOB")) {
								values.put(columnane,
										convertbmptoByteArray(bmp_container
												.get(Integer.toString(i))));
							} else if (dtypes.get(field_list.get(i))
									.equalsIgnoreCase("INTEGER")) {
								if (field_values.get(i).length() > 0) {

									try {
										values.put(columnane, Integer
												.parseInt(field_values.get(i)));
									} catch (NumberFormatException e) {
										if (AppReference.isWriteInFile)
											AppReference.logger.error(
													e.getMessage(), e);
										e.printStackTrace();
									}
								}
							} else {
								values.put(columnane, field_values.get(i));
							}
						}
						values.put("[status]", "1");

						if (callDisp.getdbHeler(context).update(
								"[" + title + "_" + updateformid + "]", values,
								updaterecordid)) {

							field_list.remove(field_list.size() - 1);
							field_list.remove(field_list.size() - 1);
							field_values.remove(field_list.size() - 1);
							field_values.remove(field_list.size() - 1);
							if (WebServiceReferences.contextTable
									.containsKey("frmviewer")) {
								FormViewer viewer = (FormViewer) WebServiceReferences.contextTable
										.get("frmviewer");
								viewer.refreshList();
							}
							GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
									.get("groupchat");
							if (gChat != null) {
								String groupMembers = gChat
										.getGroupAndMembers();
								String[] members = groupMembers.split(",");
								ArrayList<String> membersList = new ArrayList<String>();
								for (int i = 0; i < members.length; i++) {
									if (!DBAccess.getdbHeler().isRecordExists(
											"select * from formsettings where formowenerid='"
													+ CallDispatcher.LoginUser
													+ "' and formid='"
													+ tableid
													+ "' and buddyid='"
													+ members[i] + "'")) {
										membersList.add(members[i]);

									}
								}
								if (membersList.size() > 0) {
									gChat.notifyShareFormBeforeAddRecords(
											tableid, title, membersList);
								} else {
									gChat.notifyFormEdited(title);
								}

							}
							finish();
						}

					}
				} else if (obj instanceof WebServiceBean) {
					WebServiceBean bn = (WebServiceBean) obj;
					showToast(bn.getText());
				}
			}
		});

	}

	public void UploadingCompleted() {
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				cancelDialog(true);
			}
		});

	}

	public void downloadinCompleted() {
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				loadformFields();
			}
		});
	}

	public void notifyFileUploadError() {
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.upload_failed), Toast.LENGTH_LONG)
						.show();
				cancelDialog(true);

			}
		});
	}

	public void notifyFileDownloadError() {
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, "Downloading Failed ",
						Toast.LENGTH_LONG).show();

			}
		});
	}

	public class base64Decoder extends
			AsyncTask<ArrayList<Object>, Void, Bitmap> {
		ImageView iview;

		@Override
		protected Bitmap doInBackground(ArrayList<Object>... params) {

			ArrayList<Object> list = params[0];
			String bmp_array = (String) list.get(0);
			iview = (ImageView) list.get(1);

			String blob_path = Environment.getExternalStorageDirectory()
					+ "/COMMedia/" + bmp_array;
			final File fileCheckV = new File(blob_path);
			if (fileCheckV.exists()) {
				bitmap = callDisp.ResizeImage(blob_path);
				if (bitmap != null)
					bitmap = Bitmap.createScaledBitmap(bitmap, 200, 100, false);

			} else {

				downloadConfiguredNote(bmp_array);
			}

			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null)

				iview.setImageBitmap(result);

			super.onPostExecute(result);
		}

	}

	protected void datefield() {

		String clonestring = new String(formula);
		symbol = "";

		String formula1 = formula.replace("+", ",").replace("-", ",")
				.replace("*", ",").replace("/", ",").replace("%", ",");

		String[] array = formula1.split(",");

		int pos = 0;

		for (int i = 0; i < array.length - 1; i++) {

			if (symbol.trim().length() == 0) {
				if (fieldvalues.containsKey(array[i])) {

					symbol = fieldvalues.get(array[i])
							+ clonestring.charAt(array[0].length());

				} else {

					symbol = array[i] + clonestring.charAt(array[0].length());

				}

				pos = array[0].trim().length();

			} else {
				if (fieldvalues.containsKey(array[i])) {

					symbol = symbol + fieldvalues.get(array[i])
							+ clonestring.charAt(pos + array[i].length() + 1);

				} else {
					symbol = symbol + array[i]
							+ clonestring.charAt(pos + array[i].length() + 1);

				}

				pos = pos + array[i].trim().length() + 1;

			}

			if (i == array.length - 2) {

				if (fieldvalues.containsKey(array[i + 1])) {

					symbol = symbol + fieldvalues.get(array[i + 1]);

				} else {
					symbol = symbol + array[i + 1];

				}

			}

		}

	}

	public String formulafield(String formulas) {

		String clonestring = new String(formulas);
		symbol = "";

		String formula1 = formulas.replace("+", ",").replace("-", ",")
				.replace("*", ",").replace("/", ",").replace("%", ",");

		String[] array = formula1.split(",");

		int pos = 0;

		for (int i = 0; i < array.length - 1; i++) {

			if (symbol.trim().length() == 0) {

				if (fieldvalues.containsKey(array[i].trim())) {

					symbol = fieldvalues.get(array[i].trim())
							+ clonestring.charAt(array[0].length());

				} else {

					symbol = array[i] + clonestring.charAt(array[0].length());

				}

				pos = array[0].trim().length();

			} else {

				if (fieldvalues.containsKey(array[i].trim())) {

					symbol = symbol + fieldvalues.get(array[i].trim())
							+ clonestring.charAt(pos + array[i].length() + 2);

				} else {
					symbol = symbol + array[i].trim()
							+ clonestring.charAt(pos + array[i].length() + 2);

				}

				pos = pos + array[i].trim().length() + 1;

			}

			if (i == array.length - 2) {

				if (fieldvalues.containsKey(array[i + 1].trim())) {

					symbol = symbol + fieldvalues.get(array[i + 1].trim());

				} else {
					symbol = symbol + array[i + 1];

				}

			}

		}
		return symbol;

	}

	protected void freetext(String formula) {
		new String(formula);
		symbol = "";
		String formula1 = formula.replace("+", ",").replace("-", ",")
				.replace("*", ",").replace("/", ",").replace("%", ",");

		String[] array = formula1.split(",");

		int pos = 0;

		for (int i = 0; i < array.length - 1; i++) {
			if (fieldvalues.get(array[i].toString().trim()) != null
					&& !fieldvalues.get(array[i].toString().trim())
							.equalsIgnoreCase("null")) {
				if (symbol.trim().length() == 0) {
					symbol = fieldvalues.get(array[i].toString().trim());

					pos = array[0].trim().length();

				} else {
					symbol = symbol
							+ fieldvalues.get(array[i].toString().trim());
					pos = pos + array[i].trim().length() + 1;

				}

				if (i == array.length - 2) {
					Log.i("forms_test", "inside if array.lenght -2 fieldvalues"
							+ fieldvalues.get(array[i + 1]));
					if (fieldvalues.get(array[i + 1].toString().trim()) != null
							&& !fieldvalues.get(array[i + 1].toString().trim())
									.equalsIgnoreCase("null")) {
						symbol = symbol + fieldvalues.get(array[i + 1]);
					}
				}
			}
		}

	}

	public void getDisplay(String DataType, String formula) {
		field_values.clear();
		fieldvalues.clear();

		for (int i = 0; i < records_container.getChildCount() - 1; i++) {

			LinearLayout layout = (LinearLayout) records_container
					.getChildAt(i);
			final EditText fields = (EditText) layout
					.findViewById(R.id.edtxt_frmfield);
			final TextView key = (TextView) layout
					.findViewById(R.id.tview_clms);
			final ImageView im = (ImageView) layout
					.findViewById(R.id.iview_dtype);

			hint = (TextView) layout.findViewById(R.id.tview_clm_hint);

			if (!im.getContentDescription().toString()
					.equalsIgnoreCase("multimedia")) {
				if (fields.getText().toString().trim().length() != 0) {
					fieldvalues.put(key.getText().toString().trim(), fields
							.getText().toString().trim());

					hint.setVisibility(View.GONE);

				} else {
					i = records_container.getChildCount();
					if (!fields.getHint().toString().startsWith("Tap to")) {
						hint.setVisibility(View.VISIBLE);
						hint.setText("Please enter the value in form field");
						hint.setTextColor(getResources().getColor(
								R.color.orange));

					}

				}
			} else {

				fieldvalues.put("multimediafile", "multimedia");

			}

		}

		if (DataType.equalsIgnoreCase("Numeric")) {

			formulafield(formula);

		} else if (DataType.equalsIgnoreCase("Free Text")) {

			freetext(formula);
		}

		else if (DataType.equalsIgnoreCase("Date")) {

			substring(formula);

		}

	}

	public void getDate() {
		field_values.clear();
		for (int i = 0; i < records_container.getChildCount() - 1; i++) {

			LinearLayout layout = (LinearLayout) records_container
					.getChildAt(i);
			final EditText fields = (EditText) layout
					.findViewById(R.id.edtxt_frmfield);

			hint = (TextView) layout.findViewById(R.id.tview_clm_hint);
			if (field_list.get(i).contains("-")) {
				field_list.get(i).split("-");

			} else {
				field_list.get(i);

			}

			if (fields.getText().toString().trim().length() != 0) {
				field_values.add(fields.getText().toString().trim());

				hint.setVisibility(View.GONE);

			} else {
				hint.setVisibility(View.VISIBLE);
				hint.setText("Please enter the value in form field");
				hint.setTextColor(getResources().getColor(R.color.orange));
			}
		}
	}

	private void substring(String str_date1) {

		String values[] = str_date1.split("-");

		HashSet<String> Compute_Fields = new HashSet<String>();

		for (int i = 0; i < values.length; i++) {
			Compute_Fields.add(values[i]);
		}

		if (fieldvalues.containsKey(values[0].trim())
				&& fieldvalues.containsKey(values[1].trim())) {
			String date1 = fieldvalues.get(values[0].trim());
			String date2 = fieldvalues.get(values[1].trim());

			String[] arr = date1.split("-");
			String[] arr1 = date2.split("-");

			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			int day = Integer.parseInt(arr[2]);
			int year1 = Integer.parseInt(arr1[0]);
			int month1 = Integer.parseInt(arr1[1]);
			int day1 = Integer.parseInt(arr1[2]);

			int newYear = year - year1;
			int newMonth = month - month1;
			String monthdiff = String.valueOf(newMonth);
			if (monthdiff.contains("-")) {
				monthdiff = monthdiff.replace("-", "");
			}
			int newDay = day - day1;
			String daydiff = String.valueOf(newDay);
			if (daydiff.contains("-")) {
				daydiff = daydiff.replace("-", "");
			}

			symbol = newYear + "-" + monthdiff + "-" + daydiff;

		} else {

			showToast("Kindly enter all the fields");
		}

	}

	public String getUserName(String user) {
		if (user != null) {
			String[] substring = user.split(":");

			String[] substrings = substring[1].split("@");

			return substrings[0];
		} else

			return "";

	}

	public void putxmlobj(String key, Object obj) {
		if (xmlmap.containsKey(key)) {
			xmlmap.remove(key);
			xmlmap.put(key, obj);
		} else {
			xmlmap.put(key, obj);
		}
	}

	public void notifyCallDisconnected() {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					isreject = true;
					callDisp.SIPCallisAccepted = false;
					btn_rejcall.setBackgroundResource(R.drawable.reconnect);
					btn_rejcall.setTag("1");
				}
			});
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		Log.i("HEART", "Saved Instance state===>" + ComponentPath);
		outState.putString("componentpath", ComponentPath);

		super.onSaveInstanceState(outState);
	}

}
