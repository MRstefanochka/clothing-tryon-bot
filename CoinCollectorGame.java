import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CoinCollectorGame extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int GROUND = 500;
    private static final int PLAYER_SIZE = 40;
    private static final int COIN_SIZE = 20;

    private Timer timer;
    private int playerX = 100;
    private int playerY = GROUND - PLAYER_SIZE;
    private double velocityY = 0;
    private boolean jumping = false;

    private ArrayList<Rectangle> coins = new ArrayList<>();
    private Random random = new Random();
    private int score = 0;

    public CoinCollectorGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        timer = new Timer(16, this); // roughly 60 FPS
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        spawnCoin();
    }

    private void spawnCoin() {
        int x = random.nextInt(WIDTH - COIN_SIZE);
        int y = GROUND - COIN_SIZE;
        coins.add(new Rectangle(x, y, COIN_SIZE, COIN_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw ground
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, GROUND, WIDTH, HEIGHT - GROUND);

        // Draw player
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);

        // Draw coins
        g.setColor(Color.ORANGE);
        for (Rectangle coin : coins) {
            g.fillOval(coin.x, coin.y, coin.width, coin.height);
        }

        // Draw score
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // gravity
        if (playerY < GROUND - PLAYER_SIZE) {
            velocityY += 0.5;
        } else {
            playerY = GROUND - PLAYER_SIZE;
            velocityY = 0;
            jumping = false;
        }
        playerY += (int) velocityY;

        // check coin collisions
        Iterator<Rectangle> it = coins.iterator();
        while (it.hasNext()) {
            Rectangle coin = it.next();
            if (new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE).intersects(coin)) {
                it.remove();
                score++;
                spawnCoin();
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !jumping) {
            velocityY = -10;
            jumping = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            playerX = Math.max(0, playerX - 10);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            playerX = Math.min(WIDTH - PLAYER_SIZE, playerX + 10);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Coin Collector");
        CoinCollectorGame game = new CoinCollectorGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
