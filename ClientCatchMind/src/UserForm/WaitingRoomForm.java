package UserForm;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Client.DisplayThread;
import Client.UserInputThread;
import Client.UserMessageProcessor;


public class WaitingRoomForm extends JPanel implements UserForm, ActionListener, MouseListener{

	private volatile static WaitingRoomForm uniqueInstance;
	
	private UserMessageProcessor userMessageProcessor;
	private DisplayThread displayThread;
	private Socket socket;
	private UserInputThread unt;
	private JSONParser jsonParser;
	
	private Runnable userRunnable;
	private Thread userThread; // ... 데이터 전송을 위한 thread를 선언해 줍니다!!!
	
////////////////////////////////////////////////////////////
	private DefaultTableModel model1, model2;
	private JTable table1, table2;
	private JScrollPane js1, js2, js3;
	private JTextArea ta;
	private JTextField tf;
	
	private JButton b1, b2, b3;
	private JPanel p1;
	
////////////////////////////////////////////////////////////
	
	private MakeRoom_Dialog makeroom_dialog;
	
	private WaitingRoomForm(DisplayThread dt, Socket socket) {
		this.displayThread = dt;
		this.socket = socket;
		userMessageProcessor = new UserMessageProcessor();
		unt = UserInputThread.getInstance(socket);
		jsonParser = new JSONParser();
		userRunnable = unt;
		
////////////////////////////////////////////////////////////
		
				makeroom_dialog = new MakeRoom_Dialog(this);
			
				// table1에 타이틀 맨 위줄
				// table1에 내용으로 들어갈 내용
				String[] col1 = {"방번호", "방제목", "방장", "인원", "비고", "상태"};
				String[][] row1 = new String[0][col1.length];
				
				// table1에 DefaultTableModel(기본모양)으로 틀을 만들어 둠
				// table1에 만들어두었던 모델일 넣어줌
				model1 = new DefaultTableModel(row1, col1);
				table1 = new JTable(model1);
				// 스크롤이 있는 팬을 생성하면서 table1을 추가 시킴
				js1 = new JScrollPane(table1);
				
				// 위와 동일하게 두번째 JTable생성
				String[] col2 = {"ID","레벨","게임상태"};
				String[][] row2 = new String[0][col2.length];
				
				model2 = new DefaultTableModel(row2, col2);
				table2 = new JTable(model2);
				js2 = new JScrollPane(table2);
				
				// WaitRoom Panel에 레이아웃을 없애 줌 안 없애면 setBound가 안됨
				this.setLayout(null);

				// table을 넣어둔 JScrollPane들의 위치를 수정 함
				js1.setBounds(20, 50, 800, 400);
				js2.setBounds(890, 50, 300, 400);
			
				// 채팅창을 쓸 채팅창과 출력창을 생성
				ta = new JTextArea();
				tf = new JTextField();

				// 채팅창에 내용이 많아지면 스크롤이 생길 수 있도록 JScrollPane에 추가시켜줌
				js3 = new JScrollPane(ta);
			
				// 채팅창이랑 출력창 위치 배치
				js3.setBounds(20, 460, 800, 170);
				tf.setBounds(20, 640, 800, 30);
				// 채팅창을 WaitRoomPanel에 추가시켜줌
				this.add(tf);
				tf.addActionListener(this);
				
				
				p1 = new JPanel();
				b1 = new JButton("방만들기");
				b2 = new JButton("방 참여");
				b3 = new JButton("나가기");
				
				
				b1.addActionListener(this);
				b2.addActionListener(this);
				b3.addActionListener(this);
				
				p1.setBounds(20,0,300,50);
				p1.setLayout(new GridLayout(1,3,0,0));

				p1.add(b1);
				p1.add(b2);
				p1.add(b3);
				
				this.add(p1);
				
				// JScrollPane을 3개다 WaitRoom Pane에 추가 시켜줌
				this.add(js1);
				this.add(js2);
				this.add(js3);
				
////////////////////////////////////////////////////////////
	}
	
	public static WaitingRoomForm getInstance(DisplayThread dt, Socket socket) {
		if(uniqueInstance == null) {
			synchronized (WaitingRoomForm.class) {
				if(uniqueInstance == null) {
					uniqueInstance = new WaitingRoomForm(dt, socket);
				}
			}
		}
		return uniqueInstance;
	}
	
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
		
	}


	/////////////////////////////////////////////// => actionPerformed 사용할 것
	@Override
	public void actionPerformMethod() {
		// TODO Auto-generated method stub
		
		
	}
	///////////////////////////////////////////////
	
	@Override
	public void operation(String data) {
		try {
			JSONObject jsonObj = ((JSONObject)jsonParser.parse(data));
			
			switch((String)(jsonObj.get("method"))) {
			case "2002":
				displayThread.setBounds(300, 0, 1440,1024);
				displayThread.getCardLayout().show(displayThread.getContentPane(), "waitingRoom");
				// init 함수 구현해서 1. 텍스트에어리어 초기화 2. 텍스트필드초기화

				break;
			case "2102":
				//{
				//	"method" : "2102",
				//	"chat" : "abc -> asflakgnldngasl"
				//}
				ta.setText(ta.getText() + "\n" + (String)jsonObj.get("chat"));
				break;
				//대기방에서 클라이언트 전체화면이 전환되는 상황
			case "2012":
				break;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}


	@Override
	public JPanel getJPanel() {
		// TODO Auto-generated method stub
		return this;
	}

	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		// 엔터를 쳤을 때 함수로 들어오게되고 그게 tf라면
		if(e.getSource() == tf)
		{
			
			//서버로 보내봅시다. 
			//1. 메소드를 정해봅시다. 2100 -> 2102
			String sendData = "{";
			sendData += userMessageProcessor.getJSONData("method", "2100");
			sendData +="," +  userMessageProcessor.getJSONData("chat", tf.getText()); 
			sendData += "}";
			
			tf.setText("");
			
			System.out.println("method = 2100, sendData : " + sendData);
			
			//13. 데이터를 서버로 보냅니다!
			unt.setInputData(sendData);
			userThread = new Thread(userRunnable);
			userThread.start();
			
//			// 임시 String을 만든다
//			String temp = null;
//			// temp에 tf에 있던 텍스르틀 넘겨준다
//			temp = /*ScreenVO.getS_VO()+*/ tf.getText();
//			if(temp == null)
//				return;
//			else
//				System.out.println();
//			// tf에 남아있던 텍스트를 지우고 커서를 다시 돌려줌
//			tf.setText("");
//			tf.requestFocus();
//			// ta에 저장했던 text를 뿌려줌
//			ta.append(temp);
			
			
		}
		if(e.getSource() == b1)
		{
			makeroom_dialog.setVisible(true);
			
		}
		if(e.getSource() == b2)
		{
			
		}
		if(e.getSource() == b3)
		{
			
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == table1)
		{
			if(e.getClickCount() == 2)
			{
	
			}
		}
		else if(e.getSource() == table2)
		{
			if(e.getClickCount() == 2)
			{
				
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public UserInputThread getUnt() {
		return unt;
	}

	public void setUnt(UserInputThread unt) {
		this.unt = unt;
	}

	public UserMessageProcessor getUserMessageProcessor() {
		return userMessageProcessor;
	}

	public void setUserMessageProcessor(UserMessageProcessor userMessageProcessor) {
		this.userMessageProcessor = userMessageProcessor;
	}

	
}