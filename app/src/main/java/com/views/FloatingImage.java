package com.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;


public class FloatingImage extends LinearLayout implements OnTouchListener {
	WindowManager windowManager; // to hold our image on screen
	Context ctx; // context so in case i use it somewhere.
	GestureDetector gestureDetector; // to detect some listener on the image.
	WindowManager.LayoutParams params; // layoutParams where i set the image height/width and other.
	WindowManager.LayoutParams paramsF;
	int initialX;
	int initialY;
	float initialTouchX;
	float initialTouchY;
	CallButtonCallBack serviceCallbacks;
	/**
	 * @param context
	 */
	public FloatingImage(Context context) {
		super(context);
		this.ctx = context;
		this.setOnTouchListener(this); // setting touchListener to the imageView
//		this.setImageResource(R.drawable.home); // setting icon to the imageView
//		gestureDetector = new GestureDetector(ctx, new GestureListener());
//		windowManager = (WindowManager) ctx.getSystemService("window"); // ini the windowManager
//		params = new WindowManager.LayoutParams(
//				WindowManager.LayoutParams.WRAP_CONTENT,
//				WindowManager.LayoutParams.WRAP_CONTENT,
//				PixelFormat.TRANSLUCENT); // assigning height/width to the imageView
//		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//				| WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
//				| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM; // assigning some flags to the layout
//		params.gravity = Gravity.TOP | Gravity.RIGHT; // setting the gravity of the imageView
//		params.windowAnimations = android.R.style.Animation_Toast; // adding an animation to it.
//		params.x = 0; // horizontal location of imageView
//		params.y = 100; // vertical location of imageView
//		params.height = 120; // given it a fixed height in case of large image
//		params.width = 120; // given it a fixed width in case of large image
//		params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
//		windowManager.addView(this, params); // adding the imageView & the  params to the WindowsManger.

		try {
			Log.i("Float", "FloatingImage");
			this.setBackgroundResource(R.drawable.minimize_lay);
			this.setOrientation(LinearLayout.HORIZONTAL);

			ImageView imageView = new ImageView(context);
			imageView.setBackgroundResource(R.drawable.ic_action_call);
			imageView.setLayoutParams(new ViewGroup.LayoutParams(70, 70));


			Chronometer chronometer = new Chronometer(context);
			chronometer.setTextSize(20);
//		chronometer.setBase(SystemClock.elapsedRealtime() + 18610953);
			chronometer.start();
			chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    chronometer.setText(AppReference.mainContext.call_chronometer_time);
                }
            });
//       LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        convertView = inflater.inflate(R.layout.group_chat_row,
//                null);

			this.addView(chronometer, 0);
			this.addView(imageView, 0);

			gestureDetector = new GestureDetector(ctx, new GestureListener());
			windowManager = (WindowManager) ctx.getSystemService("window"); // ini the windowManager
			params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    PixelFormat.TRANSLUCENT); // assigning height/width to the imageView
			params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                    | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM; // assigning some flags to the layout
			params.gravity = Gravity.TOP | Gravity.RIGHT; // setting the gravity of the imageView
			params.windowAnimations = android.R.style.Animation_Toast; // adding an animation to it.
			params.x = 0; // horizontal location of imageView
			params.y = 200; // vertical location of imageView
			params.height = LayoutParams.WRAP_CONTENT; // given it a fixed height in case of large image
			params.width = LayoutParams.WRAP_CONTENT; // given it a fixed width in case of large image
			params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
			windowManager.addView(this, params); // adding the imageView & the  params to the WindowsManger.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public FloatingImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public FloatingImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param v
	 * @param event
	 * @return true/false
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		paramsF = params; // getting the layout params from the current one and assigning new one.
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			paramsF = params;
			initialX = paramsF.x; // Horizontal location of the ImageView
			initialY = paramsF.y; // Vertical location of the ImageView
			initialTouchX = event.getRawX(); //X coordinate  location of the ImageView
			initialTouchY = event.getRawY(); //Y coordinate  location of the ImageView
			break;
		case MotionEvent.ACTION_UP: // this called when we actually leave the Imageview
			break;
		case MotionEvent.ACTION_MOVE:

//			paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
			paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
			windowManager.updateViewLayout(this, paramsF);
			break;
		}
		return false; // returning false otherwise any touch event on the imageView wont work.
	}

	private class GestureListener extends
			GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {// When there is a touch event on the imageView
			return true;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) { // perform Double tap on the ImageView
			Toast.makeText(ctx, "Hi You Double Tap Me", Toast.LENGTH_SHORT).show();
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) { // perform single tap on the ImageView
			Toast.makeText(ctx, "Hi You Single Tap Me", Toast.LENGTH_SHORT).show();
			serviceCallbacks.buttonClickState();
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) { // perform long press on the ImageView
			Toast.makeText(ctx, "Hi You Long Tap Me", Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * return [Remove the FloatingImage from the windowManager]
	 */
	public void destroy(){
		if(windowManager != null){ // if the image still exists on the WindowManager release it.
			windowManager.removeView(this); // remove the ImageView
		}
	}

	public void setCallbacks(CallButtonCallBack callbacks) {
		serviceCallbacks = callbacks;
	}
	public interface CallButtonCallBack {

		public void buttonClickState();
	}
}