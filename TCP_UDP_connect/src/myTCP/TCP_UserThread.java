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
	private boolean using;
	String inputData, outputData;
	private BroadThread broadThread;
	private Station station;
	private Thread tr;
	
	public TCP_UserThread(int id, Socket soc, Station st) {
		try {
			this.station = st;
			using = false;
			this.ID = id;
			this.socket = soc;
			this.inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToClient = new DataOutputStream(socket.getOutputStream());
			
			Runnable r = new BroadThread(inFromClient, station, outToClient, this);
			tr = new Thread(r);
			
			outputData = id + ". client connected!";
			outToClient.writeBytes(outputData + '\n');
		}catch(IOException e) {
			System.out.println("server out");
		}
		
	}
	public void run(){
		try {
			while(true) {
				inputData = inFromClient.readLine();
				station.notifyAllObserver(inputData);
				//로직
				/*
				outputData = inputData.toUpperCase();
				outToClient.writeBytes(outputData + '\n'); //요부분을 이용해서 broadcasting
				*/
				/*
				try {
					chargeBuffer(inputData.toUpperCase());
				}catch(InterruptedException e) {
					
				}
				*/
			}
		}catch(IOException e) {
			System.out.println("client : " + ID + " out");
		}
	}
	
	public synchronized void chargeBuffer(String s) throws InterruptedException{
		if(using == true) wait();
		
		try {
			using = true;
			outputData = s;
			outToClient.writeBytes(outputData + '\n');
			using = false;
			notify();
		}catch(IOException e) {
			
		}
	}
}
