package com.septagon.entites.minigame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WaterBalloonTest {

    /**
     * ASSESSMENT_3
     */

    WaterBalloon wat = null;

    @BeforeEach
    public void setUp() {
        wat = new WaterBalloon(1,1, null, 45, 1f);
    }

    @Test
    public void waterBalloonTest(){
        assertEquals(wat.getX(), 1);
        assertEquals(wat.getY(), 1);
        assertEquals(wat.getSpeed(), 1f);
        assertEquals(wat.getTexture(), null);
    }

    @Test
    public void moveTest(){
        wat.move();
        assertEquals(wat.getY(), 2);
        assertEquals(wat.getX(), 1);
        wat.setyActual(-100);
        wat.move();
        assertEquals(wat.getY(), -99);
        assertEquals(wat.getX(), 1);
    }

    @Test
    public void isNotOutOfBoundsTest(){
        wat.setyActual(99);
        assertFalse(wat.isOutOfBounds(100));
        wat.setxActual(150);
        assertFalse(wat.isOutOfBounds(100));
        wat.setyActual(-150);
        assertFalse(wat.isOutOfBounds(100));
    }

    @Test
    public void isOutOfBoundsTest(){
        wat.setyActual(20);
        assertTrue(wat.isOutOfBounds(10));
        wat.setxActual(-1);
        assertTrue(wat.isOutOfBounds(-100));
    }
}
