package com.cg.commongui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class LineProgressBar extends ProgressView {

    protected int lineOrientation = 0;  //0-horizontal,1-verticals
    private boolean isGradientColor;
    // private boolean isEdgeRounded;

    public LineProgressBar(Context context) {
        super(context);
    }

    public LineProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public LineProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    void init() {

        initForegroundColor();
        initBackgroundColor();

    }

    // public void setRoundedEdgeProgress(boolean isEdgeRounded) {
    // this.isEdgeRounded = isEdgeRounded;
    // init();
    // }

    @Override
    public void onDraw(Canvas canvas) {

        if (getLineOrientation() ==0) {
            drawLineProgress(canvas);
            if (isGradientColor)
                setGradientColorHorizontal(gradColors);

        }
        drawText(canvas);

    }

    private void drawLineProgressVertical(Canvas canvas) {
        int nMiddle = width / 2;
        float progressY = (height / maximum_progress) * maximum_progress;

        canvas.drawLine(nMiddle, height - progressY, nMiddle, height, backgroundPaint);

        float progressX = (height / maximum_progress) * progress;
        canvas.drawLine(nMiddle, height, nMiddle, height - progressX,
                foregroundPaint);

    }


    private void drawLineProgress(Canvas canvas) {
        int nMiddle = height / 2;
        canvas.drawLine(0, nMiddle, width, nMiddle, backgroundPaint);

        int progressX = (int) (width * progress / maximum_progress);

        canvas.drawLine(0, nMiddle, progressX, nMiddle, foregroundPaint);

    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        setMeasuredDimension(width, height);

    }

    /****
     * @return int value for orientation. <li>
     * <i>HORIZONTAL=0   <li> VERTICAL=1</i></li>
     ***/
    public int getLineOrientation() {
        return lineOrientation;
    }



    @Override
    public ShapeType setType(ShapeType type) {
        return ShapeType.LINE;
    }


    public void setLinearGradientProgress(boolean isGradientColor) {
        this.isGradientColor = isGradientColor;
    }

    public void setLinearGradientProgress(boolean isGradientColor, int[] colors) {
        this.isGradientColor = isGradientColor;
        gradColors = colors;
    }



    private void setGradientColorHorizontal(int[] gradColors) {
        if (gradColors != null)
            colorHelper.setGradientPaint(foregroundPaint, 0, 0, width, 0, gradColors);
        else
            colorHelper.setGradientPaint(foregroundPaint, 0, 0, width, 0);
    }
}
