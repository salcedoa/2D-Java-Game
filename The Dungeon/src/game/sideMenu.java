package game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class sideMenu extends JPanel {
    private Game game;
    public sideMenu(Game g){
        game = g;
        // initialising components
        JButton pauseButton = new JButton("Pause");
        JButton mainMenuButton = new JButton("Main Menu");
        JButton closeButton = new JButton("Exit");

        // button functions
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.endGame();
                game.getCl().show(game.getContainerPanel(),"menu");
            }
        });

        pauseButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.getLevel().isRunning()) {
                    game.getLevel().stop();
                    pauseButton.setText("Resume");
                } else {
                    game.getLevel().start();
                    pauseButton.setText("Pause");
                }
                revalidate();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        // adding components
        this.add(mainMenuButton);
        this.add(pauseButton);
        this.add(closeButton);
    }
}