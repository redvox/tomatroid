package com.example.tomatroid.digram;

import java.util.ArrayList;

import org.joda.time.Interval;

import com.example.tomatroid.util.StoredAnimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

public class Axis extends View {

	Context context;
	float width = 0;
	float height = 0;

	int maxValue;
	float pointPerMinute = 0;

	Paint color;
	Paint text;
	Paint white;

	ArrayList<Float> position = new ArrayList<Float>();
	ArrayList<String> values = new ArrayList<String>();
	String maxText;

	public Axis(Context context, int maxValue) {
		super(context);
		this.context = context;
		this.maxValue = maxValue;

		color = new Paint();
		color.setStrokeWidth(2);
		color.setAlpha(100);

		text = new Paint();
		text.setTextSize(20);
		// text.setTextAlign(Align.LEFT);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawLine(0, 0, 0, height, color);

		for (int i = 0; i < position.size(); i++) {
			canvas.drawLine(0, position.get(i), width, position.get(i), color);
			canvas.drawText(values.get(i), 5, position.get(i) - 5, text);
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

		calculate();
	}

	private void calculate() {
		pointPerMinute = height / maxValue;
		position.clear();
		values.clear();
		
		int tenth = maxValue / 10;
		for (float i = 0; i < 10; i++) {
			position.add(height - (i * (tenth * pointPerMinute)));
			values.add(generateTimeText((int)(i * tenth)));
		}
		
		maxText = generateTimeText(maxValue);
	}

	public void adjustToNewMaximum(int newMax) {
		maxValue = newMax;
//		Animation a = new AlphaAnimation(1, 0);
//		a.setDuration(1000);
		Animation b = new AlphaAnimation(0, 1);
		b.setDuration(1000);
		
		calculate();
		startAnimation(b);
		Log.e("Axis", "max after adjustment: " + maxValue + " == "+ newMax);
	}
	
	protected String generateTimeText(int value) {
		int hours = (int) value / 60;
		int minutes = (int) value % 60;

		StringBuffer stb = new StringBuffer();

		if (hours > 0) {
			stb.append(hours);
			stb.append(":");
			if (minutes < 10)
				stb.append("0");
		}
		stb.append(minutes);

		return stb.toString();
	}
}
