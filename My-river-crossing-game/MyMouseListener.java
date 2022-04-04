import java.awt.event.*;

/**
 * <code>MyMouseListener</code> is used to add some mouse function
 * 
 * @author Chao Yuan
 * @author Yanbo Wang
 */
class MyMouseListener implements MouseListener {
    /**
     * Flag for judgment process
     */
    private int lastFlag1 = -1, lastFlag2 = -1;
    /**
     * Last show position of plank in hand
     */
    private int lastXPosition = -1, lastYPosition = -1;
    /**
     * Acoustic
     */
    private Music m;
    /**
     * A flag for the <code>m</code> to judge if when another acoustic comes and
     * there has acoustic, then interrupt it and start the new one.
     */
    private int flag;

    /**
     * Detect mouse click.
     * 
     * @param e The MouseEvent.
     */
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        // When right click, rotate plank.
        PlankStatePanel plankStatePanel = PlankStatePanel.getPlankStatePanel();
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (plankStatePanel.getPlankDirection() == 0) {
                plankStatePanel.setPlankDirection(1);
                m = new Music("src/Music/rotate.wav", false);
                if (flag == 1) {
                    m.pause();
                    flag = 0;
                }
                m.start();
            } else if (plankStatePanel.getPlankDirection() == 1) {
                plankStatePanel.setPlankDirection(0);
                m = new Music("src/Music/rotate.wav", false);
                if (flag == 1) {
                    m.pause();
                    flag = 0;
                }
                m.start();
            }
        }
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * Detect mouse entered, if entered, then check if can place plank.
     * 
     * @param e The MouseEvent.
     */
    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        // If the mouse move to a place where can place plank, display it translucent.
        PlankStatePanel plankStatePanel = PlankStatePanel.getPlankStatePanel();
        GamePanel gamePanel = GamePanel.getGamePanel();
        if (e.getSource() instanceof MyButton) {
            MyButton b = (MyButton) e.getSource();
            int state = b.getState();
            // get the position
            int x = b.getXIndex();
            int y = b.getYIndex();
            int playerX = (Player.getPlayer().getXPosition() - 5) / 40;
            int playerY = (Player.getPlayer().getYPosition() - 43) / 40;
            int temLength = -1;
            boolean canPut = true;// If the plank can be put
            int flag1 = -1, flag2 = -1;
            // The Vertical direction of the plank in hand
            if (plankStatePanel.getPlankDirection() == 1 && state == MyButton.TSP) {
                int i;
                /* Search down */
                for (i = y; temLength <= 5 && i < gamePanel.rows
                        && gamePanel.buttons[i][x].getState() == MyButton.TSP; i++) {
                    temLength++;
                    flag2 = i;
                }
                if (i >= gamePanel.rows)// Search to the boundary
                    canPut = false;
                /* Search up */
                for (i = y; temLength <= 5 && i >= 0 && gamePanel.buttons[i][x].getState() == MyButton.TSP; i--) {
                    temLength++;
                    flag1 = i;
                }
                if (flag1 == -1 || i < 0)// Search to the boundary
                    canPut = false;

                if (canPut && (gamePanel.buttons[flag1 - 1][x].getState() != MyButton.STP
                        || gamePanel.buttons[flag2 + 1][x].getState() != MyButton.STP))
                    canPut = false;
                /* Place plank */
                if (canPut && temLength == plankStatePanel.getPlankLength()) {
                    if (playerX == x && (playerY == flag1 - 1 || playerY == flag2 + 1)) {
                        for (i = flag1; i <= flag2; i++) {
                            gamePanel.buttons[i][x].changeStateTo(MyButton.PTV);
                        }
                        lastFlag1 = flag1;
                        lastFlag2 = flag2;
                        lastXPosition = x;
                    }
                }
            }
            // The Horizontal direction of the plank in hand
            else if (plankStatePanel.getPlankDirection() == 0 && state == MyButton.TSP) {
                int i;
                /* Search right */
                for (i = x; temLength <= 5 && i < gamePanel.columns
                        && gamePanel.buttons[y][i].getState() == MyButton.TSP; i++) {
                    temLength++;
                    flag2 = i;
                }
                if (i >= gamePanel.columns)// Search to the boundary
                    canPut = false;

                /* Search left */
                for (i = x; temLength <= 5 && i >= 0 && gamePanel.buttons[y][i].getState() == MyButton.TSP; i--) {
                    temLength++;
                    flag1 = i;
                }
                if (flag1 == -1 || i < 0)// Search to the boundary
                    canPut = false;
                if (canPut && (gamePanel.buttons[y][flag1 - 1].getState() != MyButton.STP
                        || gamePanel.buttons[y][flag2 + 1].getState() != MyButton.STP))
                    canPut = false;
                /* Place plank */
                if (canPut && temLength == plankStatePanel.getPlankLength()) {
                    // The plank placing place is near the player
                    if (playerY == y && (playerX == flag1 - 1 || playerX == flag2 + 1)) {
                        for (i = flag1; i <= flag2; i++) {
                            gamePanel.buttons[y][i].changeStateTo(MyButton.PTH);
                        }
                        lastFlag1 = flag1;
                        lastFlag2 = flag2;
                        lastYPosition = y;
                    }
                }
            }
        }
    }

    /**
     * Detect mouse exited, if exited, then clear the entered effects.
     * 
     * @param e The MouseEvent.
     */
    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        // Mouse move away, cancel display
        if (e.getSource() instanceof MyButton) {
            MyButton b = (MyButton) e.getSource();
            int state = b.getState();
            // Vertical situation
            if (state == MyButton.PTV) {
                for (int i = lastFlag1; i <= lastFlag2; i++) {
                    GamePanel.getGamePanel().buttons[i][lastXPosition].changeStateTo(MyButton.TSP);
                }
                lastFlag1 = -1;
                lastFlag2 = -1;
                lastXPosition = -1;
            }
            // Horizontal situation
            else if (state == MyButton.PTH) {
                for (int i = lastFlag1; i <= lastFlag2; i++) {
                    GamePanel.getGamePanel().buttons[lastYPosition][i].changeStateTo(MyButton.TSP);
                }
                lastFlag1 = -1;
                lastFlag2 = -1;
                lastYPosition = -1;
            }
        }
    }
}