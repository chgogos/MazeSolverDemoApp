package model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MazeJsonAgent {

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
}
