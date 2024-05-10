package com.geneticalgorithm;

import java.util.Random;

public class Individual {

    private Chromosome[] chromosomes;
    private int countChromosomes;

    private int maxCrossoverAttempts = 100; //Максимальная попытка скрестить хромосомы. Если не получится данное количество раз, то в потомки пойдут хромосомы без изменений
    private int maxMutationAttempts = 100; //Максимальная попытка скрестить хромосомы. Если не получится данное количество раз, то в потомки пойдут хромосомы без изменений

    // Конструкторы
    public Individual() {
        this.chromosomes = new Chromosome[0];
        this.countChromosomes = 0;
    }

    public Individual(Individual individual) {
        this.chromosomes = new Chromosome[individual.getSizeChromosomes()];
        for (int i = 0; i < individual.getSizeChromosomes(); i++){
            this.chromosomes[i] = new Chromosome(individual.getChromosomes()[i]);
        }
        this.countChromosomes = individual.getSizeChromosomes();
    }

    public Individual(int countChromosomes) {
        this.chromosomes = new Chromosome[countChromosomes];
        for (int i = 0; i < countChromosomes; i++) {
            this.chromosomes[i] = new Chromosome();
        }
        this.countChromosomes = countChromosomes;
    }

    public Individual(Chromosome chromosome) {
        this.chromosomes = new Chromosome[1];
        this.chromosomes[0] = chromosome;
        this.countChromosomes = 1;
    }

    public Individual(Chromosome[] chromosomes) {
        this.chromosomes = chromosomes;
        this.countChromosomes = chromosomes.length;
    }

    // Геттер и сеттер для chromosomes
    public Chromosome[] getChromosomes() {
        return chromosomes;
    }

    public void setChromosomes(Chromosome[] chromosomes) {
        this.chromosomes = chromosomes;
    }

    // Геттер и сеттер для sizeChromosomes
    public int getSizeChromosomes() {
        return countChromosomes;
    }

    public void setSizeChromosomes(int sizeChromosomes) {
        this.countChromosomes = sizeChromosomes;
    }

    // Мутация генотипа
    public void mutate(double mutationRate, FitnessFunction fitnessFunction) {
       // Random rand = new Random();
        for (int i = 0; i < chromosomes.length; i++) {

            int mutationAttempts = 0;
            Chromosome tmp = new Chromosome();
            do {
                tmp = new Chromosome(chromosomes[i]);
                tmp.mutate(mutationRate);

               // System.out.println("Временная хромосома в мутации: " + tmp);

                mutationAttempts++;
            } while (!(checkChromosome(tmp, fitnessFunction)) && mutationAttempts < maxMutationAttempts);
            if (mutationAttempts < maxMutationAttempts && checkChromosome(tmp, fitnessFunction)) {
                chromosomes[i] = tmp;
            }
  //         System.out.println("Итоговая хромосома после мутации: " + chromosomes[i] + "\n");

        }
    }



    public Pair<Individual, Individual> crossover(Individual partner, FitnessFunction fitnessFunction){
        Random random = new Random();
       // Создаем копии хромосом для потомков
        Chromosome[] child1Chromosomes = new Chromosome[countChromosomes];
        Chromosome[] child2Chromosomes = new Chromosome[countChromosomes];
//        for (int i = 0; i < countChromosomes; i++) {
//            child1Chromosomes[i] = new Chromosome(chromosomes[i].getBits());
//            child2Chromosomes[i] = new Chromosome(partner.getChromosomes()[i].getBits());
//        }

        // Выполняем скрещивание в точке разрыва хромосомы
        for (int i = 0; i < chromosomes.length; i++) {

            child1Chromosomes[i] = new Chromosome();
            child2Chromosomes[i] = new Chromosome();

            Pair<BinaryString, BinaryString> tmp = new Pair<BinaryString, BinaryString>();

            BinaryString child1 = new BinaryString();
            BinaryString child2 = new BinaryString();
            int crossoverAttempts = 0;

            do {
                int crossoverPoint1 = random.nextInt(chromosomes[i].getBits().getLength() - 1) + 1; // !!!!!СЛУЧАЙНАЯ ГЕНЕРАЦИЯ ТОЧКИ РАЗРЫВА
                int crossoverPoint2 = random.nextInt(chromosomes[i].getBits().getLength() - 1) + 1; // !!!!!СЛУЧАЙНАЯ ГЕНЕРАЦИЯ ТОЧКИ РАЗРЫВА

                while (crossoverPoint1 == crossoverPoint2){
                    crossoverPoint2 = random.nextInt(chromosomes[i].getBits().getLength() - 1) + 1;
                }
                if(crossoverPoint1 > crossoverPoint2){
                    int swp = crossoverPoint2;
                    crossoverPoint2 = crossoverPoint1;
                    crossoverPoint1 = swp;
                }

                tmp = BinaryString.splitAndSwap(this.getChromosomes()[i].getBits(), partner.getChromosomes()[i].getBits(), crossoverPoint1);
                tmp = BinaryString.splitAndSwap(tmp.getFirst(), tmp.getSecond(), crossoverPoint2);
                child1.setBinaryString(tmp.getFirst().getBinaryString());
                child2.setBinaryString(tmp.getSecond().getBinaryString());

//                System.out.println("\nВременные потомки: \nx = " + child1.binaryStringToDouble()[0] + "\ny = " + child1.binaryStringToDouble()[1] +
//                        "\n\nx = " + child2.binaryStringToDouble()[0] + "\ny = " + child2.binaryStringToDouble()[1]);

                crossoverAttempts++;
            } while (!(checkBinaryString(child1, fitnessFunction) && checkBinaryString(child2, fitnessFunction)) && crossoverAttempts < maxCrossoverAttempts);

//            System.out.println("\nИтоговые потомки: \nx = " + child1.binaryStringToDouble()[0] + "\ny = " + child1.binaryStringToDouble()[1] +
//                    "\n\nx = " + child2.binaryStringToDouble()[0] + "\ny = " + child2.binaryStringToDouble()[1]);


            child1Chromosomes[i].setBits(new BinaryString(child1));
            child2Chromosomes[i].setBits(new BinaryString(child2));

        }
        return new Pair<Individual, Individual>(new Individual(child1Chromosomes), new Individual(child2Chromosomes));
    }

    private boolean checkChromosome(Chromosome chromosomeForChecking, FitnessFunction fitnessFunction){
        double[] chromosome = chromosomeForChecking.getBits().binaryStringToDouble();

//        for (Double i : chromosome){
//            if (Double.isNaN(i)){
//                System.out.println("ВОТ ОНО ТУТ НАН");
//            }
//        }

        for (Double i : chromosome){
            if (Double.isNaN(i) || i < fitnessFunction.getMinArgument() || i > fitnessFunction.getMaxArgument()){
                return false;
            }
        }
        return true;
    }

    private boolean checkBinaryString(BinaryString chromosomeForChecking, FitnessFunction fitnessFunction){
        double[] chromosome = chromosomeForChecking.binaryStringToDouble();
        for (double i : chromosome){
            if (Double.isNaN(i) || i < fitnessFunction.getMinArgument() || i > fitnessFunction.getMaxArgument()){
                return false;
            }
        }
        return true;
    }

    public String invertValue(){
        String res = "";
        for (int i = 0; i < chromosomes.length; i++) {
            for (int j = 0; j < chromosomes[i].getBits().getBinaryString().length() / 64; j++) {
                res += "x" + (j + 1) + " = " + chromosomes[i].calculateValue()[j] + "\n";
            }
        }
        return res;
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < chromosomes.length; i++) {
            for (int j = 0; j < chromosomes[i].getBits().getBinaryString().length() / 64; j++) {
                res += "x" + (j + 1) + " = " + chromosomes[i].calculateValue()[j] + "\n";
            }
        }
        return res;
    }
}