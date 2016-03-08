package com.example.administrator.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
/**
 * Created by HBL on 2016/3/5.
 */
public class ColumnView extends View {
    Paint paint;
    int count = 0;
    BarAnimation anim;
    int step[] = {5600, 349, 780, 2000, 4521, 3333, 3232};
    int color[] = {0xfff44336, 0xffE91E63, 0xff9C27B0, 0xff673AB7, 0xff3F51B5, 0xff2196F3, 0xff03a9f4};
    private BlurMaskFilter mBlur = null;

    public ColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        anim = new BarAnimation();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        mBlur = new BlurMaskFilter(20, BlurMaskFilter.Blur.OUTER);
        paint.setMaskFilter(mBlur);
    }

    public void startAnim() {
        anim.setDuration(700);
        anim.setInterpolator(new LinearInterpolator());
        this.startAnimation(anim);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(0xfff44336);//E91E63 9C27B0 673AB7 3F51B5 2196F3
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        float geX = width / 15, geY = 3 * height / 4;
        canvas.translate(geX, geY);
        float leftTopX = 0, leftTopY = 0, rightBtmX = 0, rightBtmY = 0;
        for (int i = 0; i < count; i++) {
            paint.setColor(color[i]);
            paint.setAlpha(150);
            leftTopX = 2 * i * geX;
            leftTopY = -(float) (step[i] / 6000.0) * geY;
            rightBtmX = leftTopX + geX;
            rightBtmY = 0;
            Log.d("ColumnView", leftTopY + "");
            canvas.drawRect(leftTopX, leftTopY, rightBtmX, rightBtmY, paint);
        }
    }

    private class BarAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
           if (interpolatedTime < 1.0f) {
                count = (int) (interpolatedTime * 7);
                postInvalidate();
            } else
                count = 7;
        }
    }
}
