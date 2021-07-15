package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;

public abstract class GameLevel extends World {
    private Player player;

    // variable to control the bag spawn system
    private Boolean bagSpawned;
    public Boolean getBagSpawned() {
        return bagSpawned;
    }
    public void setBagSpawned(Boolean bagSpawned) {
        this.bagSpawned = bagSpawned;
    }

    protected int maxMonsters;
    private ArrayList<Monster> monsters = new ArrayList<Monster>();
    public ArrayList<Monster> getMonsters() {
        return monsters;
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

    public void spawnMonster() {
        int x = (int)Math.floor(Math.random()*(xMax-xMin+1)+xMin);
        int y = (int)Math.floor(Math.random()*(yMax+1));
        if (Game.levelNumber == 1) {
            Skeleton skeleton = new Skeleton(this);
            skeleton.setPosition(new Vec2(x, y));
            monsters.add(skeleton);
        } // TODO: Add spawn instructions for other monsters
    }

    public Player getPlayer(){
        return player;
    }
}
