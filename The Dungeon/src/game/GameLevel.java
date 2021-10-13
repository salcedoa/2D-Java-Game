package game;

import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;

import java.awt.*;
import java.util.ArrayList;

public abstract class GameLevel extends World {
    private Player player;

    private int levelScore;
    public int getLevelScore() { return levelScore; }
    public void setLevelScore(int levelScore) { this.levelScore = levelScore; }

    private int winningScore;
    public int getWinningScore() { return winningScore; }
    public void setWinningScore(int winningScore) { this.winningScore = winningScore; }

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
        levelScore = 0;
        bagSpawned = false;
        BagPickup pickup = new BagPickup(player, this);
        player.addCollisionListener(pickup);
        Door.DoorEncounter enterDoor = new Door.DoorEncounter(game);
        player.addCollisionListener(enterDoor);
    }

    // method that handles the creation of static bodies such as platforms and walls
    public void createStaticBody(float width, float height, float xPos, float yPos) {
        Shape objectShape = new BoxShape(width, height);    // shape and dimensions defined
        Body object = new StaticBody(this, objectShape); // body type is defined
        object.setPosition(new Vec2(xPos,yPos));      // setting the position of the shape
        if (Game.levelNumber == 1) {
            object.setFillColor(Color.gray);
            object.setLineColor(Color.gray);
        } else if (Game.levelNumber == 2) {
            object.setFillColor(new Color(80, 12, 9));
            object.setLineColor(new Color(80, 12, 9));
        } else if (Game.levelNumber == 3) {
            object.setFillColor(new Color(52, 22, 6));
            object.setLineColor(new Color(52, 22, 6));
        }
    }


    // method for spawning the health bags at random locations
    private int xMax = 18;
    private int xMin = -18;
    private int yMax = 15;
    private int yMin = -9;
    public void spawnHealthBag() {
        bagSpawned = true;
        int x = (int)Math.floor(Math.random()*(xMax-xMin+1)+xMin);
        int y = (int)Math.floor(Math.random()*(yMax-yMin+1)+yMin);
        HealthBag bag = new HealthBag(this, x, y);
    }

    public void spawnMonster() {
        if (levelScore <= winningScore) {
            int x = (int) Math.floor(Math.random() * (xMax - xMin + 1) + xMin);
            int y = (int) Math.floor(Math.random() * (yMax - yMin + 1) + yMin);
            if (Game.levelNumber == 1) {
                Skeleton skeleton = new Skeleton(this);
                skeleton.setPosition(new Vec2(x, y));
                monsters.add(skeleton);
            } else if (Game.levelNumber == 2) {
                Demon demon = new Demon(this);
                demon.setPosition(new Vec2(x,y));
                monsters.add(demon);
            } // TODO: Add spawn instructions for other monsters
        }
    }

    public Player getPlayer(){
        return player;
    }
}
