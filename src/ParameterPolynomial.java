public class ParameterPolynomial extends ParameterFunction {
    private double[] coefficients;
    private int degree;

    ParameterPolynomial() {
        this.type = "Polynomial";
        this.coefficients = null;
        this.degree = -1;
        this.parameterCount = 0;
    }

    ParameterPolynomial(double[] coefficients) {
        this.type = "Polynomial";
        this.coefficients = coefficients;
        this.degree = coefficients.length - 1;
        this.parameterCount = this.degree + 1;
        normalize();
    }

    ParameterPolynomial(ParameterPolynomial polynomial) {
        this.type = "Polynomial";
        this.degree = polynomial.degree;
        this.coefficients = new double[this.degree + 1];
        this.parameterCount = this.degree + 1;
        System.arraycopy(polynomial.coefficients, 0, this.coefficients, 0, this.degree + 1);
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    public int getDegree() {
        return degree;
    }

    public void setCoefficients(double[] coefficients) {
        this.coefficients = coefficients;
        this.degree = coefficients.length - 1;
        this.parameterCount = this.degree + 1;
        normalize();
    }

    @Override
    public double[] getParameters() {
        double[] parameters = new double[this.degree + 1];
        System.arraycopy(this.coefficients, 0, parameters, 0, this.degree + 1);
        return parameters;
    }

    @Override
    public double getParameter(int i) {
        return this.coefficients[i];
    }

    @Override
    public void setParameters(double[] parameters) {
        if (this.degree + 1 >= 0) System.arraycopy(parameters, 0, this.coefficients, 0, this.degree + 1);
    }

    @Override
    public void adjustParameter(int i, double value) {
        this.coefficients[i] += value;
    }

    @Override
    public void setParameter(int i, double value) {
        this.coefficients[i] = value;
    }

    void normalize() {
        if (this.degree == -1) return;
        int position = this.degree;
        while (this.coefficients[position] == 0) {
            position--;
        }

        if (position == degree) return;
        double[] newCoefficients = new double[position + 1];
        System.arraycopy(this.coefficients, 0, newCoefficients, 0, position + 1);
        this.coefficients = newCoefficients;
        this.degree = position;
    }

    @Override
    public double evaluateAt(double x) {
        if (this.degree == -1) return 0;
        double argExp = x;
        double value = this.coefficients[0];
        for(int i = 1; i <= this.degree; i++) {
            value += this.coefficients[i] * argExp;
            argExp *= x;
        }
        return value;
    }

    @Override
    public double[] getAntiGradient(double[] X, double[] Y) {
        double[] antiGradient = new double[this.parameterCount];
        for (int i = 0; i < X.length; i++) {
            double error = evaluateAt(X[i]) - Y[i];
            double tech = 2 * error;
            antiGradient[0] -= tech;
            for (int j = 1; j <= this.degree; j++) {
                tech *= X[i];
                antiGradient[j] -= tech;
            }
        }
        return antiGradient;
    }

}
