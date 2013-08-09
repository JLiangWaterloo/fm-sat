import java.util.Random;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.minisat.core.Solver;
import org.sat4j.minisat.learning.NoLearningNoHeuristics;
import org.sat4j.minisat.restarts.NoRestarts;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.SearchListenerAdapter;

/**
 *
 * @author jimmy
 */
public class Backjump {

    private static final Random rand = new Random();

    public static void main(String[] args) throws ContradictionException, TimeoutException {
        double density = 4.25;
        int nVars = 100;
        int nClauses = (int) (nVars * density);
        final Solver solver = (Solver) SolverFactory.newDefault();
        solver.setLearningStrategy(new NoLearningNoHeuristics<>());
        solver.setRestartStrategy(new NoRestarts());

        // Random 3Sat
        for (int clause = 0; clause < nClauses; clause++) {
            solver.addClause(new VecInt(new int[]{
                randLit(nVars), randLit(nVars), randLit(nVars)}));
        }

        solver.setSearchListener(new SearchListenerAdapter() {
            @Override
            public void learn(IConstr c) {
                System.out.println("learn:" + c);
            }

            @Override
            public void learnUnit(int p) {
                System.out.println("learnUnit:" + p);
            }

            @Override
            public void conflictFound(int p) {
                System.out.println("conflictFound:" + p);
            }

            @Override
            public void conflictFound(IConstr confl, int dlevel, int trailLevel) {
                System.out.println("conflictFound:" + confl + ", " + dlevel + ", " + trailLevel);
            }

            @Override
            public void backjump(int backjumpLevel) {
                System.out.println("backjump:" + backjumpLevel);
            }
        });

        System.out.println(solver.isSatisfiable() ? "Is sat" : "Is unsat");
    }

    private static int randLit(int maxVar) {
        int var = rand.nextInt(maxVar) + 1;
        return rand.nextBoolean() ? var : -var;
    }
}
