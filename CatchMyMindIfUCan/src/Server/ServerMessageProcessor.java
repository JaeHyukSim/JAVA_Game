package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ServerMessageProcessor {
	
	private static volatile ServerMessageProcessor uniqueInstance;
	
	private Station station;
	
	private String dataStream;
	private String head; 
	private String body; 
	private String tail;
	private JSONObject userData;
	private JSONParser jsonParser;
	
	private RoomData roomData;
	
	private ArrayList<Observer> waitingUserList;
	private ArrayList<RoomData> roomUserList;
	
	private String filePath;
	
	private ServerMessageProcessor() {
		
		jsonParser = new JSONParser();
		filePath = ServerMessageProcessor.class.getResource("").getPath();
		dataStream = "";
		head = "{ \"USER_DATA\" : [";
		tail = "]}";
		getFileData();
	}
	public static ServerMessageProcessor getInstMessageProcessor() {
		if(uniqueInstance == null) {
			synchronized (ServerMessageProcessor.class) {
				if(uniqueInstance == null) {
					uniqueInstance = new ServerMessageProcessor();
				}
			}
		}
		return uniqueInstance;
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

	//put to variable 'userData' from File
	public void getFileData() {
		File file = new File(filePath + "..\\Resource\\ServerResource\\userData.json");
		
		try {
			if(isOkFile(file)) {
				BufferedReader br = new BufferedReader(
						new FileReader(file));
				
				String str = "";
				String tmp = br.readLine();
				while(tmp != null) {
					str += tmp + '\n';
					tmp = br.readLine();
				}
				
				dataStream = head + body + tail;
				try {
					userData = (JSONObject)jsonParser.parse(str);
				} catch (ParseException e) {
					System.out.println("ServerMessageProcessor - JSON error : file reading");
					e.printStackTrace();
				}
				br.close();
			}else {
				System.out.println("can't approach file");
			}
		}catch(FileNotFoundException e) {
			System.out.println("ServerMessageProcessor - file not found");
		}catch(IOException e) {
			System.out.println("ServerMessageProcessor - IOException");
		}finally {
		}
	}
	
	//Save to file
	public void setFileData() {
		File file = new File(filePath + "..\\..\\src\\Resource\\ServerResource\\userData.json");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(userData.toString());
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
	
	//make json data
	public String getJSONData(String key, String value) {
		String res = "";
		res += "\"" + key +"\"" + ":";
		res += "\"" + value +"\"";
		return res;
	}
	
	//very important factor : JSON Message Processing
	public String processingServerMessage(String data, ServerFromUser sfu) {
		String res = "";
		//1. data json parsing
		try {
			System.out.println("data : " + data);
			String sendData = "{";
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject)jsonParser.parse(data);
			JSONArray originalArray = (JSONArray)userData.get("USER_DATA");
			
			//2. look the method of data - cope with each method differently
			switch((String)jsonObj.get("method")) {
			case "1000": // login message
				for(int i = 0; i < originalArray.size(); i++) {
					if(((JSONObject)originalArray.get(i)).get("id").equals(jsonObj.get("id"))) {
						if(((JSONObject)originalArray.get(i)).get("pwd").equals(jsonObj.get("pwd"))) {
							
							//1. find id
							if(DoesIDExist(sfu, (String)jsonObj.get("id"))) {
								//2. already log in
								sendData += getJSONData("method", "1024");
								sendData += "}";
								System.out.println("method:1024" + ",sendData:" + sendData);
								sfu.getStation().unicastObserver(sendData, sfu);
								return sendData;
							}
							//3. setId
							sfu.setId((String)jsonObj.get("id"));
							//sfu.setLv((String)jsonObj.get("lv"));
							sfu.setState("waiting");
							
							//1. both id and password match - login success
							sendData += getJSONData("method", "1002");
							sendData += "," + getJSONData("id", (String)((JSONObject)originalArray.get(i)).get("id"));
							sendData += "," + getJSONData("lv", (String)((JSONObject)originalArray.get(i)).get("lv"));
							sendData += "," + getJSONData("exp", (String)((JSONObject)originalArray.get(i)).get("exp"));
							sendData += "," + getJSONData("ch", (String)((JSONObject)originalArray.get(i)).get("ch"));
							sendData += "}";
							
							sfu.setId((String)((JSONObject)originalArray.get(i)).get("id"));
							sfu.setLv((String)((JSONObject)originalArray.get(i)).get("lv"));
							System.out.println("method:1002" + ",sendData:" + sendData);
							sfu.getStation().unicastObserver(sendData, sfu);
							return sendData;
						}else {
						//2. password is wrong - login pwd fail
						sendData += getJSONData("method", "1014");
						sendData += "}";
						System.out.println("method:1014" + ",sendData:" + sendData);
						sfu.getStation().unicastObserver(sendData, sfu);
						return sendData;
						}
					}
				}
				sendData += getJSONData("method", "1004");
				sendData += "}";
				System.out.println("method:1004" + ",sendData:" + sendData);
				sfu.getStation().unicastObserver(sendData, sfu);
				return sendData;
			case "1100": //Check for duplicates
				for(int i = 0; i < originalArray.size(); i++) {
					String dataStreamId = (String)((JSONObject)originalArray.get(i)).get("id");
					if(dataStreamId.equals(jsonObj.get("id"))){
						sendData += getJSONData("method", "1104");
						sendData += "}";
						System.out.println("method to : 1104 , sendData : " + sendData);
						sfu.getStation().unicastObserver(sendData, sfu);
						return sendData;
					}
				}
				sendData += getJSONData("method", "1102");
				sendData += "}";
				System.out.println("method to : 1102 , sendData : " + sendData);
				sfu.getStation().unicastObserver(sendData, sfu);
				return sendData;
			case "1200": // ID creation request
				for(int i = 0; i < originalArray.size(); i++) {
					String dataStreamId = (String)((JSONObject)originalArray.get(i)).get("id");
					if(dataStreamId.equals(jsonObj.get("id"))){
						sendData += getJSONData("method", "1204");
						sendData += "}";
						System.out.println("method to : 1204 , sendData : " + sendData);
						sfu.getStation().unicastObserver(sendData, sfu);
						return sendData;
					}
				}
				body = "";
				body += "{";
				body += getJSONData("id", (String)jsonObj.get("id"));
				body += "," + getJSONData("pwd",(String)jsonObj.get("pwd"));
				body += "," + getJSONData("lv", "1");
				body += "," + getJSONData("exp", "0");
				body += "," + getJSONData("ch", "1");
				body += "," + getJSONData("friendList", "");
				body += "}";
				dataStream = head + body + tail;
				System.out.println("body : " + body);
				JSONObject addedJSON = (JSONObject)jsonParser.parse(body);
				originalArray.add(addedJSON);
				setFileData();
				sendData += getJSONData("method", "1202");
				sendData += "}";
				System.out.println("method to : 1202, sendData ok");
				sfu.getStation().unicastObserver(sendData, sfu);
				return sendData;
			case "2000":
				//들어오니까 1. 대기실 유저 목록에 이 유저를 추가한다.
				
				//station.registerWaitingUser(sfu);
				sfu.setWaitingList();
				
				
				//1. 클라이언트한테 메시지를 보내야 한다 - 메시지 안에는 1. 대기실 유저 정보 list가 포함된다
				//2. 방 정보도 보내야돼 - 비어있더라도 보내야대 
				//3. 메소드는 2002 보내면 된다! method, watingUserList[], roomUserList[]
				waitingUserList = sfu.getWaitingList();
				sendData += getJSONData("method","2002");
				sendData += "," + "\"waitingUserList\":[";
				
				for(int i = 0; i < waitingUserList.size(); i++) {
					sendData += "{";
					sendData += getJSONData("id",waitingUserList.get(i).getId() );
					sendData += "," + getJSONData("lv",waitingUserList.get(i).getLv() );
					sendData += "," + getJSONData("state",waitingUserList.get(i).getState() );
					sendData += "}";
					if(i != waitingUserList.size()-1) {
						sendData += ",";
					}
				}
				//방정보 : 1. 방 제목, 2. 방 id. 3. 비밀번호 여부, 4. 비밀번호, 5. 총 인원, 6. 현재 인원, 7. 게임 상태
				roomUserList = sfu.getRoomList();
				sendData += "],\"roomUserList\":[";
				for(int i = 0; i < roomUserList.size() ; i++) {
					sendData += "{";
					sendData += getJSONData("roomId",roomUserList.get(i).getNumberOfRoom() );
					sendData += "," + getJSONData("roomName",roomUserList.get(i).getNameOfRoom() );
					sendData += "," + getJSONData("roomPassState",roomUserList.get(i).getRoomPassState() );
					sendData += "," + getJSONData("roomPass",roomUserList.get(i).getRoomPass() );
					sendData += "," + getJSONData("roomMaxUser",roomUserList.get(i).getCountOfMaximumUser() );
					sendData += "," + getJSONData("roomCurUser",roomUserList.get(i).getCountOfCurrentUser() );
					sendData += "," + getJSONData("roomState",roomUserList.get(i).getRoomState() );
					sendData += "}";
					if(i != waitingUserList.size()-1) {
						sendData += ",";
					}
				}
				sendData += "]}";
				System.out.println(sendData);
				sfu.getStation().broadcastWaitingObserber(sendData);
				return sendData;
			case "3000":
				System.out.println("get method : 3000");
				roomData = new RoomData();
				roomData.setCountOfCurrentUser("1");
				roomData.setCountOfMaximumUser("6");
				roomData.addUserList(sfu);
				sfu.getStation().registerRoomUserList(roomData);
				sfu.getStation().removeWaitingUser(sfu);
				//방 만들면, 
				
				sendData+= getJSONData("method", "3002");
				sendData += "}";
				System.out.println("send method : 3002");
				
				sfu.getStation().unicastObserver(sendData, sfu);
				
				sendData = "{";
				waitingUserList = sfu.getWaitingList();
				sendData += getJSONData("method","2002");
				sendData += "," + "\"waitingUserList\":[";
				
				for(int i = 0; i < waitingUserList.size(); i++) {
					sendData += "{";
					sendData += getJSONData("id",waitingUserList.get(i).getId() );
					sendData += "," + getJSONData("lv",waitingUserList.get(i).getLv() );
					sendData += "," + getJSONData("state",waitingUserList.get(i).getState() );
					sendData += "}";
					if(i != waitingUserList.size()-1) {
						sendData += ",";
					}
				}
				//방정보 : 1. 방 제목, 2. 방 id. 3. 비밀번호 여부, 4. 비밀번호, 5. 총 인원, 6. 현재 인원, 7. 게임 상태
				roomUserList = sfu.getRoomList();
				sendData += "],\"roomUserList\":[";
				for(int i = 0; i < roomUserList.size() ; i++) {
					sendData += "{";
					sendData += getJSONData("roomId",roomUserList.get(i).getNumberOfRoom() );
					sendData += "," + getJSONData("roomName",roomUserList.get(i).getNameOfRoom() );
					sendData += "," + getJSONData("roomPassState",roomUserList.get(i).getRoomPassState() );
					sendData += "," + getJSONData("roomPass",roomUserList.get(i).getRoomPass() );
					sendData += "," + getJSONData("roomMaxUser",roomUserList.get(i).getCountOfMaximumUser() );
					sendData += "," + getJSONData("roomCurUser",roomUserList.get(i).getCountOfCurrentUser() );
					sendData += "," + getJSONData("roomState",roomUserList.get(i).getRoomState() );
					sendData += "}";
					if(i != waitingUserList.size()-1) {
						sendData += ",";
					}
				}
				sendData += "]}";
				System.out.println(sendData);
				sfu.getStation().broadcastWaitingObserber(sendData);
				return sendData;
			case "2010":
				System.out.println("get method : 2010");
				sendData += getJSONData("method", "2012");
				sendData += "," + getJSONData("chat", sfu.getId() + " -> " + (String)jsonObj.get("chat"));
				sendData += "}";
				sfu.getStation().broadcastWaitingObserber(sendData);
				return sendData;
			}
			
			//3. Make data in the form of transmission
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}
	public boolean DoesIDExist(ServerFromUser sfu, String id) {
		ArrayList<Observer> userList = sfu.getUserList();
		
		System.out.println("userList size : " + userList.size());
		
		for(int i = 0; i < userList.size(); i++) {
			
			System.out.println("userList inside");
			
			if(userList.get(i).getId().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
}