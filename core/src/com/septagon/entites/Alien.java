package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.helperClasses.AssetManager;
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

            if ((closestEngine == null) && (!e.isDead())){
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
        if ((this.targetEngine != null) && (!this.targetEngine.isDead())){
            int distance = Maths.manDistance(this, this.targetEngine);
            if (distance < this.getRange()){
                this.targetEngine.takeDamage(this.damage);
                System.out.println(this.targetEngine.health);
                if(this.targetEngine.health <= 0){
                    System.out.println(this.targetEngine.isDead());
                    System.out.println("It's dead!");
                    this.targetEngine.setDead();
                }
                GameState.bullets.add(new Bullet(this.getX(), this.getY(), this.targetEngine.getX(), this.targetEngine.getY(), false));
            }
        }
        //System.out.println("Alien attacking");
    }

    public void DamageStationIfInRange(Station fireStation){
        int distance = Maths.manDistance(this.getCol(), this.getRow(), fireStation.getCol(), fireStation.getRow());
        if (distance < this.getRange()) {
            fireStation.takeDamage(this.damage);
            GameState.bullets.add(new Bullet(this.getX(), this.getY(), fireStation.getX(), fireStation.getY(), false));
        }

    }

    public void move(TiledGameMap map, ArrayList<Engine> engines, boolean targetStation, GameState state){
        findTargetEngine(engines); //Find a target
        //Get of an arrayList of tiles that give a path to that target
        if ((this.targetEngine != null) && (!this.targetEngine.isDead())) {
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
            if(targetStation){
                wander(map, state.fireStation.getCol(), state.fireStation.getRow());

            } else {
                wander(map, path[pathNum][0], path[pathNum][1]);
                pathNum++;
                if (pathNum >= path.length) {
                    pathNum = 0;
                }
            }
        }
    }


    private int fixCoord(int x){
        if(x >= 0) { //TODO add in other boundaries once map is complete
            return x;
        }
        return 0;
    }

    private void wander(TiledGameMap map, int col, int row){
        int curX = fixCoord(col);
        int curY = fixCoord(row);
        ArrayList<Tile> path = Maths.findPathTo(this.col, this.row, curX, curY, map);
        if(path.size() > this.speed) {
            for (int i = 0; i < this.speed; i++) {
                this.setPosition(path.get(i).col, path.get(i).row);
            }
        } else {
            for(Tile i : path){
                this.setPosition(i.col, i.row);
            }
        }
    }

    public int getSpeed(){ return this.speed; }
    public int getVision(){ return this.vision;}

    public void setDeadTexture(){
        setTexture(AssetManager.getDeadAlienTexture1());
    }
}
