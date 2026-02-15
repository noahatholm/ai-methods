package com.aim.runners;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.aim.RunData;
import com.aim.TestFrame;

import com.aim.TestFrameConfig;
import com.aim.heuristics.DavissBitHillClimbing;
import com.aim.heuristics.SteepestDescentHillClimbing;
import com.aim.metaheuristics.singlepoint.iteratedlocalsearch.IteratedLocalSearch;
import com.aim.pseudorandom.RandomBitFlipHeuristic;
import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;
import uk.ac.nott.cs.aim.searchmethods.SearchMethod;
import uk.ac.nott.cs.aim.statistics.PlotData;
import uk.ac.nott.cs.aim.statistics.XBoxPlot;
import uk.ac.nott.cs.aim.statistics.XLineChart;

/**
 * The Lab3ExercisesRunner class is an extension of the TestFrame class and serves
 * as the main framework for performing experimentation and analysis for SAT
 * (Satisfiability Testing) problems. It includes methods to run tests, generate
 * plots, and perform various heuristic evaluations.
 * This week the experiments focus on assessing the effectiveness of Iterated Local Search (ILS).
 */
public class Lab3ExercisesRunner extends TestFrame {

	private static final int HEURISTIC_TESTS = 1;

	public Lab3ExercisesRunner(Lab3ExercisesTestFrameConfig config) {

		super(config);
	}

	public void runTests() {

		// run the experiments
		List<List<RunData>> loRunData = runExperiments();
        List<RunData> oRunData = loRunData.get(0);

		List<PlotData> oPlotData = new ArrayList<>();

		// get a distinct list of heuristic IDs.
		List<Integer> oHeuristicIds = oRunData.stream().map(RunData::getHeuristicId).distinct()
				.toList();

		// for each heuristic, collate and add the objective values of the best
		// solutions found
		oHeuristicIds.forEach(iMethodId -> {

			List<Integer> liResults = oRunData.stream()
                    .filter(f -> f.getHeuristicId() == iMethodId)
					.map(RunData::getBestSolutionValue)
                    .collect(Collectors.toList());

			String strHeuristicName = oRunData.stream()
                    .filter(f -> f.getHeuristicId() == iMethodId)
                    .findAny()
                    .get()
					.getHeuristicName();

			oPlotData.add(new PlotData(liResults, strHeuristicName));

            // collate data to output
            String strOutput = oRunData.stream()
                    .filter(f->f.getHeuristicId() == iMethodId)
                    .sorted(Comparator.comparingInt(RunData::getTrialId))
                    .map(d -> String.format("%d\t%d", d.getTrialId(), d.getBestSolutionValue()))
                    .collect(Collectors.joining("\n"));

			// print out the results of each trial into the console
			System.out.println("TrialId\tILS");
			System.out.println(strOutput);
		});

        // create the title for the box plot
        TestFrameConfig oTestConfiguration = getTestConfiguration();
        int iIntensityOfMutation = Lab3ExercisesTestFrameConfig.getInstance().getIntensityOfMutation();
        int iDepthOfSearch = Lab3ExercisesTestFrameConfig.getInstance().getDepthOfSearch();
        String strBoxPlotTitle = String.format("Results produced by %s for solving SAT instance %d given %d seconds over %d runs with IOM = %d and DOS = %d",
                oTestConfiguration.getMethodName(), oTestConfiguration.getInstanceId(), oTestConfiguration.getRunTime(), oTestConfiguration.getTotalRuns(), iIntensityOfMutation, iDepthOfSearch);

		// create and show the plot
		XBoxPlot.getPlotCreator().createPlot(strBoxPlotTitle, "Heuristic", "Objective Value", oPlotData);

		/*
		 * generate progress plots
		 */

		// create plots for each heuristic
		for (int iHeuristicId : oHeuristicIds) {

			// get the name of the heuristic with ID 'id'
			String strHeuristicName = oRunData.stream()
                    .filter(f -> f.getHeuristicId() == iHeuristicId)
                    .findAny()
                    .get()
					.getHeuristicName();

			// set up plot labels
			String strTitle = "Comparison of the fitness traces of " + strHeuristicName;
			String strXLabel = "Iteration";
			String strYLabel = "Objective value";

			// create a list of progress plots for the current heuristic
			List<PlotData> oProgressPlotData = new ArrayList<>();
			oRunData.stream().filter(f -> f.getHeuristicId() == iHeuristicId).forEach(data -> oProgressPlotData.add(new PlotData(data.getData(), String.format("Trial #%d", data.getTrialId()))));

			// creates and shows the plot
			XLineChart.getPlotCreator().createChart(strTitle, strXLabel, strYLabel, oProgressPlotData);
		}
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

        return Lab3ExercisesTestFrameConfig.getInstance().ENABLE_PARALLEL_EXECUTION;
    }

    /**
	 * This is where the main algorithm is run.
	 * Ignore the fancy stream stuff, that just makes the framework scalable across multiple CPU cores.
	 * Try to understand the parts of the code between the [START] and [END] tags.
	 *
	 * @param iHeuristicId
	 * @return
	 */
	protected List<List<RunData>> runExperimentsForHeuristicId(int iHeuristicId) {

		long[] alSeeds = getExperimentalSeeds();
        int iIntensityOfMutation = Lab3ExercisesTestFrameConfig.getInstance().getIntensityOfMutation();
        int iDepthOfSearch = Lab3ExercisesTestFrameConfig.getInstance().getDepthOfSearch();
		
		Stream<RunData> oData = runUsingExperimentalParallelism(rangeAsStream(0, getTestConfiguration().getTotalRuns() - 1)).map(iRunId -> {

			// ---- [START] ---- set up and running of the experiments and search method.

			Random oRandom = new Random(alSeeds[iRunId]);
			SAT oProblem = new SAT(getTestConfiguration().getInstanceId(), getTestConfiguration().getRunTime(), oRandom);
			ArrayList<Integer> liFitnessTrace = new ArrayList<>();

			// TODO - set the local search operator to use here
			SATHeuristic oLocalSearch = new SteepestDescentHillClimbing(oRandom);
//			SATHeuristic oLocalSearch = new DavissBitHillClimbing(oRandom); // default

			// set the perturbation operator to use here
			SATHeuristic oMutation = new RandomBitFlipHeuristic(oRandom);

			SearchMethod oSearchMethod = new IteratedLocalSearch(oProblem, oRandom, oMutation, oLocalSearch, iIntensityOfMutation, iDepthOfSearch);

			liFitnessTrace.add(oProblem.getObjectiveFunctionValue(SATHeuristic.CURRENT_SOLUTION_INDEX));
			while (!oProblem.hasEvaluationLimitExpired()) {

				oSearchMethod.run();
				int iFitness = oProblem.getObjectiveFunctionValue(SATHeuristic.CURRENT_SOLUTION_INDEX);
				liFitnessTrace.add(iFitness);
			}

			// ---- [END] ----

			// additional logging for reporting of statistics and plots
			logResult(oSearchMethod.toString(), iRunId, oProblem.getBestSolutionValue(), oProblem.getBestSolutionAsString());
			return new RunData(liFitnessTrace, oProblem.getBestSolutionValue(), oSearchMethod.toString(), iHeuristicId, iRunId,
					oProblem.getBestSolutionAsString());
		});

		return List.of(oData.collect(Collectors.toList()));
	}

    @Override
    protected int getNumberOfMethodsToTest() {

        return HEURISTIC_TESTS;
    }

    public static void main(String[] args) {

		Lab3ExercisesTestFrameConfig oConfiguration = Lab3ExercisesTestFrameConfig.getInstance();
		TestFrame oTestFrame = new Lab3ExercisesRunner(oConfiguration);
		oTestFrame.runTests();
	}
}
