package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ServerFromUser implements Runnable, Observer{
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	private Station station;
	private ServerMessageProcessor serverMessageProcessor;
	private String userMessage;
	private Socket socket;
	private String id;
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public ServerFromUser(Station station, Socket socket) {
		id = "#";
		this.station = station;
		this.socket = socket;
		serverMessageProcessor = ServerMessageProcessor.getInstMessageProcessor();
		try {
			inFromClient = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			outToClient = new DataOutputStream(socket.getOutputStream());
			station.registerObserver(this);
		} catch (IOException e) {
			System.out.println("in ServerFromUser - inFromClient error");
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				userMessage = inFromClient.readLine();
				System.out.println("userMessage : " + userMessage);
				userMessage = serverMessageProcessor.processingServerMessage(userMessage, this);
				//station.broadcastObserver(userMessage);
				station.unicastObserver(userMessage, this);
			}
		}catch(IOException e) {
			System.out.println("in ServerFromUser - userMessage error");
			station.removeObserver(this);
			//e.printStackTrace();
		}
	}
	@Override
	public void dataSend(String data) {
		
		try {
			data += "\n";
			outToClient.write(data.getBytes("EUC_KR"));
		} catch (IOException e) {
			System.out.println("in ServerFromUser - outToClient write encoding error");
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Observer> getUserList() {
		return station.getUserList();
	}
	@Override
	public void setRegisterStation() {
		station.registerObserver(this);
	}
	
}
