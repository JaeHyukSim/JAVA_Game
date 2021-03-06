package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import UserForm.LoginForm;
import UserForm.SignUpForm;
import UserForm.UserForm;
import UserForm.WaitingRoomForm;
import jdk.nashorn.internal.scripts.JD;

public class UserServerInputThread implements Runnable{
	
	private volatile static UserServerInputThread uniqueInstance;
	
	private BufferedReader inFromServer;
	private String inputData;
	
	private LoginForm loginForm;
	private SignUpForm signUpForm;
	private WaitingRoomForm waitingRoomForm;
	
	private JPanel jpanel;
	private DisplayThread displayThread;
	private JDialog jd;
	private JButton jb;
	
	private String id;
	private String lv;
	private String exp;
	private String ch;
	
	private JSONParser jsonParser;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLv() {
		return lv;
	}

	public void setLv(String lv) {
		this.lv = lv;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public UserMessageProcessor getUserMessageProcessor() {
		return userMessageProcessor;
	}

	public void setUserMessageProcessor(UserMessageProcessor userMessageProcessor) {
		this.userMessageProcessor = userMessageProcessor;
	}

	private UserForm userForm;
	private UserMessageProcessor userMessageProcessor;
	
	private UserServerInputThread(Socket socket) {
		try {
			
			displayThread = DisplayThread.getInstance(socket);
			
			loginForm = LoginForm.getInstance(displayThread, socket);
			signUpForm = SignUpForm.getInstance(socket);
			waitingRoomForm = WaitingRoomForm.getInstance(displayThread, socket);
			
			jpanel = loginForm.getJPanel();
			jsonParser = new JSONParser();
			
			
			inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			userMessageProcessor = new UserMessageProcessor();
		} catch (IOException e) {
			System.out.println("in UserServerInputThread - inFromServer error");
			e.printStackTrace();
		}
	}
	
	public static UserServerInputThread getInstance(Socket socket) {
		if(uniqueInstance == null) {
			synchronized (UserServerInputThread.class) {
				if(uniqueInstance == null) {
					uniqueInstance = new UserServerInputThread(socket);
				}
			}
			return uniqueInstance;
		}
		System.out.println("userserverinputthread is not null");
		return uniqueInstance;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				inputData = inFromServer.readLine();
				try {
					JSONObject jsonObj = (JSONObject)jsonParser.parse(inputData);
					
					//operation start
					switch((String)jsonObj.get("method")) {
					case "1004":	//id - error
						loginForm.makeJDialog("아이디가 틀렸습니다.");
						jb = loginForm.getJb();
						jd = loginForm.getJd();
						
						jb.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								jd.setVisible(false);
							}
						});
						break;
					case "1014": // pwd - error
						loginForm.makeJDialog("비밀번호가 틀렸습니다.");
						jb = loginForm.getJb();
						jd = loginForm.getJd();
						jb.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								jd.setVisible(false);
							}
						});
						break;
					case "1002": // login ok
						id = (String)jsonObj.get("id");
						lv = (String)jsonObj.get("id");
						exp = (String)jsonObj.get("id");
						ch = (String)jsonObj.get("id");
						loginForm.setId(id);
						loginForm.setLv(lv);
						loginForm.setExp(exp);
						loginForm.setCh(ch);
						loginForm.swapLogin();
						break;
					case "1104": //id check is failed
						System.out.println("1104!");
						signUpForm.setIsCheck(false);
						break;
					case "1102": // id check is verified
						signUpForm.setIsCheck(true);
						break;
					case "1202": //sign up OK
						signUpForm.getDialog("회원가입 성공!", 250, 100);
						signUpForm.setDialLogInvisible();
						signUpForm.dialogClear();
						break;
					case "1204": //회원가입 실패
						signUpForm.getDialog("회원가입에 실패했습니다.", 250, 100);
						signUpForm.dialogClear();
						break;
					case "1024": //login - already login : fail
						signUpForm.getDialog("이미 로그인 중입니다.", 250, 100);
						break;
					case "2002": //waiting room - get info
						displayThread.setBounds(300, 0, 1440,1024);
						displayThread.getCardLayout().show(displayThread.getContentPane(), "waitingRoom");
						//현재 두개의 데이터를 받은 상태 - 대기실 방 유저들, 방 목록들
						//1. 대기실 인원 목록을 재설정하는 getWaitingUserList 함수 호출
						//2. 방 목록을 재설정하는 getGameRoomList 함수 호출
						waitingRoomForm.init();
						
						JSONArray userList = (JSONArray)jsonObj.get("roomUserList");
						for(int i = 0; i < userList.size(); i++) {
							waitingRoomForm.makeRoomList((String)((JSONObject)userList.get(i)).get("roomId"),
									(String)((JSONObject)userList.get(i)).get("roomName"),
									(String)((JSONObject)userList.get(i)).get("roomState"),
									(String)((JSONObject)userList.get(i)).get("roomCurUser"),
									(String)((JSONObject)userList.get(i)).get("roomMaxUser"),
									(String)((JSONObject)userList.get(i)).get("roomPass"),
									(String)((JSONObject)userList.get(i)).get("roomPassState"),
									i);
						}
						userList = (JSONArray)jsonObj.get("waitingUserList");
						for(int i = 0; i < userList.size(); i++) {
							waitingRoomForm.makeUserList((String)((JSONObject)userList.get(i)).get("id"),
									(String)((JSONObject)userList.get(i)).get("lv"),
									(String)((JSONObject)userList.get(i)).get("state"),
									i);
						}
						break;
					case "3012":
						waitingRoomForm.getChatData((String)jsonObj.get("chat"));
						break;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}catch(IOException e) {
			System.out.println("in UserServerInputThread - readLine error");
		}
	}
}
