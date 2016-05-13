package com.cg.forms;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class Compute extends Activity implements OnClickListener {

	private EditText compfield;
	private Button btn_addcompute, btn_dropdown, compute, add_field;
	private AlertDialog alert = null;
	private LinearLayout computelayy = null;
	private String formula = null;
	private ArrayList<String> computeFields = null;
	private ArrayList<String> computeFieldsExpresion = null;
	private String result = null;
	private String selectedfields = "";
	private LayoutInflater layoutInflate = null;
	private RelativeLayout layou = null;
	private ScrollView scrollview = null;
	private String computefields = "";
	private Context context = null;
	private Button btn_addcomputein;
	private String[] numericValue;
	private String[] DateValue;
	private String[] FreeTextValue;
	private String DataTypes = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.compute);
		compfield = (EditText) findViewById(R.id.compfield);
		btn_addcompute = (Button) findViewById(R.id.btn_addcompute);
		btn_addcompute.setOnClickListener(this);
		computelayy = (LinearLayout) findViewById(R.id.computelayy);
		computeFields = new ArrayList<String>();
		computeFieldsExpresion = new ArrayList<String>();
		btn_dropdown = (Button) findViewById(R.id.btn_dropdown);
		btn_dropdown.setOnClickListener(this);
		scrollview = (ScrollView) findViewById(R.id.content_scro);
		compute = (Button) findViewById(R.id.Compute);
		compute.setOnClickListener(this);
		context = this;
		btn_addcomputein = (Button) findViewById(R.id.btn_addcompute);
		add_field = (Button) findViewById(R.id.addnewfield);
		add_field.setOnClickListener(this);

		splitValuesBasedType();

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

	private void splitValuesBasedType() {
		// TODO Auto-generated method stub
		ArrayList<String> freetextList = new ArrayList<String>();
		ArrayList<String> NumberList = new ArrayList<String>();
		ArrayList<String> DateList = new ArrayList<String>();
		if (CallDispatcher.inputFieldList.size() > 0) {

			for (InputsFields input : CallDispatcher.inputFieldList) {

				if (input.getFieldType().startsWith("Free")) {
					freetextList.add(input.getFieldName());

				}
				if (input.getFieldType().startsWith("Num")) {
					NumberList.add(input.getFieldName());

				}
				if (input.getFieldType().startsWith("Dat")
						|| input.getFieldType().startsWith("Current Date")) {
					DateList.add(input.getFieldName());

				}

			}
			FreeTextValue = freetextList
					.toArray(new String[freetextList.size()]);
			numericValue = NumberList.toArray(new String[NumberList.size()]);
			DateValue = DateList.toArray(new String[DateList.size()]);

		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == btn_dropdown.getId()) {
			ShowField(v);
			compfield.setText("");
		} else if (v.getId() == compute.getId()) {
			String computeValue = null;
			computeValue = btn_addcomputein.getContentDescription().toString();

			if ((compfield.getText().toString().length() > 0)
					&& (!computeValue.equalsIgnoreCase("null") && computeValue
							.length() > 0)) {
				StringBuilder sbt = new StringBuilder();
				formula = "";
				computeFields.clear();
				computeFieldsExpresion.clear();

				for (int j = 0; j < computelayy.getChildCount(); j++) {
					RelativeLayout layout = (RelativeLayout) computelayy
							.getChildAt(j);
					btn_addcomputein = (Button) layout
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

				if (DataTypes.startsWith("Free")) {
					result = "FT " + formula;

				} else if (DataTypes.startsWith("Num")) {
					result = "NM " + formula;

				} else if (DataTypes.startsWith("Dat")
						|| DataTypes.equalsIgnoreCase("Current Date")) {
					result = "DT " + formula;

				}

				Intent i = new Intent();
				Bundle bun = new Bundle();
				bun.putString("compute", result);
				i.putExtra("share", bun);
				setResult(-12, i);
				finish();

			} else {
				Toast.makeText(context,
						SingleInstance.mainContext.getResources().getString(R.string.add_two_fields),
						Toast.LENGTH_LONG).show();
			}
		} else if (v.getId() == add_field.getId()) {

			if (DataTypes.length() > 0) {
				if (compfield.getText().toString().length() > 0) {
					addFormCompute();

				} else {
					Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.select_fields),
							Toast.LENGTH_LONG).show();

				}
			} else {

				Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.add_any_fields),
						Toast.LENGTH_LONG).show();
			}

		} else if (v.getId() == btn_addcompute.getId()) {
			if (compfield.getText().toString().length() > 0) {
				if (DataTypes.length() > 0) {
					if (DataTypes.startsWith("Free")) {
						showExpressionFreeText(v);

					} else if (DataTypes.startsWith("Num")) {

						showExpression(v);

					} else if (DataTypes.startsWith("Dat")
							|| DataTypes.equalsIgnoreCase("Current Date")) {
						showExpressionDate(v);

					}

				} else {
					Toast.makeText(context,
							SingleInstance.mainContext.getResources().getString(R.string.add_any_fields),
							Toast.LENGTH_LONG).show();

				}

			} else {
				Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.select_fields),
						Toast.LENGTH_LONG).show();

			}

		}

	}

	private void addFormCompute() {

		layoutInflate = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layou = (RelativeLayout) layoutInflate.inflate(R.layout.computefields,
				null);
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
				if (DataTypes.startsWith("Free")) {
					ShowFields(v, FreeTextValue);

				} else if (DataTypes.startsWith("Num")) {
					ShowFields(v, numericValue);

				} else if (DataTypes.startsWith("Dat")
						|| DataTypes.equalsIgnoreCase("Current Date")) {
					ShowFields(v, DateValue);

				}
			}

			private void ShowFields(final View v, final String[] co) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						Compute.this);
				builder.create();
				builder.setTitle("Field Values");

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
				if (DataTypes.startsWith("Free")) {
					showExpressionsFreeText(v);

				} else if (DataTypes.startsWith("Num")) {
					showExpressions(v);

				} else if (DataTypes.startsWith("Dat")
						|| DataTypes.equalsIgnoreCase("Current Date")) {
					showExpressionsDate(v);

				}
			}

			private void showExpressions(final View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						Compute.this);
				builder.create();
				builder.setTitle("Expression");

				final CharSequence[] choiceList = { "+", "-", "*", "/" };

				builder.setItems(choiceList,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								changeExpression(choiceList[which].toString(),
										v);
								alert.cancel();
							}
						});
				alert = builder.create();
				alert.show();

			}

			private void showExpressionsFreeText(final View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						Compute.this);
				builder.create();
				builder.setTitle("Expression");

				final CharSequence[] choiceList = { "+" };

				builder.setItems(choiceList,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								changeExpression(choiceList[which].toString(),
										v);
								alert.cancel();
							}
						});
				alert = builder.create();
				alert.show();

			}

			private void showExpressionsDate(final View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						Compute.this);
				builder.create();
				builder.setTitle("Expression");

				final CharSequence[] choiceList = { "-" };

				builder.setItems(choiceList,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								changeExpression(choiceList[which].toString(),
										v);
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
	}

	private void showExpression(final View v) {

		AlertDialog.Builder builder = new AlertDialog.Builder(Compute.this);
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

	}

	private void showExpressionFreeText(final View v) {

		AlertDialog.Builder builder = new AlertDialog.Builder(Compute.this);
		builder.create();
		builder.setTitle("Expression");

		final CharSequence[] choiceList = { "+" };

		builder.setItems(choiceList, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				changeExpression(choiceList[which].toString(), v);
				alert.cancel();
			}
		});
		alert = builder.create();
		alert.show();

	}

	private void showExpressionDate(final View v) {

		AlertDialog.Builder builder = new AlertDialog.Builder(Compute.this);
		builder.create();
		builder.setTitle("Expression");

		final CharSequence[] choiceList = { "-" };

		builder.setItems(choiceList, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				changeExpression(choiceList[which].toString(), v);
				alert.cancel();
			}
		});
		alert = builder.create();
		alert.show();

	}

	private void changeExpression(String type, View v) {
		Button compute = (Button) v.findViewById(R.id.btn_addcompute);
		compute.setContentDescription(type);

	}

	private void ShowField(final View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(Compute.this);
		builder.create();
		builder.setTitle("Field Values");

		ArrayList<String> comLst = new ArrayList<String>();
		final HashMap<String, String> fiel_datatype = new HashMap<String, String>();
		for (InputsFields input : CallDispatcher.inputFieldList) {

			if (input.getFieldType().equalsIgnoreCase("Free Text")
					|| input.getFieldType().equalsIgnoreCase("Date")
					|| input.getFieldType().equalsIgnoreCase("Current Date")
					|| input.getFieldType().equalsIgnoreCase("Numeric")) {
				comLst.add(input.getFieldName());
				fiel_datatype.put(input.getFieldName(), input.getFieldType());

			}
		}
		final String[] co = comLst.toArray(new String[comLst.size()]);
		if (co.length > 0) {
			builder.setItems(co, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					DataTypes = fiel_datatype.get(co[which]);
					changeFiled(co[which].toString(), v);
					alert.cancel();
				}
			});
			alert = builder.create();
			alert.show();
		} else {

			Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.add_fields_form),
					Toast.LENGTH_LONG).show();

		}

	}

	private void changeFiled(String type, View v) {
		compfield.setText(type);
	}

}
