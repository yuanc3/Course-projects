import javax.swing.*;
import java.awt.*;

/**
 * <code>Player</code> is the class for the player.
 * 
 * @author Yanbo Wang
 */
class Player extends JPanel {
    /**
     * private variable <code>panel</code> is used to implement singleton pattern
     * here, and it has almost the same function to the other panel class.
     * 
     * @see BackgroundPanel
     * @see GamePanel
     * @see PlankStatePanel
     * @see Player
     * @see SettingPanel
     * @see StartPanel
     */
    private static Player player = new Player();;

    private Image img;
    /**
     * Current x position of the player.
     */
    private int xPosition;
    /**
     * Current y position of the player.
     */
    private int yPosition;
    private int width;
    private int height;
    /**
     * Current x destination of the player.
     */
    protected int destinationX_Position;
    /**
     * Current y destination of the player.
     */
    protected int destinationY_Position;
    /**
     * If the player is moving
     */
    protected boolean isMoving = false;

    /**
     * Private constructor for this class to implement singleton pattern.
     */
    private Player() {
        width = 30;
        height = 30;
        img = new ImageIcon("src/player-back.png").getImage();
        setOpaque(true);
    }

    /**
     * Get the singleton of the <code>Player</code> class
     * 
     * @return the singleton of the <code>Player</code>
     */
    public static Player getPlayer() {
        return player;
    }

    /**
     * Set the player initial position of each level.
     * 
     * @param initialX The initial x position of each level.
     * @param initialy The initial y position of each level.
     */
    public void setInitialPosition(int initialX, int initialY) {
        xPosition = 5 + 40 * initialX;
        yPosition = 43 + 40 * initialY;
        destinationX_Position = xPosition;
        destinationY_Position = yPosition;
        setBounds(xPosition, yPosition);
    }

    /**
     * Set the player position.
     * 
     * @param xPosition The new x position of the player.
     * @param yPosition The new y position of the player.
     */
    public void setBounds(int xPosition, int yPosition) {
        setBounds(xPosition, yPosition, width, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    /**
     * Get the player <code>xPosition</code>.
     * 
     * @return The x position of the player.
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * Get the player <code>yPosition</code>.
     * 
     * @return The y position of the player.
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * Move the player.
     */
    public void move() {
        int speed = 10;
        if (destinationX_Position < xPosition) {
            img = new ImageIcon("src/player-left.png").getImage();
            xPosition -= speed;
            isMoving = true;
        } else if (destinationX_Position > xPosition) {
            img = new ImageIcon("src/player-right.png").getImage();
            xPosition += speed;
            isMoving = true;
        } else if (destinationY_Position < yPosition) {
            img = new ImageIcon("src/player-front.png").getImage();
            yPosition -= speed;
            isMoving = true;
        } else if (destinationY_Position > yPosition) {
            img = new ImageIcon("src/player-back.png").getImage();
            yPosition += speed;
            isMoving = true;
        } else
            isMoving = false;
    }
}