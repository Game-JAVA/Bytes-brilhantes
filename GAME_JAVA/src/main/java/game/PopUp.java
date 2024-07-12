package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PopUp extends JDialog {
    private Character selectedCharacter; // Personagem selecionado pelo jogador
    private List<Character> heroes; // Lista de personagens disponíveis para seleção
    private int currentHeroIndex; // Índice do herói atual que será trocado
    private boolean isReviving; // Indica se Valentina está tentando reviver um personagem

    // Construtor da classe PopUp
    public PopUp(Frame owner, List<Character> characters, int currentHeroIndex, boolean isReviving) {
        super(owner, isReviving ? "Select a Character to Revive" : "Select a Character to Swap", true); // Chama o construtor da superclasse JDialog
        this.heroes = characters; // Inicializa a lista de personagens
        this.currentHeroIndex = currentHeroIndex; // Inicializa o índice do herói atual
        this.isReviving = isReviving; // Inicializa se é uma ação de reviver
        initialize(); // Inicializa o diálogo
    }

    // Método para inicializar o diálogo
    private void initialize() {
        setLayout(new GridLayout(heroes.size() - 1, 1)); // Define o layout do diálogo com uma coluna e número de linhas igual ao número de personagens - 1

        // Adiciona botões para cada personagem, exceto o personagem atual
        for (int i = 0; i < heroes.size(); i++) {
            if (i != currentHeroIndex) { // Verifica se o índice é diferente do índice do herói atual
                Character character = heroes.get(i); // Obtém o personagem na posição i da lista
                if (isReviving && character.getHealth() > 0) {
                    continue; // Se estamos revivendo, pule os personagens com vida > 0
                } else if (!isReviving && character.getHealth() <= 0) {
                    continue; // Se estamos revivendo, pule os personagens com vida > 0
                }
                JButton button = new JButton(character.getName()); // Cria um botão com o nome do personagem
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectedCharacter = character; // Define o personagem selecionado como o personagem atual
                        setVisible(false); // Esconde o diálogo após a seleção do personagem
                    }
                });
                add(button); // Adiciona o botão ao diálogo
            }
        }

        setSize(300, 200); // Define o tamanho do diálogo
        setLocationRelativeTo(getOwner()); // Define a posição do diálogo centralizado em relação à janela proprietária
    }

    // Método para obter o personagem selecionado
    public Character getSelectedCharacter() {
        return selectedCharacter; // Retorna o personagem selecionado
    }
}
