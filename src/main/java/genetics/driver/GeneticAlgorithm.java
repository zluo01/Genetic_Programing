package genetics.driver;

import genetics.chromosome.Chromosome;
import genetics.common.FitnessCalc;
import genetics.common.Interrupt;
import genetics.common.Population;
import genetics.interfaces.CrossoverPolicy;
import genetics.interfaces.MutationPolicy;
import genetics.utils.RandEngine;
import genetics.utils.SimpleRandEngine;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

import java.util.*;

public class GeneticAlgorithm {

    protected final ChromosomesComparator comparator;
    private final RandEngine randEngine = new SimpleRandEngine();
    private final List<Interrupt> interrupts = new LinkedList<>();
    private final FitnessCalc fitnessCalc;
    protected Population population;
    private CrossoverPolicy crossoverPolicy;
    private MutationPolicy mutationPolicy;
    private double uniformRate;
    private double mutationRate;
    private int tournamentSize;
    private int elitism;
    private boolean terminate;
    private int generation;

    public GeneticAlgorithm(Population population,
                            final FitnessCalc fitnessCalc,
                            final CrossoverPolicy crossoverPolicy,
                            final double uniformRate,
                            final MutationPolicy mutationPolicy,
                            final double mutationRate,
                            final int tournamentSize,
                            final int elitism) {
        this.population = population;
        this.fitnessCalc = fitnessCalc;
        this.crossoverPolicy = crossoverPolicy;
        this.uniformRate = uniformRate;
        this.mutationPolicy = mutationPolicy;
        this.mutationRate = mutationRate;
        this.tournamentSize = tournamentSize;
        this.elitism = elitism;
        comparator = new ChromosomesComparator();
        population.sort(comparator);
    }

    protected GeneticAlgorithm(Population population,
                               final FitnessCalc fitnessCalc) {
        this.population = population;
        this.fitnessCalc = fitnessCalc;
        comparator = new ChromosomesComparator();
        population.sort(comparator);
    }

    public void evolve(int iteration) {
        terminate = false;
        for (int i = 0; i < iteration; i++) {
            if (terminate) {
                break;
            }
            population = evolvePopulation();
            generation = i;
            for (Interrupt l : interrupts) {
                l.update(this);
            }
        }
    }

    public void evolve() {
        terminate = false;
        generation = 0;
        while (!terminate) {
            population = evolvePopulation();
            generation++;
            for (Interrupt l : interrupts) {
                l.update(this);
            }
        }
    }

    protected Population evolvePopulation() {
        final int populationSize = population.size();
        Population newPopulation = new Population();

        // Keep our best individual, reproduction
        for (int i = 0; (i < populationSize) && (i < elitism); i++) {
            newPopulation.addChromosome(population.getChromosome(i));
        }

        for (int i = elitism; i < population.size(); i++) {
            Chromosome c1 = tournamentSelection(tournamentSize);
            Chromosome c2 = tournamentSelection(tournamentSize);
            Pair<Chromosome, Chromosome> pair = Tuples.pair(c1, c2);
            if (randEngine.uniform() < uniformRate) {
                pair = crossoverPolicy.crossover(pair.getOne(), pair.getTwo());
            }

            if (randEngine.uniform() < mutationRate) {
                pair = Tuples.pair(
                        mutationPolicy.mutate(pair.getOne()),
                        mutationPolicy.mutate(pair.getTwo()));
            }
            newPopulation.addChromosome(pair.getOne());
            newPopulation.addChromosome(pair.getTwo());
        }
        newPopulation.sort(comparator);
        newPopulation.trim(populationSize);
        return newPopulation;
    }

    public void addIterationListener(Interrupt listener) {
        interrupts.add(listener);
    }

    public void terminate() {
        terminate = true;
    }

    public int getGeneration() {
        return generation;
    }

    public Population getPopulation() {
        return population;
    }

    public Chromosome getBest() {
        return population.getBest();
    }

    /**
     * select chromosome from population
     *
     * @return best chromosome in the selecting group
     */
    protected Chromosome tournamentSelection(int tournamentSize) {
        assert tournamentSize < population.size();
        List<Chromosome> selection = new ArrayList<>();
        List<Chromosome> chromosomes = new ArrayList<>(population.getChromosomes());
        for (int i = 0; i < tournamentSize; i++) {
            int rind = randEngine.nextInt(chromosomes.size());
            selection.add(chromosomes.get(rind));
            chromosomes.remove(rind);
        }
        selection.sort(comparator);
        return selection.get(0);
    }

    protected class ChromosomesComparator implements Comparator<Chromosome> {
        private final Map<Chromosome, Double> cache = new WeakHashMap<>();

        @Override
        public int compare(Chromosome e1, Chromosome e2) {
            return Double.compare(fit(e1), fit(e2));
        }

        Double fit(Chromosome e) {
            Double fit = cache.get(e);
            if (fit == null) {
                fit = fitnessCalc.calc(e);
                e.fitness = fit;
                cache.put(e, fit);
            }
            return fit;
        }
    }
}