import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    private JLabel playerLabel;
    private JLabel opponentLabel;
    private JProgressBar playerHealth;
    private JProgressBar opponentHealth;

    public StatusPanel() {
        setOpaque(false); // Tornar o painel transparente
        setLayout(new GridLayout(1, 2));

        JPanel playerPanel = new JPanel();
        playerPanel.setOpaque(false); // Tornar o painel transparente
        playerLabel = new JLabel("Player");
        playerLabel.setForeground(Color.WHITE);



        playerPanel.add(playerLabel);


        JPanel opponentPanel = new JPanel();
        opponentPanel.setOpaque(false); // Tornar o painel transparente
        opponentLabel = new JLabel("Opponent");
        opponentLabel.setForeground(Color.WHITE);

        opponentPanel.add(opponentLabel);
        opponentPanel.add(opponentHealth);

        add(playerPanel);
        add(opponentPanel);
    }

    public void updatePlayerHealth() {

    }

    public void updateOpponentHealth() {

    }
}