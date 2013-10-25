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
    	
        View rootView = inflater.inflate(R.layout.ip_text_info,container,false);
        
        try {
			blockport();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return printNetworkSettings(mContext, rootView);
    }
    
    public void blockport () throws IOException {
    	sock = new ServerSocket();
    	temp = new Socket();
    	
    	
    	try {
    		new Thread(new Runnable() {

				@Override
				public void run() {
					while(true) {
						try {
							
							if (!temp.isBound()){
								temp = new Socket("10.151.42.155",2222);
//								sock.setReuseAddress(false);
//								Log.d("in not being bound","here not being bound");
//								sock = new ServerSocket (2222);
//								sock.close();
							}
							
						} catch (Exception e) {
							System.out.println("thread blockport failed"+e);
						}
					}
					
				}
    			
    		}).start();
    	} catch (Exception e){
    		Log.d("blockport Exception",""+e);
    	}
    	
    }

public View printNetworkSettings(Context mContext, View rootView) {
	TextView ipAddress = new TextView(mContext);
	ListView tempView = (ListView) rootView;
	ArrayList<String> ipViewText = new ArrayList<String>();
	
	ArrayAdapter<String> adapter;
	adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, ipViewText);
	
	

	
	
	WifiManager wifiInfo = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
	DhcpInfo addr = wifiInfo.getDhcpInfo();
	
	//ipAddress.setText(intToIp(addr.ipAddress)+"\n");
	
	
	((ListView) rootView.findViewById(R.id.listItems)).setAdapter(adapter);
	
//	ipViewText.add("IP Address: "+intToIp(addr.ipAddress)+"\n");
//	ipViewText.add("Subnet Mask: " + intToIp(addr.netmask)+"\n");
//	ipViewText.add("Default Gateway: " + intToIp(addr.gateway)+"\n");
	ipViewText.add("Packets Blocked: " + "<number>" + "\n");
	ipViewText.add("IPv4:DNSServers: " + "<?>" + "\n");
	
	
	
	adapter.notifyDataSetChanged();
	
	// ((TextView) rootView.findViewById(android.R.id.text1)).setText(intToIp(addr.ipAddress)+"\n");
	 
	 return rootView;

}


	
}
