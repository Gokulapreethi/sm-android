package com.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.utilities.CustomList;
import com.cg.utilities.UtilityBuyer;
import com.cg.utilities.UtilitySeller;
import com.cg.utilities.UtilityServiceNeeder;
import com.cg.utilities.UtilityServiceProvider;
import com.process.MemoryProcessor;
import com.util.SingleInstance;

public class UtilityFragment extends Fragment {
	ListView list;
	// Context context;
	// private SlideMenu slidemenu;
	private Handler handler = new Handler();
	CallDispatcher callDisp;
		Integer[] imageId = { R.drawable.go_forward, R.drawable.go_forward,
			R.drawable.go_forward, R.drawable.go_forward,

	};

	// private Button btn_im = null;

	private static UtilityFragment utilityFragment;

	private static Context context;

	// this button plus hide in this page,this
	// button create fragment xml

	public View view;

	public static UtilityFragment newInstance(Context maincontext) {
		try {
			if (utilityFragment == null) {
				context = maincontext;
				utilityFragment = new UtilityFragment();
			}

			return utilityFragment;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return utilityFragment;
		}
	}

	Button selectall;
	Button imView;
	Button settings;
	Button plus = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Button select = (Button) getActivity().findViewById(R.id.btn_brg);
		select.setVisibility(View.GONE);
		LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
		contact_layout.setVisibility(View.GONE);
		TextView title = (TextView) getActivity().findViewById(
				R.id.activity_main_content_title);
		
		title.setVisibility(View.VISIBLE);

		Button imVw = (Button) getActivity().findViewById(R.id.im_view);
		imVw.setVisibility(View.GONE);

		Button setting = (Button) getActivity().findViewById(R.id.btn_settings);
		setting.setVisibility(View.GONE);

		Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
		plusBtn.setVisibility(View.GONE);

		if (view == null) {
			view = inflater.inflate(R.layout.utilityxml, null);
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			try {

				// super.onCreate(savedInstanceState);
				// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
				// setContentView(R.layout.utility_options);
				// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				// R.layout.utility_options);
				// context = this;
				WebServiceReferences.contextTable.put("IM", context);

				// DisplayMetrics displaymetrics = new DisplayMetrics();
				// getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				// int noScrHeight = displaymetrics.heightPixels;
				// int noScrWidth = displaymetrics.widthPixels;

				if (WebServiceReferences.callDispatch.containsKey("calldisp"))
					callDisp = (CallDispatcher) WebServiceReferences.callDispatch
							.get("calldisp");
				else
					callDisp = new CallDispatcher(context);

				// callDisp.setNoScrHeight(noScrHeight);
				// callDisp.setNoScrWidth(noScrWidth);
				// displaymetrics = null;
				// ShowList();
				// btn_im = (Button) findViewById(R.id.util_im);
				// btn_im.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// callDisp.openReceivedIm(v, context);
				// }
				// });

				WebServiceReferences.contextTable.put("utility", context);
				// setContentView(R.layout.utilityxml);
				

				// Button btn_Settings = (Button)
				// findViewById(R.id.btn_Settings);
				//
				// btn_Settings.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// slidemenu.show();
				// }
				// });
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			((ViewGroup) view.getParent()).removeView(view);
		}
		String[] web = {SingleInstance.mainContext.getResources().getString(R.string.buyer), SingleInstance.mainContext.getResources().getString(R.string.seller), SingleInstance.mainContext.getResources().getString(R.string.service_provider), SingleInstance.mainContext.getResources().getString(R.string.service_needed),

		};

		CustomList adapter = new CustomList(getActivity(), web, imageId);
		list = (ListView) view.findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int postion, long arg3) {
				// TODO Auto-generated method stub
				if (postion == 0) {
//					Intent buyer_intent = new Intent(getActivity()
//							.getApplicationContext(),
//							UtilityBuyerNew.class);
					 Intent buyer_intent = new Intent(getActivity()
					 .getApplicationContext(),
					 UtilityBuyer.class);
					getActivity().startActivity(buyer_intent);
				} else if (postion == 1) {
					Intent seller_intent = new Intent(getActivity()
							.getApplicationContext(),
							UtilitySeller.class);
					getActivity().startActivity(seller_intent);
				} else if (postion == 2) {
					Intent seller_intent = new Intent(getActivity()
							.getApplicationContext(),
							UtilityServiceProvider.class);
					getActivity().startActivity(seller_intent);
				} else if (postion == 3) {
					Intent seller_intent = new Intent(context,
							UtilityServiceNeeder.class);
					context.startActivity(seller_intent);
				}
			}
		});
		return view;
	}

	// protected void ShowList() {
	// try {
	// // TODO Auto-generated method stub
	//
	// setContentView(R.layout.history_container);
	//
	// slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
	// ArrayList<Slidebean> datas = new ArrayList<Slidebean>();
	//
	// callDisp.composeList(datas);
	// slidemenu.init(UtilityFragment.this, datas, UtilityFragment.this,
	// 100);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	//
	public View getParentView() {
		return view;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if (WebServiceReferences.contextTable
							.containsKey("utility"))
						WebServiceReferences.contextTable.remove("utility");
					MemoryProcessor.getInstance().unbindDrawables(view);
					view = null;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

	// public void notifyProfilepictureDownloaded() {
	// handler.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// if (slidemenu != null) {
	// if (slidemenu.isMenuShowing())
	// slidemenu.refreshItem();
	// }
	// }
	// });
	//
	// }

	private String getCurrentDateTime() {
		try {
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
			return sdf.format(curDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// @Override
	// public void onSlideMenuItemClick(int itemId, View v, Context context) {
	// // TODO Auto-generated method stub
	// switch (itemId) {
	// case WebServiceReferences.CONTACTS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.USERPROFILE:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.UTILITY:
	//
	// break;
	// case WebServiceReferences.NOTES:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	// case WebServiceReferences.APPS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.CLONE:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.SETTINGS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	//
	// case WebServiceReferences.QUICK_ACTION:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.FORMS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	// case WebServiceReferences.FEEDBACK:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	// case WebServiceReferences.EXCHANGES:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	// default:
	// break;
	// }
	// }

	// @Override
	// public void notifyReceivedIM(final SignalingBean sb) {
	// // TODO Auto-generated method stub
	// handler.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// btn_im.setVisibility(View.VISIBLE);
	// btn_im.setEnabled(true);
	//
	// btn_im.setBackgroundResource(R.drawable.small_blue_balloon);
	//
	// if (!callDisp.getdbHeler(context).userChatting(sb.getFrom())) {
	// callDisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
	// CallDispatcher.LoginUser, 1,
	// CallDispatcher.LoginUser);
	// }
	//
	// }
	// });
	// }

	// @Override
	// public void onResume() {
	// // TODO Auto-generated method stub
	// if (WebServiceReferences.Imcollection.size() == 0)
	// btn_im.setVisibility(View.GONE);
	// else
	// btn_im.setVisibility(View.VISIBLE);
	// super.onResume();
	// }

}