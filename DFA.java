import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DFA {

	/*
	 * This program prints out the trace of a DFA from a string input and checks
	 * if the string is accepted or not.
	 * 
	 * The program has two options:
	 * 
	 * Default DFA: If there is argument parsed into the command line, a default
	 * DFA with the designated language L will be used; whereby L is the union
	 * of the two languages L1 and L3 (strings which do not contain the pattern
	 * '101' and strings that are odd of length).
	 * 
	 * Generic DFA: If there is an additional argument parsed into the command
	 * line (and such argument is a valid file with details and proper syntax
	 * for a DFA),the syntax of the file will be used in the program.
	 * 
	 * Date: 15-4-15 
	 * Author: Lyn Chen
	 * Subject: COMP2022 
	 */

	// Lists used for the default minimum DFA.
	// states - used to store all states available in the default minimum DFA.
	protected static ArrayList<Object> states = new ArrayList<Object>();

	/*
	 * Lists and a string used for the generic DFA:
	 * 
	 * 1. genericStates - used to store all states available for the generic
	 * DFA.
	 * 2. transitionList - used to store all the transitions of each state
	 * (an arraylist where each element is an arraylist containing the
	 * transitions of a state)
	 * 3. genericStart - a string used to define the
	 * initial state. 
	 * 4. finalStates - a list containing the names of all the
	 * final states.
	 */
	protected static ArrayList<String> genericStates = new ArrayList<String>();
	protected static ArrayList<Object> transitionList = new ArrayList<Object>();
	protected static String genericStart = null;
	protected static ArrayList<Object> alphabet = new ArrayList<Object>();
	protected static ArrayList<String> finalStates = new ArrayList<String>();

	/*
	 * This method is only used for the minimum DFA input. It is used to
	 * determine what the next state is during transitions.
	 */
	public static ArrayList<Object> dfa(String str1,
			ArrayList<Object> curState, int index, String str2) {

		// The name of the next state is found from transitions listed in the
		// curState list.
		String name = ((String) curState.get(index + 1));
		ArrayList<Object> nextState = new ArrayList<Object>();

		// The dfa trace is printed.
		System.out.println(str1 + "	" + curState.get(0) + " -- " + index
				+ " --> " + curState.get(index + 1) + " " + str2);
		// Search through all the states
		for (Object state : states) {
			if (state != null && ((ArrayList<Object>) state).get(0) == name) {
				nextState = (ArrayList<Object>) state;
				break;
			}

		}
		return nextState;
	}

	public static String genericDFA(String str1, String curState, String input,
			String str2) {
		String nextState = null;
		// Search through the list of states for the state which matches the
		// current state
		for (String str : genericStates) {
			if (str.equals(curState)) {
				/*
				 * 1. genericStates.indexOf(str)) Retrieve the index of where
				 * the current string (current state) is in the genericStates
				 * list 
				 * ------- 
				 * 2. (ArrayList<Object>)
				 * transitionList.get(genericStates.indexOf(str)) Since this
				 * index is the same index as the index it is in the
				 * transitionList list, use this index to locate the transitions
				 * of the given state (as each element in the transition list is
				 * a list of the transitions of each state)
				 * -------
				 * 3.((ArrayList<Object>)
				 * transitionList.get(genericStates.indexOf
				 * (str))).get(alphabet.indexOf(input) Once the particular
				 * transition list of the given state are found, search for the
				 * individual element which denotes what the next state is. The
				 * index which locates the element would be the same as the
				 * index to where the input character is (by definition).
				 * ------- 
				 * 4. nextState = (String) ((ArrayList<Object>)
				 * transitionList.get
				 * (genericStates.indexOf(str))).get(alphabet.indexOf(input));
				 * The next state is thus found.
				 */

				nextState = (String) ((ArrayList<Object>) transitionList
						.get(genericStates.indexOf(str))).get(alphabet
						.indexOf(input));
				//Print the trace for the DFA.
				System.out.println(str1 + " " + curState + " -- " + input
						+ " --> " + nextState + " " + str2);

			}
		}

		return nextState;

	}

	/*
	 * This method stores all the transitions of the generic
	 * DFA into a list. 
	 */
	private static ArrayList<String> transition(String states) {
		ArrayList<String> transition = new ArrayList<String>();
		//Delimiter to used to identify the states.
		//States are separated by commas.
		String delimiter = "[,]"; 
		String[] tokens = states.split(delimiter);
		tokens = states.split(delimiter);
		//Add the states into the transition.
		for (String str : tokens) {
			transition.add(str);
		}
		return transition;
	}

	public static void main(String[] args) {

		//Default DFA used when there is only one arg in the command line.
		if (args.length == 1) {
			//States
			ArrayList<Object> q0q1q5q1q5 = new ArrayList<Object>();
			ArrayList<Object> q1q6 = new ArrayList<Object>();
			ArrayList<Object> q2q6 = new ArrayList<Object>();
			ArrayList<Object> q2q5 = new ArrayList<Object>();
			ArrayList<Object> q3q5 = new ArrayList<Object>();
			ArrayList<Object> q3q6 = new ArrayList<Object>();
			ArrayList<Object> q4q6q6 = new ArrayList<Object>();
			ArrayList<Object> q4q5q5 = new ArrayList<Object>();
			ArrayList<Object> state = null;

			// transitions for q0q1q5q1q5
			q0q1q5q1q5.add("q0q1q5q1q5"); // name
			q0q1q5q1q5.add("q1q6"); // 0
			q0q1q5q1q5.add("q2q6"); // 1
			q0q1q5q1q5.add("accepted"); // status

			// transitions for q1q6
			q1q6.add("q1q6"); // name
			q1q6.add("q0q1q5q1q5"); // 0
			q1q6.add("q2q5"); // 1
			q1q6.add("accepted"); // status

			// transitions for q2q6
			q2q6.add("q2q6"); // name
			q2q6.add("q3q5"); // 0
			q2q6.add("q2q5"); // 1
			q2q6.add("accepted"); // status

			// transitions for q2q5
			q2q5.add("q2q5"); // name
			q2q5.add("q3q6"); // 0
			q2q5.add("q2q6"); // 1
			q2q5.add("accepted"); // status

			// transitions for q3q5
			q3q5.add("q3q5"); // name
			q3q5.add("q1q6"); // 0
			q3q5.add("q4q6q6"); // 1
			q3q5.add("accepted"); // status

			// transition for q3q6
			q3q6.add("q3q6"); // name
			q3q6.add("q0q1q5q1q5"); // 0
			q3q6.add("q4q5q5"); // 1
			q3q6.add("accepted"); // status

			// transition for q4q6q6
			q4q6q6.add("q4q6q6"); // name
			q4q6q6.add("q4q5q5"); // 0
			q4q6q6.add("q4q5q5"); // 1
			q4q6q6.add("accepted"); // status

			// transition for q4q5q5
			q4q5q5.add("q4q5q5"); // name
			q4q5q5.add("q4q6q6"); // 0
			q4q5q5.add("q4q6q6"); // 1
			q4q5q5.add("rejected"); // status

			states.add(q0q1q5q1q5);
			states.add(q1q6);
			states.add(q2q6);
			states.add(q2q5);
			states.add(q3q5);
			states.add(q3q6);
			states.add(q4q6q6);
			states.add(q4q5q5);
			
			//Starting state for the default DFA is q0q1q5q1q5
			state = q0q1q5q1q5;
			//First field in the trace
			String str1 = null; 
			//Last field in the trace
			String str2 = null;
			int j = 0;
			int k = 1;
			Boolean valid = true;
			for (int i = 0; i < args[0].length(); i++) {
				//If the string does not contain the characters in the alphabet {0,1},
				//it will not be accepted.
				if (args[0].charAt(i) != '0' && args[0].charAt(i) != '1') {
					System.out.println("String not accepted.");
					//Invalid input
					valid = false;
					break;
				} else
				//Update the trace and the current DFA.
				str1 = args[0].substring(0, j++);
				str2 = args[0].substring(k++, args[0].length());
				state = dfa(str1, state, (args[0].charAt(i) - '0'), str2);

			}
			//Valid input, print out result (accepting or rejecting).
			if (valid == true)
				System.out.println(state.get(3));

		}

		//Generic DFA when a valid file is used as input in the second argument.
		if (args.length == 2) {
			int line = 0;
			File file = new File(args[1]);
			Scanner scan = null;
			String state = null;
			String str1 = null;
			String str2 = null;
			int j = 0;
			int k = 1;
			//Scan through the file
			try {
				scan = new Scanner(file);
				while (scan.hasNextLine()) {
					/*
					 * Since it is predetermined that the generic DFA
					 * would have four lines which designate the set of
					 * states, alphabet, start state and final states
					 * respectively, a switch statement is used to store 
					 * the states of each line into separate lists.
					 */
					switch (++line) {
					//First line in file = set of states
					case 1:
						String delimiter = "[,]";
						String[] tokens = scan.next().split(delimiter);
						//Retrieve and add states into a list.
						for (String str : tokens) {
							genericStates.add(str);
						}
						break;
					//Second line in file = alphabet
					case 2:
						delimiter = "[,]";
						tokens = scan.next().split(delimiter);
						//Retrieve and add alphabet into a list.
						for (String str : tokens) {
							alphabet.add(str);
						}
						break;
					//Third line in the file = start state
					case 3:
						//Store the string into a variable.
						genericStart = scan.next();
						break;
					//Fourth line in the file = final state(s)
					case 4:
						delimiter = "[,]";
						tokens = scan.next().split(delimiter);
						//Retrieve and add final states into a list.
						for (String str : tokens) {
							finalStates.add(str);
						}
						break;
					}
					if (line <= 4) {
						scan.nextLine();
					} 
					/*
					 * Once the first four lines are scanned, determine
					 * how many more lines there are left to complete
					 * the generic DFA transitions and scan these lines. 
					 */
					else if (line > 4) {
						for (int i = 5; i < i + genericStates.size(); i++) {
							if (line == i && scan.hasNext()) {
								//A list to add all the transitions.
								//(An array list storing another array list)
								transitionList.add(transition(scan.next()));
							}
							if (scan.hasNextLine()) {
								scan.nextLine();
								++line;

							} else {
								break;
							}

						}

					}
					if (!scan.hasNextLine()) {
						scan.close();
						state = genericStart;
						Boolean valid = true;
						//Scan through the input string for the generic DFA.
						for (int i = 0; i < args[0].length(); i++) {
							//Character is in the alphabet, DFA is valid.
							if (alphabet.contains((Character.toString(args[0]
									.charAt(i))))) {
								String input = Character.toString(args[0]
										.charAt(i));
								//First field in the trace.
								str1 = args[0].substring(0, j++);
								//Last field in the trace.
								str2 = args[0].substring(k++, args[0].length());
								//Update current state by calling the genericDFA function.
								state = genericDFA(str1, state, input, str2);

							} else
							//Character is not in the alphabet, DFA is invalid.
								valid = false;
						}
						//Invalid DFA, print message.
						if (valid == false) {
							System.out.println("String not accepted.");
							return;
						}
						/*
						 * Search the final states list and print out the
						 * message regarding whether the state is accepted 
						 * or rejected.
						 */
						if (finalStates.contains(state))
							System.out.println("accepted");
						else
							System.out.println("rejected");
						return;
					}

				}

			}

			catch (FileNotFoundException e) {
				System.out.println("I cannot open file " + args[1]);
			}

		}
	}
}
