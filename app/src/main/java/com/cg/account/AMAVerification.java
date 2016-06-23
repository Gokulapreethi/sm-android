package com.cg.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.commonclass.BuddyListComparator;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;
import org.lib.model.SignalingBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class AMAVerification extends Activity {
    private Context context;
    public AMAAdapter adapter;
    private CheckBox selectAll_buddy;
    private TextView selected;
    private TextView txtView01, addmembers_text;
    private ListView searchResult;
    private EditText btn_1;
    private ImageView grid_icon;
    private Button search, cancel;
    private RelativeLayout RelativeLayout2, RelativeLayout3;
    Vector<BuddyInformationBean> result;
    private CallDispatcher objCallDispatcher = null;
    private Handler handler = new Handler();
    private AppMainActivity appMainActivity;
    private CallDispatcher calldisp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ama_verification);

        if (WebServiceReferences.callDispatch.containsKey("calldisp"))
            objCallDispatcher = (CallDispatcher) WebServiceReferences.callDispatch
                    .get("calldisp");
        else
            objCallDispatcher = new CallDispatcher(context);
        appMainActivity=SingleInstance.mainContext;
        calldisp=new CallDispatcher(SingleInstance.mainContext);
        RelativeLayout2 = (RelativeLayout) findViewById(R.id.RelativeLayout2);
        selectAll_buddy = (CheckBox) findViewById(R.id.selectAll_buddy);
        selected = (TextView) findViewById(R.id.selected);
        txtView01 = (TextView) findViewById(R.id.txtView01);
        grid_icon = (ImageView)findViewById(R.id.grid_icon);
        searchResult = (ListView) findViewById(R.id.searchResult);
        final LinearLayout groupbtn = (LinearLayout) findViewById(R.id.groupbtn);
        btn_1 = (EditText) findViewById(R.id.searchet);
        cancel  = (Button)findViewById(R.id.cancel);
        search = (Button) findViewById(R.id.search);
        final Button deleteContact=(Button)findViewById(R.id.delete);
        Button chat=(Button)findViewById(R.id.chat);
        Button audiocall=(Button)findViewById(R.id.audiocall);
        Button videocall=(Button)findViewById(R.id.videocall);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(txtView01.getVisibility()==View.VISIBLE){
                     txtView01.setVisibility(View.GONE);
                     btn_1.setVisibility(View.VISIBLE);
                     search.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_close));
                 }else {
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
        result = new Vector<BuddyInformationBean>();
        result.addAll(getList(ContactsFragment.getBuddyList()));
        Collections.sort(result, new BuddyListComparator());
        adapter = new AMAAdapter(context, R.layout.find_people_item, result);
        searchResult.setAdapter(adapter);
        final LinearLayout dialogue = (LinearLayout) findViewById(R.id.dialogue);
        groupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dialogue.getVisibility()==View.VISIBLE){
                    dialogue.setVisibility(View.GONE);
                    grid_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.grid_grid));

                }else{
                    dialogue.setVisibility(View.VISIBLE);
                    grid_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_close));
                }
            }
        });
        deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                ArrayList<String> names=new ArrayList<String>();
                for (BuddyInformationBean bib : result) {
                    if (bib.isSelected()) {
                        count++;
                        names.add(bib.getName());
                    }
                }
                if(count>0){
                    doDeleteContact(names, count);
                }
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for (BuddyInformationBean bib : result) {
                    if (bib.isSelected())
                        count++;
                    }
                if(count==1) {
                    for (BuddyInformationBean bib : result) {
                        if (bib.isSelected()) {
                            finish();
                            Intent intent = new Intent(SingleInstance.mainContext, GroupChatActivity.class);
                            intent.putExtra("groupid", CallDispatcher.LoginUser
                                    + bib.getName());
                            intent.putExtra("isGroup", false);
                            intent.putExtra("isReq", "C");
                            intent.putExtra("buddy", bib.getName());
                            intent.putExtra("buddystatus", bib.getStatus());
                            intent.putExtra("nickname", bib.getFirstname() + " " + bib.getLastname());
                            SingleInstance.mainContext.startActivity(intent);
                        }
                    }
                }else {
                    showToast("One Person will be allowed to chat");
                    finish();
                }
            }
        });
        audiocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(BuddyInformationBean bib:result){
                    if(bib.isSelected())
                        count++;
                }
                if(count==1){
                    for(BuddyInformationBean bib:result){
                        calldisp.MakeCall(1, bib.getName(),
                                context);
                    }
                }else
                    showToast("One Person is allowed to make audio call");
                finish();
            }
        });
        videocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(BuddyInformationBean bib:result){
                    if(bib.isSelected())
                        count++;
                }
                if(count==1){
                    for(BuddyInformationBean bib:result){
                        calldisp.MakeCall(1, bib.getName(),
                                context);
                    }
                }else
                    showToast("One Person is allowed to make audio call");
                finish();
            }
        });

        btn_1.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        }
                if (s != null && s != "")
                    adapter.getFilter().filter(s);
            }
        });
        selectAll_buddy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    for (int i = 0; i < result.size(); i++) {
                        adapter.getItem(i).setSelected(true);
                    }
                } else {
                    for (int i = 0; i < result.size(); i++) {
                        adapter.getItem(i).setSelected(false);
                    }
                }
                adapter.notifyDataSetChanged();
                int count = 0;
                for (BuddyInformationBean bib : result) {
                    if (bib.isSelected()) {
                        count++;
                    }
                }
                if (count == 0) {
                    RelativeLayout2.setVisibility(View.GONE);
                    dialogue.setVisibility(View.GONE);
                } else {
                    RelativeLayout2.setVisibility(View.VISIBLE);
                }
                selected.setText(count + " selected");
            }
        });
        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("RRRR", "TITLE VALUE " + result.get(i).isSelected());
                BuddyInformationBean bib = new BuddyInformationBean();
                final CheckBox selectUser = (CheckBox) view.findViewById(R.id.sel_buddy);
                bib = result.get(i);
                if (bib.isSelected()) {
                    selectUser.setChecked(false);
                    bib.setSelected(false);
                } else {
                    selectUser.setChecked(true);
                    bib.setSelected(true);
                }
                adapter.notifyDataSetChanged();
                int count = 0;
                for (BuddyInformationBean bib1 : result) {
                    if (bib1.isSelected()) {
                        count++;
                    }
                }
                if (count == 0) {
                    RelativeLayout2.setVisibility(View.GONE);
                    dialogue.setVisibility(View.GONE);
                } else {
                    RelativeLayout2.setVisibility(View.VISIBLE);
                }
                selected.setText(count + " selected");
                if (count == result.size()) {
                    selectAll_buddy.setChecked(true);
                } else {
                    selectAll_buddy.setChecked(false);
                }
                final BuddyInformationBean finalBib = bib;
                selectUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (finalBib.isSelected()) {
                            selectUser.setChecked(false);
                            finalBib.setSelected(false);
                        } else {
                            selectUser.setChecked(true);
                            finalBib.setSelected(true);
                        }
                        adapter.notifyDataSetChanged();
                        int count = 0;
                        for (BuddyInformationBean bib1 : result) {
                            if (bib1.isSelected()) {
                                count++;
                            }
                        }
                        selected.setText(count + " selected");
                        if (count == 0) {
                            RelativeLayout2.setVisibility(View.GONE);
                            dialogue.setVisibility(View.GONE);
                        } else {
                            RelativeLayout2.setVisibility(View.VISIBLE);
                        }
                        if (count == result.size()) {
                            selectAll_buddy.setChecked(true);
                        } else {
                            selectAll_buddy.setChecked(false);
                        }
                    }
                });
            }
        });
    }

    protected Vector<BuddyInformationBean> getList(Vector<BuddyInformationBean> vectorBean){
        String status = null;
        Vector<BuddyInformationBean> tempList = new Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> a = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> b = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> c = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> d = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> e = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> f = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> g = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> h = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> i = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> j = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> k = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> l = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> m = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> n = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> o = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> p = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> q = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> r = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> s = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> t = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> u = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> v = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> w = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> x = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> y = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> z = new  Vector<BuddyInformationBean>();
        Vector<BuddyInformationBean> individualList = new Vector<BuddyInformationBean>();
        tempList.clear();
        for (BuddyInformationBean sortlistbean : vectorBean) {
            sortlistbean.setHeader("NULL");
            status = sortlistbean.getStatus();
            String profilePic = "";
            profilePic = DBAccess.getdbHeler().getProfilePic(
                    sortlistbean.getName());
            GroupBean gBean = DBAccess.getdbHeler()
                    .getAllIndividualChatByBuddyName(
                            CallDispatcher.LoginUser,
                            sortlistbean.getName());
            if (gBean.getLastMsg() != null
                    && gBean.getLastMsg().equalsIgnoreCase("null")) {
                sortlistbean.setLastMessage(gBean.getLastMsg());
            }
            if (profilePic != null && !profilePic.contains("/COMMedia/")
                    && profilePic.length() > 0) {
                profilePic = Environment.getExternalStorageDirectory()
                        + "/COMMedia/" + profilePic;
            }
            if (profilePic != null) {
                sortlistbean.setProfile_picpath(profilePic);
            }
            if (!sortlistbean.isTitle()) {
                if (status.equalsIgnoreCase("new") || status.equalsIgnoreCase("pending")) {
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("a")){
                    a.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("b")){
                    b.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("c")){
                    c.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("d")){
                    d.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("e")){
                    e.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("f")){
                    f.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("g")){
                    g.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("h")){
                    h.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("i")){
                    i.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("j")){
                    j.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("k")){
                    k.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("l")){
                    l.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("m")){
                    m.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("n")){
                    n.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("o")){
                    o.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("p")){
                    p.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("q")){
                    q.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("r")){
                    r.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("s")){
                    s.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("t")){
                    t.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("u")){
                    u.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("v")){
                    v.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("w")){
                    w.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("x")){
                    x.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("y")){
                    y.add(sortlistbean);
                }else if(String.valueOf(sortlistbean.getName().charAt(0)).equalsIgnoreCase("z")){
                    z.add(sortlistbean);
                }
            }
        }
        if(a.size()>0){
            Collections.sort(a, new BuddyListComparator());
            a.get(0).setHeader("A");
            tempList.addAll(a);
        }
        if(b.size()>0){
            Collections.sort(b, new BuddyListComparator());
            b.get(0).setHeader("B");
            tempList.addAll(b);
        }
        if(c.size()>0){
            Collections.sort(c, new BuddyListComparator());
            c.get(0).setHeader("C");
            tempList.addAll(c);
        }
        if(d.size()>0){
            Collections.sort(d, new BuddyListComparator());
            d.get(0).setHeader("D");
            tempList.addAll(d);
        }
        if(e.size()>0){
            Collections.sort(e, new BuddyListComparator());
            e.get(0).setHeader("E");
            tempList.addAll(e);
        }
        if(f.size()>0){
            Collections.sort(f, new BuddyListComparator());
            f.get(0).setHeader("F");
            tempList.addAll(f);
        }
        if(g.size()>0){
            Collections.sort(g, new BuddyListComparator());
            g.get(0).setHeader("G");
            tempList.addAll(g);
        }
        if(h.size()>0){
            Collections.sort(h, new BuddyListComparator());
            h.get(0).setHeader("H");
            tempList.addAll(h);
        }
        if(i.size()>0){
            Collections.sort(i, new BuddyListComparator());
            i.get(0).setHeader("I");
            tempList.addAll(i);
        }
        if(j.size()>0){
            Collections.sort(j, new BuddyListComparator());
            j.get(0).setHeader("J");
            tempList.addAll(j);
        }
        if(k.size()>0){
            Collections.sort(k, new BuddyListComparator());
            k.get(0).setHeader("K");
            tempList.addAll(k);
        }
        if(l.size()>0){
            Collections.sort(l, new BuddyListComparator());
            l.get(0).setHeader("L");
            tempList.addAll(l);
        }
        if(m.size()>0){
            Collections.sort(m, new BuddyListComparator());
            m.get(0).setHeader("M");
            tempList.addAll(m);
        }
        if(n.size()>0){
            Collections.sort(n, new BuddyListComparator());
            n.get(0).setHeader("N");
            tempList.addAll(n);
        }
        if(o.size()>0){
            Collections.sort(o, new BuddyListComparator());
            o.get(0).setHeader("O");
            tempList.addAll(o);
        }
        if(p.size()>0){
            Collections.sort(p, new BuddyListComparator());
            p.get(0).setHeader("P");
            tempList.addAll(p);
        }
        if(q.size()>0){
            Collections.sort(q, new BuddyListComparator());
            q.get(0).setHeader("Q");
            tempList.addAll(q);
        }
        if(r.size()>0){
            Collections.sort(r, new BuddyListComparator());
            r.get(0).setHeader("R");
            tempList.addAll(r);
        }
        if(s.size()>0){
            Collections.sort(s, new BuddyListComparator());
            s.get(0).setHeader("S");
            tempList.addAll(s);
        }
        if(t.size()>0){
            Collections.sort(t, new BuddyListComparator());
            t.get(0).setHeader("T");
            tempList.addAll(t);
        }
        if(u.size()>0){
            Collections.sort(u, new BuddyListComparator());
            u.get(0).setHeader("U");
            tempList.addAll(u);
        }
        if(v.size()>0){
            Collections.sort(v, new BuddyListComparator());
            v.get(0).setHeader("V");
            tempList.addAll(v);
        }
        if(w.size()>0){
            Collections.sort(w, new BuddyListComparator());
            w.get(0).setHeader("W");
            tempList.addAll(w);
        }
        if(x.size()>0){
            Collections.sort(x, new BuddyListComparator());
            x.get(0).setHeader("X");
            tempList.addAll(x);
        }
        if(y.size()>0){
            Collections.sort(y, new BuddyListComparator());
            y.get(0).setHeader("Y");
            tempList.addAll(y);
        }
        if(z.size()>0){
            Collections.sort(z, new BuddyListComparator());
            z.get(0).setHeader("Z");
            tempList.addAll(z);
        }
        for (BuddyInformationBean bBean : tempList) {
            Log.d("R46NU5", "Name: " + bBean.getName() + " Header: " + bBean.getHeader());
            individualList.add(bBean);
        }
        tempList.clear();
        tempList.addAll(individualList);

        return tempList;
    }

    public class AMAAdapter extends ArrayAdapter<BuddyInformationBean> {

        private LayoutInflater inflater = null;
        private ViewHolder holder;
        private ImageLoader imageLoader;
        private Vector<BuddyInformationBean> result;
        private Vector<BuddyInformationBean> originalList;
        private  ContactsFilter filter;

        public AMAAdapter(Context context, int resource, Vector<BuddyInformationBean> objects) {
            super(context, resource, objects);
            imageLoader=new ImageLoader(context);
            result = new Vector<BuddyInformationBean>();
            result.addAll(objects);
            originalList = new Vector<BuddyInformationBean>();
            originalList.addAll(objects);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            try {
                holder = new ViewHolder();
                if(convertView == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.find_people_item, null);
                    holder.selectUser = (CheckBox) convertView.findViewById(R.id.sel_buddy);
                    holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                    holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                    holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                    holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                    holder.header_title = (TextView) convertView.findViewById(R.id.header_title);

                    convertView.setTag(holder);
                }else
                    holder = (ViewHolder) convertView.getTag();
                final BuddyInformationBean bib = result.get(i);

                if(bib!=null) {
                    if (bib.getProfile_picpath() != null) {
                        String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/COMMedia/" + bib.getProfile_picpath();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
                    holder.header_title.setVisibility(View.VISIBLE);
                    String cname1, cname2;
                    cname1 = String.valueOf(bib.getFirstname().charAt(0));

                    holder.header_title.setText(cname1.toUpperCase());

                    if (i > 0) {
                        final  BuddyInformationBean bbean1 = result.get(i - 1);
                        cname2 = String.valueOf(bbean1.getFirstname().charAt(0));
                        if (cname1.equalsIgnoreCase(cname2)) {
                            Log.d("Headervalue","title-->");
                            holder.header_title.setVisibility(View.GONE);
                        } else {
                            Log.d("Headervalue","title--> else");
                            holder.header_title.setVisibility(View.VISIBLE);
                        }

                    }
                    if (bib.isSelected()) {
                        holder.selectUser.setChecked(true);
                    } else {
                        holder.selectUser.setChecked(false);
                    }
                    if (bib.getStatus() != null) {
                        if (bib.getStatus().equalsIgnoreCase("offline") || bib.getStatus().equalsIgnoreCase("stealth")) {
                            holder.statusIcon.setBackgroundResource(R.drawable.offline_icon);
                        } else if (bib.getStatus().equalsIgnoreCase("online")) {
                            holder.statusIcon.setBackgroundResource(R.drawable.online_icon);
                        } else if (bib.getStatus().equalsIgnoreCase("busy") || bib.getStatus().equalsIgnoreCase("airport")) {
                            holder.statusIcon.setBackgroundResource(R.drawable.busy_icon);
                        } else if (bib.getStatus().equalsIgnoreCase("away")) {
                            holder.statusIcon.setBackgroundResource(R.drawable.invisibleicon);
                        } else {
                            holder.statusIcon.setBackgroundResource(R.drawable.offline_icon);
                        }
                    }
                    holder.buddyName.setText(bib.getFirstname()+" "+bib.getLastname());
                    holder.occupation.setText(bib.getOccupation());
                }
            }catch(Exception e){
                e.printStackTrace();
                Log.d("RRRR","Error FindpeopleAdapter.java => "+e.toString());
            }
            return convertView;
        }
        @Override
        public Filter getFilter() {
            if (filter == null){
                filter  = new ContactsFilter();
            }
            return filter;
        }
        private class ContactsFilter extends Filter
        {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();

                FilterResults result = new FilterResults();
                if (constraint != null && constraint.toString().length() > 0) {
                    Vector<BuddyInformationBean> buddyInformationBeans = new Vector<BuddyInformationBean>();
                    for(int i = 0, l = originalList.size(); i < l; i++)
                    {
                        BuddyInformationBean buddyInformationBean = originalList.get(i);
                        if(buddyInformationBean.getName().toLowerCase().contains(String.valueOf(constraint)))
                            buddyInformationBeans.add(buddyInformationBean);
                    }
                    buddyInformationBeans = GroupChatActivity.getAdapterList(buddyInformationBeans);
                    result.count = buddyInformationBeans.size();
                    result.values = buddyInformationBeans;
                } else {
                    synchronized (this) {
                        originalList = GroupChatActivity.getAdapterList(originalList);
                        result.values = originalList;
                        result.count = originalList.size();
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                result= (Vector<BuddyInformationBean>)results.values;
                notifyDataSetChanged();
                clear();
                result = GroupChatActivity.getAdapterList(result);
                for(int i = 0, l = result.size(); i < l; i++)
                    add(result.get(i));
                notifyDataSetInvalidated();

            }

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
    private void showToast(final String message) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void doDeleteContact(final ArrayList<String> buddynames, final int count) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    if (appMainActivity.isNetworkConnectionAvailable()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Warning !");
                        builder.setMessage("Are you sure you want to delete "
                                + count + " members?").setCancelable(false).setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                            ContactsFragment.getInstance(SingleInstance.mainContext).deleteContact(buddynames);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert1 = builder.create();
                        alert1.show();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.d("R4J1","ERROR -> "+e.toString());
                }
            }
        });

    }
}
