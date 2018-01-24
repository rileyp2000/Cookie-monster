import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * this is where the best path gets determined
 * 
 * @author Patrick
 *
 */
public class PathFinder {
	private int[][] grid;
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
		/*
		 * for(int r = 0; r < grid.length; r++){ for(int c = 0; c <
		 * grid[0].length; c++){ System.out.println(grid[r][c]); }
		 * System.out.println(); }
		 */

		GOAL = new Point(grid.length - 1, grid[0].length - 1, grid[grid.length - 1][grid[0].length - 1]);
		current = new Point(0, 0, grid[0][0]);
		numCookies = 0;
		maxCookies = new ArrayList<Integer>();
		junctions = new LinkedList<Point>();

		System.out.println("The Max number of possible cookies is: " + run());

		kybd.close();
	}

	/**
	 * This takes in a scanner and then converts into an int[][]
	 * 
	 * @param fileGrid
	 *            the scanner containing the grid
	 * @return a 2D array of ints that represents the maze
	 */
	public int[][] createGrid(Scanner fileGrid) {
		String line;

		ArrayList<int[]> temp = new ArrayList<int[]>();
		while (fileGrid.hasNextLine()) {

			line = fileGrid.nextLine();
			// String[] unparsed = line.split(" ");
			ArrayList<String> unparsed = new ArrayList<String>(Arrays.asList(line.split(" ")));
			for (int i = unparsed.size() - 1; i >= 0; i--) {
				if (unparsed.get(i).equals(""))
					unparsed.remove(i);
			}

			int[] row = new int[unparsed.size()];

			for (int i = 0; i < unparsed.size(); i++) {
				// System.out.println(unparsed.get(i));
				row[i] = Integer.parseInt(unparsed.get(i));
			}

			temp.add(row);
		}
		int[][] ret = new int[temp.size()][temp.get(0).length];
		for (int i = 0; i < temp.size(); i++)
			ret[i] = temp.get(i);
		return ret;
	}

	/**
	 * reads in a file
	 * 
	 * @param s
	 *            file name
	 * @return scanner representing the given file
	 */
	public Scanner fileReader(String s) {
		File f = new File(s);
		Scanner daGrid = null;

		try {
			daGrid = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(0);
		}

		return daGrid;
	}

	public int run() {
		numCookies = 0;
		do {
			if (peekRight() != -1 && peekDown() != -1) {
				junctions.push(new Point(current.getR(), current.getC() + 1, grid[current.getR()][current.getC() + 1]));
				current = new Point(current.getR() + 1, current.getC(), grid[current.getR() + 1][current.getC()]);
				numCookies += current.getVal();
			} else {
				if (peekRight() == -1) {
					current = new Point(current.getR() + 1, current.getC(), grid[current.getR() + 1][current.getC()]);
					numCookies += current.getVal();
				} else {
					current = new Point(current.getR(), current.getC() + 1, grid[current.getR()][current.getC() + 1]);
					numCookies += current.getVal();
				}
			}

		} while (peekRight() != -1 || peekDown() != -1);

		if (current.equals(GOAL))
			maxCookies.add(numCookies);
		if (!junctions.isEmpty()) {
			current = junctions.pop();
			run();
		}

		int totMax = maxCookies.get(0);
		for (Integer i : maxCookies) {
			if (i > totMax)
				totMax = i;
		}
		return totMax;
	}

	/**
	 * 
	 * @return Returns a copy of the point to the right of the current one
	 */
	public int peekRight() {
		int ret;
		try {
			ret = grid[current.getR()][current.getC() + 1];
		} catch (IndexOutOfBoundsException e) {
			ret = -1;
		}
		return ret;
	}

	/**
	 * 
	 * @return Returns a copy of the point under the current one
	 */
	public int peekDown() {
		int ret;
		try {
			ret = grid[current.getR() + 1][current.getC()];
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
		return ret;
	}

	/*
	 * public int run() { boolean ifTerminated; int cookieMax = 0; do {
	 * ifTerminated = moveNext(); } while (!ifTerminated);
	 * 
	 * if (current.equals(GOAL)) { maxCookies.add(numCookies); numCookies = 0;
	 * if (!junctions.isEmpty()) { current = junctions.pop(); run(); } else {
	 * cookieMax = maxCookies.get(0); for (int i = 1; i < maxCookies.size();
	 * i++) { if (maxCookies.get(i) > cookieMax) cookieMax = maxCookies.get(i);
	 * } } } else { if (!junctions.isEmpty()) { current = junctions.pop();
	 * run(); } }
	 * 
	 * if (cookieMax == 0) { System.out.println("No Possible solutions!!!");
	 * System.exit(1); } return cookieMax; }
	 * 
	 *//**
		 * 
		 * @return 0 if down, 1 if right, -1 if not possible for next moves
		 *//*
		 * public int determineNext() { int downVal, rightVal;
		 * 
		 * try{ downVal = grid[current.getR()+1][current.getC()].getVal(); }
		 * catch(IndexOutOfBoundsException e){ downVal = -1; }
		 * 
		 * if (peekDown().getC() == -1) downVal = -1; else downVal =
		 * peekDown().getVal();
		 * 
		 * 
		 * try{ rightVal = grid[current.getR()][current.getC()+1].getVal(); }
		 * catch(IndexOutOfBoundsException e){ rightVal = -1; }
		 * 
		 * if (peekRight().getC() == -1) rightVal = -1; else rightVal =
		 * peekRight().getVal();
		 * 
		 * if (downVal == -1 && rightVal == -1) return -1; if (downVal >
		 * rightVal) return 0; else return 1; }
		 * 
		 * public boolean moveNext() { numCookies += current.getVal();
		 * 
		 * if (determineNext() == -1) return true;
		 * 
		 * if (determineNext() == 0 && peekRight().getVal() != -1) {
		 * junctions.push(peekRight()); current = peekDown(); } else { if
		 * (determineNext() == 0) current = peekDown(); else { if
		 * (determineNext() == 1 && peekDown().getVal() != -1) {
		 * junctions.push(peekDown()); current = peekRight(); } else { if
		 * (determineNext() == 1) current = peekRight(); } } } return false; }
		 */
}
