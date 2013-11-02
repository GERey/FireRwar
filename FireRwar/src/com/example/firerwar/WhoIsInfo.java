package com.example.firerwar;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WhoIsInfo extends Fragment {
	Context mContext;
	
	/** ViewGroup used for maintaining a list of views that displays all the whoIs info */
	//protected ListView whoIsLayout;
	
	/** EditText used for entering ip address to query */
	protected EditText queryString;
	
	protected String query;
	
	protected ArrayList<String> data;
	protected ArrayAdapter<String> adapter;
	
	/** Button used for querying entered address */
	//protected Button searchButton;

	public static final String ARG_SECTION_NUMBER = "GOOSE";

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_who_is_info, container, false);
		data = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_list_item_1, data);

		try {
			System.out.println("create whoIs");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return printNetworkSettings(mContext, rootView);
	}

	public View printNetworkSettings(Context mContext, View rootView) {
		//TextView portDisplay = (TextView) rootView.findViewById(R.id.Namer);
		//LinearLayout tempView = (LinearLayout) rootView
		//		.findViewById(R.id.LinearPortHolder);
		LinearLayout tempView = (LinearLayout)rootView.findViewById(R.id.LinearWhoIs);
		ListView whoIsLayout = (ListView) rootView.findViewById(R.id.whoIsListViewGroup);
		Button queryButton = (Button)rootView.findViewById(R.id.queryButton);
		queryString = (EditText)rootView.findViewById(R.id.queryText);
		
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
		//		android.R.layout.simple_list_item_1, data);
		
		queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String q = queryString.getText().toString();
                query = q;
                if (q != null && !q.equals("")) {
                	queryString.setText("");
                	Log.d("whoIs", "lookup " + q);
                	// TODO: now that we have the query string (q), set up the network stuff to get whoIs info on q!!
                	
                	printData();
                }
            }
        });
		
		queryString.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == android.view.KeyEvent.KEYCODE_ENTER ||
						keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER) {
					String q = queryString.getText().toString();
	                query = q;
	                if (q != null && !q.equals("")) {
	                	queryString.setText("");
	                	Log.d("whoIs", "lookup " + q);
	                	// TODO: now that we have the query string (q), set up the network stuff to get whoIs info on q!!
	                	
	                	printData();
	                }
	                return true;
				}
					
				return false;
			}
			
		});

		whoIsLayout.setAdapter(adapter);

		return tempView;

	}
	
	public void printData() {
		data.clear();
		data.add("Address Queried: " + query);
		data.add("OrgName: ");
		data.add("OrgId: ");
		data.add("Address: ");
		data.add("City: ");
		adapter.notifyDataSetChanged();
		Log.d("eloui", "added data!");
	}

}
