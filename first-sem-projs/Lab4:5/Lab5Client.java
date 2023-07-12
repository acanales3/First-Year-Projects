import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

//*************************************
//WRITTEN BY ALEX CANALES AND BRAE OGLE
//*************************************

public class Lab5Client extends JFrame implements ActionListener {
	static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		Lab5Client scc = new Lab5Client();
		scc.setVisible(true);
	}

	JRadioButton dieselButton;
	JRadioButton steamButton;
	ButtonGroup locomotiveGroup;

	JRadioButton cabooseButton;
	JRadioButton presidentialButton;
	JRadioButton firstClassButton;
	JRadioButton openAirButton;
	ButtonGroup seatingGroup;

	JLabel adultLabel;
	JTextField adultTF;
	JLabel childrenLabel;
	JTextField childrenTF;

	JButton calcButton;
	JLabel ansLabel;
	JTextField ansTF;

	JLabel errorLabel;

	public Lab5Client() {
		setTitle("Price Calculator");
		setBounds(100, 100, 320, 280);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		dieselButton = new JRadioButton("Diesel");
		dieselButton.setBounds(25, 24, 100, 16);
		dieselButton.setSelected(true);
		getContentPane().add(dieselButton);

		steamButton = new JRadioButton("Steam");
		steamButton.setBounds(25, 64, 100, 16);
		getContentPane().add(steamButton);

		locomotiveGroup = new ButtonGroup();
		locomotiveGroup.add(dieselButton);
		locomotiveGroup.add(steamButton);

		cabooseButton = new JRadioButton("Caboose");
		cabooseButton.setBounds(170, 14, 100, 16);
		cabooseButton.setSelected(true);
		getContentPane().add(cabooseButton);

		presidentialButton = new JRadioButton("Presidential");
		presidentialButton.setBounds(170, 34, 140, 16);
		getContentPane().add(presidentialButton);

		firstClassButton = new JRadioButton("First Class");
		firstClassButton.setBounds(170, 54, 140, 16);
		getContentPane().add(firstClassButton);

		openAirButton = new JRadioButton("Open Air");
		openAirButton.setBounds(170, 74, 140, 16);
		getContentPane().add(openAirButton);

		seatingGroup = new ButtonGroup();
		seatingGroup.add(cabooseButton);
		seatingGroup.add(presidentialButton);
		seatingGroup.add(firstClassButton);
		seatingGroup.add(openAirButton);

		adultLabel = new JLabel("Adults");
		adultLabel.setBounds(30, 104, 50, 16);
		getContentPane().add(adultLabel);
		adultLabel.setVisible(false);

		adultTF = new JTextField();
		adultTF.setBounds(80, 104, 50, 16);
		getContentPane().add(adultTF);
		adultTF.setVisible(false);

		childrenLabel = new JLabel("Children");
		childrenLabel.setBounds(160, 104, 60, 16);
		getContentPane().add(childrenLabel);
		childrenLabel.setVisible(false);

		childrenTF = new JTextField();
		childrenTF.setBounds(225, 104, 50, 16);
		getContentPane().add(childrenTF);
		childrenTF.setVisible(false);

		calcButton = new JButton("Calculate");
		calcButton.setBounds(100, 144, 80, 16);
		getContentPane().add(calcButton);

		ansLabel = new JLabel("Answer");
		ansLabel.setBounds(80, 184, 50, 16);
		getContentPane().add(ansLabel);

		ansTF = new JTextField();
		ansTF.setBounds(150, 184, 75, 16);
		getContentPane().add(ansTF);
		ansTF.setEditable(false);

		errorLabel = new JLabel("");
		errorLabel.setBounds(40, 220, 200, 16);
		getContentPane().add(errorLabel);

		cabooseButton.addActionListener(this);
		presidentialButton.addActionListener(this);
		firstClassButton.addActionListener(this);
		openAirButton.addActionListener(this);
		calcButton.addActionListener(this);

		connectToServer();
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Diesel") || action.equals("Steam")) {
			ansTF.setText("");
			errorLabel.setText("");
		} else if (action.equals("Caboose")) {
			ansTF.setText("");
			errorLabel.setText("");
			adultLabel.setVisible(false);
			adultTF.setVisible(false);
			childrenLabel.setVisible(false);
			childrenTF.setVisible(false);
		} else if (action.equals("Presidential") || action.equals("First Class") || action.equals("Open Air")) {
			ansTF.setText("");
			errorLabel.setText("");
			adultLabel.setVisible(true);
			adultTF.setVisible(true);
			childrenLabel.setVisible(true);
			childrenTF.setVisible(true);
		} else if (action.equals("Calculate")) {
			calculate();
		}
	}

	// ========================================================================
	
// *************************************
// WRITTEN BY ALEX CANALES AND BRAE OGLE
// *************************************

	// Do not change anything above this line
	// Two global variables have been defined here and you will need to add more
	final static String server = "127.0.0.1";
	final static int port = 7777;
	Socket s;
	BufferedReader is;
	PrintWriter os;

	// Then implement the following methods
	void connectToServer() {
		try
		{
// Creation of a socket to connect to socket server with server IPv4 address and port number
			s = new Socket (server, port);
			os = new PrintWriter ( new BufferedOutputStream(s.getOutputStream()));
			is = new BufferedReader(new InputStreamReader(s.getInputStream()));
		}
		catch(Exception e)
		{
			errorLabel.setText("Cannot connect to server");
		}
	}

	// The following method sends an appropriate command to the server
	// Then reads the result and displays it in the answer text field
	void calculate() {
		
// Reset the error label
		errorLabel.setText("");
		
		int child = 0;
		int adult = 0;
		
// var 'line' creates a command line to send to the server for the calculations
		String line = ""; 
		
//If their is a error converting # of adult or children to integers, do not send info to server
		boolean parseIntError = false;

		try
		{
// Based on the buttons pressed, commands will be added to the line to send to the server
			if (dieselButton.isSelected())
			{
				line = "Diesel";
				if (cabooseButton.isSelected())
				{
					line+= " Caboose";
				}
				else if (presidentialButton.isSelected())
				{
					line+= " Presidential";
					try
					{
						child = Integer.parseInt(childrenTF.getText());

					}
					catch (Exception e)
					{
// For each Integer.parseInt there is a try/catch for if the input is not an int value and to send back an error message
// This is for each child and parent input, and I will not do multiple comments for its redundancy 
						errorLabel.setText("Please enter the number of children as an integer");
						parseIntError = true;
					}
					try
					{
						adult = Integer.parseInt(adultTF.getText());
					}
					catch (Exception e)
					{
						errorLabel.setText("Please enter the number of adults as an integer");
						parseIntError = true;
					}
					line+= " " +adult +" " +child;
				}
				else if (firstClassButton.isSelected())
				{
					line+= " FirstClass";
					try
					{
						child = Integer.parseInt(childrenTF.getText());
					}
					catch (Exception e)
					{
						errorLabel.setText("Please enter the number of children as an integer");
						parseIntError = true;
					}
					try
					{
						adult = Integer.parseInt(adultTF.getText());
					}
					catch (Exception e)
					{
						errorLabel.setText("Please enter the number of adults as an integer");
						parseIntError = true;
					}
					line+= " " +adult +" " +child;
				}
				else 
// Last button is Open Air cart
				{
					line+= " OpenAir";
					try
					{
						child = Integer.parseInt(childrenTF.getText());
					}
					catch (Exception e)
					{
						errorLabel.setText("Please enter the number of children as an integer");
						parseIntError = true;
					}
					try
					{
						adult = Integer.parseInt(adultTF.getText());
					}
					catch (Exception e)
					{
						errorLabel.setText("Please enter the number of adults as an integer");
						parseIntError = true;
					}
					line+= " " +adult +" " +child;
				}
			}
			else 
// If not Diesel button, then it is a Steam button
			{
				line = "Steam";
				if (cabooseButton.isSelected())
				{
					line+= " Caboose";
				}
				else if (presidentialButton.isSelected())
				{
					line+= " Presidential";
					try
					{
						child = Integer.parseInt(childrenTF.getText());

					}
					catch (Exception e)
					{
						errorLabel.setText("Please enter the number of children as an integer");
						parseIntError = true;
					}
					try
					{
						adult = Integer.parseInt(adultTF.getText());
					}
					catch (Exception e)
					{
						errorLabel.setText("Please enter the number of adults as an integer");
						parseIntError = true;
					}
					line+= " " +adult +" " +child;
				}
				else if (firstClassButton.isSelected())
				{
					line+= " FirstClass";
					try
					{
						child = Integer.parseInt(childrenTF.getText());
					}
					catch (Exception e)
					{
						errorLabel.setText("Please enter the number of children as an integer");
						parseIntError = true;
					}
					try
					{
						adult = Integer.parseInt(adultTF.getText());
					}
					catch (Exception e)
					{
						errorLabel.setText("Please enter the number of adults as an integer");
						parseIntError = true;
					}
					line+= " " +adult +" " +child;
				}
				else
// Last button is Open Air cart
				{
					line+= " OpenAir";
					try
					{
						child = Integer.parseInt(childrenTF.getText());
					}
					catch (Exception e)
					{
						errorLabel.setText("Please enter the number of children as an integer");
						parseIntError = true;
					}
					try
					{
						adult = Integer.parseInt(adultTF.getText());
					}
					catch (Exception e)
					{
						errorLabel.setText("Please enter the number of adults as an integer");
						parseIntError = true;
					}
					line+= " " +adult +" " +child;
				}
			}
			
// Error for if the input is a negative number
			if(adult < 0 || child < 0)
			{
				errorLabel.setText("Cannot have negative people");
				ansTF.setText("");
			}
//If their is a error converting # of adult or children to integers, do not send information to the server
			else if (!parseIntError)
			{
				os.println(line);
				os.flush();
				try{
					ansTF.setText("$" +is.readLine());
				}
				catch (Exception e)
				{
// Error reading back data from the server
					errorLabel.setText("Server Error");
					ansTF.setText("");
				}
			}
			else
			{
// Means that there was an empty text field for children or adults
				errorLabel.setText("Must have integer values \n for adults and children");
				ansTF.setText("");
			}

			//is.close();
			//os.close();
			//s.close();

		}
		catch (Exception e)
		{
			errorLabel.setText("I/O error: " + e);
		}
	}
}

