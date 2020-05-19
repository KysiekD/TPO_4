package zad1.Models;

public class NewsModel {

	
	private String newsHeader;
	private String newsContent;
	
	public NewsModel(String newsHeader) {
		this.newsHeader = newsHeader;
		this.newsContent = "No content, only advertisement!";
	}

	public String getNewsContent() {
		return newsContent;
	}

	public void setNewsContent(String content) {
		this.newsContent = content;
	}

	public String getNewsHeader() {
		return newsHeader;
	}

	public void setNewsHeader(String newsHeader) {
		this.newsHeader = newsHeader;
	}
	
	
	
}
