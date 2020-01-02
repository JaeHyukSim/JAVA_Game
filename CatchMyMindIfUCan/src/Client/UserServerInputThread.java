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
import UserForm.UserForm;
import jdk.nashorn.internal.scripts.JD;

public class UserServerInputThread implements Runnable{
	private BufferedReader inFromServer;
	private String inputData;
	
	private JPanel jpanel;
	private DisplayThread displayThread;
	private LoginForm loginForm;
	private JDialog jd;
	private JButton jb;
	
	public UserMessageProcessor getUserMessageProcessor() {
		return userMessageProcessor;
	}

	public void setUserMessageProcessor(UserMessageProcessor userMessageProcessor) {
		this.userMessageProcessor = userMessageProcessor;
	}

	private UserForm userForm;
	private UserMessageProcessor userMessageProcessor;
	
	public UserServerInputThread(Socket socket) {
		try {
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
	
	@Override
	public void run() {
		try {
			while(true) {
				inputData = inFromServer.readLine();
				System.out.println(inputData);
				
				JSONParser jsonParser = new JSONParser();
				try {
					JSONObject jsonObj = (JSONObject)jsonParser.parse(inputData);
					
					//동작 시작
					switch((String)jsonObj.get("method")) {
					case "1004":	//아이디 오류
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
					case "1014":
						loginForm.makeJDialog("비밀번호가 틀렸습니다");
						jb = loginForm.getJb();
						jd = loginForm.getJd();
						jb.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								jd.setVisible(false);
							}
						});
						break;
					case "1002":
						loginForm.swapLogin();
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
