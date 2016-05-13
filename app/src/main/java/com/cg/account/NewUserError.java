package com.cg.account;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cg.snazmed.R;

public class NewUserError extends Activity {
    private Context context;
    private  EditText username;
    protected void onCreate(Bundle savedInstanceState) {


        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.new_user_error);
        context = this;
        Button done = (Button) findViewById(R.id.sendrequest);
        username = (EditText) findViewById(R.id.username);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                try {
                    if ((username.getText().toString().length() > 0)) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialogue_max_no_attempt);
                        dialog.setTitle("Select any service you want to make");
                        Button done = (Button) dialog.findViewById(R.id.button);
                        dialog.show();
                        done.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {

                                try {
                                    dialog.dismiss();
                                    invitecodeExpired();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else
                        showToast("Please Enter Invite Code");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void invitecodeExpired()
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.invitecodeexpired);
        dialog.setTitle("Select any service you want to make");
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.black);
        dialog.show();
        Button done = (Button)dialog. findViewById(R.id.donebtn);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    dialog.dismiss();
                    invitecodeUsed();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void invitecodeUsed()
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.invitecodeused);
        dialog.setTitle("Select any service you want to make");
        dialog.show();
        Button done = (Button)dialog. findViewById(R.id.donebtn);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    dialog.dismiss();
                    Intent intent = new Intent(NewUserError.this, InviteCodeAttempt.class);
   				    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void showToast(String msg) {
        Toast.makeText(NewUserError.this, msg, Toast.LENGTH_LONG).show();
    }
}
