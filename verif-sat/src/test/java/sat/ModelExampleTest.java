package sat;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import org.junit.Assert;
import org.junit.Test;

public class ModelExampleTest {

    @Test
    public void testModel() {
        // Create a new context
        Context ctx = new Context();
        // Create boolean variables
        BoolExpr p = ctx.mkBoolConst("p");
        BoolExpr q = ctx.mkBoolConst("q");
        // Build formula p <-> q
        BoolExpr f = ctx.mkIff(p, q);
        // Create a solver
        Solver solver = ctx.mkSolver();
        // Add the formula
        solver.add(f);
        // Check satisfiability
        Status status = solver.check();
        Assert.assertEquals(Status.SATISFIABLE, status);
        // System.out.println(status);
        // Get a model
        Model model = solver.getModel();
        Expr pVal = model.getConstInterp(p);
        Expr qVal = model.getConstInterp(q);
        Assert.assertEquals(pVal, qVal);
        // System.out.println("p: " + pVal);
        // System.out.println("q: " + qVal);
    }
}
