package game;

import city.cs.engine.World;

import javax.swing.*;

public class Game extends World {
    private GameLevel currentLevel;
    private GameView view;

    private PlayerController controller;

    private DamageZone sword;

    public Game() {
        startGame();
    }

    public void mainMenu() {}

    public void startGame() {
        currentLevel = new Level1(this);
        controller = new PlayerController(currentLevel, sword);

        view = new GameView(currentLevel);
        view.addMouseListener(new GiveFocus(view)); // using the GiveFocus class
        view.addKeyListener(controller);
        JFrame frame = new JFrame("The Dungeon");    // a new window that will be used to look at the game world is created
        frame.add(view);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        currentLevel.start();
    }

    public static void main(String[] args) {
        new Game();
    }

    public DamageZone getSword() {
        return sword;
    }
}