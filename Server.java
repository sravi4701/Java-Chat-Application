import java.util.*;
import java.net.*;
import java.io.*;

public class Server {
	
	static int port;
	static int i = 0; // Client Counter

	//List of all the active clients
	static ArrayList<ClientHandler> cl = new ArrayList<>();

	public static void main(String[] args) throws IOException{

			// Get the port from argument
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

			// Create a ServerSocket
			ServerSocket ss = new ServerSocket(port);

			//While true Accepts all the clients
			while(true){
				Socket s = null;
				try{
					System.out.println("Waiting for client");
					s = ss.accept();
					System.out.println("New Client Request Accepted" + s);

					// Get the object of input and out data stream(this will store all the messages send and received)
					DataInputStream dis = new DataInputStream(s.getInputStream());
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					System.out.println("Creating new Handler for this client");


					// A separate Client handler for each client
					ClientHandler ch = new ClientHandler(s, "Client" + i, dis, dos);

					// Thread to handle each client
					Thread t = new Thread(ch);

					System.out.println("Adding this client to active client list");

					// Add to the client list
					cl.add(ch);

					//start the thread
					t.start();

					//Increment the Client Counter
					i++;

				}
				catch(Exception e){
					s.close();
					e.printStackTrace();

				}
			}
	}
}

// A ClientHandler Class to handle the client
class ClientHandler implements Runnable {
	Scanner sc = new Scanner(System.in);
	private String name;
	final DataInputStream dis;
	final DataOutputStream dos;
	Socket s;
	boolean isLoggedIn;

	// Constructor to initialize the socket, name, dis and dos
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
				// message format should  : message#Recipient ex. hello#Client1
				String [] part = received.split("#");
				String messageToSend = "";
				String recipient = "";
				try{
					messageToSend = part[0];
					recipient = part[1];
				} 
				catch(Exception e){

				}

				// Search whose message
				for(ClientHandler c : Server.cl){
					if(c.name.equals(recipient) && c.isLoggedIn == true){
						c.dos.writeUTF(this.name + " : " + messageToSend);
					}
				}
			}
			catch(Exception e){
				if(true){
					break;
				}
				e.printStackTrace();
			}
		}
		try{
			// closing dis and dos object
			this.dos.close();
			this.dis.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}