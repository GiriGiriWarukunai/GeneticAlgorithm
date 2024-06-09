package com.geneticsoftware.util;//import javax.lang.model.type.NullType;

import java.math.BigInteger;

public class BinaryString {

    private String binaryString;

    // Конструкторы
    public BinaryString() {
        this.binaryString = "";
    }

    public BinaryString(BinaryString binaryString) {
        this.binaryString = binaryString.getBinaryString();
    }

    public BinaryString(String binaryString) {
        this.binaryString = binaryString;
    }

    public BinaryString(double number) {
        this.binaryString = doubleToBinaryString(number);
    }

    public BinaryString(double[] numbers) {
        this.binaryString = doubleToBinaryString(numbers);
    }

    // Геттер и сеттер для binaryString
    public String getBinaryString() {
        return binaryString;
    }

    public void setBinaryString(String binaryString) {
        this.binaryString = binaryString;
    }

    public int getLength(){
        return binaryString.length();
    }

    // Функция для преобразования числа типа double в его двоичное представление в виде строки
    public String doubleToBinaryString(double number) {

        long bits = Double.doubleToLongBits(number);
        String binaryString = Long.toBinaryString(bits);
        // Дополнение нулями до 64 бит (длина двоичного представления double)
        return String.format("%64s", binaryString).replace(' ', '0');
    }

    public String doubleToBinaryString(double[] numbers) {
        String res = "";
        for (double number : numbers) {
            long bits = Double.doubleToLongBits(number);
            String binaryString = Long.toBinaryString(bits);
            res += String.format("%64s", binaryString).replace(' ', '0');

        }
        return res;
    }

    public void addDoubleToBinaryString(double number){
        binaryString += doubleToBinaryString(number);
    }

    // Функция для преобразования строки с двоичным представлением в число типа double
    public double[] binaryStringToDouble() {
        double[] doubles = new double[binaryString.length() / 64];

        BinaryString[] tmp = splitForPiecesBySize(64);

        for (int i = 0; i < tmp.length; i++){
            doubles[i] = Double.longBitsToDouble(new BigInteger(tmp[i].getBinaryString(), 2).longValue());
        }

        return doubles;
    }

    public Pair<String, String> split(int splitIndex){
        return new Pair<String, String>(binaryString.substring(0, splitIndex), binaryString.substring(splitIndex));
    }

    public BinaryString[] splitForPiecesBySize(int pieceSize){
        BinaryString[] res = new BinaryString[binaryString.length() / pieceSize];
        int i = 0, j = 0;
        for (; i < res.length - 1; i++, j += pieceSize){
            res[i] = new BinaryString(binaryString.substring(j, j + pieceSize));
        }
        res[i] = new BinaryString(binaryString.substring(j));
        return res;
    }

    public BinaryString[] splitForPiecesByCount(int pieceCount){
        BinaryString[] res = new BinaryString[pieceCount];
        int pieceSize = this.getLength() / pieceCount;
        int i = 0, j = 0;
        for (; i < res.length - 1; i++, j += pieceSize){
            res[i] = new BinaryString(binaryString.substring(j, j + pieceSize));
        }
        res[i] = new BinaryString(binaryString.substring(j));
        return res;
    }

    public static Pair<BinaryString, BinaryString> splitAndSwap(BinaryString first, BinaryString second, int splitIndex){
        Pair<String, String> tmp1 = first.split(splitIndex);
        Pair<String, String> tmp2 = second.split(splitIndex);

        Pair<BinaryString, BinaryString> res = new Pair<BinaryString, BinaryString>(new BinaryString(tmp1.getFirst() + tmp2.getSecond()), new BinaryString(tmp2.getFirst() + tmp1.getSecond()));
        return res;
    }

    public void setBit(char bit, int idx){
        StringBuilder stringBuilder = new StringBuilder(binaryString);
        stringBuilder.setCharAt(idx, bit);
        binaryString = stringBuilder.toString();
    }

    public void add(BinaryString a){
        this.binaryString += a.getBinaryString();
    }

    public void add(String a){
        this.binaryString += a;
    }

    @Override
    public String toString() {

        String res = "";
        if (binaryString.length() == 128) {

            BinaryString[] arguments = this.splitForPiecesBySize(64);
            for (int i = 0; i < arguments.length; i++) {
                res += "x" + (i + 1) + " = " + arguments[i].binaryStringToDouble()[0] + "\n";
            }

        }
        return res;
    }
}