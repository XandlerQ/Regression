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
        normalize();
    }

    void normalize() {
        if (this.degree == -1) return;
        int position = this.degree;
        while(this.coefficients[position] == 0) {
            position--;
        }

        if (position == degree) return;
        double[] newCoefficients = new double[position + 1];
        System.arraycopy(this.coefficients, 0, newCoefficients, 0, position + 1);
        this.coefficients = newCoefficients;
        this.degree = position;
    }
}
