import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.JRootPane;

public class MainFrame extends JFrame {
    private boolean isPaused = false;
    private PauseOverlay pauseOverlay;

    public MainFrame() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        BattlePanel battlePanel = new BattlePanel();
        add(battlePanel);

        pauseOverlay = new PauseOverlay();
        JRootPane rootPane = this.getRootPane();
        rootPane.setGlassPane(pauseOverlay);
        pauseOverlay.setVisible(false);

        rootPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                } else if (isPaused) {
                    togglePause(); // Qualquer tecla para retomar
                }
            }
        });
        rootPane.setFocusable(true);
        rootPane.requestFocus();
    }

    private void togglePause() {
        isPaused = !isPaused;
        pauseOverlay.setPaused(isPaused);
        pauseOverlay.setVisible(isPaused);
        repaint();
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }
}
