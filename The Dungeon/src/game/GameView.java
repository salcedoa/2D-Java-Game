package game;

import city.cs.engine.DebugViewer;
import city.cs.engine.UserView;
import city.cs.engine.World;
import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;

public class GameView extends UserView {
    private Image background;
    private Game game;
    int health;
    int score;
    public GameView(World w, Game game){
        super(w, 800, 500); // the window size is set
        this.game = game;

        // DEBUG VIEW - overrides normal view
        //JFrame debugView = new DebugViewer(w, 800, 500);

        background = new ImageIcon("data/dungeon.png").getImage();
    }

    public void updateBackground() {
        if (Game.levelNumber == 1) {
            background = new ImageIcon("data/dungeon.png").getImage();
        } else if (Game.levelNumber == 2) {
            background = new ImageIcon("data/hell.gif").getImage();
        } else if (Game.levelNumber == 3) {
            background = new ImageIcon("data/swamp.png").getImage();
        }
    }

    @Override
    protected void paintBackground(Graphics2D g){
        g.drawImage(background, -50, -50, this);
    }

    @Override
    protected void paintForeground(Graphics2D g) {
        health = game.getLevel().getPlayer().getHealth();
        score = game.getCachedScore() + game.getLevel().getLevelScore();

        // showing player health on screen
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
        g.drawString("Health: " + health, 20, 450);

        // showing total score on screen
        g.drawString("Score: " + score, getWidth() - 200, 450);
    }
}
