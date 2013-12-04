package com.example.firerwar;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.ArrayList;
import java.util.Enumeration;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	public int port_flag = 0;
	public int netinfo_flag = 0;
	public int whois_flag = 0;
	public int metrics_flag = 0;
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());
		mAppSectionsPagerAdapter.setContext(this);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		// TODO: check why the set home button doesn't work.
		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		// actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						//debug_flag=0;
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);
						
						if(position == 1) {
							if(whois_flag == 0) {
								
								/*this should only need to happen once, but not in on create...*/
										// 1. Instantiate an AlertDialog.Builder with its constructor
										AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

										// 2. Chain together various setter methods to set the dialog characteristics
										builder.setTitle("Whois")
										       .setMessage("Enter a link to query and press 'Lookup!' to get results");
										
										builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
									           public void onClick(DialogInterface dialog, int id) {
									               whois_flag = 1;
									           }
									       });

										// 3. Get the AlertDialog from create()
										AlertDialog dialog = builder.create();
										dialog.show();
										
							}
							
						}
						
						if(position == 2) {
							/*find a way to only display this once*/
							if(port_flag == 0) {
								
								/*this should only need to happen once, but not in on create...*/
										// 1. Instantiate an AlertDialog.Builder with its constructor
										AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

										// 2. Chain together various setter methods to set the dialog characteristics
										builder.setTitle("Port Blocker")
										       .setMessage("When setting ports please do not use ports between 0 -1024");
										
										builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
									           public void onClick(DialogInterface dialog, int id) {
									               port_flag = 1;
									           }
									       });

										// 3. Get the AlertDialog from create()
										AlertDialog dialog = builder.create();
										dialog.show();
										
							}
						}
						
						if(position == 3) {
							if(metrics_flag == 0) {
								
								/*this should only need to happen once, but not in on create...*/
										// 1. Instantiate an AlertDialog.Builder with its constructor
										AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

										// 2. Chain together various setter methods to set the dialog characteristics
										builder.setTitle("Metrics Viewer")
										       .setMessage("Click the button to get started!");
										
										builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
									           public void onClick(DialogInterface dialog, int id) {
									               metrics_flag = 1;
									           }
									       });

										// 3. Get the AlertDialog from create()
										AlertDialog dialog = builder.create();
										dialog.show();
										
							}
							
						}
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		// for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
		// Create a tab with text corresponding to the page title defined by the
		// adapter.
		// Also specify this Activity object, which implements the TabListener
		// interface, as the
		// listener for when this tab is selected.
		actionBar.addTab(actionBar.newTab().setText("NetInfo")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("WhoIs")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Ports")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Metrics")
				.setTabListener(this));
		// }

	}

	protected void initLayout() throws UnknownHostException {

	}

	/* took ip to string coverter off of old cpe464 assignment */
	public static String intToIp(int i) {
		return ((i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + ((i >> 24) & 0xFF));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		Context mContext;

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public void setContext(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0: /* NetInfo Screen */
				Log.d("Going to new screen", "fragment 0");
				ipInfoShow ipContextPasser = new ipInfoShow();
				ipContextPasser.setContext(mContext);
				return ipContextPasser;

			case 1: /* WhoIs Screen */
				Log.d("Going to new screen", "fragment 1");
				WhoIsInfo whoInfo = new WhoIsInfo();
				whoInfo.setContext(mContext);
				return whoInfo;

			case 2: /* Ports Screen */
				Log.d("Going to new screen", "fragment 2");
				portBlocker portInitBlock2 = new portBlocker();
				portInitBlock2.setContext(mContext);
				return portInitBlock2;

			default: /* Metrics Screen */
				// The other sections of the app are dummy placeholders.
				Log.d("Going to new screen", "fragment 3");
				// metricsViewer metrics = new metricsViewer();
				// metrics.setContext(mContext);
				return new LaunchpadSectionFragment();
			}
		}
	    /**
	     * A fragment that launches other parts of the demo application.
	     */
	    public static class LaunchpadSectionFragment extends Fragment {

	        @Override
	        public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                Bundle savedInstanceState) {
	            View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);

	            // Demonstration of a collection-browsing activity.
	            rootView.findViewById(R.id.demo_collection_button)
	                    .setOnClickListener(new View.OnClickListener() {
	                        @Override
	                        public void onClick(View view) {
	                            Intent intent = new Intent(getActivity(), metricsViewer.class);
	                            startActivity(intent);
	                        }
	                    });;

	            return rootView;
	        }
	    }

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Section " + (position + 1);
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_dummy,
					container, false);

			Bundle args = getArguments();
			((TextView) rootView.findViewById(android.R.id.text1))
					.setText("SECTION"
							+ Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	public static class ipInfoShow extends Fragment {

		Context mContext;
		public int netinfo_flag = 0;

		public static final String ARG_SECTION_NUMBER = "section_number";

		public void setContext(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.ip_text_info, container,
					false);

			return printNetworkSettings(mContext, rootView);
		}

		public View printNetworkSettings(Context mContext, View rootView) {
			// TextView ipAddress = new TextView(mContext);
			// ListView tempView = (ListView) rootView;
			
			ArrayList<String> ipViewText = new ArrayList<String>();

			ArrayAdapter<String> adapter;
			adapter = new ArrayAdapter<String>(mContext,
					android.R.layout.simple_list_item_1, ipViewText);

			WifiManager wifiInfo = (WifiManager) mContext
					.getSystemService(Context.WIFI_SERVICE);
			DhcpInfo addr = wifiInfo.getDhcpInfo();

			// ipAddress.setText(intToIp(addr.ipAddress)+"\n");

			((ListView) rootView.findViewById(R.id.listItems))
					.setAdapter(adapter);

			ipViewText.add("IP Address: " + intToIp(addr.ipAddress) + "\n");
			ipViewText.add("Subnet Mask: " + intToIp(addr.netmask) + "\n");
			ipViewText.add("Default Gateway: " + intToIp(addr.gateway) + "\n");
			ipViewText.add("Primary DNS: " + intToIp(addr.dns1) + "\n");
			ipViewText.add("Alt DNS: " + intToIp(addr.dns2) + "\n");

			adapter.notifyDataSetChanged();
			
			if(netinfo_flag == 0) {
				
				/*this should only need to happen once, but not in on create...*/
						// 1. Instantiate an AlertDialog.Builder with its constructor
						AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

						// 2. Chain together various setter methods to set the dialog characteristics
						builder.setTitle("Netinfo")
						       .setMessage("Slide to other screens or select a tab at the top to go to that screen");
						
						builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					               netinfo_flag = 1;
					           }
					       });

						// 3. Get the AlertDialog from create()
						AlertDialog dialog = builder.create();
						dialog.show();
						
			}
			

			// ((TextView)
			// rootView.findViewById(android.R.id.text1)).setText(intToIp(addr.ipAddress)+"\n");

			return rootView;

		}

	}

}
