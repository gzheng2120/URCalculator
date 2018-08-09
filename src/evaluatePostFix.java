import java.util.Stack;
import java.util.StringTokenizer;

public class evaluatePostFix {
	public static double postFix(Stack<String> input) {
		Stack<String> inverse = new Stack<>();
		Stack<String> evaluate = new Stack<>();
		// puts everything from the original stack (post fix form) into a new stack that
		// is the inverse of it
		while (!input.isEmpty()) {
			inverse.push(input.pop());
		}

		// while the inverse stack is not empty
		while (!inverse.isEmpty()) {
			// if the next value is not an operator, push it in the evaluate stack
			if (!Exceptions.isOperator(inverse.peek())) {
				evaluate.push(inverse.pop());
			}

			// if the inverse stack is a operator, pop the last 2 values
			else if (Exceptions.isOperator(inverse.peek())) {
				String op = inverse.pop();
				String b = evaluate.pop();
				String a = evaluate.pop();
				Double eval = math(op, a, b);
				String sEval = Double.toString(eval);
				evaluate.push(sEval);
			}
		}
		Double l = Double.parseDouble(evaluate.pop());
		return l;
	}

	public static double math(String op, String a, String b) {
		double a1 = Double.parseDouble(a);
		double a2 = Double.parseDouble(b);
		if (op.equals("+")) {
			return (a1 + a2);
		} else if (op.equals("-")) {
			return (a1 - a2);
		} else if (op.equals("*")) {
			return (a1 * a2);
		} else {
			return (a1 / a2);
		}
	}
}
