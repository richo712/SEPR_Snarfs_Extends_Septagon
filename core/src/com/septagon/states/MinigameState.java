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
    //Used to keep track of the score in the minigame
    private int score;
    private OrthographicCamera minigameCamera;
    private SpriteBatch minigameBatch;

    private Engine engine;
    private ArrayList<WaterBalloon> waterBalloons;
    private ArrayList<UFO> ufos;
    private float ufoSpeed = 0.5f;
    private UFO rightmostUFO, leftmostUFO, lowestUFO;
    private UFO.Direction currentDirection= UFO.Direction.RIGHT;
    private float ufoDownstepAmount = 50f;
    private float ufoDownstepCounter = 0f;
    private int fireCooldown = 0;

    public MinigameState(InputManager inputManager, BitmapFont font, StateManager stateManager, OrthographicCamera camera)
    {
        super(inputManager, font, StateID.MINIGAME, stateManager);
        this.minigameCamera = camera;
        score = 0;
    }

    public void initialise()
    {
        this.minigameBatch = new SpriteBatch();
        this.minigameCamera = new OrthographicCamera();
        //        this.minigameCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //        this.minigameCamera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);


        this.minigameCamera.setToOrtho(false,420,420);
        this.minigameCamera.position.set(420, 420, 0);
        this.minigameCamera.update();
        this.engine = new Engine(0,0, AssetManager.getEngineTexture1(), 100, 15, 4, 16, 100, 4, 01);
        this.ufos = new ArrayList<>();
        this.waterBalloons = new ArrayList<>();
        this.setUpUfos();
    }

    private void setUpUfos(){
        for (int y = 10; y < 12; y++){
            for (int x = 0; x < 5; x++) {
                this.ufos.add(new UFO(x * 2, y, AssetManager.getEngineTexture1(), 60, this.ufoSpeed));
            }
        }
        this.lowestUFO = this.ufos.get(0);
        this.rightmostUFO = this.ufos.get(0);
        this.leftmostUFO = this.ufos.get(0);
    }

    public void fire(){
        if (this.fireCooldown <= 0){
            this.fireCooldown = 10;
            this.waterBalloons.add(new WaterBalloon(engine.getX(), engine.getY(), AssetManager.getWaterBalloonTexture(), 2));
        }
    }

    public void moveEngine(int direction){
        this.engine.setX(engine.getX() + 2 * direction);
    }

    public void checkIfWaterHit(ArrayList<WaterBalloon> waterBalloons, ArrayList<UFO> ufos) {
        ArrayList<WaterBalloon> waterToRemove = new ArrayList<>();
        ArrayList<UFO> ufosToRemove = new ArrayList<>();

        for (WaterBalloon waterBalloon : waterBalloons) {
            //If the water is too high up, delete it
            if (waterBalloon.isOutOfBounds(2000)){
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

    public void update() {
        if (this.minigameNotWon() && this.minigameNotLost()) {

            this.fireCooldown -= 1;

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
            } else if (rightmostUFO.getX() > 580 && this.currentDirection == UFO.Direction.RIGHT) {
                this.ufoDownstepCounter = this.ufoDownstepAmount;
                this.currentDirection = UFO.Direction.LEFT;
            }
            if (this.ufoDownstepCounter < 0) {
                for (UFO ufo : this.ufos) {
                    ufo.time += 1;
                    ufo.move(this.currentDirection);
                }
            } else {
                this.ufoDownstepCounter -= this.ufoSpeed;
                for (UFO ufo : this.ufos) {
                    ufo.time += 1;
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

    public boolean minigameNotWon(){ // TODO: should delete minigame state when done with it
        if (this.ufos.size() == 0){
            this.stateManager.changeToExistingState(StateID.GAME);
            return false;
        }
        return true;
    }

    public boolean minigameNotLost(){
        if (this.lowestUFO.getY() < 0){
            this.stateManager.changeState(new GameOverState(this.inputManager, this.font, this.stateManager, false));
            return false;
        }
        return true;
    }

    public void dispose(){

    }

    private void returnToMainGame() {}
}
