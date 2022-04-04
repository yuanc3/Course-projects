package utm;

/**
 * Abstract data type for a Turing Machine's head.
 * @author Damian Arellanes
 */
public class Head {
    
  private final String initialState;
  private int currentCell;
  private String currentState;  
  
  /**
   * Creates a head with the specified state.
   * @param initialState A string representing the TM's initial state.
   */
  public Head(String initialState) {
    
    this.initialState = initialState;
    currentCell = 0;
    currentState = initialState;    
  }
  
  /** Moves the head to the next cell of the tape. */
  public void moveRight() { this.currentCell++; }
  
  /** Moves the head to the previous cell of the tape. */
  public void moveLeft() { this.currentCell--; }
  
  /** Moves the head to the leftmost cell and resets the state to the initial one. */
  public void reset() {    
    currentCell = 0;
    currentState = initialState;
  }

  /** 
   * Gets the position of the cell where the head is pointing to. 
   * @return The index of the current cell.
   */
  public int getCurrentCell() { return currentCell; }
  
  /** 
   * Gets the current state of the TM. 
   * @return A string representing the TM's state.
   */
  public String getCurrentState() { return currentState; }
  
  /** 
   * Sets the current state of the TM. 
   * @param currentState A string representing the TM's state.
   */
  public void setCurrentState(String currentState) {
    this.currentState = currentState;
  }  
}
