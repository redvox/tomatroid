package com.example.tomatroid.digram;

import com.example.tomatroid.MainActivity;
import com.example.tomatroid.R;
import com.example.tomatroid.util.StoredAnimation;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class Bar extends View {

	MainActivity mA;
	Context context;
	int value = 0;
	String valueText;
	int valueTextCharCount = 0;
	float width = 0;
	float height = 0;
	float barPixelHeight = 0;

	int maxValue;
	float pointPerMinute = 0;

	Paint color;
	Paint text;
	Paint white;

	public Bar(MainActivity mA, Context context, int value, int colorString, int maxValue) {
		super(context);
		this.mA = mA;
		this.context = context;
		this.value = value;
		this.maxValue = maxValue;

		color = new Paint();
		color.setColor(colorString);

		Theme t = context.getTheme();
		TypedValue typedvalueattr = new TypedValue();
		t.resolveAttribute(R.attr.myattr_textcolor, typedvalueattr, true);
		
		text = new Paint();
		text.setColor(typedvalueattr.data);
		text.setTextSize(30);
		text.setTextAlign(Align.LEFT);

		white = new Paint();
		white.setAntiAlias(true);
		white.setColor(Color.WHITE);
		white.setStyle(Style.STROKE);
		white.setStrokeWidth(10);
		white.setAlpha(75);
		changeValueText();
	}

	public void setValue(int newValue) {
		this.value = newValue;
		changeValueText();
	}

	public void addValue(int increment) {
		float pixel = increment * pointPerMinute;
		value += increment;
		if (value < maxValue){
			startAnimation(StoredAnimation.slideVertical(pixel));
		} else {
			mA.barExceededLimit(value);
		}
		
		barPixelHeight = value * pointPerMinute;
		changeValueText();
	}

	public void adjustToNewMaximum(int newMax) {
		maxValue = newMax;
		pointPerMinute = height / maxValue;
		
//		float old_barPixelHeight = barPixelHeight;
		float new_barPixelHeight = value * pointPerMinute;
		
		startAnimation(StoredAnimation.slideVertical(new_barPixelHeight));

		barPixelHeight = new_barPixelHeight;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(0, height - barPixelHeight, width, height,
				color);
		canvas.drawRect(2, height - barPixelHeight + 2, width - 2,
				height + 10, white);
		canvas.drawText(valueText, width - (20 * valueTextCharCount),
				height - 20, text);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// Account for padding
		float xpad = (float) (getPaddingLeft() + getPaddingRight());
		float ypad = (float) (getPaddingTop() + getPaddingBottom());

		width = (float) w - xpad;
		height = (float) h - ypad;

		pointPerMinute = height / maxValue;
		barPixelHeight = value * pointPerMinute;
	}

	protected void changeValueText() {
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

		valueText = stb.toString();
		valueTextCharCount = valueText.length();
	}
}
