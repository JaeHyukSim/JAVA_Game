package UserForm;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Client.UserMessageProcessor;

public class InviteDialog extends JDialog implements ActionListener, MouseListener{

	private UserMessageProcessor userMessageProcessor;
	
	private GameRoomForm gameRoomForm;
	
	private DefaultTableModel model1;
	
	private JTable table1;
	
	private JButton b1;
	
	private JLabel la1;
	
	
	
	
	
	
	public InviteDialog(GameRoomForm grf)  // 이렇게 넣어준 이유는? (GameRoomForm grf)??
	{
		
		this.gameRoomForm = grf;
		
		setBounds(700,300,300,300);  // 크기와 위치를 잡아줌
		
		userMessageProcessor = grf.getUserMessageProcessor();
		
		la1 = new JLabel("친구 추가", JLabel.CENTER);
		la1.setFont(new Font("Black Han Sans",Font.BOLD, 15)); 
		
		b1 = new JButton("확인");
		
		
		
		
		String[] col1 = {"ID","Level"};
		String[][] row1 = new String[0][col1.length];  //col1.length=> 2
		
		model1 = new DefaultTableModel(row1, col1) {

			@Override
			public boolean isCellEditable(int row, int column) {  // 더블클릭시 수정방지 
				// TODO Auto-generated method stub
				return false;
			}
			
		};
		//dialog에 table1을 붙여야 합니다
		
		JTable table1 = new JTable(model1);        // table1에 model1을 올림 
		JScrollPane js = new JScrollPane(table1);   // table1에 스크롤기능 추가해줌
		table1.setBounds(50,50,50,50);
		this.add(js);
		
		
		add("North", la1); 

		add("Center", js);

		add("South", b1); 
		
		
		
		js.setVisible(true);
		
	}// 생성자 끝







	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == table1)
		{
			
			
			
		}
		
		
		
	}







	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		// 
		
		
	}







	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}







	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}







	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}







	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
}
