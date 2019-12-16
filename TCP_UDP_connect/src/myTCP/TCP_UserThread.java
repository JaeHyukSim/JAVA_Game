package myTCP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCP_UserThread implements Runnable{
	
	private int ID;
	private Socket socket;
	private BufferedReader inFromClient;
	DataOutputStream outToClient;
	
	String inputData, outputData;
	
	public TCP_UserThread(int id, Socket soc) {
		try {
			this.ID = id;
			this.socket = soc;
			this.inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToClient = new DataOutputStream(socket.getOutputStream());
			
			outputData = id + ". client connected!";
			System.out.println(outputData);
			outToClient.writeBytes(outputData + '\n');
		}catch(IOException e) {
			System.out.println("server out");
		}
		
	}
	public void run(){
		try {
			while(true) {
				inputData = inFromClient.readLine();
				//·ÎÁ÷
				outputData = inputData.toUpperCase();
				outToClient.writeBytes(outputData + '\n');
			}
		}catch(IOException e) {
			System.out.println("client : " + ID + " out");
		}
	}
}
