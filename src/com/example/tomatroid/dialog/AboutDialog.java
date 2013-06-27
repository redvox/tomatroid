package com.example.tomatroid.dialog;

import com.example.tomatroid.MainActivity;
import com.example.tomatroid.R;
import com.example.tomatroid.util.Util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.util.Linkify;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutDialog extends Dialog {

	private static Context mContext = null;
	private static int source;
	private static int titlepicture = -1;

	public AboutDialog(Context context, int file, int image) {
		super(context);
		mContext = context;
		source = file;
		titlepicture = image;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_about);

		if(AboutDialog.titlepicture > -1){
			 ImageView iv = (ImageView) findViewById(R.id.info_image);
			 iv.setImageResource(R.drawable.beware);
		}

		TextView tv = (TextView) findViewById(R.id.legal_text);
		tv.setText(Util.readRawTextFile(mContext, R.raw.legal));
		tv = (TextView) findViewById(R.id.info_text);
		tv.setText(Html.fromHtml(Util.readRawTextFile(mContext, AboutDialog.source),
				new ImageGetter() {
					@Override
					public Drawable getDrawable(String source) {
						Drawable drawFromPath;
						int path = getContext().getResources().getIdentifier(
								source, "drawable", getContext().getPackageName());
						drawFromPath = (Drawable) getContext().getResources()
								.getDrawable(path);
						drawFromPath.setBounds(0, 0,
								drawFromPath.getIntrinsicWidth(),
								drawFromPath.getIntrinsicHeight());
						return drawFromPath;
					}
				}, null));


		tv.setLinkTextColor(Color.BLACK);
		Linkify.addLinks(tv, Linkify.ALL);
	}
}
