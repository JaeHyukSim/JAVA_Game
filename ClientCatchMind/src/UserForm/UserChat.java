
package UserForm;

import java.awt.Font;
import java.net.Socket;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.text.StyledEditorKit.BoldAction;

import Client.DisplayThread;

public class UserChat extends JWindow {
	
	JTextField tf = new JTextField();
   int userNum, x, y;
   int clock;
   Runnable taskRunnable;
   Thread taskThread;
   Runnable clockRunnable;
   Thread clockThread;
   Runnable locationRunnable;
   Thread locationThread;
   
   public UserChat(int userNum) {
      this.userNum = userNum;
      tf.setFont(new Font(getName(), Font.BOLD, 20));
      add(tf);
      clockRunnable = new ClockThread();
      clockThread = new Thread(clockRunnable);
      clock = 100;
      clockThread.start();
      taskRunnable = new printChatTread();
      taskThread = new Thread(taskRunnable);
      taskThread.start();
   }
   
   public void printChat(int x, int y, String message) {
	   this.x = x;
	   this.y = y;
	   tf.setText(" "+message);
      clock = 0;
   }
   
   class ClockThread implements Runnable {

      @Override
      public void run() {
         // TODO Auto-generated method stub
         try {
            while(true) {
               Thread.sleep(100);
               clock ++;
            }
         } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      
   }
   
   class printChatTread implements Runnable {

      @Override
      public void run() {
         // TODO Auto-generated method stub
         while(true) {
            if(clock/10 == 0) {
               switch(userNum) {
               case 0:
                  setBounds(5+x+270, 35+y+85, 5+tf.getText().length()*20, 50);
                  break;
               case 1:
                  setBounds(5+x+270, 35+y+290, 5+tf.getText().length()*20, 50);
                  break;
               case 2:
                  setBounds(5+x+270, 35+y+495, 5+tf.getText().length()*20, 50);
                  break;
               case 3:
                  setBounds(5+x+1175-(5+tf.getText().length()*20), 35+y+85, 5+tf.getText().length()*20, 50);
                  break;
               case 4:
                  setBounds(5+x+1175-(5+tf.getText().length()*20), 35+y+290, 5+tf.getText().length()*20, 50);
                  break;
               case 5:
                  setBounds(5+x+1175-(5+tf.getText().length()*20), 35+y+495, 5+tf.getText().length()*20, 50);
                  break;
               }
               setVisible(true);
            }
            else if(clock/10 > 5) {
               setVisible(false);
            }
            try {
               Thread.sleep(100);
            } catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }
   }
}