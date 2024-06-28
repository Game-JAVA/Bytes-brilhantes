package game;

import javax.swing.JLabel;
import java.util.Random;

public class Enemy extends Character {
    private boolean intangible; // Flag para indicar se o vilão está intangível
    private boolean specialDefense; // Flag para indicar se a defesa especial está ativa

    public Enemy(int health, int attack, int defense, int powerLoad, String name, String url, int position_x, int position_y, String sound) {
        super(health, attack, defense, powerLoad, name, url, position_x, position_y, sound);
        this.intangible = false;
        this.specialDefense = false;
    }

    JLabel texts = new JLabel();

    @Override
    public void specialPower(Character c) {
        // Ativa a habilidade especial do vilão
        this.intangible = true;
        this.specialDefense = true;
        texts.setText("Enemy used special power and is now immune to damage for this round.");
        this.setPowerCharge(0);
    }

    @Override
    public String attack(Character c) {
        if (!intangible) {
            int damage = calculateDamage(c);
            texts.setText(getName() + " attacked " + c.getName() + ", dealing " + damage + " damage!");
            return getName() + " attacked " + c.getName() + ", dealing " + damage + " damage!";
        } else {
            texts.setText(getName() + " is immune to damage for this round.");
            return getName() + " is immune to damage for this round.";
        }
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

    public void takeAction(Character hero) {
        increasePowerCharge();

        if (this.getPowerCharge() >= 100) {
            specialPower(hero);
        } else {
            Random random = new Random();
            int action = random.nextInt(2); // Gera um número entre 0 e 1 (0 para ataque, 1 para especial)
            if (action == 1 && !specialDefense) {
                specialPower(hero);
            } else {
                attack(hero);
            }
        }

        if (intangible) {
            intangible = false; // Reseta a invencibilidade depois do uso.
        }

        if (specialDefense) {
            this.setDefense(99); // Defesa no máximo por um round
            specialDefense = false; // Após esse round a defesa volta ao normal
        }
    }
}
