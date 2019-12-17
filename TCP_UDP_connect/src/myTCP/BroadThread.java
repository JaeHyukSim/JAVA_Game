package myTCP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.xml.stream.events.StartDocument;

public class BroadThread implements Observer, Runnable{
	private BufferedReader bf;
	private Station station;
	private DataOutputStream dataOut;
	private String data;
	private TCP_UserThread tcpUserThread;
	
	public BroadThread(BufferedReader br, Station st, DataOutputStream d,TCP_UserThread tc) {
		this.bf = br; this.station = st; this.dataOut = d;
		station.registerObserver(this);
		System.out.println("BroadThread - Station - connected");
		this.tcpUserThread = tc;
	}
	
	@Override
	public void run() {
		
	}
	
	public void update(String s) {
		try {
			data = s;
			tcpUserThread.chargeBuffer(data);
		}catch(Exception e) {
			
		}
	}
	
}
