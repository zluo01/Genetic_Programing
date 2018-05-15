package treegp.gp;

import genetics.Chromosome;
import lombok.Getter;
import lombok.Setter;
import treegp.program.TreeNode;
import treegp.solver.TreeGP;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class TGPChromosome implements Chromosome<TGPChromosome> {

    private TreeNode root;
    private TreeGP manager;

    TGPChromosome(TreeNode root, TreeGP manager) {
        this.root = root;
        this.manager = manager;
    }

    @Override
    public List<TGPChromosome> crossover(TGPChromosome chromosome, double uniformRate) {
        TGPChromosome c1 = makeCopy(), c2 = chromosome.makeCopy();

        Crossover.apply(c1, c2, manager);
        return new LinkedList<>(list(c1, c2));
    }

    @Override
    public TGPChromosome mutate(double mutationRate) {
        TGPChromosome clone = makeCopy();
        if (mutationRate == 0) MicroMutation.apply(clone, manager);
        else MacroMutation.apply(clone, manager);
        return clone;
    }

    @Override
    public TGPChromosome makeCopy() {
        return new TGPChromosome(root.copy(), manager);
    }

    @Override
    public String toString() {
        return root.toString();
    }

    @SafeVarargs
    private final <T> List<T> list(T... items) {
        List<T> list = new LinkedList<>();
        Collections.addAll(list, items);
        return list;
    }

    public double eval() {
        return root.eval();
    }
}

