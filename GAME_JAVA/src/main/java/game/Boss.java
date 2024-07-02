package game;

public class Boss extends Character{
    public Boss(int health, int attack, int defense, int powerLoad, String name, String url, int position_x, int position_y, String sound) {
        super(health, attack, defense, powerLoad, name, url, position_x, position_y, sound);
    }
    @Override
    public void specialPower( Character c){
        //Definir poder especial
        this.setPowerCharge(0);
    }
}

