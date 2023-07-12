import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Lab1 extends JFrame implements ActionListener {
	static final long serialVersionUID = 1l;
	private JTextField assemblerInstruction;
	private JTextField binaryInstruction;
	private JTextField hexInstruction;
	private JLabel errorLabel;
		
	public Lab1() {
		setTitle("XDS Sigma 9");
		setBounds(100, 100, 400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
// * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// SET UP THE ASSEMBLY LANGUAGE TEXTFIELD AND BUTTON
		assemblerInstruction = new JTextField();
		assemblerInstruction.setBounds(25, 24, 134, 28);
		getContentPane().add(assemblerInstruction);

		JLabel lblAssemblyLanguage = new JLabel("Assembly Language");
		lblAssemblyLanguage.setBounds(30, 64, 160, 16);
		getContentPane().add(lblAssemblyLanguage);

		JButton btnEncode = new JButton("Encode");
		btnEncode.setBounds(200, 25, 117, 29);
		getContentPane().add(btnEncode);
		btnEncode.addActionListener(this);
		
// * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// SET UP THE BINARY INSTRUCTION TEXTFIELD AND BUTTON
		binaryInstruction = new JTextField();
		binaryInstruction.setBounds(25, 115, 330, 28);
		getContentPane().add(binaryInstruction);

		JLabel lblBinary = new JLabel("Binary Instruction");
		lblBinary.setBounds(30, 155, 190, 16);
		getContentPane().add(lblBinary);

		JButton btnDecode = new JButton("Decode Binary");
		btnDecode.setBounds(200, 150, 150, 29);
		getContentPane().add(btnDecode);
		btnDecode.addActionListener(this);
// * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// SET UP THE HEX INSTRUCTION TEXTFIELD AND BUTTON
		hexInstruction = new JTextField();
		hexInstruction.setBounds(25, 220, 134, 28);
		getContentPane().add(hexInstruction);

		JLabel lblHexEquivalent = new JLabel("Hex Instruction");
		lblHexEquivalent.setBounds(30, 260, 131, 16);
		getContentPane().add(lblHexEquivalent);

		JButton btnDecodeHex = new JButton("Decode Hex");
		btnDecodeHex.setBounds(200, 220, 150, 29);
		getContentPane().add(btnDecodeHex);
		btnDecodeHex.addActionListener(this);		
// * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// SET UP THE LABEL TO DISPLAY ERROR MESSAGES
		errorLabel = new JLabel("");
		errorLabel.setBounds(25, 320, 280, 16);
		getContentPane().add(errorLabel);
	}

	public void actionPerformed(ActionEvent evt) {
		errorLabel.setText("");
		if (evt.getActionCommand().equals("Encode")) {
			encode();
		} else if (evt.getActionCommand().equals("Decode Binary")) {
			decodeBin();
		} else if (evt.getActionCommand().equals("Decode Hex")) {
			decodeHex();
		}
	}

	public static void main(String[] args) {
		Lab1 window = new Lab1();
		window.setVisible(true);
	}

// USE THE FOLLOWING METHODS TO CREATE A STRING THAT IS THE
// BINARY OR HEX REPRESENTATION OF A SORT OR INT

// CONVERT AN INT TO 8 HEX DIGITS
	String displayIntAsHex(int x) {
		String ans="";
		for (int i=0; i<8; i++) {
			int hex = x & 15;
			char hexChar = "0123456789ABCDEF".charAt(hex);
			ans = hexChar + ans;
			x = (x >> 4);
		}
		return ans;
	}

// CONVERT AN INT TO 32 BINARY DIGITS
	String displayIntAsBinary(int x) {
		String ans="";
		for(int i=0; i<32; i++) {
			ans = (x & 1) + ans;
			x = (x >> 1);
		}
		return ans;
	}
	
/************************************************************************/
/* Put your implementation of the encode, decodeBin, and decodeHex      */
/* methods here. You may add any other methods that you think are       */
/* appropriate. However, you MUST NOT change anything in the code       */
/* that I have written.                                                 */
/************************************************************************/
	void encode() {
				
		String assemblerString = assemblerInstruction.getText();
		
		int num = 0;
		int pos = assemblerString.indexOf(",");
		int secondPos = assemblerString.indexOf(" ");
		
		String firstHalf = assemblerString.substring(0, assemblerString.indexOf(" "));
		
		if (firstHalf.indexOf(",") == -1) {
			
			errorLabel.setText("ERROR -- Invalid first part");
			binaryInstruction.setText("");
			hexInstruction.setText("");
			
		} else {
			
			String r = assemblerString.substring(pos + 1, secondPos);
			int R = (int) Long.parseLong(r);
			
	//Checking to see the start of the assemblerInstruction whether it is LI, LW, AW, or STW which
	// sets the number equal to the first 8 bits for the assembly instruction in binary and hex for num
			
			if (assemblerString.substring(0,pos).equals("LI")) {
							
				num = (0b00100010) << 24;
				
	//Check range of R
				
				if ((R >= 0) && (R < 16)) {
					num = num | R << 20;
				} else {
					errorLabel.setText("ERROR -- illegal value for R");
					binaryInstruction.setText("");
					hexInstruction.setText("");
				}
				
	//Check range of V
				
				String v = assemblerString.substring((assemblerString.lastIndexOf(" ")) + 1);
				
				int V = (int) Long.parseLong(v);
				
				if ((V >= -524288) && (V <= 524287)) {
					
					V = V & (0b00000000000011111111111111111111);
					num = num | V;
					
					String sB  = displayIntAsBinary(num);
					String sH = displayIntAsHex(num);
				
					binaryInstruction.setText(sB);
					hexInstruction.setText(sH);
					
				} else { 
					errorLabel.setText("ERROR -- illegal value for LI");
					binaryInstruction.setText("");
					hexInstruction.setText("");
				}
							

			} else if (assemblerString.substring(0,pos).equals("LW")) {

	//Setting the number equal to the first 8 bits for the assembly instruction in binary and hex
				
				num = 0b00110010 << 24;
				
	//Check range of R

				if ((R >= 0) && (R < 16)) {
					num = num | R << 20;
				} else {
					errorLabel.setText("ERROR -- illegal value for R");
					binaryInstruction.setText("");
					hexInstruction.setText("");
				}
			
	//There are 2 cases to test, whether the "*" exists in the command or not
	//If "*" exists to make highest bit 1, which it is
				
				if (assemblerString.substring((secondPos + 1),(secondPos  + 2)).equals("*")) {
					num = num | (0b10000000000000000000000000000000);
				}
				
				if (assemblerString.substring((secondPos + 1), (secondPos + 2)).equals("*")) {
					
					num = num | (0b10000000000000000000000000000000);
					
					String secondHalf = assemblerString.substring(assemblerString.indexOf("*") + 1);
					
					if (secondHalf.indexOf(",") == -1) {
						
	// The if-statement looks if "," exists because 
	// only D exists and not X if there is no "," found in the second half of the string
	// In this case, "," does not exist, so only D exists
						
						String d = secondHalf;
						int D = (int) Long.parseLong(d);
						
	//Check range of D

						if ((D >= 0) && (D <= 131071)) {
							
							D = D & (0b00000000000000011111111111111111);
							num = num | D;
							
							String sB  = displayIntAsBinary(num);
							String sH = displayIntAsHex(num);
							
							binaryInstruction.setText(sB);
							hexInstruction.setText(sH);
							
						} else {
							errorLabel.setText("ERROR -- invalid value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
						}
						
					} else {
						
						String d = secondHalf.substring(0, secondHalf.indexOf(","));
						int D = (int) Long.parseLong(d);
						
						int X;
						String x = secondHalf.substring(secondHalf.indexOf(",") + 1);
						if(x.equals("")) {
							X = 0;
						} else {
							X = (int) Long.parseLong(x);
						}
						
	//Check range of D

						
						if ((D >= 0) && (D <= 131071)) {
							
	// The if-statement looks if "," exists because 
	// only D exists and not X if there is no "," found in the second half of the string
	// In this case, "," exists, so X exists
							
							D = D & (0b00000000000000011111111111111111);
							num = num | D;
							
	//Check range of X
							
							if ((X >= 0) && (X <=7)) {
								
								X = X << 17;
								num = num | X;
								
								String sB  = displayIntAsBinary(num);
								String sH = displayIntAsHex(num);
								
								binaryInstruction.setText(sB);
								hexInstruction.setText(sH);
							
							} else {
								errorLabel.setText("ERROR -- invalid value for X");
								binaryInstruction.setText("");
								hexInstruction.setText("");
							}
							
						} else {
							errorLabel.setText("ERROR -- invalid value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
						}
						
					}
					
									
				} else {
					
					String secondHalf = assemblerString.substring(assemblerString.indexOf(" ") + 1);
					
					if(secondHalf.indexOf(",") == -1) {
						
	// The if-statement looks if "," exists because 
	// only D exists and not X if there is no "," found in the second half of the string	
	// In this case, "," does not exist, so only D exists
						
						String d = secondHalf;
						int D = (int) Long.parseLong(d);
						
	//Check range of D
						
						if ((D >= 0) && (D <= 131071)) {
							
							D = D & (0b00000000000000011111111111111111);
							num = num | D;
							
							String sB  = displayIntAsBinary(num);
							String sH = displayIntAsHex(num);
							
							binaryInstruction.setText(sB);
							hexInstruction.setText(sH);
							
						} else {
							errorLabel.setText("ERROR -- invalid value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
						}
						
	//There are 2 cases to test, whether the "*" exists in the command or not
	//In this next case, "*" does not exist
						
					} else {
						
	// The if-statement looks if "," exists because 
	// only D exists and not X if there is no "," found in the second half of the string
	// In this case, "," exists, so X exists
						
						String d = secondHalf.substring(0, secondHalf.indexOf(","));
						int D = (int) Long.parseLong(d);
						
						int X;
						String x = secondHalf.substring(secondHalf.indexOf(",") + 1);
						if(x.equals("")) {
							X = 0;
						} else {
							X = (int) Long.parseLong(x);
						}
						
	//Check range of D
						
						if ((D >= 0) && (D <= 131071)) {
							
							D = D & (0b00000000000000011111111111111111);
							num = num | D;
							
	//Check range of X

							if ((X >= 0) && (X <= 7)) {
								
								X = X << 17;
								num = num | X;
								
								String sB  = displayIntAsBinary(num);
								String sH = displayIntAsHex(num);
								
								binaryInstruction.setText(sB);
								hexInstruction.setText(sH);
								
							} else {
								errorLabel.setText("ERROR -- invalid value for X");
								binaryInstruction.setText("");
								hexInstruction.setText("");
							}
							
						} else {
							errorLabel.setText("ERROR -- invalid value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
						}
						
					}
					
				}
				
	// ******************************************************************************
	// IT IS TO BE NOTED THAT THE SAME LOGIC USE FOR LW IS USED IN AW AND STW
	// THEREFORE I WILL NOT BE COPYING AND PASTING THE COMMENTS I MADE AS IT WOULD BE REDUNDANT
	// ******************************************************************************
				
			} else if (assemblerString.substring(0,pos).equals("AW")) {
				
				num = 0b00110000 << 24;
							
				if ((R >= 0) && (R < 16)) {
					num = num | R << 20;
				} else {
					errorLabel.setText("ERROR -- illegal value for R");
					binaryInstruction.setText("");
					hexInstruction.setText("");
				}
				
				if (assemblerString.substring((secondPos + 1),(secondPos  + 2)).equals("*")) {
					num = num | (0b10000000000000000000000000000000);
				}
				
				if (assemblerString.substring((secondPos + 1), (secondPos + 2)).equals("*")) {
					
					num = num | (0b10000000000000000000000000000000);
					
					String secondHalf = assemblerString.substring(assemblerString.indexOf("*") + 1);
					
					if (secondHalf.indexOf(",") == -1) {
											
						String d = secondHalf;
						int D = (int) Long.parseLong(d);
						
						if ((D >= 0) && (D <= 131071)) {
							
							D = D & (0b00000000000000011111111111111111);
							num = num | D;
							
							String sB  = displayIntAsBinary(num);
							String sH = displayIntAsHex(num);
							
							binaryInstruction.setText(sB);
							hexInstruction.setText(sH);
							
						} else {
							errorLabel.setText("ERROR -- invalid value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
						}
						
					} else {
						
						String d = secondHalf.substring(0, secondHalf.indexOf(","));
						int D = (int) Long.parseLong(d);
						
						int X;
						String x = secondHalf.substring(secondHalf.indexOf(",") + 1);
						if(x.equals("")) {
							X = 0;
						} else {
							X = (int) Long.parseLong(x);
						}
						
						if ((D >= 0) && (D <= 131071)) {
							
							D = D & (0b00000000000000011111111111111111);
							num = num | D;
							
							if ((X >= 0) && (X <=7)) {
								
								X = X << 17;
								num = num | X;
								
								String sB  = displayIntAsBinary(num);
								String sH = displayIntAsHex(num);
								
								binaryInstruction.setText(sB);
								hexInstruction.setText(sH);
							
							} else {
								errorLabel.setText("ERROR -- invalid value for X");
								binaryInstruction.setText("");
								hexInstruction.setText("");
							}
							
						} else {
							errorLabel.setText("ERROR -- invalid value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
						}
						
					}
					
									
				} else {
					
					String secondHalf = assemblerString.substring(assemblerString.indexOf(" ") + 1);
					
					if(secondHalf.indexOf(",") == -1) {
											
						String d = secondHalf;
						int D = (int) Long.parseLong(d);
						
						if ((D >= 0) && (D <= 131071)) {
							
							D = D & (0b00000000000000011111111111111111);
							num = num | D;
							
							String sB  = displayIntAsBinary(num);
							String sH = displayIntAsHex(num);
							
							binaryInstruction.setText(sB);
							hexInstruction.setText(sH);
							
						} else {
							errorLabel.setText("ERROR -- invalid value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
						}
						
					} else {
											
						String d = secondHalf.substring(0, secondHalf.indexOf(","));
						int D = (int) Long.parseLong(d);
						
						int X;
						String x = secondHalf.substring(secondHalf.indexOf(",") + 1);
						if(x.equals("")) {
							X = 0;
						} else {
							X = (int) Long.parseLong(x);
						}
						
						if ((D >= 0) && (D <= 131071)) {
							
							D = D & (0b00000000000000011111111111111111);
							num = num | D;
							
							if ((X >= 0) && (X <= 7)) {
								
								X = X << 17;
								num = num | X;
								
								String sB  = displayIntAsBinary(num);
								String sH = displayIntAsHex(num);
								
								binaryInstruction.setText(sB);
								hexInstruction.setText(sH);
								
							} else {
								errorLabel.setText("ERROR -- invalid value for X");
								binaryInstruction.setText("");
								hexInstruction.setText("");
							}
							
						} else {
							errorLabel.setText("ERROR -- invalid value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
						}
						
					}
					
				}
				
			} else if (assemblerString.substring(0,pos).equals("STW")) {
				
				num = 0b00110101 << 24;
				
				if ((R >= 0) && (R < 16)) {
					num = num | R << 20;
				} else {
					errorLabel.setText("ERROR -- illegal value for R");
					binaryInstruction.setText("");
					hexInstruction.setText("");
				}
				
				if (assemblerString.substring((secondPos + 1),(secondPos  + 2)).equals("*")) {
					num = num | (0b10000000000000000000000000000000);
				}
				
				if (assemblerString.substring((secondPos + 1), (secondPos + 2)).equals("*")) {
					
					num = num | (0b10000000000000000000000000000000);
					
					String secondHalf = assemblerString.substring(assemblerString.indexOf("*") + 1);
					
					if (secondHalf.indexOf(",") == -1) {
											
						String d = secondHalf;
						int D = (int) Long.parseLong(d);
						
						if ((D >= 0) && (D <= 131071)) {
							
							D = D & (0b00000000000000011111111111111111);
							num = num | D;
							
							String sB  = displayIntAsBinary(num);
							String sH = displayIntAsHex(num);
							
							binaryInstruction.setText(sB);
							hexInstruction.setText(sH);
							
						} else {
							errorLabel.setText("ERROR -- invalid value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
						}
						
					} else {
						
						String d = secondHalf.substring(0, secondHalf.indexOf(","));
						int D = (int) Long.parseLong(d);

						int X;
						String x = secondHalf.substring(secondHalf.indexOf(",") + 1);
						if(x.equals("")) {
							X = 0;
						} else {
							X = (int) Long.parseLong(x);
						}
						
						if ((D >= 0) && (D <= 131071)) {
							
							D = D & (0b00000000000000011111111111111111);
							num = num | D;
							
							if ((X >= 0) && (X <=7)) {
								
								X = X << 17;
								num = num | X;
								
								String sB  = displayIntAsBinary(num);
								String sH = displayIntAsHex(num);
								
								binaryInstruction.setText(sB);
								hexInstruction.setText(sH);
							
							} else {
								errorLabel.setText("ERROR -- invalid value for X");
								binaryInstruction.setText("");
								hexInstruction.setText("");
							}
							
						} else {
							errorLabel.setText("ERROR -- invalid value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
						}
						
					}
					
									
				} else {
					
					String secondHalf = assemblerString.substring(assemblerString.indexOf(" ") + 1);
					
					if(secondHalf.indexOf(",") == -1) {
											
						String d = secondHalf;
						int D = (int) Long.parseLong(d);
						
						if ((D >= 0) && (D <= 131071)) {
							
							D = D & (0b00000000000000011111111111111111);
							num = num | D;
							
							String sB  = displayIntAsBinary(num);
							String sH = displayIntAsHex(num);
							
							binaryInstruction.setText(sB);
							hexInstruction.setText(sH);
							
						} else {
							errorLabel.setText("ERROR -- invalid value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
						}
						
					} else {
											
						String d = secondHalf.substring(0, secondHalf.indexOf(","));
						int D = (int) Long.parseLong(d);
						
						int X;
						String x = secondHalf.substring(secondHalf.indexOf(",") + 1);
						if(x.equals("")) {
							X = 0;
						} else {
							X = (int) Long.parseLong(x);
						}
						
						if ((D >= 0) && (D <= 131071)) {
							
							D = D & (0b00000000000000011111111111111111);
							num = num | D;
							
							if ((X >= 0) && (X <= 7)) {
								
								X = X << 17;
								num = num | X;
								
								String sB  = displayIntAsBinary(num);
								String sH = displayIntAsHex(num);
								
								binaryInstruction.setText(sB);
								hexInstruction.setText(sH);
								
							} else {
								errorLabel.setText("ERROR -- invalid value for X");
								binaryInstruction.setText("");
								hexInstruction.setText("");
							}
							
						} else {
							errorLabel.setText("ERROR -- invalid value for D");
							binaryInstruction.setText("");
							hexInstruction.setText("");
						}
						
					}
					
				}
				
			} else {
				
				errorLabel.setText("ERROR -- invalid mnemonic");
				binaryInstruction.setText("");
				hexInstruction.setText("");
				
			}
			
		}
		
	}

	void decodeBin() {
		
		String binaryString = binaryInstruction.getText();
		
		int num = 0;
		
// Checking to see if the binary number is 32 bits long
		
		if((binaryString.length() > 32) || (binaryString.length() < 32)) {
			
			errorLabel.setText("ERROR -- binary number must be 32 bits");
			assemblerInstruction.setText("");
			hexInstruction.setText("");
			
		} else {
			
// Checking to see if there are any illegal bit values in the binary string
			
			boolean validBinaryNum = false;
			
			for(int i = 0; i < binaryString.length(); i++) {
				char bit = binaryString.charAt(i);
				if ((bit == '0') || (bit == '1')) {
					validBinaryNum = true;
				} else {
					validBinaryNum = false;
					break;
				}
			}
			
			if (validBinaryNum) {
		
				try {
					num = (int) Long.parseLong(binaryString,2);
				} catch (Exception e) {
					errorLabel.setText("ERROR -- illegal binary number");
					assemblerInstruction.setText("");
					hexInstruction.setText("");
				}
						
				int instruction = num >> 24;
													
		// Variable instruction looks at the first 8 bits to see what 
		// assembly instruction and it belongs in with the if-statements

				if (instruction == 0b00100010) {
					
		// Set the start of the string to the correct assembly command
					
					String assemblyInstruction = "LI";

		// Isolates the bits for R and converts it to a string for the assembly language
					
					int R = (num >> 20) & (0b000000001111);
					String assemblyR = Integer.toString(R);
								
		// Isolates the bits for V and converts it to a string for the assembly language

					int V = (num & 0b00000000000011111111111111111111);
					String assemblyV = Integer.toString(V);
					
		// Using the toString() method allows for the variables R and V to have String 
		// values in order to combine Strings together for the assembly language
					
					String sA;
					
		// If-statement looks to see whether the value for V is positive or negative
		// by checking to see if the highest ordered bit is a 0 or a one for the value of V
					
					if (V > 524287) {
						
		// uses 2's compliment to change it to the correct negative number
						
						V = (~V) + 1;
						V = V & 0b00000000000011111111111111111111;
						
						assemblyV = Integer.toString(V);
						
		// Once it is captured for the correct assembly language variable
		// 2's compliment must be used again to get the correct hex value to add to the String
						
						V = (~V) + 1;
						V = V & 0b00000000000011111111111111111111;
						
						int hexAssemblyInt = (instruction << 24) + (R << 20) + V;
						
						String hexFinalAssembly = displayIntAsHex(hexAssemblyInt);
						
						sA = (assemblyInstruction + "," + assemblyR + " -" + assemblyV);
						String sH = (hexFinalAssembly);
						
						assemblerInstruction.setText(sA);
						hexInstruction.setText(sH);
						
		// Since V is positive in this case, this means that V can stay as is and added into
		// assembly language and hex language
						
					} else {
										
						int hexInt = (instruction << 24) + (R << 20) + V;
						String hexFinalAssembly = displayIntAsHex(hexInt);
						
						String sH = (hexFinalAssembly);
						
						hexInstruction.setText(sH);
						
						sA = (assemblyInstruction + "," + assemblyR + " " + assemblyV);
						assemblerInstruction.setText(sA);
					}
					
				} else if (instruction == 0b00110010) {
								
					String instructionAssembly = "LW";

		// Using shift-operations and unary-operations to determine variables within the binary number
					
		// Using the toString() method allows for the variables to have String values in order to 
		// combine Strings together for the assembly language
					
					int R = (num >> 20) & (0b000000001111);
					String assemblyR = Integer.toString(R);
					
					int X = (num >> 17) & (0b000000000000111);
					String assemblyX = Integer.toString(X);
					
					int D = num & (0b00000000000000011111111111111111);
					String assemblyD = Integer.toString(D);
					
		// If-statement to see if X value exists, which in turn would change
		// the way the assembly language would print out, but not hex
					
					if (X == 0) {
						
						String sA = (instructionAssembly + "," + assemblyR + " " + assemblyD);
						
						int hexInt = (instruction << 24) + (R << 20) + D;
						String sH = displayIntAsHex(hexInt);
						
						assemblerInstruction.setText(sA);
						hexInstruction.setText(sH);
						
					} else {		
						
						String sA = (instructionAssembly + "," + assemblyR + " " + assemblyD + "," + assemblyX);
						
						int hexInt = (instruction << 24) + (R << 20) + (X << 17) + D;
						String sH = displayIntAsHex(hexInt);
						
						assemblerInstruction.setText(sA);
						hexInstruction.setText(sH);
						
					}
					
				} else if (instruction == -78) {
					
		// 1 in left-most bit indicates an "*" exists later in the code even with same assembly instruction
					
					String instructionAssembly = "LW";
					
		// Using shift-operations and unary-operations to determine variables within the binary number
					
		// Using the toString() method allows for the variables to have String values in order to 
		// combine Strings together for the assembly language

					int R = (num >> 20) & (0b000000001111);
					String assemblyR = Integer.toString(R);
					
					int X = (num >> 17) & (0b000000000000111);
					String assemblyX = Integer.toString(X);
					
					int D = num & (0b00000000000000011111111111111111);
					String assemblyD = Integer.toString(D);
					
		// If-statement to see if X value exists, which in turn would change
		// the way the assembly language would print out, but not hex
					
					if (X == 0) {
						
						String sA = (instructionAssembly + "," + assemblyR + " *" + assemblyD);
						
						int hexInt = (instruction << 24) + (R << 20) + D;
						String sH = displayIntAsHex(hexInt);
						
						assemblerInstruction.setText(sA);
						hexInstruction.setText(sH);
						
					} else {		
						
						String sA = (instructionAssembly + "," + assemblyR + " *" + assemblyD + "," + assemblyX);
						
						int hexInt = (instruction << 24) + (R << 20) + (X << 17) + D;
						String sH = displayIntAsHex(hexInt);
						
						assemblerInstruction.setText(sA);
						hexInstruction.setText(sH);
						
					}
					
		// ******************************************************************************
		// IT IS TO BE NOTED THAT THE SAME LOGIC USE FOR LW IS USED IN AW AND STW
		// THEREFORE I WILL NOT BE COPYING AND PASTING THE COMMENTS I MADE AS IT WOULD BE REDUNDANT
		// THIS INCLUDES CODE USED FOR THE INCLUSION OF THE "*" DUE TO A 1 IN THE HIGHEST BIT
		// ******************************************************************************
					
				} else if (instruction == 0b00110000) {
					
					String instructionAssembly = "AW";
					
					int R = (num >> 20) & (0b000000001111);
					String assemblyR = Integer.toString(R);
					
					int X = (num >> 17) & (0b000000000000111);
					String assemblyX = Integer.toString(X);
					
					int D = num & (0b00000000000000011111111111111111);
					String assemblyD = Integer.toString(D);
					
					if (X == 0) {
						
						String sA = (instructionAssembly + "," + assemblyR + " " + assemblyD);
						
						int hexInt = (instruction << 24) + (R << 20) + D;
						String sH = displayIntAsHex(hexInt);
						
						assemblerInstruction.setText(sA);
						hexInstruction.setText(sH);
						
					} else {		
						
						String sA = (instructionAssembly + "," + assemblyR + " " + assemblyD + "," + assemblyX);
						
						int hexInt = (instruction << 24) + (R << 20) + (X << 17) + D;
						String sH = displayIntAsHex(hexInt);
						
						assemblerInstruction.setText(sA);
						hexInstruction.setText(sH);
						
					}
					
				} else if (instruction == -80) {
								
					String instructionAssembly = "AW";
					
					int R = (num >> 20) & (0b000000001111);
					String assemblyR = Integer.toString(R);
					
					int X = (num >> 17) & (0b000000000000111);
					String assemblyX = Integer.toString(X);
					
					int D = num & (0b00000000000000011111111111111111);
					String assemblyD = Integer.toString(D);
					
					if (X == 0) {
						
						String sA = (instructionAssembly + "," + assemblyR + " *" + assemblyD);
						
						int hexInt = (instruction << 24) + (R << 20) + D;
						String sH = displayIntAsHex(hexInt);
						
						assemblerInstruction.setText(sA);
						hexInstruction.setText(sH);
						
					} else {		
						
						String sA = (instructionAssembly + "," + assemblyR + " *" + assemblyD + "," + assemblyX);
						
						int hexInt = (instruction << 24) + (R << 20) + (X << 17) + D;
						String sH = displayIntAsHex(hexInt);
						
						assemblerInstruction.setText(sA);
						hexInstruction.setText(sH);
						
					}
					
				} else if (instruction == 0b00110101) {
					
					String instructionAssembly = "STW";
					
					int R = (num >> 20) & (0b000000001111);
					String assemblyR = Integer.toString(R);
					
					int X = (num >> 17) & (0b000000000000111);
					String assemblyX = Integer.toString(X);
					
					int D = num & (0b00000000000000011111111111111111);
					String assemblyD = Integer.toString(D);
					
					if (X == 0) {
						
						String sA = (instructionAssembly + "," + assemblyR + " " + assemblyD);
						
						int hexInt = (instruction << 24) + (R << 20) + D;
						String sH = displayIntAsHex(hexInt);
						
						assemblerInstruction.setText(sA);
						hexInstruction.setText(sH);
						
					} else {		
						
						String sA = (instructionAssembly + "," + assemblyR + " " + assemblyD + "," + assemblyX);
						
						int hexInt = (instruction << 24) + (R << 20) + (X << 17) + D;
						String sH = displayIntAsHex(hexInt);
						
						assemblerInstruction.setText(sA);
						hexInstruction.setText(sH);
						
					}

				} else if (instruction == -75) {
					
					String instructionAssembly = "STW";
					
					int R = (num >> 20) & (0b000000001111);
					String assemblyR = Integer.toString(R);
					
					int X = (num >> 17) & (0b000000000000111);
					String assemblyX = Integer.toString(X);
					
					int D = num & (0b00000000000000011111111111111111);
					String assemblyD = Integer.toString(D);
					
					if (X == 0) {
						
						String sA = (instructionAssembly + "," + assemblyR + " *" + assemblyD);
						
						int hexInt = (instruction << 24) + (R << 20) + D;
						String sH = displayIntAsHex(hexInt);
						
						assemblerInstruction.setText(sA);
						hexInstruction.setText(sH);
						
					} else {		
						
						String sA = (instructionAssembly + "," + assemblyR + " *" + assemblyD + "," + assemblyX);
						
						int hexInt = (instruction << 24) + (R << 20) + (X << 17) + D;
						String sH = displayIntAsHex(hexInt);
						
						assemblerInstruction.setText(sA);
						hexInstruction.setText(sH);
						
					}
					
				} else {
					
					errorLabel.setText("ERROR -- illegal op code");
					hexInstruction.setText(displayIntAsHex(num));
					assemblerInstruction.setText("");
					
				}
				
			
				
			} else {
				errorLabel.setText("ERROR -- illegal binary number");
				assemblerInstruction.setText("");
				hexInstruction.setText("");
			}
			
		}
	}
	
	void decodeHex() {
		
		String hexString = hexInstruction.getText();
		
		int num = 0;
		
// Check to see if the hex string is longer than 8 digits
		
		if ((hexString.length() > 8) || (hexString.length() < 8)) {
			
			errorLabel.setText("ERROR -- hex must be 8 digits");
			assemblerInstruction.setText("");
			binaryInstruction.setText("");
			
		} else {
			
// Checking to see if there are any invalid characters that exist inside of the string
			
			boolean validHexNum = false;
			
			for(int i = 0; i < hexString.length(); i++) {
				char digit = hexString.charAt(i);
				if (
						(digit == '0') || (digit == '1') || (digit == '3') ||
						(digit == '4') || (digit == '5') || (digit == '6') ||
						(digit == '7') || (digit == '8') || (digit == '9') ||
						(digit == 'A') || (digit == 'B') || (digit == 'C') ||
						(digit == 'D') || (digit == 'E') || (digit == 'F')
						
						) {
				
				validHexNum = true;
					
				} else {
					validHexNum = false;
					break;
				}
			
			}
			
			if (validHexNum) {
				
				try {
					num = (int) Long.parseLong(hexString,16);
				} catch (Exception e) {
					errorLabel.setText("ERROR -- illegal hex number");
					assemblerInstruction.setText("");
					binaryInstruction.setText("");
				}

		// Variable instruction looks at the first 2 hex digits to see what 
		// assembly instruction and it belongs in with the if-statements
				
				int instruction = num >> 24;

				if (instruction == 0x22) {
					
		// Set the start of the string to the correct assembly command

					String assemblyInstruction = "LI";

		// Isolates the hex digit for R and convert it to a string for the assembly language
					
					int R = (num >> 20) & (0x00F);
					String assemblyR = Integer.toString(R);
								
		// Isolates the hex digits for V and convert it to a string for the assembly language

					int V = (num & 0x000FFFFF);
					String assemblyV = Integer.toString(V);
					
		// Using the toString() method allows for the variables R and V to have String 
		// values in order to combine Strings together for the assembly language
					
					String sA;
					
		// If-statement looks to see whether the value for V is positive or negative
		// by checking to see if the highest ordered bit is a 1 for the value of V
					
					if (V > 524287) {
						
		// uses 15's compliment to change it to the correct negative number
						
						V = (~V) + 1;
						V = V & 0x000FFFFF;
						
						assemblyV = Integer.toString(V);
						
		// Once it is captured for the correct assembly language variable
		// 15's compliment must be used again to get the correct hex value to add to the String
						
						V = (~V) + 1;
						V = V & 0x000FFFFF;
						
						int binaryAssemblyInt = (instruction << 24) + (R << 20) + V;
						String binaryFinalAssembly = displayIntAsBinary(binaryAssemblyInt);
						
						sA = (assemblyInstruction + "," + assemblyR + " -" + assemblyV);
						String sB = (binaryFinalAssembly);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
						
		// Since V is positive in this case, this means that V can stay as is and added into
		// assembly language and hex language
						
					} else {
										
						int binaryInt = (instruction << 24) + (R << 20) + V;
						String binaryFinalAssembly = displayIntAsBinary(binaryInt);
										
						sA = (assemblyInstruction + "," + assemblyR + " " + assemblyV);
						String sB = (binaryFinalAssembly);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
					}
					
				} else if (instruction == 0x32) {
					
					String instructionAssembly = "LW";
					
		// Using shift-operations and unary-operations to determine variables within the binary number
					
		// Using the toString() method allows for the variables to have String values in order to 
		// combine Strings together for the assembly language
					
					int R = (num >> 20) & (0x00F);
					String assemblyR = Integer.toString(R);
								
					int X = (num >> 17) & (0b000000000000111);
					String assemblyX = Integer.toString(X);
								
					int D = num & (0b00000011111111111111111);
					String assemblyD = Integer.toString(D);
					
		// If-statement to see if X value exists, which in turn would change
		// the way the assembly language would print out, but not binary
					
					if (X == 0) {
						
						String sA = (instructionAssembly + "," + assemblyR + " " + assemblyD);
						
						int binaryInt = (instruction << 24) + (R << 20) + D;
						String sB = displayIntAsBinary(binaryInt);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
						
					} else {		
						
						String sA = (instructionAssembly + "," + assemblyR + " " + assemblyD + "," + assemblyX);
						
						int binaryInt = (instruction << 24) + (R << 20) + (X << 17) + D;
						String sB = displayIntAsBinary(binaryInt);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
						
					}
					
				} else if (instruction == -78) {
					
		// 1 in left-most bit indicates an "*" exists later in the code even with same assembly instruction

					String instructionAssembly = "LW";
					
		// Using shift-operations and unary-operations to determine variables within the binary number

		// Using the toString() method allows for the variables to have String values in order to 
		// combine Strings together for the assembly language
					
					int R = (num >> 20) & (0b000000001111);
					String assemblyR = Integer.toString(R);
					
					int X = (num >> 17) & (0b000000000000111);
					String assemblyX = Integer.toString(X);
					
					int D = num & (0b00000000000000011111111111111111);
					String assemblyD = Integer.toString(D);
					
		// If-statement to see if X value exists, which in turn would change
		// the way the assembly language would print out, but not binary
					
					if (X == 0) {
						
						String sA = (instructionAssembly + "," + assemblyR + " *" + assemblyD);
						
						int binaryInt = (instruction << 24) + (R << 20) + D;
						String sB = displayIntAsBinary(binaryInt);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
						
					} else {		
						
						String sA = (instructionAssembly + "," + assemblyR + " *" + assemblyD + "," + assemblyX);
						
						int binaryInt = (instruction << 24) + (R << 20) + (X << 17) + D;
						String sB = displayIntAsBinary(binaryInt);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
						
					}
					
		// ******************************************************************************
		// IT IS TO BE NOTED THAT THE SAME LOGIC USE FOR LW IS USED IN AW AND STW
		// THEREFORE I WILL NOT BE COPYING AND PASTING THE COMMENTS I MADE AS IT WOULD BE REDUNDANT
		// THIS INCLUDES CODE USED FOR THE INCLUSION OF THE "*" DUE TO A 1 IN THE HIGHEST BIT
		// ******************************************************************************
					
				} else if (instruction == 0x30) {
					
					String instructionAssembly = "AW";
					
					int R = (num >> 20) & (0x00F);
					String assemblyR = Integer.toString(R);
					
					int X = (num >> 17) & (0b000000000000111);
					String assemblyX = Integer.toString(X);
					
					int D = num & (0b00000011111111111111111);
					String assemblyD = Integer.toString(D);
					
					if (X == 0) {
						
						String sA = (instructionAssembly + "," + assemblyR + " " + assemblyD);
						
						int binaryInt = (instruction << 24) + (R << 20) + D;
						String sB = displayIntAsBinary(binaryInt);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
						
					} else {		
						
						String sA = (instructionAssembly + "," + assemblyR + " " + assemblyD + "," + assemblyX);
						
						int binaryInt = (instruction << 24) + (R << 20) + (X << 17) + D;
						String sB = displayIntAsBinary(binaryInt);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
						
					}
					
				} else if (instruction == -80) {
					
					String instructionAssembly = "AW";
					
					int R = (num >> 20) & (0b000000001111);
					String assemblyR = Integer.toString(R);
					
					int X = (num >> 17) & (0b000000000000111);
					String assemblyX = Integer.toString(X);
					
					int D = num & (0b00000000000000011111111111111111);
					String assemblyD = Integer.toString(D);
					
					if (X == 0) {
						
						String sA = (instructionAssembly + "," + assemblyR + " *" + assemblyD);
						
						int binaryInt = (instruction << 24) + (R << 20) + D;
						String sB = displayIntAsBinary(binaryInt);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
						
					} else {		
						
						String sA = (instructionAssembly + "," + assemblyR + " *" + assemblyD + "," + assemblyX);
						
						int binaryInt = (instruction << 24) + (R << 20) + (X << 17) + D;
						String sB = displayIntAsBinary(binaryInt);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
						
					}

				} else if (instruction == 0x35) {
					
					String instructionAssembly = "STW";
					
					int R = (num >> 20) & (0x00F);
					String assemblyR = Integer.toString(R);
					
					int X = (num >> 17) & (0b000000000000111);
					String assemblyX = Integer.toString(X);
					
					int D = num & (0b00000011111111111111111);
					String assemblyD = Integer.toString(D);
					
					if (X == 0) {
						
						String sA = (instructionAssembly + "," + assemblyR + " " + assemblyD);
						
						int binaryInt = (instruction << 24) + (R << 20) + D;
						String sB = displayIntAsBinary(binaryInt);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
						
					} else {		
						
						String sA = (instructionAssembly + "," + assemblyR + " " + assemblyD + "," + assemblyX);
						
						int binaryInt = (instruction << 24) + (R << 20) + (X << 17) + D;
						String sB = displayIntAsBinary(binaryInt);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
						
					}
					
				} else if (instruction == -75) {
					
					String instructionAssembly = "STW";
					
					int R = (num >> 20) & (0b000000001111);
					String assemblyR = Integer.toString(R);
					
					int X = (num >> 17) & (0b000000000000111);
					String assemblyX = Integer.toString(X);
					
					int D = num & (0b00000000000000011111111111111111);
					String assemblyD = Integer.toString(D);
					
					if (X == 0) {
						
						String sA = (instructionAssembly + "," + assemblyR + " *" + assemblyD);
						
						int binaryInt = (instruction << 24) + (R << 20) + D;
						String sB = displayIntAsBinary(binaryInt);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
						
					} else {		
						
						String sA = (instructionAssembly + "," + assemblyR + " *" + assemblyD + "," + assemblyX);
						
						int binaryInt = (instruction << 24) + (R << 20) + (X << 17) + D;
						String sB = displayIntAsBinary(binaryInt);
						
						assemblerInstruction.setText(sA);
						binaryInstruction.setText(sB);
						
					}
					
				} else {
					
					binaryInstruction.setText(displayIntAsBinary(num));
					errorLabel.setText("ERROR -- illegal op code");
					assemblerInstruction.setText("");

				}
				
			} else {
				
				errorLabel.setText("ERROR -- illegal hex number");
				assemblerInstruction.setText("");
				binaryInstruction.setText("");
				
			}
			
		}
				
	}

}
