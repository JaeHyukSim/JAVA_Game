
package UserForm;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Client.DisplayThread;
import sun.rmi.runtime.Log;

public class LoginForm implements UserForm{
	
	private volatile static LoginForm uniqueInstance;
	private JPanel jpanel;
	private DisplayThread displayThread;
	private CardLayout card;
	
	private final double GOLDRATE = 1.618;
	
	private LoginForm(DisplayThread ds) {
		this.displayThread = ds;
		
		//login panel operation
		String loginPanelPath = "..\\Resource\\loginPanelImage2.png";
		jpanel = displayThread.createJPanel(loginPanelPath);
		jpanel.setLayout(null);
		
		
		//gamestart panel, login input panel operation
		JPanel gamePanel = new JPanel();
		JPanel loginPanel = displayThread.createJPanel(loginPanelPath);
		
		//2개의 jpanel position - golden ratio
		int goldenHeight = (int)(ds.HEIGHT / GOLDRATE);
		gamePanel.setBounds(0, 0, ds.WIDTH,ds.HEIGHT-goldenHeight);
		loginPanel.setBounds(0,ds.HEIGHT-goldenHeight,ds.WIDTH,goldenHeight);
		
		//inherit - jpanel
		jpanel.add(gamePanel);
		jpanel.add(loginPanel);

		//make inherit decoration - gamePanel
		gamePanel.setLayout(null);
		gamePanel.setOpaque(false);
		try {
			ImageIcon gameStartBtnIcon = new ImageIcon(getClass().getResource("..\\Resource\\gameStartButton_final4.png"));
			ImageIcon gameStartBtnIconRollover = new ImageIcon(getClass().getResource("..\\Resource\\gameStartButton_rollover.png"));
			ImageIcon gameStartBtnIconPressed = new ImageIcon(getClass().getResource("..\\Resource\\gameStartButton_pressed.png"));
			
			JButton gameStartBtn = new JButton(gameStartBtnIcon);
			gameStartBtn.setRolloverIcon(gameStartBtnIconRollover);
			gameStartBtn.setPressedIcon(gameStartBtnIconPressed);
			
			gameStartBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent me) {
					me.getComponent().setCursor(displayThread.handCursor);
				}
				@Override
				public void mouseExited(MouseEvent me) {
					me.getComponent().setCursor(displayThread.defaultCursor);
				}
			});
			
			gameStartBtn.setBounds(gamePanel.getWidth()/12, gamePanel.getHeight()/12,
					gamePanel.getWidth() * 8 / 10, gamePanel.getHeight() * 8 / 10);
			gamePanel.add(gameStartBtn);
			
			
			
			gameStartBtn.setContentAreaFilled(false);
			gameStartBtn.setFocusPainted(false);
			gameStartBtn.setBorderPainted(false);
			
			
		}catch(Exception e) {
			System.out.println("can't find image");
		}
		
		//make inherit decoration - loginPanel
		card = new CardLayout();
		loginPanel.setLayout(card);
		loginPanel.setOpaque(false);
		
		//input, loginOkPanel operation
		JPanel inputPanel = new JPanel();
		JPanel loginOkPanel = new JPanel();
		
		//inherit - loginPanel
		loginPanel.add(inputPanel);
		loginPanel.add(loginOkPanel);
		
		//make inherit decoration - loginPanel - inputPanel
		inputPanel.setOpaque(false);
		inputPanel.setLayout(null);
		try {
			ImageIcon loginLabelIcon = new ImageIcon(getClass().getResource("..\\Resource\\loginFormFinal3.png"));
			
			JLabel loginLabel = new JLabel(loginLabelIcon);
			//loginLabel.setBounds(10, 10,
					//gamePanel.getWidth() * 9 / 10, gamePanel.getHeight() * 9 / 10 + 100);
			loginLabel.setBounds(12, 75, 320, 192);
			inputPanel.add(loginLabel);
			
			//textField
			JTextField textFieldId = new JTextField();
			JTextField textFieldPwd = new JTextField();
			textFieldId.setBounds(15, 64, 197, 33);
			textFieldPwd.setBounds(15, 104, 197, 33);
			loginLabel.add(textFieldId);
			loginLabel.add(textFieldPwd);
			
			//login btn
			ImageIcon loginBtnIcon = new ImageIcon(getClass().getResource("..\\Resource\\loginBtn.png"));
			ImageIcon loginBtnRollover = new ImageIcon(getClass().getResource("..\\Resource\\loginBtnRollover.png"));
			ImageIcon loginBtnPressed = new ImageIcon(getClass().getResource("..\\Resource\\loginBtnPressed.png"));
			
			JButton loginBtn = new JButton(loginBtnIcon);
			loginBtn.setRolloverIcon(loginBtnRollover);
			loginBtn.setPressedIcon(loginBtnPressed);
			
			loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			loginBtn.setBounds(219,64, 86, 68);
			loginLabel.add(loginBtn);
			
			loginBtn.setContentAreaFilled(false);
			loginBtn.setFocusPainted(false);
			loginBtn.setBorderPainted(false);
			
			//sign up btn
			ImageIcon SignUpIcon = new ImageIcon(getClass().getResource("..\\Resource\\signUpBtn.png"));
			
			JButton signUpBtn = new JButton(SignUpIcon);
			
			signUpBtn.setBounds(25, 149, 60, 15);
			signUpBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			loginLabel.add(signUpBtn);
			
			signUpBtn.setContentAreaFilled(false);
			signUpBtn.setFocusPainted(false);
			signUpBtn.setBorderPainted(false);
			
			//get moust pos
			/*
			loginLabel.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					System.out.println("(" + e.getX() + "," + e.getY() + ")");
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
			*/
			
			
		}catch(Exception e) {
			System.out.println("can't apply to a image");
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
