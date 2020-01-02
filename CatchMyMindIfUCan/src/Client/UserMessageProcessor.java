package Client;

public class UserMessageProcessor {
	
	//데이터 만들기
	public String getJSONData(String key, String value) {
		String res = "";
		res += "\"" + key +"\"" + ":";
		res += "\"" + value +"\"";
		return res;
	}
	public void processingUserMessage(String data) {
		
	}
}
