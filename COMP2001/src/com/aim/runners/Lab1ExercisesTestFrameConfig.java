package com.aim.runners;

import com.aim.TestFrameConfig;

/**
 * Represents the specific configuration for Lab1 exercises, extending the general
 * framework provided by the TestFrameConfig abstract class. This class defines
 * particular test parameters, including instance ID, runtime, number of trials per
 * test, the heuristic method name, and parallel execution settings.
 * <p>
 * This configuration uses predefined experimental seeds to ensure reproducibility of
 * results and provides single-instance access via a Singleton pattern.
 * <p>
 * You will need to change the long values within m_seeds for some of the exercises
 * which can be found in the exercise sheet.
 *
 * @author Warren G Jackson
 */
public class Lab1ExercisesTestFrameConfig extends TestFrameConfig {

	/**
	 * The experimental seed, initially all are set as the date of the in-person lab.
	 */
	private static final long[] m_seeds = { 30012026L, 30012026L, 30012026L };
    //private static final long[] m_seeds = { 67, 100, 100000 };


    /*
	 * permitted instance ID's = 1
	 */
	private final int INSTANCE_ID = 1;

	/*
	 * permitted run times (nominal seconds) = 10
	 */
	private final int RUN_TIME = 10;

	/**
	 * Sets the number of trials equal to the number of experimental seeds.
	 */
	private final int TRIALS_PER_TEST = m_seeds.length;

    /**
     * Allows experiments that are based (internally) on a number of changes to a
     * solution-in-hand to be executed in parallel on your machine.
     * <p>
     * WARNING: Enabling this in future lab exercises will cause your computer to
     * run slowly for other tasks while the experiments are being run.
     */
    protected final boolean ENABLE_PARALLEL_EXECUTION = false;

	/**
	 * Singleton instance.
	 */
	private static Lab1ExercisesTestFrameConfig oThis;

    /**
     * Private constructor for the Lab1ExercisesTestFrameConfig class.
     * <p>
     * This constructor initialises the Lab1ExercisesTestFrameConfig instance
     * by calling the superclass constructor with predefined seed values.
     * Its private visibility enforces the Singleton design pattern, ensuring
     * that only one instance of this class can be created.
     */
	private Lab1ExercisesTestFrameConfig() {

		super(m_seeds);
	}

    /**
     * Returns the singleton instance of the Lab1ExercisesTestFrameConfig class.
     * This method ensures the class follows the Singleton design pattern by
     * creating an instance only when it is accessed for the first time.
     * Subsequent calls return the same instance.
     *
     * @return The singleton instance of Lab1ExercisesTestFrameConfig.
     */
	public static synchronized Lab1ExercisesTestFrameConfig getConfiguration() {

		if (oThis == null) {
			oThis = new Lab1ExercisesTestFrameConfig();
		}

		return oThis;
	}

	@Override
	public int getInstanceId() {
		return INSTANCE_ID;
	}

	@Override
	public int getRunTime() {
		return RUN_TIME;
	}

	@Override
	public String getMethodName() {
		return "Random Walk";
	}

	@Override
	public String getConfigurationAsString() {
		
		return String.format("SAT instance #%d with a run time of %d nominal seconds.", INSTANCE_ID, RUN_TIME);
	}

	@Override
	public int getTotalRuns() {
		return TRIALS_PER_TEST;
	}

}
