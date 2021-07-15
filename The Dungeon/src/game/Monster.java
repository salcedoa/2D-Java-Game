package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class Monster extends Walker implements CollisionListener, StepListener, ActionListener {

    private Player player;
    private GameLevel level;
    private Timer timer;
    protected Boolean dead;

    // All monsters need a walking, blocked and death animation
    public abstract void normal();
    public abstract void blocked();
    public abstract void death();

    public Monster(GameLevel level, Shape shape) {
        super(level, shape);
        this.level = level;
        //level.changeCurrentMonsters(1);

        normal();
        dead = false;
        level.addStepListener(this);
        addCollisionListener(this);

        // this timer will check if there are any monsters that haven't despawned after getting hit
        timer = new Timer(450, this);
        timer.setRepeats(true);
        timer.start();
    }

    public void die() {
        destroy();
        level.getMonsters().remove(this);
    }

    @Override
    public void collide(CollisionEvent event) {
        if (event.getOtherBody() instanceof Player) {
            player = (Player) event.getOtherBody();
            player.stopWalking(); // to not have the player push against the knockback
            // event.getOtherBody.addHealth(-10);
            float directionVec = event.getNormal().x;
            player.setLinearVelocity(new Vec2(directionVec * 10,8));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (dead) {
            die();
            timer.stop();
        }
    }
}
