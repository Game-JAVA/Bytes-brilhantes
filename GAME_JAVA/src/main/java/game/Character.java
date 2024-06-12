package game;

import java.util.Random;

public abstract class Character extends Image {

    Random r = new Random();

    //Atributos do personagem
    private int health, attack, defense, powerCharge = 0, powerLoad;
    private String name;

    //Construtor
    public Character(int health, int attack, int defense, int powerLoad, //Atributos de Image ->
                     String name, String url, int position_x, int position_y, String sound) {
        //Instanciando classe pai Image
        super(url, position_x, position_y, sound);
        //Instanciando classe Character
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.name = name;
        this.powerLoad = powerLoad;
    }

    //Métodos
    public int attack(Character c) {
        int damage = this.attack;

        //10% de chance de acerto crítico
        if (r.nextInt(0,100) < 10)
            damage *= 2;

        c.setHealth((c.getHealth() + c.getDefense()) - damage);

        //Se o inimigo estiver com pontos de defesa após o combate, retirar sua defesa
            //(pois a defesa dura somente uma rodada)
        if(c.isDefending())
            c.setDefense(0);

        //No fim de cada rodada, aumentar a carga do poder do personagem
        increasePowerCharge();

        return damage;
    }

    public int defend() {
        //Se o personagem já estiver com pontos de defesa, retirar sua defesa
        //(pois a defesa dura somente uma rodada)
        if(this.isDefending())
            this.setDefense(0);

        this.defense = r.nextInt(15,31);
        powerCharge += powerLoad;

        //No fim de cada rodada, aumentar a carga do poder do personagem
        increasePowerCharge();

        return defense;
    }

    //Retorna true se defesa for maior que 0 (se está com pontos de defesa)
    private boolean isDefending() {
        return getDefense() > 0;
    }

    //Aumenta a carga do poder se ainda não estiver cheia
    private void increasePowerCharge() {
        if(this.powerCharge < 100)
            this.powerCharge += this.powerLoad;
        else
            this.powerCharge = 100;
    }

    public abstract void specialPower();

    //Getters / Setters
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getPowerCharge() {
        return powerCharge;
    }

    public String getName() {
        return name;
    }

}