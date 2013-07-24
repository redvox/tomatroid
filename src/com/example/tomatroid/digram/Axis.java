package com.example.tomatroid.digram;

import java.util.ArrayList;

import com.example.tomatroid.R;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class Axis extends View {

	Context context;
	float width = 0;
	float height = 0;

	float maxValue;
	float pointPerMinute = 0;

	Paint color;
	Paint text;
	Paint white;

	ArrayList<Float> position = new ArrayList<Float>();
	ArrayList<String> values = new ArrayList<String>();
	String maxText;

	public Axis(Context context, float maxValue) {
		super(context);
		this.context = context;
		this.maxValue = maxValue;

		Theme t = context.getTheme();
		TypedValue typedvalueattr = new TypedValue();
		t.resolveAttribute(R.attr.myattr_textcolor, typedvalueattr, true);
		color = new Paint();
		color.setColor(typedvalueattr.data);
		color.setStrokeWidth(2);
		color.setAlpha(100);

		text = new Paint();
		text.setColor(typedvalueattr.data);
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
		canvas.drawLine(width, 0, width, height, color);
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
		Log.e("Axis", "pixelPerMinute: "+ pointPerMinute);
		position.clear();
		values.clear();
		
		float tenth = maxValue / 10f;
		for (float i = 0; i < 10; i++) {
			position.add(height - (i * (tenth * pointPerMinute)));
			values.add(generateTimeText((int)(i * tenth)));
		}
		
//		float tenth = height / 10f;
//		for (float i = 0; i < 10; i++) {
//			position.add(height - tenth);
//			values.add(generateTimeText(tenth/pointPerMinute));
//			tenth += tenth;
//		}
		
		Log.e("Axis", "tenth: "+tenth +" height: "+height);
		
		maxText = generateTimeText(maxValue);
	}

	public void adjustToNewMaximum(int newMax) {
		maxValue = newMax;
		// Animation a = new AlphaAnimation(1, 0);
		// a.setDuration(1000);
		Animation b = new AlphaAnimation(0, 1);
		b.setDuration(1000);

		calculate();
		startAnimation(b);
	}

	protected String generateTimeText(float value) {
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
