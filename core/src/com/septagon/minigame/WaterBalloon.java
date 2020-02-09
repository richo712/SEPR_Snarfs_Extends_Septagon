package com.septagon.minigame;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.entites.Entity;
import com.septagon.entites.Tile;

public class WaterBalloon extends Entity {

    int speed;

    public WaterBalloon(int xpos, int ypos, Texture texture, int speed){
        super(xpos, ypos, Tile.TILE_SIZE, Tile.TILE_SIZE, texture);
        this.setX(xpos);
        this.setY(ypos);
        this.speed = speed;
    }

    public void move(){
        this.y += speed;
    }

    public boolean isOutOfBounds(int yBoundary){
        if (this.getY() > yBoundary){
            return true;
        } else{
            return false;
        }
    }


}
