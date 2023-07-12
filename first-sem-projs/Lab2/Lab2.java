import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Lab2 extends JFrame implements ActionListener {
	JButton open = new JButton("Next Program");
	JTextArea result = new JTextArea(20,40);
	JLabel errors = new JLabel();
	JScrollPane scroller = new JScrollPane();
	
	public Lab2() {
		setLayout(new java.awt.FlowLayout());
		setSize(500,430);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(open); open.addActionListener(this);
		scroller.getViewport().add(result);
		add(scroller);
		add(errors);
	}
	
	public void actionPerformed(ActionEvent evt) {
		result.setText("");	//clear TextArea for next program
		errors.setText("");
		processProgram();
	}
	
	public static void main(String[] args) {
		Lab2 display = new Lab2();
		display.setVisible(true);
	}
	
	String getFileName() {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile().getPath();
		else
			return null;
	}
	
/************************************************************************/
/* Put your implementation of the processProgram method here.           */
/* Use the getFileName method to allow the user to select a program.    */
/* Then simulate the execution of that program.                         */
/* You may add any other methods that you think are appropriate.        */
/* However, you should not change anything in the code that I have      */
/* written.                                                             */
/************************************************************************/
	
	void processProgram() {
		
		String fileName = getFileName();
		ArrayList<String> listOfLines = new ArrayList<String>();
		
		ArrayList<String> variableList = new ArrayList<String>();
		ArrayList<Double> valueList = new ArrayList<Double>();
		
		if (fileName != null) 
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line;
			
			while ((line = in.readLine()) != null) {
				line = line.trim();
				listOfLines.add(line);
			}
			in.close();
		} catch (IOException e) {
			errors.setText("ERRORS: " + e);
		}
		
		int i = 0;
		while(i < listOfLines.size()) {
			
			String line = listOfLines.get(i);
			i++;
			
//Immediately checks if last line is keyword END		
			int lastLineNum = listOfLines.size() - 1;
			if (!(listOfLines.get(lastLineNum).equals("END"))) {
				errors.setText("END is not the last statement");
				return;
			}
			
// Gets the first token
			
			StringTokenizer startTok = new StringTokenizer(line, " ");	
			String firstToken = "";
			
			try {
				firstToken = startTok.nextToken();
			} catch (Exception e) {
				errors.setText("In line " + i + " illegal statement");
			}

// Gets the start of the line to see whether it is a simple statement, 
// conditional statement, or end of program
						
			if (firstToken.equals("IF")) {
				
				StringTokenizer tok = new StringTokenizer(line, " ");
				
// Order of keywords that needs to be
				String ifKW = "";
				String var = "";
				double varNum = 0.0;
				String isKW = "";
				String val = "";
				double valNum = 0.0;
				String thenKW = "";
				String simpleStatementKW = "";
				boolean cond = false;
				
				try {
					ifKW = tok.nextToken();
				} catch (Exception e) {
					errors.setText("In line " + i + " illegal statement");
					return;
				}

// Checks to make sure IF keyword exists and is not some other word
				if(!ifKW.equals("IF")) {
					errors.setText("In line " + i + " keyword IF is missing");
					return;
				}
				
				try {
					var = tok.nextToken();
				} catch (Exception e) {
					errors.setText("In line " + i + " illegal statement");
					return;
				}
				
// Checks to see if the variable that is trying to be used exists in memory
// If it exists in memory the value of the variable is saved
				if (variableList.indexOf(var) == -1) {
					errors.setText("In line " + i + " \"" + var + "\" is undefined");
					return;
				} else {
					int pos = variableList.indexOf(var);
					varNum = valueList.get(pos);
				}
				
				try {
					isKW = tok.nextToken();
				} catch (Exception e) {
					errors.setText("In line " + i + " illegal statement");
					return;
				}
				
// Checks to make sure IS keyword exists and is not some other word
				if(!isKW.equals("IS")) {
					errors.setText("In line " + i + " keyword IS is missing");
					return;
				}
				
				try {
					val = tok.nextToken();
				} catch (Exception e) {
					errors.setText("In line " + i + " illegal statement");
					return;
				}
				
				
// Gets the value of the variable or the number that is in the conditional statement
				if (isNumeric(val)) {
					
					valNum = Double.parseDouble(val);
					
				} else {
					
					if (variableList.indexOf(val) == -1) {
						errors.setText("In line " + i + " \"" + var + "\" is undefined");
						return;
					} else {
						int pos = variableList.indexOf(val);
						valNum = valueList.get(pos);
					}
					
				}
				
				try {
					thenKW = tok.nextToken();
				} catch (Exception e) {
					errors.setText("In line " + i + " illegal statement");
					return;
				}
	
// Checks to make sure IS keyword exists and is not some other word
				if (!thenKW.equals("THEN")) {
					errors.setText("In line " + i + " keyword THEN is missing");
					return;
				}
				
				try {
					simpleStatementKW = tok.nextToken();
				} catch (Exception e) {
					errors.setText("In line " + i + " illegal statement");
				}
				
				if (varNum == valNum) {
					cond = true;
				} else {
					cond = false;
				}
				
				
// If variable is equal to the value or the variable than enter the simple statement
				
				if (cond) {
					
// Checks to see what the simple statement keyword is which 
// can only be GOTO, PRINT, or variable = expression
									
					int posOfSS = line.indexOf("THEN") + 5;
					String restOfSS = line.substring(posOfSS);
					
					if(restOfSS.contains("GOTO")) {
						
						StringTokenizer gotoTok = new StringTokenizer(restOfSS, " ");
						String gotoKW = gotoTok.nextToken();
						
						// GOTO can only have two tokens and nothing more
						
						if(gotoTok.countTokens() > 1) {
							errors.setText("In line " + i + " too many tokens for GOTO");
							return;
						}
						
						int lineNum = 0;
						
						try {
							lineNum = Integer.parseInt(restOfSS.substring(5));
						} catch (Exception e) {
							errors.setText("In Line " + i + " illegal statement");
							return;
						}
						
// GOTO cannot be more than the lines in the program or less than the first line

						
						if ((lineNum > listOfLines.size()) || (lineNum < 1)) {
							errors.setText("In line " + i + " invalid GOTO");
							return;
						}
						
						i = lineNum - 1;
						
					} else if (restOfSS.contains("PRINT")) {
						
						StringTokenizer printTok = new StringTokenizer(restOfSS, " ");
						String printKW = printTok.nextToken();
						String varName = "";
																		
						try {
							varName = printTok.nextToken();
						} catch (Exception e) {
							errors.setText("In line " + i + " illegal statement");
							return;
						}
						
// Guarantees that there is exactly one token being printed
						if (printTok.countTokens() > 0) {
							errors.setText("In Line " + i + " too many tokens for PRINT");
							return;
						} 
							
						if (printTok.countTokens() == 0) {
							
// Checks if the variable being printed even exists in m
							if (variableList.indexOf(varName) == -1) {
								errors.setText("In Line " + i + " \"" + varName + "\" is undefined");
								return;
							} else {
								
								int pos = variableList.indexOf(varName);
								double valOfVar = valueList.get(pos);
								
								String res = String.format("%.2f", valOfVar);
								
								result.append(res + "\n");
								
							} 
							
						} else {
							errors.setText("In Line " + i + " illegal statement");
							return;
						}
						
					} else if (restOfSS.indexOf("=") != -1) {
						
						String varName = "";
						String assignOp = "";
						String checkAssign = "";
						double num1 = 0.0;
						double result = 0.0;
						
						StringTokenizer assignmentTok = new StringTokenizer(restOfSS, " ");

						try {
							varName = assignmentTok.nextToken();
						} catch (Exception e) {
							errors.setText("In line " + i + " illegal statement");
							return;
						}
						
		// First token checks if it is a valid name for a variable
						
						if (isNumeric(varName)) {
							errors.setText("In line " + i + " illegal variable");
							return;
						}
						
						try {
							assignOp = assignmentTok.nextToken();
						} catch (Exception e) {
							errors.setText("In line " + i + " illegal statement");
							return;
						}
						
		// Second token checks if the assignment operator is there
						
						if (!assignOp.equals("=")) {
							errors.setText("In line " + i + " missing assignment operator");
							return;
						}
						
		// Makes sure that there exists no parenthesis in th expression
						int posOfAssign = line.indexOf("=");
						String restOfExpression = line.substring(posOfAssign + 1);
						
						if (restOfExpression.contains(")") || restOfExpression.contains(")")) {
							errors.setText("In line " + i + " illegal statement");
							return;
						}
						
						try {
							checkAssign = assignmentTok.nextToken();
						} catch (Exception e) {
							errors.setText("In line " + i + " illegal statement");
							return;
						}
						
		// Variable declared does not exist in list	
						if(variableList.indexOf(varName) == -1) {
							
		// checkAssign can be either an existing variable, or a number
							if (isNumeric(checkAssign)) {
								
								try {
									num1 = Double.parseDouble(checkAssign);
								} catch (Exception e) {
									errors.setText("In line " + i + " illegal statement");
									return;
								}
								
								result = num1;
							
		// Means variable could exist as a value already
								
							} else {
								
								if (variableList.indexOf(checkAssign) == -1) {
									errors.setText("In line " + i + " \"" + checkAssign + "\" is undefined");
									return;
								} else {
									int pos = variableList.indexOf(checkAssign);
									result = valueList.get(pos);
								}
								
							}
							
							if (assignmentTok.hasMoreTokens())
							while(assignmentTok.hasMoreTokens()) {
								
								String op = "";
								String checkNum = "";
								double tempNum = 0.0;
								
								try {
									op = tok.nextToken();
								} catch (Exception e) {
									errors.setText("In line " + i + " illegal statement");
									return;
								}

		//Check to see if the operation is one of the valid operations
								if (!(op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/"))) {
									errors.setText("In line " + i + " illegal operation");
									return;
								}
								
								try {
									checkNum = tok.nextToken();
								} catch (Exception e) {
									errors.setText("In line " + i + " illegal statement");
									return;
								}
								
								if (isNumeric(checkNum)) {
									
									try {
										tempNum = Double.parseDouble(checkNum);
									} catch (Exception e) {
										errors.setText("In line " + i + " illegal statement");
										return;
									}
									
									if (op.equals("+")) {
										result = result + tempNum;
									} else if (op.equals("-")) { 
										result = result - tempNum;
									} else if (op.equals("*")) {
										result = result * tempNum;
									} else if (op.equals("/")) {
										result = result / tempNum;
									} else {
										errors.setText("In line " + i + " illegal operation");
										return;
									}
									
								} else {
									
		// Check if the next variable exists in memory in the variable list	
									if (variableList.indexOf(checkNum) == -1) {
										errors.setText("In line " + i + " \"" + checkNum + "\" is undefined");
										return;
									} else {
										
		// Takes the variable from memory and uses the operation to add it to the final result	
										int pos = variableList.indexOf(checkNum);
										
										if (op.equals("+")) {
											result = result + valueList.get(pos);
										} else if (op.equals("-")) { 
											result = result - valueList.get(pos);
										} else if (op.equals("*")) {
											result = result * valueList.get(pos);
										} else if (op.equals("/")) {
											result = result / valueList.get(pos);
										} else {
											errors.setText("In line " + i + " illegal operation");
											return;
										}
									}				
									
								}
													
							}
							
		// Adds the variable name and the value to memory
							
							variableList.add(varName);
							valueList.add(result);
							
		// The value for a variable has already been defined, and is being rewritten
						} else {
							
							int pos = variableList.indexOf(varName);
							double oldVal = valueList.get(pos);
							double tempVal = 0.0;
							double newVal = 0.0;
							
		// checkAssign can be either an existing variable, or a number
							
							if (isNumeric(checkAssign)) {
								
								try {
									tempVal = Double.parseDouble(checkAssign);
								} catch (Exception e) {
									errors.setText("In line " + i + " illegal statement");
									return;
								}
								
								newVal = tempVal;
							
							
		// Means variable could exist as a value already		
							} else {
								
								if (variableList.indexOf(checkAssign) == -1) {
									errors.setText("In line " + i + " \"" + checkAssign + "\" is undefined");
									return;
								} else {
									pos = variableList.indexOf(varName);
									newVal = valueList.get(pos);
								}
								
							}
							
							if (assignmentTok.hasMoreTokens())
							while(assignmentTok.hasMoreTokens()) {
									
								String op = "";
								String checkNum = "";
								double tempNum = 0.0;
								
								try {
									op = tok.nextToken();
								} catch (Exception e) {
									errors.setText("In line " + i + " illegal statement");
									return;
								}
								

		//Check to see if the operation is one of the valid operations
								if (!(op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/"))) {
									errors.setText("In line " + i + " illegal operation");
									return;
								}
								
								try {
									checkNum = tok.nextToken();
								} catch (Exception e) {
									errors.setText("In line " + i + " illegal statement");
									return;
								}
								
								if (isNumeric(checkNum)) {
									
									try {
										tempNum = Double.parseDouble(checkNum);
									} catch (Exception e) {
										errors.setText("In line " + i + " illegal statement");
										return;
									}
									
									if (op.equals("+")) {
										newVal = newVal + tempNum;
									} else if (op.equals("-")) { 
										newVal = newVal - tempNum;
									} else if (op.equals("*")) {
										newVal = newVal * tempNum;
									} else if (op.equals("/")) {
										newVal = newVal / tempNum;
									} else {
										errors.setText("In line " + i + " illegal operation");
										return;
									}
									
								} else {
									
		// Check if the next variable exists in memory in the variable list	
									if (variableList.indexOf(checkNum) == -1) {
										errors.setText("In line " + i + " \"" + checkNum + "\" is undefined");
										return;
									} else {
										
		// Takes the variable from memory and uses the operation to add it to the final result	
										int posOfOldVar = variableList.indexOf(checkNum);
										
										if (op.equals("+")) {
											newVal = newVal + valueList.get(posOfOldVar);
										} else if (op.equals("-")) { 
											newVal = newVal - valueList.get(posOfOldVar);
										} else if (op.equals("*")) {
											newVal = newVal * valueList.get(posOfOldVar);
										} else if (op.equals("/")) {
											newVal = newVal / valueList.get(posOfOldVar);
										} else {
											errors.setText("In line " + i + " illegal operation");
											return;
										}
									}				
									
								}
													
							}
							
							valueList.set(pos, newVal);
							
						}
						
					} else {
						errors.setText("In line " + i + " illegal statement");
						return;
					}
					
				}
				
			} else if (firstToken.equals("PRINT")) {
				
				StringTokenizer var = new StringTokenizer(line, " ");
				String printKW = var.nextToken();
				String varName = "";
				
				try {
					varName = var.nextToken();
				} catch (Exception e) {
					errors.setText("In line " + i + " illegal statement");
					return;
				}
				
// Guarantees that there is exactly one token being printed
				if (var.countTokens() > 0) {
					errors.setText("In Line " + i + " too many tokens for PRINT");
					return;
				} 
					
				if (var.countTokens() == 0) {
					
// Checks if the variable being printed even exists in m
					if (variableList.indexOf(varName) == -1) {
						errors.setText("In Line " + i + " \"" + varName + "\" is undefined");
						return;
					} else {
						
						int pos = variableList.indexOf(varName);
						double val = valueList.get(pos);
						
						String res = String.format("%.2f", val);
						
						result.append(res + "\n");
						
					} 
					
				} else {
					errors.setText("In Line " + i + " illegal statement");
					return;
				}
				
			} else if (firstToken.equals("GOTO")) {
				
				StringTokenizer tok = new StringTokenizer(line, " ");
				String gotoKW = tok.nextToken();
				
// GOTO can only have two tokens and nothing more
				
				if(tok.countTokens() > 2) {
					errors.setText("In line " + i + " too many tokens for GOTO");
					return;
				}
				
				int lineNum = 0;
				
				try {
					lineNum = Integer.parseInt(line.substring(5));
				} catch (Exception e) {
					errors.setText("In Line " + i + " illegal statement");
					return;
				}
				
// Number for GOTO cannot be larger than the amount of lines in code of less than the first line
				if ((lineNum > listOfLines.size()) || (lineNum < 1)) {
					errors.setText("In line " + i + " invalid GOTO");
					return;
				}
				
				i = lineNum - 1;
				
			} else if (firstToken.equals("END")){
				
				int firstEnd = i;
				
				if (firstEnd != listOfLines.size()) {
					errors.setText("There are statements following END");
					result.setText("");
					return;
				}
				
			} else if (line.indexOf("=") != -1) {
				
				String varName = "";
				String assignOp = "";
				String checkAssign = "";
				double num1 = 0.0;
				double result = 0.0;
				
				StringTokenizer tok = new StringTokenizer(line, " ");

				try {
					varName = tok.nextToken();
				} catch (Exception e) {
					errors.setText("In line " + i + " illegal statement");
					return;
				}
				
// First token checks if it is a valid name for a variable
				
				if (isNumeric(varName)) {
					errors.setText("In line " + i + " illegal variable");
					return;
				}
				
//  Checks the variable to make sure it does not contain parenthesis
				if(varName.contains(")") || varName.contains("(")) {
					errors.setText("In line " + i + " illegal variable");
					return;
				}
				
				try {
					assignOp = tok.nextToken();
				} catch (Exception e) {
					errors.setText("In line " + i + " illegal statement");
					return;
				}
				
// Second token checks if the assignment operator is there
				
				if (!assignOp.equals("=")) {
					errors.setText("In line " + i + " missing assignment operator");
					return;
				}
				
// Makes sure that there exists no parenthesis in th expression
				int posOfAssign = line.indexOf("=");
				String restOfExpression = line.substring(posOfAssign + 1);
				
				if (restOfExpression.contains(")") || restOfExpression.contains(")")) {
					errors.setText("In line " + i + " illegal statement");
					return;
				}
				
				try {
					checkAssign = tok.nextToken();
				} catch (Exception e) {
					errors.setText("In line " + i + " illegal statement");
					return;
				}
				
// Variable declared does not exist in list	
				if(variableList.indexOf(varName) == -1) {
					
// checkAssign can be either an existing variable, or a number
					if (isNumeric(checkAssign)) {
						
						try {
							num1 = Double.parseDouble(checkAssign);
						} catch (Exception e) {
							errors.setText("In line " + i + " illegal statement");
							return;
						}
						
						result = num1;
					
// Means variable could exist as a value already
						
					} else {
						
						if (variableList.indexOf(checkAssign) == -1) {
							errors.setText("In line " + i + " \"" + checkAssign + "\" is undefined");
							return;
						} else {
							int pos = variableList.indexOf(checkAssign);
							result = valueList.get(pos);
						}
						
					}
					
					if (tok.hasMoreTokens())
					while(tok.hasMoreTokens()) {
						
						String op = "";
						String checkNum = "";
						double tempNum = 0.0;
						
						try {
							op = tok.nextToken();
						} catch (Exception e) {
							errors.setText("In line " + i + " illegal statement");
							return;
						}

//Check to see if the operation is one of the valid operations
						if (!(op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/"))) {
							errors.setText("In line " + i + " illegal operation");
							return;
						}
						
						try {
							checkNum = tok.nextToken();
						} catch (Exception e) {
							errors.setText("In line " + i + " illegal statement");
							return;
						}
						
// CheckNum can either be an existing variable or a number
						
						if (isNumeric(checkNum)) {
							
							try {
								tempNum = Double.parseDouble(checkNum);
							} catch (Exception e) {
								errors.setText("In line " + i + " illegal statement");
								return;
							}
							
// Takes number and uses operation to get correct result
							if (op.equals("+")) {
								result = result + tempNum;
							} else if (op.equals("-")) { 
								result = result - tempNum;
							} else if (op.equals("*")) {
								result = result * tempNum;
							} else if (op.equals("/")) {
								result = result / tempNum;
							} else {
								errors.setText("In line " + i + " illegal operation");
								return;
							}
							
						} else {
							
// Check if the next variable exists in memory in the variable list	
							if (variableList.indexOf(checkNum) == -1) {
								errors.setText("In line " + i + " \"" + checkNum + "\" is undefined");
								return;
							} else {
								
// Takes the variable from memory and uses the operation to get the correct final result	
								int pos = variableList.indexOf(checkNum);
								
								if (op.equals("+")) {
									result = result + valueList.get(pos);
								} else if (op.equals("-")) { 
									result = result - valueList.get(pos);
								} else if (op.equals("*")) {
									result = result * valueList.get(pos);
								} else if (op.equals("/")) {
									result = result / valueList.get(pos);
								} else {
									errors.setText("In line " + i + " illegal operation");
									return;
								}
							}				
							
						}
											
					}
					
// Adds the variable name and the value to memory
					
					variableList.add(varName);
					valueList.add(result);
					
// The value for a variable has already been defined, and is being rewritten
				} else {
					
					int pos = variableList.indexOf(varName);
					double oldVal = valueList.get(pos);
					double tempVal = 0.0;
					double newVal = 0.0;
					
// checkAssign can be either an existing variable, or a number
					
					if (isNumeric(checkAssign)) {
						
						try {
							tempVal = Double.parseDouble(checkAssign);
						} catch (Exception e) {
							errors.setText("In line " + i + " illegal statement");
							return;
						}
						
						newVal = tempVal;
					
					
// Means variable could exist as a value already		
					} else {
						
						if (variableList.indexOf(checkAssign) == -1) {
							errors.setText("In line " + i + " \"" + checkAssign + "\" is undefined");
							return;
						} else {
							pos = variableList.indexOf(varName);
							newVal = valueList.get(pos);
						}
						
					}
					
					if (tok.hasMoreTokens())
					while(tok.hasMoreTokens()) {
							
						String op = "";
						String checkNum = "";
						double tempNum = 0.0;
						
						try {
							op = tok.nextToken();
						} catch (Exception e) {
							errors.setText("In line " + i + " illegal statement");
							return;
						}
						

//Check to see if the operation is one of the valid operations
						if (!(op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/"))) {
							errors.setText("In line " + i + " illegal operation");
							return;
						}
						
						try {
							checkNum = tok.nextToken();
						} catch (Exception e) {
							errors.setText("In line " + i + " illegal statement");
							return;
						}
						
//CheckNum can be either a number or a variable existing in memory
						if (isNumeric(checkNum)) {
							try {
								tempNum = Double.parseDouble(checkNum);
							} catch (Exception e) {
								errors.setText("In line " + i + " illegal statement");
								return;
							}
	// Takes number and uses operation to get correct result
							if (op.equals("+")) {
								newVal = newVal + tempNum;
							} else if (op.equals("-")) { 
								newVal = newVal - tempNum;
							} else if (op.equals("*")) {
								newVal = newVal * tempNum;
							} else if (op.equals("/")) {
								newVal = newVal / tempNum;
							} else {
								errors.setText("In line " + i + " illegal operation");
								return;
							}
						} else {
// Check if the next variable exists in memory in the variable list	
							if (variableList.indexOf(checkNum) == -1) {
								errors.setText("In line " + i + " \"" + checkNum + "\" is undefined");
								return;
							} else {
// Takes the variable from memory and uses the operation to add it to the final result	
								int posOfOldVar = variableList.indexOf(checkNum);
								if (op.equals("+")) {
									newVal = newVal + valueList.get(posOfOldVar);
								} else if (op.equals("-")) { 
									newVal = newVal - valueList.get(posOfOldVar);
								} else if (op.equals("*")) {
									newVal = newVal * valueList.get(posOfOldVar);
								} else if (op.equals("/")) {
									newVal = newVal / valueList.get(posOfOldVar);
								} else {
									errors.setText("In line " + i + " illegal operation");
									return;
								}
							}					
						}						
					}
// Takes the new value and puts it in memory with the variable assigned
					valueList.set(pos, newVal);	
				}
			} else {
				errors.setText("In line " + i + " illegal statement");
				return;		
			}
		}
	}
// Method to check if a value is a number or not, useful for invalid variable names, 
// and adding to variables
	public static boolean isNumeric(String str) { 
		  try {  
			  Double.parseDouble(str);  
			  return true;
		  } catch(NumberFormatException e){  
			  return false;  
		  }  
	}
}