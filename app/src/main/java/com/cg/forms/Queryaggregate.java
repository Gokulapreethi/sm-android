package com.cg.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;

public class Queryaggregate extends Activity {

	private Button save,cancel;
	private String[] field=null;
	private LinearLayout linearLayout = null;
	private AlertDialog alert = null;
	private TextView aggregate = null;
	private Context context = null;
	private CheckBox selectall = null;
//	Boolean where = false;gPgP
	String query = null;
	static CheckBox where;
	Boolean isstring = false;
	ArrayList<String> tableTypes = null;
	static String tablename = null; 
	String F_Type = null;
	CallDispatcher calldisp;
	int whindex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.formsaggregate);
		context = this;
		WebServiceReferences.contextTable.put("qaggregate", context);
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
		tableTypes = new ArrayList<String>();
		tablename = QueryActivity.tablename;
		linearLayout = (LinearLayout) findViewById(R.id.layout1);
		 
		 field = getIntent().getStringArrayExtra("field");
		 shownext();
		 
		 Log.i("welcome","Field Length..." +field.length);
		 selectall = (CheckBox) findViewById(R.id.selectall);
		 
		 save = (Button) findViewById(R.id.save);
		 save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String selected_fields="";
				
				HashMap<String,String> fieldvalue= new HashMap<String, String>();
				
				for(int i=0;i<linearLayout.getChildCount();i++)
				{
					LinearLayout lay = (LinearLayout) linearLayout.getChildAt(i);
					CheckBox checkb = (CheckBox) lay.findViewById(R.id.checkbox);
					TextView aggre  = (TextView) lay.findViewById(R.id.aggregate);
							
					if(checkb.isChecked())
					{
						TextView tv_fld=(TextView)lay.findViewById(R.id.fieldvalue);
						
						if(selected_fields.trim().length()==0)
						{
							selected_fields=tv_fld.getText().toString().trim();
							if(!aggre.getText().toString().equalsIgnoreCase("Aggregate:Not Selected"))
							{
								Log.i("welcome","Coming to haspmap-->");

							
								fieldvalue.put(selected_fields,aggre.getText().toString().replace("Aggregate:", ""));
								
								Log.i("welcome","Coming to haspmap Key-->"+selected_fields);
								Log.i("welcome","Coming to haspmap value-->"+aggre.getText().toString());
								Log.i("welcome","Coming to haspmap-->"+fieldvalue);
							}	
							}
						
						else
						{
							selected_fields=selected_fields+","+tv_fld.getText().toString().trim();
							
							if(!aggre.getText().toString().equalsIgnoreCase("Aggregate:Not Selected"))
							{
								Log.i("welcome","Coming to else haspmap-->");

								fieldvalue.put(tv_fld.getText().toString().trim(),aggre.getText().toString().replace("Aggregate:", ""));
								
								Log.i("welcome","Coming to else haspmap Key-->"+tv_fld.getText().toString().trim());
								
								Log.i("welcome","Coming to else haspmap value-->"+aggre.getText().toString());

								
								Log.i("welcome","Coming to haspmap-->"+fieldvalue);

							}
						}
					}
				
			}	 where = (CheckBox) findViewById(R.id.where);
			
				if(WebServiceReferences.contextTable.containsKey("QueryActivity"))
				{	
					if(where.isChecked())
					{
					  QueryActivity query = (QueryActivity)WebServiceReferences.contextTable.get("QueryActivity");
					  query.populateresult(selected_fields,fieldvalue);
					  query.addFormField();
					  
					}
					
					else 
					{
						QueryActivity query = (QueryActivity)WebServiceReferences.contextTable.get("QueryActivity");
						query.populateresult(selected_fields,fieldvalue);	
					}	
						
				}
				
				finish();
			}
		});
		 
		 cancel = (Button) findViewById(R.id.cancel);
		 cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		 selectall.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				{
					Log.i("welcome","select all cominggg...");

					for(int i=0;i<linearLayout.getChildCount();i++)
					{
						Log.i("welcome","-------------.."+linearLayout.getChildAt(i));
						LinearLayout lay = (LinearLayout) linearLayout.getChildAt(i);
						Log.i("welcome","-------------123.."+lay.getChildAt(0));
						Log.i("welcome","-------------12.."+lay.getChildAt(1));
						CheckBox checkb = (CheckBox) lay.findViewById(R.id.checkbox);
						Log.i("welcome","Checkbox cominggg.."+checkb);
						checkb.setChecked(isChecked);
				    }	
		       	}
			 }
		});
		
		
		 
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("qaggregate");
		super.onDestroy();
	}



	public void shownext()
	{
		
		if(field!=null)
	
			 Log.i("welcome","inside if...");

			Log.i("welcome","inside the field" +field.toString());
		
			for(int i=0;i<field.length;i++)
		   {
		    Log.i("welcome","inside for...");

			LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.formsaggregatefield, null);
			
			linearLayout.addView(layout);
		
			CheckBox checkbox = (CheckBox) layout.findViewById(R.id.checkbox);
			final Button dropdown = (Button) layout.findViewById(R.id.dropdown);
			
			TextView text = (TextView) layout.findViewById(R.id.fieldvalue);

			tableTypes = calldisp.getdbHeler(context).getColumnSpclQA(tablename);
			
			F_Type = tableTypes.get(i).toString();
			dropdown.setContentDescription(F_Type);
			text.setText(field[i]);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put(field[i], F_Type);
			
			Log.i("welcome","Field was printing tablename--->" +tablename);

			Log.i("welcome","Field was printing--->" +field[i]);
			Log.i("welcome", "-----> fieldtype"+F_Type+"<-------");


			dropdown.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					RelativeLayout lay = (RelativeLayout) v.getParent();
					
					LinearLayout lllayout= (LinearLayout)lay.findViewById(R.id.layout3);
					
					aggregate = (TextView) lllayout.findViewById(R.id.aggregate);
				
				   // ShowView(v);
					Log.i("welcome", "-----> fieldtype"+F_Type+"<-------"+dropdown.getContentDescription().toString());
					
					if (dropdown.getContentDescription().toString().equalsIgnoreCase("INTEGER")) 
					{
						ShowView(v);
					} 
					
					else if (dropdown.getContentDescription().toString().equalsIgnoreCase("nvarchar(20)")) 
					{
						showvarchar(v);
					}
					
					else 
					{
					 Toast.makeText(context, "Kindly Select Column",Toast.LENGTH_SHORT).show();
					}
					
			   }
		 });
	  }	
		
	}

	protected void ShowView(final View v) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(Queryaggregate.this);
		builder.create();
		builder.setTitle("Aggregation");
		final CharSequence[] choiceList = {"TOTAL()", "SUM()", "COUNT()", "MAX()","MIN()", "AVG()"};
		// System.out.println(choiceList);

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

		aggregate.setText("Aggregate:"+type);

	}
	protected void showvarchar(final View v) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(Queryaggregate.this);
		builder.create();
		builder.setTitle("Aggregation");
		final CharSequence[] choiceList = {"COUNT()"};
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

	}

	private void changeVarchar(String type, View v) {

		aggregate.setText("Aggregate:"+type);

	}

}


	
