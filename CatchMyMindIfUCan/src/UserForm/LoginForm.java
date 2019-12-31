
package UserForm;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Client.DisplayThread;
import sun.rmi.runtime.Log;

public class LoginForm implements UserForm{
	
	private volatile static LoginForm uniqueInstance;
	private JPanel jpanel;
	private DisplayThread displayThread;
	
	private LoginForm(DisplayThread ds) {
		this.displayThread = ds;
		
		jpanel = new JPanel();
		jpanel.setBackground(Color.YELLOW);
		
		JButton btn = new JButton("눌러");
		jpanel.add(btn);
		
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				displayThread.getCardLayout().show(
						displayThread.getContentPane(),
						"waitingRoom");
				
			}
		});
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
	public JPanel getJPanel() {
		return jpanel;
	}
}
