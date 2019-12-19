package myTCP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


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
			
		}catch(IOException e) {
			System.out.println("server out");
		}
		
	}
	
	@Override
	public void run(){
		
		MyMessageFormat mmsg = new MyMessageFormat();
		try {
			JSONParser json = new JSONParser();
			while(true) {
				inputData = inFromClient.readLine();
				//JSON PARSING ...
				try {
					JSONObject jsonObj = (JSONObject)json.parse(inputData);
					if(((String)jsonObj.get("method")).equals("id")) {
						mmsg.setMethod("id");
						mmsg.setFrom(Integer.toString(ID));
						mmsg.setName((String)jsonObj.get("name"));
						mmsg.setBody("");
						//보낼 메시지를 만든다. 그리고 DB에 저장한다. - ID, NAME을 연동 - station에 저장하자
						System.out.println(mmsg.getMessage());
						station.addUserData(mmsg.getMessage());
						
						chargeBuffer(mmsg.getMessage());
						
					} else if(jsonObj.get("method").equals("help")) {
						System.out.println("help system");
						mmsg.setMethod("help");
						for(int i = 1; i <= 1; i++) {
							mmsg.setBody(i + ".Type 'To' to see whisper options.");
						}
						System.out.println(mmsg.getBody());
						
						chargeBuffer(mmsg.getMessage());
						
					} else if(jsonObj.get("method").equals("broadcast")){
						System.out.println("broad cast system");
						mmsg.setMethod("broadcast");
						mmsg.setBody((String)jsonObj.get("body"));
						
						station.notifyAllObserver(mmsg.getMessage());
					} else if(jsonObj.get("method").equals("header")) {
						String userDate = station.getUsersId();
						mmsg.setMethod("header");
						mmsg.setBody(userDate);
						chargeBuffer(mmsg.getMessageBody());
						
					}
					
				}catch(ParseException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//station.notifyAllObserver(inputData);
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
