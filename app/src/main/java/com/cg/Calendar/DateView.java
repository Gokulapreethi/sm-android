package com.cg.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.util.SingleInstance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;


/**
 * Created by Raquib-ul-Alam Kanak on 7/21/2014.
 * Website: http://alamkanak.github.io/
 */
public class DateView extends Activity implements MonthLoader.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener {

	private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
			"Wed", "Thu", "Fri", "Sat" };



    private final String[] months = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };

	private static final int TYPE_DAY_VIEW = 1;
	private static final int TYPE_THREE_DAY_VIEW = 2;
	private static final int TYPE_WEEK_VIEW = 3;
	private int mWeekViewType = TYPE_THREE_DAY_VIEW;
	private WeekView mWeekView;


    private Button add,btn_back;
    private Context context;
    private String selected_date;
    private Handler handler;
    public static String selectedDate;
    public Vector<ScheduleBean> eventDetails = new Vector<ScheduleBean>();
    private static CallDispatcher callDisp;
    String tvdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dateview);
            context = this;

            // Get a reference for the week view in the layout.
            callDisp = new CallDispatcher(context);
            mWeekView = (WeekView) findViewById(R.id.weekView);
            String date = getIntent().getStringExtra("date");
            String day = getIntent().getStringExtra("day");
            TextView date_tv = (TextView) findViewById(R.id.date_tv);
            Button back = (Button) findViewById(R.id.cancel);
            Button plus = (Button) findViewById(R.id.plus);
            date_tv.setText(date);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DateView.this, ScheduleActivity.class);
                    intent.putExtra("selectedDate", selectedDate);
                    startActivity(intent);
                    finish();
                }
            });

            SimpleDateFormat month_date = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat datefor = new SimpleDateFormat("yyyy/MM/dd");
            Date d = null;

            try {
                d = month_date.parse(date);
                selected_date = datefor.format(d);
                selectedDate = datefor.format(d);

            } catch (Exception e) {
                e.printStackTrace();
            }
            eventDetails.clear();
            String[] name= CallDispatcher.LoginUser.split("@");
            eventDetails = DBAccess.getdbHeler()
                    .getEvents(name[0], selectedDate);
            Calendar c1 = today();

            Calendar first = (Calendar) c1.clone();
            first.add(Calendar.DAY_OF_WEEK,
                    first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));
            Date dateWithoutTime = c1.getTime();
            String toDay=dateWithoutTime.toString();
            String[] todayDate=toDay.split(" ");
            String[] weekdays=new String[7];
            TextView tv1= (TextView)findViewById(R.id.tv1);
            TextView tv2= (TextView)findViewById(R.id.tv2);
            TextView tv3= (TextView)findViewById(R.id.tv3);
            TextView tv4= (TextView)findViewById(R.id.tv4);
            TextView tv5= (TextView)findViewById(R.id.tv5);
            TextView tv6= (TextView)findViewById(R.id.tv6);
            TextView tv7= (TextView)findViewById(R.id.tv7);

            for (int i = 0; i < 7; i++) {
                dateWithoutTime = first.getTime();
                String today=dateWithoutTime.toString();
                String[] days=today.split(" ");
                weekdays[i]=days[2];
                first.add(Calendar.DAY_OF_WEEK, 1);
            }
            tv1.setText(weekdays[0]);
            tv2.setText(weekdays[1]);
            tv3.setText(weekdays[2]);
            tv4.setText(weekdays[3]);
            tv5.setText(weekdays[4]);
            tv6.setText(weekdays[5]);
            tv7.setText(weekdays[6]);
            if(todayDate[0].equalsIgnoreCase("sun"))
                tv1.setBackgroundColor(getResources().getColor(R.color.ash));
            else if(todayDate[0].equalsIgnoreCase("Mon"))
                tv2.setBackgroundColor(getResources().getColor(R.color.ash));
            else if(todayDate[0].equalsIgnoreCase("Tue"))
                tv3.setBackgroundColor(getResources().getColor(R.color.ash));
            else if(todayDate[0].equalsIgnoreCase("Wed"))
                tv4.setBackgroundColor(getResources().getColor(R.color.ash));
            else if(todayDate[0].equalsIgnoreCase("Thu"))
                tv5.setBackgroundColor(getResources().getColor(R.color.ash));
            else if(todayDate[0].equalsIgnoreCase("Fri"))
                tv6.setBackgroundColor(getResources().getColor(R.color.ash));
            else if(todayDate[0].equalsIgnoreCase("Sat"))
                tv7.setBackgroundColor(getResources().getColor(R.color.ash));

            // Show a toast message about the touched event.
            mWeekView.setOnEventClickListener(this);

            // The week view has infinite scrolling horizontally. We have to provide the events of a
            // month every time the month changes on the week view.
            mWeekView.setMonthChangeListener(this);

            // Set long press listener for events.
            mWeekView.setEventLongPressListener(this);

            // Set up a date time interpreter to interpret how the date and time will be formatted in
            // the week view. This is optional.
            setupDateTimeInterpreter(false);
            setReminder();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     //   int id = item.getItemId();
//        setupDateTimeInterpreter(id == R.id.action_week_view);
//        switch (id){
//            case R.id.action_today:
//                mWeekView.goToToday();
//                return true;
//            case R.id.action_day_view:
//                if (mWeekViewType != TYPE_DAY_VIEW) {
//                    item.setChecked(!item.isChecked());
//                    mWeekViewType = TYPE_DAY_VIEW;
//                    mWeekView.setNumberOfVisibleDays(1);
//
//                    // Lets change some dimensions to best fit the view.
//                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
//                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                }
//                return true;
//            case R.id.action_three_day_view:
//                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
//                    item.setChecked(!item.isChecked());
//                    mWeekViewType = TYPE_THREE_DAY_VIEW;
//                    mWeekView.setNumberOfVisibleDays(3);
//
//                    // Lets change some dimensions to best fit the view.
//                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
//                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
//                }
//                return true;
//            case R.id.action_week_view:
//                if (mWeekViewType != TYPE_WEEK_VIEW) {
//                    item.setChecked(!item.isChecked());
//                    mWeekViewType = TYPE_WEEK_VIEW;
//                    mWeekView.setNumberOfVisibleDays(7);
//
//                    // Lets change some dimensions to best fit the view.
//                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
//                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
//                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
//                }
//                return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
//                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());
                SimpleDateFormat format = new SimpleDateFormat("d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
//	return months[date.get(Calendar.MONTH)] + " "
				//			+ date.get(Calendar.DATE);
                tvdate=format.format(date.getTime());
                return "";
//                return format.format(date.getTime());
//                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
//                return "";
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        if(eventDetails!=null) {
            for (ScheduleBean sBean:eventDetails) {
                Log.i("AAAA", "DATE VIEW " + sBean.getEndTime() + " " + sBean.getStartTime());
                if(sBean.getStartTime()!=null && sBean.getEndTime()!=null) {
                    SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat sdf12 = new SimpleDateFormat("H:mm");
                    Date d = null;
                    Date d1 = null;
                    try {
                        d = sdf24.parse(sBean.getStartTime());
                        d1 = sdf24.parse(sBean.getEndTime());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    SimpleDateFormat datefor = new SimpleDateFormat("yyyy/MM/dd");

                    Date tmpDate = null;

                    try {
                        tmpDate = datefor.parse(sBean.getSceduleDate());
                    } catch (Exception e) {

                    }
                    Calendar startTime = Calendar.getInstance();
                    startTime.setTime(tmpDate);
                    startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sdf12.format(d)
                            .split(":")[0]));
                    startTime.set(Calendar.MINUTE, Integer.parseInt(sdf12.format(d).split(":")[1]));
                    startTime.set(Calendar.MONTH, newMonth - 1);
                    startTime.set(Calendar.YEAR, newYear);
                    Calendar endTime = (Calendar) startTime.clone();
                    endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sdf12.format(d1)
                            .split(":")[0]));
                    endTime.set(Calendar.MINUTE, Integer.parseInt(sdf12.format(d1)
                            .split(":")[1]));
                    endTime.set(Calendar.MONTH, newMonth - 1);
                    WeekViewEvent event = new WeekViewEvent(1, sBean.getTitle(), startTime, endTime);
                    event.setColor(getResources().getColor(R.color.blue2));
                    events.add(event);
                }
            }
        }
//------------------------------------
//        Calendar startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        Calendar endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR, 1);
//        endTime.set(Calendar.MONTH, newMonth-1);
//        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
//        Log.i("AAAA","DAte view "+startTime);
////        event.setColor(getResources().getColor(R.color.event_color_01));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 30);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.set(Calendar.HOUR_OF_DAY, 4);
//        endTime.set(Calendar.MINUTE, 30);
//        endTime.set(Calendar.MONTH, newMonth-1);
//        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
////        event.setColor(getResources().getColor(R.color.event_color_02));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 4);
//        startTime.set(Calendar.MINUTE, 20);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.set(Calendar.HOUR_OF_DAY, 5);
//        endTime.set(Calendar.MINUTE, 0);
//        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
////        event.setColor(getResources().getColor(R.color.event_color_03));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 5);
//        startTime.set(Calendar.MINUTE, 30);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 2);
//        endTime.set(Calendar.MONTH, newMonth-1);
//        event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
////        event.setColor(getResources().getColor(R.color.event_color_02));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 5);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        startTime.add(Calendar.DATE, 1);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        endTime.set(Calendar.MONTH, newMonth - 1);
//        event = new WeekViewEvent(3, getEventTitle(startTime), startTime, endTime);
////        event.setColor(getResources().getColor(R.color.event_color_03));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.DAY_OF_MONTH, 15);
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        event = new WeekViewEvent(4, getEventTitle(startTime), startTime, endTime);
////        event.setColor(getResources().getColor(R.color.event_color_04));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.DAY_OF_MONTH, 1);
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
////        event.setColor(getResources().getColor(R.color.event_color_01));
//        events.add(event);
//
//        startTime = Calendar.getInstance();
//        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
//        startTime.set(Calendar.HOUR_OF_DAY, 15);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR_OF_DAY, 3);
//        event = new WeekViewEvent(5, getEventTitle(startTime), startTime, endTime);
////        event.setColor(getResources().getColor(R.color.event_color_02));
//        events.add(event);

        return events;
    }

	private String getEventTitle(Calendar time) {
		return String.format("Event of %02d:%02d %s/%d",
                time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE),
                time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
	}

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
//        Toast.makeText(DateView.this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
        Calendar calendar=event.getStartTime();
        Log.i("date","1. getTime-->"+ calendar.getTime());

        String date =selected_date.split("/")[0]+"/"+selected_date.split("/")[1] +"/"+calendar.get(Calendar.DATE);
        String ss[]=calendar.getTime().toString().split(" ");
        String dd=ss[3];
        String start_time= dd.split(":")[0] +":"+dd.split(":")[1];
        for(ScheduleBean bean:eventDetails) {
            if(bean.getSceduleDate().equalsIgnoreCase(date) &&
                    bean.getStartTime().equalsIgnoreCase(start_time)) {
                Intent intent=new Intent(DateView.this,ScheduleActivity.class);
                intent.putExtra("selectedDate",selectedDate);
                intent.putExtra("title", bean.getTitle());
                intent.putExtra("starttime", bean.getStartTime());
                intent.putExtra("endtime", bean.getEndTime());
                intent.putExtra("eventclick",true);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(DateView.this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
        Calendar calendar=event.getStartTime();
        Log.i("date","1. getTime-->"+ calendar.getTime());

        String date =selected_date.split("/")[0]+"/"+selected_date.split("/")[1] +"/"+calendar.get(Calendar.DATE);
        String ss[]=calendar.getTime().toString().split(" ");
        String dd=ss[3];
        String start_time= dd.split(":")[0] +":"+dd.split(":")[1];
        for(ScheduleBean bean:eventDetails) {
            if (bean.getSceduleDate().equalsIgnoreCase(date) &&
                    bean.getStartTime().equalsIgnoreCase(start_time)) {
                clearAllAlert(bean);
                break;
            }
        }
    }
    private void clearAllAlert(final ScheduleBean sBean) {

        try {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
            myAlertDialog.setTitle("Delete Event");
            myAlertDialog
                    .setMessage("Are you sure want to delete this Event");
            myAlertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            callDisp.getdbHeler(context)
                                    .deleteSelectedEvent(
                                            sBean.getSceduleDate(),sBean.getOwner());
                            eventDetails.remove(sBean);
                            finish();
                        }
                    });
            myAlertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int arg1) {
                            // do something when the Cancel button is clicked

                            dialog.cancel();
                        }
                    });
            myAlertDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

    }
    private void setReminder()
    {
        Calendar Time_To_wake = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        for(ScheduleBean bean:eventDetails) {
            if(bean.getSceduleDate()!=null) {
                SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sdf12 = new SimpleDateFormat("H:mm");
                Date d = null;
                try {
                    d = sdf24.parse(bean.getStartTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SimpleDateFormat datefor = new SimpleDateFormat("yyyy/MM/dd");
                Date tmpDate = null;

                try {
                    tmpDate = datefor.parse(bean.getSceduleDate());
                } catch (Exception e) {
                }
                Time_To_wake.setTime(tmpDate);
                Time_To_wake.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sdf12.format(d)
                        .split(":")[0]));
                Time_To_wake.set(Calendar.MINUTE, Integer.parseInt(sdf12.format(d).split(":")[1]));
            }
        }
        if(Time_To_wake.compareTo(current) <= 0){
        }else {
            Intent intent = new Intent(SingleInstance.mainContext, ScheduleActivity.class);
            PendingIntent pending = PendingIntent.getService(this, 0, intent, 0);
            AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarm.set(AlarmManager.RTC_WAKEUP, Time_To_wake.getTimeInMillis(), pending);
        }
    }
    private Calendar today(){
        Calendar today = Calendar.getInstance();
        SimpleDateFormat datefor = new SimpleDateFormat("yyyy/MM/dd");;
        Date tmpDate=null;
        try {
            tmpDate = datefor.parse(DateView.selectedDate);
        }catch (Exception e){

        }
        today.setTime(tmpDate);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today;
    }
}
