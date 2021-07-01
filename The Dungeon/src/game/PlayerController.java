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
    private DamageZone sword;
    private GameLevel level;

    private Boolean facingRight;
    private Boolean isAttacking;
    private Boolean isBlocking;

    public PlayerController(GameLevel level, DamageZone weapon) {
        this.player = level.getPlayer();
        this.sword = weapon;
        this.level = level;
        facingRight = true;
        isAttacking = false;
        isBlocking = false;
    }

    // The 3rd parameter describes the distance away from the main body that the sensor will be
    private static final Shape swordRight = new BoxShape(0.5f,1, new Vec2(3,1));
    private static final Shape swordLeft = new BoxShape(0.5f,1, new Vec2(-3,1));

    @Override
    public void keyTyped(KeyEvent e) {
        char code = e.getKeyChar();
        switch (code) {
            case ' ':
                if (isAttacking == false) {
                    isAttacking = true;
                    player.attack1(player);
                    if (facingRight) {
                        sword = new DamageZone(player, swordRight);
                    } else {
                        sword = new DamageZone(player, swordLeft);
                    }

                    for (Monster monster : level.getMonsters()) {
                        System.out.println("Added Listener for " + monster);
                        sword.addSensorListener(monster);
                    }

                    // This timer controls the attack mechanism so that you can't hold the attacking position
                    // After 0.2 seconds, the player will return to idle stance
                    timer = new Timer(200, this);
                    timer.setRepeats(false);
                    timer.start();
                }
                break;
            case 'p':
                // debug key
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
                isBlocking = true;
                player.block(player);

                // This timer controls the blocking mechanism so that you can't hold the blocking position
                // After 0.2 seconds, the player will return to idle stance
                timer = new Timer(200, this);
                timer.setRepeats(false);
                timer.start();

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
        sword.destroy();
        isAttacking = false;
        isBlocking = false;
    }
}