package view;

import javax.swing.*;

/**
 * The main class to launch the Sudoku Solver GUI.
 */
public class Main {
    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            SudokuSolverGUI gui = new SudokuSolverGUI();
            gui.setVisible(true);
        });
    }
}
