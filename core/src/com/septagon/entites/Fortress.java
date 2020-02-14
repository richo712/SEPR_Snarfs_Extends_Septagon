package com.septagon.entites;

/**
 * Class that will be used to define all the fortresses in the game
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.helperClasses.AssetManager;
import com.septagon.states.GameState;
import com.septagon.states.State;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Fortress extends Attacker
{
    //Stores if an engine is currently active/pressed on
    private boolean selected = false;
    private Texture defeatedTexture;
    private int xBound,yBound;

    /**
     * Constructor that calls the Entity constructor to set up all the member variables
     * @param col Starting column
     * @param row Starting row
     * @param width Entity width
     * @param height Entity height
     * @param texture Initial sprite texture
     * @param defeatedTexture Sprite texture when flooded
     * @param health Maximum / starting health
     * @param damage Damage dealt to targets
     * @param range Attack range in tiles
     * @param xBound Width of object in tiles for blocking
     * @param yBound Height of object in tiles for blocking
     */
    public Fortress(int col, int row, int width, int height, Texture texture, Texture defeatedTexture, int health, int damage, int range, int xBound, int yBound)
    {
        super(col,row, width, height, texture, health, damage, range);
        this.defeatedTexture = defeatedTexture;
        this.xBound = xBound;
        this.yBound = yBound;
    }


    /***
     * Initialise method that is used to init all variables in the class and set up everything
     * Overwritten from Attacker
     */
    public void initialise()
    {
        super.initialise();
        setRangeCorners();
    }

    /***
     * Method that will be called if an engine is in range of the fortress so that the engine can be damaged
     * @param e The current engine that is being checked
     */
    public void DamageEngineIfInRange(Engine e) {
        System.out.println("Checking if should damage engine");
        if (e.getCol() >= this.rangeCorners.get(0) && e.getCol() < this.rangeCorners.get(1) && e.getRow() >= this.rangeCorners.get(2) && e.getRow() < this.rangeCorners.get(3)){
            e.takeDamage(this.damage);
            GameState.bullets.add(new Bullet(this.x + 150, this.y + 50, e.getX() + 20, e.getY() + 10, false));
            GameState.bullets.add(new Bullet(this.x + 100, this.y + 25, e.getX() + 20, e.getY() + 10, false));
            GameState.bullets.add(new Bullet(this.x + 200, this.y + 75, e.getX() + 20, e.getY() + 10, false));
        }
    }

    /***
     * Method that is used to draw the fortress on the screen
     * Overwritten from Attacker
     * @param batch The batch that is used to display all objects on the screen
     */
    public void render(SpriteBatch batch)
    {
        //If the fortress is pressed, show its boundary image
        if(selected && !dead)
        {
            batch.draw(AssetManager.getFortressBoundaryImage(), (col - this.getRange()) * Tile.TILE_SIZE, (row - this.getRange()) * Tile.TILE_SIZE,
                    (((int)width / Tile.TILE_SIZE) + range * 2) * Tile.TILE_SIZE, (((int)height / Tile.TILE_SIZE) + range * 2) * Tile.TILE_SIZE);
        }else if(dead){
            this.texture = this.defeatedTexture;
    }


        super.render(batch);
    }

    /**
     * Method used to improve the stats of fortresses when called from GameState
     * Improves a random stat (all equal chance) between Health, Damage, and Range
     */
    public void improve(){
        if(!isDead()) {
            int statToImprove = new Random().nextInt(3);
            switch (statToImprove) {
                case (0): //health
                    int hpImprovement = (int) Math.ceil(this.maxHealth * 0.2);
                    this.maxHealth += hpImprovement;
                    this.health += hpImprovement;
                    break;
                case (1): //damage
                    int dmgImprovement = (int) Math.ceil(this.damage * 0.2);
                    this.damage += dmgImprovement;
                    break;
                case (2): //range
                    this.range++;
                    break;
            }
        }
    }

    //Getters
    public boolean isSelected() { return selected; }

    public int getxBound() { return xBound; }

    public int getyBound() { return yBound; }

    //Setters
    public void setSelected(boolean selected) { this.selected = selected; }

}