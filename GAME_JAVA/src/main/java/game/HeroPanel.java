import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.Image;
import java.net.URL;

public class HeroPanel extends JPanel {
    private JLabel heroLabel;
    private JProgressBar healthBar;
    private Image heroImage;

    public HeroPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());

        // Carregar a imagem do herói
        URL heroUrl = getClass().getResource("/img/hero_faria.png");
        if (heroUrl != null) {
            ImageIcon heroIcon = new ImageIcon(heroUrl);
            heroImage = heroIcon.getImage();
            heroLabel = new JLabel(new ImageIcon(heroImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        } else {
            heroLabel = new JLabel("Hero Image Not Found");
        }

        healthBar = new JProgressBar();
        healthBar.setValue(100); // Valor inicial da vida
        healthBar.setStringPainted(true);


        add(heroLabel, BorderLayout.CENTER);

    }

    public void updateHealth(int health) {
        healthBar.setValue(health);
    }
}