package com.septagon.entites.minigame;

import com.septagon.entites.Engine;
import com.septagon.entites.Entity;
import com.septagon.helperClasses.Maths;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MathsTest {
    /**
     * ASSESSMENT_3
     */
    @Test
    public void isCollidingTest(){
        Entity e1 = new WaterBalloon(0,0,null,10, 0);
        Entity e2 = new UFO(5,0,null,10, 0);
        assertTrue(Maths.isColliding(e1,e2));
        e2.setX(-5);
        assertTrue(Maths.isColliding(e1,e2));
        e2.setX(0);
        e1.setY(5);
        assertTrue(Maths.isColliding(e1,e2));
        e1.setY(-5);
        assertTrue(Maths.isColliding(e1,e2));
        e1.setY(0);
        e2.setX(9);
        e2.setY(9);
        assertTrue(Maths.isColliding(e1,e2));
    }

    @Test
    public void isNotCollidingTest(){
        Entity e1 = new WaterBalloon(0,0,null,10, 0);
        Entity e2 = new UFO(11,0,null,10, 0);
        assertFalse(Maths.isColliding(e1,e2));
        e2.setX(-11);
        assertFalse(Maths.isColliding(e1,e2));
        e2.setX(0);
        e1.setY(11);
        assertFalse(Maths.isColliding(e1,e2));
        e1.setY(-11);
        assertFalse(Maths.isColliding(e1,e2));
        e1.setX(11);
        assertFalse(Maths.isColliding(e1,e2));
    }

    @Test
    public void manDistanceTest(){
        assertEquals(0, Maths.manDistance(0,0,0,0));
        assertEquals(15, Maths.manDistance(0,0,0,15));
        assertEquals(15, Maths.manDistance(0,0,15,0));
        assertEquals(10, Maths.manDistance(0,0,5,5));
        assertEquals(10, Maths.manDistance(-10,0,0,0));
        assertEquals(10, Maths.manDistance(0,-10,0,0));
        Engine e1 = new Engine(0, 0, null, 1, 1, 1, 1, 1, 1, 1);
        Engine e2 = new Engine(3, 3, null, 1, 1, 1, 1, 1, 1, 1);
        assertEquals(6, Maths.manDistance(e1, e2));
    }
}
