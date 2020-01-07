import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class CatchMind extends JFrame implements ActionListener, KeyListener{
	
	int userNum = 3;
	CardLayout card = new CardLayout();
	GameRoom gr = new GameRoom();
	
	CatchMind() {
		setLayout(card);
		add("GR", gr);
		setBounds(0, 0, 1440, 900);
		setResizable(false);
		setVisible(true);
		gr.btnTimer.addActionListener(this);
		gr.grChat.chatBar.addKeyListener(this);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CatchMind cm = new CatchMind();
		cm.setDefaultCloseOperation(cm.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==gr.btnTimer) {
			gr.btnTimer.setText("start");
			try {
				gr.grTimer.start();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			gr.grChat.printChat(userNum, gr.grChat.chatBar.getText());
			gr.grChat.chatBar.setText("");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}