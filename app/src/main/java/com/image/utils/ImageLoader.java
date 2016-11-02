package com.image.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.cg.snazmed.R;
import com.crypto.AESFileCrypto;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

	private Bitmap bmp;

	// Initialize MemoryCache
	MemoryCache memoryCache = new MemoryCache();

	// FileCache fileCache;

	// Create Map (collection) to store image and image url in key value pair
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;

	// handler to display images in UI thread
	Handler handler = new Handler();

	public ImageLoader(Context context) {

		// fileCache = new FileCache(context);

		// Creates a thread pool that reuses a fixed number of
		// threads operating off a shared unbounded queue.
		executorService = Executors.newFixedThreadPool(5);

	}

	// default image show in list (Before online image download)
	final int stub_id = R.drawable.refresh;

	public void DisplayImage(String fileName, ImageView imageView,
			int resource_id) {
		// Store image and fileName in Map
		imageViews.put(imageView, fileName);

		// Check image is stored in MemoryCache Map or not (see
		// MemoryCache.java)
		Bitmap bitmap = memoryCache.get(fileName);
//		Bitmap bitmap = AESFileCrypto.decryptBitmap(fileName);

		if (bitmap != null) {
			// if image is stored in MemoryCache Map then
			// Show image in listview row
			imageView.setImageBitmap(bitmap);
		} else {
			// queue Photo to download from fileName
			queuePhoto(fileName, imageView,resource_id);

			// Before downloading image show default image
			imageView.setImageResource(resource_id);
		}
	}

	private void queuePhoto(String fileName, ImageView imageView,int resourceId) {
		// Store image and filename in PhotoToLoad object
		PhotoToLoad p = new PhotoToLoad(fileName, imageView,resourceId);

		// pass PhotoToLoad object to PhotosLoader runnable class
		// and submit PhotosLoader runnable to executers to run runnable
		// Submits a PhotosLoader runnable task for execution

		executorService.submit(new PhotosLoader(p));
	}

	// Task for the queue
	private class PhotoToLoad {
		public String filename;
		public ImageView imageView;
		public int resourceId;

		public PhotoToLoad(String u, ImageView i,int rId) {
			filename = u;
			imageView = i;
			resourceId = rId;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {
				// Check if image already loaded
				if (imageViewReused(photoToLoad))
					return;

				Log.d("IMAGE_LOAD", "FileName : " + photoToLoad.filename);
				// download image from filename
//				Bitmap bmp = getBitmap(photoToLoad.filename);
                try {
                    bmp = AESFileCrypto.decryptBitmap(photoToLoad.filename);
                }catch(Exception e){
                    bmp = getBitmap(photoToLoad.filename);
                }

				// set image data in Memory Cache
				memoryCache.put(photoToLoad.filename, bmp);

				if (imageViewReused(photoToLoad))
					return;

				// Get bitmap to display
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);

				// Causes the Runnable bd (BitmapDisplayer) to be added to the
				// message queue.
				// The runnable will be run on the thread to which this handler
				// is attached.
				// BitmapDisplayer run method will call
				handler.post(bd);

			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	private Bitmap getBitmap(String filename) {
		// File f=fileCache.getFile(url);

		File f = new File(filename);

		try {
			Bitmap bitmap = decodeFile(f);
			if (bitmap != null)
				return bitmap;

			return bitmap;

		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

	// Decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {

		try {
			/*
			 * //Decode image size BitmapFactory.Options o = new
			 * BitmapFactory.Options(); o.inJustDecodeBounds = true;
			 * FileInputStream stream1=new FileInputStream(f);
			 * BitmapFactory.decodeStream(stream1,null,o); stream1.close();
			 * 
			 * //Find the correct scale value. It should be the power of 2.
			 * 
			 * // Set width/height of recreated image final int
			 * REQUIRED_SIZE=200;
			 * 
			 * int width_tmp=o.outWidth, height_tmp=o.outHeight; int scale=1;
			 * while(true){ if(width_tmp/2 < REQUIRED_SIZE || height_tmp/2 <
			 * REQUIRED_SIZE) break; width_tmp/=2; height_tmp/=2; scale*=2; }
			 * 
			 * //decode with current scale values BitmapFactory.Options o2 = new
			 * BitmapFactory.Options(); o2.inSampleSize=scale;
			 * 
			 * 
			 * FileInputStream stream2=new FileInputStream(f); Bitmap
			 * bitmap=BitmapFactory.decodeStream(stream2, null, o2);
			 * stream2.close();
			 */

			
			Bitmap bitmap = ImageUtils.decodeScaledBitmapFromSdCard(f.getAbsolutePath(),200,200);
//			if (bitmap != null)
//				bitmap = getResizedBitmap(bitmap, 200, 200);
//			else
//				Log.d("IMAGE_LOAD","Bitmap :"+bitmap);
			return bitmap;

		} catch (Exception e) {
			Log.d("IMAGE_LOAD", "Error Bitmap : " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);
		// RECREATE THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {

		String tag = imageViews.get(photoToLoad.imageView);
		// Check filename is already exist in imageViews MAP
		if (tag == null || !tag.equals(photoToLoad.filename))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;
		

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
			
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;

			try {
				Log.d("IMAGE_LOAD", "Image Loader Bitmap : " + bitmap);
				// Show bitmap on UI
				if (bitmap != null)
                    photoToLoad.imageView.setImageBitmap(bitmap);
                else
                    photoToLoad.imageView.setImageResource(photoToLoad.resourceId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void clearCache() {
		// Clear cache directory downloaded images and stored data in maps
		memoryCache.clear();
//		fileCache.clear();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {

			byte[] bytes = new byte[buffer_size];
			for (;;) {
				// Read byte from input stream

				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;

				// Write byte from output stream
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

}
