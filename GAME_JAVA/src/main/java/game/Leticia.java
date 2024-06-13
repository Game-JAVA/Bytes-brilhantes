package game;

public class Leticia extends Hero{

    @Override
    public void specialPower (Character c){
        //Se o poder estiver carregado
        if(this.getPowerCharge() == 100){
            //O inimigo será aprisionado
            c.setImprisioned(true);

            //E o poder especial voltará a 0
            this.setPowerCharge(0);
        }
    }

}
