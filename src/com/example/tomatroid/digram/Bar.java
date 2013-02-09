package com.example.tomatroid.digram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

public class Bar extends View {

	Context context;
	int value = 0;
//	float maxW = 0;
//	float maxH = 0;
	float width = 0;
	float height = 0;

	int minMax = 60;
	float pointPerMinute = 0;
	
	Paint color;
	RectF bar;

	public Bar(Context context, int value) {
		super(context);
		this.context = context;
		this.value = value;
		
		color = new Paint();
		color.setColor(Color.GRAY);
//		color.setShadowLayer(2f, 1f, 1f, Color.BLACK);
//		color.setShadowLayer(10.0f, 0.0f, 2.0f, 0xFF000000);
		
		bar = new RectF();
	}
	
	public void setValue(int newValue){
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(0, height - (value*pointPerMinute), width, height, color);
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// Account for padding
		float xpad = (float) (getPaddingLeft() + getPaddingRight());
		float ypad = (float) (getPaddingTop() + getPaddingBottom());

		// Account for the label
//		if (mShowText)
//			xpad += mTextWidth;

		width = (float) w - xpad;
		height = (float) h - ypad;
		
		pointPerMinute = height / minMax;

//		Log.e("Bar", "ppM " + pointPerMinute);
//		Log.e("Bar", "h" + height);
		
		// Figure out how big we can make the pie.
//		float diameter = Math.min(ww, hh);
	}
}
