package Views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import zad1.Models.ClientModel;

public class NewsButton extends JButton implements ActionListener {
	private ClientModel model;
	
	private JFrame newsFrame;
	private JPanel newsPanel;
	private JLabel newsLabel;

	
	public NewsButton(String text, ClientModel model) {
		super(text);
		this.model=model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("CLIENT: News button clicked.");
		
		newsFrame = new JFrame();
		newsPanel = new JPanel();
		
		String news = "<html><br>"+checkNews(model)+"</br></html>";
		news = news.replace("\n", "</br><br>");
		//String news = checkNews(model);
		
		newsLabel = new JLabel(news);
		newsPanel.add(newsLabel);
		
		newsPanel.setLayout(new BoxLayout(newsPanel, BoxLayout.PAGE_AXIS));
		newsFrame.setSize(400,200);
		newsFrame.setLocationRelativeTo(null);
		newsFrame.add(newsPanel);
		
		newsFrame.setVisible(true);
		
	}
	
	public String checkNews(ClientModel model) {
		//model.connectToServer();
		String news = model.showNews();
		Boolean testCheckForN = news.contains("\n");
		return news;
	}

}
