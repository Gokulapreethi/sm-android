package com.cg.commongui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DrawingView extends View {

	// drawing path
	private Path drawPath;
	// drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	// initial color
	private int paintColor = 0xFF660000,color=0xFF660000;
	// canvas
	private Canvas drawCanvas;
	// canvas bitmap
	private Bitmap canvasBitmap;
	// brush sizes
	private float brushSize, lastBrushSize;
	// erase flag
	private boolean erase = false,fromErase=false;

	int width;
	int height;

	private Paint clearPaint = new Paint();
	private ArrayList<Path> paths = new ArrayList<Path>();
	private ArrayList<Path> undonePaths = new ArrayList<Path>();
	private Map<Path, Integer> colorsMap = new HashMap<Path, Integer>();
	private Map<Path, Integer> brushSizeMap = new HashMap<Path, Integer>();

	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDrawing();

	}

	// setup drawing
	private void setupDrawing() {

		// prepare for drawing and setup paint stroke properties
		brushSize = getResources().getInteger(R.integer.mini_size);
		lastBrushSize = brushSize;
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}

	// size assigned to view
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);

		this.width = w;
		this.height = h;
	}

	// draw the view - will be called after touch event
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		drawPaint.setColor(paintColor);
		canvas.drawPath(drawPath, drawPaint);
		for (Path p : paths) {
			drawPaint.setColor(colorsMap.get(p));
			drawPaint.setStrokeWidth(brushSizeMap.get(p));
			canvas.drawPath(p, drawPaint);
		}

	}

	// register user touches as drawing action

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// touch_start(x, y);
				// invalidate();
				CallDispatcher.handsketch_edit = true;
				undonePaths.clear();
				drawPath.reset();
				drawPath.moveTo(x, y);
				mX = x;
				mY = y;
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				// touch_move(x, y);
				// invalidate();
				CallDispatcher.handsketch_edit = true;
				float dx = Math.abs(x - mX);
				float dy = Math.abs(y - mY);
				if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
					drawPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
					mX = x;
					mY = y;
				}
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				// touch_up();
				// invalidate();
				drawPath.lineTo(mX, mY);
				paths.add(drawPath);
				colorsMap.put(drawPath, paintColor);
				brushSizeMap.put(drawPath, (int) brushSize);
				drawPath = new Path();
				drawPath.reset();
				invalidate();
				break;
		}
		return true;
	}

	// update color
	public void setColor(String newColor) {
		invalidate();
		paintColor = Color.parseColor(newColor);
		color=paintColor;
		drawPaint.setColor(paintColor);
	}

	// set brush size
	public void setBrushSize(float newSize) {
		float pixelAmount = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, newSize, getResources()
						.getDisplayMetrics());
		brushSize = pixelAmount;
		drawPaint.setStrokeWidth(brushSize);
	}

	// get and set last brush size
	public void setLastBrushSize(float lastSize) {
		lastBrushSize = lastSize;
	}

	public float getLastBrushSize() {
		return lastBrushSize;
	}

	// set erase true or false
	public void setErase(boolean isErase) {
		erase = isErase;
		if (erase) {
			fromErase=true;
			drawPaint.setAlpha(0);
			paintColor = Color.WHITE;
			drawPaint.setColor(Color.WHITE);
			drawPaint.setStrokeWidth(brushSize);
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setMaskFilter(null);
//			drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			drawPaint.setAntiAlias(true);
		}else {
			fromErase=false;
			paintColor=color;
			drawPaint.setXfermode(null);
		}
	}

	// start new drawing
	public void startNew() {
		drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
		invalidate();
	}

	public void setImage(Bitmap decodeFile) {
		// TODO Auto-generated method stub

		// drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		startNew();

		// canvasBitmap = Bitmap.createBitmap(decodeFile,0,0,width,height);

		// drawCanvas.setBitmap(decodeFile);
		drawCanvas.drawBitmap(decodeFile, 0, 0, clearPaint);

	}

	private HandSketchActivity2 getHandSketchActivity2() {
		if (WebServiceReferences.contextTable.containsKey("handsketch")) {
			return (HandSketchActivity2) WebServiceReferences.contextTable
					.get("handsketch");
		}
		return null;
	}

	public void onClickUndo () {
		if (paths.size()>0)  {
			Log.i("undo", "Undo ifcondition");
			undonePaths.add(paths.remove(paths.size()-1));
			invalidate();
		} else  {
			Log.i("undo", "Undo elsecondition");
		}
	}

	public void onClickRedo (){
		if (undonePaths.size()>0)  {
			Log.i("undo", "Undo ifcondition");
			paths.add(undonePaths.remove(undonePaths.size()-1));
			invalidate();
		}
		else  {
			Log.i("undo", "Redo elsecondition");
		}
	}

}
