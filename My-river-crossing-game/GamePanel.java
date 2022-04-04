import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

/**
 * <code>GamePanel</code> is a child class of JPanel and the most important
 * panel for the game.
 * 
 * @author Chao Yuan
 */
public class GamePanel extends JPanel implements ActionListener {
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
    private static GamePanel panel = new GamePanel();
    /**
     * The sound effect.
     */
    private Music m;
    /**
     * The current level.
     */
    private int currentLevel = 1;
    /**
     * The rows of the game.
     */
    protected final int rows = 13;
    /**
     * The columns of the game.
     */
    protected final int columns = 9;
    /**
     * The 9*13 buttons of the game.
     */
    protected MyButton buttons[][] = new MyButton[rows][columns];

    /**
     * A flag for the <code>m</code> to judge when another acoustic comes and there
     * has acoustic, then interrupt it and start the new one.
     */
    protected int flag = 0;

    /**
     * Private constructor for this class to implement singleton pattern. Besides,
     * some initialization like set layout, location, and size are also initialized
     * here.
     */
    private GamePanel() {
        setLocation(0, 40);
        setSize(360, 520);
        setOpaque(false);
        setLayout(new GridLayout(rows, columns));
        addButtons();
    }

    /**
     * Get the singleton of the <code>GamePanel</code> class
     * 
     * @return the singleton of the <code>GamePanel</code>
     */
    public static GamePanel getGamePanel() {
        return panel;
    }

    /**
     * Init the 9*13 buttons for <code>GamePanel</code> class
     */
    private void addButtons() {
        // Add buttons in a 2D arrays
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                buttons[i][j] = new MyButton(i, j);// init with the position
                buttons[i][j].addActionListener(this);
                buttons[i][j].changeStateTo(MyButton.TSP);// a transparent backgroud
                add(buttons[i][j]);
            }
        }
    }

    /**
     * To pick and place the plank include the judgement of if a plank can be placed
     * or picked and play some sound effects.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Reset sound effect
        if (flag == 1) {
            m.pause();
            flag = 0;
        }
        PlankStatePanel plankStatePanel = PlankStatePanel.getPlankStatePanel();
        if (!SettingPanel.getSettingPanel().pause && e.getSource() instanceof MyButton) {
            MyButton b = (MyButton) e.getSource();
            int state = b.getState();
            // to get the button index place
            int x = b.getXIndex(), y = b.getYIndex();
            // to get the player recent index place
            int playerX = (Player.getPlayer().getXPosition() - 5) / 40;
            int playerY = (Player.getPlayer().getYPosition() - 43) / 40;

            int temLength = 0;// the length of plank
            int plankLength = PlankStatePanel.getPlankStatePanel().getPlankLength();
            // The clicked plank is Vertical
            if (state == MyButton.PVV) {
                // No plank in hand, is not moving, and the player is near the clicked plank
                if (plankLength == 0 && playerX == x && Math.abs(playerY - y) <= 5) {
                    boolean canPick = true;
                    // if the player near the plank
                    if (playerY < y) {
                        for (int i = playerY + 1; i < y; i++) {
                            if (buttons[i][x].getState() != MyButton.PVV) {
                                canPick = false;
                                break;
                            }
                        }
                    } else {
                        for (int i = playerY - 1; i > y; i--) {
                            if (buttons[i][x].getState() != MyButton.PVV) {
                                canPick = false;
                                break;
                            }
                        }
                    }
                    // if is moving
                    if (Player.getPlayer().isMoving)
                        canPick = false;

                    if (canPick) {
                        m = new Music("src/Music/wood_pick.wav", false);// acoustics
                        m.start();
                        flag = 1;
                        // show in the plankStatePanel
                        plankStatePanel.setPlankDirection(1);// Plank is vertical
                        for (int i = y; i < rows && buttons[i][x].getState() == MyButton.PVV; i++) {
                            temLength++;
                            buttons[i][x].changeStateTo(MyButton.TSP);
                        }
                        for (int i = y - 1; i > 0 && buttons[i][x].getState() == MyButton.PVV; i--) {
                            temLength++;
                            buttons[i][x].changeStateTo(MyButton.TSP);
                        }
                        plankStatePanel.setPlankLength(temLength);
                    } else {
                        m = new Music("src/Music/wrong.wav", false);// acoustics
                        m.start();
                        flag = 1;
                    }
                }
            }
            // The clicked plank is Horizontal
            else if (state == MyButton.PVH) {
                // No plank in hand, and the player is near the clicked plank
                if (plankLength == 0 && playerY == y && Math.abs(playerX - x) <= 5) {
                    boolean canPick = true;
                    // if the player near the plank
                    if (playerX < x) {
                        for (int i = playerX + 1; i < x; i++) {
                            if (buttons[y][i].getState() != MyButton.PVH) {
                                canPick = false;
                                break;
                            }
                        }
                    } else {
                        for (int i = playerX - 1; i > x; i--) {
                            if (buttons[y][i].getState() != MyButton.PVH) {
                                canPick = false;
                                break;
                            }
                        }
                    }
                    // if is moving
                    if (Player.getPlayer().isMoving)
                        canPick = false;
                    // show in the plankStatePanel
                    if (canPick) {
                        m = new Music("src/Music/wood_pick.wav", false);// acoustics
                        m.start();
                        flag = 1;
                        plankStatePanel.setPlankDirection(0);// Plank is horizontal
                        for (int j = x; j < rows && buttons[y][j].getState() == MyButton.PVH; j++) {
                            temLength++;
                            buttons[y][j].changeStateTo(MyButton.TSP);
                        }
                        for (int j = x - 1; j > 0 && buttons[y][j].getState() == MyButton.PVH; j--) {
                            temLength++;
                            buttons[y][j].changeStateTo(MyButton.TSP);
                        }
                        plankStatePanel.setPlankLength(temLength);
                    } else {
                        m = new Music("src/Music/wrong.wav", false);// acoustics
                        m.start();
                        flag = 1;
                    }
                }
            }
            // Place the Vertical plank
            else if (state == MyButton.PTV) {// Translucent plank
                m = new Music("src/Music/wood_put.wav", false);// acoustics
                m.start();
                flag = 1;

                for (int i = y; i < rows && buttons[i][x].getState() == MyButton.PTV; i++) {
                    buttons[i][x].changeStateTo(MyButton.PVV);
                }
                for (int i = y - 1; i > 0 && buttons[i][x].getState() == MyButton.PTV; i--) {
                    buttons[i][x].changeStateTo(MyButton.PVV);
                }
                plankStatePanel.setPlankLength(0);
                plankStatePanel.setPlankDirection(-1);
            }
            // Place the Horizontal plank
            else if (state == MyButton.PTH) {// Translucent plank
                // acoustics
                m = new Music("src/Music/wood_put.wav", false);
                m.start();
                flag = 1;

                for (int j = x; j < rows && buttons[y][j].getState() == MyButton.PTH; j++) {
                    buttons[y][j].changeStateTo(MyButton.PVH);
                }
                for (int j = x - 1; j > 0 && buttons[y][j].getState() == MyButton.PTH; j--) {
                    buttons[y][j].changeStateTo(MyButton.PVH);
                }
                plankStatePanel.setPlankLength(0);
                plankStatePanel.setPlankDirection(-1);
            }
            // click the stump
            else if (state == MyButton.STP) {
                if (canMoveTo(b)) {
                    Player.getPlayer().destinationX_Position = b.getXIndex() * 40 + 5;
                    Player.getPlayer().destinationY_Position = b.getYIndex() * 40 + 43;
                    // acoustics
                    m = new Music("src/Music/walk.wav", false);
                    m.start();
                    flag = 1;
                } else {
                    // acoustics
                    m = new Music("src/Music/wrong.wav", false);
                    m.start();
                    flag = 1;
                }
            }
            // click the water
            else if (state == MyButton.TSP) {
                // acoustics
                m = new Music("src/Music/water.wav", false);
                m.start();
                flag = 1;
            }
        }
    }

    /**
     * A pravite method for to judge if the player can move.
     */
    private boolean canMoveTo(MyButton b) {
        int x = (Player.getPlayer().getXPosition() - 5) / 40;
        int y = (Player.getPlayer().getYPosition() - 43) / 40;
        int destinationX = b.getXIndex();
        int destinationY = b.getYIndex();
        MyButton[][] buttons = GamePanel.getGamePanel().buttons;// reference
        // search left and right
        if (destinationY == y) {
            if (destinationX < x) {
                for (int i = destinationX + 1; i < x; i++) {
                    if (buttons[y][i].getState() != MyButton.PVH && buttons[y][i].getState() != MyButton.STP) {
                        return false;
                    }
                }
            } else if (destinationX > x) {
                for (int i = x + 1; i < destinationX; i++) {
                    if (buttons[y][i].getState() != MyButton.PVH && buttons[y][i].getState() != MyButton.STP) {
                        return false;
                    }
                }
            }
        }
        // search up and down
        else if (destinationX == x) {
            if (destinationY < y) {
                for (int i = destinationY + 1; i < y; i++) {
                    if (buttons[i][x].getState() != MyButton.PVV && buttons[i][x].getState() != MyButton.STP) {
                        return false;
                    }
                }
            } else if (destinationY > y) {
                for (int i = y + 1; i < destinationY; i++) {
                    if (buttons[i][x].getState() != MyButton.PVV && buttons[i][x].getState() != MyButton.STP) {
                        return false;
                    }
                }
            }
        } else
            return false;
        return true;
    }

    /**
     * Change to the next level. If it's the level 21, change to the level 1.
     */
    public void setNextLevel() {
        if (currentLevel == 1)
            setLevel(11);
        else if (currentLevel == 11)
            setLevel(21);
        else if (currentLevel == 21) {
            setLevel(1);
        }
    }

    /**
     * Set the game level.
     * 
     * @param level the level want to set
     */
    public void setLevel(int level) {
        if (level == 1) {
            // Remove all elements for last level
            for (int i = 0; i < 13; i++)
                for (int j = 0; j < 9; j++)
                    if (buttons[i][j].getState() != MyButton.TSP)
                        buttons[i][j].changeStateTo(MyButton.TSP);
            // Init player position of this level
            Player.getPlayer().setInitialPosition(2, 12);
            // Set timer to 0
            GameFrame.timer.setText("0");
            currentLevel = 1;
            // Change the Ranking List to this level
            RankPanel.getRankPanel().changeRankTo(level);
            // Remove the plank in hand
            PlankStatePanel.getPlankStatePanel().setPlankLength(0);
            PlankStatePanel.getPlankStatePanel().setPlankDirection(-1);
            // init fishes
            GameFrame.fish[0].setVisible(true);
            GameFrame.fish[1].setVisible(true);
            GameFrame.fish[2].setVisible(false);
            GameFrame.fish[3].setVisible(false);
            GameFrame.fish[4].setVisible(false);
            GameFrame.fish[5].setVisible(false);
            // Init stumps
            buttons[0][6].changeStateTo(MyButton.STP);
            buttons[4][6].changeStateTo(MyButton.STP);
            buttons[6][2].changeStateTo(MyButton.STP);
            buttons[6][6].changeStateTo(MyButton.STP);
            buttons[8][2].changeStateTo(MyButton.STP);
            buttons[12][2].changeStateTo(MyButton.STP);
            // Init blanks
            buttons[11][2].changeStateTo(MyButton.PVV);
            buttons[10][2].changeStateTo(MyButton.PVV);
            buttons[9][2].changeStateTo(MyButton.PVV);
            buttons[7][2].changeStateTo(MyButton.PVV);

        } else if (level == 11) {
            // Remove all elements for last level
            for (int i = 0; i < 13; i++)
                for (int j = 0; j < 9; j++)
                    if (buttons[i][j].getState() != MyButton.TSP)
                        buttons[i][j].changeStateTo(MyButton.TSP);
            // Init player position of this level
            Player.getPlayer().setInitialPosition(6, 12);
            // Set timer to 0
            GameFrame.timer.setText("0");
            currentLevel = 11;
            // Change the Ranking List to this level
            RankPanel.getRankPanel().changeRankTo(level);
            // Remove the plank in hand
            PlankStatePanel.getPlankStatePanel().setPlankLength(0);
            PlankStatePanel.getPlankStatePanel().setPlankDirection(-1);
            // init fishes
            GameFrame.fish[2].setVisible(true);
            GameFrame.fish[3].setVisible(true);
            GameFrame.fish[0].setVisible(false);
            GameFrame.fish[1].setVisible(false);
            GameFrame.fish[4].setVisible(false);
            GameFrame.fish[5].setVisible(false);
            // Init stumps
            buttons[0][4].changeStateTo(MyButton.STP);
            buttons[4][0].changeStateTo(MyButton.STP);
            buttons[4][4].changeStateTo(MyButton.STP);
            buttons[6][0].changeStateTo(MyButton.STP);
            buttons[6][2].changeStateTo(MyButton.STP);
            buttons[6][6].changeStateTo(MyButton.STP);
            buttons[6][8].changeStateTo(MyButton.STP);
            buttons[10][0].changeStateTo(MyButton.STP);
            buttons[10][4].changeStateTo(MyButton.STP);
            buttons[10][6].changeStateTo(MyButton.STP);
            buttons[12][6].changeStateTo(MyButton.STP);
            // Init blanks
            buttons[10][1].changeStateTo(MyButton.PVH);
            buttons[10][2].changeStateTo(MyButton.PVH);
            buttons[10][3].changeStateTo(MyButton.PVH);
            buttons[11][6].changeStateTo(MyButton.PVV);

        } else if (level == 21) {
            // Remove all elements for last level
            for (int i = 0; i < 13; i++)
                for (int j = 0; j < 9; j++)
                    if (buttons[i][j].getState() != MyButton.TSP)
                        buttons[i][j].changeStateTo(MyButton.TSP);
            // Init player position of this level
            Player.getPlayer().setInitialPosition(4, 12);
            // Set timer to 0
            GameFrame.timer.setText("0");
            currentLevel = 21;
            // Change the Ranking List to this level
            RankPanel.getRankPanel().changeRankTo(level);
            // Remove the plank in hand
            PlankStatePanel.getPlankStatePanel().setPlankLength(0);
            PlankStatePanel.getPlankStatePanel().setPlankDirection(-1);
            // init fishes
            GameFrame.fish[4].setVisible(true);
            GameFrame.fish[5].setVisible(true);
            GameFrame.fish[0].setVisible(false);
            GameFrame.fish[1].setVisible(false);
            GameFrame.fish[2].setVisible(false);
            GameFrame.fish[3].setVisible(false);
            // Init stumps
            buttons[0][4].changeStateTo(MyButton.STP);
            buttons[2][2].changeStateTo(MyButton.STP);
            buttons[2][6].changeStateTo(MyButton.STP);
            buttons[2][8].changeStateTo(MyButton.STP);
            buttons[4][0].changeStateTo(MyButton.STP);
            buttons[4][6].changeStateTo(MyButton.STP);
            buttons[6][0].changeStateTo(MyButton.STP);
            buttons[6][4].changeStateTo(MyButton.STP);
            buttons[6][8].changeStateTo(MyButton.STP);
            buttons[8][2].changeStateTo(MyButton.STP);
            buttons[8][4].changeStateTo(MyButton.STP);
            buttons[8][8].changeStateTo(MyButton.STP);
            buttons[10][0].changeStateTo(MyButton.STP);
            buttons[10][6].changeStateTo(MyButton.STP);
            buttons[12][4].changeStateTo(MyButton.STP);
            // Init blanks
            buttons[3][2].changeStateTo(MyButton.PVV);
            buttons[4][2].changeStateTo(MyButton.PVV);
            buttons[5][2].changeStateTo(MyButton.PVV);
            buttons[6][2].changeStateTo(MyButton.PVV);
            buttons[7][2].changeStateTo(MyButton.PVV);
            buttons[8][3].changeStateTo(MyButton.PVH);
            buttons[9][4].changeStateTo(MyButton.PVV);
            buttons[10][4].changeStateTo(MyButton.PVV);
            buttons[11][4].changeStateTo(MyButton.PVV);
        }
    }

    /**
     * Get the game level.
     * 
     * @return the crurrent level.
     */
    public int getLevel() {
        return currentLevel;
    }
}
