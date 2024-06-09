package com.geneticsoftware.geneticalgorithm;

import com.geneticsoftware.geneticalgorithm.crossover.*;
import com.geneticsoftware.geneticalgorithm.mutation.*;
import com.geneticsoftware.geneticalgorithm.selection.*;
import com.geneticsoftware.util.*;
import javafx.scene.chart.XYChart;

import javax.script.ScriptException;
import java.util.ArrayList;

public class GeneticAlgorithm {

    private int populationSize; //Размер популяции
    private int countChromosomes;
    private ChromosomeType chromosomeType;
    private SelectionType selectionType; //Тип Селекции
    private int size;
    private CrossoverType crossoverType; //Тип Скрещивания
    private double alpha;
    private int k;
    private MutationType mutationType;
    private Selection selectionMethod;
    private Crossover crossoverMethod;
    private Mutation mutationMethod;
    private double crossoverRate; //Вероятность мутации
    private double mutationRate; //Вероятность мутации
    private boolean findMin; //Использовать мутацю
    private boolean elitist;

    private int maxIterations; //максимальное количество иттераций алгоритма
    private int currentIteration; //максимальное количество иттераций алгоритма

    private double accuracy; // точность вычислений
    private int maxIterationsWithoutImprovement; //максимальное количество итераций без улучшения результата
    private int iterationsWithoutImprovement;

    private double previousBestFitness;
    private double currentBestFitness;

    private FitnessFunction fitnessFunction;
    private Population population;
    private Individual bestIndividual;


    public GeneticAlgorithm(int populationSize, int countChromosomes, double accuracy, double crossoverRate, double mutationRate,
                            int maxIterations, int maxIterationsWithoutImprovement, boolean findMin,
                            ChromosomeType chromosomeType, SelectionType selectionType, CrossoverType crossoverType, MutationType mutationType,
                            boolean elitist, FitnessFunction fitnessFunction, int size, double alpha, int k ) throws ScriptException {
        this.populationSize = populationSize;
        this.countChromosomes = countChromosomes;
        this.accuracy = accuracy;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.fitnessFunction = fitnessFunction;
        this.maxIterations = maxIterations;
        this.maxIterationsWithoutImprovement = maxIterationsWithoutImprovement;

        this.size = size;
        this.alpha = alpha;
        this.k = k;

        this.findMin = findMin;
        this.chromosomeType = chromosomeType;
        this.selectionType = selectionType;
        this.crossoverType = crossoverType;
        this.mutationType = mutationType;
        this.elitist = elitist;


        this.population = new Population(populationSize, countChromosomes, chromosomeType, fitnessFunction);

        this.iterationsWithoutImprovement = 0;
        this.currentIteration = 0;

        currentBestFitness = population.bestFitness();
        previousBestFitness = currentBestFitness;

        this.selectionType = selectionType;
        this.crossoverType = crossoverType;
        this.mutationType = mutationType;

        switch (selectionType){
            case ROULETTEWHEEL -> selectionMethod = new SelectionRouletteWheel();
            case RANK -> selectionMethod = new SelectionRank();
            case TOURNAMENT -> selectionMethod = new SelectionTournament();
            case TRUNCATION -> selectionMethod = new SelectionTrancation();
        }
        switch (crossoverType){
            case ONEPOINT -> crossoverMethod = new CrossoverOnePoint();
            case TWOPOINT -> crossoverMethod = new CrossoverTwoPoint();
            case KPOINT -> crossoverMethod = new CrossoverKPoint();
            case UNIFORM -> crossoverMethod = new CrossoverUniform();
            case INTERMEDIATE -> crossoverMethod = new CrossoverIntermediate();
            case LINE -> crossoverMethod = new CrossoverLine();
            case SBX -> crossoverMethod = new CrossoverSBX();
        }
        switch (mutationType){
            case ONEBITINVERTION -> mutationMethod = new MutationOneBitInvertion();
            case EVERYBITINVERTION -> mutationMethod = new MutationEveryBitInvertion();
            case PARTINVERTATION -> mutationMethod = new MutationPartInvertion();
            case PARTRANDOM -> mutationMethod = new MutationPartRandom();
            case ONEREAL -> mutationMethod = new MutationOneReal();
        }

    }

    public Pair<String, XYChart.Series<String, Double>> startAlgorithm() throws ScriptException {

        XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();

        System.out.println(population);
        bestIndividual = population.bestIndividual();

        if (findMin){
            series.getData().add(new XYChart.Data<String, Double>(Integer.toString(currentIteration), (-1)*currentBestFitness));
            System.out.println((currentIteration ) + " текущее лучшее решение: " + -currentBestFitness /*+ "\n" + population.bestIndividual()+ "\n"*/);
        }
        else{
            series.getData().add(new XYChart.Data<String, Double>(Integer.toString(currentIteration), currentBestFitness));
            System.out.println((currentIteration ) + " текущее лучшее решение: " + currentBestFitness /*+ "\n" + population.bestIndividual()+ "\n"*/);
        }


        while(!exitCondition()) {

            ArrayList<Individual> parents = new ArrayList<Individual>();
            if(selectionType == SelectionType.TOURNAMENT ||
                    selectionType == SelectionType.RANK) {
               parents = selectionMethod.selection(population);
            }
            else if (selectionType == SelectionType.TRUNCATION){
                parents = selectionMethod.selection(population, size);
            }

           System.out.println("выбранные родители:\n" + parents.toString());

           if(crossoverType == CrossoverType.ONEPOINT ||
                   crossoverType == CrossoverType.TWOPOINT) {
               population = crossoverMethod.crossover(parents, fitnessFunction, crossoverRate);
           }
           else if(crossoverType == CrossoverType.INTERMEDIATE ||
                   crossoverType == CrossoverType.LINE ||
                   crossoverType == CrossoverType.SBX ||
                   crossoverType == CrossoverType.UNIFORM){
               population = crossoverMethod.crossover(parents, fitnessFunction, crossoverRate, alpha);
           }
           else if (crossoverType == CrossoverType.KPOINT){
               population = crossoverMethod.crossover(parents, fitnessFunction, crossoverRate, k);
           }
            System.out.println("Популяция после скрещивания\n" + population);

            if (mutationRate > 0) {
                population = mutationMethod.mutation(population, mutationRate);
            }

           System.out.println("Популяция после мутации\n" + population);

            double currentFitness = population.bestFitness();
            if(currentFitness > currentBestFitness){
                previousBestFitness = currentBestFitness;
                currentBestFitness = currentFitness;
                iterationsWithoutImprovement = 0;
                bestIndividual = population.bestIndividual();
            }
            else {
                iterationsWithoutImprovement++;
            }
            currentIteration++;

            if (findMin){
                series.getData().add(new XYChart.Data<String, Double>(Integer.toString(currentIteration), -currentBestFitness));
                System.out.println((currentIteration ) + " текущее лучшее решение: " + -currentBestFitness /*+ "\n" + population.bestIndividual()+ "\n"*/);
            }
            else{
                series.getData().add(new XYChart.Data<String, Double>(Integer.toString(currentIteration), currentBestFitness));
                System.out.println((currentIteration ) + " текущее лучшее решение: " + currentBestFitness /*+ "\n" + population.bestIndividual()+ "\n"*/);
            }

        }

       // System.out.println(population);

        Pair<String, XYChart.Series<String, Double>> result = new Pair<String, XYChart.Series<String, Double>>();

        if (findMin){
            System.out.println("Количество поколений: " + currentIteration + "\n\nЛучшее решение = " + -currentBestFitness + "\n" + bestIndividual/*.invertValue()*/);
            result.setFirst("Количество поколений: " + currentIteration + "\n\nЛучшее решение = " + -currentBestFitness + "\n" + bestIndividual/*.invertValue()*/);
        }
        else{
            System.out.println("Количество поколений: " + currentIteration + "\n\nЛучшее решение = " + currentBestFitness + "\n" + bestIndividual);
            result.setFirst("Количество поколений: " + currentIteration + "\n\nЛучшее решение = " + currentBestFitness + "\n" + bestIndividual);
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
