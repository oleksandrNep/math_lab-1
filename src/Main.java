import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the a bound: ");
        double a = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter the b bound: ");
        double b = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter the accuracy: ");
        double accuracy = scanner.nextDouble();
        scanner.nextLine();
        Expression expression = new Expression("x^3-3x^2-14x-8", "3x^2-6x-14", "6x-6", a, b, accuracy, "(3x^2+14x+8)^(1.0/3)", "(6x+14)/(3*(3x^2+14x+8)^(2/3))");
        System.out.println(Methods.chordMethod(expression));
        System.out.println(Methods.newtonsMethod(expression));
        System.out.println(Methods.combinatedMethod(expression));
        System.out.println(Methods.iterativeMethod(expression));
    }
}

class Expression {
    private String expresion;
    private String derivative;
    private String secondDerivative;
    private double a;
    private double b;
    private double accuracy;
    private String iterationalExp;
    private String derivativeIterationalExp;


    public Expression(String expresion, String derivative, String secondDerivative, double a, double b, double accuracy, String iterationalExp, String derivativeIterationalExp) {
        this.expresion = expresion;
        this.derivative = derivative;
        this.secondDerivative = secondDerivative;
        this.a = a;
        this.b = b;
        this.accuracy = accuracy;
        this.iterationalExp = iterationalExp;
        this.derivativeIterationalExp = derivativeIterationalExp;
    }

    public String getExpresion() {
        return expresion;
    }

    public void setExpresion(String expresion) {
        this.expresion = expresion;
    }

    public String getDerivative() {
        return derivative;
    }

    public void setDerivative(String derivative) {
        this.derivative = derivative;
    }

    public String getSecondDerivative() {
        return secondDerivative;
    }

    public void setSecondDerivative(String secondDerivative) {
        this.secondDerivative = secondDerivative;
    }

    public double getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public String getIterationalExp() {
        return iterationalExp;
    }

    public String getDerivativeIterationalExp() {
        return derivativeIterationalExp;
    }
}

class Methods {
    public static double chordMethod(Expression expression) {
        if (expression == null) {
            System.out.println("expression is null");
            return -1;
        }
        System.out.println("\nThe Chord Method: ");
        double xn; //currentValueOfMovingPoint
        int counter = 0;

        double fafiia = replaceX(expression.getExpresion(), expression.getA()) * replaceX(expression.getSecondDerivative(), expression.getA());
        double fbfiib = replaceX(expression.getExpresion(), expression.getB()) * replaceX(expression.getSecondDerivative(), expression.getB());
        //double xnminus1 = 0;
        if (fafiia > 0 && fbfiib < 0) {//moving point is b
            xn = expression.getB();
            while (replaceX(expression.getExpresion(), expression.getA()) * replaceX(expression.getExpresion(), xn) < 0) {
                //xnminus1 = xn;
                xn = xn - (((expression.getA() - xn) * (replaceX(expression.getExpresion(), xn))) / (replaceX(expression.getExpresion(), expression.getA()) - replaceX(expression.getExpresion(), xn)));
                counter++;
                System.out.println(xn);
            }
        } else if (fafiia < 0 && fbfiib > 0) {//moving point is a
            xn = expression.getA();
            while (replaceX(expression.getExpresion(), xn) * replaceX(expression.getExpresion(), expression.getB()) < 0) {
                xn = xn - (((expression.getB() - xn) * (replaceX(expression.getExpresion(), xn))) / (replaceX(expression.getExpresion(), expression.getB()) - replaceX(expression.getExpresion(), xn)));
                counter++;
                System.out.println(xn);
            }
        } else {
            System.out.println("Moving point isn't detected");
            return -1;
        }
        System.out.println("Number of iterations by Chord method = " + counter);
        calculateAccuracyChord(xn, expression);
        return xn;
    }

    public static void calculateAccuracyChord(double xn, Expression expression) {
//        System.out.println(replaceX(expression.getExpresion(), xn));
//        System.out.println(replaceX(expression.getDerivative(), xn));
        double inAccurate = (replaceX(expression.getExpresion(), xn) / replaceX(expression.getDerivative(), xn));
        if (inAccurate <= expression.getAccuracy()) {
            System.out.println("Похибка: " + inAccurate + " точність збігається");
        } else {
            System.out.println("Точність не збігається");
        }
    }


    public static double newtonsMethod(Expression expression) {
        if (expression == null) {
            System.out.println("expression is null");
            return -1;
        }
        System.out.println("\nThe Newton's Method: ");
        double xn;
        int counter = 0;

        if (replaceX(expression.getExpresion(), expression.getA()) * replaceX(expression.getSecondDerivative(), expression.getA()) > 0) {
            //moving point is a point
            xn = expression.getA();
            while (replaceX(expression.getExpresion(), xn) * replaceX(expression.getExpresion(), expression.getB()) < 0) {
                xn = newtonsIteration(expression, xn);
                counter++;
            }
        } else if (replaceX(expression.getExpresion(), expression.getB()) * replaceX(expression.getSecondDerivative(), expression.getB()) > 0) {
            //moving point is b point
            xn = expression.getB();
            while (replaceX(expression.getExpresion(), expression.getA()) * replaceX(expression.getExpresion(), xn) < 0) {
                xn = newtonsIteration(expression, xn);
                counter++;
            }
        } else {
            System.out.println("Moving point isn't detected");
            return -1;
        }
        System.out.println("Number of iterations by Newton's method = " + counter);
        calculateAccuracyNewton(xn, expression);
        return xn;
    }

    public static double newtonsIteration(Expression expression, double x0) {
        double result = x0 - (replaceX(expression.getExpresion(), x0) / replaceX(expression.getDerivative(), x0));
        System.out.println(result);
        return result;
    }

    public static void calculateAccuracyNewton(double xn, Expression expression) {
        double inAccurate = Math.pow(replaceX(expression.getExpresion(), xn), 2)/2*(replaceX(expression.getSecondDerivative(), xn)/Math.pow(replaceX(expression.getDerivative(), xn), 3));
        System.out.println(replaceX(expression.getExpresion(), xn));
        if (inAccurate < expression.getAccuracy()) {
            System.out.println("Похибка: " + inAccurate + " точність збігається");
        } else {
            System.out.println("Точність не збігається");
        }
    }

    public static double combinatedMethod(Expression expression) {
        if (expression == null) {
            System.out.println("expression is null");
            return -1;
        }
        System.out.println("\nThe Combinated Method: ");
        double an = expression.getA();
        double bn = expression.getB();
        int counter = 0;

        double fafiia = replaceX(expression.getExpresion(), expression.getA()) * replaceX(expression.getSecondDerivative(), expression.getA());
        double fbfiib = replaceX(expression.getExpresion(), expression.getB()) * replaceX(expression.getSecondDerivative(), expression.getB());

        if (fafiia > 0) {//a - tangent method of Newton, b - chord method
            while (replaceX(expression.getExpresion(), an) * replaceX(expression.getExpresion(), bn) < 0) {
                bn = bn - (((an - bn) * (replaceX(expression.getExpresion(), bn))) / (replaceX(expression.getExpresion(), an) - replaceX(expression.getExpresion(), bn)));
                an = an - (replaceX(expression.getExpresion(), an) / replaceX(expression.getDerivative(), an)); // bn first because it depends from an and old version of an is needed
                counter++;
                System.out.println("[" + an + "; " + bn + "]");
            }
        } else if (fbfiib > 0) {//a - chord method, b - tangent method of Newton
            while (replaceX(expression.getExpresion(), an) * replaceX(expression.getExpresion(), bn) < 0) {
                an = an - (((bn - an) * (replaceX(expression.getExpresion(), an))) / (replaceX(expression.getExpresion(), bn) - replaceX(expression.getExpresion(), an)));
                bn = bn - (replaceX(expression.getExpresion(), bn) / replaceX(expression.getDerivative(), bn));
                counter++;
                System.out.println("[" + an + "; " + bn + "]");
            }
        } else {
            System.out.println("Moving point isn't detected");
            return -1;
        }
        System.out.println("Number of iterations by Combinated method = " + counter);
        //calculateAccuracy(xn, expression.getAccuracy());
        return an;
    }

    public static double iterativeMethod(Expression expression) {
        if (expression == null) {
            System.out.println("expression is null");
            return -1;
        }
        System.out.println("\nThe Iterative Method: ");

        double xnminus1 = 0;
        double actualxnminus1 = -1;
        double xn = -1;
        int counter = 0;

        double countingValue = replaceX(expression.getDerivativeIterationalExp(), expression.getA());
        if (countingValue > -1 && countingValue < 1) {
            xn = expression.getA();
        } else if (replaceX(expression.getDerivativeIterationalExp(), expression.getB()) > -1 && replaceX(expression.getDerivativeIterationalExp(), expression.getB()) < 1) {
            xn = replaceX(expression.getDerivativeIterationalExp(), expression.getB());
        } else {
            System.out.println("xn is not found");
            return -1;
        }
        do {
            actualxnminus1 = xnminus1;
            xnminus1 = xn;
            xn = replaceX(expression.getIterationalExp(), xnminus1);
            counter++;
        } while (xnminus1 != xn);

        System.out.println("Number of iterations by Iterations method = " + counter);
        calculateAccuracyIteration(xn, actualxnminus1, expression);
        return xn;
    }

    public static void calculateAccuracyIteration(double xn, double xnminus1, Expression expression) {
        double r = replaceX(expression.getExpresion(), xn);
        double inAccurate = (r * (xn-xnminus1))/(1-r);
//        System.out.println(r);
//        System.out.println(xn + "     " + xnminus1);
//        System.out.println(inAccurate);
        if (inAccurate < expression.getAccuracy()) {
            System.out.println("Похибка: " + inAccurate + " точність збігається");
        } else {
            System.out.println("Точність не збігається");
        }
    }

    public static double replaceX(String expression, double x) {
        Parser p = new Parser(expression, x);
        return p.parse();
    }

    private static class Parser {
        private final String s;
        private final double xValue;
        private int pos;
        private final int len;

        Parser(String expr, double x) {
            this.s = expr;
            this.xValue = x;
            this.pos = 0;
            this.len = s.length();
        }

        double parse() {
            double value = parseExpression();
            skipWhitespace();
            if (pos < len) throw new RuntimeException("Unexpected char at pos " + pos + ": '" + peek() + "'");
            return value;
        }

        // Expression := Term { ('+'|'-') Term }
        private double parseExpression() {
            double value = parseTerm();
            while (true) {
                skipWhitespace();
                char c = peek();
                if (c == '+') {
                    pos++;
                    value += parseTerm();
                } else if (c == '-') {
                    pos++;
                    value -= parseTerm();
                } else break;
            }
            return value;
        }

        // Term := Factor { ('*'|'/' | implicit-mul) Factor }
        private double parseTerm() {
            double value = parseFactor();
            while (true) {
                skipWhitespace();
                char c = peek();
                if (c == '*') {
                    pos++;
                    value *= parseFactor();
                } else if (c == '/') {
                    pos++;
                    value /= parseFactor();
                } else {
                    // check for implicit multiplication: next token starts with '(' or 'x' or digit or '.'
                    if (c == '(' || c == 'x' || Character.isDigit(c) || c == '.') {
                        // treat as multiplication
                        value *= parseFactor();
                    } else break;
                }
            }
            return value;
        }

        // Factor := Unary { '^' Factor }   (right-associative)
        private double parseFactor() {
            double value = parseUnary();
            skipWhitespace();
            if (peek() == '^') {
                pos++;
                double exponent = parseFactor(); // right-associative
                value = Math.pow(value, exponent);
            }
            return value;
        }

        // Unary := ('+'|'-') Unary | Primary
        private double parseUnary() {
            skipWhitespace();
            char c = peek();
            if (c == '+') {
                pos++;
                return parseUnary();
            }
            if (c == '-') {
                pos++;
                return -parseUnary();
            }
            return parsePrimary();
        }

        // Primary := number | 'x' | '(' Expression ')'
        private double parsePrimary() {
            skipWhitespace();
            char c = peek();
            if (c == '(') {
                pos++;
                double val = parseExpression();
                skipWhitespace();
                if (peek() != ')') throw new RuntimeException("Missing closing ) at pos " + pos);
                pos++;
                return val;
            }

            if (c == 'x') {
                pos++;
                return xValue;
            }

            // number (integer or floating)
            if (Character.isDigit(c) || c == '.') {
                int start = pos;
                while (pos < len && (Character.isDigit(s.charAt(pos)) || s.charAt(pos) == '.')) pos++;
                String num = s.substring(start, pos);
                return Double.parseDouble(num);
            }

            throw new RuntimeException("Unexpected token at pos " + pos + ": '" + c + "'");
        }

        private void skipWhitespace() {
            while (pos < len && Character.isWhitespace(s.charAt(pos))) pos++;
        }

        private char peek() {
            if (pos >= len) return '\0';
            return s.charAt(pos);
        }
    }

//    public static double replaceX(String expression, double x) {
//        double result = 0;
//
//        // handle parentheses recursively
//        while (expression.contains("(")) {
//            int close = expression.indexOf(")");
//            int open = expression.lastIndexOf("(", close);
//            String inside = expression.substring(open + 1, close);
//            double val = replaceX(inside, x); // recursion
//            expression = expression.substring(0, open) + val + expression.substring(close + 1);
//        }
//
//        // split by + (after normalizing - into +-)
//        String expr = expression.replace("-", "+-");
//        String[] terms = expr.split("\\+");
//
//        for (String term : terms) {
//            if (term.isEmpty()) continue;
//
//            // Case 1: something like "ax^b"
//            if (term.contains("x^")) {
//                String[] parts = term.split("x\\^");
//                int coeff = parts[0].isEmpty() || parts[0].equals("+") ? 1 :
//                        parts[0].equals("-") ? -1 : Integer.parseInt(parts[0]);
//                int power = Integer.parseInt(parts[1]);
//                result += coeff * Math.pow(x, power);
//
//                // Case 2: plain "ax"
//            } else if (term.contains("x")) {
//                String coeffPart = term.replace("x", "");
//                int coeff = coeffPart.isEmpty() || coeffPart.equals("+") ? 1 :
//                        coeffPart.equals("-") ? -1 : Integer.parseInt(coeffPart);
//                result += coeff * x;
//
//                // Case 3: number^power (e.g. "153.0^2")
//            } else if (term.contains("^")) {
//                String[] parts = term.split("\\^");
//                double base = replaceX(parts[0], x);
//                double power = replaceX(parts[1], x);
//                result += Math.pow(base, power);
//
//                // Case 4: multiplication
//            } else if (term.contains("*")) {
//                String[] parts = term.split("\\*", 2);
//                result += replaceX(parts[0], x) * replaceX(parts[1], x);
//
//                // Case 5: division
//            } else if (term.contains("/")) {
//                String[] parts = term.split("/", 2);
//                result += replaceX(parts[0], x) / replaceX(parts[1], x);
//
//                // Case 6: plain number
//            } else {
//                result += Double.parseDouble(term);
//            }
//        }
//        return result;
//    }

}