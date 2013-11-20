package com.example.firerwar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graphs);
		DownloadRenderer.setAxisTitleTextSize(16);
		DownloadRenderer.setChartTitleTextSize(20);
		DownloadRenderer.setLabelsTextSize(15);
		DownloadRenderer.setLegendTextSize(15);
		DownloadRenderer.setMargins(new int[] { 20, 30, 15, 0 });
		DownloadRenderer.setYLabelsPadding(10);
		DownloadRenderer.setAxesColor(Color.YELLOW);

		String seriesTitle = "Download Display";
		XYSeries series = new XYSeries(seriesTitle);

		DownloadDataSet.addSeries(series);
		DLCurrentSeries = series;
		/**/
		

		XYSeriesRenderer renderer = new XYSeriesRenderer();
		renderer.setColor(Color.RED);
		DownloadRenderer.addSeriesRenderer(renderer);
		printNetworkSettings();

	}
	
	/* function that will add series to the graph*/
	public void addNewSeries () {
		
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
				DLCurrentSeries.add(y, TrafficStats.getTotalRxBytes());
				y++;
				if (DLChartView != null) {
					DLChartView.repaint();
				}
				mHandler.postDelayed(this, 2000);
			}
		};
		mHandler.postDelayed(mTimer1, 300);

	}

	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	//
	// View rootView = inflater.inflate(R.layout.form, container, false);
	//
	// return printNetworkSettings(mContext, rootView);
	// }

	/*
	 * You'll want to make a uid hashtable and then update the values in the
	 * listview whenever one of them increases, these increases should also be
	 * reflected in the graph view that I choose to use
	 */
	public View printNetworkSettings() {

		TextView portDisplay = (TextView) findViewById(R.id.Namer);
		LinearLayout tempView = (LinearLayout) findViewById(R.id.LinearPortHolder);
		ListView listerPorts = (ListView) findViewById(R.id.PortItems);

		ArrayList<String> ipViewText = new ArrayList<String>();

		ArrayAdapter<String> adapter;
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ipViewText);

		listerPorts.setAdapter(adapter);

		// calculates how much data has been downloaded and uploaded from boot.
		long rxBytes = TrafficStats.getTotalRxBytes();
		long txBytes = TrafficStats.getTotalTxBytes();
		ipViewText.add("Download: " + Long.toString(rxBytes));
		ipViewText.add("Uploaded: " + Long.toString(txBytes));

		// handles reading in all of the applications name and their data
		readApplicationPackageNames(ipViewText);
		listerPorts.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{
				int i = 0;
				Log.d("Long clicked","hi");
				//Toast.makeText(this, "You long clicked a thing!",Toast.LENGTH_SHORT).show();
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
