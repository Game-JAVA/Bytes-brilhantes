package game;

import java.util.Random;

public abstract class Character extends Image {

    Random r = new Random();

    //Atributos do personagem
    private int health, attack, mitigation, defense = 0, powerCharge = 0, imprisioned = 0;
    private final int powerLoad;
    private final String name;
    protected final Sound hitSound = new Sound("sound/hit.wav", false),
            defenseSound = new Sound("sound/defend.wav", false);

    //Construtor
    public Character(int health, int attack, int mitigation, int powerLoad, //Atributos de Image ->
                     String name, String url, int position_x, int position_y, String sound) {
        //Instanciando classe pai Image
        super(url, position_x, position_y, sound, false);
        //Instanciando classe Character
        this.health = health;
        this.attack = attack;
        this.mitigation = mitigation;
        this.name = name;
        this.powerLoad = powerLoad;
    }

    //Métodos
    public int attack(Character c) {
        int damage = 0;

        //Verifca se o personagem está aprisionado (poder da Letícia)
        if(this.imprisioned <= 0){
            damage = this.attack - (c.getMitigation() + c.getDefense());
            if(damage < 0)
                damage = 0;
            //Ao atacar, aumentar a carga do poder do personagem
            increasePowerCharge();
        } else {
            this.imprisioned--;
            return -1;
        }

        //10% de chance de acerto crítico
        if (r.nextInt(0,100) < 10)
            damage *= 2;

        c.setHealth(c.getHealth() - damage);

        if(c.getHealth() < 0)
            c.setHealth(0);

        //Se o inimigo estiver com pontos de defesa após o combate, retirar sua defesa
        //(pois a defesa dura somente uma rodada)
        if(c.isDefending())
            c.setDefense(0);

        this.hitSound.play();

        return damage;
    }

    public int defend() {
        //Se o personagem já estiver com pontos de defesa, retirar sua defesa
        //(pois a defesa dura somente uma rodada)
        if(this.isDefending())
            this.defense = 0;

        //Verifca se o personagem está aprisionado (poder da Letícia)
        if(this.imprisioned <= 0) {
            this.defense = r.nextInt(5,16);
            //Ao defender, aumentar a carga do poder do personagem
            increasePowerCharge();
        } else {
            this.defense = 0;
            this.imprisioned--;
            return -1;
        }

        this.defenseSound.play();

        return defense;
    }

    //Retorna true se defesa for maior que 0 (se está com pontos de defesa)
    protected boolean isDefending() {
        return getDefense() > 0;
    }

    //Aumenta a carga do poder se ainda não estiver cheia
    protected void increasePowerCharge() {
        if(this.powerCharge < 100)
            this.powerCharge += this.powerLoad;
        else
            this.powerCharge = 100;
    }

    public abstract void specialPower(Character c);

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

    public int getImprisioned() {
        return imprisioned;
    }

    public int getMitigation() {
        return mitigation;
    }

}