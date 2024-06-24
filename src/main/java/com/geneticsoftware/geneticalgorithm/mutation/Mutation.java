package com.geneticsoftware.geneticalgorithm.mutation;

import com.geneticsoftware.geneticalgorithm.Population;

import javax.script.ScriptException;

public abstract class Mutation {

    protected int maxMutationAttempts = 100;

    public abstract Population mutation(Population population, double mutationRate) throws ScriptException;

    public abstract Population mutation(Population population, double mutationRate, int first, int second) throws ScriptException;

}
