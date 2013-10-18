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
import android.widget.ListView;
import android.widget.TextView;



public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	ViewPager mViewPager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.activity_main);

	        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
	        mAppSectionsPagerAdapter.setContext(this);

	        // Set up the action bar.
	        final ActionBar actionBar = getActionBar();

	        //TODO: check why the set home button doesn't work.
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

    }
    
    protected void initLayout () throws UnknownHostException {

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
        	
            View rootView = inflater.inflate(R.layout.fragment_section_dummy,container,false);

        	return printNetworkSettings(mContext, rootView);
        }
    
    public View printNetworkSettings(Context mContext, View rootView) {
    	//TODO: Change to listView , connect adapter and ADD TEXT VIEWS
    	TextView ipAddress = new TextView(mContext);
    	
    	WifiManager wifiInfo = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    	DhcpInfo addr = wifiInfo.getDhcpInfo();
    	
    	ipAddress.setText(intToIp(addr.ipAddress)+"\n");
    	
    	//((ListView) rootView.findViewById(R.id.listItems)).addView(ipAddress);
 	
    	 ((TextView) rootView.findViewById(android.R.id.text1)).setText(intToIp(addr.ipAddress)+"\n");
    	 
    	 return rootView;

    }
    
    }
    
}
    

