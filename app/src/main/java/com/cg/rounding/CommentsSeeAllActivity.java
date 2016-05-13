package com.cg.rounding;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.util.SingleInstance;

import org.lib.model.PatientCommentsBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

public class CommentsSeeAllActivity extends Activity {

    private Context context;
    Vector<PatientCommentsBean> CommentsList;
    String groupid,patientid,membername;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.comments_seeall);
        context = this;
        CommentsList=new Vector<PatientCommentsBean>();
        Button cancel=(Button)findViewById(R.id.cancel);
        TextView tv_member=(TextView)findViewById(R.id.txtView01);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        groupid=getIntent().getStringExtra("groupid");
        patientid=getIntent().getStringExtra("patientid");
        membername=getIntent().getStringExtra("membername");
        tv_member.setText(membername);
        CommentsList= DBAccess.getdbHeler().getParticularPatientComments(groupid, patientid, membername);
        ListView listView=(ListView)findViewById(R.id.listview_seeallcomments);
        CommentsAdapter adapter=new CommentsAdapter(context,R.layout.comments_row,CommentsList);
        listView.setAdapter(adapter);

    }
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
                final ViewHolder holder;
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
                } else {
                    holder = (ViewHolder) row.getTag();
                }
                Log.i("patientdetails", "adapter " + commentsList.size());
                PatientCommentsBean cBean=commentsList.get(position);
                    holder.commentorName.setVisibility(View.GONE);
                    holder.seeAll.setVisibility(View.GONE);
                    holder.count.setVisibility(View.GONE);
                    holder.profile_img.setVisibility(View.GONE);
                if(cBean!=null){
                    if(cBean.getDateandtime()!=null) {
                        String[] time = cBean.getDateandtime().split(" ");
                        String[] times=time[1].split(":");
                        holder.time.setText(times[0] + ":" + times[1]);
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
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return row;
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
    public static class ViewHolder {
        TextView commentorName;
        TextView comments;
        TextView time,seeAll,count;
        ImageView profile_img;
        RelativeLayout header;
        TextView date_header;

    }
}
