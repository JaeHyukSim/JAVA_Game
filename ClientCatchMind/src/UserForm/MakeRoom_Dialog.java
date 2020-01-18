package UserForm;


import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Client.UserMessageProcessor;


public class MakeRoom_Dialog extends JDialog implements ActionListener{

	private UserMessageProcessor userMessageProcessor;
	public JLabel la1, la2;
	public JTextField tf;
	public JPasswordField pf;
	public JButton b1, b2;
	
	private WaitingRoomForm waitingRoomForm;
	
	public MakeRoom_Dialog(WaitingRoomForm wr) {
		
		
		this.waitingRoomForm = wr;
		
		this.setBounds(700, 300, 500, 500);
		
		userMessageProcessor = wr.getUserMessageProcessor();
		
		
		la1 = new JLabel("방제목", JLabel.RIGHT);
		la2 = new JLabel("방비밀번호", JLabel.RIGHT);
		
		tf = new JTextField();
		pf = new JPasswordField();
		
		b1 = new JButton("방생성");
		b2 = new JButton("취소");
		b1.addActionListener(this);
		b2.addActionListener(this);
		
		// setLayout(null);
		
		la1.setBounds(140,130,80,30);
		tf.setBounds(235,130,150,30);
		la2.setBounds(150,165,80,30);
		pf.setBounds(235,165,80,30);
		//pf.setText("");
		
		JPanel p = new JPanel();
		p.add(b1);
		p.add(b2);
		p.setBounds(190, 205, 235, 35);
		p.setOpaque(false);
		
		this.add(la1);
		this.add(la2);
		this.add(tf);
		this.add(pf);
		this.add(p);
		
		this.setModal(true);
		this.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==b1)
		{
			// method : 3000 -> 3002 -> 1. 방 Arraylist에 추가해줘야대 2. 대기실 인원에서 자기를 제외해야돼
			String sendData = "{";
			sendData += (String)userMessageProcessor.getJSONData("method", "3000");
			sendData += "," + (String)userMessageProcessor.getJSONData("roomCreate", tf.getText());
			sendData += "," + (String)userMessageProcessor.getJSONData("roomPwd", String.valueOf(pf.getPassword()));
			sendData += "}";
			
			tf.setText("");
			
			System.out.println("method = 3000, sendData : " + sendData);
			
			//13. 데이터를 서버로 보냅니다!
			waitingRoomForm.getUnt().setInputData(sendData);
			waitingRoomForm.getUnt().pushMessage();
			/*
			sendData = "{";
			sendData += (String)userMessageProcessor.getJSONData("method", "3400");
			sendData += "}";
			//13. 데이터를 서버로 보냅니다!
			waitingRoomForm.getUnt().setInputData(sendData);
			Runnable r2 = waitingRoomForm.getUnt();
			Thread t1 = new Thread(r2);
			t1.start();
			*/
		}
	}
	
	
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		// TODO Auto-generated method stub
//		if(e.getSource() == b1)
//		{
//		
//			for(int i = 0 ; i > ScreenVO.getS_VO().room_Info.length ; i++)
//			{
//				TableModel model =  ScreenVO.getS_VO().w_P.table1.getModel();
//				if(ScreenVO.getS_VO().room_Info[i].room_State.equals("비활성화"));
//				{
//					for(int j = 0 ; j > 6 ; i++)
//					{
//						int room_UserNum = 0;
//						
//						//{"방번호", "방제목", "방장", "인원", "비고", "상태"};
//						
//						// 방번호
//						ScreenVO.getS_VO().room_Info[i].room_Num = String.valueOf(i);
//						model.setValueAt(ScreenVO.getS_VO().room_Info[i].room_UserNum, i , j);
//						
//						// 방제목
//						model.setValueAt(tf.getText(), i, j);
						
//						// 방장
//						model.setValueAt(ScreenVO.getS_VO().my_Info.user_Name, i, j);
//						
//						// 인원
//						ScreenVO.getS_VO().room_Info[i].room_UserNum = 	"1";
//						model.setValueAt(ScreenVO.getS_VO().room_Info[i].room_UserNum, i, j);
//						
//						// 비고
//						if(ScreenVO.getS_VO().room_Info[i].room_PassState == null)
//							ScreenVO.getS_VO().room_Info[i].room_PassState ="비활성화";
//						else
//							ScreenVO.getS_VO().room_Info[i].room_PassState ="활성화";
//						
//						ScreenVO.getS_VO().room_Info[i].room_PassNum = String.valueOf(pf.getPassword());	
//						model.setValueAt(ScreenVO.getS_VO().room_Info[i].room_PassState, i, j);
//						
//						// 상태
//						ScreenVO.getS_VO().room_Info[i].room_State = "대기중";
//						model.setValueAt(ScreenVO.getS_VO().room_Info[i].room_State, i, j);	
//					}
//				}
//			// 종료
//			this.dispose();
//			this.setVisible(false);
//			tf.setText(null);
//			pf.setText(null);
//			tf.requestFocus();
//			}
//		
//			if(e.getSource() == b1)
//			{
//			
//			}
//
//		}
//	}
}
