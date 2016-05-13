package com.cg.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.lib.model.FormAttributeBean;
import org.lib.model.WebServiceBean;
import org.lib.webservice.Servicebean;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bean.EditForm;
import com.bean.EditFormBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.util.SingleInstance;

public class AddNewForm extends FragmentActivity {
	private Button addNewForm, IMRequest;
	public LinearLayout ll;
	public RelativeLayout container;
	public CirclePageIndicator mIndicator;
	private ViewPager awesomePager;
	private PagerAdapter pm;
	public Button cancelbtn, nextbtn, canbtn;
	private ListView inputFields;
	public FieldListCustomAdapter customAdapter;
	public AddNewForm fields = null;
	public Context context;
	public ArrayList<InputsFields> fieldList = new ArrayList<InputsFields>();
	private CallDispatcher callDisp = null;
	public Animation fadeIn, fadeOut = null;
	public ImageView tap = null;
	String strScreenType = null;
	private ProgressDialog dialog;
	private Handler handler = new Handler();
	private boolean isChat = false;
	private String modes[] = {
			SingleInstance.mainContext.getResources().getString(
					R.string.free_text),
			SingleInstance.mainContext.getResources().getString(R.string.date),
			SingleInstance.mainContext.getResources().getString(R.string.time),
			SingleInstance.mainContext.getResources().getString(
					R.string.current_date),
			SingleInstance.mainContext.getResources().getString(
					R.string.current_time),
			SingleInstance.mainContext.getResources().getString(
					R.string.date_and_time),
			SingleInstance.mainContext.getResources().getString(
					R.string.multimedia),
			SingleInstance.mainContext.getResources().getString(
					R.string.drop_down),
			SingleInstance.mainContext.getResources().getString(
					R.string.radio_button),
			SingleInstance.mainContext.getResources().getString(
					R.string.numeric),
			SingleInstance.mainContext.getResources().getString(
					R.string.current_loc),
			SingleInstance.mainContext.getResources().getString(
					R.string.compute) };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_new_form);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			strScreenType = "XLarge";
		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			strScreenType = "XLarge";
		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			strScreenType = "other";
		} else {
			strScreenType = "other";
		}

		IMRequest = (Button) findViewById(R.id.im);

		IMRequest.setVisibility(View.INVISIBLE);
		IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);
		// IMRequest.setWidth(70);

		WebServiceReferences.contextTable.put("locbusy", this);

		callDisp.startWebService(
				getResources().getString(R.string.service_url), "80");
		ll = (LinearLayout) findViewById(R.id.slider);
		container = (RelativeLayout) findViewById(R.id.container);
		ll.setVisibility(View.GONE);
		addNewForm = (Button) findViewById(R.id.add_form_icon);
		awesomePager = (ViewPager) findViewById(R.id.pager);
		WebServiceReferences.contextTable.put("frmcreator", this);
		mIndicator = (CirclePageIndicator) findViewById(R.id.pagerIndicator);
		cancelbtn = (Button) findViewById(R.id.addinputcancelbtn);
		nextbtn = (Button) findViewById(R.id.btn_next);
		canbtn = (Button) findViewById(R.id.btn_cancel);
		fields = this;
		context = this;
		final boolean isValid = getIntent().getExtras().getBoolean("isvalid",
				false);
		isChat = getIntent().getExtras().getBoolean("isChat", false);
		if (!isValid) {
			CallDispatcher.inputFieldList.clear();
		} else {
			nextbtn.setBackgroundResource(R.drawable.ic_action_save);
		}

		Resources res = getResources();
		inputFields = (ListView) findViewById(R.id.list);
		customAdapter = new FieldListCustomAdapter(context,
				CallDispatcher.inputFieldList, res);
		inputFields.setAdapter(customAdapter);
		if (CallDispatcher.inputFieldList != null) {
			customAdapter.notifyDataSetChanged();
		}
		inputFields.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				String tableName = getIntent().getStringExtra("tablename");
				InputsFields iField = (InputsFields) customAdapter
						.getItem(position);
				ArrayList<EditFormBean> eList = DBAccess.getdbHeler()
						.getFormInfoDetails(tableName, iField.getFieldName());
				boolean isNewExists = false;
				for (InputsFields iFields : CallDispatcher.inputFieldList) {
					if (iFields.getAttributeId() == null) {
						Log.i("form123",
								"field name : " + iFields.getFieldName());
						isNewExists = true;
					}
				}
				if (eList != null && eList.size() > 0 && !isNewExists) {
					Intent intent = new Intent(context, EditFormFields.class);
					intent.putExtra("eList", eList);
					intent.putExtra("tablename", tableName);
					startActivity(intent);
				}
			}
		});
		fadeOut = new AlphaAnimation(0f, 0.8f);
		fadeIn = new AlphaAnimation(0.8f, 1f);
		fadeOut.setFillAfter(true);
		fadeIn.setFillAfter(true);
		ArrayList<String> names = new ArrayList<String>();
		InputModes input = new InputModes();
		for (int i = 0; i < modes.length; i++) {
			names.add(i, modes[i]);
			input.inputMode = names.get(i);
		}

		Iterator<String> it = names.iterator();

		List<GridFragment> gridFragments = new ArrayList<GridFragment>();
		it = names.iterator();
		int i = 0;
		while (it.hasNext()) {
			ArrayList<GridItems> itmLst = new ArrayList<GridItems>();

			GridItems itm = new GridItems(0, it.next());
			itmLst.add(itm);
			i = i + 1;

			if (it.hasNext()) {
				GridItems itm1 = new GridItems(1, it.next());
				itmLst.add(itm1);
				i = i + 1;
			}

			if (it.hasNext()) {
				GridItems itm2 = new GridItems(2, it.next());
				itmLst.add(itm2);
				i = i + 1;
			}

			if (it.hasNext()) {
				GridItems itm3 = new GridItems(3, it.next());
				itmLst.add(itm3);
				i = i + 1;
			}

			if (it.hasNext()) {
				GridItems itm4 = new GridItems(4, it.next());
				itmLst.add(itm4);
				i = i + 1;
			}

			if (it.hasNext()) {
				GridItems itm5 = new GridItems(5, it.next());
				itmLst.add(itm5);
				i = i + 1;
			}

			if (it.hasNext()) {
				GridItems itm6 = new GridItems(6, it.next());
				itmLst.add(itm6);
				i = i + 1;
			}

			if (it.hasNext()) {
				GridItems itm7 = new GridItems(7, it.next());
				itmLst.add(itm7);
				i = i + 1;
			}
			if (!strScreenType.equalsIgnoreCase("XLarge")) {
				if (it.hasNext()) {
					GridItems itm8 = new GridItems(8, it.next());
					itmLst.add(itm8);
					i = i + 1;
				}
			}
			GridItems[] gp = {};
			GridItems[] gridPage = itmLst.toArray(gp);
			GridFragment gFramenFragment = new GridFragment(gridPage,
					AddNewForm.this, isValid);
			gridFragments.add(gFramenFragment);
		}
		pm = new PagerAdapter(getSupportFragmentManager(), gridFragments);
		awesomePager.setAdapter(pm);
		mIndicator.setViewPager(awesomePager);

		cancelbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ll.setVisibility(View.GONE);
				container.startAnimation(fadeIn);
				canbtn.setClickable(true);
				if (CallDispatcher.inputFieldList.size() > 0) {
					nextbtn.setEnabled(true);
				}
				nextbtn.setClickable(true);
			}
		});
		tap = (ImageView) findViewById(R.id.tap);
		if (CallDispatcher.inputFieldList.size() > 0) {
			tap.setVisibility(View.GONE);
		} else {
			tap.setVisibility(View.VISIBLE);
		}
		nextbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (CallDispatcher.inputFieldList.size() > 0) {
					if (!isValid) {
						Intent intent = new Intent(context,
								FormDescription.class);
						intent.putExtra("isChat", isChat);
						startActivity(intent);
					} else {
						boolean isNewExist = false;
						for (InputsFields iFields : CallDispatcher.inputFieldList) {
							if (iFields.getAttributeId() == null) {
								isNewExist = true;
							}
						}
						String tableName = getIntent().getStringExtra(
								"tablename");
						String[] temp = tableName.split("_");
						String[] params = new String[3];
						params[0] = CallDispatcher.LoginUser;
						params[1] = temp[1];
						params[2] = "new";
						ArrayList<String> fieldName = new ArrayList<String>();
						ArrayList<String> fieldType = new ArrayList<String>();
						ArrayList<String[]> attributes = new ArrayList<String[]>();
						ArrayList<String> fieldType2 = new ArrayList<String>();
						for (InputsFields iFields : CallDispatcher.inputFieldList) {
							if (iFields.getAttributeId() == null
									|| iFields.getAttributeId().length() == 0) {
								if (iFields.getFieldType().equalsIgnoreCase(
										"Numeric")) {
									fieldName.add(iFields.getFieldName());
									fieldType.add("INTEGER");
									fieldType2.add("int(10)");
								} else if (iFields.getFieldType()
										.equalsIgnoreCase("Multimedia")) {
									fieldType.add("nvarchar(20)");
									fieldType2.add("VARCHAR(45)");
									fieldName.add("blob_"
											+ iFields.getFieldName());
								} else if (iFields.getFieldType()
										.equalsIgnoreCase("Compute")) {
									if (iFields.getFieldName().startsWith("NM")) {
										fieldName.add(iFields.getFieldName());
										fieldType.add("INTEGER");
										fieldType2.add("int(10)");
									} else {
										fieldName.add(iFields.getFieldName());
										fieldType.add("nvarchar(20)");
										fieldType2.add("VARCHAR(45)");
									}
								} else {
									fieldName.add(iFields.getFieldName());
									fieldType.add("nvarchar(20)");
									fieldType2.add("VARCHAR(45)");
								}
								String[] values = { iFields.getFieldName(),
										iFields.getFieldType(),
										iFields.getValidData(),
										iFields.getDefaultValue(),
										iFields.getInstructions(),
										iFields.getErrorMsg() };
								attributes.add(values);
							}
						}

						String[] field_name = fieldName
								.toArray(new String[fieldName.size()]);
						String[] field_type = fieldType2
								.toArray(new String[fieldType2.size()]);
						if (isNewExist) {
							showProgress();
							WebServiceReferences.webServiceClient
									.editNewFormFields(context, params,
											field_name, field_type, attributes);
						} else {
							Toast.makeText(context,
									"Sorry there is no new field to add",
									Toast.LENGTH_LONG).show();
						}
					}
				} else {
					Toast.makeText(
							context,
							SingleInstance.mainContext.getResources()
									.getString(R.string.add_fields),
							Toast.LENGTH_LONG).show();
				}

			}
		});
		canbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isValid) {
					if (CallDispatcher.inputFieldList.size() > 0) {

						showDeleteAlert();

					} else {
						finish();
					}
				} else {
					finish();
				}
			}
		});

		addNewForm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (ll.getVisibility() == View.VISIBLE) {
					ll.setVisibility(View.GONE);
					container.startAnimation(fadeIn);
					canbtn.setClickable(true);
					nextbtn.setClickable(true);

				} else {
					ll.setVisibility(View.VISIBLE);
					container.startAnimation(fadeOut);
					canbtn.setClickable(false);
					nextbtn.setClickable(false);
				}
			}
		});
	}

	public void deleteField(int mPosition) {
		final int position = mPosition;
		AlertDialog.Builder buider = new AlertDialog.Builder(context);
		buider.setMessage(
				SingleInstance.mainContext.getResources().getString(
						R.string.delete_field))
				.setPositiveButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								CallDispatcher.inputFieldList.remove(position);
								if (CallDispatcher.inputFieldList.size() == 0) {
									tap.setVisibility(View.VISIBLE);
								}
								customAdapter.notifyDataSetChanged();
							}
						})
				.setNegativeButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.no),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
		AlertDialog alert = buider.create();
		alert.show();
	}

	private class PagerAdapter extends FragmentStatePagerAdapter {
		private List<GridFragment> fragments;

		public PagerAdapter(FragmentManager fm, List<GridFragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		WebServiceReferences.contextTable.remove("frmcreator");

	}

	public void showDeleteAlert() {
		try {
			AlertDialog.Builder buider = new AlertDialog.Builder(context);
			buider.setMessage(
					SingleInstance.mainContext.getResources().getString(
							R.string.create_form))
					.setPositiveButton(
							SingleInstance.mainContext.getResources()
									.getString(R.string.yes),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(context,
											FormDescription.class);
									intent.putExtra("isChat", isChat);
									startActivity(intent);

								}
							})
					.setNegativeButton(
							SingleInstance.mainContext.getResources()
									.getString(R.string.no),
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

	private void showProgress() {

		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setMessage("Progress ...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.show();

	}

	public void cancelDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	public void notifyWebserviceResponse(Servicebean sBean) {
		try {
			Object object = sBean.getObj();
			if (object instanceof String) {
				showToast((String) object);
			} else if (object instanceof WebServiceBean) {
				WebServiceBean wsBean = (WebServiceBean) object;
				showToast(wsBean.getText());
			} else if (object instanceof EditForm) {
				EditForm eForm = (EditForm) object;
				ArrayList<EditFormBean> eList = new ArrayList<EditFormBean>();
				HashMap<String, EditFormBean> eMap = eForm.geteList();
				for (InputsFields inputFieldList : CallDispatcher.inputFieldList) {
					if (eMap.containsKey(inputFieldList.getFieldName())) {
						EditFormBean editFormBean = new EditFormBean();
						editFormBean
								.setTablename(inputFieldList.getTableName());
						editFormBean.setColumnname("["
								+ inputFieldList.getFieldName() + "]");
						editFormBean
								.setEntrymode(inputFieldList.getFieldType());
						editFormBean.setValidata(inputFieldList.getValidData());
						editFormBean.setDefaultvalue(inputFieldList
								.getDefaultValue());
						editFormBean.setInstruction(inputFieldList
								.getInstructions());
						editFormBean.setErrortip(inputFieldList.getErrorMsg());
						editFormBean
								.setAttributeid(eMap.get(
										inputFieldList.getFieldName())
										.getAttributeid());
						if (inputFieldList.getFieldType().equalsIgnoreCase(
								"Free Text")) {
							editFormBean.setDatatype("nvarchar(20)");
						} else if (inputFieldList.getFieldType()
								.equalsIgnoreCase("Numeric")) {
							editFormBean.setDatatype("INTEGER");
						} else if (inputFieldList.getFieldType()
								.equalsIgnoreCase("Multimedia")) {
							editFormBean.setDatatype("nvarchar(20)");
						} else if (inputFieldList.getFieldType()
								.equalsIgnoreCase("Compute")) {
							if (inputFieldList.getValidData().contains("NM")) {
								editFormBean.setDatatype("INTEGER");
							} else {
								editFormBean.setDatatype("nvarchar(20)");
							}
						} else {
							editFormBean.setDatatype("nvarchar(20)");
						}
						eList.add(editFormBean);
					}
				}
				String tableName = getIntent().getStringExtra("tablename");
				if (DBAccess.getdbHeler().addNewColumnInExistingTable(
						"[" + tableName + "]", eList)) {
					for (InputsFields iFields : CallDispatcher.inputFieldList) {
						FormAttributeBean faBean = new FormAttributeBean();
						if (eMap.containsKey(iFields.getFieldName())) {
							faBean.setAttributeid(eMap.get(
									iFields.getFieldName()).getAttributeid());
						} else {
							faBean.setAttributeid(iFields.getAttributeId());
						}
						faBean.setDataEntry(iFields.getFieldType());
						faBean.setDataValidation(iFields.getValidData());
						faBean.setDefaultvalue(iFields.getDefaultValue());
						faBean.setInstruction(iFields.getInstructions());
						faBean.setErrortip(iFields.getErrorMsg());
						if (DBAccess.getdbHeler().saveOrUpdateFormInfo(
								"[" + tableName + "]",
								"[" + iFields.getFieldName() + "]", faBean) > 0) {

						}

					}
					showToast("Successfully fields added");
					finish();
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			cancelDialog();
		}
	}

	private void showToast(final String message) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}
		});
	}
}
