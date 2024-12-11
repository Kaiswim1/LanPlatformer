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

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Grid Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(new MultiplayerMenu());
        //frame.add(new GameGrid(400, 400)); Actual game
        frame.setVisible(true);
    }
}
