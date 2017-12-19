
package solver;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class PathCostPair implements Comparable<PathCostPair> {
	List<ImmutablePair<Integer, Integer>> path;
	Double cost;

	public PathCostPair(List<ImmutablePair<Integer, Integer>> path, Double cost) {
		this.path = path;
		this.cost = cost;
	}

	public List<ImmutablePair<Integer, Integer>> getPath() {
		return path;
	}

	public Double getCost() {
		return cost;
	}

	@Override
	public int compareTo(PathCostPair o) {
		return cost.compareTo(o.cost);
	}

	@Override
	public String toString() {
		return String.format("%s %.2f", path, cost);
	}
}
