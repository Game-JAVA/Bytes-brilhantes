package game;

public class Leticia extends Character{

    @Override
    public void specialPower (Character c){
        //Se o poder estiver carregado
        if(this.getPowerCharge() == 100){
            //O inimigo será aprisionado
            c.setImprisioned(2);

            //E o poder especial voltará a 0
            this.setPowerCharge(0);
        }
    }

}
