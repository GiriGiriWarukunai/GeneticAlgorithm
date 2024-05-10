package com.geneticalgorithm;

import java.util.*;

public class Population {

    private Individual[] individuals;
    HashMap<Individual, Double> fitness;

    private  FitnessFunction fitnessFunction;

    // Конструкторы
    public Population(int populationSize, int countChromosomes, FitnessFunction fitnessFunction) {

        createRandomIndividuals(populationSize, countChromosomes, fitnessFunction);
        this.fitnessFunction = fitnessFunction;

        this.fitness = calculateFitness(fitnessFunction);
    }

    public Population(Population population, FitnessFunction fitnessFunction) {
        individuals = new Individual[population.getIndividuals().length];
        for (int i = 0; i < individuals.length; i++){
            individuals[i] = new Individual(population.getIndividuals()[i]);
        }
        this.fitnessFunction = fitnessFunction;
        this.fitness = calculateFitness(fitnessFunction);
    }

    public Population(Individual[] individuals, FitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;

        this.individuals = individuals;
        this.fitness = calculateFitness(fitnessFunction);
    }

    public Population(Individual[] individuals) {
        this.individuals = individuals;
    }

    private void createRandomIndividuals(int populationSize, int countChromosomes, FitnessFunction fitnessFunction) {
        this.individuals = new Individual[populationSize];

        Random random = new Random();
        double minValue = fitnessFunction.getMinArgument();
        double maxValue = fitnessFunction.getMaxArgument();

        for (int i = 0; i < populationSize; i++) {
            Chromosome[] chromosomes = new Chromosome[countChromosomes];
            for (int j = 0; j < countChromosomes; j++) {
                double[] arguments = new double[fitnessFunction.getArguments()];
                for (int k = 0; k < fitnessFunction.getArguments(); k++) {
                    arguments[k] = minValue + (maxValue - minValue) * random.nextDouble();
                }
                chromosomes[j] = new Chromosome(new BinaryString(arguments));
            }
            individuals[i] = new Individual(chromosomes);
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

    public HashMap<Individual, Double> calculateFitness(FitnessFunction fitnessFunction) {
        fitness = new HashMap<Individual, Double>();
        for (Individual individual : individuals) {
            double individualFitness = fitnessFunction.calculateFitness(individual);
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


    public Population crossover(double crossoverRate) {
        Random random = new Random();
        ArrayList<Individual> parents;
//        if(minFitness() > 0){
//            parents = selectParentsByRouletteWheel();
//        }
//        else {
//            parents = selectParentsByTournament();
//        }

        parents = selectParentsByTournament();


        Individual[] offspring = new Individual[individuals.length];

        //System.out.println("\n" + toString());

        int i = 0;
        while (!parents.isEmpty()){
            int idx1 = random.nextInt(parents.size());
            int idx2 = random.nextInt(parents.size());
            while (idx1 == idx2) {
                idx2 = random.nextInt(parents.size());
            }
            Individual parent1 = new Individual(parents.get(idx1));
            Individual parent2 = new Individual(parents.get(idx2));

            if (idx1 > idx2){
                parents.remove(idx1);
                parents.remove(idx2);
            }
            else {
                parents.remove(idx2);
                parents.remove(idx1);
            }

//            System.out.println("\nродители:\n" + parent1.toString());
//            System.out.println(parent2.toString());


            if (random.nextDouble() < crossoverRate) {
                Pair<Individual, Individual> children = parent1.crossover(parent2, fitnessFunction);
                offspring[i] = children.getFirst();
                offspring[i + 1] = children.getSecond();

//                System.out.println("потомки:\n" + offspring[i].toString());
//                System.out.println(offspring[i + 1].toString());

            } else {
                offspring[i] = parent1;
                offspring[i + 1] = parent2;

//                System.out.println("\n" + offspring[i].toString());
//                System.out.println("\n" + offspring[i + 1].toString());

            }
            i += 2;
        }

        for (Individual individual : individuals) {
            double[] tmp = individual.getChromosomes()[0].getBits().binaryStringToDouble();
            for (Double j : tmp) {
                if (j.isNaN()) {
                    System.out.println("NANANANANAAANNANANNAANANANANANANNANANNANANA");
                    System.exit(0);
                }
            }
        }

        return new Population(offspring, fitnessFunction);
    }

    private boolean checkChromosome(Chromosome chromosomeForChecking){
        double[] chromosome = chromosomeForChecking.getBits().binaryStringToDouble();
        for (double i : chromosome){
            if (i < fitnessFunction.getMinArgument() && i > fitnessFunction.getMaxArgument()){
                return false;
            }
        }
        return true;
    }

    private boolean checkBinaryString(BinaryString chromosomeForChecking){
        double[] chromosome = chromosomeForChecking.binaryStringToDouble();
        for (double i : chromosome){
            if (i < fitnessFunction.getMinArgument() && i > fitnessFunction.getMaxArgument()){
                return false;
            }
        }
        return true;
    }

    private Individual bestFitnessByIdx(ArrayList<Integer> idx){
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

    private ArrayList<Individual> selectParentsByTournament(){
        Random random = new Random();
        ArrayList<Individual> parents = new ArrayList<Individual>();
        for (int i = 0; i < individuals.length; i++){
            int countIdx;
            /*if(individuals.length > 100){
                countIdx = individuals.length / 2;
                countIdx += countIdx % 2;
            }
            else*/

            if(individuals.length >= 10){
                countIdx = individuals.length / 10;
                countIdx += countIdx % 2;
            }
            else if (individuals.length > 2){
                countIdx = individuals.length / 2;
                countIdx -= countIdx % 2;
            }
            else {
                countIdx = 2;
            }

               //countIdx = 10;

            ArrayList<Integer> idx = new ArrayList<>(countIdx);

            for (int j = 0; j < countIdx; j++){
                int tmp;
                do {
                   tmp = random.nextInt(individuals.length);
                } while(idx.contains(tmp));
                idx.add(tmp);
            }

            Individual parent = new Individual(bestFitnessByIdx(idx));
            parents.add(parent);
//            System.out.println(individuals[j].toString());
        }
        return parents;
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

    private double calculateTotalFitness() {
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

    public void mutation(double mutationRate) {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i].mutate(mutationRate, fitnessFunction);
//            System.out.println("Особь после мутации: " + individuals[i] + "\n");

            double[] tmp = this.individuals[i].getChromosomes()[0].getBits().binaryStringToDouble();
            for (Double j : tmp){
                if (j.isNaN()){
                    System.out.println("NANANANANAAANNANANNAANANANANANANNANANNANANA");
                    System.exit(0);
                }
            }
        }

 //       System.out.println(toString());

        calculateFitness(fitnessFunction);

//        System.out.println("Популяция после мутации:" + toString());
    }



    @Override
    public String toString() {

        StringBuilder res = new StringBuilder("Популяция: \n");

        for (Individual individual: individuals) {
            res.append(individual.toString() + "; Приспособленность " + fitness.get(individual) + "\n");
        }

        return res.toString();
    }

}
