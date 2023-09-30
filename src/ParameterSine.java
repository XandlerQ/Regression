public class ParameterSine extends ParameterFunction {
    private double k;
    private double omega;
    private double phi;

    ParameterSine() {
        this.type = "Sine";
        this.parameterCount = 3;
        this.k = 1.;
        this.omega = 1.;
        this.phi = 0.;
    }

    ParameterSine(double k, double omega, double phi) {
        this.type = "Sine";
        this.parameterCount = 3;
        this.k = k;
        this.omega = omega;
        this.phi = phi;
    }

    public double getK() {
        return k;
    }

    public double getOmega() {
        return omega;
    }

    public double getPhi() {
        return phi;
    }

    public void setK(double k) {
        this.k = k;
    }

    public void setOmega(double omega) {
        this.omega = omega;
    }

    public void setPhi(double phi) {
        this.phi = phi;
    }
}
