package genetics.executor;

import genetics.chromosome.Chromosome;
import genetics.common.Population;
import genetics.interfaces.FitnessCalc;
import org.eclipse.collections.impl.factory.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class GlobalDistributionExecutor<T extends Chromosome> extends Executor<T> {
  private final ExecutorService executorService;
  private final FitnessCalc<T> fitnessCalc;

  public GlobalDistributionExecutor(FitnessCalc<T> fitnessCalc) {
    this.fitnessCalc = fitnessCalc;
    this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  }

  /**
   * Compute fitness for each child in population in parallel
   *
   * @param population population
   * @return best child
   * @throws InterruptedException interrupted exception
   * @throws ExecutionException execution exception
   */
  @Override
  public T evaluate(Population<T> population) throws InterruptedException, ExecutionException {
    Collection<Callable<T>> callable = Lists.mutable.ofInitialCapacity(population.size());
    population.forEach(
        o ->
            callable.add(
                () -> {
                  o.setFitness(fitnessCalc.calc(o));
                  return o;
                }));
    Optional<T> bestChromosome = Optional.empty();
    List<Future<T>> futures = executorService.invokeAll(callable);
    double bestFitness = Double.MAX_VALUE;
    for (Future<T> future : futures) {
      T chromosome = future.get();
      if (chromosome.getFitness() < bestFitness) {
        bestFitness = chromosome.getFitness();
        bestChromosome = Optional.of(chromosome);
      }
    }
    return bestChromosome.orElse(population.getChromosome(0));
  }

  /** Shut down the executor service */
  @Override
  public void shutDown() {
    executorService.shutdown();
    try {
      if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
        executorService.shutdownNow();
      }
    } catch (InterruptedException e) {
      executorService.shutdownNow();
    }
  }
}
