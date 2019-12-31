package Client;

import java.io.IOException;
import java.net.Socket;

public class Client {
	
	final static int portNum = 16789;
	public static void main(String[] args) {
		try {
			Socket socket;
			socket = new Socket("211.238.142.202",portNum);
			
			/*
			Runnable userInputThread = new UserInputThread(socket);
			Thread userThread = new Thread(userInputThread);
			Runnable userServerInputThread = new UserServerInputThread(socket);
			Thread serverThread = new Thread(userServerInputThread);
			Runnable displayThread = new DisplayThread();
			Thread display = new Thread(displayThread);
			userThread.start();
			serverThread.start();
			display.start();
			*/
		}catch(IOException e) {
			System.out.println("in Client -> error : socket");
		}
	}

}
