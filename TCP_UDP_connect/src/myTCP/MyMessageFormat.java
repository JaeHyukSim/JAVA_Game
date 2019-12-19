package myTCP;

import java.util.ArrayList;

public class MyMessageFormat {
	
	private String id;
	private String name;
	private String method;
	private String from;
	private String to;
	private String body;
	private String message;
	
	public MyMessageFormat() {
		id = "none";
		name = "none";
		method = "none";
		from = "none";
		to = "none";
		body = "none";
		message = "none";
	}
	public String getMessage() {
		message = "";
		
		message += "{ \"method\" : \"" + method + "\" ,";
		message += " \"name\" : \"" + name + "\" ,";
		message += " \"from\" : \"" + from + "\" ,";
		message += " \"to\" : \"" + to + "\" ,";
		message += " \"body\" : \"" + body + "\" }";
		
		return message;
	}
	public String getMessageBody() {
		message = "";
		
		message += "{ \"method\" : \"" + method + "\" ,";
		message += " \"name\" : \"" + name + "\" ,";
		message += " \"from\" : \"" + from + "\" ,";
		message += " \"to\" : \"" + to + "\" ,";
		message += " \"body\" : "+ body +"}";
		
		return message;
	}
	public String getMessageArray(ArrayList<String> s) {
		int i;
		message = "";
		message += "[";
		for(i = 0; i < s.size() - 1; i++) {
			message += s.get(i);
			message += ",";
		}
		message += s.get(i);
		message += "]";
		
		return message;
	}
	public String getIdName() {
		String message = "";
		message += ("ID : " + id + "\tNAME : " + name);
		
		return message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
