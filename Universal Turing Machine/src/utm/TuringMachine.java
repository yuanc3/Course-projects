package utm;

/**
 * Abstract data type for a Turing Machine.
 * @author Damian Arellanes
 */
public class TuringMachine {
  
  private Tape tape;
  private final Head head;
  private int ruleCount;
  private final String[][] rules;  
  
  private final String initialState;
  private final String acceptState;
  private final String rejectState;
  
  /**
   * Creates a TM with no rules.
   * @param rulesNumber The number of rules that the machine will have.
   * @param initialState A string representing the TM's initial state.
   * @param acceptState A string representing the TM's accept state.
   * @param rejectState A string representing the TM's reject state.
   */
  public TuringMachine(int rulesNumber, String initialState, String acceptState, 
    String rejectState) {
    
    // The head always starts on the leftmost cell
    this.head = new Head(initialState);
    
    // Rule format: {currentState, currentSymbol, newState, newSymbol, move}
    this.ruleCount = 0;
    this.rules = new String[rulesNumber][5];  
    
    // Sets the initial and halt states of the machine
    this.initialState = initialState;
    this.acceptState = acceptState;
    this.rejectState = rejectState;
  }
  
  /**
   * Adds a new rule into the array of rules of the TM 
   * (Note that the maximum number of rules was specified during construction).
   * @param currentState A string representing state for triggering the rule.
   * @param currentSymbol A character representing symbol for triggering the rule.
   * @param newState A string representing the TM's state after triggering the rule.
   * @param newSymbol A character that will be written on the current cell after triggering the rule.
   * @param move The head movement that will be performed after triggering the rule.
   */
  public void addRule(String currentState, char currentSymbol, 
    String newState, char newSymbol, Move move) {
        
    rules[ruleCount] = new String[]{currentState, String.valueOf(currentSymbol), 
      newState, String.valueOf(newSymbol), move.toString()};    
    ruleCount++;
  }
  
  /**
   * Gets the current tape of the TM.
   * @return The abstract data type of the TM's tape.
   */
  public Tape getTape() { return tape; }  
  
  /**
   * Replaces the TM's tape with a new one.
   * @param tape The tape that will replace the existing one.
   */
  public void setTape(Tape tape) { this.tape = tape; }
  
  /**
   * Gets the head of the TM.
   * @return The abstract data type of the TM's head.
   */
  public Head getHead() { return head; }
  
  /**
   * Gets the rules of the TM.
   * @return A bidimensional array of rules where each rule has the form 
   * {currentState, currentSymbol, newState, newSymbol, move}.
   */
  public String[][] getRules() { return rules; }  
  
  /**
   * Gets the initial state of the TM.
   * @return A string representing the TM's initial state.
   */
  public String getInitialState() { return initialState; }
  
  /**
   * Gets the accept state of the TM.
   * @return A string representing the TM's accept state.
   */
  public String getAcceptState() { return acceptState; }
  
  /**
   * Gets the reject state of the TM.
   * @return A string representing the TM's reject state.
   */
  public String getRejectState() { return rejectState; }
}
