package com.example.firerwar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class metricsViewer extends FragmentActivity {
	 private final Handler mHandler = new Handler();
     private Runnable mTimer1;
     private Runnable mTimer2;
     private GraphView graphView;
     private GraphViewSeries exampleSeries1;
     private GraphViewSeries exampleSeries2;
     private double graph2LastXValue = 5d;
     private GraphViewSeries exampleSeries3;

     

     private double getRandom() {
             double high = 3;
             double low = 0.5;
             return Math.random() * (high - low) + low;
     }

     /** Called when the activity is first created. */
     @Override
     public void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.graphs);
             

             // init example series data
             exampleSeries1 = new GraphViewSeries(new GraphViewData[] {
                             new GraphViewData(1, 2.0d)
                             , new GraphViewData(2, 1.5d)
                             , new GraphViewData(2.5, 3.0d) // another frequency
                             , new GraphViewData(3, 2.5d)
                             , new GraphViewData(4, 1.0d)
                             , new GraphViewData(5, 3.0d)
             });
             exampleSeries3 = new GraphViewSeries(new GraphViewData[] {});
             exampleSeries3.getStyle().color = Color.CYAN;

             // graph with dynamically genereated horizontal and vertical labels
             graphView = new LineGraphView(
                     this // context
                     , "GraphViewDemo" // heading
            		 );
//             if (getIntent().getStringExtra("type").equals("bar")) {
//                     graphView = new BarGraphView(
//                                     this // context
//                                     , "GraphViewDemo" // heading
//                     );
//             } else {
//                     graphView = new LineGraphView(
//                                     this // context
//                                     , "GraphViewDemo" // heading
//                     );
//             }
             graphView.addSeries(exampleSeries1); // data
             graphView.addSeries(exampleSeries3);

             LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
             layout.addView(graphView);

             // ----------
//             exampleSeries2 = new GraphViewSeries(new GraphViewData[] {
//                             new GraphViewData(1, 2.0d)
//                             , new GraphViewData(2, 1.5d)
//                             , new GraphViewData(2.5, 3.0d) // another frequency
//                             , new GraphViewData(3, 2.5d)
//                             , new GraphViewData(4, 1.0d)
//                             , new GraphViewData(5, 3.0d)
//             });

             // graph with custom labels and drawBackground
//             if (getIntent().getStringExtra("type").equals("bar")) {
//                     graphView = new BarGraphView(
//                                     this
//                                     , "GraphViewDemo"
//                     );
//             } else {
//                     graphView = new LineGraphView(
//                                     this
//                                     , "GraphViewDemo"
//                     );
//                     ((LineGraphView) graphView).setDrawBackground(true);
//             }
            // graphView.addSeries(exampleSeries2); // data
            // graphView.setViewPort(1, 8);
            // graphView.setScalable(true);

             //layout = (LinearLayout) findViewById(R.id.graph2);
             //layout.addView(graphView);
     }

     @Override
     protected void onPause() {
             mHandler.removeCallbacks(mTimer1);
             mHandler.removeCallbacks(mTimer2);
             super.onPause();
     }

     @Override
     protected void onResume() {
             super.onResume();
             final int q = (int) TrafficStats.getTotalRxBytes();

             mTimer1 = new Runnable() {
                     @Override
                     public void run() {
                    	 final int[] stats = {(int) TrafficStats.getTotalRxBytes(),
                    	                      (int) TrafficStats.getTotalRxBytes(),
                    	                      (int) TrafficStats.getTotalRxBytes(),
                    	                      (int) TrafficStats.getTotalRxBytes(),
                    	                      (int) TrafficStats.getTotalRxBytes()};
                    	 
                    	 exampleSeries1.resetData(new GraphViewData[] {
                                 new GraphViewData(1,stats[0])
                                 , new GraphViewData(2, stats[1])
                                 , new GraphViewData(3, stats[2])
                                 , new GraphViewData(4, stats[3])
                                 , new GraphViewData(5, stats[4])
                    	 });

                             mHandler.postDelayed(this, 300);
                     }
             };
             mHandler.postDelayed(mTimer1, 300);

             mTimer2 = new Runnable() {
                     @Override
                     public void run() {
                             graph2LastXValue += 1d;
                             exampleSeries2.appendData(new GraphViewData(graph2LastXValue, getRandom()), true, 10);
                             mHandler.postDelayed(this, 1000);
                     }
             };
             //mHandler.postDelayed(mTimer2, 1000);
     }
}
