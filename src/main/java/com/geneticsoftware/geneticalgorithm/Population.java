package com.geneticsoftware.geneticalgorithm;

import com.geneticsoftware.util.BinaryString;
import com.geneticsoftware.util.ChromosomeType;

import javax.script.ScriptException;
import java.util.*;

public class Population {

    private Individual[] individuals;
    private HashMap<Individual, Double> fitness;

    private ChromosomeType chromosomeType;
    private FitnessFunction fitnessFunction;

    // Конструкторы
    public Population(int populationSize, int countChromosomes, ChromosomeType chromosomeType, FitnessFunction fitnessFunction) throws ScriptException {

        this.fitnessFunction = fitnessFunction;
        this.chromosomeType = chromosomeType;
        createRandomIndividuals(populationSize, countChromosomes, fitnessFunction);
        this.fitness = calculateFitness(fitnessFunction);
    }

    public Population(Population population, FitnessFunction fitnessFunction) throws ScriptException {
        individuals = new Individual[population.getIndividuals().length];
        for (int i = 0; i < individuals.length; i++){
            individuals[i] = new Individual(population.getIndividuals()[i]);
        }
        this.fitnessFunction = fitnessFunction;
        this.fitness = calculateFitness(fitnessFunction);
        this.chromosomeType = population.getChromosomeType();
    }

    public Population(Population population) throws ScriptException {
        individuals = new Individual[population.getIndividuals().length];
        for (int i = 0; i < individuals.length; i++){
            individuals[i] = new Individual(population.getIndividuals()[i]);
        }
        this.fitnessFunction = new FitnessFunction(population.getFitnessFunction());
        this.fitness = calculateFitness(fitnessFunction);
        this.chromosomeType = population.getChromosomeType();
    }

    public Population(Individual[] individuals, FitnessFunction fitnessFunction) throws ScriptException {
        this.fitnessFunction = fitnessFunction;
        this.chromosomeType = individuals[0].getChromosomeType();
        this.individuals = individuals;
        this.fitness = calculateFitness(fitnessFunction);
    }

    public Population(Individual[] individuals) {
        this.individuals = individuals;
        this.chromosomeType = individuals[0].getChromosomeType();
    }

    private void createRandomIndividuals(int populationSize, int countChromosomes, FitnessFunction fitnessFunction) {
        this.individuals = new Individual[populationSize];

        Random random = new Random();
        double[] minValue = fitnessFunction.getMinArgument();
        double[] maxValue = fitnessFunction.getMaxArgument();

        for (int i = 0; i < populationSize; i++) {
            Chromosome[] chromosomes = new Chromosome[countChromosomes];
            double[] arguments = new double[fitnessFunction.getArguments()];
            for (int k = 0; k < fitnessFunction.getArguments(); k++) {
                arguments[k] = minValue[k] + (maxValue[k] - minValue[k]) * random.nextDouble();
            }
            if (chromosomeType == ChromosomeType.BINARY) {
                BinaryString[] binaryStringArguments = (new BinaryString(arguments)).splitForPiecesByCount(countChromosomes);
                for (int j = 0; j < countChromosomes; j++) {
                    chromosomes[j] = new Chromosome(binaryStringArguments[j]);
                }
            } else if (chromosomeType == ChromosomeType.REAL) {
                chromosomes[0] = new Chromosome(arguments);
            }
            individuals[i] = new Individual(chromosomes, chromosomeType);
        }
    }

    // Геттер и сеттер для массива особей
    public Individual[] getIndividuals() {
        return individuals;
    }

    public void setIndividuals(Individual[] individuals) {
        this.individuals = individuals;
    }

    // Получение особи по индексу
    public Individual getIndividual(int index) {
        return individuals[index];
    }

    // Получение размера популяции
    public int getSize() {
        return individuals.length;
    }

    public FitnessFunction getFitnessFunction() {
        return fitnessFunction;
    }

    public void setFitnessFunction(FitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }

    public ChromosomeType getChromosomeType() {
        return chromosomeType;
    }

    public void setChromosomeType(ChromosomeType chromosomeType) {
        this.chromosomeType = chromosomeType;
    }

    public HashMap<Individual, Double> getFitness() {
        return fitness;
    }

    public void setFitness(HashMap<Individual, Double> fitness) {
        this.fitness = fitness;
    }

    public HashMap<Individual, Double> calculateFitness(FitnessFunction fitnessFunction) throws ScriptException {
        fitness = new HashMap<Individual, Double>();
        for (Individual individual : individuals) {
            double individualFitness = individual.calculateFitness(chromosomeType, fitnessFunction);
            fitness.put(individual, individualFitness);
        }
        return fitness;
    }

    public double bestFitness(){
        return fitness.get(Collections.max(fitness.entrySet(), HashMap.Entry.comparingByValue()).getKey()); //не уверен что работает
    }

    public Individual bestIndividual(){
        return Collections.max(fitness.entrySet(), HashMap.Entry.comparingByValue()).getKey(); //не уверен что работает
    }

    public double minFitness(){
        return fitness.get(Collections.min(fitness.entrySet(), HashMap.Entry.comparingByValue()).getKey()); //не уверен что работает
    }

    public Individual bestFitnessByIdx(ArrayList<Integer> idx){
        double max = fitness.get(individuals[idx.get(0)]);
        Individual res = individuals[idx.get(0)];
        idx.remove(idx.get(0));
        while(!idx.isEmpty()){
            if(fitness.get(individuals[idx.get(0)]) > max){
                max = fitness.get(individuals[idx.get(0)]);
                res = individuals[idx.get(0)];
            }
            idx.remove(idx.get(0));
        }
        return res;
    }


    private ArrayList<Individual> selectParentsByRouletteWheel() {
        Random random = new Random();
        double totalFitness = calculateTotalFitness();
        double[] wheelSectors = new double[individuals.length];

        wheelSectors[0] = (fitness.get(individuals[0])) / totalFitness;

        StringBuilder str = new StringBuilder("сектора рулетки: " + wheelSectors[0]);

        for (int i = 1; i < individuals.length - 1; i++){
            wheelSectors[i] = wheelSectors[i - 1] + fitness.get(individuals[i]) / totalFitness;

            str.append(" ").append(wheelSectors[i]);

        }
        wheelSectors[individuals.length - 1] = 1.0;

        str.append(" ").append(wheelSectors[individuals.length - 1]);
//        System.out.println(str + "\nродители:");

        ArrayList<Individual> parents = new ArrayList<Individual>();
        for (int i = 0; i < individuals.length; i++){
            double wheelPosition = random.nextDouble();
            int j;
            for(j = 0; wheelSectors[j] < wheelPosition; j++);
            parents.add(individuals[j]);

//            System.out.println(individuals[j].toString());
        }
        return parents;
    }

    public double calculateTotalFitness() {
        double totalFitness = 0;
        if(fitness.isEmpty()){
            // теоретически такого быть не должно, но мало ли. Если что, доделаю
            System.out.println("Пытаемся найти сумму приспособленности, когда приспособленность каждый особи не посчитана");
        }
        else{
            for (Double individualFitness : fitness.values()) {
                totalFitness += individualFitness;
            }
        }
        return totalFitness;
    }

    @Override
    public String toString() {

        StringBuilder res = new StringBuilder("Популяция: \n");

        for (Individual individual: individuals) {
            res.append(individual.toString() + "Приспособленность " + fitness.get(individual) + "\n");
        }

        return res.toString();
    }

}
