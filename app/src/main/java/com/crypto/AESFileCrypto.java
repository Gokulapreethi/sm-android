/**
 * 
 */
package com.crypto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.core.AESCrypto;
import org.core.ProprietarySignalling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author GK
 * 
 */
public class AESFileCrypto {

	private static boolean flag = false;

	public static boolean encryptFile(String filePath) {
		try {
			if(flag) {
				File file = new File(filePath);
				if (file.exists()) {
					FileInputStream fileInputStream = new FileInputStream(file);
					byte[] bytes = new byte[(int) file.length()];
					fileInputStream.read(bytes);
					fileInputStream.close();

					byte[] encrypted = AESCrypto.encrypt(ProprietarySignalling.seceretKey, bytes);

					file.delete();

					FileOutputStream fileOuputStream = new FileOutputStream(file);
					fileOuputStream.write(encrypted);
					fileOuputStream.close();
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Bitmap decryptBitmap(String filePath) {
		try {
			if(flag) {
				File file = new File(filePath);
				if (file.exists()) {
					FileInputStream fileInputStream = new FileInputStream(file);
					byte[] bytes = new byte[(int) file.length()];
					fileInputStream.read(bytes);
					fileInputStream.close();

					byte[] decrypted = AESCrypto.decrypt(ProprietarySignalling.seceretKey, bytes);

					return BitmapFactory.decodeByteArray(decrypted, 0,
							decrypted.length);
				}
			}else{
//				return BitmapFactory.decodeFile(filePath);
				File file = new File(filePath);
				if (file.exists() && file.isFile()) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeStream((new FileInputStream(file.getPath())), null, options);
					int imageHeight = options.outHeight / 2;
					int imageWidth = options.outWidth / 2;
					Log.i("MemoryCheck", "imageHeight : " + imageHeight + " imageWidth : " + imageWidth);
					options.inSampleSize = calculateInSampleSize(options, imageWidth / 2, imageHeight / 2);

					options.inJustDecodeBounds = false;
					return BitmapFactory.decodeStream(new FileInputStream(file), null, options);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// may file not encrypt so we try this
			try {
				return BitmapFactory.decodeFile(filePath);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return null;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static String decryptFile(Context context,String filePath) {
		try {
			if(flag) {
				File file = new File(filePath);
				if (file.exists()) {
					FileInputStream fileInputStream = new FileInputStream(file);
					byte[] bytes = new byte[(int) file.length()];
					fileInputStream.read(bytes);
					fileInputStream.close();

					byte[] decrypted = AESCrypto.decrypt(ProprietarySignalling.seceretKey, bytes);

					String path = context.getCacheDir().getAbsolutePath() + "/tmpcache";
					File newFile = new File(path);
					if (newFile.exists() == false)
						newFile.mkdir();

					path = newFile.getAbsolutePath() + "/" + file.getName();

					FileOutputStream fileOuputStream = new FileOutputStream(path);
					fileOuputStream.write(decrypted);
					fileOuputStream.close();
					return path;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}

}
