package prolog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang3.tuple.ImmutablePair;

import model.Maze;

public class PrologDFSSolver {
	Maze maze;

	public PrologDFSSolver(Maze maze) {
		this.maze = maze;
	}

	public void solve() {
		StringBuilder sb = new StringBuilder();
		// generate facts
		for (int i = 0; i < maze.getRows(); i++)
			for (int j = 0; j < maze.getRows(); j++) {
				for (ImmutablePair<Integer, Integer> succ : maze.getSuccessors(i, j)) {
					sb.append(String.format("c(cell(%d,%d), cell(%d,%d)).\n", i, j, succ.getLeft(), succ.getRight()));
				}
			}
		sb.append(String.format("goal(cell(%d,%d)).\n", maze.getFinish().getLeft(), maze.getFinish().getRight()));

		// append rules 
		try {
			sb.append(new String(Files.readAllBytes(Paths.get("dfs.pl"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		sb.append("\n");
		System.out.println(sb.toString());
	}

}
