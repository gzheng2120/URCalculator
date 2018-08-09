import java.util.Stack;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class URCalculator {
	static Stack<String> operators = new Stack<>();
	static Stack<String> postFix = new Stack<>();
	static HashMap<String, Integer> opr = new HashMap<>();
	static HashMap<String, Double> assign = new HashMap<>();
	static boolean rightHolder = true;
	static final double pi = Math.PI;
	static final double e = Math.E;

	public static void main(String[] args) {
		// Hashmap of operator values
		opr.put("(", -2);
		opr.put("[", -1);
		opr.put("{", 0);
		opr.put("+", 1);
		opr.put("-", 1);
		opr.put("/", 2);
		opr.put("*", 2);
		opr.put("}", 3);
		opr.put("]", 4);
		opr.put(")", 5);

		Scanner kb = new Scanner(System.in);

		String input;
		do {
			// asks the user for input
			System.out.println("Insert expression you want to evaluate: ");
			input = kb.nextLine();
			// if user wants to exit
			if (input.equals("exit")) {
				System.out.println("You exited the calculator");
				System.exit(0);

				// if user wants to see all values in the assign hashmap
			} else if (input.equals("show all")) {
				System.out.println("Here are all the values you want to show: ");
				printAssignment(assign);
				continue;

				// if the user wants to clear the assignment table
			} else if (input.equals("clear all")) {
				assign.clear();
				continue;
			}
			// shows the specified variable
			else if (input.contains("show") && !input.contains("all")) {
				// EXTRA CREDIT: Adding pi, and e into the assignment table if the user calls
				// for it. Also shows the max and min values in the assign map
				assign.put("e", e);
				assign.put("pi", pi);
				
				String token = "";
				String[] parts = input.split(" ");
				for (int k=0; k<parts.length; k++) {
					if (!parts[k].equals("show") && !parts[k].equals(" ")) {
						token = parts[k];
						break;
					}
				}
				
				if(!keyContain(token)) {
					System.out.println("Invalid expression. Key is not in the map.");
					continue;
				}
				
				assign.put("max", maxValueInAssign(assign));
				assign.put("min", minValueInAssign(assign));
				System.out.println(assign.get(input.substring(5)));
				continue;
			}
			// clears the specified variable
			else if (input.contains("clear") && !input.contains("all")) {
				assign.remove(input.substring(6));
				continue;
			}
			else if(!Exceptions.consecOpsOpds(input)) {
				System.out.println("Invalid expression. The operators and operands are not in the right order.");
				continue;
			}

			// takes out all the spaces and puts them back in
			input = extractSpaces(input);

			if (!Exceptions.invalidExpression(input)) {
				System.out.println("Invalid expression.");
				continue;
			}

			// if the input is a letter
			if (containsLetter(input)) {
				try {
					assignVar(input);
					}
				catch (NumberFormatException e) {
					System.out.println("Not a valid expression. Not assigned.");
				}
			}

			else {
				// if the expression is not a well formed expression
				try {
					if (!Exceptions.opsAndOperands(tester(extractSpaces(modifyAddSub(input))))) {
						System.out.println("Invalid expression.");
						continue;
					}
					double val = evaluatePostFix
							.postFix(finalRemovalHolder(tester(extractSpaces(modifyAddSub(input)))));
					if (Double.isInfinite(val)) {
						System.out.println("Invalid. Can't divide by 0.");
						continue;
					} else {
						System.out.println(val);
						continue;
					}
				} catch (EmptyStackException e) {
					System.out.println("Not a well formed expression. Bracket mismatch");
					continue;
				}
			}
		} while (!input.equals("exit"));
	}

	// prints out a stack
	public static void printStack(Stack<String> s) {
		while (!s.empty()) {
			System.out.println("Stack element: " + s.pop());
		}
	}

	// prints out all of the assignment table
	public static void printAssignment(HashMap<String, Double> input) {
		System.out.println(Arrays.asList(input));
	}

	public static double maxValueInAssign(HashMap<String, Double> input) {
		ArrayList<Double> max = new ArrayList<Double>();
		// puts all values into the ArrayList
		for (double a : input.values()) {
			max.add(a);
		}

		// iterates through the arraylist to get the max number
		double maxVal = max.get(0);
		for (double b : max) {
			if (b > maxVal) {
				maxVal = b;
			}
		}
		return maxVal;
	}

	public static double minValueInAssign(HashMap<String, Double> input) {
		ArrayList<Double> min = new ArrayList<Double>();

		for (double a : input.values()) {
			min.add(a);
		}

		double minVal = min.get(0);
		for (double b : min) {
			if (b < minVal) {
				minVal = b;
			}
		}
		return minVal;
	}

	// removes any remaning holders
	public static boolean finalHolderCheck(String input) {
		if (!Exceptions.wrongNumberHolder(input)) {
			return false;
		}
		if (!Exceptions.wrongType(input)) {
			return false;
		}
		if (!Exceptions.isLastOpen(Exceptions.addOpsToStack(input))) {
			return false;
		}
		if (!Exceptions.rightOrder(Exceptions.addOpsToStack(input))) {
			return false;
		} else {
			return true;
		}
	}

	// for variable assignments
	public static void assignVar(String input) {
		// changes input to accommodate for multiple ++ or --
		input = modifyAddSub(input);

		// splits the string up by the =, before the equal is the name of the variable
		// and after is the value of it
		StringTokenizer a = new StringTokenizer(input, "=");
		ArrayList<String> vars = new ArrayList<String>();

		// puts all of the tokens into the ArrayList
		while (a.hasMoreTokens()) {
			vars.add(a.nextToken());
		}
		// gets the last thing in the array and puts it in a stack for tester and then
		// evaluates the number
		String getLast = vars.get(vars.size() - 1);
		getLast = placeNeg(makeTokens(extractSpaces(modifyAddSub(getLast))));

		// if there is not an equal sign
		if (vars.size() == 1) {
			if(!keyContain(getLast)) {
				System.out.println("Invalid expression. Key is not in the map.");
				return;
			}
			vars.set(0, extractSpaces(vars.get(0)));
			System.out.println(evaluatePostFix
					.postFix(finalRemovalHolder(tester(placeNeg(makeTokens(modifyAddSub(vars.get(0))))))));
		}

		// if there is an equal sign and the right part of the expression contains a
		// variable
		else if (vars.size() > 1 && containsLetter(getLast)) {
			if(!keyContain(getLast)) {
				System.out.println("Invalid expression. Key is not in the map.");
				return;
			}
			getLast = Double.toString(
					evaluatePostFix.postFix(finalRemovalHolder(tester(placeNeg(makeTokens(extractSpaces(getLast)))))));
		}

		// if there is an equal sign and the right part does not contain a variable
		else if (vars.size() > 1 && !containsLetter(getLast)) {
			getLast = Double.toString(evaluatePostFix
					.postFix(finalRemovalHolder(tester(placeNeg(makeTokens(extractSpaces(modifyAddSub((getLast)))))))));
		}

		// evaluates the result and changes it into a double
		Double lastResult = evaluatePostFix
				.postFix(finalRemovalHolder(tester(placeNeg(makeTokens(modifyAddSub((getLast)))))));

		// puts everything into the hashmap
		for (int i = 0; i < vars.size() - 1; i++) {
			if (Double.isInfinite(lastResult)) {
				System.out.println("Invalid. Can't divide by 0");
				continue;
			}
			assign.put(vars.get(i).trim(), lastResult);
		}

	}

	public static Stack<String> tester(String input) {
		if (input.equals(null)) {
			return null;
		}
		// separates all the individual things
		StringTokenizer st = new StringTokenizer(input);
		while (st.hasMoreTokens()) {
			String constant = st.nextToken();
			// checks to see if the first value is a number, if it is, place it in the
			// postFix stack
			if (isNumeric(constant)) {
				postFix.push(constant);
			} else {
				try {
					if (Exceptions.isHolder(constant)) {
						// checks to see if the holders work
						if (!finalHolderCheck(input)) {
							rightHolder = false;
							break;
						} else {
							doesItHold(constant);
						}
					}

					// checks the operators and puts it in the order of hierarchy as long as it's
					// not a holder
					while ((Exceptions.isOperator(constant) && opr.get(constant) <= opr.get(operators.peek()))) {
						postFix.push(operators.pop());
					}
					// end try
				} catch (EmptyStackException e) {
				}
				operators.push(constant);
				// end else
			}
			// end while loop
		}
		// if the stack is not empty, put the last value into the postFix
		while (!operators.empty()) {
			if (Exceptions.isHolder(operators.peek())) {
				operators.pop();
				continue;
			}
			postFix.push(operators.pop());
		}
		return postFix;
	}

	// removes all of the holders that may have been left in the stack
	public static Stack<String> finalRemovalHolder(Stack<String> input) {
		Stack<String> container = new Stack<String>();
		Stack<String> vals = new Stack<String>();

		// pushes all of the values that aren't holders into the container stack
		while (!input.empty()) {
			if (Exceptions.isHolder(input.peek())) {
				input.pop();
			} else {
				container.push(input.pop());
			}
		}

		// puts all of the values back into the vals stack
		while (!container.empty()) {
			vals.push(container.pop());
		}
		return vals;
	}

	// public static ArrayList<String> modifyAddSubSpaces(ArrayList<String> input){
	//
	// }

	// checks multiple + and - cases
	public static String modifyAddSub(String constant) {
		constant = makeTokens(constant);
		int i = 0;
		int check = 1;
		String first;
		String next;
		StringTokenizer st = new StringTokenizer(constant);
		ArrayList<String> vars = new ArrayList<>();

		// adds all the values of constant into the array list
		while (st.hasMoreTokens()) {
			vars.add(st.nextToken());
		}
		// removes all the multiples in the list
		while (i + check < vars.size() - 1) {
			first = vars.get(i);
			next = vars.get(i + check);
			if (first.equals("+") || first.equals("-")) {
				if (first.equals("+") && next.equals("+")) {
					vars.set(i, "+");
					vars.set(i + check, " ");
					check++;
				} else if (first.equals("+") && next.equals("-")) {
					vars.set(i, "-");
					vars.set(i + check, " ");
					check++;
				} else if (first.equals("-") && next.equals("-")) {
					vars.set(i, "+");
					vars.set(i + check, " ");
					check++;
				} else if (first.equals("-") && next.equals("+")) {
					vars.set(i, "-");
					vars.set(i + check, " ");
					check++;
				} else {
					i++;
					check = 1;
				}
			} else {
				i++;
				check = 1;
			}
		}
		String move = " ";
		for (String go : vars) {
			move = move + " " + go + " ";
		}
		StringTokenizer mo = new StringTokenizer(move);
		vars.clear();
		// puts everything into an ArrayList
		while (mo.hasMoreTokens()) {
			String te = mo.nextToken();
			vars.add(te);
		}

		// if the first few terms in the array are + or -
		if (vars.get(0).equals("+")) {
			vars.set(0, "");
		} else if (vars.get(0).equals("-")) {
			String out = "0 ";
			for (String run : vars) {
				out = out + " " + run;
			}
			StringTokenizer plusMin = new StringTokenizer(out);
			// puts it back into vars
			vars.clear();
			while (plusMin.hasMoreTokens()) {
				String c = plusMin.nextToken();
				vars.add(c);
			}
		}

		// if there is a + or - right behind of the holder
		for (int n = 0; n < vars.size() - 1; n++) {
			if (Exceptions.isHolder(vars.get(n))) {
				// if the next value is a + and the one after it is not a (
				if (vars.get(n + 1).equals("+") && !Exceptions.isHolder(vars.get(n + 2))
						&& !isNumeric(vars.get(n + 2))) {
					vars.set(n + 1, "");
				}
				// if the + is between an open holder and a variable/number: ex: (+2 then delete
				// the +
				else if (vars.get(n + 1).equals("+") && opr.get(vars.get(n)) == -2
						&& !Exceptions.isHolder(vars.get(n + 2))) {
					vars.set(n + 1, "");
				}
				// if the next value is a -
				else if (vars.get(n + 1).equals("-") && opr.get(vars.get(n)) == -2
						&& !Exceptions.isHolder(vars.get(n + 2))) {
					vars.set(n + 1, "0-");
				}
			}
		}

		// moves everything into a string
		String temp = " ";
		for (String go : vars) {
			temp = temp + " " + go + " ";
		}
		StringTokenizer sec = new StringTokenizer(temp);
		vars.clear();
		// puts everything into an ArrayList
		while (sec.hasMoreTokens()) {
			String te = sec.nextToken();
			vars.add(te);
		}

		// if there is a - or + right behind a * or /
		for (int m = 0; m < vars.size() - 1; m++) {
			if (vars.get(m).equals("*") || vars.get(m).equals("/")) {
				if (vars.get(m + 1).equals("-")) {
					vars.set(m + 1, "");
					String convert = vars.get(m + 2).trim();
					double cha = Double.parseDouble(convert);
					cha = cha * -1;
					String fin = Double.toString(cha);
					vars.set(m + 2, fin);
				} else if (vars.get(m + 1).equals("+")) {
					vars.set(m + 1, "");
				}
			}
		}
		String em = " ";
		for (String go : vars) {
			em = em + " " + go + " ";
		}
		return em;
	}

	// if the value is a type of parenthesis, checks these things
	public static void doesItHold(String constant) {
		constant = replaceHolders(constant).trim();
		// if it is a (, [, or { pushes into operators stack
		if (Exceptions.holderType(constant) == -2) {
			operators.push(constant);
		}

		// if it is a )
		else if (Exceptions.holderType(constant) == 5) {
			// pops out all of the values that aren't the ( bracket
			while (opr.get(operators.peek()) != -2) {
				postFix.push(operators.pop());
			}
			operators.pop();
		}
	}

	// Checks to see if the value is a number or not. Returns true if it is and
	// false if it isn't
	public static boolean isNumeric(String val) {
		try {
			double d = Double.parseDouble(val);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// checks to see if there is an assignment that is going to occur
	public static boolean isLetter(String val) {
		char c = val.trim().charAt(0);
		if (Character.isLetter(c)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean containsLetter(String input) {
		StringTokenizer st = new StringTokenizer(input);

		while (st.hasMoreTokens()) {
			String constant = st.nextToken();
			char c = constant.trim().charAt(0);
			if (Character.isLetter(c)) {
				return true;
			}
		}
		return false;
	}

	// replaces all of the letters with the value from the opr hashmap
	public static String replaceLetter(String input) {
		StringTokenizer st = new StringTokenizer(input);
		ArrayList<String> arr = new ArrayList<String>();

		while (st.hasMoreTokens()) {
			String constant = st.nextToken();
			// if the token is not a holder and its not an operator, add it into the
			// arrayList
			if (isLetter(constant)) {
				arr.add(constant);
			}
		}
		String newInput = "";

		for (int i = 0; i < arr.size(); i++) {
			newInput = input.replaceAll(arr.get(i), assign.get(arr.get(i)).toString());
		}
		return newInput;
	}

	// goes through the assign map and puts all of the values in an arrayList, then
	// checks if the input value is in the list
	public static boolean isInAssign(String input) {
		ArrayList<String> assignVals = new ArrayList<String>();
		for (String a : assign.keySet()) {
			assignVals.add(a);
		}

		if (assignVals.contains(input)) {
			return true;
		} else {
			return false;
		}

	}

	public static String extractSpaces(String input) {
		input = input.replaceAll(" ", "");
		char[] inputChar = input.toCharArray();
		String temp = "";
		ArrayList<Character> charToArray = new ArrayList<Character>();
		for (int k = 0; k < input.length(); k++) {
			charToArray.add(inputChar[k]);
		}
		for (int i = 0; i < input.length(); i++) {
			if (Exceptions.isHolder("" + charToArray.get(i)) || Exceptions.isOperator("" + charToArray.get(i))) {
				temp = temp + " " + charToArray.get(i) + " ";
			} else {
				temp = temp + charToArray.get(i);
			}
		}
		input = temp;
		input = placeNeg(input);
		return input;
	}

	public static String placeNeg(String input) {
		StringTokenizer st = new StringTokenizer(input);
		ArrayList<String> vars = new ArrayList<>();

		while (st.hasMoreTokens()) {
			vars.add(st.nextToken());
		}

		for (int m = 0; m < vars.size() - 1; m++) {
			if (vars.get(m).equals("*") || vars.get(m).equals("/")) {
				if (vars.get(m + 1).equals("-")) {
					vars.set(m + 1, "");
					String convert = vars.get(m + 2).trim();
					;
					double cha = Double.parseDouble(convert);
					cha = cha * -1;
					String fin = Double.toString(cha);
					vars.set(m + 2, fin);
				} else if (vars.get(m + 1).equals("+")) {
					vars.set(m + 1, "");
				}
			}
		}

		String temp = " ";
		for (String a : vars) {
			temp = temp + " " + a + " ";
		}
		input = temp;
		return input;
	}

	// splits the expression into tokens
	public static String makeTokens(String input) {
		StringTokenizer st = new StringTokenizer(input);
		ArrayList<String> vals = new ArrayList<>();

		// adds all of the tokens into the ArrayList
		while (st.hasMoreTokens()) {
			String constant = st.nextToken();
			vals.add(constant);
		}
		// iterate through the ArrayList for the letters
		for (int i = 0; i < vals.size(); i++) {
			if (isLetter(vals.get(i)) && isInAssign(vals.get(i))) {
				vals.set(i, assign.get(vals.get(i)).toString());
			}
		}

		// converts the ArrayList into a string
		String temp = "";
		for (String a : vals) {
			temp = temp + " " + a + " ";
		}
		input = temp;
		return input;

	}

	public static boolean keyContain(String input) {
		input = extractSpaces((input));
		StringTokenizer st = new StringTokenizer(input);
		ArrayList<String> vars = new ArrayList<>();
		
		while(st.hasMoreTokens()) {
			vars.add(st.nextToken().trim());
		}

		for (String a : vars) {
			if (!isNumeric(a) && !Exceptions.isHolder(a) && !Exceptions.isOperator(a) && !assign.containsKey(a)) {
				return false;
			}
		}
		return true;
	}

	// changes all the holders into ()
	public static String replaceHolders(String input) {
		StringTokenizer st = new StringTokenizer(input);
		ArrayList<String> vals = new ArrayList<String>();

		// adds all of the tokens into the ArrayList
		while (st.hasMoreTokens()) {
			vals.add(st.nextToken());
		}

		// goes through a loop to see if what the brackets are
		for (int i = 0; i < vals.size(); i++) {
			if (opr.get(vals.get(i)) == -1 || opr.get(vals.get(i)) == 0) {
				vals.set(i, "(");
			}
			if (opr.get(vals.get(i)) == 4 || opr.get(vals.get(i)) == 3) {
				vals.set(i, ")");
			}
		}

		// makes the ArrayList a string
		String temp = "";
		for (String a : vals) {
			temp = temp + " " + a + " ";
		}
		input = temp;

		return input;
	}

	// empties stack
	public static void makeEmpty(Stack<String> input) {
		while (!input.empty()) {
			input.pop();
		}
	}
}