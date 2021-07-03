package game;

import org.jbox2d.common.Vec2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.util.ArrayList;

public class Level1 extends GameLevel implements ActionListener {

    private Timer timer;
    private Skeleton skeleton;

    public Level1(Game game){
        super(game);
        getPlayer().setPosition(new Vec2(-5,-1.5f));
        getPlayer().setHealth(0);

        createStaticBody(30,4,0,-11);      // ground
        createStaticBody(9,0.5f,13,-0.5f); // side platform
        createStaticBody(1,15, -20,3);     // left wall
        createStaticBody(1,15, 20,3);      // right wall

        // Skeleton Spawn and adding them to an arrayList for sensors
        skeleton = new Skeleton(this);
        skeleton.setPosition(new Vec2(10,2));

        skeleton = new Skeleton(this);
        skeleton.setPosition(new Vec2(-10,2));

        // Health Bag Spawner
        timer = new Timer(5000, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getPlayer().getHealth() <= 40 && !getBagSpawned()) {
            spawnHealthBag();
        }
        System.out.println("Health: " + getPlayer().getHealth());
    }
}