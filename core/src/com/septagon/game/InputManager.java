package com.septagon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.septagon.entites.Tile;
import com.septagon.states.*;


/**
Class used to handle all inputs from the user
 */

public class InputManager implements InputProcessor
{
    Vector3 tp = new Vector3();
    private boolean dragging;
    private OrthographicCamera camera;
    private StateManager stateManager;

    private BitmapFont font;
    private SpriteBatch batch;

    //Variables that keep track of what input has occurred and where
    private boolean hasBeenTouched = false;
    private float xCoord;
    private float yCoord;

    //For the minigame to see if the right or left buttons are being held down
    public static boolean moveRight = false;
    public static boolean moveLeft = false;

    public InputManager(OrthographicCamera camera, StateManager stateManager, BitmapFont font, SpriteBatch batch)
    {
        this.camera = camera;
        this.stateManager = stateManager;
        this.font = font;
        this.batch = batch;
    }

    /**
     * Usused method that is required since we are implementing InputProcessor
     */
    @Override public boolean mouseMoved (int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the user presses the screen with the mouse
     * @param screenX The x position of the touch
     * @param screenY The y position of the touch
     * @param button The button that the input occurred with
     * @return
     */
    @Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        //checks if the game is in the main game state
        if(stateManager.getCurrentState().getID() == State.StateID.GAME)
        {
            //Cast the currentState to a gameState so that gameState specific methods can be used
            GameState currentState = (GameState)stateManager.getCurrentState();

            if(!currentState.isPaused() && currentState.isPlayerTurn())
            {
                // ignore if its not left mouse button or first touch pointer
                if (button != Input.Buttons.LEFT || pointer > 0) return false;

                //Get the positions of the input in terms of screen coords
                hasBeenTouched = true;
                xCoord = Gdx.input.getX();
                yCoord = Gdx.input.getY();

                //Convert input coords to screen coords
                xCoord = xCoord + camera.position.x - (Gdx.graphics.getWidth() / 2);
                yCoord = (Gdx.graphics.getHeight() - yCoord) + camera.position.y - (Gdx.graphics.getHeight() / 2);

                //Create versions of the input that are kept in terms of screen coords
                float onScreenXCoord = Gdx.input.getX();
                float onScreenYCoord = Gdx.graphics.getHeight() - Gdx.input.getY();

                //Check if any of the UI elements have been pressed
                if (currentState.getUiManager().getShowStatsRect().contains(onScreenXCoord, onScreenYCoord))
                {
                    currentState.getUiManager().pressedShowStatsButton();
                }
                if (currentState.getUiManager().getMinimiseRect().contains(onScreenXCoord, onScreenYCoord))
                {
                    currentState.getUiManager().pressedMinimiseButton();
                }

                //call gamestate method that handles when places on the map are pressed
                currentState.getAttackerManager().touchedTile(xCoord, yCoord);

                dragging = true;
            }
        }
        else if(stateManager.getCurrentState().getID() == State.StateID.MENU){
            //Cast the currentState to menuState so menuState specific methods can be used
            MenuState currentState = (MenuState) stateManager.getCurrentState();

            //Call menustate method that processes a mouse press
            currentState.checkIfClickedOption(screenX, Gdx.graphics.getHeight() - screenY);
        }
        else if(stateManager.getCurrentState().getID() == State.StateID.GAME_OVER){
            //Casts the currentState to a gameOverState so gameOverState specific methods can be used
            GameOverState currentState = (GameOverState) stateManager.getCurrentState();

            //Call gameOverState method that processes a mouse press
            currentState.checkIfButtonPressed(screenX, Gdx.graphics.getHeight() - screenY);
        }
        return true;
    }

    /**
     * Checks if the user has performed a drag action on the screen
     * @param screenX The x position of where the drag finished
     * @param screenY The y position of where the drag finished
     * @return
     */
    @Override public boolean touchDragged (int screenX, int screenY, int pointer)
    {
        if(stateManager.getCurrentState().getID() == State.StateID.GAME)
        {
            //Convert currentState to gameState so that gameState specific methods can be used
            GameState currentState = (GameState) stateManager.getCurrentState();
            if(!currentState.isPaused() && currentState.isPlayerTurn())
            {
                if (!dragging) return false;

                //Get the positions of where the camera should be moved to
                float newX = camera.position.x - Gdx.input.getDeltaX();
                float newY = camera.position.y + Gdx.input.getDeltaY();

                //Check the new positions of the camera are within the screen bounds before performing the translation
                if (newX >= Gdx.graphics.getWidth() / 2 && newX <= currentState.getMapWidth() * Tile.TILE_SIZE - Gdx.graphics.getWidth() / 2)
                    camera.translate(-Gdx.input.getDeltaX(), 0, 0);

                if (newY >= Gdx.graphics.getHeight() / 2 && newY <= currentState.getMapHeight() * Tile.TILE_SIZE - Gdx.graphics.getHeight() / 2)
                    camera.translate(0, Gdx.input.getDeltaY(), 0);

                //Refresh the camera
                camera.update();
                camera.unproject(tp.set(screenX, screenY, 0));
            }
        }
        return true;
    }

    /**
     * Method called when the user releases the mouse button
     * @param screenX The x position of the input
     * @param screenY The y position of the input
     * @param button The mouse button that performed the input
     */
    @Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if(stateManager.getCurrentState().getID() == State.StateID.GAME)
        {
            if (button != Input.Buttons.LEFT || pointer > 0) return false;
            camera.unproject(tp.set(screenX, screenY, 0));
            dragging = false;
        }
        return true;
    }

    /**
     * Method calls when the user presses a key on the keyboard
     * The Mini-game section of this Method is new (line 247)
     * @param keycode The code of the key that is pressed
     */
    @Override public boolean keyDown (int keycode) {
        if (stateManager.getCurrentState().getID() == State.StateID.MENU) {
            //Casts the currentState to a menuState so menuState specific methods can be used
            MenuState currentState = (MenuState) stateManager.getCurrentState();

            //If up or down pressed, move the menuPosition accordingly
            if (keycode == Input.Keys.DOWN) {
                currentState.setMenuPosition(currentState.getMenuPosition() + 1);
            } else if (keycode == Input.Keys.UP) {
                currentState.setMenuPosition(currentState.getMenuPosition() - 1);
            }
            //If enter pressed, perform action depending on the position of the menu
            else if (keycode == Input.Keys.ENTER) {
                switch (currentState.getMenuPosition()) {
                    case 0:
                        stateManager.changeState(new GameState(this, font, stateManager, camera));
                        break;
                    case 1:
                        Gdx.app.exit();
                        break;
                    default:
                        System.err.println("Something went wrong with the menu system");
                        break;
                }
            }
        } else if (stateManager.getCurrentState().getID() == State.StateID.GAME) {
            //Cast currentState to a gameState so gameState specific methods can be used
            GameState currentState = (GameState) stateManager.getCurrentState();

            //If user presses escape, flip whether the game is paused or not
            if (keycode == Input.Keys.ESCAPE) {
                currentState.setPaused(!currentState.isPaused());
            }
            if (currentState.isPaused()) {
                //If up or down pressed, move pause position accordingly
                if (keycode == Input.Keys.DOWN && currentState.getUiManager().getPausePosition() == 1) {
                    currentState.getUiManager().setPausePosition(2);
                } else if (keycode == Input.Keys.UP && currentState.getUiManager().getPausePosition() == 2) {
                    currentState.getUiManager().setPausePosition(1);
                }

                //If enter pressed, perform action depending on where in the pause menu the user is
                if (keycode == Input.Keys.ENTER) {
                    if (currentState.getUiManager().getPausePosition() == 1) {
                        currentState.setPaused(false);
                    } else {
                        stateManager.changeState(new MenuState(this, font, stateManager, camera));
                    }
                }
            }
        }

        //Handle input for the game over state
        else if (stateManager.getCurrentState().getID() == State.StateID.GAME_OVER) {
            //Convert the currentState variable to an instance of GameOverState
            GameOverState currentState = (GameOverState) stateManager.getCurrentState();

            //Move the position of the gameOverState up or down based on inputs
            if (keycode == Input.Keys.DOWN && currentState.getPosition() == 1) {
                currentState.setPosition(2);
            } else if (keycode == Input.Keys.UP && currentState.getPosition() == 2) {
                currentState.setPosition(1);
            }
            //If the enter key is pressed, perform action based on the position
            if (keycode == Input.Keys.ENTER) {
                //If on yes, start a new GameState
                if (currentState.getPosition() == 1) {
                    stateManager.changeState(new GameState(this, font, stateManager, camera));
                }
                //If on no, close the window and exit the game
                else if (currentState.getPosition() == 2) {
                    Gdx.app.exit();
                }
            }
        } else if (stateManager.getCurrentState().getID() == State.StateID.MINIGAME) {
            MinigameState currentState = (MinigameState) stateManager.getCurrentState();
            if (currentState.isShowingInstructions()){ //If any button is pressed, hide the instructions, start the minigame
                currentState.hideInstructions();
            } else {//Other wise game is being played
                if (keycode == Input.Keys.SPACE) {
                    stateManager.changeToExistingState(State.StateID.GAME);//TODO: remove this
                }
                if (keycode == Input.Keys.RIGHT) {
                    this.moveRight = true;
                }
                if (keycode == Input.Keys.LEFT) {
                    this.moveLeft = true;
                }
                if (keycode == Input.Keys.UP) {
                    currentState.fire();
                }
            }
            }
            return true;
        }


    @Override
    public boolean keyUp (int keycode) {
        //Tells minigame to stop moving the engine
        if (keycode == Input.Keys.RIGHT){
            this.moveRight = false;
        }
        if (keycode == Input.Keys.LEFT){
            this.moveLeft = false;
        }
        return false;
    }

    @Override public boolean keyTyped (char character) {
        return false;
    }

    @Override public boolean scrolled (int amount) {
        return false;
    }

    //Getters
    public boolean isHasBeenTouched() { return hasBeenTouched; }
    public float getXCoord() { return xCoord; }
    public float getYCoord() { return yCoord; }
    public OrthographicCamera getCamera() { return camera; }
}