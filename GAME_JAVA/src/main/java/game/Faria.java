package game;



public class Faria extends Hero {
    //O poder especial já começa falso
    private boolean special = false;

    //Número de rodadas é = 0
    private int rounds=0;

    @Override
    public void SpecialPower(){
        //Se o poder estiver carregado a 100
        if (this.getPowerCharge() == 100 ) {
            //Vai aumetar o  ataque em 2x
            this.setAttack(this.getAttack()*2);

            // poder especial ativo
            special = true;
        }

    }

    @Override
    public int attack(Character c) {
        if (special){
            //Se passar de 3 rodadas, o ataque especial para de funcionar
            if (rounds>=3){
                this.setAttack(this.getAttack()/2);
                special = false;

                //E o poder especial voltará a 0
                this.setPowerCharge(0);
                //Caso não passe,  o ataque continua funcionando
            } else {
                rounds++;
            }
        }

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


}
