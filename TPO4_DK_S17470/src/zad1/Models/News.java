package zad1.Models;

public class News {

	private Topic topic;
	private String newsHeader;
	
	public News(Topic topic, String newsHeader) {
		this.topic = topic;
		this.newsHeader = newsHeader;
	}

	//Getters and setters:
	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public String getNewsHeader() {
		return newsHeader;
	}

	public void setNewsHeader(String newsHeader) {
		this.newsHeader = newsHeader;
	}
	
	
	
}
