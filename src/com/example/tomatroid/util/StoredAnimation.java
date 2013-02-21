package com.example.tomatroid.util;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;

public class StoredAnimation {

	public static int Animationspeed = 500;

	public static Animation slideHorizontal(float go) {
		Animation inFromRight = new TranslateAnimation(
				Animation.ABSOLUTE, go,
				Animation.ABSOLUTE, 0.0f,
				Animation.ABSOLUTE, 0.0f,
				Animation.ABSOLUTE, 0.0f);
		inFromRight.setDuration(Animationspeed);
//		inFromRight.setInterpolator(new BounceInterpolator());
		return inFromRight;
	}
	
	public static Animation slideVertical(float go) {
		Animation slideVertical = new TranslateAnimation(
				Animation.ABSOLUTE, 0.0f,
				Animation.ABSOLUTE, 0.0f,
				Animation.ABSOLUTE, go,
				Animation.ABSOLUTE, 0.0f);
		slideVertical.setDuration(Animationspeed);
		return slideVertical;
	}
	
	public static Animation slideLeft(float amount) {
		Animation inFromRight = new TranslateAnimation(
				Animation.ABSOLUTE, amount,
				Animation.ABSOLUTE, 0.0f,
				Animation.ABSOLUTE, 0.0f,
				Animation.ABSOLUTE, 0.0f);
		inFromRight.setDuration(Animationspeed);
//		inFromRight.setInterpolator(new BounceInterpolator());
		return inFromRight;
	}
	
	public static Animation inFromRightAnimation(int rate) {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(Animationspeed + (rate*50));
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	public static Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(Animationspeed);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	public static Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(Animationspeed);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}
	
	public static Animation inFromButtomAnimation(int rate) {
		Animation inFromButtom = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromButtom.setDuration(Animationspeed + (rate*150));
//		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromButtom;
	}

	public static Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(Animationspeed);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}
}
