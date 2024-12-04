# SudokuSolverGUI

A graphical user interface (GUI) application for solving Sudoku puzzles using a backtracking algorithm. Our project allows users to input a Sudoku puzzle and get an instant solution, making it a great tool for both Sudoku enthusiasts and developers interested in understanding backtracking-based puzzle solvers.

## Features

- **Easy-to-use GUI** for inputting puzzles and displaying results
- **Backtracking algorithm** to solve any valid 9x9 Sudoku puzzle
- **Real-time feedback** on the solution process

## Requirements

- **Java**

## Running the Sudoku Solver

Start the program by running **Main.java** under **src/view/**

## Usage

### 1. Input the Puzzle

The application provides two methods to input your Sudoku puzzle:

#### a. **Paste Array**

- **Description**: Enter the Sudoku board as a 2D array.
- **How to Use**:
    - Navigate to the **"Paste Array"** tab.
    - Enter the numbers for the Sudoku puzzle in the text area. Use `.` for empty cells.
    - Ensure the board has exactly 9 rows and 9 columns.
- **Example Inputs**:
    - Example input arrays can be found in the txt file in the zip folder and in the project folder.
    - **Example**:
      ```
      [[5,3,.,.,7,.,.,.,.],
      [6,.,.,1,9,5,.,.,.],
      [.,9,8,.,.,.,.,6,.],
      [8,.,.,.,6,.,.,.,3],
      [4,.,.,8,.,3,.,.,1],
      [7,.,.,.,2,.,.,.,6],
      [.,6,.,.,.,.,2,8,.],
      [.,.,.,4,1,9,.,.,5],
      [.,.,.,.,8,.,.,7,9]]
      ```

#### b. **Manual Input**

- **Description**: Enter the Sudoku board manually using the grid.
- **How to Use**:
    - Navigate to the **"Manual Input"** tab.
    - Click on each cell and enter a number between `1` and `9`. Leave cells empty or enter `.` for unknown numbers.
    - The grid alternates background colors for better visibility.

### 2. Solve the Puzzle

Once you have input the puzzle using either method, you have two options to solve it:

- **Sleep time(ms)**:
    - Enter an integer from 1 to 100 in the **"Sleep time input box"**.
    - The solver will set the sleep time in ms to slow down the solving process.

- **Solve With Animation**:
    - Click the **"Solve With Animation"** button.
    - The solver will start filling in the cells with real-time visual feedback, highlighting placed numbers in green and backtracked numbers in red.

- **Solve Instantly**:
    - Click the **"Solve Instantly"** button.
    - The solver will compute the solution without any delay, displaying the completed grid immediately.

### 3. Clear the Grid

To reset the puzzle and input a new one:

- Click the **"Clear"** button in the respective input tab (**"Paste Array"** or **"Manual Input"**).
- This will remove all numbers from the grid, allowing you to start fresh.

### 4. Stop Solving *(Available in Solved Sudoku Tab)*

- While the solver is running with animation, you can stop the process by clicking the **"Stop"** button in the **"Solved Sudoku"** tab.
- This will halt the solving process and return you to the input tab to make any necessary changes.

## How It Works

This Sudoku Solver uses a backtracking algorithm to fill in cells with potential numbers until the entire puzzle is complete. The algorithm recursively tries numbers in each cell, backtracking when a conflict is found, until it finds a solution that satisfies all Sudoku rules.

## Code Structure

- **`SudokuSolverGUI.java`**  
  Main file containing GUI setup and solving logic.

- **`SudokuSolver.java`**  
  Contains the backtracking algorithm for solving Sudoku puzzles.
