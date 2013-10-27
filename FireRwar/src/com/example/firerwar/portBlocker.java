package com.example.firerwar;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
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

public class portBlocker extends Fragment {
	ServerSocket sock;
	Socket temp;

	Context mContext;
	
    public static final String ARG_SECTION_NUMBER = "BOOP";

    public void setContext (Context mContext) {
    	this.mContext = mContext;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.port_enter,container,false);
        
        
        try {
        	//openport(2222);
			//blockport(2222);
        	System.out.println("derp");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return printNetworkSettings(mContext, rootView);
    }
    
    public void blockport (final int port) throws IOException {
    	//sock = new ServerSocket();
    	temp = new Socket();

    	
    	
    	try {
    		new Thread(new Runnable() {

				@Override
				public void run() {
					//while(true) {
						try {
							
							if (!temp.isBound()){
								temp = new Socket("10.151.42.155",port);
								temp.setReuseAddress(true);
								temp.close();
							}
							
						} catch (Exception e) {
							System.out.println("thread blockport failed"+e);
						}
					//}
					
				}
    			
    		}).start();
    	} catch (Exception e){
    		Log.d("blockport Exception",""+e);
    	}
    	
    }
    
    public void openport (final int port) throws IOException {
    	sock = new ServerSocket();

    	
    	
    	try {
    		new Thread(new Runnable() {

				@Override
				public void run() {
						try {
							
							if (!temp.isBound()){
					            ServerSocket serverSocket = new ServerSocket(port);
					   			serverSocket.accept();

							}
							
						} catch (Exception e) {
							System.out.println("thread openport failed"+e);
						}
					
				}
    			
    		}).start();
    	} catch (Exception e){
    		Log.d("openport Exception",""+e);
    	}
    	
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
	
	ipViewText.add("derp");
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
