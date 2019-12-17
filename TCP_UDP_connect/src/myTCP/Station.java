package myTCP;
/*
 * it is revised!!!
 */
import java.util.ArrayList;

public class Station implements Observable{
	private ArrayList<Observer> arr;
	
	public Station() {
		arr = new ArrayList<Observer>();
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
				System.out.println("ob : " + s);
			}catch(Exception e) {
			}
		}
	}
}
