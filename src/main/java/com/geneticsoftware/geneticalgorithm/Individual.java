package com.geneticsoftware.geneticalgorithm;

import com.geneticsoftware.util.BinaryString;
import com.geneticsoftware.util.ChromosomeType;

import javax.script.ScriptException;

public class Individual {

    private Chromosome[] chromosomes;
    private int countChromosomes;
    private ChromosomeType chromosomeType;

    // Конструкторы
    public Individual() {
        this.chromosomes = new Chromosome[0];
        this.countChromosomes = 0;
    }

    public Individual(Individual individual) {
        this.chromosomes = new Chromosome[individual.getCountChromosomes()];
        for (int i = 0; i < individual.getCountChromosomes(); i++){
            this.chromosomes[i] = new Chromosome(individual.getChromosomes()[i]);
        }
        this.countChromosomes = individual.getCountChromosomes();
        this.chromosomeType = individual.getChromosomeType();
    }

    public Individual(int countChromosomes, ChromosomeType chromosomeType) {
        this.chromosomes = new Chromosome[countChromosomes];
        for (int i = 0; i < countChromosomes; i++) {
            this.chromosomes[i] = new Chromosome();
        }
        this.countChromosomes = countChromosomes;
        this.chromosomeType = chromosomeType;
    }

    public Individual(Chromosome chromosome, ChromosomeType chromosomeType) {
        this.chromosomes = new Chromosome[1];
        this.chromosomes[0] = chromosome;
        this.countChromosomes = 1;
        this.chromosomeType = chromosomeType;
    }

    public Individual(Chromosome[] chromosomes, ChromosomeType chromosomeType) {
        this.chromosomes = chromosomes;
        this.countChromosomes = chromosomes.length;
        this.chromosomeType = chromosomeType;
    }

    // Геттер и сеттер для chromosomes
    public Chromosome[] getChromosomes() {
        return chromosomes;
    }

    public void setChromosomes(Chromosome[] chromosomes) {
        this.chromosomes = chromosomes;
    }

    // Геттер и сеттер для sizeChromosomes
    public int getCountChromosomes() {
        return countChromosomes;
    }

    public void setCountChromosomes(int sizeChromosomes) {
        this.countChromosomes = sizeChromosomes;
    }

    public void setChromosomeType(ChromosomeType chromosomeType) {
        this.chromosomeType = chromosomeType;
    }

    public ChromosomeType getChromosomeType() {
        return chromosomeType;
    }

    public BinaryString getBinaryStringFromChromosomes(){
        BinaryString res = new BinaryString();
        for (Chromosome chromosome : this.chromosomes) {
            res.add(chromosome.getBits());
        }
        return res;
    }

    static public BinaryString getBinaryStringFromChromosomes(Chromosome[] chromosomes){
        BinaryString res = new BinaryString();
        for (Chromosome chromosome : chromosomes) {
            res.add(chromosome.getBits());
        }
        return res;
    }

    public double calculateFitness( ChromosomeType chromosomeType, FitnessFunction fitnessFunction) throws ScriptException {
        double res = Double.MAX_VALUE;
        if(chromosomeType == ChromosomeType.BINARY){
            BinaryString arguments = this.getBinaryStringFromChromosomes();
            res = fitnessFunction.calculateFitness(arguments);
        }
        else if(chromosomeType == ChromosomeType.REAL){
            double[] arguments = chromosomes[0].getChromosome();
            res = fitnessFunction.calculateFitness(arguments);
        }
        return res;
    }


    //@?@??@#???2/2/2//2/!?!?!?!?!??!!!!!??!?!?!??!?!??!?!?!?!?
    public boolean checkChromosomes(Chromosome[] chromosomesForChecking, ChromosomeType chromosomeType, FitnessFunction fitnessFunction){
        if(chromosomeType == ChromosomeType.BINARY) {
            double[] arguments = getBinaryStringFromChromosomes(chromosomesForChecking).binaryStringToDouble();
            for (int i = 0; i < arguments.length; i++) {
                if (Double.isNaN(arguments[i]) || arguments[i] < fitnessFunction.getMinArgument()[i] || arguments[i] > fitnessFunction.getMaxArgument()[i]) {
                    return false;
                }
            }
            return true;
        }
        else if(chromosomeType == ChromosomeType.REAL){
            double[] arguments = chromosomesForChecking[0].getChromosome();
            for (int i = 0; i < arguments.length; i++) {
                if (Double.isNaN(arguments[i]) || arguments[i] < fitnessFunction.getMinArgument()[i] || arguments[i] > fitnessFunction.getMaxArgument()[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean checkChromosomes(double[] chromosomesForChecking, FitnessFunction fitnessFunction){
        for (int i = 0; i < chromosomesForChecking.length; i++){
            if (Double.isNaN(chromosomesForChecking[i]) || chromosomesForChecking[i] < fitnessFunction.getMinArgument()[i] || chromosomesForChecking[i] > fitnessFunction.getMaxArgument()[i]){
                return false;
            }
        }
        return true;
    }


    @Override
    public String toString() {
        String res = "";
        if(chromosomeType == ChromosomeType.BINARY) {
            BinaryString[] arguments = getBinaryStringFromChromosomes().splitForPiecesBySize(64);
            for (int i = 0; i < arguments.length; i++) {
                res += "x" + (i + 1) + " = " + arguments[i].binaryStringToDouble()[0] + "\n";
            }
        }
        else if(chromosomeType == ChromosomeType.REAL){
            for (int i = 0; i < chromosomes[0].getChromosomeSize(); i++) {
                res += "x" + (i + 1) + " = " + chromosomes[0].getChromosome()[i] + "\n";
            }
        }
        return res;
    }
}