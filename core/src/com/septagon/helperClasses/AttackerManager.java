package com.septagon.helperClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.entites.*;
import com.septagon.states.GameState;

import java.util.ArrayList;

/**
 * Helper class that is used to handle a lot of the Attacker classes
 */

public class AttackerManager
{
    private ArrayList<Engine> engines;
    private ArrayList<Tile> tiles;
    private ArrayList<Fortress> fortresses;
    private GameState gameState;

    private Tile currentlyTouchedTile = null;
    private Tile previouslyTouchedTile = null;
    private Engine currentEngine = null;

    public AttackerManager(ArrayList<Engine> engines, ArrayList<Tile> tiles, ArrayList<Fortress> fortresses, GameState gameState){
        this.engines = engines;
        this.tiles = tiles;
        this.fortresses = fortresses;
        this.gameState = gameState;
    }

    /**
     * Method that will move the camera position to one of the attackers
     *
     * @param a The attacker which the camera should be moved to
     */
    public void snapToAttacker(Attacker a, TiledGameMap gameMap, OrthographicCamera camera)
    {
        //Get the positions of where the camera should move to
        int newCameraX = a.getX() + (a.getWidth() / 2);
        int newCameraY = a.getY() + (a.getHeight() / 2);

        //Make sure the new camera position is within the bounds of the screen
        if (newCameraX <= Gdx.graphics.getWidth() / 2)
            newCameraX = Gdx.graphics.getWidth() / 2;
        else if (newCameraX >= (gameMap.getMapWidth() * Tile.TILE_SIZE) - Gdx.graphics.getWidth() / 2)
            newCameraX = (gameMap.getMapWidth() * Tile.TILE_SIZE) - Gdx.graphics.getWidth() / 2;

        if (newCameraY <= Gdx.graphics.getHeight() / 2)
            newCameraY = Gdx.graphics.getHeight() / 2;
        else if (newCameraY >= (gameMap.getMapHeight() * Tile.TILE_SIZE) - Gdx.graphics.getHeight() / 2)
            newCameraY = (gameMap.getMapHeight() * Tile.TILE_SIZE) - Gdx.graphics.getHeight() / 2;

        //Move the camera to its new position
        camera.position.x = newCameraX;
        camera.position.y = newCameraY;
        camera.update();
    }

    /***
     * Checks if all engines have been moved or not so that the game knows when to end the players turn
     * @return boolean of whether all the engines have been moved or not
     */
    public boolean allEnginesMoved(){
        for(Engine e : engines){
            if(!e.isMoved()){
                if(!e.isDead()) {
                    return false;
                }
            }
        }
        return true;
    }

    /***
     * Check if the user has pressed on a fortress and display a bounding box if they have
     * @param x The x position of the input - in world coordinates
     * @param y The y position of the input - in world coordinates
     */
    public void checkIfTouchingFortress(float x, float y)
    {
        //Loops through all fortresses to check if any have been pressed
        for(Fortress f: fortresses)
        {
            //If the clicked on tile is within the bounds of the fortress make it selected, if not make not selected
            if(x >= f.getX() && x <= f.getX() + f.getWidth() &&
                    y >= f.getY() && y <= f.getY() + f.getHeight()) {
                f.setSelected(true);
            }
            else {
                f.setSelected(false);
            }
        }
    }

    /**
     * Method that works out is all the engines have been destroyed by the fortresses
     * @return Returns true if all engines are destroyed, false otherwise
     */
    public boolean checkIfAllEnginesDead(){
        for(Engine e: engines){
            if(!e.isDead()) return false;
        }
        return true;
    }

    /***
     * Called when the InputManager detects an input and is used to work out what tile was pressed and what should occur as a result
     * @param x X position of the input
     * @param y Y position of the input
     * @return boolean that will say if a tile has been pressed or not (true if it has been pressed)
     */
    public Boolean touchedTile(float x, float y)
    {
        //Loops through all tiles to see if it has been pressed
        for(Tile t: tiles) {
            //When we have found the tile that has been pressed, perform necessary processing
            if(t.checkIfClickedInside(x, y)) {
                //updated the pointers to the current and previous tiles
                previouslyTouchedTile = currentlyTouchedTile;
                currentlyTouchedTile = t;
                //if an engine has been previously pressed on, check on if a valid move has been pressed
                //and if so perform that move
                if (currentEngine != null) {
                    if (currentlyTouchedTile.isMovable() && !currentEngine.isMoved() && !currentEngine.isDead()) {
                        currentlyTouchedTile.setOccupied(true);
                        previouslyTouchedTile.setOccupied(false);
                        currentEngine.setX(currentlyTouchedTile.getX());
                        currentEngine.setY(currentlyTouchedTile.getY());
                        currentEngine.setMoved(true);
                        break;
                    }
                }

                //If not a moveable tile pressed, check if a fortress tile has been pressed
                checkIfTouchingFortress(x, y);
                for (Engine e: engines){
                    if (t.getCol() == e.getCol() && t.getRow() == e.getRow()) {
                        currentEngine = e;
                        gameState.getUiManager().setCurrentEngine(e);
                        gameState.getTileManager().setMovableTiles(currentEngine);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /***
     * Method that is run for the phase of the game where damage events occur (damage, filling etc) turn
     */
    public void BattleTurn(Fortress f){
        //Set the moved variable to false for each engine and then check if damages can occur
        gameState.getTileManager().resetMovableTiles();
        for (int i = 0; i < engines.size(); i++){
            engines.get(i).setMoved(false);
            engines.get(i).DamageFortressIfInRange(f);
            f.DamageEngineIfInRange(engines.get(i));
            if (engines.get(i).isDead()){
                engines.remove(engines.get(i));
                break;
            }
            if (f.isDead()){
                fortresses.remove(f);
            }
            engines.get(i).ifInRangeFill(gameState.getStation());
        }
    }

    /***
     * Renders a grid showing the player where the engine that they have pressed on can move to
     */
    public void renderMovementGrid(SpriteBatch batch){
        //If there is a engine that has been pressed and that engine has not yet moved this turn
        if(currentlyTouchedTile != null && currentEngine != null && !currentEngine.isMoved() && !currentEngine.isDead()) {
            //Draw grid around engine with all the movable spaces
            for(Tile t: tiles) {
                if (t.isMovable()) {
                    batch.draw(AssetManager.getMoveSpaceTexture(), t.getX(), t.getY(), Tile.TILE_SIZE, Tile.TILE_SIZE);
                }
            }
        }
    }
}
