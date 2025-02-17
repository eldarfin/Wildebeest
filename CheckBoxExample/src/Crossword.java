
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

/**
 * Class which represents a Crossword's data. It contains two constants, ACROSS
 * and DOWN to represent the corresponding orientations in the crossword.
 *
 */
class Crossword {

    ;
	final ArrayList<Clue> acrossClues, downClues;
    final String title;
    final int size;
    public static int ACROSS = 0, DOWN = 1;

    Crossword(String title, int size, ArrayList<Clue> acrossClues,
            ArrayList<Clue> downClues) {
        this.title = title;
        this.size = size;
        this.acrossClues = acrossClues;
        this.downClues = downClues;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public static void main(String[] args) throws FileNotFoundException {
        new CrosswordFrame();
    }
}

/**
 *
 * Class which represent's a clue's data. Extended from given one for added
 * functionality.
 *
 */
class Clue {

    int number, x, y; //Clue number and coordinates of the starting cell
    final String clue, answer;
    String name, time; // User that solved the clue, and time it was solved
    int orientation, index; // Clue's orientation in the crossword and an index so it can be searched.
    boolean isHighlighted, hasBeenSolved;
    ArrayList<CellPanel> cells; // Each clue is a collection of cells

    /**
     * Default constructor
     */
    Clue(int number, int x, int y, String clue, String answer) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.clue = clue;
        this.answer = answer;
        cells = new ArrayList<CellPanel>();
        name = " ";
        time = " ";
    }

    /**
     * Constructor used when loading a puzzle. Updates each clue's state.
     */
    Clue(int number, int x, int y, String clue, String answer, String name, String time, int orientation, int index, boolean isHighlighted, boolean hasBeenSolved) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.clue = clue;
        this.answer = answer;
        this.name = name;
        this.orientation = orientation;
        this.index = index;
        this.isHighlighted = isHighlighted;
        this.hasBeenSolved = hasBeenSolved;
        cells = new ArrayList<CellPanel>();

    }

    public String toString() {
        return number + ". " + clue + " (" + answer.length() + ")";
    }

    /**
     * Add a crossword cell to the clue's collection of cells
     *
     * @param cell
     */
    public void add(CellPanel cell) {
        cells.add(cell);
    }

    /**
     * Search whether a crossword cell is in the clue's collection of cells
     *
     * @param cell The cell to be found in the collection
     * @return true if found, false otherwise
     */
    public boolean contains(CellPanel cell) {
        return cells.contains(cell);
    }

    /**
     * Get the next cell in the collection of cells
     *
     * @param cell The cell to get the next cell from
     * @return The next cell from the cell parameter
     */
    public CellPanel getNextElement(CellPanel cell) {
        int currentIndex = cells.indexOf(cell);
        if (currentIndex == -1 || currentIndex == cells.size() - 1) { // If index is -1 (safeguard) or we've reached the last cell, return the same cell as given
            return cell;
        } else {
            return cells.get(currentIndex + 1);
        }
    }

    /**
     * Get the previous cell in the collection of cells
     *
     * @param cell The cell to get the previous cell from
     * @return The previous cell from the cell parameter
     */
    public CellPanel getPreviousElement(CellPanel cell) {
        int currentIndex = cells.indexOf(cell);
        if (currentIndex == -1 || currentIndex == 0) { // If index is -1 (safeguard) or we've reached the first cell, return the same cell as given
            return cell;
        } else {
            return cells.get(currentIndex - 1);
        }
    }

    /**
     * Get the first cell in the collection of cells
     *
     * @return The first cell in the collection
     */
    public CellPanel getFirstCell() {
        return cells.get(0);
    }

    /**
     * Checks whether the clue has been solved
     *
     * @return true if the clue has been solved, false otherwise
     */
    public boolean isSolved() {
        if (!hasBeenSolved) { // Check if it hasn't been solved already
            String tmpAnswer = "";

            for (CellPanel cell : cells) {
                tmpAnswer += cell.answer; // Build the user's input from the collection of cells
            }

            if (tmpAnswer.equalsIgnoreCase(answer)) { // If it matches the clue's answer, it's been solved
                hasBeenSolved = true;
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * Highlight the clue's cells
     */
    public void highlight() {
        isHighlighted = true;
        for (CellPanel cell : cells) {
            cell.setBackground(Color.GRAY);
        }
    }

    /**
     * Unhighlight the clue's cells
     */
    public void unHighlight() {
        isHighlighted = false;
        for (CellPanel cell : cells) {
            cell.setBackground(Color.WHITE);
        }
    }

}

/**
 * Class to read and save puzzles in txt format
 *
 * @author zm1g09
 *
 */
class CrosswordIO {

    static CellPanel[][] cells;

    /**
     * Reads a puzzle from the state it was saved at
     *
     * @param filename The file's name to read from
     * @return The read crossword
     */
    public static Crossword readPuzzle(String filename) {
        BufferedReader input;
        Crossword crossword = null;
        String tmp, title;
        int size;
        ArrayList<Clue> acrossClues = new ArrayList<Clue>(), downClues = new ArrayList<Clue>();

        try {
            input = new BufferedReader(new FileReader(filename));
            // Format of text files explained in writePuzzle method
            title = input.readLine();
            size = Integer.parseInt(input.readLine());
            tmp = input.readLine();
            cells = new CellPanel[size][size]; // Make the 2-dimensional array of cells representing the crossword grid first, and update the across and down Clues in CrosswordFrame's makeCells method
            while (!tmp.equalsIgnoreCase("")) { // Will read until empty line
                Clue clue = new Clue(Integer.parseInt(tmp), Integer.parseInt(input.readLine()), Integer.parseInt(input.readLine()), input.readLine(), input.readLine(), input.readLine(), input.readLine(), Integer.parseInt(input.readLine()), Integer.parseInt(input.readLine()), Boolean.getBoolean(input.readLine()), Boolean.getBoolean(input.readLine()));
                for (int i = 0; i < clue.answer.length(); i++) {
                    CellPanel cell = new CellPanel(input.readLine(), input.readLine(), Boolean.getBoolean(input.readLine()), Integer.parseInt(input.readLine()), Integer.parseInt(input.readLine()));
                    cells[cell.x][cell.y] = cell;
                }
                acrossClues.add(clue);
                tmp = input.readLine();
            }
            tmp = input.readLine();
            while (!tmp.equalsIgnoreCase("")) { // As above, for down clues
                Clue clue = new Clue(Integer.parseInt(tmp), Integer.parseInt(input.readLine()), Integer.parseInt(input.readLine()), input.readLine(), input.readLine(), input.readLine(), input.readLine(), Integer.parseInt(input.readLine()), Integer.parseInt(input.readLine()), Boolean.getBoolean(input.readLine()), Boolean.getBoolean(input.readLine()));
                for (int i = 0; i < clue.answer.length(); i++) {
                    CellPanel cell = new CellPanel(input.readLine(), input.readLine(), Boolean.getBoolean(input.readLine()), Integer.parseInt(input.readLine()), Integer.parseInt(input.readLine()));
                    cells[cell.x][cell.y] = cell;
                    //clue.add(cell);
                }
                downClues.add(clue);
                tmp = input.readLine();
            }

            crossword = new Crossword(title, size, acrossClues, downClues);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return crossword;
    }

    /**
     * Saves the current crossword and its state to a txt file
     *
     * @param crossword The crossword to be saved
     */
    public static void writePuzzle(Crossword crossword) {
        BufferedWriter output;

        try {
            //Write all data in separate lines.
            output = new BufferedWriter(new FileWriter(crossword.getTitle() + ".txt"));
            //Write the crossword's data
            output.write(crossword.getTitle());
            output.newLine();
            output.write(String.valueOf(crossword.getSize()));
            output.newLine();
            //Write each clue's data 
            for (Clue clue : crossword.acrossClues) {
                writeValues(output, clue);
            }
            output.newLine(); // Leave empty line to separate between data of across and down clues
            for (Clue clue : crossword.downClues) {
                writeValues(output, clue);
            }
            output.newLine();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used by writePuzzle to write the data of each clue
     *
     * @param output The buffered writer object used to write to the file
     * @param clue The current clue to be written to the file
     * @throws IOException
     */
    private static void writeValues(BufferedWriter output, Clue clue) throws IOException {
        //Write all the data of a clue
        output.write(String.valueOf(clue.number));
        output.newLine();
        output.write(String.valueOf(clue.x));
        output.newLine();
        output.write(String.valueOf(clue.y));
        output.newLine();
        output.write(clue.clue);
        output.newLine();
        output.write(clue.answer);
        output.newLine();
        output.write(clue.name);
        output.newLine();
        output.write(clue.time);
        output.newLine();
        output.write(String.valueOf(clue.orientation));
        output.newLine();
        output.write(String.valueOf(clue.index));
        output.newLine();
        output.write(String.valueOf(clue.isHighlighted));
        output.newLine();
        output.write(String.valueOf(clue.hasBeenSolved));
        output.newLine();
        //Write the data of all the cells in a clue
        for (CellPanel cell : clue.cells) {
            output.write(cell.clueNumber.getText());
            output.newLine();
            output.write(cell.answer);
            output.newLine();
            output.write(String.valueOf(cell.state));
            output.newLine();
            output.write(String.valueOf(cell.x));
            output.newLine();
            output.write(String.valueOf(cell.y));
            output.newLine();
        }

    }
}

/**
 * Crossword Frame. Represents the crossword GUI
 *
 */
class CrosswordFrame extends JFrame implements ActionListener, MouseListener,
        KeyListener {

    private static final long serialVersionUID = 1L;
    private Crossword crossword;
    private CellPanel[][] cells; // The crossword grid
    private CellPanel[][] answerCells;
    private Clue tmpClue; // Holds the last selected clue
    private int size;
    private JList acrossList, downList;
    private JTextField name;
    //private JTextArea solvedClues;
    private JCheckBox solvedCluesBox;
    //private String solvedCluesText = "";
    private JComboBox cb;
    private JPanel answerPanel;
    File oldPuzzlesFile = new File("./puzzles.txt");

    /**
     * Default constructor. Initializes values and makes a new puzzle from
     * CrosswordExample
     */
    public CrosswordFrame() throws FileNotFoundException {
        super();
        crossword = new CrosswordExample().getNewPuzzle();
        size = crossword.getSize();

        cells = makeCells();
        answerCells = getAnsweredCrossword(crossword, cells);
        setLocationRelativeTo(null);
        init(0);
    }

    /**
     * Constructor to be used when loading a puzzle
     *
     * @param crossword Crossword that has been read from CrosswordIO
     */
    public CrosswordFrame(Crossword crossword) throws FileNotFoundException {
        super();
        this.crossword = crossword;
        size = crossword.getSize();
        cells = makeCells(crossword);
        setLocationRelativeTo(null);
        init(0);
    }

    public CrosswordFrame(int lastSelectedPuzzleIndex) throws FileNotFoundException {
        super();
        CrosswordExample ce = new CrosswordExample();
        crossword = ce.getPuzzle(lastSelectedPuzzleIndex);
        size = crossword.getSize();
        cells = makeCells();
        answerCells = getAnsweredCrossword(crossword, cells);
        //System.out.println(lastSelectedPuzzleIndex);
        setLocationRelativeTo(null);
        init(lastSelectedPuzzleIndex);
    }

    /**
     * Initialize all the necessary values for the GUI
     */
    private void init(int lastSelectedPuzzleIndex) throws FileNotFoundException {
        tmpClue = null;

        this.setTitle(crossword.getTitle());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
	//	this.setSize(580, 622);

        JPanel crosswordPanel = new JPanel();

        answerPanel = new JPanel();
        acrossList = new JList(crossword.acrossClues.toArray());
        downList = new JList(crossword.downClues.toArray());
        JScrollPane acrossScrollPane = new JScrollPane(acrossList);
        JScrollPane downScrollPane = new JScrollPane(downList);
        JPanel acrossPanel = new JPanel(new BorderLayout()); //Panels for the lists of clues so that labels can be displayed to make out between across and down
        JPanel downPanel = new JPanel(new BorderLayout());

        acrossPanel.add(new JLabel("Across Clues"), BorderLayout.NORTH);
        acrossPanel.add(acrossScrollPane, BorderLayout.CENTER);
        downPanel.add(new JLabel("Down Clues"), BorderLayout.NORTH);
        downPanel.add(downScrollPane, BorderLayout.CENTER);

        JPanel cluePanel = new JPanel(new GridLayout(1, 2)); // The display of the clue lists which will be added to the bottom of the GUI

        cluePanel.add(acrossPanel);
        cluePanel.add(downPanel);

        GridLayout grid = new GridLayout(size, size, 2, 2); // Leave a small gap between the cells in the crossword grid
        crosswordPanel.setLayout(grid);
        crosswordPanel.setBackground(Color.LIGHT_GRAY);
        answerPanel.setLayout(grid);
        answerPanel.setBackground(Color.pink);
        answerPanel.setVisible(false);

		//solvedClues = new JTextArea(1, 25);
        solvedCluesBox = new JCheckBox("Reveal Puzzle"); //Checkbox so that the solved clues support can toggled on and off
        int numberOfOldPuzzles = countLines(oldPuzzlesFile);

        String[] dates = new String[numberOfOldPuzzles + 1];
        for (int i = 0; i < numberOfOldPuzzles + 1; i++) {
            if (i == 0) {
                dates[0] = "Today's Puzzle";
            } else {
                dates[i] = "Old Puzzle " + i;
            }
        }

        cb = new JComboBox(dates);
        cb.setSelectedIndex(lastSelectedPuzzleIndex);
        cb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CrosswordExample examp = new CrosswordExample();
                int index = cb.getSelectedIndex();
                try {
                    new CrosswordFrame(index);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CrosswordFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                setLocationRelativeTo(null);
                dispose();
            }
        });
        JPanel inputPanel = new JPanel(new GridLayout(1, 2));
        JPanel namePanel = new JPanel(new FlowLayout());

        name = new JTextField(20);

        namePanel.add(new JLabel("Name: "));
        namePanel.add(name);

        JPanel boxPanel = new JPanel(); // Checkbox added to panel so it's centered
        boxPanel.add(cb);
        boxPanel.add(solvedCluesBox);
        inputPanel.add(namePanel);
        inputPanel.add(boxPanel);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem load = new JMenuItem("Load");
        save.addActionListener(this);
        load.addActionListener(this);
        menu.add(save);
        menu.add(load);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        JScrollPane cluesPane = new JScrollPane(answerPanel);

        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                cells[i][j].addMouseListener(this);
                cells[i][j].addKeyListener(this);
                crosswordPanel.add(cells[i][j]);
            }
        }

        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                answerPanel.add(answerCells[i][j]);
            }
        }

        acrossList.addMouseListener(this);
        downList.addMouseListener(this);
        solvedCluesBox.addActionListener(this);

        JPanel puzzlesPanel = new JPanel(new GridLayout(1, 2)); // The display of the clue lists which will be added to the bottom of the GUI

        puzzlesPanel.add(crosswordPanel);
        puzzlesPanel.add(answerPanel);

        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(puzzlesPanel, BorderLayout.CENTER);
        //getContentPane().add(answerPanel, BorderLayout.EAST);
        getContentPane().add(cluePanel, BorderLayout.SOUTH);
        SwingUtilities.invokeLater(new Runnable() { //invokeLater so the GUI is updated after all data and listeners have been loaded

            @Override
            public void run() {
                pack();
                setVisible(true);
            }

        });
    }

    public static int countLines(File posFile) throws FileNotFoundException {
        int numberOfLines = 0;
        try (Scanner scan = new Scanner(posFile)) {
            while (scan.hasNextLine()) {
                scan.nextLine();
                numberOfLines++;
            }
        }
        return numberOfLines;
    }

    /**
     * Used to update the crossword grid when loading a puzzle
     *
     * @param crossword The crossword being loaded
     * @return The crossword grid
     */
    private CellPanel[][] makeCells(Crossword crossword) {
        int size = crossword.size;
        CellPanel[][] cells = CrosswordIO.cells; // Get the crossword grid from CrosswordIO, which has updated the grid from readPuzzle
        CellPanel cell;

        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                if (cells[i][j] == null) {
                    cells[i][j] = new CellPanel(); // Make all the solid blocks of the puzzle
                }
            }
        }
        for (Clue clue : crossword.acrossClues) { // Add each non-solid cell to its corresponding clue
            for (int i = 0; i < clue.answer.length(); i++) {
                cell = cells[clue.x + i][clue.y];
                cell.setState(true);
                clue.add(cell);
            }
        }
        for (Clue clue : crossword.downClues) { //As above for down clues
            for (int i = 0; i < clue.answer.length(); i++) {
                cell = cells[clue.x][clue.y + i];
                cell.setState(true);
                clue.add(cell);
            }
        }

        return cells;
    }

    /*	private void printCells(CellPanel[][] cells){
     for (int j = 0; j < size; j++){
     for (int i = 0; i < size; i++){
     if(cells[i][j] == null){
     System.out.print("# ");
     }else if(cells[i][j].answer.equals(""))
     System.out.print("_ ");
     else
     System.out.print(cells[i][j].answer + " ");
     }
     System.out.println();
     }
     System.out.println("==================================");
     }*/
    /**
     * Initialise the crossword grid for the default puzzle
     */
    public CellPanel[][] makeCells() {
        int size = crossword.size;
        CellPanel[][] cells = new CellPanel[size][size];
        CellPanel tmpCell; //Used to traverse through each clue's cells
        int index = 0;

        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                cells[i][j] = new CellPanel(); //Initialise everything to solid
            }
        }
        for (Clue clue : crossword.acrossClues) { //Update the grid to non-solid according to the lists of clues
            clue.orientation = Crossword.ACROSS;
            clue.index = index++; // For indexing each clue

            tmpCell = cells[clue.x][clue.y];

            tmpCell.setClueNumber(clue.number);
            for (int i = 0; i < clue.answer.length(); i++) { //A clue has as many cells as characters of its answer
                tmpCell = cells[clue.x + i][clue.y];
                clue.add(tmpCell);

                tmpCell.x = clue.x + i;
                tmpCell.y = clue.y;
                tmpCell.setState(true);
            }
        }
        index = 0;
        for (Clue clue : crossword.downClues) { //As above for down clues
            clue.orientation = Crossword.DOWN;
            clue.index = index++;

            tmpCell = cells[clue.x][clue.y];

            tmpCell.setClueNumber(clue.number);
            for (int i = 0; i < clue.answer.length(); i++) {
                tmpCell = cells[clue.x][clue.y + i];
                clue.add(tmpCell);

                tmpCell.x = clue.x;
                tmpCell.y = clue.y + i;
                tmpCell.setState(true);
            }
        }
        return cells;
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        Object tmpObject = arg0.getSource();
        CellPanel tmpCell = null;
        JList tmpList;
        int index;

        if (tmpObject instanceof CellPanel) { //Used to determine what has been clicked
            tmpCell = (CellPanel) arg0.getSource();
            selectCell(tmpCell);
        } else { // If a clue in the lists was selected
            if (tmpClue != null) {
                tmpClue.unHighlight();
            }

            tmpList = (JList) arg0.getSource();
            index = tmpList.getSelectedIndex();
            if (tmpList == acrossList) { //Search through the clues to find the selected one
                for (Clue clue : crossword.acrossClues) {
                    if (clue.index == index) {
                        tmpClue = clue;
                        tmpCell = clue.getFirstCell(); //Select its first cell
                        break;
                    }
                }
            } else {
                for (Clue clue : crossword.downClues) {
                    if (clue.index == index) {
                        tmpClue = clue;
                        tmpCell = clue.getFirstCell();
                        break;
                    }
                }
            }
            /*
             * if(orientation == Crossword.DOWN) { //selectCell(tmpCell); }
             */
        }
        if (tmpCell.state) {
            refreshHighlight(tmpCell);
        }
        // selectCell(tmpCell);
    }

    /**
     * Used to set the currently selected cell, and handle the highlights
     *
     * @param tmpCell The cell to select
     */
    private void selectCell(CellPanel tmpCell) {
        for (Clue clue : crossword.acrossClues) {
            if (clue.contains(tmpCell)) {
                if ((tmpCell.getBackground() != Color.YELLOW || clue != tmpClue)) { //Used to determine whether a different cell has been clicked, or when to toggle the currently selected cell's orientation
                    if (tmpClue != null) {
                        tmpClue.unHighlight();
                    }
                    tmpClue = clue;
                    refreshHighlight(tmpCell);
                    return;
                }
            }
        }

        for (Clue clue : crossword.downClues) {
            if (clue.contains(tmpCell)) {
                if ((tmpCell.getBackground() != Color.YELLOW || clue != tmpClue)) {
                    if (tmpClue != null) {
                        tmpClue.unHighlight();
                    }
                    tmpClue = clue;
                    refreshHighlight(tmpCell);
                    return;
                }
            }
        }
    }

    /**
     * Used to refresh the highlights. Will highlight the corresponding clue in
     * the lists if a cell is clicked on the grid.
     *
     * @param tmpCell The next cell to give focus to
     */
    private void refreshHighlight(CellPanel tmpCell) {
        tmpClue.highlight();
        //Clears the selections from the objects in the opposite list and highlights the objects in the selected list.
        if (tmpClue.orientation == Crossword.ACROSS) {
            downList.setSelectionForeground(Color.BLACK);
            downList.clearSelection();
            acrossList.setSelectedIndex(tmpClue.index);
            acrossList.setSelectionForeground(Color.RED);
            acrossList.ensureIndexIsVisible(tmpClue.index);
        } else {
            acrossList.setSelectionForeground(Color.BLACK);
            acrossList.clearSelection();
            downList.setSelectedIndex(tmpClue.index);
            downList.setSelectionForeground(Color.RED);
            downList.ensureIndexIsVisible(tmpClue.index);
        }

        if (tmpCell.state) {
            tmpCell.requestFocus();
            tmpCell.setBackground(Color.YELLOW);
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent arg0) {

        CellPanel tmpCell = (CellPanel) arg0.getSource();
        if (tmpCell.state) {

            if (KeyEvent.VK_BACK_SPACE == arg0.getKeyCode()) {
                tmpCell.setBackground(Color.GRAY);
                tmpCell.setUserInput(" ");
                tmpCell = tmpClue.getPreviousElement(tmpCell);

            } else if (KeyEvent.VK_LEFT == arg0.getKeyCode()) {
                if (tmpClue.orientation == Crossword.ACROSS) {
                    tmpCell.setBackground(Color.GRAY);
                    tmpCell = tmpClue.getPreviousElement(tmpCell);
                } else {
                    selectCell(tmpCell);
                }
            } else if (KeyEvent.VK_RIGHT == arg0.getKeyCode()) {
                if (tmpClue.orientation == Crossword.ACROSS) {
                    tmpCell.setBackground(Color.GRAY);
                    tmpCell = tmpClue.getNextElement(tmpCell);
                } else {
                    selectCell(tmpCell);
                }
            } else if (KeyEvent.VK_UP == arg0.getKeyCode()) {
                if (tmpClue.orientation == Crossword.DOWN) {
                    tmpCell.setBackground(Color.GRAY);
                    tmpCell = tmpClue.getPreviousElement(tmpCell);
                } else {
                    selectCell(tmpCell);
                }
            } else if (KeyEvent.VK_DOWN == arg0.getKeyCode()) {
                if (tmpClue.orientation == Crossword.DOWN) {
                    tmpCell.setBackground(Color.GRAY);
                    tmpCell = tmpClue.getNextElement(tmpCell);
                } else {
                    selectCell(tmpCell);
                }
            } else if ((arg0.getKeyChar() + "").matches("[a-zA-Z -]")) {
                tmpCell.setBackground(Color.GRAY);
                tmpCell.setUserInput(String.valueOf(arg0.getKeyChar())
                        .toUpperCase());
                tmpCell = tmpClue.getNextElement(tmpCell);
                for (Clue clue : crossword.acrossClues) {
                    checkSolvedClues(clue);
                }
                for (Clue clue : crossword.downClues) {
                    checkSolvedClues(clue);
                }

            }

            tmpCell.requestFocus();
            tmpCell.setBackground(Color.YELLOW);

        }
    }

    private void checkSolvedClues(Clue clue) {
        if (clue.isSolved()) {
            if (name.getText().matches("\\s?")) {
                clue.name = "Anonymous User";
            } else {
                clue.name = name.getText();
            }
            clue.time = new java.util.Date().toString();
			//solvedCluesText += clue + ": " + clue.answer.toUpperCase() + "\n" + "Solved by "
            //		+ clue.name + " on " + clue.time
            //		+ "\n\n";
            toggleSolvedClueText();
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    private void toggleSolvedClueText() {
        answerPanel.setVisible(solvedCluesBox.isSelected());
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        Object tmpObject = arg0.getSource();

        if (tmpObject instanceof JCheckBox) {
            toggleSolvedClueText();
        } else if (((JMenuItem) tmpObject).getText().equalsIgnoreCase("Save")) {
            CrosswordIO.writePuzzle(crossword);
        } else if (((JMenuItem) tmpObject).getText().equalsIgnoreCase("Load")) {
            final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));

            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    new CrosswordFrame(CrosswordIO.readPuzzle(fc.getName(fc.getSelectedFile())));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CrosswordFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                setLocationRelativeTo(null);
                this.setVisible(false);
                this.dispose();
                //this.setCrossword(CrosswordIO.readPuzzle());
            }
        }
    }

    private CellPanel[][] getAnsweredCrossword(Crossword crossword, CellPanel[][] originalCells) {
        CellPanel[][] answeredCells = new CellPanel[originalCells.length][originalCells[0].length];
        for (int i = 0; i < answeredCells.length; i++) {
            for (int j = 0; j < answeredCells[i].length; j++) {
                answeredCells[i][j] = new CellPanel(originalCells[i][j].clueNumber.getText(), originalCells[i][j].answer, originalCells[i][j].state, originalCells[i][j].x, originalCells[i][j].y);
            }
        }

        for (int i = 0; i < crossword.acrossClues.size(); i++) {
            for (int j = 0; j < crossword.acrossClues.get(i).cells.size(); j++) {
                answeredCells[crossword.acrossClues.get(i).cells.get(j).x][crossword.acrossClues.get(i).cells.get(j).y].setUserInput(crossword.acrossClues.get(i).answer.charAt(j) + "");
            }
        }

        for (int i = 0; i < crossword.downClues.size(); i++) {
            for (int j = 0; j < crossword.downClues.get(i).cells.size(); j++) {
                answeredCells[crossword.downClues.get(i).cells.get(j).x]
                        [crossword.downClues.get(i).cells.get(j).y].
                        setUserInput(crossword.downClues.get(i).answer.charAt(j) + "");
            }
        }
        return answeredCells;
    }
}

class CellPanel extends JPanel {

    JLabel clueNumber;
    JLabel userInput;
    String answer;
    boolean state;
    int x, y;

    public CellPanel() {
        super();
        this.setState(false);
        clueNumber = new JLabel();
        userInput = new JLabel();
        answer = "";
        init();
    }

    public CellPanel(String clueNumber, String answer, boolean state, int x, int y) {
        this.clueNumber = new JLabel();
        this.userInput = new JLabel();
        this.setState(state);
        this.x = x;
        this.y = y;
        this.answer = "";
        init();
        setClueNumber(clueNumber);
        setUserInput(answer);
    }

    private void init() {
        clueNumber.setHorizontalAlignment(SwingConstants.LEFT);
        clueNumber.setText(" ");
        userInput.setHorizontalAlignment(SwingConstants.CENTER);
        userInput.setText(" ");
        this.setLayout(new BorderLayout());
        this.add(clueNumber, BorderLayout.NORTH);
        this.add(userInput, BorderLayout.CENTER);
    }

    public void setClueNumber(int clueNumber) {
        this.clueNumber.setText(String.valueOf(clueNumber));
    }

    public void setClueNumber(String clueNumber) {
        this.clueNumber.setText(clueNumber);
    }

    public void setUserInput(String userInput) {
        answer = userInput;
        this.userInput.setText(userInput);
    }

    public void setState(boolean state) {
        this.state = state;

        if (state) {
            this.setBackground(Color.WHITE);
        } else {
            this.setBackground(Color.BLACK);
        }
    }

}
