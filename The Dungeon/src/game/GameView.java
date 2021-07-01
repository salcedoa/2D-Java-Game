package game;

import city.cs.engine.DebugViewer;
import city.cs.engine.UserView;
import city.cs.engine.World;

import javax.swing.*;
import java.awt.*;

public class GameView extends UserView {
    private Image background;
    public GameView(World w){
        super(w, 800, 500); // the window size is set

        // DEBUG VIEW - overrides normal view
        JFrame debugView = new DebugViewer(w, 800, 500);
        background = new ImageIcon("data/dungeon.png").getImage();
    }

    @Override
    protected void paintBackground(Graphics2D g){
        g.drawImage(background, 0, 0, this);
    }
}