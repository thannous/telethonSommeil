package com.genymobile.sommeil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DashBoardActivity extends Activity {
	
	Button mReveilBtn;
	Button mDormirBtn;
	Button mStatsBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_dashboard);
		
		mReveilBtn = (Button) findViewById(R.id.reveilBtn);
		mDormirBtn = (Button) findViewById(R.id.dormirBtn);
		mStatsBtn = (Button) findViewById(R.id.statsBtn);
		
		mStatsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				SleepStatsChart chart = new SleepStatsChart();
				startActivity(chart.execute(DashBoardActivity.this));
			}
		});
	}
}