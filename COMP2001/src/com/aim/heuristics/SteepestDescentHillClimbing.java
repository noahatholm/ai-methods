package com.aim.heuristics;


import java.util.Random;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;

/**
 * Implements the Steepest Descent Hill Climbing heuristic for solving SAT problems.
 * This heuristic explores all possible single-variable flips in the solution representation
 * and selects the one that provides the best improvement in the objective function value.
 * If no improvement is found, the solution remains unchanged.
 *
 * @author Warren G. Jackson
 */
public class SteepestDescentHillClimbing extends SATHeuristic {

	public SteepestDescentHillClimbing(Random oRandom) {
		
		super(oRandom);
	}

	/**
	  * STEEPEST DESCENT HILL CLIMBING LECTURE SLIDE PSEUDO-CODE
	  * <pre>
	  *	bestEval = evaluate(currentSolution)
	  *	for (j = 0; j < length(currentSolution); j++) { // performs a single pass of the solution
	  *
	  *		<t>bitFlip(currentSolution, j) // flip the j^th bit of the solution to produce s' from s
	  *		tempEval = evaluate(solution)
	  *
	  *		if(tempEval < bestEval) {
      *
	  *			// remember which bit led to the most improvement
	  *			bestIndex = j
	  *			bestEval = tempEval
	  *			improved = true
	  *		}
	  *
	  *		bitFlip(currentSolution, j) // revert the bit flip so we can try another
	  *	}
	  *
	  *	if(improved) bitflip(currentSolution, bestIndex)
	  * </pre>
     *
	  * @param oProblem The problem to be solved.
     * @param iSolutionIndex The index of the solution in memory to apply this heuristic to.
	  */
	public void applyHeuristic(SAT oProblem, int iSolutionIndex) {
		int bestEval = oProblem.getObjectiveFunctionValue(iSolutionIndex);
        int bestIndex = -1;
        boolean improved = false;

        for (int i = 0; i < oProblem.getNumberOfVariables();i++){
            oProblem.bitFlip(i,iSolutionIndex);
            int tempEval = oProblem.getObjectiveFunctionValue(iSolutionIndex);
            if (tempEval <= bestEval) {
                bestIndex = i;
                bestEval = tempEval;
                improved = true;

            }
            oProblem.bitFlip(i,iSolutionIndex);
        }
        if (improved) {
            oProblem.bitFlip(bestIndex, iSolutionIndex);
        }
	}

	public String getHeuristicName() {
		
		return "SDHC";
	}

}
