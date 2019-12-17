package myTCP;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class TCP_server {

	public static void main(String[] args) throws Exception{
		ServerSocket welcome;
		
		final int portNum = 16789;
		try {
			System.out.println("Server - is Operating!");
			welcome = new ServerSocket(portNum);
			Runnable r = new TCP_ConnectionThread(welcome, portNum);
			Thread t = new Thread(r);
			t.start();
			
			
		}catch (IOException e) {
			System.out.println("exception : IOE exception");
		}
	}

}
