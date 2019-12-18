package myTCP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

class TCP_Client_Input implements Runnable{
	private BufferedReader inFromUser;
	private DataOutputStream outToServer;
	private String inputData;
	
	public TCP_Client_Input(BufferedReader bf, DataOutputStream DOStream) {
		this.inFromUser = bf;
		this.outToServer = DOStream;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				inputData = inFromUser.readLine();
				outToServer.writeBytes(inputData + '\n');
			}
		}catch(IOException e) {
			System.out.println("input end");
		}
	}
	
}

class TCP_Client_Output implements Runnable{
	private String outputData;
	private BufferedReader inFromServer;
	
	public TCP_Client_Output(BufferedReader bf) {
		this.inFromServer = bf;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				outputData = inFromServer.readLine();
				System.out.println("FROM SERVER : " + outputData);
			}
		}catch(IOException e) {
			System.out.println("output end");
		}
	}
}
public class TCP_client{

	public static void main(String[] args) {
		
		Scanner scn = new Scanner(System.in);
		String input;
		String output;
		Socket socket;
		
		try {
			//�����κ��� �����͸� �Է¹޴´�.
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			
			
			socket = new Socket("localhost", 16789);
			System.out.println("������ ���� �Ǿ����ϴ�...");
			
			//������ ������ ���۸� �����.
			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
			//�����κ��� �Է¹޴� ���۸� �����.
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			Runnable inputRun = new TCP_Client_Input(inFromUser, outToServer);
			Thread inputThread = new Thread(inputRun);
			inputThread.start();
			Runnable outputRun = new TCP_Client_Output(inFromServer);
			Thread outputThread = new Thread(outputRun);
			outputThread.start();
		}catch(IOException e) {
			System.out.println("client closed");
		}

	}

}
