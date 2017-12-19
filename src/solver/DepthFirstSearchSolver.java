package solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.tuple.ImmutablePair;

import model.Maze;

public class DepthFirstSearchSolver {
	Maze maze;

	public DepthFirstSearchSolver(Maze maze) {
		this.maze = maze;
	}

	public ArrayList<ImmutablePair<Integer, Integer>> solve() {
		Set<ImmutablePair<Integer, Integer>> closed = new HashSet<>();
		Stack<ArrayList<ImmutablePair<Integer, Integer>>> frontier = new Stack<>();
		ArrayList<ImmutablePair<Integer, Integer>> path = new ArrayList<>();
		path.add(maze.getStart());
		frontier.push(path);
		ArrayList<ImmutablePair<Integer, Integer>> current = frontier.pop();
		ImmutablePair<Integer, Integer> currentCell = current.get(current.size() - 1);
		boolean found = true;
		while (!currentCell.equals(maze.getFinish())) {
			if (!closed.contains(currentCell)) {
				closed.add(currentCell);
				for (ImmutablePair<Integer, Integer> cell : maze.getSuccessors(currentCell)) {
					ArrayList<ImmutablePair<Integer, Integer>> newPath = new ArrayList<>(current);
					newPath.add(cell);
					frontier.push(newPath);
				}
			}
			if (frontier.isEmpty()) {
				found = false;
				break;
			}
			current = frontier.pop();
			currentCell = current.get(current.size() - 1);
		}
		if (found)
			System.out.printf("Steps=%d, Path=%s\n", current.size(), current);
		else
			System.out.println("Path to solution not found");
		return current;
	}
}
