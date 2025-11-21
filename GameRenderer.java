import javax.swing.*;
import java.awt.*;

public class GameRenderer extends JPanel {
    private GameModel model;
    
    public GameRenderer(GameModel model) {
        this.model = model;
        setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
        setBackground(Color.BLACK);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (model.isRunning()) {
            if (!model.isPaused()) {
                drawGame(g);
            } else {
                drawPausedScreen(g);
            }
        } else {
            drawGameOverScreen(g);
        }
    }
    
    private void drawGame(Graphics g) {
        // Draw obstacles
        drawObstacles(g);
        
        // Draw food
        g.setColor(new Color(255, 215, 0)); // Gold
        g.fillRect(model.getFoodX(), model.getFoodY(), 
                   model.getUnitSize(), model.getUnitSize());
        
        // Draw power-up if active
        if (model.isPowerUpActive()) {
            g.setColor(new Color(255, 0, 255)); // Magenta
            g.fillRect(model.getPowerUpX(), model.getPowerUpY(), 
                       model.getUnitSize(), model.getUnitSize());
            g.setColor(Color.WHITE);
            g.drawRect(model.getPowerUpX(), model.getPowerUpY(), 
                       model.getUnitSize(), model.getUnitSize());
        }
        
        // Draw snake
        drawSnake(g);
        
        // Draw score and difficulty
        drawHUD(g);
    }
    
    private void drawObstacles(Graphics g) {
        g.setColor(new Color(128, 128, 128)); // Gray
        for (int[] obstacle : model.getObstacles()) {
            g.fillRect(obstacle[0], obstacle[1], model.getUnitSize(), model.getUnitSize());
            g.setColor(Color.BLACK);
            g.drawRect(obstacle[0], obstacle[1], model.getUnitSize(), model.getUnitSize());
            g.setColor(new Color(128, 128, 128));
        }
    }
    
    private void drawSnake(Graphics g) {
        int[] snakeX = model.getSnakeX();
        int[] snakeY = model.getSnakeY();
        int bodyParts = model.getBodyParts();
        int unitSize = model.getUnitSize();
        
        for (int i = 0; i < bodyParts; i++) {
            g.setColor(Color.WHITE);
            g.fillRect(snakeX[i], snakeY[i], unitSize, unitSize);
            g.setColor(Color.BLACK);
            g.drawRect(snakeX[i], snakeY[i], unitSize, unitSize);
            
            // Draw face on the head (first segment)
            if (i == 0) {
                drawSnakeFace(g, snakeX[i], snakeY[i], unitSize);
            }
        }
    }
    
    private void drawSnakeFace(Graphics g, int x, int y, int size) {
        g.setColor(Color.BLACK);
        // Left eye
        g.fillOval(x + 5, y + 5, 4, 4);
        // Right eye
        g.fillOval(x + size - 9, y + 5, 4, 4);
        // Mouth
        g.drawLine(x + 7, y + size - 5, x + size - 7, y + size - 5);
    }
    
    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + model.getScore(), 10, 25);
        g.drawString("High Score: " + model.getHighScore(), 10, 50);
        g.drawString("Difficulty: " + getDifficultyName(), 10, 75);
    }
    
    private void drawHUD(Graphics g) {
        drawScore(g);
    }
    
    private String getDifficultyName() {
        switch(model.getDifficulty()) {
            case 1: return "Easy";
            case 2: return "Medium";
            case 3: return "Hard";
            default: return "Unknown";
        }
    }
    
    private void drawPausedScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, model.getWidth(), model.getHeight());
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("PAUSED", model.getWidth() / 2 - 100, model.getHeight() / 2);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Press SPACE to resume", model.getWidth() / 2 - 150, 
                     model.getHeight() / 2 + 50);
    }
    
    private void drawGameOverScreen(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("GAME OVER", model.getWidth() / 2 - 150, model.getHeight() / 2);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Final Score: " + model.getScore(), 
                     model.getWidth() / 2 - 100, model.getHeight() / 2 + 50);
        g.drawString("High Score: " + model.getHighScore(), 
                     model.getWidth() / 2 - 110, model.getHeight() / 2 + 80);
        g.drawString("Press SPACE to restart | 1/2/3 to change difficulty", 
                     model.getWidth() / 2 - 260, model.getHeight() / 2 + 130);
    }
}
