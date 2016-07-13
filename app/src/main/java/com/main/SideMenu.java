package com.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.util.SingleInstance;

/**
 * @author Narayanan
 */
public class SideMenu extends Fragment{

    private LinearLayout ll_dash;
    private LinearLayout ll_cont;
    private LinearLayout ll_files;
    private LinearLayout ll_calc;
    private LinearLayout ll_myacc;
    private LinearLayout ll_settings,ll_rounding;
    private LinearLayout ll_logout;
    private LinearLayout ll_status, ll_invite_users;
    private TextView tv_dash;
    private TextView tv_cont;
    private TextView tv_files;
    private TextView tv_calc;
    private TextView tv_myacc,tv_rounding;
    private TextView tv_settings;
    private TextView online;
    private TextView busy;
    private TextView offline;
    private TextView invisible;
    private static TextView status;
    private static TextView userName;
    private static TextView dash_count, tv_invite_user;
    private static ImageView img_status;
    private ImageView user_image;
    private Button savepswd;
    private ImageLoader imageLoader;
    private Context mainContext;
    private SideMenuListener sideMenuListener;
    private static Handler handler = new Handler();
    private static SideMenu sideMenu;
    private int temp = 10;

    public static SideMenu getInstance() {
        try {
            if (sideMenu == null) {
                sideMenu = new SideMenu();
            }
            return sideMenu;
        } catch (Exception e) {
            e.printStackTrace();
            return sideMenu;
        }
    }

    public void setDrawerListener(SideMenuListener listener) {
        this.sideMenuListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            mainContext = getActivity();
            View view = inflater.inflate(R.layout.sidemenu, container, false);
            ll_dash = (LinearLayout) view.findViewById(R.id.ll_dash);
            ll_cont = (LinearLayout) view.findViewById(R.id.ll_cont);
            ll_files = (LinearLayout) view.findViewById(R.id.ll_files);
            ll_rounding = (LinearLayout) view.findViewById(R.id.ll_rounding);
            ll_calc = (LinearLayout) view.findViewById(R.id.ll_calc);
            ll_invite_users = (LinearLayout)view.findViewById(R.id.ll_invite_users);
            ll_myacc = (LinearLayout) view.findViewById(R.id.ll_myacc);
            ll_settings = (LinearLayout) view.findViewById(R.id.ll_settings);
            ll_logout = (LinearLayout) view.findViewById(R.id.ll_logout);
            ll_status = (LinearLayout) view.findViewById(R.id.ll_status);
            tv_dash = (TextView) view.findViewById(R.id.tv_dash);
            tv_rounding = (TextView) view.findViewById(R.id.tv_rounding);
            tv_cont = (TextView) view.findViewById(R.id.tv_cont);
            tv_files = (TextView) view.findViewById(R.id.tv_files);
            tv_calc = (TextView) view.findViewById(R.id.tv_calc);
            tv_myacc = (TextView) view.findViewById(R.id.tv_myacc);
            tv_settings = (TextView) view.findViewById(R.id.tv_settings);
            tv_invite_user = (TextView)view.findViewById(R.id.tv_invite_user);
            status = (TextView) view.findViewById(R.id.status);
            img_status = (ImageView) view.findViewById(R.id.img_status);
            user_image = (ImageView) view.findViewById(R.id.user_image);
            userName = (TextView) view.findViewById(R.id.userName);
            dash_count = (TextView) view.findViewById(R.id.dash_count);
            setDefaultMenu();
            imageLoader = new ImageLoader(mainContext);


            ll_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowView();
                    AppMainActivity.onDrawerItemSelected(0);
                }
            });

            ll_dash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDefaultMenu();
                    ll_dash.setBackgroundColor(Color.parseColor("#1F2021"));
                    tv_dash.setTextColor(mainContext.getResources().getColor(R.color.white));
                    AppMainActivity.onDrawerItemSelected(1);
                }
            });

            ll_cont.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDefaultMenu();
                    ll_cont.setBackgroundColor(Color.parseColor("#1F2021"));
                    tv_cont.setTextColor(mainContext.getResources().getColor(R.color.white));
                    AppMainActivity.onDrawerItemSelected(2);
                }
            });

            ll_files.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDefaultMenu();
                    ll_files.setBackgroundColor(Color.parseColor("#1F2021"));
                    tv_files.setTextColor(mainContext.getResources().getColor(R.color.white));
                    AppMainActivity.onDrawerItemSelected(3);
                }
            });

            ll_calc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDefaultMenu();
                    ll_calc.setBackgroundColor(Color.parseColor("#1F2021"));
                    tv_calc.setTextColor(mainContext.getResources().getColor(R.color.white));
                    AppMainActivity.onDrawerItemSelected(4);
                }
            });

            ll_myacc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDefaultMenu();
                    ll_myacc.setBackgroundColor(Color.parseColor("#1F2021"));
                    tv_myacc.setTextColor(mainContext.getResources().getColor(R.color.white));
                    AppMainActivity.onDrawerItemSelected(5);
                }
            });

            ll_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDefaultMenu();
                    ll_settings.setBackgroundColor(Color.parseColor("#1F2021"));
                    tv_settings.setTextColor(mainContext.getResources().getColor(R.color.white));
                    AppMainActivity.onDrawerItemSelected(6);
                }
            });

            ll_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDefaultMenu();
                    AppMainActivity.onDrawerItemSelected(7);
                }
            });
            ll_rounding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDefaultMenu();
                    ll_rounding.setBackgroundColor(Color.parseColor("#1F2021"));
                    tv_rounding.setTextColor(mainContext.getResources().getColor(R.color.white));
                    AppMainActivity.onDrawerItemSelected(8);
                }
            });
            ll_invite_users.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDefaultMenu();
                    ll_invite_users.setBackgroundColor(Color.parseColor("#1F2021"));
                    tv_invite_user.setTextColor(mainContext.getResources().getColor(R.color.white));
                    AppMainActivity.onDrawerItemSelected(9);
                }
            });
            return view;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void setDefaultMenu(){
        ll_dash.setBackgroundColor(mainContext.getResources().getColor(R.color.black2));
        ll_cont.setBackgroundColor(mainContext.getResources().getColor(R.color.black2));
        ll_files.setBackgroundColor(mainContext.getResources().getColor(R.color.black2));
        ll_calc.setBackgroundColor(mainContext.getResources().getColor(R.color.black2));
        ll_invite_users.setBackgroundColor(mainContext.getResources().getColor(R.color.black2));
        ll_myacc.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
        ll_settings.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
        ll_rounding.setBackgroundColor(mainContext.getResources().getColor(R.color.black2));
        ll_logout.setBackgroundColor(mainContext.getResources().getColor(R.color.black3));
        tv_dash.setTextColor(mainContext.getResources().getColor(R.color.grey3));
        tv_cont.setTextColor(mainContext.getResources().getColor(R.color.grey3));
        tv_files.setTextColor(mainContext.getResources().getColor(R.color.grey3));
        tv_calc.setTextColor(mainContext.getResources().getColor(R.color.grey3));
        tv_myacc.setTextColor(mainContext.getResources().getColor(R.color.grey3));
        tv_settings.setTextColor(mainContext.getResources().getColor(R.color.grey3));
        tv_rounding.setTextColor(mainContext.getResources().getColor(R.color.grey3));
        tv_invite_user.setTextColor(mainContext.getResources().getColor(R.color.grey3));
    }

    protected void ShowView() {
        final Dialog dialog = new Dialog(SingleInstance.mainContext, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.status_selection);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT

                , WindowManager.LayoutParams.MATCH_PARENT);
        online = (TextView) dialog.findViewById(R.id.st_online);
        busy = (TextView) dialog.findViewById(R.id.st_busy);
        offline = (TextView) dialog.findViewById(R.id.st_offline);
        invisible = (TextView) dialog.findViewById(R.id.st_invisible);
        savepswd = (Button) dialog.findViewById(R.id.savepswd);
        String status_1 = CallDispatcher.loadCurrentStatus();
        if(status_1.equalsIgnoreCase("online")){
            online.setBackgroundColor(mainContext.getResources().getColor(R.color.green));
            online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.navigation_check, 0, 0, 0);
        }else if(status_1.equalsIgnoreCase("busy")){
            busy.setBackgroundColor(mainContext.getResources().getColor(R.color.yellow));
            busy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.navigation_check, 0, 0, 0);
        }else if(status_1.equalsIgnoreCase("offline")){
            offline.setBackgroundColor(mainContext.getResources().getColor(R.color.pink));
            offline.setCompoundDrawablesWithIntrinsicBounds(R.drawable.navigation_check, 0, 0, 0);
        }else{
            invisible.setBackgroundColor(mainContext.getResources().getColor(R.color.ash));
            invisible.setCompoundDrawablesWithIntrinsicBounds(R.drawable.navigation_check, 0, 0, 0);
        }
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultStatus();
                online.setBackgroundColor(mainContext.getResources().getColor(R.color.green));
                online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.navigation_check, 0, 0, 0);
                temp = 1;
                CallDispatcher.onlineStatus = "online";
            }
        });
        busy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultStatus();
                busy.setBackgroundColor(mainContext.getResources().getColor(R.color.yellow));
                busy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.navigation_check, 0, 0, 0);
                temp = 2;
                CallDispatcher.onlineStatus = "busy";
            }
        });
        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultStatus();
                offline.setBackgroundColor(mainContext.getResources().getColor(R.color.pink));
                offline.setCompoundDrawablesWithIntrinsicBounds(R.drawable.navigation_check, 0, 0, 0);
                temp = 0;
                CallDispatcher.onlineStatus = "offline";
            }
        });
        invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultStatus();
                invisible.setBackgroundColor(mainContext.getResources().getColor(R.color.ash));
                invisible.setCompoundDrawablesWithIntrinsicBounds(R.drawable.navigation_check, 0, 0, 0);
                temp = 3;
                CallDispatcher.onlineStatus = "invisible";
            }
        });
        savepswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (temp != 10) {
                    AppMainActivity.changeFieldType(temp);
                    notifyUI();
                    temp = 10;
                }

                setDefaultStatus();
                dialog.dismiss();
                MyAccountFragment.newInstance(SingleInstance.mainContext).loadCurrentStatus();
            }
        });
        dialog.show();
    }

    private void setDefaultStatus(){
        online.setBackgroundColor(mainContext.getResources().getColor(R.color.black2));
        online.setCompoundDrawablesWithIntrinsicBounds(R.drawable.online_icon, 0, 0, 0);
        busy.setBackgroundColor(mainContext.getResources().getColor(R.color.black2));
        busy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.busy_icon, 0, 0, 0);
        offline.setBackgroundColor(mainContext.getResources().getColor(R.color.black2));
        offline.setCompoundDrawablesWithIntrinsicBounds(R.drawable.offline_icon, 0, 0, 0);
        invisible.setBackgroundColor(mainContext.getResources().getColor(R.color.black2));
        invisible.setCompoundDrawablesWithIntrinsicBounds(R.drawable.invisibleicon, 0, 0, 0);
    }

    public static void notifyUI() {
        final Context mainContext = SingleInstance.mainContext;
        handler.post(new Runnable() {
            @Override
            public void run() {
                int dashCount = 0;
                dashCount += DBAccess.getdbHeler(mainContext)
                        .getUnreadMsgCount(CallDispatcher.LoginUser) + DBAccess.getdbHeler(mainContext)
                        .getUnreadFileCount(CallDispatcher.LoginUser)+ DBAccess.getdbHeler(mainContext)
                        .getUnreadCallCount(CallDispatcher.LoginUser);

                int cal_count = DBAccess.getdbHeler(mainContext).getUnreadCallCount(CallDispatcher.LoginUser);
                Log.d("Callnotifi","users--->"+cal_count);
                Log.d("Callnotifi","users--->"+dashCount);
                if (dashCount > 0) {
                    dash_count.setText(Integer.toString(dashCount));
                    dash_count.setVisibility(View.VISIBLE);
                } else if(dashCount == 0)  {
                    dash_count.setVisibility(View.GONE);
                }
                ProfileBean bean = SingleInstance.myAccountBean;
                if (bean.getFirstname() != null && bean.getLastname() != null)
                    userName.setText(bean.getFirstname() + " " + bean.getLastname());
                else if (bean.getNickname() != null)
                    userName.setText(bean.getNickname());
                else
                    userName.setText(CallDispatcher.LoginUser);
                String status_1 = CallDispatcher.loadCurrentStatus();
                if (status_1.equalsIgnoreCase("online")) {
                    status.setText("Online");
                    img_status.setBackgroundResource(R.drawable.online_icon);
                } else if (status_1.equalsIgnoreCase("away")) {
                    status.setText("Invisible");
                    img_status.setBackgroundResource(R.drawable.invisibleicon);
                } else if (status_1.equalsIgnoreCase("busy")) {
                    status.setText("Busy");
                    img_status.setBackgroundResource(R.drawable.busy_icon);
                } else {
                    status.setText("Offline");
                    img_status.setBackgroundResource(R.drawable.offline_icon);
                }
                SideMenu.getInstance().setProfilePic();
            }
        });
    }

    public void setProfilePic() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    ProfileBean bean=SingleInstance.myAccountBean;
                    if(bean.getPhoto()!=null){
                        String profilePic = bean.getPhoto();
                        if (profilePic.length() > 0) {
                            if (!profilePic.contains("COMMedia")) {
                                profilePic = Environment.getExternalStorageDirectory()+ "/COMMedia/" + profilePic;
                            }
                            imageLoader.DisplayImage(profilePic, user_image, R.drawable.img_user);
                        }
                    }
                } catch (Exception e) {
                    SingleInstance.printLog(null, e.getMessage(), null, e);
                }
            }
        });

    }

    public interface SideMenuListener {
        void onDrawerItemSelected(int position);
    }
}
