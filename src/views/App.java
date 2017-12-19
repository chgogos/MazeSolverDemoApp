package views;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.google.common.io.PatternFilenameFilter;

import model.Maze;
import model.MazeJsonAgent;
import prolog.PrologDFSSolver;
import solver.DepthFirstSearchSolver;

public class App {

	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		App app = new App();
		int choice = -1;
		do {
			System.out.println("1. Load maze from file");
			System.out.println("2. Create random maze using DFS (resursive)");
			System.out.println("3. Create random maze using DFS");
			System.out.println("4. ASTAR (java)");
			System.out.println("5. Depth First Search (java)");
			System.out.println("6. Depth First Search (Prolog)");
			System.out.println("0. Exit");
			System.out.print("Enter option: ");
			choice = sc.nextInt();
			if (choice == 1)
				app.scenario1();
			else if (choice == 2)
				app.scenario2();
			else if (choice == 3)
				app.scenario3();
			else if (choice == 4)
				app.scenario4();
			else if (choice == 5)
				app.scenario5();
			else if (choice == 6)
				app.scenario6();
		} while (choice != 0);
		sc.close();
	}

	private Maze maze = null;

	void scenario1() {
		Pattern pattern = Pattern.compile("^.*.json"); // guava
		FilenameFilter filterByExtension = new PatternFilenameFilter(pattern);
		File dir = new File(".");
		File[] files = dir.listFiles(filterByExtension);
		if (files.length == 0)
			return;
		for (int i = 0; i < files.length; i++) {
			System.out.printf("%d. %s\n", i + 1, files[i].getName());
		}
		System.out.print("Choose file: ");
		int choice = sc.nextInt();
		maze = MazeJsonAgent.loadMaze(files[choice - 1]);
		maze.printData();
		maze.printMaze();
	}

	void scenario2() {
		System.out.print("Enter maze rows: ");
		int rows = sc.nextInt();
		System.out.print("Enter maze columns: ");
		int cols = sc.nextInt();
		maze = new Maze(rows, cols);
		maze.generateMazeRecursively();
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String fn = String.format("maze_%dx%d_%s.json", rows, cols, timeStamp);
		MazeJsonAgent.storeMaze(maze, fn);
		System.out.println("Maze generated: " + fn);
		maze.printMaze();
	}

	void scenario3() {
		System.out.print("Enter maze rows: ");
		int rows = sc.nextInt();
		System.out.print("Enter maze columns: ");
		int cols = sc.nextInt();
		maze = new Maze(rows, cols);
		maze.generateMaze();
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String fn = String.format("maze_%dx%d_%s.json", rows, cols, timeStamp);
		MazeJsonAgent.storeMaze(maze, fn);
		System.out.println("Maze generated: " + fn);
		maze.printMaze();
	}

	void scenario4() {
		// TO BE IMPLEMENTED
	}

	void scenario5() {
		if (maze == null) {
			System.err.println("No maze is loaded!");
			return;
		}
		DepthFirstSearchSolver solver = new DepthFirstSearchSolver(maze);
		maze.printSolved(solver.solve());
		System.exit(0);
	}

	void scenario6() {
		if (maze == null) {
			System.err.println("No maze is loaded!");
			return;
		}
		PrologDFSSolver solver = new PrologDFSSolver(maze);
		solver.solve();
		System.exit(0);
	}

}
