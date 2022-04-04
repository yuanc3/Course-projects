import javax.swing.*;
import java.awt.*;

/**
 * <code>Fish</code> is a child class of JPanel and its main function is to add
 * some fish to the background
 * 
 * @author Yanbo Wang
 */
class Fish extends JPanel {
    private Image img;
    private int xPosition;
    private int yPosition;
    private int width;
    private int height;
    private int speed;
    private int xBound;
    private int yBound;

    /**
     * The constructor for <code>Fish</code> class.
     * 
     * @param xPosition the x position of fish.
     * @param yPosition the y position of fish.
     * @param width     the width of fish.
     * @param height    the height of fish.
     * @param speed     the speeed of fish.
     * @param lBound    the left bond position of fish.
     * @param rBound    the right bond position of fish.
     */
    public Fish(int xPosition, int yPosition, int width, int height, int speed, String s, int lBound, int rBound) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.speed = speed;
        if (s.equals("Right"))
            img = new ImageIcon("src/fish-.png").getImage();
        else if (s.equals("LEFT"))
            img = new ImageIcon("src/fish.png").getImage();
        this.xBound = lBound;
        this.yBound = rBound;
        setOpaque(true);
        setBounds(xPosition, yPosition, width, height);

    }

    /**
     * Override the function in JComponent and draw the background of the panel.
     * 
     * @param g Graphics that is used to as our background.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    /**
     * Get the x position of fish.
     * 
     * @return <code>xPosition</code>
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * Get the y position of fish.
     * 
     * @return <code>yPosition</code>
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * Get the width of fish.
     * 
     * @return <code>width</code>
     */
    public int getWidth_() {
        return width;
    }

    /**
     * Get the height of fish.
     * 
     * @return <code>height</code>
     */
    public int getHeight_() {
        return height;
    }

    /**
     * Get the speed of fish.
     * 
     * @return <code>speed</code>
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Set the new <code>xPosition</code> of fish.
     * 
     * @param x The new x position of the fish.
     */
    public void setXPosition(int x) {
        xPosition = x;
    }

    /**
     * Set the <code>yPosition</code> of fish.
     * 
     * @param y The y position of the fish.
     */
    public void setYPosition(int y) {
        yPosition = y;
    }

    /**
     * Set the <code>speed</code> of fish.
     * 
     * @param s The new speed of the fish.
     */
    public void setSpeed(int s) {
        speed = s;
    }

    /**
     * Move the fish.
     */
    public void move() {
        setBounds(getXPosition(), getYPosition(), getWidth_(), getHeight_());
        setXPosition(getXPosition() + getSpeed());
        if (getXPosition() >= yBound) {
            img = new ImageIcon("src/fish.png").getImage();
            setSpeed(-getSpeed());
        }
        if (getXPosition() <= xBound) {
            img = new ImageIcon("src/fish-.png").getImage();
            setSpeed(-getSpeed());
        }

    }
}