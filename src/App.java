import com.google.gson.stream.JsonReader;
import grafica.GPlot;
import grafica.GPoint;
import grafica.GPointsArray;
import com.google.gson.*;
import processing.core.*;

import java.awt.*;
import java.io.FileReader;
import java.util.ArrayList;

public class App extends PApplet{
    public PApplet processingRef = this;
    private GPlot plot;
    private double epsilon = 0.000001;
    private double[] xx = new double[251];
    private double from;
    private double to;
    private boolean done = false;
    private String solver;

    private String type;

    Opti opti;

    public void settings() {
        size(1000, 1000);
    }

    public void setup() {
        background(0);
        frameRate(20);
        Gson gson = new Gson();
        PojoProblem problem = null;
        try {
            JsonReader reader = new JsonReader(new FileReader("problem3.json"));
            problem = gson.fromJson(reader, PojoProblem.class);
        }
        catch (Exception exception) {
            System.out.println(exception);
        }

        ParameterFunction parameterFunction = null;
        this.solver = problem.getSolver();

        this.type = problem.getType();

        switch (this.type) {
            case "Polynomial" -> parameterFunction = new ParameterPolynomial(problem.getInitialParameters());
            case "Rational" -> parameterFunction = new ParameterRational(problem.getInitialParameters(), problem.getNumeratorDegree());
            case "Sine" -> parameterFunction = new ParameterSine(problem.getInitialParameters());
            case "Sphere1" -> parameterFunction = new ParameterSphere(problem.getDotCount());
            case "Sphere2" -> parameterFunction = new ParameterSphere2(problem.getDotCount());
        }
        this.opti = new Opti(parameterFunction, problem.getX(), problem.getY());


        this.plot = new GPlot(this);
        this.plot.setPos(0,0);
        this.plot.setOuterDim(1000,1000);
        this.plot.getXAxis().setAxisLabelText("X");
        this.plot.getYAxis().setAxisLabelText("Y");
        this.plot.activatePanning();
        if (!this.type.equals("Sphere1") && !this.type.equals("Sphere2")) {
            this.opti.setStepSize(0.01);
            GPointsArray initialPoints = new GPointsArray();
            for (int i = 0; i < problem.getX().length; i++) {
                initialPoints.add(new GPoint((float) problem.getX()[i], (float) problem.getY()[i]));
            }
            this.plot.addLayer("IniPoints", initialPoints);
            this.plot.getLayer("IniPoints").setLineColor(Color.BLUE.getRGB());
            this.plot.getLayer("IniPoints").setPointColor(Color.BLUE.getRGB());
            this.xx[0] = problem.getX()[0];
            for (int i = 1; i < 251; i++) {
                this.xx[i] = this.xx[i - 1] + (problem.getX()[problem.getX().length - 1] - problem.getX()[0]) / 250;
            }
        }
        else {
            this.opti.setStepSize(Math.PI / 12);
            this.plot.setFixedXLim(true);
            this.plot.setFixedYLim(true);
            this.plot.setXLim(-1, 1);
            this.plot.setYLim(-1, 1);
        }
    }

    public void draw() {
        if (!this.done) {
            background(0);
            switch (this.solver) {
                case "HJ" -> this.done = this.opti.hookeJeevesStep(this.epsilon);
                case "GD" -> this.done = this.opti.gradientDescentOptimumStep(this.epsilon, 0.00000001);
            }
            GPointsArray currentPoints = new GPointsArray();
            if (!this.type.equals("Sphere1") && !this.type.equals("Sphere2")) {
                double[] yy = this.opti.getValueArray(this.xx);
                for (int i = 0; i < xx.length; i++) {
                    currentPoints.add(new GPoint((float) this.xx[i], (float) yy[i]));
                }
                this.plot.setPoints(currentPoints);
                this.plot.defaultDraw();
            }
            else {
                ArrayList<SphereDot> sphereDots;
                if (this.type.equals("Sphere1")) sphereDots = ((ParameterSphere) this.opti.getFunction()).getSphereDots();
                else sphereDots = ((ParameterSphere2) this.opti.getFunction()).getSphereDots();
                for (int i = 0; i < sphereDots.size(); i++) {
                    currentPoints.add(new GPoint((float) sphereDots.get(i).getX(), (float) sphereDots.get(i).getY()));
                    if (sphereDots.get(i).getZ() > 0) currentPoints.getLastPoint().setLabel("T");
                    else currentPoints.getLastPoint().setLabel("B");
                }
                this.plot.setPoints(currentPoints);
                this.plot.beginDraw();
                this.plot.drawBackground();
                this.plot.drawBox();
                this.plot.drawXAxis();
                this.plot.drawYAxis();
                this.plot.drawTopAxis();
                this.plot.drawRightAxis();
                this.plot.drawTitle();
                this.plot.drawPoints();
                this.plot.endDraw();
            }
        }
        else {
            System.out.println(this.opti.getIterationCount());
            if (!this.type.equals("Sphere1") && !this.type.equals("Sphere2")) this.plot.defaultDraw();
            else {
                this.plot.beginDraw();
                this.plot.drawBackground();
                this.plot.drawBox();
                this.plot.drawXAxis();
                this.plot.drawYAxis();
                this.plot.drawTopAxis();
                this.plot.drawRightAxis();
                this.plot.drawTitle();
                this.plot.drawPoints();
                this.plot.endDraw();
            }
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
