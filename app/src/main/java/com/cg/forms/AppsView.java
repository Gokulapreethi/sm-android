package com.cg.forms;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.SlideMenu.SlideMenu;
import com.cg.SlideMenu.SlideMenuInterface;
import com.cg.SlideMenu.Slidebean;
import com.cg.snazmed.R;
import com.cg.adapters.CustomQaAdapter;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.quickaction.ContactLogicbean;
import com.cg.quickaction.ContactLogics;
import com.cg.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;
import com.main.AppMainActivity;
import com.main.FormsFragment;
import com.util.SingleInstance;

public class AppsView extends Activity implements
		SlideMenuInterface.OnSlideMenuItemClickListener {

	private Context context = null;

	private ArrayList<formItem> quickaction = new ArrayList<formItem>();
	private Handler handler = new Handler();
	static int totalLayouts;
	private Button IMRequest;
	private Button form_settings = null;
	boolean addoption = false;
	boolean isreject = false;
	private SlideMenu slidemenu;
	private CallDispatcher callDisp;
	static int buddyStatus = 0;
	private GridView gridView, QAgridview;
	private LinearLayout noapp, gridlay, noQA;
	private Button addapp, newapp, addQA, quickAction, apps = null;
	private ArrayList<formItem> listValues = new ArrayList<formItem>();
	private ArrayList<formItem> SearchlistValues = new ArrayList<formItem>();
	private ArrayList<formItem> Searchquickaction = new ArrayList<formItem>();
	private StickyGridHeadersSimpleArrayAdapter customGridAdapter = null;
	private CustomQaAdapter customGridAdapters = null;
	private Bitmap homeIcon = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

		ShowList();

		setContentView(R.layout.appsview);
		context = this;
		WebServiceReferences.contextTable.put("appsview", this);

		apps = (Button) findViewById(R.id.appsviewbtn);
		quickAction = (Button) findViewById(R.id.quickbtn);
		IMRequest = (Button) findViewById(R.id.appsim);

		IMRequest.setVisibility(View.GONE);
		IMRequest.setBackgroundResource(R.drawable.one);

		Button btn_Settings = (Button) findViewById(R.id.btn_Settings);

		btn_Settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				slidemenu.show();
			}
		});

		newapp = (Button) findViewById(R.id.addanapp);
		addapp = (Button) findViewById(R.id.appnew);
		addQA = (Button) findViewById(R.id.addanapp1);

		quickaction = callDisp.getdbHeler(context).BLDatasOverall();

		noapp = (LinearLayout) findViewById(R.id.level);
		gridlay = (LinearLayout) findViewById(R.id.layout_maincontainer);
		noQA = (LinearLayout) findViewById(R.id.levelQA);

		homeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.gridfmicon);
		listValues = callDisp.getdbHeler(context).LookUpRecords(homeIcon,
				CallDispatcher.LoginUser);

		gridView = (GridView) findViewById(R.id.gridView1);
		QAgridview = (GridView) findViewById(R.id.gridViewQA);
		customGridAdapters = new CustomQaAdapter(context, R.layout.row_grid,
				quickaction);
		QAgridview.setAdapter(customGridAdapters);
		customGridAdapter = new StickyGridHeadersSimpleArrayAdapter(context,
				listValues, R.layout.header, R.layout.row_grid);
		form_settings = (Button) findViewById(R.id.form_settings);

		if (listValues.size() > 0) {

			gridView.setAdapter(customGridAdapter);
		}
		if (listValues.size() <= 0) {
			noapp.setVisibility(View.VISIBLE);
			gridlay.setVisibility(View.GONE);
			form_settings.setVisibility(View.GONE);
		} else {
			noapp.setVisibility(View.GONE);
			gridlay.setVisibility(View.VISIBLE);
			form_settings.setVisibility(View.VISIBLE);

		}

		// searchBar.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before,
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
		// public void beforeTextChanged(CharSequence s, int start, int count,
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
				addoption = false;
				gridView.setVisibility(View.VISIBLE);
				if (listValues.size() > 0) {
					form_settings.setVisibility(View.VISIBLE);
				} else {
					form_settings.setVisibility(View.GONE);
				}
				QAgridview.setVisibility(View.GONE);
				noQA.setVisibility(View.GONE);
				apps.setBackgroundResource(R.drawable.rounded_bordercolor_app);
				apps.setTextColor(getResources().getColor(R.color.white));
				quickAction
						.setBackgroundResource(R.drawable.rounded_bordercolor_app2);
				quickAction.setTextColor(getResources().getColor(
						R.color.tabcolor));

			}
		});

		quickAction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// searchBar.setText("");
				quickAction
						.setBackgroundResource(R.drawable.rounded_bordercolor_app);
				quickAction
						.setTextColor(getResources().getColor(R.color.white));
				apps.setBackgroundResource(R.drawable.rounded_bordercolor_app2);
				apps.setTextColor(getResources().getColor(R.color.tabcolor));
				form_settings.setVisibility(View.GONE);
				addoption = true;
				if (quickaction.size() != 0) {
					gridView.setVisibility(View.GONE);
					QAgridview.setVisibility(View.VISIBLE);
				} else {
					gridView.setVisibility(View.GONE);

					noQA.setVisibility(View.VISIBLE);
					QAgridview.setVisibility(View.GONE);

				}
			}
		});
		addQA.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentfrms = new Intent(context, ContactLogics.class);
				startActivity(intentfrms);
				finish();
			}
		});

		form_settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, FormSettings.class);
				startActivity(intent);
				finish();
			}
		});
		newapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FormsFragment viewProfileFragment = FormsFragment
						.newInstance(context);
				AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
						.get("MAIN");
				FragmentManager fragmentManager = appMainActivity
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment,
						viewProfileFragment);
				fragmentTransaction.commit();
				finish();
			}
		});
		addapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (addoption) {
					Intent intentfrms = new Intent(context,
							ContactLogics.class);
					startActivity(intentfrms);
					finish();
				} else {
					FormsFragment viewProfileFragment = FormsFragment
							.newInstance(context);
					AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
							.get("MAIN");
					FragmentManager fragmentManager = appMainActivity
							.getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(
							R.id.activity_main_content_fragment,
							viewProfileFragment);
					fragmentTransaction.commit();
					finish();
				}
			}
		});

		QAgridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.i("apps", "===========> Click done");

				String QAId = customGridAdapters.getId(arg2);
				ContactLogicbean country = callDisp.getdbHeler(context)
						.getQucikActionById(QAId);
				Log.i("QAA",
						"onItemClick position====>" + country.getEditlable()
								+ country.getfromuser()
								+ country.getEditToUser()
								+ country.getFtpPath() + country.getcontent()
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
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				ImageView Imgview = (ImageView) v.findViewById(R.id.item_image);
				TextView appname = (TextView) v.findViewById(R.id.appnameTXT);
				TextView ownername = (TextView) v.findViewById(R.id.ownerTXT);

				String formname = appname.getText().toString();
				String id = Imgview.getContentDescription().toString();
				String owner = ownername.getText().toString();

				HashMap<String, String> dtype = callDisp.getdbHeler(context)
						.getColumnTypes(formname + "_" + id);
				String tableId = callDisp.getdbHeler(context).getFormName(id);
				if (tableId != null || tableId != "") {
					String[] accessSetting = callDisp.getdbHeler(context)
							.getAccessRights(id, CallDispatcher.LoginUser);
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
										AppsView.this, FormViewer.class);
								viewer_intent.putExtra("name", formname);
								viewer_intent.putExtra("id", id);
								viewer_intent.putExtra("types", dtype);
								viewer_intent.putExtra("owner", owner);
								startActivity(viewer_intent);
							} else {
								ShowError(
										SingleInstance.mainContext
												.getResources().getString(
														R.string.error_err),
										SingleInstance.mainContext
												.getResources().getString(
														R.string.kindly_login));
							}

						} else {
							Toast.makeText(
									context,
									SingleInstance.mainContext.getResources().getString(R.string.no_permission_to_view_the_records),
									Toast.LENGTH_LONG).show();
						}
					}
				}
			}

		});
	}

	public void refreshIcon() {

		listValues.clear();
		quickaction.clear();
		listValues = callDisp.getdbHeler(context).LookUpRecords(homeIcon,
				CallDispatcher.LoginUser);
		quickaction = callDisp.getdbHeler(context).BLDatasOverall();
		if (listValues.size() > 0) {

			customGridAdapter = new StickyGridHeadersSimpleArrayAdapter(
					context, listValues, R.layout.header, R.layout.row_grid);
			gridView.setAdapter(customGridAdapter);
			customGridAdapter.notifyDataSetChanged();
		}

		if (quickaction.size() > 0) {

			customGridAdapters = new CustomQaAdapter(context,
					R.layout.row_grid, quickaction);
			QAgridview.setAdapter(customGridAdapters);

			customGridAdapters.notifyDataSetChanged();
		}

	}

	public void refreshGridView() {
		listValues.clear();
		quickaction.clear();
		listValues = callDisp.getdbHeler(context).LookUpRecords(homeIcon,
				CallDispatcher.LoginUser);
		quickaction = callDisp.getdbHeler(context).BLDatasOverall();
		if (listValues.size() != 0)
			customGridAdapter.notifyDataSetChanged();
		if (quickaction.size() != 0)
			customGridAdapters.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;
	}

	private void doSearchBasedOnText(String searchText) {
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
			customGridAdapter = new StickyGridHeadersSimpleArrayAdapter(
					context, SearchlistValues, R.layout.header,
					R.layout.row_grid);
			gridView.setAdapter(customGridAdapter);

			customGridAdapter.notifyDataSetChanged();
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
	}

	protected void ShowList() {
		// TODO Auto-generated method stub

		setContentView(R.layout.history_container);

		slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
		ArrayList<Slidebean> datas = new ArrayList<Slidebean>();
		callDisp.composeList(datas);
		slidemenu.init(AppsView.this, datas, AppsView.this, 100);

	}

	@Override
	protected void onDestroy() {

		try {
			Log.e("lg", "on destroy of buddyview1????????????????");
			if (WebServiceReferences.contextTable.containsKey("appsview")) {
				WebServiceReferences.contextTable.remove("appsview");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();
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

		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				AlertDialog.Builder buider = new AlertDialog.Builder(context);
				buider.setMessage(
						SingleInstance.mainContext.getResources().getString(R.string.app_background))
						.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										// finish();
										moveTaskToBack(true);
										// return true;

									}
								})
						.setNegativeButton(SingleInstance.mainContext.getResources().getString(R.string.no),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.cancel();
									}
								});
				AlertDialog alert = buider.create();
				alert.show();
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	public void notifyProfilepictureDownloaded() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (slidemenu != null) {
					if (slidemenu.isShown())
						slidemenu.refreshItem();
				}
			}
		});

	}

	@Override
	public void onSlideMenuItemClick(int itemId, View v, Context context) {
		switch (itemId) {
		case WebServiceReferences.CONTACTS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.USERPROFILE:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.UTILITY:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.NOTES:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.APPS:

			break;
		case WebServiceReferences.CLONE:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();

			break;
		case WebServiceReferences.SETTINGS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;

		case WebServiceReferences.QUICK_ACTION:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.FORMS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();

			break;

		default:
			break;
		}

	}

}
