package utm;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 * This class manages the graphics of a Turing Machine's tape.
 * @author Damian Arellanes
 */
public class TapePanel extends JPanel {  
  
  private Tape tape;
  
  /** 
   * Creates the graphical component that represents a TM's tape.
   * @param tape The abstract data type of a TM's tape.
   */
  public TapePanel(Tape tape) {
    this.tape = tape;
  }
  
  /**
   * Renders the tape graphics.
   */
  @Override
  public void paintComponent(Graphics g) {
    
    super.paintComponent(g);
    
    // Sets the font properties for the cells
    FontRenderContext fontRenderContext = new FontRenderContext(null, false, false);
    Rectangle2D symbolBounds;
    g.setFont (new Font ("Arial", 1, 20));
    
    //************************
    /* Draws the tape cells */
    //************************
            
    for(int i = 0; i < Config.MAX_CELLS; i++) {
      
      // Draws a white cell with black border
      
      int offset = 0;
      if(i == Config.MAX_CELLS - 1) offset = 1;
      
      g.setColor(Color.white);
      g.fillRect(Config.CELL_WIDTH*i, 0, Config.CELL_WIDTH, Config.CELL_HEIGHT);
      g.setColor(Color.black);
      g.drawRect(Config.CELL_WIDTH*i, 0, Config.CELL_WIDTH-offset, Config.CELL_HEIGHT-1);			

      // Draws the symbol in the tape cell
      
      symbolBounds = g.getFont().getStringBounds(String.valueOf(tape.get(i)), fontRenderContext);      
      int symbolWidth  = (int)Math.ceil(symbolBounds.getWidth());
	    int symbolHeight = (int)Math.ceil(symbolBounds.getHeight());
      
      g.drawString(String.valueOf(
        tape.get(i)), 
        Config.CELL_WIDTH*i + (Config.CELL_WIDTH-symbolWidth)/2, 
        symbolHeight + (Config.CELL_HEIGHT-symbolHeight)/2); 
    }
  }
  
  /**
   * This method allows the replacement of TM's tapes.
   * @param tape The abstract data type of a TM's tape.
   */
  public void setTape(Tape tape) { this.tape = tape; }
}
