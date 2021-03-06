package com.cg.commongui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;

public class HTML5WebView extends WebView {

	private Context mContext;

	private MyWebChromeClient mWebChromeClient;

	private View mCustomView;

	private FrameLayout mCustomViewContainer;

	private WebChromeClient.CustomViewCallback mCustomViewCallback;

	private FrameLayout mContentView;

	private FrameLayout mBrowserFrameLayout;

	private FrameLayout mLayout;

	static final String LOGTAG = "HTML5WebView";

	private CallDispatcher callDisp = null;

	private void init(Context context) {
		mContext = context;
		Activity a = (Activity) mContext;

		DisplayMetrics displaymetrics = new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int noScrHeight = displaymetrics.heightPixels;
		int noScrWidth = displaymetrics.widthPixels;

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);

		callDisp.setNoScrHeight(noScrHeight);
		callDisp.setNoScrWidth(noScrWidth);
		displaymetrics = null;

		LayoutInflater inflateLayout = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLayout = new FrameLayout(context);

		mBrowserFrameLayout = (FrameLayout) LayoutInflater.from(a).inflate(
				R.layout.custom_screen, null);
		mContentView = (FrameLayout) mBrowserFrameLayout
				.findViewById(R.id.main_content);
		mCustomViewContainer = (FrameLayout) mBrowserFrameLayout
				.findViewById(R.id.fullscreen_custom_content);

		mLayout.addView(mBrowserFrameLayout);
		mWebChromeClient = new MyWebChromeClient();
		setWebChromeClient(mWebChromeClient);
		setWebViewClient(new MyWebViewClient());
		WebSettings s = getSettings();
		s.setBuiltInZoomControls(true);
		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		s.setUseWideViewPort(true);
		s.setLoadWithOverviewMode(true);
		s.setSavePassword(true);
		s.setSaveFormData(true);
		s.setJavaScriptEnabled(true);
		// s.setPluginsEnabled(true);
		s.setPluginState(PluginState.ON);
		// enable navigator.geolocation
		s.setGeolocationEnabled(true);
		s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");

		// enable Web Storage: localStorage, sessionStorage
		s.setDomStorageEnabled(true);
		CallDispatcher.pdialog = new ProgressDialog(context);
		callDisp.showprogress(CallDispatcher.pdialog, context);
		mContentView.addView(this);
	}

	public HTML5WebView(Context context) {
		super(context);
		init(context);
	}

	public HTML5WebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public HTML5WebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ViewGroup getLayout() {
		return mLayout;
	}

	public boolean inCustomView() {
		return (mCustomView != null);
	}

	public void hideCustomView() {
		mWebChromeClient.onHideCustomView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((mCustomView == null) && canGoBack()) {
				goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private class MyWebChromeClient extends WebChromeClient {
		private Bitmap mDefaultVideoPoster;
		private View mVideoProgressView;

		@Override
		public void onShowCustomView(View view,
				WebChromeClient.CustomViewCallback callback) {
			Log.i(LOGTAG, "here in on ShowCustomView");
			HTML5WebView.this.setVisibility(View.GONE);

			// if a view already exists then immediately terminate the new one
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			mCustomViewContainer.addView(view);
			mCustomView = view;
			mCustomViewCallback = callback;
			mCustomViewContainer.setVisibility(View.VISIBLE);
		}

		@Override
		public void onHideCustomView() {

			if (mCustomView == null)
				return;

			// Hide the custom view.
			mCustomView.setVisibility(View.GONE);

			// Remove the custom view from its container.
			mCustomViewContainer.removeView(mCustomView);
			mCustomView = null;
			mCustomViewContainer.setVisibility(View.GONE);
			mCustomViewCallback.onCustomViewHidden();

			HTML5WebView.this.setVisibility(View.VISIBLE);

			// Log.i(LOGTAG, "set it to webVew");
		}

		@Override
		public Bitmap getDefaultVideoPoster() {
			// Log.i(LOGTAG, "here in on getDefaultVideoPoster");
			if (mDefaultVideoPoster == null) {
				mDefaultVideoPoster = BitmapFactory.decodeResource(
						getResources(), R.drawable.default_video_poster);
			}
			return mDefaultVideoPoster;
		}

		@Override
		public View getVideoLoadingProgressView() {
			// Log.i(LOGTAG, "here in on getVideoLoadingPregressView");

			if (mVideoProgressView == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				mVideoProgressView = inflater.inflate(
						R.layout.video_loading_progress, null);
			}
			return mVideoProgressView;
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			((Activity) mContext).setTitle(title);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			((Activity) mContext).getWindow().setFeatureInt(
					Window.FEATURE_PROGRESS, newProgress * 100);
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
		}

	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i(LOGTAG, "shouldOverrideUrlLoading: " + url);
			view.getSettings().setJavaScriptEnabled(true);
			view.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
			// view.getSettings().setPluginsEnabled(true);
			view.getSettings().setBuiltInZoomControls(true);
			view.getSettings().setLayoutAlgorithm(
					WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
			view.getSettings().setUseWideViewPort(true);
			view.getSettings().setLoadWithOverviewMode(true);
			view.getSettings().setSavePassword(true);
			view.getSettings().setSaveFormData(true);
			view.getSettings().setPluginState(PluginState.ON);
			view.getSettings().setGeolocationEnabled(true);
			view.getSettings().setGeolocationDatabasePath(
					"/data/data/org.itri.html5webview/databases/");

			view.getSettings().setDomStorageEnabled(true);
			view.loadUrl(url);

			return true;
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// TODO Auto-generated method stub

			handler.proceed();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			cancelDialog();

		}

	}

	private void cancelDialog() {
		if (CallDispatcher.pdialog != null) {
			CallDispatcher.pdialog.dismiss();
			CallDispatcher.pdialog = null;
		}
	}

	static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);
}