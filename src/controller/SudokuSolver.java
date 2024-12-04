package controller;

import model.SudokuBoard;
import view.SudokuSolverGUI;

/**
 * SudokuSolver class contains the backtracking algorithm to solve the Sudoku puzzle.
 */
public class SudokuSolver {
    // Reference to the GUI to update the visual representation of the board
    private SudokuSolverGUI gui;

    // Sleep time in milliseconds to control the speed of the visualization
    private int sleepTime;

    /**
     * Constructor for SudokuSolver.
     *
     * @param gui       Reference to the SudokuSolverGUI for updating the UI during solving.
     * @param sleepTime Time in milliseconds to pause between steps for visualization purposes.
     */
    public SudokuSolver(SudokuSolverGUI gui, int sleepTime) {
        this.gui = gui;
        this.sleepTime = sleepTime;
    }

    /**
     * Attempts to solve the Sudoku puzzle using the backtracking algorithm.
     *
     * @param sudokuBoard The SudokuBoard object containing the initial puzzle configuration.
     * @return True if the puzzle is solved successfully, otherwise false.
     */
    public boolean solveSudoku(SudokuBoard sudokuBoard) {
        // Initiates the solving process starting from the first cell (0,0)
        return solve(sudokuBoard.getBoard(), 0, 0);
    }

    /**
     * Backtracking solver method that fills the Sudoku board recursively.
     * It updates the GUI in real-time and supports cancellation.
     *
     * @param board The 9x9 Sudoku board represented as a 2D char array.
     * @param row   Current row index being processed.
     * @param col   Current column index being processed.
     * @return True if the puzzle is solved successfully, otherwise false.
     */
    private boolean solve(char[][] board, int row, int col) {
        // Check if the solving process has been cancelled by the user
        if (gui.getCurrentSolverWorker() != null && gui.getCurrentSolverWorker().isCancelled()) {
            return false; // Terminate the solving process
        }

        // Base case: If row index reaches 9, the entire board is filled correctly
        if (row == 9) {
            return true; // Puzzle solved successfully
        }

        // If the current cell is already filled, move to the next cell
        if (board[row][col] != '.') {
            // Determine the next cell's position
            if (col == 8) { // If at the end of the current row
                // Move to the first column of the next row
                return solve(board, row + 1, 0);
            } else { // If not at the end of the row
                // Move to the next column in the same row
                return solve(board, row, col + 1);
            }
        }

        // Iterate through numbers 1 to 9 to try placing them in the current empty cell
        for (char c = '1'; c <= '9'; c++) {

            // Check again if the solving process has been cancelled
            if (gui.getCurrentSolverWorker() != null && gui.getCurrentSolverWorker().isCancelled()) {
                return false; // Terminate the solving process
            }

            // Check if placing number 'c' in cell (row, col) is valid according to Sudoku rules
            if (isValid(board, row, col, c)) {
                board[row][col] = c; // Place the number on the board

                // Update the GUI to reflect the placed number (e.g., highlight in green)
                gui.updateSolvedCell(row, col, c, true);

                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime); // Pause for visualization
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }

                // Move to the next cell after placing the number
                if (col == 8) { // If at the end of the current row
                    if (solve(board, row + 1, 0)) { // Move to the first column of the next row
                        return true; // If successful, propagate the success upwards
                    }
                } else { // If not at the end of the row
                    if (solve(board, row, col + 1)) { // Move to the next column in the same row
                        return true;
                    }
                }

                // Backtracking
                // If placing 'c' did not lead to a solution, backtrack by resetting the cell
                board[row][col] = '.'; // Remove the number from the board

                // Update the GUI to reflect the removal (e.g., highlight in red)
                gui.updateSolvedCell(row, col, '.', false);

                // Introduce a delay to visualize the backtracking process
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime); // Pause for visualization
                    } catch (InterruptedException e) {
                        // If the thread is interrupted, set the interrupt flag and terminate
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }

        // If no valid number could be placed in the current cell, trigger backtracking
        return false;
    }

    /**
     * Checks if placing a specific number in a given cell is valid according to Sudoku rules.
     *
     * @param board The 9x9 Sudoku board represented as a 2D char array.
     * @param row   The row index of the cell.
     * @param col   The column index of the cell.
     * @param c     The character (number) to be placed in the cell.
     * @return True if placing the number is valid, otherwise false.
     */
    private boolean isValid(char[][] board, int row, int col, char c) {
        // Calculate the starting indices of the 3x3 subgrid containing the cell
        int boxRow = 3 * (row / 3);
        int boxCol = 3 * (col / 3);

        // Iterate through the row, column, and subgrid to check for duplicates
        for (int i = 0; i < 9; i++) {
            // Check if the number 'c' already exists in the current row
            if (board[row][i] == c) return false;

            // Check if the number 'c' already exists in the current column
            if (board[i][col] == c) return false;

            // Check if the number 'c' already exists in the current 3x3 subgrid
            if (board[boxRow + i / 3][boxCol + i % 3] == c) return false;
        }

        // If no duplicates are found, the placement is valid
        return true;
    }
}
