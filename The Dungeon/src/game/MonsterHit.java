package game;

import city.cs.engine.SensorEvent;
import city.cs.engine.SensorListener;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonsterHit implements SensorListener, ActionListener {

    private Monster monster;
    private Timer timer;

    @Override
    public void beginContact(SensorEvent sensorEvent) {
        System.out.println("This runs every time a Monster object touches a sensor");
        if (sensorEvent.getSensor() instanceof DamageZone) {
            // to be able to call the death() method on the Body it needs to be cast to class Monster
            // otherwise, sensorEvent.getContactBody() will only return a Body object and not a Monster object
            monster = (Monster) sensorEvent.getContactBody();
            monster.death();
            System.out.println("death() called for " + monster);

            timer = new Timer(50, this);
            timer.setRepeats(false);
            timer.start();
        }
    }

    @Override
    public void endContact(SensorEvent sensorEvent) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: Uncomment
        //monster.destroy();
        System.out.println(monster + " has been destroyed");
    }
}
