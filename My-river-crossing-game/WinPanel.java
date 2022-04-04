import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * <code>WinPanel</code> is used to show that you pass the level.
 * 
 * @author Xianda Wang
 * @author Nuo Chen
 */
public class WinPanel extends BackgroundPanel {
    private static WinPanel panel;

    /**
     * Private constructor for this class to implement singleton pattern. Besides,
     * some initialization like set layout, location, size are also done here.
     */
    private WinPanel(Image image) {
        super(image);
        setLayout(null);
        setSize(300, 300);
        setLocation(137, 168);
    }

    /**
     * Get the singleton of the <code>WinPanel</code> class
     * 
     * @return the singleton of the <code>WinPanel</code>
     */
    public static WinPanel getWinPanel() {
        if (panel == null) {
            Image bck = new ImageIcon("src/win.png").getImage();
            panel = new WinPanel(bck);
        }
        return panel;
    }
}
