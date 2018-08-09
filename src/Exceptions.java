import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

public class Exceptions extends URCalculator {
	// ()
	static int pO = 0;
	static int pC = 0;
	// []
	static int sO = 0;
	static int sC = 0;
	// {}
	static int cO = 0;
	static int cC = 0;

	public static boolean isHolder(String val) {
		if (val.equals("(") || val.equals(")") || val.equals("[") || val.equals("]") || val.equals("{")
				|| val.equals("}")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean containsHolder(String input) {
		StringTokenizer st = new StringTokenizer(input);
		String constant = st.nextToken();

		while (st.hasMoreTokens()) {
			if (isHolder(constant)) {
				return true;
			}
		}
		return false;
	}

	public static boolean wrongNumberHolder(String input) {
		int count = 0;
		StringTokenizer st = new StringTokenizer(input);

		while (st.hasMoreElements()) {
			String next = st.nextToken();
			if (isHolder(next)) {
				count++;
			}
		}
		if (count % 2 != 0) {
			return false;
		} else {
			return true;
		}
	}
	

	public static boolean wrongType(String input) {
		StringTokenizer st = new StringTokenizer(input);

		while (st.hasMoreTokens()) {
			String constant = st.nextToken();
			// (
			if (holderType(constant) == -2) {
				pO++;
			}
			// )
			else if (holderType(constant) == 5) {
				pC++;
			}
			// [
			else if (holderType(constant) == -1) {
				sO++;
			}
			// ]
			else if (holderType(constant) == 4) {
				sC++;
			}
			// {
			else if (holderType(constant) == 0) {
				cO++;
			}
			// }
			else if (holderType(constant) == 3) {
				cC++;
			} else {
			}
		}
		if (pO != pC || sO != sC || cO != cC) {
			return false;
		} else {
			return true;
		}

	}

	//order to holders are correct
	public static boolean rightOrder(Stack<String> input) {
		Stack<String> closing = new Stack<>();

		while (!input.empty()) {
			// if it is a closing holder in the input stack, push it into the closing stack
			if (opr.get(input.peek()) == 5 || opr.get(input.peek()) == 4 || opr.get(input.peek()) == 3) {
				closing.push(input.pop());
			}
			// if it is an opening holder, compare the value with the one in the closing
			// stack
			else {
				// if opening brackets match
				if (opr.get(input.peek()) == -2 && opr.get(closing.peek()) == 5) {
					input.pop();
					closing.pop();
				} else if (opr.get(input.peek()) == -1 && opr.get(closing.peek()) == 4) {
					input.pop();
					closing.pop();
				} else if (opr.get(input.peek()) == 0 && opr.get(closing.peek()) == 3) {
					input.pop();
					closing.pop();
				} else {
					return false;
				}
			}
		}
		return true;
	}

	// makes sure that the last holder is not an opening bracket (invalid statement)
	public static boolean isLastOpen(Stack<String> input) {
		if (opr.get(input.peek()) == -2 || opr.get(input.peek()) == -1 || opr.get(input.peek()) == 0) {
			return false;
		}
		return true;
	}

	public static boolean opsAndOperands(Stack<String> input) {
		int operands = 0;
		int operators = 0;
		
		//makes a count of the number of operators and operands in the stack
		while (!input.empty()) {
			if (isOperator(input.peek())) {
				operators++;
			}
			else if(isNumeric(input.peek())) {
				operands++;
			}
			input.pop();
		}
		
		//returns false if there is not one less operator than operands
		if(operators != (operands - 1)) {
			return false;
		}
		return true;
	}

	// puts all of the operators into a stack
	public static Stack<String> addOpsToStack(String input) {
		StringTokenizer st = new StringTokenizer(input);
		Stack<String> checker = new Stack<>();

		while (st.hasMoreTokens()) {
			String constant = st.nextToken();
			if (isHolder(constant)) {
				checker.push(constant);
			}
		}
		return checker;
	}

	public static boolean isOperator(String val) {

		if (val.equals("+") || val.equals("-") || val.equals("/") || val.equals("*")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean invalidExpression(String input) {
		StringTokenizer st = new StringTokenizer(input);
		ArrayList<String> last = new ArrayList<>();

		while (st.hasMoreTokens()) {
			String constant = st.nextToken();
			last.add(constant);
		}

		if (isOperator(last.get(last.size() - 1)) || last.get(last.size() - 1).equals("=")) {
			return false;
		} else {
			return true;
		}
	}
	
	//checks if it is an invalid expression by seeing if 
	public static boolean consecOpsOpds(String input) {
		StringTokenizer st = new StringTokenizer(input);
		ArrayList<String> vals = new ArrayList<>();
		
		while(st.hasMoreTokens()) {
			vals.add(st.nextToken().trim());
		}
		
		for(int i = 0; i < vals.size() - 1; i++) {
			 if (isNumeric(vals.get(i)) && isNumeric(vals.get(i + 1))){
					 return false;
				 }
			}
		return true;
	}

	public static int holderType(Object val) {
		if (val.equals("(")) {
			return opr.get(val);
		} else if (val.equals(")")) {
			return opr.get(val);
		} else if (val.equals("]")) {
			return opr.get(val);
		} else if (val.equals("[")) {
			return opr.get(val);
		} else if (val.equals("{")) {
			return opr.get(val);
		} else if (val.equals("}")) {
			return opr.get(val);
		} else {
			return -1000;
		}
	}
}
