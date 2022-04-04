import javax.swing.JPanel;
import java.awt.*;

/**
 * <code>PlankStatePanel</code> is mainly used to show the state of the removed
 * plank. It's contained in the default panle of the <code>GameFrame</code>
 * frame and it includes a 5*5 GridLayout to show the plank.
 * 
 * @author Yayuan Li
 * @author Nuo Chen
 */
public class PlankStatePanel extends JPanel {
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
    private static PlankStatePanel panel = new PlankStatePanel();

    /**
     * Variable used to show removed plank's direction. (0: horizontal; 1: vertical)
     */
    private int plankDirection = -1;

    /**
     * Variable used to show removed plank's length. (the value can only be 1, 3, 5
     * in river crossing game, 0 means no plank is removed currently)
     */
    private int plankLength = 0;

    /**
     * The button array shown in the <code>GridLayout</code> and used to show the
     * state of the plank.
     */
    public MyButton buttons[][] = new MyButton[5][5];

    /**
     * Private constructor for this class to implement singleton pattern. Besides,
     * some initialization like set layout, location, size are also done here.
     */
    private PlankStatePanel() {
        setLayout(new GridLayout(5, 5));
        addButtons();
        setLocation(413, 75);
        setSize(120, 120);
        setOpaque(false);
    }

    /**
     * Get the singleton of the <code>PlankStatePanel</code> class
     * 
     * @return the singleton of the <code>PlankStatePanel</code>
     */
    public static PlankStatePanel getPlankStatePanel() {
        return panel;
    }

    /**
     * Give other class access to get the plank's direction
     * 
     * @return 0: horizontal; 1: vertical; -1: no initialize
     */
    public int getPlankDirection() {
        return plankDirection;
    }

    /**
     * Give other class access to change the plank's direction and show the change
     * by change buttons' icon.
     * 
     * @param i 0: horizontal; 1: vertical
     */
    public void setPlankDirection(int i) {
        plankDirection = i;
        // Exchange horizontal and vertical status
        if (plankLength == 1) {
            int tempState = buttons[2][2].getState();
            tempState = tempState == 1 ? 2 : 1;
            buttons[2][2].changeStateTo(tempState);
        } else {
            for (int m = 0; m < 5; m++) {
                exchangeState(buttons[2][m], buttons[m][2]);
            }
        }
    }

    /**
     * Give other class access to get the plank's length
     * 
     * @return 0: short plank; 3: middle plank; 5: long plank
     */
    public int getPlankLength() {
        return plankLength;
    }

    /**
     * Give other class access to change the plank's length and show the change by
     * change buttons' icon.
     * 
     * @param length the length of the removed plank 0: short plank; 3: middle
     *               plank; 5: long plank
     */
    public void setPlankLength(int length) {
        plankLength = length;
        // clean
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                buttons[i][j].changeStateTo(MyButton.TSP);
            }
        }
        // Growth in the same direction
        if (plankDirection == 0) {
            int startIndex = 2 - (length / 2);
            for (int i = startIndex; i < startIndex + length; i++) {
                buttons[2][i].changeStateTo(MyButton.PVH);
            }
        } else if (plankDirection == 1) {
            int startIndex = 2 - (length / 2);
            for (int i = startIndex; i < startIndex + length; i++) {
                buttons[i][2].changeStateTo(MyButton.PVV);
            }
        }
    }

    /**
     * Used to add all the buttons in the GrideLayout and it's used in the
     * constructor of the <code>PlankStatePanel</code>
     */
    private void addButtons() {
        // Add buttons in a 2D array
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                buttons[i][j] = new MyButton(i, j);
                buttons[i][j].changeStateTo(MyButton.TSP);
                add(buttons[i][j]);
            }
        }
    }

    /**
     * A useful function used to change two buttons's state or icon.
     * 
     * @param button1
     * @param button2
     */
    private void exchangeState(MyButton button1, MyButton button2) {
        int state1 = button1.getState();
        int state2 = button2.getState();
        if (state1 == 1)
            state1++;
        else if (state1 == 2)
            state1--;

        if (state2 == 1)
            state2++;
        else if (state2 == 2)
            state2--;
        button1.changeStateTo(state2);
        button2.changeStateTo(state1);
    }

    /**
     * Used to clean the <code>plankStatePanel</code>. And set the icon of all the
     * buttons in <code>GridePanel</code> to transparent.
     */
    public void clean() {
        for (int m = 0; m < 5; m++) {
            for (int j = 0; j < 5; j++) {
                buttons[m][j].changeStateTo(MyButton.TSP);
            }
        }
        plankDirection = -1;
        plankLength = 0;
    }
}
