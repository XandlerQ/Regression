public class ParameterLinear extends ParameterFunction {
    private double a;
    private double b;

    ParameterLinear() {
        this.type = "Linear";
        this.parameterCount = 2;
        this.a = 0.;
        this.b = 0.;
    }

    ParameterLinear(double a, double b) {
        this();
        this.a = a;
        this.b = b;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }
}
