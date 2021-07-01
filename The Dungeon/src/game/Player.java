package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

public class Player extends Walker {
    private static final Shape playerShape = new PolygonShape(
            -0.11f,2.48f,
            1.14f,0.49f,
            0.51f,-2.46f,
            -0.55f,-2.46f,
            -1.17f,0.28f); // the points of the polygon are defined


    // Initializing BodyImage objects that will be used to change the player's animations
    private static final BodyImage idle = new BodyImage("data/player/idle.png", 4);
    private static final BodyImage walk = new BodyImage("data/player/walk.gif", 4);
    private static final BodyImage attack = new BodyImage("data/player/attacking.png", 4);
    private static final BodyImage block = new BodyImage("data/player/blocking.png", 4);
    private static final BodyImage hurt = new BodyImage("data/player/hurt.png", 4);
    private static final BodyImage heal = new BodyImage("data/player/heal.png", 4);
    private static AttachedImage currentAnimation;


    private int health;

    public void setHealth(int health) {
        this.health = health;
    }
    public void addHealth(int value) {
        this.health += value;
    }
    public int getHealth() {
        return health;
    }

    // constructor
    public Player(World world) {
        super(world, playerShape);
        currentAnimation = new AttachedImage(this, idle,1.4f,0,new Vec2(0, 0.22f));
        setGravityScale(2);
    }


    // ANIMATION METHODS

    // accessor method to control flipped images in BagPickup() events
    public static AttachedImage getCurrentAnimation() {
        return currentAnimation;
    }

    // idle
    public static void idleAnimation(Player player){
        player.removeAttachedImage(currentAnimation);
        currentAnimation = new AttachedImage(player, idle, 1.4f,0, new Vec2(0, 0.2f));
    }

    // walking
    public static void walkingAnimation(Player player){
        player.removeAttachedImage(currentAnimation);
        currentAnimation = new AttachedImage(player, walk, 1.4f,0, new Vec2(0, 0.22f));
    }

    // attack
    public static void attack1(Player player){
        if (currentAnimation.isFlippedHorizontal()){
            player.removeAttachedImage(currentAnimation);
            currentAnimation = new AttachedImage(player, attack, 1.21f,0, new Vec2(1f, -0.1f));
            player.turn(player);
        } else {
            player.removeAttachedImage(currentAnimation);
            currentAnimation = new AttachedImage(player, attack, 1.21f,0, new Vec2(1f,-0.1f));
        }
    }

    // block
    public static void block(Player player){
        if (currentAnimation.isFlippedHorizontal()){
            player.removeAttachedImage(currentAnimation);
            currentAnimation = new AttachedImage(player, block, 1.3f,0, new Vec2(0, 0.1f));
            player.turn(player);
        } else {
            player.removeAttachedImage(currentAnimation);
            currentAnimation = new AttachedImage(player, block, 1.3f,0, new Vec2(0,0.1f));
        }
    }

    // heal
    public static void healCostume(Player player) {
        player.removeAttachedImage(currentAnimation);
        currentAnimation = new AttachedImage(player, heal, 1.4f, 0, new Vec2(0,0.22f));
    }

    public static void turn(Player player){
        currentAnimation.flipHorizontal();
    }

    public static void takeDamage(Player player){
        currentAnimation = new AttachedImage(player, hurt, 1.4f,0, new Vec2(-0.5f,0.9f));
    }
}