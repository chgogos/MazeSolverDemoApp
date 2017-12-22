package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MazeJsonAgent {

	public static void main(String[] args) throws IOException {
		MazeJsonAgent ma = new MazeJsonAgent();
//		ma.convertGraphToJson("maze_ascii_1.txt", "maze_ascii_1.json");
		ma.convertGraphToJson("maze_ascii_2.txt", "maze_ascii_2.json");

	}
	
	public static Maze loadMaze(File f) {
		return loadMaze(f.getAbsolutePath());
	}

	public static Maze loadMaze(String fn) {
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader(fn));
			JSONObject jsonObject = (JSONObject) obj;

			int rows = ((Long) jsonObject.get("rows")).intValue();
			int cols = ((Long) jsonObject.get("cols")).intValue();
			String s[] = ((String) jsonObject.get("start")).split(",");
			ImmutablePair<Integer, Integer> start = new ImmutablePair<Integer, Integer>(Integer.parseInt(s[0]),
					Integer.parseInt(s[1]));
			s = ((String) jsonObject.get("finish")).split(",");
			ImmutablePair<Integer, Integer> finish = new ImmutablePair<Integer, Integer>(Integer.parseInt(s[0]),
					Integer.parseInt(s[1]));
			JSONArray grid = (JSONArray) jsonObject.get("grid");
			int a[][] = new int[rows][cols];
			Iterator<Long> iterator = grid.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				a[i / cols][i % cols] = iterator.next().intValue();
				i++;
			}
			Maze maze = new Maze(rows, cols, a, start, finish);
			return maze;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void storeMaze(Maze maze, String fn) {
		JSONObject obj = new JSONObject();
		obj.put("rows", maze.rows);
		obj.put("cols", maze.cols);
		obj.put("start", String.format("%d,%d", maze.start.getLeft(), maze.start.getRight()));
		obj.put("finish", String.format("%d,%d", maze.finish.getLeft(), maze.finish.getRight()));
		JSONArray grid = new JSONArray();
		for (int i = 0; i < maze.rows; i++)
			for (int j = 0; j < maze.cols; j++)
				grid.add(maze.data[i][j]);
		obj.put("grid", grid);
		FileWriter file;
		try {
			file = new FileWriter(fn);
			file.write(obj.toString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads a file into an array of strings, one per line.
	 */
	private static String[] readLines(InputStream f) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(f, "US-ASCII"));
		ArrayList<String> lines = new ArrayList<String>();
		String line;
		while ((line = r.readLine()) != null)
			lines.add(line);
		return lines.toArray(new String[0]);
	}

	/**
	 * Makes the maze half as wide (i. e. "+---+" becomes "+-+"), so that each cell
	 * in the maze is the same size horizontally as vertically. (Versus the expanded
	 * version, which looks better visually.)
	 */
	private static char[][] decimateHorizontally(String[] lines) {
		final int width = (lines[0].length() + 1) / 2;
		char[][] c = new char[lines.length][width];
		for (int i = 0; i < lines.length; i++)
			for (int j = 0; j < width; j++)
				c[i][j] = lines[i].charAt(j * 2);
		return c;
	}

	public void convertGraphToJson(String fn, String jfn) throws IOException {
		String[] lines = readLines(new FileInputStream(fn));
		char[][] maze = decimateHorizontally(lines);
		int rows = maze.length / 2;
		int cols = maze[0].length / 2;
		int a[][] = new int[rows][cols];
		for (int i = 1; i < maze.length; i += 2)
			for (int j = 1; j < maze[i].length; j += 2) {
				if (maze[i - 1][j] == ' ')
					a[i / 2][j / 2] = a[i / 2][j / 2] | 1;
				if (maze[i + 1][j] == ' ')
					a[i / 2][j / 2] = a[i / 2][j / 2] | 2;
				if (maze[i][j + 1] == ' ')
					a[i / 2][j / 2] = a[i / 2][j / 2] | 4;
				if (maze[i][j - 1] == ' ')
					a[i / 2][j / 2] = a[i / 2][j / 2] | 8;
			}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++)
				System.out.printf("%2d ", a[i][j]);
			System.out.println();
		}

		JSONObject obj = new JSONObject();
		obj.put("rows", rows);
		obj.put("cols", cols);
		obj.put("start", String.format("%d,%d", rows - 1, cols - 1));
		obj.put("finish", String.format("%d,%d", 0, 0));
		JSONArray grid = new JSONArray();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				grid.add(a[i][j]);
		obj.put("grid", grid);
		FileWriter file;
		try {
			file = new FileWriter(jfn);
			file.write(obj.toString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
