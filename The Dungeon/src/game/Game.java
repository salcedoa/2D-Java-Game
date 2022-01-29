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
    public String gameVersion = "v.1.0.0";

    private JFrame frame = new JFrame("The Dungeon" + " - " + gameVersion);    // a new window that will be used to look at the game world is created

    private JPanel containerPanel = new JPanel(); // main panel that will hold the menu and game screens
    public JPanel getContainerPanel() { return containerPanel; }
    private Menu menu = new Menu(this);

    private CardLayout cl = new CardLayout();
    public CardLayout getCl() { return cl; }

    JPanel gamePanel = new JPanel();

    public Game() {
        containerPanel.setLayout(cl); // layout set as card layout

        // both cards are added to the container panel with corresponding identifiers
        containerPanel.add(menu, "menu");
        containerPanel.add(gamePanel, "game");
        // first card shown when the application is run
        cl.show(containerPanel, "menu");

        //frame.setGridResolution(1);
        frame.setPreferredSize(new Dimension(800,573));
        frame.add(containerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public void startGame() {
        currentLevel = new Level1(this);
        levelNumber = 1;
        cachedScore = 0;
        
        controller = new PlayerController(currentLevel);

        view = new GameView(currentLevel, this);
        view.updateBackground();
        view.addMouseListener(new GiveFocus(view)); // using the GiveFocus class
        view.addKeyListener(controller);

        sideMenu controlPanel = new sideMenu(this);
        gamePanel.add(view);
        gamePanel.add(controlPanel, BorderLayout.SOUTH);

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
        } else if (currentLevel instanceof Level2) {
            currentLevel = new Level3(this);
        } else if (currentLevel instanceof Level3) {
            currentLevel = new Level1(this);
        }
        view.setWorld(currentLevel);
        view.updateBackground();
        controller.updateLevel(currentLevel);
        currentLevel.start();
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
        currentLevel.stop();
        gamePanel.removeAll();  // panel is cleared so that it can be loaded again without problem
        menu.getMenuScore().setText("High Score: " + String.valueOf(getHighScore()));
        cl.show(containerPanel,"menu");
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
            return 0;
        }
    }

    public static void main(String[] args) {
        new Game();
    }
}