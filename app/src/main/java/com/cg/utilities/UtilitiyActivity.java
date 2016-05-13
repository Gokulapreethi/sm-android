package com.cg.utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.lib.model.SignalingBean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.cg.SlideMenu.SlideMenu;
import com.cg.SlideMenu.SlideMenuInterface;
import com.cg.SlideMenu.Slidebean;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.instancemessage.IMNotifier;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class UtilitiyActivity extends Activity implements
		SlideMenuInterface.OnSlideMenuItemClickListener, IMNotifier {
	ListView list;
	Context context = null;
	private SlideMenu slidemenu;
	private Handler handler = new Handler();
	CallDispatcher callDisp;
	String[] web = {
			SingleInstance.mainContext.getResources().getString(R.string.buyer),
			SingleInstance.mainContext.getResources()
					.getString(R.string.seller),
			SingleInstance.mainContext.getResources().getString(
					R.string.service_provider),
			SingleInstance.mainContext.getResources().getString(
					R.string.service_needed),

	};
	Integer[] imageId = { R.drawable.go_forward, R.drawable.go_forward,
			R.drawable.go_forward, R.drawable.go_forward,

	};

	private Button btn_im = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.utility_options);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.utility_options);
			context = this;
			WebServiceReferences.contextTable.put("IM", this);

			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(context);

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);
			displaymetrics = null;
			ShowList();
			btn_im = (Button) findViewById(R.id.util_im);
			btn_im.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					callDisp.openReceivedIm(v, context);
				}
			});

			WebServiceReferences.contextTable.put("utility", this);
			setContentView(R.layout.utilityxml);
			CustomList adapter = new CustomList(UtilitiyActivity.this, web,
					imageId);
			list = (ListView) findViewById(R.id.list);
			list.setAdapter(adapter);

			Button btn_Settings = (Button) findViewById(R.id.btn_Settings);

			btn_Settings.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					slidemenu.show();
				}
			});
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int postion, long arg3) {
					// TODO Auto-generated method stub
					if (postion == 0) {
						Intent buyer_intent = new Intent(context,
								UtilityBuyer.class);
						context.startActivity(buyer_intent);
					} else if (postion == 1) {
						Intent seller_intent = new Intent(context,
								UtilitySeller.class);
						context.startActivity(seller_intent);
					} else if (postion == 2) {
						Intent seller_intent = new Intent(context,
								UtilityServiceProvider.class);
						context.startActivity(seller_intent);
					} else if (postion == 3) {
						Intent seller_intent = new Intent(context,
								UtilityServiceNeeder.class);
						context.startActivity(seller_intent);
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void ShowList() {
		try {
			// TODO Auto-generated method stub

			setContentView(R.layout.history_container);

			slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
			ArrayList<Slidebean> datas = new ArrayList<Slidebean>();

			callDisp.composeList(datas);
			slidemenu.init(UtilitiyActivity.this, datas, UtilitiyActivity.this,
					100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (WebServiceReferences.contextTable.containsKey("utility"))
			WebServiceReferences.contextTable.remove("utility");
		super.onDestroy();
	}

	public void notifyProfilepictureDownloaded() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (slidemenu != null) {
					if (slidemenu.isMenuShowing())
						slidemenu.refreshItem();
				}
			}
		});

	}

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

	@Override
	public void onSlideMenuItemClick(int itemId, View v, Context context) {
		// TODO Auto-generated method stub
		switch (itemId) {
		case WebServiceReferences.CONTACTS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.USERPROFILE:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.UTILITY:

			break;
		case WebServiceReferences.NOTES:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.APPS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.CLONE:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.SETTINGS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;

		case WebServiceReferences.QUICK_ACTION:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.FORMS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.FEEDBACK:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.EXCHANGES:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void notifyReceivedIM(final SignalingBean sb) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				btn_im.setVisibility(View.VISIBLE);
				btn_im.setEnabled(true);

				btn_im.setBackgroundResource(R.drawable.small_blue_balloon);

				if (!callDisp.getdbHeler(context).userChatting(sb.getFrom())) {
					callDisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
							CallDispatcher.LoginUser, 1,
							CallDispatcher.LoginUser);
				}

			}
		});
	}

	@Override
	protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
		if (WebServiceReferences.Imcollection.size() == 0)
			btn_im.setVisibility(View.GONE);
		else
			btn_im.setVisibility(View.VISIBLE);
	}

}