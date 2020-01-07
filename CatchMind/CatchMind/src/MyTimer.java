import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class MyTimer extends JProgressBar{
	
	static JProgressBar timeBar;
	
	MyTimer(){
		timeBar = new JProgressBar(0, 120);
		timeBar.setStringPainted(false);
		timeBar.setValue(120);
	}
	
	void start() {
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				timeBar.setValue(timeBar.getValue()-1);
			}
		};
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.SECONDS);
	   }
}

