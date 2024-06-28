package game; // Declara o pacote do programa

import javax.swing.*; // Importa a biblioteca Swing para GUI

public class DefeatScreen{

    JFrame f; // Declara um JFrame
    ImageIcon img; // Declara um ImageIcon
    JLabel l; // Declara um JLabel

    DefeatScreen() {
        f = new JFrame("Derrota"); // Cria um JFrame com o título "Derrota"
        img = new ImageIcon("img/Tela de Derrota.gif"); // Carrega a imagem GIF

        l = new JLabel("", img, JLabel.CENTER); // Cria um JLabel com a imagem centralizada
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha o programa ao fechar a janela
        f.setSize(600, 400); // Define o tamanho da janela
        f.setLocationRelativeTo(null); // Centraliza a janela na tela
        f.add(l); // Adiciona o JLabel ao JFrame
        f.setVisible(true); // Torna a janela visível
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DefeatScreen()); // Cria a GUI na Event Dispatch Thread
    }
}

//voltar tudo do Character (image/extends//)