package com.example.firerwar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    	
        View rootView = inflater.inflate(R.layout.form,container,false);
        

    	return printNetworkSettings(mContext, rootView);
    }

/* You'll want to make a uid hashtable and then update the values
 * in the listview whenever one of them increases, these increases 
 * should also be reflected in the graph view that I choose to use */
public View printNetworkSettings(Context mContext, View rootView) {
	
	
	TextView portDisplay = (TextView) rootView.findViewById(R.id.Namer);
	LinearLayout tempView = (LinearLayout) rootView.findViewById(R.id.LinearPortHolder);
	ListView listerPorts = (ListView) rootView.findViewById(R.id.PortItems);
	PackageManager packer = mContext.getPackageManager();


	
	ArrayList<String> ipViewText = new ArrayList<String>();
	
	portDisplay.setText("Ports Blocked");

	
	ArrayAdapter<String> adapter;
	adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, ipViewText);
	

	listerPorts.setAdapter(adapter);
	
	
	long rxBytes = TrafficStats.getTotalRxBytes();

	long txBytes = TrafficStats.getTotalTxBytes();
	

	ipViewText.add("Download: "+Long.toString(rxBytes));
	ipViewText.add("Uploaded: "+Long.toString(txBytes));
	
	

	File dir = new File("/proc/uid_stat/");
	String[] children = dir.list();
	List<Integer> uids = new ArrayList<Integer>();
	if (children != null) {
	  for (int i = 0; i < children.length; i++) {
	    int uid = Integer.parseInt(children[i]);
	    String uidString = String.valueOf(uid);
	    File uidFileDir = new File("/proc/uid_stat/"+uidString);
	    File uidActualFile = new File(uidFileDir,"tcp_rcv");

	    try {
	        BufferedReader br = new BufferedReader(new FileReader(uidActualFile));
	        String line;

	        while ((line = br.readLine()) != null) {
	            //Log.d(String.valueOf(uid), line);//this returns the amount of data received for the particular uid
	        	Log.d(packer.getNameForUid(uid), line);
	        	ipViewText.add(packer.getNameForUid(uid)+": "+line);

	        }
	        br.close();
	        
	    }
	    catch (IOException e) {
	        //handle this
	    }
	    
	      uids.add(uid);
	    }
	  
	  }
	adapter.notifyDataSetChanged();
	 return tempView;

}



}
