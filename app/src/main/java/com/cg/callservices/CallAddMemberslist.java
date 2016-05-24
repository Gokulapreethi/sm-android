package com.cg.callservices;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cg.commonclass.CallDispatcher;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;

import org.lib.model.BuddyInformationBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by jansi on 5/21/2016.
 */
public class CallAddMemberslist extends Activity {
    private Context context;
    private CheckBox selectAll_buddy;
    private TextView selected;
    private TextView txtView01;
    private ListView searchResult;
    private EditText btn_1;
    private Button search, cancel;
    public CallAddMemberAdapter adapter;
    private CallDispatcher objCallDispatcher;

    protected void Oncreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.call_add_memberslist);
        selectAll_buddy = (CheckBox) findViewById(R.id.selectAll_buddy);
        selected = (TextView) findViewById(R.id.selected);
        txtView01 = (TextView) findViewById(R.id.txtView01);
        searchResult = (ListView) findViewById(R.id.searchResult);
        btn_1 = (EditText) findViewById(R.id.searchet);
        cancel = (Button) findViewById(R.id.cancel);
        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtView01.getVisibility() == View.VISIBLE) {
                    txtView01.setVisibility(View.GONE);
                    btn_1.setVisibility(View.VISIBLE);
                    search.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_close));
                } else {
                    txtView01.setVisibility(View.VISIBLE);
                    btn_1.setVisibility(View.GONE);
                    search.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_search));
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        context = this;
        final ArrayList<String> memberslist = getIntent().getExtras().getStringArrayList("list");
        adapter = new CallAddMemberAdapter(context, memberslist);

//        selectAll_buddy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    for (int i = 0; i < get.size(); i++) {
//                        adapter.getItem(i).setSelected(true);
//                    }
//                } else {
//                    for (int i = 0; i < result.size(); i++) {
//                        adapter.getItem(i).setSelected(false);
//                    }
//                }
//                adapter.notifyDataSetChanged();
//                int count = 0;
//                for (BuddyInformationBean bib : result) {
//                    if (bib.isSelected()) {
//                        count++;
//                    }
//                }
//
//                selected.setText(count + " selected");
//            }
//        });
    }

    private class CallAddMemberAdapter extends ArrayAdapter {

        private List<String> memberslist;
        private LayoutInflater inflator;
        private Vector<BuddyInformationBean> result;
        Context context;
        private ViewHolder holder;
        private ImageLoader imageLoader;

        public CallAddMemberAdapter(Context context, ArrayList<String> memberslist) {
            super(context, R.layout.find_people_item, memberslist);
            this.memberslist = memberslist;

            inflator = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
        }

        @Override
        public String getItem(int position) {
            // TODO Auto-generated method stub
            return memberslist.get(position);
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            holder = new ViewHolder();
            if (convertView == null) {
                convertView = inflator.inflate(R.layout.find_people_item, null);
                holder.selectUser = (CheckBox) convertView.findViewById(R.id.sel_buddy);
                holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            final BuddyInformationBean bib = result.get(position);
            if (memberslist != null) {
                if (bib.getProfile_picpath() != null) {
                    String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/COMMedia/" + bib.getProfile_picpath();
                    File pic = new File(pic_Path);
                    if (pic.exists()) {
                        imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                    }
                }
            }

            return convertView;

        }
    }


    public static class ViewHolder {
        CheckBox selectUser;
        ImageView buddyicon;
        ImageView statusIcon;
        TextView buddyName;
        TextView occupation;
        TextView header_title;
    }
}



