/**
 * <code>Driver</code> Is use to start the game.
 * 
 * @author All group members
 */
public class Driver {
    public static void main(String args[]) {
        GameFrame frame = new GameFrame();
        while (true) {
            frame.pause(10);// pause 10 millisecond
            if (!SettingPanel.getSettingPanel().pause) {
                for (int i = 0; i < 6; i++) {
                    GameFrame.fish[i].move();
                }
                Player.getPlayer().setBounds(Player.getPlayer().getXPosition(), Player.getPlayer().getYPosition());
                Player.getPlayer().move();
                if (Player.getPlayer().getYPosition() == 43) {// Reach the shore, win the level
                    WinPanel.getWinPanel().setVisible(true);
                    Music win = new Music("src/Music/win.wav", false);// sound effect
                    win.run();
                    int currentLevel = GamePanel.getGamePanel().getLevel();
                    int time = Integer.parseInt(GameFrame.timer.getText());
                    RankPanel.getRankPanel().userRecord[currentLevel / 10] = time;// store the time record
                    RankPanel.getRankPanel().setFileInformation(time, currentLevel);
                    frame.pause(2000);
                    WinPanel.getWinPanel().setVisible(false);
                    GamePanel.getGamePanel().setNextLevel();// next level
                }
            }
        }
    }
}
