package com.aim.runners;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import com.aim.RunData;
import com.aim.TestFrame;
import com.aim.TestFrameConfig;

import com.aim.pseudorandom.RandomWalk;
import com.aim.pseudorandom.Result;
import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;

/**
 * The Lab1ExercisesRunner class extends the TestFrame superclass and provides
 * the functionality to configure, run, and manage experimental test methods
 * for the first computing session. This includes initialising the random walk
 * search algorithm for solving SAT problems, managing experimental settings,
 * and outputting results.
 *
 * @author Warren G. Jackson
 */
public class Lab1ExercisesRunner extends TestFrame {

    /**
     * Constructs an instance of the Lab1ExercisesRunner class by invoking the
     * constructor of its superclass with a predefined configuration.
     *
     * The configuration is retrieved from the singleton instance of
     * Lab1ExercisesTestFrameConfig, which specifies the experimental settings,
     * including the SAT problem instance ID, run time, number of trials, and random
     * seeds. You will need to change this configuration class for some of the exercises.
     */
	public Lab1ExercisesRunner() {

		super(Lab1ExercisesTestFrameConfig.getConfiguration());
	}

    /**
     * Executes a random walk search algorithm for solving a specific SAT problem instance.
     *
     * This method initialises the SAT problem with the given parameters, runs the RandomWalk
     * algorithm, and returns the result encapsulating the details of the execution and solution.
     *
     * @param iTrialId The identifier for the specific trial of the experiment.
     * @param lSeed The random seed used for reproducibility of the algorithm's execution.
     * @param iInstance The identifier for the specific SAT problem instance to be solved.
     * @param iTimeLimit The maximum allowed time for the algorithm execution in nominal units.
     * @return A Result object containing the details of the execution, such as the problem
     *         domain name, trial and instance identifiers, seed, objective value of the best solution,
     *         CPU time taken, and nominal time taken.
     */
	public Result runTest(int iTrialId, long lSeed, int iInstance, int iTimeLimit) {

		Random oRandom = new Random(lSeed);
		SAT oProblemInstance = new SAT(iInstance, iTimeLimit, oRandom);
		RandomWalk oRandomWalkSearchAlgorithm = new RandomWalk(oProblemInstance, oRandom);
		oRandomWalkSearchAlgorithm.run();
		
		return new Result(oProblemInstance.toString(), iInstance, iTrialId, lSeed, oProblemInstance.getBestSolutionValue(), oRandomWalkSearchAlgorithm.getTimeTaken(), iTimeLimit);
	}

    /**
     * Prints the results of an experiment or algorithm execution formatted as a single line of output.
     *
     * @param oResult The result object containing the details of the experiment or algorithm execution,
     *                including seed, the objective value of the best solution found, CPU time taken, and nominal time taken.
     */
	private void printResult(Result oResult) {

		System.out.printf("%d,%d,%.3f,%.3f%n", oResult.seed(), oResult.f_best(), oResult.cpuTimeTaken(), oResult.nominalTimeTaken());
	}

    @Override
    public boolean shouldRunExperimentsInParallel() {

        return Lab1ExercisesTestFrameConfig.getConfiguration().ENABLE_PARALLEL_EXECUTION;
    }

    @Override
    protected List<List<RunData>> runExperimentsForHeuristicId(int iHeuristicId) {

        TestFrameConfig oTestConfiguration = getTestConfiguration();

        final int iInstanceId = oTestConfiguration.getInstanceId();
        final int iTimeLimit = oTestConfiguration.getRunTime();
        final int iTotalRuns = oTestConfiguration.getTotalRuns();
        final long[] alSeeds = getExperimentalSeeds();

        System.out.println(getTestConfiguration().getConfigurationAsString());
        System.out.println("seed,f_best,time_taken(CPU seconds),time_taken(nominal seconds)");
        runUsingExperimentalParallelism(IntStream.range(0, iTotalRuns).boxed())
                .map(i -> runTest(i, alSeeds[i], iInstanceId, iTimeLimit)).forEachOrdered(this::printResult);

        // not used in this lab.
        return List.of();
    }

    @Override
    protected int getNumberOfMethodsToTest() {

        // this lab only has one search method to test - random walk.
        return 1;
    }

    @Override
	public void runTests() {

        // run the experiments
        List<List<RunData>> oRunData = runExperiments();

        // this section is not required for this lab.
        // Future labs will use the run data to generate plots.
	}

    /**
     * The main method serves as an entry point for the program and initialises
     * the Lab1ExercisesRunner to execute its experimental test methods.
     *
     * @param args not used in this lab.
     */
	public static void main(String[] args) {

		Lab1ExercisesRunner runner = new Lab1ExercisesRunner();
		runner.runTests();
	}
}
