import java.util.Random;

/**
 * The ConnectFourLogic class represents the game logic for Connect Four.
 * It manages the game board, player turns, and win conditions.
 */
public class ConnectFourLogic {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final int SEQ_LEN = 4;
    private final int PLAYER1 = 1;
    private final int PLAYER2 = 2;

    private int[][] board;
    private int[] nextAvailRow;
    private int currPlayer;
    private int nextPlayer;
    private boolean gameOn;

    /**
     * Constructor for a new ConnectFourLogic object,
     * setting up the game board and player information.
     */
    public ConnectFourLogic() {
        board = new int[ROWS][COLS];
        nextAvailRow = new int[COLS];
        restartGame();
    }

    public static int getNumOfCols() {
        return COLS;
    }

    public static int getNumOfRows() {
        return ROWS;
    }

    public int getCurrPlayer() {
        return currPlayer;
    }

    public int getNextPlayer() {
        return nextPlayer;
    }

    public boolean isGameOn() {
        return gameOn;
    }


    /**
     * Restarts the game, initializing the board and determining the starting player randomly.
     */
    public void restartGame() {
        //get the first player
        Random rand = new Random();
        currPlayer = rand.nextBoolean() ? PLAYER1 : PLAYER2;
        nextPlayer = currPlayer;

        // restarting the available rows for each column
        for (int i = 0; i < COLS; i++) {
            nextAvailRow[i] = ROWS - 1;
        }

        // restarting the board
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = 0;
            }
        }

        gameOn = true;
    }


    /**
     * Drops a disk into the specified column.
     *
     * @param col The column where the disk will be dropped.
     * @return The row where the disk is dropped, or -1 if the column is full.
     */
    public int dropDisk(int col) {
        int row = getNextAvailableRow(col);
        if (row >= 0) {

            switchPlayer();
            board[row][col] = currPlayer;

            if (connectedFour(row, col)) {
                gameOn = false;
            }

            return row;

        }
        return -1;
    }

    /**
     * Switches the current player to the next player.
     */
    private void switchPlayer() {
        currPlayer = nextPlayer;
        nextPlayer = (currPlayer == PLAYER1) ? PLAYER2 : PLAYER1;
    }

    /**
     * Gets the next available row in a column where a disk can be dropped.
     *
     * @param col The column to check.
     * @return The next available row, or -1 if the column is full.
     */
    private int getNextAvailableRow(int col) {
        if (nextAvailRow[col] >= 0) {
            return nextAvailRow[col]--;
        }
        return -1;
    }

    /**
     * Checks if dropping a disk at the specified position results in a connected four.
     *
     * @param row The row where the disk is dropped.
     * @param col The column where the disk is dropped.
     * @return True if there's a connected four, false otherwise.
     */
    public boolean connectedFour(int row, int col) {
        return hasVerticalMatch(row, col)
                || hasHorizontalMatch(row, col)
                ||   hasLeftDiagonalMatch(row, col)
                ||  hasRightDiagonalMatch(row, col);

    }

    /**
     * Checks if the specified position close a vertical connect four.
     */
    private boolean hasVerticalMatch(int row, int col) {
        if (row + 3 > ROWS - 1) {
            return false;
        }

        int count = 0;
        int rowEnd = row + (SEQ_LEN - 1);

        for (int r = row; r <= rowEnd; r++) {
            count = (board[r][col] != currPlayer) ? 0 : count + 1;
            if (count == SEQ_LEN) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the specified position close a horizontal connect four.
     */
    private boolean hasHorizontalMatch(int row, int col) {
        int count = 0;
        int colStart = Math.max(col - (SEQ_LEN - 1), 0);
        int colEnd = Math.min(col + (SEQ_LEN - 1), COLS - 1);

        for (int c = colStart; c <= colEnd; c++) {
            count = (board[row][c] != currPlayer) ? 0 : count + 1;
            if (count == SEQ_LEN) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the specified position close a left diagonal connect four.
     */
    private boolean hasLeftDiagonalMatch(int row, int col) {
        return hasDiagonalMatch(row, col, 1);
    }

    /**
     * Checks if the specified position close a right diagonal connect four.
     */
    private boolean hasRightDiagonalMatch(int row, int col) {
        return hasDiagonalMatch(row, col, -1);
    }

    /**
     * Checks if the specified position close a right/left diagonal connect four,
     * according to the given direction
     * @param direction 1: check for right diagonal (from bottom-right to top-left)
     *                  -1: check for left diagonal (bottom-left to top-right)
     */
    private boolean hasDiagonalMatch(int row, int col, int direction) {
        int count = 0;

        for (int i = -(SEQ_LEN - 1); i <= SEQ_LEN - 1; i++) {
            int r = row + i;
            int c = col + (i * direction);

            if (r >= 0 && r < ROWS && c >= 0 && c < COLS) { // inside bounds
                count = (board[r][c] != currPlayer) ? 0 : count + 1;
                if (count == SEQ_LEN) {
                    return true;
                }
            }
        }
        return false;
    }

}
