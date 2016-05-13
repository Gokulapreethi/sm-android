package com.main;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.adapters.CustomQaAdapter;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.forms.FormViewer;
import com.cg.forms.formItem;
import com.cg.quickaction.ContactLogicbean;
import com.cg.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;
import com.process.MemoryProcessor;
import com.util.SingleInstance;

public class AppsViewFragment extends Fragment {

	// private Context context = null;

	private ArrayList<formItem> quickaction = new ArrayList<formItem>();
	private Handler handler = new Handler();
	static int totalLayouts;
	// private Button IMRequest;
	// private Button form_settings = null;
	private boolean isSelected = false;
	boolean isreject = false;
	// private SlideMenu slidemenu;
	private static CallDispatcher callDisp;
	static int buddyStatus = 0;
	private GridView gridView, QAgridview;
	private LinearLayout noapp, gridlay, noQA;
	private Button newapp, addQA, quickAction, apps = null;
	private ArrayList<formItem> listValues = new ArrayList<formItem>();
	private ArrayList<formItem> SearchlistValues = new ArrayList<formItem>();
	private ArrayList<formItem> Searchquickaction = new ArrayList<formItem>();
	private StickyGridHeadersSimpleArrayAdapter customGridAdapter = null;
	private CustomQaAdapter customGridAdapters = null;
	private Bitmap homeIcon = null;
	private TextView iview=null;

	private static AppsViewFragment appsViewFragment;

	private static Context context;

	// private Button plus = null; // this button plus hide in this page,this
	// button create fragment xml

	public View view = null;

	public static AppsViewFragment newInstance(Context maincontext) {
		try {
			if (appsViewFragment == null) {
				context = maincontext;
				appsViewFragment = new AppsViewFragment();
				callDisp = CallDispatcher.getCallDispatcher(context);
			}

			return appsViewFragment;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return appsViewFragment;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {

			Button select = (Button) getActivity().findViewById(R.id.btn_brg);
			select.setVisibility(View.GONE);
			RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
			mainHeader.setVisibility(View.VISIBLE);
			LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
			contact_layout.setVisibility(View.GONE);
			TextView title = (TextView) getActivity().findViewById(
					R.id.activity_main_content_title);
		
			title.setVisibility(View.VISIBLE);

			Button imVw = (Button) getActivity().findViewById(R.id.im_view);
			imVw.setVisibility(View.GONE);

			Button setting = (Button) getActivity().findViewById(
					R.id.btn_settings);
			setting.setVisibility(View.GONE);
			setting.setText("");

			Button plusBtn = (Button) getActivity()
					.findViewById(R.id.add_group);
			plusBtn.setVisibility(View.VISIBLE);
			plusBtn.setText("");

			plusBtn.setBackgroundResource(R.drawable.toolbar_buttons_plus);

			plusBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (isSelected) {

							handler.post(new Runnable() {
								@Override
								public void run() {

									// TODO Auto-generated method stub
									try {
										TextView title = (TextView) getActivity().findViewById(
												R.id.activity_main_content_title);
										title.setText("Quick Action");
										title.setVisibility(View.VISIBLE);


									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
							backBtn.setVisibility(View.GONE);

							SingleInstance.quickViewShow = true;
							QuickActionFragment quickActionFragment = QuickActionFragment
									.newInstance(context);

							FragmentManager fragmentManager = getFragmentManager();
							FragmentTransaction fragmentTransaction = fragmentManager
									.beginTransaction();
							fragmentTransaction.replace(
									R.id.activity_main_content_fragment,
									quickActionFragment);
							fragmentTransaction.commit();

						} else {

							FormsFragment formsFragment = FormsFragment
									.newInstance(context);
							FragmentManager fragmentManager = getFragmentManager();
							FragmentTransaction fragmentTransaction = fragmentManager
									.beginTransaction();
							fragmentTransaction.replace(
									R.id.activity_main_content_fragment,
									formsFragment);
							fragmentTransaction.commit();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});


			view = null;

			if (view == null) {
				view = inflater.inflate(R.layout.appsview, null);

				isSelected = false;

				apps = (Button) view.findViewById(R.id.appsviewbtn);
				quickAction = (Button) view.findViewById(R.id.quickbtn);
				iview = (TextView)view.findViewById(R.id.iview);


				newapp = (Button) view.findViewById(R.id.addanapp);
				// addapp = (Button) findViewById(R.id.appnew);
				addQA = (Button) view.findViewById(R.id.addanapp1);

				quickaction = callDisp.getdbHeler(context).BLDatasOverall();

				noapp = (LinearLayout) view.findViewById(R.id.level);
				gridlay = (LinearLayout) view
						.findViewById(R.id.layout_maincontainer);
				noQA = (LinearLayout) view.findViewById(R.id.levelQA);

				homeIcon = BitmapFactory.decodeResource(getResources(),
						R.drawable.gridfmicon);
				listValues = callDisp.getdbHeler(context).LookUpRecords(
						homeIcon, CallDispatcher.LoginUser);

				gridView = (GridView) view.findViewById(R.id.gridView1);
				QAgridview = (GridView) view.findViewById(R.id.gridViewQA);
				customGridAdapters = new CustomQaAdapter(context,
						R.layout.row_grid, quickaction);
				QAgridview.setAdapter(customGridAdapters);
				customGridAdapter = new StickyGridHeadersSimpleArrayAdapter(
						context, listValues, R.layout.header, R.layout.row_grid);
				// form_settings = (Button) findViewById(R.id.form_settings);
				customGridAdapter.notifyDataSetChanged();
				if (listValues.size() > 0) {

					gridView.setAdapter(customGridAdapter);

				}

				if (listValues.size() <= 0) {
					noapp.setVisibility(View.VISIBLE);
					gridlay.setVisibility(View.GONE);
					// form_settings.setVisibility(View.GONE);
				} else {
					noapp.setVisibility(View.GONE);
					gridlay.setVisibility(View.VISIBLE);
					// form_settings.setVisibility(View.VISIBLE);

				}

				// searchBar.addTextChangedListener(new TextWatcher() {
				//
				// @Override
				// public void onTextChanged(CharSequence s, int start, int
				// before,
				// int count) {
				// // TODO Auto-generated method stub
				// if (searchBar.getText().toString().length() == 0) {
				// refreshIcon();
				//
				// }
				//
				// }
				//
				// @Override
				// public void beforeTextChanged(CharSequence s, int start, int
				// count,
				// int after) {
				// // TODO Auto-generated method stub
				//
				// }
				//
				// @Override
				// public void afterTextChanged(Editable s) {
				// // TODO Auto-generated method stub
				//
				// if (searchBar.getText().toString().length()==0) {
				// listValues.clear();
				// quickaction.clear();
				//
				// listValues =
				// callDisp.getdbHeler(context).LookUpRecords(homeIcon);
				// if (listValues.size()>0) {
				// customGridAdapter.notifyDataSetChanged();
				//
				// }
				//
				// quickaction = callDisp.getdbHeler(context).BLDatasOverall();
				// if (quickaction.size()>0) {
				// customGridAdapters.notifyDataSetChanged();
				// }
				//
				// }
				//
				// }
				// });
				apps.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// searchBar.setText("");
						isSelected = false;
						iview.setText(SingleInstance.mainContext.getResources().getString(R.string.your_app_list_is_empty));
						newapp.setText(SingleInstance.mainContext.getResources().getString(R.string.add_a_new_app));
						gridView.setVisibility(View.VISIBLE);

						if (listValues.size() > 0) {
							noapp.setVisibility(View.GONE);
							gridlay.setVisibility(View.VISIBLE);
							gridView.setVisibility(View.VISIBLE);
							// form_settings.setVisibility(View.VISIBLE);
						} else {
							noapp.setVisibility(View.VISIBLE);
							gridlay.setVisibility(View.GONE);
							gridView.setVisibility(View.GONE);
							// form_settings.setVisibility(View.GONE);
						}
						QAgridview.setVisibility(View.GONE);
						noQA.setVisibility(View.GONE);
						apps.setBackgroundResource(R.drawable.rounded_bordercolor_app);
						apps.setTextColor(getResources()
								.getColor(R.color.white));
						quickAction
								.setBackgroundResource(R.drawable.rounded_bordercolor_app2);
						quickAction.setTextColor(getResources().getColor(
								R.color.title));

					}
				});

				quickAction.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// searchBar.setText("");
						isSelected = true;
						iview.setText(SingleInstance.mainContext.getResources().getString(R.string.your_quick_action_list_is_empty));
						newapp.setText(SingleInstance.mainContext.getResources().getString(R.string.add_a_new_qa));
						quickAction
								.setBackgroundResource(R.drawable.rounded_bordercolor_app);
						quickAction.setTextColor(getResources().getColor(
								R.color.white));
						apps.setBackgroundResource(R.drawable.rounded_bordercolor_app2);
						apps.setTextColor(getResources()
								.getColor(R.color.title));
						// form_settings.setVisibility(View.GONE);

						if (quickaction.size() != 0) {
							noapp.setVisibility(View.GONE);
							gridlay.setVisibility(View.VISIBLE);
							gridView.setVisibility(View.GONE);
							QAgridview.setVisibility(View.VISIBLE);
						} else {
							gridView.setVisibility(View.GONE);

							noQA.setVisibility(View.VISIBLE);
							QAgridview.setVisibility(View.GONE);
							noapp.setVisibility(View.VISIBLE);
							gridlay.setVisibility(View.GONE);

						}
					}
				});
				addQA.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						QuickActionFragment quickActionFragment = QuickActionFragment
								.newInstance(context);
						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager
								.beginTransaction();
						fragmentTransaction.replace(
								R.id.activity_main_content_fragment,
								quickActionFragment);
						fragmentTransaction.commit();

					}
				});

				// form_settings.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// Intent intent = new Intent(context, FormSettings.class);
				// context.startActivity(intent);
				// getActivity().finish();
				// }
				// });
				newapp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// Intent intentfrms = new Intent(context,
						// FormsActivity.class);
						// context.startActivity(intentfrms);
						// getActivity().finish();
						if(!isSelected) {
							FormsFragment formsFragment = FormsFragment
									.newInstance(context);
							FragmentManager fragmentManager = getFragmentManager();
							FragmentTransaction fragmentTransaction = fragmentManager
									.beginTransaction();
							fragmentTransaction.replace(
									R.id.activity_main_content_fragment,
									formsFragment);
							fragmentTransaction.commit();
						} else {
							QuickActionFragment qaFragment = QuickActionFragment
									.newInstance(context);
							FragmentManager fragmentManager = getFragmentManager();
							FragmentTransaction fragmentTransaction = fragmentManager
									.beginTransaction();
							fragmentTransaction.replace(
									R.id.activity_main_content_fragment,
									qaFragment);
							fragmentTransaction.commit();
						}

					}
				});

				QAgridview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Log.i("apps", "===========> Click done");

						String QAId = customGridAdapters.getId(arg2);
						ContactLogicbean country = callDisp
								.getdbHeler(context).getQucikActionById(QAId);
						Log.i("QAA",
								"onItemClick position====>"
										+ country.getEditlable()
										+ country.getfromuser()
										+ country.getEditToUser()
										+ country.getFtpPath()
										+ country.getcontent()
										+ country.getAction()
										+ country.getEditconditon());
						callDisp.doAction(country.getEditlable(),
								country.getfromuser(), country.getEditToUser(),
								country.getFtpPath(), country.getcontent(),
								country.getAction(), country.getEditconditon());

					

					}

				});

				// searchBTN.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				//
				// searchText = searchBar.getText().toString();
				// if (searchText.length() > 0) {
				// doSearchBasedOnText(searchText);
				// } else {
				// refreshIcon();
				// }
				//
				// }
				//
				// });

				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v,
							int arg2, long arg3) {
						ImageView Imgview = (ImageView) v
								.findViewById(R.id.item_image);
						TextView appname = (TextView) v
								.findViewById(R.id.appnameTXT);
						TextView ownername = (TextView) v
								.findViewById(R.id.ownerTXT);

						String formname = appname.getText().toString();
						String id = Imgview.getContentDescription().toString();
						String owner = ownername.getText().toString();

						HashMap<String, String> dtype = callDisp.getdbHeler(
								context).getColumnTypes(formname + "_" + id);
						String tableId = callDisp.getdbHeler(context)
								.getFormName(id);
						if (tableId != null || tableId != "") {
							String[] accessSetting = callDisp.getdbHeler(
									context).getAccessRights(id,
									CallDispatcher.LoginUser);
							if (accessSetting != null) {
								if ((accessSetting[0].toString()
										.equalsIgnoreCase("A03"))
										|| (accessSetting[0].toString()
												.equalsIgnoreCase("A04"))
										|| (accessSetting[0].toString()
												.equalsIgnoreCase("A02"))
										|| (accessSetting[0].toString()
												.equalsIgnoreCase("A05"))) {
									if (CallDispatcher.LoginUser != null) {
										Intent viewer_intent = new Intent(
												context, FormViewer.class);
										viewer_intent
												.putExtra("name", formname);
										viewer_intent.putExtra("id", id);
										viewer_intent.putExtra("types", dtype);
										viewer_intent.putExtra("owner", owner);
										context.startActivity(viewer_intent);
									} else {
										ShowError("Error", "Kindly Login");
									}

								} else {
									Toast.makeText(
											context,
											"Sorry you don't have permission to view records",
											Toast.LENGTH_LONG).show();
								}
							}
						}
					}

				});
			} else {
				((ViewGroup) view.getParent()).removeView(view);
			}
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			return view;
		}
	}

	public void refreshIcon() {

		try {
			listValues.clear();
			quickaction.clear();
			listValues = callDisp.getdbHeler(context).LookUpRecords(homeIcon,
					CallDispatcher.LoginUser);
			quickaction = callDisp.getdbHeler(context).BLDatasOverall();
			if (listValues.size() > 0) {

				// customGridAdapter = new StickyGridHeadersSimpleArrayAdapter(
				// context, listValues, R.layout.header, R.layout.row_grid);
				// gridView.setAdapter(customGridAdapter);
				// customGridAdapter.notifyDataSetChanged();
			}

			if (quickaction.size() > 0) {

				customGridAdapters = new CustomQaAdapter(context,
						R.layout.row_grid, quickaction);
				QAgridview.setAdapter(customGridAdapters);

				customGridAdapters.notifyDataSetChanged();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void refreshGridView() {
		try {
			listValues.clear();
			quickaction.clear();
			listValues = callDisp.getdbHeler(context).LookUpRecords(homeIcon,
					CallDispatcher.LoginUser);
			quickaction = callDisp.getdbHeler(context).BLDatasOverall();
			if (listValues.size() != 0)
				// customGridAdapter.notifyDataSetChanged();
				if (quickaction.size() != 0)
					customGridAdapters.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		try {
			// TODO Auto-generated method stub

			super.onResume();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doSearchBasedOnText(String searchText) {
		try {
			// TODO Auto-generated method stub

			SearchlistValues.clear();
			Searchquickaction.clear();
			if (listValues.size() > 0) {
				for (int i = 0; i < listValues.size(); i++) {
					formItem item = new formItem();
					item = listValues.get(i);
					String title = item.getTitle();
					if (title.startsWith(searchText)
							|| title.equalsIgnoreCase(searchText)) {
						// listValues.remove(i);
						SearchlistValues.add(listValues.get(i));
					}

				}

				listValues.clear();
				// customGridAdapter = new StickyGridHeadersSimpleArrayAdapter(
				// context, SearchlistValues, R.layout.header,
				// R.layout.row_grid);
				// gridView.setAdapter(customGridAdapter);

				// customGridAdapter.notifyDataSetChanged();
			}

			if (quickaction.size() > 0) {
				for (int i = 0; i < quickaction.size(); i++) {
					formItem item = new formItem();
					item = quickaction.get(i);
					String title = item.getTitle();
					if (title.startsWith(searchText)
							|| title.equalsIgnoreCase(searchText)) {
						// quickaction.remove(i);
						Searchquickaction.add(quickaction.get(i));

					}

				}
				quickaction.clear();
				customGridAdapters = new CustomQaAdapter(context,
						R.layout.row_grid, Searchquickaction);
				QAgridview.setAdapter(customGridAdapters);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// protected void ShowList() {
	// // TODO Auto-generated method stub
	//
	// setContentView(R.layout.history_container);
	//
	// slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
	// ArrayList<Slidebean> datas = new ArrayList<Slidebean>();
	// callDisp.composeList(datas);
	// slidemenu.init(AppsViewFragment.this, datas, AppsViewFragment.this, 100);
	//
	// }
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
					if (WebServiceReferences.contextTable
							.containsKey("appsview")) {
						WebServiceReferences.contextTable.remove("appsview");
					}

					MemoryProcessor.getInstance().unbindDrawables(view);
					view = null;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

	public void ShowError(String Title, String Message) {
		try {
			AlertDialog confirmation = new AlertDialog.Builder(context)
					.create();
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

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	//
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
	//
	// }

	// public void notifyProfilepictureDownloaded() {
	// handler.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// if (slidemenu != null) {
	// if (slidemenu.isShown())
	// slidemenu.refreshItem();
	// }
	// }
	// });
	//
	// }

	// @Override
	// public void onSlideMenuItemClick(int itemId, View v, Context context) {
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
	//
	// break;
	// case WebServiceReferences.CLONE:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	//
	// break;
	// case WebServiceReferences.SETTINGS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	//
	// case WebServiceReferences.QUICK_ACTION:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.FORMS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	//
	// break;
	//
	// default:
	// break;
	// }
	//
	// }
	//
}
