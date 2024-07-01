package game;

public class Enemy extends Character {

    public Enemy(int health, int attack, int defense, int powerLoad, String name, String url, int position_x, int position_y, String sound) {
        super(health, attack, defense, powerLoad, name, url, position_x, position_y, sound);
    }

    @Override
    public void specialPower(Character c) {
        // Ativa a habilidade especial do vilão
        this.setPowerCharge(0);
    }

}
