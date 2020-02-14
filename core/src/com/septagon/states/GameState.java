package com.septagon.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.septagon.entites.*;
import com.septagon.game.InputManager;
import com.septagon.game.UIManager;
import com.septagon.helperClasses.AssetManager;
import com.septagon.helperClasses.AttackerManager;
import com.septagon.helperClasses.StatusBarGenerator;
import com.septagon.helperClasses.TileManager;

import java.util.ArrayList;

/*
Child class of the State class that will manage the system when the user is in the game
 */

public class GameState extends State
{
    // we will use 32px/unit in world
    public final static float SCALE = 32f;
    public final static float INV_SCALE = 1.f/SCALE;
    // this is our "target" resolution, note that the window can be any size, it is not bound to this one
    public final static float VP_WIDTH = 640;
    public final static float VP_HEIGHT = 480;

    //Variable to keep track of whether it is the player or enemies turn
    private boolean playerTurn = true;

    //Camera that control the viewport of the game depending on input
    private OrthographicCamera camera;
    //Viewport that is used alongside the camera that contains the whole game map
    private ExtendViewport viewport;
    //Spritebatch that is used for rendering all objects in the game
    private SpriteBatch objectBatch;

    //Contains all the information about our game map
    private TiledGameMap gameMap;

    private int turnNumber, fortDestroyedAt;
    private int numForts = 6;
    private boolean paused, fortDestroyed, targetStation, stationDestroyed = false;

    //Loads textures and creates objects for the engines
    private ArrayList<Engine> engines;
    private Engine engine1;
    private Engine engine2;
    private Engine engine3;
    private Engine engine4;


    //Loads textures and creates objects for the fortresses
    private ArrayList<Fortress> fortresses;
    private Fortress fortressFire;
    private Fortress fortressStation;
    private Fortress fortressMinister;
    private Fortress fortressCliffords;
    private Fortress fortressBarbican;
    private Fortress fortressRoyalTheatre;

    //Creates objects for the aliens
    private ArrayList<Alien> aliens;
    private Alien alien1;
    private Alien alien2;
    private Alien alien3;
    private Alien alien4;

    //Loads textures and creates an object for the fire station
    public Station fireStation;

    //Keeps track of where in the game map the camera is currently
    private float currentCameraX, currentCameraY;

    //Create entityManager that will handle all entities in our game
    private EntityManager entityManager = new EntityManager();

    //These are used to help manage the input of the user when clicking our objects
    private ArrayList<Tile> tiles = new ArrayList<Tile>();

    //Creates instance of class that controls all the ui elements that stay on the screen
    private UIManager uiManager;

    //Creates an array of bullets
    public static ArrayList<Bullet> bullets;
    //private boolean shouldCreateBullets = false;

    private StatusBarGenerator statusBarGenerator;
    private TileManager tileManager;

    //Used to keep track of which fortress to move to when it is the enemies turn
    private int currentFortressIndex = 0;
    private int counter = 0;
    private boolean hasChangedFortress = false;

    //Adds a slight delay between switching turns so that it doesn't just happen straight away
    private int changeTurnCounter = 0;
    private boolean changingTurn = false;

    private AttackerManager attackerManager;

    private MinigameState minigameState = new MinigameState(this.inputManager, this.font,  stateManager, new OrthographicCamera());


    /***
     * Constructor that sets inital values for all variables and gets values of variables that are used throughout full program
     * @param inputManager The games input manager that handles all the games input
     * @param font The font being used for the game
     * @param camera The camera that controls what is displayed on the screen
     */
    public GameState(InputManager inputManager, BitmapFont font, StateManager stateManager, OrthographicCamera camera)
    {
        super(inputManager, font, StateID.GAME, stateManager);
        this.camera = camera;
        turnNumber = 0;
        currentCameraX = 0;
        currentCameraY = 0;

        bullets = new ArrayList<Bullet>();
    }


    /***
     * Sets up all objects in our game and gets the game ready to be played
     */
    public void initialise()
    {
        //Initialises all engines, fortress and stations in the game
        engine1 = new Engine(0,0, AssetManager.getEngineTexture1(), 100, 10, 3, 20, 90, 4, 01);
        engine2 = new Engine(0,0, AssetManager.getEngineTexture2(), 120, 13, 4, 12, 150, 4, 02);
        engine3 = new Engine(0, 0, AssetManager.getEngineTexture1(), 135, 12, 5, 10, 150, 4, 03 );
        engine4 = new Engine(0,0,AssetManager.getEngineTexture2(), 85,15,4,16, 100, 4, 04);
        fortressFire = new Fortress(4, 10, 256, 256, AssetManager.getFortressFireTexture(), AssetManager.getDefeatedFireTexture(), 100, 20, 4, 8, 4);
        fortressMinister = new Fortress(11, 31, 256, 256, AssetManager.getFortressMinisterTexture(), AssetManager.getDefeatedMinsterTexture(), 150, 20, 5, 8, 7);
        fortressStation = new Fortress(31, 30, 256, 256, AssetManager.getFortressStationTexture(), AssetManager.getDefeatedStationTexture(), 120, 20, 3, 8, 4);
        fortressCliffords = new Fortress(50, 46, 256, 256, AssetManager.getFortressCliffordsTower(), AssetManager.getDefeatedCliffordsTower(), 70, 30, 4, 8, 7);
        fortressBarbican = new Fortress(80, 23, 256, 256, AssetManager.getFortressBarbican(), AssetManager.getDefeatedBarbican(), 95, 25, 3, 8, 8);
        fortressRoyalTheatre = new Fortress(5, 83, 256, 256, AssetManager.getFortressRoyalTheatre(), AssetManager.getDefeatedRoyalTheatre(), 140, 18, 5, 8, 8);
        fireStation = new Station(55, 11, 256, 128, AssetManager.getFireStationTexture(), 100, 8, 4);

        //Initialises all aliens and adds them to the ArrayList of Aliens
        aliens = new ArrayList<Alien>();
        int[][] path = new int[][]{{12,5}, {16,5},{20,5},{20,9},{16,9},{12,13},{12,17},{12,21},{16,21},{20,17},{16,13},{12,9}};
        alien1 = new Alien(5,5, 32,32, AssetManager.getAlienTexture1(), 100, 5, 4, 4, 15, path, 0);;
        path = new int[][]{{25,25}, {31,25},{37,25},{43,31},{43,37},{49,37},{43,40},{37,43},{31,40},{25,37},{25,31},{25,25}};
        alien2 = new Alien(25,25, 32,32, AssetManager.getAlienTexture2(), 150, 3, 5, 6, 12, path, 1);;
        path = new int[][]{{55,55}, {60,55},{60,60},{65,65},{65,62},{68,58},{70,65},{68,65},{63,65},{65,60},{60,60},{60,55}};
        alien3 = new Alien(55,55, 32,32, AssetManager.getAlienTexture3(), 80, 8, 3, 8, 10, path, 2);
        path = new int[][]{{65,35}, {68,30},{75,30},{75,38},{75,45},{70,40},{70,35},{65,30},{58,25},{56,20},{60,28},{65,30}};
        alien4 = new Alien(65,35, 32,32, AssetManager.getAlienTexture4(), 80, 8, 3, 8, 10, path, 3);
        aliens.add(alien1);
        aliens.add(alien2);
        aliens.add(alien3);
        aliens.add(alien4);

        //Adds all the fortresses to the ArrayList of fortresses
        fortresses = new ArrayList<Fortress>();
        fortresses.add(fortressFire);
        fortresses.add(fortressMinister);
        fortresses.add(fortressStation);
        fortresses.add(fortressCliffords);
        fortresses.add(fortressBarbican);
        fortresses.add(fortressRoyalTheatre);

        //Sets the engines positions so that they start from the fireStation
        engine1.setCol(fireStation.getCol() + 5);
        engine1.setRow(fireStation.getRow() - 1);
        engine2.setCol(fireStation.getCol() + 3);
        engine2.setRow(fireStation.getRow() - 1);
        engine3.setCol(fireStation.getCol() + 1);
        engine3.setRow(fireStation.getRow() - 1);
        engine4.setCol(fireStation.getCol() + 1);
        engine4.setRow(fireStation.getRow() - 2);

        //Adds all the engines to the ArrayList of engines
        engines = new ArrayList<Engine>();
        engines.add(engine1);
        engines.add(engine2);
        engines.add(engine3);
        engines.add(engine4);

        font.getData().setScale(Gdx.graphics.getWidth() / VP_WIDTH, Gdx.graphics.getHeight() / VP_HEIGHT);

        //Adds all the entities to the entity manager so all their updating and rendering can be handled
        entityManager = new EntityManager();
        entityManager.addEntity(fireStation);
        for(Fortress f: fortresses)
        {
            entityManager.addEntity(f);
        }
        for(Engine e: engines)
        {
            entityManager.addEntity(e);
        }
        for(Alien a : aliens){
            entityManager.addEntity(a);
        }

        // Initialises the game viewport and spritebatch
        viewport = new ExtendViewport(VP_WIDTH, VP_HEIGHT, camera);
        objectBatch = new SpriteBatch();
        objectBatch.setProjectionMatrix(camera.combined);

        //Creates instance of uiManager which will be used to render and manage all UI elements
        uiManager = new UIManager(this, font);

        //Creates the gameMap instance that will be used to load the map from the tmx file
        gameMap = new TiledGameMap();

        //Intialises all entities and all ui elements
        entityManager.initialise();
        uiManager.initialise();

        //Sets up the camera parameters and moves it to its inital position
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.x = gameMap.getMapWidth() * Tile.TILE_SIZE - Gdx.graphics.getWidth() / 2;
        camera.update();

        //Create objects referring to all tiles in game
        for(int row = 0; row < gameMap.getMapHeight(); row++)
        {
            for(int col = 0; col < gameMap.getMapWidth(); col++)
            {
                if(gameMap.getTileByCoordinate(0, col, row) != null)
                    tiles.add(gameMap.getTileByCoordinate(0, col, row));
            }
        }

        //Initialises the statusBarRenderer object
        statusBarGenerator = new StatusBarGenerator(engines, fortresses, aliens, fireStation);

        //Sets up all the occupied tiles on the map so they cannot be moved to
        tileManager = new TileManager(engines, tiles, aliens, fortresses, fireStation);
        tileManager.setOccupiedTiles(gameMap);

        //Initialise the AttackerManager
        attackerManager = new AttackerManager(engines, tiles, fortresses, this);
    }

    /***
     * Update method that is called every frame and will update and move all objects if required
     */
    public void update()
    {
        this.paused = uiManager.isPaused();
        //Update the bullets
        ArrayList<Bullet> bulletToRemove = new ArrayList<Bullet>();
        for (Bullet bullet : bullets)
        {
            float deltaTime = 1 / 60f;
            bullet.update(deltaTime);
            if (bullet.remove)
                bulletToRemove.add(bullet);
        }
        bullets.removeAll(bulletToRemove);

        //If we are in the process of waiting to change turn, just wait until the timer says we swap turns
        if(changingTurn){
            changeTurnCounter++;
            if(changeTurnCounter >= 30){
                changeTurnCounter = 0;
                playerTurn = !playerTurn;
                changingTurn = false;
            }
        }
        else if(!paused && playerTurn)
        {
            playerTurnUpdate();
        //If it is the enemies turn
        }else if(!paused)
        {
            enemyTurnUpdate();
        }
    }

    /**
     * Method that handles all of the updating for the player turn
     */
    private void playerTurnUpdate(){
        //Call the update method for all entities in our game
        entityManager.update();

        //If all the engines have been moved on the current turn, make it the enemies turn and does various checks
        if (attackerManager.allEnginesMoved())
        {
            this.changingTurn = true;
            changeTurnCounter = 0;

            //Prevents the turn number from becoming too big, for the extreme case where it reaches the size limit of an int in Java
            if(turnNumber < 999) {
                turnNumber++;
            } else if (!fireStation.isDead() && !targetStation){ //Ensures the aliens can still target the station. They should've found it by turn 1000.
                targetStation = true;
            }

            //Triggers the minigame at the start of player turn 25
            if(turnNumber == 25){
                changeStateToMinigame();
            }

            //Improves fortresses every 15 turns
            if(turnNumber%15 == 0){
                for(Fortress f : fortresses){
                    System.out.println("Fortresses improving!");
                    f.improve();
                }
            }

            //Checks if the station or any fortresses have been destroyed, if no : loops fortresses to check they're still alive
            //      if yes : Remembers the current turn so the alien patrols can hunt the fire station 25 turns later
            if(!fireStation.isDead()) {
                if (!targetStation) {
                    if (!fortDestroyed) {
                        if(fortresses.size() < numForts) {
                            fortDestroyed = true;
                            fortDestroyedAt = turnNumber;
                            System.out.println("Fort Destroyed");
                        }
                    } else {
                        if (turnNumber - fortDestroyedAt >= 25) {
                            targetStation = true;
                            System.out.println("Targting Station");
                        }
                    }
                }
            } else {
                targetStation = false;
            }

            System.out.println(turnNumber);
        }

        //Updates the pointers to the current x and y positions of the camera
        currentCameraX = camera.position.x;
        currentCameraY = camera.position.y;

        //Checks if the player has destroyed all the fortresses
        boolean hasWon = true;
        for (Fortress f : fortresses)
        {
            if (f.getHealth() > 0) hasWon = false;
        }
        if (hasWon)
        {
            stateManager.changeState(new GameOverState(inputManager, font, stateManager, true));
        }

        //Checks if all the players fire engines have been destroyed
        boolean hasLost = true;
        hasLost = attackerManager.checkIfAllEnginesDead();
        if (hasLost)
        {
            stateManager.changeState(new GameOverState(inputManager, font, stateManager, false));
        }
    }

    /**
     * Method that handles all the updating that should happen on an enemies turn
     */
    private void enemyTurnUpdate(){
        boolean shouldShowFortress = false;

        //Work out what should happen if we need to display a new fortress
        if(!hasChangedFortress){
            //If all fortresses have been displayed, go back to the player turn
            if(currentFortressIndex >= fortresses.size()){
                //Updates all aliens and manages their actions
                for(Alien a: aliens){
                    if(!a.isDead()) {
                        a.move(tileManager, gameMap, engines, targetStation, this);
                        a.DamageEngineIfInRange();
                        if(targetStation) {
                            a.DamageStationIfInRange(fireStation);
                        }
                    }
                }
                for(Engine engine: engines){
                    engine.damageAliensIfInRange(aliens);
                }
                for(Alien a: aliens){
                    if(a.getHealth() <= 0){
                        a.setDead();
                    }
                }
                currentFortressIndex = 0;
                //If the fortresses have destroyed all engines, finish the game
                if(attackerManager.checkIfAllEnginesDead()){
                    stateManager.changeState(new GameOverState(inputManager, font, stateManager, false));
                    return;
                }
                attackerManager.snapToAttacker(engines.get(0), gameMap, camera);
                tileManager.resetMovableTiles();
                for(Engine e: engines){
                    e.setMoved(false);
                    e.ifInRangeFill(fireStation);
                }
                playerTurn = true;
                return;
            }
            //Get the current fortress that should be displayed
            Fortress nextFortress = fortresses.get(currentFortressIndex);

            //Work out if there is an engine near to the current fortress so we can display the fortress
            for(Engine e: engines){
                int xPosition = e.getX() + (e.getWidth() / 2) - (Gdx.graphics.getWidth() / 2);
                int yPosition = e.getY() + (e.getHeight() / 2) - (Gdx.graphics.getHeight() / 2);
                if(nextFortress.getX() >= xPosition && nextFortress.getX() <= xPosition + Gdx.graphics.getWidth() &&
                        nextFortress.getY() >= yPosition && nextFortress.getY() <= yPosition + Gdx.graphics.getHeight()){
                    shouldShowFortress = true;
                }
                else if(nextFortress.getX() + nextFortress.getWidth() >= xPosition && nextFortress.getX() +
                        nextFortress.getWidth() <= xPosition + Gdx.graphics.getWidth() && nextFortress.getY() +
                        nextFortress.getHeight() >= yPosition && nextFortress.getY() <= yPosition + Gdx.graphics.getHeight()){
                    shouldShowFortress = true;
                }
            }
            //If there is an engine near the fortress, show it and perform the fortresses attack
            if(shouldShowFortress)
            {
                attackerManager.snapToAttacker(nextFortress, gameMap, camera);
                attackerManager.BattleTurn(nextFortress);
            }
            else{
                currentFortressIndex++;
                hasChangedFortress = false;
            }
            hasChangedFortress = true;
        }
        //If we are already displaying a fortress, keep displaying until the timer has reached its limit
        else
        {
            counter++;
            if(counter >= 50){
                hasChangedFortress = false;
                currentFortressIndex++;
                counter = 0;
            }
        }
    }

    /***
     * Method that will render everything in the game each frame
     * @param batch The batch which is used for all the rendering
     */
    public void render(SpriteBatch batch)
    {
        //Clear the background to red - the colour does not reall matter
        Gdx.gl.glClearColor((float) 47/255, (float) 129/255, (float) 54/255, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Render the map and all objects for our game
        gameMap.render(camera);
        objectBatch.setProjectionMatrix(camera.combined);
        objectBatch.begin();
        entityManager.render(objectBatch);
        for (Bullet bullet : bullets) {
            bullet.render(objectBatch);
        }

        //Renderers the movement grid for the currently touched engine
        if (inputManager.isHasBeenTouched() && this.playerTurn){
            attackerManager.renderMovementGrid(objectBatch);
        }

        //Ends the drawing of all the objects for the current frame
        objectBatch.end();
        statusBarGenerator.renderBars(camera);


        //renders all the ui elements
        uiManager.render();
    }

    //Unused method that is required since this is a child of State
    public void dispose(){

    }

    /***
     * Method that handles map resizing when the window size is changed
     */
    public void hasResized()
    {
        //Checks that the change in screen size has not caused the camera to show features off the map
        //If this is the case, clamp the camera position so that it sets it back to the edge of the map
        if(camera.position.x <= (Gdx.graphics.getWidth() / 2)) {
            camera.position.x = Gdx.graphics.getWidth() / 2;
        }
        if(camera.position.y <= (Gdx.graphics.getHeight() / 2)) {
            camera.position.y = Gdx.graphics.getHeight() / 2;
        }
        if(camera.position.x >= getMapWidth() * Tile.TILE_SIZE - Gdx.graphics.getWidth() / 2){
            camera.position.x = getMapWidth() * Tile.TILE_SIZE - Gdx.graphics.getWidth() / 2;
        }
        if(camera.position.y >= getMapHeight() * Tile.TILE_SIZE - Gdx.graphics.getHeight() / 2){
            camera.position.x = getMapHeight() * Tile.TILE_SIZE - Gdx.graphics.getHeight() / 2;
        }

        //uiManager.setupPositions();
    }

    /**
     * Method which changes the current Game State to the Mini-game state
     */
    public void changeStateToMinigame(){
        this.stateManager.changeState(this.minigameState);

    }

    //Getters and setters

    public int getTurnNumber() { return turnNumber; }

    public float getCurrentCameraX()
    {
        return currentCameraX;
    }

    public float getCurrentCameraY()
    {
        return currentCameraY;
    }

    public UIManager getUiManager()
    {
        return uiManager;
    }

    public TileManager getTileManager(){
        return tileManager;
    }

    public AttackerManager getAttackerManager(){
        return attackerManager;
    }

    public Station getStation(){
        return fireStation;
    }

    public boolean isPlayerTurn()
    {
        return playerTurn;
    }

    //public boolean isShouldCreateBullets() { return shouldCreateBullets; }

    public boolean isPaused()
    {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        uiManager.setPaused(paused);
    }

    //public void setShouldCreateBullets(boolean shouldCreateBullets) { this.shouldCreateBullets = shouldCreateBullets; }

    public int getMapWidth() { return gameMap.getMapWidth(); }
    public int getMapHeight() { return gameMap.getMapHeight(); }
}