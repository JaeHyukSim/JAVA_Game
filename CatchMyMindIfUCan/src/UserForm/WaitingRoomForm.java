package UserForm;

import java.awt.Color;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JLabel;
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
	
	private JLabel roomList;
	private JLabel roomCount;
	private JButton[] roomButton;
	
	private JLabel userList;
	private JButton[] userButton;
	
	private TextArea chatArea;
	private TextField chatField;
	private JButton chatEnter;
	
	private JButton makeRoom;
	private JButton outToLogin;
	
	private WaitingRoomForm(DisplayThread dt, Socket socket) {
		this.socket = socket;
		unt = UserInputThread.getInstance(socket);
		userMessageProcessor = new UserMessageProcessor();
		this.displayThread = dt;
		
		
		//form 생성
		roomList = new JLabel("-방 목록-");
		roomCount = new JLabel("총 방의 개수를 여따가 입력");
		roomButton = new JButton[4];
		for(int i = 0; i < 4; i++) {
			roomButton[i] = new JButton(i + "");
		}
		userList = new JLabel("-유저 목록=");
		userButton = new JButton[10];
		for(int i = 0; i < 10; i++) {
			userButton[i] = new JButton(i + "");
		}
		chatArea = new TextArea();
		chatField = new TextField();
		chatEnter = new JButton("입력");
		
		makeRoom = new JButton("방 만들기");
		outToLogin = new JButton("로그인으로");
		
		//form 위치 지정
		roomList.setBounds(100, 20, 100, 50);
		roomCount.setBounds(100, 300, 100, 50);
		for(int i = 0; i < 4; i++) {
			roomButton[i].setBounds(100, 40 + i*60, 100, 50);
		}
		userList.setBounds(400, 20, 100, 50);
		for(int i = 0; i < 10; i++) {
			userButton[i].setBounds(400, 40 + i*60, 100, 50);
		}
		chatArea.setBounds(100, 500, 200, 300);
		chatField.setBounds(100,820, 150, 50);
		chatEnter.setBounds(260, 820, 40, 50);
		makeRoom.setBounds(400, 500, 100, 50);
		outToLogin.setBounds(400, 570, 100, 50);
		
		jpanel = new JPanel();
		
		jpanel.setLayout(null);
		jpanel.setOpaque(false);
		JButton btn = new JButton("방만들기");
		jpanel.add(btn);
		
		//패널에 추가
		jpanel.add(roomList);
		jpanel.add(roomCount);
		for(int i = 0; i < 4; i++) {
			jpanel.add(roomButton[i]);
		}
		jpanel.add(userList);
		for(int i = 0; i < 10; i++) {
			jpanel.add(userButton[i]);
		}
		jpanel.add(chatArea);
		jpanel.add(chatField);
		jpanel.add(chatEnter);
		jpanel.add(makeRoom);
		jpanel.add(outToLogin);
		
		init();
		
		makeRoom.addActionListener(new ActionListener() {
			
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
	
	public void init() {
		for(int i = 0; i < 4; i++) {
			roomButton[i].setVisible(false);
		}
		for(int i = 0; i < 10; i++) {
			userButton[i].setVisible(false);
		}
	}
	public void makeRoomList(String id, String name, String state, String cur, String tot, String pwd, String pwdState, int num) {
		if(num >= 4) {
			return;
		}
		roomButton[num].setText("id:"+id+",name:"+name+",state:"+state+",cur:"+cur+",tot:"+tot+",pwdState:"+pwdState);
		roomButton[num].setVisible(true);
	}
	public void makeUserList(String id, String name, String state, int num) {
		if(num >= 10) {
			return;
		}
		userButton[num].setText("id:" + id + ",name:"+name+",state:"+state);
		userButton[num].setVisible(true);
	}
}
