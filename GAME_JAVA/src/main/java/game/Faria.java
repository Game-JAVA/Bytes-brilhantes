package game;

public class Faria extends Character{
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
        if (this.getPowerCharge() == 100) {
            //Vai aumetar o  ataque em 2x
            this.setAttack(this.getAttack() * 2);

            // poder especial ativo
            special = true;
        }
    }

    @Override
    public int attack(Character c) {
        if (special) {
            //Se passar de 3 rodadas, o ataque especial para de funcionar
            if (rounds >= 3) {
                this.setAttack(this.getAttack() / 2);
                special = false;

                //E o poder especial voltará a 0
                this.setPowerCharge(0);
                //Caso não passe,  o ataque continua funcionando
            } else {
                rounds++;
            }
        }

        int damage;

        //Verifca se o personagem está aprisionado (poder da Letícia)
        if (this.getImprisioned() <= 0) {
            damage = this.getAttack();
            //Ao atacar, aumentar a carga do poder do personagem
            this.increasePowerCharge();
        } else {
            damage = 0;
            this.setImprisioned(this.getImprisioned() - 1);
        }

        //10% de chance de acerto crítico
        if (r.nextInt(0, 100) < 10)
            damage *= 2;

        c.setHealth((c.getHealth() + c.getDefense()) - damage);

        //Se o inimigo estiver com pontos de defesa após o combate, retirar sua defesa
        //(pois a defesa dura somente uma rodada)
        if (c.isDefending())
            c.setDefense(0);

        return damage;

    }

}
