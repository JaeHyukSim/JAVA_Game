package UserForm;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JPanel;

import Client.DisplayThread;
import Client.UserInputThread;
import Client.UserMessageProcessor;

public class WaitingRoomForm implements UserForm{

	private volatile static WaitingRoomForm uniqueInstance;
	private JPanel jpanel;
	private DisplayThread displayThread;
	private UserMessageProcessor userMessageProcessor;
	private Socket socket;
	private UserInputThread unt;
	
	private WaitingRoomForm(DisplayThread dt, Socket socket) {
		this.socket = socket;
		unt = UserInputThread.getInstance(socket);
		userMessageProcessor = new UserMessageProcessor();
		this.displayThread = dt;
		
		jpanel = new JPanel();
		jpanel.setBackground(Color.RED);
		
		JButton btn = new JButton("방만들기");
		jpanel.add(btn);
		
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				displayThread.getCardLayout().show(
						displayThread.getContentPane(), "gameRoom");
				String sendData = "{";
				sendData += userMessageProcessor.getJSONData("method", "3000");
				sendData += "}";
				System.out.println(sendData);
				//2. 서버로 보낸다.
				unt.setInputData(sendData);
				Runnable userInputThread = unt;
				Thread userThread = new Thread(userInputThread);
				userThread.start();
				
			}
		});
	}
	
	public static WaitingRoomForm getInstance(DisplayThread dt,Socket socket) {
		if(uniqueInstance == null) {
			synchronized (WaitingRoomForm.class) {
				if(uniqueInstance == null) {
					uniqueInstance = new WaitingRoomForm(dt,socket);
				}
			}
		}
		return uniqueInstance;
	}
	@Override
	public void display() {
		
	}
	public JPanel getJPanel() {
		return jpanel;
	}
}
