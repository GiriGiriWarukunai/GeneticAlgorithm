package com.geneticalgorithm;

public class FitnessFunction {

    private double minArgument = -5.12;
    private double maxArgument = 5.12;

//    private double minArgument = -4.5;
//    private double maxArgument = 4.5;

//    private double minArgument = Double.MIN_VALUE;
//    private double maxArgument = Double.MAX_VALUE;

//    private double minArgument = -10;
//    private double maxArgument = 10;

//    private double minArgument = -1000000000;
//    private double maxArgument = 1000000000;

//    private int arguments = 2;
    private int arguments = 2;



    public double getMaxArgument() {
        return maxArgument;
    }

    public void setMaxArgument(double maxArgument) {
        this.maxArgument = maxArgument;
    }

    public double getMinArgument() {
        return minArgument;
    }

    public void setMinArgument(double minArgument) {
        this.minArgument = minArgument;
    }

    public int getArguments() { return arguments; }

    public void setArguments(int arguments) { this.arguments = arguments; }

    // private Function<Individual, Double> fitnessFunction ;

    // Конструктор
//   public FitnessFunction(Function<Individual, Double> function) {
//        this.fitnessFunction = function;
//    }

    // Метод для вычисления значения функции приспособленности для данной особи
    public double calculateFitness(Individual individual) {

//        Pair<Double, Double> tmp = new Pair<Double, Double>(individual.getChromosomes()[0].calculateValue()[0], individual.getChromosomes()[0].calculateValue()[0]);
        double[] x = individual.getChromosomes()[0].calculateValue();

        double x1 = x[0];
        double x2 = x[1];
//        double x3 = x[2];

//        double x = individual.getChromosomes()[0].calculateValue()[0];

//        return -1 * sphere(x);

        return -1 * rastrigin(x1, x2);

//        return -1 * beale(x1, x2);

//        return -1 * rosenbrock(x1, x2);

//        return -1 * crossInTray(x1, x2);

        //return x ;
    }

    public double crossInTray(double x, double y) {
        double exponent = Math.abs(100 - Math.sqrt(x * x + y * y) / Math.PI);
        double sinProduct = Math.sin(x) * Math.sin(y) * Math.exp(exponent);
        return -0.0001 * Math.pow(Math.abs(sinProduct) + 1, 0.1);
    }

    public double sphere(double[] x) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(x[i], 2);
        }
        return sum;
    }

    public double rastrigin(double x, double y) {
        // Заданные параметры
        int n = 2;
        double A = 10.0;

        // Функция Растригина

        return A * n + Math.pow(x, 2) - A * Math.cos(2 * Math.PI * x) + Math.pow(y, 2) - A * Math.cos(2 * Math.PI * y);
    }

    public static double rastrigin(double x1, double x2, double x3) {
        double A = 10.0;
        double sum = Math.pow(x1, 2) - A * Math.cos(2 * Math.PI * x1) +
                Math.pow(x2, 2) - A * Math.cos(2 * Math.PI * x2) +
                Math.pow(x3, 2) - A * Math.cos(2 * Math.PI * x3);
        return A * 3 + sum;
    }

    public double beale(double x, double y) {
        double term1 = Math.pow(1.5 - x + x * y, 2);
        double term2 = Math.pow(2.25 - x + x * Math.pow(y, 2), 2);
        double term3 = Math.pow(2.625 - x + x * Math.pow(y, 3), 2);

        return term1 + term2 + term3;
    }

    // Функция Розенброка для n = 2
    public double rosenbrock(double x1, double x2) {
        return 100 * Math.pow(x2 - x1 * x1, 2) + Math.pow(x1 - 1, 2);
    }

    public double rosenbrock(double x1, double x2, double x3) {
        double term1 = Math.pow(1 - x1, 2) + 100 * Math.pow(x2 - x1 * x1, 2);
        double term2 = Math.pow(1 - x2, 2) + 100 * Math.pow(x3 - x2 * x2, 2);
        return term1 + term2;
    }

    public double powx2(double x) {
        return Math.pow(x, 2);
    }


}
