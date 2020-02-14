package com.septagon.entites;

/**
 * Class that will be used to define the fire stations in the game
 */

import com.badlogic.gdx.graphics.Texture;

public class Station extends Attacker
{

    private int xBound, yBound;

    /**
     * Constructor that calls Entity constructor that is used to set up all member variables
     * @param col Starting column
     * @param row Starting row
     * @param width Entity width
     * @param height Entity height
     * @param texture Entity texture
     * @param health Maximum health
     * @param xBound Width of object in tiles for blocking
     * @param yBound Height of object in tiles for blocking
     */
    public Station(int col, int row, int width, int height, Texture texture, int health, int xBound, int yBound)
    {
        super(col,row,width,height,texture, health, 0, 0);
        this.xBound = xBound;
        this.yBound = yBound;
    }



    //Getters

    public int getxBound() { return xBound; }

    public int getyBound() { return yBound; }

}