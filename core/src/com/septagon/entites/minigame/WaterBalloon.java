package com.septagon.entites.minigame;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.entites.Entity;

public class WaterBalloon extends Entity {

    //Speed the water balloon will move vertically
    float speed;
    float xActual, yActual;

    public WaterBalloon(int xpos, int ypos, Texture texture, int size, float speed){
        super(0, 0,  size, size, texture);
        this.speed = speed;
        this.setxActual(xpos);
        this.setyActual(ypos);
    }

    /**
     * Moves the water balloon vertically an amount depending the balloons speed
     */
    public void move(){
        this.setyActual( this.yActual + speed);
    }

    /**
     * Checks if the balloon is too high up, so the state can delete it.
     * @param yBoundary The y value that the balloon may be above
     * @return True if above boundary, false otherwise
     */
    public boolean isOutOfBounds(int yBoundary){
        if (this.getY() > yBoundary){
            return true;
        } else{
            return false;
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

    public float getSpeed(){ return this.speed; }
}
