package com.genymobile.sommeil;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * Multiple temperature demo chart.
 */
public class SleepStatsChart extends AbstractDemoChart {
  
  /**
   * Executes the chart demo.
   * 
   * @param context the context
   * @return the built intent
   */
  public Intent execute(Context context) {
    String[] titles = new String[] { "Moyenne" };
    List<double[]> x = new ArrayList<double[]>();
    for (int i = 0; i < titles.length; i++) {
      x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
    }
    List<double[]> values = new ArrayList<double[]>();
    values.add(new double[] { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 });
    int[] colors = new int[] { Color.parseColor("#FF9900"), Color.BLACK };
    PointStyle[] styles = new PointStyle[] { PointStyle.POINT, PointStyle.POINT };
    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(2);
    setRenderer(renderer, colors, styles);
    
    setChartSettings(renderer, "", "", "", 0.5, 12.5, 0, 32,
        Color.LTGRAY, Color.LTGRAY);
    renderer.setXLabels(12);
    renderer.setBackgroundColor(Color.BLACK);
    renderer.setYLabels(0);
    renderer.setShowGrid(true);
    renderer.setXLabelsAlign(Align.RIGHT);
    renderer.setYLabelsAlign(Align.RIGHT);
    renderer.setZoomButtonsVisible(true);
    renderer.setPanLimits(new double[] { 0, 40, 0, 0 });
    renderer.setZoomLimits(new double[] { -10, 20, 0, 0 });
    renderer.setZoomRate(1.00f);
    
    XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(1);
    seriesRenderer.setFillBelowLine(true);
    seriesRenderer.setFillBelowLineColor(Color.parseColor("#880033CC"));
    seriesRenderer.setLineWidth(2.5f);
    seriesRenderer.setDisplayChartValues(false);
    
    XYSeriesRenderer average = (XYSeriesRenderer) renderer.getSeriesRendererAt(0);
    average.setFillBelowLine(false);
    average.setLineWidth(1f);
    average.setDisplayChartValues(false);
    
    renderer.setLabelsColor(Color.WHITE);
    renderer.setXLabelsColor(Color.GREEN);
    renderer.setYLabelsColor(0, colors[0]);

    renderer.addYTextLabel(7.5f, "Sommeil profond");
    renderer.addYTextLabel(31, "Sommeil léger");
    
    renderer.setYLabelsColor(0, Color.WHITE);
    renderer.setYLabelsAngle(270);
    renderer.setYLabelsAlign(Align.RIGHT);
    
    XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
    values.clear();
    values.add(new double[] { 4.3, 4.9, 5.9, 8.8, 10.8, 11.9, 13.6, 12.8, 11.4, 9.5, 7.5, 5.5 });
    addXYSeries(dataset, new String[] { "Average" }, x, values, 1);
    
    Intent intent = ChartFactory.getCubicLineChartIntent(context, dataset, renderer, 0.3f,
        "Courbe du sommeil");
    return intent;
  }
}
