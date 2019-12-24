package Client;

import UserForm.LoginForm;
import UserForm.UserForm;

public class DisplayThread implements Runnable{
	private UserForm userForm;
	
	public DisplayThread() {
		userForm = new LoginForm();
	}
	public void display() {
		
	}
	
	@Override
	public void run() {
		System.out.println("display!");
	}
}
