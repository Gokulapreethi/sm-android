package com.cg.commonclass;


import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.util.SingleInstance;

/**
 * Created by Rajalakshmi gurunath on 08-06-2016.
 */
public class Touch implements View.OnTouchListener {

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    private static final float MIN_ZOOM = 1f;
    private static final float MAX_ZOOM = 5f;

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    private PointF start = new PointF();
    private PointF mid = new PointF();

    private int mode = NONE;
    private float oldDistance = 1f;
    private float dx; // postTranslate X distance
    private float dy; // postTranslate Y distance
    private float[] matrixValues = new float[9];
    float matrixX = 0; // X coordinate of matrix inside the ImageView
    float matrixY = 0; // Y coordinate of matrix inside the ImageView
    float width = 0; // width of drawable
    float height = 0;

    private int xDelta;
    private int yDelta;
    private int zDelta;

    public boolean onTouch(View view, MotionEvent event) {
//        ImageView imageView = (ImageView) view;
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        SingleInstance.mainContext.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int sH = displaymetrics.heightPixels;
        int sW = displaymetrics.widthPixels;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

//            case MotionEvent.ACTION_DOWN:
//                savedMatrix.set(matrix);
//                start.set(event.getX(), event.getY());
//                mode = DRAG;
//                break;
//
//            case MotionEvent.ACTION_POINTER_DOWN:
//                oldDistance = spacing(event);
//                if (oldDistance > 10f) {
//                    savedMatrix.set(matrix);
//                    midPoint(mid, event);
//                    mode = ZOOM;
//                }
//                break;
//
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//                mode = NONE;
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                if (mode == DRAG) {
//                    matrix.set(savedMatrix);
//
//                    matrix.getValues(matrixValues);
//                    matrixX = matrixValues[2];
//                    matrixY = matrixValues[5];
//                    width = matrixValues[0] * (((ImageView) view).getDrawable()
//                            .getIntrinsicWidth());
//                    height = matrixValues[4] * (((ImageView) view).getDrawable()
//                            .getIntrinsicHeight());
//
//                    dx = event.getX() - start.x;
//                    dy = event.getY() - start.y;
//
//                    //if image will go outside left bound
//                    if (matrixX + dx < 0){
//                        dx = -matrixX;
//                    }
//                    //if image will go outside right bound
//                    if(matrixX + dx + width > view.getWidth()){
//                        dx = view.getWidth() - matrixX - width;
//                    }
//                    //if image will go oustside top bound
//                    if (matrixY + dy < 0){
//                        dy = -matrixY;
//                    }
//                    //if image will go outside bottom bound
//                    if(matrixY + dy + height > view.getHeight()){
//                        dy = view.getHeight() - matrixY - height;
//                    }
//                    matrix.postTranslate(0, dy);
//                }
//                break;
//        }
//        imageView.setImageMatrix(matrix);
//        return true;

            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams Params1 =
                        (RelativeLayout.LayoutParams) view.getLayoutParams();
                xDelta = X - Params1.leftMargin;
                yDelta = Y - Params1.topMargin;
                zDelta = Y + Params1.bottomMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:

                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams) view.getLayoutParams();
//                layoutParams.leftMargin = X - xDelta;
                layoutParams.topMargin = Y - yDelta;
//                layoutParams.rightMargin = X + xDelta;
                layoutParams.bottomMargin = Y + zDelta;

                view.setX(0);
                view.setY(Y);

                if(view.getY() > sW || view.getY() <= 0 ){
                    view.setY(sW-20);
                    break;
                }
                break;
        }
        return false;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        point.set((event.getX(0) + event.getX(1)) / 2, (event.getY(0) + event.getY(1)) / 2);
    }
}

