package com.septagon.minigame;

import com.septagon.entites.Entity;
import com.septagon.helperClasses.Maths;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MathsTest {

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
}
