public class ParameterRational extends ParameterFunction {
    private ParameterPolynomial numerator;
    private ParameterPolynomial denominator;

    ParameterRational() {
        this.type = "Rational";
        this.parameterCount = 0;
        this.numerator = null;
        this.denominator = null;
    }

    ParameterRational(ParameterPolynomial numerator, ParameterPolynomial denominator) {
        this.type = "Rational";
        this.parameterCount = numerator.getParameterCount() + denominator.getParameterCount();
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public ParameterPolynomial getNumerator() {
        return numerator;
    }

    public ParameterPolynomial getDenominator() {
        return denominator;
    }

    public void setNumerator(ParameterPolynomial numerator) {
        this.numerator = numerator;
    }

    public void setDenominator(ParameterPolynomial denominator) {
        this.denominator = denominator;
    }
}
