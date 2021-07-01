package game;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

// A class that performs the effects of picking up a HealthBag object
// Implements CollisionListener to begin the actions and ActionListener for the timer that handles animation
public class BagPickup implements CollisionListener, ActionListener {
    private Timer timer;
    private Player player;
    private GameLevel level;

    public BagPickup(Player p, GameLevel l) {
        this.player = p;
        this.level = l;
    }

    @Override
    public void collide(CollisionEvent event) {
        if (event.getOtherBody() instanceof HealthBag) {
            player.addHealth(10);
            if (player.getCurrentAnimation().isFlippedHorizontal()) {
                player.healCostume(player);
                player.turn(player);
            } else {
                player.healCostume(player);
            }

            event.getOtherBody().destroy();
            level.setBagSpawned(false);

            timer = new Timer(200, this);
            timer.setRepeats(false);
            timer.start();
        }
    }

    // ActionListener method that executes when timer goes off
    // Makes the player stop glowing green
    @Override
    public void actionPerformed(ActionEvent e) {
        if (player.getCurrentAnimation().isFlippedHorizontal()) {
            player.idleAnimation(player);
            player.turn(player);
        } else {
            player.idleAnimation(player);
        }

    }
}
