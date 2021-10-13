package game;

import org.jbox2d.common.Vec2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Level1 extends GameLevel implements ActionListener {

    private Timer timer;
    private Timer timer2;
    private Skeleton skeleton;

    public Level1(Game game){
        super(game);
        Game.levelNumber = 1;
        maxMonsters = 2;
        setWinningScore(50); // one enemy = 2
        getPlayer().setPosition(new Vec2(-5,-1.5f));
        getPlayer().setHealth(50);

        createStaticBody(30,4,0,-11);      // ground
        createStaticBody(9,0.5f,13,-0.5f); // side platform
        createStaticBody(1,15, -20,3);     // left wall
        createStaticBody(1,15, 20,3);      // right wall

        // Skeletons Spawn
        spawnMonster();
        spawnMonster();

        // Health Bag Spawner
        timer = new Timer(5000, this);
        timer.start();

        // Checks if the winning conditions of the level are satisfied
        timer2 = new Timer(1000, this);
        timer2.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            if (getPlayer().getHealth() <= 40 && !getBagSpawned()) {
                spawnHealthBag();
            }
        } else if (e.getSource() == timer2) {
            if (getLevelScore() >= getWinningScore()) {
                Door door = new Door(this);
                door.setPosition(new Vec2(16.5f,3));
                timer2.stop();
            }
        }
    }
}