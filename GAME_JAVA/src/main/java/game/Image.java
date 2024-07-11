package game;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Objects;
import javax.swing.*;

public class Image {

    private ImageIcon img;
    private int width, height, position_x, position_y;
    Sound sound;

    //CONSTRUTORES -> um com som necessário, outro sem
    public Image(String url, int position_x, int position_y) {
        this.img = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(url)));
        this.position_x = position_x;
        this.position_y = position_y;
        this.width = img.getIconWidth();
        this.height = img.getIconHeight();
        this.sound = null;
    }

    public Image(String url, int position_x, int position_y, String sound, boolean loop) {
        this.img = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(url)));
        this.position_x = position_x;
        this.position_y = position_y;
        this.width = img.getIconWidth();
        this.height = img.getIconHeight();
        this.sound = new Sound(sound, loop);
    }

    //MÉTODOS
    //Desenha a imagem na tela
    public void draw(Graphics g) {
        g.drawImage(img.getImage(), this.position_x, this.position_y,
                this.width, this.height, null);
    }

    public void setImg(ImageIcon img) {
        this.img = img;
    }

    public void setPosition_x(int position_x) {
        this.position_x = position_x;
    }

    public void setPosition_y(int position_y) {
        this.position_y = position_y;
    }
}