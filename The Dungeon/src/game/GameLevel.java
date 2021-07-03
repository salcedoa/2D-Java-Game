package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

public abstract class GameLevel extends World {
    private Player player;
    private PlayerController controller;

    // variable to control the bag spawn system
    private Boolean bagSpawned;

    public Boolean getBagSpawned() {
        return bagSpawned;
    }
    public void setBagSpawned(Boolean bagSpawned) {
        this.bagSpawned = bagSpawned;
    }

    public GameLevel (Game game){
        player = new Player(this);

        bagSpawned = false;
        BagPickup pickup = new BagPickup(player, this);
        player.addCollisionListener(pickup);

    }

    // method that handles the creation of static bodies such as platforms
    public void createStaticBody(float width, float height, float xPos, float yPos) {
        Shape objectShape = new BoxShape(width, height);    // shape and dimensions defined
        Body object = new StaticBody(this, objectShape); // body type is defined
        object.setPosition(new Vec2(xPos,yPos));      // setting the position of the shape
    }


    // method for spawning the health bags at random locations
    private int xMax = 18;
    private int xMin = -18;
    private int yMax = 20;
    public void spawnHealthBag() {
        bagSpawned = true;
        int x = (int)Math.floor(Math.random()*(xMax-xMin+1)+xMin);
        int y = (int)Math.floor(Math.random()*(yMax+1));
        HealthBag bag = new HealthBag(this, x, y);
    }

    public Player getPlayer(){
        return player;
    }
}
