package com.cg.rounding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.snazmed.R;
import com.group.chat.GroupChatActivity;
import com.util.SingleInstance;

import org.lib.model.GroupBean;

public class RolesManagementFragment extends Fragment {
    private static RolesManagementFragment rolesManagementFragment;
    private Context mainContext;
    private static CallDispatcher calldisp=null;
    View _rootView;
    private String groupName,groupId;
    public static RolesManagementFragment newInstance(Context context) {
        try {
            if (rolesManagementFragment == null) {
                rolesManagementFragment = new RolesManagementFragment();
                rolesManagementFragment.setContext(context);
                calldisp = CallDispatcher.getCallDispatcher(context);

            }
            return rolesManagementFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return rolesManagementFragment;
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
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TextView title = (TextView) getActivity().findViewById(
                R.id.activity_main_content_title);
        title.setVisibility(View.VISIBLE);
        title.setText("ROLES MANAGEMENT");

        Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleInstance.mainContext, GroupChatActivity.class);
                intent.putExtra("groupid", groupId);
                intent.putExtra("isRounding", true);
                intent.putExtra("isReq", "p");
                SingleInstance.mainContext.startActivity(intent);
                RoundingFragment roundingFragment = RoundingFragment.newInstance(SingleInstance.mainContext);
                FragmentManager fragmentManager = SingleInstance.mainContext
                        .getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(
                        R.id.activity_main_content_fragment, roundingFragment)
                        .commitAllowingStateLoss();
            }
        });
        _rootView = null;
        if (_rootView == null) {

            _rootView = inflater.inflate(R.layout.rounding_role1, null);
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            try {
                TextView tv_grpName=(TextView)_rootView.findViewById(R.id.grp_name);
                TextView attending=(TextView)_rootView.findViewById(R.id.attending);
                TextView fellow=(TextView)_rootView.findViewById(R.id.fellow);
                TextView chief_res=(TextView)_rootView.findViewById(R.id.chief_res);
                TextView resident=(TextView)_rootView.findViewById(R.id.resident);
                TextView med_student=(TextView)_rootView.findViewById(R.id.med_student);
                tv_grpName.setText(groupName);
                final GroupBean gBean = DBAccess.getdbHeler().getGroupAndMembers(
                        "select * from groupdetails where groupid=" + groupId);

                attending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity().getApplicationContext(),AttendingRightsActivity.class);
                        intent.putExtra("groupid",groupId);
                        intent.putExtra("groupowner",gBean.getOwnerName());
                        intent.putExtra("groupmembers",gBean.getActiveGroupMembers());
                        intent.putExtra("role","Attending");
                        getActivity().startActivity(intent);
                    }
                });
                fellow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity().getApplicationContext(),AttendingRightsActivity.class);
                        intent.putExtra("groupid",groupId);
                        intent.putExtra("groupowner",gBean.getOwnerName());
                        intent.putExtra("groupmembers",gBean.getActiveGroupMembers());
                        intent.putExtra("role","Fellow");
                        getActivity().startActivity(intent);
                    }
                });
                chief_res.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity().getApplicationContext(),AttendingRightsActivity.class);
                        intent.putExtra("groupid",groupId);
                        intent.putExtra("groupowner",gBean.getOwnerName());
                        intent.putExtra("groupmembers",gBean.getActiveGroupMembers());
                        intent.putExtra("role","Chief Resident");
                        getActivity().startActivity(intent);
                    }
                });
                resident.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity().getApplicationContext(),AttendingRightsActivity.class);
                        intent.putExtra("groupid",groupId);
                        intent.putExtra("groupowner",gBean.getOwnerName());
                        intent.putExtra("groupmembers",gBean.getActiveGroupMembers());
                        intent.putExtra("role","Resident");
                        getActivity().startActivity(intent);
                    }
                });
                med_student.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity().getApplicationContext(),AttendingRightsActivity.class);
                        intent.putExtra("groupid",groupId);
                        intent.putExtra("groupowner",gBean.getOwnerName());
                        intent.putExtra("groupmembers",gBean.getActiveGroupMembers());
                        intent.putExtra("role","Medical Student");
                        getActivity().startActivity(intent);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
            ((ViewGroup) _rootView.getParent()).removeView(_rootView);
        return _rootView;
    }

    public void setGroupName(String groupname) {
        groupName = groupname;
    }
    public void setGroupId(String groupid) {
        groupId = groupid;
    }
}
