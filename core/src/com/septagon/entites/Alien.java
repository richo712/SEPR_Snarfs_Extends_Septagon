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
        Engine closestEngine = null;
        int closestDistance = Integer.MAX_VALUE;
        for (Engine e : engines){

            if (closestEngine == null){
                closestEngine = e;
            }

            int distance = Maths.manDistance(this.getCol(), this.getRow(), e.getCol(), e.getRow());
            if( distance < this.vision && (distance < closestDistance) ){
                closestEngine = e;
                closestDistance = distance;
                }

            }

        this.targetRow = closestEngine.getRow();
        this.targetCol = closestEngine.getCol();

    }


    public void move(TiledGameMap map, ArrayList<Engine> engines){
        findTargetEngine(engines); //Find a target
        //Get of an arrayList of tiles that give a path to that target
        ArrayList<Tile> pathArray = Maths.findPathTo(this.col, this.row, this.targetCol-1, this.targetRow-1, map);
        float costTotal = 0;
        Tile targetTile = map.getTileByCoordinate(0, this.getCol(), this.getRow());
        for (Tile t : pathArray){
            if (costTotal+t.getTileCost() < speed){ //If the cost to get to the next tile, is below the speed of the alien
                costTotal += t.getTileCost();
                targetTile = t;
            } else{
                break;
            }
        }
        this.setPosition(targetTile.getCol(), targetTile.getRow());
    }

    public int getSpeed(){ return this.speed; }
    public int getVision(){ return this.vision;}
}
