package game;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;

public class PauseOverlay extends JPanel {
    private boolean paused;
    private game.Image pauseImage;

    public PauseOverlay() {
        setOpaque(false);

        pauseImage = new game.Image("img/Pause.png", 327, 162);
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
                pauseImage.draw(g);
            }
            g2d.dispose();
        }
    }
}
