/**
 *
 */
package com.image.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.widget.ImageView;

import com.crypto.AESFileCrypto;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author GK
 */
public class ImageViewer {

    Context context;

    MemoryCache memoryCache = new MemoryCache();

    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;

    Handler handler = new Handler();

    public ImageViewer(Context context) {
        this.context = context;
        executorService = Executors.newFixedThreadPool(5);
    }

    public boolean addImage(String fileName)
    {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(fileName);
            bitmap = getResizedBitmap(bitmap, 200, 200);
            memoryCache.put(fileName,bitmap);
            return  true;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return  false;
    }

    public void display(String fileName, ImageView imageView,
                        int resource_id) {

        imageViews.put(imageView, fileName);
        Bitmap bitmap = memoryCache.get(fileName);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            if(!fileName.endsWith("COMMedia/"))
            queuePhoto(fileName, imageView, resource_id);
            imageView.setImageResource(resource_id);
        }
    }

    private void queuePhoto(String fileName, ImageView imageView, int resourceId) {
        PhotoToLoad p = new PhotoToLoad(fileName, imageView, resourceId);
        executorService.submit(new PhotosLoader(p));
    }

    private class PhotoToLoad {
        public String filename;
        public ImageView imageView;
        public int resourceId;

        public PhotoToLoad(String u, ImageView i, int rId) {
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
                if (imageViewReused(photoToLoad))
                    return;

                Bitmap bmp = getBitmap(photoToLoad.filename);
                memoryCache.put(photoToLoad.filename, bmp);
                if (imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private Bitmap getBitmap(String filename) {

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
            Bitmap bitmap = AESFileCrypto.decryptBitmap(f.getAbsolutePath());
            bitmap = getResizedBitmap(bitmap, 200, 200);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        try {
            if(bm != null) {
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
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {

        String tag = imageViews.get(photoToLoad.imageView);
        // Check filename is already exist in imageViews MAP
        if (tag == null || !tag.equals(photoToLoad.filename))
            return true;
        return false;
    }

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
            if (bitmap != null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(photoToLoad.resourceId);
        }
    }

    public void clearCache() {
        memoryCache.clear();
    }

}
