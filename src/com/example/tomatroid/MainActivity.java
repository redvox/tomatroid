package com.example.tomatroid;

import java.util.ArrayList;

import com.example.tomatroid.digram.Bar;
import com.example.tomatroid.util.StoredAnimation;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

public class MainActivity extends Activity {

	LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT);

	LayoutParams barParams = new TableLayout.LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);

	LinearLayout digram;
	LinearLayout control;

	View bar1;
	View bar2;
	View bar3;
	View bar4;

	Button[] bA;

	ArrayList<String> commands = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		commands.add("Pomodoro!");
		commands.add("Pause Kurz!");
		commands.add("Pause Lang!");
		commands.add("Pause Lang! (Gaming)");
		commands.add("Thema?");
		commands.add("Statistic?");
		commands.add("Gaming!");
		commands.add("Sleeping!");
		commands.add("Stop for now!");

		digram = (LinearLayout) findViewById(R.id.digram);
		control = (LinearLayout) findViewById(R.id.control);

		bar1 = new Bar(digram.getContext(), 60);
		// bar1.setPadding(10, 0, 10 ,0);
		bar2 = new Bar(digram.getContext(), 30);
		// bar2.setPadding(10, 0, 10 ,0);
		bar3 = new Bar(digram.getContext(), 15);
		// bar3.setPadding(10, 0, 10 ,0);
		bar4 = new Bar(digram.getContext(), 55);

		digram.addView(bar1, barParams);
		digram.addView(bar2, barParams);
		digram.addView(bar3, barParams);
		digram.addView(bar4, barParams);

		// forward.setBackgroundResource(android.R.drawable.ic_media_play);

		bA = new Button[commands.size()];
		for (int i = 0; i < bA.length; i++) {
			RelativeLayout rLL = new RelativeLayout(control.getContext());
			ImageView iV = new ImageView(rLL.getContext());
			iV.setBackgroundResource(android.R.drawable.ic_media_play);
//			iV.setMaxWidth(50);
			iV.setAlpha(0);
			
			bA[i] = new Button(this);
			bA[i].setText(commands.get(i));
			bA[i].setOnClickListener(new ControlListener(bA[i]));
			// control.addView(bA[i]);
			
			RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			relativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
			
			
			rLL.addView(iV, relativeParams);
//			rLL.addView(iV);
			rLL.addView(bA[i]);

			control.addView(rLL);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (bA.length != 0) {
			for (int i = 0; i < bA.length; i++) {
				// Log.e("1", "2");
				// b.setText("Bwah");
				bA[i].startAnimation(StoredAnimation.inFromRightAnimation(i));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
