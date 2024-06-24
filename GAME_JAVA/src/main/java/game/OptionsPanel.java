package game;

import game.Character;
import game.Leticia;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class OptionsPanel extends JPanel {
    private JButton attackButton;
    private JButton defendButton;
    private JButton swapButton;
    private JButton specialButton;
    private JLabel messageLabel;
    private Font retroFont;
    private List<java.lang.Character> heroes; // Lista de personagens.
    private int currentHero; // Índice do herói atual.

    public OptionsPanel(List<java.lang.Character> heroes, int currentHero) {
        this.heroes = heroes;  // Inicializa a lista de personagens
        this.currentHero = currentHero; // Inicializa o índice do herói atual

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
        setupListeners(); // Configura os listeners para os botões, sem ela, os botões não terão ações associadas.
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

    public void setFightActionListener(java.awt.event.ActionListener listener, Character c) {
        attackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (heroes.size() > 1) { // Garante que há pelo menos dois heróis na lista
                    heroes.get(currentHero).attack(heroes.get(1)); // O herói atual ataca o inimigo
                    System.out.println(heroes.get(currentHero).getName() + " attacked " + heroes.get(1).getName() + "!");
                } else {
                    System.out.println("Not enough heroes to attack."); // Mensagem de erro se não houver heróis suficientes
                }
            }
        });
    }

    public void setBagActionListener(java.awt.event.ActionListener listener) {
        defendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (heroes.size() > 1) { // Garante que há pelo menos dois heróis na lista
                    heroes.get(currentHero).defend(heroes.get(1)); // O herói atual defende o próximo herói
                    System.out.println(heroes.get(currentHero).getName() + " defended " + heroes.get(1).getName() + "!");
                } else {
                    System.out.println("Not enough heroes to defend."); // Mensagem de erro se não houver heróis suficientes
                }
            }
        });
    }

    public void setPokemonActionListener(java.awt.event.ActionListener listener) {
        swapButton.addActionListener(listener);swapButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (heroes.size() > 1) { // Garante que há pelo menos dois heróis na lista
                    heroes.get(currentHero).swap(heroes.get(1)); // O herói atual troca de posição com o próximo herói
                    System.out.println(heroes.get(currentHero).getName() + " swapped with " + heroes.get(1).getName() + "!");
                } else {
                    System.out.println("Not enough heroes to swap."); // Mensagem de erro se não houver heróis suficientes
                }
            }
        });
    }

    public void setRunActionListener(java.awt.event.ActionListener listener) {
        specialButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (heroes.size() > 1) { // Garante que há pelo menos dois heróis na lista
                    heroes.get(currentHero).special(heroes.get(1)); // O herói atual usa uma habilidade especial no próximo herói
                    System.out.println(heroes.get(currentHero).getName() + " used special on " + heroes.get(1).getName() + "!");
                } else {
                    System.out.println("Not enough heroes to use special."); // Mensagem de erro se não houver heróis suficientes
                }
            }
        });
    }

    public void updateMessage(String message) {
        messageLabel.setText(message);
    }
}

