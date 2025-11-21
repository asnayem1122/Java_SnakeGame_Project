import java.awt.event.*;

public class GameInputHandler extends KeyAdapter {
    private GameModel model;
    
    public GameInputHandler(GameModel model) {
        this.model = model;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        switch (code) {
            case KeyEvent.VK_LEFT:
                model.setDirection('L');
                break;
            case KeyEvent.VK_RIGHT:
                model.setDirection('R');
                break;
            case KeyEvent.VK_UP:
                model.setDirection('U');
                break;
            case KeyEvent.VK_DOWN:
                model.setDirection('D');
                break;
            case KeyEvent.VK_SPACE:
                if (model.isRunning()) {
                    model.togglePause();
                } else {
                    model.restart();
                }
                break;
            case KeyEvent.VK_1:
                model.setDifficulty(1);
                break;
            case KeyEvent.VK_2:
                model.setDifficulty(2);
                break;
            case KeyEvent.VK_3:
                model.setDifficulty(3);
                break;
        }
    }
}
