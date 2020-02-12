package com.septagon.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.entites.Engine;
import com.septagon.game.InputManager;
import com.septagon.helperClasses.AssetManager;
import com.septagon.minigame.UFO;
import com.septagon.minigame.WaterBalloon;

import java.util.ArrayList;

/**
Child of State class that will be used to manage the system when the user is playing the minigame
 */

public class MinigameState extends State 
{

    private OrthographicCamera minigameCamera;
    private SpriteBatch minigameBatch;

    //Engine the player controls
    private Engine engine;
    private ArrayList<WaterBalloon> waterBalloons;
    private ArrayList<UFO> ufos;

    //Pointers to a UFO that is the furthest to the right, left, and lowest
    //(Used to detect when UFOs have hit the side of the screen, and should move in the other direction)
    private UFO rightmostUFO, leftmostUFO, lowestUFO;
    //Keeps track of the current direction the UFOs are moving
    private UFO.Direction currentDirection = UFO.Direction.RIGHT;

    //The speed the UFOs move, applies to all directions
    private float ufoSpeed = 0.8f;
    //How far down the UFOs should travel when they hit a wall, before moving to the other side of the screen
    private float ufoDownstepAmount = 50f;
    //Keeps track of how far down the UFOs have traveled this step
    private float ufoDownstepCounter = 0f;
    //How many frames shold the player have to wait before firing a water balloon again
    private final int FIRECOOLDOWN = 80;
    //Keeps track of how many frames until a water balloon can be fired again
    private int fireCooldownCounter = 0;

    public MinigameState(InputManager inputManager, BitmapFont font, StateManager stateManager, OrthographicCamera camera)
    {
        super(inputManager, font, StateID.MINIGAME, stateManager);
        this.minigameCamera = camera;
    }

    public void initialise()
    {
        this.minigameBatch = new SpriteBatch();
        this.minigameCamera = new OrthographicCamera();
        this.minigameCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.minigameCamera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        this.minigameCamera.update();
        this.engine = new Engine(0,0, AssetManager.getEngineTexture1(), 100, 15, 4, 16, 100, 4, 01);
        this.ufos = new ArrayList<>();
        this.waterBalloons = new ArrayList<>();
        this.setUpUfos();
    }

    /**
     * Called during initialisation to create new UFO objects,
     * and set their positions.
     */
    private void setUpUfos(){
        for (int y = 600; y < 701; y+=100){
            for (int x = 0; x < 500; x+=70) {
                this.ufos.add(new UFO(x * 2, y, AssetManager.getAlienAliveTexture(), 60, this.ufoSpeed));
            }
        }
        this.lowestUFO = this.ufos.get(0);
        this.rightmostUFO = this.ufos.get(0);
        this.leftmostUFO = this.ufos.get(0);
    }

    /**
     * Called every time the fire button is pressed. Will create water balloon
     * object if enough time has passed since the last one was fired.
     */
    public void fire(){
        if (this.fireCooldownCounter <= 0){
            this.fireCooldownCounter = this.FIRECOOLDOWN;
            this.waterBalloons.add(new WaterBalloon(engine.getX(), engine.getY(), AssetManager.getWaterBalloonTexture(), 2));
        }
    }

    /**
     * Called when the player pressed move left or move right, moves the position of the engine
     * and the starting position where water balloon objects will be created.
     * @param direction Direction to move the engine, +1 will move to the right, -1 will move the the left
     */
    public void moveEngine(int direction){
        this.engine.setX(engine.getX() + 2 * direction);
    }

    /**
     * Checks if any water balloons are colliding with any ufos, if so both
     * are removed from the arrays containing them. If a water balloon is
     * touching two UFOs, only one UFO will be removed.
     *
     * @param waterBalloons An array of WaterBalloon objects, will remove any colliding with any UFOs
     * @param ufos An array of UFO objects, will remove any colliding with any Water Balloons
     */
    public void checkIfWaterHit(ArrayList<WaterBalloon> waterBalloons, ArrayList<UFO> ufos) {
        ArrayList<WaterBalloon> waterToRemove = new ArrayList<>();
        ArrayList<UFO> ufosToRemove = new ArrayList<>();

        for (WaterBalloon waterBalloon : waterBalloons) {
            //If the water is too high up, delete it
            if (waterBalloon.isOutOfBounds(Gdx.graphics.getHeight() + 500)){
                waterToRemove.add(waterBalloon);
            } else { //Otherwise check if it is colliding with a UFO
                for (UFO ufo : ufos) {
                    if (ufo.isCollidingWith(waterBalloon)) { //If so, mark both water and UFO to be removed
                        waterToRemove.add(waterBalloon);
                        ufosToRemove.add(ufo);
                        break;
                    }
                }
            }
        }

        for (WaterBalloon waterBalloon: waterToRemove){
            this.waterBalloons.remove(waterBalloon);
        }
        for (UFO ufo: ufosToRemove){
            ufos.remove(ufo);
        }
    }

    /**
     * Called every frame, updates game logic and checks if the minigame is over because of a win or
     * lose condition. The state will be changed back to the main game, or to the game over state respectively.
     */
    public void update() {
        if (this.minigameNotWon() && this.minigameNotLost()) {

            this.fireCooldownCounter -= 1;

            this.leftmostUFO = this.ufos.get(0);
            this.rightmostUFO = this.ufos.get(0);
            this.lowestUFO = this.ufos.get(0);

            for (UFO ufo : this.ufos) {
                if (ufo.getX() < this.leftmostUFO.getX()) {
                    this.leftmostUFO = ufo;
                }
                if (ufo.getX() > this.rightmostUFO.getX()) {
                    this.rightmostUFO = ufo;
                }
                if (ufo.getY() < this.lowestUFO.getY()) {
                    this.lowestUFO = ufo;
                }
            }

            if (leftmostUFO.getX() < -0 && this.currentDirection == UFO.Direction.LEFT) {
                this.ufoDownstepCounter = this.ufoDownstepAmount;
                this.currentDirection = UFO.Direction.RIGHT;
            } else if (rightmostUFO.getX() > Gdx.graphics.getWidth() - 60 && this.currentDirection == UFO.Direction.RIGHT) {
                this.ufoDownstepCounter = this.ufoDownstepAmount;
                this.currentDirection = UFO.Direction.LEFT;
            }
            if (this.ufoDownstepCounter < 0) {
                for (UFO ufo : this.ufos) {
                    ufo.move(this.currentDirection);
                }
            } else {
                this.ufoDownstepCounter -= this.ufoSpeed;
                for (UFO ufo : this.ufos) {
                    ufo.move(UFO.Direction.DOWN);
                }
            }

            if (InputManager.moveRight) {
                this.moveEngine(1);
            }
            if (InputManager.moveLeft) {
                this.moveEngine(-1);
            }
            for (WaterBalloon balloon : this.waterBalloons) {
                balloon.move();
            }
            this.checkIfWaterHit(this.waterBalloons, this.ufos);
        }
    }

    /**
     * Called every frame to render the objects on the screen.
     * @param batch SpriteBatch containing sprites to render
     */
    public void render(SpriteBatch batch)
    {
        Gdx.gl.glClearColor((float) 43/255, (float) 47/255, (float) 119/255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.update();
        this.minigameBatch.begin();

        this.engine.render(minigameBatch);

        for (UFO ufo : this.ufos){
            ufo.render(minigameBatch);
        }
        for (WaterBalloon water: this.waterBalloons){
            water.render(minigameBatch);
        }
        this.minigameBatch.end();
    }

    /**
     * Checks if the game has been won or not, if so switches state back to main game.
     * @return True if no UFOs are left, false otherwise
     */
    public boolean minigameNotWon(){ // TODO: should delete minigame state when done with it
        if (this.ufos.size() == 0){
            this.stateManager.changeToExistingState(StateID.GAME);
            return false;
        }
        return true;
    }

    /**
     * Checks if the minigame has been lost, if so switches state to game over state.
     * @return True if the UFOs are too low, false otherwise
     */
    public boolean minigameNotLost(){
        if (this.lowestUFO.getY() < 0){
            this.stateManager.changeState(new GameOverState(this.inputManager, this.font, this.stateManager, false));
            return false;
        }
        return true;
    }

    public void dispose(){}

}
