package com.example.tomatroid;

import com.example.tomatroid.util.StoredAnimation;

import android.view.View;
import android.view.View.OnClickListener;


public class ControlListener implements OnClickListener {

	boolean pressed = false;
	View v;
	
	public ControlListener(View v){
		this.v = v;
	}
	
	@Override
	public void onClick(View v) {
		toggle();

	}
	
	public void animation(){
		if(pressed){
			v.startAnimation(StoredAnimation.slideHorizontal(-50));
			v.setTranslationX(50);
		} else {
			v.startAnimation(StoredAnimation.slideHorizontal(50));
			v.setTranslationX(0);
		}
	}
	
	public void toggle(){
		pressed = !pressed;
		animation();
	}
}
