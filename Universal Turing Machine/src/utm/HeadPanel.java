package utm;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * This class manages the graphics of a Turing Machine's head.
 * @author Damian Arellanes
 */
public class HeadPanel extends JPanel {
  
  private Head head;
  private Image headImage;
  
  /** 
   * Creates the graphical component that represents a TM's head.
   * @param head The abstract data type of a TM's head.
   */
  public HeadPanel(Head head) { 
    this.head = head;     
        
    // Tries to create the image resource for the head
    try {
      headImage = ImageIO.read(Config.class.getResource("tm-head.png"));
    } catch (IOException ex) {
      System.err.println(ex);
    }
  }
  
  /**
   * Moves the head graphics to the next cell of the tape.
   */
  public void moveRight() {
    
    if(head.getCurrentCell() < Config.MAX_CELLS-1) {
      head.moveRight();
      repaint(); 
    }
    else System.err.println("The head cannot be moved further to the right.");
  }
  
  /**
   * Moves the head graphics to the previous cell of the tape.
   */
  public void moveLeft() {
    
    if(head.getCurrentCell() > 0) {
      head.moveLeft();
      repaint(); 
    }
    else System.err.println("The head cannot be moved further to the left.");
  }

  /**
   * Renders the head graphics.
   */
  @Override
  public void paintComponent(Graphics g) {
    
    super.paintComponent(g);
    
    // Gets the current position of the head graphics
    int headPos = head.getCurrentCell() * Config.CELL_WIDTH; 
    
    // Sets the font properties for the cells
    FontRenderContext fontRenderContext = new FontRenderContext(null, false, false);
    Rectangle2D stateBounds;
    g.setFont (new Font ("Dialog", 1, 16));
    
    // Draws the background
    g.setColor(new Color(203, 197, 197));
    g.fillRect(0, 0, Config.HEAD_X_END, Config.HEAD_HEIGHT);
    
    // Draws the head image (according to the current head position)
    g.drawImage(headImage, headPos, 0, null);
    
    // Draws the state label (according to the current head state)
    g.setColor(Color.black);
    stateBounds = g.getFont().getStringBounds(
      head.getCurrentState(), fontRenderContext
    );      
    g.drawString(
      head.getCurrentState(),       
      headPos + (Config.HEAD_WIDTH - (int)Math.ceil(stateBounds.getWidth()))/2,
      Config.HEAD_HEIGHT/2 + 20
    );
  }

  /**
   * This method allows the replacement of TM's heads.
   * @param head The abstract data type of a TM's head.
   */
  public void setHead(Head head) { this.head = head; }
}