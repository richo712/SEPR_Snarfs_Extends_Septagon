package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * A class used to test the Engine class
 */

class EngineTest {

    Engine testE = null;
    Texture testTexture = null;

    @BeforeEach //A set up function for the tests
    public void setUp() {
        //Texture testTexture = new Texture(Gdx.files.internal("images/engine1.png"));
        testE = new Engine(1,1, null, 10, 2, 4, 2, 20, 4, 01);
    }

    @Test  //A test for the Engine class initialisation
    public void testEngine() throws Exception {
        assertEquals(testE.x, 32);
        assertEquals(testE.y, 32);
        assertEquals(testE.col, 1);
        assertEquals(testE.row, 1);
        assertEquals(testE.width, 32);
        assertEquals(testE.height, 32);
        assertEquals(testE.texture, null);
        assertEquals(testE.health, 10);
        assertEquals(testE.damage, 2);
        assertEquals(testE.range, 4);
        assertEquals(testE.speed, 2);
        assertEquals(testE.getMaxVolume(), 20);
        assertEquals(testE.getID(), 01);
        assertEquals(testE.getVolume(), 20);
        assertEquals(testE.getFillSpeed(), 4);

    }

    @Test //A test for the Engine class' ifInRangeFill method
    public void testIfInRangeFill() throws Exception {
        testE.setVolume(10);
        testE.setRangeCorners();
        Station testS = new Station(1, 1, 256, 128, null, 100, 6, 3);
        testE.ifInRangeFill(testS);
        assertEquals(testE.getVolume(), 10);
    }

    @Test //A test for the Engine class' getMaxVolume method
    public void testGetMaxVolume() throws Exception {
        assertEquals(testE.getMaxVolume(), 20);
    }

    @Test //A test for the Engine class' getID method
    public void testGetID() throws Exception {
        assertEquals(testE.getID(), 01);
    }

    @Test //A test for the Engine class' isMoved method
    public void testIsMoved() throws Exception {
        assertFalse(testE.isMoved());
    }

    @Test //A test for the Engine class' setMoved method
    public void testSetMoved() throws Exception {
        testE.setMoved(true);
        assertTrue(testE.isMoved());
    }

    @Test //A test for the Engine class' getID method
    public void testGetVolume() throws Exception {
        assertEquals(testE.getVolume(), 20);
    }

    @Test //A test for the Engine class' getID method
    public void testGetFillSpeed() throws Exception {
        assertEquals(testE.getFillSpeed(), 4);
    }


    @Test //A test for the Engine class' setVolume method
    public void testSetVolume() throws Exception {
        testE.setVolume(18);
        assertEquals(testE.getVolume(), 18);
    }

    @Test //A test for the Engine class' setFillSpeed method
    public void testSetFillSpeed() throws Exception {
        testE.setFillSpeed(6);
        assertEquals(testE.getFillSpeed(), 6);
    }

}