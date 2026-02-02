package com.aim;

/**
 * Represents the abstract base class for test or experimental configurations.
 * This class serves as a blueprint for child classes that define specific test configurations,
 * including parameters such as instance ID, runtime, method name, and configuration details.
 * Additionally, it provides a mechanism to store and retrieve seeds for randomisation purposes.
 * <p>
 * The class enforces implementation of key methods for customising configurations in derived classes.
 *
 * @author Warren G Jackson
 */
public abstract class TestFrameConfig {

    /**
     * Stores an array of seed values used to initialise random number generators or
     * to control the deterministic behaviour of randomised processes.
     * This array ensures reproducibility of experiments by allowing
     * randomisation to be consistent across multiple runs.
     */
	private final long[] m_alSeeds;

    /**
     * Constructs a new instance of the TestFrameConfig class with the specified seed values.
     * The seed values are used to initialise random number generators or control
     * deterministic behaviours in randomised processes, ensuring reproducibility across experiments.
     *
     * @param alSeeds an array of long values representing the seed values for randomisation.
     */
	public TestFrameConfig(long[] alSeeds) {

        this.m_alSeeds = alSeeds;
	}

    /**
     * Retrieves the instance ID of the problem to solve for the current test configuration.
     *
     * @return The instance ID for the current test configuration.
     */
	public abstract int getInstanceId();

    /**
     * Retrieves the runtime limit in nominal-seconds for the current test configuration.
     *
     * @return The runtime limit in nominal-seconds for the current test configuration
     */
	public abstract int getRunTime();

    /**
     * Retrieves the name of the heuristic method to be tested for the current test configuration.
     *
     * @return The name of the heuristic method to be tested for the current test configuration.
     */
	public abstract String getMethodName();

    /**
     * Retrieves a string representation of the current test configuration.
     *
     * @return The configuration details for the current test configuration as a string.
     */
	public abstract String getConfigurationAsString();

    /**
     * Retrieves the total number of runs to be executed for the current test configuration.
     *
     * @return The total number of runs to be executed for the current test configuration.
     */
	public abstract int getTotalRuns();

    /**
     * Retrieves the array of seed values used for randomisation.
     * These seeds help ensure reproducibility of experiments by
     * allowing consistent randomisation across multiple runs.
     *
     * @return An array of long values representing the seeds used for randomisation.
     */
	public long[] getSeeds() {
		
		return m_alSeeds;
	}
}
