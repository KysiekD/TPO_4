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

public class CategoryButton extends JButton implements ActionListener{
	private ClientModel model;
	private JFrame categoriesFrame;
	private JPanel categoriesPanel;
	private JLabel allCategoriesMainLabel, myCategoriesLabel, textFieldLabel;
	private JTextField textField;
	private SubscribeButton subscribeButton;
	private String allCategories;
	private String myCategories;
	
	
	public CategoryButton(String text, ClientModel model) {
		super(text);
		this.model=model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		checkCategories();
		
		//model.closeConnectionToServer();	
		
		categoriesFrame = new JFrame();
		categoriesPanel = new JPanel();
		categoriesPanel.setLayout(new BoxLayout(categoriesPanel, BoxLayout.PAGE_AXIS));
		
		//categoriesTopLabel = new JLabel("ALL CATEGORIES:");
		//categoriesPanel.add(categoriesTopLabel);
		
		allCategoriesMainLabel = new JLabel(allCategories);
		categoriesPanel.add(allCategoriesMainLabel);
		
		myCategoriesLabel = new JLabel(myCategories);
		categoriesPanel.add(myCategoriesLabel);
		
		textFieldLabel = new JLabel("Please write category to subscribe or unsubscribe: ");
		categoriesPanel.add(textFieldLabel);
		
		
		textField = new JTextField();
		categoriesPanel.add(textField);
		
		
		subscribeButton = new SubscribeButton("Subscribe",model, textField, this);
		subscribeButton.addActionListener(subscribeButton);
		categoriesPanel.add(subscribeButton);
		
		categoriesFrame.add(categoriesPanel);
		categoriesFrame.setSize(400,200);
		categoriesFrame.setLocationRelativeTo(null);
		categoriesFrame.pack();
		categoriesFrame.setVisible(true);
		
		
		
	}

	
	public void checkCategories() {
		System.out.println("CLIENT: Category button clicked.");

		model.connectToServer();
		try {
			allCategories = model.viewAllCategories();
			Thread.sleep(1000);
			myCategories = model.viewMyCategories();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public void refreshPanel() {
		myCategoriesLabel.setText(myCategories);
		categoriesPanel.repaint();
	}
	
}
