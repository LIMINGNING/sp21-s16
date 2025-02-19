package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        changed = true;

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /* 给定一列，完成一列的up操作 */
    public void mergeTile(Board b, int column) {
        int sum = 0;
        for (int row = 3; row-1 >= 0; row--) {
            if (b.tile(column,row) == null) {
                return;
            }
            else if (b.tile(column,row) != null && b.tile(column,row-1) != null) {
                mergeTileHelper(b,column,row);
                moveTileUpAsFarAsPossible(b,column,3);
            }
        }
        return;
    }

    /* 传入一个tile的column和row，将其下侧的tile合并到上侧 */
    public void mergeTileHelper(Board b, int column, int row) {
        if (b.tile(column,row).value() == b.tile(column,row-1).value()) {
            b.move(column,row,b.tile(column,row-1));
            score += b.tile(column,row).value();
        }
        return;
    }

    /* 将全部的方格尽可能向上移 */
    public void moveAllUp(Board b) {
        for (int column = 0; column <= 3; column++) {
            moveTileUpAsFarAsPossible(b,column,3);
        }
    }

    /* 给定一列，将这一列的方格尽可能向上移，如果board改变，返回true */
    public boolean moveTileUpOneColumnAFAP(Board b, int column) {
        boolean[] checkChanged = new boolean[4];
        for (int row = 3; row-1 >= 0; row--) {
            if (b.tile(column,row) == null) {
                int nextNonZero = getNextNonZeroRow(b,column,row);
                if (nextNonZero == -1 && row == 3) {
                    return false;
                }
                else if (nextNonZero == -1) {
                    continue;
                }
                else {
                    b.move(column,row,b.tile(column,nextNonZero));
                    checkChanged[row] = true;
                }
            }
            else {
                continue;
            }
        }
        return checkChanged[1] || checkChanged[2] || checkChanged[3];
    }

    /* 将给定列的所有方格尽可能向上移（传入参数row恒为3） */
    public void moveTileUpAsFarAsPossible(Board b, int column, int row) {
        if (b.tile(column,row) == null) {
            int nextNonZero = getNextNonZeroRow(b,column,row);
            if (nextNonZero == -1)
            {
                return;
            }
            else {
                b.move(column,row,b.tile(column,nextNonZero));
                moveTileUpAsFarAsPossible(b,column,getNextRow(row));
            }
        }
        else {
            if (getNextRow(row) != -1) {
                moveTileUpAsFarAsPossible(b,column,getNextRow(row));
            }
            else {
                return;
            }
        }
    }

    /* 获得下一个非0元素的位置，如果越界，则返回-1（这一列均为空） */
    public int getNextNonZeroRow(Board b,int column, int row) {
        int nextRow = getNextRow(row);
        if (nextRow == -1){
            return -1;
        }
        if (b.tile(column,nextRow) == null) {
            return getNextNonZeroRow(b,column,nextRow);
        }
        else {
            return nextRow;
        }
    }

    /* 如果下一个row比0小（越界），则返回-1，否则返回下一个row（即row-1） */
    public int getNextRow(int row) {
        if (row - 1 >= 0) {
            return row - 1;
        }
        else {
            return -1;
        }
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        for (int c = 0; c < b.size(); c++) {
            for (int r = 0; r < b.size(); r++) {
                if (b.tile(c,r) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        for (int c = 0; c < b.size(); c++) {
            for (int r = 0; r < b.size(); r++) {
                if (b.tile(c,r) == null) {
                    continue;
                }
                else if (b.tile(c,r).value() == MAX_PIECE) {
                    return true;
                }
                else {
                    continue;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        /**
         * We only need to check the top and right of the tile.
         * Pay attention to whether they are empty (null).
         */
        for (int c = 0; c < b.size(); c++) {
            for (int r = 0; r < b.size(); r++) {
                if (b.tile(c,r) == null) {
                    return true;
                }

                if (r < 3 && b.tile(c,r).value() == b.tile(c,r+1).value()) {
                    return true;
                }

                if (c < 3 && b.tile(c,r).value() == b.tile(c+1,r).value()) {
                    return true;
                }
            }
        }

        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
