package com.cg.account;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.util.SingleInstance;

import java.util.UUID;

public class GenerateNewInviteCode extends Activity {

    private Context context;
    private Handler handler = new Handler();
    private Button generate,regenerate;
    private  TextView invitecode;
    protected void onCreate(Bundle savedInstanceState) {


        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.invite2);
        context = this;
        ImageView back = (ImageView) findViewById(R.id.back);
        invitecode = (TextView) findViewById(R.id.invitecode);
        regenerate = (Button) findViewById(R.id.regenerate);
        Button sms=(Button)findViewById(R.id.sms);
        Button mail=(Button)findViewById(R.id.mail);
//        invitecode.setText(Utility.getSessionID());
        String code=UUID.randomUUID().toString().trim().substring(0,8).toUpperCase();
        invitecode.setText(code);
        regenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code=UUID.randomUUID().toString().trim().substring(0,8).toUpperCase();
                invitecode.setText(code);

            }
        });


        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSimSupport(context)) {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.sms_dialog);
                    dialog.setTitle("Select any service you want to make");
                    Button send = (Button) dialog.findViewById(R.id.sendSMSBtn);
                    final TextView PhoneNo = (TextView) dialog.findViewById(R.id.toPhoneNumberTV);
                    dialog.show();
                    send.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {

                            try {
                                String phoneNo = PhoneNo.getText().toString();
                                String msg = invitecode.getText().toString();
                                try {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                                    Toast.makeText(getApplicationContext(), "Message Sent",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                } catch (Exception ex) {
                                    Toast.makeText(getApplicationContext(),
                                            ex.getMessage().toString(),
                                            Toast.LENGTH_LONG).show();
                                    ex.printStackTrace();
                                }
                                dialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else
                    showToast("Please insert Sim.Cannot send message without Sim");

            }
        });
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sendEmail();
            }
        });

    }
    public static boolean isSimSupport(Context context)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);

    }
    public void showToast(final String message) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    Toast.makeText(getApplicationContext(), message,
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }
    public void sendEmail()
    {
        try {
            String content = "Username : "
                    + CallDispatcher.LoginUser
                    + "\n OS Details : Android \n Version : "
                    + android.os.Build.VERSION.RELEASE
                    + " \n Make : "
                    + android.os.Build.BRAND
                    + "\n Model : "
                    + android.os.Build.MODEL
                    + "\n Application Release Version : "
                    + SingleInstance.mainContext.getResources().getString(
                    R.string.app_version);
            final Intent emailIntent = new Intent(
                    android.content.Intent.ACTION_SEND_MULTIPLE);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    context.getResources().getString(R.string.app_name)
                            + " Invite Code");
            emailIntent.putExtra(
                    android.content.Intent.EXTRA_STREAM, invitecode.getText().toString());
            emailIntent
                    .putExtra(android.content.Intent.EXTRA_TEXT, content);
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
