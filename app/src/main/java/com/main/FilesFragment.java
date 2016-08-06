package com.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.callservices.AudioCallScreen;
import com.cg.callservices.CallConnectingScreen;
import com.cg.callservices.inCommingCallAlert;
import com.cg.callservices.VideoCallScreen;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.TextNoteDatas;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.WebView_doc;
import com.cg.commongui.listswipe.SwipeMenu;
import com.cg.commongui.listswipe.SwipeMenuCreator;
import com.cg.commongui.listswipe.SwipeMenuItem;
import com.cg.commongui.listswipe.SwipeMenuListView;
import com.cg.files.CompleteListBean;
import com.cg.files.ComponentCreator;
import com.cg.files.Components;
import com.cg.files.FileInfoFragment;
import com.cg.files.FilesAdapter;
import com.cg.files.sendershare;
import com.cg.hostedconf.AppReference;
import com.cg.instancemessage.BackgroundNotification;
import com.cg.snazmed.R;
import com.cg.timer.ReminderService;
import com.process.MemoryProcessor;
import com.util.SingleInstance;
import com.util.VideoPlayer;

import org.lib.model.BuddyInformationBean;
import org.lib.model.CallHistoryBean;
import org.lib.model.KeepAliveBean;
import org.lib.model.ShareReminder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;
/**
 * This screen will populate all the event entries made by the user, it may be
 * notes or call history. From here user can create notes and they can make the
 * calls too
 * 
 * 
 * 
 */
public class FilesFragment extends Fragment implements OnClickListener {
	public String strCompleteListQuery = null;
	public static CallDispatcher callDisp;
	private ProgressDialog progDailog = null;
	public String owner;
	private String FileType;
	private Boolean isfrommemeory=false;
	private boolean fromContacts = false;

	public Button btn_edit = null;
	public boolean select = false;

	public static TextNoteDatas textnotes = null;

	private AlertDialog alertDelay = null;

	private String autoPlayType = null;

	byte b = 10;
	byte c = 10;

	private static FilesFragment filesFragment;

	private static Context context;

	private Button plus = null; // this button plus hide in this page,this
								// button create fragment xml

	private Button edit;

	private Button selectall;

	public View view;

	private Handler handler = new Handler();

	public SwipeMenuListView listView = null;
	
	private Typeface tf_regular = null;
	
	private Typeface tf_bold = null;

	public FilesAdapter filesAdapter = null;

	private Button btnShareAll = null;

	private Button btnDeleteAll = null;


	private LinearLayout btnContainer = null;

	public static Vector<CompleteListBean> filesList = new Vector<CompleteListBean>();

	public boolean isEdit = false;

	// public boolean isSelectall = false;

	private EditText ed_search;

	private TextView text_show;
	private TextView title;

	public int getlist;

	public int getCount;

	public Vector<CompleteListBean> tempFilesList = new Vector<CompleteListBean>();

	private String username;
	LinearLayout content;
	private Boolean isSearch=false;
	private Button clearAllBtn;
	public static String sortorder="";

	private Dialog plus_dialog;

	public static FilesFragment newInstance(Context maincontext) {
		try {
			if (filesFragment == null) {
				context = maincontext;
				filesFragment = new FilesFragment();
				callDisp = CallDispatcher.getCallDispatcher(context);
			}

			return filesFragment;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return filesFragment;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		AppReference.bacgroundFragment=filesFragment;
		view = null;
		btn_edit = (Button) getActivity().findViewById(R.id.im_view);
		selectall = (Button) getActivity().findViewById(R.id.btn_brg);
		Button select = (Button) getActivity().findViewById(R.id.btn_brg);
		select.setVisibility(View.GONE);
		final TextView title = (TextView) getActivity().findViewById(
				R.id.activity_main_content_title);
		title.setText("SNAZBOX FILES");
		title.setVisibility(View.VISIBLE);

		Button imVw = (Button) getActivity().findViewById(R.id.im_view);
		imVw.setVisibility(View.GONE);

		final RelativeLayout mainHeader = (RelativeLayout) getActivity().findViewById(R.id.mainheader);
		mainHeader.setVisibility(View.VISIBLE);
		LinearLayout contact_layout = (LinearLayout) getActivity()
				.findViewById(R.id.contact_layout);
		contact_layout.setVisibility(View.GONE);
		ed_search=(EditText) getActivity().findViewById(R.id.search_box);
		clearAllBtn = (Button) getActivity().findViewById(R.id.btn_settings);
		ed_search.setVisibility(View.GONE);
		clearAllBtn.setVisibility(View.VISIBLE);
		clearAllBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_refresh));
		if (view == null) {
			view = inflater.inflate(R.layout.file_tab, null);
			try {
				content = (LinearLayout)view. findViewById(R.id.i1);
				content.removeAllViews();
				ImageView plusBtn=(ImageView)view.findViewById(R.id.plusBtn);

				plusBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						try {
							final Dialog dialog = new Dialog(context);
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.dialog_myacc_menu);
							WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
							lp.copyFrom(dialog.getWindow().getAttributes());
							lp.width = WindowManager.LayoutParams.MATCH_PARENT;
							lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
							lp.horizontalMargin = 15;
							Window window = dialog.getWindow();
							window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
							window.setAttributes(lp);
							window.setGravity(Gravity.BOTTOM);
							dialog.show();
							TextView newfile = (TextView) dialog.findViewById(R.id.delete_acc);
							newfile.setText("New File");
							newfile.setBackgroundColor(context.getResources().getColor(R.color.green));
							TextView newfolder = (TextView) dialog.findViewById(R.id.log_out);
							newfolder.setText("New Folder");
							newfolder.setVisibility(View.GONE);
							newfolder.setBackgroundColor(context.getResources().getColor(R.color.green));
							TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
							cancel.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View arg0) {
										dialog.dismiss();
								}
							});
							newfile.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intentComponent = new Intent(context,
											ComponentCreator.class);
									Bundle bndl = new Bundle();
									bndl.putString("type", "note");
									bndl.putBoolean("action", true);
									bndl.putBoolean("fromNew",true);
									intentComponent.putExtras(bndl);
									context.startActivity(intentComponent);
									dialog.dismiss();
								}
							});
							plus_dialog = dialog;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = layoutInflater.inflate(R.layout.files_list, content);
				getlist = 0;
				final RelativeLayout rl_file=(RelativeLayout)view.findViewById(R.id.rl_file);
				final ImageView file_img=(ImageView)view.findViewById(R.id.file_img);
				final TextView tv_file=(TextView)view.findViewById(R.id.tv_file);
				final TextView alpha_sort=(TextView)view.findViewById(R.id.alpha_sort);
				final TextView date_sort=(TextView)view.findViewById(R.id.date_sort);
				final TextView type_sort=(TextView)view.findViewById(R.id.type_sort);
				if(sortorder.equalsIgnoreCase("date"))
					date_sort.setTextColor(getResources().getColor(R.color.white));
				else if(sortorder.equalsIgnoreCase("alpha"))
					alpha_sort.setTextColor(getResources().getColor(R.color.white));
				else if(sortorder.equalsIgnoreCase("type"))
					type_sort.setTextColor(getResources().getColor(R.color.white));
				date_sort.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						sortorder="date";
						date_sort.setTextColor(getResources().getColor(R.color.white));
						alpha_sort.setTextColor(getResources().getColor(R.color.snazlgray));
						type_sort.setTextColor(getResources().getColor(R.color.snazlgray));
						filesAdapter = new FilesAdapter(context, filesList);
						listView.setAdapter(filesAdapter);
						filesAdapter.notifyDataSetChanged();
					}
				});
				alpha_sort.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						sortorder="alpha";
						date_sort.setTextColor(getResources().getColor(R.color.snazlgray));
						alpha_sort.setTextColor(getResources().getColor(R.color.white));
						type_sort.setTextColor(getResources().getColor(R.color.snazlgray));
						filesAdapter = new FilesAdapter(context, filesList);
						listView.setAdapter(filesAdapter);
						filesAdapter.notifyDataSetChanged();
					}
				});
				type_sort.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						sortorder="type";
						date_sort.setTextColor(getResources().getColor(R.color.snazlgray));
						alpha_sort.setTextColor(getResources().getColor(R.color.snazlgray));
						type_sort.setTextColor(getResources().getColor(R.color.white));
						filesAdapter = new FilesAdapter(context, getSortType(filesList));
						listView.setAdapter(filesAdapter);
						filesAdapter.notifyDataSetChanged();
					}
				});

				tf_regular = Typeface.createFromAsset(context.getAssets(),
						getResources().getString(R.string.fontfamily));
		        tf_bold = Typeface.createFromAsset(context.getAssets(),
						getResources().getString(R.string.fontfamilybold));

				btnShareAll = (Button) view.findViewById(R.id.shareAll);
				btnShareAll.setOnClickListener(this);
				btnDeleteAll = (Button) view.findViewById(R.id.deleteAll);
				btnDeleteAll.setOnClickListener(this);
				listView = (SwipeMenuListView) view.findViewById(R.id.filesList);
				listView.setTextFilterEnabled(true);
				listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				btnContainer = (LinearLayout) view
						.findViewById(R.id.button_container);
				btnContainer.setOnClickListener(this);
				filesList = loadFiles(CallDispatcher.LoginUser);
				Log.i("files123", "fileslist : " + filesList.size());
				filesAdapter = new FilesAdapter(context, filesList);
				listView.setAdapter(filesAdapter);
				filesAdapter.notifyDataSetChanged();
				text_show = (TextView) view.findViewById(R.id.emptytext);
				text_show.setText(SingleInstance.mainContext.getResources().getString(R.string.no_filess)
						+ SingleInstance.mainContext.getResources().getString(R.string.create_different_file));
				text_show.setTextSize(15);
				text_show.setGravity(Gravity.CENTER_HORIZONTAL);
				text_show.setTextColor(getResources().getColor(R.color.black));

				if(isfrommemeory){
					filesAdapter.Memeorycontrol(FileType);
					filesAdapter = new FilesAdapter(context, filesList);
					listView.setAdapter(filesAdapter);
					filesAdapter.notifyDataSetChanged();
					if (FileType.equalsIgnoreCase("audio")) {
						rl_file.setVisibility(View.VISIBLE);
						tv_file.setText("Audio Files");
						file_img.setBackgroundResource(R.drawable.audio_p);
					} else if (FileType.equalsIgnoreCase("photo")) {
						rl_file.setVisibility(View.VISIBLE);
						tv_file.setText("Photo Files");
						file_img.setBackgroundResource(R.drawable.photo_p);
					} else if (FileType.equalsIgnoreCase("video")) {
						rl_file.setVisibility(View.VISIBLE);
						tv_file.setText("Video Files");
						file_img.setBackgroundResource(R.drawable.video_p);
					} else {
						rl_file.setVisibility(View.VISIBLE);
						tv_file.setText("Other Files");
						file_img.setBackgroundResource(R.drawable.other_p);
					}
				}
				RelativeLayout audio_minimize = (RelativeLayout)getActivity().findViewById(R.id.audio_minimize);
				RelativeLayout video_minimize = (RelativeLayout)getActivity().findViewById(R.id.video_minimize);
				audio_minimize.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mainHeader.setVisibility(View.GONE);
						addShowHideListener(true);
					}
				});
				video_minimize.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mainHeader.setVisibility(View.GONE);
						addShowHideListener(false);
					}
				});
				ImageView min_incall=(ImageView)getActivity().findViewById(R.id.min_incall);
				ImageView min_outcall=(ImageView)getActivity().findViewById(R.id.min_outcall);
				min_incall.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mainHeader.setVisibility(View.GONE);
						inCommingCallAlert incommingCallAlert = inCommingCallAlert.getInstance(SingleInstance.mainContext);
						FragmentManager fragmentManager = SingleInstance.mainContext
								.getSupportFragmentManager();
						fragmentManager.beginTransaction().replace(
								R.id.activity_main_content_fragment, incommingCallAlert)
								.commitAllowingStateLoss();
					}
				});
				min_outcall.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mainHeader.setVisibility(View.GONE);
						CallConnectingScreen callConnectingScreen = CallConnectingScreen.getInstance(SingleInstance.mainContext);
						FragmentManager fragmentManager = SingleInstance.mainContext
								.getSupportFragmentManager();
						fragmentManager.beginTransaction().replace(
								R.id.activity_main_content_fragment, callConnectingScreen)
								.commitAllowingStateLoss();
					}
				});

				isEdit = false;
				// isSelectall = false;
				listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
					@Override
					public void onSwipeStart(int position) {
						Log.d("Swiselect", "onSwipeStart : " + position);
					}

					@Override
					public void onSwipeEnd(int position) {

					}
				});
				SwipeMenuCreator creator = new SwipeMenuCreator() {

					@Override
					public void create(SwipeMenu menu) {
						// Create different menus depending on the view type
								createMenu(menu);

					}
					private void createMenu(SwipeMenu menu) {
						SwipeMenuItem forwared = new SwipeMenuItem(context);
						forwared.setBackground(R.color.blue2);
						forwared.setWidth(dp2px(90));
						forwared.setIcon(R.drawable.withdraw_line_white);
						forwared.setTitleSize(10);
						forwared.setTitle("DELETE");
						forwared.setTitleColor(Color.WHITE);
						menu.addMenuItem(forwared);
					}
				};
				listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(int position,
												   SwipeMenu menu, int index) {

						try {
							CompleteListBean cBean = filesAdapter
									.getItem(position);
							deleteNote(cBean, context);
							return false;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return false;
						}
					}
				});
				listView.setMenuCreator(creator);
				ed_search.addTextChangedListener(new TextWatcher() {

					public void afterTextChanged(Editable s) {
					}

					public void beforeTextChanged(CharSequence s, int start,
												  int count, int after) {
					}

					public void onTextChanged(CharSequence s, int start,
											  int before, int count) {

						String text = ed_search.getText().toString()
								.toLowerCase(Locale.getDefault());
						filesAdapter.filter(text);
						if (filesAdapter.getCount() == 0) {
							ShowToast(SingleInstance.mainContext.getResources().getString(R.string.no_result_found));
						}
						if (text.contains("a")) {
							rl_file.setVisibility(View.VISIBLE);
							tv_file.setText("Audio Files");
							file_img.setBackgroundResource(R.drawable.audio_p);
						} else if (text.contains("p")) {
							rl_file.setVisibility(View.VISIBLE);
							tv_file.setText("Photo Files");
							file_img.setBackgroundResource(R.drawable.photo_p);
						} else if (text.contains("v")) {
							rl_file.setVisibility(View.VISIBLE);
							tv_file.setText("Video Files");
							file_img.setBackgroundResource(R.drawable.video_p);
						} else {
							rl_file.setVisibility(View.VISIBLE);
							tv_file.setText("Other Files");
							file_img.setBackgroundResource(R.drawable.other_p);
						}
						if (text.length() == 0)
							rl_file.setVisibility(View.GONE);
					}
				});
				clearAllBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						syncFiles();
					}
				});

				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						try {


							CompleteListBean cBean = filesAdapter
									.getItem(position);
							getlist = position;
							viewFileInfo(cBean, position);

							// }
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

				selectall.setVisibility(View.GONE);
//				selectall.setBackgroundResource(R.drawable.ic_action_select_all);
				selectall.setText(SingleInstance.mainContext.getResources().getString(R.string.select_all_filess2));
				selectall.setBackgroundColor(getResources().getColor(R.color.background));
				selectall.setTextColor(getResources().getColor(R.color.white));
				selectall.setTag(true);
				selectall.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							if ((Boolean) v.getTag()) {
								for (CompleteListBean fileBean : filesList) {
									fileBean.setSelected(true);
								}
								v.setTag(false);
							} else {
								for (CompleteListBean fileBean : filesList) {
									fileBean.setSelected(false);
								}
								v.setTag(true);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							if (AppReference.isWriteInFile)
								AppReference.logger.error(e.getMessage(), e);
							else
								e.printStackTrace();
						}
						filesAdapter.notifyDataSetChanged();
					}
				});

				btn_edit.setOnClickListener(this);
				btn_edit.setVisibility(View.GONE);
				btn_edit.setText(SingleInstance.mainContext.getResources().getString(R.string.edit));
				btn_edit.setTextColor(Color.parseColor("#ffffff"));
				btn_edit.setBackgroundResource(R.color.background);
				btn_edit.setTag("edit");

				if (filesAdapter.getCount() > 0 || filesList.size() > 0) {
					Log.i("files123", "fileslist 2 : " + filesList.size());
					listView.setVisibility(View.VISIBLE);
//					btn_edit.setVisibility(View.VISIBLE);
//					ed_search.setVisibility(View.VISIBLE);
					text_show.setVisibility(View.GONE);
				}

				plus = (Button) getActivity().findViewById(R.id.add_group);
				plus.setVisibility(View.VISIBLE);
				plus.setText("");
				plus.setBackgroundResource(R.drawable.navigation_search);
				plus.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!isSearch) {
							plus.setBackgroundResource(R.drawable.navigation_close);
							title.setVisibility(View.GONE);
//							clearAllBtn.setVisibility(View.GONE);
							ed_search.setVisibility(View.VISIBLE);
							isSearch = true;
						} else {
							plus.setBackgroundResource(R.drawable.navigation_search);
							title.setVisibility(View.VISIBLE);
//                            clearAllBtn.setVisibility(View.VISIBLE);
							ed_search.setVisibility(View.GONE);
							ed_search.setText("");
							InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(ed_search.getWindowToken(), 0);
							isSearch = false;
						}
					}
				});
				Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
				backBtn.setVisibility(View.GONE);
				if (CallDispatcher.LoginUser != null) {
					if (username.equalsIgnoreCase(CallDispatcher.LoginUser)
							&& !SingleInstance.myOrder) {
						title.setText("SNAZBOX FILES");
					} else {
						String localeString = Locale.getDefault().getLanguage();
						if (localeString.equals("ta")) {
							title.setText(username + " "
									+ getResources().getString(R.string.files));
						} else {
							title.setText(username + "'s Files");
						}
					}
				}
				Button selectall = (Button) getActivity().findViewById(
						R.id.btn_brg);
				selectall.setVisibility(View.GONE);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} else {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}

	public void closeOpenedDialogs(){
		if(plus_dialog != null){
			if(plus_dialog.isShowing()) {
				plus_dialog.dismiss();
			}
		}
	}

	public String getUsername(String username) {
		return this.username = username;
	}

	/**
	 * When the user received the share reminder request from the buddies
	 * changed the buddy icon color to get the attention of the user
	 * 
	 * @param sr
	 */

	public void showShareRemainderRequest() {
		try {
			CallDispatcher.reminderArrives = true;
			Log.d("xp wsd", "remainder ");

			for (int i = 0; i < WebServiceReferences.shareRemainderArray.size(); i++) {
				ShareReminder sr = WebServiceReferences.shareRemainderArray
						.get(i);

				Log.d("sr","from :"+sr.getFrom());
				Log.d("sr","to :"+sr.getTo());
				Log.d("sr","id :"+sr.getId());
				Log.d("sr","fileid :"+sr.getFileid());
				Log.d("sr","filelocation :"+sr.getFileLocation());
				Log.d("sr","remainder status:"+sr.getReminderStatus());
				Log.d("sr","remainderdate :"+sr.getReminderdatetime());
				Log.d("sr","remainder response :"+sr.getReminderResponseType());
				TimeZone tz = Calendar.getInstance().getTimeZone();
				TimeZone z = TimeZone.getTimeZone(sr.getRemindertz());

				if ((sr.getRemindertz().trim().length() != 0)
						&& sr.getReminderdatetime().trim().length() != 0) {
					try {
						String strTime = convTimeZone(sr.getReminderdatetime(),
								z.getDisplayName(), tz.getID());
						Log.d("thread", "remainder tz ondd " + strTime);

						sr.setReminderdatetime(strTime);
						Log.i("thread", "After Converting : " + strTime);
					} catch (Exception e1) {
						Log.e("thread", "EXc:" + e1.getMessage());
						Log.i("callhistory", "" + e1.getMessage());
					}

				} else if (sr.getReminderdatetime().trim().length() != 0) {
					try {

						String dateString1 = sr.getReminderdatetime();
						Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm")
								.parse(dateString1);
						String dateString2 = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm").format(date);
						sr.setReminderdatetime(dateString2);
					} catch (Exception e) {
						Log.i("callhistory", "" + e.getMessage());
					}
				}
				if (sr.getStatus().equalsIgnoreCase("new")) {

//					if (!sr.getDownloadType().equals("2")) {
//						
//						callDisp.downloadOfflineresponse(sr.getFileLocation(),
//								null, "files", sr);
//
//					} else {
//						createDBentryforStreaming(sr);
//					}
//					String filename=sr.getFileLocation();
//
//					byte[] decodedString = Base64.decode(filename, Base64.DEFAULT);
//					Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//					File img_file = new File(filename);
//					img_file.createNewFile();
//					FileOutputStream image_writter = new FileOutputStream(
//							img_file);
//					image_writter.write(decodedString);
//					image_writter.flush();
//					image_writter.close();
//					image_writter = new FileOutputStream(img_file);
//					decodedByte.compress(Bitmap.CompressFormat.JPEG, 75, image_writter);
//					decodedByte.recycle();
//					decodedByte = null;
//					image_writter.flush();
//					image_writter.close();
//
//					// img_file = null;
//					if (img_file.exists()) {}
//					String imagefile=img_file.getName();

////

//					Log.i("imagefile","imagefile="+imagefile);


//
//					    callDisp.downloadOfflineresponse(sr.getFileLocation(),
//       null, "files", sr);
					AppReference.filedownload.put(sr.getFileLocation(),sr);
					Log.i("XP WSD","username1="+CallDispatcher.LoginUser);
					Log.i("XP WSD","password1="+CallDispatcher.Password);
					Log.i("XP WSD","filelocation1=" + sr.getFileLocation());
					callDisp.downloadFile(CallDispatcher.LoginUser,CallDispatcher.Password,sr.getFileLocation());
//					callDisp.downloadFile(username,sr.getFileLocation());
				}
				KeepAliveBean aliveBean = callDisp
						.getKeepAliveBean(sr.getId(),
								"accepted");
				aliveBean.setKey("0");
				WebServiceReferences.webServiceClient.heartBeat(aliveBean);
			}
			WebServiceReferences.shareRemainderArray.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createDBentryforStreaming(final ShareReminder share) {

		try {
			if (callDisp.getToneEnabled()) {
				SharedPreferences pref = PreferenceManager
						.getDefaultSharedPreferences(context);
				String file = pref.getString("sharetone", null);
				if (file != null) {
					callDisp.playShareTone(file);
				}
			}

			if (callDisp.isApplicationInBackground(context)) {

				if (share.getType().equalsIgnoreCase("video")) {
					if (callDisp.notifier == null)
						callDisp.notifier = new BackgroundNotification(context);
					String Fullname = "";
					for(BuddyInformationBean bean:ContactsFragment.getBuddyList()){
						if(bean.getName().equalsIgnoreCase(share.getFrom())){
							Fullname = bean.getFirstname()+" "+bean.getLastname();
						}
					}
					callDisp.notifier.ShowNotification(Fullname
							+ " Shared Video note with you", context
							.getResources().getString(R.string.app_name),
							"share");

				}
			}

			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			String url = preferences.getString("url", null);

			String loginIP = url.substring(url.indexOf("://") + 3);
			Log.d("logip", "login ip" + loginIP);
			if (loginIP.contains(":")) {
				loginIP = loginIP.substring(0, loginIP.indexOf(":"));
				Log.d("logip", "login ip" + loginIP);
				loginIP = loginIP.trim();
			} else if (loginIP.contains("/")) {
				loginIP = loginIP.substring(0, loginIP.indexOf("/"));
				Log.d("logip", "login ip" + loginIP);
				loginIP = loginIP.trim();
			}

			String cmpurl = "http://" + loginIP + "/uploads/" + share.getFrom()
					+ "/" + share.getFileLocation();

			ContentValues cv = new ContentValues();
			String vanishMode = null;
			String vanishValue = null;
			if (share.getVanishMode() != null) {
				if (share.getVanishMode().equalsIgnoreCase("elapse")) {
					try {
						SimpleDateFormat dateFormat = new SimpleDateFormat(
								"dd/MM/yyyy hh:mm aa");
						DateFormatSymbols dfSym = new DateFormatSymbols();
						dfSym.setAmPmStrings(new String[] { "am", "pm" });
						dateFormat.setDateFormatSymbols(dfSym);
						Calendar cal = Calendar.getInstance();
						String currnetDate = dateFormat.format(new Date());
						Date newDate = dateFormat.parse(currnetDate);
						cal.setTime(newDate);
						cal.add(Calendar.HOUR,
								Integer.valueOf(share.getVanishValue()));
						vanishValue = dateFormat.format(cal.getTime());
					} catch (Exception e) {
						Log.e("files", "===> " + e.getMessage());
						e.printStackTrace();
					}
				} else {
					vanishValue = share.getVanishValue();
				}
				vanishMode = share.getVanishMode();
			}

			cv.put("componenttype", share.getType());
			cv.put("componentpath", cmpurl);
			cv.put("ftppath", "");
			cv.put("componentname", "Video");
			cv.put("fromuser", share.getFrom());
			cv.put("comment", share.getShareComment());
			cv.put("reminderstatus", 0);
			cv.put("owner", CallDispatcher.LoginUser);
			cv.put("vanishmode", vanishMode);
			cv.put("vanishvalue", vanishValue);
			cv.put("receiveddateandtime", getNoteCreateTimeForFiles());
			cv.put("reminderdateandtime", share.getReminderdatetime());
			cv.put("viewmode", 0);
			cv.put("reminderresponsetype", "");
			if (share.getBstype() != null) {
				cv.put("bscategory", share.getBstype());
			} else {

				cv.put("bscategory", "");

			}
			if (share.getBsstatus() != null) {
				cv.put("bsstatus", share.getBsstatus());
			} else {

				cv.put("bsstatus", "");

			}
			if (share.getDirection() != null) {
				cv.put("bsdirection", share.getDirection());
			} else {

				cv.put("bsdirection", "");

			}
			if (share.getParent_id() != null) {
				cv.put("parentid", share.getParent_id());
			} else {

				cv.put("parentid", "");
			}

			if (share.getReminderdatetime() != null
					&& share.getReminderdatetime().length() > 0)
				cv.put("reminderstatus", 1);
			else
				cv.put("reminderstatus", 0);

			if (share.getRemindertz() != null)
				cv.put("reminderzone", share.getRemindertz());
			else
				cv.put("reminderzone", "");
			if (share.getBstype() != null) {
				cv.put("bscategory", share.getBstype());
			} else {

				cv.put("bscategory", "");

			}
			if (share.getBsstatus() != null) {
				cv.put("bsstatus", share.getBsstatus());
			} else {

				cv.put("bsstatus", "");

			}
			if (share.getDirection() != null) {
				cv.put("bsdirection", share.getDirection());
			} else {

				cv.put("bsdirection", "");

			}
			if (share.getParent_id() != null) {
				cv.put("parentid", share.getParent_id());
			} else {

				cv.put("parentid", "");
			}
			int id = callDisp.getdbHeler(context).insertComponent(cv);

			final Components cmptPro = new Components();
			cmptPro.setComponentId(id);
			cmptPro.setcomponentType("video");
			cmptPro.setContentPath(cmpurl);
			cmptPro.setContentName("Video");
			cmptPro.setComment(share.getShareComment());
			cmptPro.setDateAndTime(getNoteCreateTimeForFiles());
			cmptPro.setfromuser(share.getFrom());
			cmptPro.setRemDateAndTime(share.getReminderdatetime());
			cmptPro.SetReminderStatus(0);
			cmptPro.setViewMode(0);
			cmptPro.setRemDateAndTime(share.getReminderdatetime());
			cmptPro.setresponseTpe(share.getReminderResponseType());
			cmptPro.setVanishMode(share.getVanishMode());
			cmptPro.setVanishValue(share.getVanishValue());
			cmptPro.setBstype(share.getBstype());
			cmptPro.setBsstatus(share.getBsstatus());
			cmptPro.setDirection(share.getDirection());
			cmptPro.setParent_id(share.getParent_id());

			if (callDisp.getdbHeler(context).insertComponent(cv) > 0) {

				if (CallDispatcher.LoginUser != null && callDisp.isConnected) {
					KeepAliveBean aliveBean = callDisp.getKeepAliveBean(
							share.getId(), "accepted");
					aliveBean.setKey("0");
					WebServiceReferences.webServiceClient.heartBeat(aliveBean);

				}

				WebServiceReferences.pendingShare -= 1;
				if (share.getReminderStatus().equalsIgnoreCase("true")) {
					String strDateTime = share.getReminderdatetime();
					String strQuery = "update component set reminderdateandtime='"
							+ strDateTime
							+ "',reminderstatus=1 where componentid="
							+ cmptPro.getComponentId();

					if (callDisp.getdbHeler(context).ExecuteQuery(strQuery)) {

						Intent reminderIntent = new Intent(context,
								ReminderService.class);
						context.startService(reminderIntent);
					}
				}

				handler.post(new Runnable() {

					@Override
					public void run() {

						if (callDisp.getdbHeler(context).isRecordExists(
								"select * from component where componentid="
										+ cmptPro.getComponentId() + "")) {
							SingleInstance.mainContext.notifyFileDownloaded(
									cmptPro, share.getType(),
									cmptPro.getDateAndTime());
						}
					}
				});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void syncFiles() {
//        AppMainActivity.cBean.getRouter().split(":")[0]
		Log.d("SYNC", "===>syncFiles()");
		try {
			final AlertDialog.Builder alert = new AlertDialog.Builder(
					context);
			alert.setTitle("Alert");
			alert.setMessage("Are you sure want to syncronous all files?");
			alert.setCancelable(false);
			alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					dialogInterface.dismiss();
					handler.post(new Runnable() {
						@Override
						public void run() {
							if (WebServiceReferences.running) {
								WebServiceReferences.webServiceClient
										.FileSync(SingleInstance.mainContext);
							}
							showprogress("Syncing in Progress..");
						}

					});
				}
			});
			alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					dialogInterface.dismiss();
				}
			});

			alert.show();
		}catch (Exception e){
			e.printStackTrace();
		}
	}


	public void cancelDialog() {
		try {
			if (progDailog != null) {
				progDailog.dismiss();
				progDailog = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void showprogress(String title) {
		if (progDailog != null) {
			if (progDailog.isShowing()) {
				Log.i("dialog","cancel dialog showprogress");
				cancelDialog();
			}
		}
		progDailog = new ProgressDialog(context);
		progDailog.setCancelable(false);
		progDailog.show();
		progDailog.setMessage(title);
		progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDailog.setProgress(0);
		progDailog.setMax(100);
		//progDailog.show();

	}
	public void notifySyncResult(){
		cancelDialog();
	}

	public View getParentView() {
		return view;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SingleInstance.myOrder = false;
					MemoryProcessor.getInstance().unbindDrawables(view);
					view = null;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	private String convTimeZone(String time, String sourceTZ, String destTZ)
			throws Exception {

		try {
			if (time.trim().contains("-")) {
				time = time.trim().replace("-", "/");
				String[] formate = time.split(" ");
				if (formate != null) {
					if (formate.length > 1) {
						String[] ch = formate[0].split("/");
						if (ch != null) {
							if (ch.length == 3) {
								time = ch[2] + "/" + ch[1] + "/" + ch[0] + " "
										+ formate[1];
							}
						}
					}
				}

			}

			String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
			// String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

			time = time.trim();

			SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
			Log.d("date",
					"#################### cond satisfied" + sdf.parse(time));

			Date specifiedTime = null;
			try {
				if (sourceTZ != null)
					sdf.setTimeZone(TimeZone.getTimeZone(sourceTZ));
				else
					sdf.setTimeZone(TimeZone.getDefault());

				specifiedTime = sdf.parse(time);

			} catch (Exception e1) {
				e1.printStackTrace();
			}

			// switch timezone
			if (destTZ != null)
				sdf.setTimeZone(TimeZone.getTimeZone(destTZ));
			else
				sdf.setTimeZone(TimeZone.getDefault()); // default to server's
			// timezone

			return changetoOurFormat(sdf.format(specifiedTime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private String changetoOurFormat(String strDate) {

		try {
			// create SimpleDateFormat object with source string date format
			SimpleDateFormat sdfSource = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm");

			// parse the string into Date object
			Date date = sdfSource.parse(strDate);

			// create SimpleDateFormat object with desired date format
			SimpleDateFormat sdfDestination = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");

			// parse the date into another format
			strDate = sdfDestination.format(date);

			// System.out
			// .println("Date is converted from dd/MM/yyyy HH:mm format to yyyy-MM-dd HH:mm");
			// System.out.println("Converted date is : " + strDate);

		} catch (ParseException pe) {
			// System.out.println("Parse Exception : " + pe);
		}
		return strDate;

	}

	public static void checkDir() {
		try {
			File folder = new File("/sdcard/COMMedia/");
			if (!folder.exists()) {
				folder.mkdir();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getNoteCreateTime() {
		try {
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh:mma");
			return sdf.format(curDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String getNoteCreateTimeForFiles() {
		try {
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(
					CallDispatcher.dateFormat + " hh:mm aaa");
			return sdf.format(curDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static boolean CheckReminderIsValid(String strDate) {
		try {
			SimpleDateFormat formatter;
			if (strDate.contains("-"))
				formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			else {
				formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
				DateFormatSymbols dfSym = new DateFormatSymbols();
				dfSym.setAmPmStrings(new String[] { "am", "pm" });
				formatter.setDateFormatSymbols(dfSym);
			}
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
			e.printStackTrace();
			return false;
		}
	}

	public static String getFileName() {
		String strFilename = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
			Date date = new Date();
			strFilename = dateFormat.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strFilename;
	}

	private String getUsernameFromPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String username = sharedPreferences.getString("uname", null);
		try {
			if (username == null)
				username = "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return username;

	}

	public void notifyCallHistoryToServer(String from, String[] to,
			String callType, String sessinId, String startTime[],
			String endTime[], String[] autoCall) {
		try {

			ArrayList<CallHistoryBean> arrayCallHistory = new ArrayList<CallHistoryBean>();

			for (int i = 0; i < to.length; i++) {

				CallHistoryBean chb = new CallHistoryBean();
				chb.setFrom(from);
				chb.setTo(to[i]);
				chb.setSessionId(sessinId);
				chb.setStime(startTime[i]);
				chb.setEtime(endTime[0]);
				chb.setType(callType);
				chb.setAutoCall(autoCall[i]);

				chb.setNetworkState(Integer
						.toString(CallDispatcher.networkState));
				chb.setLoginUserName(callDisp.LoginUser);
				arrayCallHistory.add(chb);

			}

			// callDisp.callHistoryMap.put(sessinId, arrayCallHistory);
		} catch (Exception e) {
			Log.i("callhistory", "" + "" + e.getMessage());
		}

	}

	// @Override
	// public void onDetachedFromWindow() {
	// // TODO Auto-generated method stub
	// try {
	// super.onDetachedFromWindow();
	// } catch (IllegalArgumentException e) {
	// // TODO: handle exception
	// Log.i("callhistory", "" + e.getMessage());
	// }
	// }

	// public void changemenuitems() {
	//
	// try {
	// Log.i("welcome",
	// "enable user changemenuitems() called---->complete list view");
	//
	// Slidebean user = new Slidebean();
	// if (CallDispatcher.LoginUser != null) {
	// user.setTitle(CallDispatcher.LoginUser);
	// } else {
	// user.setTitle("");
	// }
	// user.setId(CONTACTSTITLE);
	// slidemenu.setBean(0, user);
	//
	// Slidebean sm16 = new Slidebean();
	// sm16.setTitle("Signout");
	// sm16.setId(SIGNIN);
	// slidemenu.setBean(19, sm16);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public String getOwner() {
		return this.owner;
	}

	public void storeInLocalFolder() {
		try {
			Intent intent = getActivity().getIntent();

			InputStream is = null;

			FileOutputStream os = null;

			String fullPath = null;

			try {
				String action = intent.getAction();

				if (!Intent.ACTION_VIEW.equals(action))
					return;

				Uri uri = intent.getData();
				String scheme = uri.getScheme();
				String name = null;

				if (scheme.equals("file")) {
					List<String> pathSegments = uri.getPathSegments();

					if (pathSegments.size() > 0)
						name = pathSegments.get(pathSegments.size() - 1);
					Log.i("POIS", " file name ===>" + name);

				}

				else if (scheme.equals("content")) {
					Cursor cursor = getActivity()
							.getContentResolver()
							.query(uri,
									new String[] { MediaStore.MediaColumns.DISPLAY_NAME },
									null, null, null);

					cursor.moveToFirst();

					int nameIndex = cursor
							.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
					if (nameIndex >= 0)
						name = cursor.getString(nameIndex);
					Log.i("POIS", " content name ===>" + name);

				}

				else
					return;

				if (name == null)
					return;

				int n = name.lastIndexOf(".");
				String fileName, fileExt;

				if (n == -1)
					return;

				else {
					fileName = name.substring(0, n);
					fileExt = name.substring(n);
					if (!fileExt.equals(".docx") && !fileExt.equals(".pdf")
							&& !fileExt.equals("doc") && !fileExt.equals("xls")
							&& !fileExt.equals("xlsx"))

						return;

				}

				fullPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + fileName + fileExt;
				Log.i("POIS", "FULL PATH===>" + fullPath);
				is = getActivity().getContentResolver().openInputStream(uri);
				os = new FileOutputStream(fullPath);

				byte[] buffer = new byte[4096];
				int count;

				while ((count = is.read(buffer)) > 0)
					os.write(buffer, 0, count);

				os.close();
				is.close();

			}

			catch (Exception e) {
				if (is != null) {
					try {
						is.close();
					}

					catch (Exception e1) {
					}
				}

				if (os != null) {
					try {
						os.close();
					}

					catch (Exception e1) {
					}
				}

				if (fullPath != null) {
					File f = new File(fullPath);

					f.delete();
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void openFilesinExternalApp(String filename) {

		try {
			// TODO Auto-generated method stub

			String[] name = filename.split("\\.");

			String extn = name[1];

//			 if (extn.equals("pdf") || extn.equals("doc") ||
//			 extn.equals("docs")|| extn.equals("txt")
//			 || extn.equals("docx") || extn.equals("odt")
//			 || extn.equals("xls") || extn.equals("xlsx")
//			 || extn.equals("ppt") || extn.equals("pptx")
//			 || extn.equalsIgnoreCase("php") || extn.equals("psd")) {

			File files = null;
			String filepath;
			if (!filename.contains(""
					+ Environment.getExternalStorageDirectory())) {
				filepath = new String(Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + filename);
			} else {
				filepath = filename;
			}
			files = new File(filepath);
			Log.i("AAAA", "FILESFRAGMENT  " + files);

			if (files.exists()) {
				Uri path = Uri.fromFile(files);
				try {
					if (extn.equals("txt")) {
						Intent intent = new Intent(context, WebView_doc.class);
						intent.putExtra("path", path.toString());
						//intent.putExtra("exten", extn);
						context.startActivity(intent);
					} else {
						Log.i("pin","open Action_view");
                        AppReference.fileOpen=true;
						MimeTypeMap mime = MimeTypeMap.getSingleton();
						Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						//intent.setDataAndType(path, "*/*");
						String type = mime.getMimeTypeFromExtension(extn);
						intent.setDataAndType(path, type);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
						context.startActivity(intent);

					}


//			try {
//						context.startActivity(intent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(context,
							"No Application Available to View this file",
							Toast.LENGTH_SHORT).show();
				}
			}
			// }
			/***
			 * Ends here
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// public void notifyProfilepictureDownloaded() {
	// handler.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// if (slidemenu != null) {
	// if (slidemenu.isMenuShowing())
	// slidemenu.refreshItem();
	// }
	// }
	// });

	// }

	// @Override
	// public void onSlideMenuItemClick(int itemId, View v, Context context) {
	// // TODO Auto-generated method stub
	// switch (itemId) {
	// case WebServiceReferences.CONTACTS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// break;
	// case WebServiceReferences.USERPROFILE:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// break;
	// case WebServiceReferences.UTILITY:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// break;
	// case WebServiceReferences.NOTES:
	//
	// break;
	// case WebServiceReferences.APPS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// break;
	// case WebServiceReferences.CLONE:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// break;
	// case WebServiceReferences.SETTINGS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// break;
	//
	// case WebServiceReferences.QUICK_ACTION:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// break;
	// case WebServiceReferences.FORMS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// break;
	// case WebServiceReferences.FEEDBACK:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// break;
	// case WebServiceReferences.EXCHANGES:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// break;
	// default:
	// break;
	// }
	// }

	// @Override
	// public void notifyReceivedIM(final SignalingBean sb) {
	// // TODO Auto-generated method stub
	// handler.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// IMRequest.setVisibility(View.VISIBLE);
	// IMRequest.setEnabled(true);
	//
	// IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);
	//
	// if (!callDisp.getdbHeler(context).userChatting(sb.getFrom())) {
	// callDisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
	// CallDispatcher.LoginUser, 1,
	// CallDispatcher.LoginUser);
	// }
	//
	// }
	// });
	//
	// }

	final Runnable delayProcessrunnable = new Runnable() {
		public void run() {
			try {
				openAutoPlayScreen();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private void openAutoPlayScreen() {
		try {
			try {
				if (autoPlayType != null
						&& autoPlayType.equalsIgnoreCase("audio")) {
					Components cmpts = WebServiceReferences.llAutoPlayContent
							.removeFirst();
					Intent intent = new Intent(context, MultimediaUtils.class);
					intent.putExtra("filePath", cmpts.getContentPath());
					intent.putExtra("requestCode", 2);
					intent.putExtra("action", "audio");
					intent.putExtra("createOrOpen", "open");
					startActivity(intent);
				} else if (autoPlayType != null
						&& autoPlayType.equalsIgnoreCase("video")) {
					Components cmpts = WebServiceReferences.llAutoPlayContent
							.removeFirst();
					Intent intentVPlayer = new Intent(context,
							VideoPlayer.class);
					// intentVPlayer.putExtra("File_Path",
					// cmpts.getContentPath());
					// intentVPlayer.putExtra("Player_Type", "Video Player");
					// intentVPlayer.putExtra("time", 0);
					intentVPlayer.putExtra("video", cmpts.getContentPath());
					startActivity(intentVPlayer);
				}
				if (alertDelay != null) {
					if (alertDelay.isShowing()) {
						alertDelay.dismiss();

					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			autoPlayType = null;
			alertDelay = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		try {
			Log.i("files12345", "refresh");
			boolean isSelected = false;
			for (CompleteListBean cBean : filesList) {
				Log.i("files123", "files : " + cBean.isSelected());
				if (cBean.isSelected()) {
					isSelected = true;
					break;
				}
			}
			if (!isSelected && !isfrommemeory) {
				filesListRefresh();
			}
			if (listView.getAdapter().getCount() > 0)
				listView.setSelection(getlist);

			super.onResume();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("file", "exception ===> " + e.getMessage());
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		
		SingleInstance.mainContext.updateFileCount();

	}

	int count = 1;

	private Vector<CompleteListBean> loadFiles(String username) {
		if (CallDispatcher.LoginUser != null) {

			if (SingleInstance.myOrder)

			{

				if (username.equalsIgnoreCase(CallDispatcher.LoginUser)) {
					strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype,bscategory,bsstatus,bsdirection,parentid from component where (parentid IS NOT NULL  and parentid!='') and TRIM(parentid) and componenttype!='IM' and componenttype!='call' and owner='"
							+ username + "'ORDER BY receiveddateandtime DESC";
				} else {
					strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype,bscategory,bsstatus,bsdirection,parentid from component where (parentid IS NOT NULL  and parentid!='')  and TRIM(parentid) and componenttype!='IM' and componenttype!='call' and fromuser='"
							+ username + "'ORDER BY receiveddateandtime DESC";
				}
				filesList = DBAccess.getdbHeler().getCompleteListProperties(
						strCompleteListQuery);
				Log.i("files123", "files list : " + filesList.size());
				tempFilesList = DBAccess.getdbHeler()
						.getCompleteListProperties(strCompleteListQuery);

			} else {
				if (username.equalsIgnoreCase(CallDispatcher.LoginUser)) {
					strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype,bscategory,bsstatus,bsdirection,parentid from component where (parentid IS NULL or parentid = '') and componenttype!='IM' and componenttype!='call' and owner='"
							+ username + "'ORDER BY receiveddateandtime DESC";
				} else {
					strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype,bscategory,bsstatus,bsdirection,parentid from component where (parentid IS NULL or parentid = '') and componenttype!='IM' and componenttype!='call' and fromuser='"
							+ username + "'ORDER BY receiveddateandtime DESC";
				}
				filesList = DBAccess.getdbHeler().getCompleteListProperties(
						strCompleteListQuery);
				Log.i("files123", "files list : " + filesList.size());
				tempFilesList = DBAccess.getdbHeler()
						.getCompleteListProperties(strCompleteListQuery);

			}
		} else {

			strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype,bscategory,bsstatus,bsdirection,parentid from component where parentid IS NULL or parentid = '' and componenttype!='IM' and componenttype!='call' and owner='"
					+ username + "' ORDER BY receiveddateandtime DESC";
			filesList = DBAccess.getdbHeler().getCompleteListProperties(
					strCompleteListQuery);
			Log.i("files123", "files list : " + filesList.size());
			tempFilesList = DBAccess.getdbHeler().getCompleteListProperties(
					strCompleteListQuery);
		}
		return filesList;
	}

	public void notifyUI() {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					filesAdapter.notifyDataSetChanged();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void changeReadStatus(View view) {
		ImageView read = (ImageView) view.findViewById(R.id.cr_icon);
		read.setVisibility(View.GONE);
	}

	public void viewFileInfo(CompleteListBean cBean, int position) {
		try {
			if (cBean.getViewmode() == 0) {
				String strUpdateNote = "update component set viewmode='" + 1
						+ "' where componentid='" + cBean.getComponentId()
						+ "'";
				Log.d("qry", strUpdateNote);

				if (DBAccess.getdbHeler().ExecuteQuery(strUpdateNote)) {
					cBean.setViewMode(1);
				}
			}
			FileInfoFragment fileInfoFragment = FileInfoFragment.newInstance(context);
			FragmentManager fragmentManager = SingleInstance.mainContext
					.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(
					R.id.activity_main_content_fragment, fileInfoFragment)
					.commitAllowingStateLoss();
			callDisp.cmp = cBean;
			fileInfoFragment.setFileBean(cBean);
			fileInfoFragment.setFrom(false,"",false);
//			Intent intentComponent = new Intent(context, ComponentCreator.class);
//			Bundle bndl = new Bundle();
//			bndl.putString("type", cBean.getcomponentType());
//			bndl.putBoolean("action", false);
//			bndl.putInt("position", position);
//			bndl.putBoolean("fromNew",false);
//			intentComponent.putExtra("viewBean", cBean);
//			intentComponent.putExtras(bndl);
//			callDisp.cmp = cBean;
//			startActivity(intentComponent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void deleteNote(final CompleteListBean cBean, Context context1) {
		try {
			String message = null;
			message = "Are you sure to delete this'" + cBean.getContentName()
					+ "'?";

			AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(
					context1);
			deleteConfirmation.setTitle("Delete Confirmation");
			deleteConfirmation.setMessage(message);
			deleteConfirmation.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes)
,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {

								String strDeleteQry = "delete from component where componentid="
										+ cBean.getComponentId();

								if (callDisp.getdbHeler(context).ExecuteQuery(
										strDeleteQry)) {
									ShowToast(SingleInstance.mainContext.getResources().getString(R.string.selected_files_deleted));
									filesList.remove(cBean);
									if (cBean.getContentpath() != null
											&& cBean.getContentpath() != "") {
										if (!cBean.getcomponentType()
												.equalsIgnoreCase("video")) {

											File f = new File(cBean
													.getContentpath());
											if (f.exists()) {
												f.delete();
											}

										} else if (cBean.getcomponentType()
												.equalsIgnoreCase("video")) {

											File f = new File(cBean
													.getContentpath() + ".mp4");
											if (f.exists()) {
												f.delete();
											}
											File f1 = new File(cBean
													.getContentpath() + ".jpg");
											if (f1.exists()) {
												f1.delete();
											}

										}
									}
								}

							} catch (Exception e) {
								Log.i("callhistory", "" + e.getMessage());
								if (AppReference.isWriteInFile)
									AppReference.logger.error(e.getMessage(), e);

							}

							if (WebServiceReferences.contextTable
									.containsKey("Component")) {
								((ComponentCreator) WebServiceReferences.contextTable
										.get("Component")).finish();
							}
							if (filesAdapter.getCount() == 0) {
								btn_edit.setVisibility(View.GONE);
								ed_search.setVisibility(View.GONE);
								selectall.setVisibility(View.GONE);
								btnContainer.setVisibility(View.GONE);
								btn_edit.setText("Edit");
								listView.setVisibility(View.GONE);
								text_show.setVisibility(View.VISIBLE);
							} else {
								listView.setVelocityScale(View.VISIBLE);
								ed_search.setVisibility(View.GONE);
								btn_edit.setVisibility(View.GONE);
								text_show.setVisibility(View.VISIBLE);
							}

							notifyUI();
							filesListRefresh();
						}
					});
			deleteConfirmation.setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			deleteConfirmation.show();

			// showSingleSelectBuddy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

	}

	private void deleteMultipleFiles() {

		try {
			final Vector<CompleteListBean> deleteList = new Vector<CompleteListBean>();
			for (CompleteListBean cBean : filesList) {
				if (cBean.isSelected()) {
					deleteList.add(cBean);
				}
			}
			if (deleteList.size() > 0) {
				String message = null;
				message = "Are you sure to delete selected items?";

				AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(
						context);
				deleteConfirmation.setTitle("Delete Confirmation");
				deleteConfirmation.setMessage(message);
				deleteConfirmation.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes)
,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {

									// Vector<CompleteListBean>
									// selectedFilesList = new
									// Vector<CompleteListBean>();
									// for (CompleteListBean bean : filesList) {
									// if (bean.isSelected()) {
									// selectedFilesList.add(bean);
									// }
									// }
									// Set selectedSet = (Set) filesAdapter
									// .getSelectedFiles().entrySet();
									// Log.i("files123", "selected size1 : "
									// + selectedSet.size());
									// Iterator mapIterator = selectedSet
									// .iterator();
									// while (mapIterator.hasNext()) {
									// Map.Entry mapEntry = (Map.Entry)
									// mapIterator
									// .next();
									// CompleteListBean clBean =
									// (CompleteListBean) mapEntry
									// .getValue();
									// selectedFilesList.add(clBean);
									// }
									// Log.i("files123",
									// "selected file size2 : "
									// + selectedFilesList.size());
									for (CompleteListBean bean : deleteList) {
										String strDeleteQry = "delete from component where componentid="
												+ bean.getComponentId();
										if (DBAccess.getdbHeler(context)
												.ExecuteQuery(strDeleteQry)) {
											Log.i("files123",
													"deleted file id : "
															+ bean.getComponentId());

											filesList.remove(bean);
											SingleInstance.mainContext
													.notifyUI();
											if (bean.getContentpath() != null
													&& bean.getContentpath()
															.length() > 0) {
												File file = new File(bean
														.getContentpath());
												if (file.exists()) {
													file.delete();
												}
											}
										}
									}

									notifyUI();
									filesListRefresh();
									isEdit = false;
									selectall.setVisibility(View.GONE);
									btn_edit.setText("Edit");
									btnContainer.setVisibility(View.GONE);

								} catch (Exception e) {
									Log.i("callhistory", "" + e.getMessage());
									if (AppReference.isWriteInFile)
										AppReference.logger.error(
												e.getMessage(), e);

								}

								if (WebServiceReferences.contextTable
										.containsKey("Component")) {
									((ComponentCreator) WebServiceReferences.contextTable
											.get("Component")).finish();
								}
								if (filesAdapter.getCount() == 0) {
									btn_edit.setVisibility(View.GONE);
									ed_search.setVisibility(View.GONE);
									selectall.setVisibility(View.GONE);
									btnContainer.setVisibility(View.GONE);
									btn_edit.setText("Edit");
									listView.setVisibility(View.GONE);
									text_show.setVisibility(View.VISIBLE);
								} else {
									listView.setVelocityScale(View.VISIBLE);
									selectall.setVisibility(View.GONE);
									btn_edit.setText("Edit");
									btnContainer.setVisibility(View.GONE);
								}
								filesAdapter.notifyDataSetChanged();
							}
						});
				deleteConfirmation.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				deleteConfirmation.show();
			} else
				ShowToast(SingleInstance.mainContext.getResources().getString(R.string.select_delete_confirm)
);

			// showSingleSelectBuddy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

	}

	private void shareMultipleFiles() {

		try {
			if (CallDispatcher.LoginUser != null) {
				Vector<CompleteListBean> editList = new Vector<CompleteListBean>();
				for (CompleteListBean cBean : filesList) {
					if (cBean.isSelected()) {
						editList.add(cBean);
					}
				}
				if (editList.size() > 0) {
					// Vector<CompleteListBean> selectedFilesList = new
					// Vector<CompleteListBean>();
					// for (CompleteListBean bean : filesList) {
					// if (bean.isSelected()) {
					// selectedFilesList.add(bean);
					// }
					// }
					// Set selectedSet = (Set) filesAdapter.getSelectedFiles()
					// .entrySet();
					//
					// Iterator mapIterator = selectedSet.iterator();
					// while (mapIterator.hasNext()) {
					// Map.Entry mapEntry = (Map.Entry) mapIterator.next();
					// CompleteListBean clBean = (CompleteListBean) mapEntry
					// .getValue();
					// selectedFilesList.add(clBean);
					// }
					for (CompleteListBean bean : editList) {
						if (callDisp.getmyBuddys().length > 0) {
							callDisp.send_multiple = true;
							if (bean.isSelected()) {
								callDisp.multiple_componentlist.add(bean);
							}
						}
					}
					Intent intent = new Intent(context, sendershare.class);
					startActivity(intent);
					if (btnContainer.getVisibility() == View.VISIBLE
							|| selectall.getVisibility() == View.VISIBLE) {
						btnContainer.setVisibility(View.GONE);
						selectall.setVisibility(View.GONE);
						btn_edit.setText("Edit");
						isEdit = false;

					}
				} else
					ShowToast(SingleInstance.mainContext.getResources().getString(R.string.please_select_one_file));
			} else {
				ShowToast("Please login to share files");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

	}

	public void editFileName(CompleteListBean cBean) {
		filesList.remove(cBean);
		filesList.add(cBean);
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				filesAdapter.notifyDataSetChanged();
			}
		});
	}

	public void filesListRefresh() {
		try {
			// TODO Auto-generated method stub
			filesList.clear();
			if (CallDispatcher.LoginUser == null) {
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(context);
				username = sharedPreferences.getString("uname", null);
			}
			// } else {
			// username = CallDispatcher.LoginUser;
			// }
			Log.i("files098", "username : " + username);
			filesList = loadFiles(username);
			Log.i("files098", "fileslist : " + filesList.size());
			Log.i("files123", "fileslist : " + filesList.size());
			filesAdapter = new FilesAdapter(context, filesList);
			if (listView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.files_list, null);
				listView = (SwipeMenuListView) view.findViewById(R.id.filesList);
			}
			listView.setAdapter(filesAdapter);
			filesAdapter.notifyDataSetChanged();
			getCount = listView.getAdapter().getCount();
			if (ed_search != null && filesAdapter.getCount() > 0
					|| filesList.size() > 0) {
				if (btn_edit != null && plus != null) {
					if (!fromContacts) {
						if (btn_edit.getVisibility() != View.VISIBLE) {
							btn_edit.setVisibility(View.GONE);
						}
						if (plus.getVisibility() != View.VISIBLE) {
							plus.setVisibility(View.VISIBLE);
						}
					} else {
						btn_edit.setVisibility(View.GONE);
						plus.setVisibility(View.GONE);
					}
				}
				listView.setVisibility(View.VISIBLE);
				if (ed_search != null) {
					ed_search.setVisibility(View.GONE);
				}
				text_show.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyDownloadFile(final CompleteListBean cBean) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					if (view != null) {
						filesList.add(0,cBean);
						filesAdapter = new FilesAdapter(context, filesList);
						listView.setAdapter(filesAdapter);
						if (listView.getAdapter().getCount() > 0)
							listView.setSelection(listView.getAdapter()
									.getCount() - 1);
						filesAdapter.notifyDataSetChanged();
						if (filesAdapter.getCount() > 0 || filesList.size() > 0) {
							listView.setVisibility(View.VISIBLE);
							btn_edit.setVisibility(View.GONE);
							ed_search.setVisibility(View.GONE);
							text_show.setVisibility(View.GONE);
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		btn_edit.setBackgroundResource(R.color.black);
		switch (v.getId()) {
		case R.id.shareAll:
			btn_edit.setBackgroundResource(R.color.background);
			shareMultipleFiles();
			break;
		case R.id.deleteAll:
			btn_edit.setBackgroundResource(R.color.background);
			deleteMultipleFiles();
			break;
		case R.id.btn_settings:
			if (btnContainer.getVisibility() == View.GONE) {
				//btn_edit.setText("Update");
				selectall.setVisibility(View.VISIBLE);
				btnContainer.setVisibility(View.VISIBLE);
				isEdit = true;
			} else {
				isEdit = false;
				btn_edit.setBackgroundResource(R.color.background);
				btn_edit.setText("Edit");
				selectall.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
			}
			break;
		}

	}

	protected void ShowToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}
	public void componentType(String type,boolean isfrommemorycontrol){
		FileType = type;
		isfrommemeory = isfrommemorycontrol;
	}

	public boolean checkPosition(int position) {
		boolean isPoistion = false;
		try {
			if (filesList.size() - 1 == position) {
				isPoistion = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return isPoistion;
	}

	public boolean setFromContacts(boolean fromContacts) {
		return this.fromContacts = fromContacts;
	}

	class FilesListComparator implements Comparator<CompleteListBean> {

		@Override
		public int compare(CompleteListBean oldBean,
						   CompleteListBean newBean) {
			// TODO Auto-generated method stub
			return (oldBean.getContentName().compareToIgnoreCase(newBean.getContentName()));
		}

	}
	private Vector<CompleteListBean>  getSortType(Vector<CompleteListBean> fileList)
	{
		Vector<CompleteListBean> tempList=new Vector<CompleteListBean>();
		Vector<CompleteListBean> photoList=new Vector<CompleteListBean>();
		Vector<CompleteListBean> videoList=new Vector<CompleteListBean>();
		Vector<CompleteListBean> audioList=new Vector<CompleteListBean>();
		for(CompleteListBean cBean: fileList ) {
			if (cBean.getcomponentType().trim().equalsIgnoreCase("photo")) {
				Log.i("AAAA","sort type photo");
				photoList.add(cBean);
			} else if (cBean.getcomponentType().trim().equalsIgnoreCase("video")) {
				Log.i("AAAA","sort type video");
				videoList.add(cBean);
			} else if (cBean.getcomponentType().trim().equalsIgnoreCase("audio")) {
				Log.i("AAAA","sort type audio");
				audioList.add(cBean);
			}
		}
		if(photoList.size()>0)
			tempList.addAll(photoList);
		if(videoList.size()>0)
			tempList.addAll(videoList);
		if(audioList.size()>0)
			tempList.addAll(audioList);
		return tempList;
	}
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	void addShowHideListener( final Boolean isAudio) {
		if(isAudio) {
			AudioCallScreen audioCallScreen = AudioCallScreen.getInstance(SingleInstance.mainContext);
			FragmentManager fragmentManager = SingleInstance.mainContext
					.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(
					R.id.activity_main_content_fragment, audioCallScreen)
					.commitAllowingStateLoss();
		}else {
			VideoCallScreen videoCallScreen = VideoCallScreen.getInstance(SingleInstance.mainContext);
			FragmentManager fragmentManager = SingleInstance.mainContext
					.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(
					R.id.activity_main_content_fragment, videoCallScreen)
					.commitAllowingStateLoss();
		}
	}

}
