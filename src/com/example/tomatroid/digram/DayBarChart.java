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
		
		barValues = new int[][][]
				{
				   {
				      {4, 0*60, 8*60},

				   },
				   {
					      {4, 0*60, 8*60},
					      {2, 9*60, 10*60},
					      {0, 10*60, 11*60},
					      {0, 13*60, 15*60},
				   },
				   {
					      {4, 0*60, 6*60},
					      {0, 10*60, 11*60},
					      {0, 13*60, 15*60},
					      {3, 15*60, 25*60},
				   },
				   {
					      {4, 0*60, 7*60},
					      {2, 9*60, 10*60},
					      {0, 13*60, 15*60}, 
				   },
				   {
					      {4, 0*60, 10*60},
					      {2, 9*60, 10*60},
					      {0, 10*60, 11*60},
					      {0, 13*60, 15*60}, 
				   },
				   {
					      {4, 0*60, 11*60},
					      {2, 9*60, 10*60},
					      {0, 10*60, 11*60},
				   },
				   {
					      {4, 0*60, 7*60},
					      {2, 9*60, 10*60},
					      {0, 10*60, 11*60},
					      {0, 13*60, 15*60},
				   }
				};
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float x = 30;
		Paint paint;

		for (int i = 0; i < barValues.length; i++) {
			for (int k = 0; k < barValues[i].length; k++) {
				float barStart = ((float) barValues[i][k][1]) * pixelPerMinute;
				float barStop = ((float) barValues[i][k][2]) * pixelPerMinute;
				
				int tag = barValues[i][k][0];
				if (tag == MainActivity.TYPE_POMODORO) {
//					canvas.drawRect(x, barStart, x + bar_width, barStop, pomodoroPaint);
					paint = pomodoroPaint;
				} else if (tag == MainActivity.TYPE_LONGBREAK
						|| tag == MainActivity.TYPE_SHORTBREAK) {
//					canvas.drawRect(x, barStart, x + bar_width, barStop, breakPaint);
					paint = breakPaint;
				} else if (tag == MainActivity.TYPE_TRACKING) {
//					canvas.drawRect(x, barStart, x + bar_width, barStop, trackPaint);
					paint = trackPaint;
				} else {
//					canvas.drawRect(x, barStart, x + bar_width, barStop, sleepPaint);
					paint = sleepPaint;
				}
				
//				canvas.drawRect(x, barStart, x + bar_width, barStop, stroke);
				canvas.drawRect(x, barStart, x + bar_width, barStop, paint);
				canvas.drawRect(x, barStart, x + bar_width, barStop, stroke);
			}
			x += bar_width;
		}
		
//		canvas.drawText("0", textOffset, 10, stroke);
		canvas.drawText("0", textOffset, 15, textPaint);
		int h = 1;
		for (int m = 60; m < maxValue; m+=60) {
//			canvas.drawText(h+"", textOffset, m*pixelPerMinute+5, stroke);
			canvas.drawText(h+"", textOffset, m*pixelPerMinute+5, textPaint);
			h++;
		}
//		canvas.drawText("24", textOffset, height-10, stroke);
		canvas.drawText("24", textOffset, height-5, textPaint);
		
		
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
		bar_width = width / barValues.length;
	}
	
	public int[][][] getValuesFromDatabase(){
		SQHelper sqh = new SQHelper(getContext());
		DateTime dt = DateTime.now();
		Cursor c = sqh.getCursorForDay(dt.getDayOfMonth(), dt.getMonthOfYear(), dt.getYear());
		
		int column_type = c.getColumnIndex(SQHelper.KEY_TYPE);
		int column_startHour = c.getColumnIndex(SQHelper.KEY_DATE_START_HOUR);
		int column_startMinute = c.getColumnIndex(SQHelper.KEY_DATE_START_MINUTE);
		int column_endHour = c.getColumnIndex(SQHelper.KEY_DATE_END_HOUR);
		int column_endMinute = c.getColumnIndex(SQHelper.KEY_DATE_END_MINUTE);
		
		int[][][] barValues = new int[1][c.getCount()][3];
		int i = 0;
		if(c.moveToFirst()){
			do {
				barValues[0][i][0] = c.getInt(column_type);
				barValues[0][i][1] = (c.getInt(column_startHour)*60) + c.getInt(column_startMinute);
				barValues[0][i][2] = (c.getInt(column_endHour)*60) + c.getInt(column_endMinute);
			} while (c.moveToNext());
		}
		return barValues;
	}
}
