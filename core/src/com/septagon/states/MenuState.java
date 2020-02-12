package com.septagon.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.septagon.game.InputManager;

import java.awt.*;

/*
Child of the State class that will be used to manage the system when the user is in the menu
 */

public class MenuState extends State
{
    //Keep track of how many elements there are in the menu
    public static final int NUM_MENU_ITEMS = 4;

    //Variables with the text that will be displayed to the screen
    private String titleLabel;
    private String playLabel;
    private String exitLabel;
    private int menuPosition;

    private GlyphLayout layout;
    private int titleCentreX;

    //Create the SpriteBatch and camera which are specific for this state
    private SpriteBatch menuBatch;
    private OrthographicCamera menuCamera;
    private OrthographicCamera gameCamera;

    //Create bounding boxes which are used to act like buttons
    private Rectangle playBox;
    private Rectangle exitBox;

    /***
     * Constructor that set initial values for all class member variables
     * @param inputManager The games InputManager class so that this class can also handle input
     * @param font The games font so that the class can draw text to the screen
     */
    public MenuState(InputManager inputManager, BitmapFont font, StateManager stateManager, OrthographicCamera camera)
    {
        super(inputManager, font, StateID.MENU, stateManager);
        this.gameCamera = camera;
        titleLabel = "Kroy - Septagon";
        playLabel = "Play";
        exitLabel = "Exit";
        menuPosition = 0;
        layout = new GlyphLayout(font, titleLabel);
    }

    /**
     * Set up all the text and positions for the menu system
     */
    public void initialise()
    {
        menuBatch = new SpriteBatch();
        menuCamera = new OrthographicCamera();
        menuCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menuCamera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        menuCamera.update();
        menuBatch.setProjectionMatrix(menuCamera.combined);

        setupRectanglePositions();
    }

    /**
     * Updates everything in the game
     */
    public void update()
    {
        menuBatch.setProjectionMatrix(menuCamera.combined);
    }

    /**
     * Draws all the text to the screen
     * @param batch
     */
    public void render(SpriteBatch batch)
    {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        menuBatch.begin();

        font.setColor(Color.WHITE);
        titleCentreX = (int)(Gdx.graphics.getWidth() / 2 - layout.width / 2);
        font.draw(menuBatch, titleLabel, titleCentreX, (Gdx.graphics.getHeight()) - 30);

        drawString(menuBatch, 0, playLabel, Gdx.graphics.getWidth() / 2, (Gdx.graphics.getHeight()) - 100);
        drawString(menuBatch, 1, exitLabel,  Gdx.graphics.getWidth() / 2, (Gdx.graphics.getHeight()) - 150);

        menuBatch.end();
    }

    /***
     * Method used to draw each individual string to the screen, based on if it is the current position or not
     * @param batch The spritebatch that is used for rendering all objects
     * @param position The position in the list of menu items that the current string is
     * @param text The text that should be rendered
     * @param x The x position of the string on the screen
     * @param y The y position of the string on the screen
     */
    private void drawString(SpriteBatch batch, int position, String text, float x, float y)
    {
        if(position == menuPosition)
        {
            font.setColor(Color.GREEN);
        }else
        {
            font.setColor(Color.WHITE);
        }

        font.draw(batch, text, x, y);
    }

    //Getters and Setters for member variable menuPosition
    public void setMenuPosition(int menuPosition)
    {
        if(menuPosition < 0 || menuPosition >= NUM_MENU_ITEMS)
            return;

        this.menuPosition = menuPosition;
    }

    /**
     * Called when the window is resized
     * @param width The new width of the screen
     * @param height The new height of the screen
     */
    public void hasResized(float width, float height){
        //Update the menuCamera with the new dimensions
        menuCamera.viewportWidth = width;
        menuCamera.viewportHeight = height;
        menuCamera.position.x = Gdx.graphics.getWidth() / 2;
        menuCamera.position.y = Gdx.graphics.getHeight() / 2;
        menuCamera.update();
    }

    /**
     * Setup the positions of the buttons
     */
    private void setupRectanglePositions(){
        playBox = new Rectangle();
        playBox.setBounds(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 130, 100, 50);

        exitBox = new Rectangle();
        exitBox.setBounds(Gdx.graphics.getWidth() / 2  + 100, Gdx.graphics.getHeight() - 200, 100,50);
    }

    /**
     * Checks if the user has pressed on any of the buttons
     * @param x The x position of the input
     * @param y The y position of the input
     */
    public void checkIfClickedOption(float x, float y){
        if(x >= playBox.x && x <= playBox.x + playBox.width){
            if(y >= playBox.y && y <= playBox.y + playBox.height){
                stateManager.changeState(new GameState(inputManager, font, stateManager, gameCamera));
            }else if(y >= exitBox.y && y <= exitBox.y + exitBox.height){
                Gdx.app.exit();
            }
        }
    }

    /**
     * Method called to dispose of all objects when the game has finished
     */
    public void dispose()
    {
        menuBatch.dispose();
    }

    //Getters
    public int getMenuPosition() { return menuPosition; }
    public OrthographicCamera getMenuCamera() { return menuCamera; }
}
