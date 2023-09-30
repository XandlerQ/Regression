public class ParameterSine extends ParameterFunction {
    private double k;
    private double phi;
    private double omega;

    ParameterSine() {
        this.type = "Sine";
        this.parameterCount = 3;
        this.k = 1.;
        this.phi = 0.;
        this.omega = 1.;
    }

    ParameterSine(double k, double omega, double phi) {
        this.type = "Sine";
        this.parameterCount = 3;
        this.k = k;
        this.phi = phi;
        this.omega = omega;
    }

    public double getK() {
        return k;
    }

    public double getPhi() {
        return phi;
    }

    public double getOmega() {
        return omega;
    }

    public void setK(double k) {
        this.k = k;
    }

    public void setPhi(double phi) {
        this.phi = phi;
    }

    public void setOmega(double omega) {
        this.omega = omega;
    }

    @Override
    public void setParameters(double[] parameters) {
        this.k = parameters[0];
        this.phi = parameters[1];
        this.omega = parameters[2];
    }

    @Override
    public double evaluateAt(double x) {
        return this.k * Math.sin(this.omega * x + this.phi);
    }

    @Override
    public double[] squareErrorParameterAntiGradient(double[] X, double[] Y) {
        double[] antiGradient = new double[3];
        for (int i = 0; i < X.length; i++) {
            double error = evaluateAt(X[i]) - Y[i];
            antiGradient[0] -= 2 * error * Math.sin(this.omega * X[i] + this.phi);
            double tech = 2 * error * this.k * Math.cos(this.omega * X[i] + this.phi);
            antiGradient[1] -= tech;
            antiGradient[2] -= tech * X[i];
        }
        return antiGradient;
    }
}
