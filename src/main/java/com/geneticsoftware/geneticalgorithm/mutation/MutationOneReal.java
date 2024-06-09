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
            Individual individual = new Individual();
            double[] tmpChromosome = new double[res.getIndividuals()[i].getChromosomes()[0].getChromosomeSize()];
//            Chromosome[] tmp = new Chromosome[res.getIndividuals()[i].getCountChromosomes()];
            int mutationAttempts = 0;
            do {
//                for (int j = 0; j < res.getIndividuals()[i].getCountChromosomes(); j++) {

                tmpChromosome = res.getIndividuals()[i].getChromosomes()[0].getChromosome();
                Random random = new Random();
                if (random.nextDouble() < mutationRate) {
                    int idx = random.nextInt(res.getIndividuals()[i].getChromosomes()[0].getChromosomeSize()/* - 1*/);


//                    String pyScript = "random.triangular(" + population.getFitnessFunction().getMinArgument()[idx] + ", " + population.getFitnessFunction().getMaxArgument()[idx] + ", random.gauss(" + tmpChromosome[idx] + ", sigma))";
////                    String pyScript = "print \"UWU\"";
//
//                    ScriptEngineManager manager = new ScriptEngineManager();
//                    ScriptEngine engine = manager.getEngineByName("jython");
//                    double newGen = ((Number) engine.eval(pyScript)).doubleValue();

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
//               }

                // System.out.println("Временная хромосома в мутации: " + tmp);
                // System.out.println("Итоговая хромосома после мутации: " + chromosomes[i] + "\n");
                mutationAttempts++;

            } while (!(res.getIndividuals()[0].checkChromosomes(tmpChromosome, res.getFitnessFunction())) && mutationAttempts < maxMutationAttempts);

            if (mutationAttempts < maxMutationAttempts && res.getIndividuals()[0].checkChromosomes(tmpChromosome, res.getFitnessFunction())) {
                Chromosome[] tmp = new Chromosome[1];
                tmp[0] = new Chromosome(tmpChromosome);
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
}
