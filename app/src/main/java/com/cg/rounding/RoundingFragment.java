package com.cg.rounding;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.group.GroupActivity;
import com.group.chat.GroupChatActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.model.GroupBean;

import java.util.Vector;

public class RoundingFragment extends Fragment {
    private static RoundingFragment roundingFragment;
    private static CallDispatcher calldisp=null;
    private Context mainContext;
    View _rootView;
    private Handler handler = new Handler();
    public static RoundingAdapter adapter;
    public static Vector<GroupBean> grouplist=new Vector<GroupBean>();
    ListView list;
    private ProgressDialog progressDialog = null;

    public static synchronized RoundingAdapter getRoundingAdapter() {

        return adapter;
    }
    public static synchronized Vector<GroupBean> getRoundingList() {

        return grouplist;
    }
    public static RoundingFragment newInstance(Context context) {
        try {
            if (roundingFragment == null) {
                roundingFragment = new RoundingFragment();
                roundingFragment.setContext(context);
                calldisp = CallDispatcher.getCallDispatcher(context);

            }
            return roundingFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return roundingFragment;
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
        plusBtn.setVisibility(View.VISIBLE);
        plusBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.input_pluswhite));

        TextView title = (TextView) getActivity().findViewById(
                R.id.activity_main_content_title);
        title.setVisibility(View.VISIBLE);
        title.setText("ROUNDING");

        Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
        backBtn.setVisibility(View.GONE);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final Dialog dialog = new Dialog(SingleInstance.mainContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_myacc_menu);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.horizontalMargin = 15;
                    Window window = dialog.getWindow();
                    window.setBackgroundDrawableResource(R.color.lblack);
                    window.setAttributes(lp);
                    window.setGravity(Gravity.BOTTOM);
                    dialog.show();
                    TextView create_group = (TextView) dialog.findViewById(R.id.delete_acc);
                    TextView existing = (TextView) dialog.findViewById(R.id.log_out);
                    TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                    create_group.setText("Create New Group");
                    existing.setText("Duplicate Existing group");
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    create_group.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity().getApplicationContext(),
                                    RoundingGroupActivity.class);
                            getActivity().startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        _rootView = null;
        if (_rootView == null) {

            _rootView = inflater.inflate(R.layout.rounding, null);
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            try {
                RoundingGroupActivity.getallRoundingGroups();
                if(RoundingGroupActivity.RoundingList.size()==0)
                    showDialog();
                 list=(ListView)_rootView.findViewById(R.id.listview_rounding);
                adapter=new RoundingAdapter(mainContext,R.layout.grouplist,RoundingGroupActivity.RoundingList);
                adapter.notifyDataSetChanged();
                list.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
            ((ViewGroup) _rootView.getParent()).removeView(_rootView);
        return _rootView;
    }
    public void getList()
    {
        cancelDialog();
        RoundingGroupActivity.getallRoundingGroups();
        RoundingFragment.getRoundingList().clear();
        synchronized (RoundingFragment.getRoundingList()) {
            RoundingFragment.getRoundingList().addAll(RoundingGroupActivity.RoundingList);
        }
        if (RoundingFragment.getRoundingAdapter() != null) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    try {
                        // TODO Auto-generated method stub
                        list.setAdapter(null);
                        adapter = new RoundingAdapter(mainContext,
                                R.layout.grouplist,grouplist);
                        list.setAdapter(adapter);
                        RoundingFragment.getRoundingAdapter().notifyDataSetChanged();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });

        }

    }
    public void showGroupChatDialog(GroupBean groupBean) {
        try {
            // TODO Auto-generated method stub

            if (groupBean != null) {
                GroupBean groupBean1 = DBAccess.getdbHeler()
                        .getGroupAndMembers(
                                "select * from groupdetails where groupid="
                                        + groupBean.getGroupId());
                if (groupBean1 != null
                        && groupBean1.getActiveGroupMembers() != null
                        && groupBean1.getActiveGroupMembers().length() > 0) {
                    Intent intent = new Intent(getActivity()
                            .getApplicationContext(), GroupChatActivity.class);
                    intent.putExtra("groupid", groupBean1.getGroupId());
                    intent.putExtra("isRounding", true);
                    getActivity().startActivity(intent);

                    WebServiceReferences.webServiceClient.GetPatientRecords("", groupBean1.getGroupId()
                            , SingleInstance.mainContext);
                    WebServiceReferences.webServiceClient.GetTaskInfo("", groupBean1.getGroupId(), SingleInstance.mainContext);
                    WebServiceReferences.webServiceClient.GetRoleAccess(CallDispatcher.LoginUser, groupBean1.getGroupId(),
                            "", SingleInstance.mainContext);
                    WebServiceReferences.webServiceClient.GetMemberRights(CallDispatcher.LoginUser,
                            groupBean1.getGroupId(),SingleInstance.mainContext);


                } else {
                    showToast("Sorry no members to chat");
                    if (groupBean.getOwnerName().equalsIgnoreCase(
                            CallDispatcher.LoginUser)) {
                        Intent intent = new Intent(getActivity()
                                .getApplicationContext(),
                                RoundingGroupActivity.class);
                        intent.putExtra("isEdit", true);
                        intent.putExtra("id", groupBean.getGroupId());
                        getActivity().startActivity(intent);
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private void showToast(final String message) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(mainContext, message, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void showDialog() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    progressDialog = new ProgressDialog(mainContext);
                    if (progressDialog != null) {
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Progress ...");
                        progressDialog
                                .setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setProgress(0);
                        progressDialog.setMax(100);
                        progressDialog.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }
    public void cancelDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
