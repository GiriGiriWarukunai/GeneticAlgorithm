package com.geneticalgorithm;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class GeneticAlgorithm {

    private int genomLength; //Длина генома в битах

    private long generationCount; //Кол-во поколений
    private int populationSize; //Размер популяции
    private int countChromosomes;
    //private SelectionType selectionType; //Тип Селекции
    //private CrossingType crossingType; //Тип Скрещивания
    private double crossoverRate; //Вероятность мутации
    private boolean useMutation; //Использовать мутацю
    private double mutationRate; //Вероятность мутации
    private boolean findMin; //Использовать мутацю

    private int maxIterations; //максимальное количество иттераций алгоритма
    private int currentIteration; //максимальное количество иттераций алгоритма

    private double accuracy; // точность вычислений

    private int maxIterationsWithoutImprovement; //максимальное количество итераций без улучшения результата

    private int iterationsWithoutImprovement;

    private double previousBestFitness;
    private double currentBestFitness;
    private int crossoverPoint = 64; // локус разрыва хромосомы при скрещевании

    private  FitnessFunction fitnessFunction;
    private Population population;
    private Individual bestIndividual;

    private Scene scene;
    private LineChart<Integer, Double> resultChart;

    public GeneticAlgorithm(int populationSize, int countChromosomes, double accuracy, double crossoverRate, boolean useMutation, double mutationRate,
                            int maxIterations, int maxIterationsWithoutImprovement, boolean findMin,
                            FitnessFunction fitnessFunction/*, Scene scene,LineChart<Integer, Double> resultChart*/ ){
        this.populationSize = populationSize;
        this.countChromosomes = countChromosomes;
        this.accuracy = accuracy;
        this.crossoverRate = crossoverRate;
        this.useMutation = useMutation;
        this.mutationRate = mutationRate;
        this.fitnessFunction = fitnessFunction;
        this.maxIterations = maxIterations;
        this.maxIterationsWithoutImprovement = maxIterationsWithoutImprovement;

        this.findMin = findMin;
        this.population = new Population(populationSize, countChromosomes, fitnessFunction);

        this.iterationsWithoutImprovement = 0;
        this.currentIteration = 0;

        currentBestFitness = population.bestFitness();
        previousBestFitness = currentBestFitness;

        //this.scene = scene;
       // this.resultChart = resultChart;

    }

    public Pair<String, XYChart.Series<String, Double>> startAlgorithm(){

        XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();


        System.out.println(population);
        bestIndividual = population.bestIndividual();

        if (findMin){
            System.out.println((currentIteration ) + " текущее лучшее решение: " + -currentBestFitness /*+ "\n" + population.bestIndividual()+ "\n"*/);
        }
        else{
            System.out.println((currentIteration ) + " текущее лучшее решение: " + currentBestFitness /*+ "\n" + population.bestIndividual()+ "\n"*/);
        }

        while(!exitCondition()) {
            population = population.crossover(crossoverRate/*, crossoverPoint*/);

        //    System.out.println(population);

            if (useMutation) {
                population.mutation(mutationRate);
            }
           System.out.println(population);


            double currentFitness = population.bestFitness();
            if(currentFitness > currentBestFitness){ // '<' для задач минимизации !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! исправить на '>', чтобы прога работала для максимизации
                previousBestFitness = currentBestFitness;
                currentBestFitness = currentFitness;
                iterationsWithoutImprovement = 0;
                bestIndividual = population.bestIndividual();
            }
            else {
                iterationsWithoutImprovement++;
            }
            currentIteration++;


            //scene.ad.add(resultChart);

            if (findMin){
                series.getData().add(new XYChart.Data<String, Double>(Integer.toString(currentIteration), (-1)*currentBestFitness));
                System.out.println((currentIteration ) + " текущее лучшее решение: " + (-1)*currentBestFitness /*+ "\n" + population.bestIndividual()+ "\n"*/);
            }
            else{
                series.getData().add(new XYChart.Data<String, Double>(Integer.toString(currentIteration), currentBestFitness));
                System.out.println((currentIteration ) + " текущее лучшее решение: " + currentBestFitness /*+ "\n" + population.bestIndividual()+ "\n"*/);
            }

        }

       // System.out.println(population);

        Pair<String, XYChart.Series<String, Double>> result = new Pair<String, XYChart.Series<String, Double>>();

        if (findMin){
            System.out.println("Лучшее решение = " + -currentBestFitness + "\n" + bestIndividual.invertValue());
            result.setFirst("Лучшее решение = " + -currentBestFitness + "\n" + bestIndividual.invertValue());
        }
        else{
            System.out.println("Лучшее решение = " + currentBestFitness + "\n" + bestIndividual);
            result.setFirst("Лучшее решение = " + currentBestFitness + "\n" + bestIndividual);
        }

        series.setName("Поколения");
        result.setSecond(series);
        return result;
    }

    private boolean exitCondition() {
        if (Math.abs(previousBestFitness - currentBestFitness) < accuracy && Math.abs(previousBestFitness - currentBestFitness) != 0) {
            return true;
        } else if (iterationsWithoutImprovement == maxIterationsWithoutImprovement) {
            return true;
        } else if (currentIteration == maxIterations) {
            return true;
        } else {
            return false;
        }
    }



}
