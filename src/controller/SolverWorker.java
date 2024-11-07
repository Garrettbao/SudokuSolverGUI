package controller;

import model.SudokuBoard;
import view.SudokuSolverGUI;

import javax.swing.*;

/**
 * SolverWorker class extends SwingWorker to perform solving in background.
 */
public class SolverWorker extends SwingWorker<Boolean, Void> {
    private SudokuBoard board;
    private SudokuSolver solver;
    private SudokuSolverGUI gui;
    private JButton stopButton;

    public SolverWorker(SudokuBoard board, int sleepTime, SudokuSolverGUI gui, JButton stopButton) {
        this.board = board;
        this.gui = gui;
        this.stopButton = stopButton;
        this.solver = new SudokuSolver(gui, sleepTime);
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        // Assign this instance to gui's currentSolverWorker
        gui.setCurrentSolverWorker(this);
        // Enable the Stop button
        SwingUtilities.invokeLater(() -> stopButton.setEnabled(true));
        return solver.solveSudoku(board);
    }

    @Override
    protected void done() {
        try {
            if (isCancelled()) {
                JOptionPane.showMessageDialog(gui,
                        "Sudoku solving was stopped.",
                        "Stopped",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                boolean solved = get();
                if (solved) {
                    JOptionPane.showMessageDialog(gui,
                            "Sudoku Solved Successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(gui,
                            "No solution exists for the given Sudoku board.",
                            "Unsolvable",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(gui,
                    "An error occurred while solving the Sudoku.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            // Disable the Stop button after completion
            SwingUtilities.invokeLater(() -> stopButton.setEnabled(false));
            // Clear the reference to currentSolverWorker
            gui.setCurrentSolverWorker(null);
        }
    }
}
