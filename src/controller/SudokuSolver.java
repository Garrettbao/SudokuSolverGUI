package controller;

import model.SudokuBoard;
import view.SudokuSolverGUI;

/**
 * SudokuSolver class contains the backtracking algorithm to solve the Sudoku puzzle.
 */
public class SudokuSolver {
    private SudokuSolverGUI gui; // Reference to the GUI for updates
    private int sleepTime;

    public SudokuSolver(SudokuSolverGUI gui, int sleepTime) {
        this.gui = gui;
        this.sleepTime = sleepTime;
    }

    /**
     * Attempts to solve the Sudoku puzzle.
     *
     * @param sudokuBoard The SudokuBoard object containing the initial puzzle.
     * @return True if the puzzle is solved successfully, otherwise false.
     */
    public boolean solveSudoku(SudokuBoard sudokuBoard) {
        return solve(sudokuBoard.getBoard(), 0, 0);
    }

    /**
     * Backtracking solver method with real-time updates and cancellation support.
     *
     * @param board The 9x9 Sudoku board.
     * @param row   Current row index.
     * @param col   Current column index.
     * @return True if the puzzle is solved successfully, otherwise false.
     */
    private boolean solve(char[][] board, int row, int col) {
        // Check if the solving has been cancelled
        if (gui.getCurrentSolverWorker() != null && gui.getCurrentSolverWorker().isCancelled()) {
            return false; // Terminate solving
        }

        if (row == 9) {
            return true; // Solved
        }

        if (board[row][col] != '.') {
            // Move to next cell
            if (col == 8) {
                return solve(board, row + 1, 0);
            } else {
                return solve(board, row, col + 1);
            }
        }

        for (char c = '1'; c <= '9'; c++) {

            // Check if the solving has been cancelled
            if (gui.getCurrentSolverWorker() != null && gui.getCurrentSolverWorker().isCancelled()) {
                return false; // Terminate solving
            }

            if (isValid(board, row, col, c)) {
                board[row][col] = c;
                gui.updateSolvedCell(row, col, c, true); // Update GUI: number placed

                // Simulate delay for visualization
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        // If interrupted, terminate the solving process
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }

                // Move to next cell
                if (col == 8) {
                    if (solve(board, row + 1, 0)) {
                        return true;
                    }
                } else {
                    if (solve(board, row, col + 1)) {
                        return true;
                    }
                }

                // Backtrack
                board[row][col] = '.';
                gui.updateSolvedCell(row, col, '.', false); // Update GUI: number removed

                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        // If interrupted, terminate the solving process
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }

        return false; // Trigger backtracking
    }

    /**
     * Checks if placing a number at a specific position is valid.
     *
     * @param board The 9x9 Sudoku board.
     * @param row   The row index.
     * @param col   The column index.
     * @param c     The character to place.
     * @return True if valid, otherwise false.
     */
    private boolean isValid(char[][] board, int row, int col, char c) {
        int boxRow = 3 * (row / 3);
        int boxCol = 3 * (col / 3);

        for (int i = 0; i < 9; i++) {
            // Check row
            if (board[row][i] == c) return false;
            // Check column
            if (board[i][col] == c) return false;
            // Check 3x3 sub-box
            if (board[boxRow + i / 3][boxCol + i % 3] == c) return false;
        }

        return true;
    }
}
