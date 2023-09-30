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

    @Override
    public void setParameters(double[] parameters) {
        for (int i = 0; i <= this.numerator.getDegree(); i++) {
            this.numerator.getCoefficients()[i] = parameters[i];
        }
        for (int i = 0; i <= this.denominator.getDegree(); i++) {
            this.denominator.getCoefficients()[i] = parameters[i + this.numerator.getDegree() + 1];
        }
    }

    @Override
    public double evaluateAt(double x) {
        double denominatorValue = this.denominator.evaluateAt(x);
        if (denominatorValue == 0) return Double.NaN;
        return this.numerator.evaluateAt(x) / denominatorValue;
    }

    @Override
    public double[] squareErrorParameterAntiGradient(double[] X, double[] Y) {
        double[] antiGradient = new double[this.numerator.getDegree() + this.denominator.getDegree() + 2];
        for (int i = 0; i < X.length; i++) {
            double denominatorValue = this.denominator.evaluateAt(X[i]);
            if (denominatorValue == 0) continue;
            double numeratorValue = this.numerator.evaluateAt(X[i]);
            double value = numeratorValue / denominatorValue;
            double error = value - Y[i];
            double techNum = 2 * error / denominatorValue;
            double techDen = -2 * error * numeratorValue / (denominatorValue * denominatorValue);
            antiGradient[0] -= techNum;
            antiGradient[this.numerator.getDegree() + 1] -= techDen;
            for (int j = 1; j <= this.numerator.getDegree(); j++) {
                techNum *= X[i];
                antiGradient[j] -= techNum;
            }
            for (int j = 1; j <= this.denominator.getDegree(); j++) {
                techDen *= X[i];
                antiGradient[j + this.numerator.getDegree() + 1] -= techDen;
            }
        }
        return antiGradient;
    }
}
