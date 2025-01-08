import java.util.ArrayList;
import java.util.Collections;

public class GameLogic {
    private ArrayList<Card> player1Cards;
    private ArrayList<Card> player2Cards;
    private ArrayList<Card> remainingDeck;
    private String trumpSuit = "";
    private int player1Score = 0;
    private int player2Score = 0;
    private boolean player1Turn = true;
    private Card player1Card = null;
    private Card player2Card = null;
    private Card lastPlayedCard = null; // Son oynanan kartı tutmak için

    public GameLogic() {
        initializeGame();
    }

    private void initializeGame() {
        ArrayList<Card> deck = new ArrayList<>();
        String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};
        String[] values = {"A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2"};

        for (String suit : suits) {
            for (String value : values) {
                deck.add(new Card(value, suit));
            }
        }

        Collections.shuffle(deck);

        player1Cards = new ArrayList<>(deck.subList(0, 16));
        player2Cards = new ArrayList<>(deck.subList(16, 32));
        remainingDeck = new ArrayList<>(deck.subList(32, 52));

        player1Cards.sort(Collections.reverseOrder());
        player2Cards.sort(Collections.reverseOrder());
    }

    public ArrayList<Card> getPlayer1Cards() {
        return player1Cards;
    }

    public ArrayList<Card> getPlayer2Cards() {
        return player2Cards;
    }

    public String getTrumpSuit() {
        return trumpSuit;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public boolean isPlayer1Turn() {
        return player1Turn;
    }

    public Card getPlayer1Card() {
        return player1Card;
    }

    public Card getPlayer2Card() {
        return player2Card;
    }

    public Card getLastPlayedCard() {
        return lastPlayedCard;
    }

    public void selectCard(Card card) {
        if (player1Turn) {
            if (!isValidMove(card, player1Cards)) {
                BatakGUI.showInvalidSelectionMessage();
                return;
            }
            player1Card = card;
            player1Cards.remove(card);
        } else {
            if (!isValidMove(card, player2Cards)) {
                BatakGUI.showInvalidSelectionMessage();
                return;
            }
            player2Card = card;
            player2Cards.remove(card);
        }

        lastPlayedCard = card; // Son oynanan kartı kaydet

        if (player1Card != null && player2Card != null) {
            evaluateTurn();
        } else {
            player1Turn = !player1Turn;
        }
    }

    private boolean isValidMove(Card selectedCard, ArrayList<Card> playerCards) {
        if (player1Card == null && player2Card == null) {
            return true;
        }

        String requiredSuit = (player1Card != null) ? player1Card.getSuit() : player2Card.getSuit();

        boolean hasSameSuit = playerCards.stream().anyMatch(card -> card.getSuit().equals(requiredSuit));
        if (hasSameSuit && !selectedCard.getSuit().equals(requiredSuit)) {
            return false;
        }

        boolean hasTrump = playerCards.stream().anyMatch(card -> card.getSuit().equals(trumpSuit));
        if (!hasSameSuit && hasTrump && !selectedCard.getSuit().equals(trumpSuit)) {
            return false;
        }

        return true;
    }

    private void evaluateTurn() {
        if (trumpSuit.isEmpty() && player1Card != null) trumpSuit = player1Card.getSuit();

        if (player1Card.getSuit().equals(player2Card.getSuit())) {
            if (player1Card.getNumericValue() > player2Card.getNumericValue()) {
                player1Score++;
                player1Turn = true;
            } else {
                player2Score++;
                player1Turn = false;
            }
        } else if (player2Card.getSuit().equals(trumpSuit)) {
            player2Score++;
            player1Turn = false;
        } else if (player1Card.getSuit().equals(trumpSuit)) {
            player1Score++;
            player1Turn = true;
        } else {
            player1Score++;
            player1Turn = true;
        }

        player1Card = null;
        player2Card = null;

        if (player1Cards.isEmpty() && player2Cards.isEmpty() && !remainingDeck.isEmpty()) {
            BatakGUI.showCardDistributionMessage();
            distributeRemainingCards();
        }
    }

    private void distributeRemainingCards() {
        if (!remainingDeck.isEmpty()) {
            BatakGUI.showCardDistributionMessage(); // Show distribution message
        }

        while (!remainingDeck.isEmpty()) {
            if (!remainingDeck.isEmpty()) player1Cards.add(remainingDeck.remove(0));
            if (!remainingDeck.isEmpty()) player2Cards.add(remainingDeck.remove(0));
        }

        player1Cards.sort(Collections.reverseOrder());
        player2Cards.sort(Collections.reverseOrder());
    }
}
