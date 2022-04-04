import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.event.*;

/**
 * <code>GameFrame</code> is the main/only frame in river crossing game, it
 * includes other panel like <code>GamePanel</code>, <code>SettingPanel</code>
 * and so on. Also, some small component like timer and setting button are
 * laying on it as well.
 * 
 * @author All the group members.
 */
public class GameFrame extends JFrame {

    private BackgroundPanel backgroundPanel;// background panel
    private GamePanel gamePanel;// main panel for game
    private SettingPanel settingPanel;// a panel for some settings and change level
    private JButton settingButton;// a button to control settingPanel;
    private Player player;// the player
    private StartPanel startPanel;// first panel with rules
    private WinPanel winPanel;// when pass a level shows
    private PlankStatePanel plankStatePanel;// show the plank in the player's hand
    private RankPanel rankPanel;// show the ranking list

    protected static boolean winPanelVisible = false; // control if winPanel visible
    protected static boolean settingPanelVisible = false;// control if settingPanel visible
    protected static JLabel timer;// record playing time in each level
    protected static Fish[] fish = new Fish[10];
    protected static long bgmTime;// the time of bgm
    protected static Music bgm;

    /**
     * Constructor of <code>GameFrame</code>, init everything here.
     */
    public GameFrame() {
        // init
        setTitle("River-crossing Game!");
        setLayout(null);

        startPanel = StartPanel.getStartPanel();
        add(startPanel);

        winPanel = WinPanel.getWinPanel();
        add(winPanel);
        WinPanel.getWinPanel().setVisible(false);

        settingButton = new JButton();
        settingPanel = SettingPanel.getSettingPanel();
        setSetting();

        player = Player.getPlayer();
        add(player);

        fish[0] = new Fish(70, 90, 35, 15, 2, "RIGHT", 70, 150);
        fish[1] = new Fish(270, 390, 35, 15, 1, "RIGHT", 180, 320);
        fish[2] = new Fish(70, 160, 35, 15, 1, "RIGHT", 30, 220);
        fish[3] = new Fish(90, 390, 35, 15, 2, "RIGHT", 90, 320);
        fish[4] = new Fish(70, 90, 35, 15, 2, "RIGHT", 70, 150);
        fish[5] = new Fish(260, 370, 35, 15, 1, "RIGHT", 220, 280);
        add(fish[0]);
        add(fish[1]);
        add(fish[2]);
        add(fish[3]);
        add(fish[4]);
        add(fish[5]);

        timer = new JLabel();
        setTimer();

        gamePanel = GamePanel.getGamePanel();
        add(gamePanel);

        plankStatePanel = PlankStatePanel.getPlankStatePanel();
        add(plankStatePanel);

        rankPanel = RankPanel.getRankPanel();
        add(rankPanel);

        Image bck = new ImageIcon("src/bkg.png").getImage();
        backgroundPanel = new BackgroundPanel(bck);
        add(backgroundPanel);

        addMouseListener(new MyMouseListener());

        setSize(374 + 200, 635);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Pause the game.
     * 
     * @param s Time for pausing the game
     */
    public void pause(int s) {
        try {
            Thread.sleep(s);// 1 millisecond
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize timer, set its location, size, font, add it to the frame. Besides,
     * by using timer's actionlistener and <code>Timer</code> class provided in
     * javax.swing, our timer can change one number per second
     */
    private void setTimer() {
        timer.setLocation(300, 10);
        timer.setSize(60, 20);
        timer.setOpaque(false);
        timer.setFont(new java.awt.Font("Dialog", 1, 20));// 20 is the font size
        add(timer);
        ActionListener taskPerformer = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (StartPanel.getStartPanel().hasStarted && settingPanel.pause == false) {
                    Integer time = Integer.parseInt(timer.getText());
                    time++;
                    timer.setText(time.toString());
                }
            }
        };
        new Timer(1000, taskPerformer).start();
    }

    /**
     * Do some initalization for the <code>settingButton</code>, like set its
     * location, size and icon. Also, add an actionListener to it to open and close
     * setting window.
     */
    private void setSetting() {
        // button
        settingButton.setLocation(5, 5);
        settingButton.setSize(30, 30);

        settingButton.setIcon(new ImageIcon(new ImageIcon("src/setting.png").getImage()
                .getScaledInstance(settingButton.getWidth(), settingButton.getHeight(), 1)));

        settingButton.setContentAreaFilled(false);
        settingButton.setBorderPainted(false);
        add(settingButton);

        // panel
        settingPanel.setLocation(0, 0);
        settingPanel.setSize(374 + 200, 635);
        settingPanel.setVisible(false);
        add(settingPanel);
        settingButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (settingPanelVisible) {
                    closeSetting();
                    bgm.start();
                    bgm.setTime(bgmTime);
                } else if (winPanelVisible) {
                    bgmTime = bgm.getTime();
                    bgm.pause();

                    bgm = new Music("src/Music/win.wav", true);

                    winPanel.setVisible(true);
                    winPanelVisible = true;
                } else {
                    // To realize the following pause
                    bgmTime = bgm.getTime();
                    bgm.pause();

                    bgm = new Music("src/Music/bgm.wav", true);

                    settingPanel.setVisible(true);
                    settingPanelVisible = true;
                    settingPanel.pause = true;
                }

            }
        });
    }

    /**
     * Close the setting panel.
     */
    public static void closeSetting() {
        SettingPanel.getSettingPanel().setVisible(false);
        settingPanelVisible = false;
        SettingPanel.getSettingPanel().pause = false;
        PlankStatePanel.getPlankStatePanel().setVisible(true);
        timer.setVisible(true);
    }
}
