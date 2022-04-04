package utm;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * This class simulates a Universal Turing Machine (UTM).
 * @author Damian Arellanes
 */
public class UniversalTuringMachine extends JFrame {
  
  /** The TM that has been loaded onto the UTM. */
  private TuringMachine turingMachine;
  
  /** The container on which all graphical elements are placed. */
  private final JLayeredPane mainContainer;  
  /** The tape panel. */
  private static TapePanel tapePanel;
  /** The head panel. */
  private static HeadPanel headPanel;
  /** The label that indicates the result of a TM's execution. */
  private final JLabel haltLabel;
  /** The scroll pane for displaying the rules of a TM. */
  private final JScrollPane rulesScrollPane;  
  
  /**
   * Creates a new UTM with an empty TM.
   */
  public UniversalTuringMachine() {
    
    super("Universal Turing Machine");
    
    // Creates a TM with no rules and empty tape
    turingMachine = new TuringMachine(0, "", "", "");
    turingMachine.setTape(new Tape());
    
    // Configures the graphical components for any working machine
    mainContainer = new JLayeredPane();
    tapePanel = new TapePanel(turingMachine.getTape());
    headPanel = new HeadPanel(turingMachine.getHead());
    haltLabel = new JLabel();          
    rulesScrollPane = new JScrollPane( 
      new JTable( 
        turingMachine.getRules(), 
        new String[]{
          "Current State", "Current Symbol", "New State", "New Symbol", "Move"
        }
      )
    );
  }
  
  /**
   * Loads a TM onto the UTM.
   * @param turingMachine The TM that will be run on the UTM.
   */
  public void loadTuringMachine(TuringMachine turingMachine) {
    
    // Sets the current working machine with empty tape
    this.turingMachine = turingMachine;
    loadInput("");
    
    // Loads the rules from the TM into the table
    rulesScrollPane.setViewportView(new JTable( 
      turingMachine.getRules(), 
      new String[]{
        "Current State", "Current Symbol", "New State", "New Symbol", "Move"
      }
    ));
  }
  
  /**
   * Loads content into the tape of the current TM and resets the head to 
   * its initial position.
   * @param input The string that will be used to fill the tape.
   */
  public void loadInput(String input) {
    
    // Hides the halt label since a new execution might be required
    haltLabel.setVisible(false);
    haltLabel.setText("");
    
    // Loads the input into the tape and resets the head of the current TM
    turingMachine.setTape(new Tape(input)); 
    turingMachine.getHead().reset();
    
    // Reloads the graphics of the tape and the head
    tapePanel.setTape(turingMachine.getTape());
    headPanel.setHead(turingMachine.getHead());    
    tapePanel.repaint();
    headPanel.repaint();
  }
  
  /**
   * Displays the window for the UTM GUI.
   */
  public void display() {
    
    // Initialises the window
    setSize(new Dimension(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT));    
    setResizable(false);
    getContentPane().setBackground(new Color(203, 197, 197));
    
    // Initialises the tape and head panels
    tapePanel.setBounds(Config.TAPE_X_START, Config.TAPE_Y_START, Config.TAPE_WIDTH, Config.TAPE_HEIGHT);
    headPanel.setBounds(Config.HEAD_X_START, Config.HEAD_Y_START, Config.HEAD_X_END, Config.HEAD_HEIGHT);
    
    // Initialises the label for the TM's result
    haltLabel.setBounds(Config.RESULT_X_START, Config.RESULT_Y_START, Config.RESULT_WIDTH, Config.RESULT_HEIGHT);
    haltLabel.setFont(new Font("Dialog", 1, 16));    
    haltLabel.setVisible(false);
    
    // Initialises the rules table
    rulesScrollPane.setBounds(Config.RULES_X_START, Config.RULES_Y_START, Config.RULES_WIDTH, Config.RULES_HEIGHT);
    
    // Adds the graphical components into the main container
    mainContainer.add(tapePanel, new Integer(1));
    mainContainer.add(headPanel, new Integer(2));
    mainContainer.add(haltLabel, new Integer(3));
    mainContainer.add(rulesScrollPane, new Integer(4));
    getContentPane().add(mainContainer);
    
    this.setVisible(true);
  }
    
  /**
   * Graphically moves the head of the current TM.
   * @param move The direction in which the head will move (only RIGHT or LEFT).
   * @param isAnimated If this flag is set, a delay will be triggered to animate 
   * the head movement.
   */
  public void moveHead(Move move, boolean isAnimated) {
    
    try {
      
      if(isAnimated) Thread.sleep(Config.DELAY); // Delays the animation
      if(move.equals(MoveClassical.RIGHT)) headPanel.moveRight();
      if(move.equals(MoveClassical.LEFT)) headPanel.moveLeft();
      
    } catch (InterruptedException ex) { 
      System.err.println(ex);
    }
  }
  
  /**
   * Updates and renders the state of the loaded TM.
   * @param state A string representing the new state of the TM.
   */
  public void updateHeadState(String state) {
    turingMachine.getHead().setCurrentState(state);
    headPanel.repaint();
  }
  
  /**
   * Graphically writes a new symbol on the cell where the head points to.
   * @param symbol A character representing the symbol being written.
   */
  public void writeOnCurrentCell(char symbol) {
    turingMachine.getTape().set(turingMachine.getHead().getCurrentCell(), symbol);
    tapePanel.repaint();
  }
  
  /**
   * Displays the result of the execution of the loaded TM.
   * @param haltState The halt state to be displayed.
   */
  public void displayHaltState(HaltState haltState) {
    
    haltLabel.setText(haltState.toString());
    haltLabel.setVisible(true);
    System.out.println(haltState);
  }
  
  /**
   * Gets the TM that has been loaded onto the UTM.
   * @return The abstract data type representing the loaded TM.
   */
  public TuringMachine getTuringMachine() { return turingMachine; }
}
