package com.cg.utilities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;

public class Blocked_list extends Activity implements OnClickListener {

	private Context context;

	private ListView lview;

	private ImageView btn_back;

	private CallDispatcher callDisp;

	private TextView tv_title;

	private Typeface tf_regular;

	private Typeface tf_bold;

	private BlocklistAdapter adapter;
	
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.blocked_list);
		context = this;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int noScrHeight = displaymetrics.heightPixels;
		int noScrWidth = displaymetrics.widthPixels;

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);

		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		callDisp.setNoScrHeight(noScrHeight);
		callDisp.setNoScrWidth(noScrWidth);
		displaymetrics = null;
		tf_regular = Typeface.createFromAsset(context.getAssets(),
				"fonts/ARIAL.TTF");
		tf_bold = Typeface.createFromAsset(context.getAssets(),
				"fonts/ARIALBD.TTF");
		WebServiceReferences.contextTable.put("block_list", context);
		tv_title = (TextView) findViewById(R.id.tv_blocktitle);
		tv_title.setTypeface(tf_bold);
		btn_back = (ImageView) findViewById(R.id.iv_blockback);
		btn_back.setOnClickListener(this);
		lview = (ListView) findViewById(R.id.lview_blocklist);
		lview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub
				showAlert(callDisp.blocked_buddies.get(position));
			}
		});
		adapter = new BlocklistAdapter(this, callDisp.blocked_buddies);
		lview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    private void showAlert(final String name) {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
		myAlertDialog.setTitle("UnBclock Buddy");
		myAlertDialog.setMessage("Do you want to UnBlock " + name);
		myAlertDialog.setPositiveButton("UnBlock",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						if (WebServiceReferences.running) {
							CallDispatcher.pdialog = new ProgressDialog(context);
							callDisp.showprogress(CallDispatcher.pdialog,
									context);
							String[] block_params = new String[3];
							block_params[0] = CallDispatcher.LoginUser;
							block_params[1] = name;
							block_params[2] = "0";

							WebServiceReferences.webServiceClient
									.blockUnblock(block_params);
						}

					}
				});
		myAlertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int arg1) {
						// do something when the Cancel button is clicked
						dialog.cancel();
					}
				});
		myAlertDialog.show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("block_list");
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_blockback:
			finish();
			break;

		default:
			break;
		}
	}

	public void notifyBuddyblockunblock(Object res) {
		callDisp.cancelDialog();
		if (res instanceof String[]) {
			String[] result = (String[]) res;
			if (res != null) {
				if (result[3] != null) {
					if (result[3].equals("Unblocked successfully")) {
						for (int i = 0; i < callDisp.blocked_buddies.size(); i++) {
							if (callDisp.blocked_buddies.get(i).equals(
									result[1])) {
								callDisp.blocked_buddies.remove(i);
								break;
							}
						}
					} else if (result[3].equals("Blocked successfully")) {
						callDisp.blocked_buddies.add(result[1]);
					} else
						Toast.makeText(context, result[3], Toast.LENGTH_SHORT)
								.show();
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

	public class BlocklistAdapter extends ArrayAdapter<String> {

		private List<String> list;
		private LayoutInflater inflator;
		Context context;

		public BlocklistAdapter(Activity context, ArrayList<String> list) {
			super(context, R.layout.blcokadapter_row, list);
			this.list = list;
			inflator = context.getLayoutInflater();
			this.context = context;
		}

		@Override
		public String getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null)
				convertView = inflator.inflate(R.layout.blcokadapter_row, null);

			TextView tv_buddyname = (TextView) convertView
					.findViewById(R.id.tv_blockbuddy);
			tv_buddyname.setTypeface(tf_regular);
			tv_buddyname.setText(list.get(position));

			return convertView;

		}

	}

}
