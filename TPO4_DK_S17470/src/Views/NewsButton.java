package Views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import zad1.Models.ClientModel;

public class NewsButton extends JButton implements ActionListener {
	private ClientModel model;
	
	
	public NewsButton(String text, ClientModel model) {
		super(text);
		this.model=model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("CLIENT: News button clicked.");
	}

}
