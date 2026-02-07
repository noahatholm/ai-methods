package com.aim.runners;

import java.util.Random;

import com.aim.TestFrameConfig;

import com.aim.heuristics.*;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;

/**
 * Test frame/experimental configuration for lab 2.
 * <p>
 * You will need to change the following values to complete your experiments outlined in the formative assessment (Moodle QUIZ):
 * - INSTANCE_ID: tells the test frame which instance of the MAX-SAT problem to load; these may vary in the number of variables and clauses.
 * - RUN_TIME: tells the test frame how long to run each experiment for in "nominal seconds".
 * - TRIALS_PER_TEST: tells the test frame how many trials should be performed in the experiment.
 *
 * @author Warren G. Jackson
 */
public class Lab2ExercisesTestFrameConfig extends TestFrameConfig {

	/**
	 * The experimental seed, set as the hash code for "hill climbing" - this week's topic.
     *
     * Note that from lab 2 onwards, the seeds used in each trial will be derived from the parent
     * seed using another pseudo-random number generator that is seeded with the parent seed.
	 */
    private static final long m_parentSeed = "hill climbing".hashCode();
	//private static final long m_parentSeed = 0;

	/*
	 * permitted instance ID's in the range [0, 11] all inclusive.
	 */
	private final int INSTANCE_ID = 5;

	/*
	 * permitted run times (seconds) in the range [1,20] all inclusive.
	 */
    private final int RUN_TIME = 5;

    /**
     * Specifies the number of trials to be executed per heuristic in the experimental
     * framework. This value is used to determine how many repetitions of a single
     * test configuration will be performed during execution.
     */
    private final int TRIALS_PER_TEST = 11;

    /**
     * Allows experiments that are based (internally) on a number of changes to a
     * solution-in-hand to be executed in parallel on your machine.
     * <p>
     * WARNING: This WILL cause your computer to run slowly for other tasks while
     * the experiment is being run.
     */
    protected final boolean ENABLE_PARALLEL_EXECUTION = false;

    /**
	 * 
	 */
	private static Lab2ExercisesTestFrameConfig oThis;

	/**
	 * 
	 */
	private Lab2ExercisesTestFrameConfig() {

		super(new long[] { m_parentSeed });
	}

	public synchronized static Lab2ExercisesTestFrameConfig getInstance() {

		if (oThis == null) {
			oThis = new Lab2ExercisesTestFrameConfig();
		}

		return oThis;
	}

	@Override
	public int getInstanceId() {
		return this.INSTANCE_ID;
	}

	@Override
	public int getRunTime() {
		return this.RUN_TIME;
	}

	@Override
	public String getMethodName() {
		return "Davis's Bit Hill Climbing and Steepest Descent";
	}

	@Override
	public String getConfigurationAsString() {

		return String.format("SAT instance #%d with a run time of %d nominal seconds.", INSTANCE_ID, RUN_TIME);
	}

	@Override
	public int getTotalRuns() {

        return TRIALS_PER_TEST;
	}

	/**
	 * This method should not be changed but is intended for personal use if you
	 * wish to try with other heuristics of your own making.
	 * 
	 * @param iHeuristicID 0 for the first heuristic, or 1 for the second.
	 * @param oRandom      The random number generator used by all SATHeuristic's
	 * @return The corresponding SAT heuristic
	 */
	public SATHeuristic getSATHeuristic(int iHeuristicID, Random oRandom) {

		final SATHeuristic oHeuristic;

		switch (iHeuristicID) {
		case 0:
			oHeuristic = new DavissBitHillClimbing(oRandom);
			break;
        case 1:
            oHeuristic = new SteepestDescentHillClimbing(oRandom);
            break;
		default:
            oHeuristic = null;
			System.err.println("Request for more than 2 heuristics at a time!");
			System.exit(0);
		}

		return oHeuristic;
	}
}
