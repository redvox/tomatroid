package com.example.tomatroid.digram;

import java.util.ArrayList;

import org.joda.time.Interval;

import com.example.tomatroid.util.StoredAnimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

public class Axis extends View {

	Context context;
	float width = 0;
	float height = 0;

	float maxValue = 60;
	float pointPerMinute = 0;

	Paint color;
	Paint text;
	Paint white;

	ArrayList<Float> position = new ArrayList<Float>();
	ArrayList<String> values = new ArrayList<String>();
	String maxText = "60 ";
	
	public Axis(Context context, int value) {
		super(context);
		this.context = context;
		// this.value = value;

		color = new Paint();
		color.setStrokeWidth(2);
		color.setAlpha(100);
		
		text = new Paint();
		text.setTextSize(20);
		
//		white = new Paint();
//		white.setAntiAlias(true);
//		white.setColor(Color.WHITE);
//		white.setStyle(Style.STROKE);
//		white.setStrokeWidth(5);
//		white.setAlpha(75);
	}

	// public void setValue(int newValue){
	// this.value = newValue;
	// }
	//
	// public void addValue(int increment){
	// float pixel = increment * pointPerMinute;
	// value += increment;
	// this.startAnimation(StoredAnimation.slideVertical(pixel));
	// }

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawLine(0, 0, 0, height, color);

		for(int i=0; i<position.size(); i++){
			canvas.drawLine(0, position.get(i), width, position.get(i), color);
			canvas.drawText(values.get(i), 5, position.get(i)-5, text);
		}
		canvas.drawLine(0, 1, width, 1, color);
		canvas.drawText(maxText, 5, 20, text);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		float xpad = (float) (getPaddingLeft() + getPaddingRight());
		float ypad = (float) (getPaddingTop() + getPaddingBottom());

		width = (float) w - xpad;
		height = (float) h - ypad;

		pointPerMinute = height / maxValue;
		
		
		
		int tenth = (int) (maxValue / 10);
		for(float i=0; i<10; i++){
			position.add(height - (i*(tenth*pointPerMinute)));
			values.add(""+(int)(i*tenth));
		}
		
		Log.e("Axis", "max: "+maxValue);
		Log.e("Axis", "tenth: "+tenth);
	}
}
