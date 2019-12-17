package myTCP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
class BroadCastThread implements Runnable{
	private ArrayList<Thread> arr;
	private int count;
	
	public BroadCastThread() {
		count = 0;
		arr = new ArrayList<Thread>();
	}
	public void getThread(Thread threadData) {
		count++;
		arr.add(threadData);
	}
	
	public void run() {
		try {
			for(Thread data : arr) {
				
			}
		}catch(IOException e) {
			
		}
	}
	
}
*/
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
			
			/*
			Runnable broad = new BroadCastThread();
			Thread broadCast = new Thread(broad);
			broadCast.start();
			*/
			while(true) {
				socket = sSocket.accept();
				System.out.println(count + ". user : connected!");
				
				//User thread를 생성하자
				//쓰레드 개수는 count - 1
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
