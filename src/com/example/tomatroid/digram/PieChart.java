package com.example.tomatroid.digram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.view.View;

public class PieChart extends View {

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintBlackStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
	private float[] value_degree;
	private int[] COLORS = { Color.BLUE, Color.GREEN, Color.GRAY, Color.CYAN,
			Color.RED, Color.YELLOW };

	RectF rectf = new RectF(10, 10, 200, 200);
	float width = 0;
	float height = 0;
	
	
	public PieChart(Context context, float[] values) {
		super(context);

		paintBlackStroke.setStyle(Style.STROKE);
		paintBlackStroke.setColor(Color.BLACK);
		paintBlackStroke.setStrokeWidth(5);

		float total = 0;
		value_degree = new float[values.length];
		for (int i = 0; i < values.length; i++) {
			total += values[i];
		}
		
		for (int i = 0; i < values.length; i++) {
			value_degree[i] = 360*(values[i]/total);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int temp = 0;
		for (int i = 0; i < value_degree.length; i++) {// values2.length; i++) {
			if (i != 0)
				temp += (int) value_degree[i - 1];
			paint.setColor(COLORS[i]);
			canvas.drawArc(rectf, temp, value_degree[i], true, paint);
			canvas.drawArc(rectf, temp, value_degree[i], true, paintBlackStroke);
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
		
		if(width > height){
			width = height;
		} else {
			height = width;
		}

		rectf = new RectF(0, 0, width, height);
	}

}
