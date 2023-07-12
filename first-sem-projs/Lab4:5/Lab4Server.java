import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.io.*;

//*************************************
//WRITTEN BY ALEX CANALES AND BRAE OGLE
//*************************************

public class Lab4Server {

	final static int PORT = 9999;

	public static void main (String[] args) {
		
// Creation of Server Socket
		ServerSocket serverSocket = null;
	
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("Could not listen on port: " + PORT + ", " + e);
			System.exit(1);
		}

		while (true) {
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				System.out.println("Accept failed: " + PORT + ", " + e);
				continue;
			}
// Creates a new Thread for each Client that wants to reach to the server
			new Lab4ServerThread(clientSocket).start();
		}
	
	}
	
}


class Lab4ServerThread extends Thread{
	
// Creation of the socket to connect to the server socket
	Socket socket = null;

	Lab4ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			
			BufferedReader is = new BufferedReader(new 
									 InputStreamReader(socket.getInputStream()));
			PrintWriter os = new PrintWriter(new 
									 BufferedOutputStream(socket.getOutputStream()));

// var 'command' takes the string command that is sent from the client to do the correct
// calculations to send back to the client
			String command;
			
			while ((command = is.readLine()) != null) {
			
			Scanner s = new Scanner(command);
			
			String locomotive = "";
			String seatType = "";
			int numAdults = 0, numChildren = 0, price = 0;
			
			try {
				locomotive = s.next();
				seatType = s.next();
				if(!(seatType.equals("Caboose"))) {
					numAdults = Integer.parseInt(s.next());
					numChildren = Integer.parseInt(s.next());
				}
			} catch (Exception e) {
// Unreachable error because the client does not send anything other than 2 or 4 statements in the command
				price = -1;
// Only applies when using anything except for the Lab5Client
			}

// Calculations for all prices in the Diesel locomotive
			if(locomotive.equals("Diesel"))
			{
				if(seatType.equals("Caboose"))
				{
					price = 850;
				}
				else if (seatType.equals("Presidential"))
				{
					price = (77 * numAdults) + (57 * numChildren);
				}
				else if (seatType.equals("FirstClass"))
				{
					price = (57 * numAdults) + (37 * numChildren);
				}
				else if (seatType.equals("OpenAir"))
				{
					price = (27 * numAdults) + (22 * numChildren);
				}
				else 
				{
					price = -1;
				}
			}
// Calculations for all prices in the Steam locomotive
			else if (locomotive.equals("Steam"))
			{
				if(seatType.equals("Caboose"))
				{
					price = 950;
				}
				else if (seatType.equals("Presidential"))
				{

					price = (87 * numAdults) + (67 * numChildren);
				}
				else if (seatType.equals("FirstClass"))
				{
					price = (67 * numAdults) + (47 * numChildren);
				}
				else if (seatType.equals("OpenAir"))
				{
					price = (37 * numAdults) + (32 * numChildren);
				}
				else 
				{
					price = -1;
				}
			}
			else 
			{
				price = -1;
			}
			
// If statement only applicable for anything other than Lab5Client
			if (price < 0 || numChildren < 0 || numAdults < 0) {
				os.println("Please enter correct format");
				os.flush();
			} else {
				os.println(price);
				os.flush();
			}
			
			}
			
// Closes input stream, output stream, and socket after completion
			os.close();
			is.close();
			socket.close();
			
		} catch (IOException e) {
			System.out.println("I/O error: " + e);
		}
	}
	
}