import java.util.*;
import java.io.*;
import java.net.*;

public class Client {
	
	static int port;
	static String host;
	public static void main(String[] args) throws IOException{
			Scanner sc = new Scanner(System.in);
			try{
				System.out.println("Enter Host Name : ");
				host = sc.nextLine();
				System.out.println("Enter Port No. : ");
				port = sc.nextInt();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			InetAddress ip = InetAddress.getByName("localhost");
			Socket s = new Socket(ip, port);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());

			Thread sendMessage = new Thread(new Runnable() {
				@Override
				public void run(){
					while(true){
						try{
							String message = sc.nextLine();
							dos.writeUTF(message);
						}	
						catch(IOException e){
							e.printStackTrace();
						}
					}
				}
			});

			Thread receiveMessage = new Thread(new Runnable(){
				@Override
				public void run(){
					while(true){
						try{	
							String received = dis.readUTF();
							System.out.println(received);
						}	
						catch(IOException e){
							e.printStackTrace();
						}
					}
				}
			});
			
			sendMessage.start();
			receiveMessage.start();
	}
}