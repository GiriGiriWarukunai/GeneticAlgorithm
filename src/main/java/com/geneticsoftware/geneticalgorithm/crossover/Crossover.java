package com.geneticsoftware.geneticalgorithm.crossover;

import com.geneticsoftware.geneticalgorithm.FitnessFunction;
import com.geneticsoftware.geneticalgorithm.Individual;
import com.geneticsoftware.geneticalgorithm.Population;

import javax.script.ScriptException;
import java.util.ArrayList;

public abstract class Crossover {

    protected int maxCrossoverAttempts = 100;

    public abstract Population crossover(ArrayList<Individual> parents, FitnessFunction fitnessFunction, double crossoverRate) throws ScriptException;

    public abstract Population crossover(ArrayList<Individual> parents, FitnessFunction fitnessFunction, double crossoverRate, double alpha) throws ScriptException;

    public abstract  Population crossover(ArrayList<Individual> parents, FitnessFunction fitnessFunction, double crossoverRate, int k) throws ScriptException;

}

