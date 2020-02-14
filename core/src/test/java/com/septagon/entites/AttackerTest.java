package com.septagon.entites;

import com.badlogic.gdx.graphics.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import  static org.junit.jupiter.api.Assertions.*;

/*
 * A class used to test the Attacker class
 * Uses the concrete version of the Attacker class to enable testing
 */

class AttackerTest {

    ConcreteAttacker testCA = null;
    Texture testTexture = null;

    @BeforeEach //A set up function for the tests
    public void setUp() {
        //Texture testTexture = new Texture(Gdx.files.internal("images/engine1.png"));
        testCA = new ConcreteAttacker(1, 1, 32, 32, null, 10, 2, 4);
    }

    @Test //A test for the Attacker class initialisation
    public void testAttacker() throws Exception {
        assertEquals(testCA.col, 1);
        assertEquals(testCA.row, 1);
        assertEquals(testCA.x, 32);
        assertEquals(testCA.y, 32);
        assertEquals(testCA.width, 32);
        assertEquals(testCA.height, 32);
        assertEquals(testCA.texture, null);
        assertEquals(testCA.health, 10);
        assertEquals(testCA.damage, 2);
        assertEquals(testCA.range, 4);
    }

    @Test //A test for the Attacker class' damageFortressIfInRange method
    public void testCheckForOverlap() throws Exception {
        //Texture testTexture = new Texture(Gdx.files.internal("images/engine1.png"));
        //Texture testTexture2 = new Texture(Gdx.files.internal("images/FortressMinister.png"));
        Fortress testF1 = new Fortress(2, 2, 256, 256, null, null,100, 20, 3, 6, 4);
        Fortress testF2 = new Fortress(10, 10, 256, 256, null, null, 100, 20, 3, 5, 5);
        testCA.setRangeCorners();
    }

    @Test //A test for the Attacker class' getHealth method
    public void testGetHealth() throws Exception {
        assertEquals(testCA.getHealth(), 10);
    }

    @Test //A test for the Attacker class' takeDamage method
    public void testTakeDamage() throws Exception {
        testCA.takeDamage(6);
        assertEquals(testCA.health, 4);
    }

    @Test //A test for the Attacker class' getDamage method
    public void testGetDamage() throws Exception {
        assertEquals(testCA.getDamage(), 2);
    }

    @Test //A test for the Attacker class' setDamage method
    public void testSetDamage() throws Exception {
        testCA.setDamage(3);
        assertEquals(testCA.damage, 3);
    }

    @Test //A test for the Attacker class' getRange method
    public void testGetRange() throws Exception {
        assertEquals(testCA.getRange(), 4);
    }

    @Test //A test for the Attacker class' setRange method
    public void testSetRange() throws Exception {
        testCA.setRange(3);
        assertEquals(testCA.range, 3);
    }

    @Test //A test for the Attacker class' getRangeCorners method
    public void testGetRangeCorners() throws Exception {
        testCA.setRangeCorners();
        assertNotNull(testCA.getRangeCorners());
    }

    @Test //A test for the Attacker class' setMaxHealth method
    public void testSetMaxHealth() throws Exception {
        testCA.setMaxHealth(15);
        assertEquals(testCA.maxHealth, 15);
    }

    @Test //A test for the Attacker class' getMaxHealth method
    public void testGetMaxHealth() throws Exception {
        assertEquals(testCA.getMaxHealth(), 10);
    }

}

