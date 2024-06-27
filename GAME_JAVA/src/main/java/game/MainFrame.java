package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.util.ArrayList;
import java.util.Objects;

public class MainFrame extends JFrame implements Runnable {
    private boolean isPaused = false;
    private PauseOverlay pauseOverlay;
    private int currentHero = 0;
    private Clock clock = Clock.systemDefaultZone();
    private long millis1 = clock.millis();
    private long millis2 = millis1;
    private Font retroFont;
    private ArrayList<Character> heroes; // Declara uma lista de personagens (heróis)
    private JLabel specialGifLabel; // Novo JLabel para exibir o GIF especial

    public MainFrame() {
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

        // Chama o metodo que realiza todas as configurações iniciais necessárias
        initComponents();

        // Seta a fonte do texto
        loadFont();

        // Mecanismo de execução paralela
        createBufferStrategy(2);
        Thread t = new Thread(this);
        t.start();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // Configura o layout da tela
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        // Largura
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1400, Short.MAX_VALUE)
        );
        // Altura
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 700, Short.MAX_VALUE)
        );

        pack();
    }

    private void togglePause() {
        isPaused = !isPaused;
        pauseOverlay.setPaused(isPaused);
        pauseOverlay.setVisible(isPaused);
        repaint();
    }

    // Carrega a fonte do texto
    private void loadFont() {
        try {
            InputStream is = getClass().getResourceAsStream("font/PressStart2P-Regular.ttf");
            retroFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(12f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            retroFont = new Font("Serif", Font.PLAIN, 14); // Fonte padrão em caso de erro
        }
    }

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    private String hpBar(Character c) {
        if (c.getHealth() >= 100) {
            return "img/Health/10.png";
        } else if (c.getHealth() >= 90) {
            return "img/Health/9.png";
        } else if (c.getHealth() >= 80) {
            return "img/Health/8.png";
        } else if (c.getHealth() >= 70) {
            return "img/Health/7.png";
        } else if (c.getHealth() >= 60) {
            return "img/Health/6.png";
        } else if (c.getHealth() >= 50) {
            return "img/Health/5.png";
        } else if (c.getHealth() >= 40) {
            return "img/Health/4.png";
        } else if (c.getHealth() >= 30) {
            return "img/Health/3.png";
        } else if (c.getHealth() >= 20) {
            return "img/Health/2.png";
        } else if (c.getHealth() >= 10) {
            return "img/Health/1.png";
        } else {
            return "img/Health/0.png";
        }
    }

    public void run() {
        Graphics g = getBufferStrategy().getDrawGraphics();
        // Limpa a tela
        g.clearRect(0, 0, getWidth(), getHeight());

        // Grid de 1 por 1, pois só tem um elemento contido no JFrame atual
        setLayout(new GridLayout(1, 1));

        // Background
        game.Image backgroundImage = new game.Image("img/Background.png", 0, 0);

        // Array onde os heróis são instanciados
        heroes = new ArrayList<>();
        heroes.add(new Bruno(100, 6, 15, 50, "Bruno",
                "img/Bruno.gif", 50, 200, "sound/hit.wav"));
        // Vilão
        heroes.add(new Faria(100, 10, 20, 20, "Faria",
                "img/Faria.gif", 1000, 50, "sound/hit.wav"));
        heroes.add(new Leticia(100, 20, 6, 15, "Leticia",
                "img/Leticia.gif", 50, 200, "sound/hit.wav"));
        heroes.add(new Valentina(100, 15, 10, 10, "Valentina",
                "img/Valentina.gif", 50, 200, "sound/hit.wav"));

        // Painel dos botões
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new GridLayout(2, 2, 10, 10));
        buttonsPanel.setBackground(new Color(0, 0, 0, 0));

        // Botões
        ArrayList<JButton> buttons = new ArrayList<>();
        buttons.add(new JButton("Attack"));
        buttons.add(new JButton("Defend"));
        buttons.add(new JButton("Special"));
        buttons.add(new JButton("Swap"));

        // Todos os botões têm seus tamanhos definidos e são adicionados ao painel de botões
        for (JButton button : buttons) {
            button.setPreferredSize(new Dimension(100, 50)); // Definir o tamanho dos botões
            buttonsPanel.add(button);
            button.setFont(retroFont);
        }

        // Onde o status do round será exibido
        JLabel texts = new JLabel();
        texts.setText("BATTLE STARTED!");
        texts.setHorizontalAlignment(SwingConstants.CENTER);
        texts.setFont(retroFont);

        // Nova divisão horizontal que conterá os botões à direita e os textos à esquerda
        JSplitPane bottomDivision = new JSplitPane();
        bottomDivision.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        bottomDivision.setDividerLocation(550);
        bottomDivision.setLeftComponent(texts);
        bottomDivision.setRightComponent(buttonsPanel);

        // Ação de cada botão


        // Botão de ataque
        buttons.get(0).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Executa o ataque do herói atual (currentHero) contra o vilão (heroes.get(1))
                int attack = heroes.get(currentHero).attack(heroes.get(1));
                // Atualiza o texto para mostrar o ataque realizado e o dano causado
                texts.setText(heroes.get(currentHero).getName() + " attacked " + heroes.get(1).getName() + ", dealing " + attack + " damage!");
            }
        });

        // Botão de defesa
        buttons.get(1).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Executa a defesa do herói atual (currentHero)
                heroes.get(currentHero).defend();
                // Atualiza o texto para mostrar que o herói atual defendeu contra o vilão
                texts.setText(heroes.get(currentHero).getName() + " defended herself from " + heroes.get(1).getName());
            }
        });

        // Botão de poder especial
        buttons.get(2).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Obtém o herói atual
                Character currentCharacter = heroes.get(currentHero);

                // Verifica se o herói atual é uma instância de Valentina
                if (currentCharacter instanceof Valentina) {
                    // Cria um PopUp para selecionar um personagem para reviver
                    PopUp popUp = new PopUp((Frame) SwingUtilities.getWindowAncestor(MainFrame.this), heroes, currentHero, true);
                    popUp.setVisible(true); // Exibe o diálogo modal

                    // Obtém o personagem selecionado no PopUp
                    Character selectedCharacter = popUp.getSelectedCharacter();

                    // Se um personagem foi selecionado e está com a saúde menor ou igual a 0
                    if (selectedCharacter != null && selectedCharacter.getHealth() <= 0) {
                        // Valentina usa seu poder especial para reviver o personagem
                        currentCharacter.specialPower(selectedCharacter);
                        // Atualiza o texto para mostrar que Valentina reviveu o personagem
                        texts.setText(currentCharacter.getName() + " revived " + selectedCharacter.getName() + "!");
                        // Define a saúde do personagem revivido para 100
                        selectedCharacter.setHealth(100);

                        // Exibe o GIF especial no JLabel e define sua posição
                        specialGifLabel.setIcon(new ImageIcon(getClass().getResource("img/Valentina_Poder.gif")));
                        specialGifLabel.setBounds(50, 200, 400, 300); // Ajuste os valores de largura e altura conforme necessário
                        specialGifLabel.setVisible(true);

                        // Remove o GIF especial após um tempo (3 segundos, por exemplo)
                        Timer timer = new Timer(3000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                specialGifLabel.setVisible(false);

                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                } else {
                    // Se não for Valentina, usa o poder especial contra o vilão
                    currentCharacter.specialPower(heroes.get(1)); // Assume que a primeira posição é o vilão
                    // Atualiza o texto para mostrar que o herói usou seu ataque especial no vilão
                    texts.setText(currentCharacter.getName() + " used special attack on " + heroes.get(1).getName() + "!");

                    // Exibe o GIF especial no JLabel e define sua posição com base no herói atual
                    if (currentCharacter instanceof Bruno) {
                        specialGifLabel.setIcon(new ImageIcon(getClass().getResource("img/Bruno_Poder.gif")));
                    } else if (currentCharacter instanceof Leticia) {
                        specialGifLabel.setIcon(new ImageIcon(getClass().getResource("img/Leticia_Poder.gif")));
                    } else if (currentCharacter instanceof Faria) {
                        specialGifLabel.setIcon(new ImageIcon(getClass().getResource("img/Faria_Poder.gif")));
                    }
                    specialGifLabel.setBounds(50, 200, 400, 300); // Ajuste os valores de largura e altura conforme necessário
                    specialGifLabel.setVisible(true);

                    // Remove o GIF especial após um tempo (3 segundos)
                    // Cria um novo timer que executará uma ação após 3000 milissegundos (3 segundos)
                    Timer timer = new Timer(3000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Define o label do GIF especial como invisível quando o timer dispara
                            specialGifLabel.setVisible(false);
                        }
                    });

                    // Define que o timer não deve repetir a ação; ele só será executado uma vez
                    timer.setRepeats(false);

                    // Inicia o timer. Após 3 segundos, a ação definida acima será executada
                    timer.start();

                }
            }
        });

        // Botão de troca
        buttons.get(3).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Cria um PopUp para selecionar um personagem para troca
                PopUp popUp = new PopUp((Frame) SwingUtilities.getWindowAncestor(MainFrame.this), heroes, currentHero, false);
                popUp.setVisible(true); // Exibe o diálogo modal
                // Obtém o personagem selecionado no PopUp
                Character selectedCharacter = popUp.getSelectedCharacter();
                // Se um personagem foi selecionado
                if (selectedCharacter != null) {
                    // Atualiza o índice do herói atual para o personagem selecionado
                    currentHero = heroes.indexOf(selectedCharacter);
                    // Atualiza as imagens e outras representações gráficas conforme necessário
                    repaint(); // Redesenha a tela após a troca de personagem
                    // Atualiza o texto para mostrar que o herói atual foi trocado
                    texts.setText("Swapped to " + selectedCharacter.getName() + "!");
                }
            }
        });

        // Instancia as barras de vida iniciais (começando com 10 de vida)
        Image heroHealth = new Image("img/Health/10.png", 50, 170);
        Image enemyHealth = new Image("img/Health/10.png", 1000, 10);

        // Cria o JPane em que as imagens (jogador, inimigo e plano de fundo) são desenhadas
        JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                backgroundImage.draw(g);
                heroes.get(currentHero).draw(g);
                heroes.get(1).draw(g);
                heroHealth.draw(g);
                enemyHealth.draw(g);
            }
        };
        pane.setLayout(null); // Usamos layout nulo para posicionar componentes manualmente

        // Cria um JLabel para exibir o GIF especial
        specialGifLabel = new JLabel();
        specialGifLabel.setVisible(false); // Inicialmente invisível
        pane.add(specialGifLabel); // Adiciona o JLabel ao pane

        // O JSplitPane cria um Pane a partir de dois -> Armazena o JPane dos botões e o JPane das imagens
        JSplitPane division = new JSplitPane();
        // A orientação do JSplitPane vai ser vertical
        division.setOrientation(JSplitPane.VERTICAL_SPLIT);
        // Seta onde (em relação à vertical) o divisor vai estar
        division.setDividerLocation(500);
        // O componente do topo da divisão são as imagens
        division.setTopComponent(pane);
        // O componente de baixo vai ser o painel de botões
        division.setBottomComponent(bottomDivision);


        // Adiciona a divisão ao JFrame atual
        add(division);
        // Abre em tela cheia
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Abre a tela
        setVisible(true);


        // Entra num while infinito que vai atualizar as imagens a cada 200ms através da função "updateUI() de division"
        // Deixa os gifs animados
        // A lógica de progressão do jogo deve ser implementada aqui - Provavelmente
        while (true) {
            heroHealth.setImg(new ImageIcon(Objects.requireNonNull
                    (this.getClass().getResource(hpBar(heroes.get(currentHero))))));
            enemyHealth.setImg(new ImageIcon(Objects.requireNonNull
                    (this.getClass().getResource(hpBar(heroes.get(1))))));

            if (specialGifLabel.isVisible()) {
                heroes.get(currentHero).setImg(new ImageIcon(Objects.requireNonNull(
                        getClass().getResource("img/" + heroes.get(currentHero).getName() + "_Poder.gif"))));
            } else {
                heroes.get(currentHero).setImg(new ImageIcon(Objects.requireNonNull(
                        getClass().getResource("img/" + heroes.get(currentHero).getName() + ".gif"))));
            }

            millis1 = clock.millis();

            if ((millis1 - millis2) > 200) {
                division.updateUI();
                millis2 = millis1;
            }

            // Unidade de tempo da animação
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
