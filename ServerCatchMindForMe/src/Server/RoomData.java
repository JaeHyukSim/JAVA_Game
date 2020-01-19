package Server;

import java.util.ArrayList;

public class RoomData {
	private String nameOfRoom;
	private String numberOfRoom;
	private String countOfCurrentUser;
	private String countOfMaximumUser;
	private String countOfReadyUser;
	private ArrayList<Observer> userList;
	private String IdOfMasterUser;
	private String roomPass;
	private String roomState; // 게임중인지 아닌지
	private String roomPassState; // 비밀번호 boolean
	private String maxRound;
	private String currentRound;
	
	public RoomData() {
		userList = new ArrayList<Observer>();
		roomPass = "";	// 방 비밀번호
		numberOfRoom = "0"; // 
		nameOfRoom = "default room name!!";
		countOfCurrentUser = "0";
		countOfMaximumUser = "0";
		countOfReadyUser = "0";
		IdOfMasterUser = "#";
		roomState = "waiting";
		roomPassState = "none";
		maxRound = "5";
		currentRound = "1";
	}
	
	public boolean isAllReady() {
		if(countOfCurrentUser.equals("1")) {
			return false;
		}
		if(countOfCurrentUser.equals(String.valueOf(Integer.parseInt(countOfReadyUser) + 1))) {
			return true;
		}else {
			return false;
		}
	}
	public void someoneReadyOff() {
		if(countOfReadyUser.equals("0")) {
			System.out.println("Nobody is ready!");
		}else {
			countOfReadyUser = String.valueOf(Integer.parseInt(countOfReadyUser) - 1);
		}
		
	}
	public void someoneReadyOn() {
		if(countOfReadyUser.equals(countOfCurrentUser)) {
			System.out.println("Everyone is ready!");
		}else {
			countOfReadyUser = String.valueOf(Integer.parseInt(countOfReadyUser) + 1);
		}
	}
	public void addUserList(Observer o) {
		userList.add(o);
		countOfCurrentUser = String.valueOf(Integer.parseInt(countOfCurrentUser) + 1);
	}
	public void removeUserList(Observer o) {
		int i = userList.indexOf(o);
		if(i != -1) {
			userList.remove(i);
			countOfCurrentUser = String.valueOf(Integer.parseInt(countOfCurrentUser) - 1);
		}
	}
	public String getNameOfRoom() {
		return nameOfRoom;
	}
	public void setNameOfRoom(String nameOfRoom) {
		this.nameOfRoom = nameOfRoom;
	}
	public String getNumberOfRoom() {
		return numberOfRoom;
	}
	public void setNumberOfRoom(String numberOfRoom) {
		this.numberOfRoom = numberOfRoom;
	}
	public String getCountOfCurrentUser() {
		return countOfCurrentUser;
	}
	public void setCountOfCurrentUser(String countOfCurrentUser) {
		this.countOfCurrentUser = countOfCurrentUser;
	}
	public String getCountOfMaximumUser() {
		return countOfMaximumUser;
	}
	public void setCountOfMaximumUser(String countOfMaximumUser) {
		this.countOfMaximumUser = countOfMaximumUser;
	}
	public String getCountOfReadyUser() {
		return countOfReadyUser;
	}
	public void setCountOfReadyUser(String countOfReadyUser) {
		this.countOfReadyUser = countOfReadyUser;
	}
	public String getIdOfMasterUser() {
		return IdOfMasterUser;
	}
	public void setIdOfMasterUser(String idOfMasterUser) {
		IdOfMasterUser = idOfMasterUser;
	}
	public String getRoomPass() {
		return roomPass;
	}
	public void setRoomPass(String roomPass) {
		this.roomPass = roomPass;
	}
	public String getRoomState() {
		return roomState;
	}
	public void setRoomState(String roomState) {
		this.roomState = roomState;
	}
	public String getRoomPassState() {
		return roomPassState;
	}
	public void setRoomPassState(String roomPassState) {
		this.roomPassState = roomPassState;
	}

	public ArrayList<Observer> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<Observer> userList) {
		this.userList = userList;
	}

	public String getMaxRound() {
		return maxRound;
	}

	public void setMaxRound(String maxRound) {
		this.maxRound = maxRound;
	}

	public String getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(String currentRound) {
		this.currentRound = currentRound;
	}
	
}
