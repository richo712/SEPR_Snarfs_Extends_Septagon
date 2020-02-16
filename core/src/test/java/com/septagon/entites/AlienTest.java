package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import com.septagon.helperClasses.AssetManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/*
 * A class used to test the Alien class
 */

class AlienTest {

    Alien alien = null;
    int[][] testPath = new int[][]{{0,0},{1,1},{-1,-1}};

    @BeforeEach //A set up function for the tests
    public void setUp() {
        //Texture testTexture = new Texture(Gdx.files.internal("images/engine1.png"));
        alien = new Alien(1, 2, 32, 32, null, 20, 5, 4, 6, 7, this.testPath, 1);
    }

    @Test  //A test for the Alien class initialisation
    public void testAlien() throws Exception {
        assertEquals(alien.x, 32);
        assertEquals(alien.y, 64);
        assertEquals(alien.col, 1);
        assertEquals(alien.row, 2);
        assertEquals(alien.width, 32);
        assertEquals(alien.height, 32);
        assertEquals(alien.texture, null);
        assertEquals(alien.health, 20);
        assertEquals(alien.damage, 5);
        assertEquals(alien.range, 4);
        assertEquals(alien.getSpeed(), 6);
        assertEquals(alien.getVision(), 7);
    }

    @Test //A test to check the pathing of the Alien
    public void testAlienPathFix() throws Exception{
        assertEquals(alien.fixCoord(-1), 0);
        assertEquals(alien.fixCoord(0), 0);
        assertEquals(alien.fixCoord(125), 125);
        assertEquals(alien.fixCoord(199), 199);
        assertEquals(alien.fixCoord(200), 199);
        assertEquals(alien.fixCoord(501241412), 199);
    }

    @Test //A test to check Fire Engines are correctly targeted, and then correctly attacked
    public void testTargetEngine1() throws Exception {
        Engine testEngine = new Engine(1, 1, null, 1, 5, 5, 5, 5, 5, 1);
        ArrayList<Engine> engines = new ArrayList<Engine>();
        engines.add(testEngine);
        alien.findTargetEngine(engines);
        assertEquals(alien.getTargetEngine().getID(), 1);
    }

    @Test //A second test to check that no Fire Engine is targeted if it is too far away, and then correctly not attacked
    public void testTargetEngine2() throws Exception{
        Engine testEngine = new Engine(50, 50, null, 12, 12, 12, 12, 12, 12, 2);
        ArrayList<Engine> engines = new ArrayList<Engine>();
        engines.add(testEngine);
        alien.findTargetEngine(engines);
        assertEquals(alien.getTargetEngine(), null);
    }


}

