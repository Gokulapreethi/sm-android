package com.cg.account;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.cg.snazmed.R;

public class ShowBuddy extends Activity {
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.search_contact);
        context = this;
    }
}
