package sat;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import org.junit.Assert;
import org.junit.Test;

public class SatExampleTest {

    @Test
    public void testSat() {
        // Create a new context
        Context ctx = new Context();
        // Create boolean variables
        BoolExpr p = ctx.mkBoolConst("p");
        BoolExpr q = ctx.mkBoolConst("q");
        BoolExpr r = ctx.mkBoolConst("r");
        BoolExpr s = ctx.mkBoolConst("s");
        // Build formulas
        BoolExpr f1 = ctx.mkOr(p, q, r);
        BoolExpr f = ctx.mkIff(f1, s);
        // Create a solver
        Solver solver = ctx.mkSolver();
        // Add formulas
        solver.add(f);
        // Check satisfiability
        Status status = solver.check();
        Assert.assertEquals(Status.SATISFIABLE, status);
        // System.out.println(status);
    }
}
