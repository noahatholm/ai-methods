package com.aim;

/**
 * Enum representing different types of plots that can be generated for visualizing data.
 * This is typically used to define the scope of data points to be included in a particular plot.
 *
 * <ul>
 *   <li>ALL: Represents all available data points.</li>
 *   <li>BEST: Represents only the best data points based on specific criteria.</li>
 *   <li>WORST: Represents only the worst data points based on specific criteria.</li>
 *   <li>BEST_AND_WORST: Represents both the best and worst data points for comparison.</li>
 * </ul>
 *
 */
public enum PlotType {

	ALL, BEST, WORST, BEST_AND_WORST;
}
