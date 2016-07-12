package com.cg.rounding;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.util.SingleInstance;

import org.lib.model.PatientCommentsBean;
import org.lib.model.TaskDetailsBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class CommentsAdapter extends ArrayAdapter<PatientCommentsBean> {

    private Context context;
    ImageLoader imageLoader;
    Vector<PatientCommentsBean> commentsList;

    public CommentsAdapter(Context context, int textViewResourceId,
                               Vector<PatientCommentsBean> commentslist) {

        super(context, R.layout.comments_row, commentslist);
        this.context = context;
        imageLoader=new ImageLoader(SingleInstance.mainContext);
        commentsList=new Vector<PatientCommentsBean>();
        commentsList.addAll(commentslist);

    }
    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        View row = view;

        try {
            ViewHolder holder;
            holder = new ViewHolder();
            if (row == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.comments_row, null, false);
                holder.commentorName = (TextView) row.findViewById(R.id.name);
                holder.comments = (TextView) row.findViewById(R.id.comments);
                holder.time = (TextView) row.findViewById(R.id.time);
                holder.seeAll = (TextView) row.findViewById(R.id.seeall);
                holder.count = (TextView) row.findViewById(R.id.count);
                holder.header = (RelativeLayout) row.findViewById(R.id.header_container);
                holder.date_header = (TextView) row.findViewById(R.id.date_header);
                holder.profile_img = (ImageView) row.findViewById(R.id.profile_img);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }
            Log.i("patientdetails", "adapter " + commentsList.size());
            final PatientCommentsBean cBean=commentsList.get(position);
            if(PatientRoundingFragment.commentType.equalsIgnoreCase("mine")) {
                holder.commentorName.setVisibility(View.GONE);
                holder.seeAll.setVisibility(View.GONE);
                holder.count.setVisibility(View.GONE);
                holder.profile_img.setVisibility(View.GONE);
            }else{
                holder.commentorName.setVisibility(View.VISIBLE);
                holder.seeAll.setVisibility(View.VISIBLE);
                holder.count.setVisibility(View.VISIBLE);
                holder.profile_img.setVisibility(View.VISIBLE);
            }
            int count= DBAccess.getdbHeler().countPatientComments(cBean.getGroupid(),cBean.getPatientid(),cBean.getGroupmember());
            holder.count.setText("("+String.valueOf(count)+")");
            if(cBean!=null){
                if(cBean.getGroupowner()!=null) {
                    if(PatientRoundingFragment.commentType.equalsIgnoreCase("all")) {
                        if(cBean.getGroupmember().equalsIgnoreCase(CallDispatcher.LoginUser))
                            holder.commentorName.setText("Me");
                        else
                            holder.commentorName.setText(cBean.getMembername());
                    }else
                        holder.commentorName.setText(cBean.getMembername());
                }
                if(cBean.getDateandtime()!=null) {
                    String[] time = cBean.getDateandtime().split(" ");
                    String[] times=time[1].split(":");
                    try {
                        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                        final Date dateObj = sdf.parse(times[0] + ":" + times[1]);
                        holder.time.setText(new SimpleDateFormat("hh:mm a").format(dateObj));
                    } catch (final ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(cBean.getComments()!=null)
                    holder.comments.setText(cBean.getComments());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                String todayDate = format.format(cal.getTime());
                holder.header.setVisibility(View.VISIBLE);
                if(todayDate.equals(cBean.getDateandtime().split(" ")[0]))
                {
                    holder.date_header.setText("TODAY");
                }
                else if(getYesterdayDateString(format).equals(cBean.getDateandtime().split(" ")[0]))
                {
                    holder.date_header.setText("YESTERDAY");
                }
                else{
                    holder.date_header.setText(cBean.getDateandtime().split(" ")[0]);
                }
                holder.date_header.setVisibility(View.VISIBLE);
                if(position>0) {
                    final PatientCommentsBean gcbn = commentsList.get(position - 1);
                    if(gcbn.getDateandtime().split(" ")[0].equals(cBean.getDateandtime().split(" ")[0]))
                    {
                        holder.date_header.setVisibility(View.GONE);
                    }
                    else{
                        holder.date_header.setVisibility(View.VISIBLE);
                    }
                }
                holder.seeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(SingleInstance.mainContext,CommentsSeeAllActivity.class);
                        intent.putExtra("groupid",cBean.getGroupid());
                        intent.putExtra("patientid",cBean.getPatientid());
                        intent.putExtra("membername",cBean.getGroupmember());
                        intent.putExtra("firstname",cBean.getMembername());
                        SingleInstance.mainContext.startActivity(intent);
                    }
                });
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return row;
    }
    public static class ViewHolder {
        TextView commentorName;
        TextView comments;
        TextView time,seeAll,count;
        ImageView profile_img;
        RelativeLayout header;
        TextView date_header;

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
}
