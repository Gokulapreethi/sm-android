package com.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.image.utils.ImageLoader;

import org.lib.model.FindPeopleBean;

import java.util.Vector;


public class FoundPeopleAdapter extends ArrayAdapter<FindPeopleBean> {

    private Context context;
    private Vector<FindPeopleBean> searchList;
    private LayoutInflater inflater = null;
    private ImageLoader imageLoader;

    public FoundPeopleAdapter(Context context, Vector<FindPeopleBean> peopleList) {

        super(context, R.layout.search_list_row, peopleList);
        this.context = context;
        this.searchList = peopleList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(context.getApplicationContext());
    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;
        if(view==null) {
            holder=new ViewHolder();
            holder.buddyname = (TextView) convertView
                    .findViewById(R.id.tv_buddyName);
            holder.status = (TextView) convertView
                    .findViewById(R.id.tv_status);
            holder.dept = (TextView) convertView
                    .findViewById(R.id.tv_dept);
            holder.buddyicon = (ImageView) convertView
                    .findViewById(R.id.buddy_icon);
            holder.checkbox = (CheckBox) convertView
                    .findViewById(R.id.checkBox);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final FindPeopleBean fBean= searchList.get(position);
        if(fBean!=null) {
            if(fBean.getName()!=null)
                holder.buddyname.setText(fBean.getName());
            if(fBean.getRole()!=null)
                holder.dept.setText(fBean.getRole());
            if(fBean.getStatus()!=null)
                if(fBean.getStatus().equals("0"))
                    holder.status.setText("Online");
                else
                    holder.status.setText("Offline");
            if(fBean.getPhoto()!=null)  {
                imageLoader.DisplayImage(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/COMMedia/" +
                                fBean.getPhoto(),
                        holder.buddyicon,
                        R.drawable.icon_buddy_aoffline);

            }
        }
//        holder.checkbox.setOnCheckedChangeListener(null);
//
//        holder.checkbox.setChecked(fBean.isSelected());

//        if (!fBean.isAllowChecking()) {
//            holder.checkbox.setChecked(true);
//            holder.checkbox.setEnabled(false);
//        } else {
//            holder.checkbox
//                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton arg0,
//                                                     boolean isChecked) {
//
//                            if (isChecked) {
//                                fBean.setSelected(true);
//                            } else {
//                                fBean.setSelected(false);
//
//                                AddGroupMembers addGroupMembers = (AddGroupMembers) WebServiceReferences.contextTable
//                                        .get("groupcontact");
////									if (addGroupMembers != null) {
////										addGroupMembers.selectAll
////												.setChecked(false);
////									}
//                            }
//
//                        }
//
//                    });
//        }
        return view;
    }

    public static class ViewHolder {
        TextView buddyname;
        TextView status;
        TextView dept;
        ImageView buddyicon;
        CheckBox checkbox;

    }
}
