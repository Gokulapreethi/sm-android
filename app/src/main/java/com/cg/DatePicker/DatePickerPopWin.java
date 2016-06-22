package com.cg.DatePicker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.cg.snazmed.R;
import com.util.SingleInstance;

/**
 * PopWindow for Date Pick
 */
public class DatePickerPopWin extends PopupWindow implements OnClickListener {

    private static final int DEFAULT_MIN_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    public Button cancelBtn;
    public Button confirmBtn;
    public LoopView yearLoopView;
    public LoopView monthLoopView;
    public LoopView dayLoopView;
    public LoopView picker_hour;
    public LoopView picker_minute;
    public LoopView picker_am;
    public View pickerContainerV;
    public View contentView;//root view

    private int minYear; // min year
    private int maxYear; // max year
    private int yearPos = 0;
    private int monthPos = 0;
    private int dayPos = 0;
    private int hourPos = 0;
    private int minutePos = 0;
    private int am_pm;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;//text btnTextsize of cancel and confirm button
    private int viewTextSize;
    private boolean fromRemind=false;

    List<String> yearList = new ArrayList();
    List<String> monthList = new ArrayList();
    List<String> dayList = new ArrayList();
    List<String> hourList = new ArrayList();
    List<String> minuteList = new ArrayList();
    List<String> am_pmList = new ArrayList();



    public static class Builder{

        //Required
        private Context context;
        private OnDatePickedListener listener;
        private boolean fromReminder=false;
        public Builder(Context context, boolean fromreminder,OnDatePickedListener listener){
            this.context = context;
            this.listener = listener;
            this.fromReminder = fromreminder;
            Log.d("boolean","valueof"+fromreminder);
        }

        //Option
        private int minYear = DEFAULT_MIN_YEAR;
        private int maxYear = Calendar.getInstance().get(Calendar.YEAR)+1;
        private String textCancel = "Cancel";
        private String textConfirm = "Confirm";
        private String dateChose = getStrDate();
        private String currenttime = getCurrenttime();
        private int colorCancel = Color.parseColor("#999999");
        private int colorConfirm = Color.parseColor("#303F9F");
        private int btnTextSize = 16;//text btnTextsize of cancel and confirm button
        private int viewTextSize = 25;

        public Builder minYear(int minYear){
            this.minYear = minYear;
            return this;
        }

        public Builder maxYear(int maxYear){
            this.maxYear = maxYear;
            return this;
        }

        public Builder textCancel(String textCancel){
            this.textCancel = textCancel;
            return this;
        }

        public Builder textConfirm(String textConfirm){
            this.textConfirm = textConfirm;
            return this;
        }

        public Builder dateChose(String dateChose){
            this.dateChose = dateChose;
            return this;
        }
        public Builder currenttime(String currenttime){
            this.currenttime = currenttime;
            return this;
        }

        public Builder colorCancel(int colorCancel){
            this.colorCancel = colorCancel;
            return this;
        }

        public Builder colorConfirm(int colorConfirm){
            this.colorConfirm = colorConfirm;
            return this;
        }

        /**
         * set btn text btnTextSize
         * @param textSize dp
         */
        public Builder btnTextSize(int textSize){
            this.btnTextSize = textSize;
            return this;
        }

        public Builder viewTextSize(int textSize){
            this.viewTextSize = textSize;
            return this;
        }

        public DatePickerPopWin build(){
            if(minYear > maxYear){
                throw new IllegalArgumentException();
            }
            return new DatePickerPopWin(this);
        }
    }

    public DatePickerPopWin(Builder builder){
        this.minYear = builder.minYear;
        this.maxYear = builder.maxYear;
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        this.fromRemind=builder.fromReminder;
        setSelectedDate(builder.dateChose);

        initView();
    }

    private OnDatePickedListener mListener;

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(
                R.layout.layout_date_picker, null);
        cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
        confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);
        yearLoopView = (LoopView) contentView.findViewById(R.id.picker_year);
        monthLoopView = (LoopView) contentView.findViewById(R.id.picker_month);
        dayLoopView = (LoopView) contentView.findViewById(R.id.picker_day);
        picker_hour = (LoopView) contentView.findViewById(R.id.picker_hour);
        picker_minute = (LoopView) contentView.findViewById(R.id.picker_minute);
        picker_am = (LoopView) contentView.findViewById(R.id.picker_am);
        pickerContainerV = contentView.findViewById(R.id.container_picker);


        cancelBtn.setText(textCancel);
        confirmBtn.setText(textConfirm);
        cancelBtn.setTextColor(colorCancel);
        confirmBtn.setTextColor(colorConfirm);
        cancelBtn.setTextSize(btnTextsize);
        confirmBtn.setTextSize(btnTextsize);

        //do not loop,default can loop
        yearLoopView.setNotLoop();
        monthLoopView.setNotLoop();
        dayLoopView.setNotLoop();
        picker_hour.setNotLoop();
        picker_minute.setNotLoop();
        picker_am.setNotLoop();

        //set loopview text btnTextsize
        yearLoopView.setTextSize(viewTextSize);
        monthLoopView.setTextSize(viewTextSize);
        dayLoopView.setTextSize(viewTextSize);
        picker_hour.setTextSize(viewTextSize);
        picker_minute.setTextSize(viewTextSize);
        picker_am.setTextSize(viewTextSize);


        //set checked listen
        yearLoopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                yearPos = item;
                initDayPickerView();
            }
        });
        monthLoopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                monthPos = item;
                initDayPickerView();
            }
        });
        dayLoopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                dayPos = item;
            }
        });
        picker_hour.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                hourPos = item;
            }
        });

        picker_minute.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                minutePos = item;
            }
        });

        picker_am.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                am_pm = item;
            }
        });

        initPickerViews(); // init year and month loop view
        initDayPickerView(); //init day loop view

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);

        setTouchable(true);
        setFocusable(true);
        // setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        if(fromRemind == false){
            picker_hour.setVisibility(View.GONE);
            picker_minute.setVisibility(View.GONE);
            picker_am.setVisibility(View.GONE);
            Log.d("boolean","false");
        }else {
            picker_hour.setVisibility(View.VISIBLE);
            picker_minute.setVisibility(View.VISIBLE);
            picker_am.setVisibility(View.VISIBLE);
            Log.d("boolean", "true");
        }
    }

    /**
     * Init year and month loop view,
     * Let the day loop view be handled separately
     */
    private void initPickerViews() {

        int yearCount = maxYear - minYear;

        for (int i = 0; i < yearCount; i++) {
            yearList.add(format2LenStr(minYear + i));
        }

        for (int j = 0; j < 12; j++) {
            monthList.add(format2LenStr(j + 1));
        }

        for (int h = 0; h<12; h++){
            hourList.add(format2LenStr(h+1));
        }
        for (int m=0; m<60; m++){
            minuteList.add(format2LenStr(m));
        }
        am_pmList.add("AM");
        am_pmList.add("PM");

        yearLoopView.setArrayList((ArrayList) yearList);
        yearLoopView.setInitPosition(yearPos);

        monthLoopView.setArrayList((ArrayList) monthList);
        monthLoopView.setInitPosition(monthPos);

        picker_hour.setArrayList((ArrayList) hourList);
        picker_hour.setInitPosition(hourPos);

        picker_minute.setArrayList((ArrayList) minuteList);
        picker_minute.setInitPosition(minutePos);

        picker_am.setArrayList((ArrayList) am_pmList);
        picker_am.setInitPosition(am_pm);
    }

    /**
     * Init day item
     */
    private void initDayPickerView() {

        int dayMaxInMonth;
        int curretntime;
        Calendar calendar = Calendar.getInstance();
        dayList = new ArrayList<String>();

        calendar.set(Calendar.YEAR, minYear + yearPos);
        calendar.set(Calendar.MONTH, monthPos);
        calendar.set(Calendar.HOUR, hourPos);
        calendar.set(Calendar.MINUTE, minutePos);
        calendar.set(Calendar.AM_PM, am_pm);

        //get max day in month
        dayMaxInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        curretntime = calendar.getActualMaximum(Calendar.HOUR);

        for (int i = 0; i < dayMaxInMonth; i++) {
            dayList.add(format2LenStr(i + 1));
        }

        dayLoopView.setArrayList((ArrayList) dayList);
        dayLoopView.setInitPosition(dayPos);
    }

    /**
     * set selected date position value when initView.
     *
     * @param dateStr
     */
    public void setSelectedDate(String dateStr) {

        if (!TextUtils.isEmpty(dateStr)) {

            long milliseconds = getLongFromyyyyMMdd(dateStr);
            Log.d("am", "value1" + dateStr);
            Calendar calendar = Calendar.getInstance(Locale.US);

            if (milliseconds != -1) {

                calendar.setTimeInMillis(milliseconds);
                Log.d("am", "value2" + milliseconds);
                yearPos = calendar.get(Calendar.YEAR) - minYear;
                monthPos = calendar.get(Calendar.MONTH);
                dayPos = calendar.get(Calendar.DAY_OF_MONTH) - 1;
                hourPos = calendar.get(Calendar.HOUR);
                minutePos = calendar.get(Calendar.MINUTE);
                if (calendar.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm =0;
                else if (calendar.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = 1;
//                am_pm = calendar.get(Calendar.AM_PM);
                Log.d("am","value"+calendar.get(Calendar.AM_PM));
            }
        }
    }

    /**
     * Show date picker popWindow
     *
     * @param activity
     */
    public void showPopWin(Activity activity) {

        if (null != activity) {

            TranslateAnimation trans = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
                    0, 0);
            trans.setDuration(400);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());

            pickerContainerV.startAnimation(trans);
        }
    }

    /**
     * Dismiss date picker popWindow
     */
    public void dismissPopWin() {

        TranslateAnimation trans = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(400);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                dismiss();
            }
        });

        pickerContainerV.startAnimation(trans);
    }

    @Override
    public void onClick(View v) {

        if (v == contentView || v == cancelBtn) {

            dismissPopWin();
        } else if (v == confirmBtn) {

            if (null != mListener) {

                int year = minYear + yearPos;
                int month = monthPos + 1;
                int day = dayPos + 1;
                int hour = hourPos+1;
                int minute = minutePos;
                String am = String.valueOf(am_pm);
                StringBuffer sb = new StringBuffer();

                sb.append(String.valueOf(month));
                sb.append("-");
                sb.append(format2LenStr(day));
                sb.append("-");
                sb.append(format2LenStr(year));

                sb.append(" ");
                sb.append(format2LenStr(hour));
                sb.append(":");
                sb.append(format2LenStr(minute));
                sb.append(" ");
                if(am_pm==0)
                    sb.append("AM");
                else
                    sb.append("PM");
                mListener.onDatePickCompleted(month, day, year, hour,minute, am, sb.toString());
            }

            dismissPopWin();
        }
    }

    /**
     * get long from yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static long getLongFromyyyyMMdd(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date parse = null;
        try {
            parse = mFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parse != null) {
            return parse.getTime();
        } else {
            return -1;
        }
    }

    public static String getCurrenttime(){
        SimpleDateFormat ct = new SimpleDateFormat("yyyy-MM-dd HH:MM a", Locale.CHINA);
        return ct.format(new Date());
    }
    public static String getStrDate() {
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:MM a", Locale.CHINA);
        return dd.format(new Date());
    }

    /**
     * Transform int to String with prefix "0" if less than 10
     * @param num
     * @return
     */
    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    public static int spToPx(Context context, int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public interface OnDatePickedListener {

        /**
         * Listener when date has been checked
         *
         * @param year
         * @param month
         * @param day
         * @param dateDesc  yyyy-MM-dd
         */
        void onDatePickCompleted(int month, int day, int year, int hour, int minute, String am,
                                 String dateDesc);
    }
}
