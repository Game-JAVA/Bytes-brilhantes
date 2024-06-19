import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.Image;
import java.net.URL;

public class EnemyPanel extends JPanel {
    private JLabel enemyLabel;
    private JProgressBar healthBar;
    private Image enemyImage;

    public EnemyPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());

        // Carregar a imagem do vilão
        URL enemyUrl = getClass().getResource("/img/enemy.png");
        if (enemyUrl != null) {
            ImageIcon enemyIcon = new ImageIcon(enemyUrl);
            enemyImage = enemyIcon.getImage();
            enemyLabel = new JLabel(new ImageIcon(enemyImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        } else {
            enemyLabel = new JLabel("Enemy Image Not Found");
        }

        healthBar = new JProgressBar();
        healthBar.setValue(100); // Valor inicial da vida
        healthBar.setStringPainted(true);


        add(enemyLabel, BorderLayout.CENTER);
    }

    public void updateHealth(int health) {
        healthBar.setValue(health);
    }
}