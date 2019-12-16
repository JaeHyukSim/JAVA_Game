package myTCP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCP_ConnectionThread implements Runnable{
	
	private ServerSocket sSocket;
	private int portNum;
	private Socket socket;
	private int count;
	
	
	public TCP_ConnectionThread(ServerSocket inSocket, int port) {
		this.sSocket = inSocket;
		count = 1;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				socket = sSocket.accept();
				System.out.println(count + ". user : connected!");
				
				//User thread研 持失馬切
				
				Runnable r = new TCP_UserThread(count, socket);
				Thread tr = new Thread(r);
				tr.start();
				
				count++;
			}
		}catch(IOException e) {
			System.out.println("server close");
		}
	}
}
