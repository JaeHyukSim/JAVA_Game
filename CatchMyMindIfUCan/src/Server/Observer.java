package Server;

import java.util.ArrayList;

public interface Observer {
	public void dataSend(String data);
	public String getId();
	public void setId(String s);
	public ArrayList<Observer> getUserList();
	public void setRegisterStation();
}
