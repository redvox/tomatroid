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

	// final int x = 0;
	// final int y = 1;

	int[] barValues;
	// int[][][] barValueCoords;

	Paint paint = new Paint();
	Paint stroke = new Paint();
	RectF rect;

	public BarChart(Context context, int[] barValues) {
		super(context);
		setValues(barValues);

		paint.setColor(Color.DKGRAY);
		stroke.setStyle(Style.STROKE);
		stroke.setColor(Color.WHITE);
		stroke.setStrokeWidth(5);
	}

	// constructor
	public BarChart(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		paint.setColor(Color.DKGRAY);
		stroke.setStyle(Style.STROKE);
		stroke.setColor(Color.WHITE);
		stroke.setStrokeWidth(5);
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
//		Log.e("BarChart", "pixelPerMinute: " + pixelPerMinute + " maxVal: "
//				+ maxValue);
		// barPixelHeight = value * pointPerMinute;
	}

	public void setValues(int[] barValues) {
		this.barValues = barValues;
		for (int i = 0; i < barValues.length; i++) {
			if (barValues[i] > maxValue)
				maxValue = barValues[i];
		}
		maxValue = (maxValue / 100f) * 120f;
	}
}
