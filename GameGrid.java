import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class GameGrid extends JPanel {

    private int size;

    private Color bgColor = new Color(60, 100 ,100);

    public static HashSet<Rectangle> platforms = new HashSet<>();






    public GameGrid() throws IOException {
        this.setFocusable(true);
        this.addKeyListener(ServerIO.connectedPlayers.get(0));
    }


    private void drawAllOtherPlayers(Graphics g) throws IOException {
        int i = 1;
        for(Player p : ServerIO.connectedPlayers){
            try{
                p.getRow();
            }catch(NumberFormatException n){
                continue;
            }
            if(!p.equals(null) && i != MultiplayerMenu.getPlayerNum()){
                g.drawImage(p.getSprite(),40, 40, ServerIO.getData("p"+i+"x"), ServerIO.getData("p"+i+"y"), null);
            }
            i++;
        }
    }





    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        spawnPlatform(g, Color.green, new Rectangle(10, 100, 100, 10));
        spawnPlatform(g, Color.green, new Rectangle(80, 200, 100, 10));
        spawnPlatform(g, Color.orange, new Rectangle(50, 300, 50, 10));
        ServerIO.connectedPlayers.get(0).drawPlayer(g);
        try {
            drawAllOtherPlayers(g);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
