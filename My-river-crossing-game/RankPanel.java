import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * <code>RankPanel</code> is used to show the ranfing list of each level.
 * 
 * @author Boyang Qu
 * @author Chao Yuan
 */
public class RankPanel extends JPanel {
    /**
     * private variable <code>panel</code> is used to implement singleton pattern
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
    private static RankPanel rankPanel = new RankPanel();

    private Font font = new Font("SansSerif", Font.BOLD, 15);
    private int rankNum = 9;
    private int[] nums = new int[rankNum + 1];
    public int userRecord[] = new int[3];
    private JLabel[] label = new JLabel[rankNum + 2];

    /**
     * Private constructor for this class to implement singleton pattern.
     */
    private RankPanel() {
        setLayout(new GridLayout(rankNum + 2, 1, 5, 10));
        font = new Font("SansSerif", Font.BOLD, 16);
        label[0] = new JLabel("Ranking List", JLabel.CENTER);
        label[0].setFont(font);
        add(label[0]);
        for (int i = 1; i < rankNum + 2; i++) {
            label[i] = new JLabel("", JLabel.CENTER);
            add(label[i]);
        }
        label[rankNum + 1].setForeground(Color.RED);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /**
     * Get the singleton of the <code>RankPanel</code> class
     * 
     * @return the singleton of the <code>RankPanel</code>
     */
    public static RankPanel getRankPanel() {
        return rankPanel;
    }

    /**
     * Change the Ranking list.
     * 
     * @param currentLevel The level want to change.
     */
    public void changeRankTo(int currentLevel) {
        for (int i = 1; i < rankNum + 1; i++) {
            label[i].setText("");
            label[i].setForeground(Color.BLACK);
        }
        label[rankNum + 1].setText("");
        getFileInformation(currentLevel);
        boolean hasShown = false;
        for (int i = 1; i <= rankNum && nums[i - 1] != 0; i++) { // only the first 9 records
            if (!hasShown && userRecord[currentLevel / 10] == nums[i - 1]) {

                label[i].setText("No." + i + ":  " + nums[i - 1] + "s");
                label[i].setFont(font);
                label[i].setForeground(Color.RED);
                hasShown = true;
                continue;
            }
            label[i].setText("No." + i + ":  " + nums[i - 1] + "s");
            label[i].setFont(font);
        }
        if (!hasShown && userRecord[currentLevel / 10] != 0) {
            label[rankNum + 1].setText("Your best time:  " + userRecord[currentLevel / 10] + "s, no rank");
            Font newFont = new Font("SansSerif", Font.BOLD, 10);
            label[rankNum + 1].setFont(newFont);
            hasShown = true;
        }
        setLocation(390, 230);
        setSize(160, 300);
        setOpaque(false);
    }

    /**
     * Write the new rank information into file.
     * 
     * @param time    The new user time want to be add.
     * @param fileNum The file of each level.
     */
    public void setFileInformation(int time, int fileNum) {
        nums[rankNum] = time;
        Arrays.sort(nums);
        File file = new File("src/Text/" + fileNum + ".txt");

        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            int offset = 0;
            for (int x : nums) {
                if (x == 0)
                    offset++;
                else
                    break;
            }
            for (int i = offset; i <= rankNum; i++) {
                if (i == rankNum && offset == 0) {
                    break;
                }
                bw.write(nums[i] + ",");
            }
            bw.close();
            fw.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        changeRankTo(fileNum);
    }

    /**
     * Get the rank information from file of each level.
     * 
     * @param fileNum The file of each level.
     */
    public void getFileInformation(int fileNum) {
        File file = new File("src/Text/" + fileNum + ".txt");
        Scanner l = null;
        try {
            l = new Scanner(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        l.useDelimiter(",");
        int i = rankNum;
        while (i-- > 0)
            nums[i] = 0;
        i = 0;
        while (l.hasNextInt()) {
            nums[i] = l.nextInt();
            i++;
        }
    }
}
