package Views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import zad1.Models.ClientModel;

public class UnsubscribeButton extends JButton implements ActionListener {

	private ClientModel model;
	private JTextField categoryNameTextField;
	private CategoryButton categoryButton;

	public UnsubscribeButton(String text, ClientModel model, 
			JTextField categoryNameTextField,
			CategoryButton categoryButton){
		super(text);
		this.model = model;
		this.categoryNameTextField = categoryNameTextField;
		this.categoryButton = categoryButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("CLIENT: Unsuscribe button clicked.");

		String categoryName = categoryNameTextField.getText();
		model.connectToServer();

		model.unsuscribeCategory(categoryName);

		categoryButton.checkCategories();
		categoryButton.refreshPanel();
	}
}
