package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

public class HealthBag extends DynamicBody{

    private static final Shape bagShape = new CircleShape(1);
    private static final BodyImage image = new BodyImage("data/HealthBag.png", 3);

    public HealthBag(World w, int xPos, int yPos) {
        super(w, bagShape);
        setPosition(new Vec2(xPos,yPos));
        addImage(image);
    }
}