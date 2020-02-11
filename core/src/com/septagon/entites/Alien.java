package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.helperClasses.Maths;
import com.septagon.states.GameState;

import java.util.*;

public class Alien extends Attacker {

    private int speed, vision, targetCol, targetRow, pathNum;
    private Engine targetEngine;
    private int[][] path;

    public Alien(int col, int row, int width, int height, Texture texture, int health, int damage, int range, int speed, int vision, int[][] path){
        super(col, row, width, height, texture, health, damage, range);
        this.speed = speed;
        this.vision = vision;
        this.path = path;
        this.pathNum = 0;
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
        if (closestDistance < this.vision) {
            this.targetRow = closestEngine.getRow();
            this.targetCol = closestEngine.getCol();
            this.targetEngine = closestEngine;
        } else{
            this.targetEngine = null;
        }

    }

    public void DamageEngineIfInRange(){
        if (this.targetEngine != null){
            int distance = Maths.manDistance(this, this.targetEngine);
            if (distance < this.getRange()){
                this.targetEngine.takeDamage(this.damage);
                GameState.bullets.add(new Bullet(this.getX(), this.getY(), this.targetEngine.getX(), this.targetEngine.getY(), false));
            }
        }
        System.out.println("Alien attacking");
    }

    public void move(TiledGameMap map, ArrayList<Engine> engines){
        findTargetEngine(engines); //Find a target
        //Get of an arrayList of tiles that give a path to that target
        if (this.targetEngine != null) {
            ArrayList<Tile> pathArray = Maths.findPathTo(this.col, this.row, fixCoord(this.targetCol - 1), fixCoord(this.targetRow - 1), map);
            float costTotal = 0;
            Tile targetTile = map.getTileByCoordinate(0, this.getCol(), this.getRow());
            for (Tile t : pathArray) {
                if (costTotal + t.getTileCost() < speed) { //If the cost to get to the next tile, is below the speed of the alien
                    costTotal += t.getTileCost();
                    targetTile = t;
                } else {
                    break;
                }
            }
            this.setPosition(fixCoord(targetTile.getCol()), fixCoord(targetTile.getRow()));
        }else{
            wander(map);
            pathNum++;
            if(pathNum >= path.length){
                pathNum = 0;
            }
        }
    }

    private int fixCoord(int x){
        if(x >= 0) {
            return x;
        }
        return 0;
    }

    private void moveTo(Tile dest){
        System.out.println("MoveTo");
        int speedCheck = 0;

        for(int i = 0; i < 32; i++){
            double loopTime = System.currentTimeMillis();
            double curTime = loopTime;
            while(curTime-loopTime < 30){
                curTime = System.currentTimeMillis();
            }


            if(dest.col > this.col){
                setX(this.x ++);
            } else if(dest.col < this.col){
                setX(this.x --);
            }
            if(dest.row > this.row){
                setY(this.y ++);
            } else if(dest.row < this.row){
                setY(this.y --);
            }
            System.out.println(this.y);
            System.out.println(this.row);
        }
        this.setPosition(dest.col, dest.row);
    }

    private void wander(TiledGameMap map){
        int curX = fixCoord(path[pathNum][0]);
        int curY = fixCoord(path[pathNum][1]);
        if(Maths.manDistance(this.col, this.row, curX, curY) <= this.range) {
            ArrayList<Tile> path = Maths.findPathTo(this.col, this.row, curX, curY, map);
            if(path.size() > this.speed) {
                for (int i = 0; i < this.speed; i++) {
                    moveTo(path.get(i));
                }
            } else {
                for(Tile i : path){
                    moveTo(i);
                }
            }
            //this.setPosition(curX, curY);
        }else{


        }
    }

    public int getSpeed(){ return this.speed; }
    public int getVision(){ return this.vision;}

    public void setDead(){ this.dead = true;}
}
