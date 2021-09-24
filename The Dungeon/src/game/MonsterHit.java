package game;

import city.cs.engine.SensorEvent;
import city.cs.engine.SensorListener;
import org.jbox2d.common.Vec2;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** This class handles what will happen if an enemy is hit with a sword or shield **/
public class MonsterHit implements SensorListener, ActionListener {

    private Monster monster;
    private Player player;
    private GameLevel level;
    private Timer timer;

    private Boolean hitBySword;

    public MonsterHit(GameLevel level) {
        this.level = level;
        this.player = level.getPlayer();
    }

    @Override
    public void beginContact(SensorEvent sensorEvent) {
        if (sensorEvent.getContactBody() instanceof Monster) {
            // to be able to call the death() method on the Body it needs to be cast to class Monster
            // otherwise, sensorEvent.getContactBody() will only return a Body object and not a Monster object
            monster = (Monster) sensorEvent.getContactBody();

            if (sensorEvent.getSensor() instanceof Player.DamageZone) {
                monster.death();
                hitBySword = true;

                timer = new Timer(90, this);
                timer.setRepeats(false);
                timer.start();
            } else if (sensorEvent.getSensor() instanceof Player.ShieldZone) {
                monster.blocked();
                hitBySword = false;

                // if statement to determine the direction that the monster will be knocked back once it's been blocked
                float direction;
                if (player.getPosition().x <= monster.getPosition().x) {
                    direction = 1.0f;
                } else {
                    direction = -1.0f;
                }

                // knockback effect done by imposing a vector on the monster
                monster.setLinearVelocity(new Vec2(direction * 20, 10));

                timer = new Timer(150, this);
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    @Override
    public void endContact(SensorEvent sensorEvent) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (hitBySword) {
            monster.die();
            if (level.getMonsters().size() <= level.maxMonsters) {
                level.spawnMonster();
            }
        } else {
            monster.normal();
        }
    }
}
