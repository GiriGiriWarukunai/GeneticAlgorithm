package com.geneticsoftware.geneticalgorithm.mutation;

import com.geneticsoftware.geneticalgorithm.Chromosome;
import com.geneticsoftware.geneticalgorithm.Individual;
import com.geneticsoftware.geneticalgorithm.Population;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Random;

public class MutationOneReal extends Mutation {
    @Override
    public Population mutation(Population population, double mutationRate) throws ScriptException {
        Population res = new Population(population);
        for (int i = 0; i < res.getIndividuals().length; i++) {
            double[] tmpChromosome = new double[res.getIndividuals()[i].getChromosomes()[0].getChromosomeSize()];
            int mutationAttempts = 0;
            do {
                tmpChromosome = res.getIndividuals()[i].getChromosomes()[0].getChromosome();
                Random random = new Random();
                if (random.nextDouble() < mutationRate) {
                    int idx = random.nextInt(res.getIndividuals()[i].getChromosomes()[0].getChromosomeSize()/* - 1*/);

                    double d = 0;
                    if (tmpChromosome[idx] - population.getFitnessFunction().getMinArgument()[idx] > population.getFitnessFunction().getMaxArgument()[idx] - tmpChromosome[idx]){
                        d = (population.getFitnessFunction().getMaxArgument()[idx] - tmpChromosome[idx]) / 2;
                    }
                    else {
                        d = (tmpChromosome[idx] - population.getFitnessFunction().getMinArgument()[idx]) / 2;
                    }
                    double gaussianRandom = random.nextGaussian() * d + tmpChromosome[idx];
                    //double newGen = population.getFitnessFunction().getMinArgument()[idx] + (population.getFitnessFunction().getMaxArgument()[idx] - population.getFitnessFunction().getMinArgument()[idx]) * random.nextDouble();
                    tmpChromosome[idx] = gaussianRandom;
                }
                // System.out.println("Временная хромосома в мутации: " + tmp);
                // System.out.println("Итоговая хромосома после мутации: " + chromosomes[i] + "\n");
                mutationAttempts++;

            } while (!(res.getIndividuals()[0].checkChromosomes(tmpChromosome, res.getFitnessFunction())) && mutationAttempts < maxMutationAttempts);

            if (mutationAttempts < maxMutationAttempts && res.getIndividuals()[0].checkChromosomes(tmpChromosome, res.getFitnessFunction())) {
                Chromosome[] tmp = new Chromosome[1];
                tmp[0] = new Chromosome(tmpChromosome);
                res.getIndividuals()[i].setChromosomes(tmp);
            }
            else {
                Chromosome[] tmp = new Chromosome[1];
                tmp[0] = new Chromosome(population.getIndividuals()[i].getChromosomes()[0]);
                res.getIndividuals()[i].setChromosomes(tmp);
            }
//            System.out.println("Особь после мутации: " + individuals[i] + "\n");

            double[] tmp2 = res.getIndividuals()[i].getChromosomes()[0].getChromosome();
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
    }

    @Override
    public Population mutation(Population population, double mutationRate, int first, int second) throws ScriptException {
        return null;
    }
}
