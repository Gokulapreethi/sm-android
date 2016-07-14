package com.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.account.FindPeople;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;


public class RequestFragment extends Fragment {

    private static RequestFragment requestFragment;
    private static CallDispatcher calldisp = null;
    public Context mainContext;
    View _rootView;
    String buddy;
    String isAccept;
    private boolean isSerach=false;
    private ProgressDialog pDialog;


    public static RequestFragment newInstance(Context context) {
        try {
            if (requestFragment == null) {
                requestFragment = new RequestFragment();
                requestFragment.setContext(context);
                calldisp = CallDispatcher.getCallDispatcher(context);

            }
            return requestFragment;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return requestFragment;
        }
    }

    public void setContext(Context cxt) {
        this.mainContext = cxt;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppReference.bacgroundFragment=requestFragment;
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
        ProfileBean pb= DBAccess.getdbHeler().getProfileDetails(buddy);
        title.setText(pb.getFirstname()+" "+pb.getLastname());

        Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSerach){
                    FindPeople findPeople = FindPeople.newInstance(mainContext);
                    FragmentManager fragmentManager = SingleInstance.mainContext
                            .getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(
                            R.id.activity_main_content_fragment, findPeople)
                            .commitAllowingStateLoss();
                }else {
                    ContactsFragment contactsFragment = ContactsFragment.getInstance(mainContext);
                    FragmentManager fragmentManager = SingleInstance.mainContext
                            .getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(
                            R.id.activity_main_content_fragment, contactsFragment)
                            .commitAllowingStateLoss();
                }
            }
        });
        _rootView = null;
        if (_rootView == null) {

            _rootView = inflater.inflate(R.layout.profile, null);
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            try {
                LinearLayout accept = (LinearLayout) _rootView.findViewById(R.id.accept);
                final LinearLayout reject = (LinearLayout) _rootView.findViewById(R.id.reject);
                TextView status = (TextView) _rootView.findViewById(R.id.status);
                if(isAccept.equalsIgnoreCase("accept")) {
                    status.setText("invite received");
                    accept.setVisibility(View.VISIBLE);
                    reject.setVisibility(View.VISIBLE);
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (SingleInstance.mainContext
                                    .isNetworkConnectionAvailable()) {
                                String name = buddy;
                                Toast.makeText(
                                        mainContext,
                                        "Processing Request "
                                                + name,
                                        Toast.LENGTH_SHORT).show();

                                if (!WebServiceReferences.running) {
                                    calldisp.startWebService(
                                            mainContext.getResources()
                                                    .getString(
                                                            R.string.service_url),
                                            "80");
                                }

                                WebServiceReferences.webServiceClient
                                        .acceptRejectPeople(
                                                CallDispatcher.LoginUser,
                                                name,
                                                "1", "",
                                                ContactsFragment
                                                        .getInstance(mainContext));
                            } else {
                                ContactsFragment
                                        .getInstance(mainContext)
                                        .showAlert1("Info",
                                                "Check Internet Connection,Unable to Connect Server");
                            }
                            ContactsFragment contactsFragment = ContactsFragment.getInstance(mainContext);
                            FragmentManager fragmentManager = SingleInstance.mainContext
                                    .getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_fragment, contactsFragment)
                                    .commitAllowingStateLoss();
                        }
                    });
                    reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            reject(buddy);

                        }
                    });
                }else if(isAccept.equalsIgnoreCase("cancel")){
                    status.setText("invite sent");
                    TextView tv_reject=(TextView)_rootView.findViewById(R.id.tv_reject);
                    TextView tv_footer=(TextView)_rootView.findViewById(R.id.tv_footer);
                    reject.setVisibility(View.VISIBLE);
                    reject.setBackgroundColor(mainContext.getResources().getColor(R.color.grey3));
                    tv_reject.setText("CANCEL INVITE");
                    tv_footer.setText("You sent an invite to this user");
                    reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (!WebServiceReferences.running) {
                                    calldisp.startWebService(
                                            mainContext.getResources()
                                                    .getString(
                                                            R.string.service_url),
                                            "80");
                                }
                                WebServiceReferences.webServiceClient
                                        .deletePeople(
                                                CallDispatcher.LoginUser,
                                                buddy,
                                                ContactsFragment.getInstance(mainContext));
                                ContactsFragment.getContactAdapter().deleteUser(buddy);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            ContactsFragment contactsFragment = ContactsFragment.getInstance(mainContext);
                            FragmentManager fragmentManager = SingleInstance.mainContext
                                    .getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_fragment, contactsFragment)
                                    .commitAllowingStateLoss();
                        }
                    });
                }else if(isAccept.equalsIgnoreCase("invite")){
                    status.setText("invite sent");
                    TextView tv_accept=(TextView)_rootView.findViewById(R.id.tv_accept);
                    TextView tv_footer=(TextView)_rootView.findViewById(R.id.tv_footer);
                    accept.setVisibility(View.VISIBLE);
                    accept.setBackgroundColor(mainContext.getResources().getColor(R.color.blue2));
                    tv_accept.setText("SEND INVITE");
                    tv_footer.setText("This user is not in your contact list");
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            WebServiceReferences.webServiceClient
                                    .addPeople(CallDispatcher.LoginUser,
                                            buddy, FindPeople.newInstance(mainContext));
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
                }
                    if(pb.getUsername().equals(buddy)){
                        if(pb.getFirstname()!=null)
                        title.setText(pb.getFirstname());
                        ImageView profilePic = (ImageView) _rootView.findViewById(R.id.riv1);
                        TextView username = (TextView) _rootView.findViewById(R.id.username);
                        TextView profession = (TextView) _rootView.findViewById(R.id.profession);
                        TextView tileName = (TextView) _rootView.findViewById(R.id.tileName);
                        TextView fistName = (TextView) _rootView.findViewById(R.id.fistName);
                        TextView lastName = (TextView) _rootView.findViewById(R.id.lastName);
                        TextView nickName = (TextView) _rootView.findViewById(R.id.nickName);
                        TextView mf = (TextView) _rootView.findViewById(R.id.mf);
                        TextView atten_phys = (TextView) _rootView.findViewById(R.id.atten_phys);
                        TextView statof = (TextView) _rootView.findViewById(R.id.statof);
                        TextView profess = (TextView) _rootView.findViewById(R.id.profess);
                        TextView spec = (TextView) _rootView.findViewById(R.id.spec);
                        TextView medical = (TextView) _rootView.findViewById(R.id.medical);
                        TextView residency = (TextView) _rootView.findViewById(R.id.residency);
                        TextView felloship = (TextView) _rootView.findViewById(R.id.felloship);
                        TextView officeaddre = (TextView) _rootView.findViewById(R.id.officeaddre);
                        TextView hospitalspec = (TextView) _rootView.findViewById(R.id.hospitalspec);
                        TextView association = (TextView) _rootView.findViewById(R.id.association);
                        TextView citation = (TextView) _rootView.findViewById(R.id.citation);
                        ImageLoader imageLoader = new ImageLoader(mainContext);
                        if(!pb.getPhoto().equals(null)){
                            String directory_path = Environment
                                    .getExternalStorageDirectory()
                                    .getAbsolutePath()
                                    + "/COMMedia/"+pb.getPhoto();
                            imageLoader.DisplayImage(directory_path, profilePic,
                                    R.drawable.icon_buddy_aoffline);
                        }
                        if(!(pb.getUsername().equals(null) || pb.getUsername().equals("")))
                            username.setText(pb.getFirstname()+" "+pb.getLastname());
                        if(!(pb.getProfession().equals(null) || pb.getProfession().equals("")))
                            profession.setText(pb.getProfession());
                        if(!(pb.getTitle().equals(null) || pb.getTitle().equals("")))
                            tileName.setText(pb.getTitle());
                        if(!(pb.getFirstname().equals(null) || pb.getFirstname().equals("")))
                            fistName.setText(pb.getFirstname());
                        if(!(pb.getLastname().equals(null) || pb.getLastname().equals("")))
                            lastName.setText(pb.getLastname());
                        if(!(pb.getNickname().equals(null) || pb.getNickname().equals("")))
                            nickName.setText(pb.getNickname());
                        if(!(pb.getSex().equals(null) || pb.getSex().equals("")))
                            mf.setText(pb.getSex());
                        if(!(pb.getState().equals(null) || pb.getState().equals("")))
                            atten_phys.setText(pb.getState());
                        if(!(pb.getState().equals(null) || pb.getState().equals("")))
                            statof.setText(pb.getState());
                        if(!(pb.getProfession().equals(null) || pb.getProfession().equals("")))
                            profess.setText(pb.getProfession());
                        if(!(pb.getSpeciality().equals(null) || pb.getSpeciality().equals("")))
                            spec.setText(pb.getSpeciality());
                        if(!(pb.getMedicalschool().equals(null) || pb.getMedicalschool().equals("")))
                            medical.setText(pb.getMedicalschool());
                        if(!(pb.getResidencyprogram().equals(null) || pb.getResidencyprogram().equals("")))
                            residency.setText(pb.getResidencyprogram());
                        if(!(pb.getFellowshipprogram().equals(null) || pb.getFellowshipprogram().equals("")))
                            felloship.setText(pb.getFellowshipprogram());
                        if(!(pb.getOfficeaddress().equals(null) || pb.getOfficeaddress().equals("")))
                            officeaddre.setText(pb.getOfficeaddress());
                        if(!(pb.getHospitalaffiliation().equals(null) || pb.getHospitalaffiliation().equals("")))
                            hospitalspec.setText(pb.getHospitalaffiliation());
                        if(!(pb.getOrganizationmembership().equals(null) || pb.getOrganizationmembership().equals("")))
                            association.setText(pb.getOrganizationmembership());
                        if(!(pb.getCitationpublications().equals(null) || pb.getCitationpublications().equals("")))
                            citation.setText(pb.getCitationpublications());
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
            ((ViewGroup) _rootView.getParent()).removeView(_rootView);
        return _rootView;
    }
    public View getParentView() {
        return _rootView;
    }
    public void setBuddyName(String buddyname) {
        buddy = buddyname;
    }
    public void setFrom(boolean isFrom){
        isSerach=isFrom;
    }
    public void setRequest(String requestFor) {
        isAccept = requestFor;
    }
    public void reject(final String buddy){
        final Dialog dialog = new Dialog(mainContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reject_hov);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.color.lblack);
//        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setAttributes(lp);
        dialog.show();
        TextView reject = (TextView) dialog.findViewById(R.id.reject);
        TextView report = (TextView) dialog.findViewById(R.id.report);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        TextView bName = (TextView) dialog.findViewById(R.id.name);
        bName.setText(buddy + "'s");
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    dialog.dismiss();
                    if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {
                        String name = buddy;
                        Toast.makeText(mainContext, "Processing Request " + name,
                                Toast.LENGTH_SHORT).show();

                        if (!WebServiceReferences.running) {calldisp.startWebService(
                                mainContext.getResources().getString(
                                        R.string.service_url), "80");
                        }

                        WebServiceReferences.webServiceClient.acceptRejectPeople(
                                CallDispatcher.LoginUser, name, "0", "",
                                ContactsFragment.getInstance(mainContext));
                    } else {
                        ContactsFragment.getInstance(mainContext)
                                .showAlert1("Info", "Check Internet Connection,Unable to Connect Server");
                    }
                    ContactsFragment contactsFragment = ContactsFragment.getInstance(mainContext);
                    FragmentManager fragmentManager = SingleInstance.mainContext
                            .getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(
                            R.id.activity_main_content_fragment, contactsFragment)
                            .commitAllowingStateLoss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        report.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    dialog.dismiss();
                    final Dialog dialog = new Dialog(mainContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.reject_hov2);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    Window window = dialog.getWindow();
                    window.setBackgroundDrawableResource(R.color.lblack);
//                    window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    window.setAttributes(lp);
                    dialog.show();
                    final TextView tv_msg = (TextView) dialog.findViewById(R.id.msg);
                    final EditText message = (EditText) dialog.findViewById(R.id.mesage);
                    final TextView tv_report = (TextView) dialog.findViewById(R.id.tv_report);
                    final TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                    TextView bName = (TextView) dialog.findViewById(R.id.name);
                    bName.setText(buddy + "'s");
                    message.requestFocus();
                    tv_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                        }
                    });
                    message.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.length() > 0)
                                tv_msg.setVisibility(View.VISIBLE);
                            else
                                tv_msg.setVisibility(View.GONE);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                    tv_report.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {dialog.dismiss();
                            if (SingleInstance.mainContext.isNetworkConnectionAvailable()) {
                                String name = buddy;
                                Toast.makeText(mainContext, "Processing Request " + name,
                                        Toast.LENGTH_SHORT).show();

                                if (!WebServiceReferences.running) {calldisp.startWebService(
                                        mainContext.getResources().getString(
                                                R.string.service_url), "80");
                                }

                                WebServiceReferences.webServiceClient.acceptRejectPeople(
                                        CallDispatcher.LoginUser, name, "0", message.getText().toString(),
                                        ContactsFragment.getInstance(mainContext));
                            } else {
                                ContactsFragment.getInstance(mainContext)
                                        .showAlert1("Info", "Check Internet Connection,Unable to Connect Server");
                            }
                            ContactsFragment contactsFragment = ContactsFragment.getInstance(mainContext);
                            FragmentManager fragmentManager = SingleInstance.mainContext
                                    .getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_fragment, contactsFragment)
                                    .commitAllowingStateLoss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
