package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerFromManager implements Runnable{
	private BufferedReader inFromManager;
	private Station station;
	private String managerMessage;
	
	public ServerFromManager(Station station) {
		this.station = station;
		inFromManager = new BufferedReader(new InputStreamReader(System.in));
	}
	@Override
	public void run() {
		while(true) {
			try {
				managerMessage = inFromManager.readLine();
				station.broadcastObserver(managerMessage);
				
			} catch (IOException e) {
				System.out.println("in ServerFromManager - managerMessage Error");
				e.printStackTrace();
			}
		}
	}
}
