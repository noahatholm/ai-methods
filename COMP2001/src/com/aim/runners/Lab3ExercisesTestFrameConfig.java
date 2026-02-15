package com.aim.runners;

import com.aim.TestFrameConfig;

/**
 * Test frame/experimental configuration for lab 3.
 * 
 * @author Warren G. Jackson
 *
 */
public class Lab3ExercisesTestFrameConfig extends TestFrameConfig {

	/**
	 * The experimental seed, set as the date this lab was released.
     * Need to check what seeds to use to allow statistical comparisons to be made against DBHC alone (see formative assessment exercises).
	 */
	private static final long m_parentSeed = "metaheuristics 1".hashCode();

	/**
	 * permitted total runs = 31
	 */
	protected final int TOTAL_RUNS  = 31;
	
	/**
	 * permitted instance ID's = 1
	 */
	protected final int INSTANCE_ID = 1;
	
	/**
	 * permitted run times (seconds) = 10
	 */
	protected final int RUN_TIME = 10;
	
	/**
	 * permitted values = 0, 1, 2, 3 (default = 1)
	 */
	protected final int depthOfSearch = 0;
	
	/**
	 * permitted values = 0, 1, 2, 3 (default = 0)
	 */
	protected final int intensityOfMutation = 1;

    /**
     * Allows experiments that are based (internally) on a number of changes to a
     * solution-in-hand to be executed in parallel on your machine.
     *
     * WARNING: This WILL cause your computer to run slow for other tasks while
     * the experiments are run
     */
    protected final boolean ENABLE_PARALLEL_EXECUTION = true;
	
	/**
	 * 
	 */
	private static Lab3ExercisesTestFrameConfig oThis;

	/**
	 * 
	 */
	private Lab3ExercisesTestFrameConfig() {

		super(new long[] {m_parentSeed});
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static Lab3ExercisesTestFrameConfig getInstance() {

		if (oThis == null) {
			oThis = new Lab3ExercisesTestFrameConfig();
		}

		return oThis;
	}
	
	@Override
	public int getTotalRuns() {
		return this.TOTAL_RUNS;
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
		return "Iterated Local Search";
	}

	@Override
	public String getConfigurationAsString() {
		return "intensityOfMutation = " + intensityOfMutation + " and depthOfSearch = " + depthOfSearch;
	}
	
	public int getDepthOfSearch() {
		return this.depthOfSearch;
	}
	
	public int getIntensityOfMutation() {
		return this.intensityOfMutation;
	}

}
