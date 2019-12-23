package myTCP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class TCP_Client_Input implements Runnable{
	private BufferedReader inFromUser;
	private DataOutputStream outToServer;
	private String inputData;
	private MyMessageFormat mmsg;
	
	public TCP_Client_Input(BufferedReader bf, DataOutputStream DOStream, MyMessageFormat m) {
		this.inFromUser = bf;
		this.outToServer = DOStream;
		this.mmsg = m;
	}
	
	//�������� ������ �ް�, ������ �ѱ��
	@Override
	public void run() {
		try {
			while(true) {
				System.out.println(">>>>(���� : 'H')<<<<");
				inputData = inFromUser.readLine();
				//�޽��� ������ �����.
				if(inputData.equals("h") || inputData.equals("H")) {
					mmsg.setMethod("help");
				}else if(inputData.equals("To")) {
					mmsg.setMethod("header");
				}else {
					mmsg.setMethod("broadcast");
					mmsg.setBody(inputData);
				}
				inputData = mmsg.getMessage() + "\n";
				//outToServer.writeBytes(inputData + '\n');
				outToServer.write(inputData.getBytes("EUC_KR"));
			}
		}catch(IOException e) {
			System.out.println("input end");
		}
	}
	
}

class TCP_Client_Output implements Runnable{
	private String outputData;
	private BufferedReader inFromServer;
	private MyMessageFormat mmsg;
	
	public TCP_Client_Output(BufferedReader bf, MyMessageFormat m) {
		this.inFromServer = bf; this.mmsg = m;
	}
	
	@Override
	public void run() {
		try {
			JSONParser jsonParser = new JSONParser();
			while(true) {
				outputData = inFromServer.readLine();
				JSONObject jsonObj = (JSONObject)jsonParser.parse(outputData);
				
				
				if(jsonObj.get("method").equals("help")) {
					System.out.println(jsonObj.get("body"));
				} else if(jsonObj.get("method").equals("broadcast")) {
					System.out.println(jsonObj.get("body"));
				} else if(jsonObj.get("method").equals("header")) {
					JSONArray jsonArray = (JSONArray)jsonObj.get("body");
					for(int i = 0; i < jsonArray.size(); i++) {
						JSONObject jsonTmpObj = (JSONObject)jsonArray.get(i);
						mmsg.setName((String)jsonTmpObj.get("name"));
						mmsg.setId((String)jsonTmpObj.get("from"));
						System.out.println(mmsg.getIdName());
					}
					
				}
			}
		}catch(IOException | ParseException e) {
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
		
		MyMessageFormat mmsg = new MyMessageFormat();
		String name;
		String method;
		String message;
		
		try {
			//�����κ��� �����͸� �Է¹޴´�.
			
			JSONParser jsonParser = new JSONParser();
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			
			
			socket = new Socket("localhost", 16789);
			System.out.println("������ ���� �Ǿ����ϴ�...");
			
			//������ ������ ���۸� �����.
			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
			//�����κ��� �Է¹޴� ���۸� �����.
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream(),"EUC_KR"));
			
			System.out.println("����� �г����� �Է��ϼ���...");
			System.out.print(" >");
			//id message�� ������
			name = inFromUser.readLine();
			method = "id";
			mmsg.setName(name);
			mmsg.setMethod(method);
			message = mmsg.getMessage();
			outToServer.writeBytes(message + '\n');
			
			input = inFromServer.readLine();
			JSONObject jsonObj = (JSONObject)jsonParser.parse(input);
			System.out.println(jsonObj.get("name") + "(" + jsonObj.get("from") + ")�� ������ �����Ǿ����ϴ�.");
			//System.out.println(inFromServer.readLine() + "��(ID) ������ �����Ǿ����ϴ�.");
			
			Runnable inputRun = new TCP_Client_Input(inFromUser, outToServer, mmsg);
			Thread inputThread = new Thread(inputRun);
			inputThread.start();
			Runnable outputRun = new TCP_Client_Output(inFromServer, mmsg);
			Thread outputThread = new Thread(outputRun);
			outputThread.start();
		}catch(IOException e) {
			System.out.println("client closed");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
