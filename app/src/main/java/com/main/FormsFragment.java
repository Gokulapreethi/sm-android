package com.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.SlideMenu.SlideMenu;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.listswipe.SwipeMenu;
import com.cg.commongui.listswipe.SwipeMenuCreator;
import com.cg.commongui.listswipe.SwipeMenuItem;
import com.cg.commongui.listswipe.SwipeMenuListView;
import com.cg.commongui.listswipe.SwipeMenuListView.OnMenuItemClickListener;
import com.cg.commongui.listswipe.SwipeMenuListView.OnSwipeListener;
import com.cg.forms.AddNewForm;
import com.cg.forms.FormSettings;
import com.cg.forms.FormViewer;
import com.cg.forms.InputsFields;
import com.cg.hostedconf.AppReference;
import com.cg.instancemessage.IMNotifier;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.process.MemoryProcessor;
import com.util.SingleInstance;
import com.util.Utils;

import org.lib.model.FormAttributeBean;
import org.lib.model.FormRecordsbean;
import org.lib.model.FormsBean;
import org.lib.model.FormsListBean;
import org.lib.model.Formsinfocontainer;
import org.lib.model.SignalingBean;
import org.lib.model.WebServiceBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

public class FormsFragment extends Fragment implements OnClickListener,
		IMNotifier {
	// private Button add_btn;
	private Button settings = null;
	private SwipeMenuListView lookup_view = null;
	// private Context context = null;
	private MyCustomAdapter lookup_adapter = null;
	private ArrayList<FormsListBean> ownlist = null;
	private ArrayList<FormsListBean> buddylist = null;
	private ArrayList<String> field = null;
	private AlertDialog confirmation = null;
	private SharedPreferences p;
	private ProgressDialog dialog = null;
	private Handler wservice_handler = null;
	private CallDispatcher callDisp = null;
	private EditText ed_srch = null;
	private ArrayList<FormsListBean> allitems = null;
	private HashMap<String, String> dtype = new HashMap<String, String>();
	private int selected_index;
	private boolean isForm_requested = false;
	boolean isreject = false;
	private String list = null;
	private ArrayList<String> lists = null;
	private AlertDialog alert = null;
	private String tableIDViewForm = "";
	private Handler handler = new Handler();
	private SlideMenu slidemenu;
	static int buddyStatus = 0;
	private String popUpContents[];
	private String popUpContentsBuddy[];
	private Button IMRequest, search_button;
	private LinearLayout searchLayout;
	private PopupWindow popupWindowMenus;
	private List<String> menusList = new ArrayList<String>();
	private List<String> BuddymenusList = new ArrayList<String>();
	private MyCustomAdapter search_adapter;
	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();
	private Typeface tf_regular = null;
	
	private Typeface tf_bold = null;


	private static FormsFragment formsFragment;

	private static Context context;

	// private Button plus = null; // this button plus hide in this page,this
	// button create fragment xml

	private Button btn_settings = null;

	public View view;

	public static FormsFragment newInstance(Context maincontext) {
		try {
			if (formsFragment == null) {
				context = maincontext;
				formsFragment = new FormsFragment();

			}

			return formsFragment;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return formsFragment;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Button select = (Button) getActivity().findViewById(R.id.btn_brg);
		select.setVisibility(View.GONE);
		LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
		contact_layout.setVisibility(View.GONE);

		Button imVw = (Button) getActivity().findViewById(R.id.im_view);
		imVw.setVisibility(View.GONE);
		RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
		mainHeader.setVisibility(View.VISIBLE);

		Button setting = (Button) getActivity().findViewById(R.id.btn_settings);
		setting.setVisibility(View.VISIBLE);
		setting.setText("");
		setting.setBackgroundResource(R.drawable.ic_action_settings);
		
		tf_regular = Typeface.createFromAsset(context.getAssets(),
				getResources().getString(R.string.fontfamily));
        tf_bold = Typeface.createFromAsset(context.getAssets(),
				getResources().getString(R.string.fontfamilybold));


		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (CallDispatcher.LoginUser != null) {
					if (!CallDispatcher.isWifiClosed) {

						Intent intentfrms = new Intent(context,
								FormSettings.class);
						startActivity(intentfrms);
					} else {
						ShowError("Network Error", "Network Unreachable");
					}
				} else {
					ShowError("Error", "Kindly Login");
				}

			}
		});

		Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
		plusBtn.setVisibility(View.VISIBLE);
		plusBtn.setText("");
		plusBtn.setBackgroundResource(R.drawable.toolbar_buttons_plus);

		plusBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CallDispatcher.LoginUser != null) {
					Intent add_intent = new Intent(context, AddNewForm.class);
					add_intent.putExtra("isvalid", false);
					context.startActivity(add_intent);

				} else {

					ShowError("Error", "You Are In offline state");
				}
			}
		});
		Button menu = (Button) getActivity().findViewById(
				R.id.side_menu);
		if (SingleInstance.fromGroupChat) {
			setting.setVisibility(View.GONE);
			menu.setBackgroundResource(R.drawable.ic_action_back);
//			menu.setLayoutParams(new RelativeLayout.LayoutParams(45, 45));
			plusBtn.setVisibility(View.GONE);
		} else {
			setting.setVisibility(View.VISIBLE);
			plusBtn.setVisibility(View.VISIBLE);
		}

		if (view == null) {
			view = inflater.inflate(R.layout.form_main, null);
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			try {

				SingleInstance.contextTable.put("forms", context);

				TextView title = (TextView) getActivity().findViewById(
						R.id.activity_main_content_title);
				title.setText("Forms");
				title.setVisibility(View.VISIBLE);

				// super.onCreate(savedInstanceState);
				// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
				WebServiceReferences.contextTable.put("IM", context);

				// DisplayMetrics displaymetrics = new DisplayMetrics();
				// getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				// int noScrHeight = displaymetrics.heightPixels;
				// int noScrWidth = displaymetrics.widthPixels;
				// context = this;

				if (WebServiceReferences.callDispatch.containsKey("calldisp"))
					callDisp = (CallDispatcher) WebServiceReferences.callDispatch
							.get("calldisp");
				else
					callDisp = CallDispatcher.getCallDispatcher(context);

				// callDisp.setNoScrHeight(noScrHeight);
				// callDisp.setNoScrWidth(noScrWidth);
				//
				// displaymetrics = null;
				// ShowList();

				// setContentView(R.layout.form_main);

				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
				WebServiceReferences.contextTable.put("formactivity", context);

				// add_btn = (Button) view.findViewById(R.id.btn_add);
				// add_btn.setOnClickListener(this);

				settings = (Button) view.findViewById(R.id.btn_set);
				settings.setOnClickListener(this);
				lookup_view = (SwipeMenuListView) view
						.findViewById(R.id.form_listview);

				lookup_adapter = new MyCustomAdapter(context);
				search_adapter = new MyCustomAdapter(context);
				lookup_view.setAdapter(lookup_adapter);
				wservice_handler = new Handler();
				allitems = new ArrayList<FormsListBean>();
				searchLayout = (LinearLayout) view
						.findViewById(R.id.layout_search);

				// IMRequest = (Button) findViewById(R.id.formim);
				// IMRequest.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// callDisp.openReceivedIm(v, context);
				// }
				// });

				// IMRequest.setVisibility(View.INVISIBLE);

				search_button = (Button) view.findViewById(R.id.btn_searchlist);
				search_button.setOnClickListener(this);
				// btn_back = (Button) view.findViewById(R.id.btn_back);
				menusList.add("View");
				menusList.add("Add");
				menusList.add("Add with Call");
				menusList.add("Delete");

				popUpContents = new String[menusList.size()];

				menusList.toArray(popUpContents);

				BuddymenusList.add("View");
				BuddymenusList.add("Add");
				BuddymenusList.add("Add with Call");

				popUpContentsBuddy = new String[BuddymenusList.size()];

				BuddymenusList.toArray(popUpContentsBuddy);

				// btn_back.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View arg0) {
				// // TODO Auto-generated method stub
				// try {
				// slidemenu.show();
				// } catch (Exception e) {
				// if (AppReference.isWriteInFile)
				// AppReference.logger.error(e.getMessage(), e);
				// e.printStackTrace();
				// }
				// }
				// });
				SwipeMenuCreator creator = intiateSwipeList();
				// set creator
				lookup_view.setMenuCreator(creator);

				// step 2. listener item click event
				lookup_view
						.setOnMenuItemClickListener(new OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(int position,
									SwipeMenu menu, int index) {

								try {
									switch (index) {
									case 0:
										// open

										final FormsListBean bean = (FormsListBean) lookup_adapter
												.getItem(position);
										Vector<InputsFields> IFList = DBAccess
												.getdbHeler()
												.getFormFields(
														bean.getForm_name()
																+ "_"
																+ bean.getFormId());
										CallDispatcher.inputFieldList.clear();
										CallDispatcher.inputFieldList
												.addAll(IFList);
										Intent intent = new Intent(
												getActivity(), AddNewForm.class);
										intent.putExtra("isvalid", true);
										intent.putExtra("tablename",
												bean.getForm_name() + "_"
														+ bean.getFormId());
										startActivity(intent);
										break;
									case 1:
										if (!CallDispatcher.isWifiClosed) {

											String username;
											if (CallDispatcher.LoginUser != null)
												username = CallDispatcher.LoginUser;
											else {
												SharedPreferences pref = PreferenceManager
														.getDefaultSharedPreferences(context);
												username = pref.getString(
														"uname", "");
											}
											doDeleteForms(position, username);

										} else {
											ShowError("Network Error",
													"Network Unreachable");
											new AppMainActivity().setfooterVisiblity(false);
										}
										break;
									}
									return false;
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									return false;
								}
							}
						});

				// set SwipeListener
				lookup_view.setOnSwipeListener(new OnSwipeListener() {

					@Override
					public void onSwipeStart(int position) {
						// swipe start
					}

					@Override
					public void onSwipeEnd(int position) {
						// swipe end
					}
				});

				lookup_view.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View v,
							final int position, long arg3) {
						try {
							String username;
							if (CallDispatcher.LoginUser != null)
								username = CallDispatcher.LoginUser;
							else {
								SharedPreferences pref = PreferenceManager
										.getDefaultSharedPreferences(context);
								username = pref.getString("uname", "");
							}
							selected_index = position;
							doViewForms(username, position);

						} catch (Exception e) {
							if (AppReference.isWriteInFile)
								AppReference.logger.error(e.getMessage(), e);
							e.printStackTrace();
						}
					}

				});
				//
				// lookup_view
				// .setOnItemLongClickListener(new OnItemLongClickListener() {
				//
				// @Override
				// public boolean onItemLongClick(
				// AdapterView<?> parent, View view,
				// int position, long id) {
				//
				// if (!CallDispatcher.isWifiClosed) {
				//
				// String username;
				// if (CallDispatcher.LoginUser != null)
				// username = CallDispatcher.LoginUser;
				// else {
				// SharedPreferences pref = PreferenceManager
				// .getDefaultSharedPreferences(context);
				// username = pref.getString("uname", "");
				// }
				// doDeleteForms(position, username);
				//
				// } else {
				// ShowError("Network Error",
				// "Network Unreachable");
				// }
				// return true;
				//
				// }
				// });

				ed_srch = (EditText) view.findViewById(R.id.edtxt_searchlist);
				ed_srch.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(final CharSequence s, int arg1,
							int arg2, int arg3) {

						if (ed_srch.getText().toString().trim().length() == 0) {
							hideSoftKeyboard();
							populateLists();
						}

					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {

					}

					@Override
					public void afterTextChanged(Editable arg0) {

					}
				});

				// if (AppReference.sip_accountID != -1
				// && !AppReference.call_mode.equalsIgnoreCase("twoway")) {
				//
				// CommunicationBean bean = new CommunicationBean();
				// bean.setOperationType(sip_operation_types.MODIFY_ACCOUNT);
				// bean.setRealm(callDisp.getFS());
				// bean.setSipEndpoint(AppReference.sip_registeredid);
				// bean.setMode("twoway");
				// AppReference.call_mode = "twoway";
				// if (AppReference.sipQueue != null)
				// AppReference.sipQueue.addMsg(bean);
				// }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			((ViewGroup) view.getParent()).removeView(view);
		}
		TextView title = (TextView) getActivity().findViewById(
				R.id.activity_main_content_title);
		title.setText(getResources().getString(R.string.forms));

		populateLists();
		return view;

	}

	public void SearchForms(final String searchText) {

		try {
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {
					lookup_adapter.clearItem();
					if (ownlist != null) {
						ownlist.clear();
					}
					if (buddylist != null) {
						buddylist.clear();
					}
					if (allitems != null) {
						allitems.clear();
					}
					if (searchText != null && searchText.length() > 0) {
						p = PreferenceManager
								.getDefaultSharedPreferences(context);

						ownlist = callDisp.getdbHeler(context)
								.ownLookUpRecordsForSearch(
										CallDispatcher.LoginUser, searchText);

						if (ownlist.size() != 0) {
							FormsListBean bean = new FormsListBean();
							bean.setForm_name("My Forms");
							for (int i = 0; i < ownlist.size(); i++) {

								lookup_adapter.addItem(ownlist.get(i));
								allitems.add(ownlist.get(i));

							}
						}

						buddylist = callDisp.getdbHeler(context)
								.BuddiesLookUpRecordsForSearch(searchText);

						if (buddylist.size() != 0) {
							FormsListBean bean = new FormsListBean();
							bean.setForm_name("My Buddy's Forms");
							for (int i = 0; i < buddylist.size(); i++) {

								lookup_adapter.addItem(buddylist.get(i));

							}
						}
					}
					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							lookup_adapter.notifyDataSetChanged();
						}
					});

				}
			});
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void populateLists() {

		try {
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {

					lookup_adapter.clearItem();

					if (ownlist != null) {
						ownlist.clear();
					}
					if (buddylist != null) {
						buddylist.clear();
					}
					if (allitems != null) {
						allitems.clear();
					}
					p = PreferenceManager.getDefaultSharedPreferences(context);
					String lUser = "";
					if (CallDispatcher.LoginUser == null) {
						lUser = p.getString("uname", "");
					} else {
						lUser = CallDispatcher.LoginUser;
					}
					ownlist = callDisp.getdbHeler(context).ownLookUpRecordss(
							lUser);
					Log.i("HEART", "Login User Form====>"
							+ CallDispatcher.LoginUser);
					if (ownlist.size() != 0) {
						FormsListBean bean = new FormsListBean();
						bean.setForm_name("My Forms");
						lookup_adapter.addSeparatorItem(bean);
						for (int i = 0; i < ownlist.size(); i++) {

							lookup_adapter.addItem(ownlist.get(i));
							allitems.add(ownlist.get(i));

						}
					}

					buddylist = callDisp.getdbHeler(context)
							.BuddiesLookUpRecords();

					if (buddylist.size() != 0) {
						FormsListBean bean = new FormsListBean();
						bean.setForm_name("My Buddy's Forms");
						lookup_adapter.addSeparatorItem(bean);
						for (int i = 0; i < buddylist.size(); i++) {
							lookup_adapter.addItem(buddylist.get(i));
							allitems.add(buddylist.get(i));
						}
					}
					if (buddylist.size() > 0 || ownlist.size() > 0) {

						searchLayout.setVisibility(View.VISIBLE);
					} else {

						searchLayout.setVisibility(View.GONE);

					}
					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							lookup_adapter.notifyDataSetChanged();
						}
					});

				}
			});
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void populateListAfterDelete() {
		try {
			lookup_adapter.clearItem();

			if (ownlist != null) {
				ownlist.clear();
			}
			if (buddylist != null) {
				buddylist.clear();
			}
			if (allitems != null) {
				allitems.clear();
			}
			p = PreferenceManager.getDefaultSharedPreferences(context);

			final String username = p.getString("uname", null);
			ownlist = callDisp.getdbHeler(context).ownLookUpRecordss(username);

			if (ownlist.size() != 0) {
				FormsListBean bean = new FormsListBean();
				bean.setForm_name("My Forms");
				lookup_adapter.addSeparatorItem(bean);
				for (int i = 0; i < ownlist.size(); i++) {
					lookup_adapter.addItem(ownlist.get(i));
					lookup_adapter.notifyDataSetChanged();

					allitems.add(ownlist.get(i));

				}
			}

			buddylist = callDisp.getdbHeler(context).BuddiesLookUpRecords();
			if (buddylist.size() != 0) {
				FormsListBean bean = new FormsListBean();
				bean.setForm_name("My Buddy's Forms");
				lookup_adapter.addSeparatorItem(bean);
				for (int i = 0; i < buddylist.size(); i++) {
					lookup_adapter.addItem(buddylist.get(i));
					lookup_adapter.notifyDataSetChanged();

					allitems.add(buddylist.get(i));
				}
			}
			if (allitems.size() > 0) {
				// showprogress();
			}
			((MyCustomAdapter) lookup_view.getAdapter()).notifyDataSetChanged();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void ShowError(String Title, String Message) {

		try {
			confirmation = new AlertDialog.Builder(context).create();
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
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		try {
			// if (v.getId() == R.id.btn_add) {
			// if (CallDispatcher.LoginUser != null) {
			// Intent add_intent = new Intent(context, AddNewForm.class);
			// add_intent.putExtra("isvalid", false);
			// context.startActivity(add_intent);
			//
			// } else {
			//
			// ShowError("Error", "You Are In offline state");
			//
			// }
			//
			// }
			if (v.getId() == R.id.btn_set) {
				if (CallDispatcher.LoginUser != null) {
					if (!CallDispatcher.isWifiClosed) {

						Intent intentfrms = new Intent(context,
								FormSettings.class);
						context.startActivity(intentfrms);
					} else {
						ShowError("Network Error", "Network Unreachable");
					}
				} else {
					ShowError("Error", "Kindly Login");
				}
			} else if (v.getId() == R.id.btn_searchlist) {
				wservice_handler.post(new Runnable() {

					@Override
					public void run() {
						try {
							hideSoftKeyboard();
							if (ed_srch.getText().toString().trim().length() > 0) {
								SearchForms(ed_srch.getText().toString().trim());
							} else {
								showToast("Please enter values to search");
							}

						} catch (Exception e) {
							if (AppReference.isWriteInFile)
								AppReference.logger.error(e.getMessage(), e);
							e.printStackTrace();
						}
					}
				});

			}
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	public static class ViewHolder {
		public TextView textView;
		public TextView tv_describer;
		public ImageView iview;
		public ImageView formView;
	}

	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// try {
		// MemoryProcessor.getInstance().unbindDrawables(view);
		// view = null;
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// }
		// }).start();
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					MemoryProcessor.getInstance().unbindDrawables(view);
					view = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public View getParentView() {
		return view;
	}

	public void notifylistChanged(String name, String id, String owner) {
		try {
			lookup_adapter.clearItem();
			lookup_adapter.notifyDataSetChanged();
			FormsListBean bean = new FormsListBean();
			bean.setForm_id(id);
			bean.setForm_name(name);
			bean.setForm_ownser(owner);
			bean.setnumberof_rows("0");
			ownlist.add(0, bean);
			if (ownlist.size() != 0) {
				FormsListBean fbean = new FormsListBean();
				fbean.setForm_name("My Forms");
				lookup_adapter.addSeparatorItem(fbean);

				for (int i = 0; i < ownlist.size(); i++) {
					lookup_adapter.addItem(ownlist.get(i));

				}
			}
			if (buddylist.size() != 0) {
				FormsListBean fbean = new FormsListBean();
				fbean.setForm_name("My Buddy's Forms");
				lookup_adapter.addSeparatorItem(fbean);
				for (int i = 0; i < buddylist.size(); i++) {

					lookup_adapter.addItem(buddylist.get(i));

				}
			}
			populateLists();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	public void showprogress() {

		try {
			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
			dialog.setMessage("Progress ...");
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setProgress(0);
			dialog.setMax(100);
			dialog.show();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void cancelDialog() {
		try {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void showToast() {

		try {
			Toast.makeText(context, "NetWork Error!", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyWebServiceResponse(final Object obj) {
		try {
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {

					HashMap<String, String> dtype = new HashMap<String, String>();

					String tableID = null;
					if (obj instanceof Formsinfocontainer) {
						dtype.clear();
						final Formsinfocontainer bean = (Formsinfocontainer) obj;
						String[] fields1 = bean.getForm_fields().split(",");
						field = new ArrayList<String>();
						ArrayList<String> field_list = new ArrayList<String>();
						for (int i = 0; i < fields1.length - 1; i++) {
							String split_string[] = fields1[i].split(":");

							String sub = "[" + split_string[0] + "]";
							field_list.add(split_string[0]);
							field.add(sub);

							if (split_string[1].contains("4")) {

								dtype.put(split_string[0], "nvarchar(20)");

							} else if (split_string[1].contains("1")) {

								dtype.put(split_string[0], "INTEGER");
							} else if (split_string[1]
									.equalsIgnoreCase("LONGBLOB")) {
								dtype.put(split_string[0], "BLOB");
							} else {

								dtype.put(split_string[0], "nvarchar(20)");
							}

						}

						field_list.add("status");
						dtype.put("status", "nvarchar(20)");
						field.add("[status]");

						String tbl = "[" + bean.getForm_name() + "_"
								+ bean.getForm_id() + "]";

						if (callDisp.getdbHeler(context).isFormtableExists(
								bean.getForm_name() + "_" + bean.getForm_id())) {

							String tid_split[] = fields1[fields1.length - 1]
									.toString().split(":");
							field_list.remove("status");
							field_list.add(tid_split[0]);

							if (bean.getRec_list() != null) {

								for (int rec = 0; rec < bean.getRec_list()
										.size(); rec++) {

									FormRecordsbean field_values = bean
											.getRec_list().get(rec);

									HashMap<String, String> RecordInfo = field_values
											.getRecords_info();
									ContentValues cv = new ContentValues();

									if (field_values.getIsDeleted_record()) {

										if (callDisp
												.getdbHeler(context)
												.isRecordExists(
														"select * from "
																+ tbl
																+ "  where tableid="
																+ RecordInfo
																		.get("tableid"))) {
											String query = "delete from" + tbl
													+ " where tableid='"
													+ RecordInfo.get("tableid")
													+ "'";

											if (callDisp.getdbHeler(context)
													.ExecuteQuery(query)) {

											}
										}

									} else {

										for (int i = 0; i < RecordInfo.size(); i++) {

											String columnane = "["
													+ field_list.get(i) + "]";

											if (field_list
													.get(i)
													.equalsIgnoreCase("tableid")) {
												tableID = RecordInfo
														.get(field_list.get(i));
												cv.put(columnane, RecordInfo
														.get(field_list.get(i)));
											} else {

												if (dtype
														.get(field_list.get(i))
														.equalsIgnoreCase(
																"BLOB")) {
													cv.put(columnane,
															decodeBase64(RecordInfo
																	.get(field_list
																			.get(i))));
												} else if (dtype.get(
														field_list.get(i))
														.equalsIgnoreCase(
																"INTEGER")) {

													if (RecordInfo
															.get(field_list
																	.get(i)) != null) {

														if (RecordInfo
																.get(field_list
																		.get(i))
																.length() > 0) {
															cv.put(columnane,
																	Integer.parseInt(RecordInfo
																			.get(field_list
																					.get(i))));

														}
													} else {

														cv.put(columnane, 0);
													}

												} else if (dtype.get(
														field_list.get(i))
														.contains("2")) {

													if (RecordInfo
															.get(field_list
																	.get(i)) != null) {

														if (RecordInfo
																.get(field_list
																		.get(i))
																.contains(
																		"MPD_")
																|| RecordInfo
																		.get(field_list
																				.get(i))
																		.contains(
																				"MAD_")
																|| RecordInfo
																		.get(field_list
																				.get(i))
																		.contains(
																				"MVD_")) {

															File extStore = Environment
																	.getExternalStorageDirectory();
															File myFile = new File(
																	extStore.getAbsolutePath()
																			+ "/COMMedia/"
																			+ RecordInfo
																					.get(field_list
																							.get(i)));

															if (!myFile
																	.exists()) {
																downloadConfiguredNote(RecordInfo
																		.get(field_list
																				.get(i)));
																SingleInstance.formMultimediaRecords
																		.put(field_list
																				.get(i),
																				1);

															}
															cv.put(columnane,
																	RecordInfo
																			.get(field_list
																					.get(i)));

														} else {
															cv.put(columnane,
																	RecordInfo
																			.get(field_list
																					.get(i)));

														}
													} else {

														cv.put(columnane, "");

													}

												}

											}
										}

										cv.put("[status]", "1");

										if (callDisp.getdbHeler(context)
												.record(bean.getForm_name()
														+ "_"
														+ bean.getForm_id(),
														tableID)) {
											callDisp.getdbHeler(context)
													.update("["
															+ bean.getForm_name()
															+ "_"
															+ bean.getForm_id()
															+ "]", cv, tableID);

										} else {

											callDisp.getdbHeler(context)
													.insertForRecords(
															bean.getForm_name()
																	+ "_"
																	+ bean.getForm_id(),
															cv);

										}

									}
								}

							}

						}

						else {

							if (callDisp.getdbHeler(context).createFormTable(
									field, tbl, dtype)) {
								String tid_split[] = fields1[fields1.length - 1]
										.toString().split(":");
								field_list.remove("status");
								field_list.add(tid_split[0]);

								if (bean.getRec_list() != null) {

									for (int rec = 0; rec < bean.getRec_list()
											.size(); rec++) {

										FormRecordsbean recordsBean = bean
												.getRec_list().get(rec);
										HashMap<String, String> field_values = recordsBean
												.getRecords_info();

										ContentValues cv = new ContentValues();
										if (recordsBean.getIsDeleted_record()) {

											if (callDisp
													.getdbHeler(context)
													.isRecordExists(
															"select * from "
																	+ tbl
																	+ "  where tableid="
																	+ field_values
																			.get("tableid"))) {
												String query = "delete from"
														+ tbl
														+ " where tableid='"
														+ field_values
																.get("tableid")
														+ "'";

												if (callDisp
														.getdbHeler(context)
														.ExecuteQuery(query)) {

												}
											}

										} else {
											for (int i = 0; i < field_values
													.size(); i++) {
												String columnane = "["
														+ field_list.get(i)
														+ "]";

												if (field_list.get(i)
														.equalsIgnoreCase(
																"tableid")) {
													tableID = field_values
															.get(field_list
																	.get(i));
													cv.put(columnane,
															field_values
																	.get(field_list
																			.get(i)));
												} else {

													if (dtype.get(
															field_list.get(i))
															.equalsIgnoreCase(
																	"BLOB")) {
														cv.put(columnane,
																decodeBase64(field_values
																		.get(field_list
																				.get(i))));
													} else if (dtype.get(
															field_list.get(i))
															.equalsIgnoreCase(
																	"INTEGER")) {

														if (field_values.get(field_list
																.get(i)) != null) {

															if (field_values
																	.get(field_list
																			.get(i))
																	.length() > 0) {
																cv.put(columnane,
																		Integer.parseInt(field_values
																				.get(field_list
																						.get(i))));

															}
														} else {

															cv.put(columnane, 0);
														}

													} else if (dtype.get(
															field_list.get(i))
															.contains("2")) {

														if (field_values.get(field_list
																.get(i)) != null) {

															if (field_values
																	.get(field_list
																			.get(i))
																	.contains(
																			"MPD_")
																	|| field_values
																			.get(field_list
																					.get(i))
																			.contains(
																					"MAD_")
																	|| field_values
																			.get(field_list
																					.get(i))
																			.contains(
																					"MVD_")) {

																File extStore = Environment
																		.getExternalStorageDirectory();
																File myFile = new File(
																		extStore.getAbsolutePath()
																				+ "/COMMedia/"
																				+ field_values
																						.get(field_list
																								.get(i)));

																if (!myFile
																		.exists()) {
																	downloadConfiguredNote(field_values
																			.get(field_list
																					.get(i)));
																	SingleInstance.formMultimediaRecords
																			.put(field_list
																					.get(i),
																					1);

																}
																cv.put(columnane,
																		field_values
																				.get(field_list
																						.get(i)));
																// }

															} else {
																cv.put(columnane,
																		field_values
																				.get(field_list
																						.get(i)));

															}
														} else {

															cv.put(columnane,
																	"");

														}

													}

												}
											}

											cv.put("[status]", "1");

											callDisp.getdbHeler(context)
													.insertForRecords(
															bean.getForm_name()
																	+ "_"
																	+ bean.getForm_id(),
															cv);
										}
									}

								}

							}
						}

						if (WebServiceReferences.contextTable
								.containsKey("frmviewer")) {
							FormViewer view = (FormViewer) WebServiceReferences.contextTable
									.get("frmviewer");
							if (view.getId().equals(bean.getForm_id())) {
								view.refreshList();
							}
						} else {
							if (isForm_requested) {
								if (bean.getForm_id().equalsIgnoreCase(
										tableIDViewForm)) {

									isForm_requested = false;
									if (lookup_view.getAdapter().equals(
											lookup_adapter)) {
										FormsListBean bn = lookup_adapter
												.getItem(selected_index);

										Intent viewer_intent = new Intent(
												context, FormViewer.class);
										viewer_intent.putExtra("name",
												bean.getForm_name());
										viewer_intent.putExtra("id",
												bean.getForm_id());
										viewer_intent.putExtra("types", dtype);
										viewer_intent.putExtra("owner",
												bn.getForm_owner());

										startActivity(viewer_intent);

									} else if (lookup_view.getAdapter().equals(
											search_adapter)) {

										FormsListBean bn = search_adapter
												.getItem(selected_index);

										Intent viewer_intent = new Intent(
												context, FormViewer.class);
										viewer_intent.putExtra("name",
												bean.getForm_name());
										viewer_intent.putExtra("id",
												bean.getForm_id());
										viewer_intent.putExtra("types", dtype);
										viewer_intent.putExtra("owner",
												bn.getForm_owner());
										startActivity(viewer_intent);
									}

									cancelDialog();

								}
							} else {
								populateLists();
							}
						}

					} else if (obj instanceof WebServiceBean) {
						WebServiceBean wBean = (WebServiceBean) obj;
						showToast(wBean.getText());
					} else if (obj instanceof String) {
						showToast((String) obj);
					}
				}
			});
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			cancelDialog();
		}
	}

	private void showToast(final String msg) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void AddReordCount(String count) {

		try {
			FormsListBean bn = lookup_adapter.getItem(selected_index);
			bn.setnumberof_rows(count);
			lookup_adapter.addItematPosition(selected_index, bn);
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	private byte[] decodeBase64(String input) {
		try {
			byte[] decodedByte = Base64.decode(input, 0);
			Bitmap bmp = BitmapFactory.decodeByteArray(decodedByte, 0,
					decodedByte.length);

			Bitmap immagex = bmp;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
			byte[] b = baos.toByteArray();
			return b;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}

	}

	public void showDeleteAlert(final String[] args) {
		try {
			AlertDialog.Builder buider = new AlertDialog.Builder(context);
			buider.setMessage("Are you sure, You want to Delete this Form ?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									showprogress();

									if (WebServiceReferences.running) {

										String strQuery = "update formslookup set status='"
												+ "3"
												+ "' where tableid="
												+ args[1];
										if (callDisp.getdbHeler(context)
												.ExecuteQuery(strQuery)) {

											WebServiceReferences.webServiceClient
													.deleteForm(args,
															formsFragment);

										}
									} else {

										callDisp.startWebService(
												getResources().getString(
														R.string.service_url),
												"80");

										String strQuery = "update formslookup set status='"
												+ "3"
												+ "' where tableid="
												+ args[1];
										if (callDisp.getdbHeler(context)
												.ExecuteQuery(strQuery)) {
											WebServiceReferences.webServiceClient
													.deleteForm(args,
															formsFragment);

										}
									}

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
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

	// public void notifyForceLogout(String title, String msg) {
	//
	// try {
	// if (WebServiceReferences.contextTable.containsKey("frmreccreator")) {
	// FormRecordsCreators rec_creator = (FormRecordsCreators)
	// WebServiceReferences.contextTable
	// .get("frmreccreator");
	// rec_creator.finish();
	// }
	// if (WebServiceReferences.contextTable.containsKey("frmviewer")) {
	// FormViewer viewer = (FormViewer) WebServiceReferences.contextTable
	// .get("frmviewer");
	// viewer.finish();
	// }
	//
	// AlertDialog.Builder builder = new AlertDialog.Builder(context);
	// AlertDialog alert = null;
	//
	// builder.setMessage(msg).setPositiveButton("OK",
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// finish();
	//
	// }
	// });
	//
	// builder.setTitle(title);
	// alert = builder.create();
	//
	// alert.show();
	// } catch (Exception e) {
	// if (AppReference.isWriteInFile)
	// AppReference.logger.error(e.getMessage(), e);
	// e.printStackTrace();
	// }
	// }

	private void downloadConfiguredNote(String path) {

		try {
			if (path != null) {
				if (CallDispatcher.LoginUser != null) {
					callDisp.downloadOfflineresponse(path, "", "forms", "");

				}
			}
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void downloadinCompleted() {
		try {
			populateLists();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	// public void ShowList() {
	// try {
	// setContentView(R.layout.history_container);
	// slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
	// ArrayList<Slidebean> datas = new ArrayList<Slidebean>();
	// callDisp.composeList(datas);
	// slidemenu.init(FormsFragment.this, datas, FormsFragment.this, 100);
	// } catch (Exception e) {
	// if (AppReference.isWriteInFile)
	// AppReference.logger.error(e.getMessage(), e);
	// e.printStackTrace();
	// }
	//
	// }

	public void notifyWebServiceGetAttributeResponse(final Object obj,
			final String formnames) {
		try {
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {
					cancelDialog();
					if (obj instanceof ArrayList) {

						ArrayList response = (ArrayList) obj;

						for (int i = 0; i < response.size(); i++) {

							final Object obj = response.get(i);
							if (obj instanceof FormAttributeBean) {
								FormAttributeBean faBean = (FormAttributeBean) response
										.get(i);
								String tbl_name = "[" + formnames + "]";
								String col_name = "[" + faBean.getFieldname()
										+ "]";
								Log.i("ff123",
										"fattid : " + faBean.getAttributeid());
								// String insertQueryinfotbl =
								// "insert into forminfo(tablename,column,entrymode,validdata,defaultvalue,instruction,errortip)"
								// + "values('"
								// + tbl_name
								// + "','"
								// + col_name
								// + "','"
								// + bib.getEntry()
								// + "','"
								// + bib.getDatavalidation()
								// + "','"
								// + bib.getDefaultvalue()
								// + "','"
								// + bib.getInstruction()
								// + "','"
								// + bib.getErrortip() + "')";

								String instruction = faBean.getInstruction();

								if (instruction.length() != 0) {
									String[] instructions = instruction
											.split(",");

									for (int j = 0; j < instructions.length; j++) {
										File extStore = Environment
												.getExternalStorageDirectory();
										File myFile = new File(extStore
												.getAbsolutePath()
												+ "/COMMedia/"
												+ instructions[j]);

										if (!myFile.exists()) {
											callDisp.downloadOfflineresponse(
													instructions[j], "",
													"forms", "");

										}

									}

								}

								// if
								// (callDisp.getdbHeler(context).ExecuteQuery(
								// insertQueryinfotbl)) {
								//
								// }
								DBAccess.getdbHeler().saveOrUpdateFormInfo(
										tbl_name, col_name, faBean);

							}
						}

					}

					else if (obj instanceof WebServiceBean) {

					}
				}
			});
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void notifyFormDeletionRespose(final Object obj) {
		try {
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					if (obj instanceof FormsBean) {
						FormsBean bean = (FormsBean) obj;
						if (callDisp.getdbHeler(context).isFormExists(
								bean.getFormId(), bean.getUserName())) {
							String del_row = "delete from formslookup where tableid='"
									+ bean.getFormId()
									+ "' and owner='"
									+ bean.getUserName() + "'";
							if (callDisp.getdbHeler(context).ExecuteQuery(
									del_row)) {
								if (callDisp.getdbHeler(context)
										.isFormtableExists(
												bean.getFormName() + "_"
														+ bean.getFormId())) {
									String del_tbl = "DROP TABLE IF EXISTS'"
											+ bean.getFormName() + "_"
											+ bean.getFormId() + "'";
									if (callDisp.getdbHeler(context)
											.ExecuteQuery(del_tbl)) {
										String del = "delete from forminfo where tablename='["
												+ bean.getFormName()
												+ "_"
												+ bean.getFormId() + "]'";
										if (callDisp.getdbHeler(context)
												.ExecuteQuery(del)) {
											String del_settings = "delete from formsettings where formid='"
													+ bean.getFormId() + "'";
											callDisp.getdbHeler(context)
													.ExecuteQuery(del_settings);

											populateListAfterDelete();

										}

									}
								} else {
									populateListAfterDelete();
								}
							}
						}
					} else if (obj instanceof String[]) {

					} else if (obj instanceof WebServiceBean) {
						WebServiceBean bn = (WebServiceBean) obj;
						showToast(bn.getText());
					}
				}
			});
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	private void Offlinedataentry(String owener, String formid, String formname) {
		try {
			if (CallDispatcher.LoginUser != null) {

				Intent viewer_intent = new Intent(context, FormViewer.class);
				viewer_intent.putExtra("name", formname);
				viewer_intent.putExtra("id", formid);

				viewer_intent.putExtra("types", dtype);
				viewer_intent.putExtra("owner", owener);
				context.startActivity(viewer_intent);

			} else {
				p = PreferenceManager.getDefaultSharedPreferences(context);

				p.getString("uname", null);

				String sync = callDisp.getdbHeler(context)
						.getColumnofSyncoffline(owener, formid);

				if (sync != null) {

					if (sync.contains("1")) {
						ShowError("Error", "Never Sync to View this Form");
					} else {
						p = PreferenceManager
								.getDefaultSharedPreferences(context);

						final String usernames = p.getString("uname", null);

						list = callDisp.getdbHeler(context)
								.getColumnofAcessoffline(usernames, formid);

						if (list != null) {
							if (list.contains("1")) {

								ShowError("Error",
										"No Access to View this Form");

							} else {

								Intent viewer_intent = new Intent(context,
										FormViewer.class);
								viewer_intent.putExtra("name", formname);
								viewer_intent.putExtra("id", formid);
								viewer_intent.putExtra("types", dtype);
								viewer_intent.putExtra("owner", owener);
								context.startActivity(viewer_intent);

							}

						} else {

							Intent viewer_intent = new Intent(context,
									FormViewer.class);
							viewer_intent.putExtra("name", formname);
							viewer_intent.putExtra("id", formid);
							viewer_intent.putExtra("types", dtype);
							viewer_intent.putExtra("owner", owener);
							context.startActivity(viewer_intent);

						}

					}

				}

			}
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	public PopupWindow popupWindowMenus(String formId, String formowner) {
		try {
			PopupWindow popupWindow = new PopupWindow(context);

			ListView listViewMenus = new ListView(context);

			if (formowner.equalsIgnoreCase(CallDispatcher.LoginUser)) {
				listViewMenus.setAdapter(callDisp.menusAdapter(popUpContents,
						context));

			} else {
				listViewMenus.setAdapter(callDisp.menusAdapter(
						popUpContentsBuddy, context));

			}

			listViewMenus.setOnItemClickListener(new OnItemClickListener() {

				@SuppressWarnings("unused")
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					popupWindowMenus.dismiss();
					String username;
					if (CallDispatcher.LoginUser != null)
						username = CallDispatcher.LoginUser;
					else {
						SharedPreferences pref = PreferenceManager
								.getDefaultSharedPreferences(context);
						username = pref.getString("uname", "");
					}
					if (position == 0) {

						// doViewForms(username);
					}

					else if (position == 1) {

						// doAddRecords(false, username);
					}

				}

			});
			popupWindow.setFocusable(true);
			popupWindow.setWidth(220);
			popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

			popupWindow.setContentView(listViewMenus);

			return popupWindow;
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * adapter where the list values will be set
	 */

	private void doDeleteForms(int position, String username) {

		try {
			final FormsListBean bean = (FormsListBean) lookup_adapter
					.getItem(position);
			if (bean.getForm_owner().equalsIgnoreCase(username)) {
				String[] accessSetting = callDisp.getdbHeler(context)
						.getAccessRights(bean.getFormId(),
								CallDispatcher.LoginUser);
				if (accessSetting != null) {

					if (accessSetting[0].toString().equalsIgnoreCase("A04")) {
						String[] params = { username, bean.getFormId() };

						showDeleteAlert(params);

					} else {
						Toast.makeText(context,
								"Sorry you don't have permission to delete",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(context,
							"Sorry you don't have permission to delete",
							Toast.LENGTH_LONG).show();
				}
			}
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	// @SuppressWarnings("unused")
	// private void doAddRecords(boolean addwithcall, String username) {
	// try {
	// final FormsListBean bean = (FormsListBean) lookup_adapter
	// .getItem(CallDispatcher.formMenu);
	// String tableId = callDisp.getdbHeler(context).getFormName(
	// bean.getFormId());
	//
	// if (tableId != null || tableId != "") {
	// String[] accessSetting = callDisp.getdbHeler(context)
	// .getAccessRights(bean.getFormId(), username);
	//
	// if (accessSetting != null) {
	//
	// if (((accessSetting[0].toString().equalsIgnoreCase("A03"))
	// || (accessSetting[0].toString()
	// .equalsIgnoreCase("A04")) || (accessSetting[0]
	// .toString().equalsIgnoreCase("A05")))) {
	//
	// lists = callDisp.getdbHeler(context).getColumnNames(
	// bean.getForm_name() + "_" + bean.getFormId());
	// dtype = callDisp.getdbHeler(context).getColumnTypes(
	// bean.getForm_name() + "_" + bean.getFormId());
	// ArrayList<String> creator_list = (ArrayList<String>) lists
	// .clone();
	// creator_list.remove("uuid");
	// creator_list.remove("euuid");
	// creator_list.remove("uudate");
	// creator_list.remove("recorddate");
	// creator_list.remove("status");
	// if (addwithcall) {
	//
	// if (AppReference.sip_accountID != -1) {
	// Intent rec_intent = new Intent(context,
	// FormRecordsCreators.class);
	// rec_intent.putExtra("update", false);
	// rec_intent.putExtra("addwithsipcall",
	// addwithcall);
	// rec_intent.putExtra("call_list",
	// AppReference.process_members);
	//
	// rec_intent.putExtra("list", creator_list);
	// rec_intent.putExtra("title",
	// bean.getForm_name());
	// rec_intent.putExtra("id", bean.getFormId());
	// rec_intent.putExtra("types", dtype);
	// startActivity(rec_intent);
	// } else {
	//
	// Toast.makeText(context,
	// "Please Register to make call",
	// Toast.LENGTH_LONG).show();
	// }
	//
	// } else {
	// Intent rec_intent = new Intent(context,
	// FormRecordsCreators.class);
	// rec_intent.putExtra("update", false);
	// rec_intent.putExtra("addwithsipcall", addwithcall);
	// rec_intent.putExtra("call_list", "");
	//
	// rec_intent.putExtra("list", creator_list);
	// rec_intent.putExtra("title", bean.getForm_name());
	// rec_intent.putExtra("id", bean.getFormId());
	// rec_intent.putExtra("types", dtype);
	// startActivity(rec_intent);
	//
	// }
	// } else {
	// Toast.makeText(
	// context,
	// "Sorry you don't have permission to add records",
	// Toast.LENGTH_LONG).show();
	// }
	// } else {
	// Toast.makeText(context,
	// "Sorry you don't have permission to add records",
	// Toast.LENGTH_LONG).show();
	// }
	// } else {
	// Toast.makeText(
	// context,
	// "Sorry, your buddy delete this form/remove permission to access this form",
	// Toast.LENGTH_LONG).show();
	// }
	// } catch (Exception e) {
	// if (AppReference.isWriteInFile)
	// AppReference.logger.error(e.getMessage(), e);
	// e.printStackTrace();
	// }
	// }

	@SuppressWarnings("unused")
	private void doViewForms(String username, int position) {

		try {
			final FormsListBean bean = (FormsListBean) lookup_adapter
					.getItem(position);
			String tableId = callDisp.getdbHeler(context).getFormName(
					bean.getFormId());
			if (tableId != null || tableId != "") {
				String[] accessSetting = callDisp.getdbHeler(context)
						.getAccessRights(bean.getFormId(), username);
				if (accessSetting != null) {
					if ((accessSetting[0].toString().equalsIgnoreCase("A03"))
							|| (accessSetting[0].toString()
									.equalsIgnoreCase("A04"))
							|| (accessSetting[0].toString()
									.equalsIgnoreCase("A02"))
							|| (accessSetting[0].toString()
									.equalsIgnoreCase("A05"))) {
						if (callDisp.getdbHeler(context).isFormtableExists(
								bean.getForm_name() + "_" + bean.getFormId())) {

							dtype.clear();

							dtype = callDisp.getdbHeler(context)
									.getColumnTypes(
											bean.getForm_name() + "_"
													+ bean.getFormId());
							list = callDisp.getdbHeler(context)
									.getColumnofAcess(bean.getForm_owner(),
											bean.getFormId());

							Offlinedataentry(bean.getForm_owner(),
									bean.getFormId(), bean.getForm_name());

						} else {
							showprogress();

							if (WebServiceReferences.running) {
								WebServiceReferences.webServiceClient
										.Getcontent(CallDispatcher.LoginUser,
												bean.getFormId(), "",
												formsFragment);

							} else {
								callDisp.startWebService(getResources()
										.getString(R.string.service_url), "80");
								WebServiceReferences.webServiceClient
										.Getcontent(CallDispatcher.LoginUser,
												bean.getFormId(), "",
												formsFragment);

							}
							tableIDViewForm = bean.getFormId();
							isForm_requested = true;

						}
					} else {
						Toast.makeText(
								context,
								"Sorry you don't have permission to view records",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(context, "Forms settings loading...",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(
						context,
						"Sorry, your buddy delete this form/remove permission to access this form",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// // TODO Auto-generated method stub
	// if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	// if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	// AlertDialog.Builder buider = new AlertDialog.Builder(context);
	// buider.setMessage(
	// "Are you sure, You want to Send this application to Background ?")
	// .setPositiveButton("Yes",
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// // TODO Auto-generated method stub
	// // finish();
	// moveTaskToBack(true);
	// // return true;
	//
	// }
	// })
	// .setNegativeButton("No",
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// // TODO Auto-generated method stub
	// dialog.cancel();
	// }
	// });
	// AlertDialog alert = buider.create();
	// alert.show();
	// }
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	// void showSingleSelectBuddy() {
	//
	// try {
	//
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// builder.create();
	// CallDispatcher callDisp = (CallDispatcher)
	// WebServiceReferences.callDispatch
	// .get("calldispatch");
	// final CharSequence[] choiceList = callDisp.getOnlineBuddys();
	//
	// int selected = -1; // does not select anything
	//
	// builder.setSingleChoiceItems(choiceList, selected,
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// AppReference.process_members.clear();
	// String To = choiceList[which].toString();
	// CallerBean callerBean = new CallerBean();
	// callerBean.setUserName(To);
	// callerBean.setToNnumber(To);
	// callerBean.setPresense("Connecting");
	// callerBean.setHold(0);
	// callerBean.setMute(0);
	// callerBean.setCall_id(-1);
	// AppReference.process_members.add(callerBean);
	//
	// }
	// });
	//
	// builder.setPositiveButton("OK",
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// if (AppReference.process_members.size() != 0) {
	//
	// String username;
	// if (CallDispatcher.LoginUser != null)
	// username = CallDispatcher.LoginUser;
	// else {
	// SharedPreferences pref = PreferenceManager
	// .getDefaultSharedPreferences(getApplicationContext());
	// username = pref.getString("uname", "");
	// }
	// doAddRecords(true, username);
	// alert.dismiss();
	//
	// } else {
	//
	// Toast.makeText(context, "Select any buddy",
	// Toast.LENGTH_LONG).show();
	// }
	//
	// }
	// });
	// alert = builder.create();
	// if (choiceList != null) {
	// if (choiceList.length != 0) {
	// alert.show();
	// } else {
	// Toast.makeText(context, "No Online buddy",
	// Toast.LENGTH_LONG).show();
	//
	// }
	// }
	//
	// } catch (Exception e) {
	// if (AppReference.isWriteInFile)
	// AppReference.logger.error(e.getMessage(), e);
	// e.printStackTrace();
	// }
	// }

	public class MySearchAdapter extends BaseAdapter {

		private ArrayList<FormsListBean> mData;
		private LayoutInflater mInflater;
		Context context;
		CallDispatcher callDisp;
		Bitmap formIcon;

		public MySearchAdapter(Context cont) {
			this.context = cont;

			mInflater = LayoutInflater.from(context);
			mData = new ArrayList<FormsListBean>();
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldispatch");
		}

		public ArrayList<FormsListBean> getallItems() {
			return this.mData;
		}

		public void addItematPosition(int position, FormsListBean bean) {
			try {
				mData.remove(position);
				mData.add(position, bean);
				notifyDataSetChanged();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void addItem(final FormsListBean bean) {
			mData.add(bean);
		}

		public void clearItem() {

			mData.clear();
			notifyDataSetChanged();
		}

		public void addAll(ArrayList<FormsListBean> beanlist) {
			mData.clear();
			mData = beanlist;
		}

		public void addSeparatorItem(final FormsListBean item) {
			mData.add(item);
		}

		public int getCount() {
			return mData.size();
		}

		public FormsListBean getItem(int position) {
			return mData.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			try {
				ViewHolder holder = null;

				if (convertView == null) {
					holder = new ViewHolder();
					convertView = mInflater.inflate(R.layout.list_row, null);
					holder.textView = (TextView) convertView
							.findViewById(R.id.tview_lvrow);
					holder.textView.setTypeface (tf_regular);
					holder.textView.setTextColor(Color.parseColor("#696969"));
					holder.iview = (ImageView) convertView
							.findViewById(R.id.usr_icn);
					holder.iview
							.setBackgroundResource(R.drawable.ic_action_overflow);
					holder.formView = (ImageView) convertView
							.findViewById(R.id.form_icn);

					holder.tv_describer = (TextView) convertView
							.findViewById(R.id.tview_lvrow1);
					holder.tv_describer.setTypeface (tf_regular);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.textView.setText((((FormsListBean) mData.get(position))
						.getForm_name()));
				holder.textView.setTypeface(null, Typeface.BOLD);

				if (((FormsListBean) mData.get(position)).getFormicon() != null) {

					holder.formView
							.setImageBitmap(callDisp.setIconImage(formIcon,
									((FormsListBean) mData.get(position))
											.getFormicon()));
				} else {

					if (holder.formView != null) {
						Bitmap bitmap = BitmapFactory.decodeResource(
								context.getResources(), R.drawable.form_icon);
						holder.formView.setImageBitmap(bitmap);
					}

				}

				holder.tv_describer.setTextColor(Color.parseColor("#A9A9A9"));
				holder.tv_describer.setText("Owner : "
						+ ((FormsListBean) mData.get(position)).getForm_owner()
						+ "  Records : "
						+ ((FormsListBean) mData.get(position)).getNoofRows());

				return convertView;
			} catch (Exception e) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				e.printStackTrace();
				return null;
			}
		}
	}

	public class MyCustomAdapter extends BaseAdapter {

		private static final int TYPE_ITEM = 0;
		private static final int TYPE_SEPARATOR = 1;
		private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

		private ArrayList<FormsListBean> mData;
		private LayoutInflater mInflater;
		Context context;
		private TreeSet<Integer> mSeparatorsSet;
		CallDispatcher callDisp;
		Bitmap formIcon;
		ImageLoader imageLoader = null;

		public MyCustomAdapter(Context cont) {
			this.context = cont;

			mInflater = LayoutInflater.from(context);
			mData = new ArrayList<FormsListBean>();
			mSeparatorsSet = new TreeSet<Integer>();
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
			imageLoader = new ImageLoader(cont);
		}

		public ArrayList<FormsListBean> getallItems() {
			return this.mData;
		}

		public void addItematPosition(int position, FormsListBean bean) {
			mData.remove(position);
			mData.add(position, bean);
			this.notifyDataSetChanged();
		}

		public void addItem(final FormsListBean bean) {
			mData.add(bean);
		}

		public void clearItem() {

			mData.clear();
			mSeparatorsSet.clear();
			this.notifyDataSetChanged();
		}

		public void addAll(ArrayList<FormsListBean> beanlist) {
			mData.clear();
			mData.addAll(beanlist);
		}

		public void addSeparatorItem(final FormsListBean item) {
			mData.add(item);
			mSeparatorsSet.add(mData.size() - 1);
		}

		@Override
		public int getItemViewType(int position) {
			if (mSeparatorsSet.size() > 0) {
				return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR
						: TYPE_ITEM;
			} else {

				return TYPE_ITEM;
			}

		}

		@Override
		public int getViewTypeCount() {
			return TYPE_MAX_COUNT;
		}

		public int getCount() {
			return mData.size();
		}

		public FormsListBean getItem(int position) {
			return mData.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			try {
				ViewHolder holder = null;
				int type = getItemViewType(position);

				if (convertView == null) {
					holder = new ViewHolder();
					switch (type) {
					case TYPE_ITEM:
						convertView = mInflater
								.inflate(R.layout.list_row, null);
						holder.textView = (TextView) convertView
								.findViewById(R.id.tview_lvrow);
						holder.textView.setTextColor(Color
								.parseColor("#696969"));
						holder.iview = (ImageView) convertView
								.findViewById(R.id.usr_icn);
						holder.iview.setVisibility(View.INVISIBLE);
						holder.iview
								.setBackgroundResource(R.drawable.ic_action_overflow);
						holder.formView = (ImageView) convertView
								.findViewById(R.id.form_icn);

						holder.tv_describer = (TextView) convertView
								.findViewById(R.id.tview_lvrow1);
						break;
					case TYPE_SEPARATOR:
						convertView = mInflater.inflate(
								R.layout.section_header, null);
						holder.textView = (TextView) convertView
								.findViewById(R.id.tv_header);
						holder.textView.setTypeface (tf_regular);
						holder.textView.setTextColor(Color
								.parseColor("#FFFFFF"));
						break;
					}
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.textView.setText(((FormsListBean) mData.get(position))
						.getForm_name());
				holder.textView.setTypeface(null, Typeface.BOLD);
				if (((FormsListBean) mData.get(position)).getFormicon() != null) {

					// holder.formView
					// .setImageBitmap(callDisp.setIconImage(formIcon,
					// ((FormsListBean) mData.get(position))
					// .getFormicon()));

					imageLoader.DisplayImage(Utils.getFilePathString(mData.get(
							position).getFormicon()), holder.formView,
							R.drawable.menu_form_icon);
				} else {

					if (holder.formView != null) {
						// Bitmap bitmap = BitmapFactory.decodeResource(
						// context.getResources(),
						// R.drawable.menu_form_icon);
						// holder.formView.setImageBitmap(bitmap);
						imageLoader.DisplayImage(Utils.getFilePathString(mData
								.get(position).getFormicon()), holder.formView,
								R.drawable.menu_form_icon);
					}

				}

				if (type == TYPE_ITEM) {
					holder.tv_describer.setTextColor(Color
							.parseColor("#A9A9A9"));
					holder.tv_describer.setText("Owner : "
							+ ((FormsListBean) mData.get(position))
									.getForm_owner()
							+ "  Records : "
							+ ((FormsListBean) mData.get(position))
									.getNoofRows());

				}
				return convertView;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return convertView;
			}
		}
	}

	public void notifyFileUploadError() {
		try {
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, "Uploading Failed",
							Toast.LENGTH_LONG).show();
					cancelDialog();
				}
			});
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void hideSoftKeyboard() {
		try {
			if (getActivity().getCurrentFocus() != null) {
				// InputMethodManager inputMethodManager = (InputMethodManager)
				// getSystemService(INPUT_METHOD_SERVICE);
				// inputMethodManager.hideSoftInputFromWindow(
				// search_button.getWindowToken(), 0);
			}
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void notifyFileDownloadError() {
		try {
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, "Downloading Failed ",
							Toast.LENGTH_LONG).show();

				}
			});
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void notifyProfilepictureDownloaded() {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					if (slidemenu != null) {
						if (slidemenu.isShown())
							slidemenu.refreshItem();
					}
				}
			});
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	// @Override
	// public void onSlideMenuItemClick(int itemId, View v, Context context) {
	// try {
	// // TODO Auto-generated method stub
	//
	// switch (itemId) {
	// case WebServiceReferences.CONTACTS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.USERPROFILE:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
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
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.CLONE:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.SETTINGS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	//
	// case WebServiceReferences.QUICK_ACTION:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.FORMS:
	//
	// break;
	//
	// default:
	// break;
	// }
	// } catch (Exception e) {
	// if (AppReference.isWriteInFile)
	// AppReference.logger.error(e.getMessage(), e);
	// e.printStackTrace();
	// }
	//
	// }

	@Override
	public void notifyReceivedIM(final SignalingBean sb) {
		try {
			// TODO Auto-generated method stub
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					IMRequest.setVisibility(View.VISIBLE);
					IMRequest.setEnabled(true);

					IMRequest
							.setBackgroundResource(R.drawable.small_blue_balloon);

					if (!callDisp.getdbHeler(context)
							.userChatting(sb.getFrom())) {
						callDisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
								CallDispatcher.LoginUser, 1,
								CallDispatcher.LoginUser);
					}

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private SwipeMenuCreator intiateSwipeList() {
		try {
			SwipeMenuCreator creator = new SwipeMenuCreator() {

				@Override
				public void create(SwipeMenu menu) {
					// create "open" item
					SwipeMenuItem openItem = new SwipeMenuItem(context);
					// set item background
					openItem.setBackground(new ColorDrawable(Color.rgb(0xC9,
							0xC9, 0xCE)));
					// set item width
					openItem.setWidth(dp2px(90));
					// set item title
					openItem.setTitle("Edit");
					// set item title fontsize
					openItem.setTitleSize(18);
					// set item title font color
					openItem.setTitleColor(Color.WHITE);
					// add to menu
					menu.addMenuItem(openItem);

					// create "delete" item
					SwipeMenuItem deleteItem = new SwipeMenuItem(context);
					// set item background
					deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
							0x3F, 0x25)));
					// set item width
					deleteItem.setWidth(dp2px(90));
					// set a icon
					deleteItem.setIcon(R.drawable.ic_action_delete);
					// add to menu
					menu.addMenuItem(deleteItem);
				}
			};
			return creator;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	// @Override
	// public void onResume() {
	// // TODO Auto-generated method stub
	// if (WebServiceReferences.Imcollection.size() == 0)
	// IMRequest.setVisibility(View.GONE);
	// else
	// IMRequest.setVisibility(View.VISIBLE);
	// super.onResume();
	// }
}
