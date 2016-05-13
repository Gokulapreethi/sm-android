package com.cg.account;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.cg.snazmed.R;
import com.main.AppMainActivity;

public class ShareByProfileByddy extends Activity implements OnClickListener

{
	private ListView listView;

	private ArrayAdapter<String> adapter;

	private Context context = null;

	private Button back = null;

	private Button search, selectall = null;

	private String[] userid;

	private String[] outputStrArr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.sharebuddy);
		context = this;

		initView();

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void initView() {

		selectall = (Button) findViewById(R.id.selectall);
		back = (Button) findViewById(R.id.btn_viewrecback);
		search = (Button) findViewById(R.id.btn_viewaddanother);
		search.setOnClickListener(this);
		userid = getIntent().getStringArrayExtra("username");
		Log.i("IOS", "length====>" + userid.length);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, userid);
		listView = (ListView) findViewById(R.id.list);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
						SparseBooleanArray checked = listView.getCheckedItemPositions();
						ArrayList<String> selectedItems = new ArrayList<String>();
						for (int j = 0; j < checked.size(); j++) {
							// Item position in adapter
							int position = checked.keyAt(j);
							if (checked.valueAt(j))
								selectedItems.add(adapter.getItem(position));
						}

						outputStrArr = new String[selectedItems.size()];

						for (int k = 0; k < selectedItems.size(); k++) {
							outputStrArr[k] = selectedItems.get(k);
						}

						if (outputStrArr.length == userid.length) {
							selectall.setText("Deselect All");
						} else {
							selectall.setText("Select All");
						}
						checked = null;
					}
				}
		);

		selectall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (selectall.getText().toString()
						.equalsIgnoreCase("select all")) {
					selectall.setText("Deselect All");
					for (int i = 0; i < listView.getCount(); i++)
						listView.setItemChecked(i, true);
				} else {
					selectall.setText("Select All");

					for (int i = 0; i < listView.getCount(); i++)
						listView.setItemChecked(i, false);
				}

			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				SparseBooleanArray checked = listView.getCheckedItemPositions();
				ArrayList<String> selectedItems = new ArrayList<String>();
				for (int i = 0; i < checked.size(); i++) {
					// Item position in adapter
					int position = checked.keyAt(i);
					// Add sport if it is checked i.e.) == TRUE!
					if (checked.valueAt(i))
						selectedItems.add(adapter.getItem(position));
				}

				outputStrArr = new String[selectedItems.size()];

				for (int i = 0; i < selectedItems.size(); i++) {
					outputStrArr[i] = selectedItems.get(i);
				}

				if (outputStrArr.length > 0) {
					Intent i = new Intent();
					Bundle bun = new Bundle();
					bun.putStringArray("userid", outputStrArr);
					i.putExtra("share", bun);
					setResult(-1, i);
					finish();
				} else {
					Toast.makeText(context, "Kindly select buddy",
							Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	public void onClick(View v) {
		SparseBooleanArray checked = listView.getCheckedItemPositions();
		ArrayList<String> selectedItems = new ArrayList<String>();
		for (int i = 0; i < checked.size(); i++) {
			// Item position in adapter
			int position = checked.keyAt(i);
			// Add sport if it is checked i.e.) == TRUE!
			if (checked.valueAt(i)) {
				selectedItems.add(adapter.getItem(position));
			}
		}

		outputStrArr = new String[selectedItems.size()];

		for (int i = 0; i < selectedItems.size(); i++) {
			outputStrArr[i] = selectedItems.get(i);
		}

	}

}
