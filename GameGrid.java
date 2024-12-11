import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashSet;

public class GameGrid extends JPanel {

    private int size;
    private Player player = new Player("src/player.png",40, 40,20, 20);

    private Color bgColor = new Color(60, 100 ,100);

    public static HashSet<Rectangle> platforms = new HashSet<>();


    public GameGrid(int rows, int cols) throws IOException {

        this.size = rows/Display.getFrameHeight();
        System.out.println(size);
        this.setFocusable(true);
        this.addKeyListener(player);

    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        spawnPlatform(g, Color.green, new Rectangle(10, 100, 100, 10));
        spawnPlatform(g, Color.green, new Rectangle(80, 200, 100, 10));
        spawnPlatform(g, Color.orange, new Rectangle(50, 300, 50, 10));
        player.drawPlayer(g);
        //player.drawHitBox(g);
        updateUI();

    }



    private void spawnPlatform(Graphics g, Color c, Rectangle r){
        g.setColor(c);
        g.fillRect(r.x, r.y, r.width, r.height);
        platforms.add(r);
    }



    public void eraseSection(Graphics g, Rectangle r){

    }
}
