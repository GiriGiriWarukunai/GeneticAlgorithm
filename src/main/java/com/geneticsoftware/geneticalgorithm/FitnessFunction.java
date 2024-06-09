package com.geneticsoftware.geneticalgorithm;

import com.geneticsoftware.util.BinaryString;

import javax.script.*;

public class FitnessFunction {

    private double[] minArgument;
    private double[] maxArgument;
    private String function;
    private boolean findMin;
    private int arguments;

    private ScriptEngineManager engineManager;
    private ScriptEngine engine;

    public FitnessFunction(String function, int arguments, double[] minArgument, double[] maxArgument, boolean findMin) {

        this.arguments = arguments;
        this.minArgument = minArgument;
        this.maxArgument = maxArgument;
        this.function = function;
        this.findMin = findMin;


        this.engineManager = new ScriptEngineManager();
        this.engine = engineManager.getEngineByName("JavaScript");

    }

    public FitnessFunction(FitnessFunction fitnessFunction) {
        this.function = fitnessFunction.getFunction();
        this.arguments = fitnessFunction.getArguments();
        this.maxArgument = fitnessFunction.getMaxArgument();
        this.minArgument = fitnessFunction.getMinArgument();
        this.findMin = fitnessFunction.isFindMin();

        this.engineManager = fitnessFunction.getEngineManager();
        this.engine = fitnessFunction.getEngine();
    }

    public ScriptEngine getEngine() {
        return engine;
    }

    public void setEngine(ScriptEngine engine) {
        this.engine = engine;
    }

    public ScriptEngineManager getEngineManager() {
        return engineManager;
    }

    public void setEngineManager(ScriptEngineManager engineManager) {
        this.engineManager = engineManager;
    }

    public double[] getMaxArgument() {
        return maxArgument;
    }

    public void setMaxArgument(double[] maxArgument) {
        this.maxArgument = maxArgument;
    }

    public double[] getMinArgument() {
        return minArgument;
    }

    public void setMinArgument(double[] minArgument) {
        this.minArgument = minArgument;
    }

    public int getArguments() { return arguments; }

    public void setArguments(int arguments) { this.arguments = arguments; }

    public String getFunction() { return function; }

    public void setFunction(String function) { this.function = function; }

    public boolean isFindMin() {
        return findMin;
    }

    public void setFindMin(boolean findMin) {
        this.findMin = findMin;
    }

    public double calculateFitness(BinaryString arguments) throws ScriptException {
        double[] x = arguments.binaryStringToDouble();
        double res;
        String fitnessFunction = function;

        for (int i = 0; i < x.length; i++) {
            //String key = "x" + i;
            engine.put("x" + (i + 1), x[i]);
        }
        res = ((Number) engine.eval(fitnessFunction)).doubleValue();
        if (findMin) {
            res *= -1;
        }
        return res;
    }

    public double calculateFitness(double[] arguments) throws ScriptException {
        double res;
        String fitnessFunction = function;

        for (int i = 0; i < arguments.length; i++) {
            //String key = "x" + i;
            engine.put("x" + (i + 1), arguments[i]);
        }
        res = ((Number) engine.eval(fitnessFunction)).doubleValue();
        if (findMin) {
            res *= -1;
        }
        return res;
    }

}
