package ����ȣȣ;

import java.awt.CardLayout;

import javax.swing.JFrame;

public class DisplayThread extends JFrame{
	
	private CardLayout card;
	
	public DisplayThread() {
		setTitle("���� JFrame WINDOW�Դϴ�.");
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
