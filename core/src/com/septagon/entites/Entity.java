package com.septagon.entites;

/**
 * Abstract class used to define all Entities within our game
 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity
{
    //Creates variables for the X and Y positions which refer to the actual world coordinates of the entity
    protected int x, y;
    //Creates variables for the col and row position which refer to the positions in the map of the entity
    protected int row, col;
    //Creates variables to store the width and height of the entity
    protected int width, height;
    //Creates variables to store the texture of the entity
    protected Texture texture;
    //boolean value for if the entity is dead
    protected boolean dead = false;

    /***
     * Constructor that sets inital values for class members based on given input
     */
    public Entity(int col, int row, int width, int height, Texture texture)
    {
        this.col = col;
        this.row = row;
        this.x = col * Tile.TILE_SIZE;
        this.y = row * Tile.TILE_SIZE;
        this.width = width;
        this.height = height;
        this.texture = texture;
    }

    /***
     * Initialise method that can be overwritten by subclasses and used to set up values for entity
     */
    public void initialise() {}

    /***
     * Update method that can be overwritten by subclasses and used to update the entity each frame of the game
     */
    public void update() {}

    /***
     * Basic render method that is used to draw the texture of the entity at its position
     * @param batch The batch which is used for rendering entities to the screen
     */
    public void render(SpriteBatch batch)
    {
        batch.draw(this.texture, this.x, this.y, this.width, this.height);
    }

    //Getters
    public int getX(){ return this.x;}
    public int getY(){ return this.y; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getWidth(){ return this.width;}
    public int getHeight(){ return this.height;}
    public Texture getTexture() { return this.texture;}
    public boolean isDead() {
        return dead;
    }

    //Setters
    public void setX(int x)
    {
        this.x = x;
        this.col = x / Tile.TILE_SIZE;
    }
    public void setY(int y)
    {
        this.y = y;
        this.row = y / Tile.TILE_SIZE;
    }
    public void setCol(int col)
    {
        this.col = col;
        this.x = col * Tile.TILE_SIZE;
    }
    public void setRow(int row)
    {
        this.row = row;
        this.y = row * Tile.TILE_SIZE;
    }
    public void setPosition(int col, int row){
        setCol(col);
        setRow(row);
    }
    public void setTexture(Texture texture){ this.texture = texture; }

    public void setDead(){
        this.dead = true;
    }
}