package com.example.firerwar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class metricsViewer extends Fragment {

	ServerSocket sock;
	Socket temp;

	Context mContext;

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.form, container, false);

		return printNetworkSettings(mContext, rootView);
	}

	/*
	 * You'll want to make a uid hashtable and then update the values in the
	 * listview whenever one of them increases, these increases should also be
	 * reflected in the graph view that I choose to use
	 */
	public View printNetworkSettings(Context mContext, View rootView) {

		TextView portDisplay = (TextView) rootView.findViewById(R.id.Namer);
		LinearLayout tempView = (LinearLayout) rootView
				.findViewById(R.id.LinearPortHolder);
		LinearLayout graphV = (LinearLayout) rootView.findViewById(R.id.graph);
		ListView listerPorts = (ListView) rootView.findViewById(R.id.PortItems);

		ArrayList<String> ipViewText = new ArrayList<String>();

		ArrayAdapter<String> adapter;
		adapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_list_item_1, ipViewText);

		listerPorts.setAdapter(adapter);

		// calculates how much data has been downloaded and uploaded from boot.
		long rxBytes = TrafficStats.getTotalRxBytes();
		long txBytes = TrafficStats.getTotalTxBytes();
		ipViewText.add("Download: " + Long.toString(rxBytes));
		ipViewText.add("Uploaded: " + Long.toString(txBytes));

		// handles reading in all of the applications name and their data
		readApplicationPackageNames(ipViewText);

		// handles adding the graphview and it's colors/layouts
		graphV.addView(graphInitializer(rxBytes, txBytes));

		adapter.notifyDataSetChanged();
		return tempView;

	}

	public GraphView graphInitializer(long downloaded, long uploaded) {

		GraphViewSeries downloadSeries = new GraphViewSeries("Download",
				new GraphViewSeriesStyle(Color.RED, 10), new GraphViewData[] {
						new GraphViewData(1, downloaded),
						new GraphViewData(4, 20000000 + downloaded) });
		GraphViewSeries uploadSeries = new GraphViewSeries("Uploaded",
				new GraphViewSeriesStyle(Color.BLUE, 10), new GraphViewData[] {
						new GraphViewData(1, uploaded),
						new GraphViewData(4, 10000000 + uploaded) });
		GraphView graphView = new LineGraphView(mContext, "Network Data Graph");

		// graphView.setVerticalLabels(new String[]{});
		graphView.addSeries(downloadSeries);
		graphView.addSeries(uploadSeries);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.GREEN);

		graphView.setCustomLabelFormatter(new CustomLabelFormatter() {

			@Override
			public String formatLabel(double val, boolean isValueX) {
				int value = (int) val;
				value = value / 1000;
				if (!isValueX) {
					if (value <= 1000) {
						return "1 MB";
					} else if (value == 5000) {
						return "5 MB";
					} else if (value == 20000) {
						return "20 MB";
					} else if (value == 40000) {
						return "40 MB";
					} else if (value == 100000) {
						return "100 MB";
					}
				}
				return null;
			}
		});
		// graphView.
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
		graphView.setShowLegend(true);
		graphView.setLegendAlign(LegendAlign.TOP);
		graphView.setLegendWidth(300);
		graphView.setManualYAxisBounds(100000000, 0.0);

		return graphView;

	}

	public ArrayList<String> readApplicationPackageNames(
			ArrayList<String> infoPackage) {

		PackageManager packer = mContext.getPackageManager();
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
