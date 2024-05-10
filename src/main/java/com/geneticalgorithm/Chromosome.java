package com.geneticalgorithm;

import java.util.Random;

public class Chromosome {

    private BinaryString bits;
    //private int bitsSize;


    public Chromosome(BinaryString bits/*, int bitsSize*/) {
        this.bits = bits;
        //this.bitsSize = bitsSize;
    }

    public Chromosome() {
        bits = new BinaryString();
    }

    public Chromosome(Chromosome chromosome) {
        bits = new BinaryString(chromosome.getBits());
    }

//    public int getBitsSize() {
//        return bitsSize;
//    }
//
//    public void setBitsSize(int size) {
//        this.bitsSize = size;
//    }

    public BinaryString getBits() {
        return bits;
    }

    public void setBits(BinaryString bits) {
        this.bits = bits;
    }

    public double[] calculateValue(){
        return bits.binaryStringToDouble();
    }

    // Мутация хромосомы с вероятностью mutationRate
    public void mutate(double mutationRate) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();


//        if (random.nextDouble() < mutationRate) {
//            int idx = random.nextInt(bits.getLength());
//            bits.setBit(bits.getBinaryString().charAt(idx) == '0' ? '1' : '0', idx); // Инвертируем бит
//        }

        for (int i = 0; i < bits.getLength(); i++) {
            if (random.nextDouble() < mutationRate) {
                stringBuilder.append(bits.getBinaryString().charAt(i) == '0' ? '1' : '0'); // Инвертируем бит
            } else {
                stringBuilder.append(bits.getBinaryString().charAt(i));
            }
        }
        this.bits.setBinaryString(stringBuilder.toString());

        //System.out.println("x = " + this.bits.binaryStringToDouble()[0] + ", y = " + this.bits.binaryStringToDouble()[1]);

    }

    private boolean checkBinaryString(BinaryString chromosomeForChecking, FitnessFunction fitnessFunction){
        double[] chromosome = chromosomeForChecking.binaryStringToDouble();
        for (double i : chromosome){
            if (i < fitnessFunction.getMinArgument() && i > fitnessFunction.getMaxArgument()){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "x = " + calculateValue()[0] + ", y = " + calculateValue()[1] ;
    }

}
