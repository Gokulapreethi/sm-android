package com.util;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commongui.TouchImageView;
import com.crypto.AESFileCrypto;
import com.main.AppMainActivity;

public class FullScreenImage extends Activity {
	ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.image_view);
		Button cancel = (Button) findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		try {
			String filePath = getIntent().getStringExtra("image");

			if (filePath != null) {
				File file = new File(filePath);

				if (file.exists()) {

					// start 07-10-15 changes //

					Bitmap bitmap = AESFileCrypto.decryptBitmap(file.getAbsolutePath());
//					bitmap = ImageViewer.getResizedBitmap(bitmap,480,640);

					// end 07-10-15 changes //

//					Bitmap bitmap = ImageUtils.decodeScaledBitmapFromSdCard(
//							file.getAbsolutePath(), 640, 480);

					LinearLayout layout = (LinearLayout) findViewById(R.id.layoutView);

					TouchImageView touch = new TouchImageView(this);
					touch.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.MATCH_PARENT));
					touch.setImageBitmap(bitmap);
					touch.setMaxZoom(3f);

					layout.addView(touch);
				} else {
					showToastAndClose("Image file does not exist.");
				}
			} else {
				showToastAndClose("Image Filepath is Wrong.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			showToastAndClose("Unable to view image.");
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			showToastAndClose("Unable to view image.");
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    private void showToastAndClose(String message) {

		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
		FullScreenImage.this.finish();
	}

}
