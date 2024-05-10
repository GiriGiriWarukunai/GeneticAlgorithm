package com.geneticalgorithm;//import javax.lang.model.type.NullType;
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
//        for(int i = 0; i < doubles.length; i++) {
//            Pair<String, String> tmp = split(64);
//            long bits = Long.parseLong(binaryString, 2);
//            doubles[i] = Double.longBitsToDouble(bits);
//        }

        //!!!!!!!!!!!!!!!!!!!!!! только для функции с 2 аргументами
        String[] tmp = splitForPieces(64);
//        long bits1 = Long.parseLong(tmp.getFirst(), 2);
//        doubles[0] = Double.longBitsToDouble(bits1);
//        long bits2 = Long.parseLong(tmp.getSecond(), 2);
//        doubles[1] = Double.longBitsToDouble(bits2);

//        long bits1 = 0;
//        for (int i = 0; i < 64; i++) {
//            char c = tmp.getFirst().charAt(i);
//            if (c == '1') {
//                bits1 |= 1L << (63 - i); // Устанавливаем соответствующий бит
//            }
//        }
//        doubles[0] =  Double.longBitsToDouble(bits1);
//
//        long bits2 = 0;
//        for (int i = 0; i < 64; i++) {
//            char c = tmp.getSecond().charAt(i);
//            if (c == '1') {
//                bits2 |= 1L << (63 - i); // Устанавливаем соответствующий бит
//            }
//        }
//        doubles[1] =  Double.longBitsToDouble(bits2);

        for (int i = 0; i < tmp.length; i++){
            doubles[i] = Double.longBitsToDouble(new BigInteger(tmp[i], 2).longValue());
        }



        return doubles;
    }

    public Pair<String, String> split(int splitIndex){
        return new Pair<String, String>(binaryString.substring(0, splitIndex), binaryString.substring(splitIndex));
//        res.setFirst(); // Получение первой части строки
//        res.setSecond(binaryString.substring(splitIndex)); // Получение второй части строки
//        res;
    }

    public String[] splitForPieces(int pieceSize){
        String[] res = new String[binaryString.length() / pieceSize];
        int i = 0, j = 0;
        for (; i < res.length - 1; i++, j += pieceSize){
            res[i] = binaryString.substring(j, j + pieceSize);
        }
        res[i] = binaryString.substring(j);
        return res;
//        res.setFirst(); // Получение первой части строки
//        res.setSecond(binaryString.substring(splitIndex)); // Получение второй части строки
//        res;
    }

    public static Pair<BinaryString, BinaryString> splitAndSwap(BinaryString first, BinaryString second, int splitIndex){
        Pair<String, String> tmp1 = first.split(splitIndex);
        Pair<String, String> tmp2 = second.split(splitIndex);
        return new Pair<BinaryString, BinaryString>(new BinaryString(tmp1.getFirst() + tmp2.getSecond()), new BinaryString(tmp2.getFirst() + tmp1.getSecond()));
    }

    public void setBit(char bit, int idx){
        StringBuilder stringBuilder = new StringBuilder(binaryString);
        stringBuilder.setCharAt(idx, bit);
        binaryString = stringBuilder.toString();
    }
}