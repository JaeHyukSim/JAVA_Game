package Server;

import java.util.ArrayList;

public class Station implements Observable{
	private ArrayList<Observer> userList;
	
	public Station() {
		userList = new ArrayList<Observer>();
		
	}
	public void registerObserver(Observer o) {
		userList.add(o);
	}
	public void removeObserver(Observer o) {
		int i = userList.indexOf(o);
		if(i != -1) {
			userList.remove(i);
		}
		System.out.println("removement operating ...");
		System.out.println("size : " + userList.size());
	}
	public void broadcastObserver(String data) {
		Observer o;
		for(int i = 0; i < userList.size(); i++) {
			o = (Observer)userList.get(i);
			o.dataSend(data);
		}
		System.out.println("broadcasting...");
		System.out.println(data);
	}
	public void unicastObserver(String data, Observer o) {
		int i = userList.indexOf(o);
		if(i != -1) {
			o.dataSend(data);
		}
	}
}
