package com.example.firerwar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Enumeration;


import android.net.VpnService;
import android.net.VpnService.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class vpnIntentMetrics extends VpnService implements Handler.Callback, Runnable {
	private static final String TAG = "ToyVpnServiceUnWorking";

	private Handler mHandler;
	private Thread mThread;

	private ParcelFileDescriptor mInterface;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    // The handler is only used to show messages.
	    if (mHandler == null) {
	        mHandler = new Handler(this);
	    }

	    // Stop the previous session by interrupting the thread.
	    if (mThread != null) {
	        mThread.interrupt();
	    }

	    // Start a new session by creating a new thread.
	    mThread = new Thread(this, "ToyVpnThread");
	    mThread.start();
	    return START_STICKY;
	}
	
	@Override
	public void onCreate(){
		Toast.makeText(this, "RISSESE MY FRIEND", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "GONE! DESTROYYY", Toast.LENGTH_LONG).show();
	    if (mThread != null) {
	        mThread.interrupt();
	    }
	}

	@Override
	public boolean handleMessage(Message message) {
	    if (message != null) {
	        Toast.makeText(this, message.what, Toast.LENGTH_SHORT).show();
	    }
	    return true;
	}

	@Override
	public synchronized void run() {
	    Log.i(TAG,"running vpnService");
	    try {
	        runVpnConnection();
	    } catch (Exception e) {
	        e.printStackTrace();
	        //Log.e(TAG, "Got " + e.toString());
	    } finally {
	        try {
	            mInterface.close();
	        } catch (Exception e) {
	            // ignore
	        }
	        mInterface = null;

	        //mHandler.sendEmptyMessage(R.string.disconnected);
	        Log.i(TAG, "Exiting");
	    }
	}

	private byte convertntohs(byte[] value) {
	    ByteBuffer buf = ByteBuffer.wrap(value);
	    return buf.get();
	}

	private boolean runVpnConnection() throws Exception {

	    configure();
	    DatagramChannel tunnel = null;
	    byte typeVal;
	    boolean connected = false;
	    try {
	        // Create a DatagramChannel as the VPN tunnel.
	        tunnel = DatagramChannel.open();

	        // Protect the tunnel before connecting to avoid loopback.
	        if (!protect(tunnel.socket())) {
	            throw new IllegalStateException("Cannot protect the tunnel");
	        }
	       // Socket temp = new Socket("10.151.42.155",2223);
	        //SocketAddress tempSocket = new InetSocketAddress("207.62.170.219",8080);
	        
	        // Connect to the server.

	        // For simplicity, we use the same thread for both reading and
	        // writing. Here we put the tunnel into non-blocking mode.
	        

	        // Authenticate and configure the virtual network interface.
	        //handshake(tunnel);

	        // Now we are connected. Set the flag and show the message.
	        //connected = true;
	        tunnel.configureBlocking(false);
	        //mHandler.sendEmptyMessage(R.string.connected);

	        // Packets to be sent are queued in this input stream.
	        FileInputStream in = new FileInputStream(mInterface.getFileDescriptor());

	        // Packets received need to be written to this output stream.
	        FileOutputStream out = new FileOutputStream(mInterface.getFileDescriptor());


	        // Allocate the buffer for a single packet.
	        ByteBuffer packet = ByteBuffer.allocate(32767);
	        byte[] packetHld = new byte[32767];
	        byte[] versionIHL = new byte[1];
	        byte[] DSCPECN = new byte[1];
	        byte[] totalLength = new byte[2];
	        byte[] identification = new byte[2];
	        byte[] flagFragOffset = new byte[2];
	        byte[] ttl = new byte[1];
	        byte[] protocol = new byte[1];
	        byte[] headerChksm = new byte[2];
	        byte[] srcIp = new byte[4];
	        byte[] dstIp = new byte[4];
	        
	        
	       


	        // We use a timer to determine the status of the tunnel. It
	        // works on both sides. A positive value means sending, and
	        // any other means receiving. We start with receiving.
	        int timer = 0;

	        // We keep forwarding packets till something goes wrong.
	        while (true) {
	            // Assume that we did not make any progress in this iteration.
	            boolean idle = true;

	            // Read the outgoing packet from the input stream.
	            int length = in.read(packet.array());

	            //tunnel.connect(tempSocket);
	            if (length > 0) {
	               // packet.get(packetHld);
	            	packet.get(versionIHL);
	            	packet.get(DSCPECN);
	            	packet.get(totalLength);
	            	packet.get(identification);
	            	packet.get(flagFragOffset);
	            	packet.get(ttl);
	            	packet.get(protocol);
	            	packet.get(headerChksm);
	            	packet.get(srcIp);
	            	packet.get(dstIp);
	            	
	            	 typeVal = convertntohs(protocol);
	            	
	            	
	            	
	                
	                //DatagramPacket infoPak = new DatagramPacket(packetHld, length);
	                //System.out.println(Integer.toString(infoPak.getPort()));
	                // Write the outgoing packet to the tunnel.
	            	//System.out.println(type.toString());
	                packet.limit(length);
	                out.write(packet.array(), 0, length);

	                //tunnel.connect(tempSocket);
	                //protect(tunnel.socket());
	                //tunnel.write(packet);
	                packet.clear();

	                // There might be more outgoing packets.
	                idle = false;

	                // If we were receiving, switch to sending.
	                if (timer < 1) {
	                    timer = 1;
	                }
	            }
	            out.write(packet.array(), 0, length);

	            // Read the incoming packet from the tunnel.
	            length =0;//= tunnel.read(packet);
	            if (length > 0) {
	                // Ignore control messages, which start with zero.
	                if (packet.get(0) != 0) {
	                    // Write the incoming packet to the output stream.
	                    out.write(packet.array(), 0, length);
	                }
	                packet.clear();

	                // There might be more incoming packets.
	                idle = false;

	                // If we were sending, switch to receiving.
	                if (timer > 0) {
	                    timer = 0;
	                }
	            }

	            // If we are idle or waiting for the network, sleep for a
	            // fraction of time to avoid busy looping.
	            if (idle) {
	                Thread.sleep(100);

	                // Increase the timer. This is inaccurate but good enough,
	                // since everything is operated in non-blocking mode.
	                timer += (timer > 0) ? 100 : -100;

	                // We are receiving for a long time but not sending.
	                if (timer < -15000) {
	                    // Send empty control messages.
	                    packet.put((byte) 0).limit(1);
	                    for (int i = 0; i < 3; ++i) {
	                        packet.position(0);
	                        //tunnel.write(packet);
	                    }
	                    packet.clear();

	                    // Switch to sending.
	                    timer = 1;
	                }

	                // We are sending for a long time but not receiving.
	                if (timer > 20000) {
	                    throw new IllegalStateException("Timed out");
	                }
	            }
	        }
	    } catch (InterruptedException e) {
	        throw e;
	    } catch (Exception e) {
	        Log.e(TAG, "Got " + e.toString());
	    } finally {
	        try {
	            tunnel.close();
	        } catch (Exception e) {
	            // ignore
	        }
	    }
	    return connected;
	}

	public String getLocalIpAddress()
	{
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                Log.i(TAG,"****** INET ADDRESS ******");
	                Log.i(TAG,"address: "+inetAddress.getHostAddress());
	                Log.i(TAG,"hostname: "+inetAddress.getHostName());
	                Log.i(TAG,"address.toString(): "+inetAddress.getHostAddress().toString());
	                if (!inetAddress.isLoopbackAddress()) {
	                    //IPAddresses.setText(inetAddress.getHostAddress().toString());
	                    Log.i(TAG,"IS NOT LOOPBACK ADDRESS: "+inetAddress.getHostAddress().toString());
	                    return inetAddress.getHostAddress().toString();
	                } else{
	                    Log.i(TAG,"It is a loopback address");
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        String LOG_TAG = null;
	        Log.e(LOG_TAG, ex.toString());
	    }

	    return null;
	}

	private void configure() throws Exception {
	    // If the old interface has exactly the same parameters, use it!
	    if (mInterface != null) {
	        Log.i(TAG, "Using the previous interface");
	        return;
	    }

	    // Configure a builder while parsing the parameters.
	    Builder builder = new Builder();
	    builder.addAddress("10.151.42.155", 24);
	    //builder.addAddress("192.168.1.13", 24);

	    builder.addRoute("0.0.0.0",0);
	    try {
	        mInterface.close();
	    } catch (Exception e) {
	        Log.e("Exception","The minterface closed" +e);
	    }

	    mInterface = builder.establish();
	}

}
