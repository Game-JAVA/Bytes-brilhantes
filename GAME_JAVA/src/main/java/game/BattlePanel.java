import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.BorderLayout;
import java.net.URL;

public class BattlePanel extends JPanel {
    private Image backgroundImage;

    public BattlePanel() {
        // Carregar a imagem de fundo
        URL imageUrl = getClass().getResource("/img/Background.png");
        System.out.println("Path to image: " + imageUrl);
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            backgroundImage = icon.getImage();
        } else {
            System.out.println("Imagem de fundo não encontrada! Verifique o caminho.");
        }

        setLayout(new BorderLayout());

        HeroPanel heroPanel = new HeroPanel();
        EnemyPanel enemyPanel = new EnemyPanel();
        OptionsPanel optionsPanel = new OptionsPanel();



        add(heroPanel, BorderLayout.WEST);
        add(enemyPanel, BorderLayout.EAST);
        add(optionsPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Desenhar a imagem de fundo
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}