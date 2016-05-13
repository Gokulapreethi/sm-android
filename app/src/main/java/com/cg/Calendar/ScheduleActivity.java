package com.cg.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.snazmed.R;
import com.util.SingleInstance;

import java.util.Calendar;


public class ScheduleActivity extends Activity {

    TextView date_tv;
    Button back;
    private Context context;
    LinearLayout mainLayout,layout;
    String eventdate, etitle,sTime,eTime;
    private ScheduleBean schduleBean=new ScheduleBean();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.scheduleview);
        context=this;
        final EditText title=(EditText)findViewById(R.id.ed_title);
        final TextView startTime=(TextView)findViewById(R.id.ed_startTime);
        final TextView endTime=(TextView)findViewById(R.id.ed_endTime);
        Button save=(Button)findViewById(R.id.save);
        Button back=(Button)findViewById(R.id.cancel);
        eventdate=getIntent().getStringExtra("selectedDate");
        etitle=getIntent().getStringExtra("title");
        sTime=getIntent().getStringExtra("starttime");
        eTime=getIntent().getStringExtra("endtime");
        Boolean isClicked=getIntent().getBooleanExtra("eventclick",false);
        if(isClicked) {
            title.setText(etitle);
            startTime.setText(sTime);
            endTime.setText(eTime);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(ScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(ScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().trim()!=null && startTime.getText().toString().trim()!=null &&
                        endTime.getText().toString().trim()!=null) {
                    String[] name= CallDispatcher.LoginUser.split("@");
                    schduleBean.setOwner(name[0]);
                    schduleBean.setTitle(title.getText().toString().trim());
                    schduleBean.setStartTime(startTime.getText().toString().trim());
                    schduleBean.setEndTime(endTime.getText().toString().trim());
                    schduleBean.setSceduleDate(eventdate);
                    DBAccess.getdbHeler(context).insertorUpdateScheduleEvents(
                            schduleBean);
                    finish();
                }else
                    showToast("Enter all fields");

            }
        });
//        String date=getIntent().getStringExtra("date");
//        String day=getIntent().getStringExtra("day");
//        date_tv=(TextView)findViewById(R.id.date_tv);
//        back=(Button)findViewById(R.id.cancel);
//        date_tv.setText(date);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                finish();
//            }
//        });
//        mainLayout = (LinearLayout ) findViewById(R.id.view);
////        draw();
//        for (int i = 0; i <=24; i++) {
//            layout = new LinearLayout(context);
//            layout.setOrientation(LinearLayout.HORIZONTAL);
////            draw();
//            TextView dynamicTextView = new TextView(this);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new AbsListView.LayoutParams(35, AbsListView.LayoutParams.WRAP_CONTENT));
//            dynamicTextView.setTextSize(8.0f);
//            params.setMargins(10, 10, 0, 15);
//            dynamicTextView.setLayoutParams(params);
//            int j=i%12;
//            dynamicTextView.setText(String.valueOf(j));
//            if(i/12 == 1)
//            {
//                if(j==0)
//                    dynamicTextView.setText("12 PM");
//                else
//                    dynamicTextView.setText(j+ " PM");
//            }
//            else
//            {
//                if(j==0)
//                    dynamicTextView.setText("12 AM");
//                else
//                    dynamicTextView.setText(j+ " AM");
//            }
//            layout.addView(dynamicTextView);
//            View v = new View(this);
//            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, 1));
//            params1.setMargins(20, 20, 10, 15);
//            v.setLayoutParams(params1);
//            v.setBackgroundColor(Color.GRAY);
//            layout.addView(v);
//            mainLayout.addView(layout);
//            for (int k=1; k<=4; k++) {
//                View v1 = new View(this);
//                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, 1));
//                params2.setMargins(60, 10, 10, 15);
//                v1.setLayoutParams(params2);
//                v1.setBackgroundColor(Color.GREEN);
//                mainLayout.addView(v1);
//            }
//            }
//        String[] starttime=new String[]{"01","30","PM"};
//        float startHour= Float.parseFloat(starttime[0]);
//        float startMin= Float.parseFloat(starttime[1]);
//        String starttimeConv=starttime[2];
//        if(starttimeConv.equalsIgnoreCase("PM") && startHour!=12)
//        {
//            startHour+=12;
//        }
//        else if(starttimeConv.equalsIgnoreCase("AM") && startHour== 12)
//        {
//            startHour=0;
//        }
//        startMin/=60.0f;
//         sTime = startHour+startMin;
//
//        String[] endtime=new String[]{"02","30","PM"};
//        float endHour= Float.parseFloat(endtime[0]);
//        float endMin= Float.parseFloat(endtime[1]);
//        String endtimeConv=endtime[2];
//        if(endtimeConv.equalsIgnoreCase("PM") && endHour!=12)
//        {
//            endHour+=12;
//        }
//        else if(endtimeConv.equalsIgnoreCase("AM") && endHour== 12)
//        {
//            endHour=0;
//        }
//        endMin/=60.0f;
//         eTime = endHour+endMin;
//        Log.i("AAAA", "Date Activity " + sTime + " " + eTime);
    }
    private void draw() {
        int width = 250;
        int height = 100;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawColor(Color.CYAN);
        canvas.drawRect(100, 500, 0, 100, paint);
        ImageView imageView = new ImageView(this);

        imageView.setImageBitmap(bitmap);

        mainLayout.addView(imageView);
    }
    private void showToast(final String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
