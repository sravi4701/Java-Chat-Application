import java.util.*;
import java.net.*;
import java.io.*;

public class Server {
	
	static int port;
	static int i = 0; // Client Counter
	static ArrayList<ClientHandler> cl = new ArrayList<>();
	public static void main(String[] args) throws IOException{
			try{
				port = Integer.parseInt(args[0]);
			}
			catch(NumberFormatException e){
				System.out.println("Please Enter Integer Value");
				System.exit(0);
			}
			catch(Exception e){
				System.out.println("Please give valid port no. in argument");
				System.exit(0);
			}

			ServerSocket ss = new ServerSocket(port);

			while(true){
				Socket s = null;
				try{
					System.out.println("Waiting for client");
					s = ss.accept();
					System.out.println("New Client Request Accepted" + s);
					DataInputStream dis = new DataInputStream(s.getInputStream());
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					System.out.println("Creating new Handler for this client");

					ClientHandler ch = new ClientHandler(s, "Client" + i, dis, dos);

					Thread t = new Thread(ch);

					System.out.println("Adding this client to active client list");

					cl.add(ch);

					t.start();

					i++;

				}
				catch(Exception e){
					s.close();
					e.printStackTrace();
				}
			}
	}
}

class ClientHandler implements Runnable {
	Scanner sc = new Scanner(System.in);
	private String name;
	final DataInputStream dis;
	final DataOutputStream dos;
	Socket s;
	boolean isLoggedIn;

	public ClientHandler(Socket s, String nm, DataInputStream dis, DataOutputStream dos){
		this.s = s;
		this.name = nm;
		this.dis = dis;
		this.dos = dos;
		this.isLoggedIn = true;
	}

	@Override
	public void run(){
		String received;
		while(true){
			try{
				received = dis.readUTF();
				System.out.println(received);
				if(received.equals("logout")){
					System.out.println("Disconnecting the Client");
					this.isLoggedIn = false;
					this.s.close();
					break;
				}
				String [] part = received.split("#");
				String messageToSend = part[0];
				String recipient = part[1];

				for(ClientHandler c : Server.cl){
					if(c.name.equals(recipient) && c.isLoggedIn == true){
						c.dos.writeUTF(this.name + " : " + messageToSend);
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		try{
			this.dos.close();
			this.dis.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}