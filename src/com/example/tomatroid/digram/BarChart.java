package com.example.tomatroid.digram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BarChart extends View {

	float height = 1;
	float width = 1;
	float bar_width = 1;
	float pixelPerMinute = 0;
	float maxValue = 60;
	float average = 0;
	
	int[] barValues;

	Paint paint = new Paint();
	Paint stroke = new Paint();
	Paint avgPaint = new Paint();
	RectF rect;

	public BarChart(Context context, int[] barValues) {
		super(context);
		setValues(barValues);
		preparePaint();
	}

	// inflater constructor
	public BarChart(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		preparePaint();
	}

	private void preparePaint(){
		paint.setColor(Color.DKGRAY);
		stroke.setStyle(Style.STROKE);
		stroke.setColor(Color.WHITE);
		stroke.setStrokeWidth(5);
		avgPaint.setColor(Color.YELLOW);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float x = 0;

		for (int i = 0; i < barValues.length; i++) {
			float barPixelHeight = barValues[i] * pixelPerMinute;

			canvas.drawRect(x, height - barPixelHeight, x + bar_width, height,
					paint);
			canvas.drawRect(x, height - barPixelHeight, x + bar_width, height,
					stroke);

			x += bar_width;
		}
		
		float avgLine = height - (average * pixelPerMinute);
		canvas.drawLine(0, avgLine, width, avgLine, avgPaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// Account for padding
		float xpad = (float) (getPaddingLeft() + getPaddingRight());
		float ypad = (float) (getPaddingTop() + getPaddingBottom());

		width = (float) w - xpad;
		height = (float) h - ypad;

		pixelPerMinute = height / maxValue;
		bar_width = width / barValues.length;
	}

	public void setValues(int[] barValues) {
		this.barValues = barValues;
		int total = 0;
		
		for (int i = 0; i < barValues.length; i++) {
			total += barValues[i];
			if (barValues[i] > maxValue)
				maxValue = barValues[i];
		}
		average = total / barValues.length;
		maxValue = (maxValue / 100f) * 120f;
	}
}
