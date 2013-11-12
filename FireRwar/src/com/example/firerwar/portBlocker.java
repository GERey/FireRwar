package com.example.firerwar;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class portBlocker extends Fragment {
	ServerSocket sock;
	Socket temp;
	public EditText portText;
	public ArrayList<String> tcpViewText;
	public ArrayList<String> udpViewText;
	public ArrayAdapter<String> adapter;
	public ArrayAdapter<String> adapter2;

	Context mContext;

	protected int greenText;
	protected int redText;
	
	public static final String ARG_SECTION_NUMBER = "BOOP";

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.port_enter, container, false);
		greenText = R.color.openGreen;
		redText = R.color.blockRed;

		try {
			// openport(2222);
			// blockport(2222);
			System.out.println("derp");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return printNetworkSettings(mContext, rootView);
	}

	public void blockport(final int port) throws IOException {
		// sock = new ServerSocket();
		temp = new Socket();

		try {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// while(true) {
					try {

						if (!sock.isClosed()) {
							if (!sock.isBound()) {
								sock = new ServerSocket(port);
								sock.close();
							} else
								sock.close();

							// temp = new Socket("10.", port);
							// temp.setReuseAddress(true);
							// temp.close();
						}

					} catch (Exception e) {
						System.out.println("thread blockport failed" + e);
					}
					// }

				}

			}).start();
		} catch (Exception e) {
			Log.d("blockport Exception", "" + e);
		}

	}

	public void openport(final int port) throws IOException {
		sock = new ServerSocket();

		try {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {

						if (!sock.isBound()) {
							sock = new ServerSocket(port);

							sock.accept();

						}

					} catch (Exception e) {
						System.out.println("thread openport failed: " + e);
					}

				}

			}).start();
		} catch (Exception e) {
			Log.d("openport Exception", "" + e);
		}

	}

	public View printNetworkSettings(Context mContext, View rootView) {
		TextView tcpDisplay = (TextView) rootView.findViewById(R.id.TCPports);
		TextView udpDisplay = (TextView) rootView.findViewById(R.id.UDPports);
		LinearLayout tempView = (LinearLayout) rootView.findViewById(R.id.LinearPortHolder);
		ListView tcpPortsList = (ListView) rootView.findViewById(R.id.PortItems);
		ListView udpPortsList = (ListView) rootView.findViewById(R.id.UDPItems);
		
		Button closeTCPButton = (Button) rootView.findViewById(R.id.portClosedButton);
		Button openTCPButton = (Button) rootView.findViewById(R.id.portOpenButton);
		Button closeUDPButton = (Button)rootView.findViewById(R.id.UDPcloseButton);
		Button openUDPButton = (Button)rootView.findViewById(R.id.UDPopenButton);
		
		portText = (EditText) rootView.findViewById(R.id.portText);
		portText.setRawInputType(Configuration.KEYBOARD_12KEY);

		tcpViewText = new ArrayList<String>();
		udpViewText = new ArrayList<String>();

		tcpDisplay.setText("TCP Ports (blocked=red, open=green)");
		udpDisplay.setText("UDP Ports (blocked=red, open=green)");
		// tempView.addView(portDisplay);

		// tempView.addView(ipAddress);

		adapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_list_item_1, tcpViewText);
		adapter2 = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_list_item_1, udpViewText);

		// ipAddress.setText(intToIp(addr.ipAddress)+"\n");

		// tempView = ((LinearLayout) rootView.findViewById(R.id.listItems));

		tcpPortsList.setAdapter(adapter);
		udpPortsList.setAdapter(adapter2);
		// tempView.addView(listerPorts);

		// ipViewText.add("");
		// udpViewText.add("udp stuff here");
		// TODO: make buttons take in data and do correct stuff...
		closeTCPButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String portHold = portText.getText().toString();
				int i;
				
				try {
					int port = Integer.parseInt(portHold);
					
					for (i = 0; i < tcpViewText.size(); i++) {
						if (tcpViewText.get(i).contains(portHold)) {
							tcpViewText.remove(i);
							break;
						}
					}
					
					tcpViewText.add(portHold + " blocked");
					adapter.notifyDataSetChanged();

					// TODO add error checking for the above here

					blockport(port);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e + " failed in block port");
				} catch (Exception e) {
					System.out.println("failed in int conversion: " + e);
				}

			}
		});

		openTCPButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String portHold = portText.getText().toString();
				int i;
				
				try {
					int port = Integer.parseInt(portHold);
					
					for (i = 0; i < tcpViewText.size(); i++) {
						if (tcpViewText.get(i).contains(portHold)) {
							tcpViewText.remove(i);
							break;
						}
					}
					tcpViewText.add(portHold + " opened");
					adapter.notifyDataSetChanged();

					// TODO add error checking for the above here

					openport(port);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e + " failed in open port");
				} catch (Exception e) {
					System.out.println("failed in open int conversion: " + e);
				}

			}
		});
		
		closeUDPButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String portHold = portText.getText().toString();
				int i;
				
				try {
					int port = Integer.parseInt(portHold);
					
					for (i = 0; i < udpViewText.size(); i++) {
						if (udpViewText.get(i).contains(portHold)) {
							udpViewText.remove(i);
							break;
						}
					}
					udpViewText.add(portHold + " blocked");
					adapter2.notifyDataSetChanged();

					// TODO add error checking for the above here

					blockport(port);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e + " failed in block port");
				} catch (Exception e) {
					System.out.println("failed in int conversion: " + e);
				}
			}
			
		});

		openUDPButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String portHold = portText.getText().toString();
				int i;
				
				try {
					int port = Integer.parseInt(portHold);
					
					for (i = 0; i < udpViewText.size(); i++) {
						if (udpViewText.get(i).contains(portHold)) {
							udpViewText.remove(i);
							break;
						}
					}
					
					udpViewText.add(portHold + " opened");
					adapter2.notifyDataSetChanged();

					// TODO add error checking for the above here

					openport(port);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e + " failed in open port");
				} catch (Exception e) {
					System.out.println("failed in open int conversion: " + e);
				}
			}
			
		});
		
		adapter.notifyDataSetChanged();
		adapter2.notifyDataSetChanged();
		// ((TextView)
		// rootView.findViewById(android.R.id.text1)).setText(intToIp(addr.ipAddress)+"\n");

		return tempView;

	}

}
