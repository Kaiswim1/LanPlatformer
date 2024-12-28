import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Display extends JPanel {
    private static int frameHeight = 400;
    private static int frameWidth = 400;

    public static int getFrameHeight(){
        return frameHeight;
    }
    public static int getFrameWidth(){
        return frameWidth;
    }
    private static JFrame frame;

    private static MultiplayerMenu multiplayerMenu = new MultiplayerMenu();
    public static JFrame getGameFrame(){return frame;}

    public static void startGameplay() throws IOException {
        GameGrid gameGrid = new GameGrid(); // Create the game grid
        frame.add(gameGrid);           // Add the game grid
        gameGrid.requestFocusInWindow(); // Ensure focus on GameGrid
    }

    public static void main(String[] args) throws IOException {
        frame = new JFrame("Grid Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(multiplayerMenu);
        //frame.add(new GameGrid(400, 400)); Actual game
        frame.setVisible(true);
    }
}
