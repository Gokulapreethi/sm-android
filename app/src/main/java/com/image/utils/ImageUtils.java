package com.image.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class ImageUtils {
	public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	public static Bitmap decodeScaledBitmapFromResource(Resources res,
			int drawable, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(res, drawable, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, drawable, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	public static int resolveBitmapOrientation(String path) throws IOException {

		File bitmapFile = new File(path);
		ExifInterface exif = null;
		exif = new ExifInterface(bitmapFile.getAbsolutePath());

		return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_NORMAL);
	}

	public static Bitmap applyOrientation(Bitmap bitmap, int orientation) {
		int rotate = 0;
		switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:

				Log.d("Camera123", "ORIENTATION_ROTATE_270");
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				Log.d("Camera123", "ORIENTATION_ROTATE_180");
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				Log.d("Camera123", "ORIENTATION_ROTATE_90");
				rotate = 90;
				break;
			default:
				Log.d("Camera123", "Default");
//    return bitmap;
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix mtx = new Matrix();
		mtx.postRotate(90);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}
}
