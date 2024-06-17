package game;

import java.util.Random;

public abstract class Character extends Image {

    Random r = new Random();

    //Atributos do personagem
    private int health, attack, defense, powerCharge = 0, imprisioned = 0;
    private final int powerLoad;
    private final String name;

    //Construtor
    public Character(int health, int attack, int defense, int powerLoad, //Atributos de Image ->
                     String name, String url, int position_x, int position_y, String sound) {
        //Instanciando classe pai Image
        super(url, position_x, position_y, sound, false);
        //Instanciando classe Character
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.name = name;
        this.powerLoad = powerLoad;
    }

    //Métodos
    public int attack(Character c) {
        int damage;

        //Verifca se o personagem está aprisionado (poder da Letícia)
        if(imprisioned <= 0){
            damage = this.attack;
            //Ao atacar, aumentar a carga do poder do personagem
            increasePowerCharge();
        } else {
            damage = 0;
            imprisioned--;
        }

        //10% de chance de acerto crítico
        if (r.nextInt(0,100) < 10)
            damage *= 2;

        c.setHealth((c.getHealth() + c.getDefense()) - damage);

        //Se o inimigo estiver com pontos de defesa após o combate, retirar sua defesa
            //(pois a defesa dura somente uma rodada)
        if(c.isDefending())
            c.setDefense(0);

        return damage;
    }

    public int defend() {
        //Se o personagem já estiver com pontos de defesa, retirar sua defesa
        //(pois a defesa dura somente uma rodada)
        if(this.isDefending())
            this.defense = 0;

        //Verifca se o personagem está aprisionado (poder da Letícia)
        if(imprisioned <= 0){
            this.defense = r.nextInt(15,31);
            //Ao defender, aumentar a carga do poder do personagem
            increasePowerCharge();
        } else {
            this.defense = 0;
            imprisioned--;
        }

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

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    public int getPowerCharge() {
        return powerCharge;
    }

    public void setPowerCharge(int powerCharge) {
        this.powerCharge = powerCharge;
    }

    public String getName() {
        return name;
    }

    public void setImprisioned(int imprisioned) {
        this.imprisioned = imprisioned;
    }

}