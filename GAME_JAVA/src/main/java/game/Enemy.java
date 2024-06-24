package game;

public class Enemy extends Character {

    private boolean intangible; // Flag para indicar se o vilão está intangível

    public Enemy(int health, int attack, int defense, int powerLoad, String name, String url, int position_x, int position_y, String sound) {
        super(health, attack, defense, powerLoad, name, url, position_x, position_y, sound);
        this.intangible = false;
    }

    @Override
    public void specialPower(Character c) {
        // Ativa a habilidade especial do vilão
        this.intangible = true;
        System.out.println(this.getName() + " is now intangible and immune to damage for this round.");
        // Reinicia a carga do poder especial
        this.setPowerCharge(0);
    }

    @Override
    public int attack(Character c) {
        if (!intangible) {
            return super.attack(c);
        } else {
            System.out.println(this.getName() + " is intangible and cannot be damaged.");
            return 0; // Retorna 0 de dano se estiver intangível
        }
    }

    @Override
    protected void takeDamage(int damage) {
        if (!intangible) {
            super.takeDamage(damage);
        } else {
            System.out.println(this.getName() + " is intangible and takes no damage.");
        }
    }
}
