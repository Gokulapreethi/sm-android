package com.cg.account;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.lib.model.FieldTemplateBean;
import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.UpperCaseParse;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class ShareByProfile extends Activity implements OnClickListener

{

	String colName = null;
	String dropdown = null;
	private LinearLayout records_container = null;
	Context context = null;
	CallDispatcher callDisp;
	Button back = null;
	Button search = null;
	HashMap<Integer, String> serch_fields = new HashMap<Integer, String>();
	Set<String> UserID = new HashSet<String>();
	String fromActivity = "";
	ArrayList<String[]> fieldsForSearch = new ArrayList<String[]>();
	private Handler wservice_handler = null;
	TextView title;
	private Vector<FieldTemplateBean> profileFields = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			// requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.sharebyprofile);
			context = this;
			WebServiceReferences.contextTable.put("sharebyprofile", context);
			CallDispatcher.pdialog = new ProgressDialog(context);
			fromActivity = getIntent().getStringExtra("activity");
			if (fromActivity == null) {
				fromActivity = "";
			}
			initView();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

	@Override
	public void onDestroy() {
		try {
			super.onDestroy();

			if (WebServiceReferences.contextTable.containsKey("sharebyprofile")) {
				WebServiceReferences.contextTable.remove("sharebyprofile");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initView() {

		try {

			search = (Button) findViewById(R.id.btn_viewaddanother);

			search.setOnClickListener(this);
			records_container = (LinearLayout) findViewById(R.id.llayout_holder);
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

			profileFields = new Vector<FieldTemplateBean>();
			profileFields = callDisp.getdbHeler(context)
					.getSearchByProfileFields();

			wservice_handler = new Handler();

			doInflating(profileFields);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private ArrayList<String[]> ValidateFormSharebuddy() {
		try {
			fieldsForSearch.clear();
			for (int i = 0; i < records_container.getChildCount(); i++) {
				String[] values = new String[3];

				LinearLayout layout = (LinearLayout) records_container
						.getChildAt(i);
				layout.findViewById(R.id.tview_clms);

				final EditText fields = (EditText) layout
						.findViewById(R.id.edtxt_frmfield);
				String valueString = fields.getText().toString().trim();
				String formname_id = fields.getContentDescription().toString();
				if (valueString.length() > 0) {
					String[] name_id = formname_id.split("_");
					values[0] = name_id[1];
					values[1] = name_id[0];
					values[2] = valueString;
					fieldsForSearch.add(values);

				}

			}

			return fieldsForSearch;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String getNoteCreateTime() {
		try {
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
			return sdf.format(curDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		try {
			// TODO Auto-generated method stub

			if (v.getId() == search.getId()) {
				UserID.clear();
				if (fromActivity.equalsIgnoreCase("note")) {
					serch_fields = ValidateForm();
					if (serch_fields.size() > 0) {

						for (Integer entry : serch_fields.keySet()) {
							int fieldId = entry;
							String fieldName = serch_fields.get(fieldId);
							ArrayList<String> userid = callDisp.getdbHeler(
									context).getProfileUserName(fieldId,
									fieldName);
							if (userid.size() > 0) {
								for (int i = 0; i < userid.size(); i++) {

									if (!userid.get(i).equalsIgnoreCase(
											CallDispatcher.LoginUser)) {
										UserID.add(userid.get(i));

									}

								}
							}

						}
						if (SingleInstance.mainContext
								.isNetworkConnectionAvailable()) {

							if (UserID.size() > 0) {
								String[] returnArrayResult = UserID
										.toArray(new String[UserID.size()]);
								Intent intent = new Intent(context,
										ShareByProfileByddy.class);

								Bundle b = new Bundle();
								b.putStringArray("username", returnArrayResult);
								intent.putExtras(b);
								startActivityForResult(intent, 8);

							} else {
								showAlert1("Info",ShareByProfile.this.getResources().getString(R.string.no_result_found));
							}
						} else {
							showAlert1("Info",
									"Check internet connection Unable to connect server");
						}

					} else {
						Toast.makeText(context, "Please Enter Values",
								Toast.LENGTH_LONG).show();

					}
				} else {

					fieldsForSearch = ValidateFormSharebuddy();
					if (fieldsForSearch.size() > 0) {
						if (isWifi()) {
							if (!WebServiceReferences.running) {
								callDisp.startWebService(getResources()
										.getString(R.string.service_url), "80");
							}
							ProgressDialog progressDialog = new ProgressDialog(
									context);
							progressDialog.setCancelable(true);
							callDisp.showprogress(progressDialog, context);

							WebServiceReferences.webServiceClient
									.SharebyProfile(CallDispatcher.LoginUser,
											fieldsForSearch);
						} else {
							showAlert1("Info",
									"Check Internet Connection,Unable to Connect Server");
						}
					} else {
						showToast(SingleInstance.mainContext.getResources().getString(R.string.please_enter_values_for_search));
						

					}

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showToast(String string) {
		// TODO Auto-generated method stub
		
	}

	private void doInflating(Vector<FieldTemplateBean> profileFields) {
		try {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			for (int i = 0; i < profileFields.size(); i++) {

				FieldTemplateBean fields = profileFields.get(i);
				colName = fields.getFieldName();
				dropdown = fields.getFieldType();

				LinearLayout layout = (LinearLayout) layoutInflater.inflate(
						R.layout.formrecord_fields, null);
				layout.setBackgroundColor(getResources()
						.getColor(R.color.white));

				ImageView im = (ImageView) layout
						.findViewById(R.id.iview_dtype);
				im.setVisibility(View.GONE);
				TextView tv1 = (TextView) layout.findViewById(R.id.tview_clms);
				tv1.setTextColor(getResources().getColor(R.color.black));

				tv1.setText(UpperCaseParse
						.captionTextForUpperCaseString(colName));

				final EditText ed_fld = (EditText) layout
						.findViewById(R.id.edtxt_frmfield);
				ed_fld.setFocusableInTouchMode(true);
				ed_fld.setTag(dropdown);
				ed_fld.setContentDescription(colName + "_"
						+ String.valueOf(fields.getFieldId()));
				ed_fld.setHint(SingleInstance.mainContext.getResources()
						.getString(R.string.enter_value_to_search));
				if (colName.equalsIgnoreCase("Birth Day")) {
					im.setVisibility(View.VISIBLE);
					ed_fld.setHint(SingleInstance.mainContext.getResources()
							.getString(R.string.click_arrow_to_set_dob));
					im.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								changeType(ed_fld);
								return true;
							} else {
								return false;

							}
						}
					});

				} else if (colName.equalsIgnoreCase("Profession")) {
					im.setVisibility(View.VISIBLE);
					ed_fld.setHint(SingleInstance.mainContext.getResources()
							.getString(R.string.click_arrow_to_set_profession));
					im.setOnTouchListener(new OnTouchListener() {
						@Override									
						public boolean onTouch(View v, MotionEvent event) {							
							// TODO Auto-generated method stub			
							Log.i("Test", "SEARCH PROFESSION CLICK");
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								final String[] attach= Arrays.copyOf(SingleInstance.mainContext.profession, SingleInstance.mainContext.profession.length);
							AlertDialog.Builder builder = new AlertDialog.Builder(context);
					        builder.setTitle("Select Profession");
					        builder.setItems(attach, new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int item) {
					            	if(item==0)						 						
					            		ed_fld.setText(attach[0]);
					            	if(item==1)						 						
					            		ed_fld.setText(attach[1]);
					            	if(item==2)						 						
					            		ed_fld.setText(attach[2]);
					            	if(item==3)						 						
					            		ed_fld.setText(attach[3]);
					            	if(item==4)						 						
					            		ed_fld.setText(attach[4]);
					            	if(item==5)						 						
					            		ed_fld.setText(attach[5]);
					            	if(item==6)						 						
					            		ed_fld.setText(attach[6]);
					            	if(item==7)						 						
					            		ed_fld.setText(attach[7]);
					            	if(item==8)						 						
					            		ed_fld.setText(attach[8]);
					            	if(item==9)						 						
					            		ed_fld.setText(attach[9]);
					            	if(item==10)						 						
					            		ed_fld.setText(attach[10]);
					            	 
					            }
					        });
					        AlertDialog alert = builder.create();
					        alert.show();
							return true;
							} else
								return false;
						
					           
						}
					});
				}
				
				else if (dropdown.equalsIgnoreCase("free text")) {
					ed_fld.setInputType(InputType.TYPE_CLASS_TEXT);

				} else if (dropdown.equalsIgnoreCase("number")) {
					ed_fld.setInputType(InputType.TYPE_CLASS_NUMBER);

				} else if (dropdown.equalsIgnoreCase("email")) {
					ed_fld.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

				}

				layout.setId(records_container.getChildCount());
				Log.i("thread", "Layout id" + layout.getId());
				records_container.addView(layout);

			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private HashMap<Integer, String> ValidateForm() {
		try {

			HashMap<Integer, String> FiledValues = new HashMap<Integer, String>();

			for (int i = 0; i < records_container.getChildCount(); i++) {
				FieldTemplateBean FIELDS = profileFields.get(i);
				LinearLayout layout = (LinearLayout) records_container
						.getChildAt(i);
				layout.findViewById(R.id.tview_clms);

				final EditText fields = (EditText) layout
						.findViewById(R.id.edtxt_frmfield);
				String valueString = fields.getText().toString().trim();

				if (valueString.length() > 0) {

					FiledValues.put(Integer.parseInt(FIELDS.getFieldId()),
							valueString);

				}

			}

			return FiledValues;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			if ((requestCode == 8) && (resultCode == -1)) {

				Bundle bun = data.getBundleExtra("share");
				if (bun != null) {
					String[] returnArrayResult = bun.getStringArray("userid");
					Intent i = new Intent();
					Bundle bun1 = new Bundle();
					bun1.putStringArray("userid", returnArrayResult);
					i.putExtra("share", bun1);
					setResult(-1, i);
					finish();

				}

			}
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
					// TODO Auto-generated method stub
					if (obj instanceof ArrayList) {
						ArrayList<String> response = (ArrayList<String>) obj;
						String[] stockArr = new String[response.size()];
						stockArr = response.toArray(stockArr);
						callDisp.cancelDialog();
						Intent intentadd = new Intent(context, FindPeople.class);
						intentadd.putExtra("fromprofile", "yes");
						intentadd.putExtra("username", stockArr);
						startActivity(intentadd);
						finish();

					} else if (obj instanceof WebServiceBean) {
						String Response;
						callDisp.cancelDialog();
						WebServiceBean service_bean = (WebServiceBean) obj;
						Response = service_bean.getText();
						if (Response.equalsIgnoreCase("No matches found")) {
							showAlert1("Info", ShareByProfile.this.getResources().getString(R.string.no_result_found));
						} else {
							showToast(service_bean.getText());

						}

					}
				}

				private void showToast(String text) {
					// TODO Auto-generated method stub
					Toast.makeText(context, text, Toast.LENGTH_LONG).show();
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void changeType(final EditText field) {

		try {
			Button btnSet;
			Button btnCancel;
			final DatePicker dp;
			final TimePicker tp;
			final AlertDialog alertReminder = new AlertDialog.Builder(context)
					.create();

			ScrollView tblDTPicker = (ScrollView) View.inflate(context,
					R.layout.date_time_picker, null);

			btnSet = (Button) tblDTPicker.findViewById(R.id.btnSetDateTime);
			btnCancel = (Button) tblDTPicker
					.findViewById(R.id.btnCancelDateTime);
			dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
			tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);
			tp.setVisibility(View.GONE);
			btnCancel.setVisibility(View.VISIBLE);
			btnCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					alertReminder.dismiss();
				}
			});

			btnSet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String DOB = dp.getYear()
							+ "-"
							+ WebServiceReferences.setLength2((dp.getMonth() + 1))
							+ "-"
							+ WebServiceReferences.setLength2(dp
									.getDayOfMonth());
					field.setText(DOB);
					alertReminder.dismiss();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String currentDateandTime = sdf.format(new Date());
					if (DOB.equals(currentDateandTime)) {
						Toast.makeText(getApplicationContext(),
								"Please Enter Valid Details", Toast.LENGTH_LONG)
								.show();
					}

				}
			});
			alertReminder.setTitle(SingleInstance.mainContext
					.getString(R.string.date));
			alertReminder.setView(tblDTPicker);
			alertReminder.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("profile", "====> " + e.getMessage());
			e.printStackTrace();
		}

	}

	protected void showAlert1(String title, String msg) {
		AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
		alertCall.setMessage(msg).setTitle(title).setCancelable(false)
				.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {
							// alertCall.
							dialog.dismiss();

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		alertCall.show();

	}

	private boolean isWifi() {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);

		// For 3G check
		boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		// For WiFi Check
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();

		System.out.println(is3g + " net " + isWifi);

		if (!is3g && !isWifi) {
			return false;
			// Toast.makeText(getApplicationContext(),"Please make sure your Network Connection is ON ",Toast.LENGTH_LONG).show();
		} else {
			return true;
			// " Your method what you want to do "
		}
	}

}
