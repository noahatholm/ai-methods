package com.aim.runners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.aim.RunData;
import com.aim.TestFrame;
import com.aim.TestFrameConfig;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;
import uk.ac.nott.cs.aim.statistics.PlotData;
import uk.ac.nott.cs.aim.statistics.XBoxPlot;
import uk.ac.nott.cs.aim.statistics.XLineChart;

/**
 * The Lab2ExercisesRunner class is designed to execute experiments comparing two hill climbing
 * local search heuristics for solving MAX-SAT problems, Davis's Bit Hill Climbing (DBHC), and
 * steepest descent hill climbing (SDHC). It extends the TestFrame class
 * and leverages its functionalities to configure and execute different test scenarios.
 * <p>
 * You will need to change the configuration class Lab2ExercisesTestFrameConfig to run these experiments.
 *
 * @author Warren G. Jackson
 */
public class Lab2ExercisesRunner extends TestFrame {

    /**
     * In these experiments we are pairwise comparing two hill climbing local search heuristics.
     */
	private static final int NUMBER_OF_HEURISTICS_TO_TEST = 2;
	
	public Lab2ExercisesRunner() {

		super(Lab2ExercisesTestFrameConfig.getInstance());
	}

	public void runTests() {

		// execute the experiments - here loRunData contains a list of lists of RunData objects.
        // The outer list represents the heuristic ID, and the inner list represents the trial ID.
		List<List<RunData>> loRunData = runExperiments();

		/*
		 *  generate box plots
		 */
		List<PlotData> loPlotData = new ArrayList<>();
		

        loRunData.stream()
            // get a distinct list of heuristic IDs.
            .map(List::getFirst)
            .mapToInt(RunData::getHeuristicId)
            // for each heuristic under test
            .forEach(id -> {

                // get the objective values of the best solutions found for each trial
                List<Integer> oiResults = loRunData.stream().flatMap(List::stream)
                        .filter(f -> f.getHeuristicId() == id)
                        .map(RunData::getBestSolutionValue)
                        .toList();

                // get the name of the heuristic
                String strHeuristicName = loRunData.stream().flatMap(List::stream)
                        .filter(f -> f.getHeuristicId() == id)
                        .findAny()
                        .orElseThrow()
                        .getHeuristicName();

                // add the data to the list of plot data
                loPlotData.add(new PlotData(oiResults, strHeuristicName));

                // set up plot labels
                String strTitle = "Comparison of the fitness traces of " + strHeuristicName;
                String strXLabel = "Iteration";
                String strYLabel = "Objective value";

                // create a list of progress plots for the current heuristic
                List<PlotData> loProgressPlotData = new ArrayList<>();
                loRunData.stream().flatMap(List::stream)
                        .filter(f->f.getHeuristicId() == id)
                        .forEach( data -> {
                            loProgressPlotData.add(new PlotData(data.getData(), String.format("Trial #%d", data.getTrialId())));
                        });

                // creates and shows the progress plot for the current heuristic
                XLineChart.getPlotCreator().createChart(strTitle, strXLabel, strYLabel, loProgressPlotData);
            });

        // setup name for box plot
        TestFrameConfig oTestConfiguration = getTestConfiguration();
        String strHeuristic1 = Lab2ExercisesTestFrameConfig.getInstance().getSATHeuristic(0, null).getHeuristicName();
        String strHeuristic2 = Lab2ExercisesTestFrameConfig.getInstance().getSATHeuristic(1, null).getHeuristicName();
        String strBoxPlotTitle = String.format("Comparison of %s to %s for MAX-SAT instance %d given a nominal runtime of %d seconds over %d trials.",
                strHeuristic1, strHeuristic2, oTestConfiguration.getInstanceId(), oTestConfiguration.getRunTime(), oTestConfiguration.getTotalRuns());

		// create and show the box plot comparing both heuristics
		XBoxPlot.getPlotCreator().createPlot(strBoxPlotTitle, "Heuristic", "Objective Value", loPlotData);
	}

    /**
     * Determines whether experiments should be conducted in parallel.
     * Subclasses must provide an implementation to specify the execution
     * mode for experiments based on their specific requirements or configurations.
     *
     * @return true if experiments are configured to run in parallel, false otherwise.
     */
    @Override
    public boolean shouldRunExperimentsInParallel() {

        return Lab2ExercisesTestFrameConfig.getInstance().ENABLE_PARALLEL_EXECUTION;
    }

    @Override
    public List<List<RunData>> runExperimentsForHeuristicId(int iHeuristicId) {

		// runs the experiment over 'getTestConfiguration().getTotalRuns()' trials either synchronous or in parallel
        List<RunData> loData = runUsingExperimentalParallelism(rangeAsStream(0, getTestConfiguration().getTotalRuns() - 1))
                // runs the trials in the experiment for the given heuristic ID
				.map(iTrialId -> runExperiment(iTrialId, iHeuristicId))
                // store the results of all the trials in a list
				.toList();

        return List.of(loData);
	}

    @Override
    protected int getNumberOfMethodsToTest() {

        return NUMBER_OF_HEURISTICS_TO_TEST;
    }

    /**
     * Executes a single trial of the experiment using a specified random seed related to the trial ID
     * and heuristic identifier and returns the performance data and solution results.
     * This method applies a local search heuristic to solve a satisfiability problem (SAT) instance
     * until a predefined evaluation limit is reached or exceeded.
     *
     * @param iTrialId the identifier for the current run, used to select the appropriate random seed.
     * @param iHeuristicId the identifier of the heuristic to be applied during the experiment.
     * @return a {@code RunData} object containing the experimental results, including the fitness trace,
     *         the best solution value, heuristic name, and other related information.
     */
    public RunData runExperiment(int iTrialId, int iHeuristicId) {

		long[] alSeeds = getExperimentalSeeds();
		Random oRandom = new Random(alSeeds[iTrialId]);
		
		TestFrameConfig oTestConfiguration = getTestConfiguration();
		SAT oProblem = new SAT(oTestConfiguration.getInstanceId(), oTestConfiguration.getRunTime(), oRandom);
		ArrayList<Integer> liFitnessTrace = new ArrayList<>();

		SATHeuristic oHeuristic = Lab2ExercisesTestFrameConfig.getInstance().getSATHeuristic(iHeuristicId, oRandom);
		
		// record the objective value of the initial solution
		liFitnessTrace.add(oProblem.getObjectiveFunctionValue(SATHeuristic.CURRENT_SOLUTION_INDEX));
		
		// continually apply the local search heuristic until the execution limit expires
		while (!oProblem.hasEvaluationLimitExpired()) {

			// apply DBHC/SDHC to the solution-in-hand
			oHeuristic.applyHeuristic(oProblem);
			
			// evaluate the cost of the solution-in-hand
			int iFitness = oProblem.getObjectiveFunctionValue(SATHeuristic.CURRENT_SOLUTION_INDEX);
			
			// add data to progress plot
			if (!oProblem.hasEvaluationLimitExpired()) {
				liFitnessTrace.add(iFitness);
			}
		}

		logResult(oHeuristic.getHeuristicName(), iTrialId, oProblem.getBestSolutionValue(), oProblem.getBestSolutionAsString());

		return new RunData(liFitnessTrace, oProblem.getBestSolutionValue(), oHeuristic.getHeuristicName(), iHeuristicId, iTrialId,
				oProblem.getBestSolutionAsString());
	}

	public static void main(String[] args) {

		TestFrame runner = new Lab2ExercisesRunner();
		runner.runTests();
	}
}
