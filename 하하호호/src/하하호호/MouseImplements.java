package 하하호호;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseImplements implements MouseListener{
	YourFormHere yfh;
	DisplayThread dt;
	
	public MouseImplements(YourFormHere yfh, DisplayThread dt) {
		this.yfh = yfh; this.dt = dt;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		//JLabel jl = (JLabel)e.getSource();
		yfh.mouseLabel.setText("(" + x + "," + y + ")");
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
