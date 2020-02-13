package com.septagon.minigame;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.entites.Entity;
import com.septagon.entites.Tile;

public class WaterBalloon extends Entity {

    //Speed the water balloon will move vertically
    int speed;

    public WaterBalloon(int xpos, int ypos, Texture texture, int speed){
        super(xpos, ypos,  45, 45, texture);
        this.setX(xpos);
        this.setY(ypos);
        this.speed = speed;
    }

    /**
     * Moves the water balloon vertically an amount depending the balloons speed
     */
    public void move(){
        this.y += speed;
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

    public int getSpeed(){ return this.speed; }
}
