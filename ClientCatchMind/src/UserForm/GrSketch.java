package UserForm;
import java.awt.BasicStroke;





import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Canvas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;






public class GrSketch extends JPanel {


	
	JPanel gui_panel, paint_panel;
	
	MyCanvas can;

	
	JButton pencil_bt, eraser_bt, colorRed, colorBlue, colorGreen, colorYellow, colorPink, colorViolet, colorOrange, colorBrown;
	
	JButton allClear;
			
	
	


	JButton colorSelect_bt;


	JLabel thicknessInfo_label;
	



	JTextField thicknessControl_tf;

	
	JSlider thicknessControl_slider;
	
	
	
	Color selectedColor;

	// Graphics2D Ŭ������ ����� ���� ����
	Graphics graphics;

	//Graphics2D�� ���� Graphics�� ��������
	Graphics2D g;

	
	
	//�׷����� ���� ���⸦ �����Ҷ� ���氪�� ����Ǵ� ����
	int thickness = 10;

	int startX;  // Ŭ�����۽ÿ� X��ǥ���� ����� ����
	int startY;  // Ŭ�����۽ÿ� Y��ǥ���� ����� ����
	int endX;    // Ŭ������ÿ� X��ǥ���� ����� ����
	int endY;    // Ŭ������ÿ� Y��ǥ���� ����� ����


    boolean tf = false;
    
    
    

    	public GrSketch(){
    	//Paint Ŭ������ ����Ʈ(Default)�����ڷ� �⺻���� GUI ����, �����ϴ� �κ�.

 
    	//���� �������� ���̾ƿ��� �ʱ�ȭ��Ŵ - �츮�� ���� ���������ϰ���
    	setLayout(null);


    	//�������� Ÿ��Ʋ
  //  	setTitle(null);

    	//������ ������
  //  	setSize(880, 600);

    	 //������
  //  	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	

    	

    	//�׸��� �������� �ö󰡴� ����
    	gui_panel = new JPanel();

    	//���ȭ���� ���
    	gui_panel.setBackground(Color.WHITE);


    	//��ġ������ ���� �õ��Ҽ� �ִ�.
    	gui_panel.setLayout(null);
    	

    	//���� ��ư ����  --> ���Ŀ� ���� �׸����� ��������
    	
    	pencil_bt = new JButton(new ImageIcon((getClass().getResource("..\\Resource\\color\\black1.jpg")))); 
    	
    	pencil_bt.setBorderPainted(false);
    	pencil_bt.setFocusPainted(false);
    	pencil_bt.setContentAreaFilled(false);
    	
    	
    	colorRed = new JButton(new ImageIcon((getClass().getResource("..\\Resource\\color\\red1.jpg")))); 
    	
    	colorRed.setBorderPainted(false);
    	colorRed.setFocusPainted(false);
    	colorRed.setContentAreaFilled(false);
    	
    	
    	colorBlue = new JButton(new ImageIcon((getClass().getResource("..\\Resource\\color\\blue1.jpg")))); 
    	
    	colorBlue.setBorderPainted(false);
    	colorBlue.setFocusPainted(false);
    	colorBlue.setContentAreaFilled(false);
    	
    	colorGreen = new JButton(new ImageIcon((getClass().getResource("..\\Resource\\color\\green1.jpg"))));  
    	
    	colorGreen.setBorderPainted(false);
    	colorGreen.setFocusPainted(false);
    	colorGreen.setContentAreaFilled(false);
    	
    	colorYellow = new JButton(new ImageIcon((getClass().getResource("..\\Resource\\color\\yellow1.jpg")))); 
    	
    	colorYellow.setBorderPainted(false);
    	colorYellow.setFocusPainted(false);
    	colorYellow.setContentAreaFilled(false);
    	
    	
    	colorPink = new JButton(new ImageIcon((getClass().getResource("..\\Resource\\color\\pink1.jpg")))); 
    	
    	colorPink.setBorderPainted(false);
    	colorPink.setFocusPainted(false);
    	colorPink.setContentAreaFilled(false);
    	
    	colorViolet = new JButton(new ImageIcon((getClass().getResource("..\\Resource\\color\\violet1.jpg")))); 
    	
    	colorViolet.setBorderPainted(false);
    	colorViolet.setFocusPainted(false);
    	colorViolet.setContentAreaFilled(false);
    	
    	colorOrange = new JButton(new ImageIcon((getClass().getResource("..\\Resource\\color\\orange1.jpg")))); 
    	
    	colorOrange.setBorderPainted(false);
    	colorOrange.setFocusPainted(false);
    	colorOrange.setContentAreaFilled(false);

    	
    	colorBrown = new JButton(new ImageIcon((getClass().getResource("..\\Resource\\color\\brown1.jpg")))); 
    	
    	colorBrown.setBorderPainted(false);
    	colorBrown.setFocusPainted(false);
    	colorBrown.setContentAreaFilled(false);

    	
    	
    	
    
    //  ���� ��ư�� �۾�ü�� ũ�� ����
    //	pencil_bt.setFont(new Font("���ʷյ���",Font.BOLD, 15));
	
    	// ���� ��ư�� ���� 
   // 	pencil_bt.setBackground(Color.WHITE);

    	
    	eraser_bt = new JButton(new ImageIcon((getClass().getResource("..\\Resource\\color\\eraser.jpg")))); 
    	
    	eraser_bt.setBorderPainted(false);
    	eraser_bt.setFocusPainted(false);
    	eraser_bt.setContentAreaFilled(false);

    //	eraser_bt.setFont(new Font("���ʷյ���",Font.BOLD, 20));

    	//���찳 ��ư�� ���� �������

   // 	eraser_bt.setBackground(Color.WHITE);

    	
    	
    	
    	
    	allClear = new JButton("ALL");
    	allClear.setFont(new Font("���ʷյ���",Font.BOLD, 17));
    
    	
    	
    	colorSelect_bt = new JButton("Color");

    	colorSelect_bt.setFont(new Font("���ʷյ���",Font.BOLD, 17));

  //  	colorSelect_bt.setBackground(Color.WHITE);


    //	colorSelect_bt.setBorderPainted(false);
    //	colorSelect_bt.setFocusPainted(false);
    //	colorSelect_bt.setContentAreaFilled(false);
	

    	thicknessInfo_label = new JLabel("");

    	thicknessInfo_label.setFont(new Font("���ʷյ���",Font.BOLD, 15));

    	
    	
    	//���� ���� �Է� �ؽ�Ʈ â�ʵ� ����

    	thicknessControl_tf = new JTextField("15", 10);

    	thicknessControl_tf.setHorizontalAlignment(JTextField.CENTER);

    	thicknessControl_tf.setFont(new Font("���ʷյ���",Font.BOLD, 20));
   // 	thicknessControl_tf.setBorderPainted(false);
   //	thicknessControl_tf.setFocusPainted(false);
	    
   //	thicknessControl_tf.setContentAreaFilled(false);
  	

    	
   // 	colorRed, colorBlue, colorGreen, colorYellow, colorPink, colorViolet, colorOrange, colorBrown;
		
    	//���ʹ�ư ��ġ����

    	pencil_bt.setBounds(30, 0, 80, 50);
    	colorRed.setBounds(110, 0, 80, 50);
    	colorBlue.setBounds(190, 0, 80, 50);
    	colorGreen.setBounds(270, 0, 80, 50);
    	colorYellow.setBounds(350, 0, 80, 50);
    	
    	allClear.setBounds(440, 0, 70, 55);
    
    	colorPink.setBounds(30, 55, 80, 50);
    	colorViolet.setBounds(110, 55, 80, 50);
    	colorOrange.setBounds(190, 55, 80, 50);
    	colorBrown.setBounds(270, 55, 80, 50);


    	//���찳 ��ġ����
    	eraser_bt.setBounds(350, 50, 90, 50);

    	

    	//���� �� ��ġ ����
    	colorSelect_bt.setBounds(530, 0, 70, 55);

    	

    	//�������� �� ��ġ ����
    	thicknessInfo_label.setBounds(630, 0, 80, 55);

    	

    	//���� ���� ����Ʈ �ʵ� ��ġ ����
    	thicknessControl_tf.setBounds(670, 0, 70, 55);

    	// (500, 0, 880, 100)

    	

    
    	//gui_panel�� �����ʹ�ư �߰�
    	gui_panel.add(pencil_bt); 
    	gui_panel.add(colorRed);
    	gui_panel.add(colorBlue);
    	gui_panel.add(colorGreen);
    	gui_panel.add(colorYellow);
    	gui_panel.add(allClear);
    	gui_panel.add(colorPink);
    	gui_panel.add(colorViolet);
    	gui_panel.add(colorOrange);
    	gui_panel.add(colorBrown);
    	
    	
    	gui_panel.add(eraser_bt);  //gui_panel��  ���찳 ��ư �߰�


    	gui_panel.add(colorSelect_bt); //gui_panel�� ���� ���� �߰�

    	gui_panel.add(thicknessInfo_label); //gui_panel�� ���� ���� �� �߰�

    	gui_panel.add(thicknessControl_tf); // gui_panel�� �������� �ؽ�Ʈ�ʵ� �߰�


    	gui_panel.setBounds(0, 450, 880, 150);  // gui_panel�� ������ ���� ��ġ�� ��ġ ����

    	

    	

    	

    	//  �г� ���� /////////////////////////////////////////////////////////////////

    	

    	// �׸��� �׷��� ��ȭ�� �г� ����

    	paint_panel = new JPanel();

    	paint_panel.setBackground(Color.YELLOW); // �г��� �Ͼ� ����


    	paint_panel.setLayout(null); // paint_panel�� ���̾ƿ��� null���ְ� �г� ��ü�� setBounds�� ��ġ�� ������ �� �ִ�.

    	paint_panel.setBounds(0, 0, 880, 460);  //paint_panel �� ��ġ����



    	

    	

    	//���� �����ӿ� �ΰ��� �г� �߰� - ��ġ�� ������ �� ��������

    	add(gui_panel);

    	add(paint_panel);

    	

    	//���� �������� ���̰� �Ѵ�.

    	setVisible(true);

    	   //	paint_panel.getGraphics();

//	  	graphics = getGraphics(); //�׷��� �ʱ�ȭ

    	
    	//������ graphics ������ 2D �� ��ȯ�� 2D�� �ʱ�ȭ��(2D�� ����ϴ� ������ ���� ����� ���õ� ����� �����ϱ� ����. 2DŬ������ ��üȭ��)
	  	
	  
//    	g = (Graphics2D)graphics;


    	// �׷��� ���� ������  selectedColor�� ������ ����

//    	g.setColor(selectedColor);
    	
    	can = new MyCanvas();
    	
    	can.setSize(880,460); // ��ȭ�� ũ��
    	can.setBackground(Color.white);
    	paint_panel.add(can);
    	

    	MyHandler my = new MyHandler();
    	
    	can.addMouseMotionListener(my);
    	
		// �׼�ó�� �κ�//////////////////////////////////////////////////////////////////////

    	
    
  /*
    	can.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				//Ŭ�� �̺�Ʈ ó��
			}
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			//	startX = e.getX(); //���콺 �������� X��ǥ������ �ʱ�ȭ
			//	startY = e.getY(); //���콺 �������� Y��ǥ������ �ʱ�ȭ
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
    		   		
    	});
    	
*/
   	 

    	//paint_panel�� ���콺 ��Ǹ����� �߰�

 //   	can.addMouseMotionListener(new PaintDraw());

    	//�����ʹ�ưó��
/*		
    	pencil_bt.addActionListener(new ActionListener());
    	
    	colorRed.addActionListener(new ToolActionListener());
    	colorBlue.addActionListener(new ToolActionListener());
    	colorGreen.addActionListener(new ToolActionListener());
    	colorYellow.addActionListener(new ToolActionListener());
    	allClear.addActionListener(new ToolActionListener());
    	
    	colorPink.addActionListener(new ToolActionListener());
    	colorViolet.addActionListener(new ToolActionListener());
    	colorOrange.addActionListener(new ToolActionListener());
    	colorBrown.addActionListener(new ToolActionListener());
  */  	
    	
    	pencil_bt.addActionListener(my);
    	
    	colorRed.addActionListener(my);
    	colorBlue.addActionListener(my);
    	colorGreen.addActionListener(my);
    	colorYellow.addActionListener(my);
    	allClear.addActionListener(my);
    	
    	colorPink.addActionListener(my);
    	colorViolet.addActionListener(my);
    	colorOrange.addActionListener(my);
    	colorBrown.addActionListener(my);
    	
    	colorSelect_bt.addActionListener(my);
    	//���찳 ��ư ó��

   // 	eraser_bt.addActionListener(new ToolActionListener());
    	eraser_bt.addActionListener(my);
    	
    	/*
  	colorSelect_bt.addActionListener(new ActionListener(){
    		//�����ư �׼�ó���� �͸�Ŭ������ �ۼ�
    
    		public void actionPerformed(ActionEvent e){
    			
    			tf = true;
    			JColorChooser chooser = new JColorChooser();  // JColorChooserŬ���� ��üȭ
    			
    			selectedColor = chooser.showDialog(null, "Color", Color.ORANGE);
    			//selectedColor�� ���õ� ������ �ʱ�ȭ
    			
    			g.setColor(selectedColor);
    			
    			// �׷����� ���� ������ selectedColor�� �Ű������� �Ͽ� ����
  			
    		
    		}
    		
    		
    	});
    	
    	
    	*/

    }
    // ������ ��!!!
    	
    	
    	
    	
	
	class MyHandler implements MouseMotionListener, ActionListener{

		

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			// setTitle("");
			
			startX = e.getX(); //���콺 �������� X��ǥ������ �ʱ�ȭ
			startY = e.getY(); //���콺 �������� Y��ǥ������ �ʱ�ȭ
			can.x=startX;
			can.y=startY;
		    
			can.repaint();
			
			
			

    		thickness = Integer.parseInt(thicknessControl_tf.getText());

    		//�ؽ�Ʈ �ʵ� �κп��� ���� ������ thickness������ ����

    		

    		endX = e.getX(); //�巡�� �Ǵ� �������� x��ǥ ����

    	
    		endY = e.getY();  //�巡�� �Ǵ� �������� y��ǥ ����

    	
    		//can.gg.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND,0));

    		//can.gg.drawLine(startX+5, startY+25, endX+5 ,endY+25);

    		//������ �׷����� �Ǵ� �κ�

    		

    		startX = endX;   // ���ۺκ��� �������� �巹�׵� X��ǥ�� ������ ������ �̾� �׷��� �� �ִ�.

    		startY = endY;    // ���ۺκ��� �������� �巹�׵� Y��ǥ�� ������ ������ �̾� �׷��� �� �ִ�.

			
			
			
			
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

    		
    		Object o = e.getSource();
    		MyCanvas can2 = (MyCanvas)can;
    		//////////////////////////////////////////////////////////////////
    		
    		
    		
    		if(o == pencil_bt){

    			//		if(tf == false) g.setColor(Color.BLACK);
    			//		else g.setColor(selectedColor);
    			//	g.setColor(Color.BLACK);
    				can2.cr=Color.BLACK;	

    				}else if(o == colorRed){

    				//	if(tf == false) g.setColor(Color.RED);
    				//	else g.setColor(selectedColor);
    				//	g.setColor(Color.RED);
    					can2.cr=Color.red;

    				}else if(o == colorBlue){

    			//		if(tf == false) g.setColor(Color.BLUE);
    			//		else g.setColor(selectedColor);
    				//g.setColor(Color.BLUE);
    					
    					can2.cr=Color.blue;
    					

    				}else if(o == colorGreen){

    				//	if(tf == false) g.setColor(Color.GREEN);
    				//	else g.setColor(selectedColor);
    				//	g.setColor(Color.GREEN);
    					
    					
    					can2.cr=Color.GREEN;
    					

    				}else if(o == colorYellow){

    				//	if(tf == false) g.setColor(Color.YELLOW);
    				//	else g.setColor(selectedColor);
    				//	g.setColor(Color.YELLOW);
    					
    					can2.cr=Color.yellow;

    				}else if(o == colorPink){

    				//	if(tf == false) g.setColor(Color.PINK);
    				//	else g.setColor(selectedColor);
    				//	g.setColor(Color.PINK);

    					can2.cr=Color.PINK;

    				}else if(o == colorViolet){

    				//	if(tf == false) g.setColor(Color.MAGENTA);
    				//	else g.setColor(selectedColor);
    				//	g.setColor(Color.MAGENTA);

    					can2.cr=Color.MAGENTA;
    					
    					

    				}else if(o == colorOrange){

    				//	if(tf == false) g.setColor(Color.ORANGE);
    				//	else g.setColor(selectedColor);
    					
    				//	g.setColor(new Color(255,130,51));

    					can2.cr=Color.ORANGE;
    					

    				}else if(o == colorBrown){

    				//	if(tf == false) g.setColor(Color.getHSBColor(153, 102, 51));
    				//	else g.setColor(selectedColor);
    				//	g.setColor(new Color(194,113,81));

    					can2.cr=Color.black;
    					
    				}else if(o == allClear){

    					can2.cr=Color.WHITE;
    				//    g.setColor(Color.white);
    				//	g.fillRect(0, 0, 880 , 482);
    					
    					Graphics g = can2.getGraphics();
    					g.clearRect(0, 0, can.getWidth(), can.getHeight());

    				}else if(o == eraser_bt){

    					can2.cr=Color.WHITE;
    				
    					
    					
    				//	g.setColor(Color.WHITE);

    				}else if(o == colorSelect_bt) {
    					
    					Color selCr = JColorChooser.showDialog(null, "Color", Color.ORANGE);
    					can2.cr = selCr;
    				}


		}
		
		
	}
    
    

    

/*
 * public class PaintDraw implements MouseMotionListener{
 * 
 * 
 * 
 * @Override
 * 
 * public void mouseDragged(MouseEvent e){
 * 
 * // paint_panel ���� ���콺 �巡�� �׼��� ó���� �� �޼ҵ� ����
 * 
 * 
 * 
 * thickness = Integer.parseInt(thicknessControl_tf.getText());
 * 
 * //�ؽ�Ʈ �ʵ� �κп��� ���� ������ thickness������ ����
 * 
 * 
 * 
 * endX = e.getX(); //�巡�� �Ǵ� �������� x��ǥ ����
 * 
 * 
 * 
 * endY = e.getY(); //�巡�� �Ǵ� �������� y��ǥ ����
 * 
 * 
 * g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND,0));
 * 
 * g.drawLine(startX+5, startY+25, endX+5 ,endY+25);
 * 
 * //������ �׷����� �Ǵ� �κ�
 * 
 * 
 * 
 * startX = endX; // ���ۺκ��� �������� �巹�׵� X��ǥ�� ������ ������ �̾� �׷��� �� �ִ�.
 * 
 * startY = endY; // ���ۺκ��� �������� �巹�׵� Y��ǥ�� ������ ������ �̾� �׷��� �� �ִ�.
 * 
 * 
 * 
 * 
 * }
 * 
 * 
 * 
 * @Override
 * 
 * public void mouseMoved(MouseEvent e){
 * 
 * 
 * }
 * 
 * 
 * }
 */


	
}	
