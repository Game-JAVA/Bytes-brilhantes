package game;

public class Leticia extends Character{

    public Leticia(int health, int attack, int defense, int powerLoad, String name, String url, int position_x, int position_y, String sound) {
        super(health, attack, defense, powerLoad, name, url, position_x, position_y, sound);
    }

    @Override
    public void specialPower(Character c) {
        //Se o poder estiver carregado
        if(this.getPowerCharge() >= 100){
            //O inimigo será aprisionado
            c.setImprisioned(2);

            //E o poder especial voltará a 0
            this.setPowerCharge(0);
            this.sound.play();
        }
    }

}
