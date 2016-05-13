package com.cg.commongui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.main.AppMainActivity;

class GraphicsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    @Override
    public void setContentView(View view) {
    	   	
        if (false) { // set to true to test Picture
            ViewGroup vg = new PictureLayout(this);
            vg.addView(view);
            view = vg;
        }
        
        super.setContentView(view);
    }
}


