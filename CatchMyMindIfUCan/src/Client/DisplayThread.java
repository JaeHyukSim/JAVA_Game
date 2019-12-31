package Client;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.smartcardio.Card;
import javax.swing.JFrame;

import com.sun.javafx.tk.Toolkit;

import UserForm.LoginForm;
import UserForm.UserForm;
import UserForm.WaitingRoomForm;

public class DisplayThread extends JFrame implements Runnable{
	
	private volatile static DisplayThread uniqueInstance;
	private CardLayout card;
	private UserForm userForm;
	
	//Singleton constructor(ctor)
	private DisplayThread() {
		
		//title
		setTitle("캐치마이마인드");
		
		//position and size ( 360, 640 )
		Dimension screenSize = getToolkit().getScreenSize();
		setSize(360, 640);
		setBounds((screenSize.width - getSize().width)/2, (screenSize.height - getSize().height)/2,
				getSize().width, getSize().height);
		
		//layout - card
		card = new CardLayout();
		setLayout(card);
		
		//exit
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		//pushTemp////////////
		LoginForm login = LoginForm.getInstance(this);
		WaitingRoomForm waitingRoom = WaitingRoomForm.getInstance(this);
		getContentPane().add(login.getJPanel(), "login");
		getContentPane().add(waitingRoom.getJPanel(),"waitingRoom");
	}
	
	//Singleton Pattern
	public static DisplayThread getInstance() {
		if(uniqueInstance == null) {
			synchronized (DisplayThread.class) {
				if(uniqueInstance == null) {
					uniqueInstance = new DisplayThread();
				}
			}
		}
		return uniqueInstance;
	}
	
	public CardLayout getCardLayout() {
		return card;
	}
	
	@Override
	public void run() {
		
	}
}
