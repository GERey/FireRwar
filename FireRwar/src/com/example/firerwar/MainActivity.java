package com.example.firerwar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Enumeration;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	String retIp = "";
	TextView ipAddress;
	TextView ipAddress2;
	TextView gatewayAddress;
	TextView subnetMask;
	TextView macAddress;
	TextView dns1;
	TextView dns2;
	
	
	//TODO: Remove AsyncTask
	class getIp extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			
				/*code from the internet */
			    StringBuilder IFCONFIG=new StringBuilder();
			    String ipRet = "";
			    try {
			    	Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			    	while(en.hasMoreElements()){
			    		NetworkInterface netIntr = en.nextElement();
			    		Enumeration<InetAddress> ipAdrs = netIntr.getInetAddresses();
			    		while(ipAdrs.hasMoreElements()){
			    			InetAddress tempIps = ipAdrs.nextElement();
			    			if(!tempIps.isLinkLocalAddress() && !tempIps.isLoopbackAddress()){
			    				ipRet = tempIps.getHostAddress();
			    				
			    			}
			    		}
			    		
			    		
			    	}
			    }
			    catch (Exception e){
			    	System.out.println("Error in doInBackground funct: "+ e);
			    }
			    	
			    
				return ipRet;
				
		}
		
		protected void onPostExecute(String ipAddrs){
			
			retIp = ipAddrs;
			System.out.println(retIp);
			ipAddress.setText(retIp);

		}
		
		
	}
	
	public final static String EXTRA_MESSAGE = "com.example.firerwar.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
			initLayout();
		} catch (Exception e) {
			System.out.println(e+ ": Error due to unknown host");
		}
        
        //setContentView(R.layout.activity_main);
    }
    
    protected void initLayout () throws UnknownHostException {
    	LinearLayout rootView = new LinearLayout(this);
    	rootView.setOrientation(LinearLayout.VERTICAL);

    	
    	
    	    	
    	WifiManager wifiInfo = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    	DhcpInfo addr = wifiInfo.getDhcpInfo();
//    	
//    	System.out.println(String.valueOf(addr.ipAddress));
//    	
//    	Log.d("DHCP infomation",String.valueOf(addr.ipAddress));
//    	
//    	ipAddress.setText(String.valueOf(addr.ipAddress));
//    	rootView.addView(ipAddress);
//    	
//    	subnetMask.setText(String.valueOf(addr.netmask));
//    	rootView.addView(subnetMask);
//    	
//    	gatewayAddress.setText(String.valueOf(addr.gateway));
//    	rootView.addView(gatewayAddress);
//    	
//    	setContentView(rootView);
    	
    	
    	
       	int ip = wifiInfo.getConnectionInfo().getIpAddress();
    	String mac = wifiInfo.getConnectionInfo().getMacAddress();

    	
    	ipAddress2 = new TextView(this);
        ipAddress2.setText(intToIp(addr.ipAddress)+"\n");//setText(intToIp(ip)+"\n");
    	rootView.addView(ipAddress2);
    	
    	subnetMask = new TextView(this);
    	subnetMask.setText(intToIp(addr.netmask));
    	rootView.addView(subnetMask);

    	gatewayAddress = new TextView(this);
    	gatewayAddress.setText(intToIp(addr.gateway) + "\n");
    	rootView.addView(gatewayAddress);
    	
    	macAddress = new TextView(this);
    	macAddress.setText(mac + "\n");
    	rootView.addView(macAddress);
    	
    	dns1 = new TextView(this);
    	dns1.setText(intToIp(addr.dns1));
    	rootView.addView(dns1);
    	
    	dns2 = new TextView(this);
    	dns2.setText(intToIp(addr.dns2));
    	rootView.addView(dns2);
    	
    	
    	
    	
    	
    	
//    	ipAddress = new TextView(this);
//    	
//    	new getIp().execute(retIp);
    	
    	//ipAddress.setText(retIp);
    	
//    	rootView.addView(ipAddress);
    	setContentView(rootView);
    }
    /*took ip to string coverter off of old cpe464 assignment */
    public static String intToIp(int i) {
        return ((i  & 0xFF) + "." +
               ((i >> 8 ) & 0xFF) + "." +
               ((i >> 16 ) & 0xFF) + "." +
               ( (i >> 24)   & 0xFF));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /* in order for onclick to see it needs to be a couple of thigns
     * first it must be public
     * it must be void
     * and it must take in View and it's only parameter
     */
    public void sendMessage (View view){
    	
    	Intent intent = new Intent (this, DisplayMessageActivity.class);
    	
    	EditText editText = (EditText)findViewById(R.id.edit_message);
    	EditText edit2Text = (EditText)findViewById(R.id.edit_message);

    	String message = editText.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE, message);
    	
    	startActivity(intent);
    	
    	//derp
    }
    
}
