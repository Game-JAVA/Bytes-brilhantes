import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.io.InputStream;
import java.awt.FontFormatException;
import java.io.IOException;
import javax.swing.border.EmptyBorder;

public class OptionsPanel extends JPanel {
    private JButton attackButton;
    private JButton defendButton;
    private JButton swapButton;
    private JButton specialButton;
    private JLabel messageLabel;
    private Font retroFont;

    public OptionsPanel() {
        loadFont();

        setOpaque(false);
        setLayout(new BorderLayout());

        messageLabel = new JLabel("What will you do?");
        messageLabel.setFont(retroFont.deriveFont(14f)); // Aumentar a fonte da mensagem
        messageLabel.setForeground(Color.WHITE); // Definir a cor da mensagem como branca
        messageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(messageLabel, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new GridLayout(2, 2, 10, 10));

        attackButton = createButton("Attack");
        defendButton = createButton("Defend");
        swapButton = createButton("Swap");
        specialButton = createButton("Special");

        buttonsPanel.add(attackButton);
        buttonsPanel.add(defendButton);
        buttonsPanel.add(swapButton);
        buttonsPanel.add(specialButton);

        add(buttonsPanel, BorderLayout.CENTER);
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

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(retroFont.deriveFont(12f)); // Definir a fonte dos botões
        button.setPreferredSize(new Dimension(100, 50)); // Definir o tamanho dos botões
        return button;
    }

    public void setFightActionListener(java.awt.event.ActionListener listener) {
        attackButton.addActionListener(listener);
    }

    public void setBagActionListener(java.awt.event.ActionListener listener) {
        defendButton.addActionListener(listener);
    }

    public void setPokemonActionListener(java.awt.event.ActionListener listener) {
        swapButton.addActionListener(listener);
    }

    public void setRunActionListener(java.awt.event.ActionListener listener) {
        specialButton.addActionListener(listener);
    }

    public void updateMessage(String message) {
        messageLabel.setText(message);
    }
}