public abstract class ParameterFunction {
    protected String type;
    protected int parameterCount;

    ParameterFunction() {
        this.type = "None";
        this.parameterCount = 0;
    }

    public String getType() {
        return type;
    }

    public int getParameterCount() {
        return parameterCount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public abstract double[] getParameters();

    public abstract void setParameters(double[] parameters);

    public abstract void adjustParameter(int i, double value);

    public abstract void setParameter(int i, double value);

    public abstract double evaluateAt(double x);

    public abstract double[] squareErrorParameterAntiGradient(double[] X, double[] Y);

    public double[] getValueArray(double[] x) {
        double[] values = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            values[i] = evaluateAt(x[i]);
        }
        return values;
    }

    public double squareError(double[] X, double[] Y) {
        double sqError = 0;
        for (int i = 0; i < X.length; i++) {
            sqError += (evaluateAt(X[i]) - Y[i]) * (evaluateAt(X[i]) - Y[i]);
        }
        return sqError;
    }
}
