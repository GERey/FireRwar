package com.example.firerwar;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class metricsViewer extends Fragment{
	
	ServerSocket sock;
	Socket temp;

	Context mContext;
	
    public void setContext (Context mContext) {
    	this.mContext = mContext;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.port_enter,container,false);
        

    	return printNetworkSettings(mContext, rootView);
    }


public View printNetworkSettings(Context mContext, View rootView) {
	TextView portDisplay = new TextView(mContext);
	LinearLayout tempView = (LinearLayout) rootView;
	ListView listerPorts = (ListView) rootView.findViewById(R.id.PortItems);
	//ListView tempView = (ListView) rootView;

	
	ArrayList<String> ipViewText = new ArrayList<String>();
	
	portDisplay.setText("Ports Blocked");
	
	//tempView.addView(ipAddress);
	
	ArrayAdapter<String> adapter;
	adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, ipViewText);
	
	
	//ipAddress.setText(intToIp(addr.ipAddress)+"\n");
	
	
	//tempView = ((LinearLayout) rootView.findViewById(R.id.listItems));

	listerPorts.setAdapter(adapter);
	//tempView.addView(listerPorts);
	
	ipViewText.add("SUPER METRICS DERP");


	//TODO: Add network graph to monitor throughput
	
	
	adapter.notifyDataSetChanged();
	
	// ((TextView) rootView.findViewById(android.R.id.text1)).setText(intToIp(addr.ipAddress)+"\n");
	 
	 return tempView;

}

}
