package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Menu extends JPanel {
    private Game game;
    private ImageIcon bgImage = new ImageIcon("./data/dungeon.png");

    // initialising components
    // mainButtons
    private static final JLabel title = new JLabel(new ImageIcon("data/title.png"));
    private static final JLabel playerImage = new JLabel(new ImageIcon("data/player/idle.png"));
    private static final JButton startButton = new JButton("Start");
    private static final JButton closeButton = new JButton("Exit");

    private static final JLabel watermark = new JLabel("salcedoa.github.io");

    // to add a background image, paintComponent must be overidden
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(bgImage.getImage(),0,0,null);
    }

    public Menu(Game g) {
        game = g;

        // initialising components
        // mainButtons
        playerImage.setMaximumSize(new Dimension(60,100));
        startButton.setMaximumSize(new Dimension(90,40)); // sizing the start button

        // other labels
        JLabel menuScore = new JLabel("High Score: " + String.valueOf(game.getHighScore()));
        menuScore.setFont(new Font("Courier New", Font.BOLD, 21));
        menuScore.setForeground(Color.WHITE);

        JLabel version = new JLabel(game.gameVersion);
        version.setFont(new Font("Courier New", Font.BOLD, 16));
        version.setForeground(Color.WHITE);

        watermark.setFont(new Font("Courier New", Font.BOLD, 16));
        watermark.setForeground(Color.WHITE);
        watermark.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // cursor becomes hand when hovered over

        // when JLabel watermark is clicked it will take the user to salcedoa.github.io
        watermark.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // when user clicks on label
                super.mouseClicked(e);
                try {
                    Desktop.getDesktop().browse(new URI("https://salcedoa.github.io/"));
                } catch (IOException | URISyntaxException exception) {
                    System.out.println("Link could not be opened");
                    exception.printStackTrace();
                }
            }
        });

        // this JPanel will keep the title and the buttons close together in BoxLayout
        JPanel mainButtons = new JPanel();
        mainButtons.setLayout(new BoxLayout(mainButtons, BoxLayout.Y_AXIS));

        // Centering the components
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainButtons.add(title);
        mainButtons.add(playerImage);
        mainButtons.add(startButton);
        mainButtons.add(closeButton);

        // button functions
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.startGame();
                game.getCl().show(game.getContainerPanel(), "game");
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.setLayout(null);

        // adding components
        this.add(mainButtons);
        mainButtons.setOpaque(false); // panel is made transparent so that it doesn't interfere with background colour
        mainButtons.setBounds(210, 70, 400, 400);

        this.add(menuScore);
        menuScore.setBounds(315, 460, 200, 20);
        this.add(version);
        version.setBounds(680, 10, 200, 20);
        this.add(watermark);
        watermark.setBounds(30, 10, 185, 20);
    }
}
