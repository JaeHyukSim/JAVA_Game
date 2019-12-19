package myTCP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCP_ConnectionThread implements Runnable{
	
	private ServerSocket sSocket;
	private int portNum;
	private Socket socket;
	private int count;
	private Station station;
	
	public TCP_ConnectionThread(ServerSocket inSocket, int port) {
		this.sSocket = inSocket;
		this.portNum = port;
		count = 1;
		station = new Station();
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				socket = sSocket.accept();
				
				//User thread研 持失馬切
				Runnable r = new TCP_UserThread(count, socket, station);
				Thread tr = new Thread(r);
				
				tr.start();
				
				count++;
			}
		}catch(IOException e) {
			System.out.println("server close");
		}
	}
}
