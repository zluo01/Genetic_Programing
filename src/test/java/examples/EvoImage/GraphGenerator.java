package examples.EvoImage;

import genetics.Generator;
import org.apache.commons.math3.genetics.Chromosome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphGenerator implements Generator {

    private EvoManager manager;

    GraphGenerator(EvoManager manager) {
        this.manager = manager;
    }

    @Override
    public List<Chromosome> generate() {
        List<Polygon> polygons = new ArrayList<>();
        for (int i = 0; i < manager.MAX_SHAPES; i++) {
            Polygon polygon = new Polygon(manager.MAX_POINTS);
            for (int j = 0; j < manager.MAX_POINTS; j++) {
                polygon.add(j, manager.randEngine.nextInt(manager.MAX_WIDTH), manager.randEngine.nextInt(manager.MAX_HEIGHT));
            }
            manager.choice.apply(polygon);
            polygons.add(polygon);
        }
        return Collections.singletonList(new Paintings(polygons, manager));
    }
}
