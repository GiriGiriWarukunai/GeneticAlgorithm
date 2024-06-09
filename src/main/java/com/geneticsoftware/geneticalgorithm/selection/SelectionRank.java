package com.geneticsoftware.geneticalgorithm.selection;

import com.geneticsoftware.geneticalgorithm.Individual;
import com.geneticsoftware.geneticalgorithm.Population;

import java.util.*;

public class SelectionRank extends Selection {

    @Override
    public ArrayList<Individual> selection(Population population) {
        Random random = new Random();

        List<Double> sortedFitness = new ArrayList<>(population.getFitness().values());
        Collections.sort(sortedFitness);

        List<Individual> tmp = new ArrayList<>(population.getFitness().keySet());
        Individual[] rank = new Individual[population.getIndividuals().length];
        int i = 0;
        while(!tmp.isEmpty()){
            int j;
            for(j = 0; sortedFitness.get(i) != population.getFitness().get(tmp.get(j))/* && j < sortedFitness.size()*/; j++);
            rank[i] = new Individual(tmp.get(j));
            tmp.remove(j);
            i++;
        }

        double sumRank = 0;
        for(int j = 0; j < rank.length; j++){
            sumRank += j + 1;
        }
        double[] wheelSectors = new double[rank.length];
        wheelSectors[0] = 1 / sumRank;
        for(int j = 1; j < wheelSectors.length - 1; j++){
            wheelSectors[j] = wheelSectors[j - 1] + (j + 1) / sumRank;
        }
        wheelSectors[wheelSectors.length - 1] = 1.0;

        ArrayList<Individual> parents = new ArrayList<Individual>();
        for (int j = 0; j < population.getIndividuals().length; j++){
            double wheelPosition = random.nextDouble();
            int k;
            for(k = 0; wheelSectors[k] < wheelPosition; k++);
            parents.add(rank[k]);
        }
        return parents;
    }

    @Override
    public ArrayList<Individual> selection(Population population, int size) {
        return null;
    }
}
