package com.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.cg.snazmed.R;

/**
 * @author Narayanan
 */

public class PieChart extends View {

    Paint free;
    Paint alocated;
    Paint bgpaint;
    RectF rect;
    float percentage = 0;

    public PieChart (Context context) {
        super(context);
        init();
    }
    public PieChart (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public PieChart (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
        free = new Paint();
        free.setColor(getContext().getResources().getColor(R.color.yellow));
        free.setAntiAlias(true);
        free.setStyle(Paint.Style.FILL);
        alocated = new Paint();
        alocated.setColor(getContext().getResources().getColor(R.color.pink));
        alocated.setAntiAlias(true);
        alocated.setStyle(Paint.Style.FILL);
        bgpaint = new Paint();
        bgpaint.setColor(getContext().getResources().getColor(R.color.black2));
        bgpaint.setAntiAlias(true);
        bgpaint.setStyle(Paint.Style.FILL);
        rect = new RectF();
        this.percentage=100;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int left = 0;
        int width = getWidth();
        int top = 0;
        rect.set(left+15, top+15, left+width-15, top + width-15);
        canvas.drawArc(rect, -90, 360, true, alocated);
        rect.set(left, top, left + width, top + width);
        if(percentage!=0) {
            canvas.drawArc(rect, -90, (360*percentage)*-1, true, free);
        }
        rect.set(left + 20, top + 20, left + width - 20, top + width - 20);
        canvas.drawArc(rect, -90, 360, true, bgpaint);
    }
    public void setPercentage(float percentage) {
        this.percentage = percentage / 100;
        invalidate();
    }
}