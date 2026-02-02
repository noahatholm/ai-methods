package com.aim.pseudorandom;

/**
 * Represents the result of an algorithm execution or experiment.
 *
 * The Result record encapsulates various details regarding the execution or experimental run,
 * including information about the problem domain, the specific instance and trial,
 * the random seed used, and performance metrics such as objective value and execution times.
 *
 * @author Warren G. Jackson
 */
public record Result(
		/**
		 * Name of the problem domain.
		 */
		String domain,
		/**
		 * Instance ID.
		 */
		int instance,
		/**
		 * Trial ID.
		 */
		int iTrialId,
		/**
		 * Experimental seed.
		 */
		long seed, 
		/**
		 * Objective value of the best solution found.
		 */
		int f_best,
		/**
		 * Actual CPU time taken.
		 */
		double cpuTimeTaken, 
		/**
		 * Nominal time taken (with respect to the CHeSC 2011 Competition computer).
		 */
		double nominalTimeTaken) {
}

