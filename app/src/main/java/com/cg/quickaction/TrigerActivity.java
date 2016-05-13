package com.cg.quickaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.forms.FormQuery;
import com.cg.forms.QuerySelect;
import com.cg.forms.QuerySyntax.Condition;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class TrigerActivity extends Activity implements OnClickListener {

	Context context;
	TextView tv_title, tv_username, tv_scheduletask;

	Button btn_cancel, btn_next, btn_and, btn_or, btn_notification;
	public Button IMRequest;
	ImageView ivsetting, qa_delete;
	String F_Type;
	LinearLayout mainLayout, bindLayout = null;
	LayoutInflater inflater = null;
	LinearLayout linear_layout, lLayout1, lLayout2 = null;
	Spinner bindSpinner = null;
	Boolean isstring = false;
	AlertDialog alert = null;
	ArrayList<String> columns = new ArrayList<String>();
	String[] GETcolumn = null;
	Set<String> tableNames = new HashSet<String>();
	ArrayList<String> tablenames = new ArrayList<String>();
	ArrayList<String> spinner_values = null;
	QuerySelect querySelect = null;
	Handler handler = null;

	ArrayList<String> tableTypes = new ArrayList<String>();
	String query = null;
	private ContactLogicbean beanObj;
	HashMap<String, String> columnListMap = new HashMap<String, String>();
	FormQuery obj = null;
	boolean isWhereChecked = false;
	String TableNames = null;
	String WH_fields, WH_cond, WH_value = null;
	CallDispatcher callDisp;
	private Handler viewHandler = new Handler();
	public String owner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.trig_aggregate);

		if (WebServiceReferences.contextTable.containsKey("TriggerMain")) {
			TriggerMain frm_activity = (TriggerMain) WebServiceReferences.contextTable
					.get("TriggerMain");
			querySelect = frm_activity.querySelect;
			obj = frm_activity.obj;
			frm_activity.finish();

		}
		WebServiceReferences.contextTable.put("TriggerActivity", this);
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
		spinner_values = new ArrayList<String>();
		handler = new Handler();
		columns = getIntent().getExtras().getStringArrayList("columns");
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		beanObj = new ContactLogicbean();
		beanObj.setFromuser(CallDispatcher.LoginUser);
		TableNames = getIntent().getExtras().getString("tablename");
		tablenames = new ArrayList<String>(callDisp
				.getdbHeler(context)
				.getColumnNamesQueryMap(
						TableNames.replace("[", "").replace("]", "")).keySet());

		GETcolumn = tablenames.toArray(new String[tablenames.size()]);
		tableTypes = callDisp.getdbHeler(context).getColumnTypesTblQuery(
				TableNames.replace("[", "").replace("]", ""));

		columnListMap = callDisp.getdbHeler(context).getColumnNamesQueryMap(
				TableNames.replace("[", "").replace("]", ""));

		ivsetting = (ImageView) findViewById(R.id.iv_setting);
		ivsetting.setOnClickListener(this);

		mainLayout = (LinearLayout) findViewById(R.id.QueryBind);
		bindLayout = (LinearLayout) findViewById(R.id.wherecolumn);

		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		linear_layout = (LinearLayout) findViewById(R.id.inf);
		qa_delete = (ImageView) findViewById(R.id.qa_delete);
		qa_delete.setOnClickListener(this);

		btn_notification = (Button) findViewById(R.id.notification);
		btn_notification.setVisibility(View.GONE);
		IMRequest = (Button) findViewById(R.id.im);
		IMRequest.setVisibility(View.INVISIBLE);
		IMRequest.setBackgroundResource(R.drawable.one);

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == btn_cancel.getId()) {
			finish();
		} else if (v.getId() == btn_next.getId()) {

			if (isWhereChecked) {
				if (ValidateForm()) {
					Log.i("QAA",
							"final Query :: " + querySelect.getCompleteSQL());
					if (WebServiceReferences.contextTable
							.containsKey("QuickActionSettingcalls")) {
						QuickActionSettingcalls frm_activity = (QuickActionSettingcalls) WebServiceReferences.contextTable
								.get("QuickActionSettingcalls");
						frm_activity.changeQuery(querySelect.getCompleteSQL());

						finish();

					}
					if (WebServiceReferences.contextTable
							.containsKey("QuickActionTitlecalls")) {
						QuickActionTitlecalls frm_activity = (QuickActionTitlecalls) WebServiceReferences.contextTable
								.get("QuickActionTitlecalls");
						frm_activity.changeQuery(querySelect.getCompleteSQL());

						finish();

					}

					else {
						if (WebServiceReferences.contextTable
								.containsKey("QuickActionSettingcalls")) {
							QuickActionSettingcalls frm_activity = (QuickActionSettingcalls) WebServiceReferences.contextTable
									.get("QuickActionSettingcalls");
							frm_activity.changeQuery(querySelect
									.getCompleteSQL());
							finish();

						}
						if (WebServiceReferences.contextTable
								.containsKey("QuickActionTitlecalls")) {
							QuickActionTitlecalls frm_activity = (QuickActionTitlecalls) WebServiceReferences.contextTable
									.get("QuickActionTitlecalls");
							frm_activity.changeQuery(querySelect
									.getCompleteSQL());

							finish();

						}

					}

				} else {
					Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.required_fields),
							Toast.LENGTH_LONG).show();
				}
			} else {
				Log.i("QAA", "final Query :: " + querySelect.getCompleteSQL());
				if (WebServiceReferences.contextTable
						.containsKey("QuickActionSettingcalls")) {
					QuickActionSettingcalls frm_activity = (QuickActionSettingcalls) WebServiceReferences.contextTable
							.get("QuickActionSettingcalls");
					frm_activity.changeQuery(querySelect.getCompleteSQL());
					finish();

				}
				if (WebServiceReferences.contextTable
						.containsKey("QuickActionTitlecalls")) {
					QuickActionTitlecalls frm_activity = (QuickActionTitlecalls) WebServiceReferences.contextTable
							.get("QuickActionTitlecalls");
					frm_activity.changeQuery(querySelect.getCompleteSQL());

					finish();

				}

				else {
					if (WebServiceReferences.contextTable
							.containsKey("QuickActionSettingcalls")) {
						QuickActionSettingcalls frm_activity = (QuickActionSettingcalls) WebServiceReferences.contextTable
								.get("QuickActionSettingcalls");
						frm_activity.changeQuery(querySelect.getCompleteSQL());
						finish();

					}
					if (WebServiceReferences.contextTable
							.containsKey("QuickActionTitlecalls")) {
						QuickActionTitlecalls frm_activity = (QuickActionTitlecalls) WebServiceReferences.contextTable
								.get("QuickActionTitlecalls");
						frm_activity.changeQuery(querySelect.getCompleteSQL());

						finish();

					}
				}
			}

		} else if (v.getId() == ivsetting.getId()) {

			isWhereChecked = true;

			addFormField();

		} else if (v.getId() == qa_delete.getId()) {
			// removeField();
		}
		else {
			Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.required_fields),
					Toast.LENGTH_LONG).show();
		}
	}

	void showSingleSelectBuddy(final String[] choiceList, final String edit,
			final TextView inf_column, final EditText inf_values1) {

		try

		{

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.create();
			if (edit.equalsIgnoreCase("infcol")) {
				builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.column));
			} else {
				builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.values));
			}

			// System.out.println(choiceList);

			int selected = -1; // does not select anything

			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								final int which) {

							if (edit.equalsIgnoreCase("infcol")) {
								handler.post(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										inf_column.setText(choiceList[which]
												.toString());

										F_Type = columnListMap
												.get(choiceList[which]
														.toString());
									}
								});

							}

							else if (edit.equalsIgnoreCase("valuescol")) {
								handler.post(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub

										inf_values1.setText(choiceList[which]
												.toString());

									}
								});
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

	protected void ShowInteger(final View v, final TextView inf_condition) {

		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(
				TrigerActivity.this);
		builder.create();
		builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.integer));
		final CharSequence[] choiceList = { "<", "<=", ">", ">=", "==", "!=" };
		// System.out.println(choiceList);

		int selected = -1; // does not select anything

		builder.setSingleChoiceItems(choiceList, selected,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						changeType(choiceList[which].toString(), v,
								inf_condition);
						alert.cancel();
					}
				});
		alert = builder.create();
		alert.show();

	}

	private void changeType(String type, View v, TextView inf_condition) {

		inf_condition.setText(type);
	}

	protected void ShowVarchar(final View v) {

		try {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(
					TrigerActivity.this);
			builder.create();
			builder.setTitle("Varchar");
			final CharSequence[] choiceList = { "BEGINS WITH", "ENDS WITH",
					"CONTAINS", "EQUALS" };
			// System.out.println(choiceList);

			int selected = -1; // does not select anything

			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							changeVarchar(choiceList[which].toString(), v);
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

	private void changeVarchar(String type, View v) {
		try {
			TextView textView = (TextView) v.findViewById(R.id.condition);
			textView.setText(type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean ValidateForm() {
		try {
			Log.i("thread", "camr yo validateform");
			boolean valid = true;

			for (int j = 0; j < linear_layout.getChildCount(); j++) {

				LinearLayout layout = (LinearLayout) linear_layout
						.getChildAt(j);

				TextView fields = (TextView) layout
						.findViewById(R.id.columnane);
				TextView condition = (TextView) layout
						.findViewById(R.id.condition);
				EditText value = (EditText) layout.findViewById(R.id.value);
				Spinner spin_values = (Spinner) layout
						.findViewById(R.id.but_and);

				if (!spin_values.getSelectedItem().toString()
						.equalsIgnoreCase("BIND")) {

					spinner_values
							.add(spin_values.getSelectedItem().toString());

				}
				// if
				// (fields.getText().toString()!=null&&condition.getText().toString()!=null&&value.getText().toString()!=null&&spin_values.getSelectedItem().toString()!=null)
				// {

				if (j == 0) {
					String con = null;
					String value1 = null;
					String symbol = null;
					String[] condtion = { "<", "<=", ">", ">=", "==", "!=",
							"BEGINS WITH", "ENDS WITH", "CONTAINS", "EQUALS" };
					if ((condition.getText().toString() != null)
							&& (fields.getText().toString() != null)
							&& (value.getText().toString() != null)) {
						for (int i = 0; i < condtion.length; i++) {
							if (condition.getText().toString()
									.equalsIgnoreCase(condtion[i])) {
								symbol = condtion[i];

								con = "like";
								String[] fiel = fields.getText().toString()
										.replace("[", "").replace("]", "")
										.split("\\.");

								String LHSFormName = fiel[0];
								String LHSColumnName = fiel[1];
								if (condtion[i].equalsIgnoreCase("BEGINS WITH")) {

									value1 = value.getText().toString() + "%";
									if (value1.length() > 0) {
										if (value1.replace("%", "").matches(
												".*\\.\\[.*\\]")) {

											String[] values = value1
													.replace("[", "")
													.replace("]", "")
													.replace("%", "")
													.split("\\.");

											String RHSFormName = values[0];
											String RHSColumnName = values[1];

											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
															+ RHSFormName
															+ "colnamw:"
															+ RHSColumnName);

											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
															+ LHSFormName
															+ "colnamw:"
															+ LHSColumnName);
											querySelect
													.addINNERJoin(
															querySelect
																	.getForm(LHSFormName),
															LHSColumnName,
															querySelect
																	.getForm(RHSFormName),
															RHSColumnName);

										} else {

											Condition con1 = obj.getCondition(
													"", LHSColumnName, con,
													value1);
											obj.addCondition(con1);
										}
									} else {
										Toast.makeText(context,
												SingleInstance.mainContext.getResources().getString(R.string.required_fields),
												Toast.LENGTH_LONG).show();
										valid = false;
									}

								} else if (condtion[i]
										.equalsIgnoreCase("ENDS WITH")) {

									value1 = "%" + value.getText().toString();
									if (value1.length() > 0) {
										if (value1.replace("%", "").matches(
												".*\\.\\[.*\\]")) {
											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][]=====>");

											String[] values = value1
													.replace("[", "")
													.replace("]", "")
													.replace("%", "")
													.split("\\.");

											String RHSFormName = values[0];
											String RHSColumnName = values[1];

											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
															+ RHSFormName
															+ "colnamw:"
															+ RHSColumnName);

											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
															+ LHSFormName
															+ "colnamw:"
															+ LHSColumnName);
											if (querySelect
													.getForm(LHSFormName) != null
													&& querySelect
															.getForm(RHSFormName) != null) {
												querySelect
														.addINNERJoin(
																querySelect
																		.getForm(LHSFormName),
																LHSColumnName,
																querySelect
																		.getForm(RHSFormName),
																RHSColumnName);
											} else {
												Toast.makeText(context,
														SingleInstance.mainContext.getResources().getString(R.string.forms_has_no_entries),
														Toast.LENGTH_LONG)
														.show();
											}

										} else {
											Condition con1 = obj.getCondition(
													"", LHSColumnName, con,
													value1);
											obj.addCondition(con1);
										}
									} else {
										Toast.makeText(context,
												SingleInstance.mainContext.getResources().getString(R.string.required_fields),
												Toast.LENGTH_LONG).show();
										valid = false;
									}

								} else if (condtion[i]
										.equalsIgnoreCase("CONTAINS")) {

									value1 = "%" + value.getText().toString()
											+ "%";
									if (value1.length() > 0) {
										if (value1.replace("%", "").matches(
												".*\\.\\[.*\\]")) {
											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][]=====>");

											String[] values = value1
													.replace("[", "")
													.replace("]", "")
													.replace("%", "")
													.split("\\.");

											String RHSFormName = values[0];
											String RHSColumnName = values[1];
											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
															+ RHSFormName
															+ "colnamw:"
															+ RHSColumnName);

											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
															+ LHSFormName
															+ "colnamw:"
															+ LHSColumnName);

											querySelect
													.addINNERJoin(
															querySelect
																	.getForm(LHSFormName),
															LHSColumnName,
															querySelect
																	.getForm(RHSFormName),
															RHSColumnName);

										} else {
											Condition con1 = obj.getCondition(
													"", LHSColumnName, con,
													value1);
											obj.addCondition(con1);
										}

									} else {
										Toast.makeText(context,
												SingleInstance.mainContext.getResources().getString(R.string.required_fields),
												Toast.LENGTH_LONG).show();
										valid = false;
									}

								} else if (condtion[i]
										.equalsIgnoreCase("EQUALS")) {

									value1 = value.getText().toString();
									if (value1.length() > 0) {
										if (value1.replace("%", "").matches(
												".*\\.\\[.*\\]")) {
											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][]=====>");

											String[] values = value1
													.replace("[", "")
													.replace("]", "")
													.replace("%", "")
													.split("\\.");

											String RHSFormName = values[0];
											String RHSColumnName = values[1];

											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
															+ RHSFormName
															+ "colnamw:"
															+ RHSColumnName);

											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
															+ LHSFormName
															+ "colnamw:"
															+ LHSColumnName);

											querySelect
													.addINNERJoin(
															querySelect
																	.getForm(LHSFormName),
															LHSColumnName,
															querySelect
																	.getForm(RHSFormName),
															RHSColumnName);

										} else {
											Condition con1 = obj.getCondition(
													"", LHSColumnName, "=",
													value1);

											obj.addCondition(con1);

										}
									} else {
										Toast.makeText(context,
												SingleInstance.mainContext.getResources().getString(R.string.required_fields),
												Toast.LENGTH_LONG).show();
										valid = false;
									}
								}

								else {
									value1 = value.getText().toString();
									if (value1.length() > 0) {
										if (value1.matches(".*\\.\\[.*\\]")) {
											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][]=====>");

											String[] values = value1
													.replace("[", "")
													.replace("]", "")
													.split("\\.");

											String RHSFormName = values[0];
											String RHSColumnName = values[1];

											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
															+ RHSFormName
															+ "colnamw:"
															+ RHSColumnName);

											Log.i("welcome",
													"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
															+ LHSFormName
															+ "colnamw:"
															+ LHSColumnName);
											querySelect
													.addINNERJoin(
															querySelect
																	.getForm(LHSFormName),
															LHSColumnName,
															querySelect
																	.getForm(RHSFormName),
															RHSColumnName);

										} else {
											int number_value = Integer
													.parseInt(value1);
											Condition con1 = obj.getCondition(
													"", LHSColumnName, symbol,
													new Integer(number_value),
													"INT");
											obj.addCondition(con1);

										}
									} else {
										Toast.makeText(context,
												SingleInstance.mainContext.getResources().getString(R.string.required_fields),
												Toast.LENGTH_LONG).show();
										valid = false;
									}
								}
							}

						}
					} else {
						valid = false;
					}

				}

				else {
					if (fields.getText().toString().length() > 0
							&& value.getText().toString().length() > 0
							&& spinner_values.get(0).toString().length() > 0) {
						if ((condition.getText().toString() != null && !condition
								.getText().toString().equals(""))
								&& (fields.getText().toString() != null && !fields
										.getText().toString().equals(""))
								&& (value.getText().toString() != null && !fields
										.getText().toString().equals(""))
								&& (spinner_values.get(0).toString() != null && !fields
										.getText().toString().equals(""))) {

							String[] fiel = fields.getText().toString()
									.replace("[", "").replace("]", "")
									.split("\\.");

							String LHSFormName = fiel[0];
							String LHSColumnName = fiel[1];
							String con = null;
							String value1 = null;
							String symbol = null;
							String[] condtion = { "<", "<=", ">", ">=", "==",
									"!=", "BEGINS WITH", "ENDS WITH",
									"CONTAINS", "EQUALS" };

							for (int i = 0; i < condtion.length; i++) {
								if (condition.getText().toString()
										.equalsIgnoreCase(condtion[i])) {
									symbol = condtion[i];

									con = "like";

									if (condtion[i]
											.equalsIgnoreCase("BEGINS WITH")) {

										value1 = value.getText().toString()
												+ "%";
										if (value1.length() > 0) {
											if (value1.replace("%", "")
													.matches(".*\\.\\[.*\\]")) {

												String[] values = value1
														.replace("[", "")
														.replace("]", "")
														.replace("%", "")
														.split("\\.");

												String RHSFormName = values[0];
												String RHSColumnName = values[1];

												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
																+ RHSFormName
																+ "colnamw:"
																+ RHSColumnName);

												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
																+ LHSFormName
																+ "colnamw:"
																+ LHSColumnName);
												querySelect
														.addINNERJoin(
																querySelect
																		.getForm(LHSFormName),
																LHSColumnName,
																querySelect
																		.getForm(RHSFormName),
																RHSColumnName);

											} else {
												Condition con1 = obj
														.getCondition(
																spinner_values
																		.get(0)
																		.toString(),
																LHSColumnName,
																con, value1);
												obj.addCondition(con1);
												spinner_values.remove(0);
											}
										} else {
											Toast.makeText(
													context,
													SingleInstance.mainContext.getResources().getString(R.string.required_fields),
													Toast.LENGTH_LONG).show();
											valid = false;
										}

									} else if (condtion[i]
											.equalsIgnoreCase("ENDS WITH")) {

										value1 = "%"
												+ value.getText().toString();
										if (value1.length() > 0) {
											if (value1.replace("%", "")
													.matches(".*\\.\\[.*\\]")) {
												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][]=====>");

												String[] values = value1
														.replace("[", "")
														.replace("]", "")
														.replace("%", "")
														.split("\\.");

												String RHSFormName = values[0];
												String RHSColumnName = values[1];

												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
																+ RHSFormName
																+ "colnamw:"
																+ RHSColumnName);

												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
																+ LHSFormName
																+ "colnamw:"
																+ LHSColumnName);
												querySelect
														.addINNERJoin(
																querySelect
																		.getForm(LHSFormName),
																LHSColumnName,
																querySelect
																		.getForm(RHSFormName),
																RHSColumnName);

											} else {
												Condition con1 = obj
														.getCondition(
																spinner_values
																		.get(0)
																		.toString(),
																LHSColumnName,
																con, value1);
												obj.addCondition(con1);
												spinner_values.remove(0);

											}
										} else {
											Toast.makeText(
													context,
													SingleInstance.mainContext.getResources().getString(R.string.required_fields),
													Toast.LENGTH_LONG).show();
											valid = false;
										}

									} else if (condtion[i]
											.equalsIgnoreCase("CONTAINS")) {

										value1 = "%"
												+ value.getText().toString()
												+ "%";
										if (value1.length() > 0) {
											if (value1.replace("%", "")
													.matches(".*\\.\\[.*\\]")) {
												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][]=====>");

												String[] values = value1
														.replace("[", "")
														.replace("]", "")
														.replace("%", "")
														.split("\\.");

												String RHSFormName = values[0];
												String RHSColumnName = values[1];
												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
																+ RHSFormName
																+ "colnamw:"
																+ RHSColumnName);

												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
																+ LHSFormName
																+ "colnamw:"
																+ LHSColumnName);

												querySelect
														.addINNERJoin(
																querySelect
																		.getForm(LHSFormName),
																LHSColumnName,
																querySelect
																		.getForm(RHSFormName),
																RHSColumnName);

											} else {
												Condition con1 = obj
														.getCondition(
																spinner_values
																		.get(0)
																		.toString(),
																LHSColumnName,
																con, value1);
												obj.addCondition(con1);
												spinner_values.remove(0);
											}
										} else {
											Toast.makeText(
													context,
													SingleInstance.mainContext.getResources().getString(R.string.required_fields),
													Toast.LENGTH_LONG).show();
											valid = false;
										}

									} else if (condtion[i]
											.equalsIgnoreCase("EQUALS")) {
										value1 = value.getText().toString();
										if (value1.length() > 0) {
											if (value1.replace("%", "")
													.matches(".*\\.\\[.*\\]")) {
												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][]=====>");

												String[] values = value1
														.replace("[", "")
														.replace("]", "")
														.replace("%", "")
														.split("\\.");

												String RHSFormName = values[0];
												String RHSColumnName = values[1];

												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
																+ RHSFormName
																+ "colnamw:"
																+ RHSColumnName);

												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
																+ LHSFormName
																+ "colnamw:"
																+ LHSColumnName);

												querySelect
														.addINNERJoin(
																querySelect
																		.getForm(LHSFormName),
																LHSColumnName,
																querySelect
																		.getForm(RHSFormName),
																RHSColumnName);

											} else {
												Condition con1 = obj
														.getCondition(
																spinner_values
																		.get(0)
																		.toString(),
																LHSColumnName,
																con, value1);
												obj.addCondition(con1);
												spinner_values.remove(0);

											}
										} else {
											Toast.makeText(
													context,
													SingleInstance.mainContext.getResources().getString(R.string.required_fields),
													Toast.LENGTH_LONG).show();
											valid = false;
										}
									}

									else {
										value1 = value.getText().toString();
										if (value1.length() > 0) {
											if (value1.matches(".*\\.\\[.*\\]")) {
												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][]=====>");

												String[] values = value1
														.replace("[", "")
														.replace("]", "")
														.split("\\.");

												String RHSFormName = values[0];
												String RHSColumnName = values[1];

												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
																+ RHSFormName
																+ "colnamw:"
																+ RHSColumnName);

												Log.i("welcome",
														"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
																+ LHSFormName
																+ "colnamw:"
																+ LHSColumnName);
												querySelect
														.addINNERJoin(
																querySelect
																		.getForm(LHSFormName),
																LHSColumnName,
																querySelect
																		.getForm(RHSFormName),
																RHSColumnName);

											} else {
												int number_value = Integer
														.parseInt(value1);
												Condition con1 = obj
														.getCondition(
																spinner_values
																		.get(0)
																		.toString(),
																LHSColumnName,
																symbol,
																new Integer(
																		number_value),
																"INT");
												obj.addCondition(con1);
												spinner_values.remove(0);

											}
										} else {
											Toast.makeText(
													context,
													SingleInstance.mainContext.getResources().getString(R.string.required_fields),
													Toast.LENGTH_LONG).show();
											valid = false;
										}
									}
								}

							}
						}
					} else {
						Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.please_enter_all_fields),
								Toast.LENGTH_LONG).show();
						valid = false;
					}
				}

			}
			return valid;
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return false;
		}

	}

	public void addFormField()

	{

		try {
			Log.d("welcome", "Coming Successfully");
			final LinearLayout tv = (LinearLayout) inflater.inflate(
					R.layout.bind, null);
			final LinearLayout container = (LinearLayout) tv
					.findViewById(R.id.container);

			ImageView delete = (ImageView) tv.findViewById(R.id.delete);

			final EditText inf_values1 = (EditText) tv.findViewById(R.id.value);
			final ImageView valuescol = (ImageView) tv
					.findViewById(R.id.wh_image_col);
			inf_values1.setText("");
			final TextView inf_column = (TextView) tv
					.findViewById(R.id.columnane);
			final TextView inf_condition = (TextView) tv
					.findViewById(R.id.condition);
			lLayout1 = (LinearLayout) tv.findViewById(R.id.linear_table1);
			lLayout2 = (LinearLayout) tv.findViewById(R.id.linear_table2);
			final Spinner binds = (Spinner) tv.findViewById(R.id.but_and);
			Log.i("welcome", "Valus-->" + inf_values1);
			Log.i("welcome", "Where columns-->" + inf_column);
			binds.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					if (inf_column.getText().toString().length() != 0
							&& !inf_column.getText().toString()
									.equalsIgnoreCase("Column")) {
						parent.getItemAtPosition(position).toString();

						String bind = parent.getItemAtPosition(position)
								.toString();
						Log.i("welcome", "Spinner values-->"
								+ parent.getItemAtPosition(position).toString());
						Log.i("welcome", "Spinner argus-->" + arg1.getParent());

						if (parent.getItemAtPosition(position).toString()
								.equalsIgnoreCase("AND")
								|| parent.getItemAtPosition(position)
										.toString().equalsIgnoreCase("OR")) {

							addFormField();
							((Spinner) arg1.getParent()).setClickable(false);
						} else  {

							binds.setSelection(0);
						}

					} else if (inf_column.getText().toString().length() == 0
							&& !inf_column.getText().toString()
							.equalsIgnoreCase("Column")) { {
						Toast.makeText(context,
						
								SingleInstance.mainContext.getResources().getString(R.string.kindly_select_previous_column), 1).show();
					}
				}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}

			});

			inf_condition.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("QAA", "Column====>" + inf_column.toString());
					if (inf_column.getText().toString().length() != 0
							&& !inf_column.getText().toString()
									.equalsIgnoreCase("Column")) {
						Log.i("QAA", "FTYPE====>" + F_Type);
						if (F_Type.equalsIgnoreCase("INTEGER")) {
							isstring = true;
							inf_values1
									.setInputType(InputType.TYPE_CLASS_NUMBER);
							ShowInteger(v, inf_condition);
						}

						else if (F_Type.equalsIgnoreCase("nvarchar(20)")) {
							inf_values1.setInputType(InputType.TYPE_CLASS_TEXT);
							ShowVarchar(v);
						}
					} else {

						Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.select_columns), 1)
								.show();

					}
				}

			});

			inf_column.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showSingleSelectBuddy(GETcolumn, "infcol", inf_column,
							inf_values1);
					Log.i("QAA", "GETColumn size=====>" + GETcolumn.length);

				}

			});
			lLayout1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showSingleSelectBuddy(GETcolumn, "infcol", inf_column,
							inf_values1);
					Log.i("QAA", "GETColumn size=====>" + GETcolumn.length);

				}
			});
			lLayout2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (inf_column.getText().toString().length() != 0
							&& !inf_column.getText().toString()
									.equalsIgnoreCase("Column")) {
						Log.i("QAA", "FTYPE====>" + F_Type);
						if (F_Type.equalsIgnoreCase("INTEGER")) {
							isstring = true;
							// setInputType(InputType.TYPE_CLASS_NUMBER);
							// setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
							// setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED)
							inf_values1
									.setInputType(InputType.TYPE_CLASS_NUMBER);
							ShowInteger(v, inf_condition);
						}

						else if (F_Type.equalsIgnoreCase("nvarchar(20)")) {
							inf_values1.setInputType(InputType.TYPE_CLASS_TEXT);
							ShowVarchar(v);
						}
					} else {

						Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.select_columns), 1)
								.show();

					}
				}

			});

			valuescol.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					showSingleSelectBuddy(GETcolumn, "valuescol", inf_column,
							inf_values1);
					Log.i("QAA", "GETColumn size=====>" + GETcolumn.length);
					Log.i("QAA", "GETColumn size=====>" + GETcolumn.length);

				}

			});

			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ViewGroup vg = (ViewGroup) (container.getParent());
					vg.removeView(container);
				}
			});

			tv.setId(linear_layout.getChildCount());
			linear_layout.addView(tv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("TriggerActivity");
		super.onDestroy();
	}
}
