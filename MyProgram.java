import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MyProgram {
	/*
	 * This program attempts to output the left most derivations steps
	 * and stack contents for a particular grammar G' (shown in report) 
	 * which can be implemented by a LL(1) table-driven parser. The input
	 * is taken in as a text file and for every output, the messages
	 * "ACCEPTED" and "REJECTED" are printed, depending on whether the 
	 * grammar accepted the input or not.
	 * 
	 * Date: 7-5-2015
	 * Author: Lyn Chen
	 * Lecturer/Tutor: Kalina Yacef
	 * Subject: COMP2022
	 */
	private static int size = 0;
	private static String print = null;
	private static boolean accepted = true;
	private static boolean errorAccepted = true;
	//Each character in the input willbe stored in an array list
	private static ArrayList<String> input = new ArrayList<String>();
	private static ArrayList<String> stack = new ArrayList<String>();
	//The terminal list holds all the terminals for the grammar
	private static ArrayList<String> terminal = new ArrayList<String>();
	/*
	 * The finish list stores any variables which have the end of the stack 
	 * ($) in their follow set
	 */
	private static ArrayList<Object> finish = new ArrayList<Object>();
	
	//There is a separate list for every variable in the grammar
	private static ArrayList<Object> S = new ArrayList<Object>();
	private static ArrayList<Object> R = new ArrayList<Object>();
	private static ArrayList<Object> E = new ArrayList<Object>();
	private static ArrayList<Object> D = new ArrayList<Object>();
	private static ArrayList<Object> A = new ArrayList<Object>();
	private static ArrayList<Object> T = new ArrayList<Object>();
	private static ArrayList<Object> V = new ArrayList<Object>();
	private static ArrayList<Object> C = new ArrayList<Object>();
	private static ArrayList<Object> F = new ArrayList<Object>();
	private static ArrayList<Object> O = new ArrayList<Object>();

	// Push function used for the stack output
	public static ArrayList<String> push(String input) {
		stack.add(input);
		++size;
		return stack;
	}

	// Pop function used for the stack output
	public static ArrayList<String> pop() {
		stack.remove(size - 1);
		--size;
		return stack;
	}

	// Top function used for the stack output
	public static String top() {
		if (stack.size() == 0) {
			return "";
		}
		if (stack.size() > 1) {
			return stack.get(size - 1);
		} else
			return stack.get(0);
	}
	/*
	 * This method creates an array list storing the character of the
	 * first set of a rule. The first element is the character represented
	 * as a string and the second element is the transition itself,
	 * represented as a string. 
	 */
	public static ArrayList<String> First(String first, String transition) {
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(first);
		temp.add(transition);
		return temp;
	}
	/*
	 * Method for the output shown in the terminal.
	 */
	public static void output() {
		String str = new String();
		//The input converted into a string
		for (String s : input) {
			str += s;
		}
		//The stack converted into a string
		String stackStr = new String();
		for (int i = stack.size() - 1; i >= 0; --i) {
			stackStr += stack.get(i);
		}
		print = str + " " + stackStr;
		System.out.println(print);
	}
	/*
	 * The LL1() method represents the table driven parser for the grammar.
	 * This method follows the Descent table-driven parser algorithm.
	 */
	public static void LL1(Boolean errorCheck) {
		push("S");
		output();
		//Scan through every element in the input
		while (input.size() != 0) {
			//Scanning stops when the error recovery option is not activated
			if (!errorCheck && !accepted) {
				break;
			//Conditions when the error recovery option is activated:
			//1.Scanning stops when the characters in the input have been scanned
			} else if (errorCheck && !accepted) {
				/*
				 * Error accepted flag to ensure that the program will print out
				 * the "REJECTED" message (used for the error recovery option)
				 */
				errorAccepted = false;
				if (top().equals("$")) {
					input.remove(0);
					output();
				//2. Else continue scanning
				} else {
					accepted = true;
					input.remove(0);
					pop();
					output();

				}
			//Input is accepted when the remaining input is both empty
			} else if (input.get(0).equals("$") && top().equals("$")) {
				accepted = true;
				
			/*
			 * If the symbol on the top of the stack is a terminal or the
			 * stack is empty (and there is still input):
			 */
			} else if (terminal.contains(top()) || top().equals("$")) {
				//Pop the symbol off the stack if the current symbol in the input
				//is equal to that in the stack
				if (top().equals(input.get(0))) {
					pop();
					input.remove(0);
					output();
				} else {
					//If the symbols (top of the stack and input symbol)are not equal,
					//reject the input and print the error message 
					accepted = false;
					//Error message for a finished input, stack not empty
					if (input.get(0).equals("$")) {
						System.out.println("expected '" + top() + "'"
								+ " instead of '" + "" + "'");
					//Error message for a symbol mismatch
					} else if (!top().equals("$")) {
						System.out.println("expected '" + top() + "'"
								+ " instead of '" + input.get(0) + "'");
					} else
					//Error message for an an unfinished input, stack empty
						System.out.println("expected '" + "" + "'"
								+ " instead of '" + input.get(0) + "'");

				}

			} else {
				/*
				 * This part covers the case where the symbol on top of the stack
				 * is a variable. The function P[t,c] is called, where 't' is the symbol
				 * on top of the stack and 'c' is the current symbol input. The P[t,c]
				 * function returns a string stating the transition. The symbols of the
				 * string will be pushed onto the stack.
				 */
				while (input.size() > 0 && P(top(), input.get(0)) != null) {
					String temp = P(top(), input.get(0));
					pop();
					//Scan through the production string in reverse order and convert
					//each char into a string. This will be pushed into the stack
					for (int i = temp.length() - 1; i >= 0; --i) {
						push(Character.toString(temp.charAt(i)));
					}
					output();
					//Now that new symbols are added into the stack, check if the 
					//current symbol is equal to the top symbol and remove accordingly
					if (input.get(0).equals(top())) {
						pop();
						input.remove(0);
						//If the input is not empty after this, output
						if (input.size() > 0) {
							output();
						}
					}

				}

			}

		}

		/*
		 * Messages to tell the user whether the string is accepted or rejected
		 * 'accepted' refers to a general acceptance of the string and 'errorAccepted'
		 * refers to the special case of error recovery
		 */
		if (errorAccepted && accepted) {
			/*
			 * errorAccepted is true by default, allowing the "ACCEPTED" message to be 
			 * printed when the error recovery option is not activated
			 */
			System.out.println("ACCEPTED");
		} else {
			System.out.println("REJECTED");
		}

	}
	/*
	 * The method loosely represents the table-driven parser and the 
	 * productions involved. The input 't' represents the value on the top
	 * of the stack and the input 'c' represents the current character in the
	 * input string. The return value will be a string showing the productions
	 */
	public static String P(String t, String c) {
		switch (t) {
		case "S":
			return f(S, c);
		case "R":
			return f(R, c);
		case "E":
			return f(E, c);
		case "D":
			return f(D, c);
		case "A":
			return f(A, c);
		case "T":
			return f(T, c);
		case "V":
			return f(V, c);
		case "C":
			return f(C, c);
		case "F":
			return f(F, c);
		case "O":
			return f(O, c);
		}
		return null;
	}
	
	/*
	 * This method matches a variable and the current symbol with its
	 * production rule. It iterates through the array list of the variable
	 * and checks the first element of the each element. 
	 * (as the first element is the symbol in the first set
	 * and the variable list is an array list of an array list)
	 * If there is a string match of the current input and the first element 
	 * of such set, return the second element of the list. The second element
	 * contains a string representing production 
	 * 
	 * For instance, the variable S looks like:
	 * [[i, ER], [x, AR], [y, AR]]
	 * and the program iterates through the list and checks if first element 
	 * of each element is equivalent to the current symbol input. If there is
	 * a match, the second element of the matched element is returned
	 * 
	 */
	private static String f(ArrayList<Object> var, String c) {
		String str = new String();
		//This temporary string is used just in case an error message
		//is needed
		for (int i = 0; i < var.size(); i++) {
			str += "'" + ((ArrayList<String>) var.get(i)).get(0) + "'";
			if (i != var.size() - 1) {
				//In the condition that there can be more than one potential
				//symbol that could have been expected
				str += " or ";
			}
			if (((ArrayList<String>) var.get(i)).get(0).equals(c)) {
				//Return the production if the two symbols match
				return (String) ((ArrayList<String>) var.get(i)).get(1);
			}
		}
		//Return a "" if the prodution includes an epsilon
		if (finish.contains(var)) {
			return "";
		}
		//Error message
		accepted = false;
		System.out.println("expected " + str + " instead of " + "'" + c + "'");
		return null;
	}
	
	public static void main(String[] args) {
		terminal.add("a");
		terminal.add("b");
		terminal.add("x");
		terminal.add("y");
		terminal.add("<");
		terminal.add(">");
		terminal.add("{");
		terminal.add("}");
		terminal.add("(");
		terminal.add(")");
		terminal.add(":");
		terminal.add("=");
		terminal.add("i");
		terminal.add("f");
		terminal.add(";");
		terminal.add("e");
		terminal.add("l");
		terminal.add("s");

		// Variables which have '$' in their follow() set
		finish.add(D);
		finish.add(R);

		/*
		 * First sets for rules in variable S 
		 * First(ER) = {i} 
		 * First(AR) = {x,y}
		 */
		S.add(First("i", "ER"));
		S.add(First("x", "AR"));
		S.add(First("y", "AR"));

		/*
		 * First sets for rules in the variable R
		 * First(AR) = {x,y}
		 */
		R.add(First("x", "AR"));
		R.add(First("y", "AR"));
		
		/*
		 * First sets for the rules in the variable E
		 * First(if(C){S}D) = {i}
		 */
		E.add(First("i", "if(C){S}D"));
		
		/*
		 * First sets for the rules in the variable D
		 * First(else{S}) = {e}
		 */
		D.add(First("e", "else{S}"));
		
		/*
		 * First sets for the rules in the variable A
		 * First(V:=T;) = {x,y}
		 */
		A.add(First("x", "V:=T;"));
		A.add(First("y", "V:=T;"));

		/*
		 * First sets for the rules in the variable T
		 * First(a) = {a}
		 * First(b) = {b}
		 */
		T.add(First("a", "a"));
		T.add(First("b", "b"));

		/*
		 * First sets for the rules in the variable V
		 * First(x) = {x}
		 * First(y) = {y}
		 */
		V.add(First("x", "x"));
		V.add(First("y", "y"));
		
		/*
		 * First sets for the rules in the variable C
		 * First(TOF) = {a,b}
		 * First(VOF) = {x,y}
		 */
		C.add(First("a", "TOF"));
		C.add(First("b", "TOF"));
		C.add(First("x", "VOF"));
		C.add(First("y", "VOF"));

		/*
		 * First sets for the rules in the variable O
		 * First(<) = {<}
		 * First(>) = {>}
		 */
		O.add(First("<", "<"));
		O.add(First(">", ">"));
		
		/*
		 * First sets for the rules in the variable F
		 * First(T) = {a,b}
		 * First(V) = {x,y}
		 */
		F.add(First("a", "T"));
		F.add(First("b", "T"));
		F.add(First("x", "V"));
		F.add(First("y", "V"));
		
		//No input
		if (args.length == 0) {
			System.out.println("File not found.");
		}
		/*
		 * The program takes a single file its input. If a second argument is 
		 * provided and this argument is '-e', an error recovery check is set
		 * in place.
		 */
		else if (args.length == 1 || args.length == 2 && args[1].equals("-e")) {
			File file = new File(args[0]);
			Scanner scan = null;
			try {
				scan = new Scanner(file);
				String str = new String();
				String temp = null;
				//White spaces in the file are ignored. Input is converted to a single string
				while (scan.hasNextLine()) {
					temp = (scan.nextLine()).replaceAll("\\s+", "");
					str = str + temp;
				}
				str += "$";

				// Case where the string is empty
				if (str.equals("$")) {
					System.out.println("ERROR: No input given.");
					return;
				}
				int j = 0;
				
				//Each character of the input string is added to an array list
				for (int i = 0; i < str.length(); i++) {
					++j;
					input.add(str.substring(i, j));
				}
				//Create stack
				push("$");
				
				/*
				 * If error recovery is activated, call the LL1() function with
				 * 'true' as the paramater. Else, call the LL1() function with 
				 * a 'false' paramater.
				 */
				if (args.length == 2 && args[1].equals("-e")) {
					LL1(true);
				} else {
					LL1(false);
				}
			} catch (FileNotFoundException e) {
				System.out.println("I cannot open file " + args[0]);
			}

		}
	}
}
