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

    ParameterSine(double[] parameters) {
        this.type = "Sine";
        this.parameterCount = 3;
        this.k = parameters[0];
        this.phi = parameters[1];
        this.omega = parameters[2];
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
    public double[] getParameters() {
        double[] parameters = new double[this.parameterCount];
        parameters[0] = this.k;
        parameters[1] = this.phi;
        parameters[2] = this.omega;
        return parameters;
    }

    @Override
    public double getParameter(int i) {
        switch (i) {
            case 0 -> {
                return this.k;
            }
            case 1 -> {
                return this.phi;
            }
            case 2 -> {
                return this.omega;
            }
        }
        return 0;
    }

    @Override
    public void setParameters(double[] parameters) {
        this.k = parameters[0];
        this.phi = parameters[1];
        this.omega = parameters[2];
    }

    @Override
    public void adjustParameter(int i, double value) {
        switch (i) {
            case 0 -> this.k += value;
            case 1 -> this.phi += value;
            case 2 -> this.omega += value;
        }
    }

    @Override
    public void setParameter(int i, double value) {
        switch (i) {
            case 0 -> this.k = value;
            case 1 -> this.phi = value;
            case 2 -> this.omega = value;
        }
    }

    @Override
    public double evaluateAt(double x) {
        return this.k * Math.sin(this.omega * x + this.phi);
    }

    @Override
    public double[] getAntiGradient(double[] X, double[] Y) {
        double[] antiGradient = new double[this.parameterCount];
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
