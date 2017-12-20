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
		
		grid = convertGrid(fileGrid);
		
		GOAL = grid[grid.length-1][grid[0].length-1];
		current = grid[0][0];
		numCookies = 0;
		ArrayList<Integer> maxCookies = new ArrayList<Integer>();
		junctions = new LinkedList<Point>();
		
		System.out.println("The Max number of possible cookies is: " + run());
	}
	
	/**
	 * 
	 * @return Returns a copy of the point to the right of the current one
	 */
	public Point peekRight(){
		return new Point(grid[current.getR()][current.getC()+1]);
	}
	
	/**
	 * 
	 * @return Returns a copy of the point under the current one
	 */
	public Point peekDown(){
		return new Point(grid[current.getR()+1][current.getC()]);
	}
	
	public int run(){
		boolean ifTerminated;
		int cookieMax = 0;
		do{
			ifTerminated = moveNext();
		} while(!ifTerminated);
		
		if(current.equals(GOAL)){
			maxCookies.add(numCookies);
			numCookies = 0;
			if(!junctions.isEmpty()){
				current = junctions.pop();
				run();
			}
			else{
				cookieMax = maxCookies.get(0);
				for(int i = 1; i < maxCookies.size(); i++){
					if(maxCookies.get(i) > cookieMax)
						cookieMax = maxCookies.get(i);
				}
			}
		}
		else{
			current = junctions.pop();
			run();
		}
		
		if(cookieMax == 0){
			System.out.println("No Possible solutions!!!");
			System.exit(1);
		}
		return cookieMax;
	}
	
	/**
	 * 
	 * @return 0 if down, 1 if right, -1 if not possible for next moves
	 */
	public int determineNext(){
		int downVal,rightVal;
		try{
			downVal = grid[current.getR()+1][current.getC()].getVal();
		}
		catch(IndexOutOfBoundsException e){
			downVal = -1;
		}

		try{
			rightVal = grid[current.getR()][current.getC()+1].getVal();
		}
		catch(IndexOutOfBoundsException e){
			rightVal = -1;
		}

		if(downVal == -1 && rightVal == -1)
			return -1;
		if(downVal > rightVal)
			return 0;
		else
			return 1;
	}
	
	public boolean moveNext(){
		numCookies += current.getVal();
		
		if(determineNext() == -1)
			return true;
		
		if(determineNext() == 0 && peekRight().getVal() != -1){
			junctions.push(peekRight());
			current = peekDown();
		}
		else{
			if(determineNext() == 0)
				current = peekDown();
			else{
				if(determineNext() == 1 && peekDown().getVal() != -1){
					junctions.push(peekDown());
					current = peekRight();
				}
				else{
					if(determineNext() == 1)
						current = peekRight();
				}
			}
		}
		return false;	
	}
	
	
	/**
	 * converts the 2D array of ints from createGrid into using points
	 * @param fileGrid scanner containing the maze
	 * @return a 2D array of points that represent the maze
	 */
	public Point[][] convertGrid(Scanner fileGrid){
		
		int[][] unconvert = createGrid(fileGrid);
		Point[][] ret = new Point[unconvert.length][unconvert[0].length];
		
		for(int r = 0; r < unconvert.length; r++){
			for(int c = 0; c < unconvert[0].length; c++){
				ret[r][c] = new Point(r,c, unconvert[r][c]);
			}
		}
		
		return ret;
	}
	
	/**
	 * This takes in a scanner and then converts into an int[][]
	 * @param fileGrid the scanner containing the grid
	 * @return a 2D array of ints that represents the maze
	 */
	public int[][] createGrid(Scanner fileGrid){
		int[][] ret = new int[][]{};
		String line;
		int rowNum = 0;
		while (fileGrid.hasNextLine()){
	    
	      line = fileGrid.nextLine();
	      String[] unparsed = line.split(" ");
	      int[] row = new int[unparsed.length];
	      
	      for(int i = 0; i < unparsed.length; i++){
	    	  row[i] = Integer.parseInt(unparsed[i]);
	      }
	      
	      ret[rowNum] = row;
	      rowNum++;
	    }
		
		return ret;
	}
	
	/**
	 * reads in a file
	 * @param s file name
	 * @return scanner representing the given file
	 */
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
