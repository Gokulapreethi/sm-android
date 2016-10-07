
package com.cg.files;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lib.model.SignalingBean;
import org.util.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.Fingerprint.MainActivity;
import com.cg.DB.DBAccess;
import com.cg.account.PinSecurity;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.cg.snazmed.R.drawable;
import com.cg.account.ShareByProfile;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.TextNoteDatas;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.HandSketchActivity2;
import com.cg.commongui.MultimediaUtils;
import com.cg.instancemessage.IMNotifier;
import com.cg.instancemessage.NotePickerScreen;
import com.cg.quickaction.ShareNotePicker;
import com.cg.timer.ReminderService;
import com.crypto.AESFileCrypto;
import com.group.chat.SendListUIBean;
import com.image.utils.FileImageLoader;
import com.image.utils.ImageLoader;
import com.image.utils.ImageUtils;
import com.image.utils.MemoryCache;
import com.main.AppMainActivity;
import com.main.FilesFragment;
import com.util.CustomVideoCamera;
import com.util.FullScreenImage;
import com.util.SingleInstance;
import com.util.VideoPlayer;

public class ComponentCreator extends Activity implements IMNotifier {

    private Button btnaudio = null, btnVideo = null, btnImage = null,
            btnattach = null;
    private LinearLayout contentLayout1 = null, footerlayout = null,
            titLayout = null, commandlayout = null;
    private int noScrHeight;
    private boolean isVideo=false;
    EditText edNotes = null;
    private Context context = null;
    private String ComponentPath = null;
    private boolean ON_CREATE, forms = false;
    public boolean onCreateFile = false;
    private Handler StopAudioRecordingHandler = null, StopVideoHandler = null;
    private boolean isVideoPlay = false;
    private int videoPause = 0;
    private String parentID;
    boolean isreject = false;
    private int FROM_GALERY = 2, FROM_CAMERA = 3, AUDIO = 4, CAPTURE_VIDEO = 5,
            HAND_SKETCH = 6, GET_RESOURCES, TEXT = 1, FROMPROFILE = 8,
            DOCUMENT = 9, GALLERY_KITKAT_INTENT_CALLED = 7;
    private String[] m_type = {
            SingleInstance.mainContext.getResources()
                    .getString(R.string._text_),
            SingleInstance.mainContext.getResources().getString(R.string.photo),
            SingleInstance.mainContext.getResources().getString(
                    R.string._audio_),
            SingleInstance.mainContext.getResources().getString(R.string.video) };
    AlertDialog.Builder builder = null;
    private Button btnShare = null;
    private Button btnReminder = null;
    public Button btnrespond = null;
    private CompleteListBean cbean = null;
    private AlertDialog msg_dialog = null;
    private CallDispatcher callDisp = null;
    private String thumpnail = "";
    private TextView tv_remid = null;
    private TextView call_view = null;
    private String datas = null;
    private Button editTextNotes = null;
    private Button btnDone, btnIMRequest, btnBack;
    public TextView tvTitle;
	ImageView filePlusBtn;
    AlertDialog alert;
    // private BeatListAdapter adapter;
    private Handler viewHandler = new Handler();
    private String noteType = null;
    private VideoView videoView = null;
    private ProgressDialog dialog = null;
    private int noScrWidth;
    public RelativeLayout rlayout = null;
    private Button btn_notification;
    private Button btn_sketch;
    private TextView btn_comment = null;
    private boolean ishand_sketch = false;
    private ViewFlipper flipper = null;
    private Button btncomment_cancel = null;
    private Button btncomment_done = null;
    private EditText comment_editor = null;
    private Button btn_prev, btnaccept, btnreject = null;
    private Button btn_next = null;
    private Button btn_delete = null;
	private Boolean fromNewFile=false;
    String Comm = null;
    String strScreenType = null;
    private int position;
    final String[] choiceList = {
            SingleInstance.mainContext.getResources().getString(
                    R.string.send_to_a_contact),
            SingleInstance.mainContext.getResources().getString(
                    R.string.share_by_profile_use),
            SingleInstance.mainContext.getResources()
                    .getString(R.string.cancel) };
    ScrollView contentLayout;
    private ArrayList<String> uploadDatas = new ArrayList<String>();
    private String buddyname = null;
    private String from = "";
    public boolean send = false;
    private String strIPath = null;
    private boolean audioExist = false;
    private ImageView imgPic = null;
    private FilesFragment cList;
    private String product = null;
    private String fileName;
    private FilesFragment filesFragment = null;
    int fileListSize = 0;
    private FileImageLoader imageLoader;
    private CompleteListBean complBean = null;
    private boolean flag = false;
    private boolean response = false;
    private boolean isFocused = false;
	CallDispatcher calldisp;
	private ImageView newFileImg;
	EditText filename,fileDesc;
	TextView tv_file;
	ImageView file_img,overlay;

	private HashMap<String,Object> current_open_activity_detail = new HashMap<String,Object>();
	private boolean save_state = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			if (SingleInstance.mainContext
					.getResources()
					.getString(R.string.screenshot)
					.equalsIgnoreCase(
							SingleInstance.mainContext.getResources()
									.getString(R.string.yes))) {
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
						WindowManager.LayoutParams.FLAG_SECURE);
			}
			setContentView(R.layout.notescreationactivity);
			WebServiceReferences.contextTable.put("IM", this);
			context = this;
			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				calldisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				calldisp = new CallDispatcher(context);

			filesFragment = FilesFragment.newInstance(context);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			noScrHeight = displaymetrics.heightPixels;
			noScrWidth = displaymetrics.widthPixels;
			builder = new AlertDialog.Builder(context);
			cList = FilesFragment.newInstance(context);
			imageLoader = new FileImageLoader(context);
			if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
				strScreenType = "XLarge";
			} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
				strScreenType = "XLarge";
			} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
				strScreenType = "other";
			} else {
				strScreenType = "other";
			}
			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = CallDispatcher
						.getCallDispatcher(SingleInstance.mainContext);

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);
			displaymetrics = null;
			btnDone = (Button) findViewById(R.id.btn_save_note);
			filePlusBtn=(ImageView)findViewById(R.id.file_plus);
			newFileImg=(ImageView)findViewById(R.id.newfile);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_VERTICAL;
			btnDone.setVisibility(View.VISIBLE);
			newFileImg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(ComponentPath!=null){
						Log.i("group123", "icon clicked component "+ComponentPath);
						if (ComponentPath.endsWith(".jpg")) {
							Intent intent = new Intent(context, FullScreenImage.class);
							intent.putExtra("image", ComponentPath);
							context.startActivity(intent);
						}else if(ComponentPath.endsWith(".mp4")){
							Intent intent = new Intent(context, MultimediaUtils.class);
							intent.putExtra("filePath", ComponentPath);
							intent.putExtra("requestCode", AUDIO);
							intent.putExtra("action", "audio");
							intent.putExtra("createOrOpen", "open");
							startActivity(intent);
						} else {
							Intent intent = new Intent(context, VideoPlayer.class);
							intent.putExtra("video", ComponentPath+".mp4");
							context.startActivity(intent);
						}
					}
				}
			});

			btnBack = (Button) findViewById(R.id.btn_back);
			filename=(EditText)findViewById(R.id.ed_createfile);
			fileDesc=(EditText)findViewById(R.id.ed_filedesc);
			tv_file=(TextView)findViewById(R.id.tv_file);
			file_img=(ImageView)findViewById(R.id.file_img);
			overlay=(ImageView)findViewById(R.id.overlay);
			final TextView tv_filename=(TextView)findViewById(R.id.tv_filename);
			final TextView tv_fileDesc=(TextView)findViewById(R.id.tv_filedesc);
			btnBack.setBackgroundResource(drawable.navigation_close);
			btnBack.setText("");
			tvTitle = (TextView) findViewById(R.id.tv_note_title);
			tvTitle.setTextColor(Color.WHITE);
			tvTitle.setSingleLine();


			filename.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (s.length() > 0)
						tv_filename.setVisibility(View.VISIBLE);
					else
						tv_filename.setVisibility(View.GONE);
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			fileDesc.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(s.length()>0)
						tv_fileDesc.setVisibility(View.VISIBLE);
					else
						tv_fileDesc.setVisibility(View.GONE);
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});

			WebServiceReferences.contextTable.put("Component", this);
			WebServiceReferences.contextTable.put("a_play", this);
			final Bundle bndl = getIntent().getExtras();
			ON_CREATE = bndl.getBoolean("action");
			noteType = bndl.getString("type");
			forms = bndl.getBoolean("forms");
			position = bndl.getInt("position");
			buddyname = bndl.getString("buddyname");
			parentID = bndl.getString("parentid");
			from = bndl.getString("from");
			product = bndl.getString("produ");
			response = bndl.getBoolean("response", false);
			fromNewFile = bndl.getBoolean("fromNew", false);
			complBean = (CompleteListBean) getIntent().getExtras()
					.getSerializable("viewBean");

			current_open_activity_detail.put("action",ON_CREATE);
			current_open_activity_detail.put("type",noteType);
			current_open_activity_detail.put("forms",forms);
			current_open_activity_detail.put("position",position);
			current_open_activity_detail.put("buddyname",buddyname);
			current_open_activity_detail.put("parentid",parentID);
			current_open_activity_detail.put("from",from);
			current_open_activity_detail.put("produ",product);
			current_open_activity_detail.put("response",response);
			current_open_activity_detail.put("fromNew",fromNewFile);
			current_open_activity_detail.put("viewBean",complBean);


			if(!fromNewFile){
				if(complBean!=null){
					if(complBean.getContentpath()!=null)
						ComponentPath=complBean.getContentpath();
					if(complBean.getContentName()!=null)
						filename.setText(complBean.getContentName());
					if(complBean.getContent()!=null)
						fileDesc.setText(complBean.getContent());
					tv_file.setVisibility(View.INVISIBLE);
					file_img.setVisibility(View.INVISIBLE);
					if (complBean.getcomponentType().trim().equals("audio")) {
						imageLoader.DisplayImage(complBean.getContentpath().replace(".mp4", ".jpg"), newFileImg, R.drawable.audionotesnew);
					} else if (complBean.getcomponentType().trim().equals("video")) {
						imageLoader.DisplayImage(complBean.getContentpath() + ".mp4",newFileImg, R.drawable.videonotesnew);
					} else if (complBean.getcomponentType().trim().equals("photo")) {
						imageLoader.DisplayImage(complBean.getContentpath(), newFileImg, R.drawable.photonotesnew);
					} else if (complBean.getcomponentType().trim().equalsIgnoreCase("document")) {
						newFileImg.setBackgroundResource(R.drawable.attachfile);
					}
				}
			}
			filePlusBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.roundeditmanagement);
					WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
					lp.copyFrom(dialog.getWindow().getAttributes());
					lp.width = WindowManager.LayoutParams.MATCH_PARENT;
					lp.height = WindowManager.LayoutParams.MATCH_PARENT;
					lp.horizontalMargin = 15;
					Window window = dialog.getWindow();
					window.setBackgroundDrawableResource((R.color.lblack));
					window.setAttributes(lp);
					window.setGravity(Gravity.BOTTOM);
					dialog.show();
					TextView photo = (TextView) dialog.findViewById(R.id.round_edit);
					TextView video = (TextView) dialog.findViewById(R.id.round_role);
					TextView gallery = (TextView) dialog.findViewById(R.id.roun_new_task);
					TextView audio = (TextView) dialog.findViewById(R.id.roun_new_patient);
					TextView document = (TextView) dialog.findViewById(R.id.roun_ownership);
					TextView deletePatient = (TextView) dialog.findViewById(R.id.roun_delete);
					TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
					photo.setText("Take Photo");
					gallery.setText("Go to gallery");
					video.setText("Take Video");
					deletePatient.setVisibility(View.GONE);
					audio.setText("Record Audio File");
					document.setText("Upload document");
					document.setBackgroundColor(getResources().getColor(R.color.blue2));
					cancel.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							dialog.dismiss();
						}
					});
					gallery.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							openGallery();
							dialog.dismiss();
						}
					});
					document.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
								try {
									Intent intent = new Intent(context, FilePicker.class);
									startActivityForResult(intent, 38);
									GET_RESOURCES = DOCUMENT;
								} catch (Exception e) {
									e.printStackTrace();
								}
							dialog.dismiss();
						}
					});
					audio.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(CallDispatcher.isCallInitiate) {
								showToast("Call is in progress, Please try later");
							} else {
								openAudio();
							}
							dialog.dismiss();
						}
					});
					photo.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							if (CallDispatcher.isCallInitiate) {
								showToast("Call is in progress, Please try later");
							} else
								openCamera("photo");
						}
					});
				video.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (CallDispatcher.isCallInitiate) {
							showToast("Call is in progress, Please try later");
						} else
						openCamera("video");
					}
				});
				}
			});
			if (from == null)
				from = "";
			send = bndl.getBoolean("send");

			current_open_activity_detail.put("send",send);
			CompleteListView.checkDir();

			contentLayout = (ScrollView) findViewById(R.id.footerss);
			contentLayout1 = (LinearLayout) findViewById(R.id.footer);
			footerlayout = (LinearLayout) findViewById(R.id.footer2);
			LinearLayout parent_flip = (LinearLayout) findViewById(R.id.parent_flip);
			LinearLayout newfile_lay = (LinearLayout) findViewById(R.id.newfile_lay);
			commandlayout = (LinearLayout) findViewById(R.id.footer3);
			titLayout = (LinearLayout) findViewById(R.id.footer1);
			btnaudio = (Button) findViewById(R.id.btnAudio);
			btnImage = (Button) findViewById(R.id.btnPhoto);
			btnVideo = (Button) findViewById(R.id.btnVideo);
			btn_sketch = (Button) findViewById(R.id.btnsketch);
			btnattach = (Button) findViewById(R.id.btnattach);
			btnShare = (Button) findViewById(R.id.btnshare);
			btn_comment = (TextView) findViewById(R.id.btncommnet);
			flipper = (ViewFlipper) findViewById(R.id.lay_flipper);
			btncomment_cancel = (Button) findViewById(R.id.btncomment_cancel);
			btncomment_done = (Button) findViewById(R.id.btncomment_done);
			comment_editor = (EditText) findViewById(R.id.comment_editor);
			btnReminder = (Button) findViewById(R.id.btnreminder);
			btnrespond = (Button) findViewById(R.id.btnrespond);
			btn_prev = (Button) findViewById(R.id.btnprev);
			btn_next = (Button) findViewById(R.id.btnnext);
			btn_delete = (Button) findViewById(R.id.btndelete);
			btnrespond.setVisibility(View.GONE);
			commandlayout.setVisibility(View.GONE);
			footerlayout.setVisibility(View.GONE);
			if (CallDispatcher.LoginUser == null) {
				btn_delete.setVisibility(View.GONE);
				// btnReminder.setVisibility(View.GONE);
				btnrespond.setVisibility(View.GONE);
				btnShare.setVisibility(View.GONE);
			} else {
				btnShare.setVisibility(View.VISIBLE);
			}
			if (filesFragment.filesAdapter != null) {
				fileListSize = filesFragment.filesAdapter.getCount();
			}
			if (ON_CREATE) {

				showNoteView(noteType);
				if (product != null) {
					edNotes.setText(product.replace("[", "").replace("]", ""));
				}

				/*
				 * 
				 * if
				 * (WebServiceReferences.contextTable.containsKey("notepicker"))
				 * { if (noteType != null) { showNoteView(noteType);
				 * Log.i("log", "inside notetype"); } } else if
				 * (WebServiceReferences.contextTable
				 * .containsKey("frmreccreator")) { if (noteType != null) {
				 * showNoteView(noteType); Log.i("log", "inside notetype"); } }
				 * else if (WebServiceReferences.contextTable
				 * .containsKey("sharenotepicker")) {
				 * 
				 * if (noteType != null) { showNoteView(noteType);
				 * Log.i("notepicker", "inside share notetype"); } } else { if
				 * (noteType != null) { showNoteView(noteType); } }
				 */

			} else {
				Log.i("log", "inside else opennote");
				if (WebServiceReferences.contextTable
						.containsKey("utilitybuyer")
						|| WebServiceReferences.contextTable
								.containsKey("utilityseller")
						|| WebServiceReferences.contextTable
								.containsKey("utilityneeder")
						|| WebServiceReferences.contextTable
								.containsKey("utilityprovider")) {
					if (noteType != null) {
						String path = getIntent().getStringExtra("audio_path");
						Log.d("utility", "---->" + path);
						contentLayout.addView(AudioNoteView(3, path));
					}
				} else {
					CompleteListBean compBean = (CompleteListBean) bndl
							.getSerializable("viewBean");
					if (compBean != null) {
						callDisp.cmp = compBean;
						openNote();
					}
				}
			}

			btn_sketch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					btnDone.setVisibility(View.GONE);
					showNoteView("handsketch");

				}
			});
			btnattach.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					btnattach.setEnabled(false);
					viewHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							btnattach.setEnabled(true);

						}
					}, 1000);

					openFolder();
				}
			});
			btn_comment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub

						if (!ON_CREATE) {
							cbean = callDisp.cmp;
						} else {
							if (cbean == null) {

								Log.i("log", "cbean is null");

								if (ComponentPath != null) {
									if (GET_RESOURCES == CAPTURE_VIDEO) {
										File vfile = new File(ComponentPath
												+ ".mp4");
										File vfile1 = new File(ComponentPath
												+ ".jpg");

										if (vfile.exists() && vfile1.exists()) {
											cbean = DBAccess
													.getdbHeler(context)
													.putDBEntry(
															GET_RESOURCES,
															ComponentPath,
															WebServiceReferences
																	.getNoteCreateTimeForFiles(),
															tvTitle.getText()
																	.toString()
																	.trim(),
															complBean,"");

											if (cbean != null) {
												ComponentPath = null;
//												commandlayout
//														.setVisibility(View.VISIBLE);
//
//												footerlayout
//														.setVisibility(View.VISIBLE);

												btnDone.setEnabled(false);

											}
										} else {
											showToast(SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.pls_create_note));
										}

									} else {

										File fle = new File(ComponentPath);
										if (fle.exists()) {
											cbean = DBAccess
													.getdbHeler(context)
													.putDBEntry(
															GET_RESOURCES,
															ComponentPath,
															WebServiceReferences
																	.getNoteCreateTimeForFiles(),
															tvTitle.getText()
																	.toString()
																	.trim(),
															complBean,"");
											if (cbean != null) {
												ComponentPath = null;
//												commandlayout
//														.setVisibility(View.VISIBLE);
//
//												footerlayout
//														.setVisibility(View.VISIBLE);

												btnDone.setEnabled(false);
											}
										}

									}

								}

							}
							if (cbean.getContent() != null) {
								Log.i("log", cbean.getContent());
								comment_editor.setText(cbean.getContent());
								btn_comment.setText(cbean.getContent());
							} else {
								comment_editor.setText("");

							}

						}

						commandlayout.setVisibility(View.GONE);
						footerlayout.setVisibility(View.GONE);
						btnBack.setVisibility(View.INVISIBLE);
						btnDone.setVisibility(View.GONE);
						flipper.showNext();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

			btncomment_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

//					commandlayout.setVisibility(View.VISIBLE);
//					footerlayout.setVisibility(View.VISIBLE);
					btnBack.setVisibility(View.VISIBLE);

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							comment_editor.getWindowToken(), 0);
					getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
					flipper.showPrevious();

				}
			});

			btncomment_done.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (comment_editor.getText().toString().trim().length() != 0) {
						Comm = comment_editor.getText().toString().trim();
					} else {
						Comm = "";
					}

					if (cbean.getContent() == null) {
						String strQuery = "update component set comment='"
								+ Comm + "' where componentid="
								+ cbean.getComponentId();
						Log.i("log", "" + "if part" + strQuery);
						if (callDisp.getdbHeler(context).ExecuteQuery(strQuery)) {
//							commandlayout.setVisibility(View.VISIBLE);
//							footerlayout.setVisibility(View.VISIBLE);
							btnBack.setVisibility(View.VISIBLE);
							// callDisp.cmp.setContent(Comm);
							cbean.setContent(Comm);

							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									comment_editor.getWindowToken(), 0);
							getWindow()
									.setSoftInputMode(
											WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

							flipper.showPrevious();
							btn_comment.setText(cbean.getContent());

						}

					} else {

						if (!cbean.getContent().equalsIgnoreCase(
								comment_editor.getText().toString().trim())) {
							String strQuery = "update component set comment='"
									+ Comm + "' where componentid="
									+ cbean.getComponentId();
							Log.i("log", "notnull" + strQuery);

							if (callDisp.getdbHeler(context).ExecuteQuery(
									strQuery)) {
//								commandlayout.setVisibility(View.VISIBLE);
//								footerlayout.setVisibility(View.VISIBLE);
								btnBack.setVisibility(View.VISIBLE);
								callDisp.cmp.setContent(Comm);
								cbean.setContent(Comm);

								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										comment_editor.getWindowToken(), 0);
								getWindow()
										.setSoftInputMode(
												WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

								flipper.showPrevious();
								btn_comment.setText(cbean.getContent());

							}
						} else {
//							commandlayout.setVisibility(View.VISIBLE);
//							footerlayout.setVisibility(View.VISIBLE);
							btnBack.setVisibility(View.VISIBLE);
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									comment_editor.getWindowToken(), 0);
							getWindow()
									.setSoftInputMode(
											WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
							flipper.showPrevious();
							btn_comment.setText(cbean.getContent());

						}
					}

				}
			});

			btnaudio.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					showNoteView("audio");
				}
			});
			btnImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					btnDone.setVisibility(View.GONE);
					showNoteView("photo");
				}
			});
			btnVideo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					btnDone.setVisibility(View.GONE);
					showNoteView("video");
				}
			});
			btnrespond.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (CallDispatcher.LoginUser != null) {

						if (complBean.getParent_id() != null) {
							sendResponse(true);
						} else {
							sendResponse(false);
						}

					} else {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.kindly_login_to_send));
					}
				}
			});

			if (position == 0) {
				btn_prev.setVisibility(View.GONE);
			}

			if (position == fileListSize - 1) {
				btn_next.setVisibility(View.GONE);
			}

			// if (adapter.getCount() == 1) {
			// btn_next.setVisibility(View.GONE);
			// btn_prev.setVisibility(View.GONE);
			// }

			btn_prev.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub

						position = position - 1;
						if (position <= 0) {
							btn_prev.setVisibility(View.GONE);
						} else {
							btn_prev.setVisibility(View.VISIBLE);
						}
						btn_next.setVisibility(View.VISIBLE);
						CompleteListBean cBean = filesFragment.filesAdapter
								.getItem(position);
						callDisp.cmp = cBean;
						if (cBean.getViewmode() == 0) {
							String strUpdateNote = "update component set viewmode='"
									+ 1
									+ "' where componentid='"
									+ cBean.getComponentId() + "'";
							Log.d("qry", strUpdateNote);

							if (DBAccess.getdbHeler().ExecuteQuery(
									strUpdateNote)) {
								cBean.setViewMode(1);
								// filesAdapter.changeReadStatus();
								// filesAdapter.notifyDataSetChanged();
							}
						}
						openNote();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			btn_next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub

						position = position + 1;
						Log.i("comp", "===>" + position);

						if (cList.checkPosition(position)) {
							btn_next.setVisibility(View.GONE);
						} else {
							btn_next.setVisibility(View.VISIBLE);
						}
						btn_prev.setVisibility(View.VISIBLE);

						CompleteListBean cBean = filesFragment.filesAdapter
								.getItem(position);
						callDisp.cmp = cBean;
						if (cBean.getViewmode() == 0) {
							String strUpdateNote = "update component set viewmode='"
									+ 1
									+ "' where componentid='"
									+ cBean.getComponentId() + "'";
							Log.d("qry", strUpdateNote);

							if (DBAccess.getdbHeler().ExecuteQuery(
									strUpdateNote)) {
								cBean.setViewMode(1);
								// filesAdapter.changeReadStatus();
								// filesAdapter.notifyDataSetChanged();
							}
						}
						openNote();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

			btn_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
						filesFragment.deleteNote(cbean, context);
				}
			});

			btnShare.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (noteType.equalsIgnoreCase("note")
							&& edNotes.getText().toString().length() > 0
							&& !ON_CREATE && (!isFocused)) {
						showSingleSelectBuddy(choiceList, "");
					} else if (!(btnDone.getVisibility() == View.VISIBLE)) {
						showSingleSelectBuddy(choiceList, "");
					} else {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.kindly_save_before_share));

					}

				}
			});

			btnReminder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (cbean != null) {
						if (noteType.equalsIgnoreCase("note")
								&& edNotes.getText().toString().length() > 0
								&& !ON_CREATE && (!isFocused)) {
							showReminder();
						} else if (btnDone.getVisibility() != View.VISIBLE) {
							showReminder();
						} else {
							showToast("Kindly save before setting reminder");
						}
					} else {
						if (ComponentPath != null) {
							if (GET_RESOURCES == CAPTURE_VIDEO) {
								File vfile = new File(ComponentPath + ".mp4");
								File vfile1 = new File(ComponentPath + ".jpg");
								if (vfile.exists() && vfile1.exists()) {
									cbean = DBAccess
											.getdbHeler(context)
											.putDBEntry(
													GET_RESOURCES,
													ComponentPath,
													WebServiceReferences
															.getNoteCreateTimeForFiles(),
													tvTitle.getText()
															.toString().trim(),
													complBean,"");
									if (cbean != null) {
										ComponentPath = null;

//										commandlayout
//												.setVisibility(View.VISIBLE);
//
//										footerlayout
//												.setVisibility(View.VISIBLE);

										btnDone.setEnabled(false);
										showReminder();
									}
								} else {
									showToast(SingleInstance.mainContext
											.getResources().getString(
													R.string.pls_create_note));
								}

							} else {

								File fle = new File(ComponentPath);
								if (fle.exists()) {
									cbean = DBAccess
											.getdbHeler(context)
											.putDBEntry(
													GET_RESOURCES,
													ComponentPath,
													WebServiceReferences
															.getNoteCreateTimeForFiles(),
													tvTitle.getText()
															.toString().trim(),
													complBean,"");
									if (cbean != null) {
										ComponentPath = null;
//										commandlayout
//												.setVisibility(View.VISIBLE);
//
//										footerlayout
//												.setVisibility(View.VISIBLE);

										btnDone.setEnabled(false);
										if (cbean.getcomponentType().equals(
												"audio")) {

										}

										showReminder();
									}
								}
							}
						}

					}

				}
			});

			tvTitle.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					if (!ON_CREATE) {
						if (!callDisp.cmp.getcomponentType().equals("call")) {
							SetViewTitle(tvTitle, cbean);
						}
					} else {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.kindly_save_before_rename));
					}
					return false;
				}
			});

			btnDone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// TODO Auto-generated method stub
					if (from != null) {
						if (from.equalsIgnoreCase("utility")
								|| from.equalsIgnoreCase("forms")) {
							setActivityResult();
						} else {
							saveFile();
							// filesFragment.filesAdapter.notifyDataSetChanged();
							// Log.i("list","---save file1--"+filesFragment.listView.getCount());
						}
					} else {
						saveFile();
						// filesFragment.filesAdapter.notifyDataSetChanged();
						// Log.i("list","---save file1--"+filesFragment.listView.getCount());
					}

				}

			});

			btnBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						Log.i("files", "comp path :: " + ComponentPath);
						Log.i("files", "oncreate :: " + ON_CREATE);
						if (ON_CREATE) {
							if (GET_RESOURCES == TEXT
									&& edNotes.getText().toString() != null
									&& edNotes.getText().toString().length() > 0)
								showAlert();
							else if (GET_RESOURCES == AUDIO && audioExist) {
								showAlert();
							} else if ((GET_RESOURCES == CAPTURE_VIDEO
									|| GET_RESOURCES == FROM_CAMERA
									|| GET_RESOURCES == FROM_GALERY || GET_RESOURCES == GALLERY_KITKAT_INTENT_CALLED)
									&& ComponentPath != null
									&& ComponentPath.length() > 0)
								showAlert();
							else {
								refreshList();
							}
						} else
							refreshList();

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


    private void refreshList() {
        finish();
    }

	private void doshare(String userid) {
		try {
			// TODO Auto-generated method stub
			// if (!send)
			// cbean = callDisp.cmp;

			if (cbean != null) {
				if (CallDispatcher.LoginUser != null) {
					if (callDisp.getmyBuddys().length > 0) {
						Intent shareintent = new Intent(context,
								sendershare.class);
						shareintent
								.putExtra("filename", cbean.getContentpath());
						if (cbean.getcomponentType().equalsIgnoreCase("sketch")) {
							shareintent.putExtra("compname", "handsketch");
						} else {
							shareintent.putExtra("compname",
									cbean.getcomponentType());
						}
						shareintent.putExtra("username", userid);
						shareintent.putExtra("comments", cbean.getContent());
						Log.i("comment", "---------" + cbean.getContent());
						startActivity(shareintent);
					} else {
						Toast.makeText(
								context,
								SingleInstance.mainContext.getResources()
										.getString(
												R.string.you_don_have_buddies),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(
							context,
							SingleInstance.mainContext.getResources()
									.getString(
											R.string.kindly_login_before_share),
							Toast.LENGTH_SHORT).show();
				}
			} else {

				if (ComponentPath != null) {
					if (GET_RESOURCES == CAPTURE_VIDEO) {
						File vfile = new File(ComponentPath + ".mp4");
						File vfile1 = new File(ComponentPath + ".jpg");
						if (vfile.exists() && vfile1.exists()) {
							cbean = DBAccess.getdbHeler(context).putDBEntry(
									GET_RESOURCES,
									ComponentPath,
									WebServiceReferences
											.getNoteCreateTimeForFiles(),
									tvTitle.getText().toString().trim(),
									complBean,"");
							if (cbean != null) {
								ComponentPath = null;
//								commandlayout.setVisibility(View.VISIBLE);

//								footerlayout.setVisibility(View.VISIBLE);

								btnDone.setEnabled(false);
								if (CallDispatcher.LoginUser != null) {
									Intent shareintent = new Intent(
											ComponentCreator.this,
											sendershare.class);
									Log.e("act",
											"***********"
													+ cbean.getcomponentType());
									Log.e("act",
											"**************"
													+ cbean.getContentpath());
									shareintent.putExtra("filename",
											cbean.getContentpath());
									if (cbean.getcomponentType()
											.equalsIgnoreCase("sketch")) {
										shareintent.putExtra("compname",
												"photo");
									} else {
										shareintent.putExtra("compname",
												cbean.getcomponentType());
									}
									shareintent.putExtra("username", userid);
									startActivity(shareintent);
								} else {
									Toast.makeText(
											context,
											SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.kindly_login_before_share),
											Toast.LENGTH_SHORT).show();
								}

							}
						} else {
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.pls_create_note));
						}

					} else {

						File fle = new File(ComponentPath);
						if (fle.exists()) {
							cbean = DBAccess.getdbHeler(context).putDBEntry(
									GET_RESOURCES,
									ComponentPath,
									WebServiceReferences
											.getNoteCreateTimeForFiles(),
									tvTitle.getText().toString().trim(),
									complBean,"");
							if (cbean != null) {
								ComponentPath = null;

								btnDone.setEnabled(false);
								if (cbean.getcomponentType().equals("audio"))

								{

								} else {

								}
								if (callDisp.LoginUser != null) {
									Intent shareintent = new Intent(
											ComponentCreator.this,
											sendershare.class);

									shareintent.putExtra("filename",
											cbean.getContentpath());

									if (cbean.getcomponentType()
											.equalsIgnoreCase("sketch")) {
										shareintent.putExtra("compname",
												"handsketch");
									} else {
										shareintent.putExtra("compname",
												cbean.getcomponentType());
									}
									shareintent.putExtra("username", userid);
									startActivity(shareintent);
								} else {
									Toast.makeText(
											context,
											SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.kindly_login_before_share),
											Toast.LENGTH_SHORT).show();
								}
							}
						}

					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void showSingleSelectBuddy(final String[] choiceList, final String edit) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.create();
			int selected = -1; // does not select anything

			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (choiceList[which]
									.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(
					R.string.send_to_a_contact))) {
								doshare("No");

							} else if (choiceList[which]
									.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(
											R.string.share_by_profile_use))) {
								if (CallDispatcher.LoginUser != null) {
									Intent intent = new Intent(context,
											ShareByProfile.class);
									intent.putExtra("activity", "note");

									startActivityForResult(intent, FROMPROFILE);
								} else
									showToast(SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.kindly_login_before_share));

							} else if (choiceList[which]
									.equalsIgnoreCase("Cancel")) {
								alert.dismiss();

							}

							alert.dismiss();
						}
					});
			alert = builder.create();
			if (choiceList != null) {
				if (choiceList.length != 0) {
					alert.show();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	SharedPreferences preference;

	private void sendResponse(boolean note) {
		try {
			if (cbean != null) {

				Components component = callDisp.getdbHeler(context)
						.getComponent(
								"select * from component where componentid="
										+ cbean.getComponentId());
				int position;
				if (cbean.getReminderResponseType().equalsIgnoreCase("Note")) {

					Log.i("123", "-----position 0----");
					position = 0;
					responseType(component, position);
				} else if (cbean.getReminderResponseType().equalsIgnoreCase(
						"Image")) {
					position = 1;
					responseType(component, position);
				} else if (cbean.getReminderResponseType().equalsIgnoreCase(
						"Audio")) {
					position = 2;
					responseType(component, position);
				} else if (cbean.getReminderResponseType().equalsIgnoreCase(
						"Video")) {
					position = 3;
					responseType(component, position);
				} else {
					Log.i("123", "-----else----");
					showResponseDialog(component);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showResponseDialog(final Components component) {
		try {
			AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
			alert_builder.setTitle(SingleInstance.mainContext.getResources()
					.getString(R.string.select_response_type));
			alert_builder.setSingleChoiceItems(m_type, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int pos) {
							// TODO Auto-generated method stub
							responseType(component, pos);
							dialog.dismiss();
						}
					});
			msg_dialog = alert_builder.create();
			msg_dialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createTextNote() {
		if (ON_CREATE) {
			ComponentPath = "/sdcard/COMMedia/"
					+ CompleteListView.getFileName() + ".txt";
		} else {
			File file = new File(callDisp.cmp.getContentpath());
			if (file.exists()) {
				file.delete();
			}
			ComponentPath = callDisp.cmp.getContentpath();
		}
		strIPath = ComponentPath;
		if (CompleteListView.textnotes == null)
			CompleteListView.textnotes = new TextNoteDatas();

		CompleteListView.textnotes.checkAndCreate(ComponentPath, edNotes
				.getText().toString());
	}

    private void saveFile() {
        try {
            onCreateFile=true;
            if (GET_RESOURCES == TEXT) {

				if (edNotes.getText().toString().trim().length() != 0) {
					createTextNote();
					if (ON_CREATE) {

						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(edNotes.getWindowToken(), 0);
						getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
						createTextNote();

						cbean = DBAccess.getdbHeler(context).putDBEntry(
								GET_RESOURCES,
								ComponentPath,
								WebServiceReferences
										.getNoteCreateTimeForFiles(),
								tvTitle.getText().toString().trim()
										.replaceAll("\n", ""), complBean,"");
						if (SingleInstance.ContactSharng || parentID != null) {
							SingleInstance.ContactSharng = false;
							complBean = cbean;
							complBean.setBsstatus("Placed");
							complBean.setBstype("Order");
							complBean.setDirection("In");
							complBean.setFromUser(CallDispatcher.LoginUser);
							complBean.setTouser(buddyname);

							if (parentID != null) {
								complBean.setParent_id(parentID);
							} else {
								complBean.setParent_id(Utility.getSessionID());
							}
						}
						if (WebServiceReferences.contextTable
								.containsKey("sharenotepicker")) {
							((ShareNotePicker) WebServiceReferences.contextTable
									.get("sharenotepicker")).refreshList();
							finish();
						}
						if (WebServiceReferences.contextTable
								.containsKey("notepicker")) {
							((NotePickerScreen) WebServiceReferences.contextTable
									.get("notepicker")).refreshList();
							finish();
						}
						if (cbean != null) {
							// if (send)
							// showToast("Text note sent succesfully");
							// else
							if (!send)
								showToast(SingleInstance.mainContext
										.getResources()
										.getString(
												R.string.text_note_saved_succesfully));
							ON_CREATE = false;
							callDisp.cmp = cbean;
							openNote();
						}

					} else {
						datas = edNotes.getText().toString().trim();
						showToast(SingleInstance.mainContext
								.getResources()
								.getString(R.string.text_note_saved_succesfully));

					}
//					btnDone.setVisibility(View.GONE);
					// if (!(GET_RESOURCES == TEXT)){
					// btnDone.setEnabled(false);
					// }
				} else {
					// callDisp.showAlert("",
					// SingleInstance.mainContext.getResources()
					// .getString(R.string.cannot_save_empty_file));
					showAlert1("Please enter some content to save. Cannot save empty file");
					return;
				}

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edNotes.getWindowToken(), 0);
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

			} else {

				Log.e("NS", "ComponentPath :" + ComponentPath);
				if(filename.getText().toString()!=null
						&& filename.getText().toString().trim().length()>0 && fileDesc.getText().toString()!=null
						&& fileDesc.getText().toString().trim().length()>0) {

					if (ComponentPath != null) {
						strIPath = ComponentPath;
						if (GET_RESOURCES == CAPTURE_VIDEO) {
							File vfile = new File(ComponentPath + ".mp4");
							File vfile1 = new File(ComponentPath + ".jpg");
							if (vfile.exists() && vfile1.exists()) {
//								complBean.setComment(fileDesc.getText().toString().trim());
								cbean = DBAccess.getdbHeler(context).putDBEntry(
										GET_RESOURCES,
										ComponentPath,
										WebServiceReferences
												.getNoteCreateTimeForFiles(),
										filename.getText().toString().trim(),
										complBean,fileDesc.getText().toString().trim());
								if (SingleInstance.ContactSharng
										|| parentID != null) {
									SingleInstance.ContactSharng = false;
									complBean = cbean;
									complBean.setBsstatus("Received");
									complBean.setBstype("Order");
									complBean.setDirection("In");
									complBean.setFromUser(buddyname);
									if (parentID != null) {
										complBean.setParent_id(parentID);
									} else {
										complBean.setParent_id(Utility
												.getSessionID());
									}
								}

								if (cbean != null) {
									// if (send)
									// showToast("Video note sent succesfully");
									if (!send)
										showToast(SingleInstance.mainContext
												.getResources()
												.getString(
														R.string.video_note_saved_succesfully));
									ON_CREATE = false;
									callDisp.cmp = cbean;
									openNote();

								}

							} else {
								showToast(SingleInstance.mainContext.getResources()
										.getString(R.string.pls_create_note));
							}

						} else {
							File fle = new File(ComponentPath);
							if (fle.exists()) {
//								complBean.setComment(fileDesc.getText().toString().trim());
								cbean = DBAccess.getdbHeler(context).putDBEntry(
										GET_RESOURCES,
										ComponentPath,
										WebServiceReferences
												.getNoteCreateTimeForFiles(),
										filename.getText().toString().trim(),
										complBean,fileDesc.getText().toString().trim());
								Log.i("getresource","value"+GET_RESOURCES);
								if (SingleInstance.ContactSharng
										|| parentID != null) {
									SingleInstance.ContactSharng = false;
									complBean = cbean;
									complBean.setBsstatus("Received");
									complBean.setBstype("Order");
									complBean.setDirection("In");
									complBean.setFromUser(buddyname);
									if (parentID != null) {
										complBean.setParent_id(parentID);
									} else {
										complBean.setParent_id(Utility
												.getSessionID());
									}
								}

								if (cbean != null) {
									if (GET_RESOURCES == AUDIO) {
										// if (send)
										// showToast("Audio note sent succesfully");
										if (!send) {
											ON_CREATE = false;
											callDisp.cmp = cbean;
											openNote();
											showToast(SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.audio_note_saved_succesfully));
										}
									} else {
										// if (send)
										// showToast("Image note sent succesfully");
										if (!send) {
											ON_CREATE = false;
											callDisp.cmp = cbean;
											Log.i("getresources","valueof___________>");
											openNote();
											showToast(SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.image_note_saved_succesfully));
										}
									}
								}

							} else {
								showToast(SingleInstance.mainContext.getResources()
										.getString(R.string.pls_create_note));
							}
						}
					} else {
						callDisp.showAlert(SingleInstance.mainContext
										.getResources().getString(R.string.warning),
								SingleInstance.mainContext.getResources()
										.getString(R.string.cannot_save_empty_file));
					}
				}else {
					showToast("Please enter filename and description");
					return;
				}

				if (WebServiceReferences.contextTable
						.containsKey("sharenotepicker")) {
					((ShareNotePicker) WebServiceReferences.contextTable
							.get("sharenotepicker")).refreshList();
					finish();
				}
				if (WebServiceReferences.contextTable.containsKey("notepicker")) {
					((NotePickerScreen) WebServiceReferences.contextTable
							.get("notepicker")).refreshList();
					finish();
				}
//				btnDone.setVisibility(View.GONE);
			}
			if (send)
				finishActivitys();
			else {
//				if (edNotes.getText().toString().trim().length() == 0)
//					editTextNotes.setVisibility(View.GONE);
//				else
//					editTextNotes.setVisibility(View.VISIBLE);
			}
            if(cbean!=null) {
                String type = cbean.getcomponentType();
                if (GET_RESOURCES == CAPTURE_VIDEO) {
                    if (!ComponentPath.endsWith(".mp4")) {
                        ComponentPath = ComponentPath + ".mp4";
                    }
                }
                if(type == "sketch"){
                    type = "handsketch";
                }
                if(AESFileCrypto.encryptFile(ComponentPath)) {
//                    callDisp.sendshare(type, ComponentPath, cbean);
                }
            }
            filesFragment.filesListRefresh();
			finish();
//			WebServiceReferences.webServiceClient.FileUpdates(
//					CallDispatcher.LoginUser, ComponentPath);
            Log.i("list",
                    "---save fuction--" + filesFragment.listView.getCount()
                            + "=======" + filesFragment.getCount);
        } catch (OutOfMemoryError oe) {			oe.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

	private void openNote() {
		try {
			rlayout = new RelativeLayout(context);
//			editTextNotes.setVisibility(View.VISIBLE);
//			if (!send)
//				editTextNotes.setTag(position);

//			btnDone.setVisibility(View.GONE);
			if (!callDisp.cmp.getcomponentType().equals("note")) {
				if (rlayout != null) {
					rlayout.setVisibility(View.VISIBLE);
				}
			}

			titLayout.removeAllViews();

			rlayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			rlayout.setBackgroundColor(Color.WHITE);
			TextView tv_from = new TextView(context);
			tv_from.setId(1);
			TextView note_title = new TextView(context);
			tv_remid = new TextView(context);
			if (complBean != null && complBean.getParent_id() == null) {

			} else {
				tv_remid.setVisibility(View.GONE);
			}
			tv_from.setHeight(50);
			note_title.setHeight(50);
			tv_remid.setHeight(50);

			RelativeLayout.LayoutParams layLeft = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			layLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

			RelativeLayout.LayoutParams layCenter = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			layCenter.addRule(RelativeLayout.CENTER_IN_PARENT);

			RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			RelativeLayout.LayoutParams layoutEdit = new RelativeLayout.LayoutParams(
					30, 30);
			layoutEdit.setMargins(3, 3, 0, 0);
			layoutEdit.addRule(RelativeLayout.RIGHT_OF, tv_from.getId());
			if (callDisp == null) {
				SingleInstance.getLogger().debug("Calldispatcher is NULL");
				Log.i("files123", "calldisp : " + callDisp);
				callDisp = CallDispatcher
						.getCallDispatcher(SingleInstance.mainContext);
			}
			cbean = callDisp.cmp;
			Log.i("getResources","componenttype"+cbean.getcomponentType());
			Log.i("getResources","cmponenetid"+cbean.getComponentId());
			Log.i("getResources","content"+cbean.getContent());
			Log.i("getResources","contentpath"+cbean.getContentpath());

			if (cbean.getContent() != null) {
				btn_comment.setText(cbean.getContent());
				Log.i("cmt", "-----open note" + cbean.getContent());
			} else {
				btn_comment.setText("add comment");
				Log.i("cmt", "-----open note");
			}

			if (callDisp.cmp.getFromUser().trim().length() != 0) {
				tv_from.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.from)
						+ " :"
						+ callDisp.cmp.getFromUser());
				btnrespond.setVisibility(View.VISIBLE);
			} else {
				tv_from.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.from_self));
				btnrespond.setVisibility(View.GONE);

			}
			tv_from.setTextColor(Color.BLACK);
			tv_from.setGravity(Gravity.LEFT);

			if (callDisp.cmp.getReminderTime() != null) {
				Log.e("tag", "Condition Satisfied.... not null");
				if (callDisp.cmp.getReminderTime().trim().length() == 0) {
					tv_remid.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.not_yet_set));
					tv_remid.setTextColor(Color.BLACK);
				} else {
					if (callDisp.cmp.getReminderTime().equalsIgnoreCase("null")) {
						tv_remid.setText(SingleInstance.mainContext
								.getResources().getString(R.string.not_yet_set));
						tv_remid.setTextColor(Color.BLACK);
					} else {
						tv_remid.setText(SingleInstance.mainContext
								.getResources().getString(R.string.reminder)
								+ " :" + callDisp.cmp.getReminderTime());
						tv_remid.setTextColor(Color.BLACK);
					}
					try {
						if (CheckReminderIsValid(callDisp.cmp.getReminderTime())) {
							tv_remid.setTextColor(Color.BLUE);
						} else {
							tv_remid.setTextColor(Color.RED);
						}
					} catch (java.text.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				tv_remid.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.not_yet_set));
				tv_remid.setTextColor(Color.BLACK);
			}

			if (!callDisp.cmp.getcomponentType().equals("call")) {
				rlayout.addView(tv_from, layLeft);
				rlayout.addView(tv_remid, layRight);
				rlayout.addView(note_title, layCenter);
				titLayout.addView(rlayout);

			} else {
				titLayout.removeAllViews();
			}

            if (callDisp.cmp.getcomponentType().equals("note")) {
                tvTitle.setText(callDisp.cmp.getContentName());
                String Temp_Path = AESFileCrypto.decryptFile(context, callDisp.cmp.getContentpath());
                showDefaultView(false);
                Log.d("list", "..............." + callDisp.cmp.getContentpath());
                // if (CompleteListView.textnotes == null)
                // CompleteListView.textnotes = new TextNoteDatas();

                // datas =
                // CompleteListView.textnotes.getInformation(callDisp.cmp
                // .getContentpath());
                if (FilesFragment.textnotes == null)
                    FilesFragment.textnotes = new TextNoteDatas();
                datas = FilesFragment.textnotes.getInformation(Temp_Path);

				edNotes.setText(datas);
				// edNotes.setKeyListener(null);
				contentLayout.removeAllViews();
				contentLayout.addView(edNotes);
//				commandlayout.setVisibility(View.VISIBLE);
				if (cbean.getContent() != null) {
					btn_comment.setText(cbean.getContent());
				} else {
					btn_comment.setText(SingleInstance.mainContext
							.getResources().getString(R.string.add_a_comment));

				}

//				footerlayout.setVisibility(View.VISIBLE);

				// edNotes.setFilters(new InputFilter[] { new InputFilter() {
				// public CharSequence filter(CharSequence src, int start,
				// int end, Spanned dst, int dstart, int dend) {
				// return src.length() < 1 ? dst.subSequence(dstart, dend)
				// : "";
				// }
				// } });

				btn_comment.setVisibility(View.VISIBLE);

			} else if (callDisp.cmp.getcomponentType().equals("audio")) {
				try {
					tvTitle.setText(callDisp.cmp.getContentName());
					contentLayout.removeAllViews();
					contentLayout.addView(AudioNoteView(3,
							callDisp.cmp.getContentpath()));
//					commandlayout.setVisibility(View.VISIBLE);
					btn_comment.setText(cbean.getContent());

//					footerlayout.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (callDisp.cmp.getcomponentType().equals("video")) {
				try {
                    isVideo = true;
					tvTitle.setText(callDisp.cmp.getContentName());
					contentLayout.removeAllViews();
					btn_comment.setText(cbean.getContent());

					if (callDisp.cmp.getContentpath().startsWith("http:")) {
						contentLayout
								.addView(createStreamVideoNote(callDisp.cmp
										.getContentpath()));

					} else {
						contentLayout.addView(createVideoNote(callDisp.cmp
								.getContentpath()));
					}

//					commandlayout.setVisibility(View.VISIBLE);

//					footerlayout.setVisibility(View.VISIBLE);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} else if (callDisp.cmp.getcomponentType().equals("photo")) {
				tvTitle.setText(callDisp.cmp.getContentName());
				contentLayout1.removeAllViews();
				ishand_sketch = false;
				imgPic = createImageView(callDisp.cmp.getContentpath());
				fileName = callDisp.cmp.getContentpath();
				contentLayout1.addView(imgPic);
				btn_comment.setText(cbean.getContent());
//				commandlayout.setVisibility(View.VISIBLE);

//				footerlayout.setVisibility(View.VISIBLE);
			} else if (callDisp.cmp.getcomponentType().equalsIgnoreCase(
					"sketch")) {
				tvTitle.setText(callDisp.cmp.getContentName());
				contentLayout.removeAllViews();
				ishand_sketch = true;
				btn_comment.setText(cbean.getContent());
				contentLayout.addView(createHandSketchView(callDisp.cmp
						.getContentpath()));
//				commandlayout.setVisibility(View.VISIBLE);
//				footerlayout.setVisibility(View.VISIBLE);
			} else if (callDisp.cmp.getcomponentType().equalsIgnoreCase(
					"document")) {
				try {
                    String Temp_Path = AESFileCrypto.decryptFile(context,callDisp.cmp.getContentpath());
					tvTitle.setText(callDisp.cmp.getContentName());
					contentLayout.removeAllViews();
					contentLayout.setBackgroundColor(Color.WHITE);
					Button openFile = new Button(context);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					params.setMargins(0, 2, 0, 5);
					RelativeLayout heading = new RelativeLayout(context);
					heading.setLayoutParams(params);
					TextView textHead = new TextView(context);
					TextView textType = new TextView(context);
					TextView headingText = new TextView(context);
					textHead.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.name)
							+ " : "
							+ callDisp.cmp.getContentName());
					textType.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.type)
							+ callDisp.cmp.getcomponentType());
					textHead.setTextColor(Color.BLACK);
					textType.setTextColor(Color.BLACK);
					textHead.setEllipsize(TruncateAt.END);
					textType.setEllipsize(TruncateAt.END);
					RelativeLayout.LayoutParams leftTextParams = new RelativeLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					leftTextParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					textHead.setLayoutParams(leftTextParams);
					textHead.setId(1);
					RelativeLayout.LayoutParams rightTextParams = new RelativeLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					rightTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					textType.setLayoutParams(rightTextParams);
					RelativeLayout.LayoutParams headingTextParams = new RelativeLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					headingTextParams.addRule(RelativeLayout.CENTER_IN_PARENT);
					headingText
							.setText(SingleInstance.mainContext.getResources()
									.getString(R.string.pls_click_below));
					headingText.setTextColor(Color.BLACK);
					headingText.setId(2);
					headingText.setLayoutParams(headingTextParams);
					RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					params1.addRule(RelativeLayout.BELOW, headingText.getId());
					params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
					openFile.setText("Open");
					openFile.setLayoutParams(params1);
                    openFile.setTag(Temp_Path);
					heading.addView(textHead);
					heading.addView(textType);
					heading.addView(headingText);
					heading.addView(openFile);
					contentLayout.addView(heading);
					openFile.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							cList.openFilesinExternalApp(v.getTag().toString());

						}
					});
//					commandlayout.setVisibility(View.VISIBLE);
					btn_comment.setText(cbean.getContent());
//					footerlayout.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (cbean != null && cbean.getComponentId() > 0) {
				SingleInstance.mainContext.notifyUI();
			}
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showReminder() {
		try {
			Button btnSet;
			final DatePicker dp;
			final TimePicker tp;
			final AlertDialog alertReminder = new AlertDialog.Builder(context)
					.create();

			ScrollView tblDTPicker = (ScrollView) View.inflate(context,
					R.layout.date_time_picker, null);

			btnSet = (Button) tblDTPicker.findViewById(R.id.btnSetDateTime);
			dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
			tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);

			btnSet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String strDateTime = +dp.getYear()
							+ "-"
							+ WebServiceReferences.setLength2((dp.getMonth() + 1))
							+ "-"
							+ WebServiceReferences.setLength2(dp
									.getDayOfMonth())
							+ " "
							+ WebServiceReferences.setLength2(tp
									.getCurrentHour())
							+ ":"
							+ WebServiceReferences.setLength2(tp
									.getCurrentMinute());

					if (CompleteListView.CheckReminderIsValid(strDateTime)) {
						String strQuery = null;

						if (GET_RESOURCES == TEXT) {
							String strNotes = "";
							strNotes = edNotes.getText().toString().trim();
							if (strNotes.length() != 0) {

								if (CompleteListView.textnotes == null)
									CompleteListView.textnotes = new TextNoteDatas();

								CompleteListView.textnotes.checkAndCreate(cbean
										.getContentpath(), edNotes.getText()
										.toString());
								strQuery = "update component set comment='"
										+ strNotes
										+ "' ,reminderdateandtime='"
										+ strDateTime
										+ "',reminderstatus=1 where componentid="
										+ cbean.getComponentId();
							} else {
								showToast(SingleInstance.mainContext
										.getResources().getString(
												R.string.plz_write_reminder));
								alertReminder.dismiss();
							}
							if (strQuery != null)
								if (callDisp.getdbHeler(context).ExecuteQuery(
										strQuery)) {

									try {

										Intent reminderIntent = new Intent(
												context, ReminderService.class);
										startService(reminderIntent);

									} catch (Exception e) {
										e.printStackTrace();
									}
									alertReminder.dismiss();

									showToast(SingleInstance.mainContext
											.getResources().getString(
													R.string.reminder_added));
								} else {

									alertReminder.dismiss();

								}

						} else {
							strQuery = "update component set reminderdateandtime='"
									+ strDateTime
									+ "',reminderstatus=1 where componentid="
									+ cbean.getComponentId();

							if (callDisp.getdbHeler(context).ExecuteQuery(
									strQuery)) {

								try {
									Intent reminderIntent = new Intent(context,
											ReminderService.class);
									startService(reminderIntent);

								} catch (Exception e) {
									e.printStackTrace();
								}
								alertReminder.dismiss();
								showToast(SingleInstance.mainContext
										.getResources().getString(
												R.string.note_reminder_added));
								btnDone.setVisibility(View.GONE);
							} else {

								alertReminder.dismiss();

							}

						}
						if (ON_CREATE) {
							if (cbean != null) {
								if (tv_remid != null) {
									tv_remid.setText(SingleInstance.mainContext
											.getResources().getString(
													R.string.reminder)
											+ " :" + strDateTime.trim());
								}
								cbean.setReminderTime(strDateTime);

							}
						} else {
							if (tv_remid != null) {
								tv_remid.setText(SingleInstance.mainContext
										.getResources().getString(
												R.string.reminder)
										+ " :" + strDateTime.trim());
								tv_remid.setTextColor(Color.BLUE);
								if (cbean != null) {
									cbean.setReminderTime(strDateTime);

								}
							}
						}

					} else {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.pls_future_time));
					}
				}
			});
			alertReminder.setTitle(SingleInstance.mainContext.getResources()
					.getString(R.string.reminder));
			alertReminder.setView(tblDTPicker);
			alertReminder.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showDefaultView(final boolean onCreate) {

		try {
			GET_RESOURCES = TEXT;
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			ScrollView scrollView = new ScrollView(this);
			scrollView.setLayoutParams(lp);
			scrollView.setFillViewport(true);
//			tvTitle.setText(SingleInstance.mainContext.getResources()
//					.getString(R.string.txt_note));
			edNotes = new EditText(this);
			edNotes.setScroller(new Scroller(this));
			edNotes.setVerticalScrollBarEnabled(true);
			edNotes.setGravity(Gravity.TOP);
			edNotes.setLayoutParams(lp);
            edNotes.setLongClickable(false);
			edNotes.setHint(SingleInstance.mainContext.getResources()
					.getString(R.string.tap_to_add));
			edNotes.setHintTextColor(Color.BLACK);
			if (ON_CREATE) {
				edNotes.setFocusable(true);
				if (WebServiceReferences.contextTable
						.containsKey("sharenotepicker")
						|| WebServiceReferences.contextTable
								.containsKey("notepicker"))
					titLayout.setVisibility(View.GONE);

			} else {
				edNotes.setFocusable(true);
				edNotes.setEnabled(true);
//				editTextNotes.setVisibility(View.VISIBLE);

				if (filesFragment.filesList.size() == 0) {
					btn_next.setVisibility(View.GONE);
					btn_prev.setVisibility(View.GONE);
				}

			}
			btnDone.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if (!ON_CREATE) {
//						btnDone.setVisibility(View.GONE);
					}
					return false;
				}
			});
			edNotes.setOnFocusChangeListener(new OnFocusChangeListener() {

				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						Log.d("Test", "Isfocused");
						isFocused = true;
					}
				}
			});
			final String EnteredText = edNotes.getText().toString();
			edNotes.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
										  int before, int count) {
					// TODO Auto-generated method stub
					try {
						if (onCreate) {
							titLayout.setVisibility(View.GONE);
							btnDone.setVisibility(View.VISIBLE);
						} else {

							if (edNotes.getText().toString().trim().length() > 0
									&& !(EnteredText.contains(edNotes.getText()
									.toString().trim()))) {
//								btnDone.setVisibility(View.VISIBLE);
							} else {
//								btnDone.setVisibility(View.GONE);
							}
						}

						if (thumpnail.trim().length() == 0) {
							if (edNotes.getText().toString().length() == 0) {
								tvTitle.setText(SingleInstance.mainContext
										.getResources().getString(
												R.string.txt_note));
							} else {
								if (edNotes.getText().toString().length() >= 12) {
									tvTitle.setText(edNotes.getText()
											.toString().trim().substring(0, 11));
									flag = true;

								} else if (edNotes.getText().toString()
										.length() < 12) {
									tvTitle.setText(edNotes
											.getText()
											.toString()
											.substring(
													0,
													edNotes.getText()
															.toString()
															.length()));
									flag = true;

								}

							}
						}
						if (edNotes.getText().toString().length() != 0) {
							if (flag == true)

							{
								tvTitle.setText(callDisp.cmp.getContentName());
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
											  int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					GET_RESOURCES = TEXT;
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void openCamera(String type)
	{
		if(type.equalsIgnoreCase("photo")) {
//			tvTitle.setText("Photo");
			GET_RESOURCES = FROM_CAMERA;
			Long free_size = callDisp.getExternalMemorySize();
			if (free_size > 0 && free_size >= 5120) {
				if (!forms)
					ComponentPath = Environment
							.getExternalStorageDirectory()
							+ "/COMMedia/"
							+ CompleteListView.getFileName()
							+ ".jpg";
				else
					ComponentPath = Environment
							.getExternalStorageDirectory()
							+ "/COMMedia/MPD_image"
							+ CompleteListView.getFileName()
							+ ".jpg";
				Intent intent = new Intent(context,
						CustomVideoCamera.class);
				intent.putExtra("filePath", ComponentPath);
				intent.putExtra("isPhoto", true);
				startActivityForResult(intent, FROM_CAMERA);
			} else {
				showToast(SingleInstance.mainContext.getResources()
						.getString(R.string.insufficient_memory));
			}
		}else if(type.equalsIgnoreCase("video")){
			CompleteListView.checkDir();

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
					imm.hideSoftInputFromWindow(edNotes.getWindowToken(), 0);
					getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				}
				if (!send)
					cbean = null;
//				commandlayout.setVisibility(View.GONE);
//				footerlayout.setVisibility(View.GONE);

				ComponentPath = null;
				tvTitle.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.video));
				String strIPath = null;
				if (forms) {
					strIPath = "/sdcard/COMMedia/" + "MVD_"
							+ CompleteListView.getFileName();
					ComponentPath = strIPath;

					Log.i("BLOB", "component creator via forms" + strIPath);
				} else {
					strIPath = "/sdcard/COMMedia/"
							+ CompleteListView.getFileName();
					ComponentPath = strIPath;
				}

				GET_RESOURCES = CAPTURE_VIDEO;
				Intent intent = new Intent(context, CustomVideoCamera.class);
				intent.putExtra("filePath", ComponentPath);
				startActivityForResult(intent, 40);

			} else {
				showToast(SingleInstance.mainContext.getResources()
						.getString(R.string.video_kindly_stop));

			}
		}
	}

	private void openGallery()
	{
		tvTitle.setText("Photo");
		if (Build.VERSION.SDK_INT < 19) {
			GET_RESOURCES = FROM_GALERY;
			Intent intent = new Intent(
					Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, FROM_GALERY);
		} else {
			Log.i("img", "sdk is above 19");
			GET_RESOURCES = GALLERY_KITKAT_INTENT_CALLED;
			Intent intent = new Intent(
					Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			startActivityForResult(intent,
					GALLERY_KITKAT_INTENT_CALLED);

		}
	}
	public void showMenu() {
		try {
			final CharSequence[] items = {
					SingleInstance.mainContext.getResources().getString(
							R.string.gallery),
					SingleInstance.mainContext.getString(R.string.start_camera),
					SingleInstance.mainContext.getString(R.string.cancel) };

			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {

//					btnDone.setVisibility(View.INVISIBLE);
					tvTitle.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.photo));
					if (item == 0) {
						if (Build.VERSION.SDK_INT < 19) {
							GET_RESOURCES = FROM_GALERY;
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.setType("image/*");
							startActivityForResult(intent, FROM_GALERY);
						} else {
							Log.i("img", "sdk is above 19");
							GET_RESOURCES = GALLERY_KITKAT_INTENT_CALLED;
							Intent intent = new Intent(
									Intent.ACTION_OPEN_DOCUMENT);
							intent.addCategory(Intent.CATEGORY_OPENABLE);
							intent.setType("image/*");
							startActivityForResult(intent,
									GALLERY_KITKAT_INTENT_CALLED);

						}
					}
			   if (item == 1) {
						GET_RESOURCES = FROM_CAMERA;
						Long free_size = callDisp.getExternalMemorySize();
						if (free_size > 0 && free_size >= 5120) {
							if (!forms)
								ComponentPath = Environment
										.getExternalStorageDirectory()
										+ "/COMMedia/"
										+ CompleteListView.getFileName()
										+ ".jpg";
							else
								ComponentPath = Environment
										.getExternalStorageDirectory()
										+ "/COMMedia/MPD_image"
										+ CompleteListView.getFileName()
										+ ".jpg";
							// Intent intent = new Intent(context,
							// MultimediaUtils.class);
							// intent.putExtra("filePath", ComponentPath);
							// intent.putExtra("requestCode", FROM_CAMERA);
							// intent.putExtra("action",
							// MediaStore.ACTION_IMAGE_CAPTURE);
							// intent.putExtra("createOrOpen", "create");
							// startActivity(intent);
							Intent intent = new Intent(context,
									CustomVideoCamera.class);
							intent.putExtra("filePath", ComponentPath);
							intent.putExtra("isPhoto", true);
							startActivityForResult(intent, FROM_CAMERA);
						} else {
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.insufficient_memory));
						}
					} else if (item == 1) {
						dialog.cancel();
					}
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (send)
						finish();
					if (WebServiceReferences.contextTable
							.containsKey("sharenotepicker")) {
						finish();
					}
				}

			});
			AlertDialog alert = builder.create();
			alert.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showAudioView() {
		try {

			commandlayout.setVisibility(View.GONE);
			titLayout.setVisibility(View.VISIBLE);
			footerlayout.setVisibility(View.GONE);
			btn_next.setVisibility(View.GONE);
			btn_prev.setVisibility(View.GONE);
//			btn_delete.setVisibility(View.VISIBLE);
			String strIPath = null;
			GET_RESOURCES = AUDIO;
			contentLayout.removeAllViews();
			if (forms) {
				strIPath = "/sdcard/COMMedia/" + "MAD_audio"
						+ CompleteListView.getFileName() + ".mp4";
				ComponentPath = strIPath;
				Log.i("BLOB", "audiiooooo" + strIPath);

			} else {
				strIPath = "/sdcard/COMMedia/" + CompleteListView.getFileName()
						+ ".mp4";
				ComponentPath = strIPath;
				Log.i("BLOB", "not form audiiooooo" + strIPath);

			}
			Intent intent = new Intent(context, MultimediaUtils.class);
			intent.putExtra("filePath", ComponentPath);
			intent.putExtra("requestCode", AUDIO);
			intent.putExtra("action", "audio");
			intent.putExtra("createOrOpen", "create");
			startActivity(intent);

		} catch (Exception e) {
			Log.e("audio", "Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private LinearLayout AudioNoteView(int state, final String AudioPath)
			throws Exception {

		try {
			if (ON_CREATE) {
				btnDone.setVisibility(View.VISIBLE);
				titLayout.setVisibility(View.GONE);

			} else {
				titLayout.setVisibility(View.VISIBLE);
			}
			final int PRE_PLAY = 3;

			final LinearLayout llAudio = new LinearLayout(context);
			llAudio.setOrientation(LinearLayout.VERTICAL);
			llAudio.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
			llAudio.setGravity(Gravity.CENTER);
			final Button btnAudio = new Button(context);
			Log.d("Test", "Audio@@ComponentCreator@@" + AudioPath.length());
			btnAudio.setHint(Integer.toString(state));
			btnAudio.setGravity(Gravity.CENTER);
			tv_file.setVisibility(View.INVISIBLE);
			file_img.setVisibility(View.INVISIBLE);
			imageLoader.DisplayImage(AudioPath,newFileImg, drawable.audionotesnew);

			btnAudio.setHintTextColor(Color.TRANSPARENT);
			btnAudio.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			if (state == PRE_PLAY) {
				btnAudio.setBackgroundResource(R.drawable.btn_play_new);
				audioExist = true;
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
				commandlayout.setVisibility(View.GONE);
				footerlayout.setVisibility(View.GONE);
			}
			return llAudio;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

    private ImageView createImageView(final String Path) {
        try {
            ImageView iv = new ImageView(context);
            File CheckFile = new File(Path);
            if (CheckFile.exists()) {
                iv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));
//                imageLoader.DisplayImage(Path, iv, R.drawable.broken);
				tv_file.setVisibility(View.INVISIBLE);
				file_img.setVisibility(View.INVISIBLE);
				imageLoader.DisplayImage(Path, newFileImg, R.drawable.broken);
//                iv.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            Intent in = new Intent(context,
//                                    FullScreenImage.class);
//                            in.putExtra("image", Path);
//                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(in);
//                        } catch (Exception e) {
//                            // TODO: handle exception
//                        }
//                    }
//                });

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT);
				params.gravity = Gravity.CENTER;
				iv.setLayoutParams(params);

			} else {
				iv.setBackgroundResource(R.drawable.broken);
			}
			return iv;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

    private LinearLayout createHandSketchView(final String Path) {
        try {
            LinearLayout linearLayout = new LinearLayout(context);
            File CheckFile = new File(Path);
            if (CheckFile.exists()) {
                linearLayout.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//                Bitmap bitmap = BitmapFactory.decodeFile(Path);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(AESFileCrypto.decryptBitmap(Path));
                linearLayout.setBackgroundDrawable(bitmapDrawable);
                linearLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent in = new Intent(context,
                                    FullScreenImage.class);
                            in.putExtra("image", Path);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(in);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT);
                params.gravity = Gravity.CENTER;
                linearLayout.setLayoutParams(params);
            } else {
                linearLayout.setBackgroundResource(R.drawable.broken);
            }
            return linearLayout;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

	}

	Bitmap img = null;

	public class bitmaploader extends AsyncTask<Uri, Void, Void> {

		@Override
		protected Void doInBackground(Uri... params) {
			// TODO Auto-generated method stub
			try {
				for (Uri uri : params) {
					Log.d("image", "came to doin backgroung for image");
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
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				callDisp.cancelDialog();
				if (ComponentPath != null)
					// img = callDisp.ResizeImage(ComponentPath);
					GET_RESOURCES = FROM_GALERY;
				// if (img != null) {

				// FileOutputStream fileOutputStream = new FileOutputStream(
				// ComponentPath);
				// img.compress(CompressFormat.JPEG, 75, fileOutputStream);
				contentLayout.removeAllViews();
				ishand_sketch = false;
				contentLayout.addView(createImageView(ComponentPath));
				btnaudio.setEnabled(false);
				btnVideo.setEnabled(false);
				// } else {
				// if (ComponentPath != null) {
				// File fle = new File(ComponentPath);
				// if (fle.exists())
				// fle.delete();
				// }
				// ComponentPath = null;
				// // btnDone.setVisibility(View.VISIBLE);
				// showToast("Kindly choose image files");
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			try {
				// TODO Auto-generated method stub
				super.onPreExecute();
				callDisp.showprogress(CallDispatcher.pdialog,
						SingleInstance.mainContext);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			// TODO Auto-generated method stub

			super.onActivityResult(requestCode, resultCode, data);
			if (GET_RESOURCES == FROM_GALERY) {
				try {
					if (resultCode == Activity.RESULT_CANCELED) {
						ComponentPath = null;
						if (send)
							finish();
						if (WebServiceReferences.contextTable
								.containsKey("sharenotepicker")) {
							finish();
						}
						tvTitle.setText("Text Note");
						// showToast("Kindly choose image files");
					} else {
						btnDone.setVisibility(View.VISIBLE);
						overlay.setVisibility(View.GONE);


						Uri selectedImageUri = data.getData();
						ComponentPath = callDisp
								.getRealPathFromURI(selectedImageUri);
						File selected_file = new File(ComponentPath);
						int length = (int) selected_file.length() / 1048576;
						Log.d("busy", "........ size is------------->" + length);

						if (length <= 2) {

							if (img != null) {
								if (!img.isRecycled()) {
									img.recycle();
								}
							}
							contentLayout.removeAllViews();
							ishand_sketch = false;
							contentLayout
									.addView(createImageView(ComponentPath));

							btnaudio.setEnabled(false);
							btnVideo.setEnabled(false);
							btn_next.setVisibility(View.GONE);
							btn_prev.setVisibility(View.GONE);
						} else {

							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.image_size_too_large));

						}

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (GET_RESOURCES == GALLERY_KITKAT_INTENT_CALLED) {

				try {
					Log.d("img",
							"........GALLERY_KITKAT_INTENT_CALLED called ------------->");
					if (resultCode == Activity.RESULT_CANCELED) {
						ComponentPath = null;
						if (send)
							finish();
						if (WebServiceReferences.contextTable
								.containsKey("sharenotepicker")) {
							finish();
						}
//						tvTitle.setText("Text Note");
						// showToast("Kindly choose image files");
					} else {
						btnDone.setVisibility(View.VISIBLE);

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
						Log.d("busy", "........ size is------------->" + length);

						if (length <= 2) {

							try {
								new bitmaploader().execute(selectedImageUri);
								// img = ResizeImage(ComponentPath);
//								editTextNotes.setVisibility(View.GONE);
								btn_next.setVisibility(View.GONE);
								btn_prev.setVisibility(View.GONE);

							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							// another file
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.image_size_too_large));

						}
						overlay.setVisibility(View.GONE);

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (GET_RESOURCES == FROM_CAMERA) {
				try {
					if (resultCode == Activity.RESULT_CANCELED) {
						ComponentPath = null;
						if (send)
							finish();
						if (WebServiceReferences.contextTable
								.containsKey("sharenotepicker")) {
							finish();
						}
//						tvTitle.setText("Text Note");
					} else {
						btnDone.setVisibility(View.VISIBLE);
						if (img != null) {
							if (!img.isRecycled()) {
								img.recycle();
							}
						}

						if (ComponentPath != null) {
							// img = callDisp.ResizeImage(ComponentPath);
							// callDisp.changemyPictureOrientation(img,
							// ComponentPath);
							if (img != null) {
								if (!img.isRecycled()) {
									img.recycle();
								}
							}
							// img = callDisp.ResizeImage(ComponentPath);
							// if (img != null) {
							contentLayout1.removeAllViews();
							ishand_sketch = false;
							contentLayout1
									.addView(createImageView(ComponentPath));
							btnaudio.setEnabled(false);
							btnVideo.setEnabled(false);
							btn_next.setVisibility(View.GONE);
							btn_prev.setVisibility(View.GONE);
                            Log.d("AES_Log", "Encrypt created Image here");
							// }
						}
					}
					overlay.setVisibility(View.GONE);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (GET_RESOURCES == CAPTURE_VIDEO) {
				try {
					Log.i("logvideo", "----------onactivity video----------");
					if (resultCode == Activity.RESULT_CANCELED) {
						Log.i("logvideo",
								"----------onactivity video cancel----------");
						ComponentPath = null;
						if (send)
							finish();
						if (WebServiceReferences.contextTable
								.containsKey("sharenotepicker")) {
							finish();
						}
						tvTitle.setText("Text Note");
					} else {
						Log.i("logvideo",
								"----------onactivity video success----------");
						btnDone.setVisibility(View.VISIBLE);
//						editTextNotes.setVisibility(View.GONE);
						if (WebServiceReferences.contextTable
								.containsKey("frmreccreator")) {
//							commandlayout.setVisibility(View.GONE);

//							footerlayout.setVisibility(View.GONE);
						}

						Log.d(null, "############## came to video capture");

						if (resultCode == Activity.RESULT_CANCELED) {
							Log.d(null,
									"############## came to result canceled");

						} else if (resultCode == Activity.RESULT_OK) {
							Log.d(null, "############## came to result ok");
							Log.i("logvideo",
									"----------onactivity video compo----------"
											+ ComponentPath);
							File fl = new File(ComponentPath + ".mp4");
							if (fl.exists()) {
								Log.i("logvideo",
										"----------onactivity video file exist----------"
												+ ComponentPath + fl.exists());
								if (fl.length() != 0) {
									Log.i("logvideo",
											"----------onactivity video inside file length>0----------");
									SingleInstance.mainContext
											.CreateVideoThumbnail(ComponentPath);
									contentLayout.removeAllViews();
									Log.e("list", "pathhhhhhhhhhhhhhhh"
											+ ComponentPath);
									contentLayout
											.addView(createVideoNote(ComponentPath));
									btnaudio.setEnabled(false);
									btnImage.setEnabled(false);
									btn_delete.setVisibility(View.VISIBLE);
									btn_next.setVisibility(View.GONE);
									btn_prev.setVisibility(View.GONE);
                                    Log.d("AES_Log", "Encrypt created video here");

								} else {
									ComponentPath = null;
								}

							} else {
								ComponentPath = null;
							}
							overlay.setVisibility(View.VISIBLE);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (GET_RESOURCES == HAND_SKETCH) {

				try {
					btn_next.setVisibility(View.GONE);
					btn_prev.setVisibility(View.GONE);
					btn_delete.setVisibility(View.VISIBLE);
					overlay.setVisibility(View.GONE);
//					btnDone.setVisibility(View.GONE);
//					editTextNotes.setVisibility(View.GONE);

					if (resultCode == Activity.RESULT_CANCELED) {
						Log.d(null, "############## came to result canceled");
						showDefaultView(false);
						contentLayout.removeAllViews();
						contentLayout.addView(edNotes);
						ComponentPath = null;
						if (WebServiceReferences.contextTable
								.containsKey("sharenotepicker")) {
							finish();
						}
						tvTitle.setText("Text Note");
					} else if (resultCode == Activity.RESULT_OK) {
						Bundle bun = data.getBundleExtra("sketch");
						ComponentPath = bun.getString("path");
						if (img != null) {
							if (!img.isRecycled()) {
								img.recycle();
							}
						}

						if (ComponentPath != null) {
							tvTitle.setText(SingleInstance.mainContext
									.getResources().getString(
											R.string.hand_sketch));
							// img = callDisp.ResizeImage(ComponentPath);
							// if (img != null) {
							contentLayout.removeAllViews();
							ishand_sketch = true;
							contentLayout
									.addView(createHandSketchView(ComponentPath));
							btnaudio.setEnabled(false);
							btnVideo.setEnabled(false);
							// }
							saveFile();

						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if ((requestCode == 8) && (resultCode == -1)) {
				try {
					Log.i("IOS", "INSIDE ONACTIVITY RESULT=====>");
					Bundle bun = data.getBundleExtra("share");
					if (bun != null) {
						String[] userid = bun.getStringArray("userid");
						StringBuffer sbf = new StringBuffer();

						if (userid.length > 0) {
							sbf.append(userid[0]);
							for (int i = 1; i < userid.length; i++) {
								sbf.append(",").append(userid[i]);
							}
							doshare(sbf.toString());

						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (requestCode == 55) {
				try {
					if (resultCode == RESULT_OK) {
						String FilePath = data.getData().getPath();
						Log.i("file", "===> get from file manager" + FilePath);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (GET_RESOURCES == AUDIO) {
				try {
					overlay.setVisibility(View.GONE);
					contentLayout.addView(AudioNoteView(3, ComponentPath));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else if (requestCode == 38) {
				Log.d("strpath", "chatdoc " + strIPath);
				ComponentPath = data.getStringExtra("filePath");
				File fileCheck = new File(ComponentPath);

				if (fileCheck.exists()) {
					tv_file.setVisibility(View.INVISIBLE);
					file_img.setVisibility(View.INVISIBLE);
					imageLoader.DisplayImage(ComponentPath, newFileImg, drawable.attachfile);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showToast(SingleInstance.mainContext.getResources().getString(
					R.string.file_nt_send));
			finish();
		}
		if (WebServiceReferences.contextTable.containsKey("multimediautils"))
			((MultimediaUtils) WebServiceReferences.contextTable
					.get("multimediautils")).finish();

		if (send) {
			footerlayout.setVisibility(View.GONE);
			commandlayout.setVisibility(View.GONE);
			titLayout.setVisibility(View.GONE);
		}

	}

	Bitmap bitmapThumb = null;

	private LinearLayout createStreamVideoNote(final String path) {
		try {
			Log.e("list", "came to video note stream....." + path);
			final LinearLayout llayVideo = new LinearLayout(context);
			llayVideo.setOrientation(LinearLayout.VERTICAL);
			llayVideo.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			llayVideo.setGravity(Gravity.CENTER);

			if (bitmapThumb != null) {
				if (!bitmapThumb.isRecycled()) {
					bitmapThumb.recycle();
				}
			}

			final ImageView ivThunb = new ImageView(context);
			ivThunb.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			bitmapThumb = BitmapFactory.decodeResource(getResources(),
					R.drawable.btn_play_new);
			final TableLayout tblControl = new TableLayout(context);
			if (strScreenType.equalsIgnoreCase("other")) {
				TableLayout.LayoutParams params = new TableLayout.LayoutParams(
						TableLayout.LayoutParams.FILL_PARENT,
						TableLayout.LayoutParams.WRAP_CONTENT);
				tblControl.setLayoutParams(params);
			}
			if (!WebServiceReferences.contextTable.containsKey("notepicker")) {
//				commandlayout.setVisibility(View.VISIBLE);
//				footerlayout.setVisibility(View.VISIBLE);
			} else if (!WebServiceReferences.contextTable
					.containsKey("sharenotepicker")
					|| !WebServiceReferences.contextTable
							.containsKey("notepicker")) {
//				commandlayout.setVisibility(View.VISIBLE);
//				footerlayout.setVisibility(View.VISIBLE);
			}
			if (ON_CREATE) {
				showTitleView();
			}
			ivThunb.setImageBitmap(bitmapThumb);

			videoView = new VideoView(context);
			ivThunb.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					dialog = ProgressDialog.show(ComponentCreator.this, "",
							"Buffering video...", true);
					dialog.setCancelable(true);

					isVideoPlay = true;
					tblControl.removeAllViews();
					llayVideo.addView(videoView, 0);
					llayVideo.removeView(ivThunb);
					videoView
							.setLayoutParams(new LinearLayout.LayoutParams(
									(int) (noScrWidth / 1.4),
									(int) (noScrHeight / 3.0)));
					Uri video = Uri.parse(cbean.getContentpath());
					Log.i("filename", "filename :: " + cbean.getContentpath());
					videoView.setVideoURI(video);
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

					videoView.setOnPreparedListener(new OnPreparedListener() {

						public void onPrepared(MediaPlayer mp) {
							dialog.dismiss();
							videoView.start();
						}
					});

					/* videoView.start(); */
					final Button btnPlay = new Button(context);
					btnPlay.setHint(SingleInstance.mainContext.getResources()
							.getString(R.string.play));
					btnPlay.setHintTextColor(Color.TRANSPARENT);
					btnPlay.setBackgroundResource(R.drawable.v_pause);
					btnPlay.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Log.e("handler", "butto play.....");
							String hint = btnPlay.getHint().toString();
							if (hint.equals(SingleInstance.mainContext
									.getResources().getString(R.string.play))) {
								btnPlay.setHint(SingleInstance.mainContext
										.getResources().getString(
												R.string.pause));
								btnPlay.setBackgroundResource(R.drawable.v_play);
								videoView.pause();
								isVideoPlay = true;
							} else if (hint.equals(SingleInstance.mainContext
									.getResources().getString(R.string.pause))) {
								videoView.start();
								btnPlay.setBackgroundResource(R.drawable.v_pause);
								btnPlay.setHint(SingleInstance.mainContext
										.getResources()
										.getString(R.string.play));
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
					if (strScreenType.equalsIgnoreCase("other")) {
						TableLayout.LayoutParams params = new TableLayout.LayoutParams(
								TableLayout.LayoutParams.FILL_PARENT,
								TableLayout.LayoutParams.WRAP_CONTENT);
						params.setMargins(0, 0, 0, 55);
						tr.setLayoutParams(params);
					}
					tr.setGravity(Gravity.CENTER);
					Button btnStop = new Button(context);
					btnStop.setBackgroundResource(R.drawable.stop_select);
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

							intentVPlayer.putExtra("timevideo", pos);

							// intentVPlayer.putExtra("File_Path",sss
							// cbean.getContentpath());
							// intentVPlayer.putExtra("Player_Type",
							// "Video Player");
							// intentVPlayer.putExtra("time", pos);
							intentVPlayer.putExtra("video",
									cbean.getContentpath());

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

			return llayVideo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private LinearLayout createVideoNote(String path) {

		try {
			Log.e("list", "came to video note.....");
			final LinearLayout llayVideo = new LinearLayout(context);
			llayVideo.setOrientation(LinearLayout.VERTICAL);
			llayVideo.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			llayVideo.setGravity(Gravity.CENTER);


			if (bitmapThumb != null) {
				if (!bitmapThumb.isRecycled()) {
					bitmapThumb.recycle();
				}
			}

			if (!path.endsWith(".mp4")) {
				path = path + ".mp4";
			}
			tv_file.setVisibility(View.INVISIBLE);
			file_img.setVisibility(View.INVISIBLE);
			imageLoader.DisplayImage(path,newFileImg, drawable.videonotesnew);
			String thumb = "";
			if (path.endsWith(".mp4")) {
				thumb = path.replace(".mp4", "");
			}
            final String filename = AESFileCrypto.decryptFile(context,path);
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
				Log.e("list", ">>>>>>>>>>>>>>>" + fileCheckV.exists());
				final TableLayout tblControl = new TableLayout(context);
				if (!WebServiceReferences.contextTable
						.containsKey("notepicker")) {
//					commandlayout.setVisibility(View.VISIBLE);
//					footerlayout.setVisibility(View.VISIBLE);
				}
				if (forms) {
//					commandlayout.setVisibility(View.GONE);
//					footerlayout.setVisibility(View.GONE);
				}
				if (ON_CREATE) {
					showTitleView();
				}
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
						videoView
								.setLayoutParams(new LinearLayout.LayoutParams(
										(int) (noScrWidth / 3.0),
										(int) (noScrHeight / 1.4)));
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
						btnPlay.setHint(SingleInstance.mainContext
								.getResources().getString(R.string.play));
						btnPlay.setHintTextColor(Color.TRANSPARENT);
						btnPlay.setBackgroundResource(R.drawable.btn_pause_new);
						btnPlay.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Log.e("handler", "butto play.....");
								String hint = btnPlay.getHint().toString();
								if (hint.equals(SingleInstance.mainContext
										.getResources()
										.getString(R.string.play))) {
									btnPlay.setHint(SingleInstance.mainContext
											.getResources().getString(
													R.string.pause));
									btnPlay.setBackgroundResource(R.drawable.btn_play_new);
									videoView.pause();
									isVideoPlay = true;
								} else if (hint
										.equals(SingleInstance.mainContext
												.getResources().getString(
														R.string.pause))) {
									videoView.start();
									btnPlay.setBackgroundResource(R.drawable.btn_pause_new);
									btnPlay.setHint(SingleInstance.mainContext
											.getResources().getString(
													R.string.play));
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
								intentVPlayer.putExtra("timevideo", pos);

								// intentVPlayer.putExtra("File_Path",
								// filename);
								// intentVPlayer.putExtra("Player_Type",
								// "Video Player");
								// intentVPlayer.putExtra("time", pos);

								intentVPlayer.putExtra("video", filename);

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
				Log.e("list", "?????????????" + contentLayout.getChildCount());

			}
			return llayVideo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private void showToast(String msg) {
		try {
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		try {
			super.onPause();
			if (StopVideoHandler != null) {
				if (isVideoPlay) {
					Message msg = new Message();
					msg.arg1 = 1;
					StopVideoHandler.sendMessage(msg);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		try {
            AppMainActivity.inActivity = this;
			super.onResume();
//			if (WebServiceReferences.Imcollection.size() == 0)
//				btnIMRequest.setVisibility(View.GONE);
//			else
//				btnIMRequest.setVisibility(View.VISIBLE);
			if(AppReference.mainContext.isPinEnable) {
				if (AppReference.mainContext.openPinActivity) {
					AppReference.mainContext.openPinActivity=false;
					if(Build.VERSION.SDK_INT>20 && AppReference.mainContext.isTouchIdEnabled) {
						Intent i = new Intent(ComponentCreator.this, MainActivity.class);
						startActivity(i);
					}else {
						Intent i = new Intent(ComponentCreator.this, PinSecurity.class);
						startActivity(i);
					}
				} else {
					AppReference.mainContext.count=0;
					AppReference.mainContext.registerBroadcastReceiver();
				}
			}

			if (CallDispatcher.fromMultimediaUtils
					|| CallDispatcher.handsketch_edit) {
				ON_CREATE = true;
				showNoteView("note");
				CallDispatcher.fromMultimediaUtils = false;
				CallDispatcher.handsketch_edit = false;
				ComponentPath = "";
				if (WebServiceReferences.contextTable
						.containsKey("sharenotepicker")
						|| WebServiceReferences.contextTable
								.containsKey("notepicker") || response)
					finish();
			}
			if (imgPic != null) {
				img = callDisp.ResizeImage(fileName);
				imgPic.setImageBitmap(img);
				// imgPic = createImageView(callDisp.cmp.getContentpath());
			} else {

			}

			// NotePickerScreen npScreen = (NotePickerScreen)
			// WebServiceReferences.contextTable
			// .get("notepicker");
			// File file = new File(ComponentPath);
			// if (npScreen != null && !file.exists()) {
			// finish();
			// }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("finally")
	private boolean createbrokenThump(String strThumbPath) {
		boolean status = false;
		try {

			File fl = new File(strThumbPath + ".mp4");
			if (fl.exists()) {

				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						drawable.broken);
				bmp = ResizeVideoImage(bmp);
				Log.i("myLog", "Image Height :" + bmp.getHeight());
				Log.i("myLog", "Image Width  :" + bmp.getWidth());
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
			Log.e("Exc", "FileNotFoundException : " + ex);
			status = false;
		} catch (IOException ioe) {
			Log.e("Exc", "IOException : " + ioe);
			status = false;
		} catch (Exception e) {
			Log.e("Exc", "Exception : " + e);
			status = false;
		} finally {
			return (status);
		}
	}

	private Bitmap ResizeVideoImage(Bitmap bitmap) {

		try {
			int outWidth = bitmap.getWidth();
			int outHeight = bitmap.getHeight();

			if (outWidth == outHeight) {
				bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
			} else {

				if (outHeight == 200) {
					bitmap = Bitmap.createScaledBitmap(bitmap, outWidth,
							outHeight, true);
				} else {
					double ratio;
					if (outHeight < outWidth) {
						ratio = (double) outWidth / (double) outHeight;

						bitmap = Bitmap.createScaledBitmap(bitmap,
								(int) (200 * ratio), 200, true);
					} else {
						ratio = (double) outWidth / (double) outHeight;
						bitmap = Bitmap.createScaledBitmap(bitmap,
								(int) Math.round(200 / ratio), 200, true);
					}

				}

			}
			return bitmap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private Bitmap combineImages(Bitmap c, String time) {
		try {
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
			comboImage.drawText(time, (c.getWidth() / 4), (c.getHeight() / 2),
					paint);
			return cs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void saveFileFromGallery(String fullPath, String fileName,
			String ftpPath, String comment, String reminderdate,
			String reminderzone, String reminderresponsetype, int reminderstatus) {
		try {

			cbean = DBAccess.getdbHeler(context).putDBEntry(
					7,
					fullPath,
					FilesFragment.newInstance(context)
							.getNoteCreateTimeForFiles(), fileName.trim(),
					complBean,fileDesc.getText().toString().trim());
			if (cbean != null) {
				// if (send)
				// showToast("Text note sent succesfully");
				// else
				if (!send)
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.text_note_saved_succesfully));
				ON_CREATE = false;
				callDisp.cmp = cbean;
				openNote();
			}
			// filesListRefresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void finishActivity() {
		try {
			Log.i("files", "comp path :: " + ComponentPath);
			Log.i("files", "oncreate :: " + ON_CREATE);
			if (ON_CREATE) {
				if (GET_RESOURCES == TEXT
						&& edNotes.getText().toString() != null
						&& edNotes.getText().toString().length() > 0) {
					showAlert();
				} else
					showAlert();
			} else {

				if (WebServiceReferences.contextTable
						.containsKey("frmreccreator")) {
					if (ON_CREATE) {
						// listener.onActivityResults(100, 1, Data);

					}
				}
				finish();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stopPlayBck() {

		try {
			if (StopAudioRecordingHandler != null) {
				StopAudioRecordingHandler.sendEmptyMessage(430);
			}
			if (videoView != null) {
				if (videoView.isPlaying()) {
					videoView.stopPlayback();
				}
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static String getNoteCreateTime() {
	// try {
	// Date curDate = new Date();
	// SimpleDateFormat sdf = new SimpleDateFormat(
	// CallDispatcher.dateFormat + " hh:mm:ss aaa");
	// return sdf.format(curDate);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return null;
	// }
	// }

	private void showAlert() {
		try {
			Log.e("list", "##############" + ComponentPath);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			String message = SingleInstance.mainContext.getResources()
					.getString(R.string.save_and_goback);
			if (send) {
				message = SingleInstance.mainContext.getResources().getString(
						R.string.save_and_send);
			}
			builder.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									if (from != null) {
										if (from.equalsIgnoreCase("utility")) {
											setActivityResult();
										} else
											saveFile();
									} else
										saveFile();

									dialog.dismiss();
									audioExist = false;

//									refreshList();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									if (WebServiceReferences.contextTable
											.containsKey("sharenotepicker")) {
										((ShareNotePicker) WebServiceReferences.contextTable
												.get("sharenotepicker"))
												.refreshList();
										finish();
									}
									if (WebServiceReferences.contextTable
											.containsKey("notepicker")) {
										((NotePickerScreen) WebServiceReferences.contextTable
												.get("notepicker"))
												.refreshList();
										finish();
									}
									if (GET_RESOURCES == AUDIO && audioExist) {
										if (SingleInstance.audioComponent)
											refreshList();
										else {
											File file = new File(ComponentPath);
											if (file.exists())
												file.delete();
											// refreshList();
											showNoteView("note");
											showDefaultView(false);
											contentLayout.removeAllViews();
											contentLayout.addView(edNotes);
											ComponentPath = null;
											titLayout
													.setVisibility(View.VISIBLE);
											tvTitle.setText(SingleInstance.mainContext
													.getResources().getString(
															R.string.files));
											finish();
										}

									} else {

										audioExist = false;
										refreshList();
									}

								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			super.onDestroy();
			if (WebServiceReferences.contextTable.containsKey("Component")) {
				WebServiceReferences.contextTable.remove("Component");
			}
			if (WebServiceReferences.contextTable.containsKey("a_play")) {
				WebServiceReferences.contextTable.remove("a_play");
			}

			Object cur_context_object = null;
			Iterator it0 = SingleInstance.current_open_activity_detail.entrySet().iterator();
			while (it0.hasNext()) {
				Map.Entry pair0 = (Map.Entry) it0.next();
				Object cur_context_object0 = pair0.getKey();
				if(cur_context_object0 instanceof ComponentCreator) {
					cur_context_object =cur_context_object0;
				}
			}
			if(cur_context_object != null) {
				Log.i("reopen", "GroupChatActivity containsKey0");
				SingleInstance.current_open_activity_detail.remove(cur_context_object);
			}
			if(save_state){
				if(SingleInstance.current_open_activity_detail.containsKey(context)) {
					SingleInstance.current_open_activity_detail.remove(context);
				}
				SingleInstance.current_open_activity_detail.put(context,this.current_open_activity_detail);
				save_state = false;
			} else {
				if(SingleInstance.current_open_activity_detail.containsKey(context)) {
					SingleInstance.current_open_activity_detail.remove(context);
				}
			}

			if (callDisp != null) {
				callDisp.cmp = null;
			}

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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void finishScreenforCallRequest(){
		save_state = true;
		this.finish();
	}

	public void ShowError(String Title, String Message) {
		try {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void SetViewTitle(final TextView tv,
			final CompleteListBean component) {
		try {
			final AlertDialog.Builder alert = new AlertDialog.Builder(context);
			alert.setTitle(SingleInstance.mainContext.getResources().getString(
					R.string.change_note_name));
			final EditText input = new EditText(context);
			input.setHint("Max Length 12");
			input.setText(tv.getText().toString().trim());
			// Text and Length filter
			InputFilter[] FilterArray = new InputFilter[2];
			FilterArray[0] = new InputFilter.LengthFilter(15);
			FilterArray[1] = new InputFilter() {
				@Override
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {
					for (int i = start; i < end; i++) {
						if (source.charAt(i) == ' ') {
							return " ";
						} else if (!Character.isLetterOrDigit(source.charAt(i))) {
							return "";
						}
					}
					return null;
				}
			};

			input.setFilters(FilterArray);

			input.setSelection(tv.getText().toString().trim().length());
			alert.setView(input);
			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
											int whichButton) {

							String value = input.getText().toString().trim();
							Log.d("lg", "....." + value);
							if (!value.trim().equals(tv.getText().toString())) {
								String strTitleUpdateQry = null;
								if (component.getcomponentType().equals("note")) {
									Log.e("lg", "Update Title: if");
									strTitleUpdateQry = "update component set componentname='"
											+ value
											+ "',ContentThumbnail='"
											+ value
											+ "' where componentid="
											+ component.getComponentId();
									component.setContentName(value);
								} else {
									Log.e("lg", "Update Title: else");
									strTitleUpdateQry = "update component set componentname='"
											+ value
											+ "' where componentid="
											+ component.getComponentId();
								}
								if (callDisp.getdbHeler(context).ExecuteQuery(
										strTitleUpdateQry)) {
									Log.e("lg", "Update Title:"
											+ strTitleUpdateQry);
									tv.setText(value);
									int u_count = 0;
									for (int i = 0; i < value.length(); i++) {
										int charPoint = (char) value.charAt(i);
										if (charPoint >= 65 && charPoint <= 90) {
											u_count++;
										}
									}
									if (u_count > 9) {
										tv.setTextSize(9);

									}
									component.setContentName(tv.getText()
											.toString().trim());

								}

							}

						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
											int whichButton) {
							dialog.cancel();
						}
					});
			alert.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			// TODO Auto-generated method stub
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (ON_CREATE) {
					if (GET_RESOURCES == TEXT
							&& edNotes.getText().toString() != null
							&& edNotes.getText().toString().length() > 0)
						showAlert();
					else if (GET_RESOURCES == AUDIO && audioExist) {
						showAlert();
					} else if ((GET_RESOURCES == CAPTURE_VIDEO
							|| GET_RESOURCES == FROM_CAMERA
							|| GET_RESOURCES == FROM_GALERY || GET_RESOURCES == GALLERY_KITKAT_INTENT_CALLED)
							&& ComponentPath != null
							&& ComponentPath.length() > 0)
						showAlert();
					else {
						refreshList();
					}
				} else
					refreshList();
			}
			return super.onKeyDown(keyCode, event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static boolean CheckReminderIsValid(String strDate)
			throws java.text.ParseException {
		try {
			SimpleDateFormat formatter;

			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			Calendar cal = Calendar.getInstance();
			String strD2 = formatter.format(cal.getTime());
			Date currentDateTime = null;
			try {
				currentDateTime = (Date) formatter.parse(strD2);
				Log.e("rem", "currentDateTime:" + currentDateTime.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Date CheckDate = null;
			try {
				CheckDate = (Date) formatter.parse(strDate);
				Log.e("rem", "CheckDate:" + CheckDate.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (CheckDate.getTime() <= currentDateTime.getTime())
				return false;
			else
				return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	private void showTitleView() {
		try {
			titLayout.removeAllViews();
			rlayout = new RelativeLayout(context);
			rlayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			rlayout.setBackgroundColor(Color.WHITE);
			TextView tv_from = new TextView(context);
			tv_remid = new TextView(context);
			tv_from.setHeight(50);
			tv_from.setId(1);
			TextView note_title = new TextView(context);
			note_title.setHeight(50);
			tv_remid.setHeight(50);

			RelativeLayout.LayoutParams layLeft = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			layLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			RelativeLayout.LayoutParams layCenter = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			layCenter.addRule(RelativeLayout.CENTER_IN_PARENT);
			RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			RelativeLayout.LayoutParams layoutEdit = new RelativeLayout.LayoutParams(
					30, 30);
			layoutEdit.setMargins(3, 3, 0, 0);
			layoutEdit.addRule(RelativeLayout.RIGHT_OF, tv_from.getId());

			tv_from.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.from_self));
			tv_from.setTextSize(16);
			tv_from.setTextColor(Color.BLACK);
			tv_from.setGravity(Gravity.LEFT);

			tv_remid.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.not_yet_set));
			tv_remid.setTextColor(Color.BLACK);

			rlayout.addView(tv_from, layLeft);
			rlayout.addView(tv_remid, layRight);
			rlayout.addView(note_title, layCenter);
			titLayout.addView(rlayout);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void openAudio()
	{
		CompleteListView.checkDir();

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
				imm.hideSoftInputFromWindow(edNotes.getWindowToken(), 0);
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			}
			if (!send)
				cbean = null;
			commandlayout.setVisibility(View.GONE);
			footerlayout.setVisibility(View.GONE);
//			tvTitle.setText(SingleInstance.mainContext.getResources()
//					.getString(R.string._audio_));
			showAudioView();

		} else {
			showToast(SingleInstance.mainContext.getResources()
					.getString(R.string.video_kindly_stop));
		}
	}

	private void showNoteView(String type) {
		try {
			if (type.equals("note")) {
				showDefaultView(true);
				contentLayout.removeAllViews();
				contentLayout.addView(edNotes);
//				btnDone.setVisibility(View.GONE);
//				editTextNotes.setVisibility(View.GONE);
			} else if (type.equals("audio")) {
				CompleteListView.checkDir();

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
						imm.hideSoftInputFromWindow(edNotes.getWindowToken(), 0);
						getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
					}
					// btnImage.setEnabled(false);
					// btnVideo.setEnabled(false);
					// btn_sketch.setEnabled(false);
					if (!send)
						cbean = null;
					commandlayout.setVisibility(View.GONE);
					footerlayout.setVisibility(View.GONE);
//					tvTitle.setText(SingleInstance.mainContext.getResources()
//							.getString(R.string._audio_));
					showAudioView();

				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.video_kindly_stop));
				}

			} else if (type.equals("video")) {

				CompleteListView.checkDir();

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
						imm.hideSoftInputFromWindow(edNotes.getWindowToken(), 0);
						getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
					}
					if (!send)
						cbean = null;
					commandlayout.setVisibility(View.GONE);
					footerlayout.setVisibility(View.GONE);
					// btnDone.setEnabled(true);

					ComponentPath = null;
					tvTitle.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.video));
					String strIPath = null;
					if (forms) {
						strIPath = "/sdcard/COMMedia/" + "MVD_"
								+ CompleteListView.getFileName();
						ComponentPath = strIPath;

						Log.i("BLOB", "component creator via forms" + strIPath);
					} else {
						strIPath = "/sdcard/COMMedia/"
								+ CompleteListView.getFileName();
						ComponentPath = strIPath;
					}

					GET_RESOURCES = CAPTURE_VIDEO;
					Intent intent = new Intent(context, CustomVideoCamera.class);
					intent.putExtra("filePath", ComponentPath);
					// intent.putExtra("requestCode", CAPTURE_VIDEO);
					// intent.putExtra("action",
					// MediaStore.ACTION_VIDEO_CAPTURE);
					// intent.putExtra("createOrOpen", "create");
					startActivityForResult(intent, 40);

				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.video_kindly_stop));

				}

			} else if (type.equals("photo")) {

				CompleteListView.checkDir();

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
						imm.hideSoftInputFromWindow(edNotes.getWindowToken(), 0);
						getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
					}
					if (!send)
						cbean = null;
					commandlayout.setVisibility(View.GONE);
					footerlayout.setVisibility(View.GONE);
					showMenu();
				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.video_kindly_stop));

				}

			} else if (type.equalsIgnoreCase("handsketch")) {
				CompleteListView.checkDir();

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
						imm.hideSoftInputFromWindow(edNotes.getWindowToken(), 0);
						getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
					}
					if (!send)
						cbean = null;
					commandlayout.setVisibility(View.GONE);
					footerlayout.setVisibility(View.GONE);
					GET_RESOURCES = HAND_SKETCH;
					Intent intent = new Intent(context,
							HandSketchActivity2.class);
					intent.putExtra("send", send);
					startActivityForResult(intent, HAND_SKETCH);
				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.video_kindly_stop));

				}

			} else if (type.equalsIgnoreCase("document")) {
				CompleteListView.checkDir();

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
						imm.hideSoftInputFromWindow(edNotes.getWindowToken(), 0);
						getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
					}
					if (!send)
						cbean = null;
					commandlayout.setVisibility(View.GONE);
					footerlayout.setVisibility(View.GONE);
					GET_RESOURCES = DOCUMENT;

				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.video_kindly_stop));

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyResponse(String title, String Message) {
		try {
			callDisp.cancelDialog();
			ShowError(title, Message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void incomingCallNotification() {

		try {
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void changeReminderStatus(int id) {
		try {
			try {
				Log.i("thread", "############# reminder" + cbean);
				Log.i("thread",
						"############# reminder cid" + cbean.getComponentId());
				Log.i("thread", "############# reminder id" + id);
				if (cbean != null) {
					if (cbean.getComponentId() == id) {
						if (tv_remid != null) {
							tv_remid.setTextColor(Color.RED);
						}
					}
				} else if (callDisp.cmp.getComponentId() == id) {
					if (tv_remid != null) {
						tv_remid.setTextColor(Color.RED);

					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void hideRespondBtn(int id) {
		try {
			if (cbean != null) {
				if (cbean.getComponentId() == id) {
					if (btnrespond != null) {
						btnrespond.setVisibility(View.GONE);
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void openFolder() {
		try {
			Intent intent = new Intent(context, FileExplorer.class);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void finishActivitys() {

		if (GET_RESOURCES == CAPTURE_VIDEO) {
			if (!ComponentPath.endsWith(".mp4"))
				ComponentPath = ComponentPath + ".mp4";
		}

		if (WebServiceReferences.contextTable.containsKey("utilities")) {
			Intent i = new Intent();
			Bundle bun = new Bundle();
			bun.putString("filepath", ComponentPath);
			i.putExtra("utilities", bun);
			setResult(-10, i);
			finish();

		} else if (WebServiceReferences.contextTable
				.containsKey("textinputactivity")) {
			Intent i = new Intent();
			Bundle bun = new Bundle();
			bun.putString("filepath", ComponentPath);
			i.putExtra("share", bun);
			setResult(-10, i);
			finish();
		} else if (WebServiceReferences.running) {
			uploadDatas.add(noteType);
			uploadDatas.add("false");
			uploadDatas.add("");
			uploadDatas.add("");
			uploadDatas.add("auto");
			Log.d("sendershare", "" + uploadDatas);
			sendshare(ComponentPath);
		}

	}

	private void sendshare(String strIPath) {
		try {
//			Log.e("IOSftp", "Comes to upload here in sendShare ");
//			Log.d("IOSftptype", "Comes to upload here in sendShare " + noteType);
//			Log.d("IOSftppath", "Comes to upload here in sendShare "+ComponentPath);
//			ArrayList<String> buddy = new ArrayList<String>();
//			if (buddyname.contains(",")) {
//				String[] tousers = buddyname.split(",");
//				for (int i = 0; i < tousers.length; i++) {
//					buddy.add(tousers[i]);
//				}
//			} else {
//				buddy.add(buddyname);
//			}
//			String comments = "";
//			if (cbean != null)
//				comments = cbean.getContent();
//			Log.e("IOSftpa", "Comes to upload here in sendShare ");
//			callDisp.sendshare(false, CallDispatcher.LoginUser,
//					CallDispatcher.Password, buddyname, buddy, uploadDatas,
//					noteType, comments, strIPath, "fileslist", null, null,
//					null, null, null, false, complBean);
//			Log.e("IOSftp1b", "Comes to upload here in sendShare ");
			calldisp.uploadData.add(strIPath);
			String username = CallDispatcher.LoginUser;
			String password = CallDispatcher.Password;
			Log.i("FileUpload", "type--->" + noteType);
			Log.i("FileUpload", "type2--->" + strIPath);

			if(noteType.equalsIgnoreCase("photo")||noteType.equalsIgnoreCase("handsketch")) {
				Log.i("FileUpload", "IF PHOTO||Handsketch--->");

				Bitmap bitmap = BitmapFactory.decodeFile(strIPath);
				String base64 = encodeTobase64(bitmap);
				String fname = strIPath.split("/")[5];
				Log.i("FileUpload", "fname--->" + fname);
				Log.i("FileUpload", "uname--->" + username);
				Log.i("FileUpload", "password--->" + password);
				Log.i("FileUpload", "type--->" + noteType);
				Log.i("FileUpload", "base64--->" + base64);
				calldisp.uploadFile(username, password, noteType, fname, base64, strIPath,SingleInstance.mainContext);


			}else if(noteType.equalsIgnoreCase("audio")||noteType.equalsIgnoreCase("video"))
			{
//                encodeAudioVideoToBase64
				Log.i("FileUpload", "ELSE IF AUDIO||Video--->" );

				String base64 = encodeAudioVideoToBase64(strIPath);
//				if(noteType.equalsIgnoreCase("video"))
//					base64 = encodeAudioVideoToBase64(path1+".mp4");
				String fname = strIPath.split("/")[3];
				Log.i("FileUpload", "fname--->" + fname);
				Log.i("FileUpload", "uname--->" + username);
				Log.i("FileUpload", "password--->" + password);
				Log.i("FileUpload", "type--->" + noteType);
				Log.i("FileUpload", "base64--->" + base64);
//				if(noteType.equalsIgnoreCase("video"))
//					calldisp.uploadFile(username, password, noteType, fname+".mp4", base64);
//				else
				calldisp.uploadFile(username, password, noteType, fname, base64,strIPath,SingleInstance.mainContext);
			}
			// finish();

			ArrayList<String> b_list = new ArrayList<String>();
			b_list.add(buddyname);
			Log.d("Selectedbuddy", "buddy"+buddyname);
			ArrayList<String> u_list = new ArrayList<String>();
			u_list.add(noteType);
			u_list.add("false");
			u_list.add("");
			u_list.add("");
			u_list.add("");
			u_list.add("auto");


			calldisp.sendshare(true, username, password, buddyname, b_list, u_list, noteType, "",
					strIPath, "SendGridFiles", null, null, null,null, null, false, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//			strIPath = null;
		}
	}

	private void setActivityResult() {

		if (GET_RESOURCES == TEXT) {
			createTextNote();
		}
		if (GET_RESOURCES == CAPTURE_VIDEO) {
			if (!ComponentPath.endsWith(".mp4")) {
				ComponentPath = ComponentPath + ".mp4";
			}
		}
		if (ComponentPath != null) {

			if (new File(ComponentPath).exists()) {
				Bundle bndl = new Bundle();
				bndl.putString("filepath", ComponentPath);
				Intent intent = getIntent();
				intent.putExtra("utilityBuyer", bndl);
				setResult(Activity.RESULT_OK, intent);
			} else
				setResult(Activity.RESULT_CANCELED);
		} else
			setResult(Activity.RESULT_CANCELED);

		finish();
	}

	public void editNotes(TextView tv_title) {

		try {

			SetViewTitle(cbean, tv_title);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void SetViewTitle(final CompleteListBean component,
			final TextView tv_title) {
		try {
			final AlertDialog.Builder alert = new AlertDialog.Builder(context);
			alert.setTitle(SingleInstance.mainContext.getResources().getString(
					R.string.change_note_name));
			final EditText input = new EditText(context);
			input.setHint("Max Length 12");
			String text = null;
			text = component.getContentName();
			input.setText(text);
			// Text and Length filter
			InputFilter[] FilterArray = new InputFilter[2];
			FilterArray[0] = new InputFilter.LengthFilter(12);
			FilterArray[1] = new InputFilter() {
				@Override
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {
					for (int i = start; i < end; i++) {
						if (source.charAt(i) == ' ') {
							return " ";
						} else if (!Character.isLetterOrDigit(source.charAt(i))) {
							return "";
						}
					}
					return null;
				}
			};

			input.setFilters(FilterArray);
			input.setSelection(text.trim().length());
			alert.setView(input);
			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							try {
								String value = input.getText().toString()
										.trim();
								Log.i("file", "..... value :: " + value);
								if (value != null) {
									if (!value.trim().equals(
											tv_title.getText().toString())) {
										if (value.trim().length() != 0) {
											String strTitleUpdateQry = null;
											if (component.getcomponentType()
													.equals("note")) {
												Log.e("lg", "Update Title: if");
												strTitleUpdateQry = "update component set componentname='"
														+ value
														+ "' where componentid="
														+ component
																.getComponentId();
												component.setContentName(value);
											} else {
												Log.e("lg",
														"Update Title: else");
												strTitleUpdateQry = "update component set componentname='"
														+ value
														+ "' where componentid="
														+ component
																.getComponentId();

												component.setContentName(value);
											}
											if (callDisp.getdbHeler(context)
													.ExecuteQuery(
															strTitleUpdateQry)) {
												Log.e("lg", "Update Title:"
														+ strTitleUpdateQry);
												tv_title.setText(value);
												filesFragment
														.editFileName(component);

											}

										} else {
											showToast(SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.note_name_nt_empty));
										}

									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								Log.e("file", "==> " + e.getMessage());
								e.printStackTrace();
							}
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.cancel();
						}
					});
			alert.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void notifyReceivedIM(final SignalingBean sb) {
		// TODO Auto-generated method stub
		viewHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
//				btnIMRequest.setVisibility(View.VISIBLE);
//				btnIMRequest.setEnabled(true);
//
//				btnIMRequest
//						.setBackgroundResource(R.drawable.small_blue_balloon);

				if (!callDisp.getdbHeler(context).userChatting(sb.getFrom())) {
					callDisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
							CallDispatcher.LoginUser, 1,
							CallDispatcher.LoginUser);
				}

			}
		});

	}

	public void showAlert1(String message) {

		try {
			AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
			alertCall
					.setMessage(message)
					.setCancelable(false)
					.setNegativeButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									try {

									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
			alertCall.show();
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	private void responseType(final Components component, int pos) {
		if (pos == 0) {
			Intent intentComponent = new Intent(context, ComponentCreator.class);
			Bundle bndl = new Bundle();
			bndl.putString("type", "note");
			bndl.putBoolean("action", true);
			bndl.putBoolean("forms", false);
			bndl.putString("buddyname", component.getfromuser());
			bndl.putBoolean("send", true);
			bndl.putBoolean("response", true);
			intentComponent.putExtra("viewBean", complBean);
			intentComponent.putExtras(bndl);
			startActivity(intentComponent);
			// dialog.dismiss();
		} else if (pos == 1) {
			Intent intentComponent = new Intent(context, ComponentCreator.class);
			Bundle bndl = new Bundle();
			bndl.putString("type", "photo");
			bndl.putBoolean("action", true);
			bndl.putBoolean("forms", false);
			bndl.putString("buddyname", component.getfromuser());
			bndl.putBoolean("send", true);
			bndl.putBoolean("response", true);
			intentComponent.putExtras(bndl);
			startActivity(intentComponent);
			// dialog.dismiss();
		} else if (pos == 2) {
			Intent intentComponent = new Intent(context, ComponentCreator.class);
			Bundle bndl = new Bundle();
			bndl.putString("type", "audio");
			bndl.putBoolean("action", true);
			bndl.putBoolean("forms", false);
			bndl.putString("buddyname", component.getfromuser());
			bndl.putBoolean("send", true);
			bndl.putBoolean("response", true);
			intentComponent.putExtras(bndl);
			startActivity(intentComponent);
			// dialog.dismiss();
		} else if (pos == 3) {
			Intent intentComponent = new Intent(context, ComponentCreator.class);
			Bundle bndl = new Bundle();
			bndl.putString("type", "video");
			bndl.putBoolean("action", true);
			bndl.putBoolean("forms", false);
			bndl.putString("buddyname", component.getfromuser());
			bndl.putBoolean("send", true);
			bndl.putBoolean("response", true);
			intentComponent.putExtras(bndl);
			startActivity(intentComponent);
			// dialog.dismiss();
		}
	}

	// private void saveAndSettingScheduler(CompleteListBean cBean) {
	// try {
	// if(cBean.getReminderTime() != null) {
	// int row = 0;
	// AlarmManager alarmMgr = (AlarmManager) getApplicationContext()
	// .getSystemService(Context.ALARM_SERVICE);
	// Intent intent = new Intent(getApplicationContext(),
	// GroupChatBroadCastReceiver.class);
	// intent.putExtra("reminder", "filereminder");
	// // if (cBean.getSubCategory().equalsIgnoreCase("gs"))
	// // row = DB.getdbHeler(getApplicationContext())
	// // .insertorUpdateScheduleMsg(cBean);
	// // else if (cBean.getSubCategory().equalsIgnoreCase("gd"))
	// // saveAndNotifyGroupChatUI(cBean);
	// PendingIntent alarmIntent = PendingIntent.getBroadcast(
	// getApplicationContext(), row, intent, 0);
	// SingleInstance.scheduledMsg.put(cBean.getContentName(),
	// alarmIntent);
	// /**
	// * Due to IOS compatibility commenting this line and replacing
	// * the below date format
	// */
	// // SimpleDateFormat outPutFormatter = new SimpleDateFormat(
	// // "dd/MM/yyyy hh:mm");
	// // DateFormatSymbols dfSym = new DateFormatSymbols();
	// // dfSym.setAmPmStrings(new String[] { "am", "pm" });
	// // outPutFormatter.setDateFormatSymbols(dfSym);
	// /**
	// * Ends here
	// */
	// SimpleDateFormat outPutFormatter = new SimpleDateFormat(
	// "yyyy-MM-dd hh:mm");
	// Date date = null;
	// try {
	// date = outPutFormatter.parse(cBean.getReminderTime());
	// alarmMgr.set(AlarmManager.RTC_WAKEUP, date.getTime(),
	// alarmIntent);
	// } catch (Exception e) {
	// if (AppReference.isWriteInFile)
	// AppReference.logger.error(e.getMessage(), e);
	// else
	// e.printStackTrace();
	//
	// }
	//
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// public void notifyScheduleAndDeadLineMsg(GroupChatBean gcBean) {
	// if (gcBean.getSubCategory().equalsIgnoreCase("gs")) {
	// gcBean.setSenttime(getCurrentDateandTime());
	// saveAndNotifyGroupChatUI(gcBean);
	// } else if (gcBean.getSubCategory().equalsIgnoreCase("gd")) {
	// GroupChatActivity gcActivity = (GroupChatActivity)
	// SingleInstance.contextTable
	// .get("groupchat");
	// GroupBean gBean = DB.getdbHeler(getApplicationContext())
	// .getGroup(
	// "select * from grouplist where groupid='"
	// + gcBean.getGroupId() + "'");
	// if (gcActivity == null) {
	// alertDeadLineMessage(gcBean, gBean);
	// } else {
	// if (gcActivity.groupId.equalsIgnoreCase(gcBean.getGroupId())) {
	// gcActivity.deadLineMsgDialog(gcBean);
	// } else {
	// alertDeadLineMessage(gcBean, gBean);
	// }
	// }
	// }
	// ExchangesFragment exchanges = ExchangesFragment
	// .newInstance(getApplicationContext());
	// if (exchanges != null)
	// exchanges.adapter.notifyDataSetChanged();
	// }
	private String encodeTobase64(Bitmap image) {
		Bitmap immagex = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

		return imageEncoded;
	}
	private String encodeAudioVideoToBase64(String strIPath){
		String strFile=null;
		File file=new File(strIPath);
		try {
			FileInputStream file1=new FileInputStream(file);
			byte[] Bytearray=new byte[(int)file.length()];
			file1.read(Bytearray);
			strFile = Base64.encodeToString(Bytearray, Base64.NO_WRAP);//Convert byte array into string

		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("FileUpload", "audioVideoEncode========"+strFile);
		return strFile;
	}
	private Bitmap getBitmap(String filename) {
		// File f=fileCache.getFile(url);

		File f = new File(filename);

		try {
			Bitmap bitmap = decodeFile(f);
			if (bitmap != null)
				return bitmap;

			return bitmap;

		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}
	MemoryCache memoryCache = new MemoryCache();
	private Bitmap decodeFile(File f) {

		try {

			Bitmap bitmap = ImageUtils.decodeScaledBitmapFromSdCard(f.getAbsolutePath(), 200, 200);
			return bitmap;

		} catch (Exception e) {
			Log.d("IMAGE_LOAD", "Bitmap : " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onStop() {
		super.onStop();
		Log.i("pin", "Groupchatactivity Onstop");
		AppReference.mainContext.isApplicationBroughtToBackground();

	}
}
