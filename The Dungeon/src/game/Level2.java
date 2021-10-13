package game;

import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Level2 extends GameLevel implements ActionListener {

    private Timer timer;
    private Timer timer2;

    public Level2(Game game) {
        super(game);
        Game.levelNumber = 2; // TODO: Refactor levelNumber to just use currentLevel
        maxMonsters = 2;
        setWinningScore(40); // one enemy = 2
        getPlayer().setPosition(new Vec2(-5,-1.5f));
        getPlayer().setHealth(50);

        createStaticBody(30,4,0,-12);      // ground
        createStaticBody(7, 4, 0, -7);    // side raised ground
        createStaticBody(1,15, -20,3);     // left wall
        createStaticBody(1,15, 20,3);      // right wall

        // Demon spawn
        spawnMonster();
        spawnMonster();

        // Health Bag Spawner
        timer = new Timer(7500, this);
        timer.start();

        // Checks if the winning conditions of the level are satisfied
        timer2 = new Timer(1000, this);
        timer2.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            if (getPlayer().getHealth() <= 30 && !getBagSpawned()) {
                spawnHealthBag();
            }
        } else if (e.getSource() == timer2) {
            if (getLevelScore() >= getWinningScore()) {
                Door door = new Door(this);
                door.setPosition(new Vec2(1.2f,-0.5f));
                timer2.stop();
            }
        }
    }
}
