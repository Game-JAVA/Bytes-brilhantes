package game;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class PauseOverlay extends JPanel {
    private boolean paused;
    private Image pauseImage;

    public PauseOverlay() {
        setOpaque(false);

        // Carregar a imagem de pausa
        URL pauseUrl = getClass().getResource("/img/Pause.png");
        if (pauseUrl != null) {
            pauseImage = new ImageIcon(pauseUrl).getImage();
        }
        paused = false;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (paused) {
            Graphics2D g2d = (Graphics2D) g.create();
            // Desfoque leve no fundo
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            // Desenhar a imagem de pausa no centro
            if (pauseImage != null) {
                int imgWidth = pauseImage.getWidth(this);
                int imgHeight = pauseImage.getHeight(this);
                int x = (getWidth() - imgWidth) / 2;
                int y = (getHeight() - imgHeight) / 2;
                g2d.drawImage(pauseImage, x, y, this);
            }
            g2d.dispose();
        }
    }
}
