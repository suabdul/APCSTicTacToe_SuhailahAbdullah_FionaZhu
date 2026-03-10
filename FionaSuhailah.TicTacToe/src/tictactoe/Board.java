package tictactoe;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Board {
	// holds game play data in an instance variable
	private char[][] grid;


	// holds game play data in a CSV file
	private String filename;

	// non-default constructor - [5 points]
	public Board(String filename)
	{
		// set the file name
		// if the board is valid then create the 3x3 grid
		// and load the board from the file
		this.filename = filename;
		if (isValidBoardFile()) {
			this.grid = new char[3][3];
			loadBoardFromFile();
		}
	}
	
	public char getCell(int row, int col)
	{
		return this.grid[row][col];
	}
	
	public void setCell(int row, int col, char player)
	{
		grid[row][col] = player;
		 saveBoardToFile();   // update the file after changing one cell
	}
	
	public char[][] getGrid(){
		return this.grid;
	}

	public void setGrid(char[][] newGrid)
	{
		this.grid = newGrid;
	    saveBoardToFile();   // update the file after replacing the grid
		//if you make change to the grid, the board has to change
		//the file has to be updated everytime you have to make a change 
	}
	
	
	
	
	
	// loads the grid with the file contents - [5 points]
	public void loadBoardFromFile()
	{

		// Use a scanner to read the board file
		// and populate the grid with the board values
		// remember to close the scanner afterwards
		// use isValidBoard method as a guide
		try {
			File file = new File("src/tictactoe/" + this.filename);
			Scanner scanner = new Scanner(file);
			int row = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				grid[row][0] = line.charAt(0);
				grid[row][1] = line.charAt(2);
				grid[row][2] = line.charAt(4);
				row++;
			}
			scanner.close();
		} catch (Exception error)
		
		{
			error.printStackTrace();
		}
	}


	// valid if it resembles a 3x3 board that contains only E, X, O
	public boolean isValidBoardFile() {
	    try {
	        File file = new File("src/tictactoe/" + this.filename);
	        Scanner scanner = new Scanner(file);
	        int xCount = 0, oCount = 0;
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine().trim();
	            // flexible CSV: allow optional spaces
	            if (!line.matches("[EXO]\\s*,\\s*[EXO]\\s*,\\s*[EXO]")) {
	                scanner.close();
	                return false;
	            }

	            char[] cells = line.replace(" ", "").toCharArray();
	            for (char c : cells) {
	                if (c == 'X') xCount++;
	                else if (c == 'O') oCount++;
	            }
	        }

	        scanner.close();
	        return (xCount == oCount) || (xCount == oCount + 1);
	    } catch (Exception error) {
	        error.printStackTrace();
	        return false;
	    }
	}

	// saves the grid to the file in the proper format (CSV)
	public void saveBoardToFile() {
		try {
			File file = new File("src/tictactoe/" + this.filename);
			FileWriter writer = new FileWriter(file);
			String boardContents = "";
			for (int row = 0; row < grid.length; row++) {
				for (int col = 0; col < grid[0].length; col++) {
					if (col < 2)
						boardContents += grid[row][col] + ",";
					else
						boardContents += grid[row][col];

				}
				if (row < 2)
					boardContents += "\n";
			}
			writer.write(boardContents);
			writer.close();
		} catch (Exception error) {
			error.printStackTrace();
		}

	}

	/*** These are the methods used to test those above ***/
	// prints the current grid
	public void printGrid() {
		for (int row = 0; row < this.grid.length; row++) {
			for (int col = 0; col < grid[0].length; col++) {
				System.out.print(grid[row][col] + " ");
			}
			System.out.println();
		}
	}

	// create a random board
	public void createRandomBoard() 
	{
		char[] options = { 'E', 'X', 'O' };
		int len = options.length;
		char randomBoard[][] = new char[3][3];
		for (int row = 0; row < randomBoard.length; row++) {
			char[] randomRow = {options[(int) (Math.random() * len)],
					options[(int) (Math.random() * len)],
					options[(int) (Math.random() * len)], };
			randomBoard[row] = randomRow;
		}
		this.grid = randomBoard;
		this.saveBoardToFile();
	}

	// clears the grid by placing E in every cell
	public void clearBoard() {
		char clearedBoard[][] = {	{ 'E', 'E', 'E' },
									{ 'E', 'E', 'E' },
									{ 'E', 'E', 'E' } };

		this.grid = clearedBoard;
		this.saveBoardToFile();
	}

	public static void main(String args[])
	{
		Board b = new Board("board.csv");
		b.createRandomBoard();
		b.printGrid();
		System.out.println(b.isValidBoardFile());
		//b.saveBoardToFile();
		//b.loadBoardFromFile();
		//System.out.println();
		//b.printGrid();
	}
}