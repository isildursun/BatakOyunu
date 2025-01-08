import javax.swing.*;
import java.awt.*;

public class BatakGUI {
    private JFrame frame;
    private JPanel player1Panel, player2Panel, leftPanel, centerPanel;
    private static JLabel trumpLabel, scoreLabel;
    private JLabel player1Label, player2Label, defaultPlayer2Label;
    private JLabel playedCard1Label, defaultCardLabel;
    private GameLogic gameLogic;

    public BatakGUI() {
        gameLogic = new GameLogic();
        initializeGUI();
    }

    private void initializeGUI() {
        // Main frame
        frame = new JFrame("Batak Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen mode
        frame.setLayout(new BorderLayout());

        // Player 2 cards (top panel)
        player2Panel = new JPanel(new BorderLayout());
        JPanel player2CardsPanel = new JPanel(new GridLayout(2, 8));

        // Adding default.png to Player 2 label
        player2Label = new JLabel("Player 2", JLabel.LEFT);
        player2Label.setFont(new Font("Arial", Font.BOLD, 20));
        defaultPlayer2Label = new JLabel(scaleCardImage("images/default.png", 75, 112)); // Default image for Player 2

        JPanel player2LabelPanel = new JPanel(new BorderLayout());
        player2LabelPanel.add(player2Label, BorderLayout.WEST); // Player 2 text on the left
        player2LabelPanel.add(defaultPlayer2Label, BorderLayout.CENTER); // Default card image in the center

        player2Panel.add(player2LabelPanel, BorderLayout.SOUTH); // Bottom-left corner
        player2Panel.add(player2CardsPanel, BorderLayout.CENTER);
        frame.add(player2Panel, BorderLayout.NORTH);

        // Player 1 cards (bottom panel)
        player1Panel = new JPanel(new BorderLayout());
        JPanel player1CardsPanel = new JPanel(new GridLayout(2, 8));
        player1Label = new JLabel("Player 1", JLabel.LEFT);
        player1Label.setFont(new Font("Arial", Font.BOLD, 20));
        player1Panel.add(player1Label, BorderLayout.NORTH); // Top-left corner
        player1Panel.add(player1CardsPanel, BorderLayout.CENTER);
        frame.add(player1Panel, BorderLayout.SOUTH);

        // Left side (trump and scores)
        leftPanel = new JPanel(new GridLayout(2, 1));
        trumpLabel = new JLabel("Trump: Not set yet", JLabel.CENTER);
        scoreLabel = new JLabel("Scores - Player 1: 0, Player 2: 0", JLabel.CENTER);
        trumpLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        leftPanel.add(trumpLabel);
        leftPanel.add(scoreLabel);
        frame.add(leftPanel, BorderLayout.WEST);

        // Center panel (played cards and default card)
        centerPanel = new JPanel(new GridLayout(1, 2)); // Played card + Default card
        playedCard1Label = new JLabel("", JLabel.CENTER);
        defaultCardLabel = new JLabel("", JLabel.CENTER);
        defaultCardLabel.setIcon(scaleCardImage("images/default.png", 100, 150)); // Default card image

        centerPanel.add(playedCard1Label);
        centerPanel.add(defaultCardLabel);
        frame.add(centerPanel, BorderLayout.CENTER);

        updatePlayerPanels();

        frame.setVisible(true);
    }

    private void updatePlayerPanels() {
        JPanel player1CardsPanel = (JPanel) player1Panel.getComponent(1);
        JPanel player2CardsPanel = (JPanel) player2Panel.getComponent(1);

        player1CardsPanel.removeAll();
        player2CardsPanel.removeAll();

        boolean isPlayer1Turn = gameLogic.isPlayer1Turn();

        // Kart boyutları
        int cardWidth = 75; // Genişlik
        int cardHeight = 112; // Yükseklik

        // Player 2 cards
        for (Card card : gameLogic.getPlayer2Cards()) {
            JButton cardButton = createCardButton(card, cardWidth, cardHeight, !isPlayer1Turn);
            player2CardsPanel.add(cardButton);
        }

        // Player 1 cards
        for (Card card : gameLogic.getPlayer1Cards()) {
            JButton cardButton = createCardButton(card, cardWidth, cardHeight, isPlayer1Turn);
            player1CardsPanel.add(cardButton);
        }

        player1CardsPanel.revalidate();
        player1CardsPanel.repaint();
        player2CardsPanel.revalidate();
        player2CardsPanel.repaint();
    }

    private JButton createCardButton(Card card, int width, int height, boolean isEnabled) {
        ImageIcon icon = new ImageIcon(card.getImagePath());
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton cardButton = new JButton(scaledIcon);
        cardButton.setEnabled(isEnabled);
        cardButton.addActionListener(e -> {
            gameLogic.selectCard(card);
            updateGamePanel();
        });

        return cardButton;
    }

    private void updateGamePanel() {
        // Update trump and scores
        trumpLabel.setText("Trump: " + gameLogic.getTrumpSuit());
        scoreLabel.setText("Scores - Player 1: " + gameLogic.getPlayer1Score() +
                ", Player 2: " + gameLogic.getPlayer2Score());

        // Update played cards
        Card playedCard = gameLogic.getLastPlayedCard();
        int cardWidth = 100; // Ortadaki kart genişliği
        int cardHeight = 150; // Ortadaki kart yüksekliği

        if (playedCard != null) {
            playedCard1Label.setIcon(scaleCardImage(playedCard.getImagePath(), cardWidth, cardHeight));
        } else {
            playedCard1Label.setIcon(null);
        }

        // Remove default card after "Distributing the remaining cards..."
        if (gameLogic.getPlayer1Cards().isEmpty() && gameLogic.getPlayer2Cards().isEmpty()) {
            defaultCardLabel.setIcon(null);
            defaultPlayer2Label.setIcon(null); // Remove Player 2 default card
        }

        updatePlayerPanels();

        // Check if the game is over
        if (gameLogic.getPlayer1Cards().isEmpty() && gameLogic.getPlayer2Cards().isEmpty()) {
            showFinalScores();
        }
    }

    private ImageIcon scaleCardImage(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private void showFinalScores() {
        JOptionPane.showMessageDialog(frame, "Game Over!\n" +
                "Player 1 Score: " + gameLogic.getPlayer1Score() + "\n" +
                "Player 2 Score: " + gameLogic.getPlayer2Score(), "Game End", JOptionPane.INFORMATION_MESSAGE);

        // Display the winner
        showWinner();
    }

    private void showWinner() {
        String winnerMessage;
        if (gameLogic.getPlayer1Score() > gameLogic.getPlayer2Score()) {
            winnerMessage = "Winner: Player 1!";
        } else if (gameLogic.getPlayer1Score() < gameLogic.getPlayer2Score()) {
            winnerMessage = "Winner: Player 2!";
        } else {
            winnerMessage = "It's a Tie!";
        }

        JFrame winnerFrame = new JFrame("Winner");
        winnerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        winnerFrame.setSize(400, 200);
        winnerFrame.setLayout(new BorderLayout());

        JLabel winnerLabel = new JLabel(winnerMessage, JLabel.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        winnerFrame.add(winnerLabel, BorderLayout.CENTER);

        winnerFrame.setVisible(true);
    }

    public static void showInvalidSelectionMessage() {
        JOptionPane.showMessageDialog(null, "Invalid move! Please select again.", "Error", JOptionPane.WARNING_MESSAGE);
    }

    public static void showCardDistributionMessage() {
        JOptionPane.showMessageDialog(null, "Distributing the remaining cards...", "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
