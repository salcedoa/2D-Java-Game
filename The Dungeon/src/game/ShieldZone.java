package game;

import city.cs.engine.Body;
import city.cs.engine.Sensor;
import city.cs.engine.Shape;

public class ShieldZone extends Sensor{
    private Monster monster;

    public ShieldZone(Body body, Shape shape) {
        super(body, shape);
    }
}
