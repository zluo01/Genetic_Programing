package examples.Symbolic_engine;

import genetics.utils.Observation;
import lgp.gp.LGPChromosome;
import lgp.solver.LGPSolver;
import lgp.solver.LinearGP;
import lombok.Setter;

import java.util.List;

@Setter
public class LGPTest {

    public static void main(String[] args) {
        List<Observation> list = Test.function();

        int split_point = (int) (list.size() * .9);
        List<Observation> training = list.subList(0, split_point);
        List<Observation> testing = list.subList(split_point, list.size());
        System.out.printf("Training: %d\t Testing: %d\t\n", training.size(), testing.size());

        LinearGP gp = LinearGP.defaultConfig();
        gp.addObservations(training);
        gp.setRegisterCount(3);
        LGPSolver solver = new LGPSolver(gp);
        addListener(solver);
        Long startTime = System.currentTimeMillis();
        solver.evolve(10000);
        System.out.println((System.currentTimeMillis() - startTime) / 1000.0);

        test(solver, testing, false);
    }

    private static void test(LGPSolver solver, List<Observation> observations, boolean silent) {
        for (Observation o : observations) {
            solver.getBestGene().eval(o);
            double predicted = o.getPredictedOutput(0);
            double actual = o.getOutput(0);

            if (!silent) {
                System.out.printf("predicted: %f\t actual: %f\t difference: %f\t\n",
                        predicted, actual, Math.abs(predicted - actual));
            }
        }
    }

    private static void addListener(LGPSolver engine) {
        engine.addIterationListener(engine1 -> {

            LGPChromosome bestGene = engine1.getBestGene();

            double bestFit = engine1.fitness(bestGene);

            // log to console
            System.out.printf("Generation = %s \t fit = %s \n", engine1.getIteration(), bestFit);

            // halt condition
            if (bestFit < 5) {
                engine1.terminate();
                System.out.println(bestGene);
            }
        });
    }
}
