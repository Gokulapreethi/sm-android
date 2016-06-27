package com.group.chat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cg.DB.DBAccess;
import com.cg.snazmed.R;


import org.lib.model.chattemplatebean;

import java.util.Vector;

public class ChatTemplateActivity extends Activity {

    Context context;
    ListView tmp_listview;
    ImageView im_delete;
    ImageView im_edit;
    Vector<chattemplatebean> templateList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat_template);
        context=this;
        tmp_listview=(ListView)findViewById(R.id.lv_templates);
        im_delete=(ImageView)findViewById(R.id.deleteview);
        im_edit=(ImageView)findViewById(R.id.edview);
        templateList= DBAccess.getdbHeler(context).getChatTemplates();
        final ChattemplateAdpater templateAdapter = new ChattemplateAdpater(ChatTemplateActivity.this, templateList);
        tmp_listview.setAdapter(templateAdapter);
        tmp_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                chattemplatebean bean = templateAdapter.getItem(position);
                for (chattemplatebean chattemplatebean : templateList) {
                    if (chattemplatebean.getTempletmessage().equalsIgnoreCase(bean.getTempletmessage())) {
                        chattemplatebean.setSelected(true);
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
                for(chattemplatebean chattemplatebean:templateList){
                    if(chattemplatebean.isSelected()){
                        edit=true;
                        String message=chattemplatebean.getTempletmessage();
                        Intent intent=new Intent();
                        intent.putExtra("MESSAGE", message);
                        setResult(12, intent);
                        finish();//finishing activity
                        break;
                    }
                }
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
    private void showTemplates()
    {
        final Dialog dialog1 = new Dialog(ChatTemplateActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.chat_template);
        dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog1.getWindow().setBackgroundDrawableResource(R.color.trans_black2);
        dialog1.show();
        ListView chatlist=(ListView)dialog1.findViewById(R.id.chatlist);
        ChatListtemplateAdpater templateAdapter = new ChatListtemplateAdpater(ChatTemplateActivity.this, templateList);
        chatlist.setAdapter(templateAdapter);
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

                messages.setText(bean.getTempletmessage());

            }catch(Exception e){
                e.printStackTrace();
                Log.d("RRRR", "Error FindpeopleAdapter.java => " + e.toString());
            }
            return convertView;
        }
    }

}
