package com.septagon.entites;

/**
 * Class that will be used to define all vehicles within the game
 */

import com.badlogic.gdx.graphics.Texture;

public class Vehicle extends Attacker
{
    protected int speed;
    protected char direction;

    /**
     * Constructor that calls Entity constructor that is used to set up all member variables
     * @param col Starting column
     * @param row Starting row
     * @param texture Entity texture
     * @param health Maximum vehicle health
     * @param damage Damage dealt to targets
     * @param range Attack range in tiles
     * @param speed Movement range in tiles
     */
    public Vehicle (int col, int row, Texture texture, int health, int damage, int range, int speed)
    {
        super(col,row, Tile.TILE_SIZE, Tile.TILE_SIZE, texture,health,damage,range);
        this.speed = speed;
    }

    //Getters
    public int getSpeed() { return speed; }
    public char getDirection() { return direction; }
}
