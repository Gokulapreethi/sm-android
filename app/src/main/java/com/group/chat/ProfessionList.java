package com.group.chat;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.group.ProfessionAdapter;
import com.util.SingleInstance;

public class ProfessionList extends Activity{
	

	private Context context;
	private CallDispatcher callDisp;
	private ListView professionListView;
	private Button back,done;
	private Handler handler = new Handler();
	private ProgressDialog progressDialog = null;
	private ProfessionAdapter listAdapter;
	private String value="";
	private Boolean isChecked=false;
	private String[] profession;


	
	protected void onCreate(Bundle savedInstanceState) {
		try {

			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);

			requestWindowFeature(Window.FEATURE_NO_TITLE);

		    setContentView(R.layout.profession_list);
			context = this;
		 back = (Button) findViewById(R.id.btn_back);
		 done = (Button) findViewById(R.id.btn_done);

		 back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					try {
						finish();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		professionListView = (ListView) findViewById(R.id.professionlist);

		    profession = Arrays.copyOf(SingleInstance.mainContext.profession, SingleInstance.mainContext.profession.length);
            ArrayList<String> professionList = new ArrayList<String>();
            professionList.addAll( Arrays.asList(profession) );


             listAdapter = new ProfessionAdapter(this, professionList);

            professionListView.setAdapter( listAdapter );

            professionListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Object obj = professionListView.getAdapter().getItem(arg2);
                    value = obj.toString();
					listAdapter.setSelectedIndex(arg2);
				    listAdapter.notifyDataSetChanged();
				    Log.i("Test", "LISTVIEW CLICK VALUE"+value);
				    isChecked=true;

				}
            });
             done.setOnClickListener(new OnClickListener() {

 				@Override
 				public void onClick(View arg0) {

 					try {
 						if(isChecked) {
 						WebServiceReferences.webServiceClient.setProfessionPermission(
 								CallDispatcher.LoginUser, CallDispatcher.Password,value,context);
 						showDialog();
 						} else
 							showToast("Select Any Profession");
 					} catch (Exception e) {
 						e.printStackTrace();
 					}
 				}
 			});


	    }  catch(Exception e) {

		    e.printStackTrace();
	    }


	}
	public void notifyWebserviceResponse(Object obj)
	{
		cancelDialog();

		if (obj instanceof String) {
			String result = (String) obj;

			if (result.equalsIgnoreCase("Success"))
				showToast("Successfully profession updated");
		}
			else
				showToast("Unable to save profession.Please try again");


	}
		private void showToast(final String message) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					try {
						// TODO Auto-generated method stub
						Toast.makeText(context, message, Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		}
		public void cancelDialog() {
			try {
				if (progressDialog != null && progressDialog.isShowing()) {
					Log.i("register", "--progress bar end-----");
					progressDialog.dismiss();
					progressDialog = null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		private void showDialog() {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						progressDialog = new ProgressDialog(context);
						if (progressDialog != null) {
							progressDialog.setCancelable(false);
							progressDialog.setMessage("Progress ...");
							progressDialog
									.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressDialog.setProgress(0);
							progressDialog.setMax(100);
							progressDialog.show();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});

		}



}