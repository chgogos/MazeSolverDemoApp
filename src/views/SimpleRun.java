package views;

import model.Maze;
import model.MazeJsonAgent;
import solver.BreadthFirstSearchSolver;
import solver.DepthFirstSearchSolver;

public class SimpleRun {

	public static void main(String[] args) {
		Maze maze = MazeJsonAgent.loadMaze("maze_ascii_1.json");
		// Maze maze = MazeJsonAgent.loadMaze("maze_ascii_2.json");
		DepthFirstSearchSolver solver2 = new DepthFirstSearchSolver(maze);
		solver2.solve();
		BreadthFirstSearchSolver solver3 = new BreadthFirstSearchSolver(maze);
		solver3.solve();
	}

}
