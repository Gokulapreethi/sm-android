package com.cg.account;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.cg.snazmed.R;

public class InviteCodeAttempt extends Activity {
    private Context context;
    protected void onCreate(Bundle savedInstanceState) {


        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.invitecode_error);
        context = this;

    }
}
