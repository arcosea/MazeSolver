/*
 * Name: Erick Arcos
 * Course: CS 270
 * Assignment: Lab07 & Maze Solver
 * Date: 11/08/21
 * Sources consulted: Textbook, Professor Blaha helped with extra credit
 * Known Bugs: NA
 * Creativity: Demonstrates the most optimal path for maze if it is solvable. 
 * 
 * 
 * NOTE: Originally I had a array Index out of bounce bug because I was checking array at its
 * 		row and column length. In the if statements, I should have had a length -1.
 *		Also, did not consider special case if 
 * 
 * 
 * 
 */




import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;



public class QueueMazeSolver implements MazeSolver{
	
	// Create a MazeGUI display
	private MazeGUI gui;
	
	// Constructor that instantiates a new MazeGUI display
	public QueueMazeSolver() {
		gui = new MazeGUI( this);
	}
	
	
	 /**
     * This method is called when the start button is
     * clicked in the MazeGUI.  This method should solve the maze.
     * This method may call MazeGUI.drawMaze(...) whenever the
     * GUI display should be updated (after each step of the solution).
     *
     * The maze is provided as the first parameter.  It is a 2D array containing
     * characters that represent the spaces in the maze.  The following
     * characters will be found in the array:
     *    '#' - This represents a wall.
     *    ' ' - This represents an open space (corridor)
     *
     * When calling MazeGUI.drawMaze(...) to update the display, the GUI
     * will recognize the '#' and ' ' characters as well as the following:
     *    '@' - Means the cell is a space that has been explored
     *    '%' - Means that the cell is part of the best path to the goal.
     *
     * @param maze the maze (see above).
     * @param startR the row of the start cell.
     * @param startC the column of the start cell.
     * @param endR the row of the end (goal) cell.
     * @param endC the column of the end (goal) cell.
     */
	@Override
	public void solve(char[][] maze, int startR, int startC, int endR, int endC) {
		// Create a queue of type Cell
		Queue<Cell> myQ = new LinkedList<>();
		
		// Mark that the start location has been explored & update maze
		maze[startR][startC] = '@';
		freezeDisplay(maze);
		
		// Create a new Cell that contains the start coordinate and initializes its previous link node as null (contains no prev)
		Cell start = new Cell(startR, startC, null);
		
		// Create a cell that stores current cell viewed on queue. Initialize it with start coordinates and previous link as null
		Cell currentPos  = new Cell(startR, startC, null);
		
		// Add start Cell to Queue
		myQ.offer(start);
			
		// Boolean to determine if target location is found
		boolean foundTarget = false;
		
		// Special case: start is the target cell
		if(startR == endR && startC == endC) {
			foundTarget = true;
		}
		
		// While the Q is not empty and start cell is not end goal cell
		while(!myQ.isEmpty()) {
			
			// Pull out the Cell from Queue 
			currentPos = myQ.poll();
			
			// Store current Row and Column of current location/Cell
			int currentRow = currentPos.getRow();
			int currentCol = currentPos.getCol();
			
			// Check to see if it is target Goal location
			if(currentRow == endR && currentCol == endC) {
				
				// Switch boolean to true since target location is found and thus maze is solvable. Break loop
				foundTarget = true;
				break;
			}
			
			// Check next location one spot/row above current location, if valid to see if its empty 
			 if(currentRow > 0 && maze[currentRow - 1][currentCol] == ' ') {
							
				// Mark new location as explored and modify/freeze display
				maze[currentRow - 1][currentCol] = '@';
				freezeDisplay(maze);
							
				// Create a new Cell, add it to Queue and set previous link as currentPos
				Cell newPos = new Cell(currentRow - 1, currentCol, currentPos);
				myQ.offer(newPos);

			}
				
			
			// Check next location one spot/column to the left of current location, if valid to see if its empty and set previous link as currentPos
			if(currentCol > 0 && maze[currentRow ][currentCol - 1] == ' ') {
					
				// Mark new location as explored and modify/freeze display
				maze[currentRow][currentCol -1] = '@';
				freezeDisplay(maze);
				
				// Create a new Cell, add it to Queue and set previous link as currentPos
				Cell newPos = new Cell(currentRow, currentCol - 1, currentPos);
				myQ.offer(newPos);	

			}
			
			// Check next location one spot/row below current location, if valid to see if its empty and set previous link as currentPos
			if(currentRow < maze.length -1 && maze[currentRow + 1][currentCol] == ' ' ) {
				
				// Mark new location as explored and modify/freeze display
				maze[currentRow +1][currentCol] = '@';
				freezeDisplay(maze);
				
				// Create a new Cell, add it to Queue and set previous link as currentPos
				Cell newPos = new Cell(currentRow+1, currentCol, currentPos);
				myQ.offer(newPos);

			}
			
			
			// Check next location one spot/column to the right of current location, if valid to see if its empty and set previous link as currentPos
			if(currentCol < maze[0].length - 1 && maze[currentRow][currentCol + 1] == ' ') {										
				
				// mark new location as explored and modify/freeze display
				maze[currentRow][currentCol +1] = '@';
				freezeDisplay(maze);
				
				// Create a new Cell, add it to Queue and set previous link as currentPos
				Cell newPos = new Cell(currentRow, currentCol + 1, currentPos);
				myQ.offer(newPos);	

			}	
								
		}
		
		
		// If target was found; Display that the maze was solvable and also display the most optimal path 
		if(foundTarget) {
			gui.setStatusText("Maze is Solvable");
			
			// While link to current currentPos is not null 
			while(currentPos != null) {
				
				// Mark Location, freeze display and move to previous link
				maze[currentPos.r][currentPos.c] ='%';
				freezeDisplay(maze);
				currentPos = currentPos.prev;	
				
			}
		}
		
		// If target is not found, display that the maze is unsolvable
		else {
			gui.setStatusText("Maze is unsolvable");
		}
		
			
		
	} // End
	
	
	
	
	/**
	 * This method create updates the maze and slows the execution/freezes down display for 2/100 of a second
	 * 
	 * @param maze the array that get modified using the GUI drawMaze
	 */
	private void freezeDisplay(char[][] maze) {
		// Updates maze GUI
		gui.drawMaze(maze);
		try {
			// Causes current executing thread to stop for 2/100 of a second
			Thread.sleep(20);	
		}
		catch( InterruptedException e) {
			// Print that interruption occurred
			System.err.println("Thread interrupted");
		}	
	}
	
	
	/*
	 *  Private class Cell in which instances of these 'Cells' represent a particular
	 *  	location/space/square in the maze. Queue used in solve contains elements of 
	 *  	object type Cell. Prev Cell is cell previous to the one being created
	 */
	private class Cell {
		// Create fields r, c to represent location (x,y) in 2-D maze grid. Cell prev is previous link to a cell
		private int r;
		private int c;
		private Cell prev;
		
		
		/*
		 *  Constructor that initializes the fields
		 */
		public Cell(int x, int y, Cell pre) {
			this.r = x;
			this.c = y;
			this.prev = pre;
		}
		
		/**
	     * Returns the row coordinate for a location in maze
	     *
	     * @return r the row coordinate for a location
	     * 
	     * Note: Getter methods were not really needed but made them just cause.
	     */
		public int getRow() {
			return r;
		}
		
		/**
	     * Returns the column coordinate for a location in maze
	     *
	     * @return c the column coordinate for a location
	     */
		public int getCol() {
			return c;
		}
	
	}
	
    
 
  

    
}
