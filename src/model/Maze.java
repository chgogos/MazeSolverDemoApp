package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.essentials4j.New;

import solver.PathCostPair;

public class Maze {
	int rows;
	int cols;
	int data[][];
	ImmutablePair<Integer, Integer> start;
	ImmutablePair<Integer, Integer> finish;

	public Maze(int rows, int cols, int[][] data, ImmutablePair<Integer, Integer> start,
			ImmutablePair<Integer, Integer> finish) {
		this.rows = rows;
		this.cols = cols;
		this.data = data;
		this.start = start;
		this.finish = finish;
	}

	public Maze(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.data = new int[rows][cols];
		this.start = new ImmutablePair<Integer, Integer>(rows - 1, cols - 1);
		this.finish = new ImmutablePair<Integer, Integer>(0, 0);
	}

	public ImmutablePair<Integer, Integer> getStart() {
		return start;
	}

	public ImmutablePair<Integer, Integer> getFinish() {
		return finish;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	private boolean checkBit(int x, int k) {
		return (x & 1 << k) != 0;
	}

	public List<ImmutablePair<Integer, Integer>> getSuccessors(ImmutablePair<Integer, Integer> cell) {
		return getSuccessors(cell.getLeft(), cell.getRight());
	}

	public List<ImmutablePair<Integer, Integer>> getSuccessors(int i, int j) {
		List<ImmutablePair<Integer, Integer>> successors = new ArrayList<>();
		if (checkBit(data[i][j], 0))
			successors.add(ImmutablePair.of(i - 1, j));
		if (checkBit(data[i][j], 1))
			successors.add(ImmutablePair.of(i + 1, j));
		if (checkBit(data[i][j], 2))
			successors.add(ImmutablePair.of(i, j + 1));
		if (checkBit(data[i][j], 3))
			successors.add(ImmutablePair.of(i, j - 1));
		return successors;
	}

	public double getHeuristic(ImmutablePair<Integer, Integer> cell) {
		return getHeuristic(cell.getLeft(), cell.getRight());
	}

	public double getHeuristic(int i, int j) {
		return Math.sqrt(Math.pow(i - finish.getLeft(), 2) + Math.pow(j - finish.getRight(), 2));
	}

	public void printData() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++)
				System.out.printf("%02d ", data[i][j]);
			System.out.println();
		}
	}

	public void printSuccessors() {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				System.out.printf("(%d,%d) -> %s (heuristic=%.2f)\n", i, j, getSuccessors(i, j), getHeuristic(i, j));
	}

	public void printMaze() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				System.out.print((data[i][j] & 1) == 0 ? "+---" : "+   ");
			}
			System.out.println("+");
			for (int j = 0; j < cols; j++) {
				System.out.print((data[i][j] & 8) == 0 ? "|   " : "    ");
			}
			System.out.println("|");
		}
		for (int j = 0; j < cols; j++) {
			System.out.print("+---");
		}
		System.out.println("+");
	}

	public void printSolved(List<ImmutablePair<Integer, Integer>> path) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				System.out.print((data[i][j] & 1) == 0 ? "+---" : "+   ");
			}
			System.out.println("+");
			for (int j = 0; j < cols; j++) {
				if (path.contains(new ImmutablePair<Integer, Integer>(i, j)))
					System.out.print((data[i][j] & 8) == 0 ? "| * " : "  * ");
				else
					System.out.print((data[i][j] & 8) == 0 ? "|   " : "    ");
			}
			System.out.println("|");
		}
		for (int j = 0; j < cols; j++) {
			System.out.print("+---");
		}
		System.out.println("+");
	}

	public void generateMazeRecursively() {
		generateMazeRecursively(rows - 1, cols - 1);
	}

	private void generateMazeRecursively(int i, int j) {
		List<Character> directions = New.list('N', 'S', 'W', 'E'); // essentials4j
		Collections.shuffle(directions);
		for (char direction : directions) {
			int new_j = j;
			int new_i = i;
			int mask1 = 0;
			int mask2 = 0;
			if (direction == 'N') {
				new_i--;
				mask1 = 1;
				mask2 = 2;
			} else if (direction == 'S') {
				new_i++;
				mask1 = 2;
				mask2 = 1;
			} else if (direction == 'E') {
				new_j++;
				mask1 = 4;
				mask2 = 8;
			} else if (direction == 'W') {
				new_j--;
				mask1 = 8;
				mask2 = 4;
			}
			if (new_j >= 0 && new_j < cols && new_i >= 0 && new_i < rows && data[new_i][new_j] == 0) {
				data[i][j] = data[i][j] | mask1;
				data[new_i][new_j] = data[new_i][new_j] | mask2;
				generateMazeRecursively(new_i, new_j);
			}
		}
	}

	public void generateMaze() {
		HashMap<ImmutablePair<Integer, Integer>, Character> map = new HashMap<>();
		List<Character> directions = New.list('N', 'S', 'W', 'E'); // essentials4j
		Set<ImmutablePair<Integer, Integer>> closed = new HashSet<>();
		Deque<PathCostPair> frontier = new ArrayDeque<PathCostPair>(); // stack
		ArrayList<ImmutablePair<Integer, Integer>> path = new ArrayList<>();
		path.add(start);
		PathCostPair startNode = new PathCostPair(path, 0.0);
		frontier.add(startNode);
		PathCostPair current = frontier.peekFirst();
		ImmutablePair<Integer, Integer> currentCell = current.getPath().get(current.getPath().size() - 1);
		boolean found = true;
		while (!frontier.isEmpty()) {
			if (map.get(currentCell) != null && data[currentCell.getLeft()][currentCell.getRight()] == 0) {
				Character came_from_direction = map.get(currentCell);
				int mask1 = 0;
				int mask2 = 0;
				int old_i = currentCell.getLeft();
				int old_j = currentCell.getRight();
				if (came_from_direction == 'N') {
					mask1 = 1;
					mask2 = 2;
					old_i++;
				} else if (came_from_direction == 'S') {
					mask1 = 2;
					mask2 = 1;
					old_i--;
				} else if (came_from_direction == 'E') {
					mask1 = 4;
					mask2 = 8;
					old_j--;
				} else if (came_from_direction == 'W') {
					mask1 = 8;
					mask2 = 4;
					old_j++;
				}
				data[old_i][old_j] = data[old_i][old_j] | mask1;
				data[currentCell.getLeft()][currentCell
						.getRight()] = data[currentCell.getLeft()][currentCell.getRight()] | mask2;
			}
			frontier.pop();
			if (!closed.contains(currentCell)) {
				closed.add(currentCell);
				Collections.shuffle(directions);
				for (Character direction : directions) {
					int new_i = currentCell.getLeft();
					int new_j = currentCell.getRight();
					if (direction == 'N') {
						new_i--;
					} else if (direction == 'S') {
						new_i++;
					} else if (direction == 'E') {
						new_j++;
					} else if (direction == 'W') {
						new_j--;
					}
					if (new_j >= 0 && new_j < cols && new_i >= 0 && new_i < rows && data[new_i][new_j] == 0) {
						ArrayList<ImmutablePair<Integer, Integer>> newPath = new ArrayList<>(current.getPath());
						ImmutablePair<Integer, Integer> new_cell = new ImmutablePair<>(new_i, new_j);
						newPath.add(new_cell);
						PathCostPair aSearchNode = new PathCostPair(newPath, 0.0);
						frontier.addFirst(aSearchNode);
						map.put(new_cell, direction);
					}
				}
			}
			if (frontier.isEmpty()) {
				found = false;
				break;
			}
			current = frontier.peekFirst();
			currentCell = current.getPath().get(current.getPath().size() - 1);
		}
	}
}