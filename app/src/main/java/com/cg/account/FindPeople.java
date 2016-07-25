package com.cg.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.commonclass.BuddyListComparator;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.group.AddGroupMembers;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.main.FilesFragment;
import com.main.RequestFragment;
import com.main.SearchPeopleFragment;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;

import java.util.ArrayList;
import java.util.Collections;


public class FindPeople extends Fragment {

    FindPeopleAdapter adapter;
    private static FindPeople findPeople;
    ArrayList<BuddyInformationBean> result;
    Context mainContext;
    private CheckBox selectAll_buddy;
    private TextView selected;
    private ListView searchResult;
    private RelativeLayout RelativeLayout2;
    private Button bt_RelativeLayout2;
    private static CallDispatcher calldisp = null;
    public View _rootView;
    private ProgressDialog pDialog;
    private int checkBoxCounter = 0;

    public static FindPeople newInstance(Context context) {
        try {
            if (findPeople == null) {
                findPeople = new FindPeople();
                findPeople.setContext(context);
                calldisp = CallDispatcher.getCallDispatcher(context);

            }
            return findPeople;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return findPeople;
        }
    }

    public void setContext(Context cxt) {
        this.mainContext = cxt;
    }
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
         Button select = (Button) getActivity().findViewById(R.id.btn_brg);
         select.setVisibility(View.GONE);
         RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
         mainHeader.setVisibility(View.VISIBLE);
         LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
         contact_layout.setVisibility(View.GONE);

         Button imVw = (Button) getActivity().findViewById(R.id.im_view);
         imVw.setVisibility(View.GONE);

         final Button search1 = (Button) getActivity().findViewById(R.id.btn_settings);
         search1.setVisibility(View.GONE);

         final Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
         plusBtn.setVisibility(View.GONE);

         TextView title = (TextView) getActivity().findViewById(
                 R.id.activity_main_content_title);
         title.setVisibility(View.VISIBLE);
         title.setText("SEARCH RESULTS");

         Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
         backBtn.setVisibility(View.VISIBLE);
         backBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 SearchPeopleFragment searchPeopleFragment = SearchPeopleFragment.newInstance(SingleInstance.mainContext);
                 FragmentManager fragmentManager = SingleInstance.mainContext
                         .getSupportFragmentManager();
                 fragmentManager.beginTransaction().replace(
                         R.id.activity_main_content_fragment, searchPeopleFragment)
                         .commitAllowingStateLoss();

             }
         });
        _rootView = null;
        if (_rootView == null) {
            _rootView = inflater.inflate(R.layout.findpeople, null);
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            try {
                result = new ArrayList<BuddyInformationBean>();
                result.addAll(SingleInstance.searchedResult);
                Button cancel = (Button) _rootView.findViewById(R.id.cancel);
                RelativeLayout2 = (RelativeLayout) _rootView.findViewById(R.id.RelativeLayout2);
                bt_RelativeLayout2 = (Button)_rootView. findViewById(R.id.bt_RelativeLayout2);
                selectAll_buddy = (CheckBox) _rootView.findViewById(R.id.selectAll_buddy);
                selected = (TextView) _rootView.findViewById(R.id.selected);
                searchResult = (ListView) _rootView.findViewById(R.id.searchResult);

                selectAll_buddy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectAll_buddy.isChecked()) {
                            for (int i=0;i<result.size();i++) {
                                adapter.getItem(i).setSelected(true);
                            }
                        } else {
                            for (int i=0;i<result.size();i++) {
                                adapter.getItem(i).setSelected(false);
                            }
                        }
                        adapter.notifyDataSetChanged();

                        int count = 0;
                        checkBoxCounter=0;
                        for (BuddyInformationBean bib : result) {
                            if (bib.isSelected()) {
                                count++;
                                checkBoxCounter = count;
                            }
                        }
                        selected.setText(count + " selected");
                    }
                });
//                selectAll_buddy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                        if (b) {
//                            for (int i=0;i<result.size();i++) {
//                                adapter.getItem(i).setSelected(true);
//                            }
//                        } else {
//                            for (int i=0;i<result.size();i++) {
//                                adapter.getItem(i).setSelected(false);
//                            }
//                        }
//                        adapter.notifyDataSetChanged();
//                        int count = 0;
//                        for (BuddyInformationBean bib : result) {
//                            if (bib.isSelected()) {
//                                count++;
//                            }
//                        }
//                        selected.setText(count + " selected");
//                    }
//                });
                if (result.size() > 0) {
                    Collections.sort(result,new BuddyListComparator());
                    adapter = new FindPeopleAdapter(mainContext, R.layout.find_people_item, result);
                    searchResult.setAdapter(adapter);
                    searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("RRRR", "TITLE VALUE " + result.size());
                            BuddyInformationBean bib = result.get(i);
                            Log.d("RRRR", "TITLE VALUE " + bib.getEmailid()+" i "+i);
                            WebServiceReferences.webServiceClient.GetAllProfile(
                                    CallDispatcher.LoginUser, bib.getEmailid(), ContactsFragment
                                            .getInstance(SingleInstance.mainContext));
                            RequestFragment requestFragment = RequestFragment.newInstance(SingleInstance.mainContext);
                            requestFragment.setRequest("invite");
                            requestFragment.setFrom(true);
                            requestFragment.setBuddyName(bib.getEmailid());
                            calldisp.showprogress(CallDispatcher.pdialog,mainContext);
//                        final CheckBox selectUser = (CheckBox) view.findViewById(R.id.sel_buddy);
//                        if(bib.isSelected()){
//                            selectUser.setChecked(false);
//                            bib.setSelected(false);
//                        }else{
//                            selectUser.setChecked(true);
//                            bib.setSelected(true);
//                        }
//                        adapter.notifyDataSetChanged();
//                        int count = 0;
//                        for (BuddyInformationBean bib1 : result) {
//                            if (bib1.isSelected()) {
//                                count++;
//                            }
//                        }
//                        selected.setText(count + " selected");
//                        if(count == result.size()){
//                            selectAll_buddy.setChecked(true);
//                        }else{
//                            selectAll_buddy.setChecked(false);
//                        }
//                        final BuddyInformationBean finalBib = bib;
//                        selectUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                                if(finalBib.isSelected()){
//                                    selectUser.setChecked(false);
//                                    finalBib.setSelected(false);
//                                }else{
//                                    selectUser.setChecked(true);
//                                    finalBib.setSelected(true);
//                                }
//                                adapter.notifyDataSetChanged();
//                                int count = 0;
//                                for (BuddyInformationBean bib1 : result) {
//                                    if (bib1.isSelected()) {
//                                        count++;
//                                    }
//                                }
//                                selected.setText(count + " selected");
//                                if(count == result.size()){
//                                    selectAll_buddy.setChecked(true);
//                                }else{
//                                    selectAll_buddy.setChecked(false);
//                                }
//                            }
//                        });
                        }
                    });
                    searchResult.setDivider(null);
                }
                bt_RelativeLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RelativeLayout2.performClick();
                    }
                });
                RelativeLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("AAAA","FIND PEOPLE "+result.size());
                        int count = 0;
                        for (BuddyInformationBean bib : result) {
                            Log.i("AAAA","FIND PEOPLE "+bib.isSelected());
                            if (bib.isSelected()) {
                                count++;
                            }
                        }
                        if(count>0){
                            for (BuddyInformationBean bib : result) {
                                if (bib.isSelected()) {
                                    if (WebServiceReferences.running) {
                                        WebServiceReferences.webServiceClient
                                                .addPeople(CallDispatcher.LoginUser,
                                                        bib.getEmailid(), findPeople);
                                    }
                                }
                            }
                        }
                        FragmentManager fm = SingleInstance.mainContext
                                .getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Fragment fragments = null;
                        SingleInstance.myOrder = false;
                        fragments = ContactsFragment.getInstance(SingleInstance.mainContext);
                        if (fragments != null) {
                            // Replace current fragment by this new one
                            ft.replace(R.id.activity_main_content_fragment, fragments);
                            ft.commitAllowingStateLoss();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
            ((ViewGroup) _rootView.getParent()).removeView(_rootView);
        return _rootView;
    }
    class FindPeopleAdapter extends ArrayAdapter<BuddyInformationBean>{

        private LayoutInflater inflater = null;
        private ViewHolder holder;
        private ImageLoader imageLoader;

        public FindPeopleAdapter(Context context, int resource, ArrayList<BuddyInformationBean> objects) {
            super(context, resource, objects);
            imageLoader=new ImageLoader(context);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            try {
                holder = new ViewHolder();
                if(convertView == null) {
                    inflater = (LayoutInflater) mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.find_people_item, null);
                    holder.selectUser = (CheckBox) convertView.findViewById(R.id.sel_buddy);
                    holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                    holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                    holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                    holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                    convertView.setTag(holder);
                }else
                    holder = (ViewHolder) convertView.getTag();
                final BuddyInformationBean bib = result.get(i);
                if(bib!=null) {
//                    if (bib.getProfile_picpath() != null) {
//                        String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/COMMedia/" + bib.getProfile_picpath();
//                        File pic = new File(pic_Path);
//                        if (pic.exists()) {
//                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
//                        }
//                    }
                    if (bib.isSelected()) {
                        holder.selectUser.setChecked(true);
                    } else {
                        holder.selectUser.setChecked(false);
                    }
                    holder.selectUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((CheckBox) v).isChecked()){
                                bib.setSelected(true);
                                checkBoxCounter++;
                                countofcheckbox(checkBoxCounter);
                            }
                            else {
                                bib.setSelected(false);
                                checkBoxCounter--;
                                countofcheckbox(checkBoxCounter);
                            }
                        }
                    });
                    if (bib.getStatus().equals("0")) {
                        holder.statusIcon.setBackgroundResource(R.drawable.offline_icon);
                    } else if (bib.getStatus().equals("1")) {
                        holder.statusIcon.setBackgroundResource(R.drawable.online_icon);
                    } else if (bib.getStatus().equals("2")) {
                        holder.statusIcon.setBackgroundResource(R.drawable.busy_icon);
                    } else if (bib.getStatus().equals("3")) {
                        holder.statusIcon.setBackgroundResource(R.drawable.invisibleicon);
                    } else {
                        holder.statusIcon.setBackgroundResource(R.drawable.offline_icon);
                    }
                    if(bib.getFirstname()!=null && bib.getFirstname().length()>0
                            && bib.getLastname().length()>0 && bib.getLastname()!=null)
                    holder.buddyName.setText(bib.getFirstname()+" "+bib.getLastname());
                    else
                        holder.buddyName.setText(bib.getEmailid());
                    holder.occupation.setText(bib.getOccupation());
                }
            }catch(Exception e){
                e.printStackTrace();
                Log.d("RRRR","Error FindpeopleAdapter.java => "+e.toString());
            }
            return convertView;
        }
    }
    public View getParentView() {
        return _rootView;
    }

    public static class ViewHolder {
        CheckBox selectUser;
        ImageView buddyicon;
        ImageView statusIcon;
        TextView buddyName;
        TextView occupation;
    }
    public void countofcheckbox(int count)
    {
        Log.i("asdf", "count" + count);
        selected.setText(Integer.toString(count) + " Selected");
        if(count==result.size())
            selectAll_buddy.setChecked(true);
        else
            selectAll_buddy.setChecked(false);

    }
    private void showprogress() {
        try {
            pDialog = new ProgressDialog(SingleInstance.mainContext);
            pDialog.setCancelable(false);
            pDialog.setMessage("Progress ...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setProgress(0);
            pDialog.setMax(100);
            pDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cancelDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}