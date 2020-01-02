import java.awt.Graphics;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.*; //window와 관련된 클래스들이 모여있따리!

public class Login extends JPanel{

	Image back;
	JLabel la1, la2;
	JTextField tf;
	JPasswordField pf;
	JButton b1, b2;
	
	Login(){
		back = Toolkit.getDefaultToolkit().getImage("C:\\Users\\sist\\image\\blackhole3.jpg");
		
		la1 = new JLabel("ID", JLabel.RIGHT);
		la1.setForeground(Color.white);
		
		la2 = new JLabel("Password", JLabel.RIGHT);
		la2.setForeground(Color.white);
		
		tf = new JTextField();
		
		pf = new JPasswordField();
		
		b1 = new JButton("Login");
		
		b2 = new JButton("Out");
		
		setLayout(null);
		//배치
		int a;
		a = 0;
		
		JPanel p = new JPanel();
		p.add(b1);
		p.add(b2);
		p.setBounds(390,405,235,35);
		p.setOpaque(false);
		 
		la1.setBounds(390-a, 330, 80, 30);
		tf.setBounds(475-a,330,150,30);
		
		la2.setBounds(390-a, 365, 80, 30);
		pf.setBounds(475-a,365,150,30);
		
		add(la1); add(tf); add(la2); add(pf); add(p);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(back, 0, 0, getWidth(), getHeight(),this);
	}
	
}
