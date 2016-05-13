package com.util;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.cg.snazmed.R;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.crypto.AESFileCrypto;

public class VideoPlayer extends Activity {
	VideoView mVideoView;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.video_player_new);
		context = this;
		WebServiceReferences.contextTable.put("customvideoplayer", context);
		mVideoView = (VideoView) findViewById(R.id.video_view);
		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(mVideoView);
		 int msec = getIntent().getExtras().getInt("timevideo",0);
		String filePath = getIntent().getStringExtra("video");
		if (filePath == null)
			filePath = getIntent().getStringExtra("File_Path");

		if (filePath != null) {
			try {
				// start 07-10-15 changes

				filePath = AESFileCrypto.decryptFile(context,filePath);

				// ended 07-10-15 changes


				Uri uri = Uri.parse(filePath);
				mVideoView.setMediaController(mediaController);
				mVideoView.setVideoURI(uri);
				mVideoView.requestFocus();
				mVideoView.seekTo(msec);
				mVideoView
						.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								VideoPlayer.this.finish();
							}
						});
				mVideoView.start();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"Unable to play video.", Toast.LENGTH_SHORT).show();
				VideoPlayer.this.finish();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Video Filepath is Wrong.",
					Toast.LENGTH_SHORT).show();
			VideoPlayer.this.finish();
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("customvideoplayer");
		super.onDestroy();
	}
}
