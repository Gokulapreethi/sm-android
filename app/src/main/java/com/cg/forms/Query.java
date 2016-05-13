package com.cg.forms;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class Query extends Activity {
	/** Called when the activity is first created. */

	private EditText table, field, column, agregate,  conditions, wh_column,
			wh_condition, wh_values, inf_column, inf_condition,
			inf_values = null;
	private Button submit = null;
	private Button BUILDQUERY = null;
	private Boolean isstring = false;
	private ArrayList<String[]> rec_list = null;
	private String ss = null;
	private Context context = null;
	private String[] DBNames = null;
	private String[] GETcolumn = null;
	private LinearLayout aggregatelayout, wherelayout = null;
	private String tablename = null;
	private ArrayList<String> tablenames = null;
	private ArrayList<String> tableTypes = null;
	private ArrayList<String> field_values = null;
	private ArrayList<String> condition_values = null;
	private ArrayList<String> val_values = null;
	private ArrayList<String> spinner_values = null;
	private Boolean ANDOR = false;
	private ArrayList<String> db = null;
	private AlertDialog alert = null;
	private ImageView wh_assignvalue, inf_assignvalue = null;
	private LayoutInflater inflater = null;
	private LinearLayout tv;
	private LinearLayout linear_layout;
	private Spinner andor, infSpinner;
	private String FIELDS, AGGREGATION, WHERECOLUMN, WHCONDTITION, WHVALUES, INFCOLUMN,
			INFCONDTITION, INFVALUES = null;
	private StringBuffer sb, con, values = null;
	private String F_Type = null;
	private Boolean where = false;
	private int whindex;
	private String query = null;
	private CallDispatcher calldisp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = this;
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int noScrHeight = displaymetrics.heightPixels;
		int noScrWidth = displaymetrics.widthPixels;

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			calldisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			calldisp = new CallDispatcher(context);

		calldisp.setNoScrHeight(noScrHeight);
		calldisp.setNoScrWidth(noScrWidth);
		displaymetrics = null;
		table = (EditText) findViewById(R.id.tables);
		field = (EditText) findViewById(R.id.fields);
		agregate = (EditText) findViewById(R.id.aggregate);
		aggregatelayout = (LinearLayout) findViewById(R.id.aggregate_table);

		aggregatelayout.setVisibility(View.GONE);
		wherelayout = (LinearLayout) findViewById(R.id.wherecolumn);
		andor = (Spinner) findViewById(R.id.andor);
		// where colum
		wh_column = (EditText) findViewById(R.id.wh_columnname);
		wh_condition = (EditText) findViewById(R.id.wh_condition);
		wh_values = (EditText) findViewById(R.id.wh_value);
		wh_assignvalue = (ImageView) findViewById(R.id.wh_image_col);
		linear_layout = (LinearLayout) findViewById(R.id.inf);
		sb = new StringBuffer();
		con = new StringBuffer();
		values = new StringBuffer();
		submit = (Button) findViewById(R.id.submit);
		submit.setVisibility(View.GONE);
		BUILDQUERY = (Button) findViewById(R.id.build);

		table.setFocusableInTouchMode(false);
		field.setFocusableInTouchMode(false);
		tablenames = new ArrayList<String>();
		tableTypes = new ArrayList<String>();
		field_values = new ArrayList<String>();
		condition_values = new ArrayList<String>();

		val_values = new ArrayList<String>();

		spinner_values = new ArrayList<String>();

		db = new ArrayList<String>();
		db = calldisp.getdbHeler(context).getDBNames();

		DBNames = db.toArray(new String[db.size()]);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		tv = (LinearLayout) inflater.inflate(R.layout.text, null);
		column = (EditText) tv.findViewById(R.id.columnname);
		conditions = (EditText) tv.findViewById(R.id.condition);
		infSpinner = (Spinner) tv.findViewById(R.id.inf_andor);

		table.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showMultiselectBuddys(DBNames, "table");

			}
		});

		andor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				String val = parent.getItemAtPosition(position).toString();
				if (!val.equalsIgnoreCase("bind")) {

					// inflate layout
					if (WHERECOLUMN != null && WHCONDTITION != null
							&& wh_values.getText().toString() != null) {
						ANDOR = true;
						addFormField();

					} else {
						andor.setSelection(0);
						Toast.makeText(context,SingleInstance.mainContext.getResources().getString(R.string.kindly_enter_details), 1)
								.show();

					}

				} else {

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		wh_column.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tablename != null || tablename.length() != 0) {
					tablenames = calldisp.getdbHeler(context).getColumnNamesQuery(tablename);
					GETcolumn = tablenames.toArray(new String[tablenames.size()]);
					tableTypes = calldisp.getdbHeler(context).getColumnTypesTblQuery(tablename);

					showSingleSelectBuddy(GETcolumn, "whcol");

				} else {
					Toast.makeText(context,SingleInstance.mainContext.getResources().getString(R.string.kindly_select_table), 1).show();

				}

			}
		});

		wh_assignvalue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tablename != null || tablename.length() != 0) {
					tablenames = calldisp.getdbHeler(context).getColumnNamesQuery(tablename);
					tablenames.remove(whindex);
					GETcolumn = tablenames.toArray(new String[tablenames.size()]);
					// showMultiselectBuddys(GETcolumn,"whassign");
					showSingleSelectBuddy(GETcolumn, "whassign");

				} else {
					Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.kindly_select_table), 1).show();

				}

			}
		});

		field.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tablename != null) {

					showMultiselectBuddys(GETcolumn, "field");

				} else {
					Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.kindly_select_table), 1).show();

				}

			}
		});
		agregate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String[] aggregation = { "None", "Avg()", "Count()", "Max()",
						"Min()", "Sum()" };
				showSingleSelectBuddy(aggregation, "agr");

			}
		});

		wh_condition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (WHERECOLUMN != null) {
					if (F_Type.equalsIgnoreCase("INTEGER")) {
						isstring = false;

						String[] aggregation = { "<", "<=", ">", ">=", "==",
								"!=" };
						showSingleSelectBuddy(aggregation, "whcondition");
						wh_values.setInputType(InputType.TYPE_CLASS_NUMBER);

					} else if (F_Type.equalsIgnoreCase("nvarchar(20)")) {
						isstring = true;
						String[] aggregation = { "BEGINS WITH", "ENDS WITH",
								"CONTAINS", "EQUALS" };
						showSingleSelectBuddy(aggregation, "whcondition");
						wh_values.setInputType(InputType.TYPE_CLASS_TEXT);

					}

				} else {

					Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.kindly_select_column), 1).show();

				}

			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tablename != null && FIELDS != null) {
					wherelayout.setVisibility(View.VISIBLE);
					where = true;
				} else {
					showAlert("Please Enter Values");
					where = false;
				}
			}
		});
		WHVALUES = wh_values.getText().toString();

		BUILDQUERY.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tablename != null && FIELDS != null) {

					ValidateForm();

					if (AGGREGATION != null) {
						if (where && WHERECOLUMN != null
								&& WHCONDTITION != null && ANDOR) {

							sb.delete(0, sb.length());
							con.delete(0, con.length());
							String spinn = null;
							String[] multiagregation = FIELDS.split(",");
							if (multiagregation.length > 0) {
								for (int i = 0; i < multiagregation.length; i++) {
									String agr = AGGREGATION.replace("()", "");
									sb.append(agr + "(" + multiagregation[i]
											+ ")");
									sb.append(",");
								}

								ss = sb.toString().substring(0,
										sb.toString().length() - 1);
								String selection = andor.getSelectedItem()
										.toString();

								if (!isstring) {
									for (int i = 0; i < field_values.size(); i++) {
										if (spinner_values.get(i)
												.equalsIgnoreCase("BIND")) {
											spinn = "";
										} else {
											spinn = spinner_values.get(i);

										}

										con.append("(" + field_values.get(i)
												+ " " + condition_values.get(i)
												+ " " + val_values.get(i)
												+ ") " + spinn + " ");

									}
									String query = "SELECT DISTINCT " + ss
											+ " FROM " + tablename + " WHERE "
											+ WHERECOLUMN + " " + WHCONDTITION
											+ " "
											+ wh_values.getText().toString()
											+ " " + selection + " " + con;
									Log.i("i", "valuesssss" + WHCONDTITION);
									showAlert(query);
								} else {
									for (int i = 0; i < field_values.size(); i++) {
										if (spinner_values.get(i)
												.equalsIgnoreCase("BIND")) {
											spinn = "";
										} else {
											spinn = spinner_values.get(i);

										}

										con.append("("
												+ field_values.get(i)
												+ " "
												+ andorfunction(
														condition_values.get(i),
														val_values.get(i))
												+ ") " + spinn + " ");

									}
									isstring(ss, selection, con.toString());
								}

							}

						} else if (where && WHERECOLUMN != null
								&& WHCONDTITION != null) {

							sb.delete(0, sb.length());
							String[] multiagregation = FIELDS.split(",");
							if (multiagregation.length > 0) {
								for (int i = 0; i < multiagregation.length; i++) {
									String agr = AGGREGATION.replace("()", "");
									sb.append(agr + "(" + multiagregation[i]
											+ ")");
									sb.append(",");
								}

								ss = sb.toString().substring(0,
										sb.toString().length() - 1);

								if (!isstring) {

									String query = "SELECT DISTINCT " + ss
											+ " FROM " + tablename + " WHERE "
											+ WHERECOLUMN + " " + WHCONDTITION
											+ " "
											+ wh_values.getText().toString();
									Log.i("i", "valuesssss" + WHCONDTITION);
									showAlert(query);
								} else {

									isstring(ss, "", "");
								}

							}

						}

						else {
							sb.delete(0, sb.length());
							String[] multiagregation = FIELDS.split(",");
							if (multiagregation.length > 0) {
								for (int i = 0; i < multiagregation.length; i++) {
									String agr = AGGREGATION.replace("()", "");
									sb.append(agr + "(" + multiagregation[i]
											+ ")");
									sb.append(",");
								}

								ss = sb.toString().substring(0,
										sb.toString().length() - 1);

								String query = "SELECT DISTINCT " + ss
										+ " FROM " + tablename;
								showAlert(query);
							}
						}

					} else {
						if (where && WHERECOLUMN != null
								&& WHCONDTITION != null && ANDOR) {
							con.delete(0, con.length());

							String selection = andor.getSelectedItem()
									.toString();
							String spins = null;
							if (!isstring) {

								for (int i = 0; i < field_values.size(); i++) {
									if (spinner_values.get(i).equalsIgnoreCase(
											"BIND")) {
										spins = "";
									} else {
										spins = spinner_values.get(i);

									}
									con.append("(" + field_values.get(i) + " "
											+ condition_values.get(i) + " "
											+ val_values.get(i) + ") " + spins
											+ " ");

								}

								String query = "SELECT DISTINCT " + FIELDS
										+ " FROM " + tablename + " WHERE ("
										+ WHERECOLUMN + " " + WHCONDTITION
										+ " " + wh_values.getText().toString()
										+ ") " + selection + " " + con;
								showAlert(query);
							} else {

								for (int i = 0; i < field_values.size(); i++) {
									if (spinner_values.get(i).equalsIgnoreCase(
											"BIND")) {
										spins = "";
									} else {
										spins = spinner_values.get(i);

									}

									con.append("("
											+ field_values.get(i)
											+ " "
											+ andorfunction(
													condition_values.get(i),
													val_values.get(i)) + ") "
											+ spins + " ");

								}

								isstring(FIELDS, selection, con.toString());

							}

						} else if (where && WHERECOLUMN != null
								&& WHCONDTITION != null) {

							if (!isstring) {
								String query = "SELECT DISTINCT " + FIELDS
										+ " FROM " + tablename + " WHERE ("
										+ WHERECOLUMN + " " + WHCONDTITION
										+ " " + wh_values.getText().toString()
										+ ")";
								Log.i("i", "valuesssss" + WHCONDTITION);
								showAlert(query);

							}

							else {
								isstring(FIELDS, "", "");

							}

						}

						else {
							String query = "SELECT DISTINCT " + FIELDS
									+ " FROM " + tablename;
							showAlert(query);

						}

					}
				} else {
					showAlert(SingleInstance.mainContext.getResources().getString(R.string.please_enter_values));

				}

			}

		});

		column.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tablename != null || tablename.length() != 0) {
					tablenames = calldisp.getdbHeler(context).getColumnNamesQuery(tablename);
					GETcolumn = tablenames.toArray(new String[tablenames.size()]);
					showMultiselectBuddys(GETcolumn, "column");

				} else {
					Toast.makeText(context, "Kindly Select Table", 1).show();

				}

			}
		});

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    private String andorfunction(String condtion, String value) {

		// TODO Auto-generated method stub

		String querys = null;
		if (condtion.equalsIgnoreCase("equals")) {

			querys = "LIKE '%" + value + "%'";
		} else if (condtion.equalsIgnoreCase("contains")) {

			querys = "LIKE '%" + value + "%'";

		} else if (condtion.equalsIgnoreCase("begins with")) {

			querys = "LIKE '" + value + "%'";

		} else if (condtion.equalsIgnoreCase("ends with")) {

			querys = "LIKE '%" + value + "'";

		}

		return querys;

	}

	private void isstring(String fields, String selection, String con) {
		if (!ANDOR) {
			if (WHCONDTITION.equalsIgnoreCase("equals")) {
				String query = "SELECT DISTINCT " + fields + " FROM "
						+ tablename + " WHERE " + WHERECOLUMN + " " + "LIKE '%"
						+ wh_values.getText().toString() + "%'";
				showAlert(query);

			} else if (WHCONDTITION.equalsIgnoreCase("contains")) {
				String query = "SELECT DISTINCT " + fields + " FROM "
						+ tablename + " WHERE " + WHERECOLUMN + " " + "LIKE '%"
						+ wh_values.getText().toString() + "%'";
				showAlert(query);

			} else if (WHCONDTITION.equalsIgnoreCase("begins with")) {

				String query = "SELECT DISTINCT " + fields + " FROM "
						+ tablename + " WHERE " + WHERECOLUMN + " " + "LIKE '"
						+ wh_values.getText().toString() + "%'";
				showAlert(query);

			} else if (WHCONDTITION.equalsIgnoreCase("ends with")) {
				String query = "SELECT DISTINCT " + fields + " FROM "
						+ tablename + " WHERE " + WHERECOLUMN + " " + "LIKE '%"
						+ wh_values.getText().toString() + "'";
				showAlert(query);

			}
		} else {
			if (WHCONDTITION.equalsIgnoreCase("equals")) {
				String query = "SELECT DISTINCT " + fields + " FROM "
						+ tablename + " WHERE (" + WHERECOLUMN + " "
						+ "LIKE '%" + wh_values.getText().toString() + "%'"
						+ ") " + selection + " " + con;
				showAlert(query);

			} else if (WHCONDTITION.equalsIgnoreCase("contains")) {
				String query = "SELECT DISTINCT " + fields + " FROM "
						+ tablename + " WHERE (" + WHERECOLUMN + " "
						+ "LIKE '%" + wh_values.getText().toString() + "%'"
						+ ") " + selection + " " + con;

				showAlert(query);

			} else if (WHCONDTITION.equalsIgnoreCase("begins with")) {

				String query = "SELECT DISTINCT " + fields + " FROM "
						+ tablename + " WHERE (" + WHERECOLUMN + " " + "LIKE '"
						+ wh_values.getText().toString() + "%'" + ") "
						+ selection + " " + con;

				showAlert(query);

			} else if (WHCONDTITION.equalsIgnoreCase("ends with")) {
				String query = "SELECT DISTINCT " + fields + " FROM "
						+ tablename + " WHERE (" + WHERECOLUMN + " "
						+ "LIKE '%" + wh_values.getText().toString() + "'"
						+ ") " + selection + " " + con;

				showAlert(query);

			}

		}

	}

	void showSingleSelectBuddy(final String[] choiceList, final String edit) {
		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.create();
			

			int selected = -1; // does not select anything

			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							if (edit.equalsIgnoreCase("agr")) {
								if (choiceList[which].toString()
										.equalsIgnoreCase("none")) {
									agregate.setText("");
									AGGREGATION = null;

								} else {
									agregate.setText(choiceList[which]
											.toString());
									AGGREGATION = agregate.getText().toString();
								}

							} else if (edit.equalsIgnoreCase("BIND")) {

								if (choiceList[which].toString()
										.equalsIgnoreCase("AND")) {
									linear_layout.addView(tv);
								} else {

									linear_layout.addView(tv);
								}

							} else if (edit.equalsIgnoreCase("whcol")) {
								whindex = which;
								wh_column.setText(choiceList[which].toString());
								WHERECOLUMN = wh_column.getText().toString();
								F_Type = tableTypes.get(which).toString();

							}

							else if (edit.equalsIgnoreCase("condition")) {

								conditions.setText(choiceList[which].toString());

							} else if (edit.equalsIgnoreCase("infcol")) {
								whindex = which;

								inf_column.setText(choiceList[which].toString());
								INFCOLUMN = choiceList[which].toString();
								F_Type = tableTypes.get(which).toString();

							} else if (edit.equalsIgnoreCase("whcondition")) {

								wh_condition.setText(choiceList[which]
										.toString());
								WHCONDTITION = wh_condition.getText()
										.toString();
								Log.i("i", "valuesssss" + WHCONDTITION);

							} else if (edit.equalsIgnoreCase("whassign")) {

								wh_values.setText(choiceList[which].toString());

							} else if (edit.equalsIgnoreCase("infassign")) {

								inf_values.setText(choiceList[which].toString());
								INFVALUES = choiceList[which].toString();

							} else if (edit.equalsIgnoreCase("infcondition")) {

								inf_condition.setText(choiceList[which]
										.toString());
								INFCONDTITION = choiceList[which].toString();

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

	public void showMultiselectBuddys(final String[] elements, final String name) {
		AlertDialog.Builder d = new AlertDialog.Builder(context);
		d.create();

		int selected = -1; // does not select anything

		d.setSingleChoiceItems(elements, selected,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (name.equalsIgnoreCase("table")) {
							table.setText(elements[which].toString());
							tablename = elements[which].toString();
							if (tablename != null || tablename.length() != 0) {
								Log.i("welcome", "selected table name===>"+tablename);
								tablenames = calldisp.getdbHeler(context)
										.getColumnQueryForQuery(tablename);
								GETcolumn = tablenames
										.toArray(new String[tablenames.size()]);

							} else {
								Toast.makeText(context, "Kindly Select Table",
										1).show();

							}

						} else {

							if (name.equalsIgnoreCase("column")) {
								column.setText(elements[which].toString());

							}

							else {
								if (elements[which].toString()
										.equalsIgnoreCase("select all")) {
									field.setText("*");
									FIELDS = "*";

								} else {
									field.setText(elements[which].toString());
									FIELDS = elements[which].toString();

								}

							}

						}

						alert.dismiss();

					}
				});
		alert = d.create();
		if (elements != null) {
			if (elements.length != 0) {
				alert.show();
			}
		}
		
	}

	private void addFormField() {
		Log.d("welcome", "Coming Successfully");
		final LinearLayout tv = (LinearLayout) inflater.inflate(R.layout.text,
				null);
		inf_values = (EditText) tv.findViewById(R.id.value);
		inf_assignvalue = (ImageView) tv.findViewById(R.id.image_col);
		INFCOLUMN = null;
		INFCONDTITION = null;
		inf_values.setText("");
		inf_assignvalue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tablename != null || tablename.length() != 0) {
					tablenames = calldisp.getdbHeler(context).getColumnNamesQuery(tablename);
					tablenames.remove(whindex);
					GETcolumn = tablenames.toArray(new String[tablenames.size()]);
					showSingleSelectBuddy(GETcolumn, "infassign");

				} else {
					Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.kindly_select_table), 1).show();

				}

			}
		});
		infSpinner = (Spinner) tv.findViewById(R.id.inf_andor);
		inf_column = (EditText) tv.findViewById(R.id.columnname);
		inf_condition = (EditText) tv.findViewById(R.id.condition);
		inf_condition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (WHERECOLUMN != null) {
					if (F_Type.equalsIgnoreCase("INTEGER")) {
						isstring = true;

						String[] aggregation = { "<", "<=", ">", ">=", "==",
								"!=" };
						showSingleSelectBuddy(aggregation, "infcondition");
						inf_values.setInputType(InputType.TYPE_CLASS_NUMBER);
					} else if (F_Type.equalsIgnoreCase("nvarchar(20)")) {
						isstring = true;
						String[] aggregation = { "BEGINS WITH", "ENDS WITH",
								"CONTAINS", "EQUALS" };
						showSingleSelectBuddy(aggregation, "infcondition");
						inf_values.setInputType(InputType.TYPE_CLASS_TEXT);
					}

				} else {

					Toast.makeText(context,SingleInstance.mainContext.getResources().getString(R.string.kindly_select_column), 1).show();

				}

			}

		});
		inf_column.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tablename != null || tablename.length() != 0) {
					tablenames = calldisp.getdbHeler(context).getColumnNamesQuery(tablename);
					GETcolumn = tablenames.toArray(new String[tablenames.size()]);
					showSingleSelectBuddy(GETcolumn, "infcol");

				} else {
					Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.kindly_select_table), 1).show();

				}

			}
		});
		infSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				String val = parent.getItemAtPosition(position).toString();
				if (!val.equalsIgnoreCase("bind")) {
					// inflate layout

					if (INFCOLUMN != null && INFCONDTITION != null
							&& inf_values.getText().toString() != null) {

						addFormField();

					} else {
						infSpinner.setSelection(0);

						Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.kindly_enter_details), 1)
								.show();

					}

				} else {

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		tv.setId(linear_layout.getChildCount());
		linear_layout.addView(tv);

	}

	@SuppressWarnings("deprecation")
	private void showAlert(String message) {
		final AlertDialog alertDialog = new AlertDialog.Builder(Query.this)
				.create();

		alertDialog.setTitle("Result");

		alertDialog.setMessage(message);
		if (message != null) {
			if (message.startsWith("Please")) {

				query = "";
			} else {
				query = message;
			}
		}

		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				if (query.length() > 0) {
					rec_list = calldisp.getdbHeler(context)
							.isQueryContainResults(query, "");

					for (int i = 0; i < rec_list.size(); i++) {

						Log.i("ne",
								"rec list values $$$$$$$$$$ *"
										+ rec_list.get(i));
						String[] ff = rec_list.get(i);
						for (int j = 0; j < ff.length; j++) {
							Log.i("IMP", "rec list values $$$$$$$$$$ *" + ff[j]);
							values.append(ff[j]);
							values.append(",");

						}
						values.toString().substring(0,
								values.toString().length() - 1);

					}
			

					Intent i = new Intent();
					Bundle bun = new Bundle();
					bun.putString("query", "Q " + query);
					bun.putString("squery",  query);

					i.putExtra("share", bun);
					setResult(-1, i);
					alertDialog.dismiss();
					finish();
				} else {

					finish();
				}
			}
		});

		alertDialog.show();

	}

	private void ValidateForm() {
		Log.i("thread", "camr yo validateform");
		field_values.clear();
		for (int i = 0; i < linear_layout.getChildCount(); i++) {
			LinearLayout layout = (LinearLayout) linear_layout.getChildAt(i);
			EditText fields = (EditText) layout.findViewById(R.id.columnname);
			EditText condition = (EditText) layout.findViewById(R.id.condition);
			EditText valued = (EditText) layout.findViewById(R.id.value);
			Spinner spin_values = (Spinner) layout.findViewById(R.id.inf_andor);

			if (fields.getText().toString().trim().length() != 0) {
				field_values.add(fields.getText().toString().trim());
				Log.i("blob", "in side field checkingggg"
						+ fields.getText().toString().trim());

			}
			if (condition.getText().toString().trim().length() != 0) {
				condition_values.add(condition.getText().toString().trim());
				Log.i("blob", "in side field checkingggg"
						+ condition.getText().toString().trim());

			}
			if (valued.getText().toString().trim().length() != 0) {
				val_values.add(valued.getText().toString().trim());
				Log.i("blob", "in side field checkingggg"
						+ valued.getText().toString().trim());

			}
			if (spin_values.getSelectedItem().toString() != null) {
				spinner_values.add(spin_values.getSelectedItem().toString());
				Log.i("blob", "in side field checkingggg"
						+ spin_values.getSelectedItem().toString());

			}

		}
	}

	private void removeFormField() {
		linear_layout.removeViewAt(linear_layout.getChildCount() - 1);

	}

}