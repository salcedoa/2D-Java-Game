package game;

import city.cs.engine.World;

import javax.swing.*;

public class Game extends World {
    private GameLevel currentLevel;
    public static int levelNumber;
    private GameView view;

    private PlayerController controller;

    public Game() {
        startGame();
    }

    public void mainMenu() {}

    public void startGame() {
        currentLevel = new Level1(this);
        levelNumber = 1;

        controller = new PlayerController(currentLevel);

        view = new GameView(currentLevel, this);
        view.updateBackground();
        view.addMouseListener(new GiveFocus(view)); // using the GiveFocus class
        view.addKeyListener(controller);
        JFrame frame = new JFrame("The Dungeon");    // a new window that will be used to look at the game world is created
        frame.add(view);
        //frame.setGridResolution(1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        currentLevel.start();
    }

    public GameLevel getLevel() { return currentLevel; }

    // keeps the value of the sum of levelScore from previous levels
    private int cachedScore = 0;
    public int getCachedScore() { return cachedScore; }
    public void addToCachedScore(int score) { this.cachedScore += score; }

    public void goToNextLevel() {
        addToCachedScore(currentLevel.getLevelScore());
        currentLevel.setLevelScore(0);
        currentLevel.stop();
        if (currentLevel instanceof Level1) {
            currentLevel = new Level2(this);
            // currentLevel now refers to new level
            view.setWorld(currentLevel);
            view.updateBackground();
            controller.updateLevel(currentLevel);
            currentLevel.start();
        } else if (currentLevel instanceof Level2) {
            currentLevel = new Level3(this);
            view.setWorld(currentLevel);
            view.updateBackground();
            controller.updateLevel(currentLevel);
            currentLevel.start();
        } // TODO: create winning screen or go back to menu OR loop back to level 1
    }

    public static void main(String[] args) {
        new Game();
    }
}