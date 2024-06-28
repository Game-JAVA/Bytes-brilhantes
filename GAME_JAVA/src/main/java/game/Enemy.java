package game;

import javax.swing.JLabel;
import java.util.Random;

public class Enemy extends Character {
    private boolean specialDefense; // Flag para indicar se a defesa especial está ativa

    public Enemy(int health, int attack, int defense, int powerLoad, String name, String url, int position_x, int position_y, String sound) {
        super(health, attack, defense, powerLoad, name, url, position_x, position_y, sound);
        this.specialDefense = false;
    }

    JLabel texts = new JLabel();

    @Override
    public void specialPower(Character c) {
        // Ativa a habilidade especial do vilão
        this.specialDefense = true;
        this.setPowerCharge(0);
    }


    private int calculateDamage(Character c) {
        int damage = this.getAttack() - (c.getMitigation() + c.getDefense());
        if (damage < 0) {
            damage = 0;
        }

        // 10% chance de acerto crítico
        Random random = new Random();
        if (random.nextInt(100) < 10) {
            damage *= 2;
        }

        c.setHealth(c.getHealth() - damage);

        // Limpar defesa do oponente após o ataque, se estiver defendendo
        if (c.isDefending()) {
            c.setDefense(0);
        }

        return damage;
    }
}
