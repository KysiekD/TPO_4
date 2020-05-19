package zad1;

import zad1.Models.Category;
import zad1.Models.ClientModel;
import zad1.Models.NewsModel;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		String serverHostName = "localhost";
		int serverPortNumber = 44500;

		String clientHostName = "localhost";
		int clientPortNumber = 45666;
		String clientName = "Boris";

		ServerMain Server = new ServerMain(serverHostName, serverPortNumber);
		try {
			Category ScienceCategory = new Category("Science");
			ScienceCategory.addNewsToNewsList("Mammoths are still alive!");

			Category WeatherCategory = new Category("Weather");
			WeatherCategory.addNewsToNewsList("Terrible hurricate invades Poertaventurra!");
			WeatherCategory.addNewsToNewsList("Climate changes faster than we thought.");
			WeatherCategory.addNewsToNewsList("Why snow is white?");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ClientModel Client = new ClientModel(clientHostName, clientPortNumber, clientName);
		

		 
		 

		Client.connectToServer(serverHostName, serverPortNumber);
		 //Test addClientToSubscrivesList:
		

		Client.suscribeCategory("Weather");
		//Thread.sleep(2000);
		System.out.println("Clients subscribed to weather: " + Category.getCategoryByName("Weather").getSubscribedClientsList());
		//Thread.sleep(2000);


		// Test viewAllCategories
		String categories = Client.viewAllCategories();
		
		Client.showNews();
		
		//Client.closeConnectionToServer();

		System.out.println("END OF PROGRAM");
	}

}
