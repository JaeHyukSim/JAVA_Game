
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class MainFunction extends JFrame implements ActionListener{

	Login login = new Login();
	WaitRoom waitRoom = new WaitRoom();
	CardLayout card = new CardLayout();
	
	public MainFunction() {
		setLayout(card);
		add("Center", login);
		add("waitRoom",waitRoom);
		setSize(1024,768);
		setVisible(true);
		
		login.b1.addActionListener(this);
	}
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("1com.jtattoo.plaf.acryl.AcrylLookAndFeel");
			JFrame.setDefaultLookAndFeelDecorated(true);
		}catch (Exception e) {
			// TODO: handle exception
		}
		MainFunction mf = new MainFunction();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == login.b1) {
			card.show(getContentPane(), "waitRoom");
		}
	}

}
