package com.cg.utilities;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;

public class UtilityDistanceActivity extends Activity {
	private ListView lv = null;
	Context context = null;
	CallDispatcher callDisp = null;
	private HashMap<String, String> utilityDistance = new HashMap<String, String>();
	private Button back = null;
	private UtilityDistanceAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.utility_buddydistance);
		context = this;

		utilityDistance = (HashMap<String, String>) getIntent()
				.getSerializableExtra("distance");
		callDisp = (CallDispatcher) WebServiceReferences.callDispatch
				.get("calldisp");
		WebServiceReferences.contextTable.put("contactconf", context);
		adapter = new UtilityDistanceAdapter(utilityDistance, this);
		lv = (ListView) findViewById(R.id.contact_listview);
		back = (Button) findViewById(R.id.btn_Settings);
		lv.setAdapter(adapter);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }
}
