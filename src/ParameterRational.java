public class ParameterRational extends ParameterFunction {
    private ParameterPolynomial numerator;
    private ParameterPolynomial denominator;

    ParameterRational() {
        this.type = "Rational";
        this.parameterCount = 0;
        this.numerator = null;
        this.denominator = null;
    }

    ParameterRational(double[] numerator, double[] denominator) {
        this.type = "Rational";
        this.numerator = new ParameterPolynomial(numerator);
        this.denominator = new ParameterPolynomial(denominator);
        this.parameterCount = this.numerator.getParameterCount() + this.denominator.getParameterCount();
    }

    ParameterRational(double[] parameters, int numeratorDegree) {
        this.type = "Rational";
        double[] numerator = new double[numeratorDegree + 1];
        double[] denominator = new double[parameters.length - numeratorDegree - 1];
        System.arraycopy(parameters, 0, numerator, 0, numerator.length);
        for (int i = 0; i < denominator.length; i++) denominator[i] = parameters[i + numeratorDegree + 1];
        this.numerator = new ParameterPolynomial(numerator);
        this.denominator = new ParameterPolynomial(denominator);
        this.parameterCount = this.numerator.getParameterCount() + this.denominator.getParameterCount();
    }

    public ParameterPolynomial getNumerator() {
        return numerator;
    }

    public ParameterPolynomial getDenominator() {
        return denominator;
    }

    public void setNumerator(ParameterPolynomial numerator) {
        this.numerator = numerator;
        this.parameterCount = numerator.getParameterCount() + this.denominator.getParameterCount();
    }

    public void setDenominator(ParameterPolynomial denominator) {
        this.denominator = denominator;
        this.parameterCount = this.numerator.getParameterCount() + denominator.getParameterCount();
    }

    @Override
    public double[] getParameters() {
        double[] parameters = new double[this.parameterCount];
        for (int i = 0; i <= this.numerator.getDegree(); i++) {
            parameters[i] = this.numerator.getCoefficients()[i];
        }
        for (int i = 0; i <= this.denominator.getDegree(); i++) {
            parameters[i + this.numerator.getDegree() + 1] = this.denominator.getCoefficients()[i];
        }
        return parameters;
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
    public void adjustParameter(int i, double value) {
        if (i <= this.numerator.getDegree()) {
            this.numerator.adjustParameter(i, value);
        }
        else {
            int position = i - this.numerator.getDegree() - 1;
            this.denominator.adjustParameter(position, value);
        }
    }

    @Override
    public void setParameter(int i, double value) {
        if (i <= this.numerator.getDegree()) {
            this.numerator.setParameter(i, value);
        }
        else {
            int position = i - this.numerator.getDegree() - 1;
            this.denominator.setParameter(position, value);
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
        double[] antiGradient = new double[this.parameterCount];
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
