package com.example.tomatroid.dialog;

import com.example.tomatroid.R;
import com.example.tomatroid.util.Util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.TextView;

public class AboutDialog extends Dialog {

	private static Context mContext = null;

	public AboutDialog(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.dialog_about);
		TextView tv = (TextView) findViewById(R.id.legal_text);
		tv.setText(Util.readRawTextFile(mContext, R.raw.legal));
		tv = (TextView) findViewById(R.id.info_text);
		tv.setText(Html.fromHtml(Util.readRawTextFile(mContext, R.raw.info)));
		tv.append((Html.fromHtml(Util.readRawTextFile(mContext, R.raw.history))));
		tv.setLinkTextColor(Color.BLACK);
		Linkify.addLinks(tv, Linkify.ALL);
	}
}
