package 하하호호;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class YourFormHere {
	private DisplayThread dt;
	private Container contentPane;
	
	public JLabel mouseLabel;
	
	public YourFormHere(DisplayThread dt) {
		this.dt = dt;
		contentPane = dt.getContentPane();
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(null);
		
		mouseLabel = new JLabel();
		settingLabel();
		
		//여기부터 코드 작성하시면 됩니다.
	}
	public void settingLabel() {
		contentPane.add(mouseLabel);
		mouseLabel.setText("여기에 좌표 출력");
		mouseLabel.setBounds(0,0,100,30);
		contentPane.addMouseListener(new MouseImplements(this, dt));
	}
}
