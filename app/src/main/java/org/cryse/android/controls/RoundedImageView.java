package org.cryse.android.controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cg.snazmed.R;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class RoundedImageView extends ImageView {
	private int mBorderThickness = 0;
	private int mBorderColor = 0xFFFFFFFF;

	public RoundedImageView(Context context) {
		super(context);

	}

	public RoundedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setCustomAttributes(attrs);
	}

	public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setCustomAttributes(attrs);
	}

	private void setCustomAttributes(AttributeSet attrs) {

	}

	@Override
	protected void onDraw(Canvas canvas) {

		try {
			Drawable drawable = getDrawable();

			if (drawable == null) {
				return;
			}

			if (getWidth() == 0 || getHeight() == 0) {
				return;
			}
			this.measure(0, 0);
			if (drawable.getClass() == NinePatchDrawable.class)
				return;
			Bitmap b = ((BitmapDrawable) drawable).getBitmap();
			try {
				Bitmap bitmap = null;
				if (b != null) {
//					bitmap = bitmap_copy(b);
					bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
				}


			int w = getWidth(), h = getHeight();
			if (bitmap == null)
				bitmap = BitmapFactory.decodeResource(null,
						R.drawable.icon_buddy_aoffline);

			int radius = (w < h ? w : h) / 2 - mBorderThickness;
			Bitmap roundBitmap = getCroppedBitmap(bitmap, radius);
			// roundBitmap=ImageUtils.setCircularInnerGlow(roundBitmap,
			// 0xFFBAB399,
			// 4, 1);
			// canvas.drawBitmap(roundBitmap, w / 2 - radius, 8, null);

			final Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			paint.setDither(true);
			paint.setColor(mBorderColor);
			canvas.drawCircle(w / 2, h / 2, radius + mBorderThickness, paint);
			canvas.drawBitmap(roundBitmap, w / 2 - radius, h / 2 - radius, null);
			}catch (OutOfMemoryError error){
				error.printStackTrace();
			}catch (Exception e){
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Bitmap bitmap_copy(Bitmap bitmap){
		try {
//this is the file going to use temporally to save the bytes.

			File file = new File(Environment.getExternalStorageDirectory()
					+ "/COMMedia/temp.txt");
			if(file.exists()) {
				file.delete();
			}
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

//Open an RandomAccessFile
/*Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
into AndroidManifest.xml file*/
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

// get the width and height of the source bitmap.
			int width = getWidth();
			int height = getHeight();

//Copy the byte to the file
//Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
			FileChannel channel = randomAccessFile.getChannel();
			MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, width*height*4);
			map.rewind();
			bitmap.copyPixelsToBuffer(map);
//recycle the source bitmap, this will be no longer used.
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
//Create a new bitmap to load the bitmap again.
			bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			map.position(0);
//load it back from temporary
			bitmap.copyPixelsFromBuffer(map);
//close the temporary file and channel , then delete that also
			channel.close();
			randomAccessFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
		try {
			Bitmap scaledSrcBmp;
			int diameter = radius * 2;
			if (bmp.getWidth() != diameter || bmp.getHeight() != diameter)
				scaledSrcBmp = Bitmap.createScaledBitmap(bmp, diameter,
						diameter, false);
			else
				scaledSrcBmp = bmp;
			Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
					scaledSrcBmp.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
					scaledSrcBmp.getHeight());

			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			paint.setDither(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.parseColor("#BAB399"));
			canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
					scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
					paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);

			return output;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
