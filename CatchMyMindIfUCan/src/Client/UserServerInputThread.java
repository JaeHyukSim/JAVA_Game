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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import UserForm.LoginForm;
import UserForm.SignUpForm;
import UserForm.UserForm;
import jdk.nashorn.internal.scripts.JD;

public class UserServerInputThread implements Runnable{
	
	private volatile static UserServerInputThread uniqueInstance;
	
	private BufferedReader inFromServer;
	private String inputData;
	
	private SignUpForm signUpForm;
	private JPanel jpanel;
	private DisplayThread displayThread;
	private LoginForm loginForm;
	private JDialog jd;
	private JButton jb;
	
	private String id;
	private String lv;
	private String exp;
	private String ch;
	
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
			signUpForm = SignUpForm.getInstance(socket);
			displayThread = DisplayThread.getInstance(socket);
			loginForm = LoginForm.getInstance(displayThread, socket);
			jpanel = loginForm.getJPanel();
			
			
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
				JSONParser jsonParser = new JSONParser();
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
						loginForm.setId((String)jsonObj.get("id"));
						loginForm.setLv((String)jsonObj.get("lv"));
						loginForm.setExp((String)jsonObj.get("exp"));
						loginForm.setCh((String)jsonObj.get("ch"));
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
						System.out.println("^^ 오늘 끝!");
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
