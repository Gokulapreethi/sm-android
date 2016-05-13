package com.cg.commongui;


/**
 * Created by Rathika on 2/12/2016.
 */

        import android.content.Intent;
        import android.net.Uri;
        import android.net.http.SslError;
        import android.os.Bundle;

        import android.os.Environment;
        import android.util.Log;
        import android.view.View;

        import android.webkit.SslErrorHandler;
        import android.webkit.WebView;
        import android.webkit.WebViewClient;
        import android.widget.Button;
        import com.cg.snazmed.R;
        import java.io.File;
        import android.app.Activity;
        import android.graphics.Bitmap;
        import android.view.View.OnClickListener;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;


public class WebView_doc extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.show_webview);
        TextView title = (TextView) findViewById(R.id.title);

        Button cancel = (Button) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutView);
        Bundle extras = getIntent().getExtras();
        Uri myUri = Uri.parse(extras.getString("path"));
        //String extn=extras.getString("exten");
        //Uri myUri = extras.getString("path");
        //Log.i("AAAA", "webviewfilepath " + extn);
        Log.i("AAAA", "webviewfilepath " + myUri);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);

            webView.loadUrl(myUri.toString());


    }
private class MyBrowser extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}}
