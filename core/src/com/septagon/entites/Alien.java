package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Alien extends Attacker {

    private int speed, vision, target_x, target_y;

    public Alien(int col, int row, int width, int height, Texture texture, int health, int damage, int range, int speed, int vision){
        super(col, row, width, height, texture, health, damage, range);
        this.speed = speed;
        this.vision = vision;
    }
    public void update(){

    }

    private void findTargetEngine(ArrayList<Engine> engines){
        Engine closest_engine = null;
        int closest_distance = Integer.MAX_VALUE;
        for (Engine e : engines){

            if (closest_engine == null){
                closest_engine = e;
            }

            int distX = Math.abs(e.getCol() - this.col);
            int distY = Math.abs(e.getRow() - this.row);
            int distance = distX + distY;
            if( (distX <= vision) && (distY <= vision) && (distance < closest_distance) ){
                closest_engine = e;
                closest_distance = distance;
                }

            }

        this.target_x = closest_engine.getX();
        this.target_y = closest_engine.getY();

    }

    public void move(){
        this.x+= 64;
        System.out.println("!!!");
        System.out.println(this.x);
    }

    public int getSpeed(){
        return speed;
    }
}
