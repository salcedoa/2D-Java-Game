package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

public abstract class Monster extends Walker implements CollisionListener, StepListener {

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
}
