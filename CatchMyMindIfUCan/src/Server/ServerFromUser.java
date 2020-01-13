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
	private String readyState;
	private String ch;
	private String cnt;
	private String exp;
	
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
		roomId = "0";
		readyState = "0";
		exp = "123";
		lv = "2";
		ch = "2";
		
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
	
	public String getReadyState() {
		return readyState;
	}

	public void setReadyState(String readyState) {
		this.readyState = readyState;
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
			//방에 있었다면, 제거한다
			//removeUserFromRoom();
			
			//e.printStackTrace();
		}
	}
	@Override
	public void dataSend(String data) {
		
		try {
			data += "\n";
			//outToClient.write(data.getBytes("UTF-8"));
			outToClient.write(data.getBytes("MS949"));
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
	
	public void removeUserFromRoom() {
		//방에 있었다면, 제거한다
		if(!roomId.equals("0")) {
			RoomData rd = station.findUserListById(roomId);
			if(readyState.equals("1")) {
				rd.setCountOfReadyUser(Integer.parseInt(rd.getCountOfReadyUser())-1 + "");
			}
			if(rd.getIdOfMasterUser().equals(id)) {
				rd.setIdOfMasterUser(rd.getUserList().get(0).getId());	
			}
			rd.removeUserList(this);
			if(rd.getUserList().size() == 0) {
				station.removeRoomUserList(rd);
			}
			System.out.println("removed user(" + id + ") FROM room (" + roomId + ")");
			roomId = "0";
			readyState = "0";
		}
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public String getCnt() {
		return cnt;
	}

	public void setCnt(String cnt) {
		this.cnt = cnt;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}
	
	
}
