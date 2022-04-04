package utm;

import java.util.ArrayList;

/**
 * Abstract data type for a Turing Machine's tape.
 * @author Damian Arellanes
 */
public final class Tape extends ArrayList<Character> {
  
  /**
   * Creates a tape with no content.
   */
  public Tape() {
    for(int i = 0; i < Config.MAX_CELLS; i++) add(Config.BLANK_CELL);
  }

  /**
   * Creates a tape with initial content.
   * @param input A string that will be used to fill the tape.
   */
  public Tape(String input) {
    
    if(input.length() > Config.MAX_CELLS) {
      System.err.println("The input length must be less or equal than " + Config.MAX_CELLS);
      return;
    }      
    
    // Fills the tape with the initial content and appends blank cells if needed
    for(int i = 0; i < Config.MAX_CELLS; i++) {
      
      if(i >= input.length()) add(Config.BLANK_CELL);
      else add(input.charAt(i));
    }
  }
}
