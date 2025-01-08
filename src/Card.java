import java.io.File;

public class Card implements Comparable<Card> {
    private String value; // Card value (A, K, Q, J, 10, ...)
    private String suit;  // Card suit (Hearts, Spades, Diamonds, Clubs)

    public Card(String value, String suit) {
        this.value = value;
        this.suit = suit;
    }

    public String getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    public int getNumericValue() {
        switch (value) {
            case "A": return 14;
            case "K": return 13;
            case "Q": return 12;
            case "J": return 11;
            default: return Integer.parseInt(value);
        }
    }

    public int getSuitOrder() {
        switch (suit) {
            case "Hearts": return 1;
            case "Spades": return 2;
            case "Diamonds": return 3;
            case "Clubs": return 4;
            default: return 5; // Default: lowest priority
        }
    }

    /**
     * Returns the file path for the card's image.
     * Example: "images/card_spades_A.png" for "A Spades"
     * If the image is missing, a default image is returned.
     */
    public String getImagePath() {
        String suitName = "";
        switch (suit) {
            case "Hearts": suitName = "hearts"; break;
            case "Spades": suitName = "spades"; break;
            case "Diamonds": suitName = "diamonds"; break;
            case "Clubs": suitName = "clubs"; break;
        }

        String cardValue = value;
        if (value.matches("\\d+")) { // Format numeric values as 2 digits (e.g., "02" for 2)
            cardValue = String.format("%02d", Integer.parseInt(value));
        }

        String resourcePath = "images/card_" + suitName + "_" + cardValue + ".png";
        String fullPath;

        try {
            // Use ClassLoader to locate the images
            fullPath = getClass().getClassLoader().getResource(resourcePath).getPath();
        } catch (Exception e) {
            System.err.println("Image not found: " + resourcePath);
            fullPath = getClass().getClassLoader().getResource("images/default.png").getPath();
        }

        return fullPath;
    }


    @Override
    public int compareTo(Card other) {
        if (this.getSuitOrder() != other.getSuitOrder()) {
            return Integer.compare(this.getSuitOrder(), other.getSuitOrder());
        }
        return Integer.compare(other.getNumericValue(), this.getNumericValue());
    }

    @Override
    public String toString() {
        return value + " of " + suit;
    }
}
