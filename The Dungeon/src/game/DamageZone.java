package game;

import city.cs.engine.Body;
import city.cs.engine.Sensor;
import city.cs.engine.Shape;

public class DamageZone extends Sensor {
    private Monster monster;

    public DamageZone(Body body , Shape position) {
        super(body, position);
    }
}