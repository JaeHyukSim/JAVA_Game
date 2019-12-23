package Client;

import java.io.BufferedReader;

import UserForm.UserForm;

public class UserServerInputThread implements Runnable{
	BufferedReader inFromServer;
	String inputData;
	UserForm userForm;
}
