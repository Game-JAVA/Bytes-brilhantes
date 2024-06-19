import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.io.InputStream;
import java.awt.FontFormatException;
import java.io.IOException;

public class MessagePanel extends JPanel {
    private JLabel messageLabel;
    private Font retroFont;

    public MessagePanel() {
        setOpaque(false);
        loadFont();
        messageLabel = new JLabel("What will you do?");
        messageLabel.setFont(retroFont.deriveFont(14f)); // Definir a fonte da mensagem
        messageLabel.setForeground(Color.WHITE); // Definir a cor da mensagem como branca
        add(messageLabel);
    }

    private void loadFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            retroFont = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            retroFont = new Font("Serif", Font.PLAIN, 14); // Fonte padrão em caso de erro
        }
    }

    public void updateMessage(String message) {
        messageLabel.setText(message);
    }
}