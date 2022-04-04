package utm;

/**
 * Global configuration for the graphical interface.
 * @author Damian Arellanes
 */
public interface Config {
  
  /** Animation delay (ms). */
  public final int DELAY = 800;
      
  /** Blank cell representation. */
  public final char BLANK_CELL = '*'; 
  /** Maximum number of tape cells. */
  public final int MAX_CELLS = 20;
  /** Width of a tape cell. */
  public final int CELL_WIDTH = 40;
  /** Height of a tape cell. */
  public final int CELL_HEIGHT = 40;
    
  /** Width of the GUI window. */
  public final int WINDOW_WIDTH = 1000;
  /** Height of the GUI window. */
  public final int WINDOW_HEIGHT = 600;
      
  /** Tape width. */
  public final int TAPE_WIDTH = CELL_WIDTH*MAX_CELLS;
  /** Tape x-origin. */
  public final int TAPE_X_START = WINDOW_WIDTH/2 - TAPE_WIDTH/2;
  /** Tape y-origin. */
  public final int TAPE_Y_START = WINDOW_HEIGHT/6;
  /** Tape height. */
  public final int TAPE_HEIGHT = CELL_HEIGHT;
    
  /** Head width. */
  public final int HEAD_WIDTH = 56;  
  /** Head height. */
  public final int HEAD_HEIGHT = 85;
  /** Head x-origin. */
  public final int HEAD_X_START = TAPE_X_START - (HEAD_WIDTH - CELL_WIDTH)/2;
  /** Head x-end. */
  public final int HEAD_X_END = CELL_WIDTH*(MAX_CELLS+1); 
  /** Head y-origin. */
  public final int HEAD_Y_START = TAPE_Y_START + CELL_HEIGHT;
  
  /** Halt label x-origin. */
  public final int RESULT_X_START = TAPE_X_START + TAPE_WIDTH/2 - CELL_WIDTH;
  /** Halt label width. */
  public final int RESULT_WIDTH = 120;
  /** Halt label height. */
  public final int RESULT_HEIGHT = 20;
  /** Halt label y-origin. */
  public final int RESULT_Y_START = TAPE_Y_START - RESULT_HEIGHT*2;  
  
  /** Rules table x-origin. */
  public final int RULES_X_START = (WINDOW_WIDTH - CELL_WIDTH*20)/2;
  /** Rules table width. */
  public final int RULES_WIDTH = CELL_WIDTH*20;
  /** Rules table y-origin. */
  public final int RULES_Y_START = HEAD_Y_START + HEAD_HEIGHT + 50;
  /** Rules table height. */
  public final int RULES_HEIGHT = 200;
}
