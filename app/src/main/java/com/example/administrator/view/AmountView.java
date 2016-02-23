package com.example.administrator.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by HBL on 2016/2/1.
 */

public class AmountView extends View {
    Paint paint;
    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xff388e3c);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
    }
    public AmountView(Context context) {
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        Paint tmpPaint = new Paint(paint); //小刻度画笔
        tmpPaint.setStrokeWidth(2);
        float y = 180;
        int count = 120; //总刻度数
        for (int i = 0; i < count; i++) {
            canvas.drawLine(0f, y, 0f, y + 45f, tmpPaint);
            canvas.rotate(360 / count, 0f, 0f); //旋转画纸
        }
    }
}
