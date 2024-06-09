package com.geneticsoftware.geneticalgorithm.crossover;

import com.geneticsoftware.geneticalgorithm.Chromosome;
import com.geneticsoftware.geneticalgorithm.FitnessFunction;
import com.geneticsoftware.geneticalgorithm.Individual;
import com.geneticsoftware.geneticalgorithm.Population;
import com.geneticsoftware.util.BinaryString;
import com.geneticsoftware.util.Pair;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Random;

public class CrossoverUniform extends Crossover{

    @Override
    public Population crossover(ArrayList<Individual> parents, FitnessFunction fitnessFunction, double crossoverRate) {
        return null;
    }

    @Override
    public Population crossover(ArrayList<Individual> parents, FitnessFunction fitnessFunction, double crossoverRate, double alpha) throws ScriptException {
        Random random = new Random();
        Individual[] offspring = new Individual[parents.size()];

        //System.out.println("\n" + toString());

        int i = 0;
        while (!parents.isEmpty()) {
            int idx1 = random.nextInt(parents.size());
            int idx2 = random.nextInt(parents.size());
            while (idx1 == idx2) {
                idx2 = random.nextInt(parents.size());
            }
            Individual parent1 = new Individual(parents.get(idx1));
            Individual parent2 = new Individual(parents.get(idx2));

            if (idx1 > idx2) {
                parents.remove(idx1);
                parents.remove(idx2);
            } else {
                parents.remove(idx2);
                parents.remove(idx1);
            }

//            System.out.println("\nродители:\n" + parent1.toString());
//            System.out.println(parent2.toString());

            if (random.nextDouble() < crossoverRate) {

                Chromosome[] child1Chromosomes = new Chromosome[parent1.getCountChromosomes()];
                Chromosome[] child2Chromosomes = new Chromosome[parent2.getCountChromosomes()];

                int crossoverAttempts = 0;
                do {
                    for (int j = 0; j < parent1.getCountChromosomes(); j++) {

                        child1Chromosomes[j] = new Chromosome();
                        child2Chromosomes[j] = new Chromosome();

                        Pair<StringBuilder, StringBuilder> tmp = new Pair<StringBuilder, StringBuilder>();

                        for (int k = 0; k < parent1.getChromosomes()[j].getBits().getLength(); k++) {
                            if (random.nextDouble() < alpha) {
                                tmp.getFirst().append(parent2.getChromosomes()[j].getBits().getBinaryString().charAt(k));
                                tmp.getSecond().append(parent1.getChromosomes()[j].getBits().getBinaryString().charAt(k));
                            } else {
                                tmp.getFirst().append(parent1.getChromosomes()[j].getBits().getBinaryString().charAt(k));
                                tmp.getSecond().append(parent2.getChromosomes()[j].getBits().getBinaryString().charAt(k));
                            }
                        }
                        child1Chromosomes[j].setBits(new BinaryString(tmp.getFirst().toString()));
                        child2Chromosomes[j].setBits(new BinaryString(tmp.getSecond().toString()));
//                System.out.println("\nВременные потомки: \nx = " + child1.binaryStringToDouble()[0] + "\ny = " + child1.binaryStringToDouble()[1] +
//                        "\n\nx = " + child2.binaryStringToDouble()[0] + "\ny = " + child2.binaryStringToDouble()[1]);
                    }
                    crossoverAttempts++;
                } while (!(parent1.checkChromosomes(child1Chromosomes, parent1.getChromosomeType(), fitnessFunction) &&
                        parent1.checkChromosomes(child2Chromosomes, parent1.getChromosomeType(), fitnessFunction)) &&
                        crossoverAttempts < maxCrossoverAttempts);

//            System.out.println("\nИтоговые потомки: \nx = " + child1.binaryStringToDouble()[0] + "\ny = " + child1.binaryStringToDouble()[1] +
//                    "\n\nx = " + child2.binaryStringToDouble()[0] + "\ny = " + child2.binaryStringToDouble()[1]);

                if (parent1.checkChromosomes(child1Chromosomes, parent1.getChromosomeType(), fitnessFunction) &&
                        parent1.checkChromosomes(child2Chromosomes, parent1.getChromosomeType(), fitnessFunction) &&
                        crossoverAttempts < maxCrossoverAttempts) {
                    offspring[i] = new Individual(child1Chromosomes, parent1.getChromosomeType());
                    offspring[i + 1] = new Individual(child2Chromosomes, parent1.getChromosomeType());
                } else {
                    offspring[i] = parent1;
                    offspring[i + 1] = parent2;
                }
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

        for (Individual individual : offspring) {
            double[] tmp = individual.getBinaryStringFromChromosomes().binaryStringToDouble();
            for (Double j : tmp) {
                if (j.isNaN()) {
                    System.out.println("NANANANANAAANNANANNAANANANANANANNANANNANANA");
                    System.exit(0);
                }
            }
        }

        return new Population(offspring, fitnessFunction);
    }

    @Override
    public Population crossover(ArrayList<Individual> parents, FitnessFunction fitnessFunction, double crossoverRate, int k) throws ScriptException {
        return null;
    }
}
