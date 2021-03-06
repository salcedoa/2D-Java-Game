package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Demon extends Monster implements ActionListener {

    private enum State {
        STAND_STILL, SHOOT, REACH_PLAYER, RETREAT
    }

    private static final Shape demonShape = new BoxShape(0.8f, 2.5f);

    private static final BodyImage normal = new BodyImage("data/demon/idle.png", 3.5f);
    private static final BodyImage shoot = new BodyImage("data/demon/shoot.png", 3.5f);
    private static final BodyImage death = new BodyImage("data/demon/hurt.png", 3.5f);
    private AttachedImage currentAnimation;

    // sounds loaded using static code so that it is loaded only once instead of whenever an object is created
    private static SoundClip fireballSound;
    static {
        try {
            fireballSound = new SoundClip("data/sound/238283__meroleroman7__8-bit-noise.wav");
        } catch (UnsupportedAudioFileException | IOException| LineUnavailableException e) {
            System.out.println(e);
        }
    }

    private Demon.State state;
    private Boolean hit;
    private Boolean shooting;

    private Timer timer;

    private static final float WALKING_SPEED = 5;
    private static final float range = 6;

    private float waypointX;
    private float waypointY;
    private float XdistanceToPlayer;
    private float YdistanceToPlayer;

    private GameLevel level;

    // Constructor
    public Demon(GameLevel level) {
        super(level, demonShape);
        setGravityScale(2);
        state = State.REACH_PLAYER;
        hit = false;
        shooting = false;
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
        currentAnimation = new AttachedImage(this, death,1.4f,0,new Vec2(0, 0));
        hit = true;
    }

    public void shoot() {
        removeAttachedImage(currentAnimation);
        if (getPosition().x < waypointX) {
            currentAnimation = new AttachedImage(this, shoot, 1.4f, 0, new Vec2(0,0));
        } else {
            currentAnimation = new AttachedImage(this, shoot, 1.4f, 0, new Vec2(0, 0));
            currentAnimation.flipHorizontal();
        }
        fireballSound.play();
        shooting = true;
        hit = false;

        timer = new Timer(200, this);
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
        if (hit && state != Demon.State.STAND_STILL) {
            state = Demon.State.STAND_STILL;
        } else {
            if (XdistanceToPlayer <= 6 && YdistanceToPlayer <= 5) {
                state = State.RETREAT;
            } else if (XdistanceToPlayer <= 13 && YdistanceToPlayer <= 3) {
                state = State.SHOOT;
            } else if (XdistanceToPlayer > 13 || YdistanceToPlayer > 5) {
                state = State.REACH_PLAYER;
            }
        }
        updateBehaviour();
    }

    public void updateBehaviour() {
        switch (state) {
            case SHOOT:
                if (!shooting) {
                    shoot();
                    Fireball fireball = new Fireball(level, this);
                }
                break;
            case RETREAT:
                // Preconditions: Player is less than 6 x-coords and less than 5 y-coords away from Demon
                // check if Player is to the right or to the left of Demon
                if (waypointX < getPosition().x) {
                    startWalking(WALKING_SPEED + 1);
                } else if (waypointX > getPosition().x) {
                    startWalking(-WALKING_SPEED - 1);
                }
                // check if Player is less than 4 x-coords and less than 5 y-coords from Demon
                if (XdistanceToPlayer < 4) { jump(19); }
                break;
            case REACH_PLAYER:
                // Preconditions: Player is more than 13 x-coords and more than 5 y-coords away from Demon
                // check x coordinates and direction to walk in
                if (waypointX > getPosition().x) { startWalking(WALKING_SPEED);
                } else if (waypointX < getPosition().x) {
                    startWalking(-WALKING_SPEED);
                }
                // check y coordinates and whether to jump or not
                if (waypointY > getPosition().y) { jump(19); }
                break;
            case STAND_STILL:
                stopWalking();
                break;
        }
    }

    @Override
    public void postStep(StepEvent stepEvent) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (shooting) {
            shooting = false;
            normal();
        }
    }
}