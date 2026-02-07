package com.aim.pseudorandom;

import java.util.Random;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;


/**
 * A heuristic for solving SAT (Satisfiability) problems by randomly flipping a single bit
 * in a solution. This heuristic is a basic local operator used to explore the solution space
 * of a SAT problem.
 *
 * The RandomBitFlipHeuristic selects a random variable (bit) in the solution and inverts its value.
 * This process is repeated each time the heuristic is applied, enabling random exploration of the
 * solution space.
 *
 * @author Warren G. Jackson
 */
public class RandomBitFlipHeuristic extends SATHeuristic {

    /**
     * Constructs a new RandomBitFlipHeuristic instance with the given random number generator.
     *
     * This heuristic selects a random variable (bit) in a solution and flips its value.
     * Randomness is driven by the provided Random instance, which is seeded for experimental repeatability.
     *
     * @param oRandom A random number generator used to determine the bit to flip.
     */
	public RandomBitFlipHeuristic(Random oRandom) {
		
		super(oRandom);
	}

	@Override
	public void applyHeuristic(SAT oProblem, int iSolutionIndex) {
		// TODO - deterministically select a random bit in the solution stored in <code>iSolutionIndex</code> and flip it.
        int x = oProblem.getNumberOfVariables();
        int y = m_oRandom.nextInt(0,x);
        //System.out.println(y);
        oProblem.bitFlip(y, iSolutionIndex);
	}

	@Override
	public String getHeuristicName() {
		
		return "Random Bit Flip";
	}
}
