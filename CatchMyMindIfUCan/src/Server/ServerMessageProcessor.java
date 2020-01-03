package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ServerMessageProcessor {
	
	private String dataStream;
	private String head; 
	private String body; 
	private String tail;
	
	private String filePath;
	
	public ServerMessageProcessor() {
		filePath = ServerMessageProcessor.class.getResource("").getPath();
		dataStream = "";
		head = "{ \"USER_DATA\" : [";
		tail = "]}";
		getFileData();
	}
	
	public String getDataStream() {
		return dataStream;
	}

	public void setDataStream(String dataStream) {
		this.dataStream = dataStream;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTail() {
		return tail;
	}

	public void setTail(String tail) {
		this.tail = tail;
	}

	//파일 내용을 body에 집어넣기 + dataStream 완성하기
	public void getFileData() {
		File file = new File(filePath + "..\\Resource\\ServerResource\\userData.json");
		
		try {
			if(isOkFile(file)) {
				BufferedReader br = new BufferedReader(
						new FileReader(file));
				
				String str = "";
				String tmp = br.readLine();
				while(tmp != null) {
					str += tmp;
					tmp = br.readLine();
				}
				
				body = str;
				dataStream = head + body + tail;
				br.close();
			}else {
				System.out.println("can't approach file");
			}
		}catch(FileNotFoundException e) {
			System.out.println("ServerMessageProcessor - file not found");
		}catch(IOException e) {
			System.out.println("ServerMessageProcessor - IOException");
		}
	}
	
	//파일로 저장하기
	public void setFileData() {
		File file = new File(filePath + "..\\..\\src\\Resource\\ServerResource\\userData.json");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(body);
			bw.close();
		}catch(IOException e) {
			System.out.println("ServerMessageProcessor - IOException");
		}
	}
	
	public boolean isOkFile(File file) {
		if(file.exists()) {
			if(file.isFile() && file.canRead()) {
				return true;
			}
		}
		return false;
	}
	
	//데이터 만들기
	public String getJSONData(String key, String value) {
		String res = "";
		res += "\"" + key +"\"" + ":";
		res += "\"" + value +"\"";
		return res;
	}
	
	//가장 중요한 것 - 받은 JSON Message Processing
	public String processingServerMessage(String data) {
		String res = "";
		//1. data json parsing
		try {
			String sendData = "{";
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject)jsonParser.parse(data);
			JSONParser originalParser = new JSONParser();
			JSONObject originalObj = (JSONObject)originalParser.parse(dataStream);
			JSONArray originalArray = (JSONArray)originalObj.get("USER_DATA");
			
			//2. data의 method를 본다. - method별로 대응을 다르게 한다
			switch((String)jsonObj.get("method")) {
			case "1000": // 로그인 신청
				for(int i = 0; i < originalArray.size(); i++) {
					if(((JSONObject)originalArray.get(i)).get("id").equals(jsonObj.get("id"))) {
						if(((JSONObject)originalArray.get(i)).get("pwd").equals(jsonObj.get("pwd"))) {
							//1. id와 pwd 모두 일치 - login success
							sendData += getJSONData("method", "1002");
							sendData += "," + getJSONData("id", (String)((JSONObject)originalArray.get(i)).get("id"));
							sendData += "," + getJSONData("lv", (String)((JSONObject)originalArray.get(i)).get("lv"));
							sendData += "," + getJSONData("exp", (String)((JSONObject)originalArray.get(i)).get("exp"));
							sendData += "," + getJSONData("ch", (String)((JSONObject)originalArray.get(i)).get("ch"));
							sendData += "}";
							return sendData;
						}else {
						//2. pwd가 틀림. - login pwd fail
						sendData += getJSONData("method", "1014");
						sendData += "}";
						return sendData;
						}
					}
				}
				sendData += getJSONData("method", "1004");
				sendData += "}";
				return sendData;
			case "1100": //중복확인
				for(int i = 0; i < originalArray.size(); i++) {
					String dataStreamId = (String)((JSONObject)originalArray.get(i)).get("id");
					if(dataStreamId.equals(jsonObj.get("id"))){
						sendData += getJSONData("method", "1104");
						sendData += "}";
						System.out.println("method to : 1104 , sendData : " + sendData);
						return sendData;
					}
				}
				sendData += getJSONData("method", "1102");
				sendData += "}";
				System.out.println("method to : 1102 , sendData : " + sendData);
				return sendData;
			}
			
			//3. data를 전송할 형태로 만든다.
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}
	
}