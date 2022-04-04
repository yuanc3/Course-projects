import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.*;
import java.awt.event.*;

/**
 * <code>SettingPanel</code> is used to show setting window in river crossing
 * game. Generally it includes some buttons to choose level, restart game or
 * continue the game.
 * 
 * @author Boyang Qu
 * @author Yayuan Li
 * @author Xianda Wang
 */
public class SettingPanel extends BackgroundPanel implements ActionListener {
    /**
     * Private variable <code>panel</code> is used to implement singleton pattern
     * here, and it has almost the same function to the other panel class.
     * 
     * @see BackgroundPanel
     * @see GamePanel
     * @see PlankStatePanel
     * @see Player
     * @see SettingPanel
     * @see StartPanel
     * @see RankPanel
     */
    private static SettingPanel panel;

    /**
     * Get a singleton of the <code>plankStatePanel</code>, so that we can clean it
     * when change the level.
     */
    private PlankStatePanel plankStatePanel = PlankStatePanel.getPlankStatePanel();

    /**
     * A button used to choose the first level
     */
    private JButton levelButton1 = new JButton("Level 1");

    /**
     * A button used to choose the eleventh level
     */
    private JButton levelButton11 = new JButton("Level 11");

    /**
     * A button used to choose the Twenty one level
     */
    private JButton levelButton21 = new JButton("Level 21");

    /**
     * A button used to restart the current level of the game.
     */
    private JButton restartButton = new JButton("RESTART");

    /**
     * A button used to close the setting and continue the game
     */
    private JButton continuetButton = new JButton("CONTINUE");

    /**
     * A var used for convenience to calculate buttons' position.
     */
    private int buttonXPosition = 127;

    /**
     * Used to judge whether the game need to be paused. this var is also used by
     * the <code>Driver</code> and <code>GameFrame</code> class to make it work.
     */
    public boolean pause = false;

    /**
     * Private constructor for this class to implement singleton pattern.
     * 
     * @param image the background of the setting.
     */
    private SettingPanel(Image image) {
        super(image);
        setLayout(null);
        setButton();
    };

    /**
     * Get the singleton of the <code>SettingPanel</code> class and do some basic
     * initialization here.
     * 
     * @return The singleton of the <code>SettingPanel</code>
     */
    public static SettingPanel getSettingPanel() {
        if (panel == null) {
            Image bck = new ImageIcon("src/Translucent-background.png").getImage();
            panel = new SettingPanel(bck);
        }
        return panel;
    }

    /**
     * Used to set all the buttons in the settingpanel, set their position, size and
     * then add them to the panel.
     */
    private void setButton() {
        levelButton1.setLocation(buttonXPosition, 400);
        levelButton1.setSize(100, 30);
        levelButton1.addActionListener(this);
        levelButton11.setLocation(buttonXPosition + 110, 400);
        levelButton11.setSize(100, 30);
        levelButton11.addActionListener(this);
        levelButton21.setLocation(buttonXPosition + 220, 400);
        levelButton21.setSize(100, 30);
        levelButton21.addActionListener(this);
        restartButton.setLocation(buttonXPosition + 60, 200);
        restartButton.setSize(200, 50);
        restartButton.addActionListener(this);
        continuetButton.setLocation(buttonXPosition + 60, 300);
        continuetButton.setSize(200, 50);
        continuetButton.addActionListener(this);

        add(levelButton1);
        add(levelButton11);
        add(levelButton21);
        add(restartButton);
        add(continuetButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GamePanel gamePanel = GamePanel.getGamePanel();
        int currentLevel;
        if (e.getSource() == levelButton1) {
            plankStatePanel.clean();
            currentLevel = 1;
            gamePanel.setLevel(currentLevel);
            GameFrame.closeSetting();
            GameFrame.bgm.start();
            GameFrame.bgm.setTime(GameFrame.bgmTime);
        } else if (e.getSource() == levelButton11) {
            plankStatePanel.clean();
            currentLevel = 11;
            gamePanel.setLevel(currentLevel);
            GameFrame.closeSetting();
            GameFrame.bgm.start();
            GameFrame.bgm.setTime(GameFrame.bgmTime);
        } else if (e.getSource() == levelButton21) {
            plankStatePanel.clean();
            currentLevel = 21;
            gamePanel.setLevel(currentLevel);
            GameFrame.closeSetting();
            GameFrame.bgm.start();
            GameFrame.bgm.setTime(GameFrame.bgmTime);
        } else if (e.getSource() == restartButton) {
            currentLevel = GamePanel.getGamePanel().getLevel();
            gamePanel.setLevel(currentLevel);
            plankStatePanel.clean();
            GameFrame.closeSetting();
            GameFrame.bgm.start();
            GameFrame.bgm.setTime(GameFrame.bgmTime);
        } else if (e.getSource() == continuetButton) {
            GameFrame.closeSetting();
            GameFrame.bgm.start();
            GameFrame.bgm.setTime(GameFrame.bgmTime);
        }
    }
}
