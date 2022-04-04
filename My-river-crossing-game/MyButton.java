import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * <code>MyButton</code> is a basic class widely used in this game. It retains
 * some basic function from the <code>JButton</code> and extends some new useful
 * function here.
 * 
 * @author Yayuan Li
 * @author Chao Yuan
 * @author Nuo Chen
 */
public class MyButton extends JButton {

    // Define some basic variable for convenience

    /**
     * 'Transparent' background of <code>MyButton</code>
     */
    public static final int TSP = 0;
    /**
     * 'Visible and horizontal plank' background of <code>MyButton</code>
     */
    public static final int PVH = 1;
    /**
     * 'Visible and vertical plank' background of <code>MyButton</code>
     */
    public static final int PVV = 2;
    /**
     * 'Translucent and horizontal plank' background of <code>MyButton</code>
     */
    public static final int PTH = 3;
    /**
     * 'Translucent and vertical plank' background of <code>MyButton</code>
     */
    public static final int PTV = 4;
    /**
     * 'Stump' background of <code>MyButton</code>
     */
    public static final int STP = 5;

    private int state = 0;
    private int x = 0, y = 0;

    /**
     * Construction of MyButton class and do some basic initalize here. besides
     * receiving parameters and record its corresponding grid coordinates.
     * 
     * @param i row in grid coordinates
     * @param j column in grid coordinates
     * @see #setContentAreaFilled(boolean)
     * @see #setBorderPainted(boolean)
     * @see #changeStateTo(int)
     * @see #addMouseListener(java.awt.event.MouseListener)
     */
    public MyButton(int i, int j) {
        super();
        x = j;
        y = i;
        // init a transparent button
        setContentAreaFilled(false);
        setBorderPainted(false);// don't paint the edges of button
        changeStateTo(MyButton.TSP);
        addMouseListener(new MyMouseListener());
    }

    /**
     * Give other class ability to access its state
     * 
     * @return current button's state
     */
    public int getState() {
        return state;
    }

    /**
     * Give other class ability to access its corresponding grid coordinates
     * 
     * @return current button's X-coordinate in Grid
     */
    public int getXIndex() {
        return x;
    }

    /**
     * Give other class ability to access its corresponding grid coordinates
     * 
     * @return current button's Y-coordinate in Grid
     */
    public int getYIndex() {
        return y;
    }

    /**
     * Change button's state or icon based on the incoming parameters
     * 
     * @param state The state of the button
     */
    public void changeStateTo(int state) {
        switch (state) {
            case TSP:
                this.state = TSP;
                setIcon(new ImageIcon("src/TSP.png"));
                break;
            case PVH:
                this.state = PVH;
                // Picture adaptive button size.
                setIcon(new ImageIcon(new ImageIcon("src/PVH.png").getImage().getScaledInstance(super.getWidth(),
                        super.getHeight(), 1)));
                break;
            case PVV:
                this.state = PVV;
                // Picture adaptive button size.
                setIcon(new ImageIcon(new ImageIcon("src/PVV.png").getImage().getScaledInstance(super.getWidth(),
                        super.getHeight(), 1)));
                break;
            case PTH:
                this.state = PTH;
                // Picture adaptive button size.
                setIcon(new ImageIcon(new ImageIcon("src/PTH.png").getImage().getScaledInstance(super.getWidth(),
                        super.getHeight(), 1)));
                break;
            case PTV:
                this.state = PTV;
                // Picture adaptive button size.
                setIcon(new ImageIcon(new ImageIcon("src/PTV.png").getImage().getScaledInstance(super.getWidth(),
                        super.getHeight(), 1)));
                break;
            case STP:
                this.state = STP;
                // Picture adaptive button size.
                setIcon(new ImageIcon(new ImageIcon("src/STP.png").getImage().getScaledInstance(super.getWidth(),
                        super.getHeight(), 1)));
                break;
            default:
                System.err.println("ERROR");
        }
    }
}