import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the a bound: ");
        int a = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the b bound: ");
        int b = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the accuracy: ");
        double accuracy = scanner.nextDouble();
        scanner.nextLine();
        Expression expression = new Expression("x^3-3x^2-14x-8", "3x^2-6x-14", "6x-6", a, b, accuracy);
        System.out.println(Methods.chordMethod(expression));
        System.out.println(Methods.newtonsMethod(expression));
        System.out.println(Methods.combinatedMethod(expression));
    }
}

class Expression {
    private String expresion;
    private String derivative;
    private String secondDerivative;
    private int a;
    private int b;
    private double accuracy;

    public Expression(String expresion, String derivative, String secondDerivative, int a, int b, double accuracy) {
        this.expresion = expresion;
        this.derivative = derivative;
        this.secondDerivative = secondDerivative;
        this.a = a;
        this.b = b;
        this.accuracy = accuracy;
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

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public double getAccuracy() {
        return accuracy;
    }
}

class Methods {
    public static double chordMethod(Expression expression) {
        if (expression == null) {
            System.out.println("expression is null");
            return -1;
        }
        System.out.println("The Chord Method: ");
        double xn; //currentValueOfMovingPoint
        int counter = 0;

        double fafiia = replaceX(expression.getExpresion(), expression.getA()) * replaceX(expression.getSecondDerivative(), expression.getA());
        double fbfiib = replaceX(expression.getExpresion(), expression.getB()) * replaceX(expression.getSecondDerivative(), expression.getB());

        if (fafiia > 0 && fbfiib < 0) {//moving point is b
            xn = expression.getB();
            while (replaceX(expression.getExpresion(), expression.getA()) * replaceX(expression.getExpresion(), xn) < 0){
                xn = xn - (((expression.getA()-xn)*(replaceX(expression.getExpresion(), xn)))/(replaceX(expression.getExpresion(), expression.getA()) - replaceX(expression.getExpresion(), xn)));
                counter++;
                System.out.println(xn);
            }
        } else if (fafiia < 0 && fbfiib > 0) {//moving point is a
            xn = expression.getA();
            while (replaceX(expression.getExpresion(), xn) * replaceX(expression.getExpresion(), expression.getB()) < 0){
                xn = xn - (((expression.getB()-xn)*(replaceX(expression.getExpresion(), xn)))/(replaceX(expression.getExpresion(), expression.getB()) - replaceX(expression.getExpresion(), xn)));
                counter++;
                System.out.println(xn);
            }
        } else {
            System.out.println("Moving point isn't detected");
            return -1;
        }
        System.out.println("Number of iterations by Chord method = " + counter);
        calculateAccuracy(xn, expression.getAccuracy());
        return xn;
    }

    public static double newtonsMethod(Expression expression) {
        if (expression == null) {
            System.out.println("expression is null");
            return -1;
        }
        System.out.println("The Newton's Method: ");
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
        calculateAccuracy(xn, expression.getAccuracy());
        return xn;
    }

    public static double newtonsIteration(Expression expression, double x0) {
        double result = x0 - (replaceX(expression.getExpresion(), x0) / replaceX(expression.getDerivative(), x0));
        System.out.println(result);
        return result;
    }

    public static double combinatedMethod(Expression expression) {
        if (expression == null) {
            System.out.println("expression is null");
            return -1;
        }
        System.out.println("The Combinated Method: ");
        double an = expression.getA();
        double bn = expression.getB();
        int counter = 0;

        double fafiia = replaceX(expression.getExpresion(), expression.getA()) * replaceX(expression.getSecondDerivative(), expression.getA());
        double fbfiib = replaceX(expression.getExpresion(), expression.getB()) * replaceX(expression.getSecondDerivative(), expression.getB());

        if (fafiia > 0) {//a - tangent method of Newton, b - chord method
            while (replaceX(expression.getExpresion(), an) * replaceX(expression.getExpresion(), bn) < 0){
                bn = bn - (((an-bn)*(replaceX(expression.getExpresion(), bn)))/(replaceX(expression.getExpresion(), an) - replaceX(expression.getExpresion(), bn)));
                an = an - (replaceX(expression.getExpresion(), an) / replaceX(expression.getDerivative(), an)); // bn first because it depends from an and old version of an is needed
                counter++;
                System.out.println("[" + an + "; " + bn + "]");
            }
        } else if (fbfiib > 0) {//a - chord method, b - tangent method of Newton
            while (replaceX(expression.getExpresion(), an) * replaceX(expression.getExpresion(), bn) < 0){
                an = an - (((bn-an)*(replaceX(expression.getExpresion(), an)))/(replaceX(expression.getExpresion(), bn) - replaceX(expression.getExpresion(), an)));
                bn = bn - (replaceX(expression.getExpresion(), bn) / replaceX(expression.getDerivative(), bn));
                counter++;
                System.out.println("[" + an + "; " + bn + "]");
            }
        } else {
            System.out.println("Moving point isn't detected");
            return -1;
        }
        System.out.println("Number of iterations by Chord method = " + counter);
        //calculateAccuracy(xn, expression.getAccuracy());
        return an;
    }

    public static double calculateAccuracy(double value, double accuracy) {
        return -1;
    }

    public static double replaceX(String expression, double x) {
        double result = 0;
        String expr = expression.replace("-", "+-"); // split easier by '+'
        String[] terms = expr.split("\\+");

        for (String term : terms) {
            if (term.isEmpty()) continue;

            if (term.contains("x^")) {
                // form: ax^b
                String[] parts = term.split("x\\^");
                int coeff = parts[0].isEmpty() || parts[0].equals("+") ? 1 : parts[0].equals("-") ? -1 : Integer.parseInt(parts[0]);
                int power = Integer.parseInt(parts[1]);
                result += coeff * Math.pow(x, power);
            } else if (term.contains("x")) {
                // form: ax
                String coeffPart = term.replace("x", "");
                int coeff = coeffPart.isEmpty() || coeffPart.equals("+") ? 1 : coeffPart.equals("-") ? -1 : Integer.parseInt(coeffPart);
                result += coeff * x;
            } else {
                // just a number
                result += Integer.parseInt(term);
            }
        }
        return result;
    }

    public void newtonsContinue() {

    }

}