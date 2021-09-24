package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.lang.Math;

public class Skeleton extends Monster {

    private enum State {
        STAND_STILL, ATTACK, GO_UP, GO_DOWN
    }

    private static final Shape skeletonShape = new BoxShape(0.8f,2.5f);

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
    private float XdistanceToPlayer;
    private float YdistanceToPlayer;

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
        // Waypoints denote the player's position (for pathfinding)
        waypointX = level.getPlayer().getPosition().x;
        waypointY = level.getPlayer().getPosition().y;
        XdistanceToPlayer = Math.abs(waypointX - getPosition().x);
        YdistanceToPlayer = Math.abs(waypointY - getPosition().y);

        // update state
        if (hit && state != State.STAND_STILL) {
            state = State.STAND_STILL;
        } else {
            if (getPosition().y < waypointY && YdistanceToPlayer > 3f) {
                state = State.GO_UP;
            } else if (getPosition().y > waypointY && YdistanceToPlayer > 3f) {
                state = State.GO_DOWN;
            } else if (state != State.ATTACK) {
                state = State.ATTACK;
            }
        }
        updateBehaviour();
    }

    private void updateBehaviour() {
        switch (state) {
            case ATTACK:
                if (waypointX > getPosition().x && XdistanceToPlayer > 3f) {
                    startWalking(WALKING_SPEED);
                } else if (waypointX < getPosition().x && XdistanceToPlayer > 3f) {
                    startWalking(-WALKING_SPEED);
                } else {
                    stopWalking();
                }
                break;
            case STAND_STILL:
                stopWalking();
                break;
            case GO_UP:
                // 0<x<2.5 is the area in front of the platform on level 1
                if (getPosition().x < 0) { startWalking(WALKING_SPEED); }
                else if (getPosition().x > 2.5) { startWalking(-WALKING_SPEED); }
                else { jump(19); }
                break;
            case GO_DOWN:
                // Just walk towards X:0
                if (getPosition().x < 0) { startWalking(WALKING_SPEED); }
                else { startWalking(-WALKING_SPEED); }
                break;
        }
    }

    @Override
    public void postStep(StepEvent stepEvent) {}
}
