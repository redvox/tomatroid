package com.example.tomatroid;

import com.example.tomatroid.digram.Bar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;

public class MainActivity extends Activity {

	LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT);

	LayoutParams barParams = new TableLayout.LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);

	LinearLayout digram;

	View bar1;
	View bar2;
	View bar3;
	View bar4;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		digram = (LinearLayout) findViewById(R.id.digram);
		
		bar1 = new Bar(digram.getContext(), 60);
//		bar1.setPadding(10, 0, 10 ,0);
		bar2 = new Bar(digram.getContext(), 30);
//		bar2.setPadding(10, 0, 10 ,0);
		bar3 = new Bar(digram.getContext(), 15);
//		bar3.setPadding(10, 0, 10 ,0);
		bar4 = new Bar(digram.getContext(), 55);
		
		digram.addView(bar1, barParams);
		digram.addView(bar2, barParams);
		digram.addView(bar3, barParams);
		digram.addView(bar4, barParams);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
