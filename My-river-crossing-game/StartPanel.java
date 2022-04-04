import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.*;

/**
 * <code>StartPanel</code> is used to show the rules of the game in the
 * begining.
 * 
 * @author Xianda Wang
 */
public class StartPanel extends BackgroundPanel implements ActionListener {
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
    private static StartPanel panel = new StartPanel(new ImageIcon("src/startBck.png").getImage());
    private JButton StartButton = new JButton();
    public boolean hasStarted = false;

    /**
     * Private constructor for this class to implement singleton pattern.
     * 
     * @param image the background of the panel.
     */
    private StartPanel(Image image) {
        super(image);
        setLayout(null);
        setButton();
    }

    /**
     * Get the singleton of the <code>StartPanel</code> class.
     * 
     * @return The singleton of the <code>StartPanel</code>
     */
    public static StartPanel getStartPanel() {
        return panel;
    }

    /**
     * Set the <code>StartButton</code> of the <code>StartPanel</code> class.
     */
    private void setButton() {
        StartButton.setLocation(230, 500);
        StartButton.setSize(120, 50);
        StartButton.setBorderPainted(false);
        StartButton.addActionListener(this);
        StartButton.setIcon(new ImageIcon(new ImageIcon("src/start.png").getImage()
                .getScaledInstance(StartButton.getWidth(), StartButton.getHeight(), 1)));
        this.add(StartButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GamePanel gamePanel = GamePanel.getGamePanel();
        if (e.getSource() == StartButton) {
            gamePanel.setLevel(1);
            setVisible(false);
            // init the music
            GameFrame.bgm = new Music("src/Music/bgm.wav", true);
            GameFrame.bgm.start();
            hasStarted = true;
        }
    }
}
