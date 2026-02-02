package com.aim.pseudorandom;

import java.util.Random;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;


/**
 * The RandomWalk class implements a random walk algorithm for solving SAT (Satisfiability) problems
 * using a random bit flip heuristic. This stochastic search algorithm navigates the solution space
 * by iteratively applying random changes to an initial solution.
 *
 * The following operations are supported:
 * - Generating a random initial solution.
 * - Applying the random bit flip heuristic.
 * - Measuring the execution time.
 *
 * This class encapsulates the logic required for performing the search and tracking its execution metrics.
 *
 * @author Warren G. Jackson
 */
public class RandomWalk {

    /**
     * An instance of the SAT problem to be solved.
     *
     * This variable represents the SAT (Satisfiability) problem
     * domain and encapsulates the problem's structure, variables,
     * clauses, and associated functionality needed to solve it.
     *
     * The SAT instance is used to:
     * - Generate initial solutions.
     * - Evaluate objective function values.
     * - Check termination conditions.
     * - Perform operations required by heuristics, such as random bit flips.
     *
     * The implementation supports the random walk search algorithm,
     * whereby random heuristic-driven changes (e.g., bit flips) are
     * applied to navigate through the solution space.
     */
	private final SAT sat;

    /**
     * Represents a random bit flip heuristic used within the RandomWalk class to solve
     * SAT (Satisfiability) problems. This heuristic operates by selecting a random
     * variable (bit) within a given solution and flipping its value to facilitate exploration
     * of the solution space.
     *
     * The random bit flip heuristic is a fundamental operator for stochastic local optimisation
     * in SAT problems, enabling exploration of the solution search space. Its behaviour
     * is driven by a random number generator for repeatable experiments.
     */
	private final SATHeuristic randomBitFlip;

    /**
     * Represents the execution time of the random walk search algorithm in seconds.
     *
     * This variable stores the total CPU time taken for the execution of the
     * random walk search using the random bit flip heuristic. The time is computed
     * by measuring the difference between the start and end of the algorithm's run
     * and converting it into seconds.
     */
    private double timeTakenSeconds;

    /**
     * Constructs a RandomWalk search algorithm using a given SAT problem instance and
     * a specified random number generator.
     *
     * @param sat The SAT problem instance to solve.
     * @param random The random number generator used to create random initial solutions
     *               and drive the random bit flip heuristic.
     */
	public RandomWalk(SAT sat, Random random) {
		
		this.sat = sat;
		this.randomBitFlip = new RandomBitFlipHeuristic(random);
	}

    /**
     * Executes the random walk search algorithm using the random bit flip heuristic.
     *
     * The execution involves generating a random initial solution, iteratively applying the
     * random bit flip heuristic to navigate the solution space, and checking an evaluation
     * limit as termination criteria. The CPU time taken for the search process is recorded.
     *
     * What is going on here:
     * - Initialises a random solution for the SAT problem instance.
     * - Iteratively applies the random bit flip heuristic to change the solution.
     * - Continues until the evaluation limit is reached.
     * - Computes and records the total execution time for the algorithm.
     */
	public void run() {
		
		long start = System.nanoTime();
        int iSinglePointSolutionIndex = 0;

        // initialise a solution as a random bit string
        sat.createRandomSolution(iSinglePointSolutionIndex);

        // apply the search method until the evaluation limit is reached
		while(!sat.hasEvaluationLimitExpired()) {
			
			// applies the bit flip operator to the solution
			randomBitFlip.applyHeuristic(sat);
			
			// call required for termination criteria but not used in a random walk
			sat.getObjectiveFunctionValue(iSinglePointSolutionIndex);
		}
		
		long end = System.nanoTime();
		this.timeTakenSeconds = (double)(end - start) * 1E-9;
	}

    /**
     * Retrieves the total execution time taken by the RandomWalk search algorithm.
     *
     * The time represents the CPU time consumed during the execution of the
     * random walk search algorithm, measured in seconds.
     *
     * @return The total execution time in seconds.
     */
	public double getTimeTaken() {
		
		return this.timeTakenSeconds;
	}
}