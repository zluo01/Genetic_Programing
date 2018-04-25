package com.zluo.ga;

import java.util.*;

public class GeneticAlgorithm<E extends Chromosome<E>> {

    private static final double uniformRate = 0.4;
    private static final double mutationRate = 0.02;
    private static final int tournamentSize = 3;
    private static final int elitism = 1;
    protected final ChromosomesComparator comparator;
    private final List<Interrupt<E>> interrupts = new LinkedList<>();
    protected Population<E> pop;
    protected Fitness<E> fitness;
    private boolean terminate;
    private int generation;

    public GeneticAlgorithm(Population<E> pop, Fitness<E> fitness) {
        this.pop = pop;
        this.fitness = fitness;
        comparator = new ChromosomesComparator();
        pop.sort(comparator);
    }

    public void evolve(int iteration) {
        terminate = false;
        for (int i = 0; i < iteration; i++) {
            if (terminate) {
                break;
            }
            pop = evolvePopulation();
            generation = i;
            for (Interrupt<E> l : interrupts) {
                l.update(this);
            }
        }
    }

    protected Population<E> evolvePopulation() {
        int populationSize = pop.size();
        Population<E> newPopulation = new Population<>();

        // Keep our best individual
        pop.sort(comparator);
        for (int i = 0; (i < populationSize) && (i < elitism); i++) {
            newPopulation.addChromosome(pop.getChromosome(i));
        }

        // crossover
        for (int i = elitism; i < pop.size(); i++) {
            E gene1 = tournamentSelection();
            E gene2 = tournamentSelection();
            List<E> newGene = gene1.crossover(gene2, uniformRate);
            for (E c : newGene) {
                newPopulation.addChromosome(c);
            }
        }

        // Mutate pop
        for (int i = elitism; i < newPopulation.size(); i++)
            newPopulation.getChromosome(i).mutate(mutationRate);

        newPopulation.sort(comparator);
        newPopulation.trim(populationSize);
        return newPopulation;
    }

    public void addIterationListener(Interrupt<E> listener) {
        interrupts.add(listener);
    }

    public void terminate() {
        terminate = true;
    }

    public int getIteration() {
        return generation;
    }

    public E getBest() {
        return pop.getFirst();
    }

    public E getWorst() {
        return pop.getLast();
    }

    protected E tournamentSelection() {
        Population<E> tournament = new Population<>();
        for (int i = 0; i < tournamentSize; i++)
            tournament.addChromosome(pop.getChromosome((int) (Math.random() * pop.size())));
        tournament.sort(comparator);
        return tournament.getFirst();
    }

    protected class ChromosomesComparator implements Comparator<E> {

        private final Map<E, Double> cache = new WeakHashMap<>();

        @Override
        public int compare(E e1, E e2) {
            return Double.compare(fit(e1), fit(e2));
        }

        Double fit(E e) {
            Double fit = cache.get(e);
            if (fit == null) {
                fit = fitness.calc(e);
                cache.put(e, fit);
            }
            return fit;
        }
    }
}