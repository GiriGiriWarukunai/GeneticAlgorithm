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

public class CrossoverIntermediate extends Crossover {

    @Override
    public Population crossover(ArrayList<Individual> parents, FitnessFunction fitnessFunction, double crossoverRate) throws ScriptException {
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
                double[] child1 = new double[parent1.getChromosomes()[0].getChromosomeSize()];
                double[] child2 = new double[parent2.getChromosomes()[0].getChromosomeSize()];

                int crossoverAttempts = 0;
                do {

                    for (int j = 0; j < parent1.getChromosomes()[0].getChromosomeSize(); j++) {
                        double p1 = parent1.getChromosomes()[0].getChromosome()[j];
                        double p2 = parent2.getChromosomes()[0].getChromosome()[j];
                        if(p1 > p2){
                            double tmp = p1;
                            p1 = p2;
                            p2 = tmp;
                        }

                        double a = -alpha + (1+alpha - -alpha) * random.nextDouble();

                        child1[j] = p1 * a + p2 * (1 - a);
                        child2[j] = p2 * a + p1 * (1 - a);
                    }
                    crossoverAttempts++;
                } while (!(parent1.checkChromosomes(child1, fitnessFunction) &&
                        parent1.checkChromosomes(child2, fitnessFunction)) &&
                        crossoverAttempts < maxCrossoverAttempts);

//            System.out.println("\nИтоговые потомки: \nx = " + child1.binaryStringToDouble()[0] + "\ny = " + child1.binaryStringToDouble()[1] +
//                    "\n\nx = " + child2.binaryStringToDouble()[0] + "\ny = " + child2.binaryStringToDouble()[1]);

                if (parent1.checkChromosomes(child1, fitnessFunction) &&
                        parent1.checkChromosomes(child2, fitnessFunction) &&
                        crossoverAttempts < maxCrossoverAttempts) {
                    offspring[i] = new Individual(new Chromosome(child1), parent1.getChromosomeType());
                    offspring[i + 1] = new Individual(new Chromosome(child2), parent1.getChromosomeType());
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

//        for (Individual individual : offspring) {
//            double[] tmp = individual.getBinaryStringFromChromosomes().binaryStringToDouble();
//            for (Double j : tmp) {
//                if (j.isNaN()) {
//                    System.out.println("NANANANANAAANNANANNAANANANANANANNANANNANANA");
//                    System.exit(0);
//                }
//            }
//        }

        return new Population(offspring, fitnessFunction);
    }

    @Override
    public Population crossover(ArrayList<Individual> parents, FitnessFunction fitnessFunction, double crossoverRate, int k) throws ScriptException {
        return null;
    }
}

