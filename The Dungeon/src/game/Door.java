package game;

import city.cs.engine.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Door extends StaticBody {

    private static final Shape doorShape = new BoxShape(1.5f, 2.7f);
    private static final BodyImage doorImage = new BodyImage("data/door.png", 6);

    public Door(GameLevel level) {
        super(level, doorShape);
        addImage(doorImage);
    }

    /** INNER CLASSES */

    public static class DoorEncounter implements CollisionListener {
        private Game game;

        private static SoundClip doorSound;
        static {
            try {
                doorSound = new SoundClip("data/sound/39026__wildweasel__keypickup.wav");
            } catch(UnsupportedAudioFileException| IOException| LineUnavailableException e) {
                System.out.println(e);
            }
        }

        public DoorEncounter(Player player, Game game) {
            this.game = game;
        }

        @Override
        public void collide(CollisionEvent collisionEvent) {
            if (collisionEvent.getOtherBody() instanceof Door) {
                doorSound.play();
                game.goToNextLevel();
            }
        }
    }
}
