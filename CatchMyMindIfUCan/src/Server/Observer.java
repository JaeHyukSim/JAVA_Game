package Server;

import java.util.ArrayList;

public interface Observer {
	public void dataSend(String data);
	public String getId();
	public void setId(String s);
	
	public ArrayList<Observer> getUserList();
	public ArrayList<Observer> getWaitingList();
	public ArrayList<RoomData> getRoomList();
	
	public void setRegisterStation();
	public void setWaitingList();
	
	public String getLv();
	public void setLv(String lv);
	public String getState();
	public void setState(String state);
	
	public Station getStation();
	
}
