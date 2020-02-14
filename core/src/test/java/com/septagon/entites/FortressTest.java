package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * A class used to test the Fortress class
 */

class FortressTest {

    Fortress testF = null;
    Texture testTexture = null;

    @BeforeEach //A set up function for the tests
    public void setUp() {
        //Texture testTexture = new Texture(Gdx.files.internal("images/FortressMinister.png"));
        testF = new Fortress(4, 10, 256, 256, null, null, 100, 20, 3, 5, 4);
    }

    @Test //A test for the Fortress class initialisation
    public void testFortress() throws Exception {
        assertEquals(testF.x, 128);
        assertEquals(testF.y, 320);
        assertEquals(testF.col, 4);
        assertEquals(testF.row, 10);
        assertEquals(testF.width, 256);
        assertEquals(testF.height, 256);
        assertEquals(testF.texture, null);
        assertEquals(testF.health, 100);
        assertEquals(testF.damage, 20);
        assertEquals(testF.range, 3);
    }

    @Test //A test for the Fortress class' damageEngineIfInRange method
    public void testDamageEngineIfInRange() throws Exception {
        testF.setRangeCorners();
        //Texture testTexture2 = new Texture(Gdx.files.internal("images/engine1.png"));
        Engine testE1 = new Engine(5,9, null, 100, 2, 4, 2, 20, 4, 01);
        Engine testE2 = new Engine(3, 3, null, 10, 2, 4, 2, 20, 4, 01);
        //testF.DamageEngineIfInRange(testE1);
        //testF.DamageEngineIfInRange(testE2);
        assertEquals(testE1.health, 100);
        assertEquals(testE2.health, 10);

    }

    @Test //A test for the Fortress class' isSelected method
    public void testIsSelected() throws Exception {
        assertFalse(testF.isSelected());
    }

    @Test //A test for the Fortress class' setSelected method
    public void testSetSelected() throws Exception {
        testF.setSelected(true);
        assertTrue(testF.isSelected());
    }
}