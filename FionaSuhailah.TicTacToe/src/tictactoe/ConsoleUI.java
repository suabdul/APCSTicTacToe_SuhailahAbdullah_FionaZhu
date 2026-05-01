package tictactoe;

import java.util.Scanner;

public class ConsoleUI {

	public static void main(String[] args) 
	{
		Board board = new Board("board.csv");
		GameLogic logic = new GameLogic();
		Scanner input = new Scanner(System.in);

		board.clearBoard();

		System.out.println("=== Tic Tac Toe ===");
		System.out.println("Enter row and column from 1 to 3.");

		while (!logic.isGameOver(board)) 
		{
			printBoard(board);

			char currentPlayer = logic.getCurrentPlayer(board);
			System.out.println("Player " + currentPlayer + ", it's your turn.");

			int row = promptForNumber(input, "Row (1-3): ", 1, 3) - 1;
			int col = promptForNumber(input, "Col (1-3): ", 1, 3) - 1;

			boolean moved = logic.makeMove(board, row, col);
			if (!moved) 
			{
				System.out.println("Invalid move. Try again.");
			}
		}

		printBoard(board);

		if (logic.checkWin(board, 'X')) 
		{
			System.out.println("Player X wins!");
		} 
		else if (logic.checkWin(board, 'O')) 
		{
			System.out.println("Player O wins!");
		} 
		else 
		{
			System.out.println("It's a draw!");
		}

		input.close();
	}

	public static void printBoard(Board board) 
	{
		System.out.println();
		for (int row = 0; row < 3; row++) 
		{
			for (int col = 0; col < 3; col++) 
			{
				char cell = board.getCell(row, col);
				if (cell == 'E') 
				{
					System.out.print("-");
				} else 
				{
					System.out.print(cell);
				}

				if (col < 2) 
				{
					System.out.print(" | ");
				}
			}
			System.out.println();
			if (row < 2) 
			{
				System.out.println("---------");
			}
		}
		System.out.println();
	}

	public static int promptForNumber(Scanner input, String message, int min, int max) 
	{
		while (true) 
		{
			System.out.print(message);

			if (!input.hasNextInt()) 
			{
				System.out.println("Please enter a whole number.");
				input.next(); // discard invalid token
				continue;
			}

			int value = input.nextInt();
			if (value < min || value > max) 
			{
				System.out.println("Please enter a number between " + min + " and " + max + ".");
			} else {
				return value;
			}
		}
	}
}