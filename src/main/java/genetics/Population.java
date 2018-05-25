package genetics;

import org.apache.commons.math3.genetics.Chromosome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Population implements Iterable<Chromosome> {

    private List<Chromosome> chromosomes;

    public Population(Generator generator) {
        chromosomes = generator.generate();
    }

    public Population() {
        chromosomes = new ArrayList<>();
    }

    public Chromosome getChromosome(int index) {
        return chromosomes.get(index);
    }

    public void addChromosome(Chromosome chromosome) {
        chromosomes.add(chromosome);
    }

    public int size() {
        return chromosomes.size();
    }

    public void sort(Comparator<Chromosome> comparator) {
        chromosomes.sort(comparator);
    }

    public Chromosome getFirst() {
        return chromosomes.get(0);
    }

    public List<Chromosome> getChromosomes() {
        return chromosomes;
    }

    public void trim(int length) {
        chromosomes.subList(length, chromosomes.size()).clear();
    }

    @Override
    public Iterator<Chromosome> iterator() {
        return chromosomes.iterator();
    }
}