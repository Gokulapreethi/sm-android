package com.cg.account;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cg.snazmed.R;


public class VeratadVerification extends Activity {
    private Context context;
    protected void onCreate(Bundle savedInstanceState) {


        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.baa);
        context = this;
        Button ok=(Button)findViewById(R.id.sendrequest);
        Button back=(Button)findViewById(R.id.cancel);
        Boolean isFrom=getIntent().getBooleanExtra("myaccount",false);
        if(isFrom)
            ok.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        final TextView tv_code1=(TextView)findViewById(R.id.code1);
//        final TextView tv_code2=(TextView)findViewById(R.id.code2);
//        Button done = (Button) findViewById(R.id.donebtn);
//        final String code1=getIntent().getStringExtra("code1");
//        final String code2=getIntent().getStringExtra("code2");
//        tv_code1.setText(code1);
//        tv_code2.setText(code2);
//        done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                finish();
//            }
//        });
    }
}
