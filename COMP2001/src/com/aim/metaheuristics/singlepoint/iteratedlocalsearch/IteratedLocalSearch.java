package com.aim.metaheuristics.singlepoint.iteratedlocalsearch;

import java.util.Random;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.satheuristics.SATHeuristic;
import uk.ac.nott.cs.aim.searchmethods.SinglePointSearchMethod;

	
public class IteratedLocalSearch extends SinglePointSearchMethod {

	// local search / intensification heuristic
	private final SATHeuristic oLocalSearchHeuristic;
	
	// mutation / perturbation heuristic
	private final SATHeuristic oMutationHeuristic;
	
	// iom parameter setting
	private final int iIntensityOfMutation;
	
	// dos parameter setting
	private final int iDepthOfSearch;
	
	/**
	 * 
	 * @param oProblem The problem to be solved.
	 * @param oRandom The random number generator, use this one, not your own!
	 * @param oMutationHeuristic The mutation heuristic.
	 * @param oLocalSearchHeuristic The local search heuristic.
	 * @param iIntensityOfMutation The parameter setting for intensity of mutation.
	 * @param iDepthOfSearch The parameter setting for depth of search.
	 */
	public IteratedLocalSearch(SAT oProblem, Random oRandom, SATHeuristic oMutationHeuristic, 
			SATHeuristic oLocalSearchHeuristic, int iIntensityOfMutation, int iDepthOfSearch) {
		
		super(oProblem, oRandom);
		
		this.oMutationHeuristic = oMutationHeuristic;
		this.oLocalSearchHeuristic = oLocalSearchHeuristic;
		this.iIntensityOfMutation = iIntensityOfMutation;
		this.iDepthOfSearch = iDepthOfSearch;
	}

	/**
	 * 
	 * Main loop for ILS. The experiment framework will continually call this loop until
	 * the allocated time has expired.
	 * 
	 * -- ITERATED LOCAL SEARCH PSEUDO CODE --
	 * 
	 * // syntactic step for the below pseudocode. The solutions in both CURRENT and
	 * // BACKUP memory indices are invariantly the same at the start of each loop.
	 * s' <- s
	 * 
	 * // apply mutation heuristic "iIntensityOfMutation" times
	 * REPEAT intensityOfMutation TIMES:
	 *     s' <- mutation(s')
	 * 
	 * // apply local search heuristic "iDepthOfSearch" times
	 * REPEAT depthOfSearch TIMES:
	 *     s' <- localSearch(s')
	 * 
	 * // HINT: Remember that the solutions in the CURRENT and BACKUP memory indices should
	 * //       be the SAME after each application of the "runMainLoop()"!
	 *
	 * IF f(s') <= f(s) THEN
	 *     accept();
	 * ELIF
	 *     reject();
	 * FI
	 */
	protected void runMainLoop() {

        // TODO - implement iterated local search embedding the intensity of mutation and depth of search parameters.
        int best = m_oProblem.getObjectiveFunctionValue(CURRENT_SOLUTION_INDEX);
        //System.out.println(best);
        for (int iom = 0 ; iom < iIntensityOfMutation; iom++) {
            oMutationHeuristic.applyHeuristic(m_oProblem);
        }


        for (int dos = 0; dos < iDepthOfSearch; dos++){
            oLocalSearchHeuristic.applyHeuristic(m_oProblem);

        }
        if (m_oProblem.getObjectiveFunctionValue(CURRENT_SOLUTION_INDEX) <= best) {
            m_oProblem.copySolution(CURRENT_SOLUTION_INDEX,BACKUP_SOLUTION_INDEX);
        }
        else{
            m_oProblem.copySolution(BACKUP_SOLUTION_INDEX,CURRENT_SOLUTION_INDEX);
        }

	}
	
	public String toString() {
		return "Iterated Local Search";
	}
}
