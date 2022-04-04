import javax.swing.JPanel;
import java.awt.*;

/**
 * <code>BackgroundPanel</code> is a child class of JPanel and Its main function
 * is to enable us to add background.
 * 
 * @author Yayuan Li
 * @author Chao Yuan
 */
class BackgroundPanel extends JPanel {
    /**
     * private variable <code>panel</code> is used to implement singleton pattern
     * here, and it has almost the same function to the other panel class.
     * 
     * @see BackgroundPanel
     * @see GamePanel
     * @see PlankStatePanel
     * @see player
     * @see SettingPanel
     * @see StartPanel
     * @see RankPanel
     */
    private Image image;

    /**
     * The constructor of code>BackgroundPanel</code> class. Image here is your
     * background
     * 
     * @param image background image in the <code>Image</code> format.
     */
    public BackgroundPanel(Image image) {
        this.image = image;
        this.setOpaque(true);// paints every pixel the same with the orignal one
        this.setSize(374 + 200, 600);
        this.setLocation(0, 0);
    }

    /**
     * Override the function in JComponent and draw the background of the panel
     * 
     * @param g Graphics that is used to as our background
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}