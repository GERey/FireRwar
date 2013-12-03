package com.example.firerwar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.ActionBar.LayoutParams;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import java.util.Random;

public class metricsViewer extends FragmentActivity {

	private XYMultipleSeriesDataset DownloadDataSet = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer DownloadRenderer = new XYMultipleSeriesRenderer();
	private XYSeries DLCurrentSeries; /*
									 * make this an arrayList of xy series ,
									 * then loop through that list updating the
									 * the graph with the repective values
									 */
	private GraphicalView DLChartView;
	private final Handler mHandler = new Handler();
	private Runnable mTimer1;
	public static int y;
	public ArrayList<XYSeries> m_SeriesData;
	public ArrayList<String> dataAppUsage;
	public ArrayList<Integer> uidStorage = new ArrayList<Integer>();
	public ArrayList<Integer> uidsList = new ArrayList<Integer>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graphs);
		DownloadRenderer.setAxisTitleTextSize(16);
		DownloadRenderer.setChartTitleTextSize(20);
		DownloadRenderer.setLabelsTextSize(30);
		DownloadRenderer.setLegendTextSize(15);
		DownloadRenderer.setMargins(new int[] { 10, 30, 0, 0 });
		DownloadRenderer.setYLabelsPadding(10);
		DownloadRenderer.setAxesColor(Color.CYAN);
		DownloadRenderer.setZoomEnabled(true, false);
		
		
		//addNewSeries(0);

		String seriesTitle = "Download";
		XYSeries series = new XYSeries(seriesTitle);

		DownloadDataSet.addSeries(series);
		DLCurrentSeries = series;
		
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		renderer.setColor(Color.RED);
		DownloadRenderer.addSeriesRenderer(renderer);
		dataSetup();
		addNewSeries(1);
//		
//		/**/
//		seriesTitle = "Upload";
//		series = new XYSeries(seriesTitle);
//
//		DownloadDataSet.addSeries(series);
//		DLCurrentSeries = series;
//		
//
//		renderer = new XYSeriesRenderer();
//		renderer.setColor(Color.CYAN);
//		DownloadRenderer.addSeriesRenderer(renderer);
//		printNetworkSettings();

	}
	
	/* function that will add series to the graph*/
	public void addNewSeries (int position) {
		
		Random rand = new Random();
		String temp1 = dataAppUsage.get(position);
		uidsList.add(uidStorage.get(position));
		String packages[] = temp1.split("\\.|:");
		String seriesTitle = packages[1];
//		Scanner  scan = new Scanner(temp1).useDelimiter(".");
//		String seriesTitle = "";
//		if (scan.hasNext()){
//			seriesTitle = scan.next();
//			if (scan.hasNext()){
//				seriesTitle = scan.next();
//				Toast.makeText(this, seriesTitle, Toast.LENGTH_SHORT).show();
//			}
//		}
		XYSeries series = new XYSeries(seriesTitle);
		DownloadDataSet.addSeries(series);
		/* Not sure about setting the current series just a proof of concept*/
		
		/*Sets up the second renderer*/
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		renderer.setColor(Color.rgb(rand.nextInt(), rand.nextInt(), rand.nextInt())); /* Make random color generator*/
		DownloadRenderer.addSeriesRenderer(renderer);
		
		
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (DLChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
			DLChartView = ChartFactory.getLineChartView(this, DownloadDataSet,
					DownloadRenderer);

			layout.addView(DLChartView);
		} else {
			DLChartView.repaint();
		}

		mTimer1 = new Runnable() {
			@Override
			public void run() {
				//printNetworkSettings();
				XYSeries[] temp = DownloadDataSet.getSeries();
				
				DLCurrentSeries=temp[0];
				DLCurrentSeries.add(y, TrafficStats.getTotalRxBytes());
				
				DLCurrentSeries=temp[1];
				DLCurrentSeries.add(y, TrafficStats.getTotalTxBytes());
//				
//				/*0 Is always the downloads and 1 is always the uploads*/
				for (int i = 2; i < temp.length; i++){
					
					
					DLCurrentSeries=temp[i];
					/*uid is minus two, because two lists are added by default who carry no uid
					 * Download and Upload Stats*/
 					DLCurrentSeries.add(y, TrafficStats.getUidRxBytes(uidsList.get(i-2)));
					
//					if (temp[i].getTitle().equals("Download")){
//						DLCurrentSeries=temp[i];
//						DLCurrentSeries.add(y, TrafficStats.getTotalRxBytes());
//					}
//					else if (temp[i].getTitle().equals("Upload")){
//						DLCurrentSeries=temp[i];
//						DLCurrentSeries.add(y, TrafficStats.getTotalTxBytes());
//					}
				}
				y++;
				if (DLChartView != null) {
					DLChartView.repaint();
				}
				mHandler.postDelayed(this, 2000);
			}
		};
		mHandler.postDelayed(mTimer1, 300);

	}


	/*
	 * You'll want to make a uid hashtable and then update the values in the
	 * listview whenever one of them increases, these increases should also be
	 * reflected in the graph view that I choose to use
	 */
	public View dataSetup() {

		TextView portDisplay = (TextView) findViewById(R.id.Namer);
		LinearLayout tempView = (LinearLayout) findViewById(R.id.LinearPortHolder);
		ListView listerPorts = (ListView) findViewById(R.id.PortItems);

		dataAppUsage = new ArrayList<String>();

		ArrayAdapter<String> adapter;
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataAppUsage);

		listerPorts.setAdapter(adapter);

		// calculates how much data has been downloaded and uploaded from boot.
		long rxBytes = TrafficStats.getTotalRxBytes();
		long txBytes = TrafficStats.getTotalTxBytes();
		dataAppUsage.add("Mobile.Download: " + Long.toString(rxBytes));
		dataAppUsage.add("Mobile.Uploaded: " + Long.toString(txBytes));

		// handles reading in all of the applications name and their data
		readApplicationPackageNames(dataAppUsage);
		listerPorts.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long rowId) 
			{
				int i = 0;
				addNewSeries(position);
				return false;
				
			}});

		// handles adding the graphview and it's colors/layouts

		adapter.notifyDataSetChanged();
		return tempView;

	}

	public ArrayList<String> readApplicationPackageNames(
			ArrayList<String> infoPackage) {

		PackageManager packer = this.getPackageManager();
		File dir = new File("/proc/uid_stat/");
		String[] children = dir.list();
		List<Integer> uids = new ArrayList<Integer>();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				int uid = Integer.parseInt(children[i]);
				String uidString = String.valueOf(uid);
				File uidFileDir = new File("/proc/uid_stat/" + uidString);
				File uidActualFile = new File(uidFileDir, "tcp_rcv");

				try {
					BufferedReader br = new BufferedReader(new FileReader(
							uidActualFile));
					String line;

					while ((line = br.readLine()) != null) {
						infoPackage
								.add(packer.getNameForUid(uid) + ": " + line);
						uidStorage.add(uid);
					}
					br.close();

				} catch (IOException e) {
					// handle this
				}

				uids.add(uid);
			}

		}
		return infoPackage;
	}

}
