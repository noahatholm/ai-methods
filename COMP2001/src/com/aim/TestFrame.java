package com.aim;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * An abstract base class designed to provide a framework for conducting and analysing
 * experimental runs with various heuristic methods. The {@code TestFrame} class centralises
 * configuration management, randomisation, logging, and result recording functionalities
 * required for scientific testing and benchmarking of algorithms and procedures.
 *
 * @author Warren G Jackson
 */
public abstract class TestFrame {

    /**
     * Stores an array of random seed values used to initialise experiments or tests
     * conducted by the {@code TestFrame}. Each value in this array ensures the
     * reproducibility of independent trials or runs across different heuristic
     * methods or configurations.
     * <p>
     * The seeds can be specified explicitly in the configuration or generated
     * dynamically if fewer seeds are provided than the number of runs in the
     * experiment. These seed values are integral to maintaining consistency in
     * randomised experimental procedures.
     */
	private final long[] SEEDS;

    /**
     * Represents the platform-specific line separator string.
     * It provides a consistent way to handle new lines across different operating systems.
     * The value is retrieved from {@link System#lineSeparator()}.
     */
	private final String NEW_LINE = System.lineSeparator();

    /**
     * Represents the configuration settings for a test frame, encapsulating
     * details such as random seeds, total number of runs, and other experimental
     * parameters. This configuration is used to define the properties and behaviour
     * of the test frame during its execution.
     * <p>
     * This variable is immutable and is set during the construction of the
     * {@code TestFrame} instance. It provides access to the experimental
     * settings required for running tests or experiments.
     */
	private final TestFrameConfig m_oTestConfiguration;

    /**
     * Represents the total number of experimental runs to be executed within the test frame.
     * This value is configured in the test frame's experimental settings and is used to
     * determine the number of trials or experiments conducted.
     */
	private final int m_totalRuns;

    /**
     * Constructs a {@code TestFrame} instance using the provided configuration.
     * The configuration specifies the experimental settings, including the total number of runs
     * and the random seeds to be used for each experimental run. If the provided seeds are fewer
     * than the total number of runs, additional seeds are generated using a seeded random number generator.
     *
     * @param config The {@code TestFrameConfig} object containing the experimental configuration.
     *               This includes the number of runs, random seeds, and other specific settings for the test frame.
     */
	public TestFrame(TestFrameConfig config) {
		
		this.m_oTestConfiguration = config;
		this.m_totalRuns = config.getTotalRuns();
		
		// use a seeded random number generator to generate "TOTAL_RUNS" seeds
		SEEDS = new long[getTotalRuns()];
		System.arraycopy(config.getSeeds(), 0, SEEDS, 0, Math.min(SEEDS.length, config.getSeeds().length));
		if(config.getSeeds().length < SEEDS.length) {
			
			Random random = new Random(config.getSeeds()[0]);
			for(int i = config.getSeeds().length; i < SEEDS.length; i++) {
				SEEDS[i] = random.nextLong();
			}
		}
	}

    /**
     * Retrieves the total number of runs configured in the test frame.
     *
     * @return The total number of experimental runs to be executed.
     */
	private int getTotalRuns() {
		
		return m_totalRuns;
	}

    /**
     * Determines whether the experiments should be executed in parallel or sequentially,
     * based on the experimental parallelism setting in {@code ExperimentalSettings}.
     *
     * @param stream The input stream of Integer elements to be executed.
     * @return A parallel or sequential stream of Integer elements, depending on the
     *         {@code ENABLE_PARALLEL_EXECUTION} flag in {@code ExperimentalSettings}.
     */
	public Stream<Integer> runUsingExperimentalParallelism(Stream<Integer> stream) {
		
		return shouldRunExperimentsInParallel() ? stream.parallel() : stream.sequential();
	}

    /**
     * Determines whether experiments should be conducted in parallel.
     * Subclasses must provide an implementation to specify the execution
     * mode for experiments based on their specific requirements or configurations.
     *
     * @return true if experiments are configured to run in parallel, false otherwise.
     */
    public abstract boolean shouldRunExperimentsInParallel();

    /**
     * Saves the provided data to a file. If the file does not exist, it creates a new file
     * and writes the provided header and data. If the file already exists, it appends
     * the data to the file.
     *
     * @param filePath The relative path of the file where the data will be saved.
     * @param header The header content to add to the file if a new file is created.
     * @param data The data to be saved or appended to the file.
     */
	protected void saveData(String filePath, String header, String data) {
		
		Path path = Paths.get("./" + filePath);
		if(!Files.exists(path)) {
			try {
				Files.createFile(path);
				
				//add header
                StringBuilder headerBuilder = new StringBuilder(header);
                for(int i = 0; i < getTotalRuns(); i++) {
					
					headerBuilder.append(",").append(i);
				}

                headerBuilder.append(",Best Solution As String");
                header = headerBuilder.toString();

				Files.write(path, (header + "\r\n" + data).getBytes());
				
			} catch (IOException e) {
				System.err.println("Could not create file at " + path.toAbsolutePath());
				System.err.println("Printing data to screen instead...");
				System.out.println(data);
			}
			
		} else {
			
			try {
				byte[] currentData = Files.readAllBytes(path);
				data = "\r\n" + data;
				byte[] newData = data.getBytes();
				byte[] writeData = new byte[currentData.length + newData.length];
				System.arraycopy(currentData, 0, writeData, 0, currentData.length);
				System.arraycopy(newData, 0, writeData, currentData.length, newData.length);
				Files.write(path, writeData);
				
			} catch (IOException e) {
				System.err.println("Could not create file at " + path.toAbsolutePath());
				System.err.println("Printing data to screen instead...");
				System.out.println(data);
			}
		}
	}

    /**
     * Generates a Stream of Integer values within the specified range, inclusive
     * of both the start and end values.
     *
     * @param start The lower bound of the range (inclusive).
     * @param end The upper bound of the range (inclusive).
     * @return A Stream of Integer values representing the range from start to end, inclusive.
     */
	public Stream<Integer> rangeAsStream(int start, int end) {
		
		return IntStream.rangeClosed(start, end).boxed();
	}

    /**
     * Logs the result of a heuristic method execution, including method name, run ID,
     * the best solution value, and the corresponding solution as a string.
     *
     * @param methodName The name of the AI heuristic search method being logged.
     * @param runId The ID of the specific run or trial of the heuristic method.
     * @param bestSolutionValue The best solution value obtained during the execution.
     * @param solution The string representation of the best solution found in the run.
     */
	public void logResult(String methodName, int runId, double bestSolutionValue, String solution) {
		
		System.out.println("Heuristic: " + methodName + NEW_LINE +
				"Run ID: " + runId + NEW_LINE +
				"Best Solution Value: " + bestSolutionValue + NEW_LINE +
				"Best Solution: " + solution + NEW_LINE);
	}

    /**
     * Retrieves the test configuration associated with the current test frame.
     *
     * @return The {@code TestFrameConfig} object containing configuration details
     *         for the test frame.
     */
	public TestFrameConfig getTestConfiguration() {
		
		return m_oTestConfiguration;
	}

    /**
     * Retrieves the array of experimental random seeds used for conducting tests or experiments.
     *
     * @return An array of long values representing the experimental seeds.
     */
	public long[] getExperimentalSeeds() {
		
		return SEEDS;
	}

    /**
     * Executes multiple individual experiments across a range of heuristic methods. Each heuristic method
     * is tested for multiple trials or configurations, and the results are collected into a nested list
     * structure. The outer list contains results for different heuristic methods, while each inner list
     * holds {@code RunData} objects corresponding to individual trials of a specific heuristic.
     * <br/>
     * The execution uses experimental parallelism settings to determine whether the experiments
     * are run in parallel or sequentially. This is determined by the {@code ENABLE_PARALLEL_EXECUTION}
     * flag in {@code ExperimentalSettings} which can be modified to suit your computational environment.
     *
     * @return A list of lists where each inner list contains {@code RunData} representing the results
     *         of experiments conducted for a specific heuristic method across multiple trials.
     */
    public List<List<RunData>> runExperiments() {

        return runUsingExperimentalParallelism(rangeAsStream(0, getNumberOfMethodsToTest() - 1))
                .map(this::runExperimentsForHeuristicId)
                .flatMap(Collection::stream)
                .toList();
    }

    /**
     * Executes experiments for a specific heuristic ID and organises the results into a nested list structure.
     * Each inner list contains {@code RunData} objects representing the results of individual runs or trials
     * conducted for the given heuristic ID.
     *
     * @param iHeuristicId The unique identifier of the heuristic method for which experiments are to be run.
     * @return A list of lists where each inner list contains {@code RunData} representing experimental results
     *         for different trials or configurations of the specified heuristic ID.
     */
    protected abstract List<List<RunData>> runExperimentsForHeuristicId(int iHeuristicId);

    /**
     * Returns the total number of heuristic methods to be tested in experiments.
     * This value determines the range of heuristic method IDs to iterate over
     * when conducting experimental runs or generating results.
     * Subclasses must define the specific number of methods applicable to their context.
     *
     * @return The number of heuristic methods to be tested.
     */
    protected abstract int getNumberOfMethodsToTest();

    /**
     * Executes a set of predefined test cases or experiments based on the test configuration and
     * experimental settings. This method is intended to manage the execution of all required tests
     * in the context of the provided experimental frameworks. It is expected to leverage configured
     * resources, including instance details, runtime limits, random seeds, and test frames for generating
     * experiment data.
     * <p>
     * Concrete subclasses must provide an implementation for this method, defining the specific
     * behaviour required to execute their respective tests. The detailed implementation should specify
     * how tests are run, how results are collected, and what outputs are generated.
     * <p>
     * Designed to be used as an entry point for initiating all test cases as per the experimental
     * configuration, this method orchestrates the invocation of individual heuristic methods or
     * experiment runs.
     */
    public abstract void runTests();
	
}
