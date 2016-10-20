package com.cg.rounding;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.DB.DBAccess;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.google.android.gms.wearable.internal.ChannelSendFileResponse;
import com.group.AddGroupMembers;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.util.SingleInstance;

import org.lib.PatientDetailsBean;
import org.lib.model.GroupBean;

import java.lang.annotation.ElementType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class RoundingPatientAdapter extends ArrayAdapter<PatientDetailsBean> {

    private Context context;
    ImageLoader imageLoader;
    private int checkBoxCounter = 0;
    Vector<PatientDetailsBean> patientlist;
    AssignPatientActivity assignpatient;

    public RoundingPatientAdapter(Context context, int textViewResourceId,
                                  Vector<PatientDetailsBean> patientList) {

        super(context, R.layout.rouding_patient_row, patientList);
        this.context = context;
        imageLoader = new ImageLoader(SingleInstance.mainContext);
        patientlist = new Vector<PatientDetailsBean>();
        patientlist.addAll(patientList);


    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        View row = view;

        try {

            ViewHolder holder;
            holder = new ViewHolder();

            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.rouding_patient_row, null);
                holder.patientname = (TextView) row.findViewById(R.id.patient_name);
                holder.age = (TextView) row.findViewById(R.id.age);
                holder.sex = (TextView) row.findViewById(R.id.sex);
                holder.mrn = (TextView) row.findViewById(R.id.mrn);
                holder.floor = (TextView) row.findViewById(R.id.floor);
                holder.ward = (TextView) row.findViewById(R.id.ward);
                holder.room = (TextView) row.findViewById(R.id.room);
                holder.los = (TextView) row.findViewById(R.id.los);
                holder.bed = (TextView) row.findViewById(R.id.bed);
                holder.header = (RelativeLayout) row.findViewById(R.id.patient_header);
                holder.headertext = (TextView) row.findViewById(R.id.headertext);
                holder.select = (CheckBox) row.findViewById(R.id.btn_select);
                holder.img_pad = (ImageView) row.findViewById(R.id.img_pad);
                holder.currentstatus_lay = (LinearLayout) row.findViewById(R.id.currentstatus_lay);
                holder.currentstatus_lay1 = (LinearLayout) row.findViewById(R.id.currentstatus_lay1);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }
            Log.i("patientdetails", "adapter " + patientlist.size());
            final PatientDetailsBean pBean = patientlist.get(position);
            if (pBean != null) {
                if (pBean.isSelected()) {
                    holder.select.setChecked(true);
                } else {
                    holder.select.setChecked(false);
                    Log.i("patientdetails", "adapter false");
                }
                if (pBean.isFromPatienttab()) {
                    holder.select.setVisibility(View.GONE);
                    holder.img_pad.setVisibility(View.VISIBLE);
                } else {
                    holder.select.setVisibility(View.VISIBLE);
                    holder.img_pad.setVisibility(View.GONE);
                }

                if (pBean.getStatus() != null) {
                    String[] split = pBean.getStatus().split(" ");
                    Log.i("AAAA", "rounding adapter status " + pBean.getStatus());
                    holder.currentstatus_lay.removeAllViews();
                    holder.currentstatus_lay1.removeAllViews();
                    for (int i = 0; i < split.length; i++) {
                        if (i <= 5) {
                            TextView dynamicTextView = new TextView(context);
                            LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 60);
                            dim.leftMargin = 20;
                            dynamicTextView.setLayoutParams(dim);
                            dynamicTextView.setGravity(Gravity.CENTER);
                            dynamicTextView.setBackgroundDrawable(SingleInstance.mainContext.getResources().getDrawable(R.drawable.sender_border));
                            if (split[i].equalsIgnoreCase("critical"))
                                dynamicTextView.setTextColor(SingleInstance.mainContext.getResources().getColor(R.color.pink_tv));
                            else if (split[i].equalsIgnoreCase("stable")) {
                                dynamicTextView.setTextColor(SingleInstance.mainContext.getResources().getColor(R.color.green));
                            } else if (split[i].equalsIgnoreCase("sick")) {
                                dynamicTextView.setTextColor(SingleInstance.mainContext.getResources().getColor(R.color.yellow));
                            }
                            if (!split[i].equalsIgnoreCase("") && split.length > 0) {
                                dynamicTextView.setText(split[i]);
                                holder.currentstatus_lay.addView(dynamicTextView);
                            }

                        } else if (i > 5) {
                            TextView dynamicTextView = new TextView(context);
                            LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 60);
                            dim.leftMargin = 20;
                            dynamicTextView.setLayoutParams(dim);
                            dynamicTextView.setGravity(Gravity.CENTER);
                            dynamicTextView.setBackgroundDrawable(SingleInstance.mainContext.getResources().getDrawable(R.drawable.sender_border));
                            if (split[i].equalsIgnoreCase("critical"))
                                dynamicTextView.setTextColor(SingleInstance.mainContext.getResources().getColor(R.color.pink_tv));
                            else if (split[i].equalsIgnoreCase("stable")) {
                                dynamicTextView.setTextColor(SingleInstance.mainContext.getResources().getColor(R.color.green));
                            } else if (split[i].equalsIgnoreCase("sick")) {
                                dynamicTextView.setTextColor(SingleInstance.mainContext.getResources().getColor(R.color.yellow));
                            }
                            dynamicTextView.setText(split[i]);
                            holder.currentstatus_lay1.addView(dynamicTextView);
                        }

                    }
                }
                if (pBean.getFirstname() != null && pBean.getLastname() != null)
                    holder.patientname.setText(pBean.getFirstname() + " " + pBean.getLastname());
                if (pBean.getDob() != null && pBean.getDob().length() > 0) {
                    String birthdate = pBean.getDob();
                    Log.i("sss", "Current birthdate" + birthdate);
                    String[] str;
                    if (birthdate.contains("/"))
                        str = birthdate.split("/");
                    else
                        str = birthdate.split("-");
                    int Currentyear = Calendar.getInstance().get(Calendar.YEAR);
                    Log.i("sss", "Current year" + Currentyear);

                    String BirthYear = str[2];
                    int age = Currentyear - (Integer.parseInt(BirthYear));
                    Log.i("sss", "Current age" + age);

                    holder.age.setText("Age : " + age);
                }
                if (pBean.getSex() != null && pBean.getSex().length() > 0)
                    holder.sex.setText("Sex : " + String.valueOf(pBean.getSex().charAt(0)));
                if (pBean.getMrn() != null)
                    holder.mrn.setText("MRN : " + pBean.getMrn());
                if (pBean.getFloor() != null)
                    holder.floor.setText("Floor : " + pBean.getFloor());
                if (pBean.getRoom() != null)
                    holder.room.setText("Room : " + pBean.getRoom());
                if (pBean.getWard() != null)
                    holder.ward.setText("Ward : " + pBean.getWard());
                if (pBean.getAdmissiondate() != null && pBean.getAdmissiondate().length() > 0) {
                    String AdmitDate = pBean.getAdmissiondate();
                    long Result = 0;
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                    String inputString = dateFormat.format(date);
                    String Today = inputString;
                    SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Log.i("patientdetails", "patient adapter " + AdmitDate);
                    String fromDate = AdmitDate;
                    String inputString1 = fromDate;
                    Log.i("sss", "From Date1 : " + inputString1);
                    String inputString2 = Today;
                    Log.i("sss", "Current Date1 : " + inputString2);
                    try {
                        Date date1 = dateFormat.parse(inputString1);
                        Date date2 = dateFormat.parse(inputString2);
                        long diff = date2.getTime() - date1.getTime();
                        Log.i("sss", "DIFF" + diff);
                        long diffSeconds = diff / 1000 % 60;
                        long diffMinutes = diff / (60 * 1000) % 60;
                        long diffHours = diff / (60 * 60 * 1000) % 24;
                        long diffDays = diff / (24 * 60 * 60 * 1000);
                        System.out.print(diffDays + " days, ");
                        System.out.print(diffHours + " hours, ");
                        System.out.print(diffMinutes + " minutes, ");
                        System.out.print(diffSeconds + " seconds.");
                        Log.i("sss", "Total Days : " + diffDays);
                        Result = diffDays;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    holder.los.setText("Los : " + Result + " days");
                }
                if (pBean.getBed() != null)
                    holder.bed.setText("Bed : " + pBean.getBed());
                holder.header.setVisibility(View.VISIBLE);

                String floor1, floor2;
                if (GroupChatActivity.patientType.equalsIgnoreCase("name") || GroupChatActivity.patientType.equalsIgnoreCase("mypatient")) {
                    floor1 = String.valueOf(pBean.getFirstname().charAt(0));
                    holder.headertext.setText(floor1.toUpperCase());
                } else if (GroupChatActivity.patientType.equalsIgnoreCase("loc")) {
                    floor1 = String.valueOf(pBean.getFloor());
                    holder.headertext.setText("Floor " + floor1.toUpperCase());
                } else {
                    floor1 = String.valueOf(pBean.getStatus());
                    if (floor1.equalsIgnoreCase("") || floor1.equalsIgnoreCase("null")) {
                        holder.headertext.setText("Nil");
//                        holder.headertext.setVisibility(View.GONE);
                    } else
                        holder.headertext.setText(floor1.toUpperCase());
                }

                if (position > 0) {
                    final PatientDetailsBean gcbn = patientlist.get(position - 1);
                    if (GroupChatActivity.patientType.equalsIgnoreCase("name"))
                        floor2 = String.valueOf(gcbn.getFirstname().charAt(0));
                    else if (GroupChatActivity.patientType.equalsIgnoreCase("loc"))
                        floor2 = String.valueOf(gcbn.getFloor());
                    else
                        floor2 = String.valueOf(gcbn.getStatus());
                    if (floor1.equalsIgnoreCase(floor2)) {
                        holder.header.setVisibility(View.GONE);
                    } else {
                        holder.header.setVisibility(View.VISIBLE);
                    }
                }
                assignpatient = (AssignPatientActivity) WebServiceReferences.contextTable
                        .get("assignpatient");

                holder.select
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton arg0,
                                                         boolean isChecked) {
                                if (isChecked) {
                                    pBean.setSelected(true);
                                    checkBoxCounter++;
                                    if (assignpatient != null) {
                                        assignpatient.countofcheckbox(checkBoxCounter);
                                    }
                                } else {
                                    pBean.setSelected(false);
                                    checkBoxCounter--;
                                    if (assignpatient != null) {
                                        assignpatient.countofcheckbox(checkBoxCounter);
                                    }
                                }
                            }
                        });
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return row;
    }

    class ViewHolder {
        TextView patientname;
        TextView age;
        TextView mrn;
        TextView floor;
        TextView ward;
        TextView room;
        TextView sex;
        TextView los;
        TextView bed;
        RelativeLayout header;
        TextView headertext;
        CheckBox select;
        ImageView img_pad;
        LinearLayout currentstatus_lay, currentstatus_lay1;

    }

    public void filter(String charText) {
        // TODO Auto-generated method stub
        charText = charText.toLowerCase(Locale.getDefault());
        GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
                .get("groupchat");
        if (gChat != null) {
            gChat.tempPatientList = gChat.PatientList;
            gChat.PatientList.clear();
            Log.i("patientdetails", "adapter  filter if " + patientlist.size());
            if (charText.length() == 0) {
                patientlist.addAll(gChat.tempPatientList);
            } else {
                Log.i("patientdetails", "adapter  filter " + gChat.tempPatientList.size());
                for (PatientDetailsBean storedData : gChat.tempPatientList) {
                    if (storedData.getFirstname()
                            .toLowerCase(Locale.getDefault()).contains(charText)) {
                        patientlist.add(storedData);
                    }

                }
            }
            notifyDataSetChanged();
        }
    }

}
