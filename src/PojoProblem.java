public class PojoProblem {
    private String type;
    private double[] X;
    private double[] Y;
    private String solver;

    PojoProblem() {
        this.type = "None";
        this.X = null;
        this.Y = null;
        this.solver = "None";
    }

    public String getType() {
        return type;
    }

    public double[] getX() {
        return X;
    }

    public double[] getY() {
        return Y;
    }

    public String getSolver() {
        return solver;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setX(double[] x) {
        X = x;
    }

    public void setY(double[] y) {
        Y = y;
    }

    public void setSolver(String solver) {
        this.solver = solver;
    }
}
