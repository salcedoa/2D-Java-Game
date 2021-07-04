package game;

import city.cs.engine.SensorEvent;
import city.cs.engine.SensorListener;
import org.jbox2d.common.Vec2;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonsterHit implements SensorListener, ActionListener {

    private Monster monster;
    private Player player;
    private Timer timer;
    private Boolean hitBySword;

    public MonsterHit(Player player) {
        this.player = player;
    }

    @Override
    public void beginContact(SensorEvent sensorEvent) {
        if (sensorEvent.getContactBody() instanceof Monster) {
            // to be able to call the death() method on the Body it needs to be cast to class Monster
            // otherwise, sensorEvent.getContactBody() will only return a Body object and not a Monster object
            monster = (Monster) sensorEvent.getContactBody();

            if (sensorEvent.getSensor() instanceof DamageZone) {
                monster.death();
                hitBySword = true;

                timer = new Timer(90, this);
                timer.setRepeats(false);
                timer.start();
            } else if (sensorEvent.getSensor() instanceof ShieldZone) {
                monster.blocked();
                hitBySword = false;

                // if statement to determine the direction that the monster will be knocked back once it's been blocked
                float direction;
                if (player.getPosition().x <= monster.getPosition().x) {
                    direction = 1.0f;
                } else {
                    direction = -1.0f;
                }

                monster.setLinearVelocity(new Vec2(direction * 20, 10));
                System.out.println(monster.getLinearVelocity());

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
            monster.destroy();
        } else {
            monster.normal();
        }
    }
}
