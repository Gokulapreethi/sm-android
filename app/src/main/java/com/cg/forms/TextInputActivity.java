package com.cg.forms;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.timer.ReminderService;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class TextInputActivity extends Activity implements OnClickListener {

	private LinearLayout layout_container = null;
	private LinearLayout computelayy = null;
	private LayoutInflater layoutInflate = null;
	private RelativeLayout layou = null;
	private static final int DATE_DIALOG_ID = 0;
	private int pHour;
	private int pMinute;
	private ArrayList<String[]> rec_list = null;
	private EditText userInput = null;
	private String timeD, dateD = null;
	private static final int TIME_DIALOG_ID = 1;
	private int mYear;
	private int mMonth;
	private int mDay;
	private ImageView iview_add = null;
	private ImageView iview_remove = null;
	private Context context = null;
	private ScrollView scroll;
	private ScrollView scrollview;
	private ArrayList<String[]> details = null;
	private static ArrayList<String> Entrymode = null;
	private EditText edtxt = null;
	private CallDispatcher callDisp = null;
	private AlertDialog alert = null;
	private static String ValidData = "";
	private ViewFlipper viewfli;
	private Button btn_submit, input_menu;
	private EditText edtxt_validda;
	private EditText defaultvalue, errortip;
	private TextView inst = null;
	private EditText ed_fld;
	private String entry = "";
	private static String[] fieldname = null;
	private ArrayList<String> computeFields = null;
	private ArrayList<String> computeFieldsExpresion = null;

	private static String dateandtime;
	private static String starttime;
	private static String endtime;

	private static String greatertime;
	private static String lesstime;

	private static String pickgdate;
	private static String pickldate;
	private static String pickbdate;
	private static String numeric;
	private static String numeric2;
	private static String startnumber;
	private static String endnumber;

	private static int startnum;
	private static int endnum;
	private static int between;
	private static int number;
	private Button add_field, compute, btn_addcompute, btn_dropdown;

	private DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();
	private Calendar myCalendar = Calendar.getInstance();

	private EditText et_startdatetime, et_enddatetime, ed_number, ed_numberone,
			ed_numbertwo;
	private Button btn_submitdate, btn_canceldate, btn_numsubmit,
			btn_numcancel, btn_btnumsum, btn_btnumcan, IMRequest, btn_cancel1,
			btn_cmp;

	private EditText compfield = null;
	private String recordname;
	private String formula = null;

	private String computefields = "";
	private String selectedfields = "";

	private ArrayList<String> comList = null;
	private InputsFields inputFields = new InputsFields();
	private LinearLayout locLayout = null;
	private LinearLayout defaultValueLayout;
	private String blockCharacterSet1 = "!#$%^&*()-+={}[]<>?/|";
	private String blockCharacterSet = "!@#$%^&*()-_+={}[]<>?/|";

	private boolean isEdit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);
			context = this;
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.custom_title1);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.custom_title1);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;
			isEdit = getIntent().getBooleanExtra("isEdit", false);
			InputsFields iFields = (InputsFields) getIntent()
					.getSerializableExtra("ifields");
			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(context);

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);
			displaymetrics = null;

			IMRequest = (Button) findViewById(R.id.im);

			TextView title = (TextView) findViewById(R.id.note_date);

			btn_cancel1 = (Button) findViewById(R.id.settings);

			btn_cancel1.setBackgroundResource(R.drawable.ic_action_back);

			btn_cmp = (Button) findViewById(R.id.btncomp);
			btn_cmp.setBackgroundResource(R.drawable.ic_action_save);

			btn_cmp.setOnClickListener(this);
			btn_cancel1.setOnClickListener(this);

			IMRequest.setVisibility(View.INVISIBLE);
			IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);

			WebServiceReferences.contextTable.put("locbusy", this);

			callDisp.startWebService(
					getResources().getString(R.string.service_url), "80");

			setContentView(R.layout.text_input);
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			WebServiceReferences.contextTable.put("textinputactivity", context);
			edtxt_validda = (EditText) findViewById(R.id.validation);

			edtxt_validda.setOnClickListener(this);

			defaultvalue = (EditText) findViewById(R.id.value);
			defaultvalue.setOnClickListener(this);
			Entrymode = new ArrayList<String>();
			input_menu = (Button) findViewById(R.id.input_menu);
			edtxt_validda.setFocusableInTouchMode(false);
			defaultvalue.setFocusableInTouchMode(false);
			context = this;
			defaultvalue = (EditText) findViewById(R.id.value);
//			defaultvalueTextviw = (TextView) findViewById(R.id.defaultvaluetextview);
			inst = (TextView) findViewById(R.id.instText);
			errortip = (EditText) findViewById(R.id.error);
			errortip.setFilters(getFilter());
			entry = getIntent().getExtras().getString("entry");
			title.setText("Create " + entry);

			locLayout = (LinearLayout) findViewById(R.id.loc);
			defaultValueLayout=(LinearLayout)findViewById(R.id.defaultvaluelayout);
			if (entry.equalsIgnoreCase("current Location")
					|| entry.equalsIgnoreCase("Multimedia")
					|| entry.equalsIgnoreCase("Current Date")
					|| entry.equalsIgnoreCase("Current Time")
					|| entry.equalsIgnoreCase("Date")
					|| entry.equalsIgnoreCase("Time")) {
				locLayout.setVisibility(View.GONE);
			} else {
				locLayout.setVisibility(View.VISIBLE);
				if (entry.equalsIgnoreCase("Compute")) {
					defaultValueLayout.setVisibility(View.GONE);
				} else {
					defaultValueLayout.setVisibility(View.VISIBLE);
				}
			}
			if (iFields != null) {
				inputFields = iFields;
				if (inputFields.getFieldType() != null) {
					title.setText("Edit " + inputFields.getFieldType());
				}
				if (inputFields.getValidData() != null) {
					edtxt_validda.setText(inputFields.getValidData());
				}
				if (inputFields.getDefaultValue() != null) {
					defaultvalue.setText(inputFields.getDefaultValue());
				}
				if (inputFields.getErrorMsg() != null) {
					errortip.setText(inputFields.getErrorMsg());
				}
			}
			InputFilter[] filters = new InputFilter[1];

			filters[0] = new InputFilter() {

				@Override
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {

					// TODO Auto-generated method stub
					if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("NO SPECIAL CHAR")) {

						if (end > start) {

							char[] acceptedChars = new char[] { 'a', 'b', 'c',
									'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
									'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
									't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
									'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
									'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
									'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
									'Z', '0', '1', '2', '3', '4', '5', '6',
									'7', '8', '9' };

							for (int index = start; index < end; index++) {

								if (!new String(acceptedChars).contains(String
										.valueOf(source.charAt(index)))) {

									showToast(SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.no_special_characters_allowed));

									return "";

								}
							}
						}
					}

					else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("EMAIL")) {

						if (end > start) {

							char[] acceptedChars = new char[] { '@', '.', '_',
									'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
									'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
									'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
									'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F',
									'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
									'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
									'W', 'X', 'Y', 'Z', '0', '1', '2', '3',
									'4', '5', '6', '7', '8', '9' };

							for (int index = start; index < end; index++) {

								if (!new String(acceptedChars).contains(String
										.valueOf(source.charAt(index)))) {

									showToast("Only @ . _ are allowed");

									return "";

								}
							}
						}
					}

					return null;
				}

			};

			defaultvalue.setFilters(filters);

			input_menu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intentComponent = new Intent(context,
							Component.class);
					Bundle bndl = new Bundle();
					bndl.putString("type", "");
					bndl.putBoolean("action", true);
					bndl.putBoolean("forms", false);
					intentComponent.putExtras(bndl);
					startActivityForResult(intentComponent, 100);

				}
			});

			scroll = (ScrollView) findViewById(R.id.scroller);

			edtxt = (EditText) findViewById(R.id.input_name);
			edtxt.setFilters(getFilter());
			computeFields = new ArrayList<String>();
			computeFieldsExpresion = new ArrayList<String>();
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
	protected void onDestroy() {
		super.onDestroy();

		WebServiceReferences.contextTable.remove("textinputactivity");

	}

	@Override
	public void onClick(View v) {
		try {
			if (v.getId() == btn_cancel1.getId()) {
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				CallDispatcher.getValidation = true;
				if (WebServiceReferences.contextTable.containsKey("frmcreator")) {
					AddNewForm addNewForm = (AddNewForm) WebServiceReferences.contextTable
							.get("frmcreator");
					addNewForm.ll.setVisibility(View.GONE);
					addNewForm.fadeIn = new AlphaAnimation(0.8f, 1f);
					addNewForm.fadeIn.setFillAfter(true);
					addNewForm.container.startAnimation(addNewForm.fadeIn);
					addNewForm.canbtn.setClickable(true);
					if (CallDispatcher.inputFieldList.size() > 0) {
						addNewForm.nextbtn.setEnabled(true);
					}
					addNewForm.nextbtn.setClickable(true);
				}
				finish();

			} else if (v.getId() == btn_cmp.getId()) {
				CallDispatcher.getValidation = true;

				if (ValidateForm()) {
					String name = edtxt.getText().toString().trim();
					String validdatafields = edtxt_validda.getText().toString()
							.trim();
					String defaultvas = defaultvalue.getText().toString()
							.trim();
					String instrucs = inst.getText().toString().trim();
					String errorstips = errortip.getText().toString().trim();

					if (instrucs.length() != 0) {

						if (instrucs.endsWith(",")) {
							instrucs = instrucs.substring(0,
									instrucs.length() - 1);

							inputFields.setFieldName(name);
							inputFields.setFieldType(entry);
							inputFields.setValidData(validdatafields);
							inputFields.setDefaultValue(defaultvas);
							inputFields.setInstructions(instrucs);
							inputFields.setErrorMsg(errorstips);
							CallDispatcher.inputFieldList.add(inputFields);
						} else {

							inputFields.setFieldName(name);
							inputFields.setFieldType(entry);
							inputFields.setValidData(validdatafields);
							inputFields.setDefaultValue(defaultvas);
							inputFields.setInstructions(instrucs);
							inputFields.setErrorMsg(errorstips);
							CallDispatcher.inputFieldList.add(inputFields);
						}

					} else {
						inputFields.setFieldName(name);
						inputFields.setFieldType(entry);
						inputFields.setValidData(validdatafields);
						inputFields.setDefaultValue(defaultvas);
						inputFields.setInstructions("");
						inputFields.setErrorMsg(errorstips);
						CallDispatcher.inputFieldList.add(inputFields);

					}

					CallDispatcher.getValidation = true;
					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					if (WebServiceReferences.contextTable
							.containsKey("frmcreator")) {
						AddNewForm addNewForm = (AddNewForm) WebServiceReferences.contextTable
								.get("frmcreator");
						addNewForm.ll.setVisibility(View.GONE);
						addNewForm.fadeIn = new AlphaAnimation(0.8f, 1f);
						addNewForm.fadeIn.setFillAfter(true);
						addNewForm.container.startAnimation(addNewForm.fadeIn);
						addNewForm.canbtn.setClickable(true);
						if (CallDispatcher.inputFieldList.size() > 0) {
							addNewForm.customAdapter.notifyDataSetChanged();
							addNewForm.nextbtn.setEnabled(true);
							addNewForm.tap.setVisibility(View.GONE);
						} else {
							addNewForm.tap.setVisibility(View.VISIBLE);
						}
						addNewForm.nextbtn.setClickable(true);
					}
					finish();
				}

			}

			else if (v.getId() == edtxt_validda.getId()) {

				if (entry.equalsIgnoreCase("Drop Down")) {
					showListDialog();
				} else if (entry.equalsIgnoreCase("radio button")) {
					Intent intent = new Intent(context, HardCodeQuery.class);
					intent.putExtra("isRadio", true);
					startActivityForResult(intent, 2);
					ValidData = "";
				} else if (entry.equalsIgnoreCase("Date and Time")) {
					showView(v);
					ValidData = "";
				} else if (entry.equalsIgnoreCase("Free Text")) {
					ShowText(v);
					ValidData = "";
				} else if (entry.equalsIgnoreCase("Numeric")) {
					ShowNumeric(v);
					ValidData = "";
				} else if (entry.equalsIgnoreCase("Current Date")) {
					ValidData = "";
				} else if (entry.equalsIgnoreCase("Current Time")) {
					ValidData = "";
				} else if (entry.equalsIgnoreCase("Current location")) {
					ValidData = "";
				} else if (entry.equalsIgnoreCase("Compute")) {
					ValidData = "";
					computeFields(v);
				} else {
					ValidData = "";
				}
			} else if (v.getId() == defaultvalue.getId()) {
				if (entry.equalsIgnoreCase("Drop Down")) {
					addView(v);
				} else if (entry.equalsIgnoreCase("radio button")) {
					addView(v);
				} else if (entry.equalsIgnoreCase("Date and Time")) {
					if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("GT " + greatertime)) {

						Button btnSet;
						final DatePicker dp;
						final TimePicker tp;
						final AlertDialog alertReminder = new AlertDialog.Builder(
								context).create();

						ScrollView tblDTPicker = (ScrollView) View.inflate(
								context, R.layout.date_time_picker, null);

						btnSet = (Button) tblDTPicker
								.findViewById(R.id.btnSetDateTime);
						dp = (DatePicker) tblDTPicker
								.findViewById(R.id.datePicker);
						tp = (TimePicker) tblDTPicker
								.findViewById(R.id.timePicker);

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

								if (callDisp.CheckGreaterDateandTime(pickgdate,
										greatertime)) {
									alertReminder.dismiss();
									Intent reminderIntent = new Intent(context,
											ReminderService.class);
									startService(reminderIntent);
									defaultvalue.setText(pickgdate);
								} else {
									showToast("Kindly Select above Date from Valid Data");

								}
							}
						});
						alertReminder.setTitle(SingleInstance.mainContext
								.getResources().getString(
										R.string.date_and_time));
						alertReminder.setView(tblDTPicker);
						alertReminder.show();

					} else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("LT " + lesstime)) {

						Button btnSet;
						final DatePicker dp;
						final TimePicker tp;
						final AlertDialog alertReminder = new AlertDialog.Builder(
								context).create();

						ScrollView tblDTPicker = (ScrollView) View.inflate(
								context, R.layout.date_time_picker, null);

						btnSet = (Button) tblDTPicker
								.findViewById(R.id.btnSetDateTime);
						dp = (DatePicker) tblDTPicker
								.findViewById(R.id.datePicker);
						tp = (TimePicker) tblDTPicker
								.findViewById(R.id.timePicker);

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

								if (callDisp.CheckLessDateandTime(pickldate,
										lesstime)) {
									alertReminder.dismiss();
									Intent reminderIntent = new Intent(context,
											ReminderService.class);
									startService(reminderIntent);
									defaultvalue.setText(pickldate);
								} else {
									showToast("Kindly Select below Date from Valid Data");

								}
							}
						});
						alertReminder.setTitle(SingleInstance.mainContext
								.getResources().getString(
										R.string.date_and_time));
						alertReminder.setView(tblDTPicker);
						alertReminder.show();

					} else if (edtxt_validda.getText().toString()
							.startsWith("BW")) {

						Button btnSet;
						final DatePicker dp;
						final TimePicker tp;
						final AlertDialog alertReminder = new AlertDialog.Builder(
								context).create();

						ScrollView tblDTPicker = (ScrollView) View.inflate(
								context, R.layout.date_time_picker, null);

						btnSet = (Button) tblDTPicker
								.findViewById(R.id.btnSetDateTime);
						dp = (DatePicker) tblDTPicker
								.findViewById(R.id.datePicker);
						tp = (TimePicker) tblDTPicker
								.findViewById(R.id.timePicker);

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

								if (callDisp.Checkbetweendateandtime(pickbdate,
										starttime, endtime)) {
									alertReminder.dismiss();
									Intent reminderIntent = new Intent(context,
											ReminderService.class);
									startService(reminderIntent);
									defaultvalue.setText(pickbdate);
								} else {
									showToast("Kindly Select in between Date from Valid Data");

								}
							}
						});
						alertReminder.setTitle(SingleInstance.mainContext
								.getResources().getString(
										R.string.date_and_time));
						alertReminder.setView(tblDTPicker);
						alertReminder.show();

					}
				} else if (entry.equalsIgnoreCase("Free Text"))

				{

					if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("EMAIL")) {
						defaultvalue.setFocusableInTouchMode(true);
						defaultvalue
								.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

						if (!validateEmail(defaultvalue.getText().toString()
								.trim())) {
							defaultvalue.requestFocus();
						}

					} else if (edtxt_validda.getText().toString()
							.contains("MINIMUM")) {
						defaultvalue.setFocusableInTouchMode(true);
						if (defaultvalue.getText().toString().trim().length() < Integer
								.parseInt(userInput.getText().toString())) {
							defaultvalue.requestFocus();

						}

					}

					else if (edtxt_validda.getText().toString()
							.contains("MAXIMUM")) {
						defaultvalue.setFocusableInTouchMode(true);
						if (defaultvalue.getText().toString().trim().length() > Integer
								.parseInt(userInput.getText().toString())) {
							defaultvalue.requestFocus();

						}

					}

					else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("NO SPECIAL CHAR")) {
						defaultvalue.setFocusableInTouchMode(true);
						if (!nospecialchar(defaultvalue.getText().toString()
								.trim())) {
							defaultvalue.requestFocus();
						}

					} else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("BEGINS WITH ALPHA")) {
						defaultvalue.setFocusableInTouchMode(true);
						if (!beginalpha(defaultvalue.getText().toString()
								.trim())) {
							defaultvalue.requestFocus();
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.begin_with_alpha));
						}

					}
				} else if (entry.equalsIgnoreCase("Numeric")) {
					if (edtxt_validda.getText().length() == 0) {
						defaultvalue.setFocusableInTouchMode(false);

					}

					if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("LT " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);
						defaultvalue.setInputType(InputType.TYPE_CLASS_NUMBER);

					}

					else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("LE " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);
						defaultvalue.setInputType(InputType.TYPE_CLASS_NUMBER);

					} else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("GT " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);
						defaultvalue.setInputType(InputType.TYPE_CLASS_NUMBER);

					}

					else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("GE " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);
						defaultvalue.setInputType(InputType.TYPE_CLASS_NUMBER);

					}

					else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("EQ " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);

					} else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("NEQ " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);

					} else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("Accuracy " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);
						defaultvalue.setInputType(InputType.TYPE_CLASS_NUMBER);

					} else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("BT " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);
						defaultvalue.setInputType(InputType.TYPE_CLASS_NUMBER);

					}
				} else {

				}
			}

			else if (v.getId() == btn_submit.getId()) {

				for (int i = 0; i < layout_container.getChildCount(); i++) {

					RelativeLayout rlLayout = (RelativeLayout) layout_container
							.getChildAt(i);

					ed_fld = (EditText) rlLayout.findViewById(R.id.value);

					if (ValidData.trim().length() == 0) {
						if (ed_fld.getText().toString().length() != 0) {
							ValidData = ed_fld.getText().toString().trim();
						}
					} else {
						if (ed_fld.getText().toString().length() != 0) {
							ValidData = ValidData + ","
									+ ed_fld.getText().toString().trim();
						}
					}

				}

				if (!ed_fld.getText().toString().trim().equals("")) {

					viewfli.showPrevious();

					edtxt_validda.setText(ValidData);

				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.enter_valid_data));
				}
				removeFormFields();

			}

			else if (v.getId() == btn_canceldate.getId()) {
				Intent intent = new Intent(TextInputActivity.this,
						TextInputActivity.class);
				startActivity(intent);
			} else if (v.getId() == et_startdatetime.getId()) {

				Button btnSet;
				final DatePicker dp;
				final TimePicker tp;
				final AlertDialog alertReminder = new AlertDialog.Builder(
						context).create();

				ScrollView tblDTPicker = (ScrollView) View.inflate(context,
						R.layout.date_time_picker, null);

				btnSet = (Button) tblDTPicker.findViewById(R.id.btnSetDateTime);
				dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
				tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);

				btnSet.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						starttime = +dp.getYear()
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

						alertReminder.dismiss();
						Intent reminderIntent = new Intent(context,
								ReminderService.class);
						startService(reminderIntent);
						et_startdatetime.setText(starttime);

					}
				});
				alertReminder.setTitle(SingleInstance.mainContext
						.getResources().getString(R.string.date_and_time));
				alertReminder.setView(tblDTPicker);
				alertReminder.show();
			}

			else if (v.getId() == et_enddatetime.getId()) {

				Button btnSet;
				final DatePicker dp;
				final TimePicker tp;
				final AlertDialog alertReminder = new AlertDialog.Builder(
						context).create();

				ScrollView tblDTPicker = (ScrollView) View.inflate(context,
						R.layout.date_time_picker, null);

				btnSet = (Button) tblDTPicker.findViewById(R.id.btnSetDateTime);
				dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
				tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);

				btnSet.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						endtime = +dp.getYear()
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

						alertReminder.dismiss();
						Intent reminderIntent = new Intent(context,
								ReminderService.class);
						startService(reminderIntent);
						et_enddatetime.setText(endtime);

					}
				});
				alertReminder.setTitle(SingleInstance.mainContext
						.getResources().getString(R.string.date_and_time));
				alertReminder.setView(tblDTPicker);
				alertReminder.show();
			}

			else if (v.getId() == btn_submitdate.getId())

			{
				if (!et_startdatetime.getText().toString().trim().equals("")
						&& !et_enddatetime.getText().toString().trim()
								.equals("")) {
					viewfli.showPrevious();
					viewfli.setDisplayedChild(0);
					dateandtime = "BW " + starttime + ", " + endtime;
					edtxt_validda.setText(dateandtime);
				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.select_date_and_time));
				}

			} else if (v.getId() == btn_numsubmit.getId()) {
				if (ed_number.getText().toString().trim().length() != 0) {
					if (edtxt_validda.getText().toString()
							.equals("Lesser Than")) {
						viewfli.showPrevious();
						viewfli.setDisplayedChild(0);
						edtxt_validda.setText("LT " + numeric);

					} else if (edtxt_validda.getText().toString()
							.equals("Lesser Than Or Equal")) {
						viewfli.showPrevious();
						viewfli.setDisplayedChild(0);
						edtxt_validda.setText("LE " + numeric);

					} else if (edtxt_validda.getText().toString()
							.equals("Greater Than")) {
						viewfli.showPrevious();
						viewfli.setDisplayedChild(0);
						edtxt_validda.setText("GT " + numeric);
					} else if (edtxt_validda.getText().toString()
							.equals("Greater Than Or Equal")) {
						viewfli.showPrevious();
						viewfli.setDisplayedChild(0);
						edtxt_validda.setText("GE " + numeric);

					} else if (edtxt_validda.getText().toString()
							.equals("Equal")) {
						viewfli.showPrevious();
						viewfli.setDisplayedChild(0);
						edtxt_validda.setText("EQ " + numeric);

					}

					else if (edtxt_validda.getText().toString()
							.equals("Not Equal")) {
						viewfli.showPrevious();
						viewfli.setDisplayedChild(0);
						edtxt_validda.setText("NEQ " + numeric);

					} else if (edtxt_validda.getText().toString()
							.equals("Accuracy")) {
						viewfli.showPrevious();
						viewfli.setDisplayedChild(0);
						edtxt_validda.setText("Accuracy " + numeric);

					} else if (edtxt_validda.getText().toString()
							.equals("Between")) {
						viewfli.showPrevious();
						viewfli.setDisplayedChild(0);

					}

				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.kindly_enter_the_number));
				}
				defaultvalue.setFocusableInTouchMode(true);
			} else if (v.getId() == btn_numcancel.getId()) {
				viewfli.showPrevious();
				viewfli.setDisplayedChild(0);
			}

			else if (v.getId() == btn_btnumsum.getId()) {
				startnumber = ed_numberone.getText().toString().trim();
				endnumber = ed_numbertwo.getText().toString().trim();

				if (ed_numberone.getText().toString().trim().length() != 0
						&& ed_numbertwo.getText().toString().trim().length() != 0) {
					viewfli.showPrevious();
					viewfli.setDisplayedChild(0);
					numeric = startnumber + " and " + endnumber;
					edtxt_validda.setText("BT " + numeric);

				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.please_enter_number_here));
				}
				defaultvalue.setFocusableInTouchMode(true);

			} else if (v.getId() == btn_btnumcan.getId()) {
				viewfli.showPrevious();
				viewfli.setDisplayedChild(0);
				edtxt_validda.setText("");
			}

			else if (v.getId() == iview_add.getId()) {
				addFormField();
			}

			else if (v.getId() == iview_remove.getId()) {
				removeFormField();
			}

			else if (v.getId() == btn_addcompute.getId()) {
				showExpression(v);

			} else if (v.getId() == add_field.getId()) {

				addFormCompute();

			} else if (v.getId() == compute.getId()) {
				viewfli.showPrevious();
				viewfli.setDisplayedChild(0);
				StringBuilder sbt = new StringBuilder();
				formula = "";
				computeFields.clear();
				computeFieldsExpresion.clear();

				for (int j = 0; j < computelayy.getChildCount(); j++) {

					RelativeLayout layout = (RelativeLayout) computelayy
							.getChildAt(j);
					Button btn_addcomputein = (Button) layout
							.findViewById(R.id.btn_addcompute);
					EditText compfield = (EditText) layout
							.findViewById(R.id.compfield);

					computeFields.add(compfield.getText().toString());
					computeFieldsExpresion.add(btn_addcomputein
							.getContentDescription().toString());

				}
				for (int i = 0; i < computeFields.size(); i++) {

					sbt.append(computeFields.get(i));

					if (!(computeFieldsExpresion.get(i).equalsIgnoreCase(""))) {
						sbt.append(computeFieldsExpresion.get(i));

					}

				}

				formula = sbt.toString();

				for (int a = 0; a < details.size(); a++) {
					fieldname = new String[6];
					fieldname = details.get(a);
				}

				if (fieldname[1].contains("Numeric")) {
					edtxt_validda.setText("NM " + formula);
				}

				else if (fieldname[1].contains("Free Text")) {

					edtxt_validda.setText("FT " + formula);
				}

				else if (fieldname[1].contains("Date")) {

					edtxt_validda.setText("DT " + formula);

				}

				removecomputeFields();
			}

			else if (v.getId() == btn_dropdown.getId()) {
				ShowField(v);
				compfield.setText("");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void ShowField(final View v) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					TextInputActivity.this);
			builder.create();
			builder.setTitle(SingleInstance.mainContext.getResources()
					.getString(R.string.field_value));

			final String[] co = comList.toArray(new String[comList.size()]);

			builder.setItems(co, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					changeFiled(co[which].toString(), v);
					alert.cancel();
				}
			});
			alert = builder.create();
			alert.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void changeFiled(String type, View v) {

		compfield.setText(type);

	}

	private void showExpression(final View v) {

		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					TextInputActivity.this);
			builder.create();
			builder.setTitle("Expression");

			final CharSequence[] choiceList = { "+", "-", "*", "/" };

			builder.setItems(choiceList, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					changeExpression(choiceList[which].toString(), v);
					alert.cancel();
				}
			});
			alert = builder.create();
			alert.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void changeExpression(String type, View v) {
		try {
			btn_addcompute.setContentDescription(type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean validateEmail(String email) {
		try {
			if (email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
					&& email.length() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private boolean nospecialchar(String nospec) {

		try {
			Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
			Matcher match = pt.matcher(nospec);
			boolean isExists = true;
			while (match.find()) {
				isExists = false;
				break;
			}
			return isExists;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	private boolean beginalpha(String val) {

		try {
			if (val.length() > 0) {
				String str = defaultvalue.getText().toString();
				char c = str.charAt(0);
				if (!Character.isLetter(c)) {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.begin_with_alpha));
					return false;
				}
			}

			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private void ShowNumeric(final View v) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					TextInputActivity.this);
			builder.create();
			builder.setTitle("Numeric");
			final CharSequence[] choiceList = {
					SingleInstance.mainContext.getResources().getString(
							R.string.lesser_than),
					SingleInstance.mainContext.getResources().getString(
							R.string.lesser_equal),
					SingleInstance.mainContext.getResources().getString(
							R.string.greater_than),
					SingleInstance.mainContext.getResources().getString(
							R.string.greater_than_equal),
					SingleInstance.mainContext.getResources().getString(
							R.string.equal),
					SingleInstance.mainContext.getResources().getString(
							R.string.not_equal),
					SingleInstance.mainContext.getResources().getString(
							R.string.accuracy),
					SingleInstance.mainContext.getResources().getString(
							R.string.between) };

			int selected = -1;

			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							changeNumber(choiceList[which].toString(), v);
							alert.cancel();

						}
					});
			alert = builder.create();
			alert.show();
			defaultvalue.setText("");
			defaultvalue.requestFocus();
			defaultvalue.setFocusableInTouchMode(true);
			defaultvalue.setInputType(InputType.TYPE_CLASS_NUMBER);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void changeNumber(String type, View v) {
		try {
			edtxt_validda.setText(type);

			if (edtxt_validda.getText().toString().equals("Lesser Than")) {

				CallDispatcher.numberMode = "LT";
				Intent i = new Intent(context, NumberOperation.class);
				startActivityForResult(i, 11);
			} else if (edtxt_validda.getText().toString()
					.equals("Lesser Than Or Equal")) {

				CallDispatcher.numberMode = "LE";
				Intent i = new Intent(context, NumberOperation.class);
				startActivityForResult(i, 11);
			} else if (edtxt_validda.getText().toString()
					.equals("Greater Than")) {

				CallDispatcher.numberMode = "GT";
				Intent i = new Intent(context, NumberOperation.class);
				startActivityForResult(i, 11);

			} else if (edtxt_validda.getText().toString()
					.equals("Greater Than Or Equal")) {

				CallDispatcher.numberMode = "GE";
				Intent i = new Intent(context, NumberOperation.class);
				startActivityForResult(i, 11);

			} else if (edtxt_validda.getText().toString().equals("Equal")) {

				CallDispatcher.numberMode = "EQ";
				Intent i = new Intent(context, NumberOperation.class);
				startActivityForResult(i, 11);

			}

			else if (edtxt_validda.getText().toString().equals("Not Equal")) {

				CallDispatcher.numberMode = "NEQ";
				Intent i = new Intent(context, NumberOperation.class);
				startActivityForResult(i, 11);

			} else if (edtxt_validda.getText().toString().equals("Accuracy")) {

				CallDispatcher.numberMode = "Accuracy";
				Intent i = new Intent(context, NumberOperation.class);
				startActivityForResult(i, 11);

			} else if (edtxt_validda.getText().toString().equals("Between")) {

				CallDispatcher.numberMode = "BT";
				Intent i = new Intent(context, NumberOperation.class);
				startActivityForResult(i, 11);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void computeFields(final View v) {
		try {
			Intent i = new Intent(context, Compute.class);
			startActivityForResult(i, 12);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void ShowText(final View v) {

		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					TextInputActivity.this);
			builder.create();
			builder.setTitle("Free Text");
			final CharSequence[] choiceList = {
					SingleInstance.mainContext.getResources().getString(
							R.string.email),
					SingleInstance.mainContext.getResources().getString(
							R.string.minimum_no_char),
					SingleInstance.mainContext.getResources().getString(
							R.string.maximum_no_char),
					SingleInstance.mainContext.getResources().getString(
							R.string.no_special_char),
					SingleInstance.mainContext.getResources().getString(
							R.string.alpha_begin) };

			int selected = -1;

			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							changeText(choiceList[which].toString(), v);
							LayoutInflater li = LayoutInflater.from(context);
							View promptsView = li
									.inflate(R.layout.prompt, null);
							if (choiceList[which].toString().equalsIgnoreCase(
									"MINIMUM NO OF CHAR")
									|| choiceList[which].toString()
											.equalsIgnoreCase(
													"MAXIMUM NO OF CHAR")) {
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										context);

								alertDialogBuilder.setView(promptsView);

								userInput = (EditText) promptsView
										.findViewById(R.id.editTextDialogUserInput);

								alertDialogBuilder
										.setCancelable(false)
										.setPositiveButton(
												"OK",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														if (edtxt_validda
																.getText()
																.toString()
																.contains(
																		"MINIMUM")) {
															edtxt_validda
																	.setText("MINIMUM "
																			+ userInput
																					.getText()
																					.toString()
																			+ " CHAR");
														} else if (edtxt_validda
																.getText()
																.toString()
																.contains(
																		"MAXIMUM")) {
															edtxt_validda
																	.setText("MAXIMUM "
																			+ userInput
																					.getText()
																					.toString()
																			+ " CHAR");
														}

													}
												})
										.setNegativeButton(
												"Cancel",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														edtxt_validda
																.setText("");
														dialog.cancel();
													}
												});

								AlertDialog alertDialog = alertDialogBuilder
										.create();

								alertDialog.show();
								alert.cancel();
							} else {
								alert.cancel();
							}
						}
					});
			alert = builder.create();
			alert.show();
			defaultvalue.setText("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void changeText(String type, View v) {
		try {
			edtxt_validda.setText(type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showView(final View v) {

		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					TextInputActivity.this);
			builder.create();
			builder.setTitle("Date and Time");
			final CharSequence[] choiceList = {
					SingleInstance.mainContext.getResources().getString(
							R.string.greater_than_date_time),
					SingleInstance.mainContext.getResources().getString(
							R.string.less_than_date_time),
					SingleInstance.mainContext.getResources().getString(
							R.string.between_date_time) };

			int selected = -1; // does not select anything

			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							changeType(choiceList[which].toString(), v);
							alert.cancel();
						}
					});
			alert = builder.create();
			alert.show();

			defaultvalue.setText("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void changeType(String type, View v) {

		try {
			edtxt_validda.setText(type);

			if (edtxt_validda.getText().toString()
					.equalsIgnoreCase("Greater than Date and Time")) {

				Button btnSet;
				final DatePicker dp;
				final TimePicker tp;
				final AlertDialog alertReminder = new AlertDialog.Builder(
						context).create();

				ScrollView tblDTPicker = (ScrollView) View.inflate(context,
						R.layout.date_time_picker, null);

				btnSet = (Button) tblDTPicker.findViewById(R.id.btnSetDateTime);
				dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
				tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);
				tp.setIs24HourView(true);
				;
				btnSet.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						greatertime = +dp.getYear()
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

						alertReminder.dismiss();
						Intent reminderIntent = new Intent(context,
								ReminderService.class);
						startService(reminderIntent);
						edtxt_validda.setText("GT " + greatertime);

					}
				});
				alertReminder.setTitle(SingleInstance.mainContext
						.getResources().getString(R.string.date_and_time));
				alertReminder.setView(tblDTPicker);
				alertReminder.show();

			} else if (edtxt_validda.getText().toString()
					.equalsIgnoreCase("Less than Date and Time")) {

				Button btnSet;
				final DatePicker dp;
				final TimePicker tp;
				final AlertDialog alertReminder = new AlertDialog.Builder(
						context).create();

				ScrollView tblDTPicker = (ScrollView) View.inflate(context,
						R.layout.date_time_picker, null);

				btnSet = (Button) tblDTPicker.findViewById(R.id.btnSetDateTime);
				dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
				tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);
				tp.setIs24HourView(true);
				;

				btnSet.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						lesstime = +dp.getYear()
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

						alertReminder.dismiss();
						Intent reminderIntent = new Intent(context,
								ReminderService.class);
						startService(reminderIntent);
						edtxt_validda.setText("LT " + lesstime);

					}
				});
				alertReminder.setTitle(SingleInstance.mainContext
						.getResources().getString(R.string.date_and_time));
				alertReminder.setView(tblDTPicker);
				alertReminder.show();

			}

			else if (edtxt_validda.getText().toString()
					.equalsIgnoreCase("Between Date and Time")) {
				CallDispatcher.numberMode = "BW";

				Intent i = new Intent(context, DateBetween.class);
				startActivityForResult(i, 11);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	String getRecords(String query) {

		try {
			rec_list = callDisp.getdbHeler(context).isQueryContainResults(
					query, "");
			String val = null;
			StringBuffer values = new StringBuffer();
			for (int i = 0; i < rec_list.size(); i++) {

				String[] ff = rec_list.get(i);
				for (int j = 0; j < ff.length; j++) {
					if (ff[j].length() > 0) {
						values.append(ff[j]);
						values.append(",");
					}

				}
				val = values.toString().substring(0,
						values.toString().length() - 1);

			}

			if (val == null) {
				val = "None,";
			}
			return val;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private void addView(final View v) {

		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					TextInputActivity.this);
			builder.create();
			builder.setTitle(SingleInstance.mainContext.getResources()
					.getString(R.string.default_value));
			String validData = edtxt_validda.getText().toString().trim();
			if (validData.length() > 0) {
				final String[] Values;

				if (validData.startsWith("H")) {
					String validdataValues = validData.replace("H", "").trim();

					Values = validdataValues.split(",");

				} else if (validData.startsWith("R")) {
					String validdataValues = validData.replace("R", "").trim();

					Values = validdataValues.split(",");
				} else {
					String query = validData.substring(1).trim();
					Values = getRecords(query).split(",");

				}
				for (int i = 0; i < Values.length; i++) {
				}
				int selected = -1; // does not select anything

				builder.setSingleChoiceItems(Values, selected,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								changeField(Values[which].toString(), v);
								alert.cancel();
							}
						});
				alert = builder.create();
				alert.show();
			} else {

				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.enter_valid_data), Toast.LENGTH_LONG)
						.show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void changeField(String type, View v) {

		try {
			defaultvalue.setText(type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean ValidateForm() {

		try {
			if (edtxt.getText().toString().trim().length() != 0) {
				if (entry.trim().length() != 0) {

					for (int i = 0; i < CallDispatcher.inputFieldList.size(); i++) {

						InputsFields fields = CallDispatcher.inputFieldList
								.get(i);

						if (edtxt.getText().toString().trim()
								.equalsIgnoreCase(fields.getFieldName())) {
							showToast(SingleInstance.mainContext.getResources()
									.getString(
											R.string.field_name_already_exist));
							return false;

						}

					}

					if (entry.equalsIgnoreCase("radio button")
							|| entry.equalsIgnoreCase("Drop Down")
							|| entry.equalsIgnoreCase("Compute")) {

						if (edtxt_validda.getText().toString().trim().length() == 0) {
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.enter_valid_data));
							return false;

						}
					}
					if (entry.equalsIgnoreCase("Free Text")) {

						if (edtxt_validda.getText().toString()
								.equalsIgnoreCase("EMAIL")) {
							defaultvalue.setFocusableInTouchMode(true);
							defaultvalue
									.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
							if (defaultvalue.getText().toString().trim()
									.length() != 0) {

								if (!validateEmail(defaultvalue.getText()
										.toString().trim())) {
									showToast("Please enter valid Email address");
									return false;
								}
							}
						} else if (edtxt_validda.getText().toString()
								.contains("MINIMUM")) {
							defaultvalue.setFocusableInTouchMode(true);

							if (defaultvalue.getText().toString().trim()
									.length() != 0
									&& userInput.getText().toString() != null) {

								if (defaultvalue.getText().toString().trim()
										.length() < Integer.parseInt(userInput
										.getText().toString())) {
									showToast("Text must be Minimum "
											+ userInput.getText().toString()
											+ " characters");
									return false;
								}
							}

						} else if (edtxt_validda.getText().toString()
								.contains("MAXIMUM")) {
							defaultvalue.setFocusableInTouchMode(true);
							if (defaultvalue.getText().toString().trim()
									.length() != 0
									&& userInput.getText().toString() != null) {

								if (defaultvalue.getText().toString().trim()
										.length() > Integer.parseInt(userInput
										.getText().toString())) {
									showToast("Text allowed Maximum "
											+ userInput.getText().toString()
											+ " characters");
									return false;

								}
							}

						}

						else if (edtxt_validda.getText().toString()
								.equalsIgnoreCase("NO SPECIAL CHAR")) {
							defaultvalue.setFocusableInTouchMode(true);
							if (defaultvalue.getText().toString().trim()
									.length() != 0) {

								if (!nospecialchar(defaultvalue.getText()
										.toString().trim())) {
									showToast(SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.no_special_characters_allowed));
									return false;

								}

							}
						} else if (edtxt_validda.getText().toString()
								.equalsIgnoreCase("BEGINS WITH ALPHA")) {
							defaultvalue.setFocusableInTouchMode(true);
							if (defaultvalue.getText().toString().trim()
									.length() != 0) {

								if (!beginalpha(defaultvalue.getText()
										.toString().trim())) {
									showToast(SingleInstance.mainContext
											.getResources().getString(
													R.string.begin_with_alpha));
									return false;
								}
							}

						}
					} else if (entry.equalsIgnoreCase("numeric")) {
						String validData = edtxt_validda.getText().toString();
						String[] value = validData.split(" ");
						if (defaultvalue.getText().toString().trim().length() != 0) {

							if (value[0].equals("LT")) {

								if (!(Integer.parseInt(defaultvalue.getText()
										.toString().trim()) < Integer
										.parseInt(value[1])))

								{
									showToast("Kindly Enter less then value "
											+ value[1]);
									return false;

								}
							} else if (value[0].equals("LE")) {

								if (!(Integer.parseInt(defaultvalue.getText()
										.toString().trim()) <= Integer
										.parseInt(value[1]))) {
									showToast("Kindly Enter less then or equal value "
											+ value[1]);
									return false;
								}
							}

							else if (value[0].equals("GT")) {

								if (!(Integer.parseInt(defaultvalue.getText()
										.toString().trim()) > Integer
										.parseInt(value[1]))) {

									showToast("Kindly Enter greater then value "
											+ value[1]);
									return false;
								}
							}

							else if (value[0].equals("GE")) {

								if (!(Integer.parseInt(defaultvalue.getText()
										.toString().trim()) >= Integer
										.parseInt(value[1]))) {
									showToast("Kindly Enter greater then equal value "
											+ value[1]);
									return false;
								}

							}

							else if (value[0].equals("EQ")) {

								if (!(Integer.parseInt(defaultvalue.getText()
										.toString().trim()) == Integer
										.parseInt(value[1]))) {
									showToast("Kindly Enter equal value "
											+ value[1]);
									return false;
								}
							}

							else if (value[0].equals("NEQ")) {

								if (!(Integer.parseInt(defaultvalue.getText()
										.toString().trim()) != Integer
										.parseInt(value[1]))) {
									showToast("Kindly Enter not equal value "
											+ value[1]);
									return false;
								}
							} else if (value[0].equals("Accuracy")) {

								if (defaultvalue.getText().toString().trim()
										.length() == Integer.parseInt(value[1])) {
									showToast("Kindly Enter accuracy value digit is "
											+ value[1]);
									return false;
								}
							} else if (value[0].equals("BT")) {

								if (!(Integer.parseInt(defaultvalue.getText()
										.toString().trim()) > Integer
										.parseInt(value[1]) && Integer
										.parseInt(defaultvalue.getText()
												.toString().trim()) < Integer
										.parseInt(value[3]))) {

									showToast("Kindly Enter between values from your validadata");
									return false;

								}
							}

						}

					}

					if (!edtxt.getText().toString().trim()
							.equalsIgnoreCase("tableid")
							&& !edtxt.getText().toString().trim()
									.equalsIgnoreCase("id")
							&& !edtxt.getText().toString().trim()
									.equalsIgnoreCase("uuid")
							&& !edtxt.getText().toString().trim()
									.equalsIgnoreCase("uudate")
							&& !edtxt.getText().toString().trim()
									.equalsIgnoreCase("recorddate")
							&& !edtxt.getText().toString().trim()
									.equalsIgnoreCase("status")) {
						Pattern ptrn = Pattern.compile("[a-zA-Z]");
						String value = edtxt.getText().toString().trim()
								.substring(0, 1);
						Matcher m = ptrn.matcher(value);
						if (m.find()) {

							if (entry.equalsIgnoreCase("Free Text")) {
								recordname = edtxt.getText().toString().trim();

							} else if (entry.equalsIgnoreCase("numeric")) {
								recordname = edtxt.getText().toString().trim();

							} else if (entry.equalsIgnoreCase("Multimedia")) {
								recordname = "blob_"
										+ edtxt.getText().toString().trim();

							} else {
								recordname = edtxt.getText().toString().trim();

							}
							return true;

						} else {
							showToast("Field names should not starts with Numeric letters");
							return false;

						}
					} else {
						showToast("Kindly Don't use tableid,id,uuid,uudate as Form fields,that are reserved");
						return false;

					}
				}

				else {

					showToast("Kindly Enter EntryMode");
					return false;

				}

			}

			else {
				showToast(SingleInstance.mainContext.getResources().getString(R.string.kindly_give_your_form_record_name));
				return false;

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	private void addFormCompute() {

		try {
			layoutInflate = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layou = (RelativeLayout) layoutInflate.inflate(
					R.layout.computefields, null);
			layou.setId(computelayy.getChildCount());

			computelayy.addView(layou);
			scrollview.post(new Runnable() {

				@Override
				public void run() {

					scrollview.fullScroll(ScrollView.FOCUS_DOWN);

				}
			});

			Button dropdownin = (Button) layou.findViewById(R.id.btn_dropdown);
			final Button btn_addcomputein = (Button) layou
					.findViewById(R.id.btn_addcompute);
			final EditText compfield = (EditText) layou
					.findViewById(R.id.compfield);
			dropdownin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					ShowFields(v);
				}

				private void ShowFields(final View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							TextInputActivity.this);
					builder.create();
					builder.setTitle(SingleInstance.mainContext.getResources()
							.getString(R.string.field_value));

					final String[] co = comList.toArray(new String[comList
							.size()]);

					builder.setItems(co, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							changeFiled(co[which].toString(), v);
							alert.cancel();
						}
					});
					alert = builder.create();
					alert.show();
				}

				private void changeFiled(String type, View v) {

					compfield.setText(type);

					if (selectedfields.trim().length() == 0) {
						selectedfields = compfield.getText().toString();
					} else {
						selectedfields = selectedfields + computefields + type;
					}
				}
			});

			btn_addcomputein.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showExpressions(v);
				}

				private void showExpressions(final View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							TextInputActivity.this);
					builder.create();
					builder.setTitle("Expression");

					final CharSequence[] choiceList = { "+", "-", "*", "/" };

					builder.setItems(choiceList,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									changeExpression(
											choiceList[which].toString(), v);
									alert.cancel();
								}
							});
					alert = builder.create();
					alert.show();

				}

				private void changeExpression(String type, View v) {
					btn_addcomputein.setContentDescription(type);
					computefields = type;
				}
			});

			compfield.setText("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addFormField() {

		try {
			LayoutInflater layoutInflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(
					R.layout.form_fields, null);
			layout.setId(layout_container.getChildCount());

			layout_container.addView(layout);
			iview_remove.setVisibility(View.VISIBLE);
			scroll.post(new Runnable() {

				@Override
				public void run() {
					scroll.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void removeFormField() {
		try {
			layout_container.removeViewAt(layout_container.getChildCount() - 1);

			if (layout_container.getChildCount() == 1) {
				iview_remove.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void removeFormFields() {
		try {
			for (int i = 0; i < layout_container.getChildCount(); i++) {
				layout_container.removeViewAt(i);

				iview_remove.setVisibility(View.INVISIBLE);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void removecomputeFields() {
		try {
			for (int i = 0; i < computelayy.getChildCount(); i++) {
				computelayy.removeViewAt(i);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showSpinner(final View v) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					TextInputActivity.this);
			builder.create();
			builder.setTitle(SingleInstance.mainContext.getResources()
					.getString(R.string.field_type));
			final CharSequence[] choiceList = { "Drop Down", "Radio Button",
					"Date and Time", "Free Text", "Numeric", "MultiMedia",
					"Current Date", "Current Time", "Current Location", "Date",
					"Time", "Compute" };

			int selected = -1;

			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							changeFieldType(choiceList[which].toString(), v);
							edtxt_validda.setText("");
							defaultvalue.setText("");

							alert.cancel();
						}
					});
			alert = builder.create();
			alert.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void changeFieldType(String type, View v) {

		try {
			if (type.equalsIgnoreCase("Multimedia")
					|| type.equalsIgnoreCase("Current Date")
					|| type.equalsIgnoreCase("Current Time")
					|| type.equalsIgnoreCase("Current Location")
					|| type.equalsIgnoreCase("Date")
					|| type.equalsIgnoreCase("Time")) {

				edtxt_validda.setFocusableInTouchMode(false);

				defaultvalue.setFocusableInTouchMode(false);

				inst.setFocusableInTouchMode(false);

				errortip.setFocusableInTouchMode(false);

			} else {
				edtxt_validda.setFocusableInTouchMode(false);

				defaultvalue.setFocusableInTouchMode(false);

				inst.setFocusableInTouchMode(false);

				errortip.setFocusableInTouchMode(true);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showToast(String msg) {
		try {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private InputFilter[] getFilter() {
		try {
			InputFilter[] filters = new InputFilter[1];
			filters[0] = new InputFilter() {
				@Override
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {
					if (end > start) {

						char[] acceptedChars = new char[] { 'a', 'b', 'c', 'd',
								'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
								'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
								'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
								'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
								'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
								'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5',
								'6', '7', '8', '9', ' ' };

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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private void uploadConfiguredNote(String path) {
		try {
			if (CallDispatcher.LoginUser != null) {
				if (!path.contains(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/COMMedia/")) {
					path = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + "/COMMedia/" + path;
				}
				callDisp.uploadofflineResponse(path, false, 1, "forms");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);

			if ((requestCode == 100) && (resultCode == -10)) {
				try {
					Bundle bun = data.getBundleExtra("share");
					if (bun != null) {
						String path = bun.getString("filepath");
						if (!path.startsWith("null")) {
							if (inst.getText().toString().trim().length() > 0) {
								if (path.endsWith(",")) {
									path = path.substring(0, path.length() - 1);
								}
								inst.setText(inst.getText().toString().trim()
										+ "," + path);
							} else {
								if (path.endsWith(",")) {
									path = path.substring(0, path.length() - 1);

									inst.setText(path);
								} else {
									inst.setText(path);
								}
							}

							String[] instructionName = path.split(",");
							for (int i = 0; i < instructionName.length; i++) {
								uploadConfiguredNote(instructionName[i]);
							}
						}

					}

				} catch (NullPointerException e) {

				} catch (Exception e) {

				}
			}

			else if ((requestCode == 1) && (resultCode == -1)) {

				Bundle bun = data.getBundleExtra("share");
				if (bun != null) {
					String path = bun.getString("query");

					edtxt_validda.setText(path);
				}

			} else if ((requestCode == 2) && (resultCode == -2)) {

				Bundle bun = data.getBundleExtra("share");
				if (bun != null) {
					String path = bun.getString("hard");
					edtxt_validda.setText(path);
				}

			} else if ((requestCode == 11) && (resultCode == -11)) {

				Bundle bun = data.getBundleExtra("share");
				if (bun != null) {
					String path = bun.getString("numeric");
					numeric = bun.getString("dValue");
					numeric2 = bun.getString("dValue2");
					starttime = numeric;
					endtime = numeric2;
					edtxt_validda.setText(path);
				}

			} else if ((requestCode == 12) && (resultCode == -12)) {

				Bundle bun = data.getBundleExtra("share");
				if (bun != null) {
					String path = bun.getString("compute");
					edtxt_validda.setText(path);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showListDialog() {

		try {
			final CharSequence[] items = {
					SingleInstance.mainContext.getResources().getString(
							R.string.hard_coded_value),
					SingleInstance.mainContext.getResources().getString(
							R.string.query_based_value) };

			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle(SingleInstance.mainContext.getResources()
					.getString(R.string.get_valid_data));

			builder.setItems(items, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int item) {

					if (items[item].equals("Hard Coded Value")) {

						Intent i = new Intent(context, HardCodeQuery.class);
						i.putExtra("isRadio", false);
						startActivityForResult(i, 2);

					} else if (items[item].equals("Query Based Value")) {

						Intent i = new Intent(context, Query.class);
						startActivityForResult(i, 1);
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

	/** Displays a notification when the Time is updated */
	private void displayToast() {
		try {
			edtxt_validda.setText(timeD);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/** Updates the Time in the TextView */
	private void updateDisplayTime() {

		try {
			timeD = pad(pHour) + ":" + pad(pMinute);
			edtxt_validda.setText(timeD);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String pad(int a) {

		try {
			if (a >= 10) {
				return String.valueOf(a);

			} else {

				return "0" + String.valueOf(a);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/** Callback received when the user "picks" a Time in the dialog */
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			try {
				pHour = hourOfDay;
				pMinute = minute;
				updateDisplayTime();
				displayToast();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private void updateDisplay() {
		try {
			int month = mMonth + 1;
			dateD = month + "-" + mDay + "-" + mYear;
			edtxt_validda.setText(dateD);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			try {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				updateDisplay();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		try {
			switch (id) {
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, mDateSetListener, mYear,
						mMonth, mDay);
			case TIME_DIALOG_ID:
				return new TimePickerDialog(this, mTimeSetListener, pHour,
						pMinute, true);
			}

			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public boolean validateCheck() {

		try {
			if (entry.equalsIgnoreCase("Free Text")) {

				if (edtxt_validda.getText().toString()
						.equalsIgnoreCase("EMAIL")) {
					defaultvalue.setFocusableInTouchMode(true);
					defaultvalue
							.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

					if (!validateEmail(defaultvalue.getText().toString().trim())) {
						showToast("Please enter valid Email address");
						return false;
					}
				} else if (edtxt_validda.getText().toString()
						.contains("MINIMUM")) {
					defaultvalue.setFocusableInTouchMode(true);
					if (defaultvalue.getText().toString().trim().length() < Integer
							.parseInt(userInput.getText().toString())) {
						showToast("Text must be Minimum "
								+ userInput.getText().toString()
								+ " characters");
						return false;
					}

				} else if (edtxt_validda.getText().toString()
						.contains("MAXIMUM")) {
					defaultvalue.setFocusableInTouchMode(true);
					if (defaultvalue.getText().toString().trim().length() > Integer
							.parseInt(userInput.getText().toString())) {
						showToast("Text allowed Maximum "
								+ userInput.getText().toString()
								+ " characters");
						return false;

					}

				}

				else if (edtxt_validda.getText().toString()
						.equalsIgnoreCase("NO SPECIAL CHAR")) {
					defaultvalue.setFocusableInTouchMode(true);
					if (!nospecialchar(defaultvalue.getText().toString().trim())) {
						showToast(SingleInstance.mainContext.getResources()
								.getString(
										R.string.no_special_characters_allowed));
						return false;

					}
				} else if (edtxt_validda.getText().toString()
						.equalsIgnoreCase("BEGINS WITH ALPHA")) {
					defaultvalue.setFocusableInTouchMode(true);
					if (!beginalpha(defaultvalue.getText().toString().trim())) {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.begin_with_alpha));
						return false;
					}

				} else if (entry.equalsIgnoreCase("numeric")) {

					if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("LT " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);
						defaultvalue.setInputType(InputType.TYPE_CLASS_NUMBER);
						showToast("Between " + between);
						showToast("Number " + number);
						if (!(between < number)) {
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.enter_less_than_values));
							return false;
						}
					}

					else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("LE " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);

						if (!(between <= number)) {
							showToast(SingleInstance.mainContext
									.getResources()
									.getString(
											R.string.enter_less_than_equal_values));
							return false;
						}

					} else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("GT " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);

						if (!(between > number)) {
							showToast(SingleInstance.mainContext.getResources()
									.getString(
											R.string.enter_greater_than_values));
							return false;

						}
					}

					else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("GE " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);

						if (!(between >= number)) {
							showToast(SingleInstance.mainContext
									.getResources()
									.getString(
											R.string.enter_greater_than_equal_values));
							return false;

						}
					}

					else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("EQ " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);

						if (!(between == number)) {
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.enter_equal_values));
							return false;

						}
					} else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("NEQ " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);

						if (!(between != number)) {
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.enter_non_equal_values));
							return false;

						}
					} else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("Accuracy " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);
						defaultvalue
								.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

						defaultvalue
								.setFilters(new InputFilter[] { new DigitsKeyListener(
										Boolean.FALSE, Boolean.TRUE) {
									int beforeDecimal = 9;

									@Override
									public CharSequence filter(
											CharSequence source, int start,
											int end, Spanned dest, int dstart,
											int dend) {
										String temp = defaultvalue.getText()
												+ source.toString();

										if (temp.equals(".")) {
											return "0.";
										} else if (temp.toString().indexOf(".") == -1) {

											if (temp.length() > beforeDecimal) {
												return "";
											}
										} else {
											temp = temp.substring(temp
													.indexOf(".") + 1);
											if (temp.length() > number) {
												return "";
											}
										}

										return super.filter(source, start, end,
												dest, dstart, dend);
									}
								} });
					}

					else if (edtxt_validda.getText().toString()
							.equalsIgnoreCase("BT " + numeric)) {
						defaultvalue.setFocusableInTouchMode(true);

						if (between > startnum && between < endnum) {
							return true;
						} else {
							showToast("Please Enter the Between Values from Valid Data");
							return false;
						}
					}
				}

			}

			if (!edtxt.getText().toString().trim().equalsIgnoreCase("tableid")
					|| !edtxt.getText().toString().trim()
							.equalsIgnoreCase("id")
					|| !edtxt.getText().toString().trim()
							.equalsIgnoreCase("uuid")
					|| !edtxt.getText().toString().trim()
							.equalsIgnoreCase("uudate")
					|| !edtxt.getText().toString().trim()
							.equalsIgnoreCase("recorddate")
					|| !edtxt.getText().toString().trim()
							.equalsIgnoreCase("status")) {
				Pattern ptrn = Pattern.compile("[a-zA-Z]");
				String value = edtxt.getText().toString().trim()
						.substring(0, 1);
				Matcher m = ptrn.matcher(value);
				if (m.find()) {
					if (entry.equalsIgnoreCase("Free Text")) {
						recordname = edtxt.getText().toString().trim();

					} else if (entry.equalsIgnoreCase("numeric")) {
						recordname = edtxt.getText().toString().trim();

					} else if (entry.equalsIgnoreCase("Multimedia")) {
						recordname = "blob_"
								+ edtxt.getText().toString().trim();

					} else {
						recordname = edtxt.getText().toString().trim();

					}

				} else {
					showToast("Field names should not starts with Numeric letters");
				}
			} else {
				showToast("Kindly Don't use tableid,id,uuid,uudate,status as Form fields,that  are reserved");
			}
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
}
