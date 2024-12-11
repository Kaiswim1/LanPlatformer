
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

public class MultiplayerMenu extends JPanel implements KeyListener {
    private JButton Host;
    private JButton Join;
    private int HostUIStage = -1;
    private int JoinUIStage = -1;
    private int joinCodeInt;

    private Stack<Integer> joinCode;

    public MultiplayerMenu() {
        Host = new JButton("Host");
        Join = new JButton("Join");
        this.add(Host);
        this.add(Join);

        // Action listener for Host button
        Host.addActionListener(e -> {
            GameHoster.connect();
            HostUIStage = 1;
            this.remove(Host);
            this.remove(Join);
            updateUI();
        });

        // Action listener for Join button
        Join.addActionListener(e -> {
            JoinUIStage = 1;
            this.remove(Host);
            this.remove(Join);
            joinCode = new Stack<>();
            addKeyListener(this);
            setFocusable(true);  // Ensure panel can receive key events
            requestFocusInWindow();  // Request focus so it can capture key events
            updateUI();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(174, 135, 132));
        g.fillRect(0, 0, 400, 400);
        g.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 23));
        g.setColor(Color.BLACK);

        // Draw Host UI
        switch (HostUIStage) {
            case 1:
                g.drawString("Join Code: " + GameHoster.getLastOctet(GameHoster.getLocalIPAddress()), 120, 120);
                break;
        }

        // Display the current join code visually
        if (joinCode != null && !joinCode.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Integer digit : joinCode) {
                sb.append(digit);
            }
            g.drawString("Type the Join Code and press enter: " + sb.toString(), 30, 160);
            joinCodeInt = Integer.parseInt(sb.toString());
        }
        else if (JoinUIStage == 1){
            g.drawString("Type the Join Code and press enter: ", 30, 160);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if (Character.isDigit(keyChar) && joinCode.size()<3) {
            // Add the number to joinCode
            joinCode.push((int) keyChar - '0'); // Convert char digit to integer
            System.out.println("Number pressed: " + keyChar);
        } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && !joinCode.isEmpty()) {
            // Remove the last number when backspace is pressed
            joinCode.pop();
        } else if(e.getKeyChar() == KeyEvent.VK_ENTER && joinCode.size() == 3){

        }
        updateUI();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Not needed for this example
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed for this example
    }

    private int getJoinCode() {
        StringBuilder s = new StringBuilder();
        for (Integer i : joinCode) {
            s.append(i);
        }
        try {
            return Integer.parseInt(s.toString());
        } catch (NumberFormatException n) {
            return -1;
        }
    }
}
