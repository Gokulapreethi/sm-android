package com.cg.account;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class CustomViewFlipper extends ViewFlipper {

	public CustomViewFlipper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onDetachedFromWindow() {
		try {
			super.onDetachedFromWindow();
		} catch (IllegalArgumentException e) {

			stopFlipping();
		}
	}

}
