package com.example.firerwar;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

	private static final String DATABASE_NAME ="portdatabase";
	
	private static final String TABLE_PORTS = "ports";
	
	private static final int DATABASE_VERSION = 1;
	
	public static final String PORT_KEY_ID = "_id";
	public static final String PORT_NUM = "port_num";
	

	public static final String DATABASE_DROP = "drop table if exists " + TABLE_PORTS;
	

	
	   public DatabaseManager(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	       
	    }
		

	@Override
	public void onCreate(SQLiteDatabase databoots) {
		databoots = this.getWritableDatabase();
		String CREATE_PORTS = "CREATE TABLE " + TABLE_PORTS
				+ " (" + PORT_KEY_ID + " INTEGER PRIMARY KEY, " +
				PORT_NUM + " INTEGER UNIQUE " + ")";
		
		Log.d("Database","created table to databoots");
		databoots.execSQL(CREATE_PORTS);
								 
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase databoots, int oldVersion, int newVersion) {
		
		Log.w("DatabaseManager.java", "Database being upgraded from " + Integer.toString(oldVersion)
				+" to " + Integer.toString(newVersion));
		databoots.execSQL(DATABASE_DROP);
		onCreate(databoots);
		
	}
	
	public void addPort(int port) {
		
		SQLiteDatabase databoots = this.getWritableDatabase();
		ContentValues vals = new ContentValues();
		vals.put(PORT_NUM, port);
		Log.d("Database","added port to databoots");

		
		databoots.insert(TABLE_PORTS, null, vals);
		databoots.close();
		
	}
	
	public int getPort (int id) {
		SQLiteDatabase databoots = this.getReadableDatabase();
		
		Cursor cursor = databoots.query(TABLE_PORTS, new String[] {PORT_KEY_ID,PORT_NUM},
				PORT_KEY_ID + "=?",new String[] {String.valueOf(id)},null,null,null,null);
		
		if (cursor != null)
			cursor.moveToFirst();
		/* retrieves the port number and returns it  */
		return cursor.getInt(1);
	}
	
	public ArrayList<String> getAllPorts() {
		/*Might need to be writable, but not sure why*/
		SQLiteDatabase databoots = this.getReadableDatabase();
		ArrayList<String> portList = new ArrayList<String>();
		
		String selection = "select * from "+ TABLE_PORTS;
		Cursor cursor = databoots.rawQuery(selection, null);
		
		if(cursor !=null && cursor.moveToFirst()){
			do {
				portList.add(Integer.toString(cursor.getInt(1)));
			}while (cursor.moveToNext());
		}
		
		return portList;
	}
	/*maybe have collective tables of ports with status indicators on which
	 * are opened and which are closed*/
	/* ports should be unique in that they only need to be added to
	 * the table once */
	public int updatePortStatus(int port) {
		SQLiteDatabase databoots = this.getWritableDatabase();
		
		ContentValues vals = new ContentValues();
		vals.put(PORT_NUM, port);
		/* might need the id in the list for this value, i believe we can find it, but
		 * really there should only ever be a need to update a port status
		 * not a port*/
		int dataReturned = databoots.update(TABLE_PORTS, vals, PORT_KEY_ID+ " = ?", new String[] {Integer.toString(port)});
		return dataReturned;
	}
	
	public void deletePort (int delPort) {
		SQLiteDatabase databoots = this.getWritableDatabase();
		databoots.delete(TABLE_PORTS, PORT_KEY_ID + " = ?", new String[] {String.valueOf(delPort)});
	}
	
	public int getPortCount () {
		SQLiteDatabase databoots = this.getReadableDatabase();
		String numPorts = "SELECT * FROM " + TABLE_PORTS;
		Cursor cursor = databoots.rawQuery(numPorts, null);
		
		return cursor.getCount();
	}

}
