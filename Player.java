import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class Player implements KeyListener, Runnable {
    private BufferedImage sprite;
    private Rectangle hitBox;
    private int fallSpeed;
    private int moveSpeed = 3;
    private int width;
    private int height;
    private int row;
    private int col;
    private final HashSet<Integer> pressedKeys = new HashSet<>();
    private Color hitboxColor = new Color(255, 0, 0, 60);

    public Player(String spritePath, int width, int height, int row, int col) throws IOException {
        this.sprite = ImageIO.read(new File(spritePath));
        this.width = width;
        this.height = height;
        this.row = row;
        this.col = col;
        this.hitBox = new Rectangle(this.col + 9, this.row + 4, this.width - 20, this.height - 6);
        new Thread(this).start();
    }

    public void drawPlayer(Graphics g) {
        g.drawImage(sprite, this.col, this.row, this.width, this.height, null);
    }

    public void drawHitBox(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
        g.setColor(hitboxColor);
        g.fillRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

    private boolean isTouchingPlatform() {
        for (Rectangle r : GameGrid.platforms) {
            if (hitBox.intersects(r)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Typed");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Allow jumping only if the player is on a platform and the spacebar is pressed
        if (e.getKeyCode() == KeyEvent.VK_SPACE && isTouchingPlatform()) {
            fallSpeed = -10; // Apply jump force
            updatePlayerFall();
        }
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    private void updatePlayerFall() {
        this.row += fallSpeed;
        hitBox.y += fallSpeed;
    }

    private void updatePlayerMove() {
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            this.col -= moveSpeed;
            hitBox.x -= moveSpeed;
        }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            this.col += moveSpeed;
            hitBox.x += moveSpeed;
        }
    }

    private void nudgeUpHelper(int amount) {
        this.row+=amount;
        this.hitBox.y+=amount;
    }

    private void nudgeUp() {
        while (isTouchingPlatform()) {
            this.fallSpeed = 0; // Stop falling when touching the platform
            nudgeUpHelper(-1);
        }
        nudgeUpHelper(1);
    }

    @Override
    public void run() {
        while (true) {
            this.fallSpeed++; // Increase fall speed to simulate gravity
            updatePlayerFall();
            updatePlayerMove();
            nudgeUp();
            try {
                Thread.sleep(32);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
