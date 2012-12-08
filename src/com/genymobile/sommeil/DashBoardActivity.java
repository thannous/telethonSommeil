package com.genymobile.sommeil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DashBoardActivity extends Activity {
	
	Button mReveilBtn;
	Button mDormirBtn;
	Button mStatsBtn;
	TextView mTitle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_dashboard);
		
		mTitle = (TextView) findViewById(R.id.sleepillow);
		mReveilBtn = (Button) findViewById(R.id.reveilBtn);
		mDormirBtn = (Button) findViewById(R.id.dormirBtn);
		mStatsBtn = (Button) findViewById(R.id.statsBtn);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "SueEllenFrancisco.ttf");
		mTitle.setTypeface(font);
		
		mStatsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				SleepStatsChart chart = new SleepStatsChart();
				startActivity(chart.execute(DashBoardActivity.this));
			}
		});
	}
}