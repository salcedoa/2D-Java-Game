package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

public class Fireball extends DynamicBody implements CollisionListener {

    private Player player;

    private static final Shape fireballShape = new CircleShape(1f);
    private static final BodyImage fireballImage = new BodyImage("data/demon/fireball.png", 1f);
    private AttachedImage currentImage;

    public Fireball(GameLevel level, Demon demon) {
        super(level, fireballShape);
        setBullet(true); // turns on continuous collision detection
        player = level.getPlayer();
        addCollisionListener(this);
        if (demon.getPosition().x < player.getPosition().x) {
            currentImage = new AttachedImage(this, fireballImage, 1, 0, new Vec2(0,0));
            setLinearVelocity(new Vec2(15,0));
            setPosition(new Vec2(demon.getPosition().x + 2, demon.getPosition().y));
        } else {
            currentImage = new AttachedImage(this, fireballImage, 1, 0, new Vec2(0,0));
            currentImage.flipHorizontal();
            setPosition(new Vec2(demon.getPosition().x - 2, demon.getPosition().y));
            setLinearVelocity(new Vec2(-15,0));
        }
    }

    @Override
    public void collide(CollisionEvent collisionEvent) {
        float directionVec = collisionEvent.getNormal().x;
        if (collisionEvent.getOtherBody() instanceof Player) {
            destroy();
            player.stopWalking(); // to not have the player push against the knockback
            player.takeDamage(player);
            player.addHealth(-2);
            player.setLinearVelocity(new Vec2(directionVec * 10,8));
        } else if (collisionEvent.getOtherBody() instanceof Monster) {
            ((Monster) collisionEvent.getOtherBody()).stopWalking();
            ((Monster) collisionEvent.getOtherBody()).setLinearVelocity(new Vec2(directionVec * 7, 0));
            destroy();
        } else {
            destroy();
        }
    }

    /** INNER CLASSES */
    public static class FireballHit implements SensorListener {

        @Override
        public void beginContact(SensorEvent sensorEvent) {
            if (sensorEvent.getContactBody() instanceof Fireball) {
                sensorEvent.getContactBody().destroy();
            }
        }

        @Override
        public void endContact(SensorEvent sensorEvent) {

        }
    }
}
