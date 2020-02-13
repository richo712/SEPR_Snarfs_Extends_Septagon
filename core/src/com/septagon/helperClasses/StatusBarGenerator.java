package com.septagon.helperClasses;

/**
 * Class used to render all the health bars and the water meter bars for all entites
 * that require them
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.septagon.entites.*;

import java.util.ArrayList;

public class StatusBarGenerator
{
    //Renderer that is used to draw all the shapes of the bars
    private ShapeRenderer barRenderer;

    private ArrayList<Engine> engines;
    private ArrayList<Fortress> fortresses;
    private ArrayList<Alien> aliens;
    private Station station;

    public StatusBarGenerator(ArrayList<Engine> engines, ArrayList<Fortress> fortresses, ArrayList<Alien> aliens, Station stations){
        this.engines = engines;
        this.fortresses = fortresses;
        this.aliens = aliens;
        this.station = stations;
        barRenderer = new ShapeRenderer();
    }

    /***
     * Method that will render the health bars for all the fortresses and engines in the game
     */
    public void renderBars(OrthographicCamera camera) {
        barRenderer.setProjectionMatrix(camera.combined);

        //Render the health bar for all entities in the game
        for(Engine e: this.engines){
            renderHealthBarForAttacker(e);
            renderWaterBarForEngine(e);
        }
        for(Fortress f: this.fortresses){
            renderHealthBarForAttacker(f);
        }
        for(Alien a: this.aliens){
            renderHealthBarForAttacker(a);
        }
        renderHealthBarForAttacker(this.station);
    }

    /**
     * Method called for each attacker which will render its health bar just above it
     * @param a The Attacker which the health bar is being rendered for
     */
    private void renderHealthBarForAttacker(Attacker a){
        barRenderer.begin(ShapeRenderer.ShapeType.Filled);

        barRenderer.setColor(169.0f/255.0f, 169.0f/255.0f, 169.0f/255.0f, 1);
        barRenderer.rect(a.getX() - 2, a.getY() + a.getHeight(), a.getWidth() + 4, 9);

        //Work out whether the current health meter should show in red, yellow or green depending on health value
        int healthBoundary1 = a.getMaxHealth() / 2;
        int healthBoundary2 = a.getMaxHealth() / 4;

        if(a.getHealth() >= healthBoundary1){
            barRenderer.setColor(Color.GREEN);
        }else if(a.getHealth() >= healthBoundary2){
            barRenderer.setColor(Color.YELLOW);
        }else{
            barRenderer.setColor(Color.RED);
        }

        //Works out the size of the health bar and renderers it to the screen
        float healthBarLength = ((float)a.getWidth() / (float)a.getMaxHealth()) * a.getHealth();
        barRenderer.rect(a.getX(), a.getY() + a.getHeight() + 2, healthBarLength, 5);

        barRenderer.end();
    }

    /**
     * Method that is used to render the water meter for each engine just underneath the engine
     * @param e The engine which the health bar is being rendered for
     */
    private void renderWaterBarForEngine(Engine e){
        barRenderer.begin(ShapeRenderer.ShapeType.Filled);

        barRenderer.setColor(169.0f/255.0f, 169.0f/255.0f, 169.0f/255.0f, 1);
        barRenderer.rect(e.getX() - 2, e.getY() - 9, e.getWidth() + 4, 9);

        barRenderer.setColor(0.0f, 167.0f/255.0f, 190.0f/255.0f, 1.0f);

        float waterBarLength = ((float)e.getWidth() / (float)e.getMaxVolume()) * e.getVolume();
        barRenderer.rect(e.getX(), e.getY() - 7, waterBarLength, 5);

        barRenderer.end();
    }

    /**
     * Method useed to clean up the barRenderer
     */
    public void dispose(){
        barRenderer.dispose();
    }
}
