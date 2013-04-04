package com.example.tomatroid.digram;

import org.joda.time.DateTime;

import com.example.tomatroid.MainActivity;
import com.example.tomatroid.sql.SQHelper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.provider.SyncStateContract.Helpers;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DayBarChart extends View {

	float height = 1;
	float width = 1;
	float bar_width = 1;
	float pixelPerMinute = 0;
	float maxValue = 1440;
	float textOffset = 25;
	
	int[][][] barValues;
	int now;

	Paint pomodoroPaint;
	Paint breakPaint;
	Paint trackPaint;
	Paint sleepPaint;
	Paint nowPaint;
	Paint textPaint;
	Paint stroke;
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
//		nowPaint.setStrokeWidth(10);
		
		stroke = new Paint();
		stroke.setStyle(Style.STROKE);
		stroke.setColor(Color.WHITE);
		stroke.setStrokeWidth(2);
		stroke.setTextSize(20);
		
		textPaint = new Paint();
		textPaint.setTextAlign(Align.RIGHT);
		textPaint.setTextSize(20);

		now = DateTime.now().getMinuteOfDay();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float x = 30;
		Paint paint;

		for (int i = 0; i < barValues.length; i++) {
			Log.e("DayBarChart", " --"+i+" "+barValues.length);
			for (int k = 0; k < barValues[i].length; k++) {
				float barStart = ((float) barValues[i][k][1]) * pixelPerMinute;
				float barStop = ((float) barValues[i][k][2]) * pixelPerMinute;
				
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
				
//				canvas.drawRect(x, barStart, x + bar_width, barStop, stroke);
				canvas.drawRect(x, barStart, x + bar_width, barStop, paint);
				canvas.drawRect(x, barStart, x + bar_width, barStop, stroke);
			}
			x += bar_width;
		}
		
//		canvas.drawText("0", textOffset, 10, stroke);
		canvas.drawText("0", textOffset, 16, textPaint);
		int h = 1;
		for (int m = 60; m < maxValue; m+=60) {
//			canvas.drawText(h+"", textOffset, m*pixelPerMinute+5, stroke);
			canvas.drawText(h+"", textOffset, m*pixelPerMinute+5, textPaint);
			h++;
		}
//		canvas.drawText("24", textOffset, height-10, stroke);
		canvas.drawText("24", textOffset, height-4, textPaint);
		
		
		canvas.drawLine(0, now * pixelPerMinute, width, now * pixelPerMinute, nowPaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		float xpad = (float) (getPaddingLeft() + getPaddingRight());
		float ypad = (float) (getPaddingTop() + getPaddingBottom());

		width = (float) w - xpad;
		height = (float) h - ypad;

		pixelPerMinute = height / maxValue;
		bar_width = (width-textOffset) / barValues.length;
	}
	
	public void setValues(int[][][] values){
		this.barValues = values;
	}
}
