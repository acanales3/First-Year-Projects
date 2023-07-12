import javax.swing.*;

import java.awt.FileDialog;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.prefs.*;

public class Lab3 extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JButton addButton;
	private JLabel addressLabel;
	private JPanel addressPanel;
	private JTextField addressTextField;
	private JPanel buttonPanel;
	private JLabel cityLabel;
	private JPanel cityStatePanel;
	private JTextField cityTextField;
	private JButton deleteButton;
	private JButton findButton;
	private JButton firstButton;
	private JLabel givenNameLabel;
	private JPanel givenNamePanel;
	private JTextField givenNameTextField;
	private JButton lastButton;
	private JButton nextButton;
	private JButton previousButton;
	private JLabel stateLabel;
	private JTextField stateTextField;
	private JLabel surnameLabel;
	private JPanel surnamePanel;
	private JTextField surnameTextField;
	private JButton updateButton;
	
	String bookFile = null;
	String indexFile = null;
	
	RandomAccessFile index; 
	RandomAccessFile book;

	public Lab3() {
		setTitle("Address Book");
		setBounds(100, 100, 704, 239);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new java.awt.GridLayout(5, 0));

		surnamePanel = new JPanel();
		surnameLabel = new JLabel();
		surnameTextField = new JTextField();
		givenNamePanel = new JPanel();
		givenNameLabel = new JLabel();
		givenNameTextField = new JTextField();
		addressPanel = new JPanel();
		addressLabel = new JLabel();
		addressTextField = new JTextField();
		cityStatePanel = new JPanel();
		cityLabel = new JLabel();
		cityTextField = new JTextField();
		stateLabel = new JLabel();
		stateTextField = new JTextField();
		buttonPanel = new JPanel();
		firstButton = new JButton();
		nextButton = new JButton();
		previousButton = new JButton();
		lastButton = new JButton();
		findButton = new JButton();
		addButton = new JButton();
		deleteButton = new JButton();
		updateButton = new JButton();

		surnamePanel.setName("surnamePanel");

		surnameLabel.setText("Surname");
		surnameLabel.setName("surnameLabel");
		surnamePanel.add(surnameLabel);

		surnameTextField.setColumns(45);
		surnameTextField.setText("");
		surnameTextField.setName("surnameTextField");
		surnamePanel.add(surnameTextField);

		getContentPane().add(surnamePanel);

		givenNamePanel.setName("givenNamePanel");

		givenNameLabel.setText("Given Names");
		givenNameLabel.setName("givenNameLabel");
		givenNamePanel.add(givenNameLabel);

		givenNameTextField.setColumns(45);
		givenNameTextField.setText("");
		givenNameTextField.setName("givenNameTextField");
		givenNamePanel.add(givenNameTextField);

		getContentPane().add(givenNamePanel);

		addressPanel.setName("addressPanel");

		addressLabel.setText("Street Address");
		addressLabel.setName("addressLabel");
		addressPanel.add(addressLabel);

		addressTextField.setColumns(45);
		addressTextField.setText("");
		addressTextField.setName("addressTextField");
		addressPanel.add(addressTextField);

		getContentPane().add(addressPanel);

		cityStatePanel.setName("cityStatePanel");

		cityLabel.setText("City");
		cityLabel.setName("cityLabel");
		cityStatePanel.add(cityLabel);

		cityTextField.setColumns(30);
		cityTextField.setText("");
		cityTextField.setName("cityTextField");
		cityStatePanel.add(cityTextField);

		stateLabel.setText("State");
		stateLabel.setName("stateLabel");
		cityStatePanel.add(stateLabel);

		stateTextField.setColumns(5);
		stateTextField.setText("");
		stateTextField.setName("stateTextField");
		cityStatePanel.add(stateTextField);

		getContentPane().add(cityStatePanel);

		buttonPanel.setName("buttonPanel");

		firstButton.setText("First");
		firstButton.setName("firstButton");
		firstButton.addActionListener(this);
		buttonPanel.add(firstButton);

		nextButton.setText("Next");
		nextButton.setName("nextButton");
		nextButton.addActionListener(this);
		buttonPanel.add(nextButton);

		previousButton.setText("Previous");
		previousButton.setName("previousButton");
		previousButton.addActionListener(this);
		buttonPanel.add(previousButton);

		lastButton.setText("Last");
		lastButton.setName("lastButton");
		lastButton.addActionListener(this);
		buttonPanel.add(lastButton);

		findButton.setText("Find");
		findButton.setName("findButton");
		findButton.addActionListener(this);
		buttonPanel.add(findButton);

		addButton.setText("Add");
		addButton.setEnabled(false);
		addButton.setName("addButton");
		addButton.addActionListener(this);
		buttonPanel.add(addButton);

		deleteButton.setText("Delete");
		deleteButton.setEnabled(false);
		deleteButton.setName("deleteButton");
		deleteButton.addActionListener(this);
		buttonPanel.add(deleteButton);

		updateButton.setText("Update");
		updateButton.setEnabled(false);
		updateButton.setName("updateButton");
		updateButton.addActionListener(this);
		buttonPanel.add(updateButton);

		getContentPane().add(buttonPanel);

		getFiles();
		
		try {
			index = new RandomAccessFile(indexFile, "r");
			book = new RandomAccessFile(bookFile, "r");
		} catch(IOException ioe) {
			System.out.println(ioe);
			System.exit(0);
		}

	}
	
	void getFiles() {
			FileDialog fd = new FileDialog(this, "Select the Address Book", FileDialog.LOAD);
			fd.setVisible(true);
			String filename = fd.getFile();
			if (filename == null)
				System.exit(0);
			bookFile = fd.getDirectory() + filename;
			fd = new FileDialog(this, "Select the Index File", FileDialog.LOAD);
			fd.setVisible(true);
			filename = fd.getFile();
			if (filename == null)
				System.exit(0);
			indexFile = fd.getDirectory() + filename;
	}


	public static void main(String[] args) {
		Lab3 window = new Lab3();
		window.setVisible(true);
	}

/***************************************************************/

//implement this method
//you may add additional methods as needed
//do not change any of the code that I have written
//other than to activate buttons for extra credit
	
//Sets a variable to increment and decrement from
	long indexLength = 0;
		
	public void actionPerformed(ActionEvent evt) {

// First button execution
		if (evt.getActionCommand().equals("First")) {
			
			try {
				
// Grabs the first entry in the book
				indexLength = 0;
				
				index.seek(indexLength);
				book.seek(index.readLong());
				
// Reads each of the fields into their respective boxes
				String surname = book.readUTF();
				surnameTextField.setText(surname);

				String givenN = book.readUTF();
				givenNameTextField.setText(givenN);
				
				String address = book.readUTF();
				addressTextField.setText(address);
				
				String city = book.readUTF();
				cityTextField.setText(city);
				
				String state = book.readUTF();
				stateTextField.setText(state);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
// Next button execution
		} else if (evt.getActionCommand().equals("Next")) {
				
			try {
				
// Checks if the indexLength at the button press will exceed the amount of entries
				if (indexLength >= (index.length() - 8)) {
					indexLength = index.length() - 8;
				} else {
					indexLength += 8;
				}
				
// Finds the index of the next item in the file
				index.seek(indexLength);
				book.seek(index.readLong());
				
// Reads each of the fields into their respective boxes
				String surname = book.readUTF();
				surnameTextField.setText(surname);

				String givenN = book.readUTF();
				givenNameTextField.setText(givenN);
				
				String address = book.readUTF();
				addressTextField.setText(address);
				
				String city = book.readUTF();
				cityTextField.setText(city);
				
				String state = book.readUTF();
				stateTextField.setText(state);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
// Previous button execution
		} else if (evt.getActionCommand().equals("Previous")) {
			
			try {
				
// Checks if the indexLength at the button press will go lower than the amount of entries
				if (indexLength <= 0) {
					indexLength = 0;
				} else {
					indexLength -= 8;
				}
				
// Grabs the index of the previous item
				index.seek(indexLength);
				book.seek(index.readLong());
				
// Reads each of the fields into their respective boxes
				String surname = book.readUTF();
				surnameTextField.setText(surname);

				String givenN = book.readUTF();
				givenNameTextField.setText(givenN);
				
				String address = book.readUTF();
				addressTextField.setText(address);
				
				String city = book.readUTF();
				cityTextField.setText(city);
				
				String state = book.readUTF();
				stateTextField.setText(state);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
// Last button execution
		} else if (evt.getActionCommand().equals("Last")) {
			
			try {
				
// Grabs the last entry in the book
				indexLength = index.length() - 8;
				
				index.seek(indexLength);
				book.seek(index.readLong());
				
// Reads each of the fields into their respective boxes
				String surname = book.readUTF();
				surnameTextField.setText(surname);

				String givenN = book.readUTF();
				givenNameTextField.setText(givenN);
				
				String address = book.readUTF();
				addressTextField.setText(address);
				
				String city = book.readUTF();
				cityTextField.setText(city);
				
				String state = book.readUTF();
				stateTextField.setText(state);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
// Find button execution
		} else if (evt.getActionCommand().equals("Find")) {
			
			try {
				
// Grabs the surname field and given name text field
				String name = surnameTextField.getText() + "\t" + givenNameTextField.getText();
				
				index.seek(0);
				book.seek(index.readLong());
				
// Gets the first entry
				String firstEntry = book.readUTF();
				
				index.seek(index.length() - 8);
				book.seek(index.readLong());
				
// Gets the last entry
				String lastEntry = book.readUTF();
				
// Checks if the name grabbed from the box is greater or less than the first and last entries
// If it comes before or after the first and last entries it will set the indexLength accordingly
// Else it will use binary search to get the correct index
				if(firstEntry.compareToIgnoreCase(name) > 0 || name.equals("")) {
					indexLength = 0;
				} else if (lastEntry.compareToIgnoreCase(name) < 0) {
					indexLength = index.length() - 8;
				} else {
					indexLength = binarySearch(0, index.length() - 8, name);
				}
				
				index.seek(indexLength);
				book.seek(index.readLong());
				
// Reads each of the fields into their respective boxes
				String surname = book.readUTF();
				surnameTextField.setText(surname);

				String givenN = book.readUTF();
				givenNameTextField.setText(givenN);
				
				String address = book.readUTF();
				addressTextField.setText(address);
				
				String city = book.readUTF();
				cityTextField.setText(city);
				
				String state = book.readUTF();
				stateTextField.setText(state);
				
			} catch (EOFException e) {
				e.printStackTrace();
			} catch (IOException eo) {
				eo.printStackTrace();
			}
			
		}
		
	}
	
// My binary search method
	private long binarySearch(long startIndex, long endIndex, String target) throws IOException, EOFException {
				
// Base case
		if(startIndex > endIndex) {
			return startIndex;
		}
		
// Middle index between the start and end indexes
		long midIndex = (startIndex + endIndex) / 2;
		
// Makes sure the index fits the criteria for each 8 bytes
		if (!(midIndex % 8 == 0)) {
			midIndex += midIndex % 8;
		}
				
		index.seek(midIndex);
		book.seek(index.readLong());
		
// Gets the name of the middle index
		String name = book.readUTF() + "\t" + book.readUTF();
	
		int nameCompare = name.compareToIgnoreCase(target);
				
// Uses the compareTo method to check if the name matches the target
// Else it will continue to recursively call the binarySearch method till an index is found
		if(nameCompare == 0) {
			
			return midIndex;
			
		} else if (nameCompare < 0) {
			
			return binarySearch(midIndex + 8, endIndex, target);
			
		} else {
			
			return binarySearch(startIndex, midIndex - 8, target);
									
		}

	}
	
}
