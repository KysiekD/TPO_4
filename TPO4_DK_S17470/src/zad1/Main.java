package zad1;

import zad1.Models.ClientModel;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String serverHostName = "localhost";
		int serverPortNumber = 44500;
		
		String clientHostName = "localhost";
		int clientPortNumber = 45666;
		
		ServerMain server = new ServerMain(serverHostName, serverPortNumber);
		
		ClientModel client = new ClientModel(clientHostName, clientPortNumber);
		
		client.connectToServer(serverHostName, serverPortNumber);
		client.writeMsg("1-blabla");
		//System.out.println("TEST");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String text = client.readMsg();
		System.out.println("Client: " + text);
		client.closeConnectionToServer();
		System.out.println("END OF PROGRAM");
	}

}
