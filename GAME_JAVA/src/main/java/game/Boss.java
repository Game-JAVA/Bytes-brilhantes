package game;

import java.util.Random;

public class Boss extends Character {

    public Boss(int health, int attack, int defense, int powerLoad,
                String name, String url, int position_x, int position_y, String sound) {
        super(health, attack, defense, powerLoad, name, url, position_x, position_y, sound);
    }

    @Override
    public void specialPower(Character hero) {
        int damage = this.getAttack() - (hero.getMitigation() + hero.getDefense());
        if(damage < 0) {
            damage = 0;
        }

        // 10% chance de critical hit
        Random r = new Random();
        if(r.nextInt(100) < 10) {
            damage *= 2;
        }

        //Se cura metade do dano causado
        hero.setHealth(hero.getHealth() - damage);
        this.setHealth(this.getHealth() + damage / 2);

        //Zera a defesa do herói se ele estiver defendendo
        if(hero.isDefending())
            hero.setDefense(0);

        this.setPowerCharge(0);
        this.sound.play();
    }
}