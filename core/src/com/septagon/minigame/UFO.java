package com.septagon.minigame;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.entites.Entity;
import com.septagon.entites.Tile;

public class UFO extends Entity {

    //Distance UFO can travel each time move is called
    float speed;
    //Stores x and y coordinates in a float so position is more accurate, and speed can be set to non ints
    float xActual, yActual;
    //Used in move() to tell UFO which way it should move
    public enum Direction{
        LEFT, RIGHT, DOWN
    }
    //Used for a sin function so UFOs bob up and down, increases every time they move left or right
    public int time = 0;

    public UFO(int xpos, int ypos, Texture texture, int size, float speed){
        super(0, 0, size, size, texture);
        this.speed = speed;
        this.setxActual(xpos);
        this.setyActual(ypos);
    }

    /**
     * Shifts the UFO in a direction, an amount equal to its speed.
     * @param direction Enum corresponding to a direction: right, left, or down
     */
    public void move(Direction direction){
        switch (direction){
            case LEFT:
                this.time += 1;
                this.setxActual(this.xActual - this.speed);
                this.setyActual(this.yActual + (float) Math.sin(time*0.06) * 0.4f);
                break;
            case RIGHT:
                this.time += 1;
                this.setxActual(this.xActual + this.speed);
                this.setyActual(this.yActual + (float) Math.sin(time*0.06) * 0.4f);
                break;
            case DOWN:
                this.setyActual(this.yActual - this.speed + (float) Math.sin(time*0.06) * 0.4f);
                break;
        }
    }

    /**
     * Sets xActual, also calls setX(), the X value corresponds to the position the UFO is,
     * the xActual is just so fine changes aren't lost to rounding.
     * @param x a float the xActual will be set to
     */
    public void setxActual(float x){
        this.xActual = x;
        this.setX(Math.round(x));
    }

    /**
     * Sets yActual, also calls setY(), the Y value corresponds to the position the UFO is,
     * the yActual is just so fine changes aren't lost to rounding.
     * @param y a float the yActual will be set to
     */
    public void setyActual(float y){
        this.yActual = y;
        this.setY(Math.round(y));
    }
}
