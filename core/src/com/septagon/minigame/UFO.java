package com.septagon.minigame;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.entites.Entity;
import com.septagon.entites.Tile;

public class UFO extends Entity {

    float speed;
    float xActual, yActual;
    public enum Direction{
        LEFT, RIGHT, DOWN
    }
    public int time = 0;

    public UFO(int xpos, int ypos, Texture texture, int size, float speed){
        super(xpos, ypos, size, size, texture);
        this.speed = speed;
        this.xActual = xpos * Tile.TILE_SIZE;
        this.yActual = ypos * Tile.TILE_SIZE;
    }

    public void move(Direction direction){
        switch (direction){
            case LEFT:
                this.setxActual(this.xActual - this.speed);
                this.setyActual(this.yActual + (float) Math.sin(time*0.06) * 0.4f);
                break;
            case RIGHT:
                this.setxActual(this.xActual + this.speed);
                this.setyActual(this.yActual + (float) Math.sin(time*0.06) * 0.4f);
                break;
            case DOWN:
                this.setyActual(this.yActual - this.speed + (float) Math.sin(time*0.06) * 0.4f);
                break;
        }
    }

    public boolean isCollidingWith(WaterBalloon waterBalloon){
        if (this.getX() <= waterBalloon.getX() && this.getX() + this.getWidth() >= waterBalloon.getX()){
            if(this.getY() <= waterBalloon.getY() && this.getY() + this.getHeight() >= waterBalloon.getY()){
                return true;
            }
        }
        return false;
    }

    public void setxActual(float x){
        this.xActual = x;
        this.setX(Math.round(x));
    }

    public void setyActual(float y){
        this.yActual = y;
        this.setY(Math.round(y));
    }
}
