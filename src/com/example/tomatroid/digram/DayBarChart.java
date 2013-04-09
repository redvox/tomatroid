package com.example.tomatroid.digram;

import org.joda.time.DateTime;

import com.example.tomatroid.MainActivity;
import com.example.tomatroid.sql.SQHelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DayBarChart extends View implements OnTouchListener {

	// Dynamic
	float height = 1;
	float width = 1;
	float bar_width = 1;
	float bar_width_half = 1;
	float pixelPerMinute = 0;

	// Static
	float maxValue = 1440;
	float textOffset = 25;
	float headLineOffset = 35;
	float axisTextOffset = 30;
	float startX;
	float x;
	
	// Settings
	int daysToShow = 7;
	
	// Input
	String[] columnlables;
	int[][][] barValues;
	
	String[] hourString = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
	float now;

	// Paint
	Paint pomodoroPaint;
	Paint breakPaint;
	Paint trackPaint;
	Paint sleepPaint;
	Paint nowPaint;
	Paint axistextPaint;
	Paint labletextPaint;
	Paint strokeWhite;
	Paint strokeBlack;
	Paint paint;
	Paint whilePaint;
	RectF rect;
	
	public DayBarChart(Context context) {
		super(context);
		initialising();
	}
	
	// inflater constructor
	public DayBarChart(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		initialising();
	}
	
	private void initialising(){
		pomodoroPaint = new Paint();
		pomodoroPaint.setColor(MainActivity.COLOR_POMODORO);
		breakPaint = new Paint();
		breakPaint.setColor(MainActivity.COLOR_BREAK);
		trackPaint = new Paint();
		trackPaint.setColor(MainActivity.COLOR_TRACKING);
		sleepPaint = new Paint();
		sleepPaint.setColor(MainActivity.COLOR_SLEEP);
		nowPaint = new Paint();
		nowPaint.setColor(MainActivity.COLOR_RED);
		nowPaint.setStrokeWidth(3);
		
		strokeWhite = new Paint();
		strokeWhite.setStyle(Style.STROKE);
		strokeWhite.setColor(Color.WHITE);
		strokeWhite.setStrokeWidth(2);
		
		strokeBlack = new Paint();
		strokeBlack.setStyle(Style.STROKE);
		strokeBlack.setColor(Color.BLACK);
		strokeBlack.setStrokeWidth(1);
		
		axistextPaint = new Paint();
		axistextPaint.setTextAlign(Align.RIGHT);
		axistextPaint.setTextSize(20);
		
		labletextPaint = new Paint();
		labletextPaint.setTextAlign(Align.CENTER);
		labletextPaint.setTextSize(20);
		
		whilePaint = new Paint();
		whilePaint.setColor(Color.WHITE);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// Horizontal Raster
		for (int m = 60; m < maxValue; m+=60) {
			canvas.drawLine(axisTextOffset,  headLineOffset + m * pixelPerMinute, width,  headLineOffset+ m * pixelPerMinute, strokeBlack);
		}
		
		// For every Column
		for (int i = 0; i < barValues.length; i++) {
						
			// Column Lables
			canvas.drawRect(x, 0, x+bar_width, headLineOffset, whilePaint);
			Log.e("DayBarChart", ""+columnlables[i]);
			canvas.drawText(columnlables[i], x+bar_width_half, 25, labletextPaint);
			
			// vertical Raster
			canvas.drawLine(x,  headLineOffset, x,  height, strokeBlack);
			
			// Entrys & vertical Raster
			for (int k = 0; k < barValues[i].length; k++) {
				float barStart = headLineOffset+((float) barValues[i][k][1]) * pixelPerMinute;
				float barStop = headLineOffset+((float) barValues[i][k][2]) * pixelPerMinute;
				
				int tag = barValues[i][k][0];
				if (tag == MainActivity.TYPE_POMODORO) {
					paint = pomodoroPaint;
				} else if (tag == MainActivity.TYPE_LONGBREAK
						|| tag == MainActivity.TYPE_SHORTBREAK) {
					paint = breakPaint;
				} else if (tag == MainActivity.TYPE_TRACKING) {
					paint = trackPaint;
				} else {	
					paint = sleepPaint;
				}
				
				canvas.drawRect(x+10, barStart, x + bar_width-10, barStop, paint);
				canvas.drawRect(x+10, barStart, x + bar_width-10, barStop, strokeWhite);
			}
			x += bar_width;
		}// For every Column
		Log.e("DayBarChart", "onDraw bar_width "+bar_width);
		
		// Axis
		canvas.drawRect(0, 0, axisTextOffset, height, whilePaint);
		canvas.drawText(hourString[0], textOffset, headLineOffset+16, axistextPaint);
		canvas.drawLine(axisTextOffset, headLineOffset, width, headLineOffset, strokeBlack);
		int h = 1;
		for (int m = 60; m < maxValue; m+=60) {
			canvas.drawText(hourString[h], textOffset, m*pixelPerMinute+5+headLineOffset, axistextPaint);
			h++;
		}
		canvas.drawText(hourString[24], textOffset, height-4, axistextPaint);
		
		// NOW
		canvas.drawLine(axisTextOffset, now, width, now, nowPaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		float xpad = (float) (getPaddingLeft() + getPaddingRight());
		float ypad = (float) (getPaddingTop() + getPaddingBottom());

		width = (float) w - xpad;
		height = (float) h - ypad;

		pixelPerMinute = (height-headLineOffset) / maxValue;
		bar_width = (width-textOffset) / daysToShow;
		bar_width_half = bar_width/2;
		
		now = DateTime.now().getMinuteOfDay() * pixelPerMinute;
		Log.e("DayBarChart", "onSizeChanged");
		calculateOffset();
	}
	
	public void calculateOffset(){
		
		if(barValues != null){
			int daysOff = barValues.length - daysToShow;
			Log.e("DayBarChart", "daysOff "+daysOff);
			Log.e("DayBarChart", "bar_width "+bar_width);
			Log.e("DayBarChart", "bar_width*daysOff "+bar_width*daysOff);
			if(daysOff > 0)
				startX = (bar_width*daysOff)*-1 + axisTextOffset;
				x = startX;
			Log.e("DayBarChart", "--x-- "+x);
		}
	}
	
	public void setValues(int[][][] values, String[] columnLables){
		this.barValues = values;
		this.columnlables = columnLables;
		Log.e("DayBarChart", "setValues");
		calculateOffset();
	}
	
	float origin_x = 0;
	float move = 0;
//	float origin_y = 0;
	
	public boolean onTouch(View v, MotionEvent event) {
		
//		float distance = (float) Math.sqrt(Math.pow(event.getX() - origin_x, 2.0f)
//				+ Math.pow(event.getY() - origin_y, 2.0f));
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				origin_x = event.getX();
				break;
			}
			case MotionEvent.ACTION_UP: {
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				float distance = event.getX() - origin_x;
				origin_x = event.getX();
				move += distance;
				if(move < 0)
					move = 0;
				if(move > (startX-axisTextOffset)*-1)
					move = (startX-axisTextOffset)*-1;
				x = startX + move;
				invalidate();
				
				break;
			}
		}
		return true;
	}
}
