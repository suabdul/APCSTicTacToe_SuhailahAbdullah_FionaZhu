package tictactoe;

import java.util.Scanner;

/**
 * ╔══════════════════════════════════════════╗
 * ║     TIC-TAC-TOE  ░  CONSOLE  EDITION    ║
 * ║         Phase 5A  —  AP Exam Ready       ║
 * ╚══════════════════════════════════════════╝
 *
 * Pure console UI.  All game logic lives in
 * Board + GameLogic — this class only reads
 * input and renders output.
 */
public class ConsoleU {

    // ── ANSI colour codes (gracefully ignored on terminals that strip them) ──
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String CYAN   = "\u001B[96m";
    private static final String YELLOW = "\u001B[93m";
    private static final String GREEN  = "\u001B[92m";
    private static final String RED    = "\u001B[91m";
    private static final String MAGENTA= "\u001B[95m";
    private static final String DIM    = "\u001B[2m";

    // ── Board file shared with the Swing UI and GameLogic ───────────────────
    private static final String BOARD_FILE = "board.csv";

    // ────────────────────────────────────────────────────────────────────────

    public static void main(String[] args) {

        printBanner();

        Board     board  = new Board(BOARD_FILE);
        GameLogic logic  = new GameLogic();
        Scanner   sc     = new Scanner(System.in);

        // Start fresh
        board.clearBoard();
        board.saveBoardToFile();

        while (true) {

            printBoard(board);

            if (logic.isGameOver(board)) {
                // isGameOver is true after checkWin or isDraw
                // We need to know WHICH ending it was.
                // Re-derive: check both players then draw.
                char winner = detectWinner(board, logic);
                if (winner != 'E') {
                    printWin(winner);
                } else {
                    printDraw();
                }
                if (!askPlayAgain(sc)) break;
                board.clearBoard();
                board.saveBoardToFile();
                continue;
            }

            char current = logic.getCurrentPlayer(board);
            printPrompt(current);

            int[] move = getMove(sc, board, logic);
            if (move == null) break;   // player typed 'q'

            logic.makeMove(board, move[0], move[1]);
            board.saveBoardToFile();
        }

        sc.close();
        printGoodbye();
    }

    // ── Banner ───────────────────────────────────────────────────────────────

    private static void printBanner() {
        System.out.println();
        System.out.println(CYAN + BOLD +
            "  ╔══════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + BOLD +
            "  ║  ✦  TIC · TAC · TOE  ✦  v1.0        ║" + RESET);
        System.out.println(CYAN + BOLD +
            "  ║     File-Backed  |  Console  Edition  ║" + RESET);
        System.out.println(CYAN + BOLD +
            "  ╚══════════════════════════════════════╝" + RESET);
        System.out.println(DIM + "  Board state is persisted to: board.csv" + RESET);
        System.out.println(DIM + "  Type  row,col  (0-indexed) to play.  'q' to quit." + RESET);
        System.out.println();
    }

    // ── Board renderer ───────────────────────────────────────────────────────

    private static void printBoard(Board board) {
        System.out.println();
        System.out.println(DIM + "     0   1   2" + RESET);
        System.out.println(DIM + "   ┌───┬───┬───┐" + RESET);

        char[][] grid = board.getGrid();
        for (int r = 0; r < 3; r++) {
            System.out.print(DIM + " " + r + " │" + RESET);
            for (int c = 0; c < 3; c++) {
                char cell = grid[r][c];
                System.out.print(" " + colourCell(cell) + " " + DIM + "│" + RESET);
            }
            System.out.println();
            if (r < 2) {
                System.out.println(DIM + "   ├───┼───┼───┤" + RESET);
            }
        }
        System.out.println(DIM + "   └───┴───┴───┘" + RESET);
        System.out.println();
    }

    private static String colourCell(char c) {
        switch (c) {
            case 'X': return YELLOW + BOLD + "X" + RESET;
            case 'O': return MAGENTA + BOLD + "O" + RESET;
            default:  return DIM + "·" + RESET;
        }
    }

    // ── Input ────────────────────────────────────────────────────────────────

    private static void printPrompt(char player) {
        String colour = (player == 'X') ? YELLOW : MAGENTA;
        System.out.print(colour + BOLD + "  Player " + player + RESET
            + "  ▶  Enter row,col : ");
    }

    /**
     * Keeps asking until a valid, unoccupied cell is chosen.
     * Returns null if the user types 'q'.
     */
    private static int[] getMove(Scanner sc, Board board, GameLogic logic) {
        while (true) {
            String raw = sc.nextLine().trim();
            if (raw.equalsIgnoreCase("q")) return null;

            String[] parts = raw.split("[,\\s]+");
            if (parts.length != 2) {
                printError("Enter two numbers separated by a comma, e.g.  1,2");
                continue;
            }
            try {
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);

                if (row < 0 || row > 2 || col < 0 || col > 2) {
                    printError("Row and col must each be 0, 1, or 2.");
                    continue;
                }
                if (board.getCell(row, col) != 'E') {
                    printError("That cell is already taken! Pick another.");
                    continue;
                }
                return new int[]{row, col};

            } catch (NumberFormatException e) {
                printError("Not a number. Try again, e.g.  0,1");
            }
        }
    }

    // ── End-game messages ────────────────────────────────────────────────────

    private static void printWin(char winner) {
        String colour = (winner == 'X') ? YELLOW : MAGENTA;
        System.out.println();
        System.out.println(colour + BOLD +
            "  ╔══════════════════════════════════╗" + RESET);
        System.out.println(colour + BOLD +
            "  ║   🏆  Player " + winner + " WINS!  🏆         ║" + RESET);
        System.out.println(colour + BOLD +
            "  ╚══════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private static void printDraw() {
        System.out.println();
        System.out.println(GREEN + BOLD +
            "  ╔══════════════════════════════════╗" + RESET);
        System.out.println(GREEN + BOLD +
            "  ║   🤝  It's a DRAW!  Well played!  ║" + RESET);
        System.out.println(GREEN + BOLD +
            "  ╚══════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private static boolean askPlayAgain(Scanner sc) {
        System.out.print(CYAN + "  Play again? (y/n) : " + RESET);
        String ans = sc.nextLine().trim().toLowerCase();
        return ans.equals("y") || ans.equals("yes");
    }

    private static void printGoodbye() {
        System.out.println();
        System.out.println(CYAN + BOLD + "  Thanks for playing!  ✦" + RESET);
        System.out.println();
    }

    private static void printError(String msg) {
        System.out.println(RED + "  ⚠  " + msg + RESET);
        System.out.print("     ▶  Try again : ");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Figures out which player won (or 'E' for draw).
     * Called only when isGameOver() is true.
     */
    private static char detectWinner(Board board, GameLogic logic) {
        if (logic.checkWin(board, 'X')) return 'X';
        if (logic.checkWin(board, 'O')) return 'O';
        return 'E';  // draw
    }
}