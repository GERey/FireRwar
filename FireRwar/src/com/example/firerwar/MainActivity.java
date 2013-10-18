package com.example.firerwar;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;



public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	
	String retIp = "";
	TextView ipAddress;
	static TextView ipAddress2;
	static TextView gatewayAddress;
	static TextView subnetMask;
	static TextView macAddress;
	static TextView dns1;
	static TextView dns2;
	
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	ViewPager mViewPager;
	
	
	public final static String EXTRA_MESSAGE = "com.example.firerwar.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.activity_main);
//			try {
//				initLayout();
//			} catch (UnknownHostException e) {
//				System.out.println("found the following in initLayout: " + e);
//			}
			
			
	        // Create the adapter that will return a fragment for each of the three primary sections
	        // of the app.
	        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
	        mAppSectionsPagerAdapter.setContext(this);

	        // Set up the action bar.
	        final ActionBar actionBar = getActionBar();

	        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
	        // parent.
	        //actionBar.setHomeButtonEnabled(false);

	        // Specify that we will be displaying tabs in the action bar.
	        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
	        // user swipes between sections.
	        mViewPager = (ViewPager) findViewById(R.id.pager);
	        mViewPager.setAdapter(mAppSectionsPagerAdapter);
	        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
	            @Override
	            public void onPageSelected(int position) {
	                // When swiping between different app sections, select the corresponding tab.
	                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
	                // Tab.
	                actionBar.setSelectedNavigationItem(position);
	            }
	        });

	        // For each of the sections in the app, add a tab to the action bar.
	        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
	            // Create a tab with text corresponding to the page title defined by the adapter.
	            // Also specify this Activity object, which implements the TabListener interface, as the
	            // listener for when this tab is selected.
	            actionBar.addTab(
	                    actionBar.newTab()
	                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
	                            .setTabListener(this));
	        }

        
        //setContentView(R.layout.activity_main);
    }
    
    protected void initLayout () throws UnknownHostException {
    	//View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
    	LinearLayout rootView = new LinearLayout(this);
    	//rootView.setOrientation(LinearLayout.VERTICAL);

    	
    	
    	    	
    	WifiManager wifiInfo = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    	DhcpInfo addr = wifiInfo.getDhcpInfo();
    	
    	
       	int ip = wifiInfo.getConnectionInfo().getIpAddress();
    	String mac = wifiInfo.getConnectionInfo().getMacAddress();

    	
    	ipAddress2 = new TextView(this);
        ipAddress2.setText(intToIp(addr.ipAddress)+"\n");
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
    	
    	//setContentView(rootView);
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
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /* in order for onclick to see it needs to be a couple of thigns
     * first it must be public
     * it must be void
     * and it must take in View and it's only parameter
     */
    public void sendMessage (View view){
    	
    	Intent intent = new Intent (this, DisplayMessageActivity.class);
    	
    	startActivity(intent);

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

    	Context mContext;
        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        
        public void setContext (Context mContext) {
        	this.mContext = mContext;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                	ipInfoShow ipContextPasser = new ipInfoShow();
                	ipContextPasser.setContext(mContext);
                    return ipContextPasser;

                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
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
            //TextView testip = new TextView(context);

            //container.addView
            

//            // Demonstration of a collection-browsing activity.
//            rootView.findViewById(R.id.demo_collection_button)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //Intent intent = new Intent(getActivity(), CollectionDemoActivity.class);
//                            //startActivity(intent);
//                        }
//                    });
//
//            // Demonstration of navigating to external activities.
//            rootView.findViewById(R.id.demo_external_activity)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Create an intent that asks the user to pick a photo, but using
//                            // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
//                            // the application from the device home screen does not return
//                            // to the external activity.
//                            Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
//                            externalActivityIntent.setType("image/*");
//                            externalActivityIntent.addFlags(
//                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                            startActivity(externalActivityIntent);
//                        }
//                    });

            return rootView;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);

            Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText("SECTION" + Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
    
    public static class ipInfoShow extends Fragment {

    	Context mContext;
    	
        public static final String ARG_SECTION_NUMBER = "section_number";

        public void setContext (Context mContext) {
        	this.mContext = mContext;
        }
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	
            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            
            WifiManager wifiInfo = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        	DhcpInfo addr = wifiInfo.getDhcpInfo();
        	Log.d("creating view","can't wait to work");
        	
           	int ip = wifiInfo.getConnectionInfo().getIpAddress();
        	String mac = wifiInfo.getConnectionInfo().getMacAddress();

        	
        	//rootView.setOrientation(LinearLayout.VERTICAL);
        	ipAddress2 = new TextView(mContext);
            ipAddress2.setText(intToIp(addr.ipAddress)+"\n");
        	container.addView(ipAddress2);
        	
        	 ((TextView) rootView.findViewById(android.R.id.text1)).setText(intToIp(addr.ipAddress)+"\n");

        	
        	return rootView;
        }
    }
    
}
    

