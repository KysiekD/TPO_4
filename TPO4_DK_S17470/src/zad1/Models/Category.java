package zad1.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class Category {

	private String categoryName;
	//private HashMap<String, ClientModel> subscribesList;
	private HashMap<String, String []> subscribesList;

	private HashMap<String, NewsModel> newsModelsList;
	private static ArrayList<Category> topicExtension = new ArrayList<>();
	
	
	public Category(String categoryName) throws Exception {
	if(Category.getCategoryByName(categoryName)!=null) {
		throw new Exception("Category with name "+categoryName+" already exists.");
	}
	this.categoryName=categoryName;
	subscribesList = new HashMap<>();
	newsModelsList = new HashMap<>();
	topicExtension.add(this);
	}
	
	
	public static ArrayList<Category> getTopicExtension() {
		return topicExtension;
	}
	
	public static Category getCategoryByName(String categoryName) {
		for(Category category : topicExtension) {
			if(category.getCategoryName().equals(categoryName)) {
				return category;
			}
		}
		return null;
	}
	
	
	public void addClientToSubscribesList(String clientName, String clientHostName, int clientPortNumber){
		//ClientModel clientModel = new ClientModel(clientHostName, clientPort, clientName);
		//subscribesList.put(clientModel.getClientName(), clientModel);
		
		String [] clientInfo = new String[3];
		clientInfo[0]=clientName;
		clientInfo[1]=clientHostName;
		clientInfo[2]=String.valueOf(clientPortNumber);
		subscribesList.put(clientName, clientInfo);
		

	}
	
	public void removeClientFromSubscribesList(String clientName) {

		subscribesList.remove(clientName);
	}
	
	public void addNewsToNewsList(String newsHeader) {
		NewsModel newsModel = new NewsModel(newsHeader);
		newsModelsList.put(newsModel.getNewsHeader(), new NewsModel(newsHeader));
	}
	
	public String[] getClientModelFromList(String clientName) {
		System.out.println("Category check for a client..."); //TEST
		return subscribesList.get(clientName);
	}
	
	public NewsModel getNewsModelByHeader(String newsHeader) {
		return newsModelsList.get(newsHeader);
	}
	
	public Set<String> getNewsModelsFromList() {
		return newsModelsList.keySet();
	}
	
	public Set<String> getSubscribedClientsList() throws InterruptedException {
		//.....dokonczyc
		//System.out.println(subscribesList.keySet());
		

		return subscribesList.keySet();
		
	}
	

	//Getters and setters
	public String getCategoryName() {
		return categoryName;
	}


}