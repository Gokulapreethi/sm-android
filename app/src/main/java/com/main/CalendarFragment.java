package com.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.Calendar.DateView;
import com.cg.snazmed.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CalendarFragment extends Fragment {
    private static CalendarFragment calendarFragment;
    private static Context mainContext;
    public View view;
    CalendarView cal;
    private int mYear;
    private int mMonth;
    private int mDay;
    Calendar c = Calendar.getInstance();
    int prevDay = c.get(Calendar.DAY_OF_MONTH);
    int prevMonth = c.get(Calendar.MONTH);
    int prevYear = c.get(Calendar.YEAR);
    public static CalendarFragment newInstance(Context context) {
        try {
            if (calendarFragment == null) {
                mainContext = context;
                calendarFragment = new CalendarFragment();
                calendarFragment.setContext(context);

            }

            return calendarFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return calendarFragment;
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
            title.setText("Calendar");
            title.setVisibility(View.VISIBLE);

            Button imVw = (Button) getActivity().findViewById(R.id.im_view);
            imVw.setVisibility(View.GONE);
            Button edit = (Button) getActivity().findViewById(
                    R.id.btn_settings);
            edit.setVisibility(View.GONE);
            RelativeLayout mainHeader = (RelativeLayout) getActivity().findViewById(R.id.mainheader);
            mainHeader.setVisibility(View.VISIBLE);
            LinearLayout contact_layout = (LinearLayout) getActivity()
                    .findViewById(R.id.contact_layout);
            contact_layout.setVisibility(View.GONE);
            Button plusBtn = (Button) getActivity()
                    .findViewById(R.id.add_group);
            plusBtn.setVisibility(View.GONE);
            Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
            backBtn.setVisibility(View.GONE);
            view = null;
            if (view == null) {
                view = inflater.inflate(R.layout.calendar, null);
                cal = (CalendarView)view. findViewById(R.id.calendarView1);

                cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month,
                                                    int dayOfMonth) {
                        // TODO Auto-generated method stub
                        mDay = dayOfMonth;
                        mMonth = month;
                        mYear = year;
                        try {
                            Calendar c = Calendar.getInstance();
                            c.set(mYear, mMonth, mDay);
                            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                            String selectedDate = df.format(c.getTime());
                            Date date = df.parse(selectedDate);
                            SimpleDateFormat outFormat = new SimpleDateFormat("EEE");
                            String goal = outFormat.format(date);
                            if (changeUpdate(mYear, mMonth, mDay)) {
                                prevDay=mDay;
                                prevMonth=mMonth;
                                prevYear=mYear;
                                Intent intent = new Intent(getActivity().getApplicationContext(), DateView.class);
                                intent.putExtra("date", selectedDate);
                                intent.putExtra("day", goal);
                                getActivity().startActivity(intent);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } else {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    public View getParentView() {
        return view;
    }
    public void setContext(Context cxt) {
        this.mainContext = cxt;
    }
    private boolean changeUpdate(int curYear, int curMonth, int curDay) {
        boolean changed = false;

        if (curDay != prevDay || curMonth != prevMonth || curYear != prevYear) {
            changed = true;
        }
        return changed;
    }
}
