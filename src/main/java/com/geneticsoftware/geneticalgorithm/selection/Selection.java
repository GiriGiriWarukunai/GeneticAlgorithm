package com.geneticsoftware.geneticalgorithm.selection;

import com.geneticsoftware.geneticalgorithm.Individual;
import com.geneticsoftware.geneticalgorithm.Population;

import java.util.ArrayList;

public abstract class Selection {

    public abstract ArrayList<Individual> selection(Population population);

    public abstract ArrayList<Individual> selection(Population population,int size);

}
