package com.example.tomatroid.digram;

import com.example.tomatroid.util.StoredAnimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

public class Bar extends View {

	Context context;
	int value = 0;
	String valueText = "";
	float width = 0;
	float height = 0;

	int minMax = 60;
	float pointPerMinute = 0;
	
	Paint color;
	Paint text;
	Paint white;
//	RectF bar;

	public Bar(Context context, int value) {
		super(context);
		this.context = context;
		this.value = value;
		
		color = new Paint();
		color.setColor(Color.argb(255, 91, 172, 38));
		text = new Paint();
		text.setTextSize(30);
		
		white = new Paint();
		white.setAntiAlias(true);
		white.setColor(Color.WHITE);
		white.setStyle(Style.STROKE);
		white.setStrokeWidth(10);
		white.setAlpha(75);
	}
	
	public void setValue(int newValue){
		this.value = newValue;
		valueText = ""+value;
	}
	
	public void addValue(int increment){
		float pixel = increment * pointPerMinute;
		value += increment;
		valueText = "";
		this.startAnimation(StoredAnimation.slideVertical(pixel));
		valueText = ""+value;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(0, height - (value*pointPerMinute), width, height, color);
		canvas.drawRect(2, height - (value*pointPerMinute)+2, width-2, height+10, white);
//		canvas.drawText(valueText, 0, height-20, text);
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
