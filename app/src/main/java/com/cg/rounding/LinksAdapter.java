package com.cg.rounding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bean.GroupChatBean;
import com.cg.snazmed.R;

import java.util.Vector;

/**
 * Created by Rajalakshmi gurunath on 07-05-2016.
 */
public class LinksAdapter extends ArrayAdapter<GroupChatBean> {

    private Context context;
    private Vector<GroupChatBean> chatLinkList=new Vector<GroupChatBean>();

    public LinksAdapter(Context context, int textViewResourceId,
                        Vector<GroupChatBean> groupList) {

        super(context, R.layout.grouplist, groupList);
        this.context = context;
        chatLinkList=groupList;


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
                row = inflater.inflate(R.layout.chat_links, null, false);
                holder.fromuser = (TextView) row.findViewById(R.id.fromuser);
                holder.date = (TextView) row.findViewById(R.id.date);
                holder.message = (TextView) row.findViewById(R.id.message);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }
            GroupChatBean gcBean=(GroupChatBean)chatLinkList.get(position);
            if(gcBean!=null){
                if(gcBean.getMessage()!=null)
                    holder.message.setText(gcBean.getMessage());
                if(gcBean.getFrom()!=null)
                    holder.fromuser.setText(gcBean.getFrom());
                if(gcBean.getSenttime()!=null) {
                    String[] date=gcBean.getSenttime().split(" ");
                    holder.date.setText(date[0]);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return row;
    }

    public static class ViewHolder {
        TextView date,fromuser;
        TextView message;
    }

}
