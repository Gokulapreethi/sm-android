package com.cg.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.forms.QuerySyntax.Column;
import com.cg.forms.QuerySyntax.Condition;
import com.main.AppMainActivity;
import com.util.SingleInstance;



public class QueryActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private EditText table, field, column, conditions, wh_column,
			wh_condition, wh_values, inf_column, inf_condition,
			inf_values = null;
	private Button BUILDQUERY = null;
	private String selNonAggFld = null;
	private Context context = null;
	private String[] DBNames = null;
	private String[] GETcolumn = null;
	public static String tablename = null;
	private ArrayList<String> tablenames = null;
	private ArrayList<String> tableTypes = null;
	private ArrayList<String> field_values = null;
	private ArrayList<String> nonAggFldList = null;
	private String isGroupby = null;
	private Map<String, String> map = new HashMap<String, String>();
	private ArrayList<String> aggFldList = null;
	private ArrayList<String> columnName = null;
	private ArrayList<String> columnType = null;
	private ArrayList<String> spinner_values = null;
	boolean iswhereischecked = false;
	private QuerySelect querySelect = null;
	private HashMap<String, String> columnListMap = new HashMap<String, String>();
	private FormQuery obj = null;
	private ArrayList<String> db = null;
	private AlertDialog alert = null;
	private ImageView inf_assignvalue = null;
	private LayoutInflater inflater = null;
	private LinearLayout tv;
	private LinearLayout linear_layout;
	private Spinner infSpinner;
	private String TABLENAME, FIELDS, WHERECOLUMN, WHCONDTITION, INFCOLUMN;
	private String F_Type = null;
	private int whindex;
	private String query = null;
	private String Action = null;
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
		table.setOnClickListener(this);
		field = (EditText) findViewById(R.id.fields);
		field.setOnClickListener(this);
		wh_column = (EditText) findViewById(R.id.wh_columnname);
		wh_condition = (EditText) findViewById(R.id.wh_condition);
		wh_values = (EditText) findViewById(R.id.wh_value);
		linear_layout = (LinearLayout) findViewById(R.id.inf);
		new StringBuffer();
		new StringBuffer();
		WebServiceReferences.contextTable.put("QueryActivity", this);


		nonAggFldList = new ArrayList<String>();
		aggFldList = new ArrayList<String>();
		columnName = new ArrayList<String>();
		columnType = new ArrayList<String>();

		BUILDQUERY = (Button) findViewById(R.id.build);
		BUILDQUERY.setOnClickListener(this);
		Action = getIntent().getStringExtra("action");
		table.setFocusableInTouchMode(false);
		field.setFocusableInTouchMode(false);
		tablenames = new ArrayList<String>();
		tableTypes = new ArrayList<String>();
		field_values = new ArrayList<String>();
		new ArrayList<String>();

		new ArrayList<String>();

		spinner_values = new ArrayList<String>();

		db = new ArrayList<String>();
		db = calldisp.getdbHeler(context).getDBNames();
		db.add("Select All");
		DBNames = db.toArray(new String[db.size()]);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		tv = (LinearLayout) inflater.inflate(R.layout.text, null);
		column = (EditText) tv.findViewById(R.id.columnname);
		conditions = (EditText) tv.findViewById(R.id.condition);
		infSpinner = (Spinner) tv.findViewById(R.id.inf_andor);

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    @Override
	protected void onDestroy() {
		
		WebServiceReferences.contextTable.remove("QueryActivity");
		super.onDestroy();
	}

	public void showMultiselectBuddys(final String[] elements, final String name) {

		AlertDialog.Builder d = new AlertDialog.Builder(context);

		d.setMultiChoiceItems(elements, null, new OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position,
					boolean isChecked) {
				if (isChecked) {
				}
			}
		});

		d.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				ListView list = ((AlertDialog) dialog).getListView();

				StringBuilder sb = new StringBuilder();

				for (int i = 0; i < list.getCount(); i++) {
					boolean checked = list.isItemChecked(i);

					if (checked) {

						if (sb.length() > 0)
							sb.append(",");
						sb.append(list.getItemAtPosition(i));
					}
				}

				if (name.equalsIgnoreCase("table")) {
					table.setText(sb.toString());
					tablename = sb.toString();

					if (sb.toString().equalsIgnoreCase("select all")) {
						StringBuilder sb1 = new StringBuilder();

						for (int i = 0; i < elements.length - 1; i++) {
							if (sb1.length() > 0)
								sb1.append(",");
							sb1.append(elements[i]);
						}
						table.setText(sb1.toString());
						tablename = sb1.toString();
					}

					if (tablename != null || tablename.length() != 0) {
						tablenames = new ArrayList<String>(calldisp.getdbHeler(context)
								.getColumnNamesQueryMap(tablename).keySet());
						columnListMap = calldisp.getdbHeler(context)
								.getColumnNamesQueryMap(tablename);

					
						GETcolumn = tablenames.toArray(new String[tablenames
								.size()]);
						Log.i("welcome", "columns name Length::"
								+ GETcolumn.length);
					}

					else {
						Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.kindly_select_table), 1)
								.show();
					}
				}

				else {

					if (name.equalsIgnoreCase("column")) {
						column.setText(sb.toString());
					}

					else {
						if (sb.toString().equalsIgnoreCase("select all")) {
							field.setText(tablenames.toString());
							// field.setText("*");
							FIELDS = "*";

						} else {
							field.setText(sb.toString());
							FIELDS = sb.toString();
						}
					}
				}
			}
		});
		d.show();
	}

	public void populateresult(String value, HashMap<String, String> keyvalue) {
		selNonAggFld = value;

		Log.i("QB", "non agg  " + selNonAggFld);
		String[] temp = selNonAggFld.split(",");
		Log.i("QB", "length--->" + temp.length);
		Log.i("QB", "values of temp--->" + temp.toString());
		StringBuilder sbf = new StringBuilder();

		for (int i = 0; i < temp.length; i++) {
			Log.i("QB", "values of temp--->" + temp[i].toString());
			if (!keyvalue.containsKey(temp[i])) {
				String alias = temp[i].replace("[", "");
				alias = alias.replace("]", "");

				nonAggFldList.add(temp[i] + " as [" + alias.replace(".", "-")
						+ "]");
				sbf.append(temp[i]);

				columnName.add(alias.replace(".", "-"));
				columnType.add("nvarchar(20)");
				if (i != temp.length - 1) {
					sbf.append(",");
				}
			}

		}
		if (sbf.toString() != null) {
			Log.i("BL", "force close issue--->" + sbf.toString());
			if (sbf.toString().endsWith(",")) {
				isGroupby = sbf.toString().substring(0,
						sbf.toString().length() - 1);
			} else {
				isGroupby = sbf.toString();

			}
		}

		
		Log.i("QB", "is group by--->" + isGroupby);

		Iterator<Entry<String, String>> it = keyvalue.entrySet().iterator();
		String colName = null;
		String aggName = null;
		String aggFld = null;
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) it
					.next();
			colName = pairs.getKey();
			aggName = pairs.getValue();

			map.put(colName, aggName);

			Log.i("welcome", "--------" + aggName + "--------" + colName);

			aggFld = aggName.replace("()", "(" + colName + ")");
			String columnname = colName.replace("[", "").replace("]", "")
					.replace(".", "-");
			aggFld = aggFld + " as [" + columnname + "]";

			Log.i("BL", "------>AGGREGATE" + aggFld);
			aggFldList.add(aggFld);
			columnName.add(columnname);
			columnType.add("nvarchar(50)");
		}

		if (value != null)

			field.setText(value);

		else
			field.setText("");

		Log.i("QB", "values---->" + keyvalue.values());
		Log.i("QB", "values---->" + keyvalue.keySet().toString());
		Log.i("QB", "values---->" + nonAggFldList.toString());
		Log.i("QB", "values---->" + aggFldList.toString());

	}

	@Override
	public void onClick(View v) {
		
		if (v.getId() == table.getId()) {
			Log.i("welcome", "coming to table click");
			if (Action.equalsIgnoreCase("Dynamic Audio Call")
					|| Action.equalsIgnoreCase("Dynamic Video Call")) {
				List<String> list = new ArrayList<String>(
						Arrays.asList(DBNames));
				list.remove("Select All");
				String[] str_array = list.toArray(new String[0]);
				showSingleTable(str_array, "table");

			} else {
				showMultiselectBuddys(DBNames, "table");

			}
			Log.i("welcome", "coming to table field");

		}

		else if (v.getId() == field.getId()) {
			if (table.getText().toString().trim().length() != 0) {
				Intent intent = new Intent(context, Queryaggregate.class);
				intent.putExtra("field", GETcolumn);
				startActivity(intent);
			} else {
				Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.kindly_select_table),
						Toast.LENGTH_LONG).show();
			}
		} else if (v.getId() == BUILDQUERY.getId()) {

			if (table.getText().toString().trim().length() != 0
					&& (field.getText().toString().trim().length() != 0)) {
				Log.i("welcome", "coming to first if condtionssss--->");

				TABLENAME = table.getText().toString();
				FIELDS = field.getText().toString();

				String[] forms = TABLENAME.replace("[", "").replace("]", "").split(",");
				QueryBuilder queryBuilder = new QueryBuilder();
				querySelect = queryBuilder.getQuerySelect(forms);

				String[] colnames = FIELDS.replace("[", "").replace("]", "")
						.split(",");
				String[] columnnames = FIELDS.split(",");

				for (int i = 0; i < colnames.length; i++) {
					Log.i("welcome", "------>" + colnames[i] + "<-----");
					String[] values = colnames[i].split("\\.");
			

					String tablename = values[0];
					String columnname = values[1];
					
					Log.i("welcome", "------>" + tablename + "<-----");
					Log.i("welcome", "------>" + columnname + "<-----");

					obj = querySelect.getForm(tablename);

					obj.addColumnSelect(columnname);
					if (map.containsKey(columnnames[i])) {
						Column colObj = null;
						String aggregation = map.get(columnnames[i])
								.replace("(", "").replace(")", "");

						Log.i("welcome", "-------->aggregations---->"
								+ aggregation);
						colObj = obj.getColumnObj(columnname);
						colObj.setAggregateFunction(aggregation);
					}

				}
				Queryaggregate queryaggregate = new Queryaggregate();

				boolean b1;
				b1 = queryaggregate.where.isChecked();

				Log.i("welcome", "boolean iss" + b1);

				if (b1) {
					iswhereischecked = true;
					ValidateForm();

				}

				showAlert(querySelect.getCompleteSQL());

				query = querySelect.getCompleteSQL();

				

				TABLENAME.split(" ");

				Log.i("QB", "columnNames--->" + columnName.toString());

				Log.i("QB", "columTypes--->" + columnType.toString());

			

				Queryaggregate queryaggregate1 = new Queryaggregate();
				boolean b;
				b = queryaggregate1.where.isChecked();

				Log.i("welcome", "boolean iss" + b);

				

				Log.i("welcome", "Query Was Printing-->" + query);

			} else {
				Log.i("welcome", "coming to first if condtionssss--->");

				Toast.makeText(context, "Kindly select the table and field",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Log.i("welcome", "coming to first if condtionssss--->");

			Toast.makeText(context, "Kindly select the table",
					Toast.LENGTH_SHORT).show();
		}

		CallDispatcher.columnNames = columnName;
		CallDispatcher.columnTypes = columnType;

	}

	public void addFormField()

	{

		Log.d("welcome", "Coming Successfully");
		final LinearLayout tv = (LinearLayout) inflater.inflate(R.layout.text,
				null);
		inf_values = (EditText) tv.findViewById(R.id.value);
		inf_assignvalue = (ImageView) tv.findViewById(R.id.image_col);
		INFCOLUMN = null;
		inf_values.setText("");
		inf_assignvalue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (tablename != null || tablename.length() != 0) {
					tablenames = calldisp.getdbHeler(context).getColumnNamesQuery(tablename);
					tablenames.remove(whindex);
					GETcolumn = tablenames.toArray(new String[tablenames.size()]);
					showSingleSelectBuddy(GETcolumn, "infassign");

				} else {
					Toast.makeText(context, "Kindly Select Table", 1).show();

				}

			}
		});
		infSpinner = (Spinner) tv.findViewById(R.id.inf_andor);
		inf_column = (EditText) tv.findViewById(R.id.columnname);
		inf_condition = (EditText) tv.findViewById(R.id.condition);

		Log.i("welcome", "Valus-->" + inf_values);
		Log.i("welcome", "Where columns-->" + inf_column);

		inf_condition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (inf_column.getText().toString().length() != 0) {
					if (F_Type.equalsIgnoreCase("INTEGER")) {
						// setInputType(InputType.TYPE_CLASS_NUMBER);
						// setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
						// setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED)
						inf_values.setInputType(InputType.TYPE_CLASS_NUMBER);
						ShowInteger(v);
					}

					else if (F_Type.equalsIgnoreCase("nvarchar(20)")) {
						inf_values.setInputType(InputType.TYPE_CLASS_TEXT);
						ShowVarchar(v);
					}

				} else {

					Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.kindly_select_column), 1).show();

				}

			}

		});
		inf_column.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (tablename != null || tablename.length() != 0) {

					tablenames = calldisp.getdbHeler(context).getColumnNamesQuery(tablename);
					GETcolumn = tablenames.toArray(new String[tablenames.size()]);
					tableTypes = calldisp.getdbHeler(context).getColumnTypesTblQuery(tablename);
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
				
				parent.getItemAtPosition(position).toString();

				parent.getItemAtPosition(position).toString();
				Log.i("welcome", "Spinner values-->"
						+ parent.getItemAtPosition(position).toString());
				Log.i("welcome", "Spinner argus-->" + arg1.getParent());

				if (parent.getItemAtPosition(position).toString()
						.equalsIgnoreCase("AND")
						|| parent.getItemAtPosition(position).toString()
								.equalsIgnoreCase("OR")) {

					addFormField();
					((Spinner) arg1.getParent()).setClickable(false);
				} else {

					infSpinner.setSelection(0);
					Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.kindly_enter_details), 1).show();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				

			}
		});

		tv.setId(linear_layout.getChildCount());
		linear_layout.addView(tv);

	}

	protected void ShowInteger(final View v) {

		
		AlertDialog.Builder builder = new AlertDialog.Builder(
				QueryActivity.this);
		builder.create();
		builder.setTitle("Integer");
		final CharSequence[] choiceList = { "<", "<=", ">", ">=", "==", "!=" };

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

	}

	private void changeType(String type, View v) {

		inf_condition.setText(type);
	}

	protected void ShowVarchar(final View v) {

		
		AlertDialog.Builder builder = new AlertDialog.Builder(
				QueryActivity.this);
		builder.create();
		builder.setTitle("Varchar");
		final CharSequence[] choiceList = { "BEGINS WITH", "ENDS WITH",
				"CONTAINS", "EQUALS" };

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

	}

	private void changeVarchar(String type, View v) {

		inf_condition.setText(type);

	}

	void showSingleTable(final String[] choiceList, final String edit) {

		try

		{

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.create();
			// builder.setTitle("Add");

			// System.out.println(choiceList);

			int selected = -1; // does not select anything

			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (edit.equalsIgnoreCase("table")) {

								table.setText(choiceList[which].toString());
								tablename = choiceList[which].toString();

								if (tablename != null
										|| tablename.length() != 0) {
									tablenames = new ArrayList<String>(calldisp.getdbHeler(context)
											.getColumnNamesQueryMap(tablename)
											.keySet());
									columnListMap =calldisp.getdbHeler(context)
											.getColumnNamesQueryMap(tablename);

									GETcolumn = tablenames
											.toArray(new String[tablenames
													.size()]);
									Log.i("welcome", "columns name Length::"
											+ GETcolumn.length);
								}

								else {
									Toast.makeText(context,
											"Kindly Select Table", 1).show();
								}

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

	void showSingleSelectBuddy(final String[] choiceList, final String edit) {

		try

		{

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.create();
			// builder.setTitle("Add");

			// System.out.println(choiceList);

			int selected = -1; // does not select anything

			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (edit.equalsIgnoreCase("whcol")) {
								whindex = which;
								wh_column.setText(choiceList[which].toString());
								WHERECOLUMN = wh_column.getText().toString();
								F_Type = columnListMap.get((choiceList[which]
										.toString()));
								Log.i("welcome", "where columns" + WHERECOLUMN);
								Log.i("xml", "Printing field::" + F_Type);

							}

							else if (edit.equalsIgnoreCase("condition")) {

								conditions.setText(choiceList[which].toString());

							} else if (edit.equalsIgnoreCase("infcol")) {
								whindex = which;

								inf_column.setText(choiceList[which].toString());
								INFCOLUMN = choiceList[which].toString();
								Log.i("welcome", "Printing field::" + INFCOLUMN);

								F_Type = columnListMap.get((choiceList[which]
										.toString()));
								;

								Log.i("welcome",
										"Printing field::" + F_Type.length());

								Log.i("xml", "Printing field::" + F_Type);

								Log.i("welcome", "Printing field::"
										+ tableTypes.size());

								Log.i("welcome", "tableTypes field::"
										+ tableTypes);

								Log.i("welcome", "which field::" + which);

								Log.i("welcome", "whindex field::" + whindex);

							}

							else if (edit.equalsIgnoreCase("whcondition")) {

								wh_condition.setText(choiceList[which]
										.toString());
								WHCONDTITION = wh_condition.getText()
										.toString();
								Log.i("i", "valuesssss" + WHCONDTITION);

							} else if (edit.equalsIgnoreCase("whassign")) {

								wh_values.setText(choiceList[which].toString());

							} else if (edit.equalsIgnoreCase("infassign")) {

								inf_values.setText(choiceList[which].toString());
								choiceList[which].toString();

							} else if (edit.equalsIgnoreCase("infcondition")) {

								inf_condition.setText(choiceList[which]
										.toString());
								choiceList[which].toString();

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

	private String ValidateForm() {
		Log.i("thread", "camr yo validateform");
		field_values.clear();
		String totalvalue = " ";
		for (int j = 0; j < linear_layout.getChildCount(); j++) {

			LinearLayout layout = (LinearLayout) linear_layout.getChildAt(j);

			EditText fields = (EditText) layout.findViewById(R.id.columnname);
			EditText condition = (EditText) layout.findViewById(R.id.condition);
			EditText value = (EditText) layout.findViewById(R.id.value);
			Spinner spin_values = (Spinner) layout.findViewById(R.id.inf_andor);

			if (!spin_values.getSelectedItem().toString()
					.equalsIgnoreCase("BIND")) {

				spinner_values.add(spin_values.getSelectedItem().toString());

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
								if (value1.replace("%", "").matches(
										".*\\.\\[.*\\]")) {

									String[] values = value1.replace("[", "")
											.replace("]", "").replace("%", "")
											.split("\\.");

									String RHSFormName = values[0];
									String RHSColumnName = values[1];

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
													+ RHSFormName + "colnamw:"
													+ RHSColumnName);

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
													+ LHSFormName + "colnamw:"
													+ LHSColumnName);
									querySelect.addINNERJoin(
											querySelect.getForm(LHSFormName),
											LHSColumnName,
											querySelect.getForm(RHSFormName),
											RHSColumnName);

								} else {

									Condition con1 = obj.getCondition("",
											LHSColumnName, con, value1);
									obj.addCondition(con1);
								}

							} else if (condtion[i]
									.equalsIgnoreCase("ENDS WITH")) {

								value1 = "%" + value.getText().toString();

								if (value1.replace("%", "").matches(
										".*\\.\\[.*\\]")) {
									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][]=====>");

									String[] values = value1.replace("[", "")
											.replace("]", "").replace("%", "")
											.split("\\.");

									String RHSFormName = values[0];
									String RHSColumnName = values[1];

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
													+ RHSFormName + "colnamw:"
													+ RHSColumnName);

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
													+ LHSFormName + "colnamw:"
													+ LHSColumnName);
									querySelect.addINNERJoin(
											querySelect.getForm(LHSFormName),
											LHSColumnName,
											querySelect.getForm(RHSFormName),
											RHSColumnName);

								} else {
									Condition con1 = obj.getCondition("",
											LHSColumnName, con, value1);
									obj.addCondition(con1);
								}

							} else if (condtion[i].equalsIgnoreCase("CONTAINS")) {

								value1 = "%" + value.getText().toString() + "%";

								if (value1.replace("%", "").matches(
										".*\\.\\[.*\\]")) {
									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][]=====>");

									String[] values = value1.replace("[", "")
											.replace("]", "").replace("%", "")
											.split("\\.");

									String RHSFormName = values[0];
									String RHSColumnName = values[1];
									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
													+ RHSFormName + "colnamw:"
													+ RHSColumnName);

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
													+ LHSFormName + "colnamw:"
													+ LHSColumnName);

									querySelect.addINNERJoin(
											querySelect.getForm(LHSFormName),
											LHSColumnName,
											querySelect.getForm(RHSFormName),
											RHSColumnName);

								} else {
									Condition con1 = obj.getCondition("",
											LHSColumnName, con, value1);
									obj.addCondition(con1);
								}

							} else if (condtion[i].equalsIgnoreCase("EQUALS")) {
								value1 = value.getText().toString();
								if (value1.replace("%", "").matches(
										".*\\.\\[.*\\]")) {
									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][]=====>");

									String[] values = value1.replace("[", "")
											.replace("]", "").replace("%", "")
											.split("\\.");

									String RHSFormName = values[0];
									String RHSColumnName = values[1];

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
													+ RHSFormName + "colnamw:"
													+ RHSColumnName);

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
													+ LHSFormName + "colnamw:"
													+ LHSColumnName);

									querySelect.addINNERJoin(
											querySelect.getForm(LHSFormName),
											LHSColumnName,
											querySelect.getForm(RHSFormName),
											RHSColumnName);

								} else {
									Condition con1 = obj.getCondition("",
											LHSColumnName, "=", value1);

									obj.addCondition(con1);

								}
							}

							else {
								value1 = value.getText().toString();
								if (value1.matches(".*\\.\\[.*\\]")) {
									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][]=====>");

									String[] values = value1.replace("[", "")
											.replace("]", "").split("\\.");

									String RHSFormName = values[0];
									String RHSColumnName = values[1];

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
													+ RHSFormName + "colnamw:"
													+ RHSColumnName);

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
													+ LHSFormName + "colnamw:"
													+ LHSColumnName);
									querySelect.addINNERJoin(
											querySelect.getForm(LHSFormName),
											LHSColumnName,
											querySelect.getForm(RHSFormName),
											RHSColumnName);

								} else {
									int number_value = Integer.parseInt(value1);
									Condition con1 = obj.getCondition("",
											LHSColumnName, symbol, new Integer(
													number_value), "INT");
									obj.addCondition(con1);

								}
							}
						}

					}
				}

			}

			else {
				if ((condition.getText().toString() != null && !condition
						.getText().toString().equals(""))
						&& (fields.getText().toString() != null && !fields
								.getText().toString().equals(""))
						&& (value.getText().toString() != null && !fields
								.getText().toString().equals(""))
						&& (spinner_values.get(0).toString() != null && !fields
								.getText().toString().equals(""))) {

					String[] fiel = fields.getText().toString()
							.replace("[", "").replace("]", "").split("\\.");

					String LHSFormName = fiel[0];
					String LHSColumnName = fiel[1];
					String con = null;
					String value1 = null;
					String symbol = null;
					String[] condtion = { "<", "<=", ">", ">=", "==", "!=",
							"BEGINS WITH", "ENDS WITH", "CONTAINS", "EQUALS" };

					for (int i = 0; i < condtion.length; i++) {
						if (condition.getText().toString()
								.equalsIgnoreCase(condtion[i])) {
							symbol = condtion[i];

							con = "like";

							if (condtion[i].equalsIgnoreCase("BEGINS WITH")) {

								value1 = value.getText().toString() + "%";
								if (value1.replace("%", "").matches(
										".*\\.\\[.*\\]")) {

									String[] values = value1.replace("[", "")
											.replace("]", "").replace("%", "")
											.split("\\.");

									String RHSFormName = values[0];
									String RHSColumnName = values[1];

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
													+ RHSFormName + "colnamw:"
													+ RHSColumnName);

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
													+ LHSFormName + "colnamw:"
													+ LHSColumnName);
									querySelect.addINNERJoin(
											querySelect.getForm(LHSFormName),
											LHSColumnName,
											querySelect.getForm(RHSFormName),
											RHSColumnName);

								} else {
									Condition con1 = obj.getCondition(
											spinner_values.get(0).toString(),
											LHSColumnName, con, value1);
									obj.addCondition(con1);
									spinner_values.remove(0);
								}

							} else if (condtion[i]
									.equalsIgnoreCase("ENDS WITH")) {

								value1 = "%" + value.getText().toString();

								if (value1.replace("%", "").matches(
										".*\\.\\[.*\\]")) {
									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][]=====>");

									String[] values = value1.replace("[", "")
											.replace("]", "").replace("%", "")
											.split("\\.");

									String RHSFormName = values[0];
									String RHSColumnName = values[1];

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
													+ RHSFormName + "colnamw:"
													+ RHSColumnName);

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
													+ LHSFormName + "colnamw:"
													+ LHSColumnName);
									querySelect.addINNERJoin(
											querySelect.getForm(LHSFormName),
											LHSColumnName,
											querySelect.getForm(RHSFormName),
											RHSColumnName);

								} else {
									Condition con1 = obj.getCondition(
											spinner_values.get(0).toString(),
											LHSColumnName, con, value1);
									obj.addCondition(con1);
									spinner_values.remove(0);

								}

							} else if (condtion[i].equalsIgnoreCase("CONTAINS")) {

								value1 = "%" + value.getText().toString() + "%";

								if (value1.replace("%", "").matches(
										".*\\.\\[.*\\]")) {
									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][]=====>");

									String[] values = value1.replace("[", "")
											.replace("]", "").replace("%", "")
											.split("\\.");

									String RHSFormName = values[0];
									String RHSColumnName = values[1];
									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
													+ RHSFormName + "colnamw:"
													+ RHSColumnName);

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
													+ LHSFormName + "colnamw:"
													+ LHSColumnName);

									querySelect.addINNERJoin(
											querySelect.getForm(LHSFormName),
											LHSColumnName,
											querySelect.getForm(RHSFormName),
											RHSColumnName);

								} else {
									Condition con1 = obj.getCondition(
											spinner_values.get(0).toString(),
											LHSColumnName, con, value1);
									obj.addCondition(con1);
									spinner_values.remove(0);
								}

							} else if (condtion[i].equalsIgnoreCase("EQUALS")) {
								value1 = value.getText().toString();

								if (value1.replace("%", "").matches(
										".*\\.\\[.*\\]")) {
									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][]=====>");

									String[] values = value1.replace("[", "")
											.replace("]", "").replace("%", "")
											.split("\\.");

									String RHSFormName = values[0];
									String RHSColumnName = values[1];

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
													+ RHSFormName + "colnamw:"
													+ RHSColumnName);

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
													+ LHSFormName + "colnamw:"
													+ LHSColumnName);

									querySelect.addINNERJoin(
											querySelect.getForm(LHSFormName),
											LHSColumnName,
											querySelect.getForm(RHSFormName),
											RHSColumnName);

								} else {
									Condition con1 = obj.getCondition(
											spinner_values.get(0).toString(),
											LHSColumnName, con, value1);
									obj.addCondition(con1);
									spinner_values.remove(0);

								}
							}

							else {
								value1 = value.getText().toString();
								if (value1.matches(".*\\.\\[.*\\]")) {
									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][]=====>");

									String[] values = value1.replace("[", "")
											.replace("]", "").split("\\.");

									String RHSFormName = values[0];
									String RHSColumnName = values[1];

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] leftside=====>formname: "
													+ RHSFormName + "colnamw:"
													+ RHSColumnName);

									Log.i("welcome",
											"VALUE CONTAINS SQUARE[][][][] Rightside=====>formname: "
													+ LHSFormName + "colnamw:"
													+ LHSColumnName);
									querySelect.addINNERJoin(
											querySelect.getForm(LHSFormName),
											LHSColumnName,
											querySelect.getForm(RHSFormName),
											RHSColumnName);

								} else {
									int number_value = Integer.parseInt(value1);
									Condition con1 = obj.getCondition(
											spinner_values.get(0).toString(),
											LHSColumnName, symbol, new Integer(
													number_value), "INT");
									obj.addCondition(con1);
									// Condition con1 =
									// obj.getCondition(spinner_values.get(0).toString(),
									// fields.getText().toString(),con,value1);
									// obj.addCondition(con1);
									spinner_values.remove(0);

								}
							}
						}

					}
				}

			}

		}
		return totalvalue;

	}

	private void showAlert(String message) {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				QueryActivity.this).create();

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
				Intent i = new Intent();
				Bundle bun = new Bundle();
				bun.putString("squery", query);
				i.putExtra("share", bun);
				setResult(-1, i);
				alertDialog.dismiss();
				finish();
			}
		});

		alertDialog.show();

	}
}