package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import Message.JSONMessage;
import Message.MessageForm;

public class UserInputThread implements Runnable{
	private BufferedReader inFromUser;
	private OutputStream outToServer;
	private String inputData;
	private MessageForm messageForm;
	
	public UserInputThread(Socket socket) {
		try {
			//���� �ΰ��� �Ҵ��Ѵ�
			this.inFromUser = new BufferedReader(new InputStreamReader(System.in));
			this.outToServer = new DataOutputStream(socket.getOutputStream());
			this.messageForm = new JSONMessage();
		} catch (UnsupportedEncodingException e) {
			System.out.println("inFromUser buffer UnsupportedEncodingException error");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("inFromUser IOException error");
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		try {
			while(true) {
				//����ڷκ��� �Է��� �޴´�
				System.out.println(">");
				inputData = inFromUser.readLine();
				
				//�޽����� �����
				inputData += "\n";
				System.out.println("inputdata : " + inputData);
				outToServer.write(inputData.getBytes("EUC_KR"));
			}
		}catch(IOException e) {
			System.out.println("UserInputThread IOException error");
		}
	}
}
