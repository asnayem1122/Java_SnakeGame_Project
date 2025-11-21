import javax.swing.*;

public class SnakeGame extends JFrame {
    private GameModel model;
    private GameRenderer renderer;
    private Timer gameTimer;
    private final int DELAY = 100;
    
    public SnakeGame() {
        setTitle("HardCore Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Initialize model
        model = new GameModel();
        
        // Initialize renderer
        renderer = new GameRenderer(model);
        renderer.setFocusable(true);
        renderer.addKeyListener(new GameInputHandler(model));
        
        add(renderer);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        // Start game loop
        startGameLoop();
    }
    
    private void startGameLoop() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameTimer = new Timer(DELAY, e -> {
            model.update();
            renderer.repaint();
        });
        gameTimer.start();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SnakeGame::new);
    }
}