package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

public class Frog extends Monster{

    private enum State {
        STAND_STILL, REACH_PLAYER, JUMP_OFF
    }

    private static final Shape frogShape = new BoxShape(0.8f,1);

    private static final BodyImage normal = new BodyImage("data/frog/idle.png", 1.5f);
    private static final BodyImage death = new BodyImage("data/frog/hurt.png", 1.5f);
    private AttachedImage currentAnimation;

    private State state;
    private boolean hit;

    private static final float WALKING_SPEED = 7;

    private float waypointX;
    private float waypointY;
    private float XdistanceToPlayer;
    private float YdistanceToPlayer;

    private GameLevel level;

    public Frog(GameLevel level) {
        super(level, frogShape);
        currentAnimation = new AttachedImage(this, normal,1.4f,0,new Vec2(0, 0));
        setGravityScale(2);
        state = State.REACH_PLAYER;
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
        currentAnimation = new AttachedImage(this, normal,1.4f,0,new Vec2(0, 0));
        hit = true;
    }

    @Override
    public void death() {
        removeAttachedImage(currentAnimation);
        currentAnimation = new AttachedImage(this, death, 1.4f,0, new Vec2(0, 0));
        dead = true;
    }

    @Override
    public void preStep(StepEvent stepEvent) {
        waypointX = level.getPlayer().getPosition().x;
        waypointY = level.getPlayer().getPosition().y;
        XdistanceToPlayer = Math.abs(waypointX - getPosition().x);
        YdistanceToPlayer = Math.abs(waypointY - getPosition().y);

        // update state
        if (hit && state != State.STAND_STILL) {
            state = State.STAND_STILL;
        } else {
            if (XdistanceToPlayer > 1.5 && YdistanceToPlayer > 1.5) {
                state = State.JUMP_OFF;
            } else {
                state = State.REACH_PLAYER;
            }
        }
        updateBehaviour();
    }

    private void updateBehaviour() {
        switch (state) {
            case STAND_STILL:
                stopWalking();
                break;
            case JUMP_OFF:
                if (waypointX > getPosition().x) {
                    jump(10);
                    startWalking(-WALKING_SPEED);
                } else if (waypointX < getPosition().x) {
                    jump(10);
                    startWalking(WALKING_SPEED);
                }
            case REACH_PLAYER:
                if (waypointX > getPosition().x) {
                    jump(10);
                    startWalking(4);
                } else if (waypointX < getPosition().x) {
                    jump(10);
                    startWalking(-4);
                }
        }
    }

    @Override
    public void postStep(StepEvent stepEvent) {}
}
