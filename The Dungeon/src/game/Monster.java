package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class Monster extends Walker implements CollisionListener, StepListener, SensorListener, ActionListener {

    private Timer timer;
    private Monster monster;

    // All monsters need a normal walking and death animation
    public abstract void normal();
    public abstract void death();

    public Monster(GameLevel level, Shape shape) {
        super(level, shape);
        normal();
        level.addStepListener(this);
        addCollisionListener(this);
    }

    @Override
    public void collide(CollisionEvent event) {
        if (event.getOtherBody() instanceof Player) {
            // event.getOtherBody.addHealth(-10);
            float directionVec = event.getNormal().x;
            ((Player) event.getOtherBody()).setLinearVelocity(new Vec2(directionVec * 10,8));
        }
    }

    @Override
    public void beginContact(SensorEvent sensorEvent) {
        System.out.println("This runs every time a Monster object touches a sensor");
        if (sensorEvent.getSensor() instanceof DamageZone) {
            // to be able to call the death() method on the Body it needs to be cast to class Monster
            // otherwise, sensorEvent.getContactBody() will only return a Body object and not a Monster object
            monster = (Monster) sensorEvent.getContactBody();
            monster.death();
            System.out.println("death() called for " + monster);

            timer = new Timer(50, this);
            timer.setRepeats(false);
            timer.start();
        }
    }

    @Override
    public void endContact(SensorEvent sensorEvent) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: Uncomment
        //monster.destroy();
        System.out.println(monster + " has been destroyed");
    }
}
