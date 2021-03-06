package Views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import zad1.Models.ClientModel;

public class SubscribeButton extends JButton implements ActionListener {
	private ClientModel model;
	private JTextField categoryNameTextField;
	private CategoryButton categoryButton;
	
	public SubscribeButton(String text, ClientModel model, 
			JTextField categoryNameTextField,
			CategoryButton categoryButton) {
		super(text);
		this.model = model;
		this.categoryNameTextField = categoryNameTextField;
		this.categoryButton = categoryButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("CLIENT: Subscribe button clicked.");
		String categoryName = categoryNameTextField.getText();
		//model.connectToServer();
		try {
			model.suscribeCategory(categoryName);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		categoryButton.checkCategories();
		categoryButton.refreshPanel();
	}

}
