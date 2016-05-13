package com.cg.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.CalendarView;

import com.cg.snazmed.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CalendarActivity extends Activity {
    CalendarView cal;
    private int mYear;
    private int mMonth;
    private int mDay;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.calendar);

        cal = (CalendarView) findViewById(R.id.calendarView1);

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub
//                Toast.makeText(getBaseContext(), "Selected Date is\n\n"
//                                + dayOfMonth + " : " + month + " : " + year,
//                        Toast.LENGTH_LONG).show();

                try {
                    mDay = dayOfMonth;
                    mMonth = month;
                    mYear = year;
                    Calendar c = Calendar.getInstance();
                    c.set(mYear, mMonth, mDay);
                    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                    String selectedDate = df.format(c.getTime());
                    Date date = df.parse(selectedDate);
                    SimpleDateFormat outFormat = new SimpleDateFormat("EEE");
                    String goal = outFormat.format(date);
                    Intent intent = new Intent(CalendarActivity.this, DateView.class);
                    intent.putExtra("date", selectedDate);
                    intent.putExtra("day", goal);
                    startActivity(intent);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
