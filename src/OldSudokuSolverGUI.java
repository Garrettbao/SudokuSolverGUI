import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 *
 *
 *
 *
 *
 *
 * ##########################################
 * ###Legacy file - reference only!!!!!######
 * ##########################################
 *
 *
 *
 *
 *
 *
 * SudokuSolverGUI class integrates the Sudoku solver with a Java Swing GUI,
 * providing real-time visualization of the solving process.
 */
public class OldSudokuSolverGUI extends JFrame {

    private JTextArea arrayInputArea;
    private JTextField[][] gridInputFields;
    private JTextField[][] solvedGridFields;
    private JTabbedPane tabbedPane; // To allow tab switching
    private boolean[][] isInitialCell = new boolean[9][9]; // Tracks initial cells

    // Separate sleep time fields for each input method
    private JTextField arraySleepTimeField; // Sleep time for Array Input
    private JTextField gridSleepTimeField;  // Sleep time for Grid Input

    private SolverWorker currentSolverWorker; // To keep track of the active SolverWorker
    private JButton stopButton; // To reference the Stop button in the solved panel

    /**
     * Constructor to set up the GUI components.
     */
    public OldSudokuSolverGUI() {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null); // Center the window

        // Create Tabbed Pane for input methods
        tabbedPane = new JTabbedPane();

        // Panel for Array Input
        JPanel arrayInputPanel = createArrayInputPanel();
        tabbedPane.addTab("Paste Array", arrayInputPanel);

        // Panel for Manual Grid Input
        JPanel gridInputPanel = createGridInputPanel();
        tabbedPane.addTab("Manual Input", gridInputPanel);

        // Panel for Solved Sudoku
        JPanel solvedPanel = createSolvedPanel();
        tabbedPane.addTab("Solved Sudoku", solvedPanel);

        add(tabbedPane);
    }

    /**
     * Creates the panel for array input method.
     *
     * @return JPanel containing array input components.
     */
    private JPanel createArrayInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel instructionLabel = new JLabel("<html>Enter the Sudoku board as a 2D array below.<br>"
                + "Use '.' for empty cells.<br>"
                + "Numbers do not need to be enclosed in quotes.<br>"
                + "Ensure the board has exactly 9 rows and 9 columns.<br>"
                + "Example:<br>"
                + "[[5,3,.,.,7,.,.,.,.],<br>"
                + "[6,.,.,1,9,5,.,.,.],<br>"
                + "[.,9,8,.,.,.,.,6,.],<br>"
                + "[8,.,.,.,6,.,.,.,3],<br>"
                + "[4,.,.,8,.,3,.,.,1],<br>"
                + "[7,.,.,.,2,.,.,.,6],<br>"
                + "[.,6,.,.,.,.,2,8,.],<br>"
                + "[.,.,.,4,1,9,.,.,5],<br>"
                + "[.,.,.,.,8,.,.,7,9]]</html>");
        panel.add(instructionLabel, BorderLayout.NORTH);

        arrayInputArea = new JTextArea();
        arrayInputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(arrayInputArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton solveButton = new JButton("Solve With Animation");
        JButton solveInstantlyButton = new JButton("Solve Instantly");
        JButton clearButton = new JButton("Clear");

        // Initialize and add Sleep Time Panel specific to Array Input
        JPanel sleepTimePanel = createSleepTimePanel(true); // true indicates Array Input

        buttonPanel.add(solveButton);
        buttonPanel.add(solveInstantlyButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(sleepTimePanel);

        // Action Listener for "Solve" Button
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveFromArrayInput();
            }
        });

        // Action Listener for "Solve Instantly" Button
        solveInstantlyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveFromArrayInputInstantly();
            }
        });

        // Action Listener for "Clear" Button
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                arrayInputArea.setText("");
                clearSolvedGrid();
            }
        });

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the panel for manual grid input method.
     *
     * @return JPanel containing manual grid input components.
     */
    private JPanel createGridInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel instructionLabel = new JLabel("Enter the Sudoku board manually below. Use '.' or leave blank for empty cells.");
        panel.add(instructionLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        gridInputFields = new JTextField[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Monospaced", Font.BOLD, 20));

                // Alternate background colors for 3x3 sub-boxes
                if (((i / 3) + (j / 3)) % 2 == 0) {
                    cell.setBackground(new Color(220, 220, 220));
                } else {
                    cell.setBackground(new Color(255, 255, 255));
                }

                gridInputFields[i][j] = cell;
                gridPanel.add(cell);
            }
        }

        panel.add(gridPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton solveButton = new JButton("Solve With Animation");
        JButton solveInstantlyButton = new JButton("Solve Instantly"); // Added
        JButton clearButton = new JButton("Clear");

        // Initialize and add Sleep Time Panel specific to Grid Input
        JPanel sleepTimePanel = createSleepTimePanel(false); // false indicates Grid Input

        buttonPanel.add(solveButton);
        buttonPanel.add(solveInstantlyButton); // Added
        buttonPanel.add(clearButton);
        buttonPanel.add(sleepTimePanel); // Add Sleep Time input

        // Action Listener for "Solve" Button
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveFromGridInput();
            }
        });

        // Action Listener for "Solve Instantly" Button
        solveInstantlyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveFromGridInputInstantly();
            }
        });

        // Action Listener for "Clear" Button
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearGridInput();
                clearSolvedGrid();
            }
        });

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the panel to display the solved Sudoku.
     *
     * @return JPanel containing the solved Sudoku grid.
     */
    private JPanel createSolvedPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel solvedLabel = new JLabel("Solved Sudoku Board:");
        solvedLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(solvedLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        solvedGridFields = new JTextField[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Monospaced", Font.BOLD, 20));
                cell.setEditable(false);
                cell.setBackground(Color.WHITE);

                // Alternate background colors for 3x3 sub-boxes
                if (((i / 3) + (j / 3)) % 2 == 0) {
                    cell.setBackground(new Color(240, 240, 240));
                } else {
                    cell.setBackground(new Color(255, 255, 255));
                }

                solvedGridFields[i][j] = cell;
                gridPanel.add(cell);
            }
        }

        panel.add(gridPanel, BorderLayout.CENTER);

        // Add Stop Button
        stopButton = new JButton("Stop");

        stopButton.setEnabled(false); // Initially disabled
        JPanel stopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stopPanel.add(stopButton);
        panel.add(stopPanel, BorderLayout.SOUTH);

        // Action Listener for Stop Button
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopSolving();
            }
        });

        return panel;
    }

    /**
     * Creates the sleep time input panel.
     *
     * @param isArrayInput Indicates whether the panel is for Array Input (true) or Grid Input (false).
     * @return JPanel containing sleep time components.
     */
    private JPanel createSleepTimePanel(boolean isArrayInput) {
        JPanel sleepTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel sleepTimeLabel = new JLabel("Sleep Time (ms):");
        JTextField sleepTimeField = new JTextField("5", 5);

        // Assign to the appropriate sleep time field
        if (isArrayInput) {
            arraySleepTimeField = sleepTimeField;
        } else {
            gridSleepTimeField = sleepTimeField;
        }

        sleepTimePanel.add(sleepTimeLabel);
        sleepTimePanel.add(sleepTimeField);
        return sleepTimePanel;
    }

    /**
     * Solves the Sudoku puzzle based on array input.
     */
    private void solveFromArrayInput() {
        String input = arrayInputArea.getText();
        try {
            char[][] board = parseBoard(input);
            if (!isInitialBoardValid(board)) {
                JOptionPane.showMessageDialog(this,
                        "The initial Sudoku board is invalid. Please correct the duplicates and try again.",
                        "Invalid Board",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get sleep time specific to Array Input
            int sleepTime = getSleepTime(true);

            // Display initial board in "Solved Sudoku" tab
            displayInitialBoard(board);

            // Switch to "Solved Sudoku" tab
            tabbedPane.setSelectedIndex(2);

            // Start solving in background
            new SolverWorker(board, sleepTime).execute();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Solves the Sudoku puzzle based on array input instantly (no sleep time).
     */
    private void solveFromArrayInputInstantly() {
        String input = arrayInputArea.getText();
        try {
            char[][] board = parseBoard(input);
            if (!isInitialBoardValid(board)) {
                JOptionPane.showMessageDialog(this,
                        "The initial Sudoku board is invalid. Please correct the duplicates and try again.",
                        "Invalid Board",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Set sleep time to 0 for instant solving
            int sleepTime = 0;

            // Display initial board in "Solved Sudoku" tab
            displayInitialBoard(board);

            // Switch to "Solved Sudoku" tab
            tabbedPane.setSelectedIndex(2);

            // Start solving in background
            new SolverWorker(board, sleepTime).execute();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Solves the Sudoku puzzle based on manual grid input.
     */
    private void solveFromGridInput() {
        try {
            char[][] board = getBoardFromGridInput();
            if (!isInitialBoardValid(board)) {
                JOptionPane.showMessageDialog(this,
                        "The initial Sudoku board is invalid. Please correct the duplicates and try again.",
                        "Invalid Board",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get sleep time specific to Grid Input
            int sleepTime = getSleepTime(false);

            // Display initial board in "Solved Sudoku" tab
            displayInitialBoard(board);

            // Switch to "Solved Sudoku" tab
            tabbedPane.setSelectedIndex(2);

            // Start solving in background
            new SolverWorker(board, sleepTime).execute();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Solves the Sudoku puzzle based on manual grid input instantly (no sleep time).
     */
    private void solveFromGridInputInstantly() {
        try {
            char[][] board = getBoardFromGridInput();
            if (!isInitialBoardValid(board)) {
                JOptionPane.showMessageDialog(this,
                        "The initial Sudoku board is invalid. Please correct the duplicates and try again.",
                        "Invalid Board",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Set sleep time to 0 for instant solving
            int sleepTime = 0;

            // Display initial board in "Solved Sudoku" tab
            displayInitialBoard(board);

            // Switch to "Solved Sudoku" tab
            tabbedPane.setSelectedIndex(2);

            // Start solving in background
            new SolverWorker(board, sleepTime).execute();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Parses the board input from the 9x9 grid.
     *
     * @return 2D char array representing the Sudoku board.
     * @throws IllegalArgumentException If the input format is invalid.
     */
    private char[][] getBoardFromGridInput() throws IllegalArgumentException {
        char[][] board = new char[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = gridInputFields[i][j].getText().trim();
                if (text.isEmpty() || text.equals(".")) {
                    board[i][j] = '.';
                } else if (text.length() == 1 && Character.isDigit(text.charAt(0)) && text.charAt(0) >= '1' && text.charAt(0) <= '9') {
                    board[i][j] = text.charAt(0);
                } else {
                    throw new IllegalArgumentException("Invalid input at row " + (i + 1) + ", column " + (j + 1) +
                            ". Please enter a digit from '1' to '9' or '.' for empty cells.");
                }
            }
        }
        return board;
    }

    /**
     * Clears the manual grid input fields.
     */
    private void clearGridInput() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                gridInputFields[i][j].setText("");
            }
        }
    }

    /**
     * Clears the solved Sudoku grid and resets styles.
     */
    private void clearSolvedGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                solvedGridFields[i][j].setText("");
                solvedGridFields[i][j].setForeground(Color.BLACK);
                solvedGridFields[i][j].setBorder(new LineBorder(Color.GRAY));
                isInitialCell[i][j] = false;
            }
        }
    }

    /**
     * Displays the initial Sudoku board in the "Solved Sudoku" grid.
     *
     * @param board The initial Sudoku board.
     */
    private void displayInitialBoard(char[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    solvedGridFields[i][j].setText(String.valueOf(board[i][j]));
                    isInitialCell[i][j] = true;
                    solvedGridFields[i][j].setForeground(Color.BLACK);
                    solvedGridFields[i][j].setBorder(new LineBorder(Color.BLUE, 2));
                } else {
                    solvedGridFields[i][j].setText("");
                    isInitialCell[i][j] = false;
                    solvedGridFields[i][j].setForeground(Color.BLACK);
                    solvedGridFields[i][j].setBorder(new LineBorder(Color.GRAY));
                }
            }
        }
    }

    /**
     * Parses user input from a 2D array string into a 2D char array.
     *
     * @param input The input string representing the Sudoku board.
     * @return A 2D char array representing the Sudoku board.
     * @throws IllegalArgumentException If the input format is invalid.
     */
    public static char[][] parseBoard(String input) throws IllegalArgumentException {
        // Remove all whitespace characters
        input = input.replaceAll("\\s", "");

        // Validate the input format: it should start with "[[" and end with "]]"
        if (!input.startsWith("[[") || !input.endsWith("]]")) {
            throw new IllegalArgumentException("Invalid input format. Please ensure the board is enclosed in double square brackets.");
        }

        // Remove the outer brackets
        String inner = input.substring(2, input.length() - 2);

        // Split the input into rows based on "],["
        String[] rows = inner.split("\\],\\[");

        if (rows.length != 9) {
            throw new IllegalArgumentException("Invalid number of rows. A Sudoku board must have exactly 9 rows.");
        }

        char[][] board = new char[9][9];

        for (int i = 0; i < 9; i++) {
            String row = rows[i];
            String[] cells = row.split(",");

            if (cells.length != 9) {
                throw new IllegalArgumentException("Invalid number of columns in row " + (i + 1) + ". Each row must have exactly 9 cells.");
            }

            for (int j = 0; j < 9; j++) {
                String cell = cells[j];

                // Remove both single and double quotes
                cell = cell.replaceAll("[\"']", "");

                if (cell.equals(".") || cell.equals("")) {
                    board[i][j] = '.';
                } else if (cell.length() == 1 && Character.isDigit(cell.charAt(0)) && cell.charAt(0) >= '1' && cell.charAt(0) <= '9') {
                    board[i][j] = cell.charAt(0);
                } else {
                    throw new IllegalArgumentException("Invalid cell value at row " + (i + 1) + ", column " + (j + 1) +
                            ". Each cell must be a digit from '1' to '9' or a '.'");
                }
            }
        }

        return board;
    }

    /**
     * Validates the initial Sudoku board for any duplicate numbers.
     *
     * @param board The 9x9 Sudoku board.
     * @return True if the board is valid, otherwise false.
     */
    public static boolean isInitialBoardValid(char[][] board) {
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

    /**
     * Retrieves the sleep time entered by the user.
     *
     * @param isArrayInput Indicates whether to retrieve sleep time for Array Input (true) or Grid Input (false).
     * @return The sleep time in milliseconds.
     */
    private int getSleepTime(boolean isArrayInput) {
        try {
            int sleep;
            if (isArrayInput) {
                sleep = Integer.parseInt(arraySleepTimeField.getText().trim());
            } else {
                sleep = Integer.parseInt(gridSleepTimeField.getText().trim());
            }

            if (sleep < 0 || sleep > 100) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a sleep time between 0 and 100 milliseconds.",
                        "Invalid Sleep Time",
                        JOptionPane.ERROR_MESSAGE);
                return 5; // Default value
            }
            return sleep;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid sleep time entered. Please enter an integer between 0 and 100.",
                    "Invalid Sleep Time",
                    JOptionPane.ERROR_MESSAGE);
            return 5; // Default value
        }
    }

    /**
     * Updates the solved Sudoku grid in real-time.
     *
     * @param row      The row index.
     * @param col      The column index.
     * @param value    The character to display.
     * @param isPlaced True if the number is placed, false if removed (backtracked).
     */
    private void updateSolvedCell(int row, int col, char value, boolean isPlaced) {
        if (isInitialCell[row][col]) {
            return; // Do not update initial cells
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (value == '.') {
                    solvedGridFields[row][col].setText("");
                } else {
                    solvedGridFields[row][col].setText(String.valueOf(value));
                }

                if (isPlaced) {
                    solvedGridFields[row][col].setForeground(Color.BLUE); // Placed numbers in blue
                    solvedGridFields[row][col].setBorder(new LineBorder(Color.GREEN, 2)); // Highlight placed numbers
                } else {
                    solvedGridFields[row][col].setForeground(Color.RED); // Removed numbers in red
                    solvedGridFields[row][col].setBorder(new LineBorder(Color.RED, 2)); // Highlight removals
                }
            }
        });
    }

    /**
     * Stops the ongoing solving process and returns to the input tab.
     */
    private void stopSolving() {
        if (currentSolverWorker != null && !currentSolverWorker.isDone()) {
            currentSolverWorker.cancel(true); // Cancel the SolverWorker

            // Switch back to the input tab (determine which tab was active)
            // Here, we'll switch to "Paste Array" tab (index 0)
            tabbedPane.setSelectedIndex(0);

            // Clear the solved grid
            clearSolvedGrid();
        }
    }

    /**
     * Inner SudokuSolver class containing the solving logic with real-time updates.
     */
    public class SudokuSolver {
        private OldSudokuSolverGUI gui; // Reference to the GUI for updates
        private int sleepTime;

        public SudokuSolver(OldSudokuSolverGUI gui, int sleepTime) {
            this.gui = gui;
            this.sleepTime = sleepTime;
        }

        /**
         * Attempts to solve the Sudoku puzzle.
         *
         * @param board The 9x9 Sudoku board.
         * @return True if the puzzle is solved successfully, otherwise false.
         */
        public boolean solveSudoku(char[][] board) {
            return solve(board, 0, 0);
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
            if (currentSolverWorker != null && currentSolverWorker.isCancelled()) {
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
                if (currentSolverWorker != null && currentSolverWorker.isCancelled()) {
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

    /**
     * SolverWorker class extends SwingWorker to perform solving in background.
     */
    private class SolverWorker extends SwingWorker<Boolean, Void> {
        private char[][] board;
        private SudokuSolver solver;

        public SolverWorker(char[][] board, int sleepTime) {
            this.board = board;
            this.solver = new SudokuSolver(OldSudokuSolverGUI.this, sleepTime);
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            // Assign this instance to currentSolverWorker
            currentSolverWorker = this;
            // Enable the Stop button
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    stopButton.setEnabled(true);
                }
            });
            return solver.solveSudoku(board);
        }

        @Override
        protected void done() {
            try {
                if (isCancelled()) {
                    JOptionPane.showMessageDialog(OldSudokuSolverGUI.this,
                            "Sudoku solving was stopped.",
                            "Stopped",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    boolean solved = get();
                    if (solved) {
                        JOptionPane.showMessageDialog(OldSudokuSolverGUI.this,
                                "Sudoku Solved Successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(OldSudokuSolverGUI.this,
                                "No solution exists for the given Sudoku board.",
                                "Unsolvable",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(OldSudokuSolverGUI.this,
                        "An error occurred while solving the Sudoku.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                // Disable the Stop button after completion
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        stopButton.setEnabled(false);
                    }
                });
                currentSolverWorker = null;
            }
        }
    }

    /**
     * The main method to launch the GUI.
     *
     * @param args Command-line arguments.
     */
//    public static void main(String[] args) {
//        // Ensure the GUI is created on the Event Dispatch Thread
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                OldSudokuSolverGUI gui = new OldSudokuSolverGUI();
//                gui.setVisible(true);
//            }
//        });
//    }
}
