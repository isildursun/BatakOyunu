import javax.swing.*;

public class ImageTest {
    public static void main(String[] args) {
        // Create a simple frame
        JFrame frame = new JFrame("Card Image Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);

        // Create a card to test the image path
        Card testCard = new Card("A", "Spades");
        String imagePath = testCard.getImagePath();

        // Load the image as an icon
        ImageIcon cardImage = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(cardImage);

        // Add the image to the frame
        frame.add(imageLabel);
        frame.setVisible(true);
    }
}
