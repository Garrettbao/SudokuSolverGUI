package utils;

/**
 * Utility class for parsing user input into a Sudoku board.
 */
public class Parser {
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
}
