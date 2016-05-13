package com.cg.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cg.snazmed.R;


public class NewUserRole extends Activity {

    private Context context;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.newuserrole);
        context=this;
        ImageView back = (ImageView) findViewById(R.id.back);
        LinearLayout physician = (LinearLayout) findViewById(R.id.physician);
        LinearLayout student= (LinearLayout) findViewById(R.id.students);
        LinearLayout invitecode= (LinearLayout) findViewById(R.id.invitecode);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        physician.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(NewUserRole.this, NewUserTC.class);
                    intent.putExtra("role", "Physician");
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        student.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(NewUserRole.this, NewUserTC.class);
                    intent.putExtra("role","Student");
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        invitecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(NewUserRole.this, new_user_5.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
