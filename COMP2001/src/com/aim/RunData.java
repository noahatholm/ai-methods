package com.aim;

import java.util.ArrayList;

/**
 * Represents a data structure for storing the results of a computational experiment or heuristic run.
 * This class holds information about the performance of a heuristic, including the heuristic's name,
 * identifier, trial information, and solution details, as well as associated data and the best solution value.
 *
 * @author Warren G Jackson
 */
public class RunData {
		
	private final ArrayList<Integer> m_data;
	
	private final Integer m_best;
	
	private final String m_heuristicName;
	
	private final int m_heuristicId;
	
	private final int m_trialId;
	
	private final String m_solution;
	
	public RunData(ArrayList<Integer> data, Integer best, String heuristicName, int heuristicId, int trialId, String solution) {
		
		this.m_data = data;
		this.m_best = best;
		this.m_heuristicName = heuristicName;
		this.m_heuristicId = heuristicId;
		this.m_trialId = trialId;
		this.m_solution = solution;
	}
	
	public ArrayList<Integer> getData() {
		return m_data;
	}
	
	public Integer getBestSolutionValue() {
		return m_best;
	}
	
	public String getHeuristicName() {
		return m_heuristicName;
	}
	
	public int getHeuristicId() {
		return m_heuristicId;
	}
	
	public int getTrialId() {
		return m_trialId;
	}
	
	public String getBestSolutionSolutionAsString() {
		return m_solution;
	}
}
