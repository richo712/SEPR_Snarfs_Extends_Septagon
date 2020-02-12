package com.septagon.helperClasses;

/**
 * Class used to load in all the textures to the game so that they only have to loaded once and
 *  can be accessed anywhere in the program (since all the textures are static)
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AssetManager {

    private static final Texture engineTexture1 = new Texture(Gdx.files.internal("images/engine1.png"));
    private static final Texture engineTexture2 = new Texture(Gdx.files.internal("images/engine2.png"));
    private static final Texture moveSpaceTexture = new Texture(Gdx.files.internal("move_square.png"));

    private static final Texture fortressFireTexture = new Texture(Gdx.files.internal("images/FortressFire.png"));
    private static final Texture fortressMinisterTexture = new Texture(Gdx.files.internal("images/FortressMinister.png"));
    private static final Texture fortressStationTexture = new Texture(Gdx.files.internal("images/FortressStation.png"));

    private static final Texture defeatedFireTexture = new Texture(Gdx.files.internal("images/DefeatedOldStation.png"));
    private static final Texture defeatedMinsterTexture = new Texture(Gdx.files.internal("images/DefeatedMinster.png"));
    private static final Texture defeatedStationTexture = new Texture(Gdx.files.internal("images/DefeatedRailStation.png"));

    private static final Texture fireStationTexture = new Texture(Gdx.files.internal("images/fireStation.png"));

    private static final Texture fortressBoundaryImage = new Texture(Gdx.files.internal("selected fortress.png"));

    private static final Texture alienAliveTexture = new Texture(Gdx.files.internal("images/Alien-1.png.png"));
    private static final Texture alienDeadTexture = new Texture(Gdx.files.internal("images/Alien Dead-1.png.png"));
    //Minigame stuff
    private static final Texture waterBalloon = new Texture(Gdx.files.internal("TEMPWaterBalloon.png"));

    //Getters
    public static Texture getEngineTexture1() {
        return engineTexture1;
    }

    public static Texture getEngineTexture2() {
        return engineTexture2;
    }

    public static Texture getMoveSpaceTexture() {
        return moveSpaceTexture;
    }

    public static Texture getFortressFireTexture() {
        return fortressFireTexture;
    }

    public static Texture getFortressStationTexture() {
        return fortressStationTexture;
    }

    public static Texture getFortressMinisterTexture() {
        return fortressMinisterTexture;
    }

    public static Texture getFireStationTexture() {
        return fireStationTexture;
    }

    public static Texture getFortressBoundaryImage() {
        return fortressBoundaryImage;
    }

    public static Texture getDefeatedFireTexture() {
        return defeatedFireTexture;
    }

    public static Texture getDefeatedMinsterTexture(){ return  defeatedMinsterTexture; }

    public static  Texture getDefeatedStationTexture() { return defeatedStationTexture; }

    public static Texture getAlienAliveTexture() { return alienAliveTexture; }

    public static Texture getAlienDeadTexture() { return alienDeadTexture; }

    public static Texture getWaterBalloonTexture(){ return waterBalloon; }

}
