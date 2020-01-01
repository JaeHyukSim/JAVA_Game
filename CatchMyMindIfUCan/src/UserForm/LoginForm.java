
package UserForm;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Client.DisplayThread;
import sun.rmi.runtime.Log;

public class LoginForm implements UserForm{
	
	private volatile static LoginForm uniqueInstance;
	private JPanel jpanel;
	private DisplayThread displayThread;
	
	private final double GOLDRATE = 1.618;
	
	private LoginForm(DisplayThread ds) {
		this.displayThread = ds;
		
		//login panel operation
		String loginPanelPath = "..\\Resource\\loginPanelImage.png";
		jpanel = displayThread.createJPanel(loginPanelPath);
		jpanel.setLayout(null);
		
		
		//gamestart panel, login input panel operation
		JPanel gamePanel = new JPanel();
		JPanel loginPanel = new JPanel();
		
		//2개의 jpanel position - golden ratio
		int goldenHeight = (int)(ds.HEIGHT / GOLDRATE);
		gamePanel.setBounds(0, 0, ds.WIDTH,ds.HEIGHT-goldenHeight);
		loginPanel.setBounds(0,ds.HEIGHT-goldenHeight,ds.WIDTH,goldenHeight);
		
		//inherit
		jpanel.add(gamePanel);
		jpanel.add(loginPanel);

		//make inherit decoration - gamePanel
		gamePanel.setLayout(null);
		gamePanel.setOpaque(false);
		try {
			ImageIcon gameStartBtnIcon = new ImageIcon(getClass().getResource("..\\\\Resource\\\\gameStartButton_final4.png"));
			ImageIcon gameStartBtnIconRollover = new ImageIcon(getClass().getResource("..\\\\Resource\\\\gameStartButton_rollover.png"));
			ImageIcon gameStartBtnIconPressed = new ImageIcon(getClass().getResource("..\\\\Resource\\\\gameStartButton_pressed.png"));
			
			JButton gameStartBtn = new JButton(gameStartBtnIcon);
			gameStartBtn.setRolloverIcon(gameStartBtnIconRollover);
			gameStartBtn.setPressedIcon(gameStartBtnIconPressed);
			gameStartBtn.setBounds(gamePanel.getWidth()/10, gamePanel.getHeight()/10,
					gamePanel.getWidth() * 8 / 10, gamePanel.getHeight() * 8 / 10);
			gamePanel.add(gameStartBtn);
			
			gameStartBtn.setContentAreaFilled(false);
			gameStartBtn.setFocusPainted(false);
			gameStartBtn.setBorderPainted(false);
			
		}catch(Exception e) {
			System.out.println("can't find image");
		}
	}
	
	public static LoginForm getInstance(DisplayThread ds) {
		if(uniqueInstance == null) {
			synchronized (LoginForm.class) {
				if(uniqueInstance == null) {
					uniqueInstance = new LoginForm(ds);
				}
			}
		}
		return uniqueInstance;
	}
	
	
	@Override
	public void display() {
	}
	@Override
	public JPanel getJPanel() {
		return jpanel;
	}
}
