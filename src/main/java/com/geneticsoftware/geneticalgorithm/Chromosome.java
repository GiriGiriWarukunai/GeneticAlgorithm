package com.geneticsoftware.geneticalgorithm;

import com.geneticsoftware.util.BinaryString;

public class Chromosome {

    private BinaryString bits;
    private int bitsSize;

    private double[] chromosome;
    private int chromosomeSize;


    public Chromosome(BinaryString bits) {
        this.bits = bits;
        this.bitsSize = bits.getLength();
    }

    public Chromosome(BinaryString bits, int bitsSize) {
        this.bits = bits;
        this.bitsSize = bitsSize;
    }

    public Chromosome(double[] chromosome, int chromosomeSize) {
        this.chromosome = chromosome;
        this.chromosomeSize = chromosomeSize;
    }

    public Chromosome(double[] chromosome) {
        this.chromosome = chromosome;
        this.chromosomeSize = chromosome.length;
    }

    public Chromosome() {
        bits = new BinaryString();
        //chromosome = new double[1];
    }

    public Chromosome( int chromosomeSize) {
        this.chromosomeSize = chromosomeSize;
        chromosome = new double[chromosomeSize];
    }

    public Chromosome(Chromosome chromosome) {
        if (chromosome.getBits() != null) {
            bits = new BinaryString(chromosome.getBits());
        } else if (chromosome.getChromosome() != null) {
            chromosomeSize = chromosome.getChromosomeSize();
            this.chromosome = new double[chromosome.getChromosomeSize()];
            for (int i = 0; i < chromosomeSize; i++){
                this.chromosome[i] = chromosome.getChromosome()[i];
            }
        }
    }

    public BinaryString getBits() {
        return bits;
    }

    public void setBits(BinaryString bits) {
        this.bits = bits;
    }

    public double[] getChromosome() {
        return chromosome;
    }

    public void setChromosome(double[] chromosome) {
        this.chromosome = chromosome;
    }

    public int getChromosomeSize() {
        return chromosomeSize;
    }

    public void setChromosomeSize(int chromosomeSize) {
        this.chromosomeSize = chromosomeSize;
    }

    public int getBitsSize() {
        return bitsSize;
    }

    public void setBitsSize(int bitsSize) {
        this.bitsSize = bitsSize;
    }
}
