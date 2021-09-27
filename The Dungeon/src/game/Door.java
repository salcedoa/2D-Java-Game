package game;

import city.cs.engine.*;

public class Door extends StaticBody {

    private static final Shape doorShape = new BoxShape(1.5f, 2.7f);
    private static final BodyImage doorImage = new BodyImage("data/door.png", 6);

    public Door(GameLevel level) {
        super(level, doorShape);
        addImage(doorImage);
    }
}
