package com.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;

public class Utils {
	public static Bitmap getRoundedShape(Bitmap bitmap) {

		Bitmap result = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(result);

		int color = Color.BLUE;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getHeight() / 2, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return result;
	}

	public static String getFilePathString(String filename) {
		String tmp = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/COMMedia";
		if (filename != null) {
			tmp += "/" + filename;
		}
		return tmp;
	}

	public static String removeFullPath(String filename) {
		return filename.replace(
				Environment.getExternalStorageDirectory().getAbsolutePath()
						+ "/COMMedia/", "").trim();
	}

}
