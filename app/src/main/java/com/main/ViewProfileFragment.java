package com.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.lib.model.BuddyInformationBean;
import org.lib.model.FieldTemplateBean;
import org.lib.model.WebServiceBean;
import org.lib.webservice.Servicebean;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.SlideMenu.SlideMenu;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.UpperCaseParse;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.PhotoZoomActivity;
import com.cg.hostedconf.AppReference;
import com.cg.profiles.InviteProfile;
import com.group.chat.GroupChatActivity;
import com.group.chat.ProfessionList;
import com.image.utils.ImageLoader;
import com.process.MemoryProcessor;
import com.util.FullScreenImage;
import com.util.SingleInstance;
import com.util.VideoPlayer;

public class ViewProfileFragment extends Fragment {

	// private Context context = null;

	// private Button btn_notification = null;

	// private Button IMRequest = null;

	private Button btn_cancel = null;

	private TextView title = null;

	// private Handler viewHandler = new Handler();

	public LinearLayout records_container = null;

	private String buddyname = null;

	private String fromActivity = null;
	private Typeface tf_regular = null;
	
	private Typeface tf_bold = null;


	private Bitmap bitmap = null;

	private static CallDispatcher callDisp = null;
	private AppMainActivity appMainActivity=null;

	private Vector<FieldTemplateBean> OtherDetails = new Vector<FieldTemplateBean>();

	private SlideMenu slidemenu = null;

	private MediaPlayer chatplayer = null;

	private boolean isPlaying = false;

	private Button btn_share = null;

//	SipQueue sipQueue;

//	SipCommunicator sipCommunicator;

	private static Context context;

	private static ViewProfileFragment viewProfileFragment;

	public View view;

	private Button edit;

	private ImageLoader imageLoader;
	private Handler handler = new Handler();

	private Button plusBtn = null;

	public static ViewProfileFragment newInstance(Context maincontext) {
		try {
			if (viewProfileFragment == null) {
				context = maincontext;
				viewProfileFragment = new ViewProfileFragment();
				callDisp = CallDispatcher.getCallDispatcher(context);
			}

			return viewProfileFragment;
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return viewProfileFragment;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Button select = (Button) getActivity().findViewById(R.id.btn_brg);
		select.setVisibility(View.VISIBLE);
		select.setBackgroundResource(R.drawable.ic_action_settings);
		select.setText("");
		RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
		mainHeader.setVisibility(View.VISIBLE);
		LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
		contact_layout.setVisibility(View.GONE);
//		LinearLayout footer_layout =(LinearLayout)getActivity().findViewById(R.id.footer_layout);
//		contact_layout.setVisibility(View.GONE);

		Button imVw = (Button) getActivity().findViewById(R.id.im_view);
		imVw.setVisibility(View.GONE);
		tf_regular = Typeface.createFromAsset(context.getAssets(),
				getResources().getString(R.string.fontfamily));
        tf_bold = Typeface.createFromAsset(context.getAssets(),
				getResources().getString(R.string.fontfamilybold));


		title = (TextView) getActivity().findViewById(
				R.id.activity_main_content_title);
		title.setText(getResources().getString(R.string.view_profile));
		title.setVisibility(View.VISIBLE);
		plusBtn = (Button) getActivity().findViewById(R.id.add_group);
		plusBtn.setVisibility(View.VISIBLE);
		plusBtn.setText(SingleInstance.mainContext.getResources().getString(R.string.edit));
		plusBtn.setTextColor(Color.parseColor("#ffffff"));
		plusBtn.setBackgroundResource(R.color.background);
		plusBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CreateProfileFragment createProfileFragment = CreateProfileFragment
						.newInstance(context);
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment,
						createProfileFragment);
				fragmentTransaction.commit();

			}
		});
		Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
		backBtn.setVisibility(View.GONE);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					Log.i("Test","BACK............");
					ContactsFragment contactsFragment = ContactsFragment.getInstance(context);							
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(
							R.id.activity_main_content_fragment,
							contactsFragment);
					fragmentTransaction.commit();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					Intent intent = new Intent(getActivity()
							.getApplicationContext(), ProfessionList.class);
					getActivity().startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// if (view == null) {
		view = inflater.inflate(R.layout.profiletitle, null);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		// View view=initView();
		try {

			// imageLoader = new ImageLoader(context);

			WebServiceReferences.contextTable.put("IM", context);
			// chatplayer = new MediaPlayer();

			CallDispatcher.pdialog = new ProgressDialog(context);
			// buddyname =
			// getActivity().getIntent().getStringExtra("buddyname");
			Bundle bundle = this.getArguments();
			// buddyname = bundle.getString("buddyname");

			WebServiceReferences.contextTable.put("viewprofile", context);
			SingleInstance.instanceTable
					.put("viewprofile", viewProfileFragment);
			initView();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("viewprofile", "===> " + e.getMessage());
			e.printStackTrace();
		}
		// } else {
		// ((ViewGroup) view.getParent()).removeView(view);
		// }

		btn_share = (Button) getActivity().findViewById(R.id.btn_settings);
		if (buddyname.equalsIgnoreCase(CallDispatcher.LoginUser)) {
			btn_share.setVisibility(View.VISIBLE);
			plusBtn.setVisibility(View.VISIBLE);
			title.setText(SingleInstance.mainContext.getResources().getString(R.string.my_profile));
		} else {
			btn_share.setVisibility(View.GONE);
			plusBtn.setVisibility(View.GONE);
		}
		btn_share.setText(SingleInstance.mainContext.getResources().getString(R.string.share));
		btn_share.setTextColor(Color.parseColor("#ffffff"));
		btn_share.setBackgroundResource(R.color.background);
//		Button selectall = (Button) getActivity().findViewById(R.id.btn_brg);
//		selectall.setVisibility(View.VISIBLE);
		btn_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StringBuffer buffer = new StringBuffer();
				Vector<FieldTemplateBean> profileFieldList = callDisp
						.getdbHeler(context).getProfileFields();
				if (profileFieldList != null && profileFieldList.size() > 0) {
					for (FieldTemplateBean fieldtemplate : profileFieldList) {
						if (fieldtemplate.getFiledvalue() != null
								&& fieldtemplate.getFiledvalue().length() > 0
								&& !fieldtemplate.getFieldType()
										.equalsIgnoreCase("multimedia")
								&& !fieldtemplate.getFieldType()
										.equalsIgnoreCase("photo")
								&& !fieldtemplate.getFieldType()
										.equalsIgnoreCase("video")
								&& !fieldtemplate.getFieldType()
										.equalsIgnoreCase("audio")) {
							String fieldname = fieldtemplate.getFieldName();
							String fieldValue = fieldtemplate.getFiledvalue();
							buffer.append(fieldname + " : " + fieldValue);
							buffer.append("\n\n");
						}

					}
				}
				if (buffer.toString().length() > 0) {
					Intent shareIntent = new Intent(context,
							InviteProfile.class);
					shareIntent.putExtra("profilevalues", buffer.toString());
					startActivity(shareIntent);
				} else
					showToast("No profile text values to share profile");
			}
		});

		return view;

	}

	// protected void ShowList() {
	// // TODO Auto-generated method stub
	//
	// setContentView(R.layout.history_container);
	//
	// slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
	// ArrayList<Slidebean> datas = new ArrayList<Slidebean>();
	//
	// callDisp.composeList(datas);
	// slidemenu.init(ViewProfileFragment.this, datas, ViewProfileFragment.this,
	// 100);
	//
	// }

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.menu, menu);
	// return true;
	// }
	public View getParentView() {
		return view;
	}

	@Override
	public void onDestroy() {
		try {
			super.onDestroy();
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						WebServiceReferences.contextTable.remove("viewprofile");
						MemoryProcessor.getInstance().unbindDrawables(view);
						view = null;
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}).start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyfileDownloaded(final String fieldid,
			final String ownername) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					if (buddyname != null && ownername != null
							&& buddyname.equals(ownername)) {
						for (int i = 0; i < records_container.getChildCount(); i++) {
							View view = (View) records_container.getChildAt(i);
							String field_id = (String) view.getTag();
							if (fieldid.equals(field_id)) {
								FieldTemplateBean bean = (FieldTemplateBean) OtherDetails
										.get(i);
								initView();
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void refreshView(FieldTemplateBean fields) {

		String groupname = "";
		LayoutParams iv_params = new LayoutParams(96, 96);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout layout = (LinearLayout) layoutInflater.inflate(
				R.layout.viewprofiles, null);
		layout.setTag(fields.getFieldId());

		TextView tv1 = (TextView) layout.findViewById(R.id.Heading);
		tv1.setText(UpperCaseParse.captionTextForUpperCaseString(fields
				.getGroupName()));
		if (groupname.trim().length() == 0) {
			tv1.setVisibility(View.VISIBLE);
			groupname = fields.getGroupName();
		} else {
			if (!groupname.equals(fields.getGroupName())) {
				tv1.setVisibility(View.VISIBLE);
				groupname = fields.getGroupName();
			} else
				tv1.setVisibility(View.GONE);

		}
		LinearLayout viewProf = (LinearLayout) layout
				.findViewById(R.id.viewprof_lay);
		String fieldname = fields.getFieldName();
		String type = fields.getFieldType();
		final String value = fields.getFiledvalue();

		if (type.equalsIgnoreCase("multimedia")
				|| type.equalsIgnoreCase("audio")
				|| type.equalsIgnoreCase("video")
				|| type.equalsIgnoreCase("photo")) {
			callDisp.setProfilePicture(value, R.drawable.videonotesnew);
			LinearLayout blob_layout = new LinearLayout(context);
			blob_layout.setOrientation(LinearLayout.VERTICAL);
			blob_layout.setGravity(Gravity.LEFT);
			View view = new View(context);
			view.setPadding(0, 10, 0, 0);
			view.setLayoutParams(iv_params);
			view.setBackgroundColor(Color.parseColor("#C0C0C0"));
			LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);

			params1.setMargins(0, 0, 0, 0);
			blob_layout.addView(view, params1);
			final ImageView i_view = new ImageView(context);
			i_view.setPadding(0, 10, 0, 0);
			i_view.setLayoutParams(iv_params);
			LayoutParams params = new LayoutParams(64, 64);

			params.setMargins(20, 5, 0, 0);
			blob_layout.addView(i_view, params);

			TextView tview = new TextView(context);
			tview.setText(UpperCaseParse
					.captionTextForUpperCaseString(fieldname));
			tview.setTextColor(Color.LTGRAY);
			tview.setTextSize(13);
			tview.setPadding(0, 10, 0, 0);
			tview.setTypeface(null, Typeface.BOLD_ITALIC);
			LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);

			params2.setMargins(20, 5, 0, 0);
			blob_layout.addView(tview, params2);
			ArrayList<Object> list1 = new ArrayList<Object>();
			list1.add(value);
			list1.add(i_view);
			// if (value != null) {
			// if (value.trim().length() != 0) {
			// decode_task = new base64Decoder();
			// decode_task.execute(list1);
			// } else {
			// bitmap = BitmapFactory.decodeResource(getResources(),
			// R.drawable.videonotesnew);
			// i_view.setImageBitmap(bitmap);
			// }
			// } else {
			// bitmap = BitmapFactory.decodeResource(getResources(),
			// R.drawable.videonotesnew);
			// i_view.setImageBitmap(bitmap);
			// }
			imageLoader.DisplayImage(value, i_view, R.drawable.videonotesnew);
			layout.addView(blob_layout);

			chatplayer.setLooping(false);
			i_view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (value.trim().length() > 0) {
						if (!value.contains("MPD_")) {

							String filepath = "/sdcard/COMMedia/" + value;
							Log.i("a", "this is pathhhhh" + filepath);

							if (value.contains("MVD_")
									&& !value.contains(".mp4")) {

								Log.i("a", "inside  MVD Andd mp4");
								filepath = "/sdcard/COMMedia/" + value + ".mp4";

							}
							final File vfileCheck = new File(filepath);
							if (vfileCheck.exists()) {
								if (value.contains("MAD_")) {

									if (chatplayer.isPlaying()) {
										chatplayer.stop();
										chatplayer.reset();
										bitmap = BitmapFactory.decodeResource(
												getResources(),
												R.drawable.v_play);
										i_view.setImageBitmap(bitmap);

									} else {
										try {
											chatplayer.setDataSource(filepath);
											chatplayer.prepare();
											chatplayer.start();
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_stop);
											i_view.setImageBitmap(bitmap);

										} catch (IllegalArgumentException e) {
											// TODO
											// Auto-generated
											// catch
											// block
											e.printStackTrace();
										} catch (IllegalStateException e) {
											// TODO
											// Auto-generated
											// catch
											// block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO
											// Auto-generated
											// catch
											// block
											e.printStackTrace();
										}

										chatplayer
												.setOnCompletionListener(new OnCompletionListener() {

													@Override
													public void onCompletion(
															MediaPlayer mp) {
														// TODO
														// Auto-generated
														// method
														// stub
														Log.i("log",
																"on completion listener start");
														try {
															bitmap = BitmapFactory
																	.decodeResource(
																			getResources(),
																			R.drawable.v_play);
															i_view.setImageBitmap(bitmap);

														} catch (IllegalArgumentException e) {
															// TODO
															// Auto-generated
															// catch
															// block
															e.printStackTrace();
														} catch (IllegalStateException e) {
															// TODO
															// Auto-generated
															// catch
															// block
															e.printStackTrace();
														}

													}
												});
									}
								} else {
									Log.i("a", "file exixts trueeee");

									try {
										Intent intentVPlayer = new Intent(
												context, VideoPlayer.class);
										// intentVPlayer.putExtra("File_Path",
										// filepath);
										// intentVPlayer.putExtra("Player_Type",
										// "Video Player");
										intentVPlayer.putExtra("video",
												filepath);
										context.startActivity(intentVPlayer);
									} catch (Exception e) {
										// TODO Auto-generated
										// catch block
										Log.i("viewprofile",
												"===> " + e.getMessage());
										e.printStackTrace();
									}
								}
							} else {
								Toast.makeText(context,
										"Sorry file not available", 1).show();

							}

						} else if (value.contains("MPD_")) {
							try {

								String path = Environment
										.getExternalStorageDirectory()
										.getAbsolutePath()
										+ "/COMMedia/" + value;
								final File vfileCheck = new File(path);
								if (vfileCheck.exists()) {
									Intent in = new Intent(context,
											PhotoZoomActivity.class);
									in.putExtra("type", false);
									in.putExtra("Photo_path", path);
									in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(in);
								} else {
									Toast.makeText(context, "File Downloading",
											1).show();
								}
							} catch (Exception e) {
								Log.e("Error", "===>" + e.getMessage());
							}
						}
					} else
						Toast.makeText(context, "Sorry! No multimedia file",
								Toast.LENGTH_SHORT).show();
				}

			});

		} else {
			try {
				LinearLayout view = (LinearLayout) layoutInflater.inflate(
						R.layout.customview, null);
				TextView fieldnames = (TextView) view
						.findViewById(R.id.fieldname);
				fieldnames.setTypeface (tf_regular);
				TextView fieldvalue = (TextView) view
						.findViewById(R.id.fieldvalue);
				fieldvalue.setTypeface (tf_regular);
				final ImageView email = (ImageView) view
						.findViewById(R.id.email);
				ImageView phone = (ImageView) view.findViewById(R.id.phone);

				email.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final Intent emailIntent = new Intent(
								android.content.Intent.ACTION_SEND);

						emailIntent.setType("plain/text");

						emailIntent.putExtra(
								android.content.Intent.EXTRA_EMAIL,
								new String[] { email.getContentDescription()
										.toString() });

						emailIntent
								.putExtra(android.content.Intent.EXTRA_SUBJECT,
										"Profile");

						emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
								"");

						startActivity(Intent.createChooser(emailIntent,
								"Send mail..."));

					}
				});

				phone.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_DIAL);
						startActivity(intent);

					}
				});

				fieldnames.setText(UpperCaseParse
						.captionTextForUpperCaseString(fields.getFieldName()));
				if (fields.getFiledvalue() != null)
					fieldvalue.setText(UpperCaseParse
							.captionTextForUpperCaseString(fields
									.getFiledvalue().toString()));

				if (type.equalsIgnoreCase("phone")) {
					phone.setContentDescription(fields.getFiledvalue());

					phone.setVisibility(View.VISIBLE);
					email.setVisibility(View.GONE);

				}

				viewProf.addView(view);

				layout.setId(records_container.getChildCount());
				Log.i("thread", "Layout id" + layout.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("viewprofile", "====> " + e.getMessage());
				e.printStackTrace();
			}
		}

		records_container.addView(layout);

	}

	@SuppressWarnings("unchecked")
	public void initView() {
		try {
			plusBtn = (Button) getActivity().findViewById(R.id.add_group);
			title = (TextView) getActivity().findViewById(
					R.id.activity_main_content_title);
			btn_share = (Button) getActivity().findViewById(R.id.btn_settings);
			// if(records_container!=null){
			if (buddyname != null
					&& !buddyname.equalsIgnoreCase(CallDispatcher.LoginUser)) {

				if (title != null) {
					title.setText(buddyname + " 's Profile");
					btn_share.setVisibility(View.GONE);
					plusBtn.setVisibility(View.GONE);
				}
			} else {
				if (title != null && SingleInstance.profileView) {
					SingleInstance.profileView = false;

					title.setText(SingleInstance.mainContext.getResources().getString(R.string.my_profile));
					btn_share.setVisibility(View.VISIBLE);
					plusBtn.setVisibility(View.VISIBLE);
				}
			}
			if (view == null) {
				LayoutInflater layoutInflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				// LinearLayout layout = (LinearLayout) layoutInflater.inflate(
				// R.layout.viewprofiles, null);

				view = layoutInflater.inflate(R.layout.profiletitle, null);

			}

			// if(btn_share==null){
			// btn_share = (Button) getActivity().findViewById(
			// R.id.btn_settings);
			// btn_share.setText("Share");
			// btn_share.setTextColor(Color.parseColor("#ffffff"));
			// btn_share.setBackgroundResource(R.color.title);
			// btn_share.setVisibility(View.VISIBLE);
			// }
			records_container = (LinearLayout) view
					.findViewById(R.id.llayout_holder);
			// }
			Log.i("BackGround", "buddyname...:" + buddyname);
			OtherDetails = callDisp.getdbHeler(context).getProfileFieldsValues(
					buddyname);

			if (records_container != null)
				records_container.removeAllViews();

			LayoutParams iv_params = new LayoutParams(96, 96);
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			String groupname = "";
			for (FieldTemplateBean fields : OtherDetails) {
				Log.i("PROFILELOGS",
						"HashprofileFields===>" + fields.getGroupName());
				if (fields.getFiledvalue() != null) {
					if (fields.getFiledvalue().trim().length() > 0) {
						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.viewprofiles, null);
						layout.setTag(fields.getFieldId());

						TextView tv1 = (TextView) layout
								.findViewById(R.id.Heading);
						tv1.setText(UpperCaseParse
								.captionTextForUpperCaseString(fields
										.getGroupName()));
						if (groupname.trim().length() == 0) {
							tv1.setVisibility(View.VISIBLE);
							groupname = fields.getGroupName();
						} else {
							if (!groupname.equals(fields.getGroupName())) {
								tv1.setVisibility(View.VISIBLE);
								groupname = fields.getGroupName();
							} else
								tv1.setVisibility(View.GONE);

						}
						LinearLayout viewProf = (LinearLayout) layout
								.findViewById(R.id.viewprof_lay);
						final String fieldname = fields.getFieldName();
						final String type = fields.getFieldType();
						final String value = CallDispatcher
								.containsLocalPath(fields.getFiledvalue());
						if (type.equalsIgnoreCase("multimedia")
								|| type.equalsIgnoreCase("audio")
								|| type.equalsIgnoreCase("video")
								|| type.equalsIgnoreCase("photo")) {

							LinearLayout blob_layout = new LinearLayout(context);
							blob_layout.setOrientation(LinearLayout.VERTICAL);
							blob_layout.setGravity(Gravity.LEFT);
							View view = new View(context);
							view.setPadding(0, 10, 0, 0);
							view.setLayoutParams(iv_params);
							view.setBackgroundColor(Color.parseColor("#C0C0C0"));
							LayoutParams params1 = new LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT);

							params1.setMargins(0, 0, 0, 0);
							blob_layout.addView(view, params1);
							final ImageView i_view = new ImageView(context);
							i_view.setTag(0);
							i_view.setPadding(0, 10, 0, 0);
							i_view.setLayoutParams(iv_params);
							LayoutParams params = new LayoutParams(64, 64);

							params.setMargins(20, 5, 0, 0);
							blob_layout.addView(i_view, params);

							TextView tview = new TextView(context);
							tview.setText(UpperCaseParse
									.captionTextForUpperCaseString(fieldname));
							tview.setTextColor(Color.LTGRAY);
							tview.setTextSize(13);
							tview.setPadding(0, 10, 0, 0);
							tview.setTypeface(null, Typeface.BOLD_ITALIC);
							LayoutParams params2 = new LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT);

							params2.setMargins(20, 5, 0, 0);
							blob_layout.addView(tview, params2);
							ArrayList<Object> list1 = new ArrayList<Object>();
							list1.add(value);
							list1.add(i_view);
							// if (value != null) {
							// if (value.trim().length() != 0) {
							// decode_task = new base64Decoder();
							// decode_task.execute(list1);
							// } else {
							// bitmap = BitmapFactory.decodeResource(
							// getResources(),
							// R.drawable.videonotesnew);
							// i_view.setImageBitmap(bitmap);
							// }
							// } else {
							// bitmap = BitmapFactory.decodeResource(
							// getResources(),
							// R.drawable.videonotesnew);
							// i_view.setImageBitmap(bitmap);
							// }
							if (value == null) {
								Log.i("BackGround", "value" + null);
							}
							if (i_view == null) {
								Log.i("BackGround", "i_view" + null);
							}
							imageLoader = new ImageLoader(context);

							imageLoader.DisplayImage(value, i_view,
									R.drawable.videonotesnew);
							layout.addView(blob_layout);

							chatplayer = new MediaPlayer();

							chatplayer.setLooping(false);
							i_view.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									final ImageView iv_play = (ImageView) v;
									int tag = (Integer) v.getTag();
									SingleInstance.printLog("View Profile",
											" Field name : " + fieldname
													+ " Multimeda File Name : "
													+ value, "DEBUG", null);

									if (value.trim().length() > 0) {
										String filepath = callDisp
												.containsLocalPath(value);
										if (!value.contains("MPD_")) {
											if (filepath.contains("MVD_")
													&& !filepath
															.contains(".mp4")) {
												Log.i("a",
														"inside  MVD Andd mp4");
												filepath = filepath + ".mp4";
												SingleInstance.printLog(
														"View Profile",
														" Append .mp4 if not contains in video file "
																+ filepath,
														"DEBUG", null);
											}
											final File vfileCheck = new File(
													filepath);
											if (vfileCheck.exists()) {
												if (filepath.contains("MAD_")) {
													SingleInstance.printLog(
															"View Profile",
															"Audio File"
																	+ filepath,
															"DEBUG", null);
													Intent intent = new Intent(
															context,
															MultimediaUtils.class);
													intent.putExtra("filePath",
															filepath);
													intent.putExtra(
															"requestCode",
															"audio");
													intent.putExtra("action",
															"audio");
													intent.putExtra(
															"createOrOpen",
															"open");
													context.startActivity(intent);

												} else {
													SingleInstance.printLog(
															"View Profile",
															"Video File"
																	+ filepath,
															"DEBUG", null);
													try {
														Intent intentVPlayer = new Intent(
																context,
																VideoPlayer.class);
														// intentVPlayer.putExtra(
														// "File_Path",
														// filepath);
														// intentVPlayer.putExtra(
														// "Player_Type",
														// "Video Player");
														intentVPlayer.putExtra(
																"video",
																filepath);
														context.startActivity(intentVPlayer);
													} catch (Exception e) {
														// TODO Auto-generated
														// catch block
														Log.i("viewprofile",
																"===> "
																		+ e.getMessage());
														e.printStackTrace();
													}
												}
											} else {
												Toast.makeText(
														context,
														"Sorry File not available",
														1).show();

											}

										} else if (filepath.contains("MPD_")) {
											try {
												SingleInstance.printLog(
														"View Profile",
														" Image File"
																+ filepath,
														"DEBUG", null);
												final File vfileCheck = new File(
														filepath);
												if (vfileCheck.exists()) {
													// Intent in = new Intent(
													// context,
													// PhotoZoomActivity.class);
													// in.putExtra("type",
													// false);
													// in.putExtra("Photo_path",
													// filepath);
													// in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
													// context.startActivity(in);
													SingleInstance
															.printLog(
																	"View Profile",
																	" Image File"
																			+ filepath
																			+ " Onclick on image",
																	"DEBUG",
																	null);
													Intent intent = new Intent(
															context,
															FullScreenImage.class);
													intent.putExtra("image",
															filepath);
													intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
													startActivity(intent);
												} else {
													Toast.makeText(context,
															"File Downloading",
															1).show();
												}
											} catch (Exception e) {
												Log.e("Error",
														"===>" + e.getMessage());
												SingleInstance
														.printLog(
																"View Profile",
																" Image File"
																		+ filepath
																		+ " Onclick on image",
																"DEBUG", null);
												AppReference.logger
														.error("View Profile"
																+ " Image File"
																+ filepath
																+ " Onclick on image",
																e);
											}
										}
									} else
										Toast.makeText(context,
												"Sorry! No multimedia file",
												Toast.LENGTH_SHORT).show();
								}

							});

						} else {
							try {
								LinearLayout view = (LinearLayout) layoutInflater
										.inflate(R.layout.customview, null);
								TextView fieldnames = (TextView) view
										.findViewById(R.id.fieldname);
								fieldnames.setTypeface (tf_regular);
								TextView fieldvalue = (TextView) view
										.findViewById(R.id.fieldvalue);
								fieldvalue.setTypeface (tf_regular);
								final ImageView email = (ImageView) view
										.findViewById(R.id.email);
								ImageView phone = (ImageView) view
										.findViewById(R.id.phone);

								email.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										final Intent emailIntent = new Intent(
												android.content.Intent.ACTION_SEND);

										emailIntent.setType("plain/text");

										emailIntent
												.putExtra(
														android.content.Intent.EXTRA_EMAIL,
														new String[] { email
																.getContentDescription()
																.toString() });

										emailIntent
												.putExtra(
														android.content.Intent.EXTRA_SUBJECT,
														"Profile");

										emailIntent
												.putExtra(
														android.content.Intent.EXTRA_TEXT,
														"");

										startActivity(Intent.createChooser(
												emailIntent, "Send mail..."));

									}
								});

								phone.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(
												Intent.ACTION_DIAL);
										startActivity(intent);

									}
								});

								fieldnames.setText(UpperCaseParse
										.captionTextForUpperCaseString(fields
												.getFieldName()));
								if (fields.getFiledvalue() != null)
									fieldvalue
											.setText(UpperCaseParse
													.captionTextForUpperCaseString(fields
															.getFiledvalue()
															.toString()));

								if (type.equalsIgnoreCase("phone")) {
									phone.setContentDescription(fields
											.getFiledvalue());

									phone.setVisibility(View.VISIBLE);
									email.setVisibility(View.GONE);

								}

								viewProf.addView(view);

								layout.setId(records_container.getChildCount());
								Log.i("thread", "Layout id" + layout.getId());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								Log.e("viewprofile", "====> " + e.getMessage());
								e.printStackTrace();
							}
						}

						records_container.addView(layout);
					}

				}
			}
			callDisp.cancelDialog();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("profile", "====> " + e.getMessage());
			e.printStackTrace();
		}

	}

	// public class base64Decoder extends
	// AsyncTask<ArrayList<Object>, Void, Bitmap> {
	// ImageView iview;
	//
	// @Override
	// protected Bitmap doInBackground(ArrayList<Object>... params) {
	// try {
	// // TODO Auto-generated method stub
	// Log.e("task", "************ camr to do in background");
	//
	// ArrayList<Object> list = params[0];
	// String bmp_array = (String) list.get(0);
	// iview = (ImageView) list.get(1);
	// if (!bmp_array.contains(Environment
	// .getExternalStorageDirectory() + "/COMMedia/")) {
	// bmp_array = Environment.getExternalStorageDirectory()
	// + "/COMMedia/" + bmp_array;
	// }
	// String blob_path = bmp_array;
	// if (blob_path.contains("MPD_")) {
	// // bitmap = callDisp.ResizeImage(blob_path);
	// // if (bitmap != null)
	// // bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150,
	// // false);
	// // else
	// // bitmap = BitmapFactory.decodeResource(getResources(),
	// // R.drawable.videonotesnew);
	// // Log.i("BLOB", "inside png formate");
	// imageLoader.DisplayImage(blob_path, iview,
	// R.drawable.videonotesnew);
	// }
	//
	// else if (blob_path.contains("MVD_"))
	//
	// {
	//
	// Log.i("BLOB", "inside Video file formate");
	//
	// bitmap = BitmapFactory.decodeResource(getResources(),
	// R.drawable.v_play);
	//
	// } else if (blob_path.contains("MAD_"))
	//
	// {
	// Log.i("BLOB", "inside Audio file formate");
	//
	// bitmap = BitmapFactory.decodeResource(getResources(),
	// R.drawable.v_play);
	//
	// }
	// return bitmap;
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// Log.e("viewprofile", "===> " + e.getMessage());
	// e.printStackTrace();
	// }
	// return bitmap;
	// }
	//
	// @Override
	// protected void onPostExecute(Bitmap result) {
	// try {
	// // TODO Auto-generated method stub
	// if (result != null)
	// Log.i("BLOB", "inside imageview set formate formate");
	// iview.setImageBitmap(result);
	// super.onPostExecute(result);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// Log.e("viewprofile", "===> " + e.getMessage());
	// e.printStackTrace();
	// }
	// }
	//
	// }

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// try {
	// // TODO Auto-generated method stub
	// if ((keyCode == KeyEvent.KEYCODE_BACK))
	// callDisp.showKeyDownAlert(context);
	//
	// return super.onKeyDown(keyCode, event);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// Log.e("viewprofile", "===> " + e.getMessage());
	// e.printStackTrace();
	// return false;
	// }
	// }

	public void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public String getBuddyname() {
		return this.buddyname;
	}

	// @Override
	// public void onSlideMenuItemClick(int itemId, View v, Context context) {
	// // TODO Auto-generated method stub
	// switch (itemId) {
	// case WebServiceReferences.CONTACTS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.USERPROFILE:
	//
	// break;
	// case WebServiceReferences.UTILITY:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.NOTES:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	// case WebServiceReferences.APPS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	// case WebServiceReferences.CLONE:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.SETTINGS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	//
	// case WebServiceReferences.QUICK_ACTION:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	// case WebServiceReferences.FORMS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	// case WebServiceReferences.FEEDBACK:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	// case WebServiceReferences.EXCHANGES:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	// default:
	// break;
	// }
	// }

	// @Override
	// public void notifyReceivedIM(final SignalingBean sb) {
	// // TODO Auto-generated method stub
	// viewHandler.post(new Runnable() {
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
	// }

	// @Override
	// protected void onResume() {
	// try {
	// super.onResume();
	// if (WebServiceReferences.Imcollection.size() == 0)
	// IMRequest.setVisibility(View.GONE);
	// else
	// IMRequest.setVisibility(View.VISIBLE);
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public void notifyWEbService(Servicebean servicebean) {
		if (servicebean.getObj() instanceof ArrayList) {
			ArrayList<Object> resut = (ArrayList<Object>) servicebean.getObj();
			String[] profile_info = new String[4];
			profile_info = (String[]) resut.get(0);
			String profileOwner = profile_info[1];
			if (resut.size() > 1) {
				ArrayList<FieldTemplateBean> filed_values = (ArrayList<FieldTemplateBean>) resut
						.get(1);
				for (FieldTemplateBean fieldTemplateBean : filed_values) {

					ContentValues cv = new ContentValues();
					if (fieldTemplateBean.getFiledvalue() != null
							&& !fieldTemplateBean.getFiledvalue()
									.equalsIgnoreCase("null")
							&& fieldTemplateBean.getFieldId() != null) {

						cv.put("fieldid", fieldTemplateBean.getFieldId());
						cv.put("modifieddate",
								fieldTemplateBean.getField_modifieddate());
						cv.put("createddate",
								fieldTemplateBean.getField_createddate());
						cv.put("userid", profileOwner);

						cv.put("fieldvalue", fieldTemplateBean.getFiledvalue());

						if (callDisp.multimediaFieldsValues.size() > 0)
							callDisp.multimediaFieldsValues.clear();

						callDisp.multimediaFieldsValues = DBAccess
								.getdbHeler(context)
								.getMultimediaFieldValues(
										"SELECT fieldid,fieldvalue FROM profilefieldvalues WHERE fieldid IN (SELECT fieldid FROM fieldtemplate WHERE fieldtype = 'Audio' OR fieldtype = 'Video' OR fieldtype = 'Photo') and userid = '"
												+ profileOwner + "'");
						Log.i("profile", "====> get profile multimedia");
						if (callDisp.multimediaFieldsValues != null
								&& callDisp.multimediaFieldsValues.size() > 0) {
							Log.i("profile",
									"====> inside get profile multimedia");
							if (callDisp.multimediaFieldsValues
									.containsKey(fieldTemplateBean.getFieldId())) {
								String fieldValue = callDisp.multimediaFieldsValues
										.get(fieldTemplateBean.getFieldId());
								if (!fieldValue
										.equalsIgnoreCase(fieldTemplateBean
												.getFiledvalue())) {
									Log.i("profile",
											"====> inside get profile multimedia file exists");
									File file = new File(Environment
											.getExternalStorageDirectory()
											.getAbsolutePath()
											+ "/COMMedia/" + fieldValue);
									if (file.exists()) {
										Log.i("profile",
												"====> inside get profile multimedia file delete");
										file.delete();
									}
								}
							}
						}

						String profile_multimedia = Environment
								.getExternalStorageDirectory()
								+ "/COMMedia/"
								+ fieldTemplateBean.getFiledvalue();
						File msg_file = new File(profile_multimedia);
						String f_type = DBAccess.getdbHeler(context)
								.getFieldType(fieldTemplateBean.getFieldId());

						if (msg_file.exists())
							cv.put("status", 2);
						else {

							if (f_type != null
									&& f_type.equalsIgnoreCase("photo")
									|| f_type.equalsIgnoreCase("video")
									|| f_type.equalsIgnoreCase("audio")
									|| f_type.equalsIgnoreCase("multimedia"))
								cv.put("status", 0);
							else
								cv.put("status", 2);

						}
						boolean isUpdated = false;

						if (!DBAccess.getdbHeler(context).isRecordExists(
								"select * from profilefieldvalues where fieldid="
										+ fieldTemplateBean.getFieldId()
										+ " and userid='" + profileOwner + "'")) {

							DBAccess.getdbHeler(context)
									.insertProfileFieldValues(cv);

						} else {
							cv.remove("fieldid");
							DBAccess.getdbHeler(context)
									.updateProfileFieldValues(
											cv,
											"fieldid="
													+ fieldTemplateBean
															.getFieldId()
													+ " and userid='"
													+ profileOwner + "'");

						}

						if (fieldTemplateBean.getFieldId().equals("3"))
							isUpdated = true;

						

						if (!msg_file.exists()) {

							if (f_type != null) {
								if (f_type.equalsIgnoreCase("photo")
										|| f_type.equalsIgnoreCase("video")
										|| f_type.equalsIgnoreCase("audio")
										|| f_type
												.equalsIgnoreCase("multimedia")) {
									Log.d("profile", "Field value is :"
											+ fieldTemplateBean.getFiledvalue());
									if (fieldTemplateBean.getFieldId().equals(
											"3")) {

										BuddyInformationBean bean = null;
										for (BuddyInformationBean temp : ContactsFragment
												.getBuddyList()) {
											if (!temp.isTitle()) {
												if (temp.getName()
														.equalsIgnoreCase(
																fieldTemplateBean
																		.getUsername())) {
													bean = temp;
													break;
												}
											}
										}
										// if (WebServiceReferences.buddyList
										// .containsKey(fieldTemplateBean
										// .getUsername())) {
										// BuddyInformationBean bean =
										// WebServiceReferences.buddyList
										// .get(fieldTemplateBean
										// .getUsername());

										if (bean != null)
											bean.setProfile_picpath(fieldTemplateBean
													.getFiledvalue());
										// }
									}
									callDisp.downloadOfflineresponse(
											fieldTemplateBean.getFiledvalue(),
											fieldTemplateBean.getFieldId()
													+ "," + profileOwner + ","
													+ isUpdated,
											"profile field", null);
								}

							}
						} else {
							ContactsFragment.getInstance(context).SortList();
							// callDisp.comparewithStateanddistance();
							callDisp.profilePictureDownloaded();
						}

						msg_file = null;
					}

				}
			}

		} else if (servicebean.getObj() instanceof WebServiceBean) {
			Log.d("Profile", "Error message is --->"
					+ ((WebServiceBean) servicebean.getObj()).getText());
		}
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				callDisp.cancelDialog();
				if (SingleInstance.contextTable.containsKey("groupchat")) {
					((GroupChatActivity) SingleInstance.contextTable
							.get("groupchat")).notifyViewProfile();
				} else if (WebServiceReferences.contextTable
						.containsKey("buddiesList")) {
					(ContactsFragment.getInstance(context))
							.notifyViewProfile(true);
				}
			}
		});

	}

	public void setBuddyName(String buddyName) {
		this.buddyname = buddyName;
	}

	public void setFromActivity(String fromActivity) {
		this.fromActivity = fromActivity;
	}

}
