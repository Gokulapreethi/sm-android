package com.cg.files;

import java.io.File;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.hostedconf.AppReference;
import com.image.utils.FileImageLoader;
import com.main.FilesFragment;
import com.util.SingleInstance;

public class FilesAdapter extends ArrayAdapter<CompleteListBean> {

    /***********
     * Declare Used Variables
     *********/

    private FileImageLoader fileImageLoader = null;
    private Context context;
    private Vector<CompleteListBean> fileList;
    private LayoutInflater inflater = null;
    FilesFragment filesFragment = null;
    private Typeface tf_regular = null;

    private Typeface tf_bold = null;
    private static CallDispatcher callDisp = null;


    /*************
     * CustomAdapter Constructor
     *****************/
    public FilesAdapter(Context context, Vector<CompleteListBean> fileList) {

        super(context, R.layout.files_list_row, fileList);
        /********** Take passed values **********/
        this.context = context;
        this.fileList = fileList;
        final int THUMBNAIL_LOADER_ID = 0;
        final int IMAGE_LOADER_ID = 1;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        filesFragment = FilesFragment.newInstance(context);
        fileImageLoader = new FileImageLoader(context.getApplicationContext());
        callDisp = CallDispatcher.getCallDispatcher(context);

        /*********** Layout inflator to call external xml layout () ***********/

    }

    public CompleteListBean getItem(int position) {
        return fileList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return super.getCount();
    }

    /******
     * Depends upon data size called for each row , Create each ListView row
     *****/
    public View getView(final int position, View convertView, ViewGroup parent) {

        try {
            ViewHolder holder;
            tf_regular = Typeface.createFromAsset(context.getAssets(),
                    "fonts/ARIAL.TTF");
            tf_bold = Typeface.createFromAsset(context.getAssets(),
                    "fonts/ARIALBD.TTF");

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.files_list_row, null,
                        false);
                holder = new ViewHolder();
                holder.fileName = (TextView) convertView.findViewById(R.id.file_name);
                holder.fileName.setTypeface(tf_regular);
                holder.ttlText = (TextView) convertView.findViewById(R.id.ttl_text);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.file_check);
                holder.dateTime = (TextView) convertView.findViewById(R.id.date_time);
                holder.fileIcon = (ImageView) convertView.findViewById(R.id.file_icon);
                holder.overlay = (ImageView) convertView.findViewById(R.id.overlay);
                holder.unreadIcon = (ImageView) convertView.findViewById(R.id.unread_icon);
                holder.from_user = (TextView) convertView.findViewById(R.id.from_user);
                holder.type = (TextView) convertView.findViewById(R.id.type);
                holder.size = (TextView) convertView.findViewById(R.id.size);
                holder.header = (TextView) convertView.findViewById(R.id.header_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            convertView.setVisibility(View.VISIBLE);

            final CompleteListBean fileBean = fileList.get(position);
            Log.i("dateformat", "Format" + CallDispatcher.dateFormat);
            SimpleDateFormat df = new SimpleDateFormat(
                    CallDispatcher.dateFormat + " hh:mm aa");
            SimpleDateFormat df2 = new SimpleDateFormat(
                    CallDispatcher.dateFormat);
            String[] receivedTimes = fileBean.getDateAndTime().split(" ");
            Log.i("ppp", "***** Modified Date ****" + fileBean.getDateAndTime());

            Date receivedDate = null;
            String header1 = "", header2 = "";

            if (receivedTimes[0].contains("/")
                    && CallDispatcher.dateFormat.contains("-")) {
                SimpleDateFormat userDateFormat = new SimpleDateFormat(
                        "MM/dd/yyyy");
                SimpleDateFormat dateFormatNeeded = new SimpleDateFormat(
                        CallDispatcher.dateFormat);
                Log.i("dateformat", "received in condition " + receivedTimes[0]);
                Date date = userDateFormat.parse(receivedTimes[0]);
                String convertedDate = dateFormatNeeded.format(date);
                receivedDate = dateFormatNeeded.parse(convertedDate);
            } else if (receivedTimes[0].contains("-")
                    && CallDispatcher.dateFormat.contains("/")) {
                SimpleDateFormat userDateFormat = new SimpleDateFormat(
                        "dd-MM-yyyy");
                SimpleDateFormat dateFormatNeeded = new SimpleDateFormat(
                        CallDispatcher.dateFormat);
                Log.i("dateformat", "received in condition " + receivedTimes[0]);
                Date date = userDateFormat.parse(receivedTimes[0]);
                String convertedDate = dateFormatNeeded.format(date);
                receivedDate = df2.parse(convertedDate);
            } else {
                receivedDate = df2.parse(receivedTimes[0]);
            }
            DateFormat d1f2 = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat d1f3 = new SimpleDateFormat("MM-dd-yyyy");
            // for formatting output
            Date d = d1f2.parse(receivedTimes[0]);
            String date_header = d1f3.format(d);
            String stringMonth = (String) android.text.format.DateFormat.format("MMM", d); //Jun
            holder.dateTime.setText(stringMonth + "  " + receivedTimes[1]+" "+receivedTimes[2]);
            Calendar cal = Calendar.getInstance();
            Log.i("time", "29-29305-" + cal.getTime());
            String[] todayDate = df.format(cal.getTime()).split(" ");
            for (int i = 0; i < todayDate.length; i++) {
                Log.i("t1", "----" + todayDate[i]);
            }
            Date today = df2.parse(todayDate[0]);
            String[] yesterdayDate = getYesterdayDateString(df).split(" ");
            Date yesterday = df2.parse(yesterdayDate[0]);
            Log.i("dateformat", "today :: " + today);
            Log.i("dateformat", "yesterday :: " + yesterday);

            if (receivedDate.compareTo(today) == 0) {
                holder.header.setText("Today ");
                header1 = "Today";

            } else if (receivedDate.compareTo(yesterday) == 0) {
                holder.header.setText("Yesterday");
                header1 = "Yesterday";
            } else {
                holder.header.setText(date_header);

            }
            if (filesFragment.sortorder.equalsIgnoreCase("alpha")) {
                header1 = String.valueOf(fileBean.getContentName().charAt(0));
                holder.header.setText(header1.toUpperCase());
            } else if (filesFragment.sortorder.equalsIgnoreCase("type")) {
                header1 = fileBean.getcomponentType();
                holder.header.setText(header1.toUpperCase());
            }
            holder.header.setVisibility(View.VISIBLE);
            if (position > 0) {
                CompleteListBean fBean = fileList.get(position - 1);
                if (filesFragment.sortorder.equalsIgnoreCase("date")) {
                    String[] date1 = fileBean.getDateAndTime().split(" ");
                    header1 = date1[0];
                    String[] date2 = fBean.getDateAndTime().split(" ");
                    header2 = date2[0];
                } else if (filesFragment.sortorder.equalsIgnoreCase("alpha"))
                    header2 = String.valueOf(fBean.getContentName().charAt(0));
                else if (filesFragment.sortorder.equalsIgnoreCase("type"))
                    header2 = fBean.getcomponentType();
                if (header1.equalsIgnoreCase(header2))
                    holder.header.setVisibility(View.GONE);
                else
                    holder.header.setVisibility(View.VISIBLE);
            }
            if (filesFragment.sortorder.equalsIgnoreCase(""))
                holder.header.setVisibility(View.GONE);

            Log.i("delete", "8888888888" + fileBean.getVanishMode() + ""
                    + fileBean.getVanishValue());

            if (fileBean.getVanishMode() != null
                    && (fileBean.getVanishMode().length() > 0 && !fileBean
                    .getVanishMode().equalsIgnoreCase("null"))) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd/MM/yyyy hh:mm aa");
                DateFormatSymbols dfSym = new DateFormatSymbols();
                dfSym.setAmPmStrings(new String[]{"am", "pm"});
                dateFormat.setDateFormatSymbols(dfSym);
                String currnetDate = dateFormat.format(new Date());
                Date newDate = dateFormat.parse(currnetDate);
                String dateString = fileBean.getVanishValue();
                Date getDate = dateFormat.parse(dateString);
                Log.i("files", "current date :: " + currnetDate);
                Log.i("files", "vanish date :: " + dateString);

                if (compareDatesByDateMethods(dateFormat, getDate, newDate,
                        "files")) {
                    String strDeleteQry = "delete from component where componentid="
                            + fileBean.getComponentId();
                    DBAccess.getdbHeler(context).ExecuteQuery(strDeleteQry);
                    fileList.remove(position);
                    notifyDataSetChanged();
                    SingleInstance.mainContext.notifyUI();

                } else {
                    holder.ttlText.setVisibility(View.VISIBLE);
                    holder.ttlText.setText("Expires At : "
                            + fileBean.getVanishValue());
                }

                // holder.ttlText.setVisibility(View.VISIBLE);
                // holder.ttlText.setText("Expires At : "
                // + fileBean.getVanishValue());

            } else {
                holder.ttlText.setVisibility(View.GONE);
            }
            holder.type.setText(fileBean.getcomponentType());
            File file;
            if (fileBean.getcomponentType().equalsIgnoreCase("video"))
                file = new File(fileBean.getContentpath() + ".mp4");
            else
                file = new File(fileBean.getContentpath());
            long length = (int) file.length();
            length = length / 1024;
            holder.size.setText(bytesToSize((int) length));
            String filename = null;
            if (fileBean.getcomponentType().equalsIgnoreCase("hand sketch")) {

                filename = fileBean.getContentName() + ".jpg";
            } else if (fileBean.getcomponentType().equalsIgnoreCase("video")) {
                filename = fileBean.getContentName() + ".mp4";
            } else if (fileBean.getcomponentType().equalsIgnoreCase("Audio")) {
                filename = fileBean.getContentName() + ".mp3";
            } else if (fileBean.getcomponentType().equalsIgnoreCase("photo")) {
                filename = fileBean.getContentName() + ".jpg";
            } else {
                filename = fileBean.getContentName();
            }

            holder.fileName.setText(filename);
            String Imagepath = Environment
                    .getExternalStorageDirectory()
                    + "/COMMedia/";


            Log.d("File", "FilePath : " + fileBean.getContentpath() + ", Type : " + fileBean.getcomponentType());
            if (position == 0) {
                Log.d("Fileposition", "FilePath : " + fileBean.getContentpath() + ", Type : " + fileBean.getcomponentType());
            }

            if (fileBean.getcomponentType().trim().equalsIgnoreCase("note")) {
                holder.fileIcon.setVisibility(View.GONE);
                holder.overlay.setVisibility(View.GONE);
                holder.fileIcon.setBackgroundResource(R.drawable.textnotesnew);
            } else if (fileBean.getcomponentType().trim().equalsIgnoreCase("audio")) {
                holder.fileIcon.setTag(fileBean.getComponentId());
                holder.fileIcon.setVisibility(View.VISIBLE);
                holder.overlay.setVisibility(View.GONE);
                Log.i("ppp", "audio file path  )))****" + fileBean.getContentpath());
                fileImageLoader.DisplayImage(fileBean.getContentpath().replace(".mp4", ".jpg"), holder.fileIcon, R.drawable.audionotesnew);
            } else if (fileBean.getcomponentType().trim().equalsIgnoreCase("video")) {
                holder.fileIcon.setTag(fileBean.getComponentId());
                holder.fileIcon.setVisibility(View.VISIBLE);
                holder.overlay.setVisibility(View.VISIBLE);
                fileImageLoader.DisplayImage(fileBean.getContentpath() + ".mp4", holder.fileIcon, R.drawable.videonotesnew);
            } else if (fileBean.getcomponentType().trim().equalsIgnoreCase("sketch")) {
                holder.fileIcon.setVisibility(View.VISIBLE);
                holder.overlay.setVisibility(View.GONE);
                fileImageLoader.DisplayImage(fileBean.getContentpath(), holder.fileIcon, R.drawable.handpencil);
            } else if (fileBean.getcomponentType().trim().equalsIgnoreCase("photo")) {
                holder.fileIcon.setVisibility(View.VISIBLE);
                holder.overlay.setVisibility(View.GONE);
                fileImageLoader.DisplayImage(fileBean.getContentpath(), holder.fileIcon, R.drawable.photonotesnew);
            } else if (fileBean.getcomponentType().trim().equalsIgnoreCase("document")) {
                holder.fileIcon.setImageResource(R.drawable.attachfile);
                holder.fileIcon.setVisibility(View.VISIBLE);
                holder.overlay.setVisibility(View.GONE);
                holder.overlay.setVisibility(View.GONE);
                String[] name = fileBean.getContentpath().split("\\.");
                holder.fileIcon.setTag(fileBean.getComponentId());
                String extn = name[1];
                holder.fileName.setText(fileBean.getContentName() + "." + extn);
            }
            if (filesFragment.isEdit) {
                holder.checkbox.setVisibility(View.VISIBLE);
            } else {
                holder.checkbox.setVisibility(View.GONE);
            }

            if (fileBean.getFromUser().length() > 0
                    && !fileBean.getFromUser().equalsIgnoreCase(
                    CallDispatcher.LoginUser)) {
                holder.from_user.setVisibility(View.GONE);
                holder.from_user.setText("From:" + fileBean.getFromUser());
            } else
                holder.from_user.setVisibility(View.GONE);
            holder.checkbox.setOnCheckedChangeListener(null);
            holder.checkbox.setChecked(fileBean.isSelected());
            holder.checkbox
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            // TODO Auto-generated method stub
                            if (isChecked) {
                                fileBean.setSelected(true);
                            } else {
                                fileBean.setSelected(false);
                            }
                        }
                    });
            if (fileBean.getViewmode() == 0) {
//				holder.unreadIcon.setVisibility(View.VISIBLE);
            } else {
                holder.unreadIcon.setVisibility(View.GONE);
            }
            if (fileBean.getContent() != null) {
                Log.i("cmt", "-----file adapter--" + fileBean.getContent());
            } else
                Log.i("cmt", "-----file adapter--");

            return convertView;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    class ViewHolder {
        TextView fileName;
        TextView ttlText;
        TextView dateTime;
        CheckBox checkbox;
        ImageView fileIcon, overlay;
        ImageView unreadIcon;
        TextView from_user;
        TextView type;
        TextView size, header;
    }


    private String time(SimpleDateFormat dateFormat) {
        try {

            Calendar cal = Calendar.getInstance();
            String[] date = dateFormat.format(cal.getTime()).split(" ");
            return date[0];
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return null;
        }
    }

    private boolean compareDatesByDateMethods(SimpleDateFormat df,
                                              Date oldDate, Date newDate, String from) {
        boolean isequalGreat = false;
        try {
            // how to check if two dates are equals in java
            if (from.equalsIgnoreCase("files")) {
                if (oldDate.before(newDate)) {
                    isequalGreat = true;
                }
            }
            if (oldDate.equals(newDate)) {
                isequalGreat = true;
            }

            return isequalGreat;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return false;
        }

    }

    private String getYesterdayDateString(SimpleDateFormat dateFormat) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            Log.i("dateformat",
                    "yesterday format change :: "
                            + dateFormat.format(cal.getTime()));
            return dateFormat.format(cal.getTime());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            if (AppReference.isWriteInFile)
                AppReference.logger.error(e.getMessage(), e);
            else
                e.printStackTrace();
            return null;
        }
    }

    public void filter(String charText) {
        // TODO Auto-generated method stub
        charText = charText.toLowerCase(Locale.getDefault());
        filesFragment.filesList.clear();
        if (charText.length() == 0) {
            fileList.addAll(filesFragment.tempFilesList);
        } else {
            for (CompleteListBean storedData : filesFragment.tempFilesList) {
                if (storedData.getContentName()
                        .toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    fileList.add(storedData);
                }
            }
        }
        notifyDataSetChanged();

    }
    public void Typefilter(String charText) {
        // TODO Auto-generated method stub
        charText = charText.toLowerCase(Locale.getDefault());
        filesFragment.filesList.clear();
        if (charText.length() == 0) {
            fileList.addAll(filesFragment.tempFilesList);
        } else {
            for (CompleteListBean storedData : filesFragment.tempFilesList) {
                if (storedData.getcomponentType()
                        .toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    fileList.add(storedData);
                }
            }
        }
        notifyDataSetChanged();

    }

    public void Memeorycontrol(String Type) {
        // TODO Auto-generated method stub
        filesFragment.filesList.clear();
        for (CompleteListBean storedData : filesFragment.tempFilesList) {
            if (storedData.getcomponentType().equalsIgnoreCase(Type)) {
                fileList.add(storedData);
            }
        }
        notifyDataSetChanged();
    }

    String bytesToSize(int bytes) {
        int kilobyte = 1024;
        int megabyte = kilobyte * 1024;
        int gigabyte = megabyte * 1024;
        int terabyte = gigabyte * 1024;

        if ((bytes >= 0) && (bytes < kilobyte)) {
            return bytes + " B";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return (bytes / kilobyte) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return (bytes / megabyte) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return (bytes / gigabyte) + " GB";

        } else if (bytes >= terabyte) {
            return (bytes / terabyte) + " TB";

        } else {
            return bytes + " B";
        }
    }
}
