package com.geneticsoftware.geneticalgorithm.selection;

import com.geneticsoftware.geneticalgorithm.Individual;
import com.geneticsoftware.geneticalgorithm.Population;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SelectionTrancation extends Selection {


    @Override
    public ArrayList<Individual> selection(Population population, int size) {
        Random random = new Random();

        List<Double> sortedFitness = new ArrayList<>(population.getFitness().values());
        Collections.sort(sortedFitness);

        List<Individual> tmp = new ArrayList<>(population.getFitness().keySet());
        Individual[] trunc = new Individual[population.getIndividuals().length];
        int i = 0;
        while(!tmp.isEmpty()){
            int j;
            for(j = 0; sortedFitness.get(i) != population.getFitness().get(tmp.get(j))/* && j < sortedFitness.size()*/; j++);
            trunc[i] = new Individual(tmp.get(j));
            tmp.remove(j);
            i++;
        }

        ArrayList<Individual> parents = new ArrayList<Individual>();
        i = population.getIndividuals().length - 1;
        for (int j = 0; j < size; j++){
            parents.add(trunc[i]);
            i--;
        }
        return parents;
    }

    @Override
    public ArrayList<Individual> selection(Population population) {
        return null;
    }

}
