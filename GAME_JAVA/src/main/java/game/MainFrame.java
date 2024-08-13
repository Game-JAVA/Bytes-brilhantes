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
    private final PauseOverlay pauseOverlay;
    private int currentHero = 0, levels = -2, currentEnemy = 0;
    private final Clock clock = Clock.systemDefaultZone();
    private long millis1 = clock.millis(), millis2 = millis1;
    private Font retroFont;
    private Random r = new Random();
    private JLabel specialGifLabel; // Novo JLabel para exibir o GIF especial
    private final Sound click = new Sound("sound/buttonClick.wav", false),
            start = new Sound("sound/start.wav", false);
    private final Image damagePointsDec = new Image("img/Attack/null.png", 300, 170),
            damagePointsUnit = new Image("img/Attack/null.png", 350, 170),
            defendPointsDec = new Image("img/Defend/null.png", 900, 10),
            defendPointsUnit = new Image("img/Defend/null.png", 950, 10),
            specialAttackHero = new Image("img/null.png", 1030, 170),
            specialDefendHero = new Image("img/null.png", 120,280),
            specialAttackEnemy = new Image("img/null.png",120,280),
            specialDefendEnemy = new Image("img/null.png", 1040, 130);
    public MainFrame() {
        pauseOverlay = new PauseOverlay();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }
        });
        setFocusable(true);
        requestFocus();

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

    private void requestFocusOnFrame() {
        SwingUtilities.invokeLater(() -> {
            requestFocus();
            pauseOverlay.requestFocus();
        });
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
        if(c.getDefense() >= 10) {
            return "img/Defense/2.png";
        } else if(c.getDefense() >= 5 && c.getDefense() > 0) {
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
        heroes.add(new Bruno(100, 7, 3, 50, "Bruno",
                "img/Bruno.gif", 50, 200, "sound/bruno_special.wav"));
        heroes.add(new Faria(100, 7, 5, 40, "Faria",
                "img/Faria.gif", 50, 200, "sound/faria_special.wav"));
        heroes.add(new Leticia(100, 20, -2, 40, "Leticia",
                "img/Leticia.gif", 50, 200, "sound/leticia_special.wav"));
        heroes.add(new Valentina(100, 10, 0, 20, "Valentina",
                "img/Valentina.gif", 50, 200, "sound/valentina_special.wav"));

        //Array onde os vilões são instanciados
        ArrayList<game.Character> enemies = new ArrayList<>();
        enemies.add(new Boss(100, 14, 0, 0, "Banished Knight",
                "img/Inimigo1.gif", 1000, 50, "sound/hit.wav"));
        enemies.add(new Boss(100, 16, 0, 0, "Dragon Lord",
                "img/Inimigo2.gif", 1000, 50, "sound/hit.wav"));
        enemies.add(new Boss(100, 18, 0, 0, "Count Vamp",
                "img/Inimigo3.gif", 1000, 50, "sound/hit.wav"));
        enemies.add(new Boss(100 , 20, 0, 100, "Lord of Death",
                "img/Chefe.gif", 975, 100, "sound/boss_special.wav"));


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
        bottomDivision.setDividerLocation(670);
        bottomDivision.setLeftComponent(texts);
        bottomDivision.setRightComponent(buttonsPanel);

        // Ação de cada botão
        // Botão de ataque
        buttons.get(0).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!action) {
                    millis2 = clock.millis();
                    cleanNumbers();

                    // Executa o ataque do herói atual (currentHero) contra o vilão (heroes.get(1))
                    int damage = heroes.get(currentHero).attack(enemies.get(currentEnemy));
                    // Atualiza o texto para mostrar o ataque realizado e o dano causado
                    texts.setText(heroes.get(currentHero).getName() + " attacked " +
                            enemies.get(currentEnemy).getName() + ", dealing " + damage + " damage!");

                    //Atualiza a imagem ao atacar o inimigo
                    specialAttackHero.setImg(new ImageIcon(Objects.requireNonNull
                            (this.getClass().getResource("img/attack.png"))));

                    int unit = damage % 10;
                    int decimal = damage / 10;

                    damagePointsDec.setPosition_x(970);
                    damagePointsDec.setPosition_y(100);
                    damagePointsUnit.setPosition_x(990);
                    damagePointsUnit.setPosition_y(100);

                    //Atualiza as imagens de dano
                    damagePointsUnit.setImg(new ImageIcon(Objects.requireNonNull
                            (this.getClass().getResource("img/Attack/" + unit + ".png"))));
                    damagePointsDec.setImg(new ImageIcon(Objects.requireNonNull
                            (this.getClass().getResource("img/Attack/" + decimal + ".png"))));

                    action = true;
                }
                requestFocusOnFrame();
            }
        });

        // Botão de defesa
        buttons.get(1).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!action) {
                    millis2 = clock.millis();
                    cleanNumbers();

                    // Executa a defesa do herói atual (currentHero)
                    int defense = heroes.get(currentHero).defend();
                    // Atualiza o texto para mostrar que o herói atual defendeu contra o vilão
                    texts.setText(heroes.get(currentHero).getName() + " defended itself by " + defense + " points!");

                    specialDefendHero.setImg(new ImageIcon(Objects.requireNonNull
                            (this.getClass().getResource("img/defend.png"))));

                    int unit = defense % 10;
                    int decimal = defense / 10;

                    defendPointsDec.setPosition_x(120);
                    defendPointsDec.setPosition_y(160);
                    defendPointsUnit.setPosition_x(140);
                    defendPointsUnit.setPosition_y(160);

                    //Atualiza as imagens de defesa
                    defendPointsUnit.setImg(new ImageIcon(Objects.requireNonNull
                            (this.getClass().getResource("img/Defend/" + unit + ".png"))));
                    defendPointsDec.setImg(new ImageIcon(Objects.requireNonNull
                            (this.getClass().getResource("img/Defend/" + decimal + ".png"))));

                    action = true;
                }
                requestFocusOnFrame();
            }
        });

        // Botão de poder especial
        buttons.get(2).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Obtém o herói atual
                Character currentCharacter = heroes.get(currentHero);
                if(currentCharacter.getPowerCharge() >= 100 && !action){
                    // Verifica se o herói atual é uma instância de Valentina
                    if (currentCharacter instanceof Valentina) {
                        cleanNumbers();

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
                            //Reseta o poder especial do personagem revivido
                            selectedCharacter.setPowerCharge(0);

                            // Exibe o GIF especial no JLabel e define sua posição
                            specialGifLabel.setVisible(true);

                            // Remove o GIF especial após um tempo (3 segundos, por exemplo)
                            Timer timer = new Timer(2000, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    specialGifLabel.setVisible(false);

                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                            action = true;
                            millis2 = clock.millis();
                        } else {
                            texts.setText("There is no dead allies!");
                        }
                    } else {
                        cleanNumbers();

                        // Se não for Valentina, usa o poder especial contra o vilão
                        currentCharacter.specialPower(enemies.get(currentEnemy));
                        // Atualiza o texto para mostrar que o herói usou seu ataque especial no vilão
                        texts.setText(currentCharacter.getName() + " used special attack on " +
                                enemies.get(currentEnemy).getName() + "!");

                        specialGifLabel.setVisible(true);

                        // Remove o GIF especial após um tempo (3 segundos)
                        // Cria um novo timer que executará uma ação após 3000 milissegundos (3 segundos)
                        Timer timer = new Timer(2000, new ActionListener() {
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

                        action = true;
                        millis2 = clock.millis();
                    }
                } else if(!action) {
                    texts.setText("Power charge is at " + currentCharacter.getPowerCharge() + "%!");
                }
                requestFocusOnFrame();
            }
        });

        // Botão de troca
        buttons.get(3).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!action) {
                    cleanNumbers();

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

                        action = true;
                        millis2 = clock.millis();
                    }
                    click.play();
                }
                requestFocusOnFrame();
            }
        });

        //Instancia as barras de vida iniciais (começando com 10 de vida)
        Image heroHealth = new Image("img/Health/10.png", 50, 170);
        Image enemyHealth = new Image("img/Health/10.png", 1000, 10);

        //Instancia as barras de defesa iniciais (começando invisíveis)
        Image heroDefense = new Image("img/Defense/2-2.png", 73, 160);
        Image enemyDefense = new Image("img/Defense/2-2.png", 1023, 0);

        //Instancia o HP do herói e do inimigo (em números)
        Image heroHPHund = new Image("img/HP/1.png", 245, 160);
        Image heroHPDec = new Image("img/HP/0.png", 260, 160);
        Image heroHPUnit = new Image("img/HP/0.png", 280, 160);

        Image enemyHPHund = new Image("img/HP/1.png", 1195, 0);
        Image enemyHPDec = new Image("img/HP/0.png", 1210, 0);
        Image enemyHPUnit = new Image("img/HP/0.png", 1230, 0);


        //Cria o JPane em que as imagens (jogador, inimigo e plano de fundo) são desenhadas
        JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                backgroundImage.draw(g);

                heroes.get(currentHero).draw(g);
                heroHealth.draw(g);
                heroDefense.draw(g);

                enemies.get(currentEnemy).draw(g);
                enemyHealth.draw(g);
                enemyDefense.draw(g);

                damagePointsDec.draw(g);
                damagePointsUnit.draw(g);
                defendPointsDec.draw(g);
                defendPointsUnit.draw(g);

                heroHPUnit.draw(g);
                heroHPHund.draw(g);
                heroHPDec.draw(g);

                enemyHPUnit.draw(g);
                enemyHPHund.draw(g);
                enemyHPDec.draw(g);

                specialAttackHero.draw(g);
                specialDefendHero.draw(g);
                specialAttackEnemy.draw(g);
                specialDefendEnemy.draw(g);

            }
        };

        // Cria um JLabel para exibir o GIF especial
        specialGifLabel = new JLabel();
        specialGifLabel.setVisible(false); // Inicialmente invisível
        pane.add(specialGifLabel); // Adiciona o JLabel ao pane
        pane.setLayout(null); // Usamos layout nulo para posicionar componentes manualmente

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

        //Painel que contém o gif de transição
        Image transition = new Image("img/Transition.gif", 0, 0);
        JPanel transitionPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                transition.draw(g);
            }
        };
        transitionPane.setVisible(true);

        //Painel que contém as instruções do jogo
        Image instructions = new Image("img/Instructions.gif", 0, 0);
        JPanel instructionsPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                instructions.draw(g);
            }
        };
        instructionsPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if(x >= 1090 && x <= 1220 && y >= 570 && y <= 630) {
                    remove(instructionsPane);
                    add(transitionPane);

                    //Reseta os atributos dos inimigos para o padrão
                    for(Character enemy : enemies) {
                        enemy.setHealth(100);
                        enemy.setImprisioned(0);
                        enemy.setDefense(0);
                    }

                    //Mesmo para os heróis
                    for(Character hero : heroes) {
                        hero.setHealth(100);
                        hero.setPowerCharge(0);
                        hero.setDefense(0);
                    }

                    //Reseta o level e o array de inimigos e heróis
                    levels = -1;
                    currentEnemy = 0;
                    currentHero = 0;

                    start.play();

                    //Fecha e abre a tela para atualizar
                    setVisible(false);
                    setVisible(true);
                }
            }
        });
        instructionsPane.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if(x >= 1090 && x <= 1220 && y >= 570 && y <= 630){
                    instructionsPane.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                } else {
                    instructionsPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        instructionsPane.setVisible(true);

        //Painel que contém o menu
        Image menu = new Image("img/MenuStart2.gif", 0, 0);
        JPanel menuPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                menu.draw(g);
            }
        };
        //Detecta um clique no mouse no range entre X e Y do if
        menuPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if(y >= 530 && y <= 600) {
                    //Caso clique no botão de start, troca para a tela de instruções
                    if(x >= 470 && x <= 640){
                        start.play();
                        remove(menuPane);
                        add(instructionsPane);

                        //Fecha e abre a tela para atualizar
                        setVisible(false);
                        setVisible(true);
                    } else if(x >= 660 && x <= 830) { //Se clicar no botão de sair, fecha a tela e encerra o programa
                        click.play();
                        setVisible(false);
                        System.exit(0);
                    }
                }

            }
        });
        //Hover do mouse ao passar em cima dos botões
        menuPane.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if(((x >= 470 && x <= 640) || (x >= 660 && x <= 830)) && (y >= 530 && y <= 600)){
                    menuPane.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                } else {
                    menuPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        menuPane.setVisible(true);
        //Adiciona a tela de menu ao JFrame atual (tela atual)
        add(menuPane);

        //Painel que contém a tela de derrota
        Image defeat = new Image("img/Tela de Derrota.gif", 0, 0);
        JPanel defeatPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                defeat.draw(g);
            }
        };
        defeatPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if(x >= 590 && x <= 710 && y >= 500 && y <= 550) {
                    click.play();
                    levels = -2;
                    remove(defeatPane);
                    add(menuPane);

                    //Fecha e abre a tela para atualizar
                    setVisible(false);
                    setVisible(true);
                }
            }
        });
        defeatPane.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if(x >= 590 && x <= 710 && y >= 500 && y <= 550) {
                    defeatPane.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                } else {
                    defeatPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        defeatPane.setVisible(true);

        //Painel que contém a tela de vitória
        Image victory = new Image("img/Tela de Vitória.gif", 0, 0);
        JPanel victoryPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                victory.draw(g);
            }
        };
        victoryPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if(x >= 510 && x <= 640 && y >= 510 && y <= 560) {
                    click.play();
                    levels = -2;
                    remove(victoryPane);
                    add(menuPane);

                    //Fecha e abre a tela para atualizar
                    setVisible(false);
                    setVisible(true);
                } else if(x >= 660 && x <= 790 && y >= 510 && y <= 560) {
                    setVisible(false);
                    System.exit(0);
                }
            }
        });
        victoryPane.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if((x >= 510 && x <= 640 && y >= 510 && y <= 560) || (x >= 660 && x <= 790 && y >= 510 && y <= 560)) {
                    victoryPane.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                } else {
                    victoryPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        victoryPane.setVisible(true);

        //Instancia as músicas do jogo
        Sound defeatSound = new Sound("sound/defeat.wav", false),
                victorySound = new Sound("sound/victory.wav", false),
                menuSong = new Sound("sound/menu.wav", true),
                level = new Sound("sound/level.wav", true),
                boss = new Sound("sound/boss.wav", true);

        //Abre em tela cheia
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Abre a tela
        setVisible(true);

        //Entra num while infinito que vai atualizar as imagens a cada 200ms através da função "updateUI() de division"
        //Deixa os gifs animados
        //A lógica de progressão do jogo deve ser implementada aqui
        while (true) {
            //Animação do menu
            if(levels == -2) {
                menuSong.play();

                for(int i = 0; i < 720; i++) {
                    menuPane.repaint();
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if(!isAncestorOf(menuPane))
                        break;
                }

                menu.setImg(new ImageIcon(Objects.requireNonNull
                        (this.getClass().getResource("img/MenuBlink2.gif"))));
                levels++;
            }

            //Atualiza as imagens das barras de vida e defesa do herói e inimigo atual
            heroHealth.setImg(new ImageIcon(Objects.requireNonNull
                    (this.getClass().getResource(hpBar(heroes.get(currentHero))))));
            enemyHealth.setImg(new ImageIcon(Objects.requireNonNull
                    (this.getClass().getResource(hpBar(enemies.get(currentEnemy))))));
            heroDefense.setImg(new ImageIcon(Objects.requireNonNull
                    (this.getClass().getResource(defenseBar(heroes.get(currentHero))))));
            enemyDefense.setImg(new ImageIcon(Objects.requireNonNull
                    (this.getClass().getResource(defenseBar(enemies.get(currentEnemy))))));

            //Atualiza o HP do herói
            heroHPUnit.setImg(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("img/HP/" +
                    heroes.get(currentHero).getHealth() % 10 + ".png"))));
            heroHPDec.setImg(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("img/HP/" +
                    ((heroes.get(currentHero).getHealth() % 100) / 10) + ".png"))));
            if(heroes.get(currentHero).getHealth() < 100)
                heroHPHund.setImg(new ImageIcon(Objects.requireNonNull
                        (this.getClass().getResource("img/HP/null.png"))));
            else
                heroHPHund.setImg(new ImageIcon(Objects.requireNonNull
                        (this.getClass().getResource("img/HP/1.png"))));

            //Atualiza o HP do inimigo
            enemyHPUnit.setImg(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("img/HP/" +
                    enemies.get(currentEnemy).getHealth() % 10 + ".png"))));
            enemyHPDec.setImg(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("img/HP/" +
                    ((enemies.get(currentEnemy).getHealth() % 100) / 10) + ".png"))));
            if(enemies.get(currentEnemy).getHealth() < 100)
                enemyHPHund.setImg(new ImageIcon(Objects.requireNonNull
                        (this.getClass().getResource("img/HP/null.png"))));
            else
                enemyHPHund.setImg(new ImageIcon(Objects.requireNonNull
                        (this.getClass().getResource("img/HP/1.png"))));

            //Atualiza o gif de quando o herói utiliza o poder especial
            if (specialGifLabel.isVisible()) {
                heroes.get(currentHero).setImg(new ImageIcon(Objects.requireNonNull(
                        getClass().getResource("img/" + heroes.get(currentHero).getName() + "_Poder.gif"))));
            } else {
                heroes.get(currentHero).setImg(new ImageIcon(Objects.requireNonNull(
                        getClass().getResource("img/" + heroes.get(currentHero).getName() + ".gif"))));
            }

            millis1 = clock.millis();
            //Caso uma ação aconteça (botão seja pressionado), passa o round pro inimigo
            //Se o boss estiver com o poder especial completo, usa o poder especial
            //Se não, tem 75% de chance de atacar e 25% de defender
            if(action && (millis1 - millis2) > 2000) {
                cleanNumbers();

                if (enemies.get(currentEnemy).getPowerCharge() >= 100 && enemies.get(currentEnemy).getImprisioned() == 0) {
                    enemies.get(currentEnemy).specialPower(heroes.get(currentHero));
                    texts.setText(enemies.get(currentEnemy).getName() + " stole " + heroes.get(currentHero).getName()
                            + "'s own blood!");
                } else {
                    int decision = r.nextInt(0, 4);
                    if (decision == 3) {
                        int defense = enemies.get(currentEnemy).defend();
                        if (defense != -1) {
                            texts.setText(enemies.get(currentEnemy).getName() + " defended itself by " + defense +
                                    " points!");

                            //Mostra a imagem ao se defender
                            specialDefendEnemy.setImg(new ImageIcon(Objects.requireNonNull
                                    (this.getClass().getResource("img/defend.png"))));

                            int unit = defense % 10;
                            int decimal = defense / 10;

                            defendPointsDec.setPosition_x(1070);
                            defendPointsDec.setPosition_y(0);
                            defendPointsUnit.setPosition_x(1090);
                            defendPointsUnit.setPosition_y(0);

                            //Atualiza as imagens de defesa
                            defendPointsUnit.setImg(new ImageIcon(Objects.requireNonNull
                                    (this.getClass().getResource("img/Defend/" + unit + ".png"))));
                            defendPointsDec.setImg(new ImageIcon(Objects.requireNonNull
                                    (this.getClass().getResource("img/Defend/" + decimal + ".png"))));

                        }
                        else {
                            texts.setText(enemies.get(currentEnemy).getName() + " is imprisioned!");
                        }
                    } else {
                        int damage = enemies.get(currentEnemy).attack(heroes.get(currentHero));
                        if (damage != -1) {
                            texts.setText(enemies.get(currentEnemy).getName() + " attacked " +
                                    heroes.get(currentHero).getName() + ", dealing " + damage + " damage!");

                            //Mostra a imagem ao atacar o Heroi
                            specialAttackEnemy.setImg(new ImageIcon(Objects.requireNonNull
                                    (this.getClass().getResource("img/attack.png"))));

                            int unit = damage % 10;
                            int decimal = damage / 10;

                            defendPointsDec.setPosition_x(300);
                            defendPointsDec.setPosition_y(240);
                            defendPointsUnit.setPosition_x(320);
                            defendPointsUnit.setPosition_y(240);

                            //Atualiza as imagens de defesa
                            defendPointsUnit.setImg(new ImageIcon(Objects.requireNonNull
                                    (this.getClass().getResource("img/Attack/" + unit + ".png"))));
                            defendPointsDec.setImg(new ImageIcon(Objects.requireNonNull
                                    (this.getClass().getResource("img/Attack/" + decimal + ".png"))));
                        }
                        else {
                            texts.setText(enemies.get(currentEnemy).getName() + " is imprisioned!");
                        }
                    }
                }

                millis2 = clock.millis();
                action = false;
            } else if((millis1 - millis2) >= 1000)
                cleanNumbers();

            //Se zerar a vida do inimigo, avança o nível
            if((enemies.get(currentEnemy).getHealth() <= 0 && isAncestorOf(division)) || isAncestorOf(transitionPane)) {
                if(isAncestorOf(division) && currentEnemy < 3)
                    currentEnemy++;

                if(levels < 3) {
                    cleanNumbers();

                    level.setStop(true);
                    menuSong.setStop(true);

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

                    level.setStop(false);

                    if(levels < 2)
                        level.play();
                    else
                        boss.play();
                } else {
                    remove(division);
                    add(victoryPane);

                    level.setStop(true);
                    boss.setStop(true);
                    victorySound.play();
                }

                //Fecha e abre a tela para atualizar
                setVisible(false);
                setVisible(true);

                //Muda o background de acordo com o nível atual
                switch(levels) {
                    case -2:
                        menuSong.play();
                        break;

                    case -1:
                        backgroundImage.setImg(new ImageIcon(Objects.requireNonNull(this.getClass().
                                getResource("img/Background1.png"))));
                        //A música do nível pode ser tocada novamente
                        level.setStop(false);
                        boss.setStop(false);
                        //Para a música do menu
                        menuSong.setStop(true);
                        break;

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
                                getResource("img/BackgroundBoss.gif"))));
                        break;

                    default:
                        break;
                }

                levels++;
                texts.setText("Level passed!");
                action = false;
            }

            //Personagem que ainda está vivo (caso algum morra, será trocado para esse)
            int alive = currentHero;
            //Caso todos os heróis tenham morrido, aparece a tela de derrota
            int deads = 0;
            for(Character hero : heroes){
                if(hero.getHealth() <= 0){
                    deads++;
                } else {
                    alive = heroes.indexOf(hero);
                }
            }

            //Se estiver na tela de combate e os 4 heróis estão mortos, vai pra tela de derrota
            if(deads == 4 && isAncestorOf(division)){
                remove(division);
                add(defeatPane);

                level.setStop(true);
                boss.setStop(true);
                defeatSound.play();

                setVisible(false);
                setVisible(true);
            } else if(heroes.get(currentHero).getHealth() <= 0) {    //Troca automaticamente de herói caso o atual morra
                texts.setText("Hero: " + heroes.get(currentHero).getName() +" is dead!");
                currentHero = alive;
            }

            //Indicador de poder especial, alterando o texto do botão
            if(heroes.get(currentHero).getPowerCharge() >= 100) {
                buttons.get(2).setText("SPECIAL READY");
            } else {
                buttons.get(2).setText("Special");
            }

            //Não deixa aumentar o poder especial da leticia caso o inimigo esteja emprisionado
            if(enemies.get(currentEnemy).getImprisioned() > 0)
                heroes.get(2).setPowerCharge(0);

            if (isPaused)
                continue;

            if(isAncestorOf(menuPane) && menuSong.getStop()) {
                menuSong.setStop(false);
                menuSong.play();
            }

            //Atualiza as animações na tela
            division.repaint();
            victoryPane.repaint();
            menuPane.repaint();
            instructionsPane.repaint();
            defeatPane.repaint();

            // Unidade de tempo da animação
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void cleanNumbers() {
        //Limpa os números da tela
        defendPointsDec.setImg(new ImageIcon(Objects.requireNonNull
                (this.getClass().getResource("img/Defend/null.png"))));
        defendPointsUnit.setImg(new ImageIcon(Objects.requireNonNull
                (this.getClass().getResource("img/Defend/null.png"))));

        damagePointsDec.setImg(new ImageIcon(Objects.requireNonNull
                (this.getClass().getResource("img/Defend/null.png"))));
        damagePointsUnit.setImg(new ImageIcon(Objects.requireNonNull
                (this.getClass().getResource("img/Defend/null.png"))));

        // Limpa as imagens dos ataques e defesas especiais da tela
        specialAttackHero.setImg(new ImageIcon(Objects.requireNonNull
                (this.getClass().getResource("img/null.png"))));
        specialDefendHero.setImg(new ImageIcon(Objects.requireNonNull
                (this.getClass().getResource("img/null.png"))));
        specialAttackEnemy.setImg(new ImageIcon(Objects.requireNonNull
                (this.getClass().getResource("img/null.png"))));
        specialDefendEnemy.setImg(new ImageIcon(Objects.requireNonNull
                (this.getClass().getResource("img/null.png"))));
    }

}