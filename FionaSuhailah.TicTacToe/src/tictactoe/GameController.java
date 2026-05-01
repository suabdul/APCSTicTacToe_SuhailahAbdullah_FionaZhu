package tictactoe;

public class GameController {
    private final Board board;
    private final GameLogic logic;

    public GameController(String filename) {
        board = new Board(filename);
        logic = new GameLogic();
        board.clearBoard();
    }

    public char getCell(int row, int col) {
        return board.getCell(row, col);
    }

    public boolean makeMove(int row, int col) {
        return logic.makeMove(board, row, col);
    }

    public char getCurrentPlayer() {
        return logic.getCurrentPlayer(board);
    }

    public boolean isGameOver() {
        return logic.isGameOver(board);
    }

    public boolean isDraw() {
        return logic.isDraw(board);
    }

    public char getWinner() {
        if (logic.checkWin(board, 'X')) return 'X';
        if (logic.checkWin(board, 'O')) return 'O';
        return 'E';
    }

    public void resetBoard() {
        board.clearBoard();
    }

    public void resetGame() {
        resetBoard();
    }
}