package com.main;

import java.util.ArrayList;

import org.lib.model.RecordTransactionBean;

import com.callHistory.CallHistoryActivity;
import com.callHistory.CallHistoryAdapter;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.util.SingleInstance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class CallHistoryFragment extends Fragment {

	private static CallHistoryFragment callHistoryFragment;
	private static Context mainContext;
	private static CallDispatcher callDisp;
	public View _rootView;
	private ProgressDialog progress = null;
	public CallHistoryAdapter adapter;
	private ArrayList<RecordTransactionBean> mlist;
	private String sessionId = "";
	private Handler handler = new Handler();
	
	
	public static CallHistoryFragment newInstance(Context context) {
		try {
			if (callHistoryFragment == null) {
				mainContext = context;
				callHistoryFragment = new CallHistoryFragment();
				callDisp = CallDispatcher.getCallDispatcher(mainContext);
			}

			return callHistoryFragment;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return callHistoryFragment;
		}
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
		Button select = (Button) getActivity().findViewById(R.id.btn_brg);
		select.setVisibility(View.GONE);
			RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
			mainHeader.setVisibility(View.VISIBLE);
		LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
		contact_layout.setVisibility(View.VISIBLE);
		final Button all_contact = (Button) getActivity().findViewById(
				R.id.all_contacts);
		all_contact.setText(SingleInstance.mainContext.getResources().getString(R.string.chat_history_tab));
			all_contact.setVisibility(View.GONE);
		final Button pending_contact = (Button) getActivity().findViewById(
				R.id.pending_contacts);
		pending_contact.setText(SingleInstance.mainContext.getResources().getString(R.string.call_history_tab));

		Button imVw = (Button) getActivity().findViewById(R.id.im_view);
		imVw.setVisibility(View.GONE);		
		
		Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
		backBtn.setVisibility(View.GONE);
		TextView title = (TextView) getActivity().findViewById(
				R.id.activity_main_content_title);
		title.setText(getResources().getString(R.string.callhistory));
		title.setVisibility(View.GONE);
		
		Button clearall = (Button) getActivity().findViewById(R.id.add_group);
		clearall.setVisibility(View.GONE);
		clearall.setText(SingleInstance.mainContext.getResources().getString(
				R.string.clear_all));
		clearall.setTextColor(Color.parseColor("#ffffff"));
		clearall.setBackgroundResource(R.color.blue2);
		Button plusbtn = (Button) getActivity().findViewById(
				R.id.btn_settings);
		plusbtn.setVisibility(View.GONE);
		 _rootView = null;
				if (_rootView == null) {
					_rootView = inflater.inflate(R.layout.call_history, null);
					getActivity().getWindow().setSoftInputMode(
							WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
					try {
						
						Button selectall = (Button) getActivity().findViewById(
								R.id.btn_brg);
						selectall.setVisibility(View.GONE);

						if (WebServiceReferences.callDispatch
								.containsKey("calldisp"))
							callDisp = (CallDispatcher) WebServiceReferences.callDispatch
									.get("calldisp");
						else
							callDisp = new CallDispatcher(mainContext);

						mlist = new ArrayList<RecordTransactionBean>();
						final ListView listView = (ListView) _rootView.findViewById(R.id.listview);
						String query = "";
						query="select * from recordtransactiondetails ORDER BY id DESC";
						mlist = DBAccess.getdbHeler().getcallhistorydetails(query);
						adapter = new CallHistoryAdapter(mainContext, mlist);				
						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();						
						
						clearall.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
																
									DBAccess.getdbHeler().deleteFromCallHistory(
											CallDispatcher.LoginUser) ;																			
								listView.setAdapter(null);	
								
							}
						});
						listView.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> arg0, View arg1,
									int position, long arg3) {
								try {
								Log.i("Test", "CallHistoryFragment>>>>>>>>>>");
								RecordTransactionBean bean=mlist.get(position);
								sessionId =bean.getSessionid();
								Intent intent = new Intent(mainContext,
										CallHistoryActivity.class);
								intent.putExtra("isviewed",true);
								intent.putExtra("sessionid",
										sessionId);
								startActivity(intent);
								} catch (Exception e) {									
									e.printStackTrace();
								}
							}
						});

							
					}  catch (Exception e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
					}
				} else {
					((ViewGroup) _rootView.getParent()).removeView(_rootView);
				}
				return _rootView;
		} catch(Exception e) {
			e.printStackTrace();
			return _rootView;
		}
		
	}
	public View getParentView() {
		return _rootView;
	}
	public void notifyUI() {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (adapter != null)
						adapter.notifyDataSetChanged();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
