import com.google.gson.stream.JsonReader;
import grafica.GPlot;
import grafica.GPoint;
import grafica.GPointsArray;
import com.google.gson.*;
import processing.core.*;

import java.awt.*;
import java.io.FileReader;

public class App extends PApplet{
    public PApplet processingRef = this;
    private GPlot plot;
    private double epsilon = 0.000001;
    private double[] xx = new double[251];
    private double from = -1;
    private double to = 10;
    private boolean done = false;
    private String solver;

    Opti opti;

    public void settings() {
        size(2000, 1000);
    }

    public void setup() {
        background(0);
        frameRate(1000);
        Gson gson = new Gson();
        PojoProblem problem = null;
        try {
            JsonReader reader = new JsonReader(new FileReader("problem.json"));
            problem = gson.fromJson(reader, PojoProblem.class);
        }
        catch (Exception exception) {
            System.out.println(exception);
        }

        ParameterFunction parameterFunction = null;
        this.solver = problem.getSolver();

        switch (problem.getType()) {
            case "Polynomial" -> parameterFunction = new ParameterPolynomial(problem.getInitialParameters());
            case "Rational" -> parameterFunction = new ParameterRational(problem.getInitialParameters(), problem.getNumeratorDegree());
            case "Sine" -> parameterFunction = new ParameterSine(problem.getInitialParameters());
        }
        this.opti = new Opti(parameterFunction, problem.getX(), problem.getY());
        this.opti.setStepSize(0.01);

        plot = new GPlot(this);
        plot.setPos(0,0);
        plot.setOuterDim(2000,1000);
        plot.getXAxis().setAxisLabelText("X");
        plot.getYAxis().setAxisLabelText("Y");
        plot.activatePanning();
        GPointsArray initialPoints = new GPointsArray();
        for (int i = 0; i < problem.getX().length; i++) {
            initialPoints.add(new GPoint((float) problem.getX()[i], (float) problem.getY()[i]));
        }
        plot.addLayer("IniPoints", initialPoints);
        plot.getLayer("IniPoints").setLineColor(Color.BLUE.getRGB());
        plot.getLayer("IniPoints").setPointColor(Color.BLUE.getRGB());
        this.xx[0] = this.from;
        for (int i = 1; i < 251; i++) {
            this.xx[i] = this.xx[i - 1] + (to - from) / 250;
        }
    }

    public void draw() {
        if (!this.done) {
            background(0);
            switch (this.solver) {
                case "HJ" -> this.done = this.opti.hookeJeevesStep(this.epsilon);
                case "GD" -> this.done = this.opti.gradientDescentOptimumStep(this.epsilon, 0.00000001);
            }
            double[] yy = this.opti.getValueArray(this.xx);
            GPointsArray currentPoints = new GPointsArray();
            for (int i = 0; i < xx.length; i++) {
                currentPoints.add(new GPoint((float) this.xx[i], (float) yy[i]));
            }
            plot.setPoints(currentPoints);
            plot.defaultDraw();
        }
        else {
            System.out.println(this.opti.getIterationCount());
        }
    }

//    public void mouseClicked() {
//
//    }

    public static void main(String[] args) {
        PApplet.main("App");
//        ParameterPolynomial polynomial = new ParameterPolynomial(new double[] {10, -5.5, 0.85});
//        double[] X = new double[] {-1, 0, 2, 3, 5, 10};
//        double[] Y = new double[] {18, 11, 3, 2, 6, 51};
//        Opti regression = new Opti(polynomial, X, Y);
//        ParameterRational rational = new ParameterRational(new double[] {0, 100}, new double[] {7.5, 0, 0.7});
//        double[] X = new double[] {0.546, 0.964, 1.4, 2.449, 4.38, 6.639};
//        double[] Y = new double[] {9.97, 16, 20.226, 23.474, 20, 15.246};
//        Opti regression = new Opti(rational, X, Y);
//        System.out.println(regression.hookeJeeves(0.01, 0.000001));
//        regression.setInitialParameters(new double[] {0, 100, 7.5, 0, 0.7});
//        System.out.println(regression.gradientDescentOptimum(0.0000001, 0.001));
    }
}
