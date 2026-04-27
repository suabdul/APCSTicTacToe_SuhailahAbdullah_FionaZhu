package tictactoe;

import java.util.Scanner;

public class ConsoleUI {
	private static void printBoard(Board board) {
		System.out.println();
		System.out.println("Current board:");
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				char cell = board.getCell(row, col);
				if (cell == 'E') {
					cell = '-';
				}
				System.out.print(cell + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private static int readCoordinate(Scanner input, String coordinateName) {
		while (true) {
			System.out.print("Enter " + coordinateName + " (0-2): ");
			if (!input.hasNextInt()) {
				System.out.println("Invalid input. Please enter a number from 0 to 2.");
				input.next();
				continue;
			}

			int value = input.nextInt();
			if (value < 0 || value > 2) {
				System.out.println("Out of range. Please enter 0, 1, or 2.");
				continue;
			}
			return value;
		}
	}

	public static void main(String[] args) {
		Board board = new Board("board.csv");
		GameLogic gameLogic = new GameLogic();
		Scanner input = new Scanner(System.in);

		if (board.getGrid() == null) {
			System.out.println("Board file is missing or invalid. Fix board.csv and try again.");
			input.close();
			return;
		}

		System.out.println("Tic-Tac-Toe Console");
		System.out.println("Use row and column values from 0 to 2.");

		while (!gameLogic.isGameOver(board)) {
			printBoard(board);
			char currentPlayer = gameLogic.getCurrentPlayer(board);
			System.out.println("Player " + currentPlayer + "'s turn.");

			int row = readCoordinate(input, "row");
			int col = readCoordinate(input, "column");

			boolean moveMade = gameLogic.makeMove(board, row, col);
			if (!moveMade) {
				System.out.println("That move is not allowed. Try again.");
			}
		}

		printBoard(board);
		if (gameLogic.checkWin(board, 'X')) {
			System.out.println("Game over. Player X wins!");
		} else if (gameLogic.checkWin(board, 'O')) {
			System.out.println("Game over. Player O wins!");
		} else {
			System.out.println("Game over. It's a draw!");
		}

		input.close();
	}
}
