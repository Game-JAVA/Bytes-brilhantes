package game;

import javafx.scene.image.Image;

public class Bruno extends Character{

    @Override
    public  void specialPower(Character c){

        //Se o poder estiver carregado
        if (this.getPowerCharge() == 100 ){

            //O ataque será multiplicado por 3
            this.setAttack(this.getAttack()*3);
            this.attack(c);

            //E depois voltará ao normal
            this.setAttack(this.getAttack()/3);

            //E o poder especial voltará a 0
            this.setPowerCharge(0);
        }
    }

}
