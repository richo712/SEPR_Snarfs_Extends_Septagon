package com.septagon.entites;

/**
 * Class that inherits from vehicle that will define all the fire engines
 * in the game
 */

import com.badlogic.gdx.graphics.Texture;
import com.septagon.helperClasses.Maths;
import com.septagon.states.GameState;

import java.util.ArrayList;

public class Engine extends Vehicle
{
    //Member variables that will be unique stats of each engine
    private int volume;
    private int maxVolume;
    private int fillSpeed;
    private Integer id;

    //Keeps track of whether the engine has moved on the current player turn
    private boolean moved = false;

    /***
     * Constructor that Sets up the member variables for engine
     */
    public Engine(int col, int row, Texture texture, int health, int damage, int range, int speed, int maxVolume, int fillSpeed, Integer id) {
        super(col, row, texture, health, damage, range, speed);
        this.volume = maxVolume;
        this.maxVolume = maxVolume;
        this.fillSpeed = fillSpeed;
        this.id = id;
    }


    /***
     * Checks if the engine is in range to fill and calls the fill method if it is
     * @param s The Fire Station
     */
    public void ifInRangeFill(Station s){
        System.out.println("Checking if should fill");
        if(this.col <= s.getCol() + s.getWidth()/Tile.TILE_SIZE && this.col > s.getCol() && this.row >= s.getRow()-5 && this.row <= s.getRow()-1){
            System.out.println("filling");
            this.volume = this.maxVolume;
            this.health = this.maxHealth;
        }
    }

    /**
     * Calls to update the required variables when the engine fires at a fortress
     */
    private void fire(Attacker target){
        this.volume -= this.damage;
        target.takeDamage(this.damage);
    }



    /***
     * Checks if any of the corners of the engines range are in the body of the fortress or station
     * @param e Entity that is being checked
     * @return returns true if there is any overlap, false otherwise
     */
    private Boolean checkForOverlap(Entity e){
        for(int i=0; i<2; i++){
            for(int j=2; j<4; j++){
                if (rangeCorners.get(i) >= e.getCol() && rangeCorners.get(i) < e.getCol() + (e.getWidth()/Tile.TILE_SIZE)
                        && rangeCorners.get(j) >= e.getRow() && rangeCorners.get(j) < e.getRow() + (e.getHeight()/Tile.TILE_SIZE)){
                    return true;
                }
            }
        }
        return false;
    }

    /***
     * Method that will check if the Attacker is in range of the fortress and if so will damage it
     * @param f The fortress we are currently checking the bounds/range of
     */
    public void DamageFortressIfInRange(Fortress f){
        this.setRangeCorners();
        if(checkForOverlap(f)){
            if (this.volume >= this.damage){
                this.fire(f);
                GameState.bullets.add(new Bullet(this.x + 20, this.y + 10, f.x + 150, f.y + 50, true));
                GameState.bullets.add(new Bullet(this.x, this.y, f.x + 150, f.y + 50, true));
                GameState.bullets.add(new Bullet(this.x + 40, this.y + 20, f.x + 150, f.y + 50, true));
            }
        }
    }

    /**
     * Checks if engine can attack an ArrayList of aliens, it will spawn water and damage each one in range
     * @param aliens - ArrayList of aliens to see if engine can attack
     */
    public void damageAliensIfInRange(ArrayList<Alien> aliens){
        for(Alien alien: aliens) {
            if ((!alien.isDead()) && ((Maths.manDistance(this, alien) < this.range))){
                this.fire(alien);
                GameState.bullets.add(new Bullet(this.x + 20, this.y + 10, alien.x + 16, alien.y + 16, true));
            }
        }
    }
    //Getters and Setters
    public int getMaxVolume()
    {
        return this.maxVolume;
    }
    public int getVolume() { return this.volume; }
    public int getFillSpeed() { return fillSpeed; }

    public Integer getID()
    {
        return this.id;
    }

    public boolean isMoved(){return this.moved;}

    public void setMoved(boolean moved){this.moved = moved;}
    public void setVolume(int volume) { this.volume = volume; }
    public void setFillSpeed(int fillSpeed) { this.fillSpeed = fillSpeed; }




}