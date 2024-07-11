package game;

public class Bruno extends Character {

    public Bruno(int health, int attack, int defense, int powerLoad, String name, String url, int position_x, int position_y, String sound) {
        super(health, attack, defense, powerLoad, name, url, position_x, position_y, sound);
    }

    @Override
    public  void specialPower(Character c){
        //Se o poder estiver carregado
        if (this.getPowerCharge() >= 100){
            //O ataque será multiplicado por 3
            this.setAttack(this.getAttack()*3);
            this.attack(c);

            //E depois voltará ao normal
            this.setAttack(this.getAttack()/3);

            //E o poder especial voltará a 0
            this.setPowerCharge(0);
            this.sound.play();
        }
    }

}