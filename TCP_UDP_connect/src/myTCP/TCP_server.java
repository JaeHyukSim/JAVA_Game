package myTCP;

import java.io.IOException;
import java.net.ServerSocket;

public class TCP_server {

	public static void main(String[] args) throws Exception{
		ServerSocket welcome;
		final int portNum = 16789;
		try {
			welcome = new ServerSocket(portNum);
			Runnable r = new TCP_ConnectionThread(welcome, portNum);
			Thread t = new Thread(r);
			t.start();
			
			
		}catch (Exception e) {
			System.out.println("exception : IOE exception");
		}
	}

}
