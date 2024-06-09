package com.geneticsoftware.geneticalgorithm.selection;

import com.geneticsoftware.geneticalgorithm.Individual;
import com.geneticsoftware.geneticalgorithm.Population;

import java.util.ArrayList;
import java.util.Random;

public class SelectionTournament extends Selection {
    @Override
    public ArrayList<Individual> selection(Population population) {
        Random random = new Random();
        ArrayList<Individual> parents = new ArrayList<Individual>();
        for (int i = 0; i < population.getIndividuals().length; i++){
            int countIdx;

            if(population.getIndividuals().length >= 10){
                countIdx = population.getIndividuals().length / 10;
                countIdx += countIdx % 2;
            }
            else if (population.getIndividuals().length > 2){
                countIdx = population.getIndividuals().length / 2;
                countIdx -= countIdx % 2;
            }
            else {
                countIdx = 2;
            }

            ArrayList<Integer> idx = new ArrayList<>(countIdx);

            for (int j = 0; j < countIdx; j++){
                int tmp;
                do {
                    tmp = random.nextInt(population.getIndividuals().length);
                } while(idx.contains(tmp));
                idx.add(tmp);
            }

            Individual parent = new Individual(population.bestFitnessByIdx(idx));
            parents.add(parent);
//            System.out.println(individuals[j].toString());
        }
        return parents;
    }

    @Override
    public ArrayList<Individual> selection(Population population, int size) {
        return null;
    }
}
