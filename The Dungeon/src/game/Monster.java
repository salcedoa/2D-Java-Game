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
    private float directionVec;

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
        // increase kill count by 1
        level.setLevelScore(level.getLevelScore() + 1);

        destroy();
        level.getMonsters().remove(this);
    }

    @Override
    public void collide(CollisionEvent event) {
        directionVec = event.getNormal().x;
        if (event.getOtherBody() instanceof Player) {
            player = (Player) event.getOtherBody();
            player.stopWalking(); // to not have the player push against the knockback
            player.takeDamage(player);
            player.addHealth(-5);
            player.setLinearVelocity(new Vec2(directionVec * 10,8));
        } else if (event.getOtherBody() instanceof Monster) {
            // slight knockback effect to reduce the chance of the monsters blocking eachother's movement
            ((Monster) event.getOtherBody()).stopWalking();
            ((Monster) event.getOtherBody()).setLinearVelocity(new Vec2(directionVec * 14, 0));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // checks if there are any monsters that shouldn't be spawned and gets rid of them
        if (dead || level.getLevelScore() >= level.getWinningScore()) {
            die();
            timer.stop();
        }
    }
}
