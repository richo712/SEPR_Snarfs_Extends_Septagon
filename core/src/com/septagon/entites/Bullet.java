package com.septagon.entites;

/**
 * Class that is used to create animations when the engines and fortresses are attacking each other
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {

    private double ySPEED = 500, xSPEED = 500;
    public Texture texture;
    private double deltaY, deltaX;
    float x, y, targetX, targetY;
    public boolean remove = false;

    /**
     * Constructor for setting up new Bullets
     * @param attackerX X coordinate of the attacker
     * @param attackerY Y coordinate of the attacker
     * @param targetX X coordinate of the target
     * @param targetY Y coordinate of the target
     * @param water True if Fire Engines are attacking, false otherwise
     */
    public Bullet (float attackerX, float attackerY, float targetX, float targetY, boolean water) {
        this.x = attackerX;
        this.y = attackerY;
        this.targetX = targetX;
        this.targetY = targetY;
        deltaY = this.targetY - this.y;
        deltaX = this.targetX - this.x;

        //calculate relative speed in both x and y directions in order to move from attacker to target
        ySPEED = deltaY / (deltaX*deltaX + deltaY*deltaY);
        xSPEED = deltaX / (deltaX*deltaX + deltaY*deltaY);

        if (texture == null) {
            if (water == true) texture = new Texture("water.png");
            else texture = new Texture("gunge.png");
        }
    }

    /**
     * Move bullets in required directions
     */
    public void update (float deltaTime) {
        y += this.ySPEED * deltaTime * 25000;
        x += this.xSPEED * deltaTime * 25000;
        if ((deltaX* (targetX - x) < 0)&&(deltaY* (targetY - y) < 0))
            remove = true;
    }

    /**
     * Draws the bullet to the screen
     * @param batch The batch which is used for drawing objects to the screen
     */
    public void render (SpriteBatch batch){
        batch.draw(texture, x, y);
    }
}
