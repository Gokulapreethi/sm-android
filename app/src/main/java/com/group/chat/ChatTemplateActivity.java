package com.group.chat;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Fingerprint.MainActivity;
import com.cg.DB.DBAccess;
import com.cg.account.PinSecurity;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.main.AppMainActivity;
import com.util.SingleInstance;


import org.lib.model.ChattemplateModifieddate;
import org.lib.model.WebServiceBean;
import org.lib.model.chattemplatebean;

import java.util.Vector;

public class ChatTemplateActivity extends Activity {

    Context context;
    ListView tmp_listview;
    ImageView im_delete;
    ImageView im_edit;
    Vector<chattemplatebean> templateList;
    private CallDispatcher calldisp;
    ProgressDialog progressDialog;
    String messagecontent;
    private Handler handler = new Handler();
    Dialog dialog,dialog1;
    ChattemplateAdpater templateAdapter;
    ChatListtemplateAdpater chattemplateAdapter;
    String templateid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat_template);
        context=this;
        tmp_listview=(ListView)findViewById(R.id.lv_templates);
        calldisp = CallDispatcher.getCallDispatcher(context);
        progressDialog= new ProgressDialog(context);
        im_delete=(ImageView)findViewById(R.id.deleteview);
        im_edit=(ImageView)findViewById(R.id.edview);
        templateList= DBAccess.getdbHeler(context).getChatTemplates();
        Log.i("templatelist","chattemplate"+templateList.size());
        templateAdapter = new ChattemplateAdpater(ChatTemplateActivity.this, templateList);
        tmp_listview.setAdapter(templateAdapter);
        tmp_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                chattemplatebean bean = templateAdapter.getItem(position);
                for (chattemplatebean chattemplatebean : templateList) {
                    if (chattemplatebean.getTempletmessage().equalsIgnoreCase(bean.getTempletmessage())) {
                        chattemplatebean.setSelected(true);
                                String message=chattemplatebean.getTempletmessage();
                                Intent intent=new Intent();
                                intent.putExtra("MESSAGE", message);
                                setResult(12, intent);
                                finish();//finishing activity

                    } else {
                        chattemplatebean.setSelected(false);
                    }
                }

                templateAdapter.notifyDataSetChanged();

            }
        });

        im_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        im_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean edit=false;
                if(!edit)
                    showTemplates();
            }
        });


    }

    public class ChattemplateAdpater extends ArrayAdapter<chattemplatebean> {

        private Context context;
        private Vector<chattemplatebean> templatelist;
        private LayoutInflater inflater = null;
        int i = 0;

        /************* CustomAdapter Constructor *****************/
        public ChattemplateAdpater(Context context, Vector<chattemplatebean> tempList) {

            super(context, R.layout.template_row, tempList);
            /********** Take passed values **********/
            this.context = context;
            this.templatelist = tempList;
            i=tempList.size();


            /*********** Layout inflator to call external xml layout () ***********/
        }

        public chattemplatebean getItem(int position) {
            return templatelist.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public Vector<chattemplatebean> getAllItem(){
            return templatelist;
        }

        /******
         * Depends upon data size called for each row , Create each ListView row
         *****/
        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            try {
                if (convertView == null) {
                    inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.template_row,
                            null);
                }
                final chattemplatebean bean = templatelist.get(position);
                TextView messages=(TextView)convertView.findViewById(R.id.tv_message);
                if(bean.isSelected()){
                    messages.setBackgroundColor(Color.parseColor("#ffffff"));
                }else{
                    messages.setBackgroundColor(Color.parseColor("#000000"));
                }
                messages.setText(bean.getTempletmessage());

            }catch(Exception e){
                e.printStackTrace();
                Log.d("RRRR", "Error FindpeopleAdapter.java => " + e.toString());
            }
            return convertView;
        }
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        AppMainActivity.inActivity = this;
        AppReference.fileOpen=false;
        context = this;
        if(AppReference.mainContext.isPinEnable) {
            if (AppReference.mainContext.openPinActivity) {
                AppReference.mainContext.openPinActivity=false;
                if(Build.VERSION.SDK_INT>20 && AppReference.mainContext.isTouchIdEnabled) {
                    Intent i = new Intent(ChatTemplateActivity.this, MainActivity.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(ChatTemplateActivity.this, PinSecurity.class);
                    startActivity(i);
                }
            } else {
                AppReference.mainContext.count=0;
                AppReference.mainContext.registerBroadcastReceiver();
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("pin", "Groupchatactivity Onstop");
        AppReference.mainContext.isApplicationBroughtToBackground();
        if(AppReference.fileOpen){
            Log.i("pin", "Groupchatactivity Onstop AppReference.fileOpen==true");
            AppReference.mainContext.openPinActivity=false;
        }

    }
    private void showTemplates()
    {
        dialog1 = new Dialog(ChatTemplateActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.chat_template);
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog1.getWindow().setBackgroundDrawableResource(R.color.trans_black2);
        dialog1.show();
        ListView chatlist=(ListView)dialog1.findViewById(R.id.chatlist);
        Button cancel=(Button)dialog1.findViewById(R.id.cancel);
        Vector<chattemplatebean> templist=DBAccess.getdbHeler(context).getChatTemplates();
        chattemplatebean cBean=new chattemplatebean();
        cBean.setTempletmessage(" ");
        templist.add(cBean);
        chattemplateAdapter = new ChatListtemplateAdpater(ChatTemplateActivity.this, templist);
        chatlist.setAdapter(chattemplateAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }
    public class ChatListtemplateAdpater extends ArrayAdapter<chattemplatebean> {

        private Context context;
        private Vector<chattemplatebean> templatelist;
        private LayoutInflater inflater = null;
        int i = 0;

        /************* CustomAdapter Constructor *****************/
        public ChatListtemplateAdpater(Context context, Vector<chattemplatebean> tempList) {

            super(context, R.layout.chat_template_row, tempList);
            /********** Take passed values **********/
            this.context = context;
            this.templatelist = tempList;
            i=tempList.size();


            /*********** Layout inflator to call external xml layout () ***********/
        }

        public chattemplatebean getItem(int position) {
            return templatelist.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public Vector<chattemplatebean> getAllItem(){
            return templatelist;
        }

        /******
         * Depends upon data size called for each row , Create each ListView row
         *****/
        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            try {
                if (convertView == null) {
                    inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.chat_template_row,
                            null);
                }
                final chattemplatebean bean = templatelist.get(position);
                TextView messages=(TextView)convertView.findViewById(R.id.tv_message);
                ImageView editview=(ImageView)convertView.findViewById(R.id.editview);
                ImageView deleteview=(ImageView)convertView.findViewById(R.id.deleteview);
                ImageView plus=(ImageView)convertView.findViewById(R.id.plus);

                editview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditTemplate(bean.getTempletmessage(),bean.getTempletid(),"update");
                    }
                });
                messages.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                deleteview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] param = new String[7];
                        param[0] = CallDispatcher.LoginUser;
                        param[1] = "34";
                        param[2] = "1";
                        param[3] = "delete";
                        param[4] = bean.getTempletid();
                        param[5] = "";
                        param[6] = "";
                        templateid = bean.getTempletid();
                        calldisp.showprogress(progressDialog, context);
                        WebServiceReferences.webServiceClient.UpdateChatTemplate(param, context);
                    }
                });
                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditTemplate("", "", "add");
                    }
                });

                if(bean.getEditvalue()!=null && bean.getEditvalue().equalsIgnoreCase("0")){
                    editview.setVisibility(View.GONE);
                    deleteview.setVisibility(View.GONE);
                }else {
                    editview.setVisibility(View.VISIBLE);
                    deleteview.setVisibility(View.VISIBLE);
                }
                if(position==templatelist.size()-1){
                    plus.setVisibility(View.VISIBLE);
                    editview.setVisibility(View.GONE);
                    deleteview.setVisibility(View.GONE);
                }else {
                    plus.setVisibility(View.GONE);
                }

                messages.setText(bean.getTempletmessage());

            }catch(Exception e){
                e.printStackTrace();
            }
            return convertView;
        }
    }

    private void showEditTemplate(final String message,final String id, final String type)
    {
        dialog = new Dialog(ChatTemplateActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_buddy_diagnosis);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
        dialog.show();
        TextView tv=(TextView)dialog.findViewById(R.id.edit_title);
        ListView lv=(ListView)dialog.findViewById(R.id.listview);
        final EditText ed_message=(EditText)dialog.findViewById(R.id.new_descrip);
        Button done=(Button)dialog.findViewById(R.id.edit_diag_done);
        Button cancel=(Button)dialog.findViewById(R.id.cancel);
        lv.setVisibility(View.GONE);
        tv.setText("EDIT QUICK MESSAGE");
        ed_message.setText(message);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] param=new String[7];
                param[0]=CallDispatcher.LoginUser;
                param[1]="34";
                param[2]="1";
                param[3]=type;
                param[4]=id;
                param[5]=ed_message.getText().toString();
                param[6]="";
                messagecontent=ed_message.getText().toString();
                templateid=id;
                calldisp.showprogress(progressDialog, context);
                WebServiceReferences.webServiceClient.UpdateChatTemplate(param,context);
            }
        });
    }
    public void notifyUpdateChatTemplate(Object obj){
        calldisp.cancelDialog(progressDialog);
        if(obj instanceof String[]){
            String[] result = (String[]) obj;
            ChattemplateModifieddate bean=new ChattemplateModifieddate();
            bean.setModifieddatetime(result[0]);
            DBAccess.getdbHeler().chatTemplateModifiedDateDelete();
            DBAccess.getdbHeler().insertChatTemplateModifieddate(bean);
            if(result[1].equalsIgnoreCase("delete")){
                DBAccess.getdbHeler().deleteChatTemplate(templateid);
            }else {
                chattemplatebean cBean = new chattemplatebean();
                if(result[1].equalsIgnoreCase("add"))
                cBean.setTempletid(result[2]);
                else
                cBean.setTempletid(templateid);
                cBean.setTempletmessage(messagecontent);
                DBAccess.getdbHeler().insertChatTemplate(cBean);
            }
            showToast(result[3]);
            templateList.clear();
            templateList= DBAccess.getdbHeler(context).getChatTemplates();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    templateAdapter = new ChattemplateAdpater(ChatTemplateActivity.this, templateList);
                    tmp_listview.setAdapter(templateAdapter);
                    templateAdapter.notifyDataSetChanged();
                }
            });
            if(dialog!=null)
            dialog.dismiss();
            dialog1.dismiss();

        }else if(obj instanceof WebServiceBean){
            WebServiceBean server_response = (WebServiceBean) obj;
           showToast(server_response.getText());
            if(dialog!=null)
            dialog.dismiss();
        }
    }
    private void showToast(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
