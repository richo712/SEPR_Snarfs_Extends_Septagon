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
        assertEquals(testufo.xActual, 1);

    }

    @Test
    public void isCollidingWith() {
    }

    @Test
    public void setxActual() {
        testufo.setxActual(1f);
        assertEquals(1f,1f);
    }

    @Test
    public void setyActual() {
        testufo.setyActual(1f);
        assertEquals(1f,1f);
    }
}