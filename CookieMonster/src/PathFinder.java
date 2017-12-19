import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class PathFinder {
	private Point[][] grid;
	private LinkedList<Point> junctions;
	private Point current;
	private ArrayList<Integer> maxCookies;
	private int numCookies;
	private Point GOAL;
	
	
	public PathFinder() {
		Scanner kybd = new Scanner(System.in);
		System.out.print("Enter the file name: ");
		String fileName = kybd.nextLine();
		Scanner fileGrid = fileReader(fileName);
		
		grid = createGrid(fileGrid);
	}
	
	public Point[][] createGrid(Scanner fileGrid){
		Point[] curRow = new Point[];
		
	}
	
	public Scanner fileReader(String s) {
		File f = new File(s);
		Scanner daGrid = null;
		
		try {
			daGrid = new Scanner(f);
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(0);
		}
		
		return daGrid;
	}
}
