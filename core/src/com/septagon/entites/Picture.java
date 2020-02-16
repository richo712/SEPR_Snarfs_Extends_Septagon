package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;

/**
 * This Class is new for Assessment 3
 * Bare-bones implementation of entity,
 * used to render pictures.
 */
public class Picture extends Entity {

    /***
     * Constructor that sets initial values for class members based on given input.
     * @param x X coordinate of bottom left hand corner.
     * @param y Y coordinate of bottom left hand .
     * @param width Width of picture.
     * @param height Height of picture.
     * @param texture Texture of image.
     */
    public Picture(int x, int y, int width, int height, Texture texture) {
        super(x, y, width, height, texture);
        this.setX(x);
        this.setY(y);
    }
}
