package com.example.epsi1;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;


import com.ekn.gruzer.gaugelibrary.FullGauge;

public class CustomArcGauge extends FullGauge {


    private float sweepAngle = 180;
    private float startAngle = 180;
    private float gaugeBGWidth = 20f;

    public CustomArcGauge(Context context) {
        super(context);
        init();
    }

    public CustomArcGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomArcGauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomArcGauge(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        getGaugeBackGround().setStrokeWidth(gaugeBGWidth);
        getGaugeBackGround().setStrokeCap(Paint.Cap.ROUND);
        getGaugeBackGround().setColor(Color.parseColor("#ffffff"));
        getTextPaint().setTextSize(70f);
        setPadding(40f);
        setSweepAngle(sweepAngle);
        setStartAngle(startAngle);
    }

    protected void drawValuePoint(Canvas canvas) {
        //no point
    }
}
