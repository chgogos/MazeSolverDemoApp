package solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;

import model.Maze;

public class BreadthFirstSearchSolver {
	Maze maze;

	public BreadthFirstSearchSolver(Maze maze) {
		this.maze = maze;
	}

	public ArrayList<ImmutablePair<Integer, Integer>> solve() {
		Set<ImmutablePair<Integer, Integer>> closed = new HashSet<>();
		Queue<ArrayList<ImmutablePair<Integer, Integer>>> frontier = new LinkedList<>();
		ArrayList<ImmutablePair<Integer, Integer>> path = new ArrayList<>();
		path.add(maze.getStart());
		frontier.add(path);
		ArrayList<ImmutablePair<Integer, Integer>> current = frontier.poll();
		ImmutablePair<Integer, Integer> currentCell = current.get(current.size() - 1);
		boolean found = true;
		while (!currentCell.equals(maze.getFinish())) {
			if (!closed.contains(currentCell)) {
				closed.add(currentCell);
				for (ImmutablePair<Integer, Integer> cell : maze.getSuccessors(currentCell)) {
					ArrayList<ImmutablePair<Integer, Integer>> newPath = new ArrayList<>(current);
					newPath.add(cell);
					frontier.add(newPath);
				}
			}
			if (frontier.isEmpty()) {
				found = false;
				break;
			}
			current = frontier.poll();
			currentCell = current.get(current.size() - 1);
		}
		if (found)
			System.out.printf("Steps=%d, Path=%s\n", current.size(), current);
		else
			System.out.println("Path to solution not found");
		return current;
	}
}
