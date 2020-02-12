package com.septagon.minigame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WaterBalloonTest {

    WaterBalloon wat = null;

    @BeforeEach
    public void setUp() {
        wat = new WaterBalloon(1,1, null, 1);
    }

    @Test
    public void waterBalloonTest(){
        assertEquals(wat.getX(), 1);
        assertEquals(wat.getY(), 1);
        assertEquals(wat.getSpeed(), 1);
        assertEquals(wat.getTexture(), null);
    }

    @Test
    public void moveTest(){
        wat.move();
        assertEquals(wat.getY(), 2);
        assertEquals(wat.getX(), 1);
        wat.setY(-100);
        wat.move();
        assertEquals(wat.getY(), -99);
        assertEquals(wat.getX(), 1);
    }

    @Test
    public void isNotOutOfBoundsTest(){
        wat.setY(99);
        assertFalse(wat.isOutOfBounds(100));
        wat.setX(150);
        assertFalse(wat.isOutOfBounds(100));
        wat.setY(-150);
        assertFalse(wat.isOutOfBounds(100));
    }

    @Test
    public void isOutOfBoundsTest(){
        wat.setY(20);
        assertTrue(wat.isOutOfBounds(10));
        wat.setY(-1);
        assertTrue(wat.isOutOfBounds(-100));
    }
}
