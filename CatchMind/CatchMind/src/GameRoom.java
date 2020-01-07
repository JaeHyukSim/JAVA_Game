import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class GameRoom extends JPanel {
	
	JPanel grSketch = new JPanel();
	JPanel[] userPanel = new JPanel[6];
	MyTimer grTimer;
	Chat grChat;
	JButton btnTimer;
	static JTextField[] userChat = new JTextField[6];

	GameRoom(){
		setLayout(null);
		
		Font font = new Font(getName(), Font.BOLD, 20);
		
		for(int i=0; i<6; i++) {
			userChat[i] = new JTextField();
			userChat[i].setBounds(0,0,0,0);
			userChat[i].setBackground(Color.lightGray);
			userChat[i].setHorizontalAlignment(JTextField.CENTER);
			userChat[i].setFont(font);
			add(userChat[i]);
		}
		
		grSketch.setBounds(300, 50, 840, 550);
		grSketch.setBackground(Color.BLACK);
		add(grSketch);
		
		for(int i=0; i<6; i++) {
			userPanel[i] = new JPanel();
			userPanel[i].setBackground(Color.BLACK);
		}
		for(int i=0; i<3; i++) {
			userPanel[i].setBounds(50, 50+250*i, 200, 200);
			add(userPanel[i]);
		}
		for(int i=3; i<6; i++) {
			userPanel[i].setBounds(1190, 50+250*(i-3), 200, 200);
			add(userPanel[i]);
		}
		
		grTimer = new MyTimer();
		grTimer.timeBar.setBounds(770, 650, 320, 50);
		add(grTimer.timeBar);
		
		btnTimer = new JButton("ready");
		btnTimer.setBounds(650, 10, 65, 40);
		add(btnTimer);
		
		grChat = new Chat();
		grChat.chatBar.setBounds(770, 700, 320, 50);
		add(grChat.chatBar);
	}

}
