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
	private String lv;
	private String state;
	private String roomId;
	
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
		lv = "1";
		state = "1";
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
				//station.unicastObserver(userMessage, this);
			}
		}catch(IOException e) {
			System.out.println("in ServerFromUser - userMessage error");
			station.removeObserver(this);
			station.removeWaitingUser(this);
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
	public ArrayList<RoomData> getRoomList(){
		return station.getRoomList();
	}
	@Override
	public ArrayList<Observer> getUserList() {
		return station.getUserList();
	}
	@Override
	public void setRegisterStation() {
		station.registerObserver(this);
	}
	@Override
	public void setWaitingList() {
		station.registerWaitingUser(this);
	}
	@Override
	public ArrayList<Observer> getWaitingList(){
		return station.getWaitingList();
	}

	public String getLv() {
		return lv;
	}

	public void setLv(String lv) {
		this.lv = lv;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	public Station getStation() {
		return station;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
}
