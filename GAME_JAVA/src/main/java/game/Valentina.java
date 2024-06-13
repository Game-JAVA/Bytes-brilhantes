package game;

public class Valentina extends Hero {

    @Override
    public void specialPower(Character c){

        //Se o poder estiver carregado
        if (this.getPowerCharge() == 100 ) {

            //Se a vida de outro heroi estiver =0
            if (c.getHealth()<= 0) {

                //Ele recarregará a vida para 100
                c.setHealth(100);

                //E o poder especial voltará a 0
                this.setPowerCharge(0);
            }

        }


    }

}
