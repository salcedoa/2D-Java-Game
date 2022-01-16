package game;

import city.cs.engine.World;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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
        sideMenu controlPanel = new sideMenu(this);
        frame.add(view);
        frame.add(controlPanel, BorderLayout.SOUTH);
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
        } else if (currentLevel instanceof Level3) {
            currentLevel = new Level1(this);
            view.setWorld(currentLevel);
            view.updateBackground();
            controller.updateLevel(currentLevel);
            currentLevel.start();
        }
    }

    // called if the player's health is lower than 1
    // when run for the first time it creates a text file called "high_scores" in the data folder
    public void endGame() {
        if (view.getScore() > this.getHighScore()) {
            try {
                File scoresFile = new File("data/high_scores.txt");
                scoresFile.createNewFile(); // returns false if file already exists
                FileWriter scoreRecorder = new FileWriter("data/high_scores.txt");
                scoreRecorder.write(String.valueOf(view.getScore()));
                scoreRecorder.close();
            } catch (IOException e) {
                System.out.println("An error occurred");
                e.printStackTrace();
            }
        }
        System.exit(0); // TODO: return to main menu instead close window
    }

    public int getHighScore() {
        try {
            File scoresFile = new File ("data/high_scores.txt");
            Scanner reader = new Scanner(scoresFile);
            if (reader.hasNextLine()) {
                return reader.nextInt();
            } else {
                return 0; // if the file is empty then no high score has been set
            }
        } catch (FileNotFoundException e) {
            System.out.println("high_scores.txt does not exist");
            e.printStackTrace();
            return 0;
        }
    }

    public static void main(String[] args) {
        new Game();
    }
}