package game;

import javax.swing.JLabel;
import java.util.Random;

public class Enemy extends Character {
    private boolean intangible; // Flag para indicar se o vilão está intangível
    private int roundsSinceSpecial; // Contador de rodadas desde a última vez que usou o poder especial

    public Enemy(int health, int attack, int defense, int powerLoad, String name, String url, int position_x, int position_y, String sound) {
        super(health, attack, defense, powerLoad, name, url, position_x, position_y, sound);
        this.intangible = false;
        this.roundsSinceSpecial = 0;
    }

    JLabel texts = new JLabel();

    @Override
    public void specialPower(Character c) {
        // Ativa a habilidade especial do vilão se não estiver intangível e se passaram pelo menos 3 rodadas desde a última vez
        if (!intangible && roundsSinceSpecial >= 3) {
            this.intangible = true;
            texts.setText("Enemy is now immune to damage for this round.");
            this.setPowerCharge(0);
            roundsSinceSpecial = 0; // Reinicia o contador de rodadas desde o último uso do poder especial
        } else {
            texts.setText("Enemy's special power is on cooldown.");
        }
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

    public void takeAction() {
        Random random = new Random();
        int action = random.nextInt(2); // Gera um número entre 0 e 1 (0 para ataque, 1 para especial)

        if (roundsSinceSpecial >= 3 && action == 1) {
            specialPower(null);
        } else {
            attack(null);
        }

        if (intangible) {
            roundsSinceSpecial++;
            intangible = false; // Reset intangibility after one round
        } else {
            roundsSinceSpecial++;
        }
    }
}
