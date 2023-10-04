public class Opti {
    private ParameterFunction function;
    private double[] X;
    private double[] Y;
    private double currentStepSize;
    private double currentSqError;
    private double lastSqError;
    private int iterationCount;

    Opti() {
        this.function = null;
        this.X = null;
        this.Y = null;
        this.currentStepSize = 0;
        this.currentSqError = Double.MAX_VALUE;
        this.lastSqError = Double.MAX_VALUE;
        this.iterationCount = 0;
    }

    Opti(ParameterFunction function, double[] x, double[] y) {
        this.function = function;
        this.X = x;
        this.Y = y;
        this.currentStepSize = 0;
        this.currentSqError = function.squareError(x, y);
        this.lastSqError = Double.MAX_VALUE;
        this.iterationCount = 0;
    }

    public ParameterFunction getFunction() {
        return function;
    }

    public double[] getParameters() {
        return this.function.getParameters();
    }

    public double[] getX() {
        return X;
    }

    public double[] getY() {
        return Y;
    }

    public double getCurrentStepSize() {
        return currentStepSize;
    }

    public double getCurrentSqError() {
        return currentSqError;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public double[] getValueArray(double[] x) {
        return this.function.getValueArray(x);
    }

    public void setFunction(ParameterFunction function) {
        this.function = function;
        reset();
    }

    public void setInitialParameters(double[] parameters) {
        this.function.setParameters(parameters);
        reset();
    }

    public void setX(double[] x) {
        this.X = x;
        reset();
    }

    public void setY(double[] y) {
        this.Y = y;
        reset();
    }

    public void setStepSize(double currentStepSize) {
        this.currentStepSize = currentStepSize;
    }

    public void reset() {
        this.currentStepSize = 0;
        this.currentSqError = this.function.squareError(this.X, this.Y);
        this.lastSqError = Double.MAX_VALUE;
        this.iterationCount = 0;
    }

    public int hookeJeeves(double stepSize, double epsilon) {
        int iterationCount = 0;
        double currentStepSize = stepSize;
        double currentSqError = this.function.squareError(this.X, this.Y);
        double lastSqError = Double.MAX_VALUE;
        while (lastSqError - currentSqError > epsilon) {
            boolean improved = false;

            for (int i = 0; i < this.function.getParameterCount(); i++) {
                this.function.adjustParameter(i, currentStepSize);
                double newSqError = this.function.squareError(this.X, this.Y);
                if (newSqError > currentSqError) {
                    this.function.adjustParameter(i, -2 * currentStepSize);
                    newSqError = this.function.squareError(this.X, this.Y);
                    if (newSqError > currentSqError) {
                        this.function.adjustParameter(i, currentStepSize);
                    }
                    else {
                        lastSqError = currentSqError;
                        currentSqError = newSqError;
                        improved = true;
                    }
                }
                else {
                    lastSqError = currentSqError;
                    currentSqError = newSqError;
                    improved = true;
                }
            }
            if (!improved) currentStepSize /= 2;
            iterationCount++;
        }
        return iterationCount;
    }

    public boolean hookeJeevesStep(double epsilon) {
        boolean improved = false;

        for (int i = 0; i < this.function.getParameterCount(); i++) {
            double initialParameterValue = this.function.getParameter(i);
            this.function.adjustParameter(i, this.currentStepSize);
            double newSqError = this.function.squareError(this.X, this.Y);
            if (newSqError >= this.currentSqError) {
                this.function.adjustParameter(i, -2 * this.currentStepSize);
                newSqError = this.function.squareError(this.X, this.Y);
                if (newSqError >= this.currentSqError) {
                    this.function.setParameter(i, initialParameterValue);
                }
                else {
                    this.lastSqError = this.currentSqError;
                    this.currentSqError = newSqError;
                    improved = true;
                }
            }
            else {
                this.lastSqError = this.currentSqError;
                this.currentSqError = newSqError;
                improved = true;
            }
        }
        if (!improved) this.currentStepSize /= 2;
        this.iterationCount++;
        return lastSqError - currentSqError <= epsilon;
    }

    public int gradientDescentOptimum(double epsilon, double innerEpsilon) {
        double[] antiGradient = null;

        while (this.lastSqError - this.currentSqError > epsilon) {
            double[] currentIterationInitialParameters = this.function.getParameters();
            antiGradient = this.function.getAntiGradient(this.X, this.Y);
            double a = 0;
            double b = 1;
            double bestSqError = this.currentSqError;
            double[] bestSqErrorParameters = null;

            double delta = (b - a) / 100;

            double sqErrorLower = Double.MAX_VALUE;
            double sqErrorHigher = Double.MAX_VALUE;
            while (b - a > innerEpsilon) {
                for (int i = 0; i < this.function.getParameterCount(); i++) {
                    this.function.adjustParameter(i, (-delta + (a + b) / 2) * antiGradient[i]);
                }
                sqErrorLower = this.function.squareError(this.X, this.Y);
                if (sqErrorLower < bestSqError) {
                    bestSqError = sqErrorLower;
                    bestSqErrorParameters = this.function.getParameters();
                }
                for (int i = 0; i < this.function.getParameterCount(); i++) {
                    this.function.adjustParameter(i, 2 * delta * antiGradient[i]);
                }
                sqErrorHigher = this.function.squareError(this.X, this.Y);
                if (sqErrorHigher < bestSqError) {
                    bestSqError = sqErrorHigher;
                    bestSqErrorParameters = this.function.getParameters();
                }
                this.function.setParameters(currentIterationInitialParameters);
                if (sqErrorLower < sqErrorHigher) {
                    b = delta + (a + b) / 2;
                } else {
                    a = -delta + (a + b) / 2;
                }
                delta = (b - a) / 100;
            }

            double lambda =  (a + b) / 2;
            for (int i = 0; i < this.function.getParameterCount(); i++) {
                this.function.adjustParameter(i, lambda * antiGradient[i]);
            }
            double newSqError = this.function.squareError(this.X, this.Y);
            if (newSqError > bestSqError) {
                this.function.setParameters(bestSqErrorParameters);
                newSqError = bestSqError;
            }

            if (newSqError > this.currentSqError) {
                this.function.setParameters(currentIterationInitialParameters);
                return -1;
            }
            else {
                this.lastSqError = this.currentSqError;
                this.currentSqError = newSqError;
            }
            this.iterationCount++;
        }
        return this.iterationCount;
    }

    public boolean gradientDescentOptimumStep(double epsilon, double innerEpsilon) {
        double[] currentIterationInitialParameters = this.function.getParameters();
        double[] antiGradient = this.function.getAntiGradient(this.X, this.Y);
        double a = 0;
        double b = 1;
        double bestSqError = this.currentSqError;
        double[] bestSqErrorParameters = null;

        double delta = (b - a) / 100;

        double sqErrorLower = Double.MAX_VALUE;
        double sqErrorHigher = Double.MAX_VALUE;
        while (b - a > innerEpsilon) {
            for (int i = 0; i < this.function.getParameterCount(); i++) {
                this.function.adjustParameter(i, (-delta + (a + b) / 2) * antiGradient[i]);
            }
            sqErrorLower = this.function.squareError(this.X, this.Y);
            if (sqErrorLower < bestSqError) {
                bestSqError = sqErrorLower;
                bestSqErrorParameters = this.function.getParameters();
            }
            for (int i = 0; i < this.function.getParameterCount(); i++) {
                this.function.adjustParameter(i, 2 * delta * antiGradient[i]);
            }
            sqErrorHigher = this.function.squareError(this.X, this.Y);
            if (sqErrorHigher < bestSqError) {
                bestSqError = sqErrorHigher;
                bestSqErrorParameters = this.function.getParameters();
            }
            this.function.setParameters(currentIterationInitialParameters);
            if (sqErrorLower < sqErrorHigher) {
                b = delta + (a + b) / 2;
            } else {
                a = -delta + (a + b) / 2;
            }
            delta = (b - a) / 100;
        }

        double lambda =  (a + b) / 2;
        for (int i = 0; i < this.function.getParameterCount(); i++) {
            this.function.adjustParameter(i, lambda * antiGradient[i]);
        }
        double newSqError = this.function.squareError(this.X, this.Y);
        if (newSqError > bestSqError && bestSqErrorParameters != null) {
            this.function.setParameters(bestSqErrorParameters);
            newSqError = bestSqError;
        }


        if (newSqError > this.currentSqError) {
            this.function.setParameters(currentIterationInitialParameters);
            return true;
        }
        else {
            this.lastSqError = this.currentSqError;
            this.currentSqError = newSqError;
        }
        this.iterationCount++;

        return lastSqError - currentSqError <= epsilon;
    }
}
