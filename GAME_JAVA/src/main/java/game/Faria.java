package game;

public class Faria extends Character {
    //O poder especial já começa falso
    private boolean special = false;

    //Número de rodadas é = 0
    private int rounds = 0;

    public Faria(int health, int attack, int defense, int powerLoad, String name, String url, int position_x, int position_y, String sound) {
        super(health, attack, defense, powerLoad, name, url, position_x, position_y, sound);
    }

    @Override
    public void specialPower(Character c) {
        //Se o poder estiver carregado a 100
        if (this.getPowerCharge() >= 100) {
            //Vai aumentar o  ataque em 3x
            this.setAttack(this.getAttack() * 3);

            //Poder especial ativo
            special = true;
            this.sound.play();

            //E o poder especial voltará a 0
            this.setPowerCharge(0);
        }
    }

    @Override
    public int attack(Character c) {
        if(special) {
            //Se passar de 3 rodadas, o ataque especial para de funcionar
            if (rounds >= 3) {
                this.setAttack(this.getAttack() / 3);
                special = false;
                rounds = 0;

                //Caso não passe,  o ataque continua funcionando
            } else {
                rounds++;
            }

            //E o poder especial voltará a 0
            this.setPowerCharge(0);
        } else {
            //Ao atacar, aumentar a carga do poder do personagem
            increasePowerCharge();
        }

        int damage;

        //Verifca se o personagem está aprisionado (poder da Letícia)
        if(this.getImprisioned() <= 0){
            damage = this.getAttack() - (c.getMitigation() + c.getDefense());
            if(damage < 0)
                damage = 0;
        } else {
            damage = 0;
            this.setImprisioned(this.getImprisioned() - 1);;
            return -1;
        }

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

}