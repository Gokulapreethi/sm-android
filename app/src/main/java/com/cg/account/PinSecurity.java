package com.cg.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class PinSecurity extends Activity implements View.OnClickListener {

    private Context context;
    int count = -1 ;
    int wrongCount = 0;
    private StringBuffer pin;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pinsecurity);
        context = PinSecurity.this;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        pin = new StringBuffer("ABCD");

        TextView btn1 = (TextView) findViewById(R.id.btn1);
        TextView btn2 = (TextView) findViewById(R.id.btn2);
        TextView btn3 = (TextView) findViewById(R.id.btn3);
        TextView btn4 = (TextView) findViewById(R.id.btn4);
        TextView btn5 = (TextView) findViewById(R.id.btn5);
        TextView btn6 = (TextView) findViewById(R.id.btn6);
        TextView btn7 = (TextView) findViewById(R.id.btn7);
        TextView btn8 = (TextView) findViewById(R.id.btn8);
        TextView btn9 = (TextView) findViewById(R.id.btn9);
        TextView btn0 = (TextView) findViewById(R.id.btn0);
        LinearLayout btnx = (LinearLayout) findViewById(R.id.btnx);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        btnx.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        LinearLayout btnx = (LinearLayout) findViewById(R.id.btnx);
        ImageView pin1 = (ImageView) findViewById(R.id.pin1);
        ImageView pin2 = (ImageView) findViewById(R.id.pin2);
        ImageView pin3 = (ImageView) findViewById(R.id.pin3);
        ImageView pin4 = (ImageView) findViewById(R.id.pin4);
        TextView wrongcoutn = (TextView) findViewById(R.id.wrongcoutn);
        Character temp = null;
        if(view.getId() == btnx.getId()){
            if(count>-1)
                count--;
        }else{
            if(count<3)
                count++;
        }
        switch(view.getId()){
            case R.id.btn1:
                temp ='1';
                break;
            case R.id.btn2:
                temp ='2';
                break;
            case R.id.btn3:
                temp ='3';
                break;
            case R.id.btn4:
                temp ='4';
                break;
            case R.id.btn5:
                temp ='5';
                break;
            case R.id.btn6:
                temp ='6';
                break;
            case R.id.btn7:
                temp ='7';
                break;
            case R.id.btn8:
                temp ='8';
                break;
            case R.id.btn9:
                temp ='9';
                break;
            case R.id.btn0:
                temp ='0';
                break;
        }
        if(temp!=null && count<4 && count>-1 ){
            pin.setCharAt(count,temp);
        }
        Log.d("RRRR","entered "+pin.toString()+" original "+AppMainActivity.pinNo);
        switch(count){
            case 3:
                pin1.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinentered));
                pin2.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinentered));
                pin3.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinentered));
                pin4.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinentered));
                if(AppMainActivity.pinNo.equals(pin.toString())) {
                    finish();
                    wrongCount = 0;
                    wrongcoutn.setVisibility(View.INVISIBLE);
                }else{
                    count = -1;
                    pin1.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                    pin2.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                    pin3.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                    pin4.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                    vibrator.vibrate(500);
                    wrongcoutn.setVisibility(View.VISIBLE);
                    wrongcoutn.setText("No of times entered wrong pin "+(++wrongCount));
                    Toast.makeText(context,"Wrong Pin",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                pin1.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinentered));
                pin2.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinentered));
                pin3.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinentered));
                pin4.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                break;
            case 1:
                pin1.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinentered));
                pin2.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinentered));
                pin3.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                pin4.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                break;
            case 0:
                pin1.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinentered));
                pin2.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                pin3.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                pin4.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                break;
            case -1:
                pin1.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                pin2.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                pin3.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                pin4.setBackgroundDrawable(getResources().getDrawable(R.drawable.pinblank));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder buider = new AlertDialog.Builder(context);
            buider.setMessage(context.getResources().getString(R.string.app_background))
                    .setPositiveButton(context.getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        moveTaskToBack(true);
                                    } catch (Exception e) {
                                        SingleInstance.printLog(null, e.getMessage(), null, e);
                                    }
                                }
                            })
                    .setNegativeButton(context.getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = buider.create();
            alert.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
