package com.example.firerwar;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
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

public class vpnIntentMetrics extends VpnService implements Handler.Callback,
		Runnable {
	private static final String TAG = "ToyVpnServiceUnWorking";
	public String localInet = null;

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
		mThread = new Thread(this, "vpnIntentMetrics");
		mThread.start();
		return START_STICKY;
	}

	@Override
	public void onCreate() {
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
		Log.i(TAG, "running vpnService");
		try {
			runVpnConnection();
		} catch (Exception e) {
			e.printStackTrace();
			// Log.e(TAG, "Got " + e.toString());
		} finally {
			try {
				mInterface.close();
			} catch (Exception e) {
				// ignore
			}
			mInterface = null;

			// mHandler.sendEmptyMessage(R.string.disconnected);
			Log.i(TAG, "Exiting");
		}
	}

	/*
	 * Network to Host order bytes takes in an arry of just a single byte and
	 * outputs
	 */
	private byte convertntohb(byte[] value) {
		ByteBuffer buf = ByteBuffer.wrap(value);
		return buf.get();
	}

	private short convertntohs(byte[] value) {
		ByteBuffer buf = ByteBuffer.wrap(value);
		return buf.get();
	}

	/*
	 * using the following to decode ip address please optimize this later as it
	 * was taken from the following source
	 * http://kodejava.org/how-do-i-convert-raw-ip-address-to-string/
	 */
	public String getIpAddress(byte[] rawBytes) {
		int i = 4;
		String ipAddress = "";
		for (byte raw : rawBytes) {
			ipAddress += (raw & 0xFF);
			if (--i > 0) {
				ipAddress += ".";
			}
		}

		return ipAddress;
	}

	public int bytestoUnsigned(short signedVal) {
		return signedVal & 0xFFFF;
	}

	private boolean runVpnConnection() throws Exception {

		configure();
		//localInet = getLocalIpAddress();
		Socket passer = new Socket();
		//boolean isProtected = protect(passer);
		DatagramSocket tunnel = new DatagramSocket();
		byte typeVal;
		
		//boolean connected = false;
		try {
			// Create a DatagramChannel as the VPN tunnel.
			//tunnel = DatagramChannel.open();

			// Protect the tunnel before connecting to avoid loopback.
//			if (!protect(tunnel.socket())) {
//				throw new IllegalStateException("Cannot protect the tunnel");
//			}
			// Socket temp = new Socket("10.151.42.155",2223);
			// SocketAddress tempSocket = new
			// InetSocketAddress("207.62.170.219",8080);

			// Connect to the server.

			// For simplicity, we use the same thread for both reading and
			// writing. Here we put the tunnel into non-blocking mode.

			// Authenticate and configure the virtual network interface.
			// handshake(tunnel);

			// Now we are connected. Set the flag and show the message.
			// connected = true;
			//tunnel.configureBlocking(false);
			// mHandler.sendEmptyMessage(R.string.connected);

			// Packets to be sent are queued in this input stream.
			FileInputStream in = new FileInputStream(
					mInterface.getFileDescriptor());
			

			// Packets received need to be written to this output stream.
			FileOutputStream out = new FileOutputStream(
					mInterface.getFileDescriptor());
			
			
			
			
			

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
			byte[] srcPort = new byte[2];
			byte[] dstPort = new byte[2];

			// We use a timer to determine the status of the tunnel. It
			// works on both sides. A positive value means sending, and
			// any other means receiving. We start with receiving.
			int timer = 0;
//			tunnel.connect(new InetSocketAddress("129.65.110.44", 3022));
//			if (!protect(tunnel)) {
//				throw new IllegalStateException("Cannot protect the tunnel");
//			}
			
			// We keep forwarding packets till something goes wrong.
			while (true) {
				
				

				// Assume that we did not make any progress in this iteration.
				boolean idle = true;

				// Read the outgoing packet from the input stream.
				int length = in.read(packet.array());
				
				packet.get(packetHld);
				//out.write(packetHld);
				packet.position(0);

				// tunnel.connect(tempSocket);
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
					int currentBytesRead = 20;

					int version = versionIHL[0];
					int versionNum = version >> 4;
					int ihl = versionIHL[0];
					ihl = ihl & 0x000F;
					short srcPortL;
					short dstPortL;

					typeVal = convertntohb(protocol);

					// if ((ihl*4) != 20){
					int temp = packet.position();
					packet.position(ihl * 4);
					temp = packet.position();
					// }
					// udp
					if (typeVal == 17) {
						packet.get(srcPort);
						packet.get(dstPort);
						String srcIpStr = getIpAddress(srcIp);
						String dstIpStr = getIpAddress(dstIp);

						srcPortL = convertntohs(srcPort);
						dstPortL = convertntohs(dstPort);

						int srcPortUnsigned = bytestoUnsigned(srcPortL);
						int dstPortUnsigned = bytestoUnsigned(dstPortL);
						

						packet.position(0);
						DatagramPacket packer = new DatagramPacket(packet.array(),
						length);
						

						

						
						tunnel.connect(new InetSocketAddress(dstIpStr, 80));
						if (!protect(tunnel)) {
							throw new IllegalStateException("Cannot protect the tunnel");
						}
				        tunnel.send(packer);
				        //tunnel.close();
				        
				        System.out.println("udp sent");
				        
						

					}
					// tcp
					if (typeVal == 6) {
						packet.get(srcPort);
						packet.get(dstPort);
						String srcIpStr = getIpAddress(srcIp);
						String dstIpStr = getIpAddress(dstIp);

						srcPortL = convertntohs(srcPort);
						dstPortL = convertntohs(dstPort);

						int srcPortUnsigned = bytestoUnsigned(srcPortL);
						int dstPortUnsigned = bytestoUnsigned(dstPortL);


						packet.position(0);
						Socket sender = new Socket(dstIpStr,80);
						//tunnel.connect(new InetSocketAddress(dstIpStr, 80));
						if (!protect(tunnel)) {
							throw new IllegalStateException("Cannot protect the tunnel");
						}
						tunnel.send(new DatagramPacket(packet.array(), length));
						//tunnel.close();
						System.out.println("tcp Sent");


					}


					packet.limit(length);

					packet.clear();

					// There might be more outgoing packets.
					idle = false;

					// If we were receiving, switch to sending.
					if (timer < 1) {
						timer = 1;
					}
				}
				//recieving
				if (timer == 1) {
					DatagramPacket pack = new DatagramPacket(packetHld, length);
					
					timer = 0;
					System.out.println("recieving now!");
					
					
					
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
							// tunnel.write(packet);
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
				//tunnel.close();
			} catch (Exception e) {
				// ignore
			}
		}
		return true;
	}

	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					Log.i(TAG, "****** INET ADDRESS ******");
					Log.i(TAG, "address: " + inetAddress.getHostAddress());
					Log.i(TAG, "hostname: " + inetAddress.getHostName());
					Log.i(TAG, "address.toString(): "
							+ inetAddress.getHostAddress().toString());
					if (!inetAddress.isLoopbackAddress()) {
						// IPAddresses.setText(inetAddress.getHostAddress().toString());
						Log.i(TAG, "IS NOT LOOPBACK ADDRESS: "
								+ inetAddress.getHostAddress().toString());
						return inetAddress.getHostAddress().toString();
					} else {
						Log.i(TAG, "It is a loopback address");
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
		builder.addAddress("10.8.0.1", 32);
		//builder.addAddress("129.65.110.44", 23);
		builder.setSession("FireRwar");
		//builder.addAddress("192.168.1.13", 24);

		builder.addRoute("0.0.0.0", 0);
		try {
			mInterface.close();
		} catch (Exception e) {
			Log.d("Exception", "The minterface closed" + e);
		}

		mInterface = builder.establish();
	}

}
