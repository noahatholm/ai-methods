package com.aim.heuristics;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.helperfunctions.ArrayMethods;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;


import java.util.ArrayList;
import java.util.Random;

/**
 * Implements Davis's Bit Hill Climbing heuristic for solving SAT problems.
 * This heuristic explores the solution by flipping variables in the current
 * solution representation as per a random permutation order of variable indices.
 * Each bit flip is evaluated and accepted only if it improves the objective
 * function value. If no improvement is found, the bit flip is reverted.
 *
 * @author Warren G. Jackson
 */
public class DavissBitHillClimbing extends SATHeuristic {

    public DavissBitHillClimbing(Random oRandom) {

        super(oRandom);
    }

    /**
     * DAVIS's BIT HILL CLIMBING LECTURE SLIDE PSEUDO-CODE
     *
     * <pre>
     *  bestEval = evaluate(currentSolution)
     *  perm = createRandomPermutation(length(currentSolution))
     *   for (j = 0; j < length(currentSolution); j++) { // performs a single pass of the solution
     *
     *       bitFlip(currentSolution, perm[j]) // flip the bit referenced to in perm's j^th index
     *       tempEval = evaluate(solution)
     *   
     *       if(tempEval < bestEval) {
     *           bestEval = tempEval // accept the bit flip
     *       } else {
     *           bitFlip(currentSolution, perm[j]) // otherwise reject the bit flip
     *       }
     *   }
     *   </pre>
     *
     * @param oProblem The problem to be solved.
     * @param iSolutionIndex The index of the solution to be modified.
     */
    public void applyHeuristic(SAT oProblem, int iSolutionIndex) {

        int bestEval = oProblem.getObjectiveFunctionValue(iSolutionIndex);

        ArrayList<Integer> order = new ArrayList<Integer>();
        for (int i = 0; i < oProblem.getNumberOfVariables(); i++) {
            order.add(i);
        }

        ArrayList<Integer> shuffled = ArrayMethods.shuffle(order,m_oRandom);

        for (int i = 0; i < shuffled.size(); i++ ) {
            oProblem.bitFlip(shuffled.get(i), iSolutionIndex);
            int tempEval = oProblem.getObjectiveFunctionValue(iSolutionIndex);
            if (tempEval <= bestEval) {
                bestEval = tempEval;
            } else{
                oProblem.bitFlip(shuffled.get(i), iSolutionIndex);
            }

        }


    }

    @Override
    public String getHeuristicName() {

        return "DBHC";
    }
}
