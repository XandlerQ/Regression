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

    public abstract void setParameters(double[] parameters);

    public abstract double evaluateAt(double x);

    public abstract double[] squareErrorParameterAntiGradient(double[] X, double[] Y);
}
