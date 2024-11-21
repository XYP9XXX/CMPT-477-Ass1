package sat;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import org.junit.Assert;
import org.junit.Test;

public class ValidityExampleTest {

    @Test
    public void testValidity() {
        // Create a new context
        Context ctx = new Context();
        // Create boolean variables
        BoolExpr p = ctx.mkBoolConst("p");
        BoolExpr q = ctx.mkBoolConst("q");
        // Build formulas p -> (q -> p)
        BoolExpr f = ctx.mkImplies(p, ctx.mkImplies(q, p));
        // Check validity
        boolean valid = isValid(ctx, f);
        Assert.assertEquals(true, valid);
        // System.out.println("Is " + f + " valid? " + valid);
    }

    public boolean isValid(Context ctx, BoolExpr formula) {
        Solver solver = ctx.mkSolver();
        BoolExpr negation = ctx.mkNot(formula);
        solver.add(negation);
        return solver.check() == Status.UNSATISFIABLE;
    }
}
