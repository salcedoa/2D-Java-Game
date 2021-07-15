package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

public class Skeleton extends Monster{

    private enum State {
        STAND_STILL, ATTACK, GO_UP, GO_DOWN
    }

    private static final Shape skeletonShape = new BoxShape(0.5f,2.5f);

    // initializing BodyImage objects that will be used to change the skeleton's animations
    private static final BodyImage normal = new BodyImage("data/skeleton/walk.gif", 3.5f);
    private static final BodyImage blocked = new BodyImage("data/skeleton/blocked.png", 3.5f);
    private static final BodyImage death = new BodyImage("data/skeleton/broken.png", 3.5f);
    private AttachedImage currentAnimation;

    private State state;
    private Boolean hit;

    private static final float WALKING_SPEED = 5;

    private float waypointX;
    private float waypointY;

    private GameLevel level;

    public Skeleton(GameLevel level) {
        super(level, skeletonShape);
        setGravityScale(2);
        state = State.ATTACK;
        hit = false;
        this.level = level;
    }

    @Override
    public void normal() {
        removeAttachedImage(currentAnimation);
        currentAnimation = new AttachedImage(this, normal,1.4f,0,new Vec2(0, 0));
        hit = false;
    }

    @Override
    public void blocked() {
        removeAttachedImage(currentAnimation);
        currentAnimation = new AttachedImage(this, blocked,1.4f,0,new Vec2(0, 0));
        hit = true;
    }

    // death animation
    @Override
    public void death() {
        removeAttachedImage(currentAnimation);
        currentAnimation = new AttachedImage(this, death, 1.4f,0, new Vec2(0, 0));
        dead = true;
    }

    @Override
    public void preStep(StepEvent stepEvent) {
        // update state
        if (hit && state != State.STAND_STILL) {
            state = State.STAND_STILL;
        } else {
            if (state != State.ATTACK) {
                state = State.ATTACK;
            }
        }
        updateBehaviour();
    }

    // update what actions the skeleton will take
    private void updateBehaviour() {
        switch (state) {
            case ATTACK:
                // Waypoints denote the player's position for pathfinding
                waypointX = level.getPlayer().getPosition().x;
                waypointY = level.getPlayer().getPosition().y;
                if (waypointX > getPosition().x) {
                    startWalking(WALKING_SPEED);
                } else if (waypointX < getPosition().x) {
                    startWalking(-WALKING_SPEED);
                }
                break;
            case STAND_STILL:
                stopWalking();
        }
    }

    @Override
    public void postStep(StepEvent stepEvent) {}
}
