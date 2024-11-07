**SudokuSolverGUI**

A graphical user interface (GUI) application for solving Sudoku puzzles using a backtracking algorithm. 
This Python project allows users to input a Sudoku puzzle and get an instant solution, making it a great tool for both Sudoku enthusiasts and developers interested in understanding backtracking-based puzzle solvers.

**Features**
Easy-to-use GUI for inputting puzzles and displaying results
**Backtracking algorithm** to solve any valid 9x9 Sudoku puzzle
Real-time feedback on solution process

**Requirements**
This project is built with Java 

**Usage**
Input the Puzzle: Enter the numbers for the Sudoku puzzle you want to solve. Leave cells empty where numbers are unknown.
Solve the Puzzle: Click the "Solve" button to let the program solve the puzzle. The solution will populate the grid within seconds.
Clear: Use the "Clear" button to reset the grid for a new puzzle.

**How It Works**
This Sudoku Solver uses a backtracking algorithm to fill in cells with potential numbers until the entire puzzle is complete. The algorithm recursively tries numbers in each cell, backtracking when a conflict is found, until it finds a solution that satisfies all Sudoku rules.

**Code Structure**
SudokuSolverGUI.py: Main file containing GUI setup and solving logic
solver.py: Contains the backtracking algorithm for solving Sudoku puzzles
