package com.geneticsoftware.geneticalgorithm.mutation;

import com.geneticsoftware.geneticalgorithm.Chromosome;
import com.geneticsoftware.geneticalgorithm.Individual;
import com.geneticsoftware.geneticalgorithm.Population;

import javax.script.ScriptException;
import java.util.Random;

public class MutationOneBitInvertion extends Mutation{
    @Override
    public Population mutation(Population population, double mutationRate) throws ScriptException {

        Population res = new Population(population);
        for (int i = 0; i < res.getIndividuals().length; i++) {
            Individual individual = new Individual();

            Chromosome[] tmp = new Chromosome[res.getIndividuals()[i].getCountChromosomes()];
            int mutationAttempts = 0;
            do {
                for (int j = 0; j < res.getIndividuals()[i].getCountChromosomes(); j++) {

                    Chromosome tmpChromosome = new Chromosome(res.getIndividuals()[i].getChromosomes()[j]);
                    Random random = new Random();

                    if (random.nextDouble() < mutationRate) {

                        StringBuilder stringBuilder = new StringBuilder();
                        int idx = random.nextInt(res.getIndividuals()[i].getChromosomes()[j].getBits().getLength()/* - 1*/);

                        for (int k = 0; k < tmpChromosome.getBits().getLength(); k++) {
                            if (k == idx) {
                                stringBuilder.append(tmpChromosome.getBits().getBinaryString().charAt(k) == '0' ? '1' : '0'); // Инвертируем бит
                            }
                            else {
                                stringBuilder.append(tmpChromosome.getBits().getBinaryString().charAt(k));
                            }
                        }
                        tmpChromosome.getBits().setBinaryString(stringBuilder.toString());

                    }
                    tmp[j] = tmpChromosome;
                    // System.out.println("Временная хромосома в мутации: " + tmp);
                }
                //         System.out.println("Итоговая хромосома после мутации: " + chromosomes[i] + "\n");
                mutationAttempts++;

            } while (!(individual.checkChromosomes(tmp,population.getChromosomeType(), res.getFitnessFunction())) && mutationAttempts < maxMutationAttempts);

            if (mutationAttempts < maxMutationAttempts && individual.checkChromosomes(tmp,population.getChromosomeType(), res.getFitnessFunction())) {
                res.getIndividuals()[i].setChromosomes(tmp);
            }
//            System.out.println("Особь после мутации: " + individuals[i] + "\n");

            double[] tmp2 = res.getIndividuals()[i].getBinaryStringFromChromosomes().binaryStringToDouble();
            for (Double j : tmp2) {
                if (j.isNaN()) {
                    System.out.println("NANANANANAAANNANANNAANANANANANANNANANNANANA");
                    System.exit(0);
                }
            }
        }

        //System.out.println(toString());

        res.calculateFitness(res.getFitnessFunction());
        return res;
//        System.out.println("Популяция после мутации:" + toString());
    }
}
