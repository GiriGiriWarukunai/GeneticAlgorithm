package com.geneticalgorithm;
/*
Ген - закодированное значение одного аргумента функции
 */

public class Gene {

    private int[] bits;
    private int bitCount;


    public Gene(int[] bits, int bitCount) {
        this.bits = bits;
        this.bitCount = bitCount;
    }


    public int getBitCount() {
        return bitCount;
    }

    public void setBitCount(int bitCount) {
        this.bitCount = bitCount;
    }

    public int[] getBits() {
        return bits;
    }

    public void setBits(int[] bits) {
        this.bits = bits;
    }




}
