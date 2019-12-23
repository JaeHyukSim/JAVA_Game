package Client;

import java.io.IOException;
import java.net.Socket;

public class Client {
	
	final static int portNum = 12345;
	public static void main(String[] args) {
		try {
			Socket socket;
			socket = new Socket("localHost",portNum);
			
			Runnable userInputThread = new UserInputThread(socket);
			Thread userThread = new Thread(userInputThread);
			Runnable userServerInputThread = new UserServerInputThread();
			Thread serverThread = new Thread(userServerInputThread);
			Runnable displayThread = new DisplayThread();
			Thread display = new Thread(displayThread);
			userThread.start();
			serverThread.start();
			display.start();
			
		}catch(IOException e) {
			System.out.println("in Client -> error : socket");
		}
	}

}
