package com.cg.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.main.Registration;

import java.util.UUID;


public class GetEndorsement extends Activity {
    private Context context;
    Boolean isClicked=false;
    protected void onCreate(Bundle savedInstanceState) {


        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.get_endrosment);
        context = this;
        Button copy = (Button) findViewById(R.id.copy);
        Button mail = (Button) findViewById(R.id.mail);
        Button sms = (Button) findViewById(R.id.sms);
        Button copy2 = (Button) findViewById(R.id.copy2);
        Button mail2 = (Button) findViewById(R.id.mail2);
        Button sms2 = (Button) findViewById(R.id.sms2);
        final Button refresh = (Button) findViewById(R.id.refresh);
        final TextView code1=(TextView)findViewById(R.id.code1);
        final TextView code2=(TextView)findViewById(R.id.code2);
        TextView newcode=(TextView)findViewById(R.id.newcode);
        final TextView title1=(TextView)findViewById(R.id.title);
        final TextView title2=(TextView)findViewById(R.id.title2);
        final LinearLayout title3=(LinearLayout)findViewById(R.id.title3);
        final ImageView tick=(ImageView)findViewById(R.id.success_img);
        final LinearLayout endorse=(LinearLayout)findViewById(R.id.endorse);
        final LinearLayout img_icons=(LinearLayout)findViewById(R.id.img_lay1);
        final LinearLayout expire_lay=(LinearLayout)findViewById(R.id.expire_lay1);
        final LinearLayout linear2=(LinearLayout)findViewById(R.id.linear2);
        final LinearLayout linear1=(LinearLayout)findViewById(R.id.linear1);
        String code= UUID.randomUUID().toString().trim().substring(0,10).toUpperCase();
        code1.setText(code);
        String codes= UUID.randomUUID().toString().trim().substring(0,10).toUpperCase();
        code2.setText(codes);
        copy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    isClicked = true;
                    tick.setVisibility(View.VISIBLE);
                    refresh.setVisibility(View.VISIBLE);
                    endorse.setVisibility(View.VISIBLE);
                    title2.setVisibility(View.VISIBLE);
                    title3.setVisibility(View.VISIBLE);
                    img_icons.setVisibility(View.GONE);
                    expire_lay.setVisibility(View.GONE);
                    title1.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    isClicked=true;
                    tick.setVisibility(View.VISIBLE);
                    endorse.setVisibility(View.VISIBLE);
                    title2.setVisibility(View.VISIBLE);
                    title3.setVisibility(View.VISIBLE);
                    img_icons.setVisibility(View.GONE);
                    expire_lay.setVisibility(View.GONE);
                    title1.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    isClicked=true;
                    tick.setVisibility(View.VISIBLE);
                    endorse.setVisibility(View.VISIBLE);
                    title2.setVisibility(View.VISIBLE);
                    title3.setVisibility(View.VISIBLE);
                    img_icons.setVisibility(View.GONE);
                    expire_lay.setVisibility(View.GONE);
                    title1.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        copy2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    if(isClicked) {
                        linear1.setVisibility(View.VISIBLE);
                        linear2.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        newcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    Intent intent = new Intent(GetEndorsement.this, VeratadVerification.class);
                    intent.putExtra("code1",code1.getText().toString());
                    intent.putExtra("code2",code2.getText().toString());
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
