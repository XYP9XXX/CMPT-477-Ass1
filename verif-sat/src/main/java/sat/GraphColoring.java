package sat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;

public class GraphColoring {
    public static void main(String[] args) {
        // Get the input file and output file path
        String inPath = args[0];
        String outPath = args[1];

        // Then we need to parsing the file.
        // At first, create a arraylist to store each edges.
        List<int[]> edges = new ArrayList<>();

        // Two int variables to store number of vertices and number of colors.
        int numVertices = 0, numColors = 0;

        // Using bufferedReader to read the input file.
        try (BufferedReader br = new BufferedReader(new FileReader(inPath))) {
            // The format is "X X",split by " ".
            String[] firstLine = br.readLine().split(" ");

            // First number of first line is the number of vertices
            numVertices = Integer.parseInt(firstLine[0]);

            // Second number of first line is the number of colors.
            numColors = Integer.parseInt(firstLine[1]);

            // Create a string variable to get the information of each line.
            String line;

            // Traverse each line until end.
            while ((line = br.readLine()) != null) {
                // Except first line, each line is corresponding to a edge.
                String[] edge = line.split(" ");

                // Add edge to the edges arraylist.
                edges.add(new int[]{Integer.parseInt(edge[0]) - 1, Integer.parseInt(edge[1]) - 1});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Using Z3 to solve the question
        solveGraphColoring(numVertices, numColors, edges, outPath);
    }

    public static void solveGraphColoring(int numVertices, int numColors, List<int[]> edges, String outputFile) {
        Context ctx = new Context();
        
        // Create a booling 2-D array to store all the possible cases for each node.
        BoolExpr[][] colorVars = new BoolExpr[numVertices][numColors];
        for (int v = 0; v < numVertices; v++) {
            for (int c = 0; c < numColors; c++) {
                // p_1_1 means assign vertex 1 color 1. 
                colorVars[v][c] = ctx.mkBoolConst("p_" + v + "_" + c);
            }
        }

        // Create a solver
        Solver solver = ctx.mkSolver();

        // We need to make sure each point is at least be assigned one color.
        for (int v = 0; v < numVertices; v++) {
            BoolExpr[] atLeastOneColor = new BoolExpr[numColors];
            for (int c = 0; c < numColors; c++) {
                // If a vertex v is assigned color c, then atLeastOneColor[c] will be true.
                atLeastOneColor[c] = colorVars[v][c];
            }

            // [v][c1] or [v][c2] or [v][c3] or ... [v][cn]
            solver.add(ctx.mkOr(atLeastOneColor));
        }

        // We need to mkae sure each point is at most be assigned one color
        for (int v = 0; v < numVertices; v++) {
            for (int c1 = 0; c1 < numColors; c1++) {
                for (int c2 = c1 + 1; c2 < numColors; c2++) {
                    // Not ([v][c1] and [v][c2]) ...
                    solver.add(ctx.mkNot(ctx.mkAnd(colorVars[v][c1], colorVars[v][c2])));
                }
            }
        }

        // We need to make sure that each adjacent vertex can not be assigned same color
        for (int[] edge : edges) {
            int v1 = edge[0], v2 = edge[1];
            for (int c = 0; c < numColors; c++) {
                solver.add(ctx.mkNot(ctx.mkAnd(colorVars[v1][c], colorVars[v2][c])));
            }
        }

        // Solve
        if (solver.check() == Status.SATISFIABLE) {
            // If the status is satisfiable, then there is a solution.
            // Get model give us a contains all variable assignments that satisfy the constraints.
            Model model = solver.getModel();

            // Then write suitable solution to the .txt file
            writeSolution(numVertices, numColors, colorVars, model, outputFile);
        } else {
            // Otherwise no solution
            writeNoSolution(outputFile);
        }
    }

    public static void writeSolution(int numVertices, int numColors, BoolExpr[][] colorVars, Model model, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (int v = 0; v < numVertices; v++) {
                for (int c = 0; c < numColors; c++) {
                    // This evaluate with second argument false returns only the current value in the model, without being affected by other constraints
                    if (model.evaluate(colorVars[v][c], false).isTrue()) {
                        writer.write((v + 1) + " " + (c + 1));
                        writer.newLine();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeNoSolution(String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("No Solution");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

