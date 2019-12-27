package 하하호호;

import java.awt.CardLayout;

import javax.swing.JFrame;

public class DisplayThread extends JFrame{
	
	private CardLayout card;
	
	public DisplayThread() {
		setTitle("공통 JFrame WINDOW입니다.");
		setBounds(240,90,1440,900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
		card = new CardLayout();
		setLayout(card);
		
		YourFormHere yfh = new YourFormHere(this);
		
	}
	public static void main(String[] args) {
		DisplayThread dt = new DisplayThread();
	}

}
