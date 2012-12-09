package com.genymobile.sommeil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * Multiple temperature demo chart.
 */
public class MovingSleepStatsChartActivity extends Activity {
	
	Integer mIndex = 0;
	ArrayList<Double> mValuesX = new ArrayList<Double>();
	ArrayList<Double> mValuesY = new ArrayList<Double>();
	List<double[]> mCourbesX = new ArrayList<double[]>();
	List<double[]> mCourbesY = new ArrayList<double[]>();
	
	GraphicalView mChart;
	LinearLayout mContainer;
	AsyncTask<Void, Integer, Void> mDrawTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		double[] valuesX = new double[mValuesX.size()];
		for (int i = 0; i < mValuesX.size(); i++) {
			valuesX[i] = mValuesX.get(i);
		}
		mCourbesX.add(valuesX);
		
		double[] valuesY = new double[mValuesY.size()];
		for (int i = 0; i < mValuesY.size(); i++) {
			valuesY[i] = mValuesY.get(i);
		}
		mCourbesY.add(valuesY);
		
		Log.d("MOVING CHART", "Creating");
		mChart = new SleepStatsChart().create(this, mCourbesX, mCourbesY);
		
		setContentView(R.layout.stats);
		mContainer = (LinearLayout) findViewById(R.id.statsContainer);
		mContainer.addView(mChart);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mDrawTask = new AsyncTask<Void, Integer, Void>() {
			
			@Override
			protected Void doInBackground(Void... command) {

				while (true) {
					
					int randInt = new Random().nextInt(30);
					
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					Log.d("MOVING CHART", "PUBLISHING PROGRESS: "+randInt);
					publishProgress(randInt);
				}
			}
			
			/* (non-Javadoc)
			 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
			 */
			@Override
			protected void onProgressUpdate(Integer... progress) {
				
				mValuesX.add(mIndex.doubleValue());
				mValuesY.add(progress[0].doubleValue());
				
				mIndex++;
				
				mCourbesX.clear();
				mCourbesY.clear();
				
				double[] valuesX = new double[mValuesX.size()];
				for (int i = 0; i < mValuesX.size(); i++) {
					valuesX[i] = mValuesX.get(i);
				}
				mCourbesX.add(valuesX);
				
				double[] valuesY = new double[mValuesY.size()];
				for (int i = 0; i < mValuesY.size(); i++) {
					valuesY[i] = mValuesY.get(i);
				}
				mCourbesY.add(valuesY);
				
				mChart = new SleepStatsChart().create(MovingSleepStatsChartActivity.this, 
						mCourbesX, mCourbesY);
				
				mContainer.removeAllViews();
				mContainer.addView(mChart);
				
				super.onProgressUpdate(progress);
			}
		}.execute();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mDrawTask.cancel(true);
	}
}
