import java.util.ArrayList;

public class ParameterSphere2 extends ParameterFunction {
    private ArrayList<SphereDot> sphereDots;

    ParameterSphere2() {
        this.type = "Sphere";
        this.parameterCount = 0;
        this.sphereDots = new ArrayList<>();
    }

    ParameterSphere2(ArrayList<SphereDot> sphereDots) {
        this.type = "Sphere";
        this.parameterCount = sphereDots.size() * 2 - 3;
        this.sphereDots = sphereDots;
        setFirstDots();
    }

    ParameterSphere2(double[] phi, double[] omega) {
        this.type = "Sphere";
        this.parameterCount = phi.length * 2 - 3;
        this.sphereDots = new ArrayList<>();
        for(int i = 0; i < phi.length; i++) {
            this.sphereDots.add(new SphereDot(phi[i], omega[i]));
        }
        setFirstDots();
    }

    ParameterSphere2(int dotCount) {
        this.type = "Sphere";
        this.parameterCount = dotCount * 2 - 3;
        this.sphereDots = new ArrayList<>();
        for (int i = 0; i < dotCount; i++) {
            this.sphereDots.add(SphereDot.randomDot());
        }
        setFirstDots();
    }

    public ArrayList<SphereDot> getSphereDots() {
        return sphereDots;
    }

    public int getDotCount() { return this.sphereDots.size(); }

    public void setSphereDots(ArrayList<SphereDot> sphereDots) {
        this.sphereDots = sphereDots;
        this.parameterCount = sphereDots.size() * 2 - 3;
        setFirstDots();
    }

    private void setFirstDots() {
        this.sphereDots.get(0).setTheta(0);
        this.sphereDots.get(0).setPhi(0);
        this.sphereDots.get(1).setPhi(0);
    }

    @Override
    public double[] getParameters() {
        double[] parameters = new double[this.parameterCount];
        parameters[0] = this.sphereDots.get(1).getTheta();
        for (int i = 2; i < this.sphereDots.size(); i++) {
            parameters[2 * i - 3] = this.sphereDots.get(i).getPhi();
            parameters[2 * i - 2] = this.sphereDots.get(i).getTheta();
        }
        return parameters;
    }

    @Override
    public double getParameter(int i) {
        if (i == 0) return this.sphereDots.get(1).getTheta();
        else {
            int index = (i - 1) / 2 + 2;
            if (i % 2 == 1) return this.sphereDots.get(index).getPhi();
            else return this.sphereDots.get(index).getTheta();
        }
    }

    @Override
    public void setParameters(double[] parameters) {
        this.sphereDots.get(1).setTheta(parameters[0]);
        for (int i = 2; i < this.sphereDots.size(); i++) {
            this.sphereDots.get(i).setPhi(parameters[2 * i - 3]);
            this.sphereDots.get(i).setTheta(parameters[2 * i - 2]);
        }
    }

    @Override
    public void adjustParameter(int i, double value) {
        if (i == 0) this.sphereDots.get(1).adjustTheta(value);
        else {
            int index = (i - 1) / 2 + 2;
            if (i % 2 == 1) this.sphereDots.get(index).adjustPhi(value);
            else this.sphereDots.get(index).adjustTheta(value);
        }
    }

    @Override
    public void setParameter(int i, double value) {
        if (i == 0) this.sphereDots.get(1).setTheta(value);
        else {
            int index = (i - 1) / 2 + 2;
            if (i % 2 == 1) this.sphereDots.get(index).setPhi(value);
            else this.sphereDots.get(index).setTheta(value);
        }
    }

    @Override
    public double squareError(double[] X, double[] Y) {
        double squareError = Double.MAX_VALUE;
        for (int i = 0; i < this.sphereDots.size(); i++) {
            for (int j = i + 1; j < this.sphereDots.size(); j++) {
                double rho = 5 * this.sphereDots.get(i).distanseTo(this.sphereDots.get(j));
                if (rho < squareError) squareError = rho;
            }
        }
        return -squareError;
    }

    @Override
    public double[] getAntiGradient(double[] X, double[] Y) {
        return null;
    }

}
