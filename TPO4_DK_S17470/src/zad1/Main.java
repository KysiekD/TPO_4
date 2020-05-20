package zad1;

import Views.ClientView;
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

		ServerMain server = new ServerMain(serverHostName, serverPortNumber);
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


		ClientModel client = new ClientModel(clientHostName, clientPortNumber, clientName);
		
		//client.connectToServer();
		//client.suscribeCategory("Weather");
		//client.closeConnectionToServer();
		
		ClientView clientApplication = new ClientView(client);
		 
		 
		

		/*
		client.suscribeCategory("Weather");
		//System.out.println("Clients subscribed to weather: " + Category.getCategoryByName("Weather").getSubscribedClientsList());


		//String categories = client.viewAllCategories();
		
		String myCategories = client.viewMyCategories();
		System.out.println("Clients categoriesssss: "+myCategories);
		//client.showNews();
		
		
		//client.closeConnectionToServer();
		 */
		System.out.println("END OF PROGRAM");
	}

}
