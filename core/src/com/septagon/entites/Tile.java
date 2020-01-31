package com.septagon.entites;

/**
 * Class that will be used to keep track of the different tiles in our map and the interactions
 * between tiles
 */

import com.badlogic.gdx.graphics.Texture;

public class Tile extends Entity
{
    //Static variable that denotes the default size od all tiles in the game
    public static final int TILE_SIZE = 32;

    //Variable to keep track of if there is currently an object on the tile or not
    private boolean occupied;
    //Variable to see if the tile can be moved to by one of the engines
    private boolean movable = false;
    //Cost to move across the tile
    private float tileCost = 1.0f;

    /***
     *Constructor that sets up initial values for all member variables
     */
    public Tile(int col, int row, Texture texture,  Boolean occupied)
    {
        super(col,row,Tile.TILE_SIZE,Tile.TILE_SIZE,texture);
        this.occupied = occupied;
    }


    /***
     * Checks if the position of a mouse click is within the bounds of the tile
     * @param x The x position of the input
     * @param y The y position of the input
     * @return returns boolean indicating whether the input is in this tile or not
     */
    public boolean checkIfClickedInside(float x, float y)
    {
        if(x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height)
        {
            return true;
        }

        return false;

    }

    //Setters
    public void setMovable(boolean value) { movable = value; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }

    //Getters
    public boolean isMovable() { return movable; }
    public boolean isOccupied() { return occupied; }

    public float getTileCost(){
        return this.tileCost;
    }
}