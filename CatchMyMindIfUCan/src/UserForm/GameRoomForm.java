package UserForm;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import Client.DisplayThread;
import Client.UserInputThread;
import Client.UserMessageProcessor;

public class GameRoomForm implements UserForm{
	
	private JPanel grp;
	
	private volatile static GameRoomForm uniqueInstance; 
	
	private UserMessageProcessor userMessageProcessor;	// message를 json으로 변환시켜주는 클래스
	private DisplayThread displayThread;				// jframe의 컨테이너, 우리가 만든 gameroom panel을 여기에 add
	private Socket socket;								// 통신 소켓
	private UserInputThread unt;						// message를 서버로 보내는 thread
	
	private JPanel grSketch;
	private JPanel[] userPanel;
	private JPanel exp;
	private JPanel timer;
	private JPanel turn;
	private JPanel answer;
	private JPanel round;
	//JButton b1,b2,b3,b4;
	private JButton[] b;
	private JPanel p;
	private Label[] label;	
	private JTextField tf;
	private JProgressBar jb;
	
	private GameRoomForm(DisplayThread ds, Socket socket) {
		this.socket = socket;
		unt = UserInputThread.getInstance(socket);
		userMessageProcessor = new UserMessageProcessor();
		this.displayThread = ds;
		
		grp = new JPanel();
		grp.setSize(1446, 824);
		
		grSketch = new JPanel();
		userPanel = new JPanel[6];
		exp = new JPanel();
		timer = new JPanel();
		turn = new JPanel();
		answer = new JPanel();
		round = new JPanel();
		//JButton b1,b2,b3,b4;
		b = new JButton[4];
		p = new JPanel();
		label = new Label[24];	
		
		grp.setLayout(null);
		
		tf=new JTextField();
		
		for(int i=0; i<1; i++) {
			b[i] = new JButton("게임준비");
			b[i+1] = new JButton("게임초대");
			b[i+2] = new JButton("친구추가");
			b[i+3] = new JButton("나가기");
		}
		/*
		 * b1=new JButton("게임준비"); 
		 * b2=new JButton("게임초대"); 
		 * b3=new JButton("친구추가");
		 * b4=new JButton("나가기");
		 */
		
		//1. 스케치북		
		grSketch.setBounds(280, 15, 880, 600);
		grSketch.setBackground(Color.BLACK);
		grp.add(grSketch);
		
		//2. 유저패널		
		for(int i=0; i<6; i++) {
			userPanel[i] = new JPanel();
			userPanel[i].setBackground(Color.BLACK);
		}
		for(int i=0; i<3; i++) {
			userPanel[i].setLayout(null);
			userPanel[i].setBounds(15, 15+(205*i), 250, 190);
			grp.add(userPanel[i]);
		}
		for(int i=3; i<6; i++) {
			userPanel[i].setLayout(null);
			userPanel[i].setBounds(1175, 15+(205*(i-3)), 250, 190);
			grp.add(userPanel[i]);
		}
		
		//3.라운드표시기
		timer.setBounds(15, 630, 250, 150);
		timer.setBackground(Color.gray);
		grp.add(timer);
		
		//4.차례표시기
		turn.setBounds(280, 630, 250, 105);
		turn.setBackground(Color.pink);
		grp.add(turn);
		
		//5.정답글자수표시기		
		answer.setBounds(545, 630, 250, 105);
		answer.setBackground(Color.yellow);
		grp.add(answer);
		
		//7.타이머		
		round.setBounds(810, 630, 350, 70);
		round.setLayout(null);
		round.setBackground(Color.blue);
		grp.add(round);
		
		//6.채팅		
		tf.setBounds(810, 710, 350, 30);
		grp.add(tf);
				
		//8.경험치바
		/*
		 * exp.setBounds(280, 750, 880, 30); 
		 * exp.setBackground(Color.black);
		 * grp.add(exp);
		 */
		jb = new JProgressBar(0,100);
		jb.setBounds(280, 750, 880, 30);
		jb.setValue(0);
		jb.setStringPainted(true);
		grp.add(jb);
		
		
		
		//버튼					
		p.setLayout(new GridLayout(4,1,0,10));
		/*
		 * p.add(b1);p.add(b2); p.add(b3); p.add(b4); 
		 * //b1. 게임준비
		 * b1.setBackground(Color.white); 
		 * //b2. 게임초대 
		 * b2.setBackground(Color.white);
		 * //b3. 친구추가 
		 * b3.setBackground(Color.white); 
		 * //b4. 나가기
		 * b4.setBackground(Color.white);
		 */
		p.setBounds(1175, 630, 250, 150);
		grp.add(p);
		
		for(int i=0; i<1; i++) {
			b[i] = new JButton("게임준비");
			b[i+1] = new JButton("게임초대");
			b[i+2] = new JButton("친구추가");
			b[i+3] = new JButton("나가기");
		}
		
		for (int i=0;i<1;i++)
		{  
			p.add(b[i]);      b[i].setBackground(Color.white);		
			p.add(b[i+1]);    b[i+1].setBackground(Color.white);
			p.add(b[i+2]);    b[i+2].setBackground(Color.white);
			p.add(b[i+3]);    b[i+3].setBackground(Color.white);
		}
		
		// 라벨
				for (int i = 0; i < 6; i++) {
					label[i] = new Label("아바타");			
					label[i].setBounds(10, 15, 120, 160);
					label[i].setBackground(Color.white);
					userPanel[i].add(label[i]);
					
					label[6+i] = new Label("Id");			
					label[6+i].setBounds(135, 15, 105, 50);
					label[6+i].setBackground(Color.white);
					userPanel[i].add(label[6+i]);
					
					label[12+i] = new Label("Level");			
					label[12+i].setBounds(135, 70, 105, 50);
					label[12+i].setBackground(Color.white);
					userPanel[i].add(label[12+i]);
					
					label[18+i] = new Label("정답수");			
					label[18+i].setBounds(135, 125, 105, 50);
					label[18+i].setBackground(Color.white);
					userPanel[i].add(label[18+i]);
				}
				
		//b[0]. 게임준비 액션
		b[0].setBackground(Color.white);
		b[0].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		//displayThread.add(grp);
	}
	
	public static GameRoomForm getInstance(DisplayThread ds,Socket socket) {
		if(uniqueInstance == null) {
			synchronized (GameRoomForm.class) {
				if(uniqueInstance == null) {
					uniqueInstance = new GameRoomForm(ds, socket);
				}
			}
		}
		return uniqueInstance;
	}
	
	@Override
	public void display() {
		
	}

	@Override
	public JPanel getJPanel() {
		// TODO Auto-generated method stub
		return grp;
	}
}