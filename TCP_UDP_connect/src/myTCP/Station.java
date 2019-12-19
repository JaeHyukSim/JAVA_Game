package myTCP;
/*
 * it is revised!!!!!
 */
import java.util.ArrayList;

public class Station implements Observable{
	private ArrayList<String> userDataList;
	private ArrayList<Observer> arr;
	MyMessageFormat mmsg;
	
	public boolean isWhisperIsReady() {
		return whisperIsReady;
	}
	public void setWhisperIsReady(boolean whisperIsReady) {
		this.whisperIsReady = whisperIsReady;
	}
	public boolean isWhispering() {
		return whispering;
	}
	public void setWhispering(boolean whispering) {
		this.whispering = whispering;
	}

	private boolean whisperIsReady;
	private boolean whispering;
	
	public Station() {
		
		whisperIsReady = false;
		whispering = false;
		
		mmsg = new MyMessageFormat();
		arr = new ArrayList<Observer>();
		userDataList = new ArrayList<String>();
	}
	public void registerObserver(Observer o) {
		arr.add(o);
	}
	public void removeObserver(Observer o) {
		int i = arr.indexOf(o);
		if(i != -1) {
			arr.remove(i);
		}
	}
	public void notifyAllObserver(String s) {
		Observer ob;
		for(int i = 0; i < arr.size(); i++) {
			try {
				ob = (Observer)(arr.get(i));
				ob.update(s);
			}catch(Exception e) {
			}
		}
	}
	
	public void addUserData(String msg) {
		userDataList.add(msg);
	}
	
	public String getUsersId(){
		String message = mmsg.getMessageArray(userDataList);
		
		return message;
	}
}
