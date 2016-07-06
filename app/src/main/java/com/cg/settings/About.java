package com.cg.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.callservices.AudioCallScreen;
import com.cg.callservices.VideoCallScreen;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.main.SettingsFragment;
import com.util.SingleInstance;

public class About extends Fragment {
	TextView version;
	private static Context mainContext;
	private static About aboutfragment;
	public View view;

	public static About newInstance(Context context) {
		try {
			if (aboutfragment == null) {
				mainContext = context;
				aboutfragment = new About();

			}

			return aboutfragment;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return aboutfragment;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		try {
			Button select = (Button) getActivity().findViewById(R.id.btn_brg);
			select.setVisibility(View.GONE);
			TextView title = (TextView) getActivity().findViewById(
					R.id.activity_main_content_title);
			title.setText("ABOUT");
			title.setVisibility(View.VISIBLE);

			Button imVw = (Button) getActivity().findViewById(R.id.im_view);
			imVw.setVisibility(View.GONE);
			Button edit = (Button) getActivity().findViewById(
					R.id.btn_settings);
			edit.setVisibility(View.GONE);
			final EditText search_box = (EditText)getActivity().findViewById(R.id.search_box);
			search_box.setVisibility(View.GONE);
			final RelativeLayout mainHeader = (RelativeLayout) getActivity().findViewById(R.id.mainheader);
			mainHeader.setVisibility(View.VISIBLE);
			LinearLayout contact_layout = (LinearLayout) getActivity()
					.findViewById(R.id.contact_layout);
			contact_layout.setVisibility(View.GONE);
			Button plusBtn = (Button) getActivity()
					.findViewById(R.id.add_group);
			plusBtn.setVisibility(View.GONE);
			Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
			backBtn.setVisibility(View.VISIBLE);
			RelativeLayout audio_minimize = (RelativeLayout)getActivity().findViewById(R.id.audio_minimize);
			RelativeLayout video_minimize = (RelativeLayout)getActivity().findViewById(R.id.video_minimize);
			audio_minimize.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mainHeader.setVisibility(View.GONE);
					addShowHideListener(true);
				}
			});
			video_minimize.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mainHeader.setVisibility(View.GONE);
					addShowHideListener(false);
				}
			});
			backBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					SettingsFragment settingsFragment = SettingsFragment.newInstance(mainContext);
					FragmentManager fragmentManager = SingleInstance.mainContext
							.getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(
							R.id.activity_main_content_fragment, settingsFragment)
							.commitAllowingStateLoss();
				}
			});
			view = null;
			if (view == null) {
				view = inflater.inflate(R.layout.aboutpage, null);
				version = (TextView) view.findViewById(R.id.textView1);
				version.setText(SingleInstance.mainContext.getString(R.string.app_version));

		   } else {
			((ViewGroup) view.getParent()).removeView(view);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return view;

	}
	void addShowHideListener( final Boolean isAudio) {
		if(isAudio) {
			AudioCallScreen audioCallScreen = AudioCallScreen.getInstance(SingleInstance.mainContext);
			FragmentManager fragmentManager = SingleInstance.mainContext
					.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(
					R.id.activity_main_content_fragment, audioCallScreen)
					.commitAllowingStateLoss();
		}else {
			VideoCallScreen videoCallScreen = VideoCallScreen.getInstance(SingleInstance.mainContext);
			FragmentManager fragmentManager = SingleInstance.mainContext
					.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(
					R.id.activity_main_content_fragment, videoCallScreen)
					.commitAllowingStateLoss();
		}
	}


}
