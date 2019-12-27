package 하하호호;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class YourFormHere {
	private DisplayThread dt;
	private Container contentPane;
	
	
	
	public JLabel mouseLabel;
	
	public YourFormHere(DisplayThread dt) {
		this.dt = dt;
		contentPane = dt.getContentPane();
		contentPane.setBackground(Color.WHITE);
		
		mouseLabel = new JLabel();
		settingLabel();
		mouseLabel.setForeground(Color.WHITE);
		contentPane.setLayout(null);
		
		//여기부터 코드 작성하시면 됩니다.
		JPanel gamePanel = new JPanel();
		JPanel login = new JPanel();
		gamePanel.setBackground(Color.BLACK);
		login.setBackground(Color.GREEN);
		dt.setSize(400,600);
		
		gamePanel.setBounds(0,0,dt.getSize().width,dt.getSize().height/2);
		login.setBounds(0,dt.getSize().height/2,dt.getSize().width,dt.getSize().height /2);
		contentPane.add(gamePanel);	contentPane.add(login);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dt.setBounds((screenSize.width - dt.getSize().width)/2,(screenSize.height - dt.getSize().height)/2,
				dt.getSize().width, dt.getSize().height);
		
		gamePanel.setLayout(null);
		login.setLayout(null);
		
		ImageIcon gameIcon = new ImageIcon("C:\\javaDev\\ProjectImg\\gameStart.jpg");
		JButton gameBtn = new JButton(gameIcon);
		
		gamePanel.add(gameBtn);
		gameBtn.setBounds(50,25,300,250);
		gameBtn.setEnabled(false);
		
		JLabel id = new JLabel("아이디");
		JLabel pwd = new JLabel("비밀번호");
		JTextField idt = new JTextField();
		JPasswordField pwdt = new JPasswordField();
		JButton sign = new JButton("회원가입");
		JButton logon = new JButton("로그인");
		
		id.setForeground(Color.RED);
		login.add(id);	login.add(pwd); login.add(idt); login.add(pwdt); login.add(sign); login.add(logon);
		id.setBounds(50, 50, 60, 30);
		pwd.setBounds(40,90,60,30);
		idt.setBounds(110, 50, 200, 30);
		pwdt.setBounds(110, 90, 200, 30);
		sign.setBounds(110, 130, 90, 30);
		logon.setBounds(210, 130, 90, 30);
		
		logon.addActionListener(new ButtonActionListener(this));
		
	}
	public void settingLabel() {
		contentPane.add(mouseLabel);
		mouseLabel.setText("여기에 좌표 출력");
		mouseLabel.setBounds(0,0,100,30);
		contentPane.addMouseListener(new MouseImplements(this, dt));
	}
}
