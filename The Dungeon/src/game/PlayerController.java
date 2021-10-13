package game;

import city.cs.engine.BoxShape;
import city.cs.engine.Sensor;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

public class PlayerController implements KeyListener, ActionListener {

    private static final float WALKING_SPEED = 7;
    private Timer timer;
    private Player player;
    private GameLevel level;
    private Player.DamageZone sword;
    private Player.ShieldZone shield;

    private Boolean facingRight;
    private Boolean isAttacking;
    private Boolean isBlocking;

    public PlayerController(GameLevel level) {
        this.level = level;
        this.player = level.getPlayer();
        facingRight = true;
        isAttacking = false;
        isBlocking = false;
    }

    // this method updates the sprite being controlled after a level is changed
    public void updatePlayer(Player player) {
        this.player = player;
    }

    // The 3rd parameter describes the distance away from the main body that the sensor will be
    private static final Shape swordRight = new BoxShape(0.5f,1, new Vec2(3,1));
    private static final Shape swordLeft = new BoxShape(0.5f,1, new Vec2(-3,1));

    // The 3rd parameter describes the distance away from the main body that the sensor will be
    private static final Shape shieldRight = new BoxShape(0.5f,0.5f, new Vec2(1.5f,0.4f));
    private static final Shape shieldLeft = new BoxShape(0.5f,0.5f, new Vec2(-1.5f,0.4f));

    @Override
    public void keyTyped(KeyEvent e) {
        char code = e.getKeyChar();
        switch (code) {
            case ' ':
                if (!isAttacking) {
                    isAttacking = true;
                    player.attack1(player);
                    if (facingRight) {
                        sword = player.new DamageZone(swordRight);
                    } else {
                        sword = player.new DamageZone(swordLeft);
                    }
                    sword.addSensorListener(new MonsterHit(level));

                    // This timer controls the attack mechanism so that you can't hold the attacking position
                    // After 0.2 seconds, the player will return to idle stance
                    timer = new Timer(200, this);
                    timer.setRepeats(false);
                    timer.start();
                }
                break;
            case 'p':
                // debug key
                System.out.println(player.getPosition());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // other key commands omitted
        switch (code) {
            case KeyEvent.VK_LEFT:
                facingRight = false;
                player.walkingAnimation(player);
                player.turn(player);
                player.startWalking(-WALKING_SPEED);
                break;
            case KeyEvent.VK_RIGHT:
                facingRight = true;
                player.walkingAnimation(player);
                player.startWalking(WALKING_SPEED);
                break;
            case KeyEvent.VK_UP:
                player.jump(18);
                break;
            case KeyEvent.VK_DOWN:
                if (!isBlocking) {
                    isBlocking = true;
                    player.block(player);
                    if (facingRight) {
                        shield = player.new ShieldZone(shieldRight);
                    } else {
                        shield = player.new ShieldZone(shieldLeft);
                    }
                    shield.addSensorListener(new MonsterHit(level));

                    // This timer controls the blocking mechanism so that you can't hold the blocking position
                    // After 0.2 seconds, the player will return to idle stance
                    timer = new Timer(200, this);
                    timer.setRepeats(false);
                    timer.start();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            player.stopWalking();
            player.idleAnimation(player);
            player.turn(player);
        } else if (code == KeyEvent.VK_RIGHT) {
            player.stopWalking();
            player.idleAnimation(player);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (facingRight) {
            player.idleAnimation(player);
        } else {
            player.idleAnimation(player);
            player.turn(player);
        }

        if (isAttacking) {
            sword.destroy();
            isAttacking = false;
        } else if (isBlocking) {
            shield.destroy();
            isBlocking = false;
        }
    }
}