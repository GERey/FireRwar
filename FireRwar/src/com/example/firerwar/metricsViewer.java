package com.example.firerwar;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
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
	VpnClient connectVpn = new VpnClient();
	
    public void setContext (Context mContext) {
    	this.mContext = mContext;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.form,container,false);
        
        rootView.findViewById(R.id.connect)
        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VpnClient.class);
                startActivity(intent);
            }
        });

    	return rootView;//printNetworkSettings(mContext, rootView);
    }


public View printNetworkSettings(Context mContext, View rootView) {
	
	
	TextView portDisplay = (TextView) rootView.findViewById(R.id.Namer);
	LinearLayout tempView = (LinearLayout) rootView.findViewById(R.id.LinearPortHolder);
	ListView listerPorts = (ListView) rootView.findViewById(R.id.PortItems);
	//ListView tempView = (ListView) rootView;

	
	ArrayList<String> ipViewText = new ArrayList<String>();
	
	portDisplay.setText("Ports Blocked");
	//tempView.addView(portDisplay);
	
	//tempView.addView(ipAddress);
	
	ArrayAdapter<String> adapter;
	adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, ipViewText);
	
	
	//ipAddress.setText(intToIp(addr.ipAddress)+"\n");
	
	
	//tempView = ((LinearLayout) rootView.findViewById(R.id.listItems));

	listerPorts.setAdapter(adapter);
	//tempView.addView(listerPorts);
	
	ipViewText.add("Metrics shown here");
//	ipViewText.add("IP Address: "+intToIp(addr.ipAddress)+"\n");
//	ipViewText.add("Subnet Mask: " + intToIp(addr.netmask)+"\n");
//	ipViewText.add("Default Gateway: " + intToIp(addr.gateway)+"\n");
//	ipViewText.add("Packets Blocked: " + "<number>" + "\n");
//	ipViewText.add("IPv4:DNSServers: " + "<?>" + "\n");
	//TODO: Add button that is named release port so that you can see it working in action
	//TODO: add text field that shows ports blocked
	//TODO: look up how to block tcp and udp ports, also how to distinguish between them
	//TODO: Add network graph to monitor throughput
	
	
	adapter.notifyDataSetChanged();

	
	// ((TextView) rootView.findViewById(android.R.id.text1)).setText(intToIp(addr.ipAddress)+"\n");
	 
	 return tempView;

}

}
