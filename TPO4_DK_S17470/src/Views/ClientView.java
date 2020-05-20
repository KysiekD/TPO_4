package Views;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import zad1.Models.ClientModel;

public class ClientView extends JFrame{

	private ClientModel model;
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JLabel topLabel;
	private NewsButton newsButton;
	private CategoryButton categoryButton;
	


	
	public ClientView(ClientModel model) {
		this.model=model;
		this.mainFrame = new JFrame();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		topLabel = new JLabel("NEWS!!!");
		newsButton = new NewsButton("News.", model);
		newsButton.addActionListener(newsButton);
		categoryButton = new CategoryButton("Categories.",model);
		categoryButton.addActionListener(categoryButton);
		
		mainPanel.add(topLabel);
		mainPanel.add(newsButton);
		mainPanel.add(categoryButton);
		mainFrame.add(mainPanel);
		mainFrame.setVisible(true);
		mainFrame.setSize(300,200);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
	}
	
}
