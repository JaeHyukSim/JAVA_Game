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
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.json.simple.parser.JSONParser;

import Client.DisplayThread;
import Client.UserInputThread;
import Client.UserMessageProcessor;
public class GrSketch extends JPanel implements UserForm{
	
	private volatile static GrSketch uniqueInstance;
	//4. 필수 변수들을 선언해 줍니다!!!!!
	private UserMessageProcessor userMessageProcessor;
	private DisplayThread displayThread;
	private Socket socket;
	private UserInputThread unt;
	private JSONParser jsonParser;
	
	private Runnable userRunnable;
	private Thread userThread; // ... 데이터 전송을 위한 thread를 선언해 줍니다!!!
	
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
	int thickness = 10;

	int startX;  // Ŭ�����۽ÿ� X��ǥ���� ����� ����
	int startY;  // Ŭ�����۽ÿ� Y��ǥ���� ����� ����
	int endX;    // Ŭ������ÿ� X��ǥ���� ����� ����
	int endY;    // Ŭ������ÿ� Y��ǥ���� ����� ����


    boolean tf = false;
    
    private String color;
    
    private GrSketch(DisplayThread dt, Socket socket){
    	color = "black";
    	this.displayThread = dt;
		this.socket = socket;
		userMessageProcessor = new UserMessageProcessor();
		unt = UserInputThread.getInstance(socket);
		jsonParser = new JSONParser();
		
		userRunnable = unt;
		
    	setLayout(null);
    	gui_panel = new JPanel();

    	//���ȭ���� ���
    	gui_panel.setBackground(Color.WHITE);


    	//��ġ������ ���� �õ��Ҽ� �ִ�.
    	gui_panel.setLayout(null);
    	
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

    	eraser_bt = new JButton(new ImageIcon((getClass().getResource("..\\Resource\\color\\eraser.jpg")))); 
    	
    	eraser_bt.setBorderPainted(false);
    	eraser_bt.setFocusPainted(false);
    	eraser_bt.setContentAreaFilled(false);

    	allClear = new JButton("ALL");
    	allClear.setFont(new Font("���ʷյ���",Font.BOLD, 17));
    
    	colorSelect_bt = new JButton("Color");

    	colorSelect_bt.setFont(new Font("���ʷյ���",Font.BOLD, 17));

    	thicknessInfo_label = new JLabel("");

    	thicknessInfo_label.setFont(new Font("���ʷյ���",Font.BOLD, 15));

    	thicknessControl_tf = new JTextField("15", 10);

    	thicknessControl_tf.setHorizontalAlignment(JTextField.CENTER);

    	thicknessControl_tf.setFont(new Font("���ʷյ���",Font.BOLD, 20));

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


    	paint_panel = new JPanel();

    	paint_panel.setBackground(Color.YELLOW); // �г��� �Ͼ� ����


    	paint_panel.setLayout(null); // paint_panel�� ���̾ƿ��� null���ְ� �г� ��ü�� setBounds�� ��ġ�� ������ �� �ִ�.

    	paint_panel.setBounds(0, 0, 880, 460);  //paint_panel �� ��ġ����

    	add(gui_panel);

    	add(paint_panel);

    	can = new MyCanvas();
    	
    	can.setSize(880,460); // ��ȭ�� ũ��
    	can.setBackground(Color.white);
    	paint_panel.add(can);
    	

    	MyHandler my = new MyHandler();
    	can.addMouseMotionListener(my);
    	can.addMouseListener(my);
    	
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
 
    }
	
  //9. Singleton pattern의 유일한 instance를 만들기 위해 getInstance()메소드를 만듭니다.
  	public static GrSketch getInstance(DisplayThread dt, Socket socket) {
  		if(uniqueInstance == null) {
  			synchronized (GrSketch.class) {
  				if(uniqueInstance == null) {
  					uniqueInstance = new GrSketch(dt, socket);
  				}
  			}
  		}
  		return uniqueInstance;
  	}
    public void setColor(String color) {
    	System.out.println("color : " + color);
    	switch(color) {
    	case "black":
    		can.cr = Color.black;	break;
    	case "red":
    		can.cr = Color.red;	break;
    	case "blue":
    		can.cr = Color.blue;	break;
    	case "green":
    		can.cr = Color.green;	break;
    	case "yellow":
    		can.cr = Color.yellow;	break;
    	case "pink":
    		can.cr = Color.pink;	break;
    	case "magenta":
    		can.cr = Color.magenta;	break;
    	case "orange":
    		can.cr = Color.orange;	break;
    	case "white":
    		can.cr = Color.white;	break;
    	case "whiteAll":
    		can.cr = Color.white;
    		Graphics g = can.getGraphics();
			g.clearRect(0, 0, can.getWidth(), can.getHeight());
			break;
    	}
    }
    public void draw(int x, int y) {
    	startX = x;
		startY = y;
		can.x=startX;
		can.y=startY;
	    
		can.repaint();
		thickness = Integer.parseInt(thicknessControl_tf.getText());
		endX = x;
		endY = y;
		startX = endX;
		startY = endY;
    }
	class MyHandler implements MouseMotionListener, ActionListener, MouseListener{

		

		@Override
		public void mouseDragged(MouseEvent e) {
			
			//서버로 보내봅시다. 
			//1. 메소드를 정해봅시다. 3700 -> 3702
			String sendData = "{";
			sendData += userMessageProcessor.getJSONData("method", "3700");
			sendData += "," + userMessageProcessor.getJSONData("x", String.valueOf(e.getX()));
			sendData += "," + userMessageProcessor.getJSONData("y", String.valueOf(e.getY()));
			sendData += "," + userMessageProcessor.getJSONData("color", String.valueOf(color));
			sendData += "}";
			
			//13. 데이터를 서버로 보냅니다!
			unt.setInputData(sendData);
			userThread = new Thread(userRunnable);
			userThread.start();
			/*
			startX = e.getX(); //���콺 �������� X��ǥ������ �ʱ�ȭ
			startY = e.getY(); //���콺 �������� Y��ǥ������ �ʱ�ȭ
			can.x=startX;
			can.y=startY;
		    
			can.repaint();
    		thickness = Integer.parseInt(thicknessControl_tf.getText());

    		//�ؽ�Ʈ �ʵ� �κп��� ���� ������ thickness������ ����

    		

    		endX = e.getX(); //�巡�� �Ǵ� �������� x��ǥ ����

    	
    		endY = e.getY();  //�巡�� �Ǵ� �������� y��ǥ ����


    		startX = endX;   // ���ۺκ��� �������� �巹�׵� X��ǥ�� ������ ������ �̾� �׷��� �� �ִ�.

    		startY = endY;    // ���ۺκ��� �������� �巹�׵� Y��ǥ�� ������ ������ �̾� �׷��� �� �ִ�.
*/
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
    			color = "black";
    				can2.cr=Color.BLACK;	

    				}else if(o == colorRed){

    				//	if(tf == false) g.setColor(Color.RED);
    				//	else g.setColor(selectedColor);
    				//	g.setColor(Color.RED);
    					color = "red";
    					can2.cr=Color.red;

    				}else if(o == colorBlue){

    			//		if(tf == false) g.setColor(Color.BLUE);
    			//		else g.setColor(selectedColor);
    				//g.setColor(Color.BLUE);
    					color = "blue";
    					can2.cr=Color.blue;
    					

    				}else if(o == colorGreen){

    				//	if(tf == false) g.setColor(Color.GREEN);
    				//	else g.setColor(selectedColor);
    				//	g.setColor(Color.GREEN);
    					
    					color = "green";
    					can2.cr=Color.GREEN;
    					

    				}else if(o == colorYellow){

    				//	if(tf == false) g.setColor(Color.YELLOW);
    				//	else g.setColor(selectedColor);
    				//	g.setColor(Color.YELLOW);
    					color = "yellow";
    					can2.cr=Color.yellow;

    				}else if(o == colorPink){

    				//	if(tf == false) g.setColor(Color.PINK);
    				//	else g.setColor(selectedColor);
    				//	g.setColor(Color.PINK);
    					color = "pink";
    					can2.cr=Color.PINK;

    				}else if(o == colorViolet){

    				//	if(tf == false) g.setColor(Color.MAGENTA);
    				//	else g.setColor(selectedColor);
    				//	g.setColor(Color.MAGENTA);
    					color = "magenta";
    					can2.cr=Color.MAGENTA;
    					
    					

    				}else if(o == colorOrange){

    				//	if(tf == false) g.setColor(Color.ORANGE);
    				//	else g.setColor(selectedColor);
    					
    				//	g.setColor(new Color(255,130,51));
    					color = "orange";
    					can2.cr=Color.ORANGE;
    					

    				}else if(o == colorBrown){

    				//	if(tf == false) g.setColor(Color.getHSBColor(153, 102, 51));
    				//	else g.setColor(selectedColor);
    				//	g.setColor(new Color(194,113,81));
    					color = "black";
    					can2.cr=Color.black;
    					
    				}else if(o == allClear){
    					color = "whiteAll";
    					//서버로 보내봅시다. 
    					//1. 메소드를 정해봅시다. 3700 -> 3702
    					String sendData = "{";
    					sendData += userMessageProcessor.getJSONData("method", "3710");
    					sendData += "," + userMessageProcessor.getJSONData("color", String.valueOf(color));
    					sendData += "}";
    					
    					//13. 데이터를 서버로 보냅니다!
    					unt.setInputData(sendData);
    					userThread = new Thread(userRunnable);
    					userThread.start();
    					
    					
    				}else if(o == eraser_bt){
    					color = "white";
    					can2.cr=Color.WHITE;
    				
    				}else if(o == colorSelect_bt) {
    					
    					color = "selectColor";
    					Color selCr = JColorChooser.showDialog(null, "Color", Color.ORANGE);
    					can2.cr = selCr;
    				}

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("here!!!!!!!!!!!!!!!!!!!!");
			//서버로 보내봅시다. 
			//1. 메소드를 정해봅시다. 3700 -> 3702
			String sendData = "{";
			sendData += userMessageProcessor.getJSONData("method", "3720");
			sendData += "}";
			
			//13. 데이터를 서버로 보냅니다!
			unt.setInputData(sendData);
			userThread = new Thread(userRunnable);
			userThread.start();
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

	@Override
	public void display() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformMethod() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void operation(String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPanel getJPanel() {
		// TODO Auto-generated method stub
		return null;
	}
    
}	
