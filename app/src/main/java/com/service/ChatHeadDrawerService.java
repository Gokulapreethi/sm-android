package com.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.main.AppMainActivity;

import org.video.Preview;

public class ChatHeadDrawerService extends Service {

    private WindowManager mWindowManager;
    private View mChatHead;
    private ImageView mChatHeadImageView;
    private RelativeLayout mLayout;
    private static int screenWidth;
    private static int screenHeight;
    GestureDetector gestureDetector;
    int selected_view = 0;
    private boolean newly_started = false;
    private String current_callscrren;
    private String current_window;
    private Chronometer call_time_chronometer;
    private FrameLayout video_frame_layout;
    private Preview video_preview;
    private String call_type = "AC";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();


        try {
            newly_started = true;
            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = mWindowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;

            LayoutInflater inflater = LayoutInflater.from(this);
            mChatHead = inflater.inflate(R.layout.chathead_view, null);
            mChatHeadImageView = (ImageView) mChatHead
                    .findViewById(R.id.chathead_imageview);
            mLayout = (RelativeLayout) mChatHead
                    .findViewById(R.id.chathead_linearlayout);
            call_time_chronometer = (Chronometer) mChatHead.findViewById(R.id.video_timer);
            video_frame_layout = (FrameLayout)mChatHead.findViewById(R.id.mimimized_videoview);

            Log.i("Chronometer", "time" + (AppReference.mainContext.ctimer.getBase()));
//            call_time_chronometer.setBase(AppReference.mainContext.ctimer.getBase());
//            call_time_chronometer.start();
//            call_time_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
//                @Override
//                public void onChronometerTick(Chronometer chronometer) {
//                    Log.i("Chronometer", "chronometer :" + chronometer.getText() + " " + AppReference.mainContext.call_chronometer_time);
//
//                    chronometer.setText(chronometer.getText());
//                }
//            });
            final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT, // Width
                    WindowManager.LayoutParams.WRAP_CONTENT, // Height
                    WindowManager.LayoutParams.TYPE_PHONE, // Type
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // Flag
                    PixelFormat.TRANSLUCENT // Format
            );

            parameters.x = 0;
            parameters.y = 250;
            parameters.gravity = Gravity.TOP | Gravity.RIGHT;
            gestureDetector = new GestureDetector(ChatHeadDrawerService.this, new GestureListener());
            // Drag support!
            mLayout.setOnTouchListener(new OnTouchListener() {

                int initialX, initialY;
                float initialTouchX, initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = parameters.x;
                            initialY = parameters.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            //					mChatHeadTextView.setVisibility(View.GONE);
                            parameters.x = initialX
                                    + (int) (event.getRawX() - initialTouchX);
                            parameters.y = initialY
                                    + (int) (event.getRawY() - initialTouchY);
                            mWindowManager.updateViewLayout(mChatHead, parameters);
                            break;
                        case MotionEvent.ACTION_UP:

                            //					if (parameters.y > screenHeight * 0.6) {
                            //						mChatHead.setVisibility(View.GONE);
                            //						Toast.makeText(getApplication(), "Removed!",
                            //								Toast.LENGTH_SHORT).show();
                            //						stopSelf();
                            //					}
                            if (parameters.x < screenWidth / 2) {
                                mLayout.removeAllViews();
                                mLayout.addView(mChatHeadImageView);
                                if (selected_view == 2) {
                                    mLayout.addView(call_time_chronometer);
                                    if (current_callscrren != null && current_callscrren.equalsIgnoreCase("ACS")) {
                                        if(call_type.equalsIgnoreCase("VC")) {
                                            AppMainActivity.commEngine.stopPreview();
                                            video_frame_layout.removeView(video_preview);
                                            video_preview = null;
                                            video_preview = AppMainActivity.commEngine.getVideoPreview(ChatHeadDrawerService.this);

                                            video_frame_layout.addView(video_preview);
                                            mLayout.addView(video_frame_layout);
                                        }
                                    } else {
                                        AppMainActivity.commEngine.stopPreview();
                                        video_frame_layout.removeView(video_preview);
                                        video_preview = null;
                                        video_preview = AppMainActivity.commEngine.getVideoPreview(ChatHeadDrawerService.this);

                                        video_frame_layout.addView(video_preview);
                                        mLayout.addView(video_frame_layout);
                                    }
                                }

                            } else { // Set textView to left of image
                                mLayout.removeAllViews();
                                //						mLayout.addView(mChatHeadTextView);
                                mLayout.addView(mChatHeadImageView);
                                if (selected_view == 2) {
                                    mLayout.addView(call_time_chronometer);
                                    if (current_callscrren != null && current_callscrren.equalsIgnoreCase("ACS")) {
                                        if(call_type.equalsIgnoreCase("VC")) {
                                            AppMainActivity.commEngine.stopPreview();
                                            video_frame_layout.removeView(video_preview);
                                            video_preview = null;
                                            video_preview = AppMainActivity.commEngine.getVideoPreview(ChatHeadDrawerService.this);

                                            video_frame_layout.addView(video_preview);
                                            mLayout.addView(video_frame_layout);
                                        }
                                    } else {
                                        AppMainActivity.commEngine.stopPreview();
                                        video_frame_layout.removeView(video_preview);
                                        video_preview = null;
                                        video_preview = AppMainActivity.commEngine.getVideoPreview(ChatHeadDrawerService.this);

                                        video_frame_layout.addView(video_preview);
                                        mLayout.addView(video_frame_layout);
                                    }
                                }
                                //						mChatHeadTextView.setVisibility(View.VISIBLE);
                            }
                            break;
                        default:
                            return false;


                    }
                    return gestureDetector.onTouchEvent(event);
                }
            });

//        mChatHead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ChatHeadDrawerService.this, "Button Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
            mWindowManager.addView(mChatHead, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (newly_started) {
            newly_started = false;
            selected_view = intent.getIntExtra("sview", 0);
            if (selected_view == 0) {
                current_window = "incomingalert";
                call_time_chronometer.setVisibility(View.GONE);
                mChatHeadImageView.setImageResource(R.drawable.min_in_call);
            } else if (selected_view == 1) {
                current_window = "connecting";
                call_time_chronometer.setVisibility(View.GONE);
                mChatHeadImageView.setImageResource(R.drawable.min_out_call);
            } else if (selected_view == 2) {
                current_window = "callscreen";
                current_callscrren = intent.getStringExtra("callscreen");
                call_time_chronometer.setVisibility(View.VISIBLE);
                if (current_callscrren != null && current_callscrren.equalsIgnoreCase("ACS")) {
                    call_type = intent.getStringExtra("calltype");
                    call_time_chronometer.setBase(AppReference.mainContext.ctimer.getBase());
                    mChatHeadImageView.setImageResource(R.drawable.ic_action_call);
                    if(call_type.equalsIgnoreCase("VC")) {
                        AppMainActivity.commEngine.stopPreview();
                        video_preview = AppMainActivity.commEngine.getVideoPreview(this);
                        video_frame_layout.addView(video_preview);
                    }
                } else {
                    call_time_chronometer.setBase(AppReference.mainContext.cvtimer.getBase());
                    mChatHeadImageView.setImageResource(R.drawable.ic_action_video_white);
                    AppMainActivity.commEngine.stopPreview();
                    video_preview = AppMainActivity.commEngine.getVideoPreview(this);
                    video_frame_layout.addView(video_preview);
                }
                mLayout.setBackgroundResource(R.drawable.minimize_lay);

                call_time_chronometer.start();
                call_time_chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        Log.i("Chronometer", "chronometer :" + chronometer.getText() + " " + AppReference.mainContext.call_chronometer_time);

                        chronometer.setText(chronometer.getText());
                    }
                });
            }
        }
        return START_NOT_STICKY;
    }

    private class GestureListener extends
            GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {// When there is a touch event on the imageView
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) { // perform Double tap on the ImageView
//            Toast.makeText(ChatHeadDrawerService.this, "Hi You Double Tap Me", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) { // perform single tap on the ImageView
//            Toast.makeText(ChatHeadDrawerService.this, "Hi You Single Tap Me", Toast.LENGTH_SHORT).show();
            if (selected_view == 2) {
                if (current_callscrren != null && current_callscrren.equalsIgnoreCase("ACS")) {
                    if(call_type.equalsIgnoreCase("VC")) {
                        AppMainActivity.commEngine.stopPreview();
                    }
                } else {
                    AppMainActivity.commEngine.stopPreview();
                }
            }
            AppReference.mainContext.minimizeButtonClickState(current_window, current_callscrren);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) { // perform long press on the ImageView
//            Toast.makeText(ChatHeadDrawerService.this, "Hi You Long Tap Me", Toast.LENGTH_SHORT).show();
        }
    }

    public interface CallMinimizedButtonCallBack {

        public void minimizeButtonClickState(String current_screen, String callscreen);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call_type != null) {
            call_type = "AC";
        }

        if (mChatHead != null)
            mWindowManager.removeView(mChatHead);
    }
}
