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
    private double[] plotXArray = new double[251];
    private boolean done = false;
    private String solver;
    private String type;

    Regression regression;

    public void settings() {
        size(1000, 1000);
    }

    public void setup() {
        background(0);
        frameRate(10);
        Gson gson = new Gson();
        PojoProblem problem = null;
        try {
            JsonReader reader = new JsonReader(new FileReader("problem1.json"));
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
        }
        this.regression = new Regression(parameterFunction, problem.getX(), problem.getY());


        this.plot = new GPlot(this);
        this.plot.setPos(0,0);
        this.plot.setOuterDim(1000,1000);
        this.plot.getXAxis().setAxisLabelText("X");
        this.plot.getYAxis().setAxisLabelText("Y");
        this.plot.activatePanning();
        this.regression.setStepSize(0.01);
        GPointsArray initialPoints = new GPointsArray();
        for (int i = 0; i < problem.getX().length; i++) {
            initialPoints.add(new GPoint((float) problem.getX()[i], (float) problem.getY()[i]));
        }
        this.plot.addLayer("IniPoints", initialPoints);
        this.plot.getLayer("IniPoints").setLineColor(Color.BLUE.getRGB());
        this.plot.getLayer("IniPoints").setPointColor(Color.BLUE.getRGB());
        this.plotXArray[0] = problem.getX()[0];
        for (int i = 1; i < 251; i++) {
            this.plotXArray[i] = this.plotXArray[i - 1] + (problem.getX()[problem.getX().length - 1] - problem.getX()[0]) / 250;
        }
    }

    public void draw() {
        if (!this.done) {
            background(0);
            switch (this.solver) {
                case "HJ" -> this.done = this.regression.hookeJeevesStep(this.epsilon);
                case "GD" -> this.done = this.regression.gradientDescentOptimumStep(this.epsilon, 0.00000001);
            }
            GPointsArray currentPoints = new GPointsArray();
            double[] plotYArray = this.regression.getValueArray(this.plotXArray);
            for (int i = 0; i < plotXArray.length; i++) {
                currentPoints.add(new GPoint((float) this.plotXArray[i], (float) plotYArray[i]));
            }
            this.plot.setPoints(currentPoints);
            this.plot.defaultDraw();
        }
        else {
            System.out.println(this.regression.getIterationCount());
            this.plot.defaultDraw();
        }
    }

    public static void main(String[] args) {
        PApplet.main("App");
    }
}
