import java.io.File;

public class TestCard {
    public static void main(String[] args) {
        // Test için birkaç kart oluştur
        Card card1 = new Card("A", "Spades"); // As Maça
        Card card2 = new Card("10", "Hearts"); // 10 Kupa
        Card card3 = new Card("7", "Diamonds"); // 7 Karo
        Card card4 = new Card("2", "Clubs"); // 2 Sinek

        // Görsel yollarını yazdır
        testCardImage(card1);
        testCardImage(card2);
        testCardImage(card3);
        testCardImage(card4);
    }

    private static void testCardImage(Card card) {
        // getImagePath metodundan görsel yolunu al
        String imagePath = card.getImagePath();
        System.out.println("Testing card: " + card.toString());
        System.out.println("Image Path: " + imagePath);

        try {
            // Dosyanın gerçekten var olup olmadığını kontrol et
            File file = new File(imagePath);
            if (file.exists()) {
                System.out.println("Image found: " + imagePath);
            } else {
                System.err.println("Image not found: " + imagePath);
            }
        } catch (Exception e) {
            // Herhangi bir hata durumunda istisnayı yazdır
            System.err.println("An error occurred while testing the image path: " + e.getMessage());
        }
        System.out.println("-------------------------------------");
    }
}
