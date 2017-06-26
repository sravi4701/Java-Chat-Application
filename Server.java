public class Server {
	
	static int port;
	static int i = 0; // Client Counter

	public static void main(String[] args) {
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

			System.out.println(port);
	}
}