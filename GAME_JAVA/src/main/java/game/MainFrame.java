package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MainFrame extends JFrame implements Runnable {
    private boolean isPaused = false, action = false;
    private PauseOverlay pauseOverlay;
    private int currentHero = 0, levels = 0;
    private Clock clock = Clock.systemDefaultZone();
    private long millis1 = clock.millis(), millis2 = millis1, millis3 = millis1;
    private Font retroFont;
    private Random r = new Random();

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

        //Seta a fonte do texto
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

    //Carrega a fonte do texto
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    private String hpBar(Character c) {
        if(c.getHealth() >= 100) {
            return "img/Health/10.png";
        } else if(c.getHealth() >= 90) {
            return "img/Health/9.png";
        } else if(c.getHealth() >= 80) {
            return "img/Health/8.png";
        } else if(c.getHealth() >= 70) {
            return "img/Health/7.png";
        } else if(c.getHealth() >= 60) {
            return "img/Health/6.png";
        } else if(c.getHealth() >= 50) {
            return "img/Health/5.png";
        } else if(c.getHealth() >= 40) {
            return "img/Health/4.png";
        } else if(c.getHealth() >= 30) {
            return "img/Health/3.png";
        } else if(c.getHealth() >= 20) {
            return "img/Health/2.png";
        } else if(c.getHealth() >= 10 || c.getHealth() > 0) {
            return "img/Health/1.png";
        } else {
            return "img/Health/0.png";
        }
    }

    private String defenseBar(Character c) {
        if(c.getDefense() >= 15) {
            return "img/Defense/2.png";
        } else if(c.getDefense() >= 10 && c.getDefense() > 0) {
            return "img/Defense/2-1.png";
        } else {
            return "img/Defense/2-2.png";
        }
    }

    public void run() {
        Graphics g = getBufferStrategy().getDrawGraphics();
        //Limpa a tela
        g.clearRect(0, 0, getWidth(), getHeight());

        //Grid de 1 por 1, pois só tem um elemento contido no JFrame atual
        setLayout(new GridLayout(1, 1));

        //Background
        game.Image backgroundImage = new game.Image("img/Background1.png", 0, 0);

        //Array onde os heróis são instanciados
        ArrayList<game.Character> heroes = new ArrayList<>();
        heroes.add(new Bruno(100, 6, 5, 50, "Bruno",
                "img/Bruno.gif", 50, 200, "sound/hit.wav"));
        //Vilão
        heroes.add(new Faria(100, 10, 0, 20, "Faria",
                "img/Faria.gif", 1000, 50, "sound/hit.wav"));
        heroes.add(new Leticia(100, 20, 6, 15, "Leticia",
                "img/Leticia.gif", 50, 200, "sound/hit.wav"));
        heroes.add(new Valentina(100, 15, 10, 10, "Valentina",
                "img/Valentina.gif", 50, 200, "sound/hit.wav"));

        //Painel dos botões
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new GridLayout(2, 2, 10, 10));
        buttonsPanel.setBackground(new Color(0, 0, 0, 0));

        //Botões
        ArrayList<JButton> buttons = new ArrayList<>();
        buttons.add(new JButton("Attack"));
        buttons.add(new JButton("Defend"));
        buttons.add(new JButton("Special"));
        buttons.add(new JButton("Swap"));

        //Todos os botões têm seus tamanhos definidos e são adicionados o painel de botões
        for (JButton button : buttons) {
            button.setPreferredSize(new Dimension(100, 50)); // Definir o tamanho dos botões
            buttonsPanel.add(button);
            button.setFont(retroFont);
        }

        //Onde o status do round será exibido
        JLabel texts = new JLabel();
        texts.setText("BATTLE STARTED!");
        texts.setHorizontalAlignment(SwingConstants.CENTER);
        texts.setFont(retroFont);

        //Nova divisão horizontal que conterá os botões à direita e os textos à esquerda
        JSplitPane bottomDivision = new JSplitPane();
        bottomDivision.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        bottomDivision.setDividerLocation(550);
        bottomDivision.setLeftComponent(texts);
        bottomDivision.setRightComponent(buttonsPanel);

        //Ação de cada botão - A IMPLEMENTAR!!
        buttons.getFirst().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!action) {
                    String attack = heroes.get(currentHero).attack(heroes.get(1));
                    texts.setText(attack);
                    action = true;
                    millis1 = clock.millis();
                    millis3 = millis1;
                }
            }
        });
        buttons.get(3).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!action) {
                    currentHero++;
                    System.out.println(currentHero);
                    action = true;
                }
            }
        });

        //Instancia as barras de vida iniciais (começando com 10 de vida)
        Image heroHealth = new Image("img/Health/10.png", 50, 170);
        Image enemyHealth = new Image("img/Health/10.png", 1000, 10);

        //Instancia as barras de defesa iniciais (começando invisíveis)
        Image heroDefense = new Image("img/Defense/2-2.png", 73, 160);
        Image enemyDefense = new Image("img/Defense/2-2.png", 1023, 0);

        //Cria o JPane em que as imagens (jogador, inimigo e plano de fundo) são desenhadas
        JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                backgroundImage.draw(g);
                heroes.get(currentHero).draw(g);
                heroes.get(1).draw(g);
                heroHealth.draw(g);
                enemyHealth.draw(g);
                heroDefense.draw(g);
                enemyDefense.draw(g);
            }
        };

        //O JSplitPane cria um Pane a partir de dois -> Armazena o JPane dos botões e o JPane das imagens
        JSplitPane division = new JSplitPane();
        //A orientação do JSplitPane vai ser vertical
        division.setOrientation(JSplitPane.VERTICAL_SPLIT);
        //Seta onde (em relação à vertical) o divisor vai estar
        division.setDividerLocation(500);
        //O componente do topo da divisão são as imagens
        division.setTopComponent(pane);
        //O componente de baixo vai ser o painel de botões
        division.setBottomComponent(bottomDivision);

        //Adiciona a divisão ao JFrame atual
        add(division);

        //Painel que contém a tela de vitória
        Image victory = new Image("img/Tela de Vitória.gif", 0, 0);
        JPanel victoryPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                victory.draw(g);
            }
        };
        victoryPane.setVisible(true);

        //Painel que contém a tela de derrota
        Image defeat = new Image("img/Tela de Derrota.gif", 0, 0);
        JPanel defeatPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                defeat.draw(g);
            }
        };
        defeatPane.setVisible(true);

        //Painel que contém o gif de transição
        Image transition = new Image("img/Transition.gif", 0, 0);
        JPanel transitionPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                transition.draw(g);
            }
        };
        transitionPane.setVisible(true);

        //Abre em tela cheia
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Abre a tela
        setVisible(true);

        //Entra num while infinito que vai atualizar as imagens a cada 200ms através da função "updateUI() de division"
        //Deixa os gifs animados
        //A lógica de progressão do jogo deve ser implementada aqui - Provavelmente
        while (true) {
            //Atualiza as imagens das barras de vida e do herói atual
            heroHealth.setImg(new ImageIcon(Objects.requireNonNull
                    (this.getClass().getResource(hpBar(heroes.get(currentHero))))));
            enemyHealth.setImg(new ImageIcon(Objects.requireNonNull
                    (this.getClass().getResource(hpBar(heroes.get(1))))));
            heroDefense.setImg(new ImageIcon(Objects.requireNonNull
                    (this.getClass().getResource(defenseBar(heroes.get(currentHero))))));
            enemyDefense.setImg(new ImageIcon(Objects.requireNonNull
                    (this.getClass().getResource(defenseBar(heroes.get(1))))));
            heroes.get(currentHero).setImg(new ImageIcon(Objects.requireNonNull
                    (this.getClass().getResource("img/" + heroes.get(currentHero).getName() + ".gif"))));

            //A cada 200ms, atualiza as animações
            millis1 = clock.millis();
            if ((millis1 - millis2) > 200) {
                division.updateUI();
                victoryPane.updateUI();
                millis2 = millis1;
            }

            //Caso uma ação aconteça (botão seja pressionado), passa o round pro inimigo
            if(action && (millis1 - millis3) > 2000) {
                if(heroes.get(1).getPowerCharge() >= 100 && heroes.get(1).getImprisioned() == 0)
                    heroes.get(1).specialPower(heroes.get(currentHero));
                else {
                    int decision = r.nextInt(0,4);
                    switch(decision) {
                        case 3:
                            String defense = heroes.get(1).defend();
                            texts.setText(defense);
                            break;

                        default:
                            String attack = heroes.get(1).attack(heroes.get(currentHero));
                            texts.setText(attack);
                    }
                }

                action = false;
            }

            //Se zerar a vida do inimigo, reinicia o nível
            if(heroes.get(1).getHealth() <= 0) {
                remove(division);
                add(transitionPane);

                //Transição de nível, que dura 1500ms (300 iterações * 5ms)
                for(int i = 0; i < 300; i++) {
                    transitionPane.updateUI();
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                remove(transitionPane);
                add(division);

                //Muda o background de acordo com o nível atual
                switch (levels) {
                    case 0:
                        backgroundImage.setImg(new ImageIcon(Objects.requireNonNull(this.getClass().
                                getResource("img/Background2.png"))));
                        break;

                    case 1:
                        backgroundImage.setImg(new ImageIcon(Objects.requireNonNull(this.getClass().
                                getResource("img/Background3.png"))));
                        break;

                    case 2:
                        backgroundImage.setImg(new ImageIcon(Objects.requireNonNull(this.getClass().
                                getResource("img/BackgroundBoss.png"))));
                        break;

                    case 3:
                        remove(division);
                        add(victoryPane);
                        break;

                    default:
                        break;
                }

                levels++;
                heroes.get(1).setHealth(100);
                texts.setText("Level passed!");
            }

            //Caso todos os heróis tenham morrido, aparece a tela de derrota
            int deads = 0;
            for(Character hero : heroes){
                if(hero.getHealth() < 0){
                    deads++;
                }
            }
            if(deads == 4){
                remove(division);
                add(defeatPane);
            }

            // Unidade de tempo da animação
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
            }
        }
    }
}