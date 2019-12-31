package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import UserForm.UserForm;

public class UserServerInputThread implements Runnable{
	private BufferedReader inFromServer;
	private String inputData;
	private UserForm userForm;
	private UserMessageProcessor userMessageProcessor;
	
	public UserServerInputThread(Socket socket) {
		try {
			inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			userMessageProcessor = new UserMessageProcessor();
		} catch (IOException e) {
			System.out.println("in UserServerInputThread - inFromServer error");
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				inputData = inFromServer.readLine();
				System.out.println(inputData);
			}
		}catch(IOException e) {
			System.out.println("in UserServerInputThread - readLine error");
		}
	}
}
