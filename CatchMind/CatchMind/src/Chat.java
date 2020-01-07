
import javax.swing.JTextField;

public class Chat extends JTextField{
	
	JTextField chatBar;
	
	Chat(){
		chatBar = new JTextField();
	}
	
	void printChat(int userNum, String message) {
		
		Runnable r = new printChatThread(userNum, message);
		Thread t = new Thread(r);
		t.start();
		
	}
}

class printChatThread implements Runnable{
	
	int userNum;
	String message;
	
	printChatThread(int userNum, String message){
		this.userNum = userNum;
		this.message = message;
	}
	public void run() {
		GameRoom.userChat[userNum].setText(message);
		
		switch(userNum) {
		case 0:
			GameRoom.userChat[0].setBounds(250, 125+userNum*250, 20+message.length()*17, 50);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GameRoom.userChat[userNum].setBounds(0, 0, 0, 0);
			break;
		case 1:
			GameRoom.userChat[1].setBounds(250, 125+userNum*250, 20+message.length()*17, 50);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GameRoom.userChat[userNum].setBounds(0, 0, 0, 0);
			break;
		case 2:
			GameRoom.userChat[2].setBounds(250, 125+userNum*250, 20+message.length()*17, 50);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GameRoom.userChat[userNum].setBounds(0, 0, 0, 0);
			break;
		case 3:
			GameRoom.userChat[3].setBounds(1190-(20+message.length()*17), 125+(userNum-3)*250, 20+message.length()*17, 50);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GameRoom.userChat[userNum].setBounds(0, 0, 0, 0);
			break;
		case 4:
			GameRoom.userChat[4].setBounds(1190-(20+message.length()*17), 125+(userNum-3)*250, 20+message.length()*17, 50);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GameRoom.userChat[userNum].setBounds(0, 0, 0, 0);
			break;
		case 5:
			GameRoom.userChat[5].setBounds(1190-(20+message.length()*17), 125+(userNum-3)*250, 20+message.length()*17, 50);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GameRoom.userChat[userNum].setBounds(0, 0, 0, 0);
			break;
		}
	}
}
