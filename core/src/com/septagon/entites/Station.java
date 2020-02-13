package com.septagon.entites;

/**
 * Class that will be used to define the fire stations in the game
 */

import com.badlogic.gdx.graphics.Texture;

public class Station extends Attacker
{
    /***
     *Constructor that callS Entity constructor that is used to set up all member variables
     */
    public Station(int col, int row, int width, int height, Texture texture, int health)
    {
        super(col,row,width,height,texture, health, 0, 0);
    }


}