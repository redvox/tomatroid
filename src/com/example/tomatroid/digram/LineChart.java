package com.example.tomatroid.digram;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;

public class LineChart extends View {

	float height = 1;
	float width = 1;
	float bar_width = 1;
	float pixelPerMinute = 0;
	float maxValue = 0;

	// final int x = 0;
	// final int y = 1;

	int[][] barValues;
	// int[][][] barValueCoords;
	Paint[] lineColors;
	Paint strokeColor;
	Paint textColor;
	Axis axisView;

	String[] lineNames;
	String[] axisLabels;

	Paint paint = new Paint();
	RectF rect;

	public LineChart(Context context, int[][] barValues, String[] axisLabels,
			String[] lineNames) {
		super(context);
		this.barValues = barValues;
		this.axisLabels = axisLabels;
		this.lineNames = lineNames;

		rect = new RectF(0, 0, 20, 20);
		lineColors = new Paint[3];
		lineColors[0] = new Paint();
		lineColors[0].setColor(Color.GREEN);
		lineColors[0].setStrokeWidth(5);
		lineColors[1] = new Paint();
		lineColors[1].setColor(Color.BLUE);
		lineColors[1].setStrokeWidth(5);
		lineColors[2] = new Paint();
		lineColors[2].setColor(Color.RED);
		lineColors[2].setStrokeWidth(5);

		strokeColor = new Paint();
		strokeColor.setStyle(Style.STROKE);
		strokeColor.setColor(Color.WHITE);
		strokeColor.setStrokeWidth(5);
		strokeColor.setTextSize(25);
//		strokeColor.setTextAlign(Align.CENTER);

		textColor = new Paint();
		textColor.setColor(Color.BLACK);
//		textColor.setTextAlign(Align.CENTER);
		textColor.setTextSize(25);

		for (int i = 0; i < barValues.length; i++) {
			for (int k = 0; k < barValues[i].length; k++) {
				if (barValues[i][k] > maxValue)
					maxValue = barValues[i][k];
			}
		}
		// maxValue = (maxValue / 100f) * 105f;

		axisView = new Axis(context, maxValue);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		axisView.onDraw(canvas);
		// Lines
		float x = bar_width / 2;

		for (int i = 0; i < barValues.length; i++) {
			for (int k = 0; k < barValues[i].length - 1; k++) {
				float barPixelHeight1 = barValues[i][k] * pixelPerMinute;
				float barPixelHeight2 = barValues[i][k + 1] * pixelPerMinute;

				canvas.drawLine(x, height - barPixelHeight1, x + bar_width,
						height - barPixelHeight2, lineColors[i]);
				x += bar_width;
			}
			x = bar_width / 2;
		}

		// Boxes aka Points
		for (int i = 0; i < barValues.length; i++) {
			for (int k = 0; k < barValues[i].length; k++) {
				float barPixelHeight = barValues[i][k] * pixelPerMinute;

				canvas.drawRect(x - 10, height - barPixelHeight - 10, x + 10,
						height - barPixelHeight + 10, lineColors[i]);
				canvas.drawRect(x - 10, height - barPixelHeight - 10, x + 10,
						height - barPixelHeight + 10, strokeColor);

				if (k == 0) {
					canvas.drawText(lineNames[i], x, height - barPixelHeight
							+ 20, strokeColor);
					canvas.drawText(lineNames[i], x, height - barPixelHeight
							+ 20, textColor);
				}
				x += bar_width;
			}
			x = bar_width / 2;
		}

		x = bar_width / 2;
		// Axis Lables
		for (int i = 0; i < axisLabels.length; i++) {
			canvas.drawText(axisLabels[i], x, height + 20, textColor);
			x += bar_width;
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		h = h - 75;
		super.onSizeChanged(w, h, oldw, oldh);
		axisView.onSizeChanged(w, h, oldw, oldh);

		Log.e("MultiBar", "onSizeChanged");

		// Account for padding
		float xpad = (float) (getPaddingLeft() + getPaddingRight());
		float ypad = (float) (getPaddingTop() + getPaddingBottom());

		width = (float) w - xpad;
		height = (float) h - ypad;

		pixelPerMinute = height / maxValue;
		Log.e("LineChart", "pixelPerMinute: " + pixelPerMinute);
		bar_width = width / barValues[0].length;
		Log.e("MultiBar", "pixelPerMinute: " + pixelPerMinute + " maxVal: "
				+ maxValue);
		// barPixelHeight = value * pointPerMinute;
	}
}
