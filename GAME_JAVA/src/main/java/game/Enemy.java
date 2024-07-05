package game;

public class Enemy extends Character {
    private boolean specialDefense; // Flag para indicar se a defesa especial está ativa

    public Enemy(int health, int attack, int defense, int powerLoad, String name, String url, int position_x, int position_y, String sound) {
        super(health, attack, defense, powerLoad, name, url, position_x, position_y, sound);
        this.specialDefense = false;
    }

    @Override
    public void specialPower(Character c) {

    }
}