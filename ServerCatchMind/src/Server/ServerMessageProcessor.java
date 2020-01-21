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
	
	private static int tmp = 1;
	
	private static volatile ServerMessageProcessor uniqueInstance;
	
	private Station station;
	
	private String head; 
	private String body; 
	private String tail;
	private JSONObject userData;
	private JSONParser jsonParser;
	
	private RoomData roomData;
	
	private ArrayList<Observer> waitingUserList;
	private ArrayList<RoomData> roomUserList;
	
	private String filePath;
	
	//Room total info
	private final int ROOMSIZE;
	
	/*graphic algorithm*/
	int[][] slidingWindow;
	int slidingWindowPointer;
	
	private JSONObject json;
	
	private ServerMessageProcessor() {
		
		json = new JSONObject();
		slidingWindow = new int[2][2];
		
		ROOMSIZE = 30;
		
		jsonParser = new JSONParser();
		filePath = ServerMessageProcessor.class.getResource("").getPath();
		System.out.println("FILEPATH : " + filePath);
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
		File file = new File(filePath + "..\\..\\src\\Resource\\ServerResource\\userData.json");
		
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
		
		// 필요한것
		// 1. 유저가 속한 방을 알고 싶을 때	-> sfu.getStation().findRoomObserver_RoomData(sfu.getRoomId());
		// 2. *** RoomData rd = sfu.getStation().findRoomObserver_RoomData(sfu.getRoomId());
		// 3. 모든 사람이 레디했는지 알고 싶을 때 : rd.isAllReady() -> boolean을 return
		int index; RoomData rd;
		
		
		String res = "";
		//1. data json parsing
		try {
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
				body += "," + getJSONData("ch", String.valueOf(jsonObj.get("ch")));
				body += "," + getJSONData("friendList", "[]");
				body += "}";
				System.out.println("body : " + body);
				JSONObject addedJSON = (JSONObject)jsonParser.parse(body);
				originalArray.add(addedJSON);
				setFileData();
				sendData += getJSONData("method", "1202");
				sendData += "}";
				System.out.println("method to : 1202, sendData ok");
				sfu.getStation().unicastObserver(sendData, sfu);
				return sendData;
			case "1400": //logout request
				sendData = "{";
				sendData += getJSONData("method", "1402");
				sendData += "}";
				sfu.setId("#");
				sfu.getStation().unicastObserver(sendData, sfu);
				return sendData;
			case "1500": // withdrawal
				json.clear();
				json.put("method", "1502");
				sendData = String.valueOf(json);
				sfu.getStation().broadcastObserver(sendData);
				removeMember(sfu);
				
				return sendData;
			case "2000":
				//들어오니까 1. 대기실 유저 목록에 이 유저를 추가한다.
				 
				sfu.setWaitingList();
				
				
				//1. 클라이언트한테 메시지를 보내야 한다 - 메시지 안에는 1. 대기실 유저 정보 list가 포함된다
				//2. 방 정보도 보내야돼 - 비어있더라도 보내야대 
				//3. 메소드는 2002 보내면 된다! method, watingUserList[], roomUserList[]
				
				
				sendData = "{";
				sendData += getJSONData("method", "2002");
				sendData += "}";
				
				sfu.getStation().unicastObserver(sendData, sfu);
				
				//get other user's data
				sendData = getAllListData(sfu);
				//sfu.getStation().unicastObserver(sendData, sfu);
				
				//and spread my data
				//sfu.setWaitingList();
				//sendData = getOneListDataForWait(sfu);
				sfu.getStation().broadcastObserver(sendData);
				
				return sendData;
			case "2100":
				System.out.println("get method : 2100");
				sendData += getJSONData("method", "2102");
				sendData += "," + getJSONData("chat", sfu.getId() + " -> " + (String)jsonObj.get("chat"));
				sendData += "}";
				sfu.getStation().broadcastWaitObserver(sendData);
				return sendData;
			case "2070": // remove - find index
				sendData = getAllListData(sfu);
				sfu.getStation().broadcastObserver(sendData);
				return sendData;
			case "2400":	//out from waiting room
				sfu.getStation().removeWaitObserver(sfu); // remove user from waiting room
				sendData = getAllListData(sfu); // update all user
				sfu.getStation().broadcastObserver(sendData);
				
				//next change the card show to login form and init login form
				//first. 1302 -> init login form
				sendData = "{";
				sendData += getJSONData("method", "1302");
				sendData += "}";
				sfu.getStation().unicastObserver(sendData, sfu);
				//second. 1312 -> card show
				sendData = "{";
				sendData += getJSONData("method", "1312");
				sendData += "}";
				sfu.getStation().unicastObserver(sendData, sfu);
				return sendData;
			case "3000": // create room
				System.out.println("get method : 3000");
				
				
				//full room
				if(sfu.getStation().getRoomUserList().size() >= ROOMSIZE) {
					sendData += getJSONData("method", "2224");
					sendData += "}";
					System.out.println("room is full - method to 2224");
					sfu.getStation().unicastObserver(sendData, sfu);
					return sendData;
				}
				
				//create room
				roomData = new RoomData();
				roomData.setNameOfRoom((String)jsonObj.get("roomCreate"));
				roomData.setRoomPass((String)jsonObj.get("roomPwd"));
				roomData.setIdOfMasterUser(sfu.getId());
				if(roomData.getRoomPass().equals("")) {
					roomData.setRoomPassState("0");
				}else {
					roomData.setRoomPassState("1");
				}
				roomData.setCountOfCurrentUser("0");
				roomData.setCountOfMaximumUser("6");
				roomData.addUserList(sfu);
				
				//make room ID
				for(int i = 1; i <= ROOMSIZE; i++) {
					index = sfu.getStation().findRoomObserver(String.valueOf(i));
					if(index == -1) {
						roomData.setNumberOfRoom(String.valueOf(i));
						sfu.setRoomId(String.valueOf(i));
						System.out.println("room number : " + i);
						break;
					}
				}
				
				
				sfu.getStation().registerRoomObserver(roomData);
				sfu.getStation().removeWaitObserver(sfu);
				//방 만들면, 
				//1. dialog 제거
				sendData += getJSONData("method", "2052");
				sendData += "}";
				sfu.getStation().unicastObserver(sendData, sfu);
				
				sendData = "{";
				sendData+= getJSONData("method", "3002");
				sendData += "}";
				sfu.getStation().unicastObserver(sendData, sfu);
				
				initSlidingWindow();
				
				//3. wait room data init - for other user
				sendData = getAllListData(sfu);
				sfu.getStation().broadcastWaitObserver(sendData);
				
				//4. room register!
				sendData = getAllGameData(sfu);
				sfu.getStation().broadcastRoomObserver(sendData, sfu.getRoomId());
				
				sfu.setReadyState("2");
				sendData = "{";
				sendData += getJSONData("method", "3MASTER");
				sendData += "}";
				sfu.getStation().unicastObserver(sendData, sfu);
				
				return sendData;
			case "3010": // get room enter!
				
				index = Integer.parseInt((String)jsonObj.get("room"));
				if(index == -1) {
					return sendData;
				}
				rd = sfu.getStation().getRoomUserList().get(index);
				System.out.println("room id : " + rd.getNumberOfRoom());
				if(Integer.parseInt(rd.getCountOfCurrentUser()) >=
						Integer.parseInt(rd.getCountOfMaximumUser())) {
					//room is full
					sendData = "{";
					sendData += getJSONData("method", "3014"); // reject enter to room
					sendData += "}";
					sfu.getStation().unicastObserver(sendData, sfu);
					return sendData;
				}
				//if passward state is existed ... 
				if(rd.getRoomPassState().equals("0") == false) {
					//비밀번호를 검사해야겠죠?
					if(!(rd.getRoomPass().equals((String)jsonObj.get("pass")))) // 입력한 비밀번호가 맞다면,
					{
						sendData = "{";
						sendData+= getJSONData("method", "2113");
						sendData += "}";
						sfu.getStation().unicastObserver(sendData, sfu);					
						System.out.println("wrongPwd: "+sendData);
						return sendData;
					}
					
				}
				// 1. set Visible off
				sendData = "{";
				sendData+= getJSONData("method", "2112");
				sendData += "}";
				sfu.getStation().unicastObserver(sendData, sfu);
				System.out.println("equalPwd"+sendData);
				// 1. waiting room set
				sfu.getStation().removeWaitObserver(sfu);
				// 2. go into room
				sendData = "{";
				sendData += getJSONData("method", "3002");
				sendData += "}";
				sfu.getStation().unicastObserver(sendData, sfu);
				// 3. find room and register
				rd.addUserList(sfu);
				sfu.setRoomId(rd.getNumberOfRoom());
				
				// 4. init all thing
				sendData = getAllListData(sfu);
				sfu.getStation().broadcastWaitObserver(sendData);
				sendData = getAllGameData(sfu);
				sfu.getStation().broadcastRoomObserver(sendData, rd.getNumberOfRoom());
				
				return sendData;
			case "3090": // get room enter!
				int id = Integer.parseInt((String) jsonObj.get("room"));
				index = sfu.getStation().findRoomObserver(String.valueOf(id));
				
				if(index== -1)
				{
					// : 찾는 방이 없을때
					sendData = "{";
					sendData += getJSONData("method", "2870");
					sendData += "}";
					sfu.getStation().unicastObserver(sendData, sfu);
					return sendData;
					
				}
				
				rd = sfu.getStation().getRoomUserList().get(index);
				System.out.println("room id : " + rd.getNumberOfRoom());
				if (Integer.parseInt(rd.getCountOfCurrentUser()) >= Integer.parseInt(rd.getCountOfMaximumUser())) {
					// room is full
					sendData = "{";
					sendData += getJSONData("method", "3014"); // reject enter to room
					sendData += "}";
					sfu.getStation().unicastObserver(sendData, sfu);
					return sendData;
				}
				if (rd.getRoomPassState().equals("0") == false) {
					// room pass logic
				}
				// 1. waiting room set
				sfu.getStation().removeWaitObserver(sfu);
				// 2. go into room
				sendData = "{";
				sendData += getJSONData("method", "3002");
				sendData += "}";
				sfu.getStation().unicastObserver(sendData, sfu);
				// 3. find room and register
				rd.addUserList(sfu);
				sfu.setRoomId(rd.getNumberOfRoom());

				// 4. init all thing
				sendData = getAllListData(sfu);
				sfu.getStation().broadcastWaitObserver(sendData);
				sendData = getAllGameData(sfu);
				sfu.getStation().broadcastRoomObserver(sendData, rd.getNumberOfRoom());

				return sendData;
			case "3400": // init room
				//해당 방은 sfu.getStation().getRoom
				ArrayList<RoomData> al = sfu.getStation().getRoomUserList();
				System.out.println("al size : " + al.size());
				for(int i = 0; i < al.size(); i++) {
					System.out.println("sfu.getRoomId() : " + sfu.getRoomId());
					System.out.println("al.get(i).getNumberOfRoom() : " + al.get(i).getNumberOfRoom());
					if(sfu.getRoomId().equals(al.get(i).getNumberOfRoom())) {
						//이 데이터를 전송해야 한다. -> RoomData를 가지고 있다 (유저목록만 가져오자)
						ArrayList<Observer> usr = al.get(i).getUserList();
						
						sendData += getJSONData("method", "3402");
						sendData += ",";
						sendData += "\"userList\":[";
						for(int j = 0; j < usr.size(); j++) {
							sendData += "{";
							sendData += getJSONData("id", usr.get(i).getId());
							sendData += "," + getJSONData("lv", usr.get(i).getLv());
							sendData += "," + getJSONData("ch", usr.get(i).getCh());
							sendData += "," + getJSONData("exp", usr.get(i).getExp());
							sendData += "}";
							if(j != usr.size()-1) {
								sendData += ",";
							}
						}
						sendData += "]}";
						break;
						
					}
				}
				sfu.getStation().unicastObserver(sendData, sfu);
				sendData += getJSONData("method", "3402 : data : " + sendData);
				return sendData;
			case "3600": // get out from room
				boolean isExistRoom = true;
				//1. request -> 2. remove from room(find room and remove usr) 
				//3. register to wait room 4. init user's wait room 5. update other usr
				
				index = sfu.getStation().findRoomObserver(sfu.getRoomId());
				rd = sfu.getStation().getRoomUserList().get(index);
				if(sfu.getStation().getRoomUserList().get(index).getCountOfCurrentUser().equals("1")) {
					isExistRoom = false;
				}
				sfu.getStation().registerWaitObserver(sfu); // register
				sfu.getStation().removeRoomObserverTarget(sfu.getRoomId(), sfu);
				
				//first. card change and init chat
				sendData = "{";
				sendData += getJSONData("method", "2002");
				sendData += "}";
				sfu.getStation().unicastObserver(sendData, sfu);
				
				
				//init waitroom for me -> chat clear
				//broadcast data to all (wait)
				sendData = getAllListData(sfu);
				sfu.getStation().broadcastWaitObserver(sendData);
				
				//broadcast data to all (room) 1. find room 2. broadcast
				if(isExistRoom == true) {
					sendData = getAllGameData(sfu);
					
					if(sendData == "NO") {
						System.out.println("ServerMessageProcessor - getAllGameData : NO");
						return sendData;
					}
					sfu.getStation().broadcastRoomObserver(sendData, sfu.getRoomId());
					
					if(rd.getIdOfMasterUser().equals(sfu.getId())) {
						rd.setIdOfMasterUser(rd.getUserList().get(0).getId());
						//sendData who is master?
						sendData = messageForMaster(rd);
						sfu.getStation().broadcastRoomObserver(sendData, sfu.getRoomId());
					}
				}
				sfu.setRoomId("0");
				sfu.setReadyState("0");
				int currReadyUser = Integer.parseInt(rd.getCountOfReadyUser());
				currReadyUser--;
				rd.setCountOfReadyUser(String.valueOf(currReadyUser));
				return sendData;
			case "3700": //DRAW - get draw coordinate - x, y
				//1. don't use graphic algorithm
				
				sendData = "{";
				sendData += getJSONData("method", "3702");
				sendData += "," + getJSONData("x", (String)jsonObj.get("x"));
				sendData += "," + getJSONData("y", (String)jsonObj.get("y"));
				sendData += "," + getJSONData("color", (String)jsonObj.get("color"));
				sendData += "}";
				sfu.getStation().broadcastRoomObserver(sendData, sfu.getRoomId());
				return sendData;
			case "3710": // DRAW - clear all
				initSlidingWindow();
				sendData = "{";
				sendData += getJSONData("method", "3712");
				sendData += "," + getJSONData("color", (String)jsonObj.get("color"));
				sendData += "}";
				sfu.getStation().broadcastRoomObserver(sendData, sfu.getRoomId());
				return sendData; 
			case "3720":	//init sliding window
				sendData = "{";
				sendData += getJSONData("method", "3722");
				sendData += "}";
				sfu.getStation().broadcastRoomObserver(sendData, sfu.getRoomId());
				return sendData;
			case "3309":	// game room chat
				rd = sfu.getStation().getRoomUserList().get(sfu.getStation().findRoomObserver(sfu.getRoomId()));
				index = -1;
				for(int i = 0; i < rd.getUserList().size(); i++) {
					if(rd.getUserList().get(i).equals(sfu)) {
						index = i;
					}
				}
				if(index == -1) {
					return sendData;
				}
				sendData = "{";
				sendData += getJSONData("method", "3300");
				sendData += "," + getJSONData("message", String.valueOf(jsonObj.get("message")));
				sendData += "," + getJSONData("userNum", String.valueOf(index));
				sendData += "}";
				
				sfu.getStation().broadcastRoomObserver(sendData, sfu.getRoomId());
				
				return sendData;
			case "READY_CLICK":
				rd = sfu.getStation().getRoomUserList().get(sfu.getStation().findRoomObserver(sfu.getRoomId()));
				index = -1;
				for(int i = 0; i < rd.getUserList().size(); i++) {
					if(rd.getUserList().get(i).equals(sfu)) {
						index = i;
					}
				}
				if(index == -1) {
					return sendData;
				}
				if(sfu.getReadyState().equals("0")) {
					sfu.setReadyState("1");
					currReadyUser = Integer.parseInt(rd.getCountOfReadyUser());
					currReadyUser++;
					rd.setCountOfReadyUser(String.valueOf(currReadyUser));
					sendData = "{";
					sendData += getJSONData("method", "3READY_CLICKED");
					sendData += "," + getJSONData("userNum", String.valueOf(index));
					sendData += "," + getJSONData("readyState", "1");
					sendData += "}";
					sfu.getStation().broadcastRoomObserver(sendData, sfu.getRoomId());
					System.out.println(sendData);
					return sendData;
				}
				else if(sfu.getReadyState().equals("1")) {
					sfu.setReadyState("0");
					currReadyUser = Integer.parseInt(rd.getCountOfReadyUser());
					currReadyUser--;
					rd.setCountOfReadyUser(String.valueOf(currReadyUser));
					sendData = "{";
					sendData += getJSONData("method", "3READY_CLICKED");
					sendData += "," + getJSONData("userNum", String.valueOf(index));
					sendData += "," + getJSONData("readyState", "0");
					sendData += "}";
					sfu.getStation().broadcastRoomObserver(sendData, sfu.getRoomId());
					System.out.println(sendData);
					return sendData;
				}
				else if(sfu.getReadyState().equals("2")) {
					if(rd.isAllReady()) {
						rd = sfu.getStation().getRoomUserList().get(sfu.getStation().findRoomObserver(sfu.getRoomId()));
						for(int i = 0; i < rd.getUserList().size(); i++) {
							rd.getUserList().get(i).setReadyState("3");
						}
						sendData = "{";
						sendData += getJSONData("method", "3GAME_START");
						sendData += "}";
						sfu.getStation().broadcastRoomObserver(sendData, sfu.getRoomId());
						System.out.println("game start : "+sendData);
						return sendData;
					}
					return sendData;
				}
				return sendData;
case "3910": // invite Gameroom!
				
				// Observer invitedUser;
				sendData = messageWaitroomList(sfu); // 대기실에 있는 대기자

				sfu.getStation().unicastObserver(sendData, sfu); // 데이터 , 서버한테 요청한 자신한테 데이터 보냄
				System.out.println("3910 sendData : " + sendData); 
/*
				
				for (int i = 0; i < originalArray.size(); i++) 
				{
					if (((JSONObject) originalArray.get(i)).get("id").equals(jsonObj.get("id"))) 
						{
							// 1. find id
							if (WaitRoomDoesIDExist(sfu, (String) jsonObj.get("id"))) 
							{
								// 클라이언트에게 보낼 데이터 - 메소드넘버, 초대자id, 초대받은 id,  초대자있는방id
								sendData = "{";
								sendData += getJSONData("method", "3930");
								sendData += "," + getJSONData("invitedid", user.getid());
								sendData += "," + getJSONData("id", sfu.getId()	);
								sendData += "," + getJSONData("room", sfu.getRoomId());
								sendData += "}";
								
							break;
								System.out.println("method:3930" + ",sendData:" + sendData);
								sfu.getStation().unicastObserver(sendData, sfu);
								return sendData;
							}
						}
				}
				*/
				
				
			//	ArrayList<Observer> user = sfu.getStation().getWaitUserList(); // 대기실에 있는 대기자명단

				
			//	getJSONData("inviteDialogData", String.valueOf(inviteDialogData));
/*
				for (int i = 0; i < user.size(); i++) { //
					if (inviteDialogData.equals(user.get(i).getId())) // 대기실에 일치하는 해당 아이디가 없는 경우
					{
						
						//user.get(i).getId().equals(inviteDialogData)
						invitedUser = user.get(i);
						//
						sendData = "{";
						sendData += getJSONData("method", "3930");
						sendData += getJSONData("room", sfu.getRoomId());
						sendData += "," + getJSONData("inviteid", sfu.getId());
						sendData += "}";
						System.out.println("3930 sendData : " +sendData);
						// sfu.getStation().unicastObserver(sendData, String.valueOf(json id));
						sfu.getStation().unicastObserver(sendData, invitedUser);
						// sfu.getId()
						return sendData;
					}
					
					*/
					return sendData;
			case "3960":
				// 초대메시지가 거절되었을경우 초대자에게 거절 확인 메시지 보내기위함!
				
				Observer man = null;
				index = sfu.getStation().findRoomObserver(String.valueOf(jsonObj.get("roomId")));
				if(index == -1) { // 방 자체가 없을 경우{
					return sendData;
				}
				rd = sfu.getStation().getRoomUserList().get(index); // 방이 있는 경우, 해당 방 자체를 rd에 저장하겠다.
				
				//////////////////////// index 새로 시작
				index = -1;
				for(int i = 0; i < rd.getUserList().size(); i++) {
					if(rd.getUserList().get(i).getId().equals(String.valueOf(jsonObj.get("id")))) {
						//찾은겁니다.
						index = i;
						
						man = rd.getUserList().get(index);
						//데이터를 보내겠습니다.man = rd.getUserList().get(index);
						sendData = "{";
						sendData += getJSONData("method", "3970");
						sendData += "," + getJSONData("id", String.valueOf(jsonObj.get("id")));
						sendData += "}";
						//클라이언트로 전달하면 됩니다
						System.out.println("3960 sendData : " + sendData);
						sfu.getStation().unicastObserver(sendData, man);
						break;
					}
				}
				//방에서 A를 찾죠 id로 -> 없으면 , 이 사람은 해당 방에서 나간겁니다.
				return sendData;
					
					
			case "3990":
				//1. 서버로 보내진 메시지 : 1. 웨이팅 룸의 한 id
				//2. 해야될 일 : 찾아야죠!? - waiting room과 비교  일치하면 보내고, 못찾으면 다시 메시지를 돌려보내는데 (3930)
				//3. 먼저, 저희 리스트를 구해야 for문을 돌리겠죠 - waitUserList : sfu.getStaion().getWait...
				String inviteId = String.valueOf(jsonObj.get("id"));
				index = -1;
				ArrayList<Observer> user = sfu.getStation().getWaitUserList();
				for(int i = 0; i < user.size(); i++) {
					if(inviteId.equals(user.get(i).getId())) {
						//찾은거죠
						index = i;
						break;
					}
				}
				//못찾으면 : index = -1;
				if(index == -1) {
					sendData = "{";
					sendData += getJSONData("method", "3930");
					sendData += "," + getJSONData("id", inviteId);
					sendData += "}";
					sfu.getStation().unicastObserver(sendData, sfu);
					return sendData;
				}
				//찾았을 경우 : -  B한테 메시지가 가야겠죠? 3940
				
				
				sendData = "{";
				sendData += getJSONData("method", "2940");
				sendData += "," + getJSONData("id", sfu.getId());
				sendData += "," + getJSONData("roomId", sfu.getRoomId());
				sendData += "}";
				sfu.getStation().unicastObserver(sendData, user.get(index));
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
	
	public int getRemoveUserDataForWait(ServerFromUser sfu) {
		//1. sfu의 index를 찾고, 제거
		ArrayList<Observer> wl = sfu.getStation().getWaitUserList();
		int index = -1;
		
		for(int i = 0; i < wl.size(); i++) {
			if(sfu.getId().equals(wl.get(i).getId())) {
				index = i;
			}
		}
		sfu.getStation().removeWaitObserver(sfu);
		return index;
	}
	public String getOneListDataForWait(ServerFromUser sfu) {
		
		int index = sfu.getStation().findRoomObserver(sfu.getId());
		RoomData rd = sfu.getStation().getRoomUserList().get(index);
		String sendData = "{";
		sendData += getJSONData("method","2072");
		
		sendData += "," + "\"waitingUserList\":";
		sendData += "{";
		sendData += getJSONData("id",sfu.getId() );
		sendData += "," + getJSONData("lv",sfu.getLv() );
		sendData += "," + getJSONData("state",sfu.getState() );
		sendData += "}";
		
		sendData += ",\"roomUserList\":";
		sendData += "{";
		sendData += getJSONData("roomId",rd.getNumberOfRoom() );
		sendData += "," + getJSONData("roomName",rd.getNameOfRoom() );
		sendData += "," + getJSONData("roomPassState",rd.getRoomPassState() );
		sendData += "," + getJSONData("roomPass",rd.getRoomPass() );
		sendData += "," + getJSONData("roomMaxUser",rd.getCountOfMaximumUser() );
		sendData += "," + getJSONData("roomCurUser",rd.getCountOfCurrentUser() );
		sendData += "," + getJSONData("roomState",rd.getRoomState() );
		sendData += "}}";
		return sendData;
	}
	
	//make message for all user
	public String getWaitMessage(ServerFromUser sfu) {
		String sendData = "{";
		sendData += getJSONData("method","2072");
		
		sendData += "," + "\"waitingUserList\":";
		sendData += "{";
		sendData += getJSONData("id",sfu.getId() );
		sendData += "," + getJSONData("lv",sfu.getLv() );
		sendData += "," + getJSONData("state",sfu.getState() );
		sendData += "}";
		
		return sendData;
	}
	//make message for one user
	public String getWaitListMessage(ServerFromUser sfu) {
		waitingUserList = sfu.getWaitingList();
		String sendData = "{";
		sendData += getJSONData("method","2062");
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
		return sendData;
	}
	public String getAllListData(ServerFromUser sfu) {
		waitingUserList = sfu.getWaitingList();
		String sendData = "{";
		sendData += getJSONData("method","2062");
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
		return sendData;
	}
	//3402 
	public String getAllGameData(ServerFromUser sfu) {
		String sendData = "{";
		int index = sfu.getStation().findRoomObserver(sfu.getRoomId());
		if(index == -1) {
			return "NO";
		}
		RoomData rd = sfu.getStation().getRoomUserList().get(index);
		ArrayList<Observer> usr = rd.getUserList();
		
		sendData += getJSONData("method", "3402");
		sendData += ",";
		sendData += "\"userList\":[";
		for(int i = 0; i < usr.size(); i++) {
			sendData += "{";
			sendData += getJSONData("id", usr.get(i).getId());
			sendData += "," + getJSONData("lv", usr.get(i).getLv());
			sendData += "," + getJSONData("ch", usr.get(i).getCh());
			sendData += "," + getJSONData("exp", usr.get(i).getExp());
			sendData += "}";
			if(i != usr.size()-1) {
				sendData += ",";
			}
		}
		sendData += "]}";
		return sendData;
	}
	
	//who is master?
	public String messageForMaster(RoomData rd) {
		String sendData = "{";
		sendData += getJSONData("method", "3902");
		sendData += "," + getJSONData("master", rd.getIdOfMasterUser());
		sendData += "}";
		return sendData;
		
	}
	public void initSlidingWindow() {
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				slidingWindow[i][j] = 100000;
			}
		}
		slidingWindowPointer = 0;
	}
	
	//remove member method
	public void removeMember(ServerFromUser sfu) {
		//1. remove from file
		JSONArray jsonArr = (JSONArray)(userData.get("USER_DATA"));
		for(int i = 0; i < jsonArr.size(); i++) {
			if(((JSONObject)jsonArr.get(i)).get("id").equals(sfu.getId())) {
				jsonArr.remove(i);	break;
			}
		}
		setFileData();
		
		//2. remove from userList
		sfu.setId("#");
	}
	// 메소드의 목적 -> 저희가 대기실에 있는 사람들을 메시지로 만드는 메소드
		public String messageWaitroomList(ServerFromUser sfu) {
			String sendData = "";

			ArrayList<Observer> user = sfu.getStation().getWaitUserList(); // 배열로 대기자 보내줌
			// 1. 제이슨 형태기 때문에 {
			sendData = "{"; // 시작

			// 2. 모든 유저 데이터를 불러와야 되는데,
			sendData += getJSONData("method", "3920"); // 서버에서 보내는 메소드 이름( 메소드키:키값
			sendData += ",";
			sendData += "\"waitList\" : ["; // 키 waitList

			for (int i = 0; i < user.size(); i++) { //

				sendData += "{";
				sendData += getJSONData("id", user.get(i).getId());
				sendData += "," + getJSONData("lv", user.get(i).getLv());
				sendData += "}";

				if (i != user.size() - 1) {
					sendData += ",";

				}

			}

			sendData += "]";
			sendData += "}"; // 끝!

			/*
			 * sendData = "{hello " + "skldjgsdjgsdlgj" + "sdlgsdlkgjsdlgj" + "" +
			 * "sdgsdlkgjsdlkgjsdklgjl";
			 */

			return sendData;
		}
}