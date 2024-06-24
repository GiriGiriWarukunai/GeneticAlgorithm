package com.geneticsoftware.geneticalgorithm.selection;

import com.geneticsoftware.geneticalgorithm.Individual;
import com.geneticsoftware.geneticalgorithm.Population;

import java.util.ArrayList;
import java.util.Random;

public class SelectionRouletteWheel extends Selection {
    @Override
    public ArrayList<Individual> selection(Population population) {
        Random random = new Random();
        double totalFitness = 0;
        if (population.getFitnessFunction().isFindMin()) {
            for(int i = 0; i < population.getIndividuals().length; i++){
                totalFitness += 1/-population.getFitness().get(population.getIndividuals()[i]);
            }
        }
        else{
            totalFitness = population.calculateTotalFitness();
        }

        double[] wheelSectors = new double[population.getIndividuals().length];

        if(population.getFitnessFunction().isFindMin()){
            wheelSectors[0] = (1/-population.getFitness().get(population.getIndividuals()[0])) / totalFitness;
        }
        else{
            wheelSectors[0] = population.getFitness().get(population.getIndividuals()[0]) / totalFitness;
        }

        StringBuilder str = new StringBuilder("сектора рулетки: " + wheelSectors[0]);

        for (int i = 1; i < population.getIndividuals().length - 1; i++){
            if(population.getFitnessFunction().isFindMin()){
                wheelSectors[i] = wheelSectors[i - 1] + (1/-population.getFitness().get(population.getIndividuals()[i])) / totalFitness;
            }
            else{
                wheelSectors[i] = wheelSectors[i - 1] + population.getFitness().get(population.getIndividuals()[i]) / totalFitness;
            }
            str.append(" ").append(wheelSectors[i]);

        }

        wheelSectors[population.getIndividuals().length - 1] = 1.0;

        str.append(" ").append(wheelSectors[population.getIndividuals().length - 1]);
       // System.out.println(str + "\nродители:");

        ArrayList<Individual> parents = new ArrayList<Individual>();
        for (int i = 0; i < population.getIndividuals().length; i++){
            double wheelPosition = random.nextDouble();
            int j;
            for(j = 0; wheelSectors[j] < wheelPosition; j++);
            parents.add(population.getIndividuals()[j]);
//            System.out.println(individuals[j].toString());
        }
        return parents;
    }

    @Override
    public ArrayList<Individual> selection(Population population, int size) {
        return null;
    }
}
