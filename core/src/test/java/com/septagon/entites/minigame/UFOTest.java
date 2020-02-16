package com.septagon.entites.minigame;

import com.badlogic.gdx.graphics.Texture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UFOTest {
    /**
     * ASSESSMENT_3
     */
    UFO testufo = null;
    Texture testure = null;

    @BeforeEach
    public void setUp() {
        testufo = new UFO(1, 1, null, 32, 0.5f);
    }

    @Test
    public void UFOtest() {
        assertEquals(testufo.getxActual(), 1);
        assertEquals(testufo.getyActual(), 1);
        assertEquals(testufo.getSpeed(), 0.5f);
    }


    @Test
    public void setxActualTest() {
        testufo.setxActual(10f);
        assertEquals(testufo.getxActual(),10f);
        testufo.setxActual(90.9f);
        assertEquals(testufo.getxActual(), 90.9f);

    }

    @Test
    public void setyActualTest() {
        testufo.setyActual(5f);
        assertEquals(testufo.getyActual(),5f);
        testufo.setyActual(50.5f);
        assertEquals(testufo.getyActual(),50.5f);
    }

    @Test
    public void moveTest(){
        testufo.move(UFO.Direction.RIGHT);
        assertEquals(testufo.getxActual(), 1.5f);
        testufo.setxActual(50f);
        testufo.move(UFO.Direction.LEFT);
        assertEquals(testufo.getxActual(), 49.5f);
        testufo.setyActual(100f);
        testufo.setxActual(100f);
    }
}