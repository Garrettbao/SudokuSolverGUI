package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the Sudoku board and contains methods for validation.
 */
public class SudokuBoard {
    private char[][] board;

    public SudokuBoard(char[][] board) {
        this.board = board;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setCell(int row, int col, char value) {
        board[row][col] = value;
    }

    /**
     * Validates the initial Sudoku board for any duplicate numbers.
     *
     * @return True if the board is valid, otherwise false.
     */
    public boolean isInitialBoardValid() {
        // Check each row
        for (int i = 0; i < 9; i++) {
            Set<Character> seen = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                char current = board[i][j];
                if (current != '.') {
                    if (seen.contains(current)) {
                        System.out.println("Duplicate number '" + current + "' found in row " + (i + 1));
                        return false;
                    }
                    seen.add(current);
                }
            }
        }

        // Check each column
        for (int j = 0; j < 9; j++) {
            Set<Character> seen = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                char current = board[i][j];
                if (current != '.') {
                    if (seen.contains(current)) {
                        System.out.println("Duplicate number '" + current + "' found in column " + (j + 1));
                        return false;
                    }
                    seen.add(current);
                }
            }
        }

        // Check each 3x3 sub-box
        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                Set<Character> seen = new HashSet<>();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        char current = board[boxRow + i][boxCol + j];
                        if (current != '.') {
                            if (seen.contains(current)) {
                                System.out.println("Duplicate number '" + current + "' found in 3x3 sub-box starting at row "
                                        + (boxRow + 1) + ", column " + (boxCol + 1));
                                return false;
                            }
                            seen.add(current);
                        }
                    }
                }
            }
        }

        return true;
    }
}
