import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int UNIT_SIZE = 25;
    private final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    
    private final int[] snakeX = new int[GAME_UNITS];
    private final int[] snakeY = new int[GAME_UNITS];
    private int bodyParts;
    private int score;
    private int highScore = 0;
    private int foodX;
    private int foodY;
    private char direction;
    private boolean running;
    private boolean paused;
    private Random random;
    
    //features: difficulty, obstacles, power-ups
    private int difficulty = 1; // 1=Easy, 2=Medium, 3=Hard
    private List<int[]> obstacles;
    private int powerUpX = -1;
    private int powerUpY = -1;
    private int powerUpTimer = 0;
    private boolean powerUpActive = false;
    
    public GameModel() {
        random = new Random();
        initializeGame();
    }
    
    public void initializeGame() {
        bodyParts = 6;
        score = 0;
        direction = 'R';
        running = true;
        paused = false;
        powerUpActive = false;
        powerUpTimer = 0;
        
        // Initialize snake position
        for (int i = 0; i < bodyParts; i++) {
            snakeX[i] = 100 - i * UNIT_SIZE;
            snakeY[i] = 100;
        }
        
        // Create obstacles based on difficulty
        createObstacles();
        spawnFood();
    }
    
    private void createObstacles() {
        obstacles = new ArrayList<>();
        if (difficulty == 2) {
            // Medium: 3 obstacles
            obstacles.add(new int[]{200, 200});
            obstacles.add(new int[]{400, 300});
            obstacles.add(new int[]{300, 450});
        } else if (difficulty == 3) {
            // Hard: 6 obstacles
            obstacles.add(new int[]{150, 150});
            obstacles.add(new int[]{250, 250});
            obstacles.add(new int[]{400, 150});
            obstacles.add(new int[]{450, 400});
            obstacles.add(new int[]{200, 450});
            obstacles.add(new int[]{350, 350});
        }
    }
    
    private void spawnFood() {
        foodX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }
    
    public void update() {
        if (!running || paused) return;
        
        moveSnake();
        checkFoodCollision();
        updatePowerUp();
        checkPowerUpCollision();
        checkGameCollisions();
    }
    
    private void updatePowerUp() {
        if (powerUpActive) {
            powerUpTimer--;
            if (powerUpTimer <= 0) {
                powerUpActive = false;
                powerUpX = -1;
                powerUpY = -1;
            }
        } else if (random.nextInt(100) < 2) { // 2% chance to spawn
            powerUpX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
            powerUpY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
            powerUpActive = true;
            powerUpTimer = 150;
        }
    }
    
    private void checkPowerUpCollision() {
        if (powerUpActive && snakeX[0] == powerUpX && snakeY[0] == powerUpY) {
            bodyParts += 3;
            score += 5;
            powerUpActive = false;
            powerUpX = -1;
            powerUpY = -1;
        }
    }
    
    private void moveSnake() {
        for (int i = bodyParts; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }
        
        switch (direction) {
            case 'U': snakeY[0] -= UNIT_SIZE; break;
            case 'D': snakeY[0] += UNIT_SIZE; break;
            case 'L': snakeX[0] -= UNIT_SIZE; break;
            case 'R': snakeX[0] += UNIT_SIZE; break;
        }
        
        // Wall wrapping
        if (snakeX[0] < 0) {
            snakeX[0] = WIDTH - UNIT_SIZE;
        } else if (snakeX[0] >= WIDTH) {
            snakeX[0] = 0;
        }
        
        if (snakeY[0] < 0) {
            snakeY[0] = HEIGHT - UNIT_SIZE;
        } else if (snakeY[0] >= HEIGHT) {
            snakeY[0] = 0;
        }
    }
    
    private void checkFoodCollision() {
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            bodyParts++;
            score++;
            spawnFood();
        }
    }
    
    private void checkGameCollisions() {
        // Self collision
        for (int i = bodyParts; i > 0; i--) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                endGame();
                return;
            }
        }
        
        // Obstacle collision
        for (int[] obstacle : obstacles) {
            if (snakeX[0] == obstacle[0] && snakeY[0] == obstacle[1]) {
                endGame();
                return;
            }
        }
    }
    
    private void endGame() {
        running = false;
        if (score > highScore) {
            highScore = score;
        }
    }
    
    // Getters
    public int[] getSnakeX() { return snakeX; }
    public int[] getSnakeY() { return snakeY; }
    public int getBodyParts() { return bodyParts; }
    public int getScore() { return score; }
    public int getHighScore() { return highScore; }
    public int getFoodX() { return foodX; }
    public int getFoodY() { return foodY; }
    public int getPowerUpX() { return powerUpX; }
    public int getPowerUpY() { return powerUpY; }
    public boolean isPowerUpActive() { return powerUpActive; }
    public List<int[]> getObstacles() { return obstacles; }
    public int getDifficulty() { return difficulty; }
    public int getUnitSize() { return UNIT_SIZE; }
    public int getWidth() { return WIDTH; }
    public int getHeight() { return HEIGHT; }
    public boolean isRunning() { return running; }
    public boolean isPaused() { return paused; }
    
    // Setters
    public void setDirection(char dir) {
        // Prevent reversing into itself
        if ((direction == 'R' && dir == 'L') ||
            (direction == 'L' && dir == 'R') ||
            (direction == 'U' && dir == 'D') ||
            (direction == 'D' && dir == 'U')) {
            return;
        }
        this.direction = dir;
    }
    
    public void togglePause() {
        if (running) {
            paused = !paused;
        }
    }
    
    public void restart() {
        initializeGame();
    }
    
    public void setDifficulty(int level) {
        if (level >= 1 && level <= 3) {
            this.difficulty = level;
            restart();
        }
    }
}
