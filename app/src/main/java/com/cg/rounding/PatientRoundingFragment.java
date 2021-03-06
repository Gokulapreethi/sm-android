package com.cg.rounding;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.BuddyListComparator;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.quickaction.User;
import com.cg.snazmed.R;
import com.group.AddGroupMembers;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.lib.PatientDetailsBean;
import org.lib.model.BuddyInformationBean;
import org.lib.model.GroupBean;
import org.lib.model.GroupMemberBean;
import org.lib.model.PatientCommentsBean;
import org.lib.model.PatientDescriptionBean;
import org.lib.model.RoleAccessBean;
import org.lib.model.RoleEditRndFormBean;
import org.lib.model.RolePatientManagementBean;
import org.lib.model.RoleTaskMgtBean;
import org.lib.model.TaskDetailsBean;
import org.lib.model.WebServiceBean;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class PatientRoundingFragment extends Fragment {
    private static PatientRoundingFragment patientRoundingFragment;
    private Context mainContext;
    private static CallDispatcher calldisp = null;
    private String pastingContentCopy;
    View _rootView;
    private boolean islistAZ = true;
    private String groupName;
    private Seealladapter seealladapter;
    public Boolean title = false;
    TextView tv_rounding, tv_task, tv_comments, tv_members;
    ImageView rounding_img, task_img, comments_img, members_img;
    View view_rounding, view_task, view_comments, view_members;
    GroupBean gBean = new GroupBean();
    String patientid, patientName;
    PatientDetailsBean pBean = new PatientDetailsBean();
    TextView diagnosis, medications, testandvitals, hospital, consults;
    LinearLayout currentstatus_lay, currentstatus_lay1;
    PatientDescriptionBean pcBean = new PatientDescriptionBean();
    private int editTag = 0;
    Handler handler = new Handler();
    private ProgressDialog progress = null;
    private String reportid;
    CommentsAdapter cadapter;
    Vector<PatientCommentsBean> CommentsList;
    public static String commentType = "mine";
    ListView clistView = null;
    String statusMode, assignedMode;
    ListView tasklistView;
    String strQuery = null;
    TextView tv_status, tv_assigned;
    GroupBean gmembersbean;
    public boolean Edit = false;
    public String sorting = "online";
    private Button edit;
    boolean isfromedit_diagnosis = false;
    PatientDescriptionBean pDescBean;
    GroupMemberBean memberbean;
    RoleAccessBean roleAccessBean;
    RoleEditRndFormBean editRndFormBean;
    RolePatientManagementBean rolePatientManagementBean;
    RoleTaskMgtBean roleTaskMgtBean;
    Dialog dialog1;
    public static String Patient_ActiveReportID;
    public static String PatientID_Active;
    private boolean isMakeActiveClicked=false;
    private ListView listview;

    public static PatientRoundingFragment newInstance(Context context) {
        try {
            if (patientRoundingFragment == null) {
                patientRoundingFragment = new PatientRoundingFragment();
                patientRoundingFragment.setContext(context);
                calldisp = CallDispatcher.getCallDispatcher(context);

            }
            return patientRoundingFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return patientRoundingFragment;
        }
    }

    public void setContext(Context cxt) {
        this.mainContext = cxt;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Button select = (Button) getActivity().findViewById(R.id.btn_brg);
        select.setVisibility(View.GONE);
        RelativeLayout mainHeader = (RelativeLayout) getActivity().findViewById(R.id.mainheader);
        mainHeader.setVisibility(View.VISIBLE);
        LinearLayout contact_layout = (LinearLayout) getActivity().findViewById(R.id.contact_layout);
        contact_layout.setVisibility(View.GONE);
        EditText search_box = (EditText) getActivity().findViewById(R.id.search_box);
        search_box.setVisibility(View.GONE);
        final Button imVw = (Button) getActivity().findViewById(R.id.im_view);
        imVw.setVisibility(View.GONE);


        edit = (Button) getActivity().findViewById(R.id.btn_settings);
        edit.setVisibility(View.VISIBLE);
        edit.setBackgroundResource(R.drawable.navigation_edit);
        edit.setText("");

        memberbean = DBAccess.getdbHeler().getMemberDetails(gBean.getGroupId(), CallDispatcher.LoginUser);
        roleAccessBean = DBAccess.getdbHeler().getRoleAccessDetails(gBean.getGroupId(), memberbean.getRole());
        editRndFormBean = DBAccess.getdbHeler().getRoleEditRoundingDetails(gBean.getGroupId(), memberbean.getRole());
        rolePatientManagementBean = DBAccess.getdbHeler().getRolePatientManagement(gBean.getGroupId(), memberbean.getRole());
        roleTaskMgtBean = DBAccess.getdbHeler().getRoleTaskManagement(gBean.getGroupId(), memberbean.getRole());
        final Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
        plusBtn.setVisibility(View.VISIBLE);
        plusBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.dot));
        gmembersbean = DBAccess.getdbHeler().getGroupAndMembers(
                "select * from groupdetails where groupid="
                        + gBean.getGroupId());
        final Vector<UserBean> memberslist = new Vector<UserBean>();
        UserBean ubean = new UserBean();
        ubean.setBuddyName(gmembersbean.getOwnerName());
        if (gmembersbean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser)) {
            ubean.setFirstname(SingleInstance.myAccountBean.getFirstname() + " " + SingleInstance.myAccountBean.getLastname());
            ubean.setProfilePic(SingleInstance.myAccountBean.getPhoto());
        } else
            for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
                if (bib.getName().equalsIgnoreCase(gmembersbean.getOwnerName())) {
                    ubean.setFirstname(bib.getFirstname() + " " + bib.getLastname());
                    ubean.setProfilePic(bib.getProfile_picpath());
                    break;
                }
            }
        if (gmembersbean != null) {
            if (gmembersbean.getActiveGroupMembers() != null
                    && gmembersbean.getActiveGroupMembers().length() > 0) {
                String[] mlist = (gmembersbean.getActiveGroupMembers())
                        .split(",");
                for (String tmp : mlist) {
                    UserBean uBean = new UserBean();

                    for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
                        if (bib.getName().equalsIgnoreCase(tmp)) {
                            uBean.setFirstname(bib.getFirstname() + " " + bib.getLastname());
                            break;
                        } else
                            uBean.setFirstname(tmp);
                        uBean.setStatus(bib.getStatus());
                        ProfileBean pbean = DBAccess.getdbHeler().getProfileDetails(tmp);
                        uBean.setProfilePic(pbean.getPhoto());
                    }
                    uBean.setBuddyName(tmp);
                    memberslist.add(uBean);
                }
            }
        }
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(mainContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.roundeditmanagement);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.horizontalMargin = 15;
                Window window = dialog.getWindow();
                window.setBackgroundDrawableResource((R.color.lblack));
                window.setAttributes(lp);
                window.setGravity(Gravity.BOTTOM);
                dialog.show();
                TextView task = (TextView) dialog.findViewById(R.id.round_edit);
                final TextView assign = (TextView) dialog.findViewById(R.id.round_role);
                TextView newTask = (TextView) dialog.findViewById(R.id.roun_new_task);
                TextView newPatient = (TextView) dialog.findViewById(R.id.roun_new_patient);
                TextView dischargePatient = (TextView) dialog.findViewById(R.id.roun_ownership);
                TextView deletePatient = (TextView) dialog.findViewById(R.id.roun_delete);
                TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                task.setText("Create New Task");
                assign.setText("Assign Member");
                newTask.setVisibility(View.GONE);
                newPatient.setVisibility(View.GONE);
                dischargePatient.setText("Discharge Patient");
                deletePatient.setText("Remove Patient");
                dischargePatient.setBackgroundColor(mainContext.getResources().getColor(R.color.green));
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (!(gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) ||
                        (memberbean.getAdmin() != null && memberbean.getAdmin().equalsIgnoreCase("1")) ||
                        rolePatientManagementBean.getModify() != null && rolePatientManagementBean.getModify().equalsIgnoreCase("1"))) {
                    assign.setEnabled(false);
                    assign.setBackgroundColor(mainContext.getResources().getColor(R.color.black));
                }
                if (!(gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) ||
                        (memberbean.getAdmin() != null && memberbean.getAdmin().equalsIgnoreCase("1")) ||
                        rolePatientManagementBean.getDischarge() != null && rolePatientManagementBean.getDischarge().equalsIgnoreCase("1"))) {
                    dischargePatient.setEnabled(false);
                    dischargePatient.setBackgroundColor(mainContext.getResources().getColor(R.color.black));
                }
                if (!(gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) ||
                        (memberbean.getAdmin() != null && memberbean.getAdmin().equalsIgnoreCase("1")) ||
                        rolePatientManagementBean.getDelete() != null && rolePatientManagementBean.getDelete().equalsIgnoreCase("1"))) {
                    deletePatient.setEnabled(false);
                    deletePatient.setBackgroundColor(mainContext.getResources().getColor(R.color.black));
                }
                if (!(gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) ||
                        (memberbean.getAdmin() != null && memberbean.getAdmin().equalsIgnoreCase("1")) ||
                        roleAccessBean.getTaskmanagement() != null && roleAccessBean.getTaskmanagement().equalsIgnoreCase("1"))) {
//                    if(!(memberbean.getRole().equalsIgnoreCase(roleTaskMgtBean.getTattending())
//                            || memberbean.getRole().equalsIgnoreCase(roleTaskMgtBean.getTchiefresident()) ||
//                            memberbean.getRole().equalsIgnoreCase(roleTaskMgtBean.getTfellow()) || memberbean.getRole().equalsIgnoreCase(roleTaskMgtBean.getTmedstudent())
//                            || memberbean.getRole().equalsIgnoreCase(roleTaskMgtBean.getTresident()))) {
                    task.setEnabled(false);
                    task.setBackgroundColor(mainContext.getResources().getColor(R.color.black));
//                    }
                }
                assign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> buddylist = new ArrayList<String>();
                        Intent intent = new Intent(SingleInstance.mainContext,
                                AddGroupMembers.class);
                        for (UserBean userBean : memberslist) {

                            buddylist.add(userBean.getBuddyName());
                        }
                        intent.putExtra("groupid", pBean.getGroupid());

                        intent.putStringArrayListExtra("buddylist", buddylist);
                        intent.putExtra("fromRounding", true);
                        Log.i("AAAA", "members list " + buddylist.size());
                        startActivityForResult(intent, 3);
                        dialog.dismiss();
                    }
                });
                task.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mainContext, TaskCreationActivity.class);
                        intent.putExtra("groupid", pBean.getGroupid());
                        intent.putExtra("patientid", pBean.getPatientid());
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                dischargePatient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        showAlertForDeletepatient();
//                        showprogress();
//                        WebServiceReferences.webServiceClient.dischargePatient(CallDispatcher.LoginUser, pBean.getGroupid(), patientid, patientRoundingFragment);
                    }
                });
                deletePatient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        showprogress();
                        WebServiceReferences.webServiceClient.dischargePatient(CallDispatcher.LoginUser, pBean.getGroupid(), patientid, patientRoundingFragment);
                    }
                });
            }
        });

        TextView title = (TextView) getActivity().findViewById(
                R.id.activity_main_content_title);
        title.setEllipsize(TextUtils.TruncateAt.END);
        title.setWidth(420);
        title.setHeight(200);
        title.setSingleLine();
        title.setVisibility(View.VISIBLE);
        patientName = pBean.getFirstname() + " " + pBean.getLastname();
        title.setText(patientName);

        final Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleInstance.mainContext, GroupChatActivity.class);
                intent.putExtra("groupid", pBean.getGroupid());
                intent.putExtra("isRounding", true);
                intent.putExtra("isReq", "p");
                SingleInstance.mainContext.startActivity(intent);

            }
        });
        _rootView = null;
        if (_rootView == null) {

            _rootView = inflater.inflate(R.layout.fragment_rounding_patient, null);
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            try {

                final LinearLayout rounding = (LinearLayout) _rootView.findViewById(R.id.rounding);
                final LinearLayout task = (LinearLayout) _rootView.findViewById(R.id.task);
                final LinearLayout comments = (LinearLayout) _rootView.findViewById(R.id.comments);
                final LinearLayout members = (LinearLayout) _rootView.findViewById(R.id.members);
                tv_rounding = (TextView) _rootView.findViewById(R.id.tv_rounding);
                tv_task = (TextView) _rootView.findViewById(R.id.tv_task);
                rounding_img = (ImageView) _rootView.findViewById(R.id.rounding_img);
                task_img = (ImageView) _rootView.findViewById(R.id.task_img);
                view_rounding = (View) _rootView.findViewById(R.id.view_rounding);
                view_task = (View) _rootView.findViewById(R.id.view_task);
                view_comments = (View) _rootView.findViewById(R.id.view_comments);
                view_members = (View) _rootView.findViewById(R.id.view_members);
                tv_comments = (TextView) _rootView.findViewById(R.id.tv_comments);
                tv_members = (TextView) _rootView.findViewById(R.id.tv_members);
                comments_img = (ImageView) _rootView.findViewById(R.id.comments_img);
                members_img = (ImageView) _rootView.findViewById(R.id.members_img);
                patientDetails();
                edit.setVisibility(View.VISIBLE);
                patientid = pBean.getPatientid();
                rounding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        patientDetails();
                        setDefault();
                        edit.setVisibility(View.VISIBLE);
                        rounding_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_patients_white));
                        tv_rounding.setTextColor(getResources().getColor(R.color.white));
                        view_rounding.setVisibility(View.VISIBLE);

                    }
                });
                task.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDefault();
                        edit.setVisibility(View.GONE);
                        task_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_tasks_white));
                        tv_task.setTextColor(getResources().getColor(R.color.white));
                        view_task.setVisibility(View.VISIBLE);
                        taskProcess();

                    }
                });
                comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDefault();
                        edit.setVisibility(View.GONE);
                        comments_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_comments_white));
                        tv_comments.setTextColor(getResources().getColor(R.color.white));
                        view_comments.setVisibility(View.VISIBLE);
                        CommentsProcess();
                    }
                });
                members.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDefault();
                        edit.setVisibility(View.GONE);
                        members_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_members_white));
                        tv_members.setTextColor(getResources().getColor(R.color.white));
                        view_members.setVisibility(View.VISIBLE);
                        MembersProcess();
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            ((ViewGroup) _rootView.getParent()).removeView(_rootView);
        return _rootView;
    }

    private void showAlertForDeletepatient()
    {
        try {
            if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder.setTitle("Warning !");
                builder.setMessage(
                        "Are you sure want to delete this patient?")
                        .setCancelable(false)
                        .setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes)
                                ,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        if (!WebServiceReferences.running) {
                                            calldisp.startWebService(
                                                    getResources()
                                                            .getString(
                                                                    R.string.service_url),
                                                    "80");
                                        }
                                        showprogress();
                                        WebServiceReferences.webServiceClient.dischargePatient(CallDispatcher.LoginUser, pBean.getGroupid(), patientid, patientRoundingFragment);
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert1 = builder.create();
                alert1.show();
            } else {
                ShowError("Info",
                        "Check Internet Connection Unable to Connect server",
                        mainContext);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void ShowError(String Title, String Message, Context con) {
        try {
            AlertDialog confirmation = new AlertDialog.Builder(con).create();
            confirmation.setTitle(Title);
            confirmation.setMessage(Message);
            confirmation.setCancelable(true);
            confirmation.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            });

            confirmation.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void MembersProcess() {
        final LinearLayout content = (LinearLayout) _rootView.findViewById(R.id.content);
        LayoutInflater layoutInflater = (LayoutInflater) mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content.removeAllViews();
        final View v1 = layoutInflater.inflate(R.layout.members_layout, content);

        final ListView list = (ListView) v1.findViewById(R.id.memberslist);
        final Button alpha_sort = (Button) v1.findViewById(R.id.alpha_sort);
        final Button online_sort = (Button) v1.findViewById(R.id.online_sort);
        final Button role_sort = (Button) v1.findViewById(R.id.role_sort);


        final Vector<UserBean> memberslist = new Vector<UserBean>();
        if (pBean != null) {
            if (pBean.getAssignedmembers() != null
                    && pBean.getAssignedmembers().length() > 0) {
                String[] mlist = (pBean.getAssignedmembers())
                        .split(",");
                for (String tmp : mlist) {
                    if (!tmp.equalsIgnoreCase("") && tmp.length() > 0) {
                        UserBean uBean = new UserBean();

                        if (tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
                            ProfileBean bean = SingleInstance.myAccountBean;
                            uBean.setFirstname(bean.getFirstname() + " " + bean.getLastname());
                            uBean.setStatus(SingleInstance.mainContext.loadCurrentStatus());
                            String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/COMMedia/" + bean.getPhoto();
                            uBean.setProfilePic(pic_Path);
                        } else
                            for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
                                if (bib.getName().equalsIgnoreCase(tmp)) {
                                    uBean.setFirstname(bib.getFirstname() + " " + bib.getLastname());
                                    uBean.setStatus(bib.getStatus());
                                    uBean.setProfilePic(bib.getProfile_picpath());
                                }
                            }
                        uBean.setBuddyName(tmp);
                        memberslist.add(uBean);
                    }
                }
            }
        }

        online_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = false;
                online_sort.setTextColor(getResources().getColor(R.color.white));
                alpha_sort.setTextColor(getResources().getColor(R.color.snazlgray));
                role_sort.setTextColor(getResources().getColor(R.color.snazlgray));
                sorting = "online";
                MembersAdapter adapter = new MembersAdapter(mainContext, R.layout.rounding_member_row, getOnlineList(memberslist));
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        alpha_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                title = true;
                alpha_sort.setTextColor(getResources().getColor(R.color.white));
                online_sort.setTextColor(getResources().getColor(R.color.snazlgray));
                role_sort.setTextColor(getResources().getColor(R.color.snazlgray));

                sorting = "alpha";
                Collections.sort(memberslist, new PatientListComparator());
                if (islistAZ) {
                    islistAZ = false;
                    alpha_sort.setText("A>Z");
                } else {
                    islistAZ = true;
                    alpha_sort.setText("Z>A");
                    Collections.reverse(memberslist);
                }
                MembersAdapter adapter = new MembersAdapter(mainContext, R.layout.rounding_member_row, memberslist);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        role_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = false;
                alpha_sort.setTextColor(getResources().getColor(R.color.snazlgray));
                online_sort.setTextColor(getResources().getColor(R.color.snazlgray));
                role_sort.setTextColor(getResources().getColor(R.color.white));
                sorting = "role";
                Vector<UserBean> templist = new Vector<UserBean>();
                for (UserBean userb : memberslist) {
                    if (userb.getRole() != null && !userb.getRole().equalsIgnoreCase(""))
                        templist.add(userb);
                }
                MembersAdapter adapter = new MembersAdapter(mainContext, R.layout.rounding_member_row, templist);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        Collections.sort(memberslist, new PatientListComparator());
        MembersAdapter adapter = new MembersAdapter(mainContext, R.layout.rounding_member_row, memberslist);
        list.setAdapter(adapter);


    }

    public static Vector<UserBean> getOnlineList(Vector<UserBean> vectorBean) {
        String status = null;
        Vector<UserBean> tempList = new Vector<UserBean>();
        Vector<UserBean> onlinelist = new Vector<UserBean>();
        tempList.clear();
        for (UserBean sortlistbeanstatus : vectorBean) {
            status = sortlistbeanstatus.getStatus();
            Log.i("AAAA", "online list " + status);
            if (status != null) {
                if (status.equalsIgnoreCase("Online"))
                    onlinelist.add(sortlistbeanstatus);
            }
        }
        if (onlinelist.size() > 0)
            tempList.addAll(onlinelist);

        return tempList;

    }

    public class MembersAdapter extends ArrayAdapter<UserBean> {

        private LayoutInflater inflater = null;
        private ViewHolder holder;
        private ImageLoader imageLoader;
        private Vector<UserBean> result;

        public MembersAdapter(Context context, int resource, Vector<UserBean> objects) {
            super(context, resource, objects);
            imageLoader = new ImageLoader(context);
            result = new Vector<UserBean>();
            result.addAll(objects);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            try {
                holder = new ViewHolder();
                if (convertView == null) {
                    inflater = (LayoutInflater) mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.rounding_member_row, null);
                    holder.selectUser = (CheckBox) convertView.findViewById(R.id.sel_buddy);
                    holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                    holder.edit = (ImageView) convertView.findViewById(R.id.edit);
                    holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                    holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                    holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                    holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
                    holder.rights = (TextView) convertView.findViewById(R.id.rights);
                    holder.memberRole = (TextView) convertView.findViewById(R.id.position);
                    convertView.setTag(holder);
                } else
                    holder = (ViewHolder) convertView.getTag();
                final UserBean bib = result.get(i);
                if (bib != null) {
                    if (bib.getProfilePic() != null) {
                        String pic_Path = bib.getProfilePic();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
                    if (bib.getStatus() != null) {
                        Log.i("AAAA", "Buddy adapter status " + bib.getStatus());
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
                    if (title = false) {
                        holder.header_title.setVisibility(View.GONE);
                    } else if (title = true) {
                        holder.header_title.setVisibility(View.VISIBLE);
                        String cname1, cname2;

                        if (bib.getFirstname() != null) {
                            cname1 = String.valueOf(bib.getFirstname().charAt(0));
                            holder.header_title.setText(cname1.toUpperCase());
                        }
                        if (gmembersbean != null) {
                            if (gmembersbean.getInviteMembers() != null
                                    && gmembersbean.getInviteMembers().length() > 0) {
                                String[] listRole = (gmembersbean.getInviteMembers())
                                        .split(",");
                                for (String tmp : listRole) {
                                    GroupMemberBean bean12 = DBAccess.getdbHeler().getMemberDetails(gBean.getGroupId(), tmp);
                                    boolean found = false;
                                    for (String element : listRole) {
                                        if (tmp.equals(bib.getBuddyName())) {
                                            Log.i("AAAA", "#########string Role " + bean12.getRole() + " element " + element);
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (found) {
                                        holder.memberRole.setText(bean12.getRole());
                                    }
                                }
                            }
                        }
                        if (i > 0) {
                            final BuddyInformationBean bean = ContactsFragment.getBuddyList().get(i - 1);
                            if (bean.getFirstname() != null && bib.getFirstname() != null) {
                                cname1 = String.valueOf(bib.getFirstname().charAt(0));
                                cname2 = String.valueOf(bean.getFirstname().charAt(0));
                                if (cname1.equalsIgnoreCase(cname2)) {
                                    holder.header_title.setVisibility(View.GONE);
                                } else {
                                    holder.header_title.setVisibility(View.VISIBLE);
                                }
                            }

                        }
                    }
                    holder.selectUser.setVisibility(View.GONE);
                    holder.rights.setVisibility(View.GONE);
                    holder.edit.setVisibility(View.GONE);
                    holder.buddyName.setText(bib.getFirstname());
//                    if(bib.getOccupation()!=null)
//                     holder.occupation.setText(bib.getOccupation());
                    ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(bib.getBuddyName());
                    holder.occupation.setText(pBean.getSpeciality());

                    if (bib.isSelected()) {
                        holder.occupation.setText("Owner");
                        holder.occupation.setTextColor(getResources().getColor(R.color.green));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    public static class ViewHolder {
        CheckBox selectUser;
        ImageView buddyicon, edit;
        ImageView statusIcon;
        TextView buddyName;
        TextView occupation;
        TextView header_title, rights, memberRole;
    }

    public void setGroupBean(GroupBean bean) {
        gBean = bean;
    }

    public void setPatientDetailsBean(PatientDetailsBean pbean) {
        pBean = pbean;
    }

    private void setDefault() {
        rounding_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_patients));
        task_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_tasks));
        comments_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_comments));
        members_img.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_members));
        tv_rounding.setTextColor(getResources().getColor(R.color.black));
        tv_task.setTextColor(getResources().getColor(R.color.black));
        tv_comments.setTextColor(getResources().getColor(R.color.black));
        tv_members.setTextColor(getResources().getColor(R.color.black));
        view_comments.setVisibility(View.GONE);
        view_members.setVisibility(View.GONE);
        view_rounding.setVisibility(View.GONE);
        view_task.setVisibility(View.GONE);
    }

    private void showDiagnosisHistory(String edit_content, String title) {
        dialog1 = new Dialog(mainContext);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.diagnosis_history);
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog1.getWindow().setBackgroundDrawableResource(R.color.trans_black2);
        dialog1.show();
        Button cancel = (Button) dialog1.findViewById(R.id.cancel);
        TextView header_name = (TextView) dialog1.findViewById(R.id.header_title);
        header_name.setText(title);
         listview = (ListView) dialog1.findViewById(R.id.listview);
        String strGetQry = "select * from patientdescription where groupid='"
                + pBean.getGroupid() + "'and patientid='" + pBean.getPatientid() + "'and diagnosis!='"+""+"' ORDER BY modifieddate DESC";
        isfromedit_diagnosis = true;
        Vector<PatientDescriptionBean> comments = DBAccess.getdbHeler().getseallcomments(strGetQry);
        Log.d("listvalues", "string" + comments.size());
        seealladapter = new Seealladapter(mainContext, R.layout.seeall_row, comments);
        listview.setAdapter(seealladapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

    }

    public void refreshSeeAllAdapter() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                String strGetQry = "select * from patientdescription where groupid='"
                        + pBean.getGroupid() + "'and patientid='" + pBean.getPatientid() + "'and diagnosis!='"+" "+"' ORDER BY modifieddate DESC";
                isfromedit_diagnosis = true;
                Vector<PatientDescriptionBean> comments = DBAccess.getdbHeler().getseallcomments(strGetQry);
                Log.d("listvalues", "string" + comments.size());
                seealladapter = new Seealladapter(mainContext, R.layout.seeall_row, comments);
                listview.setAdapter(seealladapter);
                seealladapter.notifyDataSetChanged();

            }
        });
    }
    private void editDiagnosisDescription(String editingContent, String edittitle, final String edithint) {
        final Dialog dialog1 = new Dialog(mainContext);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.edit_buddy_diagnosis);
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog1.getWindow().setBackgroundDrawableResource(R.color.trans_black2);
        dialog1.show();
        Button cancel = (Button) dialog1.findViewById(R.id.cancel);
        Button edit_done = (Button) dialog1.findViewById(R.id.edit_diag_done);
        ListView listview = (ListView) dialog1.findViewById(R.id.listview);
        final TextView edit_title = (TextView) dialog1.findViewById(R.id.edit_title);
        edit_title.setText(edittitle);
        final TextView newDesc = (TextView) dialog1.findViewById(R.id.newDesc);
        newDesc.setText(edithint);
        final EditText newDescription = (EditText) dialog1.findViewById(R.id.new_descrip);
        if (editingContent.length() > 0) {
            newDescription.setText(editingContent);
        } else {
            newDescription.setHint(edithint);
        }
        isfromedit_diagnosis = false;
        String strGetQry = "select * from patientdescription where groupid='"
                + pBean.getGroupid() + "'and patientid='" + pBean.getPatientid() + "'and diagnosis!='"+" "+"'ORDER BY modifieddate DESC";

        Vector<PatientDescriptionBean> comments = DBAccess.getdbHeler().getseallcomments(strGetQry);
        Log.d("listvalues", "string" + comments.size());
        seealladapter = new Seealladapter(mainContext, R.layout.seeall_row, comments);
        listview.setAdapter(seealladapter);

        newDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    newDesc.setVisibility(View.VISIBLE);
                else
                    newDesc.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pastingContentCopy = newDescription.getText().toString();
                Log.i("SSS", "content copied" + pastingContentCopy);
                pcBean.setPatientid(pBean.getPatientid());
                pcBean.setGroupid(gBean.getGroupId());
//                if (reportid != null)
//                    pcBean.setReportid(reportid);
                pcBean.setReportcreator(CallDispatcher.LoginUser);

                if (editTag == 0) {
                    if (pastingContentCopy != null)
                        pcBean.setDiagnosis(pastingContentCopy);
                    diagnosis.setText(pastingContentCopy);
                } else if (editTag == 1) {
                    if (pastingContentCopy != null)
                        pcBean.setMedications(pastingContentCopy);
                    medications.setText(pastingContentCopy);
                } else if (editTag == 2) {
                    if (pastingContentCopy != null)
                        pcBean.setTestandvitals(pastingContentCopy);
                    testandvitals.setText(pastingContentCopy);
                } else if (editTag == 3) {
                    if (pastingContentCopy != null)
                        pcBean.setHospitalcourse(pastingContentCopy);
                    hospital.setText(pastingContentCopy);
                } else if (editTag == 4) {
                    if (pastingContentCopy != null)
                        pcBean.setConsults(pastingContentCopy);
                    consults.setText(pastingContentCopy);
                } else {
                    if (pastingContentCopy != null)
                        pcBean.setCurrentstatus(pastingContentCopy);
//                    currentstatus.setText(pastingContentCopy);
                    if (pastingContentCopy.length() > 0) {
                        String[] split = pastingContentCopy.split(" ");
                        currentstatus_lay.removeAllViews();
                        currentstatus_lay1.removeAllViews();
                        pDescBean.setCurrentstatus(pastingContentCopy);
                        for (int i = 0; i < split.length; i++) {
                            if (i <= 5) {
                                TextView dynamicTextView = new TextView(mainContext);
                                LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 60);
                                dim.leftMargin = 20;
                                dynamicTextView.setLayoutParams(dim);
                                dynamicTextView.setGravity(Gravity.CENTER);
                                dynamicTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.sender_border));
                                if (split[i].equalsIgnoreCase("critical"))
                                    dynamicTextView.setTextColor(getResources().getColor(R.color.pink_tv));
                                else if (split[i].equalsIgnoreCase("stable")) {
                                    dynamicTextView.setTextColor(getResources().getColor(R.color.green));
                                } else if (split[i].equalsIgnoreCase("sick")) {
                                    dynamicTextView.setTextColor(getResources().getColor(R.color.yellow));
                                }
                                if (!split[i].equalsIgnoreCase("") && split.length > 0) {
                                    dynamicTextView.setText(split[i]);
                                    currentstatus_lay.addView(dynamicTextView);
                                }
                            } else if (i > 5) {
                                TextView dynamicTextView = new TextView(mainContext);
                                LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 60);
                                dim.leftMargin = 20;
                                dynamicTextView.setLayoutParams(dim);
                                dynamicTextView.setGravity(Gravity.CENTER);
                                dynamicTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.sender_border));
                                if (split[i].equalsIgnoreCase("critical"))
                                    dynamicTextView.setTextColor(getResources().getColor(R.color.pink_tv));
                                else if (split[i].equalsIgnoreCase("stable")) {
                                    dynamicTextView.setTextColor(getResources().getColor(R.color.green));
                                } else if (split[i].equalsIgnoreCase("sick")) {
                                    dynamicTextView.setTextColor(getResources().getColor(R.color.yellow));
                                }
                                dynamicTextView.setText(split[i]);
                                currentstatus_lay1.addView(dynamicTextView);
                            }

                        }
                    }
                }
                deleteExisting_content();
                showprogress();
                Log.i("BBB", "call webservice from edit_done");
                pcBean.setReportid("");
                WebServiceReferences.webServiceClient.SetPatientDescription(pcBean, patientRoundingFragment);
                pcBean.setDate(getCurrentDateandTime());
//                if (edithint.equalsIgnoreCase("diagnosis"))
//                    DBAccess.getdbHeler().insertseallcomments(pcBean);
                dialog1.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
    }

    private void deleteExisting_content() {
        if (editTag != 0)
            pcBean.setDiagnosis("");
        if (editTag != 1)
            pcBean.setMedications("");
        if (editTag != 2)
            pcBean.setTestandvitals("");
        if (editTag != 3)
            pcBean.setHospitalcourse("");
        if (editTag != 4)
            pcBean.setConsults("");
        if (editTag != 5)
            pcBean.setCurrentstatus("");
    }
    private void taskProcess() {
        final LinearLayout content = (LinearLayout) _rootView.findViewById(R.id.content);
        content.removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = layoutInflater.inflate(R.layout.round_task, content);
        tv_status = (TextView) v1.findViewById(R.id.tv_status);
        tv_assigned = (TextView) v1.findViewById(R.id.tv_assigned);
        ImageView plusBtn = (ImageView) v1.findViewById(R.id.plusBtn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) ||
                        (memberbean.getAdmin() != null && memberbean.getAdmin().equalsIgnoreCase("1")) ||
                        (roleAccessBean.getTaskmanagement() != null && roleAccessBean.getTaskmanagement().equalsIgnoreCase("1"))) {
                    Intent intent = new Intent(mainContext, TaskCreationActivity.class);
                    intent.putExtra("groupid", gBean.getGroupId());
                    intent.putExtra("patientid", pBean.getPatientid());
                    startActivity(intent);
                } else
                    showToast("You have no access to create Task ");
            }
        });
        LinearLayout status = (LinearLayout) v1.findViewById(R.id.status);
        LinearLayout assigned = (LinearLayout) v1.findViewById(R.id.assigned);
        tasklistView = (ListView) v1.findViewById(R.id.listview_task);
        statusMode = "showall";
        assignedMode = "showall";
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusDialog();
            }
        });
        assigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignedTaskDialog();
            }
        });
        strQuery = "select * from taskdetails where groupid='" + gBean.getGroupId() + "'and patientid='" + patientid + "'";
        Vector<TaskDetailsBean> tasklist = DBAccess.getdbHeler().getAllTaskDetails(strQuery);
        Collections.sort(tasklist, new TaskDateComparator());
        Vector<TaskDetailsBean> taskList = GroupChatActivity.getdatelist(tasklist);
        final RoundingTaskAdapter taskAdapter = new RoundingTaskAdapter(mainContext, R.layout.round_task_row, taskList);
        tasklistView.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();
        tasklistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskDetailsBean tBean = (TaskDetailsBean) taskAdapter.getItem(i);
                if (gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) ||
                        tBean.getTaskstatus().equalsIgnoreCase("0") && (roleAccessBean.getTaskmanagement() != null && roleAccessBean.getTaskmanagement().equalsIgnoreCase("1"))) {
                    Log.i("ppp", "roleAccess of the task_member" + roleAccessBean.getTaskmanagement());
                    Intent intent = new Intent(SingleInstance.mainContext, TaskCreationActivity.class);
                    intent.putExtra("groupid", tBean.getGroupid());
                    intent.putExtra("taskid", tBean.getTaskId());
                    intent.putExtra("isEdit", true);
                    SingleInstance.mainContext.startActivity(intent);
                }
            }
        });
        tasklistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskDetailsBean tBean = (TaskDetailsBean) taskAdapter.getItem(i);
                deleteTask(tBean.getTaskId(), tBean.getGroupid(), tBean.getTaskdesc());
                return true;
            }
        });

    }

    final String[] status = new String[1];

    private void patientDetails() {
        final LinearLayout content = (LinearLayout) _rootView.findViewById(R.id.content);
        content.removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = layoutInflater.inflate(R.layout.roundingforpatient, content);

        TextView seeAll_diagnosis = (TextView) v1.findViewById(R.id.see_all);
        TextView seeAll_medications = (TextView) v1.findViewById(R.id.see_all1);
        TextView seeAll_test = (TextView) v1.findViewById(R.id.see_all3);
        TextView seeAll_hospital = (TextView) v1.findViewById(R.id.see_all4);
        TextView seeAll_consult = (TextView) v1.findViewById(R.id.see_all5);
        final Button edit_diagnosis = (Button) v1.findViewById(R.id.edit_diagnosis);
        final Button edit_medications = (Button) v1.findViewById(R.id.edit_medications);
        final Button edit_tests = (Button) v1.findViewById(R.id.edit_tests);
        final Button edit_hospital = (Button) v1.findViewById(R.id.edit_hospital);
        final Button edit_consults = (Button) v1.findViewById(R.id.edit_consults);
        final Button edit_status = (Button) v1.findViewById(R.id.edit_status);
        TextView tv_currentstatus = (TextView) v1.findViewById(R.id.tv_status);
        final LinearLayout status_click = (LinearLayout) v1.findViewById(R.id.status_click);

        diagnosis = (TextView) v1.findViewById(R.id.diagnosis);
        medications = (TextView) v1.findViewById(R.id.medications);
        testandvitals = (TextView) v1.findViewById(R.id.testandvitals);
        hospital = (TextView) v1.findViewById(R.id.hospitalcourse);
        consults = (TextView) v1.findViewById(R.id.consults);
        currentstatus_lay = (LinearLayout) v1.findViewById(R.id.currentstatus_lay);
        currentstatus_lay1 = (LinearLayout) v1.findViewById(R.id.currentstatus_lay1);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Edit) {
                    if (gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) ||
                            (memberbean.getAdmin() != null && memberbean.getAdmin().equalsIgnoreCase("1"))) {
                        edit_diagnosis.setVisibility(View.VISIBLE);
                        edit_medications.setVisibility(View.VISIBLE);
                        edit_tests.setVisibility(View.VISIBLE);
                        edit_hospital.setVisibility(View.VISIBLE);
                        edit_consults.setVisibility(View.VISIBLE);
                        edit_status.setVisibility(View.VISIBLE);

                        Edit = true;
                    } else if (roleAccessBean.getEditroundingform() != null &&
                            roleAccessBean.getEditroundingform().equalsIgnoreCase("1")) {
                        if (editRndFormBean.getDiagnosis() != null && editRndFormBean.getDiagnosis().equalsIgnoreCase("1"))
                            edit_diagnosis.setVisibility(View.VISIBLE);
                        if (editRndFormBean.getNotes() != null && editRndFormBean.getNotes().equalsIgnoreCase("1"))
                            edit_medications.setVisibility(View.VISIBLE);
                        if (editRndFormBean.getTestandvitals() != null && editRndFormBean.getTestandvitals().equalsIgnoreCase("1"))
                            edit_tests.setVisibility(View.VISIBLE);
                        if (editRndFormBean.getHospitalcourse() != null && editRndFormBean.getHospitalcourse().equalsIgnoreCase("1"))
                            edit_hospital.setVisibility(View.VISIBLE);
                        if (editRndFormBean.getConsults() != null && editRndFormBean.getConsults().equalsIgnoreCase("1"))
                            edit_consults.setVisibility(View.VISIBLE);
                        if (editRndFormBean.getStatus() != null && editRndFormBean.getStatus().equalsIgnoreCase("1"))
                            edit_status.setVisibility(View.VISIBLE);
                        Edit = true;
                    }
                } else {
                    edit_diagnosis.setVisibility(View.GONE);
                    edit_medications.setVisibility(View.GONE);
                    edit_tests.setVisibility(View.GONE);
                    edit_hospital.setVisibility(View.GONE);
                    edit_consults.setVisibility(View.GONE);
                    edit_status.setVisibility(View.GONE);


                    Edit = false;
                }
            }
        });
//        if (pBean.getPatientid() != null)
//            pDescBean = DBAccess.getdbHeler().getPatientDescriptionDetails(pBean.getPatientid());
        pDescBean=getLatestDescr_patient();
        Log.i("BBB", "getPatientDesc from DB ==>Active ReportID###=====>" + Patient_ActiveReportID + " patientid###==>" + pBean.getPatientid());
        if (pDescBean != null) {
            reportid = pDescBean.getReportid();
            if (pDescBean.getCurrentstatus() != null) {
                if (pDescBean.getCurrentstatus().length() > 0) {
                    String[] split = pDescBean.getCurrentstatus().split(" ");
                    for (int i = 0; i < split.length; i++) {
                        if (i <= 5) {
                            TextView dynamicTextView = new TextView(mainContext);
                            LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 60);
                            dim.leftMargin = 15;
                            dynamicTextView.setLayoutParams(dim);
                            dynamicTextView.setGravity(Gravity.CENTER);
                            dynamicTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.sender_border));
                            if (split[i].equalsIgnoreCase("critical"))
                                dynamicTextView.setTextColor(getResources().getColor(R.color.pink_tv));
                            else if (split[i].equalsIgnoreCase("stable")) {
                                dynamicTextView.setTextColor(getResources().getColor(R.color.green));
                            } else if (split[i].equalsIgnoreCase("sick")) {
                                dynamicTextView.setTextColor(getResources().getColor(R.color.yellow));
                            }
                            if (!split[i].equalsIgnoreCase("") && split.length > 0) {
                                dynamicTextView.setText(split[i]);
                                currentstatus_lay.addView(dynamicTextView);
                            }
                        } else if (i > 5) {
                            TextView dynamicTextView = new TextView(mainContext);
                            LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 60);
                            dim.leftMargin = 15;
                            dynamicTextView.setLayoutParams(dim);
                            dynamicTextView.setGravity(Gravity.CENTER);
                            dynamicTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.sender_border));
                            if (split[i].equalsIgnoreCase("critical"))
                                dynamicTextView.setTextColor(getResources().getColor(R.color.pink_tv));
                            else if (split[i].equalsIgnoreCase("stable")) {
                                dynamicTextView.setTextColor(getResources().getColor(R.color.green));
                            } else if (split[i].equalsIgnoreCase("sick")) {
                                dynamicTextView.setTextColor(getResources().getColor(R.color.yellow));
                            }
                            dynamicTextView.setText(split[i]);
                            currentstatus_lay1.addView(dynamicTextView);
                        }
                    }
                }
            }
            Log.i("BBB", "*******desc diag==>" + pDescBean.getDiagnosis() + " medi***==>" + pDescBean.getMedications() + "test***==>" + pDescBean.getTestandvitals() + "hosp***==> " + pDescBean.getHospitalcourse()
                    + "cosult***" + pDescBean.getConsults());
            if (pDescBean.getDiagnosis() != null)
                diagnosis.setText(pDescBean.getDiagnosis());
            if (pDescBean.getMedications() != null)
                medications.setText(pDescBean.getMedications());
            if (pDescBean.getTestandvitals() != null)
                testandvitals.setText(pDescBean.getTestandvitals());
            if (pDescBean.getHospitalcourse() != null)
                hospital.setText(pDescBean.getHospitalcourse());
            if (pDescBean.getConsults() != null)
                consults.setText(pDescBean.getConsults());
        }
        final GroupMemberBean bean = DBAccess.getdbHeler().getMemberDetails(gBean.getGroupId(), CallDispatcher.LoginUser);
        RoleEditRndFormBean erBean = DBAccess.getdbHeler().getRoleEditRoundingDetails(gBean.getGroupId(), bean.getRole());

        if (!gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) && erBean != null) {
            if (erBean.getStatus() != null) {
                if (erBean.getStatus().equalsIgnoreCase("1"))
                    edit_status.setVisibility(View.VISIBLE);
                else
                    edit_status.setVisibility(View.GONE);
            }
            if (erBean.getDiagnosis() != null) {
                if (erBean.getDiagnosis().equalsIgnoreCase("1"))
                    edit_diagnosis.setVisibility(View.VISIBLE);
                else
                    edit_diagnosis.setVisibility(View.GONE);
            }
            if (erBean.getTestandvitals() != null) {
                if (erBean.getTestandvitals().equalsIgnoreCase("1"))
                    edit_tests.setVisibility(View.VISIBLE);
                else
                    edit_tests.setVisibility(View.GONE);
            }
            if (erBean.getHospitalcourse() != null) {
                if (erBean.getHospitalcourse().equalsIgnoreCase("1"))
                    edit_hospital.setVisibility(View.VISIBLE);
                else
                    edit_hospital.setVisibility(View.GONE);
            }
            if (erBean.getConsults() != null) {
                if (erBean.getConsults().equalsIgnoreCase("1"))
                    edit_consults.setVisibility(View.VISIBLE);
                else
                    edit_consults.setVisibility(View.GONE);
            }
            if (erBean.getNotes() != null) {
                if (erBean.getNotes().equalsIgnoreCase("1"))
                    edit_medications.setVisibility(View.VISIBLE);
                else
                    edit_medications.setVisibility(View.GONE);
            }
        }
        final CharSequence[] items = {"Fullcode", "DNR", "DNI", "ComfortCare", "Critical", "Sick", "Stable", "Other"};

        status_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder3 = new AlertDialog.Builder(mainContext);
                if (gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) ||
                        editRndFormBean.getStatus() != null && editRndFormBean.getStatus().equalsIgnoreCase("1")) {
                    builder3.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            if (position == 0)
                                status[0] = "Fullcode";
                            else if (position == 1)
                                status[0] = "DNR";
                            else if (position == 2)
                                status[0] = "DNI";
                            else if (position == 3)
                                status[0] = "ComfortCare";
                            else if (position == 4)
                                status[0] = "Critical";
                            else if (position == 5)
                                status[0] = "Sick";
                            else if (position == 6)
                                status[0] = "Stable";
                            else if (position == 7)
                                status[0] = "Other";
                            String currentstatus = "";
                            pcBean.setPatientid(pBean.getPatientid());
                            pcBean.setGroupid(gBean.getGroupId());
//                            if (reportid != null)
//                                pcBean.setReportid(reportid);
                            pcBean.setReportcreator(CallDispatcher.LoginUser);
                            if (pDescBean.getCurrentstatus() != null)
                                currentstatus = pDescBean.getCurrentstatus();
                            if (status[0] != null) {
//                            if (currentstatus.contains(status[0])) {
//                                status[0] = "";
//                            } else {
                                pcBean.setCurrentstatus(status[0]);
                                pDescBean.setCurrentstatus(status[0]);

                                if (currentstatus != null) {
                                    String[] split = pcBean.getCurrentstatus().split(" ");
                                    currentstatus_lay.removeAllViews();
                                    currentstatus_lay1.removeAllViews();
                                    for (int i = 0; i < split.length; i++) {
                                        if (i <= 5) {
                                            TextView dynamicTextView = new TextView(mainContext);
                                            LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 60);
                                            dim.leftMargin = 15;
                                            dynamicTextView.setLayoutParams(dim);
                                            dynamicTextView.setGravity(Gravity.CENTER);
                                            dynamicTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.sender_border));
                                            if (split[i].equalsIgnoreCase("critical"))
                                                dynamicTextView.setTextColor(getResources().getColor(R.color.pink_tv));
                                            else if (split[i].equalsIgnoreCase("stable")) {
                                                dynamicTextView.setTextColor(getResources().getColor(R.color.green));
                                            } else if (split[i].equalsIgnoreCase("sick")) {
                                                dynamicTextView.setTextColor(getResources().getColor(R.color.yellow));
                                            }
                                            if (!split[i].equalsIgnoreCase("") && split.length > 0) {
                                                dynamicTextView.setText(split[i]);
                                                currentstatus_lay.addView(dynamicTextView);
                                            }
                                        } else if (i > 5) {
                                            TextView dynamicTextView = new TextView(mainContext);
                                            LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 60);
                                            dim.leftMargin = 15;
                                            dynamicTextView.setLayoutParams(dim);
                                            dynamicTextView.setGravity(Gravity.CENTER);
                                            dynamicTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.sender_border));
                                            if (split[i].equalsIgnoreCase("critical"))
                                                dynamicTextView.setTextColor(getResources().getColor(R.color.pink_tv));
                                            else if (split[i].equalsIgnoreCase("stable")) {
                                                dynamicTextView.setTextColor(getResources().getColor(R.color.green));
                                            } else if (split[i].equalsIgnoreCase("sick")) {
                                                dynamicTextView.setTextColor(getResources().getColor(R.color.yellow));
                                            }
                                            dynamicTextView.setText(split[i]);
                                            currentstatus_lay1.addView(dynamicTextView);
                                        }
                                    }
                                }
//                            }
                                Log.i("BBB", "call webservice from status_click");
                                pcBean.setReportid("");
                                pcBean.setDiagnosis("");
                                pcBean.setMedications("");
                                pcBean.setTestandvitals("");
                                pcBean.setHospitalcourse("");
                                pcBean.setConsults("");
                                WebServiceReferences.webServiceClient.SetPatientDescription(pcBean, patientRoundingFragment);
                            }
                        }
                    });
                    builder3.show();
                }
            }
        });
        seeAll_diagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiagnosisHistory(diagnosis.getText().toString(), "DIAGNOSIS HISTORY");

            }
        });
        seeAll_medications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiagnosisHistory(medications.getText().toString(), "MEDICATIONS HISTORY");

            }
        });

        seeAll_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiagnosisHistory(testandvitals.getText().toString(), "TEST AND VITALS HISTORY");

            }
        });
        seeAll_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiagnosisHistory(hospital.getText().toString(), "HOSPITAL COURSE HISTORY");

            }
        });
        seeAll_consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiagnosisHistory(consults.getText().toString(), "CONSULTS HISTORY");

            }
        });
        edit_diagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTag = 0;
                if (diagnosis.getText().toString() != null) {
                    editDiagnosisDescription(diagnosis.getText().toString(), "EDIT DIAGNOSIS", "Diagnosis");
                } else
                    editDiagnosisDescription("", "EDIT DIAGNOSIS", "Diagonosis");
            }
        });
        edit_medications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTag = 1;
                if (medications.getText().toString() != null)
                    editDiagnosisDescription(medications.getText().toString(), "EDIT MEDICATIONS", "Medications");
                else
                    editDiagnosisDescription("", "EDIT MEDICATIONS", "Medications");
            }
        });
        edit_tests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTag = 2;
                if (testandvitals.getText().toString() != null)
                    editDiagnosisDescription(testandvitals.getText().toString(), "EDIT TESTANDVITALS", "Test and Vitals");
                else
                    editDiagnosisDescription("", "EDIT TESTANDVITALS", "Test and Vitals");
            }
        });
        edit_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTag = 3;
                if (hospital.getText().toString() != null)
                    editDiagnosisDescription(hospital.getText().toString(), "EDIT HOSPITAL COURSE", "Hospital Course");
                else
                    editDiagnosisDescription("", "EDIT HOSPITAL COURSE", "Hospital Course");
            }
        });
        edit_consults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTag = 4;
                if (consults.getText().toString() != null)
                    editDiagnosisDescription(consults.getText().toString(), "EDIT CONSULTS", "Consults");
                else
                    editDiagnosisDescription("", "EDIT CONSULTS", "Consults");
            }
        });
        edit_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTag = 5;
                if (pDescBean.getCurrentstatus() != null)
                    editDiagnosisDescription(pDescBean.getCurrentstatus(), "EDIT CURRENT STATUS", "Current Status");
                else
                    editDiagnosisDescription("", "EDIT CURRENT STATUS", "Current Status");
            }
        });

        TextView patientname = (TextView) v1.findViewById(R.id.patient_name);
        TextView age = (TextView) v1.findViewById(R.id.age);
        TextView sex = (TextView) v1.findViewById(R.id.sex);
        TextView mrn = (TextView) v1.findViewById(R.id.mrn);
        TextView floor = (TextView) v1.findViewById(R.id.floor);
        TextView ward = (TextView) v1.findViewById(R.id.ward);
        TextView room = (TextView) v1.findViewById(R.id.room);
        TextView los = (TextView) v1.findViewById(R.id.los);
        TextView bed = (TextView) v1.findViewById(R.id.bed);
        TextView tv_hospital = (TextView) v1.findViewById(R.id.tv_hospital);
        if (pBean != null) {
            if (pBean.getFirstname() != null && pBean.getLastname() != null)
                patientname.setText(pBean.getFirstname() + " " + pBean.getLastname());
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
                int ages = Currentyear - (Integer.parseInt(BirthYear));
                Log.i("sss", "Current age" + ages);

                age.setText("Age : " + ages);
            }
            if (pBean.getSex() != null)
                sex.setText("Sex : " + pBean.getSex());
            if (pBean.getMrn() != null)
                mrn.setText("Mrn : " + pBean.getMrn());
            if (pBean.getFloor() != null)
                floor.setText("Floor : " + pBean.getFloor());
            if (pBean.getRoom() != null)
                room.setText("Room : " + pBean.getRoom());
            if (pBean.getWard() != null)
                ward.setText("Ward : " + pBean.getWard());
            if (pBean.getAdmissiondate() != null && pBean.getAdmissiondate().length() > 0) {
                String AdmitDate = pBean.getAdmissiondate();
                long Result = 0;
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                String inputString = dateFormat.format(date);
                String Today = inputString;
                SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
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
                los.setText("LOS : " + Result + " days");
            }
            if (pBean.getBed() != null)
                bed.setText("Bed : " + pBean.getBed());
            if (pBean.getHospital() != null)
                tv_hospital.setText(pBean.getHospital());
        }
    }

    public PatientDescriptionBean getLatestDescr_patient() {
        PatientDescriptionBean pDescrBean=DBAccess.getdbHeler().getPatientDescriptionDetails(pBean.getPatientid());
        strQuery = "select currentstatus from patientdescription where patientid='"+ pBean.getPatientid()  + "'and currentstatus!='" + "" + "'";
        ArrayList<String> status_List=DBAccess.getdbHeler().getDesc_patient_status(strQuery);
        Log.i("string","status of the patient ***************"+status_List.size());
        StringBuffer Status_total = new StringBuffer();
        if(status_List.size() > 0){
            Status_total.append(status_List.get(0));
            for(int i=1; i < status_List.size(); i++)
                Status_total.append(" ").append(status_List.get(i));
        }
        if(status_List!=null && status_List.size()>0)
        {
            pDescrBean.setCurrentstatus(Status_total.toString());
            DBAccess.getdbHeler().UpdatePatientStatus(Status_total.toString(),"patientid='" + pcBean.getPatientid() + "' and groupid='" + pcBean.getGroupid() + "'");
        }
        Log.i("string","current status*************"+ pDescrBean.getCurrentstatus());

        strQuery = "select medications from patientdescription where patientid='"+ pBean.getPatientid()  + "'and medications!='" + "" + "'";
        ArrayList<String> medi_List=DBAccess.getdbHeler().getDesc_patient_medication(strQuery);
        Log.i("string","Medi of the patient ***************"+medi_List);
        if(medi_List!=null && medi_List.size()>0)
        {
            if(medi_List.size()==1)
                pDescrBean.setMedications(medi_List.get(0));
            else
                pDescrBean.setMedications(medi_List.get(medi_List.size()-1));
        }
        Log.i("string","current Medi*************"+pDescrBean.getMedications());

        strQuery = "select diagnosis from patientdescription where patientid='"+ pBean.getPatientid()  + "'and diagnosis!='" + "" + "' ORDER BY modifieddate DESC";
        ArrayList<String> diag_List=DBAccess.getdbHeler().getDesc_patient_diagnosis(strQuery);
        Log.i("string","diagnosis of the patient ***************"+diag_List);
        if(diag_List!=null && diag_List.size()>0)
        {
            pDescrBean.setDiagnosis(diag_List.get(0));
        }
        Log.i("string","current diagnosis*************"+pDescrBean.getDiagnosis());

        strQuery = "select testandvitals from patientdescription where patientid='"+ pBean.getPatientid()  + "'and testandvitals!='" + "" + "'";
        ArrayList<String> testvital_List=DBAccess.getdbHeler().getDesc_patient_testvital(strQuery);
        Log.i("string","testandvitals of the patient ***************"+testvital_List);
        if(testvital_List!=null && testvital_List.size()>0)
        {
            if(testvital_List.size()==1)
                pDescrBean.setTestandvitals(testvital_List.get(0));
            else
                pDescrBean.setTestandvitals(testvital_List.get(testvital_List.size()-1));
        }
        Log.i("string","current testandvitals*************"+ pDescrBean.getTestandvitals());

        strQuery = "select hospitalcourse from patientdescription where patientid='"+ pBean.getPatientid()  + "'and hospitalcourse!='" + "" + "'";
        ArrayList<String> hospital_List=DBAccess.getdbHeler().getDesc_patient_hospital(strQuery);
        Log.i("string","hospitalcourse of the patient ***************"+hospital_List);
        if(hospital_List!=null && hospital_List.size()>0)
        {
            if(hospital_List.size()==1)
                pDescrBean.setHospitalcourse(hospital_List.get(0));
            else
                pDescrBean.setHospitalcourse(hospital_List.get(hospital_List.size()-1));
        }
        Log.i("string","current hospitalcourse*************"+pDescrBean.getHospitalcourse());

        strQuery = "select consults from patientdescription where patientid='"+ pBean.getPatientid()  + "'and consults!='" + "" + "'";
        ArrayList<String> consults_List=DBAccess.getdbHeler().getDesc_patient_consults(strQuery);
        Log.i("string","consults of the patient ***************"+consults_List);
        if(consults_List!=null && consults_List.size()>0)
        {
            if(consults_List.size()==1)
                pDescrBean.setConsults(consults_List.get(0));
            else
                pDescrBean.setConsults(consults_List.get(consults_List.size()-1));
        }
        Log.i("string","current consults*************"+ pDescrBean.getConsults());
        return pDescrBean;
    }

    public void notifySetPatientDescription(Object obj) {
        cancelDialog();
        if (obj instanceof String[]) {
            String[] result = (String[]) obj;
            pcBean.setPatientid(result[0]);
            pcBean.setReportid(result[1]);
            pcBean.setCreateddate(result[3]);
            pcBean.setModifieddate(result[4]);
            Log.i("BBB", "notify setpatientdesc create and modify date___--- " + result[3]+ "modify000000"+result[4]);
            if(isMakeActiveClicked) {
                refreshSeeAllAdapter();
                DBAccess.getdbHeler().updateActiveDescription(pcBean, "patientid='" + pcBean.getPatientid() + "' and reportid='" + pcBean.getReportid() + "'");
                Log.i("rose", "******notify setpatientdesc from make active)******************");
            }
            else
            DBAccess.getdbHeler().insertorUpdatePatientDescriptions(pcBean);
//            DBAccess.getdbHeler().updateseallcomments(pcBean.getDiagnosis(), pcBean.getDate(), pcBean.getPatientid());
        }
    }

    public void showprogress() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    progress = new ProgressDialog(mainContext);
                    if (progress != null) {
                        progress.setCancelable(false);
                        progress.setMessage("Progress ...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setProgress(0);
                        progress.setMax(100);
                        progress.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }

    public void cancelDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progress.dismiss();
                progress = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void showToast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(mainContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CommentsProcess() {
        final LinearLayout content = (LinearLayout) _rootView.findViewById(R.id.content);
        content.removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = layoutInflater.inflate(R.layout.patient_comments, content);
        Button send = (Button) v1.findViewById(R.id.donebtn);
        final EditText comment = (EditText) v1.findViewById(R.id.addcomment);
        final TextView mine = (TextView) v1.findViewById(R.id.mine);
        final TextView team = (TextView) v1.findViewById(R.id.team);
        final TextView all = (TextView) v1.findViewById(R.id.all);
        clistView = (ListView) v1.findViewById(R.id.listview_comments);
        final LinearLayout send_lay = (LinearLayout) v1.findViewById(R.id.send_lay);
        CommentsList = new Vector<PatientCommentsBean>();
        CommentsList.clear();
        commentType = "mine";
        CommentsList = DBAccess.getdbHeler().getPatientComments(gBean.getGroupId(), pBean.getPatientid(), CallDispatcher.LoginUser, commentType);
        cadapter = new CommentsAdapter(mainContext, R.layout.comments_row, CommentsList);
        clistView.setAdapter(cadapter);
        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentType = "mine";
                mine.setTextColor(getResources().getColor(R.color.white));
                team.setTextColor(getResources().getColor(R.color.snazlgray));
                all.setTextColor(getResources().getColor(R.color.snazlgray));
                CommentsSort();
                send_lay.setVisibility(View.VISIBLE);
            }
        });

        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_lay.setVisibility(View.GONE);
                if (gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser)
                        || (roleAccessBean.getCommentsview() != null &&
                        roleAccessBean.getCommentsview().equalsIgnoreCase("1"))) {
                    commentType = "team";
                    mine.setTextColor(getResources().getColor(R.color.snazlgray));
                    team.setTextColor(getResources().getColor(R.color.white));
                    all.setTextColor(getResources().getColor(R.color.snazlgray));
                    CommentsSort();
                } else {
                    showToast("You don't have access to view these comments");
                    send_lay.setVisibility(View.VISIBLE);
                }
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_lay.setVisibility(View.GONE);
                if (gBean.getOwnerName().equalsIgnoreCase(CallDispatcher.LoginUser) || (roleAccessBean.getCommentsview() != null &&
                        roleAccessBean.getCommentsview().equalsIgnoreCase("1"))) {
                    commentType = "all";
                    mine.setTextColor(getResources().getColor(R.color.snazlgray));
                    team.setTextColor(getResources().getColor(R.color.snazlgray));
                    all.setTextColor(getResources().getColor(R.color.white));
                    CommentsSort();
                } else {
                    showToast("You don't have access to view these comments");
                    send_lay.setVisibility(View.VISIBLE);
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatientCommentsBean commentsBean = new PatientCommentsBean();
                commentsBean.setGroupid(pBean.getGroupid());
                commentsBean.setGroupowner(gBean.getOwnerName());
                commentsBean.setGroupmember(CallDispatcher.LoginUser);
                commentsBean.setDateandtime(getCurrentDateandTime());
                commentsBean.setPatientid(pBean.getPatientid());
                commentsBean.setComments(comment.getText().toString());
                commentsBean.setCommentid("");
                WebServiceReferences.webServiceClient.SetPatientComments(commentsBean, patientRoundingFragment);
                comment.setText("");
                CommentsList.add(commentsBean);
                cadapter = new CommentsAdapter(mainContext, R.layout.comments_row, CommentsList);

                clistView.post(new Runnable() {
                    @Override
                    public void run() {
                        clistView.setSelection(cadapter.getCount() - 1);
                    }
                });
                clistView.setAdapter(cadapter);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        cadapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void notifySetPatientComments(Object obj) {
        Log.i("patientdetails", "notifySetPatientComments");
        if (obj instanceof PatientCommentsBean) {
            PatientCommentsBean cBean = (PatientCommentsBean) obj;
            Log.i("patientdetails", "notifySetPatientComments if");
            DBAccess.getdbHeler().insertorUpdatePatientComments(cBean);
        }
    }

    public String getCurrentDateandTime() {
        try {
            Date curDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            return sdf.format(curDate).toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private void CommentsSort() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CommentsList = DBAccess.getdbHeler().getPatientComments(gBean.getGroupId(), pBean.getPatientid(), CallDispatcher.LoginUser, commentType);

                //For type all and team sorting
                //start
                if (commentType.equalsIgnoreCase("all") || commentType.equalsIgnoreCase("team")) {
                    HashMap<String, PatientCommentsBean> list = new HashMap<String, PatientCommentsBean>();
                    Vector<PatientCommentsBean> patientCommentsBeen = new Vector<PatientCommentsBean>();
                    if (CommentsList != null) {
                        for (PatientCommentsBean commentsBean : CommentsList) {
                            if (list.containsKey(commentsBean.getGroupmember())) {
                                list.remove(commentsBean.getGroupmember());
                                list.put(commentsBean.getGroupmember(), commentsBean);
                            } else {
                                list.put(commentsBean.getGroupmember(), commentsBean);
                            }
                        }
                    }
                    Iterator iterator1 = list.entrySet()
                            .iterator();

                    while (iterator1.hasNext()) {

                        Map.Entry mapEntry = (Map.Entry) iterator1.next();

                        PatientCommentsBean bean = (PatientCommentsBean) mapEntry.getValue();
                        if (bean.getGroupmember().equalsIgnoreCase(CallDispatcher.LoginUser)) {
                            ProfileBean pbean = SingleInstance.myAccountBean;
                            bean.setMembername(pbean.getFirstname() + " " + pbean.getLastname());
                        } else
                            for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
                                if (bib.getName().equalsIgnoreCase(bean.getGroupmember()))
                                    bean.setMembername(bib.getFirstname() + " " + bib.getLastname());
                            }
                        patientCommentsBeen.add(bean);
                    }
                    cadapter = new CommentsAdapter(mainContext, R.layout.comments_row, patientCommentsBeen);
                } else {
                    cadapter = new CommentsAdapter(mainContext, R.layout.comments_row, CommentsList);
                }
                //End
                clistView.setAdapter(null);
                clistView.setAdapter(cadapter);
                cadapter.notifyDataSetChanged();
            }
        });
    }

    public void statusDialog() {
        final Dialog dialog1 = new Dialog(mainContext);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.task_status);
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog1.getWindow().setBackgroundDrawableResource(R.color.transparent);
        final RadioButton showall = (RadioButton) dialog1.findViewById(R.id.showall);
        final RadioButton active = (RadioButton) dialog1.findViewById(R.id.active);
        final RadioButton completed = (RadioButton) dialog1.findViewById(R.id.completed);
        Button apply = (Button) dialog1.findViewById(R.id.apply);
        RadioGroup gender = (RadioGroup) dialog1.findViewById(R.id.status_group);
        int selectedId = gender.getCheckedRadioButtonId();
        final RadioButton statusSelected = (RadioButton) dialog1.findViewById(selectedId);
        Button cancel = (Button) dialog1.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        if (statusMode.equalsIgnoreCase("showall")) {
            showall.setChecked(true);
        } else if (statusMode.equalsIgnoreCase("active"))
            active.setChecked(true);
        else
            completed.setChecked(true);
        showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusMode = "showall";
                showall.setTextColor(Color.parseColor("#458EDB"));
                active.setTextColor(Color.parseColor("#ffffff"));
                completed.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusMode = "active";
                active.setTextColor(Color.parseColor("#458EDB"));
                showall.setTextColor(Color.parseColor("#ffffff"));
                completed.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusMode = "completed";
                completed.setTextColor(Color.parseColor("#458EDB"));
                active.setTextColor(Color.parseColor("#ffffff"));
                showall.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        dialog1.show();
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskSorting(gBean.getGroupId());
                dialog1.dismiss();
                tv_status.setText(statusMode.toUpperCase());

            }
        });
    }

    public void assignedTaskDialog() {
        final Dialog dialog1 = new Dialog(mainContext);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.task_assigned);
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog1.getWindow().setBackgroundDrawableResource(R.color.transparent);
        final RadioButton showall = (RadioButton) dialog1.findViewById(R.id.showall);
        final RadioButton assignedtome = (RadioButton) dialog1.findViewById(R.id.assignedtome);
        final RadioButton assignedbyme = (RadioButton) dialog1.findViewById(R.id.assignedbyme);
        final RadioButton unassigned = (RadioButton) dialog1.findViewById(R.id.unassigned);
        final RadioButton assigntoteam = (RadioButton) dialog1.findViewById(R.id.assigntoteam);
        Button apply = (Button) dialog1.findViewById(R.id.apply);
        final LinearLayout members_list = (LinearLayout) dialog1.findViewById(R.id.members_list);
        RadioGroup gender = (RadioGroup) dialog1.findViewById(R.id.assigned_group);
        Button cancel = (Button) dialog1.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        if (assignedMode.equalsIgnoreCase("showall")) {
            showall.setChecked(true);
        } else if (assignedMode.equalsIgnoreCase("assignedtome")) {
            assignedtome.setChecked(true);
        } else if (assignedMode.equalsIgnoreCase("assignedbyme")) {
            assignedbyme.setChecked(true);
        } else if (assignedMode.equalsIgnoreCase("unassigned")) {
            unassigned.setChecked(true);
        } else if (assignedMode.equalsIgnoreCase("assigntoteam")) {
            assigntoteam.setChecked(true);
        }
        showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignedMode = "showall";
                showall.setTextColor(Color.parseColor("#458EDB"));
                assignedtome.setTextColor(Color.parseColor("#ffffff"));
                assignedbyme.setTextColor(Color.parseColor("#ffffff"));
                unassigned.setTextColor(Color.parseColor("#ffffff"));
                assigntoteam.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        assignedtome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignedMode = "assignedtome";
                assignedtome.setTextColor(Color.parseColor("#458EDB"));
                showall.setTextColor(Color.parseColor("#ffffff"));
                assignedbyme.setTextColor(Color.parseColor("#ffffff"));
                unassigned.setTextColor(Color.parseColor("#ffffff"));
                assigntoteam.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        assignedbyme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignedMode = "assignedbyme";
                assignedbyme.setTextColor(Color.parseColor("#458EDB"));
                assignedtome.setTextColor(Color.parseColor("#ffffff"));
                showall.setTextColor(Color.parseColor("#ffffff"));
                unassigned.setTextColor(Color.parseColor("#ffffff"));
                assigntoteam.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        unassigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignedMode = "unassigned";
                unassigned.setTextColor(Color.parseColor("#458EDB"));
                assignedtome.setTextColor(Color.parseColor("#ffffff"));
                showall.setTextColor(Color.parseColor("#ffffff"));
                assignedbyme.setTextColor(Color.parseColor("#ffffff"));
                assigntoteam.setTextColor(Color.parseColor("#ffffff"));
            }
        });
        assigntoteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignedMode = "assigntoteam";
                unassigned.setTextColor(Color.parseColor("#ffffff"));
                assignedtome.setTextColor(Color.parseColor("#ffffff"));
                showall.setTextColor(Color.parseColor("#ffffff"));
                assignedbyme.setTextColor(Color.parseColor("#ffffff"));
                assigntoteam.setTextColor(Color.parseColor("#458EDB"));
            }
        });
        final Vector<UserBean> memberslist = new Vector<UserBean>();
        if (pBean != null) {
            if (pBean.getAssignedmembers() != null
                    && pBean.getAssignedmembers().length() > 0) {
                String[] mlist = (pBean.getAssignedmembers())
                        .split(",");


                for (String tmp : mlist) {
                    UserBean uBean = new UserBean();
                    Log.d("listvalue", "listsize" + tmp);
                    if (tmp.length() > 0) {
                        Log.d("listvalue", "listsize1" + tmp);
                        for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
                            if (bib.getName().equalsIgnoreCase(tmp)) {
                                uBean.setFirstname(bib.getFirstname() + " " + bib.getLastname());
                                break;
                            } else
                                uBean.setFirstname(tmp);
                        }

                        uBean.setBuddyName(tmp);
                        uBean.setFlag("0");
                        memberslist.add(uBean);
                    }

                }
            }
        }
        TeamMembersAdapter adapter = new TeamMembersAdapter(mainContext, R.layout.rounding_member_row, memberslist);
        final int adapterCount = adapter.getCount();
        for (int i = 0; i < adapterCount; i++) {
            View item = adapter.getView(i, null, null);
            members_list.addView(item);

        }
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskSorting(gBean.getGroupId());
                dialog1.dismiss();
                if (assignedMode.equalsIgnoreCase("showall")) {
                    tv_assigned.setText(" ALL");
                } else if (assignedMode.equalsIgnoreCase("assignedtome")) {
                    tv_assigned.setText(" TO ME");
                } else if (assignedMode.equalsIgnoreCase("assignedbyme")) {
                    tv_assigned.setText(" BY ME");
                } else if (assignedMode.equalsIgnoreCase("unassigned")) {
                    tv_assigned.setText(" UNASSIGNED");
                } else if (assignedMode.equalsIgnoreCase("assigntoteam")) {
                    tv_assigned.setText(" TO " + adapterCount + " MEMBERS");
                }
//                tv_assigned.setText(assignedMode);

                String addMembers = "";
                for (UserBean bean : memberslist) {
                    if (bean.isSelected())
                        addMembers = addMembers + "" + bean.getBuddyName() + ",";
                }
            }
        });
        dialog1.show();
    }

    private void taskSorting(String groupid) {
        java.sql.Date dt1 = new java.sql.Date(System.currentTimeMillis());
        String Today = dt1.toString();

        if (assignedMode.equalsIgnoreCase("showall") && statusMode.equalsIgnoreCase("showall"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and patientid='" + patientid + "'";
        else if (assignedMode.equalsIgnoreCase("showall") && statusMode.equalsIgnoreCase("active"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and patientid='" + patientid
                    + "'and taskstatus ='" + "0" + "'";
        else if (assignedMode.equalsIgnoreCase("showall") && statusMode.equalsIgnoreCase("completed"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and patientid='" + patientid
                    + "'and taskstatus ='" + "1" + "'";
        else if (assignedMode.equalsIgnoreCase("assignedtome") && statusMode.equalsIgnoreCase("showall"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and patientid='" + patientid
                    + "'and assignmembers LIKE '%" + CallDispatcher.LoginUser + "%'";
        else if (assignedMode.equalsIgnoreCase("assignedtome") && statusMode.equalsIgnoreCase("active"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "0" + "'and patientid='"
                    + patientid + "'and assignmembers LIKE '%" + CallDispatcher.LoginUser + "%'";
        else if (assignedMode.equalsIgnoreCase("assignedtome") && statusMode.equalsIgnoreCase("completed"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "1" +
                    "'and patientid='" + patientid + "'and assignmembers LIKE '%" + CallDispatcher.LoginUser + "%'";
        else if (assignedMode.equalsIgnoreCase("assignedbyme") && statusMode.equalsIgnoreCase("showall"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and patientid='" + patientid
                    + "'and creatorname='" + CallDispatcher.LoginUser + "'";
        else if (assignedMode.equalsIgnoreCase("assignedbyme") && statusMode.equalsIgnoreCase("active"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "0" +
                    "'and patientid='" + patientid + "'and creatorname='" + CallDispatcher.LoginUser + "'";
        else if (assignedMode.equalsIgnoreCase("assignedbyme") && statusMode.equalsIgnoreCase("completed"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "1" +
                    "'and patientid='" + patientid + "'and creatorname='" + CallDispatcher.LoginUser + "'";
        else if (assignedMode.equalsIgnoreCase("unassigned") && statusMode.equalsIgnoreCase("showall"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and patientid='" + patientid
                    + "'and assignmembers NOT LIKE '%" + CallDispatcher.LoginUser + "%'";
        else if (assignedMode.equalsIgnoreCase("unassigned") && statusMode.equalsIgnoreCase("active"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "0" +
                    "'and patientid='" + patientid + "'and assignmembers NOT LIKE '%" + CallDispatcher.LoginUser + "%'";
        else if (assignedMode.equalsIgnoreCase("unassigned") && statusMode.equalsIgnoreCase("completed"))
            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "1" +
                    "'and patientid='" + patientid + "'and assignmembers NOT LIKE '%" + CallDispatcher.LoginUser + "%'";


//        else if (assignedMode.equalsIgnoreCase("assigntoteam") && statusMode.equalsIgnoreCase("showall"))
//            strQuery = "select * from taskdetails where groupid='" + groupid + "'and patientid='" + patientid
//                    + "'and assignmembers NOT LIKE '%" + " " + "%'";
//        else if (assignedMode.equalsIgnoreCase("assigntoteam") && statusMode.equalsIgnoreCase("active"))
//            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "0" +
//                    "'and patientid='" + patientid + "'and assignmembers NOT LIKE '%" + " " + "%'";
//        else if (assignedMode.equalsIgnoreCase("assigntoteam") && statusMode.equalsIgnoreCase("completed"))
//            strQuery = "select * from taskdetails where groupid='" + groupid + "'and taskstatus ='" + "1" +
//                    "'and patientid='" + patientid + "'and assignmembers NOT LIKE '%" + " " + "%'";


        Log.i("patientdetails", "statusDialog " + statusMode + " query " + strQuery);
        Vector<TaskDetailsBean> tasklist = DBAccess.getdbHeler().getAllTaskDetails(strQuery);
        Collections.sort(tasklist, new TaskDateComparator());
        Vector<TaskDetailsBean> taskList = GroupChatActivity.getdatelist(tasklist);
        final RoundingTaskAdapter taskAdapter = new RoundingTaskAdapter(mainContext, R.layout.round_task_row, taskList);
        handler.post(new Runnable() {
            @Override
            public void run() {
                tasklistView.setAdapter(taskAdapter);
                taskAdapter.notifyDataSetChanged();
            }
        });
    }

    public class TeamMembersAdapter extends ArrayAdapter<UserBean> {

        private LayoutInflater inflater = null;
        private ViewHolder holder;
        private ImageLoader imageLoader;
        private Vector<UserBean> result;
        Boolean isClicked = false;

        public TeamMembersAdapter(Context context, int resource, Vector<UserBean> objects) {
            super(context, resource, objects);
            imageLoader = new ImageLoader(context);
            result = new Vector<UserBean>();
            result.addAll(objects);
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            try {
                final ViewHolder1 holder;
                if (convertView == null) {
                    holder = new ViewHolder1();
                    inflater = (LayoutInflater) mainContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.rounding_member_row, null);
                    holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                    holder.statusIcon = (ImageView) convertView.findViewById(R.id.statusIcon);
                    holder.edit = (ImageView) convertView.findViewById(R.id.edit);
                    holder.overlay = (ImageView) convertView.findViewById(R.id.overlay);
                    holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                    holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                    holder.role = (TextView) convertView.findViewById(R.id.position);
                    holder.header_title = (TextView) convertView.findViewById(R.id.header_title);
                    holder.rights = (TextView) convertView.findViewById(R.id.rights);
                    holder.member_lay = (LinearLayout) convertView.findViewById(R.id.member_lay);
                    convertView.setTag(holder);
                } else
                    holder = (ViewHolder1) convertView.getTag();
                final UserBean bib = result.get(i);
                holder.edit.setVisibility(View.GONE);
                holder.rights.setVisibility(View.GONE);
                holder.statusIcon.setVisibility(View.GONE);
                if (bib != null) {
                    if (bib.getProfilePic() != null) {
                        String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/COMMedia/" + bib.getProfilePic();
                        File pic = new File(pic_Path);
                        if (pic.exists()) {
                            imageLoader.DisplayImage(pic_Path, holder.buddyicon, R.drawable.img_user);
                        }
                    }
                    holder.header_title.setVisibility(View.GONE);
                    holder.buddyName.setText(bib.getFirstname());
                    if (bib.getOccupation() != null)
                        holder.occupation.setText(bib.getOccupation());
                    if (bib.getRole() != null) {
                        holder.role.setText(bib.getRole());
                    }
                    holder.member_lay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (bib.getFlag().equalsIgnoreCase("0")) {
                                bib.setSelected(true);
                                holder.overlay.setVisibility(View.VISIBLE);
                                bib.setFlag("1");
                            } else {
                                bib.setSelected(false);
                                holder.overlay.setVisibility(View.GONE);
                                bib.setFlag("0");
                            }
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("RRRR", "Error FindpeopleAdapter.java => " + e.toString());
            }
            return convertView;
        }
    }

    public class ViewHolder1 {
        CheckBox selectUser;
        ImageView buddyicon, overlay;
        ImageView statusIcon, edit;
        TextView buddyName;
        TextView occupation;
        TextView header_title;
        LinearLayout member_lay;
        TextView rights, role;
    }

    public void onResume() {
        super.onResume();
        Log.d("ppp", "The onResume() event");
        Log.i("BBB", "call webservice getpatientdesc from rounding_patient refresh");

        WebServiceReferences.webServiceClient.GetPatientDescription(pBean.getPatientid(), "", SingleInstance.mainContext);

    }

    public void notifyPatientDischarge(Object obj) {
        cancelDialog();
        Log.i("patientdetails", "notifyPatientDischarge");
        if (obj instanceof String[]) {
            String[] result = (String[]) obj;
            if (result[0].equalsIgnoreCase("Patient removed Successfully")) {
                DBAccess.getdbHeler().deletePatientRelatedDetails(result[1], result[2]);
                Intent intent = new Intent(SingleInstance.mainContext, GroupChatActivity.class);
                intent.putExtra("groupid", pBean.getGroupid());
                intent.putExtra("isRounding", true);
                intent.putExtra("isReq", "p");
                SingleInstance.mainContext.startActivity(intent);
            }
        } else if (obj instanceof WebServiceBean)
            showToast(((WebServiceBean) obj).getText());

    }

    public void notifyDeletetask(Object obj) {
        cancelDialog();
        Log.i("patientdetails", "notifyDeletetask");
        if (obj instanceof String[]) {
            String[] result = (String[]) obj;
            if (result[0].equalsIgnoreCase("Task removed Successfully")) {
                String strQuery = null;
                if (!result[2].equalsIgnoreCase(""))
                    strQuery = "DELETE from taskdetails WHERE groupid='" + result[1] + "'and taskid='" + result[2] + "'";
                else
                    strQuery = "DELETE from taskdetails WHERE groupid='" + result[1] + "'";
                DBAccess.getdbHeler().deleteTaskDetails(strQuery);
                taskSorting(result[1]);
            }
        } else if (obj instanceof WebServiceBean)
            showToast(((WebServiceBean) obj).getText());

    }

    public void deleteTask(final String taskid, final String groupid, String taskname) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
        builder.setTitle("Warning !");
        builder.setMessage("Are you sure you want to delete "
                + taskname + " ?").setCancelable(false).setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!WebServiceReferences.running) {
                            calldisp.startWebService(getResources()
                                    .getString(R.string.service_url), "80");
                        }
                        showprogress();
                        WebServiceReferences.webServiceClient.DeleteTask(CallDispatcher.LoginUser, taskid,
                                groupid, patientRoundingFragment);

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

    public static boolean useList(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            // check if the request code is same as what is passed here it is 2
            if (requestCode == 3) {
                Log.i("AAAA", "request code 1343454356");
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    ArrayList<UserBean> list = (ArrayList<UserBean>) bundle
                            .get("list");
                    String addedMembers = "";
                    for (UserBean temp : list) {
                        if (temp.getBuddyName() != null)
                            if (addedMembers != null && addedMembers.length() > 0)
                                addedMembers = addedMembers + "," + temp.getBuddyName();
                            else
                                addedMembers = temp.getBuddyName();
                    }
                    Log.i("ppp", "***assign Members patient--------->" + pBean.getAssignedmembers());
                    String[] member_split = addedMembers.split(",");
                    String[] addedMember_split = pBean.getAssignedmembers().split(",");
                    for (int i = 0; i < member_split.length; i++) {
                        if (!useList(addedMember_split, member_split[i])) {
                            if (pBean.getAssignedmembers() != null && pBean.getAssignedmembers().length() > 0)

                                pBean.setAssignedmembers(pBean.getAssignedmembers() + "," + member_split[i]);
                            else
                                pBean.setAssignedmembers(member_split[i]);
                        } else
                            Log.i("AAAA", "Assigned Member Duplication true=======>");
                    }
                    WebServiceReferences.webServiceClient.SetPatientRecord(pBean, mainContext);
                    DBAccess.getdbHeler().insertorUpdatePatientDetails(pBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class Seealladapter extends ArrayAdapter<PatientDescriptionBean> {

        private Context context;
        private Typeface tf_regular = null;

        private Typeface tf_bold = null;
        ImageLoader imageLoader;
        private int checkBoxCounter = 0;
        Vector<PatientDescriptionBean> commentList = new Vector<PatientDescriptionBean>();


        public Seealladapter(Context context, int textViewResourceId,
                             Vector<PatientDescriptionBean> co) {

            super(context, R.layout.seeall_row, co);
            this.context = context;
            imageLoader = new ImageLoader(SingleInstance.mainContext);
            commentList.addAll(co);
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
                    row = inflater.inflate(R.layout.seeall_row, null, false);
                    holder.old_message = (TextView) row.findViewById(R.id.old_message);
                    holder.active = (TextView) row.findViewById(R.id.active);
                    holder.message_date = (TextView) row.findViewById(R.id.message_date);
                    holder.header_title = (TextView) row.findViewById(R.id.header_title);
                    holder.relay_header = (RelativeLayout) row.findViewById(R.id.relay_header);
                    row.setTag(holder);
                } else {
                    holder = (ViewHolder) row.getTag();
                }
                final PatientDescriptionBean pBean = (PatientDescriptionBean) commentList.get(position);
                if (position == 0) {
                    holder.header_title.setText("ACTIVE");
                    holder.active.setVisibility(View.GONE);
                } else if (position == 1) {
                    holder.header_title.setText("PREVIOUS");
                } else {
                    holder.header_title.setVisibility(View.GONE);
                    holder.relay_header.setVisibility(View.GONE);
                }
//                if(isfromedit_diagnosis = true) {
//                    holder.header_title.setVisibility(View.VISIBLE);
//                    holder.active.setVisibility(View.VISIBLE);
//                    holder.relay_header.setVisibility(View.VISIBLE);
//                }else if(isfromedit_diagnosis = false){
//                    holder.header_title.setVisibility(View.GONE);
//                    holder.active.setVisibility(View.GONE);
//                    holder.relay_header.setVisibility(View.GONE);
//                }
                holder.old_message.setText(pBean.getDiagnosis());
                holder.message_date.setText(pBean.getDate());

                holder.active.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("updateseeall", "clickevent1");
                        isMakeActiveClicked=true;
//                        DBAccess.getdbHeler().updateseallcomments(pBean.getDiagnosis(), pBean.getDate(), pBean.getPatientid());
                        Log.d("updateseeall", "clickevent" + pBean.getDiagnosis() + pBean.getDate() + pBean.getPatientid());
                        showprogress();
                        Log.i("BBB", "-----------------------------------------------------");
                        Log.i("BBB", "active_click---------" + "--->" + pBean.getReportid());
                        Log.i("BBB", "-----------------------------------------------------");

//                        pBean.setReportid(pcBean.getReportid());
                        WebServiceReferences.webServiceClient.SetPatientDescription(pBean, patientRoundingFragment);
                        diagnosis.setText(pBean.getDiagnosis());
                        pcBean.setDate(pBean.getDate());
                        pcBean.setDiagnosis(pBean.getDiagnosis());
                        dialog1.dismiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return row;
        }

        private class ViewHolder {
            TextView old_message, active, message_date, header_title;
            RelativeLayout relay_header;
        }
    }
}





