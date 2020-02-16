package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.helperClasses.AssetManager;
import com.septagon.helperClasses.Maths;
import com.septagon.helperClasses.TileManager;
import com.septagon.states.GameState;

import java.util.*;

/**
 * Class used to define and control all Alien Patrols within the game
 * Everything within this Class is new for Assessment 3
 */

public class Alien extends Attacker {

    private int speed, vision, targetCol, targetRow, pathNum;
    private Engine targetEngine;
    private int[][] path;
    private int id;

    /**
     * Constructor for the Alien class
     * @param col Starting column
     * @param row Starting row
     * @param width Entity width
     * @param height Entity height
     * @param texture Entity texture
     * @param health Alien max health
     * @param damage Damage alien deals to targets
     * @param range Attack range in tiles
     * @param speed Movement Speed in tiles
     * @param vision Range of vision in tiles
     * @param path Patrol path
     * @param id Unique identifier
     */
    public Alien(int col, int row, int width, int height, Texture texture, int health, int damage, int range, int speed, int vision, int[][] path, int id){
        super(col, row, width, height, texture, health, damage, range);
        this.speed = speed;
        this.vision = vision;
        this.path = path;
        this.pathNum = 0;
        this.id = id;
    }

    /**
     * Method which finds the closest fire engine to the selected alien
     * @param engines ArrayList of engines in the game to be checked
     */
    public void findTargetEngine(ArrayList<Engine> engines){
        Engine closestEngine = null;
        int closestDistance = Integer.MAX_VALUE;
        for (Engine e : engines){

            //If no target, and current selection isn't dead, set target to current selection
            if ((closestEngine == null) && (!e.isDead())){
                closestEngine = e;
            }

            //Gets the distance of the selected engine. If less than the current "closestEngine", sets as the new "closestEngine"
            int distance = Maths.manDistance(this.getCol(), this.getRow(), e.getCol(), e.getRow());
            if( distance < this.vision && (distance < closestDistance) ){
                closestEngine = e;
                closestDistance = distance;
                }

            }
        //Sets the "closestEngine" as the target if within line of sight
        if (closestDistance < this.vision) {
            this.targetRow = closestEngine.getRow();
            this.targetCol = closestEngine.getCol();
            this.targetEngine = closestEngine;
        } else{
            this.targetEngine = null;
        }

    }

    /**
     * Method which handles Aliens attacking their targeted Fire Engine
     */
    public void DamageEngineIfInRange(){

        if ((this.targetEngine != null) && (!this.targetEngine.isDead())) {
            int distance = Maths.manDistance(this, this.targetEngine);

            if (distance < this.getRange()) {
                System.out.println("Alien attacking");
                this.targetEngine.takeDamage(this.damage);
                System.out.println(this.targetEngine.health);

                if (this.targetEngine.health <= 0) {
                    this.targetEngine.setDead();
                }
                GameState.bullets.add(new Bullet(this.getX(), this.getY(), this.targetEngine.getX(), this.targetEngine.getY(), false));
            }
        }
    }

    /**
     * Method which handles Aliens attacking the Fire Station
     * @param fireStation Station object. The user's base Fire Station
     */
    public void DamageStationIfInRange(Station fireStation){
        int distance = Maths.manDistance(this.getCol(), this.getRow(), fireStation.getCol(), fireStation.getRow());
        if (distance < this.getRange()) {
            fireStation.takeDamage(this.damage);
            GameState.bullets.add(new Bullet(this.getX(), this.getY(), fireStation.getX(), fireStation.getY(), false));
        }

    }

    /**
     * Method to handle movement of the Aliens around the game map
     *
     * @param tileManager TileManager object for setting tile traits
     * @param map TiledGameMap object of the current game map for finding distances
     * @param engines ArrayList of Engines used for finding which is the current target
     * @param targetStation Boolean: True if aliens should move towards and attack the Fire Station
     * @param state Current GameState
     */
    public void move(TileManager tileManager, TiledGameMap map, ArrayList<Engine> engines, boolean targetStation, GameState state){
        tileManager.getTileAtLocation(this.col, this.row).setOccupied(false);
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
            if(targetStation){ //If no fire engines in range, proceed towards the fire station
                wander(map, state.fireStation.getCol(), state.fireStation.getRow());

            } else { //If no target, continue on patrol
                wander(map, path[pathNum][0], path[pathNum][1]);
                pathNum++;
                if (pathNum >= path.length) {
                    pathNum = 0;
                }
            }
        }
        tileManager.getTileAtLocation(this.col, this.row).setOccupied(true);
    }

    /**
     * Method to ensure the Aliens dont try and move to a negative grid position
     * @param x Co-ordinate to check
     * @return Parameter 'x' that is modified to be within the boundaries of the map
     */
    public int fixCoord(int x){
        if(x >= 0 && x <= 199) {
            return x;
        } else if(x < 0){
            return 0;
        } else {
            return 199;
        }
    }

    /**
     * Method to handle patrol-like movement of the Alien
     * @param map Current game map used to find paths
     * @param col Column to move to
     * @param row Row to move to
     */
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

    //Getters

    public int getSpeed(){ return this.speed; }

    public int getVision(){ return this.vision; }

    public Engine getTargetEngine(){ return this.targetEngine; }

    //Setters

    public void setDeadTexture(){
        setTexture(AssetManager.getDeadAlienTexture1());
        switch(id){
            case(0):
                setTexture(AssetManager.getDeadAlienTexture1());
                break;
            case(1):
                setTexture(AssetManager.getDeadAlienTexture2());
                break;
            case(2):
                setTexture(AssetManager.getDeadAlienTexture3());
                break;
            case(3):
                setTexture(AssetManager.getDeadAlienTexture4());
                break;

        }
    }
}
