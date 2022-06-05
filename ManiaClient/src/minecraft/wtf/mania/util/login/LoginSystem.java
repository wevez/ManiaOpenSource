package wtf.mania.util.login;

import java.io.IOException;
import java.net.Socket;

import wtf.mania.Mania;

public class LoginSystem {
	
	public static Socket authConnection;
	public static boolean connect;
	
	public LoginSystem() {
		try {
			authConnection = new Socket("127.0.0.1", 80);
			connect = true;
		} catch (IOException e) {
			connect = false;
//			System.exit(-1);
		}
	}
	
	public boolean canLogin(String name, String password) {
		if(name.equals("wevez") && password.equals("mania")) {
			Mania.user = name;
			return true;
		}
		return false;
	}

}
