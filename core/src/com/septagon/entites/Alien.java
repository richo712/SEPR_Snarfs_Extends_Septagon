package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.helperClasses.Maths;

import java.util.*;

public class Alien extends Attacker {

    private int speed, vision, targetCol, targetRow;

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

            int distance = Maths.manDistance(this.col, this.row, e.getCol(), e.getRow());
            if( distance < this.vision && (distance < closest_distance) ){
                closest_engine = e;
                closest_distance = distance;
                }

            }

        this.targetRow = closest_engine.getRow();
        this.targetCol = closest_engine.getCol();

    }


    public void move(TiledGameMap map, ArrayList<Engine> engines){
        findTargetEngine(engines);
        ArrayList<Tile> pathArray = Maths.findPathTo(this.col, this.row, this.targetCol-1, this.targetRow-1, map);
        float costTotal = 0;
        Tile targetTile = null;
        for (Tile t : pathArray){
            if (costTotal+t.getTileCost() < speed){
                costTotal += t.getTileCost();
                targetTile = t;
            } else{
                break;
            }
        }
        if (targetTile != null){
            this.setCol(pathArray.get(pathArray.size() -1).getCol());
            this.setRow(pathArray.get(pathArray.size() -1).getRow());
        }

    }

    public int getSpeed(){
        return speed;
    }
}
