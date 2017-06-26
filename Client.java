import java.util.*;
import java.io.*;
import java.net.*;

public class Client {
	
	static int port;
	static String host;
	public static void main(String[] args) {
			Scanner sc = new Scanner(System.in);
			try{
				host = sc.next();
				port = sc.nextInt();
			}
			catch(Exception e){
				e.printStackTrace();
			}

			Socket s = new Socket(host, port);
	}
}