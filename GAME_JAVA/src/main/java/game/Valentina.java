package game;

public class Valentina extends Character{

    public Valentina(int health, int attack, int defense, int powerLoad, String name, String url, int position_x, int position_y, String sound) {
        super(health, attack, defense, powerLoad, name, url, position_x, position_y, sound);
    }

    @Override
    public void specialPower(Character c){
        //Se o poder estiver carregado
        if (this.getPowerCharge() >= 100 ) {
            //Se a vida de outro heroi estiver =0
            if (c.getHealth()<= 0) {
                //Ele recarregará a vida para 100
                c.setHealth(100);

                //E o poder especial voltará a 0
                this.setPowerCharge(0);
                this.sound.play();
            }
        }
    }

}