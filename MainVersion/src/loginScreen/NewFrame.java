package loginScreen;

import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;


@SuppressWarnings("serial")
public class NewFrame extends JFrame {

	public static void main(String[] args) 
	{
		NewFrame frameTabel = new NewFrame();
	}

	JLabel welcome = new JLabel("Please close and go back to the Console");
	JPanel panel = new JPanel();

	NewFrame(){
		super("Welcome");
		setSize(300,200);
		setLocation(500,280);
		panel.setLayout (null); 

		welcome.setBounds(70,50,150,60);

		panel.add(welcome);

		getContentPane().add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

}
